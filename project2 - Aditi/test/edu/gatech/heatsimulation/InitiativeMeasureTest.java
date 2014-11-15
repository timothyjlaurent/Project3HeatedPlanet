package edu.gatech.heatsimulation;

import static edu.gatech.heatsimulation.utility.SimulationConstant.SimulationAction.SIMULATION_ACTION_START;

import org.junit.Test;

import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.utility.CommandParser;
import edu.gatech.heatsimulation.utility.Configuration;
import edu.gatech.heatsimulation.utility.Measures;


public class InitiativeMeasureTest {
	
	public static int ITERATIONS_CONSTANT = 100;
	String[] testArgs;
	// 4 kinds of conc, 3 of initiative, 3 buffer (1, 10, 100). Iteration - 100
	
	//Test with only 1 main thread, constant buffer value and a Master controlling the initiative (as default).
	@Test
	public void testMasterInitiative(){
		
	
		//Not initializing any values for the args. This should test the defaults.
		testArgs[0] = "";
		
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
			
		}
		
		
	}
	
	
	@Test
	public void testSimulationInitiative(){
		
		testArgs[0] = "-r";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
			
		}
		
	}
	
	@Test
	public void testPresentationInitiative(){
		
		testArgs[0] = "-t";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
			
		}
		
	}
	
	@Test
	public void testMasterInitiativeBuffer10(){
		
		testArgs[0] = "-t";
		testArgs[1] = "-b 10";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
			
		}
		
	}
	
	@Test
	public void testSimulationInitiativeBuffer10(){
		
		testArgs[0] = "-r";
		testArgs[1] = "-b 10";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
			
		}
		
	}
	
	@Test
	public void testPresentationInitiativeBuffer10(){
		
		testArgs[0] = "-t";
		testArgs[1] = "-b 10";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
			
		}
		
	}
	
	@Test
	public void testMasterInitiativeBuffer100(){
		
		testArgs[0] = "-t";
		testArgs[1] = "-b 100";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
			
		}
		
	}
	
	@Test
	public void testSimulationInitiativeBuffer100(){
		
		testArgs[0] = "-r";
		testArgs[1] = "-b 100";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
		}
		
	}
	
	@Test
	public void testPresentationInitiativeBuffer100(){
		
		testArgs[0] = "-t";
		testArgs[1] = "-b 100";
		Configuration configuration = new Configuration();
		
		if (CommandParser.parse(testArgs, configuration)) {
			
			Measures measure = new Measures();
			Controller simulationController = new Controller();
			simulationController.initialize(configuration);
			simulationController.suppressGUI(true);
			simulationController.simulate();
			simulationController.executeSimulationCommand(SIMULATION_ACTION_START, 
															10, 10, 60, ITERATIONS_CONSTANT);
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			measure.print(ITERATIONS_CONSTANT);
		}
		
	}

}
