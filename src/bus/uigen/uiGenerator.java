package bus.uigen;import bus.uigen.reflect.ClassProxy;import java.beans.BeanInfo;import java.beans.PropertyDescriptor;import java.io.FileInputStream;import java.io.ObjectInputStream;import java.lang.reflect.Field;import java.lang.reflect.Modifier;import java.util.Enumeration;import java.util.HashSet;import java.util.Hashtable;import java.util.Set;import java.util.Vector;import javax.swing.JOptionPane;import javax.swing.JSplitPane;import slm.SLModel;import slm.ShapesList;import util.misc.Common;import util.models.Hashcodetable;import util.trace.Tracer;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.AToolbarManager;import bus.uigen.controller.MethodParameters;import bus.uigen.controller.menus.AMenuDescriptor;import bus.uigen.controller.menus.AMethodProcessor;import bus.uigen.controller.menus.MenuSetter;import bus.uigen.controller.models.AboutManager;import bus.uigen.editors.EditorRegistry;import bus.uigen.introspect.Attribute;import bus.uigen.introspect.ClassDescriptorCache;import bus.uigen.introspect.ClassDescriptorInterface;import bus.uigen.introspect.FieldDescriptorProxy;import bus.uigen.introspect.IntrospectUtility;import bus.uigen.loggable.ACompositeLoggable;import bus.uigen.loggable.LoggableRegistry;import bus.uigen.oadapters.ClassAdapter;import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.HashtableAdapter;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.ObjectAdapterRegistry;import bus.uigen.oadapters.PrimitiveAdapter;import bus.uigen.oadapters.ReferenceAdapter;import bus.uigen.oadapters.ReferenceAdapterFactory;import bus.uigen.oadapters.RootAdapter;import bus.uigen.oadapters.ShapeObjectAdapter;import bus.uigen.oadapters.VectorAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.reflect.StandardProxyTypes;import bus.uigen.reflect.local.ReflectUtil;import bus.uigen.sadapters.ConcretePrimitive;import bus.uigen.sadapters.ConcreteShape;import bus.uigen.sadapters.ConcreteShapeFactory;import bus.uigen.sadapters.ConcreteType;import bus.uigen.sadapters.ConcreteTypeFactory;import bus.uigen.sadapters.ConcreteTypeRegistry;import bus.uigen.sadapters.GenericPrimitiveToPrimitive;import bus.uigen.trace.AttributeProcessingStarted;import bus.uigen.trace.AttributeSettingStarted;import bus.uigen.trace.AttributeSynthesisStarted;import bus.uigen.trace.ConstantsMenuAdditionEnded;import bus.uigen.trace.ConstantsMenuAdditionStarted;import bus.uigen.trace.DescendentUIComponentProcessingEnded;import bus.uigen.trace.DescendentUIComponentProcessingStarted;import bus.uigen.trace.DrawingEditorGenerationStarted;import bus.uigen.trace.EditOfNonTree;import bus.uigen.trace.EditorGenerationEnded;import bus.uigen.trace.ElideStarted;import bus.uigen.trace.LinkingRootUIComponent;import bus.uigen.trace.LogicalStructureExtractionStarted;import bus.uigen.trace.LogicalStructureLeafNodesCollectionEnded;import bus.uigen.trace.LogicalStructureLeafNodesCollectionStarted;import bus.uigen.trace.MainEditorGenerationStarted;import bus.uigen.trace.NoAdapterCreated;import bus.uigen.trace.SynthesizedAttributeProcessingStarted;import bus.uigen.trace.TreeEditorGenerationStarted;import bus.uigen.trace.UIComponentConnectionStarted;import bus.uigen.trace.UIComponentCreationStarted;import bus.uigen.trace.WidgetShellCreationStarted;import bus.uigen.view.AFlexibleBrowser;import bus.uigen.view.OEFrameSelector;import bus.uigen.view.TreeView;import bus.uigen.view.WidgetShell;import bus.uigen.viewgroups.GenerateViewObject;import bus.uigen.visitors.AddChildUIComponentsAdapterVisitor;import bus.uigen.visitors.AddSelfAdapterVisitor;import bus.uigen.visitors.AssignRowColumnAdapterVisitor;import bus.uigen.visitors.ClearVisitedNodeAdapterVisitor;import bus.uigen.visitors.CreateChildrenAdapterVisitor;import bus.uigen.visitors.CreateComponentTreeAdapterVisitor;import bus.uigen.visitors.CreateWidgetShellAdapterVisitor;import bus.uigen.visitors.ElideAdapterVisitor;import bus.uigen.visitors.ProcessAttributesWithDefaultsAdapterVisitor;import bus.uigen.visitors.ProcessDescendentUIComponentsAdapterVisitor;import bus.uigen.visitors.ProcessSynthesizedAttributesWithDefaultsAdapterVisitor;import bus.uigen.visitors.RedoExpandAdapterVisitor;import bus.uigen.visitors.SetDefaultAttributesAdapterVisitor;import bus.uigen.visitors.SetDefaultSynthesizedAttributesAdapterVisitor;import bus.uigen.widgets.VirtualComponent;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.swing.SwingTree;//comp. adding Command 
public class uiGenerator {
  
  // Temp var
  static public myLockManager lockManager;  static boolean textMode = false;
 
  static ComponentDictionary componentMapping = EditorRegistry.getComponentDictionary();
  static PrimitiveClassList primitives = new PrimitiveClassList();
  static uiFrame topFrame;
  //static Hashtable viewMethods = new Hashtable();
  
  static MenuSetter menuSetup = null;  
  static boolean initialised = false;    static boolean generating = false;  
  
  private static void init() {
    if (!initialised) {
      //registerViewMethod("java.lang.Class", "toString");//      GenerateViewObject.registerViewMethod("java.lang.Class", "toString");      GenerateViewObject.registerViewMethod("java.lang.Class", "getSimpleName");	  //registerViewMethod("bus.uigen.editors.Connections", "getDrawing");
    }
  }    //comp.    //endcomp.

  // Generate an instance of the uiFrame class
  // Recursively add components representing obj.
  // Install lman as the lockManager
  public static uiFrame generateUIFrame(Object obj) {	  //System.out.print("Object: "  + obj);	  uiFrame retVal = generateUIFrame(obj,(myLockManager) null);	  retVal.setSize();	  return retVal;
    //return generateUIFrame(obj,(myLockManager) null);
  }
  public static uiFrame generateUIFrame(Object obj, String title) {	  //System.out.print("Object: "  + obj);
    return generateUIFrame(obj,(myLockManager) null, title);
  }
  
  public static uiFrame generateUIFrame(Object obj, String title, boolean showMenus) {	  //System.out.print("Object: "  + obj);
    return generateUIFrame(obj,(myLockManager) null, title, showMenus);
  }    public static uiFrame generateUIFrame(Object obj, String title, 		  boolean showMenus, MenuSetter menuTest, AMenuDescriptor menuDescriptor){	  return generateUIFrame(obj, title, showMenus, menuTest, menuDescriptor, null, null, null);
	  /*	  //System.out.print("Object: "  + obj);	  menuSetup = menuTest;
    return generateUIFrame(obj, (myLockManager)null, title, showMenus, menuTest, menuDescriptor);
  	*/  }  public static uiFrame generateUIFrame(Object obj, String title, 		  boolean showMenus, MenuSetter menuTest, AMenuDescriptor menuDescriptor, ObjectAdapter sourceAdapter, Hashtable selfAttributes, Vector childrenAttributes){	  //System.out.print("Object: "  + obj);	  menuSetup = menuTest;    return generateUIFrame(obj, (myLockManager)null, title, showMenus, menuTest, menuDescriptor, sourceAdapter, selfAttributes, childrenAttributes);  }
  public static Object getSavedObject(String fileName) {	  try {      ObjectInputStream f = new ObjectInputStream(new FileInputStream(fileName));	        Object obj = f.readObject();	        f.close();      return obj;      /*	 uiFrame retVal = generateUIFrame(obj);	 retVal.setSaveFileName(fileName);	 return retVal;	 */	  } catch (Exception e) {      // Error in writing object      JOptionPane.showMessageDialog(null, 				    "Error reading object from "+ fileName,				    "Error",				    JOptionPane.ERROR_MESSAGE);      e.printStackTrace();    }	  return null;    }
    /*
  public static uiFrame generateUIFrameFromFile(String fileName) {	  try {
      ObjectInputStream f = new ObjectInputStream(new FileInputStream(fileName));	  
      Object obj = f.readObject();	  
      f.close();	 uiFrame retVal = generateUIFrame(obj);
	 retVal.setSaveFileName(fileName);
	 return retVal;	  } catch (Exception e) {
      // Error in writing object
      JOptionPane.showMessageDialog(null, 
				    "Error reading object from "+ fileName,
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }	  return null;
    }
    */  public static uiFrame generateUIFrameFromFile(String fileName) {	  try {		  Object obj = uiGenerator.getSavedObject(fileName);			uiFrame editor = ObjectEditor.edit(obj);			/*      ObjectInputStream f = new ObjectInputStream(new FileInputStream(fileName));	        Object obj = f.readObject();	        f.close();	 uiFrame retVal = generateUIFrame(obj);	 retVal.setSaveFileName(fileName);	 return retVal;	 */	  } catch (Exception e) {      // Error in writing object      JOptionPane.showMessageDialog(null, 				    "Error reading object from "+ fileName,				    "Error",				    JOptionPane.ERROR_MESSAGE);      e.printStackTrace();    }	  return null;    }
      public static uiFrame generateUIFrame(Object obj, ObjectAdapter sourceAdapter) {
	  //System.out.println("Object:" + obj + "from: " + sourceAdapter);	  return generateUIFrame(obj, null, sourceAdapter);
  }

  public static uiFrame generateUIFrame(Object obj, boolean editBeanInfo) {
    MethodParameters.EditBeanInfo = editBeanInfo;
    return generateUIFrame(obj, (myLockManager) null);
  }  public static uiFrame generateUIFrame(Object obj, boolean editBeanInfo, ObjectAdapter sourceAdapter) {
    MethodParameters.EditBeanInfo = editBeanInfo;
    return generateUIFrame(obj, null, sourceAdapter);
  }

  public static uiFrame generateUIFrame(Object obj, myLockManager lman) {
      //System.out.println (" generateUIFrame" + obj);	
	  	  //topFrame = new uiFrame(obj);	  topFrame = OEFrameSelector.createFrame(obj);
	  AToolbarManager.addUIFrameToolBarButtons(topFrame);	  // too early to do so proably	  /*	  if (uiFrame.toolbarCount == 1) 
		topFrame.toolBar();		*/
	  // commenting it out, does not seem to be used!	  //Container childPanel = topFrame.createNewChildPanelInNewScrollPane();
	  	  ObjectAdapter topAdapter = generateInNewBrowsableContainer(topFrame, obj);	  //topFrame.internalElide(topFrame.browsee(topAdapter), 2);	  //topFrame.setAdapter(topAdapter);
	 //topFrame.showMainPanel();
	  	  
    //return generateUIFrame(topFrame, obj, lman, null);	 // generateUIFrame(topFrame, obj, lman, null);	  return topFrame;
  }
  
    public static uiFrame generateUIFrame(Object obj, myLockManager lman, String title) {
      //System.out.println (" generateUIFrame" + obj);	  	  //topFrame = new uiFrame(obj);		  topFrame = OEFrameSelector.createFrame(obj);	
	  AToolbarManager.addUIFrameToolBarButtons(topFrame);
	  if (topFrame.toolBarCount() == 1) 
		topFrame.toolBar();	  //Container childPanel = topFrame.createNewChildPanelInNewScrollPane();
    //return generateUIFrame(topFrame, obj, lman, null, title);	  //topFrame.showMainPanel();
	  //generateUIFrame(topFrame, obj, lman, null, title);
	  ObjectAdapter adapter = generateInNewBrowsableContainer(topFrame, obj, lman, (ObjectAdapter) null, title);	  //topFrame.internalElide(topFrame.browsee(adapter), 2);
	  topFrame.setSize();
	  //topFrame.showMainPanel();	  return topFrame;
  }
  
  public static uiFrame generateUIFrame(Object obj, myLockManager lman, String title, boolean showMenus) {
      //System.out.println (" generateUIFrame" + obj);	  	  topFrame = OEFrameSelector.createFrame(obj, showMenus, null, null );	  
	  AToolbarManager.addUIFrameToolBarButtons(topFrame);
	  if (topFrame.toolBarCount() == 1) 
		topFrame.toolBar();/*	  Container childPanel = topFrame.createNewChildPanelInNewScrollPane();
     topFrame.showMainPanel();
	  //return generateUIFrame(topFrame, obj, lman, null, title);	  generateUIFrame(topFrame, obj, lman, null, title);	  */
	   ObjectAdapter adapter = generateInNewBrowsableContainer(topFrame, obj, lman, (ObjectAdapter) null, title);	  //topFrame.internalElide(topFrame.browsee(adapter), 2);
	  topFrame.setSize();
	   topFrame.showMainPanelWithoutRefreshing();	  return topFrame;
  }
    public static uiFrame generateUIFrame(Object obj, myLockManager lman, String title, boolean showMenus, MenuSetter menuTest, AMenuDescriptor theMenuDescriptor) {	  return generateUIFrame(obj, lman, title, showMenus, menuTest, theMenuDescriptor, (ObjectAdapter) null, null, null);  }  public static uiFrame generateUIFrame(Object obj, 		  							myLockManager lman, 		  							String title, 		  							boolean showMenus, 		  							MenuSetter menuTest, 		  							AMenuDescriptor theMenuDescriptor, 		  							ObjectAdapter sourceAdapter, 		  							Hashtable selfAttributes, 		  							Vector childrenAttributes) {
      //System.out.println (" generateUIFrame" + obj);	  
	  menuSetup = menuTest;	  topFrame = OEFrameSelector.createFrame(obj, showMenus, menuTest, theMenuDescriptor );	//	  topFrame.getFrame().setBackground(AttributeNames.getDefaultOrSystemDefault(AttributeNames.CONTAINER_BACKGROUND));//	  topFrame.getContainer().setBackground(AttributeNames.getDefaultOrSystemDefault(AttributeNames.CONTAINER_BACKGROUND));	  topFrame.setAttributes(selfAttributes);	  topFrame.setChildrenAttributes(childrenAttributes);	  topFrame.getFrame().setName("Top Frame (uiGenerator.generateUIFrame)");	  topFrame.getFrame().getContentPane().setName("Top ContentPane (uiGenerator.generateUIFrame");//	  topFrame.showMainPanel();
	  AToolbarManager.addUIFrameToolBarButtons(topFrame);	  /*
	  if (topFrame.toolBarCount() == 1) 
		//topFrame.toolBar();	  	topFrame.getTopViewManager().maybeShowToolbar();	  	*/	  //Container childPanel = topFrame.createNewChildPanelInNewScrollPane();
       //topFrame.showMainPanel();
	  //return generateUIFrame(topFrame, obj, lman, null, title);	  generateInNewBrowsableContainer(topFrame, obj, lman, (ObjectAdapter) sourceAdapter, title);	  if (topFrame.toolBarCount() == 1) 			//topFrame.toolBar();		  	topFrame.getTopViewManager().maybeShowToolbar();
	  topFrame.setSize();	  topFrame.setLocation();
//	  topFrame.showMainPanelWithoutRefreshing();	  topFrame.showMainPanel();	  return topFrame;
  }  
  
    public static uiFrame generateUIFrame(Object obj, myLockManager lman, ObjectAdapter sourceAdapter) {
      //System.out.println (" generateUIFrame" + obj);	  	  topFrame = OEFrameSelector.createFrame(obj);	  
	  AToolbarManager.addUIFrameToolBarButtons(topFrame);
	  if (topFrame.toolBarCount() == 1) 
		topFrame.toolBar();	  VirtualContainer childPanel = topFrame.createNewChildPanelInNewScrollPane();	  // note sure why showMainPanel is here
     //topFrame.showMainPanel();	  
	  //return generateUIFrame(topFrame, obj, lman, sourceAdapter);	  	  generateInBrowsableContainer(topFrame, obj, lman,  sourceAdapter,  childPanel, sourceAdapter.columnTitle());	  //generateUIFrame(topFrame, obj, lman, sourceAdapter);	  return topFrame;
  }
      
  public static void generateUI(VirtualContainer c, Object obj) {  	try {
    init();
    topFrame = null; // need this to prevent one method call!    ObjectAdapter rootAdapter = new RootAdapter();
    Connector.linkAdapterToComponent(rootAdapter, c);
    ObjectAdapter topAdapter = createObjectAdapter(    			 /*c,                        // parent widget */
				 rootAdapter,               // parent adaptor
				 obj,                      // object
				  ReflectUtil.toMaybeProxyTargetClass(obj),           // object's class
				 (-1),                     // position 
				 "root",                   // name of object
				 null,                     // parent object
				 false);                   // property?
    
    if (topAdapter instanceof ClassAdapter && topAdapter.getWidgetAdapter() != null &&
	topAdapter.getWidgetAdapter().getUIComponent() == null) {
      topAdapter.getWidgetAdapter().setUIComponent(c);
    } 
    
    if (topAdapter.getPropertyClass() == null)
      topAdapter.setPropertyClass( ReflectUtil.toMaybeProxyTargetClass(obj));
	if (!textMode && IntrospectUtility.isShapeModel( ReflectUtil.toMaybeProxyTargetClass(topAdapter.computeAndMaybeSetViewObject())))		topAdapter.getUIFrame().emptyMainPanel();	else	{							   uiGenerator.deepElide(topAdapter);	   if (topAdapter.getUIFrame() != null)//	   topAdapter.getUIFrame().showMainPanelWithoutRefreshing();	   topAdapter.getUIFrame().showMainPanel();
	} return;  	} catch (Exception e) {  		  	}
  }
  public static ObjectAdapter  topAddChildComponents(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, VirtualContainer childPanel) {  	// NO STATE, HIDE STATE REMOVE STATE NO SHOW STATE then return null	  return topAddChildComponents(topFrame, obj, lman, sourceAdapter, childPanel, null);
  }    public static ObjectAdapter  topAddChildComponentsOld(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, VirtualContainer childPanel, String title) {
    //System.out.print("FG<");	  // First clean up the current frame
    //System.out.println("Beginning frame generation...");	//System.out.println("top" + obj);
    init();	
    //topFrame.getChildPanel().removeAll();	//topFrame.createNewChildPanel();
    lockManager = lman;
    
	//addUIFrameToolBarButtons(topFrame);
		try {
    RootAdapter rootAdapter = new RootAdapter();
	rootAdapter.setUIFrame(topFrame);	//System.out.println("root adapter:" + rootAdapter);
    //linkAdapterToComponent(topAdapter, topFrame.getChildPanel());	
	//see if we can get away without this	Connector.linkAdapterToComponent(rootAdapter, childPanel);	String name = "root";	boolean isProperty = false;		Object parentObject = null;	if (sourceAdapter != null) {
		ObjectAdapter sourceParentAdapter = sourceAdapter.getParentAdapter();
		//if (sourceAdapter instanceof uiPrimitiveAdapter && sourceParentAdapter != null) {		if (sourceParentAdapter != null) {
			parentObject = sourceParentAdapter.getRealObject();
			rootAdapter.setRealObject(parentObject);
			//rootAdapter.setViewObject(sourceParentAdapter.getViewObject());
			rootAdapter.computeAndSetViewObject();			rootAdapter.setConcreteObject(sourceParentAdapter.getConcreteObject());			rootAdapter.setSourceAdapter(sourceParentAdapter);
						    isProperty = sourceAdapter.getAdapterType() == ObjectAdapter.PROPERTY_TYPE ;		}
			
		if ((name = sourceAdapter.getPropertyName()) == null)
			name = "root";
	}
	ClassProxy objClass;
	if (obj == null)
		objClass =StandardProxyTypes.objectClass();
	else
		objClass =  ReflectUtil.toMaybeProxyTargetClass(obj);
	//System.out.print("BT<");	ObjectAdapter topAdapter = createObjectAdapter(
				/*childPanel,*/				//topFrame.getChildPanel(), // parent widget 
				 rootAdapter,               // parent adaptor
				 obj,                      // object
				 /*obj.getClass(),           // object's class*/				 objClass,           // object's class*/
				 (-1),                     // position 
				 name,                   // name of object
				 parentObject,                     // parent object
				 isProperty);                   // property?
   rootAdapter.setChildAdapterMapping(name, topAdapter);			rootAdapter.setChildAdapterMapping(topAdapter);						//rootAdapter.childrenCreated = true;	System.out.print(">BT");
   if (title != null) {    topAdapter.setLocalAttribute(new Attribute(AttributeNames.TITLE, title));   }
   	/*
	uiWidgetAdapterInterface wa = topAdapter.getWidgetAdapter();	if (wa != null && topAdapter instanceof uiClassAdapter &&
	topAdapter.getWidgetAdapter().getUIComponent() == null) {
      topAdapter.getWidgetAdapter().setUIComponent(topFrame.getChildPanel());
    }
	*/
   System.out.print("DC<");	/*
	if (!textMode && uiBean.isShapeModel(topAdapter.getViewObject().getClass()))		topAdapter.getUIFrame().emptyMainPanel();	else	
   */	    uiGenerator.deepCreateChildren(topAdapter, true);	System.out.println(">DC");		System.out.print("UI<");
		/*		if ( childPanel == null ) {
			rootAdapter.setPreferredWidget();
			rootAdapter.processPreferredWidget();
		}
		*/
		//rootAdapter.setParentContainer(childPanel);		deepProcessAttributes(topAdapter, rootAdapter, true);
	if (sourceAdapter != null) {		//System.out.println("obj in uigenerator" + obj);		topAdapter.setSourceAdapter(sourceAdapter);
		topAdapter.setRealObject(sourceAdapter.getRealObject());		//System.out.println("real obj in uigenerator" + sourceAdapter.getRealObject());
		//topAdapter.setViewObject(sourceAdapter.getViewObject());		topAdapter.computeAndSetViewObject();
		//topAdapter.setAdapterField(sourceAdapter.getAdapterField());		
		//System.out.println("view obj in uigenerator" + sourceAdapter.getViewObject());		//System.out.println("real obj in uigenerator" + topAdapter.getRealObject());		//topAdapter.setValue(sourceAdapter.getValue());
		//}		//if (topAdapter instanceof uiPrimitiveAdapter) {		
			if (topAdapter.getAdapterType() == ObjectAdapter.PROPERTY_TYPE) {
				//topAdapter.setPropertyReadMethod(sourceAdapter.getPropertyReadMethod());				//topAdapter.setPropertyWriteMethod(sourceAdapter.getPropertyWriteMethod());
				//topAdapter.setPreReadMethod(sourceAdapter.getPreReadMethod());
				//topAdapter.setPreWriteMethod(sourceAdapter.getPreWriteMethod());				//System.out.println("real obj in uigenerator" + topAdapter.getRealObject());				//if (topAdapter instanceof uiPrimitiveAdapter)
				    topAdapter.refreshValue(sourceAdapter.getValue());
					//topAdapter.getConcreteObject().setTarget(sourceAdapter.getViewObject());
					topAdapter.refreshConcreteObject(sourceAdapter.computeAndMaybeSetViewObject());				//System.out.println("real obj in uigenerator" + topAdapter.getRealObject());
			}    else {
				//parentObject = sourceAdapter.getParentAdapter().getViewObject();
				//if (parentObject instanceof Vector) {
				if (sourceAdapter.getParentAdapter() instanceof VectorAdapter){					topAdapter.setAdapterIndex(((VectorAdapter )sourceAdapter.getParentAdapter()).getChildAdapterRealIndex(sourceAdapter));
					// In these cases, we'll have to perform a 
					// vector.setElementAt().
					
				} else if 	(sourceAdapter.getParentAdapter() instanceof HashtableAdapter){					topAdapter.setAdapterIndex(((HashtableAdapter )sourceAdapter.getParentAdapter()).getChildAdapterRealIndex(sourceAdapter));					topAdapter.setAdapterType(sourceAdapter.getAdapterType());					topAdapter.setKey(sourceAdapter.getKey());
					// In these cases, we'll have to perform a 
					// vector.setElementAt().
					
				}			}	
		//}
	}	
	//topFrame.addCurrentAdapter(topAdapter);	
	//(new ProcessAttributesAdapterVisitor (topAdapter)).traverse();
    if (topAdapter instanceof ClassAdapter && 		topAdapter.getWidgetAdapter() != null &&
	topAdapter.getWidgetAdapter().getUIComponent() == null) {
      topAdapter.getWidgetAdapter().setUIComponent(childPanel);
    } 	System.out.print(">UI");
	
    if (topAdapter.getPropertyClass() == null)
      topAdapter.setPropertyClass( ReflectUtil.toMaybeProxyTargetClass(obj));
    // Add the methods.	/*
    uiAddMethods(topFrame, obj);
    uiAddConstants(topFrame, obj);
    addHelperObjects(topFrame, obj);
	*/	
	/*	if (!uiFrame.isSavable(topAdapter))
    //if (!(obj instanceof java.io.Serializable))
      topFrame.setSaveMenuItemEnabled(false);
    // Set the frame title
    String title = (String)topAdapter.getAttributeValue(AttributeNames.TITLE);
    if (title == null) {
      // Check the class attributes
      try {
	title = (String) AttributeManager.getEnvironment().getAttribute(obj.getClass().getName(), AttributeNames.TITLE).getValue();
      } catch (Exception e) {
	title = ClassDescriptor.toShortName(obj.getClass().getName());
      }
    }
    topFrame.setTitle(title);
	*/	//topFrame.addUIFrameToolBarButton("Elide", null);		//System.out.println("setting label visible");	if (sourceAdapter != null) {
	   //topAdapter.getGenericWidget().setLabelVisible(true);	   topAdapter.getGenericWidget().setLabel(sourceAdapter.getBeautifiedPath());
	   topAdapter.setEdited(sourceAdapter.isEdited());		if (topAdapter.isEdited()) {			WidgetShell genWidget = topAdapter.getGenericWidget();			genWidget.setEdited();					}	}	System.out.print(">FG");
    //System.out.println("Done frame generation");
	//System.out.println("Beginning deep elide");	
	System.out.print("DE<");	
	if (!textMode && IntrospectUtility.isShapeModel( ReflectUtil.toMaybeProxyTargetClass(topAdapter.computeAndMaybeSetViewObject())))		topAdapter.getUIFrame().emptyMainPanel();	else		    uiGenerator.deepElide(topAdapter);	System.out.println(">DE");
		//topFrame.maybeHideMainPanel();
	//System.out.println("Ending deep elide");	return topAdapter;	} catch (Exception e) {		return null;	}
    //return topFrame;
  }
  public static ObjectAdapter  topAddChildComponents(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, VirtualContainer childPanel, String title) {
	return topAddChildComponents( topFrame,
					obj,
					lman, sourceAdapter, childPanel, title, null);
  }
  
  /*    public static Vector FirstLevelHTs(uiObjectAdapter adapter) {
	  Vector results = new Vector();
	  (new AddSelfAdapterVisitor(adapter)).traverseHTs(adapter, results);
	   return results;  }    public static Vector NextLevelHTs(Vector prevLevelHTs) {	  Vector results = new Vector();
	  (new AddSelfAdapterVisitor(adapter)).traverseChildHTs(prevLevelHTs, results);
	  return results;	    }  */    public static ObjectAdapter toTopAdapter (uiFrame theTopFrame, Object obj) {	  // we will create a new tree each time//	  ObjectAdapter retVal = theTopFrame.getBasicObjectRegistery().getObjectAdapter(obj);//	  if (retVal != null)//		  return retVal;  	try {	   RootAdapter rootAdapter = new RootAdapter();
		//uiFrame topFrame = FrameSelector.createFrame();	   //uiFrame topFrame = null;		setTopFrame(theTopFrame);		ObjectAdapter topAdapter = rootAdapter.topAddChildComponents( theTopFrame,
					obj,
					null, null, null, null);		if (ObjectEditor.colabMode())		ObjectRegistry.mapObjectToAdapter(obj, topAdapter);		return topAdapter;  	} catch (Exception e) {  		e.printStackTrace();  		return null;  	}
  }  public static ObjectAdapter toTopAdapter (Object obj) {	  return toTopAdapter(topFrame, obj);  }  
  public static void addPrimitives(ObjectAdapter adapter) {//	  long startTime = System.currentTimeMillis();		LogicalStructureLeafNodesCollectionStarted.newCase(adapter.getRealObject(), uiGenerator.class);  	Vector localPrimAdapters = (new AddSelfAdapterVisitor(adapter)).traversePrimitives();  	primAdapters.addAll(localPrimAdapters);  	for (int i = 0; i < localPrimAdapters.size(); i++) {  		PrimitiveAdapter pAdapter = (PrimitiveAdapter) localPrimAdapters.elementAt(i);  		Object tryObject =  pAdapter.getObject();  		ClassProxy primClass;  		if (tryObject != null)  			primClass =  ReflectUtil.toMaybeProxyTargetClass(tryObject);  		else  			primClass = pAdapter.getPropertyClass();  		//String typeStr = pAdapter.getObject().getClass().getName();  		String typeStr = primClass.getName();		//String pNameType = uiGenerator.beautify(pAdapter.getPropertyName()).concat(typeStr);		String pNameType = pAdapter.getPropertyName().concat(typeStr);		pString.put(pNameType, new Integer(1));		genericW.addElement((pAdapter.getGenericWidget()));		String numericTypes = "java.lang.Integerjava.lang.Double";  //can add more to these -- current examples only use these two numeric types		if (numericTypes.indexOf(typeStr) >= 0) //{			//System.out.println("numup " +	pNameType);			typeCnt[0] = typeCnt[0]+1;		//}		if (typeStr.equals("java.lang.String")) //{			//System.out.println("strup " +	pNameType);			typeCnt[2]=typeCnt[2]+1;		//}		if (typeStr.equals("java.lang.Boolean")) //{			//System.out.println("boolup " +	pNameType);			typeCnt[1]=typeCnt[1]+1;		//}		  	}	LogicalStructureLeafNodesCollectionEnded.newCase(adapter.getRealObject(), uiGenerator.class);//  	System.out.println("ADD PRIMITIVES TOOK:" + (System.currentTimeMillis() - startTime));  	  }    static boolean isAtomic(Object element) {	  return  	  		  element == null ||	  		  element instanceof String || 	  	      element instanceof Number ||	  	      element instanceof Boolean ||	  	      element.getClass().isEnum() ||	          element instanceof Character ;	    }   //static Set visitedObjects = new HashSet();// static Vector visitedObjects = new Vector();// static Hashcodetable<Object, Set<String>> visitedObjects = new Hashcodetable(); public static void addVisitedObject(Object element, Object parentObject, String component, int position, ObjectAdapter adapter, ObjectAdapter childAdapter) {//	 if (element == null) return;//	 if (element.getClass().isEnum() ) return;//	 if (element instanceof Number) return;	 if (childAdapter instanceof PrimitiveAdapter) return;	 if (element == null) return;//	 if (isAtomic(element)) return;//	 if (childAdapter.hasNoComponents()) return;//	 String fullPath = "root" + adapter.getPath();	 String path = adapter.toReferencePath(component, position);//	 Message.info("Visiting object: " + element + " as component:" + component + " of parent:" + parentObject + " whose path is:" + adapter.getPath());	 Tracer.info(uiGenerator.class, "Visiting object: " + element + " with reference:" + path + " visit #:" + adapter.getRootAdapter().getVisitedObjects().size());	 Set<String> references = new HashSet();//	 Set<String> references = new HashSet();	 references.add(path);//	 references.add(childAdapter);	 adapter.getRootAdapter().getVisitedObjects().put(element, references);	 adapter.getRootAdapter().getBasicObjectRegistery().mapObjectToAdapter(element, childAdapter); } public static boolean contains (Vector vector, Object element) {	 for (int i = 0; i < vector.size(); i++)		 if (vector.get(i) == element) return true;	 return false; }// public static boolean hasObjectBeenVisited(Object element) {//	 /*//	 if (bus.uigen.misc.Misc.isParsable(RemoteSelector.getClass(element)))//		 return false;//		 *///	 return  element != null && //	 !bus.uigen.misc.OEMisc.isParsable( ReflectUtil.toMaybeProxyTargetClass(element)) &&//	 //return false;//	 //visitedObjects.contains(element);//	 // contains(visitedObjects, element);//	 visitedObjects.get(element) != null;// } public static Set<String> previousReferences(ObjectAdapter adapter, Object element) {// public static Set<ObjectAdapter> previousReferences(Object element) {	 /*	 if (bus.uigen.misc.Misc.isParsable( IntrospectUtility.toMaybeProxyTargetClass(element)))		 return false;		 */	 if  (element == null || 	 bus.uigen.misc.OEMisc.isParsable( ReflectUtil.toMaybeProxyTargetClass(element)))		 return null;	 //return false;	 //visitedObjects.contains(element);	 // contains(visitedObjects, element);	 return adapter.getRootAdapter().getVisitedObjects().get(element); } public static void clearVisitedObjects(RootAdapter aRootAdapter) {	 Tracer.info(uiGenerator.class, "Clearing visited objects");	 aRootAdapter.getVisitedObjects().clear(); }  public static void clearVisitedObject(ObjectAdapter clearedNode) {	 Tracer.info(uiGenerator.class, "Clearing visited objects");	 clearedNode.getRootAdapter().getVisitedObjects().remove(clearedNode.getRealObject());	 clearedNode.getRootAdapter().getBasicObjectRegistery().remove(clearedNode.getRealObject()); }    public static void clearVisitedObjectsInSubtree(ObjectAdapter aSubTreeRoot) {	 Tracer.info(uiGenerator.class, "Clearing visited objects");	 aSubTreeRoot.getRootAdapter().getVisitedObjects().clearExcept(aSubTreeRoot.getObjectsInPathToRoot()); } static long startTime =System.currentTimeMillis() ; static long prevTime = startTime; ; static boolean profile = false; public static void setProfile(boolean newVal) {	 profile = newVal; } public static boolean getProfile() {	 return profile; } public static void printTime(String msg) {	 if (!profile)		 return;	 long currentTime = System.currentTimeMillis();	System.out.println(msg + ":" + (currentTime - prevTime) + ":" + (currentTime - startTime));	 prevTime = currentTime; }      public static ObjectAdapter  topAddChildComponents(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, VirtualContainer childPanel, String title, VirtualContainer topPanel) {	  //System.out.println();	  if (obj != null) {		  Tracer.info(uiGenerator.class, "Top-level object submitted for editing: "  + AFlexibleBrowser.prefix(obj.toString()));	  }	  else {		  Tracer.info(uiGenerator.class, "Object: "  + "null");	  }//	  Message.info("Object: "  + AFlexibleBrowser.prefix(obj.toString()));//	  else//		  System.out.println("Object: "  + "null");	  if (topFrame != null) {		  ObjectAdapter topAdapter = topFrame.getTopAdapter();		  if (topAdapter != null) {			  if (!(obj instanceof SLModel)) {				  (new ClearVisitedNodeAdapterVisitor(topAdapter)).traverseNonAtomicChildrenContainers();			  }		  }	  }//	  clearVisitedObjects(topAdap);
    //System.out.print("FG<");	  // First clean up the current frame
    //System.out.println("Beginning frame generation...");	//System.out.println("top" + obj);
    init();	
    //topFrame.getChildPanel().removeAll();	//topFrame.createNewChildPanel();
    lockManager = lman;
    
	//addUIFrameToolBarButtons(topFrame);
		try {
    RootAdapter rootAdapter = new RootAdapter();    printTime("before topAddChildComponents");	LogicalStructureExtractionStarted.newCase(obj, uiGenerator.class);	ObjectAdapter topAdapter = rootAdapter.topAddChildComponents( topFrame,
					obj,
					lman, sourceAdapter, childPanel, title);	if (topAdapter == null)		return null;	if (topFrame != null)  {		topFrame.setOriginalAdapter(topAdapter);		topFrame.setFrameAttributes(topAdapter);	}//	if (topFrame.isOnlyGraphicsPanel)//		return topAdapter;    //printTime("topAddChildComponents");
	//Connector.linkAdapterToComponent(rootAdapter, childPanel);
	//Connector.linkAdapterToComponent(topAdapter, topPanel);
	//System.out.print("UI<");	//uiGenerator.deepCreateChildren(rootAdapter);	//uiGenerator.deepElide(topAdapter);//	if (childPanel == topFrame.getDrawPanel())  {//		topAdapter.setPreferredWidget();//		 topAdapter.processPreferredWidget();//	} else {	uiGenerator.deepProcessAttributes(topAdapter, topPanel, rootAdapter, childPanel, true);	if (topFrame != null && !topFrame.isOnlyGraphicsPanel)	uiGenerator.addPrimitives(topAdapter);//	}		//System.out.print(">UI");	/*
	if ((!textMode && ((topAdapter.getWidgetAdapter() != null 			&& (!topAdapter.isDynamic() && topAdapter.getWidgetAdapter().isEmpty())) || 			uiBean.isShapeModel(topAdapter.getViewObject().getClass())))) 		//|| topAdapter.getUIFrame().isEmptyMainPanel())				topAdapter.getUIFrame().emptyMainPanel();	*/	// looks like the following code is useless, so am commenting it out//	if (!textMode && topAdapter.getWidgetAdapter() != null //			&& IntrospectUtility.isShapeModel(RemoteSelector.getClass(topAdapter.computeAndMaybeSetViewObject()))) //		//|| topAdapter.getUIFrame().isEmptyMainPanel())		//		topAdapter.getUIFrame().emptyMainPanel();//	// trying to figure out if a tree panel is being added dynamically,if so, do not show hidden main panel//	else if (!(topPanel instanceof SwingTree) || !(childPanel instanceof SwingScrollPane)){//	    //uiGenerator.deepElide(topAdapter);//		topFrame.showMainPanel();
//	}	
		//topFrame.maybeHideMainPanel();
	//System.out.println("Ending deep elide");	return topAdapter;	} catch (Exception e) {		System.out.println ("topChildUIComponents:" + e);		e.printStackTrace();		return null;	}
    //return topFrame;
  }  public static void deepAssignRowColumns(ObjectAdapter topAdapter) {
	  Vector rowAdapters = (new AssignRowColumnAdapterVisitor(topAdapter, null)).traverse(0);
	  for (int rowNum = 0; rowNum < rowAdapters.size(); rowNum++) {		  ObjectAdapter adapter =  (ObjectAdapter) rowAdapters.elementAt(rowNum);		  int colNum = adapter.getLevel() - 1;
		  assignRowColumnToTreePath(adapter, rowNum, colNum);		  if (adapter instanceof CompositeAdapter)			  assignRowColumnToChildren ((CompositeAdapter) adapter, rowNum, colNum);
	  }  }  public static void assignRowColumnToTreePath(ObjectAdapter adapter, int row, int col) {
		if (adapter == null) return;		
		/*		adapter.setRow(row);
		adapter.setColumn(col);
		System.out.println("Assigning " + adapter.toString() + " row" + row + " col " + col);
		*/		assignRowColumn(adapter, row, col);		if (adapter.isTopAdapter()) return;
		assignRowColumnToTreePath(adapter.getParentAdapter(), row, col - 1);  }  public static void assignRowColumnToChildren (CompositeAdapter parent, int rowNum, int parentColNum) {	  for (int childIndex = 0; childIndex < parent.getChildAdapterCount(); childIndex++) {		  assignRowColumn ((ObjectAdapter) parent.getChildAdapterAt(childIndex),
						   rowNum,						   parentColNum + 1 + childIndex);
	  }  
	    }  public static void assignRowColumn(ObjectAdapter adapter, int row, int col) {
		if (adapter == null) return;		adapter.setRow(row);
		adapter.setColumn(col);
		System.out.println("Assigning " + adapter.toString() + " row" + row + " col " + col);
		//assignRowColumnToTreePath(adapter.getParent(), row, col - 1);  }
  /*  public static void deepProcessAttributes(uiObjectAdapter topAdapter) {
	  deepProcessAttributes(topAdapter, false);  }  */  public static void deepProcessAttributes (ObjectAdapter adapter) {	  deepProcessAttributes (adapter, adapter.isTopDisplayedAdapter());  }  public static void deepProcessAttributes(ObjectAdapter topAdapter, boolean isTopDisplayedAdapter) {  	//deepProcessAttributes (topAdapter, isTopDisplayedAdapter, 0, Integer.MAX_VALUE);	  boolean isOnlyGraphics = topAdapter.getUIFrame() != null && topAdapter.getUIFrame().isOnlyGraphicsPanel;	  	  AttributeSettingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  (new SetDefaultAttributesAdapterVisitor (topAdapter)).traverse();	  if (!isOnlyGraphics) {	  SynthesizedAttributeProcessingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  (new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	   //deepAssignRowColumns(topAdapter);	  	  deepCreateWidgetShell(topAdapter);	   //(new CreateWidgetShellAdapterVisitor (topAdapter)).traverse();	  UIComponentCreationStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  }	   (new AddChildUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();		   if (!isOnlyGraphics) {	   deepElide(topAdapter);	   UIComponentConnectionStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	   (new CreateComponentTreeAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   }	   //moving it up	   //deepElide(topAdapter);	   	  // (new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   //(new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  AttributeProcessingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	   (new ProcessAttributesWithDefaultsAdapterVisitor (topAdapter)).traverse();	   if (!isOnlyGraphics) {	   SynthesizedAttributeProcessingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  (new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrder();	  DescendentUIComponentProcessingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  (new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	  DescendentUIComponentProcessingEnded.newCase(topAdapter.getRealObject(), uiGenerator.class);	   }  }   public static void deepProcessAttributes(ObjectAdapter topAdapter, boolean isTopDisplayedAdapter, int from) {  	deepProcessAttributes (topAdapter, isTopDisplayedAdapter, from, Integer.MAX_VALUE);	    }
  public static void deepProcessAttributes(ObjectAdapter topAdapter, boolean isTopDisplayedAdapter, int from, int to) {	  boolean wasLocked = locked(topAdapter);	  if (!wasLocked)	  maybeSetGraphicsScreenLocked(topAdapter, true);	  	  boolean isOnlyGraphics = topAdapter.getUIFrame() != null && topAdapter.getUIFrame().isOnlyGraphicsPanel;  		  (new SetDefaultAttributesAdapterVisitor (topAdapter)).traverseRange(from, to);	  if (!isOnlyGraphics) {	  	  (new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrderRange(from, to);	   //deepAssignRowColumns(topAdapter);	  deepCreateWidgetShell(topAdapter);	  }	   //(new CreateWidgetShellAdapterVisitor (topAdapter)).traverseRange(from, to);	   (new AddChildUIComponentsAdapterVisitor (topAdapter, false)).traverseNonAtomicContainersRange(from, to);	   if (!isOnlyGraphics) {	   deepElide(topAdapter);	  	   (new CreateComponentTreeAdapterVisitor (topAdapter)).traverseNonAtomicContainersRange(from, to);	   }	  	   //deepElide(topAdapter);	   /*	   (new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   if (topAdapter.isFlatTableRow()) {		   uiObjectAdapter tableAdapter = topAdapter.getTableAdapter();		   uiWidgetAdapterInterface widgetAdapter = tableAdapter.getWidgetAdapter();		   if (tableAdapter != topAdapter && widgetAdapter != null)			   widgetAdapter.descendentUIComponentsAdded();		   //(new ProcessDescendentUIComponentsAdapterVisitor (tableAdapter)).traverseNonAtomicContainers();	   } 	   */	   //(new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  (new ProcessAttributesWithDefaultsAdapterVisitor (topAdapter)).traverse();	  if (!isOnlyGraphics) {	  (new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrderRange(from, to);	  (new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   if (topAdapter.isFlatTableComponent()) {		   ObjectAdapter flatTableRowAdapter = topAdapter.getFlatTableRowAncestor();		   WidgetAdapterInterface widgetAdapter = flatTableRowAdapter.getWidgetAdapter();		   if (flatTableRowAdapter != topAdapter && widgetAdapter != null)			   widgetAdapter.descendentUIComponentsAdded();	   }	  if (topAdapter.isFlatTableRow()) {//	  if (topAdapter.isFlatTableComponent()) {		   ObjectAdapter tableAdapter = topAdapter.getTableAdapter();		   WidgetAdapterInterface widgetAdapter = tableAdapter.getWidgetAdapter();		   if (tableAdapter != topAdapter && widgetAdapter != null)			   widgetAdapter.descendentUIComponentsAdded();		   //(new ProcessDescendentUIComponentsAdapterVisitor (tableAdapter)).traverseNonAtomicContainers();	   } 	  }	  if (!wasLocked)		  maybeSetGraphicsScreenLocked(topAdapter, false);	  
  }     public static void deepSynthesizeAttributes(ObjectAdapter topAdapter, boolean isTopDisplayedAdapter) {	  (new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  (new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrder();	   }  public static void deepSetAndProcessAttributes(ObjectAdapter topAdapter) {	  deepSetAttributes(topAdapter);	  deepProcessSetAttributes(topAdapter);  }  public static void deepSetAttributes(ObjectAdapter topAdapter) {	  boolean isTopDisplayedAdapter = topAdapter.isTopDisplayedAdapter();	  (new SetDefaultAttributesAdapterVisitor (topAdapter)).traverse();	  (new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  // deepAssignRowColumns(topAdapter);	    }  public  static void deepProcessSetAttributes (ObjectAdapter topAdapter) {	  boolean isTopDisplayedAdapter = topAdapter.isTopDisplayedAdapter();	  (new ProcessAttributesWithDefaultsAdapterVisitor (topAdapter)).traverse();	  (new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrder();  }  public static void deepProcessAttributes(ObjectAdapter topAdapter,
										   ObjectAdapter rootAdapter, boolean isTopDisplayedAdapter) {	  (new SetDefaultAttributesAdapterVisitor (rootAdapter)).traverse();	  (new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  // deepAssignRowColumns(topAdapter);	  deepCreateWidgetShell(topAdapter);	  //(new CreateWidgetShellAdapterVisitor (topAdapter)).traverse();
	   (new AddChildUIComponentsAdapterVisitor (rootAdapter)).traverseNonAtomicContainers();	   	   (new CreateComponentTreeAdapterVisitor (rootAdapter)).traverseNonAtomicContainers();	   	   (new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   //(new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  (new ProcessAttributesWithDefaultsAdapterVisitor (topAdapter)).traverse();	  (new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrder();
  }  static boolean lockScreen;  public static void maybeSetGraphicsScreenLocked(ObjectAdapter topAdapter, boolean newVal) {	  if (!lockScreen)		  return;	  	  if (topAdapter == null) return;	  	  topAdapter.getUIFrame().setGraphicsWindowLocked(newVal);	 	    }  public static boolean locked(ObjectAdapter topAdapter) {	  if (topAdapter == null) return true;	  ShapesList drawing = topAdapter.getUIFrame().getDrawing();	  if (drawing ==null) return true;	  return drawing.locked();	    }  public static void deepProcessAttributes(ObjectAdapter topAdapter,										   VirtualContainer topPanel,
										   ObjectAdapter rootAdapter,
										   VirtualContainer rootPanel,
										   boolean isTopDisplayedAdapter) {	  boolean wasLocked = locked(topAdapter);	  if (!wasLocked)	  maybeSetGraphicsScreenLocked(topAdapter, true);  	 /*  	if (topAdapter instanceof uiContainerAdapter)   	 	System.out.println("topAdapter direction " + ((uiContainerAdapter) topAdapter).getDirection());	  *///	  Tracer.info(uiGenerator.class, "Setting  Attributes");//	  uiFrame topFrame = topAdapter.getUIFrame();	  if (topAdapter == null)		  return;	  AttributeSettingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  (new SetDefaultAttributesAdapterVisitor (rootAdapter)).traverse();	  printTime("SetDefaultAttributesAdapterVisitor");	  boolean isOnlyGraphics = topFrame != null && topFrame.isOnlyGraphicsPanel();	  /*	  if (topAdapter instanceof uiContainerAdapter)   	 	System.out.println("topAdapter direction after set default" + ((uiContainerAdapter) topAdapter).getDirection());	  *///	  Tracer.info(uiGenerator.class, "Synthesizing Attributes");	  if (!isOnlyGraphics) {	  AttributeSynthesisStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  (new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  }	  // deepAssignRowColumns(topAdapter);	  //printTime("SetDefaultSynthesizedAttributesAdapterVisitor");//	  Tracer.info(uiGenerator.class, "Linking root to rootPanel");//	  if (topFrame != null && !topFrame.isOnlyGraphicsPanel) {	  if (!isOnlyGraphics) {		  LinkingRootUIComponent.newCase(topAdapter.getRealObject(), uiGenerator.class);	 	  Connector.linkAdapterToComponent(rootAdapter, rootPanel);	  }	  	  if (topPanel != null)	     //topAdapter.setPreferredWidget(topPanel.getClass().getName());		  topAdapter.fixUIComponent(true);
	  Connector.linkAdapterToComponent(topAdapter, topPanel);	  //	  WidgetShellCreationStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);//	  Tracer.info(uiGenerator.class, "Creating widget shells");	  if (!isOnlyGraphics) {	  deepCreateWidgetShell(topAdapter);	  printTime("CreateWidgetShellAdapterVisitor");	  }//	  }	  	  //(new CreateWidgetShellAdapterVisitor (topAdapter)).traverse();	  Tracer.info(uiGenerator.class, "Binding UI components");
	   //(new AddChildUIComponentsAdapterVisitor (rootAdapter)).traverseNonAtomicContainers();	   //(new AddChildUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	  	  	  UIComponentCreationStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	  	   (new AddChildUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   printTime("AddChildUIComponentsAdapterVisitor");	   if (!isOnlyGraphics) {	   Tracer.info(uiGenerator.class, "Eliding object adapters");	  //moving it earlier	   //		  ElideStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	   	   deepElide(topAdapter);  }//	   Tracer.info(uiGenerator.class, "Processing bound UI components");	   if (!isOnlyGraphics) {	  UIComponentConnectionStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);	   (new CreateComponentTreeAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   }		  	   //KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();	   //moved it up	   //deepElide(topAdapter);//	   printTime("deepElide");
	   //(new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   //(new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();	  		  AttributeProcessingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);//	   Tracer.info(uiGenerator.class, "Processing regular attributes");	   	  (new ProcessAttributesWithDefaultsAdapterVisitor (topAdapter)).traverse();	  printTime("ProcessAttributesWithDefaultsAdapterVisitor");	  if (!isOnlyGraphics) {	  	  SynthesizedAttributeProcessingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);//	   Tracer.info(uiGenerator.class, "Processing synthesized attributes");	  (new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrder();	  }	  //printTime("ProcessSynthesizedAttributesWithDefaultsAdapterVisitor");	  	  if (!isOnlyGraphics) {	  	  DescendentUIComponentProcessingStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);//	  Tracer.info(uiGenerator.class, "Processing addition of children UI components");	  	  (new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	  	  DescendentUIComponentProcessingEnded.newCase(topAdapter.getRealObject(), uiGenerator.class);	  }	  //printTime("ProcessDescendentUIComponentsAdapterVisitor");	  if (!wasLocked)	  maybeSetGraphicsScreenLocked(topAdapter, false);
  }  public static void deepProcessAttributes(ObjectAdapter topAdapter,		   VirtualContainer topPanel,		   ObjectAdapter rootAdapter,		   VirtualContainer rootPanel,		   boolean isTopDisplayedAdapter,		   Hashtable sharedPs) {/*if (topAdapter instanceof uiContainerAdapter) System.out.println("topAdapter direction " + ((uiContainerAdapter) topAdapter).getDirection());*/(new SetDefaultAttributesAdapterVisitor (rootAdapter)).traverse(sharedPs);/*if (topAdapter instanceof uiContainerAdapter) System.out.println("topAdapter direction after set default" + ((uiContainerAdapter) topAdapter).getDirection());*///(new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder(sharedPs);// deepAssignRowColumns(topAdapter);Connector.linkAdapterToComponent(rootAdapter, rootPanel);Connector.linkAdapterToComponent(topAdapter, topPanel);deepCreateWidgetShell(topAdapter);//(new CreateWidgetShellAdapterVisitor (topAdapter)).traverseChildren(sharedPs);(new AddChildUIComponentsAdapterVisitor (rootAdapter)).traverseNonAtomicContainers(sharedPs);(new CreateComponentTreeAdapterVisitor (rootAdapter)).traverseNonAtomicContainers(sharedPs);// why isprocess descendents commented out?//(new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();//(new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();//(new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();(new ProcessAttributesWithDefaultsAdapterVisitor (topAdapter)).traverseChildren(sharedPs);//(new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrder(sharedPs);(new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();}
  public static void deepAddChildUIComponents(ObjectAdapter topAdapter) {	  (new AddChildUIComponentsAdapterVisitor (topAdapter)).traverse();		  (new CreateComponentTreeAdapterVisitor (topAdapter)).traverse();		  (new ProcessDescendentUIComponentsAdapterVisitor (topAdapter)).traverseNonAtomicContainers();	   //(new SetDefaultSynthesizedAttributesAdapterVisitor (topAdapter, isTopDisplayedAdapter)).traversePostOrder();
  }   //public static uiFrame generateUIScrollPane(uiFrame topFrame,  public static ObjectAdapter generateUIScrollPane(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter) {	   return generateUIScrollPane (topFrame, obj, lman, sourceAdapter, JSplitPane.VERTICAL_SPLIT);
   }   //public static uiFrame generateInUIPanel(uiFrame topFrame,
  public static ObjectAdapter generateInUserContainer(uiFrame topFrame,
					Object obj, VirtualContainer childPanel, boolean rootPanel) {	  	   //uiObjectAdapter adapter = generateInContainer(topFrame, obj, (uiObjectAdapter) null, childPanel);	  ObjectAdapter adapter = null;	  if (rootPanel)		  adapter = uiGenerator.generateInUIPanel (topFrame, obj, null, null, childPanel, null, null);	  else		  adapter = uiGenerator.generateInUIPanel (topFrame, obj, null, null, null, null, childPanel);	  if (adapter == null)		  return null;	   topFrame.addUserAdapter(adapter);
	   topFrame.setTitle();		
	   //frame.validate();	   return adapter;
      }    public static ObjectAdapter generateInUserContainer(uiFrame topFrame,			Object obj, VirtualContainer childPanel) {	 return  generateInUserContainer(topFrame, obj, childPanel, false);	    }  public static ObjectAdapter generateInContainer(uiFrame topFrame,
					Object obj, VirtualContainer childPanel) {	   return generateInContainer(topFrame, obj, (ObjectAdapter) null, childPanel);
      }  
  public static ObjectAdapter generateInContainer(uiFrame topFrame,
					Object obj,ObjectAdapter sourceAdapter, VirtualContainer childPanel, boolean rootPanel) {	  	  	   //return generateInUIPanel(topFrame, obj, null, sourceAdapter, childPanel);	  if (rootPanel)		  return generateInUIPanel(topFrame, obj, null, sourceAdapter, childPanel, null, null);	  else	   return generateInUIPanel(topFrame, obj, null, sourceAdapter, null, null, childPanel);
      }  public static ObjectAdapter generateInContainer(uiFrame topFrame,			Object obj,ObjectAdapter sourceAdapter, VirtualContainer childPanel) {//return generateInUIPanel(topFrame, obj, null, sourceAdapter, childPanel);return generateInUIPanel(topFrame, obj, null, sourceAdapter, null, null, childPanel);}
  public static ObjectAdapter generateInBrowsableContainer(uiFrame topFrame,
					Object obj,ObjectAdapter sourceAdapter, VirtualContainer childPanel) {	   //uiObjectAdapter retVal =  generateInContainer(topFrame, obj, sourceAdapter, childPanel);	   ObjectAdapter retVal = generateInUIPanel(topFrame, obj, null, sourceAdapter, null, null, childPanel);	   	   topFrame.addCurrentAdapter(retVal, childPanel);	   return retVal;
      }
  public static ObjectAdapter generateInBrowsableContainer(uiFrame topFrame,
					Object obj,ObjectAdapter sourceAdapter, VirtualContainer childPanel, VirtualContainer topPanel) {	   //uiObjectAdapter retVal =  generateInContainer(topFrame, obj, sourceAdapter, childPanel, topPanel);	    ObjectAdapter adapter = generateInBrowsableRootPanel(topFrame, obj, null,  sourceAdapter,  childPanel, null, topPanel);
	   //topFrame.addCurrentAdapter(adapter, childPanel);	   return adapter;
      }  
  public static ObjectAdapter generateInBrowsableContainer(uiFrame topFrame,
					Object obj,VirtualContainer childPanel) {	   return  generateInBrowsableContainer(topFrame, obj, (ObjectAdapter) null, childPanel);	   
      }
  public static ObjectAdapter generateInNewBrowsableContainer(uiFrame topFrame,
					Object obj) {		return generateInNewBrowsableContainer(topFrame, obj, (ObjectAdapter) null);
  }
    public static ObjectAdapter generateInNewBrowsableContainer(uiFrame topFrame,
					Object obj,	ObjectAdapter sourceAdapter) {		return generateInNewBrowsableContainer(topFrame, obj, sourceAdapter, JSplitPane.VERTICAL_SPLIT);
  }
  public static ObjectAdapter generateInNewBrowsableContainer(uiFrame topFrame,
					Object obj,	ObjectAdapter sourceAdapter, int direction) {
 	VirtualContainer childPanel = topFrame.createNewChildPanelInNewScrollPane(direction);	 //Container childPanel = topFrame.newContainer(direction);
	 //topFrame.showMainPanel();	//return generateInUIPanel(topFrame,	obj, lman, sourceAdapter, direction, (String) null);
		//uiObjectAdapter retVal = generateInBrowsableContainer(topFrame, obj, sourceAdapter, childPanel);	ObjectAdapter retVal =  generateInBrowsableRootPanel(topFrame, obj, null, sourceAdapter, childPanel, null, null);	/*	generateInUIPanel(uiFrame theTopFrame,			Object obj,			myLockManager lman, uiObjectAdapter sourceAdapter, VirtualContainer childPanel, String title, VirtualContainer topPanel)	   */	   //uiObjectAdapter retVal = generateInUIPanel(topFrame, obj, null, sourceAdapter, null, null, childPanel);	   	   //topFrame.addCurrentAdapter(retVal, childPanel);	   //return retVal;	//topFrame.internalElide(topFrame.browsee(retVal), 2);	return retVal;
  }  public static ObjectAdapter generateInNewBrowsableContainer(uiFrame theTopFrame,
					Object obj,
					VirtualContainer topPanel) {
	  VirtualContainer childPanel = theTopFrame.createNewChildPanelInNewScrollPane();	  childPanel.add(topPanel);
	  ObjectAdapter retVal =  generateInBrowsableContainer(theTopFrame, obj, childPanel, topPanel);
	  //topFrame.internalElide(topFrame.browsee(retVal), 2);	  return retVal;  }
     //public static uiFrame generateInUIPanel(uiFrame theTopFrame,   public static ObjectAdapter generateInUIPanel(uiFrame theTopFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, VirtualContainer childPanel) {
	  return  generateInUIPanel(theTopFrame, obj, lman,  sourceAdapter,  childPanel, null);   }
   public static ObjectAdapter generateInBrowsableContainer(uiFrame theTopFrame,
					Object obj,
					VirtualContainer childPanel, VirtualContainer topPanel) {
	   /*      uiObjectAdapter adapter = generateInUIPanel(theTopFrame, obj, null,  null,  childPanel, null, topPanel);
	  topFrame.addCurrentAdapter(adapter, childPanel);	   */
	  //return adapter;
	   return generateInBrowsableContainer(theTopFrame, obj,  childPanel, null, topPanel);   }   
   public static ObjectAdapter generateInBrowsableContainer(uiFrame theTopFrame,
					Object obj,
					VirtualContainer childPanel, String title, VirtualContainer topPanel) {      ObjectAdapter adapter = generateInBrowsableRootPanel(theTopFrame, obj, null,  null,  childPanel, title, topPanel);
	  //topFrame.addCurrentAdapter(adapter, childPanel);
	  return adapter;   }   public static ObjectAdapter generateInNewBrowsableContainer(uiFrame theTopFrame,
					Object obj,					myLockManager lman, ObjectAdapter sourceAdapter,
					String title) {
	  VirtualContainer childPanel = theTopFrame.createNewChildPanelInNewScrollPane();     //uiObjectAdapter adapter = generateInBrowsableContainer(theTopFrame, obj, lman,  sourceAdapter,  childPanel, title);      ObjectAdapter retVal =  generateInBrowsableRootPanel(theTopFrame, obj, lman, sourceAdapter, childPanel, title, null);  	/*  	generateInUIPanel(uiFrame theTopFrame,  			Object obj,  			myLockManager lman, uiObjectAdapter sourceAdapter, VirtualContainer childPanel, String title, VirtualContainer topPanel)  	   */  	   //uiObjectAdapter retVal = generateInUIPanel(topFrame, obj, null, sourceAdapter, null, null, childPanel);  	     	   //topFrame.addCurrentAdapter(retVal, childPanel);
	  //topFrame.internalElide(topFrame.browsee(adapter), 2);
	  //return adapter;  	   return retVal;   }
   public static ObjectAdapter generateInBrowsableContainer(uiFrame theTopFrame,
					Object obj,					myLockManager lman, ObjectAdapter sourceAdapter,
					VirtualContainer childPanel,					//VirtualScrollPane childPanel,					String title) {      ObjectAdapter adapter = generateInUIPanel(theTopFrame, obj, lman,  sourceAdapter,  childPanel, title);
	  topFrame.addCurrentAdapter(adapter, childPanel);
	  return adapter;   }      public static ObjectAdapter generateInContainer(uiFrame theTopFrame,
					Object obj,
					VirtualContainer childPanel, VirtualContainer topPanel) {      return generateInUIPanel(theTopFrame, obj, null,  null,  childPanel, null, topPanel);   }
      public static ObjectAdapter generateInUIPanel(uiFrame theTopFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, VirtualContainer childPanel, String title) {      return generateInUIPanel(theTopFrame, obj, lman,  sourceAdapter,  childPanel, title, null);   }   public static ObjectAdapter generateInBrowsableRootPanel(uiFrame theTopFrame,			Object obj,			myLockManager lman, ObjectAdapter sourceAdapter, 			VirtualContainer childPanel,			//VirtualScrollPane childPanel,			String title, VirtualContainer topPanel) {	   ObjectAdapter retVal = generateInUIPanel(theTopFrame,	obj,lman, sourceAdapter,  childPanel,  title,  topPanel);   theTopFrame.addCurrentAdapter(retVal, childPanel);   return retVal;   }
     //public static uiFrame generateInUIPanel(uiFrame theTopFrame,      static boolean aboutPrinted = false;      public static ObjectAdapter generateInUIPanel(uiFrame theTopFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, VirtualContainer childPanel, String title, VirtualContainer topPanel) {	if (!aboutPrinted) {		AboutManager.printAbout();		aboutPrinted = true;	}	Object newCaseObject = obj;	if (sourceAdapter != null)		newCaseObject = sourceAdapter.getRealObject(); // not sure why do this but this is how the commented next line was	if (obj instanceof SLModel)	//		DrawingEditorGenerationStarted.newCase(sourceAdapter.getRealObject(), uiGenerator.class);	DrawingEditorGenerationStarted.newCase(newCaseObject, uiGenerator.class);	else if (topPanel instanceof SwingTree || title == TreeView.TREE_VIEW_NAME) 		TreeEditorGenerationStarted.newCase(obj, uiGenerator.class);	else		MainEditorGenerationStarted.newCase(obj, uiGenerator.class);    	currentObject = obj;   	//Container childPanel = topFrame.createNewChildPanelInNewScrollPane(direction);
	topFrame = theTopFrame;		ObjectAdapter topAdapter = topAddChildComponents(topFrame, obj, lman, sourceAdapter, childPanel, title, topPanel);	// this is probably not needed as topAddChildComponents now sets it	
	topFrame.setOriginalAdapter(topAdapter);	//topFrame.replaceTopCurrentAdapter(topAdapter);
		initializeClassDescriptorCache();		  		//addPredefinedMenuModels(topFrame);//		if (!topFrame.isDummy()) {		addMenuObjects (topFrame, obj);		//uiAddMethods(topFrame, obj);		
    uiAddConstants(topFrame, obj);
//    addHelperObjects(topFrame, obj);    topFrame.setFontSize();//		}    //topFrame.setFontSize(AFontSizeModel.getFontSize());	//addPredefinedMenuModels(topFrame);
	/*	// First clean up the current frame
    System.out.println("Beginning frame generation...");
    init();	
    //topFrame.getChildPanel().removeAll();	//topFrame.createNewChildPanel();
    lockManager = lman;
    
	//addUIFrameToolBarButtons(topFrame);
		
    uiObjectAdapter rootAdapter = new uiClassAdapter();
    //linkAdapterToComponent(topAdapter, topFrame.getChildPanel());	linkAdapterToComponent(rootAdapter, childPanel);	String name = "root";	boolean isProperty = false;		Object parentObject = null;	if (sourceAdapter != null) {
		uiObjectAdapter sourceParentAdapter = sourceAdapter.getParentAdapter();
		if (sourceAdapter instanceof uiPrimitiveAdapter && sourceParentAdapter != null) {
			parentObject = sourceParentAdapter.getRealObject();
			rootAdapter.setRealObject(parentObject);
			rootAdapter.setViewObject(sourceParentAdapter.getViewObject());		}
					isProperty = sourceAdapter.getAdapterType() == uiObjectAdapter.PROPERTY_TYPE ;	
		if ((name = sourceAdapter.getPropertyName()) == null)
			name = "root";
	}	uiObjectAdapter topAdapter = uiAddComponents(topFrame.getChildPanel(), // parent widget 
				 rootAdapter,               // parent adaptor
				 obj,                      // object
				 obj.getClass(),           // object's class
				 (-1),                     // position 
				 name,                   // name of object
				 parentObject,                     // parent object
				 isProperty);                   // property?
    
	
	topAdapter.setRealObject(sourceAdapter.getRealObject());
	topAdapter.setViewObject(sourceAdapter.getViewObject());	if (topAdapter instanceof uiPrimitiveAdapter) {		//System.out.println("setting label visible");
		topAdapter.getGenericWidget().setLabelVisible(true);
		if (topAdapter.getAdapterType() == uiObjectAdapter.PROPERTY_TYPE) {
		     topAdapter.setPropertyReadMethod(sourceAdapter.getPropertyReadMethod());			 topAdapter.setPropertyWriteMethod(sourceAdapter.getPropertyWriteMethod());			 topAdapter.setValue(sourceAdapter.getValue());
		}	
		topFrame.addCurrentAdapter(topAdapter);
	} else	
	   topFrame.addCurrentAdapter(topAdapter);	
    if (topAdapter instanceof uiClassAdapter &&
	topAdapter.getWidgetAdapter().getUIComponent() == null) {
      topAdapter.getWidgetAdapter().setUIComponent(childPanel);
    } 
	
    if (topAdapter.getPropertyClass() == null)
      topAdapter.setPropertyClass(obj.getClass());	*/
    // Add the methods.	/*
    uiAddMethods(topFrame, obj);
    uiAddConstants(topFrame, obj);
    addHelperObjects(topFrame, obj);	*/
	/*	if (!uiFrame.isSavable(topAdapter))
    //if (!(obj instanceof java.io.Serializable))
      topFrame.setSaveMenuItemEnabled(false);
    // Set the frame title
    String title = (String)topAdapter.getAttributeValue(AttributeNames.TITLE);
    if (title == null) {
      // Check the class attributes
      try {
	title = (String) AttributeManager.getEnvironment().getAttribute(obj.getClass().getName(), AttributeNames.TITLE).getValue();
      } catch (Exception e) {
	title = ClassDescriptor.getMethodsMenuName(obj.getClass());
      }
    }
    topFrame.setTitle(title);
	*/	//topFrame.addUIFrameToolBarButton("Elide", null);	/*
    System.out.println("Done frame generation");
	System.out.println("Beginning deep elide");	topFrame.deepElide(topAdapter);
	System.out.println("Ending deep elide");	*/
    //return topFrame;    EditorGenerationEnded.newCase(obj, uiGenerator.class);	return topAdapter;
  }   
  
  //public static uiFrame generateUIScrollPane(uiFrame topFrame,  public static ObjectAdapter generateUIScrollPane(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, int direction) {
 	VirtualContainer childPanel = topFrame.createNewChildPanelInNewScrollPane(direction);	 //Container childPanel = topFrame.newContainer(direction);
	 //topFrame.showMainPanel();	//return generateInUIPanel(topFrame,	obj, lman, sourceAdapter, direction, (String) null);
	//uiObjectAdapter topAdapter = generateInUIPanel(topFrame,	obj, lman, sourceAdapter, childPanel);	ObjectAdapter topAdapter = generateInUIPanel(topFrame,	obj, lman, sourceAdapter, childPanel, (String) null);	topFrame.addCurrentAdapter(topAdapter, childPanel);
	//topFrame.newAdapter(topAdapter, childPanel);	return topAdapter;
  }    
  //public static uiFrame generateUIScrollPane(uiFrame topFrame,  public static ObjectAdapter generateUIScrollPane(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, int direction, String title) {
 	VirtualContainer childPanel = topFrame.createNewChildPanelInNewScrollPane(direction);	 //Container childPanel = topFrame.newContainer(direction);
	 //topFrame.showMainPanel();
	//return generateInUIPanel(topFrame,	obj, lman, sourceAdapter, childPanel, title);	ObjectAdapter topAdapter = generateInUIPanel(topFrame,	obj, lman, sourceAdapter, childPanel, title);	topFrame.addCurrentAdapter(topAdapter, childPanel);	return topAdapter;
	
  }  /*
  public static uiFrame generateUIFrame(uiFrame topFrame,
					Object obj) {	  System.out.println("uiFrame" + topFrame);
	  if (topFrame != null && topFrame.getOriginalAdapter().getObject() == null ) 	     return generateUIFrame(topFrame,	obj,null, null);	  else return generateUIFrame(obj);
  }  */
  public static uiFrame generateUIFrame(uiFrame topFrame,
					Object obj) {	  //System.out.println("uiFrame" + topFrame);
	  if (topFrame != null && topFrame.getOriginalAdapter().getObject() == null ) {		  	  generateUIFrame(topFrame,	obj,null, null);
	  return topFrame;	  } else return generateUIFrame(obj);
  }   /*
  public static uiFrame generateUIFrameOfInstance(String className) {	  return generateUIFrameOfInstance
  }
  */
   //public static uiFrame generateUIFrame(uiFrame topFrame,   public static ObjectAdapter generateUIFrame(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter) {	   return generateUIFrame(topFrame,obj, lman, sourceAdapter, null);
   }  
  //public static uiFrame generateUIFrame(uiFrame topFrame,   public static ObjectAdapter generateUIFrame(uiFrame topFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter, String title) {
   	  
    //topFrame.getChildPanel().removeAll();	/*	// First clean up the current frame
    System.out.println("Beginning frame generation...");
    init();		  
    topFrame.getChildPanel().removeAll();	//topFrame.createNewChildPanel();	//Container childPanel = topFrame.createNewChildPanelInNewScrollPane();
		  */	  // let us remove this also and see if set works	   VirtualContainer childPanel = null;
	  //Container childPanel = topFrame.createNewChildPanel();	  //System.out.println("child panel " + childPanel);
	ObjectAdapter topAdapter = topAddChildComponents(topFrame, obj, lman, sourceAdapter, childPanel, title);	  /*
    lockManager = lman;
    
	//addUIFrameToolBarButtons(topFrame);	
    uiObjectAdapter topAdapter = new uiClassAdapter();
    //linkAdapterToComponent(topAdapter, topFrame.getChildPanel());	linkAdapterToComponent(topAdapter, childPanel);
    //topAdapter = uiAddComponents(topFrame.getChildPanel(), // parent widget 	topAdapter = uiAddComponents(childPanel, // parent widget 
				 topAdapter,               // parent adaptor
				 obj,                      // object
				 obj.getClass(),           // object's class
				 (-1),                     // position 
				 "root",                   // name of object
				 null,                     // parent object
		
    if (topAdapter instanceof uiClassAdapter &&
	topAdapter.getWidgetAdapter().getUIComponent() == null) {
      //topAdapter.getWidgetAdapter().setUIComponent(topFrame.getChildPanel());		topAdapter.getWidgetAdapter().setUIComponent(childPanel);
    } 	
    topFrame.setAdapter(topAdapter);
    if (topAdapter.getPropertyClass() == null)
      topAdapter.setPropertyClass(obj.getClass());	*/
    // Add the methods.	topFrame.setAdapter(topAdapter);    initializeClassDescriptorCache();    //addPredefinedMenuModels(topFrame);        addMenuObjects(topFrame, obj);
    //uiAddMethods(topFrame, obj);    //addPredefinedMenuModels(topFrame);
    uiAddConstants(topFrame, obj);
//    addHelperObjects(topFrame, obj);
	topFrame.setSize();	//System.out.println("added helper");
	//topFrame.debugScroll(topAdapter);
	/*	if (!uiFrame.isSavable(topAdapter))
    //if (!(obj instanceof java.io.Serializable))
      topFrame.setSaveMenuItemEnabled(false);
    // Set the frame title
    String title = (String)topAdapter.getAttributeValue(AttributeNames.TITLE);
    if (title == null) {
      // Check the class attributes
      try {
	title = (String) AttributeManager.getEnvironment().getAttribute(obj.getClass().getName(), AttributeNames.TITLE).getValue();
      } catch (Exception e) {
	title = ClassDescriptor.getMethodsMenuName(obj.getClass());
      }
    }
    topFrame.setTitle(title);
	*/	//topFrame.addUIFrameToolBarButton("Elide", null);	/*
    System.out.println("Done frame generation");
	System.out.println("Beginning deep elide");	topFrame.deepElide(topAdapter);
	System.out.println("Ending deep elide");	*/
    //return topFrame;	return topAdapter;
  }// comp.	//need to create a new object for widgets in uigen since it's static	//maybe put this inside the end of generateUIFrame.	//since the method that creates one widget is called from several places.	public static void resetWidgets() {		if (currentObject != null) {  //current object stores the current one that we're working on			//it is assigned in each terminal generateUIframe (the ones that dont call others)						objStateWidgets.put(currentObject, genericW);			genericW = new Vector();		}	}	/*	public static void oldResetCommands() {   //looks like this never needs to be called b/c i'm updating it already within the code that uses it		if (currentObject != null) {  //current object stores the current one that we're working on			//it is assigned in each terminal generateUIframe (the ones that dont call others)					    //the vector is already put inside the hashtable somewhere below			commandList = new Vector();			methodHash = new Hashtable();		}	}	*/				//changed the above to Adapters for remapping need more than just the widgets.	public static void resetAdapters(Object currentObject) {		if (currentObject != null) {  //current object stores the current one that we're working on			//it is assigned in each terminal generateUIframe (the ones that dont call others)						objsPrims.put(currentObject, primAdapters);			pNameTypeString.put(currentObject, pString);			typeCount.put(currentObject, typeCnt);						primAdapters = new Vector();			pString =  new Hashtable();			typeCnt = new int[3];		}	}//	changed the above to Adapters for remapping need more than just the widgets.	public static void resetAdapters() {		resetAdapters(currentObject);	}		public static Vector getCommands(Object theObject) {  //returns the objects commands		return AMethodProcessor.getCommands(theObject);		//return (Vector)objCommands.get(theObject);  	}		public static Hashtable getObjMethods(Object theObject) {  //returns the hashtable of this objs methods as strings		return AMethodProcessor.getObjMethods(theObject);		//return (Hashtable)methodString.get(theObject);	}	public static Hashtable getObjProps(Object theObject) {		return (Hashtable)pNameTypeString.get(theObject); 	}//	comp.	//newadds	//static Vector commandList = new Vector();	//static Hashtable methodHash = new Hashtable();		public static Vector genericW = new Vector();  //holds the generic widgets for one object...reset each new object is being worked on		public static Vector primAdapters = new Vector();  //holds the primitive "sub" adapters for the object...reset each new object is being worked on	public static Hashtable pString = new Hashtable();	public static int[] typeCnt = new int[3];	//comp. ends this section from above	// comp.	//have a hashtable of the objects -> their state variable so that we can get them	//after all the .edits have been called for each object.	//static Hashtable objStateWidgets = new Hashtable(); //commenting this out b/c it the instatiation gets done in														  //resetGUI components	static Hashtable objStateWidgets = null;		//static Hashtable objsPrims = new Hashtable(); //same commenting reason here 	static Hashtable objsPrims = null; //collecting adapters for retarget 		//static Hashtable objCommands = new Hashtable(); //and here	/*	static Hashtable objCommands = null;	*/		//static Hashtable methodString = new Hashtable();  //just storing a hashtable of the string form of the meths. and props	//static Hashtable methodString = null;		//static Hashtable pNameTypeString = new Hashtable();	static Hashtable pNameTypeString = null;	static Hashtable typeCount = null;		static Object currentObject = null;//	the following is the next level up to get the outside caller the list	//of widgets in the mainscroll pane  segi wants these individually.	/*	public static Component[] getMainScrollPaneComponents() {		return topFrame.getMainScrollPaneComponents();	}	*/	/*	public static Container getMainScrollPane() {		return topFrame.browser.getMainScrollPane();	}	*/		public static uiFrame getTopFrame() {		return topFrame;	}	public static void setTopFrame(uiFrame theTopFrame) {		topFrame = theTopFrame;	}				public static Vector getStateWidgets(Object theObject) {		//topFrame.remove(topFrame.getMainScrollPane());				return (Vector)objStateWidgets.get(theObject);	}		public static Vector getPrimAdapters(Object theObject) {		return (Vector)objsPrims.get(theObject);	}			public static Hashtable getAllWidgets() {  //returns the hashtable of all the object widgets.		return objStateWidgets;	}		public static Hashcodetable getAllMethodStrings() {		return AMethodProcessor.getAllMethodStrings();		//return methodString;	}	public static Hashtable getAllPNameTypeString() {		return pNameTypeString;	}	public static Hashtable getAllTypeCount() {		return typeCount;	}		public static Hashtable getAllPrimAdapters() {  //returns the hashtable of all the object primitive adatpers.		return objsPrims;	}		public static Hashcodetable getAllCommands() { //returns the hashtable of all the objects buttons		return AMethodProcessor.getAllCommands();		//return objCommands;	}		public static void resetAllGUIComponents() {		objStateWidgets = new Hashtable();		objsPrims = new Hashtable(); 		//objCommands = new Hashtable();		AMethodProcessor.resetCommands();		pNameTypeString = new Hashtable();		typeCount = new Hashtable();			}	/*	public static void resetCommands () {		objCommands = new Hashtable();	}	*/		
// F.O. addition  to support retargeting		//we want to just generate the property UI widgets that aren't shared so we need to pass in a vector of	//of the shared ones names and filter them...you'll see the sharedPs vector being passed down thru a series of	//overloaded methods from versions already in OE.  the actual filtering happends in uiclassadapter.addUICLASScomponents	//lman, sourceAdapt are null	//topframe and object can  be accessed by Component obj.	public static uiFrame generateUIProperties(uiFrame topFrame, Object obj,			myLockManager lman, ObjectAdapter sourceAdapter, String title,			Hashtable sharedProps) {		//new method derived from uigenerator.generateUIFrame that just		// generates the properties of the object w/o dealing with		//properties		currentObject = obj;		//Container childPanel = topFrame.createNewChildPanel(); //get frame from		VirtualContainer childPanel = topFrame.createNewChildPanelInNewScrollPane(); //get frame from															   // source or															   // somethin'						return generateUIProperties(topFrame, obj, lman,				sourceAdapter,  title, childPanel, null, sharedProps);		//create the topadapter for this object			}	public static uiFrame generateUIProperties(uiFrame topFrame, Object obj,			myLockManager lman, ObjectAdapter sourceAdapter, String title, VirtualContainer childPanel, VirtualContainer topPanel,			Hashtable sharedProps) {		//new method derived from uigenerator.generateUIFrame that just		// generates the properties of the object w/o dealing with		//properties		currentObject = obj;		//Container childPanel = topFrame.createNewChildPanel(); //get frame from		//Container childPanel = topFrame.createNewChildPanelInNewScrollPane(); //get frame from															   // source or															   // somethin'		ObjectAdapter topAdapter = topAddProperties(topFrame, obj, lman,				sourceAdapter, childPanel, topPanel, title, sharedProps);		//create the topadapter for this object		topFrame.setAdapter(topAdapter); //let the frame know its adapter is										 // here		//	topFrame.setSize();		return topFrame;	}	//comp.		public static ObjectAdapter topAddProperties(uiFrame topFrame,			Object obj, myLockManager lman, ObjectAdapter sourceAdapter,			VirtualContainer childPanel, VirtualContainer topPanel, String title, Hashtable sharedPs) {		//System.exit(0);		//method derived from uiGenerator.topaddchildcomponents -- goal is to		// just get the adatpers and widgets for this topObject		try {		init(); //nothing important		lockManager = lman;				//uiObjectAdapter rootAdapter = new uiClassAdapter(); //not sure again		RootAdapter rootAdapter = new RootAdapter(); //not sure again															// what classadapter															// is for		//gets class info so it can create an objectadapter for it?		ObjectAdapter topAdapter = rootAdapter.topAddProperties( topFrame,				obj,				lman, sourceAdapter, childPanel, title,  sharedPs);		//Container topPanel = null;		uiGenerator.deepProcessAttributes(topAdapter, topPanel, rootAdapter, childPanel, true, sharedPs);		//System.out.print(">UI");		if ((!textMode && IntrospectUtility.isShapeModel( ReflectUtil.toMaybeProxyTargetClass(topAdapter.computeAndMaybeSetViewObject())))) 			//|| topAdapter.getUIFrame().isEmptyMainPanel())					topAdapter.getUIFrame().emptyMainPanel();		else {		    uiGenerator.deepElide(topAdapter);//			topFrame.showMainPanelWithoutRefreshing();		    topFrame.showMainPanel();		}		return topAdapter;		} catch (Exception e) {			return null;		}			/*		//rootAdapter.setUIFrame(topFrame);		//Connector.linkAdapterToComponent(rootAdapter, childPanel);		//String name = "root";		//boolean isProperty = false;		//Object parentObject = null;		Class objClass;		objClass = obj.getClass();		uiObjectAdapter topAdapter = uiAddComponents(childPanel,		//topFrame.getChildPanel(), // parent widget				rootAdapter, // parent adaptor				obj, // object				// obj.getClass(), // object's class 				objClass, // object's class				(-1), // position				name, // name of object				parentObject, // parent object				isProperty); // property?		if (title != null) {			topAdapter.setLocalAttribute(new Attribute(AttributeNames.TITLE,					title));		}		if (sourceAdapter != null) {			topAdapter.setSourceAdapter(sourceAdapter);			topAdapter.setRealObject(sourceAdapter.getRealObject());			topAdapter.setViewObject(sourceAdapter.getViewObject());			topAdapter.setAdapterField(sourceAdapter.getAdapterField());			if (topAdapter.getAdapterType() == uiObjectAdapter.PROPERTY_TYPE) {				topAdapter.setPropertyReadMethod(sourceAdapter						.getPropertyReadMethod());				topAdapter.setPropertyWriteMethod(sourceAdapter						.getPropertyWriteMethod());				topAdapter.setValue(sourceAdapter.getValue());				System.out.println("real obj in uigenerator"						+ topAdapter.getRealObject());			} else {				//keeping this here b/c it complex to take out (stuff for				// vectors ---will probably need later				if (sourceAdapter.getParentAdapter() instanceof uiVectorAdapter) {					topAdapter.setAdapterIndex(((uiVectorAdapter) sourceAdapter							.getParentAdapter())							.getChildAdapterIndex(sourceAdapter));					// In these cases, we'll have to perform a					// vector.setElementAt().				} else if (sourceAdapter.getParentAdapter() instanceof uiHashtableAdapter) {					topAdapter							.setAdapterIndex(((uiHashtableAdapter) sourceAdapter									.getParentAdapter())									.getChildAdapterIndex(sourceAdapter));					topAdapter.setAdapterType(sourceAdapter.getAdapterType());					topAdapter.setKey(sourceAdapter.getKey());					// In these cases, we'll have to perform a					// vector.setElementAt().				}			}			//}		}		if (topAdapter instanceof uiClassAdapter				&& topAdapter.getWidgetAdapter().getUIComponent() == null) {			topAdapter.getWidgetAdapter().setUIComponent(childPanel);		}		if (topAdapter.getPropertyClass() == null)			topAdapter.setPropertyClass(obj.getClass());		if (sourceAdapter != null) {			topAdapter.getGenericWidget().setLabel(					sourceAdapter.getBeautifiedPath());			topAdapter.setEdited(sourceAdapter.isEdited());			if (topAdapter.isEdited()) {				uiGenericWidget genWidget = topAdapter.getGenericWidget();				genWidget.setEdited();			}		}				 if (!textMode &&		  uiBean.isShapeModel(topAdapter.getViewObject().getClass()))		  topAdapter.getUIFrame().emptyMainPanel(); 		 else		 topFrame.deepElide(topAdapter, sharedPs);		//LET ME EXPAND		return topAdapter;		//return topFrame;		*/	}//end method		// Retargeting stuff stops	//end comp.	/*
  public static void registerViewMethod(String className, 
					String methodName) {
    viewMethods.put(className, methodName);
  }  */

  
  public static Object computeViewObject(CompositeAdapter parentAdapter, Object object, boolean textMode) {	  return GenerateViewObject.getViewObject(parentAdapter, object, textMode, null);  }

  // Check to see if the object has declared a view
  // by implementing a method of the signature
  // public Object uigenView(void)
  // If so, invoke this method and return the viewobject
  // Otherwise return the original object
  public static Object getViewObject(Object object, boolean textMode) {	  return GenerateViewObject.getViewObject(object, textMode);	  /*
	  if (object == null) {		  //System.out.println ("returning null for view object");
			return null;	  }
    Class c = object.getClass();
    String viewMethodName = (String) viewMethods.get(c.getName());	//if (uiBean.isPoint(object))				   //return uiBean.toPointModel(object);	
	//ShapeModel shape = uiBean.toShapeModel(object);    RemoteShape shape = null;    if (!textMode)    	 shape = uiBean.toShapeModel(object);
	if (!textMode && shape != null)
		return shape;
    else if (viewMethodName == null) 
      viewMethodName = "uigenView";
	try {
		Method viewMethod = c.getDeclaredMethod(viewMethodName,
												null);
		if (viewMethod != null) {			//System.out.println("found" + viewMethod);
			Object viewObject = viewMethod.invoke(object, null);			//System.out.println("uigen view" + viewObject);
			return getViewObject (viewObject, textMode);
		} 					   
	} catch (Exception e) {		//ssem.out.println("try failed for view method" + e);
		//return object;
	}
	return object;	*/
	/*	
	//System.out.println("no view method");
	if (object instanceof Vector) return object;	if (object instanceof Hashtable) return object;
	if (isPrimitiveClass(c)) return object;	ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(c);
	PropertyDescriptor properties[] = cdesc.getPropertyDescriptors();	FieldDescriptor reflectedFields[] = cdesc.getFieldDescriptors();	if (reflectedFields.length >  1) return object;
	Vector propertyValues = propertyValues(properties, object);
	if (reflectedFields.length + propertyValues.size() != 1) return object;
	if (reflectedFields.length == 1) {
		try {
			return reflectedFields[0].getField().get(object);
		} catch (Exception e) {
			return object;
		}	} else {		//System.out.println("returning singleton property");
		return propertyValues.elementAt(0);
	}
	*/	
    
  }  public static Object computeViewObject(CompositeAdapter parentAdapter, Object  object, String property) {	  return GenerateViewObject.getViewObject(parentAdapter, object, property);  }  public static Object computeViewObject(Object object) {	  return GenerateViewObject.getViewObject(object);	  /*
	  if (object == null) {		  //System.out.println ("returning null for view object");
			return null;	  }
    Class c = object.getClass();
    String viewMethodName = (String) viewMethods.get(c.getName());	//if (uiBean.isPoint(object))				   //return uiBean.toPointModel(object);
    if (viewMethodName == null) 
      viewMethodName = "uigenView";
	try {
		Method viewMethod = c.getDeclaredMethod(viewMethodName,
												null);
		if (viewMethod != null) {			//System.out.println("found" + viewMethod);
			Object viewObject = viewMethod.invoke(object, null);			//System.out.println("uigen view" + viewObject);
			return getViewObject (viewObject, textMode);
		} 					   
	} catch (Exception e) {		//ssem.out.println("try failed for view method" + e);
		//return object;
	}
	return object;	*/
	/*	
	//System.out.println("no view method");
	if (object instanceof Vector) return object;	if (object instanceof Hashtable) return object;
	if (isPrimitiveClass(c)) return object;	ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(c);
	PropertyDescriptor properties[] = cdesc.getPropertyDescriptors();	FieldDescriptor reflectedFields[] = cdesc.getFieldDescriptors();	if (reflectedFields.length >  1) return object;
	Vector propertyValues = propertyValues(properties, object);
	if (reflectedFields.length + propertyValues.size() != 1) return object;
	if (reflectedFields.length == 1) {
		try {
			return reflectedFields[0].getField().get(object);
		} catch (Exception e) {
			return object;
		}	} else {		//System.out.println("returning singleton property");
		return propertyValues.elementAt(0);
	}
	*/	
    
  }



  // Check if a class is a "primitive" class
  // ie one of int, float, boolean, String, etc
  // Return true if it is and false if it isnt
  public static boolean isPrimitiveClass(ClassProxy myClass) {
    return primitives.isPrimitiveClass(myClass.getName());
  }
/*
  // Link a bound property to an adaptor
  // This is done by registering the adaptor as
  // a PropertyListener of the object that contains the property
  // We first check to see if there is a property specific registration
  // method. If this doesnt exist we use the generic registration
  // method.
  public static void linkPropertyToAdapter(Object parentObject, 
										   String propertyName, 
										   uiObjectAdapter adaptor) {
	  // If the property is a bound property
	  // add the adaptor as a listener to this property.
	  // The signatures we are looking for are
	  // void addFooChangedListener(PropertyChangeListener l) and
	  // void addPropertyChangeListener(PropertyChangeListener l)
	  try {		  if (propertyName == null) return;
		  Class parentClass = parentObject.getClass();
		  Class[] params = new Class[1];
		  params[0] = Class.forName("java.beans.PropertyChangeListener");
		  
		  Method addListenerMethod = null;		  
		  try {
		  char chars[] = propertyName.toCharArray();
		  chars[0] = Character.toUpperCase(chars[0]);
		  String methodName = "add"+new String(chars)+"ChangedListener";
			  addListenerMethod = parentClass.getMethod(methodName, params);
		  //} catch (NoSuchMethodException e1) {		   } catch (Exception e1) {  
			  try {
				  addListenerMethod = parentClass.getMethod("addPropertyChangeListener", params);
			  } catch (NoSuchMethodException e2) {
				  addListenerMethod = null;
			  }
		  }
		  if (addListenerMethod != null) {
			  Object[] args = {adaptor};
			  try {
				  addListenerMethod.invoke(parentObject, args);
			  } catch (Exception e) {
				  // Nothing matters any more
				  // 
				  e.printStackTrace(); 
			  }
		  }
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }  */
  

  public static void linkAdapterToComponentOld(ObjectAdapter adaptor,
					    VirtualComponent component) {
	  //Connector.linkAdapterToComponent(adaptor, component);
	  	  if (component == null) return; 
	  /*	  System.out.println ("linking");	  System.out.println("adaptor" + adaptor);	  System.out.println("component" + component);	  System.out.println("parent adaptor" + adaptor.getParentAdapter());	  System.out.println("parent component" + component.getParent());	  */	  //System.out.println("component" + component);

    if (adaptor.getUIComponent() != null) return;	 if (component instanceof WidgetAdapterInterface) {
      WidgetAdapterInterface wa = (WidgetAdapterInterface) component;
      adaptor.setWidgetAdapter(wa);
      wa.addUIComponentValueChangedListener(adaptor);
      wa.setViewObject(adaptor.computeAndMaybeSetViewObject());
    }
    
    else {
      try {
	String widgetAdapterClass = componentMapping.getDefaultAdapter(component.getClass().getName());
	if (widgetAdapterClass.equals("none"))
	  return;
	WidgetAdapterInterface wa  = (WidgetAdapterInterface) (Class.forName(widgetAdapterClass).newInstance());		wa.setUIComponent(component);
	adaptor.setWidgetAdapter(wa);
	wa.addUIComponentValueChangedListener(adaptor);
	wa.setViewObject(adaptor.computeAndMaybeSetViewObject());       
      } catch (Exception e) {
	e.printStackTrace();
      }
    }
  }
/*
  private static LocalAttributeDescriptor getLocalAttributeDescriptor(Object obj) {
    if (obj == null)
      return null;
    Class c = obj.getClass();
    Field[] fields = c.getDeclaredFields();
    LocalAttributeDescriptor desc = null;
    for (int i=0; i<fields.length; i++) {
      // if (fields[i].getType().getName().equals("bus.uigen.LocalAttributeDescriptor")) {
      if (LocalAttributeDescriptor.class.isAssignableFrom(fields[i].getType())) {
	try {
	  desc = (LocalAttributeDescriptor) fields[i].get(obj);
	} catch (Exception e) {
	}
	break;
      }
    }
    return desc;    
  }

  private static Vector getInstanceAttributes(Object obj) {
    if (obj == null)
      return null;
    LocalAttributeDescriptor desc = getLocalAttributeDescriptor(obj);
    if (desc == null)
      return null;
    else
      return CopyAttributeVector.copyVector(desc.getAttributes());
  }  */
  
  // Add components to the container widget containw
  // Make the newly instantiated adaptors children of
  // "adaptor". 
  public static ObjectAdapter createObjectAdapter(		  //Container containW, 		  ObjectAdapter adaptor, Object obj, ClassProxy cl, int posn, Object parentObject, boolean propertyFlag) {
    return createObjectAdapter(    		/*containW, */    		adaptor, obj, cl, posn, "", parentObject, propertyFlag);
  }

 
  private static void printAtts(Vector v) {
    Attribute a;
    for (int i=0; i<v.size(); i++) {
      a = (Attribute) v.elementAt(i);
      System.out.println(a.getName()+"="+a.getValue());
    }
  }  /*
  
  //
  // getAdapterAttributes()
  // Return a Vector of attributes for the specified object
  // after inspecting 4 sources
  // 1. The object class
  // 2. The parent object class
  // 3. The object instance
  // 4. the parent object instance.
  static protected void setAdapterAttributes(uiObjectAdapter adaptor,
					   Object object, 
					   Object parentObject, 
					   String fieldname) {
    Vector list;
    Attribute attrib;
    //attrib = new Attribute("preferredWidget", componentMapping.getDefaultComponent(object.getClass().getName()));
    //adaptor.setLocalAttribute(attrib);
    
    LocalAttributeDescriptor desc = getLocalAttributeDescriptor(object);
    if (desc != null) {
      desc.addLocalAttributeListener(adaptor);
      list = desc.getAttributes();
    }
    else
      list = null;
    if (list != null) {
      for (int i=0; i< list.size(); i++) {
	adaptor.setLocalAttribute((Attribute) list.elementAt(i));
      }
    }
    list = getInstanceAttributes(parentObject);
    if (list != null) {
      Vector list1 = uiObjectAdapter.getFieldAttributes(list, fieldname);
      for (int i=0; i< list1.size(); i++) {
	adaptor.setLocalAttribute((Attribute) list1.elementAt(i));
      }
    }	
  }  */


  //
  // add components representing the specified object. The parameters are
  // contiainW      The container widget to which the components are added
  // adaptor        The parent adaptor to which child adaptors will be added
  // obj            The object to be represented
  // posn           Position of the component in the container
  // name           Name of this object (field name/property name)
  // parentObject   The object that contains this object
  // propertyFlag   Is this object a property?

  //
  // add components representing the specified object. The parameters are
  // contiainW      The container widget to which the components are added
  // adaptor        The parent adaptor to which child adaptors will be added
  // obj            The object to be represented
  // posn           Position of the component in the container
  // name           Name of this object (field name/property name)
  // parentObject   The object that contains this object
  // propertyFlag   Is this object a property?
  //static int shapeNum = 0;  public static void addToDrawing(ObjectAdapter adapter, Object o) {
	  uiFrame frame = adapter.getUIFrame();	  if (frame != null)
	    frame.addToDrawing(adapter, o);
	  /*	  if (!(o instanceof ShapeModel)) return;	  System.out.println("shapeModel");	  ShapeModel shape = (ShapeModel) o;
	  	  uiFrame frame = adapter.getUIFrame();	  if (frame == null) return;
	  System.out.println("found ui frame" + frame);
	  SLModel drawing = frame.getDrawing();
	  drawing.put(Integer.toString(shapeNum++), shape);	  */  }  public static void addKeyWords(Object parentObject, ClassProxy inputClass, ConcreteType concreteType) {	  	  ClassProxy associatedClass = inputClass;	  	  if (GenericPrimitiveToPrimitive.isPrimitiveClassStatic(inputClass.getName()) ) {		  if (parentObject == null)			  return;		  associatedClass = ReflectUtil.toMaybeProxyTargetClass(parentObject);		  ObjectEditor.associateKeywordWithClassName(concreteType.typeKeyword(), associatedClass);		  return;	  }	  	  ObjectEditor.associateKeywordWithClassName(concreteType.typeKeyword(), associatedClass);	  ObjectEditor.associateKeywordWithClassName(concreteType.programmingPatternKeyword(), associatedClass);	  ObjectEditor.associateKeywordWithClassName(concreteType.applicationKeyword(), associatedClass);	  //ObjectEditor.associateKeywordWithClassName(concreteType.toString(), inputClass);	  /*	  if (concreteType.hasPreconditions())		  ObjectEditor.associateKeywordWithClassName(ObjectEditor.PRECONDITION_KEYWORD, inputClass);		  */	  	  if (IntrospectUtility.hasPre(inputClass)) {		  ObjectEditor.associateKeywordWithClassName(ObjectEditor.PRECONDITION_DOCUMENTATION_KEYWORD, inputClass);		  ObjectEditor.associateKeywordWithClassName(ObjectEditor.PRECONDITION_PATTERN_KEYWORD, inputClass);	  }	  if (concreteType.hasValidation())		  ObjectEditor.associateKeywordWithClassName(ObjectEditor.VALIDATION_KEYWORD, inputClass);	    }  static int numObjectsVisited = 0;  // used to be 50  static final int MAX_OBJECTS_VISITED = 0;  public static int getNumObjectsVisited() {	  return numObjectsVisited;  }  public static  ObjectAdapter createObjectAdapter (		  								/*Container containW,*/
										   Object viewObject, 											  Object realObject, 											  Object parentObject, 											  int posn,											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) {  			   	  //Object viewObject = uiGenerator.getViewObject(obj1);	  /*	  if (obj == null)		  return (new PrimitiveAdapterFactory()).createObjectAdapter(null, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode);
	  */	  	  if (adaptor.getUIFrame().isOnlyGraphicsPanel &&			  			  adaptor.isShapeSpecificationRequired() ) {		  util.annotations.IsCompositeShape isCompositeShapeAnnotation = (util.annotations.IsCompositeShape) inputClass					.getAnnotation(util.annotations.IsCompositeShape.class);		  ConcreteTypeFactory aConcreteTypefatcory = ConcreteTypeRegistry.getConcreteTypeFactoory(inputClass,viewObject);		  boolean isAtomicShape = aConcreteTypefatcory instanceof ConcreteShapeFactory;		  boolean isCompositeShape = isCompositeShapeAnnotation != null && isCompositeShapeAnnotation.value();		  		 //		  boolean isCompositeShape = adaptor.isCompositeShape();//		  boolean isAtomicShape = concreteType instanceof ConcreteShape;//		  //		  if (!isAtomicShape && (isCompositeShape == null || !isCompositeShape.value()))//			  return null;		  if (!isCompositeShape  && !isAtomicShape){			  return null;		  }	  }	  ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(inputClass, viewObject, adaptor.getUIFrame());	  if (adaptor.getUIFrame().isOnlyGraphicsPanel && concreteType instanceof ConcretePrimitive) {		  return null;	  }
	  if (concreteType == null) return null;	  //	  if (adaptor.getUIFrame().isOnlyGraphicsPanel &&			  //			  adaptor.isCompositeShapeRequired()//			  && !adaptor.isCompositeShape())//		  return null;
	  ObjectAdapter retVal = ObjectAdapterRegistry.createObjectAdapter(concreteType, 			  						/*containW,*/ 			  						viewObject, 
									realObject, 									parentObject, 									posn,									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);	  	  addKeyWords(parentObject, inputClass, concreteType);	  //if (parentObject != null)	  /*	  Class associatedClass = inputClass;	  if (GenericPrimitiveToPrimitive.isPrimitiveClassStatic(inputClass.getName()) && parentObject != null) {		  associatedClass = parentObject.getClass();	  }	  */	  /*	  ObjectEditor.associateKeywordWithClassName(concreteType.programmingPatternKeyword(), inputClass);	  ObjectEditor.associateKeywordWithClassName(concreteType.typeKeyword(), inputClass);	  ObjectEditor.associateKeywordWithClassName(concreteType.applicationKeyword(), inputClass);	  //ObjectEditor.associateKeywordWithClassName(concreteType.toString(), inputClass);	 	  if (uiBean.hasPre(inputClass))		  ObjectEditor.associateKeywordWithClassName(ObjectEditor.PRECONDITION_KEYWORD, inputClass);	  if (concreteType.hasValidation())		  ObjectEditor.associateKeywordWithClassName(ObjectEditor.VALIDATION_KEYWORD, inputClass);	  */	  	  /*
	  ObjectAdapterFactory adapterFactory = ObjectAdapterRegistry.get(concreteType);
	  if (adapterFactory == null) return null;
	  uiObjectAdapter retVal = adapterFactory.createObjectAdapter (containW, obj, 
									obj1, 									parentObject, 									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);	  if (retVal == null) return null;
	  retVal.setConcreteObject(concreteType);	  */	  	  //maybe this one should be deleted and the caller one kept, that is what I am doing//	  numObjectsVisited++;//	  if (uiGenerator.hasObjectBeenVisited(retVal.getObject()) && numObjectsVisited > MAX_OBJECTS_VISITED ) {//	  //if (uiGenerator.hasObjectBeenVisited(retVal.getObject())) {//	  //if (adaptor != null && adaptor.getTopAdapter() != null && adaptor.getTopAdapter().hasObjectBeenVisited(retVal.getObject())) {//		  //		  retVal.setIsRecursive(true);//		  // actually, currently it is hidden//		  if (retVal.getGenericWidget() != null) {//			  retVal.getGenericWidget().elide();//		  }//		  //return retVal;//	  } else//		uiGenerator.addVisitedObject(retVal.getObject());
	  return retVal;
	}  public static ObjectAdapter createObjectAdapter(		  /*Container containW,*/ 		  ObjectAdapter adaptor, Object obj1, ClassProxy cl, int posn, String name, Object parentObject, boolean propertyFlag) {	  return createGraphObjectAdapter(adaptor, obj1, cl, posn, name, parentObject, propertyFlag, null);  }  public static boolean visitObject(		  ObjectAdapter adaptor, Object realObject, Object viewObject, ClassProxy realClass, int posn, String name, Object parentObject, boolean propertyFlag, Boolean theTextMode, ObjectAdapter childAdapter) {	  numObjectsVisited++;	  Set<String> previousReferences = previousReferences(adaptor, realObject);	  String objectPath = adaptor.toReferencePath(name, posn);//	  System.out.println("Visiting object:" + objectPath);	  if (previousReferences != null && previousReferences.contains(childAdapter)) {		  Tracer.error("Internal Error: revisited object adapter:" + childAdapter.getPath());		  return false;	  }	  if (previousReferences != null && /*!previousReferences.contains(objectPath) &&*/ numObjectsVisited > MAX_OBJECTS_VISITED ) {		  // let bad things happen//		  adaptor.setIsRecursive(true);		  // actually, currently it is hidden		  if (adaptor.getGenericWidget() != null) {			  adaptor.getGenericWidget().elide();		  }//		  previousReferences.add(toPath(adaptor, name, posn));		  previousReferences.add(objectPath);//		  previousReferences.add(childAdapter);		  childAdapter.setPosition(posn); // so that references are properly put//		  Message.error(" Object displayed multiple times: " + realObject + ". This time as component: " + name + " at position " + posn + " of parent object: " + parentObject + " whose path is:" + adaptor.getPath() + ". Bad things can happen now.");		  EditOfNonTree.newCase(previousReferences, realObject, uiGenerator.class);		  adaptor.notTree();		  return false;//		  Tracer.error("Object:"  + realObject + "displayed multiple times with following references:\n " + previousReferences + "\n Remove one of these references in the displayed logical structure by, for instance, renaming the getter for the property storing the shared object or adding the @util.annotations.Visible(false) annotation before the getter");		  } else {		  	uiGenerator.addVisitedObject(viewObject, parentObject, name, posn, adaptor, childAdapter);		  	return true;	  }	    }  //  public static boolean createOrReturnPreviousObjectAdapter(//		  ObjectAdapter adaptor, Object realObject, Object viewObject, ClassProxy realClass, int posn, String name, Object parentObject, boolean propertyFlag, Boolean theTextMode, ObjectAdapter childAdapter) {////	  numObjectsVisited++;//	  Set<ObjectAdapter> previousReferences = previousReferences(realObject);//	  Set<String> retVal;//	  //	  if (previousReferences == null) {//		  	uiGenerator.addVisitedObject(viewObject, parentObject, name, posn, adaptor, childAdapter);//		  	//	  }//	  String objectPath = toPath(adaptor, name, posn);//	  if (previousReferences != null && previousReferences.contains(objectPath)) {//		  Tracer.error("Internal Error: revisited object adapter:" + objectPath);//		  return false;//	  }//	  if (previousReferences != null && /*!previousReferences.contains(objectPath) &&*/ numObjectsVisited > MAX_OBJECTS_VISITED ) {//////		  // let bad things happen////		  adaptor.setIsRecursive(true);//		  // actually, currently it is hidden//		  if (adaptor.getGenericWidget() != null) {//			  adaptor.getGenericWidget().elide();//		  }////		  previousReferences.add(toPath(adaptor, name, posn));//		  previousReferences.add(objectPath);//////		  Message.error(" Object displayed multiple times: " + realObject + ". This time as component: " + name + " at position " + posn + " of parent object: " + parentObject + " whose path is:" + adaptor.getPath() + ". Bad things can happen now.");//		  EditOfNonTree.newCase(previousReferences, realObject, uiGenerator.class);//		  return false;////		  Tracer.error("Object:"  + realObject + "displayed multiple times with following references:\n " + previousReferences + "\n Remove one of these references in the displayed logical structure by, for instance, renaming the getter for the property storing the shared object or adding the @util.annotations.Visible(false) annotation before the getter");////	//	  } else {////		  	uiGenerator.addVisitedObject(viewObject, parentObject, name, posn, adaptor, childAdapter);//		  	return true;//	  }//	  //  }  static ReferenceAdapterFactory referenceAdapterFactory = new ReferenceAdapterFactory();  public static ObjectAdapter createGraphObjectAdapter(		  ObjectAdapter adaptor, Object realObject, ClassProxy realClass, int posn, String name, Object parentObject, boolean propertyFlag, Boolean theTextMode) {	 // this stuff is being checked in called createObjectAdapter, but for some reason I added it earlier//	  // adding the recursive stuff to deal with call from VectorAdapter	  boolean computedTextMode;	    if (theTextMode != null)	    	//textMode = theTextMode;	    	computedTextMode = theTextMode;	    else	    	// strange, if it has shape ancestor it should not be text mode I would assume	       computedTextMode = textMode || adaptor.hasShapeAncestor() || adaptor.getUIFrame().hasOnlyTreeManualContainer() ;	    	  Set<String> previousReferences = previousReferences(adaptor, realObject);	  ObjectAdapter retVal = null;	  if (previousReferences != null && previousReferences.size() > 0) {		  		  ObjectAdapter referentAdapter = adaptor.getRootAdapter().getBasicObjectRegistery().getObjectAdapter(realObject);		   String newPath = adaptor.toReferencePath(name, posn);		   if (referentAdapter.getReferencePath().equals(newPath)) {			   Tracer.info(uiGenerator.class, "Same object adapter created twice. Returning previous one");			   referentAdapter.registerAsListener();			   return referentAdapter;//			   return null;		   }		   previousReferences.add(adaptor.toReferencePath(name, posn));		   adaptor.notTree();//		   EditOfNonTree.newCase(previousReferences, realObject, uiGenerator.class);		   if (!referentAdapter.getAllowMultipleEqualReferences()) {		     ReferenceAdapter referenceAdapter = (ReferenceAdapter) referenceAdapterFactory.createObjectAdapter(null, null, realObject, parentObject, posn, name, realClass, propertyFlag, adaptor, computedTextMode);		       referenceAdapter.init(referentAdapter);//		   previousReferences.add(adaptor.toReferencePath(name, posn));//		   adaptor.notTree();		      EditOfNonTree.newCase(previousReferences, realObject, uiGenerator.class);		   return referenceAdapter;		   }//		   return null;		  //		  if (computedTextMode || !adaptor.hasOnlyGraphicsDescendents()) { // do not want to recurse in text mode//			  EditOfNonTree.newCase(previousReferences, realObject, uiGenerator.class);//			  return null;//		  } else {//			  retVal = ObjectRegistry.getObjectAdapter(realObject);//			  retVal.setIsRecursive(true);//		  }		 	  }	   	  Object viewObject = computeViewObject((CompositeAdapter) adaptor, realObject, name);		if (parentObject instanceof ACompositeLoggable) {		viewObject = LoggableRegistry.getLoggableModel(viewObject);		realObject = LoggableRegistry.getLoggableModel(realObject);		realClass = ACompositeLoggable.getTargetClass(realObject); // will this work for remote proxies?	}	realClass = ReflectUtil.toMaybeProxyTargetClass(realClass, realObject);				if(ObjectEditor.shareBeans && ObjectEditor.coupleElides)			ObjectRegistry.addObject(realObject);	  if (topFrame != null) {		  topFrame.addClassToAttributeMenu(realObject);		  topFrame.addClassToSourceMenu(realObject);	  }		ClassProxy inputClass;	if (viewObject == null) {		if (realClass != null)			inputClass = realClass;		else			inputClass = StandardProxyTypes.objectClass();	}	else {		//inputClass = RemoteSelector.getClass(viewObject);		inputClass = ACompositeLoggable.getTargetClass(viewObject);	}	inputClass = ReflectUtil.toMaybeProxyTargetClass(inputClass, viewObject); // do not know if it will compose with remote proxies    //System.out.println("Old class is "+inputClass.getName()+" new class is "+cl);    java.lang.String type = inputClass.getName();	  	           			    retVal = createObjectAdapter(viewObject, realObject, parentObject, posn, name, inputClass, propertyFlag, adaptor, computedTextMode);    if (retVal == null) {    	    	NoAdapterCreated.newCase(viewObject, inputClass, name, adaptor.isShapeSpecificationRequired(), uiGenerator.class);//		Tracer.info("NO adapter created for instance of " + inputClass);		return null;	}    if (retVal instanceof ShapeObjectAdapter && !computedTextMode && !retVal.getGraphicsView()) {//    	clearVisitedObjects();    	clearVisitedObject(retVal);    	// recreating object adapter after determining if it is graphics view from the other object adapter    	retVal = createObjectAdapter(viewObject, realObject, parentObject, posn, name, inputClass, propertyFlag, adaptor, true);    }  	uiGenerator.addVisitedObject(viewObject, parentObject, name, posn, adaptor, retVal);    //	if (!visitObject(adaptor, realObject, viewObject, realClass, posn, name, parentObject, propertyFlag, theTextMode, retVal))//		return null;        				   			             if(ObjectEditor.shareBeans && !ObjectEditor.coupleElides){	ObjectRegistry.mapObjectToAdapter(realObject,retVal);    }    if (adaptor.getUIFrame() != null)	  adaptor.getUIFrame().putObjectAdapter(realObject, retVal);	if (retVal == null) {    	NoAdapterCreated.newCase(viewObject, inputClass, name, adaptor.isShapeSpecificationRequired(), uiGenerator.class);//		System.out.println("E****NO adapter created for instance of " + inputClass);	}    return retVal;  }
  public static ObjectAdapter createTreeObjectAdapter(		  /*Container containW,*/ 		  ObjectAdapter adaptor, Object realObject, ClassProxy realClass, int posn, String name, Object parentObject, boolean propertyFlag, Boolean theTextMode) {
	 // this stuff is being checked in called createObjectAdapter, but for some reason I added it earlier//	  // adding the recursive stuff to deal with call from VectorAdapter		Object viewObject = computeViewObject((CompositeAdapter) adaptor, realObject, name);//		visitObject(adaptor, realObject, viewObject, realClass, posn, name, parentObject, propertyFlag, theTextMode, null);//	  numObjectsVisited++;//	  Set<String> previousReferences = previousReferences(realObject);//	  if (previousReferences != null && numObjectsVisited > MAX_OBJECTS_VISITED ) {//////	  if (uiGenerator.hasObjectBeenVisited(realObject) && numObjectsVisited > MAX_OBJECTS_VISITED ) {//	  //if (uiGenerator.hasObjectBeenVisited(retVal.getObject())) {//	  //if (adaptor != null && adaptor.getTopAdapter() != null && adaptor.getTopAdapter().hasObjectBeenVisited(retVal.getObject())) {//		  //		  adaptor.setIsRecursive(true);//		  // actually, currently it is hidden//		  if (adaptor.getGenericWidget() != null) {//			  adaptor.getGenericWidget().elide();//		  }//		  previousReferences.add(toPath(adaptor, name, posn));////		  Message.error(" Object displayed multiple times: " + realObject + ". This time as component: " + name + " at position " + posn + " of parent object: " + parentObject + " whose path is:" + adaptor.getPath() + ". Bad things can happen now.");//		  EditOfNonTree.newCase(previousReferences, realObject, uiGenerator.class);//		  Tracer.error("Object:"  + realObject + "displayed multiple times with following references:\n " + previousReferences + "\n Remove one of these references in the displayed logical structure by, for instance, renaming the getter for the property storing the shared object or adding the @util.annotations.Visible(false) annotation before the getter");////		  //System.exit(-1);//		  //return null;//		  //return retVal;//	  } else//		//uiGenerator.addVisitedObject(adaptor.getObject());////	  	uiGenerator.addVisitedObject(realObject, parentObject, name, posn, adaptor);//		  	uiGenerator.addVisitedObject(viewObject, parentObject, name, posn, adaptor);	  ObjectAdapter retVal = null;

	 
	  	  // start of normal processing
    //Object obj = getViewObject(obj1, textMode);//	Object viewObject = computeViewObject((CompositeAdapter) adaptor, realObject, name);	if (parentObject instanceof ACompositeLoggable) {		viewObject = LoggableRegistry.getLoggableModel(viewObject);		realObject = LoggableRegistry.getLoggableModel(realObject);		realClass = ACompositeLoggable.getTargetClass(realObject); // will this work for remote proxies?	}	realClass = ReflectUtil.toMaybeProxyTargetClass(realClass, realObject);		//Object obj = computeViewObject((uiContainerAdapter) adaptor, obj1, textMode);
			if(ObjectEditor.shareBeans && ObjectEditor.coupleElides)
			ObjectRegistry.addObject(realObject);
	  if (topFrame != null) {		  topFrame.addClassToAttributeMenu(realObject);		  topFrame.addClassToSourceMenu(realObject);	  }	//addToDrawing(adaptor, obj1);	//System.out.println(obj);
    //System.out.println("View object of "+obj1.getClass()+"is "+obj.getClass());
    	
    //Class inputClass = obj.getClass();	ClassProxy inputClass;	if (viewObject == null) {		if (realClass != null)			inputClass = realClass;		else			inputClass = StandardProxyTypes.objectClass();	}	else {		//inputClass = RemoteSelector.getClass(viewObject);		inputClass = ACompositeLoggable.getTargetClass(viewObject);	}	inputClass = ReflectUtil.toMaybeProxyTargetClass(inputClass, viewObject); // do not know if it will compose with remote proxies
    //System.out.println("Old class is "+inputClass.getName()+" new class is "+cl);
    java.lang.String type = inputClass.getName();

	
  	    boolean computedTextMode;    if (theTextMode != null)    	//textMode = theTextMode;    	computedTextMode = theTextMode;    else    //textMode = textMode || adaptor.hasShapeAncestor();     computedTextMode = textMode || adaptor.hasShapeAncestor();			    //if ((retVal = createObjectAdapter(/*containW,*/ obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode)) != null ){
    /*    if ((retVal = createObjectAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, computedTextMode)) != null ){	    	return retVal;		}		*/    retVal = createObjectAdapter(viewObject, realObject, parentObject, posn, name, inputClass, propertyFlag, adaptor, computedTextMode);    if (retVal == null) {    	NoAdapterCreated.newCase(viewObject, inputClass, name, adaptor.isShapeSpecificationRequired(), uiGenerator.class);//		System.out.println("E****NO adapter created for instance of " + inputClass);		return null;	}    if (retVal instanceof ShapeObjectAdapter && !computedTextMode && !retVal.getGraphicsView()) {    	clearVisitedObjects(adaptor.getRootAdapter());    	    	retVal = createObjectAdapter(viewObject, realObject, parentObject, posn, name, inputClass, propertyFlag, adaptor, true);    }	if (!visitObject(adaptor, realObject, viewObject, realClass, posn, name, parentObject, propertyFlag, theTextMode, retVal)) {//		retVal.setPosition(posn); // so that references are properly printed		return null;	}        
	/*	} else if ((retVal = uiHashtableAdapter.createHashtableAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode)) != null ){
		return retVal;	*/
			/*
    
      // Add any necessary components for this type.
      Class componentClass;
      uiVectorAdapter vectorAdapter = new uiVectorAdapter();
      setAdapterAttributes(vectorAdapter, obj, parentObject, name);
      
      vectorAdapter.setPropertyClass(inputClass);
      vectorAdapter.setPropertyName(name);
      if (propertyFlag) {
	vectorAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, vectorAdapter);
      }
      vectorAdapter.setParentAdapter(adaptor);	  vectorAdapter.setUIFrame(adaptor.getUIFrame());	    
      vectorAdapter.setRealObject(obj1);	  vectorAdapter.setViewObject(getViewObject(obj1));
      vectorAdapter.processAttributeList();	
      // If a custom editor has been registered 
      // we do not need to go further	  //vectorAdapter.uiAddVectorComponents();
	  //vectorAdapter.createChildren();	  
      return vectorAdapter;
    }			*/
    //else if (Hashtable.class.isAssignableFrom(inputClass)) {		//else if (uiBean.isHashtable(inputClass)) {	/*
	else if ((retVal = uiVectorAdapter.createVectorAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode)) != null) {		//retVal = createVectorAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);		return retVal;
	*/			/*
      uiHashtableAdapter hashtableAdapter = new uiHashtableAdapter();
      uiHashtableWidget  hashtableWidget  = new uiHashtableWidget();
     
      hashtableAdapter.setPropertyClass(inputClass);
      hashtableAdapter.setPropertyName(name);
      hashtableAdapter.setViewObject(obj);
     
      if (propertyFlag) {
	hashtableAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, hashtableAdapter);
      }
      
      hashtableAdapter.setParentAdapter(adaptor);	  hashtableAdapter.setUIFrame(adaptor.getUIFrame());
      
      hashtableAdapter.processAttributeList();
      hashtableAdapter.setRealObject(obj1);

      
      return hashtableAdapter;			*/		/*
    } else if (uiBean.isHashtable(inputClass)) {
			retVal = createHashtableAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);	} else if (uiBean.isVector(inputClass)) {
		retVal = createVectorAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);
		*/		
			 /*		else if (inputClass.isArray()) {
		retVal = uiArrayAdapter.createArrayAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode);
		}
		*/		/*
	} else if ((retVal = createShapeAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, whichShape)) != null) {		return retVal;		//} else if (!textMode && whichShape!= uiBean.NO_SHAPE) {
		//retVal = createShapeAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, whichShape);
		*/
		/*	} else {		retVal = uiClassAdapter.createClassAdapter(containW,obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode); 		*/
      /*		// This object is probably an instance of a user defined class
      // as it was not a Vector (later we'll add array, HashTable?)
      Class componentClass;
      uiClassAdapter classAdapter = new uiClassAdapter();
      setAdapterAttributes(classAdapter, obj, parentObject, name);
      classAdapter.setPropertyClass(inputClass);
      classAdapter.setPropertyName(name);
      if (propertyFlag) {
	classAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, classAdapter);
      }
      classAdapter.setParentAdapter((uiContainerAdapter) adaptor);
	   classAdapter.setUIFrame(adaptor.getUIFrame());
      	  
      classAdapter.setRealObject(obj1);
	  
	  classAdapter.setViewObject(obj);	  	  //System.out.println("processing attributes of adaptor for" + obj1);	  classAdapter.processAttributeList();		  	  	  //System.out.println("finished adding attributes of adaptor for" + obj1);
	  if (classAdapter.getUIComponent() == null) {
			
			String label = (String) classAdapter.getMergedAttributeValue(AttributeNames.LABEL);
			//label = getGenericWidget().getLabel();			containW.remove(classAdapter.getGenericWidget());	        //a.setTempAttributeValue(AttributeNames.LABEL, label);
		    //a.getGenericWidget().setLabel(label);
		    //a.setTempAttributeValue(AttributeNames.LABEL,label );			//Attribute a = new Attribute(AttributeNames.LABEL,label);   
			classAdapter.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));			//continue;		}	  //addToDrawing(classAdapter, obj);
	  //classAdapter.uiAddClassComponents();
	  //classAdapter.createChildren();	  
      // If a custom editor has been registered 
      // we do not need to go further	  	  
      
      retVal = classAdapter;		*/
    //}    if(ObjectEditor.shareBeans && !ObjectEditor.coupleElides){
	ObjectRegistry.mapObjectToAdapter(realObject,retVal);    }    if (adaptor.getUIFrame() != null)	  adaptor.getUIFrame().putObjectAdapter(realObject, retVal);	if (retVal == null) {    	NoAdapterCreated.newCase(viewObject,inputClass, name, adaptor.isShapeSpecificationRequired(), uiGenerator.class);
//		System.out.println("E****NO adapter created for instance of " + inputClass);	}
    return retVal;
  }
  /*  static uiPrimitiveAdapter createPrimitiveAdapter (Container containW, Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
	  if (!(obj instanceof uiPrimitive) && !isPrimitiveClass(inputClass)) 		  return null;
      Class componentClass;
      inputClass = primitiveClassList.getWrapperType(inputClass);
      uiPrimitiveAdapter primitiveAdapter = new uiPrimitiveAdapter();
      setAdapterAttributes(primitiveAdapter, obj, parentObject, name);
      primitiveAdapter.setLockManager(lockManager);
      primitiveAdapter.setPropertyClass(inputClass);
      primitiveAdapter.setPropertyName(name);
      if (obj instanceof uiPrimitive) {
	if (obj != null)
	  ((uiPrimitive) obj).addObjectValueChangedListener(primitiveAdapter);
	primitiveAdapter.setViewObject(obj);	//primitiveAdapter.setRealObject(obj);
	primitiveAdapter.setAdapterType(uiObjectAdapter.LISTENABLE_TYPE);
      }
      else {
	primitiveAdapter.setAdapterType(uiObjectAdapter.PRIMITIVE_TYPE);
      }
      if (propertyFlag) {
	primitiveAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, primitiveAdapter);
      }
      
      primitiveAdapter.setParentAdapter((uiContainerAdapter)adaptor);	  primitiveAdapter.setUIFrame(adaptor.getUIFrame());	  //this.addToDrawing(primitiveAdapter, obj);	  	  
      primitiveAdapter.processAttributeList();
      primitiveAdapter.setViewObject(obj);
      //primitiveAdapter.setViewObject(obj);
      primitiveAdapter.setRealObject(obj1);
	  //if (obj1 instanceof shapes.ShapeModel)
		  //System.out.println("found ShapeModel" + obj1.getClass());	  //System.out.println("setting value of" + primitiveAdapter);
      primitiveAdapter.setValue(obj);	  
	  uiFrame.deepElide(primitiveAdapter);	  //primitiveAdapter.processAttributeList();
      
      return primitiveAdapter;  }  */
  /*
  static uiClassAdapter createClassAdapter (Container containW, Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {	   Class componentClass;
      uiClassAdapter classAdapter = new uiClassAdapter();
      setAdapterAttributes(classAdapter, obj, parentObject, name);
      classAdapter.setPropertyClass(inputClass);
      classAdapter.setPropertyName(name);
      if (propertyFlag) {
	classAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, classAdapter);
      }
      	  
      classAdapter.setRealObject(obj1);
	  
	  classAdapter.setViewObject(obj);	 
	  
      classAdapter.setParentAdapter((uiContainerAdapter) adaptor);
	   classAdapter.setUIFrame(adaptor.getUIFrame());	  //System.out.println("processing attributes of adaptor for" + obj1);	  classAdapter.processAttributeList();	
	  	  //System.out.println("finished adding attributes of adaptor for" + obj1);
	  if (classAdapter.getUIComponent() == null) {
			
			String label = (String) classAdapter.getMergedAttributeValue(AttributeNames.LABEL);
			//label = getGenericWidget().getLabel();			containW.remove(classAdapter.getGenericWidget());	        //a.setTempAttributeValue(AttributeNames.LABEL, label);
		    //a.getGenericWidget().setLabel(label);
		    //a.setTempAttributeValue(AttributeNames.LABEL,label );			//Attribute a = new Attribute(AttributeNames.LABEL,label);   
			classAdapter.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));			//continue;		}	  //addToDrawing(classAdapter, obj);
	  //classAdapter.uiAddClassComponents();
	  //classAdapter.createChildren();	  
      // If a custom editor has been registered 
      // we do not need to go further	  	  
      
      return classAdapter;
  }
  */
  /*  static uiVectorAdapter createVectorAdapter (Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {	  if (!uiBean.isVector(inputClass)) return null;
      Class componentClass;
      uiVectorAdapter vectorAdapter = new uiVectorAdapter();
      setAdapterAttributes(vectorAdapter, obj, parentObject, name);	  
      vectorAdapter.setPropertyClass(inputClass);
	 
      vectorAdapter.setPropertyName(name);
      if (propertyFlag) {
	vectorAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, vectorAdapter);
      }
      vectorAdapter.setParentAdapter((uiContainerAdapter) adaptor);	  vectorAdapter.setUIFrame(adaptor.getUIFrame());	    
      vectorAdapter.setRealObject(obj1);	  vectorAdapter.setViewObject(getViewObject(obj1, textMode));
      vectorAdapter.processAttributeList();	
      // If a custom editor has been registered 
      // we do not need to go further	  //vectorAdapter.uiAddVectorComponents();
	  //vectorAdapter.createChildren();	  
      return vectorAdapter;  }  */  /*
  static uiObjectAdapter createArrayAdapter (Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
      Class componentClass;
      uiArrayAdapter arrayAdapter = new uiArrayAdapter();
      setAdapterAttributes(arrayAdapter, obj, parentObject, name);
      arrayAdapter.setPropertyClass(inputClass);
      arrayAdapter.setPropertyName(name);
      if (propertyFlag) {
	arrayAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, arrayAdapter);
      }
      arrayAdapter.setParentAdapter((uiContainerAdapter) adaptor);	  arrayAdapter.setUIFrame(adaptor.getUIFrame());	    
      arrayAdapter.setRealObject(obj1);	  arrayAdapter.setViewObject(getViewObject(obj1, textMode));
      arrayAdapter.processAttributeList();	
      // If a custom editor has been registered 
      // we do not need to go further	  //vectorAdapter.uiAddVectorComponents();
	  //vectorAdapter.createChildren();	  
      return arrayAdapter;  }
  */
  /*   static uiHashtableAdapter createHashtableAdapter (Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
	  if (!Hashtable.class.isAssignableFrom(inputClass) && !uiBean.isHashtable(inputClass)) return null;	  uiHashtableAdapter hashtableAdapter = new uiHashtableAdapter();
      uiHashtableWidget  hashtableWidget  = new uiHashtableWidget();
     
      hashtableAdapter.setPropertyClass(inputClass);	  hashtableAdapter.setPutMethod(uiBean.getPutMethod(inputClass));	  hashtableAdapter.setRemoveMethod(uiBean.getRemoveMethod(inputClass));
      hashtableAdapter.setPropertyName(name);
      hashtableAdapter.setViewObject(obj);
     
      if (propertyFlag) {
		hashtableAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
		linkPropertyToAdapter(parentObject, name, hashtableAdapter);
      }
      
      hashtableAdapter.setParentAdapter((uiContainerAdapter)adaptor);	  hashtableAdapter.setUIFrame(adaptor.getUIFrame());
      
      hashtableAdapter.processAttributeList();
      hashtableAdapter.setRealObject(obj1);

      
      return hashtableAdapter;
        }
  */
  /*   static uiShapeAdapter createShapeAdapter (Container containW,
											 Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,											  int whichShape, boolean textMode) {	   if (textMode ||  whichShape == uiBean.NO_SHAPE) return null;		   
	   uiShapeAdapter classAdapter;	   switch (whichShape) {	   case uiBean.POINT_SHAPE: classAdapter = createPointAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;
	   case uiBean.RECTANGLE_SHAPE: classAdapter = createRectangleAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;	
	   case uiBean.OVAL_SHAPE: classAdapter = createOvalAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;	
		case uiBean.LINE_SHAPE: classAdapter = createLineAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;
		case uiBean.TEXT_SHAPE: classAdapter = createTextShapeAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;				 	   default: classAdapter = new uiShapeAdapter();
	   }
	   	  classAdapter.setRealObject(obj1);	  classAdapter.setMethods();
      classAdapter.setViewObject(obj);  
      setAdapterAttributes(classAdapter, obj, parentObject, name);
      classAdapter.setPropertyClass(inputClass);
      classAdapter.setPropertyName(name);
      if (propertyFlag) {
	classAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, classAdapter);
      }
	  // link my properties too	  uiGenerator.linkPropertyToAdapter(obj1, "", classAdapter);
      classAdapter.setParentAdapter((uiContainerAdapter)adaptor);
	  classAdapter.setUIFrame(adaptor.getUIFrame());
      	  classAdapter.processAttributeList();	  
	  if (classAdapter.getUIComponent() == null) {
			
			String label = (String) classAdapter.getMergedAttributeValue(AttributeNames.LABEL);
			//label = getGenericWidget().getLabel();			containW.remove(classAdapter.getGenericWidget());	        //a.setTempAttributeValue(AttributeNames.LABEL, label);
		    //a.getGenericWidget().setLabel(label);
		    //a.setTempAttributeValue(AttributeNames.LABEL,label );			//Attribute a = new Attribute(AttributeNames.LABEL,label);   
			classAdapter.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));			//continue;		}
	  	  
      
      return classAdapter;  }
  */
  /*   static uiPointAdapter createPointAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
      
      uiPointAdapter pointAdapter = new uiPointAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return pointAdapter;  }   static uiRectangleAdapter createRectangleAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
      
      uiRectangleAdapter rectangleAdapter = new uiRectangleAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return rectangleAdapter;  }   static uiOvalAdapter createOvalAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
      
      uiOvalAdapter ovalAdapter = new uiOvalAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return ovalAdapter;  }   static uiLineAdapter createLineAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
      
      uiLineAdapter lineAdapter = new uiLineAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return lineAdapter;  }   static uiTextShapeAdapter createTextShapeAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor) {
      
      uiTextShapeAdapter textShapeAdapter = new uiTextShapeAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return textShapeAdapter;  }  */
static boolean showVectorComponentLabels = true;
    // Add components of a vector
  // What this does is call uiAddComponents for each of the objects
  // contained in the Vector. /*
  protected static void uiAddVectorComponents(Container containW, uiContainerAdapter adaptor, Object obj) {
    
    adaptor.setViewObject(obj);	boolean foundUnlabeledComposite = false;
	int maxLabelLength = 0;	
    if (obj instanceof Vector) {
      Vector v = (Vector) obj;
      //containW.setLayout(new uiGridLayout(1, v.size()));	  	  //containW.setLayout(new uiGridLayout(1, v.size(), uiGridLayout.DEFAULT_HGAP, 0));
      boolean horizontalChildren = true;
	  boolean homogeneousVector = true;
	  Class oldElemClass = null;
	  Class curElemClass = null;	  for (int i=0; i< v.size(); i++) {
		Object obj1 = v.elementAt(i);
		uiObjectAdapter a;		if (obj1 == null)			 a = uiAddComponents(containW, adaptor, obj1, uiMethodInvocationManager.OBJECT_CLASS, i, Integer.toString(i), obj, false);		else
			  a = uiAddComponents(containW, adaptor, obj1, obj1.getClass(), i, Integer.toString(i), obj, false);
		// Add to the mapping table here
		adaptor.setChildAdapterMapping(Integer.toString(i), a);		//String label = "" + (i + 1) + ". ";		String label = "" + (i + 1);
		a.getGenericWidget().setLabel(label);		if (showVectorComponentLabels)			a.getGenericWidget().setLabelVisible(true);		else
		    a.getGenericWidget().setLabelVisible(false);		//System.out.println("Adapter" + a + " " + a.getAttributeValue(AttributeNames.LABEL));
		//maxLabelLength = Math.max(maxLabelLength, ((String) a.getAttributeValue(AttributeNames.LABEL)).length());		//maxLabelLength = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());		maxLabelLength = Math.max(maxLabelLength, label.length());		//System.out.println(maxLabelLength + " " + a.getGenericWidget().getLabel().length());
		if (obj1 != null && !(a instanceof uiPrimitiveAdapter)) {			//System.out.println("found non primitive");			
			    curElemClass = obj1.getClass();			    ViewInfo childDesc = ClassDescriptorCache.getClassDescriptor(curElemClass);								
				//System.out.println(childDesc);			
				//System.out.println(childDesc.getBeanDescriptor().getValue("direction"));				//System.out.println("vector child " + a.getMergedAttributeValue("direction"));
				if (//childDesc.getBeanDescriptor() != null &&					!a.getGenericWidget().isLabelVisible() &&
					//!AttributeNames.HORIZONTAL.equals(childDesc.getBeanDescriptor().getValue("direction"))) {
					!AttributeNames.HORIZONTAL.equals(a.getMergedAttributeValue("direction"))) {
					//!(AttributeNames.HORIZONTAL.equals(a.getDirection()))) {				foundUnlabeledComposite = true;							//System.out.println ("found composite");
				horizontalChildren = false;				} 				if (oldElemClass != null && oldElemClass != curElemClass)					homogeneousVector = false;				oldElemClass = curElemClass;
		    }
		//a.setParentAdapter(adaptor);
		  } 		//containW.setLayout(new uiGridLayout(1, v.size(), uiGridLayout.DEFAULT_HGAP, 0));  
        // why here?		//adaptor.processAttributeList();		if (horizontalChildren && homogeneousVector )
			adaptor.makeColumnTitles();
		//else	
		    //adaptor.padChildrenLabels(maxLabelLength);		ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(obj.getClass());
	   		boolean alignHorizontal = false;		//if (adaptor != topAdapter) 
			if (AttributeNames.HORIZONTAL.equals (adaptor.getMergedAttributeValue("direction")))
				alignHorizontal = true;
			else if (adaptor.getMergedAttributeValue("direction") == null ) {				uiObjectAdapter parentAdaptor = adaptor.getParentAdapter();
				if ((parentAdaptor != null) &&					(parentAdaptor instanceof uiVectorAdapter)&&					!AttributeNames.HORIZONTAL.equals(parentAdaptor.getMergedAttributeValue("direction")))						alignHorizontal = true;
				//System.out.println(" " + alignHorizontal + parentAdaptor + parentAdaptor.getMergedAttributeValue("direction"));			}			//System.out.println("" + alignHorizontal+ adaptor.getTopAdapter(adaptor).getHeight());
			if (alignHorizontal) {
				
				adaptor.processDirection(AttributeNames.HORIZONTAL);			//System.out.println("horizontal " + adaptor.getMergedAttributeValue("direction"));
			}		if (//cdesc.getBeanDescriptor() != null &&
		//AttributeNames.HORIZONTAL.equals(cdesc.getBeanDescriptor().getValue("direction")))			//AttributeNames.HORIZONTAL.equals(adaptor.getMergedAttributeValue("direction"))) 
		    //containW.setLayout(new uiGridLayout(1, v.size()));
			alignHorizontal)			containW.setLayout(new uiGridLayout(1, v.size(), uiGridLayout.DEFAULT_HGAP, 0));
		else 
		  //containW.setLayout(new uiGridLayout(features.length, 1));			
			if (foundUnlabeledComposite && v.size() > 1)						  containW.setLayout(new uiGridLayout(v.size(), 1, 0, uiGridLayout.DEFAULT_VGAP));		    else				containW.setLayout(new uiGridLayout(v.size(), 1)); 
		
			}
    	
  }*/
  
  // Add components of a user defined class
  // Use reflection to identify fields and properties
  // Invoke uiAddComponents for each of these.  // this method is probably subsumed by the one in uiClassAdapter  /*
  protected static void uiAddClassComponents(Container containW, uiContainerAdapter adaptor, Object obj) {
    
    int count = 0; // Maintains the position of the next component in the
                   // container widget.
    
    ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(obj.getClass());
      
    adaptor.setViewObject(obj);	
    String classname = obj.getClass().getName();		String realClassName = adaptor.getRealObject().getClass().getName();
	if (topFrame != null) {
      topFrame.addClassToAttributeMenu(classname);
	  topFrame.addClassToAttributeMenu(realClassName);	}	
    // Get the fields
    Class inputClass = obj.getClass();	boolean foundUnlabeledComposite = false;	int maxLabelLength = 0;

    // Unified fields and properties
    // (for ordering them)
    // 04/12/99
    FeatureDescriptor features[] = cdesc.getFeatureDescriptors();
    if (features == null)
      return;	
    for (int i=0; i<features.length; i++) {
      if (features[i] instanceof PropertyDescriptor) {
	PropertyDescriptor property = (PropertyDescriptor) features[i];
	Class propertyType = property.getPropertyType();
	Method readMethod = property.getReadMethod();
	// We dont want to reflect on the class java.lang.Class
	if (!property.getName().equals("class") 
	    && readMethod != null) { 
	  try {
	    Object pobj = readMethod.invoke(obj, null);
	    uiObjectAdapter a;
	    Class pcl = property.getPropertyType();
	    if (pobj != null) pcl = pobj.getClass();
	    a = uiAddComponents(containW, adaptor, pobj, pcl, count++, property.getName(), obj, true);
	    adaptor.setChildAdapterMapping(property.getName(), a);		//System.out.println("Adapter" + a + " " + a.getAttributeValue(AttributeNames.LABEL));
	//maxLabelLength = Math.max(maxLabelLength, ((String) a.getAttributeValue(AttributeNames.LABEL)).length());	   maxLabelLength = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());
	   //maxLabelLength = Math.max(maxLabelLength, a.getGenericWidget().getLabelComponent().getSize().width);	   //System.out.println(maxLabelLength + " " + a.getGenericWidget().getLabel().length());
	    //a.setParentAdapter(adaptor);
	    // Set the propertyFlag
	    a.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	    // Set the propertyName
	    a.setPropertyName(property.getName());
	    // Set the read/write methods
	    a.setPropertyReadMethod(property.getReadMethod());
	    a.setPropertyWriteMethod(property.getWriteMethod());		a.setPreMethods(obj.getClass());
	    
	    // Check if the property is editable
	    // and set the editable attribute to false if
	    // it isnt.
		//System.out.println ("adapter" + a + " " + a.getAttributeValue(AttributeNames.LABEL));		//System.out.println(a + a.getGenericWidget().getLabel());
		//maxLabelLength = Math.max(maxLabelLength, ((String) a.getAttributeValue(AttributeNames.LABEL)).length());
		//maxLabelLength = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());	    //System.out.println(maxLabelLength + " " +a.getGenericWidget().getLabel().length() );
		if (!(a instanceof uiPrimitiveAdapter)) {
		    ViewInfo childDesc = ClassDescriptorCache.getClassDescriptor(pcl);			//System.out.println ("non primitive adapter");
			//System.out.println(childDesc);
			//System.out.println(childDesc.getBeanDescriptor().getValue("direction"));			if (childDesc.getBeanDescriptor() != null &&				!a.getGenericWidget().isLabelVisible() &&
	//!AttributeNames.HORIZONTAL.equals(childDesc.getBeanDescriptor().getValue("direction")))
				!AttributeNames.HORIZONTAL.equals(a.getMergedAttributeValue("direction"))) 			foundUnlabeledComposite = true;				//System.out.println ("found composite");
	    }
		
	    if (property.getWriteMethod() == null) {
	      if (a instanceof uiPrimitiveAdapter) {
		((uiPrimitiveAdapter) a).getWidgetAdapter().setUIComponentUneditable();
		  }
	    }
	  } catch (Exception e) {		  System.out.println(e);		  e.printStackTrace();
	  }
	}
      }
      else if (features[i] instanceof FieldDescriptor) {
	Field aField = ((FieldDescriptor) features[i]).getField();
	if (LocalAttributeDescriptor.class.isAssignableFrom(aField.getType()))
	  continue;
	Object fobj;
	try {
          fobj = aField.get(obj);
	} catch (Exception e) {
	  System.out.println("Exception while trying to get field "+aField.getName()+" from "+inputClass.getName());
	  System.out.println("is field public = "+Modifier.isPublic(aField.getModifiers()));
	  fobj = null;
	}
	Class fcl = aField.getType();
	if (fobj != null)
	  fcl = fobj.getClass();
	// Add the components for this field.
	uiObjectAdapter a = uiAddComponents(containW, adaptor, fobj, fcl, count++, aField.getName(), obj, false);
	adaptor.setChildAdapterMapping(aField.getName(), a);	//System.out.println("Adapter" + a + " " + a.getAttributeValue(AttributeNames.LABEL));
	//maxLabelLength = Math.max(maxLabelLength, ((String) a.getAttributeValue(AttributeNames.LABEL)).length());	maxLabelLength = Math.max(maxLabelLength, a.getGenericWidget().getLabel().length());
	//maxLabelLength = Math.max(maxLabelLength, a.getGenericWidget().getLabelComponent().getSize().width));	//System.out.println(maxLabelLength + " " + a.getGenericWidget().getLabel().length());
	//a.setParentAdapter(adaptor);
	a.setAdapterField(aField);

      }
    }	
	//adaptor.padChildrenLabels(maxLabelLength);	
	boolean alignHorizontal = false;
		if (AttributeNames.HORIZONTAL.equals (adaptor.getMergedAttributeValue("direction")))
			alignHorizontal = true;
		else if ((adaptor.getMergedAttributeValue("direction") == null) ) {			uiObjectAdapter parentAdaptor = adaptor.getParentAdapter();
			if ((parentAdaptor != null) &&				(parentAdaptor instanceof uiVectorAdapter)&&				!AttributeNames.HORIZONTAL.equals(parentAdaptor.getMergedAttributeValue("direction")))					alignHorizontal = true;
			//System.out.println(" " + alignHorizontal + parentAdaptor + parentAdaptor.getMergedAttributeValue("direction"));		}		//System.out.println("" + alignHorizontal+ adaptor.getTopAdapter(adaptor).getHeight());
		if (alignHorizontal) {				Vector attributes = new Vector();
				
				adaptor.processDirection(AttributeNames.HORIZONTAL);			//System.out.println("horizontal " + adaptor.getMergedAttributeValue("direction"));
			}	
			if (//cdesc.getBeanDescriptor() != null &&
		alignHorizontal)		//AttributeNames.HORIZONTAL.equals(adaptor.getMergedAttributeValue("direction"))) 
	//AttributeNames.HORIZONTAL.equals(cdesc.getBeanDescriptor().getValue("direction")))
        //containW.setLayout(new uiGridLayout(1, features.length));		containW.setLayout(new uiGridLayout(1, features.length, uiGridLayout.DEFAULT_HGAP, 0));
    else
      //containW.setLayout(new uiGridLayout(features.length, 1));
		if (foundUnlabeledComposite && features.length > 1)					  containW.setLayout(new uiGridLayout(features.length, 1, 0, uiGridLayout.DEFAULT_VGAP));	    else			containW.setLayout(new uiGridLayout(features.length, 1)); 	
  }
*//* public static Object[] excludeMethodsArray = {"toString", "notify", "notifyAll", "wait", "equals", "hashCode", "getClass", "getClientHost", "exportObject", "unexportObject", "toStub"}; 
 public static java.util.Vector excludeMethods = arrayToVector(excludeMethodsArray); */   public static Vector arrayToVector(Object[] a) {	  Vector retVal = new Vector();	  for (int i = 0; i < a.length; i++) 		  retVal.addElement(a[i]);
	  return retVal;  }
  /*  static Object[] toolBarFrameButtonsArray = {"Save","Select Up", "Elide", "Frame", "Refresh" }; 
  static java.util.Vector toolBarFrameButtons = arrayToVector(toolBarFrameButtonsArray);
    public static void addUIFrameToolBarButtons(uiFrame topFrame) {
	  for (Enumeration e = toolBarFrameButtons.elements(); e.hasMoreElements();)
		  topFrame.addUIFrameToolBarButton((String) e.nextElement(), null);
  }  */

  // uiAddMethods
  // Add a menu item for all methods in the top level
  // object    public static void initializeClassDescriptorCache() {
//	  ClassDescriptorCache.getClassDescriptor(ObjectEditor.class);  }  /*  public static Vector addToCommandList (Vector commandList, uiFrame frame, Object object, 		  ViewInfo cdesc) {	  	  MethodDescriptor[] methods = cdesc.getMethodDescriptors();		for (int i=0; i< methods.length; i++) {  //otherwise for each method			//System.out.println("adding: " + methods[i].getName());			//Method method = methods[i].getMethod();   //get the actual metho			VirtualMethod method = VirtualMethodDescriptor.getVirtualMethod(methods[i]);			String command = method.getName();			//if (excludeMethods.contains(method.getName()))  //check if it's one that we don't care for			if (excludeMethods.contains(command))  //check if it's one that we don't care for				continue;  			//System.out.println("checking position");			Integer position = (Integer) methods[i].getValue(AttributeNames.POSITION); //get it's position (order in list i guess)												Object toolbar = methods[i].getValue(AttributeNames.TOOLBAR);  			//should it go into the toolbar						Object icon = methods[i].getValue(AttributeNames.ICON);			//System.out.println("tollbar" + toolbar);			//if (toolbar != null) {						// the idea we want to run is to see if 			//1) if is supposed to go on a toolbar			//2) if so, then have a getMenu like thing that takes 			//   the name of the Toolbar and see's if it is already there and adds it.			//   or if not then creates a new one and adds the methods button there			// 			//issues  - can you name toolbars			//			can you have multiple toolbars.			//									if (ClassDescriptorCache.toBoolean(toolbar)) {  				//if it's supposed to go on A toolbar then//	comp.				//newadds  add this command to this "object"s command list...prev. topframe.topobject but we aren't creating 				//multiple frames anymore.  if problems just use the currentObject variable.				//commandList.addElement(new Command(topFrame, object, methods[i].getDisplayName(), method));								//The following comment and  line of code address the placement of buttons on the screen					//change to pass in method descriptor instead of name...since we want placement stuff				commandList.addElement(new Command(frame, object, methods[i], method));			}				//System.out.println("Inserting " + methods[i].getDisplayName());		}	  	return commandList;	    }  public static Vector createCommandList (uiFrame frame, Object object) {  	Class objectClass;  	commandList = new Vector();	if (object == null) 		objectClass = Object.class;	else		objectClass = object.getClass();  	  	String virtualClass = uiBean.getVirtualClass(object);  	if (virtualClass != null) {  		ViewInfo vcd = ClassDescriptorCache.getVirtualClassDescriptor(virtualClass);  		addToCommandList (commandList, frame, object, vcd);  	} else {  		ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(objectClass);  	  	addToCommandList (commandList, frame, object, cdesc);  	}  	  	  	return commandList;  }  */  /*  public static boolean excludeMethod (VirtualMethod method) {  	return excludeMethods.contains(method.getName());  }  */  public static void addMenuObjects (uiFrame frame, Object object) {	  if (frame.getRootMenuGroup() == null  || frame.isDummy()			  //|| frame.getMenuObjectsAdded()			  )		  return;	  AMethodProcessor.addMenuObjects(frame, object);	  frame.setMenuObjectsAdded(true);	  /*	  uiAddMethods (frame, object, null);	  addPredefinedMenuModels(frame);	  frame.displayMenuTree();	  frame.checkPreInMenuTree();	  */	    }    public static void uiAddMethods(uiFrame frame, Object object) {	  AMethodProcessor.addMethodsToMenus (frame, object, null);  }  public static void uiAddMethods(uiFrame frame, Object object, String menu) {	  AMethodProcessor.addMethodsToMenus (frame, object, menu, true);	    }  /*  public static void addPredefinedMenuModels (uiFrame frame) {	  setPredefinedMenuAttributes(frame);	  addFileMenuModel(frame);	  	 	    }  public static void setPredefinedMenuAttributes(uiFrame frame) {	  setFileMenuAttributes(frame);	    }  public static void setFileMenuAttributes(uiFrame frame) {	  ObjectEditor.setLabel(AFileMenuModel.class, uiFrame.FILE_MENU_NAME + "'");	  AMenuDescriptor menuDescriptor = frame.getMenuDescriptor();	  menuDescriptor.setAttribute(uiFrame.FILE_MENU_NAME + "'", AttributeNames.POSITION, 0);	  ObjectEditor.setMethodDisplayName(AFileMenuModel.class, "open", "Open..");	  ObjectEditor.setMethodDisplayName(AFileMenuModel.class, "load", "Load..");	  menuDescriptor.setAttribute(uiFrame.FILE_MENU_NAME + "'" + uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_OBJECT_MENU_NAME, AttributeNames.POSITION, 0);	    }  public static void addFileMenuModel (uiFrame frame) {	  	  AFileMenuModel fileMenuModel = frame.getFileMenuModel();	  frame.addMenuObject(fileMenuModel);	  	 	    }  */
  public static void uiAddMethods(uiFrame frame, Object object, String menu, boolean addToToolbar) {	AMethodProcessor.addMethodsToMenus(frame, object, menu, addToToolbar);	 /* 	  //Class myClass = object.getClass();	//System.out.println("adding methods");	//addPredefinedMenuModels(frame);	Class objectClass;	if (object == null) 		objectClass = Object.class;	else		objectClass = object.getClass();		//ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(object.getClass());	ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(objectClass, object);	MethodDescriptor[] methods = cdesc.getMethodDescriptors();			if (methods == null) //if no methods to add to menus then stop		return;//comp.	//newadds	commandList = new Vector();	methodHash = new Hashtable();	for (int i = 0; i < methods.length; i++) { //otherwise for each method			//System.out.println("adding: " + methods[i].getName());			//Method vMethod = methods[i].getMethod(); //get the actual metho			VirtualMethod vMethod = VirtualMethodDescriptor.getVirtualMethod(methods[i]); //get the actual metho			Object isVisibleObject = methods[i].getValue(AttributeNames.VISIBLE);			if (isVisibleObject != null) {				boolean isVisible = (Boolean) isVisibleObject;				if (!isVisible)					continue;							} else {				if (uiBean.isPre(vMethod)) 					continue;			}						if (excludeMethods.contains(vMethod.getName())) //check if it's one														   // that we don't care						// for			if (excludeMethod (vMethod))				continue;						//System.out.println("checking position");			// not really using it now, but let us keep it			Integer position = (Integer) methods[i]					.getValue(AttributeNames.POSITION); //get it's position														// (order in list i														// guess)			//System.out.println("pos:" + position);			String methodMenu = menu;			if (methodMenu == null)								//menu = ClassDescriptor.getMethodMenuName(vMethod);				if (vMethod.getConstructor() != null)					methodMenu =  ClassDescriptor.getLabel (objectClass) +  uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_OBJECT_MENU_NAME;				else if (methods[i] instanceof VirtualMethodDescriptor)					methodMenu = ClassDescriptor.getMethodMenuName((VirtualMethodDescriptor)methods[i]);				else					methodMenu = ClassDescriptor.getMethodMenuName(methods[i]);			//System.out.println("menu:" + menu);			if (methodMenu == null)				continue;			if (excludeMethodCategories.contains(methodMenu))				continue;						if (methodMenu != null) { //if the intended menu parent is										// already there. then add it if desired										// to be shown				//System.out.println("menu:" + menu);				int p;				if (position == null)					p = -1;				else					p = position.intValue();				//System.out.println("beautifying");				//frame.addMethodMenuItem(menu, p,				// beautify(methods[i].getName(), methods[i].getDisplayName()),				// method);				//if (menuSetup == null)							  		  			  frame.addMethodMenuItem(object, methodMenu, p, methods[i]							.getDisplayName(), vMethod, frame.menuSetter);						}			//System.out.println("not excluded" );			if (!addToToolbar) continue;			Object toolbar = methods[i].getValue(AttributeNames.TOOLBAR);			//should it go into the toolbar			Object icon = methods[i].getValue(AttributeNames.ICON);			//System.out.println("tollbar" + toolbar);			//if (toolbar != null) {			// the idea we want to run is to see if			//1) if is supposed to go on a toolbar			//2) if so, then have a getMenu like thing that takes			//   the name of the Toolbar and see's if it is already there and adds			// it.			//   or if not then creates a new one and adds the methods button			// there			// 			//issues - can you name toolbars			//			can you have multiple toolbars.			//			if (ClassDescriptorCache.toBoolean(toolbar)) {				//if it's supposed to go on A toolbar then				//comp.				//newadds add this command to this "object"s command				// list...prev. topframe.topobject but we aren't creating				//multiple frames anymore. if problems just use the				// currentObject variable.				//commandList.addElement(new Command(topFrame, object,				// methods[i].getDisplayName(), method));				//The following comment and line of code address the placement				// of buttons on the screen				//change to pass in method descriptor instead of name...since				// we want placement stuff				commandList.addElement(new Command(topFrame, object,						methods[i], vMethod));				//System.out.println("Inserting " +				// methods[i].getDisplayName());				methodHash.put(methods[i].getDisplayName().toUpperCase()						.replaceAll("OR", "/").replaceAll("_", "").trim(),						new Integer(1)); //i'm making this take in the most										 // clean version e.g. OR --> /				String place_toolbar = (String) methods[i]						.getValue(AttributeNames.PLACE_TOOLBAR);				//get it's place toolbar name defined as one of its attributes								if (place_toolbar != null) {					if (icon == null)						frame.addToolBarButton(object, (beautify(methods[i].getName(),								methods[i].getDisplayName())), //add it.								//it's over here were we have to pass it a								// toolbar name.								null, vMethod, place_toolbar);					//System.out.println(methods[i].getName());					else						//frame.addToolBarButton(beautify(methods[i].getName(),						// methods[i].getDisplayName()),						frame.addToolBarButton(object, methods[i].getDisplayName(),								(new javax.swing.ImageIcon((String) methods[i]										.getValue(AttributeNames.ICON))),								vMethod, place_toolbar);				}//fi place_t..			}					}		//comp.		//put the commands for this object in the hashablei		if (objCommands == null) AMethodProcessor.resetCommands();		//if (objCommands != null)						objCommands.put(object, commandList);		if (methodString != null)			methodString.put(object, methodHash); //need to set this up to have												  // the method strings be the												  // most cleaned up version of												  // the method name? i.e. OR ->												  // /?		//System.out.println("adding constructors");		ConstructorDescriptor[] cons = cdesc.getConstructorDescriptors();		for (int i = 0; i < cons.length; i++) {			Constructor c = cons[i].getConstructor();			//System.out.println("adding: " + cons[i].getName());			Integer position = (Integer) cons[i]					.getValue(AttributeNames.POSITION);			String cMenu = (String) cons[i].getValue(AttributeNames.MENU_NAME);			if (cMenu != null) {				int p;				if (position == null)					p = -1;				else					p = position.intValue();				frame.addConstructorMenuItem(cMenu, p, beautify(cons[i]						.getName(), cons[i].getDisplayName()), c);			}		}	//System.out.println("adding new");	// Add zero argument constructors for all visited classes?	Enumeration classes = ClassDescriptorCache.getClasses();	while (classes.hasMoreElements()) {		Object nextElement = classes.nextElement();		if (!(nextElement instanceof Class)) continue;		//ViewInfo vinfo = ClassDescriptorCache.getClassDescriptor((Class) classes.nextElement());		ViewInfo vinfo = ClassDescriptorCache.getClassDescriptor((Class) nextElement );		ConstructorDescriptor[] cs = vinfo.getConstructorDescriptors();		for (int i=0; i<cs.length; i++) {			if ((uiFrame.NEW_OBJECT_MENU_NAME.equals(cs[i].getValue(AttributeNames.MENU_NAME)))				&& !cs[i].getDisplayName().endsWith("...")){				frame.addConstructorMenuItem(uiFrame.NEW_OBJECT_MENU_NAME, 0, beautify (cs[i].getName(), cs[i].getDisplayName()),											 //if ("File".equals(cs[i].getValue(AttributeNames.MENU_NAME))) {					//frame.addConstructorMenuItem("File", 0, beautify (cs[i].getName(), cs[i].getDisplayName()),					//frame.addConstructorMenuItem((String) cs[i].getValue(AttributeNames.MENU_NAME), 0, beautify (cs[i].getName(), cs[i].getDisplayName()), 					cs[i].getConstructor());			}		}	}	//frame.getRootMenuGroup().displayMenuTree();	//System.out.println("Adding methods over");	 * 	 */}  /*
  public static void olduiAddMethods(uiFrame frame, Object object) {
	  //Class myClass = object.getClass();
	  //System.out.println("adding methods");	  if (frame.getMenuBar() == null) return;
	  Class objectClass;	  if (object == null) 		  objectClass = Object.class;	  else
		  objectClass = object.getClass();	  
	  //ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(object.getClass());	  ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(objectClass);
	  MethodDescriptor[] methods = cdesc.getMethodDescriptors();
	  	  if (methods == null) //if no methods to add to menus then stop
		  return;	  
	  for (int i=0; i< methods.length; i++) {  //otherwise for each method		  //System.out.println("adding: " + methods[i].getName());		  
		  //Method vMethod = methods[i].getMethod();   //get the actual metho		  VirtualMethod vMethod = VirtualMethodDescriptor.getVirtualMethod(methods[i]);		  
		  		  if (excludeMethods.contains(vMethod.getName()))  //check if it's one that we don't care for			  continue;  		  //System.out.println("checking position");
		  Integer position = (Integer) methods[i].getValue(AttributeNames.POSITION); //get it's position (order in list i guess)
		  //System.out.println("pos:" + position);
		  String menu = (String) methods[i].getValue(AttributeNames.MENU_NAME);  //get it's menu name defined as one of its attributes		   
		  
		  
		  		  //System.out.println("menu:" + menu);
		  
		  if (menu == null) continue;		  if (excludeMethodCategories.contains(menu)) continue;		  //System.out.println("not excluded" );
		  		  		  		  
		  Object toolbar = methods[i].getValue(AttributeNames.TOOLBAR);  //should it go into the toolbar
		  		  Object icon = methods[i].getValue(AttributeNames.ICON);
		  //System.out.println("tollbar" + toolbar);
		  //if (toolbar != null) {
		  
		  		  
		  		  if (ClassDescriptorCache.toBoolean(toolbar)) {  //if it's supposed to go on A toolbar then
			  
			  String place_toolbar = (String) methods[i].getValue(AttributeNames.PLACE_TOOLBAR);  //get it's place toolbar name defined as one of its attributes			  			  if (place_toolbar != null) {				  
				  if (icon == null)					  frame.addToolBarButton((beautify(methods[i].getName(), methods[i].getDisplayName())),  //add it. 											 //it's over here were we have to pass it a toolbar name.
						  null,
						  vMethod, place_toolbar);  
				  //System.out.println(methods[i].getName());				  else
					  //frame.addToolBarButton(beautify(methods[i].getName(), methods[i].getDisplayName()),					  frame.addToolBarButton( methods[i].getDisplayName(),
											  (new javax.swing.ImageIcon((String) methods[i].getValue(AttributeNames.ICON))),
											  vMethod, place_toolbar);
			  }//fi place_t..			  
			  			  
		  }		  
		  if (menu != null) {  //if the intended menu parent is already there.  then add it if desired to be shown			  //System.out.println("menu:" + menu);
			  int p;
			  if (position == null) p = -1;
			  else p = position.intValue();			  //System.out.println("beautifying");
			  //frame.addMethodMenuItem(menu, p, beautify(methods[i].getName(), methods[i].getDisplayName()), method); 
			  
			  if (frame.menuSetter == null)
				  frame.addMethodMenuItem(menu, p, methods[i].getDisplayName(), vMethod); 			  else				  frame.addMethodMenuItem(menu, p, methods[i].getDisplayName(), vMethod, frame.menuSetter); 
		  }
    }	//System.out.println("adding constructors");
    ConstructorDescriptor[] cons = cdesc.getConstructorDescriptors();
    for (int i=0; i< cons.length; i++) {
      Constructor c = cons[i].getConstructor();	  //System.out.println("adding: " + cons[i].getName());
      Integer position = (Integer) cons[i].getValue(AttributeNames.POSITION);
      String menu = (String) cons[i].getValue(AttributeNames.MENU_NAME);
      if (menu != null) {
	int p;
	if (position == null) p = -1;
	else p = position.intValue();
	frame.addConstructorMenuItem(menu, p, beautify(cons[i].getName(), cons[i].getDisplayName()), c);
      }
    }	//System.out.println("adding new");
    // Add zero argument constructors for all visited classes?
    Enumeration classes = ClassDescriptorCache.getClasses();
    while (classes.hasMoreElements()) {
      ViewInfo vinfo = ClassDescriptorCache.getClassDescriptor((Class) classes.nextElement());
      ConstructorDescriptor[] cs = vinfo.getConstructorDescriptors();
      for (int i=0; i<cs.length; i++) {		if ((uiFrame.NEW_OBJECT_MENU_NAME.equals(cs[i].getValue(AttributeNames.MENU_NAME)))		    && !cs[i].getDisplayName().endsWith("...")){
	    frame.addConstructorMenuItem(uiFrame.NEW_OBJECT_MENU_NAME, 0, beautify (cs[i].getName(), cs[i].getDisplayName()),
       	//if ("File".equals(cs[i].getValue(AttributeNames.MENU_NAME))) {
	  //frame.addConstructorMenuItem("File", 0, beautify (cs[i].getName(), cs[i].getDisplayName()),		//frame.addConstructorMenuItem((String) cs[i].getValue(AttributeNames.MENU_NAME), 0, beautify (cs[i].getName(), cs[i].getDisplayName()), 
				       cs[i].getConstructor());
	}
      }
    }	//System.out.println("Adding methods over");
  }  */
  
  // uiAddConstants
  // Add a menu item for all constants (static finals) in the
  // top level object
  // The menu item causes a return effect.
  // ie closes the containing uiFrame (similar to Done)
  public static void uiAddConstants(uiFrame frame, Object object) {	  if (object == null) return;	  ConstantsMenuAdditionStarted.newCase(object, uiGenerator.class);
    ClassDescriptorInterface cdesc = ClassDescriptorCache.getClassDescriptor( ReflectUtil.toMaybeProxyTargetClass(object));
    FieldDescriptorProxy[] constants = cdesc.getConstantDescriptors();
    if (constants == null)
      return;
    for (int i=0; i<constants.length; i++) {
      try {
	Object constant = constants[i].getField().get(object);
	frame.addConstantMenuItem(Common.beautify(constants[i].getName(), constants[i].getDisplayName()), 
				  constant);
      } catch (Exception e) { 		  System.out.println(e);
		  e.printStackTrace();	  }
    }	  ConstantsMenuAdditionEnded.newCase(object, uiGenerator.class);
    /*
      frame.addConstantMenuItem(constants[i].getDisplayName(), constants[i].getField());
    }
    Class myClass = object.getClass();
    try {
      Field[] fields = myClass.getFields();
      for (int i=0; i< fields.length; i++) {
	Field field = fields[i];
	int modifiers = field.getModifiers();
	if (Modifier.isStatic(modifiers) &&
	    Modifier.isFinal(modifiers)) {
	  // Got a static final field
	  uiConstantMenuItem item = frame.addConstantMenuItem(field.getName(), field);
	}
      }
    } catch (Exception e) {
    }*/
  }
  private static Field[] getDeclaredPublicFields(Class c) {
   //System.out.println("Checking fields of "+c.getName());
   Vector publicfields = new Vector();   if (!uiFrame.appletMode)   try {
   Field[] f = c.getDeclaredFields();
   for (int i=0; i< f.length; i++) {
	if (Modifier.isPublic(((Field) f[i]).getModifiers()))  {
		publicfields.addElement(f[i]);
	        //System.out.println("Adding "+f[i]);
                }
	}   } catch (Exception e) {   	System.out.println(e);   }
   //Object[] array = publicfields.toArray();
   Field[] pf = new Field[publicfields.size()];
   for (int i=0; i<pf.length; i++)
	pf[i] = (Field) publicfields.elementAt(i);
   return pf;
  }

//  private static void addHelperObjects(uiFrame frame, Object object) {
//    if (object == null) return;//	  // 
//    ViewInfo desc = ClassDescriptorCache.getClassDescriptor(RemoteSelector.getClass(object));
//    Method helperMethod = null;
//    if (desc.getBeanDescriptor() == null)
//      return;
//    // first look for instance method
//    helperMethod = (Method) desc.getBeanDescriptor().getValue(AttributeNames.HELPER_METHOD);
//    String where = (String) desc.getBeanDescriptor().getValue(AttributeNames.HELPER_LOCN);
//    if (helperMethod == null)
//      return;
//    else {
//      // invoke this method to get the helper objects
//      // pass object 
//      Class[] params = helperMethod.getParameterTypes();
//      
//      if (params.length == 0) {
//	try {
//	  Object[] retVal = (Object[]) helperMethod.invoke(object, new Object[0]);
//	  addHelpers(frame, desc, retVal, where);
//	} catch (Exception e) {e.printStackTrace();}
//      } else if (params.length == 1 &&
//		 StandardProxyTypes.objectClass().equals(params[0]) &&
//		 Modifier.isStatic(helperMethod.getModifiers())) {
//	Object[] args = new Object[1];
//	args[0] = object;
//	try {
//	  Object[] retVal = (Object []) helperMethod.invoke(null, args);
//	  addHelpers(frame, desc, retVal, where);
//	} catch (Exception e) {}
//      } else return;
//    }
//  }
  //static String[] excludeMethodCategoriesArray = {BEAN_METHODS_MENU_NAME, "Constructors"};   /*  static String[] excludeMethodCategoriesArray = {"Constructors"}; 
  static Vector excludeMethodCategories = arrayToVector(excludeMethodCategoriesArray);  */
  
  private static void addHelpers(uiFrame frame, ClassDescriptorInterface desc, Object[] array, String where) {
    for (int i=0; i<array.length; i++) {
      String label, iconpath;
      String mywhere;
      label = (String) desc.getBeanDescriptor().getValue(AttributeNames.HELPER_LABEL+"_"+i);	  iconpath = (String) desc.getBeanDescriptor().getValue(AttributeNames.HELPER_ICON+"_"+i);
      mywhere = (String) desc.getBeanDescriptor().getValue(AttributeNames.HELPER_LOCN+"_"+i);
      javax.swing.ImageIcon icon = null;
      if (iconpath != null) icon = new javax.swing.ImageIcon(iconpath);
	  if (label == null) { // make this StringBuffer later
		  /*		    String name = array[i].getClass().getName();
			label = "" + Character.toUpperCase(name.charAt(0));
			char c;			for (int nameIndex = 1; nameIndex < name.length();nameIndex++) {				if (Character.isUpperCase( c = name.charAt(nameIndex)))					label = label + " ";				label = label + c;			}			System.out.println(label);		  */
			label = Common.beautify(array[i].getClass().getName());	  }
      if (mywhere == null)
	mywhere = where;
      if (mywhere == null ||
	  AttributeNames.TOOLBAR.equals(mywhere)) 
	frame.getToolbarManager().addUIGenToolBarButton(label, icon, array[i]);
      else 
	frame.addUIGenMenuItem(mywhere, i, label, array[i]);
    }
  }

  private static PropertyDescriptor[] getDeclaredPropertyDescriptors(BeanInfo beanInfo, ClassProxy c) {
  //System.out.println("Checking proprs of "+c.getName());
  Vector publicpds = new Vector();
  PropertyDescriptor[] pdescs = beanInfo.getPropertyDescriptors();
  for (int i=0; i<pdescs.length; i++) {
      if (pdescs[i].getReadMethod() != null &&
          pdescs[i].getReadMethod().getDeclaringClass().equals(c))	
	publicpds.addElement(pdescs[i]);
      }
  PropertyDescriptor[] array = new PropertyDescriptor[publicpds.size()];
  for (int i=0; i<publicpds.size(); i++) {
       array[i] = (PropertyDescriptor) publicpds.elementAt(i);
       }
  return array;
  }
  public static String beautifyPath (String name) {
	String retVal = "";
	char c;	for (int nameIndex = 0; nameIndex < name.length();nameIndex++) {
		c = name.charAt(nameIndex);		if (c == AttributeNames.PATH_SEPARATOR.charAt(0)) { // need to fix this to search for the string
			nameIndex++;
			retVal = retVal + AttributeNames.PATH_SEPARATOR + Character.toUpperCase(name.charAt(nameIndex));
		} else if (Character.isUpperCase(c))
			retVal = retVal + ' ' + c;		else if (Character.isDigit(c))			 retVal = retVal + (Character.getNumericValue(c) + 1); 
		else
			retVal = retVal + c;
	}	return retVal;
  }  public static boolean textMode() {	  return textMode;  }  public static void setTextMode (boolean newVal) {	  textMode = newVal;  }  public static   void deepCreateChildren (ObjectAdapter adapter, int from, int to) {	try {	//(new ProcessAttributesAdapterVisitor (adapter)).traverse();	(new CreateChildrenAdapterVisitor (adapter)).traverseChildrenRange (from, to);		} catch (Exception e) {		System.out.println(e);		//System.out.println(e.getStackTrace()[0]);		//System.out.println(uiMethodInvocationManager.toString(e.getStackTrace()));		e.printStackTrace();						}}public static   void deepCreateChildren (ObjectAdapter adapter, Hashtable sharedProps) {	try {	//(new ProcessAttributesAdapterVisitor (adapter)).traverse();	(new CreateChildrenAdapterVisitor (adapter)).traverse(uiFrame.DEEP_ELIDE_LEVEL, uiFrame.DEEP_ELIDE_NUM, sharedProps);		} catch (Exception e) {		System.out.println(e);		//System.out.println(e.getStackTrace()[0]);		//System.out.println(uiMethodInvocationManager.toString(e.getStackTrace()));		e.printStackTrace();						}}// creating parent seems also to be falsepublic static   void deepCreateChildren (ObjectAdapter adapter, boolean creatingParent ) {	try {		// everytime we add or delete parent we will clear		// for now we do not do this//		if (!creatingParent)//			(new ClearVisitedNodeAdapterVisitor(adapter)).traverseNonAtomicChildrenContainers();					(new CreateChildrenAdapterVisitor (adapter)).traverseVisibleAndInvisible(uiFrame.DEEP_ELIDE_LEVEL, uiFrame.DEEP_ELIDE_NUM);	uiGenerator.printTime("CreateChildrenAdapterVisitor");		} catch (Exception e) {		System.out.println(e);		//System.out.println(e.getStackTrace()[0]);		//System.out.println(uiMethodInvocationManager.toString(e.getStackTrace()));		e.printStackTrace();						}}public static void deepCreateWidgetShell(ObjectAdapter topAdapter) {	  WidgetShellCreationStarted.newCase(topAdapter.getRealObject(), uiGenerator.class);		if (topAdapter.isAtomic()) {			(new CreateWidgetShellAdapterVisitor (topAdapter)).visit(1,0, topAdapter);			return;		}					  (new CreateWidgetShellAdapterVisitor (topAdapter)).traverse();	  }public static void deepProcessAttributesWithDefaults(ObjectAdapter topAdapter) {	  (new ProcessAttributesWithDefaultsAdapterVisitor (topAdapter)).traverse();	  	  }public static void deepProcessSynthesizedAttributesWithDefaults(ObjectAdapter topAdapter) {(new ProcessSynthesizedAttributesWithDefaultsAdapterVisitor (topAdapter)).traversePostOrder();}public static   void deepElide (ObjectAdapter adapter, int from, int to) {	try {	//(new ProcessAttributesAdapterVisitor (adapter)).traverse();	(new ElideAdapterVisitor (adapter)).traverseRange(from, to);		} catch (Exception e) {		System.out.println(e);		//System.out.println(e.getStackTrace()[0]);		//System.out.println(uiMethodInvocationManager.toString(e.getStackTrace()));		e.printStackTrace();						}}public static   void deepRedoExpand (ObjectAdapter adapter) {	(new RedoExpandAdapterVisitor   (adapter)).traverse();}public static   void deepElide (ObjectAdapter adapter) {	if (adapter.getUIFrame().isOnlyGraphicsPanel()) return;	try {		/*		if (adapter.hasOnlyGraphicsDescendents())			return;			*/		  ElideStarted.newCase(adapter.getRealObject(), uiGenerator.class);		if (adapter.isAtomic()) {			new ElideAdapterVisitor (adapter).visit(adapter, uiFrame.DEEP_ELIDE_LEVEL, 0);			return;		}	//(new ProcessAttributesAdapterVisitor (adapter)).traverse();	(new ElideAdapterVisitor (adapter)).traverse(uiFrame.DEEP_ELIDE_LEVEL, uiFrame.DEEP_ELIDE_NUM);		} catch (Exception e) {		System.out.println(e);		//System.out.println(e.getStackTrace()[0]);		//System.out.println(uiMethodInvocationManager.toString(e.getStackTrace()));		e.printStackTrace();						}}public static ObjectAdapter getTopObjectAdapter (Object object) {	Vector<uiFrame> frames = uiFrameList.getList();	for (int i = 0; i < frames.size(); i++) {		uiFrame frame = frames.elementAt(i);		for (Enumeration adapters   = frame.refreshableAdapters().elements(); adapters.hasMoreElements();) {			//System.out.println("uiFrame: refreshing   adapter" ); 			ObjectAdapter adapter = ((ObjectAdapter) adapters.nextElement());			if (adapter.getRealObject() != object) continue;			return adapter;		}			}	return null;}public static void refreshAttributes (Object topObject, ClassProxy targetClass) {	ObjectAdapter topAdapter = getTopObjectAdapter(topObject);	if (topAdapter == null)		return;	topAdapter.refreshAttributes(targetClass);	}public static void refreshAttributes (Object topObject) {	if (topObject == null)		return;	refreshAttributes(topObject,  ReflectUtil.toMaybeProxyTargetClass(topObject));	}static {//	System.out.println(OE_VERSION_NAME +": Copyright Prasun Dewan, 2011, All rights reserved.");//	System.out.println(OE_VERSION_NAME + " " + new AnObjectEditorDescription());//	AboutPrinter.printAbout();}  
}

