package bus.uigen.query;

import java.util.Map;

import util.trace.Traceable;

public interface ObjectQuery {
	public void setExpectedClass(Class expectedClass) ;
	public Map<String, Object> getPropertyToExpectedValue() ;
	public void setPropertyToExpectedValue(
			Map<String, Object> propertyToExpectedValue) ;
	public boolean matches (Object aTraceable);
	boolean isClassQuery();
	Class getExpectedClass();
	boolean isObjectQuery();
	Object getExpectedObject();


}
