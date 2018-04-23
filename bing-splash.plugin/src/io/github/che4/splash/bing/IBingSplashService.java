package io.github.che4.splash.bing;


public interface IBingSplashService {

	/**
	 * Get a Bing's photo of the day.
	 * 
	 * @param latitude the latitude of the location to get the timezone for
	 * @param longitude the longitude of the location to get the timezone for
	 * @return the Timezone information for the given latitude and longitude.  Should
	 * return <code>null</code> if the latitude or longitude are nonsensical (e.g. negative values)
	 */
	HPImageArchiveResponse getImageArchiveDescription();
}
