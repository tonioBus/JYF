/** 
 * (c) 2013 Aquila Services
 * 
 * File: IngredientNLP.java
 * Package: com.aquilaservices.metareceipe.wget.opennlp
 * Date: 18 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.crawl.opennlp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.ExpressionTree;
import com.justyour.food.server.unity.Unities;
import com.justyour.food.server.unity.UnityJXBA;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.Numeral;
import com.justyour.food.shared.jpa.models.Quantity;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> IngredientNLP.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.opennlp</b><br/>
 * <b>Date:</b> 18 août 2013
 * 
 * @author tonio
 * 
 */
public class IngredientNLP {

	static Logger logger = Logger.getLogger(IngredientNLP.class.getName());

	protected TokenizerME tokenizerME;
	protected POSTaggerME postTagger;
	protected ChunkerME chunkerME;
	Unities unities;
	protected double interpretationLevel = 1.0;

	class Node {
		protected String item;
		protected String tag;
		protected String chunk;

		public Node(String item, String tag, String chunk) {
			super();
			this.item = item;
			this.tag = tag;
			this.chunk = chunk;
		}

		public String getItem() {
			return item;
		}

		public String getTag() {
			return tag;
		}

		public String getChunk() {
			return chunk;
		}

		public boolean isTag(String string) {
			return this.tag.equals(string);
		}

		public void setTag(String string) {
			this.tag = string;
		}

		public void setItem(String rest) {
			this.item = rest;
		}

	};

	public IngredientNLP(String path2data) throws JAXBException,
			InvalidFormatException, IOException {
		String unitPath = path2data + "/data/Unities_FR.xml";
		logger.info("Reading Unities: [" + unitPath + "] -> "
				+ (new File(unitPath)).isFile());
		unities = new Unities(unitPath);

		// Tokenizer
		String tokenizerModel = path2data + "/data/opennlp/fr-token.bin";
		logger.info("Reading Tokenizer Model: [" + tokenizerModel + "] -> "
				+ (new File(tokenizerModel)).isFile());
		TokenizerModel modelTokenizer = new TokenizerModel(new File(
				tokenizerModel));
		tokenizerME = new TokenizerME(modelTokenizer);

		// pos tagger
		String posModel = path2data + "/data/opennlp/fr-pos.bin";
		logger.info("Reading POS Model: [" + posModel + "] -> "
				+ (new File(posModel)).isFile());
		POSModel modelPos = new POSModel(new File(posModel));
		postTagger = new POSTaggerME(modelPos);

		// chunker
		String chunkerModel = path2data + "/data/opennlp/fr-chunk.bin";
		logger.info("Reading Chunker Model: [" + chunkerModel + "] -> "
				+ (new File(chunkerModel)).isFile());
		ChunkerModel modelChunker = new ChunkerModel(new File(chunkerModel));
		chunkerME = new ChunkerME(modelChunker);
	}

	protected String helpString4NLP(String sz) {
		// for bad "apostrophe"
		String ret = sz.replaceAll("`", "'");
		// isolate "apostrophe"
		ret = ret.replaceAll("'", " ' ");
		return ret;

	}

	public ArrayList<IngredientQuantity> analyzeIngredients(String sentence)
			throws InvalidFormatException, IOException {
		logger.info("SENTENCE[" + sentence + "]");

		sentence = helpString4NLP(sentence);

		// Tokens
		String[] tokens = tokenizerME.tokenize(sentence);

		// pos tagger
		String[] tags = postTagger.tag(tokens);

		// chunker
		String[] chunks = chunkerME.chunk(tokens, tags);
		int i = 0;
		Node[] nodes = new Node[chunks.length];

		if (logger.isLoggable(Level.INFO)) {
			String debugSz = "";
			for (String chunk : chunks) {
				debugSz += "CHUNK[" + chunk + "]: TAGS[" + tags[i] + "] -> ["
						+ tokens[i] + "]\n";
				nodes[i] = new Node(tokens[i], tags[i], chunk);
				i++;
			}
			logger.info(debugSz);
		}
		// chunkerME.probs();
		return semantiqueAnalyze(nodes, sentence);
	}

	/**
	 * @param nodes
	 * @param details
	 * @return
	 */
	private ArrayList<IngredientQuantity> semantiqueAnalyze(Node[] nodes,
			String details) {
		ArrayList<IngredientQuantity> ret = new ArrayList<>();
		String ingredient = new String();
		String adjectif = new String();
		IngredientQuantity iq = new IngredientQuantity();
		iq.setDetails(details);
		int i = 0;
		Quantity quantity = new Quantity();
		interpretationLevel = 1.0;

		while (i < nodes.length) {
			// pseudo state 0
			Node node = nodes[i++];
			// NC - NOUNS
			if (node.isTag("NC")) {
				UnityJXBA unity = this.unities.getUnity(node.item);
				if (unity == null) {
					ingredient += node.item + " ";
				} else {
					if (quantity.getUnity() != null) {
						ingredient += node.item + " ";
					} else {
						quantity.setUnity(unity.getName());
						iq.getQuantities().add(quantity);
						quantity = new Quantity();
					}
				}
				// DET - DIGITAL
			} else if (node.isTag("DET")) {
				String rest = null;
				if ((rest = setQuantity(node, quantity, false)) != null) {
					// it should be a NC
					this.interpretationLevel -= 0.1;
					node.setTag("NC");
					node.setItem(rest);
					i--;
				}
				// CC - COORDINATION CONJONCTION, ie; et ou
			} else if (node.isTag("CC")) {
				if (node.getItem().equals("et") || node.getItem().equals("ou")) {
					iq.setIngredientSz(ingredient);
					iq.setAdjectif(adjectif);
					iq.setInterpretationLevel(interpretationLevel);
					interpretationLevel = 1.0;
					if (quantity.isEmpty() == false) {
						iq.getQuantities().add(quantity);
						quantity = new Quantity();
					}
					if (ret.size() == 0
							|| ret.get(ret.size() - 1).getOrNext() == null)
						ret.add(iq);
					ingredient = new String();
					iq = new IngredientQuantity();
					adjectif = new String();
					iq.setDetails(details);
					if (node.getItem().equals("ou")) {
						ret.get(ret.size() - 1).setOrNext(iq);
					}
				}
				// PUNCTUATION
			} else if (node.isTag("PONCT")) {
				if (node.getItem().equals(",")) {
					iq.setIngredientSz(ingredient);
					iq.setAdjectif(adjectif);
					iq.setInterpretationLevel(interpretationLevel);
					interpretationLevel = 1.0;
					if (quantity.isEmpty() == false) {
						iq.getQuantities().add(quantity);
						quantity = new Quantity();
					}
					if (ret.size() == 0
							|| ret.get(ret.size() - 1).getOrNext() == null)
						ret.add(iq);
					ingredient = new String();
					adjectif = new String();
					iq = new IngredientQuantity();
					iq.setDetails(details);
				} else if (node.getItem().equals("(")) {
					try {
						while (!(node.isTag("PONCT") && nodes[i].getItem()
								.equals(")")))
							i++;
					} catch (ArrayIndexOutOfBoundsException e) {
						break;
					}
				}
				// PROPOSITION
			} else if (node.isTag("P")) {
				if (ingredient.length() > 0) {
					ingredient += node.getItem() + " ";
					// } else {
					// this.interpretationLevel -= 0.1;
				}
				// NPP NOUN after PROPOSITION
			} else if (node.isTag("NPP")) {
				ingredient += node.getItem() + " ";
			} else if (node.isTag("ADJ")) {
				if (setQuantity(node, quantity, true) != null) {
					adjectif += node.getItem() + " ";
				}
			}
		}
		if (ingredient.trim().length() > 0)
			iq.setIngredientSz(ingredient);
		else
			interpretationLevel -= 0.2;
		iq.setAdjectif(adjectif);
		iq.setInterpretationLevel(interpretationLevel);
		if (quantity.isEmpty() == false) {
			iq.getQuantities().add(quantity);
		}
		if (ret.size() == 0 || ret.get(ret.size() - 1).getOrNext() == null)
			ret.add(iq);
		return ret;
	}

	/**
	 * @param node
	 * @param quantity
	 */
	private String setQuantity(Node node, Quantity quantity, boolean adj) {
		String valueSz = node.getItem();
		int endIndex = valueSz.length() - 1;
		int temp;

		// replace ',' by '.'
		valueSz = valueSz.replaceAll(",", ".");
		
		valueSz = valueSz.replaceAll("¼", "+0.25");
		valueSz = valueSz.replaceAll("½", "+0.5");
		valueSz = valueSz.replaceAll("¾", "+0.75");
		valueSz = valueSz.replaceAll("⅐", "+0.14285");
		valueSz = valueSz.replaceAll("⅑", "+0.11111");
		valueSz = valueSz.replaceAll("⅒", "+0.1");
		valueSz = valueSz.replaceAll("⅓", "+0.33333");
		valueSz = valueSz.replaceAll("⅔", "+0.66666");
		valueSz = valueSz.replaceAll("⅕", "+0.2");
		valueSz = valueSz.replaceAll("⅖", "+0.4");
		valueSz = valueSz.replaceAll("⅗", "+0.6");
		valueSz = valueSz.replaceAll("⅘", "+0.8");
		valueSz = valueSz.replaceAll("⅙", "+0.16666");
		valueSz = valueSz.replaceAll("⅛", "+0.125");
		valueSz = valueSz.replaceAll("⅜", "+0.375");
		valueSz = valueSz.replaceAll("⅝", "+0.625");
		valueSz = valueSz.replaceAll("⅞", "+0.875");

		valueSz = valueSz.replaceAll("1[//?]2", "+0.5"); 
		valueSz = valueSz.replaceAll("1[//?]3", "+0.333"); 
		valueSz = valueSz.replaceAll("1[//?]4", "+0.25"); 
		valueSz = valueSz.replaceAll("1[//?]5", "+0.2"); 

		if (valueSz.endsWith("-"))
			valueSz = valueSz.substring(0, endIndex);

		for (;;) {
			try {
				logger.info("ExpressionTree.parse("+valueSz+")");
				Expression expression = ExpressionTree.parse(valueSz);
				double value = expression.eval(null, null);
				Numeral numeral = new Numeral();
				numeral.setType(Numeral.TypeNumeral.DOUBLE);
				numeral.setNumber(value);
				quantity.setNumeral(numeral);
				return null;
			} catch (NullPointerException e) {
				logger.log(Level.SEVERE, "Can not evaluate [" + valueSz + "]",
						e);
				if (valueSz.trim().equalsIgnoreCase("quelques")) {
					Numeral numeral = new Numeral();
					numeral.setType(Numeral.TypeNumeral.SOME);
					quantity.setNumeral(numeral);
					return null;
				} else if ((temp = convertNumberFR(valueSz)) != -1) {
					Numeral numeral = new Numeral();
					numeral.setType(Numeral.TypeNumeral.DOUBLE);
					numeral.setNumber(temp);
					quantity.setNumeral(numeral);
					return null;
				}
				if (adj == false)
					this.interpretationLevel -= 0.1;
				return valueSz;
			} catch (ExpressionParseException e) {
				logger.log(Level.SEVERE, "Can not evaluate [" + valueSz
						+ "] at index:" + endIndex, e);
				this.interpretationLevel -= 0.2;
				endIndex = e.getIndex();
				if (endIndex == 0)
					return valueSz;
				String secondItem = valueSz.substring(endIndex);
				valueSz = valueSz.substring(0, endIndex);
				UnityJXBA unity = this.unities.getUnity(secondItem);
				logger.info("quantity:"
						+ ReflectionToStringBuilder.toString(quantity,
								ToStringStyle.DEFAULT_STYLE));
				logger.info("value to parse:" + valueSz);
				if (unity == null)
					return valueSz;
				quantity.setUnity(unity.getName());
			}
		}
	}

	private static final String[] uniteNames = { "", "un", "deux", "trois",
			"quatre", "cinq", "six", "sept", "huit", "neuf", "dix", "onze",
			"douze", "treize", "quatorze", "quinze", "seize", "dix-sept",
			"dix-huit", "dix-neuf" };

	static HashMap<String, Integer> word2Number_FR = new HashMap<String, Integer>();

	static {
		int i = 0;
		for (String numberSz : uniteNames) {
			word2Number_FR.put(numberSz, i);
			i++;
		}
	}

	/**
	 * TODO, all possible hand written number
	 * 
	 * @param numberSz
	 * @return
	 */
	public int convertNumberFR(String numberSz) {
		Integer i = word2Number_FR.get(numberSz);
		if (i != null)
			return i;
		else
			return -1;
	}
}
