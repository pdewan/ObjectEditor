package bus.uigen.sadapters;import bus.uigen.uiFrame;
import bus.uigen.reflect.ClassProxy;//import bus.uigen.introspect.*;
import java.lang.reflect.Method;//import bus.uigen.controller.uiMethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericPropertyDescriptorToHashtableStructureFactory extends GenericHashtableToHashtableStructureFactory   {
		public HashtableStructure toHashtableStructure(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {
		//if (!(uiBean.isVector(theGVectorClass))) return null;
		if (!isHashtable(theTargetClass)) return null;
		//System.out.println("Found Map!");		return  new GenericPropertyDescriptorToHashtableStructure(theTargetObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}
	public GenericPropertyDescriptorToHashtableStructure createChecker() {		return new GenericPropertyDescriptorToHashtableStructure();
	}	
	public Class getConcreteType () {		return GenericPropertyDescriptorToHashtableStructure.class;
	}
	public ConcreteType createConcreteType () {		return new GenericPropertyDescriptorToHashtableStructure();
	}	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.PROPERTY_DESCRIPTOR_PATTERN;	}	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.PROPERTY_DESCRIPTOR_PATTERN";	}		
}
