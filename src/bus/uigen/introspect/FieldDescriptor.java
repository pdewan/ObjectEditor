package bus.uigen.introspect;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Field;

public class FieldDescriptor extends FeatureDescriptor {

  Field field;

  public FieldDescriptor(Field f) {
    field = f;
    setName(f.getName());
  }
  
  public Field getField() {
    return field;
  }
}

