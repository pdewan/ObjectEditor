package bus.uigen.attributes;

import java.beans.*;
import java.util.*;
import util.misc.Common;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.*;

public class ViewInfoToUiClassAttributes {
  public static ClassAttributeManager oldConvert(ClassProxy c, ClassDescriptorInterface v) {

    ClassAttributeManager m = new ClassAttributeManager(c.getName());
    String name, an;
    PropertyDescriptorProxy[] properties = v.getPropertyDescriptors();
    FieldDescriptorProxy[] fields = v.getFieldDescriptors();

    if (properties != null) {
      for (int i=0; i<properties.length; i++) {
	name = properties[i].getName()+AttributeNames.PATH_SEPARATOR;
	Enumeration anames = properties[i].attributeNames();
	while (anames.hasMoreElements()) {
	  an = (String) anames.nextElement();
	  m.addAttribute(new Attribute(name+an, 
				       properties[i].getValue(an)));
	}
	// Add default attributes for elideString and label
	if (m.getAttribute(name+AttributeNames.LABEL) == null) 
			m.addAttribute(new Attribute(name+AttributeNames.LABEL, 
				  Common.beautify (properties[i].getName(), properties[i].getDisplayName())));
      }
    }
    
    if (fields != null) {
      for (int i=0; i<fields.length; i++) {
	name = fields[i].getName()+AttributeNames.PATH_SEPARATOR;
	Enumeration anames = fields[i].attributeNames();
	while (anames.hasMoreElements()) {
	  an = (String) anames.nextElement();
	  m.addAttribute(new Attribute(name+an, 
				       fields[i].getValue(an)));
	}
	// Add default attributes for elideString and label
	if (m.getAttribute(name+AttributeNames.LABEL) == null)
	  //m.addAttribute(new Attribute(name+AttributeNames.LABEL, fields[i].getDisplayName()));		
	    m.addAttribute(new Attribute(name+AttributeNames.LABEL, Common.beautify(fields[i].getName(), fields[i].getDisplayName())));		
      }
    }

    // Add default ELIDESTR attribute (set to classname)
    if (m.getAttribute(AttributeNames.ELIDE_STRING) == null)
      m.addAttribute(new Attribute(AttributeNames.ELIDE_STRING,
				   " < "+ AClassDescriptor.getMethodsMenuName(c)+"... >"));

    // Add the class attributes
    BeanDescriptorProxy bd = v.getBeanDescriptor();
    if (bd != null) {
       Enumeration keys = bd.attributeNames();
       while (keys.hasMoreElements()) {
	 String att = (String) keys.nextElement();
	 Object value = bd.getValue(att);
	 m.addAttribute(new Attribute(att, value));
       }
    }


    return m;
  }
  public static ClassAttributeManager convert(ClassProxy c, ClassDescriptorInterface v) {
	  return convert (c, c.getName(), v);
	  /*

	    uiClassAttributeManager m = new uiClassAttributeManager(c.getName());
	    String name, an;
	    PropertyDescriptor[] properties = v.getPropertyDescriptors();
	    FieldDescriptor[] fields = v.getFieldDescriptors();

	    if (properties != null) {
	      for (int i=0; i<properties.length; i++) {
		name = properties[i].getName()+AttributeNames.PATH_SEPARATOR;
		Enumeration anames = properties[i].attributeNames();
		while (anames.hasMoreElements()) {
		  an = (String) anames.nextElement();
		  m.addAttribute(new Attribute(name+an, 
					       properties[i].getValue(an)));
		}
		// Add default attributes for elideString and label
		if (m.getAttribute(name+AttributeNames.LABEL) == null) 
				m.addAttribute(new Attribute(name+AttributeNames.LABEL, 
					  uiGenerator.beautify (properties[i].getName(), properties[i].getDisplayName())));
	      }
	    }
	    
	    if (fields != null) {
	      for (int i=0; i<fields.length; i++) {
		name = fields[i].getName()+AttributeNames.PATH_SEPARATOR;
		Enumeration anames = fields[i].attributeNames();
		while (anames.hasMoreElements()) {
		  an = (String) anames.nextElement();
		  m.addAttribute(new Attribute(name+an, 
					       fields[i].getValue(an)));
		}
		// Add default attributes for elideString and label
		if (m.getAttribute(name+AttributeNames.LABEL) == null)
		  //m.addAttribute(new Attribute(name+AttributeNames.LABEL, fields[i].getDisplayName()));		
		    m.addAttribute(new Attribute(name+AttributeNames.LABEL, uiGenerator.beautify(fields[i].getName(), fields[i].getDisplayName())));		
	      }
	    }

	    // Add default ELIDESTR attribute (set to classname)
	    if (m.getAttribute(AttributeNames.ELIDESTRING) == null)
	      m.addAttribute(new Attribute(AttributeNames.ELIDESTRING,
					   " < "+ ClassDescriptor.getMethodsMenuName(c)+"... >"));

	    // Add the class attributes
	    BeanDescriptor bd = v.getBeanDescriptor();
	    if (bd != null) {
	       Enumeration keys = bd.attributeNames();
	       while (keys.hasMoreElements()) {
		 String att = (String) keys.nextElement();
		 Object value = bd.getValue(att);
		 m.addAttribute(new Attribute(att, value));
	       }
	    }


	    return m;
	    */
	  }
  //public final static String  METHOD_PREFIX = "method";
  public static ClassAttributeManager convert(ClassProxy c, String cs, ClassDescriptorInterface v) {

		ClassAttributeManager m = new ClassAttributeManager(cs);
		String name, an;
		PropertyDescriptorProxy[] properties = v.getPropertyDescriptors();
		PropertyDescriptorProxy[] indexOrKeys = v.getIndexOrKeyPropertyDescriptors();
		FieldDescriptorProxy[] fields = v.getFieldDescriptors();
		
		MethodDescriptorProxy[] methods = v.getMethodDescriptors();
		//adding this to integrate methods with properties
		if (methods != null) {
			for (int i = 0; i < methods.length; i++) {
				name = AttributeManager.METHOD_PREFIX + methods[i].getName()  + AttributeNames.PATH_SEPARATOR;
				Enumeration anames = methods[i].attributeNames();
				while (anames.hasMoreElements()) {
					an = (String) anames.nextElement();
					m.addAttribute(new Attribute(name + an, methods[i]
							.getValue(an)));
				}
				// Add default attributes for elideString and label
				// will handle this in getLabel
				/*
				 * if (m.getAttribute(name+AttributeNames.LABEL) == null)
				 * m.addAttribute(new Attribute(name+AttributeNames.LABEL,
				 * uiGenerator.beautify (properties[i].getName(),
				 * properties[i].getDisplayName())));
				 */
			}

		}

		if (properties != null) {
			for (int i = 0; i < properties.length; i++) {
				name = properties[i].getName() + AttributeNames.PATH_SEPARATOR;
				Enumeration anames = properties[i].attributeNames();
				while (anames.hasMoreElements()) {
					an = (String) anames.nextElement();
					m.addAttribute(new Attribute(name + an, properties[i]
							.getValue(an)));
				}
				// Add default attributes for elideString and label
				// will handle this in getLabel
				/*
				 * if (m.getAttribute(name+AttributeNames.LABEL) == null)
				 * m.addAttribute(new Attribute(name+AttributeNames.LABEL,
				 * uiGenerator.beautify (properties[i].getName(),
				 * properties[i].getDisplayName())));
				 */
			}

		}
		if (indexOrKeys != null) {
			for (int i = 0; i < indexOrKeys.length; i++) {
				name = indexOrKeys[i].getName() + AttributeNames.PATH_SEPARATOR;
				Enumeration anames = indexOrKeys[i].attributeNames();
				while (anames.hasMoreElements()) {
					an = (String) anames.nextElement();
					m.addAttribute(new Attribute(name + an, indexOrKeys[i]
							.getValue(an)));
				}
				// Add default attributes for elideString and label
				// will handle this in getLabel
				/*
				 * if (m.getAttribute(name+AttributeNames.LABEL) == null)
				 * m.addAttribute(new Attribute(name+AttributeNames.LABEL,
				 * uiGenerator.beautify (properties[i].getName(),
				 * properties[i].getDisplayName())));
				 */
			}

		}

		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				name = fields[i].getName() + AttributeNames.PATH_SEPARATOR;
				Enumeration anames = fields[i].attributeNames();
				while (anames.hasMoreElements()) {
					an = (String) anames.nextElement();
					m.addAttribute(new Attribute(name + an, fields[i]
							.getValue(an)));
				}
				// Add default attributes for elideString and label
				if (m.getAttribute(name + AttributeNames.LABEL) == null)
					// m.addAttribute(new Attribute(name+AttributeNames.LABEL,
					// fields[i].getDisplayName()));
					m.addAttribute(new Attribute(name + AttributeNames.LABEL,
							Common.beautify(fields[i].getName(), fields[i]
									.getDisplayName())));
			}
		}

		// Add default ELIDESTR attribute (set to classname)
		String methodsMenuName;
		if (c == null)
			methodsMenuName = AClassDescriptor.getMethodsMenuName(cs);
		else
			methodsMenuName = AClassDescriptor.getMethodsMenuName(c);
		/*
		 * if (m.getAttribute(AttributeNames.ELIDESTRING) == null)
		 * m.addAttribute(new Attribute(AttributeNames.ELIDESTRING, //" < "+
		 * ClassDescriptor.getMethodsMenuName(c)+"... >")); " < "+
		 * methodsMenuName+"... >"));
		 */

		// Add the class attributes
		BeanDescriptorProxy bd = v.getBeanDescriptor();
		if (bd != null) {
			Enumeration keys = bd.attributeNames();
			while (keys.hasMoreElements()) {
				String att = (String) keys.nextElement();
				Object value = bd.getValue(att);
				m.addAttribute(new Attribute(att, value));
			}
		}

		return m;
	}  public static String beautify (String name) {	String label = "" + Character.toUpperCase(name.charAt(0));
	char c;	for (int nameIndex = 1; nameIndex < name.length();nameIndex++) {		if (Character.isUpperCase( c = name.charAt(nameIndex)))			label = label + " ";		label = label + c;	} 
	return label;  }
}
