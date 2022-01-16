package automat_package;

import java.util.Locale;

public class Money {

	public static final char SEPARATOR = '.', DECIMAL_SEPARATOR = ',';
	public static final String MONEYMARK_Smaller = "ct", MONEYMARK_Bigger = "\u20ac"; //Euro sign
	public static final Locale GERMANY = Locale.GERMANY;
	public static final double[] MONEY_VALUES = new double[] { 0.1, 0.2, 0.5, 1, 2, 5, 10, 20 };
	public static final String[] MONEY_NAMES = new String[] { "10" + MONEYMARK_Smaller, "20" + MONEYMARK_Smaller, "50" + MONEYMARK_Smaller,
			"1" + MONEYMARK_Bigger, "2" + MONEYMARK_Bigger, "5" + MONEYMARK_Bigger, "10" + MONEYMARK_Bigger,
			"20" + MONEYMARK_Bigger};

	private double value;
	private boolean isCoin = false;

	public Money(double value, boolean isCoin) {
		this.value = value;
		this.isCoin = isCoin;
	}

	public Money(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public boolean isCoin() {
		return isCoin;
	}

	private String getMoneyMark() {
		if (isCoin) {
			if (value == 2 || value == 1)
				return MONEYMARK_Bigger;
			return MONEYMARK_Smaller;
		}
		return MONEYMARK_Bigger;
	}

	@Override
	public String toString() {
		String output = "";
		String v = value + "";
		for (char c : v.toCharArray()) {
			if (!Character.isDigit(c)) {
				output += DECIMAL_SEPARATOR;
			} else {
				output += c;
			}

		}
		if (output.split(DECIMAL_SEPARATOR + "")[1].length() == 1) {
			output += "0";
		}

		return output + getMoneyMark();
	}
}
