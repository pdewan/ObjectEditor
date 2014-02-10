package bus.uigen.attributes;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;

public class HashtableToInstanceAttributes {

  public static ObjectAdapter pathToAdapter(String path,
					ObjectAdapter top,
					Hashtable cache) {
    // Ignore the cache for now. Dont think it will be useful
    // anyway
    StringTokenizer tokenizer = new StringTokenizer(path, AttributeNames.PATH_SEPARATOR);
    ObjectAdapter adaptor = top;
    while (tokenizer.hasMoreTokens() &&
	    adaptor != null) {
      adaptor = ((CompositeAdapter) adaptor).getChildAdapterMapping(tokenizer.nextToken());
    }
    return adaptor;
  }

  public static void setHashtable(ObjectAdapter adaptor, Hashtable table) {
    Hashtable mapping = new Hashtable();
    Enumeration keys = table.keys();
    while (keys.hasMoreElements()) {
      String path = (String) keys.nextElement();
      Vector v    = (Vector) table.get(path);
      ObjectAdapter a = pathToAdapter(path, adaptor, mapping);
      if (a != null) {
	//a.setAttributes(v);		  a.setLocalAttributes(v);
      }
    }
  }
}
