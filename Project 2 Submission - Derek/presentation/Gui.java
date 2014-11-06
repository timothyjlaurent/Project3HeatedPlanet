package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import presentation.earth.*;
import simulation.SimStatus;
import userControl.CommunicationConfig;
import userControl.SimulationOptions;
import userControl.ThreadConfig;

class GuiConfig {
	// Setup some dimensions for our GUI
	static final int DIM_WIDTH_JPG = 800; // Our earth jpeg is 800 pixels wide
	static final int DIM_HEIGHT_JPG = 400; // Our earth jpeg is 400 pixels high
	static final int DIM_WIDTH_OPTIONS = DIM_WIDTH_JPG;
	static final int DIM_HEIGHT_OPTIONS = 220;
	static final int DIM_WIDTH_FRAME = Math.max(DIM_WIDTH_JPG,
			DIM_WIDTH_OPTIONS);
	static final int DIM_HEIGHT_FRAME = DIM_HEIGHT_JPG + DIM_HEIGHT_OPTIONS;

	// Actions
	static final String ACTION_RUN = "Run";
	static final String ACTION_STOP = "Pause";
	static final String ACTION_RESTART = "Reset";
	static final String ACTION_GRID_SPACING_EDIT = "Grid_Spacing";
	static final String ACTION_NUM_EDIT = "Num_Edit";

	// Default Settings
	static final int DEFAULT_GRID_SPACING = 15;
	static final int DEFAULT_SIM_DELAY = 200;

	// Start time
	static final String START_TIME = "12:00 PM, Dec 31, 1999";

	// Time format
	static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"hh:mm a, MMM dd, yyyy");
}

public class Gui extends JFrame implements ActionListener, ChangeListener {

	private static final long serialVersionUID = -6357311467840933836L;

	// Run Options Data
	private JButton runButton = new JButton(GuiConfig.ACTION_RUN);
	private JButton stopButton = new JButton(GuiConfig.ACTION_STOP);
	private JButton restartButton = new JButton(GuiConfig.ACTION_RESTART);

	// Simulation Options Data
	private PosAngleDegField gridSpacingDegEdit = null;
	private JSlider gridSpacingDegSlider = null;

	private PosNumberField simRateEdit = null;
	private JSlider simRateSlider = null;

	private PosNumberField visRateEdit = null;
	private JSlider visRateSlider = null;

	private PosNumberField simDelayEdit = null;
	private JSlider simDelaySlider = null;

	// private PosNumberField visDelayEdit = null;
	// private JSlider visDelaySlider = null;

	private JRadioButton threadUse_Sim = null;
	private JRadioButton threadUse_Pres = null;

	private JRadioButton comConfig_Pull = null;
	private JRadioButton comConfig_Push = null;
	private JRadioButton comConfig_Buff = null;

	private JLabel simTime = null;
	private Calendar simTimeCal = GuiConfig.DATE_FORMAT.getCalendar();

	// Earth Grid Data
	private EarthPanel earthPanel = new EarthPanel(new Dimension(
			GuiConfig.DIM_WIDTH_JPG, GuiConfig.DIM_HEIGHT_JPG), new Dimension(
			GuiConfig.DIM_WIDTH_JPG, GuiConfig.DIM_HEIGHT_JPG), new Dimension(
			GuiConfig.DIM_WIDTH_JPG, GuiConfig.DIM_HEIGHT_JPG));

	private Gui() {
		super("EarthSim");

		// Need to make sure that when the user closes the program that we
		// actually exit
		// the JFrame and go back out to the calling method... this will also
		// help us to
		// actually end the program when the window is gone :)
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Construct the GUI elements
		createGui();

		this.setVisible(true);
	}

	private static Gui instance = null;

	public static Gui getInstance() {
		if (instance == null) {
			instance = new Gui();
		}
		return instance;
	}

	public static void destroy() {
		if (instance != null) {
			instance = null;
		}
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals(GuiConfig.ACTION_RUN)) {
			// Disable the user options
			this.setEnableAllUserOptions(false);

			// Disable the Run button
			runButton.setEnabled(false);

			// Enable the Stop button
			stopButton.setEnabled(true);

			// Disable the restart button
			restartButton.setEnabled(false);

			updateUserControls();

			// Update the user controls for this simulation
			SimulationOptions.getInstance().setRun(true);
			SimulationOptions.getInstance().setResetOnStart(false);
		} else if (command.equals(GuiConfig.ACTION_STOP)) {
			// Enable the Run button
			runButton.setEnabled(true);

			// Disable the Stop button
			stopButton.setEnabled(false);

			// Enable the restart button
			restartButton.setEnabled(true);

			updateUserControls();

			SimulationOptions.getInstance().setRun(false);
		} else if (command.equals(GuiConfig.ACTION_RESTART)) {
			// Users are only allowed to edit the options when the application
			// first starts or when they've restarted the simulation. Therefore,
			// enable the user options
			this.setEnableAllUserOptions(true);

			// Disable the restart button
			restartButton.setEnabled(false);

			// Reset the earth grid
			SimulationOptions.getInstance().setResetOnStart(true);
			earthPanel.reset();

			// Reset the simulation time
			try {
				simTimeCal.setTime(GuiConfig.DATE_FORMAT
						.parse(GuiConfig.START_TIME));
				simTime.setText(GuiConfig.DATE_FORMAT.format(simTimeCal
						.getTime()));
			} catch (ParseException e2) {/* e.printStackTrace(); */
			}
		} else if (command.equals(GuiConfig.ACTION_GRID_SPACING_EDIT)) {
			// redraw the grid during updates of the grid spacing... it looks
			// cool
			earthPanel.drawGrid(gridSpacingDegEdit.getAngle());
		}
	}

	public void updateSimStatus(SimStatus simStatus) {
		if (simStatus != null) {
			// Update the position of the sun
			earthPanel.moveSunPosition((float) (simRateEdit.getValue() % 1440)
					* 360 / 1440
					* SimulationOptions.getInstance().getVisualizationRate());

			// Update the simulation time
			simTimeCal.add(Calendar.MINUTE, simRateEdit.getValue());
			simTime.setText(GuiConfig.DATE_FORMAT.format(simTimeCal.getTime()));

			// Update the temperature grid
			earthPanel.updateGrid(simStatus.getGrid());

			if (simStatus.isFinished()) {
				setEnableAllUserOptions(true);
				runButton.setEnabled(true);
				SimulationOptions.getInstance().setRun(false);
				SimulationOptions.getInstance().setResetOnStart(true);
				stopButton.setEnabled(false);
				restartButton.setEnabled(false);
				try {
					simTimeCal.setTime(GuiConfig.DATE_FORMAT
							.parse(GuiConfig.START_TIME));
				} catch (ParseException e) {/* e.printStackTrace(); */
				}
				updateUserControls();
			}
		}
	}

	public void updateUserControls() {
		// Get an instance of SimOptions
		SimulationOptions opts = SimulationOptions.getInstance();

		// Update the options based on the user controls
		opts.setGridSpacing(gridSpacingDegEdit.getAngle());
		opts.setSimulationRate(simRateEdit.getValue());
		opts.setVisualizationRate(visRateEdit.getValue());

		// Update the delay options
		opts.setSimulationDelayMilliSeconds(simDelayEdit.getValue());
		// opts.setVisualizationDelay_ms(visDelayEdit.getValue());

		// Update the threading configuration options
		if (threadUse_Sim.isSelected() && !threadUse_Pres.isSelected()) {
			opts.setThreadConfig(ThreadConfig.SIMULATIONONLY);
		} else if (!threadUse_Sim.isSelected() && threadUse_Pres.isSelected()) {
			opts.setThreadConfig(ThreadConfig.PRESENTATIONONLY);
		} else if (threadUse_Sim.isSelected() && threadUse_Pres.isSelected()) {
			opts.setThreadConfig(ThreadConfig.SIMULATIONANDPRESENTATION);
		}

		// Update the communication configuration options
		if (comConfig_Pull.isSelected()) {
			opts.setCommConfig(CommunicationConfig.PULL);
		} else if (comConfig_Push.isSelected()) {
			opts.setCommConfig(CommunicationConfig.PUSH);
		} else if (comConfig_Buff.isSelected()) {
			opts.setCommConfig(CommunicationConfig.BUFFER);
		}
	}

	private void setEnableAllUserOptions(boolean bEnable) {
		gridSpacingDegEdit.setEnabled(bEnable);
		gridSpacingDegSlider.setEnabled(bEnable);

		simRateEdit.setEnabled(bEnable);
		simRateSlider.setEnabled(bEnable);

		visRateEdit.setEnabled(bEnable);
		visRateSlider.setEnabled(bEnable);

		simDelayEdit.setEnabled(bEnable);
		simDelaySlider.setEnabled(bEnable);

		// visDelayEdit.setEnabled(bEnable);
		// visDelaySlider.setEnabled(bEnable);

		threadUse_Sim.setEnabled(bEnable);
		threadUse_Pres.setEnabled(bEnable);

		comConfig_Buff.setEnabled(bEnable);
		comConfig_Pull.setEnabled(bEnable);
		comConfig_Push.setEnabled(bEnable);
	}

	private void createGui() {
		// Apply dimensions to our GUI
		this.getContentPane().setPreferredSize(
				new Dimension(GuiConfig.DIM_WIDTH_FRAME,
						GuiConfig.DIM_HEIGHT_FRAME));

		// Initialize our start time
		try {
			simTimeCal.setTime(GuiConfig.DATE_FORMAT
					.parse(GuiConfig.START_TIME));
		} catch (ParseException e) {/* e.printStackTrace(); */
		}

		// Need some room to work, add the options panel
		JPanel optionsPanel;
		optionsPanel = new JPanel();
		optionsPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1,
				Color.blue));
		optionsPanel.setLayout(new java.awt.BorderLayout());
		optionsPanel.add(createOptionsPanel(), BorderLayout.CENTER);
		this.getContentPane().add(optionsPanel, BorderLayout.NORTH);

		// Need some room to view the simulation, add the sim panel
		JPanel simPanel;
		simPanel = new JPanel();
		simPanel.setBackground(Color.gray);
		// earthPanel.setMapOpacity((float)0.5);
		simPanel.add(earthPanel);
		this.getContentPane().add(simPanel, BorderLayout.CENTER);

		earthPanel.drawGrid(GuiConfig.DEFAULT_GRID_SPACING);

		this.pack();
	}

	private JPanel createOptionsPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(GuiConfig.DIM_WIDTH_OPTIONS,
				GuiConfig.DIM_HEIGHT_OPTIONS));
		panel.setLayout(new BorderLayout());

		// Add some new dimensions that only have meaning in this context
		int DIM_WIDTH_OPTIONS_LABELS = GuiConfig.DIM_WIDTH_OPTIONS * 4 / 7 * 1
				/ 4;
		int DIM_WIDTH_OPTIONS_EDITS = GuiConfig.DIM_WIDTH_OPTIONS * 4 / 7 * 3
				/ 4;

		// Get some tools to work with...
		JLabel tmpLabel = null;
		JLabel tmpUnits = null;

		// Setup the configuration options
		// This is a little weird, but we're going to nest our panels. We've got
		// a main panel
		// for the config options and it contains a panel for the labels on the
		// left and a panel
		// for the actual edit boxes and units on the right. We do this so that
		// we can align
		// our edit boxes and make things look professional.
		Border simBorder = BorderFactory
				.createTitledBorder("Configuration Options");
		JPanel configOpts = new JPanel(new BorderLayout());
		JPanel configOptsLabels = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Change
																				// the
																				// vertical
																				// spacing
																				// to
																				// accommodate
																				// for
																				// controls
		JPanel configOptsEdits = new JPanel(new FlowLayout(FlowLayout.LEFT));
		configOptsLabels.setPreferredSize(new Dimension(
				DIM_WIDTH_OPTIONS_LABELS, GuiConfig.DIM_HEIGHT_OPTIONS));
		configOptsEdits.setPreferredSize(new Dimension(DIM_WIDTH_OPTIONS_EDITS,
				GuiConfig.DIM_HEIGHT_OPTIONS));
		configOpts.setBorder(simBorder);
		int EDIT_BOX_WIDTH = 4; // Make all edit boxes the same width... it
								// looks better
		int LABEL_HEIGHT = 26;

		// Grid Spacing
		tmpLabel = new JLabel("Grid Spacing:");
		tmpUnits = new JLabel("degrees");
		tmpLabel.setPreferredSize(new Dimension(DIM_WIDTH_OPTIONS_LABELS,
				LABEL_HEIGHT));
		gridSpacingDegEdit = new PosAngleDegField(EDIT_BOX_WIDTH,
				GuiConfig.DEFAULT_GRID_SPACING);
		gridSpacingDegEdit.setActionCommand(GuiConfig.ACTION_GRID_SPACING_EDIT);
		gridSpacingDegEdit.setEditable(false);
		gridSpacingDegEdit.addActionListener(this);
		gridSpacingDegSlider = new JSlider(JSlider.HORIZONTAL, 1, 180,
				GuiConfig.DEFAULT_GRID_SPACING);
		gridSpacingDegSlider.setMajorTickSpacing(30);
		gridSpacingDegSlider.setMinorTickSpacing(15);
		gridSpacingDegSlider.setPaintTicks(true);
		gridSpacingDegSlider.setPaintTrack(true);
		gridSpacingDegSlider.addChangeListener(gridSpacingDegEdit);
		configOptsLabels.add(tmpLabel);
		configOptsEdits.add(gridSpacingDegSlider);
		configOptsEdits.add(gridSpacingDegEdit);
		configOptsEdits.add(tmpUnits);

		// Simulation Rate
		tmpLabel = new JLabel("Simulation Rate:");
		tmpUnits = new JLabel("min/sim");
		tmpLabel.setPreferredSize(new Dimension(DIM_WIDTH_OPTIONS_LABELS,
				LABEL_HEIGHT));
		simRateEdit = new PosNumberField(EDIT_BOX_WIDTH, 10); // Default
																// simulation
																// rate is 10
																// minutes /
																// step
		simRateEdit.setEditable(false);
		simRateSlider = new JSlider(JSlider.HORIZONTAL, 1, 1440, 10);
		simRateSlider.setMajorTickSpacing(100);
		simRateSlider.setMinorTickSpacing(50);
		simRateSlider.setPaintTicks(true);
		simRateSlider.setPaintTrack(true);
		simRateSlider.addChangeListener(simRateEdit);
		configOptsLabels.add(tmpLabel);
		configOptsEdits.add(simRateSlider);
		configOptsEdits.add(simRateEdit);
		configOptsEdits.add(tmpUnits);

		// Visualization Rate
		tmpLabel = new JLabel("Visualization Rate:");
		tmpUnits = new JLabel("sim/pres");
		tmpLabel.setPreferredSize(new Dimension(DIM_WIDTH_OPTIONS_LABELS,
				LABEL_HEIGHT));
		visRateEdit = new PosNumberField(EDIT_BOX_WIDTH, 1); // Default
																// visualization
																// rate is 1
																// step /
																// presentation
		visRateEdit.setEditable(false);
		visRateSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);
		visRateSlider.setMajorTickSpacing(20);
		visRateSlider.setMinorTickSpacing(10);
		visRateSlider.setPaintTicks(true);
		visRateSlider.setPaintTrack(true);
		visRateSlider.addChangeListener(visRateEdit);
		configOptsLabels.add(tmpLabel);
		configOptsEdits.add(visRateSlider);
		configOptsEdits.add(visRateEdit);
		configOptsEdits.add(tmpUnits);

		// Simulation Delay
		tmpLabel = new JLabel("Simulation Delay:");
		tmpUnits = new JLabel("ms/step");
		tmpLabel.setPreferredSize(new Dimension(DIM_WIDTH_OPTIONS_LABELS,
				LABEL_HEIGHT));
		simDelayEdit = new PosNumberField(EDIT_BOX_WIDTH,
				GuiConfig.DEFAULT_SIM_DELAY);
		simDelayEdit.setEditable(false);
		simDelaySlider = new JSlider(JSlider.HORIZONTAL, 0, 2000,
				GuiConfig.DEFAULT_SIM_DELAY);
		simDelaySlider.setMajorTickSpacing(500);
		simDelaySlider.setMinorTickSpacing(250);
		simDelaySlider.setPaintTicks(true);
		simDelaySlider.setPaintTrack(true);
		simDelaySlider.addChangeListener(simDelayEdit);
		configOptsLabels.add(tmpLabel);
		configOptsEdits.add(simDelaySlider);
		configOptsEdits.add(simDelayEdit);
		configOptsEdits.add(tmpUnits);

		// // Visualization Delay
		// tmpLabel = new JLabel("Visualization Delay:");
		// tmpUnits = new JLabel("ms/pres");
		// tmpLabel.setPreferredSize(new
		// Dimension(DIM_WIDTH_OPTIONS_LABELS,LABEL_HEIGHT));
		// visDelayEdit = new PosNumberField(EDIT_BOX_WIDTH,0); // Default
		// simulation rate is 10 minutes / step
		// visDelayEdit.setEditable(false);
		// visDelaySlider = new JSlider(JSlider.HORIZONTAL,0,2000,0);
		// visDelaySlider.setMajorTickSpacing(500);
		// visDelaySlider.setMinorTickSpacing(250);
		// visDelaySlider.setPaintTicks(true);
		// visDelaySlider.setPaintTrack(true);
		// visDelaySlider.addChangeListener(visDelayEdit);
		// configOptsLabels.add(tmpLabel); configOptsEdits.add(visDelaySlider);
		// configOptsEdits.add(visDelayEdit); configOptsEdits.add(tmpUnits);
		//
		// Threading configuration
		tmpLabel = new JLabel("Thread Config:");
		tmpUnits = new JLabel("         ");
		tmpLabel.setPreferredSize(new Dimension(DIM_WIDTH_OPTIONS_LABELS,
				LABEL_HEIGHT));
		threadUse_Sim = new JRadioButton("Sim Thread [-s]");
		threadUse_Pres = new JRadioButton("Pres Thread [-p]");
		if (SimulationOptions.getInstance().getThreadConfig() == ThreadConfig.SIMULATIONONLY) {
			threadUse_Sim.setSelected(true);
		} else if (SimulationOptions.getInstance().getThreadConfig() == ThreadConfig.PRESENTATIONONLY) {
			threadUse_Pres.setSelected(true);
		} else if (SimulationOptions.getInstance().getThreadConfig() == ThreadConfig.SIMULATIONANDPRESENTATION) {
			threadUse_Sim.setSelected(true);
			threadUse_Pres.setSelected(true);
		}
		configOptsLabels.add(tmpLabel);
		configOptsEdits.add(threadUse_Sim);
		configOptsEdits.add(threadUse_Pres);
		configOptsEdits.add(tmpUnits);

		// Communication configuration
		tmpLabel = new JLabel("Comm Config:");
		tmpUnits = new JLabel("");
		tmpLabel.setPreferredSize(new Dimension(DIM_WIDTH_OPTIONS_LABELS,
				LABEL_HEIGHT));
		comConfig_Push = new JRadioButton("Push [-t]");
		comConfig_Pull = new JRadioButton("Pull [-r]");
		comConfig_Buff = new JRadioButton("Buffer [-b]");
		if (SimulationOptions.getInstance().getCommConfig() == CommunicationConfig.PUSH) {
			comConfig_Push.setSelected(true);
		} else if (SimulationOptions.getInstance().getCommConfig() == CommunicationConfig.PULL) {
			comConfig_Pull.setSelected(true);
		} else if (SimulationOptions.getInstance().getCommConfig() == CommunicationConfig.BUFFER) {
			comConfig_Buff.setSelected(true);
		}
		ButtonGroup group = new ButtonGroup();
		group.add(comConfig_Push);
		group.add(comConfig_Pull);
		group.add(comConfig_Buff);
		configOptsLabels.add(tmpLabel);
		configOptsEdits.add(comConfig_Push);
		configOptsEdits.add(comConfig_Pull);
		configOptsEdits.add(comConfig_Buff);

		// Now add the options to the panel
		configOpts.add(configOptsLabels, BorderLayout.WEST);
		configOpts.add(configOptsEdits, BorderLayout.EAST);
		panel.add(configOpts, BorderLayout.WEST);

		// Setup the run options
		JPanel runAndTimePanel = new JPanel(new BorderLayout());
		Border runBorder = BorderFactory.createTitledBorder("Run Options");
		JPanel runOpts = new JPanel(/* new GridLayout(1,3,10,10) */);
		runOpts.setBorder(runBorder);

		// Run Button
		runButton.setActionCommand(GuiConfig.ACTION_RUN);
		runButton.addActionListener(this);
		runButton.setEnabled(true); // Enabled by default
		runOpts.add(runButton);

		// Stop Button
		stopButton.setActionCommand(GuiConfig.ACTION_STOP);
		stopButton.addActionListener(this);
		stopButton.setEnabled(false); // Disabled by default
		runOpts.add(stopButton);

		// Restart Button
		restartButton.setActionCommand(GuiConfig.ACTION_RESTART);
		restartButton.addActionListener(this);
		restartButton.setEnabled(false); // Disabled by default
		runOpts.add(restartButton);

		// Now add the options to the panel
		runAndTimePanel.add(runOpts, BorderLayout.CENTER);

		// Setup the Simulation Physical Time Counter
		JPanel simTimePanel = new JPanel();
		simTimePanel.setBorder(BorderFactory
				.createTitledBorder("Simulation Time"));
		simTime = new JLabel(GuiConfig.START_TIME);
		simTimePanel.add(simTime);
		runAndTimePanel.add(simTimePanel, BorderLayout.SOUTH);

		panel.add(runAndTimePanel, BorderLayout.CENTER);

		return panel;
	}

	public void stateChanged(ChangeEvent e) {
		// Do nothing
	}

}

/**
 * 
 * posNumberField: positive Number Field
 * 
 * Provides validation on a text field to enforce that values are numbers and
 * that they are positive.
 * 
 */
class PosNumberField extends JTextField implements ChangeListener {

	private static final long serialVersionUID = -8289071562629422132L;

	public PosNumberField(int cols, int initVal) {
		super(cols);
		this.setText(Integer.valueOf(initVal).toString());
	}

	protected Document createDefaultModel() {
		return new posNumberDocument();
	}

	public int getValue() {
		return Integer.valueOf(this.getText()).intValue();
	}

	static class posNumberDocument extends PlainDocument {

		private static final long serialVersionUID = -146498649501160057L;

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str == null) {
				return;
			}

			int num;
			try {
				num = (Integer.valueOf(str)).intValue();

				if (num >= 0) {
					super.insertString(offs, new String(str), a);
				}
			} catch (NumberFormatException e) {
				// do nothings... we don't care about it
			}

		}
	}

	public void stateChanged(ChangeEvent e) {
		Object eventSource = e.getSource();
		if (eventSource.getClass().getName().equals("javax.swing.JSlider")) {
			JSlider slider = (JSlider) eventSource;
			this.setText(Integer.valueOf(slider.getValue()).toString());
		}
	}
}

/**
 * 
 * posAngleDegField: positive Angle Degrees Field
 * 
 * Provides validation on a text field to enforce that values are angles
 * ([0,360) degrees) and that they are positive.
 * 
 */
class PosAngleDegField extends JTextField implements ChangeListener {

	private static final long serialVersionUID = 5483716504513288625L;

	public PosAngleDegField(int cols, int initVal) {
		super(cols);
		this.setText(Integer.valueOf(initVal).toString());
	}

	protected Document createDefaultModel() {
		return new posAngleDegDocument();
	}

	public int getAngle() {
		return Integer.valueOf(this.getText()).intValue();
	}

	static class posAngleDegDocument extends PlainDocument {

		private static final long serialVersionUID = 2731759679426625354L;

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str == null) {
				return;
			}

			try {
				String totalText = this.getText(0, this.getLength()) + str;

				float num = (Float.valueOf(totalText)).floatValue();

				int[] legalVals = { 1, 2, 3, 4, 5, 6, 9, 10, 12, 15, 18, 20,
						30, 36, 45, 60, 90, 180 };

				int finalNum = 0;
				for (int i = 0; i < legalVals.length; i++) {
					if (num >= legalVals[i]) {
						finalNum = legalVals[i];
					}
				}

				if (num > 0 && num <= 180) {
					super.insertString(offs,
							new String(Integer.valueOf(finalNum).toString()), a);
				}
			} catch (NumberFormatException e) {
				// do nothings... we don't care about it
			}

		}
	}

	public void stateChanged(ChangeEvent e) {
		Object eventSource = e.getSource();
		if (eventSource.getClass().getName().equals("javax.swing.JSlider")) {
			JSlider slider = (JSlider) eventSource;
			this.setText(Integer.valueOf(slider.getValue()).toString());
		}
		this.fireActionPerformed();
	}
}