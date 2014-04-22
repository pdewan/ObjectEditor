package bus.uigen.adapters;
import java.util.Enumeration;

import bus.uigen.widgets.ButtonGroupSelector;
import bus.uigen.widgets.RadioButtonSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualButtonGroup;
import bus.uigen.widgets.VirtualComboBox;
import bus.uigen.widgets.VirtualRadioButton;
import bus.uigen.widgets.events.VirtualActionEvent;

//public class uiJTextFieldAdapter extends uiWidgetAdapter public class RadioButtonPanelAdapter extends AbstractButtonPanelAdapter { //uiJTextComponentAdapter {

  
	
 	
  VirtualButtonGroup buttonGroup = null;
  //Container buttonPanel = null;  String text;
  /*  public String getType() {
    return "java.lang.String";
  }  */
  /*
  public boolean isComponentAtomic() {
  	return true;
  }
  */
  public String componentToText() {
  	return getSelectedButtonString();
  	/*
	  if (cb == null) return "";
	  return (String)(cb.getSelectedItem());
	  */
}
  /*
  uiEnumerationAdapter enumerationAdapter;
  Vector currentButtonGroupStrings = new Vector();
  */
  public void setCurrentChoices () {
  	if (buttonGroup == null) return;
  	Enumeration elements = buttonGroup.getElements();
  	
  	
  }
  void rebuildButtons () {
  	//buttonGroup = new ButtonGroup();
	  buttonGroup = ButtonGroupSelector.createButtonGroup();
  	super.rebuildButtons();
  }
  
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
    public Component instantiateComponent(Class cclass, uiObjectAdapter adapter) {	  //System.out.println("instantiating  jcombo");	  buttonGroup = new ButtonGroup();
	  buttonPanel =  SwingPanelFactory.createJPanel();
	  buttonPanel.setLayout(new GridLayout(0, 1));
	  //cb.setSize(20, cb.getSize().height);	  //cb.setSelectedIndex(0);
	  //cb.setEditable(true);
	 // cb.setEnabled(true);
	  //cb.setLightWeightPopupEnabled(false);
	  	  return buttonPanel;
  }  String[] defaultStrings;
  Vector defaultFinals;
  Object currentModel;
  boolean haveSetModel = false;
  int oldSize;  
  Hashtable stringToObjects;
  Hashtable stringToButtons;
  void rebuildButtons () {
  	stringToObjects = new Hashtable();
  	stringToButtons = new Hashtable();
  	buttonPanel.removeAll();
  	buttonGroup = new ButtonGroup();
  	for (int i = 0; i < getSize(); i ++) {
  		Object item = getElementAt(i);
  		String itemString = item.toString();
  		AbstractButton button = new JRadioButton(itemString);
  		button.setActionCommand(itemString);
  		stringToObjects.put(itemString, item);
  		stringToButtons.put(itemString, button);
  		button.addActionListener(this);
  		//buttonGroup.add(radioButton);
  		buttonPanel.add(button);
  		doAdditionalProcessing(button);
  	}
  	int numCols = getObjectAdapter().getUnboundButtonsRowSize();
  	buttonPanel.setLayout(new GridLayout (0, numCols));
  	oldSize = getSize();
  	
  	
  }
  */
 VirtualRadioButton createButton (String itemString) {
  	//return new JRadioButton(itemString);
  	return RadioButtonSelector.createRadioButton(itemString);
  	
  }
  void doAdditionalProcessing (VirtualButton button) {
  	buttonGroup.add(button);
  }
  void refreshPanel (Object newVal) {

//	  Object newSelectedItem = getSelectedItem(); //why was this line there?
	  Object newSelectedItem = newVal;
	  //if (!getSelectedItem().equals(lastSelectedItem)) {
	  if (!newSelectedItem.equals(lastSelectedItem) || uninitialized) {
	  	/*
	  	if (!cb.getSelectedItem().equals(lastSelectedItem)) // combo box if confused
	  		cb.setSelectedItem(lastSelectedItem);
	  		*/
		  uninitialized = false;
		 lastSelectedItem = newSelectedItem;
		 //JRadioButton selectedButton = (JRadioButton)stringToButtons.get(newSelectedItem.toString());
		 VirtualRadioButton selectedButton = (VirtualRadioButton)stringToButtons.get(newSelectedItem.toString());
		 if (selectedButton == null ) return;
		 selectedButton.setSelected(true);
		 //((JRadioButton)stringToButtons.get(newSelectedItem.toString())).setSelected(true);
		 text = newSelectedItem.toString();
		 //buttonGroup.setSelected((JRadioButton)stringToButtons.get(lastSelectedItem.toString()), true);
		 //cb.setSelectedItem(newSelectedItem);
		 //cb.validate();
	  }
		 
	
}
  public void actionPerformed (VirtualActionEvent e) {
	    Object source = e.getSource();
	    // should change this to VirtualRadioButton soon
	    
	  	
	  	super.actionPerformed(e);
//	  	if (source instanceof JRadioButton) {
//	    	((JRadioButton) source).setSelected(true);	    	
//	    }
	  	
	  }
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
		  
		  
		  Object newSelectedItem = getSelectedItem();
		  //if (!getSelectedItem().equals(lastSelectedItem)) {
		  if (!newSelectedItem.equals(lastSelectedItem)) {
		  	
			 lastSelectedItem = newSelectedItem;
			 JRadioButton selectedButton = (JRadioButton)stringToButtons.get(newSelectedItem.toString());
			 if (selectedButton == null ) return;
			 selectedButton.setSelected(true);
			 //((JRadioButton)stringToButtons.get(newSelectedItem.toString())).setSelected(true);
			 text = newSelectedItem.toString();
			 //buttonGroup.setSelected((JRadioButton)stringToButtons.get(lastSelectedItem.toString()), true);			 //cb.setSelectedItem(newSelectedItem);
			 //cb.validate();
		  }
			 //cb.setSelectedItem(initialItem);			 return;
	  }
	 
		
		 
		
	
 }
 */
 String getSelectedButtonString () {
	 /*
 	ButtonModel button = (ButtonModel) buttonGroup.getSelection();
 	 return button.getActionCommand();
 	 */
	 return buttonGroup.getSelectionActionCommand();
 	
 }
 	public int defaultWidth() {
		return 150;
	}
	public int defaultHeight() {
		return 50;
	}
 /* // this seems wrong - perhaps it is never called  public Object getUIComponentValue() {
  	return currentModel;
  	
  	 
  }
 

  Color oldColor = Color.white;
    
  
  public void linkUIComponentToMe(Component comb) {
  	comb.addFocusListener(this);
  	  }
  
     
    //at this point  	
  public void uiComponentValueChanged() {
	//  if (textChanged()) {  //wanna make sure we don't waste invocations on object if something is done but
							//doesn't ultimately change the value of the widget		super.uiComponentValueChanged();	  //}
  }
  
    */  boolean isEditable;
  boolean textChanged() {	  if (haveSetModel) return false;
	  if (text == null) return true;	  	  return !text.equals(getSelectedButtonString());	  
  }
  
  public void setUIComponentEditable() {
		isEditable = true;
//		 ((VirtualButtonGroup) getUIComponent()).setEditable(true);
//		 ((VirtualButtonGroup) getUIComponent()).setEnabled(true);
	}

	public void setUIComponentUneditable() {
		isEditable = false;
//		 ((VirtualComboBox) getUIComponent()).setEditable(false);
//		 ((VirtualComboBox) getUIComponent()).setEnabled(false);
	}

	void resetUIComponentEditable() {
		if (!isEditable)
			setUIComponentUneditable();
		// ((VirtualComboBox) getUIComponent()).setEnabled(false);

	}  
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
  public void actionPerformed (ActionEvent e) {
  	
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
 } public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	 return buttonPanel;
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



