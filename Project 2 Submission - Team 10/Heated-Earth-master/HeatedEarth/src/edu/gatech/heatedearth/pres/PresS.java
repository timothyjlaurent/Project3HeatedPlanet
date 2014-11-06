package edu.gatech.heatedearth.pres;

import java.awt.Graphics;
import java.sql.Time;

import EarthSim.*;
import earthWidgets.*;

/**
 * 
 */
public class PresS 
			implements CmdListener, Runnable, SimCmdReceivable {
	private EarthPanel ep;
	private SimBuffer buffer ;
	private CmdListenable presMsg;
	private SimSettings settings;
	private int dispRate;
	private float lastdisp;
	private boolean run;
	/**
	 * 
	 */
	public PresS() {
	}

	/**
	 * @param SimSettings
	 * @param SimBuffer
	 */
	public PresS( SimBuffer simBuffer, SimSettings simSettings) {
		this.settings = simSettings;
		ep = simSettings.dispPanel;
		ep.reset();
		ep.drawGrid(  simSettings.gridSize  );
		settings = simSettings;
		buffer = simBuffer;
		dispRate = simSettings.displayRate;
		lastdisp = System.currentTimeMillis();
	}

	public void iterate() {
		long curtime = System.currentTimeMillis();
//		refresh timer
		if ( curtime - lastdisp < dispRate ){
			try {
				Thread.sleep( (long) (dispRate - (curtime - lastdisp))  );
				curtime = System.currentTimeMillis();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		
		lastdisp = curtime;
		
		SimState state = null; 
		try {
			state = buffer.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ep.moveSunPosition( (float)state.Rotation());
		ep.updateGrid(new TempGrid(state.temperatureGrid()));
		
	}

	public void run() {
		// TODO Auto-generated method stub
		this.listen();
	}

	
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	
	public void restart() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	
	
		
			@Override
	public void listen() {
		String msg = "";
		while(run){
			msg = presMsg.take();
			if ( msg.equals("start") || msg.equals("resume")){
				iterate();
			}
			else if(msg.equals("reset")){
				ep.reset();
			}	
			if ( msg.equals("stop")){
				run = false;
			}
		}
	}	
	



	@Override
	public void start() throws InterruptedException {
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
