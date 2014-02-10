package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Observer;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeModelListener;

import util.models.Refresher;
import util.models.VectorChangeEvent;

import bus.uigen.reflect.MethodProxy;

public interface UIComponent extends Observer, Refresher, AwarePropertyChangeListener, 
AwareVectorListener,
AwareVectorMethodsListener, AwareHashtableListener, TreeModelListener, RemoteUIComponent 
 {

	public Object invokeMethod(ACompositeLoggable parentObject,
			MethodProxy method, Object[] parameterValues)
			throws InvocationTargetException, IllegalAccessException,
			InstantiationException;

}