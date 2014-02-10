package bus.uigen.view;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.editors.TreeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.tree.TreeSelector;
import bus.uigen.widgets.tree.VirtualTree;

public class TreeView {	/*
	implements TreeExpansionListener, TreeSelectionListener, 	MouseListener, TreeModel {
	*/
	public static final String TREE_VIEW_NAME = "Tree View";		uiFrame frame;	TopViewManager topViewManager;
			public void init (uiFrame theFrame, TopViewManager theTopViewManager) {		frame = theFrame;		topViewManager = theTopViewManager;
	}	//JTree   jTree;
	VirtualTree   jTree;
	//ScrollPane treePanel;	//Container treePanel;
	ObjectAdapter rootTreeNode;
	
	public VirtualTree getJTree() {		return jTree;
	}
	public VirtualContainer getTreePanel() {
		//return treePanel;		return topViewManager.getTreeContainer();
	}
	
	public void setTreeRoot(ObjectAdapter newVal) {
		rootTreeNode = newVal;				
	}		public void createTreePanel(ObjectAdapter adapter) {
		createTreePanel();	
		createJTree(this.getTreePanel());		
		/*		if (treePanel   == null) {
			treePanel   = new ScrollPane(); 			treePanel.setName(uiFrame.TREE_PANEL_NAME);			try {
			jTree   = new JTree();			treePanel.add(jTree);
		    //rootTreeNode = uiGenerator.generateInUIPanel(frame, adapter.getObject(), null,   adapter, treePanel, null, jTree);		*/		try {
			boolean oldTextMode = uiGenerator.textMode();
			uiGenerator.setTextMode(true);
			//rootTreeNode = uiGenerator.generateInUIPanel(frame, adapter.getObject(), null,   adapter, this.getTreePanel(), null, jTree);
			//rootTreeNode = uiGenerator.generateInBrowsableContainer(frame, adapter.getObject(), adapter, this.getTreePanel(), jTree);
			rootTreeNode = uiGenerator.generateInBrowsableRootPanel(frame, adapter.getObject(), null, adapter, this.getTreePanel(), "Tree View", jTree);

			frame.initDerivedAdapter(rootTreeNode);
			//topViewManager.setTreePanelIsVisible(false);
			topViewManager.showTreePanel();
			uiGenerator.setTextMode(oldTextMode);
			treeAdapter = (TreeAdapter) rootTreeNode.getWidgetAdapter();			//this.add(treePanel,BorderLayout.WEST);
			} catch (Exception e) {				System.out.println("createTreePanel " + e);
			}	}	public VirtualContainer createTreePanel() {
		VirtualContainer treePanel = getTreePanel();
				//Container treePanel = null;
		//if (getTreePanel()   == null) {
		if (treePanel == null) {			/*
			treePanel   = ScrollPaneSelector.createScrollPane();			treePanel.setName(uiFrame.TREE_PANEL_NAME);			*/			treePanel = topViewManager.newTreeContainer();			/*
			jTree   = new JTree();			treePanel.add(jTree);				*/
		}
		return treePanel;	}	public VirtualContainer createJTree(VirtualContainer treePanel) {
			//jTree   = new JTree();
			jTree   = TreeSelector.createTree();			treePanel.add(jTree);			//jTree.setBackground(treePanel.getBackground());
			return jTree;	}
	public void createTreePanel (Object obj) {		createTreePanel();
		//rootTreeNode = uiGenerator.generateInUIPanel(frame, obj, null,   null, getTreePanel(), null, jTree);
		createTreePanel(obj, getTreePanel());		/*
		rootTreeNode = uiGenerator.generateInBrowsableContainer(frame, obj,  getTreePanel(), jTree);
		*/			topViewManager.setTreePanelIsVisible(false);
			topViewManager.showTreePanel();
						}
	TreeAdapter treeAdapter;
	public void createTreePanel (Object obj, VirtualContainer treePanel) {		createJTree(treePanel);
		//rootTreeNode = uiGenerator.generateInUIPanel(frame, obj, null,   null, getTreePanel(), null, jTree);
		rootTreeNode = uiGenerator.generateInBrowsableContainer(frame, obj,  treePanel, jTree);
		treeAdapter = (TreeAdapter) rootTreeNode.getWidgetAdapter();			//topViewManager.setTreePanelIsVisible(false);
			//topViewManager.showTreePanel();				}	public ObjectAdapter   getTopTreeAdapter() {
		return rootTreeNode;
	}	/*
		public void createTreePanelOld(uiObjectAdapter adapter) {		if (treePanel   == null) {
			treePanel   = new ScrollPane(); 			treePanel.setName(uiFrame.TREE_PANEL_NAME);
			//treePanel.setLayout(new   BorderLayout());
			
			rootTreeNode = adapter;			//jTree =   new JTree(adapter);			try {
			jTree   = new JTree(this);			treePanel.add(jTree);
									jTree.addMouseListener(this);			jTree.addTreeSelectionListener(this);
			jTree.addTreeExpansionListener(this);   
			treePanel.setSize((int) 200, frame.getSize().height);			//treePanelIsVisible = false;			frame.setTreePanelIsVisible(false);
			frame.showTreePanel();			//this.add(treePanel,BorderLayout.WEST);
			} catch (Exception e) {				System.out.println("createTreePanel " + e);
			}		}			}
	//public boolean treePanelIsVisible = false;
	
	
	
	
	public void treeExpanded(TreeExpansionEvent event) {		//System.out.println("Tree expanded");
	}
	public void treeCollapsed(TreeExpansionEvent event) {		//System.out.println("tree expanded");
	}
	boolean treeSelected = false;	public void valueChanged(TreeSelectionEvent e)  {
				// suppress duplicate   event from replaceTreeSelections     
		if (internalTreeEvent) {
			internalTreeEvent   = false;
			return;
		}
				TreePath treePath   = jTree.getSelectionPath();		TreePath eventPath = e.getNewLeadSelectionPath();		TreePath eventPathOld   = e.getOldLeadSelectionPath();
		if (treePath == null)   {			//System.out.println("null selection");
			return;
		}		uiObjectAdapter treeNode = (uiObjectAdapter) treePath.getLastPathComponent();
		
		uiObjectAdapter newTreeNode =   (uiObjectAdapter) eventPath.getLastPathComponent();		//System.out.println("calling   select" + treeNode);
		TreePath[] selectedPaths = jTree.getSelectionPaths();
		Vector selectedTreeNodes = new Vector();		for (int i = 0; i   < selectedPaths.length; i++) {
			//System.out.println("no:" + i + "node" +   selectedPaths[i]);
			selectedTreeNodes.addElement((uiObjectAdapter)selectedPaths[i].getLastPathComponent());		}
		//System.out.println(selectedNodes);		treeSelected = true;
		uiSelectionManager.replaceSelections(selectedTreeNodes);		frame.setTitle();
				
		//uiSelectionManager.replaceSelections((uiObjectAdapter)createdTreePath.getLastPathComponent());		
		
				//uiSelectionManager.select(treeNode,   true);		//uiSelectionManager.replaceSelections(treeNode);
		//treeNode.uiComponentFocusGained();		//replaceFrame();
		//System.out.println("TreeNode is   " + treeNode);
		
			}	*/
	public void clearTreeSelection() {
		if (jTree   == null) return;		jTree.clearSelection();
	}
		boolean internalTreeEvent   = false;
	public void setJTreeSelectionPaths(TreePath[]   selectedPaths) {		
		
		//jTree.clearSelection();     
		//System.out.println("Selected Paths"   + selectedPaths);
		
		//TreePath createdTreePath = jTree.getPathForLocation(0,0);		//System.out.println(createdTreePath);		//createdTreePath.pathByAddingChild(topAdapter);
		//System.out.println(selectedPaths[0]);
		//jTree.setSelectionPaths(selectedPaths);		//jTree.setSelectionPath(createdTreePath);
		internalTreeEvent   = true;
				if ((jTree !=   null) && topViewManager.treePanelIsVisible())			jTree.setSelectionPaths(selectedPaths);
				
	}
	/*	public void mouseClicked(MouseEvent e) {		
		if ((e.getClickCount() == 2) ) {
			TreePath treePath = jTree.getSelectionPath();			if (treePath == null) return;
			System.out.println(treePath);			uiObjectAdapter treeNode = (uiObjectAdapter) treePath.getLastPathComponent();
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
	}	*/
	Vector  treeModelListeners = new Vector();	public void addTreeModelListener(TreeModelListener l)   {
		treeModelListeners.addElement(l);	}	public void removeTreeModelListener(TreeModelListener   l) {
		treeModelListeners.removeElement(l);	}
	/*	public Object   getChild(Object parent, int index){
		return  ((TreeNode) parent).getChildAt(index);	}	*/
	public void treeStructureChanged () {
		if (getRoot() == null) return;		Object[] path   = {getRoot()};
		for (Enumeration elements   = treeModelListeners.elements();
			 elements.hasMoreElements();)			((TreeModelListener) elements.nextElement()).treeStructureChanged(new   TreeModelEvent(this,path )); 	}	/*
		public int getChildCount(Object parent) {
		if(ObjectEditor.colabMode() && ObjectEditor.coupleElides){			frame.getChildCountReq = new AutomaticRefresh(frame,(uiObjectAdapter) parent);		} else if (ObjectEditor.colabMode()){
			frame.getChildCountReq = new AutomaticRefresh(frame,((uiObjectAdapter) parent).getPath(),"getChildCount");		}
		int retVal = subGetChildCount(parent);
		frame.getChildCountReq = null;		return(retVal);
	}	*/
	public int subGetChildCount(Object parent){		return ((TreeNode) parent).getChildCount();	}
	/*	public int getIndexOfChild(Object   parent, Object child)  {
		return ((TreeNode) parent).getIndex((TreeNode) child);	}
	*/	
	public Object   getRoot() {
		return rootTreeNode;
	}
	
	public void reload() {
		treeAdapter.reload();
	}
	
	public void expandAllTreeNodes() {
		treeAdapter.expandAllTreeNodes();
	}
	
	public void nodeChanged(TreeNode node) {
		treeAdapter.nodeChanged(node);
	}
	
	/*
	
	
	public boolean isLeaf(Object node)  {
		return ((TreeNode) node).isLeaf();
	}
	public void valueForPathChanged(TreePath path, Object   newValue)  {
	}		*/

}
