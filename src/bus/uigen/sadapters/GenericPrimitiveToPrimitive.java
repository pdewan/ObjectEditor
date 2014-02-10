package bus.uigen.sadapters;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.ClassIntrospectionFilterer;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.translator.StringToDouble;
import bus.uigen.translator.TranslatorRegistry;

// Implements the list of Primitive types

public class GenericPrimitiveToPrimitive extends AbstractConcreteType implements ConcretePrimitive {	public GenericPrimitiveToPrimitive(Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );		init();
	}	public GenericPrimitiveToPrimitive() {		init();
	}	public void setMethods(ClassProxy objectClass) {		}
  static Set<String> primitiveList =  new HashSet();  static boolean primitiveListInitialized = false;
  public  static void init() {
	if (primitiveListInitialized) return;	primitiveListInitialized = true;
    // Load defaults
    primitiveList.add("java.lang.String");
    primitiveList.add("java.lang.Boolean");
    primitiveList.add("java.lang.Integer");
    primitiveList.add("java.lang.Float");
    primitiveList.add("java.lang.Byte");
    primitiveList.add("java.lang.Double");
    primitiveList.add("java.lang.Long");
    primitiveList.add("java.lang.Short");
    primitiveList.add("java.lang.Character");
    primitiveList.add("boolean");
    primitiveList.add("int");
    primitiveList.add("float");
    primitiveList.add("byte");
    primitiveList.add("double");
    primitiveList.add("long");
    primitiveList.add("short");
    primitiveList.add("char");
    primitiveList.add(StringBuilder.class.getName());
    primitiveList.add(ImageIcon.class.getName());
    primitiveList.add(Image.class.getName());
  }
  
  public  boolean isPrimitiveClass(String typeClass) {
	  return isPrimitiveClassStatic(typeClass);
    //return primitiveList.contains(typeClass);
  }
  public  boolean isPrimitiveClass(ClassProxy aClass) {
	  return isPrimitiveClassStatic(aClass);
    //return primitiveList.contains(typeClass);
  }
  public static boolean isPrimitiveClassStatic(String typeClass) {
	  return primitiveList.contains(typeClass);
  }
  public static boolean isPrimitiveClassStatic(ClassProxy aClass) {
	  return primitiveList.contains(aClass.getName()) || ClassIntrospectionFilterer.componentsIgnored(aClass);
  }

  public Object newInstance(String typeClass) {
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
    return null;
  }

  // hack to fix the primitive type problem
  // Class.forName("int") doesnt work like it
  // used to.
  public  ClassProxy getPrimitiveClass(ClassProxy baseClass, String primitiveClass) {
    ClassProxy wrapperClass = null;
    if (primitiveClass.equals("int"))
      wrapperClass = baseClass.integerType();
    else if (primitiveClass.equals("bool"))
      wrapperClass = baseClass.booleanType(); 
    else if (primitiveClass.equals("float"))
      wrapperClass = baseClass.floatType();
    else if (primitiveClass.equals("byte"))
      wrapperClass = baseClass.byteType();
    else if (primitiveClass.equals("double"))
      wrapperClass = baseClass.doubleType(); 
    else if (primitiveClass.equals("long"))
      wrapperClass = baseClass.longType(); 
    else if (primitiveClass.equals("short"))
      wrapperClass = baseClass.shortType();
    else if (primitiveClass.equals("char"))
      wrapperClass = baseClass.characterType();;
    
    return wrapperClass;
  }
  
  public ClassProxy getWrapperType(ClassProxy primitiveClass) {
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
      ClassProxy wrapper = RemoteSelector.forName(wrapperClass);
      return wrapper;
    }
    catch (Exception e) {
      return primitiveClass;
    }  
  }  public  Object newPrimitiveInstance(ClassProxy c, Object v) {
    String classname = c.getName();
    String value = (String) v;
    Object retVal;	try {
	retVal = TranslatorRegistry.convert("java.lang.String", classname, v);
	if (retVal != null) return retVal;
	else
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
      return null;	} catch (Exception e) {return null;}
  }
  public static String PRIMITIVE = "Primitive";
  public String typeKeyword() {
	  String retVal = ObjectEditor.TYPE_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + PRIMITIVE;
	  if (targetObject != null)
		  
		retVal = retVal + AttributeNames.KEYWORD_SEPARATOR + targetObject.getClass().getSimpleName();
	  else
		  retVal = "";

	  return retVal;
	}
	public String programmingPatternKeyword() {
		return "";
	}
	ConcreteType prototype;
	@Override
	public ConcreteType getPrototype() {
		// TODO Auto-generated method stub
		return prototype;
	}
	@Override
	public void setPrototype(ConcreteType thePrototype) {
		prototype = thePrototype;
		
	}
	public Object clone() {
		return  this;
	}
	public Object objectClone() {
		return super.clone();
	}
  
}







