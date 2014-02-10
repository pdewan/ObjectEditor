package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteLabelShape;
import java.awt.Container;
public class LabelShapeAdapterFactory extends AbstractObjectAdapterFactory {
	/*
	public  uiObjectAdapter createObjectAdapter (ConcreteType concreteObject,												Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {		return uiClassAdapter.createClassAdapter (containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	}
	*/
	public Class getConcreteType () {
		return ConcreteLabelShape.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new LabelShapeAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
