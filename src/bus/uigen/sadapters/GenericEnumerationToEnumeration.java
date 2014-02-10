package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;

import util.annotations.StructurePatternNames;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.undo.*;import bus.uigen.visitors.AddChildUIComponentsAdapterVisitor;
import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
public class GenericEnumerationToEnumeration extends GenericPrimitiveToPrimitive
	implements ConcreteEnumeration {
	transient ClassProxy choicesClass = null;
	transient	ClassProxy valueClass = null;	transient	MethodProxy choicesMethod = null;
	transient MethodProxy choiceListMethod = null;
	transient	MethodProxy readMethod = null;
	transient	MethodProxy writeMethod = null;
	transient	MethodProxy addUserChoiceMethod = null;	transient	Field choicesField = null;
	transient	MethodProxy choiceAtMethod = null;
	transient	MethodProxy choicesSizeMethod = null;
	transient	ClassProxy choiceAtClass = null;
	transient List choiceList;
	transient Object[] choices;
		
	public GenericEnumerationToEnumeration (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericEnumerationToEnumeration () {
	}
	public int choicesSize() {
		if (choiceList != null) {
	  		return choiceList.size();
	  	}
		  Object[] parameters = {};		  return  ((Integer) MethodInvocationManager.invokeMethod(choicesSizeMethod, targetObject, parameters)).intValue();
	    }
  public Object choiceAt(int i) {
	  	  if (choiceList != null) {
	  		  return choiceList.get(i);
	  	  }
		  Object[] parameters = {new Integer(i)};		  return MethodInvocationManager.invokeMethod(choiceAtMethod, targetObject, parameters);
	    }
  public int getIndexOfSelection() {
	  	
		Object selectedItem = getValue();
		if (choiceList != null) {
	  		return choiceList.indexOf(selectedItem);
	  	}
		for (int i = 0; i < this.choicesSize(); i++) {
			if (selectedItem.equals(choiceAt(i))) {
				return i;
			}			
		}
		return -1;
	}  public Object getValue() {
	  
		  Object[] parameters = {};
		  // don't know why it would be null
		  if (targetObject == null)
			  return null;
		  return MethodInvocationManager.invokeMethod(readMethod, targetObject, parameters);		
	    }
  public List getChoices() {
	  refreshChoiceList();
	  return choiceList;
  }  public void setValue(Object newVal, CommandListener commandListener) { 
		  Object[] parameters = {newVal};
		  frame.getUndoer().execute (CommandCreator.createCommand(commandListener, writeMethod, targetObject, parameters));		  //uiMethodInvocationManager.invokeMethod (frame, targetObject, writeMethod, parameters, commandListener);
		    }
  public void addUserChoice(String newVal, CommandListener commandListener) {
	  String[] parameters = {newVal};
	  frame.getUndoer().execute (CommandCreator.createCommand(commandListener, addUserChoiceMethod, targetObject, parameters));

  }
  public void setMethods() {
	  setMethods(targetClass);
  }
  public void setMethods(ClassProxy objectClass) {	
	  setMethods (objectClass, targetObject);
	  
  }
  Object[] nullParams = {};
	public void setMethods(ClassProxy objectClass, Object theTargetObject) {	
		ClassDescriptorInterface cdesc;
		if (theTargetObject == null)
			cdesc = ClassDescriptorCache.getClassDescriptor(objectClass);
		else
			cdesc = ClassDescriptorCache.getClassDescriptor(objectClass, theTargetObject);
		targetObject = theTargetObject;
		//ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(objectClass);				
		
		PropertyDescriptorProxy[] properties = cdesc.getPropertyDescriptors();
		MethodDescriptorProxy[] methodDescriptors = cdesc.getMethodDescriptors();		/*
		FieldDescriptor fields[] = cdesc.getFieldDescriptors();		for (int i = 0; i < fields.length; i++) {			FieldDescriptor f = fields[i];			if (f.getName().equals("CHOICES")) {
				choicesField = f.getField();				choicesClass = choicesField.getType();				break;
			}		}
		*/		
		for (int i = 0; i < properties.length; i++) {			PropertyDescriptorProxy p = properties[i];
			if (p == null)
				break;
			if (p.getName().equals("value") || p.getName().equals("selectedItem")) {			
					readMethod = AVirtualMethod.virtualMethod (p.getReadMethod());
					LoggableRegistry.setMethodIsReadOnly(readMethod);
					writeMethod = AVirtualMethod.virtualMethod (p.getWriteMethod());					valueClass = p.getPropertyType();					break;
			} 		}
		if (methodDescriptors == null)
			return;		for (int i = 0; i < methodDescriptors.length; i++) {			MethodDescriptorProxy md = methodDescriptors[i];
			if (md.getName().equals("choices")) {				//Method m = md.getMethod();
				MethodProxy m = VirtualMethodDescriptor.getVirtualMethod(md);				ClassProxy parameters[] = m.getParameterTypes();
				ClassProxy returnType = m.getReturnType();				if (parameters.length == 0 && returnType == objectClass.enumerationClass()) {
					choicesMethod = m;
					LoggableRegistry.setMethodIsReadOnly(choicesMethod);

					LoggableRegistry.setMethodReturnsValue(choicesMethod);				}
			}  else if (md.getName().equals("getChoices")) {
				MethodProxy m = VirtualMethodDescriptor.getVirtualMethod(md);
				ClassProxy parameters[] = m.getParameterTypes();
				ClassProxy returnType = m.getReturnType();
				if (parameters.length == 0 && returnType == objectClass.listClass()) {
				//if (parameters.length == 0 && returnType.isArray()) {
					choiceListMethod = m;
					LoggableRegistry.setMethodIsReadOnly(choiceListMethod);
					LoggableRegistry.setMethodReturnsValue(choiceListMethod);
					//refreshChoiceList();
					
					/*
					
				//}
				try {
					 choiceList = (List) choiceListMethod.invoke(theTargetObject, parameters);
				} catch (Exception e) {
					
					choiceListMethod = null;
				}
				*/
				}
			}
			else if (md.getName().equals("getElementAt") || 
					   md.getName().equals("choiceAt") ||
					   md.getName().equals("getChoiceAt") ||
					   md.getName().equals("elementAt") ||
					   md.getName().equals("get"))
					   {				//Method m = md.getMethod();
				MethodProxy m = VirtualMethodDescriptor.getVirtualMethod(md);				ClassProxy parameterTypes[] = m.getParameterTypes();
				ClassProxy returnType = m.getReturnType();				if (parameterTypes.length == 1 && parameterTypes[0] == objectClass.integerType()) {
					choiceAtClass = returnType;
					choiceAtMethod = m;
					LoggableRegistry.setMethodIsReadOnly(choiceAtMethod);				}
			} else if (md.getName().equals("size") ||
					   md.getName().equals("choicesSize") ||					   md.getName().equals("getSize") ||
					   md.getName().equals("getChoicesSize")) {				//Method m = md.getMethod();
				MethodProxy m = VirtualMethodDescriptor.getVirtualMethod(md);				ClassProxy parameters[] = m.getParameterTypes();
				ClassProxy returnType = m.getReturnType();				if (parameters.length == 0 && returnType == objectClass.integerType()) {					choicesSizeMethod = m;
					LoggableRegistry.setMethodIsReadOnly(choicesSizeMethod);
				}
			}		}
		/*		if (readMethod == null || writeMethod == null ||			(choicesField == null && choicesMethod == null && 			         (choicesSizeMethod == null || choiceAtMethod == null)) ||
			(choiceAtClass != valueClass))			return null;		*/
		ClassProxy[] params = {objectClass.stringClass()};
		addUserChoiceMethod = IntrospectUtility.getMethod(objectClass, "addUserChoice", null, params);
		
	}
	void targetObjectChanged() {
		refreshChoiceList();
	}
	public static List arrayToList(Object[] source) {
		List retVal = new ArrayList();
		for (int i = 0; i < source.length; i++ )
			retVal.add(source[i]);
		return retVal;
		
	}
	void refreshChoiceList() {
		if (choiceListMethod == null)
			return;
		
		//}
		try {
			 //choiceList = (List) choiceListMethod.invoke(getTargetObject(), nullParams);
			 choiceList = (List) MethodInvocationManager.invokeMethod(getTargetObject(),choiceListMethod, nullParams);
			 //choices = (Object[]) uiMethodInvocationManager.invokeMethod(getTargetObject(),choiceListMethod, nullParams);
			 //choiceList = arrayToList(choices);
		} catch (Exception e) {
			
			choiceListMethod = null;
		}
	}
	public Object clone() {
		Object retVal =  objectClone();
		//((GenericEnumerationToEnumeration) retVal).choiceList = null;
		return retVal;
	}
	/*
	public void setTarget (Object theTargetObject) {
		super.setTarget(theTargetObject);
		refreshChoiceList();
	}
	*/	public boolean isEnumeration() {
		/*		
		return !( (readMethod == null || writeMethod == null ||			(choicesField == null && choicesMethod == null && choiceListMethod == null &&			         (choicesSizeMethod == null || choiceAtMethod == null)) ||
			(choiceAtClass != valueClass)));
			*/	
		//the getChoiced method does not call choiceAt() and size() to determine the choices
		//probably for efficiency reasons
		return readMethod != null && choiceListMethod != null;
//		return readMethod != null && (choiceListMethod != null ||
//				(choiceAtMethod != null && choicesSizeMethod != null));
			}
	public static String ENUMERATION = "DynamicEnum";
	public String programmingPatternKeyword() {
		//return  super.programmingPatternKeyword() + ObjectEditor.KEYWORD_SEPARATOR + ENUMERATION;
		return  AbstractConcreteType.PROGRAMMING_PATTERN + AttributeNames.KEYWORD_SEPARATOR + ENUMERATION;
	}
	public String typeKeyword() {
		return ObjectEditor.TYPE_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + ENUMERATION;
	}
	public String getPatternName() {
		return StructurePatternNames.ENUM_PATTERN;		
	}
	
	
}