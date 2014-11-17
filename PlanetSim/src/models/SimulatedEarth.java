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

	private final static int widthInDegrees = 360;
	private final static int heightInDegrees = 180;
	private final static int circumferenceOfEarth = 40030140;
	private final static double surfaceAreaOfEarth = 510072000000000.0;
	private final static int initialCellTemperature = 288;
	private final static double degreesRotatedPerMinute = 360.0 / 1440.0;
	private final static double heatPerMinute = 5000;
	private static final Calendar START_DATE = new GregorianCalendar(2000, 0,
			4, 0, 0);
	private static int time = 0; // time in minutes
	private static double solarConstant = 1; // this is the solar constant of
	// energy per unit
	// time and unit area to hit the
	// earth that is perpendicular
	// the sun.

	private static double heatPerIteration;
	private static double eccentricity;
	public static double obliquity;
	private static Integer timeEquinox;

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
	public SimulatedEarth(int gridSpacing, double orbitalEccentricity,
			double obliquity) {
		this.gridSpacing = gridSpacing;
		this.gridColumns = SimulatedEarth.widthInDegrees / gridSpacing;
		this.gridRows = SimulatedEarth.heightInDegrees / gridSpacing;
		this.proportionOfEquator = ((double) gridSpacing)
				/ SimulatedEarth.widthInDegrees;
		this.earth = new EarthCell[this.gridRows][this.gridColumns];

		for (int i = 0; i < this.gridRows; ++i) {
			for (int j = 0; j < this.gridColumns; ++j) {
				this.earth[i][j] = this.CreateEarthCell(i, j);
			}
		}

		this.time = 0;
		this.rotation = 0;
		this.maxDelta = 0;
		this.eccentricity = orbitalEccentricity;
		this.obliquity = obliquity;

		return;
	}

	private static int timeSinceLastPerihelion(int time) {
		return time % SimulationConstants.MINUTES_IN_YEAR;
	}

	/**
	 * Computes mean anomaly of planet as radians
	 * 
	 * @return
	 */
	private static double compute_mean_anomaly(int time) {
		return 2 * PI * timeSinceLastPerihelion(time)
				/ SimulationConstants.MINUTES_IN_YEAR;
	}

	/**
	 * finds the eccentric_anomaly of the planet as radians
	 * 
	 * @return
	 */
	private static double computeEccentricAnomaly(int time, double eccentricity) {
		double meanAnomaly = compute_mean_anomaly(time);
		//
		// expression to solve
		// meanAnomaly = x - MATH.E * sin( x );

		double epsilon = 10e-16;
		double root = 0;

		// Newton-Raphson algorithm
		for (int i = 0; i < 10000; i += 1) {
			root = root - (root - eccentricity * sin(root) - meanAnomaly)
					/ (1 - eccentricity * cos(root));
			// check if the desired precision has been reached
			if (abs(root - eccentricity * sin(root) - meanAnomaly) < epsilon) {
				return root;
			}
		}
		return root;
	}

	private static double computeTrueAnomaly(int time, double eccentricity) {
		double eccentricAnomaly = computeEccentricAnomaly(time, eccentricity);

		return acos((cos(eccentricAnomaly) - eccentricity)
				/ (1 - eccentricity * cos(eccentricAnomaly)));

	}

	private double computeTrueAnomaly(int time) {
		return this.computeTrueAnomaly(time, this.eccentricity);
	}

	/**
	 * returns the distance from the sun given a time
	 * 
	 * @param time
	 * @param eccentricity
	 * @return
	 */
	private static double getSolarDistance(int time, double eccentricity) {
		return SimulationConstants.SEMIMAJOR_AXIS
				* (1 - pow(eccentricity, 2))
				/ (1 + eccentricity
						* cos(computeTrueAnomaly(time, eccentricity)));
	}

	/**
	 * calculates the proportion of radiation the planet will receive at time
	 * compared to the amount at the parahelion parahelion
	 * 
	 * @param time
	 * @return the proportion
	 */
	public static double getDistanceRadiationAdjustment(int time) {
		// distance at perihelion
		double dist1 = getSolarDistance(0, eccentricity);
		double dist2 = getSolarDistance(time, eccentricity);
		return pow(dist1, 2) / pow(dist2, 2);
	}

	/**
	 * Computes sun latitude as a function of time
	 * 
	 * @param time
	 * @return
	 */
	public double computeSunNoonLatitude(int time) {
		return toDegrees(toRadians(obliquity)
				* sin(computeRotationalAngle(time)));
	}

	/**
	 * Computes the sun longitude as a function of time .
	 * 
	 * @param time
	 * @return
	 */
	public static double computeSunNoonLongitude(double time) {
		return time % 1440 * 360.0 / 1440;
	}

	/**
	 * 
	 * @param time
	 * @return angle of rotation in radians
	 */
	private double computeRotationalAngle(int time) {
		return ((time - computeTimeEquinox()) % SimulationConstants.MINUTES_IN_YEAR)
				* 2.0 * PI / SimulationConstants.MINUTES_IN_YEAR;
	}

	public int computeTimeEquinox() {
		// solve for t

		if (timeEquinox == null) {
			double precision = 0.0015; // degrees
			int t1 = 0; // minutes
			int t2 = SimulationConstants.MINUTES_IN_YEAR / 2; // minutes

			int t = (t2 - t1) / 2 + t1; // starting time to test

			double difference = toDegrees(this.computeTrueAnomaly(t))
					- SimulationConstants.ARGUMENT_OF_PERIAPSIS; // degree
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

				difference = toDegrees(this.computeTrueAnomaly(t))
						- SimulationConstants.ARGUMENT_OF_PERIAPSIS;
			}
			this.timeEquinox = t;
		}
		return this.timeEquinox;
	}

	/**
	 * Simulates an iteration of earth based on the time passed
	 * 
	 * @param timePassed
	 */
	public void simulateIteration(int timePassed) {
		this.rotateGrid(this.degreesToRotate(timePassed));
		this.heatPerIteration = this.diffuseHeatFromNeighbors();
		this.heatPerIteration += this.heatGridFromSun(timePassed);
		this.CoolEarth(SimulatedEarth.heatPerIteration);

	}

	protected void CoolEarth(double heatGain) {

		// this is used to get the heat
		double totalPlanetHeat = SimulatedEarth.surfaceAreaOfEarth * 288.0
				+ heatGain;

		for (int i = 0; i < this.gridRows; ++i) {
			for (int j = 0; j < this.gridColumns; ++j) {

				// The heat lost by this trapezoid according to its area and
				// temp
				double heatLoss = this.earth[i][j].SurfaceArea()
						* this.earth[i][j].Temperature() // heat in this grid
															// point
						/ totalPlanetHeat // total heat in the world
						* heatGain;

				this.earth[i][j].Temperature(this.earth[i][j].Temperature()
						- heatLoss / this.earth[i][j].SurfaceArea()); // divide
																		// heat
																		// by
																		// area
																		// to
																		// get
																		// temp
			}
		}
	}

	protected void rotateGrid(double degrees) {
		this.rotation = (this.rotation + degrees)
				% SimulatedEarth.widthInDegrees;
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
		double[][] newTemperatures = new double[this.gridRows][this.gridColumns];

		double heatChange = 0;

		for (int i = 0; i < this.gridRows; ++i) {
			for (int j = 0; j < this.gridColumns; ++j) {
				EarthCell current = this.earth[i][j];
				double cellPerimeter = current.NorthSideLength()
						+ current.SouthSideLength() + 2
						* current.VerticalSideLength();

				double neighborTemp = (current.VerticalSideLength() / cellPerimeter)
						* this.GetEastNeighbor(i, j).Temperature();
				neighborTemp += (current.VerticalSideLength() / cellPerimeter)
						* this.GetWestNeighbor(i, j).Temperature();
				neighborTemp += (current.SouthSideLength() / cellPerimeter)
						* this.GetSouthNeighbor(i, j).Temperature();
				neighborTemp += (current.NorthSideLength() / cellPerimeter)
						* this.GetNorthNeighbor(i, j).Temperature();

				newTemperatures[i][j] = (neighborTemp + current.Temperature()) / 2;

				heatChange += (newTemperatures[i][j] * current.SurfaceArea())
						- current.Temperature() * current.SurfaceArea();

			}
		}

		for (int i = 0; i < this.gridRows; ++i) {
			for (int j = 0; j < this.gridColumns; ++j) {
				// temperatureChange = newTemperatures[i][j] -
				// this.earth[i][j].Temperature();
				this.earth[i][j].Temperature(newTemperatures[i][j]);
			}
		}

		return heatChange;
	}

	protected double heatGridFromSun(int minutes) {
		double proportionRadiation = getDistanceRadiationAdjustment(this.time);
		double longitudeOfSun = computeSunNoonLongitude(this.time);
		double latitudeOfSun = computeSunNoonLatitude(this.time);

		double heatgain = 0;
		for (int i = 0; i < this.gridRows; ++i) {
			for (int j = 0; j < this.gridColumns; ++j) {
				double zenithAngle = calculateSolarZenith(i, j, longitudeOfSun,
						latitudeOfSun);
				double tempIncrease = cos(zenithAngle) > 0 ? this.solarConstant
						* proportionRadiation * cos(zenithAngle) * minutes : 0;
				heatgain += this.earth[i][j].SurfaceArea() * tempIncrease;
				this.earth[i][j].Temperature(this.earth[i][j].Temperature()
						+ tempIncrease);
			}
		}

		return heatgain;
	}

	public double degreesToRotate(int timePassed) {
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
	protected EarthCell CreateEarthCell(int i, int j) {
		double latitude = this.CalculateLatitude(i);

		double verticalSideLength = SimulatedEarth.circumferenceOfEarth
				* this.proportionOfEquator;
		double northSideLength = cos(toRadians(latitude + this.gridSpacing))
				* verticalSideLength;
		double southSideLength = cos(toRadians(latitude)) * verticalSideLength;

		double height = sqrt(verticalSideLength * verticalSideLength - .25
				* (northSideLength - southSideLength)
				* (northSideLength - southSideLength));
		double surfaceArea = .5 * (northSideLength + southSideLength) * height;

		double temperature = SimulatedEarth.initialCellTemperature;
		return new EarthCell(northSideLength, southSideLength,
				verticalSideLength, temperature,
				this.CalculateAngleForRadiation(i, j, 0), surfaceArea);
	}

	private double CalculateLongitude(int j) {
		return j < this.gridColumns / 2 ? -((j + 1) * this.gridSpacing)
				: SimulatedEarth.widthInDegrees - ((j + 1) * this.gridSpacing);
	}

	private double CalculateLatitude(int i) {
		return (i - ((double) this.gridRows / 2)) * this.gridSpacing;
	}

	protected double CalculateAngleForRadiation(int i, int j,
			double longitudeOfSun) {
		double longitudeFromSun = (this.CalculateLongitude(j) - longitudeOfSun)
				% SimulatedEarth.widthInDegrees;
		return abs(longitudeFromSun) < 90 ? cos(toRadians(longitudeFromSun))
				* cos(toRadians(this.CalculateLatitude(i))) : 0;
	}

	private double calculateSolarZenith(int i, int j, double longitudeOfSun,
			double latitudeOfSun) {
		// double longitudeFromSun = (this.CalculateLongitude(j) -
		// longitudeOfSun ) % SimulatedEarth.widthInDegrees ;
		// double latitudeFromSun = (this.CalculateLatitude(i) );
		double hourAngle = toRadians(calculateLocalHourAngle(longitudeOfSun,
				this.CalculateLongitude(j)));

		double declinationOfSun = toRadians(latitudeOfSun);

		double pointLatitude = toRadians(CalculateLatitude(i));

		// Solar_zenith_angle http://en.wikipedia.org/wiki/Solar_zenith_angle
		double solarElevationAngle = asin(cos(hourAngle)
				* cos(declinationOfSun) * cos(pointLatitude)
				+ sin(declinationOfSun) * sin(pointLatitude));

		double solarZenithAngle = PI / 2 - solarElevationAngle;

		return solarZenithAngle;
	}

	public double[] getSimStats() {
		ArrayList<Double> temps = new ArrayList<Double>();
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		double sum = 0;
		double mean;
		double stdev;

		for (int i = 0; i < this.earth.length; i += 1) {
			for (int j = 0; j < this.earth[0].length; j += 1) {
				double cur = this.earth[i][j].Temperature();

				if (cur < min) {
					min = cur;
				}
				if (cur > max) {
					max = cur;
				}

				sum += cur;

				temps.add(this.earth[i][j].Temperature());
			}
		}

		mean = sum / temps.size();

		stdev = stdDev(temps, mean);

		double ret[] = { min, max, mean, stdev };
		return ret;

	}

	public double stdDev(ArrayList<Double> list, double mean) {
		double temp = 0;
		for (double d : list) {
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
	private double calculateLocalHourAngle(double longitudeOfSun,
			double longitudeOfPoint) {

		return (longitudeOfPoint - longitudeOfSun);

	}

	protected EarthCell GetWestNeighbor(int i, int j) {
		return this.earth[i][(j + 1) % this.gridColumns];
	}

	protected EarthCell GetEastNeighbor(int i, int j) {
		return this.earth[i][(j - 1 + this.gridColumns) % this.gridColumns];
	}

	protected EarthCell GetNorthNeighbor(int i, int j) {
		return i + 1 < this.gridRows ? this.earth[i + 1][j]
				: this.earth[i][(j + (this.gridColumns / 2)) % this.gridColumns];
	}

	protected EarthCell GetSouthNeighbor(int i, int j) {
		return i - 1 >= 0 ? this.earth[i - 1][j]
				: this.earth[i][(j + (this.gridColumns / 2)) % this.gridColumns];
	}
}
