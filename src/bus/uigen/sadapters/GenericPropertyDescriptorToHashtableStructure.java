package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericPropertyDescriptorToHashtableStructure extends  GenericHashtableToHashtableStructure  {
	
	public GenericPropertyDescriptorToHashtableStructure (Object theTargetObject, uiFrame theFrame) {		super(theTargetObject, theFrame );
	}	public GenericPropertyDescriptorToHashtableStructure () {		
	}
	
	public  MethodProxy getKeysMethod(ClassProxy objectClass) {		//return uiBean.getKeysMethod(objectClass);		ClassProxy[] paramTypes = {};		return IntrospectUtility.getMethod(objectClass, "attributeNames", objectClass.enumerationClass(), paramTypes);
	}
	public  MethodProxy getGetMethod(ClassProxy objectClass) {
		ClassProxy[] paramTypes = {objectClass.objectClass()};
		return IntrospectUtility.getMethod(objectClass, "getValue", objectClass.objectClass(), paramTypes);
		//return uiBean.getMethod(objectClass);
	}
	public  MethodProxy getPutMethod(ClassProxy objectClass, MethodProxy getMethod) {
		ClassProxy[] paramTypes = {keyType(getMethod), elementType(getMethod)};
		return IntrospectUtility.getMethod(objectClass, "setValue", null, paramTypes);
	}
	public  MethodProxy getElementsMethod(ClassProxy objectClass) {		//return uiBean.getElementsMethod(objectClass);	
		return null;
		/*
		Class[] paramTypes = {};		return uiBean.getMethod(objectClass, "values", Collection.class, paramTypes);
		*/
	}	/*
	public static Vector toVector(Iterator e) {
		Vector retVal = new Vector();
		while (e.hasNext())
			retVal.addElement(e.next());
		return retVal;	}
	/*
	
	public Vector keys() {
		//try {
		Object[] params = {};
		Iterator keys =  ((Set) uiMethodInvocationManager.invokeMethod(targetObject, keysMethod, params)).iterator();
		return toVector(keys);
		
	}
	
	public Vector elements() {
		//try {
		Object[] params = {};
		Iterator elements = ((Collection) uiMethodInvocationManager.invokeMethod(targetObject, elementsMethod, params)).iterator();
		return toVector(elements);
		
	}	
	*/
	
	
}
