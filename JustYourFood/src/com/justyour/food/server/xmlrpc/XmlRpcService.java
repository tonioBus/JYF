/** 
 * Project: JustYourFood
 * (c) 2014 Aquila Services
 * 
 * File: JYFService.java
 * Package: com.justyour.food.xmlrpc
 * Date: 20 avr. 2014
 * @author Anthony Bussani (bussania@gmail.com)
 * 
 */

package com.justyour.food.server.xmlrpc;

import java.io.Serializable;
import java.util.logging.Logger;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * @author tonio
 * 
 */
public class XmlRpcService {
	static Logger logger = Logger.getLogger(XmlRpcService.class.getName());

	JYFServiceImpl instance;

	public XmlRpcService() {
		System.out.println("Init: " + this.getClass());
		logger.info("Init: " + this.getClass());
		instance = JYFServiceImpl.getInstance();
	}
	
	public synchronized Serializable getReceipes(String name, int start, int length)
			throws Exception {
		return instance.getReceipes(name, start, length);
	}

	ReceipeModel getReceipe4RPC_ReadOnly(String link) {
		return instance.getReceipe4RPC_ReadOnly(link);
	}

	
	/*
	@Override
	public long getNumberOfSuggestedReceipes(String name) {
		return instance.getNumberOfSuggestedReceipes(name);
	}

	@Override
	public int getNumberReceipes() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberIngredients() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HashMap<String, String> getServerEnv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getServerDeployFiles() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getServerProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getServletProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getSolrRequest(String input, int i)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getSolrProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReceipeModel getReceipe4RPC_ReadOnly(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getReceipesSuggestions(String searchText, int i)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiqualModel getCiqual(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCiqualSuggestions(String query, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiqualModel[] getCiqual(String text, int start, int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getNumberDBCiqual() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String register(UserID userProfile) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextFile(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startCrawl(String className, String url, int politnessDelay,
			int numberCrawls, int maxDepthCrawling, int maxPagesToFetch,
			int maxAddition, int maxVisiting) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public AdminInfos pullAdminInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getDumperPlugins() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reloadParam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reinitJYFSerletContext() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopCrawl(String className) {
		// TODO Auto-generated method stub

	}

	@Override
	public String cleanSolrFromDB() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateSOLRFromDB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dumpReceipe(String text, String string) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadReceipe(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNewsFile(String month, String day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateCiqualDB(String filename) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getNumberSolrCiqual() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumberOfReceipes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumberOfSuggestedCiquals(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumberOfCiquals() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDeployDir() {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
