/**
 * 
 */
package com.justyour.food.client.pages;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.FlowControl;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;

/**
 * @author tonio
 * 
 */
public class Text extends RootPage {

	private JYFServiceAsync server = Index.getServer();

	final HTML html = new HTML("");

	private String file;

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(Text.class);
	}

	private class Body extends MyComposite {
		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			HTMLPanel flowPanel = new HTMLPanel("");
			flowPanel.setStyleName("jyf-panel", true);
			flowPanel.setStyleName("jyf-Text", true);
			flowPanel
					.getElement()
					.setAttribute(
							"style",
							"font-size: 0.8em; padding-left:5px; padding-right:5px; font-size:1.4em; border:0;");
			flowPanel.add(html);
			panel.add(flowPanel);
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public Text(PageArguments args) {
		super(Text.class);
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		file = "";
		if (args.getArgs().length > 0) {
			file = args.getArgs()[0].trim();
		}
		server.getTextFile(file, new AsyncCallback<String>() {

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
	public String getTitle() {
		return "Texte: " + file;
	}
}
