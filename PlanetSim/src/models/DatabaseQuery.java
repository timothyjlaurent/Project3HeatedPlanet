package models;

import java.util.Date;

public class DatabaseQuery {
	private int coordinateLatitudeOne;
	private int coordinateLatitudeTwo;
	private int coordinateLongitudeOne;
	private int coordinateLongitudeTwo;
	private Date startDateTime;
	private Date endDateTime;
	private int experimentId;

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

	public void setExperimentId(final int id) {
		experimentId = id;
	}

	public int getExperimentId() {
		return experimentId;
	}

}
