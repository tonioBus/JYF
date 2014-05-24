package com.justyour.food.client.pages;

import com.google.gwt.user.cellview.client.CellTable;

public interface CiqualCellTableResource extends CellTable.Resources {
	public interface CiqualCellTableStyle extends CellTable.Style {
	};

	@Source({ "CiqualCellTable.css" })
	CiqualCellTableStyle cellTableStyle();
};
