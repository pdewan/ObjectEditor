package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericOvalToOvalFactory 	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
	public Class getConcreteType () {		return GenericOvalToOval.class;	}	public ConcreteType createConcreteType () {		return new GenericOvalToOval();	}	/*
	public Class getConcreteType () {		return GenericOvalToOval.class;
	}
	public ConcreteType createConcreteType () {		return new GenericOvalToOval();
	}	*/	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.OVAL_PATTERN;	}	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.OVAL_PATTERN";	}    public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {				if (theTargetObject == null || !IntrospectUtility.isDeclaredOval(theTargetClass)) return null;				return  new GenericOvalToOval(theTargetObject, theFrame);		   			}    public boolean useInSearch() { 		return false; 	}	
}
