// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;
import java.util.Enumeration;

public class IsSerializableAdapterVisitor extends AdapterVisitor {

  public IsSerializableAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {	   //adapter.getGenericWidget().internalElide();
	   return new Boolean( adapter.getRealObject() instanceof java.io.Serializable);  }
  
}
