package bus.uigen.test;

import java.awt.Color;

import util.misc.ThreadSupport;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.ObjectAdapter;

public class ACompositeOpener extends ACompositeObservable {
//	OEFrame frame;
//	public ACompositeColorer(OEFrame aFrame) {
//		frame = aFrame;
//	}
//	
//	public void setString(String newVal) {
//		super.setString(newVal);
//		if (newVal.isEmpty()) {
//			propertyChangeSupport.firePropertyChange("String", null,
//					new Attribute(AttributeNames.CONTAINER_BACKGROUND, Color.PINK));
//		} else {
//			propertyChangeSupport.firePropertyChange("String", null,
//					new Attribute(AttributeNames.CONTAINER_BACKGROUND, null));
//			propertyChangeSupport.firePropertyChange("String", null,
//					new Attribute(AttributeNames.COMPONENT_BACKGROUND, Color.GREEN));
//			propertyChangeSupport.firePropertyChange("Int", null,
//					new Attribute(AttributeNames.COMPONENT_FOREGROUND, Color.RED));
//		}
//
//	}
	// This should be called when any string is double clicked,
	public void foo(String aString) {
			System.out.println("print called with:" + aString);
	}
	// This should be called when any string is double clicked,
	public void open(String aString) {
		System.out.println("open called with:" + aString);
	}
	public static void main (String[] args) {
		ObjectEditor.setMethodAttribute(ACompositeOpener.class, "foo", AttributeNames.DOUBLE_CLICK_METHOD, true);
		CompositeExample example = new ACompositeOpener();
		OEFrame mainFrame = ObjectEditor.edit(example);	
		ObjectEditor.treeEdit(example);
//		example.setAString("");
//		ClassAdapter compositeAdapter = (ClassAdapter) mainFrame.getObjectAdapter(example);
//		
//		ObjectAdapter textAdapter = compositeAdapter.get("AString");
//		
//		Object adapter = mainFrame.getObjectAdapterFromPath("");
//		 adapter = mainFrame.getObjectAdapterFromPath("astring");
//		example.set("hello", 1);
//		ThreadSupport.sleep(1000);
//		compositeAdapter.setTempAttributeValue(AttributeNames.CONTAINER_BACKGROUND, Color.BLUE);
////		mainFrame.refresh();
////		compositeAdapter.getWidgetAdapter().setAttributes();
//		compositeAdapter.propagateAttributesToWidgetAdapter();
//		compositeAdapter.propagateAttributesToWidgetShell();
////		compositeAdapter.getWidgetAdapter().processAttribute(new Attribute(AttributeNames.CONTAINER_BACKGROUND, Color.PINK));
//		ThreadSupport.sleep(1000);
////		textAdapter.getWidgetAdapter().processAttribute(new Attribute(AttributeNames.COMPONENT_BACKGROUND, Color.PINK));
//		textAdapter.setTempAttributeValue(AttributeNames.CONTAINER_BACKGROUND, Color.PINK);
////		textAdapter.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, Color.PINK);
//
//		textAdapter.propagateAttributesToWidgetAdapter();
//		textAdapter.propagateAttributesToWidgetShell();
//
////		textAdapter.getWidgetAdapter().setAttributes();
//
//
//
//		mainFrame.refresh();

		
	}

}
