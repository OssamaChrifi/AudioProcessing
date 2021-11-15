package fr.chrifi.ui;

import fr.chrifi.audio.AudioProcessor;
import javafx.animation.*;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SignalView extends LineChart<Number,Number>{
	private static XYChart.Series<Number, Number> series;
	private static AudioProcessor end;
	static AnimationTimer  timer;

	public SignalView(Axis<Number> arg0, Axis<Number> arg1) {
		super(arg0, arg1);
		setTitle("Audio Signal graph");
		//defining a series
		series = new XYChart.Series<Number, Number>();
        series.setName("Input Signal");
		timer = new MyTimer();
	}


	public static void setEnd(AudioProcessor end) {
		SignalView.end = end;
	}


	public static Series<Number, Number> getSeries() {
		return series;
	}


	/** Creation of subclass of the animation and override its handle() method*/
	public class MyTimer extends AnimationTimer{
		private long prevTime;
		@Override
		public void handle(long now) {
			long dt = now-prevTime;
			if(dt>1e7) {
				prevTime = now;
				updatData();
				VuMeter.updatData(end);
				Spectrogram.updatData(end);
				RealTimeSpectrogram.updatData();
			}
		}
	}
	
	/**
	 *  Creation of subclass of the animation and override its handle() method
	 */
	public void updatData() {
		series.getData().clear();
		for(int i=0; i<end.getInputSignal().getFrameSize(); i++) {
			series.getData().add(new XYChart.Data<Number, Number>(i,end.getInputSignal().getSample(i)));
		}
	}
}


