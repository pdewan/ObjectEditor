
// uiClassAttributeManager
// Maintains Class level attributes.


package bus.uigen.attributes;
import java.util.*;
import java.io.*;
import bus.uigen.editors.*;import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.*;

public class UIAttributeManager implements Serializable {
  Hashtable attributeLists;
  
  public UIAttributeManager() {
    attributeLists = new Hashtable();
    // not clear we do anything with defaults and it prematurely builds up attribute lists
    //readDefaults();
  }
  public void removeFromAttributeLists (String className) {
	  attributeLists.remove(className);
  }
  
  private void readDefaults() {
    ClassAttributeManager m;
    Vector v;
    ComponentDictionary cd = EditorRegistry.getComponentDictionary();
    Hashtable componentMapping = cd.getDefaultComponentMapping();
    Enumeration keys = componentMapping.keys();
    while (keys.hasMoreElements()) {
    	Object nextKey = keys.nextElement();
    	String cl;
    	if (nextKey instanceof String)
    		cl = (String) nextKey;
    	else
    		continue;
      //String cl = (String) keys.nextElement();
      m = getClassAttributeManager(cl);
      v = m.getAttributes();
      //v.addElement(new Attribute("preferredWidget", cd.getDefaultComponent(cl)));
    }	/*
    m = getClassAttributeManager("java.util.Vector");
    v = m.getAttributes();    v.addElement(new Attribute("direction", "vertical"));	
		m = getClassAttributeManager("java.util.Hashtable");
    v = m.getAttributes();    v.addElement(new Attribute("direction", "vertical"));	*/
    	m = getClassAttributeManager("java.lang.Number");
    v = m.getAttributes();    //v.addElement(new Attribute(AttributeNames.NUM_COLUMNS, new Integer(15)));
	//v.addElement(new Attribute(AttributeNames.DECINCUNIT, new Integer(1)));

//do something w/ boolean here?	
		m = getClassAttributeManager("java.lang.String");
    v = m.getAttributes();    //v.addElement(new Attribute(AttributeNames.NUM_COLUMNS, new Integer(15)));
    //m = getClassAttributeManager("java.lang.String");	/*
    v = m.getAttributes();
    v.addElement(new Attribute("columns", new Integer(20)));	*/
  }
  /*
  public uiClassAttributeManager getClassAttributeManager(String c) {
    uiClassAttributeManager m = (uiClassAttributeManager) attributeLists.get(c);
    if (m == null) {
      // Hack to get class attributes initialised from
      // the BeanInfo
    	ViewInfo cd = (ViewInfo) ClassDescriptorCache.getClassDescriptor(c);
    	
      try {
	Class cl = Class.forName(c);
        cd = (ViewInfo) ClassDescriptorCache.getClassDescriptor(cl);
	m = ViewInfoToUiClassAttributes.convert(cl, cd);
      } catch (Exception e) {
	m = new uiClassAttributeManager(c);
      }
      attributeLists.put(c, m);
    }
    return m;
  }
  */
  public ClassAttributeManager getVirtualClassAttributeManager(String c) {
	    ClassAttributeManager m = (ClassAttributeManager) attributeLists.get(c);
	    ClassDescriptorInterface cd;
	    if (m == null) {	      
	    	 cd = (ClassDescriptorInterface) ClassDescriptorCache.getClassDescriptor(c);
	    	 m = ViewInfoToUiClassAttributes.convert(null, c, cd);
	    } 
	      attributeLists.put(c, m); 
  		
	    return m;
	  }
	    public ClassAttributeManager getClassAttributeManager(String c) {
	        ClassAttributeManager m = (ClassAttributeManager) attributeLists.get(c);
	        if (m == null) {
	          // Hack to get class attributes initialised from
	          // the BeanInfo
	          try {
	    	ClassProxy cl = RemoteSelector.forName(c);
	            ClassDescriptorInterface cd = (ClassDescriptorInterface) ClassDescriptorCache.getClassDescriptor(cl);
	    	m = ViewInfoToUiClassAttributes.convert(cl, cd);
	          } catch (Exception e) {
	    	m = new ClassAttributeManager(c);
	          }
	          attributeLists.put(c, m);
	        }
	        return m;
	    }
	    
	    public void clearClassAttributes(String c) {
	    	if (attributeLists.get(c) == null)
	    		return;
	    	attributeLists.remove(c);
	    }
  
  public Vector getClassAttributes(ClassProxy baseClass, String className, ClassProxy classProxy) {
//		ClassProxy classProxy = null;
	  if (classProxy == null)
		try {

			classProxy = RemoteSelector.forName(className);
		} catch (Exception e) {
			classProxy = PrimitiveClassList.getPrimitiveClass(baseClass,
					className);
			if (classProxy != null) {
				classProxy = PrimitiveClassList.getWrapperType(classProxy);
			} else {
				e.printStackTrace();
				return new Vector();
			}
		}
		ClassProxy superClass = classProxy.getSuperclass();
		Vector attributes;
		if (superClass == null) {
			attributes = CopyAttributeVector
					.copyVector(getClassAttributeManager(className)
							.getAttributes());
			return attributes;
		} else {
			attributes = getClassAttributes(baseClass, superClass.getName(), superClass);
			CopyAttributeVector.mergeAttributeLists(attributes,
					getClassAttributeManager(className).getAttributes());
			return attributes;
		}
	}
  public Vector getClassAttributesOld(ClassProxy baseClass, String className) {
	    ClassProxy classObject;
	    try {
	      
	      classObject = RemoteSelector.forName(className);
	    } catch (Exception e) {
	      classObject = PrimitiveClassList.getPrimitiveClass(baseClass, className);
	      if (classObject != null) {
		classObject = PrimitiveClassList.getWrapperType(classObject);
	      }
	      else {
		e.printStackTrace();
		return new Vector();
	      }
	    }
	    ClassProxy superClass = classObject.getSuperclass();
	    Vector attributes;
	    if (superClass == null) {
	      attributes =  CopyAttributeVector.copyVector(getClassAttributeManager(className).getAttributes());
	      return attributes;
	    }
	    else {
	      attributes = getClassAttributesOld(baseClass, superClass.getName());
	      CopyAttributeVector.mergeAttributeLists( attributes, 
						       getClassAttributeManager(className).getAttributes());
	      return attributes;
	    }
	  }
  public Vector getVirtualClassAttributes(String cs) {
	  return CopyAttributeVector.copyVector(getVirtualClassAttributeManager(cs).getAttributes());
  }
    public Vector getClassAttributes(String cs, ClassProxy origClass) {
    ClassProxy c;
    /*
    if (origClass == null) {
    	return CopyAttributeVector.copyVector(getVirtualClassAttributeManager(cs).getAttributes());
    }
    */
    try {
      
      c = RemoteSelector.forName(cs);
    } catch (Exception e) {		
      c = PrimitiveClassList.getPrimitiveClass(origClass, cs);
      if (c != null) {
	c = PrimitiveClassList.getWrapperType(c);
      }
      else if (origClass != null) {		  c = origClass;	  } else {
	e.printStackTrace();
	return new Vector();
      }
    }
    ClassProxy superClass = c.getSuperclass();
    Vector attributes;
    if (superClass == null) {
      attributes =  CopyAttributeVector.copyVector(getClassAttributeManager(cs).getAttributes());
      return attributes;
    }
    else {
//    	 ClassProxy[] interfaces = c.getInterfaces();
//    	 Vector allInterfaceAttributes = new Vector();
//    	for (int i = 0; i < interfaces.length;i++) {
//      	  Vector interfaceAttributes = getClassAttributes(origClass, interfaces[i].getName());
//            CopyAttributeVector.mergeAttributeLists( allInterfaceAttributes, interfaceAttributes);      					       );
//        }
      attributes = getClassAttributes(origClass, superClass.getName(), superClass);
      CopyAttributeVector.mergeAttributeLists( attributes, 
					       getClassAttributeManager(cs).getAttributes());
     
      
      return attributes;
    }
  }
  

  /*
  public Vector getClassAttributes(String c) {
    CopyAttributeVector.printAtts(getClassAttributeManager(c).getAttributes()); 
    return getClassAttributeManager(c).getAttributes();
  }*/
  

  public Enumeration getClassNames() {
    return attributeLists.keys();
  }
  
  public Attribute getAttribute(String classname, String name) {
    return getClassAttributeManager(classname).getAttribute(name);
  }
  
  public void setAttribute(String classname, Attribute a) {
    Attribute b = getAttribute(classname, a.getName());
    if (b != null) {
      b.setValue(a.getValue());
    } 
    else {
      getClassAttributeManager(classname).addAttribute(a);
    }
    return;
  }
}


