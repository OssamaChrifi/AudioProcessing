package fr.chrifi.ui;

import javax.sound.sampled.LineUnavailableException;

import fr.chrifi.audio.AudioIO;
import fr.chrifi.audio.AudioProcessor;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
	 
	 private String audioInput, audioOutput;
	 private int sampleRate, bufferSize;
	 private AudioProcessor end;
	
	 public void start(Stage primaryStage) {
		 try {
			 BorderPane root = new BorderPane();
			 root.setTop(createToolbar());
			 root.setBottom(createStatusbar());
			 root.setCenter(createMainContent());
			 root.setRight(createRightContent());
			 root.setLeft(createLeftContent());
			 Scene scene = new Scene(root,1500,800);
			 primaryStage.setScene(scene);
			 primaryStage.setTitle("The JavaFX audio processor");
			 primaryStage.show();
		 } 	
		 catch(Exception e) {e.printStackTrace();}
	}
	
	 private Node createToolbar(){
		Button button = new Button("start !");
		Button button2 = new Button("stop !");
		ComboBox<String> cb = new ComboBox<>();
		ComboBox<String> cb1 = new ComboBox<>();
		ComboBox<String> cb2 = new ComboBox<>();
		ComboBox<String> cb3 = new ComboBox<>();
		cb.getItems().addAll("Default Audion Device");
		cb1.getItems().addAll("SR_8000","SR_16000","SR_32000");
		cb2.getItems().addAll("SZ_256","SZ_512","SZ_1024");
		cb3.getItems().addAll("Default Audion Device");;
		ToolBar tb = new ToolBar(new Label("audio in : "), cb,new Label("audio out : "), cb3,
				new Separator(), cb1, new Label("Hz"), new Separator(), new Separator(), 
				new Label("Buffer_size"), cb2, button, new Label("Compute FFT"), new Separator(), button2);
		cb.setOnAction(e -> {
			if(cb.getSelectionModel().getSelectedItem() == "Default Audion Device") {
				 audioInput = "Default Audion Device";
				 audioOutput ="Default Audion Device";
			 }
		});
		cb1.setOnAction(e -> {
			if(cb1.getSelectionModel().getSelectedItem() == "SR_8000")  sampleRate = 8000;
			if(cb1.getSelectionModel().getSelectedItem() == "SR_16000")  sampleRate = 16000;
			if(cb1.getSelectionModel().getSelectedItem() == "SR_32000")  sampleRate = 32000;
		});
		cb2.setOnAction(e -> {
			if(cb2.getSelectionModel().getSelectedItem() == "SZ_256")  bufferSize = 256;
			if(cb2.getSelectionModel().getSelectedItem() == "SZ_512")  bufferSize = 512;
			if(cb2.getSelectionModel().getSelectedItem() == "SZ_1024")  bufferSize = 1024;
		});
		button.setOnAction(new StartAudio());
		Thread stopper = new Thread() {
			public void run() {
				end.getAudioInput().stop();end.getAudioInput().close();
				end.getAudioOutput().stop();end.getAudioOutput().close();
				SignalView.timer.stop();
				SignalView.getSeries().getData().clear();
				Spectrogram.getSeries().getData().clear();
			 }
		};
		button2.setOnAction(e -> { stopper.start(); });
		return (Node) tb;
	 }

	 private Node createStatusbar(){
		 HBox statusbar = new HBox();
		 statusbar.getChildren().addAll(new Label("Name:"), new TextField("CHRIFI Ossama"));
		 return (Node) statusbar;
	 }

	 private Node createMainContent(){
		 Group g = new Group();
		//defining the axes
		 final NumberAxis xAxis = new NumberAxis(0,1024,100);
	     final NumberAxis yAxis = new NumberAxis(-1,1,0.1);
	     xAxis.setLabel("AudioSignal");
	     yAxis.setLabel("x[n]");
	     //creating the chart
	     final SignalView lineChart = new SignalView(xAxis,yAxis);
	     final VuMeter canvas = new VuMeter(30,500);
	     lineChart.getData().add(SignalView.getSeries());
	     
	     g.getChildren().add(canvas);
		 g.getChildren().add(lineChart);
		 
		 return (Node) g;
	 }
	 
	 private Node createRightContent(){
		 Group g = new Group();
		//defining the axes
	     final NumberAxis yaxis = new NumberAxis(0,40,5);
	     final NumberAxis xaxis = new NumberAxis(0,4000/3,500/3);
	     xaxis.setLabel("f");
	     yaxis.setLabel("Amplitude");
	     //creating the chart
	     final Spectrogram spectre = new Spectrogram(xaxis, yaxis);
	     spectre.getData().add(Spectrogram.getSeries());
		 g.getChildren().add(spectre);
		 
		 return (Node) g;
	 }
	 
	 private Node createLeftContent(){
		 Group g = new Group();
	     //creating the RealTimespectrogram
	     final RealTimeSpectrogram realTimespectre = new RealTimeSpectrogram(310, 300);
		 g.getChildren().add(realTimespectre);
		 
		 return (Node) g;
	 }
	 
	 
	 /**Record and play audio
	  */
	 private class StartAudio implements EventHandler<ActionEvent>{		 
		@Override
		 public void handle(ActionEvent e) {
			try {
				System.out.println("Audio processing begin !"); 
				end=AudioIO.startAudioProcessing(audioInput, audioOutput,sampleRate, bufferSize);
				SignalView.setEnd(end);
				SignalView.timer.start();
				end.terminateAudioThread(2000);
				System.out.println("Audio processing stop !");	
				} 
			catch (LineUnavailableException e1) { e1.printStackTrace(); }
			catch (InterruptedException e1) { e1.printStackTrace(); }				 
		}
	 } 
	 
	 
}

