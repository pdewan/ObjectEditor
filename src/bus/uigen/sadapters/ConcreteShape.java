package bus.uigen.sadapters;
import bus.uigen.uiFrame;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.undo.CommandListener;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
public interface ConcreteShape extends RecordStructure {
//	public int getX();
//	public void setX (int newValue, CommandListener cl);
//	public void setX (int newValue);
//	public int getY();
//	public void setY (int newValue);
//	public void setY (int newValue, CommandListener cl);
//	public void setXY(int newXValue, int newYValue);
//	public void setXY(int newXValue, int newYValue, CommandListener cl);
	
	public Color getColor();
	public void setColor (Color newValue);
	public void setColor (int newValue, CommandListener cl);
//	public boolean isXReadOnly();
//	public boolean isYReadOnly();	
	public boolean hasColor();
	public boolean isColorReadOnly();
	public boolean getFilled() ;
	public void setFilled(boolean newValue) ;	  
	public void setFilled (boolean newValue, CommandListener cl) ;
	public boolean hasFilled();	  
    public boolean isFilledReadOnly() ;
    
    public int getZIndex() ;
    public void setZIndex(int newValue) ;	  
	public void setZIndex (int newValue, CommandListener cl) ;
	public boolean hasZIndex();	  
    public boolean isZIndexReadOnly() ;
	Stroke getBasicStroke();
	
	Paint getGradientPaint();
	// this should be in text shape, but I guess graphics has a setFont method
	boolean hasFont();
	boolean hasFontSize();
	Font getFont();
	int getFontSize();
	
	boolean hasBasicStroke();
	// this should be in Rectangle model
	boolean has3D();
	boolean hasRounded();
	boolean hasGradientPaint();
	boolean get3D();
	boolean getRounded();
	
}