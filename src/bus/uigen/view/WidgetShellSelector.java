package bus.uigen.view;import bus.uigen.oadapters.*;import bus.uigen.widgets.VirtualContainer;import javax.swing.*;import java.awt.Frame;
import bus.uigen.uiFrame;import java.util.Hashtable;import java.util.Vector;import bus.uigen.ars.*;import bus.uigen.controller.menus.MenuSetter;public class WidgetShellSelector  {	static WidgetShellFactory factory = new JPanelWidgetShellFactory();	public static void setWidgetShellFactory (WidgetShellFactory newValue) {		factory = newValue;
			}
	public static WidgetShell createWidgetShell  () {		return factory.createWidgetShell ();
	}	public static WidgetShell createWidgetShell (VirtualContainer container, ObjectAdapter objectAdapter) {		return factory.createWidgetShell(container, objectAdapter);
	}	public static WidgetShell createWidgetShell (ObjectAdapter objectAdapter) {		return factory.createWidgetShell (objectAdapter);
	}	/*	public static uiGenericWidget createWidgetShell (uiObjectAdapter objectAdapter, Hashtable ignorePs) {		return factory.createWidgetShell (objectAdapter, ignorePs);	}	*/	public static WidgetShell createWidgetShell (VirtualContainer container) {		return factory.createWidgetShell (container);
	}  	
}
