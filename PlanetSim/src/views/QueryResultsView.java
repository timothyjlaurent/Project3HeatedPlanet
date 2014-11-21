package views;

import static java.util.Collections.sort;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import models.DatabaseQuery;
import models.Experiment;
import models.GridPoint;

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
			final List<Date> dates = new ArrayList<Date>(experiment.get(0).getGridPointMap().keySet());
			final Object[][] experimentValues = new Object[dates.size()][experiment.get(0).getGridPointMap().get(dates.get(0)).size() + 1];
			final Object[] experimentColumnHeaders = new Object[experiment.get(0).getGridPointMap().get(dates.get(0)).size() + 1];

			experimentColumnHeaders[0] = "";
			int y = 0;
			int header = 1;
			for (final Entry<Date, Set<GridPoint>> entry : experiment.get(0).getGridPointMap().entrySet()) {
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

	// Source:
	// http://www.todayisearched.com/2011/02/jtable-display-long-text-with-linebreak-word-wrap.html
	private class TableCellLongTextRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
			final JTextArea jtext = new JTextArea();
			jtext.setText((String) value);
			jtext.setWrapStyleWord(true);
			jtext.setLineWrap(true);
			if (isSelected) {
				jtext.setBackground((Color) UIManager.get("Table.selectionBackground"));
			}
			return jtext;
		}

		// METHODS overridden for performance
		@Override
		public void validate() {
		}

		@Override
		public void revalidate() {
		}

		@Override
		public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		}

		@Override
		public void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
		}

	}
}
