/** 
 * (c) 2013 Aquila Services
 * 
 * File: JPAQuery.java
 * Package: com.aquilaservices.metareceipe.jpa
 * Date: 5 ao�t 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.eclipse.persistence.config.CacheUsage;

import com.justyour.food.shared.jpa.models.Ingredient;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.shared.jpa.models.UserID;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> JPAQuery.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.jpa</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * 
 * @author tonio
 * 
 */
public class JPAQuery {

	static Logger logger = Logger.getLogger(JPAQuery.class.getName());

	JPAManager jpaManager = null;

	public JPAQuery(JPAManager jpaManager) {
		this.jpaManager = jpaManager;
	}

	/**
	 * Examples of query:<br/>
	 * for all recipes:<br/>
	 * <code>
	 * select r from ReceipeModel r
	 * </code>
	 * 
	 * @param query
	 *            - see query examples above
	 * @return - list of ReceipeModel
	 */
	@SuppressWarnings("unchecked")
	public ReceipeModel[] getReceipes(String query, int max, boolean readOnly) {
		List<ReceipeModel> receipes = null;

		logger.info(query);
		try {
			Query q = this.jpaManager.getEm().createQuery(query);
			q.setHint("eclipselink.read-only", readOnly + "");
			q.setHint("eclipselink.query-results-cache", "true");
			if (max > 0) {
				receipes = q.setMaxResults(max).getResultList();
			} else {
				receipes = q.getResultList();
			}
		} catch (Exception e) {
			throw e;
		}
		return receipes.toArray(new ReceipeModel[0]);
	}

	public String[] getNativeQuery(String query, boolean readOnly) {
		logger.info("get Native Query: " + query);
		Query q = this.jpaManager.getEm().createNativeQuery(query);
		q.setHint("eclipselink.read-only", readOnly + "");
		q.setHint("eclipselink.query-results-cache", "true");

		@SuppressWarnings("rawtypes")
		List list = q.getResultList();
		String[] ret = new String[list.size()];
		int i = 0;
		for (Object object : list) {
			ret[i] = object.toString();
		}
		return ret;
	}

	public int executeNativeQuery(String query) {
		logger.info("Execute Native Query: " + query);
		Query q = this.jpaManager.getEm().createNativeQuery(query);
		int ret = q.executeUpdate();
		return ret;
	}

	/**
	 * Examples of query:<br/>
	 * for all receipes:<br/>
	 * <code>
	 * select i from Ingredient i where i.ingredient = '3 oignons nouveaux'
	 * </code>
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Ingredient[] getIngredients(String ingredient, boolean like,
			boolean readOnly, int max) {
		List<Ingredient> ingredients = null;

		logger.info("getIngredients(" + ingredient + ",like:" + like
				+ ", readOnly:" + readOnly + ",max:" + max + ")");
		try {
			String operator = like ? "LIKE" : "=";
			Query q = this.jpaManager.getEm().createQuery(
					"select i from " + Ingredient.class.getSimpleName()
							+ " i where i.ingredient " + operator
							+ " :ingredient");
			q.setParameter("ingredient", ingredient);
			if (readOnly)
				q.setHint("eclipselink.read-only", "true");
			q.setHint("eclipselink.query-results-cache", "true");
			logger.info(q.toString());
			if (max > 0) {
				ingredients = q.setMaxResults(max).getResultList();
			} else {
				ingredients = q.getResultList();
			}
		} catch (Exception e) {
			throw e;
		}
		logger.info("Ingredient found:" + ingredients.size());
		return ingredients.toArray(new Ingredient[0]);
	}

	/**
	 * @param ingredient
	 *            - ingredient
	 * @return
	 */
	public Ingredient getIngredient(String ingredientSz) {
		Ingredient ingredient = this.jpaManager.getEm().find(Ingredient.class,
				ingredientSz);
		return ingredient;
	}

	public ReceipeModel getReceipeWrite(String link) {
		HashMap<String, Object> prop = new HashMap<>();
		prop.put("eclipselink.read-only", "true");

		ReceipeModel receipe = this.jpaManager.getEm().find(ReceipeModel.class,
				link, prop);
		return receipe;
	}

	public ReceipeModel getReceipeReadOnly(String link) {
		System.out.println("getReceipeReadOnly(" + link + ")");
		Query q = this.jpaManager.getEm().createQuery(
				"SELECT r FROM " + ReceipeModel.class.getSimpleName()
						+ " r WHERE r.link = :link");
		q.setParameter("link", link);
		q.setHint("eclipselink.read-only", "true");
		q.setHint("eclipselink.maintain-cache", "false");
		q.setHint("eclipselink.cache-usage", CacheUsage.DoNotCheckCache);
		q.setHint("eclipselink.query-results-cache", "false");
		q.setHint("eclipselink.refresh", "false");
		ReceipeModel receipe = (ReceipeModel) q.getSingleResult();
		return receipe;
	}

	public CiqualModel getCiqualReadOnly(String name) {
		Query q = this.jpaManager.getEm().createQuery(
				"SELECT c FROM " + CiqualModel.class.getSimpleName()
						+ " c WHERE c.mORIGFDNM = :name");
		q.setParameter("name", name);
		q.setHint("eclipselink.read-only", "true");
		q.setHint("eclipselink.maintain-cache", "false");
		q.setHint("eclipselink.cache-usage", CacheUsage.DoNotCheckCache);
		q.setHint("eclipselink.query-results-cache", "false");
		q.setHint("eclipselink.refresh", "false");
		CiqualModel ciqualModel = (CiqualModel) q.getSingleResult();
		return ciqualModel;
	}

	@Deprecated
	public ReceipeModel getReceipeOld(String link) {

		try {
			Query q = this.jpaManager.getEm().createQuery(
					"SELECT r FROM " + ReceipeModel.class.getSimpleName()
							+ " r WHERE r.link LIKE :link");
			q.setParameter("link", link);
			q.setHint("eclipselink.read-only", "true");
			q.setHint("eclipselink.query-results-cache", "true");
			@SuppressWarnings("unchecked")
			List<ReceipeModel> receipes = q.getResultList();
			if (receipes.size() > 0)
				return receipes.get(0);
			else
				return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public synchronized int getReceipesSize() {
		try {
			Query q = this.jpaManager.getEm().createQuery(
					"SELECT COUNT(r) FROM "
							+ ReceipeModel.class.getSimpleName() + " r");
			q.setHint("eclipselink.read-only", "true");
			q.setHint("eclipselink.query-results-cache", "true");
			Number countResult = (Number) q.getSingleResult();
			return countResult.intValue();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in getReceipesSize", e);
			return 0;
		}
	}

	public synchronized List<ReceipeModel> getAllReceipes() {
		try {
			Query q = this.jpaManager.getEm().createQuery(
					"SELECT r FROM " + ReceipeModel.class.getSimpleName()
							+ " r");
			q.setHint("eclipselink.read-only", "true");
			q.setHint("eclipselink.query-results-cache", "true");
			@SuppressWarnings("unchecked")
			List<ReceipeModel> receipes = (List<ReceipeModel>) q
					.getResultList();
			return receipes;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in getAllReceipes", e);
			return new ArrayList<ReceipeModel>();
		}
	}

	public int getIngredientsSize() {

		try {
			Query q = this.jpaManager.getEm().createQuery(
					"SELECT COUNT(i) FROM " + Ingredient.class.getSimpleName()
							+ " i");
			q.setHint("eclipselink.read-only", "true");
			q.setHint("eclipselink.query-results-cache", "true");
			Number countResult = (Number) q.getSingleResult();
			return countResult.intValue();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in getIngredientsSize", e);
			return 0;
		}
	}

	public ArrayList<String> infos() {
		ArrayList<String> retArray = new ArrayList<String>();

		retArray.add("Recettes stockées");
		retArray.add(getReceipesSize() + "");
		retArray.add("Number of Ingredients stockés");
		retArray.add(getIngredientsSize() + "");

		Map<String, Object> map = this.jpaManager.getEm().getProperties();
		for (String key : map.keySet()) {
			retArray.add(key);
			retArray.add(map.get(key).toString());
		}
		retArray.add("Isactive ?");
		retArray.add(this.jpaManager.getTransaction().isActive() + "");
		retArray.add("Flush Mode");
		retArray.add(this.jpaManager.getEm().getFlushMode().name());
		return retArray;
	}

	public boolean isStored(String link) {
		return this.jpaManager.getJpaQuery().getReceipeWrite(link) != null;
	}

	public void close() {

	}

	public int getCiqualSize() {
		try {
			Query q = this.jpaManager.getEm().createQuery(
					"SELECT COUNT(r) FROM " + CiqualModel.class.getSimpleName()
							+ " r");
			q.setHint("eclipselink.read-only", "true");
			q.setHint("eclipselink.query-results-cache", "true");
			Number countResult = (Number) q.getSingleResult();
			return countResult.intValue();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in getCiqualSize", e);
			return 0;
		}
	}

	public UserID getUserProfile(String email) {
		UserID userProfile = this.jpaManager.getEm().find(UserID.class, email);
		return userProfile;
	}

}
