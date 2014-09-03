package bus.uigen.test;

import bus.uigen.ObjectEditor;

public class NoStartView {
	
	public static void main (String[] args) {
		ObjectEditor.setShowStartView(false);
		ObjectEditor.edit("Hello World");
	}

}
