package bus.uigen.sadapters;import java.lang.reflect.Array;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericVectorWithUserObjectToVectorStructureFactory extends GenericVectorToVectorStructureFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theGVectorClass, false)) return null;		Object firstChildIsUserObject = ObjectAdapter.getAttribute(theGVectorClass, AttributeNames.FIRST_CHILD_IS_USER_OBJECT);		if (firstChildIsUserObject == null) return null;		if (! (Boolean) firstChildIsUserObject) return null;		VectorStructure vectorStructure =  new GenericVectorToVectorStructure(theGVectorObject, theFrame);		
		if (vectorStructure.size() < 1) return null;		if (! (vectorStructure.elementAt(0).getClass() == String.class)) return null;		return new GenericVectorWithUserObjectToVectorStructure(theGVectorObject, theFrame);			//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}	/*
	public boolean isVector (Class theGVectorClass) {
		return uiBean.isVector(theGVectorClass);		
	}	*/
	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame, false);	}	public Class getConcreteType () {		return GenericVectorWithUserObjectToVectorStructure.class;
	}	public ConcreteType createConcreteType () {		return new GenericVectorWithUserObjectToVectorStructure();
	}
		
}
