package bus.uigen.oadapters;
import java.awt.Container;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.ConcreteType;
public interface ObjectAdapterFactory {
	public ObjectAdapter createObjectAdapter (ConcreteType concreteObject,												/*Container containW,*/
										   Object obj, 											  Object obj1, 											  Object parentObject, 
											  int posn,											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode);
	public Class getConcreteType ();
	
	
}
