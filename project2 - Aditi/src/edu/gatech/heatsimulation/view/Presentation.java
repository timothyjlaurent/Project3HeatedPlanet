package edu.gatech.heatsimulation.view;

import java.util.Date;

import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.model.EarthSurface;
import edu.gatech.heatsimulation.model.SimulationBuffer;
import edu.gatech.heatsimulation.model.SimulationSetting;
import edu.gatech.heatsimulation.service.Simulation;
import edu.gatech.heatsimulation.utility.Configuration;
import edu.gatech.heatsimulation.utility.ConfigurationConstant.ConcurrencyType;
import edu.gatech.heatsimulation.utility.ConfigurationConstant.InitiativeType;
import static edu.gatech.heatsimulation.utility.SimulationConstant.*;

public class Presentation implements Runnable {
	
	public boolean initialize(Controller controller) {
		if (controller == null) {
			return false;
		}
	
		this.controller = controller;
		
		configuration = controller.getConfiguration();		  
		 if (configuration == null) {
			 return false;
		 }
		 
		simulationBuffer = controller.getSimulationBuffer();
		if (simulationBuffer == null) {
			return false;
		}
		
		simulation = controller.getSimulation();
		if (simulation == null) {
			return false;
		}
				
		synchronized (synchronizationLock) {
	 		pendingConsumeRequest = false;
 	    }
		
		return true;
	}
	
	public boolean initRequest() {
		if (controller == null) {
			return false;
		}
		
		SimulationSetting simulationSetting = controller.getSimulationSetting();
		if (simulationSetting == null) {
			return false;
		}
		
		presentationDisplayRate = simulationSetting.getPresentationDisplayRate();
		presentationUpdateTime = MILLI_SECOND_ZERO;
		
		initApplicableThread();
		
		return true;
	}
	
	public boolean consume() {
		if (configuration == null) {
			return false;
		}
		 
		ConcurrencyType concurrencyType = configuration.getConcurrencyType();
		switch (concurrencyType) {
			case CONCURRENCY_TYPE_PRESENTATION_THREAD:
		 	case CONCURRENCY_TYPE_MULTIPLE_THREAD:
		 		requestConsume();
		 		break;			 	
			case CONCURRENCY_TYPE_SINGLE_THREAD:
			case CONCURRENCY_TYPE_SIMULATION_THREAD:
			default:
				{
					immediateConsume();
				}
				break;
		 }
		
		
		
		
		return true;
	}
	
	
	private boolean immediateConsume() {
		if ((simulationBuffer == null) ||
				(controller == null)) {
				return false;
			}
			
			EarthSurface earthSurface = simulationBuffer.popEarthSurface();	
			if (earthSurface == null) {
				return false;
			}
			
			if (isElapsedDisplayRate()) {
				controller.updateDisplay(earthSurface);
			}
			return true;
	}
	private void requestConsume() {
		
		synchronized (synchronizationLock) {
	 		pendingConsumeRequest = true;
 	    }
	}
	
	public void invokeSimulation() {
		simulation.produce();
	}
	
	 @Override
	 public void run() {
		 executeOnThread();
	 }
	 
	 public boolean performRequest() {
		 
		 
		 if (configuration == null) {
			 return false;
		 }
		 
		 ConcurrencyType concurrencyType = configuration.getConcurrencyType();
		 switch (concurrencyType) {
		 	case CONCURRENCY_TYPE_PRESENTATION_THREAD:
			case CONCURRENCY_TYPE_MULTIPLE_THREAD:
		 		break;			 	
			case CONCURRENCY_TYPE_SIMULATION_THREAD:
			case CONCURRENCY_TYPE_SINGLE_THREAD:
			 default:
				 execute();
			 	break;
		 }
		 
		 return true;
	 }

	 private boolean initApplicableThread() {
		 
		 
		 if (configuration == null) {
			 return false;
		 }
		 
		 ConcurrencyType concurrencyType = configuration.getConcurrencyType();
		 switch (concurrencyType) {
		 	case CONCURRENCY_TYPE_PRESENTATION_THREAD:
		 	case CONCURRENCY_TYPE_MULTIPLE_THREAD:
		 		createThread();
			 	break;			 	
			 case CONCURRENCY_TYPE_SINGLE_THREAD:
			 case CONCURRENCY_TYPE_SIMULATION_THREAD:
			 default:
			 	break;
		 }
		 
		 return true;
	 }
	 
	 private void execute() {
		
			 if (configuration != null) {
				
				 InitiativeType initiativeType = configuration.getInitiativeType();
				 switch (initiativeType) {
				 	case INITIATIVE_TYPE_PRESENTATION:
				 		
				 		invokeSimulation();
				 		 while ((controller != null) && 
				 				(controller.isSimulationActive())) {
				 			 if (controller.isSimulationRunning()) {
				 				immediateConsume();
				 				 invokeSimulation();
				 			 }
				 			try {
								Thread.sleep(10);
							} catch (InterruptedException e) {							
							}
				 		 }
				 		 break;
					 case INITIATIVE_TYPE_SIMULATION:
					 case INITIATIVE_TYPE_MASTER:
					 default:
					 	break;
				 }
			 }
		 
	 }
	 
	 private void executeOnThread() {
		
			 if (configuration != null) {
				
				 InitiativeType initiativeType = configuration.getInitiativeType();
				 switch (initiativeType) {
				 	case INITIATIVE_TYPE_PRESENTATION:
				 		invokeSimulation();
				 		 while ((controller != null) && 
				 				(controller.isSimulationActive())) {
				 			 if (controller.isSimulationRunning()) {
				 				immediateConsume();
				 				 invokeSimulation();
				 			 }
				 			try {
								Thread.sleep(10);
							} catch (InterruptedException e) {							
							}
				 		 }
				 		 break;
					 case INITIATIVE_TYPE_SIMULATION:
					 case INITIATIVE_TYPE_MASTER:
					 default:
					 	while ((controller != null) && 
				 			   (controller.isSimulationActive())) {
					 		
					 		if (controller.isSimulationRunning()) {
						 	    synchronized (synchronizationLock) {
							 		if (pendingConsumeRequest) {					 			
							 			pendingConsumeRequest = false;
							 			immediateConsume();				 			
							 		}
						 	    }
					 		}
					 		try {
								Thread.sleep(10);
							} catch (InterruptedException e) {							
							}
					 	}
					 	break;
				 }
			 }
		 
	 }
	 
	 private void createThread() {
		 if (presentationThread == null) {
			 presentationThread = new Thread(this);
		 }
		 
		 if ((presentationThread != null) && 
			 (!presentationThread.isAlive())) {
			 presentationThread.start();
		 }
	 }
	 
	 private boolean isElapsedDisplayRate() {
		 
		   if ((presentationUpdateTime > 0) &&
			   (((System.currentTimeMillis() - presentationUpdateTime)) 
					   < presentationDisplayRate)) {
			   
			   while (true) {
				   if (((System.currentTimeMillis() - presentationUpdateTime)) 
							   >= presentationDisplayRate) {
						break;
					}
					try {
						Thread.sleep(10);
						
					} catch (InterruptedException exception) {					
					}
				}
		   }
		   
		   presentationUpdateTime = System.currentTimeMillis();
		   
		   return true;
	 }
	 
	 private Controller controller;
	 private Simulation simulation;
	 private SimulationBuffer simulationBuffer;
	 private Thread presentationThread;
	 private volatile boolean pendingConsumeRequest;
	 private final Object synchronizationLock = new Object();
	 private long presentationUpdateTime;
	 private Configuration configuration;
	 private int presentationDisplayRate;
}
