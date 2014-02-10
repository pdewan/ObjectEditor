package bus.uigen.sadapters;import java.lang.reflect.Array;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class ArrayWithUserObjectToVectorStructureFactory extends ArrayToVectorStructureFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theTargetClass)) return null;		if (theTargetObject == null) return null;		if (Array.getLength(theTargetObject) == 0) return null;		if (Array.get(theTargetObject, 0) == null) return null;		if (! (Array.get(theTargetObject, 0).getClass() == String.class)) return null;		//ViewInfo cd = ClassDescriptorCache.getClassDescriptor(theTargetClass);		//Object firstChildIsUserObject = cd.getAttribute(AttributeNames.FIRST_CHILD_IS_USER_OBJECT);		Object firstChildIsUserObject = ObjectAdapter.getAttribute(theTargetClass, AttributeNames.FIRST_CHILD_IS_USER_OBJECT);		if (firstChildIsUserObject == null) return null;		if (! (Boolean) firstChildIsUserObject) return null;				return  new ArrayWithUserObjectToVectorStructure(theTargetObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
		/*
	public ConcreteType toConcreteType(Class theTargetClass, Object theTargetObject, uiFrame theFrame) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame);	}	*/	public Class getConcreteType () {		return ArrayWithUserObjectToVectorStructure.class;
	}	public ConcreteType createConcreteType () {		return new ArrayWithUserObjectToVectorStructure();
	}		
		
}
