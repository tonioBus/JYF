/** 
 * (c) 2013 Aquila Services
 * 
 * File: Ingredient.java
 * Package: com.aquilaservices.metareceipe.jpa.models
 * Date: 5 ao�t 2013
 * @author tonio
 * 
 */

package com.justyour.food.shared.jpa.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> Ingredient.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.jpa.models</b><br/>
 * <b>Date:</b> 5 août 2013
 * 
 * @author tonio
 * 
 */

@Entity
public class Ingredient implements IGwtRPC {

	static Logger logger = Logger.getLogger(Ingredient.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 9079323568609503338L;

	@Id
	@Column(length = 1000)
	private String ingredient = "?ingredient?";

	@Transient
	protected ReceipeModel4RPC[] receipes4RPC = null;

	@OneToMany(fetch = FetchType.LAZY)
	private List<ReceipeModel> receipes = new ArrayList<ReceipeModel>();

	private int numberReceipes = 0;

	public Ingredient() {
	}

	@Transient
	public List<ReceipeModel> getReceipes() {
		return receipes;
	}

	@Transient
	public void setReceipes(List<ReceipeModel> receipes) {
		this.receipes = receipes;
		this.numberReceipes = this.receipes.size();
	}

	public int getNumberReceipes() {
		if (this.receipes == null)
			this.numberReceipes = this.receipes4RPC.length;
		else
			this.numberReceipes = this.receipes.size();
		return this.numberReceipes;
	}

	public void setNumberReceipes(int numberReceipes) {
		this.numberReceipes = numberReceipes;
	}

	public String getIngredient() {
		logger.info("Getting ingredient with NAME[" + ingredient + "]");
		return ingredient;
	}

	private List<String> synonyms = new ArrayList<String>();

	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	@Override
	public String toString() {
		String ret = this.ingredient + ":number of receipes:"
				+ getNumberReceipes() + "\n";
//		ret += "Ingredient connected to  receipe[";
//		if (this.receipes != null) {
//			for (ReceipeModel receipe : this.receipes) {
//				ret += receipe.getNumber() + ",";
//			}
//			ret += "]";
//		}
		return ret;
	}

	/**
	 * @param ingredientSz
	 */
	public void setIngredient(String ingredientSz) {
		this.ingredient = ingredientSz.trim().toLowerCase();
		logger.info("Ingredient setted with NAME[" + ingredient + "]");
	}

	public void prepare4DisplayRPC() {
		if (receipes4RPC == null) {
			this.receipes4RPC = new ReceipeModel4RPC[this.receipes.size()];
			int i = 0;
			for (ReceipeModel receipe : this.receipes) {
				this.receipes4RPC[i] = new ReceipeModel4RPC(receipe.link,
						receipe.title);
				i++;
			}
			this.synonyms = null;
			this.receipes = null;
		}
	}

}
