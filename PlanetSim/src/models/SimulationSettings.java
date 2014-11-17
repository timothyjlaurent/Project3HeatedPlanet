package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import constants.SimulationConstants;

@Entity
@Table(name = "EXPERIMENT")
public class SimulationSettings {

	@Column(name = "EXPERIMENT_NAME")
	private String experimentName;
	
	@Transient
	private int gridSpacing = SimulationConstants.DEFAULT_GRID_SPACING;
	
	@Transient
	private int timeStamp = SimulationConstants.DEFAULT_TIME_STEP;
	
	@Transient
	private int simulationLength = SimulationConstants.DEFAULT_SIM_LENGTH;

	public SimulationSettings(final String experimentName) {
		this.experimentName = experimentName;
	}

	public SimulationSettings(final String experimentName, final int gridSpacing, final int timeStamp, final int simulationLength) {
		this.experimentName = experimentName;
		this.gridSpacing = gridSpacing;
		this.timeStamp = timeStamp;
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
