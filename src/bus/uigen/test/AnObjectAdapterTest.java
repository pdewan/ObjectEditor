package bus.uigen.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class AnObjectAdapterTest {
	public static void main (String[] args) {
		heenanTest();
		editAndAnalyzeDate();
		editAndAnalyzeList();	
		analyzeDateWithoutEditing();
	}
	
	public static void editAndAnalyzeDate() {
		System.out.println("Displaying and Analyzing Date");
		uiFrame oeFrame = (uiFrame) ObjectEditor.edit(new Date());
		ObjectAdapter dateAdapter = oeFrame.getTopAdapter();
		processDateAdapter ((ClassAdapter) dateAdapter);
	}
	
	public static void analyzeDateWithoutEditing() {
		System.out.println("Analyzing Date");
		ObjectAdapter dateAdapter = ObjectEditor.toObjectAdapter(new Date());
		processDateAdapter ((ClassAdapter) dateAdapter);
	}
	
	public static void processDateAdapter(ClassAdapter dateAdapter) {
		ConcreteType dateConcreteType = dateAdapter.getConcreteObject();
		// look at the various classes returned for concreteType and they should give you the methods you need
		RecordStructure concreteRecord = (BeanToRecord) dateConcreteType;
		Vector<String> componentNames =  concreteRecord.componentNames();
		for (String componentName:componentNames) {
			System.out.println(componentName +  ":" + concreteRecord.get(componentName));			
		}
		
	}
	public static void editAndAnalyzeList() {
		System.out.println("Displaying and Analyzing List");
		List<Date> list = new ArrayList();
		list.add(new Date());
		list.add(new Date());
		uiFrame oeFrame = (uiFrame) ObjectEditor.edit(list);
		ObjectAdapter listAdapter = oeFrame.getTopAdapter();
		processListAdapter(listAdapter);
	}
	
	public static void processListAdapter(ObjectAdapter listAdapter) {
		// TreeNode is a standard Java Type and you can use it to decompose the whole object structure
		// You can also cast any tree node to an ObjectAdapter
		TreeNode listTreeNode = (TreeNode) listAdapter;
		ConcreteType listConcreteType = listAdapter.getConcreteObject();
		VectorStructure vectorStructure = (VectorStructure) listConcreteType;
		for (int i = 0; i < vectorStructure.size(); i++ ) {
			System.out.println(i);
			System.out.println(vectorStructure.elementAt(i));
		}		
		for (int i = 0; i < listTreeNode.getChildCount(); i++) {
			ClassAdapter dateAdapter = (ClassAdapter) listTreeNode.getChildAt(i);
			System.out.println(i);
			processDateAdapter(dateAdapter);			
		}		
	}
	
	public static void heenanTest() {
		 Integer intArray[] = new Integer[10];
	        for (int i = 0; i < intArray.length; i++){ intArray[i] = i;}

	        ObjectAdapter intObjAdapter = ObjectEditor.toObjectAdapter(intArray);
	        System.out.println(" Using ObjectEdtor.toObjectAdapter on " + intArray + " gives: " + intObjAdapter);

	        uiFrame oeFrame = (uiFrame) ObjectEditor.edit(intArray);
	        ObjectAdapter top = oeFrame.getTopAdapter();
	        System.out.println(" Using oeFrame.getTopAdapter on " + intArray + " gives: " + top);


	        ObjectAdapter vectorInt = ObjectEditor.toObjectAdapter(new Vector<Integer>());
//	        ObjectAdapter vectorObjAdapter = ObjectEditor.toObjectAdapter(vectorInt);
//	        System.out.println(" Using ObjectEdtor.toObjectAdapter on " + vectorInt + " gives: " + vectorObjAdapter.toString());

	        oeFrame = (uiFrame) ObjectEditor.edit(vectorInt);
	        top = oeFrame.getTopAdapter();
	        System.out.println(" Using oeFrame.getTopAdapter on " + vectorInt + " gives: " + top);


	        System.out.println(vectorInt);
	}

}
