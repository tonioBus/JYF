/**
 * 
 */
package com.justyour.food.server.jpa;

import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.justyour.food.server.JYFServletContext;

/**
 * @author tonio
 * 
 */
public class JPATransaction {

	static Logger logger = Logger.getLogger(JPATransaction.class.getName());

	public static final String PERSISTENCE_UNIT_NAME = "JustYourFood";

	private EntityManagerFactory factory = null;
	private EntityManager em = null;
	private EntityTransaction transaction = null;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public EntityTransaction getTransaction() {
		return this.transaction;
	}

	public JPATransaction() {
		if (this.factory == null) {
			Properties prop = new Properties();
			prop.put("eclipselink.application-location", JYFServletContext.getParam().getWorkingTmp());
			prop.put("javax.persistence.jdbc.url", "jdbc:derby:"+JYFServletContext.getJYFHome()+"JustYourFoodDb;create=true");			
			this.factory = Persistence
					.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, prop);
			/*
			 * field="javax.persistence.jdbc.url" 
			 * value="jdbc:derby:C:\justyour.com\JustYourFoodDb"
			 */
			System.out.println("Persistence: jdbc.url=["+this.factory.getProperties().get("javax.persistence.jdbc.url")+"]");
			this.em = factory.createEntityManager();
			this.transaction = this.em.getTransaction();
			this.transaction.begin();
		}
	}

	public void closeHandler() {
		if( this.transaction.isActive()) this.transaction.commit();
		this.transaction = null;
		this.em.close();
		this.em = null;
		this.factory.close();
		this.factory = null;
	}

}
