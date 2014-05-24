/**
 * 
 */
package com.justyour.food.client.pages.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.justyour.food.client.Constants;
import com.justyour.food.client.FlowControl;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.ReceipeOracle;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.pages.CellTableResource;
import com.justyour.food.client.pages.Error;
import com.justyour.food.client.pages.ExpertInfo;
import com.justyour.food.client.pages.Login;
import com.justyour.food.shared.ResultsObject.ResultGetReceipes;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * @author tonio
 * 
 */
public class AdminRequestReceipes extends RootPage {

	private static int VISIBLE_LINES = 10;
	private SuggestBox nameField = null;
	private SimplePager simplePager = null;
	private Button ask4Receipe;

	private JYFServiceAsync server = Index.getServer();

	// Create a CellTable.
	private CellTable<ReceipeModel> cellTable;

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
			System.out.println("[" + new Date() + "] Update Table: field["
					+ nameField.getText() + "] start[" + start + "] length["
					+ length + "]");
			FlowControl.showWaitCursor();
			String text = nameField.getText().trim();
			final String request = text;

			server.getReceipes(request, start, length,
					new AsyncCallback<ResultGetReceipes>() {

						@Override
						public void onSuccess(ResultGetReceipes resultGetReceipes) {
							ReceipeModel[] results = resultGetReceipes.getReceipes();
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
							storeLastParams();
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

	final MyDataProvider dataProvider = new MyDataProvider();
	ReceipeOracle receipeOracle = new ReceipeOracle();

	public static String getSimpleName() {
		return ToolsClient.getSimpleName(AdminRequestReceipes.class);
	}

	protected NumberFormat fmt = NumberFormat.getFormat("00.00");
	private String text;

	private void storeLastParams() {
		PageArguments args = new PageArguments(AdminRequestReceipes.class,
				nameField.getText());
		History.newItem(args.getURL(), false);
	}

	private class Header extends MyComposite {
		public Header() {
			HorizontalPanel hPanel = new HorizontalPanel();
			initHeader(hPanel);
			hPanel.setSpacing(5);

			Button backButton = new Button(Constants.BACK);
			backButton.setStyleName("button");
			backButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					History.back();
				}
			});

			Button loginButton = new Button(Constants.HOME);
			loginButton.setStyleName("button");
			loginButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args1 = new PageArguments(Login.class);
					History.newItem(args1.getURL());
				}
			});

			Button expertInfoButton = new Button(Constants.EXPERT);
			expertInfoButton.setStyleName("button");
			expertInfoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(ExpertInfo.class);
					History.newItem(args.getURL());
				}
			});
			hPanel.setStyleName("gwt-Menu", true);
			hPanel.add(backButton);
			hPanel.add(loginButton);
			if (Index.IsAdmin())
				hPanel.add(expertInfoButton);
			hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		}
	}

	private class Body extends MyComposite {
		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(10);

			// INFOS LINE
			HorizontalPanel infoHpanel = new HorizontalPanel();
			ToolsClient.setStyle(infoHpanel, "width", "100%");
			infoHpanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			infoHpanel.setSpacing(10);
			final HTML receipesNumberLabel = new HTML();
			final HTML ingredientsNumberLabel = new HTML();
			infoHpanel.setStyleName("jyf-border", true);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

			nameField = new SuggestBox(receipeOracle);
			nameField.getElement().setAttribute("type", "search");
			nameField.setStyleName("gwt-SuggestBox");
			nameField.setLimit(20);
			nameField.setAutoSelectEnabled(true);
			nameField.setFocus(true);
			hPanel.add(nameField);
			ask4Receipe = new Button("Recettes");
			ask4Receipe.setStyleName("jyf-NormalButton");
			hPanel.add(ask4Receipe);

			infoHpanel.add(hPanel);
			infoHpanel.add(receipesNumberLabel);
			infoHpanel.add(ingredientsNumberLabel);
			panel.add(infoHpanel);

			// CELL TABLE
			CellTableResource resource = GWT.create(CellTableResource.class);
			cellTable = new CellTable<ReceipeModel>(VISIBLE_LINES, resource);
			cellTable.addCellPreviewHandler(new Handler<ReceipeModel>() {

				@Override
				public void onCellPreview(CellPreviewEvent<ReceipeModel> event) {
					boolean isClick = "click".equals(event.getNativeEvent()
							.getType());
					if (isClick) {
						ReceipeModel receipe = event.getValue();
						System.out.println("CLICK DETAILS [" + receipe.getLink()
								+ "] -> " + receipe.getTitle());
						PageArguments args1 = new PageArguments(AdminReceipe.class,
								receipe.getLink(), nameField.getText());
						History.newItem(args1.getURL());
					}
				}

			});
			// Title Column.
			TextColumn<ReceipeModel> titleColumn = new TextColumn<ReceipeModel>() {
				@Override
				public String getValue(ReceipeModel receipe) {
					System.out.println("stylename(titleColumn)=["
							+ getStyleName() + "]:" + receipe.getTitle());
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
			VerticalPanel panelTable = new VerticalPanel();
			panelTable.setStyleName("section");
			panelTable.add(cellTable);
			panelTable.setStyleName("jyf-border", true);
			ToolsClient.setStyle(panelTable, "width", "100%");
			panel.add(panelTable);
			dataProvider.addDataDisplay(cellTable);
			cellTable.setPageStart(0);
			cellTable.setPageSize(VISIBLE_LINES);
			simplePager = new SimplePager(TextLocation.CENTER);
			simplePager.setDisplay(cellTable);
			simplePager.setRangeLimited(true);
			panelTable.add(simplePager);

			server.getNumberReceipes(new AsyncCallback<Integer>() {

				@Override
				public void onSuccess(Integer size) {
					receipesNumberLabel.setText("Number of receipes: " + size);
				}

				@Override
				public void onFailure(Throwable caught) {
					storeLastParams();
					String error = "Erreur durant le RPC [getNumberReceipes()]";
					PageArguments args1 = new PageArguments(Error.class);
					FlowControl.go(new Error(args1, error, caught,
							new Exception(error)), args1, false);
				}
			});
			server.getNumberIngredients(new AsyncCallback<Integer>() {

				@Override
				public void onSuccess(Integer size) {
					ingredientsNumberLabel.setText("Number of ingredients: "
							+ size);
				}

				@Override
				public void onFailure(Throwable caught) {
					storeLastParams();
					String error = "Erreur durant le RPC [getNumberIngredients()]";
					PageArguments args1 = new PageArguments(Error.class);
					FlowControl.go(new Error(args1, error, caught,
							new Exception(error)), args1, false);
				}
			});

		}
	}

	// Create a handler for the sendButton and nameField
	class AskReceipeHandler implements ClickHandler, KeyUpHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		@Override
		public void onClick(ClickEvent event) {
			final String text = nameField.getText();
			System.out.println("onClick[" + text + "]");
			// Add the cellList to the dataProvider.
			server.getNumberOfSuggestedReceipes(text, new AsyncCallback<Long>() {

				@Override
				public void onFailure(Throwable caught) {
					storeLastParams();
					String error = "Erreur durant le RPC [getNumberOfSuggestions("
							+ text + ")]";
					PageArguments args1 = new PageArguments(Error.class);
					FlowControl.go(new Error(args1, error, caught,
							new Exception(error)), args1, false);
				}

				@Override
				public void onSuccess(Long result) {
					System.out.println("Number of data: " + result);
					int visibleLength = result > VISIBLE_LINES ? VISIBLE_LINES
							: result.intValue();
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
		 * Fired when the user types in the nameField.
		 */
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				onClick(null);
			}
		}
	}

	/**
	 * This is the entry point method.
	 * 
	 * @param args
	 *            - 0 : the searching pattern
	 */
	public AdminRequestReceipes(final PageArguments args) {
		super(AdminRequestReceipes.class);
		super.header = new Header();
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		text = "";
		if (args.getArgs().length > 0) {
			text = args.getArgs()[0].trim();
		}
		System.out.println("BEGIN OF INIT RequestReceipes(" + text + ")");
		nameField.setText(text);
		AskReceipeHandler askReceipeHandler = new AskReceipeHandler();
		ask4Receipe.addClickHandler(askReceipeHandler);
		nameField.addKeyUpHandler(askReceipeHandler);

		ask4Receipe.fireEvent(new GwtEvent<ClickHandler>() {

			@Override
			public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
				return ClickEvent.getType();
			}

			@Override
			protected void dispatch(ClickHandler handler) {
				handler.onClick(null);
			}
		});
		System.out.println("END OF INIT RequestReceipes(" + text + ")");
	}

	@Override
	public String getTitle() {
		return "Admin Recette: "+text;
	}
}
