package bus.uigen.oadapters;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteLocatableShape;
import java.awt.Container;
public class ShapeAdapterFactory  implements ObjectAdapterFactory {
	public  ObjectAdapter createObjectAdapter (ConcreteType concreteObject,												/*Container containW,*/
										   Object obj, 											  Object obj1, 											  Object parentObject,
											  int posn,											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) {		return null;
		/*		return uiShapeAdapter.createShapeAdapter (//concreteObject,												  containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);		*/
	}
	public Class getConcreteType () {
		return ConcreteLocatableShape.class;
	}
	
	
	
}
