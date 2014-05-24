package com.justyour.food.server.ciqual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

public class SolrCiqual {

	static Logger logger = Logger.getLogger(SolrCiqual.class.getName());

	public static final String SOLR_CORE = "Ciqual";

	public static final String FIELD_DEFAULT = "NameTokNgram";

	public static final String FIELD_TOKEN = "NameLowercaseTokenized";

	protected EmbeddedSolrServer server = null;

	protected String core;
	protected CoreContainer container;

	public SolrCiqual(CoreContainer container, String core) {
		this.core = core;
		this.container = container;
		init();
	}

	public void init() {
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
			System.out.println("numFound=" + numFound + ", numSpellSuggestion="
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
		modifiableSolrParams.set("start", start);
		modifiableSolrParams.set("rows", rows);
		modifiableSolrParams.set("defType", "edismax");
		modifiableSolrParams.set("hl", "true");
		modifiableSolrParams.set("fl", "Details,NameLowercase");
		// not working for multiple tokens modifiableSolrParams.set("pf",
		// "NameLowercaseTokenized");
		modifiableSolrParams
				.set("qf", "NameTokNgram", "NameLowercaseTokenized");
		// modifiableSolrParams.set("boost",
		// "sum(0.01,div(Size,sum(10,Size)))");
		modifiableSolrParams.set("spellcheck", "true");
		modifiableSolrParams.set("spellcheck.q", input);
		modifiableSolrParams.set("spellcheck.extendedResults", "true");
		modifiableSolrParams.set("debug", "false");
		return modifiableSolrParams;
	}

	public ArrayList<String> getSuggestions(String input, int start, int rows) {
		return getSuggestions(input, start, rows, "Details");
	}

	public ArrayList<String> getDisplaySuggestions(String input, int start,
			int rows) {
		return getSuggestions(input, start, rows, "NameLowercase");
	}

	protected ArrayList<String> getSuggestions(String input, int start,
			int rows, String field) {
		ModifiableSolrParams modifiableSolrParams = createSolrParams(input,
				start, rows);

		QueryResponse queryResponse;
		try {
			queryResponse = server.query(modifiableSolrParams);
			long numFound = queryResponse.getResults().getNumFound();
			long numSpellSuggestion = 0;
			if (queryResponse.getSpellCheckResponse() != null
					&& queryResponse.getSpellCheckResponse().getSuggestions() != null) {
				numSpellSuggestion = queryResponse.getSpellCheckResponse()
						.getSuggestions().size();
			}
			System.out.println("numFound=" + numFound + ", numSpellSuggestion="
					+ numSpellSuggestion);

			if (numFound == 0 && numSpellSuggestion == 0) {
				// Query For DoubleMetaphone Field
				modifiableSolrParams.set("qf", "SoundRepresentation");
				queryResponse = server.query(modifiableSolrParams);
				return getLinks(queryResponse, field);
			} else if (numFound == 0 && numSpellSuggestion != 0) {
				// Query for first spelling suggestion
				// getBestSpellingSuggestion(queryResponse, input);

				// Set query string to the most likely spelling correction and
				// queries again
				Suggestion spellSug = getBestSpellingSuggestion(queryResponse,
						input);
				modifiableSolrParams
						.set("q", spellSug.getAlternatives().get(0));
				ArrayList<String> ret = getLinks(queryResponse, field);
				queryResponse = server.query(modifiableSolrParams);
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
				modifiableSolrParams
						.set("q", spellSug.getAlternatives().get(0));

				queryResponse = server.query(modifiableSolrParams);
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
		} catch (SolrServerException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public ArrayList<String> getLinks(QueryResponse response, String field) {
		ArrayList<String> ret = new ArrayList<>();

		SolrDocumentList docs = response.getResults();
		if (docs != null) {
			System.out.println(docs.getNumFound() + " documents found, "
					+ docs.size() + " returned : ");
			for (int i = 0; i < docs.size(); i++) {
				SolrDocument doc = docs.get(i);
				System.out.println("\t-" + doc.toString());
				System.out.println("## " + doc.getFieldValue(field));
				// ret.add(doc.getFieldValue("Link").toString());
				ret.add(doc.getFieldValue(field).toString());
			}
		}
		return ret;
	}

	public long getNumFound(QueryResponse response) {
		return response.getResults().getNumFound();
	}

	public void add(CiqualModel ciqual) throws SolrServerException, IOException {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("NameLowercase", ciqual.mORIGFDNM);
		doc.addField("Type", ciqual.mORIGGPFR);
		doc.addField("Details", ciqual.mORIGFDNM);
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(doc);
		server.add(docs);
	}

	/**
	 * @return - properties of the SolrReceipe Server
	 */
	public ArrayList<String> getProperties() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("Ping Server");
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
