package models;

import constants.SimulationConstants;

public class SimulationSettings {

	private String name;
	private int gridSpacing = SimulationConstants.DEFAULT_GRID_SPACING;
	private int timeStamp = SimulationConstants.DEFAULT_TIME_STEP;
	private int simulationLength = SimulationConstants.DEFAULT_SIM_LENGTH;

	public SimulationSettings(final String name) {
		this.name = name;
	}

	public SimulationSettings(final String name, final int gridSpacing, final int timeStamp, final int simulationLength) {
		this.name = name;
		this.gridSpacing = gridSpacing;
		this.timeStamp = timeStamp;
		this.simulationLength = simulationLength;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getGridSpacing() {
		return gridSpacing;
	}

	public void setGridSpacing(final int gridSpacing) {
		this.gridSpacing = gridSpacing;
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(final int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getSimulationLength() {
		return simulationLength;
	}

	public void setSimulationLength(final int simulationLength) {
		this.simulationLength = simulationLength;
	}

}
