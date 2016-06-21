package bus.uigen.editors;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import bus.uigen.AutomaticRefresh;
import bus.uigen.ObjectEditor;
import bus.uigen.WidgetAdapter;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.tree.TreeSelector;
import bus.uigen.widgets.tree.VirtualTree;

public class TreeAdapter extends WidgetAdapter /*extends Frame*/
	implements TreeExpansionListener, TreeSelectionListener, 	MouseListener, TreeModel, TreeModelListener {		uiFrame frame;
			public void init (uiFrame theFrame) {		frame = theFrame;
	}	VirtualTree   jTree;
	boolean linked = false;
	VirtualContainer treePanel;
	ObjectAdapter rootTreeNode;
	
	public VirtualTree getVirtualTree() {		return jTree;
	}
	public VirtualComponent getTreePanel() {		return treePanel;
	}
	
	public void createVirtualTree(VirtualContainer treePanel) {		/*
		//DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");		DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);		treeModel.addTreeModelListener(this);
		//jTree   = new JTree(this);
		jTree   = new JTree(treeModel);			treePanel.add(jTree);
			jTree.setEditable(true);						jTree.addMouseListener(this);			jTree.addTreeSelectionListener(this);
			jTree.addTreeExpansionListener(this);
		*/			treePanel.add(createVirtualTree());
	}
	public VirtualTree  createVirtualTree() {
		//DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");		//rootTreeNode = this.getObjectAdapter();
//		DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);
	    treeModel = new DefaultTreeModel(rootTreeNode);
		treeModel.addTreeModelListener(this);
		//jTree   = new JTree(this);
		//jTree   = new JTree(treeModel);
		jTree   = TreeSelector.createTree(treeModel);
			jTree.setEditable(true);						jTree.addMouseListener(this);			jTree.addTreeSelectionListener(this);
			jTree.addTreeExpansionListener(this); 			return jTree;
	}
	DefaultTreeModel treeModel;
	public void  initVirtualTree(VirtualTree jTree) {
		//DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");		//rootTreeNode = this.getObjectAdapter();
		//DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);
		treeModel = new DefaultTreeModel(rootTreeNode);		treeModel.addTreeModelListener(this);
		//jTree   = new JTree(this);
		//jTree   = new JTree(treeModel);		jTree.setModel(treeModel);
		//jTree.setModel(this);
			jTree.setEditable(true);						jTree.addMouseListener(this);			jTree.addTreeSelectionListener(this);
			jTree.addTreeExpansionListener(this);
			//add this to virtual toolkit sometime
			((JTree) jTree.getPhysicalComponent()).setCellRenderer(new CustomTreeCellRender());
			if (spane == null)
				super.setAttributes(jTree);
				else {
					setSize(spane);
				  	setColors(spane);
				}
	}
	
	
	public void initVirtualTree() {		initVirtualTree(jTree);
	}
	
	public void expandAllTreeNodes() {
		JTree swingJTree = ((JTree) jTree.getPhysicalComponent());
		for (int i = 0; i < swingJTree.getRowCount(); i++)
			swingJTree.expandRow(i);
		
	}
	
	public void reload() {
//		if (((JTree) jTree.getPhysicalComponent()).getModel() == treeModel)
//		rootTreeNode.implicitRefresh();
		treeModel.reload();
		
	}
		
	
	public void nodeChanged(TreeNode node) {
		treeModel.nodeChanged(node);
	}	

	public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
    try {	  //rootTreeNode = this.getObjectAdapter();
	  //frame = this.getObjectAdapter().getUIFrame();
		//return createJTree();		
		//jTree = (JTree)cclass.newInstance();
    	instantiatedComponent = true;
		jTree = TreeSelector.createTree();
		
		if (adapter.isScrolled()) {
			   spane = ScrollPaneSelector.createScrollPane();		  
			  spane.setScrolledComponent(jTree);
			  //spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			  return spane;
		  }
		//JPanel parentPanel = new JPanel();
		//Janel childPanel = new JPanel();	  //return (Container) cclass.newInstance();
		return jTree;
				//return new JTree();
      //return treePanel;
    } catch (Exception e) {
      e.printStackTrace();
      return PanelSelector.createPanel();
      //return new JPanel();
    }
  }
	public VirtualComponent getUIComponent() {
		return jTree;
	}
	
	public void linkUIComponentToMe() {		//jTree = (VirtualTree) component;
		JTree jtree;
		rootTreeNode = this.getObjectAdapter();		getObjectAdapter().setAtomic(true);
		frame = this.getObjectAdapter().getUIFrame();
		frame.addKeyListener(jTree);
		linked = true;		//initJTree((JTree) component);		initVirtualTree();
		/*		
		treePanel = (Container) component;			  rootTreeNode = this.getObjectAdapter();
	  frame = this.getObjectAdapter().getUIFrame();	  createJTree(treePanel); 
		*/		
      
    
  }
	public void linkUIComponentToMe(VirtualComponent component) {
		if (jTree == component && linked)
			return;
		jTree = (VirtualTree) component;
		linkUIComponentToMe();
      
    
  }	Object rootObject;
	public void setUIComponentTypedValue(Object newVal) {		rootObject = newVal;		//jTree.treeDidChange();
		treeStructureChanged();		//jTree.updateUI();
	}
	public Object getUIComponentValue() {	  return rootObject;
  }	/*
	public void setUIComponentEditable() {	  
  }
	public void setUIComponentUneditable() {
	}
	*/		public void createTreePanel(ObjectAdapter adapter) {		if (treePanel   == null) {
			//treePanel   = new ScrollPane();
			treePanel = PanelSelector.createPanel();
			
			//treePanel = frame.newTreeContainer();			treePanel.setName(AttributeNames.TREE_PANEL_NAME);
			//treePanel.setLayout(new   BorderLayout());
			
			rootTreeNode = adapter;			//jTree =   new JTree(adapter);			try {				createVirtualTree(treePanel);				/*
			jTree   = new JTree(this);			treePanel.add(jTree);
									jTree.addMouseListener(this);			jTree.addTreeSelectionListener(this);
			jTree.addTreeExpansionListener(this); 				*/
			treePanel.setSize((int) 200, frame.getSize().getHeight());			//treePanelIsVisible = false;			frame.setTreePanelIsVisible(false);
			frame.showTreePanel();			//this.add(treePanel,BorderLayout.WEST);
			} catch (Exception e) {				System.out.println("createTreePanel " + e);
			}		}		/*
		if (jTree   != null)
		this.remove(jTree);
		if (adapter != null && adapter.getRealObject() !=   null) { 
		System.out.println("creating JTree for" +   adapter);		jTree = new JTree(adapter);
		System.out.println("added JTree");      
		jTree.addMouseListener(this);		jTree.addTreeSelectionListener(this);
		jTree.addTreeExpansionListener(this);		this.add(jTree, BorderLayout.WEST);
		//jTree.addVetoableChangeListener(this);		}
		*/	}
	//public boolean treePanelIsVisible = false;
	/*	void treePanel() {
	if (treePanel   == null) {
	createTreePanel(origAdapter);	}   else
	treePanel.setVisible(!treeScrollPane.isVisible());
	validate();	}
	*/
	/* AVM	public void treePanel() {
		//if (mainScrollPane.isVisible())		if (treePanelIsVisible)
			//mainScrollPane.setVisible(false);			hideTreePanel();
		else
			//mainScrollPane.setVisible(true);  			showTreePanel();
		validate();	}
	void hideTreePanel() {		if (treePanelIsVisible) {
			this.remove(treePanel);			//toolBar.setVisible(false);			treePanelIsVisible = false;
		}		if (toolBarIsVisible && !drawPanelIsVisible && ! this.mainScrollPaneIsVisible) {
			remove(toolBar);
			add(toolBar, BorderLayout.CENTER);
			//setSize(this.TOOLBAR_WIDTH,   this.TOOLBAR_HEIGHT);		}
		setSize();		
	}	void showTreePanel() {
		if (treePanel   == null)
			createTreePanel(browser.getOriginalAdapter());		if (!treePanelIsVisible) {
			if (drawPanelIsVisible ||   this.mainScrollPaneIsVisible )				//segi leave at west				this.add(treePanel, BorderLayout.WEST);
			
						else if (toolBarIsVisible) {
				this.remove(toolBar);
								//segi leave west
				//this.add(treePanel, BorderLayout.CENTER);
				
				
								this.add(toolBar,   BorderLayout.NORTH);
				//this.setSize(this.TOOLBAR_WIDTH, this.TOOLBAR_HEIGHT);			}   else
				//segi leave west
				//this.add(treePanel, BorderLayout.CENTER);
				;
						//toolBar.setVisible(true);			treePanelIsVisible = true;			
		}		
	}	*/
	/*	
	void hideTreePanel() {	if (treePanelIsVisible) {	treePanel.setVisible(false);	treePanelIsVisible = false;
	}	
	}	void showTreePanel() {
	if (treePanel   == null) {
	createTreePanel(origAdapter);
	treePanelIsVisible  = true;
	}   else if (!treePanelIsVisible) {	treePanel.setVisible(true);	treePanelIsVisible = true;
	}         
	}	*/
	
	public void treeExpanded(TreeExpansionEvent event) {				//System.out.println("Tree expanded");
		ObjectAdapter treeNode = (ObjectAdapter) event.getPath().getLastPathComponent();
		MethodInvocationManager.invokeExpandMethod(treeNode, true);
	}
	public void treeCollapsed(TreeExpansionEvent event) {
		ObjectAdapter treeNode = (ObjectAdapter) event.getPath().getLastPathComponent();
		MethodInvocationManager.invokeExpandMethod(treeNode, false);		//System.out.println("tree collapsed");
	}
	boolean treeSelected = false;	public void valueChanged(TreeSelectionEvent e)  {
				// suppress duplicate   event from replaceTreeSelections     
		if (internalTreeEvent) {
			internalTreeEvent   = false;
			return;
		}
				TreePath treePath   = (TreePath) jTree.getSelectionPath();		TreePath eventPath = e.getNewLeadSelectionPath();		TreePath eventPathOld   = e.getOldLeadSelectionPath();
		if (treePath == null)   {			//System.out.println("null selection");
			return;
		}		ObjectAdapter treeNode = (ObjectAdapter) treePath.getLastPathComponent();
		/*		TreePath createdTreePath = treeNode.getTreePath();		System.out.println("created Tree Path" + treePath);		*/
		ObjectAdapter newTreeNode =   (ObjectAdapter) eventPath.getLastPathComponent();		//System.out.println("calling   select" + treeNode);
		TreePath[] selectedPaths = (TreePath[])jTree.getSelectionPaths();
		Vector selectedTreeNodes = new Vector();		for (int i = 0; i   < selectedPaths.length; i++) {
			//System.out.println("no:" + i + "node" +   selectedPaths[i]);
			selectedTreeNodes.addElement((ObjectAdapter)selectedPaths[i].getLastPathComponent());		}
		//System.out.println(selectedNodes);		treeSelected = true;
		SelectionManager.replaceSelections(selectedTreeNodes);
		if (frame != null)		frame.setTitle();
		/*		TreePath createdTreePath = jTree.getPathForLocation(0,0);		System.out.println(createdTreePath);		createdTreePath.pathByAddingChild(topAdapter);		jTree.setSelectionPath(createdTreePath);
		*/		
		//uiSelectionManager.replaceSelections((uiObjectAdapter)createdTreePath.getLastPathComponent());		
		/*		if (selectedNodes   != null) 
		for (int i = 0; i < selectedNodes.length; i++)
		System.out.println("no:" + i + "node" + selectedNodes[i]);		*/
		/*		if (eventPath   != null)
		System.out.println("event path" + eventPath.getLastPathComponent());		if (eventPathOld != null)		System.out.println("event   path old" + eventPathOld.getLastPathComponent());		*/
				//uiSelectionManager.select(treeNode,   true);		//uiSelectionManager.replaceSelections(treeNode);
		//treeNode.uiComponentFocusGained();		//replaceFrame();
		//System.out.println("TreeNode is   " + treeNode);
		
			}
	public void clearTreeSelection() {
		if (jTree   == null) return;		jTree.clearSelection();
	}	boolean internalTreeEvent   = false;
	public void setJTreeSelectionPaths(TreePath[]   selectedPaths) {		/*		// suppress tree selection if   user selected it directly    
		if (treeSelected)   {
		treeSelected = false;
		return;
		}		*/
		
		//jTree.clearSelection();     
		//System.out.println("Selected Paths"   + selectedPaths);
		/*		for (int i = 0; i   < selectedPaths.length; i++)		System.out.println("no" +   i + " " + selectedPaths[i]);		*/
		//TreePath createdTreePath = jTree.getPathForLocation(0,0);		//System.out.println(createdTreePath);		//createdTreePath.pathByAddingChild(topAdapter);
		//System.out.println(selectedPaths[0]);
		//jTree.setSelectionPaths(selectedPaths);		//jTree.setSelectionPath(createdTreePath);
		internalTreeEvent   = true;
				if ((jTree !=   null) && frame.treePanelIsVisible())			jTree.setSelectionPaths(selectedPaths);
				
	}	public void mouseClicked(MouseEvent e) {		
		if ((e.getClickCount() == 2) ) {
			//Object treePath = jTree.getSelectionPath();
			ObjectAdapter treeNode = (ObjectAdapter) jTree.getLastSelectedPathComponent();
			if (treeNode == null) return;			//if (treePath == null) return;
			//System.out.println(treePath);
			System.out.println(treeNode);			//uiObjectAdapter treeNode = (uiObjectAdapter) treePath.getLastPathComponent();
			if (!MethodInvocationManager.invokeDoubleClickMethod(treeNode))				frame.replaceFrame(treeNode);
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
	public void notifyListener(TreeModelListener l, TreeModelEvent e) {
		l.treeStructureChanged(e);
	}
	
	Vector  treeModelListeners = new Vector();	public void addTreeModelListener(TreeModelListener l)   {
		System.out.println("Tree Model Listener added");
		treeModelListeners.addElement(l);	}	public void removeTreeModelListener(TreeModelListener   l) {
		treeModelListeners.removeElement(l);	}	public Object   getChild(Object parent, int index){
		return  ((TreeNode) parent).getChildAt(index);	}
// called whenever a new child is change, lots of spurious updates, should send event
	public void treeStructureChanged () {
		if (treeModel != null)
			treeModel.reload();
		//jTree.setModel(this);		Object[] path   = {getRoot()};
		for (Enumeration elements   = treeModelListeners.elements();
			 elements.hasMoreElements();)			((TreeModelListener) elements.nextElement()).treeStructureChanged(new   TreeModelEvent(this,path )); 	}
		public int getChildCount(Object parent) {
		if(ObjectEditor.colabMode() && ObjectEditor.coupleElides()){			frame.getChildCountReq = new AutomaticRefresh(frame,(ObjectAdapter) parent);		} else if (ObjectEditor.colabMode()){
			frame.getChildCountReq = new AutomaticRefresh(frame,((ObjectAdapter) parent).getPath(),"getChildCount");		}
		int retVal = subGetChildCount(parent);
		frame.getChildCountReq = null;		return(retVal);
	}
	public int subGetChildCount(Object parent){		return ((TreeNode) parent).getChildCount();	}	public int getIndexOfChild(Object   parent, Object child)  {
		return ((TreeNode) parent).getIndex((TreeNode) child);	}	
	public Object   getRoot() {
		return rootTreeNode;
	}
	 public  void setUIComponentSelected(ObjectAdapter[] child) {
		 
	 }
	  public  void setUIComponentDeselected(ObjectAdapter[] child) {
		  
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
	}
	public  void remove(ObjectAdapter compAdapter) {
		
	}		/* new browser
		public void setOriginalAdapter(uiObjectAdapter newVal) {
		
		origAdapter =   newVal;   		maybeShowJTree();
		//createTreePanel(newVal);		
	}
	*/
	/*		public void maybeShowJTree() {		uiObjectAdapter adapter =   frame.getOriginalAdapter();
		if (//adapter.getHeight() > DEEP_ELIDE_LEVEL +   1 ||			new HasUncreatedChildrenVisitor(adapter).traverse().contains(Boolean.TRUE))
			createTreePanel(adapter);	}	*/

}
