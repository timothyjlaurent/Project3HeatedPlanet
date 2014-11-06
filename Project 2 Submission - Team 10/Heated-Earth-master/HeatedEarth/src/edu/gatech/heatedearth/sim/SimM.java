package edu.gatech.heatedearth.sim;

import EarthSim.*;

/**
 * This is the simulation with initiative 
 */
public class SimM extends SimulationCtl implements Runnable, CmdListener {

	private SimBuffer buffer;
	private SimCmdReceivable pres;
	private SimSettings settings;
	private SimulatedEarth simulation;
	private int displayRate;
	private int gridSize;
	private int timeStep;
	
	/**
	 * 
	 * @param simBuffer
	 * @param pres2
	 * @param initMsg
	 */
	public SimM(SimBuffer simBuffer, SimSettings simSettings,  SimCmdReceivable pres) {
		this.buffer =  simBuffer ;
		this.pres = pres;
		this.initMsg = simSettings.initMsg;
		this.settings = simSettings;
		this.simulation = new SimulatedEarth(settings.gridSize);
		this.displayRate = settings.displayRate;
		this.gridSize = settings.gridSize;
		this.timeStep = settings.timeStep;
	}
	
	public void iterate() {
		this.simulation.simulateIteration(this.timeStep);
		try {
			this.buffer.Push(this.simulation.getCurrentSimulationState());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!buffer.isEmpty() ){
			try {
				pres.start();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void pause(){
		boolean paused1 = false;
	
		while( !paused1 ){

			try {
				pres.pause();
				paused1 = true; 
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void stop() {
		boolean paused2 = false;
		while( !paused2 ){
	
			try {
				pres.stop();
				paused2 = true; 
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.stop = true;
	}
	
	
	public void run(){
		this.iterate();
		this.listen();
	}

}