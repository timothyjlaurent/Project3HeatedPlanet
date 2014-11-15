package models;

/**
 * Contains information about a cell on the Earth
 * @author johnh_000
 *
 */
public class EarthCell implements ICell {

	private double northSideLength;
	
	private double southSideLength;
	
	private double verticalSideLength;
	
	private double surfaceArea;
	
	private double temperature;
	
	private double percentOfRadiation;
	
	/**
	 * Instantiates a new instance of the EarthCell class
	 * @param northSideLength the length of the north side of the cell
	 * @param southSideLength the length of the south side of the cell
	 * @param verticalSideLength the length of the vertical side of the cell
	 * @param temperature the temperature of the cell
	 * @param percentOfRadiation percent of total radiation the cell receives
	 * @param surfaceArea the surface area of the cell
	 */
	public EarthCell(double northSideLength, double southSideLength, double verticalSideLength, double temperature, double percentOfRadiation, double surfaceArea)
	{
		this.northSideLength = northSideLength;
		this.southSideLength = southSideLength;
		this.verticalSideLength = verticalSideLength;
		this.temperature = temperature;
		this.percentOfRadiation = percentOfRadiation;
		this.surfaceArea = surfaceArea;
		
		return;
	}

	/**
	 * Gets the length of the North Side of the cell in meters
	 * @return length of the north side of the cell
	 */
	public double NorthSideLength()
	{
		return this.northSideLength;
	}

	/**
	 * Gets the length of the South Side of the cell in meters
	 * @return length of the south side of the cell
	 */
	public double SouthSideLength()
	{
		return this.southSideLength;
	}

	/**
	 * Gets the length of the Vertical Sides of the cell in meters
	 * @return length of the vertical sides of the cell
	 */
	public double VerticalSideLength()
	{
		return this.verticalSideLength;
	}
	
	/**
	 * Gets the surface area of the cell in meters squared
	 * @return the surface area of the cell
	 */
	public double SurfaceArea()
	{
		return this.surfaceArea;
	}

	/**
	 * Gets the current temperature of the cell in kelvin
	 * @return the current temperature of the cell
	 */
	public double Temperature()
	{
		return this.temperature;
	}

	/**
	 * Gets the angle used to compute the amount of radiation on the cell in degrees
	 * @return angle of the cell's radiation
	 */
	public double PercentOfRadiation()
	{
		return this.percentOfRadiation;
	}

	/**
	 * Sets the Temperature of the cell in Kelvin
	 * @param the new current temperature of the cell
	 */
	public void Temperature(double temperature)
	{
		this.temperature = temperature;
	}

	/**
	 * Sets the angle of radiation for the cell in degrees
	 * @param angle of radiation
	 */
	public void PercentOfRadiation(double percentOfRadiation)
	{
		this.percentOfRadiation = percentOfRadiation;
	}
}
