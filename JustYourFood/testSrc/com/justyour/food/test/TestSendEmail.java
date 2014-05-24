package com.justyour.food.test;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;

import com.justyour.food.server.SendEmail;

public class TestSendEmail {

	@Test
	public void testSendEmail() throws AddressException, MessagingException {
		// Recipient's email ID needs to be mentioned.
		String to = "bussania@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "admin@justyour.co";

		// Set Subject: header field
		String subject = "(JustYourFood) Confirmation d'inscription";

		// Now set the actual message
		String body = "Bonjour "
				+ "userProfile.getPrenoms()"
				+ " "
				+ "userProfile.getNom()"
				+ ",\n"
				+ "\n"
				+ "Afin de valider votre inscription sur JustYourFood, suivez le lien ci-dessous pour activer votre compte :\n"
				+ "http://nutrition.justyour.co/#ConfirmRegister;"
				+ UUID.randomUUID().toString() + ";\n" + "\n"
				+ "Voici un récapitulatif de vos identifiants :\n" + "\n"
				+ "Login : " + "userProfile.getEmail()" + " Mot de passe : "
				+ "userProfile.getPassword()" + "\n" + "\n"
				+ "------------------------------------------\n"
				+ "Ceci est un message automatique du site JustYourFood.\n"
				+ "À bientôt sur http://nutrition.justyour.co/\n";
		SendEmail.Send(from, to, subject, body);
	}

	@Test
	public void testSendEmail1() throws AddressException, MessagingException {
		SendEmail.Send("admin@justyour.co", "bussania@gmail.com",
				"the subject", "the body");
	}

}
