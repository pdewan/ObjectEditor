// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;

import java.util.Hashtable;

import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.trace.TooManyLevelsInDisplayedLogicalStructure;
import bus.uigen.trace.TooManyNodesInDisplayedLogicalStructure;
public class CreateChildrenAdapterVisitor extends ElideAdapterVisitor {	  
  public CreateChildrenAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {	  if (adapter instanceof CompositeAdapter) {
		  //((uiContainerAdapter) adapter).createChildrenBasic();
		  // we may be reusing existing children, so let us make it more efficient
		  //System.out.println("Visit:Create Children " + adapter + "" + adapter.getRealObject());
		  ((CompositeAdapter) adapter).createChildren();	  }
	  return null;  }
  public Object visit(ObjectAdapter adapter, Hashtable sharedProps) {
	  if (adapter instanceof CompositeAdapter) {
		  ((CompositeAdapter) adapter).createChildrenBasic(sharedProps);
	  }
	  return null;
}

    public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum) {	  //if (! (adapter instanceof uiContainerAdapter) ) return null;	  //uiContainerAdapter composite = (uiContainerAdapter) adapter;
	 // System.out.println("visit" + adapter);	   if (((curLevel < targetLevel) && (curNodeNum < targetNodeNum))) 
		   return visit(adapter);
	   else if (curLevel >= targetLevel) {
//		   Tracer.error("Number of nesting levels:" + curLevel  + " > + max levels:" + targetLevel);
		   throw TooManyLevelsInDisplayedLogicalStructure.newCase(adapter, targetLevel, this);

	   } else {
		   throw TooManyNodesInDisplayedLogicalStructure.newCase(adapter, targetNodeNum, this);
//		   Tracer.error("Number of nodes:" + curNodeNum + " > max nodes:" + targetNodeNum);

	   }
//	   return null;  }  public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) {
	 // System.out.println("visit" + adapter);	   //if (adapter.getGenericWidget() == null) return null;
	  if (curLevel < targetLevel)
		  return visit(adapter);		
	  return null;  }
  public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, Hashtable sharedProps) {
	 // System.out.println("visit" + adapter);
	   //if (adapter.getGenericWidget() == null) return null;
	  if (curLevel < targetLevel)
		  return visit(adapter, sharedProps);		
	  return null;
 }
  public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel,
			int targetNodeNum, int curNodeNum, Hashtable sharedProps) {
		
		if (((curLevel < targetLevel) && (curNodeNum < targetNodeNum)))
		 visit (adapter, sharedProps);
		return null;
	}

//end retarget 
}
