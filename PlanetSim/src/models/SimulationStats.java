package models;

import java.util.Date;

public class SimulationStats {
	private double min = Double.POSITIVE_INFINITY;
	private Date minDate;
	private double max = Double.NEGATIVE_INFINITY;
	private Date maxDate;
	private double sum = 0;
	private double mean;
	private double standardDeviation;

	public double getMin() {
		return min;
	}

	public void setMin(final double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(final double max) {
		this.max = max;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(final double sum) {
		this.sum = sum;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(final double mean) {
		this.mean = mean;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(final double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(final Date minDate) {
		this.minDate = minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(final Date maxDate) {
		this.maxDate = maxDate;
	}
}
