package bus.uigen.sadapters;
import java.util.*;import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

import util.annotations.StructurePatternNames;
import util.models.AListenableVector;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericVectorToVectorStructure extends GenericListToVectorStructure  implements  VectorStructure  {
	/*	transient VirtualMethod setElementsMethod = null;		transient VirtualMethod elementsMethod = null;	transient VirtualMethod addElementMethod = null;		transient VirtualMethod insertElementAtMethod = null; 	transient VirtualMethod removeElementMethod = null;	 	transient VirtualMethod removeElementAtMethod = null;
	transient VirtualMethod elementAtMethod = null ;	transient VirtualMethod sizeMethod = null;
	transient VirtualMethod isEmptyMethod = null;	transient VirtualMethod setElementAtMethod = null;
	transient VirtualMethod indexOfMethod = null;
	transient VirtualMethod validateSetElementAtMethod = null;
	transient VirtualMethod validateAddElementMethod = null;	
	transient VirtualMethod validateInsertElementAtMethod = null; 
	transient VirtualMethod validateRemoveElementMethod = null;	 
	transient VirtualMethod validateRemoveElementAtMethod = null;
	*/	/*	Class targetClass;
	Object targetObject;	uiFrame frame;	*/
	public GenericVectorToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		init(theGVectorObject, theFrame );
	}	public GenericVectorToVectorStructure () {
	}
	/*
	public boolean isEditingMethod(VirtualMethod targetMethod) {
		return  super.isEditingMethod(targetMethod) ||	
				targetMethod == isEmptyMethod ||
				targetMethod == elementAtMethod ||
				targetMethod == sizeMethod ||
				targetMethod == elementsMethod ||
				targetMethod == setElementsMethod ||
				targetMethod == removeElementAtMethod ||
				targetMethod == removeElementMethod ||
				targetMethod == indexOfMethod;
	}
	public boolean isPatternMethod(VirtualMethod targetMethod) {
		return super.isPatternMethod(targetMethod)|| isEditingMethod(targetMethod) ||
				targetMethod == addElementMethod;
				//targetMethod == insertElementAtMethod ||
				//targetMethod == removeElementAtMethod ||
				
	}
	*/	/*
	public void init(Object theTargetObject, uiFrame theFrame) {		
		setTarget(theTargetObject);		frame = theFrame;
		setMethods(targetClass);
	}	public void setTarget(Object theTargetObject) {
		targetClass = theTargetObject.getClass();
		targetObject = theTargetObject;	}	*/
	/*			public Method getElementsMethod() {
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
	/*
	public void setOtherMethods(Class objectClass) {
		isEmptyMethod = uiBean.getIsEmptyMethod(objectClass);
	}
	public void setMethods(Class objectClass) {			super.setMethods(objectClass);
		setReadMethods(objectClass);
		setReadMethodDescriptors();
		setWriteMethods(objectClass);
		setWriteMethodDescriptors();
		setValidateMethods(objectClass);
		setOtherMethods(objectClass);
		sortedComponentNames.remove("empty");
		try {
			ViewInfo cd = ClassDescriptorCache.getClassDescriptor(objectClass);
			if (cd.getPropertyDescriptor(AttributeNames.ELEMENT_NAME) == null) {
				
				
			// should check if element already exists!
			// this should probably be added by ClassDescriptor
			Vector<PropertyDescriptor> v = new Vector();			
			 v.addElement(new PropertyDescriptor(AttributeNames.ELEMENT_NAME, null, null));
			  //ViewInfo cd = ClassDescriptorCache.getClassDescriptor(objectClass);
			  cd.addProperties(v);
			}
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
		
	}
	
	public void setReadMethods(Class objectClass) {		sizeMethod = getSizeMethod(objectClass);		elementsMethod = getElementsMethod(objectClass);
		elementAtMethod = getElementAtMethod(objectClass);
				//indexOfMethod = uiBean.getIndexOfMethod(objectClass);
	}
	public void setReadMethodDescriptors () {
		ViewInfo cd = ClassDescriptorCache.getClassDescriptor(getTargetClass());		
		MethodDescriptor elementAtMD = cd.getMethodDescriptor(elementAtMethod);
		if (elementAtMD != null) {
			cd.setAttribute(AttributeNames.READ_ELEMENT_METHOD, elementAtMethod);
		}
	}
	public void setWriteMethods(Class objectClass) {		setElementAtMethod = getSetElementAtMethod(objectClass);					addElementMethod = getAddElementMethod(objectClass);		
		removeElementMethod = getRemoveElementMethod(objectClass);		insertElementAtMethod = getInsertElementAtMethod(objectClass);		removeElementAtMethod = getRemoveElementAtMethod(objectClass);
	}
	public void setWriteMethodDescriptors () {
		ViewInfo cd = ClassDescriptorCache.getClassDescriptor(getTargetClass());		
		MethodDescriptor removeElementMD = cd.getMethodDescriptor(removeElementMethod);
		if (removeElementMD != null) {
			removeElementMD.setValue(AttributeNames.IS_REMOVE_METHOD, true);
		}
		MethodDescriptor addElementMD = cd.getMethodDescriptor(addElementMethod);
		if (addElementMD != null && removeElementMD != null) {
			addElementMD.setValue(AttributeNames.INVERSE, removeElementMethod);
		}
		
	}
	public void setValidateMethods(Class objectClass) {
		//VirtualMethod validate = uiBean.getValidate(uiMethodInvocationManager.virtualMethod(property.getWriteMethod()), targetClass);
		validateSetElementAtMethod = uiBean.getValidate(setElementAtMethod, targetClass);			
		validateAddElementMethod = 	uiBean.getValidate(addElementMethod, targetClass);		
		validateRemoveElementMethod = 	uiBean.getValidate(removeElementMethod, targetClass);
		validateRemoveElementMethod = 	uiBean.getValidate(removeElementMethod, targetClass);
		validateRemoveElementAtMethod = uiBean.getValidate(removeElementAtMethod, targetClass);
		validateInsertElementAtMethod = uiBean.getValidate(insertElementAtMethod, targetClass);
	}
	public  VirtualMethod getSizeMethod(Class objectClass) {		return uiBean.getSizeMethod(objectClass);
	}
	*/	public  MethodProxy getElementsMethod(ClassProxy objectClass, boolean expected) {		return IntrospectUtility.getElementsMethod(objectClass);
	}	public  MethodProxy getElementAtMethod(ClassProxy objectClass, boolean expected) {		return IntrospectUtility.getElementAtMethod(objectClass, false);
	}	public  MethodProxy getSetElementAtMethod(ClassProxy objectClass) {		return IntrospectUtility.getSetElementAtMethod(objectClass);
	}
	public  MethodProxy getAddElementMethod(ClassProxy objectClass) {		return IntrospectUtility.getAddElementMethod(objectClass);
	}	public  MethodProxy getRemoveElementMethod(ClassProxy objectClass) {		return IntrospectUtility.getRemoveElementMethod(objectClass);
	}	public  MethodProxy getInsertElementAtMethod(ClassProxy objectClass) {		return IntrospectUtility.getInsertElementAtMethod(objectClass);
	}	public  MethodProxy getRemoveElementAtMethod(ClassProxy objectClass) {		return IntrospectUtility.getRemoveElementAtMethod(objectClass);
	}
	
		
	/*			
	int sizeFromTargetSize() {		Object[] params = {};		try {
			Object retVal = sizeMethod.invoke(targetObject, params);
			return ((Integer) retVal).intValue();		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Please trace size method of:" + targetObject);
			return -1;		}
	}
	*/	int sizeFromTargetElements() {
		Enumeration elements = elementsFromTargetElements();			if (elements == null) return -1;			else {
				int retVal = 0;
				while (elements.hasMoreElements()) {					elements.nextElement();					retVal++;
				}
				return retVal;			}		
	}
	public int size() {
		
		if (getTargetObject() == null)
			return 0;
		if (getTargetObject() instanceof AListenableVector) {
			return ((AListenableVector) getTargetObject()).internalSize();
		}		if (sizeMethod != null)
			//return size(targetObject);			return sizeFromTargetSize();
		else {
			
			return sizeFromTargetElements();		
		}	}
	/*	Enumeration elementsFromTargetElements() {
				Object[] params = {};		try {
			return (Enumeration) elementsMethod.invoke(targetObject, params);		} catch (Exception e) {
			return null;		}
	}
	*/	/*
	public Enumeration elements() {		if (elementsMethod != null)			return elements(targetObject);		else {
			Vector v = new Vector();
			if (sizeMethod == null || elementAtMethod == null)
				return null;
			for (int i = 0; i < size(); i++)
				v.addElement(elementAt(i));
			return v.elements();		}
	}		*/
	/*
	public  Object elementAtFromTargetElementAt(int i) {
		try {			
			Object[] params = {new Integer(i)};			return elementAtMethod.invoke(targetObject, params);		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Please trace elementAt method of:" + targetObject + " with index " + i);
			return null;		}		
	}
	*/	/*
	public Object elementAt(int i) {
		if (elementAtMethod != null)
			return elementAtFromTargetElementAt(i);
		else {
			//System.err.println("DId not find elementAt!");
			return elementAtFromTargetElements(i);
			
		}
	}
	*/
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
	
	public void setElementAt(Object element, int pos) {
		Object[] params = {element, new Integer(pos)};
		MethodInvocationManager.invokeMethod(targetObject, setElementAtMethod, params);		//invokeWriteMethod(setElementAtMethod, targetObject, params);
	}
	public boolean validateSetElementAt(Object element, int pos) {
		if (validateSetElementAtMethod == null)
			return true;
		Object[] params = {element, new Integer(pos)};
		return (Boolean) MethodInvocationManager.invokeMethod(targetObject, validateSetElementAtMethod, params);
		//invokeWriteMethod(setElementAtMethod, targetObject, params);
	}
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
	/*
	boolean undoableInsertElementAt() {		return insertElementAtMethod != null && removeElementAtMethod != null && sizeMethod != null; 
	}
	*/
	/*
	void addUsingInsertElementAt (Object element, CommandListener commandListener) {
		//wonder why the executre was called directly or why this method is needed
		insertElementAt (element, size(), commandListener);
		
	}
	*/
	public void insertElementAt (Object element,  int pos, CommandListener commandListener) {
		Object params[] = {element, pos};
		if (commandListener == null) {
			MethodInvocationManager.invokeMethod(targetObject, insertElementAtMethod, params);
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
	public boolean validateInsertElementAt (Object element,  int pos) {
		if (validateInsertElementAtMethod == null)
			return true;
		Object params[] = {element, pos};
		return (Boolean) MethodInvocationManager.invokeMethod(targetObject, validateInsertElementAtMethod, params);
		
			//insertElementAtMethod.invoke(targetObject, params);
	}
	/*
	public boolean validateAddElement (Object element) {
		if (validateAddElementMethod == null)
			return true;
		Object params[] = {element};
		return (Boolean) uiMethodInvocationManager.invokeMethod(targetObject, validateAddElementMethod, params);
		
			//insertElementAtMethod.invoke(targetObject, params);
	}
	
	void addUsingAddElementAt (Object element, CommandListener commandListener) {
		Object params[] = {element};
			frame.getUndoer().execute (
							new SymmetricCommand(//getUIFrame(),							  		commandListener,
							  		addElementMethod,							  		targetObject,							  		params,									removeElementMethod
							));			
	}
	*/	/*
	public void addElement(Object element, CommandListener commandListener) {
		
		
		//if (insertElementAtMethod != null && removeElementAtMethod != null && sizeMethod != null) {
		if (undoableInsertElementAt()) {
			addUsingInsertElementAt(element, commandListener);
			
												   
		} else  {			
			addUsingAddElementAt(element, commandListener);
			
		}			
		
	}
	
	public Class addElementElementType() {
		Class[] parameterTypes = addElementMethod.getParameterTypes();
		return  parameterTypes[0];
		
	}
	*/
	public ClassProxy insertElementAtElementType() {
		ClassProxy[] parameterTypes = insertElementAtMethod.getParameterTypes();
		return  parameterTypes[0];
	}
	public ClassProxy addableElementType () {
		if (addElementMethod != null) {			return addElementElementType();
			/*		Class[] parameterTypes = addElementMethod.getParameterTypes();
		return  parameterTypes[0];			*/
		} else if (insertElementAtMethod != null) {			return insertElementAtElementType();
			/*			Class[] parameterTypes = insertElementAtMethod.getParameterTypes();
			return  parameterTypes[0];			*/
		} else return null;	}
	/*
	public boolean canDeleteChild() {
		return removeElementAtMethod != null || removeElementMethod != null;
	}
	public boolean canSetChild() {
		return setElementAtMethod != null;
	}
	*/
	boolean undoableRemoveElementAt() {
		return removeElementAtMethod != null && insertElementAtMethod != null && elementAtMethod != null;
	}
	public void removeElementAt(int index, CommandListener commandListener) {
		Object params[] = {new Integer(index)};
		if (commandListener == null) {
			MethodInvocationManager.invokeMethod(removeElementAtMethod, targetObject, params);
		} else {
				//setElementAtMethod.invoke(parentObject, params);
			Command subtractCommand;			if (voidRemovElementAtMethod)
				subtractCommand = new VoidSubtractAddFirstCommand(//getUIFrame(),
				  		commandListener,
				  		removeElementAtMethod,
				  		targetObject,
				  		params,
				  		insertElementAtMethod,
				  		//readMethodParams
				  		elementAtMethod	);
			else
				subtractCommand = new SubtractAddFirstCommand(//getUIFrame(),
				  		commandListener,
				  		removeElementAtMethod,
				  		targetObject,
				  		params,
				  		insertElementAtMethod);
			frame.getUndoer().execute(subtractCommand);
			/*
				frame.getUndoer().execute (
							new VoidSubtractAddFirstCommand(//getUIFrame(),							  		commandListener,
							  		removeElementAtMethod,							  		targetObject,							  		params,							  		insertElementAtMethod,
							  		//readMethodParams							  		elementAtMethod
							));	
							*/
		}
		
	}
	/*
	public boolean validateRemoveElementAt(int index) {
		if (validateRemoveElementAtMethod == null)
			return true;
		Object params[] = {new Integer(index)};
		return (Boolean) uiMethodInvocationManager.invokeMethod(validateRemoveElementAtMethod, targetObject, params);
	}
	*/
	/*
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
		return (Boolean) uiMethodInvocationManager.invokeMethod(validateRemoveElementMethod, targetObject, params);
	}
	*/
	/*
	public void removeElement(int index, Object element, CommandListener commandListener) {
		try {			//if (removeElementAtMethod != null && insertElementAtMethod != null && elementAtMethod != null) {
			if (undoableRemoveElementAt()) {
				removeElementAt(index,  commandListener);
				
				
				//uiMethodInvocationManager.invokeMethod(this.getUIFrame(), parentObject, removeElementAtMethod, params);			} else if (removeElementAtMethod != null) {				
					removeElementAt(index,  null);
			}
			else 	if (removeElementMethod != null) {
				removeElement(element, commandListener);									//uiMethodInvocationManager.invokeMethod(this.getUIFrame(), parentObject, removeElementMethod, params);
				}			}
		catch (Exception e) {
			System.err.println(e);
		}
		
	}
	*/
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
	/*
	public  void invokeWriteMethod (Method m, Object o, Object[] params) {		try {
			//m.invoke(o, params);			uiMethodInvocationManager.invokeMethod(o, m, params);
		} catch (Exception e) {			System.err.println(e);
		}	}	public  void invokeUndoableWriteMethod (Method m, Object o, Object[] params, CommandListener cl) {		try {
			//m.invoke(o, params);			uiMethodInvocationManager.invokeMethod(frame, o, m, params, cl);
		} catch (Exception e) {			System.err.println(e);
		}	}	*/
	public String getPatternName() {
		return StructurePatternNames.VECTOR_PATTERN;		
	}
	
}
