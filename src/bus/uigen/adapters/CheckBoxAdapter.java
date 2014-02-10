package bus.uigen.adapters;
import bus.uigen.*;import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.CheckBoxSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualCheckBox;
import bus.uigen.widgets.VirtualSlider;
import bus.uigen.widgets.VirtualTextField;

import javax.swing.JCheckBox;

import util.trace.Tracer;

import java.awt.Component;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class CheckBoxAdapter extends WidgetAdapter implements ItemListener{

 
  public CheckBoxAdapter() {
  }
  
  // Implementation of 
  // abstract methods
  public String getType() {
     return "java.lang.String";
  }
  boolean curVal;
  VirtualCheckBox cb;
  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
	    cb = CheckBoxSelector.createCheckBox();
	    cb.setName("Component " + adapter + " uiJcheckBoxAdapter.instantiateComponent");
		  instantiatedComponent = true;
		  Boolean  isLabelled = (Boolean) adapter.getMergedAttributeValue(AttributeNames.LABELLED);
//		  if (!adapter.getCreateWidgetShell() && adapter.isLabelled())
		  if (/*!adapter.getCreateWidgetShell() &&*/ (isLabelled == null || isLabelled))

			  
			  cb.setLabel(adapter.getLabelWithoutSuffix());

	    return cb;
	  }
  public VirtualComponent getUIComponent() {
	  return cb;
  }
  public void setUIComponentTypedValue(Object newval) {	  //System.out.println("newVal" + newval);
    try {
		boolean val;		if (newval == null) val = false;		else val = Boolean.valueOf((String) newval).booleanValue();	  //System.out.println("new val" + newval);
      //boolean val = Boolean.valueOf((String) newval).booleanValue();	  		  curVal = val;		  	  //System.out.println("curVal" + curVal);	  
      //JCheckBox cb = (JCheckBox) getUIComponent();
      //cb.setLabel("label");
      //cb.setText("text");
	  if (cb.isSelected() != val) {		  //System.out.println ("Setting checkbox" + val);
	cb.setSelected(val);
	  //System.out.println ("finsished checkbox" + val);	  }
    } catch (ClassCastException e) {
    	Tracer.error("Checkbox model cannot be: " + newval.getClass().getSimpleName());
    	return;
    }
  }
 
  public Object getUIComponentValue() {
    String val = new String("");
    try {
      //JCheckBox cb = (JCheckBox) getUIComponent();
      val = (new Boolean(cb.isSelected())).toString();
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
    return val;
  }
  
  public void setUIComponentEditable() {
    try {
      //JCheckBox cb = (JCheckBox) getUIComponent();
      cb.setEnabled(true);
    } catch (ClassCastException e) {
    }
  }
  
  public void setUIComponentUneditable() {
    try {
      //JCheckBox cb = (JCheckBox) getUIComponent();
      cb.setEnabled(false);
    } catch (ClassCastException e) {
    }
  }
/*
 
  Color oldColor = Color.white;
  public void setUIComponentSelected() {	  super.setUIComponentSelected();
    if (getUIComponent().getBackground() != SelectionColorSelector.getColor())
      oldColor = getUIComponent().getBackground();
    getUIComponent().setBackground(SelectionColorSelector.getColor());
  }
  
  public void setUIComponentDeselected() {	  super.setUIComponentDeselected();
   getUIComponent().setBackground(oldColor);
  }
  */
  public void linkUIComponentToMe() {
   //if (c instanceof JCheckBox) {
      //JCheckBox cb = (JCheckBox) c;
      cb.addItemListener(this);
    //}
  }
  public void linkUIComponentToMe(VirtualComponent c) {
	  if (c == cb) {
		  	
		  		super.setAttributes(c);
		  	
	  }
	  cb = (VirtualCheckBox) c;
	  linkUIComponentToMe();
	   //if (c instanceof JCheckBox) {
	      //JCheckBox cb = (JCheckBox) c;
	      //cb.addItemListener(this);
	    //}
	  }

  public void itemStateChanged(ItemEvent evt) {
	  //System.out.println("curVal" + curVal);	  //System.out.println("item state changed" + ((JCheckBox) evt.getSource()).isSelected());
	  if (curVal != ((JCheckBox) evt.getSource()).isSelected()) {
	  	 JCheckBox checkBox = (JCheckBox) evt.getSource();
	  	 //checkBox.setText("text");
	  	 //checkBox.setLabel("label");	     uiComponentValueChanged();
		 curVal = ((JCheckBox) evt.getSource()).isSelected();	  }
  }

}




