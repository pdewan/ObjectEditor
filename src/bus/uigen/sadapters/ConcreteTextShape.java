package bus.uigen.sadapters;
import java.text.AttributedString;

import bus.uigen.undo.CommandListener;
public interface ConcreteTextShape extends ConcreteBoundedShape {
	public String getText();
	public void setText(String newValue, CommandListener cl);
	public void setText (String newValue);
	public boolean isTextReadOnly();
		boolean hasAttributedString();
		AttributedString getAttributedString();
}