package bus.uigen.sadapters;
import bus.uigen.uiFrame;import bus.uigen.reflect.ClassProxy;
public interface VectorStructureFactory extends ConcreteTypeFactory {		public VectorStructure toVectorStructure(ClassProxy theGVectorClass, Object theGVectorObject, uiFrame theFrame, boolean forceConversion);
}
