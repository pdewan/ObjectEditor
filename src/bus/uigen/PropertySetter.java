package bus.uigen;
import java.beans.*;
import java.lang.reflect.*;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.PropertyDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;


public class PropertySetter {
	  public static boolean dummySetProperty(Object obj, 
			    String propertyName, 
			    Object propertyValue) {
		  return false;
		  
	  }
  public static boolean setProperty(Object obj, 
				    String propertyName, 
				    Object propertyValue) {	  if (obj == null) return true;
    //ClassProxy objectClass = RemoteSelector.getClass(obj);
    ClassDescriptorInterface classDescriptor = ClassDescriptorCache.getClassDescriptor(obj);
    try {
      if (propertyName.indexOf(AttributeNames.PATH_SEPARATOR) != -1 ) return false; //widgets will not have composite attributes
//      BeanInfo beanInfo = Introspector.getBeanInfo(objectClass);
//      PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
      PropertyDescriptorProxy[] properties = classDescriptor.getPropertyDescriptors();
     
      for (int i=0; i<properties.length; i++) {
	if (properties[i].getName().toLowerCase().equals(propertyName.toLowerCase())) {
	  PropertyDescriptorProxy property = properties[i];
	  MethodProxy writeMethod = property.getWriteMethod();
	  if (writeMethod == null)
	    return false;
	  try {
	    Object[] params = {propertyValue};
	    writeMethod.invoke(obj, params);
	    return true;
	  } catch (Exception e) {
	    return false;
	  }
	}
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }
}
