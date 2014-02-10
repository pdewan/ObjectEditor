package bus.uigen.adapters;
import bus.uigen.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;

import bus.uigen.ars.*;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.TextAreaSelector;
import bus.uigen.widgets.VirtualComponent;


public class MSTextAreaAdapter	extends MSTextComponentAdapter {	
//implements DocumentListener, ActionListener, FocusListener {
  
  public String getType() {
    return "bus.uigen.AMutableString";
  }
  
  // Implementation of 
  // abstract methods
    public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {	  //jtf = new JTextArea("", 1, 20);
	  jtf = TextAreaSelector.createTextArea ("", 1, 20);
	  instantiatedComponent = true;	  return jtf;
  }
   
}



