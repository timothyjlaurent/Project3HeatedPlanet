package edu.gatech.heatedearth.sim;
import EarthSim.SimState;

public class SimulatedEarth {

	private static int widthInDegrees = 360;
	private static int heightInDegrees = 180;
	private static int circumferenceOfEarth = 40030140;
	private static double surfaceAreaOfEarth = 510072000000000.0;
	private static int initialCellTemperature = 288;
	private static double degreesRotatedPerMinute = 360.0/1440.0;

	private static double heatPerMinute = 5000 ;

	private static double heatPerIteration ;
	
	private int gridSpacing;
	
	protected int gridRows;
	protected int gridColumns;
	protected double proportionOfEquator;
	
	protected EarthCell[][] earth;

	/**
	 * 
	 */
	public int time;

	/**
	 * 
	 */
	public double rotation;

	/**
	 * 
	 */
	public double maxDelta;

	public double max;
	public double min;
	public double stddev;
	public double mean; 

	
	
	/**
	 * 
	 * @param gridSpacing number of degrees for the grid; must divide evenly into 180
	 */
	public SimulatedEarth (int gridSpacing)
	{
		this.gridSpacing = gridSpacing;
		this.gridColumns = SimulatedEarth.widthInDegrees / gridSpacing;
		this.gridRows = SimulatedEarth.heightInDegrees / gridSpacing;
		this.proportionOfEquator = ((double)gridSpacing) / SimulatedEarth.widthInDegrees;
		this.earth = new EarthCell[this.gridRows][this.gridColumns];
		
		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{
				this.earth[i][j] = this.CreateEarthCell(i,j);
			}
		}
		
		this.time = 0;
		this.rotation = 0;
		this.maxDelta = 0;
		
		return;
	}
	
	/**
	 * Gets a value representing the current state of the simulation
	 * @return the current simulation state
	 */
	public SimState getCurrentSimulationState()
	{
		double[][] temperatureGrid = new double[this.gridRows][this.gridColumns];

		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{
				temperatureGrid[i][j] = this.earth[i][j].Temperature();
			}
		}	
		
		return new SimState(this.time, this.rotation, this.maxDelta, temperatureGrid);
	}
	
	/**
	 * Simulates an interation of earth based on the time passed
	 * @param timePassed
	 */
	public void simulateIteration(int timePassed)
	{
		this.heatPerIteration = timePassed * this.heatPerMinute;
		this.diffuseHeatFromNeighbors();
		this.heatGridFromSun();
		this.CoolEarth(SimulatedEarth.heatPerIteration);
		this.rotateGrid(this.degreesToRotate(timePassed));
	}
	
	protected void CoolEarth(double heatGain)
	{
		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{
//				this.earth[i][j].Temperature(this.earth[i][j].Temperature() 
//												-
//												(temperatureChange * this.earth[i][j].SurfaceArea()
//												    /
//												    SimulatedEarth.surfaceAreaOfEarth));
				
				
				this.earth[i][j].Temperature( 
						this.earth[i][j].Temperature()
						- heatGain * this.earth[i][j].SurfaceArea() /SimulatedEarth.surfaceAreaOfEarth // this grid point's portion of the heat gained
								/ this.earth[i][j].SurfaceArea() ) ; // divide heat by area to get temp.

			}
		}
	}
	
	protected void rotateGrid(double degrees)
	{
		this.rotation = (this.rotation + degrees) % SimulatedEarth.widthInDegrees;
		double totalAngles = 0.0;
		
		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{
				totalAngles = this.CalculateAngleForRadiation(i, j, this.rotation) + totalAngles;
			}
		}

		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{	
				this.earth[i][j].PercentOfRadiation( 
						(this.CalculateAngleForRadiation(i, j, this.rotation) * this.earth[i][j].SurfaceArea())
						/
						(totalAngles * SimulatedEarth.surfaceAreaOfEarth));
			}
		}
	}
	
	protected double diffuseHeatFromNeighbors()
	{
		double[][] newTemperatures = new double[this.gridRows][this.gridColumns];

		double heatChange = 0;

		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{
				EarthCell current = this.earth[i][j];
				double cellPerimeter = current.NorthSideLength() + current.SouthSideLength() + 2 * current.VerticalSideLength();
				
				double neighborTemp = (current.VerticalSideLength() / cellPerimeter) * this.GetEastNeighbor(i, j).Temperature();
				neighborTemp += (current.VerticalSideLength() / cellPerimeter) * this.GetWestNeighbor(i, j).Temperature();
				neighborTemp += (current.SouthSideLength() / cellPerimeter) * this.GetSouthNeighbor(i, j).Temperature();
				neighborTemp += (current.NorthSideLength() / cellPerimeter) * this.GetNorthNeighbor(i, j).Temperature();


				newTemperatures[i][j] = (neighborTemp + current.Temperature())/2;

				heatChange += ( newTemperatures[i][j] * current.SurfaceArea() ) - current.Temperature() * current.SurfaceArea();

			}
		}		

		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{
//				temperatureChange = newTemperatures[i][j] - this.earth[i][j].Temperature();
				this.earth[i][j].Temperature(newTemperatures[i][j]);
			}
		}
		
		return heatChange;
	}
	
	protected void heatGridFromSun()
	{
		double heatgain = 0 ;
		for (int i = 0; i < this.gridRows; ++i)
		{
			for (int j = 0; j < this.gridColumns; ++j)
			{	
				this.earth[i][j].Temperature(this.earth[i][j].Temperature() + SimulatedEarth.heatPerIteration * this.earth[i][j].PercentOfRadiation());
				
				heatgain += this.earth[i][j].SurfaceArea() * SimulatedEarth.heatPerIteration * this.earth[i][j].PercentOfRadiation() ;
				
			}
		}
	}
	
	protected double degreesToRotate (int timePassed)
	{
		return SimulatedEarth.degreesRotatedPerMinute * timePassed;
	}
	
	/**
	 * Creates a new EarthCell based on it's coordinates.
	 * @param i
	 * @param j
	 * @return An EarthCell populated with it's side lengths, starting temperature, area and angle for radiation
	 */
	protected EarthCell CreateEarthCell(int i, int j)
	{
		double latitude = this.CalculateLatitude(i);

		double verticalSideLength = SimulatedEarth.circumferenceOfEarth * this.proportionOfEquator;
		double northSideLength = Math.cos(Math.toRadians(latitude + this.gridSpacing)) * verticalSideLength;
		double southSideLength = Math.cos(Math.toRadians(latitude)) * verticalSideLength;
		
		double height = Math.sqrt(verticalSideLength * verticalSideLength - .25 * (northSideLength - southSideLength) * (northSideLength - southSideLength));
		double surfaceArea = .5 * (northSideLength + southSideLength) * height;
		
		double temperature = SimulatedEarth.initialCellTemperature;
		return new EarthCell(northSideLength, southSideLength, verticalSideLength, temperature, this.CalculateAngleForRadiation(i, j, 0), surfaceArea);	
	}

	private double CalculateLongitude(int j) 
	{
		return j < this.gridColumns/2 ? - ((j+1) * this.gridSpacing) : SimulatedEarth.widthInDegrees - ((j+1) * this.gridSpacing);
	}

	private double CalculateLatitude(int i) 
	{
		return (i-((double)this.gridRows/2)) * this.gridSpacing;
	}
	
	protected double CalculateAngleForRadiation(int i, int j, double longitudeOfSun)
	{
		double longitudeFromSun = (this.CalculateLongitude(j) - longitudeOfSun) % SimulatedEarth.widthInDegrees;
		return Math.abs(longitudeFromSun) < 90 ? Math.cos(Math.toRadians(longitudeFromSun)) * Math.cos(Math.toRadians(this.CalculateLatitude(i))) : 0;
	}
	
	protected EarthCell GetWestNeighbor(int i, int j)
	{
		return this.earth[i][(j + 1) % this.gridColumns];
	}
	
	protected EarthCell GetEastNeighbor(int i, int j)
	{
		return this.earth[i][(j - 1 + this.gridColumns) % this.gridColumns];
	}
	
	protected EarthCell GetNorthNeighbor(int i, int j)
	{		
		return i + 1 < this.gridRows ? this.earth[i + 1][j] : this.earth[i][(j + (this.gridColumns/2)) % this.gridColumns];
	}
	
	protected EarthCell GetSouthNeighbor(int i, int j)
	{
		return i - 1 >= 0 ? this.earth[i-1][j] : this.earth[i][(j + (this.gridColumns/2)) % this.gridColumns];
	}
}
