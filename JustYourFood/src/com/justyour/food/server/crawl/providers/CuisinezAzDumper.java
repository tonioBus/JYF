/**
 * 
 */
package com.justyour.food.server.crawl.providers;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import opennlp.tools.util.InvalidFormatException;

import org.apache.html.dom.HTMLLIElementImpl;
import org.apache.html.dom.HTMLMetaElementImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.justyour.food.server.ToolsServer;
import com.justyour.food.server.crawl.Dumper;
import com.justyour.food.server.crawl.opennlp.IngredientNLP;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.ReceipeModel;

/**
 * @author tonio
 *
 */
public class CuisinezAzDumper extends Dumper {
	static Logger logger = Logger.getLogger(CuisinezAzDumper.class
			.getName());
	final public static String defaultDomain = "http://www.cuisineaz.com";

	final public static String getDefaultDomain() {
		return defaultDomain;
	}
	
	public CuisinezAzDumper(String domain, IngredientNLP ingredientNLP, JPAManager jpaManager, int maxAdded, int maxVisited) {
		super(domain, ingredientNLP, jpaManager, maxAdded, maxVisited);
	}

	@Override
	public boolean dump(ReceipeModel receipe, Node node)
			throws InvalidFormatException, IOException {
		NamedNodeMap nodeMap = node.getAttributes();

		if (nodeMap != null && nodeMap.getLength() > 0) {

			/*
			 * <meta property="og:title" content="Cr�me glac�e de ricotta"
			 * /> <meta property="og:url" content=
			 * "http://www.cuisineaz.com/recettes/creme-glacee-de-ricotta-6532.aspx"
			 * /> <meta property="og:image"
			 * content="http://images.cuisineaz.com/240x192/Santalucia003.jpg"
			 * />
			 */
			if (node instanceof HTMLMetaElementImpl) {
				Node nodeClass = nodeMap.getNamedItem("property");

				if (nodeClass != null) {
					// <meta property="og:type" content="cuisineazsite:recipe"
					// />
					if ("og:type".equals(nodeClass.getNodeValue())) {
						Node nodeContent = nodeMap.getNamedItem("content");
						if (nodeContent.getNodeValue().equals(
								"cuisineazsite:recipe"))
							receipe.setWellFormed(true);
						else
							return false;
					} else if ("og:title".equals(nodeClass.getNodeValue())) {
						Node nodeContent = nodeMap.getNamedItem("content");
						receipe.setTitle(nodeContent.getNodeValue());
					} else if ("og:url".equals(nodeClass.getNodeValue())) {
						Node nodeContent = nodeMap.getNamedItem("content");
						String link = nodeContent.getNodeValue().trim();
						if (this.jpaManager.getJpaQuery().isStored(link) == true) {
							logger.fine("Receipe already exist: " + link);
							return false;
						}
						receipe.setLink(link);
					} else if ("og:image".equals(nodeClass.getNodeValue())) {
						Node nodeContent = nodeMap.getNamedItem("content");
						receipe.setImage(nodeContent.getNodeValue());
					}
				}
			} else {
				Node nodeClass = nodeMap.getNamedItem("class");
				if (nodeClass != null) {
					String classSz = nodeClass.getNodeValue();

					// old style
					// <h1 class="recetteH1 fn">Samossa de boeuf </h1>
					// if ("recetteH1 fn".equals(classSz)) {

					// }
					// INGREDIENTS
					/*
					 * <li class="currentIngredients"> <span>2 pots de Ricotta
					 * Santa Lucia (250 g) (500 g) </span> </li>
					 */
					if (node instanceof HTMLLIElementImpl
							&& "ingredient".equals(classSz)) {
						// KitchenUtensil.print(node, nodeMap);
						if (receipe.isWellFormed() == false)
							return false;
						Node child = node.getFirstChild().getFirstChild();
						if (child != null) {
							String ingredientLine = child.getNodeValue();
							System.out.println("INGREDIENT=[" + ingredientLine
									+ "]");
							List<IngredientQuantity> iqs = jpaManager
									.getJpaWriter().compileIQ(ingredientLine,
											receipe, this.ingredientNLP);
							if (iqs != null) {
								for (IngredientQuantity ingredientQuantity : iqs) {
									receipe.getIngredients().add(
											ingredientQuantity);
								}
							}
						}
						// INSTRUCTIONS
					} else if ("instructions".equalsIgnoreCase(classSz)) {
						String text = ToolsServer.getTextContent(node);
						receipe.setInstructions(text);
					}
				}
				Node nodeId = nodeMap.getNamedItem("id");
				if (nodeId != null) {
					String idSz = nodeId.getNodeValue();

					/*
					 * TIME PREPARATION <span
					 * id="ctl00_ContentPlaceHolder_LblRecetteTempsPrepa"
					 * class="preptime">10 min</span>
					 */
					if ("ctl00_ContentPlaceHolder_LblRecetteTempsPrepa"
							.equals(idSz)) {
						String text = ToolsServer.getTextContent(node);

						double preparationSecond = ToolsServer
								.getTimeInSecond_FR(text);
						receipe.setPreparationSecond(text, preparationSecond);
					}

					/*
					 * NUMBER OF PERSONS
					 * 
					 * <td id="recipeQuantity"> <span
					 * id="ctl00_ContentPlaceHolder_LblRecetteNombre"
					 * class="yield">4 Personne(s)</span> </td>
					 */
					else if ("ctl00_ContentPlaceHolder_LblRecetteNombre"
							.equals(idSz)) {
						String text = ToolsServer.getTextContent(node);
						StringTokenizer st = new StringTokenizer(text);
						if (st.hasMoreTokens()) {
							String numbers = st.nextToken();
							// we should take care of example like:
							// "6/8 persons"
							// "6-10 ..."
							// we take the minimum
							StringTokenizer st1 = new StringTokenizer(numbers,
									"//-");
							try {
								String numberSz = st1.nextToken();
								int number = Integer.valueOf(numberSz);
								logger.fine("PERSONS[" + number + "]");
								receipe.setNumberPersons(text, number);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}

					/*
					 * REST TIME <td id="ctl00_ContentPlaceHolder_recipeRepos">
					 * <span
					 * id="ctl00_ContentPlaceHolder_LblRecetteTempsRepos">3 h
					 * </span> </td>
					 */
					else if ("ctl00_ContentPlaceHolder_LblRecetteTempsRepos"
							.equals(idSz)) {
						String text = ToolsServer.getTextContent(node);

						double restSecond = ToolsServer
								.getTimeInSecond_FR(text);
						receipe.setRestSecondSentence(text);
						receipe.setRestSecond(restSecond);
					}
					/*
					 * <td id="recipeCoutFR"> <span
					 * id="ctl00_ContentPlaceHolder_LblRecetteCout"
					 * >Abordable</span> </td>
					 */
					else if ("ctl00_ContentPlaceHolder_LblRecetteCout"
							.equals(idSz)) {
						String text = ToolsServer.getTextContent(node);
						String normalizedPrice = text.toLowerCase();
						if (normalizedPrice.isEmpty())
							receipe.setPrice("Price not given", -1);
						else {
							int price = 0;
							if (normalizedPrice.equals("abordable"))
								price = 2;
							else if (normalizedPrice.equals("pas cher"))
								price = 1;
							else if (normalizedPrice.equals("assez cher"))
								price = 6;
							receipe.setPrice(text, price);
						}
					}
					/*
					 * DIFFICULTY <td id="recipeDifficult"> <span
					 * id="ctl00_ContentPlaceHolder_LblRecetteDifficulte"
					 * >Facile</span> </td>
					 */
					else if ("ctl00_ContentPlaceHolder_LblRecetteDifficulte"
							.equals(idSz)) {
						String text = ToolsServer.getTextContent(node);
						int difficulty = normalizeDifficulty(text);
						receipe.setDifficultySentence(text);
						receipe.setDifficulty(difficulty);
					}
				}
			}
		}
		Node child = node.getFirstChild();
		while (child != null) {
			boolean ret = dump(receipe, child);
			if (ret == false)
				return false;
			child = child.getNextSibling();
		}
		return true;
	}

	private int normalizeDifficulty(String text) {
		text = text.toLowerCase().trim();
		logger.fine("DIFFICULTY: " + text);
		if (text.equals("facile"))
			return 0;
		else if (text.equals("intermédiaire"))
			return 5;
		else if (text.equals("difficile"))
			return 10;
		else if (text.equals(""))
			return -1;
		else {
			logger.warning("NEW DIFFICULTY: [" + text + "]");
		}
		return -1;
	}

	
}
