package automat_package;

import java.util.Stack;
import java.awt.Point;

public class Slot {

	public final int MAX_AMOUNT = 10;

	private String id;
	private int currentAmount = 0;
	private Stack<Bottle> containing = new Stack<>();
	private Point loc;

	public Slot(int x, int y) {
		this.id = y + "_" + x;
		this.loc = new Point(x, y);
	}

	public void fillSlot(Bottle[] delivery) {
		for (int i = currentAmount; i < delivery.length; i++) {
			containing.push(delivery[i]);
			currentAmount++;
		}
	}

	public Bottle peekBottle() {
		return containing.peek();
	}

	public boolean isEmpty() {
		return containing.isEmpty();
	}

	public Bottle buyABottle() {
		if (isEmpty())
			return null;
		currentAmount--;
		return containing.pop();
	}

	public String getId() {
		return id;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public Stack<Bottle> getContaining() {
		return containing;
	}

	public Point getLoc() {
		return loc;
	}
}
