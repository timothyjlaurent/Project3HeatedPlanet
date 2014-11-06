package simulation;

public class Cell {

	private float temperature;
	private float surfaceArea;

	private Cell top;
	private Cell bottom;
	private Cell right;
	private Cell left;

	private float topLength;
	private float bottomLength;
	private float rightLength;
	private float leftLength;

	private float latitude;
	private float longitude;

	private float perimeter;
	private float cellHeight;
	private float cellProportion;

	public Cell() {

	}

	// Latitude, Longitude

	public void setLatitude(float lat) {
		this.latitude = lat;
	}

	public void setLongitude(float lon) {
		this.longitude = lon;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	// Temperature

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temp) {
		this.temperature = temp;
	}

	// Surface Area

	public float getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(float area) {
		surfaceArea = area;
	}

	// Neighboring Cell Functions

	public Cell getTop() {
		return top;
	}

	public Cell getBottom() {
		return bottom;
	}

	public Cell getLeft() {
		return left;
	}

	public Cell getRight() {
		return right;
	}

	public void setTop(Cell point) {
		top = point;
	}

	public void setBottom(Cell point) {
		bottom = point;
	}

	public void setLeft(Cell point) {
		left = point;
	}

	public void setRight(Cell point) {
		right = point;
	}

	// Neighboring Length Functions

	public float getTopLength() {
		return topLength;
	}

	public float getBottomLength() {
		return bottomLength;
	}

	public float getLeftLength() {
		return leftLength;
	}

	public float getRightLength() {
		return rightLength;
	}

	public void setTopLength(float len) {
		topLength = len;
	}

	public void setBottomLength(float len) {
		bottomLength = len;
	}

	public void setLeftLength(float len) {
		leftLength = len;
	}

	public void setRightLength(float len) {
		rightLength = len;
	}

	// Perimeter

	public void setPerimeter(float perimeter) {
		this.perimeter = perimeter;
	}

	public float getPerimeter() {
		return perimeter;
	}

	// Proportion of earth surface area taken by the cell

	public void setCellProportion(float prop) {
		cellProportion = prop;
	}

	public float getCellProportion() {
		return cellProportion;
	}

	// Cell height

	public void setCellHeight(float h) {
		cellHeight = h;
	}

	public float getCellHeight() {
		return cellHeight;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(this.getTemperature());

		return buffer.toString();
	}
}