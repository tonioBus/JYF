package com.justyour.food.server;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

public class TestCleanSolr {

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

	protected static void cleanSolr() {
		ArrayList<File> files = find("/justyour.com/jyf-solr-data", "write.lock");
		for (File file : files) {
			System.out.println("file:"+file.getAbsolutePath());
			System.out.println("deleting ?:"+file.delete());
		}
	}

	@Test
	public void test() {
		cleanSolr();
	}

}
