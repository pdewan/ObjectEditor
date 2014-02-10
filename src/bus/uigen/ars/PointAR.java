package bus.uigen.ars;
import java.awt.Point;

import bus.uigen.ObjectEditor;
import bus.uigen.undo.ExecutableCommand;
public class PointAR  implements ExecutableCommand{
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyVisible(java.awt.Point.class, "Location", false);
		return null;
	}
	
}
