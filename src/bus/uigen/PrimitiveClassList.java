
package bus.uigen;
import java.util.*;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.ConcretePrimitive;import bus.uigen.sadapters.ConcreteTypeRegistry;

// Implements the list of Primitive types

public class PrimitiveClassList {	static ConcretePrimitive checker;	static boolean checkerInitialized = false;
  //Vector primitiveList;
  public PrimitiveClassList() {	  if (checkerInitialized) return;	  checker = (ConcretePrimitive) ConcreteTypeRegistry.createConcreteType(ConcretePrimitive.class);	  if (checker == null) {
		  System.out.println("E** ConcretePrimitiveFactory not registered!");	  }
	  checkerInitialized = true;	  /*
    primitiveList = new Vector();

    // Load defaults
    primitiveList.addElement("java.lang.String");
    primitiveList.addElement("java.lang.Boolean");
    primitiveList.addElement("java.lang.Integer");
    primitiveList.addElement("java.lang.Float");
    primitiveList.addElement("java.lang.Byte");
    primitiveList.addElement("java.lang.Double");
    primitiveList.addElement("java.lang.Long");
    primitiveList.addElement("java.lang.Short");
    primitiveList.addElement("java.lang.Character");
    primitiveList.addElement("boolean");
    primitiveList.addElement("int");
    primitiveList.addElement("float");
    primitiveList.addElement("byte");
    primitiveList.addElement("double");
    primitiveList.addElement("long");
    primitiveList.addElement("short");
    primitiveList.addElement("char");	  */
  }
  
  public boolean isPrimitiveClass(String typeClass) {	  return checker.isPrimitiveClass(typeClass);
    //return primitiveList.contains(typeClass);
  }

  public Object newInstance(String typeClass) {
	  return checker.newInstance(typeClass);	  /*
    if (isPrimitiveClass(typeClass)) {
      if (typeClass.equals("java.lang.String"))
	return new String("");
      else if (typeClass.equals("java.lang.Boolean"))
	return new Boolean(true);
      else if (typeClass.equals("java.lang.Integer"))
	return new Integer(0);
      else if (typeClass.equals("java.lang.Float"))
	return new Float(0);
      else if (typeClass.equals("java.lang.Byte"))
	return new Byte("0");
      else if (typeClass.equals("java.lang.Double"))
	return new Double(0);
      else if (typeClass.equals("java.lang.Short"))
	return new Short((short) 0);
      else if (typeClass.equals("java.lang.Long"))
	return new Long(0);
      else if (typeClass.equals("java.lang.Character"))
	return new Character('x');
    }
    else {
      try {
	return Class.forName(typeClass).newInstance();
      } catch (Exception e) {
	return null;
      }	  
    }
    return null;	  */
  }

  // hack to fix the primitive type problem
  // Class.forName("int") doesnt work like it
  // used to.
  public static ClassProxy getPrimitiveClass(ClassProxy baseClass, String primitiveClass) {	  /*
    Class wrapperClass = null;
    if (primitiveClass.equals("int"))
      wrapperClass = Integer.TYPE;
    else if (primitiveClass.equals("bool"))
      wrapperClass = Boolean.TYPE;
    else if (primitiveClass.equals("float"))
      wrapperClass = Float.TYPE;
    else if (primitiveClass.equals("byte"))
      wrapperClass = Byte.TYPE;
    else if (primitiveClass.equals("double"))
      wrapperClass = Double.TYPE;
    else if (primitiveClass.equals("long"))
      wrapperClass = Long.TYPE;
    else if (primitiveClass.equals("short"))
      wrapperClass = Short.TYPE;
    else if (primitiveClass.equals("char"))
      wrapperClass = Character.TYPE;
    
    return wrapperClass;	  */	  return checker.getPrimitiveClass(baseClass, primitiveClass);
  }
  
  public static ClassProxy getWrapperType(ClassProxy primitiveClass) {
	  return checker.getWrapperType(primitiveClass);	  /*
    String wrapperClass = "";
    if (primitiveClass.equals(Integer.TYPE))
      wrapperClass = "java.lang.Integer";
    else if (primitiveClass.equals(Boolean.TYPE))
      wrapperClass = "java.lang.Boolean";
    else if (primitiveClass.equals(Float.TYPE))
      wrapperClass = "java.lang.Float";
    else if (primitiveClass.equals(Byte.TYPE))
      wrapperClass = "java.lang.Byte";
    else if (primitiveClass.equals(Double.TYPE))
      wrapperClass = "java.lang.Double";
    else if (primitiveClass.equals(Long.TYPE))
      wrapperClass = "java.lang.Long";
    else if (primitiveClass.equals(Short.TYPE))
      wrapperClass = "java.lang.Short";
    else if (primitiveClass.equals(Character.TYPE))
      wrapperClass = "java.lang.Character";
    else
      return primitiveClass;
    
    try {
      Class wrapper = Class.forName(wrapperClass);
      return wrapper;
    }
    catch (Exception e) {
      return primitiveClass;
    }  	  */
  }   public Object newPrimitiveInstance(ClassProxy c, Object v) {
	   return checker.newPrimitiveInstance(c, v);   }
  
}







