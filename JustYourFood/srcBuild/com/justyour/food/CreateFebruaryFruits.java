package com.justyour.food;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Test;

public class CreateFebruaryFruits {

	class MonthData {
		public MonthData(String name, String ciqualLink, String picLink,
				String wikiLink) {
			super();
			this.name = name;
			this.ciqualLink = ciqualLink;
			this.picLink = picLink;
			this.wikiLink = wikiLink;
			this.receipesLink = name;
		}

		public String name;
		public String ciqualLink;
		public String picLink;
		public String wikiLink;
		public String receipesLink;
	}

	String fruitsVegetable[] = { "Carotte", "Chou blanc", "Chou frisé",
			"Chou rouge", "Chou-chinois", "Citrouille", "Céleri", "Endive",
			"Mâche", "Oignon", "Poireau", "Pomme de terre", "Salsifis",
			"Topinambour", "Ananas", "Avocat", "Banane", "Citron",
			"Clémentine", "Fruit de la passion", "Grenade", "Kiwi", "Litchi",
			"Mandarine", "Mangue", "Orange", "Orange sanguine", "Pamplemousse",
			"Papaye", "Poire", "Pomme" };

	MonthData fevrier[] = {
			new MonthData(
					"Carotte",
					"#!Ciqual;Carotte,%20crue;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Daucus_Carota.jpg/290px-Daucus_Carota.jpg",
					"http://fr.wikipedia.org/wiki/Carotte"),
			new MonthData(
					"Chou blanc",
					"#!Ciqual;Chou%20blanc,%20cru;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Brassica_oleracea2.jpg/290px-Brassica_oleracea2.jpg",
					"http://fr.wikipedia.org/wiki/Chou_blanc"),
			new MonthData(
					"Chou frisé",
					null,
					"http://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Boerenkool.jpg/290px-Boerenkool.jpg",
					"http://fr.wikipedia.org/wiki/Chou_frise"),
			new MonthData(
					"Chou rouge",
					"#!Ciqual;Chou%20rouge,%20cru;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Rode_kool.jpg/290px-Rode_kool.jpg",
					"http://fr.wikipedia.org/wiki/Chou_rouge"),
			new MonthData(
					"Chou chinois",
					null,
					"http://upload.wikimedia.org/wikipedia/commons/a/ab/ChineseCabbage.jpg",
					"http://fr.wikipedia.org/wiki/Chou_chinois"),
			new MonthData(
					"Citrouille",
					null,
					"http://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Seattle_Tilth_Harvest_Fair_-_squash_01.jpg/220px-Seattle_Tilth_Harvest_Fair_-_squash_01.jpg",
					"http://fr.wikipedia.org/wiki/Citrouille"),
			new MonthData(
					"Céleri",
					"#!Ciqual;Celeri%20branche,%20cru;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/d/db/Celery_cross_section.jpg/220px-Celery_cross_section.jpg",
					"http://fr.wikipedia.org/wiki/Céleri"),
			new MonthData(
					"Endive",
					"#!Ciqual;Endive,%20crue;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/d/db/Witlof_en_wortel.jpg/290px-Witlof_en_wortel.jpg",
					"http://fr.wikipedia.org/wiki/Endive"),
			new MonthData(
					"Mâche",
					"#!Ciqual;Mache,%20crue;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Salad_at_Tate_Modern.jpg/220px-Salad_at_Tate_Modern.jpg",
					"http://fr.wikipedia.org/wiki/Mâche"),
			new MonthData(
					"Oignon",
					"#!Ciqual;Oignon,%20cru;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Onion.jpg/220px-Onion.jpg",
					"http://fr.wikipedia.org/wiki/Oignon"),
			new MonthData(
					"Poireau",
					"#!Ciqual;Poireau,%20cru;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Poireaux.JPG/290px-Poireaux.JPG",
					"http://fr.wikipedia.org/wiki/Poireaux"),
			new MonthData(
					"Pomme de terre",
					"#!Ciqual;Pomme%20de%20terre,%20cuite%20a%20l'eau;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/Solanum_tuberosum_02.jpg/170px-Solanum_tuberosum_02.jpg",
					"http://fr.wikipedia.org/wiki/Pomme_de_terre"),
			new MonthData(
					"Salsifis",
					"#!Ciqual;Salsifis,%20cuit;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Salsifis.jpg/290px-Salsifis.jpg",
					"http://fr.wikipedia.org/wiki/Salsifis"),
			new MonthData(
					"Topinambour",
					"#!Ciqual;Topinambour,%20cuit;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Tobinambur1.jpg/220px-Tobinambur1.jpg",
					"http://fr.wikipedia.org/wiki/Topinambour"),
			new MonthData(
					"Ananas",
					"#!Ciqual;Ananas,%20frais,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Owoce_Ananas.jpg/290px-Owoce_Ananas.jpg",
					"http://fr.wikipedia.org/wiki/Ananas"),
			new MonthData(
					"Avocat",
					"#!Ciqual;Avocat,%20frais,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/Avocado_with_cross_section.jpg/220px-Avocado_with_cross_section.jpg",
					"http://fr.wikipedia.org/wiki/Avocat_(fruit)"),
			new MonthData(
					"Banane",
					"#!Ciqual;Banane,%20fraiche,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Bananas_white_background.jpg/220px-Bananas_white_background.jpg",
					"http://fr.wikipedia.org/wiki/Banane"),
			new MonthData(
					"Citron",
					"#!Ciqual;Citron,%20frais,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Lemon.jpg/220px-Lemon.jpg",
					"http://fr.wikipedia.org/wiki/Citron"),
			new MonthData(
					"Clémentine",
					"#!Ciqual;Clementine%20ou%20Mandarine,%20fraiche,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Oh_my_darling.jpg/290px-Oh_my_darling.jpg",
					"http://fr.wikipedia.org/wiki/Clémentine"),
			new MonthData(
					"Fruit de la passion",
					"#!Ciqual;Fruit%20de%20la%20passion,%20frais,%20pulpe%20et%20pepins;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Passionfruit_comparison.jpg/180px-Passionfruit_comparison.jpg",
					"http://fr.wikipedia.org/wiki/Fruit_de_la_passion"),
			new MonthData(
					"Grenade",
					"#!Ciqual;Grenade,%20fraiche,%20pulpe%20et%20pepins;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Pomegranate02_edit.jpg/220px-Pomegranate02_edit.jpg",
					"http://fr.wikipedia.org/wiki/Grenade_(fruit)"),
			new MonthData(
					"Kiwi",
					"#!Ciqual;Kiwi,%20frais,%20pulpe%20et%20graines;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Kiwi_%28Actinidia_chinensis%29_1_Luc_Viatour.jpg/250px-Kiwi_%28Actinidia_chinensis%29_1_Luc_Viatour.jpg",
					"http://fr.wikipedia.org/wiki/Kiwi"),
			new MonthData(
					"Litchi",
					"#!Ciqual;Litchi,%20frais,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Litchi_chinensis_fruits.JPG/290px-Litchi_chinensis_fruits.JPG",
					"http://fr.wikipedia.org/wiki/Litchi"),
			new MonthData(
					"Mandarine",
					"#!Ciqual;Clementine%20ou%20Mandarine,%20fraiche,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Mandarin_Oranges_%28Citrus_Reticulata%29.jpg/220px-Mandarin_Oranges_%28Citrus_Reticulata%29.jpg",
					"http://fr.wikipedia.org/wiki/Mandarine"),
			new MonthData(
					"Mangue",
					"#!Ciqual;Mangue,%20fraiche,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/e/ee/Mango_and_cross_section_edit.jpg/250px-Mango_and_cross_section_edit.jpg",
					"http://fr.wikipedia.org/wiki/Mangue"),
			new MonthData(
					"Orange",
					"#!Ciqual;Orange,%20fraiche,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Ambersweet_oranges.jpg/220px-Ambersweet_oranges.jpg",
					"http://fr.wikipedia.org/wiki/Orange_(fruit)"),
			new MonthData(
					"Orange sanguine",
					null,
					"http://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/Blood_oranges.jpg/250px-Blood_oranges.jpg",
					"http://fr.wikipedia.org/wiki/Orange_sanguine"),
			new MonthData(
					"Pamplemoussee",
					"#!Ciqual;Pomelo%20(dit%20Pamplemousse),%20frais,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Citrus_paradisi_%28Grapefruit%2C_pink%29_white_bg.jpg/120px-Citrus_paradisi_%28Grapefruit%2C_pink%29_white_bg.jpg",
					"http://fr.wikipedia.org/wiki/Pamplemousse"),
			new MonthData(
					"Papaye",
					"#!Ciqual;Papaye,%20fraiche,%20pulpe;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/d/df/Papaia_%26_Mam%C3%A3o.JPG/220px-Papaia_%26_Mam%C3%A3o.JPG",
					"http://fr.wikipedia.org/wiki/Papaye"),
			new MonthData(
					"Poire",
					"#!Ciqual;Poire,%20fraiche,%20pulpe%20et%20peau;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Pears.jpg/220px-Pears.jpg",
					"http://fr.wikipedia.org/wiki/Poire"),
			new MonthData(
					"Pomme",
					"#!Ciqual;Pomme,%20fraiche,%20pulpe%20et%20peau;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Fuji_apple.jpg/220px-Fuji_apple.jpg",
					"http://fr.wikipedia.org/wiki/Pomme") };

	String colors[] = { "OrangeRed", "#FF5500", "Tomato", "#FF7050", "Coral",
			"#FF8030", "DarkOrange", "#FFA000", "Orange" };

	protected String getCell(MonthData monthData) {
		String ingredient = monthData.name;
		String ciqualLink = monthData.ciqualLink;
		String wikiLink = monthData.wikiLink;
		String imageLink = monthData.picLink;
		String cell = "<h2 style=\"font-size: 1em;\">" + ingredient + "</h2>\n";
		if (ciqualLink == null) {
		} else {
			cell += "<a " + "href=\"" + ciqualLink
					+ "\"\n title=\"Informations nutritionnelles: "
					+ ingredient + "\""
					+ " style=\"font-size: 0.9em; text-align:center;\""
					+ "\n target=\"_blank\">Nutrition</a><br/>\n";
		}
		cell += "<a " + "href=\"" + wikiLink
				+ "\"\n title=\"Information wikipedia sur " + ingredient
				+ "\"\n" + " style=\"font-size: 0.9em; text-align:center;\""
				+ " target=\"_blank\"> Wiki </a>\n";
		cell += "<a  style=\"margin-bottom: 2px;\" title=\"Recettes utilisant des " + ingredient
				+ "(s)\"\n href=\"#!RequestReceipes;" + ingredient
				+ ";\">\n<img class=\"jyf-img img-fruits\"\n src=\""
				+ imageLink + "\">\n</a>";
		return cell;
	}

	@Test
	public void test() throws FileNotFoundException {
		PrintWriter out = new PrintWriter("/tmp/todel.html");
		int col = 0;
		int li = 0;
		for (MonthData monthData : fevrier) {
			if (col == 0) {
				out.println("<TR style=\"background-color: " + colors[li]
						+ ";\">");
			}
			if (col == 4) {				
				li++;
				col = 0;
				out.println("</TR>");
			}
			String cell = getCell(monthData);
			out.println("<TD>");
			out.println(cell);
			out.println("</TD>");
			col++;
			if (col == 4) {				
				li++;
				col = 0;
				out.println("</TR>");
			}
		}
		out.close();
	}

}
