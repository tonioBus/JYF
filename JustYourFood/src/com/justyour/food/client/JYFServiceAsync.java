package com.justyour.food.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.justyour.food.shared.AdminInfos;
import com.justyour.food.shared.ResultsObject.ResultGetReceipes;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.shared.jpa.models.UserID;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client side stub for the IGwtRPC service.
 */
public interface JYFServiceAsync {
	void getReceipes(String name, int start, int length, AsyncCallback<ResultGetReceipes> callback);

	void getNumberOfSuggestedReceipes(String name, AsyncCallback<Long> callback);

	void getNumberReceipes(AsyncCallback<Integer> callback);

	void getNumberIngredients(AsyncCallback<Integer> callback);

	void getServerEnv(AsyncCallback<HashMap<String, String>> callback);

	void getServerDeployFiles(AsyncCallback<HashMap<String, Object>> callback);

	void getServerProperties(AsyncCallback<HashMap<String, String>> callback);

	void getServletProperties(AsyncCallback<ArrayList<String>> callback);

	void getSolrRequest(String input, int i, AsyncCallback<ArrayList<String>> callback);

	void getSolrProperties(AsyncCallback<ArrayList<String>> callback);

	void getReceipe4RPC_ReadOnly(String link, AsyncCallback<ReceipeModel> callback);

	void getReceipesSuggestions(String searchText, int i, AsyncCallback<String[]> callback);

	void getCiqual(String name, AsyncCallback<CiqualModel> callback);
	
	void getCiqualSuggestions(String query, int i, AsyncCallback<String[]> callback);

	void getCiqual(String text, int start, int length, AsyncCallback<CiqualModel[]> callback);

	void getNumberDBCiqual(AsyncCallback<Long> callback);

	void register(UserID userProfile, AsyncCallback<String> callback);

	void getTextFile(String file, AsyncCallback<String> callback);

	void startCrawl(String className, String url, int politnessDelay,
			int numberCrawls, int maxDepthCrawling, int maxPagesToFetch,
			int maxAddition, int maxVisiting, AsyncCallback<Void> callback);

	void pullAdminInfo(AsyncCallback<AdminInfos> callback);

	void getDumperPlugins(AsyncCallback<HashMap<String, String>> callback);

	void reloadParam(AsyncCallback<Void> callback);

	void reinitJYFSerletContext(AsyncCallback<Void> callback);

	void stopCrawl(String className, AsyncCallback<Void> callback);

	void cleanSolrFromDB(AsyncCallback<String> callback);

	void updateSOLRFromDB(AsyncCallback<String> callback);

	void dumpReceipe(String text, String string, AsyncCallback<Void> callback);

	void loadReceipe(String text, AsyncCallback<Void> callback);

	void getNewsFile(String month, String day, AsyncCallback<String> callback);

	void updateCiqualDB(String filename, AsyncCallback<String> callback);

	void getNumberSolrCiqual(AsyncCallback<Long> callback);

	void getNumberOfReceipes(AsyncCallback<Long> callback);

	void getNumberOfSuggestedCiquals(String name, AsyncCallback<Long> callback);

	void getNumberOfCiquals(AsyncCallback<Long> callback);

	void getDeployDir(AsyncCallback<String> callback);

}
