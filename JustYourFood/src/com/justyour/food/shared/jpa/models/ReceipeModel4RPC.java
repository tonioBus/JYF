/**
 * 
 */
package com.justyour.food.shared.jpa.models;

/**
 * @author tonio
 *
 */
public class ReceipeModel4RPC implements IGwtRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7039839902451470593L;
	protected String link;
	protected String title;
	
	public ReceipeModel4RPC() {	
	}
	
	public ReceipeModel4RPC(String link, String title) {
		this.link = link;
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

}
