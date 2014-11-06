package EarthSim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

//public class ControlPanel extends JPanel implements ActionListener {
//
//	@Override
//	public void actionPerformed(ActionEvent arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void initPanel(Demo demo) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//}















import static EarthSim.SimulationConstants.*;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel implements ActionListener {
	
	private JSpinner spinnerRefreshRate;
	private JSpinner spinnerTimeStep;
	private JSpinner spinnerGridSpacing;
	private JSpinner spinnerBufferSize;
	private JRadioButton rbSimInitiative;
	private JRadioButton rbPresInitiative;
	
	private JTextField textFieldDimension;	
	private JTextField textFieldTop;
	private JTextField textFieldBottom;
	private JTextField textFieldLeft;
	private JTextField textFieldRight;
	private JButton buttonStop;
	private JButton buttonStart;
	private JButton buttonResume;
	private JButton buttonPause;
	private  ButtonGroup initiativeButtons ;
	
	
	private SimCmdMsg initMsg;
	
	private Demo demoFrame;
	private SimSettings settings;
	private int panelWidth;
	private int panelHeight;
	private JSpinner spinnerDisplayRate;
	private JCheckBox checkboxPresThread;
	private JCheckBox checkboxSimThread;
	private SimulationFactory sf;
	private SimCmdReceivable init;
	
	
	public ControlPanel() {		
	}
	
	public boolean initPanel(Demo demoFrame) {
		
		this.settings = demoFrame.simSettings;
		
		this.demoFrame = demoFrame;
		
		panelWidth = (demoFrame.getWidth() > CONTROL_PANEL_WIDTH)?CONTROL_PANEL_WIDTH:(CONTROL_PANEL_WIDTH/2);
		panelHeight = demoFrame.getHeight();
		
		addComponentToPanel();
		
		
		initMsg = this.demoFrame.simSettings.initMsg;
		
		return true;
	}
	
	 public Dimension getPreferredSize() {
		 return new Dimension(panelWidth, 
				 			  panelHeight);
	 }
	  
    private void addComponentToPanel() {
    	
    	this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    	this.setLayout(new GridBagLayout());
		
        GridBagConstraints gBC = new GridBagConstraints();
//		gBC.fill = GridBagConstraints.HORIZONTAL;
		JLabel jLabel;
//		
//		JLabel jLabel = new JLabel("<html><body style='width: 120 px'>Directions :Select height and " +
//								   "width of plate. Then select " +
//								   "top, right, bottom, and left " +
//								   "temperatures. Then select" +
//								   " an algorithm and click " +
//								   "\"Go!\"");
//		gBC.fill = GridBagConstraints.BOTH;
//		gBC.insets = new Insets(10, 0, 0, 10);
//		gBC.gridx = 0;
//		gBC.gridy = 0;
//		gBC.gridheight = 2;
//		gBC.gridwidth = 2;
////		
//		gBC.gridwidth = GridBagConstraints.REMAINDER;
//		gBC.anchor = GridBagConstraints.LINE_START;
//		this.add(jLabel, gBC);
		
		gBC.fill = GridBagConstraints.BOTH;
		gBC.insets = new Insets(10, 0, 0, 10);
		gBC.gridx = 0;
		gBC.gridy = 0;
		
		gBC.gridheight = 1;
		gBC.gridwidth = 1;
		

		
		
	jLabel = new JLabel("Initiative");
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(jLabel, gBC);
	    
    rbSimInitiative = new JRadioButton("Simulation Master");   
    if( demoFrame.simSettings.simulationInitiative){
    	rbSimInitiative.setSelected(true);
    }
    rbSimInitiative.setActionCommand("sim");
    gBC.gridx = 0;
    gBC.gridy += 1;
    gBC.gridwidth =1;
    this.add(rbSimInitiative, gBC);
    
    rbPresInitiative = new JRadioButton("Presentation Master");   
    if( demoFrame.simSettings.presentationThread){
    	rbPresInitiative.setSelected(true);
    }
    rbPresInitiative.setActionCommand("pres");
    gBC.gridx = 0;
    gBC.gridy += 1;
    gBC.gridwidth =1;
    this.add(rbPresInitiative, gBC);
		
    JRadioButton rbOtherInitiative = new JRadioButton("Other Master");   
    if( demoFrame.simSettings.presentationThread){
    	rbOtherInitiative.setSelected(true);
    }
    rbOtherInitiative.setActionCommand("other");
    gBC.gridx = 0;
    gBC.gridy += 1;
    gBC.gridwidth =1;
    this.add(rbOtherInitiative, gBC);	
		
    
    initiativeButtons = new ButtonGroup();
    
    initiativeButtons.add(rbSimInitiative);
    initiativeButtons.add(rbPresInitiative);
    initiativeButtons.add(rbOtherInitiative);

//    this.add(initiativeButtons, gBC);

    
    
		
		jLabel = new JLabel("Threading");
		    gBC.gridx = 0;
		    gBC.gridy += 1;
		    gBC.gridwidth =1;
		    this.add(jLabel, gBC);
		    
	    checkboxSimThread = new JCheckBox("Simulation Thread");   
	    if( demoFrame.simSettings.simulationThread){
	    	checkboxSimThread.setSelected(true);
	    }
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(checkboxSimThread, gBC);
	    
	    checkboxPresThread = new JCheckBox("Presentation Thread");   
	    if( demoFrame.simSettings.presentationThread){
	    	checkboxPresThread.setSelected(true);
	    }
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(checkboxPresThread, gBC);
	    
	    
		
		
		 jLabel = new JLabel("Buffer Size");
		    gBC.gridx = 0;
		    gBC.gridy += 1;
		    gBC.gridwidth =1;
		    this.add(jLabel, gBC);
		
		
		spinnerBufferSize = new JSpinner(new SpinnerNumberModel(1,1,10000, 1));
		gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    spinnerBufferSize.setValue(demoFrame.simSettings.bufferSize);
	    this.add(spinnerBufferSize, gBC);
		
	   
	     
	    
//	     grid spacing
	    jLabel = new JLabel("Grid Spacing");
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(jLabel, gBC);
	    
		spinnerGridSpacing = new JSpinner(new SpinnerNumberModel(15,1,180, 1));
		
		spinnerGridSpacing.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				JSpinner spinner =((JSpinner) e.getSource()); 
				Integer input = (Integer) spinner.getValue();
				
				while( 360 % input != 0 ){
					input -= 1;
				}
				spinner.setValue(input);
			}
		});		
		gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    spinnerBufferSize.setValue(demoFrame.simSettings.bufferSize);
	    this.add(spinnerGridSpacing, gBC);

	    
	    // Timestep
	    jLabel = new JLabel("Time Step");
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(jLabel, gBC);
	    
		spinnerTimeStep = new JSpinner(new SpinnerNumberModel(1,1,1440, 1));
		gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(spinnerTimeStep, gBC);

	    jLabel = new JLabel("Display Rate(ms)");
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(jLabel, gBC);
	    
		spinnerDisplayRate = new JSpinner(new SpinnerNumberModel(1,1,1000, 1));
		gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(spinnerDisplayRate, gBC);

	   
	    buttonStart = new JButton("Start");
//	    buttonStart.addActionListener()
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(buttonStart, gBC);
	    buttonStart.setActionCommand("start");
	    buttonStart.addActionListener(this);
	    
	    buttonPause = new JButton("Pause");
//	    buttonStart.addActionListener()
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(buttonPause, gBC);
	    buttonPause.setActionCommand("pause");
	    buttonPause.addActionListener(this);
	    
	    buttonResume = new JButton("Resume");
//	    buttonResume.addActionListener()
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(buttonResume, gBC);
	    buttonResume.setActionCommand("resume");
	    buttonResume.addActionListener(this);
	    
	    buttonStop = new JButton("Stop");
//	    buttonStop.addActionListener()
	    gBC.gridx = 0;
	    gBC.gridy += 1;
	    gBC.gridwidth =1;
	    this.add(buttonStop, gBC);
	    buttonStop.setActionCommand("stop");  
	    buttonStop.addActionListener(this);

	    toggleButtons("init");
	    
    }
   
    private void toggleButtons(String s){
    	if ( s.equals("init") || s.equals("stop")){
    		buttonStart.setEnabled(true);
    		buttonPause.setEnabled(false);
    		buttonStop.setEnabled(false);
    		buttonResume.setEnabled(false);
    	}
    	if( s.equals("start") || s.equals("resume") ){
    		buttonStart.setEnabled(false);
    		buttonPause.setEnabled(true);
    		buttonStop.setEnabled(true);
    		buttonResume.setEnabled(false);
    	}
    	if( s.equals("pause") ){
    		buttonStart.setEnabled(false);
    		buttonPause.setEnabled(false);
    		buttonStop.setEnabled(true);
    		buttonResume.setEnabled(true);
    	}
    }
    
    
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
    	String msg = actionEvent.getActionCommand();
    	
    	if (msg.equals("start")) {
        // TODO-TIM needs to work here 	
    		loadSimSettings();
    		// TODO-TIM buttons not untoggling
    		this.toggleButtons("start");

    		new Thread(new Runnable(){

				@Override
				public void run() {
					initMsg.start();					
				}
    		}).start();
    		
    		
    		this.sf = new SimulationFactory(this.settings);
			new Thread(this.sf).start();
    	}
    	else if ( msg.equals("stop")){
    		new Thread(new Runnable(){

				@Override
				public void run() {
					initMsg.stop();					
				}
    		}).start();
    		toggleButtons("stop");
    	}
    	else if ( msg.equals("resume")){
    		new Thread(new Runnable(){

				@Override
				public void run() {
					initMsg.resume();					
				}
    		}).start();
    		toggleButtons("resume");
    	}
    	else if ( msg.equals("pause")){
    		new Thread(new Runnable(){

				@Override
				public void run() {
					initMsg.pause();					
				}
    		}).start();
    		toggleButtons("pause");
    	}
    	toggleButtons(msg);
    }

	private void loadSimSettings() {
		this.settings.bufferSize = (Integer) spinnerBufferSize.getValue(); 
		this.settings.displayRate = ( Integer ) spinnerDisplayRate.getValue();
		this.settings.gridSize = ( Integer ) spinnerGridSpacing.getValue();
		this.settings.timeStep = ( Integer ) spinnerTimeStep.getValue();
		if ( initiativeButtons.getSelection() == null ){
			this.settings.setInit("");
		}
		else this.settings.setInit( initiativeButtons.getSelection().getActionCommand() );
		this.settings.setThread( this.checkboxSimThread.isSelected() , this.checkboxPresThread.isSelected()	 );
	}


	

}
