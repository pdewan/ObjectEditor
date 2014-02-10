package bus.uigen.oadapters;
import java.awt.Container;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcretePrimitive;
public class PrimitiveAdapterFactory extends AbstractObjectAdapterFactory {
	/*
	public  uiObjectAdapter createObjectAdapter (ConcreteType concreteObject,												Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {		return uiPrimitiveAdapter.createPrimitiveAdapter (//concreteObject,														  containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	}
	*/
	public Class getConcreteType () {
		return ConcretePrimitive.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new PrimitiveAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
