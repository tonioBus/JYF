/**
 * 
 */
package com.justyour.food.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.ServerProperties;

/**
 * @author tonio
 *
 */
public class ServerPropertiesTest {

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

	/**
	 * Test method for {@link com.justyour.food.server.ServerProperties#getAllFile(java.io.File, java.util.HashMap)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetAllFile() throws IOException {
		HashMap<String, Object> hash=new HashMap<>();
		
		ServerProperties.getAllFile(new File("/tmp/a"), hash);
		for (String key : hash.keySet()) {
			System.out.println(key+"\t ->\t "+hash.get(key));
		}
	}

}
