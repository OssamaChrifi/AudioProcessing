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
Echo class, the PassThrough class, etc.
* The ”math” package comprises classes that handle math operations, e.g.,
Complex numbers and Fast Fourier Transform.

