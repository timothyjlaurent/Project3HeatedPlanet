package edu.gatech.heatsimulation.service;

import java.lang.*;
import java.io.*;

import edu.gatech.heatsimulation.model.EarthSurface;

public class EarthHeating{

    private final double T_SUN_DEFAULT = 278.0;
    private final int C = 40030140;     //Earth's circumference
    //private final double A = (510072 * (int)Math.pow(10.0,9.0));     //Earth's area
    //private final double A = 510072000000000;   

    //private int simTimeStep;
    private int gridSpace;  // Separation between consective longitudes or latitudes
    private int numRows;    // Number of rows in the grid
    private int numCols;    // Number of columns in the grid
    private int numCells;   // Number of cells in the grid
    //private double p;       // Portion of the Circumeference covered by one gridszie space
    //private double avgGridCellSize;
    //private double angleofRadiation;
    public EarthSurface earth;
    
    public double test_temp; //FOR TESTING ONLY . SHD BE REMOVED FOR FINAL CODE.
    
    /*public File file;
    public FileWriter writer, writer1;*/   
    public EarthHeating(){
    // temp need to remove
    
    }
    public boolean init(int gs, int simulationStep){

        setGridSpace(gs);
        //p = gridSpace/360.0;
        iteration = 0;
        this.simulationStep = simulationStep;
        //simTimeStep = tStep;

        numCols = 360/gridSpace;
        numRows = 180/gridSpace;
        numCells = numRows * numCols;
        //avgGridCellSize = A/numCells;        
        this.earth = new EarthSurface(gridSpace,numRows,numCols, 288.0);
        //fileSetup();
        timeLapsed = 0;
        //simTimeStep = tStep;
        //display();
        return true;
    }

    /*void fileSetup(){

        try {
            file = new File("/home/ubuntu/Documents/Project 2");
            FileWriter writer = new FileWriter("/home/ubuntu/Documents/Project 2/results.csv", true);
        
            writer.append("TempSun ");
            writer.append(",");
            writer.append("Expected ");
            writer.append(",");
            writer.append("TempCool ");
            writer.append(",");
            writer.append("Expected ");
            writer.append("\n");
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

        }*/

    void setGridSpace(int gs){
        int gsTemp = 1;
        while (gsTemp <= gs){
            if((180 % gsTemp) == 0){
                gridSpace = gsTemp;
            }
            gsTemp++;
        }
    
        /*System.out.print("Submitted GS is \t");
        System.out.println(gs);    
        System.out.print("Calculated GS is \t");
        System.out.println(gridSpace);*/
    }
    
    double avgCellTemp(){
        double sum = 0;        
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                sum = sum + this.earth.surface[i][j].getPrevTemp();
            }
        }     
   
        return (double) (sum/numCells);
    }
    
    double avgCellArea(){
        double sum = 0;        
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                sum = sum + this.earth.surface[i][j].getArea();
            }
        }     
        
        //System.out.print("Area  "); 
        //double temp =  (double) (sum/A)*100.0;
        //System.out.format("%2.2f",temp);    
        //System.out.println(sum);
        //System.out.println(A);

        return (double) (sum/numCells);
    }
    
    double latAttn(int rowID, int colID){
        double lat = this.earth.surface[rowID][colID].getLatitude();        
        return Math.cos(Math.toRadians(lat));
        //return 0.0;

    }

    double longAttn(int rowID, int colID, int timeLapsed){
        double lon = this.earth.surface[rowID][colID].getLongitude();
        double d;
        double dNoon;
        double angleRotation;

        angleRotation = (timeLapsed % 1440.0) * (360.0/1440.0);        

        if (lon >= 0)
            d = 360 - lon;
        else
            d = -lon;

        dNoon = d - angleRotation;
        if (dNoon < 0)
            dNoon = -dNoon;      
         
        if (dNoon < 90)
            return Math.cos(Math.toRadians(dNoon));
        else
            return 0.0;     
    }

    
    double tSun(int rowID, int colID, int timeLapsed){
        double temp;
        temp = (T_SUN_DEFAULT) * (latAttn(rowID,colID)) * (longAttn(rowID,colID,timeLapsed));
        /*System.out.print("T Sun ");
        System.out.println(temp);*/
        return temp;
    }

    /*double tCool(int rowID, int colID){
        //double avgGridCellSize = (double) A/numCells;     //Does NOT work. Better to calculate using avgCellArea() function.
        double avgGridCellSize = avgCellArea();
        double cellArea = this.earth.surface[rowID][colID].getArea();        
        //double relSizeFactor = Math.log(cellArea/avgGridCellSize);
        double relSizeFactor = cellArea/avgGridCellSize;
        double gamma = this.earth.surface[rowID][colID].getPrevTemp()/avgCellTemp();
        double temp = (relSizeFactor * gamma * (this.earth.surface[rowID][colID].getPrevTemp()));
        //double temp = (relSizeFactor * gamma * T_SUN_DEFAULT);
        //double temp = tSun( rowID, colID,  timeLapsed);
        
        
        return temp;
    }*/
    
    double lon(int rowID, int colID, int timeLapsed){
        double lon = this.earth.surface[rowID][colID].getLongitude();
        double d;
        double dNoon;
        double angleRotation;

        angleRotation = (timeLapsed % 1440.0) * (360.0/1440.0);        

        if (lon >= 0)
            d = 360 - lon;
        else
            d = -lon;

        dNoon = d - angleRotation;
        if (dNoon < 0)
            dNoon = -dNoon;      
         
        double temp = Math.cos(Math.toRadians(dNoon));
        if (temp >= 0.0)
            return temp;
        else
            return -temp;     
    }

    double tCool(int rowID, int colID, int timeLapsed){
        
        double cellArea = this.earth.surface[rowID][colID].getArea();
        double prevTemp = this.earth.surface[rowID][colID].getPrevTemp();
        double longitude = this.earth.surface[rowID][colID].getLongitude();
        double latitude = this.earth.surface[rowID][colID].getLatitude();
        //double angleRotation = (timeLapsed % 1440.0) * (360.0/1440.0);

          
        double lat = Math.cos(Math.toRadians(latitude)); 
        //double lon = Math.cos(Math.toRadians(longitude));
        double lon = lon(rowID,colID,timeLapsed);        
        double relSizeFactor = (lon * lat)/(2*cellArea);
        double gamma = 278.0/prevTemp;
        double temp = relSizeFactor * gamma * cellArea * prevTemp;
        //double temp = (relSizeFactor * gamma * T_SUN_DEFAULT);
        //double temp = tSun( rowID, colID,  timeLapsed);
        return temp;
    }

    double tDiffusion(int rowID, int colID){
        double topLen = this.earth.surface[rowID][colID].getTopLen(); 
        double sideLen = this.earth.surface[rowID][colID].getSideLen();
        double baseLen = this.earth.surface[rowID][colID].getBaseLen();
        double perimeter = this.earth.surface[rowID][colID].getPerimeter();

        double tNghbr;
        double pNghbr;

        double tDiffusion = 0.0;

        //West neighbor

        tNghbr = this.earth.surface[rowID][(colID + 1) % numCols].getPrevTemp();
        pNghbr = sideLen/perimeter;

        /*System.out.print("West neighbor Border Portion");
        System.out.println(pNghbr);*/

        tDiffusion = tDiffusion + (pNghbr*tNghbr);

        //East Neighbor

        tNghbr = this.earth.surface[rowID][(colID - 1 + numCols) % numCols].getPrevTemp();
        pNghbr = sideLen/perimeter;

        /*System.out.print("East neighbor Border Portion");
        System.out.println(pNghbr);*/
        tDiffusion = tDiffusion + (pNghbr*tNghbr);
       
        //North Neighbor
        
        if ((rowID + 1) <= (numRows - 1))        
            tNghbr = this.earth.surface[rowID + 1][colID].getPrevTemp();
        else
            tNghbr = this.earth.surface[rowID][(colID + (numCols/2)) % numCols].getPrevTemp();
            
        /*if (((int)(topLen/perimeter)*1000) == 0)
            pNghbr = 0.0;
        else   */             
            pNghbr = topLen/perimeter;
        /*System.out.print("North neighbor Border Portion");
        System.out.println(pNghbr);*/
        tDiffusion = tDiffusion + (pNghbr*tNghbr);

        //South Neighbor
        
        if ((rowID - 1) >= 0)        
            tNghbr = this.earth.surface[rowID - 1][colID].getPrevTemp();
        else
            tNghbr = this.earth.surface[rowID][(colID + (numCols/2))% numCols].getPrevTemp();
        
        /*if (((int)(topLen/perimeter)*1000) == 0)
            pNghbr = 0.0; 
        else    */                
            pNghbr = baseLen/perimeter;
        /*System.out.print("South neighbor Border Portion");
        System.out.println(pNghbr);*/ 

        tDiffusion = tDiffusion + (pNghbr*tNghbr);

        /*System.out.print("tDiffusion ");
        System.out.println(tDiffusion);*/

        return tDiffusion;
    }    
    
    void swapTemp(){
        double temp;
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                temp = this.earth.surface[i][j].getCurrentTemp();  
                this.earth.surface[i][j].setPrevTemp(temp);
            }
        }
    }
    

    public void updateTemp(){
    	
    	iteration++;
    	timeLapsed = simulationStep * iteration;
    	
        double cellPrevtemp;
        double tempDiffusion;
        double tempSun;
        double tempCool; 
        double tempNew = 0.0;
        double tempDiffSun = 0.0;
        double tempDiffNghbr = 0.0;
        swapTemp();
        
        for(int rowID=0; rowID<numRows; rowID++){
            for(int colID=0; colID<numCols; colID++){

                cellPrevtemp = this.earth.surface[rowID][colID].getPrevTemp();
                tempDiffusion = tDiffusion(rowID, colID);
                tempSun = this.tSun(rowID, colID, timeLapsed); //(1/33.25966317)
                //tempCool = this.tCool(rowID, colID);
                tempCool = this.tCool(rowID, colID, timeLapsed);

                /*if (((int) (tempSun * 1000)) == 0)
                    tempSun = 0.0;
                if (((int) (tempCool * 1000)) == 0)
                    tempCool = 0.0;*/

                /*
                System.out.print("TempSun ");
                System.out.print(tempSun);
                System.out.print(",");
                System.out.print("\t Expected ");
                double temp;
                if (tempSun > 0)
                    temp = 278.0/(numCells);
                else
                    temp = 0.0;

                System.out.print(temp);
                System.out.print(",");
                System.out.print(" \t TempCool ");
                System.out.print(tempCool);
                System.out.print("\t Expected ");
                temp = (278.0/(numCells*2));
                System.out.println(temp);
                */

                /*if (tempSun > 0)
                    tempSun = 278.0/(numCells);         
                
                
                tempCool = 278.0/(numCells*2);*/
                //System.out.println(tempSun);
                tempNew = cellPrevtemp + tempSun -  tempCool;
                tempNew = (tempNew + tempDiffusion)/2.0;
                /*System.out.print("TempNew ");
                System.out.print(tempNew);
                System.out.print(" \t TempDiffusion ");
                System.out.println(tempDiffusion);*/
                this.earth.surface[rowID][colID].setCurrentTemp(tempNew);
            }
        } 
        
        earth.setNoonCell(getNoonRowID(), getNoonColID());
        earth.setHeatingTimeElapsed(timeLapsed);
    }

    public int getNoonColID(){
        double angleRotation;
        int j;

        angleRotation = (timeLapsed % 1440.0) * (360.0/1440.0);
        j = (int) ((numCols * (angleRotation/360.0)) + (numCols) % numCols);
        return j;
    }

    public int getNoonRowID(){
        return numRows/2;
    }

    public void display(){
        
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                System.out.print(this.earth.surface[i][j].getCurrentTemp());
                System.out.print("\t");
            }
            System.out.print("\n");
        }
        //System.out.print("Latitude for Cell 0 0 is");
        //System.out.println(this.earth.surface[0][0].getLatitude());
    }

    /*public static void main(String[] args){

        EarthSimulation e = new EarthSimulation(90, 2);   
        //E.display();     
    }*/
    
    private int iteration;
    private int simulationStep;
    private int timeLapsed;
}
