package bus.uigen.test.vehicle;

import java.awt.BorderLayout;

import javax.swing.JTree;

import bus.uigen.ObjectEditor;
import bus.uigen.widgets.awt.AWTContainer;

public class OEMenuTree extends UIComposer {
	public static void main (String[] args) {
		createMenuTree();


	}
	
//	public static void createMenuDrawing() {
//		createDisplayMenuBar();
//		Bus bus = new ABus();
//	
//		ObjectEditor.bind(bus, "hrink", shrinkMenuItem);	
//		ObjectEditor.bind(bus, "magnify", magnifyMenuItem);
//		ObjectEditor.bind(bus, "moveLeft", moveLeftMenuItem);
//		ObjectEditor.bind(bus, "moveRight", moveRightMenuItem);
//		ObjectEditor.editInDrawingContainer(bus, mainPanel);
//
//		
//	}
//	
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
	
	public static void createMenuTree() {
		createDisplayMenuBar();

		Bus bus = new ABus();	
		ObjectEditor.bind(bus, "shrink", shrinkMenuItem);	
		ObjectEditor.bind(bus, "magnify", magnifyMenuItem);
		ObjectEditor.bind(bus, "moveLeft", moveLeftMenuItem);
		ObjectEditor.bind(bus, "moveRight", moveRightMenuItem);		
		JTree jTree = new JTree();
//		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(jTree);		
		ObjectEditor.editInTreeContainer(bus, AWTContainer.virtualContainer(jTree), false);
		frame.validate();

	}

}
