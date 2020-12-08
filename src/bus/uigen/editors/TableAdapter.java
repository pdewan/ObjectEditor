package bus.uigen.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import util.models.ListenableString;
import util.models.VectorChangeEvent;
import util.models.VectorListenable;
import util.models.VectorListener;
import bus.uigen.WidgetAdapter;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.SelectionListener;
import bus.uigen.controller.SelectionManager;
import bus.uigen.introspect.Attribute;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.EnumerationAdapter;
import bus.uigen.oadapters.HashtableAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.visitors.AssignRowColumnAdapterVisitor;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.table.TableSelector;
import bus.uigen.widgets.table.VirtualTable;

public class TableAdapter extends WidgetAdapter /*extends Frame*/
	implements TableModel, AdapterMatrix, ListSelectionListener, 
	TableColumnModelListener, SelectionListener, TableCellEditor, MouseListener, VectorListener {
	static TableCellEditor genericTableCellEditor = new AGenericTableCellEditor();
	
	Vector matrix = new Vector();	//Vector adapterMatrix = new Vector();
	public static final String uninitCell = "";	String noLabel = "";
	Class cellClass = String.class;	//Vector registeredClasses = new Vector();
	Hashtable<Class, Class> registeredClasses = new Hashtable();
	int maxCol = 0;	final int MAX_COL = 50;	String[] colNames = new String[MAX_COL];
	boolean[] emptyCol = new boolean[MAX_COL];
	boolean[] customEditor = new boolean[MAX_COL];
	
	//boolean[] activeEditor = new boolean[MAX_COL];	
	int labelAndFixColumn (ObjectAdapter adapter, int inputCol) {
		//if (adapter instanceof uiContainerAdapter) return inputCol;
		//not sure why this isAtomic is special cased out, commenting it
		//if (!adapter.isAtomic()) return inputCol;		//String label = adapter.columnTitle(adapter);
		String label = adapter.columnTitleJTable();		//System.err.println("Adapter: " + adapter.getID() + label);
		int colNum = inputCol;
		if (colNames[inputCol] == null) 
			colNames[inputCol] = label;
		else if (!colNames[inputCol].equals(label))
			colNames[inputCol] = "";
		/*
		int colNum;		for (colNum = inputCol; 			 colNames[colNum] != null && 
			     !colNames[colNum].equals(label) && 
				  colNum < MAX_COL;			 colNum++);		colNames[colNum] = label;
		*/		/*
		if (inputCol != colNum)
			System.err.println("Fixed " + inputCol + " to " + colNum);
		*/		return colNum;
	}	void labelColumn (ObjectAdapter adapter, int rowNum, int inputCol) {
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
		/*
		int colNum;
		for (colNum = inputCol; 
			 colNames[colNum] != null && 
			     !colNames[colNum].equals(label) && 
				  colNum < MAX_COL;
			 colNum++);
		colNames[colNum] = label;
		*/
		/*
		if (inputCol != colNum)
			System.err.println("Fixed " + inputCol + " to " + colNum);
		*/
		
	}
	
		public boolean hasPredefinedEditor (ObjectAdapter adapter) {
		return adapter instanceof PrimitiveAdapter &&
			   !(adapter instanceof EnumerationAdapter);	}
	public void add(Container parent, Component comp,
			ObjectAdapter compAdapter) {
		
	}
	void reRegisterClasses() {
		
		Enumeration<Class> keys = registeredClasses.keys();
		while (keys.hasMoreElements()) {
			try {
				Class objectClass = keys.nextElement();
				Class<TableCellEditor> tableEditor = registeredClasses.get(objectClass);
				TableCellEditor cellEditor = tableEditor.newInstance();
				//virtualTable.setDefaultEditor(objectClass, cellEditor);
				virtualTable.setDefaultEditor(objectClass, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	void maybeAddVectorListener(ObjectAdapter adapter) {
		//if (adapter.getViewObject()  instanceof VectorListenable)
		if (adapter.getViewObject()  instanceof ListenableString)
			((VectorListenable) adapter.getViewObject()).addVectorListener(this);
			
			
	}
	public boolean maybeRegisterClass(ObjectAdapter adapter, int colNum) {
		if (adapter instanceof EnumerationAdapter) {
		
				//if (adapter.isAtomic() && !hasPredefinedEditor(adapter)) {			customEditor[colNum] = true;
			ClassProxy objClass = adapter.getPropertyClass();
			//adapter.setHasCellEditor(true);			if (adapter.getWidgetAdapter() == null) {
				//adapter.processPreferredWidget();				adapter.linkToComponent();
				
				//adapter.getWidgetAdapter().setUIComponentValue(adapter.getValue());			}
			if (!this.registeredClasses.contains(objClass)) {				//virtualTable.setDefaultEditor(enumClass, new bus.uigen.adapters.JComboBoxAdapter());
				if (virtualTable == null) return true;
				if (!(objClass instanceof AClassProxy))
					return true;
				Class javaClass = ((AClassProxy) objClass).getJavaClass();
				virtualTable.setDefaultEditor(javaClass, this);
				
				//virtualTable.setDefaultEditor(objClass, new bus.uigen.adapters.JComboBoxAdapter());				//registeredClasses.addElement(objClass);
				registeredClasses.put(javaClass, bus.uigen.adapters.ComboBoxAdapter.class);				return true;
			}			return false;
					}		else {
			customEditor[colNum] = false;			return false;		}
		
				}
	/*	public void linkAdapterToEditor (int rowNum, int inputColNum, uiObjectAdapter adapter) {
		//maybeRegisterEnum(adapter);		//int colNum = labelAndFixColumn(adapter, inputColNum);
		//System.err.println("Assigning " + adapter.toString() + " row" + rowNum + " col " + colNum);		int matrixSize = matrix.size();		for (int i = matrixSize; i <= rowNum ; i ++)			matrix.addElement(new Vector());
		Vector row = (Vector) matrix.elementAt(rowNum);
		int rowSize = row.size();
		for (int i = rowSize; i <= colNum ; i ++)			row.addElement(uninitCell);		
		row.setElementAt(adapter, colNum);
		adapter.setRow(rowNum);
		adapter.setColumn(colNum);		maxCol = Math.max(colNum, maxCol);
		
	}
	public uiObjectAdapter getLinkedAdapter (int rowNum, int colNum) {		if (matrix.size() <= rowNum) return null;		
		Vector row = (Vector) matrix.elementAt(rowNum);		if (row.size() <= colNum) return null;		Object  cell = row.elementAt(colNum);		if (cell instanceof uiObjectAdapter)			return (uiObjectAdapter) cell;		else			return null;
		//return row.elementAt(colNum);
		
	}	*/
	void refreshAdapterCell (ObjectAdapter child) {	
		int rowNum = child.getRow();
		int colNum = child.getColumn();
		Vector row = (Vector) matrix.elementAt(rowNum);
		row.setElementAt(child, colNum);
	}
	public void set (int rowNum, int inputColNum, ObjectAdapter adapter) {
		
		try {
		/*	
		if (!adapter.isAtomic() && adapter.getKeyAdapter() != null)
			adapter = adapter.getKeyAdapter();	
			*/			//int colNum = labelAndFixColumn(adapter, inputColNum);
			int colNum = inputColNum;// dont change the input col to move value		/*		if (maybeRegisterClass(adapter))
			customEditor[inputColNum] = true;		*/
		maybeAddVectorListener(adapter);			maybeRegisterClass(adapter, colNum);
		//System.err.println("Assigning " + adapter.toString() + " row" + rowNum + " col " + colNum);		int matrixSize = matrix.size();		for (int i = matrixSize; i <= rowNum ; i ++)			matrix.addElement(new Vector());
		Vector row = (Vector) matrix.elementAt(rowNum);
		int rowSize = row.size();
		for (int i = rowSize; i <= colNum ; i ++)			row.addElement(uninitCell);		Object existingElement = row.get(colNum);
		if (!existingElement.equals(uninitCell))
			colNames[colNum] = null;
		row.setElementAt(adapter, colNum);
		adapter.setRow(rowNum);
		adapter.setColumn(colNum);
//		for (int columnIndex = maxCol; columnIndex < colNum; columnIndex++) {
//			virtualTable.addColumn();
//		}		maxCol = Math.max(colNum, maxCol);
		labelColumn(adapter, rowNum, colNum);
		/*		Class newCellClass = adapter.getPropertyClass();		if (cellClass == null)			cellClass = newCellClass;		else			if (cellClass != newCellClass)				cellClass = 		*/
		} catch (Exception e) {
			//System.err.println(e);
			e.printStackTrace();
		}
	}
	ObjectAdapter getFirstFilledColumn (int colNum) {
		int numRows = getRowCount();
		for (int i = 0; i < numRows; i++) {
			ObjectAdapter adapter = get (i, colNum);
			if (adapter != null)
				return adapter;
		}
		return null;
	}
	public ObjectAdapter get  (int rowNum, int colNum) {		if (matrix.size()  <= rowNum || rowNum < 0) return null;		
		Vector row = (Vector) matrix.elementAt(rowNum);		if (row.size() <= colNum) return null;		Object  cell = row.elementAt(colNum);		if (cell instanceof ObjectAdapter)			return (ObjectAdapter) cell;		else			return null;
		//return row.elementAt(colNum);
		/*		Class newCellClass = adapter.getPropertyClass();		if (cellClass == null)			cellClass = newCellClass;		else			if (cellClass != newCellClass)				cellClass = 		*/
	}		uiFrame frame;
			public void init (uiFrame theFrame) {		frame = theFrame;
	}	VirtualTable   virtualTable;
	boolean linked = false;
	VirtualContainer tablePanel;
	ObjectAdapter rootAdapter;
	boolean hasLabels = false;
	List<String> labels;
	
	public VirtualTable getVirtualTable() {		return virtualTable;
	}
	public VirtualComponent getTablePanel() {		return tablePanel;
	}
	
	public void createVirtualTable(VirtualContainer tablePanel) {		/*
		//DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");		DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);		treeModel.addTreeModelListener(this);
		//jTree   = new JTree(this);
		jTree   = new JTree(treeModel);			treePanel.add(jTree);
			jTree.setEditable(true);						jTree.addMouseListener(this);			jTree.addTreeSelectionListener(this);
			jTree.addTreeExpanssionListener(this);
		*/			tablePanel.add(createVirtualTable());
	}
	public VirtualTable  createVirtualTable() {
		//DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");		//rootTreeNode = this.getObjectAdapter();
		
		//jTree   = new JTree(this);
		//virtualTable  = new VirtualTable ();
		virtualTable = TableSelector.createTable();		return virtualTable;
	}
	    VirtualContainer parentVirtualTable = null;
    ClassProxy virtualTableClass = null;
    void replaceVirtualTable() { 
    	if ((parentVirtualTable == null) || (virtualTableClass == null))
    		return;
    	emptyParentContainer();
    	VirtualComponent c = instantiateComponent(virtualTableClass, parentVirtualTable, getObjectAdapter());
    	//virtualTable.setCellSelectionEnabled(true);
    	reLinkUIComponentToMe(c);
    	//this.refreshPreferredColumnSize();
    	
    }
   
    
    void emptyParentContainer() {
    	if (parentVirtualTable != null) {
    		parentVirtualTable.removeAll();
    	}
    	
    }
    VirtualComponent linkParentVirtualTable(VirtualContainer c, VirtualTable virtualTable) {
    	c.setLayout(new BorderLayout());
		c.add(virtualTable);
		c.add(virtualTable.getTableHeader(), BorderLayout.NORTH);
		return c;
    	
    	
    	
    }
   // VirtualScrollPane spane;
    VirtualComponent instantiateComponent (ClassProxy cclass, VirtualContainer c, ObjectAdapter adapter) {
    	try {
    		  //rootTreeNode = this.getObjectAdapter();
    		  //frame = this.getObjectAdapter().getUIFrame();
    			//return createJTree();		
    			//virtualTable = (VirtualTable)cclass.newInstance();
    			virtualTable = TableSelector.createTable();
    			//Container c = PanelSelector.createPanel();
    			//parent = c;
    			//return linkParentVirtualTable (c, virtualTable);
    			VirtualComponent retVal = linkParentVirtualTable (c, virtualTable);
    			if (adapter.isScrolled()) {
    				   spane = ScrollPaneSelector.createScrollPane();		  
    				  spane.setScrolledComponent(retVal);
    				  //spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    				  return spane;
    			  }
    			return retVal;
    			
    			/*
    			c.setLayout(new BorderLayout());
    			c.add(virtualTable);
    			c.add(virtualTable.getTableHeader(), BorderLayout.NORTH);
    			return c;
    			*/
    			//virtualTable = new VirtualTable(this);
    			//System.err.println (virtualTable.getAutoCreateColumnsFromModel());
    			//JPanel parentPanel = new JPanel();
    			//Janel childPanel = new JPanel();
    		  //return (Container) cclass.newInstance();
    			//return virtualTable;
    			
    			//return new JTree();
    	      //return treePanel;
    	    } catch (Exception e) {
    	      e.printStackTrace();
    	      //return new JPanel();
    	      return PanelSelector.createPanel();
    	    }
    	
    }
    void createParentVirtualTable() {
    	parentVirtualTable = PanelSelector.createPanel();
    }
    //VirtualContainer c 	public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
    //try {	  	
		//virtualTable = (VirtualTable)cclass.newInstance();		//VirtualContainer c = PanelSelector.createPanel();
		/*
		parentVirtualTable = PanelSelector.createPanel();
		parentVirtualTable = parentVirtualTable;
		*/
		createParentVirtualTable();
		virtualTableClass = cclass;
		return instantiateComponent(cclass, parentVirtualTable, adapter);
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
	};	/*
	public Component getUIComponent() {
		return storeomponent;
	}
	*/	// if someone else provided the component, it is guaranteed to be VirtualTable	// if we cretaed it, it is a panel
	public VirtualComponent getUIComponent() {
		return parentVirtualTable;
	}
	public void linkUIComponentToMe(VirtualComponent component) {
		frame = this.getObjectAdapter().getUIFrame();
		if (component instanceof VirtualTable && parentVirtualTable == null  && virtualTable != component) {
		//if (component instanceof VirtualTable && parentVirtualTable == null &&!frame.isDummy()) {
			//createParentVirtualTable();
			//linkParentVirtualTable(parentVirtualTable, (VirtualTable) component);
			virtualTable = (VirtualTable) component;
			if (frame != null)
				frame.addKeyListener(virtualTable);
			parentVirtualTable = virtualTable.getParent();
		}
		//else if (parentVirtualTable != component && parentVirtualTable != null) {
		else if (parentVirtualTable != component && spane == null ) {
		parentVirtualTable.remove(virtualTable);
		parentVirtualTable = (VirtualContainer) component;
		parentVirtualTable.add(virtualTable);
		}
		if (spane == null)
		super.setAttributes(virtualTable);
		else {
			setSize(spane);
		  	setColors(spane);
		}
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
		labels = rootAdapter.getLabels();
		hasLabels = labels != null;		getObjectAdapter().setAtomic(true);
		if (frame == null) {
		frame = this.getObjectAdapter().getUIFrame();
		frame.addKeyListener(virtualTable);
		}
		// steUIComponent will do this
		if (matrix.size() != 0)			refreshMatrix();
		//reLinkUIComponentToMe(component);
		reLinkUIComponentToMe (virtualTable);
		/*		virtualTable.setCellSelectionEnabled(true);		defaultRowHeight = virtualTable.getRowHeight();
		virtualTable.setModel(this);				virtualTable.clearSelection();
		*/		SelectionManager.addSelectionListener(this);
		
		/*
		ListSelectionModel row = virtualTable.getSelectionModel();		
		row.addListSelectionListener(this);
		virtualTable.getColumnModel().addColumnModelListener(this);
		*/		
				/*
		virtualTable.addColumnSelectionInterval(0,2);		virtualTable.addRowSelectionInterval(0, 2);		*/		
				/*
		virtualTable.changeSelection(0, 0, true, true);
		virtualTable.changeSelection(0, 1, true, true);
		*/		this.refreshPreferredColumnSize();
		/*		Container parent = virtualTable.getParent();		parent.add(virtualTable.getTableHeader(), BorderLayout.NORTH);		*/
		//virtualTable.validate();		//virtualTable.updateUI();		
		//System.err.println ("Autocreate? "  + virtualTable.getAutoCreateColumnsFromModel());		//initJTree((JTree) component);
		/*		
		treePanel = (Container) component;			  rootTreeNode = this.getObjectAdapter();
	  frame = this.getObjectAdapter().getUIFrame();	  createJTree(treePanel); 
		*/		
		} catch (Exception e) {
			e.printStackTrace();
		}
    
  }
	public void reLinkUIComponentToMe(VirtualComponent component) {
		if (virtualTable == null) // will this ever be hit?
			virtualTable = (VirtualTable) component;
		//rootAdapter = this.getObjectAdapter();
		//getObjectAdapter().setAtomic(true);
		//frame = this.getObjectAdapter().getUIFrame();
		//refreshMatrix();
		//virtualTable.setCellSelectionEnabled(true);
		defaultRowHeight = virtualTable.getRowHeight(/*rowNum*/);
		virtualTable.setModel(this);
		virtualTable.addMouseListener(this);
		virtualTable.clearSelection();
		//uiSelectionManager.addSelectionListener(this);
		/*
		ListSelectionModel row = virtualTable.getSelectionModel();		
		row.addListSelectionListener(this);
		*/
		virtualTable.addListSelectionListener((ListSelectionListener)this);
		//virtualTable.getColumnModel().addColumnModelListener(this);
		virtualTable.addColumnModelListener(this);
		//registeredClasses = new Vector();
		reRegisterClasses();
		
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
		for (int colIndex = 0; colIndex < emptyCol.length; colIndex++) {
			emptyCol[colIndex] = false;			colNames[colIndex] = null;
		}		(new AssignRowColumnAdapterVisitor(rootAdapter, this)).traverse(0);

		//refreshPreferredColumnSize();
		//refreshPreferredRowSize(getRowCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
			}
		public void refreshPreferredColumnSize() {
		int rowHeight = virtualTable.getRowHeight();		try {
			for (int colNum = 0; colNum < this.getColumnCount(); colNum++) {				//int width;				/*
				if (customEditor[colNum]) {					Component c = get (0, colNum).getUIComponent();					width = c.getPreferredSize().width;
					rowHeight = Math.max(c.getPreferredSize().height, rowHeight);					virtualTable.setRowHeight(rowHeight);
				} else						width = this.getMaxColumnChars(colNum)*CHAR_WIDTH;			
				*/				refreshPreferredColumnSize(colNum);				/*
				virtualTable.getColumnModel().getColumn(colNum).setPreferredWidth(							Math.min(getMaxColumnWidth(colNum), MAX_WIDTH));
				*/			}
			//virtualTable.getColumnModel().getColumn(colNum).sizeWidthToFit();
		} catch (Exception e) {			System.err.println("refresh preferred column size " + e);
			e.printStackTrace();
		}	}
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
		
//		refreshPreferredColumnSize(); 
		if (virtualTable.getModel() != this)
		virtualTable.setModel(this); // this was commented out
		virtualTable.createDefaultColumnsFromModel();
		refreshPreferredColumnSize(); // moved this down

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
	*/	public void columnAdded(TableColumnModelEvent e) {
	}
         
	public void columnMarginChanged(ChangeEvent e) {
	}
    int selectedColumn = -1;     
	public void columnMoved(TableColumnModelEvent e) {
		if (disposed || !(getObjectAdapter() instanceof VectorAdapter || getObjectAdapter() instanceof HashtableAdapter)) return;
		int columnNum1 = e.getFromIndex();
		int columnNum2 = e.getToIndex();
		if (columnNum1 != columnNum2)
			return;
		if (selectedColumn == columnNum1)
			return;
		selectedColumn = columnNum1;
		sortColumn(selectedColumn);
		//uiObjectAdapter adapter = getFirstFilledColumn(columnNum1);
	}
	
	void sortColumn (int colNum) {
		if (colNum < 0 || colNum >= getColumnCount())
			return;
		/*
		String fieldName = colNames[colNum];
		if (fieldName == null)
			return;
			*/
		ObjectAdapter adapter = getFirstFilledColumn(colNum);
		CompositeAdapter.sort((CompositeAdapter) getObjectAdapter(), adapter);
		/*
		if (adapter == null)
			return;
		String fieldName = adapter.getPropertyName();
		uiObjectAdapter parentAdapter = adapter.getParentAdapter();
		if (parentAdapter == null)
			return;
		Class parentClass = parentAdapter.getViewObject().getClass();
		if (parentClass == null)
			return;		
		ObjectEditor.setAttribute(parentClass, AttributeNames.SORT_PROPERTY, fieldName);
		//ViewInfo cd = ClassDescriptorCache.getClassDescriptor(parentClass);
		uiAttributeManager environment = AttributeManager.getEnvironment();
		  if (environment == null) 
			  return;
		  environment.removeFromAttributeLists(parentClass.getName());
		  uiGenerator.deepProcessAttributes(getObjectAdapter()); 
		  uiGenerator.deepElide(getObjectAdapter());
		  getObjectAdapter().getUIFrame().validate();
		  */
		
	}
	public void columnRemoved(TableColumnModelEvent e) {
	}
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
				/*
				
				uiObjectAdapter parent = adapter.getParentAdapter();
				if (parent != null && !findAdapter(rowNum, colNum, parent))	 {			
					results.addElement(parent);
					parent.setRow(rowNum);
					parent.setColumn(0);
				} else
					results.addElement(adapter);
		       */
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
		}
		int leadRowNum = virtualTable.getSelectedRow();
		int leadColNum = virtualTable.getSelectedColumn();	
		if (leadRowNum < 0 || (leadColNum < 0))
			return;		Vector filteredSelections = new Vector();
		boolean rowSelectionAllowed = virtualTable.getRowSelectionAllowed();
		boolean columnSelectionAllowed = virtualTable.getColumnSelectionAllowed();
		boolean cellSelectionEnabled = virtualTable.getCellSelectionEnabled();
		ObjectAdapter leadSelection = this.get(leadRowNum, leadColNum);
	    if (leadSelection != null && rowSelectionAllowed && !columnSelectionAllowed && !cellSelectionEnabled) {
	    	if (leadSelection instanceof ClassAdapter)
	    		filteredSelections.add(leadSelection);
	    	else
	    		filteredSelections.add(leadSelection.getParentAdapter());
	    } else {
		
		
		
		//if (leadSelection instanceof uiClassAdapter)
			
		
		if (minSelRow < 0) return;
		if (minSelCol < 0) return;		for (int rowNum =minSelRow; rowNum <= maxSelRow; rowNum ++) {
		
			Vector rowSelections = getRowSelections(rowNum, minSelCol, maxSelCol);
			filterSelections(rowSelections, filteredSelections);		}
	    }
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
	public void processSelectionEventOldButMaybeGold() {
		if (internalTableEvent) {
			//internalTableEvent   = false;
			return;
		}
		Vector filteredSelections = new Vector();
		if (minSelRow < 0) return;
		if (minSelCol < 0) return;
		for (int rowNum =minSelRow; rowNum <= maxSelRow; rowNum ++) {
			Vector rowSelections = getRowSelections(rowNum, minSelCol, maxSelCol);
			filterSelections(rowSelections, filteredSelections);
		}
		if (filteredSelections.size() == 0) return;
		internalTableEvent = true;
		//virtualTable.clearSelection();
		/*
		minSelRow = -1;
		maxSelRow = -1;
		minSelCol = -1;
		maxSelCol = -1;
		*/
	
		SelectionManager.replaceSelections(filteredSelections);
		internalTableEvent = false;
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
		if (!(virtualTable.getCellSelectionEnabled() || virtualTable.getRowSelectionAllowed()) ) return; 
		// not sure we need to do anything here
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
	}
		public void valueChanged(ListSelectionEvent e)  {
		if (disposed) return;
		if (internalTableEvent) return;
		if (e.getValueIsAdjusting()) return;		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (minSelRow == lsm.getMinSelectionIndex() && maxSelRow == lsm.getMaxSelectionIndex()) return;		minSelRow = lsm.getMinSelectionIndex();
		maxSelRow = lsm.getMaxSelectionIndex();		processSelectionEvent();
		/*		//System.err.println("Row Event");
		// suppress duplicate   event from replaceTreeSelections     
		if (internalTableEvent) {
			//internalTableEvent   = false;
			return;
		}		Vector filteredSelections = new Vector();
		if (minSelRow < 0) return;		for (int rowNum =minSelRow; rowNum <= maxSelRow; rowNum ++) {
			Vector rowSelections = getRowSelections(rowNum, minSelCol, maxSelCol);
			filterSelections(rowSelections, filteredSelections);		}
		internalTableEvent = true;
		//virtualTable.clearSelection();				uiSelectionManager.replaceSelections(filteredSelections);		internalTableEvent = false;		*/
		/*		upperLeftSelection = get (minSelRow, minSelCol);		upperRightSelection = get (minSelRow, maxSelRow);		lowerLeftSelection = get (maxSelRow, minSelCol);		lowerRightSelection = get (maxSelRow, maxSelCol);
		printSelection();		*/
				/*
		int rowNum = lsm.getAnchorSelectionIndex();		int colNum = lsm.getLeadSelectionIndex();		*/
		/*		System.err.println("Selected index. anchor: " + rowNum + " lead " + colNum);
		System.err.println("Selected index. minx: " + lsm.getMinSelectionIndex() + " max " + lsm.getMaxSelectionIndex());
		*/		/*
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
		*/	
	
}	int saveMinSelCol, saveMaxSelCol;		void saveSelCol() {
		saveMinSelCol = minSelCol;		saveMaxSelCol = maxSelCol;	}	void restoreSelCol() {
		minSelCol = saveMinSelCol;		maxSelCol = saveMaxSelCol;	}		void clearSelection() {
		saveSelCol();
		virtualTable.clearSelection();
		restoreSelCol();	}

	public void singleItemSelected() {
		/*		if (externalSelectionEvent)
			return;
			*/
		if (internalTableEvent) {
			internalTableEvent = false;
			return;
		}
		
		clearSelection();		updateSelections();
	}	public void noItemSelected() {		clearSelection();
	}	public void multipleItemsSelected() {
		JTable table = (JTable) virtualTable.getPhysicalComponent();		
		System.err.println ("Column Selection Allowed" + table.getColumnSelectionAllowed());
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
		virtualTable.changeSelection(rowNum, colNum, false, true);
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
	}		*/	final int MAX_WIDTH = 600;
	final int CHAR_WIDTH = 7;
	public void refreshPreferredColumnSize(int colNum) {
		/*		virtualTable.getColumnModel().getColumn(colNum).setPreferredWidth(			Math.min(getMaxColumnWidth(colNum), MAX_WIDTH));
			*/
		virtualTable.setColumnWidth (colNum, 
				Math.min(getMaxColumnWidth(colNum), MAX_WIDTH));
	}
	public int getMaxColumnChars( int columnNum) {
		int maxColumnChars = this.getColumnName(columnNum).length();
		int rowCount = getRowCount();		for (int rowNum = 0; rowNum < rowCount; rowNum++) {
			Object cellObject = getValueAt(rowNum, columnNum);
			if (cellObject == null) 
				return 0;
			String cellText = (String) getValueAt(rowNum, columnNum).toString();
			maxColumnChars = Math.max(cellText.length(), maxColumnChars);		}		return maxColumnChars;
	}
	public int getMaxColumnComponentWidth( int columnNum) {		//if (!customEditor[columnNum]) return virtualTable.getColumnModel().getColumn(columnNum).getWidth();
		//if (!customEditor[columnNum]) return virtualTable.getWidth(columnNum);;
		if (!customEditor[columnNum]) return virtualTable.getColumnWidth(columnNum);;
		//int maxColumnComponentWidth = virtualTable.getColumnModel().getColumn(columnNum).getWidth();
		int maxColumnComponentWidth = 0;		//System.err.println("Orig col width" + maxColumnComponentWidth);		int rowCount = getRowCount();
		for (int rowNum = 0; rowNum < rowCount; rowNum++) {			VirtualComponent c = get (rowNum, columnNum).getUIComponent();			
			maxColumnComponentWidth = Math.max((c.getPreferredSize()).getWidth(), maxColumnComponentWidth);					}
		//System.err.println("New col width" + maxColumnComponentWidth);		return maxColumnComponentWidth;
	}	public int getMaxColumnWidth( int columnNum) {
		if (customEditor[columnNum] && activeEditor(columnNum)) {			return getMaxColumnComponentWidth(columnNum);
		} else				return getMaxColumnChars(columnNum)*CHAR_WIDTH;
	}	 int defaultRowHeight;	boolean firstTime = false;
	//int originalRowH	public int getMaxRowHeight (int rowNum) {
		//int retVal = virtualTable.getRowHeight(/*rowNum*/);		int retVal = defaultRowHeight;
		int colCount = this.getColumnCount();		for (int colNum = 0; colNum < colCount; colNum++) {			//if (customEditor[colNum]) {
			if (activeEditor(rowNum, colNum)) {				VirtualComponent c = get (rowNum, colNum).getUIComponent();
				retVal = Math.max(( c.getPreferredSize()).getHeight(), retVal);
			}		}		return retVal;
	}
	public void refreshPreferredRowSize(int rowNum) {		virtualTable.setRowHeight(/*rowNum,*/ getMaxRowHeight(rowNum));
	}						public void addTableModelListener(TableModelListener l) {
		
	};
	public Class getColumnClass(int columnIndex)	{
		Class columnClass = null;		
		for (int rowNum = 0; rowNum < getRowCount(); rowNum++) {
			ObjectAdapter adapter = get (rowNum, columnIndex);
			//if (adapter != null && isCellEditable(rowNum, columnIndex)) {
			//if (adapter != null) {
				if (adapter == null 
					|| !adapter.isAtomic()
					/*|| adapter instanceof uiContainerAdapter */
					/*|| adapter instanceof uiEnumerationAdapter*/) return cellClass;
				//if (adapter instanceof 
				Class propertyClass = null;
				if (adapter.getPropertyClass() instanceof AClassProxy)
					propertyClass = ((AClassProxy) adapter.getPropertyClass()).getJavaClass();
				else
					propertyClass = adapter.computeAndMaybeSetViewObject().getClass();
				if (columnClass == null)
					//columnClass = adapter.getPropertyClass();
					columnClass = propertyClass;
				else
					//if (columnClass != adapter.getPropertyClass())
					if (columnClass != propertyClass)
						return cellClass;
			//}
			
		}
		if (columnClass != null) {
			//System.err.println("Column " + columnIndex + " " + columnClass);
			return columnClass;
		} else
			return cellClass;
	}	

         
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
	public String getColumnName(int columnIndex) {
		//List columnList = getObjectAdapter().getLabels();
		if (hasLabels && columnIndex < labels.size())
			return (String) labels.get(columnIndex);
		String colName = this.colNames[columnIndex];
		//if (colName == null) return noLabel;
		if (colName == null) colName = noLabel;
		//System.err.println("Col: " + columnIndex + " Name " + colName);
//		return "";
		return colName;
		/*
		uiObjectAdapter adapter =  get (0, columnIndex);
		if (adapter != null)
			return adapter.getLabel();
		return "";
		*/
		
	};
	public int getRowCount() {
		int retVal = matrix.size();
		return retVal;
		
		//return matrix.size();
	};
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		ObjectAdapter adapter =  get (rowIndex, columnIndex);
		if (adapter != null) {
			if (customEditor[columnIndex])
				//return adapter.getWidgetAdapter().componentToText();
				//return adapter;
				return adapter.getRealObject();
			else if (getColumnClass(columnIndex) == String.class)
				return adapter.toCell();
			else {
				//System.err.println("getValueAt " + adapter.getRealObject());
				return adapter.getRealObject();
			}
		} else
			return uninitCell;
		
	};
	public boolean isCellEditable(int rowIndex, int columnIndex){
		//return true;
		if ((rowIndex < 0) || (columnIndex < 0))
			return true;
		boolean retVal;
		ObjectAdapter adapter =  get (rowIndex, columnIndex);
		if (adapter != null) {
			retVal = (adapter.getUserObject() != null ) || adapter.isAtomic() && !adapter.isUnEditable();
			return retVal;
			//return /*!(adapter instanceof uiContainerAdapter)*/ adapter.isAtomic() && !adapter.isUnEditable();
		} else
			return false;
		
		
	};
	public void removeTableModelListener(TableModelListener l) {};
	public void setValueAt(Object aValue, int rowIndex, int columnIndex){
		if (getValueAt(rowIndex, columnIndex).equals(aValue))
			return;
		ObjectAdapter adapter =  get (rowIndex, columnIndex);
		if (adapter != null)
			if (aValue.getClass() == String.class)
				adapter.setCompleteUserObject(aValue);
			else
				adapter.setUserTypedObject(aValue);
	};
	
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
	 /*
	 if (anEvent instanceof MouseEvent) {
		 MouseEvent mouseEvent = (MouseEvent) anEvent;
		 if (mouseEvent.getClickCount() == 2) {
			 System.err.println("mouse event");
		 }
	 }
	 */
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
	 ObjectAdapter adapter = get(row, column);
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
 public  void remove(ObjectAdapter compAdapter) {
		
	}
 ObjectAdapter rowAdapter(int rowIndex, int colIndex) {
	 ObjectAdapter adapter =  get (rowIndex, colIndex);
	 for (;;) {
		 if (adapter == null)
			 return adapter;
		 if (adapter.getParentAdapter() == getObjectAdapter())
			 return adapter;
		 adapter = adapter.getParentAdapter();
	 }
 }
@Override
public void mouseClicked(MouseEvent e) {
	if (e.getClickCount() == 2) { 
		int rowIndex = getVirtualTable().getSelectedRow();
		int colIndex = getVirtualTable().getSelectedColumn();
		//uiObjectAdapter adapter =  get (rowIndex, colIndex);
		ObjectAdapter adapter =  rowAdapter(rowIndex, colIndex);
		if (!MethodInvocationManager.invokeDoubleClickMethod(adapter))
			frame.replaceFrame(adapter);
		
	}
	// TODO Auto-generated method stub
	
}
@Override
public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mousePressed(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
public static final int DEFAULT_TABLE_WIDTH = 300;
public static final int DEFAULT_TABLE_HEIGHT = 150;
public int defaultWidth() {
	return DEFAULT_TABLE_WIDTH;
}
public int defaultHeight() {
	return DEFAULT_TABLE_HEIGHT ;
}
boolean refreshing;
@Override
public void updateVector(VectorChangeEvent evt) {
	if (refreshing)
		return;
	refreshing = true;
	refreshMatrix();
	//virtualTable.validate();
	virtualTable.repaint();
	refreshing = false;
	// this seems to trigger a refresh, could validate also I suppose
	//refreshPreferredColumnSize();
	
}
public void processAttributes() {
	super.processAttributes();
}
public boolean processDescendentAttribute(ObjectAdapter descendent,
		Attribute attrib) {
	boolean retVal = super.processDescendentAttribute(descendent, attrib);
	 if (descendent.getRow() < 0 || descendent.getColumn() < 0) return retVal;
	 if (attrib.getName().equals(AttributeNames.EXPLANATION)) {
		 virtualTable.setTooltipText(descendent.getRow(), descendent.getColumn(), (String) attrib.getValue());
		 return true;
	 } else if (attrib.getName().equals(AttributeNames.FONT)) {
		 virtualTable.setFont(descendent.getRow(), descendent.getColumn(), (Font) attrib.getValue());
		 return true;
	 } else if (attrib.getName().equals(AttributeNames.COMPONENT_BACKGROUND)) {
		 virtualTable.setBackground(descendent.getRow(), descendent.getColumn(), (Color) attrib.getValue());
		 return true;
	 } else if (attrib.getName().equals(AttributeNames.COMPONENT_FOREGROUND)) {
		 virtualTable.setForeground(descendent.getRow(), descendent.getColumn(), (Color) attrib.getValue());
		 return true;
	 }
	 return retVal;
}


}
