package bus.uigen.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import util.trace.uigen.DrawingPanelAdditionStarted;
import util.trace.uigen.DrawingPanelDisplayEnded;
import bus.uigen.uiFrame;
import bus.uigen.uiFrameList;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.AToolbarManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.visitors.HasUncreatedChildrenVisitor;
import bus.uigen.widgets.InternalFrameSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.SplitPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDesktopPane;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualInternalFrame;
import bus.uigen.widgets.VirtualScrollPane;
import bus.uigen.widgets.VirtualSplitPane;

public class ATopViewManager  implements TopViewManager /*extends Frame*/ {
			public static   final String MAIN_PANEL_NAME = "main"; 	public static   final String DRAW_PANEL_NAME = "draw"; 	public static   final String TREE_PANEL_NAME = "tree"; 
	public static   final String TOOLBAR_PANEL_NAME = "toolbar"; 
	
	public static   final int TOOLBAR_HEIGHT = 75;
	public static   final int TOOLBAR_WIDTH = 350;
	public static   final int CHAR_WIDTH = 9;	
//	public static   final int FRAME_HEIGHT = 350;
//	public static   final int FRAME_WIDTH = 325;
	public static   final int FRAME_HEIGHT = 240;
	public static   final int FRAME_WIDTH = 320;
	public static   final int FIRST_FRAME_X = 150;
	public static   final int FIRST_FRAME_Y = 20;
	public static   final int EMPTY_FRAME_HEIGHT = 40;
	public static   final int EMPTY_FRAME_WIDTH = 250;
	
	//public static final int TREE_WIDTH = 100;	boolean nonDefaultToolbarMethodFound = false;
	public void setNonDefaultToolbarMethodFound (boolean newVal) {
		nonDefaultToolbarMethodFound = newVal;
	}
	public boolean getNonDefaultToolbarMethodFound () {
		return nonDefaultToolbarMethodFound;
	}
	public void maybeShowToolbar() {
		if (getNonDefaultToolbarMethodFound())
			toolBar();
	}	//i'm using frame so that the toolbar grouping stuff only happens once (the main object window)	//public static int toolbarCount = 0;	AToolbarManager toolbarManager;
	uiFrame frame;	
	public void init (uiFrame theFrame, AToolbarManager theToolbarManager) {
		toolbarManager = theToolbarManager;
		frame = theFrame;
		frame.setLayout(new BorderLayout());	}
	public VirtualContainer getMainContainer() {
		//return mainSplitPane;
		return mainPane;
		//return topViewManager.getMainContainer();
	}	/*
	public boolean isDynamicContainer(Container c) {		return dynamicContainers.contains(c);
	}
	*/	public Vector dynamicContainers = new Vector();	
	public Vector containers() {		Vector containers = new Vector();		if (dynamicContainers.size() == 0) return null;
		if (this.treePanelIsVisible())			containers.addElement(frame.getTreePanel());		for (int index = 0; index < dynamicContainers.size(); index ++) {
			containers.addElement(dynamicContainers.elementAt(index));		}		if (frame.secondaryScrollPaneIsVisible())			containers.addElement(frame.getSecondaryScrollPane());		return containers;
	}
	public VirtualContainer containerAt(int index) {		return (VirtualContainer) containers().elementAt(index);
	}
		public int numContainers() {
		return containers().size();	}
	public VirtualContainer getTreeContainer() {		return this.treePanel;
	}
	public VirtualContainer getSecondaryContainer() {		return this.secondaryScrollPane;
	}
	boolean firstPanel  = true;	boolean secondPanel = false;	//VirtualContainer mainScrollPane;
	VirtualContainer mainPane;	VirtualContainer spane;
	/*	public VirtualContainer getMainContainer() {
		return mainPane;
		//return topViewManager.getMainContainer();	}
	*/
	public boolean mainContainerIsEmpty() {
		return mainPane.countComponents() == 0;
	}
	//public VirtualContainer newContainer(int direction, VirtualComponent scrolledComponent) {
	public VirtualScrollPane newContainer(int direction, VirtualComponent scrolledComponent) {
				//ScrollPane newScrollPane = new ScrollPane();		//Container newScrollPane = new ScrollPane();		VirtualScrollPane newScrollPane = ScrollPaneSelector.createScrollPane(scrolledComponent);
		//JLayeredPane newLayeredPane = new JLayeredPane();		//JPanel newChildPanel = new JPanel();
		//newLayeredPane.add(newScrollPane);
		//mainPanel.add(newScrollPane);
		if (firstPanel) {
			//mainScrollPane = newScrollPane;			mainPane = newScrollPane;//readding this line			mainPane.setName(AttributeNames.MAIN_PANEL_NAME);
			
			
			//frame.add(newScrollPane);
						//segi...above was orignal.  changed to below to put south.
			//this.add(newScrollPane, BorderLayout.SOUTH);									//mainPanel.setTopComponent(newScrollPane); 
			//mainPanel.setTopComponent(newLayeredPane);    			firstPanel = false;			secondPanel = true;
		}		else {			VirtualContainer parent = spane.getParent();			parent.remove(spane);			//spane.setSize(spane.getWidth(), spane.getHeight());
			//JSplitPane splitPane = new JSplitPane(direction, spane, newScrollPane);
			VirtualSplitPane splitPane = SplitPaneSelector.createSplitPane(direction, spane, newScrollPane);
			splitPane.setResizeWeight(0.5);
			splitPane.setContinuousLayout(true);
			VirtualDimension d = (VirtualDimension) spane.getMinimumSize();			
			System.err.println ("SplitPane component min size" + d.getHeight() + "," + d.getWidth());
			spane.setMinimumSize(new VirtualDimension(50, 50));
			newScrollPane.setMinimumSize(new VirtualDimension (50, 50));
			 d = (VirtualDimension) spane.getMinimumSize();			
			System.err.println ("SplitPane component min size" + d.getHeight() + "," + d.getWidth());			//JSplitPane splitPane = new JSplitPane(direction);			/*
			if (direction == JSplitPane.HORIZONTAL_SPLIT)			this.setSize(size.width*2, size.height);			else			this.setSize(size.width, size.height*2);			*/
			
			//System.err.println("size" + size.width + " " + size.height);			//splitPane.setDividerLocation(0.5);
			//splitPane.setLeftComponent(spane);        
			//splitPane.setRightComponent(newScrollPane);			if (secondPanel) {				parent.add(splitPane);				/*
				if (parent instanceof Frame)
				mainScrollPane = splitPane;
				*/
				//readding this
				if (parent instanceof Frame) {
					mainPane = splitPane;
					//mainScrollPane.setName(uiFrame.MAIN_PANEL_NAME);				}
							} else				((VirtualSplitPane) parent).setRightComponent(splitPane);						
			//spane.doLayout();
			VirtualDimension size = frame.getSize();
			if (direction == JSplitPane.HORIZONTAL_SPLIT)				frame.setSize(size.getWidth()*2, size.getHeight());			else				frame.setSize(size.getWidth(), size.getHeight()*2);
			//newScrollPane.doLayout();			
			//splitPane.setLeftComponent(newScrollPane);
			//splitPane.setLeftComponent(newLayeredPane);			//mainPanel.setBottomComponent(splitPane);		}
		spane = newScrollPane;		dynamicContainers.addElement(spane);
		//layeredPane = newLayeredPane;		return newScrollPane;    	}
	public VirtualContainer newContainer(VirtualComponent scrolledComponent) {		return newContainer(JSplitPane.VERTICAL_SPLIT, scrolledComponent);
	}
	VirtualContainer secondaryScrollPane;	public VirtualContainer newSecondaryContainer() {		if (secondaryScrollPane == null) {
			//VirtualContainer panel = PanelSelector.createPanel();
			//secondaryScrollPane = ScrollPaneSelector.createScrollPane();
			secondaryScrollPane = ScrollPaneSelector.createScrollPane();
			secondaryScrollPane.setName(AttributeNames.SECONDARY_PANEL_NAME);
			//uiObjectAdapter adapter = uiGenerator.generateInBrowsableContainer(frame, SECONDARY_MESSAGE, secondaryScrollPane);
			//frame.setSecondaryObjectAdapter(adapter);		}
		return secondaryScrollPane;	}
	VirtualContainer treePanel;	public VirtualContainer newTreeContainer() {		if (treePanel == null) {
			treePanel   = ScrollPaneSelector.createScrollPane();			treePanel.setName(AttributeNames.TREE_PANEL_NAME);
			VirtualDimension d = (VirtualDimension) treePanel.getPreferredSize();
			treePanel.setSize (new VirtualDimension(200, d.getHeight()));		}
		return treePanel;	}
	
	public void setTreeContainer(VirtualContainer c) {
		treePanel = c;
	}
				/*	Panel   drawPanel;	//Connections   drawing = new Connections();
	SLModel drawing =   new SLModel();	public SLModel getDrawing() {
		//System.err.println("drawing   is" +  drawing);
		return drawing;
		
			}	uiObjectAdapter drawingAdapter;	public uiObjectAdapter getDrawingAdapter() {
		return drawingAdapter;	}
	*/	public boolean drawPanelIsVisible() {
		if (frame.getDrawPanel()   == null) return false;
		//return frame.getDrawPanel().isVisible();
		return drawPanelIsVisible;	}
	/*	public void createDrawPanel()   {
		if (drawPanel   != null) return;		drawPanel   = new Panel();
		drawPanel.setName(frame.DRAW_PANEL_NAME);		drawPanel.setLayout(new BorderLayout());
		try {
			bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel", "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");        			//uiGenerator.generateUI(drawPanel, new slm.SLModel());			//drawing = new Connections();			currentObjects.addElement(drawing);
			uiGenerator.generateInUIPanel(frame,  drawing, drawPanel);			//drawingAdapter = (uiObjectAdapter) currentAdapters.elementAt(currentAdapters.size() -1);
			//uiGenerator.generateUI(drawPanel, new Connections().getDrawing());			//frame.add(drawPanel, BorderLayout.SOUTH);			showDrawPanel();						//drawPanel.setBackground(Color.white);
			//int drawHeight = drawPanel.getBounds().height;
			Dimension mySize = frame.getSize();  			//System.err.println(drawHeight);
			frame.setSize(Math.max(mySize.width, SLComposer.FRAME_WIDTH), mySize.height + SLComposer.FRAME_HEIGHT);
			//frame.setSize(300, 300);			mySize = getSize();
			//System.err.println(mySize);		}   catch (Exception e) {
			System.err.println(e);		}		
	}
	*/	public void maybeHideMainPanel() {		/*
		if (!foundGenericWidget(mainScrollPane))		hideMainPanel();		*/
	}	boolean emptyMainPanel = false;
	public void emptyMainPanel() {
		if (emptyMainPanel) return;		emptyMainPanel = true;		hideMainPanel();
	}
	
	public boolean isEmptyMainPanel() {	
		//if (getMainContainer() == null) return false;
		//return ((VirtualContainer) this.getMainContainer().getComponent(0)).countComponents() == 0;
		ObjectAdapter topAdapter = frame.getTopAdapter();
		if (topAdapter != null &&  
				!topAdapter.unparseAsToString() &&
				!topAdapter.isDynamic() && 
				topAdapter.getWidgetAdapter() != null && 
				topAdapter.getWidgetAdapter().isEmpty())
			return true;
		VirtualContainer mainContainer = getMainContainer();
		if (mainContainer == null) return false;
		if (mainContainer instanceof VirtualSplitPane) return false;
		if (mainContainer instanceof VirtualScrollPane) {
			VirtualComponent contents = ((VirtualScrollPane) mainContainer).getScrolledComponent();
			if (contents instanceof VirtualContainer) {
				return ((VirtualContainer) contents).countComponents() == 0;
			}
		}
			 
				return false;
		
	
	}	
	public void drawPanel() {
		/*
		if (frame.getDrawPanel()   == null) {
			frame.createDrawPanel();		}   else 
		*/
	   if (drawPanelIsVisible)			hideDrawPanel();
		else
			showDrawPanel();		
		/* removed feb 16, 01		}   else
		drawPanel.setVisible(!drawPanel.isVisible());\		*/
		frame.validate();	}
	boolean mainScrollPaneIsVisible =   false;	public void mainPanel() {
				
		/*
		if (frame.getMainScrollPane() ==   null)			//frame.createNewChildPanelInNewScrollPane();
			uiGenerator.generateUIScrollPane(frame, "Selection in tree panel expanded here", null, null);			//return;
		//if (mainScrollPane.isVisible())
		*/		if (mainScrollPaneIsVisible)
			//mainScrollPane.setVisible(false);			hideMainPanel();
		else {			if (frame.getMainScrollPane() == null)
			frame.createMainScrollPane();
			//mainScrollPane.setVisible(true);  
						showMainPanel();		}
		frame.validate();	}	public void secondaryPanel() {
		/*		
		if (frame.getSecondaryScrollPane() ==   null) 			frame.createSecondaryScrollPane();		*/
		//if (mainScrollPane.isVisible())		if (secondaryScrollPaneIsVisible)
			//mainScrollPane.setVisible(false);			hideSecondaryScrollPane();
		else
			//mainScrollPane.setVisible(true);  			showSecondaryScrollPane();
		frame.validate();	}
	public void toolBar()   {
		//if (mainScrollPane.isVisible())		if ((toolBarIsVisible) && (frame.getShowMenuBar())) //if you're showing the menubar then 			//it's ok to take the toolbar off cuz they			//can show it later...			//if not then you should definetly show it.
			//mainScrollPane.setVisible(false);			hideToolBar();
				
		else
			//mainScrollPane.setVisible(true);  			showToolBar();		
		frame.validate();
		//setSize();	}
	static final String MAIN_MESSAGE = "Selection in tree panel expanded here.";
	static final String SECONDARY_MESSAGE = "Selection in main panel expanded here.";
	public void showMainPanel() {
		boolean newPanelCreated = false;
		if (mainScrollPaneIsVisible) return;
		//mainScrollPaneIsVisible = true;
		//if (frame.getMainScrollPane() == null) return;
		if (frame.getMainScrollPane() == null) return;
		if (isEmptyMainPanel()) return;
			//frame.createMainScrollPane();		
			
		//if (mainScrollPaneIsVisible) return;
		/* removed Feb 16
		if (tempCenterPanel != null) {
		frame.remove(frame.getComponent(1));
		tempCenterPanel.setVisible(false);
		}
		*/
		if (drawPanelIsVisible)
			frame.add(frame.getMainScrollPane(), BorderLayout.SOUTH);
		else {
			//frame.setVisible(false);
			
			if (treePanelIsVisible) {
				frame.remove(frame.getTreePanel());
				frame.add(frame.getTreePanel(), BorderLayout.WEST);
			} else if (toolBarIsVisible) {
				frame.remove(frame.getToolBar());
				frame.add(frame.getToolBar(), BorderLayout.NORTH);
			}
			frame.add(frame.getMainScrollPane(), BorderLayout.CENTER);			
			//if (newPanelCreated) frame.doubleWidth();
			frame.doubleWidth();
			frame.validate();
			//frame.setVisible(true);
		}
		//frame.add(mainScrollPane, BorderLayout.CENTER);
		mainScrollPaneIsVisible = true;
	}
			public boolean emptyFrame() {		//return frame.getComponentCount() ==   0;
		return !toolBarIsVisible && !mainPanelIsVisible() && !drawPanelIsVisible() && !treePanelIsVisible();	}	
	public boolean maybeSetEmptyFrameSize() {		if (emptyFrame())   {
			frame.setSize(EMPTY_FRAME_WIDTH, EMPTY_FRAME_HEIGHT);			return true;
		}   
		return false;         
	}	public boolean frameIsToolBar() {
		return /*frame.getComponentCount() == 1 &&*/ toolBarIsVisible && !mainPanelIsVisible() && !drawPanelIsVisible() && !treePanelIsVisible(); 	}	public int numChars(VirtualContainer toolBar)   {
		int numChars = 0;
		VirtualComponent[] components = frame.getToolBar().getComponents();		for (int i = 0; i < components.length; i++) {
			numChars    += ((JButton) components[i]).getLabel().length(); 		}
		return numChars;	}
	public boolean maybeSetToolBarSize() {		if (frameIsToolBar() && !sizeSet)   {        
			//frame.setSize(numChars(frame.getToolBar())*CHAR_WIDTH, TOOLBAR_HEIGHT);
			frame.setSize(FRAME_WIDTH, TOOLBAR_HEIGHT);			return true;
		}   
		return false;         
	}	boolean sizeSet = false;
	public void setSize()   {
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		/*		if (!maybeSetEmptyFrameSize()   && !maybeSetToolBarSize() && !sizeSet) {			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			sizeSet = true;		}
		*/
	}
	
	public final static int MAX_X = 1400;
	public final static int MAX_Y = 800;
	public void setLocation() {
//		frame.setLocation(MAX_X, MAX_Y);
		uiFrame lastFrame = uiFrameList.previousFrame();
		if (lastFrame == null) {
			frame.setLocation (FIRST_FRAME_X, FIRST_FRAME_Y);
		} else if (lastFrame.getFrame() != null && lastFrame.getFrame().getX() + lastFrame.getFrame().getWidth() < MAX_X) {
			frame.setLocation (lastFrame.getFrame().getX() + lastFrame.getFrame().getWidth(), FIRST_FRAME_Y);
//					lastFrame.getFrame().getY() + lastFrame.getFrame().getWidth());
		} else if (lastFrame.getFrame() != null && lastFrame.getFrame().getY() + lastFrame.getFrame().getHeight() < MAX_Y) {
			frame.setLocation (FIRST_FRAME_X, lastFrame.getFrame().getY() + lastFrame.getFrame().getHeight());
		} else {
			frame.setLocation (FIRST_FRAME_X, FIRST_FRAME_Y);
		}
		
	}	
	VirtualComponent   tempCenterPanel;
	public void hideMainPanelWithoutRearranging()   {
		if (frame.getMainScrollPane() !=   null)
			frame.remove(frame.getMainScrollPane());      
		mainScrollPaneIsVisible =   false;	}	public void hideMainPanel() {
		hideMainPanelWithoutRearranging();		/*
		if (mainScrollPane !=   null)
		frame.remove(mainScrollPane);      
		mainScrollPaneIsVisible =   false;		*/
		if (!drawPanelIsVisible && treePanelIsVisible){

			//segi  //leave at EAST.			frame.remove(frame.getTreePanel());			frame.add(frame.getTreePanel(), BorderLayout.CENTER);
			//frame.resize(TOOLBAR_WIDTH, TOOLBAR_HEIGHT);		}   else if (!drawPanelIsVisible && toolBarIsVisible){
			//segi //never goes to the center anymore			//frame.remove(toolBar);			//frame.add(toolBar,   BorderLayout.CENTER);

			maybeSetToolBarSize();
			frame.setSize(TOOLBAR_WIDTH,   TOOLBAR_HEIGHT);
		}
		setSize();		//maybeSetEmptyFrameSize();
		/*		if (frame.getComponentCount() != 2) return;		tempCenterPanel =   frame.getComponent(1);
		frame.remove(tempCenterPanel);		frame.add(tempCenterPanel,   BorderLayout.CENTER);		*/    	}
	public boolean mainPanelIsVisible() {		return this.mainScrollPaneIsVisible;
	}	/*
	Panel   windowHistoryPanel;
	public void createWindowHistoryPanel() {		windowHistoryPanel = new Panel();		windowHistoryPanel.setLayout(new BorderLayout());
		try {
			//bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel", "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");      			//uiGenerator.generateUI(uigenPanel, new slm.SLModel());
			uiGenerator.generateInUIPanel(frame,  frame.getAdapterHistory(), windowHistoryPanel);
			//uiGenerator.generateUI(uigenPanel, new Connections().getDrawing());			frame.add(windowHistoryPanel, BorderLayout.EAST);
			currentObjects.addElement(frame.getAdapterHistory());			//uigenPanel.setBackground(Color.white);
			//int drawHeight = uigenPanel.getBounds().height;
			//Dimension mySize = frame.getSize();    			//System.err.println(drawHeight);
			//frame.setSize(mySize.width, mySize.height*2);		}   catch (Exception e) {
			System.err.println(e);		}
	}
	*/	boolean drawPanelIsVisible = false;
	
	public void showDrawPanel() {		if (drawPanelIsVisible) return;
		drawPanelIsVisible = true;
		//if (frame.manualDrawContainer()) return;
		if (!frame.drawPanelIsInitialized()) {
			frame.createDrawPanel();
			if (frame.manualDrawContainer()) return;
			drawInternalFrame =  maybeCreateInternalFrame (frame.getDrawPanel());
		}
		if (frame.manualDrawContainer()) return;
		if (drawInternalFrame != null)
			drawInternalFrame.setVisible(true);
		else  
		//if (frame.getDrawPanel()   == null) frame.createDrawPanel();
		showDrawPanelImpl();
	     
		/*		frame.add(frame.getDrawPanel(), BorderLayout.CENTER);
		if (mainScrollPaneIsVisible) {			//hideMainPanelWithoutRearranging();
			frame.remove(frame.getMainScrollPane());			//frame.add(drawPanel, BorderLayout.CENTER);
			if (!isEmptyMainPanel())
			frame.add(frame.getMainScrollPane(), BorderLayout.SOUTH);			//showMainPanelSouth();
		}   else if (treePanelIsVisible) {
			frame.remove(frame.getTreePanel());			//frame.add(drawPanel,   BorderLayout.CENTER);
			
			//segi leave at west.			frame.add(frame.getTreePanel(), BorderLayout.WEST);						
		}   else if (toolBarIsVisible) {
			frame.remove(frame.getToolBar());			//frame.add(drawPanel,   BorderLayout.CENTER);			frame.add(frame.getToolBar(),   BorderLayout.NORTH);
		}
		*/	}
	public void showDrawPanelImpl() {
		/*
		if (drawPanelIsVisible) return;
		drawPanelIsVisible = true;
		if (frame.manualDrawContainer()) return;
		*/
		if (frame.getDrawPanel()   == null) frame.createDrawPanel();
		DrawingPanelAdditionStarted.newCase(frame, this);
		frame.add(frame.getDrawPanel(), BorderLayout.CENTER);
		if (mainScrollPaneIsVisible) {
			//hideMainPanelWithoutRearranging();
			frame.remove(frame.getMainScrollPane());
			//frame.add(drawPanel, BorderLayout.CENTER);
			if (!isEmptyMainPanel())
			frame.add(frame.getMainScrollPane(), BorderLayout.SOUTH);
			//showMainPanelSouth();
		}   else if (treePanelIsVisible) {
			frame.remove(frame.getTreePanel());
			//frame.add(drawPanel,   BorderLayout.CENTER);
			
			//segi leave at west.
			frame.add(frame.getTreePanel(), BorderLayout.WEST);
			
			
		}   else if (toolBarIsVisible) {
			frame.remove(frame.getToolBar());
			//frame.add(drawPanel,   BorderLayout.CENTER);
			frame.add(frame.getToolBar(),   BorderLayout.NORTH);
		}
		DrawingPanelDisplayEnded.newCase(frame.getDrawPanel(), this);
	}
	boolean toolBarInCenter = false;
	//boolean toolPanelAsToolBar = false;
	void maybeCenterToolBar() {
		if (toolBarIsVisible && !drawPanelIsVisible && !mainScrollPaneIsVisible && !treePanelIsVisible) {
			frame.remove(frame.getToolBar());
			frame.add(frame.getToolBar(), BorderLayout.CENTER);
			toolBarInCenter = true;
		}
		
	}
	void maybeUnCenterToolBar() {
		if (!toolBarInCenter) return;
		toolBarInCenter = false;
		frame.remove(toolbarManager.getToolBarOrPanel());		
		frame.add(toolbarManager.getToolBarOrPanel(),   BorderLayout.NORTH);
		
	}	public void hideDrawPanel() {
		if (!drawPanelIsVisible())
			return;
		drawPanelIsVisible = false;
		if (drawInternalFrame != null)
			drawInternalFrame.setVisible(false);
		else
		  hideDrawPanelImpl();			}
	public void hideDrawPanelImpl() {
		frame.remove(frame.getDrawPanel());
		if (mainScrollPaneIsVisible)    {
			frame.remove(frame.getMainScrollPane());
			frame.add(frame.getMainScrollPane(), BorderLayout.CENTER);
		} else if (treePanelIsVisible) {
			frame.remove(frame.getTreePanel());
			frame.add(frame.getTreePanel(), BorderLayout.CENTER);
			//setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);
		} else if (toolBarIsVisible)    {
			frame.remove(frame.getToolBar());
			frame.add(frame.getToolBar(),    BorderLayout.CENTER);
			//setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);
		}
		setSize();
		//maybeSetEmptyFrameSize();
		/*
		
		if (frame.getComponentCount() < 2)   return;
		tempCenterPanel =   frame.getComponent(1);
		frame.remove(tempCenterPanel);
		frame.add(tempCenterPanel,   BorderLayout.CENTER);   
		drawPanelIsVisible = false;
		*/
	}	
	public void windowHistoryPanel()   {
		if (frame.getWindowHistoryPanel() ==   null) {
			frame.createWindowHistoryPanel();		}   else
			frame.getWindowHistoryPanel().setVisible(!frame.getWindowHistoryPanel().isVisible());
		frame.validate();	}
	/*		JTree   jTree;
	ScrollPane treePanel;
	uiObjectAdapter rootTreeNode;
	
	public JTree getJTree() {		return jTree;
	}
	*/
	/*		public void createTreePanel(uiObjectAdapter adapter) {		if (treePanel   == null) {
			treePanel   = new ScrollPane(); 			treePanel.setName(frame.TREE_PANEL_NAME);
			//treePanel.setLayout(new   BorderLayout());
			
			rootTreeNode = adapter;			//jTree =   new JTree(adapter);			try {
			jTree   = new JTree(frame);			treePanel.add(jTree);
									jTree.addMouseListener(frame);			jTree.addTreeSelectionListener(frame);
			jTree.addTreeExpansionListener(frame);   
			treePanel.setSize((int) 200, frame.getSize().height);			treePanelIsVisible = false;
			showTreePanel();			//frame.add(treePanel,BorderLayout.WEST);
			} catch (Exception e) {				System.err.println("createTreePanel " + e);
			}		}			}	*/
	public boolean treePanelIsVisible = false;
		public void treePanel() {
		//if (mainScrollPane.isVisible())		if (treePanelIsVisible)
			//mainScrollPane.setVisible(false);			hideTreePanel();
		else
			//mainScrollPane.setVisible(true);  			showTreePanel();
		frame.validate();	}	public void setTreePanelIsVisible (boolean newVal) {
		treePanelIsVisible = newVal;	}
	public void hideTreePanelImpl() {
		//if (!treePanelIsVisible) return;
		//treePanelIsVisible = false;		//if (treePanelIsVisible) {
			frame.remove(frame.getTreePanel());			//toolBar.setVisible(false);			//treePanelIsVisible = false;
		//}		if (toolBarIsVisible && !drawPanelIsVisible && !mainScrollPaneIsVisible) {
			frame.remove(frame.getToolBar());
			frame.add(frame.getToolBar(), BorderLayout.CENTER);
			//setSize(frame.TOOLBAR_WIDTH,   frame.TOOLBAR_HEIGHT);		}
		setSize();		
	}
	boolean secondaryScrollPaneIsVisible = false;
		/*
	public void createSecondaryContainer() {
			if (sescondartScrollPane != null) return;			secondaryScrollPane = topViewManager.newSecondaryContainer();
			secondaryScrollPane.setName(this.SECONDARY_PANEL_NAME);			uiGenerator.generateInBrowsableContainer(frame, SECONDARY_MESSAGE, secondaryScrollPane);		
	}
	*/		public void showSecondaryScrollPane() {
		boolean createdSecondaryScrollPane = false;		if (secondaryScrollPane == null) {			frame.createSecondaryScrollPane();
			createdSecondaryScrollPane = true;			/*
			secondaryScrollPane = topViewManager.newSecondaryContainer();
			secondaryScrollPane.setName(this.SECONDARY_PANEL_NAME);			uiGenerator.generateInBrowsableContainer(frame, SECONDARY_MESSAGE, secondaryScrollPane);			*/
		}						//frame.createSecondaryScrollPane();
		if (secondaryScrollPaneIsVisible) return;
		frame.add(frame.getSecondaryScrollPane(), BorderLayout.EAST);
		/*		if (drawPanelIsVisible)
			frame.add(frame.getSecondaryScrollPane(), BorderLayout.EAST);		else			frame.add(frame.getSecondaryScrollPane(), BorderLayout.SOUTH);
			*/
		secondaryScrollPaneIsVisible = true;	}	public void hideSecondaryScrollPane() {
		if (!secondaryScrollPaneIsVisible) return;
		frame.remove(frame.getSecondaryScrollPane());
		secondaryScrollPaneIsVisible = false;	}	public boolean secondaryScrollPaneIsVisible() {
		return secondaryScrollPaneIsVisible;	}
	
	
	
	VirtualInternalFrame treeInternalFrame, drawInternalFrame;
	//boolean frameIsDesktop = false;
	
	VirtualInternalFrame maybeCreateInternalFrame (VirtualContainer c) {
		VirtualDesktopPane desktop = frame.getDesktop();
		if (desktop == null) return null;
		//frameIsDesktop = true;
		VirtualInternalFrame internalFrame = InternalFrameSelector.createInternalFrame();
		internalFrame.setContentPane(c);
		desktop.add(internalFrame);
		return internalFrame;
		
	}
	public void showTreePanel () {
		if (frame.manualTreeContainer()) return;
		if (treePanelIsVisible) return;
		setTreePanelIsVisible (true);
		
		if (frame.getTreePanel()   == null) {
			frame.createTreePanel();
			treeInternalFrame =  maybeCreateInternalFrame (treePanel);
		}
		
		if (treeInternalFrame != null)
			treeInternalFrame.setVisible(true);
		else
			showTreePanelImpl();
	}
	public void hideTreePanel() {
		if (frame.manualTreeContainer()) return;
		if (!treePanelIsVisible) return;
		treePanelIsVisible = false;		
		
		if (treeInternalFrame != null)
			treeInternalFrame.setVisible(false);
		else
			hideTreePanelImpl();
	}
		public void showTreePanelImpl() {
		/*
		if (frame.manualTreeContainer()) return;
		if (treePanelIsVisible) return;
		treePanelIsVisible = true;		
		if (frame.getTreePanel()   == null) {
			frame.createTreePanel();
			treeInternalFrame =  maybeCreateInternalFrame (treePanel);
		}
				//if (!treePanelIsVisible) {
			if (treeInternalFrame != null) {
				treeInternalFrame.setVisible(true);
			} 
			*/
		
			if (drawPanelIsVisible ||  mainScrollPaneIsVisible )				//segi leave at west
				if (frame.getContainer().getLayout() instanceof BorderLayout)				frame.add(frame.getTreePanel(), BorderLayout.WEST);
				else
					frame.add(frame.getTreePanel());
			
						else if (toolBarIsVisible) {
				frame.remove(frame.getToolBar());
								//segi leave west
				frame.add(frame.getTreePanel(), BorderLayout.CENTER);
				
				
								frame.add(frame.getToolBar(),   BorderLayout.NORTH);
				//frame.setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);			}   else if (frame.getComponentCount() > 0)				frame.add(frame.getTreePanel(), BorderLayout.WEST);			else
				//segi leave west
				frame.add(frame.getTreePanel(), BorderLayout.CENTER);
						//toolBar.setVisible(true);			//treePanelIsVisible = true;			
		//}		
	}
		/*
	
	public void treeExpanded(TreeExpansionEvent event) {		//System.err.println("Tree expanded");
	}
	public void treeCollapsed(TreeExpansionEvent event) {		//System.err.println("tree expanded");
	}
	boolean treeSelected = false;	public void valueChanged(TreeSelectionEvent e)  {
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
		uiSelectionManager.replaceSelections(selectedTreeNodes);		setTitle();
				
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
				if ((jTree !=   null) && treePanelIsVisible)			jTree.setSelectionPaths(selectedPaths);
				
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
			 elements.hasMoreElements();)			((TreeModelListener) elements.nextElement()).treeStructureChanged(new   TreeModelEvent(frame,path )); 	}
		public int getChildAdapterCount(Object parent) {
		if(ObjectEditor.colabMode() && ObjectEditor.coupleElides){			getChildAdapterCountReq = new AutomaticRefresh(frame,(uiObjectAdapter) parent);		} else if (ObjectEditor.colabMode()){
			getChildAdapterCountReq = new AutomaticRefresh(frame,((uiObjectAdapter) parent).getPath(),"getChildAdapterCount");		}
		int retVal = subgetChildAdapterCount(parent);
		getChildAdapterCountReq = null;		return(retVal);
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
	*/				public void maybeShowJTree() {		ObjectAdapter adapter =   frame.getOriginalAdapter();
		if (//adapter.getHeight() > DEEP_ELIDE_LEVEL +   1 ||			new HasUncreatedChildrenVisitor(adapter).traverse().contains(Boolean.TRUE))
			frame.createTreePanel(adapter);	}

	/*
	Menu fileMenu;
	//
	// Create   the menuBar  ...have overloaded because may not wanna pass in a MenuSetter object if not given in	// the constructor that takes in just the object.
	//
	*/			
	boolean toolBarIsVisible = false;
	//boolean toolBarIsVisible = true;			/*	private void createToolBar() {
		
		//we want a toolbar with these special additions to be added only the 1st time		//also we do not want the toolbars to be vertical if the object doesn't have any defined 		//toolbars other than native one.				//we can check somehow if there are several ToolGroups that have been customized and		//then create another toolbar panel layer so that the one doesn't go off the screen.		//would frame be in classdescriptor or the beaninfo file of the object.						//you may also be able to set the layout for the toolbar (see JToolbar.setLayout) which		//may then help in arranging framee buttons in a more pleasant manner if too many of ther		//are there for frame current design.
		
		if (uiFrame.toolbarCount == 0) {									GridLayout toolGrid = new GridLayout(1,toolbars.size(),0,0);			//FlowLayout toolGrid  = new FlowLayout(FlowLayout.LEFT,0,0);
						//toolGrid.setHgap(0);			//toolGrid.setVgap(0);			
			toolPanel = new JPanel(toolGrid);
			frame.isOEMainFrame = true;		}		else 					toolPanel = new JPanel(new FlowLayout());		
		uiFrame.toolbarCount++;  //new toolbar is created		//toolBar = new JToolBar(javax.swing.SwingConstants.VERTICAL);
		toolBar = new JToolBar(javax.swing.SwingConstants.HORIZONTAL);
		
				//toolBar   = new JPanel();
		//toolBar.setVisible(false);		toolBarIsVisible = true; //lets make  it visible by default		//toolBar.setName(frame.TOOLBAR_PANEL_NAME);
				//the idea is that the base toolbar is created already.  so frame put in early to the hashtable of toolbars
				toolbars.put(frame.TOOLBAR_PANEL_NAME, toolBar);		//frame.showToolBar();		
	}
	*/	//JPanel toolPanel ;	
	public void hideToolBar()   {		if (toolBarIsVisible)   {			// why was frame commented out? A toolbar is displayed.
			frame.remove(frame.getToolBar());
			frame.remove(frame.getToolPanel());
			frame.remove( (toolbarManager.getManualToolbar()));			//toolBar.setVisible(false);			toolBarIsVisible = false;
		}		maybeSetToolBarSize();		
	}
	/*
	JToolBar toolBar;
	JPanel toolPanel;
	Container manualToolbar;
	Hashtable toolbars = new Hashtable();
	boolean hasManualToolbar = false;
	public void useManualToolbar (boolean newVal) {
		hasManualToolbar = newVal;
	}
	public JToolBar getToolBar () {
		return toolBar;
	}
	public JPanel getToolPanel() {
		return toolPanel;
	}
	*/
	/*
	Component getToolBarOrPanel() {
		if (toolPanelAsToolBar)
			return getToolPanel();
		else
			return getToolBar();
		
	}
	*/
	/*
	public Container createManualToolbar() {		
		Container buttons = ComponentPanel.createButtonPanel(uiGenerator.getCommands(frame.getTopAdapter().getRealObject()));
		return buttons;
		//frame.add(buttons, toolbarOrientation());
		//return ;
		
	}
	*/
	/*
	public void addManualToolbar() {		
		Container buttons = ComponentPanel.createButtonPanel(uiGenerator.getCommands(frame.getTopAdapter().getRealObject()));
		frame.add(buttons, toolbarOrientation());
		return ;
		
	}
	*/
	
	String toolbarOrientation () {
		if (frame.getComponentCount() != 0)  return BorderLayout.NORTH;
		//if (drawPanelIsVisible ||   mainScrollPaneIsVisible || treePanelIsVisible) return BorderLayout.NORTH;
		return BorderLayout.CENTER;
	}
	/*
	public Container createManualToolbar() {
		if (manualToolbar != null) return manualToolbar;
		manualToolbar  = ComponentPanel.createButtonPanel(uiGenerator.getCommands(frame.getTopAdapter().getRealObject()));
		return manualToolbar;
		
	}
	*/
	 //static int toolbarCount = 0;
	/*
public void createToolBar() {
		
		//we want a toolbar with these special additions to be added only the 1st time
		//also we do not want the toolbars to be vertical if the object doesn't have any defined 
		//toolbars other than native one.
		
		//we can check somehow if there are several ToolGroups that have been customized and
		//then create another toolbar panel layer so that the one doesn't go off the screen.
		//would this be in classdescriptor or the beaninfo file of the object.
		
		
		//you may also be able to set the layout for the toolbar (see JToolbar.setLayout) which
		//may then help in arranging thise buttons in a more pleasant manner if too many of ther
		//are there for this current design.
		
		if (toolbarCount == 0) {
			
			
			GridLayout toolGrid = new GridLayout(1,toolbars.size(),0,0);
			//FlowLayout toolGrid  = new FlowLayout(FlowLayout.LEFT,0,0);
			
			//toolGrid.setHgap(0);
			//toolGrid.setVgap(0);
			
			toolPanel = new JPanel(toolGrid);
			frame.isOEMainFrame = true;
		}
		else 		
			toolPanel = new JPanel(new FlowLayout());
		
		toolbarCount++;  //new toolbar is created
		//toolBar = new JToolBar(javax.swing.SwingConstants.VERTICAL);
		toolBar = new JToolBar(javax.swing.SwingConstants.HORIZONTAL);
		
		
		//toolBar   = new JPanel();
		//toolBar.setVisible(false);
		//AVM toolBarIsVisible = true; //lets make  it visible by default
		//toolBar.setName(this.TOOLBAR_PANEL_NAME);
		
		//the idea is that the base toolbar is created already.  so this put in early to the hashtable of toolbars
		
		toolbars.put(this.TOOLBAR_PANEL_NAME, toolBar);
		//this.showToolBar();
		
	}
	*/
//Vector methodActions = new Vector();
//get a toolbar...if it's not already made, then make a new one and add it.	
	/*
private JToolBar getToolBar(String name)   {

	JToolBar tb   = (JToolBar) toolbars.get(name);
	if (tb == null)   { //then have to make one
		//if (name.equals(methodsMenuName))   {
		//tb = new JToolBar(javax.swing.SwingConstants.VERTICAL);
		tb = new JToolBar(javax.swing.SwingConstants.HORIZONTAL);
		//toolBar   = new JPanel();
		//toolBar.setVisible(false);
		
		tb.setName(name);
		
		//the idea is that the base toolbar is created already.  so this put in early.
		
		toolbars.put(name, tb);

		//menu = new uiExtendedMenu(name);
		//menus.put(name, menu);
		//menuBar.add(menu);
		
		//toolPanel.add(new Label(name));
		//toolPanel.add(tb);

		//Box toolGroup = new Box(javax.swing.BoxLayout.Y_AXIS);
		//BoxLayout tGLay = new BoxLayout(toolGroup, javax.swing.BoxLayout.Y_AXIS);									  
		//toolGroup.setLayout(tGlay);

		JPanel toolGroup = new JPanel();
		BoxLayout tGLay = new BoxLayout(toolGroup, javax.swing.BoxLayout.Y_AXIS);									  
		toolGroup.setLayout(tGLay);
		//toolGroup.setBorder(BorderFactory.createEtchedBorder());
		toolGroup.setBorder(BorderFactory.createRaisedBevelBorder());

		
		JLabel tbname = new JLabel(name.toUpperCase(),javax.swing.SwingConstants.CENTER);
		tbname.setVerticalAlignment(SwingConstants.TOP);
		toolGroup.add(tbname);
		
		//toolGroup.add(new Label(name));
		toolGroup.add(tb);
		
		toolPanel.add(toolGroup);
	}
	return tb;
}
*/
/*
public JButton addToolBarButton(String label,   Icon icon, VirtualMethod method, String place_toolbar, int pos) {
	return addToolBarButton (null, label, icon, method, place_toolbar, pos );
}
//
// Add a ToolBar item
//

public JButton addToolBarButton(Object targetObject, String label,   Icon icon, VirtualMethod method, String place_toolbar, int pos) {
	//toolBar.setVisible(true);
	//showToolBar();
	//toolBar.setFloatable(true);
	// why do we need  this line, I am commenting it out
	if (frame.getBrowser().getMenuAdapter() == null) return null; 
	//Vector methodActions = browser.getTopAdapter().getMethodActions();
	Vector methodActions = frame.getBrowser().getMenuAdapter().getMethodActions();
	VirtualMethodAction methodAction   = new VirtualMethodAction(frame, targetObject, label, icon, method);
	//return toolBar.add(new MethodAction(this, label, icon, method));
	VirtualMethodAction existing   = methodAction.getDuplicate(methodActions);
	if (existing != null)   { 
		existing.addMethod(method);
		return null;
	}   else {
		methodActions.addElement(methodAction);
		
		
		
		//here you should get the toolbar.
		JToolBar inTB = getToolBar(place_toolbar);  //i guess if it's not there already...then add it.
		
		//JButton button  = toolBar.add(methodAction);   //Toolbar here.
		//JButton button = null;
		//if (pos == -1)
			JButton button = inTB.add(methodAction);   //Toolbar here.
		
			
		
		methodAction.setButton(button);
		return  button;
	}
}
void printToolbarButtons() {
	Component[] comps   = toolBar.getComponents();
	System.err.println("Toolbar Buttons");
	for (int i=0;   i<comps.length; i++)
		System.err.println(((JButton)   comps[i]).getLabel());
}
public void removeToolBarButtons(uiObjectAdapter adapter)   {   
	System.err.println("removing buttons of" + adapter.getMethodActions());     
	printToolbarButtons();
	Vector methodActions = adapter.getMethodActions();
	for (int i=0;   i < methodActions.size(); i++) {
		VirtualMethodAction ma =   (VirtualMethodAction) methodActions.elementAt(i);
		//System.err.print(" rembut" + ma.getButton().getLabel());
		//System.err.print("index" + indexOf(toolBar.getComponents(),   ma.getButton()));
		toolBar.remove(frame.indexOf(toolBar.getComponents(), ma.getButton()));
		//toolBar.remove(ma.getButton());
	}
	
}
public void addToolBarButtons(uiObjectAdapter   adapter) {
	//System.err.println("adding buttons of" + adapter.getMethodActions());
	System.err.println("Toolbar Buttons");
	Vector methodActions = adapter.getMethodActions();
	for (int i=0;   i < methodActions.size(); i++) {
		VirtualMethodAction ma =   (VirtualMethodAction) methodActions.elementAt(i);
		//System.err.print("addbut" + ma.getButton().getLabel());
		toolBar.add(ma.getButton());
	}
	//validate();
	//htoolBar.setVisible(false);
	toolBar.setVisible(true);
	
}
*/
//
// Add a UI frame   command
//
/*
JButton forwardButton, backButton;
public void addUIFrameToolBarButton(String label,   Icon icon) {
	addUIFrameToolBarButton(label, icon, frame);
	
}
public JButton addUIFrameToolBarButton(String   label, Icon icon, ActionListener listener) {
	System.err.println("Adding Button " + label);
	if (toolBarButtons.contains(label)) return null;
	//showToolBar();
	//toolBar.setVisible(true);
	//toolBar.setFloatable(true);
	JButton button = new JButton(label, icon);
	if (listener != null)
		button.addActionListener(listener);   
	toolBarButtons.addElement(label);   
	if (label   == frame.SAVE_COMMAND){
		frame.setSaveButton(button);
		toolBar.add(button);
	}    else if (label == frame.FORWARD_ADAPTER_NAME) {
		forwardButton   = button;
		toolBar.add(button);
	}   else if (label == frame.BACK_ADAPTER_NAME) {
		backButton = button;
		toolBar.add(button);
	}   else
		toolBar.add(button);
	//toolBar.removeAll();
	return button;
}

//static Object[]   toolBarFrameButtonsArray = {BACK_ADAPTER_NAME, FORWARD_ADAPTER_NAME, DEEP_ELIDE_4, "Refresh" , UPDATE_ALL_COMMAND}; 
static Object[] toolBarFrameButtonsArray = {};
Vector toolBarButtons   = new Vector();
static java.util.Vector toolBarFrameButtons =   uiGenerator.arrayToVector(toolBarFrameButtonsArray);

public static   void addUIFrameToolBarButtons(uiFrame topFrame) {
	for (Enumeration e = toolBarFrameButtons.elements(); e.hasMoreElements();)
		topFrame.addUIFrameToolBarButton((String)   e.nextElement(), null);
	topFrame.hideToolBar();
}
*/
/*
public int toolBarCount() {
	return toolbarCount;
}
*/
//Container manualToolbar;
	/*
public Container getManualToolbar() {
	return manualToolbar;
}

public JButton addUIGenToolBarButton(String label, Icon icon,   Object obj) {
	//showToolBar();
	toolBar.setVisible(true);
	return toolBar.add(new uiGenAction(label, icon, obj));
}
*/
	public void showToolBar() {
		//gonna have to have a toolbar panel.

		//dunno how the below works but for only the first OE frame you want to
		// make sure you've created
		//a toolbar and then somewhere the customized stuff get's put on.
		if (toolBarIsVisible)
			return;
		if (frame.getTopAdapter() != null)
			frame.hideMainIfNoProperties();
		if (toolbarManager.hasManualToolbar()) {
			if (toolbarManager.getManualToolbar() == null)
				frame.createManualToolbar();
			frame.add(toolbarManager.getManualToolbar(), toolbarOrientation());
		} else if ((toolbarManager.toolbarCount() == 1) && (frame.isOEMainFrame)) { //if
																	   // you've
																	   // created
																	   // one
																	   // toolbar
																	   // (main)

			if (frame.getToolPanel() == null) // if the toolbar panel was never
											  // created then create it.
				frame.createToolBar();

			//Box toolGroup = new Box(javax.swing.BoxLayout.Y_AXIS);
			JPanel toolGroup = new JPanel(); //create the panel that will hold
											 // all the toolbar group
			BoxLayout tGLay = new BoxLayout(toolGroup,
					javax.swing.BoxLayout.Y_AXIS);

			toolGroup.setLayout(tGLay);
			//toolGroup.setBorder(BorderFactory.createEtchedBorder());
			toolGroup.setBorder(BorderFactory.createRaisedBevelBorder());

			//JLabel tbname = new
			// JLabel("Tools".toUpperCase(),javax.swing.SwingConstants.CENTER);
			//tbname.setVerticalAlignment(SwingConstants.TOP);
			//toolGroup.add(tbname);
			//toolPanel.removeAll();
			frame.getToolPanel().add(frame.getToolBar());
			//toolBarInCenter = true;
			toolbarManager.setToolPanelAsToolBar(true);
			

			//frame.addToolBarButtons(frame.topAdapter);
			//toolPanel.add(toolGroup);

			//if (!toolBarIsVisible) {
			//if (frame.getTopAdapter() != null)
			//frame.hideMainIfNoProperties();
			//if (drawPanelIsVisible || mainScrollPaneIsVisible ||
			// treePanelIsVisible) {
			//frame.add(toolBar, BorderLayout.NORTH);
			
			Object orientation = toolbarOrientation();

			frame.add(frame.getToolPanel(), orientation);
			frame.getToolPanel().setVisible(true);
			if (orientation == BorderLayout.CENTER)
				toolBarInCenter = true;

		}

		else {
			if (frame.getToolBar() == null)
				frame.createToolBar();

			//if (!toolBarIsVisible) {
			//if (frame.getTopAdapter() != null)
			//frame.hideMainIfNoProperties();
			//if (frame.getComponentCount() != 0)
			//if (drawPanelIsVisible || mainScrollPaneIsVisible ||
			// treePanelIsVisible)
			frame.add(frame.getToolBar(), toolbarOrientation());
			//else {
			//frame.add(frame.getToolBar(), BorderLayout.CENTER);
			//frame.setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);
			//}
			frame.getToolBar().setVisible(true);

		}
		toolBarIsVisible = true;
		//setSize();
		frame.validate();

	}
	
	public void oldShowToolBar() {
		//gonna have to have a toolbar panel.
		
		
		//dunno how the below works but for only the first OE frame you want to
		// make sure you've created
		//a toolbar and then somewhere the customized stuff get's put on.
		//if (manualToolbar) return;
		if ((toolbarManager.toolbarCount() == 1) && (frame.isOEMainFrame)) {  //if you've created one toolbar (main)
			
			if (frame.getToolPanel() == null)  // if the toolbar panel was never created then create it.
				frame.createToolBar();
			
			
			//Box toolGroup = new Box(javax.swing.BoxLayout.Y_AXIS);
			JPanel toolGroup = new JPanel();  //create the panel that will hold all the toolbar group
			BoxLayout tGLay = new BoxLayout(toolGroup, javax.swing.BoxLayout.Y_AXIS);									  
			
			toolGroup.setLayout(tGLay);
			//toolGroup.setBorder(BorderFactory.createEtchedBorder());
			toolGroup.setBorder(BorderFactory.createRaisedBevelBorder());
			
			//JLabel tbname = new JLabel("Tools".toUpperCase(),javax.swing.SwingConstants.CENTER);
			//tbname.setVerticalAlignment(SwingConstants.TOP);
			//toolGroup.add(tbname);
			//toolPanel.removeAll();
			frame.getToolPanel().add(frame.getToolBar());
			
			//frame.addToolBarButtons(frame.topAdapter);
			//toolPanel.add(toolGroup);
			
			
			if (!toolBarIsVisible) {
				if (frame.getTopAdapter() !=   null)
					frame.hideMainIfNoProperties();
				if (drawPanelIsVisible ||   mainScrollPaneIsVisible || treePanelIsVisible) {
					//frame.add(toolBar,   BorderLayout.NORTH);
					
					frame.add(frame.getToolPanel(), BorderLayout.NORTH);
					
					
				}
				else {
					//frame.add(toolBar,   BorderLayout.CENTER);

					//segi  never put in center.
					//frame.add(toolPanel,  BorderLayout.CENTER);
					//frame.setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);
				}                      
				
				//toolBar.setVisible(true);
				frame.getToolPanel().setVisible(true);
				toolBarIsVisible = true;
				
			}       
		}
		
		else {
			if (frame.getToolBar() == null)
				frame.createToolBar();
			
			
			if (!toolBarIsVisible) {
				if (frame.getTopAdapter() !=   null)
					frame.hideMainIfNoProperties();
				if (frame.getComponentCount() != 0)
				//if (drawPanelIsVisible ||   mainScrollPaneIsVisible || treePanelIsVisible)
					frame.add(frame.getToolBar(),   BorderLayout.NORTH);
				else {
					frame.add(frame.getToolBar(),   BorderLayout.CENTER);
					//frame.setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);
				}                      
				frame.getToolBar().setVisible(true);
				toolBarIsVisible = true;
				
			}       
			
		}
		
		setSize();	
		
		
	}
	/*
	public void addToBottomPanel(Container toadd) {  
				//toolPanel.add(toadd);   //for now let's do a simple add at end of flow				frame.add(toadd, BorderLayout.SOUTH);
		
		if (toadd != null)			System.err.println("added a toolpanel to orig");			}	public void addToMiddlePanel(Container toadd) {  
				//toolPanel.add(toadd);   //for now let's do a simple add at end of flow				frame.add(toadd, BorderLayout.CENTER);
		
		if (toadd != null)			System.err.println("added a toolpanel to mid - orig");			}
	*/	public boolean treePanelIsVisible() {		return treePanelIsVisible;
	}
			}
