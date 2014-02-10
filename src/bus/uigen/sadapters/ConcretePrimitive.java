package bus.uigen.sadapters;
import java.util.*;

import bus.uigen.reflect.ClassProxy;
public interface ConcretePrimitive extends ConcreteType {
  
  public boolean isPrimitiveClass(String typeClass);

  public Object newInstance(String typeClass);  public  Object newPrimitiveInstance(ClassProxy c, Object v);

  // hack to fix the primitive type problem
  // Class.forName("int") doesnt work like it
  // used to.
  public ClassProxy getPrimitiveClass(ClassProxy baseClass, String primitiveClass);
  
  public ClassProxy getWrapperType(ClassProxy primitiveClass);
  
}







