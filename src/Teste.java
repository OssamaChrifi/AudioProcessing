import javax.sound.sampled.*;

public class Teste{
	public static void main(String[] args) throws LineUnavailableException {
		System.out.println("Make sound");
	    byte[] buf = new byte[2];
	    int frequency = 44100; //44100 sample points per 1 second
	    AudioFormat af = new AudioFormat((float) frequency, 16, 1, true, false);
	    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
	    sdl.open();
	    sdl.start();
	    int durationMs = 5000;
	    int numberOfTimesFullSinFuncPerSec = 441; //number of times in 1sec sin function repeats
	    for (int i = 0; i < durationMs * (float) 44100 / 1000; i++) { //1000 ms in 1 second
	      float numberOfSamplesToRepresentFullSin= (float) frequency / numberOfTimesFullSinFuncPerSec;
	      double angle = i / (numberOfSamplesToRepresentFullSin/ 2.0) * Math.PI;  // /divide with 2 since sin goes 0PI to 2PI
	      short a = (short) (Math.sin(angle) * 32767);  //32767 - max value for sample to take (-32767 to 32767)
	      buf[0] = (byte) (a & 0xFF); //write 8bits ________WWWWWWWW out of 16
	      buf[1] = (byte) (a >> 8); //write 8bits WWWWWWWW________ out of 16
	      sdl.write(buf, 0, 2);
	    }
	    sdl.drain();
	    sdl.stop();
	  }
}

/*try {
final TargetDataLine inLine = AudioIO.obtainAudioInput("Default Audio Device", 16000);
inLine.open();

final SourceDataLine outLine = AudioIO.obtainAudioOutput("Default Audio Device", 16000);
outLine.open();

final ByteArrayOutputStream out = new ByteArrayOutputStream();

Thread sourceThread = new Thread() {
	@Override public void run() {
		outLine.start();
		while(true) {
			outLine.write(out.toByteArray(), 0, out.size());
		}
	}
};

Thread targetThread = new Thread() {
	@Override public void run() {
		outLine.start();
		byte[] data = new byte[inLine.getBufferSize()/5];
		int readByte;
		while(true) {
			readByte = inLine.read(data, 0, data.length);
			out.write(data, 0, readByte);
		}
	}
};

targetThread.start();
System.out.println("Started Recording");
Thread.sleep(5000);
inLine.stop();
inLine.close();
System.out.println("End Recording");

sourceThread.start();
System.out.println("Started Playback");
Thread.sleep(5000);
outLine.stop();
outLine.close();
System.out.println("end Playback");
}
catch (LineUnavailableException e) {
// TODO Auto-generated catch block
e.printStackTrace();
} 
catch (InterruptedException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}*/