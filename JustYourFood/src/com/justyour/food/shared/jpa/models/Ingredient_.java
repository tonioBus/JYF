package com.justyour.food.shared.jpa.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-12-13T10:29:11.448+0100")
@StaticMetamodel(Ingredient.class)
public class Ingredient_ {
	public static volatile SingularAttribute<Ingredient, String> ingredient;
	public static volatile ListAttribute<Ingredient, ReceipeModel> receipes;
	public static volatile SingularAttribute<Ingredient, Integer> numberReceipes;
}
