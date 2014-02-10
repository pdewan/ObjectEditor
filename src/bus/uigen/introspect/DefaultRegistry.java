package bus.uigen.introspect;

import java.util.Hashtable;

import javax.swing.ImageIcon;

import util.models.Hashcodetable;
import util.models.LabelModel;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.translator.IllegalTranslatorClassException;
import bus.uigen.translator.ImageIconToLabelModel;
import bus.uigen.translator.ObjectToLabelModel;
import bus.uigen.translator.StringToATimeWithIncrementDecrement;
import bus.uigen.translator.StringToBoolean;
import bus.uigen.translator.StringToCharacter;
import bus.uigen.translator.StringToDate;
import bus.uigen.translator.StringToDouble;
import bus.uigen.translator.StringToFloat;
import bus.uigen.translator.StringToInteger;
import bus.uigen.translator.StringToLabelModel;
import bus.uigen.translator.StringToLong;
import bus.uigen.translator.StringToShort;
import bus.uigen.translator.Translator;

public class DefaultRegistry extends Hashcodetable {

  private static Class translatorClass = null;  private static Hashtable<ClassProxy, ClassProxy> primitiveToWrapper = new Hashtable();

  public DefaultRegistry() {
    // register default translators.
    register("java.lang.String", "java.lang.Integer", new StringToInteger());
    register("java.lang.String", "int", new StringToInteger());	register("java.lang.String", "long", new StringToLong());
	register("java.lang.String", "java.lang.Long", new StringToLong());	register("java.lang.String", "short", new StringToShort());	register("java.lang.String", "java.lang.Short", new StringToShort());
    register("java.lang.String", "java.lang.Float", new StringToFloat());
    register("java.lang.String", "float", new StringToFloat());	register("java.lang.String", "double", new StringToDouble());
    register("java.lang.String", "java.lang.Character", new StringToCharacter());
    register("java.lang.String", "java.lang.Boolean", new StringToBoolean());
    register("java.lang.String", "boolean", new StringToBoolean());
    register("java.lang.String", "java.util.Date", new StringToDate());
    register("java.lang.String", "util.ATimeWithIncrementDecrement", new StringToATimeWithIncrementDecrement());
//    register(String.class, LabelModel.class, new StringToLabelModel());
    register(ImageIcon.class, LabelModel.class, new ImageIconToLabelModel());
    register(Object.class, LabelModel.class, new ObjectToLabelModel());
	initPrimitiveToWrapper();
  }  void initPrimitiveToWrapper () {
	  /*
	  primitiveToWrapper.put(Integer.TYPE, Integer.class);
	  primitiveToWrapper.put(Boolean.TYPE, Boolean.class);
	  primitiveToWrapper.put(Double.TYPE, Double.class);
	  primitiveToWrapper.put(Float.TYPE, Float.class); 	  primitiveToWrapper.put(Short.TYPE, Short.class);
	  */
	  primitiveToWrapper.put(StandardProxyTypes.integerType(), StandardProxyTypes.integerClass());
	  primitiveToWrapper.put(StandardProxyTypes.booleanType(), StandardProxyTypes.booleanClass());
	  primitiveToWrapper.put(StandardProxyTypes.doubleType(), StandardProxyTypes.doubleClass());
	  primitiveToWrapper.put(StandardProxyTypes.floatType(), StandardProxyTypes.floatClass()); 
	  primitiveToWrapper.put(StandardProxyTypes.shortType(), StandardProxyTypes.shortClass());
	    }  public static ClassProxy getWrapper (ClassProxy primitive) {
	  return primitiveToWrapper.get(primitive);
	  /*
	  if (!(primitive instanceof AClassProxy))
		  return null;
		  
	  Class primJava = ((AClassProxy) primitive).getJavaClass();
	  return AClassProxy.classProxy((Class) primitiveToWrapper.get(primJava));
	  */  }

  private void my_register(String from, String to, Object translator) {
    Hashcodetable temp = (Hashcodetable) get(from);
    if (temp == null) {
      temp = new Hashcodetable();
      put(from, temp);
    }
    temp.put(to, translator);
  }
  
  public void register(String from, String to, Class translator) throws IllegalTranslatorClassException {
    if (translatorClass == null) {
      try {
	translatorClass = Class.forName("bus.uigen.translator.Translator");
      } catch (Exception e) {}
    }
    if (translatorClass.isAssignableFrom(translator))
      my_register(from, to, translator);
    else
      throw new IllegalTranslatorClassException();
  }
  public  void register(Class from,  Class to, Translator translator) {
	    register(from.getName(), to.getName(), translator);
	  }


  public  void register(String from, String to, Translator translator) {
    my_register(from, to, translator);
  }

  public  Object getTranslator(String from, String to) {
    Hashcodetable temp = (Hashcodetable) get(from);
    if (temp != null)
      return temp.get(to);
    else
      return null;
  }

}
