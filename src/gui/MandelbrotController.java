package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import core.Mandelbrot;
import core.Mandelbrot.ColorMode;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.util.converter.NumberStringConverter;

@SuppressWarnings("unused")
public class MandelbrotController {
	
	@FXML private TextField textFieldX;
	@FXML private Slider sliderX;
	@FXML private TextField textFieldY;
	@FXML private Slider sliderY;
	@FXML private TextField textFieldZ;
	@FXML private Slider sliderZ;
	@FXML private TextField textFieldI;
	@FXML private Slider sliderI;
	@FXML private TextField resX;
	@FXML private TextField resY;
	@FXML private ChoiceBox<ColorMode> colorMode;
	@FXML private Pane imageParent;
	@FXML private ImageView imageView;
	@FXML private Button buttonSave;
	@FXML private Button buttonStop;
	
	private Mandelbrot mandelbrot;
	
	@FXML
	private void initialize() {
		imageView.fitWidthProperty().bind(imageParent.widthProperty());
		imageView.fitHeightProperty().bind(imageParent.heightProperty());
		imageView.fitWidthProperty().addListener(e -> {
			centerImage();
		});
		imageView.fitHeightProperty().addListener(e -> {
			centerImage();
		});
		resX.textProperty().addListener(e -> {
			Platform.runLater(() -> {
				centerImage();
			});
		});
		resY.textProperty().addListener(e -> {
			Platform.runLater(() -> {
				centerImage();
			});
		});
		
		colorMode.getItems().addAll(ColorMode.values());
		colorMode.setValue(ColorMode.Default);
		
		textSliderBind(textFieldX, sliderX);
		textSliderBind(textFieldY, sliderY);
		textSliderBind(textFieldZ, sliderZ);
		textSliderBind(textFieldI, sliderI);
		//textFieldI.textProperty().addListener((e, oldVal, newVal) -> {
			//if (!newVal.matches("[^\\.]")) {
	            //textFieldI.setText(newVal.replaceAll("\\..*", ""));
	        //}
		//});
		setupRenderListeners(textFieldX, textFieldY, textFieldZ, textFieldI, resX, resY);
		colorMode.valueProperty().addListener(e -> {
			render();
		});
		
		buttonSave.setOnAction(e -> {
			save();
		});
		
	}
	
	public void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);
            
        }
    }
	
	private void textSliderBind(TextField textField, Slider slider) {
		Bindings.bindBidirectional(textField.textProperty(), slider.valueProperty(), new NumberStringConverter());
		Pattern pattern1 = Pattern.compile("[\\d.-]");
		Pattern pattern2 = Pattern.compile("[^\\d.-]");
		textField.textProperty().addListener((e, oldVal, newVal) -> {
			/*
			Matcher matcher1 = pattern1.matcher(newVal);
			Matcher matcher2 = pattern2.matcher(newVal);
			if (!matcher1.matches()) {
				textField.setText(matcher2.replaceAll(""));
	        }
	        */
		});
	}
	
	private void setupRenderListeners(TextField... arr) {
		for(TextField tf : arr) {
			tf.textProperty().addListener(e -> {
				render();
			});
		}
	}
	
	private static double[] parseDoubles(TextField... arr) {
		double[] result = new double[arr.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = Double.parseDouble(arr[i].getText());
		}
		return result;
	}
	
	private static int[] parseInts(TextField... arr) {
		int[] result = new int[arr.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(arr[i].getText());
		}
		return result;
	}
	
	private void render() {
		double[] dvals = parseDoubles(textFieldX, textFieldY, textFieldZ, textFieldI);
		int[] ivals = parseInts(resX, resY);
		mandelbrot = new Mandelbrot(dvals[0], dvals[1], dvals[2], (int) dvals[3], ivals[0], ivals[1], colorMode.getValue());
		BufferedImage bi = mandelbrot.render();
		this.bi = bi;
		imageView.setImage(convertToFxImage(bi));
	}
	
	private static Image convertToFxImage(BufferedImage bi) {
	    WritableImage wr = null;
	    if (bi != null) {
	        wr = new WritableImage(bi.getWidth(), bi.getHeight());
	        PixelWriter pw = wr.getPixelWriter();
	        for (int x = 0; x < bi.getWidth(); x++) {
	            for (int y = 0; y < bi.getHeight(); y++) {
	                pw.setArgb(x, y, bi.getRGB(x, y));
	            }
	        }
	    }
	    
	    return new ImageView(wr).getImage();
	}
	
	private BufferedImage bi;
	
	private void save() {	
		try {
			ImageIO.write(bi, "png", new File("image.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
