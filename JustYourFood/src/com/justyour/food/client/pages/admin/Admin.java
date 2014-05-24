/**
 * 
 */
package com.justyour.food.client.pages.admin;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.ToolsClient.JYFDialog;
import com.justyour.food.client.pages.Login;
import com.justyour.food.shared.AdminInfos;
import com.justyour.food.shared.DumperInfos;

/**
 * @author tonio
 * 
 */
public class Admin extends RootPage {
	private JYFServiceAsync server = Index.getServer();
	VerticalPanel crawlerPanel = new VerticalPanel();
	VerticalPanel dbReceipesPanel = new VerticalPanel();
	VerticalPanel dbCiqualPanel = new VerticalPanel();

	// private HashMap<String, CrawlerParamPanel> providerPanels = new
	// HashMap<>();
	@SuppressWarnings("rawtypes")
	private HashMap providerPanels = new HashMap();

	class CrawlerParamPanel extends VerticalPanel {
		protected Grid paramPanel = new Grid(2, 8);
		protected String className;
		protected String defaultUrl;
		VerticalPanel log;
		Button startButton;
		Button stopButton;
		private boolean emptyLog = false;
		private TextBox addedReceipes;
		private TextBox visitedPages;
		Image loading = new Image();
		private boolean isRunning = false;

		VerticalPanel getLog() {
			return log;
		}

		public void running() {
			this.startButton.setEnabled(false);
			this.stopButton.setEnabled(true);
			if (isRunning == false) {
				loading.setUrl("images/pleasewait.gif");
				isRunning = true;
			}
		}

		public void notRunning() {
			this.startButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			if (isRunning == true) {
				loading.setUrl("images/nothing.gif");
				isRunning = false;
				ToolsClient.showDialog("Crawler stopped", "The crawler ["
						+ className + "] just finished");
			}
		}

		public void refresh(DumperInfos dumperInfos[]) {
			System.out.println("REFRESH(" + this.className + ")");
			for (DumperInfos dumperInfo : dumperInfos) {
				System.out.println("DumperInfos(" + dumperInfo.getDumperClass()
						+ "):" + dumperInfo.isFinish());
				if (dumperInfo.getDumperClass().equals(this.className)) {
					// we are in the good dumperinfo
					if (dumperInfo.isFinish())
						this.notRunning();
					else
						this.running();
					return;
				}
			}
			this.notRunning();
		}

		public CrawlerParamPanel(String classNameP, String defaultUrlP) {
			this.className = classNameP;
			this.defaultUrl = defaultUrlP;
			/*
			 * void com.justyour.food.client.JYFServiceAsync.startCrawl( String
			 * className, String url, int politnessDelay, int numberCrawls, int
			 * maxDepthCrawling, int maxPagesToFetch, int maxAddition, int
			 * maxVisiting, AsyncCallback<Void> callback)
			 */
			paramPanel.setWidget(0, 0, new HTML("Dumper"));
			paramPanel.setWidget(0, 1, new HTML("Web Address"));
			paramPanel.setWidget(0, 2, new HTML("Polit Time"));
			paramPanel.setWidget(0, 3, new HTML("# crawls"));
			paramPanel.setWidget(0, 4, new HTML("Max Depth"));
			paramPanel.setWidget(0, 5, new HTML("Max Fetch"));
			paramPanel.setWidget(0, 6, new HTML("Max # Addition"));
			paramPanel.setWidget(0, 7, new HTML("Max # Visit"));

			// DUMPER
			TextBox dumper = new TextBox();
			paramPanel.setWidget(1, 0, dumper);
			dumper.getElement().setAttribute("value", className);
			dumper.setWidth("100%");

			// WEB ADDRESS
			TextBox webAddress = new TextBox();
			webAddress.getElement().setAttribute("type", "url");
			webAddress.getElement().setAttribute("value", defaultUrl);
			webAddress.setWidth("100%");
			paramPanel.setWidget(1, 1, webAddress);

			// POLIT TIME
			TextBox politTime = new TextBox();
			politTime.getElement().setAttribute("type", "number");
			politTime.getElement().setAttribute("min", "100");
			politTime.getElement().setAttribute("max", "10000");
			politTime.getElement().setAttribute("value", "1000");
			politTime.getElement().setAttribute("step", "100");
			paramPanel.setWidget(1, 2, politTime);

			// NUMBER CRAWLS
			TextBox numberCrawls = new TextBox();
			numberCrawls.getElement().setAttribute("type", "number");
			numberCrawls.getElement().setAttribute("min", "1");
			numberCrawls.getElement().setAttribute("max", "100");
			numberCrawls.getElement().setAttribute("value", "1");
			numberCrawls.getElement().setAttribute("step", "1");
			paramPanel.setWidget(1, 3, numberCrawls);

			// MAX DEPTH
			TextBox maxDepth = new TextBox();
			maxDepth.getElement().setAttribute("type", "number");
			maxDepth.getElement().setAttribute("min", "1");
			maxDepth.getElement().setAttribute("max", "20");
			maxDepth.getElement().setAttribute("value", "3");
			maxDepth.getElement().setAttribute("step", "1");
			paramPanel.setWidget(1, 4, maxDepth);

			// MAX FETCH
			TextBox maxFetch = new TextBox();
			maxFetch.getElement().setAttribute("type", "number");
			maxFetch.getElement().setAttribute("min", "1");
			maxFetch.getElement().setAttribute("max", "100000");
			maxFetch.getElement().setAttribute("value", "-1");
			maxFetch.getElement().setAttribute("step", "100");
			paramPanel.setWidget(1, 5, maxFetch);

			// MAX ADDITION
			TextBox maxAddtion = new TextBox();
			maxAddtion.getElement().setAttribute("type", "number");
			maxAddtion.getElement().setAttribute("min", "-1");
			maxAddtion.getElement().setAttribute("max", "100000");
			maxAddtion.getElement().setAttribute("value", "1");
			maxAddtion.getElement().setAttribute("step", "100");
			paramPanel.setWidget(1, 6, maxAddtion);

			// MAX VISIT
			TextBox maxVisit = new TextBox();
			maxVisit.getElement().setAttribute("type", "number");
			maxVisit.getElement().setAttribute("min", "-1");
			maxVisit.getElement().setAttribute("max", "1000000");
			maxVisit.getElement().setAttribute("value", "-1");
			paramPanel.setWidget(1, 7, maxVisit);

			paramPanel.setStyleName("jyf-grid", true);
			paramPanel.setStyleName("jyf-border", true);
			String size = "100px";
			for (int i = 0; i < 8; i++) {
				paramPanel.getCellFormatter().setWidth(0, i,
						(i < 2 ? "400px" : size));
				paramPanel.getCellFormatter().setWidth(1, i,
						(i < 2 ? "400px" : size));
				paramPanel.getCellFormatter().setHorizontalAlignment(0, i,
						HasHorizontalAlignment.ALIGN_CENTER);
			}
			this.setStyleName("jyf-panel", true);

			// Provider Panel
			this.add(paramPanel);

			startButton = new Button("Launch");
			startButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					stopButton.setEnabled(false);
					startButton.setEnabled(false);
					server.startCrawl(getValue(0), getValue(1),
							Integer.parseInt(getValue(2)),
							Integer.parseInt(getValue(3)),
							Integer.parseInt(getValue(4)),
							Integer.parseInt(getValue(5)),
							Integer.parseInt(getValue(6)),
							Integer.parseInt(getValue(7)),
							new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
								}

								@Override
								public void onSuccess(Void result) {
									refreshPage();
								}
							});
				}
			});
			stopButton = new Button("stop");
			stopButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					stopButton.setEnabled(false);
					startButton.setEnabled(false);
					server.stopCrawl(className, new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							refreshPage();
						}

						@Override
						public void onFailure(Throwable caught) {
						}
					});
				}
			});
			//
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(10);
			hp.setStyleName("jyf-border", true);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.add(startButton);
			hp.add(stopButton);
			loading.setWidth("10em");
			loading.setHeight("1em");
			hp.add(loading);

			// ADDED RECEIPES
			HorizontalPanel hp2 = new HorizontalPanel();
			hp2.setSpacing(2);
			HTML label1 = new HTML("Added Receipes");
			label1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			hp2.add(label1);
			addedReceipes = new TextBox();
			addedReceipes.setReadOnly(true);
			hp2.add(addedReceipes);
			hp.add(hp2);

			// VISITED PAGES
			HorizontalPanel hp3 = new HorizontalPanel();
			hp3.setSpacing(2);
			HTML label2 = new HTML("Visited Pages");
			label2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			hp3.add(label2);
			visitedPages = new TextBox();
			visitedPages.setReadOnly(true);
			hp3.add(visitedPages);
			hp.add(hp3);

			this.add(hp);
			log = new VerticalPanel();
			this.add(log);
			log.setStyleName("jyf-logs", true);
		}

		public Grid getPanel() {
			return paramPanel;
		}

		public String getValue(int index) {
			TextBox textBox = (TextBox) paramPanel.getWidget(1, index);
			return textBox.getValue();
		}

		public boolean getEmptyLog() {
			return this.emptyLog;
		}

		public void setEmptyLog(boolean emptyLog) {
			this.emptyLog = emptyLog;
		}
	}

	protected void initDBCiqual() {

	}

	protected void initCrawlerPanel() {

		crawlerPanel.clear();
		crawlerPanel.setSpacing(15);
		server.getDumperPlugins(new AsyncCallback<HashMap<String, String>>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(HashMap<String, String> result) {
				for (String clazz : result.keySet()) {
					String url = result.get(clazz);
					CrawlerParamPanel crawlerParamPanel = new CrawlerParamPanel(
							clazz, url);
					providerPanels.put(clazz, crawlerParamPanel);
					crawlerPanel.add(crawlerParamPanel);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(Admin.class);
	}

	@Override
	public boolean isAdmin() {
		return true;
	}

	public void startTimer() {
		if (timer != null)
			timer.schedule(0);
	}

	public void stopTimer() {
		if (timer != null)
			timer.cancel();
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

			hPanel.setStyleName("gwt-Menu", true);
			hPanel.add(returnButton);
			hPanel.add(loginButton);
			if (Index.IsAdmin())
				hPanel.add(expertInfoButton);
			hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		}
	};

	protected synchronized void refreshPage() {
		server.pullAdminInfo(new AsyncCallback<AdminInfos>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				GWT.log("Erreur dans pullAdminInfo", caught);
			}

			@Override
			public void onSuccess(AdminInfos result) {
				dbReceipes.setText(result.numberReceipesFromDB + "");
				solrReceipes.setText(result.numberReceipesFromSolr + "");
				for (DumperInfos dumperInfos : result.dumperInfos) {
					CrawlerParamPanel crawlerParamPanel = (CrawlerParamPanel) providerPanels
							.get(dumperInfos.getDumperClass());
					VerticalPanel log = crawlerParamPanel.getLog();
					if (dumperInfos.getLogs().length == 0) {
						if (crawlerParamPanel.getEmptyLog() == false) {
							HTML line = new HTML(new Date()
									+ " : no log to report");
							log.add(line);
							crawlerParamPanel.setEmptyLog(true);
						}
					} else {
						crawlerParamPanel.setEmptyLog(false);
						for (String string : dumperInfos.getLogs()) {
							HTML line = new HTML();
							line.setText(string);
							log.add(line);
						}
					}
					crawlerParamPanel.addedReceipes.setText(""
							+ dumperInfos.getAdded());
					crawlerParamPanel.visitedPages.setText(""
							+ dumperInfos.getVisited());
				}
				for (Object crawlerParam : providerPanels.values()) {
					((CrawlerParamPanel) crawlerParam)
							.refresh(result.dumperInfos);
				}
			}
		});
	}

	Timer timer = new Timer() {
		@Override
		public void run() {
			System.out.println("Timer.run()");
			refreshPage();
			timer.schedule(5000);
		}
	};

	private TextBox dbReceipes;
	private TextBox solrReceipes;
	private TextBox solrCiqual;
	private TextBox dbCiqual;

	public void createCrawlerPanel(VerticalPanel mainCrawlerPanel) {
		CheckBox refreshButton = new CheckBox("Auto-refresh", false);
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				if (checked) {
					startTimer();
				} else {
					stopTimer();
				}
				System.out.println("checked[" + checked + "]");
			}
		});
		refreshButton.setValue(false, true);

		Button updateButton = new Button("Update");
		updateButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				refreshPage();
			}
		});

		Grid headerPanel = new Grid(1, 8);
		headerPanel.setBorderWidth(2);
		headerPanel.setCellPadding(5);
		headerPanel.setCellSpacing(5);
		mainCrawlerPanel.add(headerPanel);
		mainCrawlerPanel.add(crawlerPanel);
		// crawlerPanel.add(headerPanel);

		int index = 0;
		// Refresh
		headerPanel.setWidget(0, index++, refreshButton);

		// Update
		headerPanel.setWidget(0, index++, updateButton);

		// button reload
		Button reloadButton = new Button("Reload Parameters");
		reloadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final JYFDialog dialog = ToolsClient.showProcessDialog(
						"Reload Parameters", "Reload in progress ...");
				server.reloadParam(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						initCrawlerPanel();
						dialog.hide();
						ToolsClient.showDialog("Reload Parameters",
								"Error occurred: " + caught);
					}

					@Override
					public void onSuccess(Void result) {
						initCrawlerPanel();
						ToolsClient.endingProcessDialog(dialog, null);
					}

				});
			}
		});
		headerPanel.setWidget(0, index++, reloadButton);

		// button reinit
		Button reinitButton = new Button("Reinit Context");
		reinitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final JYFDialog dialog = ToolsClient.showProcessDialog(
						"Reinit Serlet Context",
						"Initialization in progress ...");
				server.reinitJYFSerletContext(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						initCrawlerPanel();
						dialog.hide();
						ToolsClient.showDialog("Reinit SerletContext",
								"Error occurred: " + caught);
					}

					@Override
					public void onSuccess(Void result) {
						initCrawlerPanel();
						ToolsClient.endingProcessDialog(dialog, null);
					}

				});
			}
		});
		headerPanel.setWidget(0, index++, reinitButton);

		// button reinit
		Button cleaningSolrFromDBButton = new Button("Cleaning SOLR <- DB");
		cleaningSolrFromDBButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final JYFDialog dialog = ToolsClient.showProcessDialog(
						"Cleaning SOLR from the DB", "In progress ...");
				server.cleanSolrFromDB(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						dialog.hide();
						refreshPage();
						GWT.log("Error in cleanSolrFromDB", caught);
						ToolsClient.showDialog("Cleaning SOLR <- DB",
								"Error occurred: " + caught);
					}

					@Override
					public void onSuccess(String result) {
						refreshPage();
						ToolsClient.endingProcessDialog(dialog, result);
					}

				});
			}
		});
		headerPanel.setWidget(0, index++, cleaningSolrFromDBButton);

		// button reinit
		Button fullUpdateSolrFromDBButton = new Button("Sync DB <- SOLR");
		fullUpdateSolrFromDBButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final JYFDialog dialog = ToolsClient.showProcessDialog(
						"Synchronization of SOLR from the DB",
						"In progress ...");
				server.updateSOLRFromDB(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						dialog.hide();
						refreshPage();
						ToolsClient.showDialog(
								"Synchronization of SOLR from the DB",
								"Error occurred: " + caught);
					}

					@Override
					public void onSuccess(String result) {
						refreshPage();
						ToolsClient.endingProcessDialog(dialog, result);
					}

				});
			}
		});
		headerPanel.setWidget(0, index++, fullUpdateSolrFromDBButton);

		// SolrReceipe # receipes
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		HTML solrNumber = new HTML("SOLR receipes");
		solrNumber.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp1.add(solrNumber);
		solrReceipes = new TextBox();
		solrReceipes.setReadOnly(true);
		hp1.add(solrReceipes);
		headerPanel.setWidget(0, index++, hp1);

		// SolrReceipe # receipes
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		HTML dbNumber = new HTML("DB receipes");
		dbNumber.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp2.add(dbNumber);
		dbReceipes = new TextBox();
		dbReceipes.setReadOnly(true);
		hp2.add(dbReceipes);
		headerPanel.setWidget(0, index++, hp2);

	}

	void updateDBManagement() {
		server.getNumberDBCiqual(new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				dbCiqual.setText("E: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Long result) {
				dbCiqual.setText(result + "");
			}

		});
		server.getNumberSolrCiqual(new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				solrCiqual.setText("E: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Long result) {
				solrCiqual.setText(result + "");
			}

		});
	}

	VerticalPanel createDBManagementPanel() {
		VerticalPanel returnPanel = new VerticalPanel();
		// button reinit
		final TextBox filenameTB = new TextBox();
		filenameTB.setWidth("500px");
		returnPanel.add(filenameTB);
		server.getDeployDir(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				filenameTB.setText(result + "/data/Ciqual_2012_v02.07.csv");
			}

			@Override
			public void onFailure(Throwable caught) {
				filenameTB.setText("data/Ciqual_2012_v02.07.csv");
			}
		});
		Button updateCiqualDBFromFile = new Button("Ciqual CSV -> DB");
		updateCiqualDBFromFile.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final JYFDialog dialog = ToolsClient.showProcessDialog(
						"Loading CSV file: into DB", "In progress ...");
				final String filename = filenameTB.getText();
				server.updateCiqualDB(filename, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						dialog.hide();
						refreshPage();
						updateDBManagement();
						ToolsClient.showDialog(
								"Synchronization of the Ciqual CSV using ["
										+ filename + "]", "Error occurred: "
										+ caught);
					}

					@Override
					public void onSuccess(String result) {
						refreshPage();
						ToolsClient.endingProcessDialog(dialog, result);
						updateDBManagement();
					}

				});
			}
		});
		returnPanel.add(updateCiqualDBFromFile);
		// SolrReceipe # receipes
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		HTML solrNumber = new HTML("SOLR ciqual: ");
		solrNumber.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp1.add(solrNumber);
		solrCiqual = new TextBox();
		solrCiqual.setReadOnly(true);
		hp1.add(solrCiqual);
		returnPanel.add(hp1);

		// SolrReceipe # receipes
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		HTML dbNumber = new HTML("DB ciqual: ");
		dbNumber.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp2.add(dbNumber);
		dbCiqual = new TextBox();
		dbCiqual.setReadOnly(true);
		hp2.add(dbCiqual);
		returnPanel.add(hp2);

		return returnPanel;
	}

	void createDBCiqualPanel() {
	}

	private class Body extends MyComposite {

		public Body() {
			VerticalPanel panel = new VerticalPanel();
			VerticalPanel mainCrawlerPanel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(5);
			TabPanel tp = new TabPanel();
			panel.add(tp);
			tp.setWidth("100%");
			createCrawlerPanel(mainCrawlerPanel);
			tp.add(mainCrawlerPanel, "Crawler");
			VerticalPanel receipeManagement = createDBManagementPanel();
			tp.add(receipeManagement, "Receipe I/O");
			createDBCiqualPanel();
			tp.add(dbCiqualPanel, "DB Ciqual");
			tp.selectTab(0);
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public Admin(PageArguments args) {
		super(Admin.class);
		super.header = new Header();
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		initCrawlerPanel();
		stopTimer();
		refreshPage();
		updateDBManagement();
	}

	@Override
	public void close() {
		stopTimer();
	}

	@Override
	public String getTitle() {
		return "Admin panel";
	}

}
