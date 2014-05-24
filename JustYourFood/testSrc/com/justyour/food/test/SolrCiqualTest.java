/**
 * 
 */
package com.justyour.food.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.solr.core.CoreContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.JYFServletContext;
import com.justyour.food.server.ciqual.SolrCiqual;

/**1
 * @author tonio
 *
 */
public class SolrCiqualTest {

	SolrCiqual solrCiqual;
	String text = "poulet";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		JYFServletContext servlet = new JYFServletContext();
		servlet.initJYFSerletContext(null);
		CoreContainer container = new CoreContainer(JYFServletContext.getParam()
				.getSolrHome());
		container.load();
		solrCiqual = new SolrCiqual(container, JYFServletContext.getParam()
				.getSolrCiqualCore());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetNumberOfSuggestions() {
		long ret = solrCiqual.getNumberOfSuggestions(text);
		System.out.println("return suggestions for [" + text + "] = " + ret);
		assertTrue(ret >0);
	}

	@Test
	public void testGetSuggestionsStringIntInt() {
		ArrayList<String> ret = solrCiqual.getSuggestions(text, 1, 10);
		System.out.println("return length:"+ret.size());
		assertTrue(ret.size()==10);
	}

}
