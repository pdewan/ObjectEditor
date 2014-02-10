package bus.uigen.sadapters;
import bus.uigen.uiFrame;
import bus.uigen.undo.CommandListener;
public interface ConcreteCurve extends ConcreteBoundedShape  {
	public int getControlX();
	public void setControlX (int newValue, CommandListener cl);
	public void setControlX (int newValue);
	public int getControlY();
	public void setControlY (int newValue);
	public void setControlY (int newValue, CommandListener cl);
	
	public Integer getControlX2();
	public void setControlX2 (Integer newValue, CommandListener cl);
	public void setControlX2 (Integer newValue);
	public Integer getControlY2();
	public void setControlY2 (Integer newValue);
	public void setControlY2 (Integer newValue, CommandListener cl);
	public void setControlXControlY(int newControlXValue, int newControlYValue) ;
	public void setControlXControlY(int newControlXValue, int newControlYValue,
			CommandListener cl);
	boolean isControlXReadOnly();

	boolean isControlYReadOnly();
	
	
}