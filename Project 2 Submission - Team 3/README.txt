HeatedEarth
===========

Team 3
 
To compile the programs, use the following:

From the "src" directory, type the following on the command line:

javac EarthSim/*.java

To run the various programs, navigate to the "src" directory, and type the following on the command line:

java EarthSim.Demo [-s] [-p] [-r|-t] [-b #]

Where:
       [-s]: Indicates that the Simulation should run in its own thread
       [-p]: Indicates that the Presentation should run in its own thread
       [-r|-t]:
              -r: Indicates that the Presentation, after completing the display of a grid, should instruct the Simulation to produce another
              -t: Indicates that the Simulation, after producing an updated grid, should instruct the Presentation to consume it
       [-b #]:  parameter controls buffering, where # is a positive integer indicating the length of the buffer. Default is 1.


Example: From the src directory

java EarthSim.Demo -s -p -r -b 5
java EarthSim.Demo -s -t -b 5
java EarthSim.Demo -s -p -r
java EarthSim.Demo -s -p -t -b 5

  
The Graphical User Interface:
The form fields can be simply filled out with the respective arguments.
Clicking the "Play" button will run the program.

Color Key:
Red = Max Temp Value
Black = Min Temp Value
Everything else is a shade between the two colors based on ratio.  
