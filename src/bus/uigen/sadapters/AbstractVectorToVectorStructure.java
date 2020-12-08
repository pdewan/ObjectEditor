package bus.uigen.sadapters;
import java.util.*;import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

import util.models.AListenableVector;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic abstract class AbstractVectorToVectorStructure extends AbstractDynamicStructure implements  VectorStructure  {
		
	transient MethodProxy setElementsMethod = null;		transient MethodProxy elementsMethod = null;	transient MethodProxy addElementMethod = null;		transient MethodProxy insertElementAtMethod = null; 
	transient MethodProxy addAllMethod = null;	transient MethodProxy removeElementMethod = null;	
	transient boolean voidRemoveElementMethod = false;	transient MethodProxy removeElementAtMethod = null;
	transient boolean voidRemovElementAtMethod = false;
	//transient VirtualMethod clearMethod = null;
	transient MethodProxy elementAtMethod = null ;	transient MethodProxy sizeMethod = null;
	transient MethodProxy isEmptyMethod = null;	transient MethodProxy setElementAtMethod = null;
	transient MethodProxy indexOfMethod = null;
	
	transient MethodProxy validateSetElementAtMethod = null;
	transient MethodProxy validateAddElementMethod = null;	
	transient MethodProxy validateInsertElementAtMethod = null; 
	transient MethodProxy validateRemoveElementMethod = null;	 
	transient MethodProxy validateRemoveElementAtMethod = null;
	transient MethodProxy isIndexedChildEditableMethod = null;
	transient MethodProxy validateElementAtMethod = null;
	
	transient MethodProxy preSetElementAtMethod = null;
	transient MethodProxy preAddElementMethod = null;	
	transient MethodProxy preInsertElementAtMethod = null; 
	transient MethodProxy preRemoveElementMethod = null;	 
	transient MethodProxy preRemoveElementAtMethod = null;
	//transient MethodProxy isIndexedChildEditableMethod = null;
	transient MethodProxy preElementAtMethod = null;
	PropertyDescriptorProxy elementProxy;
	/*	Class targetClass;
	Object targetObject;	uiFrame frame;	*/
	public AbstractVectorToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		init(theGVectorObject, theFrame );
	}	public AbstractVectorToVectorStructure () {
	}
	public boolean isEditingMethod(MethodProxy targetMethod) {
		return  super.isEditingMethod(targetMethod) ||	
				targetMethod == isEmptyMethod ||
				targetMethod == elementAtMethod ||
				targetMethod == sizeMethod ||
				targetMethod == elementsMethod ||
				targetMethod == setElementsMethod ||
				targetMethod == setElementAtMethod ||
				targetMethod == removeElementAtMethod ||
				targetMethod == removeElementMethod ||
				targetMethod == indexOfMethod;
	}
	@Override
	public boolean isPatternMethod(MethodProxy targetMethod) {
		return super.isPatternMethod(targetMethod)|| isEditingMethod(targetMethod) ||
				targetMethod == addElementMethod;
				//targetMethod == insertElementAtMethod ||
				//targetMethod == removeElementAtMethod ||
				
	}
	public  MethodProxy getClearMethod(ClassProxy objectClass) {
		//return uiBean.getKeysMethod(objectClass);
		return IntrospectUtility.getClearMethod(objectClass);
	}	/*
	public void init(Object theTargetObject, uiFrame theFrame) {		
		setTarget(theTargetObject);		frame = theFrame;
		setMethods(targetClass);
	}	public void setTarget(Object theTargetObject) {
		targetClass = theTargetObject.getClass();
		targetObject = theTargetObject;	}	*/
		/*	public Method getElementsMethod() {
		return elementsMethod;
	}
	public void setElementsMethod(Method newVal) {
		elementsMethod = newVal;
	}
		public Method getAddElementMethod() {
		return addElementMethod;
	}	public void setAddElementMethod(Method newVal) {
		addElementMethod = newVal;
	}	public Method getInsertElementAtMethod() {
		return insertElementAtMethod;
	}	public void setInsertElementAtMethod(Method newVal) {
		insertElementAtMethod = newVal;
	}
		public Method getRemoveElementAtMethod() {
		return removeElementAtMethod;
	}	public void setRemoveElementAtMethod(Method newVal) {
		removeElementAtMethod = newVal;
	}	public Method getRemoveElementMethod() {
		return removeElementMethod;
	}	public void setRemoveElementMethod(Method newVal) {
		removeElementMethod = newVal;
	}
		public Method getElementAtMethod() {
		return elementAtMethod;
	}

	public void setElementAtMethod(Method newVal) {
		elementAtMethod = newVal;
	}
		public Method getSetElementAtMethod() {
		return setElementAtMethod;
	}

	public void setSetElementAtMethod(Method newVal) {
		setElementAtMethod = newVal;
	}
		public Method getSizeMethod() {
		return sizeMethod;
	}

	public void setSizeMethod(Method newVal) {
		sizeMethod = newVal;
	}		public Method getIndexOfMethod() {
		return indexOfMethod;
	}

	public void setIndexOfMethod(Method newVal) {
		indexOfMethod = newVal;
	}	
	public void setMethods(Class objectClass) {				setSizeMethod(uiBean.getSizeMethod(objectClass));		setElementsMethod(uiBean.getElementsMethod(objectClass));
		setElementAtMethod(uiBean.getElementAtMethod(objectClass));		setSetElementAtMethod(uiBean.getSetElementAtMethod(objectClass));	  		setRemoveElementAtMethod(uiBean.getRemoveElementAtMethod(objectClass));
		setRemoveElementMethod(uiBean.getRemoveMethod(objectClass));		setAddElementMethod(uiBean.getAddElementMethod(objectClass));		setInsertElementAtMethod(uiBean.getInsertElementAtMethod(objectClass));		setIndexOfMethod(uiBean.getIndexOfMethod(objectClass));
	}
	*/
	public void setOtherMethods(ClassProxy objectClass) {
		isEmptyMethod = IntrospectUtility.getIsEmptyMethod(objectClass);
		
		LoggableRegistry.setMethodIsReadOnly( isEmptyMethod );
	}
	public void setMethods(ClassProxy objectClass) {			super.setMethods(objectClass);
		setReadMethods(objectClass);
		setReadMethodDescriptors();
		setWriteMethods(objectClass);
		setWriteMethodDescriptors();
		setValidateMethods(objectClass);
		setPreMethods(objectClass);
		setOtherMethods(objectClass);
		sortedComponentNames.remove("empty");
		sortedNonGraphicsComponentNames.remove("empty");
		try {
			ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(objectClass);
			elementProxy = cd.getPropertyDescriptor(AttributeNames.ANY_ELEMENT);
//			if (cd.getPropertyDescriptor(AClassDescriptor.ELEMENT_NAME) == null) {
			if (elementProxy == null) {	
				
			// should check if element already exists!
			// this should probably be added by ClassDescriptor
			// lools like it is now being added there
			Vector<PropertyDescriptorProxy> v = new Vector();
			elementProxy = new APropertyDescriptorProxy(AttributeNames.ANY_ELEMENT, elementAtMethod, setElementAtMethod);
			 v.addElement(elementProxy);
//			 v.addElement(new APropertyDescriptorProxy(AClassDescriptor.ELEMENT_NAME, null, null));
			  //ViewInfo cd = ClassDescriptorCache.getClassDescriptor(objectClass);
			  cd.addProperties(v);
			  cd.initPropertyMergedAttributes(elementProxy);
			}
			//} catch (IntrospectionException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		/*		setSizeMethod(uiBean.getSizeMethod(objectClass));		setElementsMethod(uiBean.getElementsMethod(objectClass));
		setElementAtMethod(uiBean.getElementAtMethod(objectClass));		setSetElementAtMethod(uiBean.getSetElementAtMethod(objectClass));	  		setRemoveElementAtMethod(uiBean.getRemoveElementAtMethod(objectClass));
		setRemoveElementMethod(uiBean.getRemoveMethod(objectClass));		setAddElementMethod(uiBean.getAddElementMethod(objectClass));		setInsertElementAtMethod(uiBean.getInsertElementAtMethod(objectClass));		setIndexOfMethod(uiBean.getIndexOfMethod(objectClass));		*/
	}
	
	public Vector<Attribute> getComponentAttributes(String componentName) {
		if (!componentName.equals(AttributeNames.ANY_ELEMENT)) {
			return super.getComponentAttributes(componentName);
		} else {
			return (Vector) elementProxy.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
		}
	}
	
	
	public void setReadMethods(ClassProxy objectClass) {		sizeMethod = getSizeMethod(objectClass);		elementsMethod = getElementsMethod(objectClass, false);
		elementAtMethod = getElementAtMethod(objectClass, false);
		/*		sizeMethod = uiBean.getSizeMethod(objectClass);		elementsMethod = uiBean.getElementsMethod(objectClass);
		elementAtMethod = uiBean.getElementAtMethod(objectClass);
		*/		indexOfMethod = IntrospectUtility.getIndexOfMethod(objectClass);
		LoggableRegistry.setMethodIsReadOnly( sizeMethod );
		LoggableRegistry.setMethodIsReadOnly( elementsMethod );
		LoggableRegistry.setMethodIsReadOnly( elementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( indexOfMethod );
	}
	public void setReadMethodDescriptors () {
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(getTargetClass());		
		MethodDescriptorProxy elementAtMD = cd.getMethodDescriptor(elementAtMethod);
		if (elementAtMD != null) {
			cd.setAttribute(AttributeNames.READ_ELEMENT_METHOD, elementAtMethod);
		}
	}
	public abstract  MethodProxy getAddAllMethod(ClassProxy objectClass);
	
	public void setWriteMethods(ClassProxy objectClass) {		setElementAtMethod = getSetElementAtMethod(objectClass);					addElementMethod = getAddElementMethod(objectClass);		
		removeElementMethod = getRemoveElementMethod(objectClass);
		LoggableRegistry.setMethodReturnsValue( removeElementAtMethod );
		
		if (removeElementMethod != null && removeElementMethod.getReturnType() == objectClass.voidType())
			voidRemoveElementMethod = true;		insertElementAtMethod = getInsertElementAtMethod(objectClass);		removeElementAtMethod = getRemoveElementAtMethod(objectClass);
		if (removeElementAtMethod != null && removeElementAtMethod.getReturnType() == objectClass.voidType())
			voidRemovElementAtMethod = true;
		addAllMethod = getAddAllMethod(objectClass);
		clearMethod = getClearMethod(objectClass);
	}
	public void setWriteMethodDescriptors () {
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(getTargetClass());		
		MethodDescriptorProxy removeElementMD = cd.getMethodDescriptor(removeElementMethod);
		if (removeElementMD != null) {
			removeElementMD.setValue(AttributeNames.IS_REMOVE_METHOD, true);
		}
		MethodDescriptorProxy addElementMD = cd.getMethodDescriptor(addElementMethod);
		if (addElementMD != null && removeElementMD != null) {
			addElementMD.setValue(AttributeNames.INVERSE, removeElementMethod);
			removeElementMD.setValue(AttributeNames.INVERSE, addElementMethod);
		}
		MethodDescriptorProxy clearMethodMD = cd.getMethodDescriptor(clearMethod);
		MethodDescriptorProxy addAllMethodMD = cd.getMethodDescriptor(addAllMethod);
		if (clearMethodMD != null && addAllMethodMD != null) {
			clearMethodMD.setValue(AttributeNames.INVERSE, addAllMethod);
			addAllMethodMD.setValue(AttributeNames.INVERSE, clearMethod);
			clearMethodMD.setValue(AttributeNames.IS_REMOVE_ALL_METHOD, true);
			addAllMethodMD.setValue(AttributeNames.IS_ADD_ALL_METHOD, true);
		}
		
	}
	public void setValidateMethods(ClassProxy objectClass) {
		//VirtualMethod validate = uiBean.getValidate(uiMethodInvocationManager.virtualMethod(property.getWriteMethod()), targetClass);
		validateElementAtMethod = IntrospectUtility.getValidate(elementAtMethod, targetClass);
		validateSetElementAtMethod = IntrospectUtility.getValidate(setElementAtMethod, targetClass);			
		validateAddElementMethod = 	IntrospectUtility.getValidate(addElementMethod, targetClass);		
		validateRemoveElementMethod = 	IntrospectUtility.getValidate(removeElementMethod, targetClass);
		validateRemoveElementAtMethod = IntrospectUtility.getValidate(removeElementAtMethod, targetClass);
		validateInsertElementAtMethod = IntrospectUtility.getValidate(insertElementAtMethod, targetClass);
		isIndexedChildEditableMethod = IntrospectUtility.getIsIndexedChildEditableMethod(targetClass);
		
		LoggableRegistry.setMethodIsReadOnly( validateElementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( validateSetElementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( validateAddElementMethod );
		LoggableRegistry.setMethodIsReadOnly( validateRemoveElementMethod );
		LoggableRegistry.setMethodIsReadOnly( validateRemoveElementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( validateInsertElementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( isIndexedChildEditableMethod );
	}
	public void setPreMethods(ClassProxy objectClass) {
		//VirtualMethod validate = uiBean.getValidate(uiMethodInvocationManager.virtualMethod(property.getWriteMethod()), targetClass);
		preElementAtMethod = IntrospectUtility.getPre(elementAtMethod, targetClass);
		preSetElementAtMethod = IntrospectUtility.getPre(setElementAtMethod, targetClass);			
		preAddElementMethod = 	IntrospectUtility.getPre(addElementMethod, targetClass);		
		preRemoveElementMethod = 	IntrospectUtility.getPre(removeElementMethod, targetClass);
		preRemoveElementAtMethod = IntrospectUtility.getPre(removeElementAtMethod, targetClass);
		preInsertElementAtMethod = IntrospectUtility.getPre(insertElementAtMethod, targetClass);
		
		LoggableRegistry.setMethodIsReadOnly( preElementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( preSetElementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( preAddElementMethod );
		LoggableRegistry.setMethodIsReadOnly( preRemoveElementMethod );
		LoggableRegistry.setMethodIsReadOnly( preRemoveElementAtMethod );
		LoggableRegistry.setMethodIsReadOnly( preInsertElementAtMethod );
		
	}
	public MethodProxy getSizeMethod(ClassProxy objectClass) {
		return IntrospectUtility.getSizeMethod(objectClass);
	}
	public abstract  MethodProxy getElementsMethod(ClassProxy objectClass, boolean expected);
	public abstract  MethodProxy getElementAtMethod(ClassProxy objectClass, boolean expected);
	public abstract  MethodProxy getSetElementAtMethod(ClassProxy objectClass);
	public abstract  MethodProxy getAddElementMethod(ClassProxy objectClass);
	public abstract  MethodProxy getRemoveElementMethod(ClassProxy objectClass);
	public abstract  MethodProxy getInsertElementAtMethod(ClassProxy objectClass);
	public abstract  MethodProxy getRemoveElementAtMethod(ClassProxy objectClass);
	/*
	public  VirtualMethod getSizeMethod(Class objectClass) {		return uiBean.getSizeMethod(objectClass);
	}	public  VirtualMethod getElementsMethod(Class objectClass) {		return uiBean.getElementsMethod(objectClass);
	}	public  VirtualMethod getElementAtMethod(Class objectClass) {		return uiBean.getElementAtMethod(objectClass);
	}	public  VirtualMethod getSetElementAtMethod(Class objectClass) {		return uiBean.getSetElementAtMethod(objectClass);
	}
	public  VirtualMethod getAddElementMethod(Class objectClass) {		return uiBean.getAddElementMethod(objectClass);
	}	public  VirtualMethod getRemoveElementMethod(Class objectClass) {		return uiBean.getRemoveElementMethod(objectClass);
	}	public  VirtualMethod getInsertElementAtMethod(Class objectClass) {		return uiBean.getInsertElementAtMethod(objectClass);
	}	public  VirtualMethod getRemoveElementAtMethod(Class objectClass) {		return uiBean.getRemoveElementAtMethod(objectClass);
	}
	
	*/	
				
	int sizeFromTargetSize() {		Object[] params = {};		try {
			//Object retVal = sizeMethod.invoke(targetObject, params);
			Object retVal = MethodInvocationManager.invokeMethod(sizeMethod, 
					  targetObject,
					  emptyParams);	
			return ((Integer) retVal).intValue();		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Please trace size method of:" + targetObject);
			return -1;		}
	}
	abstract int sizeFromTargetElements();
	/*	int sizeFromTargetElements() {
		Enumeration elements = elementsFromTargetElements();			if (elements == null) return -1;			else {
				int retVal = 0;
				while (elements.hasMoreElements()) {					elements.nextElement();					retVal++;
				}
				return retVal;			}		
	}
	*/
	public int size() {		if (sizeMethod != null)
			//return size(targetObject);			return sizeFromTargetSize();
		else {
			/*			Enumeration elements = elements();			if (elements == null) return -1;			else {
				int retVal = 0;
				while (elements.hasMoreElements()) {					elements.nextElement();					retVal++;
				}
				return retVal;			}
			*/
			return sizeFromTargetElements();		
		}	}	Enumeration elementsFromTargetElements() {
				Object[] params = {};		try {
			//return (Enumeration) elementsMethod.invoke(targetObject, params);
			return (Enumeration) MethodInvocationManager.invokeMethod(targetObject, elementsMethod, params);
					} catch (Exception e) {
			return null;		}
	}	/*
	public Enumeration elements() {		if (elementsMethod != null)			return elements(targetObject);		else {
			Vector v = new Vector();
			if (sizeMethod == null || elementAtMethod == null)
				return null;
			for (int i = 0; i < size(); i++)
				v.addElement(elementAt(i));
			return v.elements();		}
	}		*/
	//public abstract Object elementAtFromTargetElementAt(int i);
	
	public  Object elementAtFromTargetElementAt(int i) {
		try {			
			Object[] params = {new Integer(i)};			//return elementAtMethod.invoke(targetObject, params);
			return  MethodInvocationManager.invokeMethod(targetObject, elementAtMethod, params);		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Please trace elementAt method of:" + targetObject + " with index " + i);
			return null;		}		
	}
		
	public Object elementAt(int i) {
		if (getTargetObject() == null)
		return 0;
	if (getTargetObject() instanceof AListenableVector) {
		return ((AListenableVector) getTargetObject()).internalElementAt(i);
	}
		if (elementAtMethod != null)
			return elementAtFromTargetElementAt(i);
		else {
			//System.err.println("DId not find elementAt!");
			return elementAtFromTargetElements(i);
			/*			
			Enumeration elements = elementsFromTargetElements();			if (elements == null) return null;			else {
				Object retVal = null;
				for (int j = 0; elements.hasMoreElements()&& j < i; retVal = elements.nextElement());
				return retVal;			}
			*/
		}
	}
	public Object elementAtFromTargetElements(int i) {
		
			Enumeration elements = elementsFromTargetElements();			if (elements == null) return null;			else {
				Object retVal = null;
				for (int j = 0; elements.hasMoreElements()&& j <= i;j++ ) {					retVal = elements.nextElement();
				}				
				return retVal;			}
	}	/*
	public Object elementAt(int i) {
		if (elementAtMethod != null)
			return elementAt(targetObject, i);
		else {
			Enumeration elements = elements();			if (elements == null) return null;			else {
				Object retVal = null;
				for (int j = 0; elements.hasMoreElements()&& j < i; retVal = elements.nextElement());
				return retVal;			}
		}
	}
	
	static boolean hasMoreElements (Object target) {
		try {
		Method hasMoreElementsMethod = uiBean.getMethod(target, 
														"hasMoreElements",
														Boolean.class,
														null);
		Object[] nullParams = {};
		return ((Boolean) hasMoreElementsMethod.invoke(target, null)).booleanValue();
		
		} catch (Exception e) { return false;}
	}
	static boolean nextElement (Object target) {
		try {
		Method hasMoreElementsMethod = uiBean.getMethod(target, 
														"hasMoreElements",
														Boolean.class,
														null);
		Object[] nullParams = {};
		return ((Boolean) hasMoreElementsMethod.invoke(target, null)).booleanValue();
		
		} catch (Exception e) { return false;}
	}
	
	
	public int indexOf(Object element) {
		try {			
			Object[] params = {element};			return ((Integer)(indexOfMethod.invoke(targetObject, params))).intValue();		} catch (Exception e) {
			System.err.println(e);
			return -1;		}	}	*/
	public abstract void setElementAt(Object element, int pos);
	/*
	public void setElementAt(Object element, int pos) {
		Object[] params = {element, new Integer(pos)};
		uiMethodInvocationManager.invokeMethod(targetObject, setElementAtMethod, params);		//invokeWriteMethod(setElementAtMethod, targetObject, params);
	}
	*/
	public abstract boolean validateSetElementAt(Object element, int pos);
	/*
	public boolean validateSetElementAt(Object element, int pos) {
		if (validateSetElementAtMethod == null)
			return true;
		Object[] params = {element, new Integer(pos)};
		return (Boolean) uiMethodInvocationManager.invokeMethod(targetObject, validateSetElementAtMethod, params);
		//invokeWriteMethod(setElementAtMethod, targetObject, params);
	}
	*/
	/*	public   void addElement(Object element) {		
		Object[] params = {element};		invokeWriteMethod (addElementMethod, targetObject, params);		
		
	}	public  void removeElement(Object element) {
			Object[] params = {element};			invokeWriteMethod (removeElementMethod, targetObject, params);
		
	}
	public  void insertElementAt(Object element, int i) {	
			Object[] params = {element, new Integer(i)};			invokeWriteMethod (insertElementAtMethod, targetObject, params);
	}
	public  void removeElementAt(int i) {
		Object[] params = {new Integer(i)};		invokeWriteMethod (removeElementAtMethod, targetObject, params);
	}
	*/
	
	boolean undoableInsertElementAt() {		return insertElementAtMethod != null && removeElementAtMethod != null && sizeMethod != null; 
	}
	
	void addUsingInsertElementAt (Object element, CommandListener commandListener) {
		//wonder why the executre was called directly or why this method is needed
		insertElementAt (element, size(), commandListener);
		/*		Object params[] = {element, new Integer(size())};
			frame.getUndoer().execute (
							new AddSubtractFirstCommand(//getUIFrame(),							  		commandListener,
									insertElementAtMethod,							  		targetObject,							  		params,									removeElementAtMethod
							));	
							*/	
	}
	public abstract void insertElementAt (Object element,  int pos, CommandListener commandListener);
	/*
	public void insertElementAt (Object element,  int pos, CommandListener commandListener) {
		Object params[] = {element, pos};
		if (commandListener == null) {
			uiMethodInvocationManager.invokeMethod(targetObject, insertElementAtMethod, params);
			//insertElementAtMethod.invoke(targetObject, params);
		} else
			frame.getUndoer().execute (
							new AddSubtractFirstCommand(//getUIFrame(),
							  		commandListener,
									insertElementAtMethod,
							  		targetObject,
							  		params,
									removeElementAtMethod
							));		
	}
	*/
	public abstract boolean validateInsertElementAt (Object element,  int pos);
	/*
	public boolean validateInsertElementAt (Object element,  int pos) {
		if (validateInsertElementAtMethod == null)
			return true;
		Object params[] = {element, pos};
		return (Boolean) uiMethodInvocationManager.invokeMethod(targetObject, validateInsertElementAtMethod, params);
		
			//insertElementAtMethod.invoke(targetObject, params);
	}
	*/
	public boolean validateAddElement (Object element) {
		if (validateAddElementMethod == null)
			return true;
		Object params[] = {element};
		return (Boolean) MethodInvocationManager.invokeMethod(targetObject, validateAddElementMethod, params);
		
			//insertElementAtMethod.invoke(targetObject, params);
	}
	public boolean isEditable (int index) {
		if (!hasSetChildMethod() || !preSetElementAt())
			return false;
		if (isIndexedChildEditableMethod == null)
			return true;
		Object params[] = {index};
		return (Boolean) MethodInvocationManager.invokeMethod(targetObject, isIndexedChildEditableMethod, params);
		
			//insertElementAtMethod.invoke(targetObject, params);
	}
	
	void addUsingAddElementAt (Object element, CommandListener commandListener) {
		Object params[] = {element};
			frame.getUndoer().execute (
							new SymmetricCommand(//getUIFrame(),							  		commandListener,
							  		addElementMethod,							  		targetObject,							  		params,									removeElementMethod
							));			
	}	
	public void addElement(Object element, CommandListener commandListener) {
		
		
		//if (insertElementAtMethod != null && removeElementAtMethod != null && sizeMethod != null) {
		if (undoableInsertElementAt()) {
			addUsingInsertElementAt(element, commandListener);
			/*
			Object params[] = {element, new Integer(size())};
			frame.getUndoer().execute (
							new AddSubtractFirstCommand(//getUIFrame(),							  		commandListener,
									insertElementAtMethod,							  		targetObject,							  		params,									removeElementAtMethod
							));	
			*/
			/*
			uiMethodInvocationManager.invokeMethod(frame, 
												   targetObject,
												   insertElementAtMethod,
												   params,
												   commandListener);
			*/
												   
		} else  {			
			addUsingAddElementAt(element, commandListener);
			/*
			Object params[] = {element};
			frame.getUndoer().execute (
							new SymmetricCommand(//getUIFrame(),							  		commandListener,
							  		addElementMethod,							  		targetObject,							  		params,									removeElementMethod
							));	
			*/
			/*
			uiMethodInvocationManager.invokeMethod(frame, 
												   targetObject,
												   addElementMethod,
												   params,
												   commandListener);
			*/
		}			
		
	}
	public ClassProxy addElementElementType() {
		ClassProxy[] parameterTypes = addElementMethod.getParameterTypes();
		return  parameterTypes[0];
		
	}
	public abstract ClassProxy insertElementAtElementType();
	/*
	public Class insertElementAtElementType() {
		Class[] parameterTypes = insertElementAtMethod.getParameterTypes();
		return  parameterTypes[0];
	}
	*/
	public ClassProxy addableElementType () {
		if (addElementMethod != null) {			return addElementElementType();
			/*		Class[] parameterTypes = addElementMethod.getParameterTypes();
		return  parameterTypes[0];			*/
		} else if (insertElementAtMethod != null) {			return insertElementAtElementType();
			/*			Class[] parameterTypes = insertElementAtMethod.getParameterTypes();
			return  parameterTypes[0];			*/
		} else return null;	}
	
	public boolean hasDeleteChildMethod() {
		return removeElementAtMethod != null || removeElementMethod != null;
	}
	public boolean hasInsertChildMethod() {
		return insertElementAtMethod != null; 
	}
	public boolean hasAddChildMethod() {
		return hasInsertChildMethod() || addElementMethod != null;
	}
	public boolean hasSetChildMethod() {
		return setElementAtMethod != null;
	}
	abstract boolean undoableRemoveElementAt();
	/*
	boolean undoableRemoveElementAt() {
		return removeElementAtMethod != null && insertElementAtMethod != null && elementAtMethod != null;
	}
	*/
	public abstract void removeElementAt(int index, CommandListener commandListener);
	/*
	public void removeElementAt(int index, CommandListener commandListener) {
		Object params[] = {new Integer(index)};
		if (commandListener == null) {
			uiMethodInvocationManager.invokeMethod(removeElementAtMethod, targetObject, params);
		} else
				//setElementAtMethod.invoke(parentObject, params);				
				frame.getUndoer().execute (
							new VoidSubtractAddFirstCommand(//getUIFrame(),							  		commandListener,
							  		removeElementAtMethod,							  		targetObject,							  		params,							  		insertElementAtMethod,
							  		//readMethodParams							  		elementAtMethod
							));			
		
	}
	*/
	public boolean validateRemoveElementAt(int index) {
		if (validateRemoveElementAtMethod == null)
			return true;
		Object params[] = {new Integer(index)};
		return (Boolean) MethodInvocationManager.invokeMethod(validateRemoveElementAtMethod, targetObject, params);
	}
	public boolean validateElementAt(int index) {
		if (validateElementAtMethod == null)
			return true;
		Object params[] = {new Integer(index)};
		return (Boolean) MethodInvocationManager.invokeMethod(validateElementAtMethod, targetObject, params);
	}
	public void removeElement(Object element, CommandListener commandListener) {
		Object params[] = {element};
					frame.getUndoer().execute (
							new SymmetricCommand(//getUIFrame(),									commandListener,
									removeElementMethod,									targetObject,									params,									addElementMethod
							));
		
	}
	public boolean validateRemoveElement(Object element) {
		if (validateRemoveElementMethod == null)
			return true;
		Object params[] = {element};
		return (Boolean) MethodInvocationManager.invokeMethod(validateRemoveElementMethod, targetObject, params);
	}
	public void removeElement(int index, Object element, CommandListener commandListener) {
		try {			//if (removeElementAtMethod != null && insertElementAtMethod != null && elementAtMethod != null) {
			if (undoableRemoveElementAt()) {
				removeElementAt(index,  commandListener);
				/*				Object params[] = {new Integer(index)};
				//setElementAtMethod.invoke(parentObject, params);				
				frame.getUndoer().execute (
							new VoidSubtractAddFirstCommand(//getUIFrame(),							  		commandListener,
							  		removeElementAtMethod,							  		targetObject,							  		params,							  		insertElementAtMethod,
							  		//readMethodParams							  		elementAtMethod
							));					*/
				
				//uiMethodInvocationManager.invokeMethod(this.getUIFrame(), parentObject, removeElementAtMethod, params);			} else if (removeElementAtMethod != null) {				
					removeElementAt(index,  null);
			}
			else 	if (removeElementMethod != null) {
				removeElement(element, commandListener);				/*
					Object params[] = {element};
					frame.getUndoer().execute (
							new SymmetricCommand(//getUIFrame(),									commandListener,
									removeElementMethod,									targetObject,									params,									addElementMethod
							));
				*/					//uiMethodInvocationManager.invokeMethod(this.getUIFrame(), parentObject, removeElementMethod, params);
				}			}
		catch (Exception e) {
			System.err.println(e);
		}
		
	}
	public abstract void setElementAt(Object element, int pos, CommandListener commandListener);
	/*
	public void setElementAt(Object element, int pos, CommandListener commandListener) {
		if (pos == -1) { 
			System.err.println("Unexpected negative index in setElementAt");
			return;
		}
		Object params[] = {element, new Integer(pos)};
		
		frame.getUndoer().execute (				new SetGetFirstCommand(commandListener, 					setElementAtMethod, 					targetObject,					params,											   
					elementAtMethod//,
					//elementAtMethodParams					));
		
	}
	*/
	/*
	public  void invokeWriteMethod (Method m, Object o, Object[] params) {		try {
			//m.invoke(o, params);			uiMethodInvocationManager.invokeMethod(o, m, params);
		} catch (Exception e) {			System.err.println(e);
		}	}	public  void invokeUndoableWriteMethod (Method m, Object o, Object[] params, CommandListener cl) {		try {
			//m.invoke(o, params);			uiMethodInvocationManager.invokeMethod(frame, o, m, params, cl);
		} catch (Exception e) {			System.err.println(e);
		}	}	*/
	public static String SEQUENCE = "Sequence";
	public String programmingPatternKeyword() {
		return  AbstractConcreteType.PROGRAMMING_PATTERN + AttributeNames.KEYWORD_SEPARATOR + SEQUENCE;
	}
	public String typeKeyword() {
		return ObjectEditor.TYPE_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + SEQUENCE;
	}
	Object nullParams[] = {};
	public boolean preRemoveElement() {
		if (preRemoveElementMethod == null)
			return true;		
		return (Boolean) MethodInvocationManager.invokeMethod(preRemoveElementMethod, targetObject, nullParams);
		
	}
	public boolean preRemoveElementAt() {
		if (preRemoveElementAtMethod == null)
			return true;		
		return (Boolean) MethodInvocationManager.invokeMethod(preRemoveElementAtMethod, targetObject, nullParams);
	}
	public boolean preAddElement() {
		if (preAddElementMethod == null)
			return true;		
		return (Boolean) MethodInvocationManager.invokeMethod(preAddElementMethod, targetObject, nullParams);
	}
	public boolean preSetElementAt() {
		if (preSetElementAtMethod == null)
			return true;		
		return (Boolean) MethodInvocationManager.invokeMethod(preSetElementAtMethod, targetObject, nullParams);
	}
	public boolean preInsertElementAt () {
		if (preInsertElementAtMethod == null)
			return true;		
		return (Boolean) MethodInvocationManager.invokeMethod(preInsertElementAtMethod, targetObject, nullParams);
	}
	public boolean preElementAt() {
		if (preElementAtMethod == null)
			return true;		
		return (Boolean) MethodInvocationManager.invokeMethod(preElementAtMethod, targetObject, nullParams);
	}
	
}
