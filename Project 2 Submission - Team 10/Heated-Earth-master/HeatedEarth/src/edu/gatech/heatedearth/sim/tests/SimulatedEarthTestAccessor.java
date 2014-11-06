package edu.gatech.heatedearth.sim.tests;

import edu.gatech.heatedearth.sim.EarthCell;
import edu.gatech.heatedearth.sim.SimulatedEarth;

public class SimulatedEarthTestAccessor extends SimulatedEarth
{

	public SimulatedEarthTestAccessor(int gridSpacing) {
		super(gridSpacing);
		// TODO Auto-generated constructor stub
	}
	
	public EarthCell[][] GetEarth()
	{
		return this.earth;
	}
	
	public EarthCell WestNeighbor(int i, int j)
	{
		return this.GetWestNeighbor(i, j);
	}
	
	public EarthCell EastNeighbor(int i, int j)
	{
		return this.GetEastNeighbor(i, j);
	}
	
	public EarthCell NorthNeighbor(int i, int j)
	{		
		return this.GetNorthNeighbor(i, j);
	}
	
	public EarthCell SouthNeighbor(int i, int j)
	{
		return this.GetSouthNeighbor(i, j);
	}
	
	public int GetRows()
	{
		return this.gridRows;
	}
	
	public int GetColumns()
	{
		return this.gridColumns;
	}
	
	public double GetProportionOfEquator()
	{
		return this.proportionOfEquator;
	}
	
	public EarthCell GetCreatedEarthCell(int i, int j)
	{
		return this.CreateEarthCell(i, j);
	}

	public double GetCalculateAngleForRadiation(int i, int j, int longitudeOfSun)
	{
		return this.CalculateAngleForRadiation(i, j, longitudeOfSun);
	}
}
