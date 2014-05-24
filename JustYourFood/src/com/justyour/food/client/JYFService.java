package com.justyour.food.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.justyour.food.shared.AdminInfos;
import com.justyour.food.shared.ResultsObject.ResultGetReceipes;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.shared.jpa.models.UserID;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * The client side stub for the IGwtRPC service.
 */
@RemoteServiceRelativePath("JYFService")
public interface JYFService extends RemoteService {
	ResultGetReceipes getReceipes(String name, int start, int length)
			throws Exception;

	long getNumberOfSuggestedReceipes(String name);

	int getNumberReceipes() throws IllegalArgumentException;

	int getNumberIngredients() throws IllegalArgumentException;

	HashMap<String, String> getServerEnv();

	HashMap<String, Object> getServerDeployFiles() throws IOException;

	HashMap<String, String> getServerProperties();

	ArrayList<String> getServletProperties();

	ArrayList<String> getSolrRequest(String input, int i) throws Exception;

	ArrayList<String> getSolrProperties();

	ReceipeModel getReceipe4RPC_ReadOnly(String link);

	String[] getReceipesSuggestions(String searchText, int i) throws Exception;

	CiqualModel getCiqual(String name);
	
	String[] getCiqualSuggestions(String query, int i);

	CiqualModel[] getCiqual(String text, int start, int length);

	long getNumberDBCiqual();

	String register(UserID userProfile) throws Exception;

	String getTextFile(String file);

	void startCrawl(String className, String url, int politnessDelay,
			int numberCrawls, int maxDepthCrawling, int maxPagesToFetch,
			int maxAddition, int maxVisiting) throws Exception;

	AdminInfos pullAdminInfo();

	HashMap<String, String> getDumperPlugins();

	void reloadParam();

	void reinitJYFSerletContext() throws Exception;

	void stopCrawl(String className);

	String cleanSolrFromDB() throws Exception;

	String updateSOLRFromDB();

	void dumpReceipe(String text, String string) throws Exception;

	void loadReceipe(String text) throws Exception;

	String getNewsFile(String month, String day);

	String updateCiqualDB(String filename) throws Exception;

	long getNumberSolrCiqual();

	long getNumberOfReceipes();

	long getNumberOfSuggestedCiquals(String name);

	long getNumberOfCiquals();

	String getDeployDir();

}
