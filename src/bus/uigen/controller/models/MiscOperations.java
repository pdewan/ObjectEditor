package bus.uigen.controller.models;

import java.awt.Frame;

import bus.uigen.ObjectEditor;
import bus.uigen.myLockManager;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeEditor;
import bus.uigen.controller.MethodParameters;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.visitors.UpdateAdapterVisitor;

public class MiscOperations {
	public static void update(uiFrame frame) {
		ObjectAdapter selection = (ObjectAdapter) SelectionManager.getCurrentSelection();
		if (selection != null) {
			doUpdate(frame, selection);
		} 
		
	}
	 static void doUpdate (uiFrame frame, ObjectAdapter   adapter) {
		(new UpdateAdapterVisitor(adapter)).traverse();
		//adapter.uiComponentValueChanged();
		frame.doImplicitRefresh();
	}
	public  static void settings() {
		ObjectEditor.edit(new  MethodParameters());
		/*
		uiFrame sf = uiGenerator.generateUIFrame(new uiParameters(), (myLockManager) null);
		sf.setVisible(true);
		*/
		
	}
	public  static void selection() {
		ObjectAdapter selected = (ObjectAdapter) SelectionManager.getCurrentSelection();
		if (selected != null) {
			System.err.println("Creating editor");
			Frame   editor = new AttributeEditor(selected);
		} else
			System.err.println("Selection   is null");
		
	}
	
	
}
