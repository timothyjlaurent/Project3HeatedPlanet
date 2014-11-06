package EarthSim;

/**
 * Reflects the Current State of the Simulation
 */
public class SimState {
	
	private double[][] temperatureGrid;
	private int time;
	private double rotation;
	private double maxDelta;

	/**
	 * Instantiates a new instance of the SimState class
	 * @param bufferSize 
	 * 
	 */
	public SimState(int time, double rotation, double maxDelta, double[][] temperatureGrid) 
	{
		this.time = time;
		this.rotation = rotation;
		this.maxDelta = maxDelta;
		this.temperatureGrid = temperatureGrid;
	}

	/**
	 * 
	 */
	public double[][] temperatureGrid()
	{
		return this.temperatureGrid;
	}

	/**
	 * Gets the current time of the simulation.
	 */
	public int Time()
	{
		return this.time;
	}

	/**
	 * Gets the current rotation of the simulation.
	 */
	public double Rotation()
	{
		return this.rotation;
	}

	/**
	 * Gets the current maximum delta of the simulation.
	 */
	public double MaxDelta()
	{
		return this.maxDelta;
	}

	public double tempMax;
	
	public double tempMin;
	
	public double tempStdev;

	public boolean stabilized;
	
}