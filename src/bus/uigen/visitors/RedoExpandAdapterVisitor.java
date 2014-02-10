// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;
import java.util.Enumeration;

public class RedoExpandAdapterVisitor extends AdapterVisitor {

  public RedoExpandAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	   if (adapter.getGenericWidget() == null) return null;	   adapter.getGenericWidget().redoExpand();
	   return null;  }
  
}
