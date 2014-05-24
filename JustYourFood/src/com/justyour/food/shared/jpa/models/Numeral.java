/**
 * 
 */
package com.justyour.food.shared.jpa.models;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author tonio
 * 
 */

@Embeddable
public class Numeral implements IGwtRPC {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4456657515465441550L;

	static public enum TypeNumeral {
		DOUBLE, SOME
	};

	@Enumerated(EnumType.STRING)
	TypeNumeral type;
	double number;

	public Numeral() {	
		// TODO
		type = TypeNumeral.DOUBLE;
	}
	
	public TypeNumeral getType() {
		return type;
	}

	public void setType(TypeNumeral type) {
		this.type = type;
	}

	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Type:" + this.type + " Number:" + this.number;
	}


}
