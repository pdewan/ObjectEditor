package bus.uigen.trace;

import java.util.Map;

import util.trace.Traceable;

public interface TraceableQuery {
	public void setExpectedClass(Class expectedClass) ;
	public Map<String, Object> getPropertyToExpectedValue() ;
	public void setPropertyToExpectedValue(
			Map<String, Object> propertyToExpectedValue) ;
	public boolean matches (Traceable aTraceable);


}
