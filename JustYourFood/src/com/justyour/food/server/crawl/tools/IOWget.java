/**
 * File: IOWget.java
 * Package: com.aquilaservices.metareceipe.wget.tools
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.server.crawl.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> IOWget.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.tools</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * 
 * @author tonio
 * 
 */

public class IOWget {
	static Logger logger = Logger.getLogger(IOWget.class.getName());

	HTMLDocumentImpl document;

	public DocumentFragment getDocumentFragmentFromURL(URL url)
			throws SAXException, IOException {
		DOMFragmentParser parser = new DOMFragmentParser();
		document = new HTMLDocumentImpl();
		DocumentFragment fragment = document.createDocumentFragment();
		document.close();
		String urlSz = url.toString();
		urlSz = urlSz.replaceAll("/?/%93", "oe");
		try {
			parser.parse(new InputSource(urlSz), fragment);
		} catch (IllegalArgumentException ae) {
			logger.log(Level.SEVERE, "Error when reading"+url, ae);
			return null;
		} catch (DOMException de) {
			logger.log(Level.SEVERE, "Error when reading"+url, de);
			return null;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error when reading"+url, e);
			return null;
		}
		return fragment;
	}

	public DocumentFragment getDocumentFragment(String url, byte[] sz) throws SAXException, IOException {
		InputStream input = new ByteArrayInputStream(sz);
		return getDocumentFragment(url, input);
	}

	public DocumentFragment getDocumentFragment(String url, InputStream input)
			throws SAXException, IOException {
		DOMFragmentParser parser = new DOMFragmentParser();
		document = new HTMLDocumentImpl();
		DocumentFragment fragment = document.createDocumentFragment();
		document.close();
		try {
			InputSource s = new InputSource(input);
			parser.parse(s, fragment);
		} catch (IllegalArgumentException ae) {
			logger.severe(url + ":" + ae.getMessage());
			return null;
		} catch (DOMException de) {
			logger.severe(de.getMessage());
			de.printStackTrace();
			return null;
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return fragment;
	}

	public void close() {
		if (document != null)
			document.close();
	}

}
