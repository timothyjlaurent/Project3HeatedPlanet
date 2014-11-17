package edu.gatech.heatsimulation.model;

import static edu.gatech.heatsimulation.utility.SimulationConstant.*;

public class SimulationSetting {
	
	public boolean init() {
		simulationStep = SIMULATION_STEP_MIN;
		gridSpacing = GRID_SPACING_MIN;
		presentationDisplayRate = PRESENTATION_DISPLAY_RATE_MIN;
		return true;
	}
	
	public boolean init(int gridSpacing,
						int simulationStep,
						int presentationDisplayRate) {
		
		return (setGridSpacing(gridSpacing) &&
				setSimulationStep(simulationStep) &&
				setPresentationDisplayRate(presentationDisplayRate));
	}
	
	public int getSimulationStep() {
		return simulationStep;
	}
	
	public boolean setSimulationStep(int simulationStep) {
		if ((simulationStep < SIMULATION_STEP_MIN) &&
			(simulationStep > SIMULATION_STEP_MAX)) {
			return false;
		}
		this.simulationStep = simulationStep;
		return true;
	}
	
	public int getGridSpacing() {
		return gridSpacing;
	}
	
	public boolean setGridSpacing(int gridSpacing) {
		if ((gridSpacing < GRID_SPACING_MIN) &&
			(gridSpacing > GRID_SPACING_MAX)) {
				return false;
		}
		
		this.gridSpacing = gridSpacing;
		return true;
	}
	
	public int getPresentationDisplayRate() {
		return presentationDisplayRate;
	}
	
	public boolean setPresentationDisplayRate(int presentationDisplayRate) {
		if ((presentationDisplayRate < PRESENTATION_DISPLAY_RATE_MIN) &&
			(presentationDisplayRate > PRESENTATION_DISPLAY_RATE_MAX)) {
			return false;
		}
		
		this.presentationDisplayRate = presentationDisplayRate;
		return true;
	}
	
	private int simulationStep;	
	private int gridSpacing;
	private int presentationDisplayRate;	
}
