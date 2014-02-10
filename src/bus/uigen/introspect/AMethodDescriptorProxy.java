package bus.uigen.introspect;

import java.beans.FeatureDescriptor;
import java.beans.MethodDescriptor;

import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AVirtualMethod;

public class AMethodDescriptorProxy extends AFeatureDescriptorProxy implements MethodDescriptorProxy {
	MethodProxy method;
	public AMethodDescriptorProxy(MethodDescriptor theMD) {
		super(theMD);
		method = AVirtualMethod.virtualMethod(theMD.getMethod());
		// TODO Auto-generated constructor stub
	}
	public AMethodDescriptorProxy() {
		
	}
	public AMethodDescriptorProxy(MethodProxy theMethod) {
		super (theMethod.getName(), theMethod.getName());
		method = theMethod;
	}

	@Override
	public MethodProxy getMethod() {
		// TODO Auto-generated method stub
		
		return method;
	}
	public String toString() {
		return  method.toString();
	}
	public boolean equals (Object other) {
		if (!(other instanceof MethodDescriptorProxy)) return false;
		MethodDescriptorProxy otherDescriptor = (MethodDescriptorProxy) other;
		return method != null && method.equals(otherDescriptor.getMethod()) ||
				(method ==null && displayName.equals(otherDescriptor.getDisplayName())); // does this case arise?
	}
	/*
	public AMethodDescriptorProxy(VirtualMethod method) {
		
	}
	*/

}
