package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteImageShape;
import bus.uigen.sadapters.ConcreteStringShape;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTextShape;
import java.awt.Container;
public class ImageShapeAdapterFactory  extends AbstractObjectAdapterFactory  {
	
	public Class getConcreteType () {
		return ConcreteImageShape.class;
	}
//	public  uiObjectAdapter createObjectAdapter() {
//		try {
//		return new uiTextShapeAdapter();
//		} catch (Exception e) {
//			return null;
//		}
//	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new ImageShapeAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
