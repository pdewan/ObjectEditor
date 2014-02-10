package bus.uigen.viewgroups;

//import java.beans.MethodDescriptor;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

//import bus.uigen.controller.uiMethodInvocationManager;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.PropertyDescriptorProxy;
import bus.uigen.introspect.VirtualMethodDescriptor;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AClassProxy;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.BEAN_PATTERN)
public class APropertyAndCommandFilter {
	public static final ClassProxy propertyAndCommandFilterClass = AClassProxy.classProxy(APropertyAndCommandFilter.class);

	/*
	Vector<VirtualMethod> virtualMethods = new Vector();
	String[] propertyNames = new String[0];
	Vector<String> propertyNameVector = new Vector();
	Hashtable<String, Method> propertyReadMethods = new Hashtable();
	Hashtable<String, Method> propertyWriteMethods = new Hashtable();
	Hashtable<String, Method> propertyDynamicGetters = new Hashtable();
	Hashtable<String, Method> propertyDynamicSetters = new Hashtable();
	Hashtable<String, Object> propertyTargetObjects = new Hashtable();
	Hashtable<String, APropertyAndCommandFilter> propertyValues = new Hashtable();
	*/
	String virtualClass;
	DescriptorViewSupport descriptorViewSupport; 
	
	
	
	public APropertyAndCommandFilter (Object theTargetObject, Vector<PropertyDescriptorProxy> theProperties, Vector<VirtualMethodDescriptor> theCommands) {
		descriptorViewSupport = new DescriptorViewSupport(theTargetObject, theProperties, theCommands);
		set(theTargetObject, theProperties, theCommands);
		
	}
	
	public APropertyAndCommandFilter() {
		descriptorViewSupport = new DescriptorViewSupport();
		// TODO Auto-generated constructor stub
	}
	
	public String getVirtualClass() {
		return virtualClass;
	}
	
	public void setVirtualClass (String newVal) {
		 virtualClass = newVal;
	}

	protected void set(Object theTargetObject, Vector<PropertyDescriptorProxy> theProperties, Vector<VirtualMethodDescriptor> theCommands) {
		descriptorViewSupport.set(theTargetObject, theProperties);
		//addProperties (theTargetObject, theProperties);
	}
	protected void addProperty(Object theTargetObject, PropertyDescriptorProxy theProperty) {
		descriptorViewSupport.addProperty(theTargetObject, theProperty);
		/*
		Vector<PropertyDescriptor> theProperties = new Vector();
		theProperties.addElement(theProperty);
		addProperties (theTargetObject, theProperties);
		*/
	}
	protected void addPropertyGroup(String name, APropertyAndCommandFilter value) {
		descriptorViewSupport.addPropertyGroup(name, value);
		/*
		String[] newPropertyNames = new String[propertyNames.length + 1];

		for (int i = 0; i < propertyNames.length; i ++) {
			newPropertyNames[i] = propertyNames[i];
		}
		newPropertyNames[propertyNames.length] = name;
		propertyNames = newPropertyNames;
		propertyNameVector.add(name);
		propertyValues.put(name, value);
		*/
		
	}
	protected APropertyAndCommandFilter getPropertyGroup(String name) {
		return descriptorViewSupport.getPropertyGroup(name);
		//return propertyValues.get(name);
	}
	protected void addMethod(Object theTargetObject, MethodDescriptorProxy theCommand) {
		descriptorViewSupport.addMethod(theTargetObject, theCommand);
		/*
		Vector<MethodDescriptor> theMethods = new Vector();
		theMethods.addElement(theCommand);
		addMethods(theTargetObject, theMethods);
		*/
	}
	protected void addProperties (Object theTargetObject, PropertyDescriptorProxy[] theProperties) {
		descriptorViewSupport.addProperties(theTargetObject, theProperties);
	}
	
	protected void addProperties (Object theTargetObject, Vector<PropertyDescriptorProxy> theProperties) {
		
		descriptorViewSupport.addProperties(theTargetObject, theProperties);
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
		descriptorViewSupport.addMethods(theTargetObject, theMethods);
		/*
		for (int i = 0; i < theMethods.size(); i++) {
			virtualMethods.addElement(VirtualMethodDescriptor.getVirtualMethod(theMethods.elementAt(i)).moveFromObject(theTargetObject));
		}
		*/
		
		
	}
	//Object[] emptyParams = {};
	public String[] getDynamicProperties() {
		return descriptorViewSupport.getDynamicProperties();
		//return propertyNames;
	}
	public Object getDynamicProperty(String name) {
		return descriptorViewSupport.getDynamicProperty(name);
		/*
		Object value = propertyValues.get(name);
		if (value != null) return value;
		Object targetObject = propertyTargetObjects.get(name);
		if (targetObject == null) return null;
		try {
		Method readMethod = propertyReadMethods.get(name);
		
		
		if (readMethod != null)
			
			
			return readMethod.invoke(targetObject, emptyParams);
		else {
		Object[] params = {name};
		
		 Method dynamicPropertyGetter = propertyDynamicGetters.get(name);
		
			
	
		 return dynamicPropertyGetter.invoke(targetObject, params);
		}
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		*/
		
	}
	public void setDynamicProperty(String name, Object newVal) {
		descriptorViewSupport.setDynamicProperty(name, newVal);
		/*
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
			Method writeMethod = propertyWriteMethods.get(name);
			
			if (writeMethod == null) {
				writeMethod = propertyDynamicSetters.get(name);
				Object[] params2 = {name, newVal};
				params = params2;
			}
			
			writeMethod.invoke(targetObject, newVal);
			
			
		} catch (Exception e) {
			
		}
		*/
		
	}
	
	public Vector<MethodProxy> getVirtualMethods() {
		return descriptorViewSupport.getVirtualMethods();
		/*
		return virtualMethods;
		*/
	}
	
	public boolean onlyDynamicCommands() {
		return true;
	}
	public boolean onlyDynamicProperties() {
		return true;
	}
	
	public void invokeDynamicCommand (String name) {
		
	}

}
