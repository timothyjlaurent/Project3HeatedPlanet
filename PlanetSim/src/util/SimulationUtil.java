package util;

import static constants.SimulationConstants.CIRCUMFERENCE_OF_EARTH;
import static constants.SimulationConstants.DEGREES_ROTATED_PER_MINUTE;
import static constants.SimulationConstants.MINUTES_IN_YEAR;
import static constants.SimulationConstants.SURFACEAREA_OF_EARTH;
import static constants.SimulationConstants.WIDTH_IN_DEGREES;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import models.Experiment;
import models.GridPoint;
import models.SimulationStats;

public class SimulationUtil {

	public static double calculateDegreesToRotate(final int minutesPassed) {
		return (DEGREES_ROTATED_PER_MINUTE * minutesPassed) % WIDTH_IN_DEGREES;
	}

	/**
	 * 
	 * @param currentDateAndTime
	 * @return Minutes since last sun was closest to the Sun
	 */
	public static int calculateTimeSinceLastPerihelion(final int currentDateAndTime) {
		return currentDateAndTime % MINUTES_IN_YEAR;
	}

	public static double calculateStandardDeviation(final Set<GridPoint> collection, final double mean) {
		double temp = 0;
		int total = 0;
		for (final GridPoint gridPoint : collection) {
			temp += pow((mean - gridPoint.getTemperature()), 2);
			total++;
		}
		return sqrt(temp / total);
	}

	public static SimulationStats calculateSimulationStats(final Set<GridPoint> gridPoints, final Experiment experiment) {
		final SimulationStats stats = new SimulationStats();

		double sum = 0;
		for (final GridPoint gridPoint : gridPoints) {
			stats.setMin(min(stats.getMin(), gridPoint.getTemperature()));
			if (stats.getMin() == gridPoint.getTemperature()) {
				stats.setMinDate(gridPoint.getDateTime());
			}
			stats.setMax(Math.max(stats.getMax(), gridPoint.getTemperature()));

			if (stats.getMax() == gridPoint.getTemperature()) {
				stats.setMaxDate(gridPoint.getDateTime());
			}
			sum += gridPoint.getTemperature() * calculateSurfaceArea(experiment, gridPoint);
		}
		stats.setMean(sum / SURFACEAREA_OF_EARTH);
		stats.setStandardDeviation(calculateStandardDeviation(gridPoints, stats.getMean()));
		return stats;
	}

	public static GridPoint[][] convertSetToGrid(final Set<GridPoint> gridPoints, final int gridSpacing) {
		final int numOfCols = 360 / gridSpacing;
		final int numOfRows = 180 / gridSpacing;
		final GridPoint[][] gridPointGrid = new GridPoint[numOfCols][numOfRows];
		for (final GridPoint gridPoint : gridPoints) {
			final int x = (180 + gridPoint.getLeftLongitude()) / gridSpacing;
			final int y = (90 - gridPoint.getTopLatitude()) / gridSpacing;
			gridPointGrid[x][y] = gridPoint;
		}
		return gridPointGrid;
	}

	public static Map<Date, Set<GridPoint>> convertSetToMap(final Set<GridPoint> gridPoints) {
		final Map<Date, Set<GridPoint>> map = new HashMap<Date, Set<GridPoint>>();
		for (final GridPoint gridPoint : gridPoints) {
			if (map.get(gridPoint.getDateTime()) == null) {
				final HashSet<GridPoint> newHashSet = new HashSet<GridPoint>();
				newHashSet.add(gridPoint);
				map.put(gridPoint.getDateTime(), newHashSet);
			} else {
				map.get(gridPoint.getDateTime()).add(gridPoint);
			}
		}
		return map;
	}

	public static Set<GridPoint> convertMapToSet(final Map<Date, Set<GridPoint>> gridPoints) {
		final HashSet<GridPoint> set = new HashSet<GridPoint>();
		for (final Entry<Date, Set<GridPoint>> gridPointEntry : gridPoints.entrySet()) {
			set.addAll(gridPointEntry.getValue());
		}
		return set;
	}

	public static double calculateVerticalSideLength(final Experiment experiment) {
		final double circ = CIRCUMFERENCE_OF_EARTH;
		final double spacing = experiment.getSimulationSettings().getGridSpacing();
		final double width = WIDTH_IN_DEGREES;
		final double vlength = circ * spacing / width;
		return vlength;
	}

	public static double calculateSurfaceArea(final Experiment experiment, final GridPoint gridPoint) {
		final double verticalSideLength = calculateVerticalSideLength(experiment);
		final double northSideLength = calculateNorthSideLength(experiment, gridPoint, verticalSideLength);
		final double southSideLength = calculateSouthSideLength(gridPoint, verticalSideLength);
		final double height = sqrt(pow(verticalSideLength, 2) - .25 * pow(northSideLength - southSideLength, 2));
		final double surfaceArea = .5 * (northSideLength + southSideLength) * height;
		return surfaceArea;
	}

	public static double calculateNorthSideLength(final Experiment experiment, final GridPoint gridPoint, final double verticalSideLength) {
		return abs(cos(toRadians(gridPoint.getTopLatitude() + experiment.getSimulationSettings().getGridSpacing())) * verticalSideLength);
	}

	public static double calculateSouthSideLength(final GridPoint gridPoint, final double verticalSideLength) {
		return abs(cos(toRadians(gridPoint.getTopLatitude())) * verticalSideLength);
	}

	static double calcDistanceBetweenLatLongPairs(double lat1, double long1, double lat2, double long2) {
		final double radius = 6371;
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		long1 = Math.toRadians(long1);
		long2 = Math.toRadians(long2);
		final double deltaLat = (lat2 - lat1);
		final double deltaLong = long2 - long1;
	
		final double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
				Math.cos(lat1) * Math.cos(lat2) *
				Math.sin(deltaLong / 2) * Math.sin(deltaLong / 2);
		final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	
		return radius * c;
	
	}

}
