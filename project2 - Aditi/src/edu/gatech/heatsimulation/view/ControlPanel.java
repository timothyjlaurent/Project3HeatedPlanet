package edu.gatech.heatsimulation.view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;






import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.view.SimulationFrame;
import static edu.gatech.heatsimulation.utility.SimulationConstant.*;
import static edu.gatech.heatsimulation.utility.SimulationConstant.SimulationAction.*;

public class ControlPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public boolean initPanel(SimulationFrame simulationFrame,
							 Controller simulationController) {
		
		if ((simulationFrame == null) ||
			(simulationController == null)) {
			return false;
		}
		
		this.simulationFrame = simulationFrame;
		this.simulationController = simulationController;
		
		panelWidth = simulationFrame.getWidth()-PANEL_FRAME_BORDER_GAP;
		panelHeight = ((simulationFrame.getHeight()/2 > CONTROL_PANEL_HEIGHT)?(simulationFrame.getHeight()/2):(CONTROL_PANEL_HEIGHT)) - PANEL_FRAME_BORDER_GAP;
		
		addComponentToPanel();
		
		return true;
	}
	
	 public Dimension getPreferredSize() {
		 return new Dimension(panelWidth, 
				 			  panelHeight);
	 }
	 	
    private void addComponentToPanel() {
    	
    	this.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
    	this.setLayout(new GridBagLayout());
		
    	CreateControlButtonPanel();
    	CreateSimulationSettingPanel();
    	CreateMapSettingPanel();
    	CreateSimulationAndMapSettingPanel();
    	
    	CreatePresentationSettingPanel();
    	
    	setBackground(Color.WHITE);
    	GridBagConstraints gBC = new GridBagConstraints();
    	gBC.fill = GridBagConstraints.HORIZONTAL;
    	gBC.insets = new Insets(0, 20, 0, 40);				
    	gBC.fill = GridBagConstraints.BOTH;
    	gBC.weightx = 1;
    	gBC.anchor = GridBagConstraints.FIRST_LINE_START;
    	gBC.gridx = 0;
    	gBC.gridy = 0;
    	this.add(controlButtonPanel, gBC);
    	gBC.gridx = 1;
    	gBC.weightx = 8;
    	gBC.insets = new Insets(0, 120, 0, 120);	
    	this.add(simulationAndMapSettingPanel, gBC);
    	
    	gBC.gridx = 2;
    	gBC.gridy = 0;
    	gBC.weightx = 1;
    	gBC.gridheight = 2;
    	gBC.insets = new Insets(0, 150, 0, 20);
    	this.add(presentationSettingPanel, gBC);
    	
    }
    
    
    public void actionPerformed(ActionEvent actionEvent) {
    	
    	String actionCommand = actionEvent.getActionCommand();
    	if (actionCommand.equals("Reset Space")) {
    		if (gridSpacingTextField != null) {
    			gridSpacingTextField.setText(EMPTY_STRING);
    		}
    		return;
		}
		else if (actionCommand.equals("Reset Step")) {
			if (simulationStepTextField != null) {
				simulationStepTextField.setText(EMPTY_STRING);
    		}
			return;
		}
		else if (actionCommand.equals("Reset Display")) {
			if (presentationDisplayRateTextField != null) {
				presentationDisplayRateTextField.setText(EMPTY_STRING);
    		}
			return;
		}
    	    	
    	
    	SimulationAction simulationAction = SIMULATION_ACTION_NONE;
    	
        if (actionCommand.equals("Start")) {
        	simulationAction = SIMULATION_ACTION_START;
        	startButton.setEnabled(false);
        	stopButton.setEnabled(true);
        	pauseButton.setEnabled(true);
        	resumeButton.setEnabled(false);
        	exitButton.setEnabled(false);
        	spaceButton.setEnabled(false);
        	stepButton.setEnabled(false);
        	displayButton.setEnabled(false);
        }
        else if (actionCommand.equals("Stop")) {
        	simulationAction = SIMULATION_ACTION_STOP;
        	resetButton();
        }
		else if (actionCommand.equals("Pause")) {
			simulationAction = SIMULATION_ACTION_PAUSE;
			startButton.setEnabled(false);
        	stopButton.setEnabled(true);
        	pauseButton.setEnabled(false);
        	resumeButton.setEnabled(true);
        	exitButton.setEnabled(false);
		}
		else if (actionCommand.equals("Resume")) {
			simulationAction = SIMULATION_ACTION_RESUME;
			startButton.setEnabled(false);
        	stopButton.setEnabled(true);
        	pauseButton.setEnabled(true);
        	resumeButton.setEnabled(false);
        	exitButton.setEnabled(false);
		}
		else if (actionCommand.equals("Exit")) {
			simulationAction = SIMULATION_ACTION_EXIT;
			 if ((simulationController != null) && 
			     (simulationController.isSimulationRunning())) {
				        
			     	Object[] options = { "Yes", "Cancel" };
				    int optionCode = JOptionPane.showOptionDialog(this, 
				       											  "Simulation in progress, Are you sure to exit?", "Warning",
				       											  JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				       											  null,
				       											  options,
				       											  options[01]);
				    if (optionCode == JOptionPane.YES_OPTION) {
				    	System.exit(0);
				    }
			 } 
			 System.exit(0);
		}
		else {
			return;
		}
        
        try {
	        if ((simulationController == null) ||
	    		(!simulationController.executeSimulationCommand(simulationAction,
	    									     			    Integer.valueOf(gridSpacingTextField.getText()),
	    									     			    Integer.valueOf(simulationStepTextField.getText()),
	    									     			    Integer.valueOf(presentationDisplayRateTextField.getText())))) {
	        	resetButton();
	        	return;
	    	}
        }
        catch(NumberFormatException exception) {
        	resetButton(); 
        	JOptionPane.showMessageDialog(simulationFrame, "Please validate simulation setting \n Grid Spacing ( 1to 180) \n"
			 		+ "Simulation step (1 to 1440) \n Presentation Display rate (reasonable in minutes,seconds,hours");
			 
			 return;
		 }
    }

    private void resetButton() {
    	startButton.setEnabled(true);
    	stopButton.setEnabled(false);
    	pauseButton.setEnabled(false);
    	resumeButton.setEnabled(false);
    	exitButton.setEnabled(true);
    	spaceButton.setEnabled(true);
    	stepButton.setEnabled(true);
    	displayButton.setEnabled(true);
    }
    
    private boolean CreateControlButtonPanel() {
    	
    	controlButtonPanel = new JPanel();
    	//controlButtonPanel.setMaximumSize(new Dimension((panelWidth/3), panelHeight));
    	controlButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    	controlButtonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    	controlButtonPanel.setLayout(new GridBagLayout());
    	controlButtonPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));

    	GridBagConstraints gBC = new GridBagConstraints();
    	gBC.fill = GridBagConstraints.HORIZONTAL;
    				
    	JLabel jLabel = new JLabel("<html><body style='width: 80 px'>Control Panel");
    	gBC.fill = GridBagConstraints.BOTH;
    	gBC.insets = new Insets(10, 0, 0, 10);
    	gBC.gridwidth = GridBagConstraints.REMAINDER;
    	gBC.anchor = GridBagConstraints.FIRST_LINE_START;
    	gBC.gridx = 0;
    	gBC.gridy = 0;
    	controlButtonPanel.add(jLabel, gBC);
    	
    	startButton = new JButton("Start");
    	gBC.gridx = 0;
    	gBC.gridy = 1;
    	gBC.gridwidth = 1;
    	controlButtonPanel.add(startButton, gBC);	
    	startButton.addActionListener(this);
    	    				
    	stopButton = new JButton("Stop");
    	gBC.gridx = 0;
    	gBC.gridy = 2;
    	gBC.gridwidth = 1;
    	controlButtonPanel.add(stopButton, gBC);	
    	stopButton.setEnabled(false);
    	stopButton.addActionListener(this);
    	    				
    	pauseButton = new JButton("Pause");
    	gBC.gridx = 0;
    	gBC.gridy = 3;
    	gBC.gridwidth = 1;
    	controlButtonPanel.add(pauseButton, gBC);	
    	pauseButton.setEnabled(false);
    	pauseButton.addActionListener(this);
    	    				
    	resumeButton = new JButton("Resume");
    	gBC.gridx = 0;
    	gBC.gridy = 4;
    	gBC.gridwidth = 1;
    	controlButtonPanel.add(resumeButton, gBC);	
    	resumeButton.setEnabled(false);
    	resumeButton.addActionListener(this);
    	
    	exitButton = new JButton("Exit");
    	gBC.gridx = 0;
    	gBC.gridy = 5;
    	gBC.gridwidth = 1;
    	controlButtonPanel.add(exitButton, gBC);	    	    				
    	exitButton.addActionListener(this);
    	
    	return true;
    }
    
    private boolean CreateSimulationAndMapSettingPanel() {
    	
    	simulationAndMapSettingPanel = new JPanel();
    	simulationAndMapSettingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    	simulationAndMapSettingPanel.setLayout(new GridBagLayout());
    	
	        			
	        			
    	
    	simulationAndMapSettingPanel.setBackground(Color.WHITE);
    	GridBagConstraints gBC = new GridBagConstraints();
    	gBC.fill = GridBagConstraints.HORIZONTAL;
    	gBC.insets = new Insets(0, 20, 20, 40);				
    	gBC.fill = GridBagConstraints.BOTH;
    	gBC.anchor = GridBagConstraints.FIRST_LINE_START;
    	gBC.gridx = 0;
    	gBC.gridy = 0;
    	simulationAndMapSettingPanel.add(simulationSettingPanel, gBC);
    	
    	gBC.gridy = 1;
    	simulationAndMapSettingPanel.add(mapSettingPanel, gBC);
    	
    	return true;
    }
    
   
    private boolean CreateSimulationSettingPanel() {
    	
    	simulationSettingPanel = new JPanel();
    	simulationSettingPanel.setMaximumSize(new Dimension((panelWidth/2), panelHeight/2));
    	simulationSettingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    	simulationSettingPanel.setLayout(new GridBagLayout());
    	simulationSettingPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        GridBagConstraints gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel jLabel = new JLabel("<html><body style='width: 180 px'>Simulation Settings");
		gBC.fill = GridBagConstraints.BOTH;
		gBC.insets = new Insets(10, 0, 0, 10);
		
		gBC.gridwidth = GridBagConstraints.REMAINDER;
		gBC.anchor = GridBagConstraints.FIRST_LINE_START;
		
		gBC.gridx = 0;
		gBC.gridy = 0;
		
		simulationSettingPanel.add(jLabel, gBC);
				
		gridSpacingTextField = new JTextField(5);
		gBC.gridx = 0;
	    gBC.gridy = 1;
	    gBC.gridwidth = 1;
	    gridSpacingTextField.setEditable(true);
	  
	    simulationSettingPanel.add(gridSpacingTextField, gBC);

	    jLabel = new JLabel("Grid Space");
	    gBC.gridx = 1;
	    gBC.gridy = 1;
	    gBC.gridwidth =1;
	    simulationSettingPanel.add(jLabel, gBC);
		
	    spaceButton = new JButton("Reset Space");
		gBC.gridx = 0;
		gBC.gridy = 2;
		gBC.gridwidth = 1;
		simulationSettingPanel.add(spaceButton, gBC);	
		spaceButton.addActionListener(this);
		
		simulationStepTextField = new JTextField(5);
		gBC.gridx = 2;
	    gBC.gridy = 1;
	    gBC.gridwidth = 1;
	    simulationStepTextField.setEditable(true);
	    
	    simulationSettingPanel.add(simulationStepTextField, gBC);

	    jLabel = new JLabel("Simulation Step");
	    gBC.gridx = 3;
	    gBC.gridy = 1;
	    gBC.gridwidth =1;
	    simulationSettingPanel.add(jLabel, gBC);
		
	    stepButton = new JButton("Reset Step");
		gBC.gridx = 2;
		gBC.gridy = 2;
		gBC.gridwidth = 1;
		simulationSettingPanel.add(stepButton, gBC);	
		stepButton.addActionListener(this);
		
		gBC.gridy = GridBagConstraints.RELATIVE;
		
    	return true;
    }

    private boolean CreateMapSettingPanel() {
    	
    	
    	mapSettingPanel = new JPanel();
    	mapSettingPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    	mapSettingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    	mapSettingPanel.setLayout(new GridBagLayout());
    	mapSettingPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        GridBagConstraints gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel jLabel = new JLabel("<html><body style='width: 180 px'>Map Settings");
		gBC.fill = GridBagConstraints.BOTH;
		gBC.insets = new Insets(10, 0, 0, 10);
		
		gBC.gridwidth = GridBagConstraints.REMAINDER;
		gBC.anchor = GridBagConstraints.FIRST_LINE_START;
		
		gBC.gridx = 0;
		gBC.gridy = 0;
		
		mapSettingPanel.add(jLabel, gBC);
				
		String behrmannImageURL = ControlPanel.class.getResource("").toString();
		behrmannImageURL= behrmannImageURL.substring(6, behrmannImageURL.indexOf("bin/edu/gatech/heatsimulation/view/")) + "resources/behcylea.gif";
		behrmannImageURL = behrmannImageURL.replace("/", "\\\\");
		try {
			behrmannImageURL = java.net.URLDecoder.decode(behrmannImageURL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(behrmannImageURL); 
		JLabel thumb = new JLabel();
		thumb.setIcon(icon);
		thumb.setMaximumSize(new Dimension(50, 120));
				
		gBC.gridx = 0;
	    gBC.gridy = 1;
	    gBC.gridwidth = 1;
	    mapSettingPanel.add(thumb, gBC);
	    
	    JButton jbnButton = new JButton("Behrmann Projection");
		gBC.gridx = 0;
		gBC.gridy = 2;
		gBC.gridwidth = 1;
		//mapSettingPanel.add(jbnButton, gBC);
		JRadioButton behrmannProjRadioButton = new JRadioButton("Behrmann Projection");
		mapSettingPanel.add(behrmannProjRadioButton, gBC);
		jbnButton.addActionListener(this);
		
	    behrmannImageURL = ControlPanel.class.getResource("").toString();
		behrmannImageURL= behrmannImageURL.substring(6, behrmannImageURL.indexOf("bin/edu/gatech/heatsimulation/view/")) + "resources/behcylea.gif";
		behrmannImageURL = behrmannImageURL.replace("/", "\\\\");
		try {
			behrmannImageURL = java.net.URLDecoder.decode(behrmannImageURL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 icon = new ImageIcon(behrmannImageURL); 
		 thumb = new JLabel();
		thumb.setIcon(icon);
		thumb.setMaximumSize(new Dimension(50, 120));
		gBC.gridx = 1;
		gBC.gridy = 1;
		mapSettingPanel.add(thumb, gBC);
		
	    jbnButton = new JButton("Unprojected Map");
		gBC.gridx = 1;
		gBC.gridy = 2;
		gBC.gridwidth = 1;
		//mapSettingPanel.add(jbnButton, gBC);
		JRadioButton unProjRadioButton = new JRadioButton("Unprojected Map", true);
		mapSettingPanel.add(unProjRadioButton, gBC);
		ButtonGroup group = new ButtonGroup();
	      group.add(behrmannProjRadioButton);
	      group.add(unProjRadioButton);
		jbnButton.addActionListener(this);
		
		gBC.gridy = GridBagConstraints.RELATIVE;
		
    	return true;
    }

	private boolean CreatePresentationSettingPanel() {
    	
    	presentationSettingPanel = new JPanel();
    	presentationSettingPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    	
    	presentationSettingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    	presentationSettingPanel.setLayout(new GridBagLayout());
    	presentationSettingPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        GridBagConstraints gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel jLabel = new JLabel("<html><body style='width: 80 px'>Display Settings");
		gBC.fill = GridBagConstraints.BOTH;
		gBC.insets = new Insets(10, 0, 0, 10);
		
		gBC.gridwidth = GridBagConstraints.REMAINDER;
		gBC.anchor = GridBagConstraints.LINE_START;		
		gBC.gridx = 0;
		gBC.gridy = 0;
		
		presentationSettingPanel.add(jLabel, gBC);
				
		presentationDisplayRateTextField = new JTextField(5);
		gBC.gridx = 0;
	    gBC.gridy = 1;
	    gBC.gridwidth = 1;
	    presentationDisplayRateTextField.setEditable(true);
	    
	    presentationSettingPanel.add(presentationDisplayRateTextField, gBC);

	    jLabel = new JLabel("DisplayRate");
	    gBC.gridx = 1;
	    gBC.gridy = 1;
	    gBC.gridwidth =1;
	    presentationSettingPanel.add(jLabel, gBC);
		
		
	    displayButton = new JButton("Reset Display");
		gBC.gridx = 0;
		gBC.gridy = 2;
		gBC.gridwidth = 1;
		presentationSettingPanel.add(displayButton, gBC);	
		displayButton.addActionListener(this);
		
		jLabel = new JLabel("(MilliSeconds)");
	    gBC.gridx = 1;
	    gBC.gridy = 2;
	    gBC.gridwidth =1;
	    presentationSettingPanel.add(jLabel, gBC);
		
		
		
		
		gBC.gridy = GridBagConstraints.RELATIVE;
		
		return true;
    }
    
	private JTextField presentationDisplayRateTextField;
	private JTextField simulationStepTextField;
	private JTextField gridSpacingTextField;
	
	private JButton startButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton resumeButton;
	private JButton exitButton;
	
	private JButton spaceButton;
	private JButton stepButton;
	private JButton displayButton;
	
	
	private SimulationFrame simulationFrame;
	private Controller simulationController;
	
	private JPanel controlButtonPanel;
	private JPanel simulationAndMapSettingPanel;
	private JPanel simulationSettingPanel;
	private JPanel mapSettingPanel;
	private JPanel presentationSettingPanel;
	
	private int panelWidth;
	private int panelHeight;
	
	
}
