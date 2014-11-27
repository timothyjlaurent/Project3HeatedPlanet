package models;

public class SimulationStats {
	private double min = Double.POSITIVE_INFINITY;
	private double max = Double.NEGATIVE_INFINITY;
	private double sum = 0;
	private double mean;
	private double standardDeviation;
	private double sunLng;
	private double sunLat;
	private double sunDist;

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

	public void setSunLong(final double lng) {
		setSunLng(lng);
	}

	public void setSunLat(final double sunLat) {
		this.sunLat = sunLat;
	}

	public double getSunLng() {
		return sunLng;
	}

	public void setSunLng(final double sunLng) {
		this.sunLng = sunLng;
	}

	public double getSunDist() {
		return sunDist;
	}

	public void setSunDist(double sunDist) {
		this.sunDist = sunDist;
	}

}
