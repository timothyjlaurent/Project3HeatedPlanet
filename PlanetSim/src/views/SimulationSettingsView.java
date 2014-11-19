package views;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.LINE_START;
import static java.awt.GridBagConstraints.PAGE_START;
import static util.GuiUtil.buildConstraints;
import static util.GuiUtil.buildNumberFormatter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.CommandLineParam;
import constants.SimulationConstants;
import dao.DatabaseDao;

public class SimulationSettingsView extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

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

	// Informational Labels
	private final JLabel labelDateTime = new JLabel();
	private final JLabel labelMinTemp = new JLabel();
	private final JLabel labelMaxTemp = new JLabel();
	private final JLabel labelStdDeviation = new JLabel();

	public SimulationSettingsView(final CommandLineParam params, final DatabaseDao dao) {
		setLayout(new BorderLayout());
		this.add(buildSimulationSettingsAndInfoPanel(), BorderLayout.WEST);
		this.add(buildSimulationControlsPanel(), BorderLayout.SOUTH);
	}

	private JPanel buildSimulationSettingsAndInfoPanel() {
		final JPanel settingsAndInfoPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints constraints = buildConstraints(PAGE_START, 0, new Insets(10, 0, 0, 0));
		settingsAndInfoPanel.add(buildSimulationSettingsPanel(), constraints);
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
		// TODO Build Formatters

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
		final JLabel labelAxialTilt = new JLabel("Axial Tilt:");
		settingsPanel.add(labelAxialTilt, labelConstraints);
		inputAxialTilt = new JFormattedTextField(buildNumberFormatter(1, 1440));
		inputAxialTilt.setColumns(5);
		settingsPanel.add(inputAxialTilt, valueConstraints);

		// Orbital Eccentricity
		final JLabel labelOrbitalEccentricity = new JLabel("Orbital Eccentricity:");
		settingsPanel.add(labelOrbitalEccentricity, labelConstraints);
		inputOrbitalEccentricity = new JFormattedTextField(buildNumberFormatter(100, Integer.MAX_VALUE));
		inputOrbitalEccentricity.setColumns(5);
		settingsPanel.add(inputOrbitalEccentricity, valueConstraints);

		// Time Step
		final JLabel labelTimeStep = new JLabel("Time Step:");
		settingsPanel.add(labelTimeStep, labelConstraints);
		inputTimeStep = new JFormattedTextField(buildNumberFormatter(100, Integer.MAX_VALUE));
		inputTimeStep.setColumns(5);
		settingsPanel.add(inputTimeStep, valueConstraints);

		// Simulation Length
		final JLabel labelSimulationLength = new JLabel("Simulation Length:");
		settingsPanel.add(labelSimulationLength, labelConstraints);
		inputSimulationLength = new JFormattedTextField(buildNumberFormatter(100, Integer.MAX_VALUE));
		inputSimulationLength.setColumns(5);
		settingsPanel.add(inputSimulationLength, valueConstraints);

		setDefaultControlValues();
		return settingsPanel;
	}

	private JPanel buildSimulationInformationPanel() {
		final JPanel informationPanel = new JPanel(new GridBagLayout());

		// Define GUI Constraints
		final GridBagConstraints labelConstraints = buildConstraints(GridBagConstraints.CENTER, 0, new Insets(10, 0, 0, 0));
		final GridBagConstraints valueConstraints = buildConstraints(GridBagConstraints.LINE_START, 1, new Insets(10, 10, 0, 0));

		labelConstraints.gridwidth = 2;
		informationPanel.add(new JLabel("Simulation Information"), labelConstraints);

		labelConstraints.gridwidth = 1;
		labelConstraints.anchor = GridBagConstraints.LINE_START;

		informationPanel.add(new JLabel("Date Time:"), labelConstraints);
		informationPanel.add(labelDateTime, valueConstraints);

		informationPanel.add(new JLabel("Min:"), labelConstraints);
		informationPanel.add(labelMinTemp, valueConstraints);

		informationPanel.add(new JLabel("Max:"), labelConstraints);
		informationPanel.add(labelMaxTemp, valueConstraints);

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
		inputAxialTilt.setText(Double.toString(SimulationConstants.DEFAULT_AXIAL_TILT));
		inputOrbitalEccentricity.setText(Double.toString(SimulationConstants.DEFAULT_ORBITAL_ECCENTRICITY));
		inputSimulationLength.setText(Integer.toString(SimulationConstants.DEFAULT_SIM_LENGTH));
		inputTimeStep.setText(Integer.toString(SimulationConstants.DEFAULT_TIME_STEP));
	}

	private void setDefaultButtonsEnabledStatus() {
		buttonStart.setEnabled(true);
		buttonPause.setEnabled(false);
		buttonStop.setEnabled(false);
	}

	private void disableInputs() {
		inputSimulationName.setEnabled(false);
		inputAxialTilt.setEnabled(false);
		inputOrbitalEccentricity.setEnabled(false);
		inputTimeStep.setEnabled(false);
		inputSimulationLength.setEnabled(false);
	}

	private void enableInputs() {
		inputSimulationName.setEnabled(true);
		inputAxialTilt.setEnabled(true);
		inputOrbitalEccentricity.setEnabled(true);
		inputTimeStep.setEnabled(true);
		inputSimulationLength.setEnabled(true);
	}

	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();

		if (source == buttonStart) {
			disableInputs();

			if ("Start".equals(buttonStart.getText())) {
			}
			buttonStart.setText("Resume");
			buttonStart.setEnabled(false);
			buttonPause.setEnabled(true);
			buttonStop.setEnabled(true);
		} else if (source == buttonPause) {
			buttonStart.setEnabled(true);
			buttonPause.setEnabled(false);
			buttonStop.setEnabled(true);
		} else if (source == buttonStop) {
			enableInputs();
			buttonStart.setText("Start");
			setDefaultControlValues();
			setDefaultButtonsEnabledStatus();
		}
		System.out.println(inputSimulationName);
	}
}
