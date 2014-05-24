/**
 * File: IWgetProvider.java
 * Package: com.aquilaservices.metareceipe.wget
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.server.crawl;

import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

/** 
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> IWgetProvider.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * @author tonio
 * 
 */

public interface IWebCrawlerProvider {

	public static final String URLS="URLS";
	public static final String LIMIT="LIMIT";
	public static final String MAX_DEPTH_CRAWLING = "MAX_DEPTH_CRAWLING";
	public static final String POLITNESS_DELAY = "POLITNESS_DELAY";
	public static final String MAX_PAGES_TO_FETCH = "MAX_PAGES_TO_FETCH";
	public static final String NUMBER_CRAWLS = "NUMBER_CRAWLS";
	public static final String PATTERN = "PATTERN";

	/**
	 * Initiate the reading of the plugin input and populate databases accordingly.
	 * @param properties - properties for reading input
	 * @throws SAXException - in case of malformed HTML (nekohtml implementation)
	 * @throws IOException - in case of input error (URL not found, ...)
	 * @throws JAXBException 
	 */
	public abstract <T extends WebCrawler> void init(final Class<T> crawlerClass, Dumper dumper, Properties properties) throws Exception;

	/**
	 * Close the process of reading and populating databases,
	 * usually this method verify, flush and close all handlers.
	 */
	public abstract void close();

	public abstract boolean isFinished();

}