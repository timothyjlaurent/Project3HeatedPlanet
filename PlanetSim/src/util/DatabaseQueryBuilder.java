package util;

import java.util.Date;

import models.DatabaseQuery;
import models.Experiment;

public class DatabaseQueryBuilder {

	private final DatabaseQuery query = new DatabaseQuery();

	public DatabaseQueryBuilder experiment(final Experiment experiment) {
		query.setExperimentId(experiment.getExperimentId());
		return this;
	}

	public DatabaseQueryBuilder coordinateLatitudeOne(final int coordinateLatitudeOne) {
		query.setCoordinateLatitudeOne(coordinateLatitudeOne);
		return this;
	}

	public DatabaseQueryBuilder coordinateLatitudeTwo(final int coordinateLatitudeTwo) {
		query.setCoordinateLatitudeTwo(coordinateLatitudeTwo);
		return this;
	}

	public DatabaseQueryBuilder coordinateLongitudeOne(final int coordinateLongitudeOne) {
		query.setCoordinateLongitudeOne(coordinateLongitudeOne);
		return this;
	}

	public DatabaseQueryBuilder coordinateLongitudeTwo(final int coordinateLongitudeTwo) {
		query.setCoordinateLongitudeTwo(coordinateLongitudeTwo);
		return this;
	}

	public DatabaseQueryBuilder startDateTime(final Date startDateTime) {
		query.setStartDateTime(startDateTime);
		return this;
	}

	public DatabaseQueryBuilder endDateTime(final Date endDateTime) {
		query.setEndDateTime(endDateTime);
		return this;
	}

	public DatabaseQueryBuilder experimentId(final int id) {
		query.setExperimentId(id);
		return this;
	}

	public DatabaseQuery build() {
		return query;
	}
}
