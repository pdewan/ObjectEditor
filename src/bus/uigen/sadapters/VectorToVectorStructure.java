package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;

import util.models.AListenableVector;
import bus.uigen.editors.*;import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class VectorToVectorStructure extends  GenericVectorToVectorStructure   {	
	//Vector targetVector;
	public VectorToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		super(theGVectorObject, theFrame );		//targetVector = (Vector) targetObject;
	}	public VectorToVectorStructure () {		
	}	Vector targetVector() {
		return (Vector) targetObject;	}	
				
	
	public int size() {
//		if (targetObject instanceof Vector)
//        if (targetClass.equals(IntrospectUtility.vectorClass()))

		if (getTargetObject() instanceof AListenableVector) {
			return ((AListenableVector) getTargetObject()).internalSize();
		}		return targetVector().size();
//		else
//			return super.size();	}
	/*	public  Enumeration elements() {
		if (targetObject instanceof Vector)
		return targetVector().elements();
		else
		return null;
	}
	*/
	
	// write methods cannot ne called directly as reflection is needed to undo them
	public Object elementAt(int i) {
//		if (targetObject instanceof Vector)
//		if (targetClass.equals(IntrospectUtility.vectorClass()))
		return targetVector().elementAt(i);
//		else
//			return super.elementAt(i);
	}	
	boolean undoableRemoveElementAt() {
		return true;
	}
	boolean undoableInsertElementAt() {
		return true;
	}
		
	
}
