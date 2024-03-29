package com.justyour.food;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

public class CreateMarch {

	/*
	 * Carotte Chou blanc Chou frisé Chou rouge Chou-rave Céleri Endive Oignon
	 * Petit oignon blanc Poireau Salsifis Fruits Ananas Avocat Banane Citron
	 * Fruit de la passion Kiwi Mandarine Mangue Orange sanguine Pamplemousse
	 * Papaye Poire Pomme
	 */
	MonthData march[] = {
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
					"Chou-rave",
					null,
					"http://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/KohlrabiinMarket.jpg/290px-KohlrabiinMarket.jpg",
					"http://fr.wikipedia.org/wiki/Chou-rave"),
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
					"Oignon",
					"#!Ciqual;Oignon,%20cru;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Onion.jpg/220px-Onion.jpg",
					"http://fr.wikipedia.org/wiki/Oignon"),
			new MonthData(
					"Petit oignon blanc",
					"#!Ciqual;Oignon,%20cru;",
					"http://wikibouffe.iga.net/thumbs/timthumb.php?src=/images/uploads/wiki/805b_oignon_vert_BS000520th.jpg&w=266&h=219",
					"http://fr.wikipedia.org/wiki/Oignon"),
			new MonthData(
					"Poireau",
					"#!Ciqual;Poireau,%20cru;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Poireaux.JPG/290px-Poireaux.JPG",
					"http://fr.wikipedia.org/wiki/Poireaux"),
			new MonthData(
					"Salsifis",
					"#!Ciqual;Salsifis,%20cuit;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Salsifis.jpg/290px-Salsifis.jpg",
					"http://fr.wikipedia.org/wiki/Salsifis"),
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
					"Fruit de la passion",
					"#!Ciqual;Fruit%20de%20la%20passion,%20frais,%20pulpe%20et%20pepins;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Passionfruit_comparison.jpg/180px-Passionfruit_comparison.jpg",
					"http://fr.wikipedia.org/wiki/Fruit_de_la_passion"),
			new MonthData(
					"Kiwi",
					"#!Ciqual;Kiwi,%20frais,%20pulpe%20et%20graines;",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Kiwi_%28Actinidia_chinensis%29_1_Luc_Viatour.jpg/250px-Kiwi_%28Actinidia_chinensis%29_1_Luc_Viatour.jpg",
					"http://fr.wikipedia.org/wiki/Kiwi"),
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
					"Orange sanguine",
					null,
					"http://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/Blood_oranges.jpg/250px-Blood_oranges.jpg",
					"http://fr.wikipedia.org/wiki/Orange_sanguine"),
			new MonthData(
					"Pamplemousse",
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

	@Test
	public void createMarch() throws MalformedURLException, IOException {
		CreateFruitsLifeRay create = new CreateFruitsLifeRay();
		create.createFile(march);
	}

}
