package bus.uigen.adapters;
import bus.uigen.*;//import bus.uigen.widgets.*;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.ButtonSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.swing.SwingPanelFactory;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;import java.lang.reflect.Field;import javax.swing.ComboBoxModel;import javax.swing.event.ListDataListener;import javax.swing.event.ListDataEvent;
import javax.swing.table.TableCellEditor;import java.lang.reflect.*;
import bus.uigen.oadapters.EnumerationAdapter;
import java.util.EventObject;//import bus.uigen.introspect.uiBean;import bus.uigen.ars.*;
import bus.uigen.controller.MethodInvocationManager;
import javax.swing.event.ListDataEvent;
import bus.uigen.oadapters.ObjectAdapter;

//public class uiJTextFieldAdapter extends uiWidgetAdapter public class ButtonPanelAdapter extends AbstractButtonPanelAdapter { //uiJTextComponentAdapter {

  VirtualButton button;
  public void linkUIComponentToMe (VirtualComponent c) {
	  if (c == null) return;
	  button = (VirtualButton) c;
	  linkUIComponentToMe();
  }
  public void linkUIComponentToMe (/*VirtualComponent comb*/) {
  	  
	  button.addFocusListener(this);
  
  }
 
  public VirtualComponent getUIComponent() {
	  return button;
  }
  VirtualButton createButton (String itemString) {
  	//return new JButton(itemString);
	  button = ButtonSelector.createButton(itemString);
	  return button;
	  //return ButtonSelector.createButton(itemString);
  	
  }
  

 
  }



