// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
import java.util.Enumeration;import java.util.Vector;

public class AddSelfAdapterVisitor extends DisplayOrderAdapterVisitor {

  public AddSelfAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  return adapter;  }  
}
