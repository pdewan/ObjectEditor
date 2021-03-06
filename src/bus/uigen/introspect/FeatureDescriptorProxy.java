package bus.uigen.introspect;

import java.util.Enumeration;

import util.models.PropertyListenerRegisterer;

public interface FeatureDescriptorProxy extends PropertyListenerRegisterer {
	String getName();
	String getDisplayName();
	void setDisplayName(String displayName);
	Object getValue(String attributeName);
	void setValue(String attributeName, Object value);
	Enumeration attributeNames();
}
