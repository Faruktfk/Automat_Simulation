package automat_package;

import java.awt.Point;
import java.awt.Rectangle;

public class InvisibleButton {

	String name;
	Point corner1, corner2;
	int width, height;
	boolean lightUp = false;

	public InvisibleButton(String name, int x1, int y1, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.corner1 = new Point(x1,y1);
		this.corner2 = new Point(x1+width,y1+height);
	}

	public boolean checkClicked(Point click) {
		// Is the click inside the field?
		return ((corner1.x <= click.x && click.x <= corner2.x) || (corner2.x <= click.x && click.x <= corner1.x))
				&& ((corner1.y <= click.y && click.y <= corner2.y) || (corner2.y <= click.y && click.y <= corner1.y));
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(corner1.x, corner1.y, width, height);
	}

}
