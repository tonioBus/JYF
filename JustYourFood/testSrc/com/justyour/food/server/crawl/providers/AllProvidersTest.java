/** 
 * (c) 2013 Aquila Services
 * 
 * File: AllProvidersTest.java
 * Package: com.aquilaservices.metareceipe.wget.providers
 * Date: 27 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.crawl.providers;

import java.util.Properties;

import org.junit.Test;

import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.wget.IWgetProvider;
import com.justyour.food.wget.providers.cuisinezaz.CuisinezAzWget;
import com.justyour.food.wget.providers.doctissimo.DoctissimoWget;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> AllProvidersTest.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.providers</b><br/>
 * <b>Date:</b> 27 août 2013
 * 
 * @author tonio
 * 
 */
public class AllProvidersTest {

	@Test
	public void test() throws Exception {
		JPAManager jpaManager = new JPAManager();
		IWgetProvider cuisinezAz = new CuisinezAzWget();
		Properties prop = new Properties();
		// prop.setProperty(IWgetProvider.LIMIT, "10");
		prop.setProperty(IWgetProvider.URLS,
				"D:/cygwin/tmp/recettes/www.cuisineaz.com/recettes/");
		prop.setProperty(IWgetProvider.PATTERN, ".aspx");
		cuisinezAz.init(null, prop);
		cuisinezAz.close();

		IWgetProvider doctissimo = new DoctissimoWget();
		prop = new Properties();
		// prop.setProperty(IWgetProvider.LIMIT, "10");
		prop.setProperty(IWgetProvider.URLS,
				"D:/cygwin/tmp/recettes/recettes.doctissimo.fr/backup");
		prop.setProperty(IWgetProvider.PATTERN, ".htm");
		System.out
				.println("You are about to rebuild DB for: recettes.doctissimo.fr");
		doctissimo.init(null, prop);
		doctissimo.close();
		
		jpaManager.close();
		// no exception, we can copy the DB as a good one
	}

}
