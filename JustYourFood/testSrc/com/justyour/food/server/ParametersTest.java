/**
 * 
 */
package com.justyour.food.server;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.justyour.food.server.Parameters.DumperDeclaration;
import com.justyour.food.server.crawl.providers.CuisinezAzDumper;
import com.justyour.food.server.crawl.providers.DoctissimoDumper;

/**
 * @author tonio
 * 
 */
public class ParametersTest {

	/**
	 * Test method for
	 * {@link com.justyour.food.server.Parameters#getParameters(java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public void testGetParameters() throws JAXBException, IOException {
		Parameters param = new Parameters();

		param.setSiteHost("http://justyourfood.com/");
		param.setServerIpAddress("192.168.1.5");
		param.setTmp("tmp");
		param.setWget2Linux("wget");
		param.setWget2Win("c:\\cygwin\\bin\\wget");
		param.setWgetOptions("-r -np -N");
		param.setJyfClassPath(new String[] { "providers" });
		param.setDumperDeclarations(new DumperDeclaration[] {
				new DumperDeclaration(CuisinezAzDumper.class.getName(),
						"http://www.cuisineaz.com/"),
				new DumperDeclaration(DoctissimoDumper.class.getName(),
						"http://recettes.doctissimo.fr") });
//		param.setSolrReceipe("http://localhost:8080/solr-4.4.0/JYF-Receipes");
//		param.setSolrCiqual("http://localhost:8080/solr-4.4.0/Ciqual");
		param.setSolr(new Parameters.Solr());
		param.setSolrHome("/justyourfood/jyf-solr-data/");
		param.setSolrReceipeCore("JYF-Receipes");
		param.setSolrCiqualCore("Ciqual");
		
		param.setAdmins(new String[] { "admin@justyourfood.com",
				"bussania@gmail.com", "sophiecapbern@gmail.com" });
		param.set_SMTP_Server("smtp.orange.fr");
		param.set_SMTP_Auth("true");
		param.set_SMTP_Username("albin.bussanni@orange.fr");
		param.set_SMTP_Password("p3yz7ra");

		param.routerURL = "http://192.168.1.1";
		param.routerType = "LiveBox-2";
		// every minutes
		param.routerCheckPeriod = "60000";
		
		param.save("jyf.xml");
		@SuppressWarnings("unused")
		Parameters param1 = Parameters.getParameters("jyf.xml");
	}

}
