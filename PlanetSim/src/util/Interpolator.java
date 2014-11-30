package util;

import static constants.SimulationConstants.HEIGHT_IN_DEGREES;
import static constants.SimulationConstants.WIDTH_IN_DEGREES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.DatabaseQuery;
import models.Experiment;
import models.GridPoint;

public class Interpolator {

	public static Map<Date, Set<GridPoint>> interpolate(final Experiment experiment, final DatabaseQuery query) throws CloneNotSupportedException {

		Map<Date, Set<GridPoint>> map = SimulationUtil.convertSetToMap(experiment.getGridPoints());

		if (experiment.getCommandLineParam().getTemporalPrecision() < 100) {

			map = interpolateTemporal(map, experiment, query);
		}
		if (experiment.getCommandLineParam().getGeographicPrecision() < 100) {

			map = interpolateGeographic(map, experiment);
		}

		return map;
	}

	/**
	 * Transforms an input geographically sparse
	 * 
	 * @param map
	 * @param experiment
	 * @return
	 */
	private static Map<Date, Set<GridPoint>> interpolateGeographic(final Map<Date, Set<GridPoint>> map, final Experiment experiment) {

		final Set<GridPoint> outSet = new HashSet<GridPoint>();
		for (final Date date : map.keySet()) {

			for (int i = 0; i < WIDTH_IN_DEGREES; i += experiment.getSimulationSettings().getGridSpacing()) {
				for (int j = 0; j < HEIGHT_IN_DEGREES; j += experiment.getSimulationSettings().getGridSpacing()) {
					final GridPoint gridPoint = new GridPoint();
					gridPoint.setLeftLongitude(i - 180);
					gridPoint.setTopLatitude(90 - j);
					gridPoint.setDateTime(date);

					final double latitude = (gridPoint.getTopLatitude() - experiment.getSimulationSettings().getGridSpacing() / 2.0);
					final double longitude = (gridPoint.getLeftLongitude() + experiment.getSimulationSettings().getGridSpacing() / 2.0);
					gridPoint.setTemperature(getPointTempGeographicInteroplate(gridPoint, latitude, longitude, experiment.getSimulationSettings().getGridSpacing(), map.get(date)));
					outSet.add(gridPoint);
				}
			}
		}

		return SimulationUtil.convertSetToMap(outSet);
	}

	/**
	 * Used a Inverse Distance Weighting to interpolate the value
	 * 
	 * @param gridPoint
	 * @param latitude
	 * @param longitude
	 * @param spacing
	 * @param set
	 * @return
	 */
	private static double getPointTempGeographicInteroplate(final GridPoint gridPoint, final double latitude, final double longitude, final int spacing, final Set<GridPoint> set) {
		double sumOfWeights = 0;

		double sumOfWeightedVals = 0;

		for (final GridPoint point : set) {
			if (point.getTopLatitude() == gridPoint.getTopLatitude() && point.getLeftLongitude() == gridPoint.getLeftLongitude()) {
				return point.getTemperature();
			}
			final double lat1 = (point.getTopLatitude() - spacing) / 2;
			final double long1 = (point.getLeftLongitude() + spacing) / 2;
			final double dist = SimulationUtil.calcDistanceBetweenLatLongPairs(latitude, longitude, lat1, long1);
			final double weight = 1 / Math.pow(dist, 1.3);
			sumOfWeights += weight;
			final double temp = point.getTemperature();
			sumOfWeightedVals += weight * temp;
		}
		return sumOfWeightedVals / sumOfWeights;
	}

	/**
	 * Returns temporally interpolated map of points.
	 * 
	 * @param map
	 * @param experiment
	 * @param query
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private static Map<Date, Set<GridPoint>> interpolateTemporal(final Map<Date, Set<GridPoint>> map, final Experiment experiment, final DatabaseQuery query) throws CloneNotSupportedException {

		final ArrayList<Date> storedTimePoints = new ArrayList<Date>(map.keySet());
		// Sorted list of datapoint dates
		Collections.sort(storedTimePoints);

		GridPoint[][] gridPrev = null;
		GridPoint[][] gridNext = null;

		final ArrayList<Long> outTimePoints = generateOutTimes(query, experiment);

		final Set<GridPoint> outSet = new HashSet<GridPoint>();

		// iterate through the timepoints points

		for (int i = 0; i < storedTimePoints.size(); i += 1) {

			if (i != storedTimePoints.size() - 1) {
				// get new grids
				gridPrev = SimulationUtil.convertSetToGrid(map.get(storedTimePoints.get(i)), experiment.getSimulationSettings().getGridSpacing());
				gridNext = SimulationUtil.convertSetToGrid(map.get(storedTimePoints.get(i + 1)), experiment.getSimulationSettings().getGridSpacing());
			}
			for (int j = 0; j < gridPrev.length; j += 1) {
				for (int k = 0; k < gridPrev[j].length; k += 1) {
					for (int l = 0; l < outTimePoints.size(); l += 1) { // the
																		// timepoints
																		// we
																		// need
																		// to
																		// output
						final long curTime = outTimePoints.get(l);
						final GridPoint point1 = gridPrev[j][k];
						final GridPoint point2 = gridNext[j][k];

						if (point1 != null) {
							final long point1Time = point1.getDateTime().getTime();
							final long point2Time = point2.getDateTime().getTime();
							// System.err.println( 'valid Point' );
							if (i != 0 && curTime < point1Time) {
								// do nothing

							} else if (curTime > point2Time && i != storedTimePoints.size() - 1) {
								// do nothing
							} else {
								final GridPoint pointToAdd = createTemporalInterpolatedGridPoint(point1,
										point2, curTime);
								outSet.add(pointToAdd);
							}
						}
					}

				}
			}
		}

		return SimulationUtil.convertSetToMap(outSet);
	}

	public static ArrayList<Long> generateOutTimes(final DatabaseQuery query, final Experiment experiment) {

		final ArrayList<Long> outList = new ArrayList<Long>();
		final long queryStartTime = query.getStartDateTime().getTime();
		final long queryEndTime = query.getEndDateTime().getTime();
		final long timeStep = experiment.getSimulationSettings().getTimeStep() * 60 * 1000;

		long curTime = experiment.getGridPoints().iterator().next().getDateTime().getTime();

		if (curTime < queryStartTime) {
			curTime = (long) (curTime + Math.ceil((queryStartTime - curTime) / (timeStep)) * timeStep);
		} else if (curTime > queryStartTime) {
			curTime = (long) (curTime - Math.floor(Math.abs(queryStartTime - curTime) / timeStep) * timeStep);
		}

		// Add the times to the
		do {
			outList.add(curTime);
			curTime += timeStep;
		} while (curTime <= queryEndTime);

		return outList;
	}

	private static GridPoint createTemporalInterpolatedGridPoint(final GridPoint point1, final GridPoint point2, final long interpolationTime) throws CloneNotSupportedException {

		final GridPoint outPoint = point1.clone();
		final double y0 = point1.getTemperature();
		final double y1 = point2.getTemperature();
		final double x0 = point1.getDateTime().getTime();
		final double x1 = point2.getDateTime().getTime();
		final double x = interpolationTime;

		final double tempDelta = y1 - y0;
		final double timeDelta = x1 - x0;

		final double outTemp = y0 + tempDelta * (x - x0) / timeDelta;
		outPoint.setTemperature(outTemp);
		outPoint.setDateTime(new Date(interpolationTime));

		return outPoint;
	}
}
