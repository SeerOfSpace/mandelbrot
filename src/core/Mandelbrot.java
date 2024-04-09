package core;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Mandelbrot {
	
	private final double DEFAULT_SIZE = 4.0;
	
	private int maxIterations;
	private int resWidth;
	private int resHeight;
	private double zoom;
	private double centerX;
	private double centerY;
	private double width, height;
	private ColorMode colorMode;
	private boolean stop;
	
	public enum ColorMode {
		Default,
		Greyscale,
		Custom1
	}
	
	public Mandelbrot() {
		this(0, 0, 0, 100, 600, 600, ColorMode.Default);
	}
	
	public Mandelbrot(double centerX, double centerY, double zoom, int maxIterations, int resWidth, int resHeight, ColorMode colorMode) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.zoom = zoom;
		this.maxIterations = maxIterations;
		this.resWidth = resWidth;
		this.resHeight = resHeight;
		this.colorMode = colorMode;
	}
	
	public BufferedImage render() {
		stop = false;
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
				if(stop) {
					return null;
				}
				double cx = pixelToComplexX(j);
				int iter = mand(cx, cy);
				int color;
				switch(colorMode) {
					case Greyscale: color = colorGreyscale(iter); break;
					case Custom1: color = colorCustom1(iter); break;
					case Default: ;
					default: color = colorDefault(iter);
				}
				bi.setRGB(j, i, color);
			}
		}
		return bi;
	}
	
	private double pixelToComplexX(int x) {
		return (centerX - width / 2) + width / (resWidth - 1) * x;
	}
	
	private double pixelToComplexY(int y) {
		return (centerY - height / 2) + height / (resHeight - 1) * y;
	}
	
	private int mand(double cx, double cy) { //0 is in set, > 0 is not in set
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
	
	private int colorDefault(double iter) {
		int color;
		if(iter == 0) {
			color = Color.BLACK.getRGB();
		} else {
			float q = (float) iter / maxIterations;
			color = Color.HSBtoRGB(q, 1f, 1f);
		}
		return color;
	}
	
	private int colorGreyscale(double iter) {
		int color;
		if(iter == 0) {
			color = Color.BLACK.getRGB();
		} else {
			float q = (float) iter / maxIterations;
			color = Color.HSBtoRGB(0f, 0f, q);
		}
		return color;
	}
	
	private int colorCustom1(double iter) {
		int color;
		if(iter == 0) {
			color = Color.BLACK.getRGB();
		} else {
			float q = (float) iter / maxIterations;
			color = Color.HSBtoRGB(0.65f+q/2f, 1.0f, q);
		}
		return color;
	}
	
	public void stop() {
		stop = true;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public int getResWidth() {
		return resWidth;
	}

	public void setResWidth(int resWidth) {
		this.resWidth = resWidth;
	}

	public int getResHeight() {
		return resHeight;
	}

	public void setResHeight(int resHeight) {
		this.resHeight = resHeight;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	public ColorMode getColorMode() {
		return colorMode;
	}

	public void setColorMode(ColorMode colorMode) {
		this.colorMode = colorMode;
	}
	
}