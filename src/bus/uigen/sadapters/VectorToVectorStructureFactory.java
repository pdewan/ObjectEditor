package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class VectorToVectorStructureFactory extends  AbstractConcreteTypeFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theGVectorClass)) return null;		return  new VectorToVectorStructure(theGVectorObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public boolean isVector(ClassProxy theGVectorClass) {
//			return theGVectorClass.equals(IntrospectUtility.vectorClass());//			return theGVectorClass.isAssignableFrom(IntrospectUtility.vectorClass());		return IntrospectUtility.vectorClass().isAssignableFrom(theGVectorClass);					//					assigna(IntrospectUtility.vectorClass());
	}
	public ConcreteType toConcreteType(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theGVectorClass, theGVectorObject, theFrame, false);
	}
	public Class getConcreteType () {		return VectorToVectorStructure.class;
	}
	public ConcreteType createConcreteType () {		return new VectorToVectorStructure();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		 return util.annotations.StructurePatternNames.VECTOR_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		 return "util.annotations.StructurePatternNames.VECTOR_PATTERN";	}	
}
