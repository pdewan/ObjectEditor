package bus.uigen.adapters;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.ButtonModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.SpinnerModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableCellEditor;

import util.trace.Tracer;
import bus.uigen.WidgetAdapter;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.oadapters.EnumerationAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.sadapters.ConcreteEnumeration;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;
import bus.uigen.widgets.events.VirtualFocusListener;

//public class uiJTextFieldAdapter extends uiWidgetAdapter public abstract class AbstractEnumerationWidgetAdapter extends WidgetAdapterimplements ItemListener, VirtualFocusListener, KeyListener, SpinnerModel, /*ComboBoxModel,*/ MutableComboBoxModel, TableCellEditor, VirtualActionListener { //uiJTextComponentAdapter {

	long maxValue;
	long minValue;
	long stepValue;
	boolean isEnumeration = false;
	boolean isSubRange = false;
	boolean isSubRangeSet = false;
	boolean isEnumerationSet = false;
	
	void setIsEnumeration() {
		isEnumerationSet = true;
		if (getObjectAdapter() instanceof EnumerationAdapter || getObjectAdapter().isEnum())
			isEnumeration = true;		
	}
	
	
	void setIsSubRange() {
		isSubRangeSet = true;
		if (isEnumeration()) return;
		ObjectAdapter adapter = getObjectAdapter();
		if (!IntrospectUtility.isIntFamily(adapter.getPropertyClass()))
			Tracer.error(adapter.getPropertyClass() + "is not in int family" );
		Object minObject = adapter.getMinValue();
		if (minObject == null)
			Tracer.error(adapter.getBeautifiedPath() + " does not have min value" );
		minValue = (Long) minObject;
		Object maxObject = adapter.getMaxValue();
		if (maxObject == null)
			Tracer.error(adapter.getBeautifiedPath() + " does not have max value" );
		maxValue = (Long) maxObject;
		stepValue = (Long) adapter.getStepValue();
		isSubRange = true;		
		
	}
	// ObjectAdapter enumAdapter;
	
	public boolean isEnumeration() {
		if (!isEnumerationSet)
			setIsEnumeration();
		 return isEnumeration;
	}
	
	public boolean isSubRange() {
		if (!isSubRangeSet)
			setIsSubRange();
	  	return isSubRange;
	}
	
	
	
 	
  public boolean isComponentAtomic() {
  	return true;
  }
 
  //uiEnumerationAdapter enumerationAdapter;
  public EnumerationAdapter getEnumerationAdapter() {
	  return (EnumerationAdapter) this.getObjectAdapter();
  }
  
 //Vector currentButtonGroupStrings = new Vector();
 
  
  
  public static Vector toVector (Enumeration elements) {
	Vector retVal = new Vector();
	while (elements.hasMoreElements())
		retVal.addElement(((ButtonModel) elements.nextElement()).getActionCommand());
	return retVal;
  }  public Object getElementAt(int i) {	  	  try {
		  if (isSubRange()) {
			  return minValue + i*stepValue;			  
		  }
		  //return enumerationAdapter.getConcreteEnumeration().choiceAt(i);
	  	//Object retVal = getEnumerationAdapter().getConcreteEnumeration().choiceAt(i);
	  	Object retVal = getEnumerationAdapter().choiceAt(i);
	  	//System.out.println ("getElementAt:" + i + " element:" + retVal);
	  	return retVal;
	  }  catch (Exception e) {
		return null;
	   }
	  /*	  try {
		  Object[] parameters = {new Integer(i)};		  //return enumerationAdapter.getChoiceAtMethod().invoke(obj, parameters);		  return uiMethodInvocationManager.invokeMethod(enumerationAdapter.getChoiceAtMethod(), obj, parameters);
	  }  catch (Exception e) {
		return null;
	   }
	  */  }  public int getSize() {
	  try {
		  
		  if (isEnumeration()) {
		  	 //return getEnumerationAdapter().getConcreteEnumeration().choicesSize();
	  //int retVal = getEnumerationAdapter().getConcreteEnumeration().choicesSize();
	  int retVal = getEnumerationAdapter().choicesSize();
	  //System.out.println("getSize:" + retVal);
	  return retVal;
		  } else if (isSubRange()) {
			  return Math.round((maxValue - minValue + stepValue)/stepValue);
		  }
		  else return 0;
	  
	  }  catch (Exception e) {
		return 0;
	  }
	  /*	  try {
		  Object[] parameters = {};		  //return  ((Integer) enumerationAdapter.getChoicesSizeMethod().invoke(obj, parameters)).intValue();		  return  ((Integer) uiMethodInvocationManager.invokeMethod(enumerationAdapter.getChoicesSizeMethod(), obj, parameters)).intValue();
	  }  catch (Exception e) {
		return 0;
	  }
	  */  }
  Object lastSelectedItem;
  //Object lastValueReturned;
  public Object getSelectedItemFromModel() {
	  //return getEnumerationAdapter().getConcreteEnumeration().getValue();
	  if (isEnumeration())
	  return getEnumerationAdapter().getChoiceValue();
	  else if (isSubRange())
		  //return getObjectAdapter().getValue();
		  return getObjectAdapter().getRealObject();
	  else
		  return null;
  }
  boolean uninitialized = false;  public Object getSelectedItem() {	  try {
		  //lastSelectedItem =  enumerationAdapter.getConcreteEnumeration().getValue();		//return lastSelectedItem;
		if (lastSelectedItem != null) 
			return lastSelectedItem;
		uninitialized = true;
	  	//Object selVal = getEnumerationAdapter().getConcreteEnumeration().getValue();
	  	Object selVal = getSelectedItemFromModel();
	  	//return enumerationAdapter.getConcreteEnumeration().getValue();
	  	//lastValueReturned = selVal;
	  	lastSelectedItem = selVal;
	  	return selVal;
	  }  catch (Exception e) {
		  System.out.println("Returning null from getSelectedItem");		  return null;
	  }
	  	  /*
	  try {
		  Object[] parameters = {};		  //lastSelectedItem =  enumerationAdapter.getReadMethod().invoke(obj, parameters);
		  lastSelectedItem =  uiMethodInvocationManager.invokeMethod(enumerationAdapter.getReadMethod(), obj, parameters);		  return lastSelectedItem;
	  }  catch (Exception e) {		  return null;
	  }
	  */  }
  //boolean firstTime = true; 
  //the above was probably needed for older versions of swing when this method was called once automatically without a value being set  public void setSelectedItem(Object newVal) {
  	//addElement(newVal);	  /*
	  if (firstTime) {
		  firstTime = false;		  return;
	  }
	  */
	  
	  if (newVal == null) return;
	  if (newVal.equals(lastSelectedItem)) return;
	 
	  lastSelectedItem = newVal;
	  if (isSubRange()) {
			if (getObjectAdapter().getReceivedNotification() && !getObjectAdapter().getPropagateChange()) {
				return;
			}
			uiComponentValueChanged();
			return;
			
	  }
	  
	  ClassProxy lastClass =  ReflectUtil.toMaybeProxyTargetClass(lastSelectedItem);
	  boolean addUserChoice = lastClass !=  ReflectUtil.toMaybeProxyTargetClass(newVal) ;
	  //lastSelectedItem = newVal;	  
	  try {
		//int oldSize = getSize();
		  ConcreteEnumeration concreteEnumeration = getEnumerationAdapter().getConcreteEnumeration();
		  if (addUserChoice)
			  concreteEnumeration.addUserChoice((String) newVal, getEnumerationAdapter());
		  else
			  concreteEnumeration.setValue(newVal, getEnumerationAdapter());		//getEnumerationAdapter().getConcreteEnumeration().setValue(newVal, getEnumerationAdapter());
		Object realObject = getEnumerationAdapter().getConcreteEnumeration().getTargetObject();
		getEnumerationAdapter().setRealObject(realObject); // will only change in Enum
		//property change can occur at this time
		
		if (getEnumerationAdapter().getReceivedNotification() && !getEnumerationAdapter().getPropagateChange()) {
			return;
		}
		
		//if (newVal.equals(lastSelectedItem)) return;
		
	    uiComponentValueChanged();
	    /*
	    int newSize = getSize();
	    if (oldSize != newSize) {
	    	
	    }
	    */
		/*
		int newSize = getSize();
		if (oldSize != newSize) {
			addElement(null);
		}
		*/
	  }  catch (Exception e) {
	   }
	  /*	  try {
		  Object[] parameters = {newVal};		  uiMethodInvocationManager.invokeMethod(enumerationAdapter.getWriteMethod(), obj, parameters);
		  uiComponentValueChanged();		  //cb.setModel(this);
	  }  catch (Exception e) {
	   }	  */
	    }
  Vector<ListDataListener> listDataListeners = new Vector();  public void addListDataListener (ListDataListener ld) {
  	if (listDataListeners.contains(ld)) return;
  	listDataListeners.addElement(ld);  }  public void removeListDataListener (ListDataListener ld) {
  	listDataListeners.remove(ld);
  }
   /*  String[] defaultStrings;
  Vector defaultFinals;
  */
  Object currentModel;
  boolean haveSetModel = false;
  
  int oldSize;  
  /*
  Hashtable stringToObjects;
  Hashtable stringToButtons;
  */
  /*
  void rebuildButtons () {
  	stringToObjects = new Hashtable();
  	stringToButtons = new Hashtable();
  	buttondPanel.removeAll();
  	//buttonGroup = new ButtonGroup();
  	for (int i = 0; i < getSize(); i ++) {
  		Object item = getElementAt(i);
  		String itemString = item.toString();
  		//AbstractButton button = new JRadioButton(itemString);
  	    AbstractButton button = createButton (itemString);
  		button.setActionCommand(itemString);
  		stringToObjects.put(itemString, item);
  		stringToButtons.put(itemString, button);
  		button.addActionListener(this);
  		//buttonGroup.add(radioButton);
  		buttondPanel.add(button);
  		doAdditionalProcessing(button);
  	}
  	int numCols = getObjectAdapter().getUnboundButtonsRowSize();
  	buttondPanel.setLayout(new GridLayout (0, numCols));
  	oldSize = getSize();
  	
  	
  }
  abstract AbstractButton createButton (String itemString);
  */
  /*
  void refreshPanel (Object newVal) {
  	
		 
  	
  }
  void doAdditionalProcessing (AbstractButton button) {
  	
  }
  */
  /* public void setUIComponentTypedValue(Object newval) {
//	 System.out.println("in setuictv");
	 String val;
	 
	 Component c = getUIComponent();
	 	 
			 	  if ( this.getObjectAdapter() instanceof uiEnumerationAdapter) {		  enumerationAdapter = (uiEnumerationAdapter) this.getObjectAdapter();
		  boolean rebuild = (newval != currentModel) || (oldSize != getSize() );
		   currentModel = newval;
		   if (rebuild)
		   	rebuildButtons();
			 //Object initialItem = this.getSelectedItem();	
		  
		  refreshPanel(newval);
		  
		  
	  }
	 
		
		 
		
	
 }
 */
 /*
 String getSelectedButtonString () {
 	ButtonModel button = buttonGroup.getSelection();
 	 return button.getActionCommand();
 	
 }
 */ // this seems wrong - perhaps it is never called
  public boolean isEnum() {
	  return getObjectAdapter().isEnum();
	  /*
	  if (!haveSetModel)
		  return false;
	  return RemoteSelector.getClass(currentModel).isEnum();
	  */
  }  public Object getUIComponentValue() {
	  if (isSubRange())
		  return getValue().toString();
  	return currentModel;
  	/*
  	 ButtonModel button = buttonGroup.getSelection();
  	 String s = button.getActionCommand();
  	 return stringToObjects.get(s);
  	 */
  	 
  }
 /* boolean isEditable = true;
 
  public void setUIComponentEditable() {	  isEditable = true;
  }
  
  public void setUIComponentUneditable() {	  isEditable = false; }
 */

  //Color oldColor = Color.white;
  /*
  public void setUIComponentSelected() {	super.setUIComponentSelected();
    if (getUIComponent().getBackground() != SelectionColorSelector.getColor())
      oldColor = getUIComponent().getBackground();	else oldColor = getOriginalBackground(getUIComponent());
    getUIComponent().setBackground(SelectionColorSelector.getColor());
    // Repaint the component to get the color right
    getUIComponent().repaint();
  }  
  public void setUIComponentDeselected() {	super.setUIComponentDeselected();
    getUIComponent().setBackground(oldColor);
    getUIComponent().repaint();
  }
  */  
  /*
  public void linkUIComponentToMe (VirtualComponent comb) {
  	  
	  comb.addFocusListener(this);
    }
  */
  //public abstract VirtualComponent getComponent();
  
     
    //at this point  /*
  uiObjectAdapter adapter;
  uiGenericWidget genericWidget;	*/	
  public boolean uiComponentValueChanged() {
	//  if (textChanged()) {  //wanna make sure we don't waste invocations on object if something is done but
							//doesn't ultimately change the value of the widget		boolean retVal = super.uiComponentValueChanged();
		setIndexOfSelectedItem();
		return retVal;	  //}
  }
  
    /*
  public void uiComponentValueEdited() {
	  //if (textChanged())		super.uiComponentValueEdited();	
  }*/  /*
  boolean textChanged() {	  if (haveSetModel) return false;
	  if (text == null) return true;	  	  return !text.equals(getSelectedButtonString());	  
  }
  */  
  /*
  						     public void actionPerformed(ActionEvent e) {
	  
	//	System.out.println("adapter actionperformed called");		 
		if (e.getSource() == cb) {
			
		//	System.out.println("comb fires an event");
			
		}		super.actionPerformed(e);
	
  }

  public boolean processAttribute(Attribute attrib) {
    if (attrib.getName().equals("actionMode")) {
      if (attrib.getValue() instanceof Boolean) {
	actionMode = ((Boolean) attrib.getValue()).booleanValue();
      }
      return true;
    }
    else
      return super.processAttribute(attrib);
  }
  */
  public void keyReleased(KeyEvent k) {
	  System.out.println("000000");    }  public void keyPressed(KeyEvent k) {	  if (haveSetModel) return;
	  System.out.println("33434324");  	  if (k.getKeyCode() == KeyEvent.VK_ENTER)/* && (noItemState))  */{		  super.uiComponentValueEdited(true);			
	  
	  }	    }
  
    public void keyTyped (KeyEvent k) {
	 System.out.println("ppppp");   
	  
	  /*	  if (actionPerformed)		  actionPerformed = false;	  else
	      super.uiComponentValueEdited();	
	  */  }
  public void actionPerformed (VirtualActionEvent e) {
  	
  	String actionCommand = e.getActionCommand();
  	setSelectedItem(actionCommand);
  	
  }
  
  public void itemStateChanged(ItemEvent ev) {    //if (haveSetModel) return;	//noItemState = false;
    uiComponentValueChanged();
  }
    // Methods invoked on focus gain/loss in the view
  public void focusGained(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusGained();
    }
  }

  public void focusLost(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusLost();
    }
  }  public void addCellEditorListener(CellEditorListener l) {};
          
  public void cancelCellEditing() {};
          
 public Object getCellEditorValue()  { return currentModel; }
 public  boolean isCellEditable(EventObject anEvent) {
	return true;};
 public void removeCellEditorListener(CellEditorListener l){
	 
 };
 public boolean shouldSelectCell(EventObject anEvent){
	return true;
 };
 public boolean stopCellEditing() {	 return true;
 }
 /* public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	 return buttondPanel;
 }
 */
/* (non-Javadoc)
 * @see javax.swing.MutableComboBoxModel#removeElementAt(int)
 */
public void removeElementAt(int arg0) {
	// TODO Auto-generated method stub
	
}
/* (non-Javadoc)
 * @see javax.swing.MutableComboBoxModel#addElement(java.lang.Object)
 */
public void addElement(Object arg0) {
	// TODO Auto-generated method stub
	int newSize = getSize();
	for (int i = 0; i < listDataListeners.size(); i++) {
		((ListDataListener) listDataListeners.elementAt(i)).contentsChanged(
				new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, newSize-1 ));
	
	}
}
/* (non-Javadoc)
 * @see javax.swing.MutableComboBoxModel#removeElement(java.lang.Object)
 */
public void removeElement(Object arg0) {
	// TODO Auto-generated method stub
	
}
/* (non-Javadoc)
 * @see javax.swing.MutableComboBoxModel#insertElementAt(java.lang.Object, int)
 */
public void insertElementAt(Object arg0, int arg1) {
	// TODO Auto-generated method stub
	
}
Vector<ChangeListener> listeners = new Vector();
public void addChangeListener(ChangeListener l) {
	if (listeners.contains(l))
		return;
	listeners.add(l);
}
public Object getNextValue() {
	if (isSubRange()) {
		Number curValue = (Number) getValue();
		long nextValue = curValue.longValue() + stepValue;
		if (nextValue > maxValue)
			return minValue;
		
		return nextValue;
	}
	if (index == getSize() - 1)
		return getElementAt(0);
	return (getElementAt(index+1));
}

public Object getPreviousValue() {
	if (isSubRange()) {
		Number curValue = (Number) getValue();
		long prevValue = curValue.longValue() - stepValue;
		if (prevValue < minValue) {
			return maxValue;
		}
		
		return prevValue;
	}
	if (index == 0)
		return getElementAt(getSize() - 1);
	return (getElementAt(index-1));
}
int index;
public void setIndexOfSelectedItem() {
	//index = getEnumerationAdapter().getConcreteEnumeration().getIndexOfSelection();
	if (isEnumeration())
	index = getEnumerationAdapter().getIndexOfSelection();
}

public Object getValue() {
	return getSelectedItem();
}
public void removeChangeListener(ChangeListener l) {
}
 
public void setValue(Object value) {
	setSelectedItem(value);
	
	
}
public void notifyChangeListener(ChangeListener l) {
	l.stateChanged (new ChangeEvent(this));
}
public void notifyChangeListeners() {
	notifyChangeListeners(new ChangeEvent(this));
}
public void notifyChangeListeners(ChangeEvent event) {
	for (int i= 0; i < listeners.size(); i++)
		notifyChangeListener(listeners.elementAt(i));
		//listeners.elementAt(i).stateChanged(event);
}

public void notifyListDataListener(ListDataListener l) {
	int lastIndex = getSize() -1;
	notifyListDataListener(l, new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, lastIndex));
	
}
public void notifyListDataListener(ListDataListener l, ListDataEvent event) {
	l.contentsChanged (event);
}
public void notifyListDataListeners() {
	int lastIndex = getSize() -1;
	notifyListDataListeners(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, lastIndex - 1 ));
}
public void notifyListDataListeners(ListDataEvent event) {
	for (int i= 0; i < listDataListeners.size(); i++)
		notifyListDataListener(listDataListeners.elementAt(i));
		//listeners.elementAt(i).stateChanged(event);
}


 
  }



