/** 
 * Project: JustYourFood
 * (c) 2014 Aquila Services
 * 
 * File: DBCiqual.java
 * Package: com.justyour.food.server.ciqual
 * Date: 27 janv. 2014
 * @author Anthony Bussani (bussania@gmail.com)
 * 
 */

package com.justyour.food.server.ciqual;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.SolrServerException;

import com.justyour.food.server.ToolsServer;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * @author tonio
 * 
 */
public class DBCiqual {
	
	public static void clean(JPAManager jpaManager) {
		jpaManager.getJpaWriter().cleanCiqual();
	}
	
	public static void createAll(String filename, JPAManager jpaManager, SolrCiqual solrCiqual)
			throws IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, SolrServerException {
		List<String> csvLines = Files.readAllLines(Paths.get(filename),
				Charset.forName("UTF-8"));
		int i = 0;
		for (String line : csvLines) {
			if (i > 0) {
				CiqualModel ciqualModel = create(line);
				jpaManager.getJpaWriter().flush(ciqualModel);
				solrCiqual.add(ciqualModel);
			}
			i++;
		}
	}

	public static CiqualModel create(String line) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		CiqualModel ciqualModel = new CiqualModel();
		StringTokenizer st = new StringTokenizer(line, ";");
		for (String fieldSz : CiqualModel.getFields()) {
			Field field = ciqualModel.getClass().getDeclaredField(fieldSz);
			String value = st.nextToken().trim();
			value = ToolsServer.deAccent(value);
			System.out.println("FIELD[" + field.getName() + "]."
					+ field.getType() + " <- [" + value + "]");
			if (field.getType() == Double.class) {
				try {
					value = value.replaceFirst(",", ".");
					Double d = Double.valueOf(value);
					field.set(ciqualModel, d);
					System.out.println("FIELD[" + field.getName() + "]."
							+ field.getType() + " <- Double["
							+ field.get(ciqualModel) + "]");
				} catch (NumberFormatException e) {
					field.set(ciqualModel, 0.0);
					System.out.println("FIELD[" + field.getName() + "]."
							+ field.getType() + " <- String["
							+ field.get(ciqualModel) + "]");
				}
			} else {
				field.set(ciqualModel, value);
				System.out.println("FIELD[" + field.getName() + "]."
						+ field.getType() + " <- String["
						+ field.get(ciqualModel) + "]");
			}
		}
		// Tools.wait4key();
		return ciqualModel;
	}
}
