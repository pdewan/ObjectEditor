// LocalAttributeDescriptor
// Used tp implement instance level attributes.

package bus.uigen.attributes;

import java.util.*;
import java.io.*;

import bus.uigen.introspect.Attribute;

public class LocalAttributeDescriptor implements Serializable {
  
  private transient Vector listeners = new Vector();
  private Vector attributes = new Vector();

  private Hashtable classAttributes = new Hashtable();

  public LocalAttributeDescriptor() {
  }
  
  public void addLocalAttributeListener(LocalAttributeListener l) {
    listeners.addElement(l);
  }
  
  private void updateListeners(Attribute evt) {
    Vector l;
    for (int i=0; i< listeners.size(); i++) {
      LocalAttributeListener p = (LocalAttributeListener) listeners.elementAt(i);
      p.localAttributeChanged(evt);
    }
  }

  private Attribute mySetAttribute(Vector list, String name, Object value) {
    for (int i=0; i<list.size(); i++) {
      Attribute a = (Attribute) list.elementAt(i);
      if (a.getName().equals(name)) {
	a.setValue(value);
	return a;
      }
    }
    // New attribute
    Attribute a = new Attribute(name, value);
    list.addElement(a);
    return a;
  }

  
  public void setAttribute(String name, Object value) {
    Attribute a = mySetAttribute(attributes, name, value);
    updateListeners(a);
  }

  public void setFieldAttribute(String field, String name, Object value) {
    String n = field+AttributeNames.PATH_SEPARATOR+name;
    setAttribute(n, value);
  }

  public Object getAttribute(String name) {
    for (int i=0; i< attributes.size(); i++) {
      Attribute a = (Attribute) attributes.elementAt(i);
      if (a.getName().equals(name))
	return a.getValue();
    }
    return null;
  }

  public Vector getAttributes() {
    return attributes;
  }
  
  public Object getFieldAttribute(String field, String name) {
    String n = field+AttributeNames.PATH_SEPARATOR+name;
    return getAttribute(n);
  }
  /*
  public void setClassAttribute(String classname, String name, Object value) {
    Vector cv = (Vector) classAttributes.get(classname);
    if (cv == null) {
      cv = new Vector();
      classAttributes.put(classname, cv);
    }
    mySetAttribute(cv, name, value);
  }

  public void setClassFieldAttribute(String classname, String field, String name, Object value) {
    setClassAttribute(classname, field+AttributeNames.PATH_SEPARATOR+name, value);
  }
  
  public Hashtable getClassAttributes() {  
    return classAttributes;
  }*/

}
