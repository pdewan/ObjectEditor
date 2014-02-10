package bus.uigen.oadapters;
import java.awt.Container;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.HashtableStructure;
public class HashtableAdapterFactory extends AbstractObjectAdapterFactory {
	/*
	public  uiObjectAdapter createObjectAdapter (ConcreteType concreteObject,												Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {		return uiHashtableAdapter.createHashtableAdapter (concreteObject, containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	}
	*/
	public Class getConcreteType () {
		return HashtableStructure.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new HashtableAdapter();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
