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
public class CiqualModel implements IGwtRPC {

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

	/**
	 * ﻿ORIGGPCD
	 */
	public String mORIGGPCD;

	/**
	 * @return - ﻿ORIGGPCD
	 */
	public String getMORIGGPCD() {
		return this.mORIGGPCD;
	}

	/**
	 * ORIGGPFR
	 */
	public String mORIGGPFR;

	/**
	 * @return - ORIGGPFR
	 */
	public String getMORIGGPFR() {
		return this.mORIGGPFR;
	}

	/**
	 * ORIGFDCD
	 */
	public String mORIGFDCD;

	/**
	 * @return - ORIGFDCD
	 */
	public String getMORIGFDCD() {
		return this.mORIGFDCD;
	}

	@Id
	/**
	ORIGFDNM
	 */
	public String mORIGFDNM;

	/**
	 * @return - ORIGFDNM
	 */
	public String getMORIGFDNM() {
		return this.mORIGFDNM;
	}

	/**
	 * 10110 Sodium (mg/100g)
	 */
	public Double m_Sodium_10110;

	/**
	 * @return - 10110 Sodium (mg/100g)
	 */
	public Double getM_Sodium_10110() {
		return this.m_Sodium_10110;
	}

	/**
	 * 10120 Magnésium (mg/100g)
	 */
	public Double m_Magnesium_10120;

	/**
	 * @return - 10120 Magnésium (mg/100g)
	 */
	public Double getM_Magnesium_10120() {
		return this.m_Magnesium_10120;
	}

	/**
	 * 10150 Phosphore (mg/100g)
	 */
	public Double m_Phosphore_10150;

	/**
	 * @return - 10150 Phosphore (mg/100g)
	 */
	public Double getM_Phosphore_10150() {
		return this.m_Phosphore_10150;
	}

	/**
	 * 10190 Potassium (mg/100g)
	 */
	public Double m_Potassium_10190;

	/**
	 * @return - 10190 Potassium (mg/100g)
	 */
	public Double getM_Potassium_10190() {
		return this.m_Potassium_10190;
	}

	/**
	 * 10200 Calcium (mg/100g)
	 */
	public Double m_Calcium_10200;

	/**
	 * @return - 10200 Calcium (mg/100g)
	 */
	public Double getM_Calcium_10200() {
		return this.m_Calcium_10200;
	}

	/**
	 * 10251 Manganèse (mg/100g)
	 */
	public Double m_Manganese_10251;

	/**
	 * @return - 10251 Manganèse (mg/100g)
	 */
	public Double getM_Manganese_10251() {
		return this.m_Manganese_10251;
	}

	/**
	 * 10260 Fer (mg/100g)
	 */
	public Double m_Fer_10260;

	/**
	 * @return - 10260 Fer (mg/100g)
	 */
	public Double getM_Fer_10260() {
		return this.m_Fer_10260;
	}

	/**
	 * 10290 Cuivre (mg/100g)
	 */
	public Double m_Cuivre_10290;

	/**
	 * @return - 10290 Cuivre (mg/100g)
	 */
	public Double getM_Cuivre_10290() {
		return this.m_Cuivre_10290;
	}

	/**
	 * 10300 Zinc (mg/100g)
	 */
	public Double m_Zinc_10300;

	/**
	 * @return - 10300 Zinc (mg/100g)
	 */
	public Double getM_Zinc_10300() {
		return this.m_Zinc_10300;
	}

	/**
	 * 10340 Sélénium (µg/100g)
	 */
	public Double m_Selenium_10340;

	/**
	 * @return - 10340 Sélénium (µg/100g)
	 */
	public Double getM_Selenium_10340() {
		return this.m_Selenium_10340;
	}

	/**
	 * 10530 Iode (µg/100g)
	 */
	public Double m_Iode_10530;

	/**
	 * @return - 10530 Iode (µg/100g)
	 */
	public Double getM_Iode_10530() {
		return this.m_Iode_10530;
	}

	/**
	 * 25000 Protéines (g/100g)
	 */
	public Double m_Proteines_25000;

	/**
	 * @return - 25000 Protéines (g/100g)
	 */
	public Double getM_Proteines_25000() {
		return this.m_Proteines_25000;
	}

	/**
	 * 25003 Protéines brutes, N x 6_25 (g/100g)
	 */
	public Double m_Proteines_brutes_N_x_6_25_25003;

	/**
	 * @return - 25003 Protéines brutes, N x 6_25 (g/100g)
	 */
	public Double getM_Proteines_brutes_N_x_6_25_25003() {
		return this.m_Proteines_brutes_N_x_6_25_25003;
	}

	/**
	 * 31000 Glucides (g/100g)
	 */
	public Double m_Glucides_31000;

	/**
	 * @return - 31000 Glucides (g/100g)
	 */
	public Double getM_Glucides_31000() {
		return this.m_Glucides_31000;
	}

	/**
	 * 32000 Sucres (g/100g)
	 */
	public Double m_Sucres_32000;

	/**
	 * @return - 32000 Sucres (g/100g)
	 */
	public Double getM_Sucres_32000() {
		return this.m_Sucres_32000;
	}

	/**
	 * 327 Energie, Règlement UE 1169/2011 (kJ/100g)
	 */
	public Double m_Energie_Reglement_UE_1169_2011_327;

	/**
	 * @return - 327 Energie, Règlement UE 1169/2011 (kJ/100g)
	 */
	public Double getM_Energie_Reglement_UE_1169_2011_327() {
		return this.m_Energie_Reglement_UE_1169_2011_327;
	}

	/**
	 * 328 Energie, Règlement UE 1169/2011 (kcal/100g)
	 */
	public Double m_Energie_Reglement_UE_1169_2011_328;

	/**
	 * @return - 328 Energie, Règlement UE 1169/2011 (kcal/100g)
	 */
	public Double getM_Energie_Reglement_UE_1169_2011_328() {
		return this.m_Energie_Reglement_UE_1169_2011_328;
	}

	/**
	 * 33110 Amidon (g/100g)
	 */
	public Double m_Amidon_33110;

	/**
	 * @return - 33110 Amidon (g/100g)
	 */
	public Double getM_Amidon_33110() {
		return this.m_Amidon_33110;
	}

	/**
	 * 332 Energie, N x facteur Jones, avec fibres (kJ/100g)
	 */
	public Double m_Energie_N_x_facteur_Jones_avec_fibres_332;

	/**
	 * @return - 332 Energie, N x facteur Jones, avec fibres (kJ/100g)
	 */
	public Double getM_Energie_N_x_facteur_Jones_avec_fibres_332() {
		return this.m_Energie_N_x_facteur_Jones_avec_fibres_332;
	}

	/**
	 * 333 Energie, N x facteur Jones, avec fibres (kcal/100g)
	 */
	public Double m_Energie_N_x_facteur_Jones_avec_fibres_333;

	/**
	 * @return - 333 Energie, N x facteur Jones, avec fibres (kcal/100g)
	 */
	public Double getM_Energie_N_x_facteur_Jones_avec_fibres_333() {
		return this.m_Energie_N_x_facteur_Jones_avec_fibres_333;
	}

	/**
	 * 34000 Polyols totaux (g/100g)
	 */
	public Double m_Polyols_totaux_34000;

	/**
	 * @return - 34000 Polyols totaux (g/100g)
	 */
	public Double getM_Polyols_totaux_34000() {
		return this.m_Polyols_totaux_34000;
	}

	/**
	 * 34100 Fibres (g/100g)
	 */
	public Double m_Fibres_34100;

	/**
	 * @return - 34100 Fibres (g/100g)
	 */
	public Double getM_Fibres_34100() {
		return this.m_Fibres_34100;
	}

	/**
	 * 400 Eau (g/100g)
	 */
	public Double m_Eau_400;

	/**
	 * @return - 400 Eau (g/100g)
	 */
	public Double getM_Eau_400() {
		return this.m_Eau_400;
	}

	/**
	 * 40000 Lipides (g/100g)
	 */
	public Double m_Lipides_40000;

	/**
	 * @return - 40000 Lipides (g/100g)
	 */
	public Double getM_Lipides_40000() {
		return this.m_Lipides_40000;
	}

	/**
	 * 40302 AG saturés (g/100g)
	 */
	public Double m_AG_satures_40302;

	/**
	 * @return - 40302 AG saturés (g/100g)
	 */
	public Double getM_AG_satures_40302() {
		return this.m_AG_satures_40302;
	}

	/**
	 * 40303 AG monoinsaturés (g/100g)
	 */
	public Double m_AG_monoinsatures_40303;

	/**
	 * @return - 40303 AG monoinsaturés (g/100g)
	 */
	public Double getM_AG_monoinsatures_40303() {
		return this.m_AG_monoinsatures_40303;
	}

	/**
	 * 40304 AG polyinsaturés (g/100g)
	 */
	public Double m_AG_polyinsatures_40304;

	/**
	 * @return - 40304 AG polyinsaturés (g/100g)
	 */
	public Double getM_AG_polyinsatures_40304() {
		return this.m_AG_polyinsatures_40304;
	}

	/**
	 * 40400 AG 4:0, butyrique (g/100g)
	 */
	public Double m_AG_4_0_butyrique_40400;

	/**
	 * @return - 40400 AG 4:0, butyrique (g/100g)
	 */
	public Double getM_AG_4_0_butyrique_40400() {
		return this.m_AG_4_0_butyrique_40400;
	}

	/**
	 * 40600 AG 6:0, caproïque (g/100g)
	 */
	public Double m_AG_6_0_caproique_40600;

	/**
	 * @return - 40600 AG 6:0, caproïque (g/100g)
	 */
	public Double getM_AG_6_0_caproique_40600() {
		return this.m_AG_6_0_caproique_40600;
	}

	/**
	 * 40800 AG 8:0, caprylique (g/100g)
	 */
	public Double m_AG_8_0_caprylique_40800;

	/**
	 * @return - 40800 AG 8:0, caprylique (g/100g)
	 */
	public Double getM_AG_8_0_caprylique_40800() {
		return this.m_AG_8_0_caprylique_40800;
	}

	/**
	 * 41000 AG 10:0, caprique (g/100g)
	 */
	public Double m_AG_10_0_caprique_41000;

	/**
	 * @return - 41000 AG 10:0, caprique (g/100g)
	 */
	public Double getM_AG_10_0_caprique_41000() {
		return this.m_AG_10_0_caprique_41000;
	}

	/**
	 * 41200 AG 12:0, laurique (g/100g)
	 */
	public Double m_AG_12_0_laurique_41200;

	/**
	 * @return - 41200 AG 12:0, laurique (g/100g)
	 */
	public Double getM_AG_12_0_laurique_41200() {
		return this.m_AG_12_0_laurique_41200;
	}

	/**
	 * 41400 AG 14:0, myristique (g/100g)
	 */
	public Double m_AG_14_0_myristique_41400;

	/**
	 * @return - 41400 AG 14:0, myristique (g/100g)
	 */
	public Double getM_AG_14_0_myristique_41400() {
		return this.m_AG_14_0_myristique_41400;
	}

	/**
	 * 41600 AG 16:0, palmitique (g/100g)
	 */
	public Double m_AG_16_0_palmitique_41600;

	/**
	 * @return - 41600 AG 16:0, palmitique (g/100g)
	 */
	public Double getM_AG_16_0_palmitique_41600() {
		return this.m_AG_16_0_palmitique_41600;
	}

	/**
	 * 41800 AG 18:0, stéarique (g/100g)
	 */
	public Double m_AG_18_0_stearique_41800;

	/**
	 * @return - 41800 AG 18:0, stéarique (g/100g)
	 */
	public Double getM_AG_18_0_stearique_41800() {
		return this.m_AG_18_0_stearique_41800;
	}

	/**
	 * 41819 AG 18:1 9c (n-9), oléique (g/100g)
	 */
	public Double m_AG_18_1_9c_oleique_41819;

	/**
	 * @return - 41819 AG 18:1 9c (n-9), oléique (g/100g)
	 */
	public Double getM_AG_18_1_9c_oleique_41819() {
		return this.m_AG_18_1_9c_oleique_41819;
	}

	/**
	 * 41826 AG 18:2 9c,12c (n-6), linoléique (g/100g)
	 */
	public Double m_AG_18_2_9c_12c_linoleique_41826;

	/**
	 * @return - 41826 AG 18:2 9c,12c (n-6), linoléique (g/100g)
	 */
	public Double getM_AG_18_2_9c_12c_linoleique_41826() {
		return this.m_AG_18_2_9c_12c_linoleique_41826;
	}

	/**
	 * 41833 AG 18:3 c9,c12,c15 (n-3), alpha-linolénique (g/100g)
	 */
	public Double m_AG_18_3_c9_c12_c15_alpha_linolenique_41833;

	/**
	 * @return - 41833 AG 18:3 c9,c12,c15 (n-3), alpha-linolénique (g/100g)
	 */
	public Double getM_AG_18_3_c9_c12_c15_alpha_linolenique_41833() {
		return this.m_AG_18_3_c9_c12_c15_alpha_linolenique_41833;
	}

	/**
	 * 42046 AG 20:4 5c,8c,11c,14c (n-6), arachidonique (g/100g)
	 */
	public Double m_AG_20_4_5c_8c_11c_14c_arachidonique_42046;

	/**
	 * @return - 42046 AG 20:4 5c,8c,11c,14c (n-6), arachidonique (g/100g)
	 */
	public Double getM_AG_20_4_5c_8c_11c_14c_arachidonique_42046() {
		return this.m_AG_20_4_5c_8c_11c_14c_arachidonique_42046;
	}

	/**
	 * 42053 AG 20:5 5c,8c,11c,14c,17c (n-3), EPA (g/100g)
	 */
	public Double m_AG_20_5_5c_8c_11c_14c_17c_EPA_42053;

	/**
	 * @return - 42053 AG 20:5 5c,8c,11c,14c,17c (n-3), EPA (g/100g)
	 */
	public Double getM_AG_20_5_5c_8c_11c_14c_17c_EPA_42053() {
		return this.m_AG_20_5_5c_8c_11c_14c_17c_EPA_42053;
	}

	/**
	 * 42263 AG 22:6 4c,7c,10c,13c,16c,19c (n-3), DHA (g/100g)
	 */
	public Double m_AG_22_6_4c_7c_10c_13c_16c_19c_DHA_42263;

	/**
	 * @return - 42263 AG 22:6 4c,7c,10c,13c,16c,19c (n-3), DHA (g/100g)
	 */
	public Double getM_AG_22_6_4c_7c_10c_13c_16c_19c_DHA_42263() {
		return this.m_AG_22_6_4c_7c_10c_13c_16c_19c_DHA_42263;
	}

	/**
	 * 51200 Rétinol (µg/100g)
	 */
	public Double m_Retinol_51200;

	/**
	 * @return - 51200 Rétinol (µg/100g)
	 */
	public Double getM_Retinol_51200() {
		return this.m_Retinol_51200;
	}

	/**
	 * 51330 Beta-Carotène (µg/100g)
	 */
	public Double m_Beta_Carotene_51330;

	/**
	 * @return - 51330 Beta-Carotène (µg/100g)
	 */
	public Double getM_Beta_Carotene_51330() {
		return this.m_Beta_Carotene_51330;
	}

	/**
	 * 52100 Vitamine D (µg/100g)
	 */
	public Double m_Vitamine_D_52100;

	/**
	 * @return - 52100 Vitamine D (µg/100g)
	 */
	public Double getM_Vitamine_D_52100() {
		return this.m_Vitamine_D_52100;
	}

	/**
	 * 53100 Vitamine E (mg/100g)
	 */
	public Double m_Vitamine_E_53100;

	/**
	 * @return - 53100 Vitamine E (mg/100g)
	 */
	public Double getM_Vitamine_E_53100() {
		return this.m_Vitamine_E_53100;
	}

	/**
	 * 54100 Vitamine K (µg/100g)
	 */
	public Double m_Vitamine_K_54100;

	/**
	 * @return - 54100 Vitamine K (µg/100g)
	 */
	public Double getM_Vitamine_K_54100() {
		return this.m_Vitamine_K_54100;
	}

	/**
	 * 55100 Vitamine C (mg/100g)
	 */
	public Double m_Vitamine_C_55100;

	/**
	 * @return - 55100 Vitamine C (mg/100g)
	 */
	public Double getM_Vitamine_C_55100() {
		return this.m_Vitamine_C_55100;
	}

	/**
	 * 56100 Vitamine B1 ou Thiamine (mg/100g)
	 */
	public Double m_Vitamine_B1_ou_Thiamine_56100;

	/**
	 * @return - 56100 Vitamine B1 ou Thiamine (mg/100g)
	 */
	public Double getM_Vitamine_B1_ou_Thiamine_56100() {
		return this.m_Vitamine_B1_ou_Thiamine_56100;
	}

	/**
	 * 56200 Vitamine B2 ou Riboflavine (mg/100g)
	 */
	public Double m_Vitamine_B2_ou_Riboflavine_56200;

	/**
	 * @return - 56200 Vitamine B2 ou Riboflavine (mg/100g)
	 */
	public Double getM_Vitamine_B2_ou_Riboflavine_56200() {
		return this.m_Vitamine_B2_ou_Riboflavine_56200;
	}

	/**
	 * 56310 Vitamine B3 ou PP ou Niacine (mg/100g)
	 */
	public Double m_Vitamine_B3_ou_PP_ou_Niacine_56310;

	/**
	 * @return - 56310 Vitamine B3 ou PP ou Niacine (mg/100g)
	 */
	public Double getM_Vitamine_B3_ou_PP_ou_Niacine_56310() {
		return this.m_Vitamine_B3_ou_PP_ou_Niacine_56310;
	}

	/**
	 * 56400 Vitamine B5 ou Acide pantothénique (mg/100g)
	 */
	public Double m_Vitamine_B5_ou_Acide_pantothenique_56400;

	/**
	 * @return - 56400 Vitamine B5 ou Acide pantothénique (mg/100g)
	 */
	public Double getM_Vitamine_B5_ou_Acide_pantothenique_56400() {
		return this.m_Vitamine_B5_ou_Acide_pantothenique_56400;
	}

	/**
	 * 56500 Vitamine B6 (mg/100g)
	 */
	public Double m_Vitamine_B6_56500;

	/**
	 * @return - 56500 Vitamine B6 (mg/100g)
	 */
	public Double getM_Vitamine_B6_56500() {
		return this.m_Vitamine_B6_56500;
	}

	/**
	 * 56600 Vitamine B12 (µg/100g)
	 */
	public Double m_Vitamine_B12_56600;

	/**
	 * @return - 56600 Vitamine B12 (µg/100g)
	 */
	public Double getM_Vitamine_B12_56600() {
		return this.m_Vitamine_B12_56600;
	}

	/**
	 * 56700 Vitamine B9 ou Folates totaux (µg/100g)
	 */
	public Double m_Vitamine_B9_ou_Folates_totaux_56700;

	/**
	 * @return - 56700 Vitamine B9 ou Folates totaux (µg/100g)
	 */
	public Double getM_Vitamine_B9_ou_Folates_totaux_56700() {
		return this.m_Vitamine_B9_ou_Folates_totaux_56700;
	}

	/**
	 * 60000 Alcool (g/100g)
	 */
	public Double m_Alcool_60000;

	/**
	 * @return - 60000 Alcool (g/100g)
	 */
	public Double getM_Alcool_60000() {
		return this.m_Alcool_60000;
	}

	/**
	 * 65000 Acides organiques (g/100g)
	 */
	public Double m_Acides_organiques_65000;

	/**
	 * @return - 65000 Acides organiques (g/100g)
	 */
	public Double getM_Acides_organiques_65000() {
		return this.m_Acides_organiques_65000;
	}

	/**
	 * 75100 Cholestérol (mg/100g)
	 */
	public Double m_Cholesterol_75100;

	/**
	 * @return - 75100 Cholestérol (mg/100g)
	 */
	public Double getM_Cholesterol_75100() {
		return this.m_Cholesterol_75100;
	}

	public void populateValue() {
		this.values = new String[] { this.mORIGGPCD + "", this.mORIGGPFR + "",
				this.mORIGFDCD + "", this.mORIGFDNM + "",
				this.m_Sodium_10110 + "", this.m_Magnesium_10120 + "",
				this.m_Phosphore_10150 + "", this.m_Potassium_10190 + "",
				this.m_Calcium_10200 + "", this.m_Manganese_10251 + "",
				this.m_Fer_10260 + "", this.m_Cuivre_10290 + "",
				this.m_Zinc_10300 + "", this.m_Selenium_10340 + "",
				this.m_Iode_10530 + "", this.m_Proteines_25000 + "",
				this.m_Proteines_brutes_N_x_6_25_25003 + "",
				this.m_Glucides_31000 + "", this.m_Sucres_32000 + "",
				this.m_Energie_Reglement_UE_1169_2011_327 + "",
				this.m_Energie_Reglement_UE_1169_2011_328 + "",
				this.m_Amidon_33110 + "",
				this.m_Energie_N_x_facteur_Jones_avec_fibres_332 + "",
				this.m_Energie_N_x_facteur_Jones_avec_fibres_333 + "",
				this.m_Polyols_totaux_34000 + "", this.m_Fibres_34100 + "",
				this.m_Eau_400 + "", this.m_Lipides_40000 + "",
				this.m_AG_satures_40302 + "",
				this.m_AG_monoinsatures_40303 + "",
				this.m_AG_polyinsatures_40304 + "",
				this.m_AG_4_0_butyrique_40400 + "",
				this.m_AG_6_0_caproique_40600 + "",
				this.m_AG_8_0_caprylique_40800 + "",
				this.m_AG_10_0_caprique_41000 + "",
				this.m_AG_12_0_laurique_41200 + "",
				this.m_AG_14_0_myristique_41400 + "",
				this.m_AG_16_0_palmitique_41600 + "",
				this.m_AG_18_0_stearique_41800 + "",
				this.m_AG_18_1_9c_oleique_41819 + "",
				this.m_AG_18_2_9c_12c_linoleique_41826 + "",
				this.m_AG_18_3_c9_c12_c15_alpha_linolenique_41833 + "",
				this.m_AG_20_4_5c_8c_11c_14c_arachidonique_42046 + "",
				this.m_AG_20_5_5c_8c_11c_14c_17c_EPA_42053 + "",
				this.m_AG_22_6_4c_7c_10c_13c_16c_19c_DHA_42263 + "",
				this.m_Retinol_51200 + "", this.m_Beta_Carotene_51330 + "",
				this.m_Vitamine_D_52100 + "", this.m_Vitamine_E_53100 + "",
				this.m_Vitamine_K_54100 + "", this.m_Vitamine_C_55100 + "",
				this.m_Vitamine_B1_ou_Thiamine_56100 + "",
				this.m_Vitamine_B2_ou_Riboflavine_56200 + "",
				this.m_Vitamine_B3_ou_PP_ou_Niacine_56310 + "",
				this.m_Vitamine_B5_ou_Acide_pantothenique_56400 + "",
				this.m_Vitamine_B6_56500 + "", this.m_Vitamine_B12_56600 + "",
				this.m_Vitamine_B9_ou_Folates_totaux_56700 + "",
				this.m_Alcool_60000 + "", this.m_Acides_organiques_65000 + "",
				this.m_Cholesterol_75100 + "" };
	}

	static {
		fields = add2Fields("mORIGGPCD", "mORIGGPFR", "mORIGFDCD", "mORIGFDNM",
				"m_Sodium_10110", "m_Magnesium_10120", "m_Phosphore_10150",
				"m_Potassium_10190", "m_Calcium_10200", "m_Manganese_10251",
				"m_Fer_10260", "m_Cuivre_10290", "m_Zinc_10300",
				"m_Selenium_10340", "m_Iode_10530", "m_Proteines_25000",
				"m_Proteines_brutes_N_x_6_25_25003", "m_Glucides_31000",
				"m_Sucres_32000", "m_Energie_Reglement_UE_1169_2011_327",
				"m_Energie_Reglement_UE_1169_2011_328", "m_Amidon_33110",
				"m_Energie_N_x_facteur_Jones_avec_fibres_332",
				"m_Energie_N_x_facteur_Jones_avec_fibres_333",
				"m_Polyols_totaux_34000", "m_Fibres_34100", "m_Eau_400",
				"m_Lipides_40000", "m_AG_satures_40302",
				"m_AG_monoinsatures_40303", "m_AG_polyinsatures_40304",
				"m_AG_4_0_butyrique_40400", "m_AG_6_0_caproique_40600",
				"m_AG_8_0_caprylique_40800", "m_AG_10_0_caprique_41000",
				"m_AG_12_0_laurique_41200", "m_AG_14_0_myristique_41400",
				"m_AG_16_0_palmitique_41600", "m_AG_18_0_stearique_41800",
				"m_AG_18_1_9c_oleique_41819",
				"m_AG_18_2_9c_12c_linoleique_41826",
				"m_AG_18_3_c9_c12_c15_alpha_linolenique_41833",
				"m_AG_20_4_5c_8c_11c_14c_arachidonique_42046",
				"m_AG_20_5_5c_8c_11c_14c_17c_EPA_42053",
				"m_AG_22_6_4c_7c_10c_13c_16c_19c_DHA_42263", "m_Retinol_51200",
				"m_Beta_Carotene_51330", "m_Vitamine_D_52100",
				"m_Vitamine_E_53100", "m_Vitamine_K_54100",
				"m_Vitamine_C_55100", "m_Vitamine_B1_ou_Thiamine_56100",
				"m_Vitamine_B2_ou_Riboflavine_56200",
				"m_Vitamine_B3_ou_PP_ou_Niacine_56310",
				"m_Vitamine_B5_ou_Acide_pantothenique_56400",
				"m_Vitamine_B6_56500", "m_Vitamine_B12_56600",
				"m_Vitamine_B9_ou_Folates_totaux_56700", "m_Alcool_60000",
				"m_Acides_organiques_65000", "m_Cholesterol_75100");
		fieldNames = add2Fields("﻿ORIGGPCD", "Groupe", "ORIGFDCD",
				"Désignation", "Sodium ", "Magnésium ", "Phosphore ",
				"Potassium ", "Calcium ", "Manganèse ", "Fer ", "Cuivre ",
				"Zinc ", "Sélénium ", "Iode ", "Protéines ",
				"Protéines brutes, N x 6_25 ", "Glucides ", "Sucres ",
				"Energie, Règlement UE 1169/2011 ",
				"Energie, Règlement UE 1169/2011 ", "Amidon ",
				"Energie, N x facteur Jones, avec fibres ",
				"Energie, N x facteur Jones, avec fibres ", "Polyols totaux ",
				"Fibres ", "Eau ", "Lipides ", "AG saturés ",
				"AG monoinsaturés ", "AG polyinsaturés ", "AG 4:0, butyrique ",
				"AG 6:0, caproïque ", "AG 8:0, caprylique ",
				"AG 10:0, caprique ", "AG 12:0, laurique ",
				"AG 14:0, myristique ", "AG 16:0, palmitique ",
				"AG 18:0, stéarique ", "AG 18:1 9c ", "AG 18:2 9c,12c ",
				"AG 18:3 c9,c12,c15 ", "AG 20:4 5c,8c,11c,14c ",
				"AG 20:5 5c,8c,11c,14c,17c ", "AG 22:6 4c,7c,10c,13c,16c,19c ",
				"Rétinol ", "Beta-Carotène ", "Vitamine D ", "Vitamine E ",
				"Vitamine K ", "Vitamine C ", "Vitamine B1 ou Thiamine ",
				"Vitamine B2 ou Riboflavine ", "Vitamine B3 ou PP ou Niacine ",
				"Vitamine B5 ou Acide pantothénique ", "Vitamine B6 ",
				"Vitamine B12 ", "Vitamine B9 ou Folates totaux ", "Alcool ",
				"Acides organiques ", "Cholestérol ");
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
		units = add2Fields("", "", "", "", "mg/100g", "mg/100g", "mg/100g",
				"mg/100g", "mg/100g", "mg/100g", "mg/100g", "mg/100g",
				"mg/100g", "µg/100g", "µg/100g", "g/100g", "g/100g", "g/100g",
				"g/100g", "kJ/100g", "kcal/100g", "g/100g", "kJ/100g",
				"kcal/100g", "g/100g", "g/100g", "g/100g", "g/100g", "g/100g",
				"g/100g", "g/100g", "g/100g", "g/100g", "g/100g", "g/100g",
				"g/100g", "g/100g", "g/100g", "g/100g", "g/100g", "g/100g",
				"g/100g", "g/100g", "g/100g", "g/100g", "µg/100g", "µg/100g",
				"µg/100g", "mg/100g", "µg/100g", "mg/100g", "mg/100g",
				"mg/100g", "mg/100g", "mg/100g", "mg/100g", "µg/100g",
				"µg/100g", "g/100g", "g/100g", "mg/100g");
	}
}
