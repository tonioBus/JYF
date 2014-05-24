/**
 * 
 */
package com.justyour.food.server.xml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.server.JYFServletContext;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * @author tonio
 * 
 */
public class ReceipeModelXML {

	static Logger logger = Logger.getLogger(ReceipeModelXML.class.getName());

	static public void dumpReceipeToXML(String filename, ReceipeModel receipe)
			throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(ReceipeModel.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(receipe, new FileWriter(filename));
	}

	static public ReceipeModel loadReceipeFromXML(String filename)
			throws Exception {
		JAXBContext context = JAXBContext.newInstance(ReceipeModel.class);
		Unmarshaller um = context.createUnmarshaller();
		ReceipeModel receipe = (ReceipeModel) um.unmarshal(new FileReader(
				filename));
		// we need to change ingredientsRPC to ingredientsSz
		for (IngredientQuantity iqRPC : receipe.getIngredientRPC()) {
			List<IngredientQuantity> iqs = JYFServiceImpl
					.getInstance()
					.getJpaManager()
					.getJpaWriter()
					.compileIQ(iqRPC.getDetails(), receipe,
							JYFServletContext.getIngredientNLP());
			if (iqs != null) {
				for (IngredientQuantity ingredientQuantity : iqs) {
					receipe.getIngredients().add(ingredientQuantity);
				}
			}
		}
		return receipe;
	}
}
