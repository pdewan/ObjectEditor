package bus.uigen.introspect;

import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;

//import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AVirtualMethod;

public class VirtualMethodDescriptor extends AMethodDescriptorProxy {
	
	MethodProxy virtualMethod;
	MethodDescriptor md;
	//Class virtualClass;
	public VirtualMethodDescriptor(MethodProxy theMethod) {
		
		super (theMethod);
		virtualMethod = theMethod;
	
	} 
	public VirtualMethodDescriptor() {
		//super (AVirtualMethod.virtualDummy());
		//super();
	
	} 
	/*
    public  VirtualMethodDescriptor(VirtualMethod theMethod, ParameterDescriptor[] parameterDescriptors) {
    	 super(null, parameterDescriptors);
    	 virtualMethod = theMethod;
    	 
     }
     */
    
     public void setVirtualMethod (MethodProxy newVal) {
    	 virtualMethod = newVal;
     }
     
     public MethodProxy getVirtualMethod() {
    	 return virtualMethod;
     }
     public MethodProxy getMethod() {
    	 MethodProxy retVal = super.getMethod();
    	 if (retVal == null)
    		 retVal = virtualMethod;
    	 return retVal;
     }
     /*
     public void setDynamiclClass (Class newVal) {
    	 virtualClass = newVal;
     }
     
     public Class getDynamicClass() {
    	 return virtualClass;
     }
     */
     /*
     public VirtualMethod getMethod() {
    	 
    	return null;
     }
     */
     
     public static MethodProxy getVirtualMethod (MethodDescriptorProxy md) {
    	 if (md instanceof VirtualMethodDescriptor)
    		 return ((VirtualMethodDescriptor) md).getVirtualMethod();
    	 else
    		 //return uiMethodInvocationManager.virtualMethod(md.getMethod());
    	 	return md.getMethod();
    	 
     }
     public String getDisplayName() {
    	 //if (virtualMethod.getConstructor() != null)
    	 if (virtualMethod.isConstructor())
    		 return AttributeNames.NEW_OBJECT_MENU_NAME + " " + 
    		    AClassDescriptor.toShortName(virtualMethod.getName()) +
    		    getParameterRepresentation (virtualMethod);
    	 else
    		 return virtualMethod.getName();
 	}
     String getParameterRepresentation (MethodProxy m) {
    	 ClassProxy[] parameterTypes = m.getParameterTypes();
    	 String retVal = "";
    	 if (parameterTypes.length == 0)
    		 return retVal;
    	 retVal +=  "(";
    	 for (int i = 0; i < parameterTypes.length; i++) {
    		 retVal += AClassDescriptor.toShortName(parameterTypes[i].getSimpleName());
    	 }
    	 retVal += ")";
    	 return retVal;
    	 
     }
     public String getName() {
    	 return virtualMethod.getName();
     }


}
