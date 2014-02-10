package bus.uigen.test.vehicle;

import javax.swing.JTree;

import bus.uigen.ObjectEditor;
import bus.uigen.widgets.awt.AWTContainer;

public class OEMenuDrawing extends UIComposer {
	public static void main (String[] args) {
		createMenuDrawing();


	}
	
	public static void createMenuDrawing() {
		createDisplayMenuBar();
		Bus bus = new ABus();
	
		ObjectEditor.bind(bus, "hrink", shrinkMenuItem);	
		ObjectEditor.bind(bus, "magnify", magnifyMenuItem);
		ObjectEditor.bind(bus, "moveLeft", moveLeftMenuItem);
		ObjectEditor.bind(bus, "moveRight", moveRightMenuItem);
		ObjectEditor.editInDrawingContainer(bus, mainPanel);

		
	}
	
//	public static void createButtonDrawing() {
//		createDisplayButton();
//		Bus bus = new ABus();
//		ObjectEditor.setModel(bus, "DoubleDecker", doubleDeckerCheckBox);
//	
//		ObjectEditor.bind(bus, "shrink", shrinkButton);	
//		ObjectEditor.bind(bus, "magnify", magnifyButton);
//		ObjectEditor.bind(bus, "moveLeft", moveLeftButton);
//		ObjectEditor.bind(bus, "moveRight", moveRightButton);
//		ObjectEditor.editInDrawingContainer(bus, mainPanel);
//		ObjectEditor.edit(bus);
//
//		
//	}
//	
//	public static void createMenuTree() {
//		createDisplayMenuBar();
//
//		Bus bus = new ABus();
//	
//		ObjectEditor.bind(bus, "shrink", shrinkMenuItem);	
//		ObjectEditor.bind(bus, "magnify", magnifyMenuItem);
//		ObjectEditor.bind(bus, "moveLeft", moveLeftMenuItem);
//		ObjectEditor.bind(bus, "moveRight", moveRightMenuItem);
////		ObjectEditor.editInDrawingContainer(bus, frame.getContentPane());
////		JTextField textField = new JTextField("hello");
////		frame.add(textField);
////		frame.validate();
//		
//		JTree jTree = new JTree();
//		frame.add(jTree);		
////		frame.repaint();
//
//		ObjectEditor.editInTreeContainer(bus, AWTContainer.virtualContainer(jTree), false);
//		frame.validate();
//
//	}

}
