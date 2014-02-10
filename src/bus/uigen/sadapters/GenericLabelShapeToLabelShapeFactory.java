package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericLabelShapeToLabelShapeFactory 	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericLabelShapeToLabelShape.class;
	}
	public ConcreteType createConcreteType () {		return new GenericLabelShapeToLabelShape();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.LABEL_PATTERN;	}	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.LABEL_PATTERN";	}     public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !IntrospectUtility.isDeclaredLabel(theTargetClass)) return null;				return  new GenericLabelShapeToLabelShape(theTargetObject, theFrame);		   			}     public boolean useInSearch() {  		return false;  	}	
}
