package bus.uigen.test;

import java.awt.Rectangle;

import shapes.FlexibleShape;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.shapes.AnOvalModel;

public class ATelePointerTester {
	public static void main (String[] args) {
		OEFrame oeFrame = ObjectEditor.edit("Hello World");
		FlexibleShape oval = new AnOvalModel(new Rectangle(10, 10));
		oval.setFilled(true);
		oeFrame.setTelePointerModel(oval);
	}
}
