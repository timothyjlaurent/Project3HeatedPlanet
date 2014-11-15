package edu.gatech.heatsimulation.utility;

import edu.gatech.heatsimulation.model.EarthGridCell;
import edu.gatech.heatsimulation.model.EarthSurface;
import edu.gatech.heatsimulation.model.SimulationBuffer;

//This class measures and prints the performance and memory characteristics of the program

public class Measures {
	
		  long start;
		  
		  public Measures()
		  {
			  //Record start time for the program
			  start = System.currentTimeMillis();
		  }
		  
		  
		  //Calculate and print program memory characteristics
		  public  void programMemory() {
		    Runtime runtime = Runtime.getRuntime();
		    runtime.gc();
		    runtime.gc();
		    long memory = runtime.totalMemory() - runtime.freeMemory();
		    System.out.println("Memory in bytes = " + memory);
		  }
		  
		  //Calculate and print the execution time of the program
		  public  void programTime()
		  {
			  System.out.println("Execution time in Milliseconds =" + String.valueOf(System.currentTimeMillis() - start));
		  }
		  
		  //Print all measures of interest
		  //Memory
		  //execution time
		  //Number of iterations of the algorithm to compute the stable state of the heated plate
		  public void print(int numIterations)
		  {
			  programMemory();
			  programTime();
			  System.out.println("Number of iterations performed to reach equilibrium state =" + String.valueOf(numIterations));
			  
		  }
		  
		  public void print(SimulationBuffer simulationBuffer){
			  
			  EarthSurface earth = simulationBuffer.popEarthSurface();
			  EarthGridCell [][] gridCells = earth.surface;
			  EarthGridCell gridCell;
			  for(int i=0; i<gridCells.length; i++){
				  for(int j=0; j<gridCells[i].length; j++){
					  gridCell = gridCells[i][j];
					  System.out.println(" " + gridCell.getCurrentTemp() + " ");
				  }
				  System.out.println();
			  }
			  
		  }
		  
		  
}
