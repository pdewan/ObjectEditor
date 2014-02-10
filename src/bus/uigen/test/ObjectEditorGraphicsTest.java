package bus.uigen.test;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import shapes.FlexibleShape;
import util.misc.ThreadSupport;
import bus.uigen.ObjectEditor;

public class ObjectEditorGraphicsTest {
	public static void main (String[] args) {
		ObjectEditor.setShapeDrawerFrameSize(640, 480);
		ObjectEditor.setShapeDrawerBackground (new Color(86, 160, 211));
		FlexibleShape shape = ObjectEditor.drawLine(0, 30, 30, 40);
		ObjectEditor.drawString("hello world", 100, 100);		
		FlexibleShape rectangle = ObjectEditor.drawRectangle(200, 300, 100, 100);	
//		rectangle.setFilled(true);
		rectangle.set3D(true);
//		ThreadSupport.sleep(5000);
		ObjectEditor.drawOval(100, 130, 30, 40);
		ObjectEditor.drawString("goodbye world", 200, 100);		
//		ThreadSupport.sleep(5000);
		shape.setX(30);
		ThreadSupport.sleep(5000);		
		ObjectEditor.removeShape(shape);
		char c = ObjectEditor.getChar();
		ObjectEditor.drawString("InputChar:" + c, 250, 250);	
		KeyEvent keyEvent = ObjectEditor.getKeyEvent();
		ObjectEditor.drawString("KeyEvent:" + keyEvent.toString(), 250, 280);
		MouseEvent mouseEvent = ObjectEditor.getMouseClickedEvent();
		ObjectEditor.drawString("MouseEvent:" + mouseEvent, mouseEvent.getX(), mouseEvent.getY());
		AWTEvent awtEvent = ObjectEditor.getAWTEvent();
		if (awtEvent instanceof MouseEvent) {
			MouseEvent mouseEvent2 = (MouseEvent) awtEvent;
			ObjectEditor.drawString("AWTEvent-->MouseEvent", mouseEvent2.getX(), mouseEvent2.getY());
		} else if (awtEvent instanceof KeyEvent) {
			KeyEvent keyEvent2 = (KeyEvent) keyEvent;
			ObjectEditor.drawString("AWTEvent-->KeyEvent" + keyEvent.toString(), 250, 210);
		}
	}
}
