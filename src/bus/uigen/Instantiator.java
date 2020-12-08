 
// utility class to help with
// instantiating instances of
// specified classes. Should take
// care of at least some cases where
// no zero argument constructor is 
// present.

package bus.uigen;
import java.util.*;
import java.lang.reflect.*;

import util.misc.Common;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.remote.RemoteClassProxy;
import bus.uigen.reflect.remote.RemoteObjectProxy;
import bus.uigen.reflect.remote.StandardTypeIDs;
import bus.uigen.translator.*;

public class Instantiator {

  static Vector examinedClasses = new Vector(); 
  // Used to make sure we 
  // dont consider a constructor which takes us into infinite
  // recursion.
  

  // Pick an appropriate constructor to use
  public static MethodProxy chooseConstructor(MethodProxy[] constructors) {
    // First check to see if a zero argument constructor
    // exists. If so, this is our best bet.
    for (int i=0; i< constructors.length; i++) {
      if (constructors[i].getParameterTypes().length == 0)
	return constructors[i];
    }
    // Otherwise pick a constructor with parameter types
    // we havent seen before. (Hopefully this is adequate
    for (int i=0; i< constructors.length; i++) {
    	MethodProxy constructor = constructors[i];
      ClassProxy[] parameterTypes = constructor.getParameterTypes();
      for (int j=0; j< parameterTypes.length; j++) {
	if (examinedClasses.contains(parameterTypes[j]))
	  break;
      }
      return constructor;
    }
    
    // No suitable constructor found
    return null;
  }
    static PrimitiveClassList checker = new  PrimitiveClassList();
  
  public static Object newPrimitiveInstance(ClassProxy c, Object v) {
	  if (v!= null && c.isAssignableFrom(RemoteSelector.getClass(v)))
		  return v;
	  else		  return checker.newPrimitiveInstance(c, v);	  /*
    String classname = c.getName();
    String value = (String) v;	try {
    if (classname.equals("java.lang.Character")) {
      return new Character(value.charAt(0));
    }
    else if (classname.equals("java.lang.String")) {
      return new String(value);
    }
    else if (classname.equals("java.lang.Boolean") ||
	     c.equals(Boolean.TYPE)) {
      return new Boolean(value);
    }
    else if (classname.equals("java.lang.Integer") ||
	     c.equals(Integer.TYPE)) {
      return new Integer(value);
    }
    else if (classname.equals("java.lang.Float") ||
	     c.equals(Float.TYPE)) {
      return new Float(value);
    }
    else if (classname.equals("java.lang.Byte") ||
	     c.equals(Byte.TYPE)) {
      return new Byte(value);
    }
    else if (classname.equals("java.lang.Double") ||
	     c.equals(Double.TYPE)) {
      //return new Double(value);		return new StringToDouble().translate(value);
    }
    else if (classname.equals("java.lang.Short") ||
	     c.equals(Short.TYPE)) {
      return new Short(value);
    }
    else if (classname.equals("java.lang.Long") ||
	     c.equals(Long.TYPE)) {
      return new Long(value);
    }
    else
      return null;	} catch (Exception e) {return null;};	  */
  }

  public static Object newInstance(ClassProxy c) {
    examinedClasses = new Vector();
    Object value = newInstance1(c);
    return value;
  }
   static Hashtable<Object, Object> classToDefaultInstance = new Hashtable();
   static boolean initialized = false;
   
   public static void register (ClassProxy c, Object defaultInstance) {
	   classToDefaultInstance.put(c, defaultInstance);
   }
   
   static void init() {
	   if (initialized)
		   return;
	   initialized = true;
	   classToDefaultInstance = Common.nestedArrayToHashtable(classToDefaultInstanceArray);
	   
   }
   
  static Object[][] classToDefaultInstanceArray = {
	  {String.class, ""},
	  {Character.class, 'x'},
	  {Character.TYPE, 'x'},
	  {Boolean.class, false},
	  {Boolean.TYPE, false},
	  {Integer.class, 0},
	  {Integer.TYPE, 0},
	  {Byte.class, 0},
	  {Byte.TYPE, 0},
	  {Short.class, 0},
	  {Short.TYPE, 0},
	  {Long.class, 0},
	  {Long.TYPE, 0},
	  {Float.class, 0.0},
	  {Float.TYPE, 0.0},
	  {Double.class, 0.0},
	  {Double.TYPE, 0.0},
	  
	  {StandardProxyTypes.stringClass(), ""},	  
	  {StandardProxyTypes.characterType(), 'x'},
	  {StandardProxyTypes.characterClass(), 'x'},
	  {StandardProxyTypes.booleanClass(), false},
	  {StandardProxyTypes.booleanType(), false},
	  {StandardProxyTypes.integerClass(), 0},
	  {StandardProxyTypes.integerType(), 0},
	  {StandardProxyTypes.byteClass(), 0},
	  {StandardProxyTypes.byteType(), 0},
	  {StandardProxyTypes.shortClass(), 0},
	  {StandardProxyTypes.shortType(), 0},
	  {StandardProxyTypes.longClass(), 0},
	  {StandardProxyTypes.longType(), 0},
	  {StandardProxyTypes.floatClass(), 0.0},
	  {StandardProxyTypes.floatType(), 0.0},
	  {StandardProxyTypes.doubleClass(), 0.0},
	  {StandardProxyTypes.doubleType(), 0.0},
	  
	  {StandardTypeIDs.STRING_CLASS, ""},	  
	  {StandardTypeIDs.CHARACTER_TYPE, 'x'},
	  {StandardTypeIDs.CHARACTER_CLASS, 'x'},
	  {StandardTypeIDs.BOOLEAN_CLASS, false},
	  {StandardTypeIDs.BOOLEAN_TYPE, false},
	  {StandardTypeIDs.INTEGER_CLASS, 0},
	  {StandardTypeIDs.INTEGER_TYPE, 0},
	  {StandardTypeIDs.BYTE_CLASS, 0},
	  {StandardTypeIDs.BYTE_TYPE, 0},
	  {StandardTypeIDs.SHORT_CLASS, 0},
	  {StandardTypeIDs.SHORT_TYPE, 0},
	  {StandardTypeIDs.LONG_CLASS, 0},
	  {StandardTypeIDs.LONG_TYPE, 0},
	  {StandardTypeIDs.FLOAT_CLASS, 0.0},
	  {StandardTypeIDs.FLOAT_TYPE, 0.0},
	  {StandardTypeIDs.DOUBLE_CLASS, 0.0},
	  {StandardTypeIDs.DOUBLE_TYPE, 0.0}
  };
	  
  
  public static Object newInstance1(ClassProxy c) {
	init();
    String classname = c.getName();
    examinedClasses.addElement(c);
    Object retVal = classToDefaultInstance.get(c);
    if ((retVal == null) && ( c instanceof RemoteClassProxy)) {
    	RemoteClassProxy remoteClassProxy = (RemoteClassProxy) c;
    	return classToDefaultInstance.get(remoteClassProxy.getClassID());
    	/*
    	String newPredefinedInstanceID = classToDefaultInstance.get(remoteClassProxy.getClassID()).toString();
    	retVal = RemoteObjectProxy.objectProxy(remoteClassProxy.getFactoryName(),newPredefinedInstanceID , remoteClassProxy.getClassID());
    	*/
    }
    if (retVal != null)
    	return retVal;
    /*
    
    if (classname.equals("java.lang.String")) {
      return new String("");
    } 
    else if (classname.equals("java.lang.Character") ||
	     c.equals(Character.TYPE)) {
      return new Character('x');
    }
    else if (classname.equals("java.lang.Boolean") ||
	     c.equals(Boolean.TYPE)) {
      return new Boolean(true);
    }
    else if (classname.equals("java.lang.Integer") ||
	     c.equals(Integer.TYPE)) {
      return new Integer(0);
    } 
    else if (classname.equals("java.lang.Float") ||
	     c.equals(Float.TYPE)) {
      return new Float(0);
    }
    else if (classname.equals("java.lang.Byte") ||
	     c.equals(Byte.TYPE)) {
      return new Byte("0");
    }
    else if (classname.equals("java.lang.Double") ||
	     c.equals(Double.TYPE)) {
      return new Double(0);
    }
    else if (classname.equals("java.lang.Short") ||
	     c.equals(Short.TYPE)) {
      return new Short((short) 0);
    }
    else if (classname.equals("java.lang.Long") ||
	     c.equals(Long.TYPE)) {
      return new Long(0);
    }
    else {
    */
      // Not a recognised primitive type
      // Pick any constructor (which doesnt have this class
      // as an argument and call uiInstantiator:newInstance()
      // recursively for each of the parameters.
      try {
	//Constructor[] constructors = c.getConstructors();
	MethodProxy[] constructors = c.getConstructors();
	if (constructors.length == 0) {
	  //System.out.println("No declared constructors found");
	  return null;
	}
	MethodProxy constructor = chooseConstructor(constructors);
	if(constructor == null) {
	  System.err.println("Couldnt choose a constructor");
	  return null;
	}
	ClassProxy[] parameterTypes = constructor.getParameterTypes();
	Object[] parameterValues = new Object[parameterTypes.length];
	for (int i=0; i< parameterTypes.length; i++) {
	  parameterValues[i] = newInstance(parameterTypes[i]);
	}
	try {
	  Object obj = constructor.newInstance(parameterValues);
	  return obj;
	} catch (Exception e) {
	  //e.printStackTrace();
		//throw e;
	  return null;
	}
      } catch (Exception e) {
	e.printStackTrace();
	return null;
      }
      
    //}
  }
}





