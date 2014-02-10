package bus.uigen.test;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import shapes.FlexibleShape;
import util.models.AListenableVector;
import util.models.ListenableVector;
import util.trace.Tracer;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.shapes.ARectangleModel;
import bus.uigen.shapes.AnOvalModel;
import bus.uigen.widgets.awt.AWTContainer;

public class CustomDrawingView {
	static JButton next = new JButton (">");
	static JButton previous = new JButton("<");
//	static JScrollPane dataScrollPanel = new JScrollPane();
	static JPanel dataPanel = new JPanel();
//	static JScrollPane codeScrollPanel = new JScrollPane();
	static JPanel codePanel = new JPanel();
	static JSplitPane dataAndCodePanel = new JSplitPane();
	static JPanel buttonPanel = new JPanel();
	static JFrame frame = new JFrame();
	static public void layoutAndPopulateButtonPanel() {
		System.out.println(buttonPanel.getLayout());
//		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.add(next);
		buttonPanel.add(previous);
	}
	
	
	
	static public void layoutAndPopulateFrame() {
		frame.getContentPane().setLayout(new BorderLayout());		
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		frame.getContentPane().add(dataAndCodePanel);
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setCursor(Cursor.DEFAULT_CURSOR);
	}
	
	public static void layoutAndPopulateCodeAndDataPanel() {
//		dataScrollPanel.setLayout(null);
//		codeScrollPanel.setLayout(null);
//		dataScrollPanel.setViewportView(dataPanel);
//		codeScrollPanel.setViewportView(codePanel);
		dataPanel.setPreferredSize(new Dimension(300, 200));
//		dataPanel.setMinimumSize(new Dimension(300, 200));
//		dataAndCodePanel.setLayout(new GridLayout(1, 2));
		dataAndCodePanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		dataAndCodePanel.setLeftComponent(dataPanel);
		dataAndCodePanel.setRightComponent(codePanel);
		
	}
	
	static Class[] nullArgs = {};
	
	public static void main (String[] args) {
//		Tracer.showInfo(true);
		ObjectEditor.setDefaultAttribute(AttributeNames.PREDEFINED_MENUS_CHOICE, new String[]{});
		layoutAndPopulateButtonPanel();
		layoutAndPopulateCodeAndDataPanel();
//		layoutAndPopulateFrame();
		FlexibleShape rectangle = new ARectangleModel (10, 10, 50, 50);
		FlexibleShape oval = new AnOvalModel (40, 40, 50, 50);
		ListenableVector v = new AListenableVector();
		v.add(rectangle);
		ListenableVector v2 = new AListenableVector();
		v2.add(oval);
//		frame.setSize(600, 400);
//		frame.setVisible(true);
		uiFrame editor = ObjectEditor.createOEFrame(frame);
		
		Object[] menuObjects = {v, new ADynamicCommandsDemoer()};	
		ObjectEditor.addMenuObjects(editor, menuObjects);
		layoutAndPopulateFrame();

		ObjectEditor.editInDrawingContainer(editor, v, dataPanel, false);
		ObjectEditor.editInDrawingContainer(v2, codePanel, false);
		
		ObjectEditor.bind(editor, v, "clear", next);
		ObjectEditor.bind(editor, v2, "clear", previous);
		JFrame frame = new JFrame();
//		ObjectEditor.editInContainer(new AFooBar(), AWTContainer.virtualContainer(frame.getContentPane()));
//		frame.setSize(300, 200);
//		frame.setVisible(true);

	

		
	}

}
