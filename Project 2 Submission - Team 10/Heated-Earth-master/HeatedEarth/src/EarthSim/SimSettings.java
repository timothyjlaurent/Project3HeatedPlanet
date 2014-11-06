package EarthSim;

import org.kohsuke.args4j.Option;

import earthWidgets.EarthPanel;

/**
 * 
 */
public class SimSettings {


	@Option(name="-s", usage="Indicates that the Simulation should run in its own thread")
	public boolean simulationThread = false;
	
	@Option(name="-p", usage="Indicates that the Presentation should run in its own thread")
	public boolean presentationThread = false;
	
	@Option(name="-t", usage="Indicates that the Simulation, after producing an updated grid, should instruct the Presentation to consume it")
	public boolean simulationInitiative = false;
	
	@Option(name="-r", usage=" Indicates that the Presentation, after completing the display of a grid, should instruct the Simulation to produce another")
	public boolean presentationInitiative = false;

	@Option(name="-b", usage=" Indicates that the Presentation, after completing the display of a grid, should instruct the Simulation to produce another")
	public int bufferSize = 1;

	
	// initialize message queues for interthread communication
	public SimCmdMsg initMsg = new SimCmdMsg();
	
	public SimCmdMsg presMsg = new SimCmdMsg();
	
	public SimCmdMsg simMsg = new SimCmdMsg();
	
	/**
	 * 
	 */
	public int displayRate;

	/**
	 * 
	 */
	public int gridSize = 15;

	public int timeStep = 1;

	public EarthPanel dispPanel;
	

	/**
	 * 
	 * @param initiative
	 * @param simulationThread
	 * @param presentationThread
	 * @param displayRate
	 * @param gridSize
	 * @param bufferSize
	 */
	public void SimulationSettings(boolean simulationThread, boolean presentationThread,
			boolean simulationInitiative, boolean presentationInitiative, int displayRate, int gridSize, int timeStep,
			int bufferSize) {
		this.simulationThread = simulationThread;
		this.presentationThread = presentationThread;
		this.simulationInitiative = simulationInitiative;
		this.presentationInitiative = presentationInitiative;
		this.displayRate = displayRate;
		this.gridSize = gridSize;
		this.timeStep = timeStep;
		this.bufferSize = bufferSize;
	}


	public void setInit(String action) {
		this.simulationInitiative=false;
		this.presentationInitiative= false;
		
		if(action.equals("sim")){
			this.simulationInitiative=true;
		}else if( action.equals("pres")){
			this.presentationInitiative = true;
		}	
	}


	public void setThread(boolean sim, boolean pres) {
		this.simulationThread = sim;
		this.presentationThread = pres;
		
	}
}