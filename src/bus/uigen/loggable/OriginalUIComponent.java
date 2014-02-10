package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Observer;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeModelListener;

import util.models.VectorChangeEvent;

import bus.uigen.reflect.MethodProxy;

public interface OriginalUIComponent extends Observer, AwarePropertyChangeListener, 
AwareVectorListener,
AwareVectorMethodsListener, AwareHashtableListener, TreeModelListener 
 {

	public Object invokeMethod(ACompositeLoggable parentObject,
			MethodProxy method, Object[] parameterValues)
			throws InvocationTargetException, IllegalAccessException,
			InstantiationException;

	public void setProgramComponent(ProgramComponent newVal);

	public ProgramComponent getProgramComponent();

	public void propertyChange(String hostId, PropertyChangeEvent evt);

	public void updateVector(String hostId, VectorChangeEvent evt);

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

	public void keyPut(String hostId, Object source, Object key, Object value,
			int newSize);

	public void keyRemoved(String hostId, Object source, Object key, int newSize);
	public void tableChanged(LoggableTableModelEvent e);

}