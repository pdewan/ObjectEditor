package bus.uigen.oadapters;
import bus.uigen.sadapters.*;
import java.awt.Container;
import java.rmi.RemoteException;
public class VectorAdapterFactory extends AbstractObjectAdapterFactory {
	/*
	public  uiObjectAdapter createObjectAdapter (ConcreteType concreteObject,												Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {
		
		uiVectorAdapter retValue = new uiVectorAdapter();		retValue.init(concreteObject,													containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);		return retValue;
	}
	*/
	public Class getConcreteType () {
		return VectorStructure.class;
	}
	public ObjectAdapter createObjectAdapter()  {
		try {
		return  new VectorAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
