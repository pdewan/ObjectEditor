
// uiClassAttributeManager
// Maintains Class level attributes.


package bus.uigen.attributes;
import java.util.*;
import java.io.*;import bus.uigen.introspect.*;import bus.uigen.*;


public class ClassAttributeManager implements AttributeCollection, Serializable {
  
  Vector attributes;
  String forClass;

  public ClassAttributeManager(String c) {
    attributes = new Vector();
    forClass = c;
  }

  public Vector getAttributes() {
    return attributes;
  }  public Vector getLocalAttributes() {
    return getAttributes();
  }

  public void addAttribute(Attribute newAttribute) {
    attributes.addElement(newAttribute);
  }
  /*
  public void setAttributes(Vector newAttributes) {
    attributes = newAttributes;
    // Propagate the updates.
    Vector list = uiFrameList.getList();
    for (int i=0; i<list.size(); i++) {
      uiFrame f = (uiFrame) list.elementAt(i);
      f.refreshAttributes(forClass);
    }
  }
  */   public void setLocalAttributes(Vector newAttributes) {
    attributes = newAttributes;
    // Propagate the updates.
    Vector list = uiFrameList.getList();
    for (int i=0; i<list.size(); i++) {
      uiFrame f = (uiFrame) list.elementAt(i);
      f.refreshAttributes(forClass);
    }
  }

  public Attribute getAttribute(String name) {
    for (int i=0; i< attributes.size(); i++) {
      Attribute a = (Attribute) attributes.elementAt(i);
      if (a.getName().equals(name))
	return a;
    }
    return null;
  }
}






