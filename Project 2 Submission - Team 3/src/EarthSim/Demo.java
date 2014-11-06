package EarthSim;

import javax.swing.JFrame;

import EarthSim.Initiative.ConsumerInitiative;
import EarthSim.Initiative.Initiative;
import EarthSim.Initiative.InitiativeType;
import EarthSim.Initiative.ProducerInitiative;
import EarthSim.Simulation.Simulation;
import EarthSim.Simulation.ThreadedSimulation;

public class Demo extends JFrame {

	private static final long serialVersionUID = 1L;

	public static int APP_HEIGHT = 600;

	public static int APP_WIDTH = 800;

	public static boolean DEBUGGING = false;

	/********* Instance Variables ********/

	private int bufferSize = 1;

	private InitiativeType initiative = InitiativeType.MASTER;

	private boolean presentationThreaded = false;

	private boolean simulationThreaded = false;

	public Demo() {
		super("Heated Earth");
	}

	private Initiative getInitiative() {
		Initiative initiative;
		final Buffer buffer = new Buffer(bufferSize);
		switch (this.initiative) {
		case MASTER:
			// initiative = new MasterInitiative(buffer);
			initiative = new ProducerInitiative(buffer, simulationThreaded);
			break;
		case PRESENTATION:
			initiative = new ConsumerInitiative(buffer);
			break;
		case SIMULATION:
			initiative = new ProducerInitiative(buffer, simulationThreaded);
			break;
		default:
			return null;
		}

		initiative.consumer = getPresentation(initiative);
		initiative.producer = getSimulation(initiative);

		return initiative;
	}

	private Presentation getPresentation(final Initiative initiative) {
		return presentationThreaded ? new ThreadedPresentation(initiative) : new Presentation(initiative);
	}

	private Simulation getSimulation(final Initiative initiative) {
		return simulationThreaded ? new ThreadedSimulation(initiative) : new Simulation(initiative);
	}

	public void execute() {
		if (DEBUGGING) {
			// print initial object state
			System.out.println(toString());
		}

		this.setSize(APP_WIDTH, APP_HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(new MainScene(getInitiative()));

		// this should be the last operation called
		this.setVisible(true);
	}

	@Override
	public String toString() {
		return String.format("Demo[bufferSize: %d, initiative: %s, presentationThreaded: %b, simulationThreaded: %b]", bufferSize, initiative, presentationThreaded, simulationThreaded);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Demo demo = new Demo();
		for (int i = 0; i < args.length; i++) {
			if (args[i].matches("^-\\w$")) {
				switch (args[i].charAt(1)) {
				case 'b':
					demo.bufferSize = Integer.parseInt(args[++i]);
					break;
				case 'p':
					demo.presentationThreaded = true;
					break;
				case 'r':
					demo.initiative = InitiativeType.PRESENTATION;
					break;
				case 's':
					demo.simulationThreaded = true;
					break;
				case 't':
					demo.initiative = InitiativeType.SIMULATION;
					break;
				}
			}
		}
		demo.execute();
	}
}
