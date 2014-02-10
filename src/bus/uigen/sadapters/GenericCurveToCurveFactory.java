package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericCurveToCurveFactory 	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericCurveToCurve.class;
	}
	public ConcreteType createConcreteType () {		return new GenericCurveToCurve();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.CURVE_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.CURVE_PATTERN";	}    public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !IntrospectUtility.isDeclaredCurve(theTargetClass)) return null;				return  new GenericCurveToCurve(theTargetObject, theFrame);		   			}    public boolean useInSearch() { 		return false; 	}	
}
