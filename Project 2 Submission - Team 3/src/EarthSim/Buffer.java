package EarthSim;

public class Buffer {

	private Earth[] buffer;
	
	/**
	 * Used to mark when destroying buffer so any waiting threads can be notified
	 */
	private boolean cleaning = false;
	
	/**
	 * Holds a reference to the last written object
	 */
	private Earth last = new Earth();
	
	private Object lock = new Object();
	
	private int reads = 0;
	
	private boolean waiting = false;
	
	private int writes = 0;
	
	public Buffer(int size) {
		buffer = new Earth[size];
	}
	
	public void cleanUp() {
		cleaning = true;
		if (waiting) {
			synchronized(lock) {
				lock.notify();
			}
		}
	}
	
	public int getSize() {
		return (buffer == null) ? 0 : buffer.length;
	}
	
	public boolean isFull() {
		return (writes - reads >= buffer.length);
	}
	
	public Earth last() {
		return last;
	}
	
	/**
	 * If we have read to the end, continue returning the last element.
	 * Otherwise, increment the read counter and check if a thread is waiting to write
	 * @return
	 */
	public Earth read() {
		if (reads >= writes) {
			return last;
		}
		Earth earth = buffer[reads++ % buffer.length];
		if (waiting) {
			synchronized(lock) {
				lock.notify();
			}
		}
		return earth;
	}
	
	/**
	 * If the buffer is full, wait to write this object. Then increment the write counter.
	 * @param earth
	 */
	public void write(Earth earth) {
		if (isFull()) {
			waiting = true;
			synchronized(lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			waiting = false;
			if (cleaning) {
				return;
			}
		}
		last = earth;
		buffer[writes++ % buffer.length] = earth;
	}
}
