package views;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import models.CommandLineParam;
import dao.DatabaseDao;

public class PresentationView extends JFrame {

	private static final long serialVersionUID = 1L;

	public PresentationView(CommandLineParam params, final DatabaseDao dao) {
		add(newTabbedPane(dao));
	}

	private JTabbedPane newTabbedPane(final DatabaseDao dao) {
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Simulation", new SimulationSettingsView());
		tabbedPane.addTab("Query", new QueryInterfaceView(dao));
		return tabbedPane;
	}

}
