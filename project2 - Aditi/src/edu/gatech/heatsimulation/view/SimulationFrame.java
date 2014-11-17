package edu.gatech.heatsimulation.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.view.ControlPanel;
import edu.gatech.heatsimulation.view.PresentationPanel;
import static edu.gatech.heatsimulation.utility.SimulationConstant.*;

public class SimulationFrame extends JFrame {
	
	
	public boolean initFrame() {		
		
		setFrameProperty();		
		
		if (!initializeModule()) {
			return false;
		}
		
		if (!processModule()) {
			return false;
		}
		
		return true;
	}
		
	public int getHeight() {
		return frameHeight;
	}
	
	public int getWidth() {
		return frameWidth;
	}
	
	public Controller getSimulationController() {
		return controller;
	}
	
	public PresentationPanel getPresentationPanel() {
		return presentationPanel;
	}
	
	private void setFrameProperty() {
		
		frameHeight = FRAME_HEIGHT;
		frameWidth = FRAME_WIDTH;
		
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			
			if ((screenSize.width > 0) &&
				(screenSize.height > 0)) {
				
				frameWidth = (screenSize.width > FRAME_DESKTOP_BORDER_GAP)?
								(screenSize.width - FRAME_DESKTOP_BORDER_GAP):screenSize.width;
								
				frameHeight = (screenSize.height > FRAME_DESKTOP_BORDER_GAP)?
								(screenSize.height - FRAME_DESKTOP_BORDER_GAP):screenSize.width;
			}
		} catch (Exception exception) {
			// Do Nothing, default frame size is available
		}
		
		setTitle(APPLICATION_NAME);
		setSize(frameWidth, frameHeight);	    
	    setLocationRelativeTo(null);	    
	    setResizable(false);	    
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	    
	}

	 protected void processWindowEvent(WindowEvent event) {

	        if ((event.getID() == WindowEvent.WINDOW_CLOSING) &&
	        	(controller != null) && 
	        	(controller.isSimulationRunning())) {
		        
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
	        	
	        } else {
	           super.processWindowEvent(event);
	        }
	    }

	private boolean initializeModule() {
		
		try {
			if (!setController(controller)) {
				return false;
			}
			
			presentationPanel = new PresentationPanel();
			controlPanel = new ControlPanel();
			
			if ((presentationPanel == null) ||
				(controlPanel == null)) {
				return false;
			}
				
		} catch(Exception exception) {
			return false;
		}
		
		return true;
	}
	
	public boolean setController(Controller controller) {
		if (controller == null) {
			return false;
		}
		
		this.controller = controller;
		return true;		
	}
	
	private boolean processModule() {
		
		try {
			if (controlPanel != null) {
				controlPanel.initPanel(this, controller);
				this.add(controlPanel, BorderLayout.SOUTH);
			}
			
			if (presentationPanel != null) {
				presentationPanel.initPanel(this, controller);
				this.add(presentationPanel, BorderLayout.NORTH);
			}
			
			setBackground(Color.WHITE);
			getContentPane().setBackground(Color.WHITE);
			
		} catch(Exception exception) {
			return false;
		}
		
		return true;
	}
	
	/** 
     * Control Object which manages presentation of earth simulation.
     */
	private PresentationPanel presentationPanel;
		
	/** 
     * Panel Object which manages controls.
     */
	private ControlPanel controlPanel;
		
	/** 
     * Manages Heated Plate Object and Command
     */
	private Controller controller;
	
	/** 
     * Height of the frame
     */
	private int frameHeight;
	
	/** 
     * Width of the frame
     */
	private int frameWidth;
}
