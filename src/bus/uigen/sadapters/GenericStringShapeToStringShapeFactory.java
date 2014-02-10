package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericStringShapeToStringShapeFactory 	extends GenericTextShapeToTextShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericStringShapeToStringShape.class;	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.STRING_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.STRING_PATTERN";	}    public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !IntrospectUtility.isDeclaredString(theTargetClass)) return null;				return  new GenericStringShapeToStringShape(theTargetObject, theFrame);		   			}    public boolean useInSearch() { 		return false; 	}	
}
