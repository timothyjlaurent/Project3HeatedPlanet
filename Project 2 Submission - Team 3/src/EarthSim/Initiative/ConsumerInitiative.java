package EarthSim.Initiative;

import EarthSim.Buffer;

public class ConsumerInitiative extends Initiative {

	public ConsumerInitiative(final Buffer buffer) {
		super(buffer);
	}

	@Override
	public void consumerDone() {
		producer.update();
	}
}
