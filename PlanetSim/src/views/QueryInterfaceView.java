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

import models.CommandLineParam;
import models.Experiment;
import util.DatabaseQueryBuilder;
import constants.SimulationConstants;
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
	private final JFormattedTextField inputCoordinateOneLongitude = new JFormattedTextField(buildNumberFormatter(-90, 90));;
	private final JFormattedTextField inputCoordinateTwoLatitude = new JFormattedTextField(buildNumberFormatter(-180, 180));;
	private final JFormattedTextField inputCoordinateTwoLongitude = new JFormattedTextField(buildNumberFormatter(-90, 90));;
	private JFormattedTextField inputGridSpacing;
	private JFormattedTextField inputAxialTilt;
	private JFormattedTextField inputOrbitalEccentricity;
	private JFormattedTextField inputTimeStep;

	private List<Experiment> experiments = new ArrayList<Experiment>();

	private JButton buttonQuery;

	private final DatabaseDao dao;
	private final QueryResultsView resultsPanel = new QueryResultsView();
	private final CommandLineParam params;
	private JComboBox comboBoxSimulationId;

	public QueryInterfaceView(final CommandLineParam params, final DatabaseDao dao) {
		this.dao = dao;
		this.params = params;
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

		simulationPanel.add(new JLabel("Simulation Id:"), labelConstraints);

		comboBoxSimulationId = new JComboBox();
		// comboBoxSimulationId.addItemListener(new ItemListener() {
		//
		// @Override
		// public void itemStateChanged(final ItemEvent e) {
		// dao.get(new DataBaseQuery)
		// }
		//
		// });
		simulationPanel.add(comboBoxSimulationId, valueConstraints);
		// Simulation Name
		simulationPanel.add(new JLabel("Simulation Name:"), labelConstraints);
		comboBoxSimulationName = new JComboBox(new DefaultComboBoxModel(dao.getExperimentNames().toArray()));

		simulationPanel.add(comboBoxSimulationName, valueConstraints);

		final JLabel labelAxialTilt = new JLabel("Axial Tilt:");
		simulationPanel.add(labelAxialTilt, labelConstraints);
		inputAxialTilt = new JFormattedTextField(buildNumberFormatter(-180.00, 180.00, 2));
		inputAxialTilt.setColumns(5);
		simulationPanel.add(inputAxialTilt, valueConstraints);

		// Orbital Eccentricity
		final JLabel labelOrbitalEccentricity = new JLabel("Orbital Eccentricity:");
		simulationPanel.add(labelOrbitalEccentricity, labelConstraints);
		inputOrbitalEccentricity = new JFormattedTextField(buildNumberFormatter(0.00000, 1.00000, 4));
		inputOrbitalEccentricity.setColumns(5);
		simulationPanel.add(inputOrbitalEccentricity, valueConstraints);

		// Time Step
		final JLabel labelTimeStep = new JLabel("Time Step:");
		simulationPanel.add(labelTimeStep, labelConstraints);
		inputTimeStep = new JFormattedTextField(buildNumberFormatter(1, 525600));
		inputTimeStep.setColumns(5);
		simulationPanel.add(inputTimeStep, valueConstraints);

		// Grid Spacing
		simulationPanel.add(new JLabel("Grid Spacing:"), labelConstraints);
		inputGridSpacing = new JFormattedTextField(buildNumberFormatter(1, 180));
		inputGridSpacing.setColumns(5);
		simulationPanel.add(inputGridSpacing, valueConstraints);

		setDefaultControlValues();
		return simulationPanel;
	}

	private void setDefaultControlValues() {
		inputAxialTilt.setText(Double.toString(SimulationConstants.DEFAULT_AXIAL_TILT));
		inputOrbitalEccentricity.setText(Double.toString(SimulationConstants.DEFAULT_ORBITAL_ECCENTRICITY));
		inputTimeStep.setText(Integer.toString(SimulationConstants.DEFAULT_TIME_STEP));
		inputGridSpacing.setText(Integer.toString(SimulationConstants.DEFAULT_GRID_SPACING));
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
					.experimentName(comboBoxSimulationName.getSelectedItem() != null ? comboBoxSimulationName.getSelectedItem().toString() : "")
					.coordinateLatitudeOne(getIntValue(inputCoordinateOneLatitude))
					.coordinateLatitudeTwo(getIntValue(inputCoordinateTwoLatitude))
					.coordinateLongitudeOne(getIntValue(inputCoordinateOneLongitude))
					.coordinateLongitudeTwo(getIntValue(inputCoordinateTwoLongitude))
					.timeStep(getIntValue(inputTimeStep))
					.startDateTime(getDateFromComboBox(comboBoxFromYears, comboBoxFromMonths, comboBoxFromDays, comboBoxFromHours, comboBoxFromMinutes))
					.endDateTime(getDateFromComboBox(comboBoxToYears, comboBoxToMonths, comboBoxToDays, comboBoxToHours, comboBoxToMinutes))
					.axialTilt(Double.parseDouble(inputAxialTilt.getText()))
					.orbitalEccentricity(Double.parseDouble(inputOrbitalEccentricity.getText()))
					.gridSpacing(getIntValue(inputGridSpacing))
					.dataPrecision(params.getDataPrecision())
					.geoPrecision(params.getGeographicPrecision())
					.temporalPrecision(params.getTemporalPrecision());

			try {
				resultsPanel.updateExpirement(dao.get(builder.build()), builder.build());
			} catch (final CloneNotSupportedException e) {
				// TODO Auto-generated catch block
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

		final List<String> experimentList = new ArrayList<String>();
		for (final Experiment exp : experiments) {
			experimentList.add(exp.toStringShort());
		}

		comboBoxSimulationId.setModel(new DefaultComboBoxModel(experimentList.toArray()));
	}
}
