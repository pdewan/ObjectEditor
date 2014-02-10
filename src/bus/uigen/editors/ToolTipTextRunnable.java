package bus.uigen.editors;

import java.awt.Point;

import shapes.RemoteShape;
import slm.ShapesList;

public interface ToolTipTextRunnable extends Runnable {

	public abstract String getToolTipText();

	public abstract int getToolTipX();

	public abstract int getToolTipY();

	public abstract void mouseEntered(RemoteShape aComponent, String aToolTipText, Point aCursorPoint);

	public abstract void mouseExited(RemoteShape aComponent, Point aMousePoint);
	
	ShapesList getShapesList();
	
	void setShapesList(ShapesList aShapesList);


}