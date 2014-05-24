package com.justyour.food.wget.nlp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.postag.POSTaggerTrainerTool;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerFactory;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;

import com.justyour.food.server.ToolsServer;
import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.Quantity;

public class IngredientNLPTest {

	/**
	 * <code>
	 * B-NP token begins of a noun phrase;
	 * I-NP token is inside a noun phrase;
	 * B-VP token begins a verb phrase;
	 * I-VP token is inside a verb phrase;
	 * O token is outside any phrase;
	 * B-PP token begins a prepositional phrase;
	 * B-ADVP token begins an adverbial phrase.
	 * </code>
	 * 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@Test
	public void analyzeIngredientsTest() throws InvalidFormatException,
			IOException {
		analyzeIngredients("sel et poivre");
		ToolsServer.wait4key();
		analyzeIngredients("sel ou poivre");
		ToolsServer.wait4key();
		analyzeIngredients("1 bouteille d'huile d'olive");
		ToolsServer.wait4key();
		// caractere a la noix !!
		analyzeIngredients("1 bouteille d’huile d’olive");
		ToolsServer.wait4key();
		analyzeIngredients("1 gousse d'ail écrasée");
		ToolsServer.wait4key();
		analyzeIngredients("persil haché");
		ToolsServer.wait4key();
		// BufferedReader br = new BufferedReader(new FileReader(
		// "data/opennlp/i.txt"));
		// String line;
		// while ((line = br.readLine()) != null) {
		// analyzeIngredients(line);
		// // ToolsServer.wait4key();
		// }
		// br.close();
	}

	public void analyzeIngredients(String sentence)
			throws InvalidFormatException, IOException {
		System.out.println("SENTENCE[" + sentence + "]");

		// Tokenizer
		TokenizerModel modelTokenizer = new TokenizerModel(new File(
				"data/opennlp/fr-token.bin"));
		TokenizerME tokenizerME = new TokenizerME(modelTokenizer);
		// for bad "apostrophe"
		sentence = sentence.replaceAll("’", "'");
		// sentence = sentence.replaceAll(":", " ");
		String[] tokens = tokenizerME.tokenize(sentence);
		// for (String string : tokens) {
		// System.out.println("TOKEN[" + string + "]");
		// }

		// pos tagger
		POSModel modelPos = new POSModel(new File("data/opennlp/fr-pos.bin"));
		// POSModel modelPos = new POSModel(new File("data/fr-mph.bin"));
		POSTaggerME postTagger = new POSTaggerME(modelPos);
		String[] tags = postTagger.tag(tokens);
		int i = 0;
		// for (String tag : tags) {
		// System.out.println("TAGS[" + tag + "] -> ["+tokens[i++]+"]");
		// }

		// chunker
		ChunkerModel modelChunker = new ChunkerModel(new File(
				"data/opennlp/fr-chunk.bin"));
		ChunkerME chunkerME = new ChunkerME(modelChunker);
		String[] chunks = chunkerME.chunk(tokens, tags);
		i = 0;
		for (String chunk : chunks) {
			System.out.println("CHUNK[" + chunk + "]: TAGS[" + tags[i] + "] ->"
					+ tokens[i]);
			i++;
		}
		chunkerME.probs();
	}

	@Test
	public void postTaggerTrainingTest() throws IOException {
		InputStream dataIn = new FileInputStream("data/opennlp/test.train");
		OutputStream modelOut = new FileOutputStream("data/opennlp/test.model");

		ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn,
				"UTF-8");
		ObjectStream<POSSample> sampleStream = new WordTagSampleStream(
				lineStream);
		POSTaggerTrainerTool trainerTool = new POSTaggerTrainerTool();
		trainerTool.run("", new String[0]);

		TrainingParameters trainParams = new TrainingParameters();
		trainParams.put(TrainingParameters.CUTOFF_PARAM, "2");
		trainParams.put("Factory", "2");
		trainParams.put("TypeNumeral", "maxent");

		POSTaggerFactory posFactory = new POSTaggerFactory();
		POSModel model = POSTaggerME.train("en", sampleStream, trainParams,
				posFactory);

		BufferedOutputStream out = new BufferedOutputStream(modelOut);
		model.serialize(out);
		out.close();
	}

	public void ingredientNLP(IngredientNLP ingredientNLP, String sentence)
			throws InvalidFormatException, IOException {
		ArrayList<IngredientQuantity> iqs = ingredientNLP
				.analyzeIngredients(sentence);
		boolean wait = false;

		for (IngredientQuantity iq : iqs) {
			String ident = "";
			do {
				System.out.println(ident
						+ ReflectionToStringBuilder.toString(iq,
								ToStringStyle.MULTI_LINE_STYLE));
				for (Quantity quantity : iq.getQuantities()) {
					System.out.println(ident
							+ ReflectionToStringBuilder.toString(quantity,
									ToStringStyle.MULTI_LINE_STYLE));

				}
				if (iq.getInterpretationLevel() < 1.0) {
					wait = true;
				}
				ident += "\t";
				iq = iq.getOrNext();
			} while (iq != null);
			if (wait) {
				ToolsServer.wait4key("!!! Maybe wrong interpretation for: ["
						+ sentence + "]");
			}
		}
	}

	@Test
	public void ingredientNLPTest() throws InvalidFormatException,
			JAXBException, IOException {
		IngredientNLP ingredientNLP = new IngredientNLP("war");

		String[] sentences = new String[] {
				"1?4 litre de jus de pomme",
				"le zeste tres mince de 1 orange lavee soigneusement",
				"1/2 verre d'eau",
				"1- pomme de type granny smith",
				"1oignon rouge",
				"1½ l d'eau chaude",
				"1,5l de vin rouge(minimum 11degrés en volume plus le vin sera fort plus la sauce sera succulente!)",
				"persil ciselé : 2 c. à soupe",
				"huile pour la friture",
				"1/2 verre d'eau",
				"1/2 sachet de levure",
				"30 g de copeaux de parmesan",
				"1 gousse d'ail écrasée",
				"1 boîte de 400 g (poids net total) de haricots verts cassegrain",
				"2 c. à soupe de sucre", "2 c. à the de sel",
				"1 bouteille d'huile d'olive" };

		for (String string : sentences) {
			ingredientNLP(ingredientNLP, string);
			ToolsServer.wait4key("Analyzed [" + string + "]");
		}

		BufferedReader br = new BufferedReader(new FileReader(
				"data/opennlp/i.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			ingredientNLP(ingredientNLP, line);
		}
		br.close();

	}
}
