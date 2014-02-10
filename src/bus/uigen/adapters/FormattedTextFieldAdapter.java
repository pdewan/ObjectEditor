package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.FormattedTextFieldSelector;
import bus.uigen.widgets.TextFieldSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualFormattedTextField;
import bus.uigen.widgets.VirtualTextField;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import util.trace.Tracer;

import java.awt.event.*;

import bus.uigen.ars.*;
import bus.uigen.introspect.Attribute;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ObjectAdapter;


//public class uiJTextFieldAdapter extends uiWidgetAdapter public class FormattedTextFieldAdapter extends TextFieldAdapter {
//implements DocumentListener, ActionListener, FocusListener, KeyListener {
  
 
	  public VirtualComponent instantiateComponent(Class cclass, ObjectAdapter adapter) {
	  
	  if (jtf != null) return jtf;
	  jtf = FormattedTextFieldSelector.createFormattedTextField("");
	  jtf.setName(adapter.toText());
	  instantiatedComponent = true;
	  linked = false;
	  
  	return jtf;
      
  }  public String getType() {
	    return "java.lang.Object";
	  }
 
  public void setUIComponentTypedValue(Object newval) {
    //super.setUIComponentTypedValue(newval);
    getFormattedTextField().setValue(newval);
    text = getFormattedTextField().getText();
    if (!isEditable)
		  this.setUIComponentUneditable();
  }
 
  /*
  public boolean processAttribute(Attribute attrib) {
	  return super.processAttribute(attrib);
	  
      
  }
  public void linkUIComponentToMe() {
	  if (linked) return;
	  super.linkUIComponentToMe();
      ((VirtualTextField) jtf).addActionListener(this);
        }
        */
  public void linkUIComponentToMe(VirtualComponent t) {
	  //System.out.println("linking textfield");
	  if (!(t instanceof VirtualFormattedTextField)) {
		  Tracer.error("Wrong class of formatted text field: " + t.getClass().getName());
		  return;
	  }
		  
  	super.linkUIComponentToMe(t);
  	//int numColumns = ((Integer) getObjectAdapter().getMergedAttributeValue(AttributeNames.TEXT_FIELD_LENGTH)).intValue();
  	int numColumns = getObjectAdapter().getTextFieldLength();
  	((VirtualTextField) jtf).setColumns(numColumns);
  
  }
  public Object getUIComponentValue() {
	    //String val = new String("");
	    try {
	     // jtf = (JTextField) getUIComponent();
	      return getFormattedTextField().getValue();
	    } catch (ClassCastException e) {
	    	e.printStackTrace();

	    	return null;
	    }
	  }
   VirtualFormattedTextField getFormattedTextField() {
	   return (VirtualFormattedTextField) getUIComponent();
   }
  
}



