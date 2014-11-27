package cs6310.gui.widget.earth;

import java.awt.Color;

/**
 * Use this class to get a color representation of a temperature. This is
 * implemented as a singleton.
 * 
 * @author Andrew Bernard
 */
public class TemperatureColorPicker {
	private static TemperatureColorPicker instance = null;

	private TemperatureColorPicker() {
	}

	static TemperatureColorPicker getInstance() {
		if (instance == null) {
			instance = new TemperatureColorPicker();
		}
		return instance;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This is a singleton - please use the getInstance() method.");
	}

	// Adapted from
	// http://stackoverflow.com/questions/20792445/calculate-rgb-value-for-a-range-of-values-to-create-heat-map
	// def rgb(minimum, maximum, value):
	// minimum, maximum = float(minimum), float(maximum)
	// ratio = 2 * (value-minimum) / (maximum - minimum)
	// b = int(max(0, 255*(1 - ratio)))
	// r = int(max(0, 255*(ratio - 1)))
	// g = 255 - b - r
	// return r, g, b
	// return (r,g,b)
	public Color getColor(final int value, final double min, final double max) {

		final double ratio = 2 * (value - min) / (max - min);
		int b = (int) (Math.max(0, 255 * (1 - ratio)));
		int r = (int) (Math.max(0, 255 * (ratio - 1)));
		int g = 255 - b - r;
		if (b < 0) {
			b = 0;
		} else if (b > 255) {
			b = 255;
		}
		if (r < 0) {
			r = 0;
		} else if (r > 255) {
			r = 255;
		}
		if (g < 0) {
			g = 0;
		} else if (g > 255) {
			g = 255;
		}

		return new Color(r, g, b);

	}

	/**
	 * Maps a color to the given temperature.
	 * 
	 * @param temperature
	 *            in celsius
	 * @return the temperature color
	 */
	Color getColor(int temperature) {
		int b = 0;
		int g = 0;
		int r = 0;

		if (temperature <= -100) {
			b = 170;
			g = 100;
			r = 170;
		} else if (temperature <= -46) {
			temperature = -1 * temperature;
			b = 255;
			g = 145 - (temperature * 10) % 115;
			r = 255;
		} else if (temperature <= -23 && temperature > -46) {
			temperature = -1 * temperature;
			b = 255;
			g = 145;
			r = 145 + (temperature * 5) % 115;
		} else if (temperature < 0 && temperature > -23) {
			temperature = -1 * temperature;
			b = 255;
			g = 145;
			r = 145 - (temperature * 5);
		} else if (temperature == 0) {
			b = 225;
			g = 145;
			r = 145;
		} else if (temperature > 0 && temperature < 23) {
			b = 255;
			g = 145 + (temperature * 5);
			r = 145;
		} else if (temperature >= 23 && temperature < 46) {
			b = 255 - (temperature * 5) % 115;
			g = 255;
			r = 145;
		} else if (temperature >= 46 && temperature < 69) {
			b = 145;
			g = 255;
			r = 145 + (temperature * 5) % 115;
		} else if (temperature >= 69 && temperature < 92) {
			b = 145;
			g = 255 - (temperature * 5) % 115;
			r = 255;
		} else {
			b = 145 - (temperature * 10) % 115;
			g = 145 - (temperature * 10) % 115;
			r = 255;
		}

		return new Color(r, g, b);
	}
}
