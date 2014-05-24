/** 
 * (c) 2013 Aquila Services
 * 
 * File: Unities.java
 * Package: com.aquilaservices.metareceipe.wget.cooking
 * Date: 18 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.unity;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> Unities.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.cooking</b><br/>
 * <b>Date:</b> 18 août 2013
 * 
 * @author tonio
 * 
 */
public class Unities {

	UnitiesJXBA unities;
	
	public Unities(String filename) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(UnitiesJXBA.class);

		Unmarshaller um = context.createUnmarshaller();
		unities = (UnitiesJXBA) um.unmarshal(new FileReader(
				filename));
//		for (UnityJXBA unity : unities.getUnityList()) {
//			System.out.println(ReflectionToStringBuilder.toString(unity, ToStringStyle.MULTI_LINE_STYLE));
//		}
	}

	public UnityJXBA getUnity(String unity) {
		return unities.getUnity(unity);
	}
}
