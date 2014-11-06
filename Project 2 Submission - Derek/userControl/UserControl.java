package userControl;

import java.util.Observable;
import java.util.Observer;

import presentation.Presentation;

import simulation.SimConstant;
import simulation.SimEngine;

public class UserControl implements Observer {

	private Thread applicationThread = null;
	private Thread simulationThread = null;
	private Thread presentationThread = null;

	private boolean paused = false;

	public UserControl() {
		SimulationOptions.getInstance().addObserver(this);
	}

	public void update(Observable observable, Object arg1) {
		// The observable should be the simulation options, so cast it for
		// clarity
		SimulationOptions options = (SimulationOptions) observable;

		// Check the status of the options and if we should run, then run
		if (options.getRun() == true) {

			// If we are restarting, then we need to kill the thread before we
			// go forward with a run
			if (options.getResetOnStart() == true) {
				// Kill everything
				killAllThreads();
				paused = false;
			}

			// Should we start a new thread or just come back from a hold?
			if (!paused) {
				// Let's go!
				startAllThreads();
			} else {
				// Let's get things moving again!
				setAllThreadHolds(false);
			}
		} else if (options.getRun() == false) {
			// Just hold everything
			setAllThreadHolds(true);
			paused = true;
		}
	}

	private void startAllThreads() {
		SimulationOptions options = SimulationOptions.getInstance();

		// Do we need a simulation thread?
		SimEngine simulationEngine = SimEngine.getInstance();
		SimEngine.setKill(false);
		SimEngine.setHold(false);
		simulationEngine.initStatus();
		if (options.getThreadConfig() == ThreadConfig.SIMULATIONONLY
				|| options.getThreadConfig() == ThreadConfig.SIMULATIONANDPRESENTATION) {
			// OK, create our simulation thread
			simulationThread = new Thread(simulationEngine, "Sim Thread");
		}

		// Do we need a presentation thread?
		Presentation pres = Presentation.getInstance();
		Presentation.setKill(false);
		Presentation.setHold(false);
		if (options.getThreadConfig() == ThreadConfig.PRESENTATIONONLY
				|| options.getThreadConfig() == ThreadConfig.SIMULATIONANDPRESENTATION) {
			// OK, create our presentation thread
			presentationThread = new Thread(pres, "Pres Thread");
		}

		// Setup the application thread based on the threads that currently
		// exists / don't exist
		if (simulationThread == null || presentationThread == null) {
			applicationThread = new Thread(new AppThread(simulationThread,
					presentationThread), "App Thread");
		}

		// Run any necessary threads
		if (simulationThread != null) {
			simulationThread.start();
		}
		if (presentationThread != null) {
			presentationThread.start();
		}
		if (applicationThread != null) {
			applicationThread.start();
		}

	}

	private void setAllThreadHolds(boolean bHold) {
		SimEngine.setHold(bHold);
		Presentation.setHold(bHold);
		AppThread.setHold(bHold);
	}

	private void killAllThreads() {
		// Kill everything
		SimEngine.setKill(true);
		Presentation.setKill(true);
		AppThread.kill(); // This should kill the threads nearly
							// instantaneously... but let's make sure

		// boolean simDead = false, presDead = false, appDead = false;
		// while (!simDead || !presDead || !appDead) {
		// // Threads haven't died yet... update and wait for them here
		// if (simThread != null) {simDead = !simThread.isAlive();} else
		// {simDead = true;}
		// if (presThread != null) {presDead = !presThread.isAlive();} else
		// {presDead = true;}
		// if (appThread != null) {appDead = !appThread.isAlive();} else
		// {appDead = true;}
		// try {
		// Thread.sleep(1);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }

		try {
			if (simulationThread != null) {
				simulationThread.join();
			}
			if (presentationThread != null) {
				presentationThread.join();
			}
			if (applicationThread != null) {
				applicationThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// All threads are dead... return them for re-use
		simulationThread = null;
		presentationThread = null;
		applicationThread = null;

		// Also kill classes
		SimEngine.destroy();
		Presentation.destroy();
	}
}

class AppThread implements Runnable {
	private static boolean hold = false;
	private static boolean kill = false;

	public static void setHold(boolean bHold) {
		hold = bHold;
		if (SimConstant.DEBUG) {
			System.out.println("Holding AppThread!");
		}
	}

	public static void kill() {
		kill = true;
	}

	private Thread simulationThread = null;
	private Thread presentationThread = null;

	public AppThread(Thread simThread_in, Thread presThread_in) {
		kill = false;
		hold = false;
		simulationThread = simThread_in;
		presentationThread = presThread_in;
	}

	public void run() {

		if (SimConstant.DEBUG) {
			System.out.println("Enterring Application Thread!");
		}

		// Get the necessary instances of simulation and presentation
		SimEngine simulation = SimEngine.getInstance();
		Presentation presentation = Presentation.getInstance();

		// run the main application loop... if we have to
		boolean simulationFinished = false, presentationFinished = false; // Assume
																			// that
																			// we're
																			// already
																			// done!
		while ((simulationThread == null || presentationThread == null)
				&& (!simulationFinished || !presentationFinished) && !kill) {

			// Take a simulation step only if it's not running in a thread
			if (!hold) {
				if (simulationThread == null) {
					simulationFinished = !simulation.runStep();
				} else {
					simulationFinished = true;
				}
				if (presentationThread == null) {
					presentationFinished = !presentation.updateSimData();
				} else {
					presentationFinished = true;
				}
			} else {
				// wait a minute so that the computer doesn't slow down too much
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}

		if (SimConstant.DEBUG) {
			System.out.println("Leaving Application Thread!");
		}
	}
}