package EarthSim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import EarthSim.Initiative.Initiative;

public class MainScene extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	private final Initiative initiative;

	// Buttons
	private final JButton buttonStart = new JButton("Start");
	private final JButton buttonPause = new JButton("Pause");
	private final JButton buttonStop = new JButton("Stop");
	private final JButton[] controls = { buttonStart, buttonPause, buttonStop };

	// Input Fields
	private JFormattedTextField inputGridSpacing;
	private JFormattedTextField inputTimeStep;
	private JFormattedTextField inputDisplayRate;

	// Info Fields
	private final JLabel labelRotationalPosition = new JLabel();
	private final JLabel labelCurrrentTimeStep = new JLabel();
	private final JLabel labelTimeStepRate = new JLabel();
	private final JLabel labelGridSpacing = new JLabel();
	private final JLabel labelDisplayRate = new JLabel();

	private GridBagConstraints labelConstraints;
	private GridBagConstraints valueConstraints;

	public MainScene(final Initiative initiative) {
		this.initiative = initiative;

		// Builds the layout
		setLayout(new BorderLayout());
		add(buildSettingsAndInfoPanel(), BorderLayout.WEST);
		add(buildControlsPanel(), BorderLayout.SOUTH);
		// Add the presentation
		initiative.consumer.setLabels(labelCurrrentTimeStep, labelRotationalPosition);
		add(initiative.consumer, BorderLayout.CENTER);
	}

	/**
	 * Builds the Earth model for calculations
	 * 
	 * @return
	 */
	private Earth buildEarth() {
		final int gridSpacing = getIntValue(inputGridSpacing);
		final int timeStep = getIntValue(inputTimeStep);

		return new Earth(gridSpacing, timeStep);
	}

	/**
	 * Panel for holding the information to the left, including information,
	 * settings, and color legend.
	 * 
	 * @return
	 */
	private JPanel buildSettingsAndInfoPanel() {
		final JPanel settingsAndInfoPanel = new JPanel(new GridLayout(3, 1));
		settingsAndInfoPanel.add(buildSettingsPanel());
		settingsAndInfoPanel.add(buildInformationPanel());
		settingsAndInfoPanel.add(buildColorLegendPanel());
		return settingsAndInfoPanel;
	}

	/**
	 * Builds the user input panel for the various settings.
	 * 
	 * @return
	 */
	private JPanel buildSettingsPanel() {
		final JPanel settingsPanel = new JPanel(new GridBagLayout());

		// define the constraint objects
		labelConstraints = buildConstraints(GridBagConstraints.CENTER, 0, new Insets(10, 0, 0, 0));
		valueConstraints = buildConstraints(GridBagConstraints.LINE_START, 1, new Insets(10, 10, 0, 0));

		labelConstraints.gridwidth = 2;
		settingsPanel.add(new JLabel("Simulation Settings"), labelConstraints);

		labelConstraints.gridwidth = 1;
		labelConstraints.anchor = GridBagConstraints.LINE_END;

		// Grid Spacing
		final JLabel gridSpacingLabel = new JLabel("Grid Spacing:");
		settingsPanel.add(gridSpacingLabel, labelConstraints);
		inputGridSpacing = new JFormattedTextField(buildNumberFormatter(1, 180));
		inputGridSpacing.setColumns(5);
		inputGridSpacing.addPropertyChangeListener("value", this);
		settingsPanel.add(inputGridSpacing, valueConstraints);

		// Time Step
		final JLabel timeStepLabel = new JLabel("Time Step:");
		settingsPanel.add(timeStepLabel, labelConstraints);
		inputTimeStep = new JFormattedTextField(buildNumberFormatter(1, 1440));
		inputTimeStep.setColumns(5);
		settingsPanel.add(inputTimeStep, valueConstraints);

		// Display Rate
		final JLabel displayRateLabel = new JLabel("Display Rate:");
		settingsPanel.add(displayRateLabel, labelConstraints);
		inputDisplayRate = new JFormattedTextField(buildNumberFormatter(100, Integer.MAX_VALUE));
		inputDisplayRate.setColumns(5);
		settingsPanel.add(inputDisplayRate, valueConstraints);

		setDefaultControlValues();
		return settingsPanel;
	}

	/**
	 * Builds the information panel for the various outputs.
	 * 
	 * @return
	 */
	private JPanel buildInformationPanel() {
		final JPanel informationPanel = new JPanel(new GridBagLayout());

		// Define GUI Constraints
		labelConstraints = buildConstraints(GridBagConstraints.CENTER, 0, new Insets(10, 0, 0, 0));
		valueConstraints = buildConstraints(GridBagConstraints.LINE_START, 1, new Insets(10, 10, 0, 0));

		labelConstraints.gridwidth = 2;
		informationPanel.add(new JLabel("Simulation Information"), labelConstraints);

		labelConstraints.gridwidth = 1;
		labelConstraints.anchor = GridBagConstraints.LINE_START;

		informationPanel.add(new JLabel("Rotational Position:"), labelConstraints);
		informationPanel.add(labelRotationalPosition, valueConstraints);

		informationPanel.add(new JLabel("Current Time:"), labelConstraints);
		informationPanel.add(labelCurrrentTimeStep, valueConstraints);

		informationPanel.add(new JLabel("Grid Spacing:"), labelConstraints);
		informationPanel.add(labelGridSpacing, valueConstraints);

		informationPanel.add(new JLabel("Time Step Rate:"), labelConstraints);
		informationPanel.add(labelTimeStepRate, valueConstraints);

		informationPanel.add(new JLabel("Display Rate:"), labelConstraints);
		informationPanel.add(labelDisplayRate, valueConstraints);

		setDefaultControlValues();
		return informationPanel;
	}

	// Builds the color legend
	private JPanel buildColorLegendPanel() {
		final JPanel colorLegendPanel = new JPanel(new GridBagLayout());

		labelConstraints = buildConstraints(GridBagConstraints.CENTER, 0, new Insets(10, 0, 0, 0));
		valueConstraints = buildConstraints(GridBagConstraints.LINE_START, 1, new Insets(10, 10, 0, 0));

		// Panel Title
		labelConstraints.gridwidth = 2;
		colorLegendPanel.add(new JLabel("Color Legend", SwingConstants.LEFT), labelConstraints);

		// Set Constraints for Labels
		labelConstraints.gridwidth = 1;
		labelConstraints.anchor = GridBagConstraints.LINE_END;

		colorLegendPanel.add(new JLabel("min Degrees = "), labelConstraints);
		colorLegendPanel.add(createLegendColorLabel(new Color(0, 0, 0)), valueConstraints);

		colorLegendPanel.add(new JLabel("max Degrees = "), labelConstraints);
		colorLegendPanel.add(createLegendColorLabel(new Color(255, 0, 0)), valueConstraints);

		return colorLegendPanel;
	}

	private JLabel createLegendColorLabel(final Color color) {
		final JLabel legend = new JLabel("        ");
		legend.setBackground(color);
		legend.setOpaque(true);
		return legend;
	}

	private JPanel buildControlsPanel() {
		final JPanel controlsPanel = new JPanel();
		controlsPanel.setBackground(Color.DARK_GRAY);
		setDefaultButtonsEnabledStatus();

		// Adds the control buttons to the panel
		for (final JButton controlButton : controls) {
			controlButton.addActionListener(this);
			controlsPanel.add(controlButton);
		}

		return controlsPanel;
	}

	private void setFormInformationLabels() {
		labelGridSpacing.setText(inputGridSpacing.getText());
		labelTimeStepRate.setText(inputTimeStep.getText());
		labelDisplayRate.setText(inputDisplayRate.getText());
	}

	private NumberFormatter buildNumberFormatter(final int min, final int max) {
		final NumberFormatter timeStepFormatter = new NumberFormatter();
		timeStepFormatter.setMinimum(min);
		timeStepFormatter.setMaximum(max);
		return timeStepFormatter;
	}

	private GridBagConstraints buildConstraints(final int anchor, final int gridx, final Insets insets) {
		final GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = anchor;
		labelConstraints.gridx = gridx;
		labelConstraints.insets = insets;
		return labelConstraints;
	}

	private int getIntValue(final JFormattedTextField field) {
		final Number value = (Number) field.getValue();
		if (value != null) {
			return value.intValue();
		} else {
			return 0;
		}
	}

	private void setDefaultControlValues() {
		inputGridSpacing.setValue(EarthConstants.DEFAULT_GRID_SPACING);
		inputTimeStep.setValue(EarthConstants.DEFAULT_TIME_STEP);
		inputDisplayRate.setValue(100);
	}

	private void setDefaultButtonsEnabledStatus() {
		buttonStart.setEnabled(true);
		buttonPause.setEnabled(false);
		buttonStop.setEnabled(false);
	}

	private void disableInputs() {
		inputDisplayRate.setEditable(false);
		inputGridSpacing.setEditable(false);
		inputTimeStep.setEditable(false);
	}

	private void enableInputs() {
		inputDisplayRate.setEditable(true);
		inputGridSpacing.setEditable(true);
		inputTimeStep.setEditable(true);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();
		if (source == buttonStart) {
			if ("Start".equals(buttonStart.getText())) {
				initiative.write(buildEarth());
				setFormInformationLabels();
			}
			disableInputs();
			buttonStart.setText("Resume");
			buttonStart.setEnabled(false);
			buttonPause.setEnabled(true);
			buttonStop.setEnabled(true);

			initiative.start(getIntValue(inputDisplayRate));
		} else if (source == buttonPause) {
			buttonStart.setEnabled(true);
			buttonPause.setEnabled(false);
			buttonStop.setEnabled(true);
			initiative.stop();
		} else if (source == buttonStop) {
			enableInputs();
			buttonStart.setText("Start");
			setDefaultControlValues();
			setDefaultButtonsEnabledStatus();
			initiative.reset();
		}
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		final Object source = event.getSource();
		if (source == inputGridSpacing) {
			// ensure the input grid spacing parameter evenly divides into 180
			int value = ((Number) inputGridSpacing.getValue()).intValue();
			while (180 % value != 0) {
				value--;
			}
			inputGridSpacing.setValue(value);
		}

	}
}
