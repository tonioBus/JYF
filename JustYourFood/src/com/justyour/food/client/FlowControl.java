/**
 * 
 */
package com.justyour.food.client;

import java.util.HashMap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.justyour.food.client.pages.Error;
import com.justyour.food.client.pages.ExpertInfo;
import com.justyour.food.client.pages.Login;
import com.justyour.food.client.pages.News;
import com.justyour.food.client.pages.Receipe;
import com.justyour.food.client.pages.RequestReceipes;
import com.justyour.food.client.pages.Test_tmp;
import com.justyour.food.client.pages.Text;
import com.justyour.food.client.pages.Ciqual.Ciqual;
import com.justyour.food.client.pages.Ciqual.RequestCiqual;
import com.justyour.food.client.pages.admin.Admin;
import com.justyour.food.client.pages.admin.AdminReceipe;
import com.justyour.food.client.pages.admin.AdminReceipeIO;
import com.justyour.food.client.pages.admin.AdminRequestReceipes;

/**
 * @author tonio
 * 
 */
public class FlowControl {

	@SuppressWarnings("rawtypes")
	protected static HashMap pageCache = new HashMap();

	public static void showWaitCursor() {
		RootPanel.getBodyElement().setAttribute("cursor", "wait");
	}

	public static void showDefaultCursor() {
		RootPanel.getBodyElement().setAttribute("cursor", "default");
	}

	private FlowControl() {
	}

	public static void close() {
		for (Object page : pageCache.values()) {
			((RootPage) page).close();
		}
	}

	public static void go(RootPage page, PageArguments args, boolean saveHistory) {
		System.out.println("go(" + page.getName() + ")\nargs=" + args);
		close();
		Composite c;
		c = page.getHeader();
		if (c != null && RootPanel.get("header") != null) {
			RootPanel.get("header").clear();
			RootPanel.get("header").add(c);
		}
		c = page.getBody();
		if (c != null && RootPanel.get("body") != null) {
			clean(RootPanel.get("body"));
			RootPanel.get("body").add(c);
		}
		c = page.getGwtExtBody();
		if (c != null && RootPanel.get("body") != null) {
			RootPanel.get("body").add(c);
		}
		c = page.getFooter();
		if (c != null && RootPanel.get("footer") != null) {
			RootPanel.get("footer").clear();
			RootPanel.get("footer").add(c);
		}
		if (saveHistory) {
			System.out.println("Save History [" + args + "]");
			// History.newItem(args.getURL());
		}
		if (page.isInitEnable())
			page.init(args);
		Window.setTitle("Just Your Food - " + page.getTitle());
		FlowControl.showDefaultCursor();
		System.out.println("end go(" + page.getName() + ")\nargs=" + args);
	}

	private static void clean(RootPanel pageRoot) {
		pageRoot.clear();
		Element root = pageRoot.getElement();
		if ("true".equals(root.getAttribute("clean")))
			return;
		while (root.hasChildNodes()) {
			root.removeChild(root.getLastChild());
		}
		root.setAttribute("clean", "true");
	}

	@SuppressWarnings("unchecked")
	public static void goString(String url) {
		System.out.println("goSTRING(" + url + ")");
		PageArguments args = new PageArguments(url);
		String page = args.getPage();
		System.out.println("ARGS.PAGE=[" + page + "]");
		if (page == PageArguments.DEFAULT_PAGE)
			go(new Login(args), args, true);
		else if (page.equals(ExpertInfo.getSimpleName()))
			go(new ExpertInfo(args), args, true);
		// else if (page.equals(CreateUser.getSimpleName()))
		// go(new CreateUser(args), args, true);
		else if (page.equals(Admin.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(Admin.getSimpleName());
			if (pageC == null) {
				pageC = new Admin(args);
				pageCache.put(Admin.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(AdminReceipeIO.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(AdminReceipeIO
					.getSimpleName());
			if (pageC == null) {
				pageC = new AdminReceipeIO(args);
				pageCache.put(AdminReceipeIO.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(AdminReceipe.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(AdminReceipe
					.getSimpleName());
			if (pageC == null) {
				pageC = new AdminReceipe(args);
				pageCache.put(AdminReceipe.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(AdminRequestReceipes.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(AdminRequestReceipes
					.getSimpleName());
			if (pageC == null) {
				pageC = new AdminRequestReceipes(args);
				pageCache.put(AdminRequestReceipes.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(Test_tmp.getSimpleName()))
			go(new Test_tmp(args), args, true);
		else if (page.equals(Login.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(Login.getSimpleName());
			if (pageC == null) {
				pageC = new Login(args);
				pageCache.put(Login.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(Text.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(Text.getSimpleName());
			if (pageC == null) {
				pageC = new Text(args);
				pageCache.put(Text.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(News.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(News.getSimpleName());
			if (pageC == null) {
				pageC = new News(args);
				pageCache.put(News.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(RequestReceipes.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(RequestReceipes
					.getSimpleName());
			if (pageC == null) {
				pageC = new RequestReceipes(args);
				pageCache.put(RequestReceipes.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(RequestCiqual.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(RequestCiqual
					.getSimpleName());
			if (pageC == null) {
				pageC = new RequestCiqual(args);
				pageCache.put(RequestCiqual.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(Ciqual.getSimpleName())) {
			RootPage pageC = (RootPage) pageCache.get(Ciqual.getSimpleName());
			if (pageC == null) {
				pageC = new Ciqual(args);
				pageCache.put(Ciqual.getSimpleName(), pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(RequestCiqual.getSimpleName()))
			go(new RequestCiqual(args), args, true);
		else if (page.equals(Receipe.getSimpleName())) {
			String pageName = Receipe.getSimpleName();
			RootPage pageC = (RootPage) pageCache.get(pageName);
			if (pageC == null) {
				pageC = new Receipe(args);
				pageCache.put(pageName, pageC);
			}
			go(pageC, args, true);
		} else if (page.equals(Error.getSimpleName()))
			go(new Error(args, "Retour a la page d'erreur", null,
					new Exception()), args, false);
		else
			go(new Error(args, "Page non trouve [" + page + "]",
					new Exception(), null), args, false);
		System.out.println("end goSTRING(" + url + ")");
	}
}
