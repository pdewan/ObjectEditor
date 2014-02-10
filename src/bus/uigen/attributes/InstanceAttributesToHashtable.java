package bus.uigen.attributes;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.CompositeAdapter;
import java.util.*;

class InstanceAttributesToHashtable {
  
  private static void processAttributedObject(Hashtable table, ObjectAdapter adaptor) {
    Vector v = adaptor.getLocalAttributes();
    String name = adaptor.getPath();
    table.put(name, v);
    if (adaptor instanceof CompositeAdapter) {
      Enumeration children = ((CompositeAdapter) adaptor).getChildAdapters();
      while (children.hasMoreElements()) {
	ObjectAdapter child = (ObjectAdapter) children.nextElement();
	processAttributedObject(table, child);
      }
    }
    return;
  }

  protected static Hashtable getHashtable(ObjectAdapter adaptor) {
    Hashtable table = new Hashtable();
    processAttributedObject(table, adaptor);
    return table;
  }
}
