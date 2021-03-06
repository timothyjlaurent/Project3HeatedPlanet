package controllers;

import static constants.SimulationConstants.HEIGHT_IN_DEGREES;
import static constants.SimulationConstants.MINUTES_IN_DAY;
import static constants.SimulationConstants.MINUTES_IN_YEAR;
import static constants.SimulationConstants.SEMIMAJOR_AXIS;
import static constants.SimulationConstants.SOLAR_CONSTANT;
import static constants.SimulationConstants.SURFACEAREA_OF_EARTH;
import static constants.SimulationConstants.WIDTH_IN_DEGREES;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static util.SimulationUtil.calculateTimeSinceLastPerihelion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.CommandLineParam;
import models.Experiment;
import models.GridPoint;
import models.PhysicalFactors;
import models.SimulationSettings;
import util.SimulationUtil;
import constants.SimulationConstants;

public class SimulationController {
	public static Integer TimeEquinox;
	private static Set<GridPoint> heatedGridPoint;
	private static double sunLat;
	private static double sunLong;
	private static double sunDist;
	private static double sunDistMin;
	private static double orbitalPos;
	private static double sunMaxDist;

	/**
	 * @return the sunLat
	 */
	public static double getSunLat() {
		return sunLat;
	}

	/**
	 * @return the sunLong
	 */
	public static double getSunLong() {
		return sunLong;
	}

	/**
	 * @return the sunDist
	 */
	public static double getSunDist() {
		return sunDist;
	}

	/**
	 * @return the sunDistMin
	 */
	public static double getSunDistMin() {
		return sunDistMin;
	}

	/**
	 * @return the orbitalPos
	 */
	public static double getOrbitalPos() {
		return orbitalPos;
	}

	/**
	 * @return the sunMaxDist
	 */
	public static double getSunMaxDist() {
		return sunMaxDist;
	}

	@SuppressWarnings("unchecked")
	public static Set<GridPoint> simulateIteration(final Experiment experiment, final int timePassed) {
		final Map<Date, Set<GridPoint>> map = SimulationUtil.convertSetToMap(experiment.getGridPoints());
		final HashSet<GridPoint> previousGridPointSet = (HashSet<GridPoint>) ((HashSet<GridPoint>) map.get(getMaxDate(map))).clone();
		double heatPerIteration = diffuseHeatFromNeighbors(experiment, previousGridPointSet);
		heatPerIteration += heatGridFromSun(experiment, timePassed, heatedGridPoint);
		coolEarth(experiment, heatPerIteration, heatedGridPoint);

		sunLat = computeSunNoonLatitude(experiment, timePassed);
		sunLong = computeSunNoonLongitude(timePassed);
		sunDist = getSolarDistance(experiment, timePassed);
		orbitalPos = Math.toDegrees(computeRotationalAngle(experiment, timePassed));

		return (Set<GridPoint>) ((HashSet<GridPoint>) heatedGridPoint).clone();
	}

	private static Date getMaxDate(final Map<Date, Set<GridPoint>> map) {
		final List<Date> dates = new ArrayList<Date>(map.keySet());
		Collections.sort(dates);
		return dates.get(dates.size() - 1);
	}

	/**
	 * Initializes the grid of points
	 * 
	 * @param commandLineParam
	 * @param simulationSettings
	 * @param physicalFactors
	 * @param startDate
	 * @return
	 */
	public static Experiment initializeGridPoints(final CommandLineParam commandLineParam, final SimulationSettings simulationSettings, final PhysicalFactors physicalFactors, final Calendar startDate) {
		final Experiment experiment = new Experiment();
		experiment.setCommandLineParam(commandLineParam);
		experiment.setSimulationSettings(simulationSettings);
		experiment.setPhysicalFactors(physicalFactors);

		final HashSet<GridPoint> gridPointSet = new HashSet<GridPoint>();
		for (int i = 0; i < WIDTH_IN_DEGREES; i += experiment.getSimulationSettings().getGridSpacing()) {
			for (int j = 0; j < HEIGHT_IN_DEGREES; j += experiment.getSimulationSettings().getGridSpacing()) {
				final GridPoint gridPoint = new GridPoint();
				gridPoint.setLeftLongitude(i - 180);
				gridPoint.setTopLatitude(90 - j);
				gridPoint.setTemperature(SimulationConstants.DEFAULT_CELL_TEMP);
				gridPoint.setDateTime(startDate.getTime());
				gridPointSet.add(gridPoint);
			}
		}
		experiment.setGridPoints(gridPointSet);
		sunLat = computeSunNoonLatitude(experiment, 0);
		sunLong = computeSunNoonLongitude(0);
		sunDist = getSolarDistance(experiment, 0);
		orbitalPos = Math.toDegrees(computeRotationalAngle(experiment, 0));

		sunDistMin = sunDist;
		sunMaxDist = getSolarDistance(experiment, MINUTES_IN_YEAR / 2);

		return experiment;
	}

	private static GridPoint getNeigbhorGridPoint(final Set<GridPoint> gridPoints, int latitude, int longitude) {
		longitude = longitude == 180 ? longitude : 0;
		latitude = latitude == -90 ? latitude : 0;
		for (final GridPoint gridPoint : gridPoints) {
			if (latitude == gridPoint.getTopLatitude() || longitude == gridPoint.getLeftLongitude()) {
				return gridPoint;
			}
		}
		return null;
	}

	/**
	 * Computes mean anomaly as a function of time
	 * 
	 * @param time
	 * @return
	 */
	private static double calculateMeanAnomaly(final int time) {
		return 2 * PI * calculateTimeSinceLastPerihelion(time) / MINUTES_IN_YEAR;
	}

	/**
	 * finds the eccentric_anomaly of the planet as radians
	 * 
	 * @return
	 */
	private static double computeEccentricAnomaly(final Experiment experiment, final int minutesFromStart) {
		final double meanAnomaly = calculateMeanAnomaly(minutesFromStart);
		/**
		 * finds the eccentric_anomaly of the planet as radians
		 * 
		 * @return
		 */
		final double epsilon = 10e-16;
		double root = 0;
		for (int i = 0; i < 10000; i += 1) {
			root = root - (root - experiment.getPhysicalFactors().getOrbitalEccentricity() * sin(root) - meanAnomaly) / (1 - experiment.getPhysicalFactors().getOrbitalEccentricity() * cos(root));
			if (abs(root - experiment.getPhysicalFactors().getOrbitalEccentricity() * sin(root) - meanAnomaly) < epsilon) {
				return root;
			}
		}
		return root;
	}

	/*
	 * computation of true anomalay
	 */
	private static double computeTrueAnomaly(final Experiment experiment, final int minutesFromStart) {
		return acos((cos(computeEccentricAnomaly(experiment, minutesFromStart)) - experiment.getPhysicalFactors().getOrbitalEccentricity()) / (1 - experiment.getPhysicalFactors().getOrbitalEccentricity() * cos(computeEccentricAnomaly(experiment, minutesFromStart))));
	}

	private static double getSolarDistance(final Experiment experiment, final int minutesFromStart) {
		return SEMIMAJOR_AXIS * (1 - pow(experiment.getPhysicalFactors().getOrbitalEccentricity(), 2)) / (1 + experiment.getPhysicalFactors().getOrbitalEccentricity() * cos(computeTrueAnomaly(experiment, minutesFromStart)));
	}

	private static double getDistanceRadiationAdjustment(final Experiment experiment, final int minutesFromStart) {
		return pow(getSolarDistance(experiment, 0), 2) / pow(getSolarDistance(experiment, minutesFromStart), 2);
	}

	private static double computeSunNoonLatitude(final Experiment experiment, final int minutesFromStart) {
		return toDegrees(toRadians(experiment.getPhysicalFactors().getAxialTilt()) * sin(computeRotationalAngle(experiment, minutesFromStart)));
	}

	private static double computeSunNoonLongitude(final int minutesFromStart) {
		return -(minutesFromStart % MINUTES_IN_DAY) * WIDTH_IN_DEGREES / MINUTES_IN_DAY;
	}

	private static double computeRotationalAngle(final Experiment experiment, final int minutesFromStart) {
		return ((minutesFromStart - computeTimeEquinox(experiment)) % MINUTES_IN_YEAR) * 2.0 * PI / MINUTES_IN_YEAR;
	}

	// private static int computeTimeEquinox(final Experiment experiment) {
	// final double precision = 0.0015; // degrees
	// int firstOfYearInMinutes = 0;
	// int middleOfYearInMinutes = MINUTES_IN_YEAR / 2;
	// int timeEquinox = (middleOfYearInMinutes - firstOfYearInMinutes) / 2 +
	// firstOfYearInMinutes;
	// double difference = toDegrees(computeTrueAnomaly(experiment,
	// timeEquinox)) - ARGUMENT_OF_PERIAPSIS;
	// while (abs(difference) > precision) {
	// if (difference < 0) {
	// firstOfYearInMinutes = timeEquinox + 1;
	// } else {
	// middleOfYearInMinutes = timeEquinox - 1;
	// }
	// timeEquinox = (middleOfYearInMinutes - firstOfYearInMinutes) / 2 +
	// firstOfYearInMinutes;
	// difference = toDegrees(computeTrueAnomaly(experiment, timeEquinox)) -
	// ARGUMENT_OF_PERIAPSIS;
	// }
	// return timeEquinox;
	// }

	public static int computeTimeEquinox(final Experiment experiment) {
		// solve for t

		if (TimeEquinox == null) {
			final double precision = 0.0015; // degrees
			int t1 = 0; // minutes
			int t2 = SimulationConstants.MINUTES_IN_YEAR / 2; // minutes

			int t = (t2 - t1) / 2 + t1; // starting time to test

			double difference = toDegrees(computeTrueAnomaly(experiment, t)) - SimulationConstants.ARGUMENT_OF_PERIAPSIS; // degree
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

				difference = toDegrees(computeTrueAnomaly(experiment, t)) - SimulationConstants.ARGUMENT_OF_PERIAPSIS;
			}
			TimeEquinox = t;
		}
		return TimeEquinox;
	}

	private static void coolEarth(final Experiment experiment, final double heatGain, final Set<GridPoint> previousGridPointSet) {
		final double totalPlanetHeat = SURFACEAREA_OF_EARTH * SimulationConstants.DEFAULT_CELL_TEMP + heatGain;

		for (final GridPoint gridPoint : previousGridPointSet) {
			final double heatLoss = SimulationUtil.calculateSurfaceArea(experiment, gridPoint) * gridPoint.getTemperature() / totalPlanetHeat * heatGain;
			gridPoint.setTemperature(gridPoint.getTemperature() - heatLoss / SimulationUtil.calculateSurfaceArea(experiment, gridPoint));
		}
	}

	private static double diffuseHeatFromNeighbors(final Experiment experiment, final Set<GridPoint> gridPoints) {

		double heatChange = 0;
		heatedGridPoint = new HashSet<GridPoint>();
		for (final GridPoint gridPoint : gridPoints) {
			final GridPoint newGridPoint = new GridPoint();
			final double verticalSideLength = SimulationUtil.calculateVerticalSideLength(experiment);
			final double northSideLength = SimulationUtil.calculateNorthSideLength(experiment, gridPoint, verticalSideLength);
			final double southSideLength = SimulationUtil.calculateSouthSideLength(gridPoint, verticalSideLength);
			final double gridPerimeter = northSideLength + southSideLength + 2 * verticalSideLength;

			final int gridSpacing = experiment.getSimulationSettings().getGridSpacing();
			final int latitude = gridPoint.getTopLatitude();
			final int longitude = gridPoint.getLeftLongitude();
			final double eastTemp = verticalSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude + gridSpacing, longitude).getTemperature();
			final double westTemp = verticalSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude - gridSpacing, longitude).getTemperature();
			final double northTemp = southSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude, longitude - gridSpacing).getTemperature();
			final double southTemp = northSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude, longitude + gridSpacing >= 180 ? -1 * (longitude + gridSpacing) : longitude + gridSpacing).getTemperature();
			final double neighborTemp = eastTemp + westTemp + northTemp + southTemp;

			// changed to give double weight to current temp
			final double newTemp = (neighborTemp + 2 * gridPoint.getTemperature()) / 3;
			heatChange += (newTemp * SimulationUtil.calculateSurfaceArea(experiment, gridPoint)) - gridPoint.getTemperature() * SimulationUtil.calculateSurfaceArea(experiment, gridPoint);

			newGridPoint.setTemperature(newTemp);
			newGridPoint.setLeftLongitude(gridPoint.getLeftLongitude());
			newGridPoint.setTopLatitude(gridPoint.getTopLatitude());

			final Calendar cal = Calendar.getInstance();
			cal.setTime(gridPoint.getDateTime());
			cal.add(Calendar.MINUTE, experiment.getSimulationSettings().getTimeStep());
			newGridPoint.setDateTime(cal.getTime());
			heatedGridPoint.add(newGridPoint);
		}
		return heatChange;
	}

	private static double heatGridFromSun(final Experiment experiment, final int minutesFromStart, final Set<GridPoint> gridPoints) {
		final double proportionRadiation = getDistanceRadiationAdjustment(experiment, minutesFromStart);
		final double longitudeOfSun = computeSunNoonLongitude(minutesFromStart);
		final double latitudeOfSun = computeSunNoonLatitude(experiment, minutesFromStart);

		double heatGain = 0;
		for (final GridPoint gridPoint : gridPoints) {
			final double zenithAngle = calculateSolarZenith(gridPoint, longitudeOfSun, latitudeOfSun, experiment);
			final double tempIncrease = cos(zenithAngle) > 0 ? SOLAR_CONSTANT * proportionRadiation * cos(zenithAngle) * experiment.getSimulationSettings().getTimeStep() : 0;
			heatGain += SimulationUtil.calculateSurfaceArea(experiment, gridPoint) * tempIncrease;
			gridPoint.setTemperature(gridPoint.getTemperature() + tempIncrease);
		}

		return heatGain;
	}

	private static double calculateLocalHourAngle(final double longitudeOfSun, final double longitudeOfPoint) {
		return longitudeOfPoint - longitudeOfSun;
	}

	private static double calculateSolarZenith(final GridPoint gridPoint, final double longitudeOfSun, final double latitudeOfSun, final Experiment experiment) {
		final double gridSpacing = experiment.getSimulationSettings().getGridSpacing();
		final double hourAngle = toRadians(calculateLocalHourAngle(longitudeOfSun, gridPoint.getLeftLongitude() - gridSpacing / 2));
		final double declinationOfSun = toRadians(latitudeOfSun);
		final double pointLatitude = toRadians(gridPoint.getTopLatitude() + gridSpacing / 2);
		final double solarElevationAngle = asin(cos(hourAngle) * cos(declinationOfSun) * cos(pointLatitude) + sin(declinationOfSun) * sin(pointLatitude));
		return PI / 2 - solarElevationAngle;
	}

}
