package bus.uigen.test;

import java.util.List;
import java.util.Vector;

import util.models.AListenableVector;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

public class AVectorDriver {
	ACompositeExample template = new ACompositeExample();
	List<ACompositeExample> list = new Vector();
	
	public ACompositeExample getTemplate() {
		return template;
	}

	public void setTemplate(ACompositeExample template) {
		this.template = template;
	}

	public List<ACompositeExample> getList() {
		return list;
	}

	public void setList(List<ACompositeExample> list) {
		this.list = list;
	}
	
	public void addTemplate() {
		list.add(new ACompositeExample(template.getAString(), template.getAnInt()));
	}

	public static void main (String[] args) {
//		ObjectEditor.setDefaultAttribute(AttributeNames.SHOW_SYSTEM_MENUS, false);
		ObjectEditor.edit(new Vector());
		AVectorDriver vectorDriver = new AVectorDriver();
//		vectorDriver.addTemplate();
//		vector.add("String");
//		vector.addVectorListener(new AVectorDriver());
//		vector.add("hello");
//		vector.add("bye");
////		((List) vector).remove(0);
//		vector.add("hello");
		
		
//		ObjectEditor.setPropertyComponentWidth(AListenableVector.class, AttributeNames.ANY_ELEMENT, 300);
		OEFrame frame =		ObjectEditor.tabEdit(vectorDriver);
//		frame.setDemoFont();
//		try {
//			frame.select(vector, 0);
//			Thread.sleep (5000);
//			frame.select(vector, 1);
//			Thread.sleep (5000);
//			frame.select(vector, 1);
//			Thread.sleep (5000);
//		
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

//	@Override
//	public void updateVector(VectorChangeEvent evt) {
//		System.out.println(evt.getEventType());
//	}

}
