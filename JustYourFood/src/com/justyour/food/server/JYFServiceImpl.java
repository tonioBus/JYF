package com.justyour.food.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrServerException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.justyour.food.client.JYFService;
import com.justyour.food.server.ciqual.DBCiqual;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.server.xml.ReceipeModelXML;
import com.justyour.food.shared.AdminInfos;
import com.justyour.food.shared.ResultsObject.ResultGetReceipes;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.shared.jpa.models.UserID;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * The server side implementation of the IGwtRPC service.
 */
@SuppressWarnings("serial")
public class JYFServiceImpl extends RemoteServiceServlet implements JYFService {

	static Logger logger = Logger.getLogger(JYFServiceImpl.class.getName());

	private JPAManager jpaManager = null;
	private static JYFServiceImpl instance;

	public static JYFServiceImpl getInstance() {
		if (instance == null) {
			// the constructor will assign the field instance
			new JYFServiceImpl();
		}
		return instance;
	}

	/**
	 * Constructor to use only on test mode, where we can decide what to
	 * initialize
	 * 
	 * @param onlyJPA
	 */
	public JYFServiceImpl(boolean jpa, boolean solrReceipe, boolean solrCiqual) {
		instance = this;
		if (jpa)
			this.jpaManager = new JPAManager();
	}

	public JYFServiceImpl() {
		this(true, true, true);
	}

	public JPAManager getJpaManager() {
		return jpaManager;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		String ret = html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
		return ret;
	}

	@Override
	public long getNumberOfSuggestedReceipes(String name) {
		name = escapeHtml(name);
		if (name.equals(""))
			name = "*:*";
		return JYFServletContext.getSolrReceipe().getNumberOfSuggestions(name);
	}

	@Override
	public long getNumberOfReceipes() {
		return getNumberOfSuggestedReceipes("*:*");
	}

	@Override
	public long getNumberOfSuggestedCiquals(String name) {
		name = escapeHtml(name);
		if (name.equals(""))
			name = "*:*";
		return JYFServletContext.getSolrCiqual().getNumberOfSuggestions(name);
	}

	@Override
	public long getNumberOfCiquals() {
		return getNumberOfSuggestedCiquals("*:*");
	}

	@Override
	public ResultGetReceipes getReceipes(String name, int start, int length)
			throws Exception {
		name = escapeHtml(name);
		ResultGetReceipes result = new ResultGetReceipes();
		result.setNumberReceipes(getNumberReceipes());
		result.setNumberIngredients(getNumberIngredients());
		result.setNumberOfSuggestions(getNumberOfSuggestedReceipes(name));

		if (name.equals(""))
			name = "*:*";
		List<ReceipeModel> ret = new ArrayList<ReceipeModel>();
		ArrayList<String> links = JYFServletContext.getSolrReceipe()
				.getSuggestions(name, start, length);
		for (String link : links) {
			ReceipeModel receipe;
			try {
				receipe = jpaManager.getJpaQuery().getReceipeReadOnly(link);
				receipe.prepare4RPCShortDisplay();
			} catch (Exception e) {
				logger.log(Level.WARNING, "Error when getting a link", e);
				logger.warning("replacing the receipe with a dummy one");
				receipe = ReceipeModel.getDummyReceipe("Not found receipes ! ["
						+ name + "]", link);
			}
			ret.add(receipe);
		}
		result.setReceipes(ret.toArray(new ReceipeModel[0]));
		return result;
	}

	@Override
	public String cleanSolrFromDB() throws Exception {
		int nbRemoved = 0;
		ArrayList<String> links;

		try {
			links = JYFServletContext.getSolrReceipe().getSuggestions("*:*", 0,
					Integer.MAX_VALUE);
		} catch (Throwable t) {
			logger.log(Level.SEVERE,
					"Error when getting suggestions from SOLR", t);
			return "Error when getting suggestions from SOLR:" + t;
		}
		for (String link : links) {
			try {
				jpaManager.getJpaQuery().getReceipeReadOnly(link);
			} catch (Exception e) {
				logger.warning("Removing the receipe [" + link + "] from SOLR");
				JYFServletContext.getSolrReceipe().remove(link);
				nbRemoved++;
			}
		}
		try {
			JYFServletContext.getSolrReceipe().commit();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error when committing change on SOLR", t);
			return "Error when committing change on SOLR:" + t;
		}
		return "Removed " + nbRemoved + " receipe(s) from SOLR";
	}

	@Override
	public String updateSOLRFromDB() {
		int nbAddedReceipe = 0;
		try {
			JYFServletContext.getSolrReceipe().clean();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error when cleaning SOLR", t);
			return "Error when cleaning SOLR:" + t;
		}
		List<ReceipeModel> receipes = this.jpaManager.getJpaQuery()
				.getAllReceipes();
		for (ReceipeModel receipeModel : receipes) {
			logger.info("Recette:" + receipeModel.getTitle());
			try {
				JYFServletContext.getSolrReceipe().add(receipeModel);
				nbAddedReceipe++;
			} catch (Throwable t) {
				logger.log(Level.SEVERE,
						"Error when adding [" + receipeModel.getLink()
								+ "] to SOLR", t);
			}

		}
		try {
			JYFServletContext.getSolrReceipe().commit();
			JYFServletContext.getSolrReceipe().optimize();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error when commiting or optimizing SOLR",
					t);
			return "Error when commiting or optimizing SOLR:" + t;
		}

		String msg;
		if (nbAddedReceipe < receipes.size()) {
			msg = "Problem: we add only " + nbAddedReceipe + " / "
					+ receipes.size() + " receipes.";
		} else {
			msg = "Added " + nbAddedReceipe + " receipe(s) into solr.";
		}
		return msg;
	}

	@Override
	public ReceipeModel getReceipe4RPC_ReadOnly(String link) {
		ReceipeModel receipeModel = jpaManager.getJpaQuery()
				.getReceipeReadOnly(link);
		receipeModel.prepare4RPCDisplay();
		return receipeModel;
	}

	@Override
	public int getNumberReceipes() throws IllegalArgumentException {
		return jpaManager.getJpaQuery().getReceipesSize();
	}

	@Override
	public int getNumberIngredients() throws IllegalArgumentException {
		return jpaManager.getJpaQuery().getIngredientsSize();
	}

	@Override
	public HashMap<String, Object> getServerDeployFiles() throws IOException {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		ServerProperties.getAllFile(new File(JYFServletContext.getDeployDir()),
				ret);
		return ret;
	}

	@Override
	public HashMap<String, String> getServerEnv() {
		Map<String, String> map = System.getenv();
		return new HashMap<String, String>(map);
	}

	@Override
	public HashMap<String, String> getServerProperties() {
		Properties prop = System.getProperties();
		HashMap<String, String> map = new HashMap<>();

		for (final String name : prop.stringPropertyNames())
			map.put(name, prop.getProperty(name));
		return map;
	}

	@Override
	public ArrayList<String> getServletProperties() {
		ArrayList<String> ret;
		try {
			ret = jpaManager.getJpaQuery().infos();
		} catch (Throwable t) {
			t.printStackTrace();
			ret = new ArrayList<>();
			ret.add("JPA Init Exception");
			ret.add(t.getMessage());
		}
		ret.add("Server Info");
		ret.add(getServletContext().getServerInfo());
		ret.add("Context Path");
		ret.add(getServletContext().getContextPath());

		Enumeration<String> headers = this.getThreadLocalRequest()
				.getHeaderNames();
		while (headers.hasMoreElements()) {
			String key = headers.nextElement();
			ret.add("headers [" + key + "]");
			ret.add(getThreadLocalRequest().getHeader(key));
		}

		Enumeration<String> attributes = getServletContext()
				.getAttributeNames();
		while (attributes.hasMoreElements()) {
			String key = attributes.nextElement();
			ret.add("Attribute[" + key + "]");
			ret.add(getServletContext().getAttribute(key).toString());
		}

		Enumeration<String> initParams = this.getInitParameterNames();
		while (initParams.hasMoreElements()) {
			String key = initParams.nextElement();
			ret.add("Init Param [" + key + "]");
			ret.add(getServletContext().getInitParameter(key));
		}

		return ret;
	}

	@Override
	/*
	 * http://localhost:8080/solr-4.4.0/recettesKparK/select?q=pate*&wt=json&indent
	 * =true
	 * 
	 * @see
	 * com.aquilaservices.recettesKparK.client.GreetingService#getSolrRequest
	 * (java.lang.String, int)
	 */
	public ArrayList<String> getSolrRequest(String input, int i)
			throws SolrServerException {
		return null; // solr.getSolrRequest(input, i);
	}

	@Override
	public ArrayList<String> getSolrProperties() {
		return JYFServletContext.getSolrReceipe().getProperties();
	}

	@Override
	public String[] getReceipesSuggestions(String searchText, int length)
			throws Exception {
		searchText = escapeHtml(searchText);
		if (searchText.equals(""))
			searchText = "*:*";

		ArrayList<String> list = JYFServletContext.getSolrReceipe()
				.getDisplaySuggestions(searchText, 0, length);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * @param name - mORIGFDNM (Ciqual)
	 * @return
	 */
	public CiqualModel getCiqual(String name) {
		return jpaManager.getJpaQuery().getCiqualReadOnly(name);
	}
	
	@Override
	public String[] getCiqualSuggestions(String searchText, int length) {
		searchText = escapeHtml(searchText);
		if (searchText.equals(""))
			searchText = "*:*";
		ArrayList<String> list = JYFServletContext.getSolCiqual()
				.getDisplaySuggestions(searchText, 0, length);
		return list.toArray(new String[list.size()]);
	}

	@Override
	public CiqualModel[] getCiqual(String text, int start, int length) {
		List<CiqualModel> ret = new ArrayList<CiqualModel>();

		text = escapeHtml(text);
		if (text.equals(""))
			text = "*:*";
		ArrayList<String> names = JYFServletContext.getSolCiqual()
				.getSuggestions(text, start, length);
		for (String name : names) {
			CiqualModel ciqua = jpaManager.getJpaQuery().getCiqualReadOnly(name);
			ret.add(ciqua);
		}
		return ret.toArray(new CiqualModel[0]);
	}

	@Override
	public long getNumberDBCiqual() {
		return jpaManager.getJpaQuery().getCiqualSize();
	}

	@Override
	public long getNumberSolrCiqual() {
		return JYFServletContext.getSolrCiqual().getNumberOfSuggestions("*.*");
	}

	@Override
	public String register(UserID userProfile) throws Exception {
		UserID oldUser = jpaManager.getJpaQuery().getUserProfile(
				userProfile.getEmail());
		if (oldUser != null) {
			return "l'utilisateur avec l'email [" + userProfile.getEmail()
					+ "] existe deja !";
		}
		jpaManager.getJpaWriter().flush(userProfile);

		// Recipient's email ID needs to be mentioned.
		String to = userProfile.getEmail();

		// Sender's email ID needs to be mentioned
		String from = JYFServletContext.getParam().getAdmins()[0];

		String subject = "(JustYourFood) Confirmation d'inscription";
		String uuid = UUID.randomUUID().toString();
		userProfile.setUUID(uuid);

		String body = "Bonjour "
				+ userProfile.getPrenoms()
				+ " "
				+ userProfile.getNom()
				+ ",\n"
				+ "\n"
				+ "Afin de valider votre inscription sur JustYourFood, suivez le lien ci-dessous pour activer votre compte :\n"
				+ JYFServletContext.getParam().getSiteHost()
				+ "#ConfirmRegister;" + uuid + ";\n" + "\n"
				+ "Voici un récapitulatif de vos identifiants :\n" + "\n"
				+ "Login : " + userProfile.getEmail() + " Mot de passe : "
				+ userProfile.getPassword() + "\n" + "\n"
				+ "------------------------------------------\n"
				+ "Ceci est un message automatique du site JustYourFood.\n"
				+ "à bientôt sur " + JYFServletContext.getParam().getSiteHost()
				+ "\n";
		try {
			SendEmail.Send(from, to, subject, body);
		} catch (AddressException e) {
			logger.log(Level.SEVERE, "AddressException in send email", e);
			throw new Exception(e.getMessage());
		} catch (MessagingException e) {
			logger.log(Level.SEVERE, "MessagingException in send email", e);
			throw new Exception(e.getMessage());
		}
		return null;
	}

	@Override
	public String getTextFile(String filenameP) {
		String filename = filenameP.trim().replaceAll(" ", "_");
		filename = filename + ".htm";
		File file = new File(filename);
		logger.info("getTextFile(" + file.getAbsolutePath() + ")");
		// test if the name is without any parent(s)
		if (file.getName() != filename) {
			logger.warning("file.getName()=[" + file.getName() + "]");
			logger.warning("filename=[" + filename + "]");
			logger.warning("Filename [" + file + "] is not accessible");
			return "<h1>Error 34:</h1><p>The Document [" + filenameP
					+ "] is not accessible</p>";
		}
		String deployPath = JYFServletContext.getDeployDir();
		filename = deployPath + "/textFiles/" + filename;
		logger.info("filename: " + filename);
		file = new File(filename);
		if (file.canRead() == false) {
			logger.warning("Filename [" + file + "] is not readable");
			return "<h1>Error 35:</h1><p>The Document [" + filenameP
					+ "] is not accessible</p>";
		}
		if (!file.isDirectory() == false) {
			logger.warning("Filename [" + file + "] is a directory");
			return "<h1>Error 35.1:</h1><p>The Document [" + filenameP
					+ "] is not accessible</p>";
		}
		try {
			FileInputStream inputStream = new FileInputStream(file);
			String text = IOUtils.toString(inputStream, "UTF-8");
			inputStream.close();
			return text;
		} catch (FileNotFoundException e) {
			logger.warning("problem accessing file [" + file + "]: " + e);
			return "<h1>Error 36:</h1><p>The Document [" + filenameP
					+ "] is not accessible</p>";
		} catch (IOException e) {
			logger.warning("problem accessing file [" + file + "]: " + e);
			return "<h1>Error 36:</h1><p>The Document [" + filenameP
					+ "] is not accessible</p>";
		}
	}

	@Override
	public void startCrawl(String className, String url, int politnessDelay,
			int numberCrawls, int maxDepthCrawling, int maxPagesToFetch,
			int maxAddition, int maxVisiting) throws Exception {
		JYFServletContext.getCrawlerManagement().startCrawl(className, url,
				politnessDelay, numberCrawls, maxDepthCrawling,
				maxPagesToFetch, maxAddition, maxVisiting);
	}

	@Override
	public AdminInfos pullAdminInfo() {
		AdminInfos ret = new AdminInfos();
		ret.dumperInfos = JYFServletContext.getCrawlerManagement()
				.getDumperInfos();
		ret.numberReceipesFromDB = getNumberReceipes();
		ret.numberReceipesFromSolr = (int) getNumberOfReceipes();
		logger.info("pullAdminInfo: numberReceipesFromDB="
				+ ret.numberReceipesFromDB + " numberReceipesFromSolr="
				+ ret.numberReceipesFromSolr);
		return ret;
	}

	@Override
	public void stopCrawl(String className) {
		JYFServletContext.getCrawlerManagement().stopCrawl(className);
	}

	@Override
	public HashMap<String, String> getDumperPlugins() {
		return JYFServletContext.getProviderManagement().getDumpers();
	}

	@Override
	public void reloadParam() {
		JYFServletContext.reloadParam();
	}

	@Override
	public void reinitJYFSerletContext() throws Exception {
		try {
			JYFServletContext.reinitJYFSerletContext();
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public void dumpReceipe(String url, String filename) throws Exception {
		try {
			ReceipeModel receipeModel = getReceipe4RPC_ReadOnly(url);

			receipeModel.prepare4RPCDisplay();

			ReceipeModelXML.dumpReceipeToXML(filename, receipeModel);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error in dumpReceipe(" + url + ","
					+ filename + ")", t);
			throw new Exception(t.getMessage());
		}
	}

	@Override
	public void loadReceipe(String filename) throws Exception {
		try {
			ReceipeModel receipe = ReceipeModelXML.loadReceipeFromXML(filename);

			this.jpaManager.getJpaWriter().persist(receipe);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error in loadReceipe(" + filename + ")",
					t);
			throw new Exception(t.getMessage());
		}
	}

	@Override
	public String getNewsFile(String month, String day) {
		month = month.trim();
		day = day.trim();
		String fileMonth = new File(month).getName();
		if (fileMonth != month) {
			logger.warning("month.getName()=[" + fileMonth + "]");
			logger.warning("month=[" + month + "]");
			logger.warning("Month [" + month + "] is not accessible");
			return "<h1>Erreur 34:</h1><p>Le Document sous [" + month
					+ "] n'est pas accessible</p>";
		}
		String fileDay = new File(day).getName();
		if (fileDay != day) {
			logger.warning("day.getName()=[" + fileDay + "]");
			logger.warning("day=[" + day + "]");
			logger.warning("Filename [" + day + "] is not accessible");
			return "<h1>Error 34.1:</h1><p>The Document [" + day
					+ "] is not accessible</p>";
		}
		String deployPath = JYFServletContext.getDeployDir();
		logger.info("### month=[" + month + "]  day=[" + day + "]");
		String filename = deployPath + "/textFiles/" + month + "/" + day
				+ ".htm";
		logger.info("filename: " + filename);
		File file = new File(filename);
		if (file.canRead() == false) {
			logger.warning("Filename [" + file + "] is not readable");
			return "<h1>Error 35:</h1><p>The Document [" + file
					+ "] is not accessible</p>";
		}
		if (!file.isDirectory() == false) {
			logger.warning("Filename [" + file + "] is a directory");
			return "<h1>Error 35.1:</h1><p>The Document [" + file
					+ "] is not accessible</p>";
		}
		try {
			FileInputStream inputStream = new FileInputStream(file);
			String text = IOUtils.toString(inputStream, "UTF-8");
			inputStream.close();
			return text;
		} catch (FileNotFoundException e) {
			logger.warning("problem accessing file [" + file + "]: " + e);
			return "<h1>Erreur 36:</h1><p>le document [" + file
					+ "] n'est pas accessible</p>";
		} catch (IOException e) {
			logger.warning("problem accessing file [" + file + "]: " + e);
			return "<h1>Erreur 36:</h1><p>le document [" + file
					+ "] n'est pas accessible</p>";
		}
	}

	@Override
	public String updateCiqualDB(String filename) throws Exception {
		try {
			JYFServletContext.getSolrCiqual().clean();
			DBCiqual.clean( jpaManager);
			DBCiqual.createAll(filename, jpaManager, JYFServletContext.getSolCiqual());
			JYFServletContext.getSolrCiqual().optimize();	
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException
				| IOException e) {
			logger.log(Level.SEVERE, "Error when creating Ciqual DB using ["
					+ filename + "]", e);
			throw new Exception(e);
		}
		return "Traité: "+getNumberDBCiqual()+ " ingrédients.";
	}

	@Override
	public String getDeployDir() {
		return JYFServletContext.getDeployDir();
	}

}
