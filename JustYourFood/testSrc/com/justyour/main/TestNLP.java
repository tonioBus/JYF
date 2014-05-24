package com.justyour.main;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import com.justyour.food.server.crawl.opennlp.IngredientNLP;

public class TestNLP {

	public static void main(String[] args) throws InvalidFormatException, JAXBException, IOException {
		IngredientNLP ingredientNLP = new IngredientNLP("war");
	}

}
