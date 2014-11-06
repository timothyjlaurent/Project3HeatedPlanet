package EarthSim;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import EarthSim.SimulationConstants.*;
import earthWidgets.EarthPanel;
/**
 * 
 */
public class Demo extends JFrame {

	private SimulationFactory factory;
	private SimCmdReceivable initReceiver ;
	SimSettings simSettings;	
	private EarthPanel displayPanel;
	private ControlPanel controlPanel;
	
	/** 
     * Manages Heated Plate Object and Command
     */
//	private Controller heatedPlateController;
	
	/** 
     * Height of the frame
     */
	private int frameHeight;
	
	/** 
     * Width of the frame
     */
	private int frameWidth;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		parse the arguments
		SimSettings settings = new SimSettings();
		CmdLineParser parser = new CmdLineParser(settings);
		parser.setUsageWidth(80);
		try {
			parser.parseArgument(args);
			if( settings.simulationInitiative && settings.presentationInitiative ){
				throw new CmdLineException(parser, "You cannot use -t and -r together.");
			}
		} catch(CmdLineException e ) {
			System.err.println(e.getMessage());
			System.err.println("java EarthSim.Demo [-s] [-p] [-r|-t] [-b #]");
			parser.printUsage(System.err);
			return;
		}
		
		new Demo(settings);		
	}
	
	/**
	 * @return
	 * @throws InterruptedException 
	 */
	public void pause() throws InterruptedException {
		initReceiver.pause();
	}

	/**
	 * @return
	 * @throws InterruptedException 
	 */
	public void resume() throws InterruptedException {
		initReceiver.resume();
	}

	/**
	 * @return
	 * @throws InterruptedException 
	 */
	public void stop() throws InterruptedException {
		initReceiver.stop();
	}

	/**
	 * @param SimulationSettings
	 * @return
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException {
		this.updateSimSettings();
		
		initReceiver.init();
		this.factory = new SimulationFactory(this.simSettings);
		new Thread(this.factory).start();
	}

	private void updateSimSettings() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param SimulationSettings
	 */
	public Demo(SimSettings simSettings) {
		// build out ui and initialize option settings
		this.simSettings = simSettings;
//		this.displayPanel = new EarthPanel(new Dimension(100, 100), new Dimension(800, 400) , new Dimension(1000, 500));

//		this.controlPanel = new ControlPanel();
		this.setFrameProperty();
		this.processModule();
		this.initFrame();
		this.setVisible(true);
	}

	/**
	 * sets ui-parameters
	 */
	private void initializeUI() {
		
		
	}
	
	
	/*
	 *  Initialize the frame with necessary size and characteristics
	 *
	*/
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
	
//	public HeatedPlateController getHeatedPlateController() {
//		return heatedPlateController;
//	}
//	
	public EarthPanel getResultPanel() {
		return displayPanel;
	}
	
	private void setFrameProperty() {
		
		frameHeight = SimulationConstants.FRAME_HEIGHT;
		frameWidth = SimulationConstants.FRAME_WIDTH;
		
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			
			if ((screenSize.width > 0) &&
				(screenSize.height > 0)) {
				
				frameWidth = 1200;
				frameHeight = 700;
				
				//frameWidth = (screenSize.width > SimulationConstants.FRAME_DESKTOP_BORDER_GAP)?
					//			(screenSize.width - SimulationConstants.FRAME_DESKTOP_BORDER_GAP):screenSize.width;
								
				//frameHeight = (screenSize.height > SimulationConstants.FRAME_DESKTOP_BORDER_GAP)?
					//			(screenSize.height - SimulationConstants.FRAME_DESKTOP_BORDER_GAP):screenSize.width;
			}
		} catch (Exception exception) {
			// Do Nothing, default frame size is available
		}
		
		setTitle(SimulationConstants.APPLICATION_NAME);
		setSize(frameWidth, frameHeight);	    
	    setLocationRelativeTo(null);	    
	    setResizable(false);	    
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private boolean initializeModule() {
		
		try {
			displayPanel = new EarthPanel(new Dimension(150, 150), new Dimension(480, 240) , new Dimension(1000, 500));
			displayPanel.setMapOpacity(.5f);
			controlPanel = new ControlPanel();
//			heatedPlateController = new HeatedPlateController();
			simSettings.dispPanel = this.displayPanel;
			if ((displayPanel == null) ||
				(controlPanel == null) 
//				||
//				(heatedPlateController == null)) {
				){
				return false;
			}
				
		} catch(Exception exception) {
			return false;
		}
		
		return true;
	}
	
	private boolean processModule() {
		
		try {
			if (displayPanel != null) {
				displayPanel.initPanel(this);
				this.add(displayPanel, BorderLayout.EAST);
			}
			
			if (controlPanel != null) {
				this.add(controlPanel, BorderLayout.WEST);
				controlPanel.initPanel(this);
			
			}
			

			
			
//			if (heatedPlateController != null) {
//				heatedPlateController.initController(this);
//			}
			
		} catch(Exception exception) {
			return false;
		}
		
		return true;
	}
	



}