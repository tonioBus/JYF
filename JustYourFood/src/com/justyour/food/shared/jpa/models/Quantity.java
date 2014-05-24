/**
 * 
 */
package com.justyour.food.shared.jpa.models;

import javax.persistence.Embeddable;

/**
 * @author tonio
 * 
 */
@Embeddable
public class Quantity implements IGwtRPC {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7626496158691281161L;
	
	Numeral numeral = null;
	String unity = null;

	public Quantity() {	
		numeral = new Numeral();
	}
	
	public Numeral getNumeral() {
		return numeral;
	}

	public void setNumeral(Numeral numeral) {
		this.numeral = numeral;
	}

	public String getUnity() {
		return unity;
	}

	public void setUnity(String unity) {
		this.unity = unity;
	}

	public boolean isEmpty() {
		return numeral == null && unity == null;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.numeral+" "+this.unity);
		return sb.toString();
	}
	
}
