package io.github.che4.splash.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TxtFile {

	private final File file = new File("C:\\TEMP\\p2-test.txt");

	public static TxtFile get() {
		return new TxtFile();
	}

	public String path() {
		return file.getPath();
	}

	public void appendLine(String line) throws IOException {
		FileWriter out = new FileWriter(file, true);
		out.append(line + "\n");
		out.flush();
		out.close();
	}

	public void delete() {
		file.delete();
	}
}