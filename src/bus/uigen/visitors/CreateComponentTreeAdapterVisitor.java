// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Enumeration;
import java.util.Hashtable;

import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
// will attach the components created by addchilduicomponentvisitor
public class CreateComponentTreeAdapterVisitor extends AdapterVisitor {	  
  public CreateComponentTreeAdapterVisitor(ObjectAdapter root) {
    super(root);	//if (! root.processPreferredWidget() && root.getWidgetAdapter() != null && root.isAtomic()) {
	if (! root.getProcessPreferredWidget() && root.getWidgetAdapter() != null && root.isAtomic()) {
		    // this is also done by addChildComonents. Should delete one.
			root.getWidgetAdapter().childComponentsAdded(true);
			
	}
  }  /*  public Vector traverseNonAtomicContainers(uiObjectAdapter adapter, Vector results) {
	  if (adapter != null)  {		  results.addElement(visit(adapter));
		  if (adapter instanceof uiContainerAdapter && !adapter.isViewAtomic()) {			  results.addElement(visit(adapter));
			  Enumeration children = ((uiContainerAdapter) adapter).getChildAdapters();
			  while (children.hasMoreElements()) {
				  uiObjectAdapter child = (uiObjectAdapter) children.nextElement();
				  traverseNonAtomicContainers(child, results);
			  }
		  }	  }
	  return results;  }  */

   
  // Override this method to provide
  // specific action
  public Object visit(int from, int to,  ObjectAdapter adapter) {	  /*
	  if (adapter == root) {
		  adapter.processPreferredWidget();		  //return new Boolean (adapter.isAtomic());	  }	  */
	  if (adapter instanceof CompositeAdapter) {		boolean retVal = ((CompositeAdapter) adapter).childUIComponentsAdded();
		return new Boolean (retVal);	  }
	   return new Boolean (false);  } 
  public Object visit(ObjectAdapter adapter) {
	  /*
	  if (adapter == root) {
		  adapter.processPreferredWidget();
		  //return new Boolean (adapter.isAtomic());
	  }
	  */
	  if (adapter instanceof CompositeAdapter) {
			boolean retVal = ((CompositeAdapter) adapter).childUIComponentsAdded();
		return new Boolean (retVal);
	  }
	   return new Boolean (false);
  } 
  public Object visit(ObjectAdapter adapter, Hashtable ignorePs) {
	  /*
	  if (adapter == root) {
		  adapter.processPreferredWidget();
		  //return new Boolean (adapter.isAtomic());
	  }
	  */
	  if (adapter instanceof CompositeAdapter) {
			boolean retVal = ((CompositeAdapter) adapter).childUIComponentsAdded();
		return new Boolean (retVal);
	  }
	   return new Boolean (false);
}  
}
