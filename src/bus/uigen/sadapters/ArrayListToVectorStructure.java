package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;
import bus.uigen.editors.*;import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class ArrayListToVectorStructure extends  GenericListToVectorStructure   {	
	//Vector targetVector;
	public ArrayListToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		super(theGVectorObject, theFrame );		//targetVector = (Vector) targetObject;
	}	public ArrayListToVectorStructure () {		
	}	List targetList() {
		return (List) targetObject;	}
	
	public int size() {
//		if (targetObject instanceof Vector)
        if (targetClass.equals(IntrospectUtility.arrayListClass()))
		return targetList().size();
		else
			return super.size();	}
	/*	public  Enumeration elements() {
		if (targetObject instanceof Vector)
		return targetVector().elements();
		else
		return null;
	}
	*/
	
	public Object elementAt(int i) {
//		if (targetObject instanceof Vector)
		if (targetClass.equals(IntrospectUtility.arrayListClass()))
		return targetList().get(i);
		else
			return super.elementAt(i);
	}	
	boolean undoableRemoveElementAt() {
		return true;
	}
	boolean undoableInsertElementAt() {
		return true;
	}
		
	
}
