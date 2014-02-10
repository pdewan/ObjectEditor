package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;

import util.annotations.StructurePatternNames;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;
public class GenericImageShapeToImageShape extends 
	//GenericXYShapeToShape 
	GenericBoundedShapeToBoundedShape 
	implements ConcreteImageShape {
	transient MethodProxy getImageFileNameMethod = null;
	  transient MethodProxy setImageFileNameMethod = null;	
	public GenericImageShapeToImageShape (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericImageShapeToImageShape () {
	}
	public String getPatternName() {
		return StructurePatternNames.IMAGE_PATTERN;		
	}
	
		
		
		public void setMethods(ClassProxy c) {
			super.setMethods(c);
			setImageMethods(c);
		}
	  
	  public void setImageMethods (ClassProxy c) {
		  
		  getImageFileNameMethod = IntrospectUtility.getGetStringMethod(c, "ImageFileName");
		  setImageFileNameMethod = IntrospectUtility.getSetStringMethod(c, "ImageFileName");
		  
	  }
	  Object[] emptyParams = {};
	  public String getImageFileName() {
		  if (getImageFileNameMethod == null) return null;
		  return  ((String) MethodInvocationManager.invokeMethod(getImageFileNameMethod, targetObject, emptyParams));
	  }
	  public void setImageFileName (String newValue, CommandListener cl) {
		  Object[] params = {newValue};
		   MethodInvocationManager.invokeMethod(frame, targetObject, setImageFileNameMethod, params, cl);
	  }
	  public void setImageFileName (String newValue) {
		  if (setImageFileNameMethod == null) return;
		  Object[] params = {new String(newValue)};
		  MethodInvocationManager.invokeMethod(setImageFileNameMethod, targetObject, params);
	  }
	  
	  public boolean isImageFileNameReadOnly() {
		  return setImageFileNameMethod == null;
	  }
	
	
  
}