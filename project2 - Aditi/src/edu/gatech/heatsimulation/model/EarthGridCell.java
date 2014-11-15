package edu.gatech.heatsimulation.model;

import java.lang.*;

public class EarthGridCell implements GridCell {
    
    private final int C = 40030140;     //Earth's circumference
    //private int row_id;
    //private int col_id;
    
    private double latitude;
    private double longitude;
    
    private double height;
    private double baseLen;
    private double topLen;
    private double sideLen;
    private double perimeter;
    private double area;
    
    private double prevTemp;
    private double currentTemp;

    
    public EarthGridCell(int gs, int rowID, int colID, int numRows, int numCols, double defaultTemp){
        //System.out.print("Gridspace is \t");
        //System.out.println(gs);
        //row_id = row;
        //col_id = col;
        prevTemp = defaultTemp;
        currentTemp = defaultTemp;
    
        setLatitude(rowID, numRows, gs);
        setLongitude(colID, numCols, gs);

        setPerimeter(gs);
        setArea();
        //display();   //to test cell basics
    }

    void setLatitude(int rowID, int numRows, int gs){
        latitude = (double)(rowID - (numRows/2.0))*gs;
        //return latitude;
    }

    void setLongitude(int colID, int numCols, int gs){
        if (colID < (numCols/2))        
            longitude = (double) -(colID + 1.0)*gs;
        else
            longitude = (double) 360.0 - ((colID + 1.0)*gs);
        //return longitude;
    }
    
    void setPerimeter(int gs){
        sideLen = (double) C * (double)(gs/360.0);

        /*if ((int)(Math.cos(Math.toRadians(latitude)) * 1000) == 0)
            baseLen = 0.0;
        else*/     
            baseLen = (Math.cos(Math.toRadians(latitude))) * sideLen;

        /*if ((int)(Math.cos(Math.toRadians(latitude+ (double)gs))) == 0)
            topLen = 0;
        else*/
            topLen = (Math.cos(Math.toRadians(latitude+ (double)gs))) * sideLen;

        perimeter = baseLen + topLen + (2.0 * sideLen);
        /*System.out.print("Latitude ");
        System.out.println(latitude);
        System.out.print("Longitude ");
        System.out.println(longitude);
        System.out.print("Latitude Radian");
        System.out.println(Math.toRadians(latitude));
        System.out.print("Longitude Radian");
        System.out.println(Math.toRadians(longitude));
        System.out.print("Cosine Latitude Radian");
        System.out.println(Math.cos(Math.toRadians(latitude)));
        System.out.print("Cosine Longitude Radian");
        System.out.println(Math.cos(Math.toRadians(longitude)));*/
        
        
        //return perimeter;
    }
    
    void setArea(){
        height = Math.sqrt((sideLen * sideLen) - ((1.0/4.0)*(baseLen-topLen)*(baseLen-topLen)));
        /*double temp;
        temp = -topLen + baseLen + (2*sideLen);
        temp = temp * (topLen - baseLen + (2*sideLen));
        temp = temp * (topLen - baseLen) * (topLen - baseLen);
        temp = (Math.sqrt(temp))/2.0;
        if ((baseLen - topLen) < 0)
            height = temp/(topLen - baseLen);
        else if ((baseLen - topLen) > 0)
            height = temp/(baseLen - topLen);
        else
            height = 0.0;*/

        area = (1.0/2.0)*(baseLen+topLen)*height;

        /*System.out.print("Area ");
        System.out.println(area);        
        System.out.print("Base Length ");
        System.out.println(baseLen);
        System.out.print("Top Length ");
        System.out.println(topLen);
        System.out.print("Side Length ");
        System.out.println(sideLen);
        System.out.print("Height ");
        System.out.println(height);*/
        //return area;
    }

    public void setCurrentTemp(double temp){
        currentTemp = temp;
    }

    public void setPrevTemp(double temp){
        prevTemp = temp;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getTopLen(){
        return topLen;
    }

    public double getBaseLen(){
        return baseLen;
    }

    public double getSideLen(){
        return sideLen;
    }

    public double getPerimeter(){
        return perimeter;
    }

    public double getPrevTemp(){
        return prevTemp;
    }

    public double getCurrentTemp(){
        return currentTemp;
    }

    public double getArea(){
        return area;
    }

    public void display(){

        
        /*System.out.print("Row Id is \t");
        System.out.println(row_id);
        System.out.print("Col Id is \t");
        System.out.println(col_id);*/
        System.out.print("Latitude is \t");
        System.out.println(latitude);
        System.out.print("Longitude is \t");
        System.out.println(longitude);
        System.out.print("Height is \t");
        System.out.println(height);
        System.out.print("Top Length is \t");
        System.out.println(topLen);  
        System.out.print("Base Length is \t");
        System.out.println(baseLen);
        System.out.print("Side length is \t");
        System.out.println(sideLen);
        System.out.print("Perimeter is \t");
        System.out.println(perimeter);
        System.out.print("Area is \t");
        System.out.println(area);
        System.out.print("Current Temp is \t");
        System.out.println(currentTemp);
        System.out.print("Previous Temp is \t");
        System.out.println(prevTemp);
        System.out.println("************************");
    
    }    
}
  

/*public class GridCell{

    public static void main(String[] args){

        Cell C = new Cell(5,10,10,20,20);
        //C.display();
        //new Cell(5,10,10,(double) 6.5);
    }
    
   }*/
