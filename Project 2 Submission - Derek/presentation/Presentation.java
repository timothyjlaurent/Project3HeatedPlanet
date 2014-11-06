package presentation;

import simulation.SimConstant;
import simulation.SimEngine;
import simulation.SimStatus;
import sync.StatusBuffer;
import userControl.CommunicationConfig;
import userControl.SimulationOptions;

public class Presentation implements Runnable {

	private Gui gui = null;
	private SimStatus simStatus = null;

	private static boolean hold = false;

	public static void setHold(boolean bHold) {
		hold = bHold;
		if (SimConstant.DEBUG) {
			System.out.println("Holding PresThread!");
		}
	}

	private static boolean kill = false;

	public static void setKill(boolean bKill) {
		kill = bKill;
	}

	private Presentation() {
		kill = false;
		hold = false;
		if (!SimConstant.TESTING) {
			gui = Gui.getInstance();
		}
	}

	private static Presentation instance;

	public static Presentation getInstance() {
		if (instance == null) {
			instance = new Presentation();
		}
		return instance;
	}

	public static void destroy() {
		if (instance != null) {
			instance = null;
		}
	}

	public void setStatus(SimStatus simStatus) {
		if (simStatus != null) {
			if (this.simStatus == null) {
				this.simStatus = simStatus;

				pushStatusToGui();
			} else {
				// Get the number of sim steps between this one and the last
				// one?
				// We only keep the simulation status if we're supposed to based
				// on the
				// simulation rate
				if (simStatus.getSimStep() - this.simStatus.getSimStep() >= SimulationOptions
						.getInstance().getVisualizationRate()) {
					this.simStatus = simStatus;

					pushStatusToGui();
				}
			}
		}
	}

	public boolean updateSimData() {
		SimStatus status = null; // lazy initialization...
		SimulationOptions opts = SimulationOptions.getInstance();

		// We're going to get the data via two separate methods... PULL and PULL
		// + BUFFER
		// The difference is where we get the data from. Check the Sim Options
		// to see
		// which it should be.
		CommunicationConfig config = opts.getCommConfig();
		if (config == CommunicationConfig.PULL) {
			// Check the simulation controller to see if we have data available
			status = SimEngine.getInstance().getStatus();
			setStatus(status);
		} else if (config == CommunicationConfig.BUFFER) {
			// Check the SimStatus buff to see if we have data available
			status = StatusBuffer.getInstance().pullStatus();
			setStatus(status);
		} else if (config == CommunicationConfig.PUSH) {
			// Do nothing... sleep a sec to save processor resources...
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}

		// Are we finished?
		if (this.simStatus != null) {
			return !this.simStatus.isFinished();
		} else {
			return false;
		}
	}

	public void pushStatusToGui() {
		if (simStatus != null && !SimConstant.TESTING) {
			gui.updateSimStatus(this.simStatus);
		}

		// try to clear up the memory from this simStatus since it is no longer
		// needed...
		try {
			(this.simStatus).finalize();
		} catch (Throwable e) {
			// e.printStackTrace();
		} finally {
			Runtime.getRuntime().gc();
		}
	}

	public void run() {

		if (SimConstant.DEBUG) {
			System.out.println("Enterring Presentation Thread!");
		}

		SimulationOptions opts = SimulationOptions.getInstance();

		boolean isFinished = false;
		while (!kill && !isFinished) {
			if (!hold) {
				try {
					// Update our simStatus data
					isFinished = !updateSimData();

					Thread.sleep(opts.getVisualizationDelayMilliSeconds());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				// wait a minute so that the computer doesn't slow down too much
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}

		if (SimConstant.DEBUG) {
			System.out.println("Leaving Presentation Thread!");
		}
	}
}