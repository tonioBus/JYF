/**
 * File: CrawlerProvider.java
 * Package: com.aquilaservices.metareceipe.wget.providers.doctissimo
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.server.crawl;

import java.util.Properties;
import java.util.logging.Logger;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.server.JYFServletContext;
import com.justyour.food.server.crawl.tools.CrawlerCommand;
import com.justyour.food.shared.DumperInfos;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> DoctissimoWget.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.providers.doctissimo</b><br/>
 * <b>Date:</b> 5 août 2013
 * 
 * @author tonio
 * 
 */
public class WebCrawlerProvider implements IWebCrawlerProvider {

	static Logger logger = Logger.getLogger(WebCrawlerProvider.class.getName());

	protected CrawlerCommand cmd;
	protected Properties properties;
	protected Dumper dumper;

	private int getInt(String property, int init) {
		String sz = properties.getProperty(property);
		int limit = init;
		if (sz != null) {
			try {
				limit = Integer.valueOf(sz);
			} catch (NumberFormatException e) {
			}
		}
		return limit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aquilaservices.metareceipe.WGET.providers.IWGETProvider#init(java
	 * .util.Properties)
	 */
	@Override
	public <T extends WebCrawler> void init(final Class<T> crawlerClass,
			Dumper dumper, Properties properties) throws Exception {
		this.properties = properties;
		this.dumper = dumper;
		int numberCrawls = getInt(NUMBER_CRAWLS, -1);
		int maxDepthOfCrawling = getInt(MAX_DEPTH_CRAWLING, -1);
		int politnessDelay = getInt(POLITNESS_DELAY, 1000);
		int maxPagesToFetch = getInt(MAX_PAGES_TO_FETCH, -1);
		String urlsSz = properties.getProperty("URLS");
		String urls[] = urlsSz.split(";");
		logger.info("CrawlerCommand("+urls+", "+dumper.getDomain()+", "+crawlerClass);
		cmd = new CrawlerCommand(urls, dumper, JYFServletContext.getParam()
				.getWorkingTmp(), crawlerClass, numberCrawls,
				maxDepthOfCrawling, politnessDelay, maxPagesToFetch);
		cmd.start();
	}

	public DumperInfos getDumperInfos() {
		dumper.getDumperInfos().setIsFinished(isFinished());
		return dumper.getDumperInfos();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aquilaservices.metareceipe.WGET.providers.IWGETProvider#free()
	 */
	@Override
	public void close() {
		cmd.stop();
		JYFServiceImpl.getInstance().getJpaManager().getJpaWriter()
				.flushReceipes();
		JYFServiceImpl.getInstance().getJpaManager().getJpaWriter().close();
		logger.info("Provider [" + this.getClass().getName() + "]: "
				+ cmd.getNumberOfProcessedPages() + " processed pages");
	}

	@Override
	public boolean isFinished() {
		return cmd.isFinish();
	}
}
