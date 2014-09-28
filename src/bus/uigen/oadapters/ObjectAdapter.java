package bus.uigen.oadapters;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.FocusEvent;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import shapes.RemoteShape;
import slm.ShapesList;
import util.models.HashcodeSet;
import util.models.Refresher;
import util.models.RemotePropertyChangeListener;
import util.trace.Tracer;
import bus.uigen.ComponentDictionary;
import bus.uigen.Connector;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.ReplaceableChildren;
import bus.uigen.UnivPropertyChange;
import bus.uigen.ValueChangedEvent;
import bus.uigen.WidgetAdapterInterface;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.attributes.CopyAttributeVector;
import bus.uigen.attributes.LocalAttributeDescriptor;
import bus.uigen.attributes.UIAttributeManager;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionManager;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.editors.EditorRegistry;
import bus.uigen.editors.NestedShapesAdapter;
import bus.uigen.editors.TreeAdapter;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.Attribute.DefinitionKind;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.loggable.AnIdentifiableLoggable;
import bus.uigen.loggable.IdentifiableLoggable;
import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.misc.OEMisc;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.FieldProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.EnumToEnumeration;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.trace.LogicalStructureNodeCreated;
import bus.uigen.trace.UnknownPropertyNotification;
import bus.uigen.view.WidgetShell;
import bus.uigen.viewgroups.OEView;
import bus.uigen.visitors.AddListenersAdapterVisitor;
import bus.uigen.visitors.CleanUpAdapterVisitor;
import bus.uigen.visitors.CleanUpForReuseAdapterVisitor;
import bus.uigen.visitors.RecomputeAttributesAdapterVisitor;
import bus.uigen.visitors.ToTextLineAdapterVisitor;
import bus.uigen.visitors.UnmapAdapterVisitor;
import bus.uigen.widgets.ContainerFactory;
import bus.uigen.widgets.LayoutManagerFactory;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;

public abstract class ObjectAdapter /*extends UnicastRemoteObject*/ implements
ObjectAdapterInterface, Remote, Serializable
//		Observer, Refresher, PropertyChangeListener, RemotePropertyChangeListener,
//		TableModelListener, TreeModelListener, ComponentValueChangedListener,
//		AttributeCollection, LocalAttributeListener, Selectable,
//		MutableTreeNode, /* ActionListener, MouseListener, */CommandListener,
//		ModelAdapter, java.io.Serializable, Remote 
		
		/*
													 * ,
													 * ObjectValueChangedListener
													 */
   {
	public static final String USER_OBJECT_NAME = "userObject";
	// Adapter types
	public static final int PROPERTY_TYPE = 1;
	public static final int PRIMITIVE_TYPE = 2;
	public static final int LISTENABLE_TYPE = 3;
	public static final int KEY_TYPE = 4;
	public static final int VALUE_TYPE = 5;
	public static final int INDEX_TYPE = 6;
	public static final int USER_OBJECT_TYPE = 7;
	
	public static final String UP_PATTERN = AttributeNames.PARENT_REFERENCE + AttributeNames.PATH_SEPARATOR;
	transient protected CompositeAdapter parent = null;
	transient protected String propertyName;
	transient protected String displayedPropertyName;
	transient protected ClassProxy propertyClass = null;
	// transient Method propertyReadMethod = null, propertyWriteMethod = null;
	// transient Method preReadMethod = null, preWriteMethod = null;
	transient private int adaptorType;
	// transient private Field adaptorField;
	transient private WidgetAdapterInterface widgetAdapter = null;
	transient private Object realObject;
	transient private Object viewObject;
	transient private boolean preRead = true;
	transient private boolean preWrite = true;
	transient ObjectAdapter sourceAdapter;
	transient uiFrame uiFrame;
	boolean isFlatTable = false;

	boolean replicasCoupled = false;
	boolean registeredAsListener;
	//moved from shape adapter because we  need children of text mode parents to be text mode
	//boolean textMode = false;
	
	List<AttributeDependency> attributeDependencies = new ArrayList();
	HashcodeSet objectsInPathToRoot;
	boolean hasTreeLogicalStructure = true;



	public boolean isString() {
		return getPropertyClass() == StandardProxyTypes.stringClass();
	}

	public boolean getTextMode() {
		//return textMode;
		return true;
	}
//	public void setTextMode (boolean newVal) {
//		textMode = newVal;
//	}

	public boolean hasFlatTableRowDescendent() {
		return (Boolean) getTempAttributeValue(AttributeNames.HAS_FLAT_TABLE_ROW_DESCENDENT);
	}
	
	

	public String getString() {
		return "";
	}

	public uiFrame getUIFrame() {
		return uiFrame;
	}

	public void setUIFrame(uiFrame newVal) {
		uiFrame = newVal;
	}

	public boolean isKeyAdapter() {
		return getAdapterType() == KEY_TYPE;
	}

	public boolean isValueAdapter() {
		return getAdapterType() == VALUE_TYPE;
	}

	transient Object key;
	transient ObjectAdapter keyAdapter;

	public Object getKey() {
		// System.out.println("getKey + ID " + this.getID() + " " + key);
		return key;
	}

	public boolean isNameKey() {
		if (!useNameAsLabel())
			return false;
		Object val = getValue();
		return (isKeyAdapter() && (val instanceof String) &&
		// (((String) val).toLowerCase().equals("name")));
		// (((String) val).toLowerCase().equals(AttributeNames.NAME_PROPERTY)));
		(((String) val).toLowerCase().equals(getNameProperty())));
	}

	public String getNameProperty() {
		String retVal = (String) getMergedTempOrDefaultAttributeValue(AttributeNames.NAME_PROPERTY);
		if (retVal == null)
			return AttributeNames.KEY_LABEL;
		else
			return retVal.toLowerCase();

	}
	
	public Boolean useNameAsLabel() {
		if (getParentAdapter() == null) return false;
		Boolean retVal = (Boolean) getParentAdapter().getMergedTempOrDefaultAttributeValue(AttributeNames.USE_NAME_AS_LABEL);
		return retVal;

	}

	public boolean getShowReadOnlyNameChildAsLabel() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_READONLY_NAME_CHILD_AS_LABEL);
		if (retVal == null)
			retVal = true;
		return retVal;

	}

	public ObjectAdapter getKeyAdapter() {
		// System.out.println("getKey + ID " + this.getID() + " " + key);
		return keyAdapter;
	}

	public void setKey(Object newVal) {
		// System.out.println("setKey + ID " + this.getID() + " " + newVal);
		key = newVal;
	}

	public void setKeyAdapter(ObjectAdapter newVal) {
		// System.out.println("setKey + ID " + this.getID() + " " + newVal);
		if (keyAdapter == newVal)
			return;
		keyAdapter = newVal;
		if (keyAdapter == null)
			return;
		if ((keyAdapter != null) && keyAdapter.isNameKey()) {
			((CompositeAdapter) getParentAdapter())
					.nameChildChanged(getValue().toString());
		}

	}

	transient int index = -1;

	public int getIndex() {
		return index;
	}
	public int getTreeIndex() {
		if (isTopDisplayedAdapter()) {
			return 0;
		} else {
			return getParentAdapter().getTreeIndex()*10 + (getOriginalIndex() + 1);

//			return getParentAdapter().getTreeIndex()*10 + (getIndex() + 1);
//			return getParentAdapter().getTreeIndex()*10 + getVisibleNonVisiblePosition();

		}
	}

	/*
	 * String widgetShellColumnTitle = ""; public void
	 * setWidgetShellColumnTitle(String newVal) { widgetShellColumnTitle =
	 * newVal; } public String getWidgetShellColumnTitle() { return
	 * widgetShellColumnTitle; }
	 */
	boolean showChildrenColumnTitle;
	boolean showChidrenColumnTitleSet = false;

	// public boolean getShowChildreB
	public boolean childrenShowingColumnTitle() {
		return showChidrenColumnTitleSet && showChildrenColumnTitle;
	}

	public boolean childrenHidingColumnTitle() {
		return showChidrenColumnTitleSet && !getShowChildrenColumnTitle();
	}

	public boolean getShowChildrenColumnTitle() {
		return showChildrenColumnTitle;
	}

	public void setShowChildrenColumnTitle(boolean newVal) {
		showChidrenColumnTitleSet = true;
		showChildrenColumnTitle = newVal;
	}

	// boolean showWidgetShellColumnTitle;
	ColumnTitleStatus showColumnTitle = ColumnTitleStatus.disabled;

	public enum ColumnTitleStatus {
		disabled, hide, show
	};

	public boolean showColumnTitle() {
		return getColumnTitleStatus() == ColumnTitleStatus.show;
	}

	public void setColumnTitleStatus(ColumnTitleStatus newVal) {
		showColumnTitle = newVal;
	}

	public ColumnTitleStatus getColumnTitleStatus() {
		return showColumnTitle;
	}

	public ColumnTitleStatus getWidgetShellColumnTitleStatus() {
		ObjectAdapter gp = getGrandParentAdapter();
		if (gp == null)
			return showColumnTitle;
		if (gp.getSeparateUnboundTitles()
				&& showColumnTitle == ColumnTitleStatus.show)
			return ColumnTitleStatus.hide;
		else
			return showColumnTitle;
	}

	public boolean getShowColumnTitles() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_COLUMN_TITLES);
		if (value == null)
			return true;
		return (Boolean) value;

	}

	public String getColumnTitle(ObjectAdapter a) {
		// if (this.isLabelled())
		// return (String)
		// a.getMergedOrTempAttributeValue(AttributeNames.LABEL);
		// return (String) a.getLabel();
		
		// we dont want a label if the key is not structured
//		if (a instanceof uiPrimitiveAdapter)
//			return " "; 
		if (a.isLabelled())
			return (String) a.getLabelWithoutSuffix();
		else
			return "";
		// else return "";
	}

	public CompositeAdapter getAncestorWhoseParentIs(
			CompositeAdapter parentOfAncestor) {
		CompositeAdapter parentAdapter = getParentAdapter();
		if (parentAdapter == null)
			return null;
		if (parentAdapter.getParentAdapter() == parentOfAncestor)
			return parentAdapter;
		return parentAdapter.getAncestorWhoseParentIs(parentOfAncestor);

	}

	public void sortAncestor(CompositeAdapter topAdapter) {
		ObjectAdapter leafAdapter = this;
		String fieldName = leafAdapter.getPropertyName();
		int sortIndex = leafAdapter.getRealVectorIndex();
		Object sortKey = leafAdapter.getKey();
		ObjectAdapter parentAdapter = leafAdapter.getParentAdapter();
		if (parentAdapter == null)
			return;
		boolean sortChild = parentAdapter.getParentAdapter() == topAdapter;
		CompositeAdapter ancestor = leafAdapter
				.getAncestorWhoseParentIs(topAdapter);
		/*
		 * ClassProxy ancestorClass =  IntrospectUtility.toMaybeProxyTargetClass(parentAdapter
		 * .getViewObject());
		 */
		ClassProxy ancestorClass =  ReflectUtil.toMaybeProxyTargetClass(ancestor
				.computeAndMaybeSetViewObject());
		if (ancestorClass == null)
			return;
		if (sortIndex >= 0)
			ObjectEditor.setAttribute(ancestorClass,
					AttributeNames.SORT_PROPERTY, sortIndex);
		else if (sortKey != null)
			ObjectEditor.setAttribute(ancestorClass,
					AttributeNames.SORT_PROPERTY, sortKey);
		else {
			if (sortChild) {
				ObjectEditor.setAttribute(ancestorClass,
						AttributeNames.SORT_PROPERTY, fieldName);
				topAdapter.setTempAttributeValue(AttributeNames.SORT_PROPERTY,
						fieldName);
			} else {
				Vector relativePath = getVectorPathRelativeTo(ancestor);
				ObjectEditor.setAttribute(ancestorClass,
						AttributeNames.SORT_PROPERTY, relativePath);
				topAdapter.setTempAttributeValue(AttributeNames.SORT_PROPERTY,
						relativePath);

			}
		}
		// sorting based on some element of a Hashtable value
		if (topAdapter instanceof HashtableAdapter)
			topAdapter.setLocalAttribute(AttributeNames.HASHTABLE_SORT_KEYS,
					false);
		if (topAdapter instanceof VectorAdapter)
			topAdapter.setLocalAttribute(AttributeNames.LIST_SORT_USER_OBJECT,
					false);
		// topAdapter.setTempAttributeValue(AttributeNames.LIST_SORT_USER_OBJECT,
		// false);
		UIAttributeManager environment = AttributeManager.getEnvironment();
		if (environment == null)
			return;
		environment.removeFromAttributeLists(ancestorClass.getName());

		// topAdapter.setTempAttributeValue(AttributeNames.SORT, true);

		topAdapter.setLocalAttribute(AttributeNames.SORT, true);
		// topAdapter.setTempAttributeValue(AttributeNames.SORT, true);
		// topAdapter.recomputeAttributes();

		(new RecomputeAttributesAdapterVisitor(topAdapter)).traverse();

		// ViewInfo cd = ClassDescriptorCache.getClassDescriptor(parentClass);
	}

	public String columnTitleJTable(ObjectAdapter a) {
		return getColumnTitle(a);
	}

	public String columnTitle() {
		ObjectAdapter parent = getParentAdapter();
		if (parent == null)
			return getColumnTitle(this);
		return parent.getColumnTitle(this);

	}

	public String columnTitleJTable() {
		ObjectAdapter parent = getParentAdapter();
		if (parent == null)
			return columnTitleJTable(this);
		return parent.columnTitleJTable(this);

	}
	
	int originalIndex = -1;
	public int getOriginalIndex() {
		return originalIndex;
	}

	public void setIndex(int newVal) {
		if (index == -1) {
			originalIndex = newVal;
		}
		if (index == newVal)
			return;
		index = newVal;
	}

	int vectorIndex = -1;

	public void setVectorIndex(int newVal) {
		vectorIndex = newVal;
	}

	public int getVectorIndex() {
		return vectorIndex;
	}

	int realVectorIndex = -1;

	public void setRealVectorIndex(int newVal) {
		realVectorIndex = newVal;
	}

	public int getRealVectorIndex() {
		return realVectorIndex;
	}

	transient ObjectAdapter valueAdapter;

	public ObjectAdapter getValueAdapter() {
		return valueAdapter;
	}

	public void setValueAdapter(ObjectAdapter newVal) {
		valueAdapter = newVal;
	}

	public boolean isDefinedLabelled() {
		return (Boolean) getMergedAttributeValue(AttributeNames.LABELLED);
	}

	int maxComponentNameLength = 0;
	String largestDescendentLabel = "";

	public int computedLabelLength() {
		String label = getLabel();
		boolean isLabelVisible = isLabelled();
		int labelLength = 0;
		if (isLabelVisible && label != null)
			labelLength = label.length();
		else
			labelLength = maxComponentNameLength;
		return labelLength;
	}
	
	public String largestSynthesizedLabel() {
		String label = getLabel();
		boolean isLabelVisible = isLabelled();
		if (isLabelVisible && label != null)
			return label;
		else
			label = largestDescendentLabel;
		return label;
	}
	

	public boolean isLabelled() {
		// Object isLabelled = getMergedAttributeValue(AttributeNames.LABELLED);
		//if (overrideLabelVisible() || isOnlyChild())
		
		// do not rememnber why override label visible would be true
		if (overrideLabelVisible())
			return false;
		// dunno why the following was commented and the one after that used
		Object isLabelled = getMergedAttributeValue(AttributeNames.LABELLED);
		//Object isLabelled = getNonDefaultMergedAttributeValue(AttributeNames.LABELLED);
		if (isLabelled != null)
			return ((Boolean) isLabelled);
		else if (isTopDisplayedAdapter() || isOnlyChild() || computeIsOnlyChild())
			return false;
//		else if (computeIsOnlyChild())
//			return false;	
		// no idea why the following is used, but will keep it
		else
			return (getDefinedLabelAbove() == null
					&& getDefinedLabelBelow() == null
					&& getDefinedLabelLeft() == null && getDefinedLabelRight() == null);

	}

	public boolean labelKeys() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.LABEL_KEYS);
		// return retVal;
	}

	public Object labelValues() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.LABEL_VALUES);
		// return retVal;
	}

	public boolean toStringAsLabel() {
		// Object isLabelled = getMergedAttributeValue(AttributeNames.LABELLED);
		Object retVal = getMergedTempOrDefaultAttributeValue(AttributeNames.TO_STRING_AS_LABEL);
		if (retVal == null)
			return false;
		/*
		 * if (isLabelled == null || ClassDescriptorCache.toBoolean(isLabelled))
		 * return true;
		 */
		else
			return ((Boolean) retVal).booleanValue();
	}
	public boolean userObjectAsLabel() {
		// Object isLabelled = getMergedAttributeValue(AttributeNames.LABELLED);
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.USER_OBJECT_AS_LABEL);
		return retVal;
	}
	public boolean isScrolled() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SCROLLED);
		return retVal;
	}
	
	public boolean isAutoScrolledDown() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.AUTO_SCROLLED_DOWN);
		return retVal;
	}
	
	public boolean isNestedShapesContainer() {
		// Object isLabelled = getMergedAttributeValue(AttributeNames.LABELLED);
		Object retVal = getMergedTempOrDefaultAttributeValue(AttributeNames.IS_NESTED_SHAPES_CONTAINER);
		if (retVal == null)
			return false;
		return (Boolean) retVal;
	}
	 

	public boolean unparseAsToString() {
		// Object isLabelled = getMergedAttributeValue(AttributeNames.LABELLED);
		Object retVal = getMergedTempOrDefaultAttributeValue(AttributeNames.DISPLAY_TO_STRING);
		if (retVal == null)
//			return true;
			return false;
		/*
		 * if (isLabelled == null || ClassDescriptorCache.toBoolean(isLabelled))
		 * return true;
		 */
		else
			return ((Boolean) retVal).booleanValue();
	}

	public void setSourceAdapter(ObjectAdapter newSource) {
		if (newSource.getSourceAdapter() == null)
			sourceAdapter = newSource;
		else
			sourceAdapter = newSource.getSourceAdapter();
	}

	public ObjectAdapter getSourceAdapter() {
		return sourceAdapter;
	}

	public ObjectAdapter getOriginalSourceAdapter() {
		return getOriginalSourceAdapter(this);
	}

	public static ObjectAdapter getOriginalSourceAdapter(ObjectAdapter a) {
		if (a.getSourceAdapter() == null)
			return a;
		else
			return getOriginalSourceAdapter(a.getSourceAdapter());
	}

	transient private Vector localAttributeList = new Vector();

	// transient private uiGenericWidget genericWidget = new uiGenericWidget();
	// transient private uiGenericWidget genericWidget =
	// WidgetShellSelector.createWidgetShell(this);
	transient private WidgetShell genericWidget;

	public void setWidgetShell(WidgetShell newValue) {
		// if (genericWidget == null || newValue == null)
		genericWidget = newValue;
	}

	public WidgetShell getWidgetShell() {
		return genericWidget;
	}

	public WidgetShell getGenericWidget() {
		return genericWidget;
	}
	
	public String toDebugText() {
			String retVal = getLabel();
			if (retVal == null)
				retVal = "";
			if (concreteObject == null)
				return "";
			if (getRealObject() == null) {
				return " ";
			}
			if (this instanceof PrimitiveAdapter) // all primitive adapters share the same concrete object
				return getRealObject().toString();
			retVal += " " + concreteObject.toString();
			return retVal;

	}

	public String toText() {

		if (hasCellEditor())
			return getRealObject().toString();
		else {

			// System.out.println("calling to String");
			// if (!isLabelled() && isTopAdapter()) return getFrameTitle();
			// if (!isLabelled()) return "";
			// if (!isLabelled()) return toTextLine(this);
			if (!isLabelled() /* || hasCellEditor() */)
				return toObjectString();
			// String retVal = (String)
			// this.getTempAttributeValue(AttributeNames.LABEL);
			// String retVal = (String) getLabel();
			String retVal = (String) getLabelWithoutSuffix();
			if (retVal == null)
				return getFrameTitle();
			return retVal;
		}
	}

	public String toCell() {
		// return toString();

		String retVal = getUserObject();
		if (retVal != null)
			return retVal;
		if (this.isLabelled()) {
			if (this.key instanceof String)
				return this.key.toString();
			else {
				return getTrueLabel();
			}
		} else
			return bus.uigen.editors.TableAdapter.uninitCell;
	}

	public boolean hasUserObject() {
		if (!(this instanceof ClassAdapter))
			return false;

		RecordStructure recordStructure = ((ClassAdapter) this)
				.getRecordStructure();
		if (recordStructure == null)
			return false;
		return recordStructure.hasUserObject();

	}

	public String getUserObject() {
		if (!(this instanceof ClassAdapter))
			return null;

		RecordStructure recordStructure = ((ClassAdapter) this)
				.getRecordStructure();
		if (recordStructure == null)
			return null;
		if (!recordStructure.hasUserObject())
			return null;
		Object userObject = recordStructure.getUserObject();
		if (userObject == null)
			return null;
		return userObject.toString();
	}

	static int NUM_ELIDE_ELEMENTS = 2;

	public static String toTextLine(ObjectAdapter adapter) {

		StringBuffer sb = new StringBuffer();
		Vector results = new Vector();
		// (new ToTextLineAdapterVisitor(adapter)).traverse(adapter, results, 0,
		// 0);
		// (new ToTextLineAdapterVisitor(adapter)).traverseLeafs(adapter,
		// results);
		(new ToTextLineAdapterVisitor(adapter)).visitChildren(adapter, results);
		for (int i = 0; i < results.size() && i < NUM_ELIDE_ELEMENTS; i++) {
			sb.append((String) results.elementAt(i) + " ");
		}
		if (results.size() > NUM_ELIDE_ELEMENTS)
			sb.append("...");
		return sb.toString();

		// return textRep;
	}

	public String toTextLine() {
		/*
		 * ClassProxy c = getPropertyClass(); if (c == null) // c =
		 * Object.class; StandardProxyTypes.objectClass(); return
		 * ClassDescriptor.getMethodsMenuName(c);
		 */
		// return getRealObject().toString();
		return toTextLine(this);
	}

	public static String toFullTextLine(ObjectAdapter adapter) {

		StringBuffer sb = new StringBuffer();
		Vector results = new Vector();
		// (new ToTextLineAdapterVisitor(adapter)).traverse(adapter, results, 0,
		// 0);
		// (new ToTextLineAdapterVisitor(adapter)).traverseLeafs(adapter,
		// results);
		(new ToTextLineAdapterVisitor(adapter)).visitChildren(adapter, results);
		for (int i = 0; i < results.size(); i++) {
			if (adapter.getDirection() == AttributeNames.HORIZONTAL)
			sb.append((String) results.elementAt(i) + " ");
			else
			sb.append((String) results.elementAt(i) + "\n");
		}
		return sb.toString();

		// return textRep;
	}

	public String toFullTextLine() {

		return toFullTextLine(this);
	}

	String browseLabel = "";

	public String getBrowseLabel() {
		return browseLabel;

	}

	public String browseLabel() {
		/*
		 * uiObjectAdapter keyAdapter = getKeyAdapter(); if (keyAdapter != null &&
		 * keyAdapter instanceof uiPrimitiveAdapter) return
		 * this.getBeautifiedPath();
		 * 
		 * else
		 */
		WidgetShell genericWidget = this.getGenericWidget();
		if (genericWidget != null && genericWidget.isElided())
			return getLabel();
		else
			return toFullTextLine();
	}

	public void setBrowseLabel(String newVal) {
		browseLabel = newVal;
	}

	public String toObjectString() {
		if (concreteObject == null)
			return "";
		if (getRealObject() == null) {
			return " ";
		}
		if (this instanceof PrimitiveAdapter) // all primitive adapters share the same concrete object
			return getRealObject().toString();
		String retVal = concreteObject.toString();
		if (retVal == null)
			return "";
		int atIndex = retVal.indexOf('@');
		if (atIndex != -1)
			return "";
		else
			return retVal;

	}
	
	String objectID;
	
	public String objectID() {
		if (objectID != null)
			return objectID;
		try {
			objectID = getRealObject().toString();
		//return getRealObject() + ("(component:" + toString() +")");
//		return getRealObject().toString();
		} catch (Exception e) {
			return "null";
		}
		return objectID;
	}

	public String toString() {
		/*
		 * if (hasCellEditor()) return getRealObject().toString(); else
		 */
		return toText();
	}

	public String getID() {
		// return toString() + ":" + super.toString();
		return super.toString();
	}

	Object tableModelListenable;

	void setTableModelListenable(Object newValue) {
		tableModelListenable = newValue;
	}
	
	static Map<PropertyChangeListener, HashcodeSet> propertyChangeListenerToObjects = new HashMap();
	
	public static HashcodeSet getObjects(Map<PropertyChangeListener, HashcodeSet> aMap, PropertyChangeListener aListener ) {
		HashcodeSet<Object> objects = aMap.get(aListener);
		if (objects == null) {
			objects = new HashcodeSet();
			aMap.put(aListener, objects);
		}
		return objects;		
	}
	

	public static  boolean maybeAddPropertyChangeListener(Object listenable,
			PropertyChangeListener listener) {
		if (listenable == null)
			return false;
		if (listener instanceof ObjectAdapter) {
			ObjectAdapter objectAdapter = (ObjectAdapter) listener;
			HashcodeSet objects = getObjects(propertyChangeListenerToObjects, objectAdapter);
			if (objects.contains(listenable))
				return false;
			objects.add(listenable); // adding prematurely; if recursive, causes issues				
		}
		
		
		// should this not be in structure adapter?
	
		MethodProxy addPropertyChangeListenerMethod = ClassDescriptorCache.getClassDescriptor(listenable).getAddPropertyChangeListenerMethod();
		if (addPropertyChangeListenerMethod != null) {
			if (listenable instanceof ACompositeLoggable) {
				((ACompositeLoggable) listenable).addPropertyChangeListener(listener);
				return true;
			}
			try {
				Object[] args = { listener };
				addPropertyChangeListenerMethod.invoke(listenable, args);
				if (!(listenable instanceof ACompositeLoggable))
					ObjectEditor.associateKeywordWithClassName(
							ObjectEditor.PROPERTY_LISTENER_KEYWORD,
							//  IntrospectUtility.toMaybeProxyTargetClass(listenable));
							//ACompositeLoggable.getTargetClass(listenable));
							ReflectUtil.toMaybeProxyTargetClass(listenable));
				// listener.setPropertyChangeListenable (listenable);
//				Message.info("Added ObjectEditor adapter for component " + listener + " as propertyChangeListener of "+ listenable);
				Tracer.info((ObjectAdapter) listener, "Added ObjectEditor adapter: " + ((ObjectAdapter) listener).getPath() + " as propertyChangeListener of "+ listenable);
				((ObjectAdapter) listener).setRegisteredAsListener(true);
				return true;
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out
						.println("E** Could not invoke addPropertyChangeListener on"
								+ listenable);

				return false;
				// e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean maybeAddRemotePropertyChangeListener(
			Object listenable, RemotePropertyChangeListener listener) {
		if (listenable == null)
			return false;
		if (listenable instanceof ACompositeLoggable) {			
			return false;
		}

//		MethodProxy addPropertyChangeListenerMethod = uiBean
//				.getAddRemotePropertyChangeListenerMethod(RemoteSelector
//						.getClass(listenable));
		MethodProxy addPropertyChangeListenerMethod = getClassDescriptor(listenable).getAddRemotePropertyChangeListenerMethod();
		
		if (addPropertyChangeListenerMethod != null) {
			try {
				Object[] args = { listener };
				addPropertyChangeListenerMethod.invoke(listenable, args);
				((ObjectAdapter) listener).setRegisteredAsListener(true);
				return true;
				// listener.setPropertyChangeListenable (listenable);
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out.println(e);
				System.out
						.println("E** Could not invoke addPropertyChangeListener on"
								+ listenable);
				return false;
				// e.printStackTrace();
			}
		}
		return false;
	}

	public static void maybeAddTableModelListener(Object viewObject,
			TableModelListener adapter) {
		// uiObjectAdapter adapter) {
		if (viewObject == null)
			return;

//		MethodProxy addTableModelListenerMethod = uiBean
//				.getAddTableModelListenerMethod(RemoteSelector
//						.getClass(viewObject));
		MethodProxy addTableModelListenerMethod = getClassDescriptor(viewObject).getAddTableModelListenerMethod();

		if (addTableModelListenerMethod != null) {
			try {
				Object[] args = { adapter };
				addTableModelListenerMethod.invoke(viewObject, args);
				((ObjectAdapter) adapter).setRegisteredAsListener(true);
				if (adapter instanceof ObjectAdapter)
					((ObjectAdapter) adapter)
							.setTableModelListenable(viewObject);
				ObjectEditor.associateKeywordWithClassName(
						ObjectEditor.TABLE_LISTENER_KEYWORD,
						//  IntrospectUtility.toMaybeProxyTargetClass(viewObject));
						ACompositeLoggable.getTargetClass(viewObject));

			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out
						.println("E** Could not invoke addTableModelListener on"
								+ viewObject);
				// e.printStackTrace();
			}
		}
	}

	public static void maybeRemoveTableModelListener(Object viewObject,
			ObjectAdapter adapter) {
		if (viewObject == null)
			return;

		MethodProxy removeTableModelListenerMethod = IntrospectUtility
				.getRemoveTableModelListenerMethod(RemoteSelector
						.getClass(viewObject));
		if (removeTableModelListenerMethod != null) {
			try {
				Object[] args = { adapter };
				removeTableModelListenerMethod.invoke(viewObject, args);
				adapter.setTableModelListenable(null);
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out
						.println("E** Could not invoke removeTableModelListener on"
								+ viewObject);
				// e.printStackTrace();
			}
		}
	}

	Object observable;

	void setObservable(Object newValue) {
		observable = newValue;
	}

	public static void maybeAddRefresher(Object viewObject,
	// uiObjectAdapter adapter) {
			Refresher adapter) {
		if (viewObject == null)
			return;

//		MethodProxy addObserverMethod = uiBean
//				.getAddObserverMethod( IntrospectUtility.toMaybeProxyTargetClass(viewObject));
		MethodProxy addRefresherMethod = getClassDescriptor(viewObject).getAddRefresherMethod();
		if (addRefresherMethod != null) {
			try {
				Object[] args = { adapter };
				addRefresherMethod.invoke(viewObject, args);
				((ObjectAdapter) adapter).setRegisteredAsListener(true);
				if (adapter instanceof ObjectAdapter)
					((ObjectAdapter) adapter).setObservable(viewObject);
				ObjectEditor.associateKeywordWithClassName(
						ObjectEditor.JAVA_OBSERVER_PATTERN_KEYWORD,
						//  IntrospectUtility.toMaybeProxyTargetClass(viewObject));
						ACompositeLoggable.getTargetClass(viewObject));
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out.println("E** Could not invoke addRefresher on"
						+ viewObject);
				// e.printStackTrace();
			}
		}
	}
	public static void maybeAddObserver(Object viewObject,
			// uiObjectAdapter adapter) {
					Observer adapter) {
				if (viewObject == null)
					return;

//				MethodProxy addObserverMethod = uiBean
//						.getAddObserverMethod( IntrospectUtility.toMaybeProxyTargetClass(viewObject));
				MethodProxy addObserverMethod = getClassDescriptor(viewObject).getAddObserverMethod();
				if (addObserverMethod != null) {
					try {
						Object[] args = { adapter };
						addObserverMethod.invoke(viewObject, args);
						((ObjectAdapter) adapter).setRegisteredAsListener(true);
						if (adapter instanceof ObjectAdapter)
							((ObjectAdapter) adapter).setObservable(viewObject);
						ObjectEditor.associateKeywordWithClassName(
								ObjectEditor.JAVA_OBSERVER_PATTERN_KEYWORD,
								//  IntrospectUtility.toMaybeProxyTargetClass(viewObject));
								ACompositeLoggable.getTargetClass(viewObject));
					} catch (Exception e) {
						// Nothing matters any more
						// 
						System.out.println("E** Could not invoke addObserver on"
								+ viewObject);
						// e.printStackTrace();
					}
				}
			}

	static void maybeDeleteObserver(Object viewObject, ObjectAdapter adapter) {
		if (viewObject == null)
			return;

		MethodProxy DeleteObserverMethod = IntrospectUtility
				.getDeleteObserverMethod( ReflectUtil.toMaybeProxyTargetClass(viewObject));
		if (DeleteObserverMethod != null) {
			try {
				Object[] args = { adapter };
				DeleteObserverMethod.invoke(viewObject, args);
				adapter.setObservable(null);
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out.println("E** Could not invoke DeleteObserver on"
						+ viewObject);
				// e.printStackTrace();
			}
		}
	}

	Object treeModelListenable;

	void setTreeModelListenable(Object newValue) {
		treeModelListenable = newValue;
	}

	public static void maybeAddTreeModelListener(Object viewObject,
			TreeModelListener adapter) {
		// uiObjectAdapter adapter) {
		if (viewObject == null)
			return;

		MethodProxy addTreeModelListenerMethod = IntrospectUtility
				.getAddTreeModelListenerMethod(RemoteSelector
						.getClass(viewObject));
		if (addTreeModelListenerMethod != null) {
			try {
				Object[] args = { adapter };
				ObjectEditor.associateKeywordWithClassName(
						ObjectEditor.TREE_LISTENER_KEYWORD,
						//  IntrospectUtility.toMaybeProxyTargetClass(viewObject));
						ACompositeLoggable.getTargetClass(viewObject));
				addTreeModelListenerMethod.invoke(viewObject, args);
				((ObjectAdapter) adapter).setRegisteredAsListener(true);
				if (adapter instanceof ObjectAdapter)
					((ObjectAdapter) adapter)
							.setTreeModelListenable(viewObject);
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out
						.println("E** Could not invoke addTreeModelListener on"
								+ viewObject);
				// e.printStackTrace();
			}
		}
	}

	static void maybeRemoveTreeModelListener(Object viewObject,
			ObjectAdapter adapter) {
		if (viewObject == null)
			return;

		MethodProxy RemoveTreeModelListenerMethod = IntrospectUtility
				.getRemoveTreeModelListenerMethod(RemoteSelector
						.getClass(viewObject));
		if (RemoveTreeModelListenerMethod != null) {
			try {
				Object[] args = { adapter };
				RemoveTreeModelListenerMethod.invoke(viewObject, args);
				adapter.setTreeModelListenable(null);
			} catch (Exception e) {
				// Nothing matters any more
				// 
				System.out
						.println("E** Could not invoke RemoveTreeModelListener on"
								+ viewObject);
				// e.printStackTrace();
			}
		}
	}
	boolean getRegisteredAsListener() {
		return registeredAsListener;
	}
	public void setRegisteredAsListener(boolean newVal) {
		registeredAsListener = newVal;
	}

	public void registerAsListener(Object obj) {
		/*
		// if (obj == null) return;
		if (getAdapterType() == PROPERTY_TYPE) {
			uiObjectAdapter parentAdapter = getParentAdapter();
			if (parentAdapter != null) {
				Object parentObject = parentAdapter.getViewObject();
				if (parentObject != null) {
					maybeAddPropertyChangeListener(parentObject, this);
					maybeAddRemotePropertyChangeListener(parentObject, this);
				}
			}
		}
		*/
		setDisposed(false);
		if (obj == null)
			return;
		if (this instanceof PrimitiveAdapter && !(this instanceof EnumerationAdapter)) 
			return;
		maybeAddPropertyChangeListener(obj, this);
		maybeAddRemotePropertyChangeListener(obj, this);
		maybeAddTableModelListener(obj, this);
		maybeAddTreeModelListener(obj, this);
		maybeAddObserver(obj, this);
		maybeAddRefresher(obj, this);

	}
	
	
	public void registerAsListenerWorking(Object obj) {
		// if (obj == null) return;
		if (getAdapterType() == PROPERTY_TYPE) {
			ObjectAdapter parentAdapter = getParentAdapter();
			if (parentAdapter != null) {
				Object parentObject = parentAdapter.computeAndMaybeSetViewObject();
				if (parentObject != null) {
					maybeAddPropertyChangeListener(parentObject, this);
					maybeAddRemotePropertyChangeListener(parentObject, this);
				}
			}
		}
		if (obj == null)
			return;
		maybeAddTableModelListener(obj, this);
		maybeAddTreeModelListener(obj, this);
		maybeAddObserver(obj, this);

	}

	public void registerAsListener() {
		Object obj = computeAndMaybeSetViewObject();
		if (obj == null)
			return;
		registerAsListener(obj);

	}

	public void init(ConcreteType concreteObject,
	/* Container containW, */
	Object viewObject, Object realObject, Object parentObject, int posn, String name,
			ClassProxy realClass, boolean propertyFlag,
			ObjectAdapter adaptor, boolean textMode) {

		// Object viewObject = uiGenerator.getViewObject(obj1, textMode);
		/*
		 * if (this instanceof uiContainerAdapter) System.out.println("***D" +
		 * ((uiContainerAdapter) this).getDirection());
		 */
		this.setParentAdapter((CompositeAdapter) adaptor);
		setAdapterAttributes(this, viewObject, parentObject, name);
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(realClass,
				realObject);
		// this.setPropertyClass(inputClass);
		this.setPropertyClass(cd.getRealClass());
		this.setPropertyName(name);
		this.setIndex(posn);
		this.setUIFrame(adaptor.getUIFrame());
		this.setRealObject(realObject);
		// this.setViewObject(uiGenerator.getViewObject(obj1, textMode));
		this.setConcreteObject(concreteObject);
		objectID(); // has side effects
		// moving this up so we can know if the object fires events or not
		registerAsListener(viewObject);

//		this.setViewObject(viewObject, textMode);
		if (propertyFlag) {
			this.setAdapterType(ObjectAdapter.PROPERTY_TYPE);
			// linkPropertyToAdapter(parentObject, name, this);
		}
		this.setViewObject(viewObject, textMode);


		// if (viewObject != null) {
		//if (!(this instanceof uiPrimitiveAdapter) || this instanceof uiEnumerationAdapter) 
//		registerAsListener(viewObject);
		/*
		 * maybeAddRemotePropertyChangeListener(obj, this);
		 * maybeAddTableModelListener(obj, this); maybeAddTreeModelListener(obj,
		 * this); maybeAddObserver(obj, this);
		 */
		// }
		if (realObject != null && realObject != viewObject) {
			registerAsListener(realObject);
		}
		if (getUIFrame() != null && !isGraphicsLeafObject() && !isGraphicsCompositeObject() &&!isDrawingAdapter() && ! (this instanceof PrimitiveAdapter))
			RightMenuManager.getRightMenu(ReflectUtil.toMaybeProxyTargetClass(realObject), this, realObject);
		/*
		 * if (this instanceof uiContainerAdapter) System.out.println("***D" +
		 * ((uiContainerAdapter) this).getDirection());
		 */
		// this.setParentAdapter((uiContainerAdapter) adaptor);
		/*
		 * if (this instanceof uiContainerAdapter) System.out.println("***D" +
		 * ((uiContainerAdapter) this).getDirection());
		 */
		/*
		 * this.setUIFrame(adaptor.getUIFrame()); this.setRealObject(obj1);
		 * //this.setViewObject(uiGenerator.getViewObject(obj1, textMode));
		 * this.setConcreteObject(concreteObject); this.setViewObject(obj,
		 * textMode);
		 */
		// this.setVectorStructure(vectorStructure);
		/*
		 * this.setDefaultAttributes(); this.processAttributes();
		 */
		// this.processAttributeList();
		// If a custom editor has been registered
		// we do not need to go further
		// vectorAdapter.uiAddVectorComponents();
		// vectorAdapter.createChildren();
		/*
		 * if (EditorRegistry.getEditorClass(inputClass) == null && obj != null)
		 * uiAddVectorComponents( //(Container)
		 * vectorAdapter.getWidgetAdapter().getUIComponent(),
		 * vectorAdapter.getGenericWidget(), vectorAdapter, //obj getViewObject(
		 * vectorAdapter.getRealObject()) );
		 */
		LogicalStructureNodeCreated.newCase(this, this);
	}

	/*
	 * public abstract uiObjectAdapter createAdapter (Container containW, Object
	 * obj, Object obj1, Object parentObject, String name, Class inputClass,
	 * boolean propertyFlag, uiObjectAdapter adaptor, boolean textMode);
	 */
	// Constructor.
	public ObjectAdapter() throws RemoteException {
		if (ObjectEditor.shareBeans()) {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.addAdapter(this);
			} else {
				if (ObjectEditor.colabMode())
				ObjectRegistry.newAdapter(this);
			}
		}
	}

	public int getHeight() {
		return 1;
	}
	public int getRowHeight() {
		return 0;
	}

	public int getHeightOfNonShapeDescendents() {
		return 0;
	}

	public int getNumberOfNonShapeLeaves() {
		return 1;
	}

	public int getLevel() {
		if (isTopAdapter())
			return 1;
		return getParentAdapter().getLevel() + 1;
	}

	int row = -1;
	int column = -1;

	public void setRow(int theRow) {
		// if (row == -1)
		row = theRow;
	}

	public void setColumn(int theColumn) {
		// if (column == -1)
		column = theColumn;
	}

	public int getRow() {
		// if (row == -1)
		return row;
	}

	transient boolean isOnlyChild;

	public boolean isOnlyChild() {
		return isOnlyChild;
	}

	/*
	 * transient boolean skippedAdapter = false;
	 * 
	 * boolean isSkippedAdapter() { return skippedAdapter; }
	 */
	Object expansionObject;
	ObjectAdapter expansionAdapter;
	
	public Boolean getImplicitRefreshOnNotification() {
	  return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.REFRESH_ON_NOTIFICATION);
	
	}

	public Object getExpansionObjectAttribute() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.EXPANSION_OBJECT);
		if (value == null)
			return null;
		return value;
	}

	public boolean getDefaultExpanded() {
		if (getRealObject() == null)
			return true;
		Object value = getMergedAttributeValue(AttributeNames.DEFAULT_EXPANDED);
		if (value != null)
			return (Boolean) value;
		if (this instanceof ClassAdapter) {
			return !isFlatTableCell();
		}
		return (Boolean) getTempAttributeValue(AttributeNames.DEFAULT_EXPANDED);
		/*
		 * Object value =
		 * getMergedOrTempAttributeValue(AttributeNames.DEFAULT_EXPANDED); if
		 * (value == null) return true; return (Boolean) value;
		 */
	}
	
	public boolean getExpandPrimitiveChildren() {
		if (getRealObject() == null)
			return false;
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.EXPAND_PRIMITIVE_CHILDREN);
		
	}


	public boolean getShowElideHandles() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_ELIDE_HANDLES);
		if (value == null)
			return true;
		return (Boolean) value;
	}

	public boolean getShowRecursive() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_RECURSIVE);
		if (value == null)
			return true;
		return (Boolean) value;
	}

	public void setExpansionObjectAttribute(Object newVal) {
		// setLocalAttribute(AttributeNames.EXPANSION_OBJECT, newVal);
		setTempAttributeValue(AttributeNames.EXPANSION_OBJECT, newVal);
	}

	void setIsOnlyChild(boolean newVal) {
		isOnlyChild = newVal;
	}
	
	public boolean computeIsOnlyChild() {
		if (getParentAdapter() == null )
			return false;
		return computeHasOnlyChild(getParentAdapter());
	}
	public static boolean computeHasOnlyChild (CompositeAdapter adapter) {
		if (adapter instanceof VectorAdapter ||
				adapter instanceof HashtableAdapter)
				return false;
			//if (adapter.getNumberOfChildren() != 1) 
			// some field may later be unhidden
			if (!(adapter instanceof ClassAdapter))
				return false;
			ClassAdapter classAdapter = (ClassAdapter) adapter;
			if (classAdapter.getRecordStructure() == null )
				return false;
//			if (classAdapter.componentNames().size() != 1) 
//				return false;
			return classAdapter.getChildAdapterCountWithoutSideEffects() == 1;
//		return true;
	}
	
	public Object getMaxValue() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.MAX_VALUE);
		if (value == null)
			return null;
		return value;
	}

	public Object getMinValue() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.MIN_VALUE);
		
		if (value == null)
			return null;
		return value;
	}
	public Object getStepValue() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.STEP_VALUE);
		if (value == null)
			return new Long(1);
		return value;
	}

	public List getLabels() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.COLUMN_LABELS);
		if (value == null)
			return null;
		if (!(value instanceof List))
			return null;
		return (List) value;
	}

	public boolean hasLabels() {
		return getLabels() != null;
	}

	public int getRowAttribute() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.ROW);
		if (value == null)
			return -1;
		else
			return ((Integer) value).intValue();
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)).intValue();
	}
	
	public int getPositionAttribute() {
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.POSITION);
		if (value == null)
			return -1;
		else
			return ((Integer) value).intValue();
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)).intValue();
	}
	
	public String getClassViewGroup() {
		return (String) getMergedTempOrDefaultAttributeValue(AttributeNames.CLASS_VIEW_GROUP);
	}

	public boolean getIndented() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.INDENTED);

	}

	public boolean getRowsLabelled() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ROWS_LABELLED);

	}

	public boolean showRowLabelColumn() {
		// uiContainerAdapter parentAdapter = getParentAdapter();
		return getIndented() && getParentAdapter().getRowsLabelled();
	}

	public boolean getShowBlankColumn() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_BLANK_COLUMN);

	}

	public boolean getShowUnlabelledBorder() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_UNLABELLED_BORDER);

	}

	public void setShowUnlabelledBorder(Object newVal) {
		if (getNonDefaultMergedAttributeValue(AttributeNames.SHOW_UNLABELLED_BORDER) == null)

			setTempAttributeValue(AttributeNames.SHOW_UNLABELLED_BORDER, newVal);
	}

	boolean overrideShowBorder() {
		return isOnlyChild();
		/*
		 * return getParentAdapter() != null &&
		 * getParentAdapter().isSkippedAdapter();
		 */
	}

	boolean overrideLabelVisible = false;

	public boolean overrideLabelVisible() {
		return overrideLabelVisible;
	}

	public void setOverrideLabelVisible(boolean newVal) {
		overrideLabelVisible = newVal;
		propagateAttributesToWidgetShell();
	}

	public boolean getShowBorder() {
		if (overrideShowBorder())
			return false;
		// Boolean retVal = (Boolean)
		// getMergedOrTempAttributeValue(AttributeNames.SHOW_BORDER);
		Boolean retVal = (Boolean) getMergedAttributeValue(AttributeNames.SHOW_BORDER);
		if (retVal != null)
			return retVal;
		else {
			return getComputedShowBorder();
			/*
			 * if (isTopDisplayedAdapter() && !isLabelled()) return false;
			 * 
			 * //return true; //return getWidgetShellColumnTitleStatus() ==
			 * ColumnTitleStatus.hide ||
			 * (getLabelPosition().equals(AttributeNames.LABEL_IN_BORDER));
			 * return
			 * (getLabelPosition().equals(AttributeNames.LABEL_IN_BORDER));
			 * //return true;
			 */
		}

	}

	public boolean getComputedShowBorder() {
		if (overrideShowBorder())
			return false;
		
		if (!isLabelled())
			return false;

		if (isTopDisplayedAdapter() && !isLabelled())
			return false;

		// return true;
		// return getWidgetShellColumnTitleStatus() == ColumnTitleStatus.hide ||
		// (getLabelPosition().equals(AttributeNames.LABEL_IN_BORDER));
		return (getLabelPosition().equals(AttributeNames.LABEL_IN_BORDER));
		// return true;

	}

	public void setShowBorder(Object newVal) {
		if (getNonDefaultMergedAttributeValue(AttributeNames.SHOW_BORDER) == null)

			setTempAttributeValue(AttributeNames.SHOW_BORDER, newVal);
	}

	public boolean getShowTree() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_TREE);

	}

	public boolean getReadOnly() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.READ_ONLY);

	}

	public boolean getShowSystemMenus() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_SYSTEM_MENUS);
	}
	
	public String getMenuName() {
		return (String) getMergedTempOrDefaultAttributeValue(AttributeNames.MENU_NAME);
//		String retVal = (String) getMergedOrTempAttributeValue(AttributeNames.MENU_NAME);
//		if (retVal == null && getRealObject() != null )
//			return AClassDescriptor.toShortName(cl.getName());
//		return
//		 "";
	}

	public boolean getShowObjectMenus() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_OBJECT_MENUS);
	}

	public boolean getShowInterfaceMenus() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_INTERFACE_MENUS);
	}
	public String[] getPredefinedMenusChoice() {
		return (String[]) getMergedTempOrDefaultAttributeValue(AttributeNames.PREDEFINED_MENUS_CHOICE);
	}


	public boolean getShowSuperclassMenus() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_SUPERCLASS_MENUS);
	}

	public String getHashtableChildren() {
		return (String) this
				.getMergedTempOrDefaultAttributeValue(AttributeNames.HASHTABLE_CHILDREN);
	}

	public boolean getPropagateChange() {
		return (Boolean) this
				.getMergedTempOrDefaultAttributeValue(AttributeNames.PROPAGATE_CHANGE);
	}

	public boolean getHorizontalKeyValue() {
		return (Boolean) this
				.getMergedTempOrDefaultAttributeValue(AttributeNames.HORIZONTAL_KEY_VALUE);
	}

	public String getDefinedDirection() {
		return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
	}

	public String getDirection() {
		String retVal = getDefinedDirection();
		if (retVal != null)
			return retVal;
		else
			// return (String)
			// getMergedAttributeValue(AttributeNames.DIRECTION);
			// return (String)
			// getMergedAttributeValue(AttributeNames.DIRECTION);
			// return (String)
			// getMergedOrTempAttributeValue(AttributeNames.DIRECTION);
			return (String) getTempAttributeValue(AttributeNames.DIRECTION);

	}

	public Integer getTextFieldLength() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.TEXT_FIELD_LENGTH);
		if (retVal != null)
			return retVal;
		else
			return 0;

	}
	public static Object[] add (Object[] array1, Object[] array2) {
		if (array1 == null) return array2;
		if (array2 == null) return array1;
		Object retVal[] = new Object[array1.length + array2.length];
		int index = 0;
		while (index < array1.length) {
			retVal[index] = array1[index];
			index++;
		}
		while (index < array1.length + array2.length) {
			retVal[index] = array2[index - array1.length];
			index++;
		}
		return retVal;
		
	}
	public String[] getHTMLDocumentation() {
		String[] retVal = (String[]) getMergedTempOrDefaultAttributeValue(AttributeNames.HTML_DOCUMENTATION);
		MethodProxy webDocumentationMethod = (MethodProxy) getMergedAttributeValue(AttributeNames.WEB_DOCUMENTS_METHOD);
		String[] methodReturnValue = null;
		if (webDocumentationMethod != null) {
			try {
			
				Object[] nullObjectArgs = {};
				//return getExpansionObjectMethod.invoke(targetObject, nullObjectArgs);
				methodReturnValue = (String[]) MethodInvocationManager.invokeMethod(getRealObject(), webDocumentationMethod, nullObjectArgs);
				} catch (Exception e) {
					e.printStackTrace();
//					Tracer.error(error)
				}
		}
		return (String[]) add(retVal, methodReturnValue);
	}

	public String getExplanation() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		String retVal = (String) getMergedTempOrDefaultAttributeValue(AttributeNames.EXPLANATION);
		MethodProxy explanationMethod = (MethodProxy) getMergedAttributeValue(AttributeNames.EXPLANATION_METHOD);
		String methodReturnValue = null;
		if (explanationMethod != null) {
			try {
			
				Object[] nullObjectArgs = {};
				//return getExpansionObjectMethod.invoke(targetObject, nullObjectArgs);
				methodReturnValue = (String) MethodInvocationManager.invokeMethod(getRealObject(), explanationMethod, nullObjectArgs);
				} catch (Exception e) {
					e.printStackTrace();
//					Tracer.error(error)
				}
		}
		if (retVal != null && methodReturnValue != null) {
			return retVal + ". " + methodReturnValue;
			
		} else if (retVal == null) {
			return methodReturnValue;
		}
		else return retVal;

	}
	
	public boolean getDebugWithToolTip() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_DEBUG_INFO_WITH_TOOL_TIP);

	}
	
	public String getToolTipText() {
		String debugInfo = null;
		if (getDebugWithToolTip() ) {
			debugInfo = getDebugInfo();
		}
		String explanation = getExplanation();
		if (debugInfo != null && explanation != null & explanation != "") {
			return explanation + " "  + debugInfo;
		} else if (debugInfo != null) 
			return debugInfo;
		else
			return explanation;
	}
	
	public String getDebugInfo() {
		return null;
		
	}

	public Font getFont() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		Font retVal = (Font) getMergedTempOrDefaultAttributeValue(AttributeNames.FONT);
		return retVal;
	}

	public String getFontName() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		String retVal = (String) getMergedTempOrDefaultAttributeValue(AttributeNames.FONT_NAME);
		return retVal;
	}

	public Integer getFontSize() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// int retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.FONT_SIZE);
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.FONT_SIZE);
		/*
		 * if (retVal == null) return null; else return (Integer) retVal;
		 */
		// return retVal;
	}

	public Integer getFontStyle() {
		
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.FONT_STYLE);
		
	}
	
	public Map<TextAttribute, Object> getTextAttributes() {
		return (Map<TextAttribute, Object>) getMergedTempOrDefaultAttributeValue(AttributeNames.TEXT_ATTRIBUTES);
	}
	
   public Boolean getAllowColumnHide() {
		
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ALLOW_COLUMN_HIDE);
		
	}
   
   public Boolean getAllowColumnElide() {
		
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ALLOW_COLUMN_ELIDE);
		
	}
	
	public Integer getColumnPrefixWidth() {
		
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.COLUMN_PREFIX_WIDTH);
		
	}
	public Integer getRowPrefixWidth() {
		
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ROW_PREFIX_WIDTH);
		
	}
	public Boolean showColumnPrefix() {
		
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_COLUMN_PREFIX);
		
	}	
	public Boolean showRowPrefix() {
		
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_ROW_PREFIX);
		
	}	
	public Boolean showColumnSuffix() {
		
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_COLUMN_SUFFIX);
		
	}	
	public Integer getColumnSuffixWidth() {
		
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.COLUMN_SUFFIX_WIDTH);
		
	}
	public Integer getRowIndentWidth() {
		
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ROW_INDENT_WIDTH);
		
	}
	
	public Color getComponentBackground() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_BACKGROUND);
		
	}
    public Color getContainerBackground() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.CONTAINER_BACKGROUND);
		
	}
    public Color getDrawingPanelColor() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.DRAWING_PANEL_COLOR);
		
	}
	public Color getComponentForeground() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_FOREGROUND);
		
	}
	public Color getLabelBackground() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.LABEL_COLOR);
		
	}
	public Color getColumnPrefixBackground() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.COLUMN_PREFIX_COLOR);
		
	}
	public Color getRowPrefixBackground() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.ROW_PREFIX_COLOR);
		
	}
	public Color getColumnSuffixBackground() {
		
		return (Color) getMergedTempOrDefaultAttributeValue(AttributeNames.COLUMN_SUFFIX_COLOR);
		
	}
	
    public Boolean getAllowMultipleEqualReferences() {
//    	Boolean retVal = (Boolean) getMergedAttributeValue(AttributeNames.ALLOW_MULTIPLE_EQUAL_REFERENCES);
//		if (retVal == null && (hasOnlyGraphicsDescendents()  || getUIFrame().isOnlyGraphicsPanel()))
//				return true;
//		else
//			return false;
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ALLOW_MULTIPLE_EQUAL_REFERENCES);
		
	}

	public static int PIXELS_IN_CHAR = 10;

	public int pixelsInLabel() {
		VirtualComponent component = getUIComponent();
		if (component == null) {
			return maxLabelLength() * PIXELS_IN_CHAR;
		}
		Font font = (Font) component.getFont();
		FontMetrics fontMetrics = (FontMetrics) component.getFontMetrics(font);
		String maxLabel = maxLabel();
		int width = fontMetrics.stringWidth(maxLabel);
		return width;
		
//		return maxLabelLength() * PIXELS_IN_CHAR;

	}

	public int maxLabelLength() {
		int retVal;

		if (getParentAdapter() == null)
			return 0;
		int labelLength  = computedLabelLength();
		if (getAdapterType() == PROPERTY_TYPE) {
			//return getParentAdapter().getMaxComponentNameLength();
			retVal = getParentAdapter().getMaxComponentNameLength();
			
			if (retVal < labelLength) { // component was initially not visible
				
				getParentAdapter().setMaxComponentNameLength(labelLength);
				retVal = labelLength;
			}
		// return getMaximumParentComponentNameLength()*PIXELS_IN_CHAR;
		} else {
			//return getParentAdapter().getMaxDynamicComponentNameLength();
			retVal = getParentAdapter().getMaxDynamicComponentNameLength();
			if (retVal < labelLength) {				
				getParentAdapter().setMaxDynamicComponentNameLength(labelLength);
				retVal = labelLength;
			}
		}
		// how did this ever work!
		//return labelLength;
		return retVal;
		

	}
	
	public String maxLabel() {
		String retVal;

		if (getParentAdapter() == null)
			return "";
		int labelLength  = computedLabelLength();
		String label = largestSynthesizedLabel();
		if (getAdapterType() == PROPERTY_TYPE) {
			//return getParentAdapter().getMaxComponentNameLength();
			retVal = getParentAdapter().getLargestDescendentLabel();
			
			if (retVal.length() < label.length()) { // component was initially not visible
				
				getParentAdapter().setLargestDescendentLabel(label);
				retVal = label;
			}
		// return getMaximumParentComponentNameLength()*PIXELS_IN_CHAR;
		} else {
			//return getParentAdapter().getMaxDynamicComponentNameLength();
			retVal = getParentAdapter().getMaxDynamicComponentName();
			if (retVal.length() < label.length()) {				
				getParentAdapter().setMaxDynamicComponentName(label);
				retVal = label;
			}
		}
		// how did this ever work!
		//return labelLength;
		return retVal;
		
		
	}

	public Integer getLabelWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		Integer retVal = (Integer) getMergedAttributeValue(AttributeNames.LABEL_WIDTH);

		if (retVal != null)
			return retVal;
		return getComputedLabelWidth();
		/*
		 * else { int computedLabelLength = pixelsInLabel(); retVal = (Integer)
		 * AttributeNames.getSystemDefault(AttributeNames.LABEL_WIDTH); if
		 * (retVal != null) return Math.max(retVal, computedLabelLength); else
		 * return 0; }
		 */

	}
	public Integer getRowLabelWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ROW_LABEL_WIDTH);

		return retVal;

	}
	public Integer getFillerWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.FILLER_WIDTH);

		return retVal;

	}

	public Integer getRowLabelsWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		Integer retVal = (Integer) getMergedAttributeValue(AttributeNames.ROW_LABELS_WIDTH);

		if (retVal != null)
			return retVal;
		return getComputedRowLabelsWidth();

		// return retVal;
		/*
		 * else { int computedLabelLength = pixelsInLabel(); retVal = (Integer)
		 * AttributeNames.getSystemDefault(AttributeNames.LABEL_WIDTH); if
		 * (retVal != null) return Math.max(retVal, computedLabelLength); else
		 * return 0; }
		 */

	}

	static final int ROW_LABEL_WIDTH = 10;

	public Integer getComputedLabelWidth() {
		// return ROW_LABEL_WIDTH * getHeight();
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);

		int computedLabelLength = pixelsInLabel() + getComponentPadding();
		Integer retVal = (Integer) AttributeNames
				.getSystemDefault(AttributeNames.LABEL_WIDTH);
		if (retVal != null)
			return Math.max(retVal, computedLabelLength);
		else
			return 0;

	}

	public Integer getComputedRowLabelsWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		return getRowLabelWidth() * (getRowHeight() - 1);
		/*
		 * if (isTopAdapter()) return 0; int retVal = getLabelWidth(); if
		 * (getParentAdapter() != null && getParentAdapter().isFlatTableRow())
		 * retVal += getParentAdapter().getComputedRowLabelsWidth(); return
		 * retVal;
		 */

	}
	public Integer getComponentX() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_X);

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}
	public Integer getComponentY() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_Y);

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}

	public Integer getComponentWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_WIDTH);

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}
	
	public Integer getComponentPadding() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_PADDING);

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}
	
	public Integer getContainerWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.CONTAINER_WIDTH);
		

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}
	public Integer getContainerHeight() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.CONTAINER_HEIGHT);
		

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}
	public Integer getShellWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.SHELL_WIDTH);

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}
	
	public Integer getShellHeight() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.SHELL_HEIGHT);

		//if (retVal != null)
			return retVal;
//		else
//			return 0;

	}

	public Integer getElideComponentWidth() {
		
		Integer retVal = (Integer) getMergedAttributeValue(AttributeNames.ELIDE_COMPONENT_WIDTH);
		
		
//		Integer retVal = (Integer) getMergedOrTempAttributeValue(AttributeNames.ELIDE_COMPONENT_WIDTH);

		if (retVal != null)
			return retVal;
		
		retVal = (Integer) getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		//else
//		retVal = getComponentWidth();
		if (retVal != null)
			return retVal;
		retVal = (Integer) getTempAttributeValue(AttributeNames.ELIDE_COMPONENT_WIDTH);
		if (retVal != null)
			return retVal;
		return 0;
			//return getComponentWidth();

	}

	public Integer getComponentHeight() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_HEIGHT);
		return retVal;
//		if (retVal != null)
//			return retVal;
//		else
//			return 0;

	}

	public ObjectAdapter getFirstNonSkippedAdapter() {
		if (!(this instanceof CompositeAdapter))
			return this;
		CompositeAdapter containerAdapter = (CompositeAdapter) this;
		if (!containerAdapter.isSkippedAdapter())
			return this;
		return containerAdapter.getOnlyChild().getFirstNonSkippedAdapter();
	}

	public boolean isProperty() {
		return getAdapterType() == PROPERTY_TYPE;
	}

	// can probbaly use ProperrtyTyoe
	boolean isVectorChild() {
		return getAdapterType() == INDEX_TYPE ;
		/*
		 * uiContainerAdapter parent = getParentAdapter(); return (parent !=
		 * null) && (parent instanceof uiVectorAdapter);
		 */
	}

	boolean isHashtableValue() {
		return getAdapterType() == VALUE_TYPE;
		/*
		 * uiContainerAdapter parent = getParentAdapter(); return (parent !=
		 * null) && (parent instanceof uiVectorAdapter);
		 */
	}

	boolean isHashtableKey() {
		return getAdapterType() == KEY_TYPE;
		/*
		 * uiContainerAdapter parent = getParentAdapter(); return (parent !=
		 * null) && (parent instanceof uiVectorAdapter);
		 */
	}

	boolean isHashtableChild() {
		return isHashtableValue() || isHashtableKey();
		/*
		 * uiContainerAdapter parent = getParentAdapter(); return (parent !=
		 * null) && (parent instanceof uiVectorAdapter);
		 */
	}

	public boolean isDynamicChild() {
		return isVectorChild() || isHashtableChild();
		// return (getAdapterType() != PROPERTY_TYPE);
	}

	public boolean isDynamic() {
		return false;
	}

	public String getDefinedLabelSuffix() {
		String retVal = (String) getMergedAttributeValue(AttributeNames.LABEL_SUFFIX);
		return retVal;
		/*
		 * if (retVal != null) return retVal; else return
		 */
	}

	public String getDefinedLabelSuffixOrEmptyString() {
		String retVal = (String) getMergedAttributeValue(AttributeNames.LABEL_SUFFIX);
		// return retVal;

		if (retVal != null)
			return retVal;
		else
			return "";

	}

	public String getDefinedLabelSuffixOrSystemDefault() {
		String retVal = (String) getMergedAttributeValue(AttributeNames.LABEL_SUFFIX);
		// return retVal;

		if (retVal != null)
			return retVal;
		else {
			if ("".equals(getLabelWithoutSuffix()))
				return "";
			// adding the getTempAttributeValue to allow : to be added
			// did not make a diference removin it
			//if (getCopiedLabel() && getTempAttributeValue(AttributeNames.LABEL) != null)
			if (getCopiedLabel())

				// return " "; // to equalize label lengths
				return "";
			retVal = (String) getTempAttributeValue(AttributeNames.LABEL_SUFFIX);
			if (retVal == null)
				return ":";
			else
				return retVal;
		}
	}

	public String getLabelPosition() {
		String retVal = getLabelPositionWithoutConsideringColumnTitleStatus();
		if (retVal.equals(AttributeNames.LABEL_IN_BORDER)
				|| getColumnTitleStatus() != ColumnTitleStatus.show)
			return retVal;
		else
			return AttributeNames.LABEL_IS_ABOVE;
	}

	public String getDefinedLabelPosition() {
		return (String) getMergedAttributeValue(AttributeNames.LABEL_POSITION);
	}

	public String getLabelPositionWithoutConsideringColumnTitleStatus() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		/*
		 * if (getWidgetShellColumnTitleStatus() == ColumnTitleStatus.show)
		 * return AttributeNames.LABEL_IS_ABOVE;
		 */
		String retVal = (String) getMergedAttributeValue(AttributeNames.LABEL_POSITION);

		if (retVal != null) {
			return retVal;
		} else {
			Boolean showBorder = (Boolean) getMergedAttributeValue(AttributeNames.SHOW_BORDER);
			if ((showBorder != null) && (!showBorder))
				return AttributeNames.LABEL_IS_LEFT;
			ObjectAdapter adapter = getFirstNonSkippedAdapter();
			CompositeAdapter parent = adapter.getParentAdapter();
			boolean isVectorChild = (parent != null)
					&& (parent instanceof VectorAdapter);
			if (!(adapter instanceof CompositeAdapter) 
					|| isVectorChild()
					|| adapter.isFlatTableRow())
				return AttributeNames.LABEL_IS_LEFT;
			else
				return AttributeNames.LABEL_IN_BORDER;
			/*
			 * retVal = (String)
			 * AttributeNames.getSystemDefault(AttributeNames.LABEL_POSITION);
			 * if (retVal != null) return retVal; else { return
			 * AttributeNames.LABEL_IN_BORDER;
			 */

		}

	}

	public void setDirection(String newVal) {
		this.setTempAttributeValue(AttributeNames.DIRECTION, newVal);
	}

	public boolean isInvisibleNameChild() {
		return getShowReadOnlyNameChildAsLabel()
				&& isUnlabelledReadOnlyNameChild();
	}

	public boolean isUnlabelledReadOnlyNameChild() {
		if (getUsingTrueLabel())
			return false;
		CompositeAdapter parent = getParentAdapter();
		CompositeAdapter grandParent = getGrandParentAdapter();
		if (parent == null || grandParent == null || parent.isRootAdapter()
				|| grandParent.isRootAdapter())
			return false;
		return isNameChild && parent.getRecordStructure().isReadOnly(getPropertyName()) && isReadOnly() /* && parent != null */
				&& parent.getDefinedLabel() == null /*
													 * &&
													 * parent.getParentAdapter() !=
													 * null
													 */;

	}

	boolean usingTrueLabel = false;

	public boolean getUsingTrueLabel() {
		return usingTrueLabel;
	}

	public void setUsingTrueLabel(boolean newVal) {
		usingTrueLabel = newVal;
	}

	public boolean isElided() {
		return isWidgetShellElided();
		// return (Boolean)
		// getMergedOrTempAttributeValue(AttributeNames.ELIDED);
	}
	
	public boolean isVisibleComponent() {
		if (!isVisible()) return false;
		if (isTopAdapter()) return true;
		return getParentAdapter().isVisibleComponent();
	}

	public boolean isVisible() {
		// Object retVal =
		// getMergedOrTempAttributeValue(AttributeNames.VISIBLE);
		/*
		 * uiContainerAdapter parent = getOriginalParentClassAdapter(); if
		 * (parent != null) { boolean readable = parent.isChildReadable(this);
		 * //if (!(parent.isChildReadable(this))); if (!readable) return false; }
		 */
		if (getRealObject() == null & getNotVisibleIfNull())
			return false;
		if (isRecursive() /* && getTextMode() */
				&& !(this instanceof PrimitiveAdapter) && !getShowRecursive())
			return false;
		/*
		if (isInvisibleNameChild())
			return false;
			*/
		boolean readable = computePreRead();
		if (!readable)
			return false;
		Boolean retVal = (Boolean) getMergedAttributeValue(AttributeNames.VISIBLE);
		if (retVal != null)
			return retVal;
		if (isInvisibleNameChild())
			return false;
		/*
		 * else if (parentShowsReadOnlyNameChildAsLabel()) return false;
		 */
		//else
			return (Boolean) getTempAttributeValue(AttributeNames.VISIBLE);
	}

	public int getColumnAttribute() {
		// Object value = getMergedAttributeValue(AttributeNames.COLUMN);
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.COLUMN);
		if (value == null)
			return -1;
		else
			return ((Integer) value).intValue();
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)).intValue();
	}
	
	public boolean getNotVisibleIfNull() {
		// Object value = getMergedAttributeValue(AttributeNames.COLUMN);
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.INVISIBLE_IF_NULL);
		if (value == null)
			return false;
		else
			return ((Boolean) value).booleanValue();
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)).intValue();
	}

	public boolean getOpenOnDoubleClick() {
		// Object value = getMergedAttributeValue(AttributeNames.COLUMN);
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.OPEN_ON_DOUBLE_CLICK);
		if (value == null)
			return false;
		else
			return ((Boolean) value);
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)).intValue();
	}

	public Map<String, String> getProjectionGroups() {
		// Object value = getMergedAttributeValue(AttributeNames.COLUMN);
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.PROJECTION_GROUPS);
		return (Map) value;
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)).intValue();
	}

	public static List<String> toList(String string) {
		List<String> retVal = new Vector();
		String nextElement;
		int nextPos = 0;
		int prevPos = 0;
		while (true) {
			if (prevPos >= string.length())
				return retVal;
			nextPos = string.indexOf(':', prevPos);
			if (nextPos == -1)
				nextPos = string.length();
			nextElement = string.substring(prevPos, nextPos);
			retVal.add(nextElement);
			prevPos = nextPos + 1;
		}

	}

	public Map<String, List<String>> getUnParsedProjectionGroups() {
		Map<String, String> pgs = getProjectionGroups();
		if (pgs == null)
			return null;
		Iterator<String> keys = pgs.keySet().iterator();
		Map<String, List<String>> retVal = new Hashtable();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = pgs.get(key);
			List<String> newValue = toList(value);
			retVal.put(key.toLowerCase(), newValue);
		}
		return retVal;
	}

	public int getPosition() {
		// Object value = getMergedAttributeValue(AttributeNames.POSITION);
		// if (value == null)
		Object value = getMergedTempOrDefaultAttributeValue(AttributeNames.POSITION);
		// if (value == null) return -1;
		if (value == null)
			return getComputedPosition();
		return ((Integer) value).intValue();
	}
	
	public int getVisibleNonVisiblePosition() {
		CompositeAdapter parentAdapter = getParentAdapter();
		if (parentAdapter == null) 
			return -1;
		Vector visibleNonVisibleVector = parentAdapter.visibleAndNonVisibleChildrenVector;
		if (visibleNonVisibleVector == null)
			return -1;
		return visibleNonVisibleVector.indexOf(this);
	}
	

	public int getComputedPosition() {
		if (this.getIndex() != -1) {
			ClassAdapter classAdapter = (ClassAdapter) this
					.getParentAdapter();

			if (getParentAdapter() instanceof VectorAdapter
					|| getParentAdapter() instanceof HashtableAdapter)

				return classAdapter.numFeatures + index;
			else
				return index;
		} else
			return -1;
	}

	public void setPosition(int newVal) {
		this.setTempAttributeValue(AttributeNames.POSITION, newVal);

	}

	public boolean getAutoSave() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.AUTO_SAVE);
	}

	public ContainerFactory getContainerFactory() {
		return (ContainerFactory) getMergedTempOrDefaultAttributeValue(AttributeNames.CONTAINER_FACTORY);

	}

	public LayoutManagerFactory getLayoutManagerFactory() {
		return (LayoutManagerFactory) getMergedTempOrDefaultAttributeValue(AttributeNames.LAYOUT_MANAGER_FACTORY);

	}

	public List getSelectHandlers() {
		return (List) getMergedTempOrDefaultAttributeValue(AttributeNames.SELECT_HANDLERS);
	}

	/*
	 * public List getViewGroups () { return (List)
	 * getMergedOrTempAttributeValue(AttributeNames.VIEW_GROUPS); }
	 */
	public boolean getSelectionIsLink() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SELECTION_IS_LINK);
	}
	
	public Boolean getComponentSelectable() {
		//Boolean retVal =  (Boolean) getMergedOrTempAttributeValue(AttributeNames.COMPONENT_SELECTABLE);
		Boolean retVal =  (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.COMPONENT_SELECTABLE);
		if (retVal == null)
			return isReadOnly();
		return retVal;
//		if (retVal == null)
//			retVal = isReadOnly();
//		return retVal;
	}

	public void setSelectionIsLinkAttribute(boolean newVal) {
		// setLocalAttribute(AttributeNames.SELECTION_IS_LINK, newVal);
		setTempAttributeValue(AttributeNames.SELECTION_IS_LINK, newVal);
	}

	

	public List getExpandHandlers() {
		return (List) getMergedTempOrDefaultAttributeValue(AttributeNames.EXPAND_HANDLERS);
	}

	public Object getAddConstraint() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.ADD_CONSTRAINT);
	}
	
	public Integer getHorizontalBoundGap() {
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.HORIZONTAL_BOUND_GAP);
		if (retVal == null)
			return getHorizontalGap();
		return  retVal;
	}
	
	public Integer getHorizontalGap() {
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.HORIZONTAL_GAP);
	}
	
	public Integer getVerticalGap() {
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.VERTICAL_GAP);
	}
	
	public String getPrompt() {
		return (String) getMergedAttributeValue(AttributeNames.PROMPT);
	}

	public String getDefinedLabelAbove() {
		return (String) getMergedAttributeValue(AttributeNames.LABEL_ABOVE);
	}

	public String getLabelAbove() {
		String retVal = getDefinedLabelAbove();
		if (retVal != null)
			return retVal;
		return getComputedLabelAbove();
		/*
		 * Boolean definedLabelled = (Boolean)
		 * getMergedAttributeValue(AttributeNames.LABELLED); if (definedLabelled !=
		 * null && !definedLabelled &&
		 * AttributeNames.LABEL_IS_ABOVE.equals(getDefinedLabelPosition()) )
		 * return getLabel(); else return (String)
		 * getTempAttributeValue(AttributeNames.LABEL_ABOVE);
		 */
		// return (String)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_ABOVE);
	}

	public String getComputedLabelAbove() {
		Boolean definedLabelled = (Boolean) getMergedAttributeValue(AttributeNames.LABELLED);
		if (definedLabelled != null
				&& !definedLabelled
				&& AttributeNames.LABEL_IS_ABOVE
						.equals(getDefinedLabelPosition()))
			return getLabel();
		else
			return (String) getTempAttributeValue(AttributeNames.LABEL_ABOVE);

	}

	public String getDefinedLabelBelow() {
		return (String) getMergedAttributeValue(AttributeNames.LABEL_BELOW);
	}

	public String getLabelBelow() {
		String retVal = getDefinedLabelBelow();
		if (retVal != null)
			return retVal;
		return getComputedLabelBelow();
		/*
		 * Boolean definedLabelled = (Boolean)
		 * getMergedAttributeValue(AttributeNames.LABELLED); if (definedLabelled !=
		 * null && !definedLabelled &&
		 * AttributeNames.LABEL_IS_BELOW.equals(getDefinedLabelPosition()) )
		 * return getLabel(); else return (String)
		 * getTempAttributeValue(AttributeNames.LABEL_BELOW);
		 */
		// return (String)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_BELOW);
	}

	public String getComputedLabelBelow() {
		Boolean definedLabelled = (Boolean) getMergedAttributeValue(AttributeNames.LABELLED);
		if (definedLabelled != null
				&& !definedLabelled
				&& AttributeNames.LABEL_IS_BELOW
						.equals(getDefinedLabelPosition()))
			return getLabel();
		else
			return (String) getTempAttributeValue(AttributeNames.LABEL_BELOW);
	}

	public String getLabelLeft() {
		String retVal = getDefinedLabelLeft();
		if (retVal != null)
			return retVal;
		return getComputedLabelLeft();
		/*
		 * Boolean definedLabelled = (Boolean)
		 * getMergedAttributeValue(AttributeNames.LABELLED); if (definedLabelled !=
		 * null && !definedLabelled &&
		 * AttributeNames.LABEL_IS_LEFT.equals(getDefinedLabelPosition()) )
		 * return getLabel(); else return (String)
		 * getTempAttributeValue(AttributeNames.LABEL_LEFT);
		 */
		/*
		 * String retVal = (String)
		 * getMergedOrTempAttributeValue(AttributeNames.LABEL_LEFT); return
		 * retVal;
		 */
		/*
		 * if (retVal != null || this instanceof uiContainerAdapter ) return
		 * retVal; else return getLabel();
		 */

	}

	public String getComputedLabelLeft() {
		Boolean definedLabelled = (Boolean) getMergedAttributeValue(AttributeNames.LABELLED);
		if (definedLabelled != null
				&& !definedLabelled
				&& AttributeNames.LABEL_IS_LEFT
						.equals(getDefinedLabelPosition()))
			return getLabel();
		else
			return (String) getTempAttributeValue(AttributeNames.LABEL_LEFT);
	}

	public String getDefinedLabelLeft() {
		String retVal = (String) getMergedAttributeValue(AttributeNames.LABEL_LEFT);
		return retVal;
		/*
		 * if (retVal != null || this instanceof uiContainerAdapter ) return
		 * retVal; else return getLabel();
		 */

	}

	public String getLabelRight() {
		String retVal = getDefinedLabelRight();
		if (retVal != null)
			return retVal;
		return getComputedLabelRight();
		/*
		 * Boolean definedLabelled = (Boolean)
		 * getMergedAttributeValue(AttributeNames.LABELLED); if (definedLabelled !=
		 * null && !definedLabelled &&
		 * AttributeNames.LABEL_IS_RIGHT.equals(getDefinedLabelPosition()) )
		 * return getLabel(); else return (String)
		 * getTempAttributeValue(AttributeNames.LABEL_RIGHT);
		 */
		// return (String)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_RIGHT);
	}

	public String getDefinedLabelRight() {
		return (String) getMergedAttributeValue(AttributeNames.LABEL_RIGHT);
	}

	public String getComputedLabelRight() {
		Boolean definedLabelled = (Boolean) getMergedAttributeValue(AttributeNames.LABELLED);
		if (definedLabelled != null
				&& !definedLabelled
				&& AttributeNames.LABEL_IS_RIGHT
						.equals(getDefinedLabelPosition()))
			return getLabel();
		else
			return (String) getTempAttributeValue(AttributeNames.LABEL_RIGHT);
	}

	/*
	 * public String getIconAbove () { return (String)
	 * getMergedAttributeValue(AttributeNames.ICON_ABOVE); } public String
	 * getIconBelow () { return (String)
	 * getMergedAttributeValue(AttributeNames.ICON_BELOW); } public String
	 * getIconLeft () { return (String)
	 * getMergedAttributeValue(AttributeNames.ICON_LEFT); } public String
	 * getIconRight () { return (String)
	 * getMergedAttributeValue(AttributeNames.ICON_RIGHT); }
	 */
	public boolean getSeparateUnbound() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SEPARATE_UNBOUND));
	}

	public boolean getSeparateUnboundTitles() {
		// Boolean retVal = ((Boolean)
		// getMergedAttributeValue(AttributeNames.SEPARATE_UNBOUND_TITLES));
		Boolean retVal = ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SEPARATE_UNBOUND_TITLES));
		if (retVal != null)
			return retVal;
		else
			// return (getStretchUnboundColumns() && getStretchUnboundRows());
			return getComputedSeparateUnbound();
	}

	public boolean getComputedSeparateUnbound() {
		return (getStretchUnboundColumns() && getStretchUnboundRows());
	}

	public boolean getShowButtons() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_BUTTON))
				.booleanValue();
	}
	
	public boolean isBorderLayout() {		
		return(AttributeNames.BORDER_LAYOUT.equals(getLayout()));
	}
	
	public boolean isGridBagLayout() {		
		return(AttributeNames.GRID_BAG_LAYOUT.equals(getLayout()));
	}
	
	
	public boolean isNoLayout() {		
		return(AttributeNames.NO_LAYOUT.equals(getLayout()));
	}
	
	public boolean isUnboundFlowLayout() {		
		return(AttributeNames.FLOW_LAYOUT.equals(getUnboundLayout()));
	}
	
	public boolean isUnboundGridLayout() {		
		return(AttributeNames.GRID_LAYOUT.equals(getUnboundLayout()));
	}
	public boolean isUnboundBorderLayout() {		
		return(AttributeNames.BORDER_LAYOUT.equals(getUnboundLayout()));
	}
	
	public boolean isUnboundNoLayout() {		
		return(AttributeNames.NO_LAYOUT.equals(getUnboundLayout()));
	}
	
	public boolean isFlowLayout() {		
		return(AttributeNames.FLOW_LAYOUT.equals(getLayout()));
	}
	
	public boolean isGridLayout() {		
		return(AttributeNames.GRID_LAYOUT.equals(getLayout()));
	}
	
	

	public boolean getShowUnboundButtons() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_UNBOUND_BUTTONS))
				.booleanValue();
	}
	
	public String getLayout() {
		return (String) getMergedTempOrDefaultAttributeValue(AttributeNames.LAYOUT);
	}
	
	public String getUnboundLayout() {
		String retVal = (String) getMergedAttributeValue(AttributeNames.UNBOUND_LAYOUT);
		if (retVal != null)
			return retVal;
			
		return getLayout();
	}
	
	public Integer getAlignment() {
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ALIGNMENT);
	}

	public Object getBoundPlacement() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.BOUND_PLACEMENT);
	}

	public Object getUnboundPropertiesPlacement() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.UNBOUND_PROPERTIES_PLACEMENT);
	}
	
	public Object getPropertiesPlacement() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.PROPERTIES_PLACEMENT);
	}

	public Object getUnboundButtonsPlacement() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.UNBOUND_BUTTONS_PLACEMENT);
	}
	
	public Object getButtonsPlacement() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.BUTTONS_PLACEMENT);
	}


	public Object getRowsPlacement() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.ROWS_PLACEMENT);
	}

	public Object getColumnsPlacement() {
		return getMergedTempOrDefaultAttributeValue(AttributeNames.COLUMNS_PLACEMENT);
	}

	public Integer getUnboundButtonsRowSize() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.UNBOUND_BUTTONS_ROW_SIZE))
				;
				//.intValue();
	}
	public Integer getNumRows() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.NUM_ROWS));
			
	}
	
	public Integer getNumColumns() {
		return (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.NUM_COLUMNS);
	}

	public int getEmptyBorderWidth() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.EMPTY_BORDER_WIDTH))
				.intValue();
	}

	public int getEmptyBorderHeight() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.EMPTY_BORDER_HEIGHT))
				.intValue();
	}
	
	public Integer getAddWidthConstraint() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ADD_WIDTH_CONSTRAINT));
			
	}
	
	public Double getAddWeightXConstraint() {
		return ((Double) getMergedTempOrDefaultAttributeValue(AttributeNames.ADD_WEIGHT_X_CONSTRAINT));
			
	}
	
	public Double getAddWeightYConstraint() {
		return ((Double) getMergedTempOrDefaultAttributeValue(AttributeNames.ADD_WEIGHT_Y_CONSTRAINT));
			
	}
	
	public Integer getAddAnchorConstraint() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ADD_ANCHOR_CONSTRAINT));
			
	}
	
	public Integer getAddFillConstraint() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ADD_FILL_CONSTRAINT));
			
	}

	public boolean getStretchRows() {

		Boolean retVal = ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.STRETCH_ROWS))
				.booleanValue();
		if (retVal != null)
			return retVal;
		else
			return false;

	}
	
	public boolean getStretchableByParent() {
//		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.STRETCHABLE_BY_PARENT);
		Boolean retVal = (Boolean) getMergedAttributeValue(AttributeNames.STRETCHABLE_BY_PARENT);
		if (retVal != null) return retVal;
		
			Object layout = getLayout();
			if (layout != null) return true; // they have asked for a layout, dont change it
		
			return (Boolean) getTempAttributeValue(AttributeNames.STRETCHABLE_BY_PARENT);

	}

	public boolean getStretchUnboundRows() {

		Boolean retVal = ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.STRETCH_UNBOUND_ROWS));
		if (retVal != null)
			return retVal;
		else
			// return getStretchRows();
			return getComputedStretchUnboundRows();

	}

	public boolean getComputedStretchUnboundRows() {

		return getStretchRows();

	}

	public boolean getTopRowWithPrimitivesAndComposites() {
		return false;
	}

	void invalidateAncestorLeafAdapters() {
		invalidateComponentsSet();
		// leafComponentsSet = false;
		if (isFlatTableRow())
			return;
		if (getParentAdapter() != null)
			getParentAdapter().invalidateAncestorLeafAdapters();
	}

	void invalidateComponentsSet() {
		childComponentsSet = false;
		leafComponentsSet = false;
	}

	public void invalidateComponentsSetInTree() {
		invalidateComponentsSet();
	}

	boolean leafComponentsSet = false;
	Vector<ObjectAdapter> leafComponents = new Vector();

	public Vector<ObjectAdapter> getNonGraphicsLeafAdapters() {
		if (!leafComponentsSet) {
			// leafComponents.clear();
			setNonGraphicsLeafAdapters(leafComponents);
			leafComponentsSet = true;
		}

		return leafComponents;
	}

	boolean childComponentsSet = false;
	Vector<ObjectAdapter> childComponents = new Vector();

	public Vector<ObjectAdapter> getNonGraphicsChildAdapters() {
		if (!childComponentsSet) {
			// childComponents.clear();
			setNonGraphicsChildAdapters(childComponents);
			childComponentsSet = true;
		}

		return childComponents;
	}

	public void setNonGraphicsLeafAdapters(
			Vector<ObjectAdapter> theLeafComponents) {

		leafComponentsSet = true;
		if (getUIComponent() != null ) {
			theLeafComponents.add(this);
			// leafComponents.add(this);
		}
	}

	public void setNonGraphicsChildAdapters(
			Vector<ObjectAdapter> childComponents) {

		childComponentsSet = true;
		// if (getUIComponent() != null)
		// childComponents.add(this);
	}
	
	public boolean isFlatTable() {
		return isFlatTable;
	}
	public void setIsFlatTable(boolean newVal) {
		isFlatTable = newVal;
	}

	public boolean getElideIfNoComponents() {
		return (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ELIDE_IF_NO_COMPONENTS);

	}
	public boolean isFlatTableRow() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.IS_FLAT_TABLE_ROW));

	}
	public void setIsFlatTableRow() {
		 
		  if (!isFlatTableRowDescendent() && getSeparateUnboundTitles() && 
				  getHeight() >= 2 && 
				  //getHeight() > 2 && 
				  getDirection().equals(AttributeNames.HORIZONTAL) &&
					getParentAdapter() != null &&
					isVectorChild() &&
					getParentAdapter().getDirection().equals(AttributeNames.VERTICAL)) {
					  setTempAttributeValue(AttributeNames.IS_FLAT_TABLE_ROW, true);
					  getParentAdapter().setIsFlatTable(true);
		  }
	  }

	/*
	 * public boolean isFlatTableCell() { return ((Boolean)
	 * getMergedOrTempAttributeValue(AttributeNames.IS_FLAT_TABLE_CELL));
	 *  }
	 */

	public boolean isFlatTableCell() {
		Boolean retVal = ((Boolean) getNonDefaultMergedAttributeValue(AttributeNames.IS_FLAT_TABLE_CELL));
		if (retVal != null)
			return retVal;
		if (retVal != null)
			retVal = (Boolean) getTempAttributeValueWithoutMerging(AttributeNames.IS_FLAT_TABLE_CELL);
		if (retVal != null)
			return retVal;
		if (this instanceof CompositeAdapter || getParentAdapter() == null)
			return (Boolean) AttributeNames
					.getSystemDefault(AttributeNames.IS_FLAT_TABLE_CELL);
		retVal = (getParentAdapter().isFlatTableComponent() || getParentAdapter()
				.isFlatTableRow() /*
									 * )&&
									 * !(getParentAdapter().isFlatTableCell()
									 */);
		setTempAttributeValue(AttributeNames.IS_FLAT_TABLE_CELL, retVal);
		return retVal;

	}

	// purely computed - should not be set by user
	public boolean isFlatTableComponent() {

		Boolean retVal = ((Boolean) getTempAttributeValueWithoutMerging(AttributeNames.IS_FLAT_TABLE_COMPONENT));
		if (retVal != null)
			return retVal;
		retVal = getFlatTableRowAncestor() != null;
		setTempAttributeValue(AttributeNames.IS_FLAT_TABLE_COMPONENT, retVal);
		return retVal;

	}

	public boolean getStretchColumns() {
		// Boolean retVal =((Boolean)
		// getMergedOrTempAttributeValue(AttributeNames.STRETCH_COLUMNS));
		Boolean retVal = ((Boolean) getMergedAttributeValue(AttributeNames.STRETCH_COLUMNS));
		if (retVal != null)
			return retVal;
		else
			// return getStretchColumns();
			return getComputedStretchColumns();
		/*
		 * return ((Boolean)
		 * getMergedOrTempAttributeValue(AttributeNames.STRETCH_COLUMNS));
		 */
	}

	public boolean getStretchUnboundColumns() {
		Boolean retVal = ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.STRETCH_UNBOUND_COLUMNS));
		if (retVal != null)
			return retVal;
		else
			// return getStretchColumns();
			return getComputedStretchUnboundColumns();
	}

	public boolean getComputedStretchColumns() {
		return (Boolean) getTempAttributeValue(AttributeNames.STRETCH_COLUMNS);
		// return getStretchColumns();
	}

	public boolean getComputedStretchUnboundColumns() {
		return getStretchColumns();
	}

	public boolean getEqualRows() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.EQUAL_ROWS));
	}

	public boolean getAlignCells() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ALIGN_CELLS))
				.booleanValue();
	}

	public String getCellFillerLabel() {
		return ((String) getMergedTempOrDefaultAttributeValue(AttributeNames.CELL_FILLER_LABEL));
	}
	public Integer getCellFillerLabelWidth() {
		return ((Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.CELL_FILLER_LABEL));
	}

	public String getCellFillerIcon() {
		return ((String) getMergedTempOrDefaultAttributeValue(AttributeNames.CELL_FILLER_ICON));
	}

	public boolean getRowsFullSize() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ROWS_FULL_SIZE));
	}

	public boolean getShowBoundaryLabels() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_BOUNDARY_LABELS));
	}
	
	public boolean getShowBoundary() {
		return ((Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SHOW_BOUNDARY));
	}

	public int getColumn() {
		// if (column == -1)
		return column;
	}

	transient String childAdapterIndex;

	public void setAdapterIndex(String s) {
		childAdapterIndex = s;
	}

	String getAdapterIndex() {
		return childAdapterIndex;
	}

	public ObjectAdapter(CompositeAdapter parent) throws RemoteException {
		setParentAdapter(parent);
		if (ObjectEditor.shareBeans()) {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.addAdapter(this);
			} else {
				ObjectRegistry.newAdapter(this);
			}
		}
	}

	public void setWidgetAdapter(WidgetAdapterInterface w) {
		widgetAdapter = w;
	}

	public WidgetAdapterInterface getWidgetAdapter() {
		return widgetAdapter;
	}

	boolean disposed = false;

	boolean isDisposed() {
		return disposed;
	}
	
	void setDisposed(boolean newVal) {
		disposed = newVal;
	}

	public void cleanUp() {
		if (disposed)
			return;
		if (SelectionManager.getCurrentSelection() == this)
			SelectionManager.unselect();
		if (widgetAdapter != null) {
			widgetAdapter.removeUIComponentValueChangedListener(this);
			widgetAdapter.cleanUp();
		}
		this.parent = null;
		this.uiFrame = null;
		// this.genericWidget = null;
		setWidgetShell(null);
		setWidgetAdapter(null);
		// this.widgetAdapter = null;
		// this.realObject = null;
		setRealObject(null);
		this.viewObject = null;
		this.sourceAdapter = null;
		this.concreteObject = null;
		// adding later becomes a problem
//		maybeDeleteObserver(this.observable, this);
//		this.maybeRemoveTableModelListener(this.tableModelListenable, this);
//		this.maybeRemoveTreeModelListener(this.treeModelListenable, this);
		showChidrenColumnTitleSet = false;
		leafComponents = new Vector();
		leafComponentsSet = false;
		childComponentsSet = false;
		childComponents = new Vector();
		setDisposed(true);
	}

	boolean addMeBack = false;

	boolean getAddMeBack() {
		return addMeBack;
	}

	void setAddMeBack(boolean newVal) {
		addMeBack = newVal;
	}
	
//	boolean activeObserver = true;
//	boolean getActiveObserger() {
//		return activeObserver;
//	}
//	void setActiveObserver(boolean newVal) {
//		activeObserver = newVal;
//	}

	public void cleanUpForReuse() {

		if (SelectionManager.getCurrentSelection() == this)
			SelectionManager.unselect();
		/*
		 * if ((this instanceof uiContainerAdapter) ) { setWidgetShell(null);
		 * setWidgetAdapter(null); } else {
		 */
		CompositeAdapter parentAdapter = getParentAdapter();
		boolean reuse = true;
		WidgetAdapterInterface parentWidgetAdapter = null;
		// parentWidgetAdapter = parentAdapter.getWidgetAdapter();
		VirtualComponent component = getUIComponent();
		if (component == null)
			reuse = false;
		else if (parentAdapter == null)
			reuse = false;
		else {
			parentWidgetAdapter = parentAdapter.getWidgetAdapter();
			if ((parentWidgetAdapter == null)
					&& (getUIComponent().getParent() != null))
				reuse = false;
		}

		if (!reuse) {
			setWidgetShell(null);
			setWidgetAdapter(null);
		} else if (component.getParent() != null) {
			parentWidgetAdapter.remove(getIndex(), this);
			if (getWidgetShell() != null) {
				getWidgetShell().cleanUpForReuse();
			}
		}

		/*
		 * }
		 */
		showChidrenColumnTitleSet = false;
		leafComponentsSet = false;
		leafComponents = new Vector();
		childComponentsSet = false;
		childComponents = new Vector();
		// this stuff is causing problems in vector add 
//		maybeDeleteObserver(this.observable, this);
		// should add, maybeDeletePropertyCchangeListener also
//		this.maybeRemoveTableModelListener(this.tableModelListenable, this);
//		this.maybeRemoveTreeModelListener(this.treeModelListenable, this);
		setDisposed(true);
		// mergedAttributeList = null;
	}

	public void redoAncestorDisplay() {
		CompositeAdapter parentAdapter = getParentAdapter();
		if (parentAdapter == null)
			return;
		if (parentAdapter.getWidgetAdapter() == null)
			parentAdapter.redoAncestorDisplay();
		else
			parentAdapter.cleanUpForReuse();

	}

	public static void recursiveCleanUp(ObjectAdapter root) {
		(new CleanUpAdapterVisitor(root)).traverse();
	}

	public static void cleanUpForReuse(ObjectAdapter root) {
		// (new CleanUpForReuseAdapterVisitor(root)).traverse();
		(new CleanUpForReuseAdapterVisitor(root)).traversePostOrder();
	}

	public static void registerAsListener(ObjectAdapter root) {
		(new AddListenersAdapterVisitor(root)).traverse();
	}

	public void unmap() {
		LoggableRegistry.unMapLoggableFromAdapter(this);
	}

	public static void recursiveUnmap(ObjectAdapter root) {
		(new UnmapAdapterVisitor(root)).traverse();
	}

	public void recursiveUnmap() {
		recursiveUnmap(this);
	}

	public static void cleanUpDescendents(ObjectAdapter root) {
		(new CleanUpAdapterVisitor(root)).traverseChildren();
	}

	public void setAdapterType(int type) {
		if (type == PROPERTY_TYPE || type == PRIMITIVE_TYPE
				|| type == LISTENABLE_TYPE || type == INDEX_TYPE
				|| type == KEY_TYPE || type == VALUE_TYPE)
			adaptorType = type;
	}

	public int getAdapterType() {
		return adaptorType;
	}

	/*
	 * public void setAdapterField(Field f) { adaptorField = f; } public Field
	 * getAdapterField() { return adaptorField; }
	 */

	public void setPropertyName(String name) {
		propertyName = name;
	}

	public String getComponentName() {
		CompositeAdapter parentAdapter = getParentAdapter();
		if (parentAdapter == null)
			return "";
		String retVal = parentAdapter.getChildAdapterRealIndex(this);
		return retVal;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getDisplayedPropertyName() {
		return displayedPropertyName;
	}

	public void setDisplayedPropertyName(String newVal) {
		displayedPropertyName = newVal;
	}

	public void setDisplayedPropertyName() {
		setDisplayedPropertyName(getLabelWithoutSuffix());
		boolean isHashtableChild = getParentAdapter() != null
				&& getParentAdapter() instanceof HashtableAdapter;
		// the commented out condition can occur when a new adapter is created
		// from a source adapter
		/*
		 * if (!isHashtableChild && (isValueAdapter() || isKeyAdapter()))
		 * return;
		 */
		if (!isHashtableChild)
			return;
		// normal traverse does not cover non displayed children - this is a
		// hack
		if (isValueAdapter()
				&& AttributeNames.VALUES_ONLY
						.equals(((HashtableAdapter) parent)
								.getHashtableChildren())) {
			getKeyAdapter().setDisplayedPropertyName();
		} else if (isKeyAdapter()
				&& AttributeNames.KEYS_ONLY
						.equals(((HashtableAdapter) parent)
								.getHashtableChildren())) {
			getValueAdapter().setDisplayedPropertyName();
		}
	}

	public void setPropertyClass(ClassProxy newClass) {
		propertyClass = newClass;
	}

	public String getVirtualClass(Object object) {
		String virtualClass = IntrospectUtility.getVirtualClass(object);
		if (virtualClass == null)
			// return uiClassFinder.getName (object.getClass());
			return null;
		else
			return virtualClass;

	}

	public String getVirtualClass() {
		if (computeAndMaybeSetViewObject() != null)
			return getVirtualClass(computeAndMaybeSetViewObject());
		else
			// return uiClassFinder.getName (propertyClass);
			return null;
	}

	// this is strange
	public ClassProxy getPropertyClass() {
		if (computeAndMaybeSetViewObject() != null) {
			Object anObject = computeAndMaybeSetViewObject();
			// return  IntrospectUtility.toMaybeProxyTargetClass(getViewObject());
//			ClassProxy viewClass =  IntrospectUtility.toMaybeProxyTargetClass(computeAndMaybeSetViewObject());
			ClassProxy viewClass =  ReflectUtil.toMaybeProxyTargetClass(anObject);
			viewClass = ReflectUtil.toMaybeProxyTargetClass(viewClass, anObject);
			ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(viewClass,
					anObject);
			return cd.getRealClass(); // this is real vs virtual not real vs view
		} else
			return propertyClass;

	}

	/*
	 * 
	 * public void setPropertyReadMethod(Method readMethod) { propertyReadMethod =
	 * readMethod; } public Method getPropertyReadMethod() { return
	 * propertyReadMethod; }
	 * 
	 * public void setPropertyWriteMethod(Method writeMethod) {
	 * propertyWriteMethod = writeMethod; } public Method
	 * getPropertyWriteMethod() { return propertyWriteMethod; } public void
	 * setPreReadMethod(Method thePreReadMethod) { preReadMethod =
	 * thePreReadMethod; } public Method getPreReadMethod() { return
	 * preReadMethod; } public Method getPreWriteMethod() { return
	 * preWriteMethod; } public void setPreWriteMethod(Method thePreWriteMethod) {
	 * preWriteMethod = thePreWriteMethod; } public void setPreMethods(Class
	 * parentClass) { setPreReadMethod(uiBean.getPre(propertyReadMethod,
	 * parentClass)); setPreWriteMethod(uiBean.getPre(propertyWriteMethod,
	 * parentClass)); }
	 */
	public ClassAdapter getOriginalParentClassAdapter() {
		if (!(getOriginalSourceAdapter().getParentAdapter() instanceof ClassAdapter))
			return null;
		else
			return (ClassAdapter) parent;
	}

	public RecordStructure getParentRecordStructure() {
		// virtualParent = (getOriginalSourceAdapter().getParentAdapter());
		ClassAdapter parentClassAdapter = getOriginalParentClassAdapter();
		if (parentClassAdapter == null)
			return null;
		return parentClassAdapter.getRecordStructure();
	}

	public boolean getInheritedReadOnly() {
		boolean readOnlyAttribute = getReadOnly();
		if (readOnlyAttribute)
			return true;
		CompositeAdapter parent = getParentAdapter();
		if (parent == null)
			return false;
		else
			return parent.getInheritedReadOnly();
	}

	public boolean isDynamicReadOnly() {
		return false;
	}

	public boolean getInheritedDynamicReadOnly() {

		boolean retVal = isDynamicReadOnly();
		if (retVal)
			return true;
		CompositeAdapter parent = getParentAdapter();
		if (parent == null)
			return false;
		else
			return parent.getInheritedDynamicReadOnly();
	}

	public boolean isReadOnly() {
		try {
			// boolean readOnlyAttribute = getReadOnly();
			boolean readOnlyAttribute = getInheritedReadOnly();
			CompositeAdapter parent = getParentAdapter();
			if (parent == null)
				return readOnlyAttribute;
			boolean dynamicReadOnly = getInheritedDynamicReadOnly();

			return !getPreWrite() || readOnlyAttribute || dynamicReadOnly
					|| parent.isReadOnly(this);
		} catch (Exception e) {
			System.out.println("isReadOnly " + e);
			return false;
		}
	}

	public Object get() {
		try {
			return getParentRecordStructure().get(propertyName);
		} catch (Exception e) {
			System.out.println("get " + e);
			return null;
		}
	}

	public Object set(Object newVal, boolean initialChange) {
		try {
			if (initialChange)
				return getParentRecordStructure().set(propertyName, newVal,
						this);
			else
				return getParentRecordStructure().set(propertyName, newVal);
		} catch (Exception e) {
			System.out.println("set " + e);
			return null;
		}
	}

	public boolean computePreRead() {

		// if (preReadMethod == null) return true;
		/*
		 * if (!(getParentAdapter() instanceof uiClassAdapter)) return true;
		 */
		ClassAdapter parentClassAdapter = getOriginalParentClassAdapter();
		if (parentClassAdapter == null)
			return true;
		boolean readable = parent.isChildReadable(this);
		return readable;
		/*
		 * RecordStructure recordStructure =
		 * parentClassAdapter.getRecordStructure(); if (recordStructure == null)
		 * return true; return recordStructure.preRead(this.propertyName);
		 */
		/*
		 * Object parentObject = parent.getViewObject(); if (parentObject ==
		 * null) return true; try { Object[] params = {}; return ((Boolean)
		 * uiMethodInvocationManager.invokeMethod(preReadMethod, parentObject,
		 * params)).booleanValue(); } catch (Exception e) { return true; }
		 */
	}

	public boolean setPreRead() {
		boolean oldValue = preRead;
		preRead = computePreRead();
		return oldValue != preRead;
	}

	public boolean getPreRead() {
		return preRead;
	}

	public boolean computePreWrite() {
		ClassAdapter parentClassAdapter = getOriginalParentClassAdapter();
		if (parentClassAdapter == null)
			return true;
		if (parentClassAdapter == null)
			return true;
		boolean writeable = parent.isChildWriteable(this);
		return writeable;
		/*
		 * RecordStructure recordStructure =
		 * parentClassAdapter.getRecordStructure(); if (recordStructure == null)
		 * return true; return recordStructure.preWrite(this.propertyName);
		 */
		/*
		 * if (preWriteMethod == null) return true; Object parentObject =
		 * parent.getViewObject(); if (parentObject == null) return true; try {
		 * Object[] params = {}; return ((Boolean)
		 * uiMethodInvocationManager.invokeMethod(preWriteMethod, parentObject,
		 * params)).booleanValue(); } catch (Exception e) { return true; }
		 */
	}

	public boolean setPreWrite() {
		boolean oldValue = preWrite;
		preWrite = computePreWrite();
		return oldValue != preWrite;
	}

	public boolean getPreWrite() {
		return preWrite;
	}

	boolean realObjectInitialized = false;
	boolean firstValueOfRealObject = false;

	public boolean setRealObject(Object obj) {
		// let us try optimizing for the case when the adapter is a listenabe
		if (getRegisteredAsListener()) {
			realObject = obj;
			previousRealObject = obj;
			return true;
		}
		// why was the following line commented out?
		// if (realObject == obj) return false;
		obj = maybeGetComposite( obj );
	    //if the object's address does not change, but its value does, then previousObject will always be the very first object
//		boolean retVal = (realObject != obj);
// 		fixing the previous problem		
		boolean retVal = !OEMisc.equals(obj, previousRealObject);
		
		if (realObjectInitialized) {
			// previousRealObject = realObject;
			// if (this instanceof uiEnumerationAdapter)
			// previousRealObject = Misc.clone(realObject);

			firstValueOfRealObject = false;

		} else {
			// previousRealObject = obj;
			// previousRealObject = Misc.clone(obj);
			// previousRealObject = null;
			firstValueOfRealObject = true;
		}
		if (realObjectInitialized && retVal
				&& previousRealObject instanceof ACompositeLoggable) {
			LoggableRegistry.reMapLoggableToAdapter(this,
					(ACompositeLoggable) previousRealObject,
					(ACompositeLoggable) obj);

		} else if (realObjectInitialized && retVal
				&& obj instanceof ACompositeLoggable) {
			LoggableRegistry.mapLoggableToAdapter((ACompositeLoggable) obj,
					this);
		}
		if (obj instanceof ACompositeLoggable && retVal) { // adding retVal check
			// Message.error("Need to deal with cloning loggables");
			previousRealObject = OEMisc.clone(obj);
		} else if (retVal) // adding retVal check
			previousRealObject = OEMisc.clone(obj); // this value will beaccessed before new real object is set

		realObjectInitialized = true;
		if (ObjectEditor.shareBeans()) {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.replaceObject(realObject, obj);
			} else {
				ObjectRegistry.mapObjectToAdapter(obj, this);
			}
		}
		realObject = obj;
		// return true;
		return retVal;
	}

	public Object getRealObject() {
		return realObject;
	}
	// overiden by shape adapter
	public void retargetConcrete() {
		getConcreteObject().setTarget(computeAndMaybeSetViewObject());		
	}
	
	boolean retargetedButNotRefreshed;
	public void setRetargetedButNotRefreshed(boolean newValue) {
		retargetedButNotRefreshed = newValue;
	}
	public boolean getRetargetedButNotRefreshed() {
		return retargetedButNotRefreshed;
	}

	public void retarget(Object realObject) {
		setRetargetedButNotRefreshed(true);
		setRealObject(realObject);
		computeAndSetViewObject();
		retargetConcrete();
		//getConcreteObject().setTarget(computeAndMaybeSetViewObject());
	}
	public Object getViewObject() {
		return viewObject;
	}

	// side effect - this is ugly!!
	public Object computeAndMaybeSetViewObject() {
		if (realOfViewHasChanged()) 
			computeAndSetViewObject();
		return viewObject;
	}
	public Object getViewObject(Object realObject) {
		if (realOfViewHasChanged())
			computeAndSetViewObject();
		return viewObject;
	}

	public Object getCurrentViewObject() {
		if (viewObject == null) {
			// this can be called before view object is initialized
//			Tracer.error("Null view object:" + viewObject);
		}
		return viewObject;
	}

	public void askViewObjectToRefresh() {
		if (computeAndMaybeSetViewObject() == getRealObject())
			return;
		getConcreteObject().askViewObjectToRefresh(computeAndMaybeSetViewObject());
	}
	
	public Object lastRealOfViewObject() {
		return lastRealOfViewObject;
	}

	public boolean realOfViewHasChanged() {
		return lastRealOfViewObject != getRealObject();
	}
	public boolean realOfViewHasChanged(Object realObject) {
		return lastRealOfViewObject != realObject;
	}

	public Object computeViewObject(Object realObject) {
		return uiGenerator.getViewObject(realObject, getTextMode());
	}

	public Object computeViewObject(CompositeAdapter parentAdapter,
			Object realObject) {

		if (getRealObject() == realObject || viewObject != null)
			return viewObject;
		else

			return uiGenerator.computeViewObject(parentAdapter, realObject,
					getTextMode());
	}

	Object lastRealOfViewObject;

	public void computeAndSetViewObject() {
		Object oldViewObject = getViewObject();
		if (oldViewObject == lastRealOfViewObject()) {
			setViewObject( getRealObject());
			return;
		}
		if (oldViewObject != null && oldViewObject instanceof OEView) {
			((OEView) oldViewObject).retarget(getRealObject());
			//setViewObject(viewObject);
			//return;
		}
		else
			setViewObject(uiGenerator.computeViewObject(getParentAdapter(),
				getRealObject(), propertyName), getTextMode());
		// do we need this?
		lastRealOfViewObject = getRealObject();
	}

	public Object maybeGetComposite( Object obj ) {
		Object retVal = obj;
		if (parent != null && !(retVal instanceof AnIdentifiableLoggable)) {
			Object parentObject = parent.getViewObject();
			if (parentObject instanceof ACompositeLoggable) {
				retVal = LoggableRegistry.getLoggableModel(retVal);
			}
		}
		return retVal;
	}
	
	public void setViewObject(Object obj) {
		if (viewObject == obj)
			return;
		obj = maybeGetComposite( obj );
		if (ObjectEditor.shareBeans()) {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.replaceObject(viewObject, obj);
			} else {
				ObjectRegistry.mapObjectToAdapter(obj, this);
			}
		}
		viewObject = obj;
		lastRealOfViewObject = getRealObject();
		processViewObject(viewObject);
	}
	
	public void processViewObject(Object viewObject) {
//		if (isNestedShapesContainer() && viewObject instanceof ShapeListMouseClickListener)
//			shapeListMouseClickListener = (ShapeListMouseClickListener) viewObject;
	}
	
	

	public void setViewObject(Object obj, boolean textMode) {
		setViewObject(obj);
	}

	transient ConcreteType concreteObject;

	public ConcreteType getConcreteObject() {
		return concreteObject;
	}

	public void setConcreteObject(ConcreteType obj) {
		if (ObjectEditor.shareBeans()) {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.replaceObject(concreteObject, obj);
			} else {
				ObjectRegistry.mapObjectToAdapter(obj, this);
			}
		}
		concreteObject = obj;
	}

	public void refreshConcreteObject(Object viewObject) {
		if (concreteObject == null)
			return;
		viewObject = maybeGetComposite( viewObject );
		concreteObject.setTarget(viewObject);
	}

	public void refreshConcreteObject() {
		refreshConcreteObject(computeAndMaybeSetViewObject());
		/*
		 * if (concreteObject == null) return;
		 * concreteObject.setTarget(getViewObject());
		 */
	}

	public VirtualComponent getUIComponent() {
		if (getWidgetAdapter() == null)
			return null;
		else
			return getWidgetAdapter().getUIComponent();
	}

	public void setParentAdapter(CompositeAdapter p) {
		parent = p;
		/*
		 * // Add our widget as a child of the parents widget if (p != null) if
		 * (p.getUIComponent() != null && ! (this.getViewObject() instanceof
		 * ShapeModel)) { //((Container)
		 * p.getUIComponent()).add(uiGenericWidget.getNullWidget(""));
		 * //uiGenericWidget.addNullWidget ((Container) p.getUIComponent(),
		 * "space"); ((Container) p.getUIComponent()).add(genericWidget); }
		 */
	}

	public void setParentContainer(VirtualContainer p) {
		WidgetAdapterInterface wa = this.getWidgetAdapter();
		if (wa != null)
			// wa.setUIComponentContainer(p);
			wa.setParentContainer(p);
	}

	public VirtualContainer getParentContainer() {
		WidgetAdapterInterface wa = this.getWidgetAdapter();
		if (wa != null)
			// wa.setUIComponentContainer(p);
			return wa.getParentContainer();
		else
			return null;
	}

	void add(CompositeAdapter parentAdapter, ObjectAdapter childAdapter,
			VirtualContainer parent, VirtualComponent child) {
		WidgetAdapterInterface widgetAdapter = parentAdapter
				.getWidgetAdapter();
		if (widgetAdapter != null)
			widgetAdapter.add(parent, child, childAdapter);
		else
			parent.add(child);
	}

	void remove(CompositeAdapter parentAdapter, ObjectAdapter childAdapter,
			VirtualContainer parent, VirtualComponent child) {
		WidgetAdapterInterface widgetAdapter = parentAdapter
				.getWidgetAdapter();
		if (widgetAdapter != null)
			widgetAdapter.remove(parent, child, childAdapter);
		else
			parent.remove(child);
	}
	

	void remove(CompositeAdapter parentAdapter, ObjectAdapter childAdapter,
			VirtualComponent child) {
		WidgetAdapterInterface widgetAdapter = parentAdapter
				.getWidgetAdapter();
		if (widgetAdapter != null)
			widgetAdapter.remove(child, childAdapter);
		else
			((VirtualContainer) parentAdapter.getUIComponent()).remove(child);
	}

	boolean isComponentEmpty() {
		return getWidgetAdapter() != null && getWidgetAdapter().isEmpty();
	}

	public void addUIComponentToParent(VirtualComponent comp) {
		// setAddMeBack(false);
		if (this.computeAndMaybeSetViewObject() instanceof RemoteShape)
			return;
		// if (isComponentEmpty()) return;
		CompositeAdapter p = getParentAdapter();
		if (p == null)
			return;
		VirtualComponent pcomp = p.getUIComponent();
		if (!(pcomp instanceof VirtualContainer))
			return;
		// dont need this as we will create textfield etc as components, or we
		// will have to make virtual equvalents of these
		// if (pcomp instanceof JTextField || pcomp instanceof JTextArea ||
		// pcomp instanceof TextField || pcomp instanceof TextArea) return;
		VirtualContainer pcont = (VirtualContainer) pcomp;
		WidgetShell genericWidget = getGenericWidget();
		if (genericWidget == null) {
			add(p, this, pcont, comp);
			// pcont.add(comp);
			// setUIComponentContainer(pcont);
			// comp.setBackground(pcont.getBackground());
			return;
		}
		// if (genericWidget.getParent() == null) {
		// pcont.add(genericWidget);
		// pcont.add(genericWidget.getContainer());
		add(p, this, pcont, genericWidget.getContainer());
		genericWidget.setParentContainer(pcont);
		// setUIComponentContainer(genericWidget.getContainer());
		// genericWidget.getContainer().setBackground(pcont.getBackground());
		// comp.setBackground(pcont.getBackground());
		// }
		genericWidget.setComponent(comp);
		/*
		 * // Add our widget as a child of the parents widget if (p != null) if
		 * (p.getUIComponent() != null && ! (this.getViewObject() instanceof
		 * ShapeModel)) { //((Container)
		 * p.getUIComponent()).add(uiGenericWidget.getNullWidget(""));
		 * //uiGenericWidget.addNullWidget ((Container) p.getUIComponent(),
		 * "space"); ((Container) p.getUIComponent()).add(genericWidget); }
		 */
	}

	public void removeUIComponentFromParent(CompositeAdapter pAdapter) {
		if (this.computeAndMaybeSetViewObject() instanceof RemoteShape)
			return;
		// uiContainerAdapter p = getParentAdapter();
		CompositeAdapter p = pAdapter;
		/*
		 * if (p == null) return; Component pcomp = p.getUIComponent(); if (!
		 * (pcomp instanceof Container)) return; Container pcont = (Container)
		 * pcomp;
		 */
		WidgetShell genericWidget = getGenericWidget();
		if (genericWidget == null) {
			// pcont.remove(getUIComponent());
			remove(p, this, getUIComponent());
			return;
		}
		if (genericWidget.getParent() != null) {
			// pcont.add(genericWidget);
			// pcont.remove(genericWidget.getContainer());
			remove(p, this, genericWidget.getContainer());
		}
		// genericWidget.setComponent(comp);
		/*
		 * // Add our widget as a child of the parents widget if (p != null) if
		 * (p.getUIComponent() != null && ! (this.getViewObject() instanceof
		 * ShapeModel)) { //((Container)
		 * p.getUIComponent()).add(uiGenericWidget.getNullWidget(""));
		 * //uiGenericWidget.addNullWidget ((Container) p.getUIComponent(),
		 * "space"); ((Container) p.getUIComponent()).add(genericWidget); }
		 */
	}

	public void removeUIComponentFromParent(VirtualContainer pcont) {
		if (this.computeAndMaybeSetViewObject() instanceof RemoteShape)
			return;
		CompositeAdapter p = getParentAdapter();
		/*
		 * if (p == null) return; Component pcomp = p.getUIComponent(); if (!
		 * (pcomp instanceof Container)) return; Container pcont = (Container)
		 * pcomp;
		 */
		WidgetShell genericWidget = getGenericWidget();
		if (genericWidget == null) {
			// pcont.remove(getUIComponent());
			remove(p, this, pcont, getUIComponent());
			return;
		}
		if (genericWidget.getParent() != null) {
			// pcont.add(genericWidget);
			// pcont.remove(genericWidget.getContainer());
			remove(p, this, pcont, genericWidget.getContainer());
		}
		// genericWidget.setComponent(comp);
		/*
		 * // Add our widget as a child of the parents widget if (p != null) if
		 * (p.getUIComponent() != null && ! (this.getViewObject() instanceof
		 * ShapeModel)) { //((Container)
		 * p.getUIComponent()).add(uiGenericWidget.getNullWidget(""));
		 * //uiGenericWidget.addNullWidget ((Container) p.getUIComponent(),
		 * "space"); ((Container) p.getUIComponent()).add(genericWidget); }
		 */
	}

	public CompositeAdapter getParentAdapter() {
		return parent;
	}

	public CompositeAdapter getGrandParentAdapter() {
		ObjectAdapter parent = getParentAdapter();
		if (parent == null)
			return null;
		return parent.getParentAdapter();
		// return parent;
	}

	/*
	 * public uiContainerAdapter getFlatTableRowAncestor() {
	 * 
	 * uiContainerAdapter parentAdapter = getParentAdapter(); if (parentAdapter ==
	 * null) return null; if (parentAdapter.isFlatTableRow()) return
	 * parentAdapter; return parentAdapter.getFlatTableRowAncestor(); }
	 */
	public CompositeAdapter getFlatTableRowAncestor() {
		if (isFlatTableRow())
			return (CompositeAdapter) this;
		CompositeAdapter parentAdapter = getParentAdapter();
		if (parentAdapter == null)
			return null;
		/*
		 * if (parentAdapter.isFlatTableRow()) return parentAdapter;
		 */
		return parentAdapter.getFlatTableRowAncestor();
	}

	public CompositeAdapter getTopFlatTableRow() {
		if (isFlatTableRow())
			return (CompositeAdapter) this;
		else
			return getFlatTableRowAncestor();

	}
	
	// isFlatTableRow is not the same as isTopTableRow?
	
	public boolean isTopTableRow() {
		ObjectAdapter topFlatTableRow = getTopFlatTableRow();
		return topFlatTableRow == null || topFlatTableRow == this;
	}
	

	public static ObjectAdapter getTopAdapter(ObjectAdapter a) {
		if (a.getParentAdapter() == null
				|| a.getParentAdapter().getParentAdapter() == null) {
			// System.out.println("top adapter" + a + a.getRealObject());
			return a;
		} else
			return getTopAdapter(a.getParentAdapter());
	}

	public WidgetAdapterInterface getContainingWidgetAdapter() {
		if (getWidgetAdapter() != null)
			return getWidgetAdapter();
		CompositeAdapter parent = getParentAdapter();
		if (parent != null)
			// System.out.println("top adapter" + a + a.getRealObject());
			return parent.getContainingWidgetAdapter();
		return null;

	}

	public static RootAdapter getRootAdapter(ObjectAdapter a) {
		if (a.isRootAdapter())
			return (RootAdapter) a;
		if (a.getParentAdapter() != null)
			// System.out.println("top ada
			return getRootAdapter(a.getParentAdapter());
		return null;
	}

	public RootAdapter getRootAdapter() {

		return getRootAdapter(this);
	}

	public ObjectAdapter getTopAdapter() {

		return getTopAdapter(this);
	}

	public boolean isTopAdapter() {

		return getTopAdapter(this) == this;
	}

	boolean isTopDisplayedAdapter = false;

	public void setTopDisplayedAdapter(boolean newVal) {
		isTopDisplayedAdapter = newVal;
	}

	public boolean isTopDisplayedAdapter() {

		return isTopDisplayedAdapter || isTopAdapter();
	}

	public boolean isRootAdapter() {

		return getParentAdapter() == null;
	}

	public boolean isAncestor(ObjectAdapter ancestor) {
		return isAncestor(this, ancestor);
	}

	public boolean hasShapeAncestor() {
		if (this instanceof ShapeObjectAdapter)
			return true;
		if (getParentAdapter() == null)
			return false;
		return getParentAdapter().hasShapeAncestor();
	}

	public static boolean isAncestor(ObjectAdapter child,
			ObjectAdapter ancestor) {
		if (child == null)
			return false;
		// if (child.getParentAdapter() == ancestor) return true;
		if (child == ancestor)
			return true;
		return isAncestor(child.getParentAdapter(), ancestor);

	}

	public static boolean isAncestor(ObjectAdapter child, Vector ancestors) {
		for (int i = 0; i < ancestors.size(); i++)
			if (isAncestor(child, (ObjectAdapter) ancestors.elementAt(i)))
				return true;
		return false;

	}

	public boolean isAncestor(Vector ancestors) {
		return isAncestor(this, ancestors);

	}

	/*
	 * public void setAttributeManager(uiAttributeManager m) { attributeManager =
	 * m; }
	 * 
	 * public uiAttributeManager getAttributeManager() { return
	 * attributeManager; }
	 */

	// Methods to handle the primitive events
	// generated by the listenable object types.
	public void objectValueChanged(ValueChangedEvent evt) {
		// This event could have been generated by us!
		if (!evt.getNewValue().equals(getWidgetAdapter().getUIComponentValue())) {
			getWidgetAdapter().setUIComponentValue(evt.getNewValue());
		}
	}
	
	public void attributeChanged(PropertyChangeEvent evt) {
		String attributeName = evt.getPropertyName();
		Object  attributeValue = evt.getNewValue();
		Attribute attribute = new Attribute(attributeName, attributeValue);
		setLocalAttribute(attribute);
		getWidgetAdapter().processAttribute(attribute);
		
	}
	
	public void refreshAttributes() {
		propagateAttributesToWidgetAdapter();
		propagateAttributesToWidgetShell();
//		getUIFrame().refresh();
	}
	
	boolean attributeChangePending;
	void handleSuppressedNotification(PropertyChangeEvent evt) { // subclasses will handle this
		
	}

	// Method invoked when a bound property changes
	//
	// why is this not in classadapter?
	// will have to change the addListener methods. Sometime later
	// actually enumearion adapter also receives this event, so
	// it ha sto be here
	public synchronized void propertyChange(PropertyChangeEvent evt) {
//		System.out.println ("Synchronized Property Change Listener Started");
		// System.out.println("Property change:
		// "+evt.getPropertyName()+"="+evt.getNewValue());
		
		if (evt.getSource() == null) {
			Tracer.error("Null source in:" + evt);
			return;
		}
		if (evt.getPropertyName() == null) {
			Tracer.error("Null property in:" + evt);
			return;
		}
		if (isDisposed())
			return;	
		if (AttributeNames.IGNORE_NOTIFICATION == evt.getSource() ||
				AttributeNames.IGNORE_NOTIFICATION == evt.getPropertyName() ||
				AttributeNames.IGNORE_NOTIFICATION == evt.getOldValue())
			return; // not an OE notification, meant for other observers
		notifyAttributeDependencies(evt);
		if (!isVisibleComponent()) {
			return;
		}
		if (evt.getSource() instanceof ObjectAdapter) {
			attributeChanged(evt);
			return;
		}
		// we will not suppress these
		 // allow color and other changes to be made dynamically
		// changing this to suppress them and make refresh refresh attributes
		 Object newValue = evt.getNewValue();
		 if (newValue instanceof Attribute) {
			 Attribute attribute = (Attribute) newValue;
//			 ObjectAdapter adapter = getUIFrame().getObjectAdapterFromPath(evt.getPropertyName());
			 ObjectAdapter adapter = pathToObjectAdapter(this, evt.getPropertyName());

			 if (adapter == null) {
				 UnknownPropertyNotification.newCase(evt.getPropertyName(), evt.getSource(), this);
//				 Tracer.war("No component  of: " + getRealObject() + " with path:" + evt.getPropertyName());
				 return;
			 }
			 // makes no sense why local does not work but  temp does
//			 adapter.setLocalAttribute(attribute);
			 adapter.setTempAttributeValue(attribute.getAttributeName(), attribute.getValue());
			 // no refresh if suppessing
			 if (!getUIFrame().isSuppressPropertyNotifications())
			 adapter.refreshAttributes();
			 else 	
				 adapter.setAttributeChangePending(true);
			 return;
		 }
		
		 if (evt.getPropertyName().equalsIgnoreCase(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING)) {
			 if (evt.getNewValue() == null)
				 return;
			 getUIFrame().setSuppressPropertyNotifications((Boolean) evt.getNewValue());
			 	return; // do not want to repeat the refresh
				 
		 }
		 // need to check this after setting the value
		 if (getUIFrame().isSuppressPropertyNotifications()) {
			    handleSuppressedNotification(evt);
				return;
		 }
		 attributeChangePending = false;
		//		ClassAdapterReceivedPropertyChangeEvent.newCase((ClassAdapter) this, evt) ;
		// should put in some property to suppress and resume events
		 // done
		 
		
//		// we will not suppress these
//				 // allow color and other changes to be made dynamically
//				// changing this to suppress them and make refresh refresh attributes
//				 Object newValue = evt.getNewValue();
//				 if (newValue instanceof Attribute) {
//					 Attribute attribute = (Attribute) newValue;
//					 ObjectAdapter adapter = getUIFrame().getObjectAdapterFromPath(evt.getPropertyName());
//					 // makes no sense why local does not work but  temp does
////					 adapter.setLocalAttribute(attribute);
//					 adapter.setTempAttributeValue(attribute.getAttributeName(), attribute.getValue());
//
//					 adapter.refreshAttributes();
//					 return;
//				 }
			 
		 
		// maybe I should  event information
		 
		if (!ObjectEditor.shareBeans()) {
			subPropertyChange(evt);
//			System.out.println ("Synchronized Property Change Listener Left");

			// propagateChange();
		} else {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.logUnivPropertyChange(new UnivPropertyChange(
						this, evt));
			} else {
				String myPath = getCompletePathOnly();
				System.out.print("PPP propertyChange at " + myPath);
				if (myPath != null) {
					ObjectRegistry
							.logUnivPropertyChange(new UnivPropertyChange(
									myPath, evt));
				} else {
					subPropertyChange(evt);
					// System.out.println("PPP discarding propertyChange because
					// adapter has not been mapped");
				}
			}
		}
	}

	public void remotePropertyChange(PropertyChangeEvent evt)
			throws RemoteException {
		propertyChange(evt);

	}

	public ObjectAdapter getEditedObjectAdapter(String changedPropertyName) {
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
	
	public ObjectAdapter getNearestObjectAdapterWithWidgetAdapter() {
		if (getWidgetAdapter() != null
		/*
		 * && (changedPropertyName == null ||
		 * changedPropertyName.equals(this.getPropertyName()))
		 */)
			return this;
		if (isTopAdapter())
			return null;
		return getVirtualParent().getNearestObjectAdapterWithWidgetAdapter();
	}

	public boolean isLeafOfAtomic() {
		if (isAtomic())
			return true;
		if (isTopAdapter())
			return false;
		return parent.isLeafOfAtomic();
	}

	public boolean isWidgetShellElided() {
		if (getGenericWidget() == null)
			return false;
		return getGenericWidget().isElided();
	}
	
	public boolean isWidgetShellExplicitlyElided() {
		if (getGenericWidget() == null)
			return false;
		return getGenericWidget().isExplicitlyElided();
	}

	public boolean expandElidedString() {
		if (getGenericWidget() == null)
			return false;
		return getGenericWidget().expandElidedString();
	}

	public void refreshEditable() {
		boolean oldVal = isUnEditable();
		boolean newVal = isReadOnly();
		if (newVal)
			setUneditable();
		else
			setEditable();

	}

	boolean isEditable = true;

	public void setUneditable() {

		if (isEditable == false && !firstTimeEditableChanged)
			return;

		isEditable = false;
		if (getWidgetAdapter() != null) {
			getWidgetAdapter().setUIComponentUneditable();
			firstTimeEditableChanged = false;
		}
	}

	boolean firstTimeEditableChanged = true;

	public void setEditable() {
		if (isEditable == true && !firstTimeEditableChanged)
			return;
		// firstTimeEditableChanged = false;
		isEditable = true;
		if (getWidgetAdapter() != null) {
			getWidgetAdapter().setUIComponentEditable();
			firstTimeEditableChanged = false;
		}
	}
	
	public void setEditable (boolean newVal) {
		if (newVal)
			setEditable();
		else
			setUneditable();
	}

	public boolean isUnEditable() {
		return !isEditable;
	}

	public boolean iEditable() {
		return isEditable;
	}

	public void internalElide() {
		if (getGenericWidget() != null)
			getGenericWidget().internalElide();
	}

	public void internalExpand() {
		if (getGenericWidget() != null)
			getGenericWidget().internalExpand();
	}

	public void subPropertyChange(PropertyChangeEvent evt) {
		ObjectAdapter editedObjectAdapter = getEditedObjectAdapter(evt
				.getPropertyName());
//		ClassAdapterReceivedPropertyChangeEvent.newCase((ClassAdapter) this, evt) ;

		haveReceivedNotification();
		if (editedObjectAdapter == null)
			return;
		if (evt.getPropertyName().toLowerCase().equals(
				propertyName.toLowerCase())) {
			if (getAdapterType() == PROPERTY_TYPE) {
				// System.out.println("Setting property to "+evt.getNewValue());
				// setValue(evt.getNewValue());
				if (getParentAdapter() != null) {
					ObjectAdapter adapter = ((ClassAdapter) getParentAdapter())
							.getStaticChildAdapterMapping(propertyName);
					if (adapter == null)
						((ClassAdapter) getParentAdapter()).implicitRefresh(true);
					else
						// ((uiClassAdapter)
						// getParentAdapter()).refreshChild(evt.getNewValue(),
						// propertyName, false);
						((ClassAdapter) getParentAdapter()).refresh(evt
								.getNewValue(), propertyName, false);

					/*
					 * if (!((uiClassAdapter)
					 * getParentAdapter()).refreshChild(evt.getNewValue(),
					 * propertyName, false)) this.implicitRefresh();
					 */
				} else
					refreshValue(evt.getNewValue());
				if (this != editedObjectAdapter) { // editedObjectAdapter is an
					// ancestor with an atomic
					// widger
					((CompositeAdapter) editedObjectAdapter)
							.setChangedChildInAtomicWidget(this);
					editedObjectAdapter.implicitRefresh(true);
					((CompositeAdapter) editedObjectAdapter)
							.resetChangedChildInAtomicWidget();
				}

				/*
				 * if (this == editedObjectAdapter && getWidgetAdapter() !=
				 * null) refreshValue(evt.getNewValue()); else
				 * editedObjectAdapter.implicitRefresh();
				 */
			} else { // it seems the previous branch already does
				// setUIComponentValue

				if (this == editedObjectAdapter)
					getWidgetAdapter().setUIComponentValue(evt.getNewValue());
			}
			// getWidgetAdapter().setUIComponentValue(evt.getNewValue());
		}
		getUIFrame().validate();
	}

	public void refreshEditedObject() {
		/*
		 * uiFrame frame = this.getUIFrame(); if (frame != null)
		 * frame.doRefresh();
		 */

		ObjectAdapter editedObjectAdapter = getEditedObjectAdapter(null);
		if (editedObjectAdapter == null)
			return;
		editedObjectAdapter.implicitRefresh(true);

	}

	public void treeNodesChanged(TreeModelEvent e) {

		refreshEditedObject();
	}

	public void treeNodesInserted(TreeModelEvent e) {
		refreshEditedObject();
	}

	public void treeNodesRemoved(TreeModelEvent e) {
		refreshEditedObject();
	}

	public void treeStructureChanged(TreeModelEvent e) {
		refreshEditedObject();
	}

	public void tableChanged(TableModelEvent e) {
		refreshEditedObject();
	}

	public void update(Observable o, Object arg) {
		implicitRefresh(true);
//		getUIFrame().repaint();
		getUIFrame().validate();
		// refreshEditedObject();
	}
	public void refresh(Object o) {
		implicitRefresh(true);
		getUIFrame().validate();
	}

	transient boolean edited = false;

	public void setEdited(boolean newVal) {
		edited = newVal;
		if (parent != null)
			// ((uiContainerAdapter) parent).childEditingStatusChanged();
			((CompositeAdapter) parent).childEditingStatusChanged(edited);
		// genericWidget.setUpdated();
	}

	public void setWidgetShellEdited() {
		if (isEdited()) {
			WidgetShell genWidget = getGenericWidget();
			if (genericWidget != null)
				genWidget.setEdited();
		}
	}

	public boolean isEdited() {
		return edited;
	}

	public void uiComponentValueEdited(boolean edited) {

		setEdited(edited);
		if (genericWidget == null)
			return;
		genericWidget.setEdited(edited);

	}

	public void uiComponentValueEdited() {

		// if (edited) return;
		setEdited(true);
		if (genericWidget == null)
			return;
		genericWidget.setEdited();

	}

	public void uiComponentValueUpdated() {

		// if (edited) return;
		setEdited(false);
		if (genericWidget == null)
			return;
		genericWidget.setUpdated();

	}

	public boolean isDeletable() {
		if (parent == null)
			return false;
		return parent.isChildDeletable(this);
		/*
		 * if (parent.getViewObject() == null) return false; if (parent
		 * instanceof uiVectorAdapter) { uiVectorAdapter vectorAdapter =
		 * (uiVectorAdapter) parent; return
		 * vectorAdapter.getRemoveElementAtMethod() != null ||
		 * vectorAdapter.getRemoveElementMethod() != null; }; return false;
		 */
	}

	public boolean preDeleteFromParent() {
		return parent != null && parent.computeAndMaybeSetViewObject() != null
				&& parent.validateDeleteChild(this);
	}

	public void deleteFromParent() {
		if (parent == null)
			return;
		Object parentObject = parent.computeAndMaybeSetViewObject();
		if (parentObject == null)
			return;
		parent.deleteChild(this);
		/*
		 * if (parent instanceof uiVectorAdapter) { uiVectorAdapter
		 * parentAdapter = (uiVectorAdapter) getParentAdapter(); try {
		 * 
		 * Method removeElementAtMethod =
		 * parentAdapter.getRemoveElementAtMethod(); if (removeElementAtMethod !=
		 * null) { index =
		 * Integer.parseInt(parentAdapter.getChildAdapterIndex(this)); Object
		 * params[] = {new Integer(index)};
		 * //setElementAtMethod.invoke(parentObject, params);
		 * getUIFrame().getUndoer().execute ( new
		 * VoidSubtractAddFirstCommand(//getUIFrame(), this,
		 * removeElementAtMethod, parentObject, params,
		 * parentAdapter.getInsertElementAtMethod(), //readMethodParams
		 * parentAdapter.getElementAtMethod() ));
		 * //uiMethodInvocationManager.invokeMethod(this.getUIFrame(),
		 * parentObject, removeElementAtMethod, params); } else { Method
		 * removeElementMethod = parentAdapter.getRemoveElementMethod(); if
		 * (removeElementMethod != null) { Object params[] =
		 * {getOriginalValue()}; getUIFrame().getUndoer().execute ( new
		 * SymmetricCommand(//getUIFrame(), this, removeElementMethod,
		 * parentObject, params, parentAdapter.getAddElementMethod()//,
		 * //readMethodParams ));
		 * //uiMethodInvocationManager.invokeMethod(this.getUIFrame(),
		 * parentObject, removeElementMethod, params); } } //index =
		 * Integer.parseInt(vectorAdapter.getChildAdapterIndex(this)); } catch
		 * (Exception e) { //e.printStackTrace();
		 * System.out.println("Exception"); index = 0; } }
		 */

	}

	/*
	 * public void delete() { if (parent == null) return; Object parentObject =
	 * parent.getViewObject(); if (parentObject == null) return; if (parent
	 * instanceof uiVectorAdapter) { uiVectorAdapter parentAdapter =
	 * (uiVectorAdapter) getParentAdapter(); try {
	 * 
	 * Method removeElementAtMethod = parentAdapter.getRemoveElementAtMethod();
	 * if (removeElementAtMethod != null) { index =
	 * Integer.parseInt(parentAdapter.getChildAdapterIndex(this)); Object
	 * params[] = {new Integer(index)};
	 * //setElementAtMethod.invoke(parentObject, params);
	 * getUIFrame().getUndoer().execute ( new
	 * VoidSubtractAddFirstCommand(//getUIFrame(), this, removeElementAtMethod,
	 * parentObject, params, parentAdapter.getInsertElementAtMethod(),
	 * //readMethodParams parentAdapter.getElementAtMethod() ));
	 * //uiMethodInvocationManager.invokeMethod(this.getUIFrame(), parentObject,
	 * removeElementAtMethod, params); } else { Method removeElementMethod =
	 * parentAdapter.getRemoveElementMethod(); if (removeElementMethod != null) {
	 * Object params[] = {getOriginalValue()}; getUIFrame().getUndoer().execute (
	 * new SymmetricCommand(//getUIFrame(), this, removeElementMethod,
	 * parentObject, params, parentAdapter.getAddElementMethod()//,
	 * //readMethodParams ));
	 * //uiMethodInvocationManager.invokeMethod(this.getUIFrame(), parentObject,
	 * removeElementMethod, params); } } //index =
	 * Integer.parseInt(vectorAdapter.getChildAdapterIndex(this)); } catch
	 * (Exception e) { //e.printStackTrace(); System.out.println("Exception");
	 * index = 0; } }
	 *  }
	 */
	public boolean isAddableToParent(Object o) {
		if (parent == null)
			return false;
		return parent.isAddable(o);
	}

	public boolean uiComponentValueChanged() {
		return uiComponentValueChanged(true);
	}

	public void propagateChange() {
		if (!getPropagateChange())
			return;
		//does not make sense to go up the parent hierarchy in this case
		
		CompositeAdapter virtualParent = getVirtualParent();

		/*
		 * if (parent != null && !this.isTopAdapter()) {
		 * parent.uiComponentValueChanged(false); } else {
		 */
//		if (virtualParent.getRealObject() != virtualParent.computeAndMaybeSetViewObject())
//			return;
		if (virtualParent.getRealObject() == virtualParent.computeAndMaybeSetViewObject() && virtualParent != null && !virtualParent.isRootAdapter()) {

			virtualParent.uiComponentValueChanged(false);
		} else {
			if (uiFrame != null)
				uiFrame.doImplicitRefresh();
		}
	}
	
	

	public void commandActionPerformed() {
		// uiComponentValueChanged(false);
		propagateChange();
	}

	public Object getUserChange() {
		return getOriginalValue();
		// return getValue();
	}

	public CompositeAdapter getVirtualParent() {
		CompositeAdapter virtualParent;
		if (this.isTopAdapter()) {
			virtualParent = (CompositeAdapter) (getOriginalSourceAdapter()
					.getParentAdapter());

		} else {

			virtualParent = parent;

		}
		return virtualParent;

	}

	boolean receivedNotification = false;

	public boolean getReceivedNotification() {
		return receivedNotification;
	}

	public void haveReceivedNotification() {
		// bad form to this here, but convenient
		if (getImplicitRefreshOnNotification())
			return;
		receivedNotification = true;
		getUIFrame().setReceivedNotification(true);
	}

	void resetReceivedNotification() {
		receivedNotification = false;
	}

	public boolean uiComponentValueChanged(boolean initialChange) {

		// getUIFrame().doUpdateAll();
		if (recalculateRealObject())
			this.getUIFrame().doImplicitRefresh();
		// if (edited) {
		setEdited(false);

		// if (genericWidget == null) return;
		if (genericWidget != null)
			genericWidget.setUpdated();
		// }

		Object parentObject = parent.computeAndMaybeSetViewObject();
		// System.out.println("ui component value changed");
		if (parent == null) {
			System.out
					.println("Parent adaptor is NULL. Cannot set value of primitive type");
			if (getWidgetAdapter() != null)
				getWidgetAdapter().userInputUpdated(false);
			return false;
		}

		if (parentObject == null) {
			// realObject = getOriginalValue();
			// setRealObject(getOriginalValue());
			// setRealObject(getValue());
			if (!(this instanceof ShapeObjectAdapter))
				setViewObject(getOriginalValue());
			// viewObject = getOriginalValue();
			// viewObject = getValue();
			refreshConcreteObject(computeAndMaybeSetViewObject());
			if (uiFrame != null && !uiFrame.getRefreshWillHappen())
				uiFrame.doImplicitRefresh();
			if (getWidgetAdapter() != null)
				getWidgetAdapter().userInputUpdated(true);
			return true;
		}
		CompositeAdapter virtualParent = getVirtualParent();
		/*
		 * if (this.isTopAdapter()) { virtualParent = (uiContainerAdapter)
		 * (getOriginalSourceAdapter().getParentAdapter());
		 *  } else {
		 * 
		 * virtualParent = parent; }
		 */
		// System.out.println("uiCVC " + this.getID() + "VP " +
		// virtualParent.getID());
		Object newValue = getUserChange();
		if (newValue == null)
			newValue = bus.uigen.misc.OEMisc.defaultValue(getPropertyClass());
		/*
		 * realObject = getOriginalValue(); if (!(this instanceof
		 * uiShapeAdapter)) viewObject = getOriginalValue();
		 * refreshConcreteObject(newValue);
		 */
		boolean succeeded = virtualParent.childUIComponentValueChanged(this,
				newValue, initialChange);
		setRealObject(newValue);
		/*
		 * if (!succeeded) return;
		 */
		if (!getReceivedNotification()) {
			refreshConcreteObject(newValue);

			propagateChange();
		} else
			resetReceivedNotification();
		if (getWidgetAdapter() != null)
			getWidgetAdapter().userInputUpdated(false);
		return succeeded;

	}

	/*
	 * public void uiComponentValueChangedOld(boolean initialChange) {
	 * 
	 * 
	 * if (recalculateRealObject()) this.getUIFrame().doImplicitRefresh();; //if
	 * (edited) { setEdited(false); if (genericWidget == null) return;
	 * genericWidget.setUpdated(); //}
	 * 
	 * 
	 * Object parentObject = parent.getViewObject(); // System.out.println("ui
	 * component value changed"); if (getAdapterType() == PROPERTY_TYPE) {
	 * //System.out.println("prop type"); // Get the property value using a
	 * getValue() call // Find the write method of this property and // invoke
	 * this method on the parent object. //if (propertyWriteMethod != null) { if
	 * (!isReadOnly()) { //Object parentObject = parent.getViewObject(); Object
	 * newValue; if (this instanceof uiContainerAdapter) newValue =
	 * getRealObject(); else //Object newValue = getValue(); newValue =
	 * getOriginalValue(); set(newValue, initialChange);
	 * refreshConcreteObject(newValue); }} else // NOT SURE WHY THIS CHECK
	 * EXISTS. DELETING IT TO ALLOW PASTE { if (parent == null)
	 * System.out.println("Parent adaptor is NULL. Cannot set value of primitive
	 * type"); else { //Object parentObject = parent.getViewObject(); if
	 * (parentObject == null) { realObject = getOriginalValue(); // why assign
	 * this? if (!(this instanceof uiShapeAdapter)) viewObject =
	 * getOriginalValue(); if (uiFrame != null) uiFrame.doImplicitRefresh();
	 * return; } //factoring out code in all container branches
	 * 
	 * int index; uiContainerAdapter virtualParent = null; try { if
	 * (this.isTopAdapter()) { //System.out.println(this.getAdapterIndex());
	 * index = Integer.parseInt(this.getAdapterIndex()); virtualParent =
	 * (getOriginalSourceAdapter().getParentAdapter());
	 * System.out.println(virtualParent); } else {
	 * //System.out.println(parentAdapter.getChildAdapterIndex(this));
	 * //System.out.println(hashtableAdapter.getChildAdapterIndex(this));
	 * //index = Integer.parseInt(parentAdapter.getChildAdapterIndex(this));
	 * virtualParent = parent; index =
	 * Integer.parseInt(virtualParent.getChildAdapterIndex(this)); if (index <
	 * 0) { System.out.println("ADAPTER NOT FOUND"); System.out.println("ADAPTER " +
	 * this.getID()); System.out.println("PARENT " + virtualParent.getID());
	 * return; } } } catch (NumberFormatException e) { //e.printStackTrace();
	 * System.out.println(e); index = -1; }
	 * 
	 * 
	 * //if (uiBean.isHashtable(parentObject)) {
	 * 
	 * if (virtualParent instanceof uiHashtableAdapter) {
	 * 
	 * //uiHashtableAdapter hashtableAdapter = null; uiHashtableAdapter
	 * hashtableAdapter = (uiHashtableAdapter) virtualParent; HashtableStructure
	 * hashtableStructure = hashtableAdapter.getHashtableStructure(); // In
	 * these cases, we'll have to perform a // vector.setElementAt(). Object
	 * newValue = getOriginalValue(); //System.out.println("New componnet value" +
	 * newValue); Object key = null; Object oldKey = null; Object value = null;
	 * //uiContainerAdapter parentAdapter = (uiContainerAdapter)
	 * getParentAdapter(); //int index;
	 * 
	 * if (adaptorType == KEY_TYPE) {
	 * 
	 * 
	 * key = newValue; uiObjectAdapter valueAdapter =
	 * hashtableAdapter.getChildAdapterMapping(Integer.toString(index + 1));
	 * value = valueAdapter.getOriginalValue(); oldKey = valueAdapter.getKey();
	 * if (oldKey == null) return;
	 * 
	 * uiMethodInvocationManager.beginTransaction();
	 * hashtableStructure.remove(oldKey, hashtableAdapter); } else if
	 * (adaptorType == VALUE_TYPE) { value = newValue; keyAdapter =
	 * hashtableAdapter.getChildAdapterMapping(Integer.toString(index - 1)); key =
	 * keyAdapter.getOriginalValue(); }
	 * 
	 * hashtableStructure.put(key, value, hashtableAdapter);
	 * uiMethodInvocationManager.endTransaction(); } else if (virtualParent
	 * instanceof uiVectorAdapter) { uiVectorAdapter vectorAdapter =
	 * (uiVectorAdapter) virtualParent; // In these cases, we'll have to perform
	 * a // vector.setElementAt(). Object newValue = getOriginalValue();
	 * VectorStructure vectorStructure = vectorAdapter.getVectorStructure();
	 * //System.out.println("New componnet value" + newValue); if
	 * (vectorStructure == null) { System.out.println("Unexected null virtual
	 * structure"); return; } if (vectorStructure.canSetChild()) if (
	 * initialChange) // only initial propagating change needs to be logged
	 * vectorStructure.setElementAt(newValue, index, this); else
	 * vectorStructure.setElementAt(newValue, index); }
	 * 
	 * 
	 * else if (parentObject.getClass().isArray()) { uiArrayAdapter arrayAdapter =
	 * null; // In these cases, we'll have to perform a //
	 * vector.setElementAt(). Object newValue = getOriginalValue();
	 * //System.out.println("New componnet value" + newValue);
	 * uiContainerAdapter parentAdapter = (uiContainerAdapter)
	 * getParentAdapter(); //int index; try { if (this.isTopAdapter()) {
	 * //System.out.println(this.getAdapterIndex()); index =
	 * Integer.parseInt(this.getAdapterIndex()); arrayAdapter = (uiArrayAdapter)
	 * (getOriginalSourceAdapter().getParentAdapter()); } else { arrayAdapter =
	 * (uiArrayAdapter) parent;
	 * //System.out.println(parentAdapter.getChildAdapterIndex(this));
	 * //System.out.println(vectorAdapter.getChildAdapterIndex(this)); index =
	 * Integer.parseInt(parentAdapter.getChildAdapterIndex(this)); //index =
	 * Integer.parseInt(vectorAdapter.getChildAdapterIndex(this)); } } catch
	 * (NumberFormatException e) { //e.printStackTrace();
	 * System.out.println("Exception"); index = 0; } try {
	 * 
	 * 
	 * 
	 * System.out.println("Setting component "+index+" of vector to "+newValue);
	 * Array.set(parentObject, index, newValue);
	 *  // } catch (ArrayIndexOutOfBoundsException e) { } catch (Exception e) {
	 * System.out.println("Could not set " + index + "of" + parentObject + "to" +
	 * newValue); } } //else if (parentObject instanceof Hashtable) { //else if
	 * (parent instanceof uiHashtableAdapter) { } } //if (!initialChange)
	 * propagateChange();
	 * 
	 * //System.out.println ("generic widget" + getGenericWidget());
	 * //System.out.println("text adaoter" + this);
	 * //getTopAdapter(this).getGenericWidget().getUIFrame().doRefresh(); }
	 */
	public boolean recalculateRealObject() {
		return false;
	}

	public void padLabelTo(int max) {
		// System.out.println("Padding to " + max);
		/*
		 * JLabel labelComponent = getGenericWidget().getLabelComponent();
		 * labelComponent.setPreferredSize(new Dimension (100,
		 * labelComponent.getSize().height));
		 */
		if (getCopiedLabel())
			max++;
		String label = getGenericWidget().getLabel();
		// max = 100;
		// System.out.println(label + ":");
		// max += (int) ((max - label.length())*1.5);
		for (int i = label.length(); i < max; i++)
			label = label + " ";
		// System.out.println(label + ":");
		getGenericWidget().setLabel(label);
		// getGenericWidget().doLayout();

	}

	public String padLabel(String origLabel, int max) {
		// System.out.println("Padding to " + max);
		/*
		 * JLabel labelComponent = getGenericWidget().getLabelComponent();
		 * labelComponent.setPreferredSize(new Dimension (100,
		 * labelComponent.getSize().height));
		 */

		// max = 100;
		// System.out.println(label + ":");
		// max += (int) ((max - label.length())*1.5);
		String label = origLabel;
		if (getCopiedLabel())
			max++;
		for (int i = label.length(); i < max; i++)
			label = label + " ";
		// System.out.println(label + ":");
		return label;
		// getGenericWidget().doLayout();

	}

	public String padLabel(String label) {
		if (getParentAdapter() == null)
			return label;
		if (!(getParentAdapter() instanceof ClassAdapter))
			return label;
		int max = ((ClassAdapter) getParentAdapter())
				.getMaxComponentNameLength();
		return padLabel(label, max);
	}

	public void uiComponentValueChanged(Object source) {
		uiComponentValueChanged();
	}

	/*
	 * public static boolean checkMask (ActionEvent e, int mask) { int
	 * modifiers; //System.out.println("modifiers" + e.getModifiers() + "mask" +
	 * mask +":"); modifiers = e.getModifiers(); return (modifiers & mask) ==
	 * mask; }
	 * 
	 * public void actionPerformed(ActionEvent e) { }
	 * 
	 * 
	 * public void mouseClicked(MouseEvent e) { if
	 * (ASelectionTriggerMouseListener.checkMask(e, e.CTRL_MASK))
	 * addSelectionEvent(); //uiComponentFocusGained(); else if
	 * (ASelectionTriggerMouseListener.checkMask(e, e.SHIFT_MASK))
	 * extendSelectionEvent(); else replaceSelectionsEvent(); } public void
	 * mousePressed(MouseEvent e) { } public void mouseReleased(MouseEvent e) { }
	 * public void mouseMoved(MouseEvent e) { } public void
	 * mouseEntered(MouseEvent e) { } public void mouseExited(MouseEvent e) { }
	 */

	// Do any lockManager related things here
	// in these two methods.
	public void uiComponentFocusGained() {
		// getWidgetAdapter().setUIComponentSelected();
		if (getGenericWidget() != null
				&& getGenericWidget().getUIFrame() != null)
			SelectionManager.select(this);
		this.getUIFrame().setTitle();
		/*
		 * if (getGenericWidget() !=null && isEdited())
		 * getGenericWidget().editedUIComponentFocusGained();
		 */

	}
	boolean isComputedSelectable() {
		Boolean selectable = getComponentSelectable();
		if (selectable == null)
			selectable = isReadOnly();
		return (boolean) selectable;
		
	}
	public void replaceSelectionsEvent() {
		// getWidgetAdapter().setUIComponentSelected();
//		if (!isComputedSelectable())
//			return;
//		
		
		if (getGenericWidget() != null
				&& getGenericWidget().getUIFrame() != null)
			SelectionManager.replaceSelections(this);
		this.getUIFrame().setTitle();
		/*
		 * if (getGenericWidget() !=null && isEdited())
		 * getGenericWidget().editedUIComponentFocusGained();
		 */
	}

	public void addSelectionEvent() {
		// getWidgetAdapter().setUIComponentSelected();
		
		
		if (getGenericWidget() != null
				&& getGenericWidget().getUIFrame() != null)
			SelectionManager.addSelection(this);
		this.getUIFrame().setTitle();
		/*
		 * if (getGenericWidget() !=null && isEdited())
		 * getGenericWidget().editedUIComponentFocusGained();
		 */
	}

	public void extendSelectionEvent() {
		// getWidgetAdapter().setUIComponentSelected();
		if (getGenericWidget() != null
				&& getGenericWidget().getUIFrame() != null)
			SelectionManager.extendSelectionTo(this);
		this.getUIFrame().setTitle();
		/*
		 * if (getGenericWidget() !=null && isEdited())
		 * getGenericWidget().editedUIComponentFocusGained();
		 */
	}

	/*
	 * public static boolean checkMask (FocusEvent e, int mask) { int modifiers;
	 * //System.out.println("modifiers" + e.getModifiers() + "mask" + mask
	 * +":"); //modifiers = e.getModifiers(); if (modifiers == 0) modifiers =
	 * e.BUTTON1_MASK; return (modifiers & mask) == mask; }
	 */
	// This methoid is not useless
	public void uiComponentFocusGained(FocusEvent e) {

		// getWidgetAdapter().setUIComponentSelected();
		/*
		 * if (checkMask(e, e.ADJUSTMENT_EVENT_MASK))
		 * System.out.println("Adjustment event!");
		 */
		if (getGenericWidget() != null
				&& getGenericWidget().getUIFrame() != null)
			SelectionManager.select(this);
		this.getUIFrame().setTitle();
		/*
		 * if (getGenericWidget() !=null && isEdited())
		 * getGenericWidget().editedUIComponentFocusGained();
		 */

	}

	public void uiComponentFocusLost() {
		// commenting out this because button presses cause deselections
		/*
		 * if (getGenericWidget() !=null && isEdited())
		 * getGenericWidget().editedUIComponentFocusLost();
		 * uiSelectionManager.unselect();
		 */
		// getWidgetAdapter().setUIComponentDeselected();
	}

	boolean isAtomic = false;

	public void setAtomic(boolean newValue) {
		isAtomic = newValue;
	}

	public boolean isNull() {
		return computeAndMaybeSetViewObject() == null;
	}

	public boolean isAtomic() {
		return isAtomic /* || isNull() */
				|| isViewAtomic()
				|| unparseAsToString()
				|| (getWidgetAdapter() != null && getWidgetAdapter()
						.isComponentAtomic());
		/*
		 * Object obj = getObject(); Class inputClass; if (obj == null)
		 * inputClass = this.getPropertyClass(); else inputClass =
		 * obj.getClass(); if (inputClass == null) return true; return
		 * EditorRegistry.getEditorClass(inputClass) != null ;
		 */

	}

	boolean isRealAtomic() {
		// Object obj = getObject();
		Object obj = getRealObject();
		ClassProxy inputClass;
		if (obj == null)
			inputClass = this.getPropertyClass();
		else
			inputClass =  ReflectUtil.toMaybeProxyTargetClass(obj);
		if (inputClass == null)
			return true;
		return EditorRegistry.getEditorClass(inputClass) != null;

	}

	boolean isViewAtomic() {
		Object obj = computeAndMaybeSetViewObject();
		// Object obj = getRealObject();
		ClassProxy inputClass;
		if (obj == null)
			inputClass = this.getPropertyClass();
		else
			inputClass =  ReflectUtil.toMaybeProxyTargetClass(obj);
		if (inputClass == null)
			return true;
		return EditorRegistry.getEditorClass(inputClass) != null;

	}

	// duplicated from primitiveAdapter - move it to object adapter
	public void atomicSetValue(Object newValue1) {
		// System.out.println("primitive set value");

		// Object newValue = uiGenerator.getViewObject(newValue1);
		Object newValue;
		if (newValue1 != getRealObject())
			// Object newValue = getViewObject(newValue1);
			newValue = computeViewObject(getParentAdapter(), newValue1);
		else
			newValue = computeAndMaybeSetViewObject();
		/*
		 * if (setPreRead()) this.getGenericWidget().setPreRead();
		 * 
		 * else
		 */
		if (setPreWrite())
			this.getWidgetAdapter().setPreWrite();
		if (newValue == null) {
			if (getWidgetAdapter().getUIComponentValue() != null)
				getWidgetAdapter().setUIComponentValue(newValue);
		} else if (newValue.getClass().equals(getPropertyClass())) {
			Object oldValue = getWidgetAdapter().getUIComponentValue();
			if (!(this instanceof PrimitiveAdapter)
					|| !(oldValue.equals(newValue)))
				// if
				// (!(getWidgetAdapter().getUIComponentValue().equals(newValue)))
				getWidgetAdapter().setUIComponentValue(newValue);
			// System.out.println("Replaced with "+newValue);
		} else {
			// Replace this attributed object with the correct one
			if (getParentAdapter() instanceof ReplaceableChildren)
				((ReplaceableChildren) getParentAdapter())
						.replaceAttributedObject(this, newValue);
		}
	}

	public void setValueOfAtomicOrPrimitive(Object newValue1) {
		// adding this may cause problems and it did
		// loks like previous object is not kept properly
		// if (newValue1.equals(getValue())) return;
		// First check if the type of newValue
		// matches the type this attributed object represents
		// System.out.println("primtive set value");
		// Object newValue = uiGenerator.getViewObject(newValue1);
		// if (!setRealObject(newValue1)) return;
		setRealObject(newValue1);
		Object newValue = computeAndMaybeSetViewObject();
		// setViewObject(uiGenerator.getViewObject(getRealObject()));
		// setViewObject(getViewObject(getRealObject()));
		// refreshConcreteObject(getViewObject());

		// setViewObject(getViewObject(newValue1));
		// if (getWidgetAdapter() == null) return;
		/*
		 * if (setPreRead()) this.getGenericWidget().setPreRead(); else if
		 * (setPreWrite()) this.getWidgetAdapter().setPreWrite();
		 */
		propagatePreConditions();
		
		if (getWidgetShell() != null && getWidgetShell().isElided() && unparseAsToString()) {
			getWidgetShell().refreshElideComponent();
			return;
		}
		// Object newValue = getViewObject(newValue1);
		/*
		 * if (newValue == null) { if (getWidgetAdapter().getUIComponentValue() !=
		 * null) getWidgetAdapter().setUIComponentValue(newValue); } else if
		 * (newValue.getClass().equals(getPropertyClass())) {
		 * //System.out.println("calling setUIComp value"); if
		 * (!getWidgetAdapter().getUIComponentValue().equals(newValue.toString()) &&
		 * !isEdited()) { getWidgetAdapter().setUIComponentValue(newValue);
		 * //System.out.println("Replaced with "+newValue); } } else { //
		 * Replace this attributed object with the correct one if
		 * (getParentAdapter() instanceof uiReplaceableChildren)
		 * ((uiReplaceableChildren)
		 * getParentAdapter()).replaceAttributedObject(this, newValue); }
		 */
		if (haveChangedClass(newValue)) {
			if (getParentAdapter() instanceof ReplaceableChildren)
				((ReplaceableChildren) getParentAdapter())
						.replaceAttributedObject(this, newValue);
		} else
			refreshUIComponentOfSameClass(newValue);

		/*
		 * if (isNameChild) { ((uiContainerAdapter)
		 * getParentAdapter()).nameChildChanged((String) newValue); }
		 */
		// processNameChild(newValue);
	}

	public boolean haveChangedClass(Object newValue) {
		if (newValue == null)
			return false;
		// return !(newValue.getClass().equals(getPropertyClass()));
		if (newValue instanceof ACompositeLoggable)  {
			ACompositeLoggable composite = (ACompositeLoggable) newValue;
			Object realValue = composite.getRealObject();
			return !( ReflectUtil.toMaybeProxyTargetClass(realValue).equals(getPropertyClass()));
		} else if (newValue instanceof IdentifiableLoggable) {
			Tracer.warning("Identifiable Loggable may cause problems");
		}
			
		return !( ReflectUtil.toMaybeProxyTargetClass(newValue).equals(getPropertyClass()));
	}

	public void refreshUIComponentOfSameClass(Object newValue) {
		if (getWidgetAdapter() == null)
			return;
		// commenting out this because the UI component may not be in a generic
		// widget
		// if (getGenericWidget() == null) return;
		if (getWidgetAdapter().getUIComponent() == null)
			return;

		if (newValue == null) {
			if (getWidgetAdapter().getUIComponentValue() != null)
				getWidgetAdapter().setUIComponentValue(newValue);
			// } else if
			// (!getWidgetAdapter().getUIComponentValue().equals(newValue.toString())
			// && !isEdited()) {
		} else if (uiChanged(newValue)) {
			getWidgetAdapter().setUIComponentValue(newValue);
			// System.out.println("Replaced with "+newValue);
		}
	}

	public boolean isEnum() {
		return getConcreteObject() instanceof EnumToEnumeration;
		/*
		 * if (!haveSetModel) return false; return
		 *  IntrospectUtility.toMaybeProxyTargetClass(currentModel).isEnum();
		 */
	}

	public boolean uiChanged(Object newValue) {
		// the following optimization makes lazy programs not work as some properties may not notified
//		if (getUIFrame().isProcessingSuppressedNotifications() && !hasPendingValue())
//			return false;
		if (this instanceof CompositeAdapter)
			return true;
		if (getWidgetAdapter() == null || getWidgetAdapter().getUIComponentValue() == null )
			return true;
		
		String oldString = getWidgetAdapter().getUIComponentValue().toString();
		String newString = newValue.toString();
		return !oldString.equals(newString) && !isEdited();

	}

	public boolean isParentedTopAdapter() {
		ObjectAdapter parent = this.getParentAdapter();
		return (this.isTopAdapter() && parent != null && parent.getValue() != null /*
																					 * &&
																					 * this.propertyClass !=
																					 * null
																					 */);
	}

	public void refresh() {
		setEdited(false);
		implicitRefresh(true);

	}

	public int getMinimumHeight() {

		return 0;
	}
	public void implicitRefresh(boolean adapterInitiated) {
		// System.out.println(" normal objectadapter refresh" + this);
		// if (isAtomic() || isParentedTopAdapter()){
		
		
		// the first check is redudant, but might remove it later
		if (getReceivedNotification() || registeredAsListener ) {
			resetReceivedNotification();
			if (!getUIFrame().isFullRefresh()) {
//				Tracer.info(this, "Not refreshing because registered as listener ");
				getUIFrame().checkPreInMenuTreeAndButtonCommands();
				// if preconditions have changed may also need to hide or show elements so commenting the return
//				return;
				if (!adapterInitiated && !getUIFrame().checkPreOnImplicitRefresh())
					return;
			}
		}
		if (isParentedTopAdapter()) {
			atomicRefresh();
			return;
		}
		if (getUIFrame() == null) {
			Tracer.error ("NUll UI frame for" + getRealObject());
			return;
		}
		// setValue(getViewObject());
		refreshValue(getRealObject(), getUIFrame().isFullRefresh());
//		if (getUIFrame().isFullRefresh()) {
//			refreshAttributes();
//		}
		if (isAtomic())
			atomicRefresh();
		
	}

	public void propagatePreConditions() {
		if (getGenericWidget() == null)
			return;
		
//		if (getWidgetAdapter() != null)
//			this.getWidgetAdapter().setPreWrite(); // not sure we need this
		// actually
		if (isReadOnly())
			setUneditable();
		else {
			setEditable();
			if (getWidgetAdapter() != null)
				this.getWidgetAdapter().setPreWrite(); // not sure we need this
		}
		// }
	}

	public void recalculateViewObject() {
		// setViewObject(uiGenerator.getViewObject(getRealObject()));
		// setViewObject(getViewObject(getRealObject()));
		refreshConcreteObject(computeAndMaybeSetViewObject());
	}

	public void recalculateConcreteObject() {
		// setViewObject(uiGenerator.getViewObject(getRealObject()));
		// setViewObject(getViewObject(getRealObject()));
		refreshConcreteObject(computeAndMaybeSetViewObject());
	}

	public void atomicRefresh() {
		recalculateViewObject();
		propagatePreConditions();
		/*
		 * if (setPreRead()) this.getGenericWidget().setPreRead(); else if
		 * (setPreWrite()) this.getWidgetAdapter().setPreWrite();
		 */
		edited = false;
		if (genericWidget != null)
			genericWidget.setUpdated();
		// if (!isTopAdapter())
		updateParentComponentAndUpdateWidgetAdapter();

		/*
		 * uiContainerAdapter virtualParent = getVirtualParent();
		 * 
		 * Object newValue = virtualParent.getChildValue(this); if (newValue ==
		 * null) return; if (isAtomic()) {
		 * getWidgetAdapter().setUIComponentValue(getViewObject(newValue));
		 * return; } else setValue(newValue);
		 */
	}

	Object newValue() {

		CompositeAdapter virtualParent = getVirtualParent();
		/*
		 * if (this.isTopAdapter()) { virtualParent = (uiContainerAdapter)
		 * (getOriginalSourceAdapter().getParentAdapter());
		 *  } else {
		 * 
		 * virtualParent = parent; // will this option be exercised? }
		 */
		Object newValue;
		// maybe this should only be done for primitive objects
		// if (isTopAdapter()) {
		if (virtualParent == null || virtualParent.isRootAdapter()) {
			newValue = computeAndMaybeSetViewObject();
		} else {
			newValue = virtualParent.getChildValue(this);

		}
		return newValue;

	}

	public void updateParentComponentAndUpdateWidgetAdapter() {

		CompositeAdapter virtualParent = getVirtualParent();
		/*
		 * //if (this.isTopAdapter()) { // virtualParent = (uiContainerAdapter)
		 * (getOriginalSourceAdapter().getParentAdapter()); // //} else { //
		 * virtualParent = parent; // will this option be exercised?
		 * 
		 * //} // Object newValue;
		 */
		Object newValue = newValue();
		if (newValue == null)
			return;
		/*
		 * // maybe this should only be done for primitive objects //if
		 * (isTopAdapter()) { if (virtualParent == null) { newValue =
		 * getViewObject(); } else { newValue =
		 * virtualParent.getChildValue(this); if (newValue == null) return; }
		 */
		if (isAtomic()) {
			/*
			 * if (getRealObject() != newValue)
			 * getWidgetAdapter().setUIComponentValue(getViewObject(getParentAdapter(),
			 * newValue)); else
			 * getWidgetAdapter().setUIComponentValue(getViewObject());
			 */
			updateWidgetAdapter(newValue);
			return;

		} else
			refreshValue(newValue);
	}

	public void updateWidgetAdapter(Object newValue) {

		if (getWidgetAdapter() == null)
			return;

		if (getRealObject() != newValue)
			getWidgetAdapter().setUIComponentValue(
					computeViewObject(getParentAdapter(), newValue));
		else
			getWidgetAdapter().setUIComponentValue(computeAndMaybeSetViewObject());

	}

	public void oldAtomicRefresh() {
		recalculateViewObject();
		if (setPreRead())
			this.getGenericWidget().setPreRead();
		else if (setPreWrite())
			this.getWidgetAdapter().setPreWrite();
		// System.out.println("uiObjectAdapter: atomic refresh" + this.getPath()
		// + this);
		// System.out.println("adapter field" + getAdapterField());
		edited = false;
		if (genericWidget != null)
			genericWidget.setUpdated();
		Object parentObject = parent.computeAndMaybeSetViewObject();
		// System.out.println("atomicRefresh: " + parentObject);
		// Object parentObject = parent.getRealObject();
		if (getAdapterType() == PROPERTY_TYPE) {
			// System.out.println("prop type");
			// Get the property value using a getValue() call
			// Find the write method of this property and
			// invoke this method on the parent object.
			Object newValue = get();
			// if (propertyReadMethod != null) {
			if (newValue != null) {
				// Object parentObject = parent.getViewObject();
				// Object newValue;
				/*
				 * Object parms[] = {}; try { newValue =
				 * propertyReadMethod.invoke(parentObject, parms);
				 */
				// System.out.println("parent" + parentObject + " new " +
				// newValue + " old " + getValue());
				// if (newValue == getValue()) return;
				// System.out.println("widget adapter" + getWidgetAdapter());
				if (isAtomic())
					// getWidgetAdapter().setUIComponentValue(newValue);
					// getWidgetAdapter().setUIComponentValue(uiGenerator.getViewObject(newValue));
					getWidgetAdapter().setUIComponentValue(
							computeViewObject(newValue));
				else {

					refreshValue(newValue);
				}
				/*
				 * } catch (Exception e) { System.out.println("Exception occured
				 * while trying to call "+propertyReadMethod.getName()+" on
				 * "+parentObject); e.printStackTrace(); }
				 */
			}

		} else {
			// System.out.println("not prop type: vectror element");
			// Object parentObject = parent.getViewObject();
			if (parentObject instanceof Vector) {
				// In these cases, we'll have to perform a
				// vector.getElementAt().
				Object newValue;
				int index;
				try {
					// System.out.println(this.getAdapterIndex());
					index = Integer.parseInt(this.getAdapterIndex());
				} catch (NumberFormatException e) {
					// e.printStackTrace();
					System.out.println("Exception");
					index = 0;
				}
				try {

					// System.out.println("Getting component "+index+" of
					// vector" + parentObject);
					newValue = ((Vector) parentObject).elementAt(index);
					// System.out.println("new value" + newValue + "old value" +
					// getValue());
					// check here if they are deep equal!
					// if (newValue == getValue()) return;
					if (!(this instanceof PrimitiveAdapter))
						refreshValue(newValue);
					else
						getWidgetAdapter().setUIComponentValue(newValue);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				/*
				 * } else if (this.getAdapterField() != null ){
				 * //System.out.println("trying field"); try { Object newValue =
				 * getAdapterField().get(parentObject); // Lookup the adaptor
				 * corresponding to this field. // till here //if (newValue ==
				 * getValue()) return; //System.out.println ("new value" +
				 * newValue); if (isAtomic())
				 * //getWidgetAdapter().setUIComponentValue(newValue);
				 * //getWidgetAdapter().setUIComponentValue(uiGenerator.getViewObject(newValue));
				 * getWidgetAdapter().setUIComponentValue(getViewObject(newValue));
				 * else setValue(newValue); } catch (Exception e) {
				 * System.out.println("Exception occured when setting" +
				 * getAdapterField()); System.out.println(e); }
				 */

			} else {
				// getWidgetAdapter().setUIComponentValue(getRealObject());
				getWidgetAdapter().setUIComponentValue(computeAndMaybeSetViewObject());
			}

			// else refresh();

		}
	}

	transient Vector mergedAttributeList;

	Object getDefaultAttributeValue(String name) {
		return AttributeNames.getDefault(name);

	}
	
	
	 Object maybeTransformAttributeValue(String anAttributeName, Object obj) {
		if (!(obj instanceof String))
			return  obj;
		String string = ((String) obj).trim();
		if (!string.startsWith(AttributeNames.DEPENDENCY_PREFIX)) 
			return obj;
		String targetComponentName = getComponentName();
		string = string.replaceAll(AttributeNames.COMPONENT_NAME, targetComponentName).toLowerCase();
		
	
			String path = string.substring(AttributeNames.DEPENDENCY_PREFIX.length());
			
//			CompositeAdapter parent = 
	
		CompositeAdapter ancestor = getParentAdapter();
//		CompositeAdapter ancestor =  (CompositeAdapter) getTopAdapter();
//		if (ancestor != null) {
//			if (!path.startsWith(AttributeNames.PATH_SEPARATOR))
//				path = AttributeNames.PATH_SEPARATOR + path;
//			ObjectAdapter targetAdapter = pathToObjectAdapter(path);
//			if (targetAdapter != null)
//			return targetAdapter.getRealObject();
//		}
//		
		
		String suffix = path;

		int upPatternIndex = 0;
		while (true) {
			if (ancestor == null) {
				Tracer.error("Illegal path suffix:"  + suffix);
			}
			upPatternIndex = suffix.indexOf( UP_PATTERN, upPatternIndex);
			
			if (upPatternIndex < 0)
				break;
//			suffix = suffix.substring(upPatternIndex);
			ancestor = ancestor.getParentAdapter();	
			upPatternIndex += UP_PATTERN.length();
			suffix = suffix.substring(upPatternIndex);

		}
		
		String componentPath = suffix;
		String componentParentPath = null;
		CompositeAdapter componentParent = ancestor;

		int propertyIndex = suffix.lastIndexOf(AttributeNames.PATH_SEPARATOR) + AttributeNames.PATH_SEPARATOR.length();
		
		if (propertyIndex > 0) {
			componentPath = suffix.substring(propertyIndex);
			componentParentPath = suffix.substring(0, propertyIndex);
			if (!componentParentPath.startsWith(AttributeNames.PATH_SEPARATOR))
				componentParentPath = AttributeNames.PATH_SEPARATOR + componentParentPath;
			componentParent = (CompositeAdapter) pathToObjectAdapter(ancestor, componentParentPath);			
		}
		if (componentParent != null) {
//			if (!componentPath.startsWith(AttributeNames.PATH_SEPARATOR))
//				componentPath = AttributeNames.PATH_SEPARATOR + componentPath;
			ObjectAdapter targetAdapter = componentParent.getVisibleOrInvisibleChildAdapterAtRealIndex(componentPath);
			if (targetAdapter != null) {
				Object dependentValue =  targetAdapter.getRealObject();
				setLocalAttribute(new Attribute(anAttributeName, dependentValue ));
				componentParent.addAttributeDependency(new AnAttributeDependency(this, anAttributeName));
				return dependentValue;
			}
//			Object componentValue = componentParent.getComponentValue(componentPath);
//			return componentValue;
		}
		
		return null;
		
	}
	 

	public Object getMergedTempOrDefaultAttributeValue(String name) {
		Object retVal = getMergedAttributeValue(name);
		if (retVal != null)
			return retVal;
		return getTempAttributeValue(name);

	}

	public static Object getAttribute(ClassProxy theTargetClass,
			String attribute) {
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(theTargetClass);
		Object value = cd.getAttribute(attribute);
		if (value == null) {
			value = AttributeNames.getDefault(attribute);
			if (value == null)
				value = AttributeNames.getSystemDefault(attribute);
		}
		return value;

	}
	public static Object getAttribute(CompositeAdapter parentAdapter,
			ClassProxy theTargetClass,
			String attribute,
			String propertyName) {
		if (parentAdapter == null || propertyName == null)
			return getAttribute(theTargetClass, attribute);
		Object retVal = null;
		ClassProxy propertyClass = parentAdapter.getPropertyClass();
		if (propertyClass == null)
			return getAttribute(theTargetClass, attribute);
		Vector<Attribute> fieldAttributes = AttributeManager.getEnvironment().getClassAttributes(
				uiClassFinder.getName(propertyClass), propertyClass);
		//Vector<Attribute> fieldAttributes = getFieldAttributes(parentAdapter, propertyName);
		if (fieldAttributes != null)
			retVal = getAttributeValue(fieldAttributes, propertyName + AttributeNames.PATH_SEPARATOR + attribute);
		if (retVal != null)
			return retVal;
		return getAttribute(theTargetClass, attribute);		

	}

	public Object getComputedAttributeValue(String name) {
		Object retVal = getMergedTempOrDefaultAttributeValue(name);
		if (retVal != null)
			return retVal;
		if (name.equals(AttributeNames.LABEL))
			return getComputedLabel();
		if (name.equals(AttributeNames.POSITION))
			return getComputedPosition();
		if (name.equals(AttributeNames.LABEL_ABOVE))
			return getComputedLabelAbove();
		if (name.equals(AttributeNames.LABEL_BELOW))
			return getComputedLabelBelow();
		if (name.equals(AttributeNames.LABEL_LEFT))
			return getComputedLabelLeft();
		if (name.equals(AttributeNames.LABEL_RIGHT))
			return getComputedLabelRight();
		if (name.equals(AttributeNames.PRIMARY_PROPERTY))
			return getComputedPrimaryProperty();
		if (name.equals(AttributeNames.SORT_PROPERTY))
			return getComputedSortProperty();
		if (name.equals(AttributeNames.STRETCH_UNBOUND_COLUMNS))
			return getComputedStretchUnboundColumns();
		if (name.equals(AttributeNames.STRETCH_UNBOUND_ROWS))
			return getComputedStretchUnboundRows();
		return null;

	}
	public Object getMergedAttributeValue(String name) {
		Object untransformed = getUntransformedMergedAttributeValue(name);
		Object tranformed = maybeTransformAttributeValue(name, untransformed);
		return tranformed;
	}

	
	public Object getUntransformedMergedAttributeValue(String name) {
		uiFrame frame = getUIFrame();
		Object retVal = getNonDefaultMergedAttributeValue(name);
		if (retVal != null)
			return retVal;
		if (frame != null)
			retVal = frame.getDefaultAttribute(name);
		if (retVal == null)
			return getDefaultAttributeValue(name);
		return retVal;
		/*
		 * if (retVal == null) return defaultAttributeValue (name); else return
		 * retVal;
		 */
		/*
		 * if (parent == null) return defaultAttributeValue (name); if
		 * (mergedAttributeList == null ) mergedAttributeList = getAttributes();
		 * if (mergedAttributeList == null) return defaultAttributeValue (name);
		 * for (int i=0; i< mergedAttributeList.size(); i++) { Attribute a =
		 * (Attribute) mergedAttributeList.elementAt(i); if
		 * (a.getName().equals(name)) return a.getValue(); } return
		 * defaultAttributeValue (name);
		 */
	}

	public Object getNonDefaultMergedLabel() {
		Object label = getNonDefaultMergedAttributeValue(AttributeNames.LABEL);
		if (label == null)
			return null;
		if (!this.isKeyAdapter() && !this.isValueAdapter())
			return label;
		else if (this.isKeyAdapter()) {
			if (label.equals("Key"))
				return null; // set by VienInfoToUIClass
			else
				return label;
		} else {
			if (label.equals("Value"))
				return null; // set by VienInfoToUIClass
			else
				return label;
		}
	}

	public void resetMergedAttributes() {
		mergedAttributeList = null;
	}
	
	public static Object getAttributeValue(Vector<Attribute> attributeList, String name) {
		if (attributeList == null || name == null) return null;
		for (int i = 0; i < attributeList.size(); i++) {
			Attribute a = (Attribute) attributeList.elementAt(i);
			if (a.getName().equals(name))
				return a.getValue();
		}
		return null;		
	}
	
	public static Object searchAttribute (String name, Vector<Attribute> attributes) {
		if (attributes == null) return null;
		for (int i = 0; i < attributes.size(); i++) {			
			Attribute a = attributes.elementAt(i);			
			if (a.getName().equals(name))
				return a.getValue();		
		}
		return null;
		
	}

	public Object getNonDefaultMergedAttributeValue(String name) {
		/*
		 * if (name.equals(AttributeNames.LABEL) && toStringAsLabel() &&
		 * getRealObject() != null) return getRealObject().toString();
		 */
		// if (parent == null) return null;
		if (parent == null)
			return getDefaultAttributeValue(name);
		if (mergedAttributeList == null /* || mergedAttributeList.size() == 0 */)
			mergedAttributeList = getAttributes();
		if (mergedAttributeList == null)
			return getDefaultAttributeValue(name);
		try {
			if (mergedAttributeList == null) {
				Tracer.error("Merged AttributeList is null at start of loop");
				return null;
			}
				
		for (int i = 0; i < mergedAttributeList.size(); i++) {
			if (mergedAttributeList == null) { // dont understand when this will happen 
				Tracer.error("Merged AttributeList is null on iteration: " + i);
				return null;
			}
			try {
			Attribute a = (Attribute) mergedAttributeList.elementAt(i);
			
			if (a.getName().equals(name))
				return a.getValue();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
		// return defaultAttributeValue (name);
	}

	public Object getAttributeValue(String name) {
		for (int i = 0; i < localAttributeList.size(); i++) {
			Attribute a = (Attribute) localAttributeList.elementAt(i);
			if (a.getName().equals(name))
				return a.getValue();
		}
		return null;
	}

	public void setLocalAttribute(Attribute attrib) {
		for (int i = 0; i < localAttributeList.size(); i++) {
			Attribute a = (Attribute) localAttributeList.elementAt(i);
			if (a.getName().equals(attrib.getName())) {
				a.setValue(attrib.getValue());
				return;
			}
		}
		localAttributeList.addElement(attrib);
		return;
	}

	public void setLocalAttribute(String name, Object val) {
		Attribute a = new Attribute(name, val, Attribute.LOCAL_TYPE);
		a.CHANGED = true;
		setLocalAttribute(a);
	}

	private void printList(Vector list) {
		for (int i = 0; i < list.size(); i++) {
			Attribute att = (Attribute) list.elementAt(i);
			System.out.println(att.getName() + "=" + att.getValue());
		}
	}

	public Vector getLocalAttributes() {
		return localAttributeList;
	}

	protected static Vector getFieldAttributes(Vector attributes, String field) {
		Vector fieldAttributes = new Vector();
		for (int i = 0; i < attributes.size(); i++) {
			Attribute a = (Attribute) attributes.elementAt(i);
			String afield = a.getFieldName();
			if (afield != null
					&& afield.toLowerCase().equals(field.toLowerCase())) {
				Attribute b = new Attribute(a.getAttributeName(), a.getValue());
				fieldAttributes.addElement(b);
				// System.out.println("Adding attribute
				// "+b.getName()+"="+b.getValue());
			}
		}
		return fieldAttributes;
	}

	protected  Vector getSingleLevelPathAttributes(Vector attributes,
			Vector specificVector, int ancestorLevel) {
		Vector pathAttributes = new Vector();
		//reversing this so that the most specific atributes win
		for (int i = attributes.size() - 1; i >= 0; i--) {
		//for (int i = 0; i < attributes.size(); i++) {
			Attribute a = (Attribute) attributes.elementAt(i);
			String genericPath = a.getFieldName();
			// why was this there, it is the one that describes specifics
			// commenting it so specific can override generic if placed afterwards
//			if (genericPath != null && !genericPath.equals("*")
//					&& genericPath.indexOf('.') == -1) 
//				continue;

			if (genericPath != null
					&& pathMatch(genericPath, specificVector, ancestorLevel)) {
				Attribute b = new Attribute(a.getAttributeName(), a.getValue());
				pathAttributes.addElement(b);
				// System.out.println("Adding attribute
				// "+b.getName()+"="+b.getValue());
			}
		}
		return pathAttributes;
	}
	

	// ancestor level = 2, path < 2, does not apply
	// ancestor level = 2, path > 2, does not apply
	// ancestor level = 3, path < 3, does not apply
	// generic path should be exactly ancestor level long
	// specific path length is >= ancestor level

	public  boolean pathMatch(String genericPath, Vector specificVector,
			int ancestorLevel) {
		Vector genericVector = pathToVector(genericPath.toLowerCase());
		// Vector specificVector = pathToVector(specificPath.toLowerCase());
		// if (genericVector.size() + ancestorLevel != specificVector.size())
		int genericSize = genericVector.size();
		if (genericSize != ancestorLevel)
			return false;

		// int specificSize = specificVector.size();
		int specificOffset = specificVector.size() - genericSize;
		for (int i = 0; i < genericVector.size(); i++) {
			Object genericComponent = genericVector.get(i);
			if (genericComponent.equals(
					specificVector.get(i + specificOffset)))
				continue;
			else if (genericComponent.equals(AttributeNames.ANY_COMPONENT))
				continue;
			else if (this.isVectorChild() && genericComponent.equals(AttributeNames.ANY_ELEMENT) )
				continue;
			else if (this.isKeyAdapter() && genericComponent.equals(AttributeNames.ANY_KEY))
				continue;
			else if (this.isValueAdapter() && genericComponent.equals(AttributeNames.ANY_VALUE))
				continue;
			else
				return false;
		}
		return true;
	}

	Vector addAttributes(Vector original, Vector specialised) {
		if (original == null)
			return specialised;
		if (specialised == null)
			return original;
		CopyAttributeVector.mergeAttributeLists(original, specialised);
		return original;

	}

	public void setDefinitionKind(Vector<Attribute> theAttributes,
			DefinitionKind theDefinitionKind) {
		for (int i = 0; i < theAttributes.size(); i++) {
			theAttributes.elementAt(i).setDefinitionKind(theDefinitionKind);
		}

	}
	
	public static Vector<Attribute> getFieldAttributes (CompositeAdapter parentAdapter, String propertyName) {
		Vector<Attribute> temp1 = null;
		Vector<Attribute> retVal = null;
		if (parentAdapter != null) {
			//temp1 = parentAdapter.getLocalAttributes();
			temp1 = parentAdapter.getLocalAttributes();
			retVal = getFieldAttributes(temp1, propertyName);
			//CopyAttributeVector.mergeAttributeLists(attributes, temp);
		}
		return retVal;
	}
	
	public Vector<Attribute> getMultiLevelPathAttributes () {
		Vector pathVector = pathToVector(getPath().toLowerCase());
		return getMultiLevelPathAttributes(pathVector);

	}
	
	public Object getMultiLevelPathAttributeOfMethod(String method, String attributeName) {
		 Vector<Attribute> attributes = getMultiLevelPathAttributesOfMethod(method);
		 if (attributes == null)
			 return null;
		 return getAttributeValue(attributes, attributeName);
	}
	
	public Vector<Attribute> getMultiLevelPathAttributesOfMethod (String method) {
		Vector pathVector =  pathToVector(getPath().toLowerCase());
		pathVector.add(method);
		Vector<Attribute> retVal = getMultiLevelPathAttributes(pathVector, 2);
		return retVal;		
	}
	// reset by recomputeAttributes
	Map<String, Vector<Attribute>> pathToAttributes = new Hashtable();
	
	public Vector<Attribute> getMultiLevelPathAttributes (Vector pathVector) {
		return getMultiLevelPathAttributes(pathVector, 1);
	}

	// path may be to the object adapter or one of its methods so we can
	// integrate property and method attributes
		
	public Vector<Attribute> getMultiLevelPathAttributes (Vector pathVector, int initialLevel) {
		
		
		Vector<Attribute> retVal, pathFromClassAttributes, classAtributes;
		String pathString = pathVector.toString();
		retVal = pathToAttributes.get(pathString);
		if (retVal != null)
			return retVal;

		CompositeAdapter ancestorAdapter = getParentAdapter();

//		int ancestorLevel = 1;
		int ancestorLevel = initialLevel;
		//retVal = null;
		//Vector pathVector = pathToVector(getPath().toLowerCase());
		while (ancestorAdapter != null
				&& !(ancestorAdapter instanceof RootAdapter)) {
			ClassProxy ancestorPropertyClass = ancestorAdapter
					.getPropertyClass();
			classAtributes = AttributeManager.getEnvironment().getClassAttributes(
					uiClassFinder.getName(ancestorPropertyClass),
					ancestorPropertyClass);
			//moving this up
			//Vector pathVector = pathToVector(getPath().toLowerCase());
			pathFromClassAttributes = getSingleLevelPathAttributes(classAtributes, pathVector, ancestorLevel);
			retVal = addAttributes(retVal, pathFromClassAttributes);
			ancestorLevel++;
			ancestorAdapter = ancestorAdapter.getParentAdapter();
		}
		if (retVal == null)
			retVal = new Vector();
		pathToAttributes.put(pathString, retVal);
		return retVal;
	}

	public Vector<Attribute> getAttributes() {
		Vector<Attribute> attributes, temp, temp1;
		attributes = null;
		// First try any Class attributes
		// System.out.println("get attributs");
		if (AttributeManager.getEnvironment() == null)
			System.out.println("null environment");
		// System.out.println("got environment");
		// attributes =
		// CopyAttributeVector.copyVector(AttributeManager.getEnvironment().getClassAttributes(getPropertyClass().getName()));
		// attributes =
		// CopyAttributeVector.copyVector(AttributeManager.getEnvironment().getClassAttributes(getPropertyClass().getName(),
		// getPropertyClass() ));

		if (getRealObject() != null && getConcreteObject() != null) {
//			ClassProxy realClass =  IntrospectUtility.toMaybeProxyTargetClass(getRealObject());
			ClassProxy realClass = ReflectUtil.toMaybeProxyTargetClass(getRealObject());
			// this looks wrong, no use of inheritance while get class attributes does use inheritance. These two should be merged
			Vector<Attribute> classIntAttributes = getConcreteObject()
					.getAttributes();
			attributes = CopyAttributeVector.copyVector(AttributeManager
					.getEnvironment().getClassAttributes(
							uiClassFinder.getName(realClass), realClass));
			attributes = addAttributes(classIntAttributes, attributes);
			/*
			 * if (classIntAttributes != null) {
			 * CopyAttributeVector.mergeAttributeLists(classIntAttributes,
			 * attributes); attributes = classIntAttributes; }
			 */
			setDefinitionKind(attributes, DefinitionKind.Class);
			String realVirtualClassString = IntrospectUtility
					.getVirtualClass(getRealObject());
			ClassProxy virtualClass = null;

			if (realVirtualClassString != null)
				try {
					virtualClass = RemoteSelector
							.forName(realVirtualClassString);
					/*
					 * // so the virtual class is an independent class in its
					 * own right. Get its attributes Vector
					 * actualVirtualClassAttributes =
					 * CopyAttributeVector.copyVector(AttributeManager.getEnvironment().getClassAttributes(uiClassFinder.getName
					 * (virtualClass), virtualClass ));
					 * CopyAttributeVector.mergeAttributeLists(attributes,
					 * actualVirtualClassAttributes);
					 */
				} catch (Exception e) {

				}
			if (realVirtualClassString != null /* && virtualClass == null */) {
				Vector realVirtualClassAttributes = CopyAttributeVector
						.copyVector(AttributeManager.getEnvironment()
								.getVirtualClassAttributes(
										realVirtualClassString));
				setDefinitionKind(realVirtualClassAttributes,
						DefinitionKind.VirtualClass);
				CopyAttributeVector.mergeAttributeLists(attributes,
						realVirtualClassAttributes);
			} else if (virtualClass != null) {
				// try {
				// Class virtualClass = Class.forName(realVirtualClassString);

				// so the virtual class is an independent class in its own
				// right. Get its attributes
				Vector actualVirtualClassAttributes = CopyAttributeVector
						.copyVector(AttributeManager.getEnvironment()
								.getClassAttributes(
										uiClassFinder.getName(virtualClass),
										virtualClass));
				setDefinitionKind(actualVirtualClassAttributes,
						DefinitionKind.VirtualClass);
				CopyAttributeVector.mergeAttributeLists(attributes,
						actualVirtualClassAttributes);
			}
			// } catch (Exception e) {

			// }
			// }

		} else if (propertyClass != null) {
			attributes = CopyAttributeVector.copyVector(AttributeManager
					.getEnvironment()
					.getClassAttributes(uiClassFinder.getName(propertyClass),
							propertyClass));
		}
		if (computeAndMaybeSetViewObject() != getRealObject()) {
			ClassProxy viewClass =  ReflectUtil.toMaybeProxyTargetClass(computeAndMaybeSetViewObject());
			Vector viewClassAttributes = CopyAttributeVector
					.copyVector(AttributeManager
							.getEnvironment()
							.getClassAttributes(
									uiClassFinder.getName(viewClass), viewClass));
			attributes = addAttributes(attributes, viewClassAttributes);
			String viewVirtualClass = IntrospectUtility.getVirtualClass(computeAndMaybeSetViewObject());
			if (viewVirtualClass != null) {
				Vector viewVirtualClassAttributes = CopyAttributeVector
						.copyVector(AttributeManager.getEnvironment()
								.getVirtualClassAttributes(viewVirtualClass));
				attributes = addAttributes(attributes,
						viewVirtualClassAttributes);
			}
		}

		if (getParentAdapter() != null
				&& getParentAdapter().getParentAdapter() != null) {
			// temp1 =
			// AttributeManager.getEnvironment().getClassAttributes(getParentAdapter().getPropertyClass().getName());
			// temp1 =
			// AttributeManager.getEnvironment().getClassAttributes(getParentAdapter().getPropertyClass().getName(),
			// getParentAdapter().getPropertyClass());
			RecordStructure parentRecordStructure = getParentAdapter()
					.getRecordStructure();
			Vector<Attribute> propertyIntAttributes = parentRecordStructure
					.getComponentAttributes(getPropertyName());
			attributes = addAttributes(attributes, propertyIntAttributes);

			ClassProxy propertyClass = getParentAdapter().getPropertyClass();
			temp1 = AttributeManager.getEnvironment().getClassAttributes(
					uiClassFinder.getName(propertyClass), propertyClass);
			// temp = getFieldAttributes(temp1, getPropertyName());
			temp = getFieldAttributes(temp1, getComponentName());

			// CopyAttributeVector.mergeAttributeLists(attributes, temp);
			attributes = addAttributes(attributes, temp);
			/*
			 * uiContainerAdapter ancestorAdapter = getGrandParentAdapter();
			 * //uiObjectAdapter topAdapter = getTopAdapter(); int ancestorLevel =
			 * 2; while (ancestorAdapter != null && ! (ancestorAdapter
			 * instanceof uiRootAdapter )) { ClassProxy ancestorPropertyClass =
			 * ancestorAdapter.getPropertyClass(); temp1 =
			 * AttributeManager.getEnvironment().getClassAttributes(
			 * uiClassFinder.getName(ancestorPropertyClass),
			 * ancestorPropertyClass); Vector pathVector =
			 * pathToVector(getPath()); temp = getPathAttributes(temp1,
			 * pathVector, ancestorLevel); attributes =
			 * addAttributes(attributes, temp); ancestorLevel++; ancestorAdapter =
			 * ancestorAdapter.getParentAdapter(); }
			 */

			String label = getDisplayedPropertyName();
			// String path = getPath().toLowerCase();
			if (label != null && !label.equals(getPropertyName())) {
				temp = getFieldAttributes(temp1, label);
				// CopyAttributeVector.mergeAttributeLists(attributes, temp);
				attributes = addAttributes(attributes, temp);
			}
			
			ClassProxy realPropertyClass = ReflectUtil.toMaybeProxyTargetClass
					(getParentAdapter().getRealObject());
			temp1 = AttributeManager.getEnvironment().getClassAttributes(
					uiClassFinder.getName(realPropertyClass), propertyClass);
			temp = getFieldAttributes(temp1, getPropertyName());

			// CopyAttributeVector.mergeAttributeLists(attributes, temp);
			attributes = addAttributes(attributes, temp);

			// label = getDisplayedPropertyName();
			if (label != null && !label.equals(getPropertyName())) {
				temp = getFieldAttributes(temp1, label);
				// CopyAttributeVector.mergeAttributeLists(attributes, temp);
				attributes = addAttributes(attributes, temp);
			}
		
//			uiContainerAdapter ancestorAdapter = getParentAdapter();
//			
//			int ancestorLevel = 1;
//			Vector pathVector = pathToVector(getPath().toLowerCase());
//			while (ancestorAdapter != null
//					&& !(ancestorAdapter instanceof uiRootAdapter)) {
//				ClassProxy ancestorPropertyClass = ancestorAdapter
//						.getPropertyClass();
//				temp1 = AttributeManager.getEnvironment().getClassAttributes(
//						uiClassFinder.getName(ancestorPropertyClass),
//						ancestorPropertyClass);
//				//moving this up
//				//Vector pathVector = pathToVector(getPath().toLowerCase());
//				temp = getSingleLevelPathAttributes(temp1, pathVector, ancestorLevel);
//				attributes = addAttributes(attributes, temp);
//				ancestorLevel++;
//				ancestorAdapter = ancestorAdapter.getParentAdapter();
//			}
			// moving here so it will take precdence over all fields
			// uiContainerAdapter ancestorAdapter = getGrandParentAdapter();
			//replacing with the a single call
			temp = getMultiLevelPathAttributes();
			attributes = addAttributes(attributes, temp);

			// adding for virtual view class, do we need for view real class
			String parentViewVirtualClass = IntrospectUtility
					.getVirtualClass(getParentAdapter().computeAndMaybeSetViewObject());
			if (parentViewVirtualClass != null) {
				temp1 = AttributeManager.getEnvironment()
						.getVirtualClassAttributes(parentViewVirtualClass);
				temp = getFieldAttributes(temp1, getPropertyName());
				// CopyAttributeVector.mergeAttributeLists(attributes, temp);
				attributes = addAttributes(attributes, temp);

			}
		}
		// System.out.println("got parent atributes");
		// System.out.println("getting local parent attributes");
		// And the local attributes of the parent object
		
		// sould use the method getFieldAttrinbutes above
		if (getParentAdapter() != null) {
			temp1 = getParentAdapter().getLocalAttributes();
			temp = getFieldAttributes(temp1, getPropertyName());
			CopyAttributeVector.mergeAttributeLists(attributes, temp);
		}

		// Next try any local attributes
		// System.out.println("getting local attributes");
		CopyAttributeVector.mergeAttributeLists(attributes, localAttributeList);

		// Note that the preferredWidget attribute should be the
		// first!!
		Attribute elideStringAttribute = null, elideImageAttribute = null;
		// order does not matter now
		/*
		 * for (int i=0; i< attributes.size(); i++) { Attribute a = (Attribute)
		 * attributes.elementAt(i); if
		 * (a.getName().equals(AttributeNames.PREFERRED_WIDGET)) {
		 * attributes.removeElementAt(i); attributes.insertElementAt(a, 0);
		 * break; } }
		 */
		mergedAttributeList = attributes;
		return attributes;
	}

	public void recomputeAttributes() {
		// setDisplayedPropertyName(getLabelWithoutSuffix());
		setDisplayedPropertyName();
		mergedAttributeList = null;
		pathToAttributes.clear();
		/*
		 * if (getPropertyClass() == null) return;
		 */
		// AttributeManager.getEnvironment().clearClassAttributes(getPropertyClass().getName());
	}

	transient Hashtable tempAttributes = new Hashtable();

	public Hashtable getTempAttributes() {
		String computedLabel = getLabel();
		if (computedLabel != null)
			setLabel(computedLabel);
		String computedTrueLabel = getTrueLabel();
		if (computedTrueLabel != null)
			setTrueLabel(computedTrueLabel);

		return tempAttributes;
	}

	public void setTempAttributeValue(String attr, Object val) {
		// if (val == null) return;
		if (val == null)
			tempAttributes.remove(attr);
		else
			/*
			 * if (attr.equals(AttributeNames.LABEL) && parent instanceof
			 * uiHashtableAdapter)
			 * 
			 * System.out.println("SetAdapter: " + this.getID() + "Index " +
			 * this.getIndex() + "Label " + (String) val);
			 */

			tempAttributes.put(attr, val);
		// this.processAttribute(new Attribute(attr, val));
		// this.getWidgetAdapter().processAttribute(new Attribute(attr, val));
	}

	/*
	 * public void setInstanceAttributes(Hashtable<String, Object> attributes) {
	 * Enumeration<String> keys = attributes.keys(); while
	 * (keys.hasMoreElements()) { String name = keys.nextElement();
	 * setTempAttributeValue(name, attributes.get(name)); } }
	 */
	/*
	 * public Object getTempAttributeValue(String attr) { Object val =
	 * this.getMergedAttributeValue(attr); if (val == null) return
	 * tempAttributes.get(attr); else return val; }
	 */
	public Object getTempAttributeValue(String attr) {
		/*
		 * if (attr.equals(AttributeNames.LABEL) && toStringAsLabel() &&
		 * getRealObject() != null) return getRealObject().toString();
		 */
		Object val = tempAttributes.get(attr);
		if (val != null)
			return val;
		return AttributeNames.getSystemDefault(attr);
		/*
		 * if (attr.equals(AttributeNames.LABEL) && parent instanceof
		 * uiHashtableAdapter)
		 * 
		 * System.out.println("GetAdapter: " + this.getID() + "Index " +
		 * this.getIndex() + "Label " + (String) val);
		 */
		// always mergeortemp wil be called
		/*
		 * if (val == null) return this.getMergedAttributeValue(attr); else
		 * return val;
		 */
		// return val;
	}

	public Object getTempAttributeValueWithoutMerging(String attr) {
		Object val = tempAttributes.get(attr);
		return val;
	}

	public void setLocalAttributes(Hashtable attributes) {
		Enumeration keys = attributes.keys();
		Vector list = new Vector();
		while (keys.hasMoreElements()) {
			String name = (String) keys.nextElement();
			Attribute a = new Attribute(name, attributes.get(name),
					Attribute.LOCAL_TYPE);
			a.CHANGED = true;
			list.add(a);
		}
		setLocalAttributes(list);
	}

	public void setLocalAttributes(Vector list) {
		// Modify the local attribute list to
		// reflect any changes.

		localAttributeList = (Vector) list.clone();
		for (int i = 0; i < list.size(); i++) {
			Attribute a = (Attribute) list.elementAt(i);
			// not sure why this test
			// if (a.getTYPE() == Attribute.INHERITED_TYPE) {
			if (a.CHANGED == true) {
				a.setType(Attribute.LOCAL_TYPE);
				// this.setTempAttributeValue(a.getAttributeName(),
				// a.getValue());
				this.setTempAttributeValue(a.getAttributeName(), null);
				if (a.getName().equals(AttributeNames.LABEL)) {
					Attribute a2 = new Attribute(AttributeNames.TRUE_LABEL, a
							.getValue(), Attribute.LOCAL_TYPE);
					a2.CHANGED = true;
					this.setTempAttributeValue(AttributeNames.TRUE_LABEL, null);
					localAttributeList.addElement(a2);
				}
			} else
				localAttributeList.removeElement(a);
			// }
		}
		// Reprocess the attribute list.
		/*
		 * setDefaultAttributes(); processAttributes();
		 */
		processAttributeList();
	}

	transient boolean isNameChild = false;

	/*
	 * public void processAttributes(Vector attributes) {
	 * 
	 * for (int i=0; i< attributes.size(); i++) { Attribute attrib =
	 * (Attribute) attributes.elementAt(i); //uiWidgetAdapterInterface wa =
	 * getWidgetAdapter(); //if ((wa != null) && (processAttribute(attrib) ==
	 * false)) { if (processAttribute(attrib) == false) {
	 * uiWidgetAdapterInterface wa = getWidgetAdapter(); if (wa != null) {
	 * //System.out.println("process attribute starting");
	 * //getWidgetAdapter().processAttribute(attrib);
	 * wa.processAttribute(attrib); //System.out.println("process attribute
	 * ending"); } } } WidgetShell ws = getWidgetShell(); if (ws != null) {
	 * //System.out.println("process attribute starting");
	 * ws.processAttributes(); //System.out.println("process attribute ending"); }
	 * 
	 * //System.out.println ("end of process attribute list"); }
	 */
	/*
	 * public void setNameChild() { isNameChild = isString(); }
	 */
	// this method is never really called
	public void processSynthesizedAttributeList() {
		setDefaultSynthesizedAttributes();
		processSynthesizedAttributesWithDefaults();
	}

	public void setDefaultSynthesizedAttributes() {
		Object componentWidth = getNonDefaultMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		if (componentWidth != null) {
			if (getParentAdapter() != null)
				getParentAdapter().setTempAttributeValue(
						AttributeNames.STRETCH_COLUMNS, false);
		}
	}

	public void propagateAttributesToWidgetShell() {
		WidgetShell ws = getWidgetShell();
		if (ws != null) {
			// System.out.println("process attribute starting");
			ws.processAttributes();
			// System.out.println("process attribute ending");
		}
	}

	public void propagateAttributesToWidgetAdapter() {
		WidgetAdapterInterface wa = getWidgetAdapter();
		if (wa != null) {
			// System.out.println("process attribute starting");
			wa.processAttributes();
			// System.out.println("process attribute ending");
		}
	}

	public void propagateAttributesToParentWidgetAdapter() {
		CompositeAdapter parent = getParentAdapter();
		if (parent == null)
			return;
		WidgetAdapterInterface wa = parent.getWidgetAdapter();
		if (wa != null) {
			// System.out.println("process attribute starting");
			wa.processAttributes();
			// System.out.println("process attribute ending");
		}
	}

	public void processSynthesizedAttributesWithDefaults() {
		/*
		 * WidgetShell ws = getWidgetShell(); if (ws != null) {
		 * //System.out.println("process attribute starting");
		 * ws.processAttributes(); //System.out.println("process attribute
		 * ending"); }
		 */
	}

	Vector childrenAttributes;

	public void initAttributes(Hashtable selfAttributes,
			Vector theChildrenAttributes) {
		if (selfAttributes != null) {
			setLocalAttributes(selfAttributes);
		}
		childrenAttributes = theChildrenAttributes;

	}

	public void processAttributeList() {
		setDefaultAttributes();
		processAttributesWithDefaults();
		// propagateAttributesToWidgetShell();
		// processAttributes(getAttributesWithDefaults());

	}

	public void propagateProcessAttrib(Attribute attrib) {
		// if (processAttribute(attrib) == false) {
		processAttribute(attrib);
		WidgetAdapterInterface wa = getWidgetAdapter();
		if (wa != null) {
			// System.out.println("process attribute starting");
			// getWidgetAdapter().processAttribute(attrib);
			wa.processAttribute(attrib);
			// System.out.println("process attribute ending");
		}
		propagateDescendentProcessAttrib(this, attrib);
		// } else {
		/*
		 * wa = getContainingWidgetAdapter(); if (wa != null)
		 * wa.processDescendentAttribute(this, attrib);
		 */

		// }
	}

	// }
	public void propagateDescendentProcessAttrib(ObjectAdapter descendent,
			Attribute attrib) {
		CompositeAdapter parentAdapter = getParentAdapter();
		if (parentAdapter != null) {
			if (parentAdapter.getWidgetAdapter() != null)
				parentAdapter.getWidgetAdapter().processDescendentAttribute(
						descendent, attrib);
			parentAdapter.propagateDescendentProcessAttrib(descendent, attrib);
		}

	}

	public void processAttributesWithDefaults() {
		// if (getWidgetAdapter() == null) return;
		if (isRootAdapter())
			return;
		if (isAtomic() && this instanceof CompositeAdapter) {
			linkPropertyToAdapter(getRealObject(), "", this);
		}

		// if (isLeafOfAtomic()) return;

		Vector attributes = this.getAttributes();
		if (attributes == null)
			return;
		for (int i = 0; i < attributes.size(); i++) {
			Attribute attrib = (Attribute) attributes.elementAt(i);
			if (getTempAttributeValueWithoutMerging(attrib.getName()) == null)
				// if (getTempAttributeValueWithoutMerging(attrib.getName()) !=
				// null)
				propagateProcessAttrib(attrib);
			/*
			 * //uiWidgetAdapterInterface wa = getWidgetAdapter(); //if ((wa !=
			 * null) && (processAttribute(attrib) == false)) { if
			 * (processAttribute(attrib) == false) { uiWidgetAdapterInterface wa =
			 * getWidgetAdapter(); if (wa != null) {
			 * //System.out.println("process attribute starting");
			 * getWidgetAdapter().processAttribute(attrib);
			 * //System.out.println("process attribute ending"); } }
			 */
		}
		// System.out.println ("end of process attribute list");
		// dont know why we need getTempAAttributes with side effects
		// Hashtable defaultAttrs = getTempAttributes();
		Hashtable defaultAttrs = tempAttributes;
		Enumeration attrNames = defaultAttrs.keys();
		while (attrNames.hasMoreElements()) {
			String attrName = (String) attrNames.nextElement();
			Object attrValue = defaultAttrs.get(attrName);
			Attribute attrib = new Attribute(attrName, attrValue);
			propagateProcessAttrib(attrib);

		}
		propagateAttributesToWidgetShell();
		/*
		 * WidgetShell ws = getWidgetShell(); if (ws != null) {
		 * //System.out.println("process attribute starting");
		 * ws.processAttributes(); //System.out.println("process attribute
		 * ending"); }
		 */
	}

	boolean fixUIComponent = false;

	public void fixUIComponent(boolean newVal) {
		fixUIComponent = newVal;
	}

	public boolean fixUIComponent() {
		return fixUIComponent;
	}

	public void setPreferredWidget() {
	
		if (getPreferredWidget() == null) {
			ComponentDictionary cd = EditorRegistry.getComponentDictionary();
			// componentDictionary cd = EditorRegistry.getComponentDictionary();
			Hashtable componentMapping = cd.getDefaultComponentMapping();
			// let us fill this with textfield for now
//			if (!(this.isRootAdapter()) && this.isTopDisplayedAdapter()
//				&& propertyClass.equals(IntrospectUtility.stringClass()))
//
//
//			setPreferredWidget(VirtualTextArea.class.getName());
//
//			else
				
				this.setPreferredWidget(cd.getWidgetClass(this));
		

		}
	}

	void processNameChild(Object newValue) {
		if (!(newValue instanceof String)) return;
		if (getParentAdapter() == null)
			return;
		((CompositeAdapter) getParentAdapter())
				.nameChildChanged((String) newValue);
	}

//	boolean isNameKey;

	public void setNameChild() {
		// Object label = this.getMergedAttributeValue(AttributeNames.LABEL);
		if (!useNameAsLabel())
			return;
		String label = getLabelWithoutSuffix();
		// if (label == null) return;
		ObjectAdapter parentAdapter = getParentAdapter();
		

		isNameChild = (label != null)
				&& isString()
				&& parentAdapter != null
				&& (label.toLowerCase())
						.equals(parentAdapter.getNameProperty());
		if (isNameChild)
			processNameChild(getValue());
		/*
		 * else { isNameKey = isNameKey(); if (isNameKey && getValueAdapter() !=
		 * null) processNameChild (getValueAdapter().getValue()); }
		 */
		// System.out.println("NameChild label " + label + " isString" +
		// isString() + "isNameChild" + isNameChild );
	}

	public void setPosition() {

		if (this.getIndex() != -1) {
			ClassAdapter classAdapter = (ClassAdapter) this
					.getParentAdapter();

			if (getParentAdapter() instanceof VectorAdapter
					|| getParentAdapter() instanceof HashtableAdapter)

				setPosition(classAdapter.numFeatures + index);
			else
				setPosition(index);
		}
	}

	public void setDefaultAttributes() {
		// mergedAttributeList = null;
		recomputeAttributes();
		// tempAttributes = new Hashtable();
		// setPosition();
		setNameChild();
		setPreferredWidget();
		if (getParentAdapter() == null)
			return;
		if (getParentAdapter().isFlatTableCell())
			return;
		// setDoubleClickMethod();
		if (getParentAdapter() != null
				&& getParentAdapter().childrenShowingColumnTitle()) {
			setShowChildrenColumnTitle(true);
			setColumnTitleStatus(ColumnTitleStatus.show);
		} else if (getParentAdapter() != null
				&& getParentAdapter().childrenHidingColumnTitle()) {
			setShowChildrenColumnTitle(false);
			setColumnTitleStatus(ColumnTitleStatus.hide);
		}

	}
	boolean prferredWidgetResult;
	public boolean getProcessPreferredWidget() {
		return prferredWidgetResult;
	}
	public boolean processPreferredWidget() {
		// return processPreferredWidget((String)
		// this.getTempAttributeValue(AttributeNames.PREFERRED_WIDGET));
//		if (isNestedShapesContainer())
//			prferredWidgetResult = 	processPreferredWidget(NestedShapesAdapter.class.getName());
//		else
		// do not create text widgets if all we are doing is graphics, should probably check atomic insteas of primitive adapter
//		if (getUIFrame().isOnlyGraphicsPanel() && ! (this instanceof ShapeObjectAdapter) && this instanceof PrimitiveAdapter)
		if (getUIFrame().isOnlyGraphicsPanel() && ! (this instanceof ShapeObjectAdapter) )

//		if (getUIFrame().isOnlyGraphicsPanel() && ! (this instanceof ShapeObjectAdapter) && this instanceof PrimitiveAdapter)
			return false;
//		if (getUIFrame().isDummy() && !getUIFrame().mainPanelIsVisible() && ! (this instanceof ShapeObjectAdapter) && this instanceof PrimitiveAdapter)
//			return false;
		prferredWidgetResult = 	processPreferredWidget(getPreferredWidget());
		return prferredWidgetResult;
		//return processPreferredWidget(getPreferredWidget());
	}

	public boolean hasOnlyGraphicsDescendents() {
		return false;
	}
	public void addGraphicalDescendents (List<ShapeObjectAdapter> list) {		
		
	}

	public Boolean getPromoteOnlyChild() {
		Boolean retVal = (Boolean) this
				.getMergedTempOrDefaultAttributeValue(AttributeNames.PROMOTE_ONLY_CHILD);
		
		return retVal;	
//		if (computeHasOnlyChild(this))
//			return false;	
	}
	
	public ClassProxy getTruePropertyClass() {
		if (!isProperty()) return null;
		return ((ClassAdapter) getParentAdapter()).getRecordStructure().componentType(getPropertyName());	
		
	}
	
	public Boolean computePromoteOnlyChild(ObjectAdapter child) {
		ClassProxy truePropertyClass = child.getTruePropertyClass();
		return  truePropertyClass != null && uiGenerator.isPrimitiveClass(truePropertyClass);
	}
	
	public String getPreferredWidget() {
		return (String) this
				.getMergedTempOrDefaultAttributeValue(AttributeNames.PREFERRED_WIDGET);
	}

	public void setPreferredWidget(String newVal) {
		setTempAttributeValue(AttributeNames.PREFERRED_WIDGET, newVal);
	}

	public String getPreferredWidgetAdapter() {
		String retVal = (String) this
				.getMergedTempOrDefaultAttributeValue(AttributeNames.PREFERRED_WIDGET_ADAPTER);
		if (retVal == null && isNestedShapesContainer())
			return NestedShapesAdapter.class.getName();
		else
			return null;
			
	}

	public boolean linkToComponent() {
		return linkPreferredWidget(getPreferredWidget(), false);
		/*
		 * try { //Connector.linkAdapterToComponent(this, (String)
		 * this.getTempAttributeValue(AttributeNames.PREFERRED_WIDGET));
		 * Connector.linkAdapterToComponent(this, getPreferredWidget()); } catch
		 * (Exception e) { //System.out.println("could not link to" +
		 * this.getTempAttributeValue(AttributeNames.PREFERRED_WIDGET));
		 * System.out.println("could not link to" + getPreferredWidget());
		 * e.printStackTrace(); }
		 */
	}

	// return true if widget actually added
	public boolean processPreferredWidget(String widgetClassName) {
		if (widgetClassName == null)
			return false;

		// return linkPreferredWidget(widgetClassName, true);
		return linkPreferredWidget(widgetClassName,
				!hasOnlyGraphicsDescendents() || isNestedShapesContainer());
		/*
		 * // For the first time if (getUIComponent() == null) { try {
		 * 
		 * VirtualComponent c = Connector.linkAdapterToComponent(this,
		 * widgetClassName);
		 * 
		 * //genericWidget.setComponent(c); addUIComponentToParent(c);
		 * 
		 * if (genericWidget != null) genericWidget.invalidate(); return true; }
		 * catch (Exception e) { System.out.println("could not link");
		 * e.printStackTrace(); } } else if (!(this instanceof
		 * uiContainerAdapter) &&
		 * !getWidgetAdapter().getUIComponent().getClass().getName().equals(widgetClassName)) {
		 * //System.out.println("Changing component"); try { Object oldValue =
		 * getWidgetAdapter().getUIComponentValue(); VirtualComponent c =
		 * Connector.linkAdapterToComponent(this, widgetClassName);
		 * genericWidget.setComponent(c); //System.out.println("comp value" +
		 * oldValue); getWidgetAdapter().setUIComponentValue(oldValue); return
		 * false; } catch (Exception e) { System.out.println("could not link
		 * 2"); e.printStackTrace(); } } return false;
		 */
	}

	// return true if widget actually added
	public boolean linkPreferredWidget(String widgetClassName,
			boolean addToParent) {

		/*
		 * if ( !(this.isRootAdapter()) && (this == getTopAdapter(this)) &&
		 * propertyClass.getName().equals("java.lang.String")) {
		 * //System.out.println(this + propertyClass.getName() +
		 * widgetClassName); widgetClassName = "javax.swing.JTextArea"; }
		 */

		// For the first time
		if (getUIComponent() == null) {
			try {

				VirtualComponent c = Connector.linkAdapterToComponent(this,
						widgetClassName);
				//cannotembed scrollpanes inside panels
//				VirtualScrollPane spane ;
//				if (isScrolled()) {
//					spane = ScrollPaneSelector.createScrollPane();
//					spane.setScrolledComponent(c);
//					c = spane;
//				}
				/*
				 * if (realObject == null && viewObject == null && c instanceof
				 * Container ) ((Container)
				 * c).add(uiGenericWidget.getNullWidget());
				 */
				// genericWidget.setComponent(c);
				if (addToParent)
					addUIComponentToParent(c);
				/*
				 * if (genericWidget != null) genericWidget.invalidate();
				 */
				return true;
			} catch (Exception e) {
				System.out.println("could not link");
				e.printStackTrace();
			}
		} else if (
		// dont know why this condition existed
		// !(this instanceof uiContainerAdapter) &&
		!fixUIComponent()
				&& !(this instanceof RootAdapter)
				&& !getWidgetAdapter().getUIComponent().getClass().getName()
						.equals(widgetClassName)) {
			 //System.out.println("Changing component");
			try {

				// Object oldValue = getWidgetAdapter().getUIComponentValue();
				Object newValue = getWidgetAdapter().getUIComponentValue();
				VirtualComponent c = Connector.linkAdapterToComponent(this,
						widgetClassName);
//				if (c == getUIComponent())
//					return false;
				if (c != getUIComponent()) {
				System.out.println("Changing component");
				if (genericWidget != null)
				genericWidget.setComponent(c);
				// System.out.println("comp value" + oldValue);
				// getWidgetAdapter().setUIComponentValue(oldValue);
				// getWidgetAdapter().setUIComponentValue(newValue);
				if (!(isRootAdapter()))
					getWidgetAdapter().setUIComponentValue(computeAndMaybeSetViewObject());
				}
				if (addToParent) {
					/*
					 * if (getGenericWidget() != null) { VirtualComponent
					 * myComponentChild =
					 * getGenericWidget().getContainer().getParent(); if
					 * (getGenericWidget().getContainer().getParent() != null)
					 * return false;
					 * //getGenericWidget().getContainer().setParent(null); }
					 * else if (c.getParent() != null && c.isVisible()) return
					 * false; //c.setParent(null);
					 * 
					 */
					VirtualComponent myComponentChild = c;
					if (getGenericWidget() != null)
						myComponentChild = getGenericWidget().getContainer();
					if (myComponentChild.getParent() != null /*
																 * &&
																 * myComponentChild.isVisible()
																 */)
						return false;

					addUIComponentToParent(c);
					/*
					 * if (!(isRootAdapter()))
					 * getWidgetAdapter().setUIComponentValue(getViewObject());
					 */

					// refresh();
				}

				return true;
			} catch (Exception e) {
				System.out.println("could not link 2");
				e.printStackTrace();
			}
		}
		return false;
	}

	boolean processAttribute(Attribute attribute) {
		return false;
		/*
		 * // Check for any known attributes // preferredWidget, hidden are two
		 * I can think of // now. String name = attribute.getName();
		 * 
		 * //System.out.println("processing" + name);
		 * 
		 * //if (name.equals("preferredWidget")) { if
		 * (name.equals(AttributeNames.PREFERRED_WIDGET)) { return true;
		 * 
		 * //String widgetClassName = (String) attribute.getValue(); //return
		 * processPreferredWidget(widgetClassName);
		 *  }
		 * 
		 * 
		 * else if (name.equals(AttributeNames.LABEL)) {
		 * 
		 * 
		 * return true; }
		 * 
		 * //else if (name.equals("islabelled")) { else if
		 * (name.equals(AttributeNames.LABELLED)) {
		 * 
		 * return true; }
		 * 
		 * else if (name.equals(AttributeNames.VISIBLE)) {
		 * 
		 * return true; } //else if (name.equals("elideImage")) { else if
		 * (name.equals(AttributeNames.ELIDE_IMAGE)) {
		 * 
		 * return true; } //else if (name.equals("elideString")) { else if
		 * (name.equals(AttributeNames.ELIDE_STRING)) {
		 * 
		 * return true; } else if (name.equals(AttributeNames.POSITION)) {
		 * 
		 * 
		 * return true; } else return false;
		 */

	}

	// Implementation of LocalAttributeListener interface
	// methods

	public void localAttributeChanged(Attribute a) {
		setLocalAttribute(a);
		// Reprocess attributes.
		/*
		 * setDefaultAttributes(); // probably not needed processAttributes();
		 */
		processAttributeList();
	}

	public TreePath getTreePath() {
		ObjectAdapter treeParent = (ObjectAdapter) getParent();
		// if (getParent() == null || getParent().getParent() == null) {
		if (treeParent == null || treeParent.getParent() == null) {
			// if (treeParent == null ) {
			// System.out.println("uiFrame" + getUIFrame());
			// System.out.println("null parent, and me" + this);
			return (TreePath) this.getUIFrame().getJTree().getPathForLocation(
					0, 0);
		} else {
			// System.out.println("non null parent + me:" + this);
			// return ((uiObjectAdapter)
			// getParent()).getTreePath().pathByAddingChild(this);
			return treeParent.getTreePath().pathByAddingChild(this);
		}
	}
	
	public String getReferencePath() {
		if (getParentAdapter() == null ) {
			return getPath();
		}
		return getParentAdapter().toReferencePath( getPropertyName(), getIndex());
	}

	public String getPath() {
		if (getParentAdapter() == null
				|| getParentAdapter().getParentAdapter() == null) {
			return "";
		} else {
			String path = getParentAdapter().getPath()
					+ AttributeNames.PATH_SEPARATOR
					+ ((CompositeAdapter) getParentAdapter())
							.getChildAdapterRealIndex(this);
			return path;
		}
	}

	public String getCompletePathOnly() {
		if (this instanceof RootAdapter) {
			return "" + ((RootAdapter) this).getUniqueID();
		}
//		if (getParentAdapter() == null
//		|| getParentAdapter().getParentAdapter() == null) {
//	return "";
//		if (getParentAdapter() == null
//				|| getParentAdapter().getParentAdapter() == null) {
//			return "";
//		} else {
			String pathHead = getParentAdapter().getCompletePathOnly();
			String pathTail = ((CompositeAdapter) getParentAdapter())
					.getChildAdapterRealIndex(this);
			String path = null;
			if (pathHead != null && pathTail != null) {
				path = pathHead + AttributeNames.PATH_SEPARATOR + pathTail;
			}
			return path;
//		}
	}

	public static Vector pathToVector(String path) {
		Vector retVal = new Vector();
		int index = 0;
		while (index < path.length() && index >= 0) {
			int dotIndex;
			dotIndex = path.indexOf(AttributeNames.PATH_SEPARATOR, index);
			if (dotIndex == -1) {
				dotIndex = path.length();
			}
			if (dotIndex != index)
				retVal.addElement(path.substring(index, dotIndex));
			index = dotIndex + 1;
		}
		return retVal;
	}
	public static  Map<String, Object> objectAdapterToPropertyMap (ObjectAdapter aBean, String[] aProperties) {
		Map<String, Object> retVal = new HashMap();
		for (String aProperty:aProperties) {
			ObjectAdapter aChildAdapter = aBean.pathToObjectAdapter(aProperty);
			if (aChildAdapter == null )
				retVal.put(aProperty, null);
			else 
				retVal.put(aProperty, aChildAdapter.getValue());		
			
		}
		
		return retVal;	
		
	}
	
	public static  Map<String, Object> objectAdapterToPropertyMap (ObjectAdapter aBean, Collection<String> aProperties) {
		return objectAdapterToPropertyMap(aBean, aProperties.toArray(new String[aProperties.size()]));
		
	}
	public static  Map<String, Object> beanToPropertyMap (Object aBean, String[] aProperties) {
		return objectAdapterToPropertyMap(ObjectRegistry.getOrCreateObjectAdapter(aBean), aProperties);
		
	}
	public static  Object getPropertyValue (Object aBean, String aProperty) {
		String[] aProperties = {aProperty};
		Map<String, Object>  retVal = objectAdapterToPropertyMap(ObjectRegistry.getOrCreateObjectAdapter(aBean), aProperties);
		return retVal.get(aProperty);
		
	}
	public static  List<Object> getPropertyValues (List<Object> aBeans, String aProperty) {
		List retVal = new ArrayList();
		for (Object aBean:aBeans) {
			retVal.add(getPropertyValue(aBean, aProperty));
		}
		return retVal;
	}
	public static  Map<String, Object> beanToPropertyMap (Object aBean, Collection<String> aProperties) {
		return objectAdapterToPropertyMap(ObjectRegistry.getOrCreateObjectAdapter(aBean), aProperties);
		
	}

	public static String vectorToPath(Vector pathVector) {
		String retVal = null;
		for (int i = 0; i < pathVector.size(); i++) {
			if (i == 0)
				retVal = (String) pathVector.get(i);
			else
				retVal += AttributeNames.PATH_SEPARATOR + pathVector.get(i);

		}
		return retVal;
	}

	public Vector getVectorPath() {
		return pathToVector(getPath());
	}

	public Vector getVectorPathRelativeTo(CompositeAdapter ancestorAdapter) {
		Vector ancestorPath = ancestorAdapter.getVectorPath();
		Vector myPath = getVectorPath();
		for (int i = 0; i < ancestorPath.size(); i++) {
			myPath.remove(i);
		}
		return myPath;

	}

	public Vector getGenericVectorPathRelativeTo(
			CompositeAdapter ancestorAdapter) {
		Vector pathVector = getVectorPathRelativeTo(ancestorAdapter);
		if (pathVector.size() == 0)
			return pathVector;
		pathVector.set(0, "*");
		return pathVector;

	}

	public String getGenericStringPathRelativeTo(
			CompositeAdapter ancestorAdapter) {
		Vector pathVector = getGenericVectorPathRelativeTo(ancestorAdapter);
		return vectorToPath(pathVector);

	}

	public String getSiblingGenericStringPathRelativeTo(
			CompositeAdapter ancestorAdapter, String siblingName) {
		Vector pathVector = getGenericVectorPathRelativeTo(ancestorAdapter);
		if (pathVector.size() > 0) {
			pathVector.set(pathVector.size() - 1, siblingName);
		}
		return vectorToPath(pathVector);

	}

	public String getStringPathRelativeTo(CompositeAdapter ancestorAdapter) {
		Vector vectorPath = getVectorPathRelativeTo(ancestorAdapter);
		return vectorToPath(vectorPath);

	}

	public Vector<ObjectAdapter> getPeerAdapters(
			CompositeAdapter ancestorAdapter, ObjectAdapter childAdapter) {
		Vector childPath = childAdapter
				.getVectorPathRelativeTo(ancestorAdapter);

		Vector<ObjectAdapter> retVal = new Vector();
		if (childPath.size() < 1)
			return retVal;
		childPath.remove(0);
		for (int i = 0; i < ancestorAdapter.getChildAdapterCount(); i++) {
			retVal.add(pathToObjectAdapter(
					ancestorAdapter.getChildAdapterAt(i), childPath));
		}
		return retVal;
	}

	public ObjectAdapter getPeerAdapter(CompositeAdapter ancestorAdapter,
			ObjectAdapter childAdapter, String componentName) {
		Vector childPath = childAdapter
				.getVectorPathRelativeTo(ancestorAdapter);

		Vector<ObjectAdapter> retVal = new Vector();
		if (childPath.size() < 1)
			return null;
		childPath.remove(0);
		for (int i = 0; i < ancestorAdapter.getChildAdapterCount(); i++) {
			// uiObjectAdapter ancestorChild =
			// ancestorAdapter.getChildAdapterAt(i);
			ObjectAdapter peer = pathToObjectAdapter(ancestorAdapter
					.getChildAdapterAt(i), childPath);
			if (peer != null && peer.getPropertyName().equals(componentName))
				return peer;
			// retVal.add(pathToObjectAdapter(ancestorAdapter.getChildAdapterAt(i),
			// childPath));
		}
		return null;
	}

	public ObjectAdapter getPeerAdapter(CompositeAdapter ancestorAdapter,
			String componentName) {
		return getPeerAdapter(ancestorAdapter, this, componentName);
	}

	public ObjectAdapter getTablePeerAdapter(String componentName) {
		// return getPeerAdapter(getTableAdapter(), this, componentName);
		return getPeerAdapter(getRowParentAdapter(), this, componentName);
	}

	public Vector<CompositeAdapter> getPeerParentAdapters(
			CompositeAdapter ancestorAdapter, ObjectAdapter childAdapter) {
		Vector childPath = childAdapter
				.getVectorPathRelativeTo(ancestorAdapter);

		Vector<CompositeAdapter> retVal = new Vector();
		if (childPath.size() < 2)
			return retVal;
		childPath.remove(0);
		childPath.remove(childPath.size() - 1);
		for (int i = 0; i < ancestorAdapter.getChildAdapterCount(); i++) {
			ObjectAdapter child = ancestorAdapter.getChildAdapterAt(i);
			
			if (!(child instanceof CompositeAdapter))
				continue;
			ObjectAdapter peer = pathToObjectAdapter(child, childPath);
			if (peer == null)
				continue;
			if (peer.isOnlyChild())
				peer = peer.getParentAdapter();
			retVal.add((CompositeAdapter) peer);
		}
		return retVal;
	}

	public void redoVisibleChildrenOfPeerParentAdapters() {
		Vector<CompositeAdapter> updatees = getPeerParentAdaptersRelativeToTableAdapter();
		for (int i = 0; i < updatees.size(); i++) {
			updatees.get(i).redoVisibleChildren();
		}
	}
	public void deepElideVisibleChildrenOfPeerParentAdapters() {
		Vector<CompositeAdapter> updatees = getPeerParentAdaptersRelativeToTableAdapter();
		for (int i = 0; i < updatees.size(); i++) {
			ObjectAdapter nextRow = updatees.get(i);
			nextRow = nextRow.getFlatTableRowAncestor();
			uiGenerator.deepElide(nextRow);
			nextRow.invalidateComponentsSetInTree();
			nextRow.getWidgetAdapter().rebuildPanel();
		}
		getUIFrame().validate();
		getUIFrame().repaint();
	}

	public void redoDisplayChildrenOfTableWidgetAdapter() {
		CompositeAdapter tableAdapter = getTableAdapter();
		if (tableAdapter == null)
			return;
		WidgetAdapterInterface widgetAdapter = tableAdapter
				.getWidgetAdapter();
		if (widgetAdapter == null)
			return;
		Vector<ObjectAdapter> updatees = widgetAdapter
				.getChildrenAdaptersInDisplayOrder();

		for (int i = 0; i < updatees.size(); i++) {
			ObjectAdapter maybeRowAdapter = updatees.get(i);
			if (maybeRowAdapter instanceof CompositeAdapter)
				((CompositeAdapter) maybeRowAdapter).redoVisibleChildren();
			// ((uiContainerAdapter) updatees.get(i)).redoVisibleChildren();
		}
	}
	public void deepElideDisplayChildrenOfTableWidgetAdapter(ObjectAdapter tableAdapter) {
		//uiContainerAdapter tableAdapter = getTableAdapter();
		if (tableAdapter == null)
			return;
		WidgetAdapterInterface widgetAdapter = tableAdapter
				.getWidgetAdapter();
		if (widgetAdapter == null)
			return;
		Vector<ObjectAdapter> updatees = widgetAdapter
				.getChildrenAdaptersInDisplayOrder();

		for (int i = 0; i < updatees.size(); i++) {
			ObjectAdapter maybeRowAdapter = updatees.get(i);
			if (maybeRowAdapter instanceof CompositeAdapter) {
				uiGenerator.deepElide((CompositeAdapter) maybeRowAdapter);
				
				maybeRowAdapter.invalidateComponentsSetInTree();
				maybeRowAdapter.getWidgetAdapter().rebuildPanel();
			}
			// ((uiContainerAdapter) updatees.get(i)).redoVisibleChildren();
		}
	}

	public boolean grandParentFlatRowHasFlatTableRowDescendents() {
		CompositeAdapter grandpa = getGrandParentAdapter();
		if (grandpa == null)
			return false;
		return grandpa.isFlatTableRow() && grandpa.hasFlatTableRowDescendent();

	}

	public boolean isFlatRowAndHasFlatRowDescendents() {

		return isFlatTableRow() && hasFlatTableRowDescendent();

	}
	
	public boolean isFlatTableRowDescendent() {
		return isFlatTableComponent() && !isFlatTableRow();
	}

	public Vector<CompositeAdapter> getPeerParentAdaptersRelativeToTableAdapter() {
		// return getPeerParentAdapters(getTableAdapter(), this);
		return getPeerParentAdapters(getRowParentAdapter(), this);
	}

	public Vector<ObjectAdapter> getPeerAdaptersRelativeToTableAdapter() {
		// return getPeerAdapters(getTableAdapter(), this);
		return getPeerAdapters(getRowParentAdapter(), this);
	}

	public ObjectAdapter pathToObjectAdapter(Vector path) {
		ObjectAdapter retVal = getTopAdapter();
		return pathToObjectAdapter(getTopAdapter(), path);
		/*
		 * for (int i = 0; i < path.size(); i++) { retVal =
		 * ((uiContainerAdapter) retVal) .getChildAdapterAtRealIndex((String)
		 * path.elementAt(i)); } return retVal;
		 */
	}

	public static ObjectAdapter pathToObjectAdapter(ObjectAdapter topAdapter,
			Vector path) {
		try {
			ObjectAdapter retVal = topAdapter;
			for (int i = 0; i < path.size(); i++) {
				retVal = ((CompositeAdapter) retVal)
						.getVisibleOrInvisibleChildAdapterAtRealIndex((String) path.elementAt(i));
				if (retVal == null)
					return null;
			}
			return retVal;
		} catch (Exception e) {
			return null;
		}
	}
	public static ObjectAdapter pathToObjectAdapter(ObjectAdapter topAdapter,
			String path) {
		return pathToObjectAdapter(topAdapter, pathToVector(path));
	}

	public  ObjectAdapter pathToObjectAdapter(String path) {
		return pathToObjectAdapter(pathToVector(path));
	}

	public String getBeautifiedPath() {
		// if (getParentAdapter() == null /*||
		// if (this.getAdapterType() == this.KEY_TYPE) return
		// this.getExpandedAdapter().getBeautifiedPath();
		CompositeAdapter myParent = (CompositeAdapter) getParent();
		if (myParent == null /*
								 * || getParentAdapter().getParentAdapter() ==
								 * null
								 */) {
			return "";
		} else {
			/*
			 * uiGenericWidget gw = this.getGenericWidget(); if (gw == null)
			 * return ""; String label = gw.getLabel();
			 */
			String label = this.getLabel();
			if (label == null || label.equals(""))
				// if (getParentAdapter() instanceof uiVectorAdapter)
				if (myParent instanceof VectorAdapter)
					// label = "" + ((uiContainerAdapter)
					// getParentAdapter()).getChildAdapterIndex(this) + 1;
					label = "" + myParent.getChildAdapterRealIndex(this) + 1;
				else if (isTopAdapter())
					return "";
				else
					// label = "*";
					label = "*";
			// if (getParentAdapter().getBeautifiedPath().equals(""))
			if (myParent.getBeautifiedPath().equals(""))
				return label;
			else
				return
				// getParentAdapter().getBeautifiedPath() + "." + label;
				myParent.getBeautifiedPath() + AttributeNames.PATH_SEPARATOR + label;
		}
	}

	//
	//
	public void setElidedComponent() {
		// Determine what sort of elide mechanism is to be
		// used.
		// check attributes elideImage and elideString
		// if all fails, use the field name. (the label)

	}

	public boolean hasNoProperties() {
		return false;
	}

	// Abstract methods to get the value represented
	// in the ui representation and to set the value in
	// the ui repn (for properties)
	public abstract void refreshValue(Object newValue);
	public abstract void refreshValue(Object newValue, boolean forceRefresh);

	public abstract Object getValue();

	Object previousRealObject;
	Object pendingFutureRealObject;

	public Object getPendingFutureRealObject() {
		return pendingFutureRealObject;
	}

	public void setPendingFutureRealObject(Object pendingFutureRealObject) {
		this.pendingFutureRealObject = pendingFutureRealObject;
	}
	public boolean hasPendingValue() {
		return pendingFutureRealObject != null;
	}

	public void pendingValueProcessed() {
		 setPendingFutureRealObject(null);
	}

	public Object getOriginalValue() {
		// return previousRealObject;
		return getValue();
	}

	public Object getPreviousRealObject() {
		/*
		 * if (getFirstValueOfRealObject()) return new Object();
		 */

		return previousRealObject;
		// return getValue();
	}

	public boolean getFirstValueOfRealObject() {
		return firstValueOfRealObject;
	}

	public Object getValueOrRealObject() {
		return getRealObject();
	}

	// Abstract methods for the Selectable interface
	public void select() {
		/*
		 * if (getGenericWidget().elided)
		 * getGenericWidget().selectElideComponent(); else
		 */
		getWidgetAdapter().setUIComponentSelected();
	}

	public void unselect() {
		/*
		 * if (getGenericWidget().elided)
		 * getGenericWidget().unselectElideComponent(); else
		 */
		// uiComponentFocusLost();
		getWidgetAdapter().setUIComponentDeselected();
	}

	// public abstract void select();
	// public abstract void unselect();
	public abstract String getChildSelectableIndex(Selectable child);

	// public abstract Selectable getChildSelectable(String index);
	public abstract Selectable getChildSelectable(String index);

	public Selectable getParentSelectable() {
		if (getParentAdapter() == null
				|| getParentAdapter().getParentAdapter() == null)
			return null;
		else
			return getParentAdapter();
	}

	public Object getObject() {
		// if primitive type return getValue
		// otherwise return getRealObject()
		// if (getAdapterType() == PRIMITIVE_TYPE)
		if (this instanceof PrimitiveAdapter)
			return getValue();
		else
			return getRealObject();
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public Enumeration children() {
		return null;
	}

	public int getChildCount() {
		return getChildAdapterCount();
		// return 0;
	}

	public int getChildAdapterCount() {
		return 0;
	}

	public boolean isLeaf() {
		return isLeafAdapter();
	}

	public boolean isLeafAdapter() {
		return true;
	}

	public int getIndex(TreeNode node) {
		return getAdapterIndex(node);
	}

	public int getAdapterIndex(TreeNode node) {
		return -1;
	}

	public TreeNode getChildAt(int childIndex) {
		return getChildAdapterAt(childIndex);
	}

	public ObjectAdapter getChildAdapterAt(int childIndex) {
		return null;
	}

	/*
	 * public TreeNode getParent() { return getParentAdapter(); }
	 */
	public TreeNode getParent() {

		// System.out.println("get parent:" + this + "onlyCompChild" +
		// onlyCompositeChild);
		CompositeAdapter parent = (CompositeAdapter) getParentAdapter();
		if (parent == null)
			return null;
		CompositeAdapter grandParent = parent.getParentAdapter();
		if (grandParent == null || grandParent.onlyCompositeChild == null)
			return parent;
		return grandParent;

		// if (parent.isSkippedAdapter())
		/*
		 * if (parent.onlyCompositeChild != null) return parent.getParent();
		 */
		// if (onlyCompositeChild != null)
		// return parent.getParent();
		// return parent;
	}

	public void insert(MutableTreeNode child, int index) {
	}

	public void remove(int index) {
	}

	public void remove(MutableTreeNode node) {
	}

	public void removeFromParent() {
	}

	public void setParent(MutableTreeNode newParent) {
	}

	public void setUserObject(Object object) {
		if (isValueAdapter()
				&& ((HashtableAdapter) getParentAdapter())
						.setValueUserObject(this, object))
			propagateChange();
	}

	public void setCompleteUserObject(Object object) {
	}

	public void setUserTypedObject(Object realObject) {

	}

	public static boolean classChanged(Object oldValue, Object value) {
		return ((oldValue == null && value != null)
				|| (oldValue != null && value == null) || (oldValue != null
				&& value != null && oldValue.getClass() != value.getClass()));
	}

	Vector methodActions = new Vector();

	public Vector getMethodActions() {
		return methodActions;
	}

	public void setMethodActions(Vector newVal) {
		methodActions = newVal;
	}

	String frameTitle; // the resolved title attribute if not blank or class

	// name

	public void setFrameTitle(String newVal) {
		frameTitle = newVal;
	}
	ClassDescriptorInterface classDescriptor;
	ClassDescriptorInterface getClassDescriptor() {
		if (classDescriptor != null)
			return classDescriptor;
		Object obj = getRealObject();
		classDescriptor = ClassDescriptorCache.getClassDescriptor(RemoteSelector
				.getClass(obj), obj);
		return classDescriptor;
	}
	static ClassDescriptorInterface getClassDescriptor(Object obj) {
		ClassProxy objClass = ReflectUtil.toMaybeProxyTargetClass(obj);
		return ClassDescriptorCache.getClassDescriptor(objClass, obj);
//		
//		return ClassDescriptorCache.getClassDescriptor(RemoteSelector
//				.getClass(obj), obj);
		
	}

	public String getFrameTitle() {
		// if (this.getAdapterType() == this.KEY_TYPE) return
		// this.getExpandedAdapter().getFrameTitle();
		if (frameTitle != null)
			return frameTitle;
		ObjectAdapter adapter = this;
		Object obj = adapter.getRealObject();
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(RemoteSelector
				.getClass(obj), obj);
		ClassProxy actualClass = cd.getRealClass();
		// if ((topAdapter == null) && (obj != null)) {
		if (obj != null) {
			String title = (String) adapter
					.getAttributeValue(AttributeNames.TITLE);
			if (title == null) {
				// Check the class attributes
				try {
					Attribute titleAttribute = AttributeManager
							.getEnvironment().getAttribute(
									 ReflectUtil.toMaybeProxyTargetClass(obj).getName(),
									AttributeNames.TITLE);
					if (titleAttribute != null)
						title = (String) titleAttribute.getValue();
					else if (obj != adapter.computeAndMaybeSetViewObject())
						// title =
						// ClassDescriptor.toShortName( IntrospectUtility.toMaybeProxyTargetClass(obj).getName());
						title = AClassDescriptor.toShortName(actualClass
								.getName());
					else {
						ClassProxy titleClass = ReflectUtil.toMaybeProxyTargetClass(getPropertyClass(), getRealObject());
//						if (titleClass.equals(IntrospectUtility.proxyClass())) {
//						if (IntrospectUtility.isProxyClass(titleClass)) {
//
//							ClassProxy targetClass = IntrospectUtility.getProxyTargetClass(getRealObject()) ;
//							if (targetClass != null)
//								titleClass = targetClass;					
//							
//						}
						title = AClassDescriptor.toShortName(titleClass.getName());
					}
					// title = (String)
					// AttributeManager.getEnvironment().getAttribute(obj.getClass().getName(),
					// AttributeNames.TITLE).getValue();
				} catch (Exception e) {
					// title =
					// ClassDescriptor.getMethodsMenuName(obj.getClass());
					// title =
					// ClassDescriptor.getMethodsMenuName(adapter.getPropertyClass());
					// title =
					// ClassDescriptor.toShortName(adapter.getPropertyClass().getName());
					/*
					 * if (obj != adapter.getViewObject()) title =
					 * ClassDescriptor.toShortName(obj.getClass().getName());
					 * else title =
					 * ClassDescriptor.toShortName(adapter.getPropertyClass().getName());
					 */
					e.printStackTrace();
					// System.out.println(e);
				}
			}
			frameTitle = title;
			return title;
		}
		frameTitle = "";
		return "";
	}

	// Link a bound property to an adaptor
	// This is done by registering the adaptor as
	// a PropertyListener of the object that contains the property
	// We first check to see if there is a property specific registration
	// method. If this doesnt exist we use the generic registration
	// method.
	public static void linkPropertyToAdapter(Object realObject,
			String propertyName, ObjectAdapter adaptor) {
		// registerListeners should do this
			
	}

	public static void linkPropertyToAdapterWorking(Object realObject,
			String propertyName, ObjectAdapter adaptor) {
		// If the property is a bound property
		// add the adaptor as a listener to this property.
		// The signatures we are looking for are
		// void addFooChangedListener(PropertyChangeListener l) and
		// void addPropertyChangeListener(PropertyChangeListener l)
		try {
			maybeAddRemotePropertyChangeListener(realObject, adaptor);
			if (propertyName == null)
				return;
			if (realObject == null)
				return;
			ClassProxy parentClass =  ReflectUtil.toMaybeProxyTargetClass(realObject);
			ClassProxy[] params = new ClassProxy[1];
			// params[0] =
			// RemoteSelector.forName("java.beans.PropertyChangeListener");
			params[0] = parentClass
					.forName("java.beans.PropertyChangeListener");

			MethodProxy addListenerMethod = null;
			try {
				char chars[] = propertyName.toCharArray();
				chars[0] = Character.toUpperCase(chars[0]);
				String methodName = "add" + new String(chars)
						+ "ChangedListener";
				addListenerMethod = parentClass.getMethod(methodName, params);
				// } catch (NoSuchMethodException e1) {
			} catch (Exception e1) {
				try {
					addListenerMethod = parentClass.getMethod(
							"addPropertyChangeListener", params);
				} catch (NoSuchMethodException e2) {
					addListenerMethod = null;
				}
			}

			PropertyChangeListener listener = (PropertyChangeListener) adaptor;

			// Object[] args = {adaptor};
			Object[] args = { listener };
			if (addListenerMethod != null) {
				try {
					addListenerMethod.invoke(realObject, args);
					Tracer.info("Added ObjectEditor object for " + listener + " as propertyChangeListener of property:" + propertyName  + " of " + realObject);

				} catch (Exception e) {
					// Nothing matters any more
					// 

					// e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	// getAdapterAttributes()
	// Return a Vector of attributes for the specified object
	// after inspecting 4 sources
	// 1. The object class
	// 2. The parent object class
	// 3. The object instance
	// 4. the parent object instance.
	static public void setAdapterAttributes(ObjectAdapter adaptor,
			Object object, Object parentObject, String fieldname) {
		Vector list;
		Attribute attrib;
		// attrib = new Attribute("preferredWidget",
		// componentMapping.getDefaultComponent(object.getClass().getName()));
		// adaptor.setLocalAttribute(attrib);
		/*
		 * if (fieldname.equals("root")) { attrib = new Attribute("label",
		 * fieldname); adaptor.setLocalAttribute(attrib); }
		 */
		// * commenting out to check secuity
		LocalAttributeDescriptor desc = getLocalAttributeDescriptor(object);
		if (desc != null) {
			desc.addLocalAttributeListener(adaptor);
			list = desc.getAttributes();
		} else
			list = null;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				adaptor.setLocalAttribute((Attribute) list.elementAt(i));
			}
		}
		list = getInstanceAttributes(parentObject);
		if (list != null) {
			Vector list1 = ObjectAdapter.getFieldAttributes(list, fieldname);
			for (int i = 0; i < list1.size(); i++) {
				adaptor.setLocalAttribute((Attribute) list1.elementAt(i));
			}
		}
		// */
	}

	public static LocalAttributeDescriptor getLocalAttributeDescriptor(
			Object obj) {
		if (bus.uigen.uiFrame.appletMode)
			return null;
		try {
			if (obj == null)
				return null;
			ClassProxy c =  ReflectUtil.toMaybeProxyTargetClass(obj);
			FieldProxy[] fields = c.getDeclaredFields();
			LocalAttributeDescriptor desc = null;
			for (int i = 0; i < fields.length; i++) {
				// if
				// (fields[i].getType().getName().equals("bus.uigen.LocalAttributeDescriptor"))
				// {
				// this makes no sense
				// if
				// (LocalAttributeDescriptor.class.isAssignableFrom(fields[i].getType()))
				// {
				if ( ReflectUtil.toMaybeProxyTargetClass(LocalAttributeDescriptor.class)
						.isAssignableFrom(fields[i].getType())) {
					try {
						desc = (LocalAttributeDescriptor) fields[i].get(obj);
					} catch (Exception e) {
					}
					break;
				}

			}
			return desc;
		} catch (Exception e) {
			return null;
		}

	}

	public static Vector getInstanceAttributes(Object obj) {
		if (obj == null)
			return null;
		LocalAttributeDescriptor desc = getLocalAttributeDescriptor(obj);
		if (desc == null)
			return null;
		else
			return CopyAttributeVector.copyVector(desc.getAttributes());
	}

	boolean synthesizedLabel = false;

	public void setSynthesizedLabel(boolean newVal) {
		synthesizedLabel = newVal;
	}

	public boolean getSynthesizedLabel() {
		return synthesizedLabel;
	}

	public void setSynthesizedLabel(String label) {
		setLabel(label);
		// setDisplayedPropertyName(getLabelWithoutSuffix());
		setSynthesizedLabel(true);
		setDisplayedPropertyName();
	}

	public void maybeSetLabel(String label) {

		// int i = 0;
		// commenting it out to see if getComputedLabel works
		/*
		 * if (!getSynthesizedLabel()) setLabel(label);
		 */

	}

	public boolean getSort() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.SORT);
		if (retVal != null)
			return retVal;
		return false;
	}

	public String getPrimaryProperty() {
		String retVal = (String) getMergedTempOrDefaultAttributeValue(AttributeNames.PRIMARY_PROPERTY);
		if (retVal != null)
			return retVal;
		return getComputedPrimaryProperty();
		// getNameProperty();
	}

	public String getComputedPrimaryProperty() {

		return getNameProperty();
	}

	public boolean getListSortUserObject() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.LIST_SORT_USER_OBJECT);
		if (retVal != null)
			return retVal;
		return false;
	}

	public boolean getHashtableSortKeys() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.HASHTABLE_SORT_KEYS);
		if (retVal != null)
			return retVal;
		return true;
	}

	public boolean getUserObjectIsFirstStaticChild() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.USER_OBJECT_IS_FIRST_STATIC_CHILD);
		if (retVal != null)
			return retVal;
		return true;
	}

	public boolean getNestedRelation() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.NESTED_RELATION);
		if (retVal != null)
			return retVal;
		return false;
	}
	
	public boolean isCompositeShape() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.IS_COMPOSITE_SHAPE);
		if (retVal != null)
			return retVal;
		return false; // should never reach here
	}
	
	public boolean isShapeSpecificationRequired() {
		Boolean retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.IS_SHAPE_SPECIFICATION_REQUIRED);
		if (retVal != null)
			return retVal;
		return false; // should never reach here
	}

	// public String getSortProperty() {
	public Object getSortProperty() {
		// String retVal = (String)
		// getMergedOrTempAttributeValue(AttributeNames.SORT_PROPERTY);
		Object retVal = getMergedTempOrDefaultAttributeValue(AttributeNames.SORT_PROPERTY);
		if (retVal != null)
			return retVal;
		return getComputedSortProperty();
		// getNameProperty();
	}

	public Vector getSortVectorPath() {
		// String retVal = (String)
		// getMergedOrTempAttributeValue(AttributeNames.SORT_PROPERTY);
		return (Vector) getMergedTempOrDefaultAttributeValue(AttributeNames.SORT_VECTOR_PATH);

		// getNameProperty();
	}

	public String getComputedSortProperty() {

		return getNameProperty();
	}

	public void setLabel(String label) {
		if (label == null)
			return;
		this.setTempAttributeValue(AttributeNames.LABEL, label);
		if (getGenericWidget() != null)
			getGenericWidget().setLabel(label);
	}

	public void setLabelled(boolean newVal) {
		this.setTempAttributeValue(AttributeNames.LABELLED, newVal);
	}
	
	public void setScrolled(boolean newVal) {
		this.setTempAttributeValue(AttributeNames.SCROLLED, newVal);
	}

	public void setTrueLabel(String label) {
		this.setTempAttributeValue(AttributeNames.TRUE_LABEL, label);
	}

	public String getDefinedLabel() {
		// return(String) getMergedOrTempAttributeValue(AttributeNames.LABEL);
		return (String) getMergedAttributeValue(AttributeNames.LABEL);
	}

	public String getComputedLabelWithoutSuffix() {
		String retVal = (String) getTempAttributeValue(AttributeNames.LABEL);
		if (retVal == null) {
			if (toStringAsLabel() && getRealObject() != null)
				retVal = getRealObject().toString();
			else if (userObjectAsLabel() && getUserObject() != null)
				retVal = getUserObject();
			else
			{
				// dont need this as isLabelled takes care of it
				// if (this.isTopDisplayedAdapter())
				// return "";
				if (isVectorChild() && getRealVectorIndex() >= 0) {
					// retVal = "" + (getVectorIndex() + 1);
					retVal = "" + (getRealVectorIndex() + 1);
				} else if (isVectorChild() && getVectorIndex() >= 0) {
					retVal = "" + (getVectorIndex() + 1);
					//retVal = "" + (getRealVectorIndex() + 1);				
				} else if (isHashtableKey()) {
					retVal = toObjectString();
				} else if (isHashtableValue()) {
					ObjectAdapter keyAdapter = getKeyAdapter();
					if (keyAdapter != this)
						retVal = keyAdapter.getLabelWithoutSuffix();
					else
						retVal = util.misc.Common.beautify(propertyName);
				} else /* if (isProperty()) */{
					retVal = util.misc.Common.beautify(propertyName);
				}
			}
		}
		return retVal;
	}

	public void setAttributeRelativeToTopClass(String attribute, Object newVal) {
		CompositeAdapter ancestorAdapter = (CompositeAdapter) getTopAdapter();
		ClassProxy ancestorClass = ancestorAdapter.getPropertyClass();

		/*
		 * uiContainerAdapter ancestorAdapter = adapter.getParentAdapter(); if
		 * (rowAdapter != null) ancestorAdapter = rowAdapter.getParentAdapter();
		 */

		String relativePath = getGenericStringPathRelativeTo(ancestorAdapter);
		// ObjectEditor.setPropertyAttribute(parentClass,
		// adapter.getComponentName(), AttributeNames.DEFAULT_EXPANDED, expand);
		ObjectEditor.setPropertyAttribute(ancestorClass, relativePath,
				attribute, newVal);
	}

	public ClassProxy setAttributeRelativeToTableClass(String attribute, Object newVal) {
		// uiContainerAdapter ancestorAdapter = (uiContainerAdapter)
		// getTableAdapter();
		//uiContainerAdapter rowAncestor = getFlatTableRowAncestor();
		//uiContainerAdapter topRow = getTopFlatTableRow();
		CompositeAdapter topRow = getFlatTableRowAncestor();
		if (topRow == null)
			topRow = getParentAdapter();
			//rowAncestor= getGrandParentAdapter();
		// uiContainerAdapter ancestorAdapter = (uiContainerAdapter)
		// getFlatTableRowAncestor().getParentAdapter();
		CompositeAdapter ancestorAdapter = topRow.getParentAdapter();
		//uiContainerAdapter ancestorAdapter = rowAncestor;
		ClassProxy ancestorClass = ancestorAdapter.getPropertyClass();

		/*
		 * uiContainerAdapter ancestorAdapter = adapter.getParentAdapter(); if
		 * (rowAdapter != null) ancestorAdapter = rowAdapter.getParentAdapter();
		 */

		String relativePath = getGenericStringPathRelativeTo(ancestorAdapter);
		// ObjectEditor.setPropertyAttribute(parentClass,
		// adapter.getComponentName(), AttributeNames.DEFAULT_EXPANDED, expand);
		ObjectEditor.setPropertyAttribute(ancestorClass, relativePath,
				attribute, newVal);
		return ancestorClass;
	}

	public void setSiblingAttributeRelativeToTopClass(String siblingName,
			String attribute, Object newVal) {
		CompositeAdapter ancestorAdapter = (CompositeAdapter) getTopAdapter();
		ClassProxy ancestorClass = ancestorAdapter.getPropertyClass();

		String relativePath = getSiblingGenericStringPathRelativeTo(
				ancestorAdapter, siblingName);
		// ObjectEditor.setPropertyAttribute(parentClass,
		// adapter.getComponentName(), AttributeNames.DEFAULT_EXPANDED, expand);
		ObjectEditor.setPropertyAttribute(ancestorClass, relativePath,
				attribute, newVal);
	}

	public Object getAttributeRelativeToTopClass(String attribute, Object newVal) {
		CompositeAdapter ancestorAdapter = (CompositeAdapter) getTopAdapter();
		ClassProxy ancestorClass = ancestorAdapter.getPropertyClass();

		String relativePath = getGenericStringPathRelativeTo(ancestorAdapter);
		// ObjectEditor.setPropertyAttribute(parentClass,
		// adapter.getComponentName(), AttributeNames.DEFAULT_EXPANDED, expand);
		return ObjectEditor.getPropertyAttribute(ancestorClass, relativePath,
				attribute);
	}

	public CompositeAdapter getTableAdapter() {
		CompositeAdapter rowAdapter = (CompositeAdapter) getFlatTableRowAncestor();
		/*
		 * if (rowAdapter == null) rowAdapter = getParentAdapter();
		 */
		if (rowAdapter == null)
			return this.getGrandParentAdapter();
		while (rowAdapter.getParentAdapter().isFlatTableRow())
			rowAdapter = rowAdapter.getParentAdapter();
		CompositeAdapter  retVal = rowAdapter.getParentAdapter();
		return retVal;
		/*
		if (retVal.isOnlyChild)
			return retVal.getParentAdapter();
		else
			return retVal;
			*/
		//return rowAdapter.getParentAdapter();
	}
	public ObjectAdapter getAncestorWhoseClassIs(ClassProxy ancestorClass) {
		if (getPropertyClass().equals(ancestorClass))
			return this;
		if (getParentAdapter() == null)
			return null;
		return getParentAdapter().getAncestorWhoseClassIs(ancestorClass);		
		
	}
	
	public ObjectAdapter getAncestorWhoseClassIs(String ancestorClass) {
		if (getPropertyClass().getName().equals(ancestorClass))
			return this;
		if (getParentAdapter() == null)
			return null;
		return getParentAdapter().getAncestorWhoseClassIs(ancestorClass);		
		
	}
	

	public CompositeAdapter getRowParentAdapter() {
		CompositeAdapter rowAdapter = (CompositeAdapter) getFlatTableRowAncestor();

		if (rowAdapter == null) 
				//|| rowAdapter.getParentAdapter().isOnlyChild())
			rowAdapter = getParentAdapter();

		return rowAdapter.getParentAdapter();
	}

	public ClassProxy setSiblingAttributeRelativeToTableClass(String siblingName,
			String attribute, Object newVal) {
		// uiContainerAdapter ancestorAdapter = (uiContainerAdapter)
		// getTopAdapter();
		/*
		 * uiContainerAdapter rowAdapter = (uiContainerAdapter)
		 * getFlatTableRowAncestor(); if (rowAdapter == null) rowAdapter =
		 * getParentAdapter();
		 */
		// uiContainerAdapter ancestorAdapter = getTableAdapter();
		CompositeAdapter ancestorAdapter = getRowParentAdapter();
		ClassProxy ancestorClass = ancestorAdapter.getPropertyClass();

		String relativePath = getSiblingGenericStringPathRelativeTo(
				ancestorAdapter, siblingName);
		// ObjectEditor.setPropertyAttribute(parentClass,
		// adapter.getComponentName(), AttributeNames.DEFAULT_EXPANDED, expand);
		ObjectEditor.setPropertyAttribute(ancestorClass, relativePath,
				attribute, newVal);
		return ancestorClass;
	}

	public Object getAttributeRelativeToTableClass(String attribute,
			Object newVal) {
		// uiContainerAdapter ancestorAdapter = (uiContainerAdapter)
		// getTopAdapter();
		CompositeAdapter ancestorAdapter = (CompositeAdapter) getRowParentAdapter();
		ClassProxy ancestorClass = ancestorAdapter.getPropertyClass();

		String relativePath = getGenericStringPathRelativeTo(ancestorAdapter);
		// ObjectEditor.setPropertyAttribute(parentClass,
		// adapter.getComponentName(), AttributeNames.DEFAULT_EXPANDED, expand);
		return ObjectEditor.getPropertyAttribute(ancestorClass, relativePath,
				attribute);
	}

	public String getLabel() {
		// return columnTitle();
//		if (isOnlyChild())
//			return "";
		// String retVal = (String)
		// getMergedAttributeValue(AttributeNames.LABEL);
		String retVal = (String) getDefinedLabel();
		if (retVal != null)
			return retVal;
		return getComputedLabel();

	}

	boolean copiedLabel = false;

	void setCopiedLabel(boolean newVal) {
		copiedLabel = newVal;
	}

	boolean getCopiedLabel() {
		return copiedLabel;
	}

	public String getComputedLabel() {		
//		if (isOnlyChild())
//			return "";
		Object retVal = getComputedLabelWithoutSuffix();
		if (AttributeNames.LABEL_IS_LEFT.equals(getLabelPosition()))
			return retVal + getDefinedLabelSuffixOrSystemDefault();
		else
			return retVal + getDefinedLabelSuffixOrEmptyString();

		/*
		 * retVal = (String) getTempAttributeValue(AttributeNames.LABEL); if
		 * (retVal == null) { if ( toStringAsLabel() && getRealObject() != null)
		 * retVal = getRealObject().toString(); else // dont need this as
		 * isLabelled takes care of it //if (this.isTopDisplayedAdapter()) //
		 * return ""; retVal = uiGenerator.beautify (propertyName); }
		 * 
		 * //retVal = padLabel(retVal); return (String) retVal; //return
		 * (String) getTempAttributeValue(AttributeNames.LABEL);
		 * 
		 */
	}
	
	public boolean getCreateWidgetShell() {
		
		return !getUIFrame().isOnlyGraphicsPanel() &&
				
				(Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.CREATE_WIDGET_SHELL);
		
	}

	public String getLabelWithoutSuffix() {
		// return columnTitle();
//		if (isOnlyChild())
//			return "";
		// String retVal = (String)
		// getMergedAttributeValue(AttributeNames.LABEL);
		String retVal = (String) getDefinedLabel();
		if (retVal != null)
			return retVal;
		else
			return getComputedLabelWithoutSuffix();
		/*
		 * retVal = (String) getTempAttributeValue(AttributeNames.LABEL); if
		 * (retVal == null) { if ( toStringAsLabel() && getRealObject() != null)
		 * retVal = getRealObject().toString(); else // dont need this as
		 * isLabelled takes care of it //if (this.isTopDisplayedAdapter()) //
		 * return ""; retVal = uiGenerator.beautify (propertyName); }
		 * 
		 * //retVal = padLabel(retVal); return (String) retVal; //return
		 * (String) getTempAttributeValue(AttributeNames.LABEL);
		 * 
		 */
	}

	public String getLabelOld() {
		// return columnTitle();
		if (isOnlyChild())
			return "";
		String retVal = (String) getMergedTempOrDefaultAttributeValue(AttributeNames.LABEL);
		if (retVal != null)
			return retVal;
		if (toStringAsLabel() && getRealObject() != null)
			return getRealObject().toString();
		if (this.isTopDisplayedAdapter())
			return "";

		retVal = util.misc.Common.beautify(propertyName);
		/*
		 * if (retVal != null) { if
		 * (getLabelPosition().equals(AttributeNames.LABEL_IS_LEFT)) retVal =
		 * retVal + ":"; }
		 */
		// retVal = padLabel(retVal);
		return (String) retVal;
		// return (String) getTempAttributeValue(AttributeNames.LABEL);
	}

	public boolean getGraphicsView() {
		Object retVal = (Object) getMergedTempOrDefaultAttributeValue(AttributeNames.GRAPHICS_VIEW);
		if (retVal == null)
			return true;
		return (Boolean) retVal;
	}

	static final int ELIDE_LENGTH = 20;

	public String getElideString() {
		// return columnTitle();
		Object retVal = (String) getMergedTempOrDefaultAttributeValue(AttributeNames.ELIDE_STRING);
		if (retVal != null)
			return (String) retVal;
		if (getElideStringIsToString() && getRealObject() != null)
			return getRealObject().toString();
		// return "< " + toTextLine() + "... >";
		String textLine = toTextLine();
		if (textLine.length() >= ELIDE_LENGTH)
			return textLine.substring(0, ELIDE_LENGTH) + "...";
		// return "< " + toTextLine().substring(0, Math.min(textLine.length(),
		// ELIDE_LENGTH)) + "... >";
		else
			return textLine;
		/*
		 * ClassProxy c = getPropertyClass(); if (c == null) // c =
		 * Object.class; StandardProxyTypes.objectClass(); return " < " +
		 * ClassDescriptor.getMethodsMenuName(c) + "... >";
		 */

	}

	public Boolean getElideStringIsToString() {
		// return columnTitle();
		Object retVal = (Boolean) getMergedTempOrDefaultAttributeValue(AttributeNames.ELIDE_STRING_IS_TOSTRING);
		if (retVal != null)
			return (Boolean) retVal;
		return false;

	}

	public String getElideImage() {
		// return columnTitle();
		String retVal = (String) getMergedTempOrDefaultAttributeValue(AttributeNames.ELIDE_IMAGE);
		return retVal;

	}

	public String getTrueLabel() {
		// return columnTitle();
		return (String) getMergedTempOrDefaultAttributeValue(AttributeNames.TRUE_LABEL);
	}

	public Object getExpansionObject() {
		// return getExpansionObject();
		return getExpansionObjectAttribute();

	}

	public ObjectAdapter getExpandedAdapter() {
		Object newExpansionObject = getExpansionObject();
		if (newExpansionObject != null && expansionObject != newExpansionObject) {
			expansionObject = newExpansionObject;
			expansionAdapter = uiGenerator.toTopAdapter(newExpansionObject);
			return expansionAdapter;
		} else if (expansionAdapter != null)
			return expansionAdapter;
		else
			return getVirtualParent().getExpandedAdapter(this);
		// return null;
		// return this.getObject();
	}

	public boolean elideChildrenByDefault() {
		return true;
	}

	boolean recursive;

	public boolean isRecursive() {
		return recursive;

	}

	public void setIsRecursive(boolean newVal) {
//		if (newVal) {
//			Message.warning(this + " is probably recursive");
//		}
		recursive = newVal;
	}

	boolean hasCellEditor = false;

	public boolean hasCellEditor() {
		return hasCellEditor;
	}

	public void setHasCellEditor(boolean newValue) {
		hasCellEditor = newValue;
	}

	public static void refreshAttributes(ObjectAdapter topAdapter,
			ClassProxy parentClass) {
		// Class parentClass = adapter.getViewObject().getClass();
		if (parentClass == null)
			return;
		UIAttributeManager environment = AttributeManager.getEnvironment();
		if (environment == null)
			return;
		environment.removeFromAttributeLists(parentClass.getName());

		(new RecomputeAttributesAdapterVisitor(topAdapter)).traverse();
		// refresh done by the command that invoked this operation
		// topAdapter.implicitRefresh();

		/*
		 * uiGenerator.deepProcessAttributes(topAdapter);
		 * uiGenerator.deepElide(topAdapter);
		 * topAdapter.getUIFrame().validate();
		 */

	}

	public void refreshAttributes(ClassProxy parentClass) {
		refreshAttributes(this, parentClass);

	}
	
	Boolean isTreeNode;

	public boolean isTreeNode() {
		if (getUIFrame() != null && // for non interactive
				getUIFrame().hasOnlyTreeManualContainer())
			return true;
		if (getWidgetAdapter() != null
				&& getWidgetAdapter().getClass() == TreeAdapter.class)
			return true;
		if (getParentAdapter() == null || getWidgetAdapter() != null)
			return false;
		return getParentAdapter().isTreeNode();
	}
//	public boolean isTreeNode() {
//		if (isTreeNode == null) {
//		if (getWidgetAdapter() != null
//				&& getWidgetAdapter().getClass() == TreeAdapter.class)
//			isTreeNode = true;
//		else if (getParentAdapter() == null || getWidgetAdapter() != null)
//			isTreeNode = false;
//		else
//		   isTreeNode = getParentAdapter().isTreeNode();
//		}
//		return isTreeNode;
//	}

	public Object getAdaptedModel() {
		// TreeNodeAdapter foo = (TreeNodeAdapter) this;
		return computeAndMaybeSetViewObject();
	}

	Set visitedObjects = new HashSet();

	public void addVisitedObject(Object element) {
		visitedObjects.add(element);
	}

	public boolean hasObjectBeenVisited(Object element) {
		return element != null && visitedObjects.contains(element);
	}

	public void clearVisitedObjects() {
		visitedObjects.clear();
	}

	public ObjectAdapter getSiblingAdapter(String componentName) {
		// return getParentAdapter().getChildAdapterAtIndex(componentName);
		return getParentAdapter().getVisibleOrInvisibleChildAdapterAtRealIndex(componentName);
	}

	public Vector<String> getSiblingComponentNames() {
		if (getParentAdapter() == null)
			return null;
		Vector<ObjectAdapter> children = getParentAdapter()
				.getChildrenVector();

		Vector<String> retVal = new Vector();
		for (int i = 0; i < children.size(); i++) {
			ObjectAdapter sibling = children.get(i);
			if (sibling != this)
				retVal
						.add(getParentAdapter().getChildAdapterRealIndex(
								sibling));
		}
		return retVal;
	}
//	// this stuff really belongs in ACompositeAdapter
//	ShapeListMouseClickListener shapeListMouseClickListener;
//	public ShapeListMouseClickListener getShapeListMouseClickListener() {
//		return shapeListMouseClickListener;
//	}
//	public void mouseClicked(List<RemoteShape> theShapes, MouseEvent mouseEvent) {
//		if (shapeListMouseClickListener == null)
//			return;		
//		shapeListMouseClickListener.mouseClicked(theShapes, new VirtualMouseEvent(mouseEvent, 1));
//	}
//	public void registerAsMouseClickListener(ShapeEventNotifier shapeEventNotifier) {
//		  if (shapeListMouseClickListener == null) return;
//		  shapeEventNotifier.addMouseClickListener(this);
//	  }

	public static ObjectAdapter nearestNestedShapesContainer(ObjectAdapter candidate) {
		  if (candidate == null) return null;
		  if (candidate.isNestedShapesContainer())
			  return candidate;
		  return nearestNestedShapesContainer(candidate.getParentAdapter());
		  
	  }
	public  boolean isGraphicsCompositeObject() {
		if (!(this instanceof CompositeAdapter))
				return false;
		CompositeAdapter parentAdapter = getParentAdapter();
		if (parentAdapter == null)
			return false;
		if (parentAdapter.isNestedShapesContainer() || parentAdapter.hasOnlyGraphicalDescendents)
			return true;
		return parentAdapter.isNestedShapesContainer();
	}
	public boolean isGraphicsLeafObject() {
		return false;
	}
	
	public boolean isDrawingAdapter() {
		if (getUIFrame() == null)
			return false;
		return getUIFrame().getDrawingAdapter() == this || getRealObject() instanceof ShapesList;
		
	}
	
	public String getPatternName() {
		return "";
	}
	
	 public List<ShapeObjectAdapter> getGraphicalDescendents() {
		  Vector<ShapeObjectAdapter> vector = new Vector();
		  return vector;		  
	  }
	 
	 public boolean hasNoComponents() {
	 	return false;
	 	}
		
	public void addAttributeDependency(AttributeDependency anAttributeDependency) {
		if (attributeDependencies.contains(anAttributeDependency))
			return;
		attributeDependencies.add(anAttributeDependency);
	}
	
	public void notifyAttributeDependencies(PropertyChangeEvent aPropertyChangeEvent) {
		for (AttributeDependency anAttributeDependency: attributeDependencies) {
			PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, anAttributeDependency.getAttributeName(), aPropertyChangeEvent.getNewValue(),  aPropertyChangeEvent.getOldValue());
			anAttributeDependency.getObjectAdapter().propertyChange(propertyChangeEvent);	
			
		}
	}
	
	
	public HashcodeSet getObjectsInPathToRoot() {
		if (objectsInPathToRoot == null) {
			objectsInPathToRoot = new HashcodeSet();
			objectsInPathToRoot.add(getRealObject());
			if (!isTopAdapter())
				objectsInPathToRoot.addAll(getParentAdapter().getObjectsInPathToRoot());
			
		}
		return objectsInPathToRoot;
	}

	public  String toReferencePath(String property, int pos) {
		  if (property.equals("root"))
			  return property;
		  String reference = getReference (property, pos);
		  String component = AttributeNames.PATH_SEPARATOR + reference;
		  String rootClass =  getTopAdapter().getRealObject().getClass().getSimpleName();
//		  return "root" + getPath() + component;
		  return rootClass + getPath() + component;

		  
//		  String component = AttributeNames.PATH_SEPARATOR + property;
//	
//		  if (property.equals(AttributeNames.ANY_ELEMENT)) {
//			  component = AttributeNames.PATH_SEPARATOR + pos ;
//		  }
//		  return "root" + getPath() + component;
	//	  return "root" + adapter.getPath();
		  
	  }
	
	// meed to rewrite this
	public static String getReference(String property, int pos) {
		String component = property;
		  if (property.equals("root"))
			  return property;
		  if (property.equals(AttributeNames.ANY_ELEMENT)) {
			  component = "" + pos ;
			  return component;
		  }
		  return component;
		
	}
	
	// meed to rewrite this
	public String getReference() {
		return getReference(getPropertyName(), getIndex());
		
	}
	
	public boolean hasTreeLogicalStructure() {
		return hasTreeLogicalStructure;
	}
	
	public void notTree() {
		hasTreeLogicalStructure = false;
		if (getParentAdapter() != null) {
			getParentAdapter().notTree();
		}
		
	}
	
	public Object toCanonicalForm() {
		return getRealObject();
	}
		

	Object objectOrAdapter(ObjectAdapter anElement) {
		if (anElement instanceof PrimitiveAdapter) {
			return anElement.getRealObject();
		}
		return anElement;
	}
	public boolean hasCommands() {
		return getWidgetAdapter() != null && getWidgetAdapter().hasCommands();
	}

	public boolean isAttributeChangePending() {
		return attributeChangePending;
	}

	public void setAttributeChangePending(boolean attributeChangePending) {
		this.attributeChangePending = attributeChangePending;
	}
	
}
