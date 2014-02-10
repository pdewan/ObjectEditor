
// The event structure used to
// communicate the addition of
// a component to a composite type
// (Vector ?)
package bus.uigen;

public class AddComponentEvent {
  private Object obj;
  private int    position;
  AddComponentEvent(int posn, Object newobj) {
   obj   = newobj;
    position = posn;
  }

  public Object getObject() {
    return obj;
  }

  public int getPosition() {
    return position;
  }
}

