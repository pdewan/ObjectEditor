package bus.uigen.viewgroups;

import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.PropertyDescriptorProxy;
import bus.uigen.introspect.VirtualMethodDescriptor;
import bus.uigen.reflect.DynamicMethods;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;

public class DescriptorViewSupport {
	//Object targetObject;
	//Class targetClass;
	//Vector<PropertyDescriptor> properties;
	//Vector<VirtualMethodDescriptor> commands;
	Vector<MethodProxy> virtualMethods = new Vector();
	//transient Method dynamicPropertyGetter = null;
	//transient Method dynamicPropertySetter = null;
	String[] propertyNames = new String[0];
	Vector<String> propertyNameVector = new Vector();
	Hashtable<String, MethodProxy> propertyReadMethods = new Hashtable();
	Hashtable<String, MethodProxy> propertyWriteMethods = new Hashtable();
	Hashtable<String, MethodProxy> propertyDynamicGetters = new Hashtable();
	Hashtable<String, MethodProxy> propertyDynamicSetters = new Hashtable();
	Hashtable<String, Object> propertyTargetObjects = new Hashtable();
	Hashtable<String, APropertyAndCommandFilter> propertyValues = new Hashtable();
	String virtualClass;
	
	
	// this is not really called
	public DescriptorViewSupport (Object theTargetObject, Vector<PropertyDescriptorProxy> theProperties, Vector<VirtualMethodDescriptor> theCommands) {
		set(theTargetObject, theProperties);
		/*
		targetObject = theTargetObject;
		properties = theProperties;
		commands = theCommands;		
		*/
	}
	
	public DescriptorViewSupport() {
		// TODO Auto-generated constructor stub
	}
	
	

	protected void set(Object theTargetObject, Vector<PropertyDescriptorProxy> theProperties) {
		//targetObject = theTargetObject;
		//targetClass = theTargetObject.getClass();
		//properties = theProperties;
		//commands = theCommands;	
		addProperties (theTargetObject, theProperties);
	}
	protected void addProperty(Object theTargetObject, PropertyDescriptorProxy theProperty) {
		
		Vector<PropertyDescriptorProxy> theProperties = new Vector();
		theProperties.addElement(theProperty);
		addProperties (theTargetObject, theProperties);
	}
	protected void addPropertyGroup(String name, APropertyAndCommandFilter value) {
		String[] newPropertyNames = new String[propertyNames.length + 1];

		for (int i = 0; i < propertyNames.length; i ++) {
			newPropertyNames[i] = propertyNames[i];
		}
		newPropertyNames[propertyNames.length] = name;
		propertyNames = newPropertyNames;
		propertyNameVector.add(name);
		propertyValues.put(name, value);
		
	}
	protected APropertyAndCommandFilter getPropertyGroup(String name) {
		return propertyValues.get(name);
	}
	protected void addMethod(Object theTargetObject, MethodDescriptorProxy theCommand) {
		Vector<MethodDescriptorProxy> theMethods = new Vector();
		theMethods.addElement(theCommand);
		addMethods(theTargetObject, theMethods);
	}
	protected void addProperties (Object theTargetObject, PropertyDescriptorProxy[] theProperties) {
		/*
		dynamicPropertyGetter = uiBean.getDynamicPropertyGetter(theTargetObject.getClass());
		if (dynamicPropertyGetter != null) {
			dynamicPropertySetter = uiBean.getDynamicPropertySetter(targetClass, dynamicPropertyGetter.getReturnType());
			
		}
		*/
		
		
		for (int i = 0; i < theProperties.length; i ++) {
			PropertyDescriptorProxy property = theProperties[i];
			String pname = property.getName();
			if (propertyNameVector.contains(pname)) continue;
			if (AttributeNames.ANY_KEY.equals(pname) || 
					  AttributeNames.ANY_VALUE.equals(pname) ||
					  AttributeNames.ANY_ELEMENT.equals(pname))
				continue;
			propertyNameVector.addElement(pname);
			if (property.getReadMethod() != null) {
				propertyReadMethods.put(pname, property.getReadMethod());
				if (property.getWriteMethod() != null)
					propertyWriteMethods.put(pname,property.getWriteMethod());
				propertyTargetObjects.put (pname, theTargetObject );
			} else {
				MethodProxy dynamicPropertyGetter = DynamicMethods.getDynamicPropertyGetter(RemoteSelector.getClass(theTargetObject));
				if (dynamicPropertyGetter != null) {

					propertyDynamicGetters.put( pname, dynamicPropertyGetter);
					 MethodProxy dynamicPropertySetter = DynamicMethods.getDynamicPropertySetter(RemoteSelector.getClass(theTargetObject), dynamicPropertyGetter.getReturnType());
					 propertyDynamicSetters.put(pname, dynamicPropertySetter);
					 //writeMethod = dynamicPropertySetter;
				} ;
				
			}
			
		}
		//propertyNames = (String[]) propertyNameVector.toArray();
		propertyNames = new String[propertyNameVector.size()];
		for (int i = 0; i < propertyNames.length; i++)
			propertyNames[i] = propertyNameVector.elementAt(i);
			//new String[theProperties.size()];
	}
	
	
	protected void addProperties (Object theTargetObject, Vector<PropertyDescriptorProxy> theProperties) {
		PropertyDescriptorProxy[] pdArray = new PropertyDescriptorProxy[theProperties.size()];
		theProperties.copyInto(pdArray);
		addProperties (theTargetObject, pdArray);
		/*
		
		
		for (int i = 0; i < theProperties.size(); i ++) {
			PropertyDescriptor property = theProperties.elementAt(0);
			String pname = property.getName();
			if (propertyNameVector.contains(pname)) continue;
			propertyNameVector.addElement(pname);
			if (property.getReadMethod() != null) {
				propertyReadMethods.put(pname, property.getReadMethod());
				if (property.getWriteMethod() != null)
					propertyWriteMethods.put(pname,property.getWriteMethod());
				propertyTargetObjects.put (pname, theTargetObject );
			} else {
				Method dynamicPropertyGetter = uiBean.getDynamicPropertyGetter(theTargetObject.getClass());
				if (dynamicPropertyGetter != null) {

					propertyDynamicGetters.put( pname, dynamicPropertyGetter);
					 Method dynamicPropertySetter = uiBean.getDynamicPropertySetter(theTargetObject.getClass(), dynamicPropertyGetter.getReturnType());
					 propertyDynamicSetters.put(pname, dynamicPropertySetter);
					 //writeMethod = dynamicPropertySetter;
				} ;
				
			}
			
		}
		//propertyNames = (String[]) propertyNameVector.toArray();
		propertyNames = new String[propertyNameVector.size()];
		for (int i = 0; i < propertyNames.length; i++)
			propertyNames[i] = propertyNameVector.elementAt(i);
			//new String[theProperties.size()];
			 * 
			 */
	}
	protected void addMethods (Object theTargetObject, Vector<MethodDescriptorProxy> theMethods) {
		MethodDescriptorProxy[] methodArray = new MethodDescriptorProxy[theMethods.size()];
		theMethods.copyInto(methodArray);
		addMethods(theTargetObject, methodArray);
		/*
		for (int i = 0; i < theMethods.size(); i++) {
			virtualMethods.addElement(VirtualMethodDescriptor.getVirtualMethod(theMethods.elementAt(i)).moveFromObject(theTargetObject));
		}
		*/
		//dynamicPropertyGetter = uiBean.getDynamicPropertyGetter(targetClass);	
		
	}
	protected void addMethods (Object theTargetObject, MethodDescriptorProxy[] theMethods) {
		for (int i = 0; i < theMethods.length; i++) {
			virtualMethods.addElement(VirtualMethodDescriptor.getVirtualMethod(theMethods[i]).moveFromObject(theTargetObject));
		}
		
		//dynamicPropertyGetter = uiBean.getDynamicPropertyGetter(targetClass);	
		
	}
	Object[] emptyParams = {};
	protected String[] getDynamicProperties() {
		return propertyNames;
	}
	protected Object getDynamicProperty(String name) {
		Object value = propertyValues.get(name);
		if (value != null) return value;
		Object targetObject = propertyTargetObjects.get(name);
		if (targetObject == null) return null;
		try {
		MethodProxy readMethod = propertyReadMethods.get(name);
		
		
		if (readMethod != null)
			
			// dunno why this was commented out
			return MethodInvocationManager.invokeMethod(readMethod, 
													  targetObject,
													  emptyParams);	
													  
			//return readMethod.invoke(targetObject, emptyParams);
		else {
		Object[] params = {name};
		
		 MethodProxy dynamicPropertyGetter = propertyDynamicGetters.get(name);
		
			
		
		/*
		return uiMethodInvocationManager.invokeMethod(dynamicPropertyGetter, 
				  targetObject,
				  params);	
				  */
		 return dynamicPropertyGetter.invoke(targetObject, params);
		}
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		
	}
	protected void setDynamicProperty(String name, Object newVal) {
		Object value = propertyValues.get(name);
		if (value != null ) {
			return;
			//propertyValues.put(name, newVal);
			//return value;
		}
		Object targetObject = propertyTargetObjects.get(name);
		if (targetObject == null) return ;
		try {
			Object[] params = {newVal};			
			MethodProxy writeMethod = propertyWriteMethods.get(name);
			
			if (writeMethod == null) {
				writeMethod = propertyDynamicSetters.get(name);
				Object[] params2 = {name, newVal};
				params = params2;
			}
			
			writeMethod.invoke(targetObject, newVal);
			/*
			return uiMethodInvocationManager.invokeMethod (
					writeMethod,
					targetObject,													
					params);
					*/
			
		} catch (Exception e) {
			
		}
		
	}
	
	protected Vector<MethodProxy> getVirtualMethods() {
		return virtualMethods;
	}
	
	

}
