package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericVectorToVectorStructureFactory extends  AbstractConcreteTypeFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theGVectorClass, forceConversion )) return null;		return  new GenericVectorToVectorStructure(theGVectorObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}	
	public boolean isVector (ClassProxy theGVectorClass, boolean expected) {
		return IntrospectUtility.isVector(theGVectorClass, expected);		
	}	
	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame, forceConversion);	}	public Class getConcreteType () {		return GenericVectorToVectorStructure.class;
	}	public ConcreteType createConcreteType () {		return new GenericVectorToVectorStructure();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.VECTOR_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.VECTOR_PATTERN";	}
		
}
