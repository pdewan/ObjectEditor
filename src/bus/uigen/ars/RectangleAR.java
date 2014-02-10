package bus.uigen.ars;

import bus.uigen.ObjectEditor;
import bus.uigen.undo.ExecutableCommand;

public class RectangleAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyVisible(java.awt.Rectangle.class, "Bounds", false);
		ObjectEditor.setPropertyVisible(java.awt.Rectangle.class, "Frame", false);
		ObjectEditor.setPropertyVisible(java.awt.Rectangle.class, "Bounds2D", false);
		return null;
	}

}
