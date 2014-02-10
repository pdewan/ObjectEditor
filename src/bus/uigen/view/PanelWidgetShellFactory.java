package bus.uigen.view;import bus.uigen.oadapters.*;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.awt.AWTPanelFactory;import javax.swing.*;import java.awt.Frame;
import bus.uigen.uiFrame;import java.util.Vector;import bus.uigen.ars.*;import bus.uigen.controller.menus.MenuSetter;
public class PanelWidgetShellFactory extends  DefaultWidgetShellFactory {

  public VirtualContainer createContainer() {	  /*
	  Container retVal = new Panel();
	  retVal.setBackground(Color.white);	  //return new Panel();	  return retVal;	  */	  return (new AWTPanelFactory()).createPanel();
  }  
 }
