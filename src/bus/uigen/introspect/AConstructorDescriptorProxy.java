package bus.uigen.introspect;

import java.beans.FeatureDescriptor;
import java.beans.MethodDescriptor;

import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AVirtualMethod;

public class AConstructorDescriptorProxy extends AFeatureDescriptorProxy implements ConstructorDescriptorProxy {
	MethodProxy method;
	public AConstructorDescriptorProxy(ConstructorDescriptor theCD) {
		super(theCD);
		method = AVirtualMethod.virtualMethod(theCD.getConstructor());
		// TODO Auto-generated constructor stub
	}
	public AConstructorDescriptorProxy(MethodProxy theMethod) {
		super (theMethod.getName(), theMethod.getName());
		method = theMethod;
	}

	@Override
	public MethodProxy getConstructor() {
		// TODO Auto-generated method stub
		
		return method;
	}
	/*
	public AMethodDescriptorProxy(VirtualMethod method) {
		
	}
	*/

}
