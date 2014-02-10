// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;
import java.util.Enumeration;

public class UpdateAdapterVisitor extends AdapterVisitor {

  public UpdateAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  if (adapter.isEdited()) {
		  adapter.uiComponentValueChanged();		  return adapter;	  }
	  return null;  }
    
}
