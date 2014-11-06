package EarthSim.Simulation;

import EarthSim.Earth;
import EarthSim.EarthConstants;

public class SimulationCalculation {

	private Earth earth;
	private final SimulationValues simulationValues;

	public SimulationCalculation(final SimulationValues simulationValues) {
		this.simulationValues = simulationValues;
	}

	private int getColumnsRotated() {
		final double elapsedTime = (double) earth.timeStep * (double) earth.simulations;
		final double degreesRotated = (elapsedTime / 4.0) % 360;
		return (int) (degreesRotated / earth.gridSpacing);
	}

	public Earth getNextEarth() {
		// initialize the earth from the simulation values
		earth = simulationValues.getEmptyEarth();
		heatOut(heatIn());
		heatShared();
		earth.simulations++;
		earth.rotation = getColumnsRotated();
		return earth;
	}

	private double heatIn() {
		double totalHeating = 0;
		// get the number of rotated columns for the current simulation number
		final int columnsRotated = earth.rotation;
		for (int i = 0; i < earth.rows; i++) {
			// todo: clean up this loop?
			for (int j = columnsRotated - (earth.rows / 2); j < columnsRotated + (earth.rows / 2); j++) {
				int k = j;
				if (j < 0) {
					k += earth.columns;
				} else if (j >= earth.columns) {
					k -= earth.columns;
				}
				final int plateValueIndex = j + earth.rows / 2 - columnsRotated;

				// note: this implementation still has the effect of jittery
				// attenuation and the angle of incidence is not updated until a
				// full column rotation occurs
				final double addedHeat = earth.timeStep * EarthConstants.HEAT_PER_MINUTE * simulationValues.plateValues[i][7] * simulationValues.plateValues[plateValueIndex][7];
				earth.temperatures[i][k] += addedHeat;
				totalHeating += addedHeat;
			}
		}
		return totalHeating;
	}

	private void heatOut(final double totalHeating) {
		double coolingCoefficient = 0;
		// first calculate the sum of all areaRatio*tempRatio this will be
		// needed to determine how much heat each cell sheds
		for (int i = 0; i < earth.rows; i++) {
			// note that this ratio is the same for all cols in the same row
			final double areaRatio = simulationValues.plateValues[i][6] / simulationValues.averageArea;
			for (int j = 0; j < earth.columns; j++) {
				final double tempRatio = simulationValues.earth.temperatures[i][j] / simulationValues.earth.averageTemperature;
				coolingCoefficient += tempRatio * areaRatio;
			}
		}
		// coefficient is the TOTAL heat absorbed divided by the sum of the two
		// ratio multiplied out as above. then the correct heat loss for each
		// cell can be determined
		coolingCoefficient = totalHeating / coolingCoefficient;

		for (int i = 0; i < earth.rows; i++) {
			// note that this ratio is the same for all cols in the same row
			final double areaRatio = simulationValues.plateValues[i][6] / simulationValues.averageArea;
			for (int j = 0; j < earth.columns; j++) {
				final double tempRatio = simulationValues.earth.temperatures[i][j] / simulationValues.earth.averageTemperature;
				earth.temperatures[i][j] -= coolingCoefficient * tempRatio * areaRatio;
			}
		}
	}

	private void heatShared() {
		double totalTemperature = 0;
		double maximumTemperature = 0;
		double minimumTemperature = 0;
		for (int i = 0; i < earth.rows; i++) {
			for (int j = 0; j < earth.columns; j++) {
				int top = i + 1;
				int bottom = i - 1;
				int left = j - 1;
				int right = j + 1;

				// if there is no row above, use this row. top length will be 0
				// so impact on weighted temp
				if (top == earth.rows) {
					top = earth.rows - 1;
				}
				// if there is no row below, use this row. bottom length will be
				// 0 so impact on weighted temp
				if (bottom < 0) {
					bottom = 0;
				}
				// if there is no col to the left, use the last col (wraps
				// around)
				if (left < 0) {
					left = earth.columns - 1;
				}
				// if there is no col to the right, use the first col (wraps
				// around)
				if (right == earth.columns) {
					right = 0;
				}

				earth.temperatures[i][j] += simulationValues.earth.temperatures[i][left] * simulationValues.plateValues[i][0] + simulationValues.earth.temperatures[i][right] * simulationValues.plateValues[i][1] + simulationValues.earth.temperatures[bottom][j] * simulationValues.plateValues[i][2] + simulationValues.earth.temperatures[top][j] * simulationValues.plateValues[i][3];

				// do not allow temp to fall below 0 degrees K
				if (earth.temperatures[i][j] < 0) {
					earth.temperatures[i][j] = .01;
				}

				totalTemperature += earth.temperatures[i][j];

				maximumTemperature = Math.max(maximumTemperature, earth.temperatures[i][j]);
				minimumTemperature = Math.min(minimumTemperature, earth.temperatures[i][j]);
			}
		}
		earth.averageTemperature = totalTemperature / earth.rows / earth.columns;

		// Note: perhaps we could refactor this block into a subroutine...
		// average temp should be reasonably close to 288. Allow some variance
		// due to rounding
		if (Math.abs(earth.averageTemperature - 288) > .01) {
			totalTemperature = 0;
			for (int i = 0; i < earth.rows; i++) {
				for (int j = 0; j < earth.columns; j++) {
					earth.temperatures[i][j] = earth.temperatures[i][j] * 288 / earth.averageTemperature;
					totalTemperature += earth.temperatures[i][j];
					maximumTemperature = Math.max(maximumTemperature, earth.temperatures[i][j]);
					minimumTemperature = Math.min(minimumTemperature, earth.temperatures[i][j]);
				}
			}
			// recalculated average after normalized to 288
			earth.averageTemperature = totalTemperature / earth.rows / earth.columns;
		}
		earth.maximumTemperature = maximumTemperature;
		earth.minimumTemperature = minimumTemperature;
	}

}
