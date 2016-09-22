package bus.uigen.test;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class MagnificationTester {
	public static void main (String[] args) {
		ObjectEditor.setDenseMagnification(2.0);
		OEFrame aFrame = ObjectEditor.edit(new ACompositeExample());
	}

}
