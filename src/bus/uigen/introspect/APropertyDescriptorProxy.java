package bus.uigen.introspect;

import java.beans.PropertyDescriptor;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.reflect.local.AVirtualMethod;

public class APropertyDescriptorProxy extends AFeatureDescriptorProxy implements PropertyDescriptorProxy  {
	PropertyDescriptor pd;
	MethodProxy readMethod, writeMethod;
	ClassProxy propertyType;
	public APropertyDescriptorProxy(PropertyDescriptor thePD) {
		super(thePD);
		pd = thePD;
		readMethod = AVirtualMethod.virtualMethod(pd.getReadMethod()); 
		writeMethod = AVirtualMethod.virtualMethod(pd.getWriteMethod());
		propertyType = RemoteSelector.classProxy(thePD.getPropertyType());
	}
	public APropertyDescriptorProxy(String propertyName, MethodProxy theReadMethod, MethodProxy theWriteMethod) {
		super (propertyName, propertyName);
		readMethod = theReadMethod;
		writeMethod = theWriteMethod;
		if (theReadMethod != null)
			propertyType = theReadMethod.getReturnType();
		else
			propertyType = StandardProxyTypes.objectClass();
	}
	/*
	APropertyDescriptorProxy(String propertyName, Class beanClass) {
		
	}
     
	APropertyDescriptorProxy(String propertyName, Class beanClass, String readMethodName, String writeMethodName) {
		
	}
    
	APropertyDescriptorProxy(String propertyName, VirtualMethod readMethod, VirtualMethod writeMethod) {
		
	}
	*/
    //This constructor takes the name of a simple property, and Method objects for reading and writing the property 
	@Override
	public MethodProxy getReadMethod() {
		// TODO Auto-generated method stub
		return readMethod;
	}
	@Override
	public MethodProxy getWriteMethod() {
		// TODO Auto-generated method stub
		return writeMethod;
	}
	@Override
	public void setReadMethod(MethodProxy theReadMethod) {
		// TODO Auto-generated method stub
		readMethod = theReadMethod;
		//pd.setReadMethod(readMethod)
		
	}
	@Override
	public void setWriteMethod(MethodProxy theWriteMethod) {
		// TODO Auto-generated method stub
		writeMethod = theWriteMethod;
	}
	@Override
	public ClassProxy getPropertyType() {
		// TODO Auto-generated method stub
		return propertyType;
	}
	
	public boolean equals (Object other) {
		if (!(other instanceof PropertyDescriptorProxy)) return false;
		PropertyDescriptorProxy otherPropertyDescriptor = (PropertyDescriptorProxy) other;
		return (readMethod != null && 
				IntrospectUtility.equalsHeader(readMethod, otherPropertyDescriptor.getReadMethod())) ||
				(writeMethod != null && 
						IntrospectUtility.equalsHeader(writeMethod, otherPropertyDescriptor.getWriteMethod())) ||
				(readMethod == null && writeMethod == null && 
						getName().equals(otherPropertyDescriptor.getName()) ); // does this case arise?
	}
	public String toString() {
		return getName() + ": " + super.toString();
	}


}
