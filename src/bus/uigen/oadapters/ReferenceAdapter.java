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
public class ReferenceAdapter extends PrimitiveAdapter 
{
	private static final String ROOT_NAME = "root";
	ObjectAdapter referentAdapter;

	public ReferenceAdapter() throws RemoteException {
		super();
	}
	
	public ReferenceAdapter(ObjectAdapter aReferentAdapter) throws RemoteException {
		super();
		init(aReferentAdapter);
	}

	public void init(ObjectAdapter aReferentAdapter) {
		referentAdapter = aReferentAdapter;
	}		public String toString() {
		String aReferencePath = referentAdapter.getReferencePath();
		int aRootComponentEnd = aReferencePath.indexOf(".");
		if (aRootComponentEnd != -1) {
			String aSuffix = aReferencePath.substring(aRootComponentEnd);
			aReferencePath = ROOT_NAME + aSuffix;
		} else {
			aReferencePath = ROOT_NAME;
		}
//		return "Points to: " + referentAdapter.getReferencePath();
		return "Points to: " + aReferencePath;

	}
	
	public Object getViewObject() {
		return toString();
	}
//	public Object computeAndMaybeSetViewObject() {
//		return getViewObject();
//	}
	
	public void computeAndSetViewObject() {
		setViewObject(getViewObject());
	}
	
	public void setPreferredWidget() {
		setPreferredWidget(VirtualLabel.class.getName());
	}
	
	public ObjectAdapter getReferentAdapter() {
		return referentAdapter;
	}
	
		
		
	

}



