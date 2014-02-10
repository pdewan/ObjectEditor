/*
  uiSelectionManager
  Does the following:
  .  Keeps track of currently selected field
  .  listen to Select Up/ Select Down requests
  .  Services requests for currently selected field/object
  */
package bus.uigen.controller;import bus.uigen.ObjectRegistry;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;

import java.util.*;
import javax.swing.tree.TreePath;

import util.trace.Tracer;
import bus.uigen.ars.*;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.VectorAdapter;
public class SelectionManager {
  //private static Selectable lastSelection = null;
  private static ObjectAdapter lastSelection = null;
  static Stack positionStack = new Stack();
  static Vector selections = new Vector();
  //static SelectionListener selectionListener;
  static Vector selectionListeners = new Vector();  
  /*  public static void setSelectionListener(SelectionListener theSelectionListener) {
	  //selectionListener = theSelectionListener;
	  //selectionListeners.addElement(theSelectionListner);
	  addSelectionListener(theSelectionListener);  }
  */
  public static boolean overriddingGlassPaneObjectSelected(Selectable newSelection) {
	  if (selections.size() != 1)
		  return false;
	  ObjectAdapter selectedAdapter =  (ObjectAdapter) selections.get(0);
	 return selectedAdapter != newSelection && (selectedAdapter.getUIFrame().isGlassPane());
	  
  }  public static void addSelectionListener(SelectionListener theSelectionListener) {
	  //selectionListener = theSelectionListener;	  if (!selectionListeners.contains(theSelectionListener))
		selectionListeners.addElement(theSelectionListener);  }
  
  public static void removeSelectionListener (SelectionListener theSelectionListener) {
	  selectionListeners.remove(theSelectionListener);
  }
      static void notifySelectionListener() {	  //if (selectionListener == null) return;	  for (int i = 0; i < selectionListeners.size(); i++) {		  SelectionListener selectionListener = (SelectionListener) selectionListeners.elementAt(i);	  
	  if (selections.size() == 0)
		  selectionListener.noItemSelected();
	  else if (selections.size() == 1)
		  selectionListener.singleItemSelected();
	  else 
		  selectionListener.multipleItemsSelected();
	  }
	  if (selections.size() == 1) {
		  ObjectAdapter selectedAdapter = (ObjectAdapter) selections.get(0);
		  MethodInvocationManager.invokeSelectMethod(selectedAdapter);
		  
		 
	  }  }

  public static Selectable getCurrentSelection() {
	  if (selections.size() == 1)
		  return (Selectable) selections.elementAt(0);
	  else
		  return null;	  
    //return lastSelection;
  }  public static Vector getSelections() {
	  return selections;  }  public static Vector getSelectedObjects() {
	  Vector retVal = new Vector();
	  for (int i = 0; i < selections.size(); i++)
		  retVal.addElement(((ObjectAdapter) selections.elementAt(i)).getObject());
	  return retVal;  }  
  
  public static Selectable getLastSelection() {
	  return getCurrentSelection();	  
    //return lastSelection;
  }

  // Method invoked when a component is selected
  // from the user interface
  public static void select(Selectable newSelection) {
	  clearColumn();
	  select (newSelection, false);
	  /*	  
    // Check if its a toggle operation
    if (lastSelection != null && lastSelection == newSelection) {
      lastSelection.unselect();
      lastSelection = null;
      return;
    }
    // Lets just unselect the old selection first
    if (lastSelection != null)
      lastSelection.unselect();
    lastSelection = newSelection;		if (newSelection == null) return;
    // Throw away the old stack
    positionStack = new Stack();
    lastSelection.select();   
    if (uiParameters.CopyOnSelect) 
      ObjectClipboard.add(lastSelection.getObject());
	  */	  
  }  public static void unselect() {	  internalRemoveAllSelections();
	  notifySelectionListener();	  /*
	  if (lastSelection != null) {		  lastSelection.unselect();
          lastSelection = null;	  }	  */
		    }  public static void addSelection(Selectable newSelection) {
	  clearColumn();	  if (selections.contains(newSelection)) {
		newSelection.unselect();		selections.removeElement(newSelection);
		notifySelectionListener();		//if (selections.size() == 1) selectionListener.singleItemSelected();
		return;
    }	  newSelection.select();
	  selections.addElement(newSelection);	  /*
	  if (selections.size() == 1) selectionListener.singleItemSelected();
	  if (selections.size() == 2) selectionListener.multipleItemsSelected();
	  */
	  notifySelectionListener();	  	  //select(newSelection, false);  }
  public static void putSelectionInClipboard() {
	
	  if (getCurrentSelection() != null) {
		ObjectClipboard.set(getCurrentSelection().getObject());
	  } else if (column != null) {
		  ObjectClipboard.setColumn(getColumnAdapterParent(), getColumnAdapter(), getColumn());
		  
	  }	  
  }
  public static void putColumnInClipboard() {
	  ObjectClipboard.setColumn(getColumnAdapterParent(), getColumnAdapter(), getColumn());
  }  public static void internalRemoveAllSelections() {	  //selectionListener.noItemSelected();
	  for (int i = 0; i < selections.size(); i++) {		  ((Selectable) selections.elementAt(i)).unselect();
	  }
	  if (selections.size() > 0) {		  ((ObjectAdapter) selections.elementAt(0)).getUIFrame().clearTreeSelection();
	  }
	  selections.removeAllElements();
	  //notifySelectionListener();  }
  public static void removeAllSelections() {	  internalRemoveAllSelections();
	  notifySelectionListener();  }
  
  static void selectColumn (Selectable[] newSelections) {
	  //internalRemoveAllSelections();
	  Vector<Selectable> newRange = new Vector();
	  for (int i = 0; i < newSelections.length; i++)
		  newRange.add(newSelections[i]);
	  if (!selectionsChanged(newRange)) return;
	  if (internalReplaceSelections(newRange));
		notifySelectionListener();
	  //notifySelectionListener();
  }  
  public static void replaceSelections(Selectable newSelection) {
	  if (overriddingGlassPaneObjectSelected(newSelection)) return;
	  clearColumn();	  if (selections.size() == 1 && selections.contains(newSelection)) {		  internalRemoveAllSelections();
		  notifySelectionListener();	  } else {
		  Vector newSelections = new Vector();		  newSelections.addElement(newSelection);		  internalReplaceSelections(newSelections);				  notifySelectionListener();
		  //selectionListener.singleItemSelected();		  /*											  internalRemoveAllSelections();		  		  addSelection(newSelection);
		  */	  }
  }  static boolean selectionsChanged(Vector newRange) {
	  if (newRange.size() != selections.size()) return true;	  
	  for (int i = 0; i < newRange.size(); i++)
		  if (!selections.contains(newRange.elementAt(i))) return true;
	  return false;  }  public static void replaceSelections(Vector newRange) {
  	if (!selectionsChanged(newRange)) return;
  	clearColumn();
  	if (createColumn(newRange)) {
  		setColumnVariables();
  		//selectColumn(getColumn());
  		  	} 
    //else 
  	if (internalReplaceSelections(newRange))
		notifySelectionListener();  }
  public static boolean internalReplaceSelections(Vector newRange) {	  TreePath[] selectedPaths = new TreePath[newRange.size()];
	  if (!selectionsChanged(newRange)) return false;	  internalRemoveAllSelections();
	  ObjectAdapter adapter = null;
	  boolean treeVisible = false;	  for (int i = 0; i  < newRange.size(); i++) {
		  adapter = (ObjectAdapter) newRange.elementAt(i);
		  selections.addElement(adapter);
		  adapter.select();		  if (i == 0)			  treeVisible = adapter.getUIFrame().treePanelIsVisible();		  if (treeVisible) {
		     selectedPaths[i] = adapter.getTreePath();
		     //System.out.println("path:" + adapter.getPath());		      //System.out.println("path:" + adapter.getBeautifiedPath());
		     //System.out.println("treePath:" + adapter.getTreePath());
		  }	  }	
	  	  if (treeVisible && selectedPaths.length > 1) {		  adapter.getUIFrame().setJTreeSelectionPaths(selectedPaths);		  	  }	  
	  return true;	  //notifySelectionListener();
  }
    public static String firstCommonAncestor(ObjectAdapter node1, ObjectAdapter node2) {
	  Vector path1 = node1.getVectorPath();
	  Vector path2 = node2.getVectorPath();	  //commonPrefix(path1, path2);
	  return null;  }  public static Vector commonPrefix(Vector v1, Vector v2) {
	  Vector retVal = new Vector();	  for (int i = 0; i < v1.size() && i < v2.size(); i++) {
		  if (v1.elementAt(i).equals(v2.elementAt(i)))
			  retVal.addElement(v1.elementAt(i));	  }
	  return retVal;  }  public static Vector subtractPrefix(Vector v, Vector prefix) {	  Vector retVal = new Vector();
	  for (int i = prefix.size(); i < v.size(); i++)
		  retVal.addElement(v.elementAt(i));
	  return retVal;  }  public static Vector siblingRange(ObjectAdapter previousSelection, ObjectAdapter newSelection) {	  CompositeAdapter firstAncestor =  (CompositeAdapter) previousSelection.getParentAdapter();	  Vector selectionRange = new Vector();
	  Enumeration children = firstAncestor.getChildAdapters();	  boolean adding = false;	  ObjectAdapter child = null;
	  while (children.hasMoreElements()) {
		  child = (ObjectAdapter) children.nextElement();		  if (adding) {
			  selectionRange.addElement(child);			  if (child == previousSelection ||  child == newSelection)				  break;
		  } else if ((child == previousSelection || child == newSelection)) {			  adding = true;
			  selectionRange.addElement(child);
		  }				
	  }		  return selectionRange;
  }  // same as above except do not add the two end points.
  public static Vector siblingRangeExclusive(ObjectAdapter previousSelection, ObjectAdapter newSelection) {	  CompositeAdapter firstAncestor =  (CompositeAdapter) previousSelection.getParentAdapter();	  Vector selectionRange = new Vector();
	  Enumeration children = firstAncestor.getChildAdapters();	  boolean adding = false;	  ObjectAdapter child = null;
	  while (children.hasMoreElements()) {
		  child = (ObjectAdapter) children.nextElement();		  if (adding) {			  if (child == previousSelection ||  child == newSelection)				  break;			  else				  
				selectionRange.addElement(child);				  
		  } else if ((child == previousSelection || child == newSelection)) {			  adding = true;
		  }				
	  }		  return selectionRange;
  }
  
  public static void add(Vector v1, Vector v2) {	  for (int i = 0; i < v2.size(); i++) {
		  v1.addElement(v2.elementAt(i));	  }
  }  
  public static void extendSelectionTo(ObjectAdapter newSelection) {
	  clearColumn();
	  if (newSelection == null) return;	  if (selections.size() == 0)		  addSelection(newSelection);
	  else {		  Vector newSelections = new Vector();
		  ObjectAdapter previousSelection = (ObjectAdapter) selections.lastElement();
		  if (newSelection.getParent() == previousSelection.getParent())
			  internalReplaceSelections (siblingRange(previousSelection, newSelection));
		  else {			  Vector previousPath = previousSelection.getVectorPath();
			  Vector newPath = newSelection.getVectorPath();			  Vector commonPrefix = commonPrefix(newPath, previousPath);			  CompositeAdapter firstAncestor = (CompositeAdapter) newSelection.pathToObjectAdapter(commonPrefix);
			  Vector previousSuffix = subtractPrefix(previousPath, commonPrefix);
			  Vector newSuffix = subtractPrefix(newPath, commonPrefix);			  			  //find roots of the two subtrees  
			  ObjectAdapter newRoot = firstAncestor.getChildAdapterAtIndex((String) newSuffix.elementAt(0));
			  ObjectAdapter previousRoot = firstAncestor.getChildAdapterAtIndex((String) previousSuffix.elementAt(0));			  Vector intermediateRootNodes = siblingRangeExclusive(previousRoot, newRoot);
			  int previousRootIndex = firstAncestor.getIndex(previousRoot);
			  int newRootIndex = firstAncestor.getIndex(newRoot);			  //uiContainerAdapter firstRoot, secondRoot;		
			  ObjectAdapter firstNode, secondNode;
			  if (previousRootIndex <= newRootIndex) {
				  firstNode = previousSelection;
				  secondNode = newSelection;				  //firstRoot = previousRoot;				  //secondRoot = newRoot;
			  } else {				  firstNode = newSelection;				  secondNode = previousSelection;				  //firstRoot = newRoot;				  //secondRoot = previousRoot;
			  }
			  newSelections.addElement(firstNode);
			  downNodes(firstNode, firstAncestor, newSelections);			  add(newSelections, intermediateRootNodes);
			  upNodes(secondNode, firstAncestor, newSelections);
			  newSelections.addElement(secondNode);			  internalReplaceSelections(newSelections);		  
			  
		  }		  		  
	  }		  
	  
  }  
  public static void downNodes(ObjectAdapter startNode, ObjectAdapter untilAncestor, Vector nodes) {	   	  CompositeAdapter startParent = startNode.getParentAdapter();
	  if (startParent == null || startParent == untilAncestor)
		  return ;
	  int index = startParent.getIndex(startNode);	  
	  for (int i = index + 1; i < startParent.getChildAdapterCount(); i++)
		  nodes.addElement(startParent.getChildAdapterAt(i));
	  downNodes(startParent, untilAncestor, nodes);	  
  }
  public static void upNodes(ObjectAdapter startNode, ObjectAdapter untilAncestor, Vector nodes) {	   	  CompositeAdapter startParent = startNode.getParentAdapter();
	  if (startParent == null || startParent == untilAncestor)
		  return ;
	  int index = startParent.getIndex(startNode);	  
	  for (int i = 0; i < index; i++)
		  nodes.addElement(startParent.getChildAdapterAt(i));
	  upNodes(startParent, untilAncestor, nodes);	  
  }  
  // Method invoked when a component is selected
  // from the user interface
  public static void select(Selectable newSelection, boolean isTreeNode) {	//System.out.println("new selection" + newSelection + "lastSelection" + lastSelection + isTreeNode);
    // Check if its a toggle operation
    if (lastSelection != null && lastSelection == newSelection && !isTreeNode) {
      lastSelection.unselect();
      lastSelection = null;
      return;
    }
    // Lets just unselect the old selection first
	if (lastSelection != null) {
      lastSelection.unselect();	  if (!isTreeNode) {
		ObjectAdapter adapter = (ObjectAdapter) lastSelection;		 if (adapter.getGenericWidget() != null && 			   adapter.getGenericWidget().getUIFrame() != null)		   
		 adapter.getGenericWidget().getUIFrame().clearTreeSelection();	  }	}
    lastSelection = (ObjectAdapter) newSelection;		if (newSelection == null) return;
    // Throw away the old stack
    positionStack = new Stack();
	//System.out.println("new selection" + newSelection);	if (!isTreeNode ) {
       newSelection.select();
	   /*	   uiObjectAdapter adapter = (uiObjectAdapter) newSelection;	   if (adapter.getGenericWidget() != null && 		   adapter.getGenericWidget().getUIFrame() != null)		   
	   ((uiObjectAdapter) newSelection).getGenericWidget().getUIFrame().unselectTreeNode();		*/
	}	//System.out.println(lastSelection.getObject());
    if (MethodParameters.CopyOnSelect) 
      ObjectClipboard.set(lastSelection.getObject());	  
  }
  static CompositeAdapter columnAdapterParent;
  static CompositeAdapter columnAdapter;
  static ObjectAdapter[] column; 
  static void clearColumn() {
	  columnAdapterParent = null;
	  column = null;
	  columnAdapter = null;
  }
  public static CompositeAdapter getColumnAdapter() {
	  return columnAdapter;
  }
  public static CompositeAdapter getColumnAdapterParent() {
	  return columnAdapterParent;
  }
  public static ObjectAdapter[] getColumn() {
	  return column;
  }
  static boolean createColumn (Vector<ObjectAdapter> newRange) {
	  if (newRange.size() < 2)
		  return false;	  
	  ObjectAdapter firstSelection = (ObjectAdapter) newRange.elementAt(0);
	  CompositeAdapter grandParent = firstSelection.getGrandParentAdapter();
	  if ( grandParent == null)
		  return false;
	  if (grandParent.getNumberOfDynamicChildren() != newRange.size())
		  return false;
	  for (int i = 1; i < newRange.size(); i++) {
		  if (newRange.elementAt(i).getGrandParentAdapter() != grandParent)
			  return false;	  
		}
	  return preSelectColumn(firstSelection);
  }
  public static boolean preSelectColumn() {
	  lastSelection = (ObjectAdapter) getCurrentSelection();
	  if (lastSelection == null)
		  return false;
	  return preSelectColumn(lastSelection);
	  /*
	  columnAdapter = lastSelection.getParentAdapter();
	  if (columnAdapter == null)
		  return false;
	  columnAdapterParent = lastSelection.getGrandParentAdapter();
	  if  (columnAdapterParent == null)
		  return false;
	  return true;
	  */
  }
  static ObjectAdapter columnEntry;
  public static boolean preSelectColumn(ObjectAdapter lastSelection) {
	  if (lastSelection == null)
		  return false;
	  columnEntry = lastSelection;
	  columnAdapter = lastSelection.getParentAdapter();
	  if (columnAdapter == null)
		  return false;
	  columnAdapterParent = lastSelection.getGrandParentAdapter();
	  if  (columnAdapterParent == null)
		  return false;
	  return true;
  }  public static void selectColumn() {
	  if (!preSelectColumn()) {
		  Tracer.error("Cannot select column");
		  return;
	  }
	  setColumnVariables();
	  /*
	  column = columnAdapter.getColumnAdapters(columnAdapterParent, (uiObjectAdapter) lastSelection);
	  if (column == null) // should never really be null
		  return;
	  columnAdapterParent.select(column);
	  selectColumn(column);
	  */
	  
  }
  static void setColumnVariables() {
	  
	  column = columnAdapter.getColumnAdapters(columnAdapterParent, (ObjectAdapter) columnEntry);
	  if (column == null) // should never really be null
		  return;
	  columnAdapterParent.select(column);
	  selectColumn(column);
	  
  }


  // Method invoked on a select Up request
  public static void selectUp() {
	  
    if (lastSelection != null) {
       Selectable parent = lastSelection.getParentSelectable();
       if(parent != null) {
	 String index = parent.getChildSelectableIndex(lastSelection);
	 positionStack.push(index);
	 lastSelection = (ObjectAdapter) parent;
	 // Provide some feedback (by selecting container Component?)
	 lastSelection.select();
       }
    }
  }

  // Method invoked on a select Down request
  public static void selectDown() {
    if (lastSelection != null) {
      if (!positionStack.empty()) {
	String index = (String) positionStack.pop();
	Selectable newSelection =  lastSelection.getChildSelectable(index);
	if (newSelection != null) {
	  lastSelection.unselect();
	  lastSelection = (ObjectAdapter) newSelection;
	  lastSelection.select();
	}
      }
    }
  }
}
