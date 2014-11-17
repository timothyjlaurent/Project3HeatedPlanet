package models;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "EXPERIMENT")
public class Experiment {

	@Id
	@GeneratedValue
	@Column (name = "EXPERIMENT_ID")
	private int experimentId;
	
	@Transient
	private Collection<GridPoints> gridPoints;
	
	@OneToOne
	private SimulationSettings simulationSettings;
	
	@Transient
	private PhysicalFactors physicalFactors;
	
	@Transient
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
