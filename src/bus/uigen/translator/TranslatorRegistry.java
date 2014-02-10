package bus.uigen.translator;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.introspect.DefaultRegistry;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.visitors.ToTextAdapterVisitor;

// Register classes that translate between classes

public class TranslatorRegistry {
  
  private static DefaultRegistry registry = new DefaultRegistry();

   
  public static void register(String from, String to, Class translator) throws IllegalTranslatorClassException {
    registry.register(from, to, translator);
  }

  public static void register(String from, String to, String translator) throws ClassNotFoundException, IllegalTranslatorClassException {
    registry.register(from, to, Class.forName(translator));
  }
  
  public static void register(String from, String to, Translator translator) {
    registry.register(from, to, translator);
  }
  
  
  public static Object getTranslator(String from, String to) {
    return registry.getTranslator(from, to);
  }

  // Convenience method
  private static Hashtable cache = new Hashtable(32);
  
  public static Object convert(String to, Object obj) throws FormatException {	//System.out.println ("converting" + to + "obj" + obj);	  if (obj == null) return null;
    return convert(obj.getClass().getName(), to, obj);
  }
  public static Object convert(String to, Object obj, ObjectAdapter adapter) throws FormatException {
		//System.out.println ("converting" + to + "obj" + obj);
		  if (obj == null) return null;
	    return convert(obj.getClass().getName(), to, obj, adapter);
	  }
  public static Object convert(String from, String to, Object obj) throws FormatException {
	  return convert (from, to, obj, null);
  
  }
  public static Object convert(String from, String to, Object obj, ObjectAdapter adapter) throws FormatException {
    //System.out.println("converting from" + from + "to" + to + "obj" + obj);	  if (to.equals(from)) return obj;
    try {
      Class cto = Class.forName(to);
      Class cfrom = Class.forName(from);
      if (cto.isAssignableFrom(cfrom))
	return obj;
    } catch (Exception e) {}

    String key = from+" "+to;
    Translator t = (Translator) cache.get(key);
    if (t == null) {
      Object temp = getTranslator(from, to);
      if (temp == null) {
        // One case we can take care of is if the to type is String
        if ("java.lang.String".equals(to)) {
        	if (!adapter.unparseAsToString() && (! (adapter instanceof PrimitiveAdapter)))
        		return toFormattedText(adapter);
        	else
        		return obj.toString();
        } else
	   return null;
      }
      if (temp instanceof Translator) t = (Translator) temp;
      else if (temp instanceof Class) {
	t = new ClassTranslator((Class) temp);
      }
      else return null;
      cache.put(key, t);
    }
    return t.translate(obj);
  }
  public static String toFormattedText(ObjectAdapter adapter) {
	  Vector results = new Vector();
  (new ToTextAdapterVisitor(adapter)).traverse(adapter, results, 0, 0);
  StringBuffer sb = new StringBuffer();
  for (int i = 1; i < results.size(); i++ ) {
		sb.append((String) results.elementAt(i));
		String temp = (String) results.elementAt(i);
		//System.out.println(temp);
		//if (i < results.size() -1) sb.append("\n");
		//System.out.println(results.elementAt(i));
	}
  return sb.toString();
	
  }
}


