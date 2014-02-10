package bus.uigen.introspect;

import java.lang.reflect.Field;

public class ConstantDescriptor extends FieldDescriptor {
  public ConstantDescriptor(Field f) {
    super(f);
  }
}
