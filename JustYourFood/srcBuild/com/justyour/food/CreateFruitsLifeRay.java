package com.justyour.food;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;

import org.junit.Test;

public class CreateFruitsLifeRay {

	private static final String FILE = "/tmp/todel.html";
	MessageFormat format;

	protected void addLine(StringBuffer buffer, String line) {
		buffer.append(line + "\n");
	}

	public CreateFruitsLifeRay() {
		// 0: name
		// 1: picLink
		// 2: debug text wiki
		// 3: ciqual link
		// 4: wiki link
		StringBuffer buffer = new StringBuffer();
		addLine(buffer, "<div style=\"display: table-row; margin:0; \">");
		addLine(buffer,
				"<div style=\"display: table-cell; vertical-align:middle; width: 8em; margin:0 auto; text-align:center; padding-right:0.5em; \">");
		addLine(buffer, "<a title=\"Recettes utilisant des {0}(s)\"");
		addLine(buffer, "href=\"nutrition\\#!RequestReceipes;{0};\" >");
		addLine(buffer, "<img class=\"fruits-image\"");
		addLine(buffer, " src=\"{1}\" />");
		addLine(buffer, "</a>");
		addLine(buffer, "</div>");
		addLine(buffer,
				"<div style=\"display:table-cell; text-align: justify; padding-right:1em; padding-left:1em; border-left-style: ridge;\">");
		addLine(buffer, "<h2 style=\"font-size: 1.2em;\">{0}</h2>");
		addLine(buffer, "<p style=\"text-align:justify;\">");
		addLine(buffer, "{2}");
		addLine(buffer, "</p>");
		addLine(buffer, "<a href=\"{4}\"");
		addLine(buffer, "title=\"Information wikipedia sur {0}\"");
		addLine(buffer,
				"style=\"font-size: 0.8em; text-align:center;\" target=\"_blank\">la suite sur Wikipedia</a>");
		addLine(buffer, "&nbsp;");
		addLine(buffer, "<a href=\"http://justyourfood.com/nutrition/index.html{3};\"");
		addLine(buffer,
				"title=\"Informations nutritionnelles: {0}\" style=\"font-size: 0.8em; text-align:center;\"");
		addLine(buffer, "target=\"_blank\">Informations nutritionnelles</a>");
		addLine(buffer, "&nbsp;");
		addLine(buffer,
				"<a title=\"Recettes utilisant des {0}(s)\" style=\"font-size: 0.8em; text-align:center;\"");
		String url="http://justyourfood.com/web/guest/recherche-par-mots-cles-aliments-ingredients?justyourfood_fwd=http://justyourfood.com/nutrition/mini-index.html%23!RequestReceipes;{0};";
		// String url="http://justyourfood.com/nutrition/index.html#!RequestReceipes;{0};";
		addLine(buffer,
				"href=\""+url+"\">Recettes à base de {0}(s)</a><br/>");

		addLine(buffer, "</div>");
		addLine(buffer, "</div>");
		this.format = new MessageFormat(buffer.toString(), Locale.FRANCE);
	}


	protected String getCell(MonthData monthData) throws MalformedURLException,
			IOException {
		// 0: name
		// 1: picLink
		// 2: debut text wiki
		// 3: ciqual link
		// 4: wiki link
		String def = getWikiAbstract(monthData.wikiLink, -1);
		def = removeAncher(def);
		Object[] testArgs = { monthData.name, monthData.picLink, def,
				monthData.ciqualLink, monthData.wikiLink };

		return format.format(testArgs);
	}

	void createFile(MonthData datas[]) throws MalformedURLException, IOException {
		PrintWriter out = new PrintWriter(FILE, "UTF-8");
		out.println("<head><meta charset=\"UTF-8\">" + "<style>"
				+ ".fruits-image{"
				+ "-webkit-box-shadow: 1px 1px 1px 1px rgba(0,0,0,0.40);"
				+ "-moz-box-shadow: 1px 1px 1px 1px rgba(0,0,0,0.40);"
				+ "box-shadow: 1px 1px 1px 1px rgba(0,0,0,0.40);"
				+"border-radius:4px;"
				+ "max-height:7em;" + "width:7em;" + "}" + "</style>"
				+ "</head>");
		out.println("<html lang=\"fr\">");
		for (MonthData monthData : datas) {
			String cell = getCell(monthData);
			out.println(cell);
			out.println("<hr/>");
			System.out.print(".");
		}
		out.println("</html>");
		out.close();
		System.out.println("\nThe End: file in " + FILE);
	}

	@Test
	public void testGetWikiAbstract() throws MalformedURLException, IOException {
		String pomme = getWikiAbstract("http://fr.wikipedia.org/wiki/Pomme", -1);
		System.out.println("pomme:");
		System.out.println("[" + pomme + "]");
	}

	String removeAncher(String line) {
		String ret = line.replaceAll("<a([^>]*)>", "");
		ret = ret.replaceAll("</a>", "");
		return ret;
	}

	@Test
	public void testremoveAncher() {
		System.out
				.println(removeAncher("La <b>pomme</b> est le <a href=\"/wiki/Fruit_(botanique)\" title=\"Fruit (botanique)\">fruit</a> du <a href=\"/wiki/Pommier\" title=\"Pommier\">pommier</a>. Elle est <a href=\"/wiki/Alimentation\" title=\"Alimentation\">comestible</a> et a un <a href=\"/wiki/Go%C3%BBt\" title=\"Goût\">goût</a> <a href=\"/wiki/Sucre\" title=\"Sucre\">sucré</a> ou <a href=\"/wiki/Acide\" title=\"Acide\">acidulé</a> selon les variétés. Elle fait partie des fruits les plus consommés dans le monde.</p>"));
	}

	String getWikiAbstract(String url, int maxLine)
			throws MalformedURLException, IOException {
		InputStreamReader isr = new InputStreamReader(new URL(url).openStream());
		BufferedReader in = new BufferedReader(isr);
		String inputLine;
		int status = 0;
		StringBuffer buffer = new StringBuffer();
		int nbLine = 0;

		/*
		 * status before pattern status after action 0 </div> 1 0 1 <p> 2
		 * collect buffer </div> 1 0 2 <p> 2 collect buffer <div id="toc" ?
		 * return buffer 0 reset buffer
		 */
		// grab the contents at the URL
		while ((inputLine = in.readLine()) != null) {
			inputLine = inputLine.trim();
			switch (status) {
			case 0:
				if (inputLine.startsWith("</div>")) {
					status = 1;
				}
				break;
			case 1:
				if (inputLine.startsWith("<p>")) {
					buffer.append(inputLine);
					nbLine++;
					if (nbLine >= maxLine)
						return buffer.toString();
					status = 2;
				} else if (inputLine.startsWith("</div>")) {
					status = 1;
				} else
					status = 0;
				break;
			case 2:
				if (inputLine.startsWith("<p>")) {
					buffer.append(inputLine);
					nbLine++;
					if (nbLine >= maxLine)
						return buffer.toString();
				} else if (inputLine.startsWith("<div id=\"toc\"")) {
					return buffer.toString();
				} else if (inputLine.startsWith("</div>")) {
					buffer = new StringBuffer();
					status = 0;
				}
				break;
			}
		}
		return "Pas de définitions trouvées";
	}
}
