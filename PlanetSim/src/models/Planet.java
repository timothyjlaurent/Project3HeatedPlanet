package models;

import java.util.Collection;

public class Planet {

	private Collection<PlanetCell> planetCells;

	private SimulationSettings simulationSettings;

	private PhysicalFactors physicalFactors;

	public Collection<PlanetCell> getPlanetCells() {
		return planetCells;
	}

	public void setPlanetCells(final Collection<PlanetCell> planetCells) {
		this.planetCells = planetCells;
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

}
