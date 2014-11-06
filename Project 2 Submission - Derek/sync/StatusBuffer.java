package sync;

import java.util.Queue;
import java.util.LinkedList;
import simulation.SimStatus;

public class StatusBuffer {

	// The buffer data
	private Queue<SimStatus> bufferQueue;

	private static StatusBuffer instance;

	public static StatusBuffer getInstance() {
		if (instance == null) {
			instance = new StatusBuffer();
		}
		return instance;
	}

	public static void destroy() {
		if (instance != null) {
			instance = null;
		}
	}

	public StatusBuffer() {
		this.bufferQueue = new LinkedList<SimStatus>();
	}

	public SimStatus pullStatus() {
		return bufferQueue.poll();
	}

	public boolean pushStatus(SimStatus simStatus) {
		return bufferQueue.offer(simStatus);
	}
}