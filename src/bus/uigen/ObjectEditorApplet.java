package bus.uigen;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import shapes.PointModel;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.shapes.ALabelModel;
import bus.uigen.shapes.APointModel;
import bus.uigen.test.ALabelAndString;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ScrollPaneFactory;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.awt.AWTContainer;
public class ObjectEditorApplet extends java.applet.Applet  {
		public void init() {
//		super.init();
//		System.out.println("initing");
		//ScrollPaneFactory.createAWTScrollPanes();
		//ScrollPaneFactory.setScrollPaneFactory(null);
		//ObjectEditor.edit(this, this,new ObjectEditor(), new ScrollPane());
		//ObjectEditor.registerEditors();
		//bus.uigen.uiGenerator.generateUI(this, new ObjectEditor());
//		ObjectEditor.editInContainer("Hello World", AWTContainer.virtualContainer(this));
//		ObjectEditor.edit("Hello World", AWTContainer.virtualContainer(this));
		
		List list = new Vector();
		list.add("hello");
		list.add("bye");
//		ObjectEditor.editInContainer(list, AWTContainer.virtualContainer(this));
//		ObjectEditor.edit(list, this);
//		this.setName(AttributeNames.DRAW_PANEL_NAME);
//		ObjectEditor.editInContainer("hello world", AWTContainer.virtualContainer(this));
		
//		VirtualContainer drawingPanel = PanelSelector.createPanel();
//		VirtualContainer mainPanel = PanelSelector.createPanel();
//		BorderLayout borderLayout = new BorderLayout();
//		GridLayout gridLayout = new GridLayout(2, 1);
//		this.setLayout(gridLayout);
//
		VirtualContainer thisContainer = AWTContainer.virtualContainer(this);
//		drawingPanel.setSize(300, 300);
//		mainPanel.setSize(300, 100);
//		this.setSize( 300, 400);
////		thisContainer.add(mainPanel, BorderLayout.CENTER);
////		thisContainer.add(drawingPanel, BorderLayout.SOUTH);
//		thisContainer.add(mainPanel);
//		thisContainer.add(drawingPanel);
//		
//		
//		ObjectEditor.editInContainer(new ALabelAndString(), null, drawingPanel, mainPanel);
		ObjectEditor.editInContainer(new ALabelAndString(), thisContainer);

		Vector v = new Vector();
		ALabelModel labelModel = new ALabelModel("hello", null, 50, 50, 100, 20);
//		labelModel.setBounds(50, 50, 100, 100);
		v.add(labelModel);
		v.add(new APointModel(100, 100));
		v.add(new APointModel(200, 200));
//		ObjectEditor.editInDrawingContainer(v, AWTContainer.virtualContainer(this), true);
			
//		ObjectEditor.edit("Hello World", this);

//		this.add( new JTextArea ("hello world"));
		
	}
	
	public static void main (String args[]) {
		ObjectEditor.edit("Hello World");
	}
	
		
}

