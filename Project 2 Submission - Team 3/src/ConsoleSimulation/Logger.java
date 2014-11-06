package ConsoleSimulation;

public class Logger {
	private static final String DELIMITER = " :: ";
	private static final String ERROR = "ERROR";
	private static final String INFO = "INFO";
	
	public static void error(String... args) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(ERROR);
		
		for (String arg : args) {
			sb.append(DELIMITER);
			sb.append(arg);
		}
		
		System.out.println(sb.toString());
	}
	
	public static void info(String... args) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(INFO);
		
		for (String arg : args) {
			sb.append(DELIMITER);
			sb.append(arg);
		}
		
		System.out.println(sb.toString());
	}
}
