package com.justyour.food.shared.jpa.models;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.justyour.food.shared.jpa.models.UserID.Sex;
import com.justyour.food.shared.nutrition.Regime;

@Generated(value="Dali", date="2013-12-13T10:29:11.461+0100")
@StaticMetamodel(UserID.class)
public class UserID_ {
	public static volatile SingularAttribute<UserID, Boolean> isAdmin;
	public static volatile SingularAttribute<UserID, String> prenoms;
	public static volatile SingularAttribute<UserID, String> nom;
	public static volatile SingularAttribute<UserID, String> email;
	public static volatile SingularAttribute<UserID, String> password;
	public static volatile SingularAttribute<UserID, String> uuid;
	public static volatile SingularAttribute<UserID, Date> creationDate;
	public static volatile SingularAttribute<UserID, Double> sizeInCm;
	public static volatile SingularAttribute<UserID, Date> birthDate;
	public static volatile SingularAttribute<UserID, Sex> sex;
	public static volatile ListAttribute<UserID, Regime> userProfile;
}
