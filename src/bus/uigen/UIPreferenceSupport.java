package bus.uigen;
import java.lang.reflect.*;
import java.util.*;

import bus.uigen.ars.*;

class UIPreferenceSupport implements UIPreference {
  
  Class widgetClass;
  Class adaptorClass;
  Hashtable attributes = new Hashtable();

  public Class getPreferredWidgetClass() {
    return widgetClass;
  }
  public void setPreferredWidgetClass(Class wc) {
    widgetClass = wc;
  }
  public Class getPreferredAdapterClass() {
    return adaptorClass;
  }
  public void setPreferredAdapterClass(Class ac) {
    adaptorClass = ac;
  }

  public boolean setPreferredWidgetClass(String wc) {
    try {
      widgetClass = Class.forName(wc);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
  
  public boolean setPreferredAdapterClass(String wc) {
    try {
      adaptorClass = Class.forName(wc);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  public void setAttribute(String name, String value) {
    attributes.put(name, value);
  }

  public Enumeration getAttributes() {
    return attributes.keys();
  }
  
  public String getAttributeValue(String name) {
    if (attributes.containsKey(name))
        return (String)(attributes.get(name));
    else {
        System.out.println("Attrib "+name+" not found");
        return ("");
    }
  }
}


