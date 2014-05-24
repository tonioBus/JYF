/**
 * File: DoctissimoWget.java
 * Package: com.aquilaservices.metareceipe.wget.providers.doctissimo
 * Date: 05/08/2013
 * User: tonio
 *
 * (c) 2013 Aquila Services
 */

package com.justyour.food.wget.providers.doctissimo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import opennlp.tools.util.InvalidFormatException;

import org.apache.html.dom.HTMLLIElementImpl;
import org.apache.html.dom.HTMLMetaElementImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.xml.sax.SAXException;

import com.justyour.food.server.JYFServiceImpl;
import com.justyour.food.server.ToolsServer;
import com.justyour.food.server.ToolsShared;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.jpa.models.IngredientQuantity;
import com.justyour.food.shared.jpa.models.ReceipeModel;
import com.justyour.food.wget.IWgetProvider;
import com.justyour.food.wget.opennlp.IngredientNLP;
import com.justyour.food.wget.tools.IOWget;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

/**
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> DoctissimoWget.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.providers.doctissimo</b><br/>
 * <b>Date:</b> 5 août 2013
 * 
 * @author tonio
 * 
 */
public class DoctissimoWget implements IWgetProvider {

	static Logger logger = Logger.getLogger(DoctissimoWget.class.getName());

	JPAManager jpaManager = null;
	IOWget ioWget;
	IngredientNLP ingredientNLP;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aquilaservices.metareceipe.WGET.providers.IWGETProvider#init(java
	 * .util.Properties)
	 */
	@Override
	public <T extends WebCrawler> void init(final Class<T> crawlerClass, Properties properties) 
			throws SAXException, IOException, JAXBException {
		jpaManager = JYFServiceImpl.getInstance().getJpaManager();
		ioWget = new IOWget();
		ingredientNLP = new IngredientNLP();
		String limitSz = properties.getProperty(LIMIT);
		int limit = Integer.MAX_VALUE;
		if (limitSz != null) {
			try {
				limit = Integer.valueOf(limitSz);
			} catch (NumberFormatException e) {
			}
		}
		String urlsSz = properties.getProperty(URLS);
		final String pattern = properties.getProperty(PATTERN);
		File file = new File(urlsSz);
		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getPath().endsWith(pattern);
				// return file.getPath().endsWith(".htm");
			}
		};
		File[] files = file.listFiles(fileFilter);
		int length = files.length;
		URL urls[] = new URL[length];
		for (int i = 0; i < length; i++) {
			urls[i] = new URL("file:///" + files[i].getAbsolutePath());
		}

		int nbReceipes = 0;

		// loop over all files / WEB pages
		for (int i = 0; i < urls.length && nbReceipes < limit; i++) {			
			ReceipeModel receipe = jpaManager.getJpaWriter().create(urls[i] + "", "http://recettes.doctossimo.fr");
			logger.info("URL[" + i + "]: " + urls[i]);
			File fileLink = new File(urls[i].getFile());
			receipe.setLink("http://www.recettes.doctissimo.fr/"
					+ fileLink.getName());
			if (this.jpaManager.getJpaQuery().isStored(receipe.getLink()) == true) {
				logger.fine("Receipe already exist: " + receipe.getLink());
				continue;
			}
			DocumentFragment doc = ioWget.getDocumentFragmentFromURL(urls[i]);
			if (doc == null) {
				logger.info("doc=null -> roller back[" + nbReceipes + "]:" + receipe);
				continue;
			}
			boolean ret = dump(receipe, doc);
			if (ret == true && receipe.isWellFormed()) {
				this.jpaManager.getJpaWriter().persist(receipe);
				logger.info("WRITING RECEIPE[" + nbReceipes + "]:" + receipe);
				nbReceipes++;
			} else {
				logger.info("receipe not correct -> roller back[" + nbReceipes + "]:" + receipe);
				logger.info("isWellFormed:"+receipe.isWellFormed());
				// Tools.wait4key();
			}
		}
		// end loop
		logger.info("Provider [" + this.getClass().getName() + "]: "
				+ nbReceipes + " receipe(s) added");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aquilaservices.metareceipe.WGET.providers.IWGETProvider#free()
	 */
	@Override
	public void close() {
		ioWget.close();
	}

	private boolean dump(ReceipeModel receipe, Node node)
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
						// KitchenUtensil.wait4key("og:type -> [" + nodeContent
						// + "]");
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
						String text = ToolsShared.getTextContent(node);
						receipe.setInstructions(text);
					} else if (node instanceof HTMLLIElementImpl
							&& "instruction".equals(nodeItemPropSz)) {
						if (receipe.getInstructions()
								.equals(ReceipeModel.EMPTY_INSTRUCTIONS)) {
							String text = ToolsShared.getTextContent(node);
							receipe.setInstructions(text);
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
						String ingredientLine = ToolsShared.getTextContent(node);
						System.out.println("ingredient:" + ingredientLine);

						List<IngredientQuantity> iqs = jpaManager
								.getJpaWriter().compileIQ(ingredientLine,
										receipe,
										this.ingredientNLP);
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
						String text = ToolsShared.getTextContent(node);
						receipe.setPreparationSecond(text,
								ToolsServer.getTimeInSecond_FR(text));
						receipe.setPreparationSecondSentence(text);
					}
					/*
					 * <time itemprop="cookTime" datetime="PT0M">0 mn</time>
					 */
					else if ("cookTime".equals(nodeItemPropSz)) {
						String text = ToolsShared.getTextContent(node);
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
							String difficulty = ToolsShared.getTextContent(node)
									.trim();

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
							String priceSz = ToolsShared.getTextContent(node).trim();
							String normalizedPrice = priceSz.toLowerCase();
							int price = 0;
							if (normalizedPrice.equals("bon march�"))
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

	private int normalizeDifficulty(String text) {
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
			logger.severe("NEW DIFFICULTY: [" + text + "]");
			// KitchenUtensil.wait4key();
		}
		return -1;
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
