package io.github.che4.splash.bing;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
	
	public Optional<File> getSplashDirectory(){
		return Optional.ofNullable(splashDirectory);
	}
	
	public CompletableFuture<Long> updatePhotos() {
		return updatePhotos(0,1,"en-WW");
	}
	
	public CompletableFuture<Long> updatePhotos(int n) {
		return updatePhotos(0, n, "en-WW");
	}
	
	public CompletableFuture<Long> updatePhotos(int idx, int n, String mkt){
		return CompletableFuture.supplyAsync( () -> { 
			Objects.requireNonNull(splashDirectory, "Don't forget to setSplashDirectory() before inoking updatePhotos()");
			String rest_url_s = String.format(REST_SERIVCE_PATTERN, idx, n, mkt);
			HPImageArchiveResponse resp = null;
			try {
				URL rest_url = new URL(rest_url_s);
				resp = objectMapper.readValue(rest_url, HPImageArchiveResponse.class);
			}catch (Exception e) {
				throw new CompletionException(e);
			}
			Objects.nonNull(resp);
			Objects.nonNull(resp.getImages());
			return resp.getImages().stream()
				.filter( potd -> !downloaded.containsKey(potd.urlbase))
				.map( potd -> {
					String imgFile = potd.urlbase + Constants.SIZE._640x360.toString() + ".jpg";
					//String imgFile = potd.fullstartdate + "_" + mkt + "_" + Constants.SIZE._640x360.toString() + ".jpg"
					try {
						URL imgUrl = new URL("http", BING_HOST, imgFile);
						String filename = potd.fullstartdate + "_" + mkt + "_" + Constants.SIZE._640x360.toString() + ".bmp";
						//String filename = org.apache.commons.io.FilenameUtils.getName(potd.urlbase) + ".bmp";
						File splashFile = new File(splashDirectory, filename);
						BmpConverter.getBmp(imgUrl, splashFile);
					} catch (Exception e) {
						throw new CompletionException(e);
					}
					return imgFile;
				})
				.count();
		});
			
	}
}
