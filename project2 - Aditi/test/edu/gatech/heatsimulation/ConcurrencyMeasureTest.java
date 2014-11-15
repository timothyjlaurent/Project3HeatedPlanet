package edu.gatech.heatsimulation;

import static edu.gatech.heatsimulation.utility.SimulationConstant.SimulationAction.SIMULATION_ACTION_START;

import org.junit.Test;

import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.utility.CommandParser;
import edu.gatech.heatsimulation.utility.Configuration;
import edu.gatech.heatsimulation.utility.Measures;

public class ConcurrencyMeasureTest {

	public static int ITERATIONS_CONSTANT = 100;
	String[] testArgs = new String[10];
	
	@Test
	public void testConcurrency_NoConcurrency_MasterInitiative() {
		
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
	public void testConcurrencyPresentation_MasterInitiative(){
				
				testArgs[0] = "-p";
				
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
	
	//Simulation & Presentation running in separate threads with default initiative and buffer - 10
	@Test
	public void testConcurrencySimPresentation_MasterInitiativeBuffer(){
					
				
				testArgs[0] = "-s";
				testArgs[1] = "-p";
					
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
	
	//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_MasterInitiativeBuffer10(){
				
				testArgs[0] = "-s";
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
		
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_MasterInitiativeBuffer10(){
					
				
				//Not initializing any values for the args. This should test the defaults.
				testArgs[0] = "-p";
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
		
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_MasterInitiativeBuffer10(){
						
					
					testArgs[0] = "-s";
					testArgs[1] = "-p";
					testArgs[2] = "-b 10";
						
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
		
		//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_MasterInitiativeBuffer100(){
			
			testArgs[0] = "-s";
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
				
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_NoInitiativeBuffer100(){
							
						
			testArgs[0] = "-p";
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
				
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_MasterInitiativeBuffer100(){
								
			testArgs[0] = "-s";
			testArgs[1] = "-p";
			testArgs[2] = "-b 100";
								
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

		//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_SimInitiativeBuffer(){
			testArgs[0] = "-s";
			testArgs[1] = "-t";
			
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
				
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_SimInitiativeBuffer(){
							
						
		testArgs[0] = "-p";
		testArgs[1] = "-t";
							
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
				
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_SimInitiativeBuffer(){
								
			testArgs[0] = "-s";
			testArgs[1] = "-p";
			testArgs[2] = "-t";
								
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
		
		//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_SimInitiativeBuffer10(){
			testArgs[0] = "-s";
			testArgs[1] = "-t";
			testArgs[2] = "-b 10";
			
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
				
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_SimInitiativeBuffer10(){
							
						
		testArgs[0] = "-p";
		testArgs[1] = "-t";
		testArgs[2] = "-b 10";
							
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
				
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_SimInitiativeBuffer10(){
								
			testArgs[0] = "-s";
			testArgs[1] = "-p";
			testArgs[2] = "-t";
			testArgs[3] = "-b 10";
								
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
		
		//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_SimInitiativeBuffer100(){
			testArgs[0] = "-s";
			testArgs[1] = "-t";
			testArgs[2] = "-b 100";
			
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
				
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_SimInitiativeBuffer100(){
							
						
		testArgs[0] = "-p";
		testArgs[1] = "-t";
		testArgs[2] = "-b 100";
							
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
				
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_SimInitiativeBuffer100(){
								
			testArgs[0] = "-s";
			testArgs[1] = "-p";
			testArgs[2] = "-t";
			testArgs[3] = "-b 100";
								
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
		
		//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_PresInitiativeBuffer(){
			testArgs[0] = "-s";
			testArgs[1] = "-r";
			
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
				
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_PresInitiativeBuffer(){
							
						
		testArgs[0] = "-p";
		testArgs[1] = "-r";
							
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
				
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_PresInitiativeBuffer(){
								
			testArgs[0] = "-s";
			testArgs[1] = "-p";
			testArgs[2] = "-r";
								
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
		
		//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_PresInitiativeBuffer10(){
			testArgs[0] = "-s";
			testArgs[1] = "-r";
			testArgs[2] = "-b 10";
			
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
				
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_PresInitiativeBuffer10(){
							
						
		testArgs[0] = "-p";
		testArgs[1] = "-r";
		testArgs[2] = "-b 10";
							
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
				
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_PresInitiativeBuffer10(){
								
			testArgs[0] = "-s";
			testArgs[1] = "-p";
			testArgs[2] = "-r";
			testArgs[3] = "-b 10";
								
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
		
		//Simulation running is a separate thread with default initiative & buffer
		@Test
		public void testConcurrencySim_PresInitiativeBuffer100(){
			testArgs[0] = "-s";
			testArgs[1] = "-r";
			testArgs[2] = "-b 100";
			
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
				
		//Presentation running in a separate thread with default initiative & buffer
		@Test
		public void testConcurrencyPresentation_PresInitiativeBuffer100(){
							
						
		testArgs[0] = "-p";
		testArgs[1] = "-r";
		testArgs[2] = "-b 100";
							
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
				
		//Simulation & Presentation running in separate threads with default initiative and buffer
		@Test
		public void testConcurrencySimPresentation_PresInitiativeBuffer100(){
								
			testArgs[0] = "-s";
			testArgs[1] = "-p";
			testArgs[2] = "-r";
			testArgs[3] = "-b 100";
								
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
