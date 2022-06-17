package org.colomoto;

import java.io.File;

public class TestHelper {

	static File resourceFolder;
	static File outputFolder;
	
	static {
		resourceFolder = new File("target", "test-classes");
		if (!resourceFolder.isDirectory() ) {
			throw new RuntimeException("No resource folder");
		}
		
		outputFolder = new File("target", "test-output");
		if (!outputFolder.exists() ) {
			outputFolder.mkdir();
		}
		
		if (!outputFolder.isDirectory() ) {
			throw new RuntimeException("No output folder");
		}
	}
	
	public static File getTestResource(String name) {
		return getTestResource(null, name);
	}

	public static String getTestFilename(String dirname, String f) {
		File dir = TestHelper.getTestResource(dirname);
		if (!dir.isDirectory()) {
			throw new RuntimeException("Could not find the reference model folder: "+dir.getAbsolutePath());
		}
		return new File(dir, f).getAbsolutePath();
	}
	
	public static File getTestResource(String group, String name) {
		File dir = resourceFolder;
		
		if (group != null && group.length() > 0) {
			dir = new File(resourceFolder, group);
			if (!dir.isDirectory()) {
				throw new RuntimeException("resource group not found: "+dir.getAbsolutePath());
			}
		}
		
		return new File(dir, name);
	}

	public static File getTestOutput(String name) {
		return getTestOutput(null, name);
	}

	public static File getTestOutput(String group, String name) {
		File dir = outputFolder;
		
		if (group != null && group.length() > 0) {
			dir = new File(outputFolder, group);
			if (!dir.exists()) {
				dir.mkdir();
			}
			if (!dir.isDirectory()) {
				throw new RuntimeException("output group not found: "+dir.getAbsolutePath());
			}
		}
		
		return new File(dir, name);
	}

}
