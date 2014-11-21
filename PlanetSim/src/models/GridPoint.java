package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "grid_points")
public class GridPoint implements Comparable<GridPoint> {

	@Id
	@GeneratedValue
	@Column(name = "grid_id")
	private int gridId = -1;

	@Column(name = "top_latitude")
	private int topLatitude;

	@Column(name = "left_longitude")
	private int leftLongitude;

	@Column(name = "temperature")
	private double temperature;

	@Column(name = "experiment_id")
	private int experimentId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_time")
	private Date dateTime;

	public int getGridId() {
		return gridId;
	}

	public void setGridId(final int gridId) {
		this.gridId = gridId;
	}

	public int getTopLatitude() {
		return topLatitude;
	}

	public void setTopLatitude(final int topLatitude) {
		this.topLatitude = topLatitude;
	}

	public int getLeftLongitude() {
		return leftLongitude;
	}

	public void setLeftLongitude(final int leftLongitude) {
		this.leftLongitude = leftLongitude;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(final double temperature) {
		this.temperature = temperature;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(final Date dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "GridPoint [topLatitude=" + topLatitude + ", leftLongitude=" + leftLongitude + ", temperature=" + temperature + ", date=" + dateTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + leftLongitude;
		result = prime * result + topLatitude;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GridPoint other = (GridPoint) obj;
		if (leftLongitude != other.leftLongitude) {
			return false;
		}
		if (topLatitude != other.topLatitude) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final GridPoint other) {
		int c;
		c = new Integer(getTopLatitude()).compareTo(new Integer(other.getTopLatitude()));
		if (c == 0) {
			c = new Integer(getLeftLongitude()).compareTo(new Integer(other.getLeftLongitude()));
		}
		return c;
	}

}
