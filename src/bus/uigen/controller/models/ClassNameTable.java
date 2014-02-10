package bus.uigen.controller.models;

import util.models.AListenableHashtable;
import bus.uigen.ObjectEditor;

public class ClassNameTable extends AListenableHashtable<String, String>  {
	ObjectEditor objectEditor;
	public ClassNameTable(ObjectEditor theObjectEditor) {
		objectEditor = theObjectEditor;
		setKeysEditable(false);
	}
	public ClassNameTable() {
		objectEditor = ObjectEditor.defaultObjectEditor();
		setKeysEditable(false);
	}
	public void open (String newClassName) {
		objectEditor.newInstance(newClassName);
	}

}
