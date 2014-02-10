package bus.uigen.test.vehicle;

import java.awt.Color;

import javax.swing.JPanel;

import bus.uigen.ObjectEditor;
import bus.uigen.widgets.awt.AWTContainer;

public class BusOEUI extends BusUIFacade {
	public static void main (String[] args) {
		createDrawDisplay();
		createTextDisplay();
//		createTreeDisplay();
		bindWidgets();
	}

	
	public static void bindWidgets() {
		Bus bus = new ABus();
		ObjectEditor.setModel(bus, "DoubleDecker", doubleDeckerCheckBox);	
		ObjectEditor.setModel(bus, "TextDisplay", textTextField);	

		ObjectEditor.bind(bus, "shrink", shrinkMenuItem);	
		ObjectEditor.bind(bus, "magnify", magnifyMenuItem);
		ObjectEditor.bind(bus, "moveLeft", moveLeftButton);
		ObjectEditor.bind(bus, "moveRight", moveRightButton);
		mainPanel.setBackground(Color.WHITE);
//		JPanel drawContainer = new JPanel();
//		mainPanel.add(drawContainer);
		ObjectEditor.editInDrawingContainer(bus, mainPanel, false);
//		ObjectEditor.editInTreeContainer(bus, AWTContainer.virtualContainer(jTree), false);
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
