package bus.uigen.sadapters;
import bus.uigen.uiFrame;
import bus.uigen.undo.CommandListener;
public interface ConcreteArc extends ConcreteBoundedShape  {
	public int getStartAngle();
	public void setStartAngle (int newValue, CommandListener cl);
	public void setStartAngle (int newValue);
	public int getEndAngle();
	public void setEndAngle (int newValue);
	public void setEndAngle (int newValue, CommandListener cl);
	public void setStartAngleEndAngle(int newStartAngleValue, int newEndAngleValue);
	public void setStartAngleEndAngle(int newStartAngleValue, int newEndAngleValue, CommandListener cl);
	
}