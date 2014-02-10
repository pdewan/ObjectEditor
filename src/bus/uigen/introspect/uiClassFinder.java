package bus.uigen.introspect;

import java.util.Vector;import java.util.Hashtable;
import java.util.StringTokenizer;
//import java.lang.Class;import bus.uigen.introspect.*;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.remote.RemoteClassProxy;

public class uiClassFinder {
  
  private static boolean initialised = false;
  private static Vector imports = new Vector();  private static Hashtable nameToClass = new  Hashtable();
  // look in actual class for getVirtualClass
  public static String  getName(ClassProxy c) {
	  if (c == null) return null;	  String className = c.getName();
	  if (className.endsWith("_Stub")) {
	  	String tryName = className.substring(0, className.length() - 5);
	  	//boolean isStub = false;
	  	try {
	  		ClassProxy originalClass = RemoteSelector.forName( (tryName));
	  		className = tryName;
	  	} catch (Exception e) {
	  		
	  	}
	  }
	  if (nameToClass.containsKey(className)) return className;
	  nameToClass.put(className, c);
	  return className;  }
  private static void init() {
	  //String imports = "java.lang:java.util:java.awt:java.awt.event:bus.uigen:edu.unc.sync";
	  String imports = "java.lang:java.util:java.awt:java.awt.event:javax.swing:bus.uigen:bus.uigen.widgets:edu.unc.sync";
    setImports(imports);
  }    public static void addImport(String s) {	  //System.out.println(s);
	  if (!imports.contains(s)) {
		 // System.out.println(s);	     imports.addElement(s);
	  }  }


  public static String getImports() {
    if (!initialised)
      init();
    String importstring = "";
    for (int i=0; i<imports.size(); i++) {
      importstring = importstring+(String) imports.elementAt(i);
      if (i != imports.size()-1)
	importstring = importstring+":";
    }
    return importstring;
  }

  public static void setImports(String path) {
    // Parse the path string into components and
    // store each component as an element in the 
    // imports Vector.
    imports.removeAllElements();
    String separator = ":";
    StringTokenizer tokenizer = new StringTokenizer(path, separator);
    while (tokenizer.hasMoreTokens()) 
      imports.addElement(tokenizer.nextToken());
    initialised = true;
  }
  public static ClassProxy forName(String classname) throws ClassNotFoundException {
	  return forName((Object) null, classname);
  }
  public static ClassProxy forName(ClassProxy referenceClass, String classname) throws ClassNotFoundException {
	  
		  
	  if (referenceClass instanceof RemoteClassProxy) {
		  return forName (((RemoteClassProxy) referenceClass).getFactoryName(), classname);
	  }
	  else 
		  return forName(classname);
  }
  public static ClassProxy forName(Object remoteFactory, String classname) throws ClassNotFoundException {

    if (!initialised) {
     init();
    }
    
   
    String name;
    ClassProxy c;
    // forget about interfaces for now
    try {
    	if (remoteFactory != null)
    	     c = RemoteSelector.forName(classname);
    	else
    		c = AClassProxy.staticForName(classname);
    	
    } catch (Exception e) {
    	c = null;
    }
    if (c != null) return c;
    for (int i=-1; i < imports.size(); i++) {
      if (i == -1)
	name = uiClassMapper.toClassName (classname);
      else
	name = (String) imports.elementAt(i)+"."+classname;
      try {
	c = RemoteSelector.forName(name);
	return c;
      //} catch (ClassNotFoundException e) {
      } catch (Exception e) {
		  Object o = nameToClass.get(classname);		  if (o instanceof Class)			  return (ClassProxy) o;
      }
    }
    //throw new ClassNotFoundException("uiClassFinder couldnt find "+classname+" in the current class path");	throw new ClassNotFoundException(classname);
  }  public static String toLongName(String classname) throws ClassNotFoundException {

    if (!initialised) {
     init();
    }

   
    String name;
    ClassProxy c;
    for (int i=-1; i < imports.size(); i++) {
      if (i == -1)
	name = uiClassMapper.toClassName (classname);
      else
	name = (String) imports.elementAt(i)+"."+classname;
      try {
	c = RemoteSelector.forName(name);
	return name;
      //} catch (ClassNotFoundException e) {
      } catch (Exception e) {
      }
    }
    return null;
  }
}
