package bus.uigen.attributes;
import java.util.Vector;

public interface AttributeCollection {
  public Vector getAttributes();
  //public void setAttributes(Vector attributes);
  public Vector getLocalAttributes();
  public void setLocalAttributes(Vector attributes);
}
