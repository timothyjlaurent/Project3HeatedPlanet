package views;

import static javax.swing.BorderFactory.createTitledBorder;
import static util.GuiUtil.buildConstraints;
import static util.GuiUtil.buildNumberFormatter;
import static util.GuiUtil.getIntValue;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

import models.Experiment;
import util.DatabaseQueryBuilder;
import constants.SimulationConstants;
import dao.DatabaseDao;

public class QueryInterfaceView extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private GridBagConstraints labelConstraints;
	private GridBagConstraints valueConstraints;

	private List<Experiment> experiments = new ArrayList<Experiment>();
	private JComboBox comboBoxExperiments;

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
	private final JFormattedTextField inputCoordinateOneLongitude = new JFormattedTextField(buildNumberFormatter(-90, 90));;
	private final JFormattedTextField inputCoordinateTwoLatitude = new JFormattedTextField(buildNumberFormatter(-180, 180));;
	private final JFormattedTextField inputCoordinateTwoLongitude = new JFormattedTextField(buildNumberFormatter(-90, 90));;

	private JLabel labelGridSpacingVal;
	private JLabel labelAxialTiltVal;
	private JLabel labelOrbitalEccentricityVal;
	private JLabel labelTimeStepVal;

	private JButton buttonQuery;

	private final QueryResultsView resultsPanel = new QueryResultsView();

	private final DatabaseDao dao;

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
		final JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(createTitledBorder("Query:"));

		final GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.VERTICAL;
		constraint.weighty = 1.0;
		constraint.gridy = 0;

		panel.add(newSimulationPanel(), constraint);
		panel.add(newLatLongPanel(), constraint);
		panel.add(newFromDateAndTimePanel(), constraint);
		panel.add(newToDateAndTimePanel(), constraint);

		return panel;
	}

	private JPanel newSimulationPanel() {
		final JPanel simulationPanel = new JPanel(new GridBagLayout());
		labelConstraints = buildConstraints(GridBagConstraints.CENTER, 0, new Insets(10, 0, 0, 0));
		valueConstraints = buildConstraints(GridBagConstraints.CENTER, 1, new Insets(10, 10, 0, 0));
		simulationPanel.setBorder(BorderFactory.createTitledBorder("Simulation Factors"));

		simulationPanel.add(new JLabel("Simulation :"), labelConstraints);
		comboBoxExperiments = new JComboBox(new DefaultComboBoxModel(dao.getAllExperiments().toArray()));
		comboBoxExperiments.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				updateExperimentSelection();
			}

		});

		simulationPanel.add(comboBoxExperiments, valueConstraints);

		final JLabel labelAxialTilt = new JLabel("Axial Tilt:");
		simulationPanel.add(labelAxialTilt, labelConstraints);
		labelAxialTiltVal = new JLabel();

		simulationPanel.add(labelAxialTiltVal, valueConstraints);

		// Orbital Eccentricity
		final JLabel labelOrbitalEccentricity = new JLabel("Orbital Eccentricity:");
		simulationPanel.add(labelOrbitalEccentricity, labelConstraints);
		labelOrbitalEccentricityVal = new JLabel();
		simulationPanel.add(labelOrbitalEccentricityVal, valueConstraints);

		// Time Step
		final JLabel labelTimeStep = new JLabel("Time Step:");
		simulationPanel.add(labelTimeStep, labelConstraints);
		labelTimeStepVal = new JLabel();
		simulationPanel.add(labelTimeStepVal, valueConstraints);

		// Grid Spacing
		simulationPanel.add(new JLabel("Grid Spacing:"), labelConstraints);
		labelGridSpacingVal = new JLabel();
		simulationPanel.add(labelGridSpacingVal, valueConstraints);

		setDefaultControlValues();
		return simulationPanel;
	}

	private void setDefaultControlValues() {
		labelAxialTiltVal.setText(Double.toString(SimulationConstants.DEFAULT_AXIAL_TILT));
		labelOrbitalEccentricityVal.setText(Double.toString(SimulationConstants.DEFAULT_ORBITAL_ECCENTRICITY));
		labelTimeStepVal.setText(Integer.toString(SimulationConstants.DEFAULT_TIME_STEP));
		labelGridSpacingVal.setText(Integer.toString(SimulationConstants.DEFAULT_GRID_SPACING));
	}

	private JPanel newLatLongPanel() {
		final JPanel latLongPanel = new JPanel();
		latLongPanel.setLayout(new BoxLayout(latLongPanel, BoxLayout.Y_AXIS));
		latLongPanel.setBorder(createTitledBorder("Latitudes/Longitudes"));

		latLongPanel.add(buildCoordinatePanel("Coordinate One", inputCoordinateOneLatitude, inputCoordinateOneLongitude, -1));
		latLongPanel.add(buildCoordinatePanel("Coordinate Two", inputCoordinateTwoLatitude, inputCoordinateTwoLongitude, 1));

		return latLongPanel;
	}

	private JPanel buildCoordinatePanel(final String title, final JFormattedTextField inputCoordinateLatitude, final JFormattedTextField inputCoordinateLongitude, final int defaultValue) {
		final JPanel coordinatePanel = new JPanel();
		coordinatePanel.setBorder(BorderFactory.createTitledBorder(title));
		labelConstraints = buildConstraints(GridBagConstraints.NORTHWEST, 0, new Insets(10, 10, 0, 0));
		valueConstraints = buildConstraints(GridBagConstraints.NORTHWEST, 1, new Insets(10, 10, 0, 0));

		addCoordinateLabelAndInput(coordinatePanel, inputCoordinateLatitude, "Latitude: ", defaultValue * 180);
		addCoordinateLabelAndInput(coordinatePanel, inputCoordinateLongitude, "Longitude: ", defaultValue * 90);

		return coordinatePanel;
	}

	private void addCoordinateLabelAndInput(final JPanel coordinatePanel, final JFormattedTextField input, final String title, final int defaultValue) {
		coordinatePanel.add(new JLabel(title), labelConstraints);
		input.setColumns(5);
		input.setText(Integer.toString(defaultValue));
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
		panel.setBorder(createTitledBorder(title));

		panel.add(hours);
		panel.add(new JLabel(":"));
		panel.add(minutes);
		return panel;
	}

	private JPanel builDatePanel(final String title, final JComboBox years, final JComboBox months, final JComboBox days, final int startIndex) {
		final JPanel panel = new JPanel();
		panel.setBorder(createTitledBorder(title));
		updateDaysComboBox(days, years.getSelectedIndex(), months.getSelectedIndex());

		months.addActionListener(this);
		panel.add(months);

		days.addActionListener(this);
		days.setSelectedIndex(startIndex);
		panel.add(days);

		years.addActionListener(this);
		years.setSelectedItem("2000");
		panel.add(years);

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

	@Override
	public void actionPerformed(final ActionEvent event) {
		if (event.getSource().equals(comboBoxFromYears) || event.getSource().equals(comboBoxFromMonths)) {
			updateDaysComboBox(comboBoxFromDays, comboBoxFromMonths.getSelectedIndex(), comboBoxFromYears.getSelectedIndex());
		} else if (event.getSource().equals(comboBoxToYears) || event.getSource().equals(comboBoxToMonths)) {
			updateDaysComboBox(comboBoxToDays, comboBoxToMonths.getSelectedIndex(), comboBoxToYears.getSelectedIndex());
		} else if (event.getSource().equals(buttonQuery)) {
			final DatabaseQueryBuilder builder = new DatabaseQueryBuilder()
					.experimentId(((Experiment) comboBoxExperiments.getSelectedItem()).getExperimentId())
					.coordinateLatitudeOne(getIntValue(inputCoordinateOneLatitude))
					.coordinateLatitudeTwo(getIntValue(inputCoordinateTwoLatitude))
					.coordinateLongitudeOne(getIntValue(inputCoordinateOneLongitude))
					.coordinateLongitudeTwo(getIntValue(inputCoordinateTwoLongitude))
					.startDateTime(getDateFromComboBox(comboBoxFromYears, comboBoxFromMonths, comboBoxFromDays, comboBoxFromHours, comboBoxFromMinutes))
					.endDateTime(getDateFromComboBox(comboBoxToYears, comboBoxToMonths, comboBoxToDays, comboBoxToHours, comboBoxToMinutes));

			try {
				resultsPanel.updateExpirement(dao.get(builder.build()), builder.build());
			} catch (final CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	private Date getDateFromComboBox(final JComboBox comboBoxYears, final JComboBox comboBoxMonths, final JComboBox comboBoxDays, final JComboBox comboBoxHours, final JComboBox comboBoxMinutes) {
		final Calendar instance = Calendar.getInstance();
		instance.set(getIntValue(comboBoxYears), getIntValue(comboBoxMonths) - 1, getIntValue(comboBoxDays), getIntValue(comboBoxHours), getIntValue(comboBoxMinutes), 0);
		instance.set(Calendar.MILLISECOND, 0);
		return instance.getTime();
	}

	public void updateExperiments() {
		experiments = dao.getAllExperiments();
		comboBoxExperiments.setModel(new DefaultComboBoxModel(experiments.toArray()));
		updateExperimentSelection();
	}

	private void updateExperimentSelection() {
		final Experiment item = (Experiment) comboBoxExperiments.getSelectedItem();
		if (item != null) {
			labelAxialTiltVal.setText(Double.toString(item.getPhysicalFactors().getAxialTilt()));
			labelOrbitalEccentricityVal.setText(Double.toString(item.getPhysicalFactors().getOrbitalEccentricity()));
			labelTimeStepVal.setText(Integer.toString(item.getSimulationSettings().getTimeStep()));
			labelGridSpacingVal.setText(Integer.toString(item.getSimulationSettings().getGridSpacing()));
		}
	}
}
