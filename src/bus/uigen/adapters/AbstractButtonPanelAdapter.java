package bus.uigen.adapters;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTable;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.EnumerationAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.events.VirtualActionEvent;

//public class uiJTextFieldAdapter extends uiWidgetAdapter public abstract class AbstractButtonPanelAdapter extends AbstractEnumerationWidgetAdapter { //uiJTextComponentAdapter {

  
	
 	
  //ButtonGroup buttonGroup = null;
	//VirtualContainer virtualButtonsPanel;
  VirtualContainer buttonsPanel = null;  String text;
  /*  public String getType() {
    return "java.lang.String";
  }  */
  /*
  public boolean isComponentAtomic() {
  	return true;
  }
  */
  /*
  public String componentToText() {
  	return getSelectedButtonString();
  	
}
*/
/*
  uiEnumerationAdapter enumerationAdapter;
  */
  Vector currentButtonGroupStrings = new Vector();
 
  
  /*
  public static Vector toVector (Enumeration elements) {
	Vector retVal = new Vector();
	while (elements.hasMoreElements())
		retVal.addElement(((ButtonModel) elements.nextElement()).getActionCommand());
	return retVal;
  }  public Object getElementAt(int i) {	  	  try {
		  //return enumerationAdapter.getConcreteEnumeration().choiceAt(i);
	  	Object retVal = enumerationAdapter.getConcreteEnumeration().choiceAt(i);
	  	//System.out.println ("getElementAt:" + i + " element:" + retVal);
	  	return retVal;
	  }  catch (Exception e) {
		return null;
	   }
	    }  public int getSize() {
	  try {
		  	 //return enumerationAdapter.getConcreteEnumeration().choicesSize();
	  int retVal = enumerationAdapter.getConcreteEnumeration().choicesSize();
	  //System.out.println("getSize:" + retVal);
	  return retVal;
	  
	  }  catch (Exception e) {
		return 0;
	  }
	    }
  Object lastSelectedItem;  public Object getSelectedItem() {	  try {
		  //lastSelectedItem =  enumerationAdapter.getConcreteEnumeration().getValue();		//return lastSelectedItem;
	  	Object selVal = enumerationAdapter.getConcreteEnumeration().getValue();
	  	//return enumerationAdapter.getConcreteEnumeration().getValue();
	  	return selVal;
	  }  catch (Exception e) {		  return null;
	  }
	  	    }
  //boolean firstTime = true; 
  //the above was probably needed for older versions of swing when this method was called once automatically without a value being set  public void setSelectedItem(Object newVal) {
  	//addElement(newVal);	 
	  if (newVal == null) return;
	  if (newVal.equals(lastSelectedItem)) return;
	  lastSelectedItem = newVal;
	  try {
		int oldSize = getSize();		enumerationAdapter.getConcreteEnumeration().setValue(newVal, enumerationAdapter);
	    uiComponentValueChanged();
		
	  }  catch (Exception e) {
	   }
	  	  
	    }
  Vector listDataListeners = new Vector();  public void addListDataListener (ListDataListener ld) {
  	if (listDataListeners.contains(ld)) return;
  	listDataListeners.addElement(ld);  }  public void removeListDataListener (ListDataListener ld) {
  	listDataListeners.remove(ld);
  }
  */
    //public Component instantiateComponent(Class cclass, uiObjectAdapter adapter) {
  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter theAdapter) {
	  instantiatedComponent = true;	  //System.out.println("instantiating  jcombo");	 // buttonGroup = new ButtonGroup();
	 // buttonsPanel =  AnAWTContainer.virtualContainer(SwingPanelFactory.createJPanel());
	  //buttonsPanel =  SwingPanelFactory.createJPanel();
	  buttonsPanel =  PanelSelector.createPanel();
	  //virtualButtonsPanel =  AnAWTContainer.virtualContainer(buttonsPanel);
	  theAdapter.getRow();
	  //setSize(buttonsPanel);
	  
		  
	  //cb.setSize(20, cb.getSize().height);	  //cb.setSelectedIndex(0);
	  //cb.setEditable(true);
	 // cb.setEnabled(true);
	  //cb.setLightWeightPopupEnabled(false);
	  //return virtualButtonsPanel;	  return buttonsPanel;
  }
  public VirtualComponent getUIComponent() {
	  return buttonsPanel;
  }
  public void linkUIComponentToMe() {
	  
  }
  public void linkUIComponentToMe(VirtualComponent c) {
	  setAttributes(c);
	  
  }
  /*  String[] defaultStrings;
  Vector defaultFinals;
  Object currentModel;
  boolean haveSetModel = false;
  int oldSize;  
  */
  Hashtable stringToObjects;
  Hashtable stringToButtons;
  void rebuildButtons () {
  	stringToObjects = new Hashtable();
  	stringToButtons = new Hashtable();
  	buttonsPanel.removeAll();
  	//buttonGroup = new ButtonGroup();
  	int newSize = getSize();
//  	for (int i = 0; i < getSize(); i ++) {
  	for (int i = 0; i < newSize; i ++) {

  		Object item = getElementAt(i);
  		String itemString = item.toString();
  		//AbstractButton button = new JRadioButton(itemString);
  	    VirtualButton button = createButton (itemString);
  	    setColors(button);
  	    //VirtualButton button = createButton (itemString);
  		button.setActionCommand(itemString);
  		stringToObjects.put(itemString, item);
  		stringToButtons.put(itemString, button);
  		button.addActionListener(this);
  		//buttonGroup.add(radioButton);
  		buttonsPanel.add(button);
  		
  		doAdditionalProcessing(button);
  	}
  	// force the first item to be selected
		lastSelectedItem = null;
  	
  	//int numCols = getObjectAdapter().getUnboundButtonsRowSize();
  	setLayout();
  	// not sure we need this as color is set before this 
 // 	setColors(buttonsPanel);
  	/*
  	buttonsPanel.setLayout(new GridLayout (0, numCols));
  	oldSize = getSize();
  	*/
  	oldSize = newSize;
  	
  	
  }
  void setLayout () {
	  String direction = getObjectAdapter().getDirection();
	  if (direction != null &&  direction.equals(AttributeNames.HORIZONTAL))		  
		  buttonsPanel.setLayout(new GridLayout(1, 0));
	  else if( direction != null &&  direction.equals(AttributeNames.VERTICAL))
		  buttonsPanel.setLayout(new GridLayout (0, 1));
	  else {
		    int numCols = getObjectAdapter().getUnboundButtonsRowSize();
		  	buttonsPanel.setLayout(new GridLayout (0, numCols));
	  }
	  
  }
  //abstract AbstractButton createButton (String itemString);
  abstract VirtualButton createButton (String itemString);
  void refreshPanel (Object newVal) {
  	/*

	  Object newSelectedItem = getSelectedItem();
	  //if (!getSelectedItem().equals(lastSelectedItem)) {
	  if (!newSelectedItem.equals(lastSelectedItem)) {
	  	
		 lastSelectedItem = newSelectedItem;
		 JRadioButton selectedButton = (JRadioButton)stringToButtons.get(newSelectedItem.toString());
		 if (selectedButton == null ) return;
		 selectedButton.setSelected(true);
		 //((JRadioButton)stringToButtons.get(newSelectedItem.toString())).setSelected(true);
		 text = newSelectedItem.toString();
		 //buttonGroup.setSelected((JRadioButton)stringToButtons.get(lastSelectedItem.toString()), true);
		 //cb.setSelectedItem(newSelectedItem);
		 //cb.validate();
	  }
	  */
		 
  	
  }
  void doAdditionalProcessing (VirtualButton button) {
  	
  }
   public void setUIComponentTypedValue(Object newval) {
//	 System.out.println("in setuictv");
	 String val;
	 
	 //Component c = getUIComponent();
	 	 
			 	  if ( this.getObjectAdapter() instanceof EnumerationAdapter) {		  //enumerationAdapter = (uiEnumerationAdapter) this.getObjectAdapter();
		  int newSize = getSize();
//		  boolean rebuild = (newval != currentModel) || (oldSize != getSize() );
//		  boolean rebuild = (newval != currentModel) || (oldSize != newSize );

		   currentModel = newval;
		   if (newSize != oldSize)
		   		rebuildButtons();
		   if (newval != currentModel)
			   refreshPanel(newval);

//		   oldSize = newSize;
			 //Object initialItem = this.getSelectedItem();	
		  /*
		  for (int i = 0; i < getSize(); i ++ ) {
		  	String item = getElementAt(i).toString();
		  	if (!currentButtonGroupStrings.contains(item)) {
		  		JRadioButton button = new JRadioButton()
		  
		  		buttonGroup.add
		  	}
		  	*/
//		  refreshPanel(newval);
		  
		  
	  }
	 
		
		 
		
	
 }
 public void actionPerformed (VirtualActionEvent e) {
  	
  	String actionCommand = e.getActionCommand();
  	setSelectedItem(stringToObjects.get(actionCommand));
  	
  }

 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	 return (Component) buttonsPanel.getPhysicalComponent();
 }
 /*
 String getSelectedButtonString () {
 	ButtonModel button = buttonGroup.getSelection();
 	 return button.getActionCommand();
 	
 }
 */
 /* // this seems wrong - perhaps it is never called  public Object getUIComponentValue() {
  	return currentModel;
  	
  	 
  }
  */
 /* boolean isEditable = true;
 
  public void setUIComponentEditable() {	  isEditable = true;
  }
  
  public void setUIComponentUneditable() {	  isEditable = false; }
 */

 // Color oldColor = Color.white;
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
  public void linkUIComponentToMe(Component comb) {
  	comb.addFocusListener(this);
  	  }
  
     
    	
  public void uiComponentValueChanged() {
	//  if (textChanged()) {  //wanna make sure we don't waste invocations on object if something is done but
							//doesn't ultimately change the value of the widget		super.uiComponentValueChanged();	  //}
  }
  */
  
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
  /*
  public void keyReleased(KeyEvent k) {
	  System.out.println("000000");    }  public void keyPressed(KeyEvent k) {	  if (haveSetModel) return;
	  System.out.println("33434324");  	  if (k.getKeyCode() == KeyEvent.VK_ENTER){		  super.uiComponentValueEdited(true);			
	  
	  }	    }
  
    public void keyTyped (KeyEvent k) {
	 System.out.println("ppppp");   
	  
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
 

public void removeElementAt(int arg0) {
	// TODO Auto-generated method stub
	
}

public void addElement(Object arg0) {
	// TODO Auto-generated method stub
	int newSize = getSize();
	for (int i = 0; i < listDataListeners.size(); i++) {
		((ListDataListener) listDataListeners.elementAt(i)).contentsChanged(
				new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, newSize-1 ));
	
	}
}

public void removeElement(Object arg0) {
	// TODO Auto-generated method stub
	
}

public void insertElementAt(Object arg0, int arg1) {
	// TODO Auto-generated method stub
	
}
*/
 
  }



