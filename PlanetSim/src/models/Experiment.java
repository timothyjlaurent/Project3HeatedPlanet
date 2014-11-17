package models;

import java.util.Collection;

public class Experiment {

	private int experimentId;
	private Collection<GridPoints> gridPoints;
	private SimulationSettings simulationSettings;
	private PhysicalFactors physicalFactors;
	private CommandLineParam commandLineParam;

	public int getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(final int experimentId) {
		this.experimentId = experimentId;
	}

	public Collection<GridPoints> getGridPoints() {
		return gridPoints;
	}

	public void setGridPoints(final Collection<GridPoints> gridPoints) {
		this.gridPoints = gridPoints;
	}

	public SimulationSettings getSimulationSettings() {
		return simulationSettings;
	}

	public void setSimulationSettings(final SimulationSettings simulationSettings) {
		this.simulationSettings = simulationSettings;
	}

	public PhysicalFactors getPhysicalFactors() {
		return physicalFactors;
	}

	public void setPhysicalFactors(final PhysicalFactors physicalFactors) {
		this.physicalFactors = physicalFactors;
	}

	public CommandLineParam getCommandLineParam() {
		return commandLineParam;
	}

	public void setCommandLineParam(final CommandLineParam commandLineParam) {
		this.commandLineParam = commandLineParam;
	}

	public int getNumOfTimeSteps() {
		return 5;
	}

	public int getNumOfRegions() {
		return 5;
	}



}
