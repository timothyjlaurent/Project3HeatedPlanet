package views;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import models.CommandLineParam;
import dao.DatabaseDao;

public class PresentationView extends JFrame {

	private static final long serialVersionUID = 1L;

	public PresentationView(final CommandLineParam params, final DatabaseDao dao) {
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Simulation", new SimulationView(params, dao));

		final QueryInterfaceView queryInterfaceView = new QueryInterfaceView(params, dao);
		tabbedPane.addTab("Query", queryInterfaceView);

		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 1) {
					queryInterfaceView.updateExperiments();
				}
			}
		});
		add(tabbedPane);
	}
}
