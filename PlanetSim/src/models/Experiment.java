package models;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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
import javax.persistence.Transient;

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

	@Transient
	private Map<Date, Set<GridPoint>> mapOfGridPoints;

	public int getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(final int experimentId) {
		this.experimentId = experimentId;
	}

	public Map<Date, Set<GridPoint>> getGridPointMap() {
		if (mapOfGridPoints == null) {
			mapOfGridPoints = new HashMap<Date, Set<GridPoint>>();
			for (final GridPoint gridPoint : getGridPoints()) {
				if (mapOfGridPoints.get(gridPoint) == null) {
					mapOfGridPoints.put(gridPoint.getDateTime(), new HashSet<GridPoint>(Arrays.asList(gridPoint)));
				} else {
					mapOfGridPoints.get(gridPoint).add(gridPoint);
				}
			}
		}
		return mapOfGridPoints;
	}

	public void setGridPointMap(final Map<Date, Set<GridPoint>> mapOfGridPoints) {
		this.mapOfGridPoints = mapOfGridPoints;
		final Set<GridPoint> gridPoints = new HashSet<GridPoint>();
		for (final Entry<Date, Set<GridPoint>> gridPointSet : mapOfGridPoints.entrySet()) {
			for (final GridPoint gridPoint : gridPointSet.getValue()) {
				gridPoint.setDateTime(gridPointSet.getKey());
			}
			gridPoints.addAll(gridPointSet.getValue());
		}
		setGridPoints(gridPoints);
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

	public int getNumOfTimeSteps() {
		return 5;
	}

	public int getNumOfRegions() {
		return 5;
	}

	@Override
	public String toString() {
		return "Experiment [experimentId=" + experimentId + ", simulationSettings=" + simulationSettings + ", physicalFactors=" + physicalFactors + ", commandLineParam=" + commandLineParam + ", mapOfGridPoints=" + mapOfGridPoints + "]";
	}

}
