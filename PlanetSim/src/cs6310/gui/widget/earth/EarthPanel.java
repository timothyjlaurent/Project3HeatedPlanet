package cs6310.gui.widget.earth;

import java.awt.Component;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import models.GridPoint;

/**
 * A {@link JPanel} composed of the the earth and sun display components.
 * 
 * @author Andrew Bernard
 */
public class EarthPanel extends JPanel {

	private static final long serialVersionUID = -1108120537851962997L;
	private final SunDisplay sunDisplay;
	private final EarthGridDisplay earth;

	/**
	 * Constructor - sets up the panel with the earth and sun display components
	 * using a {@link BoxLayout} with {@link BoxLayout#PAGE_AXIS}.
	 */
	public EarthPanel(final int gridSpacing) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		earth = new EarthGridDisplay(gridSpacing);
		earth.setAlignmentX(Component.LEFT_ALIGNMENT);

		sunDisplay = new SunDisplay(earth.getWidth());
		sunDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(sunDisplay);
		add(earth);
	}

	/**
	 * Draws the grid.
	 * 
	 * @param degreeSeparation
	 *            the latitude and longitude degree separations between the
	 *            cells in the grid
	 */
	public void drawGrid(final int degreeSeparation) {
		earth.setGranularity(degreeSeparation);
		sunDisplay.drawSunPath(earth.getWidth());
		repaint();
	}

	/**
	 * Gets the radius of the earth.
	 * 
	 * @return the radius of the earth in pixels
	 */
	public int getRadius() {
		return earth.getRadius();
	}

	/**
	 * Updates the display with the values from the temperature grid.
	 * 
	 * @param grid
	 *            the grid to get the new temperature values from
	 */
	public void updateGrid(final Set<GridPoint> gridPoints) {
		earth.updateGrid(gridPoints);
		earth.revalidate();
	}

	/**
	 * Moves the sun's position the specified number of degrees.
	 * 
	 * @param degrees
	 *            the number of degrees to move the sun
	 */
	public void moveSunPosition(final float degrees) {
		sunDisplay.moveSunPosition(degrees);
		repaint();
	}

	/**
	 * Resets the earth display and sun position.
	 */
	public void reset() {
		sunDisplay.reset();
		earth.reset();
		repaint();
	}

	/**
	 * Sets the opacity of the map image on a scale of 0 to 1, with 0 being
	 * completely transparent.
	 * 
	 * @param value
	 *            the opacity value
	 */
	public void setMapOpacity(final float value) {
		earth.setMapOpacity(value);
		repaint();
	}

}
