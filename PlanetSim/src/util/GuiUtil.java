package util;

import static java.lang.Integer.parseInt;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

public class GuiUtil {

	public static NumberFormatter buildNumberFormatter(final int min, final int max) {
		final NumberFormatter formatter = new NumberFormatter();
		formatter.setMinimum(min);
		formatter.setMaximum(max);
		return formatter;
	}

	public static NumberFormatter buildNumberFormatter(final double min, final double max, final int minFractionDigits) {
		final DecimalFormat format = new DecimalFormat();
		format.setMinimumFractionDigits(minFractionDigits);

		final NumberFormatter formatter = new NumberFormatter(format);
		formatter.setMinimum(min);
		formatter.setMaximum(max);
		return formatter;
	}

	public static GridBagConstraints buildConstraints(final int anchor, final int gridx, final Insets insets) {
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = anchor;
		constraints.gridx = gridx;
		constraints.insets = insets;
		return constraints;
	}

	public static int getIntValue(final JFormattedTextField field) {
		final String value = field.getText();
		if (value != null && !"".equals(value.trim())) {
			return parseInt(value.replace(",", ""));
		} else {
			return 0;
		}
	}

	public static int getIntValue(final JComboBox field) {
		return parseInt(field.getSelectedItem().toString());
	}

}
