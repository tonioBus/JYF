/**
 * File: QueryTest.java
 * Package: com.aquilaservices.metareceipe.wget
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */
package com.justyour.food.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Test;

import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.server.jpa.JPATransaction;
import com.justyour.food.shared.jpa.models.Ingredient;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> QueryTest.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget</b><br/>
 * <b>Date:</b> 5 août 2013
 * 
 * @author tonio
 * 
 */
public class QueryTest {

	@Test
	public void testDumpReceipesIngredients() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory(JPATransaction.PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		// all receipes
		Query q = em.createQuery("select r from ReceipeModel r");
		q.setHint("eclipselink.read-only", "true");
		q.setHint("eclipselink.query-results-cache", "true");
		@SuppressWarnings("unchecked")
		List<ReceipeModel> receipes = q.getResultList();
		for (ReceipeModel receipeModel : receipes) {
			System.out.println("-(r) " + receipeModel.getTitle());
		}
		Assert.assertTrue(receipes.size() > 0);
		// all engredients
		q = em.createQuery("select i from Ingredient i");
		q.setHint("eclipselink.read-only", "true");
		q.setHint("eclipselink.query-results-cache", "true");
		@SuppressWarnings("unchecked")
		List<Ingredient> ingredients = q.getResultList();
		for (Ingredient ingredient : ingredients) {
			System.out.println("-(i) [" + ingredient.getIngredient() + "]");
		}
		Assert.assertTrue(ingredients.size() > 0);

		// System.out.println("Number of Receipes:\t " + receipes.size());
		// System.out.println("Number of Ingredients:\t " + ingredients.size());

		em.close();
		factory.close();
	}

	@Test
	public void testGetIngredient() {
		JPAManager jpaManager = new JPAManager();
		Ingredient ingredient = jpaManager.getJpaQuery()
				.getIngredient("poivre");
		jpaManager.close();
		Assert.assertNotNull(ingredient);
		// System.out.println("Ingredient[" + currentIngredients + "]");
		List<ReceipeModel> receipes = ingredient.getReceipes();
		for (@SuppressWarnings("unused")
		ReceipeModel receipeModel : receipes) {
			// System.out.println("receipe: " + receipeModel);
		}
		Assert.assertTrue(receipes.size() > 0);
	}

	@Test
	public void testGetIngredient1() {
		JPAManager jpaManager = new JPAManager();
		Ingredient ingredient = jpaManager.getJpaQuery().getIngredient(
				"3 tomates");
		jpaManager.close();
		Assert.assertNotNull(ingredient);
		// System.out.println("Ingredient[" + currentIngredients + "]");
		List<ReceipeModel> receipes = ingredient.getReceipes();
		for (@SuppressWarnings("unused")
		ReceipeModel receipeModel : receipes) {
			// System.out.println("receipe: " + receipeModel);
		}
		Assert.assertTrue(receipes.size() > 0);
	}

	@Test
	public void testGetIngredients() {
		JPAManager jpaManager = new JPAManager();
		Ingredient[] ingredients = jpaManager.getJpaQuery().getIngredients(
				"%pain%", true, true, 0);
		for (@SuppressWarnings("unused")
		Ingredient ingredient : ingredients) {
			// System.out.println("- " + currentIngredients);
		}
		Assert.assertTrue(ingredients.length > 0);
	}

	@Test
	public void testQueryBurst() throws InterruptedException {
		int limit = 100;
		Thread thread[] = new Thread[limit];

		for (int i = 0; i < limit; i++) {
			thread[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for (int i = 0; i < 100; i++) {
							testGetIngredient();
							testGetIngredient1();
							testDumpReceipesIngredients();
							testCount();
						}
						synchronized (this) {
							wait();
						}
					} catch (Throwable t) {
						t.printStackTrace();
						Assert.fail(t.getMessage());
					}
					// notify();
				}
			});
			thread[i].setName("TEST QUERY BURST: " + i);
		}
		for (int i = 0; i < limit; i++) {
			thread[i].start();
		}
		// Tools.wait4key();
		for (int i = 0; i < limit; i++) {
			synchronized (thread[i]) {
				System.err.println("WAIT FOR: " + thread[i]);
				thread[i].notify();
				System.err.println("END  OF : " + thread[i]);
			}
		}
	}

	@Test
	public void testCount() {
		JPAManager jpaManager = new JPAManager();
		System.out.println(Thread.currentThread() + "->Number of Receipes:\t"
				+ jpaManager.getJpaQuery().getReceipesSize());
		System.out.println(Thread.currentThread()
				+ "->Number of Ingredients:\t"
				+ jpaManager.getJpaQuery().getIngredientsSize());
	}
}
