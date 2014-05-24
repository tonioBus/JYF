/**
 *
 * (c) 2013 Aquila Services
 * 
 * File: JPAWriter.java
 * Package: com.aquilaservices.metareceipe.jpa
 * Date: 5 ao�t 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.jpa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.persistence.Query;

import opennlp.tools.util.InvalidFormatException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.eclipse.persistence.exceptions.DatabaseException;

import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.shared.jpa.models.Ingredient;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.shared.jpa.models.UserID;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> CACA.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.jpa</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * 
 * @author tonio
 * 
 */
public class JPAWriter {

	static Logger logger = Logger.getLogger(JPAWriter.class.getName());

	private static final String UNKNOW_INGREDIENT = "?UNKNOW_INGREDIENT?"
			.toLowerCase();

	private static final String DOUBLING_INGREDIENT = "?DOUBLING_INGREDIENT?"
			.toLowerCase();

	JPAManager jpaManager = null;

	public JPAWriter(JPAManager jpaManager) {
		this.jpaManager = jpaManager;
	}
	
	public int cleanReceipes() {
		int ret = this.jpaManager.jpaQuery.executeNativeQuery("DELETE FROM "+ReceipeModel.class.getSimpleName());
		logger.info("Deleted "+ret+" receipe(s).");
		return ret;
	}
	
	public int cleanCiqual() {
		int ret = this.jpaManager.jpaQuery.executeNativeQuery("DELETE FROM "+CiqualModel.class.getSimpleName());
		logger.info("Deleted "+ret+" receipe(s).");
		return ret;		
	}

	public ReceipeModel create(String source, String domain) {
		ReceipeModel receipe = new ReceipeModel();
		receipe.setCreationDate(new Date());
		receipe.setDomain(domain);
		receipe.setNumber(0);// jpaManager.jpaQuery.getReceipesSize());
		receipe.setSource(source);
		return receipe;
	}

	public void flush(CiqualModel model) {
		this.jpaManager.getEm().persist(model);
		// necessary to find previously register ingredients
		try {
			this.jpaManager.getTransaction().commit();
		} catch (DatabaseException e) {
			logger.severe("COMMIT ERROR:" + e.getQuery().toString());
			throw e;
		}
		this.jpaManager.getTransaction().begin();
	}

	public synchronized void persist(ReceipeModel receipe) {
		logger.info("BEFORE PERSIST[" + receipe.getNumber() + "]:"
				+ receipe.getSource());
		consolidate(receipe);
		logger.info(receipe.toString());
		this.jpaManager.getEm().persist(receipe);
		// necessary to find previously register ingredients
		// try {
		// this.jpaManager.getTransaction().commit();
		// } catch (DatabaseException e) {
		// logger.severe("COMMIT ERROR:" + e.getQuery().toString());
		// throw e;
		// }
		// this.jpaManager.getTransaction().begin();
		logger.info("END PERSIST[" + receipe.getNumber() + "]:"
				+ receipe.getSource());
	}

	public void flushReceipes() {
		logger.info("BEGIN flushReceipes()");
		if (this.jpaManager.getTransaction().isActive() == false) {
			logger.warning("flushReceipes() quitting, no active transaction");
		} else {
			try {
				this.jpaManager.getTransaction().commit();
			} catch (DatabaseException e) {
				logger.severe("COMMIT ERROR:" + e.getQuery().toString());
				throw e;
			}
		}
		this.jpaManager.getTransaction().begin();
		logger.info("END flushReceipes()");
	}

	public List<IngredientQuantity> compileIQ(String ingredientLine,
			ReceipeModel receipe, IngredientNLP ingredientNLP)
			throws InvalidFormatException, IOException {
		ArrayList<IngredientQuantity> iqs = ingredientNLP
				.analyzeIngredients(ingredientLine);
		return iqs;
	}

	
	protected void consolidate(ReceipeModel receipe) {
		Hashtable<String, Ingredient> currentIngredients = new Hashtable<String, Ingredient>();

		receipe.normalize();
		// the following operation is call to set the Ingredient for each IQ using
		// the IngredientSz
		for (IngredientQuantity iqIter : receipe.getIngredients()) {
			IngredientQuantity iq = iqIter;
			do {
				String ingredientSz = iq.getIngredientSz();
				if (ingredientSz != null && ingredientSz.isEmpty() == false) {
					if (currentIngredients.containsKey(ingredientSz)) {
						logger.severe("doubling ingredient [" + ingredientSz
								+ "]");
						int index = 0;
						String replaceIngredientSz;
						do {
							replaceIngredientSz = DOUBLING_INGREDIENT + " ["
									+ index + "][" + iq.getDetails()
									+ "]".toLowerCase();
							index++;
							// the following modify ingredientSz (normalizing)
							iq.setIngredientSz(replaceIngredientSz);
							ingredientSz = iq.getIngredientSz();
						} while (currentIngredients.containsKey(ingredientSz));
						logger.severe("ingredientSz is DOUBLED");
						logger.severe("Receipe:\n" + receipe.getLink());
						logger.severe("IQ:\n" + iq);
						iq.setInterpretationLevel(iq.getInterpretationLevel() - 0.2);
					}
				} else {
					// INGREDIENTSZ is empty or null
					int index = 0;
					String replaceIngredientSz;
					do {
						replaceIngredientSz = UNKNOW_INGREDIENT + " [" + index
								+ "][" + iq.getDetails() + "]".toLowerCase();
						index++;
						// the following modify ingredientSz (normalizing)
						iq.setIngredientSz(replaceIngredientSz);
						ingredientSz = iq.getIngredientSz();
					} while (currentIngredients.containsKey(ingredientSz));
					iq.setInterpretationLevel(iq.getInterpretationLevel() - 0.2);
					logger.severe("ingredientSz is NULL");
					logger.severe("Receipe:\n" + receipe.getLink());
					logger.severe("IQ:\n" + iq);
				}
				logger.info("Querying[" + ingredientSz + "]");
				Ingredient queryIngredient = jpaManager.jpaQuery
						.getIngredient(ingredientSz);
				logger.info("END Querying[" + ingredientSz + "] -> "
						+ queryIngredient);
				Ingredient ingredient;
				if (queryIngredient != null) {
					ingredient = queryIngredient;
					logger.info("Modifying QI:\n" + ingredient);
				} else {
					logger.info("CREATING NEW INGREDIENT FOR [" + ingredientSz
							+ "]");
					ingredient = new Ingredient();
					ingredient.setIngredient(ingredientSz);
				}
				ingredient.getReceipes().add(receipe);
				iq.setIngredient(ingredient);
				currentIngredients.put(ingredientSz, ingredient);
				logger.info("Persisting IQ:" + iq);
				logger.info("current Ingredient:" + ingredient);
				iq = iq.getOrNext();
			} while (iq != null);
		}
	}

	/*
	public IngredientQuantity flushIQ_old(String ingredientSz,
			ReceipeModel receipe, Hashtable<String, String> currentIngredient) {
		Ingredient ingredient = new Ingredient();
		IngredientQuantity iq = new IngredientQuantity();

		Parser.parseIngredient(ingredientSz, ingredient, iq);
		Ingredient queryIngredient = jpaManager.jpaQuery
				.getIngredient(ingredient.getIngredient());
		if (currentIngredient.containsKey(ingredient.getIngredient())) {
			return null;
		}
		currentIngredient.put(ingredient.getIngredient(), ingredientSz);
		logger.info("WRITING INGREDIENT[" + ingredient.getIngredient()
				+ "] -> " + queryIngredient);
		if (queryIngredient != null) {
			queryIngredient.getReceipes().add(receipe);
			iq.setIngredient(queryIngredient);
		} else {
			ingredient.getReceipes().add(receipe);
			iq.setIngredient(ingredient);
			this.jpaManager.getEm().persist(ingredient);
		}
		this.jpaManager.getEm().persist(iq);
		return iq;
	}*/

	@SuppressWarnings("unchecked")
	public void updateIngredientQuantities(int max) {
		Query q = this.jpaManager.getEm().createQuery(
				"select iq from "+IngredientQuantity.class.getSimpleName()+" iq");
		q.setHint("eclipselink.query-results-cache", "true");
		logger.info(q.toString());
		List<IngredientQuantity> iq;
		if (max > 0) {
			iq = q.setMaxResults(max).getResultList();
		} else {
			iq = q.getResultList();
		}
		for (IngredientQuantity ingredientQuantity : iq) {
			System.out.println("IngredientQuantity: " + ingredientQuantity);
			String allSentence = ingredientQuantity.getIngredient()
					.getIngredient();
			System.out.println("sentence=[" + allSentence + "]");
			System.out.println("iq=[" + ingredientQuantity + "]");
			updateIQ_FR(ingredientQuantity, allSentence);
			// wait4key("iq:" + ingredientQuantity);
		}

	}

	enum Status {
		NUMBER, USTENSIL, INGREDIENT
	};

	private void updateIQ_FR(IngredientQuantity iq, String allSentence) {
		StringTokenizer st = new StringTokenizer(allSentence, " \n\t,;:.");

		Status status = Status.NUMBER;
		double quantity = -1;
		String unity = null;
		String engredient = null;
		while (st.hasMoreElements()) {
			String currentWord = st.nextToken();
			switch (status) {
			case NUMBER:
				try {
					quantity = Double.valueOf(currentWord);
				} catch (NumberFormatException e) {
					// case for example:
					// sel, proivre
					quantity = 0;
				}
				status = Status.USTENSIL;
				break;
			case USTENSIL:

			case INGREDIENT:

				break;
			default:
				break;

			}
		}
		ReflectionToStringBuilder.toString(iq);
		System.out.println("quantite:" + quantity + " unity:" + unity
				+ " engredient:" + engredient);
	}

	public void close() {
		if (this.jpaManager.getTransaction().isActive() == true) {
			this.jpaManager.getTransaction().commit();
		}
		int nbReceipes = this.jpaManager.getJpaQuery().getReceipesSize();
		int nbIngredients = this.jpaManager.getJpaQuery().getIngredientsSize();
		logger.info("Closing JPAWriter ...");
		logger.info("receipes: " + nbReceipes);
		logger.info("ingredients: " + nbIngredients);
	}

	public void flush(UserID userProfile) {
		logger.info("BEFORE PERSIST[" + userProfile.getEmail() + "]");
		logger.info(userProfile.toString());
		this.jpaManager.getEm().persist(userProfile);
		try {
			this.jpaManager.getTransaction().commit();
		} catch (DatabaseException e) {
			logger.severe("COMMIT ERROR:" + e.getQuery().toString());
			throw e;
		}
		this.jpaManager.getTransaction().begin();
		logger.info("END PERSIST[" + userProfile.getEmail() + "]");
	}

}
