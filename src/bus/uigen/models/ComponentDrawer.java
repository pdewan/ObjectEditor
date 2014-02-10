package bus.uigen.models;

import shapes.FlexibleShape;
import shapes.FlexibleTextShape;

public interface ComponentDrawer  {
	public FlexibleShape drawRectangle(int x, int y, int width, int height) ;
	public FlexibleShape fillRectangle(int x, int y, int width, int height) ;
	public FlexibleShape drawOval(int x, int y, int width, int height) ;
	public FlexibleShape fillOval(int x, int y, int width, int height) ;
	public FlexibleShape drawLine(int x, int y, int width, int height) ;
	public FlexibleTextShape drawString(String text, int x, int y) ;
	public FlexibleShape drawImage(String fileName, int x, int y) ;
	public FlexibleShape drawPoint(int x, int y) ;
	public void clearDrawing();
	public boolean remove(FlexibleShape obj);
}
