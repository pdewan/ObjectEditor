package bus.uigen.tools;

import java.awt.Component;
import java.awt.Container;

import bus.uigen.ars.*;

public class WidgetHierarchy {
  
  private static void print(String tabs, Component component) {
    
    //System.err.println(tabs+"Component: "+component);
    System.err.println(tabs+"Class    : "+component.getClass());
    if (component instanceof Container) {
      Container container = (Container) component;
      int n = container.getComponentCount();
      for (int i=0; i<n; i++) {
	Component c = container.getComponent(i);
	System.err.println(tabs+(i+1)+":");
	print(tabs+"  ", c);
      }
    } 
  }

  public static void print(Component component) {
    print("", component);
  }
}

