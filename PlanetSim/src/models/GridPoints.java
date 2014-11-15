package models;

import java.util.Date;

public class GridPoints {

	private Date dateTime;
	private int topLatitude;
	private int leftLatitude;
	private Number temperature;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(final Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getTopLatitude() {
		return topLatitude;
	}

	public void setTopLatitude(final int topLatitude) {
		this.topLatitude = topLatitude;
	}

	public int getLeftLatitude() {
		return leftLatitude;
	}

	public void setLeftLatitude(final int leftLatitude) {
		this.leftLatitude = leftLatitude;
	}

	public Number getTemperature() {
		return temperature;
	}

	public void setTemperature(final Number temperature) {
		this.temperature = temperature;
	}

}
