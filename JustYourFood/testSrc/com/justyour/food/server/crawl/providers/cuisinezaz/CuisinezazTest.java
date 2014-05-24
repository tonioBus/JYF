/**
 * File: CuisinezAzWget.java
 * Package: com.aquilaservices.metareceipe.wget.providers.cuisinezaz
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.wget.providers.cuisinezaz;

import java.util.Properties;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.wget.IWgetProvider;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> CuisinezAzWget.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.providers.cuisinezaz</b><br/>
 * <b>Date:</b> 05/08/2013
 * 
 * @author tonio
 * 
 */
public class CuisinezazTest {
	static Logger logger = Logger.getLogger(CuisinezazTest.class.getName());

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

	@Test
	public void test() throws Exception {
		JPAManager jpaManager = new JPAManager();
		IWgetProvider cuisinezAz = new CuisinezAzWget();
		Properties prop = new Properties();
		// prop.setProperty(IWgetProvider.LIMIT, "1000");
		prop.setProperty(IWgetProvider.URLS,
				"D:/cygwin/tmp/recettes/www.cuisineaz.com/recettes/");
		// prop.setProperty(IWgetProvider.PATTERN, "backeofe--1725.aspx");
		prop.setProperty(IWgetProvider.PATTERN, ".aspx");
		logger.info("You are about to rebuild DB for: www.cuisineaz.com");
		cuisinezAz.init(null, prop);
		cuisinezAz.close();
		jpaManager.close();
	}

}
