package bus.uigen.sadapters;import util.annotations.StructurePatternNames;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class ArrayToVectorStructureFactory extends  AbstractConcreteTypeFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isVector(theTargetClass)) return null;		return  new ArrayToVectorStructure(theTargetObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public boolean isVector (ClassProxy theTargetClass) {
		return theTargetClass.isArray();		
	}	
	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame, false);	}	public Class getConcreteType () {		return ArrayToVectorStructure.class;
	}	public ConcreteType createConcreteType () {		return new ArrayToVectorStructure();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return StructurePatternNames.ARRAY_TYPE;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.ARRAY_TYPE";	}
		
}
