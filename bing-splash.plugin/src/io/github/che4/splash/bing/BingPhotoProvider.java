package io.github.che4.splash.bing;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.che4.splash.BmpConverter;

/**
 * Fetches &quot;Photo of the day&quot; from Bing.com and convert to BMP (for splash). 
 * @author Kalinin_DP
 *
 */
public class BingPhotoProvider {
	private static ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static final String BING_HOST = "www.bing.com";
	private static final String REST_SERIVCE_PATTERN = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=%d&n=%d&mkt=%s";
	private Map<String, File> downloaded = new HashMap<>();
	private File splashDirectory;
	
	public void setSplashDirecotry(File splashDirectory) {
		this.splashDirectory = splashDirectory;
	}
	
	public long updatePhotos() throws IOException {
		return updatePhotos(0,1,"en-WW");
	}
	
	public long updatePhotos(int n) throws IOException {
		return updatePhotos(0, n, "en-WW");
	}
	
	public long updatePhotos(int idx, int n, String mkt) throws IOException {
		if(splashDirectory == null) throw new IllegalStateException("Don't forget to setSplashDirectory() before inoking updatePhotos()");
		String rest_url_s = String.format(REST_SERIVCE_PATTERN, idx, n, mkt);
		URL rest_url = new URL(rest_url_s);
		HPImageArchiveResponse resp = objectMapper.readValue(rest_url, HPImageArchiveResponse.class);
		Objects.nonNull(resp);
		Objects.nonNull(resp.getImages());
		return resp.getImages().stream()
			.filter( potd -> !downloaded.containsKey(potd.urlbase))
			.map( potd -> {
				String imgFile = potd.urlbase + Constants.SIZE._640x360.toString() + ".jpg";
				try {
					URL imgUrl = new URL("http", BING_HOST, imgFile);
					String filename = org.apache.commons.io.FilenameUtils.getName(potd.urlbase) + ".bmp";
					File splashFile = new File(splashDirectory, filename);
					BmpConverter.getBmp(imgUrl, splashFile);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return imgFile;
			})
			.count();
			
	}
}
