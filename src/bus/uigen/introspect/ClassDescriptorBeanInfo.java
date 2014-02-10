package bus.uigen.introspect;

import java.lang.reflect.*;
import java.util.Vector;
import java.beans.*;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ClassDescriptorCustomizer;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
public class ClassDescriptorBeanInfo extends  SimpleBeanInfo /*implements ViewInfo*/ {
  
  public BeanDescriptor getBeanDescriptor() {
    try {
      Class c = AClassDescriptor.class;
      BeanDescriptor bd = new BeanDescriptor(c);
      Method m = c.getMethod("getHelpers", new Class[0]);
      bd.setValue(AttributeNames.HELPER_METHOD, m);
      bd.setValue(AttributeNames.HELPER_LABEL+"_0", "Attribute\nNames");
      return bd;
    } catch (Exception e) {
      return null;
    }
  }
  
  
  public MethodDescriptor[] getMethodDescriptors() {
    try {
      Class[] params;
      Method m;
      Class c = AClassDescriptor.class;	  	  //MethodDescriptor[] array = new MethodDescriptor[21];
      MethodDescriptor[] array = new MethodDescriptor[22]; //F.O. adding toolbar of choice

      params = new Class[1];
      params[0] = String.class;
      m = c.getMethod("writeBeanInfo", params);
      MethodDescriptor md = new MethodDescriptor(m);
      //md.setDisplayName("Save Customization");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "File");
      array[0] = md;

      params = new Class[0];
      m = c.getMethod("setLABEL", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Label");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[1] = md;
      
      m = c.getMethod("setPOSITION", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Position");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[2] = md;

      m = c.getMethod("setMENU", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Menu Name");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[3] = md;

      m = c.getMethod("setRIGHTMENU", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Popup Method");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[4] = md;

      m = c.getMethod("setTOOLBAR", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Toolbar Method");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[5] = md;

      m = c.getMethod("setICON", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Toolbar Icon");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[6] = md;

      m = c.getMethod("setVISIBLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Visible");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[7] = md;	  	  m = c.getMethod("setVISIBLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Visible");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[7] = md;	  	  m = c.getMethod("setVISIBLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Visible");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[7] = md;	  	  m = c.getMethod("setVISIBLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Visible");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[7] = md;	  	  m = c.getMethod("setVISIBLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Visible");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[7] = md;	  	  m = c.getMethod("setVISIBLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Visible");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[7] = md;	  	  m = c.getMethod("setVISIBLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Visible");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[7] = md;	  	  m = c.getMethod("setELIDE_IMAGE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Elide Image");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[8] = md;	  	  m = c.getMethod("setLABELLED", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Labelled");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[9] = md;	  	  m = c.getMethod("setHELPER_LABEL", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Helper Label");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[10] = md;	  	  m = c.getMethod("setHELPER_ICON", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Helper Icon");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[11] = md;	   	  m = c.getMethod("setHELPER_LOCN", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Helper Location");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[12] = md;	  	  m = c.getMethod("setTITLE", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Frame Title");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[13] = md;
	  	  m = c.getMethod("setPREFERRED_WIDGET", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Preferred Widget");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[14] = md;	  	  	  m = c.getMethod("setDIRECTION", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Children Direction");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[15] = md;	  	  	  m = c.getMethod("setNUM_COLUMNS", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Number Of Columns");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[16] = md;	  	   	  m = c.getMethod("setINCREMENTAL", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Incremental Feedback");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[17] = md;
	  
      m = c.getMethod("writeBeanInfo", params);
      md = new MethodDescriptor(m);
      //md.setDisplayName("Save Customization");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "File");
      array[18] = md;
	  
	  m = c.getMethod("setDOUBLE_CLICK_METHOD", params);
      md = new MethodDescriptor(m);	  
      md.setDisplayName("Double Click Method");
      //md.setDisplayName("Save Customization");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[19] = md;		  
	  m = c.getMethod("setDECINCUNIT", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Unit of Inc&Dec");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[20] = md;	        m = c.getMethod("setPLACE_TOOLBAR", params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Toolbar Group Name");
      md.setValue(AttributeNames.METHOD_MENU_NAME, "Attribute");
      array[21] = md;
	  
      return array;
 	}
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      Class c = AClassDescriptor.class;
      PropertyDescriptor[] array = new PropertyDescriptor[5];
      PropertyDescriptor pd = new PropertyDescriptor("fields", c);
      pd.setDisplayName("Edit Property Attribute");
      pd.setValue(AttributeNames.POSITION, new Integer(0));
      array[0] = pd;

      pd = new PropertyDescriptor("methods", c);
      pd.setDisplayName("Edit Method Attribute");
      pd.setValue(AttributeNames.POSITION, new Integer(1));
      array[1] = pd;

      pd = new PropertyDescriptor("bean", c);
      pd.setDisplayName("Edit Class Attribute");
      pd.setValue(AttributeNames.POSITION, new Integer(2));
      array[2] = pd;

      pd = new PropertyDescriptor("propertyName", c);
      pd.setDisplayName("Attribute");
      pd.setValue(AttributeNames.POSITION, new Integer(3));
      array[3] = pd;

      pd = new PropertyDescriptor("htVector", c, "getHtVector", null);
      pd.setDisplayName("Attribute Values");
      pd.setValue(AttributeNames.POSITION, new Integer(4));
      array[4] = pd;

      return array;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    
  }
    
  public FieldDescriptor[] getFieldDescriptors() {return null;}
  public ConstantDescriptor[] getConstantDescriptors(){return null;}
  public ConstructorDescriptor[] getConstructorDescriptors(){return null;}
  public FeatureDescriptor[] getFeatureDescriptors(){return null;}  public void setAttribute(String attr, Object val) {}  public void setPropertyAttribute(String property, String attr, Object val) {}
  public void setMethodAttribute(String property, String attr, Object val) {}
  public Object getPrototypeObject() {return null;};
  public void setPrototypeObject(Object newVal) {};
  public MethodProxy[] getVirtualMethods() { return null;}
  public void addProperties (Vector<PropertyDescriptor> newElements) {}
  public void addMethods(Vector<VirtualMethodDescriptor> newElements) {}
  public void setMethodDescriptors(Vector<MethodDescriptor> newVal) {}
  public void setPropertyDescriptors(Vector<PropertyDescriptor> newVal) {}  
  public Object getMethodAttribute(String method, String attribute) {return null;}
  public Object getPropertyAttribute(String property, String attribute) {return null;}


public Object getAttribute(String attr) {
	// TODO Auto-generated method stub
	return null;
}


public MethodDescriptor getMethodDescriptor(String name) {
	// TODO Auto-generated method stub
	return null;
}


public MethodDescriptor getMethodDescriptor(Method method) {
	// TODO Auto-generated method stub
	return null;
}


public MethodDescriptor getMethodDescriptor(MethodProxy method) {
	// TODO Auto-generated method stub
	return null;
}


public void setMethodDescriptors(MethodDescriptor[] newVal) {
	// TODO Auto-generated method stub
	
}


public void setPropertyDescriptors(PropertyDescriptor[] newVal) {
	// TODO Auto-generated method stub
	
}



public ClassDescriptorCustomizer getClassDescriptorCustomizer() {
	// TODO Auto-generated method stub
	return null;
}



public ClassDescriptorCustomizer getClassDescriptorCustomizer(
		ObjectAdapter selectedAdapter) {
	// TODO Auto-generated method stub
	return null;
}



public MethodDescriptor getDynamicCommandsMethodDescriptor() {
	// TODO Auto-generated method stub
	return null;
}



public Vector<VirtualMethodDescriptor> getDynamicMethodDescriptors() {
	// TODO Auto-generated method stub
	return null;
}


//@Override
public PropertyDescriptor[] getIndexOrKeyPropertyDescriptors() {
	// TODO Auto-generated method stub
	return null;
}


//@Override
public PropertyDescriptor getPropertyDescriptor(String name) {
	// TODO Auto-generated method stub
	return null;
}


//@Override
public boolean isDynamic(MethodDescriptor md) {
	// TODO Auto-generated method stub
	return false;
}


//@Override
public void setAttributeOfAllMethods(String attribute, Object value) {
	// TODO Auto-generated method stub
	
}


//@Override
public void setAttributeOfAllProperties(String attribute, Object value) {
	// TODO Auto-generated method stub
	
}
}
