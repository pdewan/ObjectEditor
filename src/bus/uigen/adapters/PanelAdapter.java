package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.oadapters.*;
import javax.swing.*;

import java.awt.Container;
import java.awt.event.*;
import java.util.*;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.awt.AWTComponent;
import bus.uigen.widgets.awt.AWTContainer;
import bus.uigen.widgets.swing.SwingPanelFactory;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
public class PanelAdapter extends WidgetAdapter 
implements FocusListener {
	
  public PanelAdapter() {
  }

  public void setUIComponentTypedValue(Object newval) {	  //System.out.println("nothing done in setUIComponentTypedValue");
  }
  
  public Object getUIComponentValue() {
    return "";
  }

  public void setUIComponentEditable() {
  }
  
  public void setUIComponentUneditable() {
  }  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
     //return AnAWTContainer.virtualContainer ((new SwingPanelFactory()).createPanel());
	  	  // this was commented out earlier
	  VirtualComponent panel = PanelSelector.createPanel();
	  instantiatedComponent = true;
	  panel.setName(adapter.toText());
	  return panel;
  }
  
  public void setUIComponentSelected() {	  //System.out.println("selected");
	  	super.setUIComponentSelected(); 
    //uiContainerAdapter adaptor =  (uiContainerAdapter) getObjectAdapter();	
    ObjectAdapter adaptor =   getObjectAdapter();
	//System.out.println("selected " + adaptor);	//if (adaptor instanceof uiNullAdapter) return;	if (!(adaptor instanceof CompositeAdapter)) return;
    //Enumeration children = (uiObjectAdaptor) adaptor.getChildAdapters();
    Enumeration children = ((CompositeAdapter) adaptor).getChildAdapters();	while (children.hasMoreElements()) {
      ObjectAdapter a = (ObjectAdapter) children.nextElement();	  if (a.getWidgetAdapter() != null)
      a.getWidgetAdapter().setUIComponentSelected();
    }
  }
  
  public void setUIComponentDeselected() {	  	super.setUIComponentDeselected();	ObjectAdapter adaptor =   getObjectAdapter();	//if (adaptor instanceof uiNullAdapter) return;	if (adaptor == null) return;	if (!(adaptor instanceof CompositeAdapter)) return;
    //uiContainerAdapter adaptor =  (uiContainerAdapter) getObjectAdapter();
    Enumeration children = ((CompositeAdapter) adaptor).getChildAdapters();	//Enumeration children = adaptor.getChildAdapters();
    while (children.hasMoreElements()) {
      ObjectAdapter a = (ObjectAdapter) children.nextElement();	  if (a.getWidgetAdapter() != null)
      a.getWidgetAdapter().setUIComponentDeselected();
    } 
  }    //JPanel panel;  VirtualContainer panel;
  
  public void linkUIComponentToMe(VirtualComponent c) {	  
    //if (c instanceof JPanel) {	 	//if (c instanceof AnAWTContainer) { 
      //panel = (VirtualContainer) ((AnAWTContainer) c).getAWTComponent();
	  panel = (VirtualContainer) c;
      linkUIComponentToMe();
      //panel.addFocusListener(this);
    //}	  
  }
  public void linkUIComponentToMe() {
	  
	    //if (c instanceof JPanel) {	 
		//if (c instanceof AnAWTContainer) { 
	      //panel = (Container) ((AnAWTContainer) c).getAWTComponent();
	      panel.addFocusListener(this);
	      getObjectAdapter().getUIFrame().addKeyListener(panel);
//	      setColor();
	      
	    //}
		  
	  }
  
  public void setColor() {
	  
  }
  
  public VirtualComponent getUIComponent() {
	  return panel;
  }
  
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
  }  public void setUIComponentContainer(Container parent) {
	  panel.setBackground(parent.getBackground());  }

}
