package bus.uigen.introspect;

import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Vector;

//import util.misc.Message;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class CopyOfABeanInfoProxy implements BeanInfoProxy {
	ClassProxy classProxy;
	BeanInfo binfo;
	MethodDescriptorProxy[] methodDescriptors;
	PropertyDescriptorProxy[] propertyDescriptors;
	BeanDescriptorProxy beanDescriptor;
	public CopyOfABeanInfoProxy (ClassProxy cls) {
		classProxy = cls;
		//BeanInfo binfo = AnIntrospectorProxy.getBeanInfo(cls);
		
	}
	public CopyOfABeanInfoProxy (BeanInfo theBeanInfo, ClassProxy theClassProxy) {
		binfo = theBeanInfo;
		classProxy = theClassProxy;
		
	}
	
	public BeanInfo getJavaBeanInfo() {
		return binfo;
	}
	
	@Override
	public BeanDescriptorProxy getBeanDescriptor() {
		// TODO Auto-generated method stub
		if (beanDescriptor == null) {
			if (binfo != null) {
				beanDescriptor = new ABeanDescriptorProxy(binfo.getBeanDescriptor());
			} else
				beanDescriptor = new ABeanDescriptorProxy(classProxy);
		}
			
		return beanDescriptor;
	}

	@Override
	public MethodDescriptorProxy[] getMethodDescriptors() {
		// TODO Auto-generated method stub
		if (methodDescriptors == null) {
			if (binfo != null && !(classProxy.isInterface() && classProxy.getInterfaces().length != 0)) {
				MethodDescriptor[] mds = binfo.getMethodDescriptors();
				methodDescriptors = new MethodDescriptorProxy[mds.length];
				for (int i = 0; i < mds.length; i++) {
					methodDescriptors[i] = new AMethodDescriptorProxy(mds[i]);
				}
			} else {
				MethodProxy[] methods = classProxy.getMethods();
				methodDescriptors = new MethodDescriptorProxy[methods.length];
				for (int i = 0; i < methods.length; i++) {
					int modifiers = methods[i].getModifiers();
				      if (Modifier.isPublic(modifiers)) 
					methodDescriptors[i] = new AMethodDescriptorProxy(methods[i]);
				}
			}
		}
			
		return methodDescriptors;
	}

	@Override
	public PropertyDescriptorProxy[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		if (propertyDescriptors == null) {
			if (binfo != null) {
				PropertyDescriptor[] pds = binfo.getPropertyDescriptors();
				propertyDescriptors = new PropertyDescriptorProxy[pds.length];
				for (int i = 0; i < pds.length; i++) {
					propertyDescriptors[i] = new APropertyDescriptorProxy(pds[i]);
				}
			} else {
				Vector properties = IntrospectUtility.getAllPropertiesNamesVector(classProxy); 
				propertyDescriptors = new PropertyDescriptorProxy[properties.size()];
				
				for (int i = 0; i < propertyDescriptors.length; i++) {
					String propertyName = (String) properties.elementAt(i);
					MethodProxy readMethod = IntrospectUtility.getGetterMethod(classProxy, propertyName);
					MethodProxy writeMethod = IntrospectUtility.getSetterMethod(classProxy, propertyName);
					propertyDescriptors[i] = new APropertyDescriptorProxy(propertyName, readMethod, writeMethod);
				}
				//MethodProxy[] methods = classProxy.getMethods();
				//Message.fatalError("Need to extract properties");
				/*
				methodDescriptors = new MethodDescriptorProxy[methods.length];
				for (int i = 0; i < methods.length; i++) {
					methodDescriptors[i] = new AMethodDescriptorProxy(methods[i]);
				}
				*/
			}
		}
		return propertyDescriptors;
	}

}
