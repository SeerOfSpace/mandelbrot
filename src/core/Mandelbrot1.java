package core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Mandelbrot1 {
	
	static int maxIterations = 800;
	static int resWidth = 1920;
	static int resHeight = 1080;
	static double zoom = 200;
	static double centerX = 0.388;
	static double centerY = 0.271;
	
	private static final double DEFAULT_SIZE = 4.0;
	private static double width, height;
	
	public static void main(String[] args) {
		BufferedImage bi = new BufferedImage(resWidth, resHeight, BufferedImage.TYPE_INT_RGB);
		width = height = DEFAULT_SIZE / zoom;
		if(resWidth >= resHeight) {
			width *= (double) resWidth / resHeight;
		} else {
			height *= (double) resHeight / resWidth;
		}
		for(int i = 0; i < resHeight; i++) {
			double cy = pixelToComplexY(i);
			for(int j = 0; j < resWidth; j++) {
				double cx = pixelToComplexX(j);
				int iter = mand(cx, cy);
				int color = colorCustom1(iter);
				bi.setRGB(j, i, color);
			}
		}
		try {
			ImageIO.write(bi, "png", new File("image.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int colorDefault(double iter) {
		int color;
		if(iter == 0) {
			color = Color.BLACK.getRGB();
		} else {
			color = Color.HSBtoRGB((float) iter / maxIterations, 1f, 1f);
		}
		return color;
	}
	
	public static int colorCustom1(double iter) {
		int color;
		if(iter == 0) {
			color = Color.BLACK.getRGB();
		} else {
			float q = (float) iter / maxIterations;
			color = Color.HSBtoRGB(0.65f+q/2f, 1.0f, q);
		}
		return color;
	}
	
	public static double pixelToComplexX(int x) {
		return (centerX - width / 2) + width / (resWidth - 1) * x;
	}
	
	public static double pixelToComplexY(int y) {
		return (centerY - height / 2) + height / (resHeight - 1) * y;
	}
	
	public static int mand(double cx, double cy) { //0 is in set, > 0 is not in set
		double zx = 0;
		double zy = 0;
		for(int i = 1; i <= maxIterations; i++) {
			double temp = zx * zx - zy * zy + cx; //multiplication real part + cx
			zy = 2 * zx * zy + cy; //multiplication imaginary part + cy
			zx = temp;
			if(zx * zx + zy * zy > 4) { //absolute > 2
				return i;
			}
		}
		return 0;
	}
	
}
