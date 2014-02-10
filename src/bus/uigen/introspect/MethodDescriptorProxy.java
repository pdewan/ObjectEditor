package bus.uigen.introspect;

import bus.uigen.reflect.MethodProxy;

public interface MethodDescriptorProxy extends FeatureDescriptorProxy {
	MethodProxy getMethod();
}
