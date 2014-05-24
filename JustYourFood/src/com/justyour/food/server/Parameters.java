/**
 * 
 */
package com.justyour.food.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author tonio
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "com.justyour.food.server")
public class Parameters {

	static Logger logger = Logger.getLogger(Parameters.class.getName());

	@XmlAttribute(name = "Site_Host")
	private String siteHost;

	@XmlAttribute(name = "server-ipaddress")
	private String serverIpAddress;

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(namespace = "com.justyour.food.server")
	static class Solr {

		@XmlAttribute
		private String home;

		@XmlAttribute
		private String receipeCore;

		@XmlAttribute
		private String ciqualCore;
	}
	
	private Solr solr;

	@XmlAttribute
	private String wget2Win;

	@XmlAttribute
	private String wget2Linux;

	@XmlAttribute
	private String wgetOptions;

	@XmlAttribute
	private String tmp;

	@XmlAttribute(name = "Admins")
	private String[] admins;

	@XmlAttribute(name = "SMPT_Server")
	private String _SMTP_Server;

	@XmlAttribute(name = "SMPT_Auth")
	private String _SMTP_Auth;

	@XmlAttribute(name = "SMPT_Username")
	private String _SMTP_Username;

	@XmlAttribute(name = "SMPT_Password")
	private String _SMTP_Password;

	@XmlAttribute
	private String filename;

	/*
	 * ROUTER
	 */
	@XmlAttribute(name = "routerURL")
	String routerURL;

	@XmlAttribute(name = "routerType")
	String routerType;

	@XmlAttribute(name = "routerCheckPeriod")
	String routerCheckPeriod;

	@XmlAttribute(name = "jyf-classpath")
	private String[] jyfClassPath;

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(namespace = "com.justyour.food.server")
	static class DumperDeclaration {
		public DumperDeclaration() {
		}

		public DumperDeclaration(String className, String urls) {
			super();
			this.className = className;
			this.urls = urls;
		}

		@XmlAttribute
		String className;

		@XmlAttribute
		String urls;
	}

	@XmlAttribute(name = "DetectDumper", required = false)
	private static boolean detectDumper = false;

	@XmlElement(name = "dumper-declarations")
	private DumperDeclaration[] dumperDeclarations;

	/**
	 * @param serverIpAddress
	 *            the serverIpAddress to set
	 */
	public void setServerIpAddress(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}

	/**
	 * @return the serverIpAddress
	 */
	public String getServerIpAddress() {
		return serverIpAddress;
	}

	/**
	 * @return the routerURL
	 */
	public String getRouterURL() {
		return routerURL;
	}

	/**
	 * @return the routerType
	 */
	public String getRouterType() {
		return routerType;
	}

	/**
	 * @return the routerCheckPeriod
	 */
	public String getRouterCheckPeriod() {
		return routerCheckPeriod;
	}

	/**
	 * @return the dumperDeclarations
	 */
	public DumperDeclaration[] getDumperDeclarations() {
		return dumperDeclarations;
	}

	/**
	 * @param dumperDeclarations
	 *            the dumperDeclarations to set
	 */
	public void setDumperDeclarations(DumperDeclaration[] dumperDeclarations) {
		this.dumperDeclarations = dumperDeclarations;
	}

	/**
	 * @return the jyfClassPath
	 */
	public String[] getJyfClassPath() {
		return jyfClassPath;
	}

	/**
	 * @param jyfClassPath
	 *            the jyfClassPath to set
	 */
	public void setJyfClassPath(String[] jyfClassPath) {
		this.jyfClassPath = jyfClassPath;
	}

	public Parameters() {

	}

	public String getWget() {
		switch (JYFServletContext.getOS()) {
		case LINUX:
			return wget2Linux;
		case WINDOWS:
			return wget2Win;
		default:
			return wget2Linux;
		}
	}

	public void setWget2Win(String wget2Win) {
		this.wget2Win = wget2Win;
	}

	public void setWget2Linux(String wget2Linux) {
		this.wget2Linux = wget2Linux;
	}

	public String getWgetOptions() {
		return wgetOptions;
	}

	public void setWgetOptions(String wgetOptions) {
		this.wgetOptions = wgetOptions;
	}

	/**
	 * Save parameters in XML format
	 * 
	 * @param filename
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void save(String filename) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(Parameters.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(this, new FileWriter(filename));
	}

	/**
	 * Build a Parameters object from a XML file
	 * 
	 * @param filename
	 * @return
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public static Parameters getParameters(String filename)
			throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(Parameters.class);
		Unmarshaller um = context.createUnmarshaller();
		Parameters param = (Parameters) um.unmarshal(new FileReader(filename));
		param.filename = filename;
		return param;
	}

	@Override
	public String toString() {
		String ret = ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
		for (DumperDeclaration dumper : this.dumperDeclarations) {
			ret += ToStringBuilder.reflectionToString(dumper,
					ToStringStyle.MULTI_LINE_STYLE);
		}
		for (String classpath : this.jyfClassPath) {
			ret += ToStringBuilder.reflectionToString(classpath,
					ToStringStyle.MULTI_LINE_STYLE);
		}
		return ret;
	}

	public String getWorkingTmp() {
		return JYFServletContext.getJYFHome() + tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public String[] getJYFClassPath() {
		return this.jyfClassPath;
	}

	/**
	 * @return the siteHost
	 */
	public String getSiteHost() {
		return siteHost;
	}

	/**
	 * @param siteHost
	 *            the siteHost to set
	 */
	public void setSiteHost(String siteHost) {
		this.siteHost = siteHost;
	}

	/**
	 * @return the admins
	 */
	public String[] getAdmins() {
		return admins;
	}

	/**
	 * @param admins
	 *            the admins to set
	 */
	public void setAdmins(String[] admins) {
		this.admins = admins;
	}

	/**
	 * @return the _SMTP_Server
	 */
	public String get_SMTP_Server() {
		return _SMTP_Server;
	}

	/**
	 * @param _SMTP_Server
	 *            the _SMTP_Server to set
	 */
	public void set_SMTP_Server(String _SMTP_Server) {
		this._SMTP_Server = _SMTP_Server;
	}

	/**
	 * @return the _SMTP_Auth
	 */
	public String get_SMTP_Auth() {
		return _SMTP_Auth;
	}

	/**
	 * @param _SMTP_Auth
	 *            the _SMTP_Auth to set
	 */
	public void set_SMTP_Auth(String _SMTP_Auth) {
		this._SMTP_Auth = _SMTP_Auth;
	}

	/**
	 * @return the _SMTP_Username
	 */
	public String get_SMTP_Username() {
		return _SMTP_Username;
	}

	/**
	 * @param _SMTP_Username
	 *            the _SMTP_Username to set
	 */
	public void set_SMTP_Username(String _SMTP_Username) {
		this._SMTP_Username = _SMTP_Username;
	}

	/**
	 * @return the _SMTP_Password
	 */
	public String get_SMTP_Password() {
		return _SMTP_Password;
	}

	/**
	 * @param _SMTP_Password
	 *            the _SMTP_Password to set
	 */
	public void set_SMTP_Password(String _SMTP_Password) {
		this._SMTP_Password = _SMTP_Password;
	}

	/**
	 * @return the wget2Win
	 */
	public String getWget2Win() {
		return wget2Win;
	}

	/**
	 * @return the wget2Linux
	 */
	public String getWget2Linux() {
		return wget2Linux;
	}

	/**
	 * @return the tmp
	 */
	public String getTmp() {
		return tmp;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	public static boolean getDetectDumper() {
		return detectDumper;
	}

	public Solr getSolr() {
		return solr;
	}

	public void setSolr(Solr solr) {
		this.solr = solr;
	}

	public void setSolrHome(String solrHome) {
		this.solr.home = solrHome;
	}

	public String getSolrHome() {
		return solr.home;
	}
	
	public String getSolrReceipeCore() {
		return solr.receipeCore;
	}

	public void setSolrReceipeCore(String receipeCore) {
		solr.receipeCore = receipeCore;
	}

	public void setSolrCiqualCore(String ciqualCore) {
		solr.ciqualCore = ciqualCore;
	}

	public String getSolrCiqualCore() {
		return solr.ciqualCore;
	}

}
