/** 
 * (c) 2013 Aquila Services
 * 
 * File: SoundMatchTest.java
 * Package: com.aquilaservices.metareceipe.wget.nlp
 * Date: 18 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.wget.cooking;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoderComparator;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Soundex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> SoundMatchTest.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.nlp</b><br/>
 * <b>Date:</b> 18 août 2013
 * 
 * @author tonio
 * 
 */
public class SoundMatchTest {

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

	void soundex(String sz1, String sz2) {
		Soundex sndx = new Soundex();
		// Use the StringEncoderComparator to compare these two Strings
		StringEncoderComparator comparator1 = new StringEncoderComparator(sndx);
		DoubleMetaphone doubleMetaphone = new DoubleMetaphone();
		doubleMetaphone.setMaxCodeLen(20);

		String dm1= doubleMetaphone.doubleMetaphone(sz1);
		String dm2= doubleMetaphone.doubleMetaphone(sz2);
		System.err.println("Double Metaphone [" + sz1 + "]->["
				+ dm1+"]");
		System.err.println("Double Metaphone [" + sz2 + "]->["
				+ dm2+"]");
		
		System.err.println("EQUALS: "+doubleMetaphone.isDoubleMetaphoneEqual(dm1, dm2));
		System.err.println("soundex(" + sz1 + "," + sz2 + "):"
				+ comparator1.compare(sz1, sz2));
	}

	@Test
	public void testSoundCoocking_FR() {
		soundex("cuillere", "cuiller");
		soundex("cuillere", "cuilleres");
		soundex("cuillere", "cuillere(s)");
		soundex("gramme", "gram.");
		soundex("poele a frire", "poaleafri");

	}

	@Test
	public void testDoubleMetaphone() throws EncoderException, DecoderException {

		String foreignWord1 = "onions jaune";
		String foreignWord2 = "ognions jone";

		Soundex sndx = new Soundex();
		DoubleMetaphone doubleMetaphone = new DoubleMetaphone();

		System.err.println("Soundex Code for Wilson is: "
				+ sndx.encode("Wilson"));
		System.err.println("Soundex Code for Wylson is: "
				+ sndx.encode("Wylson"));

		// Use the StringEncoderComparator to compare these two Strings
		StringEncoderComparator comparator1 = new StringEncoderComparator(sndx);
		System.err.println("Are Wilson and Wylson same based on Soundex? "
				+ comparator1.compare("Wilson", "Wylson"));

		System.err.println("Are " + foreignWord1 + " and " + foreignWord2
				+ " same based on Soundex? "
				+ comparator1.compare(foreignWord1, foreignWord2));

		System.err.println("Are " + foreignWord1 + " and " + foreignWord2
				+ " same based on Soundex? "
				+ comparator1.compare(foreignWord1, foreignWord2));

		StringEncoderComparator comparator2 = new StringEncoderComparator(
				doubleMetaphone);

		System.err.println("Are " + foreignWord1 + " and " + foreignWord2
				+ " same based on DoubleMetaphone? "
				+ comparator2.compare(foreignWord1, foreignWord2));

		System.err.println("Double Metaphone primary code for " + foreignWord1
				+ ": " + doubleMetaphone.doubleMetaphone(foreignWord1));

		System.err.println("Double Metaphone secondary code for "
				+ foreignWord1 + ": "
				+ doubleMetaphone.doubleMetaphone(foreignWord1, true));

	}

}
