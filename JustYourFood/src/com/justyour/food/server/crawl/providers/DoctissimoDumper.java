/**
 * 
 */
package com.justyour.food.server.crawl.providers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import opennlp.tools.util.InvalidFormatException;

import org.apache.html.dom.HTMLLIElementImpl;
import org.apache.html.dom.HTMLMetaElementImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;

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
public class DoctissimoDumper extends Dumper {
	static Logger logger = Logger.getLogger(DoctissimoDumper.class.getName());
	final public static String defaultDomain = "http://recettes.doctissimo.fr";

	final public static String getDefaultDomain() {
		return defaultDomain;
	}

//	public DoctissimoDumper(IngredientNLP ingredientNLP, JPAManager jpaManager,
//			int maxAdded, int maxVisited) {
//		super(domain, ingredientNLP, jpaManager, maxAdded, maxVisited);
//	}

	public DoctissimoDumper(String domain, IngredientNLP ingredientNLP,
			JPAManager jpaManager, int maxAdded, int maxVisited) {
		super(domain, ingredientNLP, jpaManager, maxAdded, maxVisited);
	}

	@Override
	public boolean dump(ReceipeModel receipe, Node node)
			throws InvalidFormatException, IOException {
		NamedNodeMap nodeMap = node.getAttributes();
		// logger.info("node=" + node.getNodeName() + " childs="
		// + node.getChildNodes().getLength());
		if (nodeMap != null && nodeMap.getLength() > 0) {

			/*
			 * <meta property="og:type" content="article" />^M <meta
			 * property="og:title"
			 * content="Abattis de poulet aux p�tes jaunes (chine)" />^M <meta
			 * property="og:image" content=
			 * "http://mf.imdoc.fr/content/5/2/5/475258/viande_rectangle.png"
			 * />^M
			 */
			if (node instanceof HTMLMetaElementImpl) {
				// KitchenUtensil.wait4key("Content="
				// + nodeMap.getNamedItem("content").getNodeValue());

				Node nodeProperty = nodeMap.getNamedItem("property");

				if (nodeProperty != null) {
					// <meta property="og:type" content="article" />
					if ("og:type".equals(nodeProperty.getNodeValue())) {
						Node nodeContent = nodeMap.getNamedItem("content");
						if (nodeContent.getNodeValue().equals("article"))
							receipe.setWellFormed(true);
						else
							return false;
						// <meta property="og:title"
						// content="Abattis de poulet aux p�tes jaunes (chine)"
						// />
					} else if ("og:title".equals(nodeProperty.getNodeValue())) {
						Node nodeContent = nodeMap.getNamedItem("content");
						receipe.setTitle(nodeContent.getNodeValue().trim());
						// <meta property="og:image"
						// content="http://mf.imdoc.fr/content/5/2/5/475258/viande_rectangle.png"
						// />
					} else if ("og:image".equals(nodeProperty.getNodeValue())) {
						Node nodeContent = nodeMap.getNamedItem("content");
						receipe.setImage(nodeContent.getNodeValue());
					}
				}
			} else {
				Node nodeItemProp = nodeMap.getNamedItem("itemprop");
				if (nodeItemProp != null) {
					String nodeItemPropSz = nodeItemProp.getNodeValue();
					/**
					 * INSTRUCTIONS <li itemprop="instruction">Pr�parer une
					 * cr�me p�tissi�re: travailler les jaunes d'oeufs avec le
					 * sucre et la farine. Ve\ rser dessus le lait chaud. Faire
					 * �paissir sur feu doux. Ajouter un peu de rhum pour
					 * parfumer. Verser dans un plat allant au four. Cou\ per en
					 * deux et d�noyauter les abricots, les faire pocher 3
					 * minutes dans un sirop obtenu en faisant simplement
					 * bouillir l'eau et le su\ cre en morceaux. Egoutter les
					 * abricots. Faire r�duire le sirop de moiti�. Disposer les
					 * demi-abricots sur la cr�me, partie bomb�e dess\ us.
					 * Parsemer des amandes effil�es, arroser de sirop, faire
					 * gratiner 10 minutes � four chaud ou au gril. A la sortie
					 * du four arroser d\ e rhum et flamber.</li>
					 */
					if (node instanceof HTMLLIElementImpl
							&& "instructions".equals(nodeItemPropSz)) {
						String text = ToolsServer.getTextContent(node);
						receipe.setInstructions(text);
					} else if (node instanceof HTMLLIElementImpl
							&& "instruction".equals(nodeItemPropSz)) {
						if (receipe.getInstructions().equals(
								ReceipeModel.EMPTY_INSTRUCTIONS)) {
							String text = ToolsServer.getTextContent(node);
							receipe.setInstructions(text);
						} else {
							String text = ToolsServer.getTextContent(node);
							receipe.setInstructions(receipe.getInstructions()
									+ "\n" + text);
						}
					}
					/*
					 * INGREDIENTS <li itemprop="ingredient" itemscope
					 * itemtype="http://data-vocabulary.org/RecipeIngredient">
					 * <span itemprop="amount"> <span class="quantite"
					 * val="1/2">1/2</span> litre</span> <span itemprop="name">
					 * de <a href=
					 * "http://recettes.doctissimo.fr/engredients/recette-lait.htm"
					 * > lait</a> </span> </li> <li itemprop="ingredient"
					 * itemscope
					 * itemtype="http://data-vocabulary.org/RecipeIngredient">
					 * <span itemprop="amount"> <span class="quantite"
					 * val="3">3</span> </span> <span itemprop="name">\ <a href=
					 * "http://recettes.doctissimo.fr/engredients/recette-jaune-d-oeuf.htm"
					 * >jaunes d'oeufs</a> </span></li>
					 */
					else if (node instanceof HTMLLIElementImpl
							&& "ingredient".equals(nodeItemPropSz)) {
						String ingredientLine = ToolsServer
								.getTextContent(node);
						logger.info("ingredient:" + ingredientLine);

						List<IngredientQuantity> iqs = jpaManager
								.getJpaWriter().compileIQ(ingredientLine,
										receipe, this.ingredientNLP);
						if (iqs != null) {
							for (IngredientQuantity ingredientQuantity : iqs) {
								receipe.getIngredients()
										.add(ingredientQuantity);
							}
						}
					}

					/*
					 * <time itemprop="totalTime" datetime="PT0M">0 mn</time>
					 */
					else if ("totalTime".equals(nodeItemPropSz)) {
						// String text = KitchenUtensil.getTextContent(node);
						// receipe.setCookingSecond(text,
						// KitchenUtensil.getTimeInSecond(text));
						receipe.setRestSecond(0);
						receipe.setRestSecondSentence("not defined");
					}
					/*
					 * <time itemprop="prepTime" datetime="PT25M">25 mn</time>
					 */
					else if ("prepTime".equals(nodeItemPropSz)) {
						String text = ToolsServer.getTextContent(node);
						receipe.setPreparationSecond(text,
								ToolsServer.getTimeInSecond_FR(text));
						receipe.setPreparationSecondSentence(text);
					}
					/*
					 * <time itemprop="cookTime" datetime="PT0M">0 mn</time>
					 */
					else if ("cookTime".equals(nodeItemPropSz)) {
						String text = ToolsServer.getTextContent(node);
						receipe.setCookingSecond(text,
								ToolsServer.getTimeInSecond_FR(text));
						receipe.setCookingSecondSentence(text);
					}
				} else {

					Node nodeClass = nodeMap.getNamedItem("class");
					if (nodeClass != null) {
						String classSz = nodeClass.getNodeValue();

						// INSTRUCTIONS
						/*
						 * <li class="difficulte"> <span class="picto"> <span
						 * class=
						 * "md_noclass_cryptlink45CBCBC02D1F1FC1434243CBCB43C2194B4F42CB46C2C2464E4F1944C11F4B43C2C243C1CBC21F4C4ACB434AC3C51F4E4FC6434949431945CB4E"
						 * >Moyenne</span> </span> </li>
						 */
						if (node instanceof HTMLLIElementImpl
								&& "difficulte".equalsIgnoreCase(classSz)) {
							String difficulty = ToolsServer
									.getTextContent(node).trim();

							receipe.setDifficultySentence(difficulty);
							receipe.setDifficulty(normalizeDifficulty(difficulty));
						}
						/*
						 * <li class="cout"> <span class="picto"> <a href=
						 * "http://recettes.doctissimo.fr/desserts/gateaux/economique.htm"
						 * >Economique</a> </span> </li>
						 */
						if (node instanceof HTMLLIElementImpl
								&& "cout".equalsIgnoreCase(classSz)) {
							String priceSz = ToolsServer.getTextContent(node)
									.trim();
							String normalizedPrice = priceSz.toLowerCase();
							int price = 0;
							if (normalizedPrice.equals("bon marché"))
								price = 2;
							else if (normalizedPrice.equals("economique"))
								price = 1;
							receipe.setPrice(priceSz, price);
						}

					} else {
						Node nodeId = nodeMap.getNamedItem("id");
						if (nodeId != null) {
							String idSz = nodeId.getNodeValue();
							/**
							 * <a id="captchaLink_alert" href=
							 * "http://recettes.doctissimo.fr/baba.htm?action=get_recette_captcha"
							 * >Cliquer ici si le code est illisible</a>
							 * Sometimes, this branch is not existing, so we use
							 * the previous link set using the filename
							 */
							if (node instanceof HTMLAnchorElement
									&& "captchaLink_alert".equals(idSz)) {
								String link = nodeMap.getNamedItem("href")
										.getNodeValue();
								link = link.substring(0, link.indexOf("?"));
								link = link.trim();
								logger.info("LINK: " + link);
								if (jpaManager.getJpaQuery().isStored(link) == true) {
									// already exist, we can stop the processing
									// of this DOM tree
									logger.info("Receipe already exist: "
											+ link);
									return false;
								}
								receipe.setLink(link);
							}
							/*
							 * <span id="nb" val="8">
							 */
							if ("nb".equals(idSz)) {
								String numberSz = nodeMap.getNamedItem("val")
										.getNodeValue();
								try {
									receipe.setNumberPersons(Integer
											.valueOf(numberSz));
								} catch (NumberFormatException e) {
									receipe.setNumberPersons(-1);
								}
								receipe.setNumberPersonsSentence(numberSz);
							}
						}
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

	protected int normalizeDifficulty(String text) {
		text = text.toLowerCase().trim();
		System.out.println("DIFFICULTY: " + text);
		if (text.equals("facile"))
			return 0;
		else if (text.equals("moyenne"))
			return 5;
		else if (text.equals("elaborée"))
			return 8;
		else if (text.equals("difficile"))
			return 10;
		else if (text.equals(""))
			return -1;
		else {
			System.out.println("NEW DIFFICULTY: [" + text + "]");
		}
		return -1;
	}

}
