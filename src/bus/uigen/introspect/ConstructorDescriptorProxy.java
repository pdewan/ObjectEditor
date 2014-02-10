package bus.uigen.introspect;

import bus.uigen.reflect.MethodProxy;

public interface ConstructorDescriptorProxy extends FeatureDescriptorProxy{
	MethodProxy getConstructor();
}
