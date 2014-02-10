package bus.uigen.translator;

import java.lang.reflect.Method;

public class ClassTranslator implements Translator {
  private Method translatorMethod = null;
  public ClassTranslator(Class c) {
    try {
      Class[] params = {Class.forName("java.lang.Object")};
      translatorMethod = c.getMethod("translate", params);
    } catch (Exception e) {
    }
  }
  public String from() {return "";}
  public String to() {return "";}

  public Object translate(Object obj) {
    try {
      Object[] params = {obj};
      return translatorMethod.invoke(null, params);
    } catch (Exception e) {
      return null;
    }
  }
}
