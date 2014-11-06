package edu.gatech.heatedearth.pres;

import EarthSim.SimCmdReceivable;
import EarthSim.SimBuffer;
import EarthSim.SimSettings;
import EarthSim.TempGrid;
import edu.gatech.heatedearth.sim.Simulation;
import EarthSim.*;
import earthWidgets.EarthPanel;

/**
 * 
 */
public class PresM extends SimulationCtl implements Runnable, CmdListener {

	
	private SimSettings simSettings;
	private SimBuffer simBuffer;
	private SimCmdReceivable simReceiver;
	private SimCmdMsg initMsg;
	private boolean run;
	private EarthPanel ep; 



	/**
	 * @param SimSettings
	 * @param SimBuffer
	 * @param Simulation
	 */
	public PresM( SimBuffer simBuffer, SimSettings simSettings,
			SimCmdReceivable sim) {
		this.simSettings = simSettings;
		this.simBuffer = simBuffer;
		this.simReceiver = sim;	
		this.initMsg = simSettings.initMsg;	
		this.ep = (EarthPanel) this.simSettings.dispPanel;
	}
	


	public void run() {
		this.iterate();
		this.listen();
	}


	public void iterate() {
		if(simBuffer.isEmpty()){
			try {
				this.simReceiver.start();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else
		{
			SimState state;
			try {
				state = simBuffer.take();
				ep.updateGrid(new TempGrid(state.temperatureGrid()));
				ep.moveSunPosition(ep.sunDegree() - (float)state.Rotation());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


}