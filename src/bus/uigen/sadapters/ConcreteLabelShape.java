package bus.uigen.sadapters;

import bus.uigen.undo.CommandListener;

public interface ConcreteLabelShape extends ConcreteTextShape {
	public String getImageFileName();
	public void setImageFileName (String newValue);
	public boolean isImageFileNameReadOnly();	
}
