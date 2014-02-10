package bus.uigen.sadapters;
import bus.uigen.uiFrame;import bus.uigen.reflect.ClassProxy;
public interface ConcreteShapeFactory extends ConcreteTypeFactory  {		public ConcreteShape toConcreteShape(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame);
}
