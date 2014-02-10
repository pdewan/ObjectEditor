package bus.uigen.sadapters;
import java.util.*;import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

import util.annotations.StructurePatternNames;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericHashtableToHashtableStructure extends GenericMapToHashtableStructure implements  HashtableStructure  {
	/*	transient VirtualMethod getMethod = null; 		transient VirtualMethod keysMethod = null; 	transient VirtualMethod elementsMethod = null;		transient VirtualMethod putMethod = null;	transient VirtualMethod removeMethod = null;	
	transient VirtualMethod isEditableKeyMethod = null;
	transient VirtualMethod isEditableElementMethod = null;
	transient VirtualMethod isRemovableMethod = null;
	transient VirtualMethod valuesMethod = null;
	transient VirtualMethod clearMethod = null;
	transient VirtualMethod sizeMethod = null;
	transient VirtualMethod isEmptyMethod = null;
	transient VirtualMethod containsKeyMethod = null;
	transient VirtualMethod keySetMethod = null;
	transient VirtualMethod entrySetMethod = null;
	transient VirtualMethod containsValueMethod = null;
	transient VirtualMethod containsMethod = null;
	transient VirtualMethod putAllMethod = null;
	*/
	
	Vector emptyVector = new Vector();
	
	public GenericHashtableToHashtableStructure (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}	public GenericHashtableToHashtableStructure () {		
	}
	
	/*		Class targetClass;
	Object targetObject;	uiFrame frame;
	
	public void init(Object theTargetObject, uiFrame theFrame) {
		
		setTarget(theTargetObject);		frame = theFrame;
		setMethods(targetClass);
	}	public void setTarget(Object theTargetObject) {
		targetClass = theTargetObject.getClass();
		targetObject = theTargetObject;	}	*/
	/*
	public boolean isEditingMethod(VirtualMethod targetMethod) {
		return  super.isEditingMethod(targetMethod) ||
				targetMethod == isEmptyMethod ||
				targetMethod == sizeMethod ||
				targetMethod == elementsMethod ||
				targetMethod == keysMethod ||
				targetMethod == keySetMethod ||
				targetMethod == entrySetMethod ||
				targetMethod == valuesMethod ||
				targetMethod == getMethod ||
				targetMethod == containsKeyMethod ||
				targetMethod == containsMethod ||
				targetMethod == containsValueMethod ||
				targetMethod == containsMethod ||
				targetMethod == removeMethod ;
	}
	public boolean isPatternMethod(VirtualMethod targetMethod) {
		return super.isPatternMethod(targetMethod)|| isEditingMethod(targetMethod) ||
				targetMethod == putMethod ||
				targetMethod == putAllMethod;
				//targetMethod == insertElementAtMethod ||
				//targetMethod == removeElementAtMethod ||
				
	}
	
	public void setOtherMethods(Class objectClass) {
		isEmptyMethod = uiBean.getIsEmptyMethod(objectClass);
		valuesMethod = uiBean.getValuesMethod(objectClass);
		keySetMethod = uiBean.getKeySetMethod(objectClass);
		entrySetMethod = uiBean.getEntrySetMethod(objectClass);
		clearMethod = uiBean.getClearMethod(objectClass);
		containsKeyMethod = uiBean.getContainsKeyMethod(objectClass);
		containsValueMethod = uiBean.getContainsValueMethod(objectClass);
		containsMethod = uiBean.getContainsMethod(objectClass);
		putAllMethod = uiBean.getPutAllMethod(objectClass);
	}
	
	public void setMethods(Class objectClass) {			super.setMethods(objectClass);
		setReadMethods(objectClass);
		setWriteMethods(objectClass);
		setOtherMethods(objectClass);
		sortedComponentNames.remove("empty");
		try {
			ViewInfo cd = ClassDescriptorCache.getClassDescriptor(objectClass);
			//boolean addedProperty = false;
		Vector<PropertyDescriptor> v = new Vector();
		if (cd.getPropertyDescriptor(AttributeNames.KEY_NAME) == null)
		 v.addElement(new PropertyDescriptor(AttributeNames.KEY_NAME, null, null));
		if (cd.getPropertyDescriptor(AttributeNames.VALUE_NAME) == null)
		  v.addElement(new PropertyDescriptor(AttributeNames.VALUE_NAME, null, null));
		  //ViewInfo cd = ClassDescriptorCache.getClassDescriptor(objectClass);
		if (v.size() > 0)
		  cd.addProperties(v);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public void setReadMethods(Class objectClass) {
		sizeMethod = uiBean.getSizeMethod(objectClass);		getMethod = getGetMethod(objectClass);		keysMethod = getKeysMethod(objectClass);
		elementsMethod = getElementsMethod(objectClass);		
	}
	public void setWriteMethods(Class objectClass) {		putMethod = getPutMethod(objectClass);					removeMethod = getRemoveMethod(objectClass);
		isEditableKeyMethod = getIsEditableKeyMethod(objectClass);
		isEditableElementMethod = getIsEditableElementMethod(objectClass);
		VirtualMethod validatePutMethod = uiBean.getValidate(putMethod, targetClass);
		if (isEditableKeyMethod == null)
			isEditableKeyMethod = validatePutMethod;
		if (isEditableElementMethod == null)
			isEditableElementMethod = validatePutMethod;
		isRemovableMethod = getIsRemovable(objectClass);
		VirtualMethod validateRemoveMethod = uiBean.getValidate(removeMethod, targetClass);
		if (isRemovableMethod == null)
			isRemovableMethod = validateRemoveMethod;
	}	
	public  VirtualMethod getGetMethod(Class objectClass) {		return uiBean.getGetMethod(objectClass);
	}
	*/
	public  MethodProxy getKeysMethod(ClassProxy objectClass) {		return IntrospectUtility.getKeysMethod(objectClass);
	}
	public  MethodProxy getElementsMethod(ClassProxy objectClass) {		return IntrospectUtility.getElementsMethod(objectClass);
	}
	/*	public  VirtualMethod getPutMethod(Class objectClass, VirtualMethod getMethod) {
		Class[] paramTypes = {keyType(getMethod), elementType(getMethod)};		return uiBean.getMethod(objectClass, "put", null, paramTypes);
	}	public  VirtualMethod getPutMethod(Class objectClass) {		return getPutMethod(objectClass, getMethod);
	}
	public VirtualMethod getRemoveMethod(Class objectClass, VirtualMethod getMethod) {
		Class[] paramTypes = {keyType(getMethod)};		return uiBean.getMethod(objectClass, "remove", null, paramTypes);
	}	public  VirtualMethod getRemoveMethod(Class objectClass) {		return getRemoveMethod(objectClass, getMethod);
	}
	public  VirtualMethod getIsEditableKeyMethod(Class objectClass) {
		return uiBean.getIsEditableKey(objectClass);
	}
	public  VirtualMethod getIsEditableElementMethod(Class objectClass) {
		return uiBean.getIsEditableElement(objectClass);
	}
	public  VirtualMethod getIsRemovable(Class objectClass) {
		return uiBean.getIsRemovable(objectClass);
	}
	*/
	
	public static Vector toVector(Enumeration e) {
		Vector retVal = new Vector();
		while (e.hasMoreElements())
			retVal.addElement(e.nextElement());
		return retVal;	}
	public Vector keys() {
		if (keysMethod == null) {
			return null;
		}
		Object[] params = {};
		Enumeration keys =  (Enumeration) MethodInvocationManager.invokeMethod(targetObject, keysMethod, params);
		return toVector(keys);
	}
	public Vector elements() {
		// may need to use sequence elements
		if (elementsMethod != null)
			return elementsFromElements();
		else
			return elementsFromGet();
		/*
		Object[] params = {};
		Enumeration elements = (Enumeration) uiMethodInvocationManager.invokeMethod(targetObject, elementsMethod, params);
		return toVector(elements);
		*/
	}
	Vector elementsFromElements() {
		Object[] params = {};
		Enumeration elements = (Enumeration) MethodInvocationManager.invokeMethod(targetObject, elementsMethod, params);
		return toVector(elements);
	}
	Vector elementsFromGet () {
		Vector elements = new Vector();
		Vector keys = keys();
		for (int i =0; i < keys.size(); i++ ) {
			elements.addElement(get(keys.elementAt(i)));			
		}
		return elements;
	}
	/*
	public Object get(Object key) {
		Object[] params = {key};
		return uiMethodInvocationManager.invokeMethod(targetObject, getMethod, params);
	}
	public boolean isEditableKey(Object key) {
		if (putMethod == null) return false;
		if (isEditableKeyMethod == null) return true;
		Object[] params = {key};
		try {
		return ((Boolean) uiMethodInvocationManager.invokeMethod(targetObject, isEditableKeyMethod, params)).booleanValue();
		} catch (Exception e) {
			return true;
		}
		}
	public boolean isEditableElement(Object key) {
		if (putMethod == null) return false;
		if (isEditableElementMethod == null) return true;
		Object[] params = {key};
		try {
		return ((Boolean)uiMethodInvocationManager.invokeMethod(targetObject, isEditableElementMethod, params)).booleanValue();
		} catch (Exception e) {
			return true;
		}
		}
	public boolean isRemovable (Object key) {
		if (removeMethod == null) return false;
		if (isRemovableMethod == null) return true;
		Object[] params = {key};
		try {
		return ((Boolean) uiMethodInvocationManager.invokeMethod(targetObject, isRemovableMethod, params)).booleanValue();
		} catch (Exception e) {
			return true;
		}
	}
	public Object put(Object key, Object value) {
		Object[] params = {key, value};		
		return uiMethodInvocationManager.invokeMethod(targetObject, putMethod, params);
	}
	public Object put(Object key, Object value, CommandListener commandListener) {
		//System.out.println("put called with key " + key + " value" + value);
		if (putMethod == null)
			return null;
		Object[] params = {key, value};
		if (get(key) == null || commandListener == null) // put undone by remove
			return uiMethodInvocationManager.invokeMethod(frame, targetObject, putMethod, params, commandListener);
		else { // put undone by put
		
			return frame.getUndoer().execute (
							new SetGetLastCommand(//getUIFrame(),							  		commandListener,
							  		putMethod,							  		targetObject,							  		params,									getMethod
							));		
		}
	}
	public Object remove(Object key) {
		return remove (key, null);
		
	}
	public Object remove(Object key, CommandListener commandListener) {
		//System.out.println("remove called with key " + key);
		if (removeMethod == null)
			return null;
		Object[] params = {key};
		if (commandListener == null) {
			return uiMethodInvocationManager.invokeMethod(frame, targetObject, removeMethod, params, commandListener);
		} else {
			return frame.getUndoer().execute (
					new SubtractAddLastCommand(//getUIFrame(),
					  		commandListener,
					  		removeMethod,
					  		targetObject,
					  		params,
							putMethod
					));		
		}
	}	
	public Class keyType(VirtualMethod getMethod) {
		return getMethod.getParameterTypes()[0];		
	}
	public Class keyType() {
		return keyType(getMethod);		
	}
	public Class elementType(VirtualMethod getMethod) {
		return getMethod.getReturnType();		
	}
	public Class elementType() {
		return elementType(getMethod);		
	}	public boolean canDeleteChild() {
		return removeMethod != null;	}	public boolean canSetChild() {
		return putMethod != null;	}
	*/
	/*
	public boolean canDeleteChild(Object key) {
		return removeMethod != null;
	}
	public boolean canSetChild(Object key) {
		return putMethod != null;
	}
	*/
	/*
	public String toString() {
		if (targetObject != null)
			return targetObject.toString();
		else
			return "";
	}
	*/
	public String getPatternName() {
		return StructurePatternNames.HASHTABLE_PATTERN;		
	}
}
