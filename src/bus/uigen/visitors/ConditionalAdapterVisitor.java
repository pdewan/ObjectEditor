// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.HashtableAdapter;
import java.util.Enumeration;import java.util.Vector;

public  abstract class ConditionalAdapterVisitor extends DisplayOrderAdapterVisitor {

  public ConditionalAdapterVisitor(ObjectAdapter root) {
    super(root);
  }
  public  boolean doVisit(ObjectAdapter adapter){ 
  	return true;  	
  }  public boolean doTraverse (CompositeAdapter adapter) {return true;}
    public Vector traverse(ObjectAdapter adapter, Vector results) {
    if (adapter != null) {	  if (doVisit(adapter))
		results.addElement(visit(adapter));
      if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;
		if (!doTraverse(parent)) return  results;
      //}
				for (int i=0; i < parent.getChildAdapterCount(); i ++) {
		//for (int i=0; i < adapter.getChildCount(); i ++) {
		   ObjectAdapter child = parent.getChildAdapterAt(i);
		//uiObjectAdapter child = (uiObjectAdapter) adapter.getChildAt(i);
		  traverse(child, results);
		}
      }
    }		 return results;
  }  public Vector traverse(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel) {
    if (adapter != null ) {		  if (doVisit(adapter))
		results.addElement(visit(adapter, targetLevel, curLevel));	  if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;	  
		if (!doTraverse(parent)) return results;
	  //}		for (int i=0; i < parent.getChildAdapterCount(); i ++) {
		//for (int i=0; i < adapter.getChildCount(); i ++) {
		   ObjectAdapter child = parent.getChildAdapterAt(i);
		  //uiObjectAdapter child = (uiObjectAdapter) adapter.getChildAt(i);
		  traverse(child, results, targetLevel, curLevel + 1);
		}
      }	}  
			 return results;
  }
  public Vector traverse(ObjectAdapter adapter, Vector results,  int curLevel) {
    if (adapter != null ) {		  if (doVisit(adapter))
		results.addElement(visit(adapter, curLevel));	  if (adapter instanceof CompositeAdapter) {		CompositeAdapter parent = (CompositeAdapter) adapter;
		if (!doTraverse(parent)) return results;
	  //}		for (int i=0; i < parent.getChildAdapterCount(); i ++) {	
		//for (int i=0; i < adapter.getChildCount(); i ++) {	
		   ObjectAdapter child = parent.getChildAdapterAt(i);
		  //uiObjectAdapter child = (uiObjectAdapter) adapter.getChildAt(i);
		  traverse(child, results, curLevel + 1);
		}
      }	}  
			 return results;
  }
  public Vector traverse(int curLevel) {	Vector results = new Vector();
    traverse(root, results, curLevel);	return results;
  }    
}
