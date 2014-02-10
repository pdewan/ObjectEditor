package bus.uigen.sadapters;
import bus.uigen.uiFrame;import bus.uigen.reflect.ClassProxy;
public interface RecordFactory extends ConcreteTypeFactory  {		public RecordStructure toRecordStructure(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame);
}
