package edu.gatech.heatsimulation.model;

public class EarthSurface implements Surface {

    public EarthGridCell [][] surface;

    public EarthSurface(int gs, int numRows, int numCols, double defaultTemp){
        
        this.surface = new EarthGridCell[numRows][numCols];
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                this.surface[i][j] = new EarthGridCell(gs,i,j,numRows,numCols,defaultTemp);
            }
        }
    }
    
        
    public boolean setNoonCell(int noonCellRowID,
    						   int noonCellColumnID) {
		
    	if ((noonCellRowID < 0) ||
    		(noonCellColumnID < 0)) {
    		
    	}
    	this.noonCellRowID = noonCellRowID;
		this.noonCellColumnID = noonCellColumnID;
		return true;
	}
	
    public int getHeatingTimeElapsed() {
		return heatingTimeElapsed;
	}

    public int setHeatingTimeElapsed(int heatingTimeElapsed) {
		return this.heatingTimeElapsed = heatingTimeElapsed;
	}
    
	public int getNoonCellRowID() {
		return noonCellRowID;
	}


	public int getNoonCellColumnID() {
		return noonCellColumnID;
	}

	private int noonCellRowID;   
	private int noonCellColumnID;
	private int heatingTimeElapsed;
}
