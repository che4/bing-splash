package io.github.che4.splash.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import io.github.che4.splash.utils.P2Util;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class SplashChooser {

	public SplashChooser() {
	}

	/**
	 * Create contents of the view part.
	 * @throws IOException 
	 */
	/*
	@PostConstruct
	public void createControls(Composite parent) throws IOException {
		Bundle thisBundle = FrameworkUtil.getBundle(getClass());
		Optional<File> optDir = P2Util.findFeatureDir(thisBundle);
		if(optDir.isPresent()) {
			File featureDir = optDir.get();
			File splashFile = new File(featureDir, "splash.bmp");
			Image image = new Image(parent.getDisplay(), new FileInputStream(splashFile));
			Button button = new Button(parent ,SWT.PUSH);
			button.setImage(image);
		}
		//Set<IInstallableUnit> iiu = P2Util.findFeature(thisBundle);
		//Bundle featureBundle = thisBundle.getBundleContext().getBundle("platform:/features/io.github.che4.splash.feature");
		//URL featureUrl = new URL("platform:/base/features/io.github.che4.splash.feature");
		//File file = new File("C:\\Java\\Project-che4\\bing-splash\\bing-splash.feature\\splash.bmp");
		//Image image = new Image(parent.getDisplay(), new FileInputStream(file));
		//URL fileUrl = FileLocator.resolve(featureUrl);
		//File file = new File(fileUrl.toString(), "splash.bmp");
		//InputStream is = fileUrl.openConnection().getInputStream();
		//Image image = new Image(parent.getDisplay(), new FileInputStream(file));
		/*
		Canvas canvas = new Canvas(parent, SWT.NONE);
		// Create a paint handler for the canvas    
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
			}
		});
		*/
		//Button button = new Button(parent ,SWT.PUSH);
		//button.setImage(image);

	//}


	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
