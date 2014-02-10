package bus.uigen.sadapters;import bus.uigen.uiFrame;import bus.uigen.reflect.ClassProxy;public class GenericEnumerationToEnumerationFactory extends  AbstractConcreteTypeFactory implements ConcreteEnumerationFactory  {	 public static ConcreteEnumeration toConcreteEnumerationStatic(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isEnumeration(theTargetClass, theTargetObject)) return null;		return  new GenericEnumerationToEnumeration(theTargetObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public static boolean isEnumeration (ClassProxy theTargetClass, Object theTargetObject) {				//GenericHashtableToHashtableStructure checker = new GenericHashtableToHashtableStructure();		GenericEnumerationToEnumeration checker = createChecker();		checker.setMethods(theTargetClass, theTargetObject);		return checker.isEnumeration();
	}
	public static GenericEnumerationToEnumeration createChecker() {		return new GenericEnumerationToEnumeration();
	}
	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toConcreteEnumeration(theTargetClass, theTargetObject, theFrame);	}
	public Class getConcreteType () {		return GenericEnumerationToEnumeration.class;
	}
	public ConcreteType createConcreteType () {		return createChecker();
	}	public  ConcreteEnumeration toConcreteEnumeration(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {		return toConcreteEnumerationStatic (theTargetClass, theTargetObject, theFrame);		/*		//if (!(uiBean.isVector(theGVectorClass))) return null;		if (!isEnumeration(theTargetClass, theTargetObject)) return null;		return  new GenericEnumerationToEnumeration(theTargetObject, theFrame);				//return vectorStructure;				//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);		 * 		 */	}	public static  ConcreteEnumeration toConcreteEnumeration(ClassProxy theTargetClass) {		return toConcreteEnumerationStatic (theTargetClass, null, null);		/*		//if (!(uiBean.isVector(theGVectorClass))) return null;		if (!isEnumeration(theTargetClass, theTargetObject)) return null;		return  new GenericEnumerationToEnumeration(theTargetObject, theFrame);				//return vectorStructure;				//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);		 * 		 */	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.ENUM_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.ENUM_PATTERN";	}	
}
