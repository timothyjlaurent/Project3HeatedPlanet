package edu.gatech.heatedearth.sim;

import EarthSim.*;

/**
 * 
 */
public class SimS implements
		Runnable, SimCmdReceivable, CmdListener {

	private int displayRate;
	private int gridSize;
	private int timeStep;
	private SimBuffer buffer;
	private CmdListenable cmd;
	private SimSettings settings; 
	private boolean stop = false;
	private SimulatedEarth simulation;

	
	/**
	 * @param SimBuffer
	 * @param t
	 * @param t
	 * @param t
	 */
	public SimS(SimBuffer simBuffer, SimSettings settings) {
		this.simulation = new SimulatedEarth(settings.gridSize);
		this.displayRate = settings.displayRate;
		this.gridSize = settings.gridSize;
		this.timeStep = settings.timeStep;
		this.buffer = simBuffer;
		this.settings = settings;
		this.cmd = (CmdListenable) settings.simMsg;
	}

	public void iterate() {
		this.simulation.simulateIteration(this.timeStep);
		try {
			this.buffer.Push(this.simulation.getCurrentSimulationState());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public void pause() {
			
	}

	
	public void stop() {
		this.stop = true; 
	}

	
	public void run() {
		this.listen();
	}

	@Override
	public void listen() {
		String msg = "";
		while(!stop){
			msg = cmd.take();
			if ( msg.equals("start") || msg.equals("resume")){
				iterate();
			}
			else if(msg.equals("refresh")){
				refresh();
			}else if(msg.equals("stop")){
				stop();
			}
		}
	}

	private void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		iterate();
	}

	@Override
	public void resume() throws InterruptedException {
		iterate();
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	
}