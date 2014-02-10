package bus.uigen.sadapters;import bus.uigen.uiFrame;
import bus.uigen.reflect.ClassProxy;import bus.uigen.reflect.MethodProxy;//import bus.uigen.introspect.*;
import java.lang.reflect.Method;//import bus.uigen.controller.uiMethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericSimpleTableToHashtableStructureFactory extends  GenericHashtableToHashtableStructureFactory implements HashtableStructureFactory  {
		
	public boolean isHashtable (ClassProxy theTargetClass) {				//GenericHashtableToHashtableStructure checker = new GenericHashtableToHashtableStructure();		AbstractHashtableToHashtableStructure checker = createChecker();
		/*		System.out.println("keys method " + checker.getKeysMethod(theTargetClass) +
						   " elements method " + checker.getElementsMethod(theTargetClass) +
						   " get method " + checker.getGetMethod(theTargetClass) +
						   " put method " + checker.getPutMethod(theTargetClass));		*/		MethodProxy getMethod;
		return //				checker.getKeysMethod(theTargetClass) != null &&				//checker.getElementsMethod(theTargetClass) != null &&			   (getMethod = checker.getGetMethod(theTargetClass)) != null;
			   //&&  checker.getPutMethod(theTargetClass, getMethod) != null;
	}
		@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.SIMPLE_TABLE_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.SIMPLE_TABLE_PATTERN";	}	
}
