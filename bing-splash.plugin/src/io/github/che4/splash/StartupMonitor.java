package io.github.che4.splash;

import java.util.Map;

import org.eclipse.osgi.service.datalocation.Location;

public class StartupMonitor implements org.eclipse.osgi.service.runnable.StartupMonitor {
	
	//private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(StartupMonitor.class);

	@Override
	public void update() {}

	@Override
	public void applicationRunning() {
		//LOG.info("Application started");
	}
	
	public void bindLocation(Location location, Map<String, String> prop) {
		//LOG.info("INSTALL LOCATION: {}", location.getURL());
	}

}
