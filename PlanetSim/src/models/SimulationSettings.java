package models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import constants.SimulationConstants;

@Embeddable
public class SimulationSettings {

	@Column(name = "EXPERIMENT_NAME")
	private String experimentName;

	@Column(name = "GRID_SPACING")
	private int gridSpacing = SimulationConstants.DEFAULT_GRID_SPACING;

	@Column(name = "TIME_STEP")
	private int timeStep = SimulationConstants.DEFAULT_TIME_STEP;

	@Column(name = "SIMULATION_LENGTH")
	private int simulationLength = SimulationConstants.DEFAULT_SIM_LENGTH;

	public SimulationSettings() {
	}

	public SimulationSettings(final String experimentName) {
		this.experimentName = experimentName;
	}

	public SimulationSettings(final String experimentName, final int gridSpacing, final int timeStep, final int simulationLength) {
		this.experimentName = experimentName;
		this.gridSpacing = gridSpacing;
		this.timeStep = timeStep;
		this.simulationLength = simulationLength;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(final String experimentName) {
		this.experimentName = experimentName;
	}

	public int getGridSpacing() {
		return gridSpacing;
	}

	public void setGridSpacing(final int gridSpacing) {
		this.gridSpacing = gridSpacing;
	}

	public int getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(final int timeStep) {
		this.timeStep = timeStep;
	}

	public int getSimulationLength() {
		return simulationLength;
	}

	public void setSimulationLength(final int simulationLength) {
		this.simulationLength = simulationLength;
	}

	@Override
	public String toString() {
		return "SimulationSettings [experimentName=" + experimentName + ", gridSpacing=" + gridSpacing + ", timeStep=" + timeStep + ", simulationLength=" + simulationLength + "]";
	}

}
