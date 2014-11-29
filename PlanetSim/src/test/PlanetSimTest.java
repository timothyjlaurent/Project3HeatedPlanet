package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import models.CommandLineParam;
import models.Experiment;
import models.PhysicalFactors;
import models.SimulationSettings;
import constants.SimulationConstants;
import controllers.SimulationController;

public class PlanetSimTest {

	private static int displayRate;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		new File("TestResults.csv").delete();
		runPrecisionTests();
		runGeographicTests();
		runTemporalTests();
		runAxialTiltTests();
		runOrbitalTests();
		runGridSpacingTests();
		runSimulationTimeTests();
		runSimulationLengthTests();
	}

	public static void runSimulation(final CommandLineParam params, final SimulationSettings settings, final PhysicalFactors factors) {
		final Experiment experiment = SimulationController.initializeGridPoints(params, settings, factors, SimulationConstants.DEFAULT_START_DATE);

		int minutesTimePassed = 0;
		final long startTime = System.currentTimeMillis();
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final int simulationTime = experiment.getSimulationSettings().getSimulationLength() * SimulationConstants.MINUTES_IN_YEAR / 12;

		while (minutesTimePassed <= simulationTime) {
			SimulationController.simulateIteration(experiment, minutesTimePassed);
			minutesTimePassed += experiment.getSimulationSettings().getTimeStep();
		}

		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long endTime = System.currentTimeMillis();

		final long memoryUsage = totalMemory - freeMemory;
		final long totalTime = endTime - startTime;

		writeToFile(memoryUsage, totalTime, experiment);
	}

	private static void runPrecisionTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setGeographicPrecision(100);
		params.setTemporalPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setGridSpacing(15);
		settings.setSimulationLength(12);
		settings.setTimeStep(1440);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(23.44);
		factors.setOrbitalEccentricity(.0167);

		writeHeader("Data Precision");

		for (int i = 0; i <= 8; i += 2) {
			params.setDataPrecision(i);
			runSimulation(params, settings, factors);
		}
	}

	private static void runGeographicTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setDataPrecision(5);
		params.setTemporalPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setGridSpacing(15);
		settings.setSimulationLength(12);
		settings.setTimeStep(1440);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(23.44);
		factors.setOrbitalEccentricity(.0167);

		writeHeader("Geographic Precision");

		for (int i = 10; i <= 90; i += 20) {
			params.setGeographicPrecision(i);
			runSimulation(params, settings, factors);
		}
	}

	private static void runTemporalTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setDataPrecision(5);
		params.setGeographicPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setGridSpacing(15);
		settings.setSimulationLength(12);
		settings.setTimeStep(1440);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(23.44);
		factors.setOrbitalEccentricity(.0167);

		writeHeader("Temporal Precision");

		for (int i = 10; i <= 90; i += 20) {
			params.setTemporalPrecision(i);
			runSimulation(params, settings, factors);
		}
	}

	private static void runAxialTiltTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setDataPrecision(5);
		params.setTemporalPrecision(100);
		params.setGeographicPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setGridSpacing(15);
		settings.setSimulationLength(12);
		settings.setTimeStep(1440);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setOrbitalEccentricity(.0167);

		writeHeader("Axial Tilt");

		factors.setAxialTilt(-180);
		runSimulation(params, settings, factors);

		factors.setAxialTilt(-72);
		runSimulation(params, settings, factors);

		factors.setAxialTilt(0);
		runSimulation(params, settings, factors);

		factors.setAxialTilt(72);
		runSimulation(params, settings, factors);

		factors.setAxialTilt(180);
		runSimulation(params, settings, factors);
	}

	private static void runOrbitalTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setDataPrecision(5);
		params.setGeographicPrecision(100);
		params.setTemporalPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setGridSpacing(15);
		settings.setSimulationLength(12);
		settings.setTimeStep(1440);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(23.44);

		writeHeader("Orbital Eccentricty");

		for (double i = .01; i <= .09; i += .02) {
			factors.setOrbitalEccentricity(i);
			runSimulation(params, settings, factors);
		}
	}

	private static void runGridSpacingTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setDataPrecision(5);
		params.setGeographicPrecision(100);
		params.setTemporalPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setSimulationLength(12);
		settings.setTimeStep(1440);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(23.44);
		factors.setOrbitalEccentricity(.0167);

		writeHeader("Grid Spacing");

		settings.setGridSpacing(10);
		runSimulation(params, settings, factors);

		settings.setGridSpacing(15);
		runSimulation(params, settings, factors);

		settings.setGridSpacing(30);
		runSimulation(params, settings, factors);

		settings.setGridSpacing(90);
		runSimulation(params, settings, factors);
	}

	private static void runSimulationTimeTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setDataPrecision(5);
		params.setGeographicPrecision(100);
		params.setTemporalPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setGridSpacing(15);
		settings.setSimulationLength(12);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(23.44);
		factors.setOrbitalEccentricity(.0167);

		writeHeader("Simulation Time");

		for (int i = 1440; i <= 12960; i += 1440) {
			settings.setTimeStep(i);
			runSimulation(params, settings, factors);
		}
	}

	private static void runSimulationLengthTests() {
		final CommandLineParam params = new CommandLineParam();
		params.setDataPrecision(5);
		params.setGeographicPrecision(100);
		params.setTemporalPrecision(100);

		final SimulationSettings settings = new SimulationSettings();
		settings.setExperimentName("Precision Test");
		settings.setGridSpacing(15);
		settings.setTimeStep(1440);

		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(23.44);
		factors.setOrbitalEccentricity(.0167);

		writeHeader("Simulation Length");

		for (int i = 100; i <= 900; i += 200) {
			settings.setSimulationLength(i);
			runSimulation(params, settings, factors);
		}
	}

	private static void writeToFile(final long memoryUsage, final long totalTime, final Experiment experiment) {
		try {
			final String filename = "TestResults.csv";
			final boolean append = true;

			final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, append)));

			out.print(experiment.getCommandLineParam().getDataPrecision());
			out.print(",");
			out.print(experiment.getCommandLineParam().getGeographicPrecision());
			out.print(",");
			out.print(experiment.getCommandLineParam().getTemporalPrecision());
			out.print(",");
			out.print(experiment.getPhysicalFactors().getAxialTilt());
			out.print(",");
			out.print(experiment.getPhysicalFactors().getOrbitalEccentricity());
			out.print(",");
			out.print(experiment.getSimulationSettings().getGridSpacing());
			out.print(",");
			out.print(experiment.getSimulationSettings().getTimeStep());
			out.print(",");
			out.print(experiment.getSimulationSettings().getSimulationLength());
			out.print(",");
			out.print(displayRate);
			out.print(",");
			out.print(memoryUsage);
			out.print(",");
			out.print(totalTime);
			out.print(",");
			out.println();
			out.close();
		} catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void writeHeader(final String scenario) {
		final String filename = "TestResults.csv";
		final boolean append = true;

		try {
			final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, append)));

			out.print(scenario);
			out.println();
			out.print("Precision");
			out.print(",");
			out.print("Geographic Precision");
			out.print(",");
			out.print("Temporal Precision");
			out.print(",");
			out.print("Axial Tilt");
			out.print(",");
			out.print("Orbital Eccentricity");
			out.print(",");
			out.print("Grid Spacing");
			out.print(",");
			out.print("Simulation Time Step");
			out.print(",");
			out.print("Simulation Length");
			out.print(",");
			out.print("Simulation Rate");
			out.print(",");
			out.print("Memory");
			out.print(",");
			out.print("Time");
			out.println();
			out.close();
		} catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
}
