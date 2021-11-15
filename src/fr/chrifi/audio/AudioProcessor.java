package fr.chrifi.audio;

import java.util.NoSuchElementException;

import javax.sound.sampled.*;

/** The main audio processing class, implemented as a Runnable so
* as to be run in a separated execution Thread. */
public class AudioProcessor implements Runnable {

private AudioSignal inputSignal, outputSignal;
private TargetDataLine audioInput;
private SourceDataLine audioOutput;
private boolean isThreadRunning; // makes it possible to "terminate" thread

/** Creates an AudioProcessor that takes input from the given TargetDataLine, and plays back
* to the given SourceDataLine.
* @param frameSize the size of the audio buffer. The shorter, the lower the latency. 
 * @throws LineUnavailableException */
	public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) throws LineUnavailableException {
		this.audioInput = audioInput;
		this.audioOutput = audioOutput;	
		inputSignal=new AudioSignal(frameSize);
		outputSignal=new AudioSignal(frameSize);
	}

/** Audio processing thread code. Basically an infinite loop that continuously fills the sample
* buffer with audio data fed by a TargetDataLine and then applies some audio effect, if any,
* and finally copies data back to a SourceDataLine.*/
	@Override
	public void run() {
		isThreadRunning = true;
		while (isThreadRunning) {
			try {
				inputSignal.recordFrom(audioInput);
				inputSignal.setFrom(outputSignal);
				outputSignal.playTo(audioOutput);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

/** Tells the thread loop to break as soon as possible. This is an asynchronous process. 
 * @throws InterruptedException */
	public void terminateAudioThread(int time) throws InterruptedException { 
		Thread.sleep(time);
	}



public AudioSignal getInputSignal() {
	return inputSignal;
}

public void setInputSignal(AudioSignal inputSignal) {
	this.inputSignal = inputSignal;
}

public AudioSignal getOutputSignal() {
	return outputSignal;
}

public void setOutputSignal(AudioSignal outputSignal) {
	this.outputSignal = outputSignal;
}

public TargetDataLine getAudioInput() {
	return audioInput;
}

public void setAudioInput(TargetDataLine audioInput) {
	this.audioInput = audioInput;
}

public SourceDataLine getAudioOutput() {
	return audioOutput;
}

public void setAudioOutput(SourceDataLine audioOutput) {
	this.audioOutput = audioOutput;
}

public boolean isThreadRunning() {
	return isThreadRunning;
}

public void setThreadRunning(boolean isThreadRunning) {
	this.isThreadRunning = isThreadRunning;
}

	/* an example of a possible test code */
	public static void main(String[] args) {
		TargetDataLine inLine = null;
		SourceDataLine outLine=null;
		try {
			inLine = AudioIO.obtainAudioInput("Default Audio Device", 16000);
			inLine.open();
			
			outLine = AudioIO.obtainAudioOutput("Default Audio Device", 16000);
			outLine.open();
			AudioProcessor as = new AudioProcessor(inLine, outLine, 1024);
			inLine.start(); outLine.start();
			new Thread(as).start();
			as.terminateAudioThread(5000);
			inLine.stop();inLine.close();
			outLine.stop();outLine.close();
			System.out.println("A new thread has been created!");
		}
		catch (LineUnavailableException e) { e.printStackTrace(); } 
		catch (InterruptedException e) { e.printStackTrace(); }
		catch (NoSuchElementException e) { e.printStackTrace(); }
	}
}

