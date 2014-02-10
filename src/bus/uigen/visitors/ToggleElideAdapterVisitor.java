// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Enumeration;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
public class ToggleElideAdapterVisitor extends AdapterVisitor {	  
  public ToggleElideAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  if (adapter != null && adapter.getGenericWidget() != null)	   //adapter.getGenericWidget().internalElide();		adapter.getGenericWidget().toggleElide();
	   return null;  }
    public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum) {	  if (adapter.getGenericWidget() == null) return null;
	 // System.out.println("visit" + adapter);	  //if (((curLevel == targetLevel) && (curNodeNum < targetNodeNum)) || !(adapter instanceof uiContainerAdapter))	   //if (curLevel < targetLevel && !adapter.isAtomic())	  if (curLevel == targetLevel)
		   adapter.getGenericWidget().toggleElide();	   
	   return null;  }  public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) {
	 // System.out.println("visit" + adapter);	   if (adapter.getGenericWidget() == null) return null;
	  //if (((curLevel == targetLevel) )|| !(adapter instanceof uiContainerAdapter))	   //if (curLevel < targetLevel && !adapter.isAtomic())	   if (curLevel == targetLevel)
		   adapter.getGenericWidget().toggleElide();
	   
	   return null;  }
}
