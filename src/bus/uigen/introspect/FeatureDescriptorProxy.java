package bus.uigen.introspect;

import java.util.Enumeration;

import util.models.PropertyListenerRegistrar;

public interface FeatureDescriptorProxy extends PropertyListenerRegistrar {
	String getName();
	String getDisplayName();
	void setDisplayName(String displayName);
	Object getValue(String attributeName);
	void setValue(String attributeName, Object value);
	Enumeration attributeNames();
}
