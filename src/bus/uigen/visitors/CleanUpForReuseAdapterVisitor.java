// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Enumeration;
import bus.uigen.oadapters.ObjectAdapter;
public class CleanUpForReuseAdapterVisitor extends AdapterVisitor {

  public CleanUpForReuseAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {	  	   adapter.cleanUpForReuse();
	   return null;  }
  
}
