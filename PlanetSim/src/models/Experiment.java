package models;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Table(name = "experiment")
public class Experiment {

	@Id
	@GeneratedValue
	@Column(name = "EXPERIMENT_ID")
	private int experimentId = -1;

	@ElementCollection(fetch = FetchType.LAZY)
	@MapKeyColumn(name = "DATE_TIME")
	@Column(name = "value")
	@CollectionTable(name = "grid_points", joinColumns = @JoinColumn(name = "EXPERIMENT_ID"))
	private Map<Date, Set<GridPoint>> gridPoints;

	@Embedded
	private SimulationSettings simulationSettings;

	@Embedded
	private PhysicalFactors physicalFactors;

	@Embedded
	private CommandLineParam commandLineParam;

	public int getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(final int experimentId) {
		this.experimentId = experimentId;
	}

	public Map<Date, Set<GridPoint>> getGridPoints() {
		return gridPoints;
	}

	public void setGridPoints(final Map<Date, Set<GridPoint>> gridPoints) {
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
