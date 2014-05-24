/**
 * 
 */
package com.justyour.food.server.crawl.tools;

import java.util.logging.Logger;

import com.justyour.food.server.crawl.Dumper;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author tonio
 * 
 */
public class CrawlerCommand {

	static Logger logger = Logger.getLogger(CrawlerCommand.class.getName());

	/*
	 * numberOfCrawlers shows the number of concurrent threads that should be
	 * initiated for crawling.
	 */
	private int numberOfCrawlers = 5;

	CrawlController controller;
	CrawlConfig config;

	@SuppressWarnings("rawtypes")
	final Class crawlerClass;

	public <T extends WebCrawler> CrawlerCommand(String seeds[], Dumper dumper,
			String storage, final Class<T> crawlerClass, int numberCrawlers,
			int maxDepthOfCrawling, int politnessDelay, int maxPagesToFetch) throws Exception {
		this.crawlerClass = crawlerClass;
		this.numberOfCrawlers = numberCrawlers;

		config = new CrawlConfig();

		config.setCrawlStorageFolder(storage);

		config.setPolitenessDelay(politnessDelay);

		config.setMaxDepthOfCrawling(maxDepthOfCrawling);

		config.setMaxPagesToFetch(maxPagesToFetch);
		
		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(false);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		controller = new CrawlController(config, pageFetcher, robotstxtServer);
		logger.info("setting CustomData to: "+dumper.getDomain());
		controller.setCustomData(dumper);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */

		// http://recettes.doctissimo.fr/plats/ - pour les plats
		for (String seed : seeds) {
			logger.info("ADD SEED ["+seed+"]");
			controller.addSeed(seed);
		}
	}

	@SuppressWarnings("unchecked")
	public void start() throws Exception {
		logger.info("getting CustomData : "+this.controller.getCustomData());
		controller.startNonBlocking(this.crawlerClass, numberOfCrawlers);
	}

	public long getNumberOfProcessedPages() {
		return controller.getFrontier().getNumberOfProcessedPages();
	}

	public boolean isFinish() {
		return controller.isFinished();
	}

	public void stop() {
		controller.shutdown();
		controller.waitUntilFinish();
	}

}
