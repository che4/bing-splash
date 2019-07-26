package io.github.che4.touchpoint;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class ConfigUtil {

	public static String separatorsToSystem(String res) {
		if (res==null) return null;
		if (File.separatorChar=='\\') {
			// From Windows to Linux/Mac
			return res.replace('/', File.separatorChar);
		} else {
			// From Linux/Mac to Windows
			return res.replace('\\', File.separatorChar);
		}
	}
	
	public static String toUrlSeparators(String res) {
		if (res==null) return null;
		if (File.separatorChar=='\\') {
			// From Windows to Linux/Mac
			return res.replace(File.separatorChar, '/');
		}
		return res;
	}
	
	public static String toConfigUrl(File file) throws MalformedURLException {
		String fileUrl = ConfigUtil.toUrlSeparators(Paths.get(file.toString()).normalize().toString());
		URL url = new URL("file", null, "/"+fileUrl);
		return url.toString();
	}
	
}
