package edu.gatech.heatsimulation.controller;

import javax.swing.SwingUtilities;

import edu.gatech.heatsimulation.model.EarthSurface;
import edu.gatech.heatsimulation.model.SimulationBuffer;
import edu.gatech.heatsimulation.model.SimulationSetting;
import edu.gatech.heatsimulation.service.Simulation;
import edu.gatech.heatsimulation.utility.Configuration;
import edu.gatech.heatsimulation.utility.ConfigurationConstant.ConcurrencyType;
import edu.gatech.heatsimulation.utility.ConfigurationConstant.InitiativeType;
import edu.gatech.heatsimulation.view.Presentation;
import edu.gatech.heatsimulation.view.PresentationPanel;
import edu.gatech.heatsimulation.view.SimulationFrame;
import static edu.gatech.heatsimulation.utility.SimulationConstant.*;
import static edu.gatech.heatsimulation.utility.SimulationConstant.SimulationState.*;
import static edu.gatech.heatsimulation.utility.ConfigurationConstant.ConcurrencyType.*;

public class Controller implements Runnable {

	public boolean initialize(Configuration configuration) {
		
		if ((!createComponent()) ||
			(!initialzeComponent(configuration))) {
			return false;
		}
			
		simulationState = SIMULATION_STATE_NONE;	
		iterationCount = COUNT_ZERO;
		iMaxterationCount = COUNT_ZERO;
		requestInProcess = false;
		suppressGUI = false;
		
		return true;
	}
	
	@Override
	public void run() {
		execute();
	}
	
	public void suppressGUI(boolean suppressGUI) {
		this.suppressGUI = suppressGUI;
	}
	
	public boolean simulate() {
		
		if (!suppressGUI) {
			simulationFrame.setController(this);
			SwingUtilities.invokeLater(new Runnable() {
	    	    @Override
	    	    public void run() {
	    	    	simulationFrame.initFrame();
	    	    	simulationFrame.setVisible(true);    	    	
	    	    }
	    	});
		}
		return true;
	}
	
	public void destroy() {
		simulationState = SimulationState.SIMULATION_STATE_NONE;
		if (mainThread != null) {
			mainThread.stop();
			mainThread = null;
		}
	}
	
	public boolean initDisplay() {
		if (!suppressGUI) {
			PresentationPanel presentationPanel = simulationFrame.getPresentationPanel();
			if (presentationPanel == null) {
				return false;
			}
			
			presentationPanel.initEarthDisplay();
		}
		
		
		return true;
	}
	
	public boolean updateDisplay(EarthSurface earthSurface) {
		if (!suppressGUI) {
			if (earthSurface == null) {
				return false;
			}
			
			PresentationPanel presentationPanel = simulationFrame.getPresentationPanel();
			if (presentationPanel == null) {
				return false;
			}
			
			presentationPanel.updateEarthDisplay(earthSurface);
		}
		
		return true;
	}
	
	
	public boolean clearDisplay() {
		if (!suppressGUI) {
			PresentationPanel presentationPanel = simulationFrame.getPresentationPanel();
			if (presentationPanel == null) {
				return false;
			}
			
			presentationPanel.clearEarthDisplay();
		}
		
		return true;
	}
	
	private boolean exitSimulation() {
		simulationState = SIMULATION_STATE_NONE;
		requestInProcess = false;
		clearDisplay();
		return true;
	}
	
	public boolean executeSimulationCommand(SimulationAction simulationAction,
											int gridSpacing,
											int simulationStep,
											int presentationDisplayRate) {
		boolean executeCommand = false;
		switch (simulationAction) {
			case SIMULATION_ACTION_STOP:
				executeCommand = stopSimulation();
				break;
			case SIMULATION_ACTION_PAUSE:
				executeCommand = pauseSimulation();
				break;
			case SIMULATION_ACTION_RESUME:
				executeCommand = resumeSimulation();
				break;
			case SIMULATION_ACTION_EXIT:
				executeCommand = exitSimulation();
				break;
			case SIMULATION_ACTION_START:
				executeCommand = startSimulation(gridSpacing, simulationStep, presentationDisplayRate);
				break;
			case SIMULATION_ACTION_NONE:
			default:
				break;
		}
		
		return executeCommand;
	}
	
	public boolean executeSimulationCommand(SimulationAction simulationAction,
											int gridSpacing,
											int simulationStep,
											int presentationDisplayRate,
											int iMaxterationCount) {
		
		if (iMaxterationCount < COUNT_ZERO) {
			return false;
		}
		
		this.iMaxterationCount = iMaxterationCount;
		
		return executeSimulationCommand(simulationAction,
										gridSpacing,
										simulationStep,
										presentationDisplayRate);
	}
	
	public void incrementIterationCount() {
		iterationCount++;
	}
	
	public boolean isElapsedIterationCount() {
		return ((iMaxterationCount > 0) && (iterationCount > iMaxterationCount));
	}

	private boolean createComponent() {
		
		if (simulation == null) {
			simulation = new Simulation();
		}
				
		if (presentation == null) {
			presentation = new Presentation();
		}
		
		if (simulationSetting == null) {
			simulationSetting = new SimulationSetting();
		}
		
		if (simulationBuffer == null) {
			simulationBuffer = new SimulationBuffer();
		}
		
		return true;
	}
	
	private boolean initialzeComponent(Configuration configuration) {
		
		if ((configuration == null) ||
			(simulation == null) ||
			(presentation == null) ||
			(simulationBuffer == null) ||
			(simulationSetting == null)) {
			return false;			
		}
		
		this.configuration = configuration;
		
		if ((!simulationSetting.init()) ||
			(!simulation.initialize(this)) ||
			(!presentation.initialize(this)) ||
			(!simulationBuffer.initialize(configuration))) {
			return false;
		}
		
		initApplicableThread();
		
		return true;
	}
		
	
	public boolean isSimulationActive() {
		return (((simulationState == SIMULATION_STATE_RUNNING) || (simulationState == SIMULATION_STATE_PAUSED)) &&
				(!isElapsedIterationCount()));
	}
	
	public boolean isSimulationRunning() {
		return (simulationState == SIMULATION_STATE_RUNNING);
	}
	
	public SimulationSetting getSimulationSetting() {
		return simulationSetting;
	}
	
	public SimulationBuffer getSimulationBuffer() {
		return simulationBuffer;
	}
	
	public Simulation getSimulation() {
		return simulation;
	}
	
	public Presentation getPresentation() {
		return presentation;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
		
	private void initApplicableThread() {
		
		if (isMainThreadRequired()) {
			mainThread = new Thread(this);
			mainThread.start();
		}
	}
	
	private boolean isMainThreadRequired() {
		boolean mainThreadRequired = true;
		
		if (configuration != null)
		{
			ConcurrencyType concurrencyType = configuration.getConcurrencyType();
			InitiativeType initiativeType = configuration.getInitiativeType();
			if (((concurrencyType == CONCURRENCY_TYPE_PRESENTATION_THREAD) && 
					(initiativeType == InitiativeType.INITIATIVE_TYPE_PRESENTATION)) ||
				((concurrencyType == CONCURRENCY_TYPE_SIMULATION_THREAD) && 
							(initiativeType == InitiativeType.INITIATIVE_TYPE_SIMULATION)) ||
				((concurrencyType == CONCURRENCY_TYPE_MULTIPLE_THREAD) && 
						((initiativeType == InitiativeType.INITIATIVE_TYPE_SIMULATION) ||
								(initiativeType == InitiativeType.INITIATIVE_TYPE_SIMULATION)))) {
				
				mainThreadRequired = false;
			}
				
		}
			
		return mainThreadRequired;
	}
	
	
	private void execute() {
		try {
			
			while (true) {
				if (requestInProcess) {
					performRequest();
				}
					
				Thread.sleep(10);
			}
		} catch(Exception exception) {
			
		}	
	}
	
	
	private boolean startSimulation(int gridSpacing,
									int simulationStep,
									int presentationDisplayRate) {
		
		if ((simulation == null) ||
			(presentation == null) ||
			(simulationBuffer == null) ||
			(!simulationSetting.init(gridSpacing, simulationStep, presentationDisplayRate))) {
			return false;
		}
		initDisplay();
		simulation.initRequest();
		presentation.initRequest();
		simulationBuffer.initBuffer();
		iterationCount = COUNT_ZERO;
		simulationState = SIMULATION_STATE_RUNNING;
		
		requestInProcess = true;
		
		return true;
	}
	
	private boolean pauseSimulation() {
		simulationState = SIMULATION_STATE_PAUSED;
		return true;
	}
	
	private boolean resumeSimulation() {
		simulationState = SIMULATION_STATE_RUNNING;
		return true;
	}
	
	private boolean stopSimulation() {
		simulationState = SIMULATION_STATE_NONE;
		clearDisplay();
		requestInProcess = false;
		return true;
	}
	
	private void performRequest() {
		if (configuration != null) {
			
			InitiativeType initiativeType = configuration.getInitiativeType();
			switch (initiativeType) {
				case INITIATIVE_TYPE_SIMULATION:
					simulation.performRequest();
			 		break;
				case INITIATIVE_TYPE_PRESENTATION:
					presentation.performRequest();
					break;
				case INITIATIVE_TYPE_MASTER:
				default:
					while (isSimulationActive()) {
						if (isSimulationRunning()) {
							simulation.produce();
							presentation.consume();
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
	
	private Configuration configuration;
	private Simulation simulation;
	private Presentation presentation;
	private SimulationState simulationState;
	private SimulationSetting simulationSetting;
	private SimulationBuffer simulationBuffer;
	final SimulationFrame simulationFrame = new SimulationFrame();
	private int iterationCount;
	private int iMaxterationCount;
	private Thread mainThread;
	private boolean requestInProcess;
	private boolean suppressGUI;
	
}
