package simulation;

public class SimKernel {

	public SimKernel() {

	}

	public float step(Grid source, Grid target, float rotationAngle_deg,
			int simulationRate, int gridSpacing) {
		// Compute lattice point temperature as average of neighbors
		float maxDiff = 0;

		float width = (2 * 180 / gridSpacing); // rows
		// float height = (180 / gridSpacing);

		int sunPosition = (int) (rotationAngle_deg == 0 ? rotationAngle_deg
				: 360 - (width * (rotationAngle_deg / 360) + (width / 2)
						% width));

		float tempSunEffect = 0;
		float tempCooling = 0;
		float tempDiffusion = 0;
		float newTemp; // newTemp = tempSunEffect+tempCooling+tempDiffusion

		float absDiffTemp;

		Cell sourceCell;
		Cell targetCell;
		do {
			sourceCell = source.getCurrent();
			targetCell = target.getCurrent();

			tempDiffusion = calcTempDiffusion(sourceCell);
			tempSunEffect = calTsun(sunPosition, sourceCell, gridSpacing);
			tempCooling = calTcool(sourceCell, gridSpacing);

			newTemp = tempCooling + tempSunEffect + tempDiffusion;

			// System.out.println("tempCooling"+ tempCooling);
			// System.out.println("tempSunEffect"+ tempSunEffect);
			// System.out.println("tempDiffusion"+ tempDiffusion);
			// System.out.println("newTemp" + newTemp);

			// float temp =

			targetCell.setTemperature(newTemp);

			// System.out.println("targetCell.getTemperature()" +
			// targetCell.getTemperature());

			absDiffTemp = Math.abs(sourceCell.getTemperature()
					- targetCell.getTemperature());
			if (absDiffTemp > maxDiff) {
				maxDiff = absDiffTemp;
			}

		} while (source.next() && target.next());

		return maxDiff;
	}

	private float calTcool(Cell sourceCel, int gridSpacing) {

		float width = (2 * 180 / gridSpacing); // rows
		float height = (180 / gridSpacing);

		float beta = (float) (sourceCel.getSurfaceArea() / (SimConstant.A / (width - height))); // actual
																								// grid
		// cell
		// area
		float tempfactor = sourceCel.getTemperature()
				/ SimConstant.AVERAGE_TEMP_K;

		return -1 * beta * tempfactor * sourceCel.getTemperature();
	}

	public static float calculateTemperatureDueToSun(float latitude,
			float longitude) {

		return (float) (288 * Math.cos(latitude) * Math.cos(longitude));

	}

	private float calTsun(int sunPosition, Cell cell, int gs) {
		int sunLongitude = getSunLocationOnEarth(sunPosition, gs);
		float attenuation_lat = (float) Math.cos(Math.toRadians(cell
				.getLatitude()));
		float attenuation_longi = (float) (((Math.abs(sunLongitude
				- cell.getLongitude()) % 360) < 90) ? Math.cos(Math
				.toRadians(sunLongitude - cell.getLongitude())) : 0);

		return 278 * attenuation_lat * attenuation_longi;
	}

	// A help function for get the Sun's corresponding location on longitude.
	private int getSunLocationOnEarth(int sunPosition, int gs) {
		// Grid column under the Sun at sunPosition
		int cols = 360 / gs;
		int j = sunPosition;
		return j < (cols / 2) ? -(j + 1) * gs : (360) - (j + 1) * gs;
	}

	private float calcTempDiffusion(Cell sourceCell) {
		// required for diffusion effect
		float cellPerimeter;
		float pE, pW, pN, pS; // Proportion of a cells border shared
								// with its neighbors to the East, West, north
								// and south

		float tempDiffusion = 0;
		cellPerimeter = sourceCell.getPerimeter();
		pE = sourceCell.getRightLength() / cellPerimeter;
		pW = sourceCell.getLeftLength() / cellPerimeter;
		pN = sourceCell.getTopLength() / cellPerimeter;
		pS = sourceCell.getBottomLength() / cellPerimeter;

		tempDiffusion = (pE * (sourceCell.getRight().getTemperature()) + pW
				* (sourceCell.getLeft().getTemperature()) + pN
				* (sourceCell.getTop().getTemperature()) + pS
				* (sourceCell.getBottom().getTemperature()));

		return tempDiffusion;

	}

}