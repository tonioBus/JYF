/**
 * 
 */
package com.justyour.food.shared.nutrition;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.justyour.food.shared.jpa.models.IGwtRPC;

/**
 * @author tonio
 * 
 */

@Entity
public class Regime implements IGwtRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7250859774752717712L;

	@Id
	protected String title;
	
	public Regime() {
	}


}
