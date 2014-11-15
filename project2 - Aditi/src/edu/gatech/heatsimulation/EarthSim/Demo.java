package edu.gatech.heatsimulation.EarthSim;

import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.utility.CommandParser;
import edu.gatech.heatsimulation.utility.Configuration;


public class Demo {

	public static void main(String[]args) {
		try {
			Configuration configuration = new Configuration();
			
			if ((configuration != null)) {
				
				if (CommandParser.parse(args, configuration)) {
					Controller controller = new Controller();
					controller.initialize(configuration);
					controller.simulate();
				}
			}
		} catch (Exception exception) {
			
		}
		
	}
}
