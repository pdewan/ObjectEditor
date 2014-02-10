package bus.uigen.loggable;

import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.ReflectUtil;


public class ACompositeLoggable extends AnIdentifiableLoggable {
	/*
	implements java.io.Serializable,
	
	//PropertyChangeListener, VectorListener, VectorMethodsListener {
	AwarePropertyChangeListener, AwareVectorListener, AwareVectorMethodsListener {
	//static int currentId = 0;
	//int objectId;
	String objectId;
	String virtualClass;
	Set objectIds = new HashSet();
	
	public ACompositeLoggable(int id) {
		super(id);
		//setObjectId(id);
	}
	public ACompositeLoggable() {
		//setObjectId(currentId++);
	}
	*/
	// need this as a local cache for equal method
	Object realObject;
	public void setRealObject(Object newVal) {
		realObject = newVal;
	}
	public Object getRealObject() {
		return realObject;
	}
	
	public String toString() {
		return realObject.toString();
	}
	/*
	public boolean equals(Object otherValue) {
		if (otherValue instanceof ACompositeLoggable) {
			return getRealObject().equals(((ACompositeLoggable) otherValue).getRealObject());
		}
		return getRealObject().equals(otherValue);
	}
	*/
	public static ClassProxy getTargetClass(Object maybeFakeModel) {
		  if (maybeFakeModel instanceof ACompositeLoggable) {
			  Object realObject = ((ACompositeLoggable) maybeFakeModel).getRealObject();
			  return  ReflectUtil.toMaybeProxyTargetClass(realObject);
//			  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(maybeFakeModel);
//			  return cd.getRealClass();
			  
		  }
		  return  ReflectUtil.toMaybeProxyTargetClass(maybeFakeModel);
	  }
	 public static Object maybeExtractRealObject(Object obj) {
		 if (obj instanceof ACompositeLoggable) {
			 return ((ACompositeLoggable) obj).getRealObject();
		 }
		 else
			 return obj;
	 }

}
