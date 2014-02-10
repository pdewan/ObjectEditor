package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;

import util.annotations.StructurePatternNames;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;
public class GenericStringShapeToStringShape extends 
	//GenericXYShapeToShape 
	GenericTextShapeToTextShape 
	implements ConcreteStringShape {
		
	public GenericStringShapeToStringShape (Object theTargetObject, uiFrame theFrame) {		super(theTargetObject, theFrame );
	}
		public GenericStringShapeToStringShape () {
	}
	public String getPatternName() {
		return StructurePatternNames.STRING_PATTERN;		
	}
	
  
}