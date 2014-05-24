/** 
 * (c) 2013 Aquila Services
 * 
 * File: IngredientQuantity.java
 * Package: com.aquilaservices.metareceipe.jpa.models
 * Date: 5 ao�t 2013
 * @author tonio
 * 
 */

package com.justyour.food.shared.jpa.models;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> IngredientQuantity.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.jpa.models</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * 
 * @author tonio
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "com.justyour.food.shared.jpa.models")
@Entity
public class IngredientQuantity implements IGwtRPC {

	static Logger logger = Logger.getLogger(IngredientQuantity.class.getName());

	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1997557316842800152L;

	@Column(length = 1000)
	private String details;

	@Column(length = 1000)
	private String adjectif;

	@XmlTransient
	private int corrected = 0;

	@XmlTransient
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	// FIXME dangerous for GWT RPC
	Ingredient ingredient;

	@Column(length = 1000)
	private String ingredientSz;

	// FIXME dangerous for GWT RPC
	@XmlTransient
	private ArrayList<Quantity> quantities = new ArrayList<Quantity>();

	@Transient
	private Quantity[] quantitiesRPC = null;

	@XmlTransient
	private double interpretationLevel;

	// FIXME dangerous for GWT RPC
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private IngredientQuantity orNext = null;

	public IngredientQuantity() {
	}

	@Transient
	public void prepare4RPC() {
		if( this.quantities==null) return;
		logger.info("IngredientQuantity:prepare4RPC:"
				+ this.ingredient.getIngredient());
		int len = this.quantities.size();
		this.quantitiesRPC = this.quantities.toArray(new Quantity[len]);
		this.quantities = null;
		this.ingredient.prepare4DisplayRPC();
		IngredientQuantity next = this.orNext;
		while (next != null) {
			next.prepare4RPC();
			next = next.getOrNext();
		}
	}

	@Transient
	public int getCorrected() {
		return corrected;
	}

	@Transient
	public void setCorrected(int corrected) {
		this.corrected = corrected;
	}

	@Transient
	public IngredientQuantity getOrNext() {
		return orNext;
		// return null;
	}

	@Transient
	public void setOrNext(IngredientQuantity orNext) {
		this.orNext = orNext;
	}

	public ArrayList<Quantity> getQuantities() {
		return quantities;
	}

	public Quantity[] getQuantitiesRPC() {
		return quantitiesRPC;
	}

	public void setQuantities(ArrayList<Quantity> quantities) {
		this.quantities = quantities;
	}

	public String getAdjectif() {
		return adjectif;
	}

	public void setAdjectif(String adjectif) {
		this.adjectif = adjectif.trim();
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details.trim();
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public void setIngredientSz(String ingredient) {
		this.ingredientSz = ingredient.trim().toLowerCase();

	}

	public String getIngredientSz() {
		return ingredientSz;
	}

	public void setInterpretationLevel(double interpretationLevel) {
		this.interpretationLevel = interpretationLevel;
	}

	public double getInterpretationLevel() {
		return interpretationLevel;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("\nIngredientSz:" + this.ingredientSz + "\n");
		sb.append("Adjectif:" + this.adjectif + "\n");
		sb.append("Details:" + this.details + "\n");
		sb.append("next:" + this.orNext + "\n");

		if (this.quantities != null) {
			for (Quantity q : this.quantities) {
				sb.append(" - " + q.toString() + "\n");
			}
		}
		if( this.quantitiesRPC!=null) {
			for(int i=0; i<this.quantitiesRPC.length; i++) {
				sb.append(" - (bis) " + this.quantitiesRPC[i].toString() + "\n");				
			}
		}
		sb.append("Ingredient:\n" + this.ingredient);
		return sb.toString();
	}
}
