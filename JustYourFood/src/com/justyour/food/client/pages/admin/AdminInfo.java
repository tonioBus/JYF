/**
 * 
 */
package com.justyour.food.client.pages.admin;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.FlowControl;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.pages.Error;
import com.justyour.food.client.pages.Login;

/**
 * @author tonio
 * 
 */
public class AdminInfo extends RootPage {

	public static String getSimpleName() {
		return ToolsClient.getSimpleName(AdminInfo.class);
	}
	private JYFServiceAsync server = Index.getServer();

	private class Header extends MyComposite {

		public Header() {
			HorizontalPanel hPanel = new HorizontalPanel();
			initHeader(hPanel);
			hPanel.setSpacing(5);

			Button backButton = new Button("Retour");
			backButton.setStyleName("button");
			backButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// FlowControl.go(new Login(), true);
					History.back();
				}
			});

			Button expertInfoButton = new Button("DB Admin (Expert)");
			expertInfoButton.setStyleName("button");
			expertInfoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(Admin.class);
					History.newItem(args.getURL());
				}
			});

			Button loginButton = new Button("Accueil");
			loginButton.setStyleName("button");
			loginButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(Login.class);
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

		private void initLabel(String labelSz, Panel panel) {
			Label label = new Label(labelSz);
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			ToolsClient.setStyle(label, "font-size", "1.4em");
			ToolsClient.setStyle(label, "font-weight", "bold");
			panel.add(label);
		}

		public Body() {
			final VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(1);
			panel.setStyleName("gwt-Section");

			initLabel("Servlet Properties", panel);
			final FlexTable servPropTable = new FlexTable();
			servPropTable.setStyleName("jyf-FlexTable");
			panel.add(servPropTable);

			// Properties
			initLabel("Properties", panel);
			final FlexTable propTable = new FlexTable();
			propTable.setStyleName("jyf-FlexTable");
			panel.add(propTable);

			// Environments
			initLabel("Environment", panel);
			final FlexTable envTable = new FlexTable();
			envTable.setStyleName("jyf-FlexTable");
			panel.add(envTable);

			// SolrReceipe
			initLabel("SolrReceipe Properties", panel);
			final FlexTable solrTable = new FlexTable();
			solrTable.setStyleName("jyf-FlexTable");
			panel.add(solrTable);

			// Deployed Files
			initLabel("Deployed File(s)", panel);
			final Tree t = new Tree();
			panel.add(t);

			server.getServletProperties(new AsyncCallback<ArrayList<String>>() {

				@Override
				public void onSuccess(ArrayList<String> list) {
					int i = 0;

					while (i < (list.size() / 2)) {
						servPropTable.setWidget(i, 0,
								new Label(list.get(2 * i)));
						servPropTable.setWidget(i, 1,
								new Label(list.get((2 * i) + 1)));
						i++;
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					PageArguments args = new PageArguments(Error.class);
					FlowControl.go(new Error(args,
							"Erreur durant le RPC [getServletProperties]",
							caught, new Exception()), args, false);
				}
			});

			server.getServerProperties(new AsyncCallback<HashMap<String, String>>() {

				@Override
				public void onSuccess(HashMap<String, String> hash) {
					int i = 0;
					for (String key : hash.keySet()) {
						propTable.setWidget(i, 0, new Label(key));
						propTable.setWidget(i, 1, new Label(hash.get(key)));
						i++;
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					PageArguments args = new PageArguments(Error.class);
					FlowControl.go(new Error(args,
							"Erreur durant le RPC [getServerProperties]",
							caught, new Exception()), args, false);
				}
			});

			server.getServerEnv(new AsyncCallback<HashMap<String, String>>() {

				@Override
				public void onSuccess(HashMap<String, String> hash) {
					int i = 0;
					for (String key : hash.keySet()) {
						envTable.setWidget(i, 0, new Label(key));
						envTable.setWidget(i, 1, new Label(hash.get(key)));
						i++;
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					PageArguments args = new PageArguments(Error.class);
					FlowControl.go(new Error(args,
							"Erreur durant le RPC [getServletEnv]", caught,
							new Exception()), args, false);
				}
			});

			server.getSolrProperties(new AsyncCallback<ArrayList<String>>() {

				@Override
				public void onSuccess(ArrayList<String> list) {
					int i = 0;

					while (i < (list.size() / 2)) {
						solrTable.setWidget(i, 0, new Label(list.get(2 * i)));
						solrTable.setWidget(i, 1,
								new Label(list.get((2 * i) + 1)));
						i++;
					}
//					solrTable.setWidget(i, 0, new Label("SolrReceipe Server"));
//					solrTable.setWidget(i, 1, new Anchor(JYFServletContext.getParam().getSolrReceipe(),
//							false, JYFServletContext.getParam().getSolrReceipe(), "_blank"));
				}

				@Override
				public void onFailure(Throwable caught) {
					PageArguments args = new PageArguments(Error.class);
					FlowControl.go(new Error(args,
							"Erreur durant le RPC [getSolrProperties]", caught,
							new Exception()), args, false);
				}
			});

			server.getServerDeployFiles(new AsyncCallback<HashMap<String, Object>>() {

				@Override
				public void onSuccess(HashMap<String, Object> hash) {
					final TreeItem root = new TreeItem(new Label(
							"Deployed Files"));
					convertTree(hash, root);
					t.addItem(root);
					root.setState(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					PageArguments args = new PageArguments(Error.class);
					FlowControl.go(new Error(args,
							"Erreur durant le RPC [getServerDeployFiles]",
							caught, new Exception()), args, false);
				}
			});
			setStyleName("body");
		}
	}

	@SuppressWarnings("unchecked")
	protected void convertTree(HashMap<String, Object> hash, TreeItem root) {
		for (String key : hash.keySet()) {
			Object value = hash.get(key);
			if (value instanceof HashMap<?, ?>) {
				TreeItem node = root.addTextItem(key);
				convertTree((HashMap<String, Object>) value, node);
			} else {
				root.addTextItem(key);
				root.setTitle(value.toString());
			}
		}
	}

	public AdminInfo(PageArguments args) {
		super(AdminInfo.class);
		super.header = new Header();
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
	}

	@Override
	public String getTitle() {
		return "Admin Info";
	}

}
