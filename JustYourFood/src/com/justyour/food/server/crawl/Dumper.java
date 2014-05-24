/**
 * 
 */
package com.justyour.food.server.crawl;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import opennlp.tools.util.InvalidFormatException;

import org.w3c.dom.Node;

import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.DumperInfos;
import com.justyour.food.shared.jpa.models.ReceipeModel;

import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author tonio
 * 
 */
public abstract class Dumper {

	static Logger logger = Logger.getLogger(Dumper.class.getName());

	final Pattern URL_FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
			+ "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	protected IngredientNLP ingredientNLP;
	protected JPAManager jpaManager;
	protected String domain;
	protected DumperInfos dumperInfos;
	
	public Dumper(String domain, IngredientNLP ingredientNLP,
			JPAManager jpaManager, int maxAdded, int maxVisited) {
		super();
		this.domain = domain;
		this.ingredientNLP = ingredientNLP;
		this.jpaManager = jpaManager;
		dumperInfos = new DumperInfos(this.getClass().getName());
		dumperInfos.init(maxVisited, maxAdded);
	}

	public abstract boolean dump(ReceipeModel receipe, Node node)
			throws InvalidFormatException, IOException;

	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		logger.fine("shouldVisit " + url);
		return !URL_FILTERS.matcher(href).matches() && href.startsWith(domain);
	}

	public String getDomain() {
		return this.domain;
	}

	public DumperInfos getDumperInfos() {
		return this.dumperInfos;
	}

	public IngredientNLP getIngredientNLP() {
		return this.ingredientNLP;
	}



}
