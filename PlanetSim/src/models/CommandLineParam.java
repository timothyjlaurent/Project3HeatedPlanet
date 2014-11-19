package models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CommandLineParam {
	
	@Column(name="DATA_PRECISION")
	private int dataPrecision;
	
	@Column(name="GEO_PRECISION")
	private int geographicPrecision;
	
	@Column(name="TEMPORAL_PRECISION")
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
