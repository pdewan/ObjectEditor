package bus.uigen.oadapters;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.*;
import java.awt.Container;
public abstract class AbstractObjectAdapterFactory implements ObjectAdapterFactory {
	
	public  ObjectAdapter createObjectAdapter (ConcreteType concreteObject,												/*Container containW,*/
										   Object viewObject, 											  Object realObject, 											  Object parentObject, 
											  int posn,											  String name, 											  ClassProxy realClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) {
		
		ObjectAdapter retValue = createObjectAdapter();
		/*
		if (retValue instanceof uiContainerAdapter) 
			System.out.println("***D" + ((uiContainerAdapter) retValue).getDirection());		*/
		retValue.init(concreteObject,													//containW, 
													viewObject, 
									realObject, 									parentObject, 
									posn,									name, 									realClass, 									propertyFlag, 									 adaptor,
									 textMode);
		/*
		if (retValue instanceof uiContainerAdapter) 
			System.out.println("***D" + ((uiContainerAdapter) retValue).getDirection());
		*/		return retValue;
	}
	
	public abstract ObjectAdapter createObjectAdapter();
	
	
	
}
