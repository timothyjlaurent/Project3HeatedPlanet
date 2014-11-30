package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "experiment")
public class Experiment {

	@Id
	@GeneratedValue
	@Column(name = "EXPERIMENT_ID")
	private int experimentId = -1;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "experiment_id")
	private Set<GridPoint> gridPoints = new HashSet<GridPoint>();

	@Embedded
	private SimulationSettings simulationSettings;

	@Embedded
	private PhysicalFactors physicalFactors;

	@Embedded
	private CommandLineParam commandLineParam;

	public Experiment() {
	}

	public Experiment(final CommandLineParam commandLineParam, final SimulationSettings simulationSettings, final PhysicalFactors physicalFactors) {
		this.commandLineParam = commandLineParam;
		this.physicalFactors = physicalFactors;
		this.simulationSettings = simulationSettings;
	}

	public int getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(final int experimentId) {
		this.experimentId = experimentId;
	}

	public Set<GridPoint> getGridPoints() {
		return gridPoints;
	}

	public void setGridPoints(final Set<GridPoint> gridPoints) {
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

	@Override
	public String toString() {
		return "Experiment [experimentId=" + experimentId + " : " + gridPoints + "]";
	}

	public String toStringShort() {
		return "Experiment: id:" + experimentId + " : ExperimentName " + simulationSettings.getExperimentName();
	}

}
