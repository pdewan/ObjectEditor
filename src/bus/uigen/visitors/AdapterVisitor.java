// Implementing a Visitor pattern for the objectAdapter tree
// New operations can be implemented by subclassing this
// class

package bus.uigen.visitors;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import util.trace.Tracer;

import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.trace.TooManyNodesInDisplayedLogicalStructure;
public abstract class AdapterVisitor {
	public static int MAX_NODES = 5000;

   ObjectAdapter root = null;
   
     
  public AdapterVisitor(ObjectAdapter root) {
    this.root = root;
  }

  public Vector traverse() {	Vector results = new Vector();
    traverse(root, results);	return results;
  }
  public Vector traverseRange (int from, int to) {
	Vector results = new Vector();
    traverseRange(root, results, from, to);
	return results;
  }
  public Vector traverse(Hashtable ignorePs) {
	Vector results = new Vector();
    traverse(root, results, ignorePs);
	return results;
  }
  public Vector traversePostOrder() {	Vector results = new Vector();
    traversePostOrder(root, results);	return results;
  }
  public Vector traversePostOrderRange(int from, int to) {
	Vector results = new Vector();
    traversePostOrderRange(root, results, from, to);
	return results;
  }
  public Vector traversePostOrder(Hashtable ignorePs) {
	Vector results = new Vector();
    traversePostOrder(root, results,  ignorePs);
	return results;
  }  public Vector traverse(int targetLevel) {	Vector results = new Vector();
    traverse(root, results, targetLevel, 1);	return results;
  }
    public Vector traverse(int targetLevel, int targetNodeNum) {	Vector results = new Vector();
    traverse(root, results, targetLevel, 1, targetNodeNum, 0);	return results;
  }
  
  public Vector traverseVisibleAndInvisible(int targetLevel, int targetNodeNum) {
		Vector results = new Vector();
	    traverseVisibleAndInvisible(root, results, targetLevel, 1, targetNodeNum, 0);
		return results;
	  }
  
  
	  
    public Vector traverseContainers() {	  	  Vector results = new Vector();	  traverseContainers(root, results);
	  return results;  }  public Vector traverseNonAtomicContainers() {	  	  Vector results = new Vector();	  traverseNonAtomicContainers(root, results);
	  return results;  }
  public Vector traverseNonAtomicChildrenContainers() {	  
	  Vector results = new Vector();
	  traverseNonAtomicChildrenContainers(root, results);
	  return results;
  }
  public Vector traverseNonAtomicContainersPostOrder() {	  
	  Vector results = new Vector();
	  traverseNonAtomicContainersPostOrder(root, results);
	  return results;
  }
  public Vector traverseNonAtomicContainersRange(int from, int to) {	  
	  Vector results = new Vector();
	  traverseNonAtomicContainersRange(root, results, from, to);
	  return results;
}
  public Vector traverseNonAtomicContainers(Hashtable ignorePs) {	  
	  Vector results = new Vector();
	  traverseNonAtomicContainers(root, results, ignorePs);
	  return results;
}
  public Vector traverseContainersTo(int maxLevel) {	  	  Vector results = new Vector();	  traverseContainersTo(root, results, maxLevel, 1);
	  return results;  }
  public Vector traverseContainersFrom(int minLevel) {	  	  Vector results = new Vector();	  traverseContainersFrom(root, results, minLevel, 1);
	  return results;  }  public Vector traverseContainers(int targetLevel) {	  	  Vector results = new Vector();	  traverseContainers(root, results, targetLevel, 1);
	  return results;  }
  public Vector visitContainersAt(int level) {	  	  Vector results = new Vector();	  visitContainersAt(root, results, level, 1);
	  return results;  }  public Vector visitChildren() {	  Vector results = new Vector();
	  visitChildren(root, results);
	  return results;  }

  public Vector traverse(ObjectAdapter adapter, Vector results) {
  	return traverseRange (adapter, results, 0, Integer.MAX_VALUE);
  	/*
    if (adapter != null) {
      results.addElement(visit(adapter));
      if (adapter instanceof uiContainerAdapter) {
		Enumeration children = ((uiContainerAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   uiObjectAdapter child = (uiObjectAdapter) children.nextElement();
		  traverse(child, results);
		}
      }
      
    		 return results;
	 */
  }
  boolean traverseContainerAdapter (CompositeAdapter adapter) {
	  return true;
  }
  public Vector traverseRange(ObjectAdapter adapter, Vector results, int from, int to) {
	  if (isRecursive(adapter, results))
		  return results;
  	int index = -1;
    if (adapter != null && !results.contains(adapter)) {
    	Object retVal = visit(from, to, adapter);
    	if (retVal != null)
    		results.addElement(retVal);
      //results.addElement(visit(from, to, adapter));
      if (adapter instanceof ClassAdapter && traverseContainerAdapter((CompositeAdapter)adapter)) {
    	  ClassAdapter classAdapter = (ClassAdapter) adapter;
    	  Enumeration children = classAdapter.getChildAdapters();
		//Enumeration children = ((uiContainerAdapter) adapter).getChildAdapters();
    	//Enumeration children2 = ((uiContainerAdapter) adapter).getDynamicChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   index++;
		   if (index >=from && index <= to || (index < classAdapter.getNumberOfDisplayedStaticChildren())) 
		   	traverse(child, results);
		}
      }
    }
	
	 return results;
  }
  public  Vector traverseChildrenRange(int from, int to) {
	  Vector results = new Vector();
	  return traverseChildrenRange (root, results, from, to);
	  
  }
  public  Vector traverseChildrenRange(ObjectAdapter adapter, Vector results, int from, int to) {
	  if (isRecursive(adapter, results))
		  return results;
	    if (adapter != null &&  !results.contains(adapter)) 
	      if (adapter instanceof CompositeAdapter) {
	    	  int index = -1;
			Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
			while (children.hasMoreElements()) {
			   ObjectAdapter child = (ObjectAdapter) children.nextElement();
			   index++;
			   if (index >=from && index <= to) {
			   results.addElement(visit(child));
		       results.addElement(traverseChildren(child, results));
			   }
			}
	      }
		return results;
	  }
  public Vector traverse(ObjectAdapter adapter, Vector results, Hashtable ignorePs) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null && !results.contains(adapter)) {
      results.addElement(visit(adapter, ignorePs));
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
			
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   if (ignorePs.get(child.getPropertyName()) == null)
		  traverse(child, results, ignorePs);
		}
      }
    }
	
	 return results;
  }
  public Vector traversePrimitives(ObjectAdapter adapter, Vector results) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null  && !results.contains(adapter)) {
      if (adapter instanceof PrimitiveAdapter)
      	results.addElement(visit(adapter));
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traversePrimitives(child, results);
		}
      }
    }
	
	 return results;
  }
  public Vector traversePrimitives(ObjectAdapter adapter) {
  	Vector results = new Vector();
  	return traversePrimitives(adapter, results);    
	
  }
  public Vector traversePrimitives() {
  	
  	return traversePrimitives(root);    
	
  }  public Vector traversePostOrder(ObjectAdapter adapter, Vector results) {
  	return traversePostOrderRange(adapter,results, 0, Integer.MAX_VALUE);
  	/*
    if (adapter != null) {
      if (adapter instanceof uiContainerAdapter) {
		Enumeration children = ((uiContainerAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   uiObjectAdapter child = (uiObjectAdapter) children.nextElement();
		  traversePostOrder(child, results);
		}
      }	  
      results.addElement(visit(adapter));
    }		 return results;
	 */
  }
  public Vector traversePostOrderRange(ObjectAdapter adapter, Vector results, int from, int to) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null && !results.contains(adapter)) {
      if (adapter instanceof ClassAdapter) {
    	  ClassAdapter classAdapter = (ClassAdapter) adapter;
      	int index = -1;
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  index++;
		  if (index >= from && index <= to || (index < classAdapter.getNumberOfDisplayedStaticChildren()))
		  traversePostOrder(child, results);
		}
      }	  
      results.addElement(visit(adapter));
    }
	
	 return results;
  }
  public Vector traversePostOrder(ObjectAdapter adapter, Vector results, Hashtable ignorePs) {
	  if (isRecursive(adapter, results))
		  return results;
  	
    if (adapter != null && !results.contains(adapter)) {
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   if (ignorePs.get(child.getPropertyName()) == null)
		   	traversePostOrder(child, results, ignorePs);
		}
      }	  
      results.addElement(visit(adapter, ignorePs));
    }
	
	 return results;
	 
  }
    public Vector traverse(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) {
      results.addElement(visit(adapter, targetLevel, curLevel));
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverse(child, results, targetLevel, curLevel + 1);
		}
      }
    }		 return results;
  }
  boolean isRecursive (ObjectAdapter adapter, Vector results) {
	  if (adapter != null && adapter.isRecursive())
		  return true;
	  if (results.size() > MAX_NODES) {
    	  Tracer.fatalError("More than " + MAX_NODES + " elements in logical structure. It is probably infinite.");   
    	  adapter.setIsRecursive(true);
    	  return true;
	  }
	  return false;
  }  public Vector traverse(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum) {
//	  if (isRecursive(adapter, results))
//		  return results;
    if (adapter != null &&  !results.contains(adapter)) {
    	
      results.addElement(visit(adapter, targetLevel, curLevel, targetNodeNum, curNodeNum++));
    	
      if (isRecursive(adapter, results))
    	  return results;
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   try {
		  traverse(child, results, targetLevel, curLevel + 1, targetNodeNum, curNodeNum++);
		   } catch (Exception e) {
			   e.printStackTrace();
	    		return results;
	    	}
		}
      }
    }		 return results;
  }
  
  public Vector traverseVisibleAndInvisible(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum) {
//	  if (isRecursive(adapter, results))
//		  return results;
    if (adapter != null &&  !results.contains(adapter)) {
    	
      results.addElement(visit(adapter, targetLevel, curLevel, targetNodeNum, curNodeNum++));
    	
      if (isRecursive(adapter, results))
    	  return results;
      if (adapter instanceof CompositeAdapter) {
//		Enumeration children = (((CompositeAdapter) adapter).getVisibleAndInvisibleDynamicChildAdapters()).elements();
		Enumeration children = (((CompositeAdapter) adapter).getVisibleAndInvisibleChildAdapters()).elements();

		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   try {
		  traverseVisibleAndInvisible(child, results, targetLevel, curLevel + 1, targetNodeNum, curNodeNum++);
		   } catch (Exception e) {
			   e.printStackTrace();
	    		return results;
	    	}
		}
      }
    }
	
	 return results;
  }
    public Vector traverseContainers(ObjectAdapter adapter, Vector results) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter) {	    results.addElement(visit(adapter));
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverseContainers(child, results);
		}
      }
	return results;  }
   public Vector traverseNonAtomicContainers(ObjectAdapter adapter, Vector results) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter && !adapter.isAtomic()) {
 //   		  &&     		  !adapter.hasOnlyGraphicsDescendents()) {	    results.addElement(visit(adapter));
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverseNonAtomicContainers(child, results);
		}
      }
	return results;  }
  public Vector traverseNonAtomicContainersPostOrder(ObjectAdapter adapter, Vector results) {
	  if (isRecursive(adapter, results))
		  return results;
	    if (adapter != null &&  !results.contains(adapter)) 
	      if (adapter instanceof CompositeAdapter && !adapter.isAtomic()) {
		    
			Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
			while (children.hasMoreElements()) {
			   ObjectAdapter child = (ObjectAdapter) children.nextElement();
			  traverseNonAtomicContainers(child, results);
			}
			results.addElement(visit(adapter));
	      }
		return results;
	  }
  
  public Vector traverseNonAtomicContainersRange(ObjectAdapter adapter, Vector results, int from, int to) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) 
     // if (adapter instanceof uiContainerAdapter && !adapter.isAtomic()) {  
     if (adapter instanceof ClassAdapter && !adapter.isAtomic()) { 
    	 ClassAdapter classAdapter =  (ClassAdapter) adapter;    	
	    results.addElement(visit(from, to, adapter));
		//Enumeration children = ((uiContainerAdapter) adapter).getChildAdapters();
		Enumeration children = classAdapter.getChildAdapters();
		int index = -1;
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   index ++;
		   if ((index >= from && index < to) || 
				   (index < classAdapter.getNumberOfDisplayedStaticChildren())) {
		  traverseNonAtomicContainers(child, results);
		   }
		}
      }
	return results;
  }
  public Vector traverseNonAtomicContainers(ObjectAdapter adapter, Vector results, Hashtable ignorePs) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter && !adapter.isAtomic()) {
	    results.addElement(visit(adapter, ignorePs));
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   if (ignorePs.get(child.getPropertyName()) == null)
		   	traverseNonAtomicContainers(child, results, ignorePs);
		}
      }
	return results;
  }
  
  public Vector traverseContainers(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter) {	    results.addElement(visit(adapter, targetLevel, curLevel));
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverseContainers(child, results, targetLevel, curLevel + 1);
		}
      }
	return results;  }    
  public Vector traverseContainersTo(ObjectAdapter adapter, Vector results, int maxLevel, int curLevel) {
	  if (isRecursive(adapter, results))
		  return results;
    if ((curLevel <= maxLevel)&& (adapter != null &&  !results.contains(adapter))) 
      if (adapter instanceof CompositeAdapter) {	    results.addElement(visit(adapter));
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverseContainersTo(child, results, maxLevel, curLevel + 1);
		}
      }
	return results;  }
  public Vector traverseContainersFrom(ObjectAdapter adapter, Vector results, int minLevel, int curLevel) {
	  if (isRecursive(adapter, results))
		  return results;
    if ( (adapter != null &&  !results.contains(adapter))) 
      if (adapter instanceof CompositeAdapter) {
		if (curLevel >= minLevel)	       results.addElement(visit(adapter));
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverseContainersFrom(child, results, minLevel, curLevel + 1);
		}
      }
	return results;  }  
  public Vector visitContainersAt(ObjectAdapter adapter, Vector results, int level, int curLevel) {
	  if (isRecursive(adapter, results))
		  return results;
    if ( (adapter != null &&  !results.contains(adapter))) 
      if (adapter instanceof CompositeAdapter) {
		if (curLevel == level)	      results.addElement(visit(adapter));
		   //visit(adapter);
			//;		else {
			Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
			while (children.hasMoreElements()) {
			   ObjectAdapter child = (ObjectAdapter) children.nextElement();
			  visitContainersAt(child, results, level, curLevel + 1);
			}		}
      }
	return results;  }    public Vector visitChildren(ObjectAdapter adapter, Vector results) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();		   	       results.addElement(visit(child));
		}
      }
	return results;  }
  public Vector traverseNonAtomicChildrenContainers(ObjectAdapter adapter, Vector results) {
	  
	  if (isRecursive(adapter, results))
    	  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   if (child instanceof CompositeAdapter && !child.isAtomic()) {
		   results.addElement(visit(child));
	       results.addElement(traverseChildren(child, results));
		   }
		}
      }
	return results;
  }
  public Vector traverseChildren(ObjectAdapter adapter, Vector results) {
	  
	  if (isRecursive(adapter, results))
    	  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   results.addElement(visit(child));
	       results.addElement(traverseChildren(child, results));
		}
      }
	return results;
  }
  public Vector traverseChildren(ObjectAdapter adapter, Vector results, Hashtable ignorePs) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) 
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		   if (ignorePs.get(child.getPropertyName()) == null) {
		   results.addElement(visit(child, ignorePs));
	       results.addElement(traverseChildren(child, results, ignorePs));
		   }
		}
      }
	return results;
  }
  public  Vector traverseChildren(ObjectAdapter adapter) {
   return traverseChildren (adapter, new Vector());
  }
  public  Vector traverseNonAtomicChildrenContainers(ObjectAdapter adapter) {
	   return traverseNonAtomicChildrenContainers(adapter, new Vector());
	  }
  public  Vector traverseChildren(ObjectAdapter adapter, Hashtable ignorePs) {
    return traverseChildren (adapter, new Vector(), ignorePs);
   }
  public  Vector traverseChildren() {
    return traverseChildren (root, new Vector());
   }
  public  Vector traverseChildren(Hashtable ignorePs) {
    return traverseChildren (root, new Vector(), ignorePs);
   }
//F.O. addition  to support retargeting
  //from adaptervistor

public Vector traverse(int targetLevel, int targetNodeNum, Vector sharedPs) {
	Vector results = new Vector();
    traverse(root, results, targetLevel, 1, targetNodeNum, 0, sharedPs );
	return results;
  }
public Vector traverse(int targetLevel, int targetNodeNum, Hashtable sharedProps) {
	Vector results = new Vector();
    traverse(root, results, targetLevel, 1, targetNodeNum, 0, sharedProps );
	return results;
  }

 //F.O. addition  to support retargeting
  public Vector traverse(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel,
	int targetNodeNum, int curNodeNum, Vector sharedPs) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) {
      results.addElement(visit(adapter, targetLevel, curLevel, targetNodeNum, curNodeNum++, sharedPs));
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverse(child, results, targetLevel, curLevel + 1, targetNodeNum, curNodeNum++, sharedPs);
		}
      }
    }
	
	 return results;
  }

  public Vector traverse(ObjectAdapter adapter, Vector results, int targetLevel, int curLevel,
	int targetNodeNum, int curNodeNum, Hashtable sharedProps) {
	  if (isRecursive(adapter, results))
		  return results;
    if (adapter != null &&  !results.contains(adapter)) {
      results.addElement(visit(adapter, targetLevel, curLevel, targetNodeNum, curNodeNum++, sharedProps));
      if (adapter instanceof CompositeAdapter) {
		Enumeration children = ((CompositeAdapter) adapter).getChildAdapters();
		while (children.hasMoreElements()) {
		   ObjectAdapter child = (ObjectAdapter) children.nextElement();
		  traverse(child, results, targetLevel, curLevel + 1, targetNodeNum, curNodeNum++, sharedProps);
		}
      }
    }
	
	 return results;
  }
  
  // Override this method to provide
  // specific action
  //public abstract Object visit(uiObjectAdapter adapter);  //public abstract Object visit(uiObjectAdapter adapter, int targetLevel, int curLevel);
  
  // for some reason am not making them abstract//  public  Object visit(ObjectAdapter adapter) { System.out.println("null traverse called");return null;};
  public  abstract Object visit(ObjectAdapter adapter);

  public  Object visit(int from,  int to, ObjectAdapter adapter) {
	  return visit(adapter);
	  };
  public  Object visit(ObjectAdapter adapter, Hashtable ignorePs) { 
  	return visit(adapter);
  };
  public  Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) {
	  return visit(adapter);
//	  System.out.println("null traverse called"); return null;
};
//  public abstract  Object visit(ObjectAdapter adapter, int targetLevel, int curLevel) ;
  public  Object visit(ObjectAdapter adapter, int curLevel) {
	  return visit(adapter);
//	  System.out.println("null traverse called"); return null;
	  };  public  Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum) {
	  return visit(adapter);
//	  System.out.println("null traverse called"); return null;
	  
  };
  public  Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum,
		   Vector sharedPs) {
	  return visit(adapter);
//System.out.println("null traverse called"); 
//  return null;
};

public  Object visit(ObjectAdapter adapter, int targetLevel, int curLevel, int targetNodeNum, int curNodeNum,
		   Hashtable sharedProps) {
	return visit(adapter);
//System.out.println("null traverse called"); 
//return null;
};

}

