/** 
 * (c) 2013 Aquila Services
 * 
 * File: ReceipeModel.java
 * Package: com.aquilaservices.metareceipe.jpa.models
 * Date: 5 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.shared.jpa.models;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.justyour.food.shared.tools.Normalizer;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> ReceipeModel.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.jpa.models</b><br/>
 * <b>Date:</b> 5 août 2013
 * 
 * @author tonio
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "com.justyour.food.shared.jpa.models")
@Entity
public class ReceipeModel implements IGwtRPC {

	static Logger logger = Logger.getLogger(ReceipeModel.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 3178700653096325960L;

	@Transient
	public static final String EMPTY_INSTRUCTIONS = "?instructions?";

	@Transient
	protected double interpretationLevel = -1;

	@Transient
	protected boolean wellFormed = false;

	@Column(length = 1000)
	protected String title = "?Title?";

	private long number;

	@Temporal(TemporalType.DATE)
	protected Date creationDate;

	protected String domain;

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	@Column(length = 1000)
	protected String source;

	@Id
	@Column(length = 1000)
	protected String link; // = "http://?link?";

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	protected List<IngredientQuantity> engredients = new ArrayList<IngredientQuantity>();

	// COOKING TIME (SEC)
	protected String cookingSecondSentence = null;
	protected double cookingSecond = -1;

	// PREPARATION TIME (SEC)
	protected String preparationSecondSentence = null;
	protected double preparationSecond = -1;

	// REST TIME (SEC)
	protected String restSecondSentence = null;
	protected double restSecond = -1;

	protected double energie = -1;

	@Column(length = 1000)
	protected String remark = "?remark?";

	// NUMBER OF PERSONS
	protected String numberPersonsSentence = null;
	protected int numberPersons = -1;

	protected List<String> drinks = new ArrayList<String>();

	// DIFFICULTY 1-10
	protected String difficultySentence = null;
	protected int difficulty = -1;

	@GwtTransient
	public static final int LENGTH_INSTRUCTIONS = 10000;

	@Column(length = LENGTH_INSTRUCTIONS)
	protected String instructions = EMPTY_INSTRUCTIONS;

	@Column(length = 1000)
	protected String imageURL = "?imageURL?";

	// PRICE
	protected int price;
	protected String priceSentence;

	@Transient
	private IngredientQuantity[] ingredientsRPC;

	@Transient
	protected String inlineIngredients;
	
	public ReceipeModel() {
	}

	public double getPreparationSecond() {
		return preparationSecond;
	}

	public void setPreparationSecond(String text, double preparationSecond) {
		this.preparationSecondSentence = text;
		this.preparationSecond = preparationSecond;
	}

	public String getCookingSecondSentence() {
		return cookingSecondSentence;
	}

	public String getPreparationSecondSentence() {
		return preparationSecondSentence;
	}

	public String getRestSecondSentence() {
		return restSecondSentence;
	}

	public String getNumberPersonsSentence() {
		return numberPersonsSentence;
	}

	public String getDifficultySentence() {
		return difficultySentence;
	}

	public double getRestSecond() {
		return restSecond;
	}

	public void setRestSecond(double restSecond) {
		this.restSecond = restSecond;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getLink() {
		return link;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public double getInterpretationLevel() {
		try {
			if (this.interpretationLevel != -1)
				return this.interpretationLevel;
			double ret = 0;
			int nbIngredients = this.getIngredients().size();
			for (IngredientQuantity iq : this.getIngredients()) {
				ret += iq.getInterpretationLevel();
			}
			this.interpretationLevel = ret / nbIngredients;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.interpretationLevel;
	}

	public boolean isWellFormed() {
		// System.out.println("welformed:" + true + "\ntitle:" + this.title
		// + "\nlink:" + link + "\nengredients:" + engredients.size());
		return wellFormed && this.link != null && this.title != null;
	}

	public void setWellFormed(boolean wellFormed) {
		this.wellFormed = wellFormed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public IngredientQuantity[] getIngredientRPC() {
		return ingredientsRPC;
	}

	public List<IngredientQuantity> getIngredients() {
		return engredients;
	}

	public void setIngredients(List<IngredientQuantity> ingredients) {
		this.engredients = ingredients;
	}

	public void addIngredient(IngredientQuantity iq) {
		this.engredients.add(iq);
	}

	public double getCookingSecond() {
		return cookingSecond;
	}

	public void setCookingSecond(String cookingSecondSentence,
			double cookingSecond) {
		this.cookingSecondSentence = cookingSecondSentence;
		this.cookingSecond = cookingSecond;
	}

	public double getEnergie() {
		return energie;
	}

	public void setEnergie(double energie) {
		this.energie = energie;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getNumberPersons() {
		return numberPersons;
	}

	public void setNumberPersons(String numberPersonsSentence, int numberPersons) {
		this.numberPersonsSentence = numberPersonsSentence;
		this.numberPersons = numberPersons;
	}

	public List<String> getDrinks() {
		return drinks;
	}

	public void setDrinks(List<String> drinks) {
		this.drinks = drinks;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setImage(String image) {
		this.imageURL = image;
	}

	public String getInlineIngredients() {
		return this.inlineIngredients;
	}
	
	protected void createInlineIngredients() {
		this.inlineIngredients = this.engredients.size() + " : ingrédient(s) - ";
		for (IngredientQuantity iq : this.engredients) {
			this.inlineIngredients += iq.ingredient.getIngredient()+" ";
		}
	}

	public String getDetails() {
		final int maxLength = 200;
		String ret = this.instructions;
		if (ret.length() > maxLength)
			ret = ret.substring(0, maxLength);
		ret = ret.replaceAll("\\W+", " ");
		ret = ret.replaceAll("\r", "");
		ret = ret.replaceAll("\t", "");
		ret = ret.replaceAll("\n\n\n", "\n");
		ret = ret.replaceAll("\n\n", "\n");
		try {
			PrintWriter out = new PrintWriter("/tmp/receipe_"+this.number+".txt");
			out.write(this.instructions);
			out.write("\n---------------------------------------------------\n");
			out.write(ret);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret+" ... ";
	}

	/**
	 * Normalize the receipe
	 */
	public void normalize() {
		this.title = Normalizer.normalize(title);
		this.link = Normalizer.normalize(link);
		this.remark = Normalizer.normalize(remark);
		this.instructions = Normalizer.normalize(instructions);
		if (this.instructions.length() > LENGTH_INSTRUCTIONS) {
			this.instructions = this.instructions.substring(0,
					LENGTH_INSTRUCTIONS - 1);
		}
		this.imageURL = Normalizer.normalize(imageURL);
	}

	@Override
	public String toString() {
		String ret = "\nTITLE: " + this.title + "\n";
		ret += "Creation Time:" + this.creationDate + " NUMBER:" + this.number
				+ "\n";
		ret += "Source:" + this.source + "\n" + "Difficulty:";
		ret += "Instructions:" + "\n";
		ret += instructions + "\n";
		ret += this.getDifficultySentence() + "\n" + "CookingTime:"
				+ this.getCookingSecondSentence() + "\n" + "PrepTime:"
				+ this.getPreparationSecondSentence() + "\n" + "RestTime:"
				+ this.getRestSecondSentence() + "\n" + "NumberPersons:"
				+ this.getNumberPersonsSentence() + "\n" + "Price:"
				+ this.getPriceSentence() + "\n" + "\nLink:" + this.getLink()
				+ "\nINGREDIENTS:";
		if (this.engredients != null) {
			for (IngredientQuantity iq : this.engredients) {
				ret += " -  [" + iq.getIngredientSz() + "]\n";
			}
		}
		return ret;
	}

	public String getPriceSentence() {
		return this.priceSentence;
	}

	public int getPrice() {
		return this.price;
	}

	public void prepare4RPCDisplay() {
		logger.info("prepare4RPCDisplay:" + this.title);
		logger.info("InterpretationLevel: " + this.getInterpretationLevel());
		int len = this.engredients.size();
		this.ingredientsRPC = this.engredients
				.toArray(new IngredientQuantity[len]);
		for (IngredientQuantity iq : this.ingredientsRPC) {
			logger.info("prepare4RPCDisplay:ingredient="
					+ iq.ingredient.getIngredient());
			iq.prepare4RPC();
		}
		logger.info("prepare4RPCDisplay:len=" + len);
		this.drinks = null;
		this.engredients = null;
	}

	@Transient
	public void prepare4RPCShortDisplay() {
		logger.info("InterpretationLevel: " + this.getInterpretationLevel());
		createInlineIngredients();
		this.engredients = null;
		this.drinks = null;
	}

	@Transient
	public static ReceipeModel getDummyReceipe(String title, String link) {
		ReceipeModel receipeModel = new ReceipeModel();
		receipeModel.setTitle(title);
		receipeModel.setLink(link);
		receipeModel.instructions = "Instructions pour la recette " + title;
		receipeModel.engredients = new ArrayList<IngredientQuantity>();
		receipeModel.setDifficultySentence("difficilement easy");
		receipeModel.drinks = new ArrayList<String>();
		return receipeModel;
	}

	public void setPrice(String priceSz, int price) {
		this.priceSentence = priceSz;
		this.price = price;
	}

	public void setCookingSecondSentence(String cookingSecondSentence) {
		this.cookingSecondSentence = cookingSecondSentence;
	}

	public void setCookingSecond(double cookingSecond) {
		this.cookingSecond = cookingSecond;
	}

	public void setPreparationSecondSentence(String preparationSecondSentence) {
		this.preparationSecondSentence = preparationSecondSentence;
	}

	public void setPreparationSecond(double preparationSecond) {
		this.preparationSecond = preparationSecond;
	}

	public void setRestSecondSentence(String restSecondSentence) {
		this.restSecondSentence = restSecondSentence;
	}

	public void setNumberPersonsSentence(String numberPersonsSentence) {
		this.numberPersonsSentence = numberPersonsSentence;
	}

	public void setNumberPersons(int numberPersons) {
		this.numberPersons = numberPersons;
	}

	public void setDifficultySentence(String difficultySentence) {
		this.difficultySentence = difficultySentence;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setNumber(long number) {
		System.out.println("@ID@: " + number);
		this.number = number;
	}

	public long getNumber() {
		return this.number;
	}

	public void setDomain(String domain) {
		this.domain = domain;

	}
}
