package fr.chrifi.ui;

import fr.chrifi.audio.AudioProcessor;
import fr.chrifi.math.Complex;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Spectrogram extends LineChart<Number,Number>{
	private static Complex[] spectre;
	private static Series<Number, Number> series;
	
	public Spectrogram(Axis<Number> arg0, NumberAxis yaxis) {
		super(arg0, yaxis);
		setTitle("Spectrogram graph");
		//defining a series
		series = new XYChart.Series<Number, Number>();
        series.setName("Input Signal");	
	}
	
	public static void updatData(AudioProcessor end) {
		spectre=end.getInputSignal().ComputeFFT(end.getInputSignal());
		double FFT_BIN_WIDTH = end.getInputSignal().getFrameSize()/spectre.length;
		double Frequency,Magnitude;
		series.getData().clear();
		for (int i = 0; i < spectre.length; i++) { 
			Frequency = i * FFT_BIN_WIDTH;
			Magnitude = spectre[i].abs();
			series.getData().add(new XYChart.Data<Number, Number>(Frequency,Magnitude));
		}
	}

	public static Complex[] getSpectre() {
		return spectre;
	}

	public static Series<Number, Number> getSeries() {
		return series;
	}

}
