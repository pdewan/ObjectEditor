package bus.uigen.adapters;
import bus.uigen.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;

import bus.uigen.ars.*;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.TextFieldSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualTextField;


public class MSTextFieldAdapter	extends MSTextComponentAdapter {	
//implements DocumentListener, ActionListener, FocusListener {
  
  public String getType() {
    return "bus.uigen.AMutableString";
  }
  
  // Implementation of 
  // abstract methods
    public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {	  //jtf = new JTextField("");
	  jtf = TextFieldSelector.createTextField("");
	  instantiatedComponent = true;
	  return jtf;
  }
  public boolean processAttribute(Attribute attrib) {
	    if (attrib.getName().equals(AttributeNames.TEXT_FIELD_LENGTH)) {
	    	JTextField jtf1 = (JTextField) jtf;
	    	jtf1.setColumns(((Integer)attrib.getValue()).intValue()); 
	    	return true;
	    } else 
	    	return super.processAttribute(attrib);
	      
	  }
  public void linkUIComponentToMe(VirtualComponent t) {
	  //System.out.println("linking textfield");
  	super.linkUIComponentToMe(t);
  	((VirtualTextField) jtf).addActionListener(this);
  	int numColumns = getObjectAdapter().getTextFieldLength();
  	//int numColumns = ((Integer) getObjectAdapter().getMergedAttributeValue(AttributeNames.TEXT_FIELD_LENGTH)).intValue();
  	if (numColumns > 0)
  	((VirtualTextField) jtf).setColumns(numColumns);
  	else  		
	   super.setAttributes(jtf);
		  	
  
  }
  public void removeAll() {
	  mutatingString = true;
	   jtf.setText("");
	   mutatingString = false;
  } 
}



