package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Observer;

import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;

import util.models.HashtableListener;
import util.models.Refresher;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
import util.models.VectorMethodsListener;


public interface ProgramComponent extends
Observer, Refresher, PropertyChangeListener, VectorListener, VectorMethodsListener,  
HashtableListener, TableModelListener, TreeModelListener {

	public void registerAsListener(Object object);

	//public  Object invokeMethod (String hostId, int parentObjectId,
	public Object invokeMethod(String hostId, int nextId,
			IdentifiableLoggable parentRemoteLoggable,
			//String parentObjectId,
			String methodId, Object[] parameterValues)
			throws InvocationTargetException, IllegalAccessException,
			InstantiationException;

	public void setUIComponent(RemoteUIComponent newVal);

	public RemoteUIComponent getUIComponent();

	public void propertyChange(PropertyChangeEvent evt);

	/*
	public void update (Observable o, Object arg) {
		int objectId = LoggableRegistry.getObjectId(o);
		//implicitRefresh();	
	 	//refreshEditedObject();
	 }
	 */
	public void updateVector(VectorChangeEvent evt);

	public void elementAdded(Object source, Object element, int newSize);

	public void elementChanged(Object source, Object element, int pos);

	public void elementInserted(Object source, Object element, int pos,
			int newSize);

	public void elementRemoved(Object source, int pos, int newSize);

	public void elementRemoved(Object source, Object element, int newSize, int pos);

	public void elementsCleared(Object source);

	/*
	boolean isLocal = true;
	public boolean isLocal() {
		return isLocal;
	}
	public void setIsLocal(boolean newVal) {
		isLocal = newVal;
	}
	 */

	public void setCurrentHostId(String newVal);

	public void keyPut(Object source, Object key, Object value, int newSize);

	public void keyRemoved(Object source, Object key, int newSize);

}