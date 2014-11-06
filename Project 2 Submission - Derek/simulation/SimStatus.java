package simulation;

public class SimStatus {

	private Grid myGrid;
	private int simulationTime_min; // unit: minute
	private float rotatioAngle_deg;
	private int simStep;
	private boolean finished = false;

	public SimStatus(Grid myGrid, int simulationTime_min,
			float rotatioAngle_deg, int simStep, boolean bFinished) {
		this.myGrid = myGrid;
		this.simulationTime_min = simulationTime_min;
		this.rotatioAngle_deg = rotatioAngle_deg;
		this.simStep = simStep;
		this.finished = bFinished;
	}

	public void setGrid(Grid grid) {
		this.myGrid = grid;
	}

	public Grid getGrid() {
		return myGrid;
	}

	public void setSimulationTime_min(int simulationTime_min) {
		this.simulationTime_min = simulationTime_min;
	}

	public int getSimulationTime_min() {
		return simulationTime_min;
	}

	public void setRotatioAngleDeg(float rotatioAngle_deg) {
		this.rotatioAngle_deg = rotatioAngle_deg;
	}

	public float getRotatioAngleDeg() {
		return rotatioAngle_deg;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setSimStep(int simStep) {
		this.simStep = simStep;
	}

	public int getSimStep() {
		return simStep;
	}

	public void finalize() throws Throwable {
		this.myGrid = null;
		if (SimConstant.DEBUG == true) {
			System.out.println("SimStatus Finalized");
		}
		super.finalize();
	}
}