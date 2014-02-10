// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
public class ElideAdapterVisitor extends AdapterVisitor {	  
  public ElideAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {	   adapter.getGenericWidget().internalElide();
	   return null;  }
    public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum) {	  if (adapter.getGenericWidget() == null) return null;
	  ObjectAdapter parentAdapter = adapter.getParentAdapter();
	  if (parentAdapter != null && parentAdapter.getExpandPrimitiveChildren())
		  return null;
	 // System.out.println("visit" + adapter);	   //if ( adapter.getDefaultExpanded() && ((curLevel < targetLevel) && (curNodeNum < targetNodeNum)) || !(adapter instanceof uiContainerAdapter))	   if ( adapter.getDefaultExpanded() && ((curLevel < targetLevel) && (curNodeNum < targetNodeNum)))
	   //if (curLevel < targetLevel && !adapter.isAtomic())
		   adapter.getGenericWidget().internalExpand();
	   else	       adapter.getGenericWidget().internalElide();
	   return null;  }  public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) {
	 // System.out.println("visit" + adapter);	   if (adapter.getGenericWidget() == null) return null;
	  // if (adapter.hasOnlyGraphicsDescendents()) return null;
	  if (/*!adapter.unparseAsToString() &&*/ (((curLevel < targetLevel) )|| !(adapter instanceof CompositeAdapter)))	   //if (curLevel < targetLevel && !adapter.isAtomic())
		   adapter.getGenericWidget().internalExpand();
	   else	       adapter.getGenericWidget().internalElide();
	   return null;  }


 
  
}
