package bus.uigen.view;

import java.beans.MethodDescriptor;
import java.util.Enumeration;

import util.models.AListenableHashtable;

import bus.uigen.uiFrame;
import bus.uigen.controller.menus.AMethodProcessor;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;

public class AnnotationManager {
	uiFrame frame;
	AListenableHashtable<AListenableHashtable<String, String>, String> methodAnnotationTables;
	//AListenableHashtable<AListenableHashtable<String, String>, String> methodAnnotationTables = new AListenableHashtable();
	//AListenableHashtable methodAnnotationTables = new AListenableHashtable();
	//AListenableHashtable<String, AListenableHashtable<String, String>> inverseMethodAnnotationTables = new AListenableHashtable();
	AListenableHashtable<String, AListenableHashtable<String, String>> menuNameToAnnotationTable;
	//AListenableHashtable<String, String> methodAnnotations = new AListenableHashtable();
	
	public AnnotationManager(uiFrame theFrame) {
		frame = theFrame;
		//methodAnnotationTables = new AListenableHashtable();
		//AListenableHashtable methodAnnotationTables = new AListenableHashtable();
		menuNameToAnnotationTable = new AListenableHashtable();
	}

	public void put (MethodDescriptorProxy md, String text, ObjectAdapter theAdapter, ClassProxy cl) {
		if (text != null && !text.equals("")) {
			String menuName = AMethodProcessor.getMethodMenuName(theAdapter, md, cl);
			AListenableHashtable<String, String> methodAnnotations = getMenuTable(menuName);
			methodAnnotations.put(AMethodProcessor.getLabel(md), text);
		}
	}
	
	//AListenableHashtable<String, String> getMenuTable(String menuName) {
	public AListenableHashtable<String, String> getMenuTable(String menuName) {
		
		AListenableHashtable<String, String> retVal = menuNameToAnnotationTable.get(menuName);
		if (retVal == null) {
			retVal = new AListenableHashtable();
			menuNameToAnnotationTable.put(menuName, retVal);
			retVal.setUserObject(menuName);
			//methodAnnotationTables.put(retVal, "This is an annotation");
			//Object val = methodAnnotationTables.get(retVal);
			//System.out.println("Put Val:" + val);
		}
		return retVal;
	}
	
	public AListenableHashtable<AListenableHashtable<String, String>, String> getAnnoationTables() {
		if (methodAnnotationTables != null)
			return methodAnnotationTables;
		methodAnnotationTables = new AListenableHashtable();
		Enumeration<AListenableHashtable<String, String>> elements = menuNameToAnnotationTable.elements();
		while (elements.hasMoreElements()) {
			AListenableHashtable<String, String> nextTable = elements.nextElement();
			methodAnnotationTables.put(nextTable, "");			
		}
		return methodAnnotationTables;
	}

}
