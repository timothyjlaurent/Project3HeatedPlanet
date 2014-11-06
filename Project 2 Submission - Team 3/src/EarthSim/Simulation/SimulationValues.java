package EarthSim.Simulation;

import EarthSim.Earth;
import EarthSim.EarthConstants;

public class SimulationValues {
	
	public double averageArea = 0;
	
	public Earth earth;
	
	public double totalArea = 0;
	
	/* 
	 * Note these are only needed once for each row and not for each column 
	 * because the values for each cell in a "stripe" will be the same
	 * 
	 * plateValues[n][0]= Weight of Left Edge
	 * plateValues[n][1]= Weight of Right Edge
	 * plateValues[n][2]= Weight of Bottom Edge
	 * plateValues[n][3]= Weight of Top Edge
	 * plateValues[n][4]= Height
	 * plateValues[n][5]= Perimeter 
	 * plateValues[n][6]= Area
	 * plateValues[n][7]= latitude attenuation
	 */
	double plateValues[][];
	 
	public SimulationValues(Earth earth) {
		this.earth = earth;
		//initialize all plate values
		plateValues = new double[earth.temperatures.length][8];
		double plateLength = (double) EarthConstants.EARTH_CIRCUMFERENCE * ((double) earth.gridSpacing / 360.0);
		for (int i = 0; i < plateValues.length; i++) {
			//calculate the perimeter and edge weights
			double angleBottom = Math.toRadians(i * earth.gridSpacing - 90);
			double angleTop = Math.toRadians(i * earth.gridSpacing + earth.gridSpacing - 90);
			double bottom = Math.cos(angleBottom) * plateLength;
			double top = Math.cos(angleTop) * plateLength;
			double perimeter = bottom + top + plateLength + plateLength;
			double leftRightEdge = plateLength / perimeter;
			double bottomEdge = bottom / perimeter;
			double topEdge = top / perimeter;
			//calculate the height, area and attenuation
			double triangleLength = Math.abs(bottom - top) / 2.0;
			double height = Math.sqrt(Math.pow(plateLength, 2) - Math.pow(triangleLength, 2));
			double area = (bottom + top) * height / 2.0;
			double attenuation = Math.cos(Math.toRadians(earth.gridSpacing / 2.0 + i * earth.gridSpacing - 90));
			//track the total area of all plates
			totalArea += area;
			
			plateValues[i][0] = leftRightEdge;
			plateValues[i][1] = leftRightEdge;
			plateValues[i][2] = bottomEdge;
			plateValues[i][3] = topEdge;
			plateValues[i][4] = height;
			plateValues[i][5] = perimeter;
			plateValues[i][6] = area;
			plateValues[i][7] = attenuation;
		}
		//determine the average area of all cells
		averageArea = totalArea / earth.temperatures.length;
	}
	
	public Earth getEmptyEarth() {
		return new Earth(earth.gridSpacing, earth.timeStep, earth.simulations, 0);
	}
}
