package automat_package;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Automat extends Budget {

	public static String CHANGE_SLOT = "change", PRODUCT_SLOT = "product", COIN_SLOT = "coin", CASH_SLOT = "cash";

	private Slot[][] slots = new Slot[3][2];
	private ArrayList<Bottle> fallenProducts = new ArrayList<>();
	private String input = "";
	private String output = "";
	private double insertedAmout;
	private Budget changeBudget;

	public Automat(Slot[][] slots, int[] amounts) {
		super(amounts);
		this.slots = slots;

		insertedAmout = 0;
		changeBudget = new Budget();
	}

	public void confirm(CostumerPanel costumerPanel) {
		if (input.length() != 2 || input.equals("33") || input.equals("23") || input.equals("13")) {
			output = "Err";
			input = "";
			System.out.println("err");
		} else {
			int index = Integer.parseInt(input);
			output = "";
			input = "";
			int x = index % 10;
			int y = index / 10;
			Slot currentSlot = slots[y - 1][x - 1];
			if (currentSlot.isEmpty()) {
				output = "Err";
				System.out.println("err");
				return;
			}

			double bottlePrice = currentSlot.peekBottle().getPrice().getValue();
			if (insertedAmout >= bottlePrice) {
				insertedAmout = ((insertedAmout * 10) - (bottlePrice * 10)) / 10;
				changeBudget.gainMoney(this.loseMoney(insertedAmout, false));
				insertedAmout = 0;
				// after a purchase all inserted money will be returned
				Bottle item = currentSlot.buyABottle();
				fallenProducts.add(item);
				AdminFrame.adminFrame.update();
				costumerPanel.update();

			}

		}

	}

	public void cancel(CostumerPanel costumerPanel) {
		input = "";
		output = "";
		changeBudget.gainMoney(this.loseMoney(insertedAmout, false));
		insertedAmout = 0;

		AdminFrame.adminFrame.update();
		costumerPanel.update();
	}

	public void getProduct(CostumerPanel costumerPanel) {

		if (fallenProducts.size() != 0) {
			for (Bottle b : fallenProducts) {
				Costumer.existingCostumer.buyItem(b);
			}
			fallenProducts.clear();
			AdminFrame.adminFrame.update();
			costumerPanel.update();
		}
	}

	public void getChange(CostumerPanel costumerPanel) {
		if (changeBudget.calculateTotal() > 0) {
			Costumer.existingCostumer.gainMoney(changeBudget.getWallet());
			changeBudget = new Budget();
			costumerPanel.update();
		}

	}

	public void insertMoney(int moneyValueIndex, CostumerPanel costumerPanel, boolean isCoinSlot) {
		if ((isCoinSlot && moneyValueIndex < 5) || (!isCoinSlot && moneyValueIndex >= 5)) {
			Money paidM = Costumer.existingCostumer.outMoney(moneyValueIndex);
			if (paidM == null)
				return;
			insertedAmout += paidM.getValue();
			this.inMoney(paidM);
			costumerPanel.update();
			AdminFrame.adminFrame.update();
		}

	}

	// Setters and Getters
	// --------------------------------------------------------------------------------
	public Slot[][] getSlots() {
		return slots;
	}

	public void setSlots(Slot[][] slots) {
		this.slots = slots;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
		this.output = input;
	}

	public String getOutput() {
		return output;
	}

	public String getTempBudgetAmount() {
		double total = insertedAmout;
		NumberFormat nf = NumberFormat.getInstance(Money.GERMANY);
		String output = nf.format(total);
		if (output.contains(Money.DECIMAL_SEPARATOR + "")
				&& output.split(Money.DECIMAL_SEPARATOR + "")[1].length() == 1) {
			output += "0";
		}
		return output + Money.MONEYMARK_Bigger;
	}

	public boolean getNotification(String slotName) {
		if (slotName.equals(CHANGE_SLOT)) {
			if (changeBudget.calculateTotal() > 0) {
				return true;
			}
			return false;

		} else if (slotName.equals(PRODUCT_SLOT)) {
			if (fallenProducts.size() > 0) {
				return true;
			}
			return false;
		}
		return false;
	}

}