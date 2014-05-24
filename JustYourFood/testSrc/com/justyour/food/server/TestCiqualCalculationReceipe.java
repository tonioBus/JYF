package com.justyour.food.server;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import org.junit.Test;

public class TestCiqualCalculationReceipe {

	@Test
	public void test() throws JAXBException, InvalidFormatException, IOException {
		JYFServletContext sc = new JYFServletContext();
		sc.initJYFSerletContext("/justyour.com/");
		Parameters param = sc.getParam();
		JYFServiceImpl serverImpl = new JYFServiceImpl();

		
	}

}
