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
public class News extends RootPage {

	private JYFServiceAsync server = Index.getServer();

	final HTML html = new HTML("");

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(News.class);
	}

	private class Body extends MyComposite {
		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			HTMLPanel flowPanel = new HTMLPanel("");
			flowPanel.setStyleName("jyf-panel", true);
			flowPanel.setStyleName("jyf-Text", true);
			flowPanel.getElement().setAttribute("style",
					"font-size: 0.8em; border:0; padding-left:5px; padding-right:5px; font-size:1.6em; font-family:'BradhITC'");
			flowPanel.add(html);
			panel.add(flowPanel);
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public News(PageArguments args) {
		super(News.class);
		// super.header = new Header();
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
		String month = "";
		String day = "";
		if (args.getArgs().length > 1) {
			month = args.getArgs()[0].trim();
			day = args.getArgs()[1].trim();
		}
		server.getNewsFile(month, day, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				String error = "Erreur durant le RPC [getNewsFile()]";
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
		return "Just Your Food - News";
	}
}
