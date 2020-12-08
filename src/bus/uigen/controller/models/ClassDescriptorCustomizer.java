package bus.uigen.controller.models;

import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.MethodDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.BeanDescriptor;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Vector;
import java.util.Enumeration;
import java.io.Serializable;import java.util.Hashtable;import bus.uigen.attributes.*;//import bus.uigen.controller.uiMethodInvocationManager;
//import bus.uigen.controller.menus.AMethodProcessor;
//import bus.uigen.misc.Misc;
import bus.uigen.introspect.BeanInfoWriter;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.FieldDescriptorProxy;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.PropertyDescriptorProxy;
import bus.uigen.introspect.htElement;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.viewgroups.APropertyAndCommandFilter;
import bus.uigen.*;
import bus.uigen.attributes.AnAttributeName;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;


public class ClassDescriptorCustomizer {
	AClassDescriptor cd;
	ObjectAdapter objectAdapter;
	PropertyDescriptorProxy[] properties;
	Hashtable attributes;
	MethodDescriptorProxy[] methods;
	FieldDescriptorProxy[] fields;
	
	public ClassDescriptorCustomizer (AClassDescriptor theClassDescriptor) {
		cd = theClassDescriptor;
		
		init();
		//beanFile = cd.toShortName(cd.getRealClass().getName()) + "BeanInfo.java";;
	}
	public void init() {
		attributes = cd.getAttributes();
		properties = cd.getPropertyDescriptors();
		methods = cd.getMethodDescriptors();
		fields = cd.getFieldDescriptors();
	}
	public ClassDescriptorCustomizer (AClassDescriptor theClassDescriptor, ObjectAdapter theAdapter) {
		cd = theClassDescriptor;
		objectAdapter = theAdapter;
		init();
		
		//beanFile = cd.toShortName(cd.getRealClass().getName()) + "BeanInfo.java";;
	}
/*
  //public static int MAX_
  protected PropertyDescriptor[] properties = {};
  protected MethodDescriptor[]   methods = {};
  protected FieldDescriptor[]    fields = {};
  protected ConstantDescriptor[] constants = {};
  protected ConstructorDescriptor[] constructors = {} ;
  protected Class realClass;
  protected String virtualClass; // will need to be passed in actually
  protected Object prototypeObject;
  protected boolean prototypeInitialized = false;
  protected boolean dynamicMethodsInitialized = false;
  protected boolean dynamicPropertiesInitialized = false;
  //protected boolean isShapeClass = false;  protected Vector doubleClickMethodsVector = new Vector();  protected VirtualMethod[] doubleClickMethods;
  Hashtable<String, VirtualMethodDescriptor> namesToMethodDescriptors = new Hashtable();
  
  Hashtable<String, PropertyDescriptor> namesToPropertyDescriptors = new Hashtable();

  protected FeatureDescriptor[] features = null;
  */
  private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
  /*
  private Hashtable attributes = new Hashtable();
  */
  
  public void addPropertyChangeListener(PropertyChangeListener l) {
    propertyChange.addPropertyChangeListener(l);
  }
  //String beanFile;
  void add(Vector sortedList, FeatureDescriptor f) {	  int initVal = 0;
	  if (sortedList.size() > 0 && 
		  ((FeatureDescriptor) sortedList.elementAt(0)).getName().equals("name"))
		  initVal = 1;		  
	  for (int i = initVal; i < sortedList.size(); i++ ) {		  FeatureDescriptor storedFeature = (FeatureDescriptor) sortedList.elementAt(i);
		  if (f.getName().equals("name")) {			  sortedList.insertElementAt(f, 0);
			  return;
		  }		  if (f.getName().compareTo(storedFeature.getName()) < 0) {
				  sortedList.insertElementAt(f, i);
				  return;
			  }	  }	 sortedList.addElement(f);	  }  
  MethodProxy[] virtualMethods;
 MethodDescriptor m;
 public void setValue(Object obj) {
	    attributes.put(getAttributeName(), obj);
	  }
  //private String attributeName = AttributeNames.LABEL;
 bus.uigen.attributes.AnAttributeName attributeName = new AnAttributeName(AttributeNames.LABEL);
  public void setAttributeName(AnAttributeName newVal) {
    // Go through the Vector and set each element's
    // property name to propertyName
    Vector vector = getAttributeValues();
    for (int i=0; i<vector.size(); i++) 
      ((htElement) vector.elementAt(i)).setAttributeName(newVal);
    this.attributeName = newVal;
    VectorChanged();
  }
  /*
  public PropertyDescriptor getAPD() {
	  PropertyDescriptor[] pds = cd.getPropertyDescriptors();
	  if (pds.length > 0) {
		  pds[0].setValue("name1", "value1");
		  pds[0].setValue("name 2", "value2");
		  return pds[0];
	  }
	  return null;
  }
  */
  public AnAttributeName getAttributeName() {
    return attributeName;
  }

  private boolean Fields = true, Methods = false, Bean=false;


  // Expose these booleans as controls
  public boolean getComponents() {
    return Fields;
  }

  public void setComponents(boolean b) {
    if (b) {
      Fields = b;
      Methods = Bean = false;
      methodsChanged();
      beanChanged();
      VectorChanged();
    }
  }

  public boolean getMethods() {
    return Methods;
  }

  public void setMethods(boolean b) {
    if (b) {
    Methods = b;
    Fields = Bean = false;
    fieldsChanged();
    beanChanged();
    VectorChanged();
    }
  }

  public boolean getSelf() {
    return Bean;
  }

  public void setSelf(boolean b) {
    if (b) {
      Bean = b;
      Fields = Methods = false;
      fieldsChanged();
      methodsChanged();
      VectorChanged();
    }
  }

  // Expose this vector
  // as an editable property
  public Vector getAttributeValues() {
    if (Fields)
      return FieldVector();		//return (Vector) FieldVector().clone();
    else if (Methods)
      return MethodVector();		//return (Vector) MethodVector().clone();
    else
        return BeanVector();		//return (Vector) BeanVector().clone();
  }


  private void fieldsChanged() {
    propertyChange.firePropertyChange("fields", null, 
				      new Boolean(getComponents()));
  }

  private void methodsChanged() {
    propertyChange.firePropertyChange("methods", null, 
				      new Boolean(getMethods()));
  }

  private void beanChanged() {
    propertyChange.firePropertyChange("bean", null, 
				      new Boolean(getSelf()));
  }

  private void VectorChanged() {
    propertyChange.firePropertyChange("htVector", null, 
				      getAttributeValues());
  }

  private void PropertyChanged() {
    propertyChange.firePropertyChange("attributeName", null, 
				      getAttributeName());
  }

  private Vector fieldVector = null, methodVector = null, beanVector = null;;
  private Vector FieldVector() {
    if (fieldVector == null) {
      fieldVector = new Vector();
      int i;
//      if (fields == null)
//    	  return fieldVector;
      for (i=0; i<fields.length; i++)
	fieldVector.addElement(new htElement(fields[i], getAttributeName(), cd, objectAdapter));
      for (i=0; i<properties.length; i++)		  if (!properties[i].getName().equals(""))
	fieldVector.addElement(new htElement(properties[i], getAttributeName(), cd, objectAdapter));
    }
    return fieldVector;
  }
  
  

  private Vector BeanVector() {
    if (beanVector == null) {
      beanVector = new Vector();
      beanVector.addElement(new htElement(cd.getBeanDescriptor(),
					   cd.getRealClass().getName(),
					   getAttributeName(), 
					   cd, objectAdapter));

    }
    return beanVector;
  }
  private Vector MethodVector() {
	    if (methodVector == null) {
	      methodVector = new Vector();
	      for (int i=0; i<methods.length; i++)
		methodVector.addElement(new htElement(methods[i], getAttributeName(),cd, objectAdapter));
	    }
	    return methodVector;
	  }

  // Expose this method as Save in the File menu
  public boolean writeBeanInfo(String filename) {
     //return BeanInfoWriter.writeBeanInfo(this, filename) && compile (fileName);	 	 return BeanInfoWriter.writeBeanInfo(cd, filename);	   }
  
  // Expose this method as Save in the File menu
  public boolean writeBeanInfo() {
     //return BeanInfoWriter.writeBeanInfo(this, filename) && compile (fileName);
    //String	beanFile = cd.toShortName(cd.getRealClass().getName()) + "BeanInfo.java";;
	  String beanFile = cd.getRealClass().getName().replace('.', '/') + "BeanInfo.java";	  System.err.println("writing to file:" + beanFile);
	  //System.err.println("a.b.c".replace('.', '/'));
	  return BeanInfoWriter.writeBeanInfo(cd, beanFile);	   }
  public void useNewAttributeValues() {
	  UIAttributeManager environment = AttributeManager.getEnvironment();
	  if (environment == null) 
		  return;
	  environment.removeFromAttributeLists(cd.getRealClass().getName());
  }
  public void refreshWithNewAttributeValues() {
	  if (objectAdapter == null){
		  System.err.println("No object was selected when customizer created");
		  return;
	  }
	  useNewAttributeValues();
	  uiGenerator.deepProcessAttributes(objectAdapter); 
	  uiGenerator.deepElide(objectAdapter);
	  objectAdapter.getUIFrame().validate();
	  /*
	  uiFrame topFrame = objectAdapter.getUIFrame();
	  topFrame.deepElide(objectAdapter);
		topFrame.showMainPanel();
		*/
	 
  }  /*  public boolean compile (String fileName) {
	  String arguments[] = new String[2];
    //arguments[0] = "-classpath";
    //arguments[1] = classpath;
    arguments[0] = "-nowarn";
    arguments[1] = fileName;

    sun.tools.javac.Main compiler;
    try {
      compiler = new sun.tools.javac.Main(System.err, "javac");
      compiler.compile(arguments);	  return true;
    } catch (Exception e) {
      System.err.println("Could not load the JDK compiler");
      //System.exit(1);	  return false;
    }  }  */
  private Object[] helpers = null;
  //
  /*
  public void setLABEL() {
    setAttributeName(AttributeNames.LABEL);
    PropertyChanged();
  }
  public void setPOSITION() {
    setAttributeName(AttributeNames.POSITION);	VectorChanged();
    PropertyChanged();
  }
  public void setMENU() {
    setAttributeName(AttributeNames.MENU_NAME);
    PropertyChanged();
  }    public void setPLACE_TOOLBAR() {
    setAttributeName(AttributeNames.PLACE_TOOLBAR);
    PropertyChanged();
  }  
  public void setRIGHTMENU() {
    setAttributeName(AttributeNames.RIGHTMENU);
    PropertyChanged();
  }
  public void setTOOLBAR() {
    setAttributeName(AttributeNames.TOOLBAR);
    PropertyChanged();
  }
  public void setICON() {
    setAttributeName(AttributeNames.ICON);
    PropertyChanged();
  } 
  public void setVISIBLE() {
    setAttributeName(AttributeNames.VISIBLE);
    PropertyChanged();
  }  
  
    public void setELIDE_IMAGE() {
    setAttributeName(AttributeNames.ELIDE_IMAGE);
    PropertyChanged();
  }  public void setLABELLED() {
    setAttributeName(AttributeNames.LABELLED);
    PropertyChanged();
  }  public void setHELPER_LABEL() {
    setAttributeName(AttributeNames.HELPER_LABEL);
    PropertyChanged();
  }  public void setHELPER_ICON() {
    setAttributeName(AttributeNames.HELPER_ICON);
    PropertyChanged();
  }  public void setHELPER_LOCN() {
    setAttributeName(AttributeNames.HELPER_LOCN);
    PropertyChanged();
  }  public void setTITLE() {
    setAttributeName(AttributeNames.TITLE);
    PropertyChanged();
  }
  
    public void setPREFERRED_WIDGET() {
    setAttributeName(AttributeNames.PREFERRED_WIDGET);
    PropertyChanged();
  }  public void setDIRECTION() {
    setAttributeName(AttributeNames.DIRECTION);
    PropertyChanged();
  }  public void setNUM_COLUMNS() {
    setAttributeName(AttributeNames.TEXT_FIELD_LENGTH);
    PropertyChanged();
  }
  
  public void setDECINCUNIT() {
    setAttributeName(AttributeNames.DECINCUNIT);
    PropertyChanged();
  }
  
    public void setINCREMENTAL() {
    setAttributeName(AttributeNames.INCREMENTAL);
    PropertyChanged();
  }  public void setDOUBLE_CLICK_METHOD() {
    setAttributeName(AttributeNames.DOUBLE_CLICK_METHOD);
    PropertyChanged();
  }
  */
  public static ClassDescriptorCustomizer getClassDescriptorCustomizer(ObjectAdapter editedAdapter) {
	  Object editedObject = editedAdapter.getRealObject();
		//ClassProxy editedClass = RemoteSelector.getClass (editedObject.getClass());
		AClassDescriptor cd = (AClassDescriptor) ClassDescriptorCache.getClassDescriptor(editedObject);
	  return new ClassDescriptorCustomizer(cd, editedAdapter);
		  //return customizer;
	  }
  
}

