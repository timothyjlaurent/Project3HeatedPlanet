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
public class GridPoints {

	@Id
	@Column(name ="grid_id")
    @GeneratedValue
	private int gridId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_time")
	private Date dateTime;

	@Column(name = "topLatitude")
	private int topLatitude;

	@Column(name = "leftLongitude")
	private int leftLongitude;

	@Column(name = "temperature")
	private Number temperature;
	
	@Column(name ="experiment_id")
	private int experimentId;

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

	public int getLeftLongitude() {
		return leftLongitude;
	}

	public void setLeftLongitude(final int leftLongitude) {
		this.leftLongitude = leftLongitude;
	}

	public Number getTemperature() {
		return temperature;
	}

	public void setTemperature(final Number temperature) {
		this.temperature = temperature;
	}

}
