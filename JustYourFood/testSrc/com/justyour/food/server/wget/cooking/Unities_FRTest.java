/** 
 * (c) 2013 Aquila Services
 * 
 * File: Unities_FRTest.java
 * Package: com.aquilaservices.metareceipe.wget.cooking
 * Date: 18 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.wget.cooking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.unity.Unities;
import com.justyour.food.server.unity.UnitiesJXBA;
import com.justyour.food.server.unity.UnityJXBA;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> Unities_FRTest.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.cooking</b><br/>
 * <b>Date:</b> 18 août 2013
 * 
 * @author tonio
 * 
 */
public class Unities_FRTest {

	static final String SAMPLE_UNITIES_FR_XML = "data/sample_unities_FR.xml";

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
	public void createXMLTest() throws JAXBException {

		UnityJXBA unity = new UnityJXBA();
		unity.setSound("cuiller");
		unity.getWords().add("cuillere");
		unity.getWords().add("cuilleres");
		unity.getWords().add("cuillere(s)");
		unity.getWords().add("c.");

		UnityJXBA unity1 = new UnityJXBA();
		unity1.setSound("tranche");
		unity1.getWords().add("tranche");
		unity1.getWords().add("tranches");
		unity1.getWords().add("tranche(s)");
		unity1.getWords().add("c.");

		UnitiesJXBA unities = new UnitiesJXBA();
		unities.getUnityList().add(unity);
		unities.getUnityList().add(unity1);

		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(UnitiesJXBA.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		// Write to System.out
		m.marshal(unities, System.out);

		// Write to File
		m.marshal(unities, new File(SAMPLE_UNITIES_FR_XML));

	}

	@Test
	public void readXMLTest() throws JAXBException, FileNotFoundException {
		// get variables from our xml file, created before
		System.out.println();
		System.out.println("Output from: " + SAMPLE_UNITIES_FR_XML);
		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(UnitiesJXBA.class);

		Unmarshaller um = context.createUnmarshaller();
		UnitiesJXBA unities = (UnitiesJXBA) um.unmarshal(new FileReader(
				SAMPLE_UNITIES_FR_XML));
		for (UnityJXBA unity : unities.getUnityList()) {
			System.out.println(ReflectionToStringBuilder.toString(unity, ToStringStyle.MULTI_LINE_STYLE));
		}
	}

	void find(String sz, Unities unities) {
		UnityJXBA unity = unities.getUnity(sz);
		System.out.println("RESULT["+sz+"]:");
		System.out.println(ReflectionToStringBuilder.toString(unity, ToStringStyle.MULTI_LINE_STYLE));
	}
	
	@Test
	public void unitiesTest() throws FileNotFoundException, JAXBException {
		Unities unities = new Unities("data/Unities_FR.xml");
		
		find("cuiller", unities);
		find("grams", unities);
		find("gram", unities);
	}
}
