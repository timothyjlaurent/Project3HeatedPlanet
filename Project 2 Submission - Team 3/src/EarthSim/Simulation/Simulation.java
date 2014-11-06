package EarthSim.Simulation;

import EarthSim.Earth;
import EarthSim.Initiative.Initiative;

public class Simulation {

	private final Initiative initiative;

	public Simulation(final Initiative initiative) {
		this.initiative = initiative;
	}

	public void update() {
		doUpdate();
	}

	public void doUpdate() {
		Earth earth = initiative.last();
		final SimulationCalculation calculation = new SimulationCalculation(new SimulationValues(earth));
		earth = calculation.getNextEarth();
		initiative.write(earth);
		initiative.producerDone();
	}
}
