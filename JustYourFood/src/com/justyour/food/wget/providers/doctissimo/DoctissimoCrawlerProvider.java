/**
 * File: DoctissimoWget.java
 * Package: com.aquilaservices.metareceipe.wget.providers.doctissimo
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.wget.providers.doctissimo;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import opennlp.tools.util.InvalidFormatException;

import org.apache.html.dom.HTMLLIElementImpl;
import org.apache.html.dom.HTMLMetaElementImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.server.ToolsServer;
import com.justyour.food.server.ToolsShared;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.wget.IWgetProvider;
import com.justyour.food.wget.opennlp.IngredientNLP;
import com.justyour.food.wget.tools.CrawlerCommand;

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
public class DoctissimoCrawlerProvider implements IWgetProvider {

	public static final String URL = "http://recettes.doctissimo.fr/";

	static Logger logger = Logger.getLogger(DoctissimoCrawlerProvider.class
			.getName());

	CrawlerCommand cmd;

	@Override
	public <T extends WebCrawler> void init(Class<T> crawlerClass,
			Properties properties) throws Exception {
		cmd = new CrawlerCommand(new String[] { URL }, ToolsServer.getParam()
				.getWorkingTmp(), DoctissimoWebCrawler.class, 5, 4, 300, -1);
		cmd.start();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aquilaservices.metareceipe.WGET.providers.IWGETProvider#init(java
	 * .util.Properties)
	 */
	private void init(Properties properties)
			throws Exception {
		String limitSz = properties.getProperty(LIMIT);
		int limit = Integer.MAX_VALUE;
		if (limitSz != null) {
			try {
				limit = Integer.valueOf(limitSz);
			} catch (NumberFormatException e) {
			}
		}
		cmd = new CrawlerCommand(new String[] { URL }, ToolsServer.getParam()
				.getWorkingTmp(), DoctissimoWebCrawler.class, 5, 4, 300, -1);
		cmd.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aquilaservices.metareceipe.WGET.providers.IWGETProvider#free()
	 */
	@Override
	public void close() {
		logger.info("Provider [" + this.getClass().getName() + "]: "
				+ cmd.getNumberOfProcessedPages() + " processed pages");
		cmd.stop();
	}

}
