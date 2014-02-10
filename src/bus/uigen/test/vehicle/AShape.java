package bus.uigen.test.vehicle;

import java.awt.Color;

import util.annotations.IndirectlyVisible;
import util.annotations.Position;

public class AShape implements Shape {
	int x, y, width, height;
	Color color;
	boolean filled;
//	public boolean filled;

	@Position(0)
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	@Position(1)
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
  
	public int getWidth() {
		return width;
	}
    
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	
	public String toString() {
		return "(" + x + "," + y + "," + width + "," + height + ")";
	}

}
