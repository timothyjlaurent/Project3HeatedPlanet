package edu.gatech.heatsimulation.service;

import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.model.SimulationBuffer;
import edu.gatech.heatsimulation.model.SimulationSetting;
import edu.gatech.heatsimulation.utility.Configuration;
import edu.gatech.heatsimulation.utility.ConfigurationConstant.ConcurrencyType;
import edu.gatech.heatsimulation.utility.ConfigurationConstant.InitiativeType;
import edu.gatech.heatsimulation.view.Presentation;

public class Simulation implements Runnable {

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
		
		presentation = controller.getPresentation();
		if (presentation == null) {
			return false;
		}
		
		synchronized (synchronizationLock) {
	 		pendingProduceRequest = false;
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
		
		if (earthHeating == null) {
			earthHeating = new EarthHeating();
		}
				
		if ((earthHeating == null) ||
			(!earthHeating.init(simulationSetting.getGridSpacing(),
							    simulationSetting.getSimulationStep()))) {
			return false;
		}
		
		initApplicableThread();
		
		return true;
	}
	
	public boolean produce() {		
		
		if (configuration == null) {
			return false;
		}
		 
		ConcurrencyType concurrencyType = configuration.getConcurrencyType();
		switch (concurrencyType) {
			case CONCURRENCY_TYPE_SIMULATION_THREAD:
		 	case CONCURRENCY_TYPE_MULTIPLE_THREAD:
		 		requestProduce();
		 		break;			 	
			case CONCURRENCY_TYPE_SINGLE_THREAD:
			case CONCURRENCY_TYPE_PRESENTATION_THREAD:
			default:
				{
					immediateProduce();
				}
				break;
		 }
		
		
		return true;
	}
	
	
	private boolean immediateProduce() {
		if ((simulationBuffer == null) ||
				(earthHeating == null) ||
				(controller == null)) {
					return false;
			}
			earthHeating.updateTemp();
			simulationBuffer.pushEarthSurface(earthHeating.earth);

				controller.incrementIterationCount();
		return true;
	}
	private void requestProduce() {
		
		synchronized (synchronizationLock) {
	 		pendingProduceRequest = true;
 	    }
	}
		 
	 public boolean performRequest() {
		 
		 if (configuration == null) {
			 return false;
		 }
		 
		 ConcurrencyType concurrencyType = configuration.getConcurrencyType();
		 switch (concurrencyType) {
		 	case CONCURRENCY_TYPE_SIMULATION_THREAD:
		 	case CONCURRENCY_TYPE_MULTIPLE_THREAD:
		 		break;			 	
			 case CONCURRENCY_TYPE_SINGLE_THREAD:
			 case CONCURRENCY_TYPE_PRESENTATION_THREAD:
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
		 	case CONCURRENCY_TYPE_SIMULATION_THREAD:
		 	case CONCURRENCY_TYPE_MULTIPLE_THREAD:
		 		createThread();
			 	break;			 	
			 case CONCURRENCY_TYPE_SINGLE_THREAD:
			 case CONCURRENCY_TYPE_PRESENTATION_THREAD:
			 default:
			 	break;
		 }
		 
		 return true;
	 }
	 
	private void invokePresentation() {
		presentation.consume();
	}
	
	@Override
	public void run() {
		executeOnThread();
	}
	 
	 private void execute() {
		 	  
		 if (configuration != null) {
			 InitiativeType initiativeType = configuration.getInitiativeType();
			 	switch (initiativeType) {
			 		case INITIATIVE_TYPE_SIMULATION:
			 			while ((controller != null) && 
			 				   (controller.isSimulationActive())) {
			 					if (controller.isSimulationRunning()) {
			 						immediateProduce();
			 						invokePresentation();
			 					}
			 					try {
									Thread.sleep(10);
								} catch (InterruptedException e) {							
								}
				 		 }
				 		 break;
					 case INITIATIVE_TYPE_PRESENTATION:
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
				 	case INITIATIVE_TYPE_SIMULATION:
				 		 while ((controller != null) && 
					 			(controller.isSimulationActive())) {
				 			 
				 			if (controller.isSimulationRunning()) {
				 				immediateProduce();
				 				invokePresentation();
				 			}
				 			try {
								Thread.sleep(10);
							} catch (InterruptedException e) {							
							}
				 		 }
				 		 break;
					 case INITIATIVE_TYPE_PRESENTATION:
					 case INITIATIVE_TYPE_MASTER:
					 default:
					 	while ((controller != null) && 
				 			   (controller.isSimulationActive())) {
					 		
					 		if (controller.isSimulationRunning()) {
						 	    synchronized (synchronizationLock) {
							 		if (pendingProduceRequest) {					 			
							 			pendingProduceRequest = false;
							 			immediateProduce();				 			
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
		 if (simulationThread == null) {
			 simulationThread = new Thread(this);
		 }
		 
		 if ((simulationThread != null) && 
			 (!simulationThread.isAlive())) {
			 simulationThread.start();
		 }
	 }
	 
	 private Controller controller;
	 private Presentation presentation;
	 private SimulationBuffer simulationBuffer;
	 private Thread simulationThread;
	 private Configuration configuration;
	 private volatile boolean pendingProduceRequest;
	 private final Object synchronizationLock = new Object();
	 private EarthHeating earthHeating;
}
