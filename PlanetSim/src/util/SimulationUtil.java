package util;

import static constants.SimulationConstants.DEGREES_ROTATED_PER_MINUTE;
import static constants.SimulationConstants.MINUTES_IN_YEAR;
import static constants.SimulationConstants.WIDTH_IN_DEGREES;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

	public static SimulationStats calculateSimulationStats(final Set<GridPoint> gridPoints) {
		final SimulationStats stats = new SimulationStats();

		double sum = 0;
		for (final GridPoint gridPoint : gridPoints) {
			stats.setMin(min(stats.getMin(), gridPoint.getTemperature()));
			stats.setMax(Math.max(stats.getMax(), gridPoint.getTemperature()));
			sum += gridPoint.getTemperature();
		}
		stats.setMean(sum / gridPoints.size());
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
}
