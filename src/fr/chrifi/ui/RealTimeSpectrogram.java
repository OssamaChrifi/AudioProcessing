package fr.chrifi.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class RealTimeSpectrogram extends Canvas{
	private static GraphicsContext graphics_context;
	private static int x;

	public RealTimeSpectrogram(double arg0, double arg1) {
		super(arg0, arg1);
		graphics_context=super.getGraphicsContext2D();
		graphics_context.setFill(Color.BLACK);
		graphics_context.fillText("Real Time Spectrogram",140,10);
		graphics_context.fillText("frequence",0,150);
		graphics_context.fillText("temps",170,280);
	}
	
	public static void updatData() {
		double Spectre=0;
		x++;
		WritableImage image = new WritableImage(250,250);
		PixelWriter pixel = image.getPixelWriter();
		for(int y=0; y<250; y++) {
			Spectre=Spectrogram.getSpectre()[Math.abs(Spectrogram.getSpectre().length/2-y*(int)Spectrogram.getSpectre().length/250)].abs();
			pixel.setColor(x, y, Color.gray(0.0,Spectre/200));				
		}	
		graphics_context.drawImage(image, 55, 20);
	}
}
