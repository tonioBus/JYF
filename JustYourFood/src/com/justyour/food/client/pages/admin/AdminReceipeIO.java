/**
 * 
 */
package com.justyour.food.client.pages.admin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.pages.Login;

/**
 * @author tonio
 * 
 */
public class AdminReceipeIO extends RootPage {
	private JYFServiceAsync server = Index.getServer();

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(AdminReceipeIO.class);
	}

	private class Header extends MyComposite {
		public Header() {
			HorizontalPanel hPanel = new HorizontalPanel();
			initHeader(hPanel);
			hPanel.setSpacing(5);

			Button returnButton = new Button("Retour");
			returnButton.setStyleName("button");
			returnButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					History.back();
				}
			});

			Button loginButton = new Button("Accueil");
			loginButton.setStyleName("button");
			loginButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args1 = new PageArguments(Login.class);
					History.newItem(args1.getURL());
				}
			});

			Button expertInfoButton = new Button("Administration (Expert)");
			expertInfoButton.setStyleName("button");
			expertInfoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(AdminInfo.class);
					History.newItem(args.getURL());
				}
			});

			hPanel.add(returnButton);
			hPanel.add(loginButton);
			if (Index.IsAdmin())
				hPanel.add(expertInfoButton);
			hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		}
	};

	private class Body extends MyComposite {
		TextBox link = new TextBox();
		TextBox filenameTB = new TextBox();
		TextBox filenameTB1 = new TextBox();

		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(5);

			HorizontalPanel hp = new HorizontalPanel();
			hp.setBorderWidth(5);
			panel.add(hp);
			Button dumpReceipe = new Button("Dump Receipe");
			link.getElement().setAttribute("type", "url");
			link.setText("http://recettes.doctissimo.fr/salade-de-poulet-facon-thai.htm");
			filenameTB.getElement().setAttribute("type", "url");
			filenameTB.getElement().setAttribute("style", "height: 100%;");
			filenameTB
					.setText("/home/tonio/Dev/git/MetaReceipe/JustYourFood/war/data/currentDump.xml");
			hp.add(filenameTB);
			hp.add(link);
			hp.add(dumpReceipe);
			dumpReceipe.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					server.dumpReceipe(
							link.getText(),
							filenameTB.getText(),
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									System.out.println("Erreur: ");
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Void result) {
									System.out.println("Success");
								}
							});
				}
			});

			HorizontalPanel hp1 = new HorizontalPanel();
			hp1.setBorderWidth(5);
			panel.add(hp1);
			Button loadReceipe = new Button("Load Receipe");
			filenameTB1.getElement().setAttribute("type", "url");
			filenameTB1.getElement().setAttribute("style", "height: 100%;");
			filenameTB1
					.setText("/home/tonio/Dev/git/MetaReceipe/JustYourFood/war/data/receipe1.xml");
			hp1.add(filenameTB1);
			hp1.add(loadReceipe);
			dumpReceipe.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					server.loadReceipe(filenameTB1.getText(),
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									System.out.println("Erreur: ");
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Void result) {
									System.out.println("Success");
								}

							});

				}
			});

		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public AdminReceipeIO(PageArguments args) {
		super(AdminReceipeIO.class);
		super.header = new Header();
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
	}

	@Override
	public String getTitle() {
		return "Admin recettes I/O";
	}

}
