package io.github.che4.splash.bing;

import java.util.concurrent.CompletableFuture;

public interface IBingSplashServiceAsync {
	
	/**
	 * Immediately (without blocking) return a CompletableFuture to later retrieve a
	 * returned Timezone instance
	 * 
	 * @param latitude the latitude of the location to get the timezone for
	 * @param longitude the longitude of the location to get the timezone for
	 * @return the CompletableFuture<Timezone> to provide later access to an
	 * instance of Timezone information for the given latitude and longitude.  Should
	 * not return <code>null</code>.
	 */
	CompletableFuture<HPImageArchiveResponse> getImageArchiveDescriptionAsync();

}
