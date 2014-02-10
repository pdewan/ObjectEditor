package bus.uigen.sadapters;import bus.uigen.uiFrame;
import bus.uigen.reflect.ClassProxy;//import bus.uigen.introspect.*;
import java.lang.reflect.Method;//import bus.uigen.controller.uiMethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericMapToHashtableStructureFactory extends GenericHashtableToHashtableStructureFactory   {
		public HashtableStructure toHashtableStructure(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isHashtable(theTargetClass)) return null;
		//System.out.println("Found Map!");		return  new GenericMapToHashtableStructure(theTargetObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public AbstractHashtableToHashtableStructure createChecker() {		return new GenericMapToHashtableStructure();
	}	
	public Class getConcreteType () {		return GenericMapToHashtableStructure.class;
	}
	public ConcreteType createConcreteType () {		return new GenericMapToHashtableStructure();
	}	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.MAP_PATTERN;	}	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.MAP_PATTERN";	}	
}
