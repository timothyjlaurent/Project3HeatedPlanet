package controllers;

import models.CommandLineParam;

public class CommandLineController {

	public static CommandLineParam parse(final String... args) throws Exception {
		try {
			if (args.length % 2 == 1) {
				throw new Exception("Odd number of command-line arguments");
			}

			final CommandLineParam commandLineParameters = new CommandLineParam();

			for (int i = 0; i < args.length; i++) {
				if (args[i].matches("^-\\w$")) {
					switch (args[i].charAt(1)) {
					case 'p':
						commandLineParameters.setDataPrecision(validateInput(args[++i], 0, Integer.MAX_VALUE));
						break;
					case 'g':
						commandLineParameters.setGeographicPrecision(validateInput(args[++i], 0, 100));
						break;
					case 't':
						commandLineParameters.setTemporalPrecision(validateInput(args[++i], 0, 100));
						break;
					}
				}
			}

			return commandLineParameters;
		} catch (final Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private static int validateInput(final String value, final int minValue, final int maxValue) throws Exception {
		final int intValue = Integer.parseInt(value);
		if (intValue >= minValue && intValue <= maxValue) {
			return intValue;
		} else {
			throw new Exception("Invalid input.");
		}
	}
}
