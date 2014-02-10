package bus.uigen.sadapters;

import java.util.*;
import java.lang.reflect.*;

import util.annotations.StructurePatternNames;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.undo.*;
import bus.uigen.*;
import bus.uigen.introspect.*;
import bus.uigen.controller.MethodInvocationManager;

public class GenericStackToVectorStructure extends
		AbstractVectorToVectorStructure {

	public GenericStackToVectorStructure(Object theGVectorObject,
			uiFrame theFrame) {
		super(theGVectorObject, theFrame);
	}

	public GenericStackToVectorStructure() {
	}

	/*
	 * public void setReadMethods(Class objectClass) { sizeMethod =
	 * getSizeMethod(objectClass); elementsMethod =
	 * getElementsMethod(objectClass); elementAtMethod =
	 * getElementAtMethod(objectClass); } public void setWriteMethods(Class
	 * objectClass) { setElementAtMethod = getSetElementAtMethod(objectClass);
	 * addElementMethod = getAddElementMethod(objectClass); removeElementMethod =
	 * getRemoveElementMethod(objectClass); insertElementAtMethod =
	 * getInsertElementAtMethod(objectClass); removeElementAtMethod =
	 * getRemoveElementAtMethod(objectClass); }
	 */
	public MethodProxy getAddAllMethod(ClassProxy objectClass) {
		// return uiBean.getKeysMethod(objectClass);
		return null;
	}

	

	public MethodProxy getElementAtMethod(ClassProxy objectClass, boolean expected) {
		try {
			ClassProxy[] paramTypes = { objectClass.integerType() };
			// not sure we want to constrain to objectClass
			return IntrospectUtility.getMethod(objectClass, "get", objectClass
					.objectClass(), paramTypes);
		} catch (Exception e) {
			return null;
		}
	}

	public MethodProxy getSetElementAtMethod(ClassProxy objectClass) {
		return null;
	}

	public MethodProxy getAddElementMethod(ClassProxy objectClass) {
		try {
			ClassProxy[] paramTypes = { objectClass.objectClass() };
			return IntrospectUtility.getMethod(objectClass, "push", null,
			// objectClass.booleanType(),
					paramTypes);
		} catch (Exception e) {
			return null;
		}

	}

	public MethodProxy getRemoveElementMethod(ClassProxy objectClass) {
		try {
			ClassProxy[] paramTypes = { objectClass.objectClass() };
			return IntrospectUtility.getMethod(objectClass, "pop", null,
			// objectClass.booleanType(),
					paramTypes);
		} catch (Exception e) {
			return null;
		}
	}

	
	public void setElementAt(Object element, int pos) {
		
	}

	public boolean validateSetElementAt(Object element, int pos) {
		return false;
		// invokeWriteMethod(setElementAtMethod, targetObject, params);
	}

	/*
	 * void addUsingInsertElementAt (Object element, CommandListener
	 * commandListener) {
	 * 
	 * Object params[] = {new Integer(size()), element};
	 * frame.getUndoer().execute ( new AddSubtractLastCommand(//getUIFrame(),
	 * commandListener, insertElementAtMethod, targetObject, params,
	 * removeElementAtMethod ));
	 *  }
	 */
	@Override
	public boolean hasValidateInsertElementAt() {
		return false;
	}

	@Override
	public boolean hasValidateAddElement() {
		return false;
	}

	public boolean validateInsertElementAt(Object element, int pos) {
		return false;
	}

	public void insertElementAt(Object element, int pos,
			CommandListener commandListener) {
		
	}

	public ClassProxy insertElementAtElementType() {
		return insertElementAtMethod.getParameterTypes()[1];
	}

	boolean undoableRemoveElementAt() {
		return removeElementAtMethod != null && insertElementAtMethod != null;
	}

	public void removeElementAt(int index, CommandListener commandListener) {
		

	}

	public void setElementAt(Object element, int pos,
			CommandListener commandListener) {
		
	}

	@Override
	public MethodProxy getElementsMethod(ClassProxy objectClass, boolean expected) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodProxy getInsertElementAtMethod(ClassProxy objectClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodProxy getRemoveElementAtMethod(ClassProxy objectClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	int sizeFromTargetElements() {
		// TODO Auto-generated method stub
		return 0;
	}
	public String getPatternName() {
		return StructurePatternNames.STACK_PATTERN;		
	}

}
