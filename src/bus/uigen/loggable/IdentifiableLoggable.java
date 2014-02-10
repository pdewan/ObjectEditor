package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;

import util.models.Refresher;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
import util.models.VectorMethodsListener;


public interface IdentifiableLoggable extends java.io.Serializable, Observer, Refresher,
//PropertyChangeListener, VectorListener, VectorMethodsListener {
AwarePropertyChangeListener, AwareVectorListener, AwareVectorMethodsListener,
AwareHashtableListener, TableModelListener, TreeModelListener {

	/*
	public void setObjectId(int newVal) {
		objectId = newVal;
	}
	public int getObjectId() {
		return objectId;
	}
	 */
	public void setObjectId(String newVal);

	public String getObjectId();

	public void addObjectId(String newVal);

	public void removeObjectId(String newVal);

	public Set getObjectIds();

	public void setObjectIds(Set newVal);

	public String getVirtualClass();

	public void setVirtualClass(String newVal);

	public void addPropertyChangeListener(PropertyChangeListener l);
	/*

	public void propertyChange(String hostId, PropertyChangeEvent evt);

	public void elementAdded(String hostId, Object source, Object element,
			int newSize);

	public void elementChanged(String hostId, Object source, Object element,
			int pos);

	public void elementInserted(String hostId, Object source, Object element,
			int pos, int newSize);

	public void elementRemoved(String hostId, Object source, int pos,
			int newSize);

	public void elementRemoved(String hostId, Object source, Object element,
			int newSize);

	public void elementsCleared(String hostId, Object source);

	public void updateVector(String hostId, VectorChangeEvent evt);
	*/

	public void addVectorListener(VectorListener vectorListener);

	public void removeVectorListener(VectorListener vectorListener);

	public void addVectorMethodsListener(VectorMethodsListener vectorListener);

	public void removeVectorMethodsListener(VectorMethodsListener vectorListener);
	/*

	public void keyPut(String hostId, Object source, Object key, Object value,
			int newSize);

	public void keyRemoved(String hostId, Object source, Object key, int newSize);

	public void update(Observable arg0, Object arg1);
	*/

}