package automat_package;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Budget {

	@SuppressWarnings("unchecked")
	private List<Money>[] wallet = new List[8];
//	0 - LinkedList<Money> coins10 = new LinkedList<>();
//	1 - LinkedList<Money> coins20 = new LinkedList<>();
//	2 - LinkedList<Money> coins50 = new LinkedList<>();
//	3 - LinkedList<Money> coins1 = new LinkedList<>();
//	4 - LinkedList<Money> coins2 = new LinkedList<>();

//	5 - Stack<Money> cash5 = new Stack<>();
//	6 - Stack<Money> cash10 = new Stack<>();
//	7 - Stack<Money> cash20 = new Stack<>();

	public Budget(int[] amounts) {
		if (amounts.length != Money.MONEY_VALUES.length) {
			System.err.println("Budget is not set correctly! (Budget.java l. 13)");
			return;
		}
		wallet = setWallet(amounts);
	}

	public Budget() {
		wallet = setWallet(new int[8]);
	}

	private List<Money>[] setWallet(int[] amounts) {
		@SuppressWarnings("unchecked")
		List<Money>[] output = new List[amounts.length];
		for (int i = 0; i < amounts.length; i++) {
			output[i] = i < 5 ? new LinkedList<Money>() : new Stack<Money>();
			for (int k = 0; k < amounts[i]; k++) {
				if (i < 5) {
					((LinkedList<Money>) output[i]).add(new Money(Money.MONEY_VALUES[i], true));
				} else {
					((Stack<Money>) output[i]).push(new Money(Money.MONEY_VALUES[i], false));
				}
			}
		}
		return output;
	}

	public double calculateTotal() {
		double sum = 0.0;
		for (int i = 0; i < wallet.length; i++) {
			int times = wallet[i].size();
			for (int k = 0; k < times; k++) {
				sum += Money.MONEY_VALUES[i] * 10;
			}
		}
		sum /= 10;
		return sum;
	}

	public String getTotal() {
		double total = calculateTotal();
		NumberFormat nf = NumberFormat.getInstance(Money.GERMANY);
		String output = nf.format(total);
		if (output.contains(Money.DECIMAL_SEPARATOR + "")
				&& output.split(Money.DECIMAL_SEPARATOR + "")[1].length() == 1) {
			output += "0";
		}
		return output + Money.MONEYMARK_Bigger;
	}

	public Money outMoney(int moneyValueIndex) { // Money will be taken out from the right list.
		if (moneyValueIndex == -1)
			return null;
		if (moneyValueIndex < Money.MONEY_VALUES.length && !wallet[moneyValueIndex].isEmpty()) {
			if (moneyValueIndex < 5) {
				return ((LinkedList<Money>) wallet[moneyValueIndex]).remove();
			} else {
				return ((Stack<Money>) wallet[moneyValueIndex]).pop();
			}
		}
		return null;
	}

	public void inMoney(Money money) { // Money will be put in the right list.
		for (int i = 0; i < Money.MONEY_VALUES.length; i++) {
			if (money.getValue() == Money.MONEY_VALUES[i]) {
				if (i < 5) {
					((LinkedList<Money>) wallet[i]).add(money);
				} else {
					((Stack<Money>) wallet[i]).push(money);
				}
				return;
			}
		}

	}

	public void gainMoney(List<Money>[] gain) {
		for (int i = 0; i < gain.length; i++) {
			if (i < 5) {
				while (!gain[i].isEmpty()) {
					((LinkedList<Money>) wallet[i]).add(((LinkedList<Money>) gain[i]).remove());
				}
			} else {
				while (!gain[i].isEmpty()) {
					((Stack<Money>) wallet[i]).push(((Stack<Money>) gain[i]).pop());
				}
			}
		}

	}

	public List<Money>[] loseMoney(double cost, boolean withCash) {
		int cost10T = (int) (cost * 10);
		List<Money>[] output = setWallet(new int[wallet.length]);
		int start = withCash ? output.length - 1 : output.length - 4;
		for (int i = start; i >= 0; i--) {
			while (cost10T >= Money.MONEY_VALUES[i] * 10 && this.getAmount(i) > 0) {
				if (i < 5) {
					((LinkedList<Money>) output[i]).add(((LinkedList<Money>) wallet[i]).remove());
				} else {
					((Stack<Money>) output[i]).push(((Stack<Money>) wallet[i]).pop());
				}
				cost10T -= (int) (Money.MONEY_VALUES[i] * 10);

			}
		}
		if (cost10T != 0) {
			System.err.println(
					"There is not enough money in the vending machine!" + "\nDebt to the costumer with the ID '"
							+ Costumer.existingCostumer + "' is " + cost + Money.MONEYMARK_Bigger + ".");
		}

		return output;
	}

	public List<Money>[] getWallet() {
		return wallet;
	}

	public int getAmount(int moneyValueIndex) {
		return wallet[moneyValueIndex].size();
	}
}
