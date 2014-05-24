/**
 * 
 */
package com.justyour.food.client.pages;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.justyour.food.client.FlowControl;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.ToolsClient.Field;
import com.justyour.food.client.ToolsClient.Update;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.Quantity;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * @author tonio
 * 
 */
public class Receipe extends RootPage {

	PageArguments args;
	String link;

	// Widgets that need update at each Init of change of possible values
	// Label labelReceipe;
	// private SuggestBox nameField = null;
	Anchor linkReceipe;
	Image imageReceipe;
	HTML instructions = new HTML();
	ReceipePropPanel propPanel;
	ReceipePropPanel delayPropPanel;
	// ReceipePropPanel creationPanel;
	MyDataProvider dataProvider;
	CellTable<IngredientQuantity> cellTable;
	private JYFServiceAsync server = Index.getServer();

	public static String getSimpleName() {
		return ToolsClient.getSimpleName(Receipe.class);
	}

	/**
	 * A custom {@link AsyncDataProvider}.
	 */
	class MyDataProvider extends AsyncDataProvider<IngredientQuantity> {
		ReceipeModel receipe;

		public void setReceipe(ReceipeModel receipe) {
			this.receipe = receipe;
		}

		@Override
		protected void onRangeChanged(HasData<IngredientQuantity> display) {
			// Get the new range.
			final Range range = display.getVisibleRange();
			final int start = range.getStart();
			List<IngredientQuantity> data = new ArrayList<IngredientQuantity>();
			if (receipe != null) {
				for (IngredientQuantity iq : this.receipe.getIngredientRPC()) {
					data.add(iq);
				}
			}
			updateRowData(start, data);
			cellTable.redraw();
		}
	};

	abstract class UpdateReceipe extends ToolsClient.Update {
		ReceipeModel receipe;

		public UpdateReceipe() {
			super(null);
		}

		public void setReceipe(ReceipeModel receipe) {
			this.receipe = receipe;
		}

	};

	class ReceipePropPanel extends ToolsClient.PropPanel {

		public ReceipePropPanel(String name, Field... fields) {
			super(name, fields);
		}

		public void setReceipe(ReceipeModel receipe) {
			for (Update update : updates) {
				((UpdateReceipe) update).setReceipe(receipe);
			}
		}
	}

	private String getQtyValue(IngredientQuantity iq) {
		Quantity[] quantities = iq.getQuantitiesRPC();
		for (int i = 0; i < quantities.length; i++) {
			Quantity quantity = quantities[i];
			if (quantity.getNumeral() != null) {
				switch (quantity.getNumeral().getType()) {
				case DOUBLE:
					return ToolsClient.getFormattedQuantity(quantity
							.getNumeral().getNumber());
				case SOME:
					break;
				default:
					break;
				}
			}
		}
		return "";
	}

	private void replaceQtyValue(IngredientQuantity iq, double ratio) {
		Quantity[] quantities = iq.getQuantitiesRPC();
		for (int i = 0; i < quantities.length; i++) {
			Quantity quantity = quantities[i];
			if (quantity.getNumeral() != null) {
				switch (quantity.getNumeral().getType()) {
				case DOUBLE:
					double oldValue = quantity.getNumeral().getNumber();
					quantity.getNumeral().setNumber(oldValue * ratio);
					break;
				case SOME:
					break;
				default:
					break;
				}
			}
		}
	}

	private class Body extends MyComposite {
		public Body() {
			final VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			createForm(panel);
		}
	}

	protected void updateForm(ReceipeModel receipe) {
		imageReceipe.setUrl(receipe.getImageURL());
		linkReceipe.setHref(receipe.getLink());
		linkReceipe.setTitle("Lien vers " + receipe.getDomain());

		linkReceipe.setHref(receipe.getLink());
		linkReceipe.setText(receipe.getTitle());
		String text = receipe.getInstructions();
		String htmlRef = " ... </br><p style=\"font-weight:bold; text-align:center;\"> la suite sur <a href=\""
				+ receipe.getLink() + "\"";
		htmlRef += " title=\"la suite des instruction sur "
				+ receipe.getDomain() + "\" ";
		htmlRef += " target=\"_blank\" ";
		htmlRef += ">" + receipe.getDomain() + "</a></p>";
		if (text == null) {
			instructions.setHTML("Instructions disponible sur " + htmlRef);
		} else {
			text = text.substring(0, text.length() / 2);
			text = text.replaceAll("\n", "<br/>");
			instructions.setHTML(text + htmlRef);
		}
		int visibleLength = receipe.getIngredientRPC().length;
		dataProvider.setReceipe(receipe);
		dataProvider.updateRowCount(visibleLength, true);
		Range range = new Range(0, visibleLength);

		cellTable.setVisibleRangeAndClearData(range, true);
		propPanel.setReceipe(receipe);
		propPanel.updateFromObject(1.0);
		delayPropPanel.setReceipe(receipe);
		delayPropPanel.updateFromObject(1.0);
		// creationPanel.setReceipe(receipe);
		// creationPanel.updateFromObject(1.0);
	}

	/**
	 * Create the form, only at the first call
	 * 
	 * @param panel
	 *            - the initialized Panel
	 */
	protected void createForm(VerticalPanel panel) {

		// Title
		linkReceipe = new Anchor("Chargement ...");
		panel.add(linkReceipe);
		linkReceipe.setStyleName("receipe-title", true);
		ToolsClient.setStyle(linkReceipe, "width", "100%");
		linkReceipe.setTarget("_blank");

		// IMAGE // TEXT
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.add(mainPanel);

		// ImagePanel
		imageReceipe = new Image();
		imageReceipe.setStyleName("jyf-img", true);
		imageReceipe.getElement().setAttribute("style",
				"width: 60%; min-width: 200px;");
		mainPanel.add(imageReceipe);

		// TextPanel
		VerticalPanel textPanel = new VerticalPanel();
		textPanel.setStyleName("receipe-verticalPanel");
		mainPanel.add(textPanel);

		// Main Properties
		HorizontalPanel mainPropPanel = new HorizontalPanel();
		mainPropPanel.setStyleName("jyf-border", true);
		mainPropPanel.setSpacing(10);
		ToolsClient.setStyle(mainPropPanel, "width", "100%");

		// Image
		imageReceipe = new Image();
		imageReceipe.setStyleName("jyf-img");
		imageReceipe.getElement().setAttribute("style",
				"width: 100%; min-width: 100%;");
		mainPropPanel.add(imageReceipe);
		VerticalPanel propertiesPanel = new VerticalPanel();
		ToolsClient.setStyle(propertiesPanel, "width", "50%");
		mainPropPanel.add(propertiesPanel);
		panel.add(mainPropPanel);
		// Propriétées Principal
		ToolsClient.Field fieldPrix = new ToolsClient.Field("Prix", "", true,
				"Coût de la recette", new UpdateReceipe() {

					@Override
					public void updateFromObject(double ratio) {
						setText(receipe.getPriceSentence());
					}

					@Override
					public void updateFromGWT() {
					}
				});

		ToolsClient.Field fieldDifficulty = new ToolsClient.Field(
				"Difficultée", "", true, "Difficultée de la réalisation",
				new UpdateReceipe() {

					@Override
					public void updateFromObject(double ratio) {
						setText(receipe.getDifficultySentence());
					}

					@Override
					public void updateFromGWT() {
					}
				});

		ToolsClient.Field fieldEnergie = new ToolsClient.Field("Energie", "",
				false, "Energie calorique de la recette", new UpdateReceipe() {

					@Override
					public void updateFromObject(double ratio) {
						setText(ToolsClient.getFormattedNumber(receipe
								.getEnergie() * ratio)
								+ " calorie(s)");
					}

					@Override
					public void updateFromGWT() {
						double ratio = ToolsClient.readNumber(getText(),
								receipe.getEnergie()) / receipe.getEnergie();
						updateReceipe(receipe, ratio);
					}
				});

		ToolsClient.Field fieldNumberPart = new ToolsClient.Field(
				"Nombre de part(s)", "", false, "Nombre de part(s)",
				new UpdateReceipe() {

					@Override
					public void updateFromObject(double ratio) {
						setText("" + receipe.getNumberPersons() * ratio);
					}

					@Override
					public void updateFromGWT() {
						double ratio = ToolsClient.readNumber(getText(),
								receipe.getNumberPersons())
								/ receipe.getNumberPersons();
						updateReceipe(receipe, ratio);
					}
				});

		propPanel = new ReceipePropPanel("Propriétés", fieldPrix,
				fieldDifficulty, fieldEnergie, fieldNumberPart);

		// CREATION PANEL
		// ToolsClient.Field fieldNumber = new ToolsClient.Field("Index", "",
		// true, "Numéro de cette recette", new UpdateReceipe() {
		//
		// @Override
		// public void updateFromObject(double ratio) {
		// setText(receipe.getNumber() + "");
		// }
		//
		// @Override
		// public void updateFromGWT() {
		// }
		// });
		//
		// ToolsClient.Field fieldCreationDate = new ToolsClient.Field(
		// "Extraction", "", true,
		// "Date d'extraction de la recette", new UpdateReceipe() {
		//
		// @Override
		// public void updateFromObject(double ratio) {
		// String date = DateTimeFormat.getMediumDateFormat()
		// .format(receipe.getCreationDate());
		// setText(date);
		// }
		//
		// @Override
		// public void updateFromGWT() {
		// }
		// });
		//
		// // Interprétation Level
		// ToolsClient.Field fieldInterpretationLevel = new ToolsClient.Field(
		// "Interprét.", "", true,
		// "100% correspond à une interprétation complètement correcte",
		// new UpdateReceipe() {
		//
		// @Override
		// public void updateFromObject(double ratio) {
		// String sz = ToolsClient.getFormattedNumber(receipe
		// .getInterpretationLevel() * 100);
		// setText(sz + "%");
		// }
		//
		// @Override
		// public void updateFromGWT() {
		// }
		// });
		//
		// creationPanel = new ReceipePropPanel("Extraction", fieldNumber,
		// fieldCreationDate, fieldInterpretationLevel);

		ToolsClient.Field delayPreparation = new ToolsClient.Field(
				"Préparation", "", true, "Temps de préparation de la recette",
				new UpdateReceipe() {

					@Override
					public void updateFromObject(double ratio) {
						setText(ToolsClient.getFormattedDelay(receipe
								.getPreparationSecond() / 60));
					}

					@Override
					public void updateFromGWT() {
					}
				});
		ToolsClient.Field delayRealisation = new ToolsClient.Field(
				"Réalisation", "", true, "Temps de réalisation de la recette",
				new UpdateReceipe() {

					@Override
					public void updateFromObject(double ratio) {
						setText(ToolsClient.getFormattedDelay(receipe
								.getCookingSecond() / 60));
					}

					@Override
					public void updateFromGWT() {
					}
				});
		ToolsClient.Field delayRepos = new ToolsClient.Field("Repos", "", true,
				"Temps de repos de la recette", new UpdateReceipe() {

					@Override
					public void updateFromObject(double ratio) {
						setText(ToolsClient.getFormattedDelay(receipe
								.getRestSecond() / 60));
					}

					@Override
					public void updateFromGWT() {
					}
				});
		delayPropPanel = new ReceipePropPanel("Délais", delayPreparation,
				delayRealisation, delayRepos);

		// creationPanel.getElement().setAttribute("style",
		// "display:block; float:right; width:100%;");
		delayPropPanel.getElement().setAttribute("style",
				"display:block; float:right; width:100%;");
		propPanel.getElement().setAttribute("style",
				"display:block; float:right; width:100%;");
		// propertiesPanel.add(creationPanel);
		propertiesPanel.add(propPanel);
		propertiesPanel.add(delayPropPanel);

		// Instruction
		VerticalPanel instPanel = new VerticalPanel();
		instPanel.setStyleName("jyf-border", true);
		ToolsClient.setStyle(instPanel, "width", "100%");
		Label label2 = new Label("Instructions");
		instPanel.add(label2);
		label2.setStyleName("jyf-simpleLabel", true);
		instructions
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);
		instructions.setStyleName("jyf-simpleBorder", true);
		instructions.setStyleName("jyf-instructionsText", true);
		instPanel.add(instructions);
		panel.add(instPanel);

		// CELL TABLE
		CellTableResource resource = GWT.create(CellTableResource.class);
		cellTable = new CellTable<IngredientQuantity>(0, resource);

		// Quantity
		EditTextCell editTextCell = new EditTextCell();
		Column<IngredientQuantity, String> qtyColumn = new Column<IngredientQuantity, String>(
				editTextCell) {
			@Override
			public String getValue(IngredientQuantity iq) {
				return getQtyValue(iq);
			}
		};
		qtyColumn.setCellStyleNames("gwt-Label");

		qtyColumn
				.setFieldUpdater(new FieldUpdater<IngredientQuantity, String>() {
					@Override
					public void update(int index, IngredientQuantity iq,
							String value) {
						System.out.println("index=" + index + " IQ="
								+ iq.toString() + " feb=" + value);
						String oldValue = getQtyValue(iq);
						updateReceipe(dataProvider.receipe, oldValue, value, iq);
					}
				});
		cellTable.addColumn(qtyColumn, "Qty");
		qtyColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		qtyColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_START);

		// Unity
		TextColumn<IngredientQuantity> unityColumn = new TextColumn<IngredientQuantity>() {
			@Override
			public String getValue(IngredientQuantity iq) {
				String ret = "";
				Quantity[] quantities = iq.getQuantitiesRPC();
				for (int i = 0; i < quantities.length; i++) {
					Quantity quantity = quantities[i];
					if (quantity.getNumeral() != null
							&& quantity.getUnity() != null) {
						ret += quantity.getUnity() + "(s)";
						if (i < (quantities.length - 1)) {
							ret += " ";
						}
					}
				}
				return ret;
			}
		};
		cellTable.addColumn(unityColumn, "Unité");
		unityColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		unityColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_START);

		// TITLE
		TextColumn<IngredientQuantity> titleColumn = new TextColumn<IngredientQuantity>() {
			@Override
			public String getValue(IngredientQuantity iq) {
				return iq.getIngredient().getIngredient();
			}
		};
		cellTable.addColumn(titleColumn, "Désignation");
		titleColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		titleColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_START);

		// NUMBER OF RECEIPES
		TextColumn<IngredientQuantity> numberReceipesColumn = new TextColumn<IngredientQuantity>() {
			@Override
			public String getValue(IngredientQuantity iq) {
				return iq.getIngredient().getNumberReceipes() + "";
			}
		};
		cellTable.addColumn(numberReceipesColumn, "Nb Recettes");
		numberReceipesColumn
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		numberReceipesColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_START);

		// INTERPRETATION LEVEL
		TextColumn<IngredientQuantity> InterpretationColumn = new TextColumn<IngredientQuantity>() {
			@Override
			public String getValue(IngredientQuantity iq) {
				return iq.getInterpretationLevel() * 100 + "%";
			}
		};
		cellTable.addColumn(InterpretationColumn, "Compréhension");
		numberReceipesColumn
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		numberReceipesColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_START);

		// CREATE A DATA PROVIDER
		dataProvider = new MyDataProvider();
		VerticalPanel panelTable = new VerticalPanel();
		panelTable.add(cellTable);
		cellTable.setWidth("100%");
		dataProvider.addDataDisplay(cellTable);
		panel.add(panelTable);

	}

	private void updateReceipe(ReceipeModel receipe, String oldValue,
			String newValue, IngredientQuantity iq) {
		double oldQty = Double.valueOf(oldValue);
		double newQty = Double.valueOf(newValue);

		// calculate the ration for all QTY
		double ratio = newQty / oldQty;
		updateReceipe(receipe, ratio);
	}

	private void updateReceipe(ReceipeModel receipe, double ratio) {
		for (IngredientQuantity iq : receipe.getIngredientRPC()) {
			replaceQtyValue(iq, ratio);
		}
		receipe.setEnergie(receipe.getEnergie() * ratio);
		receipe.setNumberPersons((int) (receipe.getNumberPersons() * ratio));
		this.cellTable.redraw();
		updateForm(receipe);
	}

	public Receipe(final PageArguments args) {
		super(Receipe.class);
		// super.header = new Header();
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		this.args = args;
		link = args.getArgs()[0];
		linkReceipe.setText("Chargement ...");
		imageReceipe.setUrl("images/loadingImg.gif");
		server.getReceipe4RPC_ReadOnly(link, new AsyncCallback<ReceipeModel>() {

			@Override
			public void onFailure(Throwable caught) {
				// storeLastParams();
				String error = "Erreur durant le RPC [getNumberIngredients()]";
				PageArguments args1 = new PageArguments(Error.class);
				FlowControl.go(new Error(args1, error, caught, new Exception(
						error)), args1, false);
			}

			@Override
			public void onSuccess(final ReceipeModel receipe) {
				updateForm(receipe);
			}
		});
	}

	@Override
	public String getTitle() {
		return "Recette: " + link;
	};
}
