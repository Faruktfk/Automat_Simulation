package automat_package;

import java.awt.Point;

public class Bottle {

	private Money price;
	private String slot;
	private Point loc;
	
	public Bottle(double value, int x, int y) {
		
		this.price = new Money(value);
		this.slot = y + "_" + x;
		this.loc = new Point(x,y);
	}
	
	public Money getPrice() {
		return price;
	}
	public void setPrice(double value, boolean isCoin) {
		this.price = new Money(value, isCoin);
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	
	public Point getLoc() {
		return loc;
	}

	
}
