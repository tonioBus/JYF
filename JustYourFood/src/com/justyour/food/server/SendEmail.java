/**
 * 
 */
package com.justyour.food.server;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author tonio
 * 
 */
public class SendEmail {

	public static void Send(String from, String to, String subject, String msg)
			throws AddressException, MessagingException {
		Message message = new MimeMessage(getSession());
		message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		for (String admin : JYFServletContext.getParam().getAdmins()) {
			message.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(admin));
		}
		message.addFrom(new InternetAddress[] { new InternetAddress(from) });
		message.setSubject(subject);
		message.setContent(msg, "text/plain");
		Transport.send(message);
	}

	private static Session getSession() {
		Authenticator authenticator = new Authenticator();

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.submitter", authenticator
				.getPasswordAuthentication().getUserName());
		properties
				.setProperty("mail.smtp.auth", JYFServletContext.getParam().get_SMTP_Auth());
		properties.setProperty("mail.smtp.host", JYFServletContext.getParam().get_SMTP_Server());
		properties.setProperty("mail.smtp.port", "25");
		if (JYFServletContext.getParam().get_SMTP_Auth().equalsIgnoreCase("true"))
			return Session.getInstance(properties, authenticator);
		else
			return Session.getInstance(properties);
	}

	private static class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;

		public Authenticator() {
			String username = JYFServletContext.getParam().get_SMTP_Username();
			String password = JYFServletContext.getParam().get_SMTP_Password();
			authentication = new PasswordAuthentication(username, password);
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}
}
