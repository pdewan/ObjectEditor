package bus.uigen;import java.util.*;
//import bus.uigen.introspect.uiBean;
import bus.uigen.introspect.IntrospectUtility;import util.models.*;
public class OriginalHashtableChangeSupport implements java.io.Serializable{
	Vector listeners = new Vector();
	Object changeable;
	Hashtable changeableCopy;
	
	public OriginalHashtableChangeSupport(Object o) {
		changeable = o;
		//changeableCopy = new Hashtable (uiBean.toHashtable(o));		changeableCopy = IntrospectUtility.toHashtable(o);		
	}
	
	public OriginalHashtableChangeSupport(){	
	}
	
	public void addHashtableListener(HashtableListener hashtableListener) {
		if (listeners.contains(hashtableListener)) return;
		listeners.addElement(hashtableListener);
	}
	
	public void removeHashtableListener(HashtableListener hashtableListener) {
		listeners.removeElement(hashtableListener);
	}
	
	public void keyPut(Object key, Object value){
		//System.out.println("putting key:" + key + " for value:" + value);
		changeableCopy.put(key, value);
		//System.out.println("size" + changeableCopy.size());
		for (int i = 0; i < listeners.size(); i++ ){
			((HashtableListener) listeners.elementAt(i)).keyPut(changeable, key, value, changeableCopy.size());
		}
	}
	
	public void keyRemoved(Object key){
		//System.out.println("removing key:" + key);
		changeableCopy.remove(key);
		//System.out.println("size" + changeableCopy.size());
		for (int i = 0; i < listeners.size(); i++ ){
			((HashtableListener) listeners.elementAt(i)).keyRemoved(changeable, key, changeableCopy.size());
		}
	}
	
	int getSize() {
		return changeableCopy.size();
	}
	
	/*
	public void valueChanged(Object key) {
		for (int i = 0; i < listeners.size(); i++ ){
			((HashtableListener) listeners.elementAt(i)).valueChanged(key);
		}
	}
	*/
}
