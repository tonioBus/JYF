/**
 * 
 */
package com.justyour.food.wget.providers.doctissimo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import org.w3c.dom.DocumentFragment;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.server.SolrReceipe;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.wget.Dumper;
import com.justyour.food.wget.opennlp.IngredientNLP;
import com.justyour.food.wget.tools.IOWget;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author tonio
 * 
 */
public class DoctissimoWebCrawler extends WebCrawler {
	static Logger logger = Logger.getLogger(DoctissimoWebCrawler.class
			.getName());

	final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
			+ "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	IOWget wget = new IOWget();
	JPAManager jpaManager = JYFServiceImpl.getInstance().getJpaManager();
	SolrReceipe solrReceipe = JYFServiceImpl.getInstance().getSolrReceipe();

	IngredientNLP ingredientNLP;
	Integer nbVisit = 0;

	private static Dumper dumper;
	private static String domain;
	public static void init(Dumper dumperP, String domainP) {
		dumper = dumperP;
		domain = domainP;
	}

	@Override
	public void onBeforeExit() {
		super.onBeforeExit();
		try {
			this.solrReceipe.flushAndOptimize();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error when exiting crawler", e);
		}
		this.wget.close();
	}

	public DoctissimoWebCrawler() throws InvalidFormatException, JAXBException,
			IOException {
		super();
		ingredientNLP = new IngredientNLP("war");
		logger.fine("build DoctissimoWebCrawler: "+this.getMyId());
	}

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith(domain);
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();

		logger.info("Docid: " + docid);
		logger.info("Content Type: ["+page.getContentType()+"]");
		if (page.getParseData() instanceof HtmlParseData) {
			byte[] bytes = page.getContentData();
			try {
				synchronized(nbVisit) {
					nbVisit++;
				}
				ReceipeModel receipe = jpaManager.getJpaWriter().create(url, domain);
						logger.info("URL[" + (nbVisit) + "]: " + url);
				receipe.setLink(url);
				if (this.jpaManager.getJpaQuery().isStored(receipe.getLink()) == true) {
					logger.info("Receipe already exist: " + receipe.getLink());
					return;
				}
				DocumentFragment doc = wget.getDocumentFragment(url, bytes);
				if (doc == null) {
					logger.info("doc=null -> roller back[]:"
							+ receipe);
					return;
				}
				boolean ret = dumper.dump(receipe, doc);
				if (ret == true && receipe.isWellFormed()) {
					int nbReceipes = this.jpaManager.getJpaQuery().getReceipesSize();
					receipe.setNumber(nbReceipes);
					this.jpaManager.getJpaWriter().persist(receipe);
					this.solrReceipe.add(receipe);
					logger.info("WRITING RECEIPE[" + nbReceipes + "]:"
							+ receipe);
					nbReceipes++;
				} else {
					logger.info("receipe not correct -> roller back[]:" + receipe);
					logger.info("isWellFormed:" + receipe.isWellFormed());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
