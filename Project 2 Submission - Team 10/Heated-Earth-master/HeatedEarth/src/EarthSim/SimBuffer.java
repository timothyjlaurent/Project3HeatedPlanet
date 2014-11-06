package EarthSim;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 
 */
public class SimBuffer {
	/**
	 * 
	 */
	private ArrayBlockingQueue<SimState> buffer;
	
	
	/**
	 * @param bufferSize 
	 * 
	 */
	public SimBuffer(int bufferSize) {
		buffer = new ArrayBlockingQueue<SimState>(bufferSize);
	}

	/**
	 * @param SimState
	 * @return
	 * @throws InterruptedException 
	 */
	public void Push(SimState simState) throws InterruptedException {
		buffer.put(simState);
	}

	/**
	 * @return
	 * @throws InterruptedException 
	 */
	public SimState take() throws InterruptedException {
		return buffer.take();
	}

	/**
	 * @return
	 * @throws InterruptedException 
	 */
	public SimState poll() throws InterruptedException {
		return buffer.poll();
	}
	
	public ArrayBlockingQueue<SimState> getBuffer() {
		return buffer;
	}

	public void setBuffer(ArrayBlockingQueue<SimState> buffer) {
		this.buffer = buffer;
	}

	public boolean hasCapacity(){
		if ( this.buffer.remainingCapacity() > 0) {
			return true;
		} return false;
	}

	public boolean isEmpty() {
		return buffer.isEmpty();
		
	}
	
}