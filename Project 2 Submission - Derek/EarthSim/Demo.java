package EarthSim;

import presentation.Gui;
import userControl.CommunicationConfig;
import userControl.SimulationOptions;
import userControl.ThreadConfig;
import userControl.UserControl;

public class Demo {

	public static void main(String[] args) {

		if (parseCommandLineArguments(args)) {
			Gui.getInstance();
			new UserControl();
		}
	}

	public static boolean parseCommandLineArguments(String[] arguments) {
		SimulationOptions options = SimulationOptions.getInstance();

		boolean simulationThread = false;
		boolean presentationThread = false;
		boolean commConfigSet = false;

		CommunicationConfig commConfig = CommunicationConfig.PUSH;

		boolean contProg = true;

		for (int i = 0; i < arguments.length; i++) {

			if (arguments[i].equalsIgnoreCase("-s")) {
				simulationThread = true;
			} else if (arguments[i].equalsIgnoreCase("-p")) {
				presentationThread = true;
			} else if (arguments[i].equalsIgnoreCase("-t")) {
				if (!commConfigSet) {
					commConfig = CommunicationConfig.PUSH;
					commConfigSet = true;
				} else {
					contProg = false;
				}
			} else if (arguments[i].equalsIgnoreCase("-r")) {
				if (!commConfigSet) {
					commConfig = CommunicationConfig.PULL;
					commConfigSet = true;
				} else {
					contProg = false;
				}
			} else if (arguments[i].equalsIgnoreCase("-b")) {
				if (!commConfigSet) {
					commConfig = CommunicationConfig.BUFFER;
					commConfigSet = true;
				} else {
					contProg = false;
				}
			}
		}

		if (!commConfigSet) {
			contProg = false;
		}

		if (contProg) {
			if (!simulationThread && !presentationThread) {
				options.setThreadConfig(ThreadConfig.NONE);
			} else if (!simulationThread && presentationThread) {
				options.setThreadConfig(ThreadConfig.PRESENTATIONONLY);
			} else if (simulationThread && !presentationThread) {
				options.setThreadConfig(ThreadConfig.SIMULATIONONLY);
			} else if (simulationThread && presentationThread) {
				options.setThreadConfig(ThreadConfig.SIMULATIONANDPRESENTATION);
			}
			options.setCommConfig(commConfig);

		} else {

			System.out.println("Usage:\n" + "\n" + "java " + "EarthSim" + " "
					+ "[" + "-s" + "]" + " " + "[" + "-p" + "]" + " " + "["
					+ "-t OR -r OR -b" + "]");
		}

		return contProg;
	}
}