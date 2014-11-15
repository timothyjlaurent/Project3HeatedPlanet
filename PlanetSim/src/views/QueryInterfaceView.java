package views;

import static util.GuiUtil.buildConstraints;
import static util.GuiUtil.buildNumberFormatter;
import static util.GuiUtil.getIntValue;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.DatabaseQuery;
import dao.DatabaseDao;

public class QueryInterfaceView extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private GridBagConstraints labelConstraints;
	private GridBagConstraints valueConstraints;

	private JComboBox comboBoxSimulationName;

	private final JComboBox comboBoxFromYears = new JComboBox(getArrayOfIntegers(Calendar.getInstance().get(Calendar.YEAR), true, "0000", 0));
	private final JComboBox comboBoxFromMonths = new JComboBox(getArrayOfIntegers(12));
	private final JComboBox comboBoxFromDays = new JComboBox();
	private final JComboBox comboBoxFromHours = new JComboBox(getArrayOfIntegers(24, false, "00", 1));;
	private final JComboBox comboBoxFromMinutes = new JComboBox(getArrayOfIntegers(60, false, "00", 1));

	private final JComboBox comboBoxToYears = new JComboBox(getArrayOfIntegers(Calendar.getInstance().get(Calendar.YEAR), true, "0000", 0));
	private final JComboBox comboBoxToMonths = new JComboBox(getArrayOfIntegers(12));
	private final JComboBox comboBoxToDays = new JComboBox();
	private final JComboBox comboBoxToHours = new JComboBox(getArrayOfIntegers(24, false, "00", 1));;
	private final JComboBox comboBoxToMinutes = new JComboBox(getArrayOfIntegers(60, false, "00", 1));;

	private final JFormattedTextField inputCoordinateOneLatitude = new JFormattedTextField(buildNumberFormatter(-180, 180));;
	private final JFormattedTextField inputCoordinateOneLongitude = new JFormattedTextField(buildNumberFormatter(-180, 180));;
	private final JFormattedTextField inputCoordinateTwoLatitude = new JFormattedTextField(buildNumberFormatter(-180, 180));;
	private final JFormattedTextField inputCoordinateTwoLongitude = new JFormattedTextField(buildNumberFormatter(-180, 180));;

	private JButton buttonQuery;

	private final DatabaseDao dao;
	private DatabaseQuery databaseQuery;
	private final QueryResultsView resultsPanel = new QueryResultsView();

	public QueryInterfaceView(final DatabaseDao dao) {
		this.dao = dao;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(resultsPanel);
		add(buildQuerySettingsAndInfoPanel());
		add(buildControlsPanel());
	}

	private JPanel buildControlsPanel() {
		final JPanel controlsPanel = new JPanel();
		controlsPanel.setBackground(Color.DARK_GRAY);

		buttonQuery = new JButton("Query");
		buttonQuery.addActionListener(this);

		controlsPanel.add(buttonQuery);
		return controlsPanel;
	}

	private JPanel buildQuerySettingsAndInfoPanel() {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Query:"));

		panel.add(newSimulationPanel());
		panel.add(newLatLongPanel());
		panel.add(newFromDateAndTimePanel());
		panel.add(newToDateAndTimePanel());

		return panel;
	}

	private JPanel newSimulationPanel() {
		final JPanel simulationPanel = new JPanel(new GridBagLayout());
		labelConstraints = buildConstraints(GridBagConstraints.CENTER, 0, new Insets(10, 0, 0, 0));
		valueConstraints = buildConstraints(GridBagConstraints.CENTER, 1, new Insets(10, 10, 0, 0));
		simulationPanel.setBorder(BorderFactory.createTitledBorder("Simulation Factors"));

		// Simulation Name
		simulationPanel.add(new JLabel("Simulation Name:"), labelConstraints);
		comboBoxSimulationName = new JComboBox(new DefaultComboBoxModel(dao.getExperimentNames().toArray()));
		simulationPanel.add(comboBoxSimulationName, valueConstraints);

		return simulationPanel;
	}

	private JPanel newLatLongPanel() {
		final JPanel latLongPanel = new JPanel();
		latLongPanel.setLayout(new BoxLayout(latLongPanel, BoxLayout.Y_AXIS));
		latLongPanel.setBorder(BorderFactory.createTitledBorder("Latitude/Longitudes"));

		latLongPanel.add(buildCoordinatePanel("Coordinate One", inputCoordinateOneLatitude, inputCoordinateOneLongitude));
		latLongPanel.add(buildCoordinatePanel("Coordinate Two", inputCoordinateTwoLatitude, inputCoordinateTwoLongitude));

		return latLongPanel;
	}

	private JPanel buildCoordinatePanel(final String title, final JFormattedTextField inputCoordinateLatitude, final JFormattedTextField inputCoordinateLongitude) {
		final JPanel coordinatePanel = new JPanel();
		coordinatePanel.setBorder(BorderFactory.createTitledBorder(title));
		labelConstraints = buildConstraints(GridBagConstraints.NORTHWEST, 0, new Insets(10, 10, 0, 0));
		valueConstraints = buildConstraints(GridBagConstraints.NORTHWEST, 1, new Insets(10, 10, 0, 0));

		addCoordinateLabelAndInput(coordinatePanel, inputCoordinateLatitude, "Latitude: ");
		addCoordinateLabelAndInput(coordinatePanel, inputCoordinateLongitude, "Longitude: ");

		return coordinatePanel;
	}

	private void addCoordinateLabelAndInput(final JPanel coordinatePanel, final JFormattedTextField input, final String title) {
		coordinatePanel.add(new JLabel(title), labelConstraints);
		input.setColumns(5);
		coordinatePanel.add(input, valueConstraints);
	}

	private JPanel newFromDateAndTimePanel() {
		final JPanel fromDateAndTimePanel = new JPanel();
		fromDateAndTimePanel.setLayout(new BoxLayout(fromDateAndTimePanel, BoxLayout.Y_AXIS));
		fromDateAndTimePanel.add(builDatePanel("From Month/Day/Year:", comboBoxFromYears, comboBoxFromMonths, comboBoxFromDays, 3));
		fromDateAndTimePanel.add(builTimePanel("From Hour/Minute:", comboBoxFromHours, comboBoxFromMinutes));
		return fromDateAndTimePanel;
	}

	private JPanel newToDateAndTimePanel() {
		final JPanel toDateAndTimePanel = new JPanel(new GridBagLayout());
		toDateAndTimePanel.setLayout(new BoxLayout(toDateAndTimePanel, BoxLayout.Y_AXIS));
		toDateAndTimePanel.add(builDatePanel("To Month/Day/Year:", comboBoxToYears, comboBoxToMonths, comboBoxToDays, 10));
		toDateAndTimePanel.add(builTimePanel("To Hour/Minute:", comboBoxToHours, comboBoxToMinutes));
		return toDateAndTimePanel;
	}

	private JPanel builTimePanel(final String title, final JComboBox hours, final JComboBox minutes) {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(title));

		panel.add(hours);
		panel.add(new JLabel(":"));
		panel.add(minutes);
		return panel;
	}

	private JPanel builDatePanel(final String title, final JComboBox years, final JComboBox months, final JComboBox days, final int startIndex) {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(title));
		updateDaysComboBox(days, years.getSelectedIndex(), months.getSelectedIndex());

		months.addActionListener(this);
		panel.add(months);

		years.addActionListener(this);
		panel.add(years);

		days.addActionListener(this);
		days.setSelectedIndex(startIndex);
		panel.add(days);

		return panel;
	}

	private Object[] getArrayOfIntegers(final int num) {
		return getArrayOfIntegers(num, false, "00", 0);
	}

	private Object[] getArrayOfIntegers(final int num, final boolean desc, final String format, final int shiftLeftBy) {
		final List<String> list = new ArrayList<String>();
		for (int i = 1 - shiftLeftBy; i <= num - shiftLeftBy; i++) {
			final DecimalFormat formatter = new DecimalFormat(format);
			list.add(formatter.format(i));
		}
		if (desc) {
			Collections.reverse(list);
		}
		return list.toArray();
	}

	private int getDaysForMonthYear(final int monthIndex, final int yearIndex) {
		int numOfDays = 31;
		final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		switch (monthIndex) {
		case 3:
		case 5:
		case 8:
		case 10:
			numOfDays = 30;
			break;
		case 1:
			if ((currentYear % 4 + yearIndex) % 4 == 0) {
				numOfDays = 29;
			} else {
				numOfDays = 28;
			}
			break;
		}
		return numOfDays;
	}

	private void updateDaysComboBox(final JComboBox comboBox, final int monthsIndex, final int yearsIndex) {
		final int curDayIndex = comboBox.getSelectedIndex();
		final int daysOfMonth = getDaysForMonthYear(monthsIndex, yearsIndex);
		final int newCurrIndex = curDayIndex + 1 > daysOfMonth ? daysOfMonth - 1 : curDayIndex;

		comboBox.setModel(new DefaultComboBoxModel(getArrayOfIntegers(daysOfMonth)));
		comboBox.setSelectedIndex(newCurrIndex);
	}

	public void actionPerformed(final ActionEvent event) {
		if (event.getSource().equals(comboBoxFromYears) || event.getSource().equals(comboBoxFromMonths)) {
			updateDaysComboBox(comboBoxFromDays, comboBoxFromMonths.getSelectedIndex(), comboBoxFromYears.getSelectedIndex());
		} else if (event.getSource().equals(comboBoxToYears) || event.getSource().equals(comboBoxToMonths)) {
			updateDaysComboBox(comboBoxToDays, comboBoxToMonths.getSelectedIndex(), comboBoxToYears.getSelectedIndex());
		} else if (event.getSource().equals(buttonQuery)) {
			databaseQuery = new DatabaseQuery();
			databaseQuery.setExpirementName(comboBoxSimulationName.getSelectedItem().toString());
			databaseQuery.setCoordinateLatitudeOne(getIntValue(inputCoordinateOneLatitude));
			databaseQuery.setCoordinateLatitudeTwo(getIntValue(inputCoordinateTwoLatitude));
			databaseQuery.setCoordinateLongitudeOne(getIntValue(inputCoordinateOneLongitude));
			databaseQuery.setCoordinateLongitudeTwo(getIntValue(inputCoordinateTwoLongitude));
			databaseQuery.setStartDateTime(getDateFromComboBox(comboBoxFromYears, comboBoxFromMonths, comboBoxFromDays, comboBoxFromHours, comboBoxFromMinutes));
			databaseQuery.setEndDateTime(getDateFromComboBox(comboBoxToYears, comboBoxToMonths, comboBoxToDays, comboBoxToHours, comboBoxToMinutes));
			System.out.println("test");
			resultsPanel.updateExpirement(dao.get(databaseQuery));
		}
	}

	private Date getDateFromComboBox(final JComboBox comboBoxYears, final JComboBox comboBoxMonths, final JComboBox comboBoxDays, final JComboBox comboBoxHours, final JComboBox comboBoxMinutes) {
		final Calendar instance = Calendar.getInstance();
		instance.set(getIntValue(comboBoxFromYears), getIntValue(comboBoxFromMonths) - 1, getIntValue(comboBoxFromDays), getIntValue(comboBoxFromHours), getIntValue(comboBoxFromMinutes), 0);
		return instance.getTime();
	}
}
