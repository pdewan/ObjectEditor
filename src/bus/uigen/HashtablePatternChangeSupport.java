package bus.uigen;import java.util.*;
//import bus.uigen.introspect.uiBean;
import bus.uigen.introspect.IntrospectUtility;import util.models.*;
public class HashtablePatternChangeSupport extends HashtableChangeSupport {

	
	public HashtablePatternChangeSupport(Object o) {
		super (o);		
	}
	
	public HashtablePatternChangeSupport(){	
	}
	
	public Hashtable toHashtable(Object o) {		return  IntrospectUtility.toHashtable(o);	}
}
