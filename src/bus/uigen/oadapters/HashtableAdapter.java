package bus.uigen.oadapters;
import java.awt.Container;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTabbedPane;
import javax.swing.tree.TreeNode;

import util.models.HashtableListener;
import util.trace.Tracer;
import bus.uigen.ObjectEditor;
import bus.uigen.WidgetAdapter;
import bus.uigen.WidgetAdapterInterface;
import bus.uigen.uiGenerator;
import bus.uigen.adapters.CommandAndStatePanelAdapter;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.diff.ACanonicalMapBean;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.misc.OEMisc;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.sadapters.HashtableStructure;
import bus.uigen.view.OEGridLayout;
import bus.uigen.visitors.ClearVisitedNodeAdapterVisitor;
import bus.uigen.widgets.VirtualContainer;
//import bus.uigen.sadapters.StructureFactoryRegistry;

//public class uiHashtableAdapter extends uiContainerAdapterpublic class HashtableAdapter extends ClassAdapter
implements HashtableAdapterInterface
//	implements HashtableListener, ReplaceableChildren, RemotePropertyChangeListener 
	{
	public HashtableAdapter () throws RemoteException {
		
	}	transient public static boolean COMPRESS = false;
	Hashtable<Object, ObjectAdapter> deletedKeyAndValueAdapters = new Hashtable();
	transient Vector<ObjectAdapter> dynamicAdapterVector = new Vector(); 
	transient Hashtable<Object, ObjectAdapter> keyAdapterMapping = new Hashtable();
	transient Vector sortedKeys = new Vector();
	transient Vector<ObjectAdapter> sortedKeyAdapters = new Vector();
	transient Hashtable<Object, ObjectAdapter> elementAdapterMapping = new Hashtable(); 
	transient Vector<ObjectAdapter> sortedElementAdapters = new Vector();
	//Hashtable<Object, uiObjectAdapter> deletedValues = new Hashtable();
	
	 
	/*	transient Method elementsMethod = null;	transient Method putMethod = null;  	transient Method removeMethod = null;
	transient Method getMethod = null ;	transient Method sizeMethod = null;
	*/
	/*	public Method getElementsMethod() {
		return elementsMethod;
	}
	public void setElementsMethod(Method newVal) {
		elementsMethod = newVal;
	}
		public Method getPutMethod() {
		return putMethod;
	}	public void setPutMethod(Method newVal) {
		putMethod = newVal;
	}	public Method getRemoveMethod() {
		return removeMethod;
	}	public void setRemoveMethod(Method newVal) {
		removeMethod = newVal;
	}
	*/
		
	/*	public Method getGetMethod() {
		return getMethod;
	}

	public void setGetMethod(Method newVal) {
		getMethod = newVal;
	}
		public Method getSizeMethod() {
		return sizeMethod;
	}

	public void setSizeMethod(Method newVal) {
		sizeMethod = newVal;
	}	
	
	public uiHashtableAdapter() {
	adapterMapping = new Vector();
	}	*/	//HashtableStructure hashtableStructure;	public void setHashtableStructure (HashtableStructure newVal) {
		setConcreteObject(newVal);	}	public  HashtableStructure getHashtableStructure () {
		//return hashtableStructure;
		return (HashtableStructure) concreteObject;	}
	void makeTablesConsistentWithSortedKeys() {
		dynamicAdapterVector.clear();
		sortedKeys.clear();
		sortedElementAdapters.clear();
		resetChildrenVector(getNumberOfDisplayedStaticChildren());
		for (int i = 0; i < sortedKeyAdapters.size(); i++) {
			ObjectAdapter keyAdapter = sortedKeyAdapters.elementAt(i);
			ObjectAdapter elementAdapter = keyAdapter.getValueAdapter();
			dynamicAdapterVector.add(keyAdapter);
			dynamicAdapterVector.add(elementAdapter);
			setChildAdapterMapping(keyAdapter);
			setChildAdapterMapping(elementAdapter);
			sortedElementAdapters.add(elementAdapter);
			sortedKeys.add(keyAdapter.getRealObject());			
		}
		resetTreeChildrenVector();
	}
	void makeTablesConsistentWithSortedElements() {
		dynamicAdapterVector.clear();
		sortedKeys.clear();
		sortedKeyAdapters.clear();
		resetChildrenVector(getNumberOfDisplayedStaticChildren());
		for (int i = 0; i < sortedElementAdapters.size(); i++) {
			ObjectAdapter elementAdapter = sortedElementAdapters.elementAt(i);
			ObjectAdapter keyAdapter = elementAdapter.getKeyAdapter();
			dynamicAdapterVector.add(keyAdapter);
			dynamicAdapterVector.add(elementAdapter);
			setChildAdapterMapping(keyAdapter);
			setChildAdapterMapping(elementAdapter);
			sortedKeyAdapters.add(keyAdapter);
			sortedKeys.add(keyAdapter.getRealObject());			
		}
		resetTreeChildrenVector();
	}
	//boolean forceRebuild;
	public boolean sort() {
		if (!getSort())
			return false;
		setForceRebuild(true);
		if (getHashtableSortKeys())
			sortKeys();
		else
			sortElements();
		return true;
		
		
	}
	public String toString() {
		if (!getSortMode())
			return super.toString();
		Object sortKey = getSortProperty();
		if (sortKey == null)
			return super.toString();
		HashtableStructure hashtableStructure = getHashtableStructure();
		
			//return super.toString();
		Object sortVal = hashtableStructure.get(sortKey);
		if (sortVal != null)
			return sortVal.toString();
		else
			return super.toString();
	}
	
	public void sortKeys() {
		setChildrenSortMode(sortedKeyAdapters, true);
		OEMisc.sort(sortedKeyAdapters);
		setChildrenSortMode(sortedKeyAdapters, false);
		makeTablesConsistentWithSortedKeys();
		
	}
	public void sortElements() {
		setChildrenSortMode(sortedElementAdapters, true);
		OEMisc.sort(sortedElementAdapters);
		setChildrenSortMode(sortedElementAdapters, false);
		makeTablesConsistentWithSortedElements();
		
	}
	
	
	
	public void resetTreeChildrenVector() {
		treeChildrenVector = treeChildrenVector();
	}
	
	 public void resetChildrenVector(int from) {		 
		 super.resetChildrenVector(from);
		 resetTreeChildrenVector();		 
	 }
	 public void resetChildrenVector() {
		 super.resetChildrenVector();
		  //childrenVector = new Vector(); 
		 resetTreeChildrenVector();
	  }
	
	public Object getUserChange () {		return getOriginalValue();	}	public boolean childUIComponentValueChanged (ObjectAdapter child 											  , Object newValue																							  , boolean initialChange											  ) {
		if (child.getAdapterType() == PROPERTY_TYPE ) {
			return super.childUIComponentValueChanged (child, newValue, initialChange);			//return;
		}		
		HashtableStructure hashtableStructure = this.getHashtableStructure();
				Object key = null;
		Object oldKey = null;		Object value = null;		int index = computeIndex(child);
		//uiContainerAdapter parentAdapter = (uiContainerAdapter) getParentAdapter();
		//int index;				if (child.getAdapterType() == KEY_TYPE) {			
			
			key = newValue;
			//uiObjectAdapter valueAdapter = this.getChildAdapterMapping(Integer.toString(index + 1));
			ObjectAdapter valueAdapter = this.getDynamicChildAdapterMapping(index + 1);
			value = valueAdapter.getOriginalValue();			oldKey = valueAdapter.getKey();			if (oldKey == null) return false;			if (!hashtableStructure.isEditableKey(oldKey)) return false;
			
			MethodInvocationManager.beginTransaction();
			// should remove not be postponed and take null instead of this?			hashtableStructure.remove(oldKey, this);
			Tracer.debug("Check that undo works correctly");
			hashtableStructure.put(key, value, null);
			MethodInvocationManager.endTransaction();
					} else if (child.getAdapterType() == VALUE_TYPE) {
			value = newValue;
			//uiObjectAdapter keyAdapter = this.getChildAdapterMapping(Integer.toString(index - 1));
			ObjectAdapter keyAdapter = this.getDynamicChildAdapterMapping(index - 1);
			key = keyAdapter.getOriginalValue();
			if (!hashtableStructure.isEditableElement(key)) return false;
			hashtableStructure.put(key, value, this);		}
		// why was this commented out?		//hashtableStructure.put(key, value, this);
		// maybe we need to build a command list, that is why we dont want the command
		//hashtableStructure.put(key, value, null);
		//askViewObjectToRefresh();
		this.resetTreeChildrenVector();		//uiMethodInvocationManager.endTransaction();
		return true;
	}
	public boolean setValueUserObject(ObjectAdapter valueAdapter, Object userObject) {
		Object oldKey = valueAdapter.getKey();
		if (oldKey != null && 
			// following is a kludge to see if the key is indeed the label
			valueAdapter.getKey().getClass().isAssignableFrom(userObject.getClass()) &&
			valueAdapter.toString().equals(oldKey) &&
			!oldKey.equals(userObject)) {
			MethodInvocationManager.beginTransaction();
			HashtableStructure hashtableStructure = getHashtableStructure();
			hashtableStructure.remove(oldKey, this);
			Object value = valueAdapter.getOriginalValue(); // why not real object?
						
		//hashtableStructure.put(key, value, this);
		hashtableStructure.put(userObject, value, null);
		//askViewObjectToRefresh();
		this.resetTreeChildrenVector();
		MethodInvocationManager.endTransaction();	
		return true;
		}
		return false;
	}
	
	public Object getChildValue (ObjectAdapter child) {
		if (child.getAdapterType() == PROPERTY_TYPE ) {
			return super.getChildValue(child);
		}		
		HashtableStructure hashtableStructure = this.getHashtableStructure();
		/*		Object key = null;
		Object oldKey = null;		Object value = null;
		*/		int index = computeIndex(child);
		//uiContainerAdapter parentAdapter = (uiContainerAdapter) getParentAdapter();
		//int index;				if (child.getAdapterType() == KEY_TYPE) {			
			return child.getOriginalValue();
					} else if (child.getAdapterType() == VALUE_TYPE) {
			//uiObjectAdapter keyAdapter = this.getChildAdapterMapping(Integer.toString(index - 1));
			ObjectAdapter keyAdapter = this.getDynamicChildAdapterMapping(index - 1);
			key = keyAdapter.getOriginalValue();			return hashtableStructure.get(key);
		}	else return null;
	}
	public Object getChildKey (ObjectAdapter child) {
		if (child.getAdapterType() == PROPERTY_TYPE ) {
			return super.getChildValue(child);
		}
		
		HashtableStructure hashtableStructure = this.getHashtableStructure();
		/*
		Object key = null;
		Object oldKey = null;
		Object value = null;
		*/
		int index = computeIndex(child);
		//uiContainerAdapter parentAdapter = (uiContainerAdapter) getParentAdapter();
		//int index;
		
		if (child.getAdapterType() == KEY_TYPE) {
			
			return child.getValueOrRealObject();
			
		} else if (child.getAdapterType() == VALUE_TYPE) {
			ObjectAdapter keyAdapter = this.getChildAdapterMapping(Integer.toString(index - 1));
			key = keyAdapter.getOriginalValue();
			return key;
		}	else return null;
	}
	public boolean isChildDeletable(ObjectAdapter child) {
		Object key = getChildKey(child);
		if (key == null) return false;
		HashtableStructure hashtableStructure = getHashtableStructure();
		return hashtableStructure.isRemovable(key);
				
		/*
		try {
			
			
		if (getViewObject() == null) return false;
			return getRemoveElementAtMethod() != null ||
				   getRemoveElementMethod() != null || Integer.parseInt(getChildAdapterIndex(child)) != -1;
		} catch (Exception e) {
			return false;
		}
		*/
	}
	public void deleteChild(ObjectAdapter child) {
		Object key = getChildKey(child);
		//uiVectorAdapter parentAdapter = this;
		//int index = Integer.parseInt(parentAdapter.getChildAdapterIndex(child));
		//if (index == -1) return;
		//Object element = child.getOriginalValue(); 
		//why is child a listener and not the adapter itself
		(getHashtableStructure()).remove(key, this);
		// we know that refresj will happen, so forget this?
		
		
			uiComponentValueChanged(false);
			
		
		
		
	}
	
	public void createChildrenBasic() {
		/*
		if (this.childrenCreated)
			return;		*/
		addClassComponents(false);		addHashtableComponents(0);
		/*		if (!isTopAdapter())		   parent.descendentsCreated();		else
			uiGenerator.deepProcessAttributes(this);		*/
		//uiGenerator.deepProcessAttributes(this);		//recursivelyCheckIfNoVisibleChildren(this);
		/*		if (checkIfNoVisibleChildren(this)) {
		//this.getUIFrame().maybeHideMainPanel();		return;
		}		*/
	}
	/*
	// Method defined in VectorListener
	// through which a listenable Vector
	// informs the UI of any changes

	public void updateVector(uiVectorEvent evt) {
		switch (evt.getEventType()) {
		case uiVectorEvent.AddComponentEvent:
			addComponent(evt.getPosition(),
						 evt.getNewValue());
			break;
		case uiVectorEvent.DeleteComponentEvent:
			deleteComponent(evt.getPosition());
			break;
		case uiVectorEvent.ChangeComponentEvent:
			changeComponent(evt.getPosition(), evt.getNewValue());
			break;
		}
	}
	*/
	public String getDefaultDirection() {
		  /*
		  if (isHomogeneous() )
			  return AttributeNames.VERTICAL;
		  else if (!isTopAdapter()  && parent.isHomogeneous())
				return "horizontal";
		  else if (this.getChildAdapterCount() < 4)
			  return AttributeNames.VERTICAL;
		  else return "square";
		  */
		  
			if (hasHomogeneousParent() && getMinimumHeight() == 1)
				return "horizontal";
			//else if ( this.getChildAdapterCount() >= 4 || getHeight() > 1)
			else
				return AttributeNames.VERTICAL;
			//else 
				// return "square";
		  
		}	public boolean processDirection(String direction) {		Container awtComponent = (Container) getWidgetAdapter().getUIComponent();
		if (awtComponent instanceof JTabbedPane) return true;
		if (getWidgetAdapter() instanceof CommandAndStatePanelAdapter)
			return getWidgetAdapter().processDirection(direction);
		try {			
			OEGridLayout lm = (OEGridLayout)(awtComponent.getLayout());
			// Check to see if direction is horizontal or
			// vertical.
			if (direction == null) {
				direction = "horizontal";
							}
			if (direction.equals("horizontal")) {
				lm.setColumns(lm.getColumns()+1);				lm.setRows(2);
			} else {
				lm.setRows(lm.getRows()+1);
				lm.setColumns(2);			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		awtComponent.doLayout();
		return false;	}	public void processDirection() {
		if (getWidgetAdapter() != null)
		getWidgetAdapter().processDirection();
		/*
		VirtualContainer containW = (VirtualContainer) getWidgetAdapter().getUIComponent();		boolean alignHorizontal = direction.equals("horizontal");
		if (getWidgetAdapter() instanceof CommandAndStatePanelAdapter) {
		  	getWidgetAdapter().processDirection();
		  	return;
		  }		
		if (getWidgetAdapter() instanceof uiJTabbedPaneAdapter) return;
		//if (containW instanceof JTabbedPane) return;
		getWidgetAdapter().processDirection();
		
		*/
			}
	/*
	// Add a new component to the Vector
	//
	public void addComponent(int position, Object obj) {
		// Add an element at the position specified in evt
		// First create a UI component corresponding to
		// the object being added.

		//Container awtComponent = (Container) getWidgetAdapter().getUIComponent();
		
		uiObjectAdapter adapter = uiGenerator.createObjectAdapter(
				//awtComponent, 
				this, obj, obj.getClass(), position, getViewObject(), false);
		//setChildAdapterMapping(Integer.toString(position), adapter);
		setChildAdapterMapping(position, adapter);
		adapter.setParentAdapter(this);		processDirection((String) getAttributeValue("direction"));		
		
	}
	*/
	
	// Delete an existing element from the Vector
	/*

	public void deleteComponent(int position) {
		// Remove the corresponding children from the 
		// AWT component.
		((Container) getWidgetAdapter().getUIComponent()).remove(position);
		getWidgetAdapter().remove(position, dynamicAdapterMapping.elementAt(position));
		
		// adjust the mapping
		dynamicAdapterMapping.removeElementAt(position);    
	}
  */
	
	// Change an existing element.
	// in the Vector. The new element may be of a different
	// Class.	// incremetal stuff - handle this later
	public void changeComponent(int position, Object newValue) {
		ObjectAdapter a = getChildAdapterMapping(Integer.toString(position)); 				
		if (a.getPropertyClass().equals(RemoteSelector.getClass(newValue))) {
			// Dont need a new ui repn
			ObjectAdapter adapter = getChildAdapterMapping(Integer.toString(position));
			adapter.refreshValue(newValue);
		}
		else {
			// Delete the components
			// representing the old Object
			try {
				//String fieldName = Integer.toString(position);
				int fieldName = position;
				ObjectAdapter oldAdapter = getDynamicChildAdapterMapping(fieldName);
				//((Container) getUIComponent()).remove(oldAdapter.getGenericWidget());
				//oldAdapter.removeUIComponentFromParent(((Container) getUIComponent()));
				oldAdapter.removeUIComponentFromParent(this);				deleteChildAdapter(fieldName);
				ObjectAdapter adapter = createChildObjectAdapter(
						//(Container)(getUIComponent()), 
						/*this,*/ newValue, RemoteSelector.getClass(newValue), position,  oldAdapter.getPropertyName(), computeAndMaybeSetViewObject(), false);
				/*
				uiObjectAdapter adapter = uiGenerator.createObjectAdapter(
						//(Container)(getUIComponent()), 
						this, newValue, newValue.getClass(), position, getViewObject(), false);
				*/
				if (oldAdapter.isUnEditable())
					adapter.setUneditable();
				else
					adapter.setEditable();
				//adapter.setUneditable(oldAdapter.isUnEditable());
				setChildAdapterMapping(fieldName, adapter);
				adapter.setParentAdapter(this);
				moveGenericWidget(adapter.getGenericWidget(), position);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public static Vector toVector(Enumeration e) {
		Vector retVal = new Vector();
		while (e.hasMoreElements())
			retVal.addElement(e.nextElement());
		return retVal;	}
	public  boolean isChildReadable(ObjectAdapter adapter) {
		return true;
		
	}
	public  boolean isChildWriteable(ObjectAdapter adapter) {
		return true;
		
	}	public Hashtable getOriginalComponents() {		//System.out.println("get value called");
		
		//return getRealObject();		
		
				//if (getViewObject() instanceof Vector) {
		Hashtable h = new Hashtable();
		for (int i=0; i< dynamicAdapterVector.size(); i = i + 2) {
			ObjectAdapter keyAdapter = getChildAdapterMapping(Integer.toString(i));			ObjectAdapter elementAdapter = getChildAdapterMapping(Integer.toString(i + 1));
			//Object key = keyAdapter.getOriginalValue();
			Object key = keyAdapter.getPreviousRealObject();			//Object element = elementAdapter.getOriginalValue();
			Object element = elementAdapter.getPreviousRealObject();			if (key != null && element != null)
				h.put(key, element);
		}
		return h;
		//} 
		//return null;
		
				
		
	}	public  void addHashtableComponentsPropagating(int from) {
		addHashtableComponents(from);
		if (!isTopAdapter())		   parent.descendentsCreated();		else
			uiGenerator.deepProcessAttributes(this, false);		//recursivelyCheckIfNoVisibleChildren(this);
	}  	
	public  void setDefaultSynthesizedAttributes() {		super.setDefaultSynthesizedAttributes();		setDefaultSynthesizedAttributes(0);
		redoDynamicChildren();
	}
	
	void redoDynamicChildren() {
		addNewlyVisibleChildren();
		removeInvisibleChildren();
		if (!sort());
			resetTreeChildrenVector();
		recomputeIndices();
	}	
	
	// Abstract methods for properties
	// setValue() should partition the newValue
	// to the fields of the Vector.
	// Has to be corrected.
	public void refreshValue(Object newValue1, boolean forceUpdate) {
		//try {		//System.out.println("new value");
		//System.out.println ("vector set value called" + newValue1);		
		//super.refreshValue(newValue1, forceUpdate);
		super.refreshValueButNotAtomic(newValue1, forceUpdate);
		//if (isAtomic()) return;		if (newValue1 == null) return;
		
		//Object newV = uiGenerator.getViewObject(newValue1);
		Object newV;
		if (newValue1 != getRealObject())
			 newV = computeViewObject(getParentAdapter(), newValue1);
		else
			newV = computeAndMaybeSetViewObject();

		//setRealObject(newValue1);	
		//Object newV = getViewObject();		//Hashtable newValue = uiBean.toHashtable( newV); 
		
		//Hashtable currentValue = (Hashtable) getValue();		Hashtable currentValue = (Hashtable) getOriginalComponents();
		// Do a better than equals() check here
		//pd addition		//Object viewObject = uiGenerator.getViewObject(newValue1);
		//setViewObject(viewObject);		setViewObject(newV);
		//setRealObject(newValue);
		setRealObject(newValue1);				HashtableStructure newValue = getHashtableStructure();		//this.refreshConcreteObject(newValue1);
		this.refreshConcreteObject(newV);
		//uncommenting next two lines
		/*
		if (isAtomic() && getWidgetAdapter() != null) {
			getWidgetAdapter().setUIComponentValue(newV);
			return;
		}
		*/	
		/*
		if (EditorRegistry.getEditorClass(newV.getClass()) == null) {
			
			return;
		}
		*/
		//newValue.setTarget(newValue1);		//end pd addition
		boolean rebuild = false;
		boolean hasChanged = false;		//Vector newKeys = toVector(newValue.keys());	  		//Vector newElements = toVector(newValue.elements());
		
		//Hashtable newVisibleValues = getVisibleChildren(newValue);
		Hashtable newVisibleValues = toHashtable(newValue);
		
		if (newVisibleValues == null) {
			newVisibleValues = receivedTable;
		}		
		//Vector newKeys = newValue.keys();	
		Vector newKeys = OEMisc.toVector(newVisibleValues.keys());
		//System.out.println("new keys" + newKeys);		//Vector newElements = newValue.elements();
		Vector newElements = OEMisc.toVector(newVisibleValues.elements());
		
		
		
		if (newKeys == null) {
			newKeys = receivedKeys;
		}
		//System.out.println("new Elements" + newElements);				Vector curElements = null;	  
		Vector curKeys = null;
		if (currentValue != null) {			curElements = toVector(currentValue.elements());	  
			curKeys = toVector(currentValue.keys());		}
		//boolean rebuild = true;	  		int rebuildFrom = 0;		if (currentValue == null || invisibleChildExisted || getForceRebuild()) {			rebuild = true;		//} else if (currentValue.size() == newValue.size()) {
		} else if (currentValue.size() == newKeys.size()) {			if (currentValue.size() == 0) return;
			/*			curKeys = toVector(currentValue.keys());			newKeys = toVector(newValue.keys());			Vector curElements = toVector(currentValue.elements());			Vector newElements = toVector(newValue.elements());			*/
			for (int i=0; i<currentValue.size(); i++) {
								Object curKey = curKeys.elementAt(i);				Object newKey = newKeys.elementAt(i);
								Object curElement = curElements.elementAt(i);				Object newElement= newElements.elementAt(i);				
								/*
				Object curElement = vectorElementAt (currentValue, i);				Object newElement = vectorElementAt (newValue, i);
				*/				//if (curElement == newElement) && (curKey == newKey))) break;				if ((curElement == null) || (newElement == null) || (curKey == null) || (newKey == null)) {					rebuild = true;					break;				}				//if (curElement.equals(newElement) && (curKey.equals(newKey))) continue;
				if (OEMisc.equals(curElement, newElement) && (OEMisc.equals(curKey, newKey))) continue;
				else hasChanged = true;
				//if (!currentValue.elementAt(i).getClass().equals(newValue.elementAt(i).getClass())) {				if (!curElement.getClass().equals(newElement.getClass()) ||					!curKey.getClass().equals(newKey.getClass())) {
					rebuild = true;
					break;
				}
			}
		}
		else {
			rebuild = true;
		}

		if (rebuild) {			//System.out.println("doing rebuild");
			// First delete all contained components
			//Container widget = (Container) getUIComponent();
			Object widget = getUIComponent();			
			if (currentValue != null && currentValue.size() != 0 && childrenCreated) {
				
				//System.out.println("checking widget");	
								// this will be duplicated by cleanup, but this is probably more efficient
				if (widget != null && !isAtomic())
				
					removeDynamicWidgets();
				
				
				
				// Remember to remove the Adapter children!!
				//System.out.println("checking adapter mapping");

				//cleanUpDescendents(this);				if ((dynamicAdapterVector != null) && (dynamicAdapterVector.size() != 0)) {
					clearDynamicAdapterMapping();
					//dynamicAdapterMapping.removeAllElements();					keyAdapterMapping.clear();					elementAdapterMapping.clear();
					sortedKeyAdapters.clear();
					sortedKeys.clear();
					sortedElementAdapters.clear();
				}				resetChildrenVector(numFeatures);
				// Redraw the whole Vector				/*
				uiGenerator.uiAddVectorComponents(widget, 
				this, 
				newValue);				*/
			}
			//System.out.println("before try");			//try {
						this.addHashtableComponents(0);
			/*
			// let us try this now
			if (isAtomic() && getWidgetAdapter() != null) {
				getWidgetAdapter().setUIComponentValue(newV);
				return;
			}
			*/			//System.out.println("invaqlidating");			if (widget != null && !isAtomic())
				getWidgetAdapter().invalidate();
			//widget.invalidate();
			//System.out.println("redoing exapnd");			uiGenerator.deepRedoExpand(this);
			if (getWidgetAdapter() != null) {
				int numStaticChildren = this.getNumberOfDisplayedStaticChildren();
				uiGenerator.deepCreateChildren(this, numStaticChildren , dynamicAdapterVector.size());			uiGenerator.deepProcessAttributes(this, false);
			}
			//System.out.println("deep eliding");
			//System.out.println(this.getGenericWidget());
			//System.out.println(this.getGenericWidget().getUIFrame());			/*
			if (getGenericWidget() != null && getGenericWidget().getUIFrame() != null)				this.getGenericWidget().getUIFrame().deepElide(this);
			*/
			/*
			if (checkIfNoVisibleChildren(this))
				return;
				*/
			//if (!isAtomic() && isTreeNode())	
				uiGenerator.deepElide(this.getTopAdapter());
				uiFrame.validate();
			//Message.debug("Added a check before deeep elide, may cause problems");
			//getUIFrame().deepElide(this);			//} catch (Exception e) {
			//		System.out.println("Strange exception again");
			//}
			/*
			if (checkIfNoVisibleChildren(this))
				return;\
				*/
			// not sure about this
			//uiFrame.deepElide(this.getTopAdapter());
			
		} else if (hasChanged){
			ObjectAdapter keyAdapter, elementAdapter;
			for (int i=0; i<newKeys.size(); i++) {
				//keyAdapter = getKeyAdapterMapping(newKeys.elementAt(i));
				keyAdapter = getChildAdapterMapping(Integer.toString(2*i));				elementAdapter = getChildAdapterMapping(Integer.toString(2*i + 1));				keyAdapter.refreshValue(newKeys.elementAt(i));				elementAdapter.refreshValue(newElements.elementAt(i));
			}
			if (isAtomic() && getWidgetAdapter() != null)
				//getWidgetAdapter().setUIComponentValue(newV);
				setValueOfAtomicOrPrimitive(newV);
			else {
				/*
				if (checkIfNoVisibleChildren(this))
					return;
				*/	
				uiGenerator.deepElide(this.getTopAdapter());
			}
		}		/*  
		} else {
		System.out.println("Non vector type passed to Vector adapter in a setValue()");  
		}
		*/
		// doing this earlier
		/*
	
		if (isAtomic() && getWidgetAdapter() != null)
			getWidgetAdapter().setUIComponentValue(newV);
			*/
		// why do this to hashtable?
		/*		if (checkIfNoVisibleChildren(this))			return;
					uiFrame.deepElide(this.getTopAdapter());
		*/
		/*		} catch (Exception e) {
			System.out.println(e);
			//e.printStackTrace();		}		*/
	}
	public boolean isKeyVisible (Object realObject) {
		if (realObject == null)
			return true;
		ObjectAdapter keyAdapter = deletedKeyAndValueAdapters.get(realObject);
		return keyAdapter == null || keyAdapter.isVisible();
		
	}
	
	public Hashtable getVisibleChildren (HashtableStructure hs) {
		Hashtable retVal = new Hashtable();
		Vector keys = hs.keys();
		for (int i = 0; i < keys.size(); i++) {
			Object nextKey = keys.elementAt(i);
			if (isKeyVisible(nextKey))
				retVal.put (nextKey, hs.get(nextKey));
		}
		return retVal;
	}
	public Hashtable toHashtable (HashtableStructure hs) {
		Hashtable retVal = new Hashtable();
		Vector keys = hs.keys();
		if (keys == null) return null;
		for (int i = 0; i < keys.size(); i++) {
			Object nextKey = keys.elementAt(i);
			//if (isKeyVisible(nextKey))
			retVal.put (nextKey, hs.get(nextKey));
		}
		return retVal;
	}
	
	public boolean isVisible (ObjectAdapter child) {
		return child.isVisible();
		/*
		if (child.isKeyAdapter())
			return child.isVisible();
		else {
			uiObjectAdapter keyAdapter = child.getKeyAdapter();
			return keyAdapter == null || keyAdapter.isVisible();
			
		}
		*/
	}
//	 alas, this is called only from one place
	public void addDynamicChildInTables (int index,  ObjectAdapter a) {
		//setChildAdapterMapping(Integer.toString(index), a);
		int adjustedIndex = Math.min(index, dynamicAdapterVector.size());		
		setDynamicChildAdapterMapping(adjustedIndex, a);
		//a.setIndex(numFeatures + index);
		//a.setVectorIndex(index);
		setChildAdapterMapping(adjustedIndex + getNumberOfStaticChildren(), a);
		if (a.isKeyAdapter()) {
			int keyIndex = index / 2;
			int adjustedKeyIndex = Math.min(keyIndex, sortedKeyAdapters.size());
			sortedKeyAdapters.insertElementAt(a, adjustedKeyIndex);
		} else {
			int elementIndex = (index - 1) / 2;
			int adjustedElementIndex = Math.min(elementIndex, sortedElementAdapters.size());
			sortedElementAdapters.insertElementAt(a, adjustedElementIndex);
		}
		
	}
	
	// should be moved to container adapter perhaps
	public void addNewlyVisibleChildren() {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return ; // reversing ret val
		// if (isAtomic()) return true;
		// if (isLeafOfAtomic()) return true;
		/*
		if (isLeafOfAtomic())
			return ;
			*/
		// boolean childMadeVisible = false;
		// if (from == 0 && to == adapterMapping.size())
		// if (from == numStaticChildren && to == adapterMapping.size() +
		// numStaticChildren)
		
		// the following makes so sense to me, maybe the whole range means that
		// the class components have not been added
		Enumeration<ObjectAdapter> elements = deletedKeyAndValueAdapters.elements();
		while (elements.hasMoreElements()) {
			
			// uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = elements.nextElement();
			//if (a.isVisible()) continue;
			if (isVisible(a)) 
				continue;
			// key wil be recomputed before value, so we should be fine
			a.recomputeAttributes();
			//if (a.isVisible()) {	
			if (isVisible(a)) {
							// if (a.isVisible()) {
				// assuming it is going back at the position from which it was deleted
				addDynamicChildInTables(a.getVectorIndex(), a);
				deletedKeyAndValueAdapters.remove(a.getRealObject());
				registerAsListener(a);
				
							// no need to reset indices
							//
							// a.registerAsListener();
							
							uiGenerator.deepProcessAttributes(a);
							// childMadeVisible = true;
						}
		}
				
		
	}
	
	boolean invisibleChildExisted = true;
	
	public void removeInvisibleChildren() {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return ; // reversing ret val
		// if (isAtomic()) return true;
		// if (isLeafOfAtomic()) return true;
		/*
		if (isLeafOfAtomic())
			return ;
			*/		
		// boolean childMadeVisible = false;
		// if (from == 0 && to == adapterMapping.size())
		// if (from == numStaticChildren && to == adapterMapping.size() +
		// numStaticChildren)
		
		// the following makes so sense to me, maybe the whole range means that
		// the class components have not been added
		
		Vector<ObjectAdapter> copyDynamicAdapters = (Vector) dynamicAdapterVector.clone();

		//Enumeration<uiObjectAdapter> elements = dynamicAdapterMapping.elements();
		Enumeration<ObjectAdapter> elements = copyDynamicAdapters.elements();
		//for (int i = 0; i < adapterMapping.size(); i++) {
		while (elements.hasMoreElements()) {
			//uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a = elements.nextElement();
			a.recomputeAttributes();
				//if (a.isVisible()) 
				if (isVisible(a)) 
					continue;
				else {
					// as adapter mapping is a table, deleting and inserting
					// into it should not matter
					invisibleChildExisted = true;
					deleteDynamicChildForReuse(a);
					a.cleanUpForReuse();
				}
			}
			
	}
	public void setDynamicPossiblyInvisibleChildIndices (int index,  ObjectAdapter a) {
		//maybe we also need dynamic Index
		//setChildAdapterMapping(Integer.toString(index), a);
		a.setIndex(numFeatures + index);
		a.setVectorIndex(index);
		//setChildAdapterMapping(a);
		
	}
	public void recomputeIndices() {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return ; // reversing ret val
		// if (isAtomic()) return true;
		// if (isLeafOfAtomic()) return true;
		if (isLeafOfAtomic())
			return ;		
		// boolean childMadeVisible = false;
		// if (from == 0 && to == adapterMapping.size())
		// if (from == numStaticChildren && to == adapterMapping.size() +
		// numStaticChildren)
		
		// the following makes so sense to me, maybe the whole range means that
		// the class components have not been added
		for (int i = 0; i < dynamicAdapterVector.size(); i++) {
		
			//uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			ObjectAdapter a =  dynamicAdapterVector.elementAt(i);
			setDynamicPossiblyInvisibleChildIndices(i, a);
		}
	}
		public  boolean addChildUIComponentsBasic() {
		//if (!childrenCreated) return true;
		if (!childrenCreated) return false;		//if (isAtomic()) return true;		//if (isLeafOfAtomic()) return true;
		if (isLeafOfAtomic()) return false;
		boolean retVal = false;
		//if (!staticComponentsAdded) {
		    retVal = super.addChildUIComponentsBasic();
		    //staticComponentsAdded = false;
		//}
		    /*
		addNewlyVisibleChildren();
		removeInvisibleChildren();
		if (!sort());
			resetTreeChildrenVector();
		recomputeIndices();
		*/
		// sort may do this already
		//resetTreeChildrenVector();		//Enumeration displayedChildren = getHTChildren();		//Enumeration displayedChildren = getChildren();
		Enumeration displayedChildren = getDynamicChildAdapters();
		int currentIndex = getNumberOfDisplayedStaticChildren();
		while (displayedChildren.hasMoreElements()) {
			ObjectAdapter a = (ObjectAdapter) displayedChildren.nextElement();
			a.setIndex(currentIndex);
			currentIndex++;
			retVal = a.processPreferredWidget() | retVal;		}		/*
		for (int i=0; i< adapterMapping.size(); i++) {
			uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			a.processPreferredWidget();		}		*/
		//return false;
		return retVal;	}
	/*
	public  boolean addChildUIComponents() {
		boolean retVal = addChildUIComponentsBasic();
		if (retVal) getWidgetAdapter().childComponentsAdded();
		return retVal;
	}
	*/

	public Object getValue() {		//System.out.println("get value called");
		
		//return getRealObject();		
		
				//if (getViewObject() instanceof Vector) {
		Hashtable h = new Hashtable();
		for (int i=0; i< dynamicAdapterVector.size(); i = i + 2) {
			ObjectAdapter keyAdapter = getChildAdapterMapping(Integer.toString(i));			ObjectAdapter elementAdapter = getChildAdapterMapping(Integer.toString(i + 1));
			Object key = keyAdapter.getValue();			Object element = elementAdapter.getValue();			if (key != null && element != null)
				h.put(key, element);
		}
		return h;
		//} 
		//return null;
				
		
	}
	/*
	public Enumeration getChildren () {	return adapterMapping.elements();	}	*/
	public Enumeration getAllChildren () {
		Vector v = new Vector();
		Enumeration properties = super.getChildren();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		properties = dynamicAdapterVector.elements();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		return v.elements();		//return adapterMapping.elements();
		//return mapping.elements();	}	
	public Enumeration getChildrenWithoutValues () {		
		Vector v = new Vector();
		Enumeration properties = super.getChildren();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		properties = keyAdapterMapping.elements();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		return v.elements();		//return adapterMapping.elements();
		//return mapping.elements();
		
		//return childrenVector().elements();	}
	
	boolean keysChildren, elementsChildren, allChildren;
	
	public boolean elideChildrenByDefault() {
		return !keysChildren;
	}
	
	public Vector getChildrenVector () {
		//return treeChildrenVector();
		return treeChildrenVector;
		/*
		Vector v = new Vector();
		//
		 //not sure why the loop below existed
		//Enumeration properties = super.getChildren();
		//while (properties.hasMoreElements())
			//v.addElement(properties.nextElement());
		//properties = keyAdapterMapping.elements();		//
		
		keysChildren = false; 
		elementsChildren = false;
		allChildren = false;
		Enumeration properties;
		
		if (!compositeKey && !compositeElement) {
			properties = this.getAllChildren();
			allChildren = true;
		} else if (compositeKey) {
			properties = getKeys();
			keysChildren = true;
		} else	 {				properties = getElements();
			elementsChildren = true;
		}
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		return v;
		*/		//return adapterMapping.elements();
		//return mapping.elements();	}
	public void clearDynamicAdapterMapping() {
		
		for (int i=0; i< dynamicAdapterVector.size(); i = i + 2) {
			ObjectAdapter keyAdapter = getDynamicChildAdapterMapping(i);
			Object keyObject = keyAdapter.getRealObject();
			if (keyObject != null)
				deletedKeyAndValueAdapters.put(keyObject, keyAdapter);
			ObjectAdapter elementAdapter = getDynamicChildAdapterMapping(i + 1);
			Object elementObject = elementAdapter.getRealObject();
			if (elementObject != null)
				deletedKeyAndValueAdapters.put(elementObject, elementAdapter);
			cleanUpForReuse(keyAdapter);
			cleanUpForReuse(elementAdapter);
			//keyAdapter.cleanUpForReuse();
			//elementAdapter.cleanUpForReuse();
			//deleteDynamicChildForReuse(keyAdapter);
			//deleteDynamicChildForReuse(elementAdapter);
			/*
			Object key = keyAdapter.getValue();
			Object element = elementAdapter.getValue();
			if (key != null && element != null)
				h.put(key, element);
				*/
		}
		dynamicAdapterVector.removeAllElements();
		
		//} 
		//return null;
		
		
		
	}	public Vector treeChildrenVector () {
		//String childrenType = (String) this.getTempAttributeValue(AttributeNames.HASHTABLE_CHILDREN);
		String childrenType = (String) this.getHashtableChildren();
		//boolean includeKeys = childrenType.equals(AttributeNames.KEYS_ONLY) || childrenType.equals(AttributeNames.KEYS_AND_VALUES);
		//boolean includeValues = childrenType.equals(AttributeNames.VALUES_ONLY) | childrenType.equals(AttributeNames.KEYS_AND_VALUES);
		Vector v = new Vector();
		Enumeration properties = super.getChildren();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		Enumeration children = getDynamicChildAdapters() ;
		/*
		if (childrenType.equals(AttributeNames.KEYS_ONLY))
			children = getKeys();
		else if (childrenType.equals(AttributeNames.VALUES_ONLY))
			children = getElements();
		else
			children = getKeysAndElements();
			*/
		while (children.hasMoreElements()) {
			//if (includeKeys)				
				v.addElement(children.nextElement());
			
		}
		
		/*
		Enumeration keys = keyAdapterMapping.elements();		
		Enumeration elements = elementAdapterMapping.elements();
		
		while (keys.hasMoreElements()) {
			//if (includeKeys)				
				v.addElement(keys.nextElement());
			
		}
		*/
		/*
		while (elements.hasMoreElements())
			v.addElement(elements.nextElement());
			*/
	    return v;		//return adapterMapping.elements();
		//return mapping.elements();	}
	public Vector treeChildrenVectorOld () {
		String childrenType = (String) this.getTempAttributeValue(AttributeNames.HASHTABLE_CHILDREN);
		//boolean includeKeys = childrenType.equals(AttributeNames.KEYS_ONLY) || childrenType.equals(AttributeNames.KEYS_AND_VALUES);
		//boolean includeValues = childrenType.equals(AttributeNames.VALUES_ONLY) | childrenType.equals(AttributeNames.KEYS_AND_VALUES);
		Vector v = new Vector();
		Enumeration properties = super.getChildren();
		while (properties.hasMoreElements())
			v.addElement(properties.nextElement());
		Enumeration children;
		if (childrenType.equals(AttributeNames.KEYS_ONLY))
			children = getKeys();
		else if (childrenType.equals(AttributeNames.VALUES_ONLY))
			children = getElements();
		else
			children = getKeysAndElements();
		while (children.hasMoreElements()) {
			//if (includeKeys)				
				v.addElement(children.nextElement());
			
		}
		/*
		Enumeration keys = keyAdapterMapping.elements();		
		Enumeration elements = elementAdapterMapping.elements();
		
		while (keys.hasMoreElements()) {
			//if (includeKeys)				
				v.addElement(keys.nextElement());
			
		}
		*/
		/*
		while (elements.hasMoreElements())
			v.addElement(elements.nextElement());
			*/
	    return v;
		//return adapterMapping.elements();
		//return mapping.elements();
	}
	public  Enumeration getDynamicChildAdapters() {
		//String childrenType = (String) this.getTempAttributeValue(AttributeNames.HASHTABLE_CHILDREN);
		String childrenType = (String) this.getHashtableChildren();
		Enumeration children;
		if (childrenType.equals(AttributeNames.KEYS_ONLY))
			children = getKeys();
		else if (childrenType.equals(AttributeNames.VALUES_ONLY))
			children = getElements();
		else
			children = getKeysAndElements();
		return children;
		
	}
	public Enumeration children() {		//System.out.println("children called");
		if (!childrenCreated)
			createChildrenPropagating();		if (onlyCompositeChild != null) {
			return onlyCompositeChild.children();		}
		//return getChildren();
		return getChildrenWithoutValues();
		//return getKeys();	}
	Vector treeChildrenVector = treeChildrenVector();
	public ObjectAdapter getChildAdapterAt(int childIndex) {		
		//System.out.println("get child  for" + childIndex);		if (!childrenCreated)
			createChildrenPropagating();
		//System.out.println("printing agaoin");
		//System.out.println(childrenVector);		
		//System.out.println("end of vector");
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterAt(childIndex);		return (ObjectAdapter) getChildrenVector().elementAt(childIndex);
	}	public TreeNode getChildAt(int childIndex) {
		//System.out.println("get child  for" + childIndex);		if (!childrenCreated)
			createChildrenPropagating();
		//System.out.println("printing agaoin");
		//System.out.println(childrenVector);		
		//System.out.println("end of vector");
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterAt(childIndex);		return (ObjectAdapter) treeChildrenVector.elementAt(childIndex);		/*
		//System.out.println("get child  for" + childIndex);		return (TreeNode) getChildAdapterAt(childIndex);		*/
	}
	public int getChildCount() {		//return getChildAdapterCount();
				//System.out.println("get child count");
		if (!childrenCreated)
			createChildrenPropagating();
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildCount();		//System.out.println("returning "+ childrenVector.size());
		return treeChildrenVector.size();
			}	/*
	public int getChildAdapterCount() {		//System.out.println("get child count");
		if (!childrenCreated)
			createChildrenPropagating();
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterCount();		//System.out.println("returning "+ childrenVector.size());
		return childrenVector().size();	}
	*/	public Enumeration getHTChildren () {
		return this.treeChildrenVector.elements();
		//return getKeys();		//return adapterMapping.elements();
		//return mapping.elements();	}
	public Enumeration getChildren () {		return getChildrenVector().elements();
		//return getChildrenWithoutValues();	}	public Enumeration getKeys () {
		return sortedKeyAdapters.elements();		//return keyAdapterMapping.elements();	}	public Enumeration getElements() {
		return sortedElementAdapters.elements();		//return elementAdapterMapping.elements();	}
	
	public Enumeration getKeysAndElements() {
		Enumeration keys = getKeys();
		Enumeration elements = getElements();
		Vector keysAndElements = new Vector();
		while (keys.hasMoreElements()) {
			keysAndElements.addElement(keys.nextElement());
			keysAndElements.addElement(elements.nextElement());
		}
		return keysAndElements.elements();
		
	}

	// Abstract method implementation for naming
	// mechanism.
	public ObjectAdapter getChildAdapterMapping(String fieldName) {
		//System.out.println("getChildAM " +  fieldName + " " + adapterMapping.size()+ " " + this.getID());
		ObjectAdapter retVal;		try {
			int index = Integer.parseInt(fieldName);
			return getDynamicChildAdapterMapping(index);
			//return (uiObjectAdapter) dynamicAdapterMapping.elementAt(index);
		} catch (NumberFormatException e) {			retVal = super.getChildAdapterMapping(fieldName);			if (retVal == null) {
				System.out.println("Illegal argument passed to uiHashtablerAdapter:getChildAdapterMapping():" + fieldName);				System.out.println("Super ChidlrenCreated" + super.childrenCreated);
				return null;			} else				return retVal;
		}
	}
	public ObjectAdapter getDynamicChildAdapterMapping(int index) {
		//System.out.println("getChildAM " +  fieldName + " " + adapterMapping.size()+ " " + this.getID());
		ObjectAdapter retVal;
		try {
			//int index = Integer.parseInt(fieldName);
			return (ObjectAdapter) dynamicAdapterVector.elementAt(index);
		} catch (Exception e) {
			e.printStackTrace();
				return null;
			
		}
	}

	public void setChildAdapterMapping(String fieldName, ObjectAdapter adapter) {
		
		//System.out.println("setChildAM " + fieldName + " " + adapter.getID() + " " + this.getID());		try {
			int index = Integer.parseInt(fieldName);
			setDynamicChildAdapterMapping(index, adapter);
			//dynamicAdapterMapping.insertElementAt(adapter, index);
		} catch (NumberFormatException e) {			super.setChildAdapterMapping(fieldName, adapter);
			//System.out.println("Illegal argument passed to uiHashtableAdapter:getChildAdapterMapping():" + fieldName);
		}
	}
	public void setDynamicChildAdapterMapping(int index, ObjectAdapter adapter) {
		
		//System.out.println("setChildAM " + fieldName + " " + adapter.getID() + " " + this.getID());
		try {
			//int index = Integer.parseInt(fieldName);
			dynamicAdapterVector.insertElementAt(adapter, index);
			adapter.setVectorIndex(index);
			adapter.setIndex(getNumberOfStaticChildren() + index);
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Illegal argument passed to uiHashtableAdapter:getChildAdapterMapping():" + fieldName);
		}
	}	/*
	public uiObjectAdapter getChildAdapterMapping(String fieldName) {
	try {
	int index = Integer.parseInt(fieldName);
	return (uiObjectAdapter) adapterMapping.elementAt(index);
	} catch (NumberFormatException e) {
	System.out.println("Illegal argument passed to uiHashtableAdapter:getChildAdapterMapping()");
	return null;
	}
	}

	public void setChildAdapterMapping(String fieldName, uiObjectAdapter adapter) {
	try {
	int index = Integer.parseInt(fieldName);
	adapterMapping.insertElementAt(adapter, index);
	} catch (NumberFormatException e) {
	System.out.println("Illegal argument passed to uiVectorAdapter:getChildAdapterMapping()");
	}
	}	*/

	public String getChildAdapterRealIndex(ObjectAdapter adapter) {
		return Integer.toString(dynamicAdapterVector.indexOf(adapter));
	}	public ObjectAdapter getChildAdapterAtIndex(String key) {
		return (ObjectAdapter) dynamicAdapterVector.elementAt(Integer.parseInt(key));	}

	//public void deleteChildAdapter(String fieldName) {
	public void deleteChildAdapter(int position) {
		//int position;
		try {
			//position = Integer.parseInt(fieldName);
			dynamicAdapterVector.removeElementAt(position);
		} catch (Exception e) {
		}
	}	public Enumeration getChildAdapters() {		//return getChildren();
		// to get the order right
		return treeChildrenVector.elements();
		//return adapterMapping.elements();
	}	/*

	public Enumeration getChildAdapters() {
	return adapterMapping.elements();
	}
	*/	public ObjectAdapter getKeyAdapterMapping(Object key) {
		try {
			return (ObjectAdapter) keyAdapterMapping.get(key);
		} catch (NumberFormatException e) {
			System.out.println("Illegal argument passed to uiHashtableAdapter:getKeyAdapterMapping()");
			return null;
		}
	}

	public void setKeyAdapterMapping(Object key, ObjectAdapter adapter) {
		try {
			keyAdapterMapping.put(key, adapter);
			sortedKeys.add(key);
			sortedKeyAdapters.add(adapter);
		} catch (NumberFormatException e) {
			System.out.println("Illegal argument passed to uiHashtableAdapter:setKeyAdapterMapping()");
		}
	}


	public void deleteKeyAdapter(Object key) {
		int position;
		try {
			ObjectAdapter keyAdapter = getKeyAdapterMapping(key);			ObjectAdapter elementAdapter = getElementAdapterMapping(key);
			dynamicAdapterVector.removeElement(keyAdapter);
			dynamicAdapterVector.removeElement(elementAdapter);
			keyAdapterMapping.remove(key);
			elementAdapterMapping.remove(key);
		} catch (NumberFormatException e) {
		}
	}

	public Enumeration<ObjectAdapter> getKeyAdapters() {
		return sortedKeyAdapters.elements();
		//return keyAdapterMapping.elements();
	}	public ObjectAdapter getElementAdapterMapping(Object key) {
		try {
			return (ObjectAdapter) elementAdapterMapping.get(key);
		} catch (Exception e) {
			System.out.println("Illegal argument passed to uiHashtableAdapter:getKeyAdapterMapping()");
			return null;
		}
	}

	public void setElementAdapterMapping(Object key, ObjectAdapter adapter) {
		try {
			elementAdapterMapping.put(key, adapter);
			sortedElementAdapters.addElement(adapter);
		} catch (NumberFormatException e) {
			System.out.println("Illegal argument passed to uiHashtableAdapter:setKeyAdapterMapping()");
		}
	}



	public Enumeration getElementAdapters() {
		return sortedElementAdapters.elements();
		//return keyAdapterMapping.elements();
	}


	// Method from uiReplaceableChildren
	public void replaceAttributedObject(ObjectAdapter child,
										Object newValue) {
		int position = dynamicAdapterVector.indexOf(child);
		changeComponent(position, newValue);
	}	/*
	public  void createChildren() {
		
		if (this.childrenCreated)
			return;
		//super.createChildren();		uiAddHashtableComponents(0);
	}	*/
	/*	public  void createChildren() {
	
	if (this.childrenCreated)
	return;	uiAddHashtableComponents(0);
	}	*/
	
	transient static boolean showHashtableComponentLabels = true;	public boolean isHomogeneous() {	  return homogeneousTable;
	}	boolean homogeneousTable = true;	boolean foundUnlabeledComposite;	int hSize;
	
	public  ObjectAdapter createChildObjectAdapter(
			  
			  /*uiObjectAdapter adaptor,*/ Object obj1, ClassProxy cl, int posn, String name, Object parentObject, boolean propertyFlag) {
		//boolean generate = true;
		ObjectAdapter retVal = null;
		if (obj1 != null) {
			retVal = deletedKeyAndValueAdapters.get(obj1);
			// why would you do this!
			/*
			if (retVal != null)
				deleteDynamicChildForReuse ( retVal);
				*/
		}
		if (retVal != null)
			return retVal;
		
		return uiGenerator.createObjectAdapter(this, obj1, cl, posn, name, parentObject, propertyFlag);
		
	}
	public void removeChildrenInTables() {
		super.removeChildrenInTables();
		for (int i = 0; i < dynamicAdapterVector.size(); i++) {
			ObjectAdapter childAdapter = dynamicAdapterVector.get(i);
			deletedKeyAndValueAdapters.put(childAdapter.getRealObject(), childAdapter);
		}
		dynamicAdapterVector.removeAllElements();
		keyAdapterMapping.clear();
		elementAdapterMapping.clear();
		sortedKeys.clear();
		sortedElementAdapters.clear();
		sortedKeyAdapters.clear();
		treeChildrenVector.clear();
		
	}
	public  void deleteDynamicChildForReuse (ObjectAdapter adapter) {
		Object realObject = adapter.getRealObject();
		if (realObject == null)
			return;
		//deletedKeyAndValueAdapters.put(realObject, adapter);
		
		ObjectAdapter keyAdapter;
		ObjectAdapter valueAdapter;
		Object key = realObject;
		if (keyAdapterMapping != null && keyAdapterMapping.containsKey(realObject)) {
			//keyAdapterMapping.remove(realObject);
			//elementAdapterMapping.remove(realObject);
			keyAdapter = adapter;
			valueAdapter = keyAdapter.getValueAdapter();			
		} else{
			valueAdapter = adapter;
			keyAdapter = adapter.getKeyAdapter();
			key = keyAdapter.getRealObject();
			/*
			if (keyAdapter != null) {
				Object key = keyAdapter.getRealObject();
				if (elementAdapterMapping != null && elementAdapterMapping.containsKey(key))		
					elementAdapterMapping.remove(key);
			}
			*/
		}
		deletedKeyAndValueAdapters.put(key, keyAdapter);
		deletedKeyAndValueAdapters.put(valueAdapter.getRealObject(), valueAdapter);
		if (dynamicAdapterVector.contains(keyAdapter))
			dynamicAdapterVector.remove(keyAdapter);
		if (dynamicAdapterVector.contains(valueAdapter))
			dynamicAdapterVector.remove(valueAdapter);		
		if (mapping.contains(key))
			mapping.remove(adapter);
		if (childrenVector != null && childrenVector.contains(keyAdapter))
			childrenVector.remove(keyAdapter);
		if (childrenVector != null && childrenVector.contains(valueAdapter))
			childrenVector.remove(valueAdapter);
		if (keyAdapterMapping.containsKey(key)) {
			keyAdapterMapping.remove(key);
		}
		if (elementAdapterMapping.containsKey(key)) {
			elementAdapterMapping.remove(key);
		}
		if (sortedKeyAdapters != null && sortedKeyAdapters.contains(keyAdapter))
			sortedKeyAdapters.remove(keyAdapter);
		if (sortedElementAdapters != null && sortedElementAdapters.contains(valueAdapter))
			sortedElementAdapters.remove(valueAdapter);
	}
	
		public  void addHashtableComponents(int from) {		//System.out.println("   UIAHC " + this.getID());
		//this.childrenCreated = true;		//super.createChildren();
		//System.out.println("add vector in vector adapter:" + this);
		
		// should only clear after from
//		uiGenerator.clearVisitedObjectsInSubtree(this);
		
		(new ClearVisitedNodeAdapterVisitor(this)).traverseNonAtomicChildrenContainers();		CompositeAdapter adaptor = this;
		//Container containW = getGenericWidget();
		/*		uiWidgetAdapterInterface wa = adaptor.getWidgetAdapter();		if (wa == null)
		return;		Container containW = (Container) adaptor.getWidgetAdapter().getUIComponent();		*/		//Container containW = null;
		//System.out.println("real object: " + getRealObject());
		//Object obj = uiGenerator.getViewObject(getRealObject());
		Object obj = computeViewObject(getParentAdapter(), getRealObject());		//System.out.println("new value: " + obj);
		adaptor.setViewObject(obj);	
		/*		foundUnlabeledComposite = false;
		int maxLabelLength = 0;		*/
		if (obj == null) return;		ClassProxy inputClass =  RemoteSelector.getClass(obj);	
		/*		if (EditorRegistry.getEditorClass(inputClass) != null) {			childrenCreated = true;
			return;		}
		*/
		
		//if (obj instanceof Vector) {
		//if (obj instanceof Object) {		/*
		Hashtable h = uiBean.toHashtable( obj);		Vector keys = toVector(h.keys());		Vector elements = toVector(h.elements());
		*/		HashtableStructure h = getHashtableStructure();		Vector keys = h.keys();
		if (keys == null) keys = receivedKeys;		//Vector elements = h.elements();
		if (h != null) {
			//Vector v = (Vector) obj;
			//Vector v = uiBean.toVector( obj);			
			//containW.setLayout(new uiGridLayout(1, v.size()));	  			//containW.setLayout(new uiGridLayout(1, v.size(), uiGridLayout.DEFAULT_HGAP, 0));
			/*			boolean horizontalChildren = true;
			homogeneousTable = true;
			Class oldElemClass = null;
			Class curElemClass = null;			Class oldKeyClass = null;
			Class curKeyClass = null;			*/
			//int vSize = vectorSize(obj);
			//hSize = h.size();
			hSize = keys.size();  
			//for (int i=from; i< h.size(); i++) {			for (int i=from; i< hSize; i++) {
				/*				if (hSize != 0) {
				System.out.println("Non empty hashtable!");				return;				}				*/
				/*				Object obj1 = null;
								try {								System.out.println(elementAtMethod);
				if (elementAtMethod != null) {
				Object[] params = {new Integer(i)};				obj1 = elementAtMethod.invoke(obj, params);				}				} catch (Exception e) {
				System.out.println(e);
				break;				}				*/
								
				Object key = keys.elementAt(i);
				//Object obj1 = this.vectorElementAt(v, i);				
				ObjectAdapter a;
				ClassProxy keyClass = RemoteSelector.classProxy(MethodInvocationManager.OBJECT_CLASS);
				if (key != null)
					keyClass = RemoteSelector.getClass(key);
				/*
				a = uiGenerator.createObjectAdapter(
						//containW, 
						adaptor, key, keyClass, 2*i, "key", obj, false);
						*/
				a = createChildObjectAdapter(
						//containW, 
						key, keyClass, 2*i, AttributeNames.ANY_KEY, obj, false);
				if (!getHashtableStructure().isEditableKey(key))
					a.setUneditable();
				/*				if (key == null)					//a = uiGenerator.uiAddComponents(containW, adaptor, obj1, uiMethodInvocationManager.OBJECT_CLASS, 2*i, Integer.toString(i), obj, false);
					a = uiGenerator.createObjectAdapter(
							//containW, 
							adaptor, key, uiMethodInvocationManager.OBJECT_CLASS, 2*i, AttributeNames.KEY_NAME, obj, false);				else
					//a = uiGenerator.uiAddComponents(containW, adaptor, obj1, obj1.getClass(), 2*i, Integer.toString(i), obj, false);					a = uiGenerator.createObjectAdapter(
							//containW, 
							adaptor, key, key.getClass(), 2*i, "key", obj, false);
							*/
				// Add to the mapping table here
				//adaptor.setChildAdapterMapping(Integer.toString(2*i), a);
				setDynamicChildAdapterMapping(2*i, a);				//a.setIndex(i);
				adaptor.setChildAdapterMapping(a);				a.setAdapterType(KEY_TYPE);
				ObjectAdapter childKeyAdapter = a;
				this.setKeyAdapterMapping(key, a);				//String label = "key:" + (i + 1) + ". ";
								//String label = "Key";
								//System.out.println(maxLabelLength + " " + a.getGenericWidget().getLabel().length());
				/*
				if (obj1 != null && !(a instanceof uiPrimitiveAdapter)) {				//System.out.println("found non primitive");			
				curKeyClass = obj1.getClass();				ViewInfo childDesc = ClassDescriptorCache.getClassDescriptor(curKeyClass);								
				//System.out.println(childDesc);			
				//System.out.println(childDesc.getBeanDescriptor().getValue("direction"));				//System.out.println("vector child " + a.getMergedAttributeValue("direction"));
				if (//childDesc.getBeanDescriptor() != null &&				!a.getGenericWidget().isLabelVisible() &&
				//!"horizontal".equals(childDesc.getBeanDescriptor().getValue("direction"))) {
				!"horizontal".equals(a.getMergedAttributeValue("direction"))) {
				//!("horizontal".equals(a.getDirection()))) {				foundUnlabeledComposite = true;							//System.out.println ("found composite");
				horizontalChildren = false;				} 				if (oldKeyClass != null && oldKeyClass != curKeyClass)				homogeneousTable = false;				oldKeyClass = curKeyClass;
				}				*/
				//a.setParentAdapter(adaptor);
				//label = "Value:" + (i + 1) + ". ";				//label = "Value";
				/*				String label = "" + (i + 1);				*/				/*
				if (obj1 instanceof String)
				label = (String) obj1;
				*/
				//Object key = obj1;
				//uiObjectAdapter keyAdapter = a;				//Object val = elements.elementAt(i);
				Object val = h.get(key);
				//Object obj1 = this.vectorElementAt(v, i);
				ClassProxy valClass = RemoteSelector.classProxy(MethodInvocationManager.OBJECT_CLASS);
				if (val != null)
					valClass = RemoteSelector.getClass(val);
				a = createChildObjectAdapter(
						//containW, 
						val, valClass, 2*i +1, "value", obj, false);
				/*				if (val == null)					//a = uiGenerator.uiAddComponents(containW, adaptor, obj1, uiMethodInvocationManager.OBJECT_CLASS, 2*i +1, Integer.toString(i), obj, false);
					a = uiGenerator.createObjectAdapter(
							//containW, 
							adaptor, val, uiMethodInvocationManager.OBJECT_CLASS, 2*i +1, "value", obj, false);				else
					//a = uiGenerator.uiAddComponents(containW, adaptor, obj1, obj1.getClass(), 2*i + 1, Integer.toString(i), obj, false);					a = uiGenerator.createObjectAdapter(
							//containW, 
							adaptor, val, val.getClass(), 2*i + 1, "value", obj, false);
							*/
				// Add to the mapping table here
				if (!getHashtableStructure().isEditableElement(key))
					a.setUneditable();
								a.setAdapterType(VALUE_TYPE);				//adaptor.setChildAdapterMapping(Integer.toString(2*i + 1), a);
				setDynamicChildAdapterMapping(2*i + 1, a);				//a.setIndex(2*i + 1);
				adaptor.setChildAdapterMapping(a);
				this.setElementAdapterMapping(key, a);
				
				
				a.setKey(key);
				a.setKeyAdapter(childKeyAdapter);
				childKeyAdapter.setValueAdapter(a);				/*
				if (a.isLabelled() && i == 0)				a.getGenericWidget().setLabelVisible(true);				else
				a.getGenericWidget().setLabelVisible(false);				*/
								//System.out.println("Adapter" + a + " " + a.getAttributeValue(AttributeNames.LABEL));
				//maxLabelLength = Math.max(maxLabelLength, ((String) a.getAttributeValue(AttributeNames.LABEL)).length());				//maxLabelLength = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());				/*
				maxLabelLength = Math.max(maxLabelLength, label.length());
				*/				//System.out.println(maxLabelLength + " " + a.getGenericWidget().getLabel().length());
				/*
				if (obj1 != null && !(a instanceof uiPrimitiveAdapter)) {				//System.out.println("found non primitive");			
				curElemClass = obj1.getClass();				ViewInfo childDesc = ClassDescriptorCache.getClassDescriptor(curElemClass);								
				//System.out.println(childDesc);			
				//System.out.println(childDesc.getBeanDescriptor().getValue("direction"));				//System.out.println("vector child " + a.getMergedAttributeValue("direction"));
				if (//childDesc.getBeanDescriptor() != null &&				!a.getGenericWidget().isLabelVisible() &&
				//!"horizontal".equals(childDesc.getBeanDescriptor().getValue("direction"))) {
				!"horizontal".equals(a.getMergedAttributeValue("direction"))) {
				//!("horizontal".equals(a.getDirection()))) {				foundUnlabeledComposite = true;							//System.out.println ("found composite");
				horizontalChildren = false;				} 				if (oldElemClass != null && oldElemClass != curElemClass)				homogeneousTable = false;				oldElemClass = curElemClass;
				}				*/
			}			
			/*			if (horizontalChildren && homogeneousTable )
			adaptor.makeColumnTitles();			*/
						ClassDescriptorInterface cdesc = ClassDescriptorCache.getClassDescriptor(RemoteSelector.getClass(obj));			RightMenuManager.getRightMenu(RemoteSelector.getClass(obj), this, obj);			/*
			setDirection();			boolean alignHorizontal = direction.equals("horizontal");			
			if (alignHorizontal) {
			
			adaptor.processDirection("horizontal");
			}			if (//cdesc.getBeanDescriptor() != null &&
			//"horizontal".equals(cdesc.getBeanDescriptor().getValue("direction")))			//"horizontal".equals(adaptor.getMergedAttributeValue("direction"))) 
			//containW.setLayout(new uiGridLayout(1, v.size()));
			alignHorizontal)
			
			containW.setLayout(new uiGridLayout(2, hSize + numFeatures, uiGridLayout.DEFAULT_HGAP, 0));			else 
			//containW.setLayout(new uiGridLayout(features.length, 1));			
			//if (foundUnlabeledComposite && v.size() > 1)
			if (foundUnlabeledComposite && hSize > 1)			//containW.setLayout(new uiGridLayout(v.size(), 1, 0, uiGridLayout.DEFAULT_VGAP));
			containW.setLayout(new uiGridLayout(hSize + numFeatures, 2, 0, uiGridLayout.DEFAULT_VGAP));			else			//containW.setLayout(new uiGridLayout(v.size(), 1)); 			containW.setLayout(new uiGridLayout(hSize + numFeatures, 2)); 
			
			*/		}		
				this.childrenCreated = true;
		resetTreeChildrenVector();
	}	public  void setDefaultAttributes(int from) {
		if (!childrenCreated) return;		if (isAtomic()) return;
		boolean homogeneousTable = true;
		ClassProxy oldElemClass = null;
		ClassProxy curElemClass = null;		ClassProxy oldKeyClass = null;
		ClassProxy curKeyClass = null;					int maxLabelLength = 0;
		int keysSize = dynamicAdapterVector.size()/2;		for (int i=from; i< keysSize; i++) {			ObjectAdapter keyAdapter = getChildAdapterMapping(Integer.toString(i));
			Object obj1 = keyAdapter.computeAndMaybeSetViewObject();
			if (obj1 == null)
				continue;
			curKeyClass = RemoteSelector.getClass(obj1);
			if (oldKeyClass != null && oldKeyClass != curKeyClass) {				homogeneousTable = false;				break;			}							oldKeyClass = curKeyClass;
			ObjectAdapter elemAdapter = getChildAdapterMapping(Integer.toString(2*i+1));
			Object obj2 = elemAdapter.computeAndMaybeSetViewObject();
			if (obj2 !=null) {
			curElemClass = RemoteSelector.getClass(obj2);
			if (oldElemClass != null && oldElemClass != curElemClass) {				homogeneousTable = false;				break;			}							oldElemClass = curElemClass;
			}
		}
				setHomogeneous(homogeneousTable);
	}
	public  void setDefaultAttributes() {		super.setDefaultAttributes();		setDefaultAttributes(0);
	}
		boolean compositeKey = false;	boolean compositeElement = false;	public boolean hasCompositeKey() {
		return compositeKey;	}	public boolean hasCompositeElement() {
		return compositeElement;	}
	boolean defaultLabelValues() {
		Object labelValues = labelValues();
		if (labelValues == null) {
			if (this.getHashtableChildren() == AttributeNames.VALUES_ONLY)
				return true;
			else
				return false;
		} else
			return (Boolean) labelValues;
		
	}
	//int maxDynamicComponentNameLength = 0;
	public  void setDefaultSynthesizedAttributes(int from) {
		if (!childrenCreated) return;		//if (isAtomic()) return;
		boolean horizontalChildren = true;		boolean foundUnlabeledComposite = false;
		compositeKey = false;		compositeElement = false;		//int keysSize = adapterMapping.size()/2;
		//for (int i=from; i< keysSize; i = i + 2) {		for (int i=from; i< dynamicAdapterVector.size()-1; i = i + 2) {
			ObjectAdapter keyAdapter = getChildAdapterMapping(Integer.toString(i));			//System.out.println("KeyAdap " + keyAdapter.getID());
			Object obj1 = keyAdapter.computeAndMaybeSetViewObject();			
			//String keyLabel = "Key";
			//String keyLabel = "";
			String keyLabel = keyAdapter.toObjectString();			//System.out.println(maxLabelLength + " " + a.getGenericWidget().getLabel().length());
			if (obj1 != null && !(keyAdapter instanceof PrimitiveAdapter)) {				//System.out.println("found non primitive");
				compositeKey = true;				//if (!ClassDescriptorCache.toBoolean(keyAdapter.getTempAttributeValue(AttributeNames.LABELLED))
				if (!ClassDescriptorCache.toBoolean(keyAdapter.isLabelled())					//!a.getGenericWidget().isLabelVisible() &&
					//!"horizontal".equals(childDesc.getBeanDescriptor().getValue("direction"))) {
					//&& !"horizontal".equals(a.getMergedAttributeValue("direction"))) {					//&& !"horizontal".equals(keyAdapter.getTempAttributeValue("direction"))) {
					&& !"horizontal".equals(keyAdapter.getDirection())) {
					//!("horizontal".equals(a.getDirection()))) {					foundUnlabeledComposite = true;								//System.out.println ("found composite");
					horizontalChildren = false;
					
				}}
						//keyAdapter.setTempAttributeValue(AttributeNames.LABEL, keyLabel);
			//if (!keyAdapter.getSynthesizedLabel())
			keyAdapter.maybeSetLabel(keyLabel);
			keyAdapter.setTrueLabel(keyLabel);
			
			if (keyLabel != null) {
				int oldMaxDynamicComponentNameLength = maxDynamicComponentNameLength;
			 maxDynamicComponentNameLength = Math.max(keyLabel.length(), maxDynamicComponentNameLength);
			 if (oldMaxDynamicComponentNameLength != maxDynamicComponentNameLength) {
				 maxDynamicComponentName = keyLabel;
			 }
			}
			 
			//System.out.println("Setting label of " + keyAdapter.getID() + keyLabel);
			if (!labelKeys())			//keyAdapter.setTempAttributeValue(AttributeNames.LABELLED, new Boolean(false));
			keyAdapter.setLabelled(new Boolean(false));
						//String elemLabel = "Value";	
			//String elemLabel = keyAdapter.toString();
			String elemLabel = keyAdapter.toObjectString();			ObjectAdapter elemAdapter = getChildAdapterMapping(Integer.toString(i+1));			//System.out.println("Elem Adapter " + elemAdapter.getID());
			Object obj2 = elemAdapter.computeAndMaybeSetViewObject();
			if (obj2 != null && !(elemAdapter instanceof PrimitiveAdapter)) {
				compositeElement = true;				if (!ClassDescriptorCache.toBoolean(elemAdapter.getTempAttributeValue(AttributeNames.LABELLED))					//!a.getGenericWidget().isLabelVisible() &&
					//!"horizontal".equals(childDesc.getBeanDescriptor().getValue("direction"))) {
					//&& !"horizontal".equals(a.getMergedAttributeValue("direction"))) {					&& !"horizontal".equals(elemAdapter.getTempAttributeValue("direction"))) {
					//!("horizontal".equals(a.getDirection()))) {					foundUnlabeledComposite = true;								//System.out.println ("found composite");
					horizontalChildren = false;
					
				}}
			//if (elemAdapter.getNonDefaultMergedAttributeValue(AttributeNames.LABEL) == null) {
			if (elemAdapter.getNonDefaultMergedLabel() == null) {
				if (compositeElement || compositeKey)
				elemAdapter.maybeSetLabel(elemLabel);
				else elemAdapter.setLabel(elemLabel);
			}
			if (!defaultLabelValues())
				elemAdapter.setLabelled(false);
			/*
			if (defaultLabelValues()) {
				elemAdapter.setLabel(elemLabel);
			} else if (compositeElement || compositeKey)			elemAdapter.maybeSetLabel(elemLabel);
			else {
				elemAdapter.maybeSetLabel("");
				//elemAdapter.setLabel("Value");
				//keyAdapter.setLabel("Key");
			}
			*/
			
							}
		//setHomogeneous(homogeneousVector);
		setChildrenHorizontal(horizontalChildren);		
		//setDirection();
	}
	
	public void makeColumnTitles () {
		//Vector children = childrenVector();
		//Vector children = treeChildrenVector;
		Vector children = dynamicAdapterVector;
		//System.out.println("making column titles");
		if (children.size() == 0) return;
		Object firstChild = children.elementAt(0);
		  if (!(firstChild instanceof CompositeAdapter)) return;
		  //((uiContainerAdapter) enum.nextElement()).makeHorizontalColumnTitles();	
		  ((CompositeAdapter) firstChild).makeHorizontalColumnTitles();	
		  for (int i=1; i< children.size(); i++) {
			   Object container =  children.elementAt(i);
			   if (container instanceof CompositeAdapter)
		         //((uiContainerAdapter) enum.nextElement()).setHorizontalLabelVisible(false);
				   ((CompositeAdapter) container).setHorizontalLabelVisible(false);
			   
		  }
		  
		 
		 
	  }
	  
		public  void processSynthesizedAttributesWithDefaults() {
		super.processSynthesizedAttributesWithDefaults();
		processSynthesizedAttributesWithDefaults(0);	}	
		public  void processSynthesizedAttributesWithDefaults(int from) {		if (!childrenCreated) return;		if (isAtomic()) return;
		//System.out.println("add vector in vector adapter:" + this);		//int numProperties = this.getCurrentChildCount();		HashtableAdapter adaptor = this;
		//Container containW = getGenericWidget();
		//Container containW = getGenericWidget().getContainer();		WidgetAdapterInterface wa = adaptor.getWidgetAdapter();		if (wa == null)
			return;		//Enumeration displayedChildren = getHTChildren();
		Enumeration displayedChildren = getChildren();		while (displayedChildren.hasMoreElements()) {
			ObjectAdapter a = (ObjectAdapter) displayedChildren.nextElement();
			//a.getGenericWidget().setLabel((String) (a.getTempAttributeValue(AttributeNames.LABEL)));
			//a.setLabel(a.getLabel());
			/*
			if (a.getLabel() != null) {
			 maxComponentNameLength = Math.max(a.getLabel().length(), maxComponentNameLength);
			 
			 a.propagateAttributesToWidgetShell();
			}
			//a.getGenericWidget().setLabel((String) (a.getLabel()));
			 * 
			 */		}
		/*		for (int i=from; i< adapterMapping.size(); i++) {
			uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));
			a.getGenericWidget().setLabel((String) (a.getTempAttributeValue(AttributeNames.LABEL)));		}		*/
				
		//uncommentng next line		//containW = (Container) adaptor.getWidgetAdapter().getUIComponent();		if (adaptor.childrenHorizontal() &&	adaptor.isHomogeneous()) {
			adaptor.makeColumnTitles();		}
					//adaptor.processAttributeList();
		/*			if (horizontalChildren && homogeneousVector )
				adaptor.makeColumnTitles();		*/		processDirection();
	}	
		public boolean isLeafAdapter() {		if (!super.isLeafAdapter())			return false;
		//System.out.println("Get Value of" + this + " " + getValue());
		//System.out.println("Get Reeal Obkect of" + this + " " + getRealObject());
		//System.out.println("GetView Obkect of" + this + " " + getViewObject());		if (this.getRealObject() == null) return true;
		/*		Hashtable h = uiBean.toHashtable(getRealObject());		if (h == null) return true;
		return h.size() == 0;
		*/
		return getHashtableStructure() == null || getHashtableStructure().keys().size() == 0;
		}
	public boolean isLeaf() {		return isLeafAdapter();
		}
	public boolean isDynamic() {
		return true;
	}	
	void removeIndexedChildren (Container widget) {		int numElements = widget.getComponentCount();
		if (numFeatures == 0)
			widget.removeAll();
		else while (widget.getComponentCount() > numFeatures)				 widget.remove(widget.getComponentCount() - 1);
		/*
		for (int i = numFeatures; i < numElements; i++)		widget.remove(numFeatures);		*/
			}
	void removeDynamicWidgets () {
		//int numElements = widget.getComponentCount();
		//int numElements = childrenVector.size();
		if (numFeatures == 0)
			getWidgetAdapter().removeAll();
		
		// as clean up for reuse will delete it, why bother calling this?
		/*
		Enumeration<uiObjectAdapter> dynamicChildren = getDynamicChildAdapters();
		while (dynamicChildren.hasMoreElements()) {
			uiObjectAdapter child = dynamicChildren.nextElement();
			getWidgetAdapter().remove(child.getIndex(), child);
		}
		*/
		
		
		
	}
	void removeDynamicWidgets (WidgetAdapter widgetAdapter, VirtualContainer widget) {
		int numElements = widget.getComponentCount();
		if (numFeatures == 0)
			widgetAdapter.removeAllProperties(widget);
		else while (widget.getComponentCount() > numFeatures)
				 widgetAdapter.remove(widget, widget.getComponentCount() - 1,
						 (ObjectAdapter) dynamicAdapterVector.elementAt(widget.getComponentCount() - 1));
		 
		/*
		for (int i = numFeatures; i < numElements; i++)
		widget.remove(numFeatures);
		*/
		
	}	public  boolean reparentChild() {		return false;	}	public boolean hasNoProperties() {		
		return false;
		
	}	/*
	public  uiObjectAdapter createAdapter (Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {
	    return createHashtableAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode);
	}
	*/
	/*	public static uiHashtableAdapter createHashtableAdapter (ConcreteType concreteObject,															 Container containW,
															 Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,											  boolean textMode) {
	 
	  //if (!Hashtable.class.isAssignableFrom(inputClass) && !uiBean.isHashtable(inputClass)) return null;	  uiHashtableAdapter hashtableAdapter = new uiHashtableAdapter();
      uiHashtableWidget  hashtableWidget  = new uiHashtableWidget();
     
      hashtableAdapter.setPropertyClass(inputClass);
	  
      hashtableAdapter.setPropertyName(name);
      hashtableAdapter.setViewObject(obj);
     
      if (propertyFlag) {
		hashtableAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
		linkPropertyToAdapter(parentObject, name, hashtableAdapter);
      }
      
      hashtableAdapter.setParentAdapter((uiContainerAdapter)adaptor);	  hashtableAdapter.setUIFrame(adaptor.getUIFrame());
      
      hashtableAdapter.processAttributeList();
      hashtableAdapter.setRealObject(obj1);
	  hashtableAdapter.setConcreteObject(concreteObject);	  //hashtableAdapter.setHashtableStructure(hashtableStructure);

      
      return hashtableAdapter;
        }	*/	public ObjectAdapter getExpandedAdapter(ObjectAdapter child) {		if (child.getAdapterType() != KEY_TYPE) 
				return super.getExpandedAdapter(child);		ObjectAdapter valueAdapter = getValueAdapter(child);
		//return getExpandedObject(getValueAdapter(child));		return valueAdapter;
		/*		Object key = child.getObject();		return getElementAdapterMapping(key);		*/
  }		public ObjectAdapter getValueAdapter(ObjectAdapter keyAdapter) {
		Object key = keyAdapter.getObject();		return getElementAdapterMapping(key);	}
	/*
	public String columnTitle(uiObjectAdapter a) {
		if (!a.isLabelled() ||  a instanceof uiContainerAdapter) return "";
		int childIndex = a.getVectorIndex();
		if (childIndex < 0) return a.getLabelWithoutSuffix();
		//if (childIndex < 0) return a.getLabel();
		return "" + (char) ('A' + childIndex);
		//return "" + (char) ('A' + a.getIndex());
	}
	*/
	public String getColumnTitle(ObjectAdapter a) {
		if (a instanceof PrimitiveAdapter && getHashtableChildren() == AttributeNames.KEYS_AND_VALUES)
			return " "; 
		
		if (!a.isLabelled() ||  a instanceof CompositeAdapter || getHashtableChildren().equals(AttributeNames.KEYS_ONLY)) return "";
		return super.getColumnTitle(a);
		// this was screwing things up
		/*
		if (a.getKeyAdapter() != null && !hasCompositeKey() && !hasCompositeElement()) 
			return "";
			//return a.getKeyAdapter().toString();
		//if (this.isLabelled())
		if (a.isLabelled())
		    return (String) a.getTempAttributeValue(AttributeNames.LABEL);
		else return "";
		*/
	}
	/*
	public String columnTitleJTable(uiObjectAdapter a) {
		
		//if (a.getKeyAdapter() != null && !hasCompositeKey() && !hasCompositeElement()) 
			//return "";
			//return a.getKeyAdapter().toString();
			//
		//if (this.isLabelled())
		if (a.isLabelled())
		    //return (String) a.getTempAttributeValue(AttributeNames.LABEL);
		 	//return (String) a.getLabel();
			return (String) a.getLabelWithoutSuffix();
		else return "";
	}
	*/
	public boolean isSLModelAdapter() {
		return getRealObject().getClass() == slm.SLModel.class;
	}
	
	public static void maybeAddHashtableListener(Object listenable,
			HashtableListener listener) {
		if (listenable == null)
			return;

		MethodProxy addHashtableListenerMethod = IntrospectUtility
				//.getAddVectorListenerMethod(listenable.getClass());
				.getAddHashtableListenerMethod(RemoteSelector.getClass(listenable));
		if (addHashtableListenerMethod != null) {
			if (listenable instanceof ACompositeLoggable) {
				
				return;
			}
			try {
				Object[] args = { listener };
				addHashtableListenerMethod.invoke(listenable, args);
				ObjectEditor.associateKeywordWithClassName(ObjectEditor.HASHTABLE_LISTENER_KEYWORD, 
						RemoteSelector.getClass(listenable));
				//listener.setPropertyChangeListenable (listenable);
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out
						.println("E** Could not invoke addHashatbleListener on"
								+ listenable);
				e.printStackTrace();
			}
		}
	}
	public void registerAsListener (Object obj ) {
		super.registerAsListener(obj);
		maybeAddHashtableListener(obj, this);
		
	}
	Hashtable receivedTable = new Hashtable();
	Vector receivedKeys = new Vector();
	public void keyPut(Object source, Object key, Object value, int newSize) {
		receivedTable.put(key, value);
		receivedKeys.add(key);
		this.refresh();
		
	}
	public void keyRemoved(Object source, Object key, int newSize) {
		receivedTable.remove(key);
		receivedKeys.remove(key);
		this.refresh();
		
	}
	public boolean preClear() {
		if (!getHashtableStructure().hasClearMethod())
			return false;
		return getHashtableStructure().hasDeleteChildMethod();
	}
	public  void clear() {
		getHashtableStructure().clear();
	}
	@Override
	public   boolean redoVisibleChildren() {
		return super.redoVisibleChildren();
		//refreshIndexed(getRealObject());
	}
	public  Object getComponentValue(String componentName) {
		Object retVal = super.get(componentName);
		if (retVal == null) {
			try {
				ObjectAdapter keyAdapter = getKeyAdapterMapping(componentName);
				ObjectAdapter valueAdapter = keyAdapter.getValueAdapter();
				return valueAdapter.getRealObject();
				
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	 Object instantiateCanonicalForm() {
			return new ACanonicalMapBean(this);
	 }
		
		public Object toCanonicalForm() {
			
			if (canonicalBean == null) {
				super.toCanonicalForm(); // create the map and will with static properties
				for (Object key:keyAdapterMapping.keySet()) {
					canonicalBean.put(key, keyAdapterMapping.get(key).getValueAdapter().toCanonicalForm());

				}
				
			}
			return canonicalBean;	
		}
	Map<Object, Object> mapDelegate() {
		if (mapDelegate == null) {
			mapDelegate = super.mapDelegate();				
//			mapDelegate = new HashMap();
			for (Object key:keyAdapterMapping.keySet()) {
//				mapDelegate.put(key, keyAdapterMapping.get(key).getValueAdapter().toCanonicalForm());
				mapDelegate.put(key, objectOrAdapter (keyAdapterMapping.get(key).getValueAdapter()));

			}
		}
		return mapDelegate;
	}
	
	public Object put(Object key, Object value) {
		Object keyAdapter = keyAdapterMapping.get(key);
		if (keyAdapter != null) {
			// hashtable key
			getHashtableStructure().put(key, value);
			mapDelegate = null;
			return mapDelegate.get(key);
//			mapDelegate.put(key, value);
		} else 
		return super.put(key, value);

	}
	

		
//	 public Vector getVisibleAndInvisibleDynamicChildAdapters () {
//		 Vector retVal = super.getVisibleAndInvisibleDynamicChildAdapters();
//		 Enumeration dynamicChildren =  getDynamicChildAdapters();
//		 while (dynamicChildren.hasMoreElements()) {
//			 retVal.add(dynamicChildren.nextElement());
//		 }
//		 return retVal;
//		
//		 
////		 return new Vector (visibleAndNonVisibleChildrenVector);
//		 
//	  }
}
