/** 
 * (c) 2013 Aquila Services
 * 
 * File: Normalizer.java
 * Package: com.aquilaservices.metareceipe.jpa.tools
 * Date: 5 ao�t 2013
 * @author tonio
 * 
 */

package com.justyour.food.shared.tools;

/** 
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> Normalizer.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.jpa.tools</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * @author tonio
 * 
 */

public class Normalizer {
	public static String normalize(String string) {
		string = string.trim();
		string.replaceAll("  ", " ");
		string.replaceAll("   ", " ");
		string.replaceAll("    ", " ");
		string.replaceAll("\t", " ");
		return string;
	}
}
