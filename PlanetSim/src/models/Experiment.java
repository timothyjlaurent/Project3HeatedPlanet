package models;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "experiment")
public class Experiment {

	@Id
	@GeneratedValue
	@Column(name = "EXPERIMENT_ID")
	private int experimentId;

	@OneToMany(targetEntity=GridPoints.class,cascade= CascadeType.ALL,fetch= FetchType.LAZY)
	private Set<GridPoints> gridPoints;

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

	public Set<GridPoints> getGridPoints() {
		return gridPoints;
	}

	public void setGridPoints(final Set<GridPoints> gridPoints) {
		this.gridPoints = gridPoints;
	}

	public SimulationSettings getSimulationSettings() {
		return simulationSettings;
	}

	public void setSimulationSettings(
			final SimulationSettings simulationSettings) {
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
