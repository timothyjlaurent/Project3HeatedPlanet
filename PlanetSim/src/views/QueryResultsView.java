package views;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.Experiment;

public class QueryResultsView extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTable dataTable;

	public QueryResultsView() {
		dataTable = new JTable(new DefaultTableModel(new Object[0][0], new Object[0]));
		final JScrollPane scollablePane = new JScrollPane(dataTable);
		scollablePane.setPreferredSize(new Dimension(985, 500));
		add(scollablePane);
	}

	public void updateExpirement(final Experiment experiment) {
		final Object[][] experimentValues = new Object[experiment.getGridPoints().keySet().size()][experiment.getNumOfRegions() + 1];
		final Object[] experimentColumnHeaders = new Object[experiment.getNumOfRegions() + 1];

		experimentColumnHeaders[0] = "";
		for (int i = 1; i < experimentColumnHeaders.length; i++) {

		}

		dataTable.setModel(new DefaultTableModel(experimentValues, experimentColumnHeaders));
	}
}
