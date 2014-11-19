package controllers;

import static constants.SimulationConstants.ARGUMENT_OF_PERIAPSIS;
import static constants.SimulationConstants.CIRCUMFERENCE_OF_EARTH;
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
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static util.SimulationUtil.calculateTimeSinceLastPerihelion;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.CommandLineParam;
import models.Experiment;
import models.GridPoint;
import models.PhysicalFactors;
import models.SimulationSettings;
import constants.SimulationConstants;

public class SimulationController {

	public static void main(final String[] args) {
		final SimulationSettings settings = new SimulationSettings("Name");
		settings.setGridSpacing(30);
		SimulationController.simulate(null, settings, null, SimulationConstants.DEFAULT_START_DATE);
	}

	private static Experiment experiment;
	private static Date dateTimeOfExperiment;

	public static Experiment simulate(final CommandLineParam commandLineParam, final SimulationSettings simulationSettings, final PhysicalFactors physicalFactors, final Calendar startDate) {
		experiment = new Experiment();
		experiment.setCommandLineParam(commandLineParam);
		experiment.setSimulationSettings(simulationSettings);
		experiment.setPhysicalFactors(physicalFactors);

		initializeGridPoints(experiment, startDate);

		int minutesFromStart = 0;
		while (minutesFromStart < simulationSettings.getSimulationLength()) {
			minutesFromStart += simulationSettings.getTimeStep();
			dateTimeOfExperiment = minutesToDate(minutesFromStart, startDate);
			experiment.getGridPoints().put(dateTimeOfExperiment, simulateIteration(minutesFromStart, new HashSet<GridPoint>(experiment.getGridPoints().get(minutesToDate(-minutesFromStart, startDate)))));
		}
		return experiment;
	}

	private static Date minutesToDate(final int currentDateTime, final Calendar startDate) {
		startDate.add(Calendar.MINUTE, currentDateTime);
		return startDate.getTime();
	}

	public static Set<GridPoint> simulateIteration(final int timePassed, final Set<GridPoint> previousGridPointSet) {

		double heatPerIteration = diffuseHeatFromNeighbors(previousGridPointSet);
		heatPerIteration += heatGridFromSun(timePassed, previousGridPointSet);
		coolEarth(heatPerIteration, previousGridPointSet);
		return previousGridPointSet;
	}

	private static void initializeGridPoints(final Experiment experiment, final Calendar startDate) {
		final Map<Date, Set<GridPoint>> gridPoints = new HashMap<Date, Set<GridPoint>>();
		final Set<GridPoint> gridPointSet = new HashSet<GridPoint>();
		for (int i = 0; i < WIDTH_IN_DEGREES; i += experiment.getSimulationSettings().getGridSpacing()) {
			for (int j = 0; j < HEIGHT_IN_DEGREES; j += experiment.getSimulationSettings().getGridSpacing()) {
				final GridPoint gridPoint = new GridPoint();
				gridPoint.setLeftLongitude(i - 180);
				gridPoint.setTopLatitude(90 - j);
				gridPoint.setTemperature(SimulationConstants.DEFAULT_CELL_TEMP);
				gridPointSet.add(gridPoint);
			}
		}
		gridPoints.put(startDate.getTime(), gridPointSet);
		experiment.setGridPoints(gridPoints);
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

	private static double calculateMeanAnomaly(final int time) {
		return 2 * PI * calculateTimeSinceLastPerihelion(time) / MINUTES_IN_YEAR;
	}

	private static double computeEccentricAnomaly(final int minutesFromStart) {
		final double meanAnomaly = calculateMeanAnomaly(minutesFromStart);
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

	private static double computeTrueAnomaly(final int minutesFromStart) {
		return acos((cos(computeEccentricAnomaly(minutesFromStart)) - experiment.getPhysicalFactors().getOrbitalEccentricity()) / (1 - experiment.getPhysicalFactors().getOrbitalEccentricity() * cos(computeEccentricAnomaly(minutesFromStart))));
	}

	private static double getSolarDistance(final int minutesFromStart) {
		return SEMIMAJOR_AXIS * (1 - pow(experiment.getPhysicalFactors().getOrbitalEccentricity(), 2)) / (1 + experiment.getPhysicalFactors().getOrbitalEccentricity() * cos(computeTrueAnomaly(minutesFromStart)));
	}

	private static double getDistanceRadiationAdjustment(final int minutesFromStart) {
		return pow(getSolarDistance(0), 2) / pow(getSolarDistance(minutesFromStart), 2);
	}

	private static double computeSunNoonLatitude(final int minutesFromStart) {
		return toDegrees(toRadians(experiment.getPhysicalFactors().getAxialTilt()) * sin(computeRotationalAngle(minutesFromStart)));
	}

	private static double computeSunNoonLongitude(final int minutesFromStart) {
		return (minutesFromStart % MINUTES_IN_DAY) * WIDTH_IN_DEGREES / MINUTES_IN_DAY;
	}

	private static double computeRotationalAngle(final int minutesFromStart) {
		return ((minutesFromStart - computeTimeEquinox()) % MINUTES_IN_YEAR) * 2.0 * PI / MINUTES_IN_YEAR;
	}

	private static int computeTimeEquinox() {
		final double precision = 0.0015; // degrees
		int firstOfYearInMinutes = 0;
		int middleOfYearInMinutes = MINUTES_IN_YEAR / 2;
		int timeEquinox = (middleOfYearInMinutes - firstOfYearInMinutes) / 2 + firstOfYearInMinutes;
		double difference = toDegrees(computeTrueAnomaly(timeEquinox)) - ARGUMENT_OF_PERIAPSIS;
		while (abs(difference) > precision) {
			if (difference < 0) {
				firstOfYearInMinutes = timeEquinox + 1;
			} else {
				middleOfYearInMinutes = timeEquinox - 1;
			}
			timeEquinox = (middleOfYearInMinutes - firstOfYearInMinutes) / 2 + firstOfYearInMinutes;
			difference = toDegrees(computeTrueAnomaly(timeEquinox)) - ARGUMENT_OF_PERIAPSIS;
		}
		return timeEquinox;
	}

	private static void coolEarth(final double heatGain, final Set<GridPoint> previousGridPointSet) {
		final double totalPlanetHeat = SURFACEAREA_OF_EARTH * SimulationConstants.DEFAULT_CELL_TEMP + heatGain;

		for (final GridPoint gridPoint : experiment.getGridPoints().get(dateTimeOfExperiment)) {
			final double heatLoss = calculateSurfaceArea(gridPoint) * gridPoint.getTemperature() / totalPlanetHeat * heatGain;
			gridPoint.setTemperature(gridPoint.getTemperature() - heatLoss / calculateSurfaceArea(gridPoint));
		}
	}

	private static double diffuseHeatFromNeighbors(Set<GridPoint> gridPoints) {

		double heatChange = 0;
		final Set<GridPoint> heatedGridPoint = new HashSet<GridPoint>();
		for (final GridPoint gridPoint : gridPoints) {
			final GridPoint newGridPoint = gridPoint;
			final double verticalSideLength = calculateVerticalSideLength();
			final double northSideLength = calculateNorthSideLength(gridPoint, verticalSideLength);
			final double southSideLength = calculateSouthSideLength(gridPoint, verticalSideLength);
			final double gridPerimeter = northSideLength + southSideLength + 2 * verticalSideLength;

			final int gridSpacing = experiment.getSimulationSettings().getGridSpacing();
			final int latitude = gridPoint.getTopLatitude();
			final int longitude = gridPoint.getGridId();
			final double eastTemp = verticalSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude + gridSpacing, longitude).getTemperature();
			final double westTemp = verticalSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude - gridSpacing, longitude).getTemperature();
			final double northTemp = southSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude, longitude - gridSpacing).getTemperature();
			final double southTemp = northSideLength / gridPerimeter * getNeigbhorGridPoint(gridPoints, latitude, longitude + gridSpacing).getTemperature();
			final double neighborTemp = eastTemp + westTemp + northTemp + southTemp;

			final double newTemp = (neighborTemp + gridPoint.getTemperature()) / 2;
			heatChange += (newTemp * calculateSurfaceArea(gridPoint)) - gridPoint.getTemperature() * calculateSurfaceArea(gridPoint);

			newGridPoint.setTemperature(newTemp);
			heatedGridPoint.add(newGridPoint);
		}

		gridPoints = heatedGridPoint;
		return heatChange;
	}

	private static double heatGridFromSun(final int minutesFromStart, final Set<GridPoint> gridPoints) {
		final double proportionRadiation = getDistanceRadiationAdjustment(minutesFromStart);
		final double longitudeOfSun = computeSunNoonLongitude(minutesFromStart);
		final double latitudeOfSun = computeSunNoonLatitude(minutesFromStart);

		double heatGain = 0;
		for (final GridPoint gridPoint : gridPoints) {
			final double zenithAngle = calculateSolarZenith(gridPoint, longitudeOfSun, latitudeOfSun);
			final double tempIncrease = cos(zenithAngle) > 0 ? SOLAR_CONSTANT * proportionRadiation * cos(zenithAngle) * minutesFromStart : 0;
			heatGain += calculateSurfaceArea(gridPoint) * tempIncrease;
			gridPoint.setTemperature(gridPoint.getTemperature() + tempIncrease);
		}

		return heatGain;
	}

	private static double calculateSurfaceArea(final GridPoint gridPoint) {
		final double verticalSideLength = calculateVerticalSideLength();
		final double northSideLength = calculateNorthSideLength(gridPoint, verticalSideLength);
		final double southSideLength = calculateSouthSideLength(gridPoint, verticalSideLength);
		final double height = sqrt(pow(verticalSideLength, 2) - .25 * pow(northSideLength - southSideLength, 2));
		final double surfaceArea = .5 * (northSideLength + southSideLength) * height;
		return surfaceArea;
	}

	private static int calculateVerticalSideLength() {
		return CIRCUMFERENCE_OF_EARTH * experiment.getSimulationSettings().getGridSpacing() / WIDTH_IN_DEGREES;
	}

	private static double calculateNorthSideLength(final GridPoint gridPoint, final double verticalSideLength) {
		return cos(toRadians(gridPoint.getTopLatitude() + experiment.getSimulationSettings().getGridSpacing())) * verticalSideLength;
	}

	private static double calculateSouthSideLength(final GridPoint gridPoint, final double verticalSideLength) {
		return cos(toRadians(gridPoint.getTopLatitude())) * verticalSideLength;
	}

	private static double calculateLocalHourAngle(final double longitudeOfSun, final double longitudeOfPoint) {
		return longitudeOfPoint - longitudeOfSun;
	}

	private static double calculateSolarZenith(final GridPoint gridPpoint, final double longitudeOfSun, final double latitudeOfSun) {
		final double hourAngle = toRadians(calculateLocalHourAngle(longitudeOfSun, gridPpoint.getLeftLongitude()));
		final double declinationOfSun = toRadians(latitudeOfSun);
		final double pointLatitude = toRadians(gridPpoint.getTopLatitude());
		final double solarElevationAngle = asin(cos(hourAngle) * cos(declinationOfSun) * cos(pointLatitude) + sin(declinationOfSun) * sin(pointLatitude));
		return PI / 2 - solarElevationAngle;
	}

}
