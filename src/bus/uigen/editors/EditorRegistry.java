package bus.uigen.editors;

import java.util.Hashtable;

import shapes.AWTShapeModel;
import util.models.ALabelBeanModel;
import bus.uigen.ComponentDictionary;
import bus.uigen.adapters.LabelAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.shapes.AnAWTShapeModel;

public class EditorRegistry {
  private static Hashtable registry = new Hashtable(32);
  private static ComponentDictionary dict = new ComponentDictionary();

  
  public static void register(String type, String editor) throws ClassNotFoundException{
    registry.put(RemoteSelector.forName(type), RemoteSelector.forName(editor));
    dict.setComponentMapping(type, editor);
  }
  public static boolean hasWidgetClass(String type) {
  		return dict.hasWidgetClass(type);
  }
  public static boolean hasWidgetClass(ClassProxy c) {
  	return hasWidgetClass(c.getName());
  }
  
  public static void register(ClassProxy type, ClassProxy editor) {
    registry.put(type, editor);
    dict.setComponentMapping(type.getName(), editor.getName());
  }

  public static ClassProxy getEditorClass(String  type) throws ClassNotFoundException {
    return getEditorClass(RemoteSelector.forName(type));
  }

  public static ClassProxy getEditorClass(ClassProxy type) {
    return (ClassProxy) registry.get(type);
  }

  public static ComponentDictionary getComponentDictionary() {
    return dict;
  }  

  public static void registerWidget(String type, String widget, String adapter) throws ClassNotFoundException {
    if (widget != null) {
	//ClassProxy t = RemoteSelector.forName(type);
    ClassProxy t = AClassProxy.staticForName(type);
    //ClassProxy w = RemoteSelector.forName(widget);
    ClassProxy w = AClassProxy.staticForName(widget);
    // not sure why the forName was done on adapter
    //RemoteSelector.forName(adapter);
    registry.put(t, w);
    }
    dict.setComponentAndAdapterMapping(type, widget, adapter);
  }
  public static void registerWidget(ClassProxy t, ClassProxy w, ClassProxy adapter) throws ClassNotFoundException {
	   
	    //Class.forName(adapter);
	    registry.put(t, w);
	    dict.setComponentAndAdapterMapping(t.getName(), w.getName(), adapter.getName());
	  }
  public static void registerWidget(Class t, Class w, Class adapter) throws ClassNotFoundException {
	   registerWidget (AClassProxy.classProxy(t), AClassProxy.classProxy(w), AClassProxy.classProxy(adapter));
	    
	  }
  public static void registerWidgetForSelfAndStub(ClassProxy t, ClassProxy w, ClassProxy adapter) throws ClassNotFoundException {
	   registerWidget(t, w, adapter);
	    //Class.forName(adapter);
	    registerWidget(t.getName()+"_Stub", w.getName()+"_Stub", adapter.getName());
	  }
  

  public static String getDefaultWidgetAdapter(String widgettype) {
    return dict.getDefaultAdapter(widgettype);
  }  //public static String getDefaultWidgetAdapter(String wobjecttype, String componentType) {
  public static String getDefaultWidgetAdapter(ClassProxy objectClass, String componentType) {
	  //public static String getDefaultWidgetAdapter(String wobjecttype, String componentType) {
	  String wobjecttype = objectClass.getName();
		  try {
			 // ClassProxy objectClass =  RemoteSelector.forName (wobjecttype);
			  ClassProxy componentClass = null;
			  if (componentType != null)
				  //componentClass = RemoteSelector.forName(componentType);
			  	  componentClass = AClassProxy.staticForName(componentType);
			  ClassProxy retVal = dict.getDefaultAdapter(objectClass, componentClass);
			  if (retVal != null) return retVal.getName();
			  else 
				  return dict.getDefaultAdapter(wobjecttype, componentType);
		  }
		  catch (Exception e) {
	    return dict.getDefaultAdapter(wobjecttype, componentType);
		  }
	  }
  /*
  public static String getDefaultWidgetAdapter(String wobjecttype, String componentType) {
	  try {
		  ClassProxy objectClass =  RemoteSelector.forName (wobjecttype);
		  ClassProxy componentClass = null;
		  if (componentType != null)
			  //componentClass = RemoteSelector.forName(componentType);
		  	  componentClass = AClassProxy.staticForName(componentType);
		  ClassProxy retVal = dict.getDefaultAdapter(objectClass, componentClass);
		  if (retVal != null) return retVal.getName();
		  else 
			  return dict.getDefaultAdapter(wobjecttype, componentType);
	  }
	  catch (Exception e) {
    return dict.getDefaultAdapter(wobjecttype, componentType);
	  }
  }
  */  public static String getDefaultWidget (String widgettype, String adapterType) {
    return dict.getDefaultWidget(widgettype, adapterType);
  }
  static {
	  registerEditors();
  }
static void registerEditors() {
	//System.out.println("checking if editors registered");
	//if (editorsRegistered) return;
	//System.out.println("registering editors");
	try {
		//bus.uigen.editors.EditorRegistry.registerWidget("slm.SLModel", "slc.SLComposer", "bus.uigen.editors.ShapesAdapter");
		registerWidget("slm.SLModel", "java.awt.Container", "bus.uigen.editors.ShapesAdapter");
		//registerWidget("slm.SLModel", "java.awt.Container", "bus.uigen.editors.NestedShapesAdapter");
		//bus.uigen.editors.EditorRegistry.registerWidget(slm.SLModel.class, java.awt.Container.class, bus.uigen.editors.ShapesAdapter.class);
		registerWidget("java.lang.StringBuffer", "javax.swing.JTextArea", "bus.uigen.adapters.SBTextAreaAdapter");
		registerWidget("util.models.AMutableString", "javax.swing.JTextArea", "bus.uigen.adapters.MSTextAreaAdapter");
		//bus.uigen.editors.EditorRegistry.registerWidget("bus.uigen.AListenableString", "javax.swing.JTextArea", "bus.uigen.adapters.MSJTextAreaAdapter");
		registerWidget("java.net.URL", "javax.swing.JEditorPane", "bus.uigen.adapters.EditorPaneAdapter");
		registerWidget("util.models.AListenableString", "javax.swing.JTextField", "bus.uigen.adapters.MSTextFieldAdapter");
		registerWidget("util.models.ListenableString", "javax.swing.JTextField", "bus.uigen.adapters.MSTextFieldAdapter");
//		registerWidget("util.models.AListenableString", null, "bus.uigen.adapters.MSTextFieldAdapter");
//		registerWidget("util.models.ListenableString", null, "bus.uigen.adapters.MSTextFieldAdapter");

		//ObjectEditor.setPreferredWidget(AListenableHashtable.class, JTable.class);
		registerWidget("shapes.ShapeModel", "shapes.ShapeModel", "bus.uigen.editors.ShapeAdapter");
		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.ShapeModel", "shapes.ShapeModel_Stub", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.RemoteShape", "shapes.RemoteShape", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.OvalModel", "shapes.OvalModel", "bus.uigen.editors.ShapeAdapter");
		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.OvalModel_Stub", "shapes.OvalModel_Stub", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.PointModel", "shapes.PointModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.RectangleModel", "shapes.RectangleModel", "bus.uigen.editors.ShapeAdapter");
		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.RectangleModel_Stub", "shapes.RectangleModel_Stub", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.LineModel", "shapes.LineModel", "bus.uigen.editors.ShapeAdapter");	
		//registerWidget("shapes.LineModel", "shapes.LineModel", "bus.uigen.editors.ShapeAdapter");	
		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.LineModel_Stub", "shapes.LineModel_Stub", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.ComponentModel", "shapes.ComponentModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.StringModel", "shapes.StringModel", "bus.uigen.editors.ShapeAdapter");

		registerWidget("shapes.LabelModel", "shapes.LabelModel", "bus.uigen.editors.ShapeAdapter");
		// dont register this one
		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.LabelModel_Stub", "shapes.LabelModel_Stub", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.TextModel", "shapes.TextModel", "bus.uigen.editors.ShapeAdapter");
		
		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.TextModel_Stub", "shapes.TextModel_Stub", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.ArcModel", "shapes.ArcModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.CurveModel", "shapes.CurveModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.ImageModel", "shapes.ImageModel", "bus.uigen.editors.ShapeAdapter");
	
		registerWidget(ALabelBeanModel.class.getName(), ALabelBeanModel.class.getName(), LabelAdapter.class.getName());
		registerWidget(AWTShapeModel.class.getName(), (AWTShapeModel.class.getName()), ShapeAdapter.class.getName());

		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.ArcModel_Stub", "shapes.ArcModel_Stub", "bus.uigen.editors.ShapeAdapter");
		/*
		bus.uigen.editors.EditorRegistry.registerWidget(slm.SLModel.class,slc.SLComposer.class, bus.uigen.editors.ShapesAdapter.class);
		bus.uigen.editors.EditorRegistry.registerWidget("java.lang.StringBuffer", "javax.swing.JTextArea", "bus.uigen.adapters.uiSBJTextAreaAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("bus.uigen.AMutableString", "javax.swing.JTextArea", "bus.uigen.adapters.MSJTextAreaAdapter");
		//bus.uigen.editors.EditorRegistry.registerWidget("bus.uigen.AListenableString", "javax.swing.JTextArea", "bus.uigen.adapters.MSJTextAreaAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("bus.uigen.AListenableString", "javax.swing.JTextField", "bus.uigen.adapters.MSJTextFieldAdapter");
		//ObjectEditor.setPreferredWidget(AListenableHashtable.class, JTable.class);
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.ShapeModel", "shapes.ShapeModel", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.ShapeModel", "shapes.ShapeModel_Stub", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.RemoteShape", "shapes.RemoteShape", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.OvalModel", "shapes.OvalModel", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.OvalModel_Stub", "shapes.OvalModel_Stub", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.PointModel", "shapes.PointModel", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.RectangleModel", "shapes.RectangleModel", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.RectangleModel_Stub", "shapes.RectangleModel_Stub", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.LineModel", "shapes.LineModel", "bus.uigen.editors.ShapeAdapter");	
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.LineModel_Stub", "shapes.LineModel_Stub", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.ComponentModel", "shapes.ComponentModel", "bus.uigen.editors.ShapeAdapter");		
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.TextModel", "shapes.TextModel", "bus.uigen.editors.ShapeAdapter");
		bus.uigen.editors.EditorRegistry.registerWidget("shapes.TextModel_Stub", "shapes.TextModel_Stub", "bus.uigen.editors.ShapeAdapter");
		*/
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("While registering:" + e);
	}
	//editorsRegistered = true;
}
}



