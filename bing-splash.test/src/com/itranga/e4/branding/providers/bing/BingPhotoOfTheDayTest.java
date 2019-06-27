package com.itranga.e4.branding.providers.bing;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.che4.splash.E4Constants;
import io.github.che4.splash.bing.BingPhotoProvider;
import io.github.che4.splash.bing.Constants;
import io.github.che4.splash.bing.HPImageArchiveResponse;
import io.github.che4.splash.utils.BmpConverter;

public class BingPhotoOfTheDayTest {

	private static ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(BingPhotoOfTheDayTest.class);
	
	@Test
	public void getHPImageArchive() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
		HPImageArchiveResponse resp = objectMapper.readValue(
				new URL("http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=7&mkt=de-DE"), 
				HPImageArchiveResponse.class);
		
		Assert.assertNotNull("There is no images", resp.getImages());
		Assert.assertTrue("It MUST be at least one image", !resp.getImages().isEmpty());
		String urlBase = resp.getImages().get(0).urlbase;
		Assert.assertNotNull("Image url is null", urlBase);
		String urlFile = urlBase + Constants.SIZE._640x360.toString() + ".jpg";
		LOG.info("Image:  {}", resp.getImages().get(0).copyright);
		//BmpConverter.getBmp(new URL("http", "www.bing.com", urlFile), output);
	}
	
	@Test
	public void getBing() throws IOException, URISyntaxException, InterruptedException {
		BingPhotoProvider bpp = new BingPhotoProvider();
		URL classesRootDir = getClass().getProtectionDomain().getCodeSource().getLocation();
		Path rootPath = Paths.get(classesRootDir.toURI());
		File splashDir = new File(rootPath.toFile(), E4Constants.DOWNLOAD_DIR);
		splashDir.mkdirs();
		bpp.setSplashDirecotry(splashDir);
		bpp.updatePhotos(7)
			.thenAccept( num -> LOG.info("Downloaded {}", num));
			//.thenAccept( () -> { LOG.info("Bing images are in {}", splashDir.getAbsolutePath()); return null;} );
		Thread.sleep(2000);
		
	}
	
	//@Test
	public void getClassFile() throws IOException, URISyntaxException {
		URL classesRootDir = getClass().getProtectionDomain().getCodeSource().getLocation();
		Path rootPath = Paths.get(classesRootDir.toURI());
		LOG.info(rootPath.toRealPath().toString());
	}
}
