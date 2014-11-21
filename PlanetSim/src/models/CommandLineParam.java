package models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import constants.SimulationConstants;

@Embeddable
public class CommandLineParam {

	@Column(name = "DATA_PRECISION")
	private int dataPrecision = SimulationConstants.DEFAULT_DATA_PRECISION;

	@Column(name = "GEO_PRECISION")
	private int geographicPrecision = SimulationConstants.DEFAULT_GEOGRAPHIC_PRECISION;

	@Column(name = "TEMPORAL_PRECISION")
	private int temporalPrecision = SimulationConstants.DEFAULT_TEMPORAL_PRECISION;

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
