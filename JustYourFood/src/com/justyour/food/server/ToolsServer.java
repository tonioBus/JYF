/**
 * 
 */
package com.justyour.food.server;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.justyour.food.shared.tools.ToolsShared.Line;

/**
 * @author tonio
 * 
 */
public class ToolsServer {

	static Logger logger = Logger.getLogger(ToolsServer.class.getName());

	public static void wait4key(String string) {
		if (string != null) {
			System.out.println(string);
		}
		System.out.println("Press <Enter> to continue ...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void wait4key() {
		wait4key(null);
	}

	/*
	 * static public void wait4key() {
	 * System.out.print("WAITING FOR A KEY ... "); try { do { System.in.read();
	 * } while (System.in.available() > 0); } catch (IOException e) {
	 * e.printStackTrace(); } }
	 */

	/**
	 * Process <b>french</b> time for coocking, preparing, resting
	 * 
	 * @param text
	 *            - something like "1 j 3 h 15 min", "1 jour 3 heures",
	 *            "35 minutes", etc etc
	 * @return number of total seconds
	 */
	public static double getTimeInSecond_FR(String text) {
		StringTokenizer st = new StringTokenizer(text);
		int retTime = 0;
		int time;

		while (st.hasMoreElements()) {
			try {
				time = Integer.valueOf(st.nextToken());
			} catch (NumberFormatException e) {
				return -1;
			}
			// by default we return minutes, is it correct ?
			if (st.hasMoreElements() == false) {
				return time * 60;
			}
			String unit = st.nextToken().toLowerCase();
			if (unit.startsWith("m")) {
				retTime += time * 60;
			} else if (unit.startsWith("h")) {
				retTime += time * 60 * 60;
			} else if (unit.startsWith("j")) {
				retTime += time * 60 * 60 * 24;
			} else {
				logger.info("unit=[" + unit + "] -> [" + text + "]");
				// wait4key();
			}
		}
		return retTime;
	}

	static class ExecStreamHandler extends LogOutputStream {
		Queue<Line> queue;

		public ExecStreamHandler(Queue<Line> queue) {
			this.queue = queue;
		}

		@Override
		protected void processLine(String lineSz, int level) {
			Line line = new Line(lineSz, level);
			queue.add(line);
			logger.info("[" + level + "]:" + lineSz);
		}
	}

	/**
	 * "http://recettes.doctissimo.fr/"
	 * 
	 * @param url
	 * @throws ExecuteException
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static int execWget(String url, Queue<Line> queue)
			throws ExecuteException, IOException, JAXBException {
		Parameters param = JYFServletContext.getParam();
		DefaultExecutor exec = new DefaultExecutor();

		exec.setWorkingDirectory(new File(param.getWorkingTmp()));
		ExecStreamHandler stream = new ExecStreamHandler(queue);
		PumpStreamHandler psh = new PumpStreamHandler(stream, stream);
		exec.setStreamHandler(psh);
		String commandSz = param.getWget();
		CommandLine cl = new CommandLine(commandSz);
		String[] options = param.getWgetOptions().split("\\s+");
		cl.addArguments(options);
		cl.addArguments("http://recettes.doctissimo.fr/");
		logger.info("command [" + cl.getExecutable() + "]");
		int exitvalue = exec.execute(cl);
		return exitvalue;
	}

	static public void print(Node node, NamedNodeMap nodeMap) {
		String indent = "  ";
		logger.info(indent + "Ingredient:" + "\n" + "name:"
				+ node.getNodeName() + "\n"
				+ indent
				+ "|type:"
				+ node.getNodeType()
				+ "\n"
				// + indent + "|textContent:" +
				// node.getTextContent()+"\n"
				+ indent + "|length:"
				+ (nodeMap == null ? "null" : nodeMap.getLength()) + "\n"
				+ indent + "|value:" + node.getNodeValue());
	}

	static public String getTextContent(Node node) {
		String value = node.getTextContent();
		return value;

		// String ret = " ";
		// if (value != null)
		// ret += value;
		//
		// Node child = node.getFirstChild();
		// while (child != null) {
		// value = getTextContent(child);
		// if (value != null)
		// ret += " " + value;
		// child = child.getNextSibling();
		// }
		// return ret;

	}

	static ArrayList<File> find(String dir, String searchFile) {
		ArrayList<File> ret = new ArrayList<>();
		find(ret, new File(dir), searchFile);
		return ret;
	}

	private static void find(ArrayList<File> ret, File dir, String searchFile) {
		if (dir.isDirectory() == false)
			return;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory())
				find(ret, file, searchFile);
			if (file.isFile() && file.getName().equals(searchFile)) {
				ret.add(file);
			}
		}
	}

	static public String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str,
				Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String removeUTF8BOM(String s) {
		if (s.startsWith("\uFEFF")) {
			s = s.substring(1);
		}
		return s;
	}

}
