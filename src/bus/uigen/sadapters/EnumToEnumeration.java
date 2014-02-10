package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
public class EnumToEnumeration extends GenericPrimitiveToPrimitive
	implements ConcreteEnumeration {
	/*
	transient Class choicesClass = null;
	transient	Class valueClass = null;	transient	Method choicesMethod = null;
	transient	Method readMethod = null;
	transient	Method writeMethod = null;	transient	Field choicesField = null;
	transient	Method choiceAtMethod = null;
	transient	Method choicesSizeMethod = null;
	transient	Class choiceAtClass = null;
	*/
	List<Object> choiceList;
	transient Object[] constants = null;
	ClassProxy outerClass;	
	public EnumToEnumeration (ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, ClassProxy theOuterClass) {		outerClass = theOuterClass;
		init(theTargetClass, theTargetObject, theFrame );
		
	}
		public EnumToEnumeration () {
	}
	public int choicesSize() {
		return constants.length;		  
	  	}
  public Object choiceAt(int i) {
  	if (i >= choicesSize()) return null;
  	return constants[i];
	    }  public Object getValue() {
	  return targetObject;
	  /*
		  Object[] parameters = {};
		  return uiMethodInvocationManager.invokeMethod(readMethod, targetObject, parameters);
		  */		
	    }
  // parent's set property will take care of this  public void setValue(Object newVal, CommandListener commandListener) { 
	  targetObject = newVal;
	  targetClass = RemoteSelector.getClass(newVal);
	  /*
		  Object[] parameters = {newVal};		  uiMethodInvocationManager.invokeMethod (frame, targetObject, writeMethod, parameters, commandListener);
		  */
		    }
  public void setMethods() {
	  setMethods(targetClass);
  }
	// actually constants in this case
  public List getChoices() {
	  if (choiceList != null)
		  return choiceList;
	  choiceList = GenericEnumerationToEnumeration.arrayToList(constants);
	  return choiceList;
  }
	public void setMethods(ClassProxy objectClass) {	
		if (outerClass == null)
			constants = objectClass.getEnumConstants();
		else
			constants = outerClass.getEnumConstants();
		/*
		objectClass.getEnumConstants();
		ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(objectClass);				
		
		PropertyDescriptor[] properties = cdesc.getPropertyDescriptors();
		MethodDescriptor[] methodDescriptors = cdesc.getMethodDescriptors();
		*/		/*
		FieldDescriptor fields[] = cdesc.getFieldDescriptors();		for (int i = 0; i < fields.length; i++) {			FieldDescriptor f = fields[i];			if (f.getName().equals("CHOICES")) {
				choicesField = f.getField();				choicesClass = choicesField.getType();				break;
			}		}
		*/		/*
		for (int i = 0; i < properties.length; i++) {			PropertyDescriptor p = properties[i];
			if (p.getName().equals("value") || p.getName().equals("selectedItem")) {			
					readMethod = p.getReadMethod();
					writeMethod = p.getWriteMethod();					valueClass = p.getPropertyType();					break;
			} 		}		for (int i = 0; i < methodDescriptors.length; i++) {			MethodDescriptor md = methodDescriptors[i];
			if (md.getName().equals("choices")) {				Method m = md.getMethod();				Class parameters[] = m.getParameterTypes();
				Class returnType = m.getReturnType();				if (parameters.length == 0 && returnType == Enumeration.class) {
					choicesMethod = m;				}
			} else if (md.getName().equals("getElementAt") || 
					   md.getName().equals("choiceAt") ||
					   md.getName().equals("getChoiceAt")) {				Method m = md.getMethod();				Class parameterTypes[] = m.getParameterTypes();
				Class returnType = m.getReturnType();				if (parameterTypes.length == 1 && parameterTypes[0] == Integer.TYPE) {
					choiceAtClass = returnType;
					choiceAtMethod = m;				}
			} else if (md.getName().equals("size") ||
					   md.getName().equals("choicesSize") ||					   md.getName().equals("getSize") ||
					   md.getName().equals("getChoicesSize")) {				Method m = md.getMethod();				Class parameters[] = m.getParameterTypes();
				Class returnType = m.getReturnType();				if (parameters.length == 0 && returnType == Integer.TYPE)					choicesSizeMethod = m;
			}		}
		*/
		/*		if (readMethod == null || writeMethod == null ||			(choicesField == null && choicesMethod == null && 			         (choicesSizeMethod == null || choiceAtMethod == null)) ||
			(choiceAtClass != valueClass))			return null;		*/
	}	public boolean isEnumeration() {
		return targetClass.isEnum();		
			}
	public int getIndexOfSelection() {
		Object selectedItem = getValue();
		for (int i = 0; i < this.choicesSize(); i++) {
			if (selectedItem.equals(choiceAt(i))) {
				return i;
			}			
		}
		return -1;
	}
	public static String ENUMERATION = "Enum";
	public String programmingPatternKeyword() {
		return  "";
	}
	public String typeKeyword() {
		return ObjectEditor.TYPE_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + ENUMERATION;
	}
	public Object clone() {
		return objectClone();
	}

	@Override
	public void addUserChoice(String newVal, CommandListener commandListener) {
		// TODO Auto-generated method stub
		
	}
	
}