package bus.uigen.sadapters;
import java.util.*;import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import bus.uigen.editors.*;import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericVectorWithUserObjectToVectorStructure extends GenericVectorToVectorStructure implements  VectorStructure  {
	/*	transient VirtualMethod setElementsMethod = null;		transient VirtualMethod elementsMethod = null;	transient VirtualMethod addElementMethod = null;		transient VirtualMethod insertElementAtMethod = null; 	transient VirtualMethod removeElementMethod = null;	 	transient VirtualMethod removeElementAtMethod = null;
	transient VirtualMethod elementAtMethod = null ;	transient VirtualMethod sizeMethod = null;
	transient VirtualMethod isEmptyMethod = null;	transient VirtualMethod setElementAtMethod = null;
	transient VirtualMethod indexOfMethod = null;
	transient VirtualMethod validateSetElementAtMethod = null;
	transient VirtualMethod validateAddElementMethod = null;	
	transient VirtualMethod validateInsertElementAtMethod = null; 
	transient VirtualMethod validateRemoveElementMethod = null;	 
	transient VirtualMethod validateRemoveElementAtMethod = null;
	*/	/*	Class targetClass;
	Object targetObject;	uiFrame frame;	*/
	public GenericVectorWithUserObjectToVectorStructure (Object theGVectorObject, uiFrame theFrame) {		//init(theGVectorObject, theFrame );
		super (theGVectorObject, theFrame);
	}	public GenericVectorWithUserObjectToVectorStructure () {
	}
	/*
	int sizeFromTargetElements() {
		Enumeration elements = elementsFromTargetElements();
			if (elements == null) return -1;
			else {
				int retVal = 0;
				elements.nextElement();
				while (elements.hasMoreElements()) {
					elements.nextElement();
					retVal++;
				}
				return retVal;
			}
		
	}
	*/
	public int size() {
		return super.size() - 1;	}		
	public Object elementAt(int i) {
		return super.elementAt(i + 1);
	}
	/*
	public Object elementAtFromTargetElements(int i) {
		
			Enumeration elements = elementsFromTargetElements();			if (elements == null) return null;						else {
				elements.nextElement(); // go past user object
				Object retVal = null;
				for (int j = 0; elements.hasMoreElements()&& j <= i;j++ ) {					retVal = elements.nextElement();
				}				
				return retVal;			}
	}	*/
	
	public void setElementAt(Object element, int pos) {
		super.setElementAt(element, pos + 1);
	}	
	
	public void insertElementAt (Object element,  int pos, CommandListener commandListener) {
		super.insertElementAt(element, pos + 1, commandListener);		
	}	
	
	public void removeElementAt(int index, CommandListener commandListener) {
		super.removeElement(index + 1,  commandListener);		
	}
	
	public void removeElement(int index, Object element, CommandListener commandListener) {
		super.removeElement(index + 1, element, commandListener);
		
	}	
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
