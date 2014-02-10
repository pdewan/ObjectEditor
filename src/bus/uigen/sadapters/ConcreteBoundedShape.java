package bus.uigen.sadapters;
import bus.uigen.uiFrame;
import bus.uigen.undo.CommandListener;
public interface ConcreteBoundedShape extends ConcreteLocatableShape {
	public int getWidth();
	public void setWidth(int newValue, CommandListener cl);
	public void setWidth (int newValue);
	public int getHeight();
	public void setHeight (int newValue);
	public void setHeight (int newValue, CommandListener cl);
	public boolean isWidthReadOnly();
	public boolean isHeightReadOnly();	
}