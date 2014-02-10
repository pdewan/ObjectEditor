package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import util.models.HashtableChangeSupport;
//import bus.uigen.HashtablePatternChangeSupport;
import util.models.Refresher;
import util.models.VectorChangeEvent;
import util.models.VectorChangeSupport;
//import bus.uigen.VectorPatternChangeSupport;
import util.models.VectorListener;
import util.models.VectorMethodsListener;


public class AnIdentifiableLoggable extends Observable 
	implements
	/*
	java.io.Serializable, Observer,
	//PropertyChangeListener, VectorListener, VectorMethodsListener {
	AwarePropertyChangeListener, AwareVectorListener, AwareVectorMethodsListener,
	AwareHashtableListener,
	*/ 
	IdentifiableLoggable {
	//static int currentId = 0;
	//int objectId;
	String objectId;
	String virtualClass;
	Set objectIds = new HashSet();
	public AnIdentifiableLoggable(int id) {
		//setObjectId(id);
	}
	public AnIdentifiableLoggable() {
		//setObjectId(currentId++);
	}
	/*
	// need this as a local cache for equal method
	Object realObject;
	public void setRealObject(Object newVal) {
		realObject = newVal;
	}
	public Object getRealObject() {
		return realObject;
	}
	*/
	
	/*
	public void setObjectId(int newVal) {
		objectId = newVal;
	}
	public int getObjectId() {
		return objectId;
	}
	*/
	public void setObjectId(String newVal) {
		objectId = newVal;
	}
	public String getObjectId() {
		return objectId;
	}
	public void addObjectId(String newVal) {
		objectIds.add(newVal);
	}
	public void removeObjectId(String newVal) {
		objectIds.remove(newVal);
	}
	public Set getObjectIds() {
		return objectIds;
	}
	public void setObjectIds(Set newVal) {
		objectIds = newVal;
	}
	public String getVirtualClass() {
		return virtualClass;
	}	
	public void setVirtualClass (String newVal) {
		 virtualClass = newVal;
	}
	transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this) ;
	@util.annotations.ObserverRegisterer(util.annotations.ObserverTypes.PROPERTY_LISTENER)
	public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
        //uiObjectAdapter.maybeAddPropertyChangeListener(getRealObject(), this);
    }
	
	public synchronized void addObserver(Observer arg0) {
		super.addObserver(arg0);
	}
	
	@Override
	public void propertyChange(String hostId, PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		propertyChangeSupport.firePropertyChange(evt);
		
	}
	@Override
	public void elementAdded(String hostId, Object source, Object element, int newSize) {
		vectorChangeSupport.fireElementAdded(source, element, newSize);
		
	}

	@Override
	public void elementChanged(String hostId, Object source, Object element, int pos) {
		vectorChangeSupport.fireElementChanged(source, element, pos);
		
	}

	@Override
	public void elementInserted(String hostId, Object source, Object element, int pos,
			int newSize) {
		vectorChangeSupport.fireElementInserted(source, element, pos, newSize);
		
	}

	@Override
	public void elementRemoved(String hostId, Object source, int pos, int newSize) {
		
		vectorChangeSupport.fireElementRemoved(source, pos, newSize);
	}

	@Override
	public void elementRemoved(String hostId, Object source, Object element, int newSize) {
		vectorChangeSupport.fireElementRemoved(source, element, newSize);
		
	}

	@Override
	public void elementsCleared(String hostId, Object source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateVector(String hostId, VectorChangeEvent evt) {
		vectorChangeSupport.fireUpdateVector(evt);
		
	}
	//transient VectorPatternChangeSupport vectorChangeSupport = new VectorPatternChangeSupport(this);
	transient VectorChangeSupport vectorChangeSupport = new VectorChangeSupport();

	public void addVectorListener(VectorListener vectorListener) {
		vectorChangeSupport.addVectorListener(vectorListener);
	}
	public void removeVectorListener(VectorListener vectorListener) {
		vectorChangeSupport.removeVectorListener(vectorListener);
	}
	public void addVectorMethodsListener(VectorMethodsListener vectorListener) {
		vectorChangeSupport.addVectorMethodsListener(vectorListener);
	}
	public void removeVectorMethodsListener(VectorMethodsListener vectorListener) {
		vectorChangeSupport.removeVectorMethodsListener(vectorListener);
	}
	//transient HashtablePatternChangeSupport hashtableChangeSupport = new HashtablePatternChangeSupport(this);
	transient HashtableChangeSupport hashtableChangeSupport = new HashtableChangeSupport();
	@Override
	public void keyPut(String hostId, Object source, Object key, Object value,
			int newSize) {
		hashtableChangeSupport.keyPut(key, value);		
	}
	@Override
	public void keyRemoved(String hostId, Object source, Object key, int newSize) {
		hashtableChangeSupport.keyRemoved(key);
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		setChanged();
		notifyObservers(arg1);
		
	}
	transient Vector<Refresher> refreshers = new Vector() ;
	@util.annotations.ObserverRegisterer(util.annotations.ObserverTypes.REFRESHER)
	public void addRefresher(Refresher l) {
        refreshers.add(l);
        //uiObjectAdapter.maybeAddPropertyChangeListener(getRealObject(), this);
    }
	@Override
	public void refresh(Object o) {
		notifyRefreshers();
		
	}
	void notifyRefreshers() {
		for(Refresher refresher:refreshers)
			refresher.refresh(this);
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		notifyTableModelListeners(e);
		
	}
	Vector<TableModelListener> tableModelListeners = new Vector();
	public void notifyTableModelListeners(TableModelEvent e) {
		for (int i = 0; i < tableModelListeners.size(); i++ ) {
			tableModelListeners.elementAt(i).tableChanged(e);
		}
	}
	public void addTableModelListener(TableModelListener theTableModelListener) {
		tableModelListeners.add(theTableModelListener);		
	}
	Vector<TreeModelListener> treeModelListeners = new Vector();
	
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		for (int i = 0; i < treeModelListeners.size(); i++ ) {
			treeModelListeners.elementAt(i).treeNodesChanged(e);
		}
		
	}
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		for (int i = 0; i < treeModelListeners.size(); i++ ) {
			treeModelListeners.elementAt(i).treeNodesInserted(e);
		}
		
	}
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		for (int i = 0; i < treeModelListeners.size(); i++ ) {
			treeModelListeners.elementAt(i).treeNodesRemoved(e);
		}
		
	}
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		for (int i = 0; i < treeModelListeners.size(); i++ ) {
			treeModelListeners.elementAt(i).treeStructureChanged(e);
		}		
	}

}
