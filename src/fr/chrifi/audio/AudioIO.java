package fr.chrifi.audio;

import java.util.Arrays;

import javax.sound.sampled.*;

/** A collection of static utilities related to the audio system. */
public class AudioIO {

/** Displays every audio mixer available on the current system. */
	public static void printAudioMixers() {
		System.out.println("Mixers:");
		Arrays.stream(AudioSystem.getMixerInfo()).forEach(e -> System.out.println("- name=\"" + e.getName()
		+ "\" description=\"" + e.getDescription() + " by " + e.getVendor() + "\""));
}

/** @return a Mixer.Info whose name matches the given string.
Example of use: getMixerInfo("Macbook default output") */
	public static Mixer.Info getMixerInfo(String mixerName) {
		// see how the use of streams is much more compact than for() loops!
		return Arrays.stream(AudioSystem.getMixerInfo()).filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
	}

/** Return a line that's appropriate for recording sound from a microphone.
* Example of use:
* TargetDataLine line = obtainInputLine("USB Audio Device", 8000);
* @param mixerName a string that matches one of the available mixers.
 * @throws LineUnavailableException 
* @see AudioSystem.getMixerInfo() which provides a list of all mixers on your system.
*/
	public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) throws LineUnavailableException{
		/*Mixer.Info l1 = getMixerInfo(mixerName);
		Mixer l2 = AudioSystem.getMixer(l1);
		Line.Info[] l3 = l2.getTargetLineInfo();
		if(!l1.equals(l3)) System.out.println("line incompatible");
		AudioFormat sdl = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);
		TargetDataLine AudioInput = AudioSystem.getTargetDataLine(sdl, l1);
		return AudioInput;*/
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if(!AudioSystem.isLineSupported(info)) System.err.println("Line incompatible");
		TargetDataLine targetdataline = (TargetDataLine)AudioSystem.getLine(info);
		return targetdataline;
	}

/** Return a line that's appropriate for playing sound to a loudspeaker. 
 * @throws LineUnavailableException */
	public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) throws LineUnavailableException{
		/*Mixer.Info son = getMixerInfo(mixerName);
		Mixer l2 = AudioSystem.getMixer(son);
		Line.Info[] l3 = l2.getTargetLineInfo();
		if(!son.equals(l3)) System.out.println("line incompatible");
		AudioFormat sdl = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);
		SourceDataLine AudioOutput = AudioSystem.getSourceDataLine(sdl, son);
		return AudioOutput;*/
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		if(!AudioSystem.isLineSupported(info)) System.err.println("Line incompatible");
		SourceDataLine sourcedataline = (SourceDataLine)AudioSystem.getLine(info);
		return sourcedataline;
	}
	
	public static AudioProcessor startAudioProcessing(String inputMixer, String outputMixer,int sampleRate, int frameSize) throws LineUnavailableException{
		TargetDataLine inLine = null;
		SourceDataLine outLine=null;
		inLine = AudioIO.obtainAudioInput(inputMixer, sampleRate);
		inLine.open();
			
		outLine = AudioIO.obtainAudioOutput(outputMixer, sampleRate);
		outLine.open();
		AudioProcessor as = new AudioProcessor(inLine, outLine, frameSize);
		inLine.start(); outLine.start();
		new Thread(as).start();
		return as;
	}

	public static void stopAudioProcessing(AudioProcessor as, int time) throws InterruptedException{
		as.terminateAudioThread(time);
		as.getAudioInput().stop();as.getAudioInput().close();
		as.getAudioOutput().stop();as.getAudioOutput().close();
	}
	
	public static void main(String[] args){
		printAudioMixers();
	}
	
}
