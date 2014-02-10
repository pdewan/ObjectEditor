package bus.uigen.introspect;

import java.util.Enumeration;

public interface FeatureDescriptorProxy {
	String getName();
	String getDisplayName();
	void setDisplayName(String displayName);
	Object getValue(String attributeName);
	void setValue(String attributeName, Object value);
	Enumeration attributeNames();
}
