package bus.uigen.oadapters;

import java.awt.Container;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import util.models.Hashcodetable;
import util.models.RemotePropertyChangeListener;
import util.models.VectorChangeEvent;
import util.models.VectorListenable;
import util.models.VectorListener;
import util.models.VectorListenerRegisterer;
import util.trace.Tracer;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.ReplaceableChildren;
import bus.uigen.UnivVectorEvent;
import bus.uigen.WidgetAdapterInterface;
import bus.uigen.uiGenerator;
import bus.uigen.adapters.CommandAndStatePanelAdapter;
import bus.uigen.adapters.MSTextFieldAdapter;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.diff.ACanonicalListBean;
import bus.uigen.editors.EditorRegistry;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.misc.OEMisc;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.sadapters.VectorStructure;
import bus.uigen.trace.VectorAdapterReceivedVectorChangeEvent;
import bus.uigen.view.WidgetShell;
import bus.uigen.visitors.ClearVisitedNodeAdapterVisitor;
import bus.uigen.visitors.IsEditedAdapterVisitor;
import bus.uigen.widgets.VirtualContainer;

//public class uiVectorAdapter extends uiContainerAdapter
public class VectorAdapter extends ClassAdapter implements VectorListener,
		ReplaceableChildren, RemotePropertyChangeListener {
	public static boolean COMPRESS = false;

	
	boolean isListenableString = false;
	

	// will contain only visible children and is sorted by user preference
	transient Vector<ObjectAdapter> indexedAdapterVector = new Vector();
	// will contain all chidren. Needed by get all original components. Sorted
	// by position
	// in vector and not user preference
	transient Vector<ObjectAdapter> indexedVisibleAndInvisibleAdapterVector = new Vector();
	
	List listDelegate;
	List canonicalListBean;

	// table indexed by real object
	// originally contained deleted and non visible adapters. Now will contain
	// only
	// visible children
	//transient Hashtable<Object, ObjectAdapter> deletedIndexedAdapters = new Hashtable();
	transient Hashcodetable<Object, ObjectAdapter> deletedIndexedAdapters = new Hashcodetable();
	
	public VectorAdapter() throws RemoteException {

	}

	// transient Hashtable<Object, uiObjectAdapter> indexedAdaptersTable = new
	// Hashtable();
	public void setViewObject(Object obj) {
		super.setViewObject(obj);
		if (obj != null && IntrospectUtility.isListenableString(obj)) {

//		if (obj != null && obj.getClass().isAssignableFrom(AListenableString.class))

//		if (obj != null && obj.getClass().isAssignableFrom(ListenableString.class))
			isListenableString = true;
		}
	}
	
	// VectorStructure vectorStructure;
	public void sortAncestor(ObjectAdapter topAdapter) {

		if (!hasUserObject())
			return;
		ObjectAdapter parentAdapter = getParentAdapter();
		if (!(parentAdapter instanceof VectorAdapter))
			return;
		parentAdapter.setLocalAttribute(AttributeNames.LIST_SORT_USER_OBJECT,
				true);
		/*
		 * uiAttributeManager environment = AttributeManager.getEnvironment();
		 * if (environment == null) return;
		 * environment.removeFromAttributeLists(parentClass.getName());
		 */
		// ViewInfo cd = ClassDescriptorCache.getClassDescriptor(parentClass);
	}

	public String toString() {
		if (!getSortMode() || getParentAdapter() == null)
			return super.toString();

		if (getParentAdapter().getListSortUserObject()) {
			return getUserObject();
		}
		Object sortProperty = getSortProperty();
		if (!(sortProperty instanceof Integer))
			return super.toString();
		int sortIndex = (Integer) sortProperty;
		VectorStructure vectorStructure = getVectorStructure();
		if (sortIndex >= vectorStructure.size())
			sortIndex = 0;
		// return super.toString();
		Object sortVal = vectorStructure.elementAt(sortIndex);
		if (sortVal != null)
			return sortVal.toString();
		else
			return super.toString();
	}

	void setVectorStructure(VectorStructure newVal) {
		// vectorStructure = newVal;
		setConcreteObject(newVal);
	}

	public VectorStructure getVectorStructure() {
		// return vectorStructure;
		return (VectorStructure) getConcreteObject();
	}

	public boolean isAddable(Object o) {
		ClassProxy addableElementType = getVectorStructure()
				.addableElementType();
		System.out.println(addableElementType);
		return addableElementType != null
				&& addableElementType.isAssignableFrom(RemoteSelector
						.getClass(o));
	}

	public void insertObject(Object element, int pos) {
		(getVectorStructure()).insertElementAt(element, pos, this);
	}

	public void addObject(Object o) /* throws Exception */{
		try {
			(getVectorStructure()).addElement(o, this);

			/*
			 * Object params[] = {o}; Object parentObject =
			 * this.getRealObject(); // delete uses real object
			 * getUIFrame().getUndoer().execute ( new
			 * SymmetricCommand(//getUIFrame(), this, addElementMethod,
			 * parentObject, params, removeElementMethod));
			 */
			uiComponentValueChanged(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// propagateChange();

	}

	/*
	 * public uiVectorAdapter() { adapterMapping = new Vector(); }
	 */
	public boolean isChildDeletable(ObjectAdapter child) {
		VectorStructure vectorStructure = getVectorStructure();
		int index = Integer.parseInt(getChildAdapterRealIndex(child));
		// return vectorStructure.canDeleteChild() &&
		// Integer.parseInt(getChildAdapterIndex(child)) != -1;
		return vectorStructure.hasDeleteChildMethod() && index != -1
				&& !child.isReadOnly();
		// vectorStructure.isEditable(index);

		/*
		 * try {
		 * 
		 * 
		 * if (getViewObject() == null) return false; return
		 * getRemoveElementAtMethod() != null || getRemoveElementMethod() !=
		 * null || Integer.parseInt(getChildAdapterIndex(child)) != -1; } catch
		 * (Exception e) { return false; }
		 */
	}

	public boolean validateDeleteChild(ObjectAdapter child) {
		VectorAdapter parentAdapter = this;
		// int index =
		// Integer.parseInt(parentAdapter.getChildAdapterIndex(child));
		int index = child.getRealVectorIndex();
		if (index == -1)
			return false;
		Object element = child.getOriginalValue();
		// why is child a listener and not the adapter itself
		if (!getVectorStructure().hasDeleteChildMethod())
			return false;
		if (!getVectorStructure().isEditable(index))
			return false;
		if (!getVectorStructure().validateRemoveElement(element))
			return false;
		if (!getVectorStructure().validateRemoveElementAt(index))
			return false;
		return true;

	}

	public boolean validateAddChild(Object child) {
		return (getVectorStructure().hasAddChildMethod() && getVectorStructure()
				.validateAddElement(child));
	}

	public boolean canAddChild() {
		return getVectorStructure().hasAddChildMethod();
	}

	public boolean canInsertChild() {
		return (getVectorStructure().hasInsertChildMethod());

	}

	public boolean hasValidateInsertElementAt() {
		return getVectorStructure().hasValidateInsertElementAt();
	}

	public boolean hasValidateAddElement() {
		return getVectorStructure().hasValidateAddElement();
	}

	public boolean validateInsertChild(int index, Object child) {
		if (index == -1)
			return false;
		if (getVectorStructure().hasInsertChildMethod()
				&& getVectorStructure().validateInsertElementAt(child, index))
			return true;
		if (index != getVectorStructure().size())
			return false;
		return validateAddChild(child);

	}

	public ClassProxy addableElementType() {
		if (getNumberOfDynamicChildren() > 0) {
			return RemoteSelector.getClass(getIndexedAdaptersVector()
					.elementAt(0).getRealObject());
		} else
			return getVectorStructure().addableElementType();
	}

	public void deleteChild(ObjectAdapter child) {
		VectorAdapter parentAdapter = this;
		// int index =
		// Integer.parseInt(parentAdapter.getChildAdapterIndex(child));
		int index = child.getRealVectorIndex();
		if (index == -1)
			return;
		Object element = child.getOriginalValue();
		// why is child a listener and not the adapter itself
		if (child.isReadOnly())
			// if (!getVectorStructure().isEditable(index))
			return;
		if (!getVectorStructure().hasDeleteChildMethod())
			return;
		if (!getVectorStructure().validateRemoveElement(element))
			return;
		if (!getVectorStructure().validateRemoveElementAt(index))
			return;
		(getVectorStructure()).removeElement(index, element, this);
		uiComponentValueChanged(false);
		// propagateChange();

	}

	public Object getUserChange() {
		return getOriginalValue();
		// return getValue();
	}

	// public void childUIComponentValueChanged (boolean initialChange) {
	public boolean childUIComponentValueChanged(ObjectAdapter child,
			Object newValue, boolean initialChange) {
		if (child.getAdapterType() == PROPERTY_TYPE) {
			return super.childUIComponentValueChanged(child, newValue,
					initialChange);
			// return;
		}
		VectorStructure vectorStructure = getVectorStructure();
		// System.out.println("New componnet value" + newValue);
		/*
		 * if (vectorStructure == null) { System.out.println("Unexected null
		 * virtual structure"); return; }
		 */
		int index = computeIndex(child);
		if (	!vectorStructure.hasSetChildMethod() ||
				child.isReadOnly() ||
				!vectorStructure.preSetElementAt() ||
				!vectorStructure.hasSetChildMethod() || 
				!vectorStructure.validateSetElementAt(newValue, index))
				
				
						return false;
//		if (!vectorStructure.canSetChild())
//			return false;
//		// if (!vectorStructure.isEditable(index))
//		if (child.isReadOnly())
//			return false;
//		if (!vectorStructure.validateSetElementAt(newValue, index))
//			return false;
//		
//		if (!vectorStructure.preSetElementAt())
//			return false;
//		if (!vectorStructure.canSetChild())
//			return false;
//		// if (!vectorStructure.isEditable(index))
//		if (child.isReadOnly())
//			return false;
//		if (!vectorStructure.validateSetElementAt(newValue, index))
//			return false;
		if (initialChange) // only initial propagating change needs to be
							// logged
			vectorStructure.setElementAt(newValue, index, child);
		else
			vectorStructure.setElementAt(newValue, index);
		return true;
		// askViewObjectToRefresh();

	}

	public Object getChildValue(ObjectAdapter child) {
		if (child.getAdapterType() == PROPERTY_TYPE)
			return super.getChildValue(child);
		VectorStructure vectorStructure = getVectorStructure();
		int index = computeIndex(child);
		if (index < 0 || index >= vectorStructure.size())
			return null;
		return vectorStructure.elementAt(index);
	}

	public Enumeration getChildren() {
		Vector v = new Vector();
		Enumeration properties = super.getChildren();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		properties = indexedAdapterVector.elements();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		return v.elements();
		// return adapterMapping.elements();
		// return mapping.elements();
	}

	public Enumeration getDynamicChildAdapters() {
		return indexedAdapterVector.elements();

	}

	public Vector<ObjectAdapter> getIndexedAdaptersVector() {
		return indexedAdapterVector;

	}

	public int getNumberOfDynamicChildren() {
		return indexedAdapterVector.size();
	}

	// Method defined in VectorListener
	// through which a listenable Vector
	// informs the UI of any changes

	public void updateVector(VectorChangeEvent evt) {
		if (isDisposed())
			return;
		VectorAdapterReceivedVectorChangeEvent.newCase(this, evt);
		if (!ObjectEditor.shareBeans()) {
			subUpdateVector(evt);
		} else {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry
						.logUnivVectorEvent(new UnivVectorEvent(this, evt));
			} else {
				String myPath = getCompletePathOnly();
				System.out.print("PPP propertyChange at " + myPath);
				if (myPath != null) {
					if (ObjectEditor.colabMode())
					ObjectRegistry.logUnivVectorEvent(new UnivVectorEvent(
							myPath, evt));
				} else {
					subUpdateVector(evt);
				}
			}
		}
	}
	
	boolean receivedVectorNotification = false;
	public boolean getReceivedVectorNotification() {
		return receivedVectorNotification;
	}
//	public void haveReceivedNotification() {
//		
//		if (getImplicitRefreshOnNotification())
//			return;
//		super.haveReceivedNotification();
//		resetReceivedVectorNotification();
//	}

	public void haveReceivedVectorNotification() {
		// bad form to this here, but convenient
		if (getImplicitRefreshOnNotification())
			return;
		receivedVectorNotification = true;
	}

	void resetReceivedVectorNotification() {
		receivedVectorNotification = false;
	}
//	void resetReceivedNotification() {
//		super.resetReceivedNotification();
//		resetReceivedVectorNotification();
//	}

	public void subUpdateVector(VectorChangeEvent evt) {
		// Assume that the sender knows what it is doing
//		if (evt.getSource() != computeAndMaybeSetViewObject()) {
//			return;
//		}
		Tracer.info(this, "OE received event:" + evt);
		haveReceivedNotification();
		haveReceivedVectorNotification();
		if (isListenableString) {
			if (getWidgetAdapter() != null)
			getWidgetAdapter().childComponentsAdded(true);
			return;
		}
		int position = evt.getPosition();
		int size = indexedAdapterVector.size();
		switch (evt.getEventType()) {
		case VectorChangeEvent.AddComponentEvent:
		case VectorChangeEvent.InsertComponentEvent:
			
			if (position < 0 || position > indexedAdapterVector.size()) {
				Tracer.error("Illegal event position: " + position + " in vector insert event received from: " + evt.getSource() + ". Assuming position = " + size);
				position = size;
			}
			
			//addComponent(evt.getPosition(), evt.getNewValue());
			addComponent(position, evt.getNewValue());
			//addComponentWorkingOld(evt.getPosition(), evt.getNewValue());
			break;
		case VectorChangeEvent.DeleteComponentEvent:
			
			if (position < 0 || position >= indexedAdapterVector.size()) {
				Tracer.error("Illegal event position: " + position + " in vector delete event received from: " + evt.getSource() + ". Assuming position = " + (size - 1));
				position = size - 1;
				return;
			}
			deleteComponent(evt.getPosition());
			break;
		case VectorChangeEvent.ChangeComponentEvent:
			if (position < 0 || position >= indexedAdapterVector.size()) {
				Tracer.error("Illegal event position: " + position + " in vector change event received from: " + evt.getSource() + ". Assuming position = " + (size - 1));
			}
			position = size - 1;
			changeComponent(evt.getPosition(), evt.getNewValue());
			break;
		case VectorChangeEvent.ClearEvent:
			removeAllComponents();
			break;
		case VectorChangeEvent.AddAllComponentsEvent:
			if (position < 0 || position > indexedAdapterVector.size()) {
				Tracer.error("Illegal event position: " + position + " in vector change event received from: " + evt.getSource() + ". Assuming position = " + (size));
			}
			position = size;
			//removeAllComponents();
			addComponent(evt.getPosition(), (Collection) evt.getNewValue());
			break;
		}
		//recomputeVisibleChildrenLists();
		//if (shouldRebuildAllVisible())
			//rebuildAllVisible();
		//else {
		// this seems to be done by the routines above
		/*
		if (!shouldRebuildAllVisible()) {
		recomputeIndices();
		redisplayIndices();
		}
		*/
		//}
		getUIFrame().validate();
		getUIFrame().repaint();
		// why was this not there before?
		resetReceivedVectorNotification();
		
	}

	public boolean processDirection(String direction) {
		// if (getWidgetAdapter() instanceof CommandAndStatePanelAdapter)
		return getWidgetAdapter().processDirection(direction);

		
		
		// return false;
	}

	// Add a new component to the Vector
	//
	ObjectAdapter get (Hashcodetable<Object, ObjectAdapter> table, Object realObject) {
		return table.get(realObject);
		
	}
	public void addComponent(int position, Object obj) {
//		if (isListenableString) {
//			if (getWidgetAdapter() != null)
//			getWidgetAdapter().childComponentsAdded(true);
//			return;
//		}
		List elements = new ArrayList();
		elements.add(obj);
		
		addComponent(position, elements);
		
	}
	public void addComponent(int position, Collection elements) {
		// is this some vestigal code, why was it size?
//		int from = indexedAdapterVector.size();
		int from = position;
		int index = from;
		invalidateComponentsSet();
		for (Object obj:elements) {
		addComponentBasic(index, obj);
		index++;		
		}
	
		int to =  indexedAdapterVector.size();
		if (to == from)
			return;
		recomputeRealIndices(from);
		setDirection();
		//dont think this is ever needed
		//processDirection((String) getMergedAttributeValue(AttributeNames.DIRECTION));
		
		int numStaticChildren = getNumberOfDisplayedStaticChildren() ;
		
		// batch this also
		uiGenerator.deepCreateChildren(this, numStaticChildren
				+ from, numStaticChildren
				+ to);
		
		uiGenerator.deepProcessAttributes(this, false,
				numStaticChildren + from,
				numStaticChildren + to);
		uiGenerator.deepElide(this);
		

	}
	public void addComponentBasic(int position, Object obj) {
		// Add an element at the position specified in evt
		// First create a UI component corresponding to
		// the object being added.
		// int previousNumberOfChildren = this.getCurrentChildCount();

		// Container awtComponent = (Container)
		// getWidgetAdapter().getUIComponent();
		Object newViewObject = computeAndMaybeSetViewObject();

		if (getForceRebuild()) {
			// implicitRefresh();
			refresh();
			return;
		}

		ObjectAdapter adapter = null;
		boolean createAdapter = true;
		if (obj != null) {
			// adapter = deletedIndexedAdapters.get(obj);
			adapter = get(deletedIndexedAdapters, obj);

			if (adapter != null) {

				// createAdapter = false;
				deletedIndexedAdapters.remove(obj);
				registerAsListener(adapter);
				createAdapter = false;
				// makes sure components of it are created before it is inserted
				// for some reason refreshValue does not work
				// adapter.refreshValue(obj);
//				uiGenerator.deepCreateChildren(adapter, false);
			
			}
		}
		if (adapter == null) {
			adapter = uiGenerator.createObjectAdapter(

			this, obj, RemoteSelector.getClass(obj), position,
					AttributeNames.ANY_ELEMENT,
					computeAndMaybeSetViewObject(), false);
		}
		uiGenerator.deepCreateChildren(adapter, false);
	
		adapter.setAdapterType(INDEX_TYPE); // this may be needed only in creation case
		// setDynamicVisibleChildIndices(position, adapter);
		if (!getVectorStructure().isEditable(position))
			adapter.setUneditable();
		// adapter.setRealVectorIndex(position);
		LoggableRegistry.mapLoggableToAdapter(adapter);
		// we did that before
//		if (createAdapter)
//			uiGenerator.deepCreateChildren(adapter, false);

		indexedVisibleAndInvisibleAdapterVector.insertElementAt(adapter,
				position);
		// //batch this
		// recomputeRealIndices(position);

		adapter.setParentAdapter(this);
		// // this should be batched
		// setDirection();
		// processDirection((String)
		// getMergedAttributeValue(AttributeNames.DIRECTION));
		if (adapter.isVisible()) {
			indexedAdapterVector.add(adapter);
			childrenVector.add(adapter);
			int lastVisibleIndex = indexedAdapterVector.size() - 1;
			int numStaticChildren = getNumberOfDisplayedStaticChildren();
			adapter.setVectorIndex(lastVisibleIndex);
			adapter.setIndex(numStaticChildren + lastVisibleIndex);

			// this will add it to the parent container also
			// do not need this
			if (!createAdapter && adapter instanceof CompositeAdapter) {
				((CompositeAdapter) adapter).refreshValue(obj, true);

			}

		}

		

	}
	public void addComponentBasicMultipleRefreshes(int position, Object obj) {
		// Add an element at the position specified in evt
		// First create a UI component corresponding to
		// the object being added.
		// int previousNumberOfChildren = this.getCurrentChildCount();

		// Container awtComponent = (Container)
		// getWidgetAdapter().getUIComponent();
		Object newViewObject = computeAndMaybeSetViewObject();
		
		if (getForceRebuild()) {
			// implicitRefresh();
			refresh();
			return;
		}

		ObjectAdapter adapter = null;
		boolean createAdapter = true;
		if (obj != null) {
			//adapter = deletedIndexedAdapters.get(obj);
			adapter = get(deletedIndexedAdapters, obj);
			
			
			if (adapter != null) {
				
				// createAdapter = false;
				deletedIndexedAdapters.remove(obj);
				registerAsListener(adapter);
				createAdapter = false;
				// makes sure components of it are created before it is inserted 
				//adapter.refreshValue(obj);
				uiGenerator.deepCreateChildren(adapter, false);
//				if (adapter instanceof uiContainerAdapter) {
//					((uiContainerAdapter) adapter).refreshValue(obj, true);
//				}
				
				// later we will do deepProcessAttributes of vector so this seems unncessary
				//uiGenerator.deepProcessAttributes(adapter);
			}
		}
		if (adapter == null)
			adapter = uiGenerator.createObjectAdapter(
					
					this, obj, RemoteSelector.getClass(obj), position,
					AttributeNames.ANY_ELEMENT, computeAndMaybeSetViewObject(), false);
		adapter.setAdapterType(INDEX_TYPE);
		//setDynamicVisibleChildIndices(position, adapter);
		if (!getVectorStructure().isEditable(position))
			adapter.setUneditable();
		//adapter.setRealVectorIndex(position);
		LoggableRegistry.mapLoggableToAdapter(adapter);
			if (createAdapter)
			uiGenerator.deepCreateChildren(adapter, false);
			
			indexedVisibleAndInvisibleAdapterVector.insertElementAt(adapter, position);
//			//batch this
//			recomputeRealIndices(position);
			
			adapter.setParentAdapter(this);
//			// this should be batched
//			setDirection();
//			processDirection((String) getMergedAttributeValue(AttributeNames.DIRECTION));
			if (adapter.isVisible()) {
			indexedAdapterVector.add(adapter);
			childrenVector.add(adapter);
			int lastVisibleIndex = indexedAdapterVector.size() -1;
			int numStaticChildren = getNumberOfDisplayedStaticChildren() ;
			adapter.setVectorIndex(lastVisibleIndex);
			adapter.setIndex(numStaticChildren + lastVisibleIndex);
			
			// this will add it to the parent container also
			if (!createAdapter && adapter instanceof CompositeAdapter) {
				((CompositeAdapter) adapter).refreshValue(obj, true);
				
			}
			
			
//			// batch this also
//			uiGenerator.deepCreateChildren(this, numStaticChildren
//					+ lastVisibleIndex, numStaticChildren
//					+ indexedAdapterVector.size());
//			
//			// batch this
//			uiGenerator.deepProcessAttributes(this, false,
//					numStaticChildren + lastVisibleIndex,
//					numStaticChildren + indexedAdapterVector.size());
//			uiGenerator.deepElide(this);
			}
			
			//boolean onlyAdditions = position == indexedVisibleAndInvisibleAdapterVector.size() -1;
			//refreshVisible(newViewObject, onlyAdditions, false, false, position, -1, position);
			/*
			if (getUIFrame() != null) {
				getUIFrame().getFrame().validate();
				getUIFrame().getFrame().repaint();
			}
			*/
			
		//}
		//String label;
		// maybe we should reset indices in both case
		
		/*
		
		recomputeVisibleChildrenLists();
		recomputeIndices();
		int displayPosition = adapter.getVectorIndex();
		if (shouldRebuildAllVisible())
			rebuildAllVisible();
		else {
		int numStaticChildren = super.getNumberOfStaticChildren();
		
		uiGenerator.deepProcessAttributes(this, isTopAdapter(),
				numStaticChildren + displayPosition, numStaticChildren + displayPosition + 1);
		
		getWidgetAdapter().childComponentsAdded(true);
		// uiGenerator.deepElide(adapter);

		// deep elide this rather than adaper because the vector may be empty
		uiGenerator.deepElide(this);
		
		if (getUIFrame() != null) {
			getUIFrame().getFrame().validate();
			getUIFrame().getFrame().repaint();
		}
		}
		*/

		// processDirection((String) getAttributeValue("direction"));
		

	}
	public void addComponentWorkingOld(int position, Object obj) {
		// Add an element at the position specified in evt
		// First create a UI component corresponding to
		// the object being added.
		// int previousNumberOfChildren = this.getCurrentChildCount();

		// Container awtComponent = (Container)
		// getWidgetAdapter().getUIComponent();
		Object newViewObject = computeAndMaybeSetViewObject();
		
		if (getForceRebuild()) {
			// implicitRefresh();
			refresh();
			return;
		}

		ObjectAdapter adapter = null;
		boolean createAdapter = true;
		if (obj != null) {
			//adapter = deletedIndexedAdapters.get(obj);
			adapter = get(deletedIndexedAdapters, obj);
			if (adapter != null) {
				// createAdapter = false;
				deletedIndexedAdapters.remove(obj);
				registerAsListener(adapter);
				createAdapter = false;
				// makes sure components of it are created before it is inserted 
				uiGenerator.deepCreateChildren(adapter, false);
				// later we will do deepProcessAttributes of vector so this seems unncessary
				//uiGenerator.deepProcessAttributes(adapter);
			}
		}
		if (adapter == null)
			adapter = uiGenerator.createObjectAdapter(
					// awtComponent,
					// this, obj, obj.getClass(), position, "element",
					// getViewObject(), false);
					this, obj, RemoteSelector.getClass(obj), position,
					AttributeNames.ANY_ELEMENT, computeAndMaybeSetViewObject(), false);
		adapter.setAdapterType(INDEX_TYPE);
		//setDynamicVisibleChildIndices(position, adapter);
		if (!getVectorStructure().isEditable(position))
			adapter.setUneditable();
		//adapter.setRealVectorIndex(position);
		LoggableRegistry.mapLoggableToAdapter(adapter);
		// adapter.setIndex(position);
		//if (!adapter.isVisible()) {
			//setForceRebuild(true);
			//cleanUpForReuse(adapter);
			//deleteIndexedChildForReuse(adapter);

		//} else {
			// what if real object has changed!
			// if (createAdapter)
			uiGenerator.deepCreateChildren(adapter, false);
			
			indexedVisibleAndInvisibleAdapterVector.insertElementAt(adapter, position);
			recomputeRealIndices(position);
			
			//adapter.setRealVectorIndex(position);
			
			//this.setChildAdapterMapping(Integer.toString(position), adapter);
			// adapter.setAdapterType(INDEX_TYPE);
			// adapter.setIndex(position);
			// adding numFeatures
			//insertChildAdapterAt(adapter, position + numFeatures);
			adapter.setParentAdapter(this);
			setDirection();
			//processDirection((String) getMergedAttributeValue("direction"));
			processDirection((String) getMergedAttributeValue(AttributeNames.DIRECTION));
			if (adapter.isVisible()) {
			indexedAdapterVector.add(adapter);
			childrenVector.add(adapter);
			int lastVisibleIndex = indexedAdapterVector.size() -1;
			int numStaticChildren = getNumberOfDisplayedStaticChildren() ;
			adapter.setVectorIndex(lastVisibleIndex);
			adapter.setIndex(numStaticChildren + lastVisibleIndex);
			uiGenerator.deepCreateChildren(this, numStaticChildren
					+ lastVisibleIndex, numStaticChildren
					+ indexedAdapterVector.size());
			// uiGenerator.deepProcessAttributes(this, false,
			// numStaticChildren + rebuildFrom, numStaticChildren +
			// indexedAdapterVector.size());
			uiGenerator.deepProcessAttributes(this, false,
					numStaticChildren + lastVisibleIndex,
					numStaticChildren + indexedAdapterVector.size());
			uiGenerator.deepElide(this);
			}
			
			//boolean onlyAdditions = position == indexedVisibleAndInvisibleAdapterVector.size() -1;
			//refreshVisible(newViewObject, onlyAdditions, false, false, position, -1, position);
			/*
			if (getUIFrame() != null) {
				getUIFrame().getFrame().validate();
				getUIFrame().getFrame().repaint();
			}
			*/
			
		//}
		//String label;
		// maybe we should reset indices in both case
		
		/*
		
		recomputeVisibleChildrenLists();
		recomputeIndices();
		int displayPosition = adapter.getVectorIndex();
		if (shouldRebuildAllVisible())
			rebuildAllVisible();
		else {
		int numStaticChildren = super.getNumberOfStaticChildren();
		
		uiGenerator.deepProcessAttributes(this, isTopAdapter(),
				numStaticChildren + displayPosition, numStaticChildren + displayPosition + 1);
		
		getWidgetAdapter().childComponentsAdded(true);
		// uiGenerator.deepElide(adapter);

		// deep elide this rather than adaper because the vector may be empty
		uiGenerator.deepElide(this);
		
		if (getUIFrame() != null) {
			getUIFrame().getFrame().validate();
			getUIFrame().getFrame().repaint();
		}
		}
		*/

		// processDirection((String) getAttributeValue("direction"));
		

	}

	// Delete an existing element from the Vector

	public void deleteComponent(int position) {
		// Remove the corresponding children from the
		// AWT component.

		// adjust the mapping

		if (getForceRebuild()) {
			// implicitRefresh();
			refresh();
			return;
		}
		invalidateComponentsSet();
		ObjectAdapter elementAdapter = (ObjectAdapter) indexedVisibleAndInvisibleAdapterVector
		.elementAt(position);
		deleteAdapter(position);
		int displayPosition = elementAdapter.getVectorIndex();
		recomputeRealIndices(position);
		recomputeDisplayIndices(displayPosition);
		propagateAttributesToChildrenWidgetShell(displayPosition);
		propagateAttributesToChildrenWidgetAdapters(displayPosition);
		

	}
	
	 public Vector<ObjectAdapter> getVisibleAndInvisibleDynamicChildAdapters () {
		  return (Vector<ObjectAdapter>) indexedVisibleAndInvisibleAdapterVector.clone();
	  }
	 
	 public Vector<ObjectAdapter> getVisibleAndInvisibleChildAdapters () {
		 Vector retVal = getVisibleAndInvisibleStaticChildAdapters();
		 for (ObjectAdapter anObjectAdapter:indexedVisibleAndInvisibleAdapterVector) {
			 retVal.add(anObjectAdapter);
		 }
		 return retVal;
//		  return (Vector<ObjectAdapter>) indexedVisibleAndInvisibleAdapterVector.clone();
	  }
	 
	

	public void removeAllComponents() {
		// Remove the corresponding children from the
		// AWT component.
		// ((Container) getWidgetAdapter().getUIComponent()).remove(position);
		/*
		 * getWidgetAdapter().remove((Container)
		 * getWidgetAdapter().getUIComponent(),
		 * position,getChildAdapterAt(position));
		 */

		// adjust the mapping
		if (getForceRebuild()) {
			// implicitRefresh();
			refresh();
			return;
		}
		//Vector<uiObjectAdapter> children = (Vector) visibleAndNonVisibleChildrenVector.clone();
		Vector<ObjectAdapter> children = (Vector) indexedVisibleAndInvisibleAdapterVector.clone();

		for (int i = 0; i < children.size(); i++) {
			ObjectAdapter removedAdapter = children.elementAt(i);

			// uiObjectAdapter removedAdapter = getChildAdapterAt(position);
			// adapterMapping.removeElementAt(position);
			deleteIndexedChildForReuse(removedAdapter);
			cleanUpForReuse(removedAdapter);
			// removeChildAdapterAt(position);

		}
		WidgetShell genericWidget = this.getGenericWidget();
		if (genericWidget != null)
			genericWidget.expandElidedString();
		//children.removeAllElements();
		// dont real need this
		//indexedAdapterVector.removeAllElements();
		//indexedVisibleAndInvisibleAdapterVector.removeAllElements();
		
		/*
		 * getWidgetAdapter().remove((Container)
		 * getWidgetAdapter().getUIComponent(), position, removedAdapter);
		 */
		if (getWidgetAdapter() != null)
		getWidgetAdapter().removeAll();
		// getWidgetAdapter().remove( position, removedAdapter);
		// mapping.removeElementAt(position);

	}

	// Change an existing element.
	// in the Vector. The new element may be of a different
	// Class.
	public void oldChangeComponent(int position, Object newValue) {
		// uiObjectAdapter a =
		// getChildAdapterMapping(Integer.toString(position));
		ObjectAdapter a = getChildAdapterMapping(position);
		if (a.getPropertyClass().equals(newValue.getClass())) {
			// Dont need a new ui repn
			// uiObjectAdapter adapter =
			// getChildAdapterMapping(Integer.toString(position));
			ObjectAdapter adapter = getChildAdapterMapping(position);
			adapter.refreshValue(newValue);
		} else {
			// Delete the components
			// representing the old Object
			try {
				
				ObjectAdapter oldAdapter = getChildAdapterMapping(position);
				
				oldAdapter.removeUIComponentFromParent(this);
				// deleteChildAdapter(fieldName);
				deleteChildAdapter(position);

				// could reuse deleted adapter for same real object, but not
				// sure if that will work in case real object componnets have
				// changed
				ObjectAdapter adapter = uiGenerator.createObjectAdapter(
						// (Container)(getUIComponent()),
						this, newValue, RemoteSelector.getClass(newValue),
						position, AttributeNames.ANY_ELEMENT,
						computeAndMaybeSetViewObject(), false);
				adapter.setAdapterType(INDEX_TYPE);
				// setChildAdapterMapping(fieldName, adapter);
				setChildAdapterMapping(position, adapter);
				adapter.setParentAdapter(this);
				// moveGenericWidget(adapter.getGenericWidget(), position);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void changeComponent(int position, Object newValue) {
		// uiObjectAdapter a =
		// getChildAdapterMapping(Integer.toString(position));
		ObjectAdapter a = indexedVisibleAndInvisibleAdapterVector
				.get(position);
		//if (a.getPropertyClass().equals(newValue.getClass())) {
		if (a.getPropertyClass().equals(RemoteSelector.getClass(newValue))) {
			// Dont need a new ui repnet(
			// uiObjectAdapter adapter =
			// getChildAdapterMapping(Integer.toString(position));
			// uiObjectAdapter adapter = getChildAdapterMapping(position);
			a.refreshValue(newValue);
		} else {
			// Delete the components
			// representing the old Object
			try {
				
				a.removeUIComponentFromParent(this);
				// deleteChildAdapter(fieldName);
				// deleteChildAdapter(position);
				int indexedPos = indexedAdapterVector.indexOf(a);
				// indexedAdapterVector.remove(a);
				// indexedVisibleAndInvisibleAdapterVector.remove(position);

				// could reuse deleted adapter for same real object, but not
				// sure if that will work in case real object componnets have
				// changed
				ObjectAdapter adapter = uiGenerator.createGraphObjectAdapter(
						// (Container)(getUIComponent()),
						this, newValue, RemoteSelector.getClass(newValue),
						position, AttributeNames.ANY_ELEMENT,
//				        computeAndMaybeSetViewObject(), false);
						computeAndMaybeSetViewObject(), false, getChildTextMode());
				adapter.setAdapterType(INDEX_TYPE);
				// setChildAdapterMapping(fieldName, adapter);
				indexedVisibleAndInvisibleAdapterVector.set(position, adapter);
				if (indexedPos != -1)
					indexedAdapterVector.set(indexedPos, adapter);
				// setChildAdapterMapping(position, adapter);
				adapter.setParentAdapter(this);
				uiGenerator.deepProcessAttributes(adapter);

				// moveGenericWidget(adapter.getGenericWidget(), position);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void removeIndexedWidgets(Container widget) {
		int numElements = widget.getComponentCount();
		if (numFeatures == 0) {
			if (getWidgetAdapter() != null)
				widget.removeAll();
			// getWidgetAdapter().emptyContainer();
		} else
			while (widget.getComponentCount() > numFeatures)
				widget.remove(widget.getComponentCount() - 1);
		/*
		 * for (int i = numFeatures; i < numElements; i++)
		 * widget.remove(numFeatures);
		 */

	}

	void removeIndexedWidgets(/* Container widget, */int from) {
		if (from == -1)
			return;
		if (getWidgetAdapter() == null)
			return;
		// int numElements = widget.getComponentCount();
		int numElements = childrenVector.size();
		if (numFeatures == 0 && from == 0) {
			// if (getWidgetAdapter() != null)
			getWidgetAdapter().removeAll();
			// widget.removeAll();
			// getWidgetAdapter().emptyContainer();
			/*
			 * } else while (widget.getComponentCount() > numFeatures + from)
			 * //widget.remove(widget.getComponentCount() - 1);
			 * getWidgetAdapter().remove (widget, widget.getComponentCount() -
			 * 1, (uiObjectAdapter)
			 * adapterMapping.elementAt(widget.getComponentCount() - 1));
			 */
		} else
			for (int i = numFeatures; i < numElements; i++)
				getWidgetAdapter().removeLast();
		/*
		 * for (int i = numFeatures; i < numElements; i++)
		 * widget.remove(numFeatures);
		 */

	}

	// Vector v not used here??
	public Object fromVector(Vector v) {
		return computeViewObject(getParentAdapter(), getRealObject());
	}

	public static boolean equals(Object o1, Object o2) {

		if (o1 == null && o2 == null)
			return true;
		if (o1 == null || o2 == null)
			return false;
		return OEMisc.equals(LoggableRegistry.getRealObject(o1), LoggableRegistry
				.getRealObject(o2));
		// return o1.equals(o2);
	}

	// int insertionIndex (Vector originalVector, VectorStructure newVector) {
	int insertionIndex(Vector originalVector, Vector newVector) {
		int retVal = 0;
		if (newVector.size() < originalVector.size())
			return -1;
		for (int i = 0; i < originalVector.size(); i++) {
			/*
			 * Object originalElement = originalVector.elementAt(i); Object
			 * newElement = newVector.elementAt(i);
			 */

			if (!equals(originalVector.elementAt(i), newVector.elementAt(i)))
				return i;
		}
		return originalVector.size();
	}

	// int deletionIndex (Vector originalVector, VectorStructure newVector) {
	int deletionIndex(Vector originalVector, Vector newVector) {
		int retVal = 0;
		if (newVector.size() > originalVector.size())
			return -1;
		for (int i = 0; i < newVector.size(); i++) {
			// if (!equals (originalVector.elementAt(i),
			// newVector.elementAt(i))) return i;
			if (!OEMisc.equals(LoggableRegistry.getRealObject(originalVector
					.elementAt(i)), LoggableRegistry.getRealObject(newVector
					.elementAt(i))))
				return i;
		}
		return newVector.size();
	}

	public void deleteIndexedChildForReuse(ObjectAdapter adapter) {
		Object realObject = adapter.getRealObject();
		if (realObject == null)
			return;
		deletedIndexedAdapters.put(realObject, adapter);
		if (indexedAdapterVector.contains(adapter))
			indexedAdapterVector.remove(adapter);
		if (indexedVisibleAndInvisibleAdapterVector.contains(adapter))
			indexedVisibleAndInvisibleAdapterVector.remove(adapter);
		if (mapping.contains(adapter))
			mapping.remove(adapter);
		if (childrenVector != null && childrenVector.contains(adapter))
			childrenVector.remove(adapter);
		if (visibleAndNonVisibleChildrenVector != null
				&& visibleAndNonVisibleChildrenVector.contains(adapter))
			visibleAndNonVisibleChildrenVector.remove(adapter);

	}

	public void setChildrenSortMode(boolean newVal) {
		setChildrenSortMode(indexedAdapterVector, newVal);

		/*
		 * for (int i = 0; i < indexedAdapterVector.size(); i++) {
		 * uiObjectAdapter child = indexedAdapterVector.elementAt(i); if (child
		 * instanceof uiClassAdapter) ((uiClassAdapter)
		 * child).setSortMode(newVal); }
		 */

	}

	public void recomputeChildrenVector() {
		int numDisplayedProperties = getNumberOfDisplayedStaticChildren();
		resetChildrenVector(numDisplayedProperties);
		for (int i = 0; i < indexedAdapterVector.size(); i++)
			childrenVector.add(indexedAdapterVector.elementAt(i));

	}

	public void recomputeVisibleAndInvisibleChildrenVector() {
		int numProperties = getNumberOfStaticChildren();
		resetVisibleAndInvisibleChildrenVector(numProperties);
		for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++)
			visibleAndNonVisibleChildrenVector
					.add(indexedVisibleAndInvisibleAdapterVector.elementAt(i));

	}

	public boolean recomputeVisibleChildrenLists() {
		boolean visibleChildrenChanged = false;
		Vector newIndexedAdapterVector = new Vector();
		// indexedAdapterVector.clear();
		for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
			ObjectAdapter compAdapter = indexedVisibleAndInvisibleAdapterVector
					.get(i);
			if (compAdapter.isVisible()) {
				if ((indexedAdapterVector.size() <= i)
						|| (indexedAdapterVector.get(i) != compAdapter))
					visibleChildrenChanged = true;
				newIndexedAdapterVector.add(compAdapter);
			}
		}
		
		if (visibleChildrenChanged) {
			indexedAdapterVector = newIndexedAdapterVector;
			recomputeChildrenVector();
			sort();
			recomputeIndices();
			//return true;
		} 
		
		
		return visibleChildrenChanged || probablyNeedsSort();

	}
	
	int previousSortSize = 0;
	Object prevSortProperty = "";
	boolean previousAscending = true; // do not really need this for now
	boolean sortDirectionChanged = false;
	public boolean explicitSort() {
		//String currentSortProperty = (String) getSortProperty();
		Object currentSortProperty = getSortProperty();
		if (
			getSortProperty().equals(prevSortProperty) &&
			//getSortProperty() == prevSortProperty &&
				previousSortSize == indexedAdapterVector.size()) {
			previousAscending = !previousAscending;
			sortDirectionChanged = true;
		}
		resetSortData();
		//previousSortSize = 0;
		return sort();
	}
	public boolean probablyNeedsSort() {
		String sortProperty = (String) getSortProperty();
		return getSort() && (
				//getSortProperty() != prevSortProperty 
				!(getSortProperty().equals(prevSortProperty))
				|| indexedAdapterVector.size() != previousSortSize );
	}
	void resetSortData() {
		previousSortSize = 0;
	}
	public boolean sort() {
		/*
		if (!getSort())
			return false;
			*/
		// just a heuristic. will set size to 0 when forced sort is requested
		//if (getSortProperty() == prevSortProperty && indexedAdapterVector.size() == previousSortSize )
		/*
		if (!probablyNeedsSort())	
			return false;
			*/
			
		// removing this for now
		// setForceRebuild(true);
		if (sortDirectionChanged) {
			OEMisc.reverse(indexedAdapterVector);
			recomputeChildrenVector();
			recomputeIndices();
			sortDirectionChanged = false;
		} else {
		setSortMode(true);		
		setChildrenSortMode(true);
		OEMisc.sort(indexedAdapterVector);
		setChildrenSortMode(false);
		recomputeChildrenVector();
		recomputeIndices();
		previousSortSize = indexedAdapterVector.size();
		prevSortProperty = getSortProperty();
		}
		return true;
	}

	// this method is not called, so we dont care about it
	// should from refer to absolute or view position?
	void deleteAdapterMapping(VirtualContainer widget, int from) {
		boolean deleteWidget = getWidgetAdapter() != null && widget != null
				&& !isAtomic();
		boolean removeAllComponents = numFeatures == 0 && from == 0
				&& deleteWidget;
		if (indexedAdapterVector == null)
			return;
		if (from < 0)
			return;
		if (removeAllComponents)
			getWidgetAdapter().removeAllProperties(widget);
		// may want a delete range method
		while (indexedAdapterVector.size() > from) {
			int lastIndex = indexedAdapterVector.size() - 1;
			ObjectAdapter elementAdapter = (ObjectAdapter) indexedAdapterVector
					.elementAt(lastIndex);
			if (!removeAllComponents && getWidgetAdapter() != null)
				getWidgetAdapter().remove(widget, lastIndex, elementAdapter);
			// cleanUp(elementAdapter);
			cleanUpForReuse(elementAdapter);
			// deletedAdapters.put(elementAdapter.getRealObject(),
			// elementAdapter);
			deleteIndexedChildForReuse(elementAdapter);
			indexedAdapterVector.removeElementAt(lastIndex);
			indexedVisibleAndInvisibleAdapterVector.remove(elementAdapter);
		}

	}

	public void removeChildrenInTables() {
		super.removeChildrenInTables();
		for (int i = 0; i < indexedAdapterVector.size(); i++) {
			ObjectAdapter elementAdapter = indexedAdapterVector.get(i);
			if (elementAdapter.getRealObject() != null)
			deletedIndexedAdapters.put(elementAdapter.getRealObject(),
					elementAdapter);
		}
		indexedAdapterVector.clear();
		indexedVisibleAndInvisibleAdapterVector.clear();

	}

	// assumes reference in visible children
	void oldDeleteAdapter(int lastIndex) {
		ObjectAdapter elementAdapter = (ObjectAdapter) indexedAdapterVector
				.elementAt(lastIndex);

		// cleanUp(elementAdapter);
		if (elementAdapter.getRealObject() != null)
			deletedIndexedAdapters.put(elementAdapter.getRealObject(),
					elementAdapter);
		elementAdapter.recursiveUnmap();
		cleanUpForReuse(elementAdapter);
		indexedAdapterVector.removeElementAt(lastIndex);
	}

	// asumes reference in all children
	void deleteAdapter(int realIndex) {
		ObjectAdapter elementAdapter = (ObjectAdapter) indexedVisibleAndInvisibleAdapterVector
				.elementAt(realIndex);
		deleteAdapterWithoutRefreshingVisible(realIndex);
		deleteMaybeVisibleAdapter(elementAdapter);
		/*
		int elementVectorIndex = elementAdapter.getVectorIndex();
		//deleteAdapterWithoutRefreshingVisible(realIndex);
		//recomputeVisibleAfterDeleteAdapter(elementVectorIndex);
		
		// cleanUp(elementAdapter);
		if (elementAdapter.getRealObject() != null)
			deletedIndexedAdapters.put(elementAdapter.getRealObject(),
					elementAdapter);
		elementAdapter.recursiveUnmap();
		cleanUpForReuse(elementAdapter);
		indexedVisibleAndInvisibleAdapterVector.removeElementAt(realIndex);
		indexedAdapterVector.remove(elementAdapter);
		childrenVector.remove(elementAdapter);
		visibleAndNonVisibleChildrenVector.remove(elementAdapter);
		
		recomputeIndices();
		if (shouldRebuildAllVisible())
			elementVectorIndex = 0;
		if (elementVectorIndex < 0) // invisible element
			return;
		for (int i = elementVectorIndex; i < indexedAdapterVector.size(); i++) {
			uiObjectAdapter adapter = indexedAdapterVector.elementAt(i);
			adapter.propagateAttributesToWidgetShell();
		}
		*/
		
			
		
	}
	
	void deleteMaybeVisibleAdapter(ObjectAdapter elementAdapter) {
		int elementVectorIndex = elementAdapter.getVectorIndex();
		if (elementVectorIndex < 0) 
			return;
		indexedAdapterVector.removeElementAt(elementVectorIndex);
		childrenVector.remove(elementAdapter);
		//recomputeIndices();
		if (shouldRebuildAllVisible())
			elementVectorIndex = 0;
		if (elementVectorIndex < 0) // invisible element
			return;
		/*
		for (int i = elementVectorIndex; i < indexedAdapterVector.size(); i++) {
			uiObjectAdapter adapter = indexedAdapterVector.elementAt(i);
			adapter.propagateAttributesToWidgetShell();
		}
		*/
		
		
	}
	// asumes reference in all children
	void deleteAdapterWithoutRefreshingVisible(int realIndex) {
		ObjectAdapter elementAdapter = (ObjectAdapter) indexedVisibleAndInvisibleAdapterVector
				.elementAt(realIndex);
		int elementVectorIndex = elementAdapter.getRealVectorIndex();

		// cleanUp(elementAdapter);
		if (elementAdapter.getRealObject() != null)
			deletedIndexedAdapters.put(elementAdapter.getRealObject(),
					elementAdapter);
		elementAdapter.recursiveUnmap();
		cleanUpForReuse(elementAdapter);
		indexedVisibleAndInvisibleAdapterVector.removeElementAt(realIndex);
		//indexedAdapterVector.remove(elementAdapter);
		//childrenVector.remove(elementAdapter);
		visibleAndNonVisibleChildrenVector.remove(elementAdapter);
		/*
		recomputeIndices();
		if (shouldRebuildAllVisible())
			realIndex = 0;
		if (elementVectorIndex < 0) // invisible element
			return;
		for (int i = elementVectorIndex; i < indexedAdapterVector.size(); i++) {
			uiObjectAdapter adapter = indexedAdapterVector.elementAt(i);
			adapter.propagateAttributesToWidgetShell();
		}
		*/
			
		
	}

	// this one assumes from is a reference in all visible and invisible
	// children
	void deleteAdapterMapping(int from) {
		boolean deleteWidget = getWidgetAdapter() != null
				&& getUIComponent() != null && !isAtomic();
		boolean removeAllComponents = numFeatures == 0 && from == 0
				&& deleteWidget;
		if (indexedVisibleAndInvisibleAdapterVector == null)
			return;
		if (from < 0)
			return;
		if (removeAllComponents)
			// getWidgetAdapter().removeAllProperties(widget);
			getWidgetAdapter().removeAll();
		// may want a delete range method
		while (indexedVisibleAndInvisibleAdapterVector.size() > from) {
			int lastIndex = indexedVisibleAndInvisibleAdapterVector.size() - 1;
			//deleteAdapter(lastIndex);
			deleteAdapterWithoutRefreshingVisible(lastIndex);

		}
		
		//recomputeVisibleAfterDeleteAdapter(from);

	}

	// this one assumed from was a reference in indexed
	void oldDeleteAdapterMapping(int from) {
		boolean deleteWidget = getWidgetAdapter() != null
				&& getUIComponent() != null && !isAtomic();
		boolean removeAllComponents = numFeatures == 0 && from == 0
				&& deleteWidget;
		if (indexedAdapterVector == null)
			return;
		if (from < 0)
			return;
		if (removeAllComponents)
			// getWidgetAdapter().removeAllProperties(widget);
			getWidgetAdapter().removeAll();
		// may want a delete range method
		while (indexedAdapterVector.size() > from) {
			int lastIndex = indexedAdapterVector.size() - 1;
			deleteAdapter(lastIndex);

		}

	}

	public static void deleteVectorElements(Vector v, int from) {
		if (v == null)
			return;
		if (from < 0)
			return;

		while (v.size() > from) {
			v.removeElementAt(v.size() - 1);

		}

	}

	public boolean isChildVisible(Object realObject) {
		if (realObject == null)
			return true;
		ObjectAdapter elementAdapter = deletedIndexedAdapters.get(realObject);
		return elementAdapter == null || elementAdapter.isVisible();

	}

	public Vector getVisibleChildren(VectorStructure vs) {
		Vector retVal = new Vector();
		for (int i = 0; i < vs.size(); i++) {
			Object nextElement = vs.elementAt(i);
			if (isChildVisible(nextElement))
				retVal.addElement(nextElement);
		}
		return retVal;
	}

	public Vector toVector(VectorStructure vs) {
		if (vs.getTargetObject() == null)
			return null;
		Vector retVal = new Vector();
		int vsSize = vs.size();
		//for (int i = 0; i < vs.size(); i++) {
		for (int i = 0; i < vsSize; i++) {
			Object nextElement = vs.elementAt(i);
			// if (isElementVisible(nextElement))
			retVal.addElement(nextElement);
		}
		return retVal;
	}
	
	public boolean shouldRebuildAllVisible() {
		return indexedAdapterVector.size() != indexedVisibleAndInvisibleAdapterVector.size();
	}
	
	public void rebuildAllVisible() {
		if (getWidgetAdapter() == null)
			return;		
		getWidgetAdapter().removeAll();
		/*
		uiGenerator.deepCreateChildren(this, numStaticChildren
				+ rebuildVisibleAndInvisibleFrom, numStaticChildren
				+ indexedVisibleAndInvisibleAdapterVector.size());
				*/
		uiGenerator.deepCreateChildren(this, false);
		// uiGenerator.deepProcessAttributes(this, false,
		// numStaticChildren + rebuildFrom, numStaticChildren +
		// indexedAdapterVector.size());
		uiGenerator.deepProcessAttributes(this);
		/*
		uiGenerator.deepProcessAttributes(this, false,
				numStaticChildren + rebuildVisibleAndInvisibleFrom,
				numStaticChildren + indexedAdapterVector.size());
				*/
		
		
	}
	
	
	public void oldRefreshValue(Object newValue1, boolean forceUpdate) {
		// System.out.println("new value");
		// System.out.println ("vector set value called" + newValue1);
		// if (newValue1 == null) return;
		// super.refreshValue(newValue1, forceUpdate);
		super.refreshValueButNotAtomic(newValue1, forceUpdate);
		// Object newV = uiGenerator.getViewObject(newValue1);
		Object newV;
		if (newValue1 != getRealObject())
			newV = computeViewObject(getParentAdapter(), newValue1);
		else
			newV = computeAndMaybeSetViewObject();

		Vector currentValue = getOriginalComponents();
		// Do a better than equals() check here
		// pd addition
		// Object viewObject = uiGenerator.getViewObject(newValue1);
		// setViewObject(viewObject);
		setViewObject(newV);
		// setRealObject(newValue);
		if (newValue1 instanceof VectorListenable
				&& newValue1 != getRealObject()) {
			System.out.println("uiVectorAdapter: adding vector listener");
			ObjectEditor.associateKeywordWithClassName(
					ObjectEditor.VECTOR_LISTENER_KEYWORD, RemoteSelector
							.getClass(newValue1));
			((VectorListenable) newValue1).addVectorListener(this);
		}
		setRealObject(newValue1);
		VectorStructure newVectorStructure = getVectorStructure();
		// VectorStructure newValue = getVectorStructure();
		// Vector newValue = getVisibleChildren(newVectorStructure);
		Vector newValue = toVector(newVectorStructure);
		// this.refreshConcreteObject(newValue1);
		this.refreshConcreteObject(newV);
		// newValue.setTarget(newValue1);

		// end pd addition
		boolean rebuildVisibleAndInvisible = false;
		boolean rebuildVisible = false;
		boolean hasChangedVisibleAndInvisible = false;
		boolean hasChangedVisible = false;		
		int numStaticChildren = super.getNumberOfStaticChildren(); // surely this should be number of displayed static children
		// boolean reassign = false;
		// boolean rebuild = true;
		int addVisibleAndInvisibleFrom = 0;
		int deleteFrom = 0;
		if (currentValue == null && newV == null) {
			return;
		} else if (currentValue == null || getForceRebuild()) {
			rebuildVisibleAndInvisible = true;

		} else if (currentValue.size() == newValue.size()) {
			if (currentValue.size() == 0)
				return;
			for (int i = 0; i < currentValue.size(); i++) {

				Object curElement = currentValue.elementAt(i);
				Object newElement = newValue.elementAt(i);

				// if (curElement == newElement) continue;
				// reassign = true;
				if ((curElement == null) || (newElement == null)) {
					rebuildVisibleAndInvisible = true;
					break;
				}
				

				if (curElement != newElement
						&& OEMisc.equals(curElement, newElement))

					continue;
				else

					hasChangedVisibleAndInvisible = true;

				
				if (!curElement.getClass().equals(newElement.getClass())) {
					rebuildVisibleAndInvisible = true;
					break;
				}
			}
		} else {
			// rebuildFrom = insertionIndex (currentValue, newValue);
			deleteFrom = deletionIndex(currentValue, newValue);
			if (deleteFrom != -1)
				addVisibleAndInvisibleFrom = deleteFrom;
			else
				addVisibleAndInvisibleFrom = insertionIndex(currentValue,
						newValue);
			rebuildVisibleAndInvisible = true;
		}

		if (rebuildVisibleAndInvisible) {
			if ((new IsEditedAdapterVisitor(this)).traverse().contains(
					new Boolean(true)))
				return;
			
			int cleanUpFrom = currentValue.size();
			if (deleteFrom != -1)
				cleanUpFrom = deleteFrom;
			else if (addVisibleAndInvisibleFrom != -1)
				cleanUpFrom = addVisibleAndInvisibleFrom;

			if (currentValue != null && currentValue.size() != 0
					&& childrenCreated) {
				
				// this will delete also the visible adapters' widgets
				deleteAdapterMapping(cleanUpFrom);
				
				// resetChildrenVector(numFeatures + cleanUpFrom);
				// this should be done later when additions also have been done
				// no recomputing until we have added vector components
				/*
				hasChangedVisible = recomputeVisibleChildrenLists();
				resetVisibleAndInvisibleChildrenVector(numFeatures
						+ cleanUpFrom);
						*/
				// do not need this as we have already called cleanup before
				// cleanUpDescendents(this);
				// Redraw the whole Vector

				// uiGenerator.uiAddVectorComponents(widget,
				// this,
				// newValue);

			}
			// System.out.println("before try");
			// try {

			// this.uiAddVectorComponents(0);
			
			/*
			if (addVisibleAndInvisibleFrom == -1) { // only deletions
				hasChangedVisible = recomputeVisibleChildrenLists();
				resetVisibleAndInvisibleChildrenVector(numFeatures
						+ cleanUpFrom);
			}
			else
			*/ 
			if (addVisibleAndInvisibleFrom != -1) {
				// this.uiAddVectorComponents(rebuildFrom, widget);
				uiGenerator.clearVisitedObjects(getRootAdapter());
				//hasChangedVisible = this.addVectorComponents(addVisibleAndInvisibleFrom);
				addVectorComponentsWithoutRefreshingVisible(addVisibleAndInvisibleFrom);
				// addVector components recomputes visible lists also
				
				//hasChangedVisible = recomputeVisibleChildrenLists();
				/*
				resetVisibleAndInvisibleChildrenVector(numFeatures
						+ cleanUpFrom);
						*/
				
				//hasChangedVisible = recomputeVisibleChildrenLists();
				

				// System.out.println("invaqlidating");

				for (int i = 0; i < addVisibleAndInvisibleFrom; i++) {
					if (!OEMisc.equals(currentValue.elementAt(i), newValue
							.elementAt(i))) {
						ObjectAdapter a = indexedVisibleAndInvisibleAdapterVector
								.get(i);
						// uiObjectAdapter a =indexedAdapterVector.get(i);
						// boolean childWasVisible = a.getUIComponent() != null;
						
						// refreshing even invisible value
						a.refreshValue(newValue.elementAt(i));
					}
				}

				if (getWidgetAdapter() != null) {
					
					if (hasChangedVisible)
					//if ((getSort() && probablyNeedsSort() || shouldRebuildAllVisible()))
					//if ((getSort() && probablyNeedsSort() || shouldRebuildAllVisible()))
						rebuildAllVisible();
					else {
						// in this case all nodes are visible and there is no sorting needed
					uiGenerator.deepCreateChildren(this, numStaticChildren
							+ addVisibleAndInvisibleFrom, numStaticChildren
							+ indexedVisibleAndInvisibleAdapterVector.size());
					// uiGenerator.deepProcessAttributes(this, false,
					// numStaticChildren + rebuildFrom, numStaticChildren +
					// indexedAdapterVector.size());
					uiGenerator.deepProcessAttributes(this, false,
							numStaticChildren + addVisibleAndInvisibleFrom,
							numStaticChildren + indexedVisibleAndInvisibleAdapterVector.size());
					}
				}
				// moving from before the check, should be a no-op
				// adding this for JTree problems
				if (!isAtomic())
					uiGenerator.deepElide(this);
				if (isAtomic() && getWidgetAdapter() != null)
					getWidgetAdapter().setUIComponentValue(newV);
			}
			if (checkIfNoVisibleChildren(this)) {
				// this.getUIFrame().maybeHideMainPanel();
				return;
			}

			
		} else if (hasChangedVisibleAndInvisible) { // number of items the same
			if (probablyNeedsSort())
				recomputeVisibleChildrenLists();
			if (shouldRebuildAllVisible()) {
				recomputeIndices();
				rebuildAllVisible();
			} else {
			ObjectAdapter a;
			ObjectAdapter redisplayedChild = null;
			for (int i = 0; i < newValue.size(); i++) {
				// a = getChildAdapterMapping(Integer.toString(i));
				// a = indexedAdapterVector.get(i);
				a = indexedVisibleAndInvisibleAdapterVector.get(i);
				// boolean childWasVisible = a.getUIComponent() != null;
				a.refreshValue(newValue.elementAt(i));
				if (redisplayedChild == null && a.getAddMeBack())
					redisplayedChild = a;

			}
			if (isAtomic() && getWidgetAdapter() != null)
				// cant need both!
				setValueOfAtomicOrPrimitive(newValue1);
			// getWidgetAdapter().setUIComponentValue(newV);
			else {
				if (checkIfNoVisibleChildren(this)) {
					// this.getUIFrame().maybeHideMainPanel();
					return;
				}
				if (redisplayedChild != null) {
					// uiGenerator.deepProcessAttributes(this, false,
					// numStaticChildren + redisplayedChild, numStaticChildren +
					// redisplayedChild + 1);
					// getWidgetAdapter().setIncrementalChildAddition(true);
					getWidgetAdapter().childComponentsAdded(true);
					redisplayedChild.propagateAttributesToWidgetShell();
					// getWidgetAdapter().setIncrementalChildAddition(false);
				}
				uiGenerator.deepElide(this.getTopAdapter());
				// uiFrame.deepElide(this.getTopAdapter(), numStaticChildren +
				// rebuildFrom, numStaticChildren + adapterMapping.size());
			}
			}

		} else { // some stuff got deleted but existing stuff did not get changed
			recomputeVisibleChildrenLists();
			if (shouldRebuildAllVisible())
				rebuildAllVisible();
		}
		/*
		 * } else { System.out.println("Non vector type passed to Vector adapter
		 * in a setValue()"); }
		 */
		if (checkIfNoVisibleChildren(this)) {
			// this.getUIFrame().maybeHideMainPanel();
			return;
		}
		/*
		 * if (isAtomic() && hasChanged) setValueOfAtomicOrPrimitive(newValue1);
		 */

		uiGenerator.deepElide(this.getTopAdapter());
		//uiFrame.validate();
	}
	public void refreshValue(Object newValue1, boolean forceUpdate) {
		
//		if (!forceUpdate && getReceivedVectorNotification()) {
		// getReceivedVectorNotification() is redundant, keeping it just in case we go back to it
		if (!forceUpdate && (getReceivedVectorNotification() || getRegisteredAsListener())) {

//			Tracer.info("Not refreshing: " + this + "  because received vector notification for:" + newValue1);
			Tracer.info(this, "Not refreshing: " + this + "  because registered as listener");

			resetReceivedVectorNotification();
			return;
		}
		// if a property change occurred go ahead and process the elements
		if (receivedNotification)
			resetReceivedNotification();
		if (isListenableString) {
			if (getWidgetAdapter() != null)
			   getWidgetAdapter().childComponentsAdded(true);
		}
		// System.out.println("new value");
		// System.out.println ("vector set value called" + newValue1);
		// if (newValue1 == null) return;
		// super.refreshValue(newValue1, forceUpdate);
		super.refreshValueButNotAtomic(newValue1, forceUpdate);
		// Object newV = uiGenerator.getViewObject(newValue1);
		Object newV;
		if (newValue1 != getRealObject()) {
			
			newV = computeViewObject(getParentAdapter(), newValue1);
			
		} else
			newV = computeAndMaybeSetViewObject();

		Vector currentValue = getOriginalComponents();
		// Do a better than equals() check here
		// pd addition
		// Object viewObject = uiGenerator.getViewObject(newValue1);
		// setViewObject(viewObject);
		setViewObject(newV);
		// setRealObject(newValue);
		if (newValue1 instanceof VectorListenable
				&& newValue1 != getRealObject()) {
			System.out.println("uiVectorAdapter: adding vector listener");
			ObjectEditor.associateKeywordWithClassName(
					ObjectEditor.VECTOR_LISTENER_KEYWORD, RemoteSelector
							.getClass(newValue1));
			((VectorListenable) newValue1).addVectorListener(this);
		}
		setRealObject(newValue1);
		VectorStructure newVectorStructure = getVectorStructure();
		// VectorStructure newValue = getVectorStructure();
		// Vector newValue = getVisibleChildren(newVectorStructure);
		Vector newValue = toVector(newVectorStructure);
		// this.refreshConcreteObject(newValue1);
		this.refreshConcreteObject(newV);
		// newValue.setTarget(newValue1);

		// end pd addition
		boolean rebuildVisibleAndInvisible = false;
		boolean rebuildVisible = false;
		boolean hasChangedVisibleAndInvisible = false;
		boolean hasChangedVisible = false;		
		int numStaticChildren = super.getNumberOfStaticChildren(); // surely
																	// this
																	// should be
																	// number of
																	// displayed
																	// static
																	// children
		// boolean reassign = false;
		// boolean rebuild = true;
		int rebuildVisibleAndInvisibleFrom = 0;
		int deleteFrom = -1;
		int addFrom = -1;
		if (currentValue == null && newV == null) {
			return;
		} else if (currentValue == null || getForceRebuild()) {
			rebuildVisibleAndInvisible = true;

		} else if (currentValue.size() == newValue.size()) {
			if (currentValue.size() == 0)
				return;
			for (int i = 0; i < currentValue.size(); i++) {

				Object curElement = currentValue.elementAt(i);
				Object newElement = newValue.elementAt(i);

				// if (curElement == newElement) continue;
				// reassign = true;
				if (((curElement == null) || (newElement == null)) 
						&& curElement != newElement) {
					rebuildVisibleAndInvisible = true;
					break;
				}
				if (curElement != newElement && // this means we could not serialize the value
						OEMisc.equals(curElement, newElement))
							
//				if (/*!forceUpdate* && */ (curElement == newElement
//					|| OEMisc.equals(curElement, newElement)))
//				if (curElement != newElement
//						&& OEMisc.equals(curElement, newElement))

					continue;
				else

					hasChangedVisibleAndInvisible = true;

				
				if ( curElement != null && !curElement.getClass().equals(newElement.getClass())) {
					rebuildVisibleAndInvisible = true;
					hasChangedVisibleAndInvisible = false;
					break;
				}
			}
		} else {
			// rebuildFrom = insertionIndex (currentValue, newValue);
			deleteFrom = deletionIndex(currentValue, newValue);
			if (deleteFrom != -1)
				rebuildVisibleAndInvisibleFrom = deleteFrom;
			else {
				rebuildVisibleAndInvisibleFrom = insertionIndex(currentValue,
						newValue);
				addFrom = rebuildVisibleAndInvisibleFrom;
			}
			rebuildVisibleAndInvisible = true;
		}
		boolean onlyDeletions = addFrom == -1 && newValue.size() == deleteFrom;
		boolean onlyAdditions = deleteFrom == -1 && addFrom == currentValue.size();
		boolean onlyReplacements = !rebuildVisibleAndInvisible;

		if (rebuildVisibleAndInvisible) {
			invalidateAncestorLeafAdapters();
			if ((new IsEditedAdapterVisitor(this)).traverse().contains(
					new Boolean(true)))
				return;
			
			int cleanUpFrom = currentValue.size();
			if (deleteFrom != -1)
				cleanUpFrom = deleteFrom;
			else if (rebuildVisibleAndInvisibleFrom != -1)
				cleanUpFrom = rebuildVisibleAndInvisibleFrom;

			if (currentValue != null && currentValue.size() != 0
					&& childrenCreated) {
				
				// this will delete also the visible adapters' widgets
				// visible will not change
				deleteAdapterMapping(cleanUpFrom);
				
				

			}
			
			if (rebuildVisibleAndInvisibleFrom != -1) {
				// this.uiAddVectorComponents(rebuildFrom, widget);
				
				// clearing more than necessary, ignoring from
				(new ClearVisitedNodeAdapterVisitor(this)).traverseNonAtomicChildrenContainers();
		
				// hasChangedVisible =
				// this.addVectorComponents(addVisibleAndInvisibleFrom);
				addVectorComponentsWithoutRefreshingVisible(rebuildVisibleAndInvisibleFrom);
				
				for (int i = 0; i < rebuildVisibleAndInvisibleFrom; i++) {
					if (!OEMisc.equals(currentValue.elementAt(i), newValue
							.elementAt(i))) {
						ObjectAdapter a = indexedVisibleAndInvisibleAdapterVector
								.get(i);
						// uiObjectAdapter a =indexedAdapterVector.get(i);
						// boolean childWasVisible = a.getUIComponent() != null;
						
						// refreshing even invisible value
						a.refreshValue(newValue.elementAt(i));
					}
				}

				
			
				if (isAtomic() && getWidgetAdapter() != null)
					getWidgetAdapter().setUIComponentValue(newV);
			}
			if (checkIfNoVisibleChildren(this)) {
				// this.getUIFrame().maybeHideMainPanel();
				return;
			}

			
		} else if (hasChangedVisibleAndInvisible) { 
			
			ObjectAdapter a;
			ObjectAdapter redisplayedChild = null;
			for (int i = 0; i < newValue.size(); i++) {
				// a = getChildAdapterMapping(Integer.toString(i));
				// a = indexedAdapterVector.get(i);
				a = indexedVisibleAndInvisibleAdapterVector.get(i);
				// boolean childWasVisible = a.getUIComponent() != null;
				a.refreshValue(newValue.elementAt(i));
				if (redisplayedChild == null && a.getAddMeBack())
					redisplayedChild = a;

			}
			// this stuff is somehwat kludgy
			if (redisplayedChild != null) {
				
				getWidgetAdapter().childComponentsAdded(true);
				redisplayedChild.propagateAttributesToWidgetShell();
				// getWidgetAdapter().setIncrementalChildAddition(false);
			}
			
		}
		/*
		if (isAtomic() && getWidgetAdapter() != null)
			// cant need both!
			setValueOfAtomicOrPrimitive(newValue1);
		else {
			
			uiGenerator.deepElide(this.getTopAdapter());			
		}
		*/
		//
		// orginall no check for topDynamic, will bogey's stuff was not refreshing
		//if (getUIFrame().isExplicitRefresh()
		if (getUIFrame().isFullRefresh() && isTopDynamic())
			refreshIndexed(newValue1, forceUpdate);
		else
		
		refreshVisible(newV, onlyAdditions, onlyDeletions, onlyReplacements, addFrom, deleteFrom, rebuildVisibleAndInvisibleFrom);
		//refreshVisible(newValue1, onlyAdditions, onlyDeletions, onlyReplacements, addFrom, deleteFrom, rebuildVisibleAndInvisibleFrom);
			
	}
	void refreshChildrenWidgets() {
		ObjectAdapter a;
		VectorStructure newVectorStructure = getVectorStructure();
		// VectorStructure newValue = getVectorStructure();
		// Vector newValue = getVisibleChildren(newVectorStructure);
		Vector newValue = toVector(newVectorStructure);
	for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
		// a = getChildAdapterMapping(Integer.toString(i));
		// a = indexedAdapterVector.get(i);
		a = indexedVisibleAndInvisibleAdapterVector.get(i);
		// boolean childWasVisible = a.getUIComponent() != null;
		a.refreshValue(newValue.elementAt(i));
	}
		
	}
	
	void propagateAttributesToChildrenWidgetShell (int fromVectorIndex) {
		for (int i = fromVectorIndex; i < indexedAdapterVector.size(); i++) {
			ObjectAdapter adapter = indexedAdapterVector.elementAt(i);
			adapter.propagateAttributesToWidgetShell();
		}
	}
	void propagateAttributesToChildrenWidgetAdapters (int fromVectorIndex) {
		for (int i = fromVectorIndex; i < indexedAdapterVector.size(); i++) {
			ObjectAdapter adapter = indexedAdapterVector.elementAt(i);
			adapter.propagateAttributesToWidgetAdapter();
		}
	}
	// widgets have already been deleted
	void deleteVisible (int deleteFrom) {
		int numStaticChildren = getNumberOfDisplayedStaticChildren();
		int minDeleteIndex = indexedAdapterVector.size();
		 for (int i = indexedAdapterVector.size() - 1; i >=0  ; i--) {
			 ObjectAdapter compAdapter = indexedAdapterVector.get(i);
			 if (compAdapter.getRealVectorIndex() >= deleteFrom) {
				 indexedAdapterVector.remove(i);
				 childrenVector.remove(i + numStaticChildren);
				 minDeleteIndex = Math.min(i, minDeleteIndex);
			 }
				 
		 }
		 recomputeDisplayIndices(minDeleteIndex);
		 propagateAttributesToChildrenWidgetShell(minDeleteIndex);
	}
	// widgets have to be added
	void addVisible (int addFrom) {
		int numStaticChildren = getNumberOfDisplayedStaticChildren();
		
		int oldIndexedAdapterSize = indexedAdapterVector.size();
		addVisibleWithoutDisplayingThem(addFrom);
		/*
		 for (int i = addFrom; i< indexedVisibleAndInvisibleAdapterVector.size(); i++) {
			 uiObjectAdapter compAdapter = indexedVisibleAndInvisibleAdapterVector.get(i);
			 if (!compAdapter.isVisible()) {
			 indexedAdapterVector.add(compAdapter);
			 childrenVector.add(compAdapter);
			 }
		 }
		 */
		 uiGenerator.deepCreateChildren(this, numStaticChildren
					+ oldIndexedAdapterSize, numStaticChildren
					+ indexedAdapterVector.size());
			// uiGenerator.deepProcessAttributes(this, false,
			// numStaticChildren + rebuildFrom, numStaticChildren +
			// indexedAdapterVector.size());
			uiGenerator.deepProcessAttributes(this, false,
					numStaticChildren + oldIndexedAdapterSize,
					numStaticChildren + indexedAdapterVector.size());
	}
	void addVisibleWithoutDisplayingThem (int addFrom) {
		int numStaticChildren = getNumberOfDisplayedStaticChildren();
		//int oldIndexedAdapterSize = indexedAdapterVector.size();
		 for (int i = addFrom; i< indexedVisibleAndInvisibleAdapterVector.size(); i++) {
			 ObjectAdapter compAdapter = indexedVisibleAndInvisibleAdapterVector.get(i);
			 if (compAdapter.isVisible()) {
			 indexedAdapterVector.add(compAdapter);
			 childrenVector.add(compAdapter);
			 compAdapter.setVectorIndex(i);
			 compAdapter.setIndex(i + numStaticChildren);
			 if (compAdapter.isReadOnly())
				 compAdapter.setUneditable();
			 resetSortData();
			 }
		 }
		 
		 
	}
	@Override
	public   boolean redoVisibleChildren() {
		boolean retVal = super.redoVisibleChildren();
		if (retVal)
			return retVal;
		return redoVisibleIndexedChildren();
		//refreshIndexed(getRealObject());
	}
	void refreshIndexed(Object newValue1, boolean forceUpdate) {
		if (!isTopDynamic())
			return;
		int numStaticChildren = getNumberOfDisplayedStaticChildren();
		
		//uiObjectAdapter topRowAdapter = getTopFlatTableRow();
		boolean removeStaticChildren = false;
		if (isFlatTableRow())
			removeStaticChildren = true;
		//this would remove static components also
		///if (getWidgetAdapter() != null) {
		//	getWidgetAdapter().removeAll();
		//}
		
		for (int i = indexedAdapterVector.size() - 1; i >= 0 ; i--) {
			ObjectAdapter elementAdapter = indexedAdapterVector.get(i);
			cleanUpForReuse(elementAdapter);
			
			childrenVector.remove(i + numStaticChildren); // for some reason this was commented out
			/*
			getWidgetAdapter().remove(elementAdapter);
			if (elementAdapter instanceof uiShapeAdapter)
				elementAdapter.cleanUpForReuse();
				*/
			
		}
		if (removeStaticChildren) {
			for (int i = childrenVector.size() - 1; i >= 0; i--) {
				ObjectAdapter elementAdapter = childrenVector.get(i);
				cleanUpForReuse(elementAdapter);
				
			}
			//childrenVector.removeAllElements();
		}
		
		//getUIFrame().validate();
			
		indexedAdapterVector.removeAllElements();
		//remove(childrenVector, numStaticChildren);
		//getWidgetAdapter().removeAll();
		int displayIndex = 0;
		for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
			ObjectAdapter elementAdapter = indexedVisibleAndInvisibleAdapterVector.get(i);
			elementAdapter.recomputeAttributes();
			elementAdapter.refreshValue(elementAdapter.getRealObject(), forceUpdate);
			if (elementAdapter.isVisible()) {
				childrenVector.add(elementAdapter);
				elementAdapter.setDisposed(false); // why are we readding - dont know
				// we are adding the adapter but not its children. 
				// clean up before removed children also
				// but did not remove from mapping. so uiGenerator will add the child back.
				indexedAdapterVector.add(elementAdapter);
				elementAdapter.setIndex(displayIndex + numStaticChildren);
				elementAdapter.setVectorIndex(displayIndex);
				// looks like the only observer we remove is this, so this is the only one we will add back
				// to make our shapes models work
//				maybeAddObserver(elementAdapter.getRealObject(), elementAdapter);
				displayIndex++;
				
			}
		}
		/*
		resetSortData();
		if (getSort())
		sort();
		*/
		invalidateAncestorLeafAdapters();
//		(new ClearVisitedNodeAdapterVisitor(this)).traverseNonAtomicChildrenContainers();
		uiGenerator.deepCreateChildren(this, false);
		// uiGenerator.deepProcessAttributes(this, false,
		// numStaticChildren + rebuildFrom, numStaticChildren +
		// indexedAdapterVector.size());
		resetSortData();
		if (getSort())
		sort();
		/*
		if (uiFrame.isExplicitRefresh() && !isTopAdapter()) // will get refreshed at top level
			return;
			*/
		uiGenerator.deepProcessAttributes(this);
		if (isAtomic() && getWidgetAdapter() != null)
			// cant need both!
			setValueOfAtomicOrPrimitive(newValue1);
		else {
			// deep process attributes does this
			//uiGenerator.deepElide(this.getTopAdapter());
			
		}
		if (getVectorIndex() == 0 && getParentAdapter() != null && getParentAdapter().getWidgetAdapter() != null)
			getParentAdapter().getWidgetAdapter().refillColumnTitle(this);
			
		
		//getUIFrame().validate();
		
		
	}
	boolean redoVisibleIndexedChildren() {

		int numStaticChildren = getNumberOfDisplayedStaticChildren();
		
		
		int displayIndex = 0;
		boolean added = false;
		boolean deleted = false;
		for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
			ObjectAdapter elementAdapter = indexedVisibleAndInvisibleAdapterVector.get(i);
			elementAdapter.recomputeAttributes();
			boolean isVisible = elementAdapter.isVisible();
			boolean wasVisible = indexedAdapterVector.contains(elementAdapter);
			if (isVisible == wasVisible) {
				displayIndex++;
				continue;
			}
			if (isVisible) {
				childrenVector.insertElementAt(elementAdapter, displayIndex + numStaticChildren);				
				indexedAdapterVector.insertElementAt(elementAdapter, displayIndex);
				if (getWidgetAdapter() != null)
					getWidgetAdapter().add(elementAdapter);
				displayIndex++;
				added = true;
				
			} else {
				childrenVector.remove(elementAdapter);
				indexedAdapterVector.remove(elementAdapter);
				if (getWidgetAdapter() != null)
					getWidgetAdapter().remove(elementAdapter);
				deleted = true;
			
			}
		}
		if (!added && !deleted)
			return false;
		resetSortData();
		if (getSort())
		sort();
		recomputeIndices();
		invalidateAncestorLeafAdapters();
		CompositeAdapter rebuildFromAdapter = getTopFlatTableRow();
		
		/*
		uiGenerator.deepCreateChildren(this);
		// uiGenerator.deepProcessAttributes(this, false,
		// numStaticChildren + rebuildFrom, numStaticChildren +
		// indexedAdapterVector.size());
		uiGenerator.deepProcessAttributes(this);
		*/
		if (isAtomic() && getWidgetAdapter() != null)
			// cant need both!
			setValueOfAtomicOrPrimitive(getRealObject());
		else {
			
			uiGenerator.deepElide(this.getTopAdapter());
			/*
			if (indexedAdapterVector.size() > 0 && indexedAdapterVector.get(0) instanceof uiContainerAdapter)
				getWidgetAdapter().refillColumnTitle((uiContainerAdapter) indexedAdapterVector.get(0));
				*/
		}
		
		if (getWidgetAdapter() != null) {
			if (isFlatTableRow())
				getWidgetAdapter().rebuildPanel();
			else if (added)
				getWidgetAdapter().childComponentsAdded(numStaticChildren > 0);
			//else 
			if (rebuildFromAdapter != null && rebuildFromAdapter.getWidgetAdapter() != null) {
				WidgetAdapterInterface ancestorWidgetAdapter = rebuildFromAdapter.getWidgetAdapter();
				
				ancestorWidgetAdapter.add(this);
				//invalidateAncestorLeafAdapters();
				ancestorWidgetAdapter.descendentUIComponentsAdded();			
			}
			
		}
			
		if (getVectorIndex() == 0 && getParentAdapter() != null && getParentAdapter().getWidgetAdapter() != null) {
			//getParentAdapter().getWidgetAdapter().rebuildPanel();
			getParentAdapter().getWidgetAdapter().refillColumnTitle(this);
			
		}
			
		return true;
		//getUIFrame().validate();
		
		
	}
	
	public void refreshVisible(Object newValue1, boolean onlyAdditions, 
			boolean onlyDeletions, 
			boolean onlyReplacements,
			int addFrom,
			int deleteFrom,
			int rebuildFrom) {
		if (!onlyReplacements) { 
			if (onlyDeletions) {
				deleteVisible(deleteFrom);
				
			} else if (onlyAdditions) {
				addVisible(addFrom);
			} else {
				deleteVisible(rebuildFrom);
				addVisible(rebuildFrom);
			}
			
		}
		/*
		if (!isAtomic())
			uiGenerator.deepElide(this);
		*/
		if (isAtomic() && getWidgetAdapter() != null)
			// cant need both!
			setValueOfAtomicOrPrimitive(newValue1);
		else {
			
			uiGenerator.deepElide(this.getTopAdapter());			
		}
		//getUIFrame().validate();
	}
	
	void remove(List vector, int fromIndex) {
		for (int i = vector.size() -1; i >= fromIndex ; i--) {
			vector.remove(i);
		}
		
	}
	
	

	public boolean isDynamic() {
		return true;
	}

	// this one assumes
	public Object oldGetValue() {
		// this is the buffered value - should probably not be vector structure
		// System.out.println("get value called");

		// return getRealObject();

		// if (getViewObject() instanceof Vector) {
		Vector v = new Vector();
		for (int i = 0; i < indexedAdapterVector.size(); i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedAdapterVector.get(i);
			Object obj = a.getValue();
			v.addElement(obj);
		}
		return v;
		// }
		// return null;

	}

	public Object getValue() {
		// this is the buffered value - should probably not be vector structure
		// System.out.println("get value called");

		// return getRealObject();

		// if (getViewObject() instanceof Vector) {
		Vector v = new Vector();
		for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			// uiObjectAdapter a = indexedAdapterVector.get(i);
			ObjectAdapter a = indexedVisibleAndInvisibleAdapterVector.get(i);
			Object obj = a.getValue();
			v.addElement(obj);
		}
		return v;
		// }
		// return null;

	}

	public boolean isChildReadable(ObjectAdapter adapter) {
		if (adapter.getRealVectorIndex() == -1)
			return super.isChildReadable(adapter);
		return getVectorStructure().validateElementAt(
				adapter.getRealVectorIndex());

	}

	public boolean isChildWriteable(ObjectAdapter adapter) {
		return true;

	}

	public Vector getOriginalComponents() {
		// System.out.println("get value called");

		// return getRealObject();

		// if (getViewObject() instanceof Vector) {
		if (getViewObject() == null)
			return null;
		Vector v = new Vector();
		for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedVisibleAndInvisibleAdapterVector.get(i);
			// Object obj = a.getOriginalValue();
			Object obj = a.getPreviousRealObject();
			v.addElement(obj);
		}
		return v;
		// }
		// return null;

	}

	public Vector oldgetOriginalComponents() {
		// System.out.println("get value called");

		// return getRealObject();

		// if (getViewObject() instanceof Vector) {
		Vector v = new Vector();
		for (int i = 0; i < indexedAdapterVector.size(); i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedAdapterVector.get(i);
			// Object obj = a.getOriginalValue();
			Object obj = a.getPreviousRealObject();
			v.addElement(obj);
		}
		return v;
		// }
		// return null;

	}

	// Abstract method implementation for naming
	// mechanism.

	public ObjectAdapter getChildAdapterMapping(String fieldName) {
		ObjectAdapter retVal;
		try {
			int index = Integer.parseInt(fieldName);
			if (index < 0 || index >= indexedAdapterVector.size())
				return null;
			return (ObjectAdapter) indexedAdapterVector.elementAt(index);
		} catch (NumberFormatException e) {
			retVal = super.getChildAdapterMapping(fieldName);
			if (retVal == null) {
				System.out
						.println("Illegal argument passed to uiVectorAdapter:getChildAdapterMapping():"
								+ fieldName);
				// return super.getChildAdapterMapping(fieldName);
				return null;
			} else
				return retVal;
		}
	}

	public ObjectAdapter getChildAdapterMapping(int index) {
		ObjectAdapter retVal;
		try {

			if (index < 0 || index >= indexedAdapterVector.size())
				return null;
			return (ObjectAdapter) indexedAdapterVector.elementAt(index);
		} catch (Exception e) {
			return null;
		}
	}

	public void setChildAdapterMapping(String fieldName, ObjectAdapter adapter) {
		try {
			/*
			 * if (adapter instanceof uiVectorAdapter) {
			 * System.out.println("adding " + adapter + "to " + this); }
			 */
			int index = Integer.parseInt(fieldName);
			// setIndexedChildAdapterMapping (index, adapter);
			indexedAdapterVector.insertElementAt(adapter, index);
			// indexedAdapterVector.insertElementAt(adapter, index);
		} catch (NumberFormatException e) {
			super.setChildAdapterMapping(fieldName, adapter);
			// System.out.println("Illegal argument passed to
			// uiVectorAdapter:getChildAdapterMapping():" + fieldName);
		}
	}

	public void setVisibleOrInvisiblleChildAdapterMapping(String fieldName,
			ObjectAdapter adapter) {
		try {
			/*
			 * if (adapter instanceof uiVectorAdapter) {
			 * System.out.println("adding " + adapter + "to " + this); }
			 */
			int index = Integer.parseInt(fieldName);
			// setIndexedChildAdapterMapping (index, adapter);
			indexedVisibleAndInvisibleAdapterVector.insertElementAt(adapter,
					index);
			// indexedAdapterVector.insertElementAt(adapter, index);
		} catch (NumberFormatException e) {
			super.setChildAdapterMapping(fieldName, adapter);
			// System.out.println("Illegal argument passed to
			// uiVectorAdapter:getChildAdapterMapping():" + fieldName);
		}
	}

	public void setIndexedChildAdapterMapping(int index, ObjectAdapter adapter) {
		try {
			/*
			 * if (adapter instanceof uiVectorAdapter) {
			 * System.out.println("adding " + adapter + "to " + this); }
			 */
			if (adapter.getRealObject() != null)
				indexedAdapterVector.insertElementAt(adapter, index);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// System.out.println("Illegal argument passed to
			// uiVectorAdapter:getChildAdapterMapping():" + fieldName);
		}
	}

	public String getChildAdapterRealIndex(ObjectAdapter adapter) {

		// System.out.println("Searching for adapter: " + adapter.getID());
		/*
		 * for (int i = 0; i < adapterMapping.size(); i++) {
		 * System.out.println(((uiObjectAdapter)adapterMapping.elementAt(i)).getID());
		 *  }
		 */
		// int index = indexedAdapterVector.indexOf(adapter);
		if (adapter.getAdapterType() == PROPERTY_TYPE) {
			return super.getChildAdapterRealIndex(adapter);
		}
		int index = adapter.getRealVectorIndex();
		if (index < 0) // adapter got changed underneath
			index = indexOf(adapter.getRealObject());
		if (index < 0)
			index = adapter.getIndex();
		return Integer.toString(index);
		// return Integer.toString(adapterMapping.indexOf(adapter));
		// return Integer.toString(indexOf (adapterMapping, adapter));
	}

	public int indexOf(Object obj) {
		for (int i = 0; i < indexedAdapterVector.size(); i++) {
			ObjectAdapter child = (ObjectAdapter) indexedAdapterVector
					.elementAt(i);
			if (child.getRealObject() == obj)
				return i;
		}
		return -1;
	}

	public ObjectAdapter getChildAdapterAtIndex(String key) {
		return (ObjectAdapter) indexedAdapterVector.elementAt(Integer
				.parseInt(key));
	}
	public  ObjectAdapter getVisibleOrInvisibleChildAdapterAtRealIndex(String key) {
		int intFromKey ;
		try {
			intFromKey = Integer
			.parseInt(key);
			return (ObjectAdapter) indexedVisibleAndInvisibleAdapterVector.elementAt(intFromKey);
			
		} catch (Exception e) {
			//intFromKey = -1;
		}
		//return super.getChildAdapterAtIndex(key);
		return super.getVisibleOrInvisibleChildAdapterAtRealIndex(key);
		/*
		uiObjectAdapter staticAdapter = super.getChildAdapterAtIndex(key);
		if (staticAdapter != null)
			return staticAdapter;
		return (uiObjectAdapter) indexedVisibleAndInvisibleAdapterVector.elementAt(Integer
				.parseInt(key));
				*/
	  }

	// not called
	public void deleteChildAdapter(String fieldName) {
		int position;
		try {
			position = Integer.parseInt(fieldName);
			indexedAdapterVector.removeElementAt(position);
		} catch (NumberFormatException e) {
		}
	}

	public void deleteChildAdapter(int position) {
		// int position;
		try {

			indexedAdapterVector.removeElementAt(position);
		} catch (NumberFormatException e) {
		}
	}

	public Enumeration getChildAdapters() {
		return getChildren();
		// return adapterMapping.elements();
	}

	// Method from uiReplaceableChildren
	public void oldReplaceAttributedObject(ObjectAdapter child,
			Object newValue) {
		int position = indexedAdapterVector.indexOf(child);
		changeComponent(position, newValue);
	}

	// Method from uiReplaceableChildren
	public void replaceAttributedObject(ObjectAdapter child, Object newValue) {
		int position = indexedVisibleAndInvisibleAdapterVector.indexOf(child);
		changeComponent(position, newValue);
	}

	public void deleteExistingChildren() {
		super.deleteExistingChildren();
		while (indexedAdapterVector.size() > 0) {
			int lastIndex = indexedAdapterVector.size() - 1;
			ObjectAdapter elementAdapter = (ObjectAdapter) indexedAdapterVector
					.elementAt(lastIndex);
			// VirtualComponent child = null;
			// VirtualContainer parent = null;
			/*
			 * if (elementAdapter.getRealObject() != null)
			 * deletedAdapters.put(elementAdapter.getRealObject(),
			 * elementAdapter);
			 */
			deleteIndexedChildForReuse(elementAdapter);
			// what aboutt cdleanUpForReuse?
// child is deleted above
//			indexedAdapterVector.removeElementAt(lastIndex);
			// parent.add(child);
		}

	}

	public void createChildrenBasic() {
		/*
		 * if (this.childrenCreated) return;
		 */
		deleteExistingChildren();
		addClassComponents(false);
		addVectorComponents(0);
		/*
		 * if (!isTopAdapter()) parent.descendentsCreated(); else
		 * uiGenerator.deepProcessAttributes(this);
		 */
		// uiGenerator.deepProcessAttributes(this);
		// recursivelyCheckIfNoVisibleChildren(this);
		/*
		 * if (checkIfNoVisibleChildren(this)) {
		 * //this.getUIFrame().maybeHideMainPanel(); return; }
		 */
	}

	public void uiAddVectorComponentsPropagating(int from) {
		addVectorComponents(from);
		if (!isTopAdapter())
			parent.descendentsCreated();
		else
			uiGenerator.deepProcessAttributes(this, false);
		// recursivelyCheckIfNoVisibleChildren(this);
	}

	transient static boolean showVectorComponentLabels = true;
	/*
	 * // not referenced method public int vectorSize(Object v) { Object[]
	 * params = {}; try { Object retVal = sizeMethod.invoke(v, params); return
	 * ((Integer) retVal).intValue(); } catch (Exception e) { return 0; } }
	 */
	/*
	 * // these are not referenced either public Object vectorElementAt(Object
	 * v, int i) { return vectorElementAt(elementAtMethod, v, i); } public
	 * static Object vectorElementAt(Method elementAtMethod, Object v, int i) {
	 * if (elementAtMethod == null) return null; try {
	 * 
	 * Object[] params = {new Integer(i)}; return elementAtMethod.invoke(v,
	 * params); } catch (Exception e) { System.out.println(e); return null; }
	 * 
	 * 
	 * //Object obj1 = v.elementAt(i); }
	 */
	boolean homogeneousVector = true;

	/*
	 * public boolean isHomogeneous() { return
	 * ClassDescriptorCache.toBoolean(getTempAttributeValue(HOMOGENEOUS));
	 * //return homogeneousVector; }
	 * 
	 * public void setHomogeneous(boolean newVal) {
	 * this.setTempAttributeValue(HOMOGENEOUS, new Boolean(newVal)); } public
	 * void setChildrenHorizontal (boolean newValue) {
	 * this.setTempAttributeValue(CHILDREN_HORIZONTAL, new Boolean(newValue)); }
	 * public boolean childrenHorizontal() { return
	 * ClassDescriptorCache.toBoolean(
	 * getTempAttributeValue(CHILDREN_HORIZONTAL)); //return homogeneousVector; }
	 */
	public void processDirection() {
		// String direction = (String)
		// this.getTempAttributeValue(AttributeNames.DIRECTION);
		String direction = getDirection();
		boolean alignHorizontal = direction.equals("horizontal");
		boolean square = direction.equals("square");
		boolean box = direction.equals(AttributeNames.BOX);
		// Container containW = (Container) getWidgetAdapter().getUIComponent();
		// this.getWidgetAdapter().childComponentsAdded();
		if (getWidgetAdapter() instanceof CommandAndStatePanelAdapter) {
			// getWidgetAdapter().processDirection();
			return;
		}
		// this makes no sense, why should tabbed pane require direction
		/*
		 * if (containW instanceof JTabbedPane ) return;
		 * 
		 * if ( alignHorizontal) {
		 * 
		 * containW.setLayout(new uiGridLayout(1, vSize + numFeatures,
		 * uiGridLayout.DEFAULT_HGAP, 0));
		 * this.setTempAttributeValue(AttributeNames.DIRECTION, "horizontal"); }
		 * else { this.setTempAttributeValue(AttributeNames.DIRECTION,
		 * AttributeNames.VERTICAL); //containW.setLayout(new
		 * uiGridLayout(features.length, 1));
		 * 
		 * //if (foundUnlabeledComposite && v.size() > 1) if
		 * (foundUnlabeledComposite && vSize > 1) //containW.setLayout(new
		 * uiGridLayout(v.size(), 1, 0, uiGridLayout.DEFAULT_VGAP));
		 * containW.setLayout(new uiGridLayout(vSize + numFeatures, 1, 0,
		 * uiGridLayout.DEFAULT_VGAP)); else //containW.setLayout(new
		 * uiGridLayout(v.size(), 1)); containW.setLayout(new uiGridLayout(vSize +
		 * numFeatures, 1)); }
		 * 
		 */
		// this.getWidgetAdapter().childComponentsAdded();
	}

	boolean horizontalChildren = true;
	int vSize = 0;

	/*
	 * public void uiAddVectorComponents(int from) { uiAddVectorComponents
	 * (from, null); }
	 */
	// alas, this is called only from one place
	public void addDynamicChildInTables(int index, ObjectAdapter a) {
		// setChildAdapterMapping(Integer.toString(index), a);
		int adjustedIndex = Math.min(index, indexedVisibleAndInvisibleAdapterVector.size());
		// setIndexedChildAdapterMapping(adjustedIndex, a);
		//indexedAdapterVector.insertElementAt(a, adjustedIndex);
		indexedVisibleAndInvisibleAdapterVector.insertElementAt(a, adjustedIndex);
		// a.setIndex(numFeatures + index);
		// a.setVectorIndex(index);
		// setChildAdapterMapping(adjustedIndex + getNumberOfStaticChildren(),
		// a);
		/*
		setChildAdapterMapping(adjustedIndex
				+ getNumberOfDisplayedStaticChildren(), a);
				*/
				

	}

	public boolean setDynamicVisibleChildIndices(int index, ObjectAdapter a) {
		// maybe we also need dynamic Index
		// setChildAdapterMapping(Integer.toString(index), a);
		if (!a.isVisible())
			return false;
		a.setAdapterType(INDEX_TYPE);
		a.setIndex(numFeatures + index);
		a.setVectorIndex(index);
		return true;
		// setChildAdapterMapping(a);

	}
	@Override
	 public  void addComponents() {
		super.addComponents();
		addVectorComponents(0);
		
	}

	// boolean forceRebuild = false;
	public void addVectorComponents(int from) {
		if (isListenableString) // this seems like somen kind of a hack
			return;
		addVectorComponentsWithoutRefreshingVisible(from);
		addVisibleWithoutDisplayingThem(from);
		/*
		// this.childrenCreated = true;
		// System.out.println("add vector in vector adapter:" + this);
		
		uiContainerAdapter adaptor = this;
		
		Object obj = null;
		if (!ObjectEditor.shareBeans()) {
			// obj = getViewObject(getRealObject());
			obj = getViewObject();
		} else {

			// obj = ObjectRegistry.logReadMethodInvocation(new
			// UnivMethodInvocation(parent.getViewObject(),getPropertyReadMethod(),null));
			obj = get();
		}
		// System.out.println("new value: " + obj);
		adaptor.setViewObject(obj);
		
		if (obj == null)
			return false;
		// Class inputClass = obj.getClass();
		// ClassProxy inputClass = RemoteSelector.getClass(obj);
		ClassProxy inputClass = ClassDescriptor.getTargetClass(obj);
		
		VectorStructure v = getVectorStructure();
		if (v != null) {
			
			vSize = v.size();
			
			// int displayIndex = 0; // this seems wrong
			// maybe this should be an instance variable
			int displayIndex = from;
			for (int i = from; i < vSize; i++) {
				

				Object obj1 = v.elementAt(i);
				// Object obj1 = this.vectorElementAt(v, i);

				uiObjectAdapter a;
				// System.out.println("add components");
				if (obj1 == null)
					// a = uiGenerator.uiAddComponents(containW, adaptor, obj1,
					// uiMethodInvocationManager.OBJECT_CLASS, i,
					// Integer.toString(i), obj, false);
					a = uiGenerator.createObjectAdapter(
							// containW,
							adaptor, obj1,
							uiMethodInvocationManager.OBJECT_CLASS_PROXY, i,
							ClassDescriptor.ELEMENT_NAME, obj, false);
				else {
					// a = uiGenerator.uiAddComponents(containW, adaptor, obj1,
					// obj1.getClass(), i, Integer.toString(i), obj, false);
					a = deletedIndexedAdapters.get(obj1);

					if (a != null) {
						deletedIndexedAdapters.remove(obj1);
						registerAsListener(a);
					} else

						a = uiGenerator.createObjectAdapter(
								// containW,
								adaptor, obj1, RemoteSelector.getClass(obj1),
								i, ClassDescriptor.ELEMENT_NAME, obj, false);
				}
				
				a.setNameChild();
				// if (a.isVisible()) {
				addDynamicChildInTables(i, a);
				
				a.setRealVectorIndex(i);
				LoggableRegistry.mapLoggableToAdapter(a);
				// if (!getVectorStructure().isEditable(i))
				if (a.isReadOnly())
					a.setUneditable();
				
				// making it more efficient - does not work sigh
				// uiGenerator.deepProcessAttributes(adaptor, false);

				// uiGenerator.deepProcessAttributes(this);
				
				ViewInfo cdesc = ClassDescriptorCache
						.getClassDescriptor(inputClass);
				if (getUIFrame() != null)
					RightMenuManager.getRightMenu(inputClass, this);
				
			}
		}

		this.childrenCreated = true;
		*/
		//return recomputeVisibleChildrenLists();
		
		

	}
	// boolean forceRebuild = false;
	public void addVectorComponentsWithoutRefreshingVisible(int from) {
		// this.childrenCreated = true;
		// System.out.println("add vector in vector adapter:" + this);
		
		CompositeAdapter adaptor = this;
		
		Object obj = null;
		if (!ObjectEditor.shareBeans()) {
			// obj = getViewObject(getRealObject());
			obj = computeAndMaybeSetViewObject();
		} else {

			// obj = ObjectRegistry.logReadMethodInvocation(new
			// UnivMethodInvocation(parent.getViewObject(),getPropertyReadMethod(),null));
			obj = get();
		}
		// System.out.println("new value: " + obj);
		adaptor.setViewObject(obj);
		
		if (obj == null)
			return ;
		// Class inputClass = obj.getClass();
		// ClassProxy inputClass = RemoteSelector.getClass(obj);
		ClassProxy inputClass = ACompositeLoggable.getTargetClass(obj);
		
		VectorStructure v = getVectorStructure();
		if (v != null) {
			
			vSize = v.size();
			
			// int displayIndex = 0; // this seems wrong
			// maybe this should be an instance variable
			int displayIndex = from;
			for (int i = from; i < vSize; i++) {
				

				Object obj1 = v.elementAt(i);
				// Object obj1 = this.vectorElementAt(v, i);

				ObjectAdapter a;
				// System.out.println("add components");
				if (obj1 == null)
					// a = uiGenerator.uiAddComponents(containW, adaptor, obj1,
					// uiMethodInvocationManager.OBJECT_CLASS, i,
					// Integer.toString(i), obj, false);
					a = uiGenerator.createGraphObjectAdapter(
							// containW,
							adaptor, obj1,
							MethodInvocationManager.OBJECT_CLASS_PROXY, i,
//							AClassDescriptor.ELEMENT_NAME, obj, false);
							AttributeNames.ANY_ELEMENT, obj, false, getChildTextMode());
				else {
					// a = uiGenerator.uiAddComponents(containW, adaptor, obj1,
					// obj1.getClass(), i, Integer.toString(i), obj, false);
					a = deletedIndexedAdapters.get(obj1);					

					if (a != null) {
						deletedIndexedAdapters.remove(obj1);
						a.recalculateViewObject();
						registerAsListener(a);
					} else

						a = uiGenerator.createGraphObjectAdapter(
								// containW,
								adaptor, obj1, RemoteSelector.getClass(obj1),
							//	i, AClassDescriptor.ELEMENT_NAME, obj, false);
					            i, AttributeNames.ANY_ELEMENT, obj, false, getChildTextMode());
					if (a == null)
						break;
				}
				
				//a.setNameChild();
				// if (a.isVisible()) {
				addDynamicChildInTables(i, a);
				
				a.setRealVectorIndex(i);
				a.setAdapterType(INDEX_TYPE);
				LoggableRegistry.mapLoggableToAdapter(a);
				// if (!getVectorStructure().isEditable(i))
//				if (a.isReadOnly())
//					a.setUneditable();
				a.setNameChild();
				
				// making it more efficient - does not work sigh
				// uiGenerator.deepProcessAttributes(adaptor, false);

				// uiGenerator.deepProcessAttributes(this);
				
				ClassDescriptorInterface cdesc = ClassDescriptorCache
						.getClassDescriptor(inputClass);
				if (getUIFrame() != null && !isGraphicsCompositeObject())
					RightMenuManager.getRightMenu(inputClass, this, obj);
				
			}
		}

		this.childrenCreated = true;
		//return recomputeVisibleChildrenLists();
		
		

	}
	public void cleanUpForReuse() {
		super.cleanUpForReuse();
		childrenCreated = false;
	}

	public String getDefaultDirection() {
		
		//if (inRowWithPrimitiveAndComposites())
		if (computeIsOnlyChild())
			return getParentAdapter().getDefaultDirection();
		if (isFlatTableRow())
			return AttributeNames.HORIZONTAL;
		else if (getParentAdapter() != null && (getParentAdapter() instanceof VectorAdapter 
  				|| getParentAdapter() instanceof HashtableAdapter)) 
			return 	AttributeNames.HORIZONTAL;	
		else if (hasHomogeneousParent() && getMinimumHeight() == 1 && getAdapterType() != PROPERTY_TYPE)
			return AttributeNames.HORIZONTAL;
		// else if ( this.getChildAdapterCount() >= 4 || getHeight() > 1)
		else	 if (inRowWithPrimitiveAndComposites())
				  return AttributeNames.HORIZONTAL;
		else
			return AttributeNames.VERTICAL;
		// else
		// return "square";

	}

	public void uiAddVectorComponentsOld(int from) {
		// System.out.println("add vector in vector adapter:" + this);
		int numProperties = this.getCurrentChildCount();
		CompositeAdapter adaptor = this;
		// Container containW = getGenericWidget();
		// Container containW = getGenericWidget().getContainer();
		WidgetAdapterInterface wa = adaptor.getWidgetAdapter();
		if (wa == null)
			return;
		// uncommentng next line
		// containW = (Container) adaptor.getWidgetAdapter().getUIComponent();
		// System.out.println("real object: " + getRealObject());
		// Object obj = uiGenerator.getViewObject(getRealObject());
		Object obj = null;
		if (!ObjectEditor.shareBeans()) {
			obj = computeViewObject(getRealObject());
		} else {

			// obj = ObjectRegistry.logReadMethodInvocation(new
			// UnivMethodInvocation(parent.getViewObject(),getPropertyReadMethod(),null));
			obj = get();
		}
		// System.out.println("new value: " + obj);
		adaptor.setViewObject(obj);
		foundUnlabeledComposite = false;
		int maxLabelLength = 0;
		if (obj == null)
			return;
		ClassProxy inputClass = RemoteSelector.getClass(obj);
		if (EditorRegistry.getEditorClass(inputClass) != null)
			return;

		// if (obj instanceof Vector) {
		// if (obj instanceof Object) {

		// Vector v = uiBean.toVector( obj);
		VectorStructure v = getVectorStructure();
		if (v != null) {
			// Vector v = (Vector) obj;
			// Vector v = uiBean.toVector( obj);

			// containW.setLayout(new uiGridLayout(1, v.size()));
			// containW.setLayout(new uiGridLayout(1, v.size(),
			// uiGridLayout.DEFAULT_HGAP, 0));
			// boolean horizontalChildren = true;
			homogeneousVector = true;
			ClassProxy oldElemClass = null;
			ClassProxy curElemClass = null;
			// int vSize = vectorSize(obj);
			vSize = v.size();
			// for (int i=0; i< v.size(); i++) {
			// for (int i=from; i< v.size(); i++) {
			for (int i = from; i < vSize; i++) {
				/*
				 * Object obj1 = null;
				 * 
				 * try {
				 * 
				 * System.out.println(elementAtMethod); if (elementAtMethod !=
				 * null) { Object[] params = {new Integer(i)}; obj1 =
				 * elementAtMethod.invoke(obj, params); } } catch (Exception e) {
				 * System.out.println(e); break; }
				 */

				Object obj1 = v.elementAt(i);
				// Object obj1 = this.vectorElementAt(v, i);

				ObjectAdapter a;
				// System.out.println("add components");
				if (obj1 == null)
					// a = uiGenerator.uiAddComponents(containW, adaptor, obj1,
					// uiMethodInvocationManager.OBJECT_CLASS, i,
					// Integer.toString(i), obj, false);
					a = uiGenerator.createObjectAdapter(
							// containW,
							adaptor, obj1,
							MethodInvocationManager.OBJECT_CLASS_PROXY, i,
							AttributeNames.ANY_ELEMENT, obj, false);
				else
					// a = uiGenerator.uiAddComponents(containW, adaptor, obj1,
					// obj1.getClass(), i, Integer.toString(i), obj, false);
					a = uiGenerator.createObjectAdapter(
							// containW,
							adaptor, obj1, RemoteSelector.getClass(obj1), i,
							AttributeNames.ANY_ELEMENT, obj, false);
				// System.out.println("fin add comp");
				// Add to the mapping table here
				// if (a instanceof uiShapeAdapter) return;
				uiGenerator.deepProcessAttributes(a, false);
				adaptor.setChildAdapterMapping(Integer.toString(i), a);
				a.setIndex(i);
				adaptor.setChildAdapterMapping(a);
				// String label = "" + (i + 1) + ". ";
				String label = "" + (i + 1);
				a.setTempAttributeValue(AttributeNames.LABEL, label);
				a.getGenericWidget().setLabel(label);
				/*
				 * if (showVectorComponentLabels)
				 * a.getGenericWidget().setLabelVisible(true); else
				 * a.getGenericWidget().setLabelVisible(false);
				 */
				// System.out.println("Adapter" + a + " " +
				// a.getAttributeValue(AttributeNames.LABEL));
				// maxLabelLength = Math.max(maxLabelLength, ((String)
				// a.getAttributeValue(AttributeNames.LABEL)).length());
				// maxLabelLength = Math.max(maxLabelLength,
				// a.getGenericWidget().getLabel().length());
				maxLabelLength = Math.max(maxLabelLength, label.length());
				// System.out.println(maxLabelLength + " " +
				// a.getGenericWidget().getLabel().length());
				if (obj1 != null && !(a instanceof PrimitiveAdapter)) {
					// System.out.println("found non primitive");
					curElemClass = RemoteSelector.getClass(obj1);
					ClassDescriptorInterface childDesc = ClassDescriptorCache
							.getClassDescriptor(curElemClass);

					// System.out.println(childDesc);
					// System.out.println(childDesc.getBeanDescriptor().getValue("direction"));
					// System.out.println("vector child " +
					// a.getMergedAttributeValue("direction"));
					if (// childDesc.getBeanDescriptor() != null &&
					!a.getGenericWidget().isLabelVisible() &&
					// !"horizontal".equals(childDesc.getBeanDescriptor().getValue("direction")))
					// {
							!"horizontal".equals(a
									.getMergedAttributeValue("direction"))) {
						// !("horizontal".equals(a.getDirection()))) {
						foundUnlabeledComposite = true;
						// System.out.println ("found composite");
						horizontalChildren = false;
					}
					if (oldElemClass != null && oldElemClass != curElemClass)
						homogeneousVector = false;
					oldElemClass = curElemClass;
				}
				// a.setParentAdapter(adaptor);
			}
			// containW.setLayout(new uiGridLayout(1, v.size(),
			// uiGridLayout.DEFAULT_HGAP, 0));
			// why here?
			// adaptor.processAttributeList();
			if (horizontalChildren && homogeneousVector)
				adaptor.makeColumnTitles();
			// else
			// adaptor.padChildrenLabels(maxLabelLength);
			ClassDescriptorInterface cdesc = ClassDescriptorCache
					.getClassDescriptor(RemoteSelector.getClass(obj));
			RightMenuManager.getRightMenu(RemoteSelector.getClass(obj), this, null);

			// new direction code
			setDirection();
			boolean alignHorizontal = direction.equals("horizontal");

			// old direction code
			/*
			 * if ("horizontal".equals
			 * (adaptor.getMergedAttributeValue("direction"))) alignHorizontal =
			 * true; else if (adaptor.getMergedAttributeValue("direction") ==
			 * null { uiObjectAdapter parentAdaptor =
			 * adaptor.getParentAdapter(); if ((parentAdaptor != null) &&
			 * (parentAdaptor instanceof uiVectorAdapter)&&
			 * !"horizontal".equals(parentAdaptor.getMergedAttributeValue("direction")))
			 * alignHorizontal = true; }
			 */
			/*
			 * if ( alignHorizontal) {
			 * 
			 * containW.setLayout(new uiGridLayout(1, vSize + numFeatures,
			 * uiGridLayout.DEFAULT_HGAP, 0));
			 * this.setTempAttributeValue(AttributeNames.DIRECTION,
			 * "horizontal"); } else {
			 * this.setTempAttributeValue(AttributeNames.DIRECTION,
			 * AttributeNames.VERTICAL); //containW.setLayout(new
			 * uiGridLayout(features.length, 1));
			 * 
			 * //if (foundUnlabeledComposite && v.size() > 1) if
			 * (foundUnlabeledComposite && vSize > 1) //containW.setLayout(new
			 * uiGridLayout(v.size(), 1, 0, uiGridLayout.DEFAULT_VGAP));
			 * containW.setLayout(new uiGridLayout(vSize + numFeatures, 1, 0,
			 * uiGridLayout.DEFAULT_VGAP)); else //containW.setLayout(new
			 * uiGridLayout(v.size(), 1)); containW.setLayout(new
			 * uiGridLayout(vSize + numFeatures, 1)); }
			 * 
			 */
		}

		this.childrenCreated = true;
		processDirection();
	}

	public void setDefaultAttributes() {
		super.setDefaultAttributes();
		setDefaultAttributes(0);
	}

	public void setDefaultSynthesizedAttributes() {
		// in super, should probably calculate number of displayed static
		// children in a spearate
		// loop, put this is working out well right now. Two wrongs are making
		// one right.
		super.setDefaultSynthesizedAttributes();
		setDefaultSynthesizedAttributes(0);
		// redoDynamicChildren();
		/*
		 * addNewlyVisibleChildren(); removeInvisibleChildren(); sort();
		 * recomputeIndices();
		 */
	}

	void redoDynamicChildren() {
		addNewlyVisibleChildren();
		removeInvisibleChildren();
		sort();
		recomputeIndices();
	}
	
	public ObjectAdapter getTitleChild() {
		if (this.getNumberOfChildren() == 0) return null;
		ObjectAdapter zerothChild = getChildAdapterAt(0);
		int columnTitleIndex;
		//uiObjectAdapter firstChild = indexedAdapterVector.get(0);
		ObjectAdapter firstChild = null;
		if (getNumberOfDisplayedStaticChildren() == 1 && zerothChild.isFlatTableRow()) {
			firstChild = zerothChild;
			columnTitleIndex = 0;
		} else {
			if (indexedAdapterVector.size() == 0)
				return null;
			firstChild = indexedAdapterVector.get(0);
			columnTitleIndex = 1;
		}
		return firstChild;
		
	}

	public void makeColumnTitles() {
		// if (isSkippedAdapter()) return;
		// System.out.println("making column titles");
		if (getChildAdapterCount() == 0)
			return;
		String direction = getDirection();
		if (!direction.equals(AttributeNames.VERTICAL))
			return;
		
		// uiObjectAdapter firstChild =
		// getChildAdapterMapping(Integer.toString(0));
		//uiObjectAdapter firstChild = getChildAdapterMapping(0);
		
		
		ObjectAdapter zerothChild = getChildAdapterAt(0);
		int columnTitleIndex;
		//uiObjectAdapter firstChild = indexedAdapterVector.get(0);
		ObjectAdapter firstChild = null;
		if (getNumberOfDisplayedStaticChildren() == 1 && zerothChild.isFlatTableRow()) {
			firstChild = zerothChild;
			columnTitleIndex = 0;
		} else {
			if (indexedAdapterVector.size() == 0)
				return;
			firstChild = indexedAdapterVector.get(0);
			columnTitleIndex = 1;
		}
		
		
		if (!(firstChild instanceof CompositeAdapter) || firstChild.getRealObject() == null)
			return;
		// ((uiContainerAdapter)
		// enum.nextElement()).makeHorizontalColumnTitles();
		if (((CompositeAdapter) firstChild).isSkippedAdapter())
			return;
		boolean titleChanged = false;
		if (!firstChild.childrenShowingColumnTitle()) {
			((CompositeAdapter) firstChild).makeHorizontalColumnTitles();
			titleChanged = true;
		}
		//for (int i = 1; i < indexedAdapterVector.size(); i++) {
		for (int i = columnTitleIndex; i < indexedAdapterVector.size(); i++) {
			// uiObjectAdapter container =
			// getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter container = indexedAdapterVector.get(i);
			if (container instanceof CompositeAdapter) {
					//&& !container.childrenHidingColumnTitle()) { // no idea what is going on here
				((CompositeAdapter) container)
						.setHorizontalLabelVisible(false);
				titleChanged = true;
			}

		}
		if (titleChanged)
			propagateAttributesToWidgetAdapter();

	}

	public void setDefaultAttributes(int from) {
		if (!childrenCreated)
			return;
		// if (isAtomic()) return;
		boolean homogeneousVector = true;
		Class oldElemClass = null;
		Class curElemClass = null;
		int maxLabelLength = 0;
		for (int i = from; i < indexedAdapterVector.size(); i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedAdapterVector.get(i);
			// if (a.getMergedAttributeValue(AttributeNames.LABEL) == null)
			a.maybeSetLabel("" + (i + 1));
			a.setTrueLabel("" + (i + 1));
			if (!homogeneousVector)
				continue;
			// a.setNameChild();
			// else
			// a.setTrueLabel ((String)
			// (a.getTempAttributeValue(AttributeNames.LABEL)));
			// a.setTrueLabel ((String)
			// (a.getTempAttributeValue(AttributeNames.LABEL)));
			// a.getGenericWidget().setLabel((String)
			// (a.getTempAttributeValue(AttributeNames.LABEL)));

			Object obj1 = a.computeAndMaybeSetViewObject();
			if (obj1 == null) {
				curElemClass = null;

			} else {
				curElemClass = obj1.getClass();
			}
			if (oldElemClass != null && oldElemClass != curElemClass) {
				homogeneousVector = false;
				// break;
			}
			oldElemClass = curElemClass;

		}
		setHomogeneous(homogeneousVector /* && numFeatures == 0 */);
	}

	// int maxDynamicComponentNameLength = 0;
	public void setDefaultSynthesizedAttributes(int from) {
		if (!childrenCreated)
			return;
		// if (isAtomic()) return;
		boolean homogeneousVector = true;
		horizontalChildren = true;
		foundUnlabeledComposite = false;
		/*
		 * Class oldElemClass = null; Class curElemClass = null;
		 */
		int maxLabelLength = 0;
		for (int i = from; i < indexedAdapterVector.size(); i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedAdapterVector.get(i);
			Object obj1 = a.computeAndMaybeSetViewObject();

			// String label = "" + (i + 1);
			// System.out.println(maxLabelLength + " " +
			// a.getGenericWidget().getLabel().length());
			if (obj1 != null && !(a instanceof PrimitiveAdapter)) {
				// System.out.println("found non primitive");
				// if
				// (!ClassDescriptorCache.toBoolean(a.getTempAttributeValue(AttributeNames.LABELLED))
				if (!ClassDescriptorCache.toBoolean(a.isLabelled())
				// !a.getGenericWidget().isLabelVisible() &&
						// !"horizontal".equals(childDesc.getBeanDescriptor().getValue("direction")))
						// {
						// &&
						// !"horizontal".equals(a.getMergedAttributeValue("direction")))
						// {
						&& !"horizontal".equals(a
								.getTempAttributeValue("direction"))) {
					// !("horizontal".equals(a.getDirection()))) {
					foundUnlabeledComposite = true;
					// System.out.println ("found composite");
					horizontalChildren = false;
					/*
					 * 
					 * curElemClass = obj1.getClass();
					 * 
					 * 
					 * if (oldElemClass != null && oldElemClass != curElemClass)
					 * homogeneousVector = false; oldElemClass = curElemClass;
					 */
					/*
					 * if (horizontalChildren) label = "" + ('A' + 1);
					 */
				}
			}
			/*
			 * String label; if (horizontalChildren) label = "" + ('A' + 1);
			 * else label = "" + (i + 1);
			 */
			// a.setLocalAttribute(new Attribute(AttributeNames.LABEL, label));
			// a.setTempAttributeValue(AttributeNames.LABEL, label);
			// if (a.getMergedAttributeValue(AttributeNames.LABEL) == null)
			// a.setLabel(label);
			// a.setNameChild();
			// a.setTrueLabel(label);
			String label = a.getLabel();
			int oldMaxLabelLength = maxLabelLength;
			maxLabelLength = Math.max(maxLabelLength, label.length());
			maxDynamicComponentNameLength = Math.max(label.length(),
					maxDynamicComponentNameLength);
			if (maxDynamicComponentNameLength != oldMaxLabelLength) {
				maxDynamicComponentName = label;
			}

			// a.setParentAdapter(adaptor);

			// containW.setLayout(new uiGridLayout(1, v.size(),
			// uiGridLayout.DEFAULT_HGAP, 0));
			// why here?
			// adaptor.processAttributeList();
			/*
			 * if (horizontalChildren && homogeneousVector )
			 * adaptor.makeColumnTitles(); //else
			 * //adaptor.padChildrenLabels(maxLabelLength); ViewInfo cdesc =
			 * ClassDescriptorCache.getClassDescriptor(obj.getClass());
			 * RightMenuCache.initRightMenu(obj.getClass(), this);
			 *  // new direction code
			 */

		}
		// setHomogeneous(homogeneousVector);
		setChildrenHorizontal(horizontalChildren);

		// setDirection();
	}

	public String getColumnTitle(ObjectAdapter a) {
		//if (!a.isLabelled() || a instanceof uiContainerAdapter)
		if (!a.isLabelled() )
			return "";
		/*
		if (!a.isLabelled() )
			return "";
			*/
		if (a.toStringAsLabel() && a.getRealObject() != null)
			return a.getRealObject().toString();
		else if (a.userObjectAsLabel() && a.getUserObject() != null)
			return a.getUserObject();
		String label = a.getDefinedLabel();
		// should we not use column title?
		if (label != null)
			return label;
		//int childIndex = a.getVectorIndex();
		int childIndex = a.getRealVectorIndex();		
		if (childIndex < 0)
			return a.getLabelWithoutSuffix();
		// if (childIndex < 0) return a.getLabel();
		return "" + (char) ('A' + childIndex);
		// return "" + (char) ('A' + a.getIndex());
	}

	public void processSynthesizedAttributesWithDefaults() {
		super.processSynthesizedAttributesWithDefaults();
		processSynthesizedAttributesWithDefaults(0);
	}

	// return true if some work actually done
	public boolean addChildUIComponentsBasic() {
		/*
		 * if (hasOnlyGraphicsDescendents()) return false;
		 */
		int numStaticChildren = getNumberOfChildren();
		// int numVisibleAndInvisibleChildren = getVectorStructure().size();
		// originally it was # visible and invisible children, which made no
		// sense for add ui components
		int numVisibleIndexedChildren = indexedAdapterVector.size();
		// return addChildUIComponentsBasic (numStaticChildren + 0,
		// numStaticChildren + adapterMapping.size());
		return addChildUIComponentsBasic(numStaticChildren + 0,
				numStaticChildren + numVisibleIndexedChildren);
	}

	public boolean addChildUIComponentsBasic(int from, int to) {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return false; // reversing ret val
		// if (isAtomic()) return true;
		// if (isLeafOfAtomic()) return true;
		if (isLeafOfAtomic())
			return false;
		boolean retVal = false;
		retVal = retVal | super.addChildUIComponentsBasic();
		if (from == to)
			// return false;
			return retVal;

		// boolean retVal = false;
		// int numStaticChildren = getNumberOfStaticChildren();
		// retVal = retVal | super.addChildUIComponentsBasic();
		int numStaticChildren = getNumberOfDisplayedStaticChildren();
		/*
		if (from == 0)
			from = numStaticChildren;
			*/
		// cant hurt to have a big ranger
		// check if static child

		if (from < numStaticChildren)
			return false;
		// the below stuff will not get executed
		// from = Math.max(from, numStaticChildren);
		to = Math.min(to, numStaticChildren + indexedAdapterVector.size());
		if (/* from < numStaticChildren || */to > numStaticChildren
				+ indexedAdapterVector.size())
			return false;
		int startVectorIndex = from - numStaticChildren;
		int stopVectorIndex = to - numStaticChildren;
		try {
		ObjectAdapter fromAdapter = indexedAdapterVector
				.elementAt(startVectorIndex);
		ObjectAdapter toAdapter = indexedAdapterVector
				.elementAt(stopVectorIndex - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			Tracer.error("Start Vector Index out range. Please report this error if you have not received the multiple references error");
		}

		/*
		 * addNewlyVisibleChildren(); removeInvisibleChildren(); sort();
		 * recomputeIndices(); startVectorIndex =
		 * indexedAdapterVector.indexOf(fromAdapter); stopVectorIndex =
		 * indexedAdapterVector.indexOf(toAdapter) + 1;
		 */
		// thismakes no sense
		// int numStaticChildren = getNumberOfChildren();
		// boolean childMadeVisible = false;
		// if (from == 0 && to == adapterMapping.size())
		// if (from == numStaticChildren && to == adapterMapping.size() +
		// numStaticChildren)
		// the following makes so sense to me, maybe the whole range means that
		// the class components have not been added
		/*
		 * if (from == numStaticChildren //&& to == getVectorStructure().size() +
		 * numStaticChildren) && to == indexedAdapterVector.size() +
		 * numStaticChildren)
		 */
		/*
		 * if (startVectorIndex == 0 //&& to == getVectorStructure().size() +
		 * numStaticChildren) && stopVectorIndex == indexedAdapterVector.size() )
		 */
		// always do this, super will return if already called
		// this should go up before checking number of children
		// moved ths to much earlier
		// retVal = retVal | super.addChildUIComponentsBasic();
		/*
		 * addNewlyVisibleChildren(); removeInvisibleChildren(); sort();
		 * recomputeIndices();
		 */

		// this is now always true because previous code has been commented
		/*
		 * if (startVectorIndex != indexedAdapterVector.indexOf(fromAdapter) ||
		 * stopVectorIndex - 1 != indexedAdapterVector.indexOf(toAdapter)) {
		 * startVectorIndex = 0; stopVectorIndex = indexedAdapterVector.size(); }
		 */
		// for (int i = from - numStaticChildren; i < to - numStaticChildren;
		// i++) {
		for (int i = startVectorIndex; i < stopVectorIndex; i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedAdapterVector.get(i);
			// this is being done earlier
			//a.setIndex(getNumberOfDisplayedStaticChildren() + i);
			retVal = retVal | a.processPreferredWidget();

		}
		// return false;
		return retVal;
	}

	public void addNewlyVisibleChildren() {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return; // reversing ret val
		// if (isAtomic()) return true;
		// if (isLeafOfAtomic()) return true;
		/*
		 * if (isLeafOfAtomic()) return ;
		 */
		// boolean childMadeVisible = false;
		// if (from == 0 && to == adapterMapping.size())
		// if (from == numStaticChildren && to == adapterMapping.size() +
		// numStaticChildren)
		// the following makes so sense to me, maybe the whole range means that
		// the class components have not been added
		Enumeration<ObjectAdapter> elements = deletedIndexedAdapters
				.elements();
		while (elements.hasMoreElements()) {

			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = elements.nextElement();
			if (a.isVisible())
				continue;
			a.recomputeAttributes();
			if (a.isVisible()) {
				// if (a.isVisible()) {
				// assuming it is going back at the position from which it was
				// deleted
				addDynamicChildInTables(a.getVectorIndex(), a);
				deletedIndexedAdapters.remove(a.getRealObject());
				registerAsListener(a);

				// no need to reset indices
				//
				// a.registerAsListener();

				uiGenerator.deepProcessAttributes(a);
				// childMadeVisible = true;
			}
		}

	}

	public void removeInvisibleChildren() {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return; // reversing ret val

		// the following makes no sense to me, maybe the whole range means that
		// the class components have not been added

		Enumeration<ObjectAdapter> elements = indexedVisibleAndInvisibleAdapterVector
				.elements();
		// for (int i = 0; i < adapterMapping.size(); i++) {
		while (elements.hasMoreElements()) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = elements.nextElement();
			a.recomputeAttributes();
			if (a.isVisible())
				continue;
			else {
				indexedAdapterVector.remove(a);
				removeWidget(a);

				// as adapter mapping is a table, deleting and inserting
				// into it should not matter
				// forceRebuild = true;
				// setForceRebuild(true);
				// deleteIndexedChildForReuse(a);
				// a.cleanUpForReuse();
			}
		}

	}

	public void oldRemoveInvisibleChildren() {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return; // reversing ret val

		// the following makes no sense to me, maybe the whole range means that
		// the class components have not been added

		Enumeration<ObjectAdapter> elements = indexedAdapterVector.elements();
		// for (int i = 0; i < adapterMapping.size(); i++) {
		while (elements.hasMoreElements()) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = elements.nextElement();
			a.recomputeAttributes();
			if (a.isVisible())
				continue;
			else {
				// as adapter mapping is a table, deleting and inserting
				// into it should not matter
				// forceRebuild = true;
				setForceRebuild(true);
				deleteIndexedChildForReuse(a);
				a.cleanUpForReuse();
			}
		}

	}
	
	public void recomputeIndices() {
		recomputeDisplayIndices(0);
	}
	public void recomputeRealIndices(int fromIndex) {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return; // reversing ret val
		// if (isAtomic()) return true;
		// if (isLeafOfAtomic()) return true;
		if (isLeafOfAtomic())
			return;
		
		for (int i = fromIndex; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {

			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedVisibleAndInvisibleAdapterVector.elementAt(i);
			/*
			if (getSort())
				a.setPosition(displayIndex + getNumberOfDisplayedStaticChildren());
				*/
			// setDynamicVisibleChildIndices(i, a);
			a.setRealVectorIndex(i);
			//a.propagateAttributesToWidgetShell();
		}
	}

	public void recomputeDisplayIndices(int fromIndex) {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return; // reversing ret val
		
		//if (isAtomic()) return;
//		if (isLeafOfAtomic())
//			return;
		//leafOfAtomic does not work for AListenableString, let us try to modify. This is ugly
		// let us put a debug to see when it does return
		//added the widgetAdapter != null clause because it is possible in graphics to have no widget adaoter and
		//maybe we still need display indices to be recomputed in that case
		if (isLeafOfAtomic() && getWidgetAdapter() != null && getWidgetAdapter().getClass() != MSTextFieldAdapter.class)
			return;
		

		// the following makes so sense to me, maybe the whole range means that
		// the class components have not been added
		int displayIndex = fromIndex;
		for (int i = fromIndex; i < indexedAdapterVector.size(); i++) {

			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedAdapterVector.elementAt(i);
			/*
			if (getSort())
				a.setPosition(displayIndex + getNumberOfDisplayedStaticChildren());
				*/
			// setDynamicVisibleChildIndices(i, a);
			if (setDynamicVisibleChildIndices(displayIndex, a))
				displayIndex++;
		}
	}

	public void redisplayIndices() {
		Vector<ObjectAdapter> dynamicAdapters = this
				.getIndexedAdaptersVector();
		for (int i = 0; i < dynamicAdapters.size(); i++) {
			WidgetShell ws = dynamicAdapters.elementAt(i).getWidgetShell();
			if (ws != null)
				ws.processAttributes();

		}
	}

	// return true if some work was actually done,
	public boolean addChildUIComponents(int from, int to) {
		boolean retVal = addChildUIComponentsBasic(from, to);
		// if (retVal)
//		if (getWidgetAdapter() != null)
		if (getWidgetAdapter() != null)
		getWidgetAdapter().childComponentsAdded(retVal);
		return retVal;

		/*
		 * if (!childrenCreated) return true; //if (isAtomic()) return true; if
		 * (isLeafOfAtomic()) return true; super.addChildUIComponents(); for
		 * (int i=0; i< adapterMapping.size(); i++) { uiObjectAdapter a =
		 * getChildAdapterMapping(Integer.toString(i));
		 * a.processPreferredWidget(); } return false;
		 */

	}

	public void descendentCreated(int index) {

		boolean informAncestor = !getUIFrame().isOnlyGraphicsPanel() && 
				(Boolean)AttributeNames.getDefaultOrSystemDefault(AttributeNames.INFORM_ABOUT_DESCENDENTS);
		if (informAncestor)
		uiGenerator.deepProcessAttributes(this, false, index, index + 1);
		/*
		 * setDirection(); processDirection();
		 */

	}

	public void processSynthesizedAttributesWithDefaults(int from) {
		if (!childrenCreated)
			return;
		if (isAtomic())
			return;
		// if (isLeafOfAtomic()) return;
		// System.out.println("add vector in vector adapter:" + this);
		int numProperties = this.getCurrentChildCount();
		VectorAdapter adaptor = this;
		// Container containW = getGenericWidget();
		// Container containW = getGenericWidget().getContainer();
		WidgetAdapterInterface wa = adaptor.getWidgetAdapter();
		if (wa == null)
			return;
		for (int i = from; i < indexedAdapterVector.size(); i++) {
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = indexedAdapterVector.get(i);
			
		}
		// uncommentng next line
		// commenting again
		// containW = (Container) adaptor.getWidgetAdapter().getUIComponent();
		if (adaptor.childrenHorizontal() && adaptor.isHomogeneous()) {
			adaptor.makeColumnTitles();
		}

		// adaptor.processAttributeList();
		
		processDirection();
	}

	public boolean isLeaf() {
		return isLeafAdapter();
		
	}

	public boolean isLeafAdapter() {
		if (!super.isLeafAdapter())
			return false;
		// System.out.println("Get Value of" + this + " " + getValue());
		// System.out.println("Get Reeal Obkect of" + this + " " +
		// getRealObject());
		// System.out.println("GetView Obkect of" + this + " " +
		// getViewObject());
		if (this.getRealObject() == null)
			return true;
		// Vector v = uiBean.toVector(getRealObject());
		VectorStructure v = getVectorStructure();
		if (v == null)
			return true;
		// return ((Vector) this.getRealObject()).size() == 0;
		return v.size() == 0;
	}

	public boolean reparentChild() {
		return false;
	}

	public boolean hasNoProperties() {

		return false;

	}

	
	public static void maybeAddVectorListener(Object listenable,
			VectorListener listener) {

		if (listenable == null)
			return;
		if (listenable instanceof VectorListenerRegisterer) {
			((VectorListenerRegisterer) listenable).addVectorListener(listener);
			ObjectEditor.associateKeywordWithClassName(
					ObjectEditor.VECTOR_LISTENER_KEYWORD, RemoteSelector
							.getClass(listenable));
			Tracer.info(VectorAdapter.class, "Added ObjectEditor adapter for component " + listener + " as vector listener of:" + listenable);
			((ObjectAdapter) listener).setRegisteredAsListener(true);
			return;
		}

		MethodProxy addVectorListenerMethod = IntrospectUtility
				.getAddVectorListenerMethod(RemoteSelector.getClass(listenable));
		if (addVectorListenerMethod != null) {
			if (listenable instanceof ACompositeLoggable) {
				((ACompositeLoggable) listenable).addVectorListener(listener);
				return;
			}

			try {
				Object[] args = { listener };
				addVectorListenerMethod.invoke(listenable, args);
				ObjectEditor.associateKeywordWithClassName(
						ObjectEditor.VECTOR_LISTENER_KEYWORD, RemoteSelector
								.getClass(listenable));
				Tracer.info(VectorAdapter.class, "Added ObjectEditor adapter for component " + listener + " as vector listener of:" + listenable);
				((ObjectAdapter) listener).setRegisteredAsListener(true);

				// listener.setPropertyChangeListenable (listenable);
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out.println("E** Could not invoke addVectorListener on"
						+ listenable);
				e.printStackTrace();
			}
		}

	}

	public void registerAsListener(Object obj) {
		super.registerAsListener(obj);
		maybeAddVectorListener(obj, this);

	}

	public boolean preClear() {
		if (!getVectorStructure().hasClearMethod())
			return false;
		for (int i = 0; i < getNumberOfDynamicChildren(); i++) {
			if (!getVectorStructure().isEditable(index))
				return false;
		}
		return getVectorStructure().hasDeleteChildMethod();
	}

	public void clear() {
		getVectorStructure().clear();
	}

	public ObjectAdapter[] getColumnAdapters(CompositeAdapter parent,
			ObjectAdapter child) {
		if (parent == null)
			return null;
		if (child.isProperty())
			return super.getColumnAdapters(parent, child);
		Vector<ObjectAdapter> children = parent.getChildrenVector();
		int childIndex = child.getRealVectorIndex();
		if (childIndex < 0)
			return null;
		ObjectAdapter[] retVal = new ObjectAdapter[children.size()];
		for (int i = 0; i < children.size(); i++) {
			retVal[i] = children.get(i).getChildAdapterAt(childIndex);
		}
		return retVal;
		// return retVal;
	}

	public void select(ObjectAdapter[] column) {
		if (getWidgetAdapter() != null) {
			getWidgetAdapter().setUIComponentSelected(column);
			return;
		}
		if (getParentAdapter() == null)
			return;
		getParentAdapter().select(column);

	}

	public boolean isReadOnly(ObjectAdapter child) {
		int index = child.getVectorIndex();
		if (index < 0)
			return super.isReadOnly(child);
		else
			return !getVectorStructure().isEditable(index);
	}
	
//	@Override
//	public String getDebugInfo() {
//		return getVectorStructure().getPatternName();
//	}
	@Override
	public String getPatternName() {
		return getVectorStructure().getPatternName();
	}
	public  Object getComponentValue(String componentName) {
		Object retVal = super.get(componentName);
		if (retVal == null) {
			try {
				int pos = Integer.parseInt(componentName);
				return getVectorStructure().elementAt(pos);
				
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	
	 Object instantiateCanonicalForm() {
			return new ACanonicalListBean(this);
		}
		
		public Object toCanonicalForm() {
			if (canonicalListBean == null) {
				canonicalListBean = (List) instantiateCanonicalForm();
				for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
					canonicalListBean.add(						
//							indexedVisibleAndInvisibleAdapterVector.get(i).toCanonicalForm());
					indexedVisibleAndInvisibleAdapterVector.get(i).toCanonicalForm());

				}
				
				
			}
			return canonicalListBean;	
		}
		
	
	// map methods
		Map<Object, Object> mapDelegate() {
			if (mapDelegate == null) {
				mapDelegate = super.mapDelegate();				
//				mapDelegate = new HashMap();
				for (int i = 0; i < indexedVisibleAndInvisibleAdapterVector.size(); i++) {
					mapDelegate.put(i, 						
//							indexedVisibleAndInvisibleAdapterVector.get(i).toCanonicalForm());
					objectOrAdapter (indexedVisibleAndInvisibleAdapterVector.get(i)));

				}
			}
			return mapDelegate;
		}
		
		public Object put(Object key, Object value) {
			if (! (key instanceof Integer)) {
				return super.put(key,  value);
			}
			Integer index = (Integer) key;
			VectorStructure vectorStructure = getVectorStructure();
			int size = vectorStructure.size();
			if (index < size ) {
				Object retVal = vectorStructure.elementAt(index);
				vectorStructure.setElementAt(value, index);
				return retVal;
			}
			mapDelegate = null;
			return null;		

		}
		
		public void add( int index, Object value) {
			VectorStructure vectorStructure = getVectorStructure();
			vectorStructure.insertElementAt(value, index, this);
			mapDelegate = null;
		}
		
		public void add( Object value) {
			VectorStructure vectorStructure = getVectorStructure();
			vectorStructure.addElement(value, null);
			mapDelegate = null;

		}
		
		public void remove( int index) {
			VectorStructure vectorStructure = getVectorStructure();
			vectorStructure.removeElementAt(index, null);
			mapDelegate = null;
		}
		

//	
//	public boolean isEmpty() {
//		return listDelegate.isEmpty();
//	}
//
//	public boolean contains(Object o) {
//		return listDelegate.contains(o);
//	}
//
//	public Iterator iterator() {
//		return listDelegate.iterator();
//	}
//
//	public Object[] toArray() {
//		return listDelegate.toArray();
//	}
//
//	public Object[] toArray(Object[] a) {
//		return listDelegate.toArray(a);
//	}
//
//	public boolean add(Object e) {
//		return listDelegate.add(e);
//	}
//
//	public boolean removeElement(Object o) {
//		return listDelegate.remove(o);
//	}
//
//	public boolean containsAll(Collection c) {
//		return listDelegate.containsAll(c);
//	}
//
//	public boolean addAll(Collection c) {
//		return listDelegate.addAll(c);
//	}
//
//	public boolean addAll(int index, Collection c) {
//		return listDelegate.addAll(index, c);
//	}
//
//	public boolean removeAll(Collection c) {
//		return listDelegate.removeAll(c);
//	}
//
//	public boolean retainAll(Collection c) {
//		return listDelegate.retainAll(c);
//	}
//
//	public boolean equals(Object o) {
//		return listDelegate.equals(o);
//	}
//
//	
//	public Object get(int index) {
//		return listDelegate.get(index);
//	}
//
//	public Object set(int index, Object element) {
//		return listDelegate.set(index, element);
//	}
//
//	public void add(int index, Object element) {
//		listDelegate.add(index, element);
//	}
//
//	public Object removeElement(int index) {
//		return listDelegate.remove(index);
//	}
//
//	public int lastIndexOf(Object o) {
//		return listDelegate.lastIndexOf(o);
//	}
//
//	public ListIterator listIterator() {
//		return listDelegate.listIterator();
//	}
//
//	public ListIterator listIterator(int index) {
//		return listDelegate.listIterator(index);
//	}
//
//	public List subList(int fromIndex, int toIndex) {
//		return listDelegate.subList(fromIndex, toIndex);
//	}

}
