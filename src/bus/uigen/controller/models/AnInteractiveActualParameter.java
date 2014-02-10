package bus.uigen.controller.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.lang.reflect.Modifier;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.DynamicEnum;
import util.trace.Tracer;

//import org.eclipse.swt.internal.ole.win32.ISpecifyPropertyPages;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.ObjectParameterListener;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.introspect.uiClassMapper;
import bus.uigen.misc.OEMisc;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AnInteractiveActualParameter implements InteractiveActualParameter {
	AnInteractiveMethodInvoker methodInvoker;
	String parameterName;
	ClassProxy parameterType;
	Object parameterTypeChoices;
	String originalParameterType;
	Object originalValue;
	Object parameterValue;
	//Object parameterDisplay;
	int parameterNumber;
	boolean showType;
	boolean isValueParsable = true;
	boolean isTypePrimitive = true;
	public static final String NULL_DISPLAY = "unspecified (null)";
	MethodProxy method;
	
	MethodInvocationManager invocationManager;
	void setValueWithoutPropertyNotification (Object newVal) {
		if (newVal == null)
			return;
		parameterValue = newVal;
		invocationManager.setParameterValue(parameterNumber, parameterValue);
	}
	void displayParameter(Object obj) {
		
		 uiFrame editor = ObjectEditor.edit(obj);
			//tf.setTitle(result.toString());
			editor.setTitle(parameterName + ":" +  obj.toString());
			
	}
	public AnInteractiveActualParameter (
			AnInteractiveMethodInvoker theMethodInvoker,
			MethodInvocationManager theInvocationManager,
			MethodProxy theMethod,
			int theIndex,
			String theParameterName,
			ClassProxy theParameterType) {
		methodInvoker = theMethodInvoker;
		method = theMethod;
		invocationManager = theInvocationManager;
		showType = (Boolean) AttributeManager.getInheritedAttribute(method, AttributeNames.SHOW_PARAMETER_TYPE, null).getValue();
		invocationManager.addObjectParameterListener(this);
		propertyChangeSupport = new PropertyChangeSupport(this);
		parameterNumber = theIndex;
		parameterName = theParameterName;
		parameterType = theParameterType;
		parameterValue = theInvocationManager.getDefaultParameterValue(theIndex, parameterType);
		setValueWithoutPropertyNotification(parameterValue);
		if (parameterType.isPrimitive()) {
			parameterTypeChoices = parameterType.toString();
			//setValueWithoutPropertyNotification(bus.uigen.misc.Misc.defaultValue(parameterType));
			//parameterDisplay = parameterValue;
			
			//invocationManager.parameterValues[parameterNumber] = parameterValue;
		} else {	
			
			isTypePrimitive = false;
			parameterTypeChoices = new util.models.ADynamicEnum(uiClassMapper.getClassMapping(parameterType));
			//setValueWithoutPropertyNotification(null);	
			//parameterDisplay = NULL_DISPLAY;
			isValueParsable = bus.uigen.misc.OEMisc.isParsable(theParameterType);
			//parameterValue = bus.uigen.misc.Misc.defaultValue(parameterType);
			//setValueWithoutPropertyNotification(parameterValue);
			
			if (parameterValue != null && !isValueParsable) {
				((DynamicEnum) parameterTypeChoices).setValue(parameterValue.getClass().getName());
				displayParameter(parameterValue);
				//setType(parameterTypeChoices);
			} else {
				
			/*	
			if (isValueParsable) {
				parameterValue = bus.uigen.misc.Misc.defaultValue(parameterType);
				parameterDisplay = parameterValue;
			} else {
				setValueWithoutPropertyNotification(null);
				//parameterDisplay = parameterValue;
				//parameterDisplay = NULL_DISPLAY;
			}
			*/
			if (parameterType.isInterface() ||
					Modifier.isAbstract(parameterType.getModifiers()) ||
					parameterType == parameterType.objectClass() 
					) {
				
				if (parameterType.isAssignableFrom(parameterType.stringClass())) {
					((DynamicEnum) parameterTypeChoices).setValue("String");
					 setType(parameterTypeChoices);
				}
				/*
			if (theParameterType == theParameterType.objectClass())
				setType(theParameterType.stringClass());
			*/
			}
			}
			originalParameterType = (String)((DynamicEnum) parameterTypeChoices).getValue();
		}
		originalValue = parameterValue;
	}
	
	public String getName() {
		return parameterName;		
	}
	public boolean preGetType() {
		return showType;
	}	
	public Object getType() {
		return parameterTypeChoices;
	}
	public boolean preSetType() {
		return !isTypePrimitive;
	}
	public void setType(Object newVal) {
		ClassProxy newType = null;
		if (parameterTypeChoices instanceof String) {
			parameterTypeChoices = newVal;
			return;
		} else if (parameterTypeChoices instanceof DynamicEnum) {
			DynamicEnum parameterEnum = (DynamicEnum) parameterTypeChoices;
			
			//String newTypeString = (String) ((DynamicEnum) parameterTypeChoices).getValue();
			String newTypeString = (String) parameterEnum.getValue();
			try {
			
			 newType = uiClassFinder.forName(parameterType, newTypeString);
			if (newType == null) {
				Tracer.error(newTypeString + " is not a recognized type. Please enter full name of the type");
				return;
			}else if (newType.isInterface() ||
					Modifier.isAbstract(newType.getModifiers()) ||
					newType == newType.objectClass() 
					) {
				/*
				if (newType.isAssignableFrom(newType.stringClass())) {
					parameterEnum.setValue("String");
					newType = newType.stringClass();
					} else {
				*/	
				Tracer.userMessage("Cannot instantiate " + newType);
				isValueParsable = false;
				setValue(null);
				//setParameterValue(null);
				//parameterDisplay = NULL_DISPLAY;
				return;
				/*
					}
					*/
			} else	if (!parameterType.isAssignableFrom(newType)) {					
				Tracer.userMessage(newType.getName() + " is not a subtype of " + parameterType);
				setValue(null);
				//setParameterValue(null);
				//parameterDisplay = NULL_DISPLAY;
				return;				
			}
			isValueParsable = bus.uigen.misc.OEMisc.isParsable(newType);
			//if (newType.isPrimitive()) {
			if (isValueParsable) {
			//if (newType.isPrimitive()) {
				//parameterTypeChoices = parameterType.toString();
				//setParameterValue(bus.uigen.misc.Misc.defaultValue(newType));
				setValue(bus.uigen.misc.OEMisc.defaultValue(newType));
				//parameterDisplay = parameterValue;
			}
			if (isValueParsable)
				return;
			/*
			else 
				if (newType.isInterface() ||
					Modifier.isAbstract(newType.getModifiers()) ||
					newType == newType.objectClass() 
					) {
				Message.error("Cannot instantiate " + newType);
				parameterValue = null;
				parameterDisplay = "";
				return;
			}
			
			if (!parameterType.isAssignableFrom(newType)) {					
				Message.error(newType.getName() + " is not a subtype of " + parameterType);
				parameterValue = null;
				parameterDisplay = "";
				return;				
			}
			*/
			} catch (Exception e) {
				Tracer.userMessage(newTypeString + " is not a recognized type. Please enter full name of the type.");
				parameterEnum.getChoices().remove(newTypeString);
				return;
				
			}
			
			invocationManager.instantiateClass(
					parameterNumber, 
					//parameterType, 
					newType,
					//(String) ((DynamicEnum) parameterTypeChoices).getValue(),
					true);
			//parameterValue = invocationManager.getParameterValue(parameterNumber);
			//parameterDisplay = parameterValue.toString();
			//((DynamicEnum) parameterTypeChoices).setValue(newVal);
		}
	}
	
	public Object getValue() {
		if (isValueParsable)
		return parameterValue;
		else
			return parameterValue.toString();
		//return parameterDisplay;
	}
	
	public boolean preSetValueObject() {
		return isValueParsable /*&& parameterValue != null*/;
	}
	
	public void setValue(Object newVal) {
		if (parameterValue == newVal)
			return;
		Object oldVal = parameterValue;
		 parameterValue = newVal;
		 invocationManager.setParameterValue(parameterNumber, newVal);
		 propertyChangeSupport.firePropertyChange("value", oldVal, newVal);
		 if (methodInvoker.implicitInvoke)
			 methodInvoker.invokeDynamicCommand(methodInvoker.displayName);
		 
	}
	public void setInvocationManager (MethodInvocationManager theInvocationManager) {
		invocationManager = theInvocationManager;
		invocationManager.setParameterValue(parameterNumber, parameterValue);
	}

	@Override
	public void newUserValue(int theParameterNumber, Object theValue) {
		if (parameterNumber != theParameterNumber)
			return;
		// TODO Auto-generated method stub
		parameterValue = theValue;
		//parameterDisplay = parameterValue.toString();
		//parameterDisplay = parameterValue;
		propertyChangeSupport.firePropertyChange("value", "", getValue());
		
	}
	PropertyChangeSupport propertyChangeSupport;
	@util.annotations.ObserverRegisterer(util.annotations.ObserverTypes.PROPERTY_LISTENER)
	public void addPropertyChangeListener(PropertyChangeListener l) {
		
		propertyChangeSupport.addPropertyChangeListener(l);
	}
	public void reset() {
		setValue(originalValue);
		if (originalParameterType != null) {
			((DynamicEnum) parameterTypeChoices).setValue(originalParameterType);
			setType(parameterTypeChoices);
		}
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		setType(evt.getSource());
		// TODO Auto-generated method stub
		
	}
			
	

}
