package EarthSim;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import EarthSim.Initiative.Initiative;

public class Presentation extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Earth earth = new Earth();
	
	private Initiative initiative;
	
	private JLabel timeStep;
	
	private JLabel rotationalPosition;
	
	public Presentation(Initiative initiative) {
		this.initiative = initiative;
	}
	
	private Color createColorFromValue(final double value, final double range) {
		final int redValue;
		if (range > 0) {
			redValue = (int) (((value + earth.minimumTemperature) / range) * 255);
		} else {
			redValue = 255;
		}
		return new Color(255 - Math.max(redValue, 0), 0, 0);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		double range = earth.getTemperatureRange();
		for (int i = 0; i < earth.temperatures.length; i++) {
			for (int j = 0; j < earth.temperatures[i].length; j++) {
				final int colWidth = getWidth() / earth.temperatures.length;
				final int rowHeight = getHeight() / earth.temperatures[i].length;
				g.setColor(createColorFromValue(earth.temperatures[i][j], range));
				g.fillRect(i * colWidth - 1, j * rowHeight - 1, colWidth, rowHeight);
			}
		}

		// Draw Vertical Grid Lines
		g.setColor(Color.WHITE);
		for (int k = 0; k < earth.gridSpacing; k++) {
			final int horizontalPos = k * getWidth() / earth.gridSpacing;
			final int latitudeValue = -180 + 360 / earth.gridSpacing * k;

			g.drawRect(horizontalPos, 0, 1, getHeight());
			g.drawString(latitudeValue + "", horizontalPos + 10, getHeight() - 10);
		}

		// Draw Equator & Prime Meridian
		g.drawRect(getWidth() / 2, 0, 1, getHeight());
		g.drawString("0", getWidth() / 2 + 10, getHeight() - 10);

		g.drawRect(0, getHeight() / 2, getWidth(), 1);
		g.drawString("0", 10, getHeight() / 2 - 10);

	}
	
	public void setLabels(JLabel timeStep, JLabel rotationalPosition) {
		this.timeStep = timeStep;
		this.rotationalPosition = rotationalPosition;
	}
	
	public void update() {
		doUpdate();
	}
	
	protected void doUpdate() {
		earth = initiative.read();
		this.repaint();
		timeStep.setText(Integer.toString(earth.simulations * earth.timeStep));
		rotationalPosition.setText(Integer.toString(earth.rotation));
		initiative.consumerDone();
	}
}
