package bus.uigen.adapters;
import bus.uigen.ars.*;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.*;//import bus.uigen.widgets.*;

import javax.swing.*;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.ComboBoxSelector;
import bus.uigen.widgets.VirtualComboBox;
import bus.uigen.widgets.VirtualComponent;

import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Vector;

//public class uiJTextFieldAdapter extends uiWidgetAdapter public class PropAltWidgetAdapter extends WidgetAdapterimplements ItemListener, FocusListener, KeyListener { //uiJTextComponentAdapter {

  
	
 	
  VirtualComboBox cb = null;
  boolean filledCB = false;  String text;  public String getType() {
    return "java.lang.String";
  }
  
    public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {/*
	  System.out.println("instantiating jcombobox for property" + getObjectAdapter().getPropertyName());	  //Enumeration propertyList = uiBean.getAllPropertyNames(getObjectAdapter().getRealObject());  //assume getRealObject is what we need	  	  Vector defaultStrings = uiBean.getPropertyAlternatives(getObjectAdapter().getParentAdapter().getRealObject(),getObjectAdapter().getPropertyName());	  //assume getting parent will return the class that creates this property. and hence where	  //we can call the method <propertyName>Alternatives
	  	  if (defaultStrings == null) {  //if no properties		  System.out.println("no alternatives");
		  cb = new JComboBox();
		  cb.setEditable(true);		  return cb;
	  }
	  
	  //else there are some so fill
	  
	  System.out.println("found alt strings" + defaultStrings.size());	  cb = new JComboBox(defaultStrings);	  */
	  System.err.println("instantiating  jcombo");	  //cb = new JComboBox();
	  cb = ComboBoxSelector.createComboBox();
	  instantiatedComponent = true;

	  cb.setEditable(true);
	  //cb.enable();
	  cb.setEnabled(true);	  
	  	  return cb;
  }
  
  public VirtualComponent getUIComponent() {
	  return cb;
  }   public void setUIComponentTypedValue(Object newval) {
//	 System.out.println("in setuictv");
	 String val;
	 VirtualComponent c = getUIComponent();	 
	 if (c instanceof JComboBox) {
				if (!filledCB) {
			 try {				

					 String[] defaultStringsArray = IntrospectUtility.getPropertyAlternatives(getObjectAdapter().getParentAdapter().getRealObject(),getObjectAdapter().getPropertyName());					//assume getting parent will return the class that creates this property. and hence where					//we can call the method <propertyName>Alternatives
					 Vector defaultStrings = new Vector();
					 for (int i = 0; i < defaultStringsArray.length; i++) {
					 	defaultStrings.addElement(defaultStringsArray[i]);
					 }					
					 if (defaultStrings != null) {  //if alternatives
						
											  System.err.println("alternatives found " + defaultStrings.size() );					  for (int j = 0;j < defaultStrings.size(); j++) { 
						  cb.addItem(new String((String)defaultStrings.elementAt(j)));
						  System.out.println((String)defaultStrings.elementAt(j));
					  }
					  					  System.err.println("and added" + " " + cb.getItemCount());
					  cb.setEditable(true);
					  
						
	//					JComboBox newCB = new JComboBox(defaultStrings);	//					cb = newCB;						//cb.enable(true);
						cb.setEnabled(true);
						cb.invalidate();						cb.validate();
												
											}//endifnull					filledCB = true;
						
				 
							 }
			 catch (Exception fcb) {System.out.println("can't fill");}
		}			 
		 
		 		try {
				if (newval != null) {
					val = new String((String)newval);
					System.out.println("adding component value to  " + val);					
					if (val != null)
						cb.addItem(val);  //should add and select (make visible the item)
					else
						cb.addItem(new String(""));  //should add and select (make visible the item)
													}				else {					val = "";
					cb.addItem(new String(""));  //should add and select (make visible the item)
				}				System.out.println("and added" + " " + cb.getItemCount());
				text = val;
				System.out.println("setted");
			}				catch (ClassCastException e) {							 										 System.out.println("exception in setUicomp");
		}	 
			
	 }
	
 }  public Object getUIComponentValue() {
	  VirtualComponent c = getUIComponent();	  String val = new String("");
	  if (c instanceof JComboBox) {
		
		try {
		 
			val = new String((String)cb.getSelectedItem());		  
		} 		catch (ClassCastException e) {			System.err.println("exeception in getUIcom");		}	  }
		return val;
  }
  
  public void setUIComponentEditable() {
	  cb.setEditable(true);
  }
  
  public void setUIComponentUneditable() {
	  cb.setEditable(false); }
/*
  Color oldColor = Color.white;
  
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
  }  */
  
  public void linkUIComponentToMe() {
    //if (comb instanceof JComboBox) {
  	  cb.addItemListener(this);
      cb.addFocusListener(this);
	  
	//}  }
  public void linkUIComponentToMe(VirtualComponent comb) {
	    cb = (VirtualComboBox) comb;
	    linkUIComponentToMe();
  }
     
    //at this point  /*
  uiObjectAdapter adapter;
  uiGenericWidget genericWidget;	*/	
  public boolean uiComponentValueChanged() {
	  if (textChanged()) {  //wanna make sure we don't waste invocations on object if something is done but
							//doesn't ultimately change the value of the widget		return super.uiComponentValueChanged();	  }
	  return false;
  }  /*
  public void uiComponentValueEdited() {
	  //if (textChanged())		super.uiComponentValueEdited();	
  }*/  
  boolean textChanged() {
	  if (text == null) return true;	  	  return !text.equals((String)(cb.getSelectedItem()));	  
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
  public void keyReleased(KeyEvent k) {  }  public void keyPressed(KeyEvent k) {  }
  
    public void keyTyped (KeyEvent k) {	  
	  /*	  if (actionPerformed)		  actionPerformed = false;	  else
	      super.uiComponentValueEdited();	
	  */  }
  
  public void itemStateChanged(ItemEvent ev) {    System.err.println("itemstatechangeEvent " + cb.isEditable());
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
  }
  }



