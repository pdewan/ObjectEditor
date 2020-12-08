package bus.uigen.editors;

import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bus.uigen.WidgetAdapter;
import bus.uigen.uiFrame;
import bus.uigen.controller.SelectionListener;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.visitors.AssignRowColumnAdapterVisitor;
import bus.uigen.widgets.ListSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualList;

public class ListAdapter extends WidgetAdapter /*extends Frame*/
	implements ListModel, AdapterMatrix, ListSelectionListener, 
	 SelectionListener  {
	//static TableCellEditor genericTableCellEditor = new AGenericTableCellEditor();
	
	Vector matrix = new Vector();	//Vector adapterMatrix = new Vector();
	public static final String uninitCell = "";	String noLabel = "";
	//Class cellClass = String.class;	//Vector registeredClasses = new Vector();
	//Hashtable<Class, Class> registeredClasses = new Hashtable();
	int maxCol = 0;
	int size = 0;	//final int MAX_COL = 50;	//String[] colNames = new String[MAX_COL];
	//boolean[] emptyCol = new boolean[MAX_COL];
	//boolean[] customEditor = new boolean[MAX_COL];
	
	//boolean[] activeEditor = new boolean[MAX_COL];	/*
	int labelAndFixColumn (uiObjectAdapter adapter, int inputCol) {
		//if (adapter instanceof uiContainerAdapter) return inputCol;
		//not sure why this isAtomic is special cased out, commenting it
		//if (!adapter.isAtomic()) return inputCol;		//String label = adapter.columnTitle(adapter);
		String label = adapter.columnTitleJTable();		//System.err.println("Adapter: " + adapter.getID() + label);
		int colNum = inputCol;
		if (colNames[inputCol] == null) 
			colNames[inputCol] = label;
		else if (!colNames[inputCol].equals(label))
			colNames[inputCol] = "";
				return colNum;
	}
	*/
	/*	void labelColumn (uiObjectAdapter adapter, int rowNum, int inputCol) {
		//if (adapter instanceof uiContainerAdapter) return inputCol;
		//not sure why this isAtomic is special cased out, commenting it
		//if (!adapter.isAtomic()) return inputCol;
		//String label = adapter.columnTitle(adapter);
		if (hasLabels) return;
		String label = adapter.columnTitleJTable();
		//System.err.println("Adapter: " + adapter.getID() + label);
		int colNum = inputCol;
		if (colNames[inputCol] == null) 
			colNames[inputCol] = label;
		else if (!colNames[inputCol].equals(label))
			colNames[inputCol] = "";
		
		
	}
	*/	/*	public boolean hasPredefinedEditor (uiObjectAdapter adapter) {
		return adapter instanceof uiPrimitiveAdapter &&
			   !(adapter instanceof uiEnumerationAdapter);	}
	*/
	public void add(Container parent, Component comp,
			ObjectAdapter compAdapter) {
		
	}
	
	
	public void set (int rowNum, int inputColNum, ObjectAdapter adapter) {
		
		try {
				//int colNum = labelAndFixColumn(adapter, inputColNum);
			int colNum = inputColNum;// dont change the input col to move value		/*		if (maybeRegisterClass(adapter))
			customEditor[inputColNum] = true;		*/
					//maybeRegisterClass(adapter, colNum);
		//System.err.println("Assigning " + adapter.toString() + " row" + rowNum + " col " + colNum);		int matrixSize = matrix.size();		for (int i = matrixSize; i <= rowNum ; i ++)			matrix.addElement(new Vector());
		Vector row = (Vector) matrix.elementAt(rowNum);
		int rowSize = row.size();
		for (int i = rowSize; i <= colNum ; i ++)			row.addElement(uninitCell);		Object existingElement = row.get(colNum);
		
		row.setElementAt(adapter, colNum);
		adapter.setRow(rowNum);
		adapter.setColumn(colNum);		maxCol = Math.max(colNum, maxCol);
		size++;
		//labelColumn(adapter, rowNum, colNum);
		
		} catch (Exception e) {
			//System.err.println(e);
			e.printStackTrace();
		}
	}
	/*
	uiObjectAdapter getFirstFilledColumn (int colNum) {
		int numRows = getRowCount();
		for (int i = 0; i < numRows; i++) {
			uiObjectAdapter adapter = get (i, colNum);
			if (adapter != null)
				return adapter;
		}
		return null;
	}
	*/
	public ObjectAdapter get  (int rowNum, int colNum) {		if (matrix.size()  <= rowNum || rowNum < 0) return null;		
		Vector row = (Vector) matrix.elementAt(rowNum);		if (row.size() <= colNum) return null;		Object  cell = row.elementAt(colNum);		if (cell instanceof ObjectAdapter)			return (ObjectAdapter) cell;		else			return null;
		//return row.elementAt(colNum);
		
	}		uiFrame frame;
			public void init (uiFrame theFrame) {		frame = theFrame;
	}	VirtualList   virtualList;
	boolean linked = false;
	//VirtualContainer tablePanel;
	ObjectAdapter rootAdapter;
	//boolean hasLabels = false;
	//List<String> labels;
	
	public VirtualList getVirtualList() {		return virtualList;
	}
	
	
	
	public VirtualList  createVirtualTable() {
		//DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");		//rootTreeNode = this.getObjectAdapter();
		
		//jTree   = new JTree(this);
		//virtualTable  = new VirtualTable ();
		virtualList = ListSelector.createList();		return virtualList;
	}
	    //VirtualContainer parentVirtualTable = null;
    //Class virtualTableClass = null;
    
   
    
    
    VirtualComponent instantiateComponent (ClassProxy cclass) {
    	try {
    		  //rootTreeNode = this.getObjectAdapter();
    		  //frame = this.getObjectAdapter().getUIFrame();
    			//return createJTree();		
    			//virtualTable = (VirtualTable)cclass.newInstance();
    			virtualList = ListSelector.createList();
    			 if (adapter.isScrolled()) {
    				  spane = ScrollPaneSelector.createScrollPane();		  
    				  spane.setScrolledComponent(virtualList);
    				  //spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    				  return spane;
    			  }
    			return virtualList;
    			
    	    } catch (Exception e) {
    	      e.printStackTrace();
    	      //return new JPanel();
    	      return PanelSelector.createPanel();
    	    }
    	
    }
    
    //VirtualContainer c 	public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
    //try {	  	
		//virtualTable = (VirtualTable)cclass.newInstance();		//VirtualContainer c = PanelSelector.createPanel();
		/*
		parentVirtualTable = PanelSelector.createPanel();
		parentVirtualTable = parentVirtualTable;
		*/
		//createParentVirtualTable();
		//virtualTableClass = cclass;
		return instantiateComponent(cclass);
		/*		c.setLayout(new BorderLayout());		c.add(virtualTable);		c.add(virtualTable.getTableHeader(), BorderLayout.NORTH);		return c;
		*/
		/*
    } catch (Exception e) {
      e.printStackTrace();
      return new JPanel();
    }
    */
  }
	public void emptyComponent() {
	};		// if someone else provided the component, it is guaranteed to be VirtualTable	// if we cretaed it, it is a panel
	public VirtualComponent getUIComponent() {
		return virtualList;
	}
	public void linkUIComponentToMe(VirtualComponent component) {
		frame = this.getObjectAdapter().getUIFrame();
		if (component == spane)
			System.err.println("spane");
		if (component instanceof VirtualList  && virtualList != component) {
		//if (component instanceof VirtualTable && parentVirtualTable == null &&!frame.isDummy()) {
			//createParentVirtualTable();
			//linkParentVirtualTable(parentVirtualTable, (VirtualTable) component);
			virtualList = (VirtualList) component;
			if (frame != null)
				frame.addKeyListener(virtualList);
			//parentVirtualTable = virtualTable.getParent();
		}
		//else if (parentVirtualTable != component && parentVirtualTable != null) {
		
		//virtualTable = (VirtualTable) component;
		linkUIComponentToMe();
	
	}
	boolean disposed = false;
	public void cleanUp() {
		disposed = true;
		SelectionManager.removeSelectionListener(this);		
	}
		public void linkUIComponentToMe() {
	
		/*
		if (virtualTable == null)			virtualTable = (VirtualTable) component;
			*/
		try {
		rootAdapter = this.getObjectAdapter();
		//labels = rootAdapter.getLabels();
		//hasLabels = labels != null;		getObjectAdapter().setAtomic(true);
		if (frame == null) {
		frame = this.getObjectAdapter().getUIFrame();
		frame.addKeyListener(virtualList);
		}
		// steUIComponent will do this
		if (matrix.size() != 0)			refreshMatrix();
		//reLinkUIComponentToMe(component);
		reLinkUIComponentToMe (virtualList);		
				SelectionManager.addSelectionListener(this);
		
		if (spane != null) {
			setSize(spane);
		  	setColors(spane);
		} else {
			super.setAttributes(virtualList);
		}
		
				//this.refreshPreferredColumnSize();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
    
  }
	public void reLinkUIComponentToMe(VirtualComponent component) {
		if (virtualList== null) // will this ever be hit?
			virtualList = (VirtualList) component;
		//rootAdapter = this.getObjectAdapter();
		//getObjectAdapter().setAtomic(true);
		//frame = this.getObjectAdapter().getUIFrame();
		//refreshMatrix();
		//virtualTable.setCellSelectionEnabled(true);
		//defaultRowHeight = virtualTable.getRowHeight(/*rowNum*/);
		virtualList.setModel(this);		
		virtualList.clearSelection();
		//uiSelectionManager.addSelectionListener(this);
		/*
		ListSelectionModel row = virtualTable.getSelectionModel();		
		row.addListSelectionListener(this);
		*/
		virtualList.addListSelectionListener((ListSelectionListener)this);
		//virtualTable.getColumnModel().addColumnModelListener(this);
		//virtualList.addColumnModelListener(this);
		//registeredClasses = new Vector();
		//reRegisterClasses();
		
		/*
		virtualTable.addColumnSelectionInterval(0,2);
		virtualTable.addRowSelectionInterval(0, 2);
		*/
		
		
		/*
		virtualTable.changeSelection(0, 0, true, true);
		virtualTable.changeSelection(0, 1, true, true);
		*/
		//this.refreshPreferredColumnSize();
		/*
		Container parent = virtualTable.getParent();
		parent.add(virtualTable.getTableHeader(), BorderLayout.NORTH);
		*/
		//virtualTable.validate();
		//virtualTable.updateUI();
		
		//System.err.println ("Autocreate? "  + virtualTable.getAutoCreateColumnsFromModel());
		//initJTree((JTree) component);
		/*
		
		treePanel = (Container) component;
		
	  rootTreeNode = this.getObjectAdapter();
	  frame = this.getObjectAdapter().getUIFrame();
	  createJTree(treePanel); 
		*/
		
      
    
  }		public void refreshMatrix() {
		try {
		//registeredClasses = new Vector();
		maxCol = 0;
		matrix.removeAllElements();
		/*
		for (int colIndex = 0; colIndex < emptyCol.length; colIndex++) {
			emptyCol[colIndex] = false;			colNames[colIndex] = null;
		}
		*/		(new AssignRowColumnAdapterVisitor(rootAdapter, this)).traverse(0);

		//refreshPreferredColumnSize();
		//refreshPreferredRowSize(getRowCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
			}
	/*	public void refreshPreferredColumnSize() {
		int rowHeight = virtualTable.getRowHeight();		try {
			for (int colNum = 0; colNum < this.getColumnCount(); colNum++) {				//int width;								refreshPreferredColumnSize(colNum);							}
			//virtualTable.getColumnModel().getColumn(colNum).sizeWidthToFit();
		} catch (Exception e) {			System.err.println("refresh preferred column size " + e);
			e.printStackTrace();
		}	}
	*/
		Object rootObject;	public void setUIComponentValue(Object newVal) {
		setUIComponentTypedValue(newVal);	}
	public void add (VirtualContainer parent, VirtualComponent comp, ObjectAdapter childAdapter) {
		/*
		  if (!processAtomicOperations(childAdapter)) {
		  parent.add(comp);
		  setParentContainer (parent);
		  }
		  */
	  }
	void refreshAdapterCell (ObjectAdapter child) {	
		int rowNum = child.getRow();
		int colNum = child.getColumn();
		Vector row = (Vector) matrix.elementAt(rowNum);
		row.setElementAt(child, colNum);
	}
	boolean setting = false;
	public void setUIComponentTypedValue(Object newVal) {
		if (setting) return;
		setting = true;
		internalTableEvent = true;
		try {		rootObject = newVal;
		int oldNumColumns = this.getColumnCount();
		ObjectAdapter changedAdapter = getObjectAdapter() ;
		if (getObjectAdapter() instanceof CompositeAdapter)
		changedAdapter = ((CompositeAdapter) getObjectAdapter()).getChangedChildInAtomicWidget();
		//uiObjectAdapter changedAdapter = ((uiContainerAdapter) getObjectAdapter()).getChangedChildInAtomicWidget();
		
		if (changedAdapter == getObjectAdapter())			refreshMatrix();
		else
			refreshAdapterCell(changedAdapter);
		
		int newNumColumns = this.getColumnCount();
		/*
		if (oldNumColumns == newNumColumns) {			//this.refreshPreferredColumnSize();
			
			virtualTable.createDefaultColumnsFromModel();
			//this.refreshPreferredColumnSize();
			//virtualTable.updateUI();
		} else
			replaceVirtualTable();
			*/
		
		//refreshPreferredColumnSize();
		//virtualTable.setModel(this);
		//virtualTable.createDefaultColumnsFromModel();
		//virtualTable.updateUI();
		
		/*
		if (changedAdapter == getObjectAdapter())
			refreshMatrix();
		else
			refreshAdapterCell(changedAdapter);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setting = false;
		internalTableEvent = false;
	}
	public Object getUIComponentValue() {	  return rootObject;
  }	/*
	public void setUIComponentEditable() {	  
  }
	public void setUIComponentUneditable() {
	}
	*/	
	
	int minSelCol, maxSelCol, minSelRow, maxSelRow = -1;
	ObjectAdapter upperLeftSelection, upperRightSelection, lowerLeftSelection, lowerRightSelection;
	
	public boolean findAdapter (int rowNum, int uptoColNum, ObjectAdapter adapter) {
		for (int i = 0; i < uptoColNum; i++) {
			if (adapter == get(rowNum, i)) return true;
		}
		return false;
	}
	
	public Vector getRowSelections (int rowNum, int minColNum, int maxColNum) {
		Vector results = new Vector();
		if (minColNum < 0) return results;
		for (int colNum = minColNum; colNum <= maxColNum; colNum++) {
			ObjectAdapter adapter = get(rowNum, colNum);
			if (adapter != null) {
				// not sure why not always select adapter. commenting 
				results.addElement(adapter);
				
			}
			
				
		}
		return results;
	}
	public Vector filterSelections( Vector selections, Vector results) {
		//Vector results = new Vector();
		for (int selIndex = 0; selIndex < selections.size(); selIndex++) {
			ObjectAdapter adapter = (ObjectAdapter) selections.elementAt(selIndex);
			if (!adapter.isAncestor(results))
				results.addElement(adapter);			
		}
		return results;
	}
	
	public void printSelection() {
		System.err.println ("Selection. Min Row " + minSelRow + " Max Row " + maxSelRow
							+ " Min Col " + minSelCol + " Max Col " + maxSelCol);
		//virtualTable.clearSelection();
	}
		boolean internalTableEvent = false;
	//boolean externalSelectionEvent = false;
	boolean treeSelected = false;
	public void processSelectionEvent() {		if (internalTableEvent) {
			//internalTableEvent   = false;
			return;
		}		Vector filteredSelections = new Vector();
		if (minSelRow < 0) return;
		if (minSelCol < 0) return;		for (int rowNum =minSelRow; rowNum <= maxSelRow; rowNum ++) {
			Vector rowSelections = getRowSelections(rowNum, minSelCol, maxSelCol);
			filterSelections(rowSelections, filteredSelections);		}
		if (filteredSelections.size() == 0) return;
		internalTableEvent = true;
		//virtualTable.clearSelection();		/*
		minSelRow = -1;
		maxSelRow = -1;
		minSelCol = -1;
		maxSelCol = -1;
		*/
			SelectionManager.replaceSelections(filteredSelections);		internalTableEvent = false;
	}
	/*
	public  void setUIComponentSelected(uiObjectAdapter[] column) {
		
		clearSelection();
		 //externalSelectionEvent = true;
		 for (int i = 0; i < column.length; i++ ) {
			 int rowNum = column[i].getRow();
			 int colNum = column[i].getColumn();
			 if (rowNum < -1 || colNum < -1)
				 continue;
			 select (rowNum, colNum);
		 }
		 //externalSelectionEvent = false;
	 }
	  public  void setUIComponentDeselected(uiObjectAdapter[] child) {
		  
	  }
	  */
	public void columnSelectionChanged(ListSelectionEvent e)  {
		if (disposed) return;
		if (internalTableEvent) {
			//internalTableEvent   = false;
			return;
		}
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (minSelCol == lsm.getMinSelectionIndex() && maxSelCol == lsm.getMaxSelectionIndex()) return;
		minSelCol = lsm.getMinSelectionIndex();
		maxSelCol = lsm.getMaxSelectionIndex();		//System.err.println("Col Event");		//printSelection();		processSelectionEvent();
		/*		System.err.println("New selected col: " + min + " lead " + colNum);
		System.err.println("Selected index. minx: " + lsm.getMinSelectionIndex() + " max " + lsm.getMaxSelectionIndex());
		*/		
	}	public void valueChanged(ListSelectionEvent e)  {
		if (updatingUI) return;
		if (disposed) return;
		if (internalTableEvent) return;
		if (e.getValueIsAdjusting()) return;		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (minSelRow == lsm.getMinSelectionIndex() && maxSelRow == lsm.getMaxSelectionIndex()) return;		minSelRow = lsm.getMinSelectionIndex();
		maxSelRow = lsm.getMaxSelectionIndex();		processSelectionEvent();
			
	
}	int saveMinSelCol, saveMaxSelCol;		void saveSelCol() {
		saveMinSelCol = minSelCol;		saveMaxSelCol = maxSelCol;	}	void restoreSelCol() {
		minSelCol = saveMinSelCol;		maxSelCol = saveMaxSelCol;	}		void clearSelection() {
		saveSelCol();
		virtualList.clearSelection();
		restoreSelCol();	}
	boolean updatingUI;
	public void singleItemSelected() {
		updatingUI = true;
		/*		if (externalSelectionEvent)
			return;
			*/
		clearSelection();		updateSelections();
		updatingUI = false;
	}	public void noItemSelected() {		clearSelection();
	}	public void multipleItemsSelected() {
		JList list = (JList) virtualList.getPhysicalComponent();		
		//System.err.println ("Column Selection Allowed" + lis.getColumnSelectionAllowed());
		internalTableEvent = true;
		//clearSelection();
		/*
		select (0, 0);
		select (1, 0);
		select (0, 0);
		select (1, 0);
		*/
		
		//clearSelection();
		//table.changeSelection(0, 1, true, true);
		/*
		virtualTable.addRowSelectionInterval(0, getRowCount() - 1);
		virtualTable.addColumnSelectionInterval(0, 0);
		virtualTable.updateUI();
		*/
		
		
		clearSelection();
		updateSelections();
		
		internalTableEvent = false;
		
		
	}	public void select (int rowNum, int colNum) {
		virtualList.setSelectedIndex(rowNum);
		/*
		virtualTable.addColumnSelectionInterval(colNum ,colNum);		virtualTable.addRowSelectionInterval(rowNum, rowNum);
		*/			}
	
	public void select (ObjectAdapter adapter, int stopRow) {		int startRowNum = adapter.getRow();
		if (startRowNum < 0) return;		int startColNum = adapter.getColumn();		if (startColNum < 0) return;
		for (int rowNum = adapter.getRow(); rowNum < stopRow; rowNum++) {
			for (int colNum = 0; colNum < getColumnCount(); colNum++) {				ObjectAdapter child = get (rowNum, colNum);
				if (child != null) {					if (child.isAncestor(adapter))						select(rowNum, colNum); 
				}				
			}				
		}		
	}
	public Vector filterNonCells (Vector adapters) {
		Vector results = new Vector();		for (int i = 0; i < adapters.size(); i ++) {
			ObjectAdapter adapter = (ObjectAdapter) adapters.elementAt(i);			if (adapter == null) continue;
			if (adapter.getRow() >= 0 || adapter.getRow() < this.getRowCount() 
				|| adapter.getColumn() >=  0 || adapter.getColumn() < this.getColumnCount())
				results.addElement(adapter);		}		return results;
	}
	
	public void updateSelections() {		Vector selections = filterNonCells (SelectionManager.getSelections());
		//virtualTable.setRowSelectionAllowed(false);
		//virtualTable.setColumnSelectionAllowed(false);		for (int i = 0; i < selections.size(); i++) {
			select ((ObjectAdapter) selections.elementAt(i), getRowCount());		}
		//virtualTable.setRowSelectionAllowed(true);
		//virtualTable.setColumnSelectionAllowed(true);
	}	
	/*	public void valueChanged(TreeSelectionEvent e)  {
				// suppress duplicate   event from replaceTreeSelections     
		if (internalTreeEvent) {
			internalTreeEvent   = false;
			return;
		}
				TreePath treePath   = jTree.getSelectionPath();		TreePath eventPath = e.getNewLeadSelectionPath();		TreePath eventPathOld   = e.getOldLeadSelectionPath();
		if (treePath == null)   {			//System.err.println("null selection");
			return;
		}		uiObjectAdapter treeNode = (uiObjectAdapter) treePath.getLastPathComponent();
		
		uiObjectAdapter newTreeNode =   (uiObjectAdapter) eventPath.getLastPathComponent();		//System.err.println("calling   select" + treeNode);
		TreePath[] selectedPaths = jTree.getSelectionPaths();
		Vector selectedTreeNodes = new Vector();		for (int i = 0; i   < selectedPaths.length; i++) {
			//System.err.println("no:" + i + "node" +   selectedPaths[i]);
			selectedTreeNodes.addElement((uiObjectAdapter)selectedPaths[i].getLastPathComponent());		}
		//System.err.println(selectedNodes);		treeSelected = true;
		uiSelectionManager.replaceSelections(selectedTreeNodes);
		if (frame != null)		frame.setTitle();
				
		//uiSelectionManager.replaceSelections((uiObjectAdapter)createdTreePath.getLastPathComponent());		
		
				//uiSelectionManager.select(treeNode,   true);		//uiSelectionManager.replaceSelections(treeNode);
		//treeNode.uiComponentFocusGained();		//replaceFrame();
		//System.err.println("TreeNode is   " + treeNode);
		
			}
	public void clearTreeSelection() {
		if (jTree   == null) return;		jTree.clearSelection();
	}	boolean internalTreeEvent   = false;
	public void setJTreeSelectionPaths(TreePath[]   selectedPaths) {		
		
		//jTree.clearSelection();     
		//System.err.println("Selected Paths"   + selectedPaths);
		
		//TreePath createdTreePath = jTree.getPathForLocation(0,0);		//System.err.println(createdTreePath);		//createdTreePath.pathByAddingChild(topAdapter);
		//System.err.println(selectedPaths[0]);
		//jTree.setSelectionPaths(selectedPaths);		//jTree.setSelectionPath(createdTreePath);
		internalTreeEvent   = true;
				if ((jTree !=   null) && frame.treePanelIsVisible())			jTree.setSelectionPaths(selectedPaths);
				
	}	public void mouseClicked(MouseEvent e) {		
		if ((e.getClickCount() == 2) ) {
			TreePath treePath = jTree.getSelectionPath();			if (treePath == null) return;
			System.err.println(treePath);			uiObjectAdapter treeNode = (uiObjectAdapter) treePath.getLastPathComponent();
			if (!uiMethodInvocationManager.invokeDoubleClickMethod(treeNode))				frame.replaceFrame(treeNode);
		}		
		//if (listener !=   null) 
		//listener.uiComponentFocusGained();
	}	
	public void mousePressed(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public  void mouseExited(MouseEvent e) {
	}
	Vector  treeModelListeners = new Vector();	public void addTreeModelListener(TreeModelListener l)   {
		treeModelListeners.addElement(l);	}	public void removeTreeModelListener(TreeModelListener   l) {
		treeModelListeners.removeElement(l);	}	public Object   getChild(Object parent, int index){
		return  ((TreeNode) parent).getChildAt(index);	}
	public void treeStructureChanged () {		Object[] path   = {getRoot()};
		for (Enumeration elements   = treeModelListeners.elements();
			 elements.hasMoreElements();)			((TreeModelListener) elements.nextElement()).treeStructureChanged(new   TreeModelEvent(this,path )); 	}
		public int getChildAdapterCount(Object parent) {
		if(ObjectEditor.colabMode() && ObjectEditor.coupleElides()){			frame.getChildAdapterCountReq = new AutomaticRefresh(frame,(uiObjectAdapter) parent);		} else if (ObjectEditor.colabMode()){
			frame.getChildAdapterCountReq = new AutomaticRefresh(frame,((uiObjectAdapter) parent).getPath(),"getChildAdapterCount");		}
		int retVal = subgetChildAdapterCount(parent);
		frame.getChildAdapterCountReq = null;		return(retVal);
	}
	public int subgetChildAdapterCount(Object parent){		return ((TreeNode) parent).getChildAdapterCount();	}	public int getIndexOfChild(Object   parent, Object child)  {
		return ((TreeNode) parent).getIndex((TreeNode) child);	}	
	public Object   getRoot() {
		return rootTreeNode;
	}
	
	public boolean isLeaf(Object node)  {
		return ((TreeNode) node).isLeaf();
	}
	public void valueForPathChanged(TreePath path, Object   newValue)  {
	}
	
	public void treeNodesChanged(TreeModelEvent e) {
	}
	public void treeNodesInserted(TreeModelEvent e) {
	}
	public void treeNodesRemoved(TreeModelEvent e) {
	}
	public void treeStructureChanged(TreeModelEvent e) {
	}		*/		 int defaultRowHeight;	boolean firstTime = false;
	//int originalRowH
			

         
	public int getColumnCount() {
		if (getRowCount() == 0)
			return 0;
		int retVal = maxCol + 1;
		return retVal;
		//return maxCol + 1;
		/*
		if (getRowCount() == 0) return 0;
		return((Vector) matrix.elementAt(0)).size();	
		*/
	}  
	
	public int getRowCount() {
		int retVal = matrix.size();
		return retVal;
		
		//return matrix.size();
	};
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		ObjectAdapter adapter =  get (rowIndex, columnIndex);
		if (adapter != null) {
			return adapter.getRealObject();
			
		} else
			return uninitCell;
		
	};
	
	//public void removeTableModelListener(TableModelListener l) {};
	/*
	public void setValueAt(Object aValue, int rowIndex, int columnIndex){
		if (getValueAt(rowIndex, columnIndex).equals(aValue))
			return;
		uiObjectAdapter adapter =  get (rowIndex, columnIndex);
		if (adapter != null)
			if (aValue.getClass() == String.class)
				adapter.setCompleteUserObject(aValue);
			else
				adapter.setUserTypedObject(aValue);
	};
	*/
	/*
	Vector cellEditorListeners = new Vector();
	public void addCellEditorListener(CellEditorListener l) {
		if (!hasLabels)
			this.refreshPreferredColumnSize();
		this.refreshPreferredRowSize(getRowNum);
		cellEditorListeners.addElement(l);
		//System.err.println("leaving register listener");
		
		//refreshPreferredColumnSize();
		//virtualTable.updateUI();
	};
          
  public void cancelCellEditing() {
	  
	  for (int i = 0; i < cellEditorListeners.size(); i++) {
		  ((CellEditorListener) cellEditorListeners.elementAt(i)).editingCanceled(new ChangeEvent(this));
	  }
	  
  };
          
 public Object getCellEditorValue()  {
	//refreshPreferredColumnSize();
	 return getValue; 
 }
 public  boolean isCellEditable(EventObject anEvent) {
	 //this.refreshPreferredColumnSize(getColumnNum);
	 //this.refreshPreferredRowSize(getRowNum);
	
	 //refreshPreferredColumnSize();
	 //virtualTable.updateUI();
	 return isCellEditable(getRowNum, getColumnNum);
	//return true;
 }
 
 public void removeCellEditorListener(CellEditorListener l){
	 cellEditorListeners.removeElement(l);
	 
 };
 public boolean shouldSelectCell(EventObject anEvent){
	return true;
 };
 public boolean stopCellEditing() {
	 	 getColumnNum = -1;	 getRowNum = -1;
	 for (int i = 0; i < cellEditorListeners.size(); i++) {
		  ((CellEditorListener) cellEditorListeners.elementAt(i)).editingStopped(new ChangeEvent(this));
	  }
	  	 return true;
 }
 Object getValue;
 int getRowNum = -1;
 int getColumnNum = -1; public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	 try {
		 System.err.println("getTableCellEditor called");
	 getValue = value;
	 getRowNum = row;
	 getColumnNum = column;
	 //activeEditor[column] = true;
	 uiObjectAdapter adapter = get(row, column);
	 return (Component) adapter.getUIComponent().getPhysicalComponent();	 
	 } catch (Exception e) {
		 System.err.println("getTableCelleditor " + e);
		 return new TextField("Error Component");
		 //return TextFieldSelector.createTextField("Error Component");
	 }
 }
 
 boolean activeEditor (int rowNum, int colNum) {
	 return rowNum == getRowNum && colNum == getColumnNum;
 }
 boolean activeEditor (int colNum) {
	 return colNum == getColumnNum;
 }
 boolean activeRowEditor (int rowNum) {
	 return rowNum == getRowNum;
 }
 */
	public void addListDataListener(ListDataListener l) {
		
	}
	public void removeListDataListener(ListDataListener l) {
		
	}
	public Object getElementAt(int i) {
		int rowNum = i/getColumnCount();
		int colNum = i - rowNum*getColumnCount();
		return get(rowNum, colNum);
		
	}
	public int getSize() {
		return size;
	}
	public  void remove(ObjectAdapter compAdapter) {
		
	}

}
