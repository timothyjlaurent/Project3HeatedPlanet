package EarthSim;

public class Earth {

	/**
	 * The average temperature value across all plates
	 */
	public double averageTemperature;
	
	/**
	 * Number of columns in the earth representation
	 */
	public int columns;
	
	/**
	 * Degrees of precision in grid
	 */
	public int gridSpacing;
	
	/**
	 * The maximum temperature value across all plates
	 */
	public double maximumTemperature;
	
	/**
	 * The minimum temperature value across all plates
	 */
	public double minimumTemperature;
	
	/**
	 * Current rotation of the earth as the column directly under the sun
	 */
	public int rotation;
	
	/**
	 * Number of rows in the earth simulation
	 */
	public int rows;
	
	/**
	 * Total number of simulations represented
	 */
	public int simulations;
	
	/**
	 * Temperature value for every cell
	 */
	public double[][] temperatures;
	
	/**
	 * The number of minutes to advance in each simulation iteration
	 */
	public int timeStep;
	
	public Earth() {
		this(EarthConstants.DEFAULT_GRID_SPACING, EarthConstants.DEFAULT_TIME_STEP, 0, EarthConstants.START_TEMP);
	}
	
	public Earth(int gridSpacing, int timeStep) {
		this(gridSpacing, timeStep, 0, EarthConstants.START_TEMP);
	}
	
	public Earth(int gridSpacing, int timeStep, int simulations) {
		this(gridSpacing, timeStep, simulations, EarthConstants.START_TEMP);
	}
	
	public Earth(int gridSpacing, int timeStep, int simulations, int initialTemp) {
		this.gridSpacing = gridSpacing;
		this.timeStep = timeStep;
		this.simulations = simulations;
		
		averageTemperature = initialTemp;
		columns = EarthConstants.EARTH_DEGREES / this.gridSpacing;
		maximumTemperature = initialTemp;
		minimumTemperature = initialTemp;
		rows = EarthConstants.EARTH_DEGREES / 2 / this.gridSpacing;
		temperatures = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				temperatures[i][j] = initialTemp;
			}
		}
	}
	
	public double getTemperatureRange() {
		return Math.abs(maximumTemperature - minimumTemperature);
	}
}
