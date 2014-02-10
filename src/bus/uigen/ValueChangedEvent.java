package bus.uigen;
// The event structure used to
// communicate changes in 
// primitive fields.

public class ValueChangedEvent {
  private Object newValue;
  public ValueChangedEvent(Object obj) {
    newValue = obj;
  }

  public Object getNewValue() {
    return newValue;
  }
}
