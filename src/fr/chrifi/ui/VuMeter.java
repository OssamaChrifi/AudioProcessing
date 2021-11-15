package fr.chrifi.ui;


import fr.chrifi.audio.AudioProcessor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VuMeter extends Canvas {
	private static GraphicsContext graphics_context;
	private static double dBLevel;

	public VuMeter() {
		super();
	}

	public VuMeter(double arg0, double arg1) {
		super(arg0,arg1);
		graphics_context = super.getGraphicsContext2D();
		graphics_context.setFill(Color.RED);
		graphics_context.fillRect(10, 35, 30, 65);
		graphics_context.setFill(Color.ORANGE);
		graphics_context.fillRect(10, 100, 30, 60);
		graphics_context.setFill(Color.GREEN);
		graphics_context.fillRect(10, 160, 30, 200);
		graphics_context.setFill(Color.BLACK);
		graphics_context.fillText(" 0", 0, 40);
		graphics_context.fillText("-5 ", 0, 70);
		graphics_context.fillText("-10 ", 0, 100);
		graphics_context.fillText("-15", 0, 130);
		graphics_context.fillText("-20", 0, 160);
		graphics_context.fillText("-25", 0, 190);
		graphics_context.fillText("-30", 0, 220);
		graphics_context.fillText("-35", 0, 250);
		graphics_context.fillText("-40", 0, 280);
		graphics_context.fillText("-45", 0, 310);
		graphics_context.fillText("-50", 0, 340);
	}
	
	/**
	 *  Creation of subclass of the animation and override its handle() method
	*/
	public static void updatData(AudioProcessor end) {
		graphics_context.clearRect(10, 35, 30, 360);
		graphics_context.setFill(Color.RED);
		graphics_context.fillRect(10, 35, 30, 65);
		graphics_context.setFill(Color.ORANGE);
		graphics_context.fillRect(10, 100, 30, 60);
		graphics_context.setFill(Color.GREEN);
		graphics_context.fillRect(10, 160, 30, 200);
		graphics_context.setFill(Color.BLACK);
		dBLevel = end.getInputSignal().getdBlevel();
		graphics_context.setFill(Color.BLUE);
		graphics_context.fillRect(10, Math.abs(dBLevel/5*30+40), 30, 10);	
		graphics_context.setFill(Color.BLACK);
		graphics_context.fillText(" 0", 0, 40);
		graphics_context.fillText("-5 ", 0, 70);
		graphics_context.fillText("-10 ", 0, 100);
		graphics_context.fillText("-15", 0, 130);
		graphics_context.fillText("-20", 0, 160);
		graphics_context.fillText("-25", 0, 190);
		graphics_context.fillText("-30", 0, 220);
		graphics_context.fillText("-35", 0, 250);
		graphics_context.fillText("-40", 0, 280);
		graphics_context.fillText("-45", 0, 310);
		graphics_context.fillText("-50", 0, 340);
	}
}

