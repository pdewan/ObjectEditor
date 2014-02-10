package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteCurve;
import bus.uigen.sadapters.ConcreteTextShape;
import java.awt.Container;
public class CurveAdapterFactory extends AbstractObjectAdapterFactory {
	
	public Class getConcreteType () {
		return ConcreteCurve.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new CurveAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
