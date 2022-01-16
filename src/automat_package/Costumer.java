package automat_package;

import java.util.LinkedList;

public class Costumer extends Budget {

	public static Costumer existingCostumer;

	@SuppressWarnings("unchecked")
	private LinkedList<Bottle>[][] cart = new LinkedList[3][2];

	public Costumer(int[] amounts) {
		super(amounts);
		existingCostumer = this;
		for(int y = 0; y<cart.length; y++) {
			for(int x = 0; x<cart[y].length; x++) {
				cart[y][x] = new LinkedList<>();
			}
		}
	}

	public void buyItem(Bottle bottle) {
		cart[bottle.getLoc().y][bottle.getLoc().x].add(bottle);
	}

	public Bottle getBottle(int x, int y) {
		return cart[y][x].get(0);
	}

	public int getBottleAmount(int x, int y) {
		return cart[y][x].size();
	}

	public LinkedList<Bottle> getCart(int x, int y) {
		return cart[y][x];
	}

}
