// We can derive primitive adaptors by extending
// This class
package bus.uigen.oadapters;import bus.uigen.translator.*;
import bus.uigen.view.AGenericWidgetShell;
import bus.uigen.widgets.VirtualTextArea;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import util.models.RemotePropertyChangeListener;
import bus.uigen.*;import bus.uigen.ars.*;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.Selectable;import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.sadapters.ConcreteType;import bus.uigen.sadapters.ConcretePrimitive;
public class PrimitiveAdapter extends ObjectAdapter 
implements PrimitiveAdapterInterface {
//ObjectValueChangedListener, RemotePropertyChangeListener {
	public PrimitiveAdapter() throws RemoteException {
		
	}
  // Just for a demo purpose
  transient public myLockManager lockManager;
  
  transient Vector linkMethods = new Vector();
  boolean isViewAtomic() {
	  return true;  }
 public ConcretePrimitive getConcretePrimitive() {		return (ConcretePrimitive) getConcreteObject();
	}
 
  public void setLockManager(myLockManager lman) {
    lockManager = lman;
  }  static final int MAX_VAL_LENGTH = 30;
  static public String prefix(String s) {	  int newLineIndex = s.indexOf('\n');
	  int index = Math.min(MAX_VAL_LENGTH, s.length());	  if (newLineIndex < 0)		  return s.substring(0, index);	  else		  return s.substring(0, Math.min(newLineIndex, index));
  }  public String toString() {
	  return toText();	  //return prefix(toText());
	  //return prefix(toString());  }  public String toText() {	  String label = super.toText();
	  if (isKeyAdapter() || hasCellEditor()) return label;
	  /*	  Object val;
	  if (getWidgetAdapter() == null) 
		  try {	     val = TranslatorRegistry.convert("java.lang.Object", getViewObject());		  } catch (Exception e) { val = null;}
	  else		 val = getValue();
	  if (val != null) {		    String valRep = val.toString();			//System.out.println(valRep.indexOf('\n'));
			if (label.equals(""))
			    return valRep;
			else
			    return label + ":" + valRep;
		
	  } else
		  return label;
	  */	  //String valRep = toStringValue();
	  String valRep = null;
	  if (getRealObject() != null)
		 valRep = getRealObject().toString();	  if (valRep != null) {
			if (label.equals("") || !isLabelled())
			    return valRep;
			else
			    return label + ":" + valRep;
		
	  } else
		  return label;
	    }
  public String browseLabel() {
	
		return toStringValue();		
}
  public String toCell() {		return toStringValue();
	}  public String toStringValue() {
	  Object val;	  String valRep = null;
	  if (getWidgetAdapter() == null) 
		  try {	     val = TranslatorRegistry.convert("java.lang.Object", computeAndMaybeSetViewObject());		  } catch (Exception e) { val = null;}
	  else		 val = getValue();
	  if (val != null) {		     valRep = val.toString();
	  }
	  return valRep;  }
  public static String valuePart(String text) {
	  int labelEnd = text.lastIndexOf(':');	  String valRep;
	  if (labelEnd < 0)
		  valRep = text;
	  else
		  valRep = text.substring(labelEnd + 1);	  return valRep;
	      }    public Object fromText(String text) {	  /*
	  int labelEnd = text.indexOf(':');	  String valRep;
	  if (labelEnd < 0)
		  valRep = text;
	  else
		  valRep = text.substring(labelEnd + 1);	  */
	    return Instantiator.newPrimitiveInstance(getPropertyClass(),
						 valuePart(text));  }
  public Object fromCompleteText(String text) {
	  /*
	  int labelEnd = text.indexOf(':');
	  String valRep;
	  if (labelEnd < 0)
		  valRep = text;
	  else
		  valRep = text.substring(labelEnd + 1);
	  */
	    return Instantiator.newPrimitiveInstance(getPropertyClass(),
						 text);
}
  String inputText;  public void setUserObject(Object object)  {
	  inputText = (String) object;
	  setRealObject(fromText(inputText));
	  setViewObject(computeViewObject(getParentAdapter(), getRealObject()));
	  uiComponentValueChanged();
	  //System.out.println("New Input:" + inputText);
	}
  public void setCompleteUserObject(Object object)  {
  	  inputText = (String) object;
	  setRealObject(fromCompleteText(inputText));
	  //setViewObject(getViewObject(getRealObject()));
	  computeAndMaybeSetViewObject(); // side effect is to set view object
	  uiComponentValueChanged();
	}
  public void setUserTypedObject (Object realObject)  {	  
	  setRealObject(realObject);
	  setViewObject(computeViewObject(getParentAdapter(), getRealObject()));
	  computeAndMaybeSetViewObject(); // side effect is to set view object
	  uiComponentValueChanged();
	  //System.out.println("New Input:" + inputText);
	}
	public Object getUserObject(Object object)  {
		return inputText;
	}	public Object getUserTypedObject(Object object)  {
		return getRealObject();
	}
//	public Object computeAndMaybeSetViewObject() {
////		return getRealObject(); // these never have special views
//		return getViewObject(); 
//	}


  // Method to return a list of Methods to be invoked
  // on the AWT component to bind the component to 
  // the adaptor. Only one argument (ie the adaptor)
  // will be passed to these methods.
  public Vector getLinkMethods() {
    return linkMethods;
  }
  public void addLinkMethod(Method method) {
    linkMethods.addElement(method);
  }
					  
/*
  // Methods to handle the primitive events
  // generated by the listenable object types.
  public void objectValueChanged(uiValueChangedEvent evt) {
    // This event could have been generated by us!
    if (!evt.getNewValue().equals(getWidgetAdapter().getUIComponentValue())) {
      getWidgetAdapter().setUIComponentValue(evt.getNewValue());
    }
  }  */
  
  // Method to be invoked when a UI component
  // update event is received. 
  public boolean uiComponentValueChanged() {
    if (getAdapterType() == PROPERTY_TYPE ||
	getAdapterType() == PRIMITIVE_TYPE ||		getAdapterType() == KEY_TYPE ||				getAdapterType() == VALUE_TYPE ||
		getAdapterType() == INDEX_TYPE) {
      return super.uiComponentValueChanged();
      //return ;
    }
    else if (getAdapterType() == LISTENABLE_TYPE){
      ValueChangedEvent evt = new ValueChangedEvent(getWidgetAdapter().getUIComponentValue());
      try {
	((ObjectValueChangedListener) computeAndMaybeSetViewObject()).objectValueChanged(evt);
      } catch (ClassCastException e) {
	e.printStackTrace();
	return false;
      }
    }
	propagateChange();
	return true;	/*
    if (getParentAdapter() != null) {
      getParentAdapter().uiComponentValueChanged();
	} else {
		if (uiFrame != null) {			uiFrame.doImplicitRefresh();
		}
	}
	*/		
  }
  
  /*
  // Method to be invoked when Focus is gained
  public void uiComponentFocusGained() {
    uiSelectionManager.select(this);
  }

  // Method to be invoked when Focus is lost
  public void uiComponentFocusLost() {
  }
  */

  // Implement abstract property related methods
  // Set the value of a property    /*  moving to ObjectAdapter
  public void refreshUIComponentOfSameClass (Object newValue) {	  if (getWidgetAdapter() == null) return;	  
	  if (newValue == null) {		  if (getWidgetAdapter().getUIComponentValue() != null)
			  getWidgetAdapter().setUIComponentValue(newValue); 
	  } else if (!getWidgetAdapter().getUIComponentValue().equals(newValue.toString()) && !isEdited()) {
		    getWidgetAdapter().setUIComponentValue(newValue); 
		    //System.out.println("Replaced with "+newValue);		}
  }
  */   
  /*
  public boolean haveChangedClass (Object newValue) {	  if (newValue == null) return false;	  return !(newValue.getClass().equals(getPropertyClass()));
  }
  */
   public void refreshValue(Object newValue1) {
	  refreshValue(newValue1, false);
  }
  

  public void refreshValue(Object newValue1, boolean forceUpdate) {
	  setValueOfAtomicOrPrimitive(newValue1);	  /*
	  //adding this may cause problems and it did	  //if (newValue1.equals(getValue())) return;
	  // First check if the type of newValue
	  // matches the type this attributed object represents
	  //System.out.println("primtive set value");
	  //Object newValue = uiGenerator.getViewObject(newValue1);	  setRealObject(newValue1);	  setViewObject(getViewObject(newValue1));	  //if (getWidgetAdapter() == null) return;	  
	  propagatePreConditions();	  Object newValue = getViewObject(newValue1);	  
	  if (haveChangedClass(newValue)) {		  if (getParentAdapter() instanceof uiReplaceableChildren)
				  ((uiReplaceableChildren) getParentAdapter()).replaceAttributedObject(this, newValue);	  } else		  refreshUIComponentOfSameClass (newValue);
		  	   */	  processNameChild(newValue1);
	  if (attributeChangePending && forceUpdate ) {
			refreshAttributes();
			attributeChangePending = false;
		}	 
  }
  void processNameChild(Object newValue) {	  if (isNameChild || ((keyAdapter != null) && keyAdapter.isNameKey())) {		  ((CompositeAdapter) getParentAdapter()).nameChildChanged((String) newValue);	  }
	  
  }    public void setIsNameChild(boolean val) {	  //System.out.println(getPath() + "name child" + this + getValue());
	  isNameChild = true;  }
  
  transient boolean tried = false;

  public Object getValue() {	  //System.out.println("get value called");
	if (getRealObject() == null)
		return null;	if (getWidgetAdapter() == null) {
		return computeAndMaybeSetViewObject();	}
	Class realObjectClass = getRealObject().getClass();

    // If the realObject type is Primitive
    // return value contained in the UI
    // else return the realObject
    PrimitiveClassList pcl = new PrimitiveClassList();
    if (pcl.isPrimitiveClass(realObjectClass.getName()) ||
	realObjectClass.isPrimitive() ||
	getRealObject() instanceof Primitive) {
      
      //      Object obj = getViewObject();
      //if (obj != null &&
      //  obj instanceof uiPrimitive)
      //return obj;
      //else
      return Instantiator.newPrimitiveInstance(getPropertyClass(),
						 getWidgetAdapter().getUIComponentValue());
    }
    else
      return getRealObject();
  }  public  Object getValueOrRealObject() {
		return getValue();	}


  public void select() {	  /*
    if (getGenericWidget().elided)
      getGenericWidget().selectElideComponent();
    else	  */	  if (getWidgetAdapter() != null)
		getWidgetAdapter().setUIComponentSelected();
  }
  

  public void unselect() {	 /*
    if (getGenericWidget().elided)
      getGenericWidget().unselectElideComponent();
    else */	  if (getWidgetAdapter() != null)
		getWidgetAdapter().setUIComponentDeselected();
  }
  
  public String getChildSelectableIndex(Selectable child) {
    return "";
  }  
  
  public Selectable getChildSelectable(String index) {
    return null;
  }  public void refresh() {
	  setEdited(false);
	  implicitRefresh();  }  
  public void implicitRefresh() {	  updateParentComponentAndUpdateWidgetAdapter();
  }
  public boolean getDefaultExpanded() {
	  return super.getDefaultExpanded();
		//return true;
	}  public void implicitRefreshOld() {	  //System.out.println("primitive refresh");
	  	   Object parentObject = parent.computeAndMaybeSetViewObject();
	  if (getAdapterType() == PROPERTY_TYPE) {		  //System.out.println("prop type");
		  // Get the property value using a getValue() call
		  // Find the write method of this property and
		  // invoke this method on the parent object.		  Object newValue = get();
		  //if (propertyReadMethod != null) {		  if (newValue != null) {
			  //Object parentObject = parent.getViewObject();
			  //Object newValue;
			  /*			  Object parms[] = {};
			  try {
				  newValue = propertyReadMethod.invoke(parentObject, parms);
			  */				  getWidgetAdapter().setUIComponentValue(newValue);				  /*
			  }
			  catch (Exception e) {
				  System.out.println("Exception occured while trying to call "+propertyReadMethod.getName()+" on "+parentObject);
				  e.printStackTrace();
			  }
				  */		  }
		  
	  }	else {		  //Object parentObject = parent.getViewObject();
		  if (parentObject instanceof Vector) {
			  // In these cases, we'll have to perform a 
			  // vector.getElementAt().
			  Object newValue;
			  int index;
			  try {				  System.out.println(this.getAdapterIndex());
				  index = Integer.parseInt(this.getAdapterIndex());
			  } catch (NumberFormatException e) {
				  e.printStackTrace();//				  System.out.println("Exception");
				  index = 0;
			  }
			  try {
				  
				  //System.out.println("Getting component "+index+" of vector" + parentObject);
				  newValue = ((Vector) parentObject).elementAt(index);				  getWidgetAdapter().setUIComponentValue(newValue);
			  } catch (ArrayIndexOutOfBoundsException e) {  }
		  }		  
		  else  super.implicitRefresh(true);	  
		  
		  	  }  }
  public  ObjectAdapter createAdapter (
		  //Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) throws RemoteException  {		return createPrimitiveAdapter (
				//containW, 
				obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	}  public static PrimitiveAdapter createPrimitiveAdapter (
		  //Container containW,
		  Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,											  boolean textMode) throws RemoteException {
	  if (!(obj instanceof Primitive) && !uiGenerator.isPrimitiveClass(inputClass)) 		  return null;
      Class componentClass;
      inputClass = PrimitiveClassList.getWrapperType(inputClass);
      PrimitiveAdapter primitiveAdapter = new PrimitiveAdapter();
      setAdapterAttributes(primitiveAdapter, obj, parentObject, name);
      //primitiveAdapter.setLockManager(lockManager);
      primitiveAdapter.setPropertyClass(inputClass);
      primitiveAdapter.setPropertyName(name);
      if (obj instanceof Primitive) {
	if (obj != null)
	  ((Primitive) obj).addObjectValueChangedListener(primitiveAdapter);
	primitiveAdapter.setViewObject(obj);	//primitiveAdapter.setRealObject(obj);
	primitiveAdapter.setAdapterType(ObjectAdapter.LISTENABLE_TYPE);
      }
      else {
	primitiveAdapter.setAdapterType(ObjectAdapter.PRIMITIVE_TYPE);
      }
      if (propertyFlag) {
	primitiveAdapter.setAdapterType(ObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, primitiveAdapter);
      }
      
      primitiveAdapter.setParentAdapter((CompositeAdapter)adaptor);	  primitiveAdapter.setUIFrame(adaptor.getUIFrame());	  //this.addToDrawing(primitiveAdapter, obj);	  	  
      primitiveAdapter.processAttributeList();
      primitiveAdapter.setViewObject(obj);
      //primitiveAdapter.setViewObject(obj);
      primitiveAdapter.setRealObject(obj1);
	  //if (obj1 instanceof shapes.ShapeModel)
		  //System.out.println("found ShapeModel" + obj1.getClass());	  //System.out.println("setting value of" + primitiveAdapter);
      primitiveAdapter.refreshValue(obj);	  
	  bus.uigen.uiGenerator.deepElide(primitiveAdapter);	  //primitiveAdapter.processAttributeList();
      
      return primitiveAdapter;  }
  public  void init (ConcreteType concreteObject,													   /*Container containW,*/
													   Object obj, 											  Object obj1, 											  Object parentObject, 
											  int posn,											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) {
	  if (obj != null)	  inputClass = ((ConcretePrimitive) concreteObject).getWrapperType(inputClass);
	  	  super.init(concreteObject,													/*containW, */
													obj, 
									obj1, 									parentObject, 
									posn,									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	  if (propertyFlag) return;	  if (obj instanceof Primitive) {
		  if (obj != null)
			  ((Primitive) obj).addObjectValueChangedListener(this);
		  this.setViewObject(obj);		  //primitiveAdapter.setRealObject(obj);
		  this.setAdapterType(ObjectAdapter.LISTENABLE_TYPE);
	  }
	  else {
		  this.setAdapterType(ObjectAdapter.PRIMITIVE_TYPE);
	  }
	  
	  
	  
			
		  }
  public String toTextLine() {
	  if (getRealObject() != null)
	  return getRealObject().toString();
	  else {
		  
	  
	  
		ClassProxy c = getPropertyClass();
		if (c == null)
			// c = Object.class;
			StandardProxyTypes.objectClass();
		String retVal = AClassDescriptor.getMethodsMenuName(c);
		return "null" + "(" + retVal + ")";
		
	  }
	}
  
 public final static int  PRIMITIVE_ELIDE_COMPONENT_WIDTH = 10;  
  public Integer getElideComponentWidth() {
		// return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		// Integer retVal = (Integer)
		// getMergedOrTempAttributeValue(AttributeNames.LABEL_LENGTH);
		// Integer retVal = (Integer)
		// getMergedAttributeValue(AttributeNames.COMPONENT_WIDTH);
		Integer retVal = (Integer) getMergedTempOrDefaultAttributeValue(AttributeNames.ELIDE_COMPONENT_WIDTH);

		if (retVal != null)
			return retVal;
		else
			return PRIMITIVE_ELIDE_COMPONENT_WIDTH ;

	}
  /*
   * a primitive  does not really have any properties but it has attributes
   * property name will be an attribute name
   */

//  public synchronized void propertyChange(PropertyChangeEvent evt) {
//	  
//  }
  
//  void setIsOnlyChild(boolean newVal) {
//		super.setIsOnlyChild(newVal);
//		if (newVal && 
//				getParentAdapter().isTopDisplayedAdapter() && 
//				getPropertyClass().equals(IntrospectUtility.stringClass())) {
//			setPreferredWidget(VirtualTextArea.class.getName());
//		}
//			
//		
//	}

}



