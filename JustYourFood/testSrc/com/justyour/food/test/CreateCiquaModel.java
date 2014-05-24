/**
 * 
 */
package com.justyour.food.test;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justyour.food.server.ToolsServer;
import com.justyour.food.server.jpa.JPAManager;
import com.justyour.food.shared.jpa.models.ciqual.CiqualModel;

/**
 * @author tonio
 * 
 */
public class CreateCiquaModel {

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

	@Test
	public void testExtractField() {
		String fieldSz = "332 Energie, N x facteur Jones, avec fibres (kJ/100g)";
		Pattern p = Pattern.compile("(\\d+)\\s+([^\\(]+)");
		Matcher m = p.matcher(fieldSz);
		if (m.find()) {
			System.out.println(" \"" + m.group(2) + "\",");
		} else {
			System.out.println(" \"\",");
		}

	}

	public static void createAll(String filename, JPAManager jpaManager)
			throws IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		List<String> csvLines = Files.readAllLines(Paths.get(filename),
				Charset.forName("UTF-8"));
		int i = 0;
		for (String line : csvLines) {
			if (i > 0) {
				CiqualModel ciqualModel = create(line);
				jpaManager.getJpaWriter().flush(ciqualModel);
			}
			i++;
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPattern() throws IOException {
		String field = "51330 Beta-Carotène (µg/100g)";
		System.out.println("old field[" + field + "]");
		// Pattern: \(\w+/\w+\)
		String pattern = "\\(\\S+/\\S+\\)";
		System.out.println("pattern[" + pattern + "]");
		String newField = field.replaceFirst(pattern, "");
		System.out.println("new field[" + newField + "]");
		Pattern p = Pattern.compile("(\\S+/\\S+)");
		Matcher m = p.matcher(field);
		if (m.find()) {
			System.out.println(" \"" + m.group() + "\",");
		} else {
			System.out.println(" \"\",");
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void createModel() throws IOException {
		final String ciqualModelPath = "src/com/justyour/food/shared/jpa/models/ciqual/";
		List<String> csvLines = Files.readAllLines(
				Paths.get("data/Ciqual_2012_v02.07.csv"),
				Charset.forName("UTF-8"));
		List<String> templateLines = Files.readAllLines(
				Paths.get(ciqualModelPath + "/CiqualModel_template.java"),
				Charset.forName("UTF-8"));

		int i = 0;
		int nbField = 0;
		List<String> addedLines = null;
		ArrayList<String> fields = new ArrayList<String>();
		ArrayList<String>[] columns = null;
		for (String line : csvLines) {
			System.out.println(line);
			if (i == 0) {
				String headerLine = line.trim();
				StringTokenizer st = new StringTokenizer(headerLine, ";");
				nbField = st.countTokens();
				while (st.hasMoreTokens()) {
					fields.add(st.nextToken().trim());
				}
			} else {
				String headerLine = line.trim();
				StringTokenizer st = new StringTokenizer(headerLine, ";");
				columns = new ArrayList[nbField];

				int j = 0;
				while (st.hasMoreTokens()) {
					if (columns[j] == null)
						columns[j] = new ArrayList<String>();
					columns[j].add(st.nextToken());
				}
				j++;
			}
			i++;
		}
		// Looking for a possible PrimaryKey
		// ////////////////////////////////////////////////////////////////////////
		/*
		 * int primaryKeyIndex = -1; for (int columnIndex = 0; columnIndex <
		 * nbField; columnIndex++) { Hashtable<String, Boolean> ht = new
		 * Hashtable<>(); boolean primary = true; for (String string :
		 * columns[columnIndex]) {
		 * System.out.println("string["+columnIndex+"]:["
		 * +string+"] : "+ht.containsKey(string)); if (ht.containsKey(string)) {
		 * primary = false; break; } else { ht.put(string, true); } } if
		 * (primary == true) { primaryKeyIndex = columnIndex;
		 * System.out.println("PrimaryIndex:" +
		 * primaryKeyIndex+" diff. keys:"+ht.size()); break; } }
		 */
		int primaryKeyIndex = 3;

		// creating the new set of Fields / methods
		// ////////////////////////////////////////////////////////////////////////
		addedLines = createModel(fields, primaryKeyIndex);

		// Modifying the template
		// ////////////////////////////////////////////////////////////////////////
		// 0: waiting for BEGIN
		// 1 waiting for END
		ArrayList<String> newLines = new ArrayList<>();
		int state = 0;
		for (String line : templateLines) {
			switch (state) {
			case 0:
				if (line.contains("BEGIN")) {
					state = 1;
					newLines.addAll(addedLines);
				} else {
					line = line.replaceAll("_template", "");
					newLines.add(line);
				}
				break;
			case 1:
				if (line.contains("END"))
					state = 0;
				break;
			}
		}
		// Writing the Java Model
		// ////////////////////////////////////////////////////////////////////////
		FileWriter writer = new FileWriter(ciqualModelPath + "CiqualModel.java");
		for (String str : newLines) {
			writer.write(str);
			writer.write(System.lineSeparator());
		}
		writer.close();
	}

	@Test
	public void populateDB() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {
		JPAManager jpaManager = new JPAManager();
		/*
		 * Session session = (Session) jpaManager.getEm().getDelegate();
		 * Vector<?> ret =
		 * session.executeSQL("drop table METARECEIPE.CIQUAMODEL;"); for (Object
		 * object : ret) { System.out.println("Object:"+object); }
		 */
		createAll("data/Ciqual_2012_v02.07.csv", jpaManager);
	}

	protected List<String> createModel(ArrayList<String> fields,
			int primaryKeyIndex) {
		ArrayList<String> ret = new ArrayList<>();
		String fieldList = new String();
		String fieldNameList = new String();
		String valuesList = new String();
		String detailList = new String();
		String unitList = new String();
		int i = 0;
		for (String field : fields) {
			String newField = field.trim();
			String fieldName = extractFieldName(newField, i);
			newField = ToolsServer.removeUTF8BOM(newField);
			newField = ToolsServer.deAccent(newField);
			String pattern = "\\(\\S*\\)";
			newField = newField.replaceAll(pattern, "");
			newField = newField.replaceAll(",", "_");
			newField = newField.replaceAll("-", "_");
			newField = newField.replaceAll("\\s", " ").trim();
			newField = newField.replaceAll(" ", "_");
			newField = newField.replaceAll("/", "_");
			newField = newField.replaceAll(":", "_");
			Pattern p = Pattern.compile("^(\\d+)");
			Matcher m = p.matcher(newField);
			if (m.find()) {
				newField = newField.replaceFirst(p.pattern(), "");
				newField = newField + "_" + m.group();
			}
			newField = newField.replaceAll("_+", "_");
			fieldList += " \"m" + newField + "\",";
			fieldNameList += " \"" + fieldName + "\",";
			valuesList = valuesList + "this.m" + newField + "+\"\",";
			detailList += " \"" + field + "\",";
			p = Pattern.compile("\\((\\S+/\\S+)\\)");
			m = p.matcher(field);
			String type;
			if (i == primaryKeyIndex)
				ret.add("\t@Id");
			if (m.find()) {
				unitList += " \""
						+ m.group().substring(1, m.group().length() - 1)
						+ "\",";
				type = "Double";
			} else {
				unitList += " \"\",";
				type = "String";
			}
			ret.add("\t/**");
			ret.add("\t" + field);
			ret.add("\t*/");
			ret.add("\tpublic " + type + " m" + newField + ";");
			ret.add("\t/**");
			ret.add("\t@return - " + field);
			ret.add("\t*/");
			ret.add("\tpublic " + type + " getM" + newField + "()");
			ret.add("\t{");
			ret.add("\t\treturn this.m" + newField + ";");
			ret.add("\t}");
			ret.add("");
			i++;
		}
		// let's remove the last ','
		fieldList = fieldList.substring(0, fieldList.length() - 1);
		fieldNameList = fieldNameList.substring(0, fieldNameList.length() - 1);
		valuesList = valuesList.substring(0, valuesList.length() - 1);
		detailList = detailList.substring(0, detailList.length() - 1);
		unitList = unitList.substring(0, unitList.length() - 1);
		ret.add("public void populateValue() {");
		ret.add("	this.values = new String[] {");
		ret.add(valuesList);
		ret.add("};}");
		ret.add("\tstatic {");
		ret.add("\t\tfields = add2Fields(" + fieldList + ");");
		ret.add("\t\tfieldNames = add2Fields(" + fieldNameList + ");");
		ret.add("\t\tdetails = add2Fields(" + detailList + ");");
		ret.add("\t\tunits = add2Fields(" + unitList + ");");
		ret.add("\t}");
		return ret;
	}

	private String extractFieldName(String fieldSz, int i) {
		final Pattern p = Pattern.compile("(\\d+)\\s+([^\\(]+)");
		Matcher m = p.matcher(fieldSz);
		if (m.find()) {
			return m.group(2);
		}
		switch (i) {
		case 1:
			return "Groupe";
		case 3:
			return "Désignation";
		}
		return fieldSz;
	}

}
