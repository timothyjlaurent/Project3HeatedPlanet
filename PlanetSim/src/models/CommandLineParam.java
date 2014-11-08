package models;

public class CommandLineParam {
	private int dataPrecision;
	private int geographicPrecision;
	private int temporalPrecision;

	public int getDataPrecision() {
		return dataPrecision;
	}

	public void setDataPrecision(final int dataPrecision) {
		this.dataPrecision = dataPrecision;
	}

	public int getGeographicPrecision() {
		return geographicPrecision;
	}

	public void setGeographicPrecision(final int geographicPrecision) {
		this.geographicPrecision = geographicPrecision;
	}

	public int getTemporalPrecision() {
		return temporalPrecision;
	}

	public void setTemporalPrecision(final int temporalPrecision) {
		this.temporalPrecision = temporalPrecision;
	}
}
