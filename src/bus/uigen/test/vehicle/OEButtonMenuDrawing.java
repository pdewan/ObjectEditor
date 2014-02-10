package bus.uigen.test.vehicle;

import java.awt.Color;

import javax.swing.JPanel;

import bus.uigen.ObjectEditor;

public class OEButtonMenuDrawing extends UIComposer {
	public static void main (String[] args) {
		createButtonDrawing();
	}

	
	public static void createButtonDrawing() {
		createDisplay();
		Bus bus = new ABus();
		ObjectEditor.setModel(bus, "DoubleDecker", doubleDeckerCheckBox);	
		ObjectEditor.bind(bus, "shrink", shrinkMenuItem);	
		ObjectEditor.bind(bus, "magnify", magnifyMenuItem);
		ObjectEditor.bind(bus, "moveLeft", moveLeftButton);
		ObjectEditor.bind(bus, "moveRight", moveRightButton);
		mainPanel.setBackground(Color.WHITE);
		frame.add(mainPanel);
//		JPanel drawContainer = new JPanel();
//		mainPanel.add(drawContainer);
		ObjectEditor.editInDrawingContainer(bus, mainPanel);
//		ObjectEditor.edit(bus);

		
	}
	
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
