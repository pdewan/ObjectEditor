package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeModelEvent;

import sun.security.action.GetLongAction;
import util.models.HashtableListener;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
import util.models.VectorMethodsListener;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.HashtableAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.reflect.MethodProxy;

public class AProgramComponent extends Observable implements 
	//Observer, PropertyChangeListener, VectorListener, VectorMethodsListener,  HashtableListener, 
	ProgramComponent  {
	RemoteUIComponent uiComponent;
	Hashtable<String, Integer> hostIdToNextObjectId = new Hashtable();
	Set processedModels = new HashSet();
	//Set propertyChangingModels = new HashSet();
	public AProgramComponent() {
		processedModels.addAll(LoggableRegistry.models());
		Iterator modelsIterator = processedModels.iterator();
		while (modelsIterator.hasNext()) {
			registerAsListener(modelsIterator.next());
		}
		
	}
	public void registerAsListener (Object object) {
		if (processedModels.contains(object))
			return;
		ObjectAdapter.maybeAddPropertyChangeListener(object, this);
		ObjectAdapter.maybeAddObserver(object, this);
		ObjectAdapter.maybeAddRefresher(object, this);
		VectorAdapter.maybeAddVectorListener(object, this);
		HashtableAdapter.maybeAddHashtableListener(object, this);
		VectorAdapter.maybeAddTableModelListener(object, this);
		ObjectAdapter.maybeAddTreeModelListener(object, this);
		
		processedModels.add(object);
		//propertyChangingModels.add(object);
		
	}
	/*
	public  Object invokeMethod (String hostId, int parentObjectId,
			  String methodId,		
	          Object[] parameterValues) {
		return invokeMethod(hostId, parentObjectId, methodId, parameterValues);
		
	}
	*/
	int getCurrentHostNextId () {
		return hostIdToNextObjectId.get(getCurrentHostId());
	}
	//public  Object invokeMethod (String hostId, int parentObjectId,
	public  Object invokeMethod (String hostId, int nextId, 
			  IdentifiableLoggable parentRemoteLoggable,
			  //String parentObjectId,
			  String methodId,		
	          Object[] parameterValues) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		 //try {
		 //ACompositeLoggable loggableModel = LoggableRegistry.getLoggableModel(parentObjectId);
		 //ACompositeLoggable loggableModel = LoggableRegistry.getLoggableModel(parentRemoteLoggable.getObjectId());
		 //Object parentObject = loggableModel.getRealObject();
		 //Object parentObject = LoggableRegistry.getRealObject(loggableModel);
		 //Object parentObject = LoggableRegistry.getRealObject(loggableModel);
		
		 
		 Object parentObject = localLoggableModelToRealObject(parentRemoteLoggable);
		 MethodProxy method = AProgramComponent.getMethod(methodId);
		 
		 setCurrentHostId(hostId);
		 hostIdToNextObjectId.put(hostId, nextId);
		 //Object retVal = method.invoke(parentObject, parameterValues);
		 Object retVal = method.invoke(parentObject, actualParameters( parameterValues));
//		 if (LoggableRegistry.isMethodReadOnly(method)) 
//			 System.out.println("Readonly: " + method.getName());
		 if (LoggableRegistry.methodReturnsValue(methodId))
			 return retVal;
		 return getLoggableModel(retVal);
		 //return AProgramComponent.getLoggableModel(isLocal, hostId, getCurrentHostNextId(), retVal);
		 //return method.invoke(parentObject, parameterValues);
		 /*
		} catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
		 */
		 
	 }
	Object getLoggableModel (Object realObject) {
		return getLoggableModel(LoggableRegistry.isLocal(), getCurrentHostId(), getCurrentHostNextId(), realObject);
	}
	Object[] actualParameters(Object[] loggableParameters) {
		Object[] actualParameters = new Object[loggableParameters.length];
		for (int i =0; i < loggableParameters.length; i++) {
			//actualParameters[i] = LoggableRegistry.getRealObject(loggableParameters[i]);
			actualParameters[i] = localLoggableModelToRealObject(loggableParameters[i]);
		}
		return actualParameters;
	}
	  Object localLoggableModelToRealObject(
			Object obj) {
		if (obj instanceof ACompositeLoggable && !LoggableRegistry.isLocal() ) {							
			ACompositeLoggable compositeLoggable = (ACompositeLoggable) obj;
			LoggableRegistry.setObjectId(compositeLoggable.getRealObject(), compositeLoggable,  compositeLoggable.getObjectId());
			return compositeLoggable.getRealObject();
		} else if (obj instanceof AnIdentifiableLoggable) {
			ACompositeLoggable loggableModel = LoggableRegistry.getLoggableModel(((IdentifiableLoggable) obj).getObjectId());
			 //Object parentObject = loggableModel.getRealObject();
			 return loggableModel.getRealObject();
		} else
			return obj;
		
	}
	public static void synchronizeIds (IdentifiableLoggable local, ACompositeLoggable remote) {
		if (local.getObjectIds().size() != remote.getObjectIds().size())
		remote.setObjectIds(local.getObjectIds());
		
	}
	
	public void setUIComponent(RemoteUIComponent newVal) {
		uiComponent = newVal;
	}
	public RemoteUIComponent getUIComponent() {
		return uiComponent;
	}
	AnIdentifiableLoggable transformSource (Object source) {
		String objectId = AProgramComponent.getObjectStringId(source);
		AnIdentifiableLoggable identifiableLoggable = (AnIdentifiableLoggable) AProgramComponent.getLoggableModel (LoggableRegistry.isLocal(), getCurrentHostId(),  getCurrentHostNextId(), source);
		return identifiableLoggable;
		
	}
	PropertyChangeEvent transformSource(PropertyChangeEvent evt) {
		//int objectId = LoggableRegistry.getObjectIntId(evt.getSource());
		/*
		String objectId = LoggableRegistry.getObjectStringId(evt.getSource());
		AnIdentifiableLoggable identifiableLoggable = (AnIdentifiableLoggable) LoggableRegistry.getLoggableModel (isLocal(), getCurrentHostId(), evt.getSource());
		*/
		IdentifiableLoggable identifiableLoggable = transformSource(evt.getSource());
		PropertyChangeEvent uiPropertyChangeEvent = 
			new PropertyChangeEvent(identifiableLoggable, evt.getPropertyName(), 
					transformEventValue(evt.getOldValue()), 
					transformEventValue(evt.getNewValue()));
		return uiPropertyChangeEvent;
		
	}
	VectorChangeEvent transformSource(VectorChangeEvent evt) {
		//int objectId = LoggableRegistry.getObjectIntId(evt.getSource());
		/*
		String objectId = LoggableRegistry.getObjectStringId(evt.getSource());
		AnIdentifiableLoggable identifiableLoggable = (AnIdentifiableLoggable) LoggableRegistry.getLoggableModel (isLocal(), getCurrentHostId(), evt.getSource());
		*/
		IdentifiableLoggable identifiableLoggable = transformSource(evt.getSource());
		VectorChangeEvent uiVectorEvent = 
			new VectorChangeEvent(identifiableLoggable, evt.getEventType(), 
					evt.getPosition(), 
					transformEventValue(evt.getOldValue()), 
					transformEventValue (evt.getNewValue()), 
					evt.getNewSize());
		return uiVectorEvent;
		
	}
	LoggableTableModelEvent transform(TableModelEvent evt) {
		//int objectId = LoggableRegistry.getObjectIntId(evt.getSource());
		/*
		String objectId = LoggableRegistry.getObjectStringId(evt.getSource());
		AnIdentifiableLoggable identifiableLoggable = (AnIdentifiableLoggable) LoggableRegistry.getLoggableModel (isLocal(), getCurrentHostId(), evt.getSource());
		*/
		IdentifiableLoggable identifiableLoggable = transformSource(evt.getSource());
		LoggableTableModelEvent tableModelEvent = new ALoggableTableModelEvent (identifiableLoggable,
													evt.getFirstRow(), evt.getLastRow(), evt.getColumn(), evt.getType());
			
		return tableModelEvent;
	}
	Object transformEventValue(Object val) {
		//return LoggableRegistry.getExistingLoggableModelOrRealObject(val);
		return getLoggableModel(val);
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (getUIComponent() == null) return;
		// TODO Auto-generated method stub
		/*
		int objectId = LoggableRegistry.getObjectId(evt.getSource());
		PropertyChangeEvent uiPropertyChangeEvent = 
			new PropertyChangeEvent(objectId, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		*/
		getUIComponent().propertyChange(LoggableRegistry.getHostId(), transformSource(evt));
		
	}
	
	/*
	public void update (Observable o, Object arg) {
		int objectId = LoggableRegistry.getObjectId(o);
		//implicitRefresh();	
	 	//refreshEditedObject();
	 }
	 */
	@Override
	public void updateVector(VectorChangeEvent evt) {
		if (getUIComponent() == null) return;
		/*
		int objectId = LoggableRegistry.getObjectIntId(evt.getSource());
		VectorChangeEvent vectorEvent = new VectorChangeEvent(objectId, evt.getEventType(), evt.getPosition(), evt.getOldValue(), evt.getNewValue(), evt.getNewSize());
		*/
		getUIComponent().updateVector(LoggableRegistry.getHostId(), transformSource(evt));
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		getUIComponent().tableChanged(transform(e));
		
	}
	@Override
	public void elementAdded(Object source, Object element, int newSize) {
		getUIComponent().elementAdded(LoggableRegistry.getHostId(), 
				transformSource(source), 
				transformEventValue(element), 
				newSize);
		
		
	}
	@Override
	public void elementChanged(Object source, Object element, int pos) {
		getUIComponent().elementChanged(LoggableRegistry.getHostId(), 
				transformSource(source), 
				transformEventValue(element),  
				pos);
		
		
	}
	@Override
	public void elementInserted(Object source, Object element, int pos,
			int newSize) {
		getUIComponent().elementInserted(LoggableRegistry.getHostId(), 
				transformSource(source), transformEventValue(element),  pos,  newSize);
		
	}
	@Override
	public void elementRemoved(Object source, int pos, int newSize) {
		getUIComponent().elementRemoved(LoggableRegistry.getHostId(), 
				transformSource(source), pos,  newSize);
		
	}
	@Override
	public void elementRemoved(Object source, Object element, int newSize, int pos) {
		getUIComponent().elementRemoved(LoggableRegistry.getHostId(), 
				transformSource(source), transformEventValue(element),  newSize);
		
	}
	@Override
	public void elementsCleared(Object source) {
		getUIComponent().elementsCleared(LoggableRegistry.getHostId(), 
				transformSource(source));
		
	}
	/*
	boolean isLocal = true;
	public boolean isLocal() {
		return isLocal;
	}
	public void setIsLocal(boolean newVal) {
		isLocal = newVal;
	}
	*/

	Object clone(Object original) {
		if (LoggableRegistry.isLocal())
			return OEMisc.clone(original);
		else
			return original;
		
	}
	
	String currentHostId = "";
	public void setCurrentHostId (String newVal) {
		currentHostId = newVal;
	}
	String getCurrentHostId() {
		return currentHostId;
	}
	@Override
	public void keyPut(Object source, Object key, Object value, int newSize) {
		if (getUIComponent() == null) return;
		getUIComponent().keyPut(LoggableRegistry.getHostId(), 
				transformSource(source), 
				transformEventValue(key), 
				transformEventValue (value),  
				newSize);
		
		
	}
	@Override
	public void keyRemoved(Object source, Object key, int newSize) {
		if (getUIComponent() == null) return;
		getUIComponent().keyRemoved(LoggableRegistry.getHostId(), 
				transformSource(source), 
				transformEventValue(key), 
				newSize);
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (getCurrentHostId() == "") return;
		if (getUIComponent() == null) return;
		getUIComponent().update(transformSource(o), transformEventValue(arg));
		
	}
	@Override
	public void refresh(Object o) {
		if (getCurrentHostId() == "") return;
		if (getUIComponent() == null) return;
		getUIComponent().refresh(transformSource(o));
		
	}
	public static String getObjectStringId(Object object) {
		return LoggableRegistry.objectToStringId.get(object);
	}
	public static MethodProxy getMethod(String id) {
		MethodProxy retVal = LoggableRegistry.stringIdToMethod.get(id);
		if (retVal == null) {
			retVal = LoggableRegistry.stringToMethodProxy(id);
			LoggableRegistry.setMethodId(retVal, id);
		}
		return retVal;
	}
	public static Object getLoggableModel(boolean isLocal, String hostId,
			int nextHostIntId,	
			Object realObject) {
		if (realObject == null)
			return null;
		/*
		// uiObjectAdapter adapter = ObjectEditor.toObjectAdapter(realObject);
		if (realObject == null)
			return null;
		ClassProxy realClass = RemoteSelector.getClass(realObject);
		if (realObject instanceof ACompositeLoggable
				|| primitiveFactory.isPrimitive(realClass, realObject)
				|| enumFactory.isEnumeration(realClass))
			return realObject;
		String existingId = objectToStringId.get(realObject);
		if (existingId != null) {
			return stringIdToLoggableModel.get(existingId);
		}
		*/
		Object retVal = LoggableRegistry.getExistingLoggableModel(realObject);
		if (retVal != null) {
			if (isLocal || !(retVal instanceof ACompositeLoggable))
			return retVal;
			return LoggableRegistry.removeRealObject((ACompositeLoggable) retVal);
		}
		String stringId = "";
		if (isLocal) {
			int id = LoggableRegistry.nextId++;
			stringId = "" + id;
			return LoggableRegistry.createLoggableModel(realObject, stringId);
		} else {
			stringId = LoggableRegistry.getObjectId(hostId, nextHostIntId);
			retVal = LoggableRegistry.createLoggableModel(realObject, stringId);
			//LoggableRegistry.getProgramComponent().processModel(retVal);
			return retVal;
			//return LoggableRegistry.createLoggableModel(realObject, stringId);
		}
		// return createCompositeLoggableModel(realObject);
	}
	Object[] transformChildren(Object[] children) {
		if (children == null)
			return null;
		Object[] retVal = new Object[children.length];
		for (int i = 0; i < children.length; i++)
			retVal[i] = transformEventValue(children[i]);
		return retVal;
	}
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		
		TreeModelEvent evt = new TreeModelEvent(transformSource(e.getSource()),
				e.getTreePath(),
				e.getChildIndices(),
				transformChildren(e.getChildren()));
		getUIComponent().treeNodesChanged(e);
		// TODO Auto-generated method stub
		
	}
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		TreeModelEvent evt = new TreeModelEvent(transformSource(e.getSource()),
				e.getTreePath(),
				e.getChildIndices(),
				transformChildren(e.getChildren()));
		getUIComponent().treeNodesInserted(e);
		
	}
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		TreeModelEvent evt = new TreeModelEvent(transformSource(e.getSource()),
				e.getTreePath(),
				e.getChildIndices(),
				transformChildren(e.getChildren()));
		getUIComponent().treeNodesRemoved(e);
		
	}
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		TreeModelEvent evt = new TreeModelEvent(transformSource(e.getSource()),
				e.getTreePath(),
				e.getChildIndices(),
				transformChildren(e.getChildren()));
		getUIComponent().treeStructureChanged(e);
		
	}
	@Override
	public void elementsAdded(Object source, Collection element, int newSize) {
		// TODO Auto-generated method stub
		
		
	}
	@Override
	public void elementCopied(Object source, int fromIndex, int toIndex, int newSize) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementCopied(Object source, int fromIndex, int fromNewSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementMoved(Object source, int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementMoved(Object source, int fromIndex, int fromNewSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementReplaced(Object source, int fromIndex, int toIndex, int newSize) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementReplaced(Object source, int fromIndex, int newFromSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void elementSwapped(int index1, int index2) {
//		elementSwapped(null, index1, index2);
//	}
	@Override
	public void elementSwapped(Object newParam, int index1, int index2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementSwapped(Object source, int index1, Object other, int index2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementCopiedToUserObject(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementCopiedFromUserObject(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void userObjectChanged(Object source, Object newVal) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementRead(Object source, Object element, Integer pos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tempChanged(Object source, Object newVal) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementCopiedToTemp(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementCopiedFromTemp(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void userObjectCopiedToTemp(Object source, Object copiedValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tempCopiedToUserObject(Object source, Object copiedValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void userObjectRead(Object source, Object readValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tempRead(Object source, Object readValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementCopiedAndInserted(Object source, int fromIndex,
			int toIndex, int newSize) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void elementCopiedAndInserted(Object source, int fromIndex,
			int fromNewSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pointerChanged(Object source, Integer pointerValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pointer2Changed(Object source, Integer pointerValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void userOperationOccured(Object source, Integer aTargetIndex,
			Object anOperation) {
		// TODO Auto-generated method stub
		
	}
	
	

}
