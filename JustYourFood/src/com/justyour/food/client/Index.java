/**
 * 
 */
package com.justyour.food.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.justyour.food.client.pages.Login;

/**
 * @author tonio
 * 
 */
public class Index implements EntryPoint, ValueChangeHandler<String> {

	protected static JYFServiceAsync server = null;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	@Override
	public void onModuleLoad() {
		Window.setTitle("Just Your Food");
//		Window.addResizeHandler(new ResizeHandler() {
//
//			@Override
//			public void onResize(ResizeEvent event) {
//				System.out.println("resize: " + event.getHeight() + ":"
//						+ event.getWidth());
//			}
//		});

//		Window.addWindowClosingHandler(new Window.ClosingHandler() {
//
//			@Override
//			public void onWindowClosing(ClosingEvent event) {
//				String msg = "You are close to leave Just Your Food";
//				event.setMessage(msg);
//				System.out.println("CLOSING WINDOW");
//			}
//		});

		History.addValueChangeHandler(this);
		if (History.getToken().isEmpty()) {
			PageArguments args = new PageArguments(Login.class);
			History.newItem(args.getURL());
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> e) {
		String token = History.getToken();
		FlowControl.goString(token);
	}

	static public JYFServiceAsync getServer() {
		return server;
	}

	public static boolean IsAdmin() {
		return false;
	}

	static {
		System.out.println("Create JustForYou RPC service");
		server = GWT.create(JYFService.class);
	}
}
