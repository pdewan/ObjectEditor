package bus.uigen.ars;

import bus.uigen.ObjectEditor;
import bus.uigen.undo.ExecutableCommand;

public class DimensionAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setVisible(java.awt.Dimension.class,  false);
		return null;
	}

}
