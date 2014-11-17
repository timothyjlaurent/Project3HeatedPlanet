package constants;

public class SimulationConstants {

	// DEFAULTS
	final public static int DEFAULT_CELL_TEMP = 288; // Degrees Kelvin
	final public static double DEFAULT_AXIAL_TILT = 23.44;
	final public static double DEFAULT_ORBITAL_ECCENTRICITY = .0167;
	final public static double DEFAULT_TILT = 23.44; // degrees
	final public static int DEFAULT_GRID_SPACING = 15; // Degrees (1 Timezone)
	final public static int DEFAULT_TIME_STEP = 1440; // Minutes (One Solar Day)
	final public static int DEFAULT_SIM_LENGTH = 12; // Solar Months

	// DEFAULTS COMMAND LINE PARAMETERS
	final public static int DEFAULT_DATA_PRECISION = 8;
	final public static int DEFAULT_GEOGRAPHIC_PRECISION = 100;
	final public static int DEFAULT_TEMPORAL_PRECISION = 100;
	public static final double SEMIMAJOR_AXIS = 149600000;


	public static final double  ARGUMENT_OF_PERIAPSIS = 114 ; //degrees
	//	private static final int MINUTES_IN_YEAR = 525600;
	public static final int MINUTES_IN_YEAR = 525949;  // this accounts for leap years
}
