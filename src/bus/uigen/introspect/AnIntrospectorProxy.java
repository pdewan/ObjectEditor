package bus.uigen.introspect;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;
public class AnIntrospectorProxy {

	static Hashtable<ClassProxy, BeanInfoProxy> classToBeanInfo = new Hashtable();
//	public static BeanInfoProxy getBeanInfo(ClassProxy c) {
//		BeanInfoProxy retVal = classToBeanInfo.get(c);
//		if ( retVal != null) 
//			return retVal;
//		
//		try {
//		if (!(c instanceof AClassProxy))
//			retVal = new ABeanInfoProxy(c); // this is strange
//			
//		else
//			retVal = new ABeanInfoProxy(Introspector.getBeanInfo(((AClassProxy) c).getJavaClass()), c);
//	
//		classToBeanInfo.put(c, retVal);
//		} catch (Exception e) {
//			retVal = null;
//		}
//		return retVal;
//		
//	}
	
	public static BeanInfoProxy getBeanInfo(ClassProxy c, boolean ignoreComponents) {
		BeanInfoProxy retVal = classToBeanInfo.get(c);
		if ( retVal != null) 
			return retVal;
		// methods cannot be supported if this happens
//		if (ignoreComponents) {
//			retVal = new ABeanInfoProxy(null, null, c);
//			classToBeanInfo.put(c, retVal);
//			return retVal;
//		}
		
		try {
			Set<BeanInfo> beanInfos = new HashSet();
			BeanInfo mainBeanInfo = Introspector.getBeanInfo(((AClassProxy) c).getJavaClass());
			beanInfos.add(mainBeanInfo);

			if (c.isInterface() && c.getInterfaces().length > 0) { // a derived interface, mainBeanInfo will not have derived methods
				Set<ClassProxy> superInterfaces = IntrospectUtility.getAllSuperInterfaces(c);
				for (ClassProxy anInterface: superInterfaces) {
					beanInfos.add(Introspector.getBeanInfo(((AClassProxy) anInterface).getJavaClass()));
				}
				
			}		

			retVal = new ABeanInfoProxy(mainBeanInfo, beanInfos, c);
	
		classToBeanInfo.put(c, retVal);
		} catch (Exception e) {
			retVal = null;
		}
		return retVal;
		
	}
	
	
		
		
	}


