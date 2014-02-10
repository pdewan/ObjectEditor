package bus.uigen.sadapters;
import java.awt.Shape;

import bus.uigen.uiFrame;
import bus.uigen.undo.CommandListener;
public interface ConcreteAWTShape extends ConcreteShape  {
	Shape getAWTShape();
	void setAWTShape(Shape aNewVal);	
	 void setAWTShape(Shape aNewVal, CommandListener cl);
	
}