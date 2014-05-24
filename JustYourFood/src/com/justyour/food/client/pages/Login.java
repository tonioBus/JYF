/**
 * 
 */
package com.justyour.food.client.pages;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.CiqualOracle;
import com.justyour.food.client.FlowControl;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.ReceipeOracle;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.pages.Ciqual.RequestCiqual;
import com.justyour.food.client.pages.tools.WatermarkedSuggestBox;

/**
 * @author tonio
 * 
 */
public class Login extends RootPage {
	private JYFServiceAsync server = Index.getServer();
	private WatermarkedSuggestBox nameFieldCiqual;
	private WatermarkedSuggestBox nameFieldReceipe;
	CiqualOracle ciqualOracle = new CiqualOracle();
	ReceipeOracle receipeOracle = new ReceipeOracle();

	final HTML html = new HTML("");

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(Login.class);
	}

	protected Panel buildReceipeSearch() {
		// Receipes Search
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		nameFieldReceipe = new WatermarkedSuggestBox(receipeOracle,
				"Recettes (recherche rapide)");
		nameFieldReceipe.setTitle("Moteur de recherche de recettes");
		nameFieldReceipe.setWidth("25em");
		nameFieldReceipe.setLimit(RequestCiqual.MAX_SUGGESTIONS);
		nameFieldReceipe.getElement().setAttribute("type", "search");
		hPanel.getElement().setAttribute("style",
				"margin: 0 auto; text-align: center;");
		nameFieldReceipe.setAutoSelectEnabled(false);
		nameFieldReceipe.setFocus(true);
		hPanel.add(nameFieldReceipe);

		// Create a handler for the sendButton and nameField
		class AskReceipeHandler implements KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					final String text = nameFieldReceipe.getText();

					PageArguments args = new PageArguments(
							RequestReceipes.class, text);
					History.newItem(args.getURL(), true);

				}
			}

		}
		AskReceipeHandler askReceipeHandler = new AskReceipeHandler();
		nameFieldReceipe.addKeyUpHandler(askReceipeHandler);
		return hPanel;

	}

	protected Panel buildCiqualSearch() {
		// Ingredients Search
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		nameFieldCiqual = new WatermarkedSuggestBox(ciqualOracle,
				"Ingredients (recherche rapide)");
		nameFieldCiqual.setTitle("Moteur de recherche d'ingredients");
		nameFieldCiqual.setWidth("25em");
		nameFieldCiqual.setLimit(RequestCiqual.MAX_SUGGESTIONS);
		nameFieldCiqual.getElement().setAttribute("type", "search");
		hPanel.getElement().setAttribute("style",
				"margin: 0 auto; text-align: center;");
		nameFieldCiqual.setAutoSelectEnabled(false);
		nameFieldCiqual.setFocus(true);
		hPanel.add(nameFieldCiqual);

		// Create a handler for the sendButton and nameField
		class AskReceipeHandler implements KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					final String text = nameFieldCiqual.getText();

					PageArguments args = new PageArguments(RequestCiqual.class,
							text);
					History.newItem(args.getURL(), true);

				}
			}

		}
		AskReceipeHandler askReceipeHandler = new AskReceipeHandler();
		nameFieldCiqual.addKeyUpHandler(askReceipeHandler);
		return hPanel;
	}

	private class Body extends MyComposite {
		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);

			Grid searchPanel = new Grid(2, 2);

			searchPanel.setWidget(0, 0, new HTML("Recettes"));
			searchPanel.setWidget(0, 1, buildReceipeSearch());
			searchPanel.setWidget(1, 0, new HTML("Ingredients"));
			searchPanel.setWidget(1, 1, buildCiqualSearch());
			searchPanel.setStyleName("jyf-border", true);
			searchPanel.setHeight("100px");
			searchPanel.getElement().setAttribute("style",
					"margin: 0 auto; text-align: center;");
			panel.add(searchPanel);
			// Actualitées
			HTMLPanel flowPanel = new HTMLPanel("<h2>Actualités</h2>");
			flowPanel.setStyleName("jyf-panel", true);
			flowPanel.setStyleName("jyf-Text", true);
			flowPanel.getElement().setAttribute("style",
					"border:0; padding-left:5px; padding-right:5px;");
			flowPanel.add(html);
			flowPanel.setHeight("600px");
			panel.add(flowPanel);
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public Login(PageArguments args) {
		super(Login.class);
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		server.getTextFile("1_NEWS", new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				String error = "Erreur durant le RPC [getTextFile()]";
				PageArguments args1 = new PageArguments(Error.class);
				FlowControl.go(new Error(args1, error, caught, new Exception(
						error)), args1, false);
			}

			@Override
			public void onSuccess(String result) {
				html.setHTML(result);
			}

		});

	}

	@Override
	public void close() {
	}

	@Override
	public String getTitle() {
		return "Just Your Food - Accueil";
	}

}
