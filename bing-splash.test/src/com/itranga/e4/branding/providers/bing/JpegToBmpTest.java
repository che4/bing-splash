package com.itranga.e4.branding.providers.bing;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.junit.Test;

public class JpegToBmpTest {

	@Test
	public void test1() throws IOException {
		getBmp();
	}
	
	private void getBmp() throws IOException {
		//File input = new File("C:\\Users\\Kalinin_DP\\Documents\\Books\\scala\\new_projects\\com.itranga.e4.branding.test\\bing.com\\img_1.jpg");
		//BufferedImage image = ImageIO.read(input);
		
		BufferedImage image = ImageIO.read(new URL("http://bing.com/az/hprichbg/rb/SplitVestibule_DE-DE13382955266_1920x1080.jpg"));
		
		/*
		Dimension splashDimension = new Dimension(606, 326);
		Dimension originalDimension = new Dimension(image.getWidth(), image.getHeight());
		Dimension targetDimension = getScaledDimension(originalDimension, splashDimension); 
		Image targetImage = image.getScaledInstance(targetDimension.width, targetDimension.height, Image.SCALE_DEFAULT);
		*/
		File output;
		BufferedImage splashImage = getScaledImage(image, 606, 326);
		//boolean result = ImageIO.write(splashImage, "BMP", output);
		//System.out.println("Saving result: " + result);
	}
	
	public static BufferedImage getScaledImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		Objects.requireNonNull(originalImage);
		Dimension splashDimension = new Dimension(targetWidth, targetHeight);
		Dimension originalDimension = new Dimension(originalImage.getWidth(null), originalImage.getHeight(null));
		
		Dimension targetDimension = getScaledDimension(originalDimension, splashDimension); 
		
		System.out.println("Image ratio: " + targetDimension.width + "x" + targetDimension.height);
		
		//Image scaledImage = originalImage.getScaledInstance(targetDimension.width, targetDimension.height, Image.SCALE_DEFAULT);
		
		//BufferedImage splashImage = new BufferedImage(targetDimension.width, targetDimension.height, BufferedImage.TYPE_BYTE_BINARY);
		BufferedImage bmpImage = new BufferedImage(originalDimension.width, originalDimension.height, BufferedImage.TYPE_INT_BGR);
		for (int y = 0; y < originalImage.getHeight(); ++y)
			for (int x = 0; x < originalImage.getWidth(); ++x)
				bmpImage.setRGB(x, y, originalImage.getRGB(x, y));
		// Draw the image on to the buffered image
		
		BufferedImage splashImage = new BufferedImage(targetDimension.width, targetDimension.height, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = splashImage.createGraphics();
		g.drawImage(bmpImage, 0, 0, targetDimension.width, targetDimension.height, null);
		/*
		AffineTransform at = AffineTransform.getScaleInstance(
				targetDimension.getWidth()/originalDimension.getWidth(),
				targetDimension.getHeight()/originalDimension.getHeight()
		);
		g.drawRenderedImage(bmpImage, at);
		*/
		g.dispose();
		return splashImage;
	}
	
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		// first check if we need to scale width
		if (original_width > bound_width) {
			//scale width to fit
			new_width = bound_width;
			//scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > bound_height) {
			//scale height to fit instead
			new_height = bound_height;
			//scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}
}
