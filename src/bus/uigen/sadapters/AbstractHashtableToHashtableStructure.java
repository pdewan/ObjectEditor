package bus.uigen.sadapters;
import java.util.*;import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic abstract class AbstractHashtableToHashtableStructure extends AbstractDynamicStructure implements  HashtableStructure   {
		transient MethodProxy getMethod = null; 		transient MethodProxy keysMethod = null; 	transient MethodProxy elementsMethod = null;		transient MethodProxy putMethod = null;	transient MethodProxy removeMethod = null;	
	transient MethodProxy isEditableKeyMethod = null;
	transient MethodProxy isEditableElementMethod = null;
	transient MethodProxy isRemovableMethod = null;
	transient MethodProxy valuesMethod = null;
	//transient VirtualMethod clearMethod = null;
	transient MethodProxy sizeMethod = null;
	transient MethodProxy isEmptyMethod = null;
	transient MethodProxy containsKeyMethod = null;
	transient MethodProxy keySetMethod = null;
	transient MethodProxy entrySetMethod = null;
	transient MethodProxy containsValueMethod = null;
	transient MethodProxy containsMethod = null;
	transient MethodProxy putAllMethod = null;
	
	
	public AbstractHashtableToHashtableStructure (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}	public AbstractHashtableToHashtableStructure () {		
	}
	
	/*		Class targetClass;
	Object targetObject;	uiFrame frame;
	
	public void init(Object theTargetObject, uiFrame theFrame) {
		
		setTarget(theTargetObject);		frame = theFrame;
		setMethods(targetClass);
	}	public void setTarget(Object theTargetObject) {
		targetClass = theTargetObject.getClass();
		targetObject = theTargetObject;	}	*/
	public boolean isEditingMethod(MethodProxy targetMethod) {
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
	public boolean isPatternMethod(MethodProxy targetMethod) {
		return super.isPatternMethod(targetMethod)|| isEditingMethod(targetMethod) ||
				targetMethod == putMethod ||
				targetMethod == putAllMethod;
				//targetMethod == insertElementAtMethod ||
				//targetMethod == removeElementAtMethod ||
				
	}
	
	public void setOtherMethods(ClassProxy objectClass) {
		isEmptyMethod = IntrospectUtility.getIsEmptyMethod(objectClass);
		valuesMethod = IntrospectUtility.getValuesMethod(objectClass);
		keySetMethod = IntrospectUtility.getKeySetMethod(objectClass);
		entrySetMethod = IntrospectUtility.getEntrySetMethod(objectClass);
		clearMethod = IntrospectUtility.getClearMethod(objectClass);
		containsKeyMethod = IntrospectUtility.getContainsKeyMethod(objectClass);
		containsValueMethod = IntrospectUtility.getContainsValueMethod(objectClass);
		containsMethod = IntrospectUtility.getContainsMethod(objectClass);
		putAllMethod = IntrospectUtility.getPutAllMethod(objectClass);
		
		LoggableRegistry.setMethodIsReadOnly( isEmptyMethod );
		LoggableRegistry.setMethodIsReadOnly( valuesMethod );
		LoggableRegistry.setMethodIsReadOnly( keySetMethod );
		LoggableRegistry.setMethodIsReadOnly( entrySetMethod );
		LoggableRegistry.setMethodIsReadOnly( containsKeyMethod );
		LoggableRegistry.setMethodIsReadOnly( containsValueMethod );
		LoggableRegistry.setMethodIsReadOnly( containsMethod );
		LoggableRegistry.setMethodReturnsValue( putAllMethod );
	}
	
	public void setMethods(ClassProxy objectClass) {			super.setMethods(objectClass);
		setReadMethods(objectClass);
		setWriteMethods(objectClass);
		setOtherMethods(objectClass);
		setWriteMethodDescriptors();
		sortedComponentNames.remove("empty");	
		sortedNonGraphicsComponentNames.remove("empty");
		try {
			ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(objectClass);
			//boolean addedProperty = false;
		Vector<PropertyDescriptorProxy> v = new Vector();
		if (cd.getPropertyDescriptor(AttributeNames.ANY_KEY) == null)
		 v.addElement(new APropertyDescriptorProxy(AttributeNames.ANY_KEY, null, null));
		if (cd.getPropertyDescriptor(AttributeNames.ANY_VALUE) == null)
		  v.addElement(new APropertyDescriptorProxy(AttributeNames.ANY_VALUE, null, null));
		  //ViewInfo cd = ClassDescriptorCache.getClassDescriptor(objectClass);
		if (v.size() > 0)
		  cd.addProperties(v);
		//} catch (IntrospectionException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setReadMethods(ClassProxy objectClass) {
		sizeMethod = IntrospectUtility.getSizeMethod(objectClass);		getMethod = getGetMethod(objectClass);		keysMethod = getKeysMethod(objectClass);
		elementsMethod = getElementsMethod(objectClass);
		
		LoggableRegistry.setMethodIsReadOnly( sizeMethod );
		LoggableRegistry.setMethodIsReadOnly( getMethod );
		LoggableRegistry.setMethodIsReadOnly( keysMethod );
		LoggableRegistry.setMethodIsReadOnly( elementsMethod );
	}
	public void setWriteMethods(ClassProxy objectClass) {		putMethod = getPutMethod(objectClass);			
		LoggableRegistry.setMethodReturnsValue( putMethod );
		removeMethod = getRemoveMethod(objectClass);
		LoggableRegistry.setMethodReturnsValue( removeMethod );
		
		isEditableKeyMethod = getIsEditableKeyMethod(objectClass);
		LoggableRegistry.setMethodIsReadOnly( isEditableKeyMethod );
		
		isEditableElementMethod = getIsEditableElementMethod(objectClass);
		LoggableRegistry.setMethodIsReadOnly( isEditableElementMethod );

		MethodProxy validatePutMethod = IntrospectUtility.getValidate(putMethod, targetClass);
		LoggableRegistry.setMethodIsReadOnly( validatePutMethod );
		
		if (isEditableKeyMethod == null)
			isEditableKeyMethod = validatePutMethod;
		if (isEditableElementMethod == null)
			isEditableElementMethod = validatePutMethod;
		isRemovableMethod = getIsRemovable(objectClass);
		LoggableRegistry.setMethodIsReadOnly( isRemovableMethod );
		
		MethodProxy validateRemoveMethod = IntrospectUtility.getValidate(removeMethod, targetClass);
		LoggableRegistry.setMethodIsReadOnly( validateRemoveMethod );
		if (isRemovableMethod == null)
			isRemovableMethod = validateRemoveMethod;
	}
	public void setWriteMethodDescriptors () {
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(getTargetClass());		
		MethodDescriptorProxy removeMD = cd.getMethodDescriptor(removeMethod);
		if (removeMD != null) {
			removeMD.setValue(AttributeNames.IS_REMOVE_METHOD, true);
		}
		MethodDescriptorProxy putMD = cd.getMethodDescriptor(putMethod);
		if (removeMD != null && putMD != null) {
			putMD.setValue(AttributeNames.INVERSE, removeMethod);
			removeMD.setValue(AttributeNames.INVERSE, putMethod);
		}
		MethodDescriptorProxy clearMethodMD = cd.getMethodDescriptor(clearMethod);
		MethodDescriptorProxy  putAllMethodMD = cd.getMethodDescriptor(putAllMethod);
		if (clearMethodMD != null && putAllMethodMD != null) {
			clearMethodMD.setValue(AttributeNames.INVERSE, putAllMethod);
			putAllMethodMD.setValue(AttributeNames.INVERSE, clearMethod);
			clearMethodMD.setValue(AttributeNames.IS_REMOVE_ALL_METHOD, true);
			putAllMethodMD.setValue(AttributeNames.IS_ADD_ALL_METHOD, true);
		}
		
	}
	public  MethodProxy getGetMethod(ClassProxy objectClass) {		return IntrospectUtility.getGetMethod(objectClass);
	}
	public abstract  MethodProxy getKeysMethod(ClassProxy objectClass);
	/*
	public  VirtualMethod getKeysMethod(Class objectClass) {		return uiBean.getKeysMethod(objectClass);
	}
	*/
	public abstract  MethodProxy getElementsMethod(ClassProxy objectClass);
	/*
	public  VirtualMethod getElementsMethod(Class objectClass) {		return uiBean.getElementsMethod(objectClass);
	}
	*/	public  MethodProxy getPutMethod(ClassProxy objectClass, MethodProxy getMethod) {
		ClassProxy[] paramTypes = {keyType(getMethod), elementType(getMethod)};		return IntrospectUtility.getMethod(objectClass, "put", null, paramTypes);
	}	public  MethodProxy getPutMethod(ClassProxy objectClass) {		return getPutMethod(objectClass, getMethod);
	}
	public MethodProxy getRemoveMethod(ClassProxy objectClass, MethodProxy getMethod) {
		ClassProxy[] paramTypes = {keyType(getMethod)};		return IntrospectUtility.getMethod(objectClass, "remove", null, paramTypes);
	}	public  MethodProxy getRemoveMethod(ClassProxy objectClass) {		return getRemoveMethod(objectClass, getMethod);
	}
	public  MethodProxy getIsEditableKeyMethod(ClassProxy objectClass) {
		return IntrospectUtility.getIsEditableKey(objectClass);
	}
	public  MethodProxy getIsEditableElementMethod(ClassProxy objectClass) {
		return IntrospectUtility.getIsEditableElement(objectClass);
	}
	public  MethodProxy getIsRemovable(ClassProxy objectClass) {
		return IntrospectUtility.getIsRemovable(objectClass);
	}
	/*
	public static Vector toVector(Enumeration e) {
		Vector retVal = new Vector();
		while (e.hasMoreElements())
			retVal.addElement(e.nextElement());
		return retVal;	}
	*/
	public abstract Vector keys();
	/*
	public Vector keys() {
		Object[] params = {};
		Enumeration keys =  (Enumeration) uiMethodInvocationManager.invokeMethod(targetObject, keysMethod, params);
		return toVector(keys);
	}
	*/
	public abstract Vector elements();
	/*
	public Vector elements() {
		// may need to use sequence elements
		if (elementsMethod != null)
			return elementsFromElements();
		else
			return elementsFromGet();
		
	}
	
	Vector elementsFromElements() {
		Object[] params = {};
		Enumeration elements = (Enumeration) uiMethodInvocationManager.invokeMethod(targetObject, elementsMethod, params);
		return toVector(elements);
	}
	*/
	Vector elementsFromGet () {
		Vector elements = new Vector();
		Vector keys = keys();
		for (int i =0; i < keys.size(); i++ ) {
			elements.addElement(get(keys.elementAt(i)));			
		}
		return elements;
	}
	public Object get(Object key) {
		Object[] params = {key};
		return MethodInvocationManager.invokeMethod(targetObject, getMethod, params);
	}
	public boolean isEditableKey(Object key) {
		if (putMethod == null) return false;
		if (isEditableKeyMethod == null) return true;
		Object[] params = {key};
		try {
		return ((Boolean) MethodInvocationManager.invokeMethod(targetObject, isEditableKeyMethod, params)).booleanValue();
		} catch (Exception e) {
			return true;
		}
		}
	public boolean isEditableElement(Object key) {
		if (putMethod == null) return false;
		if (isEditableElementMethod == null) return true;
		Object[] params = {key};
		try {
		return ((Boolean)MethodInvocationManager.invokeMethod(targetObject, isEditableElementMethod, params)).booleanValue();
		} catch (Exception e) {
			return true;
		}
		}
	public boolean isRemovable (Object key) {
		if (removeMethod == null) return false;
		if (isRemovableMethod == null) return true;
		Object[] params = {key};
		try {
		return ((Boolean) MethodInvocationManager.invokeMethod(targetObject, isRemovableMethod, params)).booleanValue();
		} catch (Exception e) {
			return true;
		}
	}
	public Object put(Object key, Object value) {
		Object[] params = {key, value};		
		return MethodInvocationManager.invokeMethod(targetObject, putMethod, params);
	}
	public Object put(Object key, Object value, CommandListener commandListener) {
		//System.out.println("put called with key " + key + " value" + value);
		if (putMethod == null)
			return null;
		Object[] params = {key, value};
		if (get(key) == null || commandListener == null) // put undone by remove
			return MethodInvocationManager.invokeMethod(frame, targetObject, putMethod, params, commandListener);
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
			return MethodInvocationManager.invokeMethod(frame, targetObject, removeMethod, params, commandListener);
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
	public ClassProxy keyType(MethodProxy getMethod) {
		return getMethod.getParameterTypes()[0];		
	}
	public ClassProxy keyType() {
		return keyType(getMethod);		
	}
	public ClassProxy elementType(MethodProxy getMethod) {
		return getMethod.getReturnType();		
	}
	public ClassProxy elementType() {
		return elementType(getMethod);		
	}	public boolean hasDeleteChildMethod() {
		return removeMethod != null;	}
	
	public boolean hasInsertChildMethod() {
		return false;
	}
	
	public boolean hasAddChildMethod() {
		return putMethod != null;
	}	public boolean hasSetChildMethod() {
		return putMethod != null;	}
	public static String HASHTABLE = "HASHTABLE";
	//public static String HASHTABLE_PATTERN = PATTERN + ObjectEditor.KEYWORD_SEPARATOR + HASHTABLE;
	public String programmingPatternKeyword() {
		return super.programmingPatternKeyword() + AttributeNames.KEYWORD_SEPARATOR + HASHTABLE;
	}
	public String typeKeyword() {
		return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + HASHTABLE;
	}
	
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
	public int size() {
		Object[] params = {};
		try {
			//Object retVal = sizeMethod.invoke(targetObject, params);
			Object retVal = MethodInvocationManager.invokeMethod(targetObject, sizeMethod, params);
			return ((Integer) retVal).intValue();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Please trace size method of:" + targetObject);
			return -1;
		}
	}
}
