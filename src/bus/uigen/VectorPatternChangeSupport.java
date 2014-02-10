package bus.uigen;import java.util.Vector;import bus.uigen.introspect.IntrospectUtility;import util.models.*;
//import bus.uigen.uiBean;//import bus.uigen.introspect.uiBean;

public class VectorPatternChangeSupport extends util.models.VectorChangeSupport{
	public VectorPatternChangeSupport(Object o) {		super(o);	}public  Vector toClassVector(Object object) {	return IntrospectUtility.toClassVector(object);}

}
