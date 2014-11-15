package edu.gatech.heatsimulation.utility;

public class ConfigurationConstant {
	
	public static final String EMPTY_STRING = "";
	
	public static enum ConcurrencyType {
    	CONCURRENCY_TYPE_SINGLE_THREAD(0),
    	CONCURRENCY_TYPE_SIMULATION_THREAD(1),
    	CONCURRENCY_TYPE_PRESENTATION_THREAD(2),
    	CONCURRENCY_TYPE_MULTIPLE_THREAD(3);
		
    	public int getConcurrencyType() {
			return concurrencyType;
		}
    	
		private ConcurrencyType(final int concurrencyType) {
			this.concurrencyType = concurrencyType;
		}
		 
		private int concurrencyType;		 
    };
    
    public static enum InitiativeType {
    	INITIATIVE_TYPE_MASTER(0),
    	INITIATIVE_TYPE_SIMULATION(1),
    	INITIATIVE_TYPE_PRESENTATION(2);
		
    	public int getInitiativeType() {
			return initiativeType;
		}
    	
		private InitiativeType(final int initiativeType) {
			this.initiativeType = initiativeType;
		}
		 
		private int initiativeType;		 
    };
    
    public static final int DEFAULT_BUFFER_LENGTH = 1;	
}
