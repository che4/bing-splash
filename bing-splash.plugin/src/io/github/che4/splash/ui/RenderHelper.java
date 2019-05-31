package io.github.che4.splash.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;

import io.github.che4.splash.E4Constants;

public class RenderHelper {
	private final Canvas canvas;
	private File featureDir;
	private File imageDir;
	private int counter;
	private File chosenFile;
	
	public RenderHelper(final Canvas canvas) {
		this.canvas = canvas;
	}
	
	public void setFeatureDir(final File featureDir) {
		Objects.requireNonNull(featureDir);
		if(!featureDir.isDirectory()) throw new IllegalArgumentException("Directory expected, but file was provided");
		this.featureDir = featureDir;
	}
	public void setImageDir(final File imageDir) {
		Objects.requireNonNull(featureDir);
		if(!imageDir.isDirectory()) throw new IllegalArgumentException("Directory expected, but file was provided");
		this.imageDir = imageDir;
	}
	
	public void render(File file) {		
		try {
			Image image = new Image(canvas.getParent().getDisplay(), new FileInputStream(file));
			//canvas.getParent().getDisplay().syncExec( () -> {
				canvas.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(image, 0, 0);
					}
				});
				canvas.setSize(image.getBounds().width, image.getBounds().height);
				canvas.redraw();
			//});
		} catch (FileNotFoundException e) {
			MessageDialog.openError(canvas.getParent().getShell(), 
					"Splash file not found",
					"File not found \r\n" + e.getMessage());
		}
	}
	
	public void next() {
		Objects.requireNonNull(imageDir);
		List<File> fileList = Arrays.asList(
				imageDir.listFiles( file -> file.getName().toLowerCase().endsWith(".bmp")));
		try {
			chosenFile = fileList.get(counter++);
			render(chosenFile);
		}catch (ArrayIndexOutOfBoundsException e) {
			counter = 0;
			chosenFile = null;
			File splashFile = new File(featureDir, E4Constants.SPLASH_FILE_NAME);
			render(splashFile);
		}
	}
	

	public void previous() {
		Objects.requireNonNull(imageDir);
		List<File> fileList = Arrays.asList(
				imageDir.listFiles( file -> file.getName().toLowerCase().endsWith(".bmp")));
		try {
			chosenFile = fileList.get(fileList.size()-1 - counter++);
			render(chosenFile);
		}catch (ArrayIndexOutOfBoundsException e) {
			counter = 0;
			chosenFile = null;
			File splashFile = new File(featureDir, E4Constants.SPLASH_FILE_NAME);
			render(splashFile);
		}		
	}
	
	public void saveSplash() {
		Objects.requireNonNull(featureDir);
		if( chosenFile != null ) {
			File splashFile = new File(featureDir, E4Constants.SPLASH_FILE_NAME);
			try {
				FileUtils.copyFile(chosenFile, splashFile);
				MessageDialog.openInformation(canvas.getParent().getShell(), 
						"New splash installed", "After restart you will see a new splash");
			} catch (IOException e) {
				MessageDialog.openError(canvas.getParent().getShell(), 
						"Failed to save splash",
						"Error while copy file.\r\n" + e.getMessage());
			}
		}
	}
}
