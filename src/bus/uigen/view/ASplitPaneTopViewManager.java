package bus.uigen.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JSplitPane;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.SplitPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualScrollPane;
import bus.uigen.widgets.VirtualSplitPane;

public class ASplitPaneTopViewManager extends ATopViewManager  {
			/*	public static   final String MAIN_PANEL_NAME = "main"; 	public static   final String DRAW_PANEL_NAME = "draw"; 	public static   final String TREE_PANEL_NAME = "tree"; 
	public static   final String TOOLBAR_PANEL_NAME = "toolbar"; 
	
	public static   final int TOOLBAR_HEIGHT = 75;
	public static   final int TOOLBAR_WIDTH = 350;
	public static   final int CHAR_WIDTH = 9;	
	public static   final int FRAME_HEIGHT = 250;
	public static   final int FRAME_WIDTH = 325;
	public static   final int EMPTY_FRAME_HEIGHT = 40;
	public static   final int EMPTY_FRAME_WIDTH = 250;
	*/	/*	//i'm using frame so that the toolbar grouping stuff only happens once (the main object window)	public static int toolbarCount = 0;	
	uiFrame frame;	
	public void init (uiFrame theFrame) {
		frame = theFrame;	}	*/
	/*	public Vector dynamicContainers = new Vector();	
	public Vector containers() {		Vector containers = new Vector();		if (dynamicContainers.size() == 0) return null;
		if (this.treePanelIsVisible())			containers.addElement(frame.getTreePanel());		for (int index = 0; index < dynamicContainers.size(); index ++) {
			containers.addElement(dynamicContainers.elementAt(index));		}		if (frame.secondaryScrollPaneIsVisible())			containers.addElement(frame.getSecondaryScrollPane());		return containers;
	}
	public Container containerAt(int index) {		return (Container) containers().elementAt(index);
	}
		public int numContainers() {
		return containers().size();	}
	public Container getTreeContainer() {		return this.treePanel;
	}
	public Container getSecondaryContainer() {		return this.secondaryScrollPane;
	}
	
	boolean firstPanel  = true;	boolean secondPanel = false;	Container mainScrollPane;	Container spane;	public Container getMainContainer() {
		return mainScrollPane;
		//return topViewManager.getMainContainer();	}
	public boolean mainContainerIsEmpty() {
		return mainScrollPane.countComponents() == 0;
	}
	*/
	VirtualSplitPane mainSplitPane;
	//public VirtualContainer newContainer(int direction, VirtualComponent scrolledComponent) {
	public VirtualScrollPane newContainer(int direction, VirtualComponent scrolledComponent) {
				VirtualScrollPane newScrollPane = ScrollPaneSelector.createScrollPane(scrolledComponent);
		frame.addKeyListener(newScrollPane);
	
		if (firstPanel) {
			//mainScrollPane = newScrollPane;			mainPane = newScrollPane;//readding this line
//			mainPane.setBackground(AttributeNames.getDefaultOrSystemDefault(AttributeNames.CONTAINER_BACKGROUND));			mainPane.setName(AttributeNames.MAIN_PANEL_NAME + "(ASplitPaneTopViewManager.newContainer)");
			
			
			//frame.add(newScrollPane);
						//segi...above was orignal.  changed to below to put south.
			//this.add(newScrollPane, BorderLayout.SOUTH);									//mainPanel.setTopComponent(newScrollPane); 
			//mainPanel.setTopComponent(newLayeredPane);    			firstPanel = false;			secondPanel = true;
			mainSplitPane = createVirtualSplitPane (JSplitPane.VERTICAL_SPLIT, mainPane);
//			mainSplitPane.setBackground(AttributeNames.getDefaultOrSystemDefault(AttributeNames.CONTAINER_BACKGROUND));

			mainSplitPane.setName("Main SplitPane(ASplitPaneTopViewManager.newContainer)");
			
		}		else {			VirtualContainer parent = spane.getParent();			parent.remove(spane);			//spane.setSize(spane.getWidth(), spane.getHeight());
			//JSplitPane splitPane = new JSplitPane(direction, spane, newScrollPane);
			VirtualSplitPane splitPane = createVirtualSplitPane (direction, spane, newScrollPane);
			/*
			splitPane.setResizeWeight(0.5);
			splitPane.setContinuousLayout(true);
			Dimension d = spane.getMinimumSize();			
			System.out.println ("SplitPane component min size" + d.getHeight() + "," + d.getWidth());
			spane.setMinimumSize(new Dimension(50, 50));
			newScrollPane.setMinimumSize(new Dimension (50, 50));
			 d = spane.getMinimumSize();			
			System.out.println ("SplitPane component min size" + d.getHeight() + "," + d.getWidth());			//JSplitPane splitPane = new JSplitPane(direction);
			 * 
			 */			
			
			//System.out.println("size" + size.width + " " + size.height);			//splitPane.setDividerLocation(0.5);
			//splitPane.setLeftComponent(spane);        
			//splitPane.setRightComponent(newScrollPane);			if (secondPanel) {				parent.add(splitPane);				/*
				if (parent instanceof Frame)
				mainScrollPane = splitPane;
				*/
				//readding this
				if (parent instanceof Frame || parent == treeSplitPane) {
					mainPane = splitPane;
					//mainScrollPane.setName(uiFrame.MAIN_PANEL_NAME);				}
							} else				//((JSplitPane) parent).setRightComponent(splitPane);
				 replaceRightComponent ((VirtualSplitPane) parent, splitPane);						
			//spane.doLayout();
			VirtualDimension size = frame.getSize();
			if (direction == JSplitPane.HORIZONTAL_SPLIT)				frame.setSize(size.getWidth()*2, size.getHeight());			else				frame.setSize(size.getWidth(), size.getHeight()*2);
			//newScrollPane.doLayout();			
			//splitPane.setLeftComponent(newScrollPane);
			//splitPane.setLeftComponent(newLayeredPane);			//mainPanel.setBottomComponent(splitPane);		}
		spane = newScrollPane;		dynamicContainers.addElement(spane);
		//layeredPane = newLayeredPane;		return newScrollPane;    	}
	/*
	public Container newContainer() {		return newContainer(JSplitPane.VERTICAL_SPLIT);
	}
	*/
	/*
	Container secondaryScrollPane;	public Container newSecondaryContainer() {		if (secondaryScrollPane == null) {
			secondaryScrollPane = ScrollPaneSelector.createScrollPane();
			secondaryScrollPane.setName(uiFrame.SECONDARY_PANEL_NAME);
			//uiObjectAdapter adapter = uiGenerator.generateInBrowsableContainer(frame, SECONDARY_MESSAGE, secondaryScrollPane);
			//frame.setSecondaryObjectAdapter(adapter);		}
		return secondaryScrollPane;	}
	Container treePanel;
	*/
	VirtualSplitPane treeSplitPane;
	VirtualSplitPane createVirtualSplitPane () {
		return createVirtualSplitPane (0.5);
		/*
		JSplitPane splitPane =  new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setContinuousLayout(true);
		return splitPane;
		*/
	}
	VirtualSplitPane createVirtualSplitPane (double resizeWeight) {
		//VirtualSplitPane splitPane =  new JSplitPane();
		VirtualSplitPane splitPane =  SplitPaneSelector.createSplitPane();		
		splitPane.setResizeWeight(resizeWeight);
		splitPane.setContinuousLayout(true);
		return splitPane;
	}
	VirtualSplitPane createVirtualSplitPane (int direction, VirtualComponent spane, VirtualComponent newScrollPane) {
		//JSplitPane splitPane = new JSplitPane(direction, spane, newScrollPane);
		VirtualSplitPane splitPane = createVirtualSplitPane();
		splitPane.setOrientation(direction);
		splitPane.setLeftComponent(spane);
		splitPane.setRightComponent(newScrollPane);
		
		//Dimension d = spane.getMinimumSize();			
		//System.out.println ("SplitPane component min size" + d.getHeight() + "," + d.getWidth());
		spane.setMinimumSize(new VirtualDimension(50, 50));
		newScrollPane.setMinimumSize(new VirtualDimension (50, 50));
		return splitPane;
	}
	VirtualSplitPane createVirtualSplitPane (int direction, VirtualComponent spane, double resizeWeight) {
		//JSplitPane splitPane = new JSplitPane(direction, spane, newScrollPane);
		VirtualSplitPane splitPane = createVirtualSplitPane(resizeWeight);
		clearRightComponent(splitPane);
		splitPane.setOrientation(direction);
		splitPane.setLeftComponent(spane);
		/*
		if (splitPane.getLeftComponent().getPhysicalComponent() != spane.getPhysicalComponent())
			splitPane = null;
			*/
		//splitPane.setRightComponent(newScrollPane);
		
		VirtualDimension d = (VirtualDimension) spane.getMinimumSize();			
		//System.out.println ("SplitPane component min size" + d.getHeight() + "," + d.getWidth());
		spane.setMinimumSize(new VirtualDimension(50, 50));
		//newScrollPane.setMinimumSize(new Dimension (50, 50));
		return splitPane;
	}
	VirtualSplitPane createVirtualSplitPane (int direction, VirtualComponent spane) {
		return createVirtualSplitPane (direction, spane, 0.5);
		/*
		//JSplitPane splitPane = new JSplitPane(direction, spane, newScrollPane);
		JSplitPane splitPane = createJSplitPane(resizeWeight);
		splitPane.setOrientation(direction);
		splitPane.setLeftComponent(spane);
		//splitPane.setRightComponent(newScrollPane);
		
		Dimension d = spane.getMinimumSize();			
		//System.out.println ("SplitPane component min size" + d.getHeight() + "," + d.getWidth());
		spane.setMinimumSize(new Dimension(50, 50));
		//newScrollPane.setMinimumSize(new Dimension (50, 50));
		return splitPane;
		*/
	}	public VirtualContainer newTreeContainer() {
		
		if (treePanel == null) {
		super.newTreeContainer();
		treeSplitPane = createVirtualSplitPane (JSplitPane.HORIZONTAL_SPLIT, treePanel, 0.3);
		//treeSplitPane.setResizeWeight(0);
		
		//treeSplitPane.setSize(TREE_WIDTH, 0 );
		}
		return treePanel;
		
		/*		if (treePanel == null) {
			treePanel   = ScrollPaneSelector.createScrollPane();			treePanel.setName(uiFrame.TREE_PANEL_NAME);
			Dimension d = treePanel.getPreferredSize();
			treePanel.setSize (new Dimension(200, d.height));		}
		return treePanel;
		*/	}
	// is this really called?
	
	public void setTreeContainer(VirtualContainer c) {
		treePanel = c;
	}
				/*	Panel   drawPanel;	//Connections   drawing = new Connections();
	SLModel drawing =   new SLModel();	public SLModel getDrawing() {
		//System.out.println("drawing   is" +  drawing);
		return drawing;
		
			}	uiObjectAdapter drawingAdapter;	public uiObjectAdapter getDrawingAdapter() {
		return drawingAdapter;	}
	*/
	/*	public boolean drawPanelIsVisible() {
		if (frame.getDrawPanel()   == null) return false;
		return frame.getDrawPanel().isVisible();	}
	*/
	/*	public void createDrawPanel()   {
		if (drawPanel   != null) return;		drawPanel   = new Panel();
		drawPanel.setName(frame.DRAW_PANEL_NAME);		drawPanel.setLayout(new BorderLayout());
		try {
			bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel", "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");        			//uiGenerator.generateUI(drawPanel, new slm.SLModel());			//drawing = new Connections();			currentObjects.addElement(drawing);
			uiGenerator.generateInUIPanel(frame,  drawing, drawPanel);			//drawingAdapter = (uiObjectAdapter) currentAdapters.elementAt(currentAdapters.size() -1);
			//uiGenerator.generateUI(drawPanel, new Connections().getDrawing());			//frame.add(drawPanel, BorderLayout.SOUTH);			showDrawPanel();						//drawPanel.setBackground(Color.white);
			//int drawHeight = drawPanel.getBounds().height;
			Dimension mySize = frame.getSize();  			//System.out.println(drawHeight);
			frame.setSize(Math.max(mySize.width, SLComposer.FRAME_WIDTH), mySize.height + SLComposer.FRAME_HEIGHT);
			//frame.setSize(300, 300);			mySize = getSize();
			//System.out.println(mySize);		}   catch (Exception e) {
			System.out.println(e);		}		
	}
	*/	public void maybeHideMainPanel() {		/*
		if (!foundGenericWidget(mainScrollPane))		hideMainPanel();		*/
	}
	/*	boolean emptyMainPanel = false;
	public void emptyMainPanel() {
		if (emptyMainPanel) return;		emptyMainPanel = true;		hideMainPanel();
	}
	
	public boolean isEmptyMainPanel() {	
		if (getMainContainer() == null) return false;
		return ((Container) this.getMainContainer().getComponent(0)).countComponents() == 0;
	}
	*/	/*
	public void drawPanel() {
		if (frame.getDrawPanel()   == null) {
			frame.createDrawPanel();		}   else if (drawPanelIsVisible)			hideDrawPanel();
		else
			showDrawPanel();		
		
		frame.validate();	}
	*/
	//boolean mainScrollPaneIsVisible =   false;
	/*	public void mainPanel() {
				
				if (mainScrollPaneIsVisible)
			//mainScrollPane.setVisible(false);			hideMainPanel();
		else {			if (frame.getMainScrollPane() == null)
			frame.createMainScrollPane();
			//mainScrollPane.setVisible(true);  
						showMainPanel();		}
		frame.validate();	}
*/
	/*	public void secondaryPanel() {
		
		//if (mainScrollPane.isVisible())		if (secondaryScrollPaneIsVisible)
			//mainScrollPane.setVisible(false);			hideSecondaryScrollPane();
		else
			//mainScrollPane.setVisible(true);  			showSecondaryScrollPane();
		frame.validate();	}
*/
/*
	public void toolBar()   {
		//if (mainScrollPane.isVisible())		if ((toolBarIsVisible) && (frame.getShowMenuBar())) //if you're showing the menubar then 			//it's ok to take the toolbar off cuz they			//can show it later...			//if not then you should definetly show it.
			//mainScrollPane.setVisible(false);			hideToolBar();
				
		else
			//mainScrollPane.setVisible(true);  			showToolBar();		
		frame.validate();
		setSize();	}
	static final String MAIN_MESSAGE = "Selection in tree panel expanded here.";
	static final String SECONDARY_MESSAGE = "Selection in main panel expanded here.";
	*/
	void maybeAddSecondary() {
	if (secondaryScrollPaneIsVisible)
		addSecondaryToMain();
		//replaceRightComponent(mainSplitPane, secondaryScrollPane);
	}
	
	void addMainToTree() {
		replaceRightComponent (treeSplitPane, mainSplitPane);
		//maybeAddSecondary();
	}
	
	void addDrawToMain () {
		replaceRightComponent (mainSplitPane, frame.getDrawPanel());
	}
	
	void addMainToFrame() {
		frame.add(mainSplitPane, BorderLayout.CENTER);
		//frame.add(getMainContainer(),BorderLayout.CENTER );
	}
	public VirtualContainer getMainContainer() {
		//return mainSplitPane;
		return mainPane;
		//return topViewManager.getMainContainer();
	}
	
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
		if (treePanelIsVisible && !drawPanelIsVisible) {
			//treeSplitPane.setRightComponent(mainSplitPane);
			maybeAddSecondary();
			addMainToTree();
			
			
		} else if (treePanelIsVisible && drawPanelIsVisible) {
			//Component drawPanel = frame.getDrawPanel();
			
			//Component drawPanel = treeSplitPane.getRightComponent();
			
			//Component secondaryPanel = mainSplitPane.getRightComponent();
			//if (secondaryPanel != null)
				//mainSplitPane.remove(secondaryPanel);
			//if (drawPanel != null) { // should never be null
				//Container drawPanel = frame.getDrawPanel();
				//replaceRightComponent (treeSplitPane, mainSplitPane);
				addMainToTree();
				//replaceRightComponent (mainSplitPane, drawPanel);
				addDrawToMain();
				//treeSplitPane.remove(drawPanel);
				//mainSplitPane.setRightComponent(drawPanel);
			//}
			//treeSplitPane.setRightComponent(mainSplitPane);
		} else if (drawPanelIsVisible) { // tree panel is not visible
			VirtualContainer drawPanel = frame.getDrawPanel();
			//treeSplitPane.remove(drawPanel);
			frame.remove(drawPanel);
			addDrawToMain();
			//replaceRightComponent(mainSplitPane, drawPanel);
			//mainSplitPane.setRightComponent(drawPanel);
			addMainToFrame();
			//frame.add(mainSplitPane, BorderLayout.CENTER);
		} else { // drawPanel is not visible
			//maybeUnCenterToolBar();
			//frame.add(mainSplitPane, BorderLayout.CENTER);

			maybeUnCenterToolBar();
			maybeAddSecondary();
			addMainToFrame();
			//maybeAddSecondary();
		}
		/*
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
		 * 
		 */
		mainScrollPaneIsVisible = true;
	}
		/*	public boolean emptyFrame() {		return frame.getComponentCount() ==   0;	}	
	public boolean maybeSetEmptyFrameSize() {		if (emptyFrame())   {
			frame.setSize(frame.EMPTY_FRAME_WIDTH, frame.EMPTY_FRAME_HEIGHT);			return true;
		}   
		return false;         
	}	public boolean frameIsToolBar() {
		return frame.getComponentCount() == 1 && toolBarIsVisible; 	}	public int numChars(JToolBar toolBar)   {
		int numChars = 0;
		Component[] components = frame.getToolBar().getComponents();		for (int i = 0; i < components.length; i++) {
			numChars    += ((JButton) components[i]).getLabel().length(); 		}
		return numChars;	}
	public boolean maybeSetToolBarSize() {		if (frameIsToolBar())   {        
			frame.setSize(numChars(frame.getToolBar())*CHAR_WIDTH, TOOLBAR_HEIGHT);			return true;
		}   
		return false;         
	}	boolean sizeSet = false;
	public void setSize()   {		if (!maybeSetEmptyFrameSize()   && !maybeSetToolBarSize() && !sizeSet) {			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			sizeSet = true;		}
	}	
	Component   tempCenterPanel;
	*/
	/*
	public void hideMainPanelWithoutRearranging()   {
		if (frame.getMainScrollPane() !=   null)
			frame.remove(frame.getMainScrollPane());      
		mainScrollPaneIsVisible =   false;	}
	*/
	
	void removeMainFromTree() {
		clearRightComponent(treeSplitPane);
		//treeSplitPane.remove(mainSplitPane);
	}
	void addDrawToTree() {
		replaceRightComponent(treeSplitPane, frame.getDrawPanel());
	}
	void removeMainFromFrame() {
		frame.remove(mainSplitPane);
	}	public void hideMainPanel() {
		
		//hideMainPanelWithoutRearranging();
		if (treePanelIsVisible && !drawPanelIsVisible) {
			//treeSplitPane.remove(mainSplitPane);
			removeMainFromTree();
		} else if (treePanelIsVisible && drawPanelIsVisible) {
			//Component drawPanel = mainSplitPane.getRightComponent();
			//mainSplitPane.remove(drawPanel);
			clearRightComponent(mainSplitPane);
			addDrawToTree();
			//replaceRightComponent(treeSplitPane, frame.getDrawPanel());
			/*
			treeSplitPane.remove (mainSplitPane);
			treeSplitPane.setRightComponent (drawPanel);
			*/
		} else if (drawPanelIsVisible && !frame.manualDrawContainer()) {
			clearRightComponent(mainSplitPane);
			//Component drawPanel = mainSplitPane.getRightComponent();
			//frame.remove(mainSplitPane);
			removeMainFromFrame();
			frame.add( frame.getDrawPanel(), BorderLayout.CENTER);
			
		} else {
			removeMainFromFrame();
		}
		mainScrollPaneIsVisible =   false;
		maybeCenterToolBar();
			
		/*			
		if (!drawPanelIsVisible && treePanelIsVisible){

			//segi  //leave at EAST.			frame.remove(frame.getTreePanel());			frame.add(frame.getTreePanel(), BorderLayout.CENTER);
			//frame.resize(TOOLBAR_WIDTH, TOOLBAR_HEIGHT);		}   else if (!drawPanelIsVisible && toolBarIsVisible){
			//segi //never goes to the center anymore			//frame.remove(toolBar);			//frame.add(toolBar,   BorderLayout.CENTER);

			maybeSetToolBarSize();
			frame.setSize(TOOLBAR_WIDTH,   TOOLBAR_HEIGHT);
		}
		*/
		setSize();		//maybeSetEmptyFrameSize();
		/*		if (frame.getComponentCount() != 2) return;		tempCenterPanel =   frame.getComponent(1);
		frame.remove(tempCenterPanel);		frame.add(tempCenterPanel,   BorderLayout.CENTER);		*/    	}
	/*
	public boolean mainPanelIsVisible() {		return this.mainScrollPaneIsVisible;
	}
	*/	/*
	Panel   windowHistoryPanel;
	public void createWindowHistoryPanel() {		windowHistoryPanel = new Panel();		windowHistoryPanel.setLayout(new BorderLayout());
		try {
			//bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel", "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");      			//uiGenerator.generateUI(uigenPanel, new slm.SLModel());
			uiGenerator.generateInUIPanel(frame,  frame.getAdapterHistory(), windowHistoryPanel);
			//uiGenerator.generateUI(uigenPanel, new Connections().getDrawing());			frame.add(windowHistoryPanel, BorderLayout.EAST);
			currentObjects.addElement(frame.getAdapterHistory());			//uigenPanel.setBackground(Color.white);
			//int drawHeight = uigenPanel.getBounds().height;
			//Dimension mySize = frame.getSize();    			//System.out.println(drawHeight);
			//frame.setSize(mySize.width, mySize.height*2);		}   catch (Exception e) {
			System.out.println(e);		}
	}
	*/	//boolean drawPanelIsVisible = false;
	void clearRightComponent (VirtualSplitPane splitPane) {
		if (splitPane == null)
			return;
		VirtualComponent rightComponent = splitPane.getRightComponent();
		if (rightComponent != null)
			splitPane.remove(rightComponent);
	}
	void clearLeftComponent (VirtualSplitPane splitPane) {
		VirtualComponent leftComponent = splitPane.getLeftComponent();
		if (leftComponent != null)
			splitPane.remove(leftComponent);
	}
	void replaceRightComponent (VirtualSplitPane splitPane, VirtualComponent newVal) {
		VirtualComponent rightComponent = splitPane.getRightComponent();
		if (rightComponent == newVal) return;		
		//clearRightComponent(splitPane);
		splitPane.setRightComponent(newVal);
	}
	void replaceLeftComponent (VirtualSplitPane splitPane, VirtualComponent newVal) {
		VirtualComponent leftComponent = splitPane.getLeftComponent();
		if (leftComponent == newVal) return;		
		//clearLeftComponent(splitPane);
		splitPane.setLeftComponent(newVal);
	}
	boolean drawPanelMinimimumSizeSet = false;
	public void showDrawPanelImpl() {
		/*
		if (drawPanelIsVisible) return;
		drawPanelIsVisible = true;
		
		if (frame.manualDrawContainer()) return;
		VirtualComponent drawPanel = frame.getDrawPanel();
		if (drawPanel  == null) frame.createDrawPanel();
		*/

		VirtualComponent drawPanel = frame.getDrawPanel();
		if (!drawPanelMinimimumSizeSet) {
			drawPanel.setMinimumSize(new VirtualDimension (50, 50));
			drawPanelMinimimumSizeSet = true;
		}
		//frame.add(frame.getDrawPanel(), BorderLayout.CENTER);
		if (mainScrollPaneIsVisible) {
			addDrawToMain();
			//replaceRightComponent(mainSplitPane, drawPanel);
			/*
			if (secondaryScrollPaneIsVisible) {
				clearRightComponent(mainSplitPane);
			}	
			mainSplitPane.setRightComponent(drawPanel);
			*/
			//showMainPanelSouth();
		}  else if (treePanelIsVisible) {
			addDrawToTree();
			//replaceRightComponent (treeSplitPane, drawPanel);
			//treeSplitPane.setRightComponent(drawPanel);
		}
		else {
			maybeUnCenterToolBar();
			frame.add(drawPanel, BorderLayout.CENTER);
			
		}
//		setSize();
//		setLocation();
			

	}
	
	public void hideDrawPanelImpl() {
		//if (frame.manualDrawContainer()) return;
		/*
		if (!drawPanelIsVisible) return;
		drawPanelIsVisible = false;
		*/
		//if (frame.getDrawPanel()   == null) frame.createDrawPanel();
		//frame.add(frame.getDrawPanel(), BorderLayout.CENTER);
		if (mainScrollPaneIsVisible) {
			clearRightComponent(mainSplitPane);
			maybeAddSecondary();
			/*
			Component drawPanel = mainSplitPane.getRightComponent();
			mainSplitPane.remove(drawPanel);
			if (secondaryScrollPaneIsVisible)
				mainSplitPane.setRightComponent(secondaryScrollPane);
				*/
			
		}  else if (treePanelIsVisible) {
			clearRightComponent(treeSplitPane);
			/*
			Component drawPanel = treeSplitPane.getRightComponent();
			treeSplitPane.remove(drawPanel);
			*/
		} else {
			frame.remove(frame.getDrawPanel());
		}
		maybeCenterToolBar();
		setSize();
			
	}
	
	/*	public void hideDrawPanel() {		if (drawPanelIsVisible)			frame.remove(frame.getDrawPanel());		drawPanelIsVisible =    false;		if (mainScrollPaneIsVisible)    {
			frame.remove(frame.getMainScrollPane());
			frame.add(frame.getMainScrollPane(), BorderLayout.CENTER);
		} else if (treePanelIsVisible) {			frame.remove(frame.getTreePanel());			frame.add(frame.getTreePanel(), BorderLayout.CENTER);			//setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);
		} else if (toolBarIsVisible)    {			frame.remove(frame.getToolBar());			frame.add(frame.getToolBar(),    BorderLayout.CENTER);			//setSize(frame.TOOLBAR_WIDTH, frame.TOOLBAR_HEIGHT);
		}
		setSize();		//maybeSetEmptyFrameSize();			}
	*/	/*
	public void windowHistoryPanel()   {
		if (frame.getWindowHistoryPanel() ==   null) {
			frame.createWindowHistoryPanel();		}   else
			frame.getWindowHistoryPanel().setVisible(!frame.getWindowHistoryPanel().isVisible());
		frame.validate();	}
	*/
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
			} catch (Exception e) {				System.out.println("createTreePanel " + e);
			}		}			}	*/
	//public boolean treePanelIsVisible = false;
	/*	public void treePanel() {
		//if (mainScrollPane.isVisible())		if (treePanelIsVisible)
			//mainScrollPane.setVisible(false);			hideTreePanel();
		else
			//mainScrollPane.setVisible(true);  			showTreePanel();
		frame.validate();	}
		public void setTreePanelIsVisible (boolean newVal) {
		treePanelIsVisible = newVal;	}
	*/
	/*
	void maybeCenterToolBar() {
		if (toolBarIsVisible && !drawPanelIsVisible && !mainScrollPaneIsVisible && !treePanelIsVisible) {
			frame.remove(frame.getToolBar());
			frame.add(frame.getToolBar(), BorderLayout.CENTER);
		}
		
	}
	*/
	void removeTreeFromFrame() {
		frame.remove(treeSplitPane);
	}
	public void hideTreePanelImpl() {
		/*
		if (!treePanelIsVisible) return;
		treePanelIsVisible = false;
		*/
		removeTreeFromFrame();
		VirtualComponent rightComponent = treeSplitPane.getRightComponent();
		//frame.remove(treeSplitPane);
		if (rightComponent != null)
			frame.add(rightComponent, BorderLayout.CENTER);
		maybeCenterToolBar();	
		

		/*
			frame.remove(frame.getTreePanel());			//toolBar.setVisible(false);			treePanelIsVisible = false;
		}		if (toolBarIsVisible && !drawPanelIsVisible && !mainScrollPaneIsVisible) {
			frame.remove(frame.getToolBar());
			frame.add(frame.getToolBar(), BorderLayout.CENTER);
			//setSize(frame.TOOLBAR_WIDTH,   frame.TOOLBAR_HEIGHT);		}
		*/
		setSize();		
	}
	//boolean secondaryScrollPaneIsVisible = false;
		/*
	public void createSecondaryContainer() {
			if (sescondartScrollPane != null) return;			secondaryScrollPane = topViewManager.newSecondaryContainer();
			secondaryScrollPane.setName(this.SECONDARY_PANEL_NAME);			uiGenerator.generateInBrowsableContainer(frame, SECONDARY_MESSAGE, secondaryScrollPane);		
	}
	*/	
	void setRightComponentIfNull (VirtualSplitPane splitPane, VirtualComponent newVal) {
		if (splitPane.getRightComponent() == null)
			splitPane.setRightComponent(newVal);
	}
	void addSecondaryToMain () {
		setRightComponentIfNull(mainSplitPane, secondaryScrollPane);
	}
	boolean secondaryPaneMinimized = false;	public void showSecondaryScrollPane() {
		boolean createdSecondaryScrollPane = false;		if (secondaryScrollPane == null) {			frame.createSecondaryScrollPane();
			createdSecondaryScrollPane = true;			/*
			secondaryScrollPane = topViewManager.newSecondaryContainer();
			secondaryScrollPane.setName(this.SECONDARY_PANEL_NAME);			uiGenerator.generateInBrowsableContainer(frame, SECONDARY_MESSAGE, secondaryScrollPane);			*/
		}						//frame.createSecondaryScrollPane();
		if (secondaryScrollPaneIsVisible) return;
		//setRightComponentIfNull(mainSplitPane, secondaryScrollPane);
		if (!secondaryPaneMinimized) {
			secondaryPaneMinimized = true;
			secondaryScrollPane.setMinimumSize(new VirtualDimension (50, 50));
		}
		
		addSecondaryToMain();
		secondaryScrollPaneIsVisible = true;
		frame.getBrowser().secondaryPaneVisible();
		/*
			
				if (drawPanelIsVisible)
			frame.add(frame.getSecondaryScrollPane(), BorderLayout.EAST);		else			frame.add(frame.getSecondaryScrollPane(), BorderLayout.SOUTH);
			*/
		//secondaryScrollPaneIsVisible = true;
		//setSize();	}
	void removeRightComponent (VirtualSplitPane splitPane, VirtualComponent rightComponent) {
		if (splitPane.getRightComponent() == rightComponent)
			splitPane.remove(rightComponent);
		
	}	public void hideSecondaryScrollPane() {
		if (!secondaryScrollPaneIsVisible) return;
		removeRightComponent (mainSplitPane, frame.getSecondaryScrollPane());
		//frame.remove(frame.getSecondaryScrollPane());
		secondaryScrollPaneIsVisible = false;
		frame.getBrowser().secondaryPaneHidden();	}
	/*	public boolean secondaryScrollPaneIsVisible() {
		return secondaryScrollPaneIsVisible;	}
	*/
	void addTreeToFrame() {
		frame.add(treeSplitPane, BorderLayout.CENTER);
	}	public void showTreePanelImpl() {
		if (frame.getMainScrollPane() == null) return;
		/*
		if (frame.manualTreeContainer()) return;		
		if (frame.getTreePanel()   == null)
			frame.createTreePanel();
		if (treePanelIsVisible) return;
		treePanelIsVisible = true;
		*/
		if (mainPanelIsVisible()) {
			//frame.remove(mainSplitPane);
			removeMainFromFrame();
			addMainToTree();
			//replaceRightComponent(treeSplitPane, mainSplitPane);
		} else if (drawPanelIsVisible()) {
			frame.remove(frame.getDrawPanel());
			addDrawToTree();
			//replaceRightComponent(treeSplitPane, frame.getDrawPanel());
		}
		maybeUnCenterToolBar();
		addTreeToFrame();
		//frame.add(treeSplitPane, BorderLayout.CENTER);
		setSize();
		
		/*	
				if (!treePanelIsVisible) {
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
						//toolBar.setVisible(true);			treePanelIsVisible = true;			
		}
		*/		
	}
		/*
	
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
		uiSelectionManager.replaceSelections(selectedTreeNodes);		setTitle();
				
		//uiSelectionManager.replaceSelections((uiObjectAdapter)createdTreePath.getLastPathComponent());		
				//uiSelectionManager.select(treeNode,   true);		//uiSelectionManager.replaceSelections(treeNode);
		//treeNode.uiComponentFocusGained();		//replaceFrame();
		//System.out.println("TreeNode is   " + treeNode);
		
			}
	public void clearTreeSelection() {
		if (jTree   == null) return;		jTree.clearSelection();
	}	boolean internalTreeEvent   = false;
	public void setJTreeSelectionPaths(TreePath[]   selectedPaths) {		
		
		//jTree.clearSelection();     
		//System.out.println("Selected Paths"   + selectedPaths);
		
		//TreePath createdTreePath = jTree.getPathForLocation(0,0);		//System.out.println(createdTreePath);		//createdTreePath.pathByAddingChild(topAdapter);
		//System.out.println(selectedPaths[0]);
		//jTree.setSelectionPaths(selectedPaths);		//jTree.setSelectionPath(createdTreePath);
		internalTreeEvent   = true;
				if ((jTree !=   null) && treePanelIsVisible)			jTree.setSelectionPaths(selectedPaths);
				
	}	public void mouseClicked(MouseEvent e) {		
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
	*/			/*	public void maybeShowJTree() {		uiObjectAdapter adapter =   frame.getOriginalAdapter();
		if (//adapter.getHeight() > DEEP_ELIDE_LEVEL +   1 ||			new HasUncreatedChildrenVisitor(adapter).traverse().contains(Boolean.TRUE))
			frame.createTreePanel(adapter);	}
	*/

	/*
	Menu fileMenu;
	//
	// Create   the menuBar  ...have overloaded because may not wanna pass in a MenuSetter object if not given in	// the constructor that takes in just the object.
	//
	*/			
	//boolean toolBarIsVisible = false;
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
	*/	//JPanel toolPanel ;	/*
	public void hideToolBar()   {		if (toolBarIsVisible)   {			// why was frame commented out? A toolbar is displayed.
			frame.remove(frame.getToolBar());
			frame.remove(frame.getToolPanel());
			frame.remove( (frame.getManualToolbar()));			//toolBar.setVisible(false);			toolBarIsVisible = false;
		}		maybeSetToolBarSize();		
	}
	boolean manualToolbar = false;
	public void useManualToolbar (boolean newVal) {
		manualToolbar = newVal;
	}
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
	/*
	String toolbarOrientation () {
		if (frame.getComponentCount() != 0)  return BorderLayout.NORTH;
		//if (drawPanelIsVisible ||   mainScrollPaneIsVisible || treePanelIsVisible) return BorderLayout.NORTH;
		return BorderLayout.CENTER;
	}
	public void showToolBar() {
		//gonna have to have a toolbar panel.

		//dunno how the below works but for only the first OE frame you want to
		// make sure you've created
		//a toolbar and then somewhere the customized stuff get's put on.
		if (toolBarIsVisible)
			return;
		if (frame.getTopAdapter() != null)
			frame.hideMainIfNoProperties();
		if (manualToolbar) {
			if (frame.getManualToolbar() == null)
				frame.createManualToolbar();
			frame.add(frame.getManualToolbar(), toolbarOrientation());
		} else if ((frame.toolbarCount == 1) && (frame.isOEMainFrame)) { //if
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

			//frame.addToolBarButtons(frame.topAdapter);
			//toolPanel.add(toolGroup);

			//if (!toolBarIsVisible) {
			//if (frame.getTopAdapter() != null)
			//frame.hideMainIfNoProperties();
			//if (drawPanelIsVisible || mainScrollPaneIsVisible ||
			// treePanelIsVisible) {
			//frame.add(toolBar, BorderLayout.NORTH);

			frame.add(frame.getToolPanel(), toolbarOrientation());
			frame.getToolPanel().setVisible(true);

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
		setSize();
		frame.validate();

	}
	
	public void oldShowToolBar() {
		//gonna have to have a toolbar panel.
		
		
		//dunno how the below works but for only the first OE frame you want to
		// make sure you've created
		//a toolbar and then somewhere the customized stuff get's put on.
		//if (manualToolbar) return;
		if ((frame.toolbarCount == 1) && (frame.isOEMainFrame)) {  //if you've created one toolbar (main)
			
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
	public void addToBottomPanel(Container toadd) {  
				//toolPanel.add(toadd);   //for now let's do a simple add at end of flow				frame.add(toadd, BorderLayout.SOUTH);
		
		if (toadd != null)			System.out.println("added a toolpanel to orig");			}	public void addToMiddlePanel(Container toadd) {  
				//toolPanel.add(toadd);   //for now let's do a simple add at end of flow				frame.add(toadd, BorderLayout.CENTER);
		
		if (toadd != null)			System.out.println("added a toolpanel to mid - orig");			}	public boolean treePanelIsVisible() {		return treePanelIsVisible;
	}
	*/
			}
