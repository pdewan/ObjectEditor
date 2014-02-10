package bus.uigen.view;import bus.uigen.oadapters.*;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.swing.SwingPanelFactory;import javax.swing.*;import java.awt.Frame;
import bus.uigen.uiFrame;import java.util.Vector;import bus.uigen.ars.*;import bus.uigen.controller.menus.MenuSetter;
public class JPanelWidgetShellFactory extends  DefaultWidgetShellFactory {

  public VirtualContainer createContainer() {	  /*
	  Container retVal = new JPanel();
	  retVal.setBackground(Color.white);	  //return new Panel();	  return retVal;	  //return new JPanel();
	  */	  return (new SwingPanelFactory()).createPanel();
  }  
 }
