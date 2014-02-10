package bus.uigen.test.vehicle;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JPanel;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.widgets.awt.AWTContainer;

public class ABusOEUI extends ABusUIFacade {
	Bus bus;
	public ABusOEUI(Bus aBus, int anX, int aY) {
		super(anX, aY);
		bus = aBus;
	}
	public static void main (String[] args) {
		int numBuses = 2;
		if (args.length > 0) {
			try {
				 numBuses = Integer.parseInt(args[0]);
			} catch (Exception e) {
				
			}
		}		
		Bus bus = new ABus();
		for (int i = 0; i < numBuses; i++) {
			ABusOEUI busOEUI1 = new ABusOEUI(bus,750*i , 0);
			busOEUI1.createDrawDisplay();
			busOEUI1.createTextDisplay();
//			createTreeDisplay();
			busOEUI1.bindWidgets();
		}
//		ABusOEUI busOEUI1 = new ABusOEUI(bus, 0 , 0);
//		busOEUI1.createDrawDisplay();
//		busOEUI1.createTextDisplay();
////		createTreeDisplay();
//		busOEUI1.bindWidgets();
//		ABusOEUI busOEUI2 = new ABusOEUI(bus, 750, 0);
//		busOEUI2.createDrawDisplay();
//		busOEUI2.createTextDisplay();
//		busOEUI2.bindWidgets();

	}

	
	public  void bindWidgets() {
//		Bus bus = new ABus();
		ObjectEditor.setDefaultAttribute(AttributeNames.SHOW_DEBUG_INFO_WITH_TOOL_TIP, false);
//		ObjectEditor.setAttribute(ABus.class, AttributeNames.SHOW_DEBUG_INFO_WITH_TOOL_TIP, false);
		ObjectEditor.setPropertyAttribute(ABus.class, "FrontTire",  AttributeNames.SHOW_DEBUG_INFO_WITH_TOOL_TIP, false);

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
//		ObjectEditor.editInDrawingContainer(editor, oeVisualizer.getVisualization(), (Container) dataPanel,
//				false);
//		ObjectEditor.graphicsOnlyEdit(obj)

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
