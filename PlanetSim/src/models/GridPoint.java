package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grid_points")
public class GridPoint {

	@Id
	@Column(name = "grid_id")
	@GeneratedValue
	private int gridId = -1;

	@Column(name = "topLatitude")
	private int topLatitude;

	@Column(name = "leftLongitude")
	private int leftLongitude;

	@Column(name = "temperature")
	private double temperature;

	@Column(name = "experiment_id")
	private int experimentId;

	public int getGridId() {
		return gridId;
	}

	public void setGridId(int gridId) {
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

	@Override
	public String toString() {
		return "GridPoint [topLatitude=" + topLatitude + ", leftLongitude=" + leftLongitude + ", temperature=" + temperature + "]";
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridPoint other = (GridPoint) obj;
		if (leftLongitude != other.leftLongitude)
			return false;
		if (topLatitude != other.topLatitude)
			return false;
		return true;
	}

}
