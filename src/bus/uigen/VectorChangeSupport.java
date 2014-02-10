package bus.uigen;import java.util.Vector;import bus.uigen.introspect.IntrospectUtility;import util.models.AMutableString;import util.models.AVector;import util.models.VectorChangeEvent;import util.models.VectorInterface;import util.models.VectorListener;
//import bus.uigen.uiBean;//import bus.uigen.introspect.uiBean;
import util.models.VectorMethodsListener;
public class VectorChangeSupport implements java.io.Serializable{
	Vector listeners = new Vector();
	transient Vector transientListeners = new Vector();
	Vector methodsListeners = new Vector();
	transient Vector transientMethodsListeners = new Vector();
	Object changeable;
	VectorInterface changeableCopy;
	public VectorChangeSupport(Object o) {
		changeable = o;
		if (o instanceof String)
			changeableCopy = new AMutableString((String) o);
		else
		    changeableCopy = new AVector (IntrospectUtility.toClassVector(o));
	}
	
	public void initTransients(){
		if (transientListeners == null) transientListeners = new Vector();
		if (transientMethodsListeners == null) transientMethodsListeners = new Vector();
	}
	
	public VectorChangeSupport() {
		/*
		System.out.println("CONSTRUCTOR!!");
		listeners = new Vector();
		methodsListeners = new Vector();
		*/
	}
	public void addVectorListener(VectorListener vectorListener) {
		//if (listeners == null) listeners = new Vector();
		initTransients();
		if (vectorListener instanceof java.io.Serializable){
			if (listeners.contains(vectorListener)) return;
			listeners.addElement(vectorListener);
		}
		else{
			if (transientListeners.contains(vectorListener)) return;
			transientListeners.addElement(vectorListener);
		}
	}
	public void removeVectorListener(VectorListener vectorListener) {
		initTransients();
		listeners.removeElement(vectorListener);
		transientListeners.removeElement(vectorListener);
	}
	
	public void addVectorMethodsListener(VectorMethodsListener vectorListener) {
		//if (methodsListeners == null) methodsListeners = new Vector();
		initTransients();
		if (vectorListener instanceof java.io.Serializable){
			if (methodsListeners.contains(vectorListener)) return;
			methodsListeners.addElement(vectorListener);
		}
		else{
			if (transientMethodsListeners.contains(vectorListener)) return;
			transientMethodsListeners.addElement(vectorListener);
		}
	}
	public void removeVectorMethodsListener(VectorMethodsListener vectorListener) {
		initTransients();
		methodsListeners.removeElement(vectorListener);
		transientMethodsListeners.removeElement(vectorListener);
	}
	
	public void elementAdded(Object element) {
		//System.out.println("adding element" + element);
		//System.out.println("size" + changeableCopy.size() + 1);
		initTransients();
		for (int i = 0; i < listeners.size(); i++ )			try {
			((VectorListener) listeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.AddComponentEvent,
								  changeableCopy.size(),null, element, changeableCopy.size() + 1));		     } catch (Exception e) {		    	 e.printStackTrace();		     }
		for (int i = 0; i < transientListeners.size(); i++ )			try {
			((VectorListener) transientListeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.AddComponentEvent,
								  changeableCopy.size(),null, element, changeableCopy.size() + 1));			} catch (Exception e) {		    	 e.printStackTrace();		     }
		changeableCopy.addElement(element);
		notifyElementAdded(element);
		
	}
	void notifyElementAdded(Object element) {
		//if (methodsListeners == null) methodsListeners = new Vector();
		initTransients();
		for (int i = 0; i < methodsListeners.size(); i++ ) {
			((VectorMethodsListener) methodsListeners.elementAt(i)).elementAdded(changeable, element, changeableCopy.size());
		}
		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {
			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementAdded(changeable, element, changeableCopy.size());
		}
	}
	void notifyAboutExistingElements(VectorMethodsListener methodsListener) {
		for (int i = 0; i < changeableCopy.size(); i++)
			methodsListener.elementAdded(changeable, changeableCopy.elementAt(i), i+1);
	}
	void notifyAboutExistingElements(VectorListener eventListener) {
		for (int i = 0; i < changeableCopy.size(); i++)
			eventListener.updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.AddComponentEvent,
								  i,null, changeableCopy.elementAt(i), i + 1));
			
	}
	public void elementInserted(Object element, int pos) {
		Object oldElement;
		initTransients();
		if (pos >= changeableCopy.size())
			oldElement = null;
		else
			oldElement = changeableCopy.elementAt(pos);
		for (int i = 0;i < listeners.size(); i++ )
			((VectorListener) listeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.InsertComponentEvent,
								  pos,
								  oldElement,
								  element, changeableCopy.size() + 1));
		for (int i = 0;i < transientListeners.size(); i++ )
			((VectorListener) transientListeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.InsertComponentEvent,
								  pos,
								  oldElement,
								  element, changeableCopy.size() + 1));
		changeableCopy.insertElementAt(element, pos);
		notifyElementInserted(element, pos);
		
	}
	int getSize() {
		return changeableCopy.size();
	}
	void notifyElementInserted(Object element, int pos) {
		initTransients();
		for (int i = 0; i < methodsListeners.size(); i++ ) {
			((VectorMethodsListener) methodsListeners.elementAt(i)).elementInserted(changeable, element, pos, getSize());
		}
		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {
			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementInserted(changeable, element, pos, getSize());
		}
	}
	public void elementChanged(Object element, int pos) {
		//System.out.println("elementChanged" + (listeners == null));
		Object oldElement;
		if (pos >= changeableCopy.size())
			oldElement = null;
		else
			oldElement = changeableCopy.elementAt(pos);
		for (int i = 0; i < listeners.size(); i++ )
			((VectorListener) listeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.ChangeComponentEvent,
								  pos,
								  oldElement,
								  element, changeableCopy.size()));
		initTransients();
		for (int i = 0; i < transientListeners.size(); i++ )
			((VectorListener) transientListeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.ChangeComponentEvent,
								  pos,
								  oldElement,
								  element, changeableCopy.size()));
		
//		System.out.println("in vectorchangesupport.elementchanged(), after for loop");
		
		changeableCopy.setElementAt(element, pos);
//		System.out.println("after changeablecopy");
		notifyElementChanged(element, pos);
//		System.out.println("after notifyelementchanged");
		
	}
	void notifyElementChanged(Object element, int pos) {
		initTransients();
		for (int i = 0; i < methodsListeners.size(); i++ ) {
			((VectorMethodsListener) methodsListeners.elementAt(i)).elementChanged(changeable, element, pos);
		}
		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {
			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementChanged(changeable, element, pos);
		}
	}
	public void elementRemoved(int pos) {		try {
		initTransients();
		Object oldElement;
		if (pos >= changeableCopy.size())
			oldElement = null;
		else
			oldElement = changeableCopy.elementAt(pos);
		for (int i = 0; i < listeners.size(); i++ )
			((VectorListener) listeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.DeleteComponentEvent,
								  pos, oldElement, null, changeableCopy.size() - 1));
		for (int i = 0; i < transientListeners.size(); i++ )
			((VectorListener) transientListeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.DeleteComponentEvent,
								  pos, oldElement, null, changeableCopy.size() - 1));
		changeableCopy.removeElementAt(pos);
		notifyElementRemoved(pos);		} catch (Exception e) {			System.out.println("Illegal remove index:" + pos + " in " + changeable);			e.printStackTrace();		}
		
	}
	void notifyElementRemoved(int pos) {
		initTransients();
		for (int i = 0; i < methodsListeners.size(); i++ ) {
			((VectorMethodsListener) methodsListeners.elementAt(i)).elementRemoved(changeable, pos, getSize());
		}
		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {
			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementRemoved(changeable, pos, getSize());
		}
	}
	public void elementRemoved(Object element) {
		initTransients();
		int pos = changeableCopy.indexOf(element);
		for (int i = 0; i < listeners.size(); i++)
			((VectorListener) listeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.DeleteComponentEvent,
								  pos, element, null, changeableCopy.size() - 1));
		for (int i = 0; i < transientListeners.size(); i++)
			((VectorListener) transientListeners.elementAt(i)).
							updateVector(new VectorChangeEvent
							     (changeable, VectorChangeEvent.DeleteComponentEvent,
								  pos, element, null, changeableCopy.size() - 1));
		changeableCopy.removeElement(element);
		notifyElementRemoved(element);
		
	}		public void elementsCleared() {		initTransients();		for (int i = 0; i < listeners.size(); i++)			((VectorListener) listeners.elementAt(i)).							updateVector(new VectorChangeEvent							     (changeable, VectorChangeEvent.ClearEvent,								  0, null, null, 0));		for (int i = 0; i < transientListeners.size(); i++)			((VectorListener) transientListeners.elementAt(i)).							updateVector(new VectorChangeEvent							     (changeable, VectorChangeEvent.ClearEvent,										  0, null, null, 0));;				changeableCopy.removeAllElements();		notifyElementsCleared();			}	
	void notifyElementRemoved(Object element) {
		initTransients();
		for (int i = 0; i < methodsListeners.size(); i++ ) {
			((VectorMethodsListener) methodsListeners.elementAt(i)).elementRemoved(changeable, element, getSize(), -1);
		}
		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {
			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementRemoved(changeable, element, getSize(), -1);
		}
	}	void notifyElementsCleared() {		initTransients();		for (int i = 0; i < methodsListeners.size(); i++ ) {			((VectorMethodsListener) methodsListeners.elementAt(i)).elementsCleared(changeable);		}		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementsCleared(changeable);		}	}		public void fireUpdateVector(VectorChangeEvent vectorEvent) {		initTransients();		for (int i = 0; i < listeners.size(); i++)			((VectorListener) listeners.elementAt(i)).							updateVector(vectorEvent);		for (int i = 0; i < transientListeners.size(); i++)			((VectorListener) transientListeners.elementAt(i)).							updateVector(vectorEvent);;			}		public void fireElementAdded(Object source, Object element, int newSize) {		// TODO Auto-generated method stub		initTransients();		for (int i = 0; i < methodsListeners.size(); i++ ) {			((VectorMethodsListener) methodsListeners.elementAt(i)).elementAdded(source, element, newSize);		}		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementAdded(source, element, newSize);		}			}		public void fireElementChanged(Object source, Object element, int pos) {		// TODO Auto-generated method stub		initTransients();		for (int i = 0; i < methodsListeners.size(); i++ ) {			((VectorMethodsListener) methodsListeners.elementAt(i)).elementChanged(source, element, pos);		}		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementChanged(source, element, pos);		}					}		public void fireElementInserted(Object source, Object element, int pos,			int newSize) {		initTransients();		for (int i = 0; i < methodsListeners.size(); i++ ) {			((VectorMethodsListener) methodsListeners.elementAt(i)).elementInserted(source, element, pos, newSize);		}		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementInserted(source, element, pos, newSize);		}			}		public void fireElementRemoved(Object source, int pos, int newSize) {		initTransients();		for (int i = 0; i < methodsListeners.size(); i++ ) {			((VectorMethodsListener) methodsListeners.elementAt(i)).elementRemoved(source, pos, newSize);		}		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementRemoved(source, pos, newSize);		}			}		public void fireElementRemoved(Object source, Object element, int newSize) {		initTransients();		for (int i = 0; i < methodsListeners.size(); i++ ) {			((VectorMethodsListener) methodsListeners.elementAt(i)).elementRemoved(source, element, newSize, -1);		}		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementRemoved(source, element, newSize, -1);		}			}		public void fireElementsCleared(Object source) {		initTransients();		for (int i = 0; i < methodsListeners.size(); i++ ) {			((VectorMethodsListener) methodsListeners.elementAt(i)).elementsCleared(source);		}		for (int i = 0; i < transientMethodsListeners.size(); i++ ) {			((VectorMethodsListener) transientMethodsListeners.elementAt(i)).elementsCleared(source);		}			}

}
