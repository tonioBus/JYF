/**
 * 
 */
package com.justyour.food.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

/**
 * @author tonio
 * 
 */
public class ServerProperties {

	public static void getAllFile(File file, HashMap<String, Object> node)
			throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				if( file2.isDirectory()) {
					HashMap<String, Object> newNode = new HashMap<String, Object>();
					getAllFile(file2, newNode);
					node.put(file2.getAbsolutePath(), newNode);
				} else {
					getAllFile(file2, node);
				}
			}
		} else {
			String attributes = ""; // "Attributes: ";
			attributes += file.canExecute() ? "X+ " : "X- ";
			attributes += file.canWrite() ? "W+ " : "W- ";
			attributes += file.canRead() ? "R+ " : "R- ";
			BasicFileAttributes attrs = Files.readAttributes(file.toPath(),
					BasicFileAttributes.class);
			attributes += "\tCreation:" + attrs.creationTime();
			attributes += "\tLast Access Time:" + attrs.lastAccessTime();
			attributes += "\tLast Modified Time:" + attrs.lastModifiedTime();
			attributes += "\tKey:" + attrs.fileKey();
			node.put(file.getAbsolutePath(), attributes);
		}
	}

}
