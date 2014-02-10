package bus.uigen.introspect;

import java.beans.FeatureDescriptor;
import java.util.Enumeration;
import java.util.Hashtable;

public class AFeatureDescriptorProxy implements FeatureDescriptorProxy {
	FeatureDescriptor fd;	
	String name, displayName;
	Hashtable<String, Object> attributes = new Hashtable();
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
			attributes.put(attributeName, value);
		
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

}
