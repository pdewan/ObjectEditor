package bus.uigen.controller.models;

import util.models.AListenableVector;
import bus.uigen.ObjectEditor;

public class ClassNameList extends AListenableVector<String>  {
	ObjectEditor objectEditor;
	public ClassNameList(ObjectEditor theObjectEditor) {
		objectEditor = theObjectEditor;
	}
	public ClassNameList() {
		objectEditor = ObjectEditor.defaultObjectEditor();
	}
	public void open (String newClassName) {
		objectEditor.newInstance(newClassName);
	}

}
