package bus.uigen.view;import bus.uigen.oadapters.*;import bus.uigen.widgets.PanelSelector;import bus.uigen.widgets.VirtualContainer;import javax.swing.*;import java.awt.Frame;
import bus.uigen.uiFrame;import java.util.Vector;import bus.uigen.ars.*;import bus.uigen.controller.menus.MenuSetter;
public  class DefaultWidgetShellFactory implements  WidgetShellFactory {
public WidgetShell createWidgetShell  () {	return createWidgetShell ((ObjectAdapter) null);
}  public WidgetShell createWidgetShell (VirtualContainer container, ObjectAdapter objectAdapter) {	  WidgetShell widgetShell = new AGenericWidgetShell();	  widgetShell.setObjectAdapter (objectAdapter);	  	  widgetShell.init(container);	  return widgetShell;
  }  public WidgetShell createWidgetShell (ObjectAdapter objectAdapter) {//	  System.out.println("**** creating container");	  VirtualContainer container = createContainer();//	  System.out.println("**** created container");	  container.setName("WidgetShell Container " + objectAdapter.toDebugText() + "( DefaultWidgetShellFactory.createWidgetShell)");	  //return createWidgetShell (createContainer(), objectAdapter);	  return createWidgetShell (container, objectAdapter);
  }  public WidgetShell createWidgetShell (VirtualContainer container) {	   return createWidgetShell (container, null);
  }
  public  VirtualContainer createContainer() {	  return PanelSelector.createPanel();
  }
    
 }
