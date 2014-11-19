package util;

import static constants.SimulationConstants.DEGREES_ROTATED_PER_MINUTE;
import static constants.SimulationConstants.MINUTES_IN_YEAR;
import static constants.SimulationConstants.WIDTH_IN_DEGREES;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Collection;
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

	public static double calculateStandardDeviation(final Collection<Set<GridPoint>> collection, final double mean) {
		double temp = 0;
		int total = 0;
		for (final Set<GridPoint> set : collection) {
			for (final GridPoint gridPoint : set) {
				temp += pow((mean - gridPoint.getTemperature()), 2);
				total++;
			}
		}
		return sqrt(temp / total);
	}

	public SimulationStats calculateSimulationStats(final Experiment experiment) {
		final SimulationStats stats = new SimulationStats();

		double sum = 0;
		for (final Set<GridPoint> gridPointSet : experiment.getGridPoints().values()) {
			for (final GridPoint gridPoint : gridPointSet) {
				stats.setMin(min(stats.getMin(), gridPoint.getTemperature()));
				stats.setMax(Math.max(stats.getMax(), gridPoint.getTemperature()));
				sum += gridPoint.getTemperature();
			}
		}
		stats.setMean(sum / experiment.getGridPoints().size());
		stats.setStandardDeviation(calculateStandardDeviation(experiment.getGridPoints().values(), stats.getMean()));
		return stats;
	}
}
