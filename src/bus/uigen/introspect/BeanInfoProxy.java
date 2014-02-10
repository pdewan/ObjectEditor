package bus.uigen.introspect;

public interface BeanInfoProxy {
	public BeanDescriptorProxy getBeanDescriptor();
	public MethodDescriptorProxy[] getMethodDescriptors();
	public PropertyDescriptorProxy[] getPropertyDescriptors();

}
