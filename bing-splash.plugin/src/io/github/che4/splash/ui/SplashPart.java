package io.github.che4.splash.ui;

import java.io.File;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import io.github.che4.splash.E4Constants;
import io.github.che4.splash.bing.BingPhotoProvider;
import io.github.che4.splash.utils.P2Util;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import swing2swt.layout.BorderLayout;

public class SplashPart {

	public SplashPart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));
		
		Button backButton = new Button(composite, SWT.NONE);
		backButton.setLayoutData(BorderLayout.WEST);
		backButton.setText("<---");
		
		Button forwardButton = new Button(composite, SWT.NONE);
		forwardButton.setLayoutData(BorderLayout.EAST);
		forwardButton.setText("--->");
		
		
		Button saveButton = new Button(composite, SWT.NONE);
		saveButton.setLayoutData(BorderLayout.SOUTH);
		saveButton.setText("Set this image as splash");
		
		Canvas canvas = new Canvas(composite, SWT.NONE);
		canvas.setLayoutData(BorderLayout.CENTER);
		
		final RenderHelper renderHelper = new RenderHelper(canvas);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setLayoutData(BorderLayout.NORTH);
		lblNewLabel.setText("Choose your next splash screen");
		
		Bundle thisBundle = FrameworkUtil.getBundle(getClass());
		Optional<File> optDir;
		try {
			optDir = P2Util.findFeatureDir(thisBundle);
		} catch (Exception e) {		
			//FIXME clear this
			File featureDir = new File("C:\\Java\\Project-che4\\bing-splash\\bing-splash.feature");
			optDir = Optional.of(featureDir);
			//optDir = Optional.empty();
		}
		if(optDir.isPresent()) {
				File featureDir = optDir.get();				
				final File splashDir = new File(featureDir, E4Constants.DOWNLOAD_DIR);				
				splashDir.mkdirs();
				renderHelper.setFeatureDir(featureDir);
				renderHelper.setImageDir(splashDir);
				
				forwardButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {						
						//MessageDialog.openInformation(parent.getShell(), "Move forward", "Avanti!!!");
						renderHelper.next();
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}
				});
				
				backButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {						
						//MessageDialog.openInformation(parent.getShell(), "Move forward", "Avanti!!!");
						renderHelper.previous();
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}
				});
				
				saveButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						renderHelper.saveSplash();
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}
				});
				
				BingPhotoProvider bpp = new BingPhotoProvider();
				bpp.setSplashDirecotry(splashDir);
				bpp.updatePhotos(7)
					.thenAcceptAsync( counts -> {
						//renderHelper.setFeatureDir(featureDir);
					})
					.exceptionally(e -> {
						MessageDialog.openWarning(parent.getShell(), 
								"Update from bing.com failed",
								"New images aren't downloaded.\r\n" + e.getMessage());
						return null;
					});
				renderHelper.render(new File(featureDir, E4Constants.SPLASH_FILE_NAME));
		}else {
			MessageDialog.openError(parent.getShell(), 
				"Failed",
				"Are you runing this plugin from development environment?? See comments in io.github.splash.ui.SplashPart");
		}
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
}
