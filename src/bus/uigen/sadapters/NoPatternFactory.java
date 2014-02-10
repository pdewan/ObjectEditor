package bus.uigen.sadapters;import bus.uigen.uiFrame;
import bus.uigen.introspect.ClassDescriptorCache;import bus.uigen.introspect.ClassDescriptorInterface;import bus.uigen.introspect.IntrospectUtility;import bus.uigen.reflect.ClassProxy;//import bus.uigen.introspect.*;
import java.lang.reflect.Method;//import bus.uigen.controller.uiMethodInvocationManager;
import util.trace.Tracer;
//public class uiVectorAdapter extends uiContainerAdapterpublic class NoPatternFactory extends  AbstractConcreteTypeFactory implements ConcreteTypeFactory  {
			public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		if (forceConversion) {			ClassDescriptorInterface cdesc;				if (theTargetObject == null)			 cdesc = ClassDescriptorCache.getClassDescriptor(theTargetClass);		else			cdesc = ClassDescriptorCache.getClassDescriptor(theTargetClass, theTargetObject);		if (cdesc.getMethodDescriptors().length == 0 && theTargetClass != IntrospectUtility.objectClass())  {			Tracer.error("Expected one or more public methods in class: " + theTargetClass.getName());			return null;		}		}				return new NoPattern(theTargetClass, theTargetObject, theFrame);	}
	
	public Class getConcreteType () {		return NoPattern.class;
	}		public String getPatternName() {		return util.annotations.StructurePatternNames.NO_PATTERN;			}	public String getPatternPath() {		return "util.annotations.StructurePatternNames.NO_PATTERN";			}
	public ConcreteType createConcreteType () {		return new NoPattern();
	}	
}
