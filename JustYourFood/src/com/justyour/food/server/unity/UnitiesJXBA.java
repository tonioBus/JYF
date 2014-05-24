/** 
 * (c) 2013 Aquila Services
 * 
 * File: UnitiesJXBA.java
 * Package: com.aquilaservices.metareceipe.wget.cooking
 * Date: 18 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.unity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.codec.language.DoubleMetaphone;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> UnitiesJXBA.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.cooking</b><br/>
 * <b>Date:</b> 18 août 2013
 * 
 * @author tonio
 * 
 */

// This statement means that class "UnitiesJXBA.java" is the root-element
@XmlRootElement(namespace = "com.justyour.food.server.unity")
public class UnitiesJXBA {

	DoubleMetaphone doubleMetaphone;

	public UnitiesJXBA() {
		doubleMetaphone = new DoubleMetaphone();
		doubleMetaphone.setMaxCodeLen(20);
	}

	public ArrayList<UnityJXBA> getUnityList() {
		return unityList;
	}

	public UnityJXBA getUnity(String unitySz) {
		double lastGoodPoints = 0;
		UnityJXBA ret = null;

		unitySz = unitySz.trim().toLowerCase();
		unitySz=unitySz.replaceAll("é", "e");
		unitySz=unitySz.replaceAll("è", "e");
		unitySz=unitySz.replaceAll("ê", "e");
		unitySz=unitySz.replaceAll("à", "a");
		unitySz=unitySz.replaceAll("ù", "u");

		for (UnityJXBA unity : this.unityList) {
			double points = unity.getCoherences(unitySz, doubleMetaphone);
			if (points > lastGoodPoints) {
				lastGoodPoints = points;
				ret = unity;
			}
		}
		if (lastGoodPoints > 0.9)
			return ret;
		else
			return null;
	}

	public void setUnityList(ArrayList<UnityJXBA> bookList) {
		this.unityList = bookList;
	}

	// XmLElementWrapper generates a wrapper element around XML representation
	// @XmlElementWrapper(name = "UnitiesList")
	// XmlElement sets the name of the entities
	private ArrayList<UnityJXBA> unityList = new ArrayList<UnityJXBA>();

}
