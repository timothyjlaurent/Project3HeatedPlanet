package models;

import constants.SimulationConstants;

public class PhysicalFactors {

	private double axialTilt = SimulationConstants.DEFAULT_AXIAL_TILT;
	private double orbitalEccentricity = SimulationConstants.DEFAULT_ORBITAL_ECCENTRICITY;

	public PhysicalFactors() {

	}

	public PhysicalFactors(final double axialTilt, final double orbitalEccentricity) {
		this.axialTilt = axialTilt;
		this.orbitalEccentricity = orbitalEccentricity;
	}

	public double getAxialTilt() {
		return axialTilt;
	}

	public void setAxialTilt(final double axialTilt) {
		this.axialTilt = axialTilt;
	}

	public double getOrbitalEccentricity() {
		return orbitalEccentricity;
	}

	public void setOrbitalEccentricity(final double orbitalEccentricity) {
		this.orbitalEccentricity = orbitalEccentricity;
	}

}
