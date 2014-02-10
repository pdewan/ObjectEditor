package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericPointToPointFactory	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericPointToPoint.class;
	}
	public ConcreteType createConcreteType () {		return new GenericPointToPoint();
	}	public String getPatternName() {		return util.annotations.StructurePatternNames.POINT_PATTERN;			}	public String getPatternPath() {		return "util.annotations.StructurePatternNames.POINT_PATTERN";			}	public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !IntrospectUtility.isDeclaredPoint(theTargetClass)) return null;				return  new GenericPointToPoint(theTargetObject, theFrame);		   			}	 public boolean useInSearch() {	 		return false;	 	}	
}
