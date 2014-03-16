package bus.uigen.trace;

import util.trace.TraceableInfo;
import bus.uigen.introspect.FeatureDescriptorProxy;
import bus.uigen.oadapters.ObjectAdapter;

public class AttributeSetInfo extends TraceableInfo {
	String attributeName;
	Object attributeValue;
	FeatureDescriptorProxy feature;
	public AttributeSetInfo(String aMessage, FeatureDescriptorProxy aFeature, String anAttributeName, Object anAttributeValue, Object aFinder) {
		super(aMessage, aFinder);
		feature = aFeature;
		attributeName = anAttributeName;
		attributeValue = anAttributeValue;
	}
	public AttributeSetInfo(String aMessage) {
		super(aMessage);
	}
	public String getAttributeName() {
		return attributeName;
	}
//	public void setAttributeName(String attributeName) {
//		this.attributeName = attributeName;
//	}
	public Object getAttributeValue() {
		return attributeValue;
	}
	public FeatureDescriptorProxy getFeature() {
		return feature;
	}
	// for some reason this screws up attribute settings
	public static AttributeSetInfo newCase(FeatureDescriptorProxy aFeature, String anAttributeName, Object anAttributeValue, Object aFinder) {
// the lines below cause strange issues with attributes
//		String aMessage = "Setting attribute: " + anAttributeName + " to: " + anAttributeValue + " of feature: " + aFeature;
//		String aMessage = "Setting attribute: " + anAttributeName + " to: " + anAttributeValue + " of feature:" + aFeature.getName();
		String aMessage = "Setting attribute: " + anAttributeName + " to: " + anAttributeValue ;


//		String aMessage = "foo";
		AttributeSetInfo retVal = new AttributeSetInfo(aMessage, aFeature, anAttributeName, anAttributeValue, aFinder);
		retVal.announce();		
		
		return retVal;
//		return null;
	}
	
//	public void setAttributeValue(Object attributeValue) {
//		this.attributeValue = attributeValue;
//	}
	
	
	
}
