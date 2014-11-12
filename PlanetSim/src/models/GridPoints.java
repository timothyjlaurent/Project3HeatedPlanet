package models;

public class GridPoints {

	private String date;
	private String time;
	private int topLatitude;
	private int leftLatitude;
	private Number temperature;

	public String getDate() {
		return date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(final String time) {
		this.time = time;
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
