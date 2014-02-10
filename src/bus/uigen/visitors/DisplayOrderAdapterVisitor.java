// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.HashtableAdapter;
import java.util.Enumeration;import java.util.Vector;

public abstract class DisplayOrderAdapterVisitor extends AdapterVisitor {

  public DisplayOrderAdapterVisitor(ObjectAdapter root) {
    super(root);
  }
  public Vector visitChildren(ObjectAdapter adapter, Vector results) {
    if (adapter != null) 
      if (adapter instanceof CompositeAdapter) {
      	CompositeAdapter parent = (CompositeAdapter) adapter;
      	for (int i=0; i < parent.getChildAdapterCount(); i ++) {
      		ObjectAdapter child = parent.getChildAdapterAt(i);	   
	       results.addElement(visit(child));
		}
      }
	return results;
  }    public Vector traverse(ObjectAdapter adapter, Vector results) {
    if (adapter != null) {
      results.addElement(visit(adapter));
      if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;		for (int i=0; i < parent.getChildAdapterCount(); i ++) {		
		   ObjectAdapter child = parent.getChildAdapterAt(i);
		  traverse(child, results);
		}
      }
    }		 return results;
  }  public Vector traverse(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel) {
    if (adapter != null) {
      results.addElement(visit(adapter, targetLevel, curLevel));	  if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;		for (int i=0; i < parent.getChildAdapterCount(); i ++) {		
		   ObjectAdapter child = parent.getChildAdapterAt(i);		   if (targetLevel < curLevel)
		  traverse(child, results, targetLevel, curLevel + 1);
		}
      }	}  
			 return results;
  }
  public Vector traverse(ObjectAdapter adapter, Vector results, int curLevel) {
    if (adapter != null) {
      results.addElement(visit(adapter,  curLevel));	  if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;		for (int i=0; i < parent.getChildAdapterCount(); i ++) {		
		   ObjectAdapter child = parent.getChildAdapterAt(i);		   //if (targetLevel < curLevel)
		  traverse(child, results,  curLevel + 1);
		}
      }	}  
			 return results;
  }  public Vector traverseLeafs(ObjectAdapter adapter, Vector results) {
    if (adapter != null) {
      //results.addElement(visit(adapter));	  if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;		for (int i=0; i < parent.getChildAdapterCount(); i ++) {		
		   ObjectAdapter child = parent.getChildAdapterAt(i);
		  traverseLeafs(child, results);
		}
      } else results.addElement(visit(adapter));	}  
			 return results;
  }
  public Vector traverseHTs(ObjectAdapter adapter, Vector results, Vector traversed) {
  	if (traversed.contains(adapter.getRealObject())) return results;
  	traversed.addElement(adapter.getRealObject());    if (adapter instanceof HashtableAdapter) {
    	Object visitResult = visit(adapter);
    	//if (results.contains(visitResult)) return results; 
    	results.addElement(visitResult);
    	//results.addElement(visit(adapter));
    }
    if (adapter != null) {
      //results.addElement(visit(adapter));	  if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;				for (int i=0; i < parent.getChildAdapterCountBasic(); i ++) {		
		   ObjectAdapter child = parent.getChildAdapterAt(i);
		  traverseHTs(child, results, traversed);
		}
      } 	}  
			 return results;
  }
  public Vector traverseChildHTs(HashtableAdapter parent, Vector results) {	  for (int i = 0; i < parent.getChildAdapterCount(); i ++) {		 ObjectAdapter child = parent.getChildAdapterAt(i).getExpandedAdapter();
		  traverseHTs(child, results, new Vector());
		}
			 return results;
  }
  public Vector traverseChildHTs(Vector parents, Vector results) {	  for (int i = 0; i < parents.size(); i++) {
		  traverseChildHTs ((HashtableAdapter) parents.elementAt(i), results);		 
		}			 return results;
  }  
}
