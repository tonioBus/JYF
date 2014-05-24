package com.justyour.food.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.justyour.food.shared.tools.ToolsShared.LimitedQueue;

/**
 * @author tonio
 * 
 */
public class DumperInfos implements IsSerializable {

	protected int maxVisited;
	protected int maxAdded;
	protected int visited;
	protected int added;
	protected String dumperClass;
	

	LimitedQueue<String> logs = new LimitedQueue<String>(100);
	private boolean finish;

	public DumperInfos() {
	}

	public DumperInfos(String dumperClass) {
		this.dumperClass = dumperClass;
	}

	public String getDumperClass() {
		return this.dumperClass;
	}

	public void init(int maxVisited, int maxAdded) {
		this.maxAdded = maxAdded;
		this.maxVisited = maxVisited;
	}

	public synchronized int getVisited() {
		return this.visited;
	}

	public synchronized int getAdded() {
		return this.added;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.justyour.food.wget.Dumper#incNbVisit()
	 */
	public synchronized void incNbVisit() {
		this.visited++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.justyour.food.wget.Dumper#incNbAdded()
	 */
	public synchronized void incNbAdded() {
		this.added++;
	}

	public int getMaxVisit() {
		return this.maxVisited;
	}

	public int getMaxAdded() {
		return maxAdded;
	}

	public boolean isMaxAddedReached() {
		boolean ret = maxAdded > 0 && added >= maxAdded;
		return ret;
	}

	public boolean isMaxVisitReached() {
		boolean ret = maxVisited > 0 && visited >= maxVisited;
		return ret;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CLASS: "+dumperClass);
		sb.append("\nvisited : " + visited);
		sb.append("\nadded : " + added);
		sb.append("\nlogs:\n" + logs);
		String[] logs = getLogs();
		for (String string : logs) {
			sb.append("\n - " + string);
		}
		return sb.toString();
	}

	public void log(String... line) {
		for (String string : line) {
			this.logs.add(new Date() + " : " + string);
		}
	}

	public String[] getLogs() {
		String[] ret = new String[this.logs.size()];
		int i = 0;
		for (String string : this.logs) {
			ret[i++] = string;
		}
		return ret;
	}

	public void setIsFinished(boolean finish) {
		this.finish = finish;
	}

	public boolean isFinish() {
		return finish;
	}


}