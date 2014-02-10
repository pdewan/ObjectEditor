
package bus.uigen;
public class ChangeComponentEvent {
  private int position;
  private Object newValue;
  public ChangeComponentEvent(int posn, Object newObject) {
    position = posn;
    newValue = newObject;
  }

  public int getPosition() {
    return position;
  }
  
  public Object getNewValue() {
    return newValue;
  }
}
