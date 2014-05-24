/**
 * 
 */
package com.justyour.food.client;

/**
 * @author tonio
 * 
 */
public class PageArguments {
	public static final String DEFAULT_PAGE = "default_page";
	public static final String URL_DELIM = "void";
	/**
	 * for crawling in AJAX technologie (so GWT) we need to add '!' before the
	 * sharp ('#') of URLs. see
	 * https://support.google.com/webmasters/answer/174992?hl=en
	 */
	protected static final Character AJAX_crawling_char = '!';
	protected String page = DEFAULT_PAGE;
	protected String args[] = new String[0];
	protected StackTraceElement caller;

	public String getPage() {
		return page;
	}

	public String[] getArgs() {
		return args;
	}

	public PageArguments(String url) {
		boolean lastParamIsInGWT = url.lastIndexOf(';') == url.length() - 1;
		String tokens[] = url.split(";");

		// we remove the first '!' (AJAX_crawling_char)
		if (tokens[0].startsWith(AJAX_crawling_char + ""))
			this.page = tokens[0].substring(1);

		caller = new Exception().getStackTrace()[1];
		int len = tokens.length - 1;
		if (!lastParamIsInGWT && len > 0)
			len--;
		args = new String[len];
		for (int i = 0; i < len; i++) {
			String token = tokens[i + 1];
			if (token.equals(URL_DELIM))
				token = "";
			args[i] = token;
		}
	}

	public PageArguments(Class<?> clazz) {
		caller = new Exception().getStackTrace()[1];
		this.page = ToolsClient.getSimpleName(clazz);
		args = new String[0];
	}

	public PageArguments(Class<?> clazz, String argument) {
		caller = new Exception().getStackTrace()[1];
		this.page = ToolsClient.getSimpleName(clazz);
		args = new String[] { argument };
	}

	public PageArguments(Class<?> clazz, String arg1, String arg2) {
		caller = new Exception().getStackTrace()[1];
		this.page = ToolsClient.getSimpleName(clazz);
		args = new String[] { arg1, arg2 };
	}

	@Override
	public String toString() {
		String ret = "page[" + page + "]";
		for (int i = 0; i < args.length; i++) {
			ret += "args[" + i + "]=\"" + args[i] + "\"";
		}
		return ret;
	}

	public String getURL() {
		String ret = AJAX_crawling_char + this.page;
		for (String arg : args) {
			if (arg == "")
				arg = URL_DELIM;
			ret += ";" + arg;
		}
		return ret + ";";
	}
}
