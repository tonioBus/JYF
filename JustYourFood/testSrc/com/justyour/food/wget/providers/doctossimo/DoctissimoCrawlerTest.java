/**
 * File: DoctissimoTest.java
 * Package: com.aquilaservices.metareceipe.wget.providers.cuisinezaz
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.wget.providers.doctossimo;

import java.util.Properties;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.JYFServletContext;
import com.justyour.food.server.ToolsServer;
import com.justyour.food.server.crawl.IWebCrawlerProvider;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> DoctissimoTest.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.providers.cuisinezaz</b><br/>
 * <b>Date:</b> 05/08/2013
 * 
 * @author tonio
 * 
 */
public class DoctissimoCrawlerTest {

	static Logger logger = Logger.getLogger(DoctissimoCrawlerTest.class.getName());

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		JYFServletContext.getSolrReceipe().clean();
		// JPAManager jpaManager = new JPAManager();
		// IWgetProvider doctissimo = new DoctissimoCrawlerProvider();
		Properties prop = new Properties();
		prop.setProperty(IWebCrawlerProvider.LIMIT, "1");
		prop.setProperty(IWebCrawlerProvider.URLS,
				"http://recettes.doctissimo.fr");
		prop.setProperty(IWebCrawlerProvider.PATTERN, ".htm");
		System.out
				.println("You are about to fetch data from: recettes.doctissimo.fr");
		// doctissimo.init(null, prop);
		
		ToolsServer.wait4key();
		// doctissimo.close();
		// jpaManager.close();
	}

}
