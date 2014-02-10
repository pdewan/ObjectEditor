package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericStackToVectorStructureFactory extends  AbstractConcreteTypeFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theGVectorClass)) return null;		return  new GenericStackToVectorStructure(theGVectorObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public boolean isVector (ClassProxy theGVectorClass) {		GenericStackToVectorStructure checker = new GenericStackToVectorStructure();
		return (checker.getElementAtMethod(theGVectorClass, false) != null &&				checker.getSizeMethod(theGVectorClass) != null) ||			    checker.getElementsMethod(theGVectorClass, false) != null;		
	}	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame, false);	}
	public Class getConcreteType () {		return GenericStackToVectorStructure.class;
	}
	public ConcreteType createConcreteType () {		return new GenericStackToVectorStructure();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.STACK_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.STACK_PATTERN";	}	
}
