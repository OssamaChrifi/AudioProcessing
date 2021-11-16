package fr.chrifi.audio;

import java.nio.ByteBuffer;

import javax.sound.sampled.*;

import fr.chrifi.math.Complex;
import fr.chrifi.math.FFT;

/** A container for an audio signal backed by a double buffer so as to allow floating point calculation
* for signal processing and avoid saturation effects. Samples are 16 bit wide in this implementation. */
public class AudioSignal {
	private double[] sampleBuffer; // floating point representation of audio samples
	private double dBlevel; // current signal level
	
	/** Construct an AudioSignal that may contain up to "frameSize" samples.
	* @param frameSize the number of samples in one audio frame 
	 * @throws LineUnavailableException*/
	public AudioSignal(int frameSize) throws LineUnavailableException{ 
		 sampleBuffer = new double[frameSize];
	}
	
	public double getSample(int i) {
		return sampleBuffer[i];
	}

	public void setSample(int i, double value) {
		this.sampleBuffer[i] = value;
	}

	public int getFrameSize() {
		return sampleBuffer.length;
	}

	public double getdBlevel() {
		return dBlevel;
	}

	public void setdBlevel(double dBlevel) {
		this.dBlevel = dBlevel;
	}

	/** Sets the content of this signal from another signal.
	* @param other other.length must not be lower than the length of this signal. 
	 * @throws LineUnavailableException */
	public void setFrom(AudioSignal other) throws LineUnavailableException{ 
		for(int i=0; i<getFrameSize(); i++) {
			other.setSample(i, this.sampleBuffer[i]);
		}
	}
	
	/** Fills the buffer content from the given input. Byte's are converted on the fly to double's.
	* @return false if at end of stream */
	public boolean recordFrom(TargetDataLine audioInput) {
		byte[] byteBuffer = new byte[sampleBuffer.length*2]; // 16 bit samples
		double RMS = 0;
		if (audioInput.read(byteBuffer, 0, byteBuffer.length)==-1) return false;
		for (int i=0; i<sampleBuffer.length; i++) {
			sampleBuffer[i] = ((byteBuffer[2*i]<<8)+byteBuffer[2*i+1]) / 32768.0; // big endian
			RMS += sampleBuffer[i]*sampleBuffer[i];
		}
		RMS=Math.sqrt(RMS)/sampleBuffer.length;
		this.dBlevel=20*Math.log10(RMS);
		return true;
	}
	
	/** Plays the buffer content to the given output.
	* @return false if at end of stream */
	public boolean playTo(SourceDataLine audioOutput) {
		byte[] byteBuffer = new byte[sampleBuffer.length*2];    

        for (int i=0; i<sampleBuffer.length; i++) {

            double unscaled = sampleBuffer[i] * 32768.0;

            byteBuffer[2*i] = (byte) (unscaled / 256);  
            byteBuffer[2*i+1] = (byte) (unscaled % 256); 

        }

        if (audioOutput.write(byteBuffer, 0, byteBuffer.length) == -1) return false;
        return true;
	}
	
	public Complex[] ComputeFFT(AudioSignal input) {
		Complex[] complex= new Complex[input.getFrameSize()];
		for(int i=0; i<input.getFrameSize(); i++) {
			complex[i] = new Complex(input.getSample(i),0);
		}
		complex=FFT.fft(complex);
		return complex;
	}
	
	public Complex[] ComputeDFT(AudioSignal input) {
		Complex[] complex= new Complex[input.getFrameSize()];
		for(int i=0; i<input.getFrameSize(); i++) {
			complex[i] = new Complex(input.getSample(i),0);
		}
		complex=FFT.dft(complex);
		return complex;
	}
	

	public static void main(String[] args){
		try {
			AudioSignal audio = new AudioSignal(1024);
			AudioFormat sdl = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);
			TargetDataLine audioInput = AudioSystem.getTargetDataLine(sdl);
			audioInput.open();audioInput.start();
			audio.recordFrom(audioInput);
			
			/*for(int i=0;i<audio.getFrameSize();i++) {
				System.out.println(audio.getSample(i));
			}*/
			audioInput.stop();
			audioInput.close();
		}
		catch (LineUnavailableException e) { e.printStackTrace(); }
	}
	
}
