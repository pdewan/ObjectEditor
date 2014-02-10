package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;
import bus.uigen.editors.*;import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class ArrayWithUserObjectToVectorStructure extends ArrayToVectorStructure implements  VectorStructure  {
		
	public ArrayWithUserObjectToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		
		//init(theGVectorObject, theFrame );
		super (theGVectorObject, theFrame );
	}
		public ArrayWithUserObjectToVectorStructure () {
	}	
	
	
	public int size() {
		return super.size() - 1;	}	
	public Object elementAt(int i) {
		return super.elementAt (i + 1);
		
	}
		
	public void setElementAt(Object element, int pos) {
		super.setElementAt(element, pos + 1);
	}
		
	public void setElementAt(Object element, int pos, CommandListener commandListener) {
		super.setElementAt(element, pos+1);
		
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
