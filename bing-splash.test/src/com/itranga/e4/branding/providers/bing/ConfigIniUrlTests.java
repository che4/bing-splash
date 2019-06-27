package com.itranga.e4.branding.providers.bing;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;

import org.junit.Test;


public class ConfigIniUrlTests {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ConfigIniUrlTests.class);
	@Test
	public void skipWhitespaceEscaping() throws UnsupportedEncodingException, MalformedURLException {
		File f = new File("C:\\Program Files\\1C\\1CE\\components\\1c-edt-1.10.2+57-x86_64\\features\\io.github.che4.splash.feature_1.0.1.10");
		LOG.debug("URL: {}", f.toURI().toURL());
		LOG.info("URL decoded: {}", URLDecoder.decode(f.toURI().toURL().toString(), "UTF-8"));
	}
}
