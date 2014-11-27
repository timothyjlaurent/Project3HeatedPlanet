package views;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import java.awt.Component;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import models.DatabaseQuery;
import models.Experiment;
import models.GridPoint;
import models.SimulationStats;
import util.SimulationUtil;

public class QueryResultsView extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTable dataTable;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	public QueryResultsView() {
		dataTable = new JTable(new DefaultTableModel(new Object[0][0], new Object[0]));
		dataTable.setRowSelectionAllowed(false);
		dataTable.getTableHeader().setReorderingAllowed(false);
		dataTable.setPreferredSize(new Dimension(985, 450));
		dataTable.setRowHeight(dataTable.getRowHeight() * 2);
		dataTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		dataTable.setAlignmentY(Component.TOP_ALIGNMENT);

		final JScrollPane scollablePane = new JScrollPane(dataTable);
		scollablePane.setPreferredSize(new Dimension(985, 500));
		add(scollablePane);
	}

	public void updateExpirement(final List<Experiment> experiment, final DatabaseQuery query) {
		if (!experiment.isEmpty()) {
			final Map<Date, Set<GridPoint>> map = SimulationUtil.convertSetToMap(experiment.get(0).getGridPoints());

			final long diffInMilles = abs(query.getEndDateTime().getTime() - query.getStartDateTime().getTime());
			final long diffInMinutes = TimeUnit.MINUTES.convert(diffInMilles, TimeUnit.MILLISECONDS);
			final long numOfTimeSteps = diffInMinutes / query.getTimeStep();

			final long numOfLatitudeSpaces = abs(query.getCoordinateLatitudeTwo() - query.getCoordinateLatitudeOne()) / query.getGridSpacing();
			final long numOfLongitudeSpaces = abs(query.getCoordinateLongitudeTwo() - query.getCoordinateLongitudeOne()) / query.getGridSpacing();

			final Object[][] experimentValues = new Object[(int) numOfTimeSteps + 1][(int) (numOfLatitudeSpaces * numOfLongitudeSpaces) + 1];
			final Object[] experimentColumnHeaders = new Object[(int) (numOfLatitudeSpaces * numOfLongitudeSpaces) + 1];

			experimentColumnHeaders[0] = "";

			final Map<Integer, Set<GridPoint>> pointsPerRegion = new HashMap<Integer, Set<GridPoint>>();
			for (int i = 0; i < experimentValues.length; i++) {

				final Calendar cal = Calendar.getInstance();
				cal.setTime(query.getStartDateTime());
				cal.add(Calendar.MINUTE, (i) * query.getTimeStep());
				int pos = 1;

				final ArrayList<GridPoint> points = new ArrayList<GridPoint>(map.get(cal.getTime()));
				Collections.sort(points);

				for (int j = 0; j < numOfLatitudeSpaces; j++) {
					final SimulationStats stats = SimulationUtil.calculateSimulationStats(new HashSet<GridPoint>(points));
					for (int k = 0; k < numOfLongitudeSpaces; k++) {

						final StringBuilder builder = new StringBuilder();
						builder.append("<html>");
						builder.append("<div style=\"text-align: left;\">");
						builder.append("Lat:" + (min(query.getCoordinateLatitudeOne(), query.getCoordinateLatitudeTwo()) + j * query.getGridSpacing()));
						builder.append("  |  ");
						builder.append("Long:" + (min(query.getCoordinateLongitudeOne(), query.getCoordinateLongitudeTwo()) + k * query.getGridSpacing()));
						builder.append("<br/>Min Temp:" + stats.getMin());
						builder.append("@ " + getStringFromDate(stats.getMinDate(), false));
						builder.append("<br/>Max Temp:" + stats.getMax());
						builder.append("@ " + getStringFromDate(stats.getMaxDate(), false));
						builder.append("<br/>Mean Temp:" + stats.getMean());
						builder.append("</div>");
						builder.append("</html>");
						experimentColumnHeaders[pos] = builder.toString();
						experimentValues[i][pos] = Double.toString(points.get(pos - 1).getTemperature());

						if (pointsPerRegion.get(i) == null) {
							final Set<GridPoint> set = new HashSet<GridPoint>();
							set.add(points.get(pos - 1));
							pointsPerRegion.put(i, set);
						} else {
							pointsPerRegion.get(i).add(points.get(pos - 1));
						}
						pos++;
					}
				}

				final String formattedDate = getStringFromDate(cal.getTime(), false);
				final SimulationStats statsPerRegion = SimulationUtil.calculateSimulationStats(pointsPerRegion.get(i));
				experimentValues[i][0] = formattedDate + "\nMean: " + statsPerRegion.getMean();
			}

			dataTable.setModel(new DefaultTableModel(experimentValues, experimentColumnHeaders) {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(final int row, final int column) {
					return false;
				}

			});

			final TableColumn tc = dataTable.getColumnModel().getColumn(0);
			tc.setCellRenderer(new ScrollingTextAreaCellRenderer(2, 15));
		}
	}

	private String getStringFromDate(final Date date, final boolean newLine) {
		final StringBuilder builder = new StringBuilder();
		builder.append(dateFormat.format(date));
		builder.append(newLine ? "\n" : " ");
		builder.append(timeFormat.format(date));
		return builder.toString();
	}

	private class ScrollingTextAreaCellRenderer implements TableCellRenderer {
		private final JTextArea textArea;

		public ScrollingTextAreaCellRenderer(final int rows, final int cols) {
			textArea = new JTextArea(rows, cols);
			textArea.setEditable(false);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
			textArea.setText((String) value);
			return textArea;
		}
	}

}
