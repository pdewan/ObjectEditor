package bus.uigen.test;

import java.util.List;

//import com.sun.xml.internal.messaging.saaj.util.TeeInputStream;

import util.models.AListenableVector;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
import util.trace.Tracer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.shapes.ARectangleModel;

public class AListenableVectorDriver implements VectorListener{
	public static void main (String[] args) {
//		ObjectEditor.setDefaultAttribute(AttributeNames.SHOW_SYSTEM_MENUS, false);
//		Tracer.showInfo(true);
		AListenableVector vector = new AListenableVector();
		vector.addVectorListener(new AListenableVectorDriver());
//		ObjectEditor.setDefaultAttribute(AttributeNames.INVISIBLE_IF_NULL, true);
//		System.out.println(5);
		OEFrame frame =		ObjectEditor.edit(vector);
//		OEFrame frame =		ObjectEditor.graphicsOnlyEdit(vector);


		vector.add("hello");
//		vector.add("bye");
//		((List) vector).remove(0);
//		vector.add("hello");
		
		ObjectEditor.setPropertyComponentWidth(AListenableVector.class, AttributeNames.ANY_ELEMENT, 300);
//		OEFrame frame =		ObjectEditor.edit(vector);
		vector.add(new ARectangleModel (100, 100, 100, 100));

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

	@Override
	public void updateVector(VectorChangeEvent evt) {
//		System.out.println(evt.getEventType());
	}

}
