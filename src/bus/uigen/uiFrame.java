package bus.uigen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Menu;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import slc.SLComposer;
import slgv.SLGView;
import slm.SLModel;
import slm.ShapesList;
import util.awt.AGlassPaneRedispatcher;
import util.misc.HashIdentityMap;
import util.misc.IdentityMap;
import util.models.AnOldCheckedVector;
import util.models.ABoundedBuffer;
import util.models.Hashcodetable;
import util.trace.TraceableClassToInstancesFactory;
import util.trace.TraceableLogFactory;
import util.trace.Tracer;
import util.undo.ExecutedCommand;
import bus.uigen.adapters.TabbedPaneAdapter;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.attributes.HashtableToInstanceAttributes;
import bus.uigen.attributes.UIAttributeManager;
import bus.uigen.compose.ButtonCommand;
import bus.uigen.controller.AToolbarManager;
import bus.uigen.controller.ConstantMenuItem;
import bus.uigen.controller.DoneListener;
import bus.uigen.controller.DoneMenuItem;
import bus.uigen.controller.KeyShortCuts;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.MethodInvocationRunnable;
import bus.uigen.controller.MethodParameters;
import bus.uigen.controller.ObjectClipboard;
import bus.uigen.controller.SelectionManager;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.controller.menus.AMenuGroup;
import bus.uigen.controller.menus.AMethodProcessor;
import bus.uigen.controller.menus.MenuSetter;
import bus.uigen.controller.menus.MethodMenuItem;
import bus.uigen.controller.menus.OEMenuItem;
import bus.uigen.controller.menus.SelectedMenuItem;
import bus.uigen.controller.menus.VirtualMethodMenuItem;
import bus.uigen.controller.models.ACustomizeOperationsModel;
import bus.uigen.controller.models.ADemoFontOperationsModel;
import bus.uigen.controller.models.ADoOperationsModel;
import bus.uigen.controller.models.AFileOperationsModel;
import bus.uigen.controller.models.AFontSizeModel;
import bus.uigen.controller.models.AMiscEditOperationsModel;
import bus.uigen.controller.models.AModelRegistry;
import bus.uigen.controller.models.ANewEditorOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.ASelectionOperationsModel;
import bus.uigen.controller.models.ASourceOperationsModel;
import bus.uigen.controller.models.ATreeWindowOperationsModel;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.controller.models.AnElideOperationsModel;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.controller.models.InteractiveMethodInvoker;
import bus.uigen.controller.models.MethodInvocationFrameCreationListener;
import bus.uigen.editors.ShapesAdapter;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.AttributeRegistry;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.RootAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.trace.DrawingPanelCreationStarted;
import bus.uigen.trace.FrameSetVisibleEnded;
import bus.uigen.trace.FrameSetVisibleStarted;
import bus.uigen.trace.FullRefreshFromProgram;
import bus.uigen.trace.ImplicitRefreshStarted;
import bus.uigen.trace.MenuTreeDisplayEnded;
import bus.uigen.trace.MenuTreeDisplayStarted;
import bus.uigen.trace.SelectionIndexOutOfBounds;
import bus.uigen.trace.SelectionOfIndexOfNonIndexedObject;
import bus.uigen.trace.SelectionOfInvisibleObject;
import bus.uigen.trace.SelectionOfPropertyOfNonBean;
import bus.uigen.undo.AHistoryUndoer;
import bus.uigen.undo.ListeningUndoer;
import bus.uigen.view.AFlexibleBrowser;
import bus.uigen.view.ASmartSplitPaneTopViewManager;
import bus.uigen.view.ATopViewManager;
import bus.uigen.view.AnnotationManager;
import bus.uigen.view.OEFrameSelector;
import bus.uigen.view.TopViewManager;
import bus.uigen.view.TreeView;
import bus.uigen.view.WidgetShell;
import bus.uigen.visitors.ElideAdapterVisitor;
import bus.uigen.visitors.ElideWithoutHandleAdapterVisitor;
import bus.uigen.visitors.HasUncreatedChildrenVisitor;
import bus.uigen.visitors.IsSerializableAdapterVisitor;
import bus.uigen.visitors.ToggleElideAdapterVisitor;
import bus.uigen.visitors.UpdateAdapterVisitor;
import bus.uigen.widgets.FrameSelector;
import bus.uigen.widgets.MenuBarSelector;
import bus.uigen.widgets.MenuItemSelector;
import bus.uigen.widgets.MenuSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDesktopPane;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.VirtualMenu;
import bus.uigen.widgets.VirtualMenuBar;
import bus.uigen.widgets.VirtualMenuComponent;
import bus.uigen.widgets.VirtualMenuItem;
import bus.uigen.widgets.VirtualPoint;
import bus.uigen.widgets.awt.AWTContainer;
import bus.uigen.widgets.display.ADisplayedContainer;
import bus.uigen.widgets.display.DisplayedComponent;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;
import bus.uigen.widgets.swing.DelegateJPanel;
import bus.uigen.widgets.swing.SwingComponent;
import bus.uigen.widgets.tree.VirtualTree;

public class uiFrame /* extends Frame */ extends ADummyCompleteOEFrame implements CompleteOEFrame {
//implements ActionListener, /*
//							 * TreeExpansionListener, TreeSelectionListener,
//							 * MouseListener, TreeModel,
//							 */Runnable, HistoryUndoerListener, /* SelectionListener, */
//		KeyListener {

	public static final int DEEP_ELIDE_LEVEL = 30;
	public static final String EDIT_MENU = "Edit";
	public static final String EXIT_COMMAND = "Exit";
	public static final String SOURCE_MENU = "Source";
	public static final String ALL_SOURCE_NAME = "All Source";
	public static final String FONT_SIZE_MENU_NAME = "Font Size";
	public static final String TOOLKIT_SELECTION_MENU_NAME = "Toolkit";
	public static final String AWT = "AWT";
	public static final String SWING = "Swing";
	public static final String DEMO_FONT_SIZE = "Demo Font Size";
	public static final String SOURCE_ALL_MENU_NAME = "All Sources";
	public static final String BEAN_METHODS_MENU_NAME = "Bean";
	public static final String CONSTANTS_MENU_NAME = "Constants";
	public static final String CUSTOMIZE_MENU = "Customize";
	public static final String SELECTED_COMMAND = "Selected";
	public static final String REFRESH_COMMAND = "Refresh";
	public static final String AUTO_REFRESH_COMMAND = "Auto Refresh";
	public static final String AUTO_REFRESH_ALL_COMMAND = "Auto Refresh All Frames";
	public static final String INCREMENTAL_REFRESH_COMMAND = "Incremental Refresh";
	// public static final String DEEP_ELIDE_2 = "Expand 2";
	// public static final String DEEP_ELIDE_2 = "Expand/Collapse Children";
	public static final String ELIDE_HANDLE = "Hide/Show Handles";
	// public static final String DEEP_ELIDE_3 = "Expand 3";
	// public static final String DEEP_ELIDE_4 = "Expand 4";
	public static final String DEEP_ELIDE_4 = "Deep Expand";
	public static final String ELIDE_COMMAND = "Expand/Collapse";
	public static final String ELIDE_CHILDREN_COMMAND = "Expand/Collapse Children";
	public static final String FORWARD_ADAPTER_NAME = "Forward";
	public static final String BACK_ADAPTER_NAME = "Back";
	public static final String REPLACE_FRAME_COMMAND = "Replace Window";
	public static final String NEW_FRAME_COMMAND = "New Editor";
	public static final String NEW_FRAME_COMMAND_WITHOUT_ATTRIBUTES = "New Editor Without Customization";
	public static final String NEW_TEXT_FRAME_COMMAND = "New Text Editor";
	public static final String NEW_TABLE_FRAME_COMMAND = "New Table Editor";
	public static final String NEW_TAB_FRAME_COMMAND = "New Tab Editor";
	public static final String NEW_DESKTOP_FRAME_COMMAND = "New Desktop Editor";
	public static final String NEW_SCROLL_PANE_COMMAND = "New Window Right";
	public static final String NEW_SCROLL_PANE_BOTTOM_COMMAND = "New Window Bottom";
	public static final String TREE_PANEL_COMMAND = "Tree";
	public static final String GRAPH_LOGICAL_STRUCTURE_COMMAND = "Graph Logical Structure";
	public static final String DRAW_PANEL_COMMAND = "Drawing";
	public static final String MAIN_PANEL_COMMAND = "Main Panel";
	public static final String SECONDARY_PANEL_COMMAND = "Secondary Panel";
	public static final String TOOLBAR_COMMAND = "Toolbar";
	public static final String WINDOW_HISTORY_PANEL_COMMAND = "Windows";
	public static final String DISPLAY_COMPLETE_WINDOW_TREE_COMMAND = "Display Complete Widget Tree";
	public static final String GRAPH_WINDOW_LOGICAL_STRUCTURE_COMMAND = "Graph Window Logical Structure";

	public static final String DISPLAY_TOPADAPTER_STRUCTURE_COMMAND = "Display Widget Tree";

	public static final String SAVE_COMMAND = "Save";
	public static final String SAVE_AS_FILE_COMMAND = "Save As...";
	public static final String SAVE_TEXT_AS_FILE_COMMAND = "Save Text As...";
	public static final String SAVE_TEXT_FILE_COMMAND = "Save Text...";
	public static final String OPEN_FILE_COMMAND = "Open...";
	public static final String LOAD_FILE_COMMAND = "Load...";
	public static final String UPDATE_COMMAND = "Update";
	public static final String UPDATE_ALL_COMMAND = "Update All";
	public static final String DONE_COMMAND = "Done";
	public static final String UNDO_COMMAND = "Undo";
	public static final String REDO_COMMAND = "Redo";
	public static final String CUT_COMMAND = "Cut";
	public static final String COPY_COMMAND = "Copy";
	public static final String DELETE_COMMAND = "Delete";
	public static final String PASTE_COMMAND = "Paste";
	public static final String PASTE_AFTER = "Paste After";
	public static final String PASTE_BEFORE = "Paste Before";
	public static final String INSERT_AFTER = "Insert After";
	public static final String INSERT_BEFORE = "Insert Before";
	public static final String LINK_COMMAND = "Link";
	public static final String CLEAR_COMMAND = "Delete All Elements";
	public static final String SELECT_PEERS_COMMMAND = "Select Peers";
	public static final String SELECT_UP_COMMAND = "Select Up";
	public static final String SELECT_DOWN_COMMAND = "Select Down";
	public static final String SELECT_ALL_COMMAND = "Select All";
	public static final String SETTINGS_COMMAND = "Settings";
	public static final String SELECTION_COMMAND = "Selection";
	public static final String BROADCAST = "Broadcast/Multicast";
	public static final String HELP_MENU_NAME = "Help";
	public static final String DOCUMENTS_MENU = "Documents";
	public static final String PROBLEMS_MENU = "Problems";

	public static final String ABOUT_COMMAND = "About Object Editor";
	public static final int TOOLBAR_HEIGHT = 75;
	public static final int TOOLBAR_WIDTH = 350;
	public static final int CHAR_WIDTH = 9;

	public static final int NORMAL_FRAME = 0;
	public static final int HELP_FRAME = 1;

	public static final String RIGHT_WINDOW_MESSAGE = "Selection in left window expanded here";
	public static final String BOTTOM_WINDOW_MESSAGE = "Selection in top window expanded here";

	// public static final String UNDO_REDO_MODEL = "UndoRedoModel";

	// Map<String, FrameModel> registeredModels = new Hashtable();
	/*
	 * public static final int FRAME_HEIGHT = 450; public static final int
	 * FRAME_WIDTH = 325; public static final int EMPTY_FRAME_HEIGHT = 40;
	 * public static final int EMPTY_FRAME_WIDTH = 250;
	 */

	// i'm using this so that the toolbar grouping stuff only happens once (the
	// main object window)
	// public static int toolbarCount = 0;
	VirtualMenuBar menuBar;
	boolean showMenuBar = true;
	VirtualMenuItem exitItem, saveItem, saveAsItem, forwardItem, backItem;
	VirtualMenuItem undoItem, redoItem;
	VirtualMenuItem cutItem, pasteItem, linkItem, copyItem;
	VirtualButton saveButton = null;
	public boolean isOEMainFrame = false;

	Vector<VirtualMethodMenuItem> customMenuItems = new Vector();
	Vector<ButtonCommand> customOrAutomaticButtons = new Vector();

	VirtualContainer myContainer;
	VirtualFrame myFrame;
	AModelRegistry modelRegistry = new AModelRegistry();
	
	List<MethodInvocationFrameCreationListener> methodInvocationFrameCreationListeners = new ArrayList();
	boolean suppressPropertyNotifications;

	public AModelRegistry getModelRegistry() {
		return modelRegistry;
	}

	public void setModelRegistry(AModelRegistry theModelRegistry) {
		modelRegistry = theModelRegistry;

	}

	public void registerModel(String name, FrameModel model) {
		// registeredModels.put(name, model);
		modelRegistry.registerModel(name, model);
	}

	public FrameModel getRegisteredModel(String name) {
		// return registeredModels.get(name);
		return modelRegistry.getRegisteredModel(name);
	}

	public void addCustomMenuItem(VirtualMethodMenuItem item) {
		customMenuItems.add(item);
	}

	public Vector<VirtualMethodMenuItem> getCustomMenuItems() {
		return customMenuItems;
	}

	public void addCustomOrAutomaticButton(ButtonCommand command) {
		customOrAutomaticButtons.add(command);
	}

	public Vector<ButtonCommand> getCustomButtons() {
		return customOrAutomaticButtons;
	}

	public boolean getShowMenuBar() {
		return showMenuBar;
	}

	AFlexibleBrowser browser;

	public AFlexibleBrowser getBrowser() {
		return browser;
	}
	
//	BasicObjectRegistry basicObjectRegistry = new ABasicObjectRegistry();
//	public BasicObjectRegistry getBasicObjectRegistery() {
//		return basicObjectRegistry;
//	}
//	 Hashcodetable<Object, Set<String>> visitedObjects = new Hashcodetable();
//	 public Hashcodetable<Object, Set<String>> getVisitedObjects() {
//		 return visitedObjects;
//	 }
	

	TopViewManager topViewManager;

	public TopViewManager getTopViewManager() {
		return topViewManager;
	}
	DisplayedComponent componentTree = null;
	DisplayedComponent topAdaptercomponentTree = null;
	public DisplayedComponent getComponentTree() {
		if (componentTree == null)
			componentTree = new ADisplayedContainer(getFrame().getContentPane());
		return componentTree;
	}
	public DisplayedComponent getTopAdapterComponentTree() {
		ObjectAdapter topAdapter = getTopAdapter();
		if (topAdapter == null)
			return getComponentTree();
		if (topAdaptercomponentTree != null)
			return topAdaptercomponentTree;
		WidgetShell widgetShell = topAdapter.getWidgetShell();
		VirtualComponent topComponent = null;
		VirtualContainer topContainer = null;
		if (widgetShell != null)
			topContainer = widgetShell.getContainer();
		else
			topComponent = topAdapter.getUIComponent();	
		if (topContainer == null && topComponent instanceof VirtualContainer) {
			topContainer = (VirtualContainer) topComponent;
		}
		if (topContainer!= null)		
			topAdaptercomponentTree = new ADisplayedContainer(topContainer);
		else if (topComponent != null)
			topAdaptercomponentTree = new ADisplayedContainer(topComponent);
		else
			topAdaptercomponentTree = null;

		return topAdaptercomponentTree;
	}

	AnnotationManager annotationManager;
	

	public AnnotationManager getAnnotationManager() {
		return annotationManager;
	}

	TreeView treeView;
	AToolbarManager toolbarManager;

	public AToolbarManager getToolbarManager() {
		return toolbarManager;
	}
	
	boolean isGlassPane;
	
	public void setIsGlassPane(boolean newVal) {
		isGlassPane = newVal;
	}
	
	public boolean isGlassPane() {
		return isGlassPane;
	}
	
	Object glassPaneModel;
	
	public Object getGlassPaneModel() {
		return glassPaneModel;
	}
	
	public VirtualComponent getDrawVirtualComponent() {
		
		return SwingComponent.virtualComponent(getDrawComponent());		
	}
	
	public JComponent setGlassPaneModel(Object aGlassPaneModel) {
		glassPaneModel = aGlassPaneModel;
		uiFrame editor = ObjectEditor.createOEFrame(null);
		if (!(editor.getFrame().getPhysicalComponent() instanceof JFrame)) {
			Tracer.error("Glass pane supported only for Swing toolkit");
			return null;
		}
		editor.setIsGlassPane(true);
		ObjectEditor.editInDrawingContainer(editor,glassPaneModel, (VirtualContainer) null, true);
		ObjectAdapter glassPaneAdapter = editor.getDrawingAdapter();
		ShapesAdapter shapesAdapter = (ShapesAdapter) glassPaneAdapter.getWidgetAdapter();
		SLGView view = shapesAdapter.getView();
		JComponent drawingComponent = view.getContainer();	
		JFrame myJFrame = (JFrame) myFrame.getPhysicalComponent();

//		VirtualComponent virtualComponent = AWTComponent.virtualComponent(jPanel);
//		JFrame myJFrame = (JFrame) myFrame.getPhysicalComponent();
		
		
//		AGlassPaneRedispatcher redispatcher = new AGlassPaneRedispatcher(drawingComponent, myJFrame);
		
		
		myJFrame.setGlassPane(drawingComponent);
		drawingComponent.setOpaque(false);
		drawingComponent.setVisible(true);
		return drawingComponent;

	}
	public JComponent setTelePointerModel(Object aGlassPaneModel) {
		JFrame myJFrame = (JFrame) myFrame.getPhysicalComponent();

		JComponent drawingComponent = setGlassPaneModel(aGlassPaneModel);
		if (drawingComponent == null) return null;
		AGlassPaneRedispatcher redispatcher = new AGlassPaneRedispatcher(drawingComponent, myJFrame);
		
		return drawingComponent;
		

	}

	
	public void init(VirtualFrame newFrame, VirtualContainer newContainer) {
		try {
			TraceableClassToInstancesFactory.getOrCreateTraceableClassToInstances();
			TraceableLogFactory.getTraceableLog();	
//			new AnOEFrameShutDownHook(this);
			myFrame = newFrame;
			myContainer = newContainer;
			// topViewManager = new ATopViewManager();
			// topViewManager = new ASplitPaneTopViewManager();
			topViewManager = new ASmartSplitPaneTopViewManager();
			annotationManager = new AnnotationManager(this);
			treeView = new TreeView();
			toolbarManager = new AToolbarManager(this);
			topViewManager.init(this, toolbarManager);
			treeView.init(this, topViewManager);
			// uiSelectionManager.addSelectionListener(this);
			// ObjectEditor.register();
			browser = new AFlexibleBrowser();
			browser.init(this, topViewManager, treeView);
			drawing = new SLModel();
			// ModelRegistry.putDefault(uiFrame.FILE_MENU_NAME, fileMenuModel);
			if (ObjectEditor.colabMode())
				ObjectRegistry.addUIFrame(this);
			/*
			 * uiSelectionManager.addSelectionListener(this);
			 * ObjectEditor.register();
			 */

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void init() {
		// Frame defaultFrame = new Frame();
		// JFrame defaultFrame = new JFrame();
		VirtualFrame defaultFrame = FrameSelector.createFrame();
		// Container defaultContainer = defaultFrame;
		VirtualContainer defaultContainer = defaultFrame.getContentPane();
		init(defaultFrame, defaultContainer);
	}

	public uiFrame() {
		// ObjectEditor.register();
	}

	public uiFrame(VirtualFrame newFrame, VirtualContainer newContainer) {
		init(newFrame, newContainer);
	}

	public uiFrame(Vector theAdapters, Vector theTargetAdaptersList,
			Vector theMethods) {
		init();
		// if (ObjectEditor.colabMode()) ObjectRegistry.addUIFrame(this);

		validAdapters = theAdapters;
		methods = theMethods;
		targetAdaptersList = theTargetAdaptersList;
	}

	public void init(Vector theAdapters, Vector theTargetAdaptersList,
			Vector theMethods) {
		validAdapters = theAdapters;
		methods = theMethods;
		targetAdaptersList = theTargetAdaptersList;
	}

	public boolean getTextMode() {
		return myMode;
	}

	public uiFrame(Object obj) {
		init();
		/*
		 * if (ObjectEditor.colabMode()) ObjectRegistry.addUIFrame(this);
		 */

		// ObjectEditor.registerEditors();
		// Inverses.init();
		// uiSelectionManager.addSelectionListener(this);
		topObject = obj;
		myMode = uiGenerator.textMode;
		// TOO EARLY TO GET IT - LET US PUT IT LATER AFTER ATTRIBUTED
		// INITIALIZED
		/*
		 * if (obj != null) methodsMenuName =
		 * ClassDescriptor.getMethodsMenuName(topObject.getClass());
		 */
		setBeanInfoSearchPath();
		/*
		 * if (setBeanInfoSearchPath) { String[] searchPath =
		 * {"sun.beans.infos", "bus.uigen.beans.infos"};
		 * java.beans.Introspector.setBeanInfoSearchPath(searchPath); }
		 */

		// originalBackground = this.getBackground();
		// Generate the basic ui frame
		// getContentPane().setLayout(new BorderLayout());
		setLayout(new BorderLayout());

		createMenuItemBar(showMenuBar);

		createToolBar();

		// addUIFrameToolBarButton(ELIDE_COMMAND, null);
		// mainPanel = new JPanel();
		// mainPanel = new JSplitPane();
		// mainPanel.setContinuousLayout(true);
		// mainPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// mainPanel.setLayout(new BorderLayout());
		// mainPanel.setLayout(new GridLayout());
		// childPanel = new JPanel();
		// childPanel.setLayout(new BorderLayout());
		// childPanel.setLayout(new GridLayout(2, 1));
		// ScrollPane spane = new ScrollPane();
		// spane.add(childPanel);

		// spane = new ScrollPane();
		// getContentPane().add(toolBar, BorderLayout.NORTH);
		// getContentPane().add(spane, BorderLayout.CENTER);

		// add(toolBar, BorderLayout.NORTH);
		// add(mainPanel, BorderLayout.CENTER);
		// add(spane, BorderLayout.CENTER);

		// mainPanel.add(spane);
		/*
		 * add(toolBar); add(mainPanel);
		 */
		// add(spane);
		// mainPanel.add(childPanel);
		// spane.add(mainPanel);
		final uiFrame me = this;
		 getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isTopFrame() && exitEnabled)
					System.exit(0);
				else {
					uiFrameList.removeFrame(me);
					setVisible(false);
					dispose();
				}
			}
		});

		Enumeration classes = AttributeManager.getEnvironment().getClassNames();
		while (classes.hasMoreElements()) {
			String name = (String) classes.nextElement();
			addClassToAttributeMenu(name);
		}
		if (obj != null)
			methodsMenuName = AClassDescriptor
					.getMethodsMenuName(ACompositeLoggable
							.getTargetClass(topObject));
		uiFrameList.addFrame(this);
		/*
		 * if (animationMode) { methodInvocationThread.start(); threadStarted =
		 * true; }
		 */
		noItemSelected();
		// (new ElideAdapterVisitor
		// (topAdapter)).traverseContainersFrom(DEEP_ELIDE_LEVEL);
		this.setResizable(true);
		setSize(ATopViewManager.FRAME_WIDTH, ATopViewManager.FRAME_HEIGHT);
		this.setResizable(true);
	}
	
	public void close() {
		uiFrameList.removeFrame(this);
		setVisible(false);
		dispose();
	}

	public static boolean appletMode = true;
	String[] searchPath = { "sun.beans.infos", "bus.uigen.beans.infos" };

	void setBeanInfoSearchPath() {
		try {
			if (appletMode)
				java.beans.Introspector.setBeanInfoSearchPath(searchPath);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public MenuSetter getMenuSetter() {
		return menuSetter;
	}

//	boolean isDummy = false;
//
//	public void setIsDummy(boolean newVal) {
//		isDummy = newVal;
//	}
//
//	public boolean isDummy() {
//		return isDummy;
//	}

	// maybe replace the above overload with just this one below and change
	// calls to pass null MenuSetter.
	public uiFrame(Object obj, boolean showMenus, MenuSetter defMenus,
			AMenuDescriptor theMenuDescriptor) {
		init();
		initFrameModels(obj);
		/*
		 * if (ObjectEditor.colabMode()) ObjectRegistry.addUIFrame(this);
		 */

		// ObjectEditor.registerEditors();
		topObject = obj;

		if (obj != null)
			methodsMenuName = AClassDescriptor
					.getMethodsMenuName(ACompositeLoggable
							.getTargetClass(topObject));
		setBeanInfoSearchPath();
		/*
		 * String[] searchPath = {"sun.beans.infos", "bus.uigen.beans.infos"};
		 * java.beans.Introspector.setBeanInfoSearchPath(searchPath);
		 */

		// originalBackground = this.getBackground();
		// Generate the basic ui frame
		// getContentPane().setLayout(new BorderLayout());
		setLayout(new BorderLayout());

		showMenuBar = showMenus;

		if (defMenus != null)
			createMenuItemBar(showMenus, defMenus);
		else
			createMenuItemBar(showMenus);

		createToolBar();

		// addUIFrameToolBarButton(ELIDE_COMMAND, null);
		// mainPanel = new JPanel();
		// mainPanel = new JSplitPane();
		// mainPanel.setContinuousLayout(true);
		// mainPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// mainPanel.setLayout(new BorderLayout());
		// mainPanel.setLayout(new GridLayout());
		// childPanel = new JPanel();
		// childPanel.setLayout(new BorderLayout());
		// childPanel.setLayout(new GridLayout(2, 1));
		// ScrollPane spane = new ScrollPane();
		// spane.add(childPanel);

		// spane = new ScrollPane();
		// getContentPane().add(toolBar, BorderLayout.NORTH);
		// getContentPane().add(spane, BorderLayout.CENTER);

		// add(toolBar, BorderLayout.NORTH);
		// add(mainPanel, BorderLayout.CENTER);
		// add(spane, BorderLayout.CENTER);

		// mainPanel.add(spane);
		/*
		 * add(toolBar); add(mainPanel);
		 */
		// add(spane);
		// mainPanel.add(childPanel);
		// spane.add(mainPanel);
		final uiFrame me = this;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isTopFrame())
					System.exit(0);
				else {
					uiFrameList.removeFrame(me);
					setVisible(false);
					dispose();
				}
			}
		});

		Enumeration classes = AttributeManager.getEnvironment().getClassNames();
		while (classes.hasMoreElements()) {
			String name = (String) classes.nextElement();
			addClassToAttributeMenu(name);
		}
		uiFrameList.addFrame(this);
		/*
		 * if (animationMode) { methodInvocationThread.start(); threadStarted =
		 * true; }
		 */
		// (new ElideAdapterVisitor
		// (topAdapter)).traverseContainersFrom(DEEP_ELIDE_LEVEL);
		this.setResizable(true);
		setSize(ATopViewManager.FRAME_WIDTH, ATopViewManager.FRAME_HEIGHT);
		this.setResizable(true);
	}

	public void init(Object obj) {
		init(obj, true, null, null);
	}

	MenuSetter menuSetter = null;
	
	public RootAdapter getRootAdapter() {
		ObjectAdapter topAdapter = getTopAdapter();
		if (topAdapter == null) {
			return null;
		}
		RootAdapter rootAdapter = topAdapter.getRootAdapter();
		return rootAdapter;
	}
	
	public BasicObjectRegistry getBasicObjectRegistry() {
		RootAdapter rootAdapter = getRootAdapter();
		if (rootAdapter == null) return null;
		return rootAdapter.getBasicObjectRegistery();
	}
	
//	public Object getObjectAdapter(Object anObject) {
//		BasicObjectRegistry aRegistry = getBasicObjectRegistry();
//		if (aRegistry == null)
//			return null;
//		return aRegistry.getObjectAdapter(anObject);
//	}

	// maybe replace the above overload with just this one below and change
	// calls to pass null MenuSetter.
	public void init(Object obj, boolean showMenus, MenuSetter defMenus,
			AMenuDescriptor theMenuDescriptor) {
		menuSetter = defMenus;
		menuDescriptor = theMenuDescriptor;
		topObject = obj;
		initFrameModels(obj);
		/*
		 * if (ObjectEditor.colabMode()) ObjectRegistry.addUIFrame(this);
		 */

		// ObjectEditor.registerEditors();
		// uiSelectionManager.addSelectionListener(this);
		uiFrameList.addFrame(this);
		//ObjectEditor.register();
		topObject = obj;
		// if (this.getContainer() == null) return;
		if (obj != null)
			methodsMenuName = AClassDescriptor
					.getMethodsMenuName(ACompositeLoggable
							.getTargetClass(topObject));
		setBeanInfoSearchPath();
		showMenuBar = showMenus;

		if (defMenus != null)
			createMenuItemBar(showMenus, defMenus);
		else
			createMenuItemBar(showMenus);

		createToolBar();

		// originalBackground = this.getBackground();

		// Generate the basic ui frame
		// getContentPane().setLayout(new BorderLayout());
		if (this.getContainer() == null)
			return;
		// uiSelectionManager.addSelectionListener(this);
		if (this.getLayout() == null)

			setLayout(new BorderLayout());
		/*
		 * showMenuBar = showMenus;
		 * 
		 * if (defMenus != null) createMenuItemBar( showMenus, defMenus); else
		 * createMenuItemBar(showMenus);
		 * 
		 * createToolBar();
		 */

		// addUIFrameToolBarButton(ELIDE_COMMAND, null);
		// mainPanel = new JPanel();
		// mainPanel = new JSplitPane();
		// mainPanel.setContinuousLayout(true);
		// mainPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// mainPanel.setLayout(new BorderLayout());
		// mainPanel.setLayout(new GridLayout());
		// childPanel = new JPanel();
		// childPanel.setLayout(new BorderLayout());
		// childPanel.setLayout(new GridLayout(2, 1));
		// ScrollPane spane = new ScrollPane();
		// spane.add(childPanel);
		// spane = new ScrollPane();
		// getContentPane().add(toolBar, BorderLayout.NORTH);
		// getContentPane().add(spane, BorderLayout.CENTER);
		// add(toolBar, BorderLayout.NORTH);
		// add(mainPanel, BorderLayout.CENTER);
		// add(spane, BorderLayout.CENTER);
		// mainPanel.add(spane);
		/*
		 * add(toolBar); add(mainPanel);
		 */
		// add(spane);
		// mainPanel.add(childPanel);
		// spane.add(mainPanel);
		final uiFrame me = this;
		 getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ObjectAdapter topAdapter = getBrowser().getOriginalAdapter();
				boolean autoSave = topAdapter.getAutoSave();
				if (autoSave)
					OEMisc.saveState(topAdapter.getRealObject());
				if (isTopFrame() && exitEnabled)
					System.exit(0);
//				else {
//					uiFrameList.removeFrame(me);
//					setVisible(false);
//					dispose();
//				}
			}
		});

		Enumeration classes = AttributeManager.getEnvironment().getClassNames();
		while (classes.hasMoreElements()) {
			String name = (String) classes.nextElement();
			addClassToAttributeMenu(name);
		}
		// uiFrameList.addFrame(this);
		/*
		 * if (animationMode) { methodInvocationThread.start(); threadStarted =
		 * true; }
		 */
		// (new ElideAdapterVisitor
		// (topAdapter)).traverseContainersFrom(DEEP_ELIDE_LEVEL);
//		this.setResizable(true);
//		setSize(ATopViewManager.FRAME_WIDTH, ATopViewManager.FRAME_HEIGHT);
		this.setResizable(true);
	}

	public Object getObject() {
		return topObject;
	}

	public VirtualFrame getFrame() {
		return myFrame;
	}

	public VirtualContainer getContainer() {
		return myContainer;
	}
	
	public void setContainer(VirtualContainer newVal) {
		myContainer = newVal;
	}

	public void add(VirtualComponent component) {
		myContainer.add(component);
	}

	public void add(VirtualComponent component, Object o) {
		myContainer.add(component, o);
	}

	public void remove(VirtualComponent component) {
		if (component != null)
			myContainer.remove(component);
	}

	public VirtualContainer getParent() {
		return myContainer.getParent();
	}

	public void validate() {
		if (myFrame != null) {
			myFrame.validate();
			// not necessary to do this
//			if (drawPanel != null) {
//				DelegateJPanel aComponent = getDrawComponent(drawPanel);
//				aComponent.validate();
//			}
		}
	}

	public void setVisible(boolean newVal) {
		if (myFrame != null) {
			FrameSetVisibleStarted.newCase(this, this);
			myFrame.setVisible(newVal);
			FrameSetVisibleEnded.newCase(this,  this);
		}
	}
	
	
	public void setSize(int newWidth, int newHeight) {
		if (myFrame != null && !isDummy()) {
			
			myFrame.setSize(newWidth, newHeight);
		}
	}
	
	public void setLocation() {
		topViewManager.setLocation();
		
	}
	
	public VirtualPoint getLocation() {
		return getFrame().getLocation();
	}

	public void setSize() {
		// System.out.println("Frame dimension" + myFrame.getSize());
		topViewManager.setSize();
	}
	
	

	public void resize(int newWidth, int newHeight) {
		myFrame.resize(newWidth, newHeight);
	}

	public void resize() {
		// int oldWidth = myFrame.getSize();
		myFrame.setSize(myFrame.getSize());
	}

	public void setResizable(boolean newVal) {
		if (myFrame != null)
			myFrame.setResizable(newVal);
	}

	public VirtualDimension getSize() {
		if (myFrame != null)
			return (VirtualDimension) myFrame.getSize();
		else if (myContainer != null)
			return (VirtualDimension) myContainer.getSize();
		else
			return null;
	}

	public void doubleWidth() {
		if (myFrame == null)
			return;
		VirtualDimension size = myFrame.getSize();
		myFrame.setSize(size.getWidth() * 2, size.getHeight());

	}

	public void doubleHeight() {
		VirtualDimension size = myFrame.getSize();
		myFrame.setSize(size.getWidth(), size.getHeight() * 2);
	}

	public int getComponentCount() {
		// if (myContainer == null) return 0;
		return myContainer.getComponentCount();
	}
	
	List<WindowListener> windowListeners = new ArrayList();
	@Override
	public void addWindowListener(WindowListener newVal) {
//		if (windowListeners.contains(newVal)) return;
//		windowListeners.add(newVal);
		if (myFrame != null)
			myFrame.addWindowListener(newVal);
	}

	/*
	 * & public void repaint() { myFrame.repaint(); }
	 */
	public void show() {
		myFrame.setVisible(true);
	}

	public void hide() {
		myFrame.setVisible(false);
	}

	public void show(boolean newVal) {
		myFrame.setVisible(newVal);
	}
	boolean disposed;

	public void dispose() {
		if (disposed)
			return;
		disposed = true;
		myFrame.dispose();
		ObjectAdapter rootAdapter = this.getRootAdapter();
		if  (rootAdapter != null) {
			ObjectAdapter.recursiveCleanUp(rootAdapter);
		}
		uiFrameList.removeFrame(this);
	}

	public void setLocation(int newX, int newY) {
		myFrame.setLocation(newX, newY);
	}

	public void setLocation(Point newVal) {
		myFrame.setLocation(newVal);
	}
	
	

	public String getTitle() {
		return myFrame.getTitle();
	}

	boolean manualTitle = false;

	public boolean isManualTitle() {
		return manualTitle;
	}

	public void setTitle(String newVal) {
		myFrame.setTitle(newVal);
		manualTitle = true;
	}

	public void setAutomaticTitle(String newVal) {
		if (myFrame == null)
			return;
		if (!manualTitle)
			myFrame.setTitle(newVal);
	}

	public void setMenuBar(VirtualMenuBar newVal) {
		if (myFrame != null && newVal != null)
			myFrame.setMenuBar(newVal);
	}

	public void setSaveButton(VirtualButton newVal) {
		saveButton = newVal;
	}

	public void doLayout() {
		myContainer.doLayout();
	}

	public void setLayout(LayoutManager newVal) {
		if (myContainer != null && myContainer.getLayout() != null
				&& myContainer.getLayout().getClass() != newVal.getClass()
				&& myContainer.getComponentCount() == 0) {
			myContainer.setLayout(newVal);
		}
	}

	// public LayoutManager getLayout() {
	public Object getLayout() {
		return myContainer.getLayout();
	}

	public void setCursor(int newVal) {
		if (myFrame != null)
			myFrame.setCursor(newVal);
	}

	public void setCursor(Cursor newVal) {
		myContainer.setCursor(newVal);
	}

	int frameKind = NORMAL_FRAME;

	public int getFrameKind() {
		return frameKind;
	}

	public void setFrameKind(int theFrameKind) {
		frameKind = theFrameKind;
	}

	Hashtable selfAttributes;
	Hashtable<String, Object> defaultAttributes = new Hashtable();

	public void setDefaultAttribute(String attributeName, Object value) {
		defaultAttributes.put(attributeName, value);
	}

	public Object getDefaultAttribute(String attributeName) {
		return defaultAttributes.get(attributeName);
	}

	public void setSelfAttributes(Hashtable newVal) {
		selfAttributes = newVal;
	}

	public Hashtable getSelfAttributes() {
		return selfAttributes;
	}

	Vector childrenAttributes;

	public void setChildrenAttributes(Vector newVal) {
		childrenAttributes = newVal;
	}

	public Vector getChildrenAttributes() {
		return childrenAttributes;
	}

	public void setAttributes(Hashtable selfAttributes,
			Vector childrenAttributes) {
		setSelfAttributes(selfAttributes);
		setChildrenAttributes(childrenAttributes);
		ObjectAdapter selfAdapter = getTopAdapter();
		setAttributes(selfAdapter, selfAttributes, childrenAttributes);
		// being done above

//		if (selfAdapter != null)
//			selfAdapter.initAttributes(selfAttributes, childrenAttributes);
		

	}
	
	

	public void setAttributes(Hashtable selfAttributes) {
		if (selfAttributes == null)
			return;
		setAttributes(selfAttributes, null);

	}

	public void setAttributes(ObjectAdapter selfAdapter,
			Hashtable aSelfAttributes, Vector aChildrenAttributes) {

		if (selfAdapter != null)
			selfAdapter.initAttributes(aSelfAttributes, aChildrenAttributes);
		// setSelfAttributes(selfAttributes);
		// setChildrenAttributes (childrenAttributes);

	}
	
	public void setFrameAttributes(ObjectAdapter selfAdapter) {
		setAttributes(selfAdapter, getSelfAttributes(), getChildrenAttributes());

	}

	public void setAttributes() {
		setAttributes(getSelfAttributes(), getChildrenAttributes());

	}

	// JToolBar toolBar;

	public VirtualContainer getToolBar() {
		return toolbarManager.getToolBar();
	}

	public void createMainScrollPane() {
		browser.createMainScrollPane();
	}

	public VirtualComponent getMainScrollPane() {
		return topViewManager.getMainContainer();
	}

	public VirtualContainer getSecondaryScrollPane() {
		return topViewManager.getSecondaryContainer();
	}

	public ObjectAdapter getSecondaryObjectAdapter() {
		return browser.getSecondaryObjectAdapter();
	}

	public void setSecondaryObjectAdapter(ObjectAdapter adapter) {
		browser.setSecondaryObjectAdapter(adapter);
	}

	public void createSecondaryScrollPane(Object o) {
		browser.createSecondaryScrollPane(o);
	}

	public void createSecondaryScrollPane(ObjectAdapter o) {
		browser.createSecondaryScrollPane(o);
	}

	public void createSecondaryScrollPane() {
		browser.createSecondaryScrollPane();
	}

	public VirtualContainer getTreePanel() {
		return topViewManager.getTreeContainer();
	}

	public ObjectAdapter getTopTreeAdapter() {
		return treeView.getTopTreeAdapter();
	}

	public TreeView getJTreeAdapter() {
		return treeView;
	}

	public VirtualContainer getToolPanel() {
		return toolbarManager.getToolPanel();
	}

	/*
	 * Container manualToolbar; public Container getManualToolbar() { return
	 * manualToolbar; }
	 */

	public VirtualComponent getWindowHistoryPanel() {
		return windowHistoryPanel;
	}

	public boolean treePanelIsVisible() {
		return topViewManager.treePanelIsVisible();
	}

	// JPanel toolPanel ;

	// JPanel toolBar;
	// Menu toolMenu;

	Hashtable<String, VirtualMenu> menus = new Hashtable();
	// Hashtable toolbars = new Hashtable();

	/*
	 * new browser Container childPanel;
	 */

	int parameterNumber = -1;
	private boolean topFrame = false;

	/*
	 * new browser uiObjectAdapter topAdapter = null; uiObjectAdapter
	 * origAdapter = null;
	 */
	// Undoer undoer = new AHistoryUndoer(this);
	ListeningUndoer undoer = new AHistoryUndoer(this);
	KeyShortCuts keyShortCuts = new KeyShortCuts(this);
	// uiAttributeManager attributeManager = null;

	public static AutomaticRefresh getChildAdapterCountReq = null;
	public static AutomaticRefresh getChildCountReq = null;

	public ListeningUndoer getUndoer() {
		return undoer;
	}

	public void setUndoer(ListeningUndoer newVal) {
		undoer = newVal;
		undoer.addListener(this);
	}

	public KeyShortCuts getKeyShortCuts() {
		return keyShortCuts;
	}

	public void setKeyShortCuts(KeyShortCuts newVal) {
		keyShortCuts = newVal;
	}

	public boolean isTopFrame() {
		return (uiFrameList.getList().size() <= 1);
	}

	public void setTopFrame() {
		topFrame = true;
	}

	public void setParameterNumber(int p) {
		parameterNumber = p;
	}

	public int getParameterNumber() {
		return parameterNumber;
	}

	boolean edited = false;
	String title;

	public void setEdited() {
		setEdited(true);
	}

	
	public void setEdited(boolean flag) {
		if (edited == flag)
			return;
		edited = flag;
		setTitle();
	}

	public String toStringEdited() {
		if (edited)
			return "*";
		else
			return "";

	}

	/*
	 * new browser AdapterHistory adapterHistory = new AdapterHistory();
	 */

	final String PREV_ADAPTER_SEPARATOR = "<-";

	

	public void setNewChildPanel(VirtualContainer newChildPanel) {
		if (getContainer() == null)
			return;
		browser.setNewChildPanel(newChildPanel);
	}

	public VirtualContainer createNewChildPanelInNewScrollPane() {

		return browser.createNewChildPanelInNewScrollPane();
	}

	public VirtualContainer createNewChildPanelInNewScrollPane(int direction) {
		return browser.createNewChildPanelInNewScrollPane(direction);
	}

	public void repaint() {
		// super.repaint();
		if (myFrame == null) return;
		myFrame.repaint();
		toolbarManager.getToolBar().revalidate();
		/*
		 * JToolBar foo = null; foo.revalidate();
		 */
		// this.toolBar.repaint();
	}

	static Thread paintThread;
	static Thread eventThread;
	static Thread blockedThread;

	/*
	 * public void paint(Graphics g) { super.paint(g); Thread currentThread =
	 * Thread.currentThread();
	 * 
	 * if (currentThread != paintThread) { paintThread = currentThread;
	 * System.out.println("new paint thread" + currentThread);
	 * Thread.currentThread().getThreadGroup().list();
	 * System.out.println(paintThread.getClass()); if (currentThread ==
	 * eventThread) System.out.println("currentThread is paintThread"); } if
	 * (blockedThread != null) { System.out.println("paint thread during
	 * blocking" + currentThread); if (blockedThread == currentThread)
	 * System.out.println("paint thread same as blocked thread!");
	 * currentThread.dumpStack(); }
	 * 
	 *  }
	 */
	public void backAdapter() {
		browser.backAdapter();
	}

	/*
	 * new browser public void backAdapter() {
	 * changeToExistingAdapter(skipPrevCurrentAdapters(adapterHistory.toPrevAdapter())); }
	 * 
	 * 
	 * public void forwardAdapter() {
	 * changeToExistingAdapter(skipNextCurrentAdapters(adapterHistory.toNextAdapter())); }
	 * 
	 * public void changeToExistingAdapter(uiObjectAdapter adapter) {
	 * System.out.println("starting change to existing" + adapter);
	 * 
	 * 
	 * 
	 * changeAdapter(adapter); if (adapter == null) return;
	 * //System.out.println(adapter.getUIComponent()); restoreAdapter(adapter);
	 * changeChildPanel( (Container)
	 * adapter.getParentAdapter().getUIComponent());
	 * //System.out.println("returned from child panel");
	 * //changeAdapter(adapter); //System.out.println("returned from change
	 * Adapter"); //adapter.refresh(); //deepElide(adapter); this.validate();
	 * //System.out.println("finished validation"); //this.repaint(); }
	 */
	public static void setButtonEnabled(VirtualButton button, boolean enabled) {
		if (button != null)
			button.setEnabled(enabled);
	}

	public void setForwardEnabled(boolean enabled) {
		if (forwardItem != null)
			forwardItem.setEnabled(enabled);
		toolbarManager.setForwardEnabled(enabled);
		// setButtonEnabled(forwardButton, enabled);
	}

	public void setBackEnabled(boolean enabled) {
		if (backItem != null)
			backItem.setEnabled(enabled);
		toolbarManager.setBackEnabled(enabled);
		// setButtonEnabled(backButton, enabled);
	}

	/*
	 * new browser //Hashtable adapterTable = new Hashtable(); public
	 * uiObjectAdapter skipPrevCurrentAdapters(uiObjectAdapter adapter) { if
	 * (adapter != null && (currentAdapters.contains(adapter) ||
	 * adapter.getRealObject() == drawing || adapter.getRealObject() ==
	 * adapterHistory) ) { //System.out.println("prev curent adapter " +
	 * adapter); //System.out.println("current" +
	 * currentAdapters.contains(adapter)); //System.out.println("drawing" +
	 * (adapter.getRealObject() == drawing)); //System.out.println("history" +
	 * (adapter.getRealObject() == adapterHistory));
	 * 
	 * 
	 * //this.resetScrollPane(adapter); //restoreAdapter(adapter); //topAdapter =
	 * adapter;
	 * 
	 * return skipPrevCurrentAdapters(adapterHistory.toPrevAdapter()); } else {
	 * System.out.println("prev adapter " + adapter); return adapter; } } public
	 * uiObjectAdapter skipNextCurrentAdapters(uiObjectAdapter adapter) { if
	 * (adapter != null && (currentAdapters.contains(adapter) ||
	 * adapter.getRealObject() == drawing || adapter.getRealObject() ==
	 * adapterHistory)) { //System.out.println("next curent adapter " +
	 * adapter);
	 * 
	 * //resetScrollPane(adapter); //restoreAdapter(adapter); //topAdapter =
	 * adapter; return skipNextCurrentAdapters(adapterHistory.toNextAdapter()); }
	 * else {
	 * 
	 * //System.out.println("next adapter " + adapter); return adapter; } }
	 * 
	 * void swapCurrentAdapters (uiObjectAdapter cur, uiObjectAdapter next) { if
	 * (cur == null) return; int index = currentAdapters.indexOf(cur); if (index <
	 * 0) System.out.println("current adapter not there"); else {
	 * //System.out.println("cur" + cur); //System.out.println("next" + cur);
	 * //System.out.println("index" + index); removeToolBarButtons(cur);
	 * addToolBarButtons(next); System.out.println( currentAdapters);
	 * currentAdapters.removeElement(cur); currentAdapters.insertElementAt(next,
	 * index); System.out.println("after swap");
	 * //System.out.println(currentAdapters); }
	 *  }
	 */

	public void singleItemSelected() {
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
		noItemSelected();
	}

	public void processCut(ObjectAdapter selection) {
		// cutItem.setEnabled(selection.isDeletable());
		cutItem.setEnabled(selection.isDeletable());
	}

	public void processLink(ObjectAdapter selection) {
		Object target = ObjectClipboard.getFirst();
		// linkItem.setEnabled(
		linkItem.setEnabled(target != null
				&& (selection.getPropertyClass().isAssignableFrom(
						ACompositeLoggable.getTargetClass(target)) || selection
						.isAddableToParent(target)));

	}

	public void processCopy(ObjectAdapter selection) {
		Object source = selection.getObject();
		// linkItem.setEnabled(
		copyItem.setEnabled(source instanceof java.io.Serializable);
	}

	public void processPaste(ObjectAdapter selection) {
		System.out.println("process paste");
		Object target = ObjectClipboard.getFirst();
		boolean retVal = target != null
				&& target instanceof java.io.Serializable
				&& (selection.getPropertyClass().isAssignableFrom(
						ACompositeLoggable.getTargetClass(target)) || selection
						.isAddableToParent(target));
		System.out.println("paste enable? " + retVal);
		pasteItem.setEnabled(retVal);
		/*
		 * 
		 * //linkItem.setEnabled( pasteItem.setEnabled( target != null && target
		 * instanceof java.io.Serializable &&
		 * (selection.getPropertyClass().isAssignableFrom(target.getClass()) ||
		 * selection.isAddableToParent(target)));
		 */

	}

	public String toStringSelection() {
		ObjectAdapter selection = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		if (selection == null)
			return "";
		// if (selection.getAdapterType() =
		String retVal = selection.getBeautifiedPath();
		if (retVal.equals(""))
			retVal = selection.getFrameTitle();
		return "(" + retVal + ")";

	}

	public void setTitle() {
		if (!isManualTitle())
			browser.setTitle();
		/*
		 * setTitle(browser.toStringBackAdapters() + "[" +
		 * browser.toStringCurrentAdapters() + toStringSelection() + "]" +
		 * browser.toStringForwardAdapters() + toStringEdited() );
		 */
	}

	/*
	 * public void changeAdapter(uiObjectAdapter adapter) {
	 * setForwardEnabled(!(adapterHistory.nextAdapter() == null));
	 * setBackEnabled(!(adapterHistory.prevAdapter() == null)); if (adapter ==
	 * null) return; Object obj = adapter.getRealObject();
	 * 
	 * 
	 * 
	 * if (getOriginalAdapter() == null || origAdapter.getRealObject() == null) {
	 * //origAdapter = adapter; setOriginalAdapter(adapter);
	 * //System.out.println("original adapter" + origAdapter); }
	 * 
	 * swapCurrentAdapters(topAdapter, adapter); topAdapter = adapter;
	 * //this.setTitle(toStringBackAdapters() + " " + adapter.getFrameTitle() + " " +
	 * toStringForwardAdapters()); //this.setTitle(toStringBackAdapters() + " " +
	 * toStringCurrentAdapters() + " " + toStringForwardAdapters()); setTitle();
	 * //Object obj = adapter.getRealObject(); if (obj == null)
	 * setSaveAsMenuItemEnabled(true); else
	 * //setSaveMenuItemEnabled(isSavable(topAdapter));
	 * setSaveAsMenuItemEnabled(isSavable(getOriginalAdapter()));
	 * //System.out.println(currentAdapters); //System.out.println(obj);
	 * 
	 *  // Set the frame title
	 * 
	 *  }
	 * 
	 * public void debugScroll (uiObjectAdapter adapter) { adapter =
	 * adapter.getParentAdapter(); System.out.println("adaptor" + adapter);
	 * Component component = adapter.getUIComponent();
	 * //System.out.println("component" + component);
	 * //System.out.println("parent adaptor" + adapter.getParentAdapter());
	 * System.out.println("parent component" + component.getParent());
	 *  }
	 */

	public void hideMainIfNoProperties() {
		if (browser.noPropertiesInAdapterHistory())
			topViewManager.hideMainPanel();

	}

	
	public ObjectAdapter getOriginalAdapter() {
		return browser.getOriginalAdapter();
	}

	public ObjectAdapter getTopAdapter() {
		return browser.getTopAdapter();
	}

	public void setAdapter(ObjectAdapter adapter) {
		browser.setAdapter(adapter);
		// getTopAdapter().initAttributes(getSelfAttributes(),
		// getChildrenAttributes());

	}

	// SLComposer drawPanel;
	/*
	 * public void createDrawPanel() { try { drawPanel = new SLComposer(this);
	 * this.add(drawPanel, BorderLayout.SOUTH); } catch (Exception e) {
	 * System.out.println(e); } }
	 */
	public ObjectAdapter getAdapter() {
		return browser.getAdapter();
	}

	int shapeNum = 0;

	public void addToDrawing(ObjectAdapter adapter, Object o) {
		/*
		 * if (!(o instanceof ShapeModel)) return;
		 * System.out.println("shapeModel"); ShapeModel shape = (ShapeModel) o;
		 * SLModel drawing = getDrawing(); //String key =
		 * Integer.toString(shapeNum++); String key =
		 * adapter.getBeautifiedPath(); drawing.put(key, shape);
		 * //drawing.put(key, adapter.getBeautifiedPath());
		 */
	}

	Vector currentObjects = new Vector();
	// Panel drawPanel;
	VirtualContainer drawPanel;
	// Connections drawing = new Connections();
	SLModel drawing /* = new SLModel() */;

	public SLModel getDrawing() {
		// System.out.println("drawing is" + drawing);
		return drawing;
		/*
		 * if (drawing != null) return drawing.getDrawing(); else return null;
		 */

	}

	ObjectAdapter drawingAdapter;

	public ObjectAdapter getDrawingAdapter() {
		return drawingAdapter;
	}

	/*
	 * public boolean drawPanelIsVisible() { if (drawPanel == null) return
	 * false; return drawPanel.isVisible(); }
	 */
	public VirtualContainer getDrawPanel() {
		return drawPanel;
	}
	
	public DelegateJPanel getDrawComponent() {
		
		return getDrawComponent(drawPanel);
	}
	
	public DelegateJPanel getDrawComponent(VirtualContainer c) {
		if (c != null & c.getComponentCount() > 0 ) {
			VirtualComponent vitualComponent = c.getComponent(0);
			Object physicalComponent = vitualComponent.getPhysicalComponent() ;
			if (physicalComponent instanceof DelegateJPanel)
				return (DelegateJPanel) physicalComponent;
			else if (vitualComponent instanceof VirtualContainer)
				return getDrawComponent( (VirtualContainer) vitualComponent);
			else
				return null;			
		}
		return null;
	}

	public boolean drawPanelIsInitialized() {
		return drawPanelInitialized;
	}

	boolean drawPanelInitialized = false;
	
	public void createDrawComponent() {
		try {
			if (drawingAdapter != null)
				return;
			// bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel",
			// "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");
			bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel",
					"java.awt.Container", "bus.uigen.editors.ShapesAdapter");
			// bus.uigen.editors.EditorRegistry.registerWidget(slm.SLModel.class,
			// java.awt.Container.class, bus.uigen.editors.ShapesAdapter.class);
			// uiGenerator.generateUI(drawPanel, new slm.SLModel());
			// drawing = new Connections();
			currentObjects.addElement(drawing);
			// drawingAdapter = uiGenerator.generateInContainer(this, drawing,
			// drawPanel);
			// drawingAdapter = uiGenerator.generateInUserContainer(this,
			// drawing, drawPanel, isRootDrawContainer);
			if (isRootDrawContainer) {

				drawingAdapter = uiGenerator.generateInUIPanel(this, drawing,
						null, getOriginalAdapter(), drawPanel, null, null);
			} else {
				drawingAdapter = uiGenerator.generateInUIPanel(this, drawing,
						null, getTopAdapter(), null, null, drawPanel);
			}
			// drawingAdapter = (uiObjectAdapter)
			// currentAdapters.elementAt(currentAdapters.size() -1);
			// uiGenerator.generateUI(drawPanel, new
			// Connections().getDrawing());
			// this.add(drawPanel, BorderLayout.SOUTH);
			showDrawPanel();
			/*
			 * if (mainScrollPaneIsVisible) { hideMainPanel();
			 * this.add(drawPanel, BorderLayout.CENTER); showMainPanel(); } else
			 * this.add(drawPanel, BorderLayout.SOUTH);
			 */
			// drawPanel.setBackground(Color.white);
			// int drawHeight = drawPanel.getBounds().height;
			VirtualDimension mySize = this.getSize();
			if (mySize != null) {
				// System.out.println(drawHeight);
				this.setSize(Math.max(mySize.getWidth(), SLComposer.FRAME_WIDTH),
						mySize.getHeight() + SLComposer.FRAME_HEIGHT);
				// this.setSize(300, 300);
				mySize = getSize();
			}
			ObjectAdapter topAdapter = getOriginalAdapter();
//			getDrawComponent().setBackground(AttributeNames.getDrawingPanelColor());
			if (!isDummy())
			getDrawComponent().setBackground(topAdapter.getDrawingPanelColor());	
			else {
				DelegateJPanel drawComponent = getDrawComponent();
				VirtualContainer drawPanel = getDrawPanel();
				drawComponent.setBackground((Color) drawPanel.getBackground());
			}

			// System.out.println(mySize);
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e);
		}
		
	}

	public void createDrawPanel() {
		// if (drawPanel != null) return;
		if (drawPanelInitialized)
			return;
		DrawingPanelCreationStarted.newCase(this, this);
		drawPanelInitialized = true;
		if (drawPanel == null && !isGlassPane()) {
			// drawPanel = new Panel();
			drawPanel = PanelSelector.createPanel();
			drawPanel.setName(AttributeNames.DRAW_PANEL_NAME);
			drawPanel.setLayout(new BorderLayout());
		}
		createDrawComponent();
//		try {
//			// bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel",
//			// "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");
//			bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel",
//					"java.awt.Container", "bus.uigen.editors.ShapesAdapter");
//			// bus.uigen.editors.EditorRegistry.registerWidget(slm.SLModel.class,
//			// java.awt.Container.class, bus.uigen.editors.ShapesAdapter.class);
//			// uiGenerator.generateUI(drawPanel, new slm.SLModel());
//			// drawing = new Connections();
//			currentObjects.addElement(drawing);
//			// drawingAdapter = uiGenerator.generateInContainer(this, drawing,
//			// drawPanel);
//			// drawingAdapter = uiGenerator.generateInUserContainer(this,
//			// drawing, drawPanel, isRootDrawContainer);
//			if (isRootDrawContainer) {
//
//				drawingAdapter = uiGenerator.generateInUIPanel(this, drawing,
//						null, getOriginalAdapter(), drawPanel, null, null);
//			} else {
//				drawingAdapter = uiGenerator.generateInUIPanel(this, drawing,
//						null, getTopAdapter(), null, null, drawPanel);
//			}
//			// drawingAdapter = (uiObjectAdapter)
//			// currentAdapters.elementAt(currentAdapters.size() -1);
//			// uiGenerator.generateUI(drawPanel, new
//			// Connections().getDrawing());
//			// this.add(drawPanel, BorderLayout.SOUTH);
//			showDrawPanel();
//			/*
//			 * if (mainScrollPaneIsVisible) { hideMainPanel();
//			 * this.add(drawPanel, BorderLayout.CENTER); showMainPanel(); } else
//			 * this.add(drawPanel, BorderLayout.SOUTH);
//			 */
//			// drawPanel.setBackground(Color.white);
//			// int drawHeight = drawPanel.getBounds().height;
//			VirtualDimension mySize = this.getSize();
//			if (mySize != null) {
//				// System.out.println(drawHeight);
//				this.setSize(Math.max(mySize.getWidth(), SLComposer.FRAME_WIDTH),
//						mySize.getHeight() + SLComposer.FRAME_HEIGHT);
//				// this.setSize(300, 300);
//				mySize = getSize();
//			}
//			ObjectAdapter topAdapter = getOriginalAdapter();
////			getDrawComponent().setBackground(AttributeNames.getDrawingPanelColor());
//			if (!isDummy())
//			getDrawComponent().setBackground(topAdapter.getDrawingPanelColor());	
//			else {
//				DelegateJPanel drawComponent = getDrawComponent();
//				VirtualContainer drawPanel = getDrawPanel();
//				drawComponent.setBackground((Color) drawPanel.getBackground());
//			}
//
//			// System.out.println(mySize);
//		} catch (Exception e) {
//			e.printStackTrace();
//			// System.out.println(e);
//		}

	}

	public void toolBar() {
		topViewManager.toolBar();
	}

	public void emptyMainPanel() {
		// topViewManager.emptyMainPanel();
		topViewManager.hideMainPanel();
	}

	public boolean isEmptyMainPanel() {
		// topViewManager.emptyMainPanel();
		return topViewManager.isEmptyMainPanel();
	}

	
	

	 
	// Panel windowHistoryPanel;
	VirtualContainer windowHistoryPanel;

	public void createWindowHistoryPanel() {
		// windowHistoryPanel = new Panel();
		windowHistoryPanel = PanelSelector.createPanel();
		windowHistoryPanel.setLayout(new BorderLayout());
		try {
			// bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel",
			// "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");
			// uiGenerator.generateUI(uigenPanel, new slm.SLModel());
			uiGenerator.generateInContainer(this, browser.getAdapterHistory(),
					windowHistoryPanel);
			// uiGenerator.generateUI(uigenPanel, new
			// Connections().getDrawing());
			this.add(windowHistoryPanel, BorderLayout.EAST);
			currentObjects.addElement(browser.getAdapterHistory());
			// uigenPanel.setBackground(Color.white);
			// int drawHeight = uigenPanel.getBounds().height;
			// VirtualDimension mySize = this.getSize();
			// System.out.println(drawHeight);
			// this.setSize(mySize.width, mySize.height*2);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void showDrawPanel() {
//		if (!isGlassPane())
		topViewManager.showDrawPanel();
	}

	public boolean drawPanelIsVisible() {
		return topViewManager.drawPanelIsVisible();
	}

	public void createTreePanel(ObjectAdapter adapter) {
		browser.createTreePanel((ObjectAdapter) adapter);
	}

	public void createTreePanel(Object obj) {
		browser.createTreePanel(obj);
	}

	public void createTreePanel() {
		browser.createTreePanel();
		// return jTreeAdapter.createTreePanel();
	}

	public void createTreePanel(Object obj, VirtualContainer treePanel) {
		browser.createTreePanel(obj, treePanel);
	}

	public VirtualContainer treeContainer() {
		return topViewManager.getTreeContainer();
	}

	public VirtualContainer treeComponent() {
		return treeView.getJTree();
	}

	public VirtualContainer newContainer(VirtualContainer scrolledComponent) {
		return topViewManager.newContainer(scrolledComponent);
	}

	public VirtualContainer newContainer(int direction,
			VirtualContainer scrolledComponent) {
		return topViewManager.newContainer(direction, scrolledComponent);
	}

	public VirtualContainer newTreeContainer() {
		return topViewManager.newTreeContainer();
	}

	

	public VirtualTree getJTree() {
		return treeView.getJTree();
	}

	public void setTreeRoot(ObjectAdapter newVal) {
		treeView.setTreeRoot(newVal);

	}

	/*
	 * public int subgetChildAdapterCount(Object parent){ return
	 * jTreeAdapter.subgetChildAdapterCount(parent); }
	 */
	public int subGetChildCount(Object parent) {
		return treeView.subGetChildCount(parent);
	}

	public void clearTreeSelection() {
		treeView.clearTreeSelection();
	}

	public void setJTreeSelectionPaths(TreePath[] selectedPaths) {
		treeView.setJTreeSelectionPaths(selectedPaths);
	}

	

	/*
	 * new browser
	 * 
	 * public void setOriginalAdapter(uiObjectAdapter newVal) {
	 * 
	 * origAdapter = newVal; maybeShowJTree(); //createTreePanel(newVal);
	 *  }
	 */

	public void maybeShowJTree() {
		ObjectAdapter adapter = browser.getOriginalAdapter();
		if (adapter != null
				&& adapter.getShowTree()
				&& (
				// adapter.getHeight() > DEEP_ELIDE_LEVEL + 1 ||
				new HasUncreatedChildrenVisitor(adapter).traverse().contains(
						Boolean.TRUE) || topViewManager.drawPanelIsVisible()))
			createTreePanel(adapter);
	}

	boolean manualTreeContainer = false;

	public void setTreeContainer(VirtualContainer c) {
		manualTreeContainer = true;
		topViewManager.setTreeContainer(c);
	}
	
	public VirtualContainer getTreeContainer() {
		return topViewManager.getTreeContainer();
	}
	
	boolean manualMainContainer = false;
	public void setManualMainContainer(boolean newVal) {
		manualMainContainer = true;
	}
	
	public boolean isManualMainContainer() {
		return manualMainContainer;
	}
	
	public boolean hasOnlyTreeManualContainer() {
		return isDummy() &&  
				getTreeContainer() != null && 
				getDrawPanel() == null;
	}

	// boolean isDesktop = false;
	// boolean initializedIsDesktop
	public boolean isDesktop() {

		return getFrame().getContentPane() instanceof VirtualDesktopPane;

	}

	public VirtualDesktopPane getDesktop() {
		if (getFrame() == null)
			return null;
		VirtualContainer container = getFrame().getContentPane();
		if (container instanceof VirtualDesktopPane)
			return (VirtualDesktopPane) container;
		else
			return null;
	}

	public boolean manualTreeContainer() {
		return manualTreeContainer;
	}

	boolean manualDrawContainer = false;
	boolean isRootDrawContainer = true;

	public void setDrawingContainer(VirtualContainer c) {
		setDrawingContainer(c, true);
	}

	public void setDrawingContainer(VirtualContainer c, boolean isRoot) {
		if (c == null) {
			Tracer.error("Null Draw Container");
			System.exit(-1);
		}
		manualDrawContainer = true;
		drawPanel = c;
		isRootDrawContainer = isRoot;
//		if (drawPanel != null)
//		if (isRoot && drawPanel != null)
		if (isRoot)	 // otherwise subpanels will be added, in either case probably should be broder layout
			drawPanel.setLayout(new BorderLayout()); 
		createDrawPanel();
		// topViewManager.setTreeContainer(c);
	}

	public boolean manualDrawContainer() {
		return manualDrawContainer;
	}

	VirtualMenu fileMenu;

	//
	// Create the menuBar ...have overloaded because may not wanna pass in a
	// MenuSetter object if not given in
	// the constructor that takes in just the object.
	//
	public VirtualMenuBar getMenuBar() {
		return menuBar;
	}

	void addMenus() {
		VirtualMenuItem item;
		// Create the file menu
		fileMenu = getMenu(AttributeNames.FILE_MENU);
		VirtualMenu newMenu = getMenu(AttributeNames.NEW_OBJECT_MENU_NAME);
		fileMenu.add(newMenu);
		menus.put(AttributeNames.NEW_OBJECT_MENU_NAME, newMenu);

		item = MenuItemSelector.createMenuItem(OPEN_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		fileMenu.add(item);

		item = MenuItemSelector.createMenuItem(SAVE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		saveItem = item;
		fileMenu.add(item);
		setSaveMenuItemEnabled(false);
		item = MenuItemSelector.createMenuItem(SAVE_AS_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		saveAsItem = item;
		fileMenu.add(item);

		item = MenuItemSelector.createMenuItem(SAVE_TEXT_AS_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		fileMenu.add(item);

		item = MenuItemSelector.createMenuItem(LOAD_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		fileMenu.add(item);

		item = MenuItemSelector.createMenuItem(UPDATE_ALL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		fileMenu.add(item);

		item = (new DoneMenuItem(uiFrame.EXIT_COMMAND)).getMenuItem();
		((DoneMenuItem) item).setUIFrame(this);
		item.addActionListener(this);
		fileMenu.add(item);
		exitItem = item;

		// Set up the Edit menu
		VirtualMenu editMenu = getMenu(uiFrame.EDIT_MENU);

		// item = MenuItemSelector.createMenuItem("Tool");
		// item.addActionListener(this);
		// editMenu.add(item);
		item = MenuItemSelector.createMenuItem(UNDO_COMMAND);
		item.addActionListener(this);
		editMenu.add(item);
		undoItem = item;
		undoHistoryEmpty(true);

		item = MenuItemSelector.createMenuItem(REDO_COMMAND);
		item.addActionListener(this);
		editMenu.add(item);
		redoItem = item;
		redoHistoryEmpty(true);

		editMenu.add("-");

		item = MenuItemSelector.createMenuItem(CUT_COMMAND);
		item.addActionListener(this);
		editMenu.add(item);
		cutItem = item;

		item = MenuItemSelector.createMenuItem(COPY_COMMAND);
		item.addActionListener(this);
		editMenu.add(item);
		copyItem = item;

		item = MenuItemSelector.createMenuItem(PASTE_COMMAND);
		item.addActionListener(this);
		editMenu.add(item);
		pasteItem = item;

		item = MenuItemSelector.createMenuItem(LINK_COMMAND);
		item.addActionListener(this);
		editMenu.add(item);
		linkItem = item;

		item = MenuItemSelector.createMenuItem(CLEAR_COMMAND);
		item.addActionListener(this);
		editMenu.add(item);

		editMenu.add("-");

		item = MenuItemSelector.createMenuItem(SELECT_UP_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		editMenu.add(item);

		item = MenuItemSelector.createMenuItem(SELECT_DOWN_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		editMenu.add(item);

		item = MenuItemSelector.createMenuItem(SELECT_ALL_COMMAND);
		item.addActionListener(this);
		// item.setEnabled(false);
		editMenu.add(item);
		editMenu.add("-");
		/*
		 * item = MenuItemSelector.createMenuItem(FORWARD_ADAPTER_NAME);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * editMenu.add(item); forwardItem = item;
		 * 
		 * item = MenuItemSelector.createMenuItem(BACK_ADAPTER_NAME);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this); backItem =
		 * item; editMenu.add(item);
		 */

		item = MenuItemSelector.createMenuItem(UPDATE_COMMAND);
		item.addActionListener(this);
		// item.setEnabled(false);
		editMenu.add(item);

		editMenu.add(MenuItemSelector.createMenuItem("-"));

		item = MenuItemSelector.createMenuItem(SETTINGS_COMMAND);
		item.addActionListener(this);
		// item.setEnabled(false);
		editMenu.add(item);

		editMenu
				.add(new SelectedMenuItem(SELECTION_COMMAND).getMenuItem());

		// View menu
		VirtualMenu viewMenu = getMenu(AttributeNames.VIEW_MENU);

		item = MenuItemSelector.createMenuItem(REFRESH_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(AUTO_REFRESH_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		// viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(AUTO_REFRESH_ALL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(INCREMENTAL_REFRESH_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		viewMenu.add("-");

		item = MenuItemSelector.createMenuItem(ELIDE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		/*
		 * item = MenuItemSelector.createMenuItem("Deep Elide");
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * viewMenu.add(item);
		 */

		/*
		 * item = MenuItemSelector.createMenuItem(DEEP_ELIDE_2);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * viewMenu.add(item);
		 */
		/*
		 * item = MenuItemSelector.createMenuItem(DEEP_ELIDE_3);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * viewMenu.add(item);
		 */

		item = MenuItemSelector.createMenuItem(ELIDE_HANDLE);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);

		item = MenuItemSelector.createMenuItem(DEEP_ELIDE_4);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		viewMenu.add(MenuItemSelector.createMenuItem("-"));
		item = MenuItemSelector.createMenuItem(TREE_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(DRAW_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(MAIN_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(SECONDARY_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(TOOLBAR_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(WINDOW_HISTORY_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		viewMenu.add(MenuItemSelector.createMenuItem("-"));
		item = MenuItemSelector.createMenuItem(NEW_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		viewMenu.add(item);
		/*
		 * item = MenuItemSelector.createMenuItem(NEW_TEXT_FRAME_COMMAND);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * viewMenu.add(item);
		 */
		item = MenuItemSelector.createMenuItem(NEW_TABLE_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		item = MenuItemSelector.createMenuItem(NEW_TAB_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);

		item = MenuItemSelector.createMenuItem(REPLACE_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);

		item = MenuItemSelector.createMenuItem(NEW_SCROLL_PANE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);

		item = MenuItemSelector.createMenuItem(NEW_SCROLL_PANE_BOTTOM_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		viewMenu.add(item);
		viewMenu.add(MenuItemSelector.createMenuItem("-"));

		item = MenuItemSelector.createMenuItem(FORWARD_ADAPTER_NAME);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		viewMenu.add(item);
		forwardItem = item;

		item = MenuItemSelector.createMenuItem(BACK_ADAPTER_NAME);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		backItem = item;
		viewMenu.add(item);

		// Attribute Menu
		// Menu attributeMenu = getMenu("Attributes");
		VirtualMenu attributeMenu = getMenu(CUSTOMIZE_MENU);
		item = MenuItemSelector.createMenuItem(SELECTED_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		attributeMenu.add(item);
		attributeMenu.add(MenuItemSelector.createMenuItem("-"));

		// Multicast Menu
		VirtualMenu multicastMenu = getMenu(CUSTOMIZE_MENU);
		item = MenuItemSelector.createMenuItem("Broadcast/Multicast");
		item.addActionListener(this);
		attributeMenu.add(item);
		attributeMenu.add(MenuItemSelector.createMenuItem("-"));

	}

	private void createMenuItemBar(boolean showMenus) {
		// MenuItem item;

		menuBar = MenuBarSelector.createMenuBar();

		if (showMenus)
			setMenuBar(menuBar);
		else if (!showMenus) { // if user wants all menus hidden then set the
								// menu for this frame to null and return to
			// avoid below processing.
			setMenuBar(null);
			return;
		}
		
		
	}

	boolean showMenu(Hashtable<String, Boolean> choices, String menuName) {

		return (choices.get(menuName) != null && choices.get(menuName));
	}

	AMenuGroup rootMenuGroup;

	public AMenuGroup getRootMenuGroup() {
		return rootMenuGroup;
	}

	boolean menuObjectsAdded = false;

	public void setMenuObjectsAdded(boolean newVal) {
		menuObjectsAdded = newVal;
	}

	public boolean getMenuObjectsAdded() {
		return menuObjectsAdded;
	}

	void addMenus(MenuSetter menuTest) {
		VirtualMenuItem item;
		// Create the file menu

		// boolean test = ((Boolean)( menuTest.get("File") ) ).booleanValue();

		// System.out.println("---"+test);

		// store whether File menu should be shown to test in checks for subitem
		// inclusion
		boolean showFile = ((Boolean) (menuTest.get("File"))).booleanValue();
		// boolean showFile = showMenu(menuTest, uiFrame.FILE_MENU_NAME);

		// System.out.println("showfile---"+showFile);

		// if ( ((Boolean)( menuTest.get("File") ) ).booleanValue() ) { //if the
		// menusetter object has "File" true at the high
		// level then go ahead and look within each option.

		fileMenu = (VirtualMenu) getMenu(AttributeNames.FILE_MENU, menuTest).getMenu();

		VirtualMenu newMenu = (VirtualMenu) getMenu(AttributeNames.NEW_OBJECT_MENU_NAME,
				menuTest).getMenu();

		// check here
		if ((showFile)
				&& ((Boolean) menuTest.get(AttributeNames.NEW_OBJECT_MENU_NAME))
						.booleanValue()) {

			fileMenu.add(newMenu);
			menus.put(AttributeNames.NEW_OBJECT_MENU_NAME, newMenu);

		}

		// if (((Boolean)menuTest.get(OPEN_FILE_COMMAND)).booleanValue()) {

		item = MenuItemSelector.createMenuItem(OPEN_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showFile)
				&& ((Boolean) menuTest.get(OPEN_FILE_COMMAND)).booleanValue()) {
			fileMenu.add(item);
		}

		// if (((Boolean)menuTest.get(SAVE_COMMAND)).booleanValue()) {

		item = MenuItemSelector.createMenuItem(SAVE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		saveItem = item;

		// check here
		if ((showFile) && ((Boolean) menuTest.get(SAVE_COMMAND)).booleanValue()) {
			fileMenu.add(item);
		}
		setSaveMenuItemEnabled(false);

		// if (((Boolean)menuTest.get(SAVE_AS_FILE_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(SAVE_AS_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		saveAsItem = item;
		// check here
		if ((showFile)
				&& ((Boolean) menuTest.get(SAVE_AS_FILE_COMMAND))
						.booleanValue()) {
			fileMenu.add(item);
		}

		// if
		// (((Boolean)menuTest.get(SAVE_TEXT_AS_FILE_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(SAVE_TEXT_AS_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		// check here
		if ((showFile)
				&& ((Boolean) menuTest.get(SAVE_TEXT_AS_FILE_COMMAND))
						.booleanValue()) {
			fileMenu.add(item);
		}

		// if (((Boolean)menuTest.get(LOAD_FILE_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(LOAD_FILE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		// check here
		if ((showFile)
				&& ((Boolean) menuTest.get(LOAD_FILE_COMMAND)).booleanValue()) {
			fileMenu.add(item);
		}

		// if (((Boolean)menuTest.get(UPDATE_ALL_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(UPDATE_ALL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showFile)
				&& ((Boolean) menuTest.get(UPDATE_ALL_COMMAND)).booleanValue()) {
			fileMenu.add(item);
		}

		// if (((Boolean)menuTest.get("Exit")).booleanValue()) {
		item = new DoneMenuItem(uiFrame.EXIT_COMMAND).getMenuItem();
		((DoneMenuItem) item).setUIFrame(this);
		item.addActionListener(this);
		// check here
		if ((showFile) && ((Boolean) menuTest.get(EXIT_COMMAND)).booleanValue()) {
			fileMenu.add(item);
		}
		exitItem = item;

		// END STUFF FOR FILE

		// Set up the Edit menu

		// store whether Edit menu should be shown to test in checks for subitem
		// inclusion
		boolean showEdit = ((Boolean) menuTest.get(uiFrame.EDIT_MENU))
				.booleanValue();

		// System.out.println("showedit---"+showEdit);
		// if (((Boolean)menuTest.get("Edit")).booleanValue()) { //if the
		// menusetter object has "Edit" true at the high
		// level then go ahead and look within each option.

		VirtualMenu editMenu = (VirtualMenu) getMenu(uiFrame.EDIT_MENU,
				menuTest).getMenu();

		// item = MenuItemSelector.createMenuItem("Tool");
		// item.addActionListener(this);
		// editMenu.add(item);
		item = MenuItemSelector.createMenuItem(UNDO_COMMAND);
		item.addActionListener(this);
		// check here
		if ((showEdit) && ((Boolean) menuTest.get(UNDO_COMMAND)).booleanValue()) {
			editMenu.add(item);
			undoItem = item;
			undoHistoryEmpty(true);
		}

		// if (((Boolean)menuTest.get(COPY_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(REDO_COMMAND);
		item.addActionListener(this);
		// check here
		if ((showEdit) && ((Boolean) menuTest.get(REDO_COMMAND)).booleanValue()) {
			editMenu.add(item);
			redoItem = item;
			redoHistoryEmpty(true);
			editMenu.add("-");
		}

		// if (((Boolean)menuTest.get(PASTE_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(CUT_COMMAND);
		item.addActionListener(this);
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(PASTE_COMMAND)).booleanValue()) {
			editMenu.add(item);
			cutItem = item;
		}
		item = MenuItemSelector.createMenuItem(COPY_COMMAND);
		item.addActionListener(this);
		// check here
		if ((showEdit) && ((Boolean) menuTest.get(COPY_COMMAND)).booleanValue()) {
			editMenu.add(item);
			copyItem = item;
		}

		// if (((Boolean)menuTest.get(PASTE_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(PASTE_COMMAND);
		item.addActionListener(this);
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(PASTE_COMMAND)).booleanValue()) {
			editMenu.add(item);
			pasteItem = item;
		}
		item = MenuItemSelector.createMenuItem(LINK_COMMAND);
		item.addActionListener(this);
		// check here
		if ((showEdit) && ((Boolean) menuTest.get(LINK_COMMAND)).booleanValue()) {
			editMenu.add(item);
			linkItem = item;
		}
		// if (((Boolean)menuTest.get(CLEAR_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(CLEAR_COMMAND);
		item.addActionListener(this);
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(CLEAR_COMMAND)).booleanValue()) {
			editMenu.add(item);

			editMenu.add("-");
		}
		// if (((Boolean)menuTest.get(SELECT_UP_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(SELECT_UP_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(SELECT_UP_COMMAND)).booleanValue()) {
			editMenu.add(item);
		}

		// if (((Boolean)menuTest.get("Select Down")).booleanValue()) {
		item = MenuItemSelector.createMenuItem(SELECT_DOWN_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(SELECT_DOWN_COMMAND)).booleanValue()) {
			editMenu.add(item);
		}

		// if (((Boolean)menuTest.get("Select All")).booleanValue()) {
		item = MenuItemSelector.createMenuItem(SELECT_ALL_COMMAND);
		item.addActionListener(this);
		// item.setEnabled(false);
		// check here
		if ((showEdit) && ((Boolean) menuTest.get("Select All")).booleanValue()) {
			editMenu.add(item);
			editMenu.add("-");
		}
		/*
		 * item = MenuItemSelector.createMenuItem(FORWARD_ADAPTER_NAME);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * editMenu.add(item); forwardItem = item;
		 * 
		 * item = MenuItemSelector.createMenuItem(BACK_ADAPTER_NAME);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this); backItem =
		 * item; editMenu.add(item);
		 */

		// if (((Boolean)menuTest.get(UPDATE_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(UPDATE_COMMAND);
		item.addActionListener(this);
		// item.setEnabled(false);
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(UPDATE_COMMAND)).booleanValue()) {
			editMenu.add(item);

			editMenu.add(MenuItemSelector.createMenuItem("-"));
		}

		// if (((Boolean)menuTest.get("Settings")).booleanValue()) {
		item = MenuItemSelector.createMenuItem(SETTINGS_COMMAND);
		item.addActionListener(this);
		// item.setEnabled(false);
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(SETTINGS_COMMAND)).booleanValue()) {
			editMenu.add(item);
		}

		// if (((Boolean)menuTest.get(SELECTION_COMMAND)).booleanValue()) {
		// check here
		if ((showEdit)
				&& ((Boolean) menuTest.get(SELECTION_COMMAND)).booleanValue()) {
			// editMenu.add(new uiGenSelectedMenuItem(SELECTION_COMMAND));
			editMenu.add(MenuItemSelector.createMenuItem(SELECTION_COMMAND));
		}

		// View menu
		// store whether Edit menu should be shown to test in checks for subitem
		// inclusion
		boolean showView = ((Boolean) menuTest.get(AttributeNames.VIEW_MENU))
				.booleanValue();

		// System.out.println("showview---"+showView);

		// if (((Boolean)menuTest.get("View")).booleanValue()) { //if the
		// menusetter object has "View" true at the high
		// level then go ahead and look within each option.

		VirtualMenu viewMenu = (VirtualMenu) getMenu(AttributeNames.VIEW_MENU, menuTest)
				.getMenu();

		// if (((Boolean)menuTest.get(REFRESH_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(REFRESH_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(REFRESH_COMMAND)).booleanValue()) {
			viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(AUTO_REFRESH_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(AUTO_REFRESH_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(AUTO_REFRESH_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
			// viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(AUTO_REFRESH_ALL_COMMAND)).booleanValue())
		// {

		item = MenuItemSelector.createMenuItem(AUTO_REFRESH_ALL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(AUTO_REFRESH_ALL_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
			// viewMenu.add("-");
		}
		item = MenuItemSelector.createMenuItem(INCREMENTAL_REFRESH_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(INCREMENTAL_REFRESH_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
			viewMenu.add("-");
		}

		// if (((Boolean)menuTest.get(ELIDE_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(ELIDE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(ELIDE_COMMAND)).booleanValue()) {
			viewMenu.add(item);
			/*
			 * item = MenuItemSelector.createMenuItem("Deep Elide");
			 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, //
			 * Event.CTRL_MASK, // false)); item.addActionListener(this);
			 * viewMenu.add(item);
			 */
		}
		/*
		 * //if (((Boolean)menuTest.get(DEEP_ELIDE_2)).booleanValue()) { item =
		 * MenuItemSelector.createMenuItem(DEEP_ELIDE_2);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * 
		 * //check here if ( (showView) &&
		 * ((Boolean)menuTest.get(DEEP_ELIDE_2)).booleanValue() ) {
		 * viewMenu.add(item); }
		 */
		/*
		 * 
		 * //if (((Boolean)menuTest.get(DEEP_ELIDE_3)).booleanValue()) { item =
		 * MenuItemSelector.createMenuItem(DEEP_ELIDE_3);
		 * 
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this);
		 * 
		 * //check here if ( (showView) &&
		 * ((Boolean)menuTest.get(DEEP_ELIDE_3)).booleanValue() ) {
		 * viewMenu.add(item); }
		 */
		// if (((Boolean)menuTest.get(DEEP_ELIDE_3)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(ELIDE_HANDLE);

		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		// check here
		if ((showView) && ((Boolean) menuTest.get(ELIDE_HANDLE)).booleanValue()) {
			viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(DEEP_ELIDE_4)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(DEEP_ELIDE_4);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		// check here
		if ((showView) && ((Boolean) menuTest.get(DEEP_ELIDE_4)).booleanValue()) {
			viewMenu.add(item);
			viewMenu.add(MenuItemSelector.createMenuItem("-"));
		}

		// if (((Boolean)menuTest.get(TREE_PANEL_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(TREE_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(TREE_PANEL_COMMAND)).booleanValue()) {
			viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(DRAW_PANEL_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(DRAW_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(DRAW_PANEL_COMMAND)).booleanValue()) {
			viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(MAIN_PANEL_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(MAIN_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);

		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(MAIN_PANEL_COMMAND)).booleanValue()) {
			viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(TOOLBAR_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(TOOLBAR_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(TOOLBAR_COMMAND)).booleanValue()) {
			viewMenu.add(item);
		}

		// if
		// (((Boolean)menuTest.get(WINDOW_HISTORY_PANEL_COMMAND)).booleanValue())
		// {
		item = MenuItemSelector.createMenuItem(WINDOW_HISTORY_PANEL_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(WINDOW_HISTORY_PANEL_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
			viewMenu.add(MenuItemSelector.createMenuItem("-"));
		}

		// if (((Boolean)menuTest.get(NEW_FRAME_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(NEW_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(NEW_FRAME_COMMAND)).booleanValue()) {
			viewMenu.add(item);
		}
		/*
		 * //if (((Boolean)menuTest.get(NEW_TEXT_FRAME_COMMAND)).booleanValue()) {
		 * item = MenuItemSelector.createMenuItem(NEW_TEXT_FRAME_COMMAND);
		 * //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, //
		 * Event.CTRL_MASK, // false)); item.addActionListener(this); //check
		 * here if ( (showView) &&
		 * ((Boolean)menuTest.get(NEW_TEXT_FRAME_COMMAND)).booleanValue() ) {
		 * viewMenu.add(item); }
		 */
		// if (((Boolean)menuTest.get(NEW_TEXT_FRAME_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(NEW_TABLE_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(NEW_TABLE_FRAME_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
		}
		// if (((Boolean)menuTest.get(NEW_TEXT_FRAME_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(NEW_TAB_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(NEW_TAB_FRAME_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(REPLACE_FRAME_COMMAND)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(REPLACE_FRAME_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(REPLACE_FRAME_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
		}

		// if (((Boolean)menuTest.get(NEW_SCROLL_PANE_COMMAND)).booleanValue())
		// {
		item = MenuItemSelector.createMenuItem(NEW_SCROLL_PANE_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(NEW_SCROLL_PANE_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
		}

		// if
		// (((Boolean)menuTest.get(NEW_SCROLL_PANE_BOTTOM_COMMAND)).booleanValue())
		// {
		item = MenuItemSelector.createMenuItem(NEW_SCROLL_PANE_BOTTOM_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(NEW_SCROLL_PANE_BOTTOM_COMMAND))
						.booleanValue()) {
			viewMenu.add(item);
			viewMenu.add(MenuItemSelector.createMenuItem("-"));
		}

		// if (((Boolean)menuTest.get(FORWARD_ADAPTER_NAME)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(FORWARD_ADAPTER_NAME);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(FORWARD_ADAPTER_NAME))
						.booleanValue()) {
			viewMenu.add(item);
		}
		forwardItem = item;

		// if (((Boolean)menuTest.get(BACK_ADAPTER_NAME)).booleanValue()) {
		item = MenuItemSelector.createMenuItem(BACK_ADAPTER_NAME);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		backItem = item;
		// check here
		if ((showView)
				&& ((Boolean) menuTest.get(BACK_ADAPTER_NAME)).booleanValue()) {
			viewMenu.add(item);
		}

		// Attribute Menu

		// store whether Edit menu should be shown to test in checks for subitem
		// inclusion
		boolean showCustomize = ((Boolean) menuTest.get(CUSTOMIZE_MENU))
				.booleanValue();

		// System.out.println("showcustomize---"+showCustomize);
		// if (((Boolean)menuTest.get(ATTRIBUTES_MENU_NAME)).booleanValue()) {
		// //if the menusetter object has ATTRIBUTES_MENU_NAME aka "Customize"
		// true at the high
		// level then go ahead and look within each option.

		// Menu attributeMenu = getMenu("Attributes");
		// may only be able to eliminate customize all or none...may be bean
		// too.

		VirtualMenu attributeMenu = (VirtualMenu) getMenu(CUSTOMIZE_MENU,
				menuTest).getMenu();

		// if (((Boolean)menuTest.get("Selected")).booleanValue()) {
		item = MenuItemSelector.createMenuItem(SELECTED_COMMAND);
		// item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
		// Event.CTRL_MASK,
		// false));
		item.addActionListener(this);
		// check here
		if ((showCustomize)
				&& ((Boolean) menuTest.get(SELECTED_COMMAND)).booleanValue()) {
			attributeMenu.add(item);
			attributeMenu.add(MenuItemSelector.createMenuItem("-"));
		}

		VirtualMenu multicastMenu = (VirtualMenu) getMenu(CUSTOMIZE_MENU,
				menuTest).getMenu();

		item = MenuItemSelector.createMenuItem("Broadcast/Multicast");
		item.addActionListener(this);
		// check here
		if ((showCustomize)
				&& ((Boolean) menuTest.get("Broadcast/Multicast"))
						.booleanValue()) {
			attributeMenu.add(item);
			attributeMenu.add(MenuItemSelector.createMenuItem("-"));
		}

	}

	private void createMenuItemBar(boolean showMenus, MenuSetter menuTest) {
		// MenuItem item;

		menuBar = MenuBarSelector.createMenuBar();
		rootMenuGroup = new AMenuGroup("", menuBar, menuDescriptor);

		// System.out.println("---test");
		// System.exit(0);

		if (showMenus)
			setMenuBar(menuBar);
		else if (!showMenus) { // if user wants all menus hidden then set the
								// menu for this frame to null and return to
			// avoid below processing.
			setMenuBar(null);
			return;
		}
		

	}

	
	public void useManualToolbar(boolean newVal) {
		toolbarManager.useManualToolbar(newVal);
	}

	public VirtualContainer createManualToolbar() {
		return toolbarManager.createManualToolbar();
		

	}

	public int toolBarCount() {
		return toolbarManager.toolbarCount();
	}

	// commenting out panel based toolbar
	public void createToolBar() {
		toolbarManager.createToolBar();
		

	}

	public void hideToolBar() {
		topViewManager.hideToolBar();
	}

	public void showToolBar() {
		topViewManager.showToolBar();
	}

	public void setTreePanelIsVisible(boolean newVal) {
		topViewManager.setTreePanelIsVisible(newVal);
	}

	public void showTreePanel() {
//		topViewManager.showTreePanel();
		getTreeMenuObject().tree();
	}

	/*
	 * public void setSecondaryScrollPaneIsVisible (boolean newVal) {
	 * topViewManager.setSecondaryScrollPaneIsVisible(newVal); }
	 */
	boolean isOnlyGraphicsPanel = false;
	public void setOnlyGraphicsPanel(boolean newVal) {
		isOnlyGraphicsPanel = newVal;
	}
	public boolean isOnlyGraphicsPanel() {
		return isOnlyGraphicsPanel;
	}
	
	boolean checkPreOnImplicitRefresh = true;
	public boolean checkPreOnImplicitRefresh() {
		return checkPreOnImplicitRefresh;
	}
	
	public void setCheckPreOnImplicitRefresh(boolean newValue) {
		checkPreOnImplicitRefresh = newValue;
	}
	public void showMainPanel() {
		if (topViewManager != null)
			topViewManager.showMainPanel();
		// dynamic components do not get added to a vector for some reason
//		if (getFrame().isVisible()) { // if not visible, then initial content has not changed, ne cest pas?
//		System.out.println("Not refreshing");
//		refreshOperationsModel.refresh();
		
	}
	public void showMainPanelWithoutRefreshing() {
		if (topViewManager != null)
			topViewManager.showMainPanel();
//		refreshOperationsModel.refresh();
	}

	public void hideMainPanel() {
		topViewManager.hideMainPanel();
	}

	public boolean mainPanelIsVisible() {
		return topViewManager.mainPanelIsVisible();
	}

	public void showSecondaryScrollPane() {
		// if (!secondaryScrollPaneIsVisible())
		topViewManager.showSecondaryScrollPane();
	}

	public void hideSecondaryScrollPane() {
		// if (secondaryScrollPaneIsVisible())
		topViewManager.hideSecondaryScrollPane();
	}

	public boolean secondaryScrollPaneIsVisible() {
		return topViewManager.secondaryScrollPaneIsVisible();
	}

	
	public void addToMiddlePanel(VirtualContainer toAdd) {

		toolbarManager.addToMiddlePanel(toAdd);

	}

	

	public void setDrawingAdapter(ObjectAdapter newVal) {
		drawingAdapter = newVal;
	}

	Object topObject;
	String methodsMenuName;
	

	boolean exitEnabled = true;
	@Override
	public void setAutoExitEnabled(boolean newVal) {
		exitEnabled = newVal;
	}
	@Override
	public boolean getAutoExitEnabled() {
		return exitEnabled;
	}

	boolean myMode;

	boolean animationMode = false;
	ABoundedBuffer methodInvocationBuffer = new ABoundedBuffer();
	MethodInvocationRunnable methodInvocationRunnable = new MethodInvocationRunnable(
			methodInvocationBuffer, this);
	public static int numMethodInvocationThreads = 2;
	Thread[] methodInvocationThreads;

	// Thread methodInvocationThread = new Thread(methodInvocationRunnable);
	// Thread methodInvocationThread2 = new Thread(methodInvocationRunnable);
	public boolean getAnimationMode() {
		return animationMode;
	}

	boolean threadsStarted = false;
	static boolean threadsAllowed = true;

	public boolean createMethodInvocationThreads() {
		if (threadsAllowed && !threadsStarted) {
			methodInvocationThreads = new Thread[numMethodInvocationThreads];
			for (int i = 0; i < methodInvocationThreads.length; i++) {
				methodInvocationThreads[i] = new Thread(
						methodInvocationRunnable);
				methodInvocationThreads[i].setName("Method Invocation:" + i);
				methodInvocationThreads[i].start();
				// methodInvocationThread.start();
				// methodInvocationThread2.start();
				threadsStarted = numMethodInvocationThreads > 0;
			}
		}
		return threadsStarted;
	}

	public ABoundedBuffer getMethodInvocationBuffer() {
		return methodInvocationBuffer;
	}

	public void doUpdate(ObjectAdapter adapter) {
		(new UpdateAdapterVisitor(adapter)).traverse();
		// adapter.uiComponentValueChanged();
		this.doImplicitRefresh();
	}

	public void doUpdateAll() {
		// for some reason this was commented out
		// subDoUpdateAll();
		if (!ObjectEditor.shareBeans()) {
			subDoUpdateAll();
		} else {
			ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this,
					"doUpdateAll"));
		}

	}

	public void subDoUpdateAll() {
		Vector results = new Vector();
		;
		for (Enumeration adapters = browser.getAdapterHistory().elements(); adapters
				.hasMoreElements();)
			results = (new UpdateAdapterVisitor(((ObjectAdapter) adapters
					.nextElement()))).traverse();

		// setEdited(false);
		// doUpdate(getOriginalAdapter());
		// (new UpdateAdapterVisitor(getOriginalAdapter())).traverse();
		// System.out.println("Not doing implicit refresh on update");
		if (results.size() > 0)
			this.doImplicitRefresh();
	}

	static final int DEEP_ELIDE_NUM = 200;

	public void elideHandle(ObjectAdapter o) {
		WidgetShell gw = o.getGenericWidget();
		if (gw != null)
			gw.elideHandle();
	}

	public void elideHandle(Vector adapters) {
		for (int i = 0; i < adapters.size(); i++) {
			elideHandle((ObjectAdapter) adapters.elementAt(i));
		}
	}

	public void deepElide(ObjectAdapter adapter, int level) {
		if (adapter == null)
			return;
		if (!ObjectEditor.shareBeans()) {
			subDeepElide(adapter, level);
		} else {
			if (ObjectEditor.coupleElides) {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this,
						adapter, level));
			} else {
				if (ObjectEditor.colabMode())
					ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(
							this, adapter.getPath(), level));
			}
		}
	}

	public void toggleElide(ObjectAdapter adapter, int level) {
		if (adapter == null)
			return;
		if (!ObjectEditor.shareBeans()) {
			subToggleElide(adapter, level);
		} else {
			if (ObjectEditor.coupleElides) {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this,
						adapter, level));
			} else {
				if (ObjectEditor.colabMode())
					ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(
							this, adapter.getPath(), level));
			}
		}
	}

	public void internalElide(ObjectAdapter adapter, int level) {
		if (adapter == null)
			return;
		if (!ObjectEditor.shareBeans()) {
			subInternalElide(adapter, level);
		} else {
			if (ObjectEditor.coupleElides) {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this,
						adapter, level));
			} else {
				if (ObjectEditor.colabMode())
					ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(
							this, adapter.getPath(), level));
			}
		}
	}

	public void internalElideTopChildren(ObjectAdapter adapter) {
		internalElide(adapter, 2);
	}

	public void internalElideTopChildren() {
		internalElideTopChildren(getAdapter());
	}

	public void toggleElide(Vector adapters) {
		for (int i = 0; i < adapters.size(); i++) {
			toggleElide((ObjectAdapter) adapters.elementAt(i));
		}

	}

	public void toggleElide(ObjectAdapter adapter) {
		if (adapter == null)
			return;
		if (!ObjectEditor.shareBeans()) {
			subToggleElide(adapter);
		} else {
			if (ObjectEditor.coupleElides) {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this,
						adapter, 1));
			} else {
				if (ObjectEditor.colabMode())
					ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(
							this, adapter.getPath(), 1));
			}
		}
	}

	public void subToggleElide(ObjectAdapter adapter) {
		WidgetShell gw = adapter.getGenericWidget();
		if (gw != null)
			gw.toggleElide();
		this.validate();
	}

	public void subDeepElide(ObjectAdapter adapter, int level) {
		if (!browser.getCurrentAdapters().contains(adapter.getTopAdapter()))
			adapter = browser.getDefaultAdapter();
		// System.out.println("deep elide" + adapter);
		(new ElideAdapterVisitor(adapter)).traverse(level);
		// this.doLayout();
		// this.pack();
		this.validate();
	}

	public void subToggleElide(ObjectAdapter adapter, int level) {
		if (!browser.getCurrentAdapters().contains(adapter.getTopAdapter()))
			adapter = browser.getDefaultAdapter();
		// System.out.println("deep elide" + adapter);
		// (new ToggleElideAdapterVisitor (adapter)).traverse(level);
		(new ToggleElideAdapterVisitor(adapter)).visitContainersAt(level);
		// this.doLayout();
		// this.pack();
		this.validate();
	}

	public void subInternalElide(ObjectAdapter adapter, int level) {
		if (!browser.getCurrentAdapters().contains(adapter.getTopAdapter()))
			adapter = browser.getDefaultAdapter();
		// System.out.println("deep elide" + adapter);
		// (new ToggleElideAdapterVisitor (adapter)).traverse(level);
		(new ElideWithoutHandleAdapterVisitor(adapter))
				.visitContainersAt(level);
		// this.doLayout();
		// this.pack();
		this.validate();
	}

	
	public ObjectAdapter browsee(ObjectAdapter adapter) {
		return browser.browsee(adapter);
	}

	public void replaceFrame(Object obj) {
		browser.replaceFrame((ObjectAdapter) null, obj, (String) null);
	}

	public void replaceFrame(Object obj, String title) {
		browser.replaceFrame(null, obj, title);
	}

	public void replaceFrame(ObjectAdapter adapter) {
		browser.replaceFrame(adapter, (Object) null, (String) null);
	}

	public boolean getChoice(String s) {
		return (JOptionPane.showConfirmDialog(null, s, "Ok",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}

	public void showMessage(String s) {
		JOptionPane.showMessageDialog(null, s, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public boolean checkWithUser(String problem, String message) {
		return JOptionPane.showConfirmDialog(null, message, problem,
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	

	// public void processMethod (Method method) {
	public Vector targetObjects(MethodProxy method) {
		// print(currentAdapters);
		// Enumeration adapters = currentAdapters.elements();
		Enumeration adapters = browser.getAdapterHistory().elements();
		boolean foundMultipleObjects = false;
		Vector targetObjects = new Vector();
		ClassProxy c;
		ObjectAdapter adapter;

		while (adapters.hasMoreElements()) {
			adapter = ((ObjectAdapter) adapters.nextElement());
			/*
			 * c = adapter.getPropertyClass(); primitiveClassList list = new
			 * primitiveClassList(); Object object; if
			 * (list.isPrimitiveClass(c.getName())) { object
			 * =adapter.getValue(); //System.out.println("primitive class");
			 *  } else object = adapter.getRealObject();
			 */
			Object object = adapter.getValueOrRealObject();
			// System.out.println("trying adapter:" + adapter + "object" +
			// object);
			if (object != null
					&& method.getDeclaringClass().isAssignableFrom(
							ACompositeLoggable.getTargetClass(object))) {
				// System.out.println("successful: " +
				// method.getDeclaringClass() + object.getClass());
				/*
				 * if (targetObject != null && targetObject != object) {
				 * foundMultipleObjects = true; break; } else targetObject =
				 * object;
				 */
				targetObjects.addElement(object);
			}
		}
		Object object;
		for (Enumeration elements = currentObjects.elements(); elements
				.hasMoreElements();) {
			object = elements.nextElement();
			c = ACompositeLoggable.getTargetClass(object);
			// System.out.println("trying adapter:" + object);
			if (method.getDeclaringClass().isAssignableFrom(c)) {
				/*
				 * // System.out.println("successful: " +
				 * method.getDeclaringClass() + object.getClass()); if
				 * (targetObject != null && targetObject != object) {
				 * foundMultipleObjects = true; break; } else targetObject =
				 * object;
				 */
				targetObjects.addElement(object);
			}
		}
		return targetObjects;
		/*
		 * 
		 * if (targetObjects.size() == 0) showMessage("Command cannot be invoked
		 * on any windowed object"); else if (targetObjects.size() == 1) new
		 * uiMethodInvocationManager(this, targetObjects.elementAt(0), method);
		 * else if (this.checkWithUser("Ambiguous command invocation", "Invoke
		 * command on all valid objects: " + targetObjects.toString())) for (int
		 * i = 0; i < targetObjects.size(); i++) new
		 * uiMethodInvocationManager(this, targetObjects.elementAt(i), method);
		 * else showMessage("Ambiguous command invocation: Command not
		 * executed");
		 */
	}

	public Vector predefinedTargetObjects(MethodProxy method) {
		// print(currentAdapters);
		// Enumeration adapters = currentAdapters.elements();
		Enumeration adapters = browser.getAdapterHistory().elements();
		boolean foundMultipleObjects = false;
		Vector targetObjects = new Vector();
		ClassProxy c;
		ObjectAdapter adapter;

		Object object;
		for (Enumeration elements = currentObjects.elements(); elements
				.hasMoreElements();) {
			object = elements.nextElement();
			c = ACompositeLoggable.getTargetClass(object);
			// System.out.println("trying adapter:" + object);
			if (method.getDeclaringClass().isAssignableFrom(c)) {
				/*
				 * // System.out.println("successful: " +
				 * method.getDeclaringClass() + object.getClass()); if
				 * (targetObject != null && targetObject != object) {
				 * foundMultipleObjects = true; break; } else targetObject =
				 * object;
				 */
				targetObjects.addElement(object);
			}
		}
		return targetObjects;
		/*
		 * 
		 * if (targetObjects.size() == 0) showMessage("Command cannot be invoked
		 * on any windowed object"); else if (targetObjects.size() == 1) new
		 * uiMethodInvocationManager(this, targetObjects.elementAt(0), method);
		 * else if (this.checkWithUser("Ambiguous command invocation", "Invoke
		 * command on all valid objects: " + targetObjects.toString())) for (int
		 * i = 0; i < targetObjects.size(); i++) new
		 * uiMethodInvocationManager(this, targetObjects.elementAt(i), method);
		 * else showMessage("Ambiguous command invocation: Command not
		 * executed");
		 */
	}

	Object targetObject(ObjectAdapter adapter) {
		return adapter.getValueOrRealObject();
		/*
		 * Class c = adapter.getPropertyClass(); primitiveClassList list = new
		 * primitiveClassList(); Object object; if
		 * (list.isPrimitiveClass(c.getName())) { object =adapter.getValue();
		 * //System.out.println("primitive class");
		 *  } else object = adapter.getRealObject(); return object;
		 */
	}

	public void maybeAddTargetAdapter(Vector targetAdapters,
			ObjectAdapter adapter, MethodProxy method) {
		if (adapter == null)
			return;
		Object object = adapter.getValueOrRealObject();
		// System.out.println("trying adapter:" + adapter + "object" + object);
		if (object != null
				&& method.getDeclaringClass().isAssignableFrom(
						ACompositeLoggable.getTargetClass(object))) {
			// System.out.println("successful: " + method.getDeclaringClass() +
			// object.getClass());
			/*
			 * if (targetObject != null && targetObject != object) {
			 * foundMultipleObjects = true; break; } else targetObject = object;
			 */
			targetAdapters.addElement(adapter);
		}
	}

	public Vector targetAdapters(MethodProxy method) {
		// print(currentAdapters);
		// Enumeration adapters = browser.getCurrentAdapters().elements();
		// probably should be an if here with a parameter saying should all
		// adapters be chosen
		// Enumeration adapters = adapterHistory.elements();
		boolean foundMultipleObjects = false;
		Vector targetAdapters = new Vector();
		ClassProxy c;
		ObjectAdapter adapter;
		Enumeration adapters = browser.getCurrentAdapters().elements();
		while (adapters.hasMoreElements()) {
			adapter = ((ObjectAdapter) adapters.nextElement());
			
			maybeAddTargetAdapter(targetAdapters, adapter, method);
			
		}

		adapters = getUserAdapters().elements();
		while (adapters.hasMoreElements()) {
			adapter = ((ObjectAdapter) adapters.nextElement());
			// Object object;

			maybeAddTargetAdapter(targetAdapters, adapter, method);

		}

		maybeAddTargetAdapter(targetAdapters, this.getTopTreeAdapter(), method);
		maybeAddTargetAdapter(targetAdapters, this.getSecondaryObjectAdapter(),
				method);

		return targetAdapters;
		
	}

	
	public static String toStringVector(Vector list, String prefix,
			String separator, String suffix) {
		String retVal = "";
		for (int i = 0; i < list.size(); i++) {
			String title = list.elementAt(i).toString();
			if (i == 0)
				retVal += prefix + title + suffix;
			else
				retVal += separator + prefix + title + suffix;
		}
		return retVal;
	}

	public static String toStringVector(Vector list) {
		return toStringVector(list, "", ", ", "");
	}

	public boolean checkWithUserAll(Vector allValidAdapters) {
		if (allValidAdapters.size() <= 1)
			return true;
		return checkWithUser("Ambiguous command invocation",
				"Invoke command on all valid   objects: "
						+ browser.toStringAdapterList(allValidAdapters, "",
								", ", ""));
	}

	public boolean checkWithUserSelected() {
		return checkWithUser("Ambiguous command invocation",
				"Invoke command on selected valid objects: ");
	}

	public boolean maybeExecuteOnSelection(Vector allValidAdapters,
			Vector targetAdaptersList, Vector<MethodProxy> methods) {
		ObjectAdapter selection = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		if (selection == null)
			return false;
		if (!allValidAdapters.contains(selection))
			return false;
		if (checkWithUser("Ambiguous command invocation",
				"Invoke command  on selection: " + selection.getFrameTitle())) {
			int i = allValidAdapters.indexOf(selection);
			new MethodInvocationManager(this, targetObject(selection),
					(MethodProxy) methods.elementAt(rowOf(selection,
							targetAdaptersList)));
			return true;
		}
		return false;
	}

	public boolean maybeExecuteOnSelections(Vector allValidAdapters,
			Vector targetAdaptersList, Vector methods) {

		Vector selections = SelectionManager.getSelections();
		if (selections.size() == 0)
			return false;
		if (selections.size() == 1
				|| checkWithUser("Ambiguous command invocation",
						"Invoke command  on valid selections: "
								+ browser.toStringAdapterList(selections, "",
										", ", ""))) {
			return executeMethodsOnSameArguments(selections,
					targetAdaptersList, methods);

		}
		return false;
	}

	int rowOf(Object obj, Vector vectorList) {
		for (int i = 0; i < vectorList.size(); i++) {
			if (((Vector) vectorList.elementAt(i)).contains(obj))
				return i;
		}
		System.out
				.println("Internal Error***   did not find object in vector list");
		return -1;
	}

	public boolean maybeExecuteOnCurrentAdapter(Vector allValidAdapters,
			Vector targetAdaptersList, Vector<MethodProxy> methods) {
		if (browser.getCurrentAdapters().size() == 0)
			return false;
		Vector currentValidAdapters = new Vector();
		for (int i = 0; i < browser.getCurrentAdapters().size(); i++) {
			Object currentAdapter = browser.getCurrentAdapters().elementAt(i);
			if (allValidAdapters.contains(currentAdapter))
				currentValidAdapters.addElement(currentAdapter);
		}
		if (currentValidAdapters.size() == 1) {
			ObjectAdapter currentAdapter = (ObjectAdapter) currentValidAdapters
					.elementAt(0);

			if (checkWithUser("Ambiguous command invocation",
					"Invoke command  on visible object: "
							+ currentAdapter.getFrameTitle())) {
				// int i = allValidAdapters.indexOf(currentAdapter);
				new MethodInvocationManager(this,
						targetObject(currentAdapter), (MethodProxy) methods
								.elementAt(rowOf(currentAdapter,
										targetAdaptersList)));
				return true;
			}
		}
		return false;
	}

	public void processMethod(MethodProxy method) {
		Vector<MethodProxy> methods = new Vector();
		methods.addElement(method);
		processMethods(null, methods);
	}

	Vector validAdapters;
	Vector targetAdaptersList;

	public void run() {
		// Vector validAdapterNames = new Vector();
		/*
		 * for (int i = 0; i < myValidAdapters.size(); i++)
		 * validAdapterNames.addElement(((uiObjectAdapter)
		 * validAdapters.elementAt(i)).getFrameTitle());
		 */
		AnOldCheckedVector checkedVector = (AnOldCheckedVector) ObjectEditor
				.syncEdit(new AnOldCheckedVector(validAdapters));
		// Vector retVal = (Vector) ObjectEditor.syncEdit(validAdapterNames);
		executeMethodsOnSameArguments(checkedVector.checkedElements(),
				targetAdaptersList, methods);
		// System.out.println(checkedVector.trueElementsInPlace());
	}

	public void executeMethods(Vector validAdapters, Vector targetAdaptersList,
			Vector<MethodProxy> methods) {
		Vector retVals = new Vector();
		Object[] parameterValues = null;
		for (int i = 0; i < targetAdaptersList.size(); i++) {
			Vector targetAdapters = (Vector) targetAdaptersList.elementAt(i);
			for (int j = 0; j < targetAdapters.size(); j++) {
				ObjectAdapter targetAdapter = (ObjectAdapter) targetAdapters
						.elementAt(j);
				if (!validAdapters.contains(targetAdapter))
					continue;
				Object targetObject = targetObject(targetAdapter);
				if (targetObject != null) {
					MethodInvocationManager mi = new MethodInvocationManager(
							this, targetObject, (MethodProxy) methods
									.elementAt(i), false);
					Object retVal = mi.getResult();
					if (retVal != null)
						retVals.addElement(retVal);
				}
			}
		}
		if (retVals.size() != 0)
			ObjectEditor.edit(retVals);
	}

	public Object executeMethod(Object targetObject, MethodProxy method,
			Object[] parameterValues) {
		MethodInvocationManager mi;
		if (parameterValues == null) {
			mi = new MethodInvocationManager(this, targetObject, method,
					false);
			parameterValues = mi.getParameters();
		} else
			mi = new MethodInvocationManager(this, targetObject, method,
					parameterValues, false);
		return mi.getResult();
	}

	
	public boolean executeMethodsOnSameArguments(Vector validAdapters,
			Vector targetAdaptersList, Vector methods) {
		Hashtable objectMethods = new Hashtable();
		for (int i = 0; i < targetAdaptersList.size(); i++) {
			Vector targetAdapters = (Vector) targetAdaptersList.elementAt(i);
			for (int j = 0; j < targetAdapters.size(); j++) {
				ObjectAdapter targetAdapter = (ObjectAdapter) targetAdapters
						.elementAt(j);
				if (!validAdapters.contains(targetAdapter))
					continue;
				Object targetObject = targetObject(targetAdapter);
				if (targetObject != null) {
					objectMethods.put(targetObject, methods.elementAt(i));
				}
			}
		}
		if (objectMethods.size() == 0)
			return false;
		else {
			new MethodInvocationManager(this, null, null, objectMethods);
			return true;
		}
	}

	public static void show(Vector retVals) {
		if (retVals.size() == 0)
			return;
		if (retVals.size() == 1)
			ObjectEditor.edit(retVals.elementAt(0));
		else
			ObjectEditor.edit(retVals);
	}

	void addValidAdapter(Vector validAdapters, ObjectAdapter adapter) {
		for (int i = 0; i < validAdapters.size(); i++) {
			if (targetObject(adapter) == targetObject(((ObjectAdapter) validAdapters
					.elementAt(i))))
				;
			return;
		}
		validAdapters.addElement(adapter);
	}

	public void processMethod(Object targetObject, MethodProxy method) {
		if (targetObject == null) {
			Vector<MethodProxy> methods = new Vector();
			methods.addElement(method);
			processMethods(targetObject, methods);
			return;
		}
		Hashtable<Object, MethodProxy> objectMethods = new Hashtable();
		objectMethods.put(targetObject, method);
		new MethodInvocationManager(this, null, null, objectMethods);

	}

	public void processMethods(Object object, Vector<MethodProxy> methods) {
		if (object != null && methods.size() == 1) {
			processMethod(object, methods.elementAt(0));
			return;
		}
		Vector targetAdaptersList = new Vector();
		Vector allValidAdapters = new Vector();
		int size = 0;
		for (int i = 0; i < methods.size(); i++) {
			Vector targetAdapters = targetAdapters((MethodProxy) methods
					.elementAt(i));
			for (int j = 0; j < targetAdapters.size(); j++)
				// allValidAdapters.addElement(targetAdapters.elementAt(j));
				addValidAdapter(allValidAdapters,
						(ObjectAdapter) targetAdapters.elementAt(j));
			size += targetAdapters.size();
			targetAdaptersList.addElement(targetAdapters);

		}
		if (allValidAdapters.size() == 0) {
			// showMessage("Command cannot be invoked on any windowed object");
			processMethodsOnPredefinedObjects(methods);
			return;
		} else if (allValidAdapters.size() > 1)
			if (maybeExecuteOnSelections(allValidAdapters, targetAdaptersList,
					methods))
				return;
			else if (maybeExecuteOnCurrentAdapter(allValidAdapters,
					targetAdaptersList, methods))
				return;
			else if (
			// !checkWithUser("Ambiguous command invocation", "Invoke command on
			// all valid Adapters: " + toStringAdapterList (allValidAdapters,
			// "", ", ", ""))) {
			!checkWithUserAll(allValidAdapters)) {
				if (checkWithUserSelected()) {
					// new Thread(new
					// uiFrame(allValidAdapters,targetAdaptersList,
					// methods)).start();
					new Thread(OEFrameSelector.createFrame(allValidAdapters,
							targetAdaptersList, methods)).start();
					return;
				} else {
					/*
					 * blockedThread = Thread.currentThread();
					 * System.out.println("stack before blocking");
					 * blockedThread.dumpStack();
					 */
					showMessage("Ambiguous  command invocation: Command not executed");
					/*
					 * if (blockedThread != Thread.currentThread())
					 * System.out.println("thread changed!");
					 * System.out.println("stack after blocking");
					 * blockedThread.dumpStack(); blockedThread = null;
					 */
					return;
				}
			}
		
		Hashtable objectMethods = new Hashtable();
		for (int i = 0; i < targetAdaptersList.size(); i++) {
			Vector targetAdapters = (Vector) targetAdaptersList.elementAt(i);
			for (int j = 0; j < targetAdapters.size(); j++) {
				Object targetObject = targetObject((ObjectAdapter) targetAdapters
						.elementAt(j));
				// new uiMethodInvocationManager(this,
				// targetObject((uiObjectAdapter)targetAdapters.elementAt(j)),(Method)
				// methods.elementAt(i));
				if (targetObject != null) {
					objectMethods.put(targetObject, methods.elementAt(i));
				}
			}
		}
		new MethodInvocationManager(this, null, null, objectMethods);
	}

	public void processMethodsOnPredefinedObjects(Vector<MethodProxy> methods) {
		Vector targetObjectsList = new Vector();
		Vector allValidObjects = new Vector();
		int size = 0;
		for (int i = 0; i < methods.size(); i++) {
			Vector targetObjects = predefinedTargetObjects((MethodProxy) methods
					.elementAt(i));
			for (int j = 0; j < targetObjects.size(); j++)
				allValidObjects.addElement(targetObjects.elementAt(j));
			size += targetObjects.size();
			targetObjectsList.addElement(targetObjects);
		}
		if (allValidObjects.size() == 0) {
			showMessage("Command cannot be invoked on   any windowed object");
			return;
		} else if (allValidObjects.size() > 1
				&& !checkWithUser("Ambiguous  command invocation",
						"Invoke command on all valid objects: "
								+ allValidObjects.toString())) {
			showMessage("Ambiguous command invocation: Command not executed");
			return;
		}
		for (int i = 0; i < targetObjectsList.size(); i++) {
			Vector targetObjects = (Vector) targetObjectsList.elementAt(i);
			for (int j = 0; j < targetObjects.size(); j++) {
				new MethodInvocationManager(this, targetObjects.elementAt(j),
						(MethodProxy) methods.elementAt(i));
			}
		}

	}

	public void initDerivedFrame(uiFrame editor) {
		editor.setAttributes(getSelfAttributes(), getChildrenAttributes());
		if (manualTitle)
			editor.setTitle(getTitle());
	}

	public void initDerivedAdapter(ObjectAdapter selfAdapter) {
		selfAdapter.initAttributes(selfAttributes, childrenAttributes);
	}

	public void actionPerformed(VirtualActionEvent e) {
		/*
		 * Thread currentThread = Thread.currentThread(); if (currentThread !=
		 * eventThread) { eventThread = currentThread;
		 * System.out.println("eventThread" + currentThread);
		 * System.out.println(currentThread.getClass()); if (paintThread ==
		 * eventThread) System.out.println("event thread is paint thread");
		 *  }
		 */
		/*
		 * System.out.println("action performed"); debugScroll(topAdapter);
		 */
		String name = e.getActionCommand();
		// System.out.println ("Event " + name);

		if (e.getSource() instanceof VirtualMenuItem
				|| e.getSource() instanceof VirtualButton) {
			// if (e.getSource() instanceof uiMethodMenuItem) {
			// uiVirtualMethodMenuItem item = (uiVirtualMethodMenuItem)
			// e.getSource();
			// processMethodMenuItem(item);
			/*
			 * Class c = getAdapter().getPropertyClass(); primitiveClassList
			 * list = new primitiveClassList(); Object object; if
			 * (list.isPrimitiveClass(c.getName())) object =
			 * getAdapter().getValue(); else object =
			 * getAdapter().getRealObject(); if (item.getMethod() != null) {
			 * Method method = item.getMethod(); uiMethodInvocationManager iman =
			 * new uiMethodInvocationManager(this, object, method); } else if
			 * (item.getConstructor() != null) { System.out.println("executing
			 * constructor"); Constructor constructor = item.getConstructor();
			 * uiMethodInvocationManager iman = new
			 * uiMethodInvocationManager(object, constructor); }
			 */
			// }
			/* else */if (name.equals(ELIDE_COMMAND)) {
				elideOperationsModel.elide();
				/*
				 * uiObjectAdapter selection = (uiObjectAdapter)
				 * uiSelectionManager.getCurrentSelection(); //ObjectAdapter
				 * selection = uiSelectionManager.getSelectedObjects();
				 * toggleElide(uiSelectionManager.getSelections());
				 */

			}
			/*
			 * else if (name.equals("Deep Elide")) { uiObjectAdapter selection =
			 * (uiObjectAdapter) uiSelectionManager.getCurrentSelection(); if
			 * (selection != null) { deepElide(selection); this.doLayout();
			 * //(new ElideAdapterVisitor
			 * (selection)).traverseContainersFrom(DEEP_ELIDE_LEVEL); }}
			 */
			else if (name.equals(DEEP_ELIDE_4)) {
				elideOperationsModel.deepExpand();
				
			} else if (name.equals(ELIDE_HANDLE)) {
				elideOperationsModel.handles();
				// elideHandle(uiSelectionManager.getSelections());

			} else if (name.equals(UPDATE_COMMAND)) {
				miscEditOperationsModel.update();
				
			} else if (name.equals(UPDATE_ALL_COMMAND)) {
				fileMenuModel.updateAll();
				// doUpdateAll();
			} else if (name.equals("Tool")) {
				// Invoke a chooser/editor tool for the
				// current selection.
				doToolAction();
			} else if (name.equals(SETTINGS_COMMAND)) {
				// Configure settings
				miscEditOperationsModel.settings();
				// doSettingsAction();
			} else if (name.equals(SELECT_UP_COMMAND)) {
				selectionOperationsModel.selectUp();
				// uiSelectionManager.selectUp();
			} else if (name.equals(SELECT_DOWN_COMMAND)) {
				selectionOperationsModel.selectDown();
				// uiSelectionManager.selectDown();
			} else if (name.equals(SELECT_ALL_COMMAND)) {
				selectionOperationsModel.selectAll();
				// uiSelectionManager.select(browser.getAdapter());
			} else if (name.equals(FORWARD_ADAPTER_NAME)) {
				// System.out.println("started forwarding");
				// changeToExistingAdapter(skipNextCurrentAdapters(adapterHistory.toNextAdapter()));
				windowOperationsModel.forward();
				// windows
				// browser.forwardAdapter();
				// System.out.println("finished forwarding");
			} else if (name.equals(BACK_ADAPTER_NAME)) {
				windowOperationsModel.back();
				// changeToExistingAdapter(skipPrevCurrentAdapters(adapterHistory.toPrevAdapter()));
				// browser.backAdapter();
			} else if (name.equals(REFRESH_COMMAND)) {
				refreshOperationsModel.refresh();
				// doRefresh();
			} else if (name.equals(AUTO_REFRESH_COMMAND)) {
				refreshOperationsModel.autoRefresh();
				// doAutoRefresh();
			} else if (name.equals(AUTO_REFRESH_ALL_COMMAND)) {
				refreshOperationsModel.autoRefreshAllFrames();
				// doAutoRefreshAll();
			} else if (name.equals(INCREMENTAL_REFRESH_COMMAND)) {
				refreshOperationsModel.incrementalRefresh();
				ShapesAdapter.toggleIncremental();
			} else if (name.equals(EXIT_COMMAND)) {
				fileMenuModel.exit();
				/*
				 * if (isTopFrame()) { System.exit(0); } else {
				 * uiFrameList.removeFrame(this); dispose(); }
				 */
			} else if (name.equals(LOAD_FILE_COMMAND)) {
				fileMenuModel.load();
				
			} else if (name.equals(OPEN_FILE_COMMAND)) {
				fileMenuModel.open();
				
			} else if (name.equals(SAVE_COMMAND)) {
				fileMenuModel.save();
				// doSave();

			} else if (name.equals(SAVE_AS_FILE_COMMAND)) {
				fileMenuModel.saveAs();
				
			} else if (name.equals(SAVE_TEXT_AS_FILE_COMMAND)) {
				fileMenuModel.saveTextAs();
				// doSaveTextAs();
			} else if (name.equals(NEW_FRAME_COMMAND)) {
				newEditorOperationsModel.newEditor();
				

			} else if (name.equals(NEW_TEXT_FRAME_COMMAND)) {
				newEditorOperationsModel.newTextEditor();
				
			} else if (name.equals(NEW_TABLE_FRAME_COMMAND)) {
				newEditorOperationsModel.newTableEditor();
				
			} else if (name.equals(NEW_TAB_FRAME_COMMAND)) {
				newEditorOperationsModel.newTableEditor();
				

			} else if (name.equals(NEW_SCROLL_PANE_COMMAND)) {
				windowOperationsModel.newWindowRight();
				/*
				 * Selectable s = uiSelectionManager.getCurrentSelection();
				 * newScrollPaneRight((uiObjectAdapter) s);
				 */

			} else if (name.equals(NEW_SCROLL_PANE_BOTTOM_COMMAND)) {
				windowOperationsModel.newWindowBottom();
				/*
				 * Selectable s = uiSelectionManager.getCurrentSelection();
				 * newScrollPaneBottom((uiObjectAdapter) s);
				 */

			} else if (name.equals(REPLACE_FRAME_COMMAND)) {
				newEditorOperationsModel.replaceWindow();
				// browser.replaceFrame();

			} else if (name.equals(TREE_PANEL_COMMAND)) {
				windowOperationsModel.tree();
				// topViewManager.treePanel();

			} else if (name.equals(DRAW_PANEL_COMMAND)) {
				windowOperationsModel.drawing();
				// topViewManager.drawPanel();

			} else if (name.equals(MAIN_PANEL_COMMAND)) {
				windowOperationsModel.mainPanel();
				// topViewManager.mainPanel();

			} else if (name.equals(TOOLBAR_COMMAND)) {
				windowOperationsModel.toolbar();
				// topViewManager.toolBar();
			} else if (name.equals(SECONDARY_PANEL_COMMAND)) {
				windowOperationsModel.secondaryPanel();
				// topViewManager.secondaryPanel();

			} else if (name.equals(WINDOW_HISTORY_PANEL_COMMAND)) {
				windowOperationsModel.secondaryPanel();
				// topViewManager.windowHistoryPanel();

			} else if (name.equals(SELECTED_COMMAND)) {
				customizeOperationsModel.selected();
				
			} else if (name.equals("Broadcast/Multicast")) {
				// Get the current selection
				customizeOperationsModel.broadcast();
				
			} else if (name.equals(UNDO_COMMAND)) {
				undoRedoModel.undo();
				// undoer.undo();
			} else if (name.equals(REDO_COMMAND)) {
				undoRedoModel.redo();
				// undoer.redo();
			} else if (name.equals(COPY_COMMAND)) {
				doOperationsModel.copy();
				/*
				 * Selectable s = uiSelectionManager.getCurrentSelection(); if
				 * (s != null) { ObjectClipboard.clear();
				 * ObjectClipboard.add(s.getObject()); }
				 */
			} else if (name.equals(CUT_COMMAND)) {
				doOperationsModel.cut();
				/*
				 * uiObjectAdapter s = (uiObjectAdapter)
				 * uiSelectionManager.getCurrentSelection();
				 * //uiSelectionManager.unselect(); if (s != null) {
				 * ObjectClipboard.add(s.getObject()); s.deleteFromParent(); }
				 */
			} else if (name.equals(PASTE_COMMAND)) {
				doOperationsModel.paste();
				
			} else if (name.equals(LINK_COMMAND)) {
				doOperationsModel.link();
				
			} else if (name.equals(CLEAR_COMMAND)) {
				doOperationsModel.deleteAllElements();
				// ObjectClipboard.clear();
			}

			else if (((Menu) ((MenuItem) e.getSource()).getParent()).getLabel()
					.equals(CUSTOMIZE_MENU)) {
				// Invoke the class attribute editor.
				// CHANGED TO TEST BEANINFO GENERATION
				customizeOperationsModel.invokeDynamicCommand(name);
				
			}
		}
		/*
		 * setCursor(Cursor.WAIT_CURSOR); getAdapter().refresh(); validate();
		 * setCursor(Cursor.DEFAULT_CURSOR);
		 */
	}

	// Vector currentAdapters = new Vector();
	Vector currentButtons = new Vector();

	public ObjectAdapter newWindowRight(Object o) {
		this.doLayout();
		return uiGenerator.generateInNewBrowsableContainer(this, o, null,
				JSplitPane.HORIZONTAL_SPLIT);
		// uiFrame editor =
		/*
		 * uiGenerator.generateUIScrollPane(this, o, null, (uiObjectAdapter)
		 * null, JSplitPane.HORIZONTAL_SPLIT);
		 */
	}

	public ObjectAdapter newWindowBottom(Object o) {
		this.doLayout();
		// uiFrame editor =
		return uiGenerator.generateUIScrollPane(this, o, null,
				(ObjectAdapter) null, JSplitPane.VERTICAL_SPLIT);
	}

	public ObjectAdapter newWindowRight() {
		this.doLayout();
		// uiFrame editor =
		return uiGenerator.generateUIScrollPane(this, null, null,
				(ObjectAdapter) null, JSplitPane.HORIZONTAL_SPLIT);
	}

	public ObjectAdapter newWindowBottom() {
		this.doLayout();
		// uiFrame editor =
		return uiGenerator.generateUIScrollPane(this, null, null,
				(ObjectAdapter) null, JSplitPane.VERTICAL_SPLIT);
	}

	public void newScrollPaneRight(ObjectAdapter a) {
		// showToolBar();
		if (a == null)
			return;

		// uiFrame editor =
		// uiGenerator.generateUIScrollPane(this, a.getObject(),
		uiGenerator.generateInNewBrowsableContainer(this, a.getObject(),
				(ObjectAdapter) a, JSplitPane.HORIZONTAL_SPLIT);
		// editor.setVisible(true);
		this.setVisible(true);
	}

	void newScrollPaneRight(Object obj) {
		// showToolBar();
		/*
		 * uiFrame editor = uiGenerator.generateUIScrollPane(this, obj, null,
		 * null, JSplitPane.HORIZONTAL_SPLIT);
		 */
		newScrollPaneRight(obj, null);
	}

	void newScrollPaneRight(Object obj, String title) {
		// showToolBar();

		// uiFrame editor =
		uiGenerator.generateUIScrollPane(this, obj, null, null,
				JSplitPane.HORIZONTAL_SPLIT, title);
	}

	void newScrollPaneBottom(ObjectAdapter a) {
		// showToolBar();

		if (a != null) {
			// uiFrame editor = uiGenerator.generateUIFrame(s.getObject());
			// uiFrame editor =
			// uiGenerator.generateUIScrollPane(this, a.getObject(),
			uiGenerator.generateInNewBrowsableContainer(this, a.getObject(),
					(ObjectAdapter) a, JSplitPane.VERTICAL_SPLIT);
			// editor.setVisible(true);
			this.setVisible(true);
		}
	}

	
	public void addCurrentAdapter(ObjectAdapter adapter, VirtualContainer c) {
		// public void addCurrentAdapter(uiObjectAdapter adapter,
		// VirtualScrollPane c) {
		Object drawing = this.getDrawing();
		// System.out.println("adding " + adapter);
		/*
		 * if (adapter.getRealObject() == drawing) { frame.setDrawingAdapter
		 * (adapter); return; }
		 */
		if (adapter == null || adapter.getRealObject() == drawing)
			return;
		// browser.addCurrentAdapter(adapter);
		browser.newAdapter(adapter, c);
		// getTopAdapter().initAttributes(getSelfAttributes(),
		// getChildrenAttributes());
	}

	public void setOriginalAdapter(ObjectAdapter adapter) {
		Object drawing = this.getDrawing();
		// System.out.println("adding " + adapter);
		/*
		 * if (adapter.getRealObject() == drawing) { frame.setDrawingAdapter
		 * (adapter); return; }
		 */
		if (adapter == null || adapter.getRealObject() == drawing)
			return;
		// browser.addCurrentAdapter(adapter);
		browser.setOriginalAdapter(adapter);
	}


	public Vector refreshableAdapters() {
		Vector retVal = new Vector();
		if (this.getTopTreeAdapter() != null)
			retVal.addElement(this.getTopTreeAdapter());
		// if (this.getDrawingAdapter() != null)
		// retVal.addElement(this.getDrawingAdapter());
		for (Enumeration adapters = browser.getAdapterHistory().elements(); adapters
				.hasMoreElements();) {
			// System.out.println("uiFrame: refreshing adapter" );
			retVal.addElement(adapters.nextElement());
			// ((uiObjectAdapter) adapters.nextElement()).refresh();
			// uiGenerator.deepProcessAttributes(adapter);
		}
		for (Enumeration adapters = this.getUserAdapters().elements(); adapters
				.hasMoreElements();) {
			// System.out.println("uiFrame: refreshing adapter" );
			retVal.addElement(adapters.nextElement());
			// ((uiObjectAdapter) adapters.nextElement()).refresh();
			// uiGenerator.deepProcessAttributes(adapter);
		}
		if (this.getSecondaryObjectAdapter() != null)
			retVal.addElement(this.getSecondaryObjectAdapter());
		return retVal;

	}
	// meant to be called by app programmer
	public void refresh() {
		FullRefreshFromProgram.newCase(getAdapter().getRealObject(), this);
//		Tracer.warning("Refreshing complete object: " + getTopAdapter().getRealObject() + ". If you know them, annonce property and/or list events.");
		doRefresh();
	}

	public void doRefresh() {
		// System.out.println("uiFrame: doing refresh");
		setExplicitRefresh(true);
		setRefreshWillHappen(false);
		doImplicitRefresh();
		setExplicitRefresh(false);
		
		
	}
	public void oldDoRefresh() {
		// System.out.println("uiFrame: doing refresh");
		setExplicitRefresh(true);
		setRefreshWillHappen(false);
		setCursor(Cursor.WAIT_CURSOR);
		
		for (Enumeration adapters = this.refreshableAdapters().elements(); adapters
				.hasMoreElements();) {
			// System.out.println("uiFrame: refreshing adapter" );
			ObjectAdapter adapter = (ObjectAdapter) adapters.nextElement();
			adapter.refresh();
			
		}
		checkPreInMenuTreeAndButtonCommands();
		setExplicitRefresh(false);
		
		if (isDummy())
			return;

		// getAdapter().refresh();

		if (getJTree() != null && getJTree().isVisible())
			treeView.treeStructureChanged();
		else
			maybeShowJTree();
		setTitle();
		validate();
		// checkPreInMenuTreeAndButtonCommands();
		// rootMenuGroup.checkPreInMenuTree();
		toolbarManager.checkPre();
		setCursor(Cursor.DEFAULT_CURSOR);
	}
	
	public void refreshTreeIfVisible() {
		if (getJTree() != null && getJTree().isVisible())
			treeView.reload();
	}
	
	public void nodeChanged(TreeNode node) {
		if (getJTree() != null && getJTree().isVisible())
			treeView.nodeChanged(node);
	}
	public void expandAllTreeNodes() {
		if (getJTree() != null && getJTree().isVisible())
			treeView.expandAllTreeNodes();
		
	}


	boolean autoRefresh = true;

	/*
	 * public void doAutoRefresh() { autoRefresh = !autoRefresh; if
	 * (!autoRefresh) autoRefreshAll = false; }
	 */
	public boolean autoRefreshAll() {
		return autoRefreshAll;
	}

	public boolean autoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(boolean newVal) {
		autoRefresh = newVal;
	}

	public void setAutoRefreshAll(boolean newVal) {
		autoRefreshAll = newVal;
	}

	boolean autoRefreshAll = false;

	/*
	 * public void doAutoRefreshAll() { autoRefreshAll = !autoRefreshAll; if
	 * (autoRefreshAll) autoRefresh = true; }
	 */
	public void printContainerTree(VirtualComponent c, String offset) {
		System.out.println(offset + c.getName() + " " + c.getClass() + " " + c);
		if (c instanceof Container) {
			VirtualContainer con = (VirtualContainer) c;
			for (int i = 0; i < con.getComponentCount(); i++) {
				printContainerTree(con.getComponent(i), offset + "	");
			}
		}

	}

	void doMyImplicitRefresh() {
		if (disposed || isSuppressPropertyNotifications())
			return;

		// System.out.println("before refresh");
		// printContainerTree(this.getContainer(), " ");
		// System.out.println("uiFrame: doing implicit refresh");
		ImplicitRefreshStarted.newCase(this, this);
		if (myFrame != null)
		setCursor(Cursor.WAIT_CURSOR);
		// if (this.getTopTreeAdapter() != null)
		// getTopTreeAdapter().implicitRefresh();

		// for (Enumeration adapters = browser.getAdapterHistory().elements();
		// adapters.hasMoreElements();) {
		for (Enumeration adapters = this.refreshableAdapters().elements(); adapters
				.hasMoreElements();) {
			// System.out.println("uiFrame: refreshing adapter" );
			((ObjectAdapter) adapters.nextElement()).implicitRefresh(false);
		}
		expandAllTreeNodes();
//		refreshTreeIfVisible();
		/*
		 * if (drawingAdapter != null) drawingAdapter.implicitRefresh();
		 */

		/*
		 * for (Enumeration adapters = currentAdapters.elements();
		 * adapters.hasMoreElements();) { //System.out.println("refreshing ");
		 * ((uiObjectAdapter) adapters.nextElement()).refresh(); }
		 */

		if (getJTree() == null || !getJTree().isVisible())
			maybeShowJTree();

		// getAdapter().refresh();
		/*
		 * if (jTree != null && jTree.isVisible()) treeStructureChanged(); else
		 * maybeShowJTree();
		 */
		// System.out.println("after refresh");
		// printContainerTree(this.getContainer(), " ");
		
		checkPreInMenuTreeAndButtonCommands();
		
		// 
		
//		if (!this.isDummy()) {
		if (myFrame != null) {
			toolbarManager.checkPre();

			validate();
			setCursor(Cursor.DEFAULT_CURSOR);
			setFontSize();
			repaint();
		}
	}
	public void refreshWindow() {
		validate();
		//setCursor(Cursor.DEFAULT_CURSOR);
		//setFontSize();
		repaint();
		
	}

	boolean refreshing = false;
	boolean refreshWillHappen = false;

	public void setRefreshWillHappen(boolean newVal) {
		refreshWillHappen = newVal;
	}

	public boolean getRefreshWillHappen() {
		return refreshWillHappen;
	}

	boolean receivedNotification = false;

	public void setReceivedNotification(boolean newValue) {
		receivedNotification = newValue;
	}

	public boolean getReceivedNotification() {
		return receivedNotification;
	}
	boolean implicitRefresh = true; // set by someone else, unset by uiFrame
	public void setImplicitRefresh (boolean newVal) {
		implicitRefresh = newVal;
	}
	public void doImplicitRefresh() {
		if (!implicitRefresh) {
			implicitRefresh = true;
			return;
		}
		if (refreshing || isSuppressPropertyNotifications()) // unless they are resumed do not update
			return;
		refreshing = true;

		if (!ObjectEditor.shareBeans()) {
			subDoImplicitRefresh();
		} else {
			ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this,
					"doImplicitRefresh"));
		}
		refreshing = false;
	}

	public void subDoImplicitRefresh() {
		if (autoRefreshAll) {
			Vector frames = uiFrameList.getList();
			for (int i = 0; i < frames.size(); i++) {
				uiFrame frame = (uiFrame) frames.elementAt(i);
				if (frame == this)
					frame.doMyImplicitRefresh();
				else
					frame.doRefresh();
				// refreshOperationsModel.refresh();
			}
		} else if (autoRefresh)
			doMyImplicitRefresh();
	}

	/*
	 * public Container getChildPanel() { return browser.getChildPanel(); }
	 */
	AMenuDescriptor menuDescriptor = new AMenuDescriptor();

	public AMenuDescriptor getMenuDescriptor() {
		return menuDescriptor;
	}

	// public static char MENU_NESTING_DELIMITER =
	// GenerateViewObject.NESTING_DELIMITER;
	public static char MENU_NESTING_DELIMITER = '>';

	public static AMenuGroup getMenu(String name, AMenuGroup rootMenuGroup,
			AMenuDescriptor menuDescriptor, MenuSetter menuTest) {

		int leftMark = 0;
		int rightMark = 0;
		if (rootMenuGroup == null)
			return null;
		AMenuGroup parentMenuGroup = rootMenuGroup;
		AMenuGroup retVal = null;
		while (true) {
			rightMark = name.indexOf(MENU_NESTING_DELIMITER, leftMark);
			if (rightMark < 0 || rightMark >= name.length()) {
				rightMark = name.length();
			}
			String childName = name.substring(leftMark, rightMark);
			String childFullName = name.substring(0, rightMark);

			AMenuGroup childMenuGroup = parentMenuGroup
					.getSubMenuGroup(childName);
			if (childMenuGroup == null) {
				if (!menuTest.get(childFullName))
					return null;
				// Menu childMenu = new uiExtendedMenu(childName);
				VirtualMenu childMenu = MenuSelector.createMenu(childName);
				childMenuGroup = new AMenuGroup(childFullName, childMenu,
						menuDescriptor);
				parentMenuGroup.putSubMenuGroup(childName, childMenuGroup);

			}
			// retVal = childMenuGroup.getMenu();
			retVal = childMenuGroup;
			if (rightMark >= name.length())
				break;
			leftMark = rightMark + 1;
			parentMenuGroup = childMenuGroup;
		}

		if (retVal instanceof AMenuGroup)
			return (AMenuGroup) retVal;
		else
			return null;

	}

	private VirtualMenu getMenu(String name) {
		// System.out.println("getMenu " + name);
		// System.out.println(topObject);
		/*
		 * do not know why this check if (name.equals("Constructors") ||
		 * name.equals("Inherited methods") ||
		 * name.equals(BEAN_METHODS_MENU_NAME)) getMenu(methodsMenuName);
		 */
		VirtualMenu menu = (VirtualMenu) menus.get(name);

		if (menu == null) {

			if (name.equals(methodsMenuName)) {
				// menu = new uiExtendedMenu(name);
				menu = MenuSelector.createMenu(name);
				menus.put(name, menu);
				menuBar.add(menu);
				
			} else {
				// menu = new Menu(name);
				menu = MenuSelector.createMenu(name);
				menus.put(name, menu);
				if (!name.equals(this.CONSTANTS_MENU_NAME))
					menuBar.add(menu);
			}
		}
		return menu;
	}

	// private Menu getMenu(String name, MenuSetter menuTest) {
	private AMenuGroup getMenu(String name, MenuSetter menuTest) {
		// System.out.println("getMenu " + name);
		// System.out.println(topObject);
		/*
		 * do not know why this check if (name.equals("Constructors") ||
		 * name.equals("Inherited methods") ||
		 * name.equals(BEAN_METHODS_MENU_NAME)) getMenu(methodsMenuName ,
		 * menuTest);
		 */
		return getMenu(name, rootMenuGroup, menuDescriptor, menuTest);
		/*
		 * Menu menu = (VirtualMenu) menus.get(name);
		 * 
		 * if (menu == null) menu = getMenu(name,rootMenuGroup, menuDescriptor,
		 * menuTest);
		 * 
		 * return menu;
		 */
	}

	Vector methods = new Vector();

	// Add a method to the Method's menu
	public void addMethodMenuItem(String menulabel, MethodDescriptorProxy md,
	/* int position, */String label, MethodProxy method) {
		addMethodMenuItem(null, md, menulabel, /* position, */label, method);

	}


	public void addMethodMenuItem(Object theTargetObject,
			MethodDescriptorProxy md, String menulabel,
			/* int position, */String label, MethodProxy virtualOrRegularMethod) {
		// System.out.println("label" + label + "menulabel" + menulabel);
		// if (menulabel.equals("SLModel") && getDrawPanel()!= null) return;
		if (IntrospectUtility.isPre(virtualOrRegularMethod))
			return;
		if (methods.contains(virtualOrRegularMethod))
			return;
		ClassProxy c = virtualOrRegularMethod.getDeclaringClass();
		// Class c = VirtualMethod.getDeclaringClass(virtualOrRegularMethod);
		// System.out.println(c.getName() + "method" + method.getName());
		if ((getDrawPanel() != null)
				&& c
						.isAssignableFrom(AClassProxy
								.classProxy(slm.SLModel.class)))
			return;
		methods.addElement(virtualOrRegularMethod);
		// uiMethodMenuItem item = new uiMethodMenuItem(theTargetObject, label,
		// virtualOrRegularMethod);
		MethodMenuItem item = new VirtualMethodMenuItem(this,
				theTargetObject, label, virtualOrRegularMethod, md);
		// item.getMenuItem().addActionListener(this);

		VirtualMenu menu = getMenu(menulabel); // get the parent of this
		// item...hopefully returns and creates
		// a new
		// item if this one is not already there.
		/*
		 * if (position != -1) menu.insert(item.getMenuItem(), position); else
		 */
		menu.add(item.getMenuItem());
	}

	public void addMethodMenuItem(String menulabel, MethodDescriptorProxy md,
	/* int position, */String label, MethodProxy method, MenuSetter menuTest,
			String theToolTipText) {
		addMethodMenuItem(null, menulabel, md, /* position, */label, method,
				menuTest, theToolTipText);
	}

	// overloaded even though you could test for null stuff but gets
	// complicated.
	// (caller of this) uiGenerator didn't overload, but made state variable b/c
	// it needed to pass it in here
	// and it had already received the menuSetter object from generateUIframe.

	public void addMethodMenuItem(Object targetObject, String menulabel,
			MethodDescriptorProxy md,
			/* int position, */String label, MethodProxy method,
			MenuSetter menuTest, String theToolTipText) {
		// System.out.println("label" + label + "menulabel" + menulabel);
		// if (menulabel.equals("SLModel") && getDrawPanel()!= null) return;
		if (methods.contains(method))
			return;

		ClassProxy c = method.getDeclaringClass();
		// System.out.println(c.getName() + "method" + method.getName());
		if ((getDrawPanel() != null)
				&& c
						.isAssignableFrom(AClassProxy
								.classProxy(slm.SLModel.class)))
			return;
		methods.addElement(method);
		// uiMethodMenuItem item = new uiMethodMenuItem(label, method);
		VirtualMethodMenuItem item = new VirtualMethodMenuItem(this,
				targetObject, label, method, md);
		VirtualMenuItem menuItem = item.getMenuItem();
		AMethodProcessor.setFont(menuItem, this, md, getOriginalAdapter());
		item.setToolTipText(theToolTipText);
		getAnnotationManager().put(md, theToolTipText, getOriginalAdapter(),
				ACompositeLoggable.getTargetClass(targetObject));
		// getAnnotationManager().put(label, theToolTipText);
		// item.getMenuItem().addActionListener(this);
		AMenuGroup parentMenuGroup = getMenu(menulabel, menuTest);
		VirtualMenuComponent menu = parentMenuGroup.getMenu();
		AMethodProcessor.setFont(menu, this, md,getOriginalAdapter() );
		if (parentMenuGroup == null)
			return;
		// Menu menu = getMenu(menulabel, menuTest);//get the parent of this
		// item...hopefully returns and creates a new
		// item if this one is not already there.
		String fullName = parentMenuGroup.getFullName()
				+ uiFrame.MENU_NESTING_DELIMITER + label;
		// Boolean showItem = ((Boolean)menuTest.get(label)); //did user enter
		// this in Menusetter?
		Boolean showItem = ((Boolean) menuTest.get(fullName)); // did user
																// enter this in
																// Menusetter?
		boolean show = false;

		if (showItem == null) // if wasn't entered in the MenuSetter then just
								// show it by default
			show = true;
		else
			show = showItem.booleanValue();
		/*
		 * if (show){ if (position != -1) menu.insert(item, position); else
		 * menu.add(item); }
		 */
		if (show) {
			AMenuGroup itemGroup = new AMenuGroup(fullName, item.getMenuItem(),
					menuDescriptor);
			parentMenuGroup.putSubMenuGroup(label, itemGroup);
			/*
			 * if (position != -1) menu.insert(item, position); else
			 * menu.add(item);
			 */
		}
	}

	public void addUIGenMenuItem(String menuLabel, int position, String label,
			Object obj) {
		MenuComponent test;
		System.out.println("Label   Called: " + label);
		OEMenuItem item = new OEMenuItem(label, obj);
		VirtualMenu menu = getMenu(menuLabel);
		menu.add(item.getMenuItem());
	}

	Vector<MethodProxy> constructorVMs = new Vector();
	Vector constructors = new Vector();

	public Vector<MethodProxy> getConstructors() {
		return constructorVMs;
	}

	public void addConstructorMenuItem(String menulabel, int position,
			String label, MethodProxy c) {
		if (menuBar == null)
			return;
		if (constructors.contains(c))
			return;
		constructors.addElement(c);
		constructorVMs.addElement(c);
		/*
		 * if (fileMenuModel != null) fileMenuModel.addConstructor(new
		 * AVirtualMethod(c));
		 */
		// uiMethodMenuItem item = new uiMethodMenuItem(label, c);
		/*
		 * uiVirtualMethodMenuItem item = new uiVirtualMethodMenuItem(label, c);
		 * item.addActionListener(this); Menu menu = (VirtualMenu)
		 * getMenu(menulabel, menuSetter).getMenu(); if (position != -1)
		 * menu.insert(item, position); else menu.add(item);
		 */
	}

	Vector constants = new Vector();

	public ConstantMenuItem addConstantMenuItem(String name, Object constant) {
		if (menuBar == null)
			return null;
		if (constants.contains(constant))
			return null;
		constants.addElement(constant);
		VirtualMenu constantMenu = getMenu(CONSTANTS_MENU_NAME);
		ConstantMenuItem item = new ConstantMenuItem(name, constant);
		constantMenu.add(item.getMenuItem());
		return item;
	}

	public VirtualMenuItem getDoneItem() {
		return exitItem;
	}

	public VirtualMenuItem addDoneItem() {
		doneEnabled = true;
		rootMenuGroup.checkPreInMenuTree();
		if (exitItem == null)
			return null;
		exitItem.setLabel(DONE_COMMAND);
		exitItem.setActionCommand(DONE_COMMAND);
		// exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
		// Event.CTRL_MASK,
		// false));
		return exitItem;
	}

	boolean doneEnabled = false;

	public boolean doneEnabled() {
		return doneEnabled;
	}

	Vector<DoneListener> doneListeners = new Vector();

	public void addDoneListener(DoneListener doneListener) {
		if (doneListeners.contains(doneListener))
			return;
		doneListeners.addElement(doneListener);
	}

	public void removeDoneListener(DoneListener doneListener) {
		doneListeners.remove(doneListener);
	}

	public void notifyDoneListeners() {
		for (int i = 0; i < doneListeners.size(); i++) {
			doneListeners.get(i).frameDone(this);
		}
	}

	public VirtualMenuItem addDoneItem(VirtualActionListener actionListener) {
		doneEnabled = true;
		rootMenuGroup.checkPreInMenuTree();
		VirtualMenuItem menuItem = addDoneItem();
		menuItem.addActionListener(actionListener);
		return menuItem;
	}

	public VirtualButton addDoneButton(VirtualActionListener actionListener) {
		return toolbarManager.addUIFrameToolBarButton(DONE_COMMAND, null,
				actionListener);
	}

	public void addDoneItemAndButton(VirtualActionListener actionListener) {
		addDoneItem(actionListener);
		addDoneButton(actionListener);
	}

	public VirtualButton addDoneButton() {
		return toolbarManager.addUIFrameToolBarButton(DONE_COMMAND, null, null);
	}

	//
	public void addClassToAttributeMenu(Object object) {
		if (object == null)
			return;
		ClassProxy compositeTargetClass = ACompositeLoggable.getTargetClass(object);
		compositeTargetClass = ReflectUtil.toMaybeProxyTargetClass(compositeTargetClass, object);
//		String className = ACompositeLoggable.getTargetClass(object).getName();
		String className = compositeTargetClass.getName();

		addClassToAttributeMenu(className);
	}

	public void addClassToSourceMenu(Object object) {
		/*
		 * if (object instanceof CachingRemoteClassProxy)
		 * addClassToSourceMenu(RemoteSelector.getClass(object).getName()); else
		 * addClassToSourceMenu(object.getClass());
		 */

		// String className = object.getClass().getName();
		ClassProxy cp = ACompositeLoggable.getTargetClass(object);
		addClassToSourceMenu(ReflectUtil.toMaybeProxyTargetClass(
				
				ACompositeLoggable.getTargetClass(object), object));

	}

	public void showSource(String className) {
		// if (getSourceDirectory() != null)
		ObjectEditor.showSource(getSourceDirectory(), className);
		// else
		// ObjectEditor.showSource(className);
	}

	public File open(String className) {
		// if (getSourceDirectory() != null)
		return util.misc.Common.open(getSourceDirectory(), className);
		// else
		// ObjectEditor.showSource(className);
	}

	//
	public String[] getCustomizeClassNames() {
		String[] retVal = new String[customizeClassNames.size()];
		for (int i = 0; i < customizeClassNames.size(); i++) {
			retVal[i] = customizeClassNames.elementAt(i);
		}
		return retVal;
	}

	String sourceDirectory;

	public void setSourceDirectory(String newVal) {
		sourceDirectory = newVal;
	}

	public void setGlobalSourceDirectory(String newVal) {
		Vector<uiFrame> frames = uiFrameList.getList();
		for (int i = 0; i < frames.size(); i++) {
			frames.get(i).setSourceDirectory(newVal);
		}
		// sourceDirectory = newVal;
	}

	public String getSourceDirectory() {
		return sourceDirectory;
	}

	Vector<String> customizeClassNames = new Vector();

	// public MenuItem addClassToAttributeMenu(String classname) {

	public VirtualMenuItem addClassToAttributeMenu(String classname) {
		// First check if it is already there
		// if (menuBar == null) return null;
		if (!customizeClassNames.contains(classname))
			customizeClassNames.addElement(classname);
		/*
		 * MenuItem m; //Menu attributeMenu = getMenu("Attributes"); Menu
		 * attributeMenu = (VirtualMenu) getMenu(CUSTOMIZE_MENU_NAME,
		 * menuSetter).getMenu(); for (int i=0; i<attributeMenu.getItemCount();
		 * i++) { m = attributeMenu.getItem(i); if
		 * (m.getLabel().equals(classname)) return m; } m =
		 * MenuItemSelector.createMenuItem(classname); attributeMenu.add(m);
		 * m.addActionListener(this); addSuperClassToAttributeMenu(classname);
		 * return m;
		 */
		return null;
	}

	List<String> sourceClassNames = new Vector();
	List<ClassProxy> sourceClasses = new ArrayList();

	public void addClassToSourceMenu(ClassProxy c) {
		// if (c == null || util.Misc.isJavaClass(c))
		if (c == null || AClassDescriptor.isPredefinedClass(c))
			return;
		String className = c.getName();
		int dollarPos = className.indexOf('$');
		if (dollarPos != -1) // inner class
			return;
		if (sourceClassNames.contains(className))
			return;
		// if (!sourceClassNames.contains(className))
		sourceClassNames.add(className);
		sourceClasses.add(c);
		addClassToSourceMenu(c.getInterfaces());
		addClassToSourceMenu(c.getSuperclass());
		ClassProxy arClass = AttributeRegistry.getAttributeRegistererClass(c);
		if (arClass != null)
			addClassToSourceMenu(arClass);
		ClassProxy binfoClass = AClassDescriptor.getBeanInfoClass(c);
		if (binfoClass != null)
			addClassToSourceMenu(binfoClass);

		return;
	}
	

	public void addClassToSourceMenu(ClassProxy[] classes) {
		for (int i = 0; i < classes.length; i++)
			addClassToSourceMenu(classes[i]);
	}

	public void addClassToSourceMenu(String classname) {
		// First check if it is already there
		// if (menuBar == null) return null;
		if (!sourceClassNames.contains(classname))
			sourceClassNames.add(classname);

		return;
	}

	public List<String> getSourceClassNames() {
		return sourceClassNames;
	}
	public List<ClassProxy> getSourceClasses() {
		return sourceClasses;
	}

	public void newAddClassToAttributeMenu(String classname) {
		// First check if it is already there
		// if (menuBar == null) return null;
		if (!customizeClassNames.contains(classname))
			customizeClassNames.addElement(classname);
		/*
		 * MenuItem m; //Menu attributeMenu = getMenu("Attributes"); Menu
		 * attributeMenu = (VirtualMenu) getMenu(CUSTOMIZE_MENU_NAME,
		 * menuSetter).getMenu(); for (int i=0; i<attributeMenu.getItemCount();
		 * i++) { m = attributeMenu.getItem(i); if
		 * (m.getLabel().equals(classname)) return m; } m =
		 * MenuItemSelector.createMenuItem(classname); attributeMenu.add(m);
		 * m.addActionListener(this); addSuperClassToAttributeMenu(classname);
		 * return m;
		 */
	}

	public void addSuperClassToAttributeMenu(String className) {
		try {
			ClassProxy c = uiClassFinder.forName(className);
			if (c.getSuperclass() != null) {
				addClassToAttributeMenu(AClassDescriptor.toShortName(c
						.getSuperclass().getName()));
			}

		} catch (Exception e) {
		}
	}

	

	public VirtualButton addToolBarButton(String label, Icon icon,
			MethodProxy method, String place_toolbar) {
		// return addToolBarButton (null, label, icon, method, place_toolbar );
		return toolbarManager.addToolBarButton(label, icon, method,
				place_toolbar, -1);
	}

	//
	// Add a ToolBar item
	//
	public VirtualButton addToolBarButton(Object targetObject, String label,
			Icon icon, MethodProxy method, String place_toolbar, int pos) {
		return toolbarManager.addToolBarButton(targetObject, label, icon,
				method, place_toolbar, pos);
	}

	public VirtualButton addToolBarButton(Object targetObject, String label,
			Icon icon, MethodProxy method, String place_toolbar) {
		return toolbarManager.addToolBarButton(targetObject, label, icon,
				method, place_toolbar, -1);
		
	}

	/*
	 * void printToolbarButtons() { Component[] comps = toolBar.getComponents();
	 * System.out.println("Toolbar Buttons"); for (int i=0; i<comps.length;
	 * i++) System.out.println(((JButton) comps[i]).getLabel()); }
	 */
	public static int indexOf(VirtualComponent[] components, VirtualComponent c) {
		int index;
		for (index = 0; index < components.length; index++) {
			// System.out.println(((JButton)c).getLabel() + "c[i]" +
			// ((JButton)components[index]).getLabel());
			if (components[index] == c)
				return index;
		}
		return index;

	}

	
	//
	// Add a UI frame command
	//
	// JButton forwardButton, backButton;
	public void addUIFrameToolBarButton(String label, Icon icon) {
		toolbarManager.addUIFrameToolBarButton(label, icon);
		/*
		 * if (toolBarButtons.contains(label)) return; //showToolBar();
		 * //toolBar.setVisible(true); //toolBar.setFloatable(true); JButton
		 * button = new JButton(label, icon); button.addActionListener(this);
		 * toolBarButtons.addElement(label); if (label == "Save"){
		 * setSaveButton(button); toolBar.add(button); } else if (label ==
		 * FORWARD_ADAPTER_NAME) { forwardButton = button; toolBar.add(button); }
		 * else if (label == BACK_ADAPTER_NAME) { backButton = button;
		 * toolBar.add(button); } else toolBar.add(button);
		 * //toolBar.removeAll();
		 */
	}

	

	private void recursiveRefreshAttributes(ObjectAdapter a, String classname) {

		if (a instanceof CompositeAdapter) {
			Enumeration children = ((CompositeAdapter) a).getChildAdapters();
			while (children.hasMoreElements()) {
				ObjectAdapter c = (ObjectAdapter) children.nextElement();
				recursiveRefreshAttributes(c, classname);
			}
		}

		a.processAttributeList();
	}

	//
	public void refreshAttributes(String classname) {
		setCursor(Cursor.WAIT_CURSOR);
		recursiveRefreshAttributes(browser.getTopAdapter(), classname);
		// repaint();
		validate();
		setCursor(Cursor.DEFAULT_CURSOR);
	}

	static public boolean isSavable(ObjectAdapter adapter) {
		return !((new IsSerializableAdapterVisitor(adapter)).traverse())
				.contains(new Boolean(false));
		/*
		 * try { ObjectOutputStream f = new ObjectOutputStream(new
		 * FileOutputStream()); f = new ObjectOutputStream (new FileOutputStream
		 * (new File( f.writeObject(obj); f.close(); return true; } catch
		 * (Exception e) { // Error in writing object return false;
		 *  }
		 */
	}

	ClassProxy lastClass;
	Hashtable fileDirectoryMapping = new Hashtable();
	String lastFileName;
	String lastDirectoryName;
	String lastSaveFileName;
	String lastTextDirectoryName;
	String lastTextFileName;
	String lastSaveDirectoryName;
	static FileDialog fileDialog = new FileDialog(new Frame(), "Save as",
			FileDialog.SAVE);
	static FileDialog textFileDialog = new FileDialog(new Frame(),
			"Save Text as", FileDialog.SAVE);

	void setFileDirectoryPair(FileDialog fileDialog) {
		if (lastFileName == null)
			fileDialog.setFile(this.getTitle() + ".obj");
		else
			fileDialog.setFile(lastFileName);
		if (lastDirectoryName != null)
			fileDialog.setDirectory(lastDirectoryName);
		/*
		 * AFileDirectoryPair fdp = fileDirectoryMapping.get(obj.getClass()); if
		 * (fdp == null) fileDialog.setFile(this.getTitle() + ".obj"); else {
		 * fileDialog.setFile(fdp.getDirectoryName());
		 * fileDialog.setFile(fdp.getFileName()); }
		 */
	}

	void setTextFileDirectoryPair(FileDialog fileDialog) {
		if (lastTextFileName == null)
			fileDialog.setFile(this.getTitle());
		else
			fileDialog.setFile(lastTextFileName);
		if (lastTextDirectoryName != null)
			fileDialog.setDirectory(lastTextDirectoryName);
		else if (lastDirectoryName != null)
			fileDialog.setDirectory(lastDirectoryName);
		/*
		 * AFileDirectoryPair fdp = fileDirectoryMapping.get(obj.getClass()); if
		 * (fdp == null) fileDialog.setFile(this.getTitle() + ".obj"); else {
		 * fileDialog.setFile(fdp.getDirectoryName());
		 * fileDialog.setFile(fdp.getFileName()); }
		 */
	}

	public void doSaveAs() {
		fileMenuModel.save();
		
	}

	public void doSaveTextAs() {
		fileMenuModel.saveTextAs();
		
	}

	public void doSave() {
		fileMenuModel.save();
		/*
		 * if (saveFileName != null)
		 * 
		 * saveState(saveFileName);
		 */
	}


	public void doLoad() {
		fileMenuModel.load();
		
	}

	
	public void loadAttributes(String directory, String file) {

		String fileName = directory + System.getProperty("file.separator")
				+ file;
		try {
			// ObjectInputStream f = new ObjectInputStream(new
			// FileInputStream(directory+System.getProperty("file.separator")+"class_attributes"));
			ObjectInputStream f = new ObjectInputStream(new FileInputStream(
					fileName + "class_attributes"));
			UIAttributeManager m = (UIAttributeManager) f.readObject();
			f.close();
			AttributeManager.setEnvironment(m);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load the instance attributes
		try {
			// ObjectInputStream f = new ObjectInputStream(new
			// FileInputStream(directory+System.getProperty("file.separator")+"instance_attributes"));
			ObjectInputStream f = new ObjectInputStream(new FileInputStream(
					fileName + "instance_attributes"));
			Hashtable table = (Hashtable) f.readObject();
			f.close();
			HashtableToInstanceAttributes.setHashtable(browser.getAdapter(),
					table);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			refreshAttributes("");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public String getShortFileName(String fileName) {
		// String retVal;
		int pos = fileName.lastIndexOf(System.getProperty("file.separator"));
		if (pos < 0)
			return fileName;
		else
			return fileName.substring(pos + 1, fileName.length());

	}

	

	public void setFontSize() {
		setFontSize(AFontSizeModel.getFontSize());
	}

	public void setFontSize(Container c) {
		VirtualContainer vc = AWTContainer.virtualContainer(c);
		WidgetAdapter.setFontInContainerTree(vc, null, null, null,
				AFontSizeModel.getFontSize());
	}

	public void setFontSize(Integer size) {
		if (size == null)
			return;
		WidgetAdapter.setFontInFrameTree(this, null, null, null, size);
	}

	public void doOpen() {
		fileMenuModel.open();
		
	}

	public void setSaveMenuItemEnabled(boolean flag) {
		saveItem.setEnabled(flag);
		if (saveButton != null)
			saveButton.setEnabled(flag);
		/*
		 * if (saveAsButton != null) saveAsButton.setEnabled(flag);
		 */
	}

	public void setSaveAsMenuItemEnabled(boolean flag) {
		saveAsItem.setEnabled(flag);
	}

	boolean undoHistoryEmpty = true;

	public void undoHistoryEmpty(boolean isEmpty) {
		if (undoItem != null) {
			undoItem.setEnabled(!isEmpty);
		}
	}

	public void redoHistoryEmpty(boolean isEmpty) {
		if (redoItem != null) {
			redoItem.setEnabled(!isEmpty);
		}
	}

	public void commandUndone(ExecutedCommand c, int index) {

	}

	public void commandRedone(ExecutedCommand c, int index) {

	}

	public void commandExecuted(ExecutedCommand c, int index) {

	}

	public void doToolAction() {

	}

	public void doSettingsAction() {
		uiFrame sf = uiGenerator.generateUIFrame(new MethodParameters(),
				(myLockManager) null);
		sf.setVisible(true);
	}

	/*
	 * public void pack() { //this.setSize(100,100); }
	 */

	Vector userAdapters = new Vector();

	public Vector getUserAdapters() {
		return userAdapters;
	}

	public void addUserAdapter(ObjectAdapter adapter) {
		if (userAdapters.contains(adapter))
			return;
		userAdapters.addElement(adapter);
	}

	/*
	 * public void addUserAdapter(uiObjectAdapter adapter) {
	 * browser.addUserAdapter(adapter); } public Vector getUserAdapters() {
	 * return userAdapters; }
	 */
	public void addMenuObject(Object menuObject) {
		uiGenerator.uiAddMethods(this, menuObject);
		displayMenuTree();
		doRefresh();
	}
	Map<String, Object> nameToMenuObject = new Hashtable();
	public void addButNotDisplayMenuObject(String name, Object menuObject) {
		nameToMenuObject.put(name, menuObject);
		uiGenerator.uiAddMethods(this, menuObject);
		// displayMenuTree();
	}
	
	public Object getMenuObject (String name) {
		return nameToMenuObject.get(name);
	}
	
	public Object getMenuObject (ClassProxy aClassProxy) {
		return getMenuObject(aClassProxy.getSimpleName());
	}
	
	public Object getMenuObject (Class aClass) {
		return getMenuObject(aClass.getSimpleName());
	}
	
	public ADemoFontOperationsModel getDemoMenuObject () {
		return (ADemoFontOperationsModel) getMenuObject(ADemoFontOperationsModel.class);
	}
	
	public ATreeWindowOperationsModel  getTreeMenuObject () {
		return (ATreeWindowOperationsModel) getMenuObject(ATreeWindowOperationsModel.class);
	}
	
	public void setDemoFont() {
		ADemoFontOperationsModel demoFontOperationsModel = getDemoMenuObject();
		if (demoFontOperationsModel != null)
		demoFontOperationsModel.demoFontSize();
		else
			Tracer.error("No demo font model to set demo font");
	}


	public void displayMenuTree() {
		MenuTreeDisplayStarted.newCase(rootMenuGroup, this);
		rootMenuGroup.displayMenuTree();
		MenuTreeDisplayEnded.newCase(rootMenuGroup, this);

	}

	public void checkPreInMenuTreeAndButtonCommands() {
		if (rootMenuGroup != null)
			rootMenuGroup.checkPreInMenuTree();
		for (int i = 0; i < customMenuItems.size(); i++)
			customMenuItems.get(i).checkPre();
		for (int i = 0; i < customOrAutomaticButtons.size(); i++)
			customOrAutomaticButtons.get(i).checkPre();

	}

	public void registerFullNamesInMenuTree() {
		rootMenuGroup.registerFullNamesInMenuTree();
	}

	public void checkPreInToolbar() {
		if (toolbarManager != null)
			toolbarManager.checkPre();
	}

	AFileOperationsModel fileMenuModel = new AFileOperationsModel();
	ACustomizeOperationsModel customizeOperationsModel = new ACustomizeOperationsModel();
	ADoOperationsModel doOperationsModel = new ADoOperationsModel();
	AMiscEditOperationsModel miscEditOperationsModel = new AMiscEditOperationsModel();
	AnElideOperationsModel elideOperationsModel = new AnElideOperationsModel();
	ANewEditorOperationsModel newEditorOperationsModel = new ANewEditorOperationsModel();
	AnUndoRedoModel undoRedoModel = new AnUndoRedoModel();
	ARefreshOperationsModel refreshOperationsModel = new ARefreshOperationsModel();
	ASelectionOperationsModel selectionOperationsModel = new ASelectionOperationsModel();
	AWindowOperationsModel windowOperationsModel = new AWindowOperationsModel();
	ASourceOperationsModel sourceOperationsModel = new ASourceOperationsModel();
	FrameModel[] frameModels = { fileMenuModel, customizeOperationsModel,
			doOperationsModel, miscEditOperationsModel, elideOperationsModel,
			newEditorOperationsModel, undoRedoModel, refreshOperationsModel,
			selectionOperationsModel, windowOperationsModel,
			sourceOperationsModel };

	public void initFrameModels(Object obj) {
		for (int i = 0; i < frameModels.length; i++)
			frameModels[i].init(this, obj, null);
	}

	public void initFrameModels() {
		initFrameModels(topObject);
	}

	public AFileOperationsModel getFileMenuModel() {
		return fileMenuModel;

	}

	public void beginTransaction() {
		MethodInvocationManager.beginTransaction();
		if (getUndoer() != null)
			getUndoer().beginTransaction();

	}

	public void endTransaction() {
		MethodInvocationManager.endTransaction();
		if (getUndoer() != null)
			getUndoer().endTransaction();
	}

	public void addKeyListener(VirtualComponent component) {
//		no longer needed as we will use keyboard focus manager
//		getKeyShortCuts().addKeyListener(component);
		
	}

	public void keyPressed(KeyEvent event) {
		System.out.println("KeyPressed");
	}

	public void keyReleased(KeyEvent event) {
		System.out.println("KeyReleased");
	}

	public void keyTyped(KeyEvent event) {
		int keyChar = event.getKeyChar();
		System.out.println("KeyTyped:" + keyChar);
	}
	boolean fullRefreshOnEachOperation = false;
	public void setFullRefreshOnEachOperation(boolean newVal) {
		fullRefreshOnEachOperation = newVal;
	}
	public boolean getFullRefreshOnEachOperation() {
		return fullRefreshOnEachOperation;
	}

	boolean explicitRefresh;
	public boolean isExplicitRefresh() {
		return explicitRefresh;
	}

	public boolean isFullRefresh() {
		return 
//				!isProcessingSuppressedNotifications() && // by removing this condition this value we can see the effect of efficient notification processing
//				(
				explicitRefresh || fullRefreshOnEachOperation;
//				);
	}
	
	
	public void setExplicitRefresh(boolean explicitRefresh) {
		this.explicitRefresh = explicitRefresh;
	}
	// done in object registry, but it is static
	
//	Map<Object, ObjectAdapter> objectToObjectAdapter = new HashMap();
	IdentityMap<Object, ObjectAdapter> objectToObjectAdapter = new HashIdentityMap<Object, ObjectAdapter>();
	// Identity map is wrong here as we need equals test
	Map<String, ObjectAdapter> pathToObjectAdapter = new HashMap<String, ObjectAdapter>();
	

	public void putObjectAdapterOld(Object object, ObjectAdapter adapter) {
		if (adapter != null) {
		objectToObjectAdapter.put(object, adapter);
		pathToObjectAdapter.put(adapter.getCompletePathOnly(), adapter);
		} 
	}

	public void putObjectAdapter(Object object, ObjectAdapter adapter) {
//		if (adapter != null) {
//		objectToObjectAdapter.put(object, adapter);
//		pathToObjectAdapter.put(adapter.getCompletePathOnly(), adapter);
//		} 
	}
	public ObjectAdapter getObjectAdapter(Object object) {
		BasicObjectRegistry basicObjectRegistry = getBasicObjectRegistry();
		if (basicObjectRegistry == null)
			return null;
		return basicObjectRegistry.getObjectAdapter(object);
//		return objectToObjectAdapter.get(object);
	}
	// pathToObjectAdapter in ObjectAdapter is a more space efficient scheme
	public ObjectAdapter getObjectAdapterFromPath(String path) {
//		return pathToObjectAdapter.get(path);
//		return getTopAdapter().pathToObjectAdapter(path);
		return ObjectAdapter.pathToObjectAdapter(getTopAdapter(), path);
	}
		public Object getObjectFromPath(String path) {
			ObjectAdapter objectAdapter = getObjectAdapter(path);
			if (objectAdapter != null)
				return objectAdapter.getRealObject();
			else
				return null;
		}
	public  void select (Object object, int index) {
		  ObjectAdapter adapter = getObjectAdapter(object);
		  if (adapter == null )  {
			  SelectionOfInvisibleObject.newCase(object, this);
//			  Tracer.error("Selected object: " + object + " not displayed");
			  return;
		  }
		  if (! (adapter instanceof VectorAdapter)) {
			  SelectionOfIndexOfNonIndexedObject.newCase(object, this);
//			  Tracer.error("Selection of index of non indexed collection: " + object);
			  return;
		  }
		  ObjectAdapter childAdapter = ((VectorAdapter) adapter).getChildAdapterMapping(index);
		  if (childAdapter == null) {
			  SelectionIndexOutOfBounds.newCase(index, object, this);
//			  Tracer.error("Selected index " + index + " out of bounds.");
		  }
		  childAdapter.replaceSelectionsEvent();
	  }
	  public  void select (Object object, String property) {
		  ObjectAdapter adapter = getObjectAdapter(object);
		  if (adapter == null )  {
			  SelectionOfInvisibleObject.newCase(object, this);
//			  Tracer.error("Object: " + object + " not displayed");
			  return;
		  }
		  if (! (adapter instanceof ClassAdapter)) {
			  SelectionOfPropertyOfNonBean.newCase(property, object, this);
//			  Tracer.error("Selection of property of non Bean: " + object);
			  return;
		  }
		  ObjectAdapter childAdapter = ((ClassAdapter) adapter).getChildAdapterMapping(property);
		  if (childAdapter == null) {
			  Tracer.error("Selection of non existing:" + property + " of object:" + object);
			  return;
		  }
		  childAdapter.replaceSelectionsEvent();
	  }
	  @Override
	  public  void focus (Object object, String property) {
		  ObjectAdapter adapter = getObjectAdapter(object);
		  if (adapter == null)
			  return;
		  WidgetAdapterInterface widgetAdapter = adapter.getWidgetAdapter();
		  if (widgetAdapter == null) 
			  return;
		  if (widgetAdapter instanceof TabbedPaneAdapter) {
			  ((TabbedPaneAdapter) widgetAdapter).setSelectedProperty(property);
		  } else {
			  Tracer.error("Focus supported on tabbed displays only (at least for now");
		  }
		  
		  
	  }
	  
	  
	 public void addMethodInvocationFrameCreationListener (MethodInvocationFrameCreationListener aListener) {
		  if (methodInvocationFrameCreationListeners.contains(aListener))
			  return;
		  methodInvocationFrameCreationListeners.add(aListener);
	  }
	  
	 public  void notifyMethodInvocationFrameCreated (OEFrame aParentFrame, OEFrame anInvocationFrame, InteractiveMethodInvoker anInteractiveMethodInvoker) {
		  for (MethodInvocationFrameCreationListener aListener:methodInvocationFrameCreationListeners) {
			  aListener.methodInvocationFrameCreated(aParentFrame, anInvocationFrame, anInteractiveMethodInvoker);
		  }
	  }
	 public  void setGraphicsWindowLocked( boolean newVal) {
		  ShapesList drawing = getDrawing();
		  if (drawing ==null) return;
		  drawing.setLocked(newVal);
		  
	  }
	@Override
	public boolean isSuppressPropertyNotifications() {
		return suppressPropertyNotifications;
	}
	
	boolean processingSuppressedNotifications;
	
	public boolean isProcessingSuppressedNotifications() {
		return processingSuppressedNotifications;
	}

	public void setProcessingSuppressedNotifications(
			boolean processingSuppressedNotifications) {
		this.processingSuppressedNotifications = processingSuppressedNotifications;
	}

	@Override
	public void setSuppressPropertyNotifications(
			boolean newVal) {
		if (suppressPropertyNotifications == newVal)
			return;
		this.suppressPropertyNotifications = newVal;

		if (!newVal ) { // resuming notifications  
			processingSuppressedNotifications = true;
			doRefresh();
			processingSuppressedNotifications = false;

		}
	
		
	}
	  
	  

}
