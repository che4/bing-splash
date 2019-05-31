package io.github.che4.touchpoint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestTxtFile {

	private final File file = new File("C:\\TEMP\\p2-test.txt");

	public static TestTxtFile get() {
		return new TestTxtFile();
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