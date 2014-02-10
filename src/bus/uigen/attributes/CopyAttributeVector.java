package bus.uigen.attributes;

import java.util.*;

import bus.uigen.introspect.Attribute;

public class CopyAttributeVector {
  public static Vector copyVector(Vector v) {
    Vector x = new Vector();
    for (int i=0; i<v.size(); i++) {
      Attribute a = (Attribute) v.elementAt(i);
      x.addElement(a.clone());
    }
    return x;
  }

  public static void mergeAttributeLists(Vector main, Vector specialised) {
	 if (specialised == null) return;
    int length = main.size();
    int i, j;
    Attribute a, b;
    for (i=0; i< specialised.size(); i++) {
    // make the specific one, which is earlier, win
    // tries reverssing, did not work
    //for (i=specialised.size() - 1; i >= 0; i--) {
      b = (Attribute) specialised.elementAt(i);
      for (j=0; j< length; j++) {
	a = (Attribute) main.elementAt(j);
	if (a.getName().equals(b.getName())) {
	  a.setValue(b.getValue());
	  break;
	}
      }
      if (j == length) {
	main.addElement(b);
      }
    }
  }

  public static void printAtts(Vector v) {
    Attribute a;
    for (int i=0; i<v.size(); i++) {
      a = (Attribute) v.elementAt(i);
      System.out.println(a.getName()+"="+a.getValue());
    }
  }
}
