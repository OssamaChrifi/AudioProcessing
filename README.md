# AudioProcessing

### 1 What this lab is about

We want to build an application that can record a sound from a microphone in
real time, display the wave form and the spectrum on a chart, apply some effect
(like an echo chamber), and play it back to the headphone plug.
The general app design could look like in Figure 1.

### 2 A simple User Interface with JavaFX

#### 2.1 JavaFX vs Swing
This series of Labs is based on the JavaFx framework, a set of dedicated Java
libraries that will allow you to build beautifully styled user interfaces (UI).
JavaFX has become a neat, modern and efficient replacement to the Swing API

If you have been used to Swing components, you might find the philosophy
behind JavaFX quite familiar at first, but this is merely the tip of the iceberg!
JavaFX can do much more than Swing does, from fluid animations to CSS style
sheet based styling to 3D shapes and transforms. Above all, JavaFX is hardware
accelerated on most platform, either through DirectX on Windows, or through
OpenGL on OS X or Linux. With all these features in its bag, it is thus quite
easy to understand why JavaFX is slowly superseding Swing as the reference
framework for modern UI’s.
Building a User Interface in JavaFX revolves mostly around creating a component tree made of nodes (branches and leaves), where leaves can be widgets
(buttons and co), shapes (lines, rectangle, etc), text (with tons of styling options), images, sound or videos, charts and tables, and branches are actually
groups thereof (e.g., menu, toolbars, complex drawings), see Figure 2. There
are also special nodes that support dynamic processing, like transforms (rotations, translations) and animation effects (e.g., you want to make a 3D box
rotate for 3 seconds), and every visible node can have an event listener attached
to it, just as was already the case with Swing. Finally, every component in the
tree supports a CSS based styling scheme, with the usual ”classes” and ”id” parameters of CSS styling. This makes it possible to split appearance issues from
functionality issues, and have someone specialized in graphic design take care
of the UI appearance while developers are busy designing the interface itself.

#### 2.1 Running a JavaFX app from the command line

Open a shell (or a ”Command” or a ”Terminal”, depending on your OS), and go
to where your code is located. Change to the folder where all compiled classes
are located (e.g. ”bin” when working with Eclipse), and then run the following
command (everything on a single line):

```bash
java --module-path path-to-javafx-libs --add-modules ALL-MODULE-PATH application.Main
```
where ”path-to-javafx-libs” has to replaced by the path to your javafx libraries (remember? you add to write down somewhere where this folder is
located...)
Hint: you may want to put the whole command above in a file, save this file
as — for example — run.bat (or run.sh if you are working on Linux or MacOS),
and simply execute your application by typing ”run.bat” from the command
line. This will save you some time!

### 3 Audio processing
#### 3.1 Code architecture
You will now write your code inside java packages according to the following
guidelines:
* the ”ui” package is responsible for everything that has to be displayed on
the screen as well as mouse event handling: Main (main JavaFX class),
SignalView (signal visualization), VuMeter (dB level monitoring), Spectrogram, etc.
* the ”audio” package is dedicated to audio processing classes, e.g., AudioSignal (signal container), AudioProcessor (audio I/O processor), and
AudioIO (hub for I/O resources, e.g., microphones, headphones, etc).
* the ”audio.effect” subpackage contained specialized audio effects, e.g., the
* the ”math” package comprises classes that handle math operations, e.g.,
Complex numbers and Fast Fourier Transform.

#### 3.2 Basic audio processing with Java
There are two java packages dedicated to audio operations:
* the javax.sound.sampled package, for audio I/O
* the javax.sound.midi package, for MIDI4
related development
We will stick to the javax.sound.sampled package from now on. Among the
numerous classes inside this package, only a couple is really useful to scrutinize
into:
* SourceDataLine: an audio rendering device (an audio line to which data
may be written, e.g., a headphone)
* TargetDataLine: an audio capture device (a line from which audio data
can be read, like a microphone)
* Mixer : an audio device with one or more (input or output) lines. You can
think of a Mixer as a part of a soundcard. Usually there is one Mixer for
audio outputs (headphones), and another for audio inputs (microphones).
* Mixer.Info and Line.Info : represent information about an audio mixer
or an audio line, including the product’s name, version, and vendor (for
example : ”USB Audio Device” or ”External Headphones”)
* AudioFormat : specifies the sample rate, sample size in bits, number of
channels, etc
* AudioSystem: acts as the entry point to obtain audio resources. It is
where most things start... since with no resource you cannot do anything.

The basic process when one wants to play a sound is more or less the following
one:

1. Obtain a SourceDataLine, either the default one (usually connected to
the headphone plug), or for a specific mixer if your laptop is equipped
with more than one soundcard or has multiple audio outputs ; to begin
with, you may want to simply use:
AudioSystem.getTargetDataLine(AudioFormat)
where AudioFormat may be initialized with a sample rate of 8000Hz, 8
bits per sample, and a ”monophonic” (one channel) configuration as in:
new AudioFormat(8000, 8, 1, true, true)
This will give you the default audio output, and if you are lucky, this
corresponds to the embedded loudspeaker, or the default headphone plug
:) If you cannot here anything, do not panic, this just means your default
audio output is not the one you wanted and you will have to write a few
more lines of code to correct this (see below, AudioIO class).
2. call open() and then start() on the SourceDataLine you just obtained.
3. Once you have a working SourceDataLine, it is enough to (periodically)
fill a buffer of bytes with a sound wave, and then write this buffer to the
audio output by means of the SourceDataLine.write() method.
The same process in the reverse way allows you to record sound from a
microphone using a TargetDataLine.

#### 3.3 Populating the audio package
We have to write three classes for our application to properly process audio:
* AudioSignal: this is a generic container for audio samples represented
as double’s ; if offers methods to modify sample values, get the value of
particular sample, compute the level of the signal in dB.
* AudioProcessor : the main audio processor that offers methods to record
audio signals from a microphone, play signals to a headphone, and compute the FFT of the input signal ; it also offers the ability to run concurrently as a Thread (question: why is this crucial when doing audio inside
a graphic application?)
* AudioIO : a ”hub” offering methods to retrieve audio lines (microphones,
headphones, etc), and which knows how to start the whole audio processing.

### 4 Connecting the audio part with JavaFX
If everything inside the audio package works as expected, it is time to connect
it to classes inside the ”ui” package. We will be much less explicit here, as if
you have reached this point this probably means you may have gained sufficient
autonomy to design things on your own.
There is a first series of things to be done to make it possible for the user
to set audio parameters (sample rate, audio inputs and outputs, etc) and start
audio processing directly from the JavaFX interface: this is the role of the various widgets in the Toolbar, see Figure 1. Here it will prove useful to resort
not only to Button’s, but also to ComboBox’s, using the various static methods
inside AudioIO to fill the content of the various ComboBox’s. Beware here: it
is strictly forbidden to call a resource intensive piece of code from inside an
EventHandler! (this should remind you of a similar rule when writing IRQ handlers for microcontrollers...). So as general rule of thumb: always move resource
intensive code inside a separate thread, and just start the thread from inside
your EventHandler, letting it execute nicely on its own (whatever the time this
resource intensive task takes to terminate, YOUR EventHandler can swiftly
terminate and give the control back to the JavaFX machinery, and this machinery can thus proceed back to handling mouse events and displaying things on
the screen... otherwise it would just FREEZE). As a rule: audio processing is
resource intensive ; writing huge files to the network is resource intensive ; doing artificial intelligence calculation is resource intensive ; System.out.println()
alone... is not. See the point?
Then, as a second item of business, you may want to consider the following
general architecture for the rest of the ”ui” package:
* write a ui.SignalView class that extends LineChart<Number,Number>
and can display an AudioSignal. You need to have a method updateData()
that would update the chart content from an AudioSignal. This method
may be called periodically from a javafx.animation.AnimationTimer,
which is probably the most efficient way as THIS timer knows what is the
best update frequency (it is useless and thus inefficient to update charts
at a higher pace than that of the whole JavaFX interface, usually around
10Hz!).
* write a ui.VuMeter class that can displays the signal level (see Figure 1).
It may extend Canvas and draw a vertical green/orange/red rectangle depending on the signal level. You may want to use Canvas.getGraphicsContext2D()
to write geometrical shapes and fill them with colours. Here again you need
an update() method that can update the color and size of the VuMeter
rectangle, and may also be called from the same AnimationTimer as above.
* at this point, you may want to add spectrum vizualisation: for this you will
first need to add a Complex[] computeFFT() method to the AudioSignal
class. You may want to base your code on
https://introcs.cs.princeton.edu/java/97data/Complex.java.html
10
and
[a link](https://introcs.cs.princeton.edu/java/97data/FFT.java.html)
It is probably time to create a math package and add these two classes,
FFT and Complex, to it, and then test them separately until they work as
expected (for example by feeding the FFT algorithm with a well known
signal created by hand).
Then if everything works great, create a ui.Spectrogram class that extends Canvas. We will use a WritableImage to draw the spectrogram
in real time (see the rightmost image in Figure 1 to see what it could
look like). GraphicsContext.drawImage() in effect displays this image on the screen (more exactly: inside the Canvas area). Using the
getPixelWriter() method, you can obtain a tool that allwos you to modify the image pixels one by one, which, with some clever coding, should
get the job done... The idea is to draw one vertical line after the other,
filling each line with the (absolute value of the) coefficients of the Fourier
transform of the signal...
