/**
 * 
 */
package com.justyour.food.server;

import org.junit.Test;

/**
 * @author tonio
 *
 */
public class JYFServiceImplTest {

	JYFServiceImpl serverImpl = new JYFServiceImpl();

	/**
	 * Test method for {@link com.justyour.food.server.JYFServiceImpl#getTextFile(java.lang.String)}.
	 */
	@Test
	public void testGetTextFile() {
		String ret = serverImpl.getTextFile("Principales_allergies_alimentaires");
		
		System.out.println(ret);
	}

}
