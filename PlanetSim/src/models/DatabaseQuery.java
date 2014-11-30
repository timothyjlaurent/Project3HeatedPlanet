package models;

import java.util.Date;

public class DatabaseQuery {
	private String expirementName;
	private int coordinateLatitudeOne;
	private int coordinateLatitudeTwo;
	private int coordinateLongitudeOne;
	private int coordinateLongitudeTwo;
	private Date startDateTime;
	private Date endDateTime;
	private int geoPrecision;
	private int temporalPrecision;
	private int dataPrecision;
	private double orbitalEccentricity;
	private double axialTilt;
	private int gridSpacing;
	private int timeStep;
	private int experimentId;

	public String getExpirementName() {
		return expirementName;
	}

	public void setExpirementName(final String expirementName) {
		this.expirementName = expirementName;
	}

	public int getCoordinateLatitudeOne() {
		return coordinateLatitudeOne;
	}

	public void setCoordinateLatitudeOne(final int coordinateLatitudeOne) {
		this.coordinateLatitudeOne = coordinateLatitudeOne;
	}

	public int getCoordinateLatitudeTwo() {
		return coordinateLatitudeTwo;
	}

	public void setCoordinateLatitudeTwo(final int coordinateLatitudeTwo) {
		this.coordinateLatitudeTwo = coordinateLatitudeTwo;
	}

	public int getCoordinateLongitudeOne() {
		return coordinateLongitudeOne;
	}

	public void setCoordinateLongitudeOne(final int coordinateLongitudeOne) {
		this.coordinateLongitudeOne = coordinateLongitudeOne;
	}

	public int getCoordinateLongitudeTwo() {
		return coordinateLongitudeTwo;
	}

	public void setCoordinateLongitudeTwo(final int coordinateLongitudeTwo) {
		this.coordinateLongitudeTwo = coordinateLongitudeTwo;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(final Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(final Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public double getOrbitalEccentricity() {
		return orbitalEccentricity;
	}

	public void setOrbitalEccentricity(final double orbitalEccentricity) {
		this.orbitalEccentricity = orbitalEccentricity;
	}

	public double getAxialTilt() {
		return axialTilt;
	}

	public void setAxialTilt(final double axialTilt) {
		this.axialTilt = axialTilt;
	}

	public int getGeoPrecision() {
		return geoPrecision;
	}

	public void setGeoPrecision(final int geoPrecision) {
		this.geoPrecision = geoPrecision;
	}

	public int getTemporalPrecision() {
		return temporalPrecision;
	}

	public void setTemporalPrecision(final int temporalPrecision) {
		this.temporalPrecision = temporalPrecision;
	}

	public int getDataPrecision() {
		return dataPrecision;
	}

	public void setDataPrecision(final int dataPrecision) {
		this.dataPrecision = dataPrecision;
	}

	public int getGridSpacing() {
		return gridSpacing;
	}

	public void setGridSpacing(final int gridSpacing) {
		this.gridSpacing = gridSpacing;
	}

	public int getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(final int timeStep) {
		this.timeStep = timeStep;
	}

	@Override
	public String toString() {
		return "DatabaseQuery [expirementName=" + expirementName + ", coordinateLatitudeOne=" + coordinateLatitudeOne + ", coordinateLatitudeTwo=" + coordinateLatitudeTwo + ", coordinateLongitudeOne=" + coordinateLongitudeOne + ", coordinateLongitudeTwo=" + coordinateLongitudeTwo + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", geoPrecision=" + geoPrecision + ", temporalPrecision=" + temporalPrecision + ", dataPrecision=" + dataPrecision + ", orbitalEccentricity="
				+ orbitalEccentricity + ", axialTilt=" + axialTilt + ", gridSpacing=" + gridSpacing + ", timeStep=" + timeStep + "]";
	}

	public void setExperimentId(final int id) {
		experimentId = id;
	}

	public int getExperimentId() {
		return experimentId;
	}

}
