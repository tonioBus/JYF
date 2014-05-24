/** 
 * Project: JustYourFood
 * (c) 2014 Aquila Services
 * 
 * File: ResultGetReceipes.java
 * Package: com.justyour.food.client.pages
 * Date: 22 janv. 2014
 * @author Anthony Bussani (bussania@gmail.com)
 * 
 */

package com.justyour.food.shared.ResultsObject;

import com.justyour.food.shared.jpa.models.IGwtRPC;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * @author tonio
 * 
 */
public class ResultGetReceipes implements IGwtRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6335001984956814227L;

	long numberReceipes = 0;
	long numberIngredients = 0;
	long numberOfSuggestions = 0;

	ReceipeModel[] receipes = null;

	public void setReceipes(ReceipeModel[] receipes) {
		this.receipes = receipes;
	}

	public void setNumberReceipes(long numberReceipes) {
		this.numberReceipes = numberReceipes;
	}

	public void setNumberIngredients(long numberIngredients) {
		this.numberIngredients = numberIngredients;
	}

	public void setNumberOfSuggestions(long numberOfSuggestions) {
		this.numberOfSuggestions = numberOfSuggestions;
	}

	public ReceipeModel[] getReceipes() {
		return receipes;
	}

	public long getNumberReceipes() {
		return numberReceipes;
	}

	public long getNumberIngredients() {
		return numberIngredients;
	}

	public long getNumberOfSuggestions() {
		return numberOfSuggestions;
	}

}
