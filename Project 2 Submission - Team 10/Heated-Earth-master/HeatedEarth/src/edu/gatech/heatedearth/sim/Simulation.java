package edu.gatech.heatedearth.sim;

import EarthSim.SimBuffer;
import EarthSim.SimState;

/**
 * 
 */
public abstract class Simulation {

	/**
	 * 
	 */
	public Simulation() {
	}

	/**
	 * 
	 */
	private SimBuffer buffer;

	/**
	 * 
	 */
	public int gridSpacing;

	/**
	 * 
	 */
	public int simTimeStep;

	/**
	 * @return
	 */
	public SimState iterateSimulation() {
		// TODO implement here
		return null;
	}

	public SimBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(SimBuffer buffer) {
		this.buffer = buffer;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}


}