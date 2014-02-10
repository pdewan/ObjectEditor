package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteType;
import java.awt.Container;
import bus.uigen.sadapters.ConcreteEnumeration;
public class EnumerationAdapterFactory extends AbstractObjectAdapterFactory {
	/*
	public  uiObjectAdapter createObjectAdapter (ConcreteType concreteObject,												Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {		return uiEnumerationAdapter.createEnumerationAdapter (concreteObject,															  containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	}
	*/
	public Class getConcreteType () {
		return ConcreteEnumeration.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new EnumerationAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
