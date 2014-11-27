package models;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import constants.SimulationConstants;

public class SimulatedEarth {

	private static int widthInDegrees = 360;
	private static int heightInDegrees = 180;
	private static int circumferenceOfEarth = 40030140;
	private static double surfaceAreaOfEarth = 510072000000000.0;
	private static int initialCellTemperature = 288;
	private static double degreesRotatedPerMinute = 360.0 / 1440.0;
	private static double heatPerIteration;
	private static final Calendar START_DATE = new GregorianCalendar(2000, 0, 4, 0, 0);
	private static int time = 0; // time in minutes
	private static double eccentricity;
	public static double obliquity;
	private static Integer timeEquinox;

	private static double solarConstant = 1; // this is the solar constant of
												// energy per unit
												// time and unit area to hit the
												// earth that is perpendicular
												// the sun.

	private int gridSpacing;
	protected int gridRows;
	protected int gridColumns;
	protected double proportionOfEquator;

	protected EarthCell[][] earth;

	/**
	 *
	 */
	public double rotation;

	/**
	 *
	 */
	public double maxDelta;

	public double max;
	public double min;
	public double stddev;
	public double mean;

	/**
	 * 
	 * @param gridSpacing
	 *            number of degrees for the grid; must divide evenly into 180
	 */
	public SimulatedEarth(final int gridSpacing, final double orbitalEccentricity, final double obliquity) {
		this.gridSpacing = gridSpacing;
		gridColumns = SimulatedEarth.widthInDegrees / gridSpacing;
		gridRows = SimulatedEarth.heightInDegrees / gridSpacing;
		proportionOfEquator = ((double) gridSpacing) / SimulatedEarth.widthInDegrees;
		earth = new EarthCell[gridRows][gridColumns];

		for (int i = 0; i < gridRows; ++i) {
			for (int j = 0; j < gridColumns; ++j) {
				earth[i][j] = CreateEarthCell(i, j);
			}
		}

		time = 0;
		rotation = 0;
		maxDelta = 0;
		eccentricity = orbitalEccentricity;
		SimulatedEarth.obliquity = obliquity;

		return;
	}

	private static int timeSinceLastPerihelion(final int time) {
		return time % SimulationConstants.MINUTES_IN_YEAR;
	}

	/**
	 * Computes mean anomaly of planet as radians
	 * 
	 * @return
	 */
	private static double compute_mean_anomaly(final int time) {
		return 2 * PI * timeSinceLastPerihelion(time) / SimulationConstants.MINUTES_IN_YEAR;
	}

	/**
	 * finds the eccentric_anomaly of the planet as radians
	 * 
	 * @return
	 */
	private static double computeEccentricAnomaly(final int time, final double eccentricity) {
		final double meanAnomaly = compute_mean_anomaly(time);
		//
		// equation to solve
		// meanAnomaly = x - MATH.E * sin( x );

		final double epsilon = 10e-16;
		double root = 0;

		// Newton-Raphson algorithm
		for (int i = 0; i < 10000; i += 1) {
			root = root - (root - eccentricity * sin(root) - meanAnomaly) / (1 - eccentricity * cos(root));
			// check if the desired precision has been reached
			if (abs(root - eccentricity * sin(root) - meanAnomaly) < epsilon) {
				return root;
			}
		}
		return root;
	}

	private static double computeTrueAnomaly(final int time, final double eccentricity) {
		final double eccentricAnomaly = computeEccentricAnomaly(time, eccentricity);

		return acos((cos(eccentricAnomaly) - eccentricity) / (1 - eccentricity * cos(eccentricAnomaly)));

	}

	private double computeTrueAnomaly(final int time) {
		return SimulatedEarth.computeTrueAnomaly(time, eccentricity);
	}

	/**
	 * returns the distance from the sun given a time
	 * 
	 * @param time
	 * @param eccentricity
	 * @return
	 */
	private static double getSolarDistance(final int time, final double eccentricity) {
		return SimulationConstants.SEMIMAJOR_AXIS * (1 - pow(eccentricity, 2)) / (1 + eccentricity * cos(computeTrueAnomaly(time, eccentricity)));
	}

	/**
	 * calculates the proportion of radiation the planet will receive at time
	 * compared to the amount at the parahelion parahelion
	 * 
	 * @param time
	 * @return the proportion
	 */
	public static double getDistanceRadiationAdjustment(final int time) {
		// distance at perihelion
		final double dist1 = getSolarDistance(0, eccentricity);
		final double dist2 = getSolarDistance(time, eccentricity);
		return pow(dist1, 2) / pow(dist2, 2);
	}

	public double computeSunNoonLatitude(final int time) {
		return toDegrees(toRadians(obliquity) * sin(computeRotationalAngle(time)));
	}

	/**
	 * Computes the sun longitude as a function of time .
	 * 
	 * @param time
	 * @return
	 */
	public static double computeSunNoonLongitude(final double time) {
		return time % 1440 * 360.0 / 1440;
	}

	/**
	 * 
	 * @param time
	 * @return angle of rotation in radians
	 */
	private double computeRotationalAngle(final int time) {
		return ((time - computeTimeEquinox()) % SimulationConstants.MINUTES_IN_YEAR) * 2.0 * PI / SimulationConstants.MINUTES_IN_YEAR;
	}

	public int computeTimeEquinox() {
		// solve for t

		if (timeEquinox == null) {
			final double precision = 0.0015; // degrees
			int t1 = 0; // minutes
			int t2 = SimulationConstants.MINUTES_IN_YEAR / 2; // minutes

			int t = (t2 - t1) / 2 + t1; // starting time to test

			double difference = toDegrees(this.computeTrueAnomaly(t)) - SimulationConstants.ARGUMENT_OF_PERIAPSIS; // degree
																													// difference

			while (abs(difference) > precision) {

				if (difference < 0) {
					// guessed under
					t1 = t + 1;
				} else {
					// guessed over
					t2 = t - 1;
				}

				t = (t2 - t1) / 2 + t1;

				difference = toDegrees(this.computeTrueAnomaly(t)) - SimulationConstants.ARGUMENT_OF_PERIAPSIS;
			}
			timeEquinox = t;
		}
		return timeEquinox;
	}

	/**
	 * Simulates an iteration of earth based on the time passed
	 * 
	 * @param timePassed
	 */
	public void simulateIteration(final int timePassed) {
		rotateGrid(degreesToRotate(timePassed));
		heatPerIteration = diffuseHeatFromNeighbors();
		heatPerIteration += heatGridFromSun(timePassed);
		CoolEarth(SimulatedEarth.heatPerIteration);

	}

	protected void CoolEarth(final double heatGain) {

		// this is used to get the heat
		final double totalPlanetHeat = SimulatedEarth.surfaceAreaOfEarth * 288.0 + heatGain;

		for (int i = 0; i < gridRows; ++i) {
			for (int j = 0; j < gridColumns; ++j) {

				// The heat lost by this trapezoid according to its area and
				// temp
				final double heatLoss = earth[i][j].SurfaceArea() * earth[i][j].Temperature() // heat
																								// in
																								// this
																								// grid
																								// point
						/ totalPlanetHeat // total heat in the world
						* heatGain;

				earth[i][j].Temperature(earth[i][j].Temperature() - heatLoss / earth[i][j].SurfaceArea()); // divide
																											// heat
																											// by
																											// area
																											// to
																											// get
																											// temp
			}
		}
	}

	protected void rotateGrid(final double degrees) {
		rotation = (rotation + degrees) % SimulatedEarth.widthInDegrees;
		// double totalAngles = 0.0;

		// for (int i = 0; i < this.gridRows; ++i)
		// {
		// for (int j = 0; j < this.gridColumns; ++j)
		// {
		// totalAngles = this.CalculateAngleForRadiation(i, j, this.rotation) +
		// totalAngles;
		// }
		// }
		//
		// for (int i = 0; i < this.gridRows; ++i)
		// {
		// for (int j = 0; j < this.gridColumns; ++j)
		// {
		// this.earth[i][j].PercentOfRadiation(
		// (this.CalculateAngleForRadiation(i, j, this.rotation) *
		// this.earth[i][j].SurfaceArea())
		// /
		// (totalAngles * SimulatedEarth.surfaceAreaOfEarth));
		// }
		// }
	}

	protected double diffuseHeatFromNeighbors() {
		final double[][] newTemperatures = new double[gridRows][gridColumns];

		double heatChange = 0;

		for (int i = 0; i < gridRows; ++i) {
			for (int j = 0; j < gridColumns; ++j) {
				final EarthCell current = earth[i][j];
				final double cellPerimeter = current.NorthSideLength() + current.SouthSideLength() + 2 * current.VerticalSideLength();

				double neighborTemp = (current.VerticalSideLength() / cellPerimeter) * GetEastNeighbor(i, j).Temperature();
				neighborTemp += (current.VerticalSideLength() / cellPerimeter) * GetWestNeighbor(i, j).Temperature();
				neighborTemp += (current.SouthSideLength() / cellPerimeter) * GetSouthNeighbor(i, j).Temperature();
				neighborTemp += (current.NorthSideLength() / cellPerimeter) * GetNorthNeighbor(i, j).Temperature();

				newTemperatures[i][j] = (neighborTemp + current.Temperature()) / 2;

				heatChange += (newTemperatures[i][j] * current.SurfaceArea()) - current.Temperature() * current.SurfaceArea();

			}
		}

		for (int i = 0; i < gridRows; ++i) {
			for (int j = 0; j < gridColumns; ++j) {
				// temperatureChange = newTemperatures[i][j] -
				// this.earth[i][j].Temperature();
				earth[i][j].Temperature(newTemperatures[i][j]);
			}
		}

		return heatChange;
	}

	protected double heatGridFromSun(final int minutes) {
		final double proportionRadiation = getDistanceRadiationAdjustment(time);
		final double longitudeOfSun = computeSunNoonLongitude(time);
		final double latitudeOfSun = computeSunNoonLatitude(time);

		double heatgain = 0;
		for (int i = 0; i < gridRows; ++i) {
			for (int j = 0; j < gridColumns; ++j) {
				final double zenithAngle = calculateSolarZenith(i, j, longitudeOfSun, latitudeOfSun);
				final double tempIncrease = cos(zenithAngle) > 0 ? solarConstant * proportionRadiation * cos(zenithAngle) * minutes : 0;
				heatgain += earth[i][j].SurfaceArea() * tempIncrease;
				earth[i][j].Temperature(earth[i][j].Temperature() + tempIncrease);
			}
		}

		return heatgain;
	}

	public double degreesToRotate(final int timePassed) {
		return SimulatedEarth.degreesRotatedPerMinute * timePassed;
	}

	/**
	 * Creates a new EarthCell based on it's coordinates.
	 * 
	 * @param i
	 * @param j
	 * @return An EarthCell populated with it's side lengths, starting
	 *         temperature, area and angle for radiation
	 */
	protected EarthCell CreateEarthCell(final int i, final int j) {
		final double latitude = CalculateLatitude(i);

		final double verticalSideLength = SimulatedEarth.circumferenceOfEarth * proportionOfEquator;
		final double northSideLength = cos(toRadians(latitude + gridSpacing)) * verticalSideLength;
		final double southSideLength = cos(toRadians(latitude)) * verticalSideLength;

		final double height = sqrt(verticalSideLength * verticalSideLength - .25 * (northSideLength - southSideLength) * (northSideLength - southSideLength));
		final double surfaceArea = .5 * (northSideLength + southSideLength) * height;

		final double temperature = SimulatedEarth.initialCellTemperature;
		return new EarthCell(northSideLength, southSideLength, verticalSideLength, temperature, CalculateAngleForRadiation(i, j, 0), surfaceArea);
	}

	private double CalculateLongitude(final int j) {
		return j < gridColumns / 2 ? -((j + 1) * gridSpacing) : SimulatedEarth.widthInDegrees - ((j + 1) * gridSpacing);
	}

	private double CalculateLatitude(final int i) {
		return (i - ((double) gridRows / 2)) * gridSpacing;
	}

	protected double CalculateAngleForRadiation(final int i, final int j, final double longitudeOfSun) {
		final double longitudeFromSun = (CalculateLongitude(j) - longitudeOfSun) % SimulatedEarth.widthInDegrees;
		return abs(longitudeFromSun) < 90 ? cos(toRadians(longitudeFromSun)) * cos(toRadians(CalculateLatitude(i))) : 0;
	}

	private double calculateSolarZenith(final int i, final int j, final double longitudeOfSun, final double latitudeOfSun) {
		// double longitudeFromSun = (this.CalculateLongitude(j) -
		// longitudeOfSun ) % SimulatedEarth.widthInDegrees ;
		// double latitudeFromSun = (this.CalculateLatitude(i) );
		final double hourAngle = toRadians(calculateLocalHourAngle(longitudeOfSun, CalculateLongitude(j)));

		final double declinationOfSun = toRadians(latitudeOfSun);

		final double pointLatitude = toRadians(CalculateLatitude(i));

		// Solar_zenith_angle http://en.wikipedia.org/wiki/Solar_zenith_angle
		final double solarElevationAngle = asin(cos(hourAngle) * cos(declinationOfSun) * cos(pointLatitude) + sin(declinationOfSun) * sin(pointLatitude));

		final double solarZenithAngle = PI / 2 - solarElevationAngle;

		return solarZenithAngle;
	}

	/**
	 * gets statistics for the grid points.
	 * 
	 * @return min, max, mean, stdDev
	 */
	public double[] getSimStats() {
		final ArrayList<Double> temps = new ArrayList<Double>();
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		double sum = 0;
		double mean;
		double stdev;

		for (final EarthCell[] element : earth) {
			for (int j = 0; j < earth[0].length; j += 1) {
				final double cur = element[j].Temperature();

				if (cur < min) {
					min = cur;
				}
				if (cur > max) {
					max = cur;
				}

				sum += cur;

				temps.add(element[j].Temperature());
			}
		}

		mean = sum / temps.size();

		stdev = stdDev(temps, mean);

		final double ret[] = { min, max, mean, stdev };
		return ret;

	}

	public double stdDev(final ArrayList<Double> list, final double mean) {
		double temp = 0;
		for (final double d : list) {
			temp += (mean - d) * (mean - d);
		}
		return sqrt(temp / list.size());
	}

	/**
	 * 
	 * @param longitudeOfSun
	 * @param longitudeOfPoint
	 * @return Thee
	 */
	private double calculateLocalHourAngle(final double longitudeOfSun, final double longitudeOfPoint) {

		return (longitudeOfPoint - longitudeOfSun);

	}

	protected EarthCell GetWestNeighbor(final int i, final int j) {
		return earth[i][(j + 1) % gridColumns];
	}

	protected EarthCell GetEastNeighbor(final int i, final int j) {
		return earth[i][(j - 1 + gridColumns) % gridColumns];
	}

	protected EarthCell GetNorthNeighbor(final int i, final int j) {
		return i + 1 < gridRows ? earth[i + 1][j] : earth[i][(j + (gridColumns / 2)) % gridColumns];
	}

	protected EarthCell GetSouthNeighbor(final int i, final int j) {
		return i - 1 >= 0 ? earth[i - 1][j] : earth[i][(j + (gridColumns / 2)) % gridColumns];
	}
}
