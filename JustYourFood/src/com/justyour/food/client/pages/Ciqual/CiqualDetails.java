package com.justyour.food.client.pages.Ciqual;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.justyour.food.client.pages.CiqualCellTableResource;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

public class CiqualDetails extends VerticalPanel implements ICiqualUpdate {
	CellTable<CiqualLine> cellTable;

	class CiqualLine {

		public CiqualLine(String field, String value, String unity) {
			super();
			this.field = field;
			this.value = value;
			this.unity = unity;
		}

		String field;
		String value;
		String unity;
	};

	/**
	 * A custom {@link AsyncDataProvider}.
	 */
	class MyDataProvider extends AsyncDataProvider<CiqualLine> {
		CiqualModel ciqual;

		public void setCiqualModel(CiqualModel ciqual) {
			this.ciqual = ciqual;
			ciqual.populateValue();
		}

		@Override
		protected void onRangeChanged(HasData<CiqualLine> display) {
			// Get the new range.
			final Range range = display.getVisibleRange();
			final int start = range.getStart();

			List<CiqualLine> data = new ArrayList<CiqualLine>();
			if (ciqual != null) {
				int length = CiqualModel.getDetails().length;
				for (int i = 0; i < length; i++) {
					CiqualLine ciqualLine = new CiqualLine(ciqual.getFieldName(i),
							ciqual.getValue(i), ciqual.getUnit(i));
					data.add(ciqualLine);
				}
			}
			updateRowData(start, data);
			cellTable.redraw();
		}

	}

	private MyDataProvider dataProvider;

	public CiqualDetails() {
		// CELL TABLE
		CiqualCellTableResource resource = GWT.create(CiqualCellTableResource.class);
		cellTable = new CellTable<CiqualLine>(0, resource);
		dataProvider = new MyDataProvider();
		VerticalPanel panelTable = new VerticalPanel();
		panelTable.add(cellTable);
		cellTable.setWidth("100%");

		// Désignation
		TextColumn<CiqualLine> designationColumn = new TextColumn<CiqualLine>() {
			@Override
			public String getValue(CiqualLine ciqualLine) {
				return ciqualLine.field;
			}
		};
		cellTable.addColumn(designationColumn, "Désignation");
		// Values
		TextColumn<CiqualLine> valueColumn = new TextColumn<CiqualLine>() {
			@Override
			public String getValue(CiqualLine ciqualLine) {
				return ciqualLine.value;
			}
		};
		cellTable.addColumn(valueColumn, "Quantités");
		// Unities
		TextColumn<CiqualLine> unityColumn = new TextColumn<CiqualLine>() {
			@Override
			public String getValue(CiqualLine ciqualLine) {
				return ciqualLine.unity;
			}
		};
		cellTable.addColumn(unityColumn, "Unitées");

		dataProvider.addDataDisplay(cellTable);
		this.add(panelTable);

	}

	@Override
	public void update(CiqualModel ciqual) {
		Range range = new Range(0, ciqual.nbFields());
		dataProvider.setCiqualModel(ciqual);
		dataProvider.updateRowCount(ciqual.nbFields(), true);
		cellTable.setVisibleRangeAndClearData(range, true);
	}

}
