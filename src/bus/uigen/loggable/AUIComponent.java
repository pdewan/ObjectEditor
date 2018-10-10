package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.table.TableModel;

//import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
import util.models.VectorMethodsListener;
import util.trace.Tracer;

import bus.uigen.reflect.MethodProxy;

public class AUIComponent
		// implements PropertyChangeListener, VectorListener,
		// VectorMethodsListener{
		extends Observable
		implements 
		/*
		AwarePropertyChangeListener, AwareVectorListener,
		AwareVectorMethodsListener, AwareHashtableListener, 
		Observer,
		*/
		UIComponent {
	ProgramComponent programComponent;
	static boolean processingEvent = false;
	

	static boolean isProcessingEvent() {
		return processingEvent;
	}

	static void setProcessingEvent(boolean processingEvent) {
		AUIComponent.processingEvent = processingEvent;
	}
	
	static Set objectsReceived = new HashSet();
	
	static void finishedProcessingEvent() {
		setProcessingEvent(false);
		objectsReceived.clear();
	}
	static void addReceivedObject(Object newValue) {
		if (newValue != null)
			objectsReceived.add(newValue);
	}
	
	Object invokeMethodLocally (ACompositeLoggable parentObject,
			MethodProxy method, Object[] parameterValues) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		
		return method.invoke(parentObject.getRealObject(), LoggableRegistry.getRealObjects(parameterValues));
		
	}
	
	  boolean isLocallyInvocable(ACompositeLoggable parentObject,
			MethodProxy method) {
		return objectsReceived.contains(parentObject) &&
			LoggableRegistry.isMethodReadOnly(method) &&
			LoggableRegistry.isPrimitive(method.getReturnType()) &&
			LoggableRegistry.isPrimitive(method.getParameterTypes());
		
	}

	public Object invokeMethod(ACompositeLoggable parentObject,
			MethodProxy method, Object[] parameterValues) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		// int objectId = parentObject.getObjectId();
		// String objectId = parentObject.getObjectId();
		/*
		if (LoggableRegistry.isMethodReadOnly(method)) {
			 System.out.println("Readonly: " + method.getName());
			 if (objectsReceived.contains(parentObject) && 
					 LoggableRegistry.isPrimitive(method.getReturnType()))
					 System.out.println("short circuit operation");
					 
				 
			 
		}
		*/
		if (isLocallyInvocable(parentObject, method)) {
			return invokeMethodLocally(parentObject, method, parameterValues);
		}
		IdentifiableLoggable identifiableLoggable = LoggableRegistry
				.removeRealObject(parentObject);
		// int methodId = LoggableRegistry.getMethodIntId(method);
		String methodId = LoggableRegistry.getMethodStringId(method);
		// return getProgramComponent().invokeMethod(objectId, methodId,
		// parameterValues);
		Object retVal = getProgramComponent().invokeMethod(
				LoggableRegistry.getHostId(), LoggableRegistry.nextId,
				identifiableLoggable,
				// objectId,
				methodId, loggableParameters(parameterValues));
//		if (LoggableRegistry.methodReturnsValue(methodId))
//			return retVal;
//		return AUIComponent.remoteToLocalLoggableModel(retVal);
		if (!(retVal instanceof AnIdentifiableLoggable))
			return retVal;
//		if (LoggableRegistry.methodReturnsValue(methodId))
//			System.out.println( "method returns value" );
		retVal = AUIComponent.remoteToLocalLoggableModel(retVal);
//		if (retVal instanceof AnIdentifiableLoggable &&
//				!(retVal instanceof ACompositeLoggable) ) 
//			System.out.println( "AUIComponent::invokeMethod" );
		return retVal;
	}

	 static Object[] loggableParameters(Object[] actualParameters) {
		Object[] loggableParameters = new Object[actualParameters.length];
		for (int i = 0; i < actualParameters.length; i++) {
			loggableParameters[i] = getRemoteLoggableModel(actualParameters[i]);
		}
		return loggableParameters;
	}

	static Object getRemoteLoggableModel(Object realObject) {
		// uiObjectAdapter adapter = ObjectEditor.toObjectAdapter(realObject);
		if (realObject == null)
			return null;
		Object retVal = LoggableRegistry.getExistingLoggableModel(realObject);
		if (retVal != null) {
			if (!(retVal instanceof ACompositeLoggable))
				return retVal;

			return LoggableRegistry
					.removeRealObject((ACompositeLoggable) retVal);
			// return retVal;
		}
		return AUIComponent.createRemoteLoggableModel(realObject);
		// return createCompositeLoggableModel(realObject);
	}

	public void setProgramComponent(ProgramComponent newVal) {
		programComponent = newVal;
	}

	public ProgramComponent getProgramComponent() {
		return programComponent;
	}

	@Override
	public void propertyChange(String hostId, PropertyChangeEvent evt) {
		// int objectId = (Integer) evt.getSource();
		// String objectId = (String) evt.getSource();
		IdentifiableLoggable source = (IdentifiableLoggable) evt
				.getSource();
		// ACompositeLoggable loggableModel =
		// LoggableRegistry.getLoggableModel(objectId);
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
				.remoteToLocalLoggableModel(source);

		// LoggableRegistry.getLoggableModel(source.getObjectId());
		Object localOldValue = remoteToLocalLoggableModel(evt.getOldValue());
		Object localNewValue = remoteToLocalLoggableModel(evt.getNewValue());
		PropertyChangeEvent transformedEvent = new PropertyChangeEvent(
				loggableModel, 
				evt.getPropertyName(), 
				localOldValue,
				localNewValue
				//AUIComponent.remoteToLocalLoggableModel(evt.getOldValue()),
				//AUIComponent.remoteToLocalLoggableModel(evt.getNewValue())
				);
		addReceivedObject(localNewValue);
		addReceivedObject(localOldValue);
		loggableModel.propertyChange(hostId, transformedEvent);
		finishedProcessingEvent();
	}
	
	public void updateVector(String hostId, VectorChangeEvent evt) {
		
		// int objectId = (Integer) evt.getSource();
		// String objectId = (String) evt.getSource();
		IdentifiableLoggable source = (IdentifiableLoggable) evt
				.getSource();
		// ACompositeLoggable loggableModel =
		// LoggableRegistry.getLoggableModel(objectId);
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
				.remoteToLocalLoggableModel(source);

		// LoggableRegistry.getLoggableModel(source.getObjectId());
		Object localOldValue = remoteToLocalLoggableModel(evt.getOldValue());
		Object localNewValue = remoteToLocalLoggableModel(evt.getNewValue());
			
		VectorChangeEvent transformedEvent = new VectorChangeEvent(loggableModel, evt
				.getEventType(), evt.getPosition(), 
				localOldValue,
				//remoteToLocalLoggableModel(evt.getOldValue()),				
				//remoteToLocalLoggableModel(evt.getNewValue()), 
				localNewValue,
				evt.getNewSize());
		addReceivedObject(localNewValue);
		addReceivedObject(localOldValue);
		/*
		objectsReceived.add(localNewValue);
		objectsReceived.add(localOldValue);
		*/
		loggableModel.updateVector(hostId, transformedEvent);
		finishedProcessingEvent();
		/*
		int objectId = (Integer) evt.getSource();
		ACompositeLoggable loggableModel = LoggableRegistry
				.getLoggableModel(objectId);
		VectorChangeEvent transformedEvent = new VectorChangeEvent(loggableModel, evt
				.getEventType(), evt.getPosition(), evt.getOldValue(), evt
				.getNewValue(), evt.getNewSize());
		loggableModel.updateVector(hostId, evt);
		*/
	}

	@Override
	public void elementAdded(String hostId, Object source, Object element,
			int newSize) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		/*
		int objectId = (Integer) source;
		ACompositeLoggable loggableModel = LoggableRegistry
				.getLoggableModel(objectId);
				*/
		Object localElement = remoteToLocalLoggableModel(element);
		addReceivedObject(localElement);
		loggableModel.elementAdded(hostId, loggableModel, 
				localElement,
				newSize);
		finishedProcessingEvent();

	}

	@Override
	public void elementChanged(String hostId, Object source, Object element,
			int pos) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		/*
		int objectId = (Integer) source;
		ACompositeLoggable loggableModel = LoggableRegistry
				.getLoggableModel(objectId);
				*/
		Object localElement = remoteToLocalLoggableModel(element);
		addReceivedObject(localElement);
		loggableModel.elementChanged(hostId, loggableModel, 
				localElement,
				pos);
		finishedProcessingEvent();

	}

	@Override
	public void elementInserted(String hostId, Object source, Object element,
			int pos, int newSize) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		/*
		int objectId = (Integer) source;
		ACompositeLoggable loggableModel = LoggableRegistry
				.getLoggableModel(objectId);
				*/
		Object localElement = remoteToLocalLoggableModel(element);
		addReceivedObject(localElement);
		loggableModel.elementInserted(hostId, loggableModel, 
				localElement, 
				pos,
				newSize);
		finishedProcessingEvent();

	}

	@Override
	public void elementRemoved(String hostId, Object source, int pos,
			int newSize) {
		/*
		int objectId = (Integer) source;
		ACompositeLoggable loggableModel = LoggableRegistry
				.getLoggableModel(objectId);
				*/
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		
		loggableModel.elementRemoved(hostId, loggableModel, pos, newSize);
		finishedProcessingEvent();

	}

	@Override
	public void elementRemoved(String hostId, Object source, Object element,
			int newSize) {
		/*
		int objectId = (Integer) source;
		ACompositeLoggable loggableModel = LoggableRegistry
				.getLoggableModel(objectId);
				*/
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		Object localElement = remoteToLocalLoggableModel(element); 
		addReceivedObject(localElement);
		loggableModel.elementRemoved(hostId, loggableModel,  
				localElement,
				//remoteToLocalLoggableModel(element), 
				newSize);
		finishedProcessingEvent();

	}

	@Override
	public void elementsCleared(String hostId, Object source) {
		/*
		int objectId = (Integer) source;
		ACompositeLoggable loggableModel = LoggableRegistry
				.getLoggableModel(objectId);
				*/
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		loggableModel.elementsCleared(hostId, loggableModel);
		

	}
	 static void synchronizeIds (ACompositeLoggable local, IdentifiableLoggable remote) {
		if (local.getObjectIds().size() != remote.getObjectIds().size())
		local.setObjectIds(remote.getObjectIds());
		
	}
	 static Object[] remoteToLocalLoggableModel(Object[] remoteLoggableModel) {
		 Object[] retVal = new Object[remoteLoggableModel.length];
		 for (int i = 0; i < retVal.length; i++) {
			 retVal[i] = remoteToLocalLoggableModel(remoteLoggableModel[i]);			 
		 }
		 return retVal;
	 }
	 static Object remoteToLocalLoggableModel(Object remoteLoggableModel) {
		// uiObjectAdapter adapter = ObjectEditor.toObjectAdapter(realObject);
		// if (remoteLoggableModel instanceof ACompositeLoggable) {
		if (remoteLoggableModel instanceof AnIdentifiableLoggable) {
			// ACompositeLoggable remoteComposite = (ACompositeLoggable)
			// remoteLoggableModel;
			IdentifiableLoggable remoteIdentifiable = (IdentifiableLoggable) remoteLoggableModel;
			// int id = remoteComposite.getObjectId();
			String id = remoteIdentifiable.getObjectId();
			ACompositeLoggable local = LoggableRegistry.getLoggableModel(id);
			if (local != null) {
				synchronizeIds(local, remoteIdentifiable);
				return local;
			}
			/*
			 * if (id < nextId) { Message.error("reote id " + id + " <" + "local
			 * nextId " + nextId); }
			 */
			local = new ACompositeLoggable();
			if (!(remoteIdentifiable instanceof ACompositeLoggable))
				Tracer.error("Remote Loggable " + remoteIdentifiable
						+ "not ACompositeLoggable");
			ACompositeLoggable remoteComposite = (ACompositeLoggable) remoteIdentifiable;
			String remoteId = remoteComposite.getObjectId();
			LoggableRegistry.resetObjectId(remoteId);
			local.setObjectId(remoteId);
			local.setVirtualClass(remoteComposite.getVirtualClass());
			local.setObjectIds(remoteComposite.getObjectIds());
			// Object realObject = remoteComposite.getRealObject();
			// Object realObject = LoggableRegistry.getRealObject(local);
			LoggableRegistry.setObjectId(remoteComposite.getRealObject(),
					local, id);
			return local;
		} else {
			return remoteLoggableModel;
		}

	}

	@Override
	public void keyPut(String hostId, Object source, Object key,  Object value, int newSize) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		Object localKey = remoteToLocalLoggableModel(key);
		Object localValue = remoteToLocalLoggableModel(value);
		loggableModel.keyPut(hostId, loggableModel, 
				localKey,
				//remoteToLocalLoggableModel(key), 
				localValue,
				//remoteToLocalLoggableModel(value), 
				newSize);

		
	}

	@Override
	public void keyRemoved(String hostId, Object source, Object key, int newSize) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		Object localKey = remoteToLocalLoggableModel(key);
		loggableModel.keyRemoved(hostId, loggableModel, 
				localKey,
				//remoteToLocalLoggableModel(key),
				newSize);
		
	}
	@Override
	public void update(Observable source, Object arg) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		Object localArg = remoteToLocalLoggableModel(arg);
		// the first argument of update is ignored
		loggableModel.update(source, localArg);
		
		
	}
	@Override
	public void refresh(Object source) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		//Object localArg = remoteToLocalLoggableModel(arg);
		// the argument is ignored
		loggableModel.refresh(loggableModel);
		
		
	}

	@Override
	public void tableChanged(LoggableTableModelEvent e) {
		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(e.getSource());
		loggableModel.tableChanged(new TableModelEvent(
				(TableModel) loggableModel.getRealObject(),
				e.getFirstRow(),
				e.getLastRow(),
				e.getFirstRow(),
				e.getType()));
		
	}
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		IdentifiableLoggable source = (IdentifiableLoggable) e
		.getSource();

		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		loggableModel.treeNodesInserted(new TreeModelEvent(
				loggableModel.getRealObject(),
				e.getTreePath(),
				e.getChildIndices(),
				remoteToLocalLoggableModel(e.getChildren())));
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		IdentifiableLoggable source = (IdentifiableLoggable) e
		.getSource();

		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		loggableModel.treeNodesRemoved(new TreeModelEvent(
				loggableModel.getRealObject(),
				e.getTreePath(),
				e.getChildIndices(),
				remoteToLocalLoggableModel(e.getChildren())));
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		IdentifiableLoggable source = (IdentifiableLoggable) e
		.getSource();

		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		loggableModel.treeStructureChanged(new TreeModelEvent(
				loggableModel.getRealObject(),
				e.getTreePath(),
				e.getChildIndices(),
				remoteToLocalLoggableModel(e.getChildren())));
		
	}

	static Object createRemoteLoggableModel (Object realObject) {
		int id = LoggableRegistry.nextId++;
		//String stringId = "" + id;
		String stringId = LoggableRegistry.getObjectId(LoggableRegistry.getHostId(), id);
		return LoggableRegistry.createLoggableModel(realObject, stringId);
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		IdentifiableLoggable source = (IdentifiableLoggable) e
		.getSource();

		ACompositeLoggable loggableModel = (ACompositeLoggable) AUIComponent
		.remoteToLocalLoggableModel(source);
		loggableModel.treeNodesChanged(new TreeModelEvent(
				loggableModel.getRealObject(),
				e.getTreePath(),
				e.getChildIndices(),
				remoteToLocalLoggableModel(e.getChildren())));
		
	}

	


	
	


	

}
