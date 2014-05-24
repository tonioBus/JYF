/**
 * 
 */
package com.justyour.food.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServerException;

import com.justyour.food.server.crawl.Dumper;
import com.justyour.food.server.crawl.IWebCrawlerProvider;
import com.justyour.food.server.crawl.WebCrawler4J;
import com.justyour.food.server.crawl.WebCrawlerProvider;
import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.DumperInfos;

/**
 * @author tonio
 * 
 */
public class CrawlerManagement {

	static Logger logger = Logger.getLogger(CrawlerManagement.class.getName());

	java.util.HashMap<String, WebCrawlerProvider> runningCrawlerProvider = new HashMap<>();

	public void cleanSolr() throws SolrServerException, IOException {
		JYFServletContext.getSolrReceipe().clean();
	}

	public void startCrawl(String className, String url, int politnessDelay,
			int numberCrawls, int maxDepthCrawling, int maxPagesToFetch,
			int maxAddition, int maxVisiting) throws Exception {
		JPAManager jpaManager = JYFServiceImpl.getInstance().getJpaManager();
		IngredientNLP ingredientNLP = JYFServletContext.getIngredientNLP();

		@SuppressWarnings("unchecked")
		Class<Dumper> clazz = (Class<Dumper>) Class.forName(className);
		logger.info("startCRAWL: Initializing class: [" + clazz+"]->["+url+"]");
		Constructor<Dumper> cons = clazz.getConstructor(String.class,
				IngredientNLP.class, JPAManager.class, int.class, int.class);
		Dumper dumper = cons.newInstance(url, ingredientNLP, jpaManager,
				maxAddition, maxVisiting);
		logger.info("builded Dumper: " + dumper);
		startCrawl(dumper, url, politnessDelay, numberCrawls, maxDepthCrawling,
				maxPagesToFetch, maxAddition, maxVisiting);
	}

	protected void startCrawl(Dumper dumper, String url, int politnessDelay,
			int numberCrawls, int maxDepthCrawling, int maxPagesToFetch,
			int maxAddition, int maxVisiting) throws Exception {
		Properties prop = new Properties();
		// String url = "http://recettes.doctissimo.fr";
		// prop.setProperty(IWgetProvider.URLS, url);
		// prop.setProperty(IWgetProvider.POLITNESS_DELAY, "1000");
		// prop.setProperty(IWgetProvider.NUMBER_CRAWLS, "5");
		// prop.setProperty(IWgetProvider.MAX_DEPTH_CRAWLING, "5");
		// prop.setProperty(IWgetProvider.MAX_PAGES_TO_FETCH, "-1");
		// String url = "http://recettes.doctissimo.fr";
		prop.setProperty(IWebCrawlerProvider.URLS, url);
		prop.setProperty(IWebCrawlerProvider.POLITNESS_DELAY, ""
				+ politnessDelay);
		prop.setProperty(IWebCrawlerProvider.NUMBER_CRAWLS, "" + numberCrawls);
		prop.setProperty(IWebCrawlerProvider.MAX_DEPTH_CRAWLING, ""
				+ maxDepthCrawling);
		prop.setProperty(IWebCrawlerProvider.MAX_PAGES_TO_FETCH, ""
				+ maxPagesToFetch);
		// DoctissimoWebCrawler.init(new DoctissimoDumper(ingredientNLP,
		// jpaManager), url);
		WebCrawlerProvider provider = new WebCrawlerProvider();
		provider.init(WebCrawler4J.class, dumper, prop);
		runningCrawlerProvider.put(dumper.getClass().getName(), provider);
	}

	public WebCrawlerProvider getCrawlerProvider(String className) {
		WebCrawlerProvider provider = runningCrawlerProvider.get(className);
		return provider;
	}

	public DumperInfos getDumperInfos(String className) {
		WebCrawlerProvider provider = runningCrawlerProvider.get(className);
		return provider.getDumperInfos();
	}

	public DumperInfos[] getDumperInfos() {
		DumperInfos[] ret = new DumperInfos[this.runningCrawlerProvider.size()];
		int i = 0;
		for (WebCrawlerProvider provider : this.runningCrawlerProvider.values()) {
			ret[i++] = provider.getDumperInfos();
		}
		return ret;
	}

	public void stopCrawl(String className) {
		WebCrawlerProvider provider = runningCrawlerProvider.get(className);
		provider.close();
	}

	public boolean allFinished() {
		boolean ret = true;

		for (WebCrawlerProvider provider : this.runningCrawlerProvider.values()) {
			if (provider.isFinished() == false)
				return false;
		}
		return ret;
	}

}
