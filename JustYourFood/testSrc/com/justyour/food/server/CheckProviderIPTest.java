package com.justyour.food.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.xml.sax.SAXException;

public class CheckProviderIPTest {

	@Test
	public void testCheck() throws InvalidFormatException, JAXBException,
			IOException, SAXException, AddressException, MessagingException {
		JYFServletContext sc = new JYFServletContext();
		sc.initJYFSerletContext("/justyour.com/");

		CheckProviderIP checkProviderIP = new CheckProviderIP();
		checkProviderIP.check();
		checkProviderIP.check();
		checkProviderIP.check();
	}

	@Test
	public void testScheduledCheck() throws InvalidFormatException,
			JAXBException, IOException {
		JYFServletContext sc = new JYFServletContext();
		sc.initJYFSerletContext("/justyour.com/");
		JYFServletContext.getParam().routerCheckPeriod = "1000";
		CheckProviderIP checkProviderIP = new CheckProviderIP();
		checkProviderIP.start();
		ToolsServer.wait4key("<Enter> to stop the scheduling");
	}

	@Test
	public void testIpInterface() throws SocketException {
		Enumeration <NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
		while(ifs.hasMoreElements()) {
			NetworkInterface netInterface = ifs.nextElement();
			System.out.println("if:"+ToStringBuilder.reflectionToString(netInterface, ToStringStyle.MULTI_LINE_STYLE));
			Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();
			while( inetAddresses.hasMoreElements()) {
				InetAddress inetAddress = inetAddresses.nextElement();
				System.out.println("InetAddress:"+inetAddress.getHostAddress());
				System.out.println("inet:"+ToStringBuilder.reflectionToString(inetAddress, ToStringStyle.MULTI_LINE_STYLE));
				if(inetAddress.getHostAddress().equals("192.168.1.4")) {
					System.out.println("PRODUCTION SERVER");
				}
			}
		}
	}
	
	protected void dumpMatchers(Matcher matcher) {
		int count = 0;
		while (matcher.find()) {
			count++;
			System.out.println("Match number " + count);
			System.out.println("start(): " + matcher.start());
			System.out.println("end(): " + matcher.end());
			System.out.println("end(): " + matcher.group(count));
		}

	}

	@Test
	public void testPattern() {
		String line0 = "					<td>  vfzbvzrz";
		System.out.println("line=[" + line0 + "]");
		Pattern pattern0 = Pattern.compile("^\\s*<([^>]+)>");
		// Pattern pattern = Pattern.compile("\\s*<[^>]+>");
		Matcher matcher0 = pattern0.matcher(line0);
		dumpMatchers(matcher0);

		String line = "					<td class=\"value\">Livebox-48f8</td>";
		System.out.println("line=[" + line + "]");
		Pattern pattern = Pattern.compile("\\s*<[^>]+>([^<]+)<[^>]+>");
		// Pattern pattern = Pattern.compile("\\s*<[^>]+>");
		Matcher matcher = pattern.matcher(line);
		dumpMatchers(matcher);

	}

}
