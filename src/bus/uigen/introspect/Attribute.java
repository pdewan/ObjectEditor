package bus.uigen.introspect;
import java.io.*;
//
// Attribute
import java.util.Vector;

import bus.uigen.attributes.AttributeNames;


public class Attribute implements Cloneable, Serializable {
  private String name;
  private Object value;
  private int type;
  public enum DefinitionKind {Property, Class, VirtualClass, ViewClass, ViewVirtualClass, SystemDefault, UserDefault,  Computed};
  DefinitionKind definitionKind = DefinitionKind.Property;
  public static final int LOCAL_TYPE = 0, INHERITED_TYPE = 1, ALL_STARS = 2;
  public boolean CHANGED = false;

  
  // Properties of the Attribute
  private boolean editable = true;

  public Attribute(String n, Object v) {
    name = n;
    value = v;
    type = LOCAL_TYPE;
  }

  public Attribute(String n, Object v, int theType) {
    name = n;
    value = v;
    type = theType;
  }

  public int getType() {
    return type;
  }

  public void setType(int theType) {
    type = theType;
  }

  public String getFieldName() {
    int index = name.lastIndexOf(AttributeNames.PATH_SEPARATOR);
    if (index == -1)
      return null;
    else {
      return name.substring(0, index);
      }
  }

  public String getAttributeName() {
    int index = name.lastIndexOf(AttributeNames.PATH_SEPARATOR);
    if (index == -1)
      //return null;
    	return name;
      else
	return name.substring(index+1);
  }
  

  public void setEditable(boolean value) {
    editable = value;
  }
  
  public boolean isEditable() {
    return editable;
  }

  public void setValue(Object newValue) {
    value = newValue;
  }

  public Object getValue() {
    return value;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String n) {
    name = n;
  }
  public DefinitionKind getDefinitionKind() {
	    return definitionKind;
	  }
	  
  public void setDefinitionKind(DefinitionKind newVal) {
	    definitionKind = newVal;
  }

  public Object clone() {
    try{
      return super.clone();
    } catch (Exception e) {
      return(null);
    }
  }
  public String toString() {
	  if (name != null && value != null)
		  return name + ":" + value;
	  else
		  return super.toString();
  }
  public static Object getAttribute (Vector<Attribute> list, String name) {
	  for (int i = 0; i < list.size(); i++) {
		  Attribute attr = list.elementAt(i);
		  if (attr.getName().equals(name))
			  return attr.getValue();
	  }
	  return null;
	  
  }
}
