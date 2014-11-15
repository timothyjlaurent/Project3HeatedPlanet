			README
			======

	    HeatedEarth 1.1 release
	    ------------------------------

HeatedEarth is GUI based application to simulate the heating of earth surface
by sun under various conditions, Application also supports various configuration
of running conditions based on concurrency, data buffer size and control flow.

Features supported
-----------------
	UI - Control panel to set simulation step, presentation display rate, set state of the simulation
	   - Presentation panel to view the simulation results i.e heat projections on simulated earth surface
	
Contents
--------

    Included in this release are the following:

    README.txt 		this file
       
    docs/project2.report
			Description of the Heated Earth Application's Design Specification
			
	docs/project2.study
			Detailed study report of the Heated Earth Application.

    src/com/edu/gatech/heatsimulation/EarthSim/Demo.java	demo program to run heating of earth under various 
															running conditions.
  
    src/com/edu/gatech/heatsimulation/	source files for Heated Earth application.


Requirements
------------

HeatedEarth application requires JDK 1.6, it is available at http://www.oracle.com/technetwork/java/javaee/downloads/java-ee-sdk-6u3-jdk-6u29-downloads-523388.html




Running on Windows
------------

  1. Unzip the project2.tar archive.
    
  2. Go to the EarthSim directory

  3. Compile Demo Java using your Java compiler. For example:
	  3.a. Open Commandline
	  
	  3.b. Navigate to project root folder say: c:\heatedearth
	  	c:\heatedearth>
	  
	  3.c. Create directory bin
		c:\heatedearth>mkdir bin
	  
	  3.d Complile Demo.java
           c:\heatedearth>javac -d bin -sourcepath src src/edu/gatech/heatsimulation/EarthSim/Demo.java


  4. Run the demo. The '-' option lists the required and optional
     command-line options to successfully run any demo. For example:

      c:\heatedearth>java -cp bin; edu.gatech.heatsimulation.EarthSim.Demo -


       lists the available options. 

      java demo [-s] [-p] [-r|-t] [-b <buffersize>]
    
   5. Run the Demo with simulation and presentation on individual thread with master initiative and buffer length of 25
		c:\heatedearth>java -cp bin; edu.gatech.heatsimulation.EarthSim.Demo -s -p -b25
		
   6. Run the Demo with presentation on individual thread and instructs simulation to produce next iteration of data and buffer length of 1
		c:\heatedearth>java -cp bin; edu.gatech.heatsimulation.EarthSim.Demo  -p -r -b1
		
   7. Run the Demo with just GUI thread and simulation instructs presentation to consume data and buffer length of 1
		c:\heatedearth>java -cp bin; edu.gatech.heatsimulation.EarthSim.Demo  -t -b1
		
		

Thank You!
Team 8
