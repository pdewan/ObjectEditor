package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.TextFieldSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualTextField;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;

import bus.uigen.ars.*;
import bus.uigen.introspect.Attribute;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;


//public class uiJTextFieldAdapter extends uiWidgetAdapter public class TextFieldAdapter extends TextComponentAdapter {
//implements DocumentListener, ActionListener, FocusListener, KeyListener {
  
 /*
  public uiJTextFieldAdapter() {
  }	
  
  // Implementation of 
  // abstract methods

  public String getType() {
    return "java.lang.String";
  }
  //JTextField jtf;  JTextComponent jtf;
  String text;
	*/
	  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
	  //System.out.println("instantiating jtextfield");	  //jtf = new JTextField("", NUM_COLUMNS);'	  
	  //jtf = new JTextField("");
	  if (jtf != null) return jtf;
	  instantiatedComponent = true;
	  jtf = TextFieldSelector.createTextField("");
	  jtf.setName("Component " + adapter.toText() + " uiJTextFieldAdapter.createTextField");
	  linked = false;
	  if (adapter.isScrolled()) {
		  spane = ScrollPaneSelector.createScrollPane();		  
		  spane.setScrolledComponent(jtf);
		  //spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		  return spane;
	  }
	  
	  
	  /*
	  int numColumns = getObjectAdapter().getTextFieldLength();
	  	if (numColumns != 0)
	  	((VirtualTextField) jtf).setColumns(numColumns);
	  	else {
	  		super.setWidth(jtf);
	  		
	  	}
	  	*/
	  //Misc.setHeight(jtf, Misc.SWING_DEFAULT_HEIGHT);
	  //else
		  //jtf.setText("");
	  //((JTextField) jtf).setColumns(100);
  	//int numColumns = ((Integer) this.getObjectAdapter().getMergedAttributeValue(AttributeNames.NUM_COLUMNS)).intValue();  	//jtf = new JTextField("", numColumns);
  	return jtf;
      //return new JTextField("", NUM_COLUMNS);
  }  
 
  public void setUIComponentTypedValue(Object newval) {
    super.setUIComponentTypedValue(newval);
	//JTextField jtf1 = (JTextField) jtf;
	VirtualTextField jtf1 = (VirtualTextField) jtf;	//jtf1.setColumns(Math.max(jtf1.getColumns(), numColumns ((String) newval)));	//jtf1.validate();
  }
//  boolean spuriousFcousHasHappenned; // a spurious focus event seems to be generated
//  public void focusGained(FocusEvent e) {
//	   //System.out.println("focus gained");
//	  boolean editable = jtf.isEditable();	 
//	  if (!spuriousFcousHasHappenned) {
//		  spuriousFcousHasHappenned = true;	 
//		  if (editable) {
//			  jtf.setEditable(false);
//			  jtf.setEditable(true);			  
//		  }
//		  
//		 return;
//	  }
//	  super.focusGained(e);
//	  
// }
 /*
  public Object getUIComponentValue() {
    String val = new String("");
    try {
     // jtf = (JTextField) getUIComponent();
      val = jtf.getText();
    } catch (ClassCastException e) {
    }
    return val;
  }
  
  public void setUIComponentEditable() {
    ((JTextComponent) getUIComponent()).setEditable(true);
  }
  
  public void setUIComponentUneditable() {
    ((JTextComponent) getUIComponent()).setEditable(false);
  }
  
 
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
  }
  public void linkUIComponentToMe(Component t) {	  //System.out.println("linking textfield");
    if (t instanceof JTextComponent) {
     jtf = (JTextComponent) t;
      PlainDocument document = new PlainDocument();
      jtf.setDocument(document);
      document.addDocumentListener(this);
      jtf.addActionListener(this);	  jtf.addKeyListener(this);
      jtf.addFocusListener(this);
    }
  }
  uiObjectAdapter adapter;
  uiGenericWidget genericWidget;
  public void uiComponentValueChanged() {
	  	  if (textChanged()) {		super.uiComponentValueChanged();	  }
		
  }
  public void uiComponentValueEdited() {
	  	  //if (textChanged())		super.uiComponentValueEdited();		
  }
  boolean textChanged() {
	  if (text == null) return true;	  	  return !text.equals(jtf.getText());	  
  }  
  
  // DocumentListener interface
  public void changedUpdate(DocumentEvent e) {
    if (!actionMode)
      uiComponentValueChanged();
  }  boolean firstTime = true;
  public void insertUpdate(DocumentEvent e) {	  if (!actionMode)
      uiComponentValueChanged();
  }

  public void removeUpdate(DocumentEvent e) {
    	  if (!actionMode)
      uiComponentValueChanged();
  }
  boolean actionPerformed = false;
  public void actionPerformed(ActionEvent e) {
    if (actionMode) {		actionPerformed = true;
       uiComponentValueChanged();
    } 
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
      //awtComponent.setBackground(Color.white);
    }
  }  public void keyReleased(KeyEvent k) {  }  public void keyPressed(KeyEvent k) {  }  public void keyTyped (KeyEvent k) {	  if (actionPerformed)		  actionPerformed = false;	  else
	      super.uiComponentValueEdited();	  }  */
  
  public boolean processAttribute(Attribute attrib) {
	  return super.processAttribute(attrib);
	  /*
    if (attrib.getName().equals(AttributeNames.TEXT_FIELD_LENGTH)) {
    	//JTextField jtf1 = (JTextField) jtf;
    	VirtualTextField jtf1 = (VirtualTextField) jtf;
    	jtf1.setColumns(((Integer)attrib.getValue()).intValue()); 
    	return true;
    } else 
    	return super.processAttribute(attrib);
    	*/
      
  }
  public void linkUIComponentToMe() {
	  if (linked) return;
	  super.linkUIComponentToMe();
      ((VirtualTextField) jtf).addActionListener(this);
        }
  public void linkUIComponentToMe(VirtualComponent t) {
	  //System.out.println("linking textfield");
	  if (t == jtf || t == spane) {
		  	int numColumns = getObjectAdapter().getTextFieldLength();
		  	if (numColumns != 0)
		  	((VirtualTextField) jtf).setColumns(numColumns);
		  	else {
		  		if (t == spane) {
		  			setSize(spane);
				  	setColors(spane);
				  	super.linkUIComponentToMe(jtf);
				  	return;
		  		} else
		  		super.setAttributes(jtf);
		  		
		  	}
	  }
  	super.linkUIComponentToMe(t);
  	//int numColumns = ((Integer) getObjectAdapter().getMergedAttributeValue(AttributeNames.TEXT_FIELD_LENGTH)).intValue();
  	
  
  	/*
  	int width = adapter.getComponentWidth();
  	if (width == 0) {
  	int numColumns = getObjectAdapter().getTextFieldLength();
  	if (numColumns != 0)
  	((VirtualTextField) jtf).setColumns(numColumns);
  	}
  	else
  		Misc.setWidth(jtf, width);
  		*/
  
  }

  
}



