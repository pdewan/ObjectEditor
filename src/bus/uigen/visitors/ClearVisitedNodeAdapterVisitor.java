// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Hashtable;

import util.trace.uigen.TooManyLevelsInDisplayedLogicalStructure;
import util.trace.uigen.TooManyNodesInDisplayedLogicalStructure;
import bus.uigen.uiGenerator;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
public class ClearVisitedNodeAdapterVisitor extends AdapterVisitor {	  
  public ClearVisitedNodeAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  uiGenerator.clearVisitedObject(adapter);
	  return adapter;	    }


   
}
