package views;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import models.CommandLineParam;
import dao.DatabaseDao;

public class PresentationView extends JFrame {

	private static final long serialVersionUID = 1L;

	public PresentationView(final CommandLineParam params, final DatabaseDao dao) {
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Simulation", new SimulationView(params, dao));
		tabbedPane.addTab("Query", new QueryInterfaceView(params, dao));
		add(tabbedPane);
	}
}
