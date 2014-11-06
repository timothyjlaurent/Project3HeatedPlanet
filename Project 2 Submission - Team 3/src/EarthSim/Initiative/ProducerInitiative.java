package EarthSim.Initiative;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import EarthSim.Buffer;
import EarthSim.Earth;

public class ProducerInitiative extends Initiative {

	/**
	 * track whether the producer is currently pending a write
	 */
	private boolean stalled = false;

	/**
	 * store the last write attempt when pending to avoid idling when producer
	 * has the initiative as a stall can seize the program when not threaded
	 */
	private Earth stashed;

	private boolean stopped = true;

	private final boolean threaded;

	public ProducerInitiative(final Buffer buffer, final boolean threaded) {
		super(buffer);
		this.threaded = threaded;
	}

	/**
	 * Return a timer object that invokes itself again after use. This is the
	 * crux of the producer work loop when it has the initiative.
	 * 
	 * @return
	 */
	private Timer buildTimer() {
		final Timer timer = new Timer(0, new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent event) {
				producer.update();
				if (!stopped) {
					buildTimer().start();
				}
			}

		});
		timer.setInitialDelay(0);
		timer.setRepeats(false);
		return timer;
	}

	/**
	 * Clean up a stalled write attempt, if one exists
	 */
	private void clearStalled() {
		if (stalled) {
			buffer.write(stashed);
			stashed = null;
			stalled = false;
			buildTimer().start();
		}
	}

	@Override
	public Earth read() {
		final Earth earth = super.read();
		clearStalled();
		return earth;
	}

	@Override
	public void reset() {
		super.reset();
		clearStalled();
	}

	@Override
	public void start(final int displayRate) {
		super.start(displayRate);
		stopped = false;
		buildTimer().start();
	}

	@Override
	public void stop() {
		super.stop();
		stopped = true;
	}

	/**
	 * Intercept a write if it would cause a stall and instead stash the object
	 */
	@Override
	public void write(final Earth earth) {
		if (threaded || !buffer.isFull()) {
			buffer.write(earth);
		} else {
			stopped = true;
			stalled = true;
			stashed = earth;
		}
	}
}
