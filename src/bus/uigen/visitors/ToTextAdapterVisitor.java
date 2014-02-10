// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
import java.util.Enumeration;

public class ToTextAdapterVisitor extends AdapterVisitor {

  public ToTextAdapterVisitor(ObjectAdapter root) {
    super(root);
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {
	  return adapter.toString();  }  
    public Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) {	
	  String myText = adapter.toText();
	  if (myText.equals("")) return "";	  CompositeAdapter parent = adapter.getParentAdapter();	  	  StringBuffer sb = new StringBuffer("");
	  if (parent != null)
		if (parent.getDirection() == "vertical") {
			if (adapter.getIndex() != 0)
			  sb.append('\n');			for (int i = 1; i < curLevel; i++)				  sb.append('\t');
		} else
			  sb.append('\t');	  //sb.append(adapter.toString());	  sb.append(myText);
	 // System.out.println("visit" + adapter);	   return sb.toString();  }
}
