package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericLineToLineFactory 	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericLineToLine.class;
	}
	public ConcreteType createConcreteType () {		return new GenericLineToLine();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.LINE_PATTERN;	}	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.LINE_PATTERN";	}     public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {		 		if (theTargetObject == null || !IntrospectUtility.isDeclaredLine(theTargetClass)) return null;				return  new GenericLineToLine(theTargetObject, theFrame);		   			}     public boolean useInSearch() {  		return false;  	}	
}
