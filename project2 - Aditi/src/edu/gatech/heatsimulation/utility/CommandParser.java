package edu.gatech.heatsimulation.utility;

import edu.gatech.heatsimulation.utility.Configuration;
import static edu.gatech.heatsimulation.utility.ParameterConstant.*;
import static edu.gatech.heatsimulation.utility.ConfigurationConstant.*;
import static edu.gatech.heatsimulation.utility.ConfigurationConstant.ConcurrencyType.*;
import static edu.gatech.heatsimulation.utility.ConfigurationConstant.InitiativeType.*;


public class CommandParser {

	public static boolean parse(String[] command, Configuration configuration) {
	
		try {
			if (configuration == null) {
				printUsage();
				return false;
			}
		
			String prevParameter = EMPTY_STRING;
			if ((command != null) &&
				(command.length > 0)) {
				for (String parameter : command) {
					
					if (parameter != null) {
						if ((parameter.equalsIgnoreCase(PARAMETER_USAGE)) &&
							(command.length == 1)) {
							printUsage();
							return false;
						}
						
						if (parameter.startsWith(PARAMETER_SIMULATION_THREAD)) {
							configuration.setConcurrencyType(CONCURRENCY_TYPE_SIMULATION_THREAD);
						}
						
						if (parameter.startsWith(PARAMETER_PRESENTATION_THREAD)) {
							configuration.setConcurrencyType(CONCURRENCY_TYPE_PRESENTATION_THREAD);
						}
			
						if (parameter.startsWith(PARAMETER_SIMULAITION_INTIATIVE)) {
							configuration.setInitiativeType(INITIATIVE_TYPE_SIMULATION);
						}
						
						if (parameter.startsWith(PARAMETER_PRESENTATION_INTIATIVE)) {
							configuration.setInitiativeType(INITIATIVE_TYPE_PRESENTATION);
						}
						
						if (prevParameter.startsWith(PARAMETER_BUFFER)) {						
							configuration.setBufferLength(parameter);
						}
						
						prevParameter = parameter;
					}
				}
			}
		} catch (Exception exception) {
			printUsage();
		}
	    return true;
	}
	
	public static void printUsage() {
		System.out.println(APPLICATION_USAGE);
	}
}
