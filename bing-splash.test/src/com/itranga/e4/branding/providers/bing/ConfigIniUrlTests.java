package com.itranga.e4.branding.providers.bing;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;



public class ConfigIniUrlTests {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ConfigIniUrlTests.class);
	@Test
	public void skipWhitespaceEscaping() throws IOException {
		String stringPath = "C:\\Program Files\\1C\\1CE\\components\\1c-edt-1.10.2+57%&$SA-x86_64\\features\\io.github.che4.splash.feature_1.0.1.10";
		File f = new File(stringPath);
		URL url = f.toURI().toURL();
		LOG.debug("URL: {}. Details: protocol={} host={}, path={}, file{}", url, url.getProtocol(), url.getHost(), url.getPath(), url.getFile());
		LOG.info("URL decoded: {}", URLDecoder.decode(url.toString(), "UTF-8"));
		LOG.info("File: {}", url.getFile());
		
		Path path = Paths.get(stringPath);
		
		URL url2 = new URL("file", null, ConfigUtil.toUrlSeparators("/"+path.normalize().toString()));
		LOG.info("URL2: {}", url2.toString());
		
		LOG.info("Resutlt: {}", ConfigUtil.toConfigUrl(f));
	}
}
