package edu.gatech.heatsimulation.model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import edu.gatech.heatsimulation.utility.Configuration;
import static edu.gatech.heatsimulation.utility.SimulationConstant.*;

public class SimulationBuffer {
	
	public boolean initialize(Configuration configuration) {
		
		bufferSize = ((configuration == null) || (configuration.getBufferLength() < BUFFER_SIZE_MIN))?
					   BUFFER_SIZE_MIN:configuration.getBufferLength();
		
		if (earthSurfaceArray == null) {
			earthSurfaceArray = new ArrayList<EarthSurface>(bufferSize);
		}		
		earthSurfaceArray.trimToSize();
		return true;
	}
	
	public boolean initBuffer() {
		return clearBuffer();
	}
	
	public EarthSurface popEarthSurface() {
		
		if (earthSurfaceArray.size() < BUFFER_SIZE_MIN) {
			while (true) {
				if (earthSurfaceArray.size() >= BUFFER_SIZE_MIN) {
					break;
				}
				try {
					Thread.sleep(10);					
				} catch (InterruptedException exception) {					
				}
			}
		}
		
		return (EarthSurface)earthSurfaceArray.remove(INDEX_ZERO);
	}
	
	public boolean pushEarthSurface(EarthSurface earthSurface) {
		
		if (earthSurface == null) {
			return false;
		}
		earthSurfaceArray.trimToSize();
		if (earthSurfaceArray.size() >= bufferSize) {
			while (true) {
				if (earthSurfaceArray.size() < bufferSize) {
					break;
				}
				try {
					Thread.sleep(10);					
				} catch (InterruptedException exception) {					
				}
			}
		}
		
		earthSurfaceArray.add(earthSurface);
		earthSurfaceArray.trimToSize();
		
		return true;
	}
		
	public boolean destroy() {
		clearBuffer();
		earthSurfaceArray = null;
		return true;
	}
	
	private boolean clearBuffer() {
		if (earthSurfaceArray != null) {
			earthSurfaceArray.clear();
			earthSurfaceArray.trimToSize();
		}
		return true;
	}
	
	private ArrayList earthSurfaceArray;
	private int bufferSize;
}
