package views;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import dao.DatabaseDao;
import dao.DatabaseDaoFlatImpl;

public class PresentationView extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) {
		final PresentationView view = new PresentationView(new DatabaseDaoFlatImpl());
		view.setName("Heat Planet Simulation");
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setPreferredSize(new Dimension(1000, 800));
		view.setMaximumSize(new Dimension(1000, 800));
		view.setResizable(false);
		view.pack();
		view.setVisible(true);
	}

	public PresentationView(final DatabaseDao dao) {
		add(newTabbedPane(dao));
	}

	private JTabbedPane newTabbedPane(final DatabaseDao dao) {
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Simulation", new SimulationSettingsView());
		tabbedPane.addTab("Query", new QueryInterfaceView(dao));
		return tabbedPane;
	}

}
