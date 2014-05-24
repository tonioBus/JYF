/**
 * 
 */
package com.justyour.food.client.pages.Ciqual;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.justyour.food.client.CiqualOracle;
import com.justyour.food.client.FlowControl;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.pages.Error;
import com.justyour.food.client.pages.tools.WatermarkedSuggestBox;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * @author tonio
 * 
 */
public class RequestCiqual extends RootPage {

	private static int VISIBLE_LINES = 15;
	public static final int MAX_SUGGESTIONS = 10;
	private WatermarkedSuggestBox nameField;
	private SimplePager simplePager;
	final HTML ciqualNumberLabel = new HTML();
	private JYFServiceAsync server = Index.getServer();
	CiqualOracle ciqualOracle = new CiqualOracle();
	// Create a CellTable.
	private CellTable<CiqualModel> cellTable;
	private VerticalPanel panelTable;
	// Create a data provider.
	final MyDataProvider dataProvider = new MyDataProvider();
	private String text;

	public static String getSimpleName() {
		return ToolsClient.getSimpleName(RequestCiqual.class);
	}

	/**
	 * A custom {@link AsyncDataProvider}.
	 */
	class MyDataProvider extends AsyncDataProvider<CiqualModel> {
		/**
		 * {@link #onRangeChanged(HasData)} is called when the table requests a
		 * new range of data. You can push data back to the displays using
		 * {@link #updateRowData(int, List)}.
		 */
		@Override
		protected void onRangeChanged(HasData<CiqualModel> display) {
			// Get the new range.
			final Range range = display.getVisibleRange();

			/*
			 * Query the data asynchronously. If you are using a database, you
			 * can make an RPC call here. We'll use a Timer to simulate a delay.
			 */
			final int start = range.getStart();
			final int length = range.getLength();
			FlowControl.showWaitCursor();
			String text = nameField.getText().trim();
			if (text.length() == 0)
				text = "*:*";
			server.getCiqual(text, start, length,
					new AsyncCallback<CiqualModel[]>() {

						@Override
						public void onSuccess(CiqualModel[] results) {
							ArrayList<CiqualModel> listCiquas = new ArrayList<CiqualModel>();
							if (results != null) {
								for (CiqualModel ciqua : results) {
									listCiquas.add(ciqua);
								}
							}
							updateRowData(start, listCiquas);
							FlowControl.showDefaultCursor();
						}

						@Override
						public void onFailure(Throwable caught) {
							PageArguments args1 = new PageArguments(Error.class);
							History.newItem(args1.getURL());
						}
					});
		}
	}

	private class Body extends MyComposite {

		public Body() {
			final VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(10);

			HTMLPanel header = new HTMLPanel("h2",
					"Moteur de recherche Ingredients");
			panel.add(header);

			// INFOS LINE
			HorizontalPanel infoHpanel = new HorizontalPanel();
			ToolsClient.setStyle(infoHpanel, "width", "100%");
			infoHpanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			infoHpanel.setSpacing(10);
			infoHpanel.setStyleName("jyf-border", true);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

			nameField = new WatermarkedSuggestBox(ciqualOracle, "Ingredients");
			nameField.setTitle("Moteur de recherche d'ingredients");
			nameField.setWidth("25em");
			nameField.setLimit(MAX_SUGGESTIONS);
			nameField.getElement().setAttribute("type", "search");
			panel.getElement().setAttribute("style",
					"margin: 0 auto; text-align: center;");
			nameField.setAutoSelectEnabled(false);
			nameField.setFocus(true);
			hPanel.setStyleName("jyf-border", true);
			hPanel.add(nameField);

			infoHpanel.add(hPanel);
			infoHpanel.add(ciqualNumberLabel);
			panel.add(infoHpanel);
			panel.add(hPanel);

			// CELL TABLE
			RequestCiqualCellTableResource resource = GWT
					.create(RequestCiqualCellTableResource.class);
			cellTable = new CellTable<CiqualModel>(VISIBLE_LINES, resource);
			//
			cellTable.addCellPreviewHandler(new Handler<CiqualModel>() {

				@Override
				public void onCellPreview(CellPreviewEvent<CiqualModel> event) {
					boolean isClick = "click".equals(event.getNativeEvent()
							.getType());
					if (isClick) {
						CiqualModel ciqual = event.getValue();
						PageArguments args1 = new PageArguments(Ciqual.class,
								ciqual.getMORIGFDNM());
						History.newItem(args1.getURL());
					}
				}

			});

			// GROUP
			TextColumn<CiqualModel> groupColumn = new TextColumn<CiqualModel>() {
				@Override
				public String getValue(CiqualModel ciqual) {
					return ciqual.getMORIGGPFR();
				}
			};
			cellTable.addColumn(groupColumn, "Groupe");
			// INGREDIENT
			TextColumn<CiqualModel> ingredientColumn = new TextColumn<CiqualModel>() {
				@Override
				public String getValue(CiqualModel ciqual) {
					return ciqual.getMORIGFDNM();
				}
			};
			cellTable.addColumn(ingredientColumn, "Ingredient");
			// Energy
			TextColumn<CiqualModel> energyColumn = new TextColumn<CiqualModel>() {
				@Override
				public String getValue(CiqualModel ciqual) {
					return ciqual.m_Energie_Reglement_UE_1169_2011_328 + "";
				}
			};
			cellTable.addColumn(energyColumn, "Energie\n(kcal/100g)");
			// Chorosterol
			TextColumn<CiqualModel> cholesterolColumn = new TextColumn<CiqualModel>() {
				@Override
				public String getValue(CiqualModel ciqual) {
					return ciqual.m_Cholesterol_75100 + "";
				}
			};
			cellTable.addColumn(cholesterolColumn, " Cholestérol\n(mg/100g)");
			// Protéines
			TextColumn<CiqualModel> proteinesColumn = new TextColumn<CiqualModel>() {
				@Override
				public String getValue(CiqualModel ciqual) {
					return ciqual.m_Proteines_25000 + "";
				}
			};
			cellTable.addColumn(proteinesColumn, "Protéines\n(g/100g)");
			// Lipides
			TextColumn<CiqualModel> lipidesColumn = new TextColumn<CiqualModel>() {
				@Override
				public String getValue(CiqualModel ciqual) {
					return ciqual.m_Lipides_40000 + "";
				}
			};
			cellTable.addColumn(lipidesColumn, "Lipides\n(g/100g)");
			// Glucides
			TextColumn<CiqualModel> glucidesColumn = new TextColumn<CiqualModel>() {
				@Override
				public String getValue(CiqualModel ciqual) {
					return ciqual.m_Glucides_31000 + "";
				}
			};
			cellTable.addColumn(glucidesColumn, "Glucides\n(g/100g)");

			panelTable = new VerticalPanel();
			panelTable.setStyleName("section");
			panelTable.add(cellTable);
			panelTable.setStyleName("jyf-border", true);
			ToolsClient.setStyle(panelTable, "width", "100%");
			panel.add(panelTable);
			dataProvider.addDataDisplay(cellTable);
			cellTable.setPageStart(0);
			cellTable.setPageSize(VISIBLE_LINES);
			simplePager = new SimplePager();
			simplePager.setDisplay(cellTable);
			simplePager.setRangeLimited(true);
			panelTable.add(simplePager);

			server.getNumberDBCiqual(new AsyncCallback<Long>() {

				@Override
				public void onSuccess(Long size) {
					ciqualNumberLabel.setText("Ingredients: " + size);
				}

				@Override
				public void onFailure(Throwable caught) {
					PageArguments args1 = new PageArguments(Error.class);
					History.newItem(args1.getURL());
				}
			});

			// Create a handler for the sendButton and nameField
			class AskReceipeHandler implements KeyUpHandler {
				/**
				 * Fired when the user clicks on the sendButton.
				 */
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						final String text = nameField.getText();

						PageArguments args = new PageArguments(
								RequestCiqual.class, text);
						History.newItem(args.getURL(), true);

					}
				}

			}
			AskReceipeHandler askReceipeHandler = new AskReceipeHandler();
			nameField.addKeyUpHandler(askReceipeHandler);
		}
	}

	/**
	 * Add the cellList to the dataProvider.
	 */
	protected void updateTable(final String suggest) {
		if (suggest.trim().length() == 0) {
			simplePager.setVisible(false);
			cellTable.setVisible(false);
			panelTable.setVisible(false);
			Range range = new Range(0, 0);
			cellTable.setVisibleRangeAndClearData(range, true);
			return;
		}
		server.getNumberOfSuggestedCiquals(suggest, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				String error = "Erreur durant le RPC [getNumberOfSuggestions("
						+ suggest + ")]";
				PageArguments args1 = new PageArguments(Error.class);
				FlowControl.go(new Error(args1, error, caught, new Exception(
						error)), args1, false);
			}

			@Override
			public void onSuccess(Long result) {
				int visibleLength = result > VISIBLE_LINES ? VISIBLE_LINES
						: result.intValue();
				simplePager.setVisible(true);
				cellTable.setVisible(true);
				panelTable.setVisible(true);
				simplePager.startLoading();
				simplePager.firstPage();
				simplePager.setRangeLimited(true);
				dataProvider.updateRowCount(result.intValue(), true);
				Range range = new Range(0, visibleLength);
				cellTable.setVisibleRangeAndClearData(range, true);
			}
		});
	}

	/**
	 * This is the entry point method.
	 * 
	 * @param args
	 *            - 0 : the searching pattern
	 */
	public RequestCiqual(final PageArguments argsParam) {
		super(RequestCiqual.class);
		this.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		text = "";
		if (args.getArgs().length > 0) {
			text = args.getArgs()[0].trim();
		}
		nameField.setValue(text);
		updateTable(text);
	}

	@Override
	public String getTitle() {
		return "Moteur de recherche ingredient - nutrition: " + text;
	}
}
