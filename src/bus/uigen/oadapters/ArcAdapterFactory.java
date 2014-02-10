package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteArc;
import bus.uigen.sadapters.ConcreteTextShape;
import java.awt.Container;
public class ArcAdapterFactory extends AbstractObjectAdapterFactory {
	
	public Class getConcreteType () {
		return ConcreteArc.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new ArcAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
