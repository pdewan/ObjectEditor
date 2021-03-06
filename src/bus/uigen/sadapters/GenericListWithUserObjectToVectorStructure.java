package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;
import bus.uigen.controller.MethodInvocationManager;public class GenericListWithUserObjectToVectorStructure extends  GenericListToVectorStructure  {
		public GenericListWithUserObjectToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		super(theGVectorObject, theFrame );
	}
	public GenericListWithUserObjectToVectorStructure () {		
	}	
	public int size() {
		return super.size() - 1;
	}
	
	
	public Object elementAt(int i) {
		return super.elementAt(i + 1);
	}
	
	
	public void setElementAt(Object element, int pos) {
		super.setElementAt(element, pos + 1);
	}
		
	public void insertElementAt (Object element,  int pos, CommandListener commandListener) {
		super.insertElementAt(element, pos + 1, commandListener);		
	}
		
	public void removeElementAt(int index, CommandListener commandListener) {
		super.removeElementAt(index + 1,  commandListener);		
	}
	/*
	public void removeElement(int index, Object element, CommandListener commandListener) {
		super.removeElement(index + 1, element, commandListener);		
	}
	*/
	
	public void setElementAt(Object element, int pos, CommandListener commandListener) {
		super.setElementAt(element, pos + 1, commandListener);		
		
	}
	public boolean hasUserObject() {
		return true;
	}
	public Object getUserObject() {		
		return super.elementAt(0);
	}
	public void setUserObject(Object newVal) {
		super.setElementAt(newVal, 0);
	}
	
}
