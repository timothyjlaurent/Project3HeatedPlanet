package models;

import constants.SimulationConstants;

public class PhysicalFactors {

	private double axialTilt = SimulationConstants.DEFAULT_AXIAL_TILT;
	private double oribitalEccentricity = SimulationConstants.DEFAULT_ORBITAL_ECCENTRICITY;

	public PhysicalFactors() {

	}

	public PhysicalFactors(final double axialTilt, final double oribitalEccentricity) {
		this.axialTilt = axialTilt;
		this.oribitalEccentricity = oribitalEccentricity;
	}

	public double getAxialTilt() {
		return axialTilt;
	}

	public void setAxialTilt(final double axialTilt) {
		this.axialTilt = axialTilt;
	}

	public double getOribitalEccentricity() {
		return oribitalEccentricity;
	}

	public void setOribitalEccentricity(final double oribitalEccentricity) {
		this.oribitalEccentricity = oribitalEccentricity;
	}

}
