package EarthSim;

public final class EarthConstants {
	
	public static final int DEFAULT_GRID_SPACING = 15;
	
	public static final int DEFAULT_TIME_STEP = 1;

	public static final int EARTH_CIRCUMFERENCE = 40030140;
	
	public static final long EARTH_HEMISPHERE_AREA = 255036000000000L;
	
	public static final int EARTH_DEGREES = 360;
	
	public static final int EARTH_RADIUS = 6371000;
	
	public static final long EARTH_SURFACE_AREA = 510072000000000L;
	
	public static final int MINUTES_IN_DAY = 1440;
	
	public static final int START_TEMP = 288;
	
	public static final int SUN_IN = 278;
	
	
	/*note this assumes we model earth as a 10cm deep plate to be heated. 
	volumetric heat capacities: water=4.1795 silica=1.547 both in J/cm^3/K 
	assume 2/1 ratio of water/silica for net of 330210J/m^2 at 10cm depth. Using watts=1366/m^2 per the domain model*/
	public static final double HEAT_PER_MINUTE= 2.713;
}
