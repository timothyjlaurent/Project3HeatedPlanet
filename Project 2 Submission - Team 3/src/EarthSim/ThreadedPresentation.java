package EarthSim;

import EarthSim.Initiative.Initiative;

public class ThreadedPresentation extends Presentation {

	private static final long serialVersionUID = 1L;

	private final Object lock = new Object();

	public ThreadedPresentation(final Initiative initiative) {
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
