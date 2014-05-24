/**
 * File: DoctissimoTest.java
 * Package: com.aquilaservices.metareceipe.wget.providers.cuisinezaz
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.server.crawl.providers;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.CrawlerManagement;
import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.server.JYFServletContext;
import com.justyour.food.server.crawl.Dumper;
import com.justyour.food.server.crawl.IWebCrawlerProvider;
import com.justyour.food.server.crawl.WebCrawler4J;
import com.justyour.food.server.crawl.WebCrawlerProvider;
import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.AdminInfos;

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
public class AllCrawlersTest {

	static Logger logger = Logger.getLogger(AllCrawlersTest.class.getName());

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
	public void cleanSolr() throws SolrServerException, IOException {
		JYFServiceImpl serverImpl = JYFServiceImpl.getInstance();
		JYFServletContext.getSolrReceipe().clean();
	}

	@Test
	public void testJYFStartCrawl() throws Exception {
		// init
		JYFServletContext sc = new JYFServletContext();
		sc.initJYFSerletContext("/justyour.com/");
		JYFServiceImpl server = new JYFServiceImpl();
		String className = "com.justyour.food.server.crawl.providers.DoctissimoDumper";
		String urls = "http://recettes.doctissimo.fr";
		int politnessDelay = 1000;
		int numberCrawls = 1;
		int maxDepthCrawling = 3;
		int maxPagesToFetch = -1;
		int maxAddition = 1;
		int maxVisiting = -1;
		server.startCrawl(className, urls, politnessDelay, numberCrawls,
				maxDepthCrawling, maxPagesToFetch, maxAddition, maxVisiting);
		AdminInfos adminInfos;
		do {
			adminInfos = server.pullAdminInfo();
			System.out.println("VISITED : "+adminInfos.dumperInfos[0].getVisited());
			System.out.println("ADDED   : "+adminInfos.dumperInfos[0].getAdded());
			Thread.sleep(10000);
		} while( adminInfos.dumperInfos[0].isFinish()==false);
	}

	@Test
	public void testStartCrawl() throws Exception {
		// init
		JYFServletContext sc = new JYFServletContext();
		sc.initJYFSerletContext("/justyour.com/");
		new JYFServiceImpl();
		CrawlerManagement crawlerManagement = new CrawlerManagement();

		// doctissimo
		String url1 = "http://recettes.doctissimo.fr";
		// Dumper class:
		// com.justyour.food.server.crawl.providers.DoctissimoDumper
		crawlerManagement.startCrawl(DoctissimoDumper.class.getName(), url1,
				1000, 5, 3, -1, 1, -1);

		// cuisineaz
		String url2 = "http://www.cuisineaz.com/";
		// Dumper class:
		// com.justyour.food.server.crawl.providers.CuisinezAzDumper
		crawlerManagement.startCrawl(CuisinezAzDumper.class.getName(), url2,
				1000, 5, 3, -1, 1, -1);

		while (crawlerManagement.allFinished() == false) {
			System.out.println("stat[" + url1 + "]="
					+ crawlerManagement.getDumperInfos(url1));
			System.out.println("stat[" + url2 + "]="
					+ crawlerManagement.getDumperInfos(url2));
			Thread.sleep(250);
		}

		// Closing
		crawlerManagement.stopCrawl(url1);
		crawlerManagement.stopCrawl(url2);
	}

	// Doctissimo
	// ////////////////////////////////////////////////////////////
	@Test
	public void testDoctissimo() throws Exception {
		JYFServletContext sc = new JYFServletContext();
		sc.initJYFSerletContext("/justyour.com/");
		new JYFServiceImpl();

		Properties prop = new Properties();
		String url = "http://recettes.doctissimo.fr";
		prop.setProperty(IWebCrawlerProvider.URLS, url);
		prop.setProperty(IWebCrawlerProvider.POLITNESS_DELAY, "1000");
		prop.setProperty(IWebCrawlerProvider.NUMBER_CRAWLS, "5");
		prop.setProperty(IWebCrawlerProvider.MAX_DEPTH_CRAWLING, "5");
		prop.setProperty(IWebCrawlerProvider.MAX_PAGES_TO_FETCH, "-1");
		IngredientNLP ingredientNLP = new IngredientNLP("war");
		JPAManager jpaManager = JYFServiceImpl.getInstance().getJpaManager();
		Dumper dumper = new DoctissimoDumper(DoctissimoDumper.defaultDomain,
				ingredientNLP, jpaManager, -1, -1);
		// DoctissimoWebCrawler.init(new DoctissimoDumper(ingredientNLP,
		// jpaManager), url);
		WebCrawlerProvider provider = new WebCrawlerProvider();
		provider.init(WebCrawler4J.class, dumper, prop);

		do {
			Thread.sleep(1000);
		} while (!provider.isFinished());
		provider.close();
	}

	// CUISINEAZ
	// ////////////////////////////////////////////////////////////
	@Test
	public void testCuisinezAz() throws Exception {
		JYFServletContext sc = new JYFServletContext();
		sc.initJYFSerletContext("/justyour.com/");
		new JYFServiceImpl();

		Properties prop = new Properties();
		String url = "http://www.cuisineaz.com/";
		prop.setProperty(IWebCrawlerProvider.URLS, url);
		prop.setProperty(IWebCrawlerProvider.POLITNESS_DELAY, "1000");
		prop.setProperty(IWebCrawlerProvider.NUMBER_CRAWLS, "5");
		prop.setProperty(IWebCrawlerProvider.MAX_DEPTH_CRAWLING, "2");
		prop.setProperty(IWebCrawlerProvider.MAX_PAGES_TO_FETCH, "-1");
		IngredientNLP ingredientNLP = new IngredientNLP("war");
		JPAManager jpaManager = JYFServiceImpl.getInstance().getJpaManager();
		Dumper dumper = new CuisinezAzDumper(CuisinezAzDumper.defaultDomain,
				ingredientNLP, jpaManager, 1, -1);
		// CuisinezAzWebCrawler.init(new CuisinezAzDumper(ingredientNLP,
		// jpaManager), url);
		WebCrawlerProvider provider = new WebCrawlerProvider();
		provider.init(WebCrawler4J.class, dumper, prop);

		do {
			Thread.sleep(1000);
		} while (!provider.isFinished());
		provider.close();
	}

}
