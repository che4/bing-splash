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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridData;

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
		composite.setLayout(new GridLayout(3, true));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setText("Choose your next splash screen");
		
		Canvas canvas = new Canvas(composite, SWT.BORDER);
		GridData gd_canvas = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		gd_canvas.widthHint = 579;
		gd_canvas.heightHint = 326;
		canvas.setLayoutData(gd_canvas);
		
		
		
		Button backButton = new Button(composite, SWT.NONE);
		backButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		backButton.setBounds(3, 3, 35, 25);
		backButton.setText("\u2190");
		
		Button saveButton = new Button(composite, SWT.NONE);
		saveButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		saveButton.setBounds(41, 3, 136, 25);
		saveButton.setText("Set this image as splash");
		
		Button forwardButton = new Button(composite, SWT.NONE);
		forwardButton.setBounds(180, 3, 35, 25);
		forwardButton.setText("\u2192");
		
		
		
		final RenderHelper renderHelper = new RenderHelper(canvas);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
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
