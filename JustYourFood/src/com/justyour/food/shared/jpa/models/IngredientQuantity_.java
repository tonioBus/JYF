package com.justyour.food.shared.jpa.models;

import java.util.ArrayList;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-12-13T10:29:11.451+0100")
@StaticMetamodel(IngredientQuantity.class)
public class IngredientQuantity_ {
	public static volatile SingularAttribute<IngredientQuantity, Long> id;
	public static volatile SingularAttribute<IngredientQuantity, String> details;
	public static volatile SingularAttribute<IngredientQuantity, String> adjectif;
	public static volatile SingularAttribute<IngredientQuantity, Integer> corrected;
	public static volatile SingularAttribute<IngredientQuantity, Ingredient> ingredient;
	public static volatile SingularAttribute<IngredientQuantity, String> ingredientSz;
	public static volatile SingularAttribute<IngredientQuantity, ArrayList> quantities;
	public static volatile SingularAttribute<IngredientQuantity, Double> interpretationLevel;
	public static volatile SingularAttribute<IngredientQuantity, IngredientQuantity> orNext;
}
