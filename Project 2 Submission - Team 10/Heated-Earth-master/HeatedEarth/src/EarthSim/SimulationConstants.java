package EarthSim;

public class SimulationConstants {
	public static final String APPLICATION_NAME = "Heated Earth";
	public static final int FRAME_HEIGHT = 500;
	public static final int FRAME_WIDTH = 500;
	public static final int FRAME_DESKTOP_BORDER_GAP = 100;
	public static final int PANEL_FRAME_BORDER_GAP = 25;
	public static final int PANEL_GRID_BORDER_GAP = 10;
	public static final int CONTROL_PANEL_WIDTH = 170;
	
	public static enum PlateCommandType {
    	PLATE_COMMAND_TYPE_TPDAHP(0),
    	PLATE_COMMAND_TYPE_TPFAHP(1),
    	PLATE_COMMAND_TYPE_TWFAHP(2),
    	PLATE_COMMAND_TYPE_TPDOHP(3);
		
    	public int getPlateType() {
			return plateCommandType;
		}
    	
		private PlateCommandType(final int plateCommandType) {
			this.plateCommandType = plateCommandType;
		}
		 
		private int plateCommandType;		 
    };
}
