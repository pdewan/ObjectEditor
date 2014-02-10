package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTextShape;
import java.awt.Container;
public class TextShapeAdapterFactory extends AbstractObjectAdapterFactory {
	/*
	public  uiObjectAdapter createObjectAdapter (ConcreteType concreteObject,												Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {		return uiClassAdapter.createClassAdapter (containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	}
	*/
	public Class getConcreteType () {
		return ConcreteTextShape.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new TextShapeAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
