package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteStringShape;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTextShape;
import java.awt.Container;
public class StringShapeAdapterFactory extends TextShapeAdapterFactory {
	
	public Class getConcreteType () {
		return ConcreteStringShape.class;
	}
//	public  uiObjectAdapter createObjectAdapter() {
//		try {
//		return new uiTextShapeAdapter();
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
	
	
}
