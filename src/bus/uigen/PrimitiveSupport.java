// Support class for building new primitive types
// Extend this class and implement the abstract
// methods
package bus.uigen;
import java.util.*;

public abstract class PrimitiveSupport extends UIPreferenceSupport implements Primitive {
  Vector listeners = new Vector();
  
  // "Stringify" the object.
  // This operation, should return a (human consumable)
  // string, which is directly displayed by the 
  // selected widget 
  abstract public String toString();
  // Change our state depending on this value that
  // has come in (from one of the UIs)
  abstract public void setState(String newval);
  
  public void objectValueChanged(ValueChangedEvent sevt) {
    try {
      setState((String) sevt.getNewValue());
      notifyListeners(sevt);
    } catch (ClassCastException e) {
    }
  }

  public void addObjectValueChangedListener(ObjectValueChangedListener l) {
    listeners.addElement(l);
  }
  public void notifyListeners(ValueChangedEvent sevt) {
    for (int i=0; i< listeners.size(); i++) {
      ObjectValueChangedListener l = 
	(ObjectValueChangedListener)listeners.elementAt(i);
	l.objectValueChanged(sevt);
    }
  }

  public void objectValueChanged() {
      ValueChangedEvent evt = new ValueChangedEvent(toString());
      objectValueChanged(evt);
  }
}
