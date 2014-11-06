package simulation;

import presentation.earth.TemperatureGrid;

public class Grid implements TemperatureGrid {

	private int curCol;
	private int curRow;
	private int rows;
	private int cols;
	private int gs_deg; // gridSpcing
	private Cell[][] myGrid;

	public int getSpacing() {
		return gs_deg;
	}

	public Grid(Grid other) {
		this.initialize(other.getSpacing(), 0);

		// The temperature is the only thing that differentiates one grid from
		// another
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.myGrid[i][j].setTemperature(other.getCell(i, j)
						.getTemperature());
			}
		}

		this.reset();
	}

	public Grid copy() {
		return new Grid(this);
		// return this;
	}

	public Grid(int degSpacing, float temp_K) {
		this.initialize(degSpacing, temp_K);
	}

	public void initialize(int degSpacing, float temp_K) {
		gs_deg = degSpacing;
		// The number of rows in the grid:360/2
		cols = SimConstant.MAX_GRID_WIDTH / degSpacing;

		// The number of rows in the grid:180/2
		rows = cols / 2;

		curCol = 0;
		curRow = 0;

		myGrid = new Cell[rows][cols];

		// Compute parameters for cells

		float p_deg = (float) gs_deg / 360;
		float lenVer = SimConstant.C * p_deg;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				myGrid[i][j] = new Cell();

				// float latitude = (i + (float)0.5 - (float)rows/2) * gs;
				// float longitude = (j - ((float)cols/2)) * gs;
				float lat_deg = (i - (rows / 2)) * gs_deg;
				float long_deg = (j - (cols / 2)) * gs_deg;

				// float lenBot = SimConstant.C *
				// (float)Math.cos((latitude-0.5*gs)*Math.PI/180) * p;
				float lenBot = (float) (2 * Math.PI * SimConstant.R
						* Math.cos(Math.toRadians(lat_deg)) * p_deg);

				// float lenTop = SimConstant.C *
				// (float)Math.cos((lat_deg-0.5*gs + gs)*Math.PI/180) * p;
				float lenTop = (float) (2 * Math.PI * SimConstant.R
						* Math.cos(Math.toRadians(lat_deg + gs_deg)) * p_deg);

				float h = (float) Math.sqrt(Math.pow(lenVer, 2) - 0.25
						* Math.pow(lenBot - lenTop, 2));

				float perimeter = lenTop + lenBot + 2 * lenVer;

				float area = (float) 0.5 * (lenTop + lenBot) * h;

				float cellProportion = area / SimConstant.A;

				myGrid[i][j].setTemperature(temp_K);
				myGrid[i][j].setLatitude(lat_deg);
				myGrid[i][j].setLongitude(long_deg);
				myGrid[i][j].setTopLength(lenTop);
				myGrid[i][j].setBottomLength(lenBot);
				myGrid[i][j].setLeftLength(lenVer);
				myGrid[i][j].setRightLength(lenVer);
				myGrid[i][j].setSurfaceArea(area);
				myGrid[i][j].setPerimeter(perimeter);
				myGrid[i][j].setCellHeight(h);
				myGrid[i][j].setCellProportion(cellProportion);
			}
		}

		// for (int i = 0; i < rows; i++) {
		// for (int j = 0; j < cols; j++) {
		// // Set the neighbors
		// myGrid[i][j].setTop( myGrid[Math.min(i,rows-1)] [j] ); // North
		// myGrid[i][j].setBottom( myGrid[Math.max(i,0)] [j] ); // South
		// myGrid[i][j].setLeft( myGrid[i] [(j + 1) % cols] ); // West
		// myGrid[i][j].setRight( myGrid[i] [(cols + j - 1) % cols] ); // East
		// }
		// }

		// Setting links from here

		for (int j = 0; j < cols / 2; j++) {
			myGrid[0][j].setTop(myGrid[0][j + (cols / 2)]);
			myGrid[0][j + (cols / 2)].setTop(myGrid[0][j]);
			myGrid[rows - 1][j].setBottom(myGrid[rows - 1][j + (cols / 2)]);
			myGrid[rows - 1][j + (cols / 2)].setBottom(myGrid[rows - 1][j]);
		}

		myGrid[0][0].setLeft(myGrid[0][cols - 1]);
		myGrid[0][cols - 1].setRight(myGrid[0][0]);

		for (int i = 1; i < rows; i++) {
			myGrid[i][0].setLeft(myGrid[i][cols - 1]);
			myGrid[i][cols - 1].setRight(myGrid[i][0]);
			myGrid[i][0].setTop(myGrid[i - 1][0]);
			myGrid[i - 1][0].setBottom(myGrid[i][0]);
		}

		for (int j = 1; j < cols; j++) {
			myGrid[0][j].setLeft(myGrid[0][j - 1]);
			myGrid[0][j - 1].setRight(myGrid[0][j]);
		}

		for (int i = 1; i < rows; i++) {
			for (int j = 1; j < cols; j++) {
				myGrid[i][j].setLeft(myGrid[i][j - 1]);
				myGrid[i][j - 1].setRight(myGrid[i][j]);
				myGrid[i][j].setTop(myGrid[i - 1][j]);
				myGrid[i - 1][j].setBottom(myGrid[i][j]);
			}
		}

		// Setting links until here

	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return cols;
	}

	public Cell getCurrent() {
		return myGrid[curRow][curCol];
	}

	public boolean next() {
		if (curCol + 1 == cols) {
			curCol = 0;

			if (curRow + 1 == rows) {
				reset();
				return false;
			} else {
				curRow++;
			}
		} else {
			curCol++;
		}

		return true;
	}

	public void reset() {
		curCol = 0;
		curRow = 0;
	}

	public float getAverageTemp() {
		float avgTemp = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				avgTemp += myGrid[r][c].getTemperature();
			}
		}
		int nCell = rows * cols;
		if (nCell != 0) {
			avgTemp /= nCell;
		}
		return avgTemp;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				buffer.append(myGrid[r][c].getTemperature() - 273);
				buffer.append("\t");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	public String printLatitude() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Latitude:\n");
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				buffer.append(myGrid[r][c].getLatitude());
				buffer.append("\t");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	public String printTopLatitude() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Top Latitude:\n");
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				buffer.append(myGrid[r][c].getBottomLength());
				buffer.append("\t");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	public String printLongitude() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Longitude:\n");
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				buffer.append(myGrid[r][c].getLongitude());
				buffer.append("\t");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	public String printSurfaceArea() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("SurfaceArea:\n");
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				buffer.append(myGrid[r][c].getSurfaceArea());
				buffer.append("\t");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	public String printTopLen() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("TopLen:\n");
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				buffer.append(myGrid[r][c].getTopLength());
				buffer.append("\t");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	public String printCellHeight() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Cell Height:\n");
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				buffer.append(myGrid[r][c].getCellHeight());
				buffer.append("\t");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	private Cell getCell_flippedIndeces(int x, int y) {
		return myGrid[y][x];
	}

	// This function uses flipped indeces... it is here just for
	// support of the Temperature Grid interface used by the earth panel

	// The cell height should be the proportion of cell in grid display
	// with the total image height
	public float getCellHeight(int x, int y) {
		Cell cell = getCell_flippedIndeces(x, y);
		float latitudeTop = cell.getLatitude();
		float latitudeBot = latitudeTop + (float) getSpacing();
		float height = (float) (Math.sin(Math.toRadians(latitudeTop)) - Math
				.sin(Math.toRadians(latitudeBot))) / 2;
		height = (float) Math.abs(height);
		return height;
	}

	public Cell getCell(int x, int y) {
		return myGrid[x][y];
	}

	// This function uses flipped indeces... it is here just for
	// support of the Temperature Grid interface used by the earth panel
	public double getTemperature(int x, int y) {
		return getCell_flippedIndeces(x, y).getTemperature() - 273.15;
	}
}