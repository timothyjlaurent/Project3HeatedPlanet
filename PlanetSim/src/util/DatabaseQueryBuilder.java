package util;

import java.util.Date;

import models.DatabaseQuery;
import models.Experiment;

public class DatabaseQueryBuilder {

	private final DatabaseQuery query = new DatabaseQuery();

	public DatabaseQueryBuilder experiment(final Experiment experiment) {
		query.setExpirementName(experiment.getSimulationSettings().getExperimentName());
		query.setGridSpacing(experiment.getSimulationSettings().getGridSpacing());
		query.setTimeStep(experiment.getSimulationSettings().getGridSpacing());
		query.setDataPrecision(experiment.getCommandLineParam().getDataPrecision());
		query.setTemporalPrecision(experiment.getCommandLineParam().getTemporalPrecision());
		query.setGeoPrecision(experiment.getCommandLineParam().getGeographicPrecision());
		query.setAxialTilt(experiment.getPhysicalFactors().getAxialTilt());
		query.setOrbitalEccentricity(experiment.getPhysicalFactors().getOrbitalEccentricity());
		return this;
	}

	public DatabaseQueryBuilder experimentName(final String experimentName) {
		query.setExpirementName(experimentName);
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

	public DatabaseQueryBuilder geoPrecision(final int geoPrecision) {
		query.setGeoPrecision(geoPrecision);
		return this;
	}

	public DatabaseQueryBuilder temporalPrecision(final int temporalPrecision) {
		query.setTemporalPrecision(temporalPrecision);
		return this;
	}

	public DatabaseQueryBuilder dataPrecision(final int dataPrecision) {
		query.setDataPrecision(dataPrecision);
		return this;
	}

	public DatabaseQueryBuilder orbitalEccentricity(final double orbitalEccentricity) {
		query.setOrbitalEccentricity(orbitalEccentricity);
		return this;
	}

	public DatabaseQueryBuilder axialTilt(final double axialTilt) {
		query.setAxialTilt(axialTilt);
		return this;
	}

	public DatabaseQueryBuilder gridSpacing(final int gridSpacing) {
		query.setGridSpacing(gridSpacing);
		return this;
	}

	public DatabaseQueryBuilder timeStep(final int timeStep) {
		query.setTimeStep(timeStep);
		return this;
	}

	public DatabaseQuery build() {
		return query;
	}
}
