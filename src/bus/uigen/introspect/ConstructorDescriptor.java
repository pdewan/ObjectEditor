package bus.uigen.introspect;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Constructor;


public class ConstructorDescriptor extends FeatureDescriptor {

  Constructor constructor;
  public ConstructorDescriptor(Constructor c) {
    constructor = c;
    setName(c.getName());
  }
  public Constructor getConstructor() {
    return constructor;
  }
}
