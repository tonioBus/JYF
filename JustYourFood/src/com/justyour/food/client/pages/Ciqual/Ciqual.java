/**
 * 
 */
package com.justyour.food.client.pages.Ciqual;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
public class Ciqual extends RootPage {
	private JYFServiceAsync server = Index.getServer();
	CiqualDetails ciqualDetails;
	CiqualOracle ciqualOracle = new CiqualOracle();
	private WatermarkedSuggestBox nameFieldCiqual;
	private String text;

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(Ciqual.class);
	}

	private class Body extends MyComposite {

		public Body() {
			final VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(2);
			panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			HTML title = new HTML("<h2>Ingredients</h2>");
			title.getElement().setAttribute("style",
					"margin: 0 auto; text-align: center; padding:5px;");
			panel.add(title);
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			nameFieldCiqual = new WatermarkedSuggestBox(ciqualOracle,
					"Ingredients (recherche rapide)");
			nameFieldCiqual.setTitle("Moteur de recherche d'ingredients");
			nameFieldCiqual.setWidth("25em");
			nameFieldCiqual.setLimit(RequestCiqual.MAX_SUGGESTIONS);
			nameFieldCiqual.getElement().setAttribute("type", "search");
			hPanel.getElement().setAttribute("style",
					"margin: 0 auto; text-align: center; padding:5px;");
			nameFieldCiqual.setAutoSelectEnabled(false);
			nameFieldCiqual.setFocus(true);
			hPanel.add(nameFieldCiqual);
			hPanel.setStyleName("jyf-border", true);
			panel.add(hPanel);

			TabPanel tabPanel = new TabPanel();
			tabPanel.setWidth("100%");
			ciqualDetails = new CiqualDetails();
			HTML ciqualDetailsTab = new HTML("Détails Nutritionnels");
			ciqualDetailsTab.setTitle("Valeurs nutritionnelles détaillées");
			tabPanel.add(ciqualDetails, ciqualDetailsTab);
			tabPanel.selectTab(0);
			panel.add(tabPanel);

		}
	}

	public Ciqual(final PageArguments args) {
		super(Ciqual.class);
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		text = "";
		if (args.getArgs().length > 0) {
			text = args.getArgs()[0].trim();
		}
		nameFieldCiqual.setValue(text);

		final String ciqualSz = text;
		server.getCiqual(ciqualSz, new AsyncCallback<CiqualModel>() {

			@Override
			public void onSuccess(CiqualModel ciqual) {
				ciqualDetails.update(ciqual);
			}

			@Override
			public void onFailure(Throwable caught) {
				String error = "Erreur durant le RPC [getCiqual(" + ciqualSz
						+ ")]";
				PageArguments args1 = new PageArguments(Error.class);
				FlowControl.go(new Error(args1, error, caught, new Exception(
						error)), args1, false);
			}
		});
	}

	@Override
	public String getTitle() {
		return "Ingredient - nutrition: "+text;
	}
}
