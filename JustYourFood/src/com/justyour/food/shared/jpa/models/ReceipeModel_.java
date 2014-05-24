package com.justyour.food.shared.jpa.models;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-01-06T22:51:15.088+0100")
@StaticMetamodel(ReceipeModel.class)
public class ReceipeModel_ {
	public static volatile SingularAttribute<ReceipeModel, String> title;
	public static volatile SingularAttribute<ReceipeModel, Long> number;
	public static volatile SingularAttribute<ReceipeModel, Date> creationDate;
	public static volatile SingularAttribute<ReceipeModel, String> domain;
	public static volatile SingularAttribute<ReceipeModel, String> source;
	public static volatile SingularAttribute<ReceipeModel, String> link;
	public static volatile ListAttribute<ReceipeModel, IngredientQuantity> engredients;
	public static volatile SingularAttribute<ReceipeModel, String> cookingSecondSentence;
	public static volatile SingularAttribute<ReceipeModel, Double> cookingSecond;
	public static volatile SingularAttribute<ReceipeModel, String> preparationSecondSentence;
	public static volatile SingularAttribute<ReceipeModel, Double> preparationSecond;
	public static volatile SingularAttribute<ReceipeModel, String> restSecondSentence;
	public static volatile SingularAttribute<ReceipeModel, Double> restSecond;
	public static volatile SingularAttribute<ReceipeModel, Double> energie;
	public static volatile SingularAttribute<ReceipeModel, String> remark;
	public static volatile SingularAttribute<ReceipeModel, String> numberPersonsSentence;
	public static volatile SingularAttribute<ReceipeModel, Integer> numberPersons;
	public static volatile SingularAttribute<ReceipeModel, String> difficultySentence;
	public static volatile SingularAttribute<ReceipeModel, Integer> difficulty;
	public static volatile SingularAttribute<ReceipeModel, String> instructions;
	public static volatile SingularAttribute<ReceipeModel, String> imageURL;
	public static volatile SingularAttribute<ReceipeModel, Integer> price;
	public static volatile SingularAttribute<ReceipeModel, String> priceSentence;
}
