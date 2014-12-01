package constants;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SimulationConstants {

	// DEFAULTS
	public static final int DEFAULT_CELL_TEMP = 288; // Degrees Kelvin
	public static final double DEFAULT_AXIAL_TILT = 23.44;
	public static final double DEFAULT_ORBITAL_ECCENTRICITY = .0167;
	public static final double DEFAULT_TILT = 23.44; // degrees
	public static final int DEFAULT_GRID_SPACING = 15; // Degrees (1 Timezone)
	public static final int DEFAULT_TIME_STEP = 1440; // Minutes (One Solar Day)
	public static final int DEFAULT_SIM_LENGTH = 12; // Solar Months
	public static final Calendar DEFAULT_START_DATE = new GregorianCalendar(2000, 0, 4, 0, 0);

	// DEFAULTS COMMAND LINE PARAMETERS
	public static final int DEFAULT_DATA_PRECISION = 5;
	public static final int DEFAULT_GEOGRAPHIC_PRECISION = 100;
	public static final int DEFAULT_TEMPORAL_PRECISION = 100;
	public static final double SEMIMAJOR_AXIS = 149600000;

	public static final double ARGUMENT_OF_PERIAPSIS = 114; // degrees
	// private static final int MINUTES_IN_YEAR = 525600;
	public static final int MINUTES_IN_YEAR = 525949; // this accounts for leap
														// years
	public static final int WIDTH_IN_DEGREES = 360;
	public static final int HEIGHT_IN_DEGREES = 180;
	public static final int CIRCUMFERENCE_OF_EARTH = 40030140;
	public static final double SURFACEAREA_OF_EARTH = 510072000000000.0;
	public static final double DEGREES_ROTATED_PER_MINUTE = 360.0 / 1440.0;
	public static final double HEAT_PER_MINUTE = 5000;
	public static final int MINUTES_IN_DAY = 1440;
	public static double SOLAR_CONSTANT = 1.1; // this is the solar constant of

}
