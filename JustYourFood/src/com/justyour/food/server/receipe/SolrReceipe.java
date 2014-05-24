package com.justyour.food.server.receipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.CoreContainer;

import com.justyour.food.shared.jpa.models.ReceipeModel;

public class SolrReceipe {

	static Logger logger = Logger.getLogger(SolrReceipe.class.getName());

	public static final String SOLR_CORE = "JYF-Receipes";

	protected static final String FIELD_DEFAULT = "NameTokNgram";

	protected static final String FIELD_TOKEN = "NameLowercaseTokenized";

	protected EmbeddedSolrServer server = null;

	protected String core;
	protected CoreContainer container;

	public SolrReceipe(CoreContainer container, String core) {
		this.core = core;
		this.container = container;
		init();
	}

	public synchronized void init() {
		try {
			server = new EmbeddedSolrServer(container, core);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error when opening a channel to\n --> ["
					+ this.container.getSolrHome() + " core=" + core + "]", t);
			return;
		}
	}
	
	public void close() {
		server.shutdown();
	}

	/**
	 * This method picks up the most likely spelling suggestion (The one with
	 * the most tokens) Example: "exxson mobile" => "exxon", "exxon mobil"
	 * "exxon mobil" will be picked up, because its more likely that the user
	 * was looking for this term
	 * 
	 * @param qr
	 * @param searchString
	 * @return
	 */
	private static Suggestion getBestSpellingSuggestion(QueryResponse qr,
			String searchString) {
		List<Suggestion> suggestions = qr.getSpellCheckResponse()
				.getSuggestions();

		int maxToken = 0;
		Suggestion picked = null;

		for (Suggestion sug : suggestions) {
			String sugToken = sug.getToken();
			// If a suggestion exists for the search term (usually the case)
			if (sugToken.equalsIgnoreCase(searchString)) {
				picked = sug;
				break;
			} else {
				// Else pick the suggestion for the longest part of the input
				// string (most tokens)
				int tokens = sugToken.split(" ").length;
				if (tokens > maxToken) {
					maxToken = tokens;
					picked = sug;
				}
			}
		}
		return picked;
	}

	/**
	 * @param input
	 *            - "*:*" to get all document
	 * @return - the number of suggestions
	 */
	public long getNumberOfSuggestions(String input) {
		ModifiableSolrParams modifiableSolrParams = createSolrParams(input, 0,
				0);
		long ret = 0;
		QueryResponse queryResponse;

		try {
			queryResponse = server.query(modifiableSolrParams);
			long numFound = getNumFound(queryResponse);
			ret = numFound;
			long numSpellSuggestion = 0;
			if (queryResponse.getSpellCheckResponse() != null) {
				numSpellSuggestion = queryResponse.getSpellCheckResponse()
						.getSuggestions().size();
			}
			logger.info("numFound=" + numFound + ", numSpellSuggestion="
					+ numSpellSuggestion);

			if (numFound == 0 && numSpellSuggestion == 0) {
				// Query For DoubleMetaphone Field
				modifiableSolrParams.set("qf", "SoundRepresentation");
				queryResponse = server.query(modifiableSolrParams);
				return getNumFound(queryResponse);
			} else if (numFound == 0 && numSpellSuggestion != 0) {
				// Query for first spelling suggestion
				// getBestSpellingSuggestion(queryResponse, input);

				// Set query string to the most likely spelling correction and
				// queries again
				Suggestion spellSug = getBestSpellingSuggestion(queryResponse,
						input);
				modifiableSolrParams
						.set("q", spellSug.getAlternatives().get(0));
				queryResponse = server.query(modifiableSolrParams);
				ret = getNumFound(queryResponse);
				return ret;
			} else if (numSpellSuggestion != 0) {
				// Query for first spelling suggestion
				// getBestSpellingSuggestion(queryResponse, input);

				// Set query string to the most likely spelling correction and
				// queries again
				Suggestion spellSug = getBestSpellingSuggestion(queryResponse,
						input);

				modifiableSolrParams
						.set("q", spellSug.getAlternatives().get(0));

				queryResponse = server.query(modifiableSolrParams);
				ret += getNumFound(queryResponse);
				return ret;
			}
		} catch (SolrServerException e) {
			logger.severe("Erreur in getNumberOfSuggestions(" + input + "):"
					+ e);
			logger.log(Level.SEVERE, "Error in getNumberOfSuggestions", e);
			return 0;
		}
		return ret;
	}

	protected ModifiableSolrParams createSolrParams(String input, int start,
			int rows) {
		ModifiableSolrParams modifiableSolrParams = new ModifiableSolrParams();
		modifiableSolrParams.set("q", input);
		modifiableSolrParams.set("wt", "xml");
		if (start >= 0)
			modifiableSolrParams.set("start", start);
		if (rows >= 0)
			modifiableSolrParams.set("rows", rows);
		modifiableSolrParams.set("defType", "edismax");
		modifiableSolrParams.set("hl", "true");
		modifiableSolrParams.set("fl", "Link,NameLowercase");
		modifiableSolrParams.set("qf", "NameTokNgram",
				"NameLowercaseTokenized", "Instructions", "Link", "Difficulty");
		modifiableSolrParams.set("spellcheck", "true");
		modifiableSolrParams.set("spellcheck.q", input);
		modifiableSolrParams.set("spellcheck.extendedResults", "true");
		modifiableSolrParams.set("debug", "false");
		return modifiableSolrParams;
	}

	public ArrayList<String> getSuggestions(String input, int start, int rows)
			throws Exception {
		return getSuggestions(input, start, rows, "Link");
	}

	public ArrayList<String> getDisplaySuggestions(String input, int start,
			int rows) throws Exception {
		return getSuggestions(input, start, rows, "NameLowercase");
	}

	protected ArrayList<String> getSuggestions(String input, int start,
			int rows, String field) {
		ModifiableSolrParams modifiableSolrParams = createSolrParams(input,
				start, rows);

		QueryResponse queryResponse;
		try {
			queryResponse = server.query(modifiableSolrParams);
		} catch (SolrServerException e) {
			return null;
		}
		long numFound = queryResponse.getResults().getNumFound();
		long numSpellSuggestion = 0;
		if (queryResponse.getSpellCheckResponse() != null
				&& queryResponse.getSpellCheckResponse().getSuggestions() != null) {
			numSpellSuggestion = queryResponse.getSpellCheckResponse()
					.getSuggestions().size();
		}
		logger.info("numFound=" + numFound + ", numSpellSuggestion="
				+ numSpellSuggestion);

		if (numFound == 0 && numSpellSuggestion == 0) {
			// Query For DoubleMetaphone Field
			modifiableSolrParams.set("qf", "SoundRepresentation");
			try {
				queryResponse = server.query(modifiableSolrParams);
			} catch (SolrServerException e) {
				e.printStackTrace();
				return null;
			}
			return getLinks(queryResponse, field);
		} else if (numFound == 0 && numSpellSuggestion != 0) {
			// Query for first spelling suggestion
			// getBestSpellingSuggestion(queryResponse, input);

			// Set query string to the most likely spelling correction and
			// queries again
			Suggestion spellSug = getBestSpellingSuggestion(queryResponse,
					input);
			modifiableSolrParams.set("q", spellSug.getAlternatives().get(0));
			ArrayList<String> ret = getLinks(queryResponse, field);
			try {
				queryResponse = server.query(modifiableSolrParams);
			} catch (SolrServerException e) {
				return null;
			}
			ArrayList<String> newRet = getLinks(queryResponse, field);
			ret.addAll(newRet);
			ret.addAll(ret.subList(0, rows - 1));
			return ret;
		} else if (numFound < (rows - 1) && numSpellSuggestion != 0) {
			// If not a full result list is available, fill it with spell
			// recommendations
			ArrayList<String> ret = getLinks(queryResponse, field);

			// Query for first spelling suggestion
			// getBestSpellingSuggestion(queryResponse, input);

			// Set query string to the most likely spelling correction and
			// queries again
			Suggestion spellSug = getBestSpellingSuggestion(queryResponse,
					input);
			modifiableSolrParams.set("q", spellSug.getAlternatives().get(0));

			try {
				queryResponse = server.query(modifiableSolrParams);
			} catch (SolrServerException e) {
				e.printStackTrace();
				return null;
			}
			ArrayList<String> newRet = getLinks(queryResponse, field);
			int lenRest = (int) (rows - 1 - numFound);
			if (lenRest < newRet.size())
				ret.addAll(newRet.subList(0, lenRest));
			else
				ret.addAll(newRet);
			return ret;
		} else {
			return getLinks(queryResponse, field);
		}
	}

	public ArrayList<String> getLinks(QueryResponse response, String field) {
		ArrayList<String> ret = new ArrayList<>();

		SolrDocumentList docs = response.getResults();
		if (docs != null) {
			logger.info(docs.getNumFound() + " documents found, " + docs.size()
					+ " returned : ");
			for (int i = 0; i < docs.size(); i++) {
				SolrDocument doc = docs.get(i);
				logger.warning("\t-" + doc.toString());
				logger.warning("## " + doc.getFieldValue(field));
				if (doc.getFieldValue(field) == null) {
					logger.warning("SOLR record incorrect:\n"
							+ ToStringBuilder.reflectionToString(doc));
				} else {
					String ff = "NameLowercase";
					logger.warning(ff + "=["
							+ doc.getFieldValue(field).toString() + "]");
					ff = "Link";
					logger.warning(ff + "=["
							+ doc.getFieldValue(field).toString() + "]");
					ff = "Instructions";
					logger.warning(ff + "=["
							+ doc.getFieldValue(field).toString() + "]");
					ff = "Difficulty";
					logger.warning(ff + "=["
							+ doc.getFieldValue(field).toString() + "]");
					ret.add(doc.getFieldValue(field).toString());
				}
			}
		}
		return ret;
	}

	public synchronized void add(ReceipeModel receipe)
			throws SolrServerException, IOException {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("NameLowercase", receipe.getTitle());
		doc.addField("Link", receipe.getLink());
		doc.addField("Instructions", receipe.getInstructions());
		doc.addField("Difficulty", receipe.getDifficultySentence());
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(doc);
		server.add(docs);
	}

	public void remove(String link) throws SolrServerException, IOException {
		server.deleteById(link);
	}

	public synchronized int fullImport() throws SolrServerException,
			IOException {
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/dataimport");
		params.set("command", "full-import");
		QueryResponse response = server.query(params);
		commit();
		return response.getResponse().size();
	}

	public synchronized void clean() throws SolrServerException, IOException {
		server.deleteByQuery("*:*");
		server.commit();
	}

	public synchronized void commit() throws SolrServerException, IOException {
		server.commit();
	}

	public synchronized void optimize() throws SolrServerException, IOException {
		server.commit();
		server.optimize();
	}

	public long getNumFound(QueryResponse response) {
		return response.getResults().getNumFound();
	}

	/**
	 * @return - properties of the Solr Server
	 */
	public ArrayList<String> getProperties() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("Base URL");
		ret.add(this.server.getCoreContainer().getSolrHome());
		SolrPingResponse response;
		try {
			response = server.ping();
			ret.add(response.toString());
		} catch (Exception e) {
			ret.add(e.getMessage());
		}
		return ret;
	}
}
