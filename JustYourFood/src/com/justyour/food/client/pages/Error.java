/**
 * 
 */
package com.justyour.food.client.pages;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.Index;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;

/**
 * @author tonio
 * 
 */
public class Error extends RootPage {

	// final TextArea textAreaCurrentTrace = new TextArea();
	// final TextArea textAreaException = new TextArea();
	VerticalPanel textAreaCurrentTrace = new VerticalPanel();
	VerticalPanel textAreaException = new VerticalPanel();
	Label errorlabel = new Label();
	String label;
	
	static public String getSimpleName() {
		return ToolsClient.getSimpleName(Error.class);
	}

	@Override
	public boolean isInitEnable() {
		return false;
	}

	private class Header extends MyComposite {
		public Header() {
			com.google.gwt.user.client.Window
					.setStatus("Désolé: une erreur sur JustYourFood a été détecté");
			VerticalPanel panel = new VerticalPanel();
			initHeader(panel);

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
					PageArguments args = new PageArguments(Login.class);
					History.newItem(args.getURL());
				}
			});
			Button expertInfoButton = new Button("Administration (Expert)");
			expertInfoButton.setStyleName("button");
			expertInfoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(ExpertInfo.class);
					History.newItem(args.getURL());
				}
			});
			Grid buttonGrid = new Grid(1, 3);
			buttonGrid.setStyleName("gwt-Menu", true);
			buttonGrid.setWidget(0, 0, returnButton);
			buttonGrid.setWidget(0, 1, loginButton);
			if (Index.IsAdmin())
				buttonGrid.setWidget(0, 1, expertInfoButton);
			buttonGrid.getCellFormatter().setHorizontalAlignment(0, 0,
					HasHorizontalAlignment.ALIGN_CENTER);
			buttonGrid.getCellFormatter().setVerticalAlignment(0, 0,
					HasVerticalAlignment.ALIGN_MIDDLE);
			panel.add(buttonGrid);
		}
	}

	public class Body extends MyComposite {
		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(15);
			HorizontalPanel errorPanel = new HorizontalPanel();
			errorPanel.setWidth("100%");
			VerticalPanel detailsPanel = new VerticalPanel();
			detailsPanel.setWidth("100%");
			TabPanel tp = new TabPanel();
			detailsPanel.setSpacing(10);
			tp.setWidth("100%");
			tp.add(errorPanel, "Désignation");
			tp.add(detailsPanel, "Détails");
			// Show the 'bar' tab initially.
			tp.selectTab(0);
			// Add it to the root panel.
			panel.add(tp);

			Button returnButton = new Button("Retour vers la page d'accueil");
			returnButton.setStyleName("button");
			returnButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(Login.class);
					History.newItem(args.getURL());
				}
			});
			panel.add(returnButton);

			// DESIGNATION
			Image img = new Image("icons/Warning.png");
			errorPanel.add(img);
			VerticalPanel panel1 = new VerticalPanel();
			HTML html01 = new HTML("Date: " + new Date());
			ToolsClient.setStyle(html01, "font-size", "1.2em");
			HTML html02 = new HTML(
					"Désolé, la précédente requête à échouer avec le libellé suivant:");
			ToolsClient.setStyle(html02, "font-size", "1.2em");
			HTML html03 = new HTML(
					"Ceci peut être dû à une interruption momentanée de nos servers.");
			ToolsClient.setStyle(html03, "font-size", "1.2em");
			ToolsClient.setStyle(errorlabel, "font-size", "1.2em");
			ToolsClient.setStyle(errorlabel, "background-color", "yellow");
			panel1.add(html01);
			panel1.add(html02);
			panel1.add(errorlabel);
			panel1.add(html03);
			errorPanel.add(panel1);

			// DETAILS
			VerticalPanel currentStackPanel = new VerticalPanel();
			HTML html1 = new HTML("Current Stack");
			ToolsClient.setStyle(html1, "font-size", "1.2em");
			ToolsClient.setStyle(html1, "text-decoration", "underline");
			currentStackPanel.add(html1);
			currentStackPanel.setStyleName("jyf-border", true);
			currentStackPanel.add(textAreaCurrentTrace);

			VerticalPanel exceptionStackPanel = new VerticalPanel();
			HTML html2 = new HTML("Exception");
			ToolsClient.setStyle(html2, "font-size", "1.2em");
			ToolsClient.setStyle(html2, "text-decoration", "underline");
			exceptionStackPanel.add(html2);
			exceptionStackPanel.setStyleName("jyf-border", true);
			exceptionStackPanel.add(textAreaException); //

			detailsPanel.add(currentStackPanel);
			detailsPanel.add(exceptionStackPanel);
		}
	}

	public Error(PageArguments args, String label, Throwable t, Exception eLine) {
		super(Error.class);
		super.header = new Header();
		super.body = new Body();
		init(args, label, t, eLine);
	}

	public void init(PageArguments args, String label, Throwable t,
			Exception eLine) {
		// current trace
		this.label = label;
		if (t != null) {
			StackTraceElement st[] = t.getStackTrace();
			for (StackTraceElement stackTraceElement : st) {
				String line = stackTraceElement.toString();
				HTML html = new HTML(line);
				if (line.trim().startsWith("com.justyour")) {
					ToolsClient.setStyle(html, "color", "white");
					ToolsClient.setStyle(html, "background-color", "red");
				}
				textAreaCurrentTrace.add(html);
			}
		} else {
			textAreaCurrentTrace.add(new HTML("No Exception detected"));
		}

		// Exception
		if (eLine != null) {
			StackTraceElement st[] = eLine.getStackTrace();
			int lineNumber = st[0].getLineNumber();
			String filename = st[0].getFileName();
			String method = st[0].getMethodName();
			label = "File:" + filename + " method:" + method + " line:"
					+ lineNumber + " " + label;
			for (StackTraceElement stackTraceElement : st) {
				String line = stackTraceElement.toString();
				HTML html = new HTML(line);
				if (line.trim().startsWith("com.justyour")) {
					ToolsClient.setStyle(html, "color", "white");
					ToolsClient.setStyle(html, "background-color", "red");
				}
				textAreaException.add(html);
			}
			errorlabel.setText(label);
		} else {
			textAreaException.add(new HTML("No Exception detected"));
			errorlabel.setText(label);
		}
	}

	@Override
	public void init(PageArguments args) {
		textAreaException.clear();
		init(args, "Re-initialization d'erreur", new Exception("Error Page"),
				null);
	}

	@Override
	public String getTitle() {
		return "Just Your Food: Error: "+this.label;
	}

}
