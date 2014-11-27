package cs6310.gui.widget.earth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Set;

import javax.swing.JPanel;

import models.GridPoint;
import util.SimulationUtil;

/**
 * Use this class to display an image of the earth with a grid drawn on top. All
 * the methods that could be used to update the grid are given package level
 * access - these methods should be interacted with from the {@link EarthPanel}.
 * 
 * @author Andrew Bernard
 */
public class EarthGridDisplay extends JPanel {
	private static final long serialVersionUID = -1108120968981962997L;
	private static final float OPACITY = 0.65f;
	private static final int DEFAULT_CELL_TEMP = 15; // degrees in celsius
	private final TemperatureColorPicker colorPicker = TemperatureColorPicker.getInstance();

	private BufferedImage imgTransparent;
	private final BufferedImage earthImage;
	private final float[] scales = { 1f, 1f, 1f, OPACITY }; // last index
															// controls the
															// transparency
	private final float[] offsets = new float[4];
	private int degreeSeparation;
	private int pixelsPerCellX; // number of pixels per latitudal division
	private int pixelsPerCellY; // number of pixels per longitudal division
	private int imgWidth; // in pixels
	private int imgHeight; // in pixels
	private int numCellsX;
	private int numCellsY;
	private int radius;
	private boolean paintInitialColors = true;
	private Set<GridPoint> gridPoints;

	/**
	 * Constructs a display grid with a default grid spacing.
	 * 
	 * @param defaultGridPacing
	 *            in degrees
	 */
	EarthGridDisplay(final int defaultGridPacing) {
		earthImage = new EarthImage().getBufferedImage();
		setGranularity(defaultGridPacing);
		setIgnoreRepaint(false);
	}

	/**
	 * Sets the granularity of the grid.
	 * 
	 * @param degreeSeparation
	 *            the latitude and longitude degree separations between the
	 *            cells in the grid
	 */
	void setGranularity(final int degreeSeparation) {
		this.degreeSeparation = degreeSeparation;

		numCellsX = 360 / degreeSeparation;
		pixelsPerCellX = earthImage.getWidth() / numCellsX;
		imgWidth = numCellsX * pixelsPerCellX;

		numCellsY = 180 / degreeSeparation;
		pixelsPerCellY = earthImage.getHeight() / numCellsY;
		imgHeight = numCellsY * pixelsPerCellY;
		radius = imgHeight / 2;

		// create an image capable of transparency; then draw our image into it
		imgTransparent = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		final Graphics g = imgTransparent.getGraphics();
		g.drawImage(earthImage, 0, 0, imgWidth, imgHeight, null);
	}

	@Override
	public void paint(final Graphics g) {
		// the order in which these are called does matter
		if (paintInitialColors) {
			initCellColors(g);
		} else {
			fillCellColors(g);
		}
		drawTransparentImage(g);
		drawGrid(g);
	}

	private void initCellColors(final Graphics g) {
		g.setColor(colorPicker.getColor(273, 272, 274));
		g.fillRect(0, 0, imgWidth, imgHeight);
	}

	/**
	 * Updates the display with the values from the temperature grid.
	 * 
	 * @param grid
	 *            the grid to get the new temperature values from
	 */
	void updateGrid(final Set<GridPoint> gridPoints) {
		this.gridPoints = gridPoints;
		paintInitialColors = false;
		imgTransparent = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		final Graphics g = imgTransparent.getGraphics();
		g.drawImage(earthImage, 0, 0, imgWidth, imgHeight, null);
		this.repaint();
	}

	/**
	 * Gets the radius of the earth.
	 * 
	 * @return the radius of the earth in pixels
	 */
	int getRadius() {
		return radius;
	}

	/**
	 * This is used implicitly by swing to do it's layout job properly
	 */
	@Override
	public int getWidth() {
		return imgWidth;
	}

	private void fillCellColors(final Graphics g) {

		final GridPoint[][] gridPointGrid = SimulationUtil.convertSetToGrid(gridPoints, degreeSeparation);
		int cellX = 0, cellY = 0;
		final int cellWidth = pixelsPerCellX;

		final double[] minMax = getMinMax(gridPointGrid);

		for (int x = 0; x < numCellsX; x++) {
			for (int y = 0; y < numCellsY; y++) {
				final double newTemp = gridPointGrid[x][y].getTemperature();
				final int colorValue = new Double(newTemp).intValue();
				final int cellHeight = getCellHeight(y);

				g.setColor(colorPicker.getColor(colorValue, minMax[0], minMax[1]));
				g.fillRect(cellX, cellY, cellWidth, cellHeight);
				cellY += cellHeight;
			}
			cellX += cellWidth;
			cellY = 0;
		}
	}

	private double[] getMinMax(final GridPoint[][] gridPointGrid) {
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		for (final GridPoint[] element : gridPointGrid) {
			for (int j = 0; j < gridPointGrid[0].length; j += 1) {
				min = Math.min(min, element[j].getTemperature());
				max = Math.max(max, element[j].getTemperature());
			}
		}

		final double[] minMax = { min, max };
		return minMax;
	}

	private int getCellHeight(final int col) {
		final int lat1 = 90 - (col * degreeSeparation);
		final int lat2 = 90 - ((col + 1) * degreeSeparation);

		final int y1 = (int) Util.getDistToEquator(lat1, radius);
		final int y2 = (int) Util.getDistToEquator(lat2, radius);
		return Math.abs(y1 - y2);
	}

	private void drawTransparentImage(final Graphics g) {
		final RescaleOp rop = new RescaleOp(scales, offsets, null);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(imgTransparent, rop, 0, 0);
	}

	private void drawGrid(final Graphics g) {
		g.setColor(Color.black);

		// draw longitude lines
		for (int x = 0; x <= imgWidth; x += pixelsPerCellX) {
			g.drawLine(x, 0, x, imgHeight);
		}

		// draw scaled latitude lines
		for (int lat = 0; lat <= 90; lat += degreeSeparation) {
			final int y = (int) Util.getDistToEquator(lat, radius);
			g.drawLine(0, radius - y, imgWidth, radius - y);
			g.drawLine(0, radius + y, imgWidth, radius + y);
		}

		g.setColor(Color.blue);
		g.drawLine(imgWidth / 2, 0, imgWidth / 2, imgHeight); // prime meridian
		g.drawLine(0, imgHeight / 2, imgWidth, imgHeight / 2); // equator
	}

	/**
	 * Sets the opacity of the map image on a scale of 0 to 1, with 0 being
	 * completely transparent.
	 * 
	 * @param value
	 *            the opacity value
	 */
	void setMapOpacity(final float value) {
		scales[3] = value;
	}

	void reset() {
		paintInitialColors = true;
	}

}
