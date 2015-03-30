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
		/*
		registerWidget("slm.SLModel", "java.awt.Container", "bus.uigen.editors.ShapesAdapter");
		
		registerWidget("java.lang.StringBuffer", "javax.swing.JTextArea", "bus.uigen.adapters.SBTextAreaAdapter");
		registerWidget("util.models.AMutableString", "javax.swing.JTextArea", "bus.uigen.adapters.MSTextAreaAdapter");
		registerWidget("java.net.URL", "javax.swing.JEditorPane", "bus.uigen.adapters.EditorPaneAdapter");
		registerWidget("util.models.AListenableString", "javax.swing.JTextField", "bus.uigen.adapters.MSTextFieldAdapter");
		registerWidget("util.models.ListenableString", "javax.swing.JTextField", "bus.uigen.adapters.MSTextFieldAdapter");

		registerWidget("shapes.ShapeModel", "shapes.ShapeModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.RemoteShape", "shapes.RemoteShape", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.OvalModel", "shapes.OvalModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.PointModel", "shapes.PointModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.RectangleModel", "shapes.RectangleModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.LineModel", "shapes.LineModel", "bus.uigen.editors.ShapeAdapter");	
		
		registerWidget("shapes.ComponentModel", "shapes.ComponentModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.StringModel", "shapes.StringModel", "bus.uigen.editors.ShapeAdapter");

		registerWidget("shapes.LabelModel", "shapes.LabelModel", "bus.uigen.editors.ShapeAdapter");
		// dont register this one
		//bus.uigen.editors.EditorRegistry.registerWidget("shapes.LabelModel_Stub", "shapes.LabelModel_Stub", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.TextModel", "shapes.TextModel", "bus.uigen.editors.ShapeAdapter");
		
		registerWidget("shapes.ArcModel", "shapes.ArcModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.CurveModel", "shapes.CurveModel", "bus.uigen.editors.ShapeAdapter");
		registerWidget("shapes.ImageModel", "shapes.ImageModel", "bus.uigen.editors.ShapeAdapter");
	
		registerWidget(ALabelBeanModel.class.getName(), ALabelBeanModel.class.getName(), LabelAdapter.class.getName());
		registerWidget(AWTShapeModel.class.getName(), (AWTShapeModel.class.getName()), ShapeAdapter.class.getName());
	*/
		registerWidget(slm.SLModel.class, java.awt.Container.class, bus.uigen.editors.ShapesAdapter.class);
		registerWidget(java.lang.StringBuffer.class, javax.swing.JTextArea.class, bus.uigen.adapters.SBTextAreaAdapter.class);
		registerWidget(util.models.AMutableString.class, javax.swing.JTextArea.class, bus.uigen.adapters.MSTextAreaAdapter.class);
		registerWidget(java.net.URL.class, javax.swing.JEditorPane.class, bus.uigen.adapters.EditorPaneAdapter.class);
		registerWidget(util.models.AListenableString.class, javax.swing.JTextField.class, bus.uigen.adapters.MSTextFieldAdapter.class);
		registerWidget(util.models.ListenableString.class, javax.swing.JTextField.class, bus.uigen.adapters.MSTextFieldAdapter.class);

		registerWidget(shapes.ShapeModel.class, shapes.ShapeModel.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.RemoteShape.class, shapes.RemoteShape.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.OvalModel.class, shapes.OvalModel.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.PointModel.class, shapes.PointModel.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.RectangleModel.class, shapes.RectangleModel.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.LineModel.class, shapes.LineModel.class, bus.uigen.editors.ShapeAdapter.class);	
		
		registerWidget(shapes.ComponentModel.class, shapes.ComponentModel.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.StringModel.class, shapes.StringModel.class, bus.uigen.editors.ShapeAdapter.class);

		registerWidget(shapes.LabelModel.class, shapes.LabelModel.class, bus.uigen.editors.ShapeAdapter.class);
		// dont register this one
		//bus.uigen.editors.EditorRegistry.registerWidget(shapes.LabelModel_Stub.class, shapes.LabelModel_Stub.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.TextModel.class, shapes.TextModel.class, bus.uigen.editors.ShapeAdapter.class);
		
		registerWidget(shapes.ArcModel.class, shapes.ArcModel.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.CurveModel.class, shapes.CurveModel.class, bus.uigen.editors.ShapeAdapter.class);
		registerWidget(shapes.ImageModel.class, shapes.ImageModel.class, bus.uigen.editors.ShapeAdapter.class);
	
		registerWidget(ALabelBeanModel.class, ALabelBeanModel.class, LabelAdapter.class);
		registerWidget(AWTShapeModel.class, (AWTShapeModel.class), ShapeAdapter.class);


		
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("While registering:" + e);
	}
	//editorsRegistered = true;
}
}



