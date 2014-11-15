package edu.gatech.heatsimulation.utility;

public class SimulationConstant {
	
	public static final String APPLICATION_NAME = "Heated Earth";
	public static final int FRAME_HEIGHT = 500;
	public static final int FRAME_WIDTH = 500;
	public static final int FRAME_DESKTOP_BORDER_GAP = 100;
	public static final int PANEL_FRAME_BORDER_GAP = 10;
	public static final int PANEL_GRID_BORDER_GAP = 10;
	public static final int CONTROL_PANEL_WIDTH = 170;
	public static final int CONTROL_PANEL_HEIGHT = 200;
	
	public static final int SIMULATION_STEP_MIN = 1;
	public static final int SIMULATION_STEP_MAX = 1440;
	
	public static final int GRID_SPACING_MIN = 1;
	public static final int GRID_SPACING_MAX = 180;
	
	public static final int PRESENTATION_DISPLAY_RATE_MIN = 1;
	public static final int PRESENTATION_DISPLAY_RATE_MAX = 86400;
	public static final int INDEX_ZERO = 0;
	public static final int BUFFER_SIZE_MIN = 1;
	public static final int MILLI_SECOND_ZERO = 0;
	public static final int COUNT_ZERO = 0;
	public static final String EMPTY_STRING = "";
	
	public static enum SimulationState {
    	SIMULATION_STATE_NONE(0),
    	SIMULATION_STATE_RUNNING(1),
    	SIMULATION_STATE_PAUSED(2);
		
    	public int getSimluationState() {
			return simulationState;
		}
    	
		private SimulationState(final int simulationState) {
			this.simulationState = simulationState;
		}
		 
		private int simulationState;		 
    };
    
    
    public static enum SimulationAction {
    	SIMULATION_ACTION_NONE(0),
    	SIMULATION_ACTION_START(1),
    	SIMULATION_ACTION_STOP(2),
    	SIMULATION_ACTION_PAUSE(3),
    	SIMULATION_ACTION_RESUME(4),
    	SIMULATION_ACTION_EXIT(5);
    	
    	public int getSimulationAction() {
			return simulationAction;
		}
    	
		private SimulationAction(final int simulationAction) {
			this.simulationAction = simulationAction;
		}
		 
		private int simulationAction;		 
    };
    
    
}
