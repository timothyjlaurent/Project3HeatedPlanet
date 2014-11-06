package EarthSim;

import earthWidgets.TemperatureGrid;

public class TempGrid implements TemperatureGrid{
	double[][] grid;
	
	@Override
	public double getTemperature(int x, int y) {
		// TODO Auto-generated method stub
		
		// The generator expect temps in celcius.
		return grid[y][x] - 273;
	}

	@Override
	public float getCellHeight(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public TempGrid(double[][] grid){
		this.grid = grid;
		
	}
	
}
