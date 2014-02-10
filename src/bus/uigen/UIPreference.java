// Interface that lets the uiGenerator
// know the fields widget preferences
package bus.uigen;
import java.util.*;

import bus.uigen.ars.*;

interface UIPreference {
  Class getPreferredWidgetClass();
  void  setPreferredWidgetClass(Class componentClass);
  Class getPreferredAdapterClass();
  void setPreferredAdapterClass(Class adaptorClass);
  void setAttribute(String name, String value);
  Enumeration getAttributes();
  String getAttributeValue(String attrib);
}

