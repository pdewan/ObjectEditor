package bus.uigen.sadapters;

import util.annotations.StructurePatternNames;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.CommandListener;

public class GenericLabelShapeToLabelShape extends
	GenericTextShapeToTextShape
	implements ConcreteLabelShape {
	transient MethodProxy getImageFileNameMethod = null;
  transient MethodProxy setImageFileNameMethod = null;
	
	public GenericLabelShapeToLabelShape (Object theTargetObject, uiFrame theFrame) {
		init(theTargetObject, theFrame );
	}
	
	public GenericLabelShapeToLabelShape () {
	}
	public void setMethods(ClassProxy c) {
		super.setMethods(c);
		setLabelMethods(c);
	}
  
  public void setLabelMethods (ClassProxy c) {
	  
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
  public static String LABEL = "Label";
	public String programmingPatternKeyword() {
		return  super.programmingPatternKeyword() + AttributeNames.KEYWORD_SEPARATOR + LABEL;
	}
	public String typeKeyword() {
		return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + LABEL ;
	}
	public String getPatternName() {
		return StructurePatternNames.LABEL_PATTERN;		
	}

}
