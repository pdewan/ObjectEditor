// Interface to be implemented by the
// object adaptors.
package bus.uigen;import java.awt.event.FocusEvent;
public interface ComponentValueChangedListener {
  public void uiComponentFocusGained();  public void uiComponentFocusGained(FocusEvent e);
  public void uiComponentFocusLost();
  public boolean uiComponentValueChanged();
  public boolean uiComponentValueChanged(boolean initChange);  public void uiComponentValueEdited(boolean edited);
  public void uiComponentValueChanged(Object source);  public void setEdited(boolean newVal);  public boolean isEdited();
}
