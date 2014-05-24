/** 
 * Project: JustYourFood
 * (c) 2014 Aquila Services
 * 
 * File: CheckProviderIP.java
 * Package: com.justyour.food.server
 * Date: Jan 9, 2014
 * @author Anthony Bussani (bussania@gmail.com)
 * 
 */

package com.justyour.food.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author tonio
 * 
 */
public class CheckProviderIP implements Runnable {
	static Logger logger = Logger.getLogger(CheckProviderIP.class.getName());
	boolean isStopping = false;
	String typeRouter;
	URL urlRouter;
	String routerInfoFilename;
	ScheduledExecutorService exec = Executors
			.newSingleThreadScheduledExecutor();

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(namespace = "com.justyour.food.server")
	static class RouterInfo {
		// 1.1 Nom de routeur : Livebox-48f8
		String name;
		// 1.2 Version logicielle : FAST3XXX_6814BC
		String softwareRelease;
		// 1.3 Adresse IP WAN : 86.205.182.170
		String ipWlan;
		// 1.10 Adresse MAC de la Livebox : 00:26:91:8b:48:f8
		String macAddress;

		public void save(String filename) throws JAXBException, IOException {
			JAXBContext context = JAXBContext.newInstance(RouterInfo.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(this, new FileWriter(filename));
		}

		public static RouterInfo getFromFile(String filename)
				throws JAXBException, FileNotFoundException {
			JAXBContext context = JAXBContext.newInstance(RouterInfo.class);
			Unmarshaller um = context.createUnmarshaller();
			RouterInfo routerInfo = (RouterInfo) um.unmarshal(new FileReader(
					filename));
			return routerInfo;
		}

	};

	public CheckProviderIP() throws MalformedURLException {
		String url = JYFServletContext.getParam().routerURL;
		typeRouter = JYFServletContext.getParam().routerType;
		urlRouter = new URL(url);
		routerInfoFilename = JYFServletContext.getParam().getWorkingTmp()
				+ "/RouterInfo.xml";
	}

	protected String getFieldFromHTML(String line) {
		Pattern pattern = Pattern.compile("^\\s*<[^>]+>([^<]+)<[^>]+>");
		Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}

	}

	public RouterInfo readRouterStatus() throws IOException {
		RouterInfo routerInfo = new RouterInfo();
		InputStreamReader isr = new InputStreamReader(urlRouter.openStream());
		BufferedReader in = new BufferedReader(isr);
		String inputLine;
		// grab the contents at the URL
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains("Nom de routeur :")) {
				if ((inputLine = in.readLine()) != null) {
					routerInfo.name = getFieldFromHTML(inputLine);
				}
			}
			if (inputLine.contains("Version logicielle :")) {
				if ((inputLine = in.readLine()) != null) {
					routerInfo.softwareRelease = getFieldFromHTML(inputLine);
				}
			}
			if (inputLine.contains("Adresse IP WAN :")) {
				if ((inputLine = in.readLine()) != null) {
					routerInfo.ipWlan = getFieldFromHTML(inputLine);
				}
			}
			if (inputLine.contains("Adresse MAC de la Livebox :")) {
				if ((inputLine = in.readLine()) != null) {
					routerInfo.macAddress = getFieldFromHTML(inputLine);
				}
			}
		}
		in.close();
		return routerInfo;
	}

	public void check() throws IOException, JAXBException, AddressException,
			MessagingException {
		RouterInfo currentRouterInfo = readRouterStatus();
		// logger.fine("CHECK ROUTER: scheduled time: "
		// + Long.valueOf(JYFServletContext.getParam().routerCheckPeriod));
		// logger.fine("CHECK ROUTER: Current RouterInfo:"
		// + ToStringBuilder.reflectionToString(currentRouterInfo));
		RouterInfo savedRouterInfo;
		try {
			savedRouterInfo = RouterInfo.getFromFile(this.routerInfoFilename);
			// logger.fine("CHECK ROUTER: Current savedRouterInfo:"
			// + ToStringBuilder.reflectionToString(savedRouterInfo));
		} catch (IOException e) {
			logger.warning("bad or not found RouterInfo file. Writing a new one.");
			warn(currentRouterInfo, null);
			currentRouterInfo.save(routerInfoFilename);
			return;
		}
		if (savedRouterInfo.ipWlan.equals(currentRouterInfo.ipWlan) == false) {
			logger.warning("CHECK ROUTER: currentRouter["
					+ currentRouterInfo.ipWlan + "] != savedRouter["
					+ savedRouterInfo.ipWlan + "]. Writing a new one.");
			warn(currentRouterInfo, savedRouterInfo);
			currentRouterInfo.save(routerInfoFilename);
		}
		// else {
		// logger.fine("CHECK ROUTER: Ok");
		// }
	}

	private void warn(RouterInfo currentRouterInfo, RouterInfo savedRouterInfo)
			throws AddressException, MessagingException, JAXBException,
			IOException {
		// Recipient's email ID needs to be mentioned.
		String to = JYFServletContext.getParam().getAdmins()[0];

		// Sender's email ID needs to be mentioned
		String from = JYFServletContext.getParam().getAdmins()[0];

		String subject = "!!! PRIORITAIRE: "
				+ JYFServletContext.getParam().getSiteHost()
				+ ": Verification du router: changement de configuration !!!";
		String savedInfo;
		if (savedRouterInfo == null) {
			savedInfo = "!! Pas d'informations lisible dans ["
					+ routerInfoFilename + "]\n";
		} else {
			savedInfo = ToStringBuilder.reflectionToString(savedRouterInfo,
					ToStringStyle.MULTI_LINE_STYLE);
		}
		String currentInfo = ToStringBuilder.reflectionToString(
				currentRouterInfo, ToStringStyle.MULTI_LINE_STYLE);

		String body = "!!!! Le router "
				+ currentRouterInfo.name
				+ " a changé de configuration !!!!\n"
				+ "ANCIENNE CONFIG:\n"
				+ savedInfo
				+ "NOUVELLE CONFIG:\n"
				+ currentInfo
				+ "\n"
				+ "\n"
				+ "La nouvelle adresse IP: \n"
				+ currentRouterInfo.ipWlan
				+ "\nLe DNS peut être changer directement au:\n"
				+ "https://dns.godaddy.com/ZoneFile.aspx?zone=justyourfood.com&zoneType=0&sa=%2526isc%253dgtnifr15&marketid=en-US&regionsite=www"
				+ "\nLe forward ce trouve au:\n"
				+ "https://dcc.godaddy.com/dcc50/DomainDetails.aspx?identifier=1YVW8GJ8wU4AijJqhk0hSvfJMFfI0wnMGeQyZAQ%2fTA0sJbIrwQBSrsbb5Rgf9ht89L9XWGKcmCM%3d&sa=isc%3dgtnifr23&prog_id=&marketid=en-US&regionsite=www"
				+ "\nou\n" + "http://www.godaddy.com/"
				+ "\n----------------------\n";
		logger.warning("SENDING EMAIL:\n" + body);
		try {
			SendEmail.Send(from, to, subject, body);
		} catch (Throwable t) {
			logger.severe("Send Email error:" + t);
			return;
		}
		logger.warning("saving Retour Info in: " + this.routerInfoFilename);
		currentRouterInfo.save(this.routerInfoFilename);
	}

	public void start() {
		this.exec.schedule(this, 0, TimeUnit.MILLISECONDS);
	}

	public void stop() {
		isStopping = true;
		this.exec.shutdown();
	}

	@Override
	public void run() {
		try {
			check();
		} catch (IOException | JAXBException | MessagingException e) {
			logger.log(Level.SEVERE, "Error in scheduled check Router:", e);
		}
		this.exec.shutdown();
		if (JYFServletContext.isProductionServer() && isStopping == false) {
			this.exec = Executors.newSingleThreadScheduledExecutor();
			this.exec
					.schedule(
							this,
							Long.valueOf(JYFServletContext.getParam().routerCheckPeriod),
							TimeUnit.MILLISECONDS);
		}
	}
}
