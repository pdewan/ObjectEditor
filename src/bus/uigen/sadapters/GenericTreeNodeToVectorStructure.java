package bus.uigen.sadapters;
import java.util.*;
import java.lang.reflect.*;

import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericTreeNodeToVectorStructure extends AbstractDynamicStructure /*BeanToRecord*/ implements  VectorStructure  {
	  transient MethodProxy getChildAtMethod = null;
	  transient MethodProxy getChildCountMethod = null; 
	  transient MethodProxy childrenMethod = null;
	  ///transient Method getIndexMethod = null;
	  transient MethodProxy isLeafMethod = null;
	  transient MethodProxy insertMethod = null;
	  transient MethodProxy removeMethod = null;
	  transient MethodProxy removeFromParentMethod = null;
	  //transient Method getUserObjectMethod = null;
	  //transient Method setUserObjectMethod = null;
	 ClassProxy[] nullArgs = {};  
	 String[] excludeProperties = {"parent", "childAt", "leaf", "childCount", "allowsChildren"};
	 String[] excludeProperties() {
		//String[] retVal = {};
		return excludeProperties;
	}
	 
	 public boolean excluded (String property) {
	 	Vector excludedPropertiesVector = uiGenerator.arrayToVector(excludeProperties());
	 	return(excludedPropertiesVector.contains(property));
	 }
	
	 boolean excludeFields () {
	 	return true;
	 }
	 
   public void setMethods(ClassProxy c) {	
   		  super.setMethods(c);
		  getChildAtMethod = IntrospectUtility.getTreeGetChildAtMethod(c);
		  getChildCountMethod = IntrospectUtility.getTreeGetChildCountMethod(c);
		  //getGetIndexMethod = uiBean.get
		  isLeafMethod = IntrospectUtility.getTreeIsLeafMethod(c);
		  insertMethod = IntrospectUtility.getTreeInsertMethod(c);
		  removeMethod = IntrospectUtility.getTreeRemoveMethod(c);		  
		  insertMethod = IntrospectUtility.getTreeInsertMethod(c);
		  //getUserObjectMethod = uiBean.getTreeGetUserObjectMethod(c);
		  //setUserObjectMethod = uiBean.getTreeSetUserObjectMethod(c);	
    }
   public boolean isGenericTreeNode() {
   		return getChildAtMethod != null &&
		  getChildCountMethod != null &&
		 isLeafMethod != null;
   }	
	public GenericTreeNodeToVectorStructure (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
	/*
	
	public Object getUserObject (Object o) {
		try {
		//getUserObjectMethod = uiBean.getTreeGetUserObjectMethod(o.getClass());
		if (getUserObjectMethod == null) return null;
		Object[] nullObjectArgs = {};
		return getUserObjectMethod.invoke(o, nullObjectArgs);
		} catch (Exception e) {
			return null;
		}
		
	}
	public Object getUserObject () {
		return getUserObject(targetObject);
		
	}
	public void setUserObject (Object o, Object newVal) {
		try {

		Class[] userObjectArgs = {Object.class};
		//setUserObjectMethod = uiBean.getTreeSetUserObjectMethod(o.getClass());		
		if (setUserObjectMethod == null) return ;
		Object[] objectArgs = {newVal};
		setUserObjectMethod.invoke(o, objectArgs);
		} catch (Exception e) {
			return ;
		}
		
	}
	public void setUserObject (Object newVal) {
		setUserObject (targetObject, newVal);
		
	}
	public boolean hasUserObject() {
		return getUserObjectMethod != null;
	}
	public boolean hasEditableUserObject() {
		return setUserObjectMethod != null;
	}
	*/	
	public int size() {		try {			//Object retVal = getChildCountMethod.invoke(targetObject, nullArgs);
			Object retVal = MethodInvocationManager.invokeMethod(targetObject,getChildCountMethod, nullArgs);
			int retInt = ((Integer) retVal).intValue();
			if (retInt > 0)
			    return ((Integer) retVal).intValue();
			return retInt;
			
		} catch (Exception e) {			return -1;
		}	}	
	public Object elementAt(int i) {
		try {			
			Object[] args = {new Integer(i)};			//Object element = getChildAtMethod.invoke(targetObject, args);
			Object element = MethodInvocationManager.invokeMethod(targetObject,getChildAtMethod, args);
			/*
			Object userObject = getUserObject(element);
			if (userObject != null) 
				return userObject;
			else 
			*/
				return element;
		} catch (Exception e) {			return null;
		}		
	}
		
	public void setElementAt(Object newVal, int pos) {
		try {
			/*
			Object[] args = {new Integer(pos)};
			Object element = getChildAtMethod.invoke(targetObject, args);
			setUserObject(element, newVal);
			*/
		} catch (Exception e) {			System.err.println("setElementAt " + e);
		}
	}
		
	public ClassProxy addableElementType () {
		return  targetClass.objectClass();	}
	
	public boolean hasDeleteChildMethod() {
		return true;
	}
	public boolean hasSetChildMethod() {
		return true;
	}
	public boolean hasAddChildMethod() {
		return true;
	}
	public boolean hasInsertChildMethod() {
		return true;
	}
	public boolean isEditable (int index) {
		return true;
	}
	
	public void setElementAt(Object element, int pos, CommandListener commandListener) {
		setElementAt(element, pos);
		
	}
	public void removeElementAt(int index, CommandListener commandListener) {
		Object params[] = {new Integer(index)};
				//setElementAtMethod.invoke(parentObject, params);
				
				frame.getUndoer().execute (
							new VoidSubtractAddFirstCommand(//getUIFrame(),
							  		commandListener,
							  		removeMethod,
							  		targetObject,
							  		params,
							  		insertMethod,
							  		//readMethodParams
							  		getChildAtMethod
							));			
		
	}
	void addElementAt (Object element, int index, CommandListener commandListener) {
		Object params[] = {element, new Integer(index)};
			frame.getUndoer().execute (
							new AddSubtractFirstCommand(//getUIFrame(),
							  		commandListener,
									insertMethod,
							  		targetObject,
							  		params,
									removeMethod
							));		
	}
	public void removeElement(int index, Object element, CommandListener commandListener) {		removeElementAt(index, commandListener);
	}	public void addElement(Object element, CommandListener commandListener){
		addElementAt(element, size(), commandListener);	}
	public void insertElementAt (Object element, int pos, CommandListener commandListener) {
		addElement(element, commandListener);
		
	}
	public boolean validateRemoveElement(Object element) {
		return true;
	}
	public boolean validateRemoveElementAt(int index) {
		return true;
	}
	public boolean validateElementAt(int index) {
		return true;
	}
	public boolean validateAddElement(Object element) {
		return true;
	}
	public boolean validateSetElementAt(Object element, int pos) {
		return true;
	}
	public boolean validateInsertElementAt (Object element, int pos) {
		return true;
	}
	/*
	public Vector componentNames() {
		return new Vector();
	}
	*/
	/*
	public Object get (String componentName) {
		return null;
	}
	
	public Class componentType (String componentName) {return null;}
		
	public boolean isReadOnly (String componentName) {
		return false;		
	}
	public boolean preRead (String componentName) {
		return true;
	}
	public boolean preWrite (String componentName) {
		return true;		
	}
	public Object set (String componentName, Object value, CommandListener commandListener) {
		return null;		
	}
	public Object set (String componentName, Object value) {
		return null;		
	}
	*/
	public GenericTreeNodeToVectorStructure () {
	}

	@Override
	public boolean hasValidateInsertElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasValidateAddElement() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preAddElement() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preInsertElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preRemoveElement() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preRemoveElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preSetElementAt() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
