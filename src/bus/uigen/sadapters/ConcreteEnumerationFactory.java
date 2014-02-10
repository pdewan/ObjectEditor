package bus.uigen.sadapters;
import bus.uigen.uiFrame;import bus.uigen.reflect.ClassProxy;
public interface ConcreteEnumerationFactory extends ConcreteTypeFactory  {		public ConcreteEnumeration toConcreteEnumeration(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame);
}
