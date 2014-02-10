package bus.uigen.view;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import slm.SLModel;
import shapes.ShapeModel;
import slc.SLComposer;
import bus.uigen.editors.Connections;
import bus.uigen.undo.*;
import bus.uigen.oadapters.*;
import bus.uigen.visitors.*;
import bus.uigen.introspect.*;
import bus.uigen.ars.*;
import bus.uigen.attributes.*;
import bus.uigen.controller.*;
import bus.uigen.view.AGenericWidgetShell;
import bus.uigen.view.OEFrameSelector;
import bus.uigen.widgets.ButtonSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualScrollPane;
import bus.uigen.widgets.VirtualToolBar;
import bus.uigen.*;

public class AFlexibleBrowser implements SelectionListener/* extends Frame */
{

	public static final String FORWARD_ADAPTER_NAME = "Forward";
	public static final String BACK_ADAPTER_NAME = "Back";
	public static final String REPLACE_FRAME_COMMAND = "Replace Window";
	public static final String NEW_FRAME_COMMAND = "New Editor";
	public static final String NEW_TEXT_FRAME_COMMAND = "New Text Editor";
	public static final String NEW_SCROLL_PANE_COMMAND = "New Window Right";
	public static final String NEW_SCROLL_PANE_BOTTOM_COMMAND = "New Window Bottom";
	public static final String TREE_PANEL_COMMAND = "Tree";
	public static final String DRAW_PANEL_COMMAND = "Drawing";
	public static final String MAIN_PANEL_COMMAND = "Main Panel";
	public static final String TOOLBAR_COMMAND = "Toolbar";
	public static final String WINDOW_HISTORY_PANEL_COMMAND = "Windows";

	// public static final String METHODS_MENU_NAME = "Methods";
	// public static final String METHODS_MENU_NAME = "Object";
	public static final String MAIN_PANEL_NAME = "main";
	public static final String SECONDARY_PANEL_NAME = "secondary";
	public static final String DRAW_PANEL_NAME = "draw";
	public static final String TREE_PANEL_NAME = "tree";
	public static final String TOOLBAR_PANEL_NAME = "toolbar";

	public static final int TOOLBAR_HEIGHT = 75;
	public static final int TOOLBAR_WIDTH = 350;
	public static final int CHAR_WIDTH = 9;

	// i'm using this so that the toolbar grouping stuff only happens once (the
	// main object window)
	public static int toolbarCount = 0;

	public boolean isOEMainFrame = false;
	transient Hashtable<Object, ObjectAdapter> replacedAdapters = new Hashtable();

	public AFlexibleBrowser() {
	}

	public void singleItemSelected() {
		browseObject((ObjectAdapter) SelectionManager.getCurrentSelection());
		/*
		 * browseObject( (uiObjectAdapter)
		 * uiSelectionManager.getCurrentSelection(), null, null );
		 */
		// for some reason all of this was commented
		// because enabling/disabling does not work in 1.1 or 1.4.2!
		/*
		 * uiObjectAdapter s = (uiObjectAdapter)
		 * uiSelectionManager.getCurrentSelection(); processCut(s);
		 * processLink(s); processPaste(s); processCopy(s);
		 * //copyItem.setEnabled(true); //copyItem.enable(true);
		 * this.validate();
		 */

	}

	public void noItemSelected() {
		// this was commented too (because of 1.1!)
		/*
		 * cutItem.setEnabled(false); copyItem.setEnabled(false);
		 * linkItem.setEnabled(false); pasteItem.setEnabled(false);
		 */

		/*
		 * cutItem.enable(false); copyItem.enable(false);
		 * linkItem.enable(false); pasteItem.enable(false);
		 */

	}

	public void multipleItemsSelected() {
		/*
		 * cutItem.setEnabled(false); copyItem.setEnabled(false);
		 * linkItem.setEnabled(false); pasteItem.setEnabled(false);
		 */
		// noItemSelected();
	}

	uiFrame frame;
	TopViewManager topViewManager;
	TreeView jTreeAdapter;

	public void init(uiFrame theFrame, TopViewManager theTopViewManager,
			TreeView theJTreeAdapter) {
		frame = theFrame;
		topViewManager = theTopViewManager;
		SelectionManager.addSelectionListener(this);
		jTreeAdapter = theJTreeAdapter;
	}

	VirtualContainer childPanel;

	ObjectAdapter topAdapter = null;
	ObjectAdapter origAdapter = null;

	public ObjectAdapter getTopAdapter() {
		/*
		 * 
		 * if (topAdapter != null) return topAdapter; Vector userAdapters =
		 * frame.getUserAdapters(); if (userAdapters == null ||
		 * userAdapters.size() == 0) return null; return (uiObjectAdapter)
		 * userAdapters.elementAt(0);
		 */

		return topAdapter;
	}

	ObjectAdapter menuAdapter;

	public ObjectAdapter getMenuAdapter() {
		if (menuAdapter != null)
			return menuAdapter;
		if (topAdapter != null)
			return topAdapter;
		if (frame.getTopTreeAdapter() != null)
			return frame.getTopTreeAdapter();
		if (frame.getSecondaryObjectAdapter() != null)
			return frame.getSecondaryObjectAdapter();
		if (frame.getOriginalAdapter() != null)
			return frame.getOriginalAdapter();
		return null;
	}

	AdapterHistory adapterHistory = new AdapterHistory();

	public AdapterHistory getAdapterHistory() {
		return adapterHistory;
	}
	
	

	final String PREV_ADAPTER_SEPARATOR = "<-";

	/* destructive function */
	Vector filterCurrentAdapters(Vector list) {
		for (int i = 0; i < currentAdapters.size(); i++) {
			list.removeElement(currentAdapters.elementAt(i));
		}
		return list;

	}

	public String toStringBackAdapters() {
		return toStringAdapterList(filterCurrentAdapters(adapterHistory
				.prevAdapters()), "", "", PREV_ADAPTER_SEPARATOR);

	}

	final String NEXT_ADAPTER_SEPARATOR = "->";

	public String toStringForwardAdapters() {
		return toStringAdapterList(filterCurrentAdapters(adapterHistory
				.nextAdapters()), NEXT_ADAPTER_SEPARATOR, "", "");
	}

	public String toStringCurrentAdapters() {
		return toStringAdapterList(currentAdapters, "", ", ", "");
	}

	public String toStringCurrentAndUserAdapters() {
		return toStringAdapterList(currentAndUserAdapters(), "", ", ", "");
	}

	public VirtualContainer createNewChildPanel() {
		// JPanel newChildPanel = new JPanel();
		// Panel newChildPanel = new Panel();
		VirtualContainer newChildPanel = PanelSelector.createPanel();
		frame.addKeyListener(newChildPanel);
		// newChildPanel.setMinimumSize(new Dimension(0,0));
		newChildPanel.setLayout(new BorderLayout());
		// changeChildPanel(newChildPanel);
		changeRootPanel(newChildPanel);

		return childPanel;
	}

	public void setNewChildPanel(VirtualContainer newChildPanel) {
		newChildPanel.setLayout(new BorderLayout());
		if (currentRootPane != null)
			changeChildPanel(newChildPanel);
		else {
			// frame.getContainer().removeAll();
			frame.getContainer().add(newChildPanel);
		}

		// return childPanel;
	}

	// no refernces, so remove this
	// JLayeredPane layeredPane;
	VirtualContainer mainScrollPane;
	// Container secondaryScrollPane;
	// Container secondaryPanel;
	ObjectAdapter secondaryObjectAdapter;

	// JSplitPane mainPanel;
	public void createTreePanel(Object obj) {
		jTreeAdapter.createTreePanel(obj);
	}

	boolean treePanelCreated = false;

	public void createTreePanel(ObjectAdapter adapter) {
		if (treePanelCreated)
			return;
		treePanelCreated = true;
		jTreeAdapter.createTreePanel(adapter);

	}

	public void createTreePanel() {
		jTreeAdapter.createTreePanel(frame.getOriginalAdapter());
		// return jTreeAdapter.createTreePanel();
	}

	public void createTreePanel(Object obj, VirtualContainer treePanel) {
		jTreeAdapter.createTreePanel(obj, treePanel);
	}

	public static final String SECONDARY_MESSAGE = "Selection in last panel added expanded here. Excecute command again to hide panel.";
	static final String MAIN_MESSAGE = "Selection in tree panel expanded here.";

	public void createMainScrollPane() {
		if (frame.getMainScrollPane() != null)
			return;

		{
			// uiGenerator.generateUIScrollPane(frame, "Selection in tree panel
			// expanded here", null, null);
			uiGenerator.generateInNewBrowsableContainer(frame, MAIN_MESSAGE);
			// newPanelCreated = true;
			// frame.doubleWidth();
			// return;
		}
	}

	public void createSecondaryScrollPane() {
		createSecondaryScrollPane(SECONDARY_MESSAGE);
		/*
		 * if (topViewManager.getSecondaryContainer() != null) return; Container
		 * secondaryScrollPane = topViewManager.newSecondaryContainer();
		 * //secondaryScrollPane.setName(this.SECONDARY_PANEL_NAME);
		 * uiGenerator.generateInBrowsableContainer(frame, SECONDARY_MESSAGE,
		 * secondaryScrollPane); //topViewManager.createSecondaryContainer();
		 */

	}

	/*
	 * public void createMainPanel() { if (topViewManager.getMainContainer() !=
	 * null) return; //uiGenerator.generateUIScrollPane(frame, "Selection in
	 * tree panel expanded here", null, null);
	 * uiGenerator.generateInNewBrowsableContainer(frame, MAIN_MESSAGE);
	 * //newPanelCreated = true; //frame.doubleWidth(); //return; }
	 */
	public void createSecondaryScrollPane(Object obj) {
		VirtualContainer secondaryScrollPane = topViewManager
				.getSecondaryContainer();
		if (secondaryScrollPane == null)
			secondaryScrollPane = topViewManager.newSecondaryContainer();
		// secondaryScrollPane.setName(this.SECONDARY_PANEL_NAME);
		// uiGenerator.generateInBrowsableContainer(frame, obj,
		// secondaryScrollPane);
		ObjectAdapter retVal = uiGenerator.generateInBrowsableRootPanel(
				frame, obj, null, null, secondaryScrollPane, null, null);
		// frame.addCurrentAdapter(retVal, secondaryScrollPane);
		topViewManager.showSecondaryScrollPane();
		/*
		 * //frame.setSecondaryScrollPaneIsVisible(false); //frame.validate();
		 * frame.hideSecondaryScrollPane(); //secondaryScrollPane.validate();
		 * this.getSecondaryScrollPane().validate();
		 * frame.showSecondaryScrollPane(); frame.validate(); frame.resize();
		 */

	}

	public void createSecondaryScrollPane(ObjectAdapter sourceAdapter,
			Object obj) {
		// frame.hideSecondaryScrollPane();
		createSecondaryScrollPane();
		// secondaryObjectAdapter = uiGenerator.generateInUIPanel(frame, obj,
		// null, sourceAdapter, getSecondaryScrollPane(), null);
		secondaryObjectAdapter = uiGenerator.generateInContainer(frame, obj,
				sourceAdapter, getSecondaryScrollPane());
		// frame.setSecondaryScrollPaneIsVisible(false);
		frame.hideSecondaryScrollPane();
		// secondaryScrollPane.validate();
		getSecondaryScrollPane().validate();
		// frame.validate();
		frame.showSecondaryScrollPane();
		frame.validate();
		frame.resize();
		// frame.validate();

	}

	public VirtualContainer getSecondaryScrollPane() {
		// return secondaryScrollPane;
		return topViewManager.getSecondaryContainer();
	}

	public ObjectAdapter getSecondaryObjectAdapter() {
		return secondaryObjectAdapter;
	}

	public void setSecondaryObjectAdapter(ObjectAdapter newVal) {
		// System.out.println("Adding secondary " + newVal.getID());
		secondaryObjectAdapter = newVal;
		// containerAdapterTable.put(frame.getSecondaryScrollPane(), newVal);
		/*
		 * if (frame.secondaryScrollPaneIsVisible()){
		 * frame.hideSecondaryScrollPane(); getSecondaryScrollPane().validate();
		 * frame.showSecondaryScrollPane(); frame.resize(); }
		 */
	}

	// boolean firstPanel = true;
	// boolean secondPanel = false;

	public VirtualContainer getMainScrollPane() {
		// return mainScrollPane;
		return topViewManager.getMainContainer();
	}

	public VirtualContainer createNewChildPanelInNewScrollPane() {

		return createNewChildPanelInNewScrollPane(JSplitPane.VERTICAL_SPLIT);
	}

	VirtualContainer currentRootPane;
	/*
	 * public Container createNewChildPanelInNewScrollPane(int direction) {
	 * //ScrollPane newScrollPane = new ScrollPane(); //Container newScrollPane =
	 * new ScrollPane(); Container newScrollPane =
	 * ScrollPaneSelector.createScrollPane(); //JLayeredPane newLayeredPane =
	 * new JLayeredPane(); //JPanel newChildPanel = new JPanel(); Panel
	 * newChildPanel = new Panel(); //newChildPanel.setMinimumSize(new
	 * Dimension(0,0)); newChildPanel.setLayout(new BorderLayout());
	 * newScrollPane.add(newChildPanel); //newLayeredPane.add(newScrollPane);
	 * //mainPanel.add(newScrollPane); if (firstPanel) { //mainScrollPane =
	 * newScrollPane; mainScrollPane = newScrollPane;//readding this line
	 * mainScrollPane.setName(this.MAIN_PANEL_NAME);
	 * 
	 * 
	 * //frame.add(newScrollPane);
	 * 
	 * //segi...above was orignal. changed to below to put south.
	 * //this.add(newScrollPane, BorderLayout.SOUTH);
	 * 
	 * 
	 * //mainPanel.setTopComponent(newScrollPane);
	 * //mainPanel.setTopComponent(newLayeredPane); firstPanel = false;
	 * secondPanel = true; } else { Container parent = spane.getParent();
	 * parent.remove(spane); //spane.setSize(spane.getWidth(),
	 * spane.getHeight()); JSplitPane splitPane = new JSplitPane(direction,
	 * spane, newScrollPane); //JSplitPane splitPane = new
	 * JSplitPane(direction);
	 * 
	 * 
	 * //System.out.println("size" + size.width + " " + size.height);
	 * //splitPane.setDividerLocation(0.5); //splitPane.setLeftComponent(spane);
	 * //splitPane.setRightComponent(newScrollPane); if (secondPanel) {
	 * parent.add(splitPane);
	 * 
	 * //readding this if (parent instanceof Frame) { mainScrollPane =
	 * splitPane; mainScrollPane.setName(this.MAIN_PANEL_NAME); }
	 *  } else ((JSplitPane) parent).setRightComponent(splitPane);
	 * 
	 * 
	 * //spane.doLayout(); Dimension size = frame.getSize(); if (direction ==
	 * JSplitPane.HORIZONTAL_SPLIT) frame.setSize(size.width*2, size.height);
	 * else frame.setSize(size.width, size.height*2);
	 * //newScrollPane.doLayout();
	 * 
	 * //splitPane.setLeftComponent(newScrollPane);
	 * //splitPane.setLeftComponent(newLayeredPane);
	 * //mainPanel.setBottomComponent(splitPane); } childPanel = newChildPanel;
	 * spane = newScrollPane; //layeredPane = newLayeredPane; return
	 * newChildPanel; }
	 */
	Vector parentedContainers = new Vector();

	public void addParentedContainer(VirtualContainer parent) {
		if (parentedContainers.contains(parent))
			return;
		parentedContainers.addElement(parent);
	}

	public boolean isParentedContainer(VirtualContainer parent) {
		return parentedContainers.contains(parent);
	}

	// Hashtable<VirtualContainer, VirtualScrollPane> containerScrollPaneMapping
	// = new Hashtable();
	Hashtable<VirtualContainer, VirtualContainer> containerScrollPaneMapping = new Hashtable();

	public VirtualContainer createNewChildPanelInNewScrollPane(int direction) {
		// ScrollPane newScrollPane = new ScrollPane();
		// Container newScrollPane = new ScrollPane();
		// Container newScrollPane = ScrollPaneSelector.createScrollPane();

		VirtualContainer newChildPanel = PanelSelector.createPanel();
		newChildPanel.setName("ChildPanel(AFlexibleBrwoser.createNewChildPanelInNewScrollPane)");
		// VirtualScrollPane newScrollPane =
		// topViewManager.newContainer(direction, newChildPanel);
		frame.addKeyListener(newChildPanel);
		VirtualContainer newScrollPane = topViewManager.newContainer(direction,
				newChildPanel);
//		newScrollPane.setBackground(AttributeNames.getDefaultOrSystemDefault(AttributeNames.CONTAINER_BACKGROUND));

		newScrollPane.setName("Child ScrollPane(AFlexibleBrowser.createNewChildPanelInNewScrollPane)");
		// JLayeredPane newLayeredPane = new JLayeredPane();
		// JPanel newChildPanel = new JPanel();
		// Panel newChildPanel = new Panel();
		// VirtualContainer newChildPanel = PanelSelector.createPanel();
		// newChildPanel.setMinimumSize(new Dimension(0,0));
		newChildPanel.setLayout(new BorderLayout());
		// newScrollPane.add(newChildPanel);
//		newChildPanel.setBackground(newScrollPane.getBackground());
		containerScrollPaneMapping.put(newChildPanel, newScrollPane);
		addParentedContainer(newScrollPane);
		// newLayeredPane.add(newScrollPane);
		// mainPanel.add(newScrollPane);
		/*
		 * if (firstPanel) { //mainScrollPane = newScrollPane; mainScrollPane =
		 * newScrollPane;//readding this line
		 * mainScrollPane.setName(this.MAIN_PANEL_NAME);
		 * 
		 * 
		 * //frame.add(newScrollPane);
		 * 
		 * //segi...above was orignal. changed to below to put south.
		 * //this.add(newScrollPane, BorderLayout.SOUTH);
		 * 
		 * 
		 * //mainPanel.setTopComponent(newScrollPane);
		 * //mainPanel.setTopComponent(newLayeredPane); firstPanel = false;
		 * secondPanel = true; } else { Container parent = spane.getParent();
		 * parent.remove(spane); //spane.setSize(spane.getWidth(),
		 * spane.getHeight()); JSplitPane splitPane = new JSplitPane(direction,
		 * spane, newScrollPane); //JSplitPane splitPane = new
		 * JSplitPane(direction);
		 * 
		 * 
		 * //System.out.println("size" + size.width + " " + size.height);
		 * //splitPane.setDividerLocation(0.5);
		 * //splitPane.setLeftComponent(spane);
		 * //splitPane.setRightComponent(newScrollPane); if (secondPanel) {
		 * parent.add(splitPane);
		 * 
		 * //readding this if (parent instanceof Frame) { mainScrollPane =
		 * splitPane; mainScrollPane.setName(this.MAIN_PANEL_NAME); }
		 *  } else ((JSplitPane) parent).setRightComponent(splitPane);
		 * 
		 * 
		 * //spane.doLayout(); Dimension size = frame.getSize(); if (direction ==
		 * JSplitPane.HORIZONTAL_SPLIT) frame.setSize(size.width*2,
		 * size.height); else frame.setSize(size.width, size.height*2);
		 * //newScrollPane.doLayout();
		 * 
		 * //splitPane.setLeftComponent(newScrollPane);
		 * //splitPane.setLeftComponent(newLayeredPane);
		 * //mainPanel.setBottomComponent(splitPane); }
		 */
		childPanel = newChildPanel;
		currentRootPane = newScrollPane;
		// layeredPane = newLayeredPane;

		return newChildPanel;
		// return newScrollPane;
	}

	/*
	 * public Container createNewChildPanelInNewScrollPaneOld() { ScrollPane
	 * newScrollPane = new ScrollPane(); JLayeredPane layeredPane = new
	 * JLayeredPane(); JPanel newChildPanel = new JPanel();
	 * newChildPanel.setLayout(new BorderLayout());
	 * newScrollPane.add(newChildPanel); //mainPanel.add(newScrollPane); if
	 * (firstPanel) { mainPanel.setTopComponent(newScrollPane); firstPanel =
	 * false; } else { JSplitPane splitPane = new JSplitPane();
	 * splitPane.setLeftComponent(newScrollPane);
	 * mainPanel.setBottomComponent(splitPane); } childPanel = newChildPanel;
	 * spane = newScrollPane; return newChildPanel; }
	 */
	// removing this as there are no references
	/*
	 * public void changeChildPanelLayered(VirtualContainer newChildPanel) {
	 * //System.out.e(childPanel); //newChildPanel.setVisible(false);
	 * //spane.remove(childPanel); //childPanel.setVisible(false); //childPanel =
	 * newChildPanel; //spane.add(newChildPanel);
	 * //newChildPanel.setVisible(true); layeredPane.add(newChildPanel);
	 * //System.out.println(spane.getComponentCount());
	 * //mainPanel.add(childPanel); //this.doLayout(); //this.validate();
	 * //this.repaint();
	 *  }
	 */
	public void changeChildPanel(VirtualContainer newChildPanel) {
		// if (spane == null) return;
		// System.out.println("changing child panel");
		// System.out.println(newChildPanel);
		// System.out.println(spane);
		// System.out.e(childPanel);
		newChildPanel.setVisible(false);
		// System.out.println("new childPanel " + newChildPanel);
		// System.out.println("childPanel" + newChildPanel + childPanel);
		// System.out.println(spane);
		// spane.remove(childPanel);
		// System.out.println("removing" + spane.getComponent(0));
		if (currentRootPane.getComponentCount() > 0)
			currentRootPane.remove(currentRootPane.getComponent(0));
		// System.out.println("removed childPanel " + childPanel);
		// childPanel.setVisible(false);
		childPanel = newChildPanel;
		// if (spane != childPanel && newChildPanel.getParent() != null)
		if (currentRootPane != childPanel && newChildPanel.getParent() == null)
			currentRootPane.add(newChildPanel);
		newChildPanel.setBackground(currentRootPane.getBackground());
		// mainScrollPane = spane;
		// mainScrollPane.setName(this.MAIN_PANEL_NAME);
		// System.out.println("added to spane" + newChildPanel);
		newChildPanel.setVisible(true);
		// System.out.println("made visible");
		// System.out.println(spane.getComponentCount());
		// mainPanel.add(childPanel);
		// this.doLayout();
		// this.validate();
		// this.repaint();
		/*
		 * System.out.println(childPanel);
		 * System.out.println(childPanel.getComponentCount()); for (int i = 0; i <
		 * childPanel.getComponentCount(); i++)
		 * System.out.println(childPanel.getComponent(i));
		 */
	}

	public void changeRootPanel(VirtualContainer newChildPanel) {

		newChildPanel.setVisible(false);

		VirtualContainer commonScrollPane = containerScrollPaneMapping
				.get(currentRootPane);
		commonScrollPane.removeAll();
		/*
		 * if (currentRootPane.getComponentCount() > 0)
		 * currentRootPane.remove(currentRootPane.getComponent(0));
		 */

		childPanel = newChildPanel;
		// if (currentRootPane != childPanel && newChildPanel.getParent() ==
		// null)
		// if (currentRootPane != childPanel )
		// currentRootPane.add(newChildPanel);
		commonScrollPane.add(childPanel);

		newChildPanel.setBackground(currentRootPane.getBackground());

		newChildPanel.setVisible(true);

	}

	public void backAdapter() {
		changeToExistingAdapter(skipPrevCurrentAdapters(adapterHistory
				.toPrevAdapter()));
	}

	public void forwardAdapter() {
		changeToExistingAdapter(skipNextCurrentAdapters(adapterHistory
				.toNextAdapter()));
	}

	public void changeToExistingAdapter(ObjectAdapter adapter) {
		System.out.println("starting change to existing" + adapter);
		/*
		 * System.out.println("change to exisiting"); debugScroll(adapter);
		 */

		changeAdapter(adapter);
		if (adapter == null)
			return;
		// System.out.println(adapter.getUIComponent());
		restoreAdapter(adapter);
		// changeChildPanel( (VirtualContainer)
		// adapter.getParentAdapter().getUIComponent());
		changeRootPanel((VirtualContainer) adapter.getParentAdapter()
				.getUIComponent());
		// System.out.println("returned from child panel");
		// changeAdapter(adapter);
		// System.out.println("returned from change Adapter");
		// adapter.refresh();
		// deepElide(adapter);
		frame.validate();
		// System.out.println("finished validation");
		// this.repaint();
	}

	public ObjectAdapter skipPrevCurrentAdapters(ObjectAdapter adapter) {
		Object drawing = frame.getDrawing();
		if (adapter != null
				&& (currentAdapters.contains(adapter)
						|| adapter.getRealObject() == drawing || adapter
						.getRealObject() == adapterHistory)) {
			// System.out.println("prev curent adapter " + adapter);
			// System.out.println("current" +
			// currentAdapters.contains(adapter));
			// System.out.println("drawing" + (adapter.getRealObject() ==
			// drawing));
			// System.out.println("history" + (adapter.getRealObject() ==
			// adapterHistory));

			/*
			 * childPanel = (Container)
			 * adapter.getParentAdapter().getUIComponent().getParent();
			 * //childPanel = (Container)
			 * adapter.getParentAdapter().getUIComponent();
			 * //System.out.println(childPanel.getParent()); spane =
			 * (ScrollPane) childPanel.getParent();
			 */
			// this.resetScrollPane(adapter);
			// restoreAdapter(adapter);
			// topAdapter = adapter;
			return skipPrevCurrentAdapters(adapterHistory.toPrevAdapter());
		} else {
			System.out.println("prev adapter " + adapter);
			return adapter;
		}
	}

	public ObjectAdapter skipNextCurrentAdapters(ObjectAdapter adapter) {
		Object drawing = frame.getDrawing();
		if (adapter != null
				&& (currentAdapters.contains(adapter)
						|| adapter.getRealObject() == drawing || adapter
						.getRealObject() == adapterHistory)) {
			// System.out.println("next curent adapter " + adapter);
			/*
			 * childPanel = (Container)
			 * adapter.getParentAdapter().getUIComponent().getParent();
			 * //System.out.println(childPanel.getParent()); spane =
			 * (ScrollPane) childPanel.getParent();
			 */
			// resetScrollPane(adapter);
			// restoreAdapter(adapter);
			// topAdapter = adapter;
			return skipNextCurrentAdapters(adapterHistory.toNextAdapter());
		} else {

			// System.out.println("next adapter " + adapter);
			return adapter;
		}
	}

	void swapCurrentAdapters(ObjectAdapter cur, ObjectAdapter next) {
		if (cur == null)
			return;
		int index = currentAdapters.indexOf(cur);
		if (index < 0)
			System.out.println("current adapter not there");
		else {
			// System.out.println("cur" + cur);
			// System.out.println("next" + cur);
			// System.out.println("index" + index);
			frame.getToolbarManager().removeToolBarButtons(cur);
			frame.getToolbarManager().addToolBarButtons(next);
			System.out.println(currentAdapters);
			currentAdapters.removeElement(cur);
			currentAdapters.insertElementAt(next, index);
			System.out.println("after swap");
			// System.out.println(currentAdapters);
		}

	}

	Vector<ForwardBackwardListener> forwardBackwardListeners = new Vector();

	public void addForwardBackwardListener(ForwardBackwardListener l) {
		if (forwardBackwardListeners.contains(l))
			return;
		forwardBackwardListeners.add(l);

	}

	public void notifyForwardBackwardListeners() {
		for (int i = 0; i < forwardBackwardListeners.size(); i++) {
			ForwardBackwardListener listener = forwardBackwardListeners
					.elementAt(i);
			listener.forwardEnabled(!(adapterHistory.nextAdapter() == null));
			listener.backwardEnabled(!(adapterHistory.prevAdapter() == null));
			if (frame.getRootMenuGroup() != null)
				frame.getRootMenuGroup().checkPreInMenuTree();
		}
	}

	Vector<SaveAsListener> saveAsListeners = new Vector();

	public void addSaveAsListener(SaveAsListener l) {
		if (saveAsListeners.contains(l))
			return;
		saveAsListeners.add(l);

	}

	public void notifySaveAsListeners(boolean newVal) {
		for (int i = 0; i < saveAsListeners.size(); i++) {
			SaveAsListener listener = saveAsListeners.elementAt(i);
			listener.saveAsEnabled(newVal);
		}
	}

	public void changeAdapter(ObjectAdapter adapter) {

		frame.setForwardEnabled(!(adapterHistory.nextAdapter() == null));
		frame.setBackEnabled(!(adapterHistory.prevAdapter() == null));

		notifyForwardBackwardListeners();
		if (adapter == null)
			return;
		Object obj = adapter.getRealObject();
		/*
		 * //if ((topAdapter == null) && (obj != null)) { if (obj != null) {
		 * String title =
		 * (String)adapter.getAttributeValue(AttributeNames.TITLE); if (title ==
		 * null) { // Check the class attributes try { title = (String)
		 * AttributeManager.getEnvironment().getAttribute(obj.getClass().getName(),
		 * AttributeNames.TITLE).getValue(); } catch (Exception e) { //title =
		 * ClassDescriptor.getMethodsMenuName(obj.getClass()); //title =
		 * ClassDescriptor.getMethodsMenuName(adapter.getPropertyClass());
		 * //title =
		 * ClassDescriptor.toShortName(adapter.getPropertyClass().getName()); if
		 * (obj != adapter.getViewObject()) title =
		 * ClassDescriptor.toShortName(obj.getClass().getName()); else title =
		 * ClassDescriptor.toShortName(adapter.getPropertyClass().getName()); } }
		 * //adapter.setFrameTitle(title);
		 *  }
		 */

		if (getOriginalAdapter() == null || origAdapter.getRealObject() == null) {
			// origAdapter = adapter;
			setOriginalAdapter(adapter);
			// System.out.println("original adapter" + origAdapter);
		}

		swapCurrentAdapters(topAdapter, adapter);
		topAdapter = adapter;
		// this.setTitle(toStringBackAdapters() + " " + adapter.getFrameTitle()
		// + " " + toStringForwardAdapters());
		// this.setTitle(toStringBackAdapters() + " " +
		// toStringCurrentAdapters() + " " + toStringForwardAdapters());
		frame.setTitle();
		// Object obj = adapter.getRealObject();
		if (obj == null)
			// frame.setSaveAsMenuItemEnabled(true);
			notifySaveAsListeners(true);
		else
			// setSaveMenuItemEnabled(isSavable(topAdapter));
			// frame.setSaveAsMenuItemEnabled(frame.isSavable(getOriginalAdapter()));
			notifySaveAsListeners(frame.isSavable(getOriginalAdapter()));
		// System.out.println(currentAdapters);
		// System.out.println(obj);

		/*
		 * if (!isSavable(topAdapter)) //if (!(obj instanceof
		 * java.io.Serializable)) this.setSaveMenuItemEnabled(false); if
		 * (adapterHistory.nextAdapter() == null) {
		 * forwardItem.setEnabled(false); //setButtonEnabled(forwardButton,
		 * false);
		 *  } else { forwardItem.setEnabled(true);
		 * //setButtonEnabled(forwardButton, true); } if
		 * (adapterHistory.prevAdapter() == null) backItem.setEnabled(false);
		 * else backItem.setEnabled(true);
		 */
		// Set the frame title
		/*
		 * System.out.println("change:"); adapter = adapter.getParentAdapter();
		 * System.out.println("adaptor" + adapter); Component component =
		 * adapter.getUIComponent(); //System.out.println("component" +
		 * component); //System.out.println("parent adaptor" +
		 * adapter.getParentAdapter()); System.out.println("parent component" +
		 * component.getParent());
		 */
	}

	public void debugScroll(ObjectAdapter adapter) {
		adapter = adapter.getParentAdapter();
		System.out.println("adaptor" + adapter);
		VirtualComponent component = adapter.getUIComponent();
		// System.out.println("component" + component);
		// System.out.println("parent adaptor" + adapter.getParentAdapter());
		System.out.println("parent component" + component.getParent());

	}

	public boolean noPropertiesInCurrentAdapters() {
		boolean retVal = true;
		for (int i = 0; i < currentAdapters.size(); i++) {
			retVal &= ((ObjectAdapter) currentAdapters.elementAt(i))
					.hasNoProperties();
		}
		return retVal;
	}

	public boolean noPropertiesInAdapterHistory() {
		boolean retVal = true;
		// not sure if topAdapter is aleady in history
		if (topAdapter != null)
			retVal = topAdapter.hasNoProperties();
		for (Enumeration adapters = adapterHistory.elements(); adapters
				.hasMoreElements();) {
			// uiObjectAdapter nextAdapter = (uiObjectAdapter
			// )adapters.nextElement();
			// if (nextAdapter != null)
			retVal &= ((ObjectAdapter) adapters.nextElement())
					.hasNoProperties();
			// retVal &= nextAdapter.hasNoProperties();
		}
		return retVal;
	}

	public void setAdapter(ObjectAdapter adapter, VirtualContainer theRootPane) {
		if (topAdapter == null) {
			this.addCurrentAdapter(adapter, theRootPane);
		} else
			adapterHistory.addAdapter(adapter);
		if (theRootPane == null)
			return;

		adapterRootPaneTable.put(adapter, theRootPane);
		changeAdapter(adapter);
		// this.setTitle(toStringBackAdapters() + " " +
		// toStringCurrentAdapters() + " " + toStringForwardAdapters());
		/*
		 * System.out.println ("after set:"); debugScroll(adapter);
		 */
		/*
		 * adapter = adapter.getParentAdapter(); System.out.println("adaptor" +
		 * adapter); Component component = adapter.getUIComponent();
		 * 
		 * //System.out.println("component" + component);
		 * //System.out.println("parent adaptor" + adapter.getParentAdapter());
		 * System.out.println("parent component" + component.getParent());
		 * 
		 * //topAdapter = adapter;
		 */
	}

	public void setAdapter(ObjectAdapter adapter) {
		setAdapter(adapter, currentRootPane);
	}

	public ObjectAdapter getAdapter() {
		// return topAdapter;

		if (topAdapter != null)
			return topAdapter;
		Vector userAdapters = frame.getUserAdapters();
		// if (userAdapters == null || userAdapters.size() == 0) return null;
		if (userAdapters != null && userAdapters.size() != 0)
			return (ObjectAdapter) userAdapters.elementAt(0);
		if (jTreeAdapter != null && jTreeAdapter.getTopTreeAdapter() != null)
			return jTreeAdapter.getTopTreeAdapter();
		if (origAdapter != null)
			return origAdapter;
		return null;

	}

	public ObjectAdapter getOriginalAdapter() {
		return origAdapter;
	}

	// SLComposer drawPanel;
	/*
	 * public void createDrawPanel() { try { drawPanel = new SLComposer(this);
	 * this.add(drawPanel, BorderLayout.SOUTH); } catch (Exception e) {
	 * System.out.println(e); } }
	 */

	public void setOriginalAdapter(ObjectAdapter newVal) {

		origAdapter = newVal;
		frame.maybeShowJTree();
		// createTreePanel(newVal);

	}

	public ObjectAdapter getDefaultAdapter() {
		// System.out.println("getting default dapter");
		// System.out.println (currentAdapters);
		if (currentAdapters.size() != 0) {
			// System.out.println(currentAdapters.elementAt(0));
			return (ObjectAdapter) currentAdapters.elementAt(0);
		} else
			return null;

	}

	/*
	 * public void print(Vector v) { System.out.println("Vector Start"); for
	 * (int i = 0; i<v.size(); i++) System.out.println(v.elementAt(i));
	 * System.out.println("Vector End"); }
	 */
	Hashtable adapterRootPaneTable = new Hashtable();

	ObjectAdapter adapterFromScrollPane(ScrollPane spane) {
		for (Enumeration keys = adapterRootPaneTable.keys(); keys
				.hasMoreElements();) {
			ObjectAdapter adapter = (ObjectAdapter) keys.nextElement();
			if (adapterRootPaneTable.get(adapter) == spane)
				return adapter;
		}
		return null;
	}

	public void resetScrollPaneOld(ObjectAdapter a) {
		// System.out.println(a);
		ObjectAdapter parent = a.getParentAdapter();
		VirtualComponent c = parent.getUIComponent();
		// System.out.println("component" + c);
		while ((c.getParent() != null)
				&& !(c.getParent() instanceof ScrollPane))
			c = c.getParent();
		childPanel = (VirtualContainer) c;
		// System.out.println("child panel" + childPanel);
		// System.out.println("parent " + c.getParent());
		if (c.getParent() != null)
			// spane = (ScrollPane) c.getParent();
			currentRootPane = (VirtualContainer) c.getParent();
	}

	public void resetScrollPane(ObjectAdapter a) {
		// System.out.println(a);
		childPanel = (VirtualContainer) a.getRootAdapter().getUIComponent();
		/*
		 * uiObjectAdapter parent = a.getParentAdapter(); Component c =
		 * parent.getUIComponent(); //System.out.println("component" + c);
		 * while((c.getParent() != null) && !(c.getParent() instanceof
		 * ScrollPane) ) c = c.getParent();
		 */
		// childPanel =(Container) c;
		// System.out.println("child panel" + childPanel);
		// System.out.println("parent " + c.getParent());
		if (childPanel.getParent() != null)
			currentRootPane = (VirtualContainer) childPanel.getParent();

	}

	public VirtualContainer restoreAdapter(ObjectAdapter adapter) {

		// uiObjectAdapter selectionAdapter =
		// (uiObjectAdapter)uiSelectionManager.getCurrentSelection();
		// System.out.println("calling select with nll");
		SelectionManager.select(null);
		// Container origScrollPane = (ScrollPane)
		// adapterScrollPaneTable.get(adapter);
		VirtualContainer origRootPane = (VirtualContainer) adapterRootPaneTable
				.get(adapter);
		// System.out.println("returning" + origScrollPane + "for" + adapter);
		// Container c = (Container)
		// adapter.getParentAdapter().getUIComponent();
		currentRootPane = origRootPane;
		return currentRootPane;
		// changeChildPanel(c);
		/*
		 * System.out.println("restore:"); debugScroll(adapter);
		 */
		/*
		 * adapter = adapter.getParentAdapter(); System.out.println("adaptor" +
		 * adapter); Component component = adapter.getUIComponent();
		 * //System.out.println("component" + component);
		 * //System.out.println("parent adaptor" + adapter.getParentAdapter());
		 * System.out.println("parent component" + component.getParent());
		 */

	}

	/*
	 * public void replaceFrame (uiObjectAdapter adapter) {
	 * //System.out.println("replace Frame called with " + adapter); if (adapter !=
	 * null) { toolBar.removeAll();
	 * addUIFrameToolBarButton(FORWARD_ADAPTER_NAME, null);
	 * addUIFrameToolBarButton(BACK_ADAPTER_NAME, null); uiObjectAdapter
	 * selectionTop = adapter.getTopAdapter(adapter);
	 * //System.out.println("selection Top" + selectionTop); uiObjectAdapter
	 * replaced; int index = currentAdapters.indexOf(selectionTop);
	 * //System.out.println("replacement index " + index); if (index + 1 <
	 * currentAdapters.size()) { replaced =(uiObjectAdapter)
	 * currentAdapters.elementAt(index + 1);
	 * //currentAdapters.insertElementAt(adapter, index + 1); } else { replaced =
	 * selectionTop; //currentAdapters.addElement(adapter); }
	 * 
	 * //topAdapter = replaced; //currentAdapters.removeElement(replaced);
	 * 
	 * //System.out.println("childPanel" + childPanel.getParent()); //childPanel =
	 * (Container) replaced.getParentAdapter().getUIComponent().getParent();
	 * //System.out.println("replavced: " + replaced);
	 * //System.out.println("replaced parent" + replaced.getParentAdapter());
	 * 
	 * //resetScrollPane(replaced); restoreAdapter(replaced);
	 * 
	 * //System.out.println("putting" + adapter + spane);
	 * //adapterScrollPaneTable.put(adapter.getTopAdapter(adapter), spane);
	 * topAdapter = replaced; this.removeToolBarButtons(replaced); uiFrame
	 * editor = uiGenerator.generateUIFrame(this, adapter.getObject(), null,
	 * adapter); showToolBar(); editor.setVisible(true);
	 *  } }
	 */
	/*
	 * public void replaceAdapterFrame (uiObjectAdapter adapter, Object obj) {
	 * replaceFrame(adapter, obj, title); }
	 */
	public void browseObjectOld(ObjectAdapter adapter, Object obj,
			String title) {
		// System.out.println("replace Frame called with " + adapter);

		// toolBar.removeAll();
		// toolBarButtons.removeAllElements();
		// methodActions.removeAllElements();
		ObjectAdapter selectionTop;
		if (adapter == null)
			return;
		ObjectAdapter expandedAdapter;
		// if (adapter != null)
		expandedAdapter = adapter.getExpandedAdapter();
		// obj = adapter.getObject();
		obj = expandedAdapter.getObject();
		if (obj == null)
			return;
		// selectionTop = topAdapter;
		// else
		selectionTop = adapter.getTopAdapter(adapter);
		// System.out.println("selection Top" + selectionTop);
		ObjectAdapter replaced = null;
		// Container replacedPanel = null;
		// int index;
		if (selectionTop == frame.getTopTreeAdapter()) {
			if (frame.drawPanelIsVisible()
					&& frame.secondaryScrollPaneIsVisible()) {
				createSecondaryScrollPane(expandedAdapter, obj);
				// createSecondaryScrollPane(obj);
				return;

			} else if (frame.mainPanelIsVisible())
				replaced = (ObjectAdapter) currentAdapters.elementAt(0);
			else
				return;

		} else {
			int index = currentAdapters.indexOf(selectionTop);
			if (index < 0)
				return;
			if (index == currentAdapters.size() - 1) {
				if (frame.secondaryScrollPaneIsVisible()) {
					createSecondaryScrollPane(expandedAdapter, obj);
					// createSecondaryScrollPane(obj);
				}
				return;
			}
			// if (index < 0 || index + 1 >= currentAdapters.size()) return;
			// System.out.println("replacement index " + index);
			// if (index + 1 < currentAdapters.size()) { // next adpter is also
			// visible, replace its frame
			replaced = (ObjectAdapter) currentAdapters.elementAt(index + 1);
		}
		if (replaced == null)
			return;
		// currentAdapters.insertElementAt(adapter, index + 1);
		// }
		// if (replaced == null) return;
		/*
		 * System.out.println("replacing Frame"); debugScroll(replaced);
		 */
		// topAdapter = replaced;
		// currentAdapters.removeElement(replaced);
		// System.out.println("childPanel" + childPanel.getParent());
		// childPanel = (Container)
		// replaced.getParentAdapter().getUIComponent().getParent();
		// System.out.println("replavced: " + replaced);
		// System.out.println("replaced parent" + replaced.getParentAdapter());
		/*
		 * childPanel = (Container)
		 * replaced.getParentAdapter().getUIComponent();
		 * System.out.println("childPanel" + childPanel);
		 * System.out.println("spane" + childPanel.getParent()); ScrollPane
		 * replacedScrollPane = (ScrollPane) childPanel.getParent();
		 * //topAdapter = replaced; if (replacedScrollPane != null) spane =
		 * replacedScrollPane;
		 */
		// resetScrollPane(replaced);
		VirtualContainer replacedPanel = (VirtualContainer) restoreAdapter(
				replaced).getComponent(0);
		replacedPanel.removeAll();

		// System.out.println("putting" + adapter + spane);
		// adapterScrollPaneTable.put(adapter.getTopAdapter(adapter), spane);
		// topAdapter = replaced;
		// this.removeToolBarButtons(replaced);
		// uiFrame editor =
		/*
		 * uiObjectAdapter expandedAdapter; //if (adapter != null)
		 * expandedAdapter = adapter.getExpandedAdapter(); //obj =
		 * adapter.getObject(); obj = expandedAdapter.getObject(); if (obj ==
		 * null) return;
		 */
		// uiObjectAdapter newAdapter = uiGenerator.generateInUIPanel(frame,
		// obj, null, expandedAdapter, replacedPanel, title);
		ObjectAdapter newAdapter = uiGenerator.generateInContainer(frame,
				obj, expandedAdapter, replacedPanel);
		replaceAdapterInScrolledPanel(replaced, newAdapter.getTopAdapter());

		replacedPanel.validate();
		// frame.showToolBar();
		// editor.setVisible(true);
		/*
		 * System.out.println("after generation"); debugScroll(replaced);
		 */
	}

	public void replaceFrame(ObjectAdapter adapter, Object obj, String title) {
		// System.out.println("replace Frame called with " + adapter);
		/*
		 * if (adapter == null) return;
		 */
		/*
		 * if (adapter != null && !adapter.getOpenOnDoubleClick()) return;
		 */
		/*
		 * if (adapter != null) obj = adapter.getObject(); if (obj == null)
		 * return;
		 */

		// toolBar.removeAll();
		// toolBarButtons.removeAllElements();
		// methodActions.removeAllElements();
		// frame.addUIFrameToolBarButton(FORWARD_ADAPTER_NAME, null);
		// frame.addUIFrameToolBarButton(BACK_ADAPTER_NAME, null);
		ObjectAdapter selectionTop;
		if (adapter == null)
			selectionTop = topAdapter;
		else
			selectionTop = adapter.getTopAdapter(adapter);
		// System.out.println("selection Top" + selectionTop);
		// we change to current adapter through single click so commenting this
		// out
		/*
		 * uiObjectAdapter replaced; int index =
		 * currentAdapters.indexOf(selectionTop);
		 * //System.out.println("replacement index " + index); if (index + 1 <
		 * currentAdapters.size()) { // next adpter is also visible, replace its
		 * frame replaced =(uiObjectAdapter) currentAdapters.elementAt(index +
		 * 1); //currentAdapters.insertElementAt(adapter, index + 1); } else {
		 * replaced = selectionTop; //currentAdapters.addElement(adapter); }
		 */
		ObjectAdapter replaced = selectionTop; // simpler semantics
		replaceFrame(adapter, obj, replaced);
		/*
		 * //resetScrollPane(replaced);
		 * frame.addUIFrameToolBarButton(FORWARD_ADAPTER_NAME, null);
		 * frame.addUIFrameToolBarButton(BACK_ADAPTER_NAME, null);
		 * VirtualContainer replacedRootPane = restoreAdapter(replaced); if
		 * (replacedRootPane == null) return; VirtualContainer newRootPanel =
		 * this.createNewChildPanel();
		 * 
		 * //System.out.println("putting" + adapter + spane);
		 * //adapterScrollPaneTable.put(adapter.getTopAdapter(adapter), spane);
		 * topAdapter = replaced; //this.removeToolBarButtons(replaced);
		 * //uiFrame editor = //uiGenerator.generateUIFrame(frame, obj, null,
		 * adapter, title); //uiObjectAdapter newAdapter =
		 * uiGenerator.generateInContainer(frame, obj, adapter, newRootPanel);
		 * //uiObjectAdapter newAdapter = uiGenerator.generateInContainer(frame,
		 * obj, adapter, newRootPanel, false); uiObjectAdapter newAdapter =
		 * uiGenerator.generateInContainer(frame, obj, adapter, newRootPanel,
		 * true); setAdapter(newAdapter); //this.setNewChildPanel(newRootPanel);
		 * frame.showToolBar(); //editor.setVisible(true);
		 * frame.setVisible(true);
		 */
		/*
		 * System.out.println("after generation"); debugScroll(replaced);
		 */
	}

	public void replaceFrame(ObjectAdapter adapter, Object obj,
			ObjectAdapter replaced) {
		// System.out.println("replace Frame called with " + adapter);
		/*
		 * if (adapter == null) return;
		 */
		/*
		 * if (adapter != null && !adapter.getOpenOnDoubleClick()) return;
		 */
		// Object obj = null;
		if (adapter != null && obj == null)
			obj = adapter.getObject();
		if (obj == null)
			return;

		// toolBar.removeAll();
		// toolBarButtons.removeAllElements();
		// methodActions.removeAllElements();

		// frame.addUIFrameToolBarButton(FORWARD_ADAPTER_NAME, null);
		// frame.addUIFrameToolBarButton(BACK_ADAPTER_NAME, null);
		/*
		 * uiObjectAdapter selectionTop; if (adapter == null) selectionTop =
		 * topAdapter; else selectionTop = adapter.getTopAdapter(adapter);
		 */
		// System.out.println("selection Top" + selectionTop);
		// we change to current adapter through single click so commenting this
		// out
		/*
		 * uiObjectAdapter replaced; int index =
		 * currentAdapters.indexOf(selectionTop);
		 * //System.out.println("replacement index " + index); if (index + 1 <
		 * currentAdapters.size()) { // next adpter is also visible, replace its
		 * frame replaced =(uiObjectAdapter) currentAdapters.elementAt(index +
		 * 1); //currentAdapters.insertElementAt(adapter, index + 1); } else {
		 * replaced = selectionTop; //currentAdapters.addElement(adapter); }
		 */
		// uiObjectAdapter replaced = selectionTop; // simpler semantics
		/*
		 * System.out.println("replacing Frame"); debugScroll(replaced);
		 */
		// topAdapter = replaced;
		// currentAdapters.removeElement(replaced);
		// System.out.println("childPanel" + childPanel.getParent());
		// childPanel = (Container)
		// replaced.getParentAdapter().getUIComponent().getParent();
		// System.out.println("replavced: " + replaced);
		// System.out.println("replaced parent" + replaced.getParentAdapter());
		/*
		 * childPanel = (Container)
		 * replaced.getParentAdapter().getUIComponent();
		 * System.out.println("childPanel" + childPanel);
		 * System.out.println("spane" + childPanel.getParent()); ScrollPane
		 * replacedScrollPane = (ScrollPane) childPanel.getParent();
		 * //topAdapter = replaced; if (replacedScrollPane != null) spane =
		 * replacedScrollPane;
		 */
		// resetScrollPane(replaced);
		VirtualContainer replacedRootPane = restoreAdapter(replaced);
		if (replacedRootPane == null) {
			ObjectEditor.edit(obj, adapter);
			return;
		}
		frame.addUIFrameToolBarButton(FORWARD_ADAPTER_NAME, null);
		frame.addUIFrameToolBarButton(BACK_ADAPTER_NAME, null);
		VirtualContainer newRootPanel = this.createNewChildPanel();

		// System.out.println("putting" + adapter + spane);
		// adapterScrollPaneTable.put(adapter.getTopAdapter(adapter), spane);
		topAdapter = replaced;
		// this.removeToolBarButtons(replaced);
		// uiFrame editor =
		// uiGenerator.generateUIFrame(frame, obj, null, adapter, title);
		// uiObjectAdapter newAdapter = uiGenerator.generateInContainer(frame,
		// obj, adapter, newRootPanel);
		// uiObjectAdapter newAdapter = uiGenerator.generateInContainer(frame,
		// obj, adapter, newRootPanel, false);
		ObjectAdapter newAdapter = uiGenerator.generateInContainer(frame,
				obj, adapter, newRootPanel, true);
		setAdapter(newAdapter);
		// this.setNewChildPanel(newRootPanel);
		frame.showToolBar();
		// editor.setVisible(true);
		frame.setVisible(true);
		/*
		 * System.out.println("after generation"); debugScroll(replaced);
		 */
	}

	/*
	 * public void replaceFrame () { replaceFrame ((uiObjectAdapter)
	 * uiSelectionManager.getCurrentSelection()); }
	 */
	public void replaceFrame() {
		replaceFrame(
				(ObjectAdapter) SelectionManager.getCurrentSelection(),
				(Object) null, (String) null);
	}

	public void replaceFrame(Object obj) {
		replaceFrame((ObjectAdapter) null, obj, (String) null);
	}

	public void replaceFrame(Object obj, String title) {
		replaceFrame(null, obj, title);
	}

	public void replaceFrame(ObjectAdapter adapter) {
		replaceFrame(adapter, (Object) null, (String) null);
	}

	/*
	 * public Vector targetAdapters(Method method) { //print(currentAdapters);
	 * Enumeration adapters = currentAdapters.elements(); //probably should be
	 * an if here with a parameter saying should all adapters be chosen
	 * //Enumeration adapters = adapterHistory.elements(); boolean
	 * foundMultipleObjects = false; Vector targetAdapters = new Vector(); Class
	 * c; uiObjectAdapter adapter;
	 * 
	 * while (adapters.hasMoreElements()) { adapter = ((uiObjectAdapter)
	 * adapters.nextElement()); //Object object;
	 * 
	 * Object object = adapter.getValueOrRealObject();
	 * //System.out.println("trying adapter:" + adapter + "object" + object); if
	 * (object != null &&
	 * method.getDeclaringClass().isAssignableFrom(object.getClass())) { //
	 * System.out.println("successful: " + method.getDeclaringClass() +
	 * object.getClass());
	 * 
	 * targetAdapters.addElement(adapter); } }
	 * 
	 * return targetAdapters;
	 *  }
	 */
	/*
	 * public void processMethod (Method method) { Vector targetObjects =
	 * targetObjects(method); if (targetObjects.size() == 0)
	 * showMessage("Command cannot be invoked on any windowed object"); else if
	 * (targetObjects.size() == 1) new uiMethodInvocationManager(this,
	 * targetObjects.elementAt(0), method); else if
	 * (this.checkWithUser("Ambiguous command invocation", "Invoke command on
	 * all valid objects: " + targetObjects.toString())) for (int i = 0; i <
	 * targetObjects.size(); i++) new uiMethodInvocationManager(this,
	 * targetObjects.elementAt(i), method); else showMessage("Ambiguous command
	 * invocation: Command not executed");
	 *  }
	 */
	/*
	 * public void processMethod (Method method) { Vector targetAdapters =
	 * targetAdapters(method); if (targetAdapters.size() == 0)
	 * showMessage("Command cannot be invoked on any windowed object"); else if
	 * (targetAdapters.size() == 1) new uiMethodInvocationManager(this,
	 * targetObject((uiObjectAdapter) targetAdapters.elementAt(0)), method);
	 * else if (this.checkWithUser("Ambiguous command invocation", "Invoke
	 * command on all valid Adapters: " + targetAdapters.toString())) for (int i =
	 * 0; i < targetAdapters.size(); i++) new uiMethodInvocationManager(this,
	 * targetObject((uiObjectAdapter) targetAdapters.elementAt(i)), method);
	 * else showMessage("Ambiguous command invocation: Command not executed");
	 *  }
	 */

	public static String toStringAdapterList(Vector list, String prefix,
			String separator, String suffix) {
		String retVal = "";
		for (int i = 0; i < list.size(); i++) {
			String title = ((ObjectAdapter) list.elementAt(i))
					.getFrameTitle();
			if (i == 0)
				retVal += prefix + title + suffix;
			else
				retVal += separator + prefix + title + suffix;
		}
		return retVal;
	}

	/*
	 * public static String toStringVector(Vector list, String prefix, String
	 * separator, String suffix ) { String retVal = ""; for (int i=0; i<list.size();
	 * i++) { String title = list.elementAt(i).toString(); if (i == 0) retVal +=
	 * prefix + title + suffix; else retVal += separator + prefix + title +
	 * suffix; } return retVal; } public static String toStringVector(Vector
	 * list ) { return toStringVector(list, "", ", ", ""); }
	 */

	Vector currentAdapters = new Vector();
	Vector currentButtons = new Vector();

	public Vector getCurrentAdapters() {
		return currentAdapters;
		/*
		 * Vector retVal = new Vector(); for (int i = 0; i <
		 * currentAdapters.size(); i++) {
		 * retVal.addElement(currentAdapters.elementAt(i)); } for (int i = 0; i <
		 * userAdapters.size(); i++) {
		 * retVal.addElement(userAdapters.elementAt(i)); } return retVal;
		 */
	}

	public Vector currentAndUserAdapters() {
		// return currentAdapters;

		Vector retVal = new Vector();
		for (int i = 0; i < currentAdapters.size(); i++) {
			retVal.addElement(currentAdapters.elementAt(i));
		}
		for (int i = 0; i < frame.getUserAdapters().size(); i++) {
			retVal.addElement(frame.getUserAdapters().elementAt(i));
		}
		return retVal;

	}

	public void replaceAdapterInScrolledPanel(ObjectAdapter oldAdapter,
			ObjectAdapter newAdapter) {
		if (oldAdapter == null)
			return;
		int index = currentAdapters.indexOf(oldAdapter);
		if (index < 0)
			return;
		currentAdapters.removeElementAt(index);
		currentAdapters.insertElementAt(newAdapter, index);
		// int newIndex = currentAdapters.indexOf(newAdapter);
		// if (index != newIndex) System.out.println("index " + index + " new
		// index" + newIndex);
		Object scrollPane = adapterRootPaneTable.get(oldAdapter);
		if (scrollPane == null)
			return;
		adapterRootPaneTable.remove(oldAdapter);
		adapterRootPaneTable.put(newAdapter, scrollPane);
		// topAdapter = newAdapter;
		index = adapterHistory.indexOf(oldAdapter);
		if (index < 0)
			return;
		adapterHistory.removeElementAt(index);
		adapterHistory.insertElementAt(newAdapter, index);

	}

	public void addCurrentAdapter(ObjectAdapter adapter) {
		addCurrentAdapter(adapter, currentRootPane);
	}

	public void addCurrentAdapter(ObjectAdapter adapter,
			VirtualContainer spane) {
		if (spane == null)
			return;
		Object drawing = frame.getDrawing();
		// JToolBar toolBar = frame.getToolBar();
		VirtualContainer toolBar = frame.getToolBar();
		// System.out.println("adding " + adapter);
		/*
		 * if (adapter.getRealObject() == drawing) { frame.setDrawingAdapter
		 * (adapter); return; }
		 */
		if (adapter.getRealObject() == drawing
				|| adapter.getRealObject() == adapterHistory)
			return;
		currentAdapters.addElement(adapter);
		adapterHistory.addAdapter(adapter);
		// JButton button = new JButton(adapter.getFrameTitle());
		// VirtualButton button = new JButton(adapter.getFrameTitle());
		VirtualButton button = ButtonSelector.createButton(adapter
				.getFrameTitle());
		// new JButton(adapter.getFrameTitle());
		currentButtons.addElement(button);
		/*
		button.addActionListener(adapter);
		button.addMouseListener(adapter);
		*/
		button.addMouseListener(new ABrowserButtonClickMouseListener(adapter));

		int size = currentAdapters.size();
		/*
		 * if (size == 2) { toolBar.add((VirtualButton)
		 * currentButtons.elementAt(0)); toolBar.add((VirtualButton)
		 * currentButtons.elementAt(1));
		 *  } else if (size >2 ){ toolBar.add(button); }
		 */

		adapterRootPaneTable.put(adapter, spane);
		frame.setTitle();
		// this.setTitle(toStringBackAdapters() + " " +
		// toStringCurrentAdapters() + " " + toStringForwardAdapters());
		// mainScrollPane = spane;
		// System.out.println("adding current adapter");
		// System.out.println(currentAdapters);
	}

	public VirtualContainer getChildPanel() {
		return childPanel;
	}

	/*
	 * 
	 * public Container treeContainer() { return frame.getTreePanel(); } public
	 * Container treeComponent() { return frame.getJTree(); } public Container
	 * mainContainer() { return spane; } public Container secondaryContainer() {
	 * return getSecondaryScrollPane(); } public Container newTreeContainer() {
	 * Container retVal = frame.createTreePanel();
	 * frame.setTreePanelIsVisible(false); frame.showTreePanel(); return retVal; }
	 * public Container newMainContainer() { return
	 * createNewChildPanelInNewScrollPane(); } public Container
	 * newMainContainer(int direction) { return
	 * createNewChildPanelInNewScrollPane(direction); } public Container
	 * newSecondaryContainer() { return this.createSecondaryScrollPane(); }
	 * public Container newContainer() { return newMainContainer(); } public
	 * Container newContainer(int direction) { return
	 * newMainContainer(direction); }
	 */

	public Hashtable adapterContainerTable() {
		Hashtable retVal = new Hashtable();
		if (frame.treePanelIsVisible())
			retVal.put(frame.getTopTreeAdapter(), frame.getTreePanel());
		if (frame.secondaryScrollPaneIsVisible())
			retVal.put(frame.getSecondaryObjectAdapter(), frame
					.getSecondaryScrollPane());

		Enumeration currentKeys = adapterRootPaneTable.keys();
		while (currentKeys.hasMoreElements()) {
			Object key = currentKeys.nextElement();
			retVal.put(key, ((VirtualContainer) adapterRootPaneTable.get(key))
					.getComponent(0));
		}
		return retVal;
	}

	/*
	 * public Vector adapters() { Vector adapters = new Vector(); if
	 * (frame.treePanelIsVisible())
	 * adapters.addElement(frame.getTopTreeAdapter()); for (int index = 0; index <
	 * currentAdapters.size(); index ++) {
	 * adapters.addElement(currentAdapters.elementAt(index)); } if
	 * (frame.secondaryScrollPaneIsVisible())
	 * adapters.addElement(frame.getSecondaryObjectAdapter()); return adapters;
	 *  }
	 */
	Vector browsableContainers = new Vector();

	public void addBrowsableContainer(VirtualContainer c) {
		if (browsableContainers.contains(c))
			return;
		browsableContainers.addElement(c);
	}

	public void removeBrowsableContainer(VirtualContainer c) {
		if (!browsableContainers.contains(c))
			return;
		browsableContainers.removeElement(c);
	}

	public Vector containers() {
		Vector viewContainers = topViewManager.containers();
		if (viewContainers != null && viewContainers.size() > 0)
			return viewContainers;
		return browsableContainers;
	}

	public Vector adapters() {
		// Vector containers = topViewManager.containers();
		Vector containers = this.containers();
		Vector adapters = new Vector();
		for (int index = 0; index < containers.size(); index++) {
			Object adapter = (ObjectAdapter) scrollPaneAdapterTable
					.get(((VirtualContainer) containers.elementAt(index)));
			/*
			 * Object adapter; Container container = (Container)
			 * containers.elementAt(index); while (true) { //adapter =
			 * (uiObjectAdapter) containerAdapterTable.get( //((Container)
			 * containers.elementAt(index))); adapter = (uiObjectAdapter)
			 * containerAdapterTable.get(container); if (adapter != null) break;
			 * container = container.getParent(); if (container == null) break; }
			 */

			if (adapter != null)
				adapters.addElement(adapter);
		}
		return adapters;

	}

	/*
	 * public Vector containers() { Vector containers = new Vector(); if
	 * (frame.treePanelIsVisible()) containers.addElement(frame.getTreePanel());
	 * for (int index = 0; index < currentAdapters.size(); index ++) {
	 * adapters.addElement(currentAdapters.elementAt(index)); } if
	 * (frame.secondaryScrollPaneIsVisible())
	 * adapters.addElement(frame.getSecondaryObjectAdapter()); return
	 * containers; }
	 */
	/*
	 * public Vector containers() { Vector containers = new Vector(); Vector
	 * adapters = adapters(); Hashtable adapterContainerTable =
	 * adapterContainerTable(); for (int index = 0; index < adapters.size();
	 * index++) { Object adapter = adapters.elementAt(index);
	 * containers.addElement((adapterContainerTable.get(adapter)));
	 * 
	 *  } return containers; }
	 */
	/*
	 * public Vector containers() { return topViewManager.containers(); }
	 * 
	 * public Container containerAt(int index) { return (Container)
	 * containers().elementAt(index); } public uiObjectAdapter adapterAt(int
	 * index) { return (uiObjectAdapter) adapters().elementAt(index); } public
	 * int numContainers() { return adapters().size(); } public int
	 * indexOfContainer(Object container) { return
	 * containers().indexOf(container); }
	 */
	// Hashtable<VirtualScrollPane, uiObjectAdapter> scrollPaneAdapterTable =
	// new Hashtable();
	Hashtable<VirtualContainer, ObjectAdapter> scrollPaneAdapterTable = new Hashtable();

	public void secondaryPaneVisible() {
		ObjectAdapter adapter = getSecondaryObjectAdapter();
		if (adapter == null)
			return;
		VirtualContainer container = frame.getSecondaryScrollPane();
		scrollPaneAdapterTable.put(container, adapter.getTopAdapter());
		addBrowsableContainer(container);
		// setSecondaryObjectAdapter(adapter);
	}

	public void secondaryPaneHidden() {
		ObjectAdapter adapter = getSecondaryObjectAdapter();
		if (adapter == null)
			return;
		VirtualContainer container = frame.getSecondaryScrollPane();
		scrollPaneAdapterTable.remove(container);
		addBrowsableContainer(container);
		// setSecondaryObjectAdapter(adapter);
	}

	public void newAdapter(ObjectAdapter adapter, VirtualContainer container) {
		// public void newAdapter (uiObjectAdapter adapter, VirtualScrollPane
		// container) {
		// if (container != null) addBrowsableContainer(container);
		// System.out.println("adding adapter " + adapter.getID());
		// containerAdapterTable.put(container, adapter);
		if (container == frame.getSecondaryScrollPane()) {
			// if (adapter == getSecondaryObjectAdapter())
			// if (container == getSecondaryContainer())
			setSecondaryObjectAdapter(adapter);
			secondaryPaneVisible();
			/*
			 * scrollPaneAdapterTable.put(container, adapter.getTopAdapter());
			 * addBrowsableContainer(container);
			 * setSecondaryObjectAdapter(adapter);
			 */
		} else if (container == frame.getTreePanel()) {
			// else if (adapter == frame.getTopTreeAdapter())
			// else if (container ==
			scrollPaneAdapterTable.put(container, adapter.getTopAdapter());
			addBrowsableContainer(container);
			frame.setTreeRoot(adapter);
		} else /* if (isParentedContainer(container)) */{
			// VirtualContainer scrollPane = (VirtualContainer)
			// containerScrollPaneMapping.get(container);
			VirtualScrollPane scrollPane = (VirtualScrollPane) containerScrollPaneMapping
					.get(container);
			if (scrollPane != null)
				scrollPaneAdapterTable.put(scrollPane, adapter.getTopAdapter());
			// not sure why the parent is being put!
			/*
			 * else containerAdapterTable.put(container.getParent(),
			 * adapter.getTopAdapter()); //containerAdapterTable.put(container,
			 * adapter.getTopAdapter()); //addBrowsableContainer(container);
			 * //containerAdapterTable.put(container.getParent(),
			 * adapter.getTopAdapter());
			 * addBrowsableContainer(container.getParent());
			 * addParentedContainer(container.getParent()); if (topAdapter ==
			 * null) setAdapter(adapter, container.getParent()); else
			 * this.addCurrentAdapter(adapter, container.getParent());
			 */
			else
				scrollPaneAdapterTable.put(container, adapter.getTopAdapter());
			// containerAdapterTable.put(container, adapter.getTopAdapter());
			// addBrowsableContainer(container);
			// containerAdapterTable.put(container.getParent(),
			// adapter.getTopAdapter());
			addBrowsableContainer(container);
			addParentedContainer(container);
			if (topAdapter == null)
				setAdapter(adapter, container);
			else
				this.addCurrentAdapter(adapter, container);
		}
		setTitle();
	}

	public void retarget(ObjectAdapter oldAdapter, ObjectAdapter newAdapter) {
		if (oldAdapter == getSecondaryObjectAdapter())
			setSecondaryObjectAdapter(newAdapter);
		else if (oldAdapter == frame.getTopTreeAdapter())
			frame.setTreeRoot(newAdapter);
		else {
			VirtualContainer container = restoreAdapter(oldAdapter);
			replaceAdapterInScrolledPanel(oldAdapter, newAdapter);
		}
		// oldAdapter.cleanUp();
	}

	public ObjectAdapter browsee(ObjectAdapter adapter) {
		Vector currentAdapters = this.adapters();
		// Vector containers2 = this.containers();
		int index = currentAdapters.indexOf(adapter.getTopAdapter());
		if (index > 0)
			return (ObjectAdapter) currentAdapters.elementAt(index - 1);
		else
			return null;
	}

	boolean shouldBrowse(ObjectAdapter adapter) {
		return !frame.getTopViewManager().drawPanelIsVisible();

	}

	// public void browseObject (uiObjectAdapter adapter, Object obj, String
	// title) {
	public void browseObject(ObjectAdapter adapter) {
		// System.out.println("replace Frame called with " + adapter);

		// toolBar.removeAll();
		// toolBarButtons.removeAllElements();
		// methodActions.removeAllElements();
		ObjectAdapter selectionTop;
		if (adapter == null)
			return;
		if (!shouldBrowse(adapter))
			return;
		ObjectAdapter expandedAdapter;
		// if (adapter != null)
		expandedAdapter = adapter.getExpandedAdapter();
		// obj = adapter.getObject();
		if (expandedAdapter == null)
			return;
		Object obj = expandedAdapter.getObject();
		if (obj == null)
			return;
		// selectionTop = topAdapter;
		// else
		selectionTop = adapter.getTopAdapter(adapter);
		// System.out.println("selection Top" + selectionTop);
		ObjectAdapter replaced = null;
		/*
		 * //Container replacedPanel = null; //int index; if (selectionTop ==
		 * frame.getTopTreeAdapter()) { if (frame.drawPanelIsVisible() &&
		 * frame.secondaryScrollPaneIsVisible()) {
		 * createSecondaryScrollPane(expandedAdapter, obj);
		 * //createSecondaryScrollPane(obj); return;
		 *  } else if (frame.mainPanelIsVisible()) replaced = (uiObjectAdapter)
		 * currentAdapters.elementAt(0); else return;
		 *  } else {
		 */
		Vector currentAdapters = this.adapters();
		Vector<VirtualScrollPane> containers = this.containers();
		int index = currentAdapters.indexOf(selectionTop);
		VirtualContainer replacedPanel;
		if (index < 0 || index + 1 >= currentAdapters.size()) {

			if (!frame.secondaryScrollPaneIsVisible()
					|| adapter == getSecondaryObjectAdapter()) {
				if (adapter.getSelectionIsLink())
					replaceFrame(expandedAdapter, null, adapter);
				return;
			} else { // at all levels of frame stack, use secondary panel
				replaced = getSecondaryObjectAdapter();
				replacedPanel = getSecondaryScrollPane();
			}
		} else {
			// System.out.println("replacement index " + index);
			// if (index + 1 < currentAdapters.size()) { // next adpter is also
			// visible, replace its frame
			replaced = (ObjectAdapter) currentAdapters.elementAt(index + 1);
			if (replaced == null)
				return;
			// currentAdapters.insertElementAt(adapter, index + 1);
			// Container replacedPanel = topViewManager.containerAt(index + 1);
			// VirtualContainer replacedPanel = (VirtualContainer)
			// containers.elementAt(index + 1);
			replacedPanel = (VirtualContainer) containers.elementAt(index + 1);
		}
		// Container replacedPanel = (Container)
		// restoreAdapter(replaced).getComponent(0);
		if (this.isParentedContainer(replacedPanel)
				&& replacedPanel instanceof VirtualScrollPane)
			// replacedPanel = ((VirtualContainer)
			// replacedPanel.getComponent(0));
			replacedPanel = (VirtualContainer) ((VirtualScrollPane) replacedPanel)
					.getScrolledComponent();
		// ((Container) replacedPanel.getComponent(0)).removeAll();
		// else
		replacedPanel.removeAll();

		// System.out.println("putting" + adapter + spane);
		// adapterScrollPaneTable.put(adapter.getTopAdapter(adapter), spane);
		// topAdapter = replaced;
		// this.removeToolBarButtons(replaced);
		// uiFrame editor =
		/*
		 * uiObjectAdapter expandedAdapter; //if (adapter != null)
		 * expandedAdapter = adapter.getExpandedAdapter(); //obj =
		 * adapter.getObject(); obj = expandedAdapter.getObject(); if (obj ==
		 * null) return;
		 */
		// expandedAdapter.setBrowseLabel(uiGenericWidget.elideText(adapter,""));
		// expandedAdapter.setBrowseLabel(adapter.browseLabel());
		// uiObjectAdapter newAdapter = uiGenerator.generateInContainer(frame,
		// obj, expandedAdapter, replacedPanel);
		ObjectAdapter newAdapter;
		// if (replacedPanel instanceof VirtualScrollPane )
		if (!frame.isDummy()) {
			newAdapter = replacedAdapters.get(obj);
			// the display seems to not refresh well if it is not generated.
			// Maybe the whole frame should be refreshed
			newAdapter = null;
			if (newAdapter != null) {
				replacedPanel.add(newAdapter.getUIComponent());
				if (newAdapter.getGenericWidget() != null)
					newAdapter.getGenericWidget().cleanUpForReuse();
				newAdapter.implicitRefresh(true);
			} else {
				newAdapter = uiGenerator.generateInUIPanel(frame, obj, null,
						expandedAdapter, replacedPanel, null, null);
				replacedAdapters.put(obj, newAdapter);
			}
		} else {
			newAdapter = uiGenerator.generateInUIPanel(frame, obj, null,
					expandedAdapter, null, null, replacedPanel);
			replaced.getWidgetAdapter().cleanUp();
		}
		// containerAdapterTable.put(topViewManager.containerAt(index + 1),
		// newAdapter.getTopAdapter());
		newAdapter.setLabel(adapter.browseLabel());
		// newAdapter.setLabel(uiGenericWidget.elideText(expandedAdapter,""));
		scrollPaneAdapterTable.put(containers.elementAt(index + 1), newAdapter
				.getTopAdapter());
		// replaceAdapterInScrolledPanel(replaced, newAdapter.getTopAdapter());

		this.retarget(replaced, newAdapter.getTopAdapter());
		Vector currentAdapters2 = this.adapters();
		// Vector containers2 = this.containers();
		int index2 = currentAdapters2.indexOf(newAdapter.getTopAdapter());
		if (index2 >= 0 && index2 + 1 < currentAdapters2.size()
				&& newAdapter.elideChildrenByDefault())
			frame.internalElide(newAdapter, 2);
		replacedPanel.validate();
		// frame.resize();
		// frame.showToolBar();
		// editor.setVisible(true);
		/*
		 * System.out.println("after generation"); debugScroll(replaced);
		 */
	}

	public static String prefix(String s) {
		if (s == null) return "";
		if (s.length() <= MAX_TITLE_CHARS)
			return s;
		return s.substring(0, MAX_TITLE_CHARS) + "...";
	}

	static int MAX_TITLE_CHARS = 40;

	public void setTitle() {
		String treePart = "";
		ObjectAdapter treeAdapter = frame.getTopTreeAdapter();
		if (treeAdapter != null)
			treePart = treeAdapter.getFrameTitle() + ",";

		String mainPart = this.toStringBackAdapters()
				+ "["
				+
				// this.toStringCurrentAdapters() + /*frame.toStringSelection()
				// +*/ "]" +
				prefix(this.toStringCurrentAndUserAdapters())
				+ /* frame.toStringSelection() + */"]"
				+ prefix(this.toStringForwardAdapters()); /*
															 * +
															 * frame.toStringEdited();
															 */
		// mainPart = mainPart.substring(0, MAX_TITLE_CHARS);

		/*
		 * frame.setTitle(browser.toStringBackAdapters() + "[" +
		 * browser.toStringCurrentAdapters() + toStringSelection() + "]" +
		 * browser.toStringForwardAdapters() + toStringEdited() );
		 */
		String secondaryPart = "";
		ObjectAdapter secondaryAdapter = frame.getSecondaryObjectAdapter();
		if (secondaryAdapter != null)
			secondaryPart = "," + prefix(secondaryAdapter.getFrameTitle());
		// secondaryPart = secondaryPart.substring(0, MAX_TITLE_CHARS);
		String selectionTitle = frame.toStringSelection();
		// selectionTitle = selectionTitle.substring(0, MAX_TITLE_CHARS);
		frame.setAutomaticTitle(treePart + mainPart + secondaryPart
				+ prefix(selectionTitle) + frame.toStringEdited());
		// frame.setAutomaticTitle(treePart + mainPart + secondaryPart +
		// frame.toStringSelection() + frame.toStringEdited());
	}
	/*
	 * Vector userAdapters = new Vector(); public Vector getUserAdapters() {
	 * return userAdapters; } public void addUserAdapter(uiObjectAdapter
	 * adapter) { if (userAdapters.contains(adapter)) return;
	 * userAdapters.addElement(adapter); }
	 */

}
