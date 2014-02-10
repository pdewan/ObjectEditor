package bus.uigen.ars;

import javax.swing.JTable;

import util.models.AListenableHashtable;

import bus.uigen.ObjectEditor;
import bus.uigen.undo.ExecutableCommand;

public class AListenableHashtableAttributeRegisterer implements ExecutableCommand {
	
	public Object execute(Object theFrame) {
		ObjectEditor.setPreferredWidget(AListenableHashtable.class, JTable.class);
		return null;
	}

}
