package views;

import static java.util.Collections.sort;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.DatabaseQuery;
import models.Experiment;
import models.GridPoint;
import util.SimulationUtil;

public class QueryResultsView extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTable dataTable;

	public QueryResultsView() {
		dataTable = new JTable(new DefaultTableModel(new Object[0][0], new Object[0]));
		final JScrollPane scollablePane = new JScrollPane(dataTable);
		scollablePane.setPreferredSize(new Dimension(985, 500));
		add(scollablePane);
	}

	public void updateExpirement(final List<Experiment> experiment, final DatabaseQuery query) {
		if (!experiment.isEmpty()) {
			final Map<Date, Set<GridPoint>> map = SimulationUtil.convertSetToMap(experiment.get(0).getGridPoints());
			final List<Date> dates = new ArrayList<Date>(map.keySet());
			final Object[][] experimentValues = new Object[dates.size()][map.get(dates.get(0)).size() + 1];
			final Object[] experimentColumnHeaders = new Object[map.get(dates.get(0)).size() + 1];

			experimentColumnHeaders[0] = "";
			int y = 0;
			int header = 1;
			for (final Entry<Date, Set<GridPoint>> entry : map.entrySet()) {
				int x = 0;
				experimentColumnHeaders[header] = "Header";

				final List<GridPoint> sortableList = new ArrayList<GridPoint>(entry.getValue());
				sort(sortableList);

				for (final GridPoint gridPoint : sortableList) {
					experimentValues[x][y] = gridPoint.getTemperature();
					x++;
				}
				header++;
				y++;
			}
			dataTable.setModel(new DefaultTableModel(experimentValues, experimentColumnHeaders));
		}
	}
}
