package bus.uigen.editors;import bus.uigen.*;
import java.awt.Color;import java.awt.Component;import java.awt.Container;import java.awt.Frame;import java.awt.event.*;
import slc.SLComposer;import slgc.SLGController;import slgv.SLGView;
import util.models.Listenable;import util.undo.Listener;import shapes.ShapesAPI;
import slm.SLModel;
import shapes.ShapeModel;
import shapes.LineModel;
import java.lang.Math;
import bus.uigen.introspect.Attribute;import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.widgets.PanelSelector;import bus.uigen.widgets.VirtualComponent;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.VirtualFrame;import bus.uigen.widgets.awt.AWTComponent;import bus.uigen.widgets.awt.AWTContainer;import bus.uigen.widgets.swing.DelegateJPanel;import bus.uigen.widgets.swt.SWTPanel;import bus.uigen.adapters.AnAWTWidgetAdapter;import bus.uigen.ars.*;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.SelectionManager;import bus.uigen.controller.menus.RightMenu;import bus.uigen.controller.menus.RightMenuManager;import java.util.Vector;
public class NestedShapesAdapter extends ShapesAdapter  {

  public String getType() {
    return "java.lang.Object";
  }  
  public void setUIComponentTypedValue(Object newval) {	  try {	  if (!(newval instanceof SLModel))		  super.setUIComponentTypedValue(new SLModel());	  else		  super.setUIComponentTypedValue(newval);	  } catch (Exception e) {		  e.printStackTrace();	  }
  }
      public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {	  if (!adapter.isNestedShapesContainer()) {		  return super.instantiateComponent(cclass, adapter);	  	  }	 	  DelegateJPanel delegateJPanel = new DelegateJPanel();	 	  VirtualComponent virtualComposer = AWTContainer.virtualContainer(delegateJPanel);	  instantiatedComponent = true;	  	  return virtualComposer;	 
  }  //  public void linkUIComponentToMe () {//	  super.linkUIComponentToMe();//	  CompositeAdapter shapesObjectAdapter =  (CompositeAdapter) getObjectAdapter();//	  if (shapesObjectAdapter.getShapeListMouseClickListener() != null) {//		  shapesObjectAdapter.registerAsMouseClickListener(getView().getShapeEventNotifier());//	  }//  }  

}


