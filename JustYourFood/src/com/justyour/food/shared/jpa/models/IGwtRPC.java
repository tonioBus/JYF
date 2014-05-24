package com.justyour.food.shared.jpa.models;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface IGwtRPC extends IsSerializable, Serializable {

	/**
	 * remove all element that are not serializable, usually, persistent special
	 * type (Relation), are a problem for GWT RPC calls ...
	 */
//	public void prepare4RPC();
	
}
