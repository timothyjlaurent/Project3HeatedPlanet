package EarthSim;

import edu.gatech.heatedearth.pres.PresM;
import edu.gatech.heatedearth.pres.PresS;
import edu.gatech.heatedearth.sim.SimM;
import edu.gatech.heatedearth.sim.SimS;
import edu.gatech.heatedearth.sim.Simulation;

/**
 * 
 */
public class SimulationFactory implements Runnable {

	private SimSettings simSettings;
	/**
	 * 
	 */
	public SimulationFactory(SimSettings simSettings) {
		this.simSettings = simSettings;
	}

	/**
	 * @param SimSettings
	 * @return
	 * @throws InterruptedException 
	 */
	public void buildSimulation() throws InterruptedException {
		SimBuffer buffer = new SimBuffer(simSettings.bufferSize);
		SimCmdReceivable sim = null;
		SimCmdReceivable pres = null;
		CmdListener simL = null;
		CmdListener presL = null;
		CmdListener init;
	
		// This is where the factory build logic lives
		if( !simSettings.simulationInitiative ){
            if( simSettings.simulationThread ){
                //threaded slave simulation
                sim = simSettings.simMsg;
                (new Thread(new SimS(buffer , simSettings))).start();
            }
            else {
            	// slave simulation
            	sim = (SimCmdReceivable) new SimS( buffer, simSettings );
            	simL =  (CmdListener) sim;
            }
		}
		
		if( !simSettings.presentationInitiative ){
			if( simSettings.presentationThread ){
				//threaded slave presentation
				pres = new SimCmdMsg();
				(new Thread(new PresS(buffer, simSettings))).start();
			}
			else {
				//slave presentation
				pres = (SimCmdReceivable) new PresS(buffer, simSettings );
				presL = (CmdListener) pres;
			}
		}
		
		if ( simSettings.simulationInitiative ){
			if(simSettings.simulationThread){
				// threaded master/				
				new Thread(new SimM(buffer, simSettings, pres)).start();
				if ( presL != null ){
					presL = (CmdListener) pres;
					presL.listen();
				}
				
			} else {
				init = new SimM(buffer, simSettings, pres);
				init.listen();
			}
		} else if ( simSettings.presentationInitiative ) {
			if(simSettings.presentationThread){
				new Thread(new PresM(buffer, simSettings, sim)).start();
				if(simL != null ) {
					simL = (CmdListener) sim;
					simL.listen();
				}
			} else {
				init = new PresM(buffer,simSettings, sim);
				init.listen();
			}
		} else{
			init= new SimulationCtl(buffer, simSettings, sim, pres);
			init.listen();
		}
	
		return;
	}

	@Override
	public void run() {
		try {
			this.buildSimulation();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}