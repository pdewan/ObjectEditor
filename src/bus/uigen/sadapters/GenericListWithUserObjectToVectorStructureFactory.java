package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericListWithUserObjectToVectorStructureFactory extends GenericListToVectorStructureFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theGVectorClass, false)) return null;		Object firstChildIsUserObject = ObjectAdapter.getAttribute(theGVectorClass, AttributeNames.FIRST_CHILD_IS_USER_OBJECT);		if (firstChildIsUserObject == null) return null;		if (! (Boolean) firstChildIsUserObject) return null;		VectorStructure vectorStructure =  new GenericListToVectorStructure(theGVectorObject, theFrame);				if (vectorStructure.size() < 1) return null;		if (! (vectorStructure.elementAt(0).getClass() == String.class)) return null;			return  new GenericListWithUserObjectToVectorStructure(theGVectorObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	
	public Class getConcreteType () {		return GenericListWithUserObjectToVectorStructure.class;
	}
	public ConcreteType createConcreteType () {		return new GenericListWithUserObjectToVectorStructure();
	}	
}
