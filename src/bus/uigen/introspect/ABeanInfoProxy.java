package bus.uigen.introspect;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import util.trace.Tracer;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class ABeanInfoProxy implements BeanInfoProxy {
	ClassProxy classProxy;
	BeanInfo mainBeanInfo;
	Set<BeanInfo> allBeanInfos;
	MethodDescriptorProxy[] methodDescriptors;
	PropertyDescriptorProxy[] propertyDescriptors;
	BeanDescriptorProxy beanDescriptor;
	public ABeanInfoProxy (ClassProxy cls) {
		classProxy = cls;
		//BeanInfo binfo = AnIntrospectorProxy.getBeanInfo(cls);
		
	}
	public ABeanInfoProxy (BeanInfo theMainBeanInfo, Set<BeanInfo> theAllBeanInfos, ClassProxy theClassProxy) {
		mainBeanInfo = theMainBeanInfo;
		classProxy = theClassProxy;
		allBeanInfos = theAllBeanInfos;
		
	}
	
	public BeanInfo getJavaBeanInfo() {
		return mainBeanInfo;
	}
	
	@Override
	public BeanDescriptorProxy getBeanDescriptor() {
		if (beanDescriptor == null) {
			if (mainBeanInfo != null) {
				beanDescriptor = new ABeanDescriptorProxy(mainBeanInfo.getBeanDescriptor());
			} else {
				Tracer.error("Bean info should never be null.");
				beanDescriptor = new ABeanDescriptorProxy(classProxy);
			}
		}
			
		return beanDescriptor;
	}
	

	@Override
	public MethodDescriptorProxy[] getMethodDescriptors() {
		if (methodDescriptors != null) return methodDescriptors;
		Set<MethodDescriptorProxy> methodDescriptorsSet = new HashSet();
		for (BeanInfo beanInfo:allBeanInfos) {
			MethodDescriptor[] aMethodDescriptors = beanInfo.getMethodDescriptors();
			for (MethodDescriptor aMethodDescriptor:aMethodDescriptors) {
				methodDescriptorsSet.add(new AMethodDescriptorProxy(aMethodDescriptor));
			}
		}
		methodDescriptors = new MethodDescriptorProxy[0];
		methodDescriptors = methodDescriptorsSet.toArray(methodDescriptors);				
		return methodDescriptors;
	}
	
	@Override
	public PropertyDescriptorProxy[] getPropertyDescriptors() {
		if (propertyDescriptors != null) return propertyDescriptors;
		Collection<PropertyDescriptorProxy> propertyDescriptorsSet = new ArrayList();
		for (BeanInfo beanInfo:allBeanInfos) {
			PropertyDescriptor[] aPropertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor aPropertyDescriptor:aPropertyDescriptors) {
				if (aPropertyDescriptor instanceof IndexedPropertyDescriptor)
					continue;
				PropertyDescriptorProxy newVal = new APropertyDescriptorProxy(aPropertyDescriptor);
				if (!propertyDescriptorsSet.contains(newVal))
				propertyDescriptorsSet.add(newVal);
			}
		}
		propertyDescriptors = new PropertyDescriptorProxy[0];

		propertyDescriptors = propertyDescriptorsSet.toArray(propertyDescriptors);				
		return propertyDescriptors;
	}
	
	// not expected to used
	public static MethodDescriptorProxy[] methodDescriptorsFromMethods(ClassProxy classProxy) {
		MethodProxy[] methods = classProxy.getMethods();
		MethodDescriptorProxy[] retVal = new MethodDescriptorProxy[methods.length];
		for (int i = 0; i < methods.length; i++) {
			int modifiers = methods[i].getModifiers();
		      if (Modifier.isPublic(modifiers)) 
			retVal[i] = new AMethodDescriptorProxy(methods[i]);
		}
		return retVal;
		
	}
	
	// not expected to used
	public static PropertyDescriptorProxy[] propertyDescriptorsFromMethods(ClassProxy classProxy) {
		PropertyDescriptorProxy[] retVal;
		Vector properties = IntrospectUtility.getAllPropertiesNamesVector(classProxy); 
		retVal = new PropertyDescriptorProxy[properties.size()];
		
		for (int i = 0; i < retVal.length; i++) {
			String propertyName = (String) properties.elementAt(i);
			MethodProxy readMethod = IntrospectUtility.getGetterMethod(classProxy, propertyName);
			MethodProxy writeMethod = IntrospectUtility.getSetterMethod(classProxy, propertyName);
			retVal[i] = new APropertyDescriptorProxy(propertyName, readMethod, writeMethod);
		}
		return retVal;		
	}

//	@Override
//	public PropertyDescriptorProxy[] getPropertyDescriptors() {
//		// TODO Auto-generated method stub
//		if (propertyDescriptors == null) {
//			if (mainBeanInfo != null) {
//				PropertyDescriptor[] pds = mainBeanInfo.getPropertyDescriptors();
//				propertyDescriptors = new PropertyDescriptorProxy[pds.length];
//				for (int i = 0; i < pds.length; i++) {
//					propertyDescriptors[i] = new APropertyDescriptorProxy(pds[i]);
//				}
//			} else {
//				Vector properties = IntrospectUtility.getAllPropertiesNamesVector(classProxy); 
//				propertyDescriptors = new PropertyDescriptorProxy[properties.size()];
//				
//				for (int i = 0; i < propertyDescriptors.length; i++) {
//					String propertyName = (String) properties.elementAt(i);
//					MethodProxy readMethod = IntrospectUtility.getGetterMethod(classProxy, propertyName);
//					MethodProxy writeMethod = IntrospectUtility.getSetterMethod(classProxy, propertyName);
//					propertyDescriptors[i] = new APropertyDescriptorProxy(propertyName, readMethod, writeMethod);
//				}
//				//MethodProxy[] methods = classProxy.getMethods();
//				//Message.fatalError("Need to extract properties");
//				/*
//				methodDescriptors = new MethodDescriptorProxy[methods.length];
//				for (int i = 0; i < methods.length; i++) {
//					methodDescriptors[i] = new AMethodDescriptorProxy(methods[i]);
//				}
//				*/
//			}
//		}
//		return propertyDescriptors;
//	}

}
