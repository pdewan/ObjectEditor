

package bus.uigen.visitors;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.oadapters.RootAdapter;
import bus.uigen.oadapters.VectorAdapter;

import java.util.Enumeration;import java.util.Vector;
import util.models.AListenableString;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.editors.AdapterMatrix;
import bus.uigen.oadapters.HashtableAdapter;

public class AssignRowColumnAdapterVisitor extends ConditionalAdapterVisitor {
 AdapterMatrix adapterMatrix; int curRowNum = 0;
 int curColNum = 0;
 int indentation = 0;
  public AssignRowColumnAdapterVisitor(ObjectAdapter root, AdapterMatrix theMatrix) {
    	 super(root);		 adapterMatrix = theMatrix;
  }

   
  // Override this method to provide
  // specific action
  public Object visit(ObjectAdapter adapter) {	  
	  return adapter;  }
  
  public static  boolean hasCompositeChild (CompositeAdapter parent) {		for (int i=0; i < parent.getChildAdapterCount(); i ++) {		
		   ObjectAdapter child = parent.getChildAdapterAt(i);
		   //if ((child instanceof uiContainerAdapter)) return true;		   if (!child.isAtomic()) return true;
		}		return false;
  }  
  /*  public Object visit(uiObjectAdapter adapter, int targetLevel, int curLevel) {	
	 return this;  }
  */  public Object oldvisit(ObjectAdapter adapter, int curLevel) {	  //curRowNum++;
	  if ((curLevel > 0) )
	  	/*&& (adapter.isAtomic() ||
	  	  	(adapter.toString() != null && adapter.toString() != "" && adapter.toString() != " "))) 
	  	  	*/
	  	  	{		adapterMatrix.set(curRowNum, curLevel - 1, adapter);
		//nextLevel = curLevel;
	  } if (!(adapter  instanceof CompositeAdapter)) {		  curRowNum++;
		  //curColNum = 0;
		   return adapter;
	  }	   CompositeAdapter parent = (CompositeAdapter) adapter;
	   if (hasCompositeChild(parent)) return adapter;
	   assignRowColumnToChildren (parent, curRowNum, curLevel - 1);	   curRowNum++;
	   return adapter;  }
  public Object visit(ObjectAdapter adapter, int curLevel) {
  	return newvisit(adapter, curLevel);
  }
  //public Object newvisit(uiObjectAdapter adapter, int curLevel) {
  static boolean  isListenableString(ObjectAdapter adapter) {
	  return adapter instanceof VectorAdapter && 
	  adapter.getViewObject().getClass() == AListenableString.class;
	  
	  
  }
  public Object newvisit(ObjectAdapter adapter, int index) {
	  //curRowNum++;
	//int nextLevel = curLevel - 2;
	  //int nextLevel = curLevel - 1;
	  //uiObjectAdapter parent = (uiContainerAdapter) adapter.getParent();
	//if (curLevel == 0) nextLevel = -1; // not really sure what is going on here, but -2 is wrong
	  //if ((curLevel > 0) && (adapter.isAtomic() ||
	  if (adapter instanceof PrimitiveAdapter || isListenableString(adapter) ||
			  adapter != root &&  ((adapter.isAtomic() || (adapter.getUserObject() != null) || adapter.getShowBlankColumn() || adapter.isLabelled() &&
	  	  	(adapter.toString() != null && 
	  	  	adapter.toString() != "" && 
	  	  	adapter.toString() != " ")))) {
		  
		  
		  
		  if (adapter instanceof PrimitiveAdapter || isListenableString(adapter) ||
				  //adapter.getIndented()) {
			  adapter.showRowLabelColumn()) {
			  //uiContainerAdapter parent = (uiContainerAdapter) adapter.getParent();
			  
			  /*
			  if (curColNum == -1)
				  curColNum = getIndentation (adapter);
			  else
				  curColNum++;
			 // curColNum++;
			  * 
			  */
			  //if (index == 0) indentation ++;
			  
			  adapterMatrix.set (curRowNum, curColNum, adapter);
			  /*
			  if (index == parent.getChildAdapterCount() - 1 )
				  indentation --;
				  */
			  /*
			  if ( goToNextRow(parent, adapter)) {
			      curRowNum++;
				  //curColNum = curLevel - 1;
			      curColNum = indentation;
			  } else 
				  curColNum++;
				  */
			  
			  return true;
			  
			  
		  }
		  /*
		if (curColNum == - 1)
			adapterMatrix.set(curRowNum, curLevel - 1, adapter);
		else
			adapterMatrix.set (curRowNum, curColNum + 1, adapter);
			*/
		
		
		//nextLevel = curLevel -1;
	  } 
	  return false;
	  /*
	  if (!(adapter  instanceof uiContainerAdapter)) {
		  //curRowNum++;
		  //curColNum = 0;
		  //return lastCol + 1;
		  return adapter;
	  }
	  
	   //uiContainerAdapter parent = (uiContainerAdapter) adapter;
	   return adapter;
	   */
	   /*
	   if (hasCompositeChild(parent)) return adapter;
	   assignRowColumnToChildren (parent, curRowNum, nextLevel);
	   //curRowNum++;
	   //lastCol = -1;
	   return adapter;
	   */
}
 boolean horizontalKeyValue (ObjectAdapter adapter, ObjectAdapter prevChild) {
	 return prevChild != null && prevChild.isKeyAdapter() && 
	         adapter.getHashtableChildren().equals(AttributeNames.KEYS_AND_VALUES) 
	         && adapter.getHorizontalKeyValue();
 }
 boolean horizontalKeyValue (ObjectAdapter adapter) {
	 return adapter.getHashtableChildren().equals(AttributeNames.KEYS_AND_VALUES) 
     && adapter.getHorizontalKeyValue();
 }
  boolean goToNextRow (ObjectAdapter adapter, ObjectAdapter prevChild) {
	  //return adapter.isFlatTableRow();
	  return /*prevChild instanceof uiPrimitiveAdapter &&*/ adapter != null && 
	  //adapter.getTempAttributeValue(AttributeNames.DIRECTION).equals(AttributeNames.VERTICAL)
	  adapter.getDirection() != null &&
	  adapter.getDirection().equals(AttributeNames.VERTICAL);
	  /*
	  &&
	  !horizontalKeyValue(adapter, prevChild);
	  */
			  
  }
  boolean goToNextRow (ObjectAdapter adapter) {
	 return goToNextRow(adapter, null);
			  
  }
  int getIndentation (ObjectAdapter adapter) {
	  if (adapter instanceof RootAdapter ) return -1;
	  if (adapter.getIndented()) return 1 + getIndentation((CompositeAdapter)adapter.getParent());
	  return getIndentation((CompositeAdapter) adapter.getParent());
	  
  }
  public Vector traverse(ObjectAdapter adapter, Vector results,  int curLevel) {
	  boolean added = false;
	  if (adapter != null ) {	
		  if (doVisit(adapter))
			//results.addElement(newvisit(adapter, curLevel));
			  added = (Boolean) newvisit(adapter, curLevel);
		  if (adapter instanceof CompositeAdapter && !isListenableString(adapter)) {
			CompositeAdapter parent = (CompositeAdapter) adapter;
			//commenting out to have uniform
			//if (!doTraverse(parent)) return results;
		  //}
			/*
			if (adapter.getIndented())
				indentation++;
				*/
			// EITHER USE THIS - WHICH CAUSES
			if (added) {
				indentation ++;
				// let us put this in separate line
				curColNum = indentation;
			}
			// OR USE THIS
			/*
			if (goToNextRow(parent)) {
				curColNum = indentation;
				
			//}
				
			} else
				curColNum++;
				*/
				
			boolean horizontalKeyValue = horizontalKeyValue(adapter);
			boolean isKey = true;
			int origIndentation = indentation;
			for (int i=0; i < parent.getChildAdapterCount(); i ++) {	
			//for (int i=0; i < adapter.getChildCount(); i ++) {	
			   ObjectAdapter child = parent.getChildAdapterAt(i);			   
			  //uiObjectAdapter child = (uiObjectAdapter) adapter.getChildAt(i);
			  //traverse(child, results, curLevel + 1);
			   traverse(child, results, i);
			  //if (adapter.getDirection().equals(AttributeNames.VERTICAL)) {
			   /*
			   if (i == (parent.getChildAdapterCount() -1) && added)
				   indentation --;
				   */
			  
			  if (i < (parent.getChildAdapterCount() - 1)) {
				  if (goToNextRow(parent, child)) { 
					  if (parent instanceof HashtableAdapter && horizontalKeyValue && isKey ) {
						  curColNum++;
						  indentation = curColNum;
					  } else if (parent instanceof HashtableAdapter && horizontalKeyValue && !isKey) {
						  curRowNum++;			   
						  
				  //curColNum = curLevel - 1;
					      curColNum = origIndentation;
					  }
					  else {
						  curRowNum++;
						  curColNum = indentation;
					  }
					  
				  } 				  
				  
				   else if (i != 0 ||  ! (child instanceof CompositeAdapter)) //if we do not check otherwise we get an extra space
					  curColNum ++;
					  
			      
			  } 
			  isKey = !isKey;
			  /*
			  else
				  curColNum++;
				  */
				  
			}
			if (added) {
				indentation --;	
				curColNum = indentation;
			}
			/*
			if (added)
				indentation --;
				*/
			/*
			if (adapter.getIndented())
				indentation--;
				*/
	      }
		}  
		
		 return results;
	  }  public  void assignRowColumnToChildren (CompositeAdapter parent, int rowNum, int parentColNum) {
	  /*  	if (
  			parent.isTopDisplayedAdapter() && 
  			parent instanceof uiHashtableAdapter 
  			&& 
  			!((uiHashtableAdapter) parent).hasCompositeKey() &&
			!((uiHashtableAdapter) parent).hasCompositeElement()
			) 
  	{
  		boolean isKey = true;
  		int colNum = 0;
  		for (int childIndex = 0; childIndex < parent.getChildAdapterCount(); childIndex++) {
  		  //for (int childIndex = 0; childIndex < parent.getChildCount(); childIndex++) {
  		  	
  			  adapterMatrix.set (rowNum,
  							   colNum,
  							   (uiObjectAdapter) parent.getChildAdapterAt(childIndex));
  			  if (!isKey) {
  			  	rowNum++;
  			  	colNum = 0;
  			  } else colNum = 1;
  			  isKey = !isKey;
  			  
  							   //(uiObjectAdapter) parent.getChildAt(childIndex));
  								  
  			  
  		  }  
  		
  	} else if (parent instanceof uiHashtableAdapter 
  			&& 
		!((uiHashtableAdapter) parent).hasCompositeKey() &&
		!((uiHashtableAdapter) parent).hasCompositeElement()) {
  		if (parentColNum == -2) parentColNum = -1;
  		for (int childIndex = 1; childIndex < parent.getChildAdapterCount(); childIndex = childIndex + 2) {
  			adapterMatrix.set (rowNum,
  					//Math.max(parentColNum + 1 + childIndex/2, 0),
  					parentColNum + 1 + childIndex/2,
					   (uiObjectAdapter) parent.getChildAdapterAt(childIndex));
  			 
  		
  		}
  		*/
	  if (parent instanceof HashtableAdapter) {
		  Vector children = ((HashtableAdapter) parent).treeChildrenVector();
		  for (int childIndex = 0; childIndex < children.size(); childIndex++) {
			  //for (int childIndex = 0; childIndex < parent.getChildCount(); childIndex++) {
			  	
				  adapterMatrix.set (rowNum,
								   parentColNum + 1 + childIndex,
								   (ObjectAdapter) children.elementAt(childIndex));
				  curColNum = parentColNum + 1 + childIndex;
								   //(uiObjectAdapter) parent.getChildAt(childIndex));
									  
				  
			  }  
		  
  	} else  
  	for (int childIndex = 0; childIndex < parent.getChildAdapterCount(); childIndex++) {
	  //for (int childIndex = 0; childIndex < parent.getChildCount(); childIndex++) {
	  			  adapterMatrix.set (rowNum,						   parentColNum + 1 + childIndex,						   (ObjectAdapter) parent.getChildAdapterAt(childIndex));
		  curColNum = parentColNum + 1 + childIndex;
						   //(uiObjectAdapter) parent.getChildAt(childIndex));
							  
		  
	  }
	  
	    }
  
  
  /*   public boolean doVisit(uiObjectAdapter adapter) {
	  return !(adapter instanceof uiContainerAdapter) || !hasCompositeChild((uiContainerAdapter) adapter);  }
  */  public boolean doTraverse(CompositeAdapter parent) {	  return hasCompositeChild(parent);
	    }
}
