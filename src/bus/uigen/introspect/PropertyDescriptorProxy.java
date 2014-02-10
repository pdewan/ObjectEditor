package bus.uigen.introspect;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public interface PropertyDescriptorProxy extends FeatureDescriptorProxy {
	 MethodProxy getReadMethod(); 
	 MethodProxy getWriteMethod();
	 void setReadMethod(MethodProxy readMethod);
	 void setWriteMethod(MethodProxy writeMethod);
	ClassProxy getPropertyType(); 
}
