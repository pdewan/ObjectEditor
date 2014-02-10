package bus.uigen.introspect;

import java.util.Hashtable;
import java.util.Set;

import util.misc.HashIdentityMap;
import util.misc.IdentityMap;
import bus.uigen.ObjectEditor;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.DynamicMethods;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
//import bus.uigen.undo.CommandListener;
//import bus.uigen.viewgroups.APropertyAndCommandFilter;
import bus.uigen.reflect.local.ReflectUtil;

public class ClassDescriptorCache {
  private static IdentityMap  cache = new HashIdentityMap();  private static Hashtable subclasses = new Hashtable();    public static void clear() {
	  cache.clear();
  }
  public static ClassDescriptorInterface getClassDescriptor(Class c) {
	  return getClassDescriptorAndRegisterAttributes(AClassProxy.classProxy(c), null);
  }
  
  public static ClassDescriptorInterface getClassDescriptor(ClassProxy c) {
	  return getClassDescriptorAndRegisterAttributes(c, null);
	  /*
	 init();
    ViewInfo cd = (ViewInfo) cache.get(c);
    if (cd == null) {
    	
      cd = ViewIntrospector.getViewInfo(c);
    	
    		
    	
      cache.put(c, cd);
	  AMenuDescriptor.registerAttributeRegistrer(c);
    }
      //initRightMenu(c, cd, null);
    
    return cd;
    */
  }
  public static ClassDescriptorInterface getClassDescriptorAndRegisterAttributes(ClassProxy c, Object theFrame) {
		 init();
	    ClassDescriptorInterface cd = (ClassDescriptorInterface) cache.get(c);
	    if (cd == null) {
	    	
	      cd = ViewIntrospector.getViewInfo(c);
	    	
	    		
	    	
	      cache.put(c, cd);
		  AttributeRegistry.registerAttributeRegistrer(c, theFrame);
	    }
	      //initRightMenu(c, cd, null);
	    
	    return cd;
	  }
  
  
  static boolean initialized = false;
  static void init () {
	  if (initialized) return;
	  	ObjectEditor.registerAttributes();
	  	//ObjectEditor.registerModels();
		initialized = true;
  }
  public static ClassDescriptorInterface getClassDescriptor(String c) {
	  init();
	  ClassDescriptorInterface cd = (ClassDescriptorInterface) cache.get(c);
	  if (cd != null) return cd;
	  ClassProxy cClass = null;
	  try {
		  cClass = RemoteSelector.forName(c);
		  cd = (ClassDescriptorInterface) cache.get(cClass);
		  if (cd == null)
			  cd =  ViewIntrospector.getViewInfo(cClass);
		 return cd;
		  
	  } catch (Exception e) {		  
		  return null;
	  }
	  
	 
	    //return (ViewInfo) cache.get(c);
	  }
  public static ClassDescriptorInterface getClassProxyDescriptor(String c) {
	  init();
	  ClassDescriptorInterface cd = (ClassDescriptorInterface) cache.get(c);
	  if (cd != null) return cd;
	  ClassProxy cClass = null;
	  try {
		  cClass = RemoteSelector.forName(c);
		  cd = (ClassDescriptorInterface) cache.get(cClass);
		  if (cd == null)
			  cd =  ViewIntrospector.getViewInfo(cClass);
		 return cd;
		  
	  } catch (Exception e) {		  
		  return null;
	  }
	  
	 
	    //return (ViewInfo) cache.get(c);
	  }
  public static ClassDescriptorInterface getClassDescriptor(Object prototype) {
	  init();
	  if (prototype == null) return null;
	  /*
	  String virtualClass = uiBean.getVirtualClass(prototype);
	  	if (virtualClass != null)
	  		return ClassDescriptorCache.getVirtualClassDescriptor(virtualClass);
	  	else 
	  	*/
	  	//return getClassDescriptor(ClassDescriptor.getTargetClass(prototype), prototype);
	  	return getClassDescriptor(RemoteSelector.getClass(prototype), prototype);
//	  	return getClassDescriptor(ReflectUtil.toMaybeProxyTargetClass(prototype),  prototype);

  }
  // reverting back to this one
  public static ClassDescriptorInterface workingGetClassDescriptor(ClassProxy c, Object prototype) {
	  init();
	  String virtualClass = IntrospectUtility.getVirtualClass(prototype);
	   ClassDescriptorInterface cd;
	   //Class c = APropertyAndCommandFilter.class;
	   //Class c = prototype.getClass();
	   if (virtualClass == null) {
	     cd = (ClassDescriptorInterface) cache.get(c);
	   } else
		   cd = (ClassDescriptorInterface) cache.get(virtualClass);
	    if (cd == null) {
	    	cd = ViewIntrospector.getViewInfo(c, prototype, virtualClass);
	    	/*
	    	if (virtualClass == null)
	    		cd = ViewIntrospector.getViewInfo(c);
	    	else
	    		cd = ViewIntrospector.getViewInfo(c, prototype, virtualClass);
	    	*/
	      //cache.put(c, cd);
	      if (virtualClass != null)
	    	  cache.put(virtualClass,cd );
	      else
	    	  cache.put(c, cd);
	      AttributeRegistry.registerAttributeRegistrer(c, null);
	      //initRightMenu(c, cd, null);
	    }
	   cd.setPrototypeObject(prototype);
	    return cd;
	  }
  public static ClassDescriptorInterface getClassDescriptor(ClassProxy c, Object prototype) {
	  prototype = ACompositeLoggable.maybeExtractRealObject(prototype);
	  init();
	  String virtualClassString = IntrospectUtility.getVirtualClass(prototype);
	  if (virtualClassString == null && DynamicMethods.hasDynamicCommands(c)) {
		 virtualClassString = c.getName() + ":" + prototype;
	  }
	  ClassProxy virtualClass = null;
	 
	  try {
		  //should use RemoteSelector 
		  //virtualClass = Class.forName(virtualClassString);
		  if (virtualClassString != null)
		  virtualClass = RemoteSelector.forName(virtualClassString);
	  } catch (Exception e) {
		  
	  }
	  //previous code for some reason set the string to null. It seems the if was
	  //never taken. We will assume this is a remote proxy object and we should
	  // get methods of reall class
	  if (virtualClass != null && virtualClass != c) {
		  virtualClassString = null;
		  // does not allow prototype to be used
		  //return getClassDescriptor(virtualClass);
		  return getClassDescriptor(virtualClass, prototype);
	  }
	   ClassDescriptorInterface cd;
	   //Class c = APropertyAndCommandFilter.class;
	   //Class c = prototype.getClass();
	   if (virtualClassString == null ) {
	     cd = (ClassDescriptorInterface) cache.get(c);
	   } else //if (virtualClass == null)
		   cd = (ClassDescriptorInterface) cache.get(virtualClassString);
	  
	    if (cd == null) {
	    	cd = ViewIntrospector.getViewInfo(c, prototype, virtualClassString);
	    	/*
	    	if (virtualClass == null)
	    		cd = ViewIntrospector.getViewInfo(c);
	    	else
	    		cd = ViewIntrospector.getViewInfo(c, prototype, virtualClass);
	    	*/
	      //cache.put(c, cd);
	      if (virtualClassString != null )
	    	  cache.put(virtualClassString,cd );
	      else
	    	  cache.put(c, cd);
	      AttributeRegistry.registerAttributeRegistrer(c, null);
	      //initRightMenu(c, cd, null);
	    }
	   cd.setPrototypeObject(prototype);
	    return cd;
	  }
  
  public static ClassDescriptorInterface getVirtualClassDescriptor(String virtualClass, ClassProxy c) {
	  init();
	  ClassDescriptorInterface cd;
		   cd = (ClassDescriptorInterface) cache.get(virtualClass);

	    	//ClassProxy c = AClassProxy.classProxy(APropertyAndCommandFilter.class);
	    if (cd == null) {
	    	cd = ViewIntrospector.getViewInfo(c, null, virtualClass);
	    	/*
	    	if (virtualClass == null)
	    		cd = ViewIntrospector.getViewInfo(c);
	    	else
	    		cd = ViewIntrospector.getViewInfo(c, prototype, virtualClass);
	    	*/
	      //cache.put(c, cd);
	      if (virtualClass != null)
	    	  cache.put(virtualClass,cd );
	      //initRightMenu(c, cd, null);
	    }
	   //cd.setPrototypeObject(prototype);
	    return cd;
	  }
//  public static ViewInfo getVirtualClassDescriptor(String virtualClass) {
//	  init();
//	  ViewInfo cd;
//		   cd = (ViewInfo) cache.get(virtualClass);
//
//	    	ClassProxy c = AClassProxy.classProxy(APropertyAndCommandFilter.class);
//	    if (cd == null) {
//	    	cd = ViewIntrospector.getViewInfo(c, null, virtualClass);
//	    	/*
//	    	if (virtualClass == null)
//	    		cd = ViewIntrospector.getViewInfo(c);
//	    	else
//	    		cd = ViewIntrospector.getViewInfo(c, prototype, virtualClass);
//	    	*/
//	      //cache.put(c, cd);
//	      if (virtualClass != null)
//	    	  cache.put(virtualClass,cd );
//	      //initRightMenu(c, cd, null);
//	    }
//	   //cd.setPrototypeObject(prototype);
//	    return cd;
//	  }
  public static Set<Object> getClasses() {
    return cache.keySet();
  }
      public static boolean toBoolean(Object val) {
	  if (val == null)
		  return false;
	  if (val instanceof String)		  //return Boolean.getBoolean((String) val);
		  return ((String) val).startsWith("t") ||  ((String) val).startsWith("T");
	  if (val instanceof Boolean)
		  return ((Boolean) val).booleanValue();
	  return false;  }  public static int toInt(Object val) {
	  if (val == null)
		  return -1;
	  if (val instanceof String)		  return Integer.parseInt((String) val);
		  //return ((String) val).startsWith("t") ||  ((String) val).startsWith("T");
	  if (val instanceof Integer)
		  return ((Integer) val).intValue();
	  return 0;  }  /*
  //boolean rightMenuInitialized = false;  public static void initRightMenu(Class c, 
				  CommandListener cl) {	  initRightMenu(c, (ViewInfo) cache.get(c), cl);	  //initRightMenu(c,  cl);
  }	  

   static void initRightMenu(Class c, 
				    ViewInfo cd, CommandListener cl) {	//if (rightMenuInitialized) return;	//rightMenuInitialized = true;
    MethodDescriptor[] m = cd.getMethodDescriptors();
    if (m == null)
      return;	RightMenu menu = RightMenuCache.createRightMenu(cl);
    for (int i=0; i<m.length; i++) {
      //if (m[i].getValue(AttributeNames.RIGHT_MENU) != null)	if (toBoolean(m[i].getValue(AttributeNames.RIGHT_MENU)))
	RightMenuCache.addRightMenuMethod(menu, c, 
					  m[i].getMethod(),
					  m[i].getDisplayName(), cl);
    }
  }  */
}
