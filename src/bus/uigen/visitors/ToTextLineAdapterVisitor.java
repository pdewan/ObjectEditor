// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
import java.util.Enumeration;import java.util.Vector;

public class ToTextLineAdapterVisitor extends DisplayOrderAdapterVisitor {

  public ToTextLineAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  return adapter.toText();  }  
    public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) {	
	  String myText = adapter.toText();
	  if (adapter == root || myText.equals("")) return "";
	  /*	  uiContainerAdapter parent = adapter.getParentAdapter();	  	  StringBuffer sb = new StringBuffer("");
	  if (parent != null)
		if (parent.getDirection() == "vertical") {
			  sb.append('\n');			for (int i = 1; i < curLevel; i++)				  sb.append('\t');
		} else
			  sb.append('\t');	  //sb.append(adapter.toString());
	  */	  return " " + myText;
	 // System.out.println("visit" + adapter);	   //return sb.toString();  }
  /*  public Vector traverse(uiObjectAdapter adapter, Vector results) {
    if (adapter != null) {
      results.addElement(visit(adapter));
      if (adapter instanceof uiContainerAdapter) {		uiContainerAdapter parent = (uiContainerAdapter) adapter;		for (int i=0; i < parent.getChildAdapterCount(); i ++) {		
		   uiObjectAdapter child = parent.getChildAdapterAt(i);
		  traverse(child, results);
		}
      }
    }		 return results;
  }  public Vector traverse(uiObjectAdapter adapter, Vector results, int targetLevel, int curLevel) {
    if (adapter != null) {
      results.addElement(visit(adapter, targetLevel, curLevel));	  if (adapter instanceof uiContainerAdapter) {		uiContainerAdapter parent = (uiContainerAdapter) adapter;		for (int i=0; i < parent.getChildAdapterCount(); i ++) {		
		   uiObjectAdapter child = parent.getChildAdapterAt(i);
		  traverse(child, results, targetLevel, curLevel + 1);
		}
      }	}  
			 return results;
  }  */
}
