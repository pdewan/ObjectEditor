package bus.uigen.sadapters;import bus.uigen.uiFrame;
import bus.uigen.introspect.ClassDescriptorCache;import bus.uigen.introspect.ClassDescriptorInterface;import bus.uigen.reflect.ClassProxy;//import bus.uigen.introspect.*;
import java.lang.reflect.Method;//import bus.uigen.controller.uiMethodInvocationManager;
import util.trace.Tracer;
//public class uiVectorAdapter extends uiContainerAdapterpublic class BeanToRecordFactory extends AbstractConcreteTypeFactory implements RecordFactory  {
		public RecordStructure toRecordStructure(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {
				return  new BeanToRecord (theTargetClass, theTargetObject, theFrame);		
		//return vectorStructure;		
		//return new GenericVectorToVectorStructure(theGVectorClass, theGVector);	}	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		if (forceConversion) {			ClassDescriptorInterface cdesc;				if (theTargetObject == null)			 cdesc = ClassDescriptorCache.getClassDescriptor(theTargetClass);		else			cdesc = ClassDescriptorCache.getClassDescriptor(theTargetClass, theTargetObject);		if (cdesc.getPropertyDescriptors().length == 0)  {			Tracer.error("Expected one or more programmer-defined properties in class: " + theTargetClass.getName());			return null;		}		}		return toRecordStructure(theTargetClass, theTargetObject, theFrame);	}
	
	public Class getConcreteType () {		return BeanToRecord.class;
	}		public String getPatternName() {		return util.annotations.StructurePatternNames.BEAN_PATTERN;			}	public String getPatternPath() {		return "util.annotations.StructurePatternNames.BEAN_PATTERN";			}
	public ConcreteType createConcreteType () {		return new BeanToRecord();
	}	
}
