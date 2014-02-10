package bus.uigen.test;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.util.EventObject;
import java.util.Vector;

import util.models.VectorChangeEvent;

public class ReflectionTest {
	public static void main (String[] args) {
		PropertyChangeEvent event = new PropertyChangeEvent(new Object(), "foo", 5, 6);
		Field[] fields1 = event.getClass().getDeclaredFields();
		System.out.println(fields1);
		VectorChangeEvent vEvent = new VectorChangeEvent(new Vector(), 1, 5);
		Field[] fields2 = vEvent.getClass().getDeclaredFields();		
		System.out.println(fields2);
		Field[] fields3 = EventObject.class.getDeclaredFields();
		System.out.println(fields3);



	}

}
