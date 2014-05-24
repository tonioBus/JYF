/**
 * 
 */
package com.justyour.food.server.jpa;

import java.util.logging.Logger;

/**
 * @author tonio
 * 
 */
public class JPAManager extends JPATransaction {

	static Logger logger = Logger.getLogger(JPAManager.class.getName());

	JPAWriter jpaWriter = null;
	JPAQuery jpaQuery = null;
	
	public JPAWriter getJpaWriter() {
		return jpaWriter;
	}

	public JPAQuery getJpaQuery() {
		return jpaQuery;
	}

	public JPAManager() {
		logger.info("BEGIN <CONS> " + JPAManager.class.getName());
		jpaWriter = new JPAWriter(this);
		jpaQuery = new JPAQuery(this);
		logger.info("END <CONS> " + JPAManager.class.getName());
	}
	
	public void close() {
		jpaWriter.close();
		jpaQuery.close();
		super.closeHandler();
	}

}
