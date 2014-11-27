package views;

import static controllers.SimulationController.simulateIteration;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.LINE_START;
import static java.awt.GridBagConstraints.PAGE_START;
import static java.lang.Double.parseDouble;
import static util.GuiUtil.buildConstraints;
import static util.GuiUtil.buildNumberFormatter;
import static util.GuiUtil.getIntValue;
import static util.SimulationUtil.calculateSimulationStats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import models.CommandLineParam;
import models.DatabaseQuery;
import models.Experiment;
import models.GridPoint;
import models.PhysicalFactors;
import models.SimulationSettings;
import models.SimulationStats;
import util.DatabaseQueryBuilder;
import constants.SimulationConstants;
import controllers.SimulationController;
import cs6310.gui.widget.earth.EarthPanel;
import dao.DatabaseDao;

public class SimulationView extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	private static final DecimalFormat DEC_FMT = new DecimalFormat("#.##");

	// Buttons
	private final JButton buttonStart = new JButton("Start");
	private final JButton buttonPause = new JButton("Pause");
	private final JButton buttonStop = new JButton("Stop");
	private final JButton[] simulationControls = { buttonStart, buttonPause, buttonStop };

	// Input Fields
	private JFormattedTextField inputSimulationName;
	private JFormattedTextField inputAxialTilt;
	private JFormattedTextField inputOrbitalEccentricity;
	private JFormattedTextField inputTimeStep;
	private JFormattedTextField inputSimulationLength;
	private JFormattedTextField inputGridSpacing;
	private JFormattedTextField inputDisplayRate;
	private JCheckBox inputShowAnimation;

	// Informational Labels
	private final JLabel labelDateTime = new JLabel();
	private final JLabel labelMinTemp = new JLabel();
	private final JLabel labelMaxTemp = new JLabel();
	private final JLabel labelStdDeviation = new JLabel();
	private final JLabel labelMean = new JLabel();
	private final JLabel labelOrbitalPosition = new JLabel();
	private final JLabel labelRotationalPosition = new JLabel();

	private final CommandLineParam params;
	private final DatabaseDao dao;

	private EarthPanel earthPanel;
	private Timer timer;
	private Experiment experiment;
	private boolean showAnimation = true;
	private int minutesTimePassed = 0;

	private HashSet<GridPoint> newGridPoints;

	public SimulationView(final CommandLineParam params, final DatabaseDao dao) {
		this.params = params;
		this.dao = dao;

		setLayout(new BorderLayout());
		this.add(buildSimulationSettingsAndInfoPanel(), BorderLayout.WEST);
		this.add(buildSimulationControlsPanel(), BorderLayout.SOUTH);
	}

	private JPanel buildSimulationSettingsAndInfoPanel() {
		final JPanel settingsAndInfoPanel = new JPanel(new GridBagLayout());
		settingsAndInfoPanel.setPreferredSize(new Dimension(300, 500));
		settingsAndInfoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		final GridBagConstraints constraints = buildConstraints(PAGE_START, 0, new Insets(10, 0, 0, 0));
		settingsAndInfoPanel.add(buildSimulationSettingsPanel(), constraints);
		constraints.weighty = 1;
		settingsAndInfoPanel.add(buildSimulationInformationPanel(), constraints);
		return settingsAndInfoPanel;
	}

	/**
	 * Builds the user input panel for the various settings.
	 * 
	 * @return
	 */
	private JPanel buildSimulationSettingsPanel() {
		final JPanel settingsPanel = new JPanel(new GridBagLayout());

		// define the constraint objects
		final GridBagConstraints labelConstraints = buildConstraints(CENTER, 0, new Insets(10, 0, 0, 0));
		final GridBagConstraints valueConstraints = buildConstraints(LINE_START, 1, new Insets(10, 10, 0, 0));

		labelConstraints.gridwidth = 2;
		settingsPanel.add(new JLabel("Simulation Settings"), labelConstraints);

		labelConstraints.gridwidth = 1;
		labelConstraints.anchor = GridBagConstraints.LINE_END;

		// Simulation Name
		final JLabel labelSimulationName = new JLabel("Simulation Name:");
		settingsPanel.add(labelSimulationName, labelConstraints);
		inputSimulationName = new JFormattedTextField();
		inputSimulationName.setColumns(5);
		settingsPanel.add(inputSimulationName, valueConstraints);

		// Axial Tilt
		final JLabel labelAxialTilt = new JLabel("Axial Tilt(degrees):");
		settingsPanel.add(labelAxialTilt, labelConstraints);
		inputAxialTilt = new JFormattedTextField(buildNumberFormatter(-180.00, 180.00, 2));
		inputAxialTilt.setColumns(5);
		settingsPanel.add(inputAxialTilt, valueConstraints);

		// Orbital Eccentricity
		final JLabel labelOrbitalEccentricity = new JLabel("Orbital Eccentricity:");
		settingsPanel.add(labelOrbitalEccentricity, labelConstraints);
		inputOrbitalEccentricity = new JFormattedTextField(buildNumberFormatter(0.00000, 1.00000, 4));
		inputOrbitalEccentricity.setColumns(5);
		settingsPanel.add(inputOrbitalEccentricity, valueConstraints);

		// Time Step
		final JLabel labelTimeStep = new JLabel("Time Step(min):");
		settingsPanel.add(labelTimeStep, labelConstraints);
		inputTimeStep = new JFormattedTextField(buildNumberFormatter(1, 525600));
		inputTimeStep.setColumns(5);
		settingsPanel.add(inputTimeStep, valueConstraints);

		// Grid Spacing
		settingsPanel.add(new JLabel("Grid Spacing(degrees):"), labelConstraints);
		inputGridSpacing = new JFormattedTextField(buildNumberFormatter(1, 180));
		inputGridSpacing.setColumns(5);
		settingsPanel.add(inputGridSpacing, valueConstraints);

		// Simulation Length
		settingsPanel.add(new JLabel("Simulation Length(months):"), labelConstraints);
		inputSimulationLength = new JFormattedTextField(buildNumberFormatter(1, 120));
		inputSimulationLength.setColumns(5);
		settingsPanel.add(inputSimulationLength, valueConstraints);

		// Display Rate
		settingsPanel.add(new JLabel("Display Rate(ms):"), labelConstraints);
		// inputDisplayRate = new JFormattedTextField(buildNumberFormatter(1,
		// 120));
		inputDisplayRate = new JFormattedTextField(buildNumberFormatter(1, 5000));
		inputDisplayRate.setColumns(5);
		settingsPanel.add(inputDisplayRate, valueConstraints);

		settingsPanel.add(new JLabel("Show Animation:"), labelConstraints);
		inputShowAnimation = new JCheckBox();
		inputShowAnimation.setSelected(true);
		inputShowAnimation.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				showAnimation = inputShowAnimation.isSelected();
			}
		});
		settingsPanel.add(inputShowAnimation, valueConstraints);

		setDefaultControlValues();
		return settingsPanel;
	}

	private JPanel buildSimulationInformationPanel() {
		final JPanel informationPanel = new JPanel(new GridBagLayout());

		// Define GUI Constraints
		final GridBagConstraints labelConstraints = buildConstraints(CENTER, 0, new Insets(10, 0, 0, 0));
		final GridBagConstraints valueConstraints = buildConstraints(LINE_START, 1, new Insets(10, 10, 0, 0));

		labelConstraints.gridwidth = 2;
		informationPanel.add(new JLabel("Simulation Information"), labelConstraints);

		labelConstraints.gridwidth = 1;
		labelConstraints.anchor = GridBagConstraints.LINE_START;

		informationPanel.add(new JLabel("Date Time:"), labelConstraints);
		informationPanel.add(labelDateTime, valueConstraints);

		informationPanel.add(new JLabel("Orbital Position:"), labelConstraints);
		informationPanel.add(labelOrbitalPosition, valueConstraints);

		informationPanel.add(new JLabel("Rotational Position:"), labelConstraints);
		informationPanel.add(labelRotationalPosition, valueConstraints);

		informationPanel.add(new JLabel("Data Precision:"), labelConstraints);
		informationPanel.add(new JLabel(Integer.toString(params.getDataPrecision())), valueConstraints);

		informationPanel.add(new JLabel("Geo Precision:"), labelConstraints);
		informationPanel.add(new JLabel(Integer.toString(params.getGeographicPrecision())), valueConstraints);

		informationPanel.add(new JLabel("Temporal Precision:"), labelConstraints);
		informationPanel.add(new JLabel(Integer.toString(params.getTemporalPrecision())), valueConstraints);

		informationPanel.add(new JLabel("Min:"), labelConstraints);
		informationPanel.add(labelMinTemp, valueConstraints);

		informationPanel.add(new JLabel("Max:"), labelConstraints);
		informationPanel.add(labelMaxTemp, valueConstraints);

		informationPanel.add(new JLabel("Mean:"), labelConstraints);
		informationPanel.add(labelMean, valueConstraints);

		informationPanel.add(new JLabel("Std Deviation:"), labelConstraints);
		informationPanel.add(labelStdDeviation, valueConstraints);

		setDefaultControlValues();
		return informationPanel;
	}

	private JPanel buildSimulationControlsPanel() {
		final JPanel controlsPanel = new JPanel();
		controlsPanel.setBackground(Color.DARK_GRAY);
		setDefaultButtonsEnabledStatus();

		// Adds the control buttons to the panel
		for (final JButton controlButton : simulationControls) {
			controlButton.addActionListener(this);
			controlsPanel.add(controlButton);
		}

		return controlsPanel;
	}

	private void setDefaultControlValues() {
		inputSimulationName.setText("");
		inputAxialTilt.setText(Double.toString(SimulationConstants.DEFAULT_AXIAL_TILT));
		inputOrbitalEccentricity.setText(Double.toString(SimulationConstants.DEFAULT_ORBITAL_ECCENTRICITY));
		inputSimulationLength.setText(Integer.toString(SimulationConstants.DEFAULT_SIM_LENGTH));
		inputTimeStep.setText(Integer.toString(SimulationConstants.DEFAULT_TIME_STEP));
		inputGridSpacing.setText(Integer.toString(SimulationConstants.DEFAULT_GRID_SPACING));
		inputDisplayRate.setText(Integer.toString(500));

	}

	private void setDefaultButtonsEnabledStatus() {
		buttonStart.setEnabled(true);
		buttonStart.setText("Start");
		buttonPause.setEnabled(false);
		buttonStop.setEnabled(false);
	}

	private void disableInputs() {
		inputSimulationName.setEditable(false);
		inputAxialTilt.setEditable(false);
		inputOrbitalEccentricity.setEditable(false);
		inputTimeStep.setEditable(false);
		inputSimulationLength.setEditable(false);
		inputGridSpacing.setEditable(false);
		inputDisplayRate.setEditable(false);
	}

	private void enableInputs() {
		inputSimulationName.setEditable(true);
		inputAxialTilt.setEditable(true);
		inputOrbitalEccentricity.setEditable(true);
		inputTimeStep.setEditable(true);
		inputSimulationLength.setEditable(true);
		inputGridSpacing.setEditable(true);
		inputDisplayRate.setEditable(true);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();

		if (source == buttonStart) {
			if (inputSimulationName.getText().length() > 0 && 180 % getIntValue(inputGridSpacing) == 0) {
				disableInputs();
				buttonStart.setEnabled(false);
				buttonPause.setEnabled(true);
				buttonStop.setEnabled(true);

				final Calendar endDate = Calendar.getInstance();
				endDate.setTime(SimulationConstants.DEFAULT_START_DATE.getTime());
				endDate.add(Calendar.MINUTE, getSimulationSettings().getSimulationLength() * SimulationConstants.MINUTES_IN_YEAR / 12);

				final DatabaseQuery query = new DatabaseQueryBuilder().experiment(new Experiment(params, getSimulationSettings(), getPhysicalFactors())).startDateTime(SimulationConstants.DEFAULT_START_DATE.getTime()).endDateTime(endDate.getTime()).coordinateLatitudeOne(-90).coordinateLatitudeTwo(90).coordinateLongitudeOne(-180).coordinateLongitudeTwo(180).build();
				final List<Experiment> list = dao.get(query);
				System.out.println(list);
				if (list.isEmpty()) {
					if ("Start".equals(buttonStart.getText())) {
						experiment = SimulationController.initializeGridPoints(params, getSimulationSettings(), getPhysicalFactors(), SimulationConstants.DEFAULT_START_DATE);
						newGridPoints = new HashSet<GridPoint>();
						for (final GridPoint point : experiment.getGridPoints()) {
							try {
								newGridPoints.add(point.clone());
							} catch (final CloneNotSupportedException e) {
								e.printStackTrace();
							}
						}
						if (showAnimation) {
							earthPanel = new EarthPanel(getIntValue(inputGridSpacing));
							earthPanel.updateGrid(experiment.getGridPoints(), SimulationController.getSunLat(), SimulationController.getSunLong());
							this.add(earthPanel, BorderLayout.CENTER);
						}
					}
					start(experiment, getIntValue(inputDisplayRate));
				}
				buttonStart.setText("Resume");
			} else {
				System.err.println("Please enter valid inputs.");
			}
		} else if (source == buttonPause) {
			buttonStart.setEnabled(true);
			buttonPause.setEnabled(false);
			buttonStop.setEnabled(true);
			stop();
		} else if (source == buttonStop) {
			enableInputs();
			experiment.setGridPoints(newGridPoints);
			dao.saveOrUpdate(experiment);
			buttonStart.setText("Start");
			setDefaultControlValues();
			setDefaultButtonsEnabledStatus();
			minutesTimePassed = 0;
			stop();
		}
	}

	public void start(final Experiment experiment, final int displayRate) {
		stop();
		timer = new Timer(displayRate, new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(final ActionEvent event) {
				if (minutesTimePassed >= experiment.getSimulationSettings().getSimulationLength() * SimulationConstants.MINUTES_IN_YEAR / 12) {
					dao.saveOrUpdate(experiment);
					setDefaultButtonsEnabledStatus();
					stop();
				} else {
					minutesTimePassed += experiment.getSimulationSettings().getTimeStep();
					final Set<GridPoint> gridPoints = simulateIteration(experiment, minutesTimePassed);
					newGridPoints.addAll((Set<GridPoint>) ((HashSet<GridPoint>) gridPoints).clone());
					experiment.setGridPoints((HashSet<GridPoint>) newGridPoints.clone());

					final SimulationStats stats = calculateSimulationStats(gridPoints, experiment);

					labelDateTime.setText(DF.format(new Date(getSimulationDate(minutesTimePassed).getTime())));
					labelMaxTemp.setText(DEC_FMT.format(stats.getMax()));
					labelMinTemp.setText(DEC_FMT.format(stats.getMin()));
					labelStdDeviation.setText(DEC_FMT.format(stats.getStandardDeviation()));
					labelMean.setText(DEC_FMT.format(stats.getMean()));
					labelOrbitalPosition.setText(DEC_FMT.format(SimulationController.getOrbitalPos()));
					labelRotationalPosition.setText(DEC_FMT.format(minutesTimePassed % 1440 / 1440 * 360));

					if (showAnimation) {
						earthPanel.updateGrid(gridPoints, stats.getMin(), stats.getMax(), SimulationController.getSunLat(), SimulationController.getSunLong());
					}

				}
			}
		});
		timer.setInitialDelay(0);
		timer.start();
	}

	public void stop() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}

	private Date getSimulationDate(final int i) {
		final Calendar cal = (Calendar) SimulationConstants.DEFAULT_START_DATE.clone();
		cal.add(Calendar.MINUTE, i);
		return cal.getTime();
	}

	private PhysicalFactors getPhysicalFactors() {
		final PhysicalFactors factors = new PhysicalFactors();
		factors.setAxialTilt(parseDouble(inputAxialTilt.getText()));
		factors.setOrbitalEccentricity(parseDouble(inputOrbitalEccentricity.getText()));
		return factors;
	}

	private SimulationSettings getSimulationSettings() {
		final SimulationSettings settings = new SimulationSettings(inputSimulationName.getText());
		settings.setGridSpacing(getIntValue(inputGridSpacing));
		settings.setTimeStep(getIntValue(inputTimeStep));
		settings.setSimulationLength(getIntValue(inputSimulationLength));
		return settings;
	}

}
