/**
 * 
 */
package com.justyour.food.server.crawl;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.justyour.food.server.CrawlerClassLoader;
import com.justyour.food.server.Parameters;

/**
 * @author tonio
 *
 */
public class ProviderManagerTest {

	/**
	 * Test method for
	 * {@link com.justyour.food.server.CrawlerClassLoader#loadDumpers(com.justyour.food.server.Parameters)}
	 * .
	 * 
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testLoadDumpers() throws FileNotFoundException, JAXBException {
		CrawlerClassLoader pm = new CrawlerClassLoader();
		Parameters param = Parameters.getParameters("/justyour.com/jyf.xml");
		pm.loadDumpers(param);
	}

}
