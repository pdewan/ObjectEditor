package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericTextShapeToTextShapeFactory 	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericTextShapeToTextShape.class;
	}
	public ConcreteType createConcreteType () {		return new GenericTextShapeToTextShape();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.TEXT_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.TEXT_PATTERN";	}    public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !IntrospectUtility.isDeclaredText(theTargetClass)) return null;				return  new GenericTextShapeToTextShape(theTargetObject, theFrame);		   			}    public boolean useInSearch() { 		return false; 	}	
}
