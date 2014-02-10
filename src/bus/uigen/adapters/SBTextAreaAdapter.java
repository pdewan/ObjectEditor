package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.TextAreaSelector;
import bus.uigen.widgets.VirtualComponent;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;


public class SBTextAreaAdapter	extends SBTextComponentAdapter {	
//implements DocumentListener, ActionListener, FocusListener {
  
  public String getType() {
    return "java.lang.StringBuffer";
  }
  
  // Implementation of 
  // abstract methods
    public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {	  //jtf = new JTextArea("");
	  jtf = TextAreaSelector.createTextArea();
	  instantiatedComponent = true;
	  jtf.setName(adapter.toText());	  return jtf;
  }
   
}



