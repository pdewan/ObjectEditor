package bus.uigen;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;

import shapes.FlexibleShape;
import shapes.FlexibleTextShape;
import util.annotations.Explanation;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.models.ADynamicEnum;
import util.models.AListenableVector;
import util.models.DynamicEnum;
import util.pipe.AConsoleModel;
import util.trace.Tracer;
import util.trace.TracingLevel;
import util.trace.uigen.CompositePropertyBind;
import util.trace.uigen.NoFramesForUndoBind;
import util.trace.uigen.PropertyMissingBoundComponent;
import util.trace.uigen.UnknownPropertyBind;
import bus.uigen.ars.AConsoleModelAR;
import bus.uigen.ars.AListenableVectorAR;
import bus.uigen.ars.ExceptionAR;
import bus.uigen.ars.MethodAR;
import bus.uigen.ars.ObjectAR;
import bus.uigen.ars.StringBuilderAR;
import bus.uigen.ars.ThreadAR;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.attributes.UIAttributeManager;
import bus.uigen.compose.ButtonCommand;
import bus.uigen.controller.AComponentInputter;
import bus.uigen.controller.AToolbarManager;
import bus.uigen.controller.ComponentInputter;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.controller.menus.MenuSetter;
import bus.uigen.controller.menus.VirtualMethodMenuItem;
import bus.uigen.controller.models.ADoOperationsModel;
import bus.uigen.controller.models.ASourceOperationsModel;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.ClassNameTable;
import bus.uigen.folders.ATextFileFolder;
import bus.uigen.folders.AnObjectFolder;
import bus.uigen.folders.PackageObject;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.AttributeRegistry;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.ClassIntrospectionFilterer;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.misc.OEMisc;
import bus.uigen.models.AComponentDrawer;
import bus.uigen.models.ComponentDrawer;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.HashtableAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.ObjectAdapterRegistry;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.sadapters.AbstractConcreteType;
import bus.uigen.undo.Inverses;
import bus.uigen.view.AStartView;
import bus.uigen.view.ATopViewManager;
import bus.uigen.view.OEFrameSelector;
import bus.uigen.view.TopViewManager;
import bus.uigen.viewgroups.APropertyAndCommandFilter;
import bus.uigen.viewgroups.APropertyAndCommandFilterAttributeRegistrer;
import bus.uigen.viewgroups.GenerateViewObject;
import bus.uigen.visitors.AddSelfAdapterVisitor;
import bus.uigen.widgets.ContainerFactory;
import bus.uigen.widgets.DesktopPaneSelector;
import bus.uigen.widgets.FrameSelector;
import bus.uigen.widgets.InternalFrameSelector;
import bus.uigen.widgets.LayoutManagerFactory;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.TabbedPaneSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDesktopPane;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.VirtualInternalFrame;
import bus.uigen.widgets.VirtualMenuItem;
import bus.uigen.widgets.VirtualScrollPane;
import bus.uigen.widgets.VirtualTabbedPane;
import bus.uigen.widgets.VirtualToolkit;
import bus.uigen.widgets.awt.AWTComponent;
import bus.uigen.widgets.awt.AWTContainer;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;
import bus.uigen.widgets.swing.SwingFrame;
import bus.uigen.widgets.swing.SwingFrameFactory;
import bus.uigen.widgets.swing.SwingInternalFrameFactory;
import bus.uigen.widgets.swing.SwingTabbedPane;
import bus.uigen.widgets.swing.SwingTable;
import bus.uigen.widgets.swing.SwingToolkit;
import bus.uigen.widgets.swing.SwingTree;
import bus.uigen.widgets.table.TableSelector;
import bus.uigen.widgets.table.VirtualTable;
import bus.uigen.widgets.tree.TreeSelector;
import bus.uigen.widgets.tree.VirtualTree;
import bus.uigen.widgets.universal.AUniversalWidget;


@util.annotations.Explanation("Tool to allow instantiation of classes.")
@util.annotations.Keywords({"ObjectEditor", "Preconditions.menus", "Preconditions.properties", "BeanInfo", "AttributeRegistrer"})
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ObjectEditor  implements java.io.Serializable, VirtualActionListener, Runnable   {
	static int frame_width = 700;
    static int frame_height = 150;
	static transient bus.uigen.uiFrame lastEditor;
	static  boolean shareBeans = false;
	static  boolean coupleElides = true;
	static boolean runUIComponentOnly = false; // if false, run both program and UI components
	static boolean unserializableOutput = false; // if true, serializable output
	static boolean colabMode = false;
	static ObjectEditor defaultObjectEditor; 
	public static final String TYPE_KEYWORD = "Type";
	public static final String APPLICATION_KEYWORD = "Application";
	public static final String TEXT_KEYWORD = APPLICATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "Text";
	public static final String GRAPHICS_KEYWORD = APPLICATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "Graphics";
	public static final String DOCUMENTATION_KEYWORD = "Documentation";
	public static final String PRECONDITION_DOCUMENTATION_KEYWORD = DOCUMENTATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "Precondition";
	public static final String PRECONDITION_PATTERN_KEYWORD = AbstractConcreteType.PROGRAMMING_PATTERN + AttributeNames.KEYWORD_SEPARATOR + "Precondition";
	public static final String VALIDATION_KEYWORD = DOCUMENTATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "Validation";
	public static final String ANNOTATION_KEYWORD = DOCUMENTATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "Annotation";
	public static final String EXPLANATION_ANNOTATION_KEYWORD = ANNOTATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "Description";
	public static final String HTML_DOCUMENTATION_ANNOTATION_KEYWORD = ANNOTATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "HTML Documentation";
	public static final String KEYWORDS_ANNOTATION_KEYWORD = ANNOTATION_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "Keywords";
	public static final String DESIGN_PATTERN_KEYWORD =  "Design Pattern";
	public static final String OBSERVER_PATTERN_KEYWORD = DESIGN_PATTERN_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "\"Observer\"";
	public static final String JAVA_OBSERVER_PATTERN_KEYWORD = OBSERVER_PATTERN_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "\"Java Observer\"";
	public static final String PROPERTY_LISTENER_KEYWORD = OBSERVER_PATTERN_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "\"Property Listener\"";
	public static final String VECTOR_LISTENER_KEYWORD = OBSERVER_PATTERN_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "\"Vector Listener";
	public static final String HASHTABLE_LISTENER_KEYWORD = OBSERVER_PATTERN_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "\"Hashtable Listener\"";
	public static final String TREE_LISTENER_KEYWORD = OBSERVER_PATTERN_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "\"Tree Listener\"";
	public static final String TABLE_LISTENER_KEYWORD = OBSERVER_PATTERN_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + "\"Table Listener\"";
	public static final String CONTROL_KEYWORD = "Control";
	public static final String CONDITIONAL_KEYWORD = "Conditional";
	public static final String LOOP_KEYWORD = "Loop";
	public static final String WIDGET_KEYWORD = "Widget";
	public static final String PROPERTY_ATTRIBUTES_KEYWORD = AttributeNames.ATTRIBUTES_KEYWORD  + AttributeNames.KEYWORD_SEPARATOR + "Property";
	public static final String METHOD_ATTRIBUTES_KEYWORD = AttributeNames.ATTRIBUTES_KEYWORD  + AttributeNames.KEYWORD_SEPARATOR + "Method";
	public static final String SUPPRESS_PROPERTY = "Suppress Property Notifications";
	static MenuSetter defaultMenuSetter = new MenuSetter(); // does not really make a difference
	/*
	private static uiFrame createNano(){
		fm3d.oeInterface.TrackerPanel trackerPanel = new fm3d.oeInterface.TrackerPanel();
		fm3d.oeInterface.BottomPanel bottomPanel = new fm3d.oeInterface.BottomPanel(trackerPanel);
		MenuSetter menuTest =  new MenuSetter();
		uiFrame uif = edit(bottomPanel, true, menuTest, trackerPanel);
		return uif;
	}
	*/
	// do nothing, just call static block
	public  static void initialize() {
		
	}
	
	public static boolean shareBeans() {
		return shareBeans;
	}
	public static boolean colabMode() {
		return colabMode;
	}
	public static boolean coupleElides() {
		return coupleElides;
	}
	public static void main(String args[]) throws Exception {
		int i;
		for(i=0;i<args.length;i++){
			if(args[i].equals("-uce")){ // Uncouple Elides
				coupleElides = false;
			} else if(args[i].equals("-uso")){ // Unserializable Output
				runUIComponentOnly = true;
				unserializableOutput = true;
			} else if(args[i].equals("-rsu")){ // run single uigen instance
				runUIComponentOnly = true;
			} else if(args[i].equals("-cm")){ 
				colabMode = true;
			} else{
				break;
			}
		}
		//String newArgs[] = new String[args.length-i];
		String newArgs[] = new String[args.length];
		for(int j=0;j<newArgs.length;i++,j++){
			newArgs[j] = args[i];
		}
		args = newArgs;
				
		if(args.length == 5 || args.length == 6){
		// why is there no special mode for shareBeans?
		//if(args.length == 6 || args.length == 7){
			shareBeans = true;
			if(runUIComponentOnly){
				System.out.println("Running a locally-non-replicated uigen instance: no dynamic program replication allowed");
			}
			if(unserializableOutput){
				System.out.println("Only Replicated sharing allowed");
				args[4] = "true";
			}
		}
		register();	
		loadStaticState();
		bus.uigen.uiFrame editor;
		if (args.length != 0) {
			try {
				//System.out.println(args.length);
				//objectClassName = args[0];
				System.out.println(args[0]);
				//internalNewInstance(toClassName(toCompleteName(toShortName(args[0]))));
				//processFile(toCompleteName(toShortName(args[0])));
				if( args.length == 5 ){
					/*
					if(args[0].equals("Nano")){
						uiFrame uif = createNano();
						uif.setVisible(false);
					} else{
					*/
						uiFrame uif = uiGenerator.generateUIFrame(Class.forName(args[0]).newInstance());
						if(runUIComponentOnly){
							uif.setVisible(true);
						} else{
							uif.setVisible(false);
						}
					/*
					}
					*/
				}
				else if (args.length == 1 || args.length == 6) {
					System.out.println("args 1");
					/*
					if(args[0].equals("Nano")){
						createNano();
					} else{
					*/
						if (args[0].indexOf('.') < 0){
						     internalNewInstance(args[0]);
						}
						else
						     processFile(toCompleteName(args[0]));
					/*
					}
					*/
				}
				else
					if (args.length == 2) {
						System.out.println("args 2");
						if (args[0].indexOf('.') < 0)
						     internalNewInstance(args[0], Boolean.getBoolean(args[1].trim()) );
						else
						     processFile(toCompleteName(args[0]));
					}
						
				//processFile(toCompleteName(args[0]));
				/*
				Class objectClass = Class.forName(args[0]);
				editor = bus.uigen.uiGenerator.generateUIFrame(objectClass.newInstance());
				editor.show();
				*/
				if(args.length == 5 || args.length == 6){ // 5 - program; 6 - ui
					String commandHeader = "java";
					String securityProperty = System.getProperty("java.security.policy");
					if(securityProperty!=null){
						commandHeader = commandHeader+" -Djava.security.policy="+securityProperty;
					}
					
					String loggerUIRMIName = "//localhost:1100/"+args[2]+"_"+args[0]+"_LoggerUI";
					
					if(args.length==5){
						String command = commandHeader+" logging.logger.LoggerUIStarter "+loggerUIRMIName;
						Process p = Runtime.getRuntime().exec(command,null);
					}
						
					String loggerUIRMI = null;
					// if output is unserializable, we will run just the replicated architecture, using only UI components
					boolean runProgramComponent = (args.length==5 && !runUIComponentOnly); // if false, run ui component
					if(!runProgramComponent) loggerUIRMI = loggerUIRMIName;
					if (ObjectEditor.colabMode())
					ObjectRegistry.createLoggerUI(args[0],args[1],args[2],args[3],args[4],runProgramComponent,loggerUIRMI);
					
					if(runProgramComponent){ // if we ran the program component, now run the UI Component
						String command = commandHeader+" bus.uigen.ObjectEditor -cm "+(coupleElides?"":"-uce ")+args[0]+" "+args[1]+" "+args[2]+" "+args[3]+" "+args[4]+" "+loggerUIRMIName;
						
						System.out.println("\n"+command+"\n");
						/*
						Process p = Runtime.getRuntime().exec(command,null);
						
						java.io.InputStream is = p.getInputStream();
						byte[] b = new byte[10];
						int num;
						while((num=is.read(b))>=0){
							for(int j=0;j<num;j++){
								System.out.print(((char)(b[j])));
							}
						}
						*/
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		} else {
			//System.out.println("count before frame generation" + Thread.activeCount());			
			//Thread.currentThread().getThreadGroup().list(); 
			ObjectEditor oe = new ObjectEditor();
			//editor = bus.uigen.uiGenerator.generateUIFrame(new ObjectEditor());
			//editor = bus.uigen.uiGenerator.generateUIFrame(oe);
			editor = (uiFrame) ObjectEditor.edit(oe);
			//editor = bus.uigen.uiGenerator.generateUIFrame(new shapes.RectangleModel(new java.awt.Rectangle(100, 100, 100, 100)));
			//editor.pack();			
			lastEditor = editor;
			oe.setFrame(editor);
			editor.setSize(frame_width, frame_height);
			//editor.pack();
			/*
			System.out.println("count after frame creation" + Thread.activeCount());
			Thread.currentThread().getThreadGroup().list();
			*/
			editor.show();
			/*
			Thread.currentThread().getThreadGroup().list();
			System.out.println("count after frame display" + Thread.activeCount());
					
			Thread[] threads = new Thread[10];
			int newThreadCnt = Thread.currentThread().getThreadGroup().enumerate(threads);
			int oldCnt = 0;
			int newCnt;
			int newAGCnt;
			int oldAGCnt = 0;
			//Object retVal = ObjectEditor.syncEdit(new Integer(6));
			//System.out.println("Returned value" + retVal);
			for (;;) {
				newCnt = Thread.activeCount();
				if (oldCnt != newCnt) {
					System.out.println("count changed " + newCnt);
					Thread.currentThread().getThreadGroup().list();
				}
				newAGCnt = Thread.currentThread().getThreadGroup().activeGroupCount();
				if (oldAGCnt != newAGCnt)
					System.out.println("active count changed" + newAGCnt);
				oldCnt = newCnt;
			}
			*/
		}
		
		
	}
	 public static void registerDefaultParameterValues(MethodProxy m, Object[] parameterValues) {
	 	MethodInvocationManager.registerDefaultParameterValues(m, parameterValues);
	 
	 }
	 public static void registerDefaultParameterValues(Method m, Object[] parameterValues) {
		 	MethodInvocationManager.registerDefaultParameterValues(m, parameterValues);
		 
		 }
	 public static void registerParameterNames(MethodProxy m, String[] parameterNames) {
		 	MethodInvocationManager.registerParameterNames(m, parameterNames);
		 
		 }
		 public static void registerParameterNames(Method m, String[] parameterNames) {
			 	MethodInvocationManager.registerParameterNames(m, parameterNames);
			 
			 }
	public static void previousMainToBeDeleted(String args[]) throws Exception {		
		register();	
	    loadStaticState();
		bus.uigen.uiFrame editor;
		if (args.length != 0) {
			try {
				//System.out.println(args.length);
				//objectClassName = args[0];
				System.out.println(args[0]);
				//internalNewInstance(toClassName(toCompleteName(toShortName(args[0]))));
				//processFile(toCompleteName(toShortName(args[0])));
				if (args.length == 1) {
					System.out.println("args 1");
					if (args[0].indexOf('.') < 0)
					     internalNewInstance(args[0]);
					else
					     processFile(toCompleteName(args[0]));
				}
				else
					if (args.length == 2) {
						System.out.println("args 2");
						if (args[0].indexOf('.') < 0)
						     internalNewInstance(args[0], Boolean.getBoolean(args[1].trim()) );
						else
						     processFile(toCompleteName(args[0]));
				}
						
				//processFile(toCompleteName(args[0]));
				/*
				Class objectClass = Class.forName(args[0]);
				editor = bus.uigen.uiGenerator.generateUIFrame(objectClass.newInstance());
				editor.show();
				*/
				
			} catch (Exception e) {
				System.out.println(e);
				
			}
		} else {
			//System.out.println("count before frame generation" + Thread.activeCount());			
			//Thread.currentThread().getThreadGroup().list(); 
			ObjectEditor oe = new ObjectEditor();
			//editor = bus.uigen.uiGenerator.generateUIFrame(new ObjectEditor());
			editor = bus.uigen.uiGenerator.generateUIFrame(oe);
			//editor = bus.uigen.uiGenerator.generateUIFrame(new shapes.RectangleModel(new java.awt.Rectangle(100, 100, 100, 100)));
			//editor.pack();			
			lastEditor = editor;
			oe.setFrame(editor);
			editor.setSize(frame_width, frame_height);
			//editor.pack();
			/*
			System.out.println("count after frame creation" + Thread.activeCount());
			Thread.currentThread().getThreadGroup().list();
			*/
			editor.show();
			/*
			Thread.currentThread().getThreadGroup().list();
			System.out.println("count after frame display" + Thread.activeCount());
					
			Thread[] threads = new Thread[10];
			int newThreadCnt = Thread.currentThread().getThreadGroup().enumerate(threads);
			int oldCnt = 0;
			int newCnt;
			int newAGCnt;
			int oldAGCnt = 0;
			//Object retVal = ObjectEditor.syncEdit(new Integer(6));
			//System.out.println("Returned value" + retVal);
			for (;;) {
				newCnt = Thread.activeCount();
				if (oldCnt != newCnt) {
					System.out.println("count changed " + newCnt);
					Thread.currentThread().getThreadGroup().list();
				}
				newAGCnt = Thread.currentThread().getThreadGroup().activeGroupCount();
				if (oldAGCnt != newAGCnt)
					System.out.println("active count changed" + newAGCnt);
				oldCnt = newCnt;
			}
			*/
		}
		
		
	}
	
	static final String SOURCE_FILE_SUFFIX = ".java";
	static final String CLASS_FILE_SUFFIX = ".class";
	static final String OBJECT_FILE_SUFFIX = ".obj";
	static final String TEXT_FILE_SUFFIX = ".txt";
	
	static void processFile(String fileName) {
		if (fileName.endsWith(CLASS_FILE_SUFFIX))
			processClassFile(fileName);
		else if (fileName.endsWith(OBJECT_FILE_SUFFIX))
			processObjectFile(fileName);
		else if (fileName.endsWith(TEXT_FILE_SUFFIX))
			processTextFile(fileName);
	}
	static public Object processClassFile(String fileName) {
		return internalNewInstance(toClassName(fileName));
	}
	static public void processObjectFile(String fileName) {
		File file = new File (fileName);
		uiFrame f = uiGenerator.generateUIFrameFromFile(file.getAbsolutePath());		
		f.setVisible(true);
		//System.out.println("setting true");
	}
	static public void processTextFile(String fileName) {
		File file = new File (fileName);
		ATextFileFolder.open(file);
	}
	transient static boolean editorsRegistered = false;
	transient static boolean structureFactoriesRegistered = false;
	transient static boolean initializedStatic = false;
	transient static boolean showStartView = true;
	
	public static boolean isShowStartView() {
		return showStartView;
	}
	public static void setShowStartView(boolean showStartView) {
		ObjectEditor.showStartView = showStartView;
	}
	public static void initStatic() {
		if (initializedStatic)
			return;
		if (showStartView)
		new AStartView();
//		VirtualToolkit.setDefaultToolkit(new SwingToolkit());
//		AClassDescriptor.addListener(new AClassDescriptorListener());
//		register();
	}
	
	static {
//		new AStartView();
//		VirtualToolkit.setDefaultToolkit(new SwingToolkit());
//		AClassDescriptor.addListener(new AClassDescriptorListener());
//		register();
		VirtualToolkit.setDefaultToolkit(new SwingToolkit());
		AClassDescriptor.addListener(new AClassDescriptorListener());
		register();
		
	}
	public static void register() {
		//registerInverses();
		//registerEditors();	
		//registerVirtualComponentClasses();
		//registerWidgets();
		//ObjectAdapterRegistry.registerObjectAdapterFactories();		
		//registerStructureFactories();
		//connectObjectAdapterFactoriesAndConcreteTypes();
		//MenuDescriptorRegistry.registerMenuDescriptorSetters();
		//registerModels();
		//registerClassAttributes();
		//registerAttributes();
	}
	transient static boolean inversesRegistered = false;
	public static void registerInverses() {
		if (inversesRegistered) return;
		Inverses.init();
		inversesRegistered = true;
	}
	transient static boolean modelsRegistered = false;
	public static void registerModels() {
		if (modelsRegistered) return;
		//FrameModelRegistry.putDefault(uiFrame.FILE_MENU_NAME, new AFileOperationsModel());
		//FrameModelRegistry.putDefault(FrameModelRegistry.UNDO_REDO_MODEL_NAME, new AnUndoRedoModel());
		//FrameModelRegistry.putDefault(FrameModelRegistry.DO_MODEL_NAME, new ADoOperationsModel());
		//FrameModelRegistry.putDefault(FrameModelRegistry.SELECTION_MODEL_NAME, new ASelectionOperationsModel());
		//FrameModelRegistry.putDefault(FrameModelRegistry.MISC_EDIT_OPERATIONS_MODEL_NAME, new AMiscEditOperationsModel());
		//FrameModelRegistry.putDefault(FrameModelRegistry.REFRESH_OPERATIONS_MODEL_NAME, new ARefreshOperationsModel());
	}
	transient static boolean menuDescriptorSetterRegistered = false;
	transient static boolean attributesRegistered = false;
	public static void registerAttributes() {
		if (attributesRegistered) return;
		attributesRegistered = true;
		//if (AttributeRegistry.get(APropertyAndCommandFilter.class) == null)
		AttributeRegistry.setAttributeRegisterer(AClassProxy.classProxy(AListenableVector.class), AClassProxy.classProxy(AListenableVectorAR.class));

		AttributeRegistry.setAttributeRegisterer(AClassProxy.classProxy(AConsoleModel.class), AClassProxy.classProxy(AConsoleModelAR.class));
		AttributeRegistry.setAttributeRegisterer(AClassProxy.classProxy(StringBuilder.class), AClassProxy.classProxy(StringBuilderAR.class));

		AttributeRegistry.setAttributeRegisterer(AClassProxy.classProxy(java.awt.Point.class), AClassProxy.classProxy(bus.uigen.ars.PointAR.class));

//ObjectEditor.setPropertyVisible(java.awt.Point.class, "Location", false);
//		ObjectEditor.setPropertyVisible(java.awt.Rectangle.class, "Bounds", false);
//		ObjectEditor.setPropertyVisible(java.awt.Rectangle.class, "Frame", false);
//		ObjectEditor.setPropertyVisible(java.awt.Rectangle.class, "Bounds2D", false);
		AttributeRegistry.setAttributeRegisterer(AClassProxy.classProxy(java.awt.Rectangle.class), AClassProxy.classProxy(bus.uigen.ars.RectangleAR.class));
		AttributeRegistry.setAttributeRegisterer(AClassProxy.classProxy(java.awt.Dimension.class), AClassProxy.classProxy(bus.uigen.ars.DimensionAR.class));

//		ObjectEditor.setVisible(java.awt.Dimension.class,  false);
//		ObjectEditor.setPropertyAttribute(ObjectEditor.class, "currentClassName", AttributeNames.COMPONENT_WIDTH, 250);
		//ObjectEditor.setPreferredWidget(ObjectEditor.class, "classNameList", JTree.class);
		//ObjectEditor.setLabelled(ClassNameList.class, false);
//		ObjectEditor.setPropertyAttribute(ClassNameTree.class, "element", AttributeNames.LABELLED, false);		
		//ObjectEditor.setAttribute(Double.class, AttributeNames.COMPONENT_WIDTH, 200);
		//ObjectEditor.setAttribute(Integer.class, AttributeNames.COMPONENT_WIDTH, 100);
		//ObjectEditor.setAttribute(Boolean.class, AttributeNames.COMPONENT_WIDTH, 100);
		//ObjectEditor.setAttribute(String.class, AttributeNames.COMPONENT_WIDTH, 200);
		
		// SLModel never figures, so why do we need this?
		
//		ObjectEditor.setAttribute(RemoteSelector.classProxy(slm.SLModel.class), AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(RemoteSelector.classProxy(slm.ShapesList.class), AttributeNames.TOOLBAR_METHOD, new Boolean(false));
			AttributeRegistry.putDefault(RemoteSelector.classProxy(APropertyAndCommandFilter.class), new APropertyAndCommandFilterAttributeRegistrer());
			AttributeRegistry.putDefault(RemoteSelector.classProxy(Object.class), new ObjectAR());
			AttributeRegistry.putDefault(RemoteSelector.classProxy(Thread.class), new ThreadAR());
			AttributeRegistry.putDefault(RemoteSelector.classProxy(Method.class), new MethodAR());
			AttributeRegistry.putDefault(RemoteSelector.classProxy(Exception.class), new ExceptionAR());




		//if (AttributeRegistry.get(AListenableHashtable.class) == null)
//			AttributeRegistry.putDefault(RemoteSelector.classProxy(AListenableHashtable.class), new AListenableHashtableAttributeRegisterer());
		//if (AttributeRegistry.get(AFileOperationsModel.class) == null)
//			AttributeRegistry.putDefault(RemoteSelector.classProxy(FrameModel.class), new AFrameModelAttributeRegisterer());
			//AttributeRegistry.putDefault(AFileOperationsModel.class, new AFileOperationsModelAttributeRegistrer());
			//AttributeRegistry.putDefault(AnUndoRedoModel.class, new AnUndoRedoModelAtttributeRegistrer());
			//AttributeRegistry.putDefault(ADoOperationsModel.class, new ADoOperationsModelAtttributeRegistrer());
			//AttributeRegistry.putDefault(ASelectionOperationsModel.class, new ASelectionOperationsModelAtttributeRegistrer());
			//AttributeRegistry.putDefault(AMiscEditOperationsModel.class, new AMiscEditOperationsModelAtttributeRegistrer());
			AttributeRegistry.registerAll();
		/*
		ObjectEditor.setAttribute(APropertyAndCommandFilter.class, AttributeNames.ONLY_DYNAMIC_METHODS, new Boolean (true));
		ObjectEditor.setAttribute(APropertyAndCommandFilter.class, AttributeNames.ONLY_DYNAMIC_PROPERTIES, new Boolean (true));
		ObjectEditor.setPreferredWidget(AListenableHashtable.class, JTable.class);
		*/
		
	}
	transient static boolean virtualClassesRegistered = false;
	public static void registerVirtualComponentClasses() {
		if (virtualClassesRegistered) return;
		AUniversalWidget.registerUniversalWidgetClasses();
		virtualClassesRegistered = true;
		
		
	}
	transient static boolean widgetsRegistered = false;
	public static void registerWidgets() {
		if (widgetsRegistered) return;
		ComponentDictionary.registerDefaults();
		/*
		FrameSelector.setFrameFactory(new SwingFrameFactory());
		//ScrollPaneSelector.setScrollPaneFactory(new AWTScrollPaneFactory());
		ScrollPaneSelector.setScrollPaneFactory(new SwingScrollPaneFactory());
		WidgetShellSelector.setWidgetShellFactory(new JPanelWidgetShellFactory());
		*/
		widgetsRegistered = true;
	}
	
	transient static boolean objectAdapterFactoriesRegistered = false;
	transient static boolean objectAdapterFactoriesAndConcreteTypesConnected = false;
	static void connectObjectAdapterFactoriesAndConcreteTypes () {
		if (objectAdapterFactoriesAndConcreteTypesConnected) return;
		ObjectAdapterRegistry.connectObjectAdapterFactoriesAndConcreteTypes();
		objectAdapterFactoriesAndConcreteTypesConnected = true;
	}
	
	
	public static synchronized void treeEdit(uiFrame frame, Object obj) {				frame.createTreePanel(obj);				
		//Container treeContainer = frame.newTreeContainer();		//if (treeContainer == null) return null;
		/*		Container treeComponent = frame.treeComponent();
		uiObjectAdapter treeRoot = uiGenerator.generateInUIPanel(frame, obj, null,   null, treeContainer, null, treeComponent);		frame.setTreeRoot(treeRoot);		*/
		//return treeContainer;				}
	public static synchronized uiFrame drawEdit(Object obj) {
		VirtualFrame frame = FrameSelector.createFrame();		
		uiFrame editor = OEFrameSelector.createFrame(frame, frame.getContentPane());
//		VirtualContainer drawContainer = PanelSelector.createPanel();
//		VirtualScrollPane spane = ScrollPaneSelector.createScrollPane(drawContainer);
//		frame.getContentPane().add(spane);
		editor.setDrawingContainer(frame.getContentPane(), true);
		VirtualContainer dummyMain = null;
		if (!editor.isGlassPane)
			dummyMain = PanelSelector.createPanel();
//		editor.setOnlyGraphicsPanel(true);

//		editor.setOnlyGraphicsPanel(true);
//		VirtualContainer dummyMain = PanelSelector.createPanel();
		editInContainer(editor, obj, dummyMain); 
//		return editInDrawingContainer(obj, drawContainer);
		editor.setSize();
		editor.setVisible(true);
		return editor;

			
	}
	public static synchronized uiFrame graphicsOnlyEdit(Object obj) {
		VirtualFrame frame = FrameSelector.createFrame();		
		uiFrame editor = OEFrameSelector.createFrame(frame, frame.getContentPane());
		frame.getContentPane().setBackground(AttributeNames.CAROLINA_BLUE);
		editInDrawingContainer(editor, obj, frame.getContentPane(), false, true);

		frame.setSize(ATopViewManager.FRAME_WIDTH, ATopViewManager.FRAME_HEIGHT);

		editor.setVisible(true);
		return editor;

			
	}
	
	public static synchronized uiFrame graphicsEdit(Object obj) {
		uiFrame frame = (uiFrame) edit(obj);
		frame.hideMainPanel();
		frame.showDrawPanel();
		return frame;
	}
//	public static uiFrame editInContainer(	Object object, Container aContainer) {
//		return editInContainer (object, AWTContainer.virtualContainer(aContainer));
//	}
//
//public static uiFrame editInContainer(	Object object, VirtualContainer aContainer) {
//		
//	register();
//	//uiFrame frame = uiFrameSelector.createFrame(obj);
//	VirtualFrame frame = FrameSelector.createFrame();
//	uiFrame editor = OEFrameSelector.createFrame(frame, frame.getContentPane());
//	editor.setIsDummy(true);
//	//frame.createTreePanel(obj);
//	//spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//	//spane.setScrolledComponent(tree);
//	//spane.add(tree);
//	//frame.getContentPane().add(tree);
//	//uiFrame editor = edit(frame, frame.getContentPane(), obj, tree);
//	uiGenerator.generateInUserContainer(editor, object, aContainer, false);
//	//uiGenerator.generateInUserContainer(editor, obj, tree, true);
//	/*
//	uiObjectAdapter adapter = uiGenerator.generateInUIPanel (editor, obj, null, null, null, null, tree);
//	   editor.addUserAdapter(adapter);
//	   editor.setTitle();
//	   */	
//	
//	return editor;
//	
//	//Container treeContainer = frame.newTreeContainer();
//	//if (treeContainer == null) return null;
//	/*
//	Container treeComponent = frame.treeComponent();
//	uiObjectAdapter treeRoot = uiGenerator.generateInUIPanel(frame, obj, null,   null, treeContainer, null, treeComponent);
//	frame.setTreeRoot(treeRoot);
//	*/
//	//return treeContainer;
//
//	}



     
	public static synchronized uiFrame treeEdit(Object obj) {
		register();
		//uiFrame frame = uiFrameSelector.createFrame(obj);
		VirtualFrame frame = FrameSelector.createFrame();
		uiFrame editor = OEFrameSelector.createFrame(frame, frame.getContentPane());		//frame.createTreePanel(obj);
		VirtualTree tree = TreeSelector.createTree();		
		VirtualScrollPane spane = ScrollPaneSelector.createScrollPane(tree);
		//spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//spane.setScrolledComponent(tree);
		//spane.add(tree);
		//frame.getContentPane().add(tree);
		frame.getContentPane().add(spane);
		//uiFrame editor = edit(frame, frame.getContentPane(), obj, tree);		uiGenerator.generateInUserContainer(editor, obj, tree, false);
		//uiGenerator.generateInUserContainer(editor, obj, tree, true);
		/*
		uiObjectAdapter adapter = uiGenerator.generateInUIPanel (editor, obj, null, null, null, null, tree);
		   editor.addUserAdapter(adapter);
		   editor.setTitle();
		   */	
		editor.setSize();
		editor.setVisible(true);
		return editor;		
		//Container treeContainer = frame.newTreeContainer();		//if (treeContainer == null) return null;
		/*		Container treeComponent = frame.treeComponent();
		uiObjectAdapter treeRoot = uiGenerator.generateInUIPanel(frame, obj, null,   null, treeContainer, null, treeComponent);		frame.setTreeRoot(treeRoot);		*/
		//return treeContainer;				}
	public static synchronized uiFrame tableEdit (Object object) {
		return tableEdit(object, null, null);
	}
	static Hashtable toAttributes(java.util.List<String> labels, Boolean indented) {
		if (labels == null) return null;
		Hashtable retVal = new Hashtable();		
		retVal.put(AttributeNames.COLUMN_LABELS, labels);
		if (indented != null)
			retVal.put(AttributeNames.ROWS_LABELLED, indented);
		return retVal;
	}
	public static void commitCommands(uiFrame editor) {
		getSourceModel(editor).init(editor, editor.getObject(), null);
		editor.checkPreInMenuTreeAndButtonCommands();
	}
	public static synchronized uiFrame tableEdit (Object object, java.util.List<String> labels) {
		return tableEdit(object, labels, false);
		
	}
	public static ObjectAdapter toObjectAdapter(Object obj) {
		ObjectAdapter retVal = uiGenerator.toTopAdapter(obj);
		uiGenerator.deepCreateChildren(retVal, false);
		return retVal;
	}
	public static boolean isHeadless() {
		String headlLessProperty = System.getProperty("java.awt.headless");
		return "true".equals(headlLessProperty) || GraphicsEnvironment.isHeadless();
	}
	public static ObjectAdapter toObjectAdapter(uiFrame theFrame, Object obj) {
		return uiGenerator.toTopAdapter(theFrame, obj);
	}
	public static synchronized uiFrame tableEdit (Object object, java.util.List<String> labels, Boolean indent) {
		//Object labelled = ObjectEditor.getDefaultAttribute(AttributeNames.LABELLED);
		//ObjectEditor.setDefaultAttribute(AttributeNames.LABELLED, false);
		//register();
		uiFrame frame = OEFrameSelector.createFrame(object);		
		frame.setSelfAttributes(toAttributes(labels, indent));
		//Container c = ScrollPaneSelector.createScrollPane();
		
		VirtualContainer c = PanelSelector.createPanel();	
		c.setLayout(new BorderLayout());
		
		//JTable jTable = new JTable();
		VirtualTable jTable = TableSelector.createTable();
		
		c.add(jTable);
		c.add(jTable.getTableHeader(), BorderLayout.NORTH);	
		frame.setSize(300, 200);
		frame.getContainer().add(c);
//		frame.setVisible(true);
		uiGenerator.generateInBrowsableContainer(frame, object, c,  jTable);
		frame.setVisible(true);

		return frame;
		//return setModel (object, jTable, labels);
		
		//frame.addKeyListener(frame.getContainer());
		//frame.addKeyListener(jTable);
		//uiGenerator.generateInBrowsableContainer(frame, object, (VirtualContainer) null,  jTable);
		 
		//uiGenerator.generateInBrowsableContainer(frame, object, c,  jTable);
		/*
		//uiGenerator.generateInBrowsableContainer(frame, object, c,  c);
		
		VirtualContainer childPanel = frame.createNewChildPanelInNewScrollPane();
		VirtualTable jTable = TableSelector.createTable();
	     //uiObjectAdapter adapter = generateInBrowsableContainer(theTopFrame, obj, lman,  sourceAdapter,  childPanel, title);
	      uiObjectAdapter retVal =  uiGenerator.generateInUIPanel(frame, object, null, null, childPanel, null, jTable);
	      */
		
		//frame.setSize();
		//frame.setVisible(true);
		//retVal.setSize(300, 200);
		//return frame;
	}
	public static synchronized uiFrame setModel (Object object, VirtualContainer c, java.util.List<String> labels, Boolean indent) {
		/*
		register();
		Object labelled = ObjectEditor.getDefaultAttribute(AttributeNames.LABELLED);
		ObjectEditor.setDefaultAttribute(AttributeNames.LABELLED, false);
		Object[] objects = {object};
		VirtualContainer[] containers = {c};
		Vector<java.util.List<String>> attributeList = new Vector();
		attributeList.add(labels);
		uiFrame retVal = edit (objects, containers, attributeList, false);
		ObjectEditor.setDefaultAttribute(AttributeNames.LABELLED, labelled);
		return retVal;
		*/
		return setModel(null, object, c, labels, indent);
		
	}
	public static synchronized uiFrame setModel (uiFrame frame, Object object, VirtualContainer c, java.util.List<String> labels, Boolean indent) {
		register();
		//uiFrame frame = uiFrameSelector.createFrame(object);		
		//frame.setSelfAttributes(toAttributes(labels));
		//Object labelled = ObjectEditor.getDefaultAttribute(AttributeNames.LABELLED);
		//ObjectEditor.setDefaultAttribute(AttributeNames.LABELLED, false);
		Object[] objects = {object};
		VirtualContainer[] containers = {c};
		Vector<java.util.List<String>> labelsList = new Vector();
		labelsList.add(labels);
		Vector<Boolean> indentList = new Vector();
		indentList.add(indent);
		uiFrame retVal = edit (frame, objects, containers, labelsList, indentList, false);
		//ObjectEditor.setDefaultAttribute(AttributeNames.LABELLED, labelled);
		return retVal;
		
	}
	
	public static synchronized uiFrame setModel (uiFrame frame, Object object, Container c, Class elementClass, String[] properties, String[] labels, Boolean indent) {
		if (properties != null) {
			ObjectEditor.setVisibleAllProperties(elementClass, false);
		//frame.setDefaultAttribute(AttributeNames.VISIBLE, false);
		for (int index = 0; index < properties.length; index++) {
			ObjectEditor.setPropertyAttribute(elementClass, properties[index], AttributeNames.VISIBLE, true);
		}
		}
		Vector<String> labelVector =  util.misc.Common.deepArrayToVector(labels);	
		VirtualContainer vc = AWTContainer.virtualContainer(c);
		return setModel(frame,  object,  vc,  labelVector,  indent);
		
	}
	
	public static void setDisplayedProperties(Class elementClass, String[] properties, String[] labels ) {
		if (properties == null) return;
			ObjectEditor.setVisibleAllProperties(elementClass, false);
			ObjectEditor.setLabelledAllProperties(elementClass, false);
		//frame.setDefaultAttribute(AttributeNames.VISIBLE, false);
		for (int index = 0; index < properties.length; index++) {			
			ObjectEditor.setPropertyAttribute(elementClass, properties[index], AttributeNames.VISIBLE, true);
			ObjectEditor.setPropertyAttribute(elementClass, properties[index], AttributeNames.POSITION, index);
			if (labels != null && index < labels.length) {
				ObjectEditor.setPropertyAttribute(elementClass, properties[index], AttributeNames.LABEL, labels[index]);
				ObjectEditor.setPropertyAttribute(elementClass, properties[index], AttributeNames.LABELLED, true);
			}
		}
		
		
	}
	
	
	public static synchronized uiFrame setModel (uiFrame frame, Object object, VirtualContainer c, java.util.List<String> labels, boolean indent) {
		register();
		//uiFrame frame = uiFrameSelector.createFrame(object);		
		//frame.setSelfAttributes(toAttributes(labels));
		//Object labelled = ObjectEditor.getDefaultAttribute(AttributeNames.LABELLED);
		//ObjectEditor.setDefaultAttribute(AttributeNames.LABELLED, false);
		Object[] objects = {object};
		VirtualContainer[] containers = {c};
		Vector<java.util.List<String>> labelList = new Vector();
		labelList.add(labels);
		Vector<Boolean> indentList = new Vector();
		indentList.add(indent);
		uiFrame retVal = edit (frame, objects, containers, labelList, indentList, false);
		//ObjectEditor.setDefaultAttribute(AttributeNames.LABELLED, labelled);
		return retVal;
		
	}
	public static synchronized uiFrame setModel (Object object,Container c, java.util.List<String> labels) {
		VirtualContainer vc = AWTContainer.virtualContainer(c);
		return setModel (object, vc, labels, false);
		
	}
	public static synchronized uiFrame setModel (Object object,Container c, java.util.List<String> labels, Boolean indent) {
		VirtualContainer vc = AWTContainer.virtualContainer(c);
		return setModel (object, vc, labels, indent);
		
	}
	public static synchronized uiFrame tabEdit (Object object) {
		register();
		uiFrame frame = OEFrameSelector.createFrame(object);
		frame.setManualMainContainer(true);
		//Container c = ScrollPaneSelector.createScrollPane();
		VirtualContainer c = PanelSelector.createPanel();	
		c.setLayout(new BorderLayout());
	
		VirtualTabbedPane jTabbedPane = TabbedPaneSelector.createTabbedPane();
//		c.add(jTabbedPane);
//		frame.getContainer().add(c);
		frame.getContainer().add(jTabbedPane);
		uiGenerator.generateInBrowsableContainer(frame, object, c,  jTabbedPane);
		
		frame.setSize();
		frame.setVisible(true);
		return frame;
	}
	
	public static synchronized CompleteOEFrame textEdit(Object object) {
		boolean prevMode = uiGenerator.textMode();
		uiGenerator.setTextMode (true);
		uiFrame editor = (uiFrame) ObjectEditor.edit(object);
		uiGenerator.setTextMode( prevMode);	
		return editor;
	}	
	public static synchronized uiFrame desktopEdit (Object object) {
		//register();
		uiFrame frame = OEFrameSelector.createFrame(object);
		//Container c = ScrollPaneSelector.createScrollPane();
		//VirtualContainer c = PanelSelector.createPanel();	
		//c.setLayout(new BorderLayout());
		//JTable jTable = new JTable();
		//JTabbedPane jTabbedPane = new JTabbedPane();
		VirtualDesktopPane jDesktopPane = DesktopPaneSelector.createDesktopPane();
		frame.getFrame().setContentPane(jDesktopPane);
		jDesktopPane.setLayout(new GridLayout());
		//frame.getContainer().add(c);
		uiGenerator.generateInBrowsableContainer(frame, object, jDesktopPane,  jDesktopPane);
		
		frame.setSize();
		frame.setVisible(true);
		//retVal.setSize(300, 200);
		return frame;
	}
	
	public static uiFrame treeBrowse (Object parentObject, Object childObject) {
		return treeBrowse(parentObject, childObject, false);
		/*
		register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(childObject);
		retVal.setLayout(new BorderLayout());
		retVal.createTreePanel(parentObject);
		retVal.setSize();
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
		*/
	}
	public static uiFrame treeBrowse (Object parentObject, Object childObject, boolean showMenus) {
		//register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(childObject, null, showMenus, defaultMenuSetter, new AMenuDescriptor() );
		retVal.setLayout(new BorderLayout());
		retVal.createTreePanel(parentObject);
		retVal.setSize();
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	public static uiFrame treeBrowse (Object parentObject, boolean showMenus) {
		return treeBrowse(parentObject, "", showMenus);
	}
	public static uiFrame treeBrowse (Object parentObject) {
		return treeBrowse(parentObject, "");
	}
	public static uiFrame browse (Object parentObject, boolean showMenus) {
		return browse (parentObject, uiFrame.RIGHT_WINDOW_MESSAGE, showMenus);
	}
	public static uiFrame browse (Object parentObject, Object childObject) {
		/*
		register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(parentObject);
		retVal.internalElideTopChildren();
		retVal.createSecondaryScrollPane(childObject);
		retVal.setSize();
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
		*/
		return browse (parentObject, childObject, true);
	}
	public static uiFrame browse (Object parentObject, Object childObject, boolean showMenus) {
		//register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(parentObject, null, showMenus, defaultMenuSetter, new AMenuDescriptor());
		retVal.internalElideTopChildren();
		retVal.newScrollPaneRight(childObject);
		//retVal.createSecondaryScrollPane(childObject);
		retVal.setSize();
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	public static uiFrame browse (Object parentObject, Object childObject, boolean showMenus, uiFrame theFrame) {
		//register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(parentObject, null, showMenus, defaultMenuSetter, new AMenuDescriptor());
		retVal.internalElideTopChildren();
		retVal.newScrollPaneRight(childObject);
		//retVal.createSecondaryScrollPane(childObject);
		retVal.setSize();
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	public static uiFrame treeBrowse (Object rootObject, Object parentObject, Object childObject) {
		//register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(parentObject);
		//retVal.internalElideTopChildren();
		retVal.createTreePanel(rootObject);
		retVal.createSecondaryScrollPane(childObject);
		retVal.validate();
		retVal.setSize();
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	public static uiFrame treeBrowse (Object rootObject, Object parentObject, Object childObject, boolean showMenus) {
		register();
		//uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(parentObject);
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(parentObject, null, showMenus, defaultMenuSetter, new AMenuDescriptor());
		//retVal.internalElideTopChildren();
		retVal.createTreePanel(rootObject);
		retVal.createSecondaryScrollPane(childObject);
		retVal.validate();
		retVal.setSize();
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	
	public static uiFrame browse (Object rootObject, Object parentObject, Object childObject) {
		//register();
		//uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(rootObject);
		uiFrame retVal = (uiFrame) ObjectEditor.edit(rootObject);
		//retVal.toggleElide (retVal.getAdapter(), 2);
		retVal.internalElideTopChildren();
		retVal.validate();
		retVal.setSize();
		retVal.setVisible(true);
		ObjectAdapter parentAdapter = retVal.newWindowRight(parentObject);
		//retVal.toggleElide (parentAdapter, 2);
		//retVal.createTreePanel(rootObject);
		retVal.createSecondaryScrollPane(childObject);
		retVal.internalElideTopChildren(parentAdapter);
		retVal.validate();
		//retVal.setSize();
		//retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	
	public static uiFrame browse (VirtualFrame frame, VirtualContainer frameContainer,
						   Object root, VirtualContainer rootContainer,
						   Object parent, VirtualContainer parentContainer,
						   Object child, VirtualContainer childContainer) {
		/*
		uiFrame uiF = FrameSelector.createFrame(frame, frameContainer);
		//Frame frame = uiF.getFrame();	
		ObjectEditor.editInBrowsableContainer(uiF, root, rootContainer);
		ObjectEditor.editInBrowsableContainer(uiF, parent, parentContainer);
		ObjectEditor.editInBrowsableContainer(uiF, child, childContainer);
		uiF.validate();
		uiF.setVisible(true);
		return uiF;
		*/
		Object objects[] = {root, parent, child};
		VirtualContainer containers[] = {rootContainer, parentContainer, childContainer};
		return browse (frame, frameContainer, objects, containers);
	}
	public static uiFrame browse (VirtualFrame frame, VirtualContainer frameContainer,
						   Object parent, VirtualContainer parentContainer,
						   Object child, VirtualContainer childContainer) {
		/*
		uiFrame uiF = FrameSelector.createFrame(frame, frameContainer);
		//Frame frame = uiF.getFrame();	
		ObjectEditor.editInBrowsableContainer(uiF, root, rootContainer);
		ObjectEditor.editInBrowsableContainer(uiF, parent, parentContainer);
		ObjectEditor.editInBrowsableContainer(uiF, child, childContainer);
		uiF.validate();
		uiF.setVisible(true);
		return uiF;
		*/
		Object objects[] = {parent, child};
		VirtualContainer containers[] = {parentContainer, childContainer};
		return browse (frame, frameContainer, objects, containers);
	}
	public static uiFrame browse (VirtualFrame frame, VirtualContainer frameContainer,
								  Object[] objects, VirtualContainer[] containers) {
		/*
		if (objects.length != containers.length) {
			System.out.println("Browse Exception: object and container arrays of different lengths");
			return null;
		}
		*/
		
		uiFrame uiF = OEFrameSelector.createFrame(frame, frameContainer);
		browse(uiF, objects, containers, null, true);
		/*
		for (int i = 0; i < objects.length; i++)
		//Frame frame = uiF.getFrame();	
			ObjectEditor.editInBrowsableContainer(uiF, objects[i], containers[i]);
		//uiF.setSize();
		//uiF.validate();
		*/
		uiF.setVisible(true);
		return uiF;
		
	}
	public static uiFrame browseWithAttributes (Object[] objects, Container[] containers, java.util.List<Hashtable> attributeTableList) {
		VirtualContainer[] virtualContainers = new VirtualContainer[containers.length];
		for (int i = 0; i < containers.length; i++) {
			virtualContainers[i] = AWTContainer.virtualContainer(containers[i]);
		}
		return browseWithAttributes(objects, virtualContainers, attributeTableList);	
		
	}
	/*
	public static uiFrame browse (Object[] objects, Container[] containers, java.util.List<java.util.List<String>> labelList) {
		VirtualContainer[] virtualContainers = new VirtualContainer[containers.length];
		for (int i = 0; i < containers.length; i++) {
			virtualContainers[i] = AWTContainer.virtualContainer(containers[i]);
		}
		return browse(objects, virtualContainers, labelList);	
		
	}
	*/
	static uiFrame nullFrame = null;
	static uiFrame nullFrame() {
		if (nullFrame == null) {
			nullFrame = OEFrameSelector.createFrame(null, null);
			nullFrame.setIsDummy(true);
		}
		return nullFrame;
	}
	public static void bindRightMenu (VirtualComponent component, Object object) {
		ObjectAdapter objectAdapter = toObjectAdapter(object);
		
	}
	public static ButtonCommand bind (uiFrame theFrame, Object targetObject, String targetMethodName, JButton button ) {
		VirtualButton virtualButton = bus.uigen.widgets.swing.SwingButton.virtualButton(button);
		return bind(theFrame, targetObject, targetMethodName, virtualButton);
		
	}
	public static ButtonCommand bind (Object targetObject, String targetMethodName, JButton button ) {
		return bind (nullFrame(), targetObject, targetMethodName, button);
	}
	public static VirtualMethodMenuItem bind (uiFrame theFrame, Object targetObject, String targetMethodName, JMenuItem menuItem ) {
		VirtualMenuItem virtualMenuItem = bus.uigen.widgets.swing.SwingMenuItem.virtualMenuItem(menuItem);
		return bind(theFrame, targetObject, targetMethodName, virtualMenuItem);
		
	}
	public static VirtualMethodMenuItem bind (Object targetObject, String targetMethodName, JMenuItem menuItem ) {
		return bind (nullFrame(), targetObject, targetMethodName, menuItem);
	}
	public static void addComponentSelectionListener(uiFrame frame, ComponentSelectionListener l) {
		ObjectAdapter adapter = frame.getOriginalAdapter();
		java.util.List listeners = (java.util.List) adapter.getSelectHandlers();
		if (listeners == null)
			listeners = new Vector();
		if (listeners.contains(l))
			return;
		listeners.add(l);		
		
	}
	public static void addComponentExpansionListener(uiFrame frame, ComponentExpansionListener l) {
		ObjectAdapter adapter = frame.getOriginalAdapter();
		java.util.List listeners = (java.util.List) adapter.getExpandHandlers();
		if (listeners == null)
			listeners = new Vector();
		if (listeners.contains(l))
			return;
		listeners.add(l);		
		
	}
	public static uiFrame browse (Object[] objects, Container[] containers,java.util.List<Boolean> indentList, java.util.List<java.util.List<String>> labelList) {
		return edit (objects, containers, labelList, indentList, true);
		
	}
	public static uiFrame edit (Object[] objects, Container[] containers, java.util.List<java.util.List<String>> labelList, java.util.List<Boolean> indentList, boolean browse) {
		VirtualContainer[] virtualContainers = new VirtualContainer[containers.length];
		for (int i = 0; i < containers.length; i++) {
			virtualContainers[i] = AWTContainer.virtualContainer(containers[i]);
		}
		return edit(objects, virtualContainers, labelList, indentList, browse);	
		
	}
	public static uiFrame edit (Object[] objects, Container[] containers, java.util.List<java.util.List<String>> labelList, java.util.List<Boolean> indentList) {
		return edit(objects, containers, labelList, indentList, false);
	}
	public static uiFrame edit (Object[] objects, Container[] containers) {
		return edit(objects, containers, null, null);
	}
	public static uiFrame browseWithAttributes (Object[] objects, VirtualContainer[] containers, java.util.List<Hashtable> attributeTableList) {
		
		uiFrame uiF = OEFrameSelector.createFrame(null, null);
		uiF.setIsDummy(true);
		return browse (uiF, objects, containers, attributeTableList, false);
	}
	public static uiFrame browse (Object[] objects, VirtualContainer[] containers, java.util.List<java.util.List<String>> labelListList) {
		uiFrame uiF = OEFrameSelector.createFrame(null, null);
		java.util.List<Hashtable> attributeTableList = new ArrayList();
		for (int i = 0; i < labelListList.size(); i++) {
			Hashtable attributeTable = toAttributes(labelListList.get(i), null);
			attributeTableList.add(attributeTable);
		}
		uiF.setIsDummy(true);
		return browse (uiF, objects, containers, attributeTableList, false);
	}
	public static uiFrame setModels (Object[] objects, VirtualContainer[] containers) {
		return edit (objects, containers, null, null, false);
	}
	public static uiFrame setModels (uiFrame theFrame, Object[] objects, VirtualContainer[] containers) {
		return edit (theFrame, objects, containers, null, null, false);
	}
	public static uiFrame edit (Object[] objects, VirtualContainer[] containers, java.util.List<java.util.List<String>> labelListList, java.util.List<Boolean>  indentList, boolean browse) {
		//uiFrame uiF = uiFrameSelector.createFrame(null, null);
		return edit (null, objects, containers, labelListList, indentList, browse);
		/*
		java.util.List<Hashtable> attributeTableList = new ArrayList();
		if (labelListList != null)
		for (int i = 0; i < labelListList.size(); i++) {
			Hashtable attributeTable = toAttributes(labelListList.get(i));
			attributeTableList.add(attributeTable);
		}
		uiF.setIsDummy(true);
		return edit (uiF, objects, containers, attributeTableList, false, browse);
		*/
	}
	public static uiFrame edit (uiFrame uiF, Object[] objects, VirtualContainer[] containers, java.util.List<java.util.List<String>> labelListList, java.util.List<Boolean> indentList, boolean browse) {
		if (uiF == null) {
			uiF = OEFrameSelector.createFrame(null, null);
		}
		java.util.List<Hashtable> attributeTableList = new ArrayList();
		if (labelListList != null && indentList != null)
		for (int i = 0; i < labelListList.size(); i++) {
			Hashtable attributeTable = toAttributes(labelListList.get(i), indentList.get(i));
			attributeTableList.add(attributeTable);
		}
		uiF.setIsDummy(true);
		return edit (uiF, objects, containers, attributeTableList, false, browse);
	}
	
	
	
	public static uiFrame treeBrowse (VirtualFrame frame, VirtualContainer frameContainer,
									  Object treeObject, VirtualContainer treeContainer,
								  Object[] objects, VirtualContainer[] containers) {
		/*
		if (objects.length != containers.length) {
			System.out.println("Browse Exception: object and container arrays of different lengths");
			return null;
		}
		*/
		
		uiFrame uiF = OEFrameSelector.createFrame(frame, frameContainer);
		treeBrowse(uiF, treeObject, treeContainer, objects, containers);
		/*
		for (int i = 0; i < objects.length; i++)
		//Frame frame = uiF.getFrame();	
			ObjectEditor.editInBrowsableContainer(uiF, objects[i], containers[i]);
		//uiF.setSize();
		//uiF.validate();
		*/
		uiF.setVisible(true);
		return uiF;
		
	}
	public static uiFrame treeBrowse (VirtualFrame frame, VirtualContainer frameContainer,
			  Object treeObject, VirtualContainer treeContainer,
		  Object parentObject,  VirtualContainer parentContainer, Object childObject, VirtualContainer childContainer) {
		Object[] objects = {parentObject, childObject};
		VirtualContainer[] containers = {parentContainer, childContainer};
		return treeBrowse (frame, frameContainer, treeObject, treeContainer, objects, containers);		
		
	}
	
	public static VirtualContainer[] toVirtualContainers (Container[] containers) {
		VirtualContainer[] retVal = new VirtualContainer[containers.length];
		for (int i = 0; i < containers.length; i++)
			retVal[i] = toVirtualContainer(containers[i]);
		return retVal;
	}
	
	public static VirtualContainer toVirtualContainer (Container c) {
		return AWTContainer.virtualContainer(c);
	}
	public static VirtualTree toVirtualTree (JTree c) {
		return SwingTree.virtualTree(c);
	}
	public static VirtualTable toVirtualTable (JTable c) {
		return SwingTable.virtualTable(c);
	}
	public static VirtualContainer toVirtualTabbedPane (JTabbedPane c) {
		return SwingTabbedPane.virtualTabbedPane(c);
	}
	
	public static uiFrame browseAndEdit (VirtualFrame frame, VirtualContainer frameContainer,
								  Object[] browseObjects, VirtualContainer[] browseContainers,
								  Object[] editObjects, VirtualContainer[] editContainers) {
		/*
		if (objects.length != containers.length) {
			System.out.println("Browse Exception: object and container arrays of different lengths");
			return null;
		}
		*/
		
		uiFrame uiF = OEFrameSelector.createFrame(frame, frameContainer);
		browse(uiF, browseObjects, browseContainers, null, true);
		edit (uiF, editObjects, editContainers, true);
		/*
		for (int i = 0; i < objects.length; i++)
		//Frame frame = uiF.getFrame();	
			ObjectEditor.editInBrowsableContainer(uiF, objects[i], containers[i]);
		//uiF.setSize();
		//uiF.validate();
		*/
		uiF.setVisible(true);
		return uiF;
		
	}
	
	public static uiFrame treeBrowseAndEdit (VirtualFrame frame, VirtualContainer frameContainer,
								Object treeObject, VirtualContainer treeContainer,  
								Object[] browseObjects, VirtualContainer[] browseContainers,
								  Object[] editObjects, VirtualContainer[] editContainers) {
		
		Object[] treeObjects = {treeObject};
		VirtualContainer[] treeContainers = {treeContainer};
		return treeBrowseAndEdit(frame, frameContainer, treeObjects, treeContainers, browseObjects, browseContainers, editObjects, editContainers);
		
	}
	public static uiFrame treeBrowseAndEdit (VirtualFrame frame, VirtualContainer frameContainer,
								Object[] treeObjects, VirtualContainer[] treeContainers,  
								Object[] browseObjects, VirtualContainer[] browseContainers,
								  Object[] editObjects, VirtualContainer[] editContainers) {
		/*
		if (objects.length != containers.length) {
			System.out.println("Browse Exception: object and container arrays of different lengths");
			return null;
		}
		*/
		
		uiFrame uiF = OEFrameSelector.createFrame(frame, frameContainer);
		treeBrowse(uiF, treeObjects, treeContainers,
				   browseObjects, browseContainers);
		edit (uiF, editObjects, editContainers, true);
		/*
		for (int i = 0; i < objects.length; i++)
		//Frame frame = uiF.getFrame();	
			ObjectEditor.editInBrowsableContainer(uiF, objects[i], containers[i]);
		//uiF.setSize();
		//uiF.validate();
		*/
		uiF.setVisible(true);
		return uiF;
		
	}
	
	public static void editAsLoggable(Object realObject) {
		Object loggable = LoggableRegistry.getLoggableModel(realObject);
		ObjectEditor.edit(loggable);
		LoggableRegistry.setIsLocal(false);
	}
	
	public static uiFrame browse (uiFrame uiF,
								  Object[] objects, VirtualContainer[] containers, 
								  java.util.List<Hashtable> attributeTableList, boolean rootPanel) {
		if (objects == null || containers == null) return null;
		if (objects.length != containers.length) {
			System.out.println("Browse Exception: object and container arrays of different lengths");
			return null;
		}
		//uiFrame uiF = FrameSelector.createFrame(frame, frameContainer);
		for (int i = 0; i < objects.length; i++) {
		//Frame frame = uiF.getFrame();	
			if (attributeTableList != null && i < attributeTableList.size()) {
				uiF.setSelfAttributes(attributeTableList.get(i));
			}
			//uiObjectAdapter adapter = ObjectEditor.editInBrowsableContainer(uiF, objects[i], containers[i]);
			ObjectAdapter adapter;
			if (!rootPanel)
			    adapter = uiGenerator.generateInBrowsableContainer(uiF, objects[i], (ObjectAdapter) null, containers[i]);
			else
				adapter = uiGenerator.generateInBrowsableRootPanel(uiF, objects[i], null, null, containers[i], null, null);
			if (i < objects.length - 1)
				uiF.internalElideTopChildren(adapter);
		}
		uiF.setSize();
		uiF.validate();
		//uiF.setVisible(true);
		return uiF;
		
	}
	public static uiFrame edit(uiFrame uiF, Object[] objects,
			VirtualContainer[] containers,
			java.util.List<Hashtable> attributeTableList, boolean rootPanel,
			boolean browse) {
		if (objects == null || containers == null)
			return null;
		if (objects.length != containers.length) {
			System.out
					.println("Browse Exception: object and container arrays of different lengths");
			return null;
		}
		// uiFrame uiF = FrameSelector.createFrame(frame, frameContainer);
		for (int i = 0; i < objects.length; i++) {
			// Frame frame = uiF.getFrame();
			if (attributeTableList != null && i < attributeTableList.size()) {
				uiF.setSelfAttributes(attributeTableList.get(i));
			}
			// uiObjectAdapter adapter =
			// ObjectEditor.editInBrowsableContainer(uiF, objects[i],
			// containers[i]);
			ObjectAdapter adapter;
			if (!browse)
				adapter = uiGenerator.generateInUserContainer(uiF, objects[i], containers[i], rootPanel);
			else {
			if (!rootPanel)
				adapter = uiGenerator.generateInBrowsableContainer(uiF,
						objects[i], (ObjectAdapter) null, containers[i]);
			else
				adapter = uiGenerator.generateInBrowsableRootPanel(uiF,
						objects[i], null, null, containers[i], null, null);
			}
			if (i < objects.length - 1)
				uiF.internalElideTopChildren(adapter);
		}
		uiF.setSize();
		uiF.validate();
		//uiF.setVisible(true);
		return uiF;

	}
	public static uiFrame treeBrowse (uiFrame uiF, Object treeObject, VirtualContainer treeContainer,
								  Object[] objects, VirtualContainer[] containers) {
		
		//uiF.createTreePanel(treeObject, treeContainer);
		//browse (uiF, objects, containers);
		Object[] treeObjects = {treeObject};
		VirtualContainer[] treeContainers = {treeContainer};
		return treeBrowse(uiF, treeObjects, treeContainers, objects, containers);
		//return uiF;
		
	}
	public static uiFrame treeBrowse (uiFrame uiF, Object[] treeObjects, VirtualContainer[] treeContainers,
								  Object[] objects, VirtualContainer[] containers) {
		if (treeObjects.length != treeContainers.length) {
			System.out.println("E***treeBrowse: length of tree objects and containsers does not match");
			return null;
		}
		for (int i = 0; i < treeObjects.length; i++)
			uiF.createTreePanel(treeObjects[i], treeContainers[i]);
		browse (uiF, objects, containers, null, true);
		return uiF;
		
	}
	public static uiFrame edit (VirtualFrame frame, VirtualContainer frameContainer,
						   Object parent, VirtualContainer parentContainer,
						   Object child, VirtualContainer childContainer) {
		/*
		uiFrame uiF = FrameSelector.createFrame(frame, frameContainer);
		//Frame frame = uiF.getFrame();	
		ObjectEditor.editInBrowsableContainer(uiF, root, rootContainer);
		ObjectEditor.editInBrowsableContainer(uiF, parent, parentContainer);
		ObjectEditor.editInBrowsableContainer(uiF, child, childContainer);
		uiF.validate();
		uiF.setVisible(true);
		return uiF;
		*/
		Object objects[] = {parent, child};
		VirtualContainer containers[] = {parentContainer, childContainer};
		return edit (frame, frameContainer, objects, containers);
	}
	
	public static uiFrame multiEdit (
			Object[] objects) {
		register();
		VirtualFrame frame = (new SwingFrameFactory()).createFrame();
		JFrame jFrame = (JFrame) frame.getPhysicalComponent();
		
		//JDesktopPane desktop = new JDesktopPane();
		//jFrame.setContentPane(desktop);
		jFrame.getContentPane().setLayout(new GridLayout(1,2));
		VirtualContainer[] components = new VirtualContainer[objects.length];
		for (int i = 0; i < objects.length; i++) {

			JInternalFrame internalFrame = new JInternalFrame();			
			JPanel panel = new JPanel();
			//internalFrame.add(panel);
			//components[i] = AnAWTContainer.virtualContainer(new JInternalFrame());
			//components[i] = AnAWTContainer.virtualContainer(new JPanel());
			components[i] = AWTContainer.virtualContainer(panel);
			//desktop.add((JInternalFrame) components[i].getPhysicalComponent());
			//desktop.add(internalFrame);
			//((Container) frame.getContentPane().getPhysicalComponent()).add((Container) components[i].getPhysicalComponent());
			jFrame.getContentPane().add(panel);
			
		}
		
	
		return edit (frame, frame.getContentPane(), objects, components);
		
		
	}
	/*
	public static uiFrame desktopEdit (
			Object[] objects) {
		register();
		VirtualFrame frame = (new SwingFrameFactory()).createFrame();
		JFrame jFrame = (JFrame) frame.getPhysicalComponent();
		//VirtualDesktopPane desktop = DesktopPaneSelector.createDesktopPane();
		
		JDesktopPane desktop = new JDesktopPane();
		jFrame.setContentPane(desktop);
		//frame.setContentPane(desktop);
		jFrame.getContentPane().setLayout(new GridLayout(1,2));
		//frame.getContentPane().setLayout(new GridLayout(1,2));
		if (frame.getContentPane().getPhysicalComponent() != jFrame.getContentPane()) 
			System.out.println ("COntentpane error");
		VirtualContainer[] components = new VirtualContainer[objects.length];
		for (int i = 0; i < objects.length; i++) {
			//VirtualInternalFrame internalFrame = InternalFrameSelector.createInternalFrame();
			JInternalFrame internalFrame = new JInternalFrame();			
			//JPanel panel = new JPanel();
			//internalFrame.add(panel);
			internalFrame.setVisible(true);
			//internalFrame.setSize(20, 30);
			//components[i] = AnAWTContainer.virtualContainer(new JInternalFrame());
			//components[i] = AnAWTContainer.virtualContainer(new JPanel());
			components[i] = AnAWTContainer.virtualContainer(internalFrame.getContentPane());
			//components[i] = internalFrame.getContentPane();
			//desktop.add((JInternalFrame) components[i].getPhysicalComponent());
			desktop.add(internalFrame);
			//((Container) frame.getContentPane().getPhysicalComponent()).add((Container) components[i].getPhysicalComponent());
			//jFrame.getContentPane().add(panel);
		}


		jFrame.setSize(600, 400);
		return edit (frame, frame.getContentPane(), objects, components);
	}
	*/
		public static uiFrame desktopEdit (
				Object[] objects) {
			//register();
			VirtualFrame frame = (new SwingFrameFactory()).createFrame();
			frame.setSize(600, 400);
			//JFrame jFrame = (JFrame) frame.getPhysicalComponent();
			VirtualDesktopPane desktop = DesktopPaneSelector.createDesktopPane();
			
			//JDesktopPane desktop = new JDesktopPane();
			//jFrame.setContentPane(desktop);
			frame.setContentPane(desktop);
			//jFrame.getContentPane().setLayout(new GridLayout(1,2));
			//frame.getContentPane().setLayout(new GridLayout(1,2));
			desktop.setLayout(new GridLayout(1,2));
			VirtualContainer[] components = new VirtualContainer[objects.length];
			for (int i = 0; i < objects.length; i++) {
				VirtualInternalFrame internalFrame = InternalFrameSelector.createInternalFrame();
				//JInternalFrame internalFrame = new JInternalFrame();			
				//JPanel panel = new JPanel();
				//internalFrame.add(panel);
				internalFrame.setVisible(true);
				//internalFrame.setSize(20, 30);
				//components[i] = AnAWTContainer.virtualContainer(new JInternalFrame());
				//components[i] = AnAWTContainer.virtualContainer(new JPanel());
				//components[i] = AnAWTContainer.virtualContainer(internalFrame.getContentPane());
				components[i] = internalFrame.getContentPane();
				//desktop.add((JInternalFrame) components[i].getPhysicalComponent());
				desktop.add(internalFrame);
				//((Container) frame.getContentPane().getPhysicalComponent()).add((Container) components[i].getPhysicalComponent());
				//jFrame.getContentPane().add(panel);
			}
			
		
		
		return edit (frame, frame.getContentPane(), objects, components);
			
		}
		public static void swingDesktopEditWithMenus (
				Object[] objects) {
			register();
			VirtualFrame frame = (new SwingFrameFactory()).createFrame();
			frame.setSize(300, 200);
			JFrame jFrame = (JFrame) frame.getPhysicalComponent();
			
			JDesktopPane desktop = new JDesktopPane();
			jFrame.setContentPane(desktop);
			VirtualInternalFrame frame2 = (new SwingInternalFrameFactory()).createFrame();
			JDesktopPane desktop2 = new JDesktopPane();
			JInternalFrame jFrame2 = (JInternalFrame) frame2.getPhysicalComponent();
			desktop.add(jFrame2);
			jFrame2.setContentPane(desktop2);
			//jFrame.getContentPane().setLayout(new GridLayout(1,2));
			VirtualContainer[] components = new VirtualContainer[objects.length];
			for (int i = 0; i < objects.length; i++) {
				VirtualFrame childFrame = (new SwingInternalFrameFactory()).createFrame();
				//JInternalFrame internalFrame = new JInternalFrame();	
				JInternalFrame internalFrame =  (JInternalFrame) childFrame.getPhysicalComponent();
				//JPanel panel = new JPanel();
				//internalFrame.add(panel);
				//internalFrame.setVisible(true);
				//components[i] = AnAWTContainer.virtualContainer(new JInternalFrame());
				//components[i] = AnAWTContainer.virtualContainer(new JPanel());
				//components[i] = AnAWTContainer.virtualContainer(internalFrame.getContentPane());
				//desktop.add((JInternalFrame) components[i].getPhysicalComponent());
				ObjectEditor.edit(childFrame, childFrame.getContentPane(), objects[i]);
				//desktop.add(internalFrame);
				desktop2.add(internalFrame);
				//((Container) frame.getContentPane().getPhysicalComponent()).add((Container) components[i].getPhysicalComponent());
				//jFrame.getContentPane().add(panel);
				
			}

			jFrame2.setVisible(true);
			jFrame.setVisible(true);
		
	
		//return edit (frame, frame.getContentPane(), objects, components);
		
		
	}
	
	
	public static uiFrame edit (VirtualFrame frame, VirtualContainer frameContainer,
						   Object root, VirtualContainer rootContainer,
						   Object parent, VirtualContainer parentContainer,
						   Object child, VirtualContainer childContainer) {
		/*
		uiFrame uiF = FrameSelector.createFrame(frame, frameContainer);
		//Frame frame = uiF.getFrame();	
		ObjectEditor.editInBrowsableContainer(uiF, root, rootContainer);
		ObjectEditor.editInBrowsableContainer(uiF, parent, parentContainer);
		ObjectEditor.editInBrowsableContainer(uiF, child, childContainer);
		uiF.validate();
		uiF.setVisible(true);
		return uiF;
		*/
		Object objects[] = {root, parent, child};
		VirtualContainer containers[] = {rootContainer, parentContainer, childContainer};
		return edit (frame, frameContainer, objects, containers);
	}
	public static uiFrame edit (VirtualFrame frame, VirtualContainer frameContainer,
								  Object[] objects, VirtualContainer[] containers) {
		if (objects.length != containers.length) {
			System.out.println("Edit Exception: object and container arrays of different lengths");
			return null;
		}
		uiFrame uiF = OEFrameSelector.createFrame(frame, frameContainer);
		edit(uiF, objects, containers, true);
		/*
		for (int i = 0; i < objects.length; i++)
		//Frame frame = uiF.getFrame();	
			ObjectEditor.editInContainer(uiF, objects[i], containers[i]);
		*/
		//uiF.setSize();
		//uiF.validate();
		uiF.setVisible(true);
		
		return uiF;
		
	}
	public static uiFrame setModel(	Object object, Container container) {
		VirtualContainer virtualContainer = AWTContainer.virtualContainer(container);
		//return editInContainer(object, virtualContainer);
		return setModel(object, virtualContainer);
	}
	public static uiFrame setModel(uiFrame theFrame,	Object object, Container container) {
		VirtualContainer virtualContainer = AWTContainer.virtualContainer(container);
		//return editInContainer(object, virtualContainer);
		return setModel(theFrame, object, virtualContainer);
	}
	public static uiFrame edit(	Object object, Container container) {
		return setModel(object, container);
	}
	
	public static uiFrame setModel(	Object object, VirtualContainer container) {
		//VirtualContainer virtualContainer = AWTContainer.virtualContainer(container);
		//return editInContainer(object, virtualContainer);
		Object[] objects = {object};
		VirtualContainer[] virtualContainers = {container};
		return setModels(objects, virtualContainers);
	}
	public static uiFrame setModel(uiFrame theFrame,	Object object, VirtualContainer container) {
		//VirtualContainer virtualContainer = AWTContainer.virtualContainer(container);
		//return editInContainer(object, virtualContainer);
		Object[] objects = {object};
		VirtualContainer[] virtualContainers = {container};
		return setModels(theFrame, objects, virtualContainers);
	}
	public static uiFrame edit(	Object object, VirtualContainer container) {
		return setModel(object, container);
	}
	
	
	public static uiFrame editInMainContainer(	Object object, VirtualContainer container) {
		
		return editInContainer (object, null, null, container);
		

	}
	
public static uiFrame editInMainContainer(	Object object, Container container) {
		
		return editInContainer (object, null, null, AWTContainer.virtualContainer(container));
		

	}
public static uiFrame editInTreeContainer(	Object object, Container container) {
	
	return editInContainer (object, AWTContainer.virtualContainer(container), null, null );
	

}
	public static uiFrame addGraphicsModel (uiFrame uiF, Object object, Container drawPanel) {
		VirtualContainer virtualPanel = AWTContainer.virtualContainer(drawPanel);
		return editInDrawingContainer(uiF, object, virtualPanel, true);
	}
	public static uiFrame addGraphicsModel (uiFrame uiF, Object object, VirtualContainer drawPanel) {
		return editInDrawingContainer(uiF, object, drawPanel, true);
	}
	public static uiFrame editInDrawingContainer (uiFrame uiF, Object object,  VirtualContainer drawingContainer, boolean rootPanel) {
		return editInDrawingContainer(uiF, object, drawingContainer, rootPanel, true);
//		//uiFrame uiF = new uiFrame();
//		//uiF.init (null, null);
//		//uiF.init(object);
//		
//		if (drawingContainer.getComponentCount() > 0) {
////			Tracer.error("Found components " + drawingContainer.getComponent(0).getPhysicalComponent() + " in draw container: " + drawingContainer.getPhysicalComponent() + "Cannot have child components in draw container. \n Either remove child components or put a new container in the current draw container and make it teh draw container."); 
//			Tracer.error("Found component " + drawingContainer.getComponent(0).getPhysicalComponent().getClass() + " in draw container: " + drawingContainer.getPhysicalComponent().getClass() + ". Cannot have child components in a draw container. \n Either remove child components or put a new container in the current draw container and make the new container the draw container."); 
//
//			System.exit(-1);
//		}
//		uiF.setIsDummy(true); // do we need this?
//		uiF.setDrawingContainer(drawingContainer, rootPanel);
//		uiF.setCheckPreOnImplicitRefresh(false); // unlikely or impossible that elements in the panel will be shown as a result of menu commands - maybe, let us see this later
//		VirtualContainer dummyMain = null;
//		if (!uiF.isGlassPane)
//			dummyMain = PanelSelector.createPanel();
//		uiF.setOnlyGraphicsPanel(true);
//
////		VirtualContainer dummyMain = PanelSelector.createPanel();
//		editInContainer(uiF, object, dummyMain); 
//		return uiF;
	}
	public static uiFrame editInDrawingContainer (uiFrame uiF, Object object,  VirtualContainer drawingContainer, boolean rootPanel, boolean isDummy) {
		//uiFrame uiF = new uiFrame();
		//uiF.init (null, null);
		//uiF.init(object);
		
		if (drawingContainer.getComponentCount() > 0) {
//			Tracer.error("Found components " + drawingContainer.getComponent(0).getPhysicalComponent() + " in draw container: " + drawingContainer.getPhysicalComponent() + "Cannot have child components in draw container. \n Either remove child components or put a new container in the current draw container and make it teh draw container."); 
			Tracer.error("Found component " + drawingContainer.getComponent(0).getPhysicalComponent().getClass() + " in draw container: " + drawingContainer.getPhysicalComponent().getClass() + ". Cannot have child components in a draw container. \n Either remove child components or put a new container in the current draw container and make the new container the draw container."); 

			System.exit(-1);
		}
		if (isDummy)
		uiF.setIsDummy(true); // do we need this?
		uiF.setDrawingContainer(drawingContainer, rootPanel);
		uiF.setCheckPreOnImplicitRefresh(false); // unlikely or impossible that elements in the panel will be shown as a result of menu commands - maybe, let us see this later
		VirtualContainer dummyMain = null;
		if (!uiF.isGlassPane)
			dummyMain = PanelSelector.createPanel();
		uiF.setOnlyGraphicsPanel(true);

//		VirtualContainer dummyMain = PanelSelector.createPanel();
		editInContainer(uiF, object, dummyMain); 
		return uiF;
	}
	public static uiFrame editInTreeContainer (uiFrame uiF, Object object,  VirtualContainer treeContainer, boolean rootPanel) {
		//uiFrame uiF = new uiFrame();
		//uiF.init (null, null);
		//uiF.init(object);
		uiF.setIsDummy(true);
		uiF.setTreeContainer(treeContainer);
		VirtualContainer dummyMain = null;
//		if (!uiF.isGlassPane)
//			dummyMain = PanelSelector.createPanel();
//		VirtualContainer dummyMain = PanelSelector.createPanel();
		editInContainer(uiF, object, treeContainer, rootPanel); 
		return uiF;
	}
	public static uiFrame editInDrawingContainer (uiFrame uiF, Object object, Container drawingContainer, boolean rootPanel) {
		VirtualContainer virtualContainer = AWTContainer.virtualContainer(drawingContainer);
		return editInDrawingContainer(uiF, object, virtualContainer, rootPanel);
	}
	public static uiFrame editInDrawingContainer (Object object,  VirtualContainer drawingContainer, boolean rootPanel) {
		uiFrame uiF = new uiFrame();
		uiF.init (null, null);
		uiF.init(object);
		uiF.setIsDummy(true);
		return editInDrawingContainer(uiF, object, drawingContainer, rootPanel);
		/*
		uiF.setDrawingContainer(drawingContainer, rootPanel);
		VirtualContainer dummyMain = PanelSelector.createPanel();
		editInContainer(uiF, object, dummyMain); 
		
		return uiF;
		*/
	}
	
	public static uiFrame editInTreeContainer (Object object,  VirtualContainer treeContainer, boolean rootPanel) {
		uiFrame uiF = new uiFrame();
		uiF.init (null, null);
		uiF.init(object);
		uiF.setIsDummy(true);
		return editInTreeContainer(uiF, object, treeContainer, rootPanel);
		/*
		uiF.setDrawingContainer(drawingContainer, rootPanel);
		VirtualContainer dummyMain = PanelSelector.createPanel();
		editInContainer(uiF, object, dummyMain); 
		
		return uiF;
		*/
	}
	public static uiFrame editInDrawingContainer (Object object,  VirtualContainer drawingContainer) {
		return editInDrawingContainer(object, drawingContainer, false);
	}
	public static uiFrame editInDrawingContainer (Object object,  Container drawingContainer, boolean rootPanel) {
		VirtualContainer virtualContainer = AWTContainer.virtualContainer(drawingContainer);
		return editInDrawingContainer(object, virtualContainer, rootPanel);

	}
	public static uiFrame editInDrawingContainer (Object object,  Container drawingContainer) {
		return editInDrawingContainer(object, drawingContainer, true);
		
	}
	public static ComponentDrawer createComponentDrawer (Container drawingContainer) {
		VirtualContainer virtualContainer = AWTContainer.virtualContainer(drawingContainer);
		ComponentDrawer shapeDrawer = createNewShapeDrawer();
		 editInDrawingContainer(shapeDrawer, virtualContainer, false);
		 return shapeDrawer;

	}
	static Hashtable<VirtualContainer, uiFrame> drawingMap = new Hashtable();
	public static uiFrame addModel (Object object,  Container drawingContainer) {
		VirtualContainer virtualContainer = AWTContainer.virtualContainer(drawingContainer);
		return addModel(object, virtualContainer);
	}
	public static uiFrame addModel (Object object,  VirtualContainer drawingContainer) {
		uiFrame frame = drawingMap.get(drawingContainer);
		Vector models;
		if (frame == null) {
			models = new Vector();
			models.add(object);
			frame = editInDrawingContainer(models, drawingContainer, false);	
			drawingMap.put(drawingContainer, frame);
		} else {
			models = (Vector) frame.getAdapter().getRealObject();
			models.add(object);
			frame.doRefresh();
		}
		return frame;
		
	}
	
	
	public static uiFrame editInContainer(	Object object, Container frameContainer) {
		VirtualContainer aVirtualContainer = AWTContainer.virtualContainer(frameContainer);
		return editInContainer(object, aVirtualContainer);
	}

	
public static uiFrame editInContainer(	Object object, VirtualContainer frameContainer) {
		
		
		uiFrame uiF = new uiFrame();
		uiF.setIsDummy(true);
		uiF.init (null, null);
		uiF.init(object);
		uiF.setContainer(frameContainer);
		edit(uiF, object);
		/*
		 * for (int i = 0; i < objects.length; i++) //Frame frame =
		 * uiF.getFrame(); ObjectEditor.editInContainer(uiF, objects[i],
		 * containers[i]);
		 */
		//uiF.setSize();
		//uiF.validate();
		uiF.setVisible(true);

		return uiF;

	}
	
	public static uiFrame editInContainer(	Object object, VirtualContainer tree, VirtualContainer draw, VirtualContainer container) {
		
		
		uiFrame uiF = new uiFrame();
		uiF.setIsDummy(true);
		uiF.init (null, null);
		uiF.init(object);
		if (draw != null)
		uiF.setDrawingContainer(draw, true);
		if (tree != null)
		uiF.setTreeContainer(tree);
		uiF.setManualMainContainer(true);
	
		editInContainer(uiF, object, container);
		
		uiF.setVisible(true);

		return uiF;

	}
	
public static uiFrame editInTreeContainer(	Object object, VirtualContainer tree) {
		return editInContainer(object, tree, null, null);

	}
	
	public static uiFrame edit(VirtualFrame frame, VirtualContainer frameContainer,
			Object object, VirtualContainer container) {
		
		
		uiFrame uiF = OEFrameSelector.createFrame(frame, frameContainer);
		VirtualContainer childPanel = container;
		if (childPanel == null) {
			childPanel = uiF.createNewChildPanelInNewScrollPane();
		}
		
		editInContainer(uiF, object, childPanel);
		
		uiF.setVisible(true);

		return uiF;
		
		//return edit (frame, frameContainer, object, null, null, container);

	}
	public static uiFrame edit(VirtualFrame frame, VirtualContainer frameContainer,
			Object object) {
		
		return edit (frame, frameContainer, object, frameContainer);
		

	}
	public static uiFrame createCommands(VirtualFrame frame,
			Object object, MenuSetter menuSetter, AMenuDescriptor menuDescriptor) {
		uiFrame uiF = OEFrameSelector.createFrame(object, true, menuSetter, menuDescriptor);
		createCommands(uiF, object);
		return uiF;
	}
	public static uiFrame createCommands(VirtualFrame frame,
			Object object) {
		
		MenuSetter menuSetter = defaultMenuSetter;
		AMenuDescriptor menuDescriptor = new AMenuDescriptor();
		return createCommands (frame, object, menuSetter, menuDescriptor);
		/*
		uiFrame uiF = FrameSelector.createFrame(frame, null);
		createCommands(uiF, object);
		return uiF;
		*/
		
		//return edit (frame, frameContainer, object, null, null, container);

	}
	public static uiFrame createEditor(VirtualFrame frame, VirtualContainer container) {
		return OEFrameSelector.createFrame(frame, container);
	}
	public static uiFrame createEditor(VirtualFrame frame) {
		return OEFrameSelector.createFrame(frame, null);
	}
	public static uiFrame addMenuObjects (uiFrame f, Object[] objects) {
		if (f ==null) {
			f = OEFrameSelector.createFrame(null, null);
			f.setSize(500, 100);
			f.setVisible(true);
		}
		for (int i = 0; i < objects.length; i++) {
			f.addMenuObject(objects[i]);
			
		}
		f.validate();
		return f;
	}
	public static uiFrame addMenuObjects (Object[] objects) {
		return addMenuObjects(null, objects);
	}
	
	public static uiFrame createCommands(uiFrame uiF,
			Object object) {	
		
		//uiGenerator.uiAddMethods(uiF, object);
		//uiF.displayMenuTree();
		uiGenerator.addMenuObjects(uiF, object);
		AToolbarManager.addUIFrameToolBarButtons(uiF);
		
		//editInContainer(uiF, object, null);
		
		uiF.setVisible(true);

		return uiF;
		
		//return edit (frame, frameContainer, object, null, null, container);

	}
	public static uiFrame edit(VirtualFrame frame, VirtualContainer frameContainer,
			Object object, VirtualContainer tree, VirtualContainer graphics, VirtualContainer main) {
		
		
		uiFrame uiF = OEFrameSelector.createFrame(frame, frameContainer);
		uiF.setTreeContainer(tree);
		uiF.setDrawingContainer(graphics);
		
		editInContainer(uiF, object, main);
		/*
		 * for (int i = 0; i < objects.length; i++) //Frame frame =
		 * uiF.getFrame(); ObjectEditor.editInContainer(uiF, objects[i],
		 * containers[i]);
		 */
		//uiF.setSize();
		//uiF.validate();
		uiF.setVisible(true);

		return uiF;

	}
	public static uiFrame edit (uiFrame uiF, 
								  Object[] objects, VirtualContainer[] containers, boolean rootPanel) {
		if (objects == null || containers == null) return null;
		if (objects.length != containers.length) {
			System.out.println("Edit Exception: object and container arrays of different lengths");
			return null;
		}
		//uiFrame uiF = FrameSelector.createFrame(frame, frameContainer);
		for (int i = 0; i < objects.length; i++)
		//Frame frame = uiF.getFrame();	
			ObjectEditor.editInContainer(uiF, objects[i], containers[i]);
		
		uiF.setSize();
		uiF.validate();
		/*
		uiF.setVisible(true);
		*/
		return uiF;
		
	}
	public static void secondaryEdit(uiFrame frame, Object obj) {				frame.createSecondaryScrollPane(obj);				
		//Container treeContainer = frame.newTreeContainer();		//if (treeContainer == null) return null;
		/*		Container treeComponent = frame.treeComponent();
		uiObjectAdapter treeRoot = uiGenerator.generateInUIPanel(frame, obj, null,   null, treeContainer, null, treeComponent);		frame.setTreeRoot(treeRoot);		*/
		//return treeContainer;				}
	public static synchronized ObjectAdapter editInContainer(uiFrame frame, Object obj, VirtualContainer c) {
		return editInContainer(frame, obj, c, true);
	}
	
	public static synchronized ObjectAdapter editInContainer(uiFrame frame, Object obj, VirtualContainer c, boolean rootPanel) {
		//register();
		return uiGenerator.generateInUserContainer(frame, obj, c, rootPanel);
	}
	public static synchronized ObjectAdapter editInScrolledBrowsableContainer(uiFrame frame, Object obj, VirtualContainer scrollPane) {
		//register();
		//Panel p = new Panel();
		VirtualContainer p = PanelSelector.createPanel();
		p.setLayout(new BorderLayout());
		//sp.setLayout(new BorderLayout());			
		scrollPane.add(p);
		p.setBackground(scrollPane.getBackground());
		return uiGenerator.generateInBrowsableContainer(frame, obj, p);
		
	}
	public static synchronized ObjectAdapter editInBrowsableContainer(uiFrame frame, Object obj, VirtualContainer pane) {
		register();
		//Panel p = new Panel();
		//VirtualContainer p = PanelSelector.createPanel();
		//p.setLayout(new BorderLayout());
		//sp.setLayout(new BorderLayout());			
		//scrollPane.add(p);
		//p.setBackground(scrollPane.getBackground());
		return uiGenerator.generateInBrowsableContainer(frame, obj, pane);
		
	}
	public static synchronized ObjectAdapter edit(uiFrame frame, Object obj) {
		return uiGenerator.generateInNewBrowsableContainer(frame, obj, null, (ObjectAdapter) null, null);
		//frame.setVisible(true);
		//return editInBrowsableContainer(frame, obj, null);
		
	}
	
	public static Vector firstLevelHTs(ObjectAdapter adapter) {
	  Vector results = new Vector();
	  (new AddSelfAdapterVisitor(adapter)).traverseHTs(adapter, results, new Vector());
	   return results;  }    public static Vector nextLevelHTs(Vector prevLevelHTs) {	  Vector results = new Vector();	  if (prevLevelHTs.size() > 0)
		(new AddSelfAdapterVisitor(null)).traverseChildHTs(prevLevelHTs, results);
	  return results;	    }
  
  public static ObjectAdapter getFirstKey(Vector hts ) {
	  if (hts.size() > 0) {
			HashtableAdapter htAdapter = ((HashtableAdapter) hts.elementAt(0));
			if (htAdapter.getChildAdapterCount() > 0)
				return htAdapter.getChildAdapterAt(0);
			else
				return null;
			//uiObjectAdapter firstKey = htAdapter.getChildAdapterAt(0);
			//return firstKey;
			
			
		}
	  return null;
  }
  public static synchronized CompleteOEFrame editWithAttributes(Object o, Hashtable selfAttributes) {
  	return editWithAttributes (o, selfAttributes, null);
  	
  }
  public static synchronized CompleteOEFrame editWithAttributes(Object o, Hashtable selfAttributes, Vector childrenAttributes) {
	  CompleteOEFrame uiF = edit (o, selfAttributes, childrenAttributes);
  	//uiF.setAttributes(selfAttributes, childrenAttributes);
//  	uiF.setAttributes(selfAttributes);
//  	uiF.setChildrenAttributes(childrenAttributes);
  	
  	return uiF;
  	
  }
  /**
 * @deprecated Use {@link AClassDescriptor#writeWithAttributeRegister(boolean)} instead
 */
public static void writeWithAttributeRegister (boolean newVal) {
	AClassDescriptor.writeWithAttributeRegister(newVal);
}
  /**
 * @deprecated Use {@link AClassDescriptor#withAttributeRegisterer()} instead
 */
public static boolean withAttributeRegisterer() {
	return AClassDescriptor.withAttributeRegisterer();
}
  
  public static void toggleWithAttributeRegisterer() {
	  AClassDescriptor.withAttributeRegisterer = !AClassDescriptor.withAttributeRegisterer;
  }
  
  
  public static synchronized  uiFrame editWithLabels (Object o, String selfLabel, String[] childrenLabels) {
  	Vector childrenAttributes = new Vector();
  	for (int i = 0; i < childrenLabels.length; i++) {
  		Hashtable childAttributes = new Hashtable();
  		childAttributes.put(AttributeNames.LABEL, childrenLabels[i]);
  		childrenAttributes.add(childAttributes);
  	}
  	uiFrame uiF = (uiFrame) editWithAttributes (o, null, childrenAttributes);
  	uiF.setTitle(selfLabel);
  	return uiF;
  }  
  public static uiFrame editWithTitle (Object o, String selfLabel) {
  
  	uiFrame uiF = (uiFrame) edit (o);
  	uiF.setTitle(selfLabel);
  	return uiF;
  }
  public static synchronized uiFrame profiledEdit (Object o) {
	  uiGenerator.setProfile(true);
	  uiGenerator.printTime("start of profiling");
	  //return ObjectEditor.edit(o, true, new MenuSetter(), new AMenuDescriptor());
	  return (uiFrame) ObjectEditor.edit(o, AClassDescriptor.withAttributeRegisterer(), defaultMenuSetter, new AMenuDescriptor(), null, null);
	  //return edit(o, null);
  }
  public static synchronized uiFrame editWithPromotedChild(Object o) {
	  Hashtable table = new Hashtable();
		table.put(AttributeNames.PROMOTE_ONLY_CHILD, true);
		return (uiFrame) ObjectEditor.editWithAttributes(o, table);
  }
  public static synchronized CompleteOEFrame edit (Object o) {
    return ObjectEditor.edit(o, null, null);
  }
  public static synchronized uiFrame edit (Object o, boolean showMenus) {
	    return ObjectEditor.edit(o, showMenus, (Hashtable) null, null);
	  }
  public static synchronized CompleteOEFrame edit(Object o, Class widgetType) {
	  String widgetTypeName = widgetType.getName();
	  Hashtable attributeTable = new Hashtable();
		attributeTable.put(AttributeNames.PREFERRED_WIDGET, widgetTypeName);
		return ObjectEditor.edit(o, attributeTable, null);
  }
  public static synchronized CompleteOEFrame edit (Object o, Hashtable selfAttributes, Vector childrenAttributes) {
//	  /uiGenerator.printTime("start of edit");
	  //return ObjectEditor.edit(o, true, new MenuSetter(), new AMenuDescriptor());
	  return ObjectEditor.edit(o, AClassDescriptor.withAttributeRegisterer(), defaultMenuSetter, new AMenuDescriptor(), selfAttributes, childrenAttributes);
	  //return edit(o, null);
  }
  public static synchronized CompleteOEFrame edit (Object o, ObjectAdapter sourceAdapter) {
	  //return ObjectEditor.edit(o, true, defaultMenuSetter, new AMenuDescriptor());
	  return ObjectEditor.edit(o, AClassDescriptor.withAttributeRegisterer(), defaultMenuSetter, new AMenuDescriptor(), sourceAdapter, null, null);
	  //return edit(o, null);
  }
  
  public static synchronized uiFrame edit (Object o, boolean showMenus, Hashtable selfAttributes, Vector childrenAttributes) {
	  return (uiFrame) ObjectEditor.edit(o, showMenus, defaultMenuSetter, new AMenuDescriptor(), selfAttributes, childrenAttributes);
	  //return edit(o, null);
  }
  
  /*
  public static uiFrame edit (Object o) {
	  return edit(o, null);
  }
  */
  public static ClassAdapter setModel (Object o, 
			String property, Component component) {
	   String[] properties = {property};
	   Component[] components = {component};
	   return setModels (o, properties, components);	   
  }
  public static ClassAdapter setModel (Object o, 
			String property, VirtualComponent virtualComponent) {
	   String[] properties = {property};
	   VirtualComponent[] virtualComponents = {virtualComponent};
	   return setModels (o, properties, virtualComponents);	   
 }
  public static ClassAdapter setModel (uiFrame frame, Object o, 
			String property, VirtualComponent virtualComponent) {
	   String[] properties = {property};
	   VirtualComponent[] virtualComponents = {virtualComponent};
	   return setModels (frame, o, properties, virtualComponents);	   
  }
  public static ClassAdapter setModel (uiFrame frame, Object o, 
			String property, Component component) {
	  VirtualComponent vc = AWTComponent.virtualComponent(component);
	 return  setModel (frame, o, property, vc);
	  
  }
  public static uiFrame createOEFrame() {
		uiFrame frame = OEFrameSelector.createFrame(null, null);
		 frame.setIsDummy(true);
		 return frame;
  }
  public static uiFrame createOEFrame(VirtualFrame frame, VirtualContainer container) {
		uiFrame oeFrame = OEFrameSelector.createFrame(frame, container);
		 oeFrame.setIsDummy(true);
		 return oeFrame;
 }
  public static uiFrame createOEFrame(JFrame frame) {
	  VirtualFrame virtualFrame = SwingFrame.virtualFrame(frame);
	  if (virtualFrame == null)
		  return createOEFrame((VirtualFrame) null, (VirtualContainer) null);
	  else
	  return createOEFrame (virtualFrame, virtualFrame.getContentPane());
}
  public static ClassAdapter setModels (uiFrame frame, Object o, 
			String[] properties, VirtualComponent[] virtualComponents) {
	  	Vector<String> unconsumedProperties = util.misc.Common.deepArrayToVector(properties);	  
	  	Vector<VirtualComponent> unconsumedComponents = util.misc.Common.deepArrayToVector(virtualComponents);
	  	Vector<String> currentProperties = new Vector();
	  	Vector<VirtualComponent> currentComponents = new Vector();
	  	//uiFrame frame = uiFrameSelector.createFrame(null, null);
		 //frame.setIsDummy(true);
		 ClassAdapter classAdapter = null;
	  for (;;) {
	  	if (unconsumedProperties.size() == 0)
	  		break;
		String[] strings = {unconsumedProperties.get(0)};
	  	VirtualComponent[] components = {unconsumedComponents.get(0)};
	  	currentProperties.clear();
	  	currentComponents.clear();
	  	for (int i = 0; i < unconsumedProperties.size(); i++) {
	  		String nextProperty = unconsumedProperties.get(i);
	  		VirtualComponent component = unconsumedComponents.get(i);
	  		if (currentProperties.contains(nextProperty))
	  			continue;
	  		currentProperties.add(nextProperty);
	  		currentComponents.add(component);
	  		//unconsumedProperties.remove(i);
	  		//unconsumedComponents.remove(i);	  		
	  	}
	  	
	  	//uiFrame frame = uiFrameSelector.createFrame(null, null);
		 //frame.setIsDummy(true);
	  	
	  
	  	String[] currentPropertiesArray = (String[]) currentProperties.toArray(strings);
	  	VirtualComponent[] currentComponentsArray = (VirtualComponent[]) currentComponents.toArray(components);
	  	classAdapter = edit (frame, o, currentPropertiesArray, currentComponentsArray);
	  	if (classAdapter != null & !frame.getBrowser().getAdapterHistory().contains(classAdapter))
			  frame.getBrowser().getAdapterHistory().addAdapter(classAdapter);
	  	for (int i = 0; i < currentComponents.size(); i++) {
	  		VirtualComponent component = currentComponents.get(i);
	  		int index = unconsumedComponents.indexOf(component);
	  		unconsumedProperties.remove(index);
	  		unconsumedComponents.remove(index);	  		
	  	}
	  }
	  /*
	  if (classAdapter != null & !frame.getBrowser().getAdapterHistory().contains(classAdapter))
		  frame.getBrowser().getAdapterHistory().addAdapter(classAdapter);
		  */
	  return classAdapter;
	   
		//return edit (frame, o, properties, virtualComponents);
}
  public static ClassAdapter setModels (Object o, 
			String[] properties, VirtualComponent[] virtualComponents) {
	  	/*
	  Vector<String> unconsumedProperties = util.Misc.deepArrayToVector(properties);	  
	  	Vector<VirtualComponent> unconsumedComponents = util.Misc.deepArrayToVector(virtualComponents);
	  	Vector<String> currentProperties = new Vector();
	  	Vector<VirtualComponent> currentComponents = new Vector();
	  	*/
	  	uiFrame frame = OEFrameSelector.createFrame(null, null);
		 frame.setIsDummy(true);
		 return setModels(frame,  o, 
			 properties,  virtualComponents);
		 /*
		 uiClassAdapter classAdapter = null;
	  for (;;) {
	  	if (unconsumedProperties.size() == 0)
	  		break;
		String[] strings = {unconsumedProperties.get(0)};
	  	VirtualComponent[] components = {unconsumedComponents.get(0)};
	  	currentProperties.clear();
	  	currentComponents.clear();
	  	for (int i = 0; i < unconsumedProperties.size(); i++) {
	  		String nextProperty = unconsumedProperties.get(i);
	  		VirtualComponent component = unconsumedComponents.get(i);
	  		if (currentProperties.contains(nextProperty))
	  			continue;
	  		currentProperties.add(nextProperty);
	  		currentComponents.add(component);
	  		//unconsumedProperties.remove(i);
	  		//unconsumedComponents.remove(i);	  		
	  	}
	  	
	  	//uiFrame frame = uiFrameSelector.createFrame(null, null);
		 //frame.setIsDummy(true);
	  	
	  
	  	String[] currentPropertiesArray = (String[]) currentProperties.toArray(strings);
	  	VirtualComponent[] currentComponentsArray = (VirtualComponent[]) currentComponents.toArray(components);
	  	classAdapter = edit (frame, o, currentPropertiesArray, currentComponentsArray);
	  	for (int i = 0; i < currentComponents.size(); i++) {
	  		VirtualComponent component = currentComponents.get(i);
	  		int index = unconsumedComponents.indexOf(component);
	  		unconsumedProperties.remove(index);
	  		unconsumedComponents.remove(index);	  		
	  	}
	  }
	  return classAdapter;
	   
		//return edit (frame, o, properties, virtualComponents);
		 * 
		 */
  }
  public static ASourceOperationsModel getSourceModel(uiFrame editor) {
	  return editor.getModelRegistry().getSourceModel();
  }
  public static void bindToAllSource(uiFrame editor, JMenuItem allSourceMenuItem) {
	  ASourceOperationsModel sourceModel = editor.getModelRegistry().getSourceModel();
	  ObjectEditor.bind(editor, sourceModel, "allSource", allSourceMenuItem);
  }
  
  public static AnUndoRedoModel getUndoRedoModel(uiFrame editor) {
	  return editor.getModelRegistry().getUndoRedoModel();
  }
  
  public static void bindToUndoRedo(uiFrame editor, JButton undoButton, JButton redoButton) {
	  AnUndoRedoModel undoRedoModel = editor.getModelRegistry().getUndoRedoModel();
	  ObjectEditor.bind(editor, undoRedoModel, "undo", undoButton);
	  ObjectEditor.bind(editor, undoRedoModel, "redo", redoButton);
  }  
  
  public static void bindToUndo(uiFrame editor, VirtualMenuItem undoButton, VirtualButton redoButton) {
	  AnUndoRedoModel undoRedoModel = editor.getModelRegistry().getUndoRedoModel();
	  //ObjectEditor.bind(editor, undoRedoModel, "undo", undoMenuItem);
	  //ObjectEditor.bind(editor, undoRedoModel, "redo", redoMenuItem);
	  
  }
  
  public static void bindToUndoRedo(uiFrame editor, JMenuItem undoMenuItem, JMenuItem redoMenuItem) {
	  AnUndoRedoModel undoRedoModel = editor.getModelRegistry().getUndoRedoModel();
	  ObjectEditor.bind(editor, undoRedoModel, "undo", undoMenuItem);
	  ObjectEditor.bind(editor, undoRedoModel, "redo", redoMenuItem);
  }
  public static void bindToUndo(uiFrame editor, JMenuItem undoMenuItem) {
	  AnUndoRedoModel undoRedoModel = editor.getModelRegistry().getUndoRedoModel();
	  ObjectEditor.bind(editor, undoRedoModel, "undo", undoMenuItem);
  }
  public static void bindToUndo(JMenuItem undoMenuItem) {
	  Vector frames = uiFrameList.getList();
	  if (frames.size() == 0) {
		  NoFramesForUndoBind.newExample(ObjectEditor.class);
//		  Tracer.error("Bind first the application objects to widgets before binding the undo");
		  return;
	  }
	  uiFrame frame = (uiFrame) frames.elementAt(0);
	  bindToUndo(frame, undoMenuItem);
	
  }
  public static void bindToRedo(uiFrame editor, JMenuItem redoMenuItem) {
	  AnUndoRedoModel undoRedoModel = editor.getModelRegistry().getUndoRedoModel();
	  ObjectEditor.bind(editor, undoRedoModel, "redo", redoMenuItem);
  }
  public static void bindToCut(uiFrame editor, JButton cutButton) {
	  ADoOperationsModel doModel = editor.getModelRegistry().getDoModel();
	  ObjectEditor.bind(editor, doModel, "cut", cutButton);
  }
  public static void bindToCopy(uiFrame editor, JButton copyButton) {
	  ADoOperationsModel doModel = editor.getModelRegistry().getDoModel();
	  ObjectEditor.bind(editor, doModel, "copy", copyButton);
  }
  public static void bindToPaste(uiFrame editor, JButton pasteButton) {
	  ADoOperationsModel doModel = editor.getModelRegistry().getDoModel();
	  ObjectEditor.bind(editor, doModel, "paste", pasteButton);
  }
  public static void bindToPasteAfter(uiFrame editor, JButton pasteButton) {
	  ADoOperationsModel doModel = editor.getModelRegistry().getDoModel();
	  ObjectEditor.bind(editor, doModel, "pasteAfter", pasteButton);
  }
  
  public static ClassAdapter setModels (Object o, 
			String[] properties, Component[] components) {
	  /*
	   VirtualComponent[] virtualComponents = new VirtualComponent[components.length];
	   for (int i = 0; i < components.length; i++) {
		   virtualComponents[i] = AWTComponent.virtualComponent(components[i]);
	   }
	   return setModels (o, properties, virtualComponents);
	   */
//	  uiFrame theFrame = createOEFrame();
	  uiFrame theFrame = nullFrame();
	  return setModels(theFrame, o, properties, components);
  }
  public static ClassAdapter setModels (uiFrame theFrame, Object o, 
			String[] properties, Component[] components) {
	   VirtualComponent[] virtualComponents = new VirtualComponent[components.length];
	   for (int i = 0; i < components.length; i++) {
		   virtualComponents[i] = AWTComponent.virtualComponent(components[i]);
	   }
	   return setModels (theFrame, o, properties, virtualComponents);
}
  public static ClassAdapter edit (uiFrame theFrame, Object o, 
		  			String[] properties, VirtualComponent[] virtualComponents) {
	  if (theFrame == null) {
		  theFrame = nullFrame();
//		  theFrame = OEFrameSelector.createFrame(null, null);
//		  theFrame.setIsDummy(true);
	  }
	  ObjectAdapter adapter = uiGenerator.toTopAdapter(theFrame, o);
	  theFrame.addUserAdapter(adapter);
	  if (!(adapter instanceof ClassAdapter)) {
		  Tracer.fatalError("Object " + o + " is a primitive object and has no properties");
		  return null;
	  }	  
	  ClassAdapter classAdapter = (ClassAdapter) adapter;
	  uiGenerator.deepSetAttributes(classAdapter);
	  for (int i = 0; i < properties.length; i ++) {
		  ObjectAdapter childAdapter = null;
		  if (properties[i].indexOf('.') == -1)
		  //uiObjectAdapter childAdapter = classAdapter.getChildAdapterMapping(properties[i]);
			  childAdapter = classAdapter.getChildAdapterMapping(properties[i]);
		  else
		    childAdapter = classAdapter.pathToObjectAdapter(properties[i]);
		  if (childAdapter == null) {
			  UnknownPropertyBind.newCase(properties[i], o, ObjectEditor.class);
//			  Tracer.error("Object " + o + " does not have property " + properties[i]);
			  continue;
			  //return null;
		  }
		  if (! (childAdapter instanceof PrimitiveAdapter)) {
//			  Tracer.error("Property " + properties[i] + " is not primitive");
			  CompositePropertyBind.newCase(properties[i], o, ObjectEditor.class);
			  continue;
			  //return null;
		  }
		  if (i >= virtualComponents.length) {
			  PropertyMissingBoundComponent.newCase(properties[i], o, ObjectEditor.class);
//			  Tracer.error("Please provide UI component for property: " + properties[i]);
			  continue;
			  //return null;
		  }
		  Connector.linkAdapterToComponent(childAdapter, virtualComponents[i]);
	  }
	  uiGenerator.deepProcessSetAttributes(classAdapter);
	  return classAdapter;
  }
	public static uiFrame browseIfHashtable (Object o) {
		ObjectAdapter adapter = uiGenerator.toTopAdapter(o);
		//System.out.println("To adapter:" + adapter.getID());
		Vector firstLevels = firstLevelHTs(adapter);		
		if (firstLevels.size() == 0)
			return edit(o, (String) null);
		else {
			System.out.println("Found HT: " + o);
			Vector secondLevels = null;			
			ObjectAdapter firstKey = getFirstKey(firstLevels);
			Object firstKeyValue;
			if (firstKey == null)
				firstKeyValue = "Table element in parent panel expanded here";
			else {				
				firstKeyValue =  firstKey.getExpandedAdapter().getRealObject();
				secondLevels = nextLevelHTs(firstLevels);
			}			
			System.out.println("First key Value" +  firstKeyValue);
			//Vector secondLevels = nextLevelHTs(firstLevels);
			if (secondLevels == null || secondLevels.size() == 0) {
				return treeBrowse(o, firstKeyValue);
			} else {
				System.out.println("Foundscond level HT: " + o);
				ObjectAdapter secondLevelFirstKey = getFirstKey(secondLevels);
				Object secondLevelFirstKeyValue;
				if (secondLevelFirstKey != null) 
				   secondLevelFirstKeyValue = secondLevelFirstKey.getExpandedAdapter().getRealObject();
				else
				   secondLevelFirstKeyValue = "Table element in parent panel expanded here.";				
				System.out.println("First key valuye of second level" +  secondLevelFirstKeyValue);
				return treeBrowse(o, firstKeyValue, secondLevelFirstKeyValue);
				
			}
		}		
		
		//return edit(o, null);		
		//register();
		/*
		String s = "Selection in tree panel expanded here";
		uiFrame frame = bus.uigen.uiGenerator.generateUIFrame(s, (String) null);
		*/
		/*
		
		Frame myFrame = new Frame();
		//myFrame.setLayout(new BorderLayout());
		
		Container sp = ScrollPaneSelector.createScrollPane();
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		//sp.setLayout(new BorderLayout());			
		sp.add(p);
		
		uiFrame frame = FrameSelector.createFrame(myFrame, myFrame);		uiGenerator.generateInBrowsableContainer(frame, o, p);						
		myFrame.add(sp, BorderLayout.CENTER);
		frame.validate();		*/
				/*
		
		//Container sp = ScrollPaneSelector.createScrollPane();
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		//sp.setLayout(new BorderLayout());			
		//sp.add(p);
		
		uiFrame frame = FrameSelector.createFrame(myFrame, myFrame);		uiGenerator.generateInUserContainer(frame, o, p);						
		myFrame.add(p, BorderLayout.CENTER);		//frame.setSize(300, 200);
		//frame.validate();
		*/		/*		uiFrame frame = FrameSelector.createFrame();
				
		treeEdit(frame, o);
		frame.createSecondaryScrollPane("Selection in main displayed here");
		*/
		
		//frame.showSecondaryPanel();
		//frame.validate();	
		/*
		frame.setSize(300, 200);
		//frame.setTitle();		
		//frame.validate();
		frame.setVisible(true);
		return frame;
		*/
		//uiFrame frame = edit ("Selection in tree panel expanded here", null);
		//frame.createTreePanel(o);
		//return frame;
		/*
		registerEditors();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o);
		retVal.setVisible(true);
		retVal.setSize(300, 200);
		return retVal;
		*/
	}
	public static uiFrame treeEditFlawed(Object o) {
		uiFrame frame = OEFrameSelector.createFrame(o);
		AToolbarManager.addUIFrameToolBarButtons(frame);		if (frame.getToolbarManager().toolbarCount() == 1) 
		frame.toolBar();
		frame.createTreePanel(o);
		
		frame.setVisible(true);
		return frame;
		//return edit(o, null);
		/*
		registerEditors();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o);
		retVal.setVisible(true);
		retVal.setSize(300, 200);
		return retVal;
		*/
	}
	public static Object syncEdit(Object o) {
		return new ObjectEditor().syncEditInstance(o);
		/*
		ObjectEditorRunnable thread = new ObjectEditorRunnable(this);
		thread.run();
		return new ObjectEditor().syncEditInstance(o);	
		*/
		/*
		registerEditors();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o);
		retVal.setVisible(true);
		retVal.setSize(300, 200);
		return retVal;
		*/
	}
	
	public void actionPerformed(VirtualActionEvent e) {
		//this.notify();
		notifyMe();
	}
	
	public synchronized void notifyMe() {
		//syncRetVal = syncFrame.getAdapter().getValue();
		try {
			this.notify();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public synchronized  Object syncEditInstance(Object o) {
		syncObject = o;		
		uiFrame f = (uiFrame) edit(o);
		f.addDoneItemAndButton(this);
		/*
		MenuItem okMenuItem = f.addDoneItem();		
		okMenuItem.addActionListener(this);
		JButton okButton= f.addDoneButton();	
		*/
		//Thread eventThread = new java.awt.EventDispatchThread().run();
		//run();		
		try {
			this.wait();
			//eventThread.destroy();
		} catch (Exception e) 
		{
			System.out.println(e);
		}
		f.setVisible(false);
		return f.getAdapter().getValue();
		
		//return syncRetVal;
		
		
	}
	transient Object syncObject;
	transient Object syncRetVal;
	transient uiFrame syncFrame;
	
	public void run() {
		
		syncFrame = (uiFrame) edit(syncObject);
		VirtualMenuItem okButton = syncFrame.addDoneItem();		
		okButton.addActionListener(this);
		//return f.getAdapter().getValue();				
	}
		
	public static uiFrame edit(Object o, String title) {
		//registerEditors();
		register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o, title);
		retVal.setVisible(true);
		retVal.setSize();
		//retVal.setSize(300, 200);
		return retVal;
	}
	/*
	public static uiFrame edit(Object o, boolean showMenus) {
		
		//F.O. passing in null for string as if edit (0) called.
		register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o, null, showMenus );
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	*/
	
	public static uiFrame edit(Object o, boolean showMenus, VirtualContainer externalPanel) {
		
		//F.O. passing in null for string as if edit (0) called.
		register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o, null, showMenus );
		
		//retVal.addToBottomPanel(externalPanel);//and add to retval aka main object uiframe
		retVal.addToMiddlePanel(externalPanel);
		
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	

	
	public static synchronized CompleteOEFrame edit(Object o, boolean showMenus, MenuSetter menuTest, AMenuDescriptor menuDescriptor, Hashtable selfAttributes, Vector childrenAttributes) {
		return edit(o, showMenus, menuTest, menuDescriptor, (ObjectAdapter) null, selfAttributes, childrenAttributes);
		/*
		//F.O. passing in null for string as if edit (0) called.
		registerEditors();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o, null, showMenus, menuTest, menuDescriptor );
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
		*/
	}
	public static synchronized CompleteOEFrame edit(Object o, boolean showMenus, MenuSetter menuTest, AMenuDescriptor menuDescriptor) {
		return edit(o, showMenus, menuTest, menuDescriptor, null, null);
		/*
		//F.O. passing in null for string as if edit (0) called.
		registerEditors();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o, null, showMenus, menuTest, menuDescriptor );
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
		*/
	}
public static synchronized CompleteOEFrame edit(Object o, boolean showMenus, MenuSetter menuTest, AMenuDescriptor menuDescriptor, ObjectAdapter sourceAdapter, Hashtable selfAttributes, Vector childrenAttributes) {
	if (isHeadless()) {

//	if (GraphicsEnvironment.isHeadless()) {
		System.err.println("Headless program, not generating UI");
		CompleteOEFrame retVal = new ADummyCompleteOEFrame();
//		retVal.setIsDummy(true);
		return retVal;
	}
	initStatic();
		//F.O. passing in null for string as if edit (0) called.
		//registerEditors();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o, null, showMenus, menuTest, menuDescriptor, sourceAdapter, selfAttributes, childrenAttributes );
		retVal.setSize();
		retVal.setVisible(true);
		 //KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
		 //KeyboardFocusManager.getCurrentKeyboardFocusManager().
		retVal.getFrame().getContentPane().requestFocus();
		
		//retVal.setSize(300, 200);
		return retVal;
	}
    
		

    public static synchronized uiFrame edit(Object o, boolean showMenus, 
    		MenuSetter menuTest, AMenuDescriptor menuDescriptor, VirtualContainer externalPanel) {
		
		//F.O. passing in null for string as if edit (0) called.
		//register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o, null, showMenus, menuTest, menuDescriptor);
		
		//retVal.addToBottomPanel(externalPanel);//and add to retval aka main object uiframe
		retVal.addToMiddlePanel(externalPanel);
		
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
		
	
	public static synchronized uiFrame edit(Object o, boolean showMenus, MenuSetter menuTest, AMenuDescriptor menuDescriptor, VirtualContainer externalPanel,
		String userName, String programName, String remoteUserRMIName){
		runUIComponentOnly = true;
		unserializableOutput = true;
		shareBeans = true;
		
		uiFrame retVal = edit(o,showMenus,menuTest, menuDescriptor, externalPanel);
		
		String commandHeader = "java";
		String securityProperty = System.getProperty("java.security.policy");
		if(securityProperty!=null){
			commandHeader = commandHeader+" -Djava.security.policy="+securityProperty;
		}
		
		String loggerUIRMIName = "//localhost:1100/"+userName+"_"+programName+"_LoggerUI";
		
		try{
			String command = commandHeader+" logging.logger.LoggerUIStarter "+loggerUIRMIName;
			Process p = Runtime.getRuntime().exec(command,null);
		} catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		if (ObjectEditor.colabMode()) {
		ObjectRegistry.createLoggerUI(programName,remoteUserRMIName,userName,
			((remoteUserRMIName.equals("null"))?"true":"false"),"true",false,loggerUIRMIName);
		}
		
		return retVal;
	}	

		
	public static uiFrame oldeditOverlayList(Vector objectList) {
		if (objectList.size() == 0) return null;
		register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0));		
		ObjectAdapter firstAdapter = retVal.getAdapter();
		for (int i = 1; i < objectList.size(); i++) {
			retVal.replaceFrame(objectList.elementAt(i));
		}
		for (int i = 1; i < objectList.size(); i++) {
			retVal.backAdapter();
		}
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
//	ignores menu omitting extentions
	public static synchronized uiFrame editOverlayList(Vector objectList) {
		if (objectList.size() == 0) return null;
		//registerEditors();
		//uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0));
		uiFrame retVal = (uiFrame) ObjectEditor.edit(objectList.elementAt(0));
		ObjectAdapter firstAdapter = retVal.getAdapter();
		
//comp.
		//need to reset in a diff place since doing all the generation in one methods.
		//put it here after each frame is generated
		bus.uigen.uiGenerator.resetAllGUIComponents();
		bus.uigen.uiGenerator.resetWidgets();
		
		
		for (int i = 1; i < objectList.size(); i++) {
			retVal.replaceFrame(objectList.elementAt(i));
			//comp.
			//need to reset in a diff place since doing all the generation in one methods.
			//put it here after each frame is generated
			bus.uigen.uiGenerator.resetWidgets(); 
			//assume call belongs here instead of below loop cuz. it is only calling backadapter.
			
		}
		for (int i = 1; i < objectList.size(); i++) {
			retVal.backAdapter();
		}
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	public static uiFrame  editOverlayList(Vector objectList, Vector nameList) {
		if (objectList == null) return null;
		int objectListSize = objectList.size();		
		if (objectListSize == 0) return null;		
		register();
		int nameListSize;
		if (nameList == null)
			nameListSize = 0;
		else
			nameListSize = nameList.size();
		uiFrame retVal;
		if (nameListSize > 0) {
			retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0), (String) nameList.elementAt(0));
		} else
			retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0));		
		int i;
		for (i = 1; i < objectListSize && i < nameListSize; i++) {
			retVal.replaceFrame(objectList.elementAt(i), (String) nameList.elementAt(i));
		}
		for (int j = i; j < objectListSize; j++)
			retVal.replaceFrame(objectList.elementAt(j));
		for (int j = 1; j < objectList.size(); j++) {
			retVal.backAdapter();
		}
		/*		
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0));
		for (int i = 1; i < objectList.size(); i++) {
			retVal.newScrollPaneRight(objectList.elementAt(i));
		}
		*/
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
//	comp.
//	below extends previous editOList by accepting showmenus which decides if menus should be created or not...originally done for 3dfm stuff w/ segi
//	didn't code for removing specific menus for retargeting/composition stuff
		public static synchronized uiFrame editOverlayList(Vector objectList, boolean showMenus) {  
			//registerEditors();
			// moving from below
			bus.uigen.uiGenerator.resetAllGUIComponents();
			//uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame((objectList.elementAt(0)),null, showMenus);	
			uiFrame retVal = ObjectEditor.edit(objectList.elementAt(0), showMenus, (Hashtable) null, null);
			
			ObjectAdapter firstAdapter = retVal.getAdapter();
			
//	comp.
			//need to reset in a diff place since doing all the generation in one method.
			//put it here after each frame is generated
			//bus.uigen.uiGenerator.resetAllGUIComponents();
			bus.uigen.uiGenerator.resetWidgets();
			bus.uigen.uiGenerator.resetAdapters(objectList.elementAt(0));
			for (int i = 1; i < objectList.size(); i++) {
				retVal.replaceFrame(objectList.elementAt(i));
//	comp.
				//need to reset in a diff place since doing all the generation in one method.
				//put it here after each frame is generated
				bus.uigen.uiGenerator.resetWidgets();
				bus.uigen.uiGenerator.resetAdapters(); 
				//assume call belongs here instead of below loop cuz. it is only calling backadapter.
				
			}
			for (int i = 1; i < objectList.size(); i++) {
				retVal.backAdapter();
			}
			retVal.setVisible(true);
			//retVal.setSize(300, 200);
			return retVal;
		}
		
		public static uiFrame replaceOverlayList(Vector objectList, uiFrame retVal) {// instead of generating a bunch of stuff for the frame when refreshing 
																				//just replace inner frame components
			if (objectList.size() == 0) return null;
			retVal.replaceFrame(objectList.elementAt(0));
			ObjectAdapter firstAdapter = retVal.getAdapter();
			bus.uigen.uiGenerator.resetAllGUIComponents();
			bus.uigen.uiGenerator.resetWidgets();
			bus.uigen.uiGenerator.resetAdapters(); 

			
			for (int i = 1; i < objectList.size(); i++) {
				retVal.replaceFrame(objectList.elementAt(i));
				
//	comp.
				//need to reset in a diff place since doing all the generation in one methods.
				//put it here after each frame is generated
				bus.uigen.uiGenerator.resetWidgets();
				bus.uigen.uiGenerator.resetAdapters(); 
				//assume call belongs here instead of below loop cuz. it is only calling backadapter.
				
			}
			for (int i = 1; i < objectList.size(); i++) {
				retVal.backAdapter();
			}
			retVal.setVisible(true);
			//retVal.setSize(300, 200);
			return retVal;
		}
	public static uiFrame editList(Vector objectList) {
		if (objectList.size() == 0) return null;
		register();
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0));
		for (int i = 1; i < objectList.size(); i++) {
			retVal.newScrollPaneRight(objectList.elementAt(i));
		}		
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	public static uiFrame editList(Vector objectList, Vector nameList) {
		if (objectList == null) return null;
		int objectListSize = objectList.size();		
		if (objectListSize == 0) return null;		
		register();
		int nameListSize;
		if (nameList == null)
			nameListSize = 0;
		else
			nameListSize = nameList.size();
		uiFrame retVal;
		if (nameListSize > 0) {
			retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0), (String) nameList.elementAt(0));
		} else
			retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0));		
		int i;
		for (i = 1; i < objectListSize && i < nameListSize; i++) {
			retVal.newScrollPaneRight(objectList.elementAt(i), (String) nameList.elementAt(i));
		}
		for (int j = i; j < objectListSize; j++)
			retVal.newScrollPaneRight(objectList.elementAt(j));
		/*		
		uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(objectList.elementAt(0));
		for (int i = 1; i < objectList.size(); i++) {
			retVal.newScrollPaneRight(objectList.elementAt(i));
		}
		*/
		retVal.setVisible(true);
		//retVal.setSize(300, 200);
		return retVal;
	}
	public static uiFrame editAsApplet(Object o) {
		//uiFrame retVal = bus.uigen.uiGenerator.generateUIFrame(o);
		register();
		Frame f = new Frame();
		java.applet.Applet a = new java.applet.Applet();
		VirtualContainer vc = AWTContainer.virtualContainer(a);
		bus.uigen.uiGenerator.generateUI(vc, o);
		f.add(a);
		f.setVisible(true);		
		return (null);
	}
	
	
	
	void saveState() {
		try {
			/*
		  ObjectOutputStream f = new ObjectOutputStream(new FileOutputStream("ObjectEditor.obj"));
		  //f.writeObject(className);
		  //System.out.println("frame_width" + frame_width);
		  f.writeObject(this);
		  f.close();
		  */
			OEMisc.saveState(this);
		}
		catch (Exception e) {
			e.printStackTrace();
			//System.out.println("exception" + e);
		}
	}
	
	static transient boolean stateLoaded = false;
	int my_frame_width, my_frame_height;
	 static void loadStaticState() {
		if (stateLoaded) return;
		try {
			ObjectEditor oe = (ObjectEditor) OEMisc.loadState(ObjectEditor.class);
		  //ObjectInputStream f = new ObjectInputStream(new FileInputStream("ObjectEditor.obj"));
		  //ObjectEditor oe = (ObjectEditor) f.readObject();
		  //className = (String) f.readObject();
		   //System.out.println("frame_width" + frame_width);
		  frame_width = oe.my_frame_width;
		  //System.out.println("frame_width" + frame_width);
		  frame_height = oe.my_frame_height;
		  //f.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		stateLoaded = true;
	}
		
	
	void loadState() {
		try {
			/*
		  ObjectInputStream f = new ObjectInputStream(new FileInputStream("ObjectEditor.obj"));
		  ObjectEditor oe = (ObjectEditor) f.readObject();
		  */
			ObjectEditor oe = (ObjectEditor) OEMisc.loadState(this);
			if (oe == null) {
				initState();
				return;
			}
		  //className = (String) f.readObject();
		  className = oe.getCurrentClassName();
		  showingClasses = oe.showingClasses;
		  showingEntireFolder = oe.showingEntireFolder;
		  showingCurrentClassName = oe.showingCurrentClassName;
		  showingClassNameList = oe.showingClassNameList;
		  if (oe.classDescription != null)
			  classDescription = oe.classDescription;
		  else 
			  classDescription = "";
		  classNameList = oe.classNameList;
		  //keywordsTable = oe.keywordsTable;
		  //showClassesClassName = oe.showClassesClassName;
		  
		  if (showingCurrentClassName)
			  internalShowClassName();
		  if (showingCurrentClassName || showingClassNameList)
			  loadClasses();
		  /*
		  if (showingClasses)
			  //loadClasses();
			  internalShowClasses();
		  else
			  internalHideClasses();
			*/  
		  if (showingEntireFolder)
			  internalShowEntireFolder();
		  //exception = oe.getException();
		  //animationEnabled = oe.getAnimationEnabled();
		  //f.close();
		} catch (Exception e) {
			initState();
		}
	}
	
	void initState() {
		className = hideClassesClassName;
		  showingClasses = false;
		  showingEntireFolder = false;
		  showingCurrentClassName = false;
	}
	
	public boolean preRestoreDefaults() {
		return className != hideClassesClassName ||
		  showingClasses != false ||
		  showingEntireFolder != false ||
		  showingCurrentClassName != false;
	}
	@Visible(false)
	public void restoreDefaults() {
		initState();
	}
	
	public static String toClassName(String fileName) {
		String retVal = null;
		if (fileName.endsWith(".class") || fileName.endsWith(".CLA") )
			return withoutExtension(toShortName(fileName));
		return null;
	}
	public static String withoutExtension(String fileName) {
		String retVal = null;
		int i = fileName.lastIndexOf('.');
		return  fileName.substring(0, i);
	}
	public static String getDirectoryName(String fileName) {
		int index = fileName.lastIndexOf(File.separatorChar);
		if (index < 0)
			return (".");
		String dirName = fileName.substring(0, index+1);
		//System.out.println("returning dir "+ dirName);
		return dirName;
	}
	public static String toCompleteName(String origFileName) {
		int i = origFileName.lastIndexOf('~');
		if (i < 0 || i >= origFileName.length()) return origFileName;
		String directoryName = getDirectoryName(origFileName);
		//System.out.println(getDirectoryName(origFileName));
		int omittedLength = Integer.parseInt("" + origFileName.charAt(i+1)); 
		String fileName = toShortName(origFileName.toLowerCase());
		i = fileName.lastIndexOf('~');
		System.out.println("omitted length" + omittedLength);
		String prefix = fileName.substring(0,i);		
		System.out.println("prefix" + prefix);
		i = fileName.lastIndexOf(".");
		String suffix = "";
		if (i >= 0 && i < fileName.length())
		  suffix = fileName.substring(i, fileName.length());				
		System.out.println("suffix" + suffix);
		//File dir = new File(".");
		File dir = new File(directoryName);
		String[] files = dir.list();
		String elementName;
		for (int k = 0; k < files.length; k++) {			
			elementName = files[k].toLowerCase();
			//System.out.println("element name" + elementName);
			if (elementName.startsWith(prefix)) {
				System.out.println("found prfeix***" + elementName);
				int j = elementName.indexOf(suffix.toLowerCase());
				if (j < 0 || j > elementName.length())
					continue;
				else {
					System.out.println("matched" + elementName);
					String elementPrefix = withoutExtension(elementName);
					System.out.println("Prfeix" + elementPrefix.length() + "prefix" + prefix.length() + "omitted" + omittedLength);
					if ((omittedLength == 1) || (elementPrefix.length() == prefix.length() + omittedLength))
						return directoryName + files[k];
				}
			}
		}
		return null;
	}
	static public String toShortName(String s) {
	  int i = s.lastIndexOf(File.separatorChar);
	  if (i < s.length() && i >= 0) {
	      return s.substring(i + 1, s.length());
		  
	  } else return s;
  }
	static ClassLoader myClassLoader;
	 java.awt.Dimension mySize;
	 transient bus.uigen.uiFrame myEditor;
	 void setFrame(uiFrame thisEditor) {
		 myEditor = thisEditor;
		 //myEditor.addComponentListener(new ComponentAdapter() {
		 myEditor.getFrame().addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
    	  if (my_frame_width != myEditor.getSize().getWidth() || my_frame_height != myEditor.getSize().getHeight() ) {
		  my_frame_width = myEditor.getSize().getWidth();
		  my_frame_height = myEditor.getSize().getHeight();
	    saveState();
    	  }
	  }}
    );
	}
	public static ObjectEditor defaultObjectEditor() {
		//System.out.println("default OE at start " + defaultObjectEditor);
		if (defaultObjectEditor == null)
			//defaultObjectEditor = new ObjectEditor();
			new ObjectEditor(); // this has side effect of assigning
		//System.out.println("default OE at send " + defaultObjectEditor);
		return defaultObjectEditor;
	}
	void init() {
		hideClassesClassName = new ADynamicEnum("Enter Class Name");
		className = hideClassesClassName;
		
	}
	public ObjectEditor() {
		init();
		//System.out.println("Creating object editor");
		defaultObjectEditor = this;
		loadState();
		
		/*
		if (myClassLoader == null)
		   //myClassLoader = this.getClass().getClassLoader();
			myClassLoader = ClassLoader.getSystemResource();
		*/
		//readDirectory();
	}

	public ObjectEditor(boolean loadFile) {
		init();
		System.out.println("Creating object editor");		
		if (loadFile)
		    loadState();
		//readDirectory();
	}
	transient PackageObject packageObject;
	transient AnObjectFolder objectFolder;
	//transient AnObject packageObject;
	public AnObjectFolder readEntireFolder() {
		if (objectFolder != null)
			return objectFolder;
		File file = new File(".");
		objectFolder = new AnObjectFolder(file, "", null);
		return objectFolder;
		
	}
	transient DynamicEnum showClassesClassName;
	transient DynamicEnum hideClassesClassName;
//	= new ADynamicEnum("Enter Class Name");
	Vector<String> classNames;
	public void readClasses() {
		classNames = readEntireFolder().getClassNamesInFolderTree();
		//if (classNameList == null)
		
		//classNameList = new ClassDirectoryToAnnotation(classNames, this, "");
		//keywordsTable = new HierarchicalKeywordsToClassTable(classNames, this, "");
		showClassesClassName = new ADynamicEnum(classNames);
		
	}
	
	
	boolean showingClasses = false;
	boolean showingEntireFolder = false;
	transient Object folder;
	public boolean preGetFolder() {		
		return showingEntireFolder;
		//return true;
	}
	public Object getFolder() {
		if (folder == null)
			return "Folder Not Shown";
		return folder;
	}
	
	public boolean preShowClasses() {
		return !showingClasses && showingCurrentClassName;
	}
	public void internalShowClasses() {
		/*
		if (showingClasses) {
			showingClasses = false;
			folder = null;
		} else {
		*/
		if (!showingClasses) {
			showingClasses = true;
			if (showClassesClassName == null)
				loadClasses();
			className = showClassesClassName;
			/*
			if (packageObject == null)
				readClasses();
			folder = packageObject;
			showingClasses = true;
			showingEntireFolder = false;
			*/
		}
		//saveState();
	}
	@Visible(false)
	public void showClasses() {
		
		internalShowClasses();
		//saveState();
	}
	public boolean preHideClasses() {
		return showingClasses && showingCurrentClassName;
		//return true;
	}
	@Visible(false)
	public void hideClasses() {
		internalHideClasses();
		//saveState();
		
	}
	public void internalHideClasses() {
		if (showingClasses) {
			className = hideClassesClassName;
				
			showingClasses = false;
		}
		//saveState();
		
	}
	void loadClasses() {
		if (packageObject == null)
				readClasses();
		//folder = packageObject;
		//showingEntireFolder = false;
	}
	public boolean preShowEntireFolder() {
		return !showingEntireFolder;
	}
	public void internalShowEntireFolder() {
		if (objectFolder == null)
			readEntireFolder();
		folder = objectFolder;
		showingEntireFolder = true;
		//showingClasses = false;
		/*
		ObjectEditor.setVisible(ObjectEditor.class, "folder", true);
		ObjectEditor.refreshAttributes(this);
		*/
		
		//saveState();
	}
	void showEntireFolder() {
		internalShowEntireFolder();
		//saveState();
	}
	public boolean preHideEntireFolder() {
		return showingEntireFolder;
	}
	void hideEntireFolder() {
		showingEntireFolder = false;
		folder = null;
		/*
		ObjectEditor.setVisible(ObjectEditor.class, "folder", false);
		ObjectEditor.refreshAttributes(this);
		*/
		
		//saveState();
	}
	boolean showingClassNameList = false;

	public boolean preHideClassNameList() {
		return showingClassNameList;
	}
	public boolean preShowClassNameList() {
		return !showingClassNameList;
	}
	// not very useful given the j2h
	@Visible(false)	
	@Explanation("Browse through all of the classes with their explanations")
	public void showClassNameList() {		
		showingClassNameList = true;
		//ObjectEditor.treeEdit(getClassNameList());
		ObjectEditor.treeBrowse(getClassNameList(), "explanation of selected classes shown here", 
				(boolean) false);
		saveState();
	}
	@Visible(false)
	public void hideClassNameList() {
		showingClassNameList = false;
		saveState();
	}
	@Visible(false)
	public void indexedClasses() {
		ObjectEditor.treeBrowse(getKeywordsTable(), "class names for selected indexed item shown here", 
				"explanation of selected class name shown here", 
				(boolean) false);
		saveState();
	}
	/*
	public PackageObject getDirectory() {
		return packageObject;
	}
	*/
	/*
	public AnObjectFolder getEntireFolder() {
		return objectFolder;
	}
	*/
	/*
	boolean animationEnabled = false;
	public boolean getAnimationEnabled() {
		return animationEnabled;
	}
	public void setAnimationEnabled(boolean newVal) {
		animationEnabled = newVal;
		saveState();
	}
	*/
	 DynamicEnum<String> className;
	 //= hideClassesClassName;
		 //new ADynamicEnumeration ("Enter Class Name");
	transient Object instance;
	public boolean preGetCurrentClassName() {
		return showingCurrentClassName;
	}
	@util.annotations.Explanation("The full name of the class to be instantiated by the ObjectEditor->New command")
	public DynamicEnum<String> getCurrentClassName() {
		return className;
	}
	transient ClassProxy objectClass;
	public void setCurrentClassName(DynamicEnum<String> newVal) {
		if ((newVal == null) || (newVal.getValue().equals(""))) {
			return;
		}		
		try {
		className = newVal;
		objectClass = uiClassFinder.forName(className.getValue());
		classDescription = AClassDescriptor.getAnnotationString(objectClass);
		if (objectClass.isInterface() || isAbstract(objectClass))
			JOptionPane.showMessageDialog(null,className + " is not instantiatable." );
		
		//newInstance();
		} catch (Exception e) {
			String exception = AClassDescriptor.toShortName(e.toString());
			JOptionPane.showMessageDialog(null,className + " not found" );
			//JOptionPane.showMessageDialog(null, exception );
			
		}
		//className = objectClass.getName();
		saveState();
	}
	
	String classDescription = "";
	
	public String getClassDescription() {
		return classDescription;
	}
	
	
	
	/*
	public Object getInstance() {
		return instance;
	}
	public void setInstance(Object newVal) {	
		instance = newVal;
	}
	*/
	//String exception = "";
	//Exception exception;
	/*
	String getException() {
		return exception;
	}
	*/
	/*
	public void setException(Exception newVal) {	
		exception = newVal;
	}
	*/
	boolean showingCurrentClassName = false;
	public boolean preShowClassName() {
		return !showingCurrentClassName;
	}
	 void internalShowClassName() {
		 /*
		ObjectEditor.setVisible(ObjectEditor.class, "currentClassName", true);
		ObjectEditor.refreshAttributes(this);
		*/
		showingCurrentClassName = true;
	}
	@Explanation("Show the name of the last class used in the main panel")
	public void showClassName() {
		internalShowClassName();
		//saveState();
	}
	public boolean preHideClassName() {
		return showingCurrentClassName;
	}
	@Explanation("Hide the name of the last class used in the main panel")
	public void hideClassName() {
		/*
		ObjectEditor.setVisible(ObjectEditor.class, "currentClassName", false);
		ObjectEditor.refreshAttributes(this);
		*/
		showingCurrentClassName = false;
		//saveState();
	}
	public boolean preNewInstance() {
		return true;
//		return showingCurrentClassName;
	}
	public boolean preNewInstanceString() {
		return true;
//		return !showingCurrentClassName;
	}
	@Explanation("Create a new instance of the last class specified")
	public Object newInstance() {
		if ((className == null) || (className.equals(""))) {
			JOptionPane.showMessageDialog(null, "Current Class Name Field is Empty");
			return null;
		}
		/*
		if (!showingCurrentClassName) {
			showClassName();
			JOptionPane.showMessageDialog(null, "Please enter current class name in the new field created in ObjectEditor window");
			
			return null;
			
		}
		*/
		instance = internalNewInstance(className.getValue());
		return instance;
		/*
		try {
		   //Class objectClass = Class.forName(className);
			objectClass = Class.forName(className);
			
		    uiFrame f;
			Constructor[] constructors = objectClass.getConstructors();
			if ((constructors.length > 0) || constructors[0].getParameterTypes().length > 0) {
				f= uiMethodInvocationManager.instantiateClass(objectClass);
				//f.setAnimationMode(animationEnabled);
			}
			else {
		      instance = objectClass.newInstance();
		       f = edit(instance);
			  //f.setAnimationMode(animationEnabled);
			}
		} catch (Exception e) {
			String exception = ClassDescriptor.toShortName(e.toString());
			JOptionPane.showMessageDialog(null, exception);
			
		}
		*/
	}
	// vestigal info, referenced by BeanInfo, so let us keep it
	@Visible(false)
	public void source() {
		if (objectFolder == null)
			readEntireFolder();
			//return;
		ObjectEditor.showSource(className.getValue());
		//objectFolder.openSource(className.getValue());
		
	}
	
	public void source(ClassProxy c) {
		String className = c.getName();
		if (objectFolder == null)
			readEntireFolder();
			//return;
		objectFolder.openSource(className);
		//ObjectEditor.showSource(className);
		
	}
	public static void showSource(ClassProxy c) {
		showSource(c.getName());
		/*
		File classFile = util.Misc.open(c);
		String text = util.Misc.toText(classFile);
		ObjectEditor.edit(text);
		*/
		
		//defaultOE.source(c);
	}
	public static void showSource(Class c) {
		showSource(c.getName());
		/*
		File classFile = util.Misc.open(c);
		String text = util.Misc.toText(classFile);
		ObjectEditor.edit(text);
		*/
		
		//defaultOE.source(c);
	}
	public static void showSource(String sourceDirectory, ClassProxy c) {
		showSource(sourceDirectory, c.getCanonicalName());
	}
	public static void showSource(String sourceDirectory, String c) {
		try {
		File classFile = util.misc.Common.open(sourceDirectory, c);
		String text = util.misc.Common.toText(classFile);
		uiFrame editor = (uiFrame) ObjectEditor.edit(text);
		editor.setTitle(c);
		} catch (Exception e) {
			Tracer.userMessage(e.getMessage());
			e.printStackTrace();
		}
		
		//defaultOE.source(c);
	}
	
	public static void showSource(File classFile) {
		try {
		//File classFile = util.Misc.open(sourceDirectory, c);
		String text = util.misc.Common.toText(classFile);
		ObjectEditor.edit(text);
		} catch (Exception e) {
			Tracer.userMessage(e.getMessage());
			e.printStackTrace();
		}
		
		//defaultOE.source(c);
	}
	
	
	
	public static void showSourceWithGlobalSourceDirectory(ClassProxy c) {
		if (uiFrameList.getList().size() != 0) {
			uiFrame initialFrame = (uiFrame) uiFrameList.getList().get(0);
			if (initialFrame != null) 
				 showSource(initialFrame.getSourceDirectory(), c);
		} else
			showSource(c);
		
	}
	public static void showSource(String className) {
		File classFile = util.misc.Common.open(className);
		String text = util.misc.Common.toText(classFile);
		ObjectEditor.edit(text);
		/*
		try {
			//Frame editor = uiGenerator.generateUIFrame(ClassDescriptorCache.getClassDescriptor(Class.forName(name)), (myLockManager) null);
			//uiFrame editor = uiGenerator.generateUIFrame(ClassDescriptorCache.getClassDescriptor(uiClassFinder.forName(className)), (myLockManager) null);
			//editor.setVisible(true);
			Class c = uiClassFinder.forName(className);
			showSource(c);
		}   catch (Exception ex) {}
		*/
	}
	@Explanation("Create a new instance of the class entered as a parameter of this command")
	public Object newInstance(String theClassName) {
		getCurrentClassName().setValue(theClassName);
		//setCurrentClassName( theClassName);
		return newInstance();
	}
	//ClassNameTree classNameList;
	//ClassNameTableTree classNameList;
	ClassDirectoryToAnnotation classNameList;
	
	public boolean preGetClassNameList() {
		return showingClassNameList;
	}
	//public ClassNameTree getClassNameList() {
	//public ClassNameTableTree getClassNameList() {
	public ClassDirectoryToAnnotation getClassNameList() {
		if (classNameList == null) {
			if (classNames == null) {
				readClasses();
			}
			classNameList = new ClassDirectoryToAnnotation(classNames, "");
		}
		//keywordsTable = new HierarchicalKeywordsToClassTable(classNames, this, "");
			//loadClasses();
		return classNameList;
		
	}
	HierarchicalKeywordsToClassTable keywordsTable;
	static Hashtable<String, ClassNameTable> keyWordsToClassNames = new Hashtable();
	public static ClassNameTable getClassNameTable (String key) {
		ClassNameTable classNames = ObjectEditor.keyWordsToClassNames.get(key);
		if (classNames ==null) { 
			classNames = new ClassNameTable();
			ObjectEditor.keyWordsToClassNames.put(key, classNames);
		}
		return classNames;
	}
	/*
public static void associateKeywordWithClassName(String keyword, ClassDescriptor cd) {
		
		if (keyword == null || keyword.equals("")) 
			return;
		if (uiFrame.isPredefinedClass(cd))
			return;
		//String className = c.getName();
		ClassNameTable classNames = getClassNameTable(keyword);
		String description = ClassDescriptor.getAnnotationString(c);
		classNames.put(c.getName(), description);
		
		
	}
	*/
	public HierarchicalKeywordsToClassTable getKeywordsTable() {
		if (keywordsTable == null) {
			if (classNames == null) {
				readClasses();
			}
			
			keywordsTable = new HierarchicalKeywordsToClassTable(classNames, "");
		}
		return keywordsTable;
	}
	public static void associateKeywordWithClassName(String keyword, ClassProxy c) {
		
		if (keyword == null || keyword.equals("")) 
			return;
		if (AClassDescriptor.isPredefinedClass(c))
			return;
		//String className = c.getName();
		ClassNameTable classNames = getClassNameTable(keyword);
		String description = AClassDescriptor.getAnnotationString(c);
		classNames.put(c.getName(), description);
		
		
	}
	///
	public static void bind (Object targetObject, Method targetMethod) {
		//uiFrame oeFrame = ObjectEditor.createOEFrame();
		MethodProxy virtualMethod = AVirtualMethod.virtualMethod(targetMethod);
		bind(targetObject, virtualMethod);
		
	}
	public static void bind (Object targetObject, MethodProxy targetMethod) {
		uiFrame frame = ObjectEditor.createOEFrame();
		ObjectEditor.setMethodAttribute(targetObject, targetMethod, AttributeNames.INPLACE_METHOD_RESULT, true);
		bind(frame, targetObject, targetMethod, (VirtualButton) null, null, null );
		
	}
	public static void bind (uiFrame frame, Object targetObject, Method targetMethod, JMenuItem theInvokeButton, Component[] theParameterComponents , Component resultComponent) {
		VirtualMenuItem virtualButton = bus.uigen.widgets.swing.SwingMenuItem.virtualMenuItem(theInvokeButton);
		VirtualComponent[] virtualParameterComponents = null;
		VirtualComponent virtualResultComponent = null;
		if (theParameterComponents != null) {
		virtualParameterComponents = new VirtualComponent[theParameterComponents.length];
		for (int i = 0; i < virtualParameterComponents.length; i++)
			virtualParameterComponents[i] = AWTComponent.virtualComponent (theParameterComponents[i]);
		}
		if (resultComponent != null)
			virtualResultComponent = AWTComponent.virtualComponent (resultComponent);
		bind(frame, targetObject, AVirtualMethod.virtualMethod(targetMethod), virtualButton, virtualParameterComponents, virtualResultComponent );
		
	}
	public static void bind (uiFrame frame, Object targetObject, MethodProxy targetMethod, VirtualMenuItem theInvokeButton, VirtualComponent[] theParameterComponents, VirtualComponent resultComponent ) {
		if (targetObject == null)
			return;
		ClassProxy targetClass =  ReflectUtil.toMaybeProxyTargetClass(targetObject);
		//MethodProxy targetMethod = uiBean.getParameterLessMethod(targetClass, targetMethodName);
		Hashtable<Object, MethodProxy>   objectMethods = new Hashtable();
		//uiMethodInvocationManager invocationManager = new uiMethodInvocationManager();
		objectMethods.put(targetObject, targetMethod);
		new MethodInvocationManager(frame, null,   null, objectMethods, theInvokeButton, theParameterComponents, resultComponent);
	}
	///
	/*
	public static void bind (Object targetObject, Method targetMethod) {
		//uiFrame oeFrame = ObjectEditor.createOEFrame();
		MethodProxy virtualMethod = AVirtualMethod.virtualMethod(targetMethod);
		bind(targetObject, virtualMethod);
		
	}
	public static void bind (Object targetObject, MethodProxy targetMethod) {
		uiFrame frame = ObjectEditor.createOEFrame();
		ObjectEditor.setMethodAttribute(targetObject, targetMethod, AttributeNames.INPLACE_METHOD_RESULT, true);
		bind(frame, targetObject, targetMethod, (VirtualButton) null, null, null );
		
	}
	*/
	public static void bind (uiFrame frame, Object targetObject, Method targetMethod,  Component[] theParameterComponents , Component resultComponent) {
		bind( frame,  targetObject,  targetMethod, theParameterComponents ,  resultComponent);
	}
	public static void bind (uiFrame frame, Object targetObject, Method targetMethod, JButton theInvokeButton, Component[] theParameterComponents , Component resultComponent) {
		VirtualButton virtualButton = bus.uigen.widgets.swing.SwingButton.virtualButton(theInvokeButton);
		VirtualComponent[] virtualParameterComponents = null;
		VirtualComponent virtualResultComponent = null;
		if (theParameterComponents != null) {
		virtualParameterComponents = new VirtualComponent[theParameterComponents.length];
		for (int i = 0; i < virtualParameterComponents.length; i++)
			virtualParameterComponents[i] = AWTComponent.virtualComponent (theParameterComponents[i]);
		}
		if (resultComponent != null)
			virtualResultComponent = AWTComponent.virtualComponent (resultComponent);
		bind(frame, targetObject, AVirtualMethod.virtualMethod(targetMethod), virtualButton, virtualParameterComponents, virtualResultComponent );
		
	}
	public static void bind (uiFrame frame, Object targetObject, MethodProxy targetMethod, VirtualButton theInvokeButton, VirtualComponent[] theParameterComponents, 
			VirtualComponent resultComponent ) {
		if (targetObject == null)
			return;
		ClassProxy targetClass = ACompositeLoggable.getTargetClass(targetObject);
		//MethodProxy targetMethod = uiBean.getParameterLessMethod(targetClass, targetMethodName);
		Hashtable<Object, MethodProxy>   objectMethods = new Hashtable();
		//uiMethodInvocationManager invocationManager = new uiMethodInvocationManager();
		objectMethods.put(targetObject, targetMethod);
		new MethodInvocationManager(frame, null,   null, objectMethods, theInvokeButton, theParameterComponents, resultComponent);
	}
	public static ButtonCommand bind (uiFrame frame, Object targetObject, String targetMethodName, VirtualButton button ) {
		if (targetObject == null)
			return null;
		if (button == null)
			return null;
		//ClassProxy targetClass = RemoteSelector.getClass(targetObject);
		ClassProxy targetClass = ACompositeLoggable.getTargetClass(targetObject);
		MethodProxy targetMethod = IntrospectUtility.getParameterLessMethod(targetClass, targetMethodName);
		ButtonCommand command = ButtonCommand.createCommandFromButton(frame, targetObject, targetClass, targetMethod, button, null);
		//frame.addCustomOrAutomaticButton(command);
		return command;
	}
	public static ButtonCommand bind (uiFrame frame, Object targetObject, MethodProxy targetMethod, VirtualButton button ) {
		if (targetObject == null)
			return null;
		if (button == null)
			return null;
		ClassProxy targetClass = ACompositeLoggable.getTargetClass(targetObject);
		//MethodProxy targetMethod = uiBean.getParameterLessMethod(targetClass, targetMethodName);
		ButtonCommand command = ButtonCommand.createCommandFromButton(frame, targetObject, targetClass, targetMethod, button, null);
		//frame.addCustomOrAutomaticButton(command);
		return command;
	}
	
	public static VirtualMethodMenuItem bind (uiFrame frame, Object targetObject, String targetMethodName, VirtualMenuItem menuItem ) {
		if (targetObject == null)
			return null;
		if (menuItem == null)
			return null;
		ClassProxy targetClass = ACompositeLoggable.getTargetClass(targetObject);
		MethodProxy targetMethod = IntrospectUtility.getParameterLessMethod(targetClass, targetMethodName);
		VirtualMethodMenuItem vMenuItem = new VirtualMethodMenuItem(frame, targetObject,  targetMethod, menuItem);
		frame.addCustomMenuItem(vMenuItem);
		return vMenuItem;
	}
	public static VirtualMethodMenuItem bind (uiFrame frame, Object targetObject, MethodProxy targetMethod, VirtualMenuItem menuItem ) {
		if (targetObject == null)
			return null;
		if (menuItem == null)
			return null;
		ClassProxy targetClass = ACompositeLoggable.getTargetClass(targetObject);
		//MethodProxy targetMethod = uiBean.getParameterLessMethod(targetClass, targetMethodName);
		VirtualMethodMenuItem vMenuItem = new VirtualMethodMenuItem(frame, targetObject,  targetMethod, menuItem);
		frame.addCustomMenuItem(vMenuItem);
		return vMenuItem;
	}
	public static ButtonCommand bind (Object targetObject, String targetMethodName, VirtualButton button ) {
		return bind (nullFrame(), targetObject, targetMethodName, button );
	}
	public static VirtualMethodMenuItem bind ( Object targetObject, String targetMethodName, VirtualMenuItem menuItem ) {
		return bind (nullFrame(), targetObject, targetMethodName, menuItem);
	}
	public static boolean isAbstract(ClassProxy c) {
		return Modifier.isAbstract (c.getModifiers());
	}
	public static Object internalNewInstance(String className) {
		
		Object instance = null;
		try {
		   //Class objectClass = Class.forName(className);
			ClassProxy objectClass = uiClassFinder.forName(className);
			//String longName = uiClassFinder.toLongName(className);
			//Class objectClass = myClassLoader.loadClass(longName);
			if ( objectClass == null ) {
				JOptionPane.showMessageDialog(null, "Cannot find class: " + className);
				return null;
			}
			if ( objectClass.isInterface()) {
				JOptionPane.showMessageDialog(null, "Cannot instantiate an interface: " + className);
				return null;
			}
			
		    uiFrame f;
			MethodProxy[] constructors = objectClass.getConstructors();	
			if ((constructors.length > 0) || (constructors.length > 0 && constructors[0].getParameterTypes().length > 0)) {
				f= MethodInvocationManager.instantiateClass(objectClass);
				//f.setAnimationMode(animationEnabled);
			}
			else {
		      instance = objectClass.newInstance();
		       f = (uiFrame) edit(instance);
			  //f.setAnimationMode(animationEnabled);
			}
		} catch (IllegalAccessException iae) {
			String exception = iae.getClass().getSimpleName() + ". Cannot instantiate the non-public class: "  + className;
			JOptionPane.showMessageDialog(null, exception);
			iae.printStackTrace();
		} catch (Exception e) {
			String exception = AClassDescriptor.toShortName(e.toString());
			JOptionPane.showMessageDialog(null, exception);
			e.printStackTrace();
			
		}
		return instance;
	}
	
	public static Object internalNewInstance(String className, boolean showMenus) {
		
		Object instance = null;
		try {
		   //Class objectClass = Class.forName(className);
			ClassProxy objectClass = uiClassFinder.forName(className);
			//String longName = uiClassFinder.toLongName(className);
			//Class objectClass = myClassLoader.loadClass(longName);
			if (objectClass.isInterface())
				JOptionPane.showMessageDialog(null, "Cannot instantiate an interface: " + className);
			
		    uiFrame f;
			MethodProxy[] constructors = objectClass.getConstructors();	
			if ((constructors.length > 0) || (constructors.length > 0 && constructors[0].getParameterTypes().length > 0)) {
				f= MethodInvocationManager.instantiateClass(objectClass);
				
				//f.setAnimationMode(animationEnabled);
			}
			else {
		      instance = objectClass.newInstance();
		       f = edit(instance, showMenus,  (Hashtable) null, null);
			   
			  //f.setAnimationMode(animationEnabled);
			}
		} catch (Exception e) {
			String exception = AClassDescriptor.toShortName(e.toString());
			JOptionPane.showMessageDialog(null, exception);
			e.printStackTrace();
			
		}
		return instance;
	}
		
	static  ComponentDrawer shapeDrawer;
	static  OEFrame shapeDrawerFrame;
	public static OEFrame getShapeDrawerFrame() {
		createShapeDrawer();
		return shapeDrawerFrame;
		
	}
	public static void setShapeDrawerFrameSize(int newWidth, int newHeight) {
		getShapeDrawerFrame().setSize(newWidth, newHeight);
		
	}
	public static void setShapeDrawerBackground(Color newColor) {
		getShapeDrawerFrame().getDrawComponent().setBackground(newColor);		
	}
	
	public static void openKeyStream() {
		createShapeDrawer();
		componentInputStreamsManager.openKeyStream();
	}
	public static void closeKeyStream()  {
		createShapeDrawer();
		componentInputStreamsManager.closeKeyStream();
	}
	
	public static void openKeyEventStream() {
		createShapeDrawer();
		componentInputStreamsManager.openKeyEventStream();
	}
	public static void closeKeyEventStream() {
		createShapeDrawer();
		componentInputStreamsManager.closeKeyEventStream();
	}
	
	public static void openMouseEventStream() {
		createShapeDrawer();
		componentInputStreamsManager.openMouseClickEventStream();
	}
	public static void closeMouseEventStream() {
		createShapeDrawer();
		componentInputStreamsManager.closeMouseClickEventStream();
	}
	
	public static char getChar() {
		createShapeDrawer();
		return componentInputStreamsManager.getChar();
	}
	public static AWTEvent getAWTEvent() {
		createShapeDrawer();
		return componentInputStreamsManager.getAWTEvent();
	}
	public static KeyEvent getKeyEvent() {
		createShapeDrawer();
		return componentInputStreamsManager.getKeyEvent();
	}
	public static MouseEvent getMouseClickedEvent() {
		createShapeDrawer();
		return componentInputStreamsManager.getMouseClickedEvent();
	}
	public static MouseEvent getMouseDraggedEvent() {
		createShapeDrawer();
		return componentInputStreamsManager.getMouseDraggedEvent();
	}
	
	static ComponentInputter componentInputStreamsManager;
	
	
	static void createShapeDrawer() {
		if (shapeDrawer != null) return;
		shapeDrawer = new AComponentDrawer(null);
//		OEShape dummy = new APointModel(5, 5);
//		shapeDrawer.add(dummy);
//		OEFrame frame = ObjectEditor.edit(shapeDrawer);
		OEFrame frame = ObjectEditor.graphicsEdit(shapeDrawer);

		shapeDrawerFrame = frame;
//		shapeDrawer.remove(0);
//		frame.hideMainPanel();
//		frame.showDrawPanel();
//		frame.hideMainPanel();
//		ObjectEditor.setShapeDrawerFrameSize(320, 240);
//		setShapeDrawerBackground (new Color(86, 160, 211));
		componentInputStreamsManager = new AComponentInputter(frame);
//		frame.showDrawPanel();
//		if (uiFrameList.getList().size() == 0) {
//			uiFrame frame = ObjectEditor.edit(new ObjectEditor());
////			frame.hideMainPanel();
////			frame.showDrawPanel();
//			
//		}
//		uiFrame frame = (uiFrame) (uiFrameList.getList().lastElement());
//		if (frame == null) {
//			ObjectEditor.edit(new ObjectEditor());			
//			frame = (uiFrame) ( uiFrameList.getList().lastElement());
//		}
//		try {
//			frame.showDrawPanel();
//			SLModel slModel = frame.getDrawing();
//			shapeDrawer = new ShapeDrawer(slModel);
//		} catch (Exception e) {
//			System.out.println("Error finding frame to draw on");
//			e.printStackTrace();
//		}
	}
	
	  public static void ignoreOperationsOfClass (Class aClass) {
		  ClassIntrospectionFilterer.ignoreOperationsOfClass(aClass);
		}
	  public static void ignoreComponentsOfClass (Class aClass) {
		  	ClassIntrospectionFilterer.ignoreComponentsOfClass(aClass);
		}
	public static ComponentDrawer createNewShapeDrawer() {
		return new AComponentDrawer(null);
		
	}
	
	
	public OEFrame displayShapeDrawer(ComponentDrawer shapeDrawer) {
		return ObjectEditor.edit(shapeDrawer);
	}
	
	public static void removeShape(FlexibleShape aShape) {
		createShapeDrawer();
		shapeDrawer.remove(aShape);
	}
	public static FlexibleShape drawRectangle(int x, int y, int width, int height){
		createShapeDrawer();
		return shapeDrawer.drawRectangle(x, y, width, height);
	}
	public static FlexibleShape fillRectangle(int x, int y, int width, int height){
		createShapeDrawer();
		return shapeDrawer.fillRectangle(x, y, width, height);
	}
	public static FlexibleShape drawOval(int x, int y, int width, int height){
		createShapeDrawer();
		return shapeDrawer.drawOval(x, y, width, height);
	}
	public static FlexibleShape fillOval(int x, int y, int width, int height){
		createShapeDrawer();
		return shapeDrawer.fillOval(x, y, width, height);
	}
	public static FlexibleShape drawLine(int x, int y, int width, int height){
		createShapeDrawer();
		return shapeDrawer.drawLine(x, y, width, height);
	}
	public static FlexibleTextShape drawString(String text, int x, int y){
		createShapeDrawer();
		return shapeDrawer.drawString(text, x, y);
	}
	public static FlexibleShape drawImage(String fileName, int x, int y){
		createShapeDrawer();
		return shapeDrawer.drawImage(fileName, x, y);
	}
	public static FlexibleShape drawPoint(int x, int y){
		createShapeDrawer();
		return shapeDrawer.drawPoint(x, y);
	}
	public static void setDefaultValue(Class cl, Object newVal) {
		if (!cl.isInstance(newVal))
			Tracer.error("setDefaultValue: " + newVal + " is not an instance of " + cl);
		util.misc.Common.setDefaultValue(cl, newVal);
	}
	public static void clearDrawing() {
		createShapeDrawer();
		shapeDrawer.clearDrawing();
	}
	public static void setDefaultAttribute (String name, Object value) {
	  	AttributeNames.setDefault(name, value);
	  }
	public static Object getDefaultAttribute (String name) {
	  	return AttributeNames.getDefault(name);
	  }
	public static void setVisible (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.VISIBLE, newVal);		
	}
	public static void setProjectionGroups (Class c, Map newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.PROJECTION_GROUPS, newVal);		
	}
	public static void setPreferredWidget (Class c, String widget) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.PREFERRED_WIDGET, widget);		
	}
	public static void setHashtableChildren (Class c, String value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.HASHTABLE_CHILDREN, value);		
	}
	public static void setLabelKeys (Class c, boolean value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.LABEL_KEYS, value);		
	}
	public static void setLabelValues (Class c, boolean value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.LABEL_VALUES, value);		
	}
	public static void tabEdit (Class c) {
		 //ObjectEditor.setPreferredWidget(c, "javax.swing.JTabbedPane");
		ObjectEditor.setPreferredWidget(c, javax.swing.JTabbedPane.class);
	}	
	
	public static void desktopEdit (Class c) {
		 //ObjectEditor.setPreferredWidget(c, "javax.swing.JTabbedPane");
		ObjectEditor.setPreferredWidget(c, javax.swing.JDesktopPane.class);
	}
	public static void setToStringAsLabel (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.TO_STRING_AS_LABEL, new Boolean (newVal));		
	}
	public static void setUnparseAsToString (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.DISPLAY_TO_STRING, new Boolean (newVal));		
	}
	public static void setPreferredWidget (Class c, Class widgetClass) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.PREFERRED_WIDGET, widgetClass.getName());		
	}
	public static void setPreferredWidgetAdapter (Class c, Class widgetAdapterClass) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.PREFERRED_WIDGET_ADAPTER, widgetAdapterClass.getName());		
	}
	public static void setLayoutManagerFactory (Class c, LayoutManagerFactory newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.LAYOUT_MANAGER_FACTORY, newVal);		
	}
	public static void setContainerFactory (Class c, ContainerFactory newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.CONTAINER_FACTORY, newVal);		
	}
	
	public static void setIncremental (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.INCREMENTAL, new Boolean (newVal));		
	}
	public static void setLabelled (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.LABELLED, new Boolean (newVal));		
	}
	public static void setLabel (Class c, String newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.LABEL, newVal);		
	}
	public static void setHorizontal (Class c) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.DIRECTION, AttributeNames.HORIZONTAL);		
	}
	public static void setVertical (Class c) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.DIRECTION, AttributeNames.VERTICAL);		
	}
	public static void setIndented (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.INDENTED, newVal);
	}
	public static void setShowBlankColumn(Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.SHOW_BLANK_COLUMN, newVal);
	}
	public static void setShowTree(Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.SHOW_TREE, newVal);
	}
	public static void setHorizontalKeyValue (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.HORIZONTAL_KEY_VALUE, newVal);
	}
	public static void setStretchRows (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.STRETCH_ROWS, new Boolean (newVal));		
	}
	public static void setStretchColumns (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.STRETCH_COLUMNS, new Boolean (newVal));		
	}
	public static void setMainViewGroupStretchRows (Class c, boolean newVal) {
		setMainViewGroupAttribute(c, AttributeNames.STRETCH_ROWS,new Boolean (newVal) );
	}
	public static void setMainViewGroupStretchColumns (Class c, boolean newVal) {
		setMainViewGroupAttribute(c, AttributeNames.STRETCH_COLUMNS,new Boolean (newVal) );
	}
	public static void setViewGroupStretchRows (Class c, String group, boolean newVal) {
		setViewGroupAttribute(c, group, AttributeNames.STRETCH_ROWS,new Boolean (newVal) );
	}
	public static void setViewGroupStretchColumns (Class c, String group, boolean newVal) {
		setViewGroupAttribute(c, group, AttributeNames.STRETCH_COLUMNS,new Boolean (newVal) );
	}
	public static void setMainViewGroupLayoutManagerFactory (Class c, LayoutManagerFactory newVal) {
		
		setMainViewGroupAttribute(c, AttributeNames.LAYOUT_MANAGER_FACTORY, newVal);
	}
	public static void setRowsFullSize (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.ROWS_FULL_SIZE, new Boolean (newVal));		
	}
	public static void setRowsPlacement (Class c, Object newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.ROWS_PLACEMENT, newVal);		
	}
	public static void setColumnsPlacement (Class c, Object newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.COLUMNS_PLACEMENT, newVal);		
	}
	public static void setBoundPlacement (Class c, Object newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.BOUND_PLACEMENT, newVal);		
	}
	public static void setUnboundButtonsPlacement (Class c, Object newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.UNBOUND_BUTTONS_PLACEMENT, newVal);		
	}
	public static void setUnboundPropertiesPlacement (Class c, Object newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (AttributeNames.UNBOUND_PROPERTIES_PLACEMENT, newVal);		
	}
	
	public static void setAttribute (Class c, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (attribute, value);	
	}
	public static void setAttribute (ClassProxy c, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttribute (attribute, value);	
	}
	public static void setVirtualClassAttribute (String c, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getVirtualClassDescriptor(c, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		cd.setAttribute (attribute, value);	
	}
	public static void setViewGroupAttribute (Class c, String group, String attribute, Object value) {
		String virtualClassName = c.getName() + GenerateViewObject.NESTING_DELIMITER + 
		GenerateViewObject.VIEW_NAME + GenerateViewObject.NESTING_DELIMITER + group;
		//Class cl = APropertyAndCommandFilter.class;
		ClassDescriptorInterface  cd = ClassDescriptorCache.getVirtualClassDescriptor(virtualClassName, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		cd.setAttribute (attribute, value);	
	}
	public static void setViewGroupPropertyAttribute (Class c, String group, String property, String attribute, Object value) {
		String virtualClassName = c.getName() + GenerateViewObject.NESTING_DELIMITER + 
		GenerateViewObject.VIEW_NAME + GenerateViewObject.NESTING_DELIMITER + group;
		//Class cl = APropertyAndCommandFilter.class;
		ClassDescriptorInterface  cd = ClassDescriptorCache.getVirtualClassDescriptor(virtualClassName, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		cd.setPropertyAttribute (property, attribute, value);	
	}
	public static void setViewGroupPropertyRow (Class c, String group, String property,  int value) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.ROW, new Integer(value));
	}
	public static void setViewGroupPropertyColumn (Class c, String group, String property,  int value) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.COLUMN, new Integer(value));
	}
	public static void setViewGroupPropertyRowColumn (Class c, String group, String property,  int row, int col) {
		setViewGroupPropertyRow (c, group, property, new Integer(row));
		setViewGroupPropertyColumn (c, group, property, new Integer(col));
	}
	public static void setViewGroupPropertyTextFieldLength (Class c, String group, String property,  int value) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.TEXT_FIELD_LENGTH, new Integer(value));
	}
	public static void setViewGroupMethodAttribute (Class c, String group, String method, String attribute, Object value) {
		String virtualClassName = c.getName() + GenerateViewObject.NESTING_DELIMITER + 
		GenerateViewObject.VIEW_NAME + GenerateViewObject.NESTING_DELIMITER + group;
		//Class cl = APropertyAndCommandFilter.class;
		ClassDescriptorInterface  cd = ClassDescriptorCache.getVirtualClassDescriptor(virtualClassName, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		cd.setMethodAttribute (method, attribute, value);	
	}
	public static void setViewGroupMethodRow (Class c, String group, String method,  int value) {
		setViewGroupMethodAttribute (c, group, method, AttributeNames.ROW, new Integer(value));
	}
	
	public static void setViewGroupMethodColumn (Class c, String group, String method,  int value) {
		setViewGroupMethodAttribute (c, group, method, AttributeNames.COLUMN, new Integer(value));
	}
	public static void setViewGroupMethodRowColumn (Class c, String group, String method,  int row, int col) {
		setViewGroupMethodRow (c, group, method, new Integer(row));
		setViewGroupMethodColumn (c, group, method, new Integer(col));
	}
	
	public static void setViewGroupMethodTextFieldLength (Class c, String group, String method,  int value) {
		setViewGroupMethodAttribute (c, group, method, AttributeNames.TEXT_FIELD_LENGTH, new Integer(value));
	}
	public static void setMainViewGroupMethodAttribute (Class c,  String method, String attribute, Object value) {
		String virtualClassName = c.getName() + GenerateViewObject.NESTING_DELIMITER + 
		GenerateViewObject.VIEW_NAME ;
		//Class cl = APropertyAndCommandFilter.class;
		ClassDescriptorInterface  cd = ClassDescriptorCache.getVirtualClassDescriptor(virtualClassName, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		cd.setMethodAttribute (method, attribute, value);	
	}
	
	public static void setMainViewGroupMethodRow (Class c,  String method,  int value) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.ROW, new Integer(value));
	}
	public static void setMainViewGroupMethodColumn (Class c,  String method,  int value) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.COLUMN, new Integer(value));
	}
	public static void setMainViewGroupMethodRowColumn (Class c,  String method,  int row, int col) {
		setMainViewGroupMethodRow (c, method,  new Integer(row));
		setMainViewGroupMethodColumn (c, method,  new Integer(col));
	}
	public static void setMainViewGroupPropertyAttribute (Class c,  String property, String attribute, Object value) {
		String virtualClassName = c.getName() + GenerateViewObject.NESTING_DELIMITER + 
		GenerateViewObject.VIEW_NAME ;
		//Class cl = APropertyAndCommandFilter.class;
		ClassDescriptorInterface  cd = ClassDescriptorCache.getVirtualClassDescriptor(virtualClassName, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		cd.setPropertyAttribute (property, attribute, value);	
	}
	public static void setMainViewGroupPropertyRow (Class c,  String property,  int value) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.ROW, new Integer(value));
	}
	public static void setMainViewGroupPropertyColumn (Class c,  String property,  int value) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.COLUMN, new Integer(value));
	}
	public static void setMainViewGroupPropertyRowColumn (Class c,  String property,  int row, int col) {
		setMainViewGroupPropertyRow (c, property,  new Integer(row));
		setMainViewGroupPropertyColumn (c, property,  new Integer(col));
	}
	
	public static void tabEditViewGroup (Class c, String group) {
		setViewGroupAttribute(c, group, AttributeNames.PREFERRED_WIDGET, "javax.swing.JTabbedPane");
	}
	public static void setMainViewGroupAttribute (Class c, String attribute, Object value) {
		String virtualClassName = c.getName() + GenerateViewObject.NESTING_DELIMITER + 
		GenerateViewObject.VIEW_NAME;
		//Class cl = APropertyAndCommandFilter.class;
		ClassDescriptorInterface  cd = ClassDescriptorCache.getVirtualClassDescriptor(virtualClassName, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		cd.setAttribute (attribute, value);	
	}
	public static void showButtonsInViewGroup (Class c, String group, boolean newVal) {
		setViewGroupAttribute (c, group, AttributeNames.SHOW_BUTTON, new Boolean(newVal));
	}
	public static void showUnboundButtonsInViewGroup (Class c, String group, boolean newVal) {
		setViewGroupAttribute (c, group, AttributeNames.SHOW_UNBOUND_BUTTONS, new Boolean(newVal));
	}
	public static void showAllButtonsInViewGroup (Class c, String group, boolean newVal) {
		showButtonsInViewGroup(c, group, newVal);
		showUnboundButtonsInViewGroup(c, group, newVal);
	}
	public static void showButtons (Class c, boolean newVal) {
		setAttribute (c, AttributeNames.SHOW_BUTTON, new Boolean(newVal));
	}
	public static void showUnboundButtons (Class c, boolean newVal) {
		setAttribute (c, AttributeNames.SHOW_UNBOUND_BUTTONS, new Boolean(newVal));
	}
	public static void showAllButtons (Class c, boolean newVal) {
		showButtons(c, newVal);
		showUnboundButtons(c, newVal);
		
	}
	public static void setAttribute (Object o, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(RemoteSelector.getClass(o), o);
		cd.setAttribute (attribute, value);	
	}
	public static void setPropertyAttribute(Class c, String property, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, attribute, value);		
	}
	
	public static void setPropertyAttribute(ClassProxy c, String property, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, attribute, value);		
	}
	public static void setAttributeOfAllProperties(Class c, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttributeOfAllProperties (attribute, value);		
	}
	public static void setPropertyViewGroup (Class c, String property, String group) {
		setPropertyAttribute(c, property, AttributeNames.VIEW_GROUP, group);
	}
	public static void setPropertyAttribute(Object o, String property, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor( ReflectUtil.toMaybeProxyTargetClass(o), o);
		cd.setPropertyAttribute (property, attribute, value);		
	}
	public static void setPropertyComponentHeight(Class c, String property,  Object value) {
		setPropertyAttribute(c, property, AttributeNames.COMPONENT_HEIGHT, value);
	}
	public static void setPropertyComponentWidth(Class c, String property,  Object value) {
		setPropertyAttribute(c, property, AttributeNames.COMPONENT_WIDTH, value);		
	}
	public static void setComponentWidth(Class c,  Object value) {
		setAttribute(c, AttributeNames.COMPONENT_WIDTH, value);	
	}
	public static void setHorizontal(Object o) {
		setAttribute (o, AttributeNames.DIRECTION, AttributeNames.HORIZONTAL);
		/*
		ViewInfo  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.DIRECTION, AttributeNames.HORIZONTAL);	
		*/	
	}
	public static void setAddConstraint (Class c, String property, Object constraint) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.ADD_CONSTRAINT, constraint);		
	}
	/*
	public static void setPosition(Class c, String property, int newVal) {
		ViewInfo  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.POSITION, new Integer (newVal));		
	}
	*/
	public static void setPreferredWidget (Class c, String property, String widget) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.PREFERRED_WIDGET, widget);		
	}
	
	public static void setPreferredWidget (Class c, String property, Class widgetClass) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.PREFERRED_WIDGET, widgetClass.getName());		
	}
	public static void setPreferredWidget (Object o, String property, Class widgetClass) {
		//ViewInfo  cd = ClassDescriptorCache.getClassDescriptor(c);
		setPropertyAttribute (o, property, AttributeNames.PREFERRED_WIDGET, widgetClass.getName());		
	}
	public static void setTextFieldLength (Class c, String property, int newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.TEXT_FIELD_LENGTH, new Integer (newVal));		
	}
	public static void setIncremental (Class c, String property, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.INCREMENTAL, new Boolean (newVal));		
	}
	public static void setGraphicsIncremental (boolean newVal) {
		setIncremental (slm.SLModel.class, newVal);
	}
	public static void setPosition (Class c, String property, int newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.POSITION,  new Integer (newVal));		
	}
	public static void setPropertyLabelled (Class c, String property, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.LABELLED, new Boolean (newVal));		
	}
	public static void setPropertyShowBorder(Class c, String property, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.SHOW_BORDER, new Boolean (newVal));		
	}
	public static void setPropertyVisible (Class c, String property, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.VISIBLE, new Boolean (newVal));		
	}
	public static void setVisible (ClassProxy c, String property, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.VISIBLE, new Boolean (newVal));		
	}
	public static Boolean getPropertyVisible (ClassProxy c, String property) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		return (Boolean) cd.getPropertyAttribute (property, AttributeNames.VISIBLE);		
	}
	public static Boolean getPropertyAttribute (ClassProxy c, String property, String attribute) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		return (Boolean) cd.getPropertyAttribute (property, attribute);		
	}
	public static void setVisibleAllProperties (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttributeOfAllProperties (AttributeNames.VISIBLE, new Boolean (newVal));		
	}
	public static void setLabelledAllProperties (Class c, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setAttributeOfAllProperties (AttributeNames.LABELLED, new Boolean (newVal));		
	}
	public static void setToStringAsLabel (Class c, String property, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.TO_STRING_AS_LABEL, new Boolean (newVal));		
	}
	public static void setUnparseAsToString (Class c, String property, boolean newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.DISPLAY_TO_STRING, new Boolean (newVal));		
	}
	public static void setHorizontal(Class c, String property) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.DIRECTION, AttributeNames.HORIZONTAL);		
	}
	public static void setVertical(Class c, String property) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.DIRECTION, AttributeNames.VERTICAL);		
	}
	public static void setBox(Class c, String property) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.DIRECTION, "box");		
	}
	public static void setLabel(Class c, String property, String val) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.LABEL, val);		
	}
	public static void setIcon(Class c, String property, String val) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.ICON, val);		
	}
	public static void setLabelLeft(Class c, String property, String val) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.LABEL_LEFT, val);		
	}
	public static void setLabelRight(Class c, String property, String val) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.LABEL_RIGHT, val);		
	}
	public static void setLabelAbove(Class c, String property, String val) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.LABEL_ABOVE, val);		
	}
	public static void setLabelBelow(Class c, String property, String val) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.LABEL_BELOW, val);		
	}
	
	
	public static void setRowsFullSize(Class c, String property, String val) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setPropertyAttribute (property, AttributeNames.ROWS_FULL_SIZE, val);		
	}
	public static void setMaxValue (Object o, String property, Object maxVal) {
		setPropertyAttribute(o, property, AttributeNames.MAX_VALUE, maxVal);
	}
	public static void setMinValue (Object o, String property, Object minVal) {
		setPropertyAttribute(o, property, AttributeNames.MIN_VALUE, minVal);	
	}
	public static void setMaxValue (Class c, String property, Object maxVal) {
		setPropertyAttribute(c, property, AttributeNames.MAX_VALUE, maxVal);
	}
	public static void setMinValue (Class c, String property, Object minVal) {
		setPropertyAttribute(c, property, AttributeNames.MIN_VALUE, minVal);	
	}
	public static void setStepValue (Class c, String property, Object stepVal) {
		setPropertyAttribute(c, property, AttributeNames.STEP_VALUE, stepVal);	
	}
	public static void setMethodAttribute(Object o, MethodProxy method, String attribute, Object value) {
		ClassProxy c;
		if ( o != null)
			//c = RemoteSelector.getClass(o);
			c = ACompositeLoggable.getTargetClass(o);
		else
			c = method.getDeclaringClass();
		ClassDescriptorInterface cd;		
		cd = ClassDescriptorCache.getClassDescriptor(c);
		cd.setMethodAttribute(method, attribute, value);
	  }
	public static void setMethodAttribute(Object o, String method, String attribute, Object value) {
		if (o == null)
			return;
		ClassProxy c;
		
			c = ACompositeLoggable.getTargetClass(o);
		ClassDescriptorInterface cd;		
		cd = ClassDescriptorCache.getClassDescriptor(c, o);
		cd.setMethodAttribute(method, attribute, value);
	  }
	
	public static void setMethodAttribute(Class c, String method, String attribute, Object value) {
		if (method.equals("*")) {
			seAttributeOfAllMethods(c, attribute, value);
			return;
		}
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c); 
		cd.setMethodAttribute(method, attribute, value);
	  }
	public static void setMethodsAttribute(Class c, String[] methods, String attribute, Object value) {
		for (int i = 0; i < methods.length; i++) {
			setMethodAttribute(c, methods[i], attribute, value );
		}
	  }
	public static void seAttributeOfAllMethods (Class c, String attribute, Object value) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c); 
		cd.setAttributeOfAllMethods(attribute, value);
	  }
	public static void setMethodInToolbar (Class c, String method, boolean newVal) {
		setMethodAttribute (c, method, AttributeNames.TOOLBAR, newVal);
	}
	public static void setMethodToolbar (Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.PLACE_TOOLBAR, newVal);
	}	
	public static void setMethodUndoable (Class c, String method, boolean newVal) {
		setMethodAttribute (c, method, AttributeNames.IS_UNDOABLE, newVal);
	}
	public static void setMethodViewGroup (Class c, String method, String group) {
		setMethodAttribute(c, method, AttributeNames.VIEW_GROUP, group);
	}
	
	public static void setPropertyRowColumn (Class c, String property, int row, int column) {
		setPropertyAttribute (c, property, AttributeNames.ROW, new Integer (row));
		setPropertyAttribute (c, property, AttributeNames.COLUMN, new Integer (column));
	}
	public static void setMethodRowColumn (Class c, String method, int row, int column) {
		setMethodAttribute (c, method, AttributeNames.ROW, new Integer (row));
		setMethodAttribute (c, method, AttributeNames.COLUMN, new Integer (column));
	}
	public static void setPropertyRow (Class c, String property, int row) {
		setPropertyAttribute (c, property, AttributeNames.ROW, new Integer (row));
	}
	
	public static void setMethodRow (Class c, String method, int row) {
		setMethodAttribute (c, method, AttributeNames.ROW, new Integer (row));
	}
	public static void setColumn (Class c, String property, int column) {
		setPropertyAttribute (c, property, AttributeNames.COLUMN, new Integer (column));
	}
	public static void setMethodColumn (Class c, String method, int column) {
		setMethodAttribute (c, method, AttributeNames.COLUMN, new Integer (column));
	}
	public static void setMethodVisible (Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.VISIBLE, newVal);
	}
	
	public static void setMethodLabel(Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.LABEL, newVal);
	}
	
	public static void setMethodDisplayName(Class c, String name, String newVal) {
		ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(c); 
		MethodDescriptorProxy md = cd.getMethodDescriptor(name);
		md.setDisplayName(newVal);
	}
	public static void setMethodMenuName (Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.METHOD_MENU_NAME, newVal);
	}
	public static void setMethodLabelBelow (Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.LABEL_BELOW, newVal);
	}
	
	public static void setMethodLabelAbove (Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.LABEL_ABOVE, newVal);
	}
	public static void setMethodLabelLeft (Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.LABEL_LEFT, newVal);
	}
	public static void setMethodLabelRight (Class c, String method, String newVal) {
		setMethodAttribute (c, method, AttributeNames.LABEL_RIGHT, newVal);
	}
	public static void setMethodAddConstraint (Class c, String group, String method, Object constraint) {
		setMethodAttribute (c, method, AttributeNames.ADD_CONSTRAINT, constraint);	
	}
	public static void setMethodPosition (Class c, String method,  int newVal) {
		setMethodAttribute (c, method, AttributeNames.POSITION, new Integer (newVal));
	}
	public static void setMethodVisible (Class c, String method,  boolean newVal) {
		setMethodAttribute (c, method, AttributeNames.VISIBLE, newVal);
	}
	public static void setMethodsVisible (Class c,boolean newVal) {
		setAttribute (c, AttributeNames.METHODS_VISIBLE, newVal);
	}
	// main group
	public static void setMainViewGroupMethodLabel (Class c, String method, String newVal) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.LABEL, newVal);
	}
	public static void setMainViewGroupMethodLabelLeft (Class c, String method, String newVal) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.LABEL_LEFT, newVal);
	}
	public static void setMainViewGroupMethodLabelRight (Class c, String method, String newVal) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.LABEL_RIGHT, newVal);
	}
	public static void setMainViewMethodLabelBelow (Class c, String method, String newVal) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.LABEL_BELOW, newVal);
	}
	public static void setMainViewMethodLabelAbove (Class c, String method, String newVal) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.LABEL_ABOVE, newVal);
	}
	public static void setMainViewGroupMethodPosition (Class c, String method, int newVal) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.POSITION, new Integer (newVal));
	}
	public static void setMainViewGroupMethodAddConstraint (Class c, String method, Object constraint) {
		setMainViewGroupMethodAttribute (c, method, AttributeNames.ADD_CONSTRAINT, constraint);	
	}
	
	
	// view group
	
	public static void setViewGroupMethodPosition (Class c, String group, String method,  int newVal) {
		setViewGroupMethodAttribute (c, method, group, AttributeNames.POSITION, new Integer (newVal));
	}
	
	public static void setViewGroupMethodLabel (Class c, String group, String method, String newVal) {
		setViewGroupMethodAttribute (c,  group, method, AttributeNames.LABEL, newVal);
	}
	public static void setViewGroupMethodVisible (Class c, String group, String method, String newVal) {
		setViewGroupMethodAttribute (c,  group, method, AttributeNames.VISIBLE, newVal);
	}
	public static void setViewGroupMethodIcon (Class c, String group, String method, String newVal) {
		setViewGroupMethodAttribute (c,  group, method, AttributeNames.ICON, newVal);
	}
	
	public static void setViewGroupMethodLabelLeft (Class c, String group, String method, String newVal) {
		setViewGroupMethodAttribute (c,  group, method, AttributeNames.LABEL_LEFT, newVal);
	}
	public static void setViewGroupMethodLabelRight (Class c, String group, String method, String newVal) {
		setViewGroupMethodAttribute (c, group,method, AttributeNames.LABEL_RIGHT, newVal);
	}
	public static void setViewGroupMethodLabelBelow (Class c, String group, String method, String newVal) {
		setViewGroupMethodAttribute (c, group, method, AttributeNames.LABEL_BELOW, newVal);
	}
	public static void setViewGroupMethodLabelAbove (Class c, String group, String method, String newVal) {
		setViewGroupMethodAttribute (c, group, method, AttributeNames.LABEL_ABOVE, newVal);
	}
	
	public static void setViewGroupMethodAddConstraint (Class c, String group, String method, Object constraint) {
		setViewGroupMethodAttribute (c, group, method, AttributeNames.ADD_CONSTRAINT, constraint);	
	}
	
	/// properties
//	 main group
	public static void setMainViewGroupPropertyVisible (Class c, String property, String newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.VISIBLE, newVal);
	}
	public static void setMainViewGroupPropertyLabel (Class c, String property, String newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.LABEL, newVal);
	}
	public static void setMainViewGroupPropertyLabelLeft (Class c, String property, String newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.LABEL_LEFT, newVal);
	}
	public static void setMainViewGroupPropertyLabelRight (Class c, String property, String newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.LABEL_RIGHT, newVal);
	}
	public static void setMainViewPropertyLabelBelow (Class c, String property, String newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.LABEL_BELOW, newVal);
	}
	public static void setMainViewPropertyLabelAbove (Class c, String property, String newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.LABEL_ABOVE, newVal);
	}
	public static void setMainViewGroupPropertyPosition (Class c, String property, int newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.POSITION, new Integer (newVal));
	}
	public static void setMainViewGroupPropertyAddConstraint (Class c, String property, Object constraint) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.ADD_CONSTRAINT, constraint);	
	}
	
	// view group
	
	public static void setViewGroupPropertyPosition (Class c, String group, String property,  int newVal) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.POSITION, new Integer (newVal));
	}
	public static void setViewGroupPropertyPreferredWidget (Class c, String group, String property,  Class widgetClass) {
		setMainViewGroupPropertyAttribute (c, property, AttributeNames.PREFERRED_WIDGET, widgetClass.getName());
	}
	public static void setViewGroupPropertyVisible (Class c, String group, String property,  boolean newVal) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.VISIBLE, newVal);
	}
	public static void setViewGroupPropertyLabelled (Class c, String group, String property, boolean newVal) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.LABELLED, newVal);
	}
	public static void setViewGroupPropertyLabel (Class c, String group, String property, String newVal) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.LABEL, newVal);
	}
	
	public static void setViewGroupPropertyLabelLeft (Class c, String group, String property, String newVal) {
		setViewGroupPropertyAttribute (c,  group, property, AttributeNames.LABEL_LEFT, newVal);
	}
	public static void setViewGroupPropertyLabelRight (Class c, String group, String property, String newVal) {
		setViewGroupPropertyAttribute (c, group,property, AttributeNames.LABEL_RIGHT, newVal);
	}
	public static void setViewGroupPropertyLabelBelow (Class c, String group, String property, String newVal) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.LABEL_BELOW, newVal);
	}
	public static void setViewGroupPropertyLabelAbove (Class c, String group, String property, String newVal) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.LABEL_ABOVE, newVal);
	}
	
	public static void setViewGroupPropertyAddConstraint (Class c, String group, String property, Object constraint) {
		setViewGroupPropertyAttribute (c, group, property, AttributeNames.ADD_CONSTRAINT, constraint);	
	}
	public static void refreshAttributes (Object topObject, ClassProxy targetClass) {
		uiGenerator.refreshAttributes(topObject, targetClass);
		
	}
//	public static void refreshAttributes(ClassProxy parentClass) {
//		// Class parentClass = adapter.getViewObject().getClass();
//		if (parentClass == null)
//			return;
//		refreshAttributes(parentClass.getName());
//	}
//	public static void refreshAttributes(Class parentClass) {
//		// Class parentClass = adapter.getViewObject().getClass();
//		if (parentClass == null)
//			return;
//		refreshAttributes(parentClass.getName());
//	}
	public static void refreshAttributes(String parentClassName) {
		// Class parentClass = adapter.getViewObject().getClass();		
		UIAttributeManager environment = AttributeManager.getEnvironment();
		if (environment == null)
			return;
		environment.removeFromAttributeLists(parentClassName);
	}
	public static void refreshAttributes (Object topObject, Class targetClass) {
		uiGenerator.refreshAttributes(topObject, AClassProxy.classProxy(targetClass));
		
	}
	public static void refreshAttributes (Object topObject) {
		uiGenerator.refreshAttributes(topObject);		
	}
	public static void deepRefreshAttributes(Object topObject) {
		
	}
	public ObjectAdapter getObjectAdapter (Object object) {
		return ObjectRegistry.getObjectAdapter(object);
	}
	public VirtualComponent getVirtualComponent(Object object) {
		ObjectAdapter objectAdapter = getObjectAdapter(object);
		if (objectAdapter == null)
			return null;
		return objectAdapter.getUIComponent();
	}
	public Component getComponent(Object object) {
		VirtualComponent virtualComponent = getVirtualComponent(object);
		if (virtualComponent == null || ! (virtualComponent.getPhysicalComponent() instanceof Component))
			return null;
		return (Component) virtualComponent.getPhysicalComponent();
	}	
	public static void setTracingLevel(
			TracingLevel outputLoggingLevel
			) {
//		Message.s_outputLoggingLevel = outputLoggingLevel;
		Tracer.setTracingLevel(outputLoggingLevel);

	}
	@Visible(false)
	public static TracingLevel getTracingLevel() {
		return Tracer.getTracingLevel();
	}
	static Double denseMagnification;
	static boolean freezeDenseMagnification;
	public static void freezeDenseMagnification() {
		freezeDenseMagnification = true;
	}
	public static Double getDenseMagnification() {
		return denseMagnification;
	}
	public static void setDenseMagnification(Double newVal) {
		if (!freezeDenseMagnification)
		denseMagnification = newVal;
	}
	
	static Dimension screenSize;
	
   
  static {
	  try {
		 screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	  } catch (HeadlessException e) {
		  System.setProperty("java.awt.headless", "true");
	  }
  }
	
//	public static OutputLoggingLevel getOutputLoggingLevel() {
//		return Message.s_outputLoggingLevel;
//	}
	
}

