package bus.uigen.test;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

public class ANewEditorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ObjectEditor.setDefaultAttribute(AttributeNames.SHOW_SYSTEM_MENUS, false);
		ObjectEditor.edit("hello");
	}
}
