package bus.uigen.sadapters;

import java.util.*;
import java.lang.reflect.*;

import util.annotations.StructurePatternNames;
import util.trace.Tracer;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.undo.*;
import bus.uigen.*;
import bus.uigen.introspect.*;
import bus.uigen.controller.MethodInvocationManager;

public class GenericListToVectorStructure extends
		AbstractVectorToVectorStructure {

	public GenericListToVectorStructure(Object theGVectorObject,
			uiFrame theFrame) {
		super(theGVectorObject, theFrame);
	}

	public GenericListToVectorStructure() {
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
		ClassProxy[] paramTypes = {};
		return IntrospectUtility.getAddAllMethod(objectClass);
	}
	@Override
	public MethodProxy getElementsMethod(ClassProxy objectClass, boolean expected) {
		try {
			ClassProxy[] paramTypes = {};
			return IntrospectUtility.getMethod(objectClass, "iterator", RemoteSelector
					.forName("java.util.Iterator"), paramTypes);
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public MethodProxy getElementAtMethod(ClassProxy objectClass, boolean expected) {
		try {
			ClassProxy[] paramTypes = { objectClass.integerType() };
			// not sure we want to constrain to objectClass
			MethodProxy retVal = IntrospectUtility.getMethod(objectClass, "get", objectClass
					.objectClass(), paramTypes);
			if (expected && retVal == null)
				Tracer.error("Did not find in class: " + objectClass.getName() + " a read method with header: " +
				"public <T> get(int <parameter name>)");
			return retVal;
				
		} catch (Exception e) {
//			if (expected) {
//				Tracer.error("Did not find in class: " + objectClass + " the read method: " +
//						"public <T> get (int <Parameter Name>)");
//			}
			return null;
		}
	}

	public MethodProxy getSetElementAtMethod(ClassProxy objectClass) {
		try {
			ClassProxy[] paramTypes = { objectClass.integerType(),
					objectClass.objectClass() };
			return IntrospectUtility.getMethod(objectClass, "set",
			// Void.TYPE,
					null, paramTypes);
		} catch (Exception e) {
			return null;
		}
	}

	public MethodProxy getAddElementMethod(ClassProxy objectClass) {
		try {
			ClassProxy[] paramTypes = { objectClass.objectClass() };
			return IntrospectUtility.getMethod(objectClass, "add", null,
			// objectClass.booleanType(),
					paramTypes);
		} catch (Exception e) {
			return null;
		}

	}

	public MethodProxy getRemoveElementMethod(ClassProxy objectClass) {
		try {
			ClassProxy[] paramTypes = { objectClass.objectClass() };
			return IntrospectUtility.getMethod(objectClass, "remove", null,
			// objectClass.booleanType(),
					paramTypes);
		} catch (Exception e) {
			return null;
		}
	}

	public MethodProxy getInsertElementAtMethod(ClassProxy objectClass) {
		try {
			ClassProxy[] paramTypes = { objectClass.integerType(),
					objectClass.objectClass() };
			return IntrospectUtility.getMethod(objectClass, "add", objectClass.voidType(),
					paramTypes);
		} catch (Exception e) {
			return null;
		}
	}

	public MethodProxy getRemoveElementAtMethod(ClassProxy objectClass) {
		try {
			ClassProxy[] paramTypes = { objectClass.integerType() };
			return IntrospectUtility.getMethod(objectClass, "remove",
			// objectClass.objectClass(),
					objectClass.voidType(), paramTypes);
		} catch (Exception e) {
			return null;
		}

	}

	int sizeFromTargetElements() {
		Iterator elements = iteratorFromTargetElements();
		if (elements == null)
			return -1;
		else {
			int retVal = 0;
			while (elements.hasNext()) {
				elements.next();
				retVal++;
			}
			return retVal;
		}

	}

	Iterator iteratorFromTargetElements() {

		Object[] params = {};
		try {
			// return (Iterator) elementsMethod.invoke(targetObject, params);
			return (Iterator) MethodInvocationManager.invokeMethod(
					targetObject, elementsMethod, params);
		} catch (Exception e) {
			return null;
		}
	}

	public Object elementAtFromTargetElements(int i) {

		Iterator elements = (Iterator) iteratorFromTargetElements();
		if (elements == null)
			return null;
		else {
			Object retVal = null;
			for (int j = 0; elements.hasNext() && j <= i; j++) {
				retVal = elements.next();
			}
			return retVal;
		}
	}

	public void setElementAt(Object element, int pos) {
		Object[] params = { new Integer(pos), element };
		MethodInvocationManager.invokeMethod(targetObject,
				setElementAtMethod, params);
		// invokeWriteMethod(setElementAtMethod, targetObject, params);
	}

	public boolean validateSetElementAt(Object element, int pos) {
		if (validateSetElementAtMethod == null)
			return true;
		Object[] params = { new Integer(pos), element };
		return (Boolean) MethodInvocationManager.invokeMethod(targetObject,
				validateSetElementAtMethod, params);
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
		return validateInsertElementAtMethod != null;
	}

	@Override
	public boolean hasValidateAddElement() {
		return validateAddElementMethod != null;
	}

	public boolean validateInsertElementAt(Object element, int pos) {
		if (validateInsertElementAtMethod == null)
			return true;
		Object params[] = { new Integer(pos), element };
		return (Boolean) MethodInvocationManager.invokeMethod(targetObject,
				validateInsertElementAtMethod, params);
	}

	public void insertElementAt(Object element, int pos,
			CommandListener commandListener) {
		Object params[] = { new Integer(pos), element };
		frame.getUndoer().execute(
				new AddSubtractLastCommand(
						// getUIFrame(),
						commandListener, insertElementAtMethod, targetObject,
						params, removeElementAtMethod));
	}

	public ClassProxy insertElementAtElementType() {
		return insertElementAtMethod.getParameterTypes()[1];
	}

	boolean undoableRemoveElementAt() {
		return removeElementAtMethod != null && insertElementAtMethod != null;
	}

	public void removeElementAt(int index, CommandListener commandListener) {
		Object params[] = { new Integer(index) };
		Command subtractCommand;
		if (voidRemoveElementMethod)
			subtractCommand = new VoidSubtractAddLastCommand(
					// getUIFrame(),
					commandListener, removeElementAtMethod, targetObject,
					params, insertElementAtMethod, elementAtMethod);
		else
			subtractCommand = new SubtractAddLastCommand(
					// getUIFrame(),
					commandListener, removeElementAtMethod, targetObject,
					params, insertElementAtMethod);
		frame.getUndoer().execute(subtractCommand);
		/*
		frame.getUndoer().execute(
				new SubtractAddLastCommand(
						// getUIFrame(),
						commandListener, removeElementAtMethod, targetObject,
						params, insertElementAtMethod));
						*/

	}

	public void setElementAt(Object element, int pos,
			CommandListener commandListener) {
		if (pos == -1) {
			System.out.println("Unexpected negative index in setElementAt");
			return;
		}
		Object params[] = { new Integer(pos), element };
		frame.getUndoer().execute(
				new SetGetLastCommand(commandListener, setElementAtMethod,
						targetObject, params, elementAtMethod));
	}
	public String getPatternName() {
		return StructurePatternNames.LIST_PATTERN;		
	}

}
