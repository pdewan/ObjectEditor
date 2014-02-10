package bus.uigen.oadapters;
import bus.uigen.sadapters.ConcreteType;
import java.awt.Container;
import bus.uigen.sadapters.ConcreteEnumeration;
public class ReferenceAdapterFactory extends PrimitiveAdapterFactory {

	public Class getConcreteType () {
		return ConcreteEnumeration.class;
	}
	public  ObjectAdapter createObjectAdapter() {
		try {
		return new ReferenceAdapter();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	
}
