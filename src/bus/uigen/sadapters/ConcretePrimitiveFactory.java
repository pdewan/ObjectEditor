package bus.uigen.sadapters;
import bus.uigen.uiFrame;import bus.uigen.reflect.ClassProxy;
public interface ConcretePrimitiveFactory extends ConcreteTypeFactory  {		public ConcretePrimitive toConcretePrimitive(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame);
}
