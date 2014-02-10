// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Enumeration;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
public class SetDefaultSynthesizedAttributesAdapterVisitor extends AdapterVisitor {	  
  public SetDefaultSynthesizedAttributesAdapterVisitor(ObjectAdapter root, boolean isTopDisplayedAdapter) {
    super(root);		root.setTopDisplayedAdapter(isTopDisplayedAdapter);
  }  

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {	   adapter.setDefaultSynthesizedAttributes();
	   return null;  }   
}
