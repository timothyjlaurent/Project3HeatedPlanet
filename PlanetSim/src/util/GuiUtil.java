package util;

import static java.lang.Integer.parseInt;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

public class GuiUtil {

	public static NumberFormatter buildNumberFormatter(final int min, final int max) {
		final NumberFormatter timeStepFormatter = new NumberFormatter();
		timeStepFormatter.setMinimum(min);
		timeStepFormatter.setMaximum(max);
		return timeStepFormatter;
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
			return parseInt(value);
		} else {
			return 0;
		}
	}

	public static int getIntValue(final JComboBox field) {
		return parseInt(field.getSelectedItem().toString());
	}

}
