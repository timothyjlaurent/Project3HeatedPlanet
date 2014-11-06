package simulation;

import java.util.Calendar;

import userControl.CommunicationConfig;
import userControl.SimulationOptions;
import presentation.Presentation;
import sync.StatusBuffer;

public class SimEngine implements Runnable {

	private Grid grid;
	private Grid gridTmp;
	private Calendar startTime;
	private int simulationTime;
	private float rotationAngle_deg;

	private float diffGrid = (float) 1e8;
	private float maxDiffGrid = (float) 0.00001;
	private int maxSimulationTime = 144000;

	private int steps = 0;
	private int simulationRate;
	private SimKernel simKernel;
	private SimulationOptions simOption;

	private static boolean hold = false;

	public static void setHold(boolean bHold) {
		hold = bHold;
		if (SimConstant.DEBUG) {
			System.out.println("Holding SimThread!");
		}
	}

	private static boolean kill = false;

	public static void setKill(boolean bKill) {
		kill = bKill;
	}

	private static SimEngine instance;

	public static SimEngine getInstance() {
		if (instance == null) {
			instance = new SimEngine();
		}
		return instance;
	}

	public static void destroy() {
		if (instance != null) {
			instance = null;
		}
	}

	private SimEngine() {
		kill = false;
		hold = false;
		initStatus();
	}

	public void initStatus() {
		steps = 0;
		startTime = Calendar.getInstance();
		startTime.set(1999, 12, 31, 0, 0, 0);
		simulationTime = 0;
		rotationAngle_deg = 0;
		simKernel = new SimKernel();
		simOption = SimulationOptions.getInstance();
		simulationRate = simOption.getSimulationRate();
		grid = new Grid(simOption.getGridSpacing(), SimConstant.AVERAGE_TEMP_K);
		gridTmp = new Grid(simOption.getGridSpacing(),
				SimConstant.AVERAGE_TEMP_K);
	}

	public boolean isTerminationSatisfied() {
		if (simulationTime >= maxSimulationTime || diffGrid < maxDiffGrid) {
			if (SimConstant.DEBUG) {
				System.out.println("Simulation Termination Satisfied!");
			}
			return true;
		} else {
			return false;
		}
	}

	public void run() {

		if (SimConstant.DEBUG) {
			System.out.println("Enterring SimEngine Thread!");
		}

		while (!kill && !isTerminationSatisfied()) {
			if (!hold) {
				runStep();
			} else {
				// wait a half-second so that the computer doesn't slow down too
				// much
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}

		if (SimConstant.DEBUG) {
			System.out.println("Leaving SimEngine Thread!");
		}
	}

	public boolean runStep() {

		if (!isTerminationSatisfied()) {
			// Update status
			steps++;

			// Update the rotation angle
			simulationTime += simOption.getSimulationRate();
			// rotationAngle_deg = -(float)(simulationTime % 1440) * 360 / 1440;
			rotationAngle_deg = -(float) (simulationTime % 1440) * 360 / 1440;
			if (rotationAngle_deg > 180) {
				rotationAngle_deg -= 360;
			} else if (rotationAngle_deg <= -180) {
				rotationAngle_deg += 360;
			}

			rotationAngle_deg = rotationAngle_deg + 180;
			diffGrid = simKernel.step(grid, gridTmp, rotationAngle_deg,
					simulationRate, simOption.getGridSpacing());

			swapGrid();

			if (SimConstant.DEBUG) {
				System.out.println(toString());
			}

			if (simOption.getCommConfig() == CommunicationConfig.PUSH) {
				Presentation.getInstance().setStatus(getStatus());
			} else if (simOption.getCommConfig() == CommunicationConfig.BUFFER) {
				StatusBuffer.getInstance().pushStatus(getStatus());
			}

			try {
				Thread.sleep(simOption.getSimulationDelay_ms());
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}

			return true;
		} else {
			return false;
		}
	}

	public int getSteps() {
		return steps;
	}

	public float getDiffGrid() {
		return diffGrid;
	}

	public SimStatus getStatus() {
		return new SimStatus(getGrid(), simulationTime, rotationAngle_deg,
				steps, isTerminationSatisfied());
	}

	public synchronized Grid getGrid() {
		// return grid.copy();
		return grid;
	}

	public synchronized void swapGrid() {
		Grid tmp = this.grid;
		this.grid = this.gridTmp;
		this.gridTmp = tmp;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("-------------steps: ");
		buffer.append(steps);
		buffer.append("-------------\n");

		buffer.append("Average Temperature:\n");
		buffer.append(grid.getAverageTemp());
		buffer.append("\n");

		buffer.append("grid:\n");
		buffer.append(grid.toString());
		buffer.append("\n");

		buffer.append("DiffGrid: ");
		buffer.append(diffGrid);

		return buffer.toString();
	}

	public void finalize() throws Throwable {
		this.grid = null;
		this.gridTmp = null;
		if (SimConstant.DEBUG == true) {
			System.out.println("SimEngine Finalized");
		}
		super.finalize();
	}
}
