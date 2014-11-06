package EarthSim.Initiative;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import EarthSim.Buffer;
import EarthSim.Earth;
import EarthSim.Presentation;
import EarthSim.Simulation.Simulation;

public abstract class Initiative {

	protected Buffer buffer;

	public Presentation consumer;

	public Simulation producer;

	private Timer timer;

	public Initiative(final Buffer buffer) {
		this.buffer = buffer;
	}

	public void consumerDone() {
	}

	public void producerDone() {
	}

	public Earth last() {
		return buffer.last();
	}

	public Earth read() {
		return buffer.read();
	}

	public void reset() {
		stop();
		buffer.cleanUp();
		buffer = new Buffer(buffer.getSize());
	}

	public void start(final int displayRate) {
		stop();
		timer = new Timer(displayRate, new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent event) {
				consumer.update();
			}

		});
		timer.setInitialDelay(0);
		timer.start();
	}

	public void stop() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}

	public void write(final Earth earth) {
		buffer.write(earth);
	}
}
