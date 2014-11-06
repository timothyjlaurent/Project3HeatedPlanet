package userControl;

import java.util.Observable;

public class SimulationOptions extends Observable {

	// Default options
	private int gridSpacing = 15;
	private int simulationRate = 1;
	private int simulationDelayMilliSeconds = 0;
	private int visualizationRate = 2;
	private boolean run = false;
	private boolean resetOnStart = false;
	private int visualizationDelayMilliSeconds = 0;
	private CommunicationConfig commConfig = CommunicationConfig.BUFFER;
	private ThreadConfig threadConfig = ThreadConfig.NONE;

	private SimulationOptions() {
	};

	private static SimulationOptions instance;

	public static SimulationOptions getInstance() {
		if (instance == null) {
			instance = new SimulationOptions();
		}
		return instance;
	}

	public static void destroy() {
		if (instance != null) {
			instance = null;
		}
	}

	public void setGridSpacing(int gridSpacing) {
		this.gridSpacing = gridSpacing;
	}

	public int getGridSpacing() {
		return this.gridSpacing;
	}

	public void setSimulationRate(int simulationRate) {
		this.simulationRate = simulationRate;
	}

	public int getSimulationRate() {
		return this.simulationRate;
	}

	public void setVisualizationRate(int visualizationRate) {
		this.visualizationRate = visualizationRate;
	}

	public int getVisualizationRate() {
		return this.visualizationRate;
	}

	public void setRun(boolean run) {
		this.run = run;
		this.setChanged();
		this.notifyObservers();
	}

	public boolean getRun() {
		return this.run;
	}

	public void setResetOnStart(boolean resetOnStart) {
		this.resetOnStart = resetOnStart;
	}

	public boolean getResetOnStart() {
		return this.resetOnStart;
	}

	public void setCommConfig(CommunicationConfig commConfig) {
		this.commConfig = commConfig;
	}

	public CommunicationConfig getCommConfig() {
		return this.commConfig;
	}

	public void setThreadConfig(ThreadConfig threadConfig) {
		this.threadConfig = threadConfig;
	}

	public ThreadConfig getThreadConfig() {
		return this.threadConfig;
	}

	public void setSimulationDelayMilliSeconds(int simulationDelay) {
		this.simulationDelayMilliSeconds = simulationDelay;
	}

	public int getSimulationDelay_ms() {
		return this.simulationDelayMilliSeconds;
	}

	public void setVisualizationDelayMilliSeconds(int visualizationDelay) {
		this.visualizationDelayMilliSeconds = visualizationDelay;
	}

	public int getVisualizationDelayMilliSeconds() {
		return this.visualizationDelayMilliSeconds;
	}

}
