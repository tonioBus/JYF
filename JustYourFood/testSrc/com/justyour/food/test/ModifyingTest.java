/** 
 * (c) 2013 Aquila Services
 * 
 * File: ModifyingTest.java
 * Package: com.aquilaservices.metareceipe.wget
 * Date: 15 ao�t 2013
 * @author tonio
 * 
 */

package com.justyour.food.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.server.jpa.JPAWriter;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> ModifyingTest.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget</b><br/>
 * <b>Date:</b> 15 août 2013
 * 
 * @author tonio
 * 
 */
public class ModifyingTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.aquilaservices.server.metareceipe.jpa.JPAWriter#updateIngredientQuantities()}
	 * .
	 */
	@Test
	public void testUpdateIngredientQuantities() {
		JPAManager jpaManager = new JPAManager();
		JPAWriter jpaWriter = new JPAWriter(jpaManager);
		jpaWriter.updateIngredientQuantities(0);
		jpaWriter.close();
	}

	@Test
	public void testupdateIQ_FR() throws IOException {
		FileInputStream in = new FileInputStream("data/currentIngredients.txt");
		BufferedInputStream bf = new BufferedInputStream(in);

		in.close();
	}

}
