package com.justyour.food.client;


public abstract class RootPage {

	protected String name;
	protected MyComposite header = null;

	protected MyComposite body = null;

	protected MyComposite footer = null;
	public MyComposite gwtExtbody = null;

	public String getName() {
		return name;
	}
	
	public boolean isInitEnable() {
		return true;
	}

	public boolean isAdmin() {
		return false;
	}
	
	public MyComposite getHeader() {
		return header;
	}

	public MyComposite getBody() {
		return body;
	}

	public MyComposite getGwtExtBody() {
		return gwtExtbody;
	}

	public MyComposite getFooter() {
		return footer;
	}


	public abstract void init(PageArguments args);

	public RootPage(Class<?> clazz) {
		FlowControl.showWaitCursor();
		this.name = ToolsClient.getSimpleName(clazz);
	}

	public void close() {
	}

	public abstract String getTitle();

}
