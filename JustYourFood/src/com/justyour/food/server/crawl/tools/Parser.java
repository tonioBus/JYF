/** 
 * (c) 2013 Aquila Services
 * 
 * File: Parser.java
 * Package: com.aquilaservices.metareceipe.jpa.tools
 * Date: 5 ao�t 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.crawl.tools;

import java.util.regex.Pattern;

import com.justyour.food.shared.jpa.models.Ingredient;
import com.justyour.food.shared.jpa.models.IngredientQuantity;

/** 
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> Parser.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.jpa.tools</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * @author tonio
 * 
 */

public class Parser {

	public static void parseIngredient(String ingredientSz,
			Ingredient ingredient, IngredientQuantity iq) {
		
		ingredientSz = ingredientSz.trim();
		ingredientSz = ingredientSz.toLowerCase();
		
		ingredient.setIngredient(ingredientSz);
		// should be done above: iq.setIngredient(ingredient);
		iq.setDetails(ingredientSz);
	}

	public static boolean like(final String str, final String expr)
	{
	  String regex = expr;  // = quotemeta(expr);
	  // regex = regex.replace("_", ".").replace("%", ".*?");
	  Pattern p = Pattern.compile(regex,
	      Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	  return p.matcher(str).matches();
	}
	
}
