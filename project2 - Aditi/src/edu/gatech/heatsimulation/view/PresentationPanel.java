package edu.gatech.heatsimulation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.model.EarthSurface;
import edu.gatech.heatsimulation.model.SimulationSetting;
import edu.gatech.heatsimulation.view.ControlPanel;
import edu.gatech.heatsimulation.view.PresentationPanel;
import static edu.gatech.heatsimulation.utility.SimulationConstant.*;

public class PresentationPanel extends JPanel  implements ActionListener {
	
	
	public boolean initPanel(SimulationFrame simulationFrame,
			 				 Controller simulationController) {
		
		if ((simulationFrame == null) ||
			(simulationController == null)) {
			return false;
		}
			
		this.simulationFrame = simulationFrame;
		this.simulationController = simulationController;
		
		panelWidth = simulationFrame.getWidth() - PANEL_FRAME_BORDER_GAP;
		panelHeight = ((simulationFrame.getHeight()/2) > CONTROL_PANEL_HEIGHT)?(simulationFrame.getHeight() /2 + simulationFrame.getHeight() %2):(simulationFrame.getHeight() - CONTROL_PANEL_HEIGHT);
		
		
		simulationProgress = false;
		addComponentsToPanel();
		
		// Only when single thread 
		timer = new Timer(1, this);
        timer.start();
       
		return true;
	}
	
	public boolean initEarthDisplay() {
		int gridSpacing = GRID_SPACING_MIN;
		int simulationStep = SIMULATION_STEP_MIN;
	    if (simulationController != null) {
	    	SimulationSetting simulationSetting = simulationController.getSimulationSetting();
	    	if (simulationSetting != null) {
	    		gridSpacing = simulationSetting.getGridSpacing();
	    		simulationStep = simulationSetting.getSimulationStep();
	    	}
	    }
	    
	    if (timeElapsedTextField != null) {
			timeElapsedTextField.setText("00");
		}
		
		if (gridSpaceTextField != null) {
			gridSpaceTextField.setText(Integer.toString(gridSpacing));
		}
		
		if (simulationStepTextField != null) {
			simulationStepTextField.setText(Integer.toString(simulationStep));
		}
	    
		return ((earthDisplayPanel != null) &&
				(earthDisplayPanel.initEarthDisplay(gridSpacing)));
	}
	
	public boolean updateEarthDisplay(EarthSurface earthSurface) {
		
		  if ((timeElapsedTextField != null) &&
		  	  (earthSurface != null)) {
			  	int elapsed = earthSurface.getHeatingTimeElapsed();
			  	String elapseunits = EMPTY_STRING;
			  	int formattedElapse = (elapsed);
			  	if (formattedElapse < 60) {
			  		elapseunits = " minutes";
			  	} else if (formattedElapse < 1440) {
			  		elapseunits = " hours";
			  		formattedElapse = formattedElapse/60;
			  	}  else {
			  		elapseunits = " days";
			  		formattedElapse = formattedElapse/1440;
			  	} 
				timeElapsedTextField.setText(Integer.toString(formattedElapse) + elapseunits);
			}
		
		return ((earthDisplayPanel != null) &&
				(earthDisplayPanel.updateEarthDisplay(earthSurface)));
	}
	 
	
	public boolean clearEarthDisplay() {
		
		if (timeElapsedTextField != null) {
			timeElapsedTextField.setText("00");
		}
		
		if (gridSpaceTextField != null) {
			gridSpaceTextField.setText("00");
		}
		
		if (simulationStepTextField != null) {
			simulationStepTextField.setText("00");
		}
				
		return ((earthDisplayPanel != null) &&
				(earthDisplayPanel.clearEarthDisplay()));
	}

	 public Dimension getPreferredSize() {
		 return new Dimension(panelWidth, panelHeight);
	 }
	
	 private void addComponentsToPanel() {
		 JPanel simulationSettingPanel = new JPanel();
	    	simulationSettingPanel.setMaximumSize(new Dimension((panelWidth/2), panelHeight/2));
	    	simulationSettingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	    	simulationSettingPanel.setLayout(new GridBagLayout());
	    	simulationSettingPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
	        GridBagConstraints gBC = new GridBagConstraints();
			gBC.fill = GridBagConstraints.HORIZONTAL;
			
			JLabel jLabel = new JLabel("Time Elapsed");
			gBC.fill = GridBagConstraints.BOTH;
			gBC.insets = new Insets(10, 0, 0, 10);
			
			gBC.gridwidth = GridBagConstraints.REMAINDER;
			gBC.anchor = GridBagConstraints.FIRST_LINE_START;
			
			gBC.gridx = 0;
			gBC.gridy = 0;
			gBC.gridwidth = 1;
			simulationSettingPanel.add(jLabel, gBC);
					
			timeElapsedTextField = new JTextField(15);
			gBC.gridx = 1;
		    gBC.gridy = 0;
		    gBC.gridwidth = 1;
		    timeElapsedTextField.setEditable(false);
		    timeElapsedTextField.setText("00");
		    simulationSettingPanel.add(timeElapsedTextField, gBC);

		   
			jLabel = new JLabel("Grid Space");
			gBC.gridx = 2;
			
			simulationSettingPanel.add(jLabel, gBC);
	      
			gridSpaceTextField = new JTextField(5);
			gBC.gridx = 3;
			gridSpaceTextField.setEditable(false);
			gridSpaceTextField.setText("00");
			simulationSettingPanel.add(gridSpaceTextField, gBC);
	      
			
			jLabel = new JLabel("Simulation Time Step");
			gBC.gridx = 4;
			simulationSettingPanel.add(jLabel, gBC);
	      
			simulationStepTextField = new JTextField(5);
			gBC.gridx = 5;
			simulationStepTextField.setEditable(false);
			simulationStepTextField.setText("00");
			simulationSettingPanel.add(simulationStepTextField, gBC);
			
			simulationSettingPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		    this.setLayout(new BorderLayout());
	    this.add(simulationSettingPanel, BorderLayout.PAGE_END);
	    
	    JLabel label = new JLabel("Display Panel");
	      label.setOpaque(true);
	      label.setBackground(Color.lightGray);

	      JPanel topPanel = new JPanel(new GridBagLayout());
	      
	      topPanel.add(label, gBC);
	      topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	      
	      
	    this.add(topPanel, BorderLayout.PAGE_START);
	    
	    earthDisplayPanel = new EarthDisplayPanel();
	    int gridSpacing = GRID_SPACING_MIN;
	    if (simulationController != null) {
	    	SimulationSetting simulationSetting = simulationController.getSimulationSetting();
	    	if (simulationSetting != null) {
	    		gridSpacing = simulationSetting.getGridSpacing();
	    	}
	    }
	    earthDisplayPanel.initPanel(panelWidth, (panelHeight-70), gridSpacing);
	    this.add(earthDisplayPanel, BorderLayout.CENTER);
	    
	 }
	 
    private void paintMap(Graphics aGraphics) {
    	
    	try {
    	
	    	if (aGraphics != null) {
	    	
	    		
	    		
	    		try {
	    			if (earthImage == null) {
	    				URL imgURL = this.getClass().getResource("worldmap.png");
		    			earthImage = ImageIO.read(imgURL);
	    			}
	    			 aGraphics.drawImage(earthImage, 10, 10, panelWidth-10, panelHeight-70, this);
	    	    }
	    	    catch (IOException e) {
	    	    	e.printStackTrace();
	    	    }
		        
	    	}
		 } catch(Exception exception) {
			        	
		}
    }
    
	protected void paintComponent(Graphics aGraphics) {
        super.paintComponent(aGraphics);
        
        BufferedImage bufferedImage = new BufferedImage(getWidth() - (2 * PANEL_GRID_BORDER_GAP), 
        									 getHeight() - (2 * PANEL_GRID_BORDER_GAP),
        									 BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.createGraphics();
        ///paintMap(graphics);
        
        aGraphics.drawImage(bufferedImage, 0, 0, this);
   }
	
	 @Override
	    public void actionPerformed(ActionEvent arg0) {
	 repaint();
	 }
	private SimulationFrame simulationFrame;
	private Controller simulationController;
	private int panelWidth;
	private int panelHeight;
	private int gridWidth;
	private int gridHeight;
	private int dimension;
	private double[][] color;
	private boolean simulationProgress;
	private Timer timer;
	private BufferedImage earthImage;
	private EarthDisplayPanel earthDisplayPanel;
	
	private JTextField timeElapsedTextField;
	private JTextField simulationStepTextField;
	private JTextField gridSpaceTextField;
}
