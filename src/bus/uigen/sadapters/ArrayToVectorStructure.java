package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;

import util.trace.Tracer;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class ArrayToVectorStructure extends BeanToRecord implements  VectorStructure  {
		
	public ArrayToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		init(theGVectorObject, theFrame );
	}	public ArrayToVectorStructure () {
	}	
	public void setMethods(Class objectClass) {		
	}
	
	
	public int size() {		try {			return Array.getLength(targetObject);
		} catch (Exception e) {			return -1;
		}	}	
	public Object elementAt(int i) {
		try {			return Array.get(targetObject, i);
		} catch (Exception e) {			return null;
		}
		
	}
		
	public void setElementAt(Object element, int pos) {
		try {			 Array.set(targetObject, pos, element);
		} catch (Exception e) {			System.err.println("setElementAt " + e);
		}
	}
		
	public ClassProxy addableElementType () {
		return null;	}
	
	public boolean hasDeleteChildMethod() {
		return false;
	}
	public boolean hasInsertChildMethod() {
		return false;
	}
	public boolean hasAddChildMethod() {
		return false;
	}
	public boolean hasSetChildMethod() {
		return true;
	}
	public void clear() {
		Tracer.error("Cannot clear an array");
	}
	public void clear(CommandListener commandListener) {
		clear();
	}
	
	public void setElementAt(Object element, int pos, CommandListener commandListener) {
		setElementAt(element, pos);
		
	}
	public void removeElement(int index, Object element, CommandListener commandListener) {		System.err.println("Array remove element should not have been called");
	}
	public void removeElementAt(int index, CommandListener commandListener) {
		System.err.println("Array remove element at should not have been called");
	}	public void addElement(Object element, CommandListener commandListener){
		System.err.println("Array add element should not have been called");	}
	public void insertElementAt (Object element, int pos, CommandListener commandListener) {
		addElement(element, commandListener);
		
	}
	public boolean isEditable (int index) {
		return true;
	}
	public boolean validateRemoveElement(Object element) {
		return true;
	}
	public boolean validateRemoveElementAt(int index) {
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
	public static String ARRAY = "Array";
	public String programmingPatternKeyword() {
		return  "";
	}
	public String typeKeyword() {
		return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + ARRAY;
	}
	@Override
	public boolean hasClearMethod() {
		// TODO Auto-generated method stub
		return false;
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
	public boolean validateElementAt(int index) {
		// TODO Auto-generated method stub
		return true;
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
		return true;
	}
	
}
