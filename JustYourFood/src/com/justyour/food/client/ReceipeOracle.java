package com.justyour.food.client;

import java.util.Collection;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public class ReceipeOracle extends SuggestOracle {

	class MySuggestion implements SuggestOracle.Suggestion {
		String replace;
		String display;

		public MySuggestion(String replace, String display) {
			this.replace = replace;
			this.display = display;
		}

		@Override
		public String getReplacementString() {
			return replace;
		}

		@Override
		public String getDisplayString() {
			// String ret = display;
			String ret= "<span class=\"jyf-\">" + display + "</span>";
			// ret.replaceAll(replace,
			// "<span style=\"font-weight:bold;\">"+replace+"<span>");
			return ret;
		}
	};

	JYFServiceAsync server;

	public ReceipeOracle() {
		server = Index.getServer();
	}

	@Override
	public void requestSuggestions(final SuggestOracle.Request suggestRequest,
			final SuggestOracle.Callback callback) {
		server.getReceipesSuggestions(suggestRequest.getQuery(), 10,
				new AsyncCallback<String[]>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(String[] suggestions) {
						SuggestOracle.Response r = new SuggestOracle.Response();
						// Fill the suggestions based on the response parameter.
						Collection<MySuggestion> collection = new Vector<MySuggestion>();
						for (int i = 0; i < suggestions.length; i++) {
							String title = suggestions[i];
							MySuggestion suggestion = new MySuggestion(title,
									title);
							collection.add(suggestion);
						}
						r.setSuggestions(collection);
						callback.onSuggestionsReady(suggestRequest, r);
					}
				});

	}

	@Override
	public boolean isDisplayStringHTML() {
		return true;
	}

}
