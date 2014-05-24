/**
 * 
 */
package com.justyour.food.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.solr.core.CoreContainer;

import com.justyour.food.server.ciqual.SolrCiqual;
import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.server.receipe.SolrReceipe;

/**
 * @author tonio
 * 
 */
public class JYFServletContext implements ServletContextListener {

	static Logger logger = Logger.getLogger(JYFServletContext.class.getName());

	static private Parameters param;
	static private OS os;
	static private String jyfHome;
	static private String deployDir = ".";
	static private IngredientNLP ingredientNLP;
	static private CrawlerManagement crawlerManagement;
	static private CrawlerClassLoader crawlerClassLoader;
	static private CheckProviderIP checkProviderIP;
	static private CoreContainer container = null;
	static private SolrReceipe solrReceipe = null;
	static private SolrCiqual solrCiqual = null;

	public static String getParamFile() {
		return getJYFHome() + "jyf.xml";
	}

	static public OS getOS() {
		return os;
	}

	public static Parameters getParam() {
		return param;
	}

	public static String getJYFHome() {
		return jyfHome;
	}

	public static String getDeployDir() {
		return deployDir;
	}

	public enum OS {
		WINDOWS, LINUX, MACOS
	}

	private static boolean isProductionServer = false;

	public JYFServletContext() {
		logger.info("Building ServletContext for JYF");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.entering(JYFServletContext.class.getName(), "contextInitialized("+event.toString()+")");
		ServletContext sc = event.getServletContext();
		deployDir = sc.getRealPath(File.separator);
		logger.info("JYF Context Initialized: " + deployDir);
		try {
			initJYFSerletContext(sc.getInitParameter("jyf-home"));
		} catch (JAXBException | IOException e) {
			logger.severe("Error when initializing the JYF Context");
			e.printStackTrace();
		}
		logger.exiting(JYFServletContext.class.getName(), "contextInitialized("+event.toString()+")");
	}

	public void initJYFSerletContext(String jyfHome)
			throws InvalidFormatException, JAXBException, IOException {
		logger.entering(JYFServletContext.class.getName(), "initJYFServletContext("+jyfHome+")");
		initOS();
		if (jyfHome == null) {
			switch (os) {
			case WINDOWS:
				jyfHome = "c:\\justyour.com\\";
			case LINUX:
				jyfHome = "/justyour.com/";
			default:
				jyfHome = "/justyour.com/";
			}
		}
		JYFServletContext.jyfHome = jyfHome;
		reinitJYFSerletContext();
		logger.exiting(JYFServletContext.class.getName(), "initJYFServletContext("+jyfHome+")");
	}

	public static void reloadParam() {
		logger.entering(JYFServletContext.class.getName(), "reloadParam");
		try {
			param = Parameters.getParameters(getParamFile());
		} catch (FileNotFoundException e) {
			logger.severe("Exception when reading parameter file");
			e.printStackTrace();
		} catch (JAXBException e) {
			logger.severe("Exception when reading parameter file");
			e.printStackTrace();
		} finally {
			logger.info("Init param:" + param);
		}
		crawlerClassLoader = new CrawlerClassLoader();
		crawlerClassLoader.loadDumpers(param);
		if (container != null) {
			container.shutdown();
			container = null;
		}
		cleanSolrLocks();
		container = new CoreContainer(param.getSolrHome());
		container.load();
		solrReceipe = new SolrReceipe(container, param.getSolrReceipeCore());
		solrCiqual = new SolrCiqual(container, param.getSolrCiqualCore());
		logger.exiting(JYFServletContext.class.getName(), "reloadParam");
	}

	public void close() {
		logger.entering(JYFServletContext.class.getName(), "close");
		checkProviderIP.stop();
		if (solrReceipe != null) {
			solrReceipe.close();
			solrReceipe = null;
		}
		if (solrCiqual != null) {
			solrCiqual.close();
			solrCiqual = null;
		}
		if (container != null) {
			container.shutdown();
			container = null;
		}
		crawlerClassLoader = null;
		crawlerManagement = null;
		logger.exiting(JYFServletContext.class.getName(), "close");
	}

	public static SolrCiqual getSolrCiqual() {
		return solrCiqual;
	}

	protected static void cleanSolrLocks() {
		logger.entering(JYFServletContext.class.getName(), "cleanSolr");
		logger.info("clearning all write.lock found in ["+param.getSolrHome()+"]");
		ArrayList<File> files = ToolsServer.find(param.getSolrHome(), "write.lock");
		for (File file : files) {
			logger.info("file:"+file.getAbsolutePath()+ "deleting ? : "+file.delete());
		}
		logger.exiting(JYFServletContext.class.getName(), "cleanSolr");
	}
	
	public static void reinitJYFSerletContext() throws InvalidFormatException,
			JAXBException, IOException {
		logger.entering(JYFServletContext.class.getName(), "reinitJYFSerletContext");
		logger.info("JYF Context Initialized with jyf-home: [" + jyfHome + "]");
		reloadParam();
		testTypeServer();
		if (checkProviderIP != null) {
			checkProviderIP.exec.shutdown();
			checkProviderIP = null;
		}
		if (isProductionServer) {
			checkProviderIP = new CheckProviderIP();
			checkProviderIP.start();
		}
		ingredientNLP = new IngredientNLP(deployDir);
		crawlerManagement = new CrawlerManagement();
		logger.exiting(JYFServletContext.class.getName(), "reinitJYFSerletContext");
	}

	/**
	 * @return the isProductionServer
	 */
	static public boolean isProductionServer() {
		return isProductionServer;
	}

	/**
	 * @return the crawlerManagement
	 */
	public static CrawlerManagement getCrawlerManagement() {
		return crawlerManagement;
	}

	/**
	 * From http://www.javaneverdie.com/java/java-os-name-property-values/
	 * Possible values; AIX, Digital Unix, FreeBSD, HP UX, Irix, Linux, Mac OS,
	 * Mac OS X, MPE/iX, Netware 4.11, OS/2, Solaris, Windows 2000, Windows 95,
	 * Windows 98, Windows NT, Windows Vista, Windows XP,
	 */
	protected void initOS() {
		String osName = System.getProperty("os.name");
		logger.info("OS:" + osName);
		os = OS.LINUX;
		if (osName.startsWith("Windows"))
			os = OS.WINDOWS;
		if (osName.startsWith("Mac"))
			os = OS.MACOS;
		if (osName.startsWith("Linux"))
			os = OS.LINUX;
	}

	/**
	 * Test if the current platform is the production server
	 * 
	 * @throws SocketException
	 */
	protected static void testTypeServer() throws SocketException {
		isProductionServer = false;
		Enumeration<NetworkInterface> ifs = NetworkInterface
				.getNetworkInterfaces();
		while (ifs.hasMoreElements()) {
			NetworkInterface netInterface = ifs.nextElement();
			logger.info("if:"
					+ ToStringBuilder.reflectionToString(netInterface,
							ToStringStyle.MULTI_LINE_STYLE));
			Enumeration<InetAddress> inetAddresses = netInterface
					.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				InetAddress inetAddress = inetAddresses.nextElement();
				logger.info("InetAddress:" + inetAddress.getHostAddress());
				if (inetAddress.getHostAddress().equals(
						getParam().getServerIpAddress())) {
					logger.info("PRODUCTION SERVER FOUND ["
							+ inetAddress.getHostAddress() + "]");
					isProductionServer = true;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.entering(JYFServletContext.class.getName(), "contextDestroyed("+event.toString()+")");
		this.close();
		logger.exiting(JYFServletContext.class.getName(), "contextDestroyed("+event.toString()+")");
	}

	public static IngredientNLP getIngredientNLP() {
		return ingredientNLP;
	}

	public static CrawlerClassLoader getProviderManagement() {
		return crawlerClassLoader;
	}

	public static SolrReceipe getSolrReceipe() {
		return solrReceipe;
	}

	public static SolrCiqual getSolCiqual() {
		return solrCiqual;
	}

}
