package bus.uigen.sadapters;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericArcToArcFactory 	extends GenericShapeToShapeFactory implements ConcreteShapeFactory  {
		
	public Class getConcreteType () {		return GenericArcToArc.class;
	}
	public ConcreteType createConcreteType () {		return new GenericArcToArc();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.ARC_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.ARC_PATTERN";	}     public ConcreteLocatableShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {		 		if (theTargetObject == null || ! 				 				IntrospectUtility.isDeclaredArc(theTargetClass))  				return null;				return  new GenericArcToArc(theTargetObject, theFrame);		   			}     public boolean useInSearch() { 		return false; 	}	
}
