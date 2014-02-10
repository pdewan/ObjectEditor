package bus.uigen.introspect;

import java.beans.BeanDescriptor;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

public class ABeanDescriptorProxy extends AFeatureDescriptorProxy implements BeanDescriptorProxy  {
	BeanDescriptor bd;
	ClassProxy classProxy;
	/*
	public ABeanDescriptorProxy(Class beanClass) {
		
	}
	*/
	
	public ABeanDescriptorProxy (BeanDescriptor theBD) {
		super (theBD);
		bd = theBD;
		classProxy = AClassProxy.classProxy(bd.getBeanClass());
	}
	public ABeanDescriptorProxy (ClassProxy theClassProxy) {
		classProxy = theClassProxy;
	}

	@Override
	public ClassProxy getBeanClass() {
		// TODO Auto-generated method stub
		
		return classProxy;
	}

}
