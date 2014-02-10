// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.*;
import java.util.Enumeration;

public class HasUncreatedChildrenVisitor extends AdapterVisitor {

  public  HasUncreatedChildrenVisitor (ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {	   //adapter.getGenericWidget().internalElide();	  //if (adapter instanceof uiContainerAdapter && !adapter.isLeaf() && adapter.getViewObject() != null) {	  if (adapter instanceof CompositeAdapter && !adapter.isAtomic() && adapter.computeAndMaybeSetViewObject() != null) {
		  return new Boolean( !((CompositeAdapter) adapter).childrenCreated());	  } else return new Boolean(false);  }
  
}
