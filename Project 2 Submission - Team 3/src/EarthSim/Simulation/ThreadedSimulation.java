package EarthSim.Simulation;

import EarthSim.Initiative.Initiative;

public class ThreadedSimulation extends Simulation {

	private final Object lock = new Object();

	public ThreadedSimulation(final Initiative initiative) {
		super(initiative);
		new Thread() {

			@Override
			public void run() {
				while (true) {
					try {
						synchronized (lock) {
							lock.wait();
						}
						doUpdate();
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();
	}

	@Override
	public void update() {
		synchronized (lock) {
			lock.notify();
		}
	}
}
