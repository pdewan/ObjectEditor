package bus.uigen.test;

import javax.swing.JFrame;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.awt.AWTContainer;
import bus.uigen.widgets.swing.SwingFrame;

public class CustomTextView {

	public static void main (String[] args) {
//		ObjectEditor.edit(new AFooBar());
		ACompositeExample random = new ACompositeExample();
		JFrame frame1 = new JFrame();
		uiFrame editor = ObjectEditor.createOEFrame(frame1);
		VirtualContainer container1 = AWTContainer.virtualContainer(frame1.getContentPane());
		ObjectEditor.editInContainer(editor,  random, container1);
//		ObjectEditor.editInContainer(editor,  "foo", container);

//		ObjectEditor.edit(new AFooBar(), frame.getContentPane());
		frame1.setSize(200, 100);
		frame1.setVisible(true);
		
		JFrame frame2 = new JFrame();
//		VirtualFrame vframe2 = SwingFrame.virtualFrame(frame2);
		VirtualContainer container2 = AWTContainer.virtualContainer(frame2.getContentPane());
//		ObjectEditor.editInMainContainer("foo", container2);
		ObjectEditor.editInMainContainer(random, container2);
//		ObjectEditor.editInMainContainer("foo \n bar", container2);

		frame2.setSize(200, 100);
		frame2.setVisible(true);
		
		JFrame frame3 = new JFrame();
//		VirtualFrame vframe2 = SwingFrame.virtualFrame(frame2);
		VirtualContainer container3 = AWTContainer.virtualContainer(frame3.getContentPane());
//		ObjectEditor.editInMainContainer("foo", container2);
		ObjectEditor.editInMainContainer("foo \n bar", container3);
//		ObjectEditor.editInMainContainer("foo", container2);

		frame3.setSize(200, 100);
		frame3.setVisible(true);
		
		ObjectEditor.edit ("foo \n bar");
		
		
	}

}
