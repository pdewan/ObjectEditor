package bus.uigen.oadapters;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import shapes.RemoteShape;
import slgc.ShapeEventNotifier;
import slgc.ShapeListAWTMouseListener;
import util.trace.Tracer;
import bus.uigen.OEFrame;
import bus.uigen.WidgetAdapterInterface;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.Selectable;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.misc.ShapeListMouseClickListener;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.translator.TranslatorRegistry;
import bus.uigen.view.WidgetShell;
import bus.uigen.visitors.IsEditedAdapterVisitor;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.events.VirtualMouseEvent;

public abstract class CompositeAdapter extends ObjectAdapter implements CompositeAdapterInterface {
//ShapeListAWTMouseListener {		public final String HOMOGENEOUS = "homogeneous";	public final String CHILDREN_HORIZONTAL = "childrenHorizontal";
	boolean childTextMode = false;
	
	ObjectAdapter userObjectAdapter;
	public ObjectAdapter getUserObjectAdapter() {
		return userObjectAdapter;
	}
	
	public void setUserObjectAdapter(ObjectAdapter newVal) {
		userObjectAdapter = newVal;
	}
	public boolean hasNoComponents() {
		return !isDynamic() && getChildCount() == 0;
//		return !isDynamic() && hasNoProperties();

	}
	
	
	
	public abstract Object getComponentValue(String componentName);
		
   ObjectAdapter changedChildInAtomicWidget = this;
   void setChangedChildInAtomicWidget (ObjectAdapter child) {
	   changedChildInAtomicWidget = child;
   }
   public ObjectAdapter getChangedChildInAtomicWidget() {
	   return changedChildInAtomicWidget;
   }
   void resetChangedChildInAtomicWidget () {
	   changedChildInAtomicWidget = this;
   }
  // Constructor.
  public CompositeAdapter() throws RemoteException  {
  }
  public Vector<String> getColumnTitles() {	
	  Vector<String> retVal = new Vector();
	  /*
	  if (isLabelled() && !"".equals(getLabel()) )
			  retVal.addElement("");
			  */
	  WidgetAdapterInterface wa = getWidgetAdapter();
	  if (wa != null) {
		  Vector <ObjectAdapter> displayedChildren = wa.getChildrenAdaptersInDisplayOrder();
		  for (int i = 0; i < displayedChildren.size(); i++) {
			  retVal.add(displayedChildren.elementAt(i).columnTitle());
		  }
		  return retVal;
	  }
	  Vector children = this.getChildrenVector();
	  for (int i = 0; i < children.size(); i++) {
		  retVal.add(((ObjectAdapter) children.elementAt(i)).columnTitle());
	  }
	  return retVal;	
	  /*
	  Enumeration children = this.getChildAdapters();
	  while (children.hasMoreElements()) {
		  retVal.add(((uiObjectAdapter) children.nextElement()).columnTitle());
	  }
	  return retVal;	
	  */		  
	  
  }
  public Vector<ObjectAdapter> getColumnAdapters() {	
	  Vector<ObjectAdapter> retVal = new Vector();
	  // now we will allow nested column titles
	  /*
	  if (getAdapterType() == PROPERTY_TYPE)
		  return retVal;
		  */
	  /*
	  if (isLabelled() && !"".equals(getLabel()) )
			  retVal.addElement("");
			  */
	  WidgetAdapterInterface wa = getWidgetAdapter();
	  if (wa != null) {
		  Vector <ObjectAdapter> displayedChildren = wa.getChildrenAdaptersInDisplayOrder();
		  for (int i = 0; i < displayedChildren.size(); i++) {
			  //retVal.add(displayedChildren.elementAt(i).columnTitle());
			  ObjectAdapter childAdapter = displayedChildren.elementAt(i);
			  //if (childAdapter.getAdapterType() != PROPERTY_TYPE)
			  retVal.add(displayedChildren.elementAt(i));
		  }
		  return retVal;
	  }
	  Vector<ObjectAdapter> children = this.getChildrenVector();
	  for (int i = 0; i < children.size(); i++) {
		  //retVal.add(((uiObjectAdapter) children.elementAt(i)).columnTitle());
		  retVal.add(((ObjectAdapter) children.elementAt(i)));
	  }
	  return retVal;	
	  /*
	  Enumeration children = this.getChildAdapters();
	  while (children.hasMoreElements()) {
		  retVal.add(((uiObjectAdapter) children.nextElement()).columnTitle());
	  }
	  return retVal;	
	  */		  
	  
  }
  boolean hasOnlyGraphicalDescendents = false;
  boolean hasOnlyGraphicalDescendentsSet = false;
  
  @Override
  public boolean hasOnlyGraphicsDescendents() {
	  setHasOnlyGraphicsDescendents();
	  return hasOnlyGraphicalDescendents;
  }
  public void setHasOnlyGraphicsDescendents() {
	  	if (hasOnlyGraphicalDescendentsSet)
	  		return;		
	  	hasOnlyGraphicalDescendentsSet = true;
		Boolean retVal = (Boolean) getMergedAttributeValue(AttributeNames.ONLY_GRAPHIICAL_DESCENDENTS);
		if (retVal != null ) {
			hasOnlyGraphicalDescendents = retVal;
			return;
		}
		Vector<ObjectAdapter> children = this.getChildrenVector();
		if (children.size() == 0) {
			// added this hack because otherwise there were spurious levels in the title matrix
			// added isFlatTableComponent so that vectors not in a flat table are displayed
			//if (isDynamic() && !isTopDisplayedAdapter && !getParentAdapter().isDynamic())
			if (isDynamic() && !isTopDisplayedAdapter && !getParentAdapter().isDynamic() && isFlatTableComponent())
				hasOnlyGraphicalDescendents = true;
			else
		//if (children.size() == 0) {
				hasOnlyGraphicalDescendents = false;
			return;
		}
		//boolean retVal = false;
		for (int i = 0; i < children.size(); i++) {
			ObjectAdapter child = children.elementAt(i);
			if (!child.hasOnlyGraphicsDescendents()) {
				hasOnlyGraphicalDescendents = false;
				return;
			}
		}
		hasOnlyGraphicalDescendents = true;
	}
  public List<ShapeObjectAdapter> getGraphicalDescendents() {
	  Vector<ShapeObjectAdapter> vector = new Vector();
	  addGraphicalDescendents(vector);
	  return vector;
	  
  }
  public void addGraphicalDescendents (List<ShapeObjectAdapter> list) {
	  Vector<ObjectAdapter> children = this.getChildrenVector();
		if (children.size() == 0) {
			return;
		}
		//boolean retVal = false;
		for (int i = 0; i < children.size(); i++) {
			ObjectAdapter child = children.elementAt(i);
			child.addGraphicalDescendents(list);
		}
		
	}
  public void removeGraphicalDescendents() {
		List<ShapeObjectAdapter> adapters = getGraphicalDescendents();
		for (int i=0; i < adapters.size(); i++) {
			ShapeObjectAdapter shape = adapters.get(i);
			shape.removeShape();
		}
	}
  public void addGraphicalDescendents() {
		List<ShapeObjectAdapter> adapters = getGraphicalDescendents();
		for (int i=0; i < adapters.size(); i++) {
			ShapeObjectAdapter shape = adapters.get(i);
			shape.addShape();
		}
	}  public void refreshValue(Object newValue) {	  //System.out.println("uiContainerAdapter-Set Value: " + newValue);
	  refreshValue(newValue, false);  }
  //int maxComponentNameLength = 0;
	public int getMaxComponentNameLength() {
		return maxComponentNameLength;
	}
	
	public String getLargestDescendentLabel() {
		return largestDescendentLabel;
	}
	int maxDynamicComponentNameLength = 0;
	String maxDynamicComponentName = "";
	public int getMaxDynamicComponentNameLength() {
		return maxDynamicComponentNameLength;
	}	
	public String getMaxDynamicComponentName() {
		return maxDynamicComponentName;
	}
  public abstract void refreshValue(Object newValue, boolean forceUpdate);
  public abstract boolean isChildReadable(ObjectAdapter adapter);
  public abstract boolean isChildWriteable(ObjectAdapter adapter);
  public boolean isReadOnly(ObjectAdapter child) {
	  return true;
  }
 boolean foundUnlabeledComposite = false;
 
	public boolean foundUnlabeledComposite() {
		return foundUnlabeledComposite;
	}
boolean childrenKindSet = false;
boolean hasCompositeChild = false;
boolean hasPrimitiveChild = false;
public boolean getHasOnlyPrimitiveChildren () {
	return hasPrimitiveChild && !hasCompositeChild;
}
void setChildrenKind() {
	if (childrenKindSet) return;
	hasCompositeChild = false;
	hasPrimitiveChild = false;
	for (int i = 0; i < getChildAdapterCount(); i++) {
		ObjectAdapter a = getChildAdapterAt(i);
		if (a instanceof CompositeAdapter && !((CompositeAdapter) a).hasOnlyGraphicsDescendents())
			hasCompositeChild = true;
		if (a instanceof PrimitiveAdapter)
			hasPrimitiveChild = true;
		
	}
	childrenKindSet = true;
	
}
public boolean getHasOnlyCompositeChildren () {
	if (!childrenKindSet)
		setChildrenKind();
	return !hasPrimitiveChild && hasCompositeChild;
	
}
public boolean getHasPrimitiveAndCompositeChildren () {
	setChildrenKind();
	return hasPrimitiveChild && hasCompositeChild;
}

  public boolean isAddable(Object o) {		return false;	}  public boolean isChildDeletable(ObjectAdapter child) {		return false;	}
  public void deleteChild(ObjectAdapter child) {
												 }
  public boolean validateDeleteChild(ObjectAdapter child) {
	  return false;
  }
  public boolean validateInsertChild(int index, Object child) {
	  return false;
  }
  public boolean validateAddChild(Object child) {
	  return false;
  }
  public ClassProxy addableElementType () {
	  return null;
  }  public Object getOriginalValue() {
	  
		//return getViewObject(getRealObject());
	  return computeAndMaybeSetViewObject();
	}
  public void setDirection() {
	  //String direction = (String) getMergedAttributeValue(AttributeNames.DIRECTION);
	  String direction = getDirection(); // should not matter if tempAttribute is gotten
	  if ( direction == null) {		  
		  //direction = getDefaultDirection();
		  setDirection (getDefaultDirection());
		  //this.setTempAttributeValue (AttributeNames.DIRECTION, getDefaultDirection());	  }
	}
  /*  public String getDirection() {		//return direction;	  return (String) this.getTempAttributeValue(AttributeNames.DIRECTION);
  }
  */
  public boolean isHorizontal() {	
		//System.out.println (" " + this.getMergedAttributeValue("direction") + this);		//if (!"horizontal".equals(this.getMergedAttributeValue("direction")))		//System.out.println("is horizontal" + this.getDirection() + direction + this.getBeautifiedPath());    		if (!("horizontal".equals(this.getDirection())))
			return false;
		else
			return true;
	}    public abstract Object getChildValue(ObjectAdapter child);	  
   public void refresh() {	   /* maybe should exceute if for isParentedTopAdapter()
	  //if (isAtomic() || isParentedTopAdapter()){	  if (isParentedTopAdapter()){
		  atomicRefresh();
	      return;
       }
	   */	  //System.out.println(" normal objectadapter refresh" + this);
      //setValue(getViewObject());
	  refreshValue(getRealObject(), true);	//this.getGenericWidget().doLayout();	//this.getGenericWidget().validate();
  }
  
  public void setLocalAttributes(Vector list) {
    //super.setAttributes(list);	 super.setLocalAttributes(list);
    // Also invoke process attributes on this
    // adaptors children (just in case a field 
    // attribute was set).
    Enumeration children = getChildAdapters();
    while (children.hasMoreElements()) {
      ObjectAdapter a = (ObjectAdapter) children.nextElement();
      a.processAttributeList();
	  /*	  a.setDefaultAttributes();	  a.processAttributes();	  */
    }
  }
  public abstract void processDirection();
  @Override
  public void processAttributesWithDefaults() {
	  super.processAttributesWithDefaults();
	  processDirection();
  }
  public void processDrawingComponentColor() {
	  if (isTopAdapter()) {
			OEFrame oeFrame = getUIFrame();
			if (oeFrame != null) {
				VirtualComponent drawComponent = oeFrame.getDrawVirtualComponent();
				if (drawComponent != null) {
					drawComponent.setBackground(this.getDrawingPanelColor());
				}
			}
		}
  }
  
  @Override
  public void setDefaultAttributes() {
	  super.setDefaultAttributes();
	  setDirection();
	  setIsFlatTableRow();
	  /*
	  if (	  getParentAdapter() != null && 
			  getParentAdapter().childrenShowingColumnTitle()) {
		  setShowChildrenColumnTitle(true);
		  setColumnTitleStatus(ColumnTitleStatus.show);
	  } else if (getParentAdapter() != null && 
			  getParentAdapter().childrenHidingColumnTitle()) {
		  setShowChildrenColumnTitle(false);
		  setColumnTitleStatus(ColumnTitleStatus.hide);
	  }
	  */
  }
   public  void setDefaultSynthesizedAttributes() {	  super.setDefaultSynthesizedAttributes();
		if (!childrenCreated) return;
		maxComponentNameLength = 0;
		for (int i=0; i < getChildAdapterCount() ; i++) {
			//uiObjectAdapter a = getChildAdapterMapping(Integer.toString(i));			ObjectAdapter a = getChildAdapterAt(i);			a.setNameChild();
			if (a.isVisible() && a.isLabelled() && a.getRealObject() != null && !(a.getRealObject() instanceof Boolean)) {
				int oldMaxComponentNameLength = maxComponentNameLength;
				maxComponentNameLength = Math.max(a.computedLabelLength(), maxComponentNameLength) ;
				if (maxComponentNameLength != oldMaxComponentNameLength) {
					largestDescendentLabel = a.largestSynthesizedLabel();
				}				
			}
			if (a.isFlatTableRow() && a.isVisible()) {
				setTempAttributeValue(AttributeNames.HAS_FLAT_TABLE_ROW_DESCENDENT,true);
			}
		}
		//setIsFlatTableRow();
	}
  public void setMaxComponentNameLength(int newVal) {
	  // this check is inefficient but makes debugging easier
	  if (maxComponentNameLength != newVal)
	  maxComponentNameLength = newVal;
	  
  }
  public void setLargestDescendentLabel(String newVal) {
	  // this check is inefficient but makes debugging easier
//	  if (!largestDescendentLabel.equals(newVal))
	  largestDescendentLabel = newVal;
	  
  }
  public void setMaxDynamicComponentNameLength(int newVal) {
	  maxDynamicComponentNameLength  = newVal;	  
  }
  public void setMaxDynamicComponentName(String newVal) {
	  maxDynamicComponentName  = newVal;	  
  }  public  void initAttributes( Hashtable selfAttributes, Vector childrenAttributes) {
  	super.initAttributes(selfAttributes, childrenAttributes);
  	try {
  
  	 if (childrenAttributes != null) {
		Enumeration children = getChildAdapters();
		Enumeration childrenAttributesEnum = childrenAttributes.elements();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   Hashtable childAttributes = (Hashtable) childrenAttributesEnum.nextElement();
		   child.setLocalAttributes(childAttributes);
		  
		}
  	 }
  	 } catch (Exception e) {
  		e.printStackTrace();
  	 	//System.out.println(e);
  	 }
  	
  	
  }		


  public void moveGenericWidget(WidgetShell w, int position) {
    //Container container = (Container) getUIComponent();
    //container.remove(w);	//container.remove(w.getContainer());

	this.getWidgetAdapter().remove(w.getContainer());
    //container.add(w, position);	//container.add(w.getContainer(), position);
	getWidgetAdapter().add(w.getContainer(), position);
  }

  // Abstract methods that implement the naming
  // mechanism.

  public abstract ObjectAdapter  getChildAdapterMapping(String fieldName);
  public abstract void setChildAdapterMapping(String fieldName, ObjectAdapter a);
  public abstract void deleteChildAdapter(String fieldName);

  public abstract String getChildAdapterRealIndex(ObjectAdapter a);  public abstract ObjectAdapter getChildAdapterAtIndex(String key) ;
  public  ObjectAdapter getVisibleOrInvisibleChildAdapterAtRealIndex(String key) {
	  return getChildAdapterAtIndex(key);
  }

  public abstract Enumeration getChildAdapters();
  public abstract Enumeration getDynamicChildAdapters();
  public Vector<ObjectAdapter> getVisibleAndInvisibleDynamicChildAdapters () {
	  Vector<ObjectAdapter> retVal = new Vector();
	  return retVal;
  }
  public abstract boolean childUIComponentValueChanged (ObjectAdapter child,											  Object newValue, boolean initialChange);    transient boolean childrenCreated = false;
  public boolean childrenCreated() {
	  return childrenCreated;
  }  public static int computeIndex(ObjectAdapter child) {				int index;
				try {
					if (child.isTopAdapter()) {						//System.out.println(this.getAdapterIndex());
						index = Integer.parseInt(child.getAdapterIndex());
											} else {
						index = Integer.parseInt(child.getParentAdapter().getChildAdapterRealIndex(child));
						if (index < 0) {							System.out.println("ADAPTER  NOT FOUND");							System.out.println("ADAPTER " + child.getID());
						}					}
				} catch (NumberFormatException e) {
					//e.printStackTrace();					System.out.println("returning negative index");
					index = -1;
				}
				return index;	}  public Object getUserChange () {
  	if (getWidgetAdapter() != null && getWidgetAdapter().getType().equals("java.lang.String")) {
  		try {
  		return TranslatorRegistry.convert("java.lang.String", this.getPropertyClass().getName(), getWidgetAdapter().getUIComponentValue());
  		} catch (Exception e) {
  			return null;
  		}
  	}
  	else			return getRealObject();	}
  
	
    public boolean uiComponentValueChanged() {		return uiComponentValueChanged(false);	}
    /*
    public void uiComponentValueChanged(boolean initChange) {
		uiComponentValueChanged(initChange);
	}
	*/
	public void commandActionPerformed() {		uiComponentValueChanged(false);
		//propagateChange();	}	transient String direction = AttributeNames.VERTICAL;
	public boolean processDirection(String newDirection) {
		direction = newDirection;
		//System.out.println("process Direction" + direction + this.getBeautifiedPath());		return this.getWidgetAdapter().processDirection(newDirection);	}
	boolean forceRebuild = false;
	public boolean getForceRebuild() {
		return forceRebuild;
	}
	public void setForceRebuild(boolean newVal) {
		 forceRebuild = newVal;
	}
		public void descendentsCreated () {
		if (!getForceRebuild())
	  uiGenerator.deepProcessAttributes(this, false);	  /*
	  setDirection();
	  processDirection();
	  */	  
  }
	public void descendentCreated (int index) {
		  descendentsCreated();
	}  public boolean processDescendentsUIComponents() {
	  if (getWidgetAdapter() == null)
		  return false;
	  
	  getWidgetAdapter().descendentUIComponentsAdded();
	  return true;
  }  public abstract boolean addChildUIComponents();
  public abstract boolean childUIComponentsAdded();
  public  boolean addChildUIComponents(int from, int to) {
	  return addChildUIComponents();
  }
  public  boolean addChildUIComponents(Hashtable ignorePs) {
  	return addChildUIComponents(ignorePs);
  }
  /*
  public boolean getComputedStretchColumns() {
	  
		if (inRowWithPrimitiveAndComposites())
			return false;
		else
			return (Boolean) getTempAttributeValue(AttributeNames.STRETCH_COLUMNS);
		//return getStretchColumns();
}
*/
 
  boolean topRowWithPrimitivesAndComposites;
  public boolean getTopRowWithPrimitivesAndComposites() {
	    setInRowWithPrimitiveAndComposites();
		return topRowWithPrimitivesAndComposites;
	}
  public CompositeAdapter getTopDynamicAdapter() {
		CompositeAdapter retVal = null;
		CompositeAdapter candidate = this;
		while (candidate != null) {
			if (candidate.isDynamic())
				retVal = candidate;
			candidate = candidate.getParentAdapter();
		}
		return retVal;
	}
  boolean isTopDynamic() {
	  return this == getTopDynamicAdapter();
  }
  boolean inRowWithPrimitiveAndCompositesSet = false;
  boolean inRowWithPrimitiveAndComposites = false;
  public void setInRowWithPrimitiveAndComposites() {
	  if (inRowWithPrimitiveAndCompositesSet)
		  return;
	  CompositeAdapter parentAdapter = getParentAdapter();
	  if (parentAdapter == null) {
		  inRowWithPrimitiveAndComposites = false;
		  return;
	  }
	  
	  else
		  if ( (parentAdapter instanceof VectorAdapter || parentAdapter instanceof HashtableAdapter) &&			  
				    (parentAdapter.getDirection() == AttributeNames.VERTICAL) && 
				    !(this.getAdapterType() == PROPERTY_TYPE) && // dynamic child of vector or hashtable
					//(parentAdapter.getDirection() == AttributeNames.HORIZONTAL) && // got a row adapter				
				    (this.getHasPrimitiveAndCompositeChildren())  
					 ) {
				  inRowWithPrimitiveAndComposites = true;
				  topRowWithPrimitivesAndComposites = true;
				  /*
				  if (getSeparateUnboundTitles())
				  setTempAttributeValue(AttributeNames.IS_FLAT_TABLE_ROW, true);
				  */
		  } else  {
		  inRowWithPrimitiveAndComposites = parentAdapter.inRowWithPrimitiveAndComposites();
		  /*
		  if (parentAdapter.isFlatTableComponent() || parentAdapter.isFlatTableRow())
			  setTempAttributeValue(AttributeNames.IS_FLAT_TABLE_COMPONENT, true);
			  */
		  }
	  inRowWithPrimitiveAndCompositesSet = true;
  }
  public boolean flattenTable () {
		//return getSeparateUnboundTitles() && inRowWithPrimitiveAndComposites();
	  return getSeparateUnboundTitles() && getHeight() > 2;
	}
  public boolean inRowWithPrimitiveAndComposites() {
	  setInRowWithPrimitiveAndComposites();
	  return inRowWithPrimitiveAndComposites;
	  /*
	  uiContainerAdapter parentAdapter = getParentAdapter();
	  if (parentAdapter == null)
		  return false;
	  uiContainerAdapter grandParentAdapter = parentAdapter.getParentAdapter();
	  if (grandParentAdapter == null)
		  return false;
	  if ( (grandParentAdapter instanceof uiVectorAdapter || grandParentAdapter instanceof uiHashtableAdapter) &&			  
		    (grandParentAdapter.getDirection() == AttributeNames.VERTICAL) && 
		    !(parentAdapter.getAdapterType() == PROPERTY_TYPE) && // dynamic child of vector or hashtable
			(parentAdapter.getDirection() == AttributeNames.HORIZONTAL) && // got a row adapter				
		    (parentAdapter.getHasPrimitiveAndCompositeChildren())  
			 )
		  return true;
	  else
		  return parentAdapter.inRowWithPrimitiveAndComposites();
		  */
  }
  
	  public String getDefaultDirection() {	  /*
	  if (isHomogeneous() )
		  return AttributeNames.VERTICAL;
	  else if (!isTopAdapter()  && parent.isHomogeneous())
			return "horizontal";
	  else if (this.getChildAdapterCount() < 4)
		  return AttributeNames.VERTICAL;	  else return "square";
	  */
	  	
	  //if has an ancestor that is vertical and vector or hashtable, and this ancestor
	  // is  not homogeneous, then it should be horizontal also
	  
	  if (isFlatTableCell())
		  return AttributeNames.VERTICAL;
	  if (inRowWithPrimitiveAndComposites())
		  return AttributeNames.HORIZONTAL;
	  else if (getParentAdapter() != null && (getParentAdapter() instanceof VectorAdapter 
			  				|| getParentAdapter() instanceof HashtableAdapter)) 
			  return 	AttributeNames.HORIZONTAL;				  
	  else if (hasHomogeneousParent() && getMinimumHeight() == 1)
			return AttributeNames.HORIZONTAL;		else if (/*isHomogeneous() ||*/ this.getChildAdapterCount() >= 4 || getHeight() > 1)
			return AttributeNames.VERTICAL;		else 			return AttributeNames.SQUARE;	  
	}  public boolean hasHomogeneousParent() {	  return !isTopDisplayedAdapter()  && parent.isHomogeneous();
    }
  public boolean childrenCreated(boolean flag) {
	  return childrenCreated  = flag;
  }
   public abstract void createChildrenBasic(); 
//retarget
 
  public abstract void createChildrenBasic(Hashtable sharedProps);
  //end retarget  public  void createChildren() {
	  	if (this.childrenCreated || isRecursive())
		//if (this.childrenCreated || isRecursive())
			return;
	  	/*
	  	if (!getDefaultExpanded() &&( getGenericWidget() == null|| isElided()))
	  		return;
	  		*/
		createChildrenBasic();  }
   public  void createChildrenPropagating() {
	  /*
	  if (uiGenerator.hasObjectBeenVisited(this.getObject())) {
		  childrenCreated = true;
		  setIsRecursive(true);
		  if (getGenericWidget() != null) {
			  getGenericWidget().elide();
		  }
		  return;
	  } else
		uiGenerator.addVisitedObject(this.getObject());
		*/
	  
	  if (this.isRecursive())
		  childrenCreated = true;	
	    
		if (this.childrenCreated)
			return;
		createChildrenBasic();
		if (!isTopAdapter())		   parent.descendentCreated(this.getIndex());		else
			uiGenerator.deepProcessAttributes(this, false);		//recursivelyCheckIfNoVisibleChildren(this);
	}  
  //public abstract void createChildrenBasic(); 
  /*  public boolean isHomogeneous() {	  return false;
  }
  */
  
 
  @Override
  public void setNonGraphicsLeafAdapters(Vector<ObjectAdapter>theLeafComponents) {
	  
	  
	  leafComponentsSet = true;
	  leafComponents.clear();
	  
	  if (hasOnlyGraphicsDescendents()) {
		  return;
	  }
	  if (isElided()) {
	  //if (isWidgetShellExplicitlyElided()) {
		  super.setNonGraphicsLeafAdapters(theLeafComponents);
		  return;
	  }
	  if (isFlatTableCell()) {
		  super.setNonGraphicsLeafAdapters(theLeafComponents);
		  return;
	  }
	  int childAdapterCount = getChildAdapterCount();
	  for (int i = 0; i < childAdapterCount; i++) {
		  //getChildAdapterAt(i).setNonGraphicsLeafAdapters(theLeafComponents);
		  getChildAdapterAt(i).setNonGraphicsLeafAdapters(leafComponents);
		  
	  }
	  if (leafComponents != theLeafComponents) {
	  for (int i = 0; i < leafComponents.size(); i++)
		  theLeafComponents.add(leafComponents.get(i));
	  }
  }
  @Override
  public void setNonGraphicsChildAdapters(Vector<ObjectAdapter>childComponents) {
	  if (childComponentsSet)
		  return;
	  childComponentsSet = true;
	  childComponents.clear();
	  
	  if (hasOnlyGraphicsDescendents()) {
		  return;
	  }
	  if (isElided() || isFlatTableCell()) {
		   super.setNonGraphicsChildAdapters(childComponents);
		   return;
	  }
	  for (int i = 0; i < getChildAdapterCount(); i++) {
		  ObjectAdapter childAdapter = getChildAdapterAt(i);
		  if (childAdapter.hasOnlyGraphicsDescendents())
			  continue;
		  childComponents.add(childAdapter);
	  }
  }
  
  int numOfNonShapeDescendents = 1;
  boolean numOfNonShapeDescendentsSet = false;
  @Override
  public int getNumberOfNonShapeLeaves() {
	  setNumberOfNonShapeDescendents();
	  return numOfNonShapeDescendents;
  }
  public void setNumberOfNonShapeDescendents() {
	  if (numOfNonShapeDescendentsSet)
	  return;
	  numOfNonShapeDescendentsSet = true;
  numOfNonShapeDescendents = 0;
  if (hasOnlyGraphicsDescendents()) {
	  numOfNonShapeDescendents = 0;
	  return;
  }
  if (isElided()) {
	  numOfNonShapeDescendents = super.getNumberOfNonShapeLeaves();
	  return;
  }
  if (isFlatTableCell()) {
	  numOfNonShapeDescendents = super.getNumberOfNonShapeLeaves();
	  return;
  }
  for (Enumeration children = getChildren(); children.hasMoreElements();) {
	  numOfNonShapeDescendents += ((ObjectAdapter) children.nextElement()).getNumberOfNonShapeLeaves();
	     		 
	  }
  
}
	  
  
  int heightOfNonShapeDescendents = 0;
  boolean heightOfNonShapeDescendentsSet = false;
  @Override
  public int getHeightOfNonShapeDescendents() {
	  setHeightOfNonShapeDescendents();
	  return heightOfNonShapeDescendents;
	  /*
	  int maxChildLevel = 0;
	  if (hasOnlyGraphicsDescendents())
		  return maxChildLevel;
	  for (Enumeration children = getChildren(); children.hasMoreElements();) {
		  maxChildLevel = Math.max(maxChildLevel, ((uiObjectAdapter) children.nextElement()).getHeightOfNonShapeDescendents());
		     
			 
		  }
	  return maxChildLevel + 1;
	  */
  }
  public void setHeightOfNonShapeDescendents() {
	  if (heightOfNonShapeDescendentsSet)
		  return;
	  heightOfNonShapeDescendentsSet = true;
	  int maxChildLevel = 0;
	  if (hasOnlyGraphicsDescendents()) {
		  heightOfNonShapeDescendents = maxChildLevel;
		  return;
	  }
	  if (isFlatTableCell()) {
		  heightOfNonShapeDescendents = maxChildLevel;
		  return;
	  }
	  for (Enumeration children = getChildren(); children.hasMoreElements();) {
		  maxChildLevel = Math.max(maxChildLevel, ((ObjectAdapter) children.nextElement()).getHeightOfNonShapeDescendents());
		     		 
		  }
	  //if (!hasFlatTableRowDescendent())
	  heightOfNonShapeDescendents = maxChildLevel + 1;
	  /*
	  else
		  heightOfNonShapeDescendents = maxChildLevel;
		  */
  }
  boolean heightSet = false;
  int height;
  public void setHeight() {
	  if (heightSet)
		  return;
	  heightSet = true;
	  int maxChildLevel = 0;
	  for (Enumeration children = getChildren(); children.hasMoreElements();) {
	     maxChildLevel = Math.max(maxChildLevel, ((ObjectAdapter) children.nextElement()).getHeight());
		 
	  }
	  	  height =  maxChildLevel + 1;
	  
  }
  boolean rowHeightSet = false;
  int rowHeight;
  public void setRowHeight() {
	  if (rowHeightSet)
		  return;
	 
	  rowHeightSet = true;
	  /*
	  if (!isFlatTableRow())
		  rowHeight = 0;
		  */
	  int maxChildLevel = 0;
	  for (Enumeration children = getChildren(); children.hasMoreElements();) {
		  ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  if (!child.isFlatTableRow())
			  continue;
	     maxChildLevel = Math.max(maxChildLevel, child.getRowHeight());
		 
	  }
	  	  rowHeight =  maxChildLevel + 1;
	  
  }
  @Override
  public int getRowHeight() {
	  setRowHeight();
	  return rowHeight;
  }
  @Override  public int getHeight() {
	  setHeight();
	  return height;
	  /*	  int maxChildLevel = 0;
	  for (Enumeration children = getChildren(); children.hasMoreElements();) {
	     maxChildLevel = Math.max(maxChildLevel, ((uiObjectAdapter) children.nextElement()).getHeight());		 
	  }
	  	  return maxChildLevel + 1;
	  	  */
  }
  public int getMinimumHeight() {
	  if (isAtomic())
		  return 0;	  int minChildHeight = Integer.MAX_VALUE;	  Enumeration children = getChildren();	  if (!children.hasMoreElements()) return 0;
	  while (children.hasMoreElements()) {
	     minChildHeight = Math.min(minChildHeight, ((ObjectAdapter) children.nextElement()).getMinimumHeight());		 
	  }
	  	  return minChildHeight + 1;
  }  public void expandPrimitiveChildren() {
	  for (Enumeration children = getChildren(); children.hasMoreElements();) {
	     ObjectAdapter child = (ObjectAdapter) children.nextElement();
		 if (child instanceof PrimitiveAdapter && child.getGenericWidget() != null && getExpandPrimitiveChildren())
			 child.getGenericWidget().internalExpand();		 
	  }
  }    
  
  /*  public void childEditingStatusChanged() {
	  Vector result = (new IsEditedAdapterVisitor(this)).traverse();	  if (getGenericWidget() == null) return;
	  if (result.contains(new Boolean(true))) 
			  getGenericWidget().setEdited(true);
	  else
			  getGenericWidget().setEdited(false);	  if (parent != null) 		  ((uiContainerAdapter) parent).childEditingStatusChanged();	  else		  this.getUIFrame().setEdited();
		    }  */  public void childEditingStatusChanged() {
	  Vector result = (new IsEditedAdapterVisitor(this)).traverse();	  
	  boolean edited = result.contains(new Boolean(true));	  childEditingStatusChanged(edited);	  
		    }
  public void childEditingStatusChanged(boolean edited) {
	  WidgetShell widget = getGenericWidget();
	  if (widget != null)
		  widget.setEdited(edited);	  	  if (parent != null) 		  ((CompositeAdapter) parent).childEditingStatusChanged(edited);	  else		  this.getUIFrame().setEdited(edited);
		    }  

  //transient uiWidgetAdapter widgetAdapter;
  // not sure why it was here
  
  
  // Selection interface
  public void select() {	  /*
    if (getGenericWidget().elided)
      getGenericWidget().selectElideComponent();
    else */	   if (getWidgetAdapter() != null)
      getWidgetAdapter().setUIComponentSelected();
	  //getWidgetAdapter().getGenericWidget().expand();	  
  }

  public void unselect() {
	  
	 	  	  /*
    if (getGenericWidget().elided)
      getGenericWidget().unselectElideComponent();
    else */	   if (getWidgetAdapter() != null)
      getWidgetAdapter().setUIComponentDeselected();
  }

  public String getChildSelectableIndex(Selectable child) {
    return getChildAdapterRealIndex((ObjectAdapter) child);
  }
  
  public Selectable getChildSelectable(String index) {
    return getChildAdapterMapping(index);
  }  
public abstract Enumeration getChildren (); 
public abstract int getNumberOfChildren();
public abstract int getNumberOfStaticChildren();
public abstract int getNumberOfDynamicChildren();
public void padChildrenLabels (int maxLength) {
	  for (Enumeration elements = getChildren(); elements.hasMoreElements(); )
		  ((ObjectAdapter) elements.nextElement()).padLabelTo(maxLength);  }
public void makeColumnTitles () {
	/*
	//System.out.println("making column titles");	  Enumeration enum = getChildren();
	  if (!enum.hasMoreElements()) return;	 // System.out.println("returning from column titles");
	  Object firstChild = enum.nextElement();
	  if (!(firstChild instanceof uiContainerAdapter)) return;	  //((uiContainerAdapter) enum.nextElement()).makeHorizontalColumnTitles();		  ((uiContainerAdapter) firstChild).makeHorizontalColumnTitles();	
	  while (enum.hasMoreElements()) {		   Object container =  enum.nextElement();		   if (container instanceof uiContainerAdapter)
	         //((uiContainerAdapter) enum.nextElement()).setHorizontalLabelVisible(false);			   ((uiContainerAdapter) container).setHorizontalLabelVisible(false);		   
	  }
	  */	 
	   }/*	public void makeHorizontalDescendentColumnTitles() {		System.out.println("Make Colimn Totles called" + this + this.isHorizontal());
		//if (getParentAdapter().isHorizontal()) {
			System.out.println(this + " is horizontal");			  //getGenericWidget().alignLabel(GridBagConstraints.NORTH);			  			  for (Enumeration children = getChildren();
				   children.hasMoreElements();)
				  ((uiObjectAdapter) children.nextElement()).makeHorizontalColumnTitles();
		//}	   
	}
*/	/*		
	public void setHorizontalDescendentLabelVisible(boolean visible) {		//if (getParentAdapter().isHorizontal()) {			  getGenericWidget().setLabelVisible(false);
		if (isHorizontal()) 			  for (Enumeration children = getChildren();
				   children.hasMoreElements();)
				  ((uiObjectAdapter) children.nextElement()).setHorizontalLabelVisible(visible);
			  		
	}
*/
    	public void makeHorizontalColumnTitles() {		//System.out.println("Make Colimn Totles called" + this + this.isHorizontal());
		//if (getParentAdapter().isHorizontal()) {
			//System.out.println(this + " is horizontal");			  //getGenericWidget().alignLabel(GridBagConstraints.NORTH);		//uiObjectAdapter adapter;
		/*
		if (hasOnlyGraphicsDescendents())
			return;
			*/
		setShowChildrenColumnTitle(true);
		if (isHorizontal())		  for (Enumeration children = getChildren(); children.hasMoreElements();) {
			  ObjectAdapter adapter;
		  	  //((uiObjectAdapter) children.nextElement()).makeHorizontalColumnTitles();		  	  adapter = ((ObjectAdapter) children.nextElement());
			  //adapter.getGenericWidget().alignLabel(GridBagConstraints.NORTH);
		  	  adapter.setColumnTitleStatus(ColumnTitleStatus.show);
		  	  /*
			  if (adapter.getGenericWidget() != null) {			  adapter.getGenericWidget().showColumnTitle(columnTitle(adapter));
			  }
			  */
			  //adapter.propagateAttributesToWidgetShell();
			  if (adapter instanceof CompositeAdapter && adapter.getDirection() == AttributeNames.HORIZONTAL )			   ((CompositeAdapter) adapter).makeHorizontalColumnTitles();
			  if (!adapter.isFlatTableComponent())
			  adapter.propagateAttributesToWidgetShell();
			  //adapter.propagateAttributesToParentWidgetAdapter();
		  }			  
		//}	   
	}
				
	public void setHorizontalLabelVisible(boolean visible) {
		//System.out.println("setting horizontal" + visible);		//if (getParentAdapter().isHorizontal()) {			  //getGenericWidget().setLabelVisible(false);
		setShowChildrenColumnTitle(false);
		if (isHorizontal()) //{
			//System.out.println("IS HORIZONTAAL");			for (Enumeration children = getChildren(); children.hasMoreElements();) {

				ObjectAdapter adapter;
				  //((uiObjectAdapter) children.nextElement()).setHorizontalLabelVisible(visible);
			   adapter = ((ObjectAdapter) children.nextElement());			   //System.out.println("set horiz");
			   //adapter.getGenericWidget().setLabelVisible(false);
			   adapter.setColumnTitleStatus(ColumnTitleStatus.hide);
			   /*
			   if (adapter instanceof uiContainerAdapter && adapter.getDirection() == AttributeNames.HORIZONTAL )
				   ((uiContainerAdapter) adapter).setHorizontalLabelVisible(false);
				   */
			   /*
			   if (adapter.getGenericWidget() != null) {				   adapter.getGenericWidget().hideColumnTitle();
				   
			   }
			   */
			   
			   //adapter.propagateAttributesToWidgetShell();
			   if (adapter instanceof CompositeAdapter  )			     ((CompositeAdapter) adapter).setHorizontalLabelVisible(false);
			   if (!adapter.isFlatTableComponent())
			   adapter.propagateAttributesToWidgetShell();
		  			}
			  		//}
		 	}	public void nameChildChanged(String newVal) {		//String curLabel = this.getGenericWidget().getLabel();		//System.out.println("name child changed of " + this);		
		WidgetShell gw = this.getGenericWidget();		if (((isTopAdapter() || getParentAdapter() instanceof VectorAdapter)) ) {
			//&&	((!(this instanceof uiVectorAdapter)) && (!(this instanceof uiHashtableAdapter)))) {
		   if (getSourceAdapter() != null)
			   newVal = getSourceAdapter().getBeautifiedPath();
		   if (gw != null)			gw.setLabel(newVal);
		   this.setSynthesizedLabel(newVal);
		   		}
		//gw.setElideString("<" + newVal + "..." + ">", this);	}
	// contains all children
  transient Vector<ObjectAdapter> visibleAndNonVisibleChildrenVector = new Vector();
  // contains sttaic and non static visible children
  transient Vector<ObjectAdapter> childrenVector = new Vector();   
  public void resetChildrenVector() {
	  if (childrenVector == null)	  childrenVector = new Vector();
	  else
		  childrenVector.clear();
  }
  public void resetVisibleAndInvisibleChildrenVector() {
	  if (visibleAndNonVisibleChildrenVector == null)
		  visibleAndNonVisibleChildrenVector = new Vector();
	  else
		  visibleAndNonVisibleChildrenVector.clear();
  }
  
  public void setChildrenSortMode(Vector<ObjectAdapter> children, boolean newVal) {
		for (int i = 0; i < children.size(); i++) {
			ObjectAdapter child = children.elementAt(i);
			if (child instanceof ClassAdapter)
				((ClassAdapter) child).setSortMode(newVal);
		}
		
	}
  public void resetChildrenVector(int from) {
	  //System.out.println("from" + from + "size" + childrenVector.size());	  //System.out.println(childrenVector);
	  if (from == 0) {
		  resetChildrenVector();
		  return;
	  }
	  
	  //while (childrenVector().size() > from)
	  while (childrenVector.size() > from)
		  childrenVector.removeElementAt(childrenVector.size() - 1);	  
  }
	public abstract Vector getVisibleAndInvisibleChildAdapters();

  public void resetVisibleAndInvisibleChildrenVector(int from) {
	  //System.out.println("from" + from + "size" + childrenVector.size());
	  //System.out.println(childrenVector);
	  if (from == 0) {
		  resetVisibleAndInvisibleChildrenVector();
		  return;
	  }
	  
	  //while (childrenVector().size() > from)
	  while (visibleAndNonVisibleChildrenVector.size() > from)
		  visibleAndNonVisibleChildrenVector.removeElementAt(visibleAndNonVisibleChildrenVector.size() - 1);
	  
  }
  public abstract void addComponents();  public void setChildAdapterMapping(ObjectAdapter adaptor) {	  //System.out.println("adding " + adaptor + "at " + childrenVector.size());
      childrenVector.addElement(adaptor);
	   //System.out.println("size" + childrenVector.size());			 //System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");  }
  public void setChildAdapterMapping(int index, ObjectAdapter adaptor) {
	  //System.out.println("adding " + adaptor + "at " + childrenVector.size());
      childrenVector.insertElementAt(adaptor, index);
	   //System.out.println("size" + childrenVector.size());
			 //System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
  }
  /*
  public void setChildAdapterMapping(int pos, uiObjectAdapter adaptor) {
	  //System.out.println("adding " + adaptor + "at " + childrenVector.size());
      childrenVector.addElement(adaptor);
	   //System.out.println("size" + childrenVector.size());
			 //System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
  }
  */
  public void unSetChildAdapterMapping(ObjectAdapter adaptor) {
	  //System.out.println("adding " + adaptor + "at " + childrenVector.size());
      childrenVector.removeElement(adaptor);
	   //System.out.println("size" + childrenVector.size());
			 //System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
  }
  public void resetChildAdapterMapping(ObjectAdapter oldAdapter, ObjectAdapter newAdapter) {
	  //System.out.println("adding " + adaptor + "at " + childrenVector.size());
	  int index = childrenVector.indexOf(oldAdapter);
	  if (index == -1)
		  return;
	  System.out.println("resetChildAdapterMapping taking action after all");
      childrenVector.setElementAt(newAdapter, index);
	   //System.out.println("size" + childrenVector.size());
			 //System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
  }  public void setChildAdapterMapping(ObjectAdapter adaptor, int pos, ObjectAdapter oldAdapter) {	  //System.out.println("replacing " + adaptor + "at " + pos);	  //System.out.println("size" + childrenVector.size());	  childrenVector.removeElement(oldAdapter);
	  if (pos >= 0 && pos <= childrenVector.size())
      childrenVector.insertElementAt(adaptor, pos);
	  else
		  Tracer.error("illegal vector pos:" + pos);
	   //System.out.println("size" + childrenVector.size());			 //System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");  }  public void insertChildAdapterAt(ObjectAdapter adaptor, int pos) {	  
      //childrenVector.insertElementAt(adaptor, pos);
	  visibleAndNonVisibleChildrenVector.insertElementAt(adaptor, pos);
	     }  public void removeChildAdapterAt(int pos) {	  //System.out.println("replacing " + adaptor + "at " + pos);	  //System.out.println("size" + childrenVector.size());
      childrenVector.removeElementAt(pos);
	   //System.out.println("size" + childrenVector.size());			 //System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");  }
	transient boolean skippedAdapter = false;	transient ObjectAdapter onlyChild = null;	transient CompositeAdapter onlyCompositeChild = null;
	void setSkippedAdapter() {	   skippedAdapter = true;
//	   onlyChild = (ObjectAdapter) childrenVector.elementAt(0);
	   if (onlyChild instanceof CompositeAdapter)	     onlyCompositeChild = (CompositeAdapter)  childrenVector.elementAt(0);
	}
	
	public boolean isSkippedAdapter() {		return skippedAdapter;
	}
	public ObjectAdapter getOnlyChild() {
		return onlyChild;
	}
	/*
	public boolean getShowBorder() {		
		if (isSkippedAdapter()) 
			return false;
		else
			return super.getShowBorder();
		
	}
	*/	public boolean getAllowsChildren() {		
		//System.out.println("get allows children");		return true;
	}
		public Enumeration children() {		//System.out.println("children called");
		if (!childrenCreated)
			createChildrenPropagating();		if (onlyCompositeChild != null) {
			return onlyCompositeChild.children();		}
		return getChildren();	}
	
		public boolean isLeaf() {		return isLeafAdapter();
		/*		//System.out.println("is leaf");		if (onlyChild != null)			return onlyChild.isLeaf();
		//return isAtomic();
		return false;
		*/	}	public boolean isLeafAdapter() {		//System.out.println("is leaf");		if (onlyChild != null)			return onlyChild.isLeafAdapter();
		//return isAtomic();
		return false;	}
	/*	public TreeNode getParent() {
				System.out.println("get parent:" + this + "onlyCompChild" + onlyCompositeChild);		uiContainerAdapter parent = (uiContainerAdapter) getParentAdapter();
		if (parent == null) return null;
		uiContainerAdapter grandParent = parent.getParentAdapter();
		if (grandParent == null ||grandParent.onlyCompositeChild == null )
			return parent;
		return grandParent;
				//if (parent.isSkippedAdapter())		
		//if (parent.onlyCompositeChild != null)		//	return parent.getParent();
				//if (onlyCompositeChild != null)			//return parent.getParent();
		//return parent;	}*/
	public int getCurrentChildCount() {		return getChildrenVector().size();
	}		public int getChildCount() {		return getChildAdapterCount();
		/*		//System.out.println("get child count");
		if (!childrenCreated)
			createChildrenPropagating();
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildCount();		//System.out.println("returning "+ childrenVector.size());
		return childrenVector().size();
		*/	}
	
	public int getChildAdapterCount() {		//System.out.println("get child count");
		if (!childrenCreated)
			createChildrenPropagating();
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterCount();		//System.out.println("returning "+ childrenVector.size());
		return getChildrenVector().size();	}
	
	public int getChildAdapterCountWithoutSideEffects() {
		//System.out.println("get child count");
		
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterCount();
		//System.out.println("returning "+ childrenVector.size());
		return getChildrenVector().size();
	}
	public int getVisbileAndInvisibleChildAdapterCount() {
		if (!childrenCreated)
			createChildrenPropagating();
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterCount();
		//System.out.println("returning "+ childrenVector.size());
		return visibleAndNonVisibleChildrenVector.size();
		
	}
	public int getChildAdapterCountBasic() {
		//System.out.println("get child count");
		if (!childrenCreated)
			createChildrenBasic();
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterCountBasic();
		//System.out.println("returning "+ childrenVector.size());
		return getChildrenVector().size();
	}		public Vector<ObjectAdapter> getChildrenVector() {
		return childrenVector;	}
	
	
	public ObjectAdapter getChildAdapterAt(int childIndex) {		
		//System.out.println("get child  for" + childIndex);		if (!childrenCreated)
			createChildrenPropagating();		
		if (onlyCompositeChild != null)
			return onlyCompositeChild.getChildAdapterAt(childIndex);		return (ObjectAdapter) getChildrenVector().elementAt(childIndex);
	}	public TreeNode getChildAt(int childIndex) {		
		//System.out.println("get child  for" + childIndex);
		TreeNode retVal = (TreeNode) getChildAdapterAt(childIndex);
		return retVal;//		return (TreeNode) getChildAdapterAt(childIndex);
	}
		public int getIndex(TreeNode node) {		return getAdapterIndex(node);
		/*		//System.out.println("get Index of" + node);		if (onlyCompositeChild != null)			return onlyCompositeChild.getIndex(node);
		return childrenVector().indexOf(node);
		*/	}
	public int getAdapterIndex(TreeNode node) {		//System.out.println("get Index of" + node);		if (onlyCompositeChild != null)			return onlyCompositeChild.getAdapterIndex(node);
		return getChildrenVector().indexOf(node);	}
		public static void removeFromFrame(Container c, uiFrame f) {
     //if (c == f) return;
	 if (c == null) return;
	 Container parent = c.getParent();
	 if (parent == null) return;
	 parent.remove(c);	 
	 System.out.println("removed c" + c );	 //if (parent == f) {	 if (parent == f.getFrame()) {
		 f.emptyMainPanel();		 f.validate();	 } else
	    removeFromFrame(parent, f);  }
  public static boolean checkIfNoVisibleChildren(CompositeAdapter adapter) {
	  if (adapter.isAtomic()) return false;
	  if (adapter instanceof HashtableAdapter || adapter instanceof VectorAdapter)
		  return false;	  if (adapter.getChildAdapterCount() == 0 ) return false;
	  //if (adapter.getChildCount() == 0 && !adapter.childrenCreated) return false;
	  //if (adapter.getChildCount() == 0 && !(adapter instanceof uiVectorAdapter)) return true;	  VirtualComponent component =  adapter.getUIComponent();	  if (component == null) return false;
	  //System.out.println("component" + component);	  if (!(component instanceof Container)) return false;
	  Container container = (Container) component;
	  if (container.getComponentCount() != 0) return false;	
	  //System.out.println("no component");
	  //Container genWidget =  adapter.getGenericWidget();
	  VirtualContainer genWidget =  adapter.getGenericWidget().getContainer();	  if (genWidget == null) return true;
	  VirtualContainer parentContainer = genWidget.getParent();	  if (parentContainer == null) return true;
	//  if (!(parentContainer instanceof uiGenericWidget)) return false;
	  ObjectAdapter parent = adapter.getParentAdapter();
	  if (parent == null) return true;
	  WidgetAdapterInterface parentWidgetAdapter = parent.getWidgetAdapter();
	  if (parentWidgetAdapter == null) return true;
	  parentWidgetAdapter.remove(parentContainer, genWidget, adapter);	  //parentContainer.remove(genWidget);	  
	  //uiObjectAdapter parent = adapter.getParentAdapter();
	  
	  if ( parent != null )		  checkIfNoVisibleChildren((CompositeAdapter) parent);	  
	  if (adapter.isTopAdapter())
		  adapter.getUIFrame().emptyMainPanel();
	  	  
	  return true;  }  public static boolean recursivelyCheckIfNoVisibleChildren(CompositeAdapter adapter) {	  if (!checkIfNoVisibleChildren(adapter)) return false;	  
	  ObjectAdapter parent = adapter.getParentAdapter();
	  /*	  if (!(parentContainer instanceof uiGenericWidget))		  removeFromFrame(parentContainer, adapter.getUIFrame());	  	  else 	  */
	  //if ( parent != null && parent instanceof uiClassAdapter)
	  if ( parent != null )		  recursivelyCheckIfNoVisibleChildren((CompositeAdapter) parent);
	  	  
	  return true;  }  public ObjectAdapter replaceAdapter(ObjectAdapter child, 
				      Object newValue) {
    // Assume that this change is legal?
    int position = 0;
    position = childrenVector.indexOf(child);
    if (position < 0) {
    	System.out.println("Replace Adapter: child position = " + position);
    	return null;
    }
    // we dont need to look at components
    /*
    Component[] components = ((Container) getUIComponent()).getComponents();	//System.out.println("componnets length" + components.length);
	for (;position<components.length; position++) {		//System.out.println ("pos" + position + "comp" + components[position]);
      if (child.getGenericWidget().equals(components[position]))
	break;
    if (position == components.length)
      return null;
	}
		//System.out.println("components size before removal" + components.length);	//System.out.println("position" + position);
	 * 
	 */
    //((Container) getUIComponent()).remove(child.getGenericWidget());
	//child.removeUIComponentFromParent(((Container) getUIComponent()));
	child.removeUIComponentFromParent(this);	//components = ((Container) getUIComponent()).getComponents();		//System.out.println("components size after removal" + components.length);	//System.out.println ("this" + this + "value" + getRealObject());	//System.out.println ("child" + child + "value" + newValue);
    ObjectAdapter adapter = uiGenerator.createObjectAdapter(
    		//(Container) getUIComponent(), 
    		this, newValue,  ReflectUtil.toMaybeProxyTargetClass(newValue), position, child.getPropertyName(), getRealObject(), (child.getAdapterType() == PROPERTY_TYPE));	//components = ((Container) getUIComponent()).getComponents();	
	//System.out.println("components size after addition" + components.length);	//childrenVector.setElementAt(adapter, position);	this.setChildAdapterMapping(adapter, position, child);
    // Set up property change listener link if required!	return adapter;
  }  public boolean isHomogeneous() {
		return ClassDescriptorCache.toBoolean(getTempAttributeValue(HOMOGENEOUS));	    //return homogeneousVector;
	}
		public void setHomogeneous(boolean newVal) {
		this.setTempAttributeValue(HOMOGENEOUS, new Boolean(newVal));	}	public void setChildrenHorizontal (boolean newValue) {
		this.setTempAttributeValue(CHILDREN_HORIZONTAL, new Boolean(newValue));	}	public boolean childrenHorizontal() {
		return ClassDescriptorCache.toBoolean(
							getTempAttributeValue(CHILDREN_HORIZONTAL));	    //return homogeneousVector;
	}	public ObjectAdapter getExpandedAdapter(ObjectAdapter child) {
	  //return child;
	  return null;
  }
	public boolean isSLModelAdapter() {
		return false;
	}
	String inputText;
	transient RecordStructure recordStructure;
	public RecordStructure getRecordStructure() {
		return recordStructure;
	}
	public String toText() {
		if (!(concreteObject instanceof RecordStructure)) return super.toText();
		recordStructure = (RecordStructure) concreteObject;
		if (!recordStructure.hasUserObject()) return super.toText();
		Object userObject = recordStructure.getUserObject();
		 String label = super.toText();		 
		 //String valRep = userObject.toString();
		 String valRep;
		  if (userObject != null && (valRep = userObject.toString()) != null) {
				if (!isLabelled() || label.equals("") || label.equals(valRep))
				    return valRep;
				else
				    return label + ":" + valRep;
			
		  } else if (isLabelled())
			  return label;
		  else
			  return "";
		  
	  }
	
	  public void setUserObject(Object object)  {
		if (!(concreteObject instanceof RecordStructure))  super.setUserObject(object);
		recordStructure = (RecordStructure) concreteObject;
		if (!recordStructure.hasUserObject())  super.setUserObject(object);
		if (!recordStructure.hasEditableUserObject())  super.setUserObject(object);		
		//if (recordStructure.hasUserObject())
			recordStructure.setUserObject(PrimitiveAdapter.valuePart((String) object));		
		uiComponentValueChanged();
		  //System.out.println("New Input:" + inputText);
	  }
	  public void setCompleteUserObject(Object object)  {
	  	if (!(concreteObject instanceof RecordStructure))  super.setUserObject(object);
		recordStructure = (RecordStructure) concreteObject;
		if (!recordStructure.hasEditableUserObject())  super.setUserObject(object);
		recordStructure.setUserObject(object);		
		uiComponentValueChanged();
		}
	  // not a regular column
	  public void sortAncestor(ObjectAdapter topAdapter) {
			if (getKey() == null)
				return;		
			ObjectAdapter parentAdapter = getParentAdapter();
			if (!(parentAdapter instanceof HashtableAdapter))
				return;
			parentAdapter.setLocalAttribute(AttributeNames.HASHTABLE_SORT_KEYS, true);
			/*
			uiAttributeManager environment = AttributeManager.getEnvironment();
			  if (environment == null) 
				  return;
			  environment.removeFromAttributeLists(parentClass.getName());
			  */
			//ViewInfo cd = ClassDescriptorCache.getClassDescriptor(parentClass);
		}
	  public boolean explicitSort() {
		  return sort();
	  }
	public static void sort (CompositeAdapter topAdapter, ObjectAdapter leafAdapter) {
		
		if (leafAdapter == null)
			return;
		leafAdapter.sortAncestor(topAdapter);
		
		//topAdapter.implicitRefresh();
		// this is the stuff that works
		  //topAdapter.sort();
		  topAdapter.explicitSort();
		  //topAdapter.recomputeIndices();
		  topAdapter.sortChildWidgets();
		  /*
		  uiGenerator.deepProcessAttributes(topAdapter); 
		  uiGenerator.deepElide(topAdapter);
		  topAdapter.getUIFrame().validate();
		  */
		  
		  
		
	}
	// will override this also
	public boolean sort() {
		return false;
		
	}
	public void recomputeIndices() {
		
	}
	
	// will override this to provide better behavior
	public  void sortChildWidgets() {
		/*
		  uiGenerator.deepProcessAttributes(this); 
		  uiGenerator.deepElide(this);
		  getUIFrame().validate();
		  */
	}
	
	
	
	public boolean preClear() {
		return false;
	}
	public  void clear() {
	}
	
	public ObjectAdapter[] getColumnAdapters (CompositeAdapter parent, ObjectAdapter child) {
		return null;
		/*
		uiObjectAdapter[] retVal = new uiObjectAdapter[1];
		return retVal;
		*/
	}
	
	public void select (ObjectAdapter[] column) {
		
	}
	@Override
	void invalidateComponentsSet () {
		super.invalidateComponentsSet();
		numOfNonShapeDescendentsSet = false;
		childComponentsSet = false;
		leafComponentsSet = false;
		heightSet = false;
		rowHeightSet = false;
	}
	@Override
	public void invalidateComponentsSetInTree() {
		invalidateComponentsSet();
		for (int i = 0; i < getChildAdapterCount(); i++ ) {
			getChildAdapterAt(i).invalidateComponentsSetInTree();
		}
	}
	public ObjectAdapter getTitleChild() {
		return null;
	}
	
	public boolean getChildTextMode() {
		return childTextMode;
	}
	
	public void setChildTextMode(boolean newVal) {
		childTextMode = newVal;
	}
	public void setViewObject(Object obj, boolean textMode) {
		super.setViewObject(obj);
		setChildTextMode(textMode);
	}
	
	public  abstract boolean redoVisibleChildren();
	// this stuff really belongs in ACompositeAdapter
	public void processViewObject(Object viewObject) {
		if (isNestedShapesContainer() && viewObject instanceof ShapeListMouseClickListener)
			shapeListMouseClickListener = (ShapeListMouseClickListener) viewObject;
	}
	ShapeListMouseClickListener shapeListMouseClickListener;
	public ShapeListMouseClickListener getShapeListMouseClickListener() {
		return shapeListMouseClickListener;
	}
	public void mouseClicked(List<RemoteShape> theShapes, VirtualMouseEvent mouseEvent) {
		if (shapeListMouseClickListener == null)
			return;		
		shapeListMouseClickListener.mouseClicked(theShapes, mouseEvent);
		// need to have a thread that refreshes at the end
		this.refresh();
	}
//	public void mouseClicked(List<RemoteShape> theShapes, VirtualMouseEvent mouseEvent) {
//		if (shapeListMouseClickListener == null)
//			return;		
//		shapeListMouseClickListener.mouseClicked(theShapes, new VirtualMouseEvent(mouseEvent, 1));
//		// need to have a thread that refreshes at the end
//		this.refresh();
//	}
	public void registerAsMouseClickListener(ShapeEventNotifier shapeEventNotifier) {
		  if (shapeListMouseClickListener == null) return;
		  shapeEventNotifier.addMouseListener(this);
	  }
	
	  
}



