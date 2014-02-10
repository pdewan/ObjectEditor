package bus.uigen.introspect;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Customizer;
import java.beans.FeatureDescriptor;import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.controller.models.ClassDescriptorCustomizer;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;


public interface ClassDescriptorInterface {
	public ClassProxy getRealClass();
	public String getName();
	public BeanDescriptorProxy getBeanDescriptor();
  public FieldDescriptorProxy[] getFieldDescriptors();
  public FieldDescriptorProxy[] getConstantDescriptors();
  public ConstructorDescriptorProxy[] getConstructorDescriptors();
  public MethodDescriptorProxy[] getMethodDescriptors();
  public void setMethodDescriptors(MethodDescriptorProxy[] newVal);
  public void setMethodDescriptors(Vector<MethodDescriptorProxy> newVal);
  public PropertyDescriptorProxy[] getPropertyDescriptors() ;
  public PropertyDescriptorProxy getPropertyDescriptor(String name);
  public PropertyDescriptorProxy[] getIndexOrKeyPropertyDescriptors(); 
  public void setPropertyDescriptors(PropertyDescriptorProxy[] newVal);
  public void setPropertyDescriptors(Vector<PropertyDescriptorProxy> newVal);
  public FeatureDescriptorProxy[] getFeatureDescriptors();
  public Object getAttribute(String attr);  public void setAttribute(String attr, Object val);
  public void setPropertyAttribute(String property, String attr, Object val);
  public void setMethodAttribute(String methodName, String attr, Object val);
  public void setMethodAttribute(MethodProxy method, String attr, Object val);
  public void setAttributeOfAllMethods(String attribute, Object value);
  public void setAttributeOfAllProperties(String attribute, Object value);
  public void setPrototypeObject (Object newValue);
  public MethodProxy[] getVirtualMethods();
  public MethodProxy[] getVirtualConstructors();
  public void addProperties (Vector<PropertyDescriptorProxy> newElements);
  public void addMethods(Vector<VirtualMethodDescriptor> newElements);
  public Object getMethodAttribute(String method, String attribute);
  public Object getMethodAttribute (MethodProxy m, String name);
  public Object getPropertyAttribute(String property, String attribute);
  public MethodDescriptorProxy getMethodDescriptor (String name);
  public MethodDescriptorProxy getMethodDescriptor (MethodProxy method);
  //public MethodDescriptorProxy getMethodDescriptor (VirtualMethod method);
  //public ClassDescriptorCustomizer getClassDescriptorCustomizer();
  //public ClassDescriptorCustomizer getClassDescriptorCustomizer(uiObjectAdapter selectedAdapter);
  public boolean isDynamic(MethodDescriptorProxy md);
  public  Vector<VirtualMethodDescriptor> getDynamicMethodDescriptors ();
  public  MethodDescriptorProxy getDynamicCommandsMethodDescriptor();
  //public Object getInheritedAttribute(MethodDescriptor md, String attribute);  
  //public Method[] getDoubleClickMethods();
MethodProxy getAddPropertyChangeListenerMethod();
public MethodProxy getAddRemotePropertyChangeListenerMethod() ;
public MethodProxy getAddVectorListenerMethod() ;
public MethodProxy getAddHashtableListenerMethod() ;
public MethodProxy getAddTreeModelListenerMethod() ;
public MethodProxy getAddTableModelListenerMethod() ;
MethodProxy getAddObserverMethod();
MethodProxy getAddRefresherMethod();
public Hashtable getAttributes();
public void initPropertyMergedAttributes(PropertyDescriptorProxy pd);

//public PropertyDescriptorProxy[] getPropertyDesciptors();
}
