package EarthSim;

import EarthSim.*;

import java.util.concurrent.TimeUnit;

/**
 * This is the third party Controller 
 */
public class SimulationCtl implements CmdListener {
	protected CmdListenable initMsg;
	protected SimBuffer buffer;
	private SimCmdReceivable sim;
	protected SimCmdReceivable pres;
	private String state;
	protected boolean stop;
	private boolean pause;
	private boolean run;
	
	/**
	 * @param pres 
	 * @param sim 
	 * @param simSettings 
	 * @param buffer 
	 * 
	 */
	public SimulationCtl(SimBuffer buffer, SimSettings simSettings, SimCmdReceivable sim, SimCmdReceivable pres) {
		this.setBuffer(buffer);
		this.initMsg = simSettings.initMsg;
		this.sim = sim ;
		this.pres = pres;
		this.pause = true;
	}

	/**
	 * constructor for subclasses
	 */
	public SimulationCtl(){};

	
	/**
	 * Run method, for third party controller :
	 * Strategy : tell simulation to go if the buffer isn't full, 
	 * tell the presentation to go if the buffer isn't empty
	 *  
	 */

	public void iterate() {
		if ( getBuffer().hasCapacity() ){
			try {
				sim.start();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} if( !getBuffer().isEmpty() ){	
			try {
				pres.start();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void pause() {
		boolean paused1 = false;
		while( !paused1 ){
			try {
				sim.pause();
				paused1 = true;
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void resume() {
		
		try {
			sim.resume();
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pres.resume();
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	
	@Override
	public void listen() {
		String msg = "";
		while( !stop ) {
			if( run ==  false ){
				try {
					msg = ((SimCmdMsg)initMsg).messages.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				msg = this.initMsg.poll(); // returns null if no message
			}
			if( msg != null){
			if(msg.equals("start") || msg.equals("resume") ){
				this.run = true;
				msg = "";

			}
			if(msg.equals("pause") ){
				this.run = false; 
				pause(); //  puts the pause message in the synchronous queues blocking if necessary until message is delivered 
				msg="";
			}
			if(msg.equals("stop") ){
				stop(); //  puts the stop message in the synchronous queues and then returns 
				msg="";
			}
			}
			if( this.run ){
				this.iterate();
			}
		}
	}
	

	private void run() {
		this.listen();
	}

	private void stop() {
		boolean paused1 = false;
		boolean paused2 = false;
		while( !paused1 && !paused2 ){
			try {
				sim.stop();
				paused1 = true;
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	private void execute() {
		String message = null;
		message = initMsg.take();
				process(message);
	}
	
	
	private void process(String message) {
		if (message.equals("start") || message.equals("resume")){
			this.pause = false ;
		} else
		if (message.equals("pause")){
			this.pause = true ; 
		}
		
		
	}

	public SimBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(SimBuffer buffer) {
		this.buffer = buffer;
	}


}
