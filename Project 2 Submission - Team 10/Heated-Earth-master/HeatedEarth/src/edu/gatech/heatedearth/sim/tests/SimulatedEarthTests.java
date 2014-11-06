package edu.gatech.heatedearth.sim.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.gatech.heatedearth.sim.EarthCell;

public class SimulatedEarthTests {
	@Test
	public void TestGridRows()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		assertEquals(18, target.GetRows());
	}

	@Test
	public void TestGridColumns()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		assertEquals(36, target.GetColumns());
	}

	@Test
	public void TestProportionOfEquator()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		assertEquals(0.0277, target.GetProportionOfEquator(), .0001);
	}
	
	@Test
	public void TestGetWestNeighbor()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.WestNeighbor(5,5);
		assertEquals(neighbor, target.GetEarth()[5][6]);
	}

	@Test
	public void TestGetWestNeighborOverlap()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.WestNeighbor(5,35);
		assertEquals(neighbor, target.GetEarth()[5][0]);
	}
	
	@Test
	public void TestGetEastNeighbor()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.EastNeighbor(5,5);
		assertEquals(neighbor, target.GetEarth()[5][4]);
	}

	@Test
	public void TestGetEastNeighborOverlap()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.EastNeighbor(5,0);
		assertEquals(neighbor, target.GetEarth()[5][35]);
	}
	
	@Test
	public void TestGetNorthNeighbor()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.NorthNeighbor(5,5);
		assertEquals(neighbor, target.GetEarth()[6][5]);
	}

	@Test
	public void TestGetNorthNeighborOverlap()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.NorthNeighbor(17,5);
		assertEquals(neighbor, target.GetEarth()[17][23]);
	}
	
	@Test
	public void TestGetSouthNeighbor()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.SouthNeighbor(5,5);
		assertEquals(neighbor, target.GetEarth()[4][5]);
	}

	@Test
	public void TestGetSouthNeighborOverlap()
	{
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		EarthCell neighbor = target.SouthNeighbor(0,5);
		assertEquals(neighbor, target.GetEarth()[0][23]);
	}

	@Test
	public void TestCreateEarthCell()
	{
		double expectedNorthSideLength = 962975.5043;  
		double expectedSouthSideLength = 851801.8417;
		double expectedVerticalSideLength = 1111948.33333; 
		double expectedTemperature = 288; 
		double expectedInitialAngleForRadiation = 0;
		double expectedSurfaceArea = 1007707800000.0;	
		EarthCell target = new SimulatedEarthTestAccessor(10).GetCreatedEarthCell(5, 10);

		assertEquals(expectedVerticalSideLength, target.VerticalSideLength(), 100);
		assertEquals(expectedNorthSideLength, target.NorthSideLength(), 100);
		assertEquals(expectedSouthSideLength, target.SouthSideLength(), 100);
		assertEquals(expectedTemperature, target.Temperature(), .0001);
		assertEquals(expectedInitialAngleForRadiation, target.PercentOfRadiation(), 100);
		assertEquals(expectedSurfaceArea, target.SurfaceArea(), 1000000);
	}

	@Test
	public void TestCreateEarthCellAngle()
	{
		double expectedInitialAngleForRadiation = 0.3830;
		EarthCell target = new SimulatedEarthTestAccessor(10).GetCreatedEarthCell(5, 5);
		
		assertEquals(expectedInitialAngleForRadiation, target.PercentOfRadiation(), .0001);
	}

	@Test
	public void TestCalculateAngleForRadiation()
	{
		int i = 8;
		int j = 2;
		int longitudeOfSun = 45;
		int gridSpacing = 10;
		double longitude = (-longitudeOfSun - ((j+1) * gridSpacing)) % 360;
		double expectedAngleForRadiation = Math.cos(Math.toRadians(longitude)) * Math.cos(Math.toRadians(80));
		
		assertEquals(expectedAngleForRadiation, 
				new SimulatedEarthTestAccessor(gridSpacing).GetCalculateAngleForRadiation(i, j, longitudeOfSun),
				.0001);
	}

	@Test
	public void TestCurrentSimulationState()
	{
		double expectedTemperature = 375;
		double expectedRotation = 3.5;
		double expectedMaxDelta = 23;
		int expectedTime = 12;
		SimulatedEarthTestAccessor target = new SimulatedEarthTestAccessor(10);
		target.GetEarth()[8][12].Temperature(expectedTemperature);
		target.time = expectedTime;
		target.rotation = expectedRotation;
		target.maxDelta = expectedMaxDelta;		
		
		EarthSim.SimState result = target.getCurrentSimulationState();
		
		assertEquals(expectedTemperature, result.temperatureGrid()[8][12], 0);
		assertEquals(expectedTime, result.Time(), 0);
		assertEquals(expectedMaxDelta, result.MaxDelta(), 0);
		assertEquals(expectedRotation, result.Rotation(), 0);
	}
}
