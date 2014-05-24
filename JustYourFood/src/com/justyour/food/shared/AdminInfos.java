package com.justyour.food.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AdminInfos implements IsSerializable {
	public DumperInfos[] dumperInfos;
	public int numberReceipesFromDB;
	public int numberReceipesFromSolr;
	
	public AdminInfos() {
	}

	@Override
	public String toString() {
		// return ToStringBuilder.reflectionToString(this);
		StringBuffer sb = new StringBuffer();
		sb.append(dumperInfos);
		return sb.toString();
	}
}
