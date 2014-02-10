package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;

import util.annotations.StructurePatternNames;
import util.trace.Tracer;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.FieldProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.DynamicMethods;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.trace.PublicVariable;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
public class BeanToRecord extends AbstractConcreteType implements RecordStructure {
	transient  Map<String, Object>  componentTable = new Hashtable();
	transient Map<String, PropertyDescriptorProxy> propertyTable = new Hashtable();
	transient Hashtable<String, String> lowerCaseToRealName = new Hashtable();
	transient Hashtable<String,MethodProxy> preReadTable  = new Hashtable();
	transient Hashtable<String,MethodProxy> preWriteTable = new Hashtable();
	transient Hashtable<String,MethodProxy> validateTable  = new Hashtable();
	transient Map<String,MethodProxy> isEditableTable  = new Hashtable();
	transient Vector sortedComponentNames = new Vector();
	transient Vector sortedNonGraphicsComponentNames = new Vector();
	transient Vector sortedGraphicsComponentNames = new Vector();
	transient MethodProxy getUserObjectMethod = null;
	transient MethodProxy setUserObjectMethod = null;
	transient MethodProxy getExpansionObjectMethod = null;
	transient MethodProxy dynamicPropertyGetter = null;
	transient MethodProxy dynamicPropertySetter = null;
	transient MethodProxy dynamicPropertyPreRead = null;
	transient MethodProxy dynamicPropertyPreWrite = null;
	transient MethodProxy dynamicPropertyValidate = null;
	transient MethodProxy dynamicPropertyIsEditable = null;
	transient MethodProxy dynamicPropertyTypeGetter = null;
	transient MethodProxy isEditableMethod = null;
	ClassDescriptorInterface cdesc;
	String soleProperty;
	static Set<String> publicVariableMessageGiven = new HashSet();
	static String[] excludeProperties = {"parent", "ClientHost", "Log", "ref", "CommonPort", "virtualClass", 
									DynamicMethods.DYNAMIC_PROPERTY_PROPERTY, 
									DynamicMethods.DYNAMIC_METHOD_PROPERTY,
									DynamicMethods.DYNAMIC_COMMAND_PROPERTY,
									//"DynamicCommands", "virtualMethods", 
									"editable", "expansionObject", "indexedElementChecker", "bounds"};
	//String[] excludeProperties = {"parent", "clienthost", "log", "ref", "commonport", "virtualclass", DynamicMethods.GET_DYNAMIC_COMMANDS, "DynamicCommands", "virtualMethods", "editable", "expansionObject"};
	static Vector excludedPropertiesVector = uiGenerator.arrayToVector(excludeProperties);
	 String[] excludeProperties() {
		//String[] retVal = {};
		return excludeProperties;
	}
	 
	 public MethodProxy getReadMethod(String propertyName) {
			PropertyDescriptorProxy property = propertyTable.get(propertyName.toLowerCase());
			if (property != null)
			return property.getReadMethod();
			return null;
		 }
	 
	 
	 public MethodProxy getWriteMethod(String propertyName) {
			PropertyDescriptorProxy property = propertyTable.get(propertyName.toLowerCase());
			if (property != null)
			return property.getWriteMethod();
			return null;
		 }
	 
	 public MethodProxy getReadMethod(String propertyName, ClassProxy aClassProxy) {
		PropertyDescriptorProxy property = propertyTable.get(propertyName.toLowerCase());
		if ((property != null) && aClassProxy.isAssignableFrom(property.getPropertyType())) 
		return property.getReadMethod();
		return null;
	 }
	 
	 public MethodProxy getWriteMethod(String propertyName, ClassProxy aClassProxy) {
			PropertyDescriptorProxy property = propertyTable.get(propertyName.toLowerCase());
			if ((property != null) && aClassProxy.isAssignableFrom(property.getPropertyType())) 
			return property.getWriteMethod();
			return null;
		 }
	 
	 public boolean excluded (String property) {
	 	//Vector excludedPropertiesVector = uiGenerator.arrayToVector(excludeProperties());
	 	//return(excludedPropertiesVector.contains(property.toLowerCase()));
	 	return(excludedPropertiesVector.contains(property));
	 }
	 
	 public boolean isEditingMethod(MethodProxy targetMethod) {
		 return IntrospectUtility.isGetter(targetMethod, componentNames()) || IntrospectUtility.isSetter(targetMethod, componentNames());
	 }
	 
	 
	
	 public Hashtable<String, MethodProxy> getPreReadTable() {
		return preReadTable;
	}
	 
	 public Hashtable<String, MethodProxy> getPreWriteTable() {
			return preWriteTable;
		}

//	public void setPreReadTable(Hashtable<String, MethodProxy> preReadTable) {
//		this.preReadTable = preReadTable;
//	}

	boolean excludeFields () {
	 	return false;
	 }	
	public BeanToRecord (ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {		init(theTargetClass, theTargetObject, theFrame );
		//filterMethodDescriptors(theTargetObject);
	}
	/*
	public BeanToRecord (Class theTargetClass, Object theTargetObject, uiFrame theFrame, String theSoleProperty) {
		soleProperty = theSoleProperty;
		init(theTargetClass, theTargetObject, theFrame );
		if (get(soleProperty) == null) {
			System.out.println("E*** Object " + theTargetObject + " does not have property: " + soleProperty);
		}
		//filterMethodDescriptors(theTargetObject);
	}
	*/
	/*
    public  void filterMethodDescriptors (Object obj) {
    	if (obj == null) return;
    	ViewInfo cd = ClassDescriptorCache.getClassDescriptor(obj.getClass(), obj);
    	MethodDescriptor[] md = cd.getMethodDescriptors();
    	Vector v = new Vector();
    	for (int i = 0; i < md.length; i++) {
    		VirtualMethod m = VirtualMethodDescriptor.getVirtualMethod(md[i]);
    		//if (uiBean.isGetter(m) || uiBean.isSetter(m) || uiBean.isPre(m)) continue;
    		if (excludeMethod(m)) continue;
    		v.add(md[i]);
    	}
    	cd.setMethodDescriptors(v);
    	
    	
		
	}
    */
	// never called as far as I can tell, AMethodProcessor seems to filter out regular commands
	// and isPatternMethod takes care of the rest
    boolean excludeMethod (MethodProxy m) {
    	return (IntrospectUtility.isGetter(m) || IntrospectUtility.isSetter(m) || IntrospectUtility.isPre(m));
    }
    
    
   
		public BeanToRecord () {		
	}
	
	public Vector componentNames() {
		return sortedComponentNames;	}
	
	public Vector nonGraphicsComponentNames() {
		return sortedNonGraphicsComponentNames;
	}
	
	public Vector graphicsComponentNames() {
		return sortedGraphicsComponentNames;
	}
	
	public boolean isGraphics(String componentName) {
		return sortedGraphicsComponentNames.contains(componentName);
	}
	public Object get (String componentAnyCaseName) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			return null;
		// for some reasom this 
//		if (!preRead(componentName))
//			return null;
		Object componentDescriptor = componentTable.get(componentName/*.toLowerCase()*/);
		Object child = null;
		if (componentDescriptor instanceof PropertyDescriptorProxy )
			//return get ((PropertyDescriptorProxy ) componentDescriptor);
			child = get ((PropertyDescriptorProxy ) componentDescriptor);
		else 
			//return get ((FieldProxy) componentDescriptor);
			child = get ((FieldProxy) componentDescriptor);
		/*
		if (this.getTargetObject() instanceof ALoggableModel)
			return LoggableRegistry.createLoggableModel(child);
			*/
		return child;	}
	static Object[] emptyParams = {};	public Object get (PropertyDescriptorProxy  pd) {
		if (pd.getReadMethod() != null)
		
		
			return MethodInvocationManager.invokeMethod(pd.getReadMethod(), 
													  targetObject,
													  emptyParams);	
		else {
		Object[] params = {pd.getName()};
		return MethodInvocationManager.invokeMethod(dynamicPropertyGetter,
				    //(Object) null,
				  targetObject,
				  params);	
		}
	}	public Object get (FieldProxy field) {		try {
			return field.get(targetObject);
		} catch (Exception e) {			return null;
		}	}
	ClassProxy dynamicComponentType (String componentAnyCaseName) {
		if (dynamicPropertyTypeGetter == null) return null;
		
		Object[] params = {componentAnyCaseName};
	try {
		
		Object classOrClassProxy =  MethodInvocationManager.invokeMethod(dynamicPropertyTypeGetter, targetObject, params);
		return  RemoteSelector.classProxy(classOrClassProxy);
	} catch (Exception e) {
		return null;
	}
	}
	public ClassProxy componentType (String componentAnyCaseName) {
		ClassProxy retVal = dynamicComponentType(componentAnyCaseName);
		if (retVal != null) return retVal;
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			return null;		Object componentDescriptor = componentTable.get(componentName/*.toLowerCase()*/);
		if (componentDescriptor instanceof PropertyDescriptorProxy )
			return ((PropertyDescriptorProxy ) componentDescriptor).getPropertyType();
		else 			return (((FieldProxy) componentDescriptor)).getDeclaringClass();
	}
	boolean isReadOnly (PropertyDescriptorProxy  pd) {
		if (pd.getReadMethod() != null)
			return pd.getWriteMethod() == null;
		else
			return dynamicPropertySetter == null;
		
	}	public boolean isReadOnly (String componentAnyCaseName) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			return false;
		Object componentDescriptor = componentTable.get(componentName/*.toLowerCase()*/);
		if (componentDescriptor instanceof PropertyDescriptorProxy ) {
			
			//return ((PropertyDescriptor) componentDescriptor).getWriteMethod() == null;
			return isReadOnly ((PropertyDescriptorProxy ) componentDescriptor);
		} else 
			return isReadOnly ((FieldProxy) componentDescriptor);	}	public static boolean isReadOnly (FieldProxy field) {
		return Modifier.isFinal(field.getModifiers());	}
	public Object set (String componentAnyCaseName, Object value, CommandListener commandListener) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			return null;
		Object componentDescriptor = componentTable.get(componentName/*.toLowerCase()*/);
		if (componentDescriptor instanceof PropertyDescriptorProxy ) {
			return set ((PropertyDescriptorProxy ) componentDescriptor, value, commandListener);
			/*			
			return uiMethodInvocationManager.invokeMethod (frame,													targetObject,													((PropertyDescriptor) componentDescriptor).getWriteMethod(),													params,													commandListener);			*/
		} else  
			return set ((FieldProxy) componentDescriptor, value);
		
	}
	public Object set (String componentAnyCaseName, Object value) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			return null;
		Object componentDescriptor = componentTable.get(componentName/*.toLowerCase()*/);
		if (componentDescriptor instanceof PropertyDescriptorProxy ) {
			return set ((PropertyDescriptorProxy ) componentDescriptor, value);
			/*			
			return uiMethodInvocationManager.invokeMethod (frame,													targetObject,													((PropertyDescriptor) componentDescriptor).getWriteMethod(),													params,													commandListener);			*/
		} else  
			return set ((FieldProxy) componentDescriptor, value);
		
	}
	public Object set (FieldProxy field, Object value) {
		try {
			
				field.set(targetObject, value);
				return null;
			   } catch (Exception e) {
					 return null;
			   }
	}
	public Object set (PropertyDescriptorProxy  property, Object value, CommandListener commandListener) {
		
		
		MethodProxy writeMethod = AVirtualMethod.virtualMethod (property.getWriteMethod());
		MethodProxy readMethod = AVirtualMethod.virtualMethod (property.getReadMethod());
		Object[] params = {value};
		if (writeMethod == null) {
			writeMethod = AVirtualMethod.virtualMethod (dynamicPropertySetter);
			readMethod = AVirtualMethod.virtualMethod(dynamicPropertyGetter);
			Object[] params2 = {property.getName(), value};
			params = params2;
		}
		if (writeMethod == null || readMethod == null) 
			return null;
		return frame.getUndoer().execute (CommandCreator.createCommand(commandListener, writeMethod, targetObject, params));
		/*
		return frame.getUndoer().execute (
				new SetGetLastCommand(
				  					commandListener,
				  					writeMethod,
				  					targetObject,
				  					params,
				  					readMethod//,
				  					//readMethodParams
				  					));
				  					*/
		/*
			return frame.getUndoer().execute (
						new SetGetLastCommand(						  					commandListener,
						  					property.getWriteMethod(),						  					targetObject,						  					params,						  					property.getReadMethod()//,
						  					//readMethodParams
						  					));
						  					*/
	}
	public Object set (PropertyDescriptorProxy  property, Object value) {
		Object[] params = {value};
		//VirtualMethod writeMethod = uiMethodInvocationManager.virtualMethod ( property.getWriteMethod());
		MethodProxy writeMethod = property.getWriteMethod();
		if (writeMethod == null) {
			//writeMethod = uiMethodInvocationManager.virtualMethod (dynamicPropertySetter);
			writeMethod = dynamicPropertySetter;
			Object[] params2 = {property.getName(), value};
			params = params2;
		}
		return MethodInvocationManager.invokeMethod (
				writeMethod,
				targetObject,													
				params);
		/*
			return uiMethodInvocationManager.invokeMethod (
													property.getWriteMethod(),													targetObject,																										params);
													*/
	}
	boolean isDynamicProperty (String componentAnyCaseName) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			return false;
		Object descriptor = componentTable.get(componentName);
		if (!(descriptor instanceof PropertyDescriptorProxy )) return false;
		//PropertyDescriptor pd = (PropertyDescriptor) componentTable.get(componentName) ;
		PropertyDescriptorProxy  pd = (PropertyDescriptorProxy ) descriptor;
		return pd != null && pd.getReadMethod() == null;
	}	public boolean preRead (String componentAnyCaseName) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			//return false;
			return true;
		MethodProxy preReadMethod = (MethodProxy) preReadTable.get(componentName/*.toLowerCase()*/);
		Object[] params = emptyParams;
		if (isDynamicProperty (componentName)) {
			preReadMethod = AVirtualMethod.virtualMethod (dynamicPropertyPreRead);
			Object[] params2 = {componentName};
			params = params2;
		}		if (preReadMethod == null) return true;
		return ((Boolean) MethodInvocationManager.invokeMethod(preReadMethod, targetObject, params)).booleanValue();		//return ((Boolean) uiMethodInvocationManager.invokeMethod(preReadMethod, targetObject, emptyParams)).booleanValue();		
	}
		public boolean preWrite (String componentAnyCaseName) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			//return false;
			return true;
		MethodProxy preWriteMethod = (MethodProxy) preWriteTable.get(componentName/*.toLowerCase()*/);
		Object[] params = emptyParams;
		if (isDynamicProperty (componentName)) {
			preWriteMethod = AVirtualMethod.virtualMethod (dynamicPropertyPreWrite);
			Object[] params2 = {componentName};
			params = params2;
		}		if (preWriteMethod == null) return true;
		return ((Boolean) MethodInvocationManager.invokeMethod(preWriteMethod, targetObject, params));		//return ((Boolean) uiMethodInvocationManager.invokeMethod(preWriteMethod, targetObject, emptyParams)).booleanValue();
	}
	public boolean validate (String componentAnyCaseName, Object newVal) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			//return false;
			return true;
		MethodProxy validateMethod = (MethodProxy) validateTable.get(componentName/*.toLowerCase()*/);
		Object[] params = {newVal};
		if (isDynamicProperty (componentName)) {
			validateMethod = AVirtualMethod.virtualMethod (dynamicPropertyValidate);
			Object[] params2 = {componentName, newVal};
			params = params2;
		}
		if (validateMethod == null) return true;
		return ((Boolean) MethodInvocationManager.invokeMethod(validateMethod, targetObject, params));
		//return ((Boolean) uiMethodInvocationManager.invokeMethod(preWriteMethod, targetObject, emptyParams)).booleanValue();
	}
	public boolean isEditable (String componentAnyCaseName) {
		String componentName = lowerCaseToRealName.get(componentAnyCaseName.toLowerCase());
		if (componentName == null)
			//return false;
			return true;
		MethodProxy isEditableMethod = (MethodProxy) isEditableTable.get(componentName/*.toLowerCase()*/);
		Object[] params = {};
		if (isDynamicProperty (componentName)) {
			isEditableMethod = AVirtualMethod.virtualMethod (dynamicPropertyIsEditable);
			Object[] params2 = {componentName};
			params = params2;
		}
		if (isEditableMethod == null) return true;
		return ((Boolean) MethodInvocationManager.invokeMethod(isEditableMethod, targetObject, params));
		//return ((Boolean) uiMethodInvocationManager.invokeMethod(preWriteMethod, targetObject, emptyParams)).booleanValue();
	}
	
	public boolean isEditable () {
		if (isEditableMethod == null)
			return true;
		Object[] params = {};
		return ((Boolean) MethodInvocationManager.invokeMethod(isEditableMethod, targetObject, params));
		//return ((Boolean) uiMethodInvocationManager.invokeMethod(preWriteMethod, targetObject, emptyParams)).booleanValue();
	}
  
  public void setMethods() {
	  setMethods(targetClass);
  }
 
	
	public void setMethods(ClassProxy objectClass) {			componentTable = new Hashtable();
		preReadTable = new Hashtable();
		preWriteTable = new Hashtable();
		validateTable = new Hashtable();
		sortedComponentNames = new Vector();
		isEditableMethod = IntrospectUtility.getIsEditableMethod(objectClass);
		LoggableRegistry.setMethodIsReadOnly( isEditableMethod );
		dynamicPropertyGetter = DynamicMethods.getDynamicPropertyGetter(objectClass);
		if (dynamicPropertyGetter != null) {
			dynamicPropertySetter = DynamicMethods.getDynamicPropertySetter(objectClass, dynamicPropertyGetter.getReturnType());
			dynamicPropertyPreRead = DynamicMethods.getDynamicPropertyPreRead(objectClass);
			dynamicPropertyPreWrite = DynamicMethods. getDynamicPropertyPreWrite(objectClass);
			dynamicPropertyValidate = DynamicMethods.getDynamicPropertyValidate(objectClass);
			dynamicPropertyIsEditable = DynamicMethods.getDynamicPropertyIsEditable(objectClass);
			LoggableRegistry.setMethodIsReadOnly(dynamicPropertyPreRead);
			LoggableRegistry.setMethodIsReadOnly(dynamicPropertyPreWrite);
			LoggableRegistry.setMethodIsReadOnly(dynamicPropertyValidate);
			LoggableRegistry.setMethodIsReadOnly(dynamicPropertyIsEditable);
			
		}
		dynamicPropertyTypeGetter = DynamicMethods.getDynamicPropertyTypeGetter(objectClass);
		LoggableRegistry.setMethodIsReadOnly(dynamicPropertyTypeGetter);
		//ViewInfo cdesc;		if (targetObject == null)
			 cdesc = ClassDescriptorCache.getClassDescriptor(objectClass);
		else
			cdesc = ClassDescriptorCache.getClassDescriptor(objectClass, targetObject);
		//ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(objectClass);		/*
		PropertyDescriptor[] properties = cdesc.getPropertyDescriptors();
		//MethodDescriptor[] methodDescriptors = cdesc.getMethodDescriptors();
		FieldDescriptor fieldDescriptors[] = cdesc.getFieldDescriptors();		for (int i = 0; i < fieldDescriptors.length; i++) {
			Field  field = fieldDescriptors[i].getField();			componentTable.put(field.getName(), field);		}		
		for (int i = 0; i < properties.length; i++) {			PropertyDescriptor property = properties[i];
			componentTable.put(property.getName(), property);
			Method preRead = uiBean.getPre(properties[i].getReadMethod(), targetClass);			if (preRead != null)				preReadTable.put(property.getName(), preRead);
			Method preWrite = uiBean.getPre(properties[i].getWriteMethod(), targetClass);						if (preWrite != null)				preWriteTable.put(property.getName(), preWrite);		}
		*/
		FeatureDescriptorProxy[] features = cdesc.getFeatureDescriptors();
		if (features == null) 
			return;	
		if (!unparseAsToString(cdesc)) {
		addProperties(cdesc);
		addFields(cdesc);
		}
		/*
		for (int i=0; i<features.length; i++) {	
			if (features[i] instanceof PropertyDescriptor) {
				PropertyDescriptor property = (PropertyDescriptor) features[i];		
				String name = property.getName();
				if (excluded (name)) continue;
				if (name.equals("userObject") || name.equals("selfObject")) continue;				if (property.getReadMethod() == null && dynamicPropertyGetter == null) continue;	
					
				componentTable.put(property.getName(), property);
				VirtualMethod preRead = uiBean.getPre(uiMethodInvocationManager.virtualMethod (property.getReadMethod()), targetClass);
			    //VirtualMethod preRead = uiBean.getPre(property.getReadMethod(), targetClass);				if (preRead != null)					preReadTable.put(property.getName(), preRead);
				VirtualMethod preWrite = uiBean.getPre(uiMethodInvocationManager.virtualMethod(property.getWriteMethod()), targetClass);							if (preWrite != null)					preWriteTable.put(property.getName(), preWrite);				sortedComponentNames.addElement(property.getName());
			} else if (features[i] instanceof FieldDescriptor) {
				if (excludeFields()) return;				Field  field = ((FieldDescriptor) features[i]).getField();
				if (componentTable.get(field.getName()) == null)					componentTable.put(field.getName(), field);				sortedComponentNames.addElement(field.getName());
			}
		}
		*/
		  getUserObjectMethod = IntrospectUtility.getTreeGetUserObjectMethod(objectClass);
		  setUserObjectMethod = IntrospectUtility.getTreeSetUserObjectMethod(objectClass);
		  getExpansionObjectMethod = IntrospectUtility.getExpansionObjectMethod(objectClass);		
	}	
	boolean isVisible(PropertyDescriptorProxy  pd) {
		try {
		if (pd == null) return true;		
		// under current semantics tey are the same value
		Boolean visible = (Boolean) pd.getValue(AttributeNames.VISIBLE);
//		Boolean indirectVisible = (Boolean) pd.getValue(AttributeNames.INDIRECTLY_VISIBLE);
		if (visible == null)
			return true;
//			visible = true;
//		if (indirectVisible == null)
//			 indirectVisible = false;
//		return visible && !indirectVisible;	
		return visible;
//		return ((Boolean) visible);
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
	boolean unparseAsToString(ClassDescriptorInterface cd) {
		Object val = cd.getAttribute(AttributeNames.DISPLAY_TO_STRING);
		return val != null && (Boolean) val;
		
	}
	public void addProperties(ClassDescriptorInterface cdesc) {	
		
		//FeatureDescriptor[] features = cdesc.getFeatureDescriptors();
		FeatureDescriptorProxy[] properties = cdesc.getFeatureDescriptors();
		//PropertyDescriptor[] properties = cdesc.getPropertyDescriptors();
		if (properties == null) 
			return;	
		
		for (int i=0; i<properties.length; i++) {	
			if (properties[i] instanceof PropertyDescriptorProxy ) {
				PropertyDescriptorProxy  property = (PropertyDescriptorProxy ) properties[i];	
				//if (!isVisible(property)) continue;
				String name = property.getName();
				if (excluded (name)) continue;
				if (name.equals("userObject") || name.equals("selfObject")) continue;
				if (property.getReadMethod() == null && dynamicPropertyGetter == null) continue;
//				Boolean visibleAttribute = (Boolean) property.getValue(AttributeNames.VISIBLE);
//				
//				if (visibleAttribute != null 
//						&& !visibleAttribute) 
				if (!isVisible(property))
					continue;
				LoggableRegistry.setMethodIsReadOnly(property.getReadMethod());
				componentTable.put(property.getName()/*.toLowerCase()*/, property);
				propertyTable.put(property.getName()/*.toLowerCase()*/, property);
				MethodProxy preRead = IntrospectUtility.getPre(AVirtualMethod.virtualMethod (property.getReadMethod()), targetClass);
			    //VirtualMethod preRead = uiBean.getPre(property.getReadMethod(), targetClass);
				
				if (preRead != null) {
					preReadTable.put(property.getName()/*.toLowerCase()*/, preRead);
					LoggableRegistry.setMethodIsReadOnly(preRead);
				}
				MethodProxy preWrite = IntrospectUtility.getPre(AVirtualMethod.virtualMethod(property.getWriteMethod()), targetClass);			
				if (preWrite != null) {
					preWriteTable.put(property.getName()/*.toLowerCase()*/, preWrite);
					LoggableRegistry.setMethodIsReadOnly(preWrite);
				}
				MethodProxy validate = IntrospectUtility.getValidate(AVirtualMethod.virtualMethod(property.getWriteMethod()), targetClass);
				if (validate != null) {
					validateTable.put(property.getName(), validate);
					LoggableRegistry.setMethodIsReadOnly(validate);
				}
				MethodProxy isEditable = IntrospectUtility.getIsPropertyEditableMethod(targetClass, property.getName());
				if (isEditable != null) {
					isEditableTable.put(property.getName(), isEditable);
					LoggableRegistry.setMethodIsReadOnly(isEditable);
				}
				//sortedComponentNames.addElement(property.getName());
				sortedComponentNames.addElement(property.getName()/*.toLowerCase()*/);
				Boolean onlyGraphics = (Boolean) AClassDescriptor.getAnnotationAttribute( properties[i], AttributeNames.ONLY_GRAPHIICAL_DESCENDENTS);

//				Boolean onlyGraphics = (Boolean) properties[i].getValue(AttributeNames.ONLY_GRAPHIICAL_DESCENDENTS);
				if (onlyGraphics == null || !onlyGraphics)
					sortedNonGraphicsComponentNames.addElement(property.getName());
				else 
					sortedGraphicsComponentNames.addElement(property.getName());
				lowerCaseToRealName.put(property.getName().toLowerCase(),property.getName());
			
		}
		}
		
	}
public void addFields(ClassDescriptorInterface cdesc) {	
		
		FeatureDescriptorProxy[] features = cdesc.getFeatureDescriptors();
		if (features == null) 
			return;	
		for (int i=0; i<features.length; i++) {	
			if (features[i] instanceof FieldDescriptorProxy) {
				if (excludeFields()) return;
				FieldProxy  field = ((FieldDescriptorProxy) features[i]).getField();
				if (componentTable.get(field.getName()) == null) {
					componentTable.put(field.getName(), field);
					//componentTable.put(field.getName().toLowerCase, field);
					//sortedComponentNames.addElement(field.getName());
					sortedComponentNames.addElement(field.getName()/*.toLowerCase()*/);
					Boolean onlyGraphics = (Boolean) features[i].getValue(AttributeNames.ONLY_GRAPHIICAL_DESCENDENTS);
					if (onlyGraphics == null || !onlyGraphics)
						sortedNonGraphicsComponentNames.addElement(field.getName());
					lowerCaseToRealName.put(field.getName().toLowerCase(), field.getName());
//					Tracer.warning("Found public variable:" + field.getName() + " in class: " + targetClass + " Will lead to same object displayed twice if it is also a property.");
					String classAndField = targetClass.getName() + ":" + field.getName();
					if (!publicVariableMessageGiven.contains(classAndField)) {
					    PublicVariable.newCase(field, targetClass, this);
					    publicVariableMessageGiven.add(classAndField);
					}
				}
			}
			
		}
		
	}
	public Object getUserObject (Object o) {
		try {
		//getUserObjectMethod = uiBean.getTreeGetUserObjectMethod(o.getClass());
		if (getUserObjectMethod == null || targetObject == null) return null;
		Object[] nullObjectArgs = {};
		//return getUserObjectMethod.invoke(o, nullObjectArgs);
		return MethodInvocationManager.invokeMethod(targetObject, getUserObjectMethod, nullObjectArgs);
		} catch (Exception e) {
			return null;
		}
		
	}
	public Object getExpansionObject () {
		try {
		//getUserObjectMethod = uiBean.getTreeGetUserObjectMethod(o.getClass());
		if (getExpansionObjectMethod == null) return null;
		Object[] nullObjectArgs = {};
		//return getExpansionObjectMethod.invoke(targetObject, nullObjectArgs);
		return MethodInvocationManager.invokeMethod(targetObject, getExpansionObjectMethod, nullObjectArgs);
		} catch (Exception e) {
			return null;
		}
		
	}
	public Object getUserObject () {
		return getUserObject(targetObject);
		
	}
	public void setUserObject (Object o, Object newVal) {
		try {

		Class[] userObjectArgs = {Object.class};
		//setUserObjectMethod = uiBean.getTreeSetUserObjectMethod(o.getClass());		
		if (setUserObjectMethod == null) return ;
		Object[] objectArgs = {newVal};
		//setUserObjectMethod.invoke(o, objectArgs);
		MethodInvocationManager.invokeMethod(o, setUserObjectMethod, objectArgs);
		} catch (Exception e) {
			return ;
		}
		
	}
	public void setUserObject (Object newVal) {
		setUserObject (targetObject, newVal);		
		
	}
	public boolean hasUserObject() {
		return getUserObjectMethod != null;
	}
	public boolean hasEditableUserObject() {
		return setUserObjectMethod != null;
	}
	public static final String RECORD = "Record";
	public String programmingPatternKeyword() {
		return AbstractConcreteType.PROGRAMMING_PATTERN + AttributeNames.KEYWORD_SEPARATOR + RECORD;
	}
	public String typeKeyword() {
		return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + RECORD;
	}
	public boolean hasPreconditions() {
		return preReadTable.size() != 0 || preWriteTable.size() != 0;
	}
	public boolean hasValidation() {
		return validateTable.size() != 0 ;
	}
	public String getPatternName() {
		if (componentTable.size() == 0) {
			return StructurePatternNames.NO_PATTERN;
		}
		return StructurePatternNames.BEAN_PATTERN;
		
	}
	public String getPatternPath() {
		return getPatternName();
		
	}
	/*
	public static Vector<Attribute> merge (Vector<Attribute> list1, Vector<Attribute> list2) {
		if (list1 == null)
			return list2;
		else if (list2 == null)
			return list1;
		else {
			for (int i = 0; i < list2.size(); i++)
				list1.add((list2.elementAt(i)));
			return list1;
		}
		
	}
	*/
	@Override
	public Vector<Attribute> getComponentAttributes(String componentName) {
		// TODO Auto-generated method stub
		Object retVal;
		Object componentDescriptor = componentTable.get(componentName/*.toLowerCase()*/);
		if (componentDescriptor instanceof PropertyDescriptorProxy ) {
			PropertyDescriptorProxy pd = ((PropertyDescriptorProxy ) componentDescriptor);
			return (Vector) pd.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
			//return ((PropertyDescriptorProxy ) componentDescriptor).
			/*
			PropertyDescriptorProxy pd = ((PropertyDescriptorProxy ) componentDescriptor);
			MethodProxy readMethod = pd.getReadMethod();
			MethodDescriptorProxy readMD = 	cdesc.getMethodDescriptor(readMethod);
			Vector<Attribute> readIntAttributes = null;
			if (readMD != null)
				readIntAttributes = (Vector) readMD.getValue(AttributeNames.INT_ATTRIBUTES_ANNOTATION);
			MethodProxy writeMethod = pd.getWriteMethod();
			MethodDescriptorProxy writeMD = cdesc.getMethodDescriptor(writeMethod);
			Vector<Attribute> writeIntAttributes = null;
			if (writeMD != null)
				writeIntAttributes = (Vector) writeMD.getValue(AttributeNames.INT_ATTRIBUTES_ANNOTATION);
			
			return merge(readIntAttributes, writeIntAttributes);
			*/
		} else 
			// should probbaly look at annotation of field
			//return get ((FieldProxy) componentDescriptor);
			return null;
	}
	
	public Set<String> getPropertyNames() {
		return propertyTable.keySet();
	}
	
	public Set<String> getEdtitablePropertyNames() {
		return isEditableTable.keySet();
	}
	
	
}