package bus.uigen.introspect;

import java.beans.FeatureDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;
import java.util.Hashtable;

import bus.uigen.trace.AttributeSetInfo;

public class AFeatureDescriptorProxy implements FeatureDescriptorProxy {
	FeatureDescriptor fd;	
	String name, displayName;
	Hashtable<String, Object> attributes = new Hashtable();
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	public AFeatureDescriptorProxy (String theName, String theDisplayName ) {
		name = theName;
		displayName = theDisplayName;
	}
	
	public AFeatureDescriptorProxy (FeatureDescriptor theFD) {
		fd = theFD;		
	}
	
	public AFeatureDescriptorProxy () {
		
	}
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub	
		if (fd != null)
			return fd.getDisplayName();
		return displayName;
		//return fd.getDisplayName();
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		if (fd != null)
			return fd.getName();
		//return fd.getName();
		return name;
	}

	@Override
	public Object getValue(String attributeName) {
		// TODO Auto-generated method stub
		Object retVal = attributes.get(attributeName);
		// the code below should be executed only if someone asks for an attribute that is not in attribute names
		if (retVal == null && fd != null)
			return fd.getValue(attributeName);
		return retVal;
//		if (fd != null)
//			return fd.getValue(attributeName);
//		else
//			return attributes.get(attributeName);
	}

	@Override
	public void setDisplayName(String theDisplayName) {
		// TODO Auto-generated method stub
		if (fd != null)
			fd.setDisplayName(theDisplayName);
		else
			displayName = theDisplayName;
		
	}

	@Override
	public void setValue(String attributeName, Object value) {
		// TODO Auto-generated method stub
//		if (fd != null)
//			fd.setValue(attributeName, value);
//		else
			Object oldValue = attributes.get(attributeName);
			attributes.put(attributeName, value);
			AttributeSetInfo.newCase(this, attributeName, value, this);
			propertyChangeSupport.firePropertyChange(attributeName, oldValue, value);
//			
		
	}

	@Override
	public Enumeration attributeNames() {
		// TODO Auto-generated method stub
//		if (fd != null)
//			return fd.attributeNames();
//		else
			return attributes.keys();
	}
	public String toString() {
		if (name != null)
			return name;
		else
			return super.toString();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
	}

}
