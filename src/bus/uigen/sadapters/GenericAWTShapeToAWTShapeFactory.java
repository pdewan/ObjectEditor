package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericAWTShapeToAWTShapeFactory 	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericAWTShapeToAWTShape.class;
	}
	public ConcreteType createConcreteType () {		return new GenericAWTShapeToAWTShape();
	}	@Override	public String getPatternName() {		return util.annotations.StructurePatternNames.AWT_SHAPE_PATTERN;	}	@Override	public String getPatternPath() {		return "util.annotations.StructurePatternNames.AWT_SHAPE_PATTERN";	}    public ConcreteShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !				(IntrospectUtility.isDeclaredAWTShape(theTargetClass) || IntrospectUtility.hasDeclaredAWTShape(theTargetClass))) 			return null;				return  new GenericAWTShapeToAWTShape(theTargetObject, theFrame);		   			}    public boolean useInSearch() { 		return true; 	}	
}
