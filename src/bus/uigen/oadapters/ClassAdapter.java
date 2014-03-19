package bus.uigen.oadapters;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import shapes.RemoteShape;
import util.models.AListPair;
import util.trace.Tracer;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.WidgetAdapterInterface;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.adapters.CommandAndStatePanelAdapter;
import bus.uigen.adapters.TabbedPaneAdapter;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.diff.ACanonicalBean;
import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.misc.OEMisc;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.trace.ClassAdapterReceivedPropertyChangeEvent;
import bus.uigen.trace.IllegalSourceOfPropertyNotification;
import bus.uigen.trace.UnknownPropertyNotification;
import bus.uigen.visitors.ClearVisitedNodeAdapterVisitor;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;

public class ClassAdapter extends CompositeAdapter implements ClassAdapterInterface {
//		ReplaceableChildren {

	transient Hashtable<String, ObjectAdapter> mapping;
	transient Map mapDelegate;
	transient Map canonicalBean;
	transient int numFeatures = 0;
	transient boolean propertiesCreated = false;
	// this contains invisible and visible children. static properties cannot be
	// really deleted
	Hashtable<String, ObjectAdapter> deletedChildren = new Hashtable();
	/*
	 * boolean sorted = false; public boolean getSorted() { return sorted; }
	 * public void setSorted(boolean newVal) { sorted = newVal; }
	 */
	// this mode is for sorting the elements using text sort
	boolean sortMode = false;

	public void setSortMode(boolean newVal) {
		sortMode = newVal;
	}

	String toString(String sortProperty) {
		Object sortVal = getRecordStructure().get(sortProperty);
		return toString(sortVal);

	}

	String toString(Object sortVal) {
		if (sortVal != null)
			return sortVal.toString();
		else
			return super.toString();

	}

	String toString(Vector relativePath) {
		Vector path = getVectorPath();
		path.addAll(relativePath);
		ObjectAdapter sortedDescendent = pathToObjectAdapter(path);
		if (sortedDescendent == null) {
			Tracer.error("Null sorted descendent");
		}
		Object sortVal = sortedDescendent.getRealObject();
		return toString(sortVal);

	}

	public String toString() {
		if (!getSortMode())
			return super.toString();
		Object sortProperty = getSortProperty();
		if (sortProperty instanceof String)
			return toString((String) sortProperty);
		else if (sortProperty instanceof Vector)
			return toString((Vector) sortProperty);
		else
			return super.toString();
		/*
		 * String sortProperty = (String) getSortProperty(); Object sortVal =
		 * getRecordStructure().get(sortProperty); if (sortVal != null) return
		 * sortVal.toString(); else return super.toString();
		 */
	}
	
	

	public boolean getSortMode() {
		return sortMode;
	}

	public int getNumberOfStaticChildren() {

		return numFeatures;
	}

	public int getNumberOfDynamicChildren() {
		return 0;
	}

	public ClassAdapter() throws RemoteException {
		mapping = new Hashtable();
	}

	public Enumeration getChildren() {
		return mapping.elements();
	}

	public int getNumberOfChildren() {
		return mapping.size();
	}

	public boolean isNoPattern() {
		return getNumberOfChildren() == 0;
	}

	public void padChildrenLabels(int maxLength) {
		for (Enumeration elements = getChildren(); elements.hasMoreElements();)
			((ObjectAdapter) elements.nextElement()).padLabelTo(maxLength);
	}

	public RecordStructure getRecordStructure() {
		// return vectorStructure;
		return (RecordStructure) getConcreteObject();
	}

	public boolean isReadOnly(ObjectAdapter child) {
		try {
			if (isRecursive())
				return false;
			if (child == null)
				return false;
			RecordStructure record = getRecordStructure();
			if (record == null)
				return false;
			String propertyName = child.getPropertyName();
			return record.isReadOnly(propertyName)
					|| !record.preWrite(propertyName);
		} catch (Exception e) {
			// System.out.println("isReadOnly " + e);
			e.printStackTrace();
			return false;
		}
	}

	public boolean isDynamicReadOnly() {
		if (getRecordStructure() == null)
			return false;
		return !getRecordStructure().isEditable();
	}

	public boolean isValid(ObjectAdapter child, Object newValue) {
		try {
			return getRecordStructure().validate(child.getPropertyName(),
					newValue);
		} catch (Exception e) {
			System.out.println("isReadOnly " + e);
			return false;
		}
	}

	public Object get(ObjectAdapter child) {
		try {
			return getRecordStructure().get(child.getPropertyName());
		} catch (Exception e) {
			System.out.println("get " + e);
			return null;
		}
	}

	public Object set(ObjectAdapter child, Object newVal, boolean initialChange) {

		try {
			if (initialChange && !(child instanceof EnumerationAdapter))
				return getRecordStructure().set(child.getPropertyName(),
						newVal, this);
			else
				return getRecordStructure()
						.set(child.getPropertyName(), newVal);
		} catch (Exception e) {
			System.out.println("set " + e);
			e.printStackTrace();
			return null;
		}
	}

	// public void childUIComponentValueChanged (boolean initialChange) {
	public boolean childUIComponentValueChanged(ObjectAdapter child,
			Object newValue, boolean initialChange) {
		// if (child.getAdapterType() != PROPERTY_TYPE || isReadOnly(child) ||
		// !isValid(child, newValue))
		if (child.getAdapterType() != PROPERTY_TYPE || child.isUnEditable()
				|| !isValid(child, newValue))
			return false;
		/*
		 * Object newValue;
		 * 
		 * if (child instanceof uiContainerAdapter) newValue =
		 * child.getRealObject(); else //Object newValue = getValue(); newValue
		 * = child.getOriginalValue();
		 */
		set(child, newValue, initialChange);
		return true;
		// child.refreshConcreteObject(newValue);
	}

	public Object getChildValue(ObjectAdapter child) {
		if (child.getAdapterType() != PROPERTY_TYPE)
			return null;
		return get(child);
	}

	/*
	 * public void oldRefreshValue(Object newValue, boolean forceUpdate) {
	 * //System.out.println("uiClassAdapter-Set Value: " + newValue); // Use
	 * reflection to get the field/property components // and hand them down to
	 * their corresponding widget/adaptors
	 * 
	 * //System.out.println("set value called on:" + this.getPath() + newValue);
	 * boolean classChanged = false; if (newValue == null) return; boolean
	 * viewDefined = getViewObject() == getRealObject();
	 * 
	 * //Object viewObject = uiGenerator.getViewObject(newValue); Object
	 * viewObject = computeViewObject(newValue); setViewObject(viewObject);
	 * RecordStructure recordStructure = getRecordStructure();
	 * this.refreshConcreteObject(viewObject);
	 * //recordStructure.setTarget(viewObject); //Vector componentNames =
	 * recordStructure.componentNames(); if (getRealObject() == null) { //
	 * Temporary change System.out.println("TEMPORARY!"+newValue);
	 * setRealObject(newValue);
	 * 
	 * this.addClassComponents(); //((Container)
	 * getWidgetAdapter().getUIComponent()).invalidate();
	 * getWidgetAdapter().invalidate(); return; } // Set the real Object anyway
	 * setRealObject(newValue); if (!childrenCreated) return; // JTree //if
	 * (isAtomic()) return; ClassProxy myClass = getPropertyClass(); if (myClass
	 * == null) {
	 * //System.out.println("uiClassAdapter: propertyClass is null for" + this);
	 * return; // Shouldnt happen. } Vector componentNames =
	 * recordStructure.componentNames(); RightMenuManager.getRightMenu(myClass,
	 * this);
	 * 
	 * 
	 * // Now for the properties. //ViewInfo cdesc =
	 * ClassDescriptorCache.getClassDescriptor(myClass);
	 * 
	 * 
	 * //PropertyDescriptor properties[] = cdesc.getPropertyDescriptors(); for
	 * (int i = 0; i < componentNames.size(); i++) { String componentName =
	 * (String) componentNames.elementAt(i);
	 * 
	 * Object value = recordStructure.get(componentName); //if (readMethod !=
	 * null && !property.getName().equals("class")) { if (value != null) {
	 * 
	 * 
	 * //System.out.println("Read "+property.getName()+" = "+value);
	 * //uiObjectAdapter adapter = getChildAdapterMapping(property.getName());
	 * uiObjectAdapter adapter = getChildAdapterMapping(componentName); if
	 * (forceUpdate) adapter.setEdited(false); if (adapter != null) { Object
	 * oldValue = adapter.getObject(); if(ObjectEditor.shareBeans()){
	 * if(ObjectEditor.coupleElides()){
	 * ObjectRegistry.replaceObject(oldValue,value); } else{
	 * ObjectRegistry.mapObjectToAdapter(value,adapter); } } if ((oldValue ==
	 * null && value != null )|| (oldValue != null && value == null) ||
	 * (oldValue != null && newValue != null &&
	 * RemoteSelector.getClass(oldValue) != RemoteSelector.getClass(value)) ){
	 * //boolean isElided = adapter.getGenericWidget().isElided(); boolean
	 * isElided = adapter.isElided(); //boolean isElided = true; classChanged =
	 * true; //value.getClass(); //System.out.println("Class changed from:" +
	 * oldValue.getClass() + value.getClass());
	 * 
	 * //System.out.println ("about to replaced adapter"); uiObjectAdapter
	 * newAdapter = this.replaceAdapter(adapter, value);
	 * 
	 * //System.out.println ("replaced adapter");
	 * //this.setChildAdapterMapping(property.getName(), newAdapter);
	 * this.setChildAdapterMapping(componentName, newAdapter); //maxLabelLength
	 * = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());
	 * 
	 * newAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE); // Set the
	 * propertyName //newAdapter.setPropertyName(property.getName());
	 * newAdapter.setPropertyName(componentName);
	 * //newAdapter.setPropertyReadMethod(property.getReadMethod());
	 * //newAdapter.setPropertyWriteMethod(property.getWriteMethod());
	 * 
	 * 
	 * if (recordStructure.isReadOnly(componentName)) {
	 * 
	 * //if (property.getWriteMethod() == null) {
	 * 
	 * //if (newAdapter instanceof uiPrimitiveAdapter) { if
	 * (newAdapter.isLeafAdapter()){ //((uiPrimitiveAdapter)
	 * newAdapter).getWidgetAdapter().setUIComponentUneditable();
	 * //newAdapter.getWidgetAdapter().setUIComponentUneditable();
	 * newAdapter.setUneditable(); } //till here }
	 * //System.out.println("internal elide start"); if (isElided)
	 * newAdapter.internalElide(); else newAdapter.internalExpand();
	 * 
	 * //System.out.println("internal elide end");
	 * 
	 * } else if (! (value == null && oldValue == null)) {
	 * adapter.refreshValue(value);
	 * //adapter.getConcreteObject().setTarget(adapter.getViewObject());
	 * adapter.refreshConcreteObject(adapter.getViewObject()); } } else {
	 * 
	 * }
	 * 
	 * } }
	 * 
	 * // System.out.println("set value make column titles ended"); if
	 * (classChanged && this.getDirection().equals("horizontal") && this.parent
	 * instanceof uiVectorAdapter) ((uiContainerAdapter)
	 * this.parent).makeColumnTitles();
	 * 
	 * if (isAtomic()) setValueOfAtomicOrPrimitive(newValue);
	 * //System.out.println("set value ended"); }
	 */

	public void refreshValue(Object newValue, boolean forceUpdate) {
		Object oldValue = computeAndMaybeSetViewObject();
		/*
		 * refreshSelf(newValue, forceUpdate); boolean classChanged =
		 * refreshChildren(newValue, forceUpdate); //
		 * System.out.println("set value make column titles ended"); if
		 * (classChanged && this.getDirection().equals("horizontal") &&
		 * this.parent instanceof uiVectorAdapter) ((uiContainerAdapter)
		 * this.parent).makeColumnTitles();
		 */

		refreshValueButNotAtomic(newValue, forceUpdate);
		if (isAtomic())
			setValueOfAtomicOrPrimitive(newValue);
		if (isAttributeChangePending() /* && forceUpdate*/ ) {
			refreshAttributes();
			attributeChangePending = false;
		}
		// System.out.println("set value ended");
	}

	public void refreshValueButNotAtomic(Object newValue, boolean forceUpdate) {
		refreshSelf(newValue, forceUpdate);
		if (newValue == null)
			return;
		boolean classChanged = refreshChildren(newValue, forceUpdate);
		// System.out.println("set value make column titles ended");
		if (classChanged && this.getDirection().equals("horizontal")
				&& this.parent instanceof VectorAdapter)
			((CompositeAdapter) this.parent).makeColumnTitles();

	}

	public void refreshSelf(Object newValue, boolean forceUpdate) {
		// System.out.println("uiClassAdapter-Set Value: " + newValue);
		// Use reflection to get the field/property components
		// and hand them down to their corresponding widget/adaptors
		/*
		 * if (isAtomic()) { atomicSetValue(newValue); return; }
		 */
		// System.out.println("set value called on:" + this.getPath() +
		// newValue);
		// boolean classChanged = false;
		if (newValue == null)
			return;

		// Object viewObject = uiGenerator.getViewObject(newValue);
		// Object viewObject = getViewObject(newValue);
		setRealObject(newValue);
		// computeAndSetViewObject();
		Object viewObject = computeAndMaybeSetViewObject();
		// boolean viewDefined = computeAndMaybeSetViewObject() !=
		// getRealObject();
		boolean viewDefined = viewObject != getRealObject();
		if (viewDefined)
			askViewObjectToRefresh();
		// setViewObject(viewObject);
		/* RecordStructure */recordStructure = getRecordStructure();
		this.refreshConcreteObject(viewObject);
		// recordStructure.setTarget(viewObject);
		// Vector componentNames = recordStructure.componentNames();
		if (getRealObject() == null) {
			// Temporary change
			System.out.println("TEMPORARY!" + newValue);
			setRealObject(newValue);
			/*
			 * uiGenerator.uiAddClassComponents((Container)
			 * getWidgetAdapter().getUIComponent(), this, newValue);
			 */
			this.addClassComponents(false);
			// ((Container) getWidgetAdapter().getUIComponent()).invalidate();
			getWidgetAdapter().invalidate();
			return;
		}
		// Set the real Object anyway
		// setRealObject(newValue); // moving this up
	}

	/*
	 * public void refreshEditable(uiObjectAdapter a) { boolean oldVal =
	 * a.isUnEditable(); boolean newVal = a.isReadOnly(); if (newVal)
	 * a.setUneditable(); else a.setEditable();
	 * 
	 * }
	 */
	public boolean refreshChildren(Object newValue, boolean forceUpdate) {
		boolean classChanged = false;
		if (!childrenCreated)
			return classChanged;
		// JTree
		// if (isAtomic()) return;
		ClassProxy myClass = getPropertyClass();
		if (myClass == null) {
			// System.out.println("uiClassAdapter: propertyClass is null for" +
			// this);
			return classChanged; // Shouldnt happen.
		}
		if (recordStructure == null) {
			Tracer.error("Null record structure. Report this error if do not receive the multiple visit error.");
			return false;
		}
		Vector componentNames = recordStructure.componentNames();
		RightMenuManager.getRightMenu(myClass, this, newValue);

		// Now for the properties.
		// ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(myClass);

		// PropertyDescriptor properties[] = cdesc.getPropertyDescriptors();
		refreshStaticComponents();
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			if (refreshChild(newValue, componentName, forceUpdate))
				classChanged = true;

		}
		return classChanged;
	}

	public boolean refreshChild(Object newParentValue, String componentName,
			boolean forceUpdate) {

		/*
		 * CONTINUE HERE
		 */
		ObjectAdapter childAdapter = getVisibleOrDeletedObjectAdapter(componentName);
		boolean childHasPendingValue = childAdapter.hasPendingValue();
		Object value;
		if (childHasPendingValue)
			value = childAdapter.getPendingFutureRealObject();
		else value = getRecordStructure().get(componentName);

//		Object value = getRecordStructure().get(componentName);
		boolean retVal = refresh(value, componentName, forceUpdate);
		if (childHasPendingValue)
			childAdapter.pendingValueProcessed();
//		return refresh(value, componentName, forceUpdate);
		return retVal;

	}

	public boolean refresh(Object newComponentValue, String componentName,
			boolean forceUpdate) {
		boolean classChanged = false;
		boolean prePropagated = false;

		/*
		 * for (int i=0; i< properties.length; i++) { PropertyDescriptor
		 * property = properties[i]; // Get the value of the property. //not
		 * sure why the write method was an issue - for some reason it was
		 * giving problems //if (property.getWriteMethod() == null) continue;
		 * Method readMethod = property.getReadMethod();
		 */
		try {
			// System.out.println("recordStructure of " + this + "is" +
			// recordStructure);
			// Object value = recordStructure.get(componentName);

			// Object value = getRecordStructure().get(componentName);
			Object value = newComponentValue;
			// Object value = newValue;
			// if (readMethod != null && !property.getName().equals("class")) {
			// if (value != null) {

			/*
			 * try { Object value = null; if(!ObjectEditor.shareBeans()){ value
			 * = readMethod.invoke(viewObject, null); } else{
			 * 
			 * value = ObjectRegistry.logReadMethodInvocation(new
			 * UnivMethodInvocation(viewObject,readMethod,null)); }
			 */

			// System.out.println("Read "+property.getName()+" = "+value);
			// uiObjectAdapter adapter =
			// getChildAdapterMapping(property.getName());
			// ObjectAdapter adapter =
			// getStaticChildAdapterMapping(componentName);
			ObjectAdapter adapter = getVisibleOrDeletedObjectAdapter(componentName);

			if (adapter == null)
				return false;
			 componentName = adapter.getPropertyName(); // normalize the name
			if (forceUpdate)
				adapter.setEdited(false);
			// if (adapter != null) {
			// Object oldValue = adapter.getObject();
			// getOriginalValue?
			// oldValue = adapter.getObject();
			// Object oldValue = adapter.getOriginalValue();
			Object oldValue = adapter.getPreviousRealObject();
			if (ObjectEditor.shareBeans()) {
				if (ObjectEditor.coupleElides()) {
					ObjectRegistry.replaceObject(oldValue, value);
				} else {
					ObjectRegistry.mapObjectToAdapter(value, adapter);
				}
			}
			if ( (!getManualUI()
					&& (oldValue == null && value != null
					// && ACompositeLoggable.getTarget(value) !=
					// adapter.getPropertyClass()
							|| (oldValue != null && value == null) ||
					// (oldValue != null && value != null && oldValue.getClass()
					// != value.getClass())
					// (oldValue != null && value != null &&
					// !Misc.equals(ACompositeLoggable.getTarget(oldValue),
					// ACompositeLoggable.getTarget(value)))
					(oldValue != null && value != null && !OEMisc.equals(
							ACompositeLoggable.getTargetClass(oldValue),
							ACompositeLoggable.getTargetClass(value)))) && (adapter
					.getUIComponent() != null
					|| adapter instanceof ShapeObjectAdapter
					|| adapter.isTreeNode() || getDeletedAdapter(componentName) != null))) {
				// boolean isElided = adapter.getGenericWidget().isElided();
				boolean isElided = adapter.isWidgetShellElided();
				// boolean isElided = true;
				classChanged = true;
				// System.out.println("Class changed from:" +
				// oldValue.getClass() + value.getClass());

				// System.out.println ("about to replaced adapter");
				// if (adapter.getUIComponent() != null)
				ObjectAdapter newAdapter = this.replaceAdapter(adapter, value);
				if (newAdapter == null)
					return false; // System.out.println ("replaced adapter");
				// this.setChildAdapterMapping(property.getName(), newAdapter);
				replaceChildInTables(componentName, adapter, newAdapter);
				// this.setChildAdapterMapping(componentName, newAdapter);
				// maxLabelLength = Math.max(maxLabelLength,
				// a.getGenericWidget().getLabel().length());

				newAdapter.setAdapterType(ObjectAdapter.PROPERTY_TYPE);
				
				// Set the propertyName
				// newAdapter.setPropertyName(property.getName());
				// not sure if we need this as the name has been set already. In any case it should be name of child
				// not string sent by yser
//				newAdapter.setPropertyName(componentName);
				
				
				
				// newAdapter.setPropertyReadMethod(property.getReadMethod());
				// newAdapter.setPropertyWriteMethod(property.getWriteMethod());
				/*
				 * if (!(newAdapter instanceof uiPrimitiveAdapter)) { ViewInfo
				 * childDesc =
				 * ClassDescriptorCache.getClassDescriptor(value.getClass());
				 * 
				 * if (childDesc.getBeanDescriptor() != null &&
				 * !newAdapter.getGenericWidget().isLabelVisible() &&
				 * //!"horizontal"
				 * .equals(childDesc.getBeanDescriptor().getValue("direction")))
				 * !"horizontal".equals(newAdapter.getMergedAttributeValue(
				 * "direction"))) foundUnlabeledComposite = true;
				 * //System.out.println ("found composite"); }
				 */

				if (recordStructure.isReadOnly(componentName)) {

					// if (property.getWriteMethod() == null) {

					// if (newAdapter instanceof uiPrimitiveAdapter) {
					if (newAdapter.isLeafAdapter()) {
						// ((uiPrimitiveAdapter)
						// newAdapter).getWidgetAdapter().setUIComponentUneditable();
						// newAdapter.getWidgetAdapter().setUIComponentUneditable();
						newAdapter.setUneditable();
					}
					// till here
				}
				// System.out.println("internal elide start");
				// if we transformed from a null value to a non null value then
				// expand it
				if (isElided && newAdapter.getRealObject() != null
						&& adapter.getRealObject() != null)
					newAdapter.internalElide();
				else
					// newAdapter.internalExpand();
					uiGenerator.deepElide(newAdapter);
				/*
				 * if (isElided) newAdapter.getGenericWidget().internalElide();
				 * else newAdapter.getGenericWidget().internalExpand();
				 */
				// System.out.println("internal elide end");

			}
			// else if ( ! (value == null && oldValue == null) &&
			// !Misc.equals(value, oldValue)) {
			// else if ( ! (value == null && oldValue == null) &&
			else if (forceUpdate
					|| adapter.getRetargetedButNotRefreshed()
					||
					// the cmmented line does not make sense
					// ! (value == null && oldValue == null) && ((value ==
					// oldValue) || // this means oldValue is not a clone so
					// assume they are different
					(!(value == null && oldValue == null) && (value == oldValue))
					|| // this means oldValue is not a clone so assume they are
						// different

					!OEMisc.equals(LoggableRegistry.getRealObject(value),
							LoggableRegistry.getRealObject(oldValue))) {

				// moving this below
				// adapter.refreshValue(value);
				// adapter.getConcreteObject().setTarget(adapter.getViewObject());
				/*
				 * if (value != oldValue) adapter.setRealObject(value); // not
				 * sure why this was not done before!
				 */
				// adapter.refreshConcreteObject(adapter.getViewObject());
				// this was commented out earlier
				adapter.setRealObject(value);
				adapter.refreshConcreteObject();
//				if (value != oldValue && adapter instanceof CompositeAdapter) {
//					adapter.refreshValue(value, true); // this may fix problem with old structure being used
//				} else
				prePropagated = true;
				adapter.refreshValue(value, forceUpdate);
				adapter.setRetargetedButNotRefreshed(false);
			}
			/*
			 * }
			 * 
			 * else {
			 * 
			 * }
			 */

			/*
			 * } catch (IllegalStateException ise) { //ise.printStackTrace(); }
			 * catch (InvocationTargetException ite) { //e.printStackTrace();
			 * //System.out.println(e); Throwable actualException =
			 * ite.getTargetException();
			 * //System.out.println("Couldnt set value of property "
			 * +property.getName() + ite); String s =
			 * "Error setting property value\n"; //s = s + e + "\n" + "Object: "
			 * + obj + "\nProperty: " + property.getName() + "\nMethod: " +
			 * property.getReadMethod().getName(); s = s + "\n" + "\nProperty: "
			 * + property.getName(); if (property.getWriteMethod() != null) s= s
			 * + "\nMethod: " + property.getWriteMethod().getName(); s = s +
			 * "\nException: " + actualException.getMessage();
			 * actualException.printStackTrace(); s = s +
			 * "\nPlease trace method"; JOptionPane.showMessageDialog(null,s );
			 * } catch (Exception e) { //e.printStackTrace();
			 * //System.out.println(e);
			 * System.out.println("Couldnt set value of property "
			 * +property.getName() + e); String s =
			 * "Error setting property value\n"; //s = s + e + "\n" + "Object: "
			 * + obj + "\nProperty: " + property.getName() + "\nMethod: " +
			 * property.getReadMethod().getName(); s = s + "\n" + "\nProperty: "
			 * + property.getName(); if (property.getWriteMethod() != null) s= s
			 * + "\nMethod: " + property.getWriteMethod().getName(); s = s +
			 * "\nPlease trace method"; e.printStackTrace();
			 * JOptionPane.showMessageDialog(null,s ); }
			 */
			// }
			if (!classChanged)
				adapter.refreshEditable();
			if (adapter.getAddMeBack()) {
				getWidgetAdapter().childComponentsAdded(true);
				adapter.propagateAttributesToWidgetShell();
			}
			if (!prePropagated)
				adapter.propagatePreConditions();
			return classChanged;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return false;
		}

	}

	public Object getValue() {

		// Use reflection to get the field/property components
		// and hand them down to their corresponding widget/adaptors
		ClassProxy myClass = getPropertyClass();
		if (myClass == null) {
			// System.out.println("uiClassAdapter: propertyClass is null for" +
			// this + "adapter");
			return null; // Shouldnt happen.
		}
		return getRealObject();
	}

	public void uiComponentValueChanged(Object source) {
		if (source instanceof WidgetAdapterInterface) {
			// This event came from a widget. we need to update
			// our value
			// System.out.println("Value is"+
			// getWidgetAdapter().getUIComponentValue());
			// TEMP FIX
			setRealObject(getWidgetAdapter().getUIComponentValue());
			// setValue(getWidgetAdapter().getUIComponentValue());
		}
		// Do the usual stuff
		uiComponentValueChanged();
	}

	public void setChildAdapterMapping(String fieldName, ObjectAdapter adaptor) {
		// System.out.println("putting in mapping" + fieldName + "adapter" +
		// adaptor);
		if (adaptor == null)
			return;

		Object retVal = mapping.put(fieldName.toLowerCase(), adaptor);
		// System.out.println("put in mapping: retVal" + retVal);
		// System.out.println("size" + mapping.size());

		// if (mapping.put(fieldName, adaptor) != null);
		// System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
	}

	public void addChildInTables(String fieldName, ObjectAdapter adapter) {
		setChildAdapterMapping(fieldName, adapter);
		setChildAdapterMapping(adapter);
		adapter.setAdapterType(PROPERTY_TYPE);

	}

	public void removeChildInTables(ObjectAdapter adaptor) {
		removeChildInTables(adaptor.getPropertyName().toLowerCase(), adaptor);

	}

	public void removeChildInTables(String fieldName, ObjectAdapter adaptor) {
		// System.out.println("putting in mapping" + fieldName + "adapter" +
		// adaptor);
		// Object retVal = mapping.put(fieldName, adaptor);
		Object retVal = mapping.remove(fieldName);
		deletedChildren.put(fieldName, adaptor);
		unSetChildAdapterMapping(adaptor);
		mapping.remove(fieldName);

		// System.out.println("put in mapping: retVal" + retVal);
		// System.out.println("size" + mapping.size());

		// if (mapping.put(fieldName, adaptor) != null);
		// System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
	}

	public void removeChildrenInTables() {
		removeStaticChildrenInTables();
		/*
		 * Enumeration<String> keys = mapping.keys(); while
		 * (keys.hasMoreElements()) { String componentName = keys.nextElement();
		 * uiObjectAdapter child = mapping.get(componentName);
		 * deletedChildren.put(componentName, child); } mapping.clear();
		 * childrenVector.clear(); visibleAndNonVisibleChildrenVector.clear();
		 * childComponents.clear(); leafComponents.clear(); childComponentsSet =
		 * false; childrenCreated = false; //resetChildrenVector();
		 */

	}

	public void removeStaticChildrenInTables() {
		Enumeration<String> keys = mapping.keys();
		while (keys.hasMoreElements()) {
			String componentName = keys.nextElement();
			ObjectAdapter child = mapping.get(componentName);
			if (child == null) {
				Tracer.error("Null child, shoulld not happen");
				continue;
			}
			deletedChildren.put(componentName, child);
			/*
			 * mapping.remove(child);
			 * visibleAndNonVisibleChildrenVector.remove(child); chidrenVector.r
			 */
		}
		mapping.clear();
		childrenVector.clear();
		visibleAndNonVisibleChildrenVector.clear();
		childComponents.clear();
		leafComponents.clear();
		childComponentsSet = false;
		childrenCreated = false;
		// children will be recreated so remove them from the visited list
		(new ClearVisitedNodeAdapterVisitor(this)).traverseNonAtomicChildrenContainers();

		// resetChildrenVector();

	}

	public void replaceChildInTables(String fieldName,
			ObjectAdapter oldAdapter, ObjectAdapter newAdapter) {
		// System.out.println("putting in mapping" + fieldName + "adapter" +
		// adaptor);
		// Object retVal = mapping.put(fieldName, adaptor);
		// Object retVal = mapping.put(fieldName, newAdapter);
		setChildAdapterMapping(fieldName, newAdapter);
		resetChildAdapterMapping(oldAdapter, newAdapter);
		deletedChildren.put(fieldName, oldAdapter);
//		if (!(oldAdapter instanceof PrimitiveAdapter)) {
//			
//		getRootAdapter().getBasicObjectRegistery().remove(oldAdapter.getRealObject());
//		getRootAdapter().getVisitedObjects().remove(oldAdapter.getRealObject());
//		}
		// System.out.println("put in mapping: retVal" + retVal);
		// System.out.println("size" + mapping.size());

		// if (mapping.put(fieldName, adaptor) != null);
		// System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
	}

	public ObjectAdapter getStaticChildAdapterMapping(String fieldName) {
		return (ObjectAdapter) mapping.get(fieldName.toLowerCase());
	}

	public ObjectAdapter getDeletedAdapter(String fieldName) {
		return (ObjectAdapter) deletedChildren.get(fieldName.toLowerCase());
	}

	public ObjectAdapter getVisibleOrDeletedObjectAdapter(String fieldName) {
		ObjectAdapter retVal = getStaticChildAdapterMapping(fieldName);
		if (retVal == null) {
			return getDeletedAdapter(fieldName);
			// if (! retVal.isVisible()) return retVal;
			// else return null;

		} else
			return retVal;

	}

	public ObjectAdapter getChildAdapterMapping(String fieldName) {
		return getStaticChildAdapterMapping(fieldName);
		// return (uiObjectAdapter) mapping.get(fieldName);
	}

	public ObjectAdapter get(String fieldName) {
		return getChildAdapterMapping(fieldName);
		// return (uiObjectAdapter) mapping.get(fieldName);
	}

	public Vector componentNames() {
		return getRecordStructure().componentNames();
	}

	public ObjectAdapter getExistingOrDeletedAdapter(String fieldName) {
		ObjectAdapter child = getStaticChildAdapterMapping(fieldName);
		if (child == null)
			child = deletedChildren.get(fieldName);
		return child;
		// return (uiObjectAdapter) mapping.get(fieldName);
	}

	public String getChildAdapterRealIndex(ObjectAdapter adaptor) {
		// Search the mapping table for the adaptor
		// and return the key.
		/*
		 * Enumeration keys = mapping.keys(); while (keys.hasMoreElements()) {
		 * String key = (String) keys.nextElement(); if
		 * (mapping.get(key).equals(adaptor)) return key; }
		 */
		String retVal = adaptor.getPropertyName();
		if (retVal != null)
			return retVal;
		System.out.println("Didnt find requested field in uiClassAdapter!!");
		return null;
	}

	public ObjectAdapter getChildAdapterAtIndex(String key) {
		return getStaticChildAdapterAtIndex(key);
		// return (uiObjectAdapter) mapping.get(key.toLowerCase());
	}

	public ObjectAdapter getStaticChildAdapterAtIndex(String key) {
		return (ObjectAdapter) mapping.get(key.toLowerCase());
	}

	public ObjectAdapter getVisibleOrInvisibleChildAdapterAtRealIndex(String key) {
		ObjectAdapter retVal = getStaticChildAdapterAtIndex(key);
		if (retVal != null)
			return retVal;
		return deletedChildren.get(key);
	}
	public Vector getVisibleAndInvisibleDynamicChildAdapters() {
		return getVisibleAndInvisibleChildAdapters ();
	}
	public Vector getVisibleAndInvisibleStaticChildAdapters () {
		Vector retVal = new Vector(mapping.values());
		 Set<String> mappingKeys = mapping.keySet();
		 Set<String> deletedKeys = deletedChildren.keySet();
		 for (String deletedKey:deletedKeys) {
			 if (!mappingKeys.contains(deletedKey)) {
				 retVal.add(deletedChildren.get(deletedKey));
			 }
		 }
		 return retVal;
		
	}
	 public Vector getVisibleAndInvisibleChildAdapters () {
//		 return new Vector (visibleAndNonVisibleChildrenVector);
//		 Vector retVal = new Vector(mapping.values());
//		 Set<String> mappingKeys = mapping.keySet();
//		 Set<String> deletedKeys = deletedChildren.keySet();
//		 for (String deletedKey:deletedKeys) {
//			 if (!mappingKeys.contains(deletedKey)) {
//				 retVal.add(deletedChildren.get(deletedKey));
//			 }
//		 }
		 
		 Vector retVal = getVisibleAndInvisibleStaticChildAdapters();
		 
		 Enumeration dynamicChildren =  getDynamicChildAdapters();
		 while (dynamicChildren.hasMoreElements()) {
			 retVal.add(dynamicChildren.nextElement());
		 }
//		 if (recordStructure == null)
//			 return retVal;
//		 Vector<String> componentNames = recordStructure.componentNames();
//		 

//		 for (String componentName:componentNames){
//			 if (mapping.get(componentName) == null) {
//				 retVal.add(deletedChildren.get(componentName));
//			 }
//		 }
//		 Collection<ObjectAdapter> deletedAdapters = deletedChildren.values();
//		 for (ObjectAdapter deletedAdapter:deletedAdapters) {
//			 if (!retVal.contains(deletedAdapter))
//				 retVal.add(deletedAdapter);
//			 else {
//				 Tracer.info(this, "Deleted adapter in current adapters:" + deletedAdapter);
//			 }
//		 }
////		 retVal.addAll(deletedChildren.values());
		 return retVal;
	  }

	public void deleteChildAdapter(String fieldName) {
		// Dont do anything
	}

	public Enumeration getChildAdapters() {
		return mapping.elements();
	}

	Vector nullVector = new Vector();

	public Enumeration getDynamicChildAdapters() {
		return nullVector.elements();

	}

	public static boolean reparentChild(ClassAdapter adapter) {
		/*
		 * if (adapter instanceof uiVectorAdapter || adapter instanceof
		 * uiHashtableAdapter) return false; //if (adapter.getNumberOfChildren()
		 * != 1) // some field may later be unhidden if
		 * (adapter.getRecordStructure().componentNames().size() != 1) return
		 * false;
		 */
		// check already done in set sythesized attributes
//		if (!computeHasOnlyChild(adapter))
//			return false;
		if (adapter == null)
			return false;
		VirtualComponent component = adapter.getUIComponent();
//		ObjectAdapter childAdapter = adapter.getChildAdapterAt(0);
		ObjectAdapter childAdapter = adapter.getChildrenVector().elementAt(0);

		
		if (childAdapter == null)
			return false;
		// System.out.println("component" + component);
		if (!(component instanceof VirtualContainer))
			return false;
		VirtualContainer container = (VirtualContainer) component;
		if (container.getComponentCount() != 1)
			return false;
		// why was this commented out, this is what I have gone back to.
		// if (adapter.getChildAdapterCount() != 1) return false;
		// System.out.println("singleton component");
		// this does not work as we dont know if we are getting a gen widget or
		// ui component
		// VirtualComponent child = childAdapter.getUIComponent();
		VirtualComponent child = container.getComponent(0);
		if (child == null)
			return false;
		// Container genWidget = adapter.getGenericWidget();
		VirtualContainer genWidget = null;
		if (adapter.getGenericWidget() != null)
			// VirtualContainer genWidget =
			// adapter.getGenericWidget().getContainer();
			genWidget = adapter.getGenericWidget().getContainer();
		else
			genWidget = container;
		// System.out.println("gen widet" + genWidget);
		VirtualContainer parentContainer = genWidget.getParent();
		if (parentContainer == null)
			return false;
		// System.out.println("parent Container" + parentContainer);
		// do this only if the child will fill up the whole window
		if (adapter.isTopDisplayedAdapter())
		reparentChild(parentContainer, genWidget, child);
		return true;
	}

	public boolean reparentChild() {
		return reparentChild(this);
	}

	public static int getPosition(VirtualContainer container,
			VirtualComponent component) {
		int position = 0;
		VirtualComponent[] components = container.getComponents();
		for (; position < components.length; position++) {
			// System.out.println ("pos" + position + "comp" +
			// components[position]);
			if (component == components[position])
				break;
		}
		// System.out.println("returning position" + position + "out of" +
		// components.length);
		return position;

	}

	public static void reparentChild(VirtualContainer parent,
			VirtualContainer container, VirtualComponent child) {
		// let us not do something and simply muck around with labels and
		// borders
		
		// wonder why the mucking is bad
		
		
		 int position = getPosition(parent, container);
		 container.remove(child); 
		 parent.remove(position);
		 parent.add(child, position);
		 

		// System.out.println("reparented");
	}

	public ObjectAdapter replaceAdapter(ObjectAdapter child, Object newValue) {
		// Assume that this change is legal?
		// Container uiComponent = (Container) getUIComponent();
		
		Object uiComponent = getUIComponent();
		
		// Component childGenericWidget = child.getGenericWidget();
		int position = this.childrenVector.lastIndexOf(child);
		if (position < 0
//				&& (getDeletedAdapter(child.getPropertyName()) == null)
				// || !child.isVisible()) {
				&& getDeletedAdapter(child.getPropertyName()) == null) {

			// Message.error("Illegal child position: " + position +
			// " new value: " + newValue + ". Old value:" +
			// child.getRealObject() + ". You may have to refresh again.");
			return null;
		}

		/*
		 * uiComponent.remove(child.getGenericWidget());
		 * 
		 * //Component[] components = ((Container)
		 * getUIComponent()).getComponents(); Component[] components =
		 * uiComponent.getComponents();
		 * 
		 * //System.out.println("componnets length" + components.length); for
		 * (position=0;position<components.length; position++) {
		 * //System.out.println ("pos" + position + "comp" +
		 * components[position]); //if
		 * (child.getGenericWidget().equals(components[position])) if
		 * (childGenericWidget== components[position]) break;
		 * 
		 * } if (position == components.length) {
		 * System.out.println("Did not find component in replaceAdapter");
		 * return null; }
		 * 
		 * //System.out.println("components size before removal" +
		 * components.length); //System.out.println("position" + position);
		 * //((Container) getUIComponent()).remove(child.getGenericWidget());
		 */
		try {
			List<ShapeObjectAdapter> graphicsDescendents = child.getGraphicalDescendents();
			for (ShapeObjectAdapter shapeAdapter:graphicsDescendents) {
				shapeAdapter.getWidgetAdapter().removeFromParentUIContainer();
			}
			
			
			if (uiComponent != null && 
					!child.hasOnlyGraphicsDescendents() &&
					!isTreeNode() &&
					position >= 0) {
				// uiComponent.remove(position);
				// this.getWidgetAdapter().removeForReplacement( uiComponent,
				// position, child);
				this.getWidgetAdapter().removeForReplacement(position, child);
				
			}
			if (child.isVisible())
			   numDisplayedStaticChildren--;
		} catch (Exception e) {
			System.out.println("position: " + position);
			e.printStackTrace();
			// System.out.println(e);
		}


		// components = uiComponent.getComponents();

		position = numDisplayedStaticChildren;
		numDisplayedStaticChildren++;
		ClassProxy newClass =  ACompositeLoggable.getTargetClass(newValue);
		// property class creates problems
		// ClassProxy newClass = (newValue == null) ? child.getPropertyClass()
		// : RemoteSelector.getClass(newValue);

		// this is not really needed as deep create children later deals with this
		
//		uiGenerator.clearVisitedObjects();
//		uiGenerator.clearVisitedObject(child);

		// getTopAdapter().clearVisitedObjects();
		Boolean textMode = null;
		/*
		 * if (child.getPropertyName().indexOf("loc") != -1 ||
		 * child.getPropertyName().indexOf("coord") != -1 ||
		 * child.getPropertyName().indexOf("point") != -1 ||
		 * child.getPropertyName().indexOf("offset") != -1) textMode = true;
		 */

		if (IntrospectUtility.isPointType(newClass))
			textMode = true; // text mode not used
		boolean isTreeNode = isTreeNode();
		textMode = isTreeNode?true:null;
		(new ClearVisitedNodeAdapterVisitor(child)).traverseNonAtomicContainers();
		ObjectAdapter adapter = uiGenerator.createGraphObjectAdapter(
				// (Container) getUIComponent(),
				// this, newValue, newClass, position, child.getPropertyName(),
				// getRealObject(), (child.getAdapterType() == PROPERTY_TYPE));
				// this, newValue, newClass, position, child.getPropertyName(),
				// getRealObject(), (child.getAdapterType() == PROPERTY_TYPE),
				// child.isElided() || child.expandElidedString());
				this, newValue, newClass, position, child.getPropertyName(),
				getRealObject(), (child.getAdapterType() == PROPERTY_TYPE),
				textMode);
//				isTreeNode());
//				null);
		// should we copy all the attributes, specially the temp attributes
		adapter.setColumnTitleStatus(child.getColumnTitleStatus());
		// adapter.setLabel(child.getLabel());
		uiGenerator.deepCreateChildren(adapter, false);
		

		if (!child.isVisible()) {
			removeChildInTables(adapter);
			return null;

		}

		VirtualComponent childComponent = child.getUIComponent();
		WidgetAdapterInterface widgetAdapter = child.getWidgetAdapter();
		if (childComponent != null && widgetAdapter != null
				&& getDeletedAdapter(child.getPropertyName()) != null) { // let
																			// us
																			// see
																			// if
			// we can
			// retarget;
			uiGenerator.deepSetAttributes(adapter);
			String desiredWidgetClassName = adapter.getPreferredWidget();
			ClassProxy desiredWidgetClass = null;
			try {
				desiredWidgetClass = AClassProxy
						.staticForName(desiredWidgetClassName);
				if (desiredWidgetClass.isAssignableFrom(RemoteSelector
						.getClass(childComponent.getPhysicalComponent()))) {
					adapter.setWidgetAdapter(widgetAdapter);
					widgetAdapter.setObjectAdapter(adapter);
					// widgetAdapter.setUIComponent(childComponent);
					// adapter.getConcreteObject().setTarget(adapter.getRealObject());
					widgetAdapter.setUIComponentValue(adapter.getRealObject());
					this.setChildAdapterMapping(adapter, position, child);
					return adapter;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// Think we can take this path always
		// if (true || getWidgetAdapter() != null || child.getUIComponent() !=
		// null) {
		// this used to be commented out
		// uiGenerator.deepSetAttributes(adapter);
		// uiGenerator.deepProcessAttributes(adapter, false);

		// boolean addVisibleChild = adapter.isVisible() && getWidgetAdapter()
		// != null && (! (adapter instanceof ShapeObjectAdapter)) &&
		// (adapter.getUIComponent() == null) && child.getUIComponent() == null;
		// if (addVisibleChild) {
		// // will also add the number of static children
		// addVisibleChild(adapter);
		// } else {

		// need this because otherwise adapter not added to flat table row
		uiGenerator.deepProcessAttributes(adapter, false);
		// }

		// if (adapter.isVisible() && getWidgetAdapter() != null && (! (adapter
		// instanceof ShapeObjectAdapter)) && (adapter.getUIComponent() == null)
		// && child.getUIComponent() == null) {
		// if (addVisibleChild) {
		// // we need to add adapter to parent;
		// addVisibleChild(adapter);
		// }
		// calling it deepProcessAttributes with "this" does not result in
		// add
		updateLeafAdapters(child, adapter);
		if (getWidgetAdapter() != null) {
		this.getWidgetAdapter().childComponentsAdded(true);
		this.getWidgetAdapter().descendentUIComponentsAdded();
		if (getGenericWidget() != null)
			getGenericWidget().processAttributes();
		}
		// }
		// uiFrame.deepElide(adapter);
		/*
		 * components = uiComponent.getComponents(); if (components[position] !=
		 * adapter.getGenericWidget()) {
		 * System.out.println("Components[position]" + components[position]);
		 * System.out.println("generic widget" + adapter.getGenericWidget()); }
		 */
		// components = ((Container) getUIComponent()).getComponents();

		// System.out.println("components size after addition" +
		// components.length);
		// childrenVector.setElementAt(adapter, position);
		this.setChildAdapterMapping(adapter, position, child);
		if (adapter.isReadOnly()) {
			adapter.setUneditable();
		}
		// Set up property change listener link if required!
		return adapter;
	}

	void updateLeafAdapters(ObjectAdapter old, ObjectAdapter newAdapter) {
		int index = leafComponents.indexOf(old);
		if (index < 0)
			return;
		leafComponents.set(index, newAdapter);
	}

	public void replaceAttributedObject(ObjectAdapter child, Object newValue) {
		replaceAdapter(child, newValue);
		/*
		 * // Assume that this change is legal? int position = 0; Component[]
		 * components = ((Container) getUIComponent()).getComponents(); for
		 * (;position<components.length; position++) if
		 * (child.getGenericWidget().equals(components[position])) break; if
		 * (position == components.length) return null; ((Container)
		 * getUIComponent()).remove(child.getGenericWidget()); uiObjectAdapter
		 * adapter = uiGenerator.uiAddComponents((Container) getUIComponent(),
		 * this, newValue, ClassSelector.getClass(newVaue), position,
		 * child.getPropertyName(), getRealObject(), (child.getAdapterType() ==
		 * PROPERTY_TYPE)); // Set up property change listener link if required!
		 * return adapter;
		 */

	}

	/*
	 * public void createChildren() { if (this.childrenCreated) return;
	 * uiAddClassComponents(); //propertiesCreated = true; if (!isTopAdapter())
	 * parent.descendentsCreated(); else
	 * uiGenerator.deepProcessAttributes(this);
	 * //recursivelyCheckIfNoVisibleChildren(this); }
	 */
	public void deleteExistingChildren() {
		Enumeration<String> keys = mapping.keys();
		while (keys.hasMoreElements()) {
			String nextKey = keys.nextElement();
			ObjectAdapter nextAdapter = mapping.get(nextKey);
			removeChildInTables(nextKey, nextAdapter);
			/*
			 * deletedChildren.put(nextKey, nextAdapter);
			 * mapping.remove(nextKey);
			 */
		}
	}

	public void createChildrenBasic() {
		deleteExistingChildren();
		addClassComponents(false);
	}

	public void createChildrenBasic(Hashtable sharedProps) {
		addClassComponents(sharedProps);
	}

	// Add components of a user defined class
	// Use reflection to identify fields and properties
	// Invoke uiAddComponents for each of these.
	// String longestProperty;
	/*
	 * int maxComponentNameLength = 0; public int getMaxComponentNameLength() {
	 * return maxComponentNameLength; }
	 */
	/*
	 * public int computedLabelLength() { String label = getLabel(); boolean
	 * isLabelVisible = isLabelled(); int labelLength = 0; if (isLabelVisible &&
	 * label != null) labelLength = label.length(); else labelLength =
	 * maxComponentNameLength; return labelLength; }
	 */

	/**
	 * @param recordStructure
	 * @param doNotGenerateIfPossible
	 * @param pobj
	 * @param componentName
	 * @param isUserObject
	 * @param propertyType
	 * @param childPos
	 * @return
	 */
	public ObjectAdapter addClassComponent(RecordStructure recordStructure,
			boolean doNotGenerateIfPossible, Object pobj, String componentName,
			boolean isUserObject, ClassProxy propertyType, int childPos) {
		ObjectAdapter parentAdapter = this;
		ClassProxy pcl = propertyType;
		Object obj = computeAndMaybeSetViewObject();
		ObjectAdapter childAdapter;
		if (pobj != null)
			pcl =  ACompositeLoggable.getTargetClass(pobj);
		childAdapter = deletedChildren.get(componentName.toLowerCase());
		ObjectAdapter deletedChild = childAdapter;
		boolean generate = true;
		if (childAdapter != null) {
			// if (a.getRealObject() == pobj)
			if (doNotGenerateIfPossible
					|| OEMisc.equalsClass(childAdapter.getRealObject(), pobj)) {
				generate = false;
				childAdapter.setDisposed(false);
			} else if (childAdapter instanceof PrimitiveAdapter
					|| childAdapter instanceof EnumerationAdapter) {
				// if (a.getPropertyClass() == pcl) {
				if (doNotGenerateIfPossible
						|| OEMisc.equals(childAdapter.getPropertyClass(), pcl)) {
					// a.setRealObject(pobj);
					// a.computeAndSetViewObject(pobj);
					generate = false;
				}
			}
		}

		if (generate) {
			// System.out.println("adapter" + this + "containW" + containW);
			Boolean textMode = null;
			Vector<Attribute> childAnnotationAttributes = recordStructure
					.getComponentAttributes(componentName);
			Boolean isAtomicShape = (Boolean) searchAttribute(
					AttributeNames.IS_ATOMIC_SHAPE, childAnnotationAttributes);
			if (isAtomicShape != null && (!isAtomicShape)) { // set text mode
																// true
				childAdapter = uiGenerator.createGraphObjectAdapter(
						/* containW, */
						// adaptor, pobj, pcl, count++, componentName, obj,
						// true);
						parentAdapter, pobj, pcl, childPos, componentName, obj,
						true, true);
			} else {
				boolean isTreeNode = isTreeNode();
				Boolean childTextMode = isTreeNode?true:null;
				childAdapter = uiGenerator.createGraphObjectAdapter(
						/* containW, */
						// adaptor, pobj, pcl, count++, componentName, obj,
						// true);
						parentAdapter, pobj, pcl, childPos, componentName, obj,
						true, childTextMode);
				if (childAdapter == null)
					return null;
			}
		} else if (childAdapter.getRealObject() != pobj) {
			childAdapter.retarget(pobj);
			// a.setRealObject(pobj);
			// a.computeAndSetViewObject(pobj);
		}
		// moving this earlier so we can use path in attributes
		// childAdapter.recalculateViewObject();
		childAdapter.setIndex(childPos);
		/*
		 * System.out.println("Path " + a.getPath());
		 * System.out.println("Beautified Path " + a.getBeautifiedPath());
		 * System.out.println("Complete Path " + a.getCompletePathOnly());
		 */
		LoggableRegistry.mapLoggableToAdapter(childAdapter);

		childAdapter.setNameChild();
		// if (getUIFrame() != null && !childAdapter.isGraphicsCompositeObject()
		// &&!childAdapter.isDrawingAdapter())
		// RightMenuManager.getRightMenu(RemoteSelector.getClass(pobj),
		// childAdapter, pobj);
		/*
		 * if (a.isReadOnly()) a.setUneditable(); if (deletedChild != null) {
		 * a.setCopiedLabel(true); a.setLabel(deletedChild.getLabel()); }
		 */

		visibleAndNonVisibleChildrenVector.add(childAdapter);
		if (childAdapter.isVisible()) {
			addChildInTables(componentName, childAdapter);

			numDisplayedStaticChildren++;

		} else {
			// no lower case before
			// deletedChildren.put(componentName, childAdapter);
			deletedChildren.put(componentName.toLowerCase(), childAdapter);
		}
		// this stuff moved down after tables have adapter

		// a.setNameChild();
		if (childAdapter.isReadOnly())
			childAdapter.setUneditable();
		if (deletedChild != null) {
			// commenting out because of visivility/invisibility problems
			// childAdapter.setCopiedLabel(true);
			// for some reason this was not acceptable
			// childAdapter.setLabel(deletedChild.getLabel());
			// when a node reappears this sets it to null
			// childAdapter.setLabel((String)
			// deletedChild.getTempAttributeValue(AttributeNames.LABEL));
			String originalLabel = (String) deletedChild
					.getTempAttributeValue(AttributeNames.LABEL);
			if (originalLabel != null) {
				childAdapter.setCopiedLabel(true);
				childAdapter.setLabel(originalLabel);
			}

		}
		// moving oath stuff later
		// a.setIndex(i);

		// LoggableRegistry.mapLoggableToAdapter(a);

		// not sure why this is done here and not at end
		/*
		 * this.childrenCreated = true; propertiesCreated = true; numFeatures =
		 * this.getChildAdapterCount();
		 */
		if (recordStructure.isReadOnly(componentName)) {

			// if (property.getWriteMethod() == null) {

			// if (newAdapter instanceof uiPrimitiveAdapter) {
			if (childAdapter.isLeafAdapter()) {
				// ((uiPrimitiveAdapter)
				// newAdapter).getWidgetAdapter().setUIComponentUneditable();
				// newAdapter.getWidgetAdapter().setUIComponentUneditable();
				childAdapter.setUneditable();
			}
			// ti
			// uiFrame.deepElide(this.getTopAdapter());
			// uiGenerator.deepProcessAttributes(a);

		}
		return childAdapter;
	}

	// public void addClassComponents(boolean doNotGenerateIfPossible) {
	public void addClassComponents(boolean doNotGenerateIfPossible) {
		numDisplayedStaticChildren = 0;
		// System.out.println("add class in class adapter");
		// uiFrame topFrame = this.getGenericWidget().getUIFrame();
		// this.childrenCreated = true;
		uiFrame topFrame = this.getUIFrame();
		CompositeAdapter adaptor = this;
		// Container containW = getGenericWidget();
		// uiWidgetAdapterInterface wa = adaptor.getWidgetAdapter();
		/*
		 * debugging this part out to allow children to be created if (wa ==
		 * null) return; Container containW = (Container)
		 * adaptor.getWidgetAdapter().getUIComponent();
		 */
		// Container containW = null;
		// Object obj = uiGenerator.getViewObject(getRealObject());
		// Object obj = getViewObject(getRealObject());
		Object obj = computeAndMaybeSetViewObject();

		if (obj == null) {
			this.childrenCreated = true;
			return;
		}
//		ClassProxy inputClass = RemoteSelector.getClass(obj);
		ClassProxy inputClass = ACompositeLoggable.getTargetClass(obj);

		String classname =  ACompositeLoggable.getTargetClass(obj).getName();
		// String realClassName = getRealObject().getClass().getName();
		if (topFrame != null) {
			topFrame.addClassToAttributeMenu(classname);
			// topFrame.addClassToAttributeMenu(realClassName);
		}
		// System.out.println("Editor reg of" + this + "class" +
		// EditorRegistry.getEditorClass(inputClass));
		/*
		 * if (EditorRegistry.getEditorClass(inputClass) != null){ //String
		 * compType = (String)
		 * this.getMergedAttributeValue(AttributeNames.PREFERRED_WIDGET); //if
		 * (EditorRegistry.getDefaultWidgetAdapter(compType) != null)
		 * //checkIfNoVisibleChildren(this); childrenCreated = true; return; }
		 */
		int count = 0; // Maintains the position of the next component in the
		// container widget.

		// ViewInfo cdesc =
		// ClassDescriptorCache.getClassDescriptor(obj.getClass());
		// if (topFrame != null)
		// if (topFrame != null && !isGraphicsCompositeObject()
		// &&!isDrawingAdapter())
		// RightMenuManager.getRightMenu(RemoteSelector.getClass(obj), this,
		// obj);
		//
		adaptor.setViewObject(obj);
		RecordStructure recordStructure = getRecordStructure();
		this.refreshConcreteObject(obj);
		// recordStructure.setTarget(obj);
		Vector componentNames = recordStructure.componentNames();
//		Vector componentNames = recordStructure.nonGraphicsComponentNames();

		// Get the fields
		// Class inputClass = obj.getClass();
		// boolean foundUnlabeledComposite = false;
		foundUnlabeledComposite = false;
		// hasPrimitiveChild = false;
		// hasCompositeChild = false;
		int maxLabelLength = 0;
		/*
		 * features = cdesc.getFeatureDescriptors(); if (features == null)
		 * //||features.length==0) return;
		 */
		// this.childrenCreated = true;
		childrenVector = new Vector();
		visibleAndNonVisibleChildrenVector = new Vector();
		for (int i = 0; i < componentNames.size(); i++) {
			ObjectAdapter a = null;
			String componentName = ((String) componentNames.elementAt(i));
			// maxComponentNameLength = Math.max(componentName.length(),
			// maxComponentNameLength);
			// if (features[i] instanceof PropertyDescriptor) {
			// PropertyDescriptor property = (PropertyDescriptor) features[i];
			// Class propertyType = property.getPropertyType();
			ClassProxy propertyType = recordStructure
					.componentType(componentName);
			Object pobj = recordStructure.get(componentName);

			a = addClassComponent(recordStructure, doNotGenerateIfPossible,
					pobj, componentName, false, propertyType, i);
			if (a == null) {
				continue;
			}

			// ti
			// uiFrame.deepElide(this.getTopAdapter());
			// uiGenerator.deepProcessAttributes(a);

		}
		// next three lines were commented out for some reason
		this.childrenCreated = true;
		propertiesCreated = true;
		// numFeatures = this.getChildAdapterCount();
		numFeatures = componentNames.size();

	}

	public void monolithicaddClassComponents(boolean doNotGenerateIfPossible) {
		numDisplayedStaticChildren = 0;
		// System.out.println("add class in class adapter");
		// uiFrame topFrame = this.getGenericWidget().getUIFrame();
		// this.childrenCreated = true;
		uiFrame topFrame = this.getUIFrame();
		CompositeAdapter adaptor = this;
		// Container containW = getGenericWidget();
		// uiWidgetAdapterInterface wa = adaptor.getWidgetAdapter();
		/*
		 * debugging this part out to allow children to be created if (wa ==
		 * null) return; Container containW = (Container)
		 * adaptor.getWidgetAdapter().getUIComponent();
		 */
		// Container containW = null;
		// Object obj = uiGenerator.getViewObject(getRealObject());
		// Object obj = getViewObject(getRealObject());
		Object obj = computeAndMaybeSetViewObject();

		if (obj == null) {
			this.childrenCreated = true;
			return;
		}
		ClassProxy inputClass =  ACompositeLoggable.getTargetClass(obj);
		String classname =  ACompositeLoggable.getTargetClass(obj).getName();
		// String realClassName = getRealObject().getClass().getName();
		if (topFrame != null) {
			topFrame.addClassToAttributeMenu(classname);
			// topFrame.addClassToAttributeMenu(realClassName);
		}
		// System.out.println("Editor reg of" + this + "class" +
		// EditorRegistry.getEditorClass(inputClass));
		/*
		 * if (EditorRegistry.getEditorClass(inputClass) != null){ //String
		 * compType = (String)
		 * this.getMergedAttributeValue(AttributeNames.PREFERRED_WIDGET); //if
		 * (EditorRegistry.getDefaultWidgetAdapter(compType) != null)
		 * //checkIfNoVisibleChildren(this); childrenCreated = true; return; }
		 */
		int count = 0; // Maintains the position of the next component in the
		// container widget.

		// ViewInfo cdesc =
		// ClassDescriptorCache.getClassDescriptor(obj.getClass());
		if (topFrame != null)
			RightMenuManager.getRightMenu( ACompositeLoggable.getTargetClass(obj), this,
					null);

		adaptor.setViewObject(obj);
		RecordStructure recordStructure = getRecordStructure();
		this.refreshConcreteObject(obj);
		// recordStructure.setTarget(obj);
		Vector componentNames = recordStructure.componentNames();

		// Get the fields
		// Class inputClass = obj.getClass();
		// boolean foundUnlabeledComposite = false;
		foundUnlabeledComposite = false;
		// hasPrimitiveChild = false;
		// hasCompositeChild = false;
		int maxLabelLength = 0;
		/*
		 * features = cdesc.getFeatureDescriptors(); if (features == null)
		 * //||features.length==0) return;
		 */
		// this.childrenCreated = true;
		childrenVector = new Vector();
		visibleAndNonVisibleChildrenVector = new Vector();
		for (int i = 0; i < componentNames.size(); i++) {
			ObjectAdapter a = null;
			String componentName = ((String) componentNames.elementAt(i));
			// maxComponentNameLength = Math.max(componentName.length(),
			// maxComponentNameLength);
			// if (features[i] instanceof PropertyDescriptor) {
			// PropertyDescriptor property = (PropertyDescriptor) features[i];
			// Class propertyType = property.getPropertyType();
			ClassProxy propertyType = recordStructure
					.componentType(componentName);
			Object pobj = recordStructure.get(componentName);

			ClassProxy pcl = propertyType;

			if (pobj != null)
				pcl =  ACompositeLoggable.getTargetClass(pobj);
			a = deletedChildren.get(componentName.toLowerCase());
			ObjectAdapter deletedChild = a;
			boolean generate = true;
			if (a != null) {
				// if (a.getRealObject() == pobj)
				if (doNotGenerateIfPossible
						|| OEMisc.equalsClass(a.getRealObject(), pobj))
					generate = false;
				else if (a instanceof PrimitiveAdapter
						|| a instanceof EnumerationAdapter) {
					// if (a.getPropertyClass() == pcl) {
					if (doNotGenerateIfPossible
							|| OEMisc.equals(a.getPropertyClass(), pcl)) {
						// a.setRealObject(pobj);
						// a.computeAndSetViewObject(pobj);
						generate = false;
					}
				}
			}

			if (generate) {
				// System.out.println("adapter" + this + "containW" + containW);
				Boolean textMode = null;
				a = uiGenerator.createObjectAdapter(
				/* containW, */
				adaptor, pobj, pcl, count++, componentName, obj, true);
			} else if (a.getRealObject() != pobj) {
				a.retarget(pobj);
				// a.setRealObject(pobj);
				// a.computeAndSetViewObject(pobj);
			}
			// moving this earlier so we can use path in attributes
			a.setIndex(i);
			/*
			 * System.out.println("Path " + a.getPath());
			 * System.out.println("Beautified Path " + a.getBeautifiedPath());
			 * System.out.println("Complete Path " + a.getCompletePathOnly());
			 */
			LoggableRegistry.mapLoggableToAdapter(a);

			a.setNameChild();
			/*
			 * if (a.isReadOnly()) a.setUneditable(); if (deletedChild != null)
			 * { a.setCopiedLabel(true); a.setLabel(deletedChild.getLabel()); }
			 */

			visibleAndNonVisibleChildrenVector.add(a);
			if (a.isVisible()) {
				addChildInTables(componentName, a);

				numDisplayedStaticChildren++;

			} else {
				deletedChildren.put(componentName, a);
			}
			// this stuff moved down after tables have adapter

			// a.setNameChild();
			if (a.isReadOnly())
				a.setUneditable();
			if (deletedChild != null) {
				a.setCopiedLabel(true);
				a.setLabel(deletedChild.getLabel());
			}
			// moving oath stuff later
			// a.setIndex(i);

			// LoggableRegistry.mapLoggableToAdapter(a);

			// not sure why this is done here and not at end
			/*
			 * this.childrenCreated = true; propertiesCreated = true;
			 * numFeatures = this.getChildAdapterCount();
			 */
			if (recordStructure.isReadOnly(componentName)) {

				// if (property.getWriteMethod() == null) {

				// if (newAdapter instanceof uiPrimitiveAdapter) {
				if (a.isLeafAdapter()) {
					// ((uiPrimitiveAdapter)
					// newAdapter).getWidgetAdapter().setUIComponentUneditable();
					// newAdapter.getWidgetAdapter().setUIComponentUneditable();
					a.setUneditable();
				}
				// ti
				// uiFrame.deepElide(this.getTopAdapter());
				// uiGenerator.deepProcessAttributes(a);
			}
		}
		// next three lines were commented out for some reason
		this.childrenCreated = true;
		propertiesCreated = true;
		// numFeatures = this.getChildAdapterCount();
		numFeatures = componentNames.size();

		/*
		 * 
		 * //System.out.println("Adapter" + a + " " +
		 * a.getAttributeValue(AttributeNames.LABEL)); //maxLabelLength =
		 * Math.max(maxLabelLength, ((String)
		 * a.getAttributeValue(AttributeNames.LABEL)).length()); maxLabelLength
		 * = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());
		 * //maxLabelLength = Math.max(maxLabelLength,
		 * a.getGenericWidget().getLabelComponent().getSize().width);
		 * //System.out.println(maxLabelLength + " " +
		 * a.getGenericWidget().getLabel().length());
		 * //a.setParentAdapter(adaptor); // Set the propertyFlag
		 * a.setAdapterType(uiObjectAdapter.PROPERTY_TYPE); // Set the
		 * propertyName a.setPropertyName(componentName); // Set the read/write
		 * methods
		 * 
		 * //a.setPreMethods(obj.getClass()); if
		 * (recordStructure.isReadOnly(componentName)) { //if
		 * (property.getWriteMethod() == null) {
		 * //System.out.println("no writemethod for" + a.getBeautifiedPath());
		 * //if (a instanceof uiPrimitiveAdapter) { if (a.isLeaf()) {
		 * //((uiPrimitiveAdapter)
		 * a).getWidgetAdapter().setUIComponentUneditable();
		 * a.getWidgetAdapter().setUIComponentUneditable(); }
		 * //System.out.println("adapter" + a + "writemethod set"); if
		 * (a.getUIComponent() == null) {
		 * 
		 * String label = (String)
		 * a.getMergedAttributeValue(AttributeNames.LABEL); //label =
		 * getGenericWidget().getLabel(); ((Container)
		 * getUIComponent()).remove(a.getGenericWidget());
		 * //a.setTempAttributeValue(AttributeNames.LABEL, label);
		 * //a.getGenericWidget().setLabel(label);
		 * //a.setTempAttributeValue(AttributeNames.LABEL,label ); //Attribute a
		 * = new Attribute(AttributeNames.LABEL,label);
		 * a.getWidgetAdapter().processAttribute(new Attribute("class." +
		 * AttributeNames.LABEL,label));
		 * 
		 * continue; }
		 * 
		 * // Check if the property is editable // and set the editable
		 * attribute to false if // it isnt. //System.out.println ("adapter" + a
		 * + " " + a.getAttributeValue(AttributeNames.LABEL));
		 * //System.out.println(a + a.getGenericWidget().getLabel());
		 * //maxLabelLength = Math.max(maxLabelLength, ((String)
		 * a.getAttributeValue(AttributeNames.LABEL)).length());
		 * //maxLabelLength = Math.max(maxLabelLength,
		 * a.getGenericWidget().getLabel().length());
		 * //System.out.println(maxLabelLength + " "
		 * +a.getGenericWidget().getLabel().length() ); if (!(a instanceof
		 * uiPrimitiveAdapter)) { ViewInfo childDesc =
		 * ClassDescriptorCache.getClassDescriptor(pcl); //System.out.println
		 * ("non primitive adapter"); //System.out.println(childDesc);
		 * //System.out
		 * .println(childDesc.getBeanDescriptor().getValue("direction")); if
		 * (childDesc.getBeanDescriptor() != null &&
		 * !a.getGenericWidget().isLabelVisible() &&
		 * //!"horizontal".equals(childDesc
		 * .getBeanDescriptor().getValue("direction")))
		 * !"horizontal".equals(a.getMergedAttributeValue("direction")))
		 * foundUnlabeledComposite = true; //System.out.println
		 * ("found composite"); }
		 * 
		 * } }
		 * 
		 * if (checkIfNoVisibleChildren(this)) {
		 * //this.getUIFrame().maybeHideMainPanel(); return; } //if
		 * (reparentChild(this)) { if (reparentChild()) { uiObjectAdapter a =
		 * (uiObjectAdapter) childrenVector.elementAt(0);
		 * this.setSkippedAdapter(); String label = (String)
		 * adaptor.getTempAttributeValue(AttributeNames.LABEL); if
		 * (adaptor.isTopAdapter()) return;
		 * a.setTempAttributeValue(AttributeNames.LABEL, label);
		 * a.getGenericWidget().setLabel(label);
		 * a.setTempAttributeValue(AttributeNames.LABEL,label ); return; } if
		 * (this instanceof uiVectorAdapter || this instanceof
		 * uiHashtableAdapter ) return; uiGenericWidget gw =
		 * this.getGenericWidget();
		 * 
		 * setDirection(); boolean alignHorizontal =
		 * direction.equals("horizontal"); boolean square = false; boolean box =
		 * false;
		 * 
		 * uiFrame.deepElide(this.getTopAdapter()); processDirection();
		 */
	}

	public void addClassComponents(Hashtable sharedProps) {

		// System.out.println("add class in class adapter");
		// uiFrame topFrame = this.getGenericWidget().getUIFrame();
		// this.childrenCreated = true;
		uiFrame topFrame = this.getUIFrame();
		CompositeAdapter adaptor = this;
		// Container containW = getGenericWidget();
		// uiWidgetAdapterInterface wa = adaptor.getWidgetAdapter();
		/*
		 * debugging this part out to allow children to be created if (wa ==
		 * null) return; Container containW = (Container)
		 * adaptor.getWidgetAdapter().getUIComponent();
		 */
		// Container containW = null;
		// Object obj = uiGenerator.getViewObject(getRealObject());
		// Object obj = getViewObject(getRealObject());
		Object obj = computeAndMaybeSetViewObject();
		if (obj == null) {
			this.childrenCreated = true;
			return;
		}
		ClassProxy inputClass =  ACompositeLoggable.getTargetClass(obj);
		String classname =  ACompositeLoggable.getTargetClass(obj).getName();
		// String realClassName = getRealObject().getClass().getName();
		if (topFrame != null) {
			topFrame.addClassToAttributeMenu(classname);
			// topFrame.addClassToAttributeMenu(realClassName);
		}
		// System.out.println("Editor reg of" + this + "class" +
		// EditorRegistry.getEditorClass(inputClass));
		/*
		 * if (EditorRegistry.getEditorClass(inputClass) != null){ //String
		 * compType = (String)
		 * this.getMergedAttributeValue(AttributeNames.PREFERRED_WIDGET); //if
		 * (EditorRegistry.getDefaultWidgetAdapter(compType) != null)
		 * //checkIfNoVisibleChildren(this); childrenCreated = true; return; }
		 */
		int count = 0; // Maintains the position of the next component in the
		// container widget.

		// ViewInfo cdesc =
		// ClassDescriptorCache.getClassDescriptor(obj.getClass());
		RightMenuManager.getRightMenu( ACompositeLoggable.getTargetClass(obj), this, obj);

		adaptor.setViewObject(obj);
		RecordStructure recordStructure = getRecordStructure();
		this.refreshConcreteObject(obj);
		// recordStructure.setTarget(obj);
		Vector componentNames = recordStructure.componentNames();

		// Get the fields
		// Class inputClass = obj.getClass();
		// boolean foundUnlabeledComposite = false;
		foundUnlabeledComposite = false;
		int maxLabelLength = 0;
		/*
		 * features = cdesc.getFeatureDescriptors(); if (features == null)
		 * //||features.length==0) return;
		 */
		// this.childrenCreated = true;
		childrenVector = new Vector();
		visibleAndNonVisibleChildrenVector = new Vector();
		for (int i = 0; i < componentNames.size(); i++) {
			ObjectAdapter a = null;
			String componentName = (String) componentNames.elementAt(i);
			if (sharedProps.get(util.misc.Common.beautify(componentName)) != null)
				continue;
			// if (features[i] instanceof PropertyDescriptor) {
			// PropertyDescriptor property = (PropertyDescriptor) features[i];
			// Class propertyType = property.getPropertyType();
			ClassProxy propertyType = recordStructure
					.componentType(componentName);
			Object pobj = recordStructure.get(componentName);

			ClassProxy pcl = propertyType;

			if (pobj != null)
				pcl =  ACompositeLoggable.getTargetClass(pobj);
			// System.out.println("adapter" + this + "containW" + containW);
			a = uiGenerator.createObjectAdapter(
			// containW,
					adaptor, pobj, pcl, count++, componentName, obj, true);
			// System.out.println("finished uiAddComponents");
			// adaptor.setChildAdapterMapping(property.getName(), a);
			if (adaptor.isVisible()) {
				addChildInTables(componentName, a);
				/*
				 * adaptor.setChildAdapterMapping(componentName, a);
				 * adaptor.setChildAdapterMapping(a);
				 */
			} else
				deletedChildren.put(componentName, a);
			a.setIndex(i);
			// not sure why this is done here and not at end
			this.childrenCreated = true;
			propertiesCreated = true;
			numFeatures = this.getChildAdapterCount();
			a.setPropertyName(componentName);
			char chars[] = componentName.toCharArray(); // making the label
														// start w/ an uppercase
														// letter
			// i don't know where it got changed to lowercase in the past (maybe
			// the field/prop descriptors stuff did it
			// anyhow, I need to make it start w/ upper case becasue when doing
			// retargeting i'm looking at the widget
			// names for the mapping to shared properties. I know I could just
			// look at something more pure like the actual
			// source object itself but I don't want to make strong connections
			// to the source object also the partitioning
			// of properties and commands has already been done when creating
			// the UI so why do it again on the source obj.
			// later maybe I could add an instance variable that holds the
			// properties unformatted name in uiGenericWidget
			// and use this and have the displayed name more formatted

			chars[0] = Character.toUpperCase(chars[0]);
			if (a.getGenericWidget() != null)
				a.getGenericWidget().setLabel(new String(chars)); // perform...added
																	// so it
																	// would
																	// show the
																	// property
																	// name..for
																	// some
																	// reason it
																	// isn't
			else
				a.setLabel(new String(chars));
			if (recordStructure.isReadOnly(componentName)) {

				// if (property.getWriteMethod() == null) {

				// if (newAdapter instanceof uiPrimitiveAdapter) {
				if (a.isLeafAdapter()) {
					// ((uiPrimitiveAdapter)
					// newAdapter).getWidgetAdapter().setUIComponentUneditable();
					// newAdapter.getWidgetAdapter().setUIComponentUneditable();
					a.setUneditable();
				}
				// ti
				// uiFrame.deepElide(this.getTopAdapter());
				// uiGenerator.deepProcessAttributes(a);
			}
		}
		// next three lines were commented out for some reason
		this.childrenCreated = true;
		propertiesCreated = true;
		numFeatures = this.getChildAdapterCount();

		/*
		 * 
		 * //System.out.println("Adapter" + a + " " +
		 * a.getAttributeValue(AttributeNames.LABEL)); //maxLabelLength =
		 * Math.max(maxLabelLength, ((String)
		 * a.getAttributeValue(AttributeNames.LABEL)).length()); maxLabelLength
		 * = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());
		 * //maxLabelLength = Math.max(maxLabelLength,
		 * a.getGenericWidget().getLabelComponent().getSize().width);
		 * //System.out.println(maxLabelLength + " " +
		 * a.getGenericWidget().getLabel().length());
		 * //a.setParentAdapter(adaptor); // Set the propertyFlag
		 * a.setAdapterType(uiObjectAdapter.PROPERTY_TYPE); // Set the
		 * propertyName a.setPropertyName(componentName); // Set the read/write
		 * methods
		 * 
		 * //a.setPreMethods(obj.getClass()); if
		 * (recordStructure.isReadOnly(componentName)) { //if
		 * (property.getWriteMethod() == null) {
		 * //System.out.println("no writemethod for" + a.getBeautifiedPath());
		 * //if (a instanceof uiPrimitiveAdapter) { if (a.isLeaf()) {
		 * //((uiPrimitiveAdapter)
		 * a).getWidgetAdapter().setUIComponentUneditable();
		 * a.getWidgetAdapter().setUIComponentUneditable(); }
		 * //System.out.println("adapter" + a + "writemethod set"); if
		 * (a.getUIComponent() == null) {
		 * 
		 * String label = (String)
		 * a.getMergedAttributeValue(AttributeNames.LABEL); //label =
		 * getGenericWidget().getLabel(); ((Container)
		 * getUIComponent()).remove(a.getGenericWidget());
		 * //a.setTempAttributeValue(AttributeNames.LABEL, label);
		 * //a.getGenericWidget().setLabel(label);
		 * //a.setTempAttributeValue(AttributeNames.LABEL,label ); //Attribute a
		 * = new Attribute(AttributeNames.LABEL,label);
		 * a.getWidgetAdapter().processAttribute(new Attribute("class." +
		 * AttributeNames.LABEL,label));
		 * 
		 * continue; }
		 * 
		 * // Check if the property is editable // and set the editable
		 * attribute to false if // it isnt. //System.out.println ("adapter" + a
		 * + " " + a.getAttributeValue(AttributeNames.LABEL));
		 * //System.out.println(a + a.getGenericWidget().getLabel());
		 * //maxLabelLength = Math.max(maxLabelLength, ((String)
		 * a.getAttributeValue(AttributeNames.LABEL)).length());
		 * //maxLabelLength = Math.max(maxLabelLength,
		 * a.getGenericWidget().getLabel().length());
		 * //System.out.println(maxLabelLength + " "
		 * +a.getGenericWidget().getLabel().length() ); if (!(a instanceof
		 * uiPrimitiveAdapter)) { ViewInfo childDesc =
		 * ClassDescriptorCache.getClassDescriptor(pcl); //System.out.println
		 * ("non primitive adapter"); //System.out.println(childDesc);
		 * //System.out
		 * .println(childDesc.getBeanDescriptor().getValue("direction")); if
		 * (childDesc.getBeanDescriptor() != null &&
		 * !a.getGenericWidget().isLabelVisible() &&
		 * //!"horizontal".equals(childDesc
		 * .getBeanDescriptor().getValue("direction")))
		 * !"horizontal".equals(a.getMergedAttributeValue("direction")))
		 * foundUnlabeledComposite = true; //System.out.println
		 * ("found composite"); }
		 * 
		 * } }
		 * 
		 * if (checkIfNoVisibleChildren(this)) {
		 * //this.getUIFrame().maybeHideMainPanel(); return; } //if
		 * (reparentChild(this)) { if (reparentChild()) { uiObjectAdapter a =
		 * (uiObjectAdapter) childrenVector.elementAt(0);
		 * this.setSkippedAdapter(); String label = (String)
		 * adaptor.getTempAttributeValue(AttributeNames.LABEL); if
		 * (adaptor.isTopAdapter()) return;
		 * a.setTempAttributeValue(AttributeNames.LABEL, label);
		 * a.getGenericWidget().setLabel(label);
		 * a.setTempAttributeValue(AttributeNames.LABEL,label ); return; } if
		 * (this instanceof uiVectorAdapter || this instanceof
		 * uiHashtableAdapter ) return; uiGenericWidget gw =
		 * this.getGenericWidget();
		 * 
		 * setDirection(); boolean alignHorizontal =
		 * direction.equals("horizontal"); boolean square = false; boolean box =
		 * false;
		 * 
		 * uiFrame.deepElide(this.getTopAdapter()); processDirection();
		 */
	}

	public void setDefaultSynthesizedAttributes() {
		super.setDefaultSynthesizedAttributes();
		recordStructure = getRecordStructure();
		Vector componentNames = recordStructure.componentNames();
		foundUnlabeledComposite = false;
		int maxLabelLength = 0;
		// getchildadaptercount has side effects!
		if (computeHasOnlyChild(this) ) {

//		if (getChildAdapterCountWithoutSideEffects() == 1 && computeHasOnlyChild(this) ) {
//		if (getChildAdapterCountWithoutSideEffects() > 0 && computeHasOnlyChild(this) ) {
			Boolean promote = getPromoteOnlyChild();
		
			// as we are not really re
			ObjectAdapter a = (ObjectAdapter) childrenVector.elementAt(0);
			if (promote == null) {
				promote = computePromoteOnlyChild(a);
			}
			if (!promote) return;
			onlyChild = a;
			if (isTopDisplayedAdapter()) {
				a.setTopDisplayedAdapter(true);
			}
			this.setSkippedAdapter();
			a.setIsOnlyChild(true);
//			a.propagateAttributesToWidgetShell();
//			adaptor.propagateAttributesToWidgetShell();
			
		}
//		for (int i = 0; i < componentNames.size(); i++) {
//			String componentName = (String) componentNames.elementAt(i);
//			ObjectAdapter a = getStaticChildAdapterMapping(componentName);
//		}
		/*
		 * if (!childrenCreated) return; //if (childrenCreated)
		 * //setDirection(); //redoStaticComponents();
		 * 
		 * //if (redoStaticComponents()) { if (childrenVisibilityChanged()) { //
		 * why should parent be deleted, not deleting it causes stack overflow
		 * cleanUpForReuse(); //cleanUpForReuse(); removeChildrenWidgets();
		 * 
		 * uiGenerator.deepCreateChildren(this);
		 * uiGenerator.deepSetAttributes(this); setAddMeBack(true);
		 * 
		 * //uiGenerator.deepElide(this); }
		 */

	}

	public boolean isChildReadable(ObjectAdapter adapter) {
		String componentName = adapter.getPropertyName();
		if (componentName == null)
			return true;
		RecordStructure recordStructure = getRecordStructure();
		if (recordStructure == null)
			return true;
		// boolean readable = getRecordStructure().preRead(componentName);
		// return readable;
		return getRecordStructure().preRead(componentName);

	}

	public boolean isChildWriteable(ObjectAdapter adapter) {
		String componentName = adapter.getPropertyName();
		if (componentName == null)
			return true;
		// boolean readable = getRecordStructure().preRead(componentName);
		// return readable;
		if (getRecordStructure() == null)
			return true;
		return getRecordStructure().preWrite(componentName);

	}

	// function with side effects
	boolean childrenVisibilityChanged() {
		if (!childrenCreated)
			return false;
		recordStructure = getRecordStructure();
		if (recordStructure == null)
			return false;
		Vector componentNames = recordStructure.componentNames();
		// numDisplayedStaticChildren = 0;
		boolean childVisibilityChanged = false;
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);

			if (a == null) {
				a = deletedChildren.get(componentName);
				if (a != null /*
							 * && a.getRealObject().getClass() ==
							 * recordStructure.get(componentName).getClass()
							 */) {
					// a.mergedAttributeList = null;
					a.recomputeAttributes(); // this is a side effect, and we
												// need it for every child for
												// now
					if (a.isVisible()) {
						// a.setIndex(numDisplayedStaticChildren);
						// numDisplayedStaticChildren++;
						// addChildInTables(componentName, a);
						// a.registerAsListener();
						/*
						 * setChildAdapterMapping(componentName, a);
						 * setChildAdapterMapping(a);
						 */

						// this is going to add the component but perhaps in the
						// wrong location
						// inefficient as we will remove the component
						// uiGenerator.deepProcessAttributes(a);
						// uiGenerator.deepSetAttributes(a);
						childVisibilityChanged = true;
						// return true;
						// continue;
					}
				}
			} else {
				if (!a.isVisible()) {
					// removeChildInTables(componentName, a);
					childVisibilityChanged = true;
					// return true;
				}
				// }
				// if (a == null || !a.isVisible() /*|| childMadeVisible*/)
				// continue;
				// a.setIndex(numDisplayedStaticChildren);
				// numDisplayedStaticChildren++;
			}
		}
		if (childVisibilityChanged) {
			// cleanUpForReuse();
			// removeChildrenWidgets();
			return true;
		}
		return false;
	}

	List<ObjectAdapter> childrenWhoseVisibilityChanged() {
		Vector<ObjectAdapter> children = new Vector();
		if (!childrenCreated)
			return children;
		recordStructure = getRecordStructure();
		if (recordStructure == null)
			return children;
		Vector componentNames = recordStructure.componentNames();
		// numDisplayedStaticChildren = 0;
		boolean childVisibilityChanged = false;
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);

			if (a == null) {
				a = deletedChildren.get(componentName);
				if (a != null /*
							 * && a.getRealObject().getClass() ==
							 * recordStructure.get(componentName).getClass()
							 */) {
					// a.mergedAttributeList = null;
					a.recomputeAttributes(); // this is a side effect, and we
												// need it for every child for
												// now
					if (a.isVisible()) {
						// a.setIndex(numDisplayedStaticChildren);
						// numDisplayedStaticChildren++;
						// addChildInTables(componentName, a);
						// a.registerAsListener();
						/*
						 * setChildAdapterMapping(componentName, a);
						 * setChildAdapterMapping(a);
						 */

						// this is going to add the component but perhaps in the
						// wrong location
						// inefficient as we will remove the component
						// uiGenerator.deepProcessAttributes(a);
						// uiGenerator.deepSetAttributes(a);
						// childVisibilityChanged = true;
						children.add(a);
						// return true;
						// continue;
					}
				}
			} else {
				if (!a.isVisible()) {
					// removeChildInTables(componentName, a);
					// childVisibilityChanged = true;
					children.add(a);
					// return true;
				}
				// }
				// if (a == null || !a.isVisible() /*|| childMadeVisible*/)
				// continue;
				// a.setIndex(numDisplayedStaticChildren);
				// numDisplayedStaticChildren++;
			}
		}
		return children;
	}

	AListPair<ObjectAdapter> newlyVisibleAndInvisibleChildren() {
		AListPair<ObjectAdapter> listPair = new AListPair();
		Vector<ObjectAdapter> visibleChildren = new Vector();
		Vector<ObjectAdapter> invisibleChildren = new Vector();
		listPair.list1 = visibleChildren;
		listPair.list2 = invisibleChildren;
		if (!childrenCreated)
			return listPair;
		recordStructure = getRecordStructure();
		if (recordStructure == null)
			return listPair;
		Vector componentNames = recordStructure.componentNames();
		// numDisplayedStaticChildren = 0;
		boolean childVisibilityChanged = false;
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);

			if (a == null) {
				a = deletedChildren.get(componentName.toLowerCase());
				// a = deletedChildren.get(componentName);
				if (a != null /*
							 * && a.getRealObject().getClass() ==
							 * recordStructure.get(componentName).getClass()
							 */) {
					// a.mergedAttributeList = null;
					a.recomputeAttributes(); // this is a side effect, and we
												// need it for every child for
												// now
					if (a.isVisible()) {
						// a.setIndex(numDisplayedStaticChildren);
						// numDisplayedStaticChildren++;
						// addChildInTables(componentName, a);
						// a.registerAsListener();
						/*
						 * setChildAdapterMapping(componentName, a);
						 * setChildAdapterMapping(a);
						 */

						// this is going to add the component but perhaps in the
						// wrong location
						// inefficient as we will remove the component
						// uiGenerator.deepProcessAttributes(a);
						// uiGenerator.deepSetAttributes(a);
						// childVisibilityChanged = true;
						visibleChildren.add(a);
						// return true;
						// continue;
					}
				}
			} else {
				if (!a.isVisible()) {
					// removeChildInTables(componentName, a);
					// childVisibilityChanged = true;
					invisibleChildren.add(a);
					// return true;
				}
				// }
				// if (a == null || !a.isVisible() /*|| childMadeVisible*/)
				// continue;
				// a.setIndex(numDisplayedStaticChildren);
				// numDisplayedStaticChildren++;
			}
		}
		return listPair;
	}

	boolean redoStaticComponents() {
		if (!childrenCreated)
			return false;
		recordStructure = getRecordStructure();
		Vector componentNames = recordStructure.componentNames();
		numDisplayedStaticChildren = 0;
		boolean childVisibilityChanged = false;
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);

			if (a == null) {
				a = deletedChildren.get(componentName);
				if (a != null /*
							 * && a.getRealObject().getClass() ==
							 * recordStructure.get(componentName).getClass()
							 */) {
					// a.mergedAttributeList = null;
					a.recomputeAttributes();
					if (a.isVisible()) {
						a.setIndex(numDisplayedStaticChildren);
						numDisplayedStaticChildren++;
						addChildInTables(componentName, a);
						// a.registerAsListener();
						/*
						 * setChildAdapterMapping(componentName, a);
						 * setChildAdapterMapping(a);
						 */

						// this is going to add the component but perhaps in the
						// wrong location
						// inefficient as we will remove the component
						// uiGenerator.deepProcessAttributes(a);
						uiGenerator.deepSetAttributes(a);
						childVisibilityChanged = true;
						// continue;
					}
				}
			} else {
				if (!a.isVisible()) {
					removeChildInTables(componentName, a);
					childVisibilityChanged = true;
				}
				// }
				if (a == null || !a.isVisible() /* || childMadeVisible */)
					continue;
				a.setIndex(numDisplayedStaticChildren);
				numDisplayedStaticChildren++;
			}
		}
		if (childVisibilityChanged) {
			// cleanUpForReuse();
			// removeChildrenWidgets();
			return true;
		}
		return false;
	}

	boolean manualUI = false;

	public void setManualUI(boolean newVal) {
		manualUI = newVal;
	}

	public boolean getManualUI() {
		return manualUI;
	}

	public void redoAttributesOfNewlyVisibleChildren(
			List<ObjectAdapter> children) {
		for (int i = 0; i < children.size(); i++) {
			ObjectAdapter child = children.get(i);
			// if (child.isVisible()) {
			uiGenerator.deepProcessAttributes(child);
			// }
		}
		if (getWidgetAdapter() != null)
			getWidgetAdapter().childComponentsAdded(true);

	}

	public void redoALabelsOfPreviouslyVisibleChildren(
			List<ObjectAdapter> children) {
		if (children.size() == 0)
			return;
		recordStructure = getRecordStructure();
		if (recordStructure == null)
			return;
		Vector componentNames = recordStructure.componentNames();
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);
			if (a != null && !children.contains(a)
					&& a.getWidgetShell() != null) {
				a.getWidgetShell().setLabelWidth();
			}
		}

	}

	@Override
	public boolean redoVisibleChildren() {
		return refreshStaticComponents();
	}

	// boolean refreshingStaticComponents;
	@Override
	public void addComponents() {
		addClassComponents(true);

	}

	public boolean refreshStaticComponents() {
		// boolean childrenVisibilityChanged = redoStaticComponents();
		// List<uiObjectAdapter> childrenWhoseVisibilityChanged =
		// childrenWhoseVisibilityChanged();
		AListPair<ObjectAdapter> childrenWhoseVisibilityChanged = newlyVisibleAndInvisibleChildren();
		// boolean childrenVisibilityChanged = childrenVisibilityChanged();
		// boolean childrenVisibilityChanged =
		// childrenWhoseVisibilityChanged.size() > 0;
		boolean childrenVisibilityChanged = childrenWhoseVisibilityChanged.list1
				.size() > 0 || childrenWhoseVisibilityChanged.list2.size() > 0;
		/*
		 * if (refreshingStaticComponents) return; refreshingStaticComponents =
		 * true;
		 */
		if (!childrenVisibilityChanged)
			return false;
		int oldMaxComponentNameWidth = getMaxComponentNameLength();
		// if (childrenVisibilityChanged ) {
		// uiGenerator.deepCreateChildren(this);
		// removeChildrenWidgets();
		// uiGenerator.deepElide(this);
		// uiGenerator.deepSetAttributes(this);
		/*
		 * uiGenerator.deepCreateChildren(this); addChildUIComponents();
		 */
		// set sythesized attributes will find this, right?
		/*
		 * uiWidgetAdapterInterface parentWidgetAdapter = null;
		 * 
		 * if (getParentAdapter() != null) parentWidgetAdapter =
		 * getParentAdapter().getWidgetAdapter(); if (parentWidgetAdapter !=
		 * null) parentWidgetAdapter.setIncrementalChildAddition(true);
		 */

		// get the top table adapter and refresh it.
		// uiObjectAdapter rebuildFromAdapter = getFlatTableRowAncestor();
		CompositeAdapter rebuildFromAdapter = getTopFlatTableRow();
		// removeChildrenWidgetsAtThisLevel();
		removeNewlyInvisibleChildren(childrenWhoseVisibilityChanged.list2);
		removeChildrenInTables();
		// removeStaticChildrenInTables();
		// addClassComponents(true);
		addComponents();
		addNewlyVisibleGraphicalChildren(childrenWhoseVisibilityChanged.list1);

		// uiGenerator.deepProcessAttributes(this);
		// rebuildFromAdapter.makeHorizontalColumnTitles();

		// uiGenerator.deepCreateChildren(this);
		this.setDefaultAttributes();
		// uiGenerator.deepAddChildUIComponents(this);
		// this.addChildUIComponents();

		redoAttributesOfNewlyVisibleChildren(childrenWhoseVisibilityChanged.list1);

		if (oldMaxComponentNameWidth != getMaxComponentNameLength()) {
			redoALabelsOfPreviouslyVisibleChildren(childrenWhoseVisibilityChanged.list1);
		}

		this.setDefaultSynthesizedAttributes();
		// propagateAttributesToWidgetShell();
		// uiGenerator.deepElide(this);
		uiGenerator.deepElide(this);

		if (rebuildFromAdapter == null) {
			/*
			 * removeChildrenWidgetsAtThisLevel(); removeChildrenInTables();
			 * createChildren(); this.setDefaultAttributes();
			 * this.setDefaultSynthesizedAttributes();
			 * this.addChildUIComponents(); propagateAttributesToWidgetShell();
			 * uiGenerator.deepElide(this);
			 */
			// addNewlyVisibleChildren(childrenWhoseVisibilityChanged.list1);

			if (getParentAdapter() != null) {
				WidgetAdapterInterface parentWidgetAdapter = getParentAdapter()
						.getWidgetAdapter();

				// parentWidgetAdapter.add(this);
				// parentWidgetAdapter.childComponentsAdded(true);
				if (parentWidgetAdapter != null)
					parentWidgetAdapter.refillColumnTitle(this);
			}
			// addNewlyVisibleChildren(childrenWhoseVisibilityChanged.list1);
			/*
			 * propagateAttributesToWidgetShell(); uiGenerator.deepElide(this);
			 */

		} else {
			/*
			 * removeChildrenWidgetsAtThisLevel(); removeChildrenInTables();
			 * addClassComponents(true);
			 * 
			 * 
			 * //uiGenerator.deepCreateChildren(this);
			 * this.setDefaultAttributes();
			 * //uiGenerator.deepAddChildUIComponents(this);
			 * //this.addChildUIComponents();
			 * 
			 * redoVisibleChildrenAttributes(childrenWhoseVisibilityChanged);
			 * 
			 * this.setDefaultSynthesizedAttributes();
			 * //propagateAttributesToWidgetShell();
			 * //uiGenerator.deepElide(this);
			 */

			if (rebuildFromAdapter != null
					&& rebuildFromAdapter.getWidgetAdapter() != null) {
				WidgetAdapterInterface ancestorWidgetAdapter = rebuildFromAdapter
						.getWidgetAdapter();

				ancestorWidgetAdapter.add(this);
				invalidateAncestorLeafAdapters();
				ancestorWidgetAdapter.descendentUIComponentsAdded();
			}
			uiGenerator.deepElide(this);

			// uiGenerator.deepProcessAttributes(this);
			// uiGenerator.deepElide(this);

			/*
			 * if (parentWidgetAdapter != null)
			 * parentWidgetAdapter.setIncrementalChildAddition(false);
			 */

		}
		return true;
		// uiGenerator.deepElide(this);
		// }
		// refreshingStaticComponents = true;
	}

	boolean foundProperties;

	public boolean addChildUIComponents() {
		if (getWidgetAdapter() == null && 
				!isCompositeShape() && 
				!getUIFrame().isOnlyGraphicsPanel())
//				hasOnlyGraphicsDescendents())
			return false;
		boolean retVal = addChildUIComponentsBasic();
		foundProperties = retVal;
		// will be done by childUIComponentsAdded
		// getWidgetAdapter().childComponentsAdded(retVal);
		return retVal;

	}

	public boolean childUIComponentsAdded() {
		if (getWidgetAdapter() == null)
			return false;
		getWidgetAdapter().childComponentsAdded(foundProperties);
		return foundProperties;
	}

	public boolean childComponentsAdded() {
		if (getWidgetAdapter() == null)
			return false;
		getWidgetAdapter().childComponentsAdded(foundProperties);
		return foundProperties;

	}

	public Object getExpansionObject() {
		Object retVal = super.getExpansionObject();
		if (retVal != null)
			return retVal;

		return getRecordStructure().getExpansionObject();

	}

	public void cleanUpForReuse() {
		super.cleanUpForReuse();
		staticUIComponentsAdded = false;
		removeChildrenInTables();
	}
	
	public void removeChildrenWidgets() {
		// super.cleanUpForReuse();

		if (!childrenCreated)
			return;
		if (!staticUIComponentsAdded)
			return;
		if (getWidgetAdapter() == null) {
			redoAncestorDisplay();
			return;
		}
		staticUIComponentsAdded = false;
		recordStructure = getRecordStructure();
		Vector componentNames = recordStructure.componentNames();

		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);
			if (a != null) {
				a.cleanUpForReuse();
			}

		}

	}

	public void removeChildrenWidgetsAtThisLevel() {
		// super.cleanUpForReuse();

		if (!childrenCreated)
			return;
		if (!staticUIComponentsAdded)
			return;
		if (getWidgetAdapter() == null) {
			redoAncestorDisplay();
			return;
		}
		staticUIComponentsAdded = false;
		recordStructure = getRecordStructure();
		Vector componentNames = recordStructure.componentNames();

		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);
			if (a != null) {
				getWidgetAdapter().remove(a);
			}

		}

	}

	public void removeNewlyInvisibleChildren(
			List<ObjectAdapter> childrenWhoseVisibilityChanged) {
		// super.cleanUpForReuse();

		if (!childrenCreated)
			return;
		if (!staticUIComponentsAdded)
			return;
		
		// should this go below
		if (getWidgetAdapter() == null) {
			redoAncestorDisplay();
//			return;    
		}

		for (int i = 0; i < childrenWhoseVisibilityChanged.size(); i++) {
			ObjectAdapter a = childrenWhoseVisibilityChanged.get(i);
			// if (a != null && !a.isVisible()) {
			if (getWidgetAdapter() != null)
			getWidgetAdapter().remove(a);
			// }
			if (a instanceof CompositeAdapter) {
				((CompositeAdapter) a).removeGraphicalDescendents();
			}

		}

	}

	public void addNewlyVisibleGraphicalChildren(
			List<ObjectAdapter> childrenWhoseVisibilityChanged) {
		// super.cleanUpForReuse();

		if (!childrenCreated)
			return;
		if (!staticUIComponentsAdded)
			return;
		if (getWidgetAdapter() == null) {
			return;
		}

		for (int i = 0; i < childrenWhoseVisibilityChanged.size(); i++) {
			ObjectAdapter a = childrenWhoseVisibilityChanged.get(i);

			if (a instanceof CompositeAdapter) {
				((CompositeAdapter) a).addGraphicalDescendents();
			}

		}

	}

	public void addNewlyVisibleChildren(
			List<ObjectAdapter> childrenWhoseVisibilityChanged) {
		// super.cleanUpForReuse();

		if (!childrenCreated)
			return;
		if (!staticUIComponentsAdded)
			return;
		if (getWidgetAdapter() == null) {
			redoAncestorDisplay();
			return;
		}

		for (int i = 0; i < childrenWhoseVisibilityChanged.size(); i++) {
			ObjectAdapter a = childrenWhoseVisibilityChanged.get(i);
			// if (a != null && !a.isVisible()) {
			getWidgetAdapter().add(a);
			// }

		}
		getWidgetAdapter().childComponentsAdded(true);

	}

	boolean staticUIComponentsAdded = false;
	int numDisplayedStaticChildren = 0;

	public boolean getStaticUIComponentsAdded() {
		return staticUIComponentsAdded;
	}

	public void maybeAddStaticChildren() {

	}

	public int getNumberOfDisplayedStaticChildren() {
		/*
		 * if (!getStaticUIComponentsAdded()) addStaticChildUIComponentsBasic();
		 */
		return numDisplayedStaticChildren;
	}

	public boolean addChildUIComponentsBasic() {
		return addStaticChildUIComponentsBasic();
	}

	public boolean addStaticChildUIComponentsBasic() {

		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return false;
		if (staticUIComponentsAdded)
			return false;
		staticUIComponentsAdded = true;
		if (getWidgetAdapter() != null) // happens when no main panel and frame is dummy{
		getWidgetAdapter().childComponentsAdditionStarted();

		if (isLeafOfAtomic())
			return false;
		// if (getRealObject() == null) return true;
		if (getRealObject() == null)
			return false;
		// why was this commented away?
		numDisplayedStaticChildren = 0;

		if (getWidgetAdapter() != null && !(this.getWidgetAdapter().uiIsContainer()))
			return false;
		ClassAdapter adaptor = this;
		/* RecordStructure */recordStructure = getRecordStructure();
		Vector componentNames = recordStructure.componentNames();
		boolean retVal = false;
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);

			if (a == null || !a.isVisible())
				continue;
			retVal = addVisibleChild(a) | retVal;

			// a.setIndex(numDisplayedStaticChildren);
			// numDisplayedStaticChildren++;
			// retVal = a.processPreferredWidget() | retVal;
		}

		return retVal;

	}

	public boolean addVisibleChild(ObjectAdapter a) {
		a.setIndex(numDisplayedStaticChildren);
		numDisplayedStaticChildren++;
		return a.processPreferredWidget();
	}

	public void addWidget(ObjectAdapter childAdapter) {
		if (getWidgetAdapter() == null)
			return;
		VirtualComponent comp = childAdapter.getUIComponent();
		if (childAdapter.getGenericWidget() != null) {
			childAdapter.getGenericWidget().processAttributes();
			comp = childAdapter.getGenericWidget().getContainer();
		}
		getWidgetAdapter().add((VirtualContainer) getUIComponent(), comp,
				childAdapter);

	}

	public void removeWidget(ObjectAdapter childAdapter) {
		if (getWidgetAdapter() == null)
			return;

		getWidgetAdapter().remove(childAdapter);

	}

	public void sortChildWidgets() {

		if (getWidgetAdapter() == null)
			return;
		// getWidgetAdapter().removeAll();
		// this will clear all
		getWidgetAdapter().childComponentsAdditionStarted();
		for (int i = 0; i < getChildAdapterCount(); i++) {
			ObjectAdapter childAdapter = getChildAdapterAt(i);
			addWidget(childAdapter);
			/*
			 * VirtualComponent comp = childAdapter.getUIComponent(); if
			 * (childAdapter.getGenericWidget() != null) {
			 * childAdapter.getGenericWidget().processAttributes(); comp =
			 * childAdapter.getGenericWidget().getContainer(); }
			 * getWidgetAdapter().add((VirtualContainer) getUIComponent(), comp
			 * , childAdapter);
			 */

		}
		getWidgetAdapter().childComponentsAdded(true);
		getUIFrame().validate();
		// getWidgetAdapter().getUIComponent().validate();
		// getWidgetAdapter().childComponentsAdded(numDisplayedStaticChildren >
		// 0);
	}

	public boolean addChildUIComponents(Hashtable ignorePs) {
		// if (!childrenCreated) return true;
		if (!childrenCreated)
			return false;
		// if (isAtomic()) return true;
		// if (isLeafOfAtomic()) return true;
		if (isLeafOfAtomic())
			return false;
		// if (getRealObject() == null) return true;
		if (getRealObject() == null)
			return false;
		CompositeAdapter adaptor = this;
		/* RecordStructure */recordStructure = getRecordStructure();
		Vector componentNames = recordStructure.componentNames();
		boolean retVal = false;
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			if (ignorePs.get(componentName) == null) {
				ObjectAdapter a = adaptor.getChildAdapterMapping(componentName);
				retVal = retVal || a.processPreferredWidget();
			}
		}
		// if (retVal)
		getWidgetAdapter().childComponentsAdded(retVal);
		return retVal;

	}

	public void processSynthesizedAttributesWithDefaults() {
		// super.processSynthesizedAttributesWithDefaults();
		if (!childrenCreated)
			return;
		if (getRealObject() == null)
			return;
		// if (isLeafOfAtomic()) return;
		if (isAtomic())
			return;
		CompositeAdapter adaptor = this;
		/* RecordStructure */recordStructure = getRecordStructure();
		Vector componentNames = recordStructure.componentNames();
		foundUnlabeledComposite = false;
		int maxLabelLength = 0;
		for (int i = 0; i < componentNames.size(); i++) {
			String componentName = (String) componentNames.elementAt(i);
			ObjectAdapter a = getStaticChildAdapterMapping(componentName);

			// if (a == null) continue;
			// why should generic widget be created?
			// if ((a == null) || a.getWidgetAdapter() == null ||
			// a.getGenericWidget() == null) continue;
			if ((a == null) || a.getWidgetAdapter() == null)
				continue;
			// this may create problems as it was commented out, but let us see
			maxLabelLength = Math.max(maxLabelLength, getLabel().length());
			/*
			 * if (a.getWidgetAdapter() == null || a.getGenericWidget() == null)
			 * continue; maxLabelLength = Math.max(maxLabelLength,
			 * a.getGenericWidget().getLabel().length());
			 */
			// dont know why we dont use this label
			// maxLabelLength = Math.max(maxLabelLength, ((String)
			// a.getTempAttributeValue (AttributeNames.LABEL)).length());

			// if (recordStructure.isReadOnly(componentName)) {
			// if (a.isReadOnly()) {
			if (a.isUnEditable()) {

				// if (a.isLeafAdapter()) {
				if (a.isAtomic()) {
					// ((uiPrimitiveAdapter)
					// a).getWidgetAdapter().setUIComponentUneditable();
					a.getWidgetAdapter().setUIComponentUneditable();
				}
				if (a.getGenericWidget() == null)
					continue;
				// System.out.println("adapter" + a + "writemethod set");
				if (a.getUIComponent() == null) {

					String label = (String) a
							.getMergedAttributeValue(AttributeNames.LABEL);
					// label = getGenericWidget().getLabel();
					// ((Container)
					// getUIComponent()).remove(a.getGenericWidget());
					getWidgetAdapter().remove(
							(VirtualContainer) getUIComponent(),
							a.getGenericWidget().getContainer(), a);
					// ((Container)
					// getUIComponent()).remove(a.getGenericWidget().getContainer());
					// a.setTempAttributeValue(AttributeNames.LABEL, label);
					// a.getGenericWidget().setLabel(label);
					// a.setTempAttributeValue(AttributeNames.LABEL,label );
					// Attribute a = new Attribute(AttributeNames.LABEL,label);
					a.getWidgetAdapter().processAttribute(
							new Attribute("class." + AttributeNames.LABEL,
									label));

					continue;
				}

				// Check if the property is editable
				// and set the editable attribute to false if
				// it isnt.
				// System.out.println ("adapter" + a + " " +
				// a.getAttributeValue(AttributeNames.LABEL));
				// System.out.println(a + a.getGenericWidget().getLabel());
				// maxLabelLength = Math.max(maxLabelLength, ((String)
				// a.getAttributeValue(AttributeNames.LABEL)).length());
				// maxLabelLength = Math.max(maxLabelLength,
				// a.getGenericWidget().getLabel().length());
				// System.out.println(maxLabelLength + " "
				// +a.getGenericWidget().getLabel().length() );
				if (!(a instanceof PrimitiveAdapter)) {
					ClassDescriptorInterface childDesc = ClassDescriptorCache
							.getClassDescriptor(a.getPropertyClass());
					// System.out.println ("non primitive adapter");
					// System.out.println(childDesc);
					// System.out.println(childDesc.getBeanDescriptor().getValue("direction"));
					if (childDesc.getBeanDescriptor() != null
							&& !a.getGenericWidget().isLabelVisible() &&
							// !"horizontal".equals(childDesc.getBeanDescriptor().getValue("direction")))
							!"horizontal".equals(a
									.getMergedAttributeValue("direction")))
						foundUnlabeledComposite = true;
					// System.out.println ("found composite");
				}
				/*
				 * if (!(a instanceof uiPrimitiveAdapter)) {
				 * foundUnlabeledComposite = true; }
				 */
				/*
				 * if (property.getWriteMethod() == null) {
				 * //System.out.println("no writemethod for" +
				 * a.getBeautifiedPath()); //if (a instanceof
				 * uiPrimitiveAdapter) { if (a.isLeaf()) {
				 * //((uiPrimitiveAdapter)
				 * a).getWidgetAdapter().setUIComponentUneditable();
				 * a.getWidgetAdapter().setUIComponentUneditable(); }
				 */
			}
			super.processSynthesizedAttributesWithDefaults(); // set widget
																// shell
																// attributes at
																// this pt
		}
		if (checkIfNoVisibleChildren(this)) {
			// this.getUIFrame().maybeHideMainPanel();
			return;
		}
		// if (reparentChild(this)) {
		// if (childrenVector.size() > 0 && reparentChild()) {
		
		// ugly ugly, reparentChild has side effects
//		if (childrenVector.size() > 0 && reparentChild()) {
		if (onlyChild != null && reparentChild()) {

			// as we are not really re
			ObjectAdapter a = (ObjectAdapter) childrenVector.elementAt(0);
			this.setSkippedAdapter();
//			a.setIsOnlyChild(true);
			a.propagateAttributesToWidgetShell();
			adaptor.propagateAttributesToWidgetShell();
			
			return;
		}
		if (this instanceof VectorAdapter || this instanceof HashtableAdapter)
			return;
		// processDirection();
	}

	public boolean hasNoProperties() {
		return getRecordStructure().componentNames().size() == 0;
//		return false;
		// return childrenCreated() && !isAtomic() &&
		// !getChildren().hasMoreElements();

	}

	/*
	 * String getUserObject() { RecordStructure recordStructure =
	 * getRecordStructure(); if (recordStructure == null) return null; if
	 * (!recordStructure.hasUserObject()) return null; Object userObject =
	 * recordStructure.getUserObject(); if (userObject == null) return null;
	 * return userObject.toString(); }
	 */
	/*
	 * public String getComputedLabelWithoutSuffix() { RecordStructure
	 * recordStructure = getRecordStructure(); if (recordStructure == null)
	 * return super.getComputedLabelWithoutSuffix(); if
	 * (!recordStructure.hasUserObject()) return
	 * super.getComputedLabelWithoutSuffix(); Object userObject =
	 * recordStructure.getUserObject(); if (userObject == null) return
	 * super.getComputedLabelWithoutSuffix(); if (!(userObject instanceof
	 * String)) return super.getComputedLabelWithoutSuffix(); return (String)
	 * userObject; }
	 */

	// FeatureDescriptor features[];
	// boolean foundUnlabeledComposite = false;
	// public boolean foundUnlabeledComposite() {
	// return foundUnlabeledComposite;
	// }
	/*
	 * public void setDirection() { String direction = (String)
	 * getMergedAttributeValue(AttributeNames.DIRECTION); if ( direction ==
	 * null) { //direction = getDefaultDirection(); this.setTempAttributeValue
	 * (AttributeNames.DIRECTION, getDefaultDirection()); } }
	 */
	public void processDirection() {
		if (numFeatures == 0)
			return;
		// String direction = (String)
		// getTempAttributeValue(AttributeNames.DIRECTION);
		String direction = getDirection();
		boolean alignHorizontal = direction.equals(AttributeNames.HORIZONTAL);
		boolean square = direction.equals("square");
		boolean box = direction.equals(AttributeNames.BOX);
		if (getWidgetAdapter() == null)
			return;
		// Container containW = (Container) getWidgetAdapter().getUIComponent();
		if (getWidgetAdapter() instanceof CommandAndStatePanelAdapter) {
			// getWidgetAdapter().processDirection();
			((CompositeAdapter) this.parent).makeColumnTitles();
			return;
		}
		if (getWidgetAdapter() instanceof TabbedPaneAdapter) {
			// getWidgetAdapter().processDirection();
			return;
		}
		if (getWidgetAdapter().getUIComponent() == null)
			return;
		getWidgetAdapter().processDirection();

		// rest of the stuff is for the old Panel adapter, - let us just get rid
		// of it
		/*
		 * //if (containW == null || containW instanceof JTabbedPane) return; if
		 * (alignHorizontal) { Vector attributes = new Vector();
		 * 
		 * processDirection("horizontal"); //System.out.println("horizontal " +
		 * adaptor.getMergedAttributeValue("direction")); }
		 * 
		 * if (alignHorizontal)
		 * //"horizontal".equals(adaptor.getMergedAttributeValue("direction")))
		 * /
		 * /"horizontal".equals(cdesc.getBeanDescriptor().getValue("direction"))
		 * ) //containW.setLayout(new uiGridLayout(1, features.length));
		 * //containW.setLayout(new uiGridLayout(1, features.length,
		 * uiGridLayout.DEFAULT_HGAP, 0)); containW.setLayout(new
		 * uiGridLayout(1, numFeatures, uiGridLayout.DEFAULT_HGAP, 0)); else
		 * //fisayo changed the below to make the ui more presentable....support
		 * the square value for the direction if (square) { //int feature_count
		 * = features.length; //containW.setLayout(new
		 * uiGridLayout((int)Math.ceil(Math.sqrt(feature_count)),
		 * (int)Math.ceil(Math.sqrt(feature_count)))); containW.setLayout(new
		 * uiGridLayout((int)Math.ceil(Math.sqrt(numFeatures)),
		 * (int)Math.ceil(Math.sqrt(numFeatures))));
		 * 
		 * }
		 * 
		 * else //fisayo changed the below to make the ui more
		 * presentable....support the square value for the direction if (box) {
		 * //containW.setLayout(new
		 * uiGridLayout((int)Math.ceil(features.length/2),
		 * (int)Math.ceil(features.length/2))); containW.setLayout(new
		 * uiGridLayout((int)Math.ceil(numFeatures/2),
		 * (int)Math.ceil(numFeatures/2)));
		 * 
		 * }
		 * 
		 * 
		 * else
		 * 
		 * //containW.setLayout(new uiGridLayout(features.length, 1)); //if
		 * (foundUnlabeledComposite && features.length > 1) if
		 * (foundUnlabeledComposite && numFeatures > 1) //containW.setLayout(new
		 * uiGridLayout(features.length, 1, 0, uiGridLayout.DEFAULT_VGAP));
		 * containW.setLayout(new uiGridLayout(numFeatures, 1, 0,
		 * uiGridLayout.DEFAULT_VGAP)); else //containW.setLayout(new
		 * uiGridLayout(features.length, 1)); containW.setLayout(new
		 * uiGridLayout(numFeatures, 1)); ((uiContainerAdapter)
		 * this.parent).makeColumnTitles();
		 * 
		 * //getWidgetAdapter().childComponentsAdded();
		 */
	}

	/*
	 * public uiObjectAdapter createAdapter (Container containW, Object obj,
	 * Object obj1, Object parentObject, String name, Class inputClass, boolean
	 * propertyFlag, uiObjectAdapter adaptor, boolean textMode) { return
	 * createClassAdapter (containW, obj, obj1, parentObject, name, inputClass,
	 * propertyFlag, adaptor, textMode); }
	 */
	/*
	 * public static uiClassAdapter createClassAdapterOld (Container containW,
	 * Object obj, Object obj1, Object parentObject, String name, Class
	 * inputClass, boolean propertyFlag, uiObjectAdapter adaptor, boolean
	 * textMode) { Class componentClass; uiClassAdapter classAdapter = new
	 * uiClassAdapter(); setAdapterAttributes(classAdapter, obj, parentObject,
	 * name); classAdapter.setPropertyClass(inputClass);
	 * classAdapter.setPropertyName(name); if (propertyFlag) {
	 * classAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	 * linkPropertyToAdapter(parentObject, name, classAdapter); }
	 * 
	 * classAdapter.setRealObject(obj1);
	 * 
	 * classAdapter.setViewObject(obj);
	 * 
	 * classAdapter.setParentAdapter((uiContainerAdapter) adaptor);
	 * classAdapter.setUIFrame(adaptor.getUIFrame());
	 * //System.out.println("processing attributes of adaptor for" + obj1);
	 * classAdapter.processAttributeList();
	 * 
	 * //System.out.println("finished adding attributes of adaptor for" + obj1);
	 * if (classAdapter.getUIComponent() == null) {
	 * 
	 * String label = (String)
	 * classAdapter.getMergedAttributeValue(AttributeNames.LABEL); //label =
	 * getGenericWidget().getLabel();
	 * containW.remove(classAdapter.getGenericWidget());
	 * //a.setTempAttributeValue(AttributeNames.LABEL, label);
	 * //a.getGenericWidget().setLabel(label);
	 * //a.setTempAttributeValue(AttributeNames.LABEL,label ); //Attribute a =
	 * new Attribute(AttributeNames.LABEL,label);
	 * classAdapter.getWidgetAdapter().processAttribute(new Attribute("class." +
	 * AttributeNames.LABEL,label)); //continue; } //addToDrawing(classAdapter,
	 * obj); //classAdapter.uiAddClassComponents();
	 * //classAdapter.createChildren();
	 * 
	 * // If a custom editor has been registered // we do not need to go further
	 * 
	 * 
	 * 
	 * return classAdapter; }
	 */
	// the overridden one in uiObjectAdpater should not work
	@Override
	public ObjectAdapter getEditedObjectAdapter(String changedPropertyName) {
		Vector<ObjectAdapter> children = getChildrenVector();
		// uiObjectAdapter retVal = null;
		for (ObjectAdapter child : children) {
			if (child.getAdapterType() == PROPERTY_TYPE
					&& child.getPropertyName().equals(changedPropertyName))
				return child;
		}

		if (getWidgetAdapter() != null
		/*
		 * && (changedPropertyName == null ||
		 * changedPropertyName.equals(this.getPropertyName()))
		 */)
			return this;
		if (isTopAdapter())
			return null;
		return getVirtualParent().getEditedObjectAdapter(changedPropertyName);
	}
	
    void handleSuppressedNotification(PropertyChangeEvent evt) { // cache this value in child adapter
    	ObjectAdapter childAdapter = getAdapterForNotifiedProperty( evt);
    	if (childAdapter == null) return;
    	childAdapter.setPendingFutureRealObject(evt.getNewValue()); // does it make sense to store cache this changes
    	
		
	}
    
    ObjectAdapter getAdapterForNotifiedProperty(PropertyChangeEvent evt) {
    	String changedPrpertyName = evt.getPropertyName();
    	ObjectAdapter childAdapter = getVisibleOrDeletedObjectAdapter(changedPrpertyName);
		if (childAdapter != null && childAdapter instanceof CompositeAdapter) {
			Tracer.warning("Received notification about change to composite property: " + changedPrpertyName  +" .It is usually more efficient to notify about changes to atomic properties." );
		}

		if (childAdapter == null) {
			if (changedPrpertyName.equals("this"))
				childAdapter = this;
			else {
				if (evt.getSource() != getViewObject())
					IllegalSourceOfPropertyNotification.newCase(evt, getViewObject(), this);
				else {
				UnknownPropertyNotification.newCase(changedPrpertyName, evt.getSource(), this);
//				return null;
//				childAdapter = this;
				}
//				Tracer.warning("Received notification for unknown property: "
//						+ changedPrpertyName + " of object " + evt.getSource() + ". Updating complete object.");
//				childAdapter = this;
//				return;
			}
		}
		return childAdapter;
    	
    }

	// the one in uiObjectAdapter does not work as far as I can tell
	@Override
	public void subPropertyChange(PropertyChangeEvent evt) {

		// haveReceivedNotification();
//		System.out.println(getRealObject().toString());
//		Tracer.info("ObjectAdapter: " + getPath() +
//				" created for object:"	+ objectID() + 
//				" received new value " + evt.getNewValue()
//				+ " of property " + evt.getPropertyName() + " of source "
//				+ evt.getSource());
		if (!childrenCreated)
			return;
		ClassAdapterReceivedPropertyChangeEvent.newCase((ClassAdapter) this, evt) ;

		String changedPrpertyName = evt.getPropertyName();
		// ObjectAdapter childAdapter =
		// getStaticChildAdapterMapping(changedPrpertyName);
		ObjectAdapter childAdapter = getAdapterForNotifiedProperty(evt);
		if (childAdapter == null) return;
//		ObjectAdapter childAdapter = getVisibleOrDeletedObjectAdapter(changedPrpertyName);
//		if (childAdapter != null && childAdapter instanceof CompositeAdapter) {
//			Tracer.warning("Received notification about change to composite property: " + changedPrpertyName  +" .It is usually more efficient to notify about changes to atomic properties." );
//		}
//
//		if (childAdapter == null) {
//			if (changedPrpertyName.equals("this"))
//				childAdapter = this;
//			else {
//				if (evt.getSource() != getViewObject())
//					IllegalSourceOfPropertyNotification.newCase(evt, getViewObject(), this);
//				else
//				UnknownPropertyNotification.newCase(changedPrpertyName, evt.getSource(), this);
////				Tracer.warning("Received notification for unknown property: "
////						+ changedPrpertyName + " of object " + evt.getSource() + ". Updating complete object.");
//				childAdapter = this;
////				return;
//			}
//		}
		haveReceivedNotification();
		ObjectAdapter nearestAdapter = getNearestObjectAdapterWithWidgetAdapter();
		if (nearestAdapter == null) {
			nearestAdapter = this;
		}

		Object newValue = evt.getNewValue();
		getUIFrame().getDrawing().setLocked(true);
		// changing this to true to force update. New value has been received, so the property components must have changed.
		boolean classChanged = refresh(newValue, changedPrpertyName, true);

//		boolean classChanged = refresh(newValue, changedPrpertyName, false);
		getUIFrame().getDrawing().setLocked(false);

		Object oldValue = evt.getOldValue();
		// wjy are we refreshing when we receive event?
		// oeal22 am commenting this out
		Tracer.info(this, "Getting rid of implicit refresh of nearest adapter in proeprty change event for efficiency reasons");
		if (childAdapter == nearestAdapter) {
			// the adapter changed with property name "this"

			implicitRefresh(true);
			
		} else
		if (classChanged) {
		if (nearestAdapter == this)
			implicitRefresh(true);
		else {
			// if (childAdapter == null)
			// childAdapter = this;
			((CompositeAdapter) nearestAdapter)
					.setChangedChildInAtomicWidget(childAdapter);
			nearestAdapter.implicitRefresh(true);
			((CompositeAdapter) nearestAdapter)
					.resetChangedChildInAtomicWidget();
		}
		}
		if (isTreeNode()) {
		getUIFrame().refreshTreeIfVisible();
		getUIFrame().expandAllTreeNodes();
		}

		getUIFrame().validate();
		getUIFrame().repaint();
	}

	@Override
	public void mousePressed(List<RemoteShape> theShapes,
			RemoteShape theSmallestShape, MouseEvent mouseEvent,
			Point aClickPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(List<RemoteShape> theShapes,
			RemoteShape theSmallestShape, MouseEvent mouseEvent,
			Point aClickPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(List<RemoteShape> theShapes,
			RemoteShape theSmallestShape, MouseEvent mouseEvent,
			Point aClickPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(List<RemoteShape> theShapes,
			RemoteShape theSmallestShape, MouseEvent mouseEvent,
			Point aClickPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(List<RemoteShape> theShapes,
			RemoteShape theSmallestShape, MouseEvent mouseEvent,
			Point aClickPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDebugInfo() {
		return getPatternName();
	}

	public String getPatternName() {
		if (isNoPattern()) {
			return util.annotations.StructurePatternNames.NO_PATTERN.toString();
		} else if (recordStructure != null)
			return recordStructure.getPatternName();
		else
			// this path taken even when it is not null
			return null;
	}
	public  Object getComponentValue(String componentName) {
		return recordStructure.get(componentName);
	}
	
	
	// map methods
	Map<Object, Object> mapDelegate() {
		if (mapDelegate == null) {
			mapDelegate = new HashMap();
			for (String key:mapping.keySet()) {
				mapDelegate.put(key, objectOrAdapter(mapping.get(key)));
			}
		}
		return mapDelegate;
	}
	
	 Object instantiateCanonicalForm() {
		return new ACanonicalBean(this);
	}
	
	public Object toCanonicalForm() {
		if (canonicalBean == null) {
			canonicalBean = (Map) instantiateCanonicalForm();
			for (String key:mapping.keySet()) {
				canonicalBean.put(key, mapping.get(key).toCanonicalForm());
			}
		}
		return canonicalBean;	
	}
	
	

	public int size() {
		return mapDelegate().size();
	}

	public boolean isEmpty() {
		return mapDelegate().isEmpty();
	}

	public boolean containsKey(Object key) {
		return mapDelegate().containsKey(key);
	}

	public boolean containsValue(Object value) {
		return mapDelegate().containsValue(value);
	}

	public Object get(Object key) {
		return mapDelegate().get(key);
	}

	public Object put(String key, Object value) {
		recordStructure.set(key, value);
		mapDelegate = null;
		return mapDelegate.get(key);
//		return mapDelegate().put(key, value);
	}
	@Override
	public Object put(Object key, Object value) {
		return put(key, value);

	}

	public Object remove(Object key) {
		return null;
//		return mapDelegate().remove(key);
	}

	public void putAll(Map m) {
		mapDelegate().putAll(m);
	}

	public void clear() {
		mapDelegate().clear();
	}

	public Set<Object> keySet() {
		return mapDelegate().keySet();
	}

	public Collection<Object> values() {
		return mapDelegate().values();
	}

	public Set<Entry<Object, Object>> entrySet() {
		return mapDelegate().entrySet();
	}

	
	

	
	
	
	

}
