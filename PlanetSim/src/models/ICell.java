package models;

public interface ICell {
	/**
	 * Gets the length of the North Side of the cell in meters
	 * @return length of the north side of the cell
	 */
	public double NorthSideLength();

	/**
	 * Gets the length of the South Side of the cell in meters
	 * @return length of the south side of the cell
	 */
	public double SouthSideLength();

	/**
	 * Gets the length of the Vertical Sides of the cell in meters
	 * @return length of the vertical sides of the cell
	 */
	public double VerticalSideLength();

	/**
	 * Gets the surface area of the cell in meters squared
	 * @return the surface area of the cell
	 */
	public double SurfaceArea();

	/**
	 * Gets the current temperature of the cell in kelvin
	 * @return the current temperature of the cell
	 */
	public double Temperature();

	/**
	 * Gets the angle used to compute the amount of radiation on the cell in degrees
	 * @return angle of the cell's radiation
	 */
	public double PercentOfRadiation();

	/**
	 * Sets the Temperature of the cell in Kelvin
	 * @param the new current temperature of the cell
	 */
	public void Temperature(double temperature);

	/**
	 * Sets the angle of radiation for the cell in degrees
	 * @param angle of radiation
	 */
	public void PercentOfRadiation(double angleForRadiation);
}
