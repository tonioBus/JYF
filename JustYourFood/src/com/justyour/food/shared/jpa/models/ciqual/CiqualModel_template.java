package com.justyour.food.shared.jpa.models.ciqual;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.justyour.food.shared.jpa.models.IGwtRPC;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * this class is a JPA / GWT representation of the CIQUAL TABLE
 *
 * @author tonio
 * 
 * @see <a
 *      href="http://www.afssa.fr/TableCIQUAL/Documents/Ciqual_2012_v02.07.csv">http://www.afssa.fr/TableCIQUAL/Documents/Ciqual_2012_v02.07.csv</a>
 * @see <a href="http://www.afssa.fr/">http://www.afssa.fr/</a>
 * 
 */
@Entity
public class CiqualModel_template implements IGwtRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6940162702697500670L;

	@GwtTransient
	@Transient
	static String fields[];

	@GwtTransient
	@Transient
	static String fieldNames[];

	@GwtTransient
	@Transient
	static String details[];

	@GwtTransient
	@Transient
	static String units[];

	@GwtTransient
	@Transient
	String values[];

	public int nbFields() {
		return fields.length;
	}

	public static String[] add2Fields(String... strings) {
		String[] array = new String[strings.length];
		int i = 0;
		for (String string : strings) {
			array[i++] = string;
		}
		return array;
	}
	
	public String getField(final int i) {
		return fields[i];
	}

	public String getFieldName(final int i) {
		return fieldNames[i];
	}

	public String getDetail(final int i) {
		return details[i];
	}

	public String getValue(final int i) {
		return values[i];
	}

	public String getUnit(final int i) {
		return units[i];
	}

	public static String[] getFields() {
		return fields;
	}

	public static String[] getFieldNames() {
		return fieldNames;
	}

	public String[] getValues() {
		return values;
	}

	public static String[] getDetails() {
		return details;
	}
	
	public static String[] getUnits() {
		return units;
	}
	
	/*
	 * * ORIGGPCD;ORIGGPFR;ORIGFDCD;ORIGFDNM;10110 Sodium (mg/100g);10120
	 * Magnésium (mg/100g);10150 Phosphore (mg/100g);10190 Potassium
	 * (mg/100g);10200 Calcium (mg/100g);10251 Manganèse (mg/100g);10260 Fer
	 * (mg/100g);10290 Cuivre (mg/100g);10300 Zinc (mg/100g);10340 Sélénium
	 * (µg/100g);10530 Iode (µg/100g);25000 Protéines (g/100g);25003 Protéines
	 * brutes, N x 6_25 (g/100g);31000 Glucides (g/100g);32000 Sucres
	 * (g/100g);327 Energie, Règlement UE 1169/2011 (kJ/100g);328 Energie,
	 * Règlement UE 1169/2011 (kcal/100g);33110 Amidon (g/100g);332 Energie, N x
	 * facteur Jones, avec fibres (kJ/100g);333 Energie, N x facteur Jones, avec
	 * fibres (kcal/100g);34000 Polyols totaux (g/100g);34100 Fibres
	 * (g/100g);400 Eau (g/100g);40000 Lipides (g/100g);40302 AG saturés
	 * (g/100g);40303 AG monoinsaturés (g/100g);40304 AG polyinsaturés
	 * (g/100g);40400 AG 4:0, butyrique (g/100g);40600 AG 6:0, caproïque
	 * (g/100g);40800 AG 8:0, caprylique (g/100g);41000 AG 10:0, caprique
	 * (g/100g);41200 AG 12:0, laurique (g/100g);41400 AG 14:0, myristique
	 * (g/100g);41600 AG 16:0, palmitique (g/100g);41800 AG 18:0, stéarique
	 * (g/100g);41819 AG 18:1 9c (n-9), oléique (g/100g);41826 AG 18:2 9c,12c
	 * (n-6), linoléique (g/100g);41833 AG 18:3 c9,c12,c15 (n-3),
	 * alpha-linolénique (g/100g);42046 AG 20:4 5c,8c,11c,14c (n-6),
	 * arachidonique (g/100g);42053 AG 20:5 5c,8c,11c,14c,17c (n-3), EPA
	 * (g/100g);42263 AG 22:6 4c,7c,10c,13c,16c,19c (n-3), DHA (g/100g);51200
	 * Rétinol (µg/100g);51330 Beta-Carotène (µg/100g);52100 Vitamine D
	 * (µg/100g);53100 Vitamine E (mg/100g);54100 Vitamine K (µg/100g);55100
	 * Vitamine C (mg/100g);56100 Vitamine B1 ou Thiamine (mg/100g);56200
	 * Vitamine B2 ou Riboflavine (mg/100g);56310 Vitamine B3 ou PP ou Niacine
	 * (mg/100g);56400 Vitamine B5 ou Acide pantothénique (mg/100g);56500
	 * Vitamine B6 (mg/100g);56600 Vitamine B12 (µg/100g);56700 Vitamine B9 ou
	 * Folates totaux (µg/100g);60000 Alcool (g/100g);65000 Acides organiques
	 * (g/100g);75100 Cholestérol (mg/100g)11;Abats;40003;Cervelle, agneau,
	 * cuite
	 * ;130;15,9;316;205;6,93;0,059;3,53;0,21;1,36;-;1;10,8;10,8;0,8;-;523;126
	 * ;-;
	 * 523;126;0;0;77,3;8,8;2,6;1,84;1,04;0;0;0;-;0;0,02;1,25;1,27;-;-;-;-;0;0
	 * ,59;0;-;-;-;-;12;0,11;0,24;2,47;0,99;0,11;9,25;5;0;-;2080 *
	 *//* * example: 11 */

	// BEGIN - everything from Here will be replaced by CreateCiquaModel
	public void populateValue() {
		CiqualModel.values = new String[] {
			this.m_Acides_organiques_65000+"", this.m_AG_10_0_caprique_41000+""
		};
	}

	static {
		fields = add2Fields("m﻿ORIGGPCD", "mORIGGPFR", "mORIGFDCD",
				"mORIGFDNM", "m10110_Sodium", "m10120_Magnesium",
				"m10150_Phosphore", "m10190_Potassium", "m10200_Calcium",
				"m10251_Manganese", "m10260_Fer", "m10290_Cuivre",
				"m10300_Zinc", "m10340_Selenium", "m10530_Iode",
				"m25000_Proteines", "m25003_Proteines_brutes__N_x_6_25",
				"m31000_Glucides", "m32000_Sucres",
				"m327_Energie__Reglement_UE_1169_2011",
				"m328_Energie__Reglement_UE_1169_2011", "m33110_Amidon",
				"m332_Energie__N_x_facteur_Jones__avec_fibres",
				"m333_Energie__N_x_facteur_Jones__avec_fibres",
				"m34000_Polyols_totaux", "m34100_Fibres", "m400_Eau",
				"m40000_Lipides", "m40302_AG_satures",
				"m40303_AG_monoinsatures", "m40304_AG_polyinsatures",
				"m40400_AG_4_0__butyrique", "m40600_AG_6_0__caproique",
				"m40800_AG_8_0__caprylique", "m41000_AG_10_0__caprique",
				"m41200_AG_12_0__laurique", "m41400_AG_14_0__myristique",
				"m41600_AG_16_0__palmitique", "m41800_AG_18_0__stearique",
				"m41819_AG_18_1_9c___oleique",
				"m41826_AG_18_2_9c_12c___linoleique",
				"m41833_AG_18_3_c9_c12_c15___alpha_linolenique",
				"m42046_AG_20_4_5c_8c_11c_14c___arachidonique",
				"m42053_AG_20_5_5c_8c_11c_14c_17c___EPA",
				"m42263_AG_22_6_4c_7c_10c_13c_16c_19c___DHA", "m51200_Retinol",
				"m51330_Beta_Carotene", "m52100_Vitamine_D",
				"m53100_Vitamine_E", "m54100_Vitamine_K", "m55100_Vitamine_C",
				"m56100_Vitamine_B1_ou_Thiamine",
				"m56200_Vitamine_B2_ou_Riboflavine",
				"m56310_Vitamine_B3_ou_PP_ou_Niacine",
				"m56400_Vitamine_B5_ou_Acide_pantothenique",
				"m56500_Vitamine_B6", "m56600_Vitamine_B12",
				"m56700_Vitamine_B9_ou_Folates_totaux", "m60000_Alcool",
				"m65000_Acides_organiques", "m75100_Cholesterol");
		details = add2Fields("﻿ORIGGPCD", "ORIGGPFR", "ORIGFDCD", "ORIGFDNM",
				"10110 Sodium (mg/100g)", "10120 Magnésium (mg/100g)",
				"10150 Phosphore (mg/100g)", "10190 Potassium (mg/100g)",
				"10200 Calcium (mg/100g)", "10251 Manganèse (mg/100g)",
				"10260 Fer (mg/100g)", "10290 Cuivre (mg/100g)",
				"10300 Zinc (mg/100g)", "10340 Sélénium (µg/100g)",
				"10530 Iode (µg/100g)", "25000 Protéines (g/100g)",
				"25003 Protéines brutes, N x 6_25 (g/100g)",
				"31000 Glucides (g/100g)", "32000 Sucres (g/100g)",
				"327 Energie, Règlement UE 1169/2011 (kJ/100g)",
				"328 Energie, Règlement UE 1169/2011 (kcal/100g)",
				"33110 Amidon (g/100g)",
				"332 Energie, N x facteur Jones, avec fibres (kJ/100g)",
				"333 Energie, N x facteur Jones, avec fibres (kcal/100g)",
				"34000 Polyols totaux (g/100g)", "34100 Fibres (g/100g)",
				"400 Eau (g/100g)", "40000 Lipides (g/100g)",
				"40302 AG saturés (g/100g)", "40303 AG monoinsaturés (g/100g)",
				"40304 AG polyinsaturés (g/100g)",
				"40400 AG 4:0, butyrique (g/100g)",
				"40600 AG 6:0, caproïque (g/100g)",
				"40800 AG 8:0, caprylique (g/100g)",
				"41000 AG 10:0, caprique (g/100g)",
				"41200 AG 12:0, laurique (g/100g)",
				"41400 AG 14:0, myristique (g/100g)",
				"41600 AG 16:0, palmitique (g/100g)",
				"41800 AG 18:0, stéarique (g/100g)",
				"41819 AG 18:1 9c (n-9), oléique (g/100g)",
				"41826 AG 18:2 9c,12c (n-6), linoléique (g/100g)",
				"41833 AG 18:3 c9,c12,c15 (n-3), alpha-linolénique (g/100g)",
				"42046 AG 20:4 5c,8c,11c,14c (n-6), arachidonique (g/100g)",
				"42053 AG 20:5 5c,8c,11c,14c,17c (n-3), EPA (g/100g)",
				"42263 AG 22:6 4c,7c,10c,13c,16c,19c (n-3), DHA (g/100g)",
				"51200 Rétinol (µg/100g)", "51330 Beta-Carotène (µg/100g)",
				"52100 Vitamine D (µg/100g)", "53100 Vitamine E (mg/100g)",
				"54100 Vitamine K (µg/100g)", "55100 Vitamine C (mg/100g)",
				"56100 Vitamine B1 ou Thiamine (mg/100g)",
				"56200 Vitamine B2 ou Riboflavine (mg/100g)",
				"56310 Vitamine B3 ou PP ou Niacine (mg/100g)",
				"56400 Vitamine B5 ou Acide pantothénique (mg/100g)",
				"56500 Vitamine B6 (mg/100g)", "56600 Vitamine B12 (µg/100g)",
				"56700 Vitamine B9 ou Folates totaux (µg/100g)",
				"60000 Alcool (g/100g)", "65000 Acides organiques (g/100g)",
				"75100 Cholestérol (mg/100g)");
		units = add2Fields("", "", "", "", "(mg/100g)", "(mg/100g)",
				"(mg/100g)", "(mg/100g)", "(mg/100g)", "(mg/100g)",
				"(mg/100g)", "(mg/100g)", "(mg/100g)", "(µg/100g)",
				"(µg/100g)", "(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)",
				"(kJ/100g)", "(kcal/100g)", "(g/100g)", "(kJ/100g)",
				"(kcal/100g)", "(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)",
				"(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)",
				"(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)",
				"(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)", "(g/100g)",
				"(g/100g)", "(g/100g)", "(µg/100g)", "(µg/100g)", "(µg/100g)",
				"(mg/100g)", "(µg/100g)", "(mg/100g)", "(mg/100g)",
				"(mg/100g)", "(mg/100g)", "(mg/100g)", "(mg/100g)",
				"(µg/100g)", "(µg/100g)", "(g/100g)", "(g/100g)", "(mg/100g)");
	}

	@Id
	String ORIGGPCD;
	Double m10150Phosphore;

	// END
}