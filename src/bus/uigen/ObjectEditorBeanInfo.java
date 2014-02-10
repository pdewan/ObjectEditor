package bus.uigen;
import java.beans.*;
import java.lang.reflect.*;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.uiClassFinder;

public class ObjectEditorBeanInfo extends SimpleBeanInfo {
  public BeanDescriptor getBeanDescriptor() {
    try {
      Class c;
      Class[] params;
      //c = uiClassFinder.forName("bus.uigen.ObjectEditor");
      c = ObjectEditor.class;
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
      //c = uiClassFinder.forName("bus.uigen.ObjectEditor");
      c = ObjectEditor.class;
      Method m;
      MethodDescriptor[] array = new MethodDescriptor[22];
      MethodDescriptor md;
      params = new Class[1];
      params[0] = java.lang.String.class;
      m = c.getMethod("newInstance",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("New(String)");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(1));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue("doubleClickMethod", new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[0] = md;

      params = new Class[0];
      m = c.getMethod("showClasses",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Show Folder Classes");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(4));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[1] = md;
      
      params = new Class[0];
      m = c.getMethod("hideClasses",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Hide Folder Classes");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(5));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[2] = md;

      params = new Class[0];
      m = c.getMethod("source",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Source");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(8));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[3] = md;
      
      /*
      params = new Class[0];
      m = c.getMethod("showEntireFolder",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Show Entire Folder");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(6));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[4] = md;
      
      params = new Class[0];
      m = c.getMethod("hideEntireFolder",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Hide Entire Folder");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[5] = md;
      */

      params = new Class[0];
      m = c.getMethod("newInstance",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("New");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(0));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[4] = md;
      
      params = new Class[0];
      m = c.getMethod("showClassName",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Show Class Name");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(2));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[5] = md;
      
      params = new Class[0];
      m = c.getMethod("hideClassName",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Hide Class Name");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(3));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[6] = md;
      
      params = new Class[0];
      m = c.getMethod("preNewInstance",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre New Instance ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[7] = md;
      
      //Class[] params2 = {String.class};
      m = c.getMethod("preNewInstanceString",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre New Instance ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[8] = md;
      
      m = c.getMethod("preShowClasses",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Show Classes ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[9] = md;
      
      m = c.getMethod("preHideClasses",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Hide Classes ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[10] = md;
      
      m = c.getMethod("preShowClassName",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Show Class Name ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[11] = md;
      
      m = c.getMethod("preHideClassName",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Hide Class Name ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[12] = md;
      
      m = c.getMethod("preShowEntireFolder",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Show Entire Folder ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[13] = md;
      
      m = c.getMethod("preHideEntireFolder",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Hide Entire Folder ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[14] = md;
      
      m = c.getMethod("preGetCurrentClassName",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Get Current Class Name ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[15] = md;
      
      m = c.getMethod("preGetFolder",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Pre Get Folder ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[16] = md;
      
      m = c.getMethod("restoreDefaults",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Restore Defaults");
      //md.setValue(AttributeNames.VISIBLE, false);
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(9));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[17] = md;
      
      m = c.getMethod("preRestoreDefaults",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Restore Defaults ");
      md.setValue(AttributeNames.VISIBLE, false);
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(7));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[18] = md;
      
      params = new Class[0];
      m = c.getMethod("showClassNameList",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Show Class Name List");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(10));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.METHOD_MENU_NAME, "ObjectEditor");
      array[19] = md;
      
      params = new Class[0];
      m = c.getMethod("getCurrentClassName",params);
      md = new MethodDescriptor(m);
      array[20] = md;
      //md.setDisplayName("Show Class Name List");
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(10));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
     
      params = new Class[0];
      m = c.getMethod("indexedClasses",params);
      md = new MethodDescriptor(m);
      array[21] = md;
      /*
      
      params = new Class[0];
      m = c.getMethod("hideClassNameList",params);
      md = new MethodDescriptor(m);
      md.setDisplayName("Hide Class Name List");
      md.setValue(AttributeNames.POSITION, new java.lang.Integer(11));
      md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[20] = md;
      
      params = new Class[0];
      m = c.getMethod("preHideClassNameList",params);
      md = new MethodDescriptor(m);
      //md.setDisplayName("Hide Class Name List");
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(11));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[21] = md;
      
      params = new Class[0];
      m = c.getMethod("preShowClassNameList",params);
      md = new MethodDescriptor(m);
      //md.setDisplayName("Hide Class Name List");
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(11));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[22] = md;
      
      params = new Class[0];
      m = c.getMethod("preGetClassNameList",params);
      md = new MethodDescriptor(m);
      //md.setDisplayName("Hide Class Name List");
      //md.setValue(AttributeNames.POSITION, new java.lang.Integer(11));
      //md.setValue(AttributeNames.RIGHT_MENU, new java.lang.Boolean(true));
      //md.setValue(AttributeNames.MENU_NAME, "ObjectEditor");
      array[23] = md;
      */


      return array;
    } catch (Exception e) {
    	e.printStackTrace();
      return null;
    }
  }
  
  public PropertyDescriptor[] getPropertyDescriptors() {

    try {
      Class c = ObjectEditor.class;
      Class[] params;
      //PropertyDescriptor[] array = new PropertyDescriptor[2];
      PropertyDescriptor[] array = new PropertyDescriptor[1];
      PropertyDescriptor pd;
      /*
      pd = new PropertyDescriptor("folder", bus.uigen.ObjectEditor.class
      , "getFolder", null);
      pd.setValue(AttributeNames.POSITION, new java.lang.Integer(1));
      array[0] = pd;
      */
      /*
      pd = new PropertyDescriptor("classNameList", bus.uigen.ObjectEditor.class
    	      , "getClassNameList", null);
    	      pd.setValue(AttributeNames.POSITION, new java.lang.Integer(1));
    	      array[0] = pd;
	*/
      pd = new PropertyDescriptor("currentClassName",
        bus.uigen.ObjectEditor.class
      );
      pd.setValue(AttributeNames.POSITION, new java.lang.Integer(0));
      pd.setValue(AttributeNames.LABEL, "Class:");
      //params = new Class[0];
      //Method readMethod = c.getMethod("getCurrentClassName",params);
      //pd.setReadMethod(readMethod);
      array[0] = pd;
      /*
      pd = new PropertyDescriptor("classDescription",
    	        bus.uigen.ObjectEditor.class, "getClassDescription", null
    	      );
    	      pd.setValue(AttributeNames.POSITION, new java.lang.Integer(1));
    	      //pd.setValue(AttributeNames.LABEL, "Class:");
    	      //params = new Class[0];
    	      //Method readMethod = c.getMethod("getCurrentClassName",params);
    	      //pd.setReadMethod(readMethod);
    	      array[1] = pd;
      
	 */
      return array;
    }catch (Exception e) {return null;}
  }
}
