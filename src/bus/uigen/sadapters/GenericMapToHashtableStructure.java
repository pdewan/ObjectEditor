package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;

import util.annotations.StructurePatternNames;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericMapToHashtableStructure extends  AbstractHashtableToHashtableStructure  {
	
	public GenericMapToHashtableStructure (Object theTargetObject, uiFrame theFrame) {		super(theTargetObject, theFrame );
	}	public GenericMapToHashtableStructure () {		
	}
	
	public  MethodProxy getKeysMethod(ClassProxy objectClass) {		//return uiBean.getKeysMethod(objectClass);		ClassProxy[] paramTypes = {};		return IntrospectUtility.getMethod(objectClass, "keySet", objectClass.setClass(), paramTypes);
	}
	
	public  MethodProxy getElementsMethod(ClassProxy objectClass) {		//return uiBean.getElementsMethod(objectClass);		
		ClassProxy[] paramTypes = {};		return IntrospectUtility.getMethod(objectClass, "values", objectClass.collectionClass(), paramTypes);
	}	
	public static Vector toVector(Iterator e) {
		Vector retVal = new Vector();
		while (e.hasNext())
			retVal.addElement(e.next());
		return retVal;	}
	public Vector keys() {
		//try {
		Object[] params = {};
		Iterator keys =  ((Set) MethodInvocationManager.invokeMethod(targetObject, keysMethod, params)).iterator();
		return toVector(keys);
		/*
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		*/
	}
	public Vector elements() {
		//try {
		Object[] params = {};
		Iterator elements = ((Collection) MethodInvocationManager.invokeMethod(targetObject, elementsMethod, params)).iterator();
		return toVector(elements);
		/*
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		*/
	}	
	public String getPatternName() {
		return StructurePatternNames.MAP_PATTERN;		
	}
	
}
