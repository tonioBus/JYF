package com.justyour.food.test;

import org.junit.Test;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.shared.jpa.models.UserID;

public class adminUserID {

	JYFServiceImpl server = new JYFServiceImpl(true, false, false);

	@Test
	public void populate() throws Exception {
		String prenoms;
		String nom;
		String email;
		String password;

		// register admin
		prenoms = "admin";
		nom = "justyourfood";
		email = "admin@justyourfood.com";
		password = "hip4tdah";
		UserID userId = new UserID(prenoms, nom, email, password);
		userId.setAdmin(true);
		server.register(userId);

		// register moi
		prenoms = "Tonio";
		nom = "Bussani";
		email = "bussania@gmail.com";
		password = "Mougins4945";
		userId = new UserID(prenoms, nom, email, password);
		userId.setAdmin(false);
		server.register(userId);

	}

}
