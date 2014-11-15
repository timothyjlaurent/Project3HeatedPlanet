package edu.gatech.heatsimulation.view;

import static edu.gatech.heatsimulation.utility.SimulationConstant.CONTROL_PANEL_HEIGHT;
import static edu.gatech.heatsimulation.utility.SimulationConstant.PANEL_FRAME_BORDER_GAP;
import static edu.gatech.heatsimulation.utility.SimulationConstant.PANEL_GRID_BORDER_GAP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;






import edu.gatech.heatsimulation.controller.Controller;
import edu.gatech.heatsimulation.utility.EarthMath;
import edu.gatech.heatsimulation.utility.GridColorPicker;
import edu.gatech.heatsimulation.model.EarthSurface;

public class EarthDisplayPanel extends JPanel {

	

	
	public boolean initPanel(int panelWidth,
							 int panelHeight,
							 int degreeSeparation) {
		
		if ((panelWidth < 0) ||
			(panelHeight < 0)) {
			return false;
		}
		
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
				
		
		String worldMapImageURL = EarthDisplayPanel.class.getResource("").toString();
		worldMapImageURL= worldMapImageURL.substring(6, worldMapImageURL.indexOf("bin/edu/gatech/heatsimulation/view/")) + "resources/worldmap.png";
		worldMapImageURL = worldMapImageURL.replace("/", "\\\\");
		try {
			worldMapImageURL = java.net.URLDecoder.decode(worldMapImageURL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			if (earthImage == null) {
				//URL imgURL = new URL(worldMapImageURL);
				// InputStream in = getClass().getResourceAsStream(worldMapImageURL);
    			earthImage = ImageIO.read(new File(worldMapImageURL));
			}
			// aGraphics.drawImage(earthImage, 0, 0, panelWidth, panelHeight-70, this);
	    }
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
		
		initEarthDisplay(degreeSeparation);
		 
		return true;
	}
	
	public boolean initEarthDisplay(int degreeSeparation) {
		this.degreeSeparation = degreeSeparation;
	    
	    numCellsX = 360 / degreeSeparation;      
	    pixelsPerCellX = /*earthImage.getWidth()*/ panelWidth/ numCellsX;
	    imgWidth = numCellsX * pixelsPerCellX;

	    numCellsY = 180 / degreeSeparation;
	    pixelsPerCellY = /*earthImage.getHeight()*/ panelHeight/ numCellsY;
	    imgHeight = numCellsY * pixelsPerCellY;
	    radius = imgHeight/2;
	    
	    //create an image capable of transparency; then draw our image into it
	    bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics g = bufferedImage.getGraphics();
	    g.drawImage(earthImage, 0, 0, imgWidth, imgHeight, null);  
	    
	    return true;
	}
	
	public boolean updateEarthDisplay(EarthSurface earthSurface) {
		if (earthSurface == null) {
			return false;
		}
		this.earthSurface = earthSurface;
		paintInitialColors = false; 
		repaint();
		return true;
	}
	
	
	public boolean clearEarthDisplay() {
	
		paintInitialColors = true; 
		repaint();
		return true;
	}
	
	protected void paintComponent(Graphics aGraphics) {
        super.paintComponent(aGraphics);
        
      /*  bufferedImage = new BufferedImage(getWidth(), 
				 getHeight(),
				 BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.createGraphics();
        paintMap(graphics);

		aGraphics.drawImage(bufferedImage, 0, 0, this);*/
		
		if(paintInitialColors)
    {
      initCellColors(aGraphics);
    }
    else 
      fillCellColors(aGraphics);
    
	
	drawGrid(aGraphics);
	drawTransparentImage(aGraphics);
	}
	
	
	  private void initCellColors(Graphics g) {
		    g.setColor(GridColorPicker.getColor(DEFAULT_CELL_TEMP));
		    g.fillRect(0, 0, imgWidth, imgHeight);
		  }
		  
	private void paintMap(Graphics aGraphics) {
	    	
	    	try {
	    	
		    	if (aGraphics != null) {
		    	
		    		
		    		
		    		try {
		    			if (earthImage == null) {
		    				URL imgURL = this.getClass().getResource("worldmap.png");
			    			earthImage = ImageIO.read(imgURL);
		    			}
		    			 aGraphics.drawImage(earthImage, 0, 0, panelWidth, panelHeight-70, this);
		    	    }
		    	    catch (IOException e) {
		    	    	e.printStackTrace();
		    	    }
			        
		    	}
			 } catch(Exception exception) {
				        	
			}
	    }
	
	
	
	private void drawTransparentImage(Graphics g) {    
	    RescaleOp rop = new RescaleOp(scales, offsets, null);
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.drawImage(bufferedImage, rop, 0, 0);
	  }
	  
	  private void drawGrid(Graphics g) {
	    g.setColor(Color.black);
	    
	    //draw longitude lines
	    for(int x = 0; x <= imgWidth; x += pixelsPerCellX) {
	      g.drawLine(x, 0, x, imgHeight);      
	    }
	    
	    //draw scaled latitude lines
	    for(int lat = 0; lat <= 90; lat += degreeSeparation) {
	      int y = (int)EarthMath.getDistToEquator(lat, radius);
	      g.drawLine(0, radius-y, imgWidth, radius-y);
	      g.drawLine(0, radius+y, imgWidth, radius+y);
	    }
	    
	    //nadimetla - change display color to red
	    g.setColor(Color.red);
	    g.drawLine(imgWidth/2, 0, imgWidth/2, imgHeight); //prime meridian
	    g.drawLine(0, imgHeight/2, imgWidth, imgHeight/2); // equator
	  }
	  
	  public int getWidth() {
		    return imgWidth;
		  }
		  
		  private void fillCellColors(Graphics g) {
		    int cellX=0, cellY=0;
		    int cellWidth = pixelsPerCellX;
		    
		    int noonCellRowID = earthSurface.getNoonCellRowID();
		    int noonCellColumnID = earthSurface.getNoonCellColumnID();
		    
		    for (int x = 0; x < numCellsX; x++) {      
		      for (int y = 0; y < numCellsY; y++) {
		    	  
		        double newTemp = earthSurface.surface[y][x].getCurrentTemp();
		        int colorValue = new Double(newTemp).intValue();
		        //int cellHeight = (int)grid.getCellHeight(x, y);
				int de1 = (int)EarthMath.getDistToEquator(90 - (y * degreeSeparation), radius);
				int de2 = (int)EarthMath.getDistToEquator(90 - ((y + 1) * degreeSeparation), radius);
				int cellHeight = de1 - de2;

		        
		        g.setColor(GridColorPicker.getColor(colorValue));
		        g.fillRect(cellX, cellY, cellWidth, cellHeight);
		      
		        if (((noonCellRowID-1) == y) &&
		        	((noonCellColumnID-1) == x)) {
		        
			        g.setColor(Color.YELLOW);
			        g.fillOval(cellX, cellY, cellWidth, cellHeight);
		        }
		        
		        cellY += cellHeight;
		        
//		        System.out.print("\n["+x+", "+y+"] color: " + colorValue);
		      }      
		      cellX += cellWidth;
		      cellY = 0;
		    }
		  }
		  
		  int getRadius() {
			    return radius;
			  }
	private int panelWidth;
	private int panelHeight;
	
		private BufferedImage earthImage;
		private BufferedImage bufferedImage;
		private EarthSurface earthSurface;
		
		
		  private static final long serialVersionUID = -1108120968981962997L;
		  private static final float OPACITY = 0.65f;  
		  private static final int DEFAULT_CELL_TEMP = 15; //degrees in celsius
		  private GridColorPicker colorPicker = GridColorPicker.getInstance();
		  
		  
		  private float[] scales = { 1f, 1f, 1f, OPACITY }; //last index controls the transparency
		  private float[] offsets = new float[4];
		  private int degreeSeparation;
		  private int pixelsPerCellX; //number of pixels per latitudal division
		  private int pixelsPerCellY; //number of pixels per longitudal division
		  private int imgWidth; // in pixels
		  private int imgHeight; // in pixels
		  private int numCellsX;
		  private int numCellsY;
		  private int radius;
		  private boolean paintInitialColors = true;
	    
}
