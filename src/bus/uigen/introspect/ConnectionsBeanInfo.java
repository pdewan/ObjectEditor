package bus.uigen.introspect;
import java.beans.*;
import java.lang.reflect.*;
//import bus.uigen.AttributeNames;
//import bus.uigen.editors.Connections;

public class ConnectionsBeanInfo extends SimpleBeanInfo {
  public BeanDescriptor getBeanDescriptor() {
    try {
      Class c;
      Class[] params;
      c = bus.uigen.editors.Connections.class;
      BeanDescriptor bd = new BeanDescriptor(c);
      return bd;
    } catch (Exception e) {
      return null;
    }
  }
  public MethodDescriptor[] getMethodDescriptors() {
    try {
      Class c;
      Class[] params;
      c = bus.uigen.editors.Connections.class;
      Method m;
      MethodDescriptor[] array = new MethodDescriptor[19];
      MethodDescriptor md;
      params = new Class[0];
      m = c.getMethod("getDrawing",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Get Drawing");
      md.setValue("menuName", bus.uigen.uiFrame.BEAN_METHODS_MENU_NAME);
      array[0] = md;

      params = new Class[1];
      params[0] = util.undo.Listener.class;
      m = c.getMethod("addListener",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Add Listener ...");
      md.setValue("menuName", "Listenable");
      array[1] = md;

      params = new Class[0];
      m = c.getMethod("notifyListeners",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Notify Listeners");
      md.setValue("menuName", "Listenable");
      array[2] = md;

      params = new Class[0];
      m = c.getMethod("notifyAll",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Notify All");
      md.setValue("menuName", "Object");
      array[3] = md;

      params = new Class[1];
      params[0] = java.lang.Long.TYPE;
      m = c.getMethod("wait",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Wait ...");
      md.setValue("menuName", "Object");
      array[4] = md;

      params = new Class[2];
      params[0] = java.lang.Long.TYPE;
      params[1] = java.lang.Integer.TYPE;
      m = c.getMethod("wait",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Wait ...");
      md.setValue("menuName", "Object");
      array[5] = md;

      params = new Class[0];
      m = c.getMethod("toString",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("To String");
      md.setValue("menuName", "Object");
      array[6] = md;

      params = new Class[2];
      params[0] = util.models.Listenable.class;
      params[1] = java.lang.Object.class;
      m = c.getMethod("update",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Update ...");
      md.setValue("toolbar", new java.lang.Boolean(false));
      md.setValue("menuName", "Connections");
      array[7] = md;

      params = new Class[0];
      m = c.getMethod("reset",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Reset");
      md.setValue("toolbar", new java.lang.Boolean(false));
      md.setValue("menuName", "Connections");
      array[8] = md;

      params = new Class[3];
      params[0] = java.lang.String.class;
      params[1] = java.lang.String.class;
      params[2] = java.lang.String.class;
      m = c.getMethod("connect",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Connect ...");
      md.setValue("toolbar", new java.lang.Boolean(false));
      md.setValue("menuName", "Connections");
      array[9] = md;

      params = new Class[0];
      m = c.getMethod("notify",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Notify");
      md.setValue("menuName", "Object");
      array[10] = md;

      params = new Class[1];
      params[0] = slm.SLModel.class;
      m = c.getMethod("setDrawing",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Set Drawing ...");
      md.setValue("menuName", "Connections");
      array[11] = md;

      params = new Class[0];
      m = c.getMethod("clone",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Clone");
      md.setValue("toolbar", new java.lang.Boolean(false));
      md.setValue("menuName", "Listenable");
      array[12] = md;

      params = new Class[1];
      params[0] = java.lang.Object.class;
      m = c.getMethod("equals",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Equals ...");
      md.setValue("menuName", "Object");
      array[13] = md;

      params = new Class[1];
      params[0] = util.undo.Listener.class;
      m = c.getMethod("removeListener",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Remove Listener ...");
      md.setValue("menuName", "Listenable");
      array[14] = md;

      params = new Class[0];
      m = c.getMethod("hashCode",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Hash Code");
      md.setValue("menuName", "Object");
      array[15] = md;

      params = new Class[1];
      params[0] = java.lang.Object.class;
      m = c.getMethod("notifyListeners",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Notify Listeners ...");
      md.setValue("menuName", "Listenable");
      array[16] = md;

      params = new Class[0];
      m = c.getMethod("getClass",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Get Class");
      md.setValue("menuName", bus.uigen.uiFrame.BEAN_METHODS_MENU_NAME);
      array[17] = md;

      params = new Class[0];
      m = c.getMethod("wait",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Wait");
      md.setValue("menuName", "Object");
      array[18] = md;

      return array;
    } catch (Exception e) {
      return null;
    }
  }
  public PropertyDescriptor[] getPropertyDescriptors() {

    try {
      Class c;
      Class[] params;
      PropertyDescriptor[] array = new PropertyDescriptor[2];
      PropertyDescriptor pd;
      pd = new PropertyDescriptor("class",
        bus.uigen.editors.Connections.class
      , "getClass", null);
      array[0] = pd;

      pd = new PropertyDescriptor("drawing",
        bus.uigen.editors.Connections.class
      );
      array[1] = pd;

      return array;
    }catch (Exception e) {return null;}
  }
}
