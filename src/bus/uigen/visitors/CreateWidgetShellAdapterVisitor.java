// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Enumeration;
import java.util.Hashtable;

import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.view.WidgetShell;
import bus.uigen.view.WidgetShellSelector;
public class CreateWidgetShellAdapterVisitor extends AdapterVisitor {	  
  public CreateWidgetShellAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  if (adapter.getCreateWidgetShell() && adapter.getWidgetShell() == null && !adapter.hasOnlyGraphicsDescendents()) {		  WidgetShell widgetShell = WidgetShellSelector.createWidgetShell(adapter);
		  widgetShell.getContainer().setName("Widget Shell: " + adapter + " CreateWidgetShellAdapterVisitor");
//		  widgetShell.getContainer().setBackground(adapter.getContainerBackground());
		  adapter.setWidgetShell(WidgetShellSelector.createWidgetShell(adapter));
	  }
	   return null;  } 
  
  boolean traverseContainerAdapter (CompositeAdapter adapter) {
	  return !adapter.isAtomic() && !adapter.hasOnlyGraphicsDescendents();
  }
  
  public Object visit(ObjectAdapter adapter, Hashtable ignorePs) {
  	   return visit(adapter);
  	   /*
	   adapter.setWidgetShell(WidgetShellSelector.createWidgetShell(adapter));
	   return null;
	   */
} 
  
}
