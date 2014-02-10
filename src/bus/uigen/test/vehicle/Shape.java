package bus.uigen.test.vehicle;

import java.awt.Color;

public interface Shape {
	public int getX() ;

	public void setX(int x) ;

	public int getY() ;

	public void setY(int y) ;

	public int getWidth();

	public void setWidth(int width);

	public int getHeight() ;

	public void setHeight(int height);
	
	public Color getColor();

	public void setColor(Color color) ;
	public boolean isFilled() ;

	public void setFilled(boolean filled) ;
}
