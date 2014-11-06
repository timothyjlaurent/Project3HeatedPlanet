package edu.gatech.heatedearth.sim.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.gatech.heatedearth.sim.EarthCell;

public class EarthCellTests {

	@Test
	public void TestNorthSideLength() 
	{
		float expectedNorthSideLength = 3.42f;
		EarthCell target = new EarthCell(expectedNorthSideLength, 432.4f, 243.3f, 43.34f, 23.3f, 4.3f);
		assertEquals(expectedNorthSideLength, target.NorthSideLength(), .0001f);
	}

	@Test
	public void TestSouthSideLength() 
	{
		float expectedSouthSideLength = 3.42f;
		EarthCell target = new EarthCell(432.4f, expectedSouthSideLength, 243.3f, 43.34f, 23.3f, 4.3f);
		assertEquals(expectedSouthSideLength, target.SouthSideLength(), .0001f);
	}

	@Test
	public void TestVerticalSideLength() 
	{
		float expectedVerticalSideLength = 3.42f;
		EarthCell target = new EarthCell(34.0f, 432.4f, expectedVerticalSideLength, 43.34f, 23.3f, 4.3f);
		assertEquals(expectedVerticalSideLength, target.VerticalSideLength(), .0001f);
	}

	@Test
	public void TestSurfaceArea() 
	{
		float expectedArea = 189.39f;
		EarthCell target = new EarthCell(12.4f, 17.9f, 12.8f, 12.8f, 23.3f, expectedArea);
		assertEquals(expectedArea, target.SurfaceArea(), .0001f);
	}

	@Test
	public void TestAngleForRadiation() 
	{
		float expectedAngle = 3.42f;
		EarthCell target = new EarthCell(3.5f, 432.4f, 243.3f, 43.34f, expectedAngle, 23.3f);
		assertEquals(expectedAngle, target.PercentOfRadiation(), .0001f);
	}

	@Test
	public void TestTemperature() 
	{
		float expectedTemperature = 3.42f;
		EarthCell target = new EarthCell(32.5f, 432.4f, 243.3f, expectedTemperature, 43.34f, 4.3f);
		assertEquals(expectedTemperature, target.Temperature(), .0001f);
	}

	@Test
	public void TestAngleForRadiationUpdates() 
	{
		float expectedAngle = 3.42f;
		EarthCell target = new EarthCell(3.5f, 432.4f, 243.3f, 43.34f, 23.3f, 15.3f);
		target.PercentOfRadiation(expectedAngle);
		assertEquals(expectedAngle, target.PercentOfRadiation(), .0001f);
	}

	@Test
	public void TestTemperatureUpdates() 
	{
		float expectedTemperature = 3.42f;
		EarthCell target = new EarthCell(32.5f, 432.4f, 243.3f, 43.34f, 55.2f, 4.3f);
		target.Temperature(expectedTemperature);
		assertEquals(expectedTemperature, target.Temperature(), .0001f);
	}
}
