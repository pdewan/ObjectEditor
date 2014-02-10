package bus.uigen;
// The event structure used to
// communicate the removal of
// a component to a composite type
// (Vector ?)

public class DeleteComponentEvent {
  private int    position;
  public DeleteComponentEvent(int posn) {
    position = posn;
  }

  public int getPosition() {
    return position;
  }
}
