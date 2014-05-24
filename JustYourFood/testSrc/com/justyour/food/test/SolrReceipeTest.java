package com.justyour.food.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.core.CoreContainer;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.JYFServletContext;
import com.justyour.food.server.receipe.SolrReceipe;
import com.justyour.food.shared.jpa.models.ReceipeModel;

public class SolrReceipeTest {

	SolrReceipe solrReceipe;
	String text = "poulet";
	boolean init = false;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		if (init == false) {
			JYFServletContext servlet = new JYFServletContext();
			servlet.initJYFSerletContext(null);
			CoreContainer container = new CoreContainer(JYFServletContext.getParam()
					.getSolrHome());
			container.load();
			solrReceipe = new SolrReceipe(container, JYFServletContext.getParam()
					.getSolrReceipeCore());
			init = true;
		}
	}

	@Test
	public void testGetNumberOfSuggestions() throws InvalidFormatException,
			JAXBException, IOException {
		long ret = solrReceipe.getNumberOfSuggestions(text);
		System.out.println("return suggestions for [" + text + "] = " + ret);
		assertTrue(ret > 0);
	}

	@Test
	public void testGetSuggestionsStringIntInt() throws Exception {
		ArrayList<String> ret = solrReceipe.getSuggestions(text, 1, 10);
		System.out.println("return length:" + ret.size());
		assertTrue(ret.size() == 10);
	}

	public class SolrThread extends Thread {
		final int LIMIT = 100;
		String key;

		public SolrThread(String key) {
			this.key = key;
		}

		public void run() {
			System.out.println("Hello from a thread!");
			for (int i = 0; i < LIMIT; i++) {
				long ret = solrReceipe.getNumberOfSuggestions("*.*");
				System.out.println("return suggestions for [" + text + "] = "
						+ ret);
				ArrayList<String> ret1;
				try {
					ret1 = solrReceipe.getSuggestions(key, 1, 10);
					System.out.println("return length:" + ret1.size());
				} catch (Exception e) {
					e.printStackTrace();
				}

				ReceipeModel receipe = ReceipeModel.getDummyReceipe(
						"Recette numero_" + i, "http://justyourfood#recette_"
								+ i);
				try {
					solrReceipe.add(receipe);
				} catch (SolrServerException | IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Test
	public void testMeshoui() throws Exception {
		final int NUMBER_THREAD = 10;
		SolrThread t[] = new SolrThread[NUMBER_THREAD];

		for (int i = 0; i < NUMBER_THREAD; i++) {
			t[i] = new SolrThread("a" + i);
		}
		for (int i = 0; i < NUMBER_THREAD; i++) {
			t[i].start();
		}

		boolean done = true;
		do {
			Thread.sleep(1000);
			System.out.print(".");
			for (int i = 0; i < NUMBER_THREAD; i++) {
				if (t[i].isAlive()) {
					done = false;
					break;
				}
			}

		} while (done == false);
		System.out.println("Waiting for a key ...");
	}
}
