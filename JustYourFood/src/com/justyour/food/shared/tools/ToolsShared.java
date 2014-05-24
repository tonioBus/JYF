/**
 * File: KitchenUtensil.java
 * Package: com.aquilaservices.metareceipe.wget.tools
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */
package com.justyour.food.shared.tools;

import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> KitchenUtensil.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.tools</b><br/>
 * <b>Date:</b> 5 ao�t 2013
 * 
 * @author tonio
 * 
 */
public class ToolsShared {
	static Logger logger = Logger.getLogger(ToolsShared.class.getName());

	/*
	public static String[] getInstructions(String text) {
		StringTokenizer st = new StringTokenizer(text, "\n.");
		ArrayList<String> list = new ArrayList<String>();

		while (st.hasMoreTokens()) {
			String sz = st.nextToken().trim();
			if (sz.length() > 0)
				list.add(sz);
		}
		return list.toArray(new String[0]);
	}
*/
	
	/*
	public static String[] glueStringList(String[] list1, String[] list2) {
		int len1 = list1.length;
		int len2 = list2.length;
		String[] retList = new String[len1 + len2];
		int i = 0;
		for (String string : list1) {
			retList[i++] = string;
		}
		for (String string : list2) {
			retList[i++] = string;
		}
		return retList;
	}
*/
	
	static public class LimitedQueue<E> extends LinkedList<E> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5892910428997351269L;
		private int limit;

		public LimitedQueue() {
			this(100);
		}
		
		public LimitedQueue(int limit) {
			this.limit = limit;
		}

		@Override
		public boolean add(E o) {
			boolean added = super.add(o);
			while (added && size() > limit) {
				super.remove();
			}
			return added;
		}

	}

	static public class Line {
		int level;
		String line;

		public Line(String line, int level) {
			this.line = line;
			this.level = level;
		}
	}

}
