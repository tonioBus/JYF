/**
 * 
 */
package com.justyour.food.client.pages;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.justyour.food.client.FlowControl;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.ReceipeOracle;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.pages.tools.WatermarkedSuggestBox;
import com.justyour.food.shared.ResultsObject.ResultGetReceipes;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * @author tonio
 * 
 */
public class RequestReceipes extends RootPage {

	private static int VISIBLE_LINES = 20;
	private static final int MAX_SUGGESTIONS = 10;
	private WatermarkedSuggestBox nameField = null;
	private SimplePager simplePager = null;
	final HTML receipesNumberLabel = new HTML();
	final HTML ingredientsNumberLabel = new HTML();
	private VerticalPanel panelTable;
	final MyDataProvider dataProvider = new MyDataProvider();
	ReceipeOracle receipeOracle = new ReceipeOracle();

	private JYFServiceAsync server = Index.getServer();

	// Create a CellTable.
	private CellTable<ReceipeModel> cellTable;

	public static String getSimpleName() {
		return ToolsClient.getSimpleName(RequestReceipes.class);
	}

	/**
	 * A custom {@link AsyncDataProvider}.
	 */
	class MyDataProvider extends AsyncDataProvider<ReceipeModel> {
		/**
		 * {@link #onRangeChanged(HasData)} is called when the table requests a
		 * new range of data. You can push data back to the displays using
		 * {@link #updateRowData(int, List)}.
		 */
		@Override
		protected void onRangeChanged(HasData<ReceipeModel> display) {
			// Get the new range.
			final Range range = display.getVisibleRange();

			final int start = range.getStart();
			final int length = range.getLength();
			FlowControl.showWaitCursor();
			String text = nameField.getText().trim();
			final String request = text;

			server.getReceipes(request, start, length,
					new AsyncCallback<ResultGetReceipes>() {

						@Override
						public void onSuccess(
								ResultGetReceipes resultGetReceipes) {
							ReceipeModel[] results = resultGetReceipes
									.getReceipes();
							long numberSuggestions = resultGetReceipes
									.getNumberOfSuggestions();
							long numberReceipes = resultGetReceipes
									.getNumberReceipes();
							receipesNumberLabel.setText(numberSuggestions
									+ " / " + numberReceipes + " recette(s)");
							long numberIngredients = resultGetReceipes
									.getNumberIngredients();
							ingredientsNumberLabel.setText(numberIngredients
									+ " ingredients");

							ArrayList<ReceipeModel> listReceipes = new ArrayList<ReceipeModel>();
							for (ReceipeModel receipe : results) {
								listReceipes.add(receipe);
							}
							updateRowData(start, listReceipes);
							cellTable.redraw();
							FlowControl.showDefaultCursor();
						}

						@Override
						public void onFailure(Throwable caught) {
							String error = "Erreur durant le RPC [getReceipes("
									+ request + ", " + start + ", " + length
									+ ")]";
							PageArguments args1 = new PageArguments(Error.class);
							FlowControl.go(new Error(args1, error, caught,
									new Exception(error)), args1, false);
						}

					});
		}
	}

	protected NumberFormat fmt = NumberFormat.getFormat("00.00");

	// public interface CellTableResource extends CellTable.Resources {
	// public interface CellTableStyle extends CellTable.Style {
	// };
	//
	// @Source({ "CellTable.css" })
	// CellTableStyle cellTableStyle();
	// };

	private class Body extends MyComposite {

		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);

//			panel.setSpacing(10);
//			HTMLPanel header = new HTMLPanel("h2",
//					"Moteur de recherche de recettes");
//			panel.add(header);

			// INFOS LINE
			HorizontalPanel infoHpanel = new HorizontalPanel();
			ToolsClient.setStyle(infoHpanel, "width", "100%");
			infoHpanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			infoHpanel.setSpacing(10);
			// infoHpanel.setStyleName("jyf-border", true);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

			nameField = new WatermarkedSuggestBox(receipeOracle, "Recettes");
			nameField.setTitle("Moteur de recherche de Recettes");
			nameField.setWidth("25em");
			nameField.setLimit(MAX_SUGGESTIONS);
			nameField.getElement().setAttribute("type", "search");
			panel.getElement().setAttribute("style",
					"margin: 0 auto; text-align: center;");
			nameField.setAutoSelectEnabled(false);
			nameField.setFocus(true);
			// hPanel.setStyleName("jyf-border", true);
			hPanel.add(nameField);

			infoHpanel.add(receipesNumberLabel);
			infoHpanel.add(ingredientsNumberLabel);
			panel.add(infoHpanel);
			panel.add(hPanel);

			// CELL TABLE
			CellTableResource resource = GWT.create(CellTableResource.class);
			cellTable = new CellTable<ReceipeModel>(VISIBLE_LINES, resource);
			//
			cellTable.addCellPreviewHandler(new Handler<ReceipeModel>() {

				@Override
				public void onCellPreview(CellPreviewEvent<ReceipeModel> event) {
					boolean isClick = "click".equals(event.getNativeEvent()
							.getType());
					if (isClick) {
						ReceipeModel receipe = event.getValue();
						PageArguments args1 = new PageArguments(Receipe.class,
								receipe.getLink(), nameField.getText());
						History.newItem(args1.getURL());
					}
				}

			});

			// Title Column.
			TextColumn<ReceipeModel> titleColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					return receipe.getTitle();
				}

				@Override
				public void setCellStyleNames(String styleNames) {
					super.setCellStyleNames(styleNames);
				}
			};
			cellTable.addColumn(titleColumn, "Titre");
			titleColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			titleColumn
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_START);

			// Number of Persons Column.
			TextColumn<ReceipeModel> quantityColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					return receipe.getNumberPersons() + "";
				}
			};
			cellTable.addColumn(quantityColumn, "Nb");
			quantityColumn
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			quantityColumn
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			// Number of Personnes Column.
			TextColumn<ReceipeModel> difficultyColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					return receipe.getDifficultySentence();
				}
			};
			cellTable.addColumn(difficultyColumn, "Difficulté");
			difficultyColumn
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			difficultyColumn
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			// Price Column.
			TextColumn<ReceipeModel> priceColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					return receipe.getPriceSentence();
				}
			};
			cellTable.addColumn(priceColumn, "Prix");
			priceColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			priceColumn
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			// Total Time Column.
			TextColumn<ReceipeModel> totalTimeColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					String num = fmt.format(receipe.getCookingSecond() / 60);
					return num + " min";
				}
			};
			cellTable.addColumn(totalTimeColumn, "T. Prep.");
			totalTimeColumn
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			totalTimeColumn
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			// Preparation Time Column.
			TextColumn<ReceipeModel> prepTimeColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					String num = fmt
							.format(receipe.getPreparationSecond() / 60);
					return num + " min";
				}
			};
			// Interprétation Level
			TextColumn<ReceipeModel> interpretationLevelColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					String ret = ToolsClient.getFormattedNumber(receipe
							.getInterpretationLevel() * 100);
					ret = ret + "%";
					return ret;
				}
			};

			cellTable.addColumn(prepTimeColumn, "T. Total");
			prepTimeColumn
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			prepTimeColumn
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			cellTable.addColumn(interpretationLevelColumn, "Interpretation");
			interpretationLevelColumn
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			interpretationLevelColumn
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			// Create a data provider.
			panelTable = new VerticalPanel();
			// panelTable.setStyleName("section");
			panelTable.add(cellTable);
			// panelTable.setStyleName("jyf-border", true);
			ToolsClient.setStyle(panelTable, "width", "100%");
			panel.add(panelTable);
			dataProvider.addDataDisplay(cellTable);
			cellTable.setPageStart(0);
			cellTable.setPageSize(VISIBLE_LINES);
			simplePager = new SimplePager();// TextLocation.CENTER);
			simplePager.setDisplay(cellTable);
			simplePager.setRangeLimited(true);
			panelTable.add(simplePager);
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
		server.getNumberOfSuggestedReceipes(suggest, new AsyncCallback<Long>() {

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

	// Create a handler for the sendButton and nameField
	class AskReceipeHandler implements KeyUpHandler {

		/**
		 * Fired when the user types in the nameField.
		 */
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				final String text = nameField.getText();

				PageArguments args = new PageArguments(RequestReceipes.class,
						text);
				History.newItem(args.getURL(), true);
			}
		}
	}

	/**
	 * This is the entry point method.
	 * 
	 * @param args
	 *            - 0 : the searching pattern
	 */
	public RequestReceipes(final PageArguments args) {
		super(RequestReceipes.class);
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		String text = "";
		if (args.getArgs().length > 0) {
			text = args.getArgs()[0].trim();
		}
		nameField.setText(text);
		updateTable(text);
	}

	@Override
	public String getTitle() {
		return "Moteur de recherche de recettes: " + nameField.getText();
	}
}
