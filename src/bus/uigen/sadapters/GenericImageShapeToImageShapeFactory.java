package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericImageShapeToImageShapeFactory 	extends GenericTextShapeToTextShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericImageShapeToImageShape.class;	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.IMAGE_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.IMAGE_PATTERN";	}    public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !IntrospectUtility.isDeclaredImage(theTargetClass)) return null;				return  new GenericImageShapeToImageShape(theTargetObject, theFrame);		   			}    public boolean useInSearch() { 		return false; 	}	
}
