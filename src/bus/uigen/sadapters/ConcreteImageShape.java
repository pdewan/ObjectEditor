package bus.uigen.sadapters;

import bus.uigen.undo.CommandListener;

public interface ConcreteImageShape extends ConcreteBoundedShape  {
	public String getImageFileName();
	public void setImageFileName (String newValue);
	public boolean isImageFileNameReadOnly();
	
}
