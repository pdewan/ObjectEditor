package bus.uigen.introspect;


import java.beans.Introspector;

//import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;

public  class ViewIntrospector {
  // Return a ViewInfo object (a ClassDescriptor)
  // for the specified class. The algorithm for
  // this is as follows
  // . If the class has an associated ViewInfo
  //   class, return an instance of this class
  // . otherwise return a new ClassDescriptor
  //   with the class`s BeanInfo merged with its
  //   fields (and constants)
  public static ClassDescriptorInterface getViewInfo(ClassProxy c) {
    // First see if a ViewInfo exists
	  ClassDescriptorInterface retVal = new AClassDescriptor(c);
	  return retVal;
	  /*
    String classname = c.getName();
    try {
      Class  vinfo = Class.forName(classname+"ViewInfo");
      if ((ViewInfo.class).isAssignableFrom(vinfo)) {
	return (ViewInfo) vinfo.newInstance();
      }
      else {
	return new ClassDescriptor(c);
      }
      
    }catch (ClassNotFoundException e) {
        // Couldnt find a ViewInfo
        return new ClassDescriptor(c);
      }
    catch (Exception e) {
      // Couldnt find a ViewInfo
    	e.printStackTrace();
      return null;
    }
    */
  }
  /*
  public static ViewInfo exposingExceptionBugetViewInfo(Class c) {
	    // First see if a ViewInfo exists
	    String classname = c.getName();
	    try {
	      Class  vinfo = Class.forName(classname+"ViewInfo");
	      if ((ViewInfo.class).isAssignableFrom(vinfo)) {
		return (ViewInfo) vinfo.newInstance();
	      }
	      else {
		return new ClassDescriptor(c);
	      }
	      
	    }catch (ClassNotFoundException e) {
	        // Couldnt find a ViewInfo
	        return new ClassDescriptor(c);
	      }
	    catch (Exception e) {
	      // Couldnt find a ViewInfo
	    	e.printStackTrace();
	      return null;
	    }
	  }
	  */
  public static ClassDescriptorInterface getViewInfo(ClassProxy c, Object prototypeObject, String virtualClass) {
	  prototypeObject = ACompositeLoggable.maybeExtractRealObject(prototypeObject);
	  if (c == null)
		  return new AClassDescriptor(c, prototypeObject, virtualClass);
	    // First see if a ViewInfo exists
	    try {

		    String classname = c.getName();
	      Class  vinfo = Class.forName(classname+"ViewInfo");
	      if ((ClassDescriptorInterface.class).isAssignableFrom(vinfo)) {
		return (ClassDescriptorInterface) vinfo.newInstance();
	      }
	      else {
		return new AClassDescriptor(c, prototypeObject, virtualClass);
	      }
	      
	    } catch (Exception e) {
	      // Couldnt find a ViewInfo
	      //return new ClassDescriptor(c);
	      return new AClassDescriptor(c, prototypeObject, virtualClass);
	    }
	  }
}
