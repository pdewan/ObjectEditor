package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.ObjectAdapter;import java.util.Vector;

public class ObjectClipboard {
  private static Vector buffer = new Vector();  static CompositeAdapter columnAdapterParent;  static CompositeAdapter columnAdapter;  static ObjectAdapter[] column;   public static CompositeAdapter getColumnAdapter() {	  return columnAdapter;  }  public static CompositeAdapter getColumnAdapterParent() {	  return columnAdapterParent;  }  public static ObjectAdapter[] getColumn() {	  return column;  }  public static void setColumn(CompositeAdapter theColumnAdapterParent,		  				 CompositeAdapter theColumnAdapter,		  				 ObjectAdapter[] theColumn) {	  columnAdapterParent = theColumnAdapterParent;	  columnAdapter = theColumnAdapter;	  column = theColumn;	  //set(column);  }
  
  public static void set(Object obj) {
    clear();
    buffer.addElement(obj);
  }  public static void set(Object[] objects) {	    clear();	    for (int i = 0; i < objects.length; i++)	    	buffer.addElement(objects[i]);	  }
  public static Object getFirst() {
    if (buffer.size() != 0)
      return buffer.elementAt(0);
    else
      return null;
  }  public static Object[] get() {	  return buffer.toArray();  }
  
  public static void clear() {
    buffer.removeAllElements();
  }
}
