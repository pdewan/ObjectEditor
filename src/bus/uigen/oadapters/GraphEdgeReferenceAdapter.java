// We can derive primitive adaptors by extending
// This class
package bus.uigen.oadapters;
import java.lang.reflect.*;
import java.rmi.RemoteException;
import java.util.*;import java.beans.*;

import util.models.RemotePropertyChangeListener;
import util.trace.Tracer;
import bus.uigen.*;import bus.uigen.ars.*;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.editors.EditorRegistry;
import bus.uigen.introspect.*;import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.ConcreteEnumeration;import bus.uigen.sadapters.ConcreteType;
import bus.uigen.viewgroups.OEView;
import bus.uigen.widgets.VirtualLabel;

import java.util.List;
public class GraphEdgeReferenceAdapter extends ReferenceAdapter 
{
	
	public GraphEdgeReferenceAdapter(ObjectAdapter aReferentAdapter) throws RemoteException {
		super(aReferentAdapter);
	}

			public String toString() {
		return referentAdapter.getPropertyName();
		

	}
	
		
	

}



