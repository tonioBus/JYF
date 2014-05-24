/**
 * 
 */
package com.justyour.food.server.crawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import org.w3c.dom.DocumentFragment;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.server.JYFServletContext;
import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.server.crawl.tools.IOWget;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.server.receipe.SolrReceipe;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.ReceipeModel;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author tonio
 * 
 */
public class WebCrawler4J extends WebCrawler {
	static Logger logger = Logger.getLogger(WebCrawler4J.class.getName());

	IOWget wget = new IOWget();
	JPAManager jpaManager = JYFServiceImpl.getInstance().getJpaManager();
	SolrReceipe solrReceipe = JYFServletContext.getSolrReceipe();

	IngredientNLP ingredientNLP;
	protected Dumper dumper;
	protected String domain;
	protected String domainDir;

	public WebCrawler4J() throws InvalidFormatException, JAXBException,
			IOException {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#init(int,
	 * edu.uci.ics.crawler4j.crawler.CrawlController)
	 */
	@Override
	public void init(int id, CrawlController crawlController) {
		super.init(id, crawlController);
		this.dumper = (Dumper) crawlController.getCustomData();
		this.domain = dumper.getDomain();
		try {
			this.domainDir = new URL(domain).getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			this.domainDir = "bad_url_" + domain;
		}
		this.domainDir = JYFServletContext.getParam().getWorkingTmp()
				+ "/cache/" + this.domainDir + "/";
		ingredientNLP = dumper.getIngredientNLP();
		logger.fine("build JYFWebCrawler: " + this.getMyId());
	}

	@Override
	public void onBeforeExit() {
		logger.info("ADDED " + dumper.getDumperInfos().getAdded() + " receipes");
		try {
			this.jpaManager.getJpaWriter().flushReceipes();
			this.solrReceipe.commit();
		} catch (Throwable e) {
			logger.log(Level.SEVERE, "Error when exiting crawler", e);
			dumper.getDumperInfos().log("Error when exiting crawler: " + e);
		}
		try {
			this.wget.close();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error when exiting crawler", t);
			dumper.getDumperInfos().log("Error when exiting crawler: " + t);
		}
		super.onBeforeExit();
	}

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		boolean ret = dumper.shouldVisit(url);
		return ret;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		// we should not continue if we will be over max in case we add a
		// receipe
		// if (maxAdded > 0 && dumper.getDumperInfos().getAdded() >= maxAdded) {
		if (dumper.getDumperInfos().isMaxAddedReached()) {
			logger.info("shuting down: number of added receipe(s) > "
					+ dumper.getDumperInfos().getMaxAdded());
			super.getMyController().shutdown();
			return;
		}

		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();

		logger.info("Docid: " + docid);
		logger.info("Content Type: [" + page.getContentType() + "]");
		// writing visited file
		String filename = page.getWebURL().getPath();
		// test if this is a node
		if (filename.endsWith("/")) {
			logger.info("Directory detected:" + filename);
			return;
		}
		Path path = FileSystems.getDefault().getPath(domainDir, filename);

		FileChannel outChannel;
		try {
			Path pathParent = path.getParent().normalize();
			logger.info("Create dir: " + pathParent);
			Files.createDirectories(pathParent);
			logger.info("Writing file: " + path.normalize());
			outChannel = FileChannel.open(path.normalize(), EnumSet.of(
					StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.WRITE));
			outChannel.write(ByteBuffer.wrap(page.getContentData()));
			outChannel.close();
		} catch (IOException e1) {
			logger.severe("Exception when writing file: " + path);
			e1.printStackTrace();
		}

		if (page.getParseData() instanceof HtmlParseData) {
			byte[] bytes = page.getContentData();
			try {
				dumper.getDumperInfos().incNbVisit();
				if (dumper.getDumperInfos().isMaxVisitReached()) {
					logger.info("shuting down: number of visit > "
							+ dumper.getDumperInfos().getMaxVisit());
					super.getMyController().shutdown();
					return;
				}
				dumper.getDumperInfos().log("visiting: " + url);
				ReceipeModel receipe = jpaManager.getJpaWriter().create(url,
						domain);
				logger.info("URL[" + dumper.getDumperInfos().getVisited()
						+ "]: " + url);
				receipe.setLink(url);
				if (this.jpaManager.getJpaQuery().isStored(receipe.getLink()) == true) {
					logger.info("Receipe already exist: " + receipe.getLink());
					return;
				}
				DocumentFragment doc = wget.getDocumentFragment(url, bytes);
				if (doc == null) {
					logger.info("doc=null -> roller back[]:" + receipe);
					return;
				}
				boolean ret = dumper.dump(receipe, doc);
				if (ret == true && receipe.isWellFormed()) {
					int nbReceipes = this.jpaManager.getJpaQuery()
							.getReceipesSize();
					receipe.setNumber(nbReceipes);
					try {
						this.jpaManager.getJpaWriter().persist(receipe);
						this.solrReceipe.add(receipe);
					} catch (Throwable t) {
						logger.log(Level.SEVERE, "Error when adding a new receipe", t);
						dumper.getDumperInfos().log(
								"Adding receipe failed: " + receipe.getTitle());
						dumper.getDumperInfos().log(
								"Exception: " + t.getMessage());
					}
					logger.info("WRITING RECEIPE[" + nbReceipes + "]:"
							+ receipe);
					nbReceipes++;
					dumper.getDumperInfos().log(
							"Added receipe: " + receipe.getTitle());
					for (IngredientQuantity iq : receipe.getIngredients()) {
						dumper.getDumperInfos().log(
								"  - details: " + iq.getDetails() + " ("
										+ iq.getInterpretationLevel() + ")");
					}
					dumper.getDumperInfos().incNbAdded();
				} else {
					logger.info("receipe not correct -> roller back[]:"
							+ receipe);
					logger.info("isWellFormed:" + receipe.isWellFormed());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
