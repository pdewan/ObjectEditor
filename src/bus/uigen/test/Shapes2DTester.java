package bus.uigen.test;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import bus.uigen.ObjectEditor;

public class Shapes2DTester {
	public static void main (String[] args) {
		Rectangle rectangle = new Rectangle(new Point(10, 10), new Dimension (100, 100));
		ObjectEditor.edit(rectangle);
		
	}

}
