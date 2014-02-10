package bus.uigen.sadapters;import util.trace.Tracer;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.reflect.MethodProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericListToVectorStructureFactory extends  AbstractConcreteTypeFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theGVectorClass, forceConversion)) return null;		return  new GenericListToVectorStructure(theGVectorObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public boolean isVector (ClassProxy theGVectorClass, boolean expected) {		if (theGVectorClass == IntrospectUtility.arrayListClass()) return true;		GenericListToVectorStructure checker = new GenericListToVectorStructure();		MethodProxy sizeMethod =  checker.getSizeMethod(theGVectorClass);		if (sizeMethod != null) {			MethodProxy elementAtMethod =  checker.getElementAtMethod(theGVectorClass, expected);			return (elementAtMethod != null);		} else {			boolean hasElementsMethod = checker.getElementAtMethod(theGVectorClass, false) != null;			if (!hasElementsMethod && expected) { 				Tracer.error("Expecting in class: " +  theGVectorClass.getName() + " the size method with header: public int size()");				checker.getElementAtMethod(theGVectorClass, expected);				return false;			} else return hasElementsMethod;					}
	}	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame, forceConversion);	}
	public Class getConcreteType () {		return GenericListToVectorStructure.class;
	}
	public ConcreteType createConcreteType () {		return new GenericListToVectorStructure();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.LIST_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.LIST_PATTERN";	}	
}
