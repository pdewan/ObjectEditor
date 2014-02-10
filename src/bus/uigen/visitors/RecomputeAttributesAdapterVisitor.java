// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Enumeration;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.reflect.ClassProxy;
public class RecomputeAttributesAdapterVisitor extends AdapterVisitor {	  ClassProxy classProxy;
  public RecomputeAttributesAdapterVisitor(ObjectAdapter root) {
    super(root);
  }
   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  if (adapter != null)
		  adapter.recomputeAttributes();
	   return null;  }
    public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum) {	  if (adapter == null) return null;	  if (curLevel == targetLevel)
		   adapter.recomputeAttributes();	   
	   return null;  }  public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) {
	 if (adapter == null) return null;	   if (curLevel == targetLevel)
		   adapter.recomputeAttributes();
	   
	   return null;  }
}
