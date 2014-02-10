package bus.uigen.sadapters;import bus.uigen.uiFrame;
import bus.uigen.Primitive;import bus.uigen.reflect.ClassProxy;public class GenericPrimitiveToPrimitiveFactory extends  AbstractConcreteTypeFactory implements ConcretePrimitiveFactory  {	public ConcretePrimitive toConcretePrimitive(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isPrimitive(theTargetClass)) return null;		return  new GenericPrimitiveToPrimitive(theTargetObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public boolean isPrimitive (ClassProxy theTargetClass) {				//GenericHashtableToHashtableStructure checker = new GenericHashtableToHashtableStructure();		GenericPrimitiveToPrimitive checker = createChecker();		return /*theTargetObject instanceof uiPrimitive ||*/ checker.isPrimitiveClass(theTargetClass);		
	}
	public GenericPrimitiveToPrimitive createChecker() {		return new GenericPrimitiveToPrimitive();
	}
	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toConcretePrimitive(theTargetClass, theTargetObject, theFrame);	}
	public Class getConcreteType () {		return GenericPrimitiveToPrimitive.class;
	}
	public ConcreteType createConcreteType () {		return new GenericPrimitiveToPrimitive();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return null;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return null;	}		
}
