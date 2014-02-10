package bus.uigen.controller.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.annotation.Annotation;


import util.annotations.Column;
import util.annotations.Explanation;
import util.annotations.Position;
import util.annotations.Row;
import util.annotations.Visible;
import util.models.DynamicCommands;
import bus.uigen.attributes.AnInheritedAttributeValue;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.attributes.AnInheritedAttributeValue.InheritanceKind;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
//@util.Pattern(util.PatternNames.NO_PATTERN)
@util.annotations.StructurePattern (util.annotations.StructurePatternNames.BEAN_PATTERN)
//@util.Pattern("Bean Pattern")
public class AnInteractiveMethodInvoker implements InteractiveMethodInvoker {
	MethodProxy method;
	Object parentObject; // not really used
	boolean isVoid;
//	boolean showResult;
	boolean implicitInvoke;
	//ClassProxy[] parameterTypes;
	//String[] parameterNames;	
	//Vector<AnInteractiveActualParameter> actualParameters = new Vector();
	AnInteractiveActualParameter[] actualParameters;
	MethodInvocationManager invocationManager;
	String displayName;
	String[] dynamicCommands = {"invoke"};
	Object result;
	boolean alwaysShowResult = false;
	String signature;
	boolean inPlaceResult;
	boolean autoCloseSpecified;
	boolean autoClose = true;
	boolean autoReset = false;
	boolean autoResetSpecified;
	public AnInteractiveMethodInvoker (
			MethodInvocationManager theInvocationManager,
			String theDisplayName,
			MethodProxy theMethod,
			String theSignature,
			Object parentObject,
			String[] theParameterNames,
			boolean theAlwaysShowResult
		
			) {
		//dynamicCommands = {"invokeDynamic"};
		boolean theImplicitInvoke = (Boolean) AttributeManager.getInheritedAttribute(theMethod, AttributeNames.IMPLICIT_METHOD_INVOCATION, null).getValue();
		init( theInvocationManager,
				 theDisplayName,
				 theMethod,
				 theSignature,
				 parentObject,
				 theParameterNames,
				 theAlwaysShowResult,
				 theImplicitInvoke);
		/*
		method = theMethod;
		isVoid = method.getReturnType() == method.getReturnType().voidType();
		//showResult = (Boolean) AttributeNames.getDefaultOrSystemDefault(AttributeNames.INPLACE_METHOD_RESULT);
		showResult = (Boolean) ClassDescriptor.getInheritedAttribute(method, AttributeNames.INPLACE_METHOD_RESULT).getValue();
		implicitInvoke = (Boolean) ClassDescriptor.getInheritedAttribute(method, AttributeNames.IMPLICIT_METHOD_INVOCATION).getValue();
		ClassProxy[] parameterTypes = theMethod.getParameterTypes();
		actualParameters = new AnInteractiveActualParameter[parameterTypes.length];
		alwaysShowResult = theAlwaysShowResult;
		for (int i = 0; i < parameterTypes.length; i++) {
			AnInteractiveActualParameter parameter = new AnInteractiveActualParameter(
						this, theInvocationManager, method, i, getParameterName(theParameterNames, i), parameterTypes[i]);
			//actualParameters.add(parameter);
			actualParameters[i] = parameter;
					
		}
		invocationManager = theInvocationManager;
		displayName = theDisplayName;
		if (implicitInvoke || displayName == null)
			dynamicCommands = new String[0];
	
		//if (displayName != null) {
		else
			dynamicCommands[0] = displayName;
		//}
		propertyChangeSupport = new PropertyChangeSupport(this);
		*/
	}
	public void doImplicitInvoke(boolean theNewValue) {
		implicitInvoke = theNewValue;
	}
	public AnInteractiveMethodInvoker (
			MethodInvocationManager theInvocationManager,
			String theDisplayName,
			MethodProxy theMethod,
			String theSignature,
			Object parentObject,
			String[] theParameterNames,
			boolean theAlwaysShowResult,
			boolean theImplicitInvoke
		
			) {
		//dynamicCommands = {"invokeDynamic"};
		//implicitInvoke = (Boolean) ClassDescriptor.getInheritedAttribute(theMethod, AttributeNames.IMPLICIT_METHOD_INVOCATION).getValue();
		init( theInvocationManager,
				 theDisplayName,
				 theMethod,
				 theSignature,
				 parentObject,
				 theParameterNames,
				 theAlwaysShowResult,
				 theImplicitInvoke);
		
	}
	 void init (
			MethodInvocationManager theInvocationManager,
			String theDisplayName,
			MethodProxy theMethod,
			String theSignature,
			Object parentObject,
			String[] theParameterNames,
			boolean theAlwaysShowResult,
			boolean theImplicitInvoke
		
			) {
		//dynamicCommands = {"invokeDynamic"};
		
		method = theMethod;
		isVoid = method.getReturnType() == method.getReturnType().voidType();
		AnInheritedAttributeValue persist = AttributeManager.getInheritedAttribute(method, AttributeNames.PERSIST_INVOCATION_WINDOW, null);
		if (persist.getInheritanceKind() != InheritanceKind.SYSTEM_DEFAULT) {
			autoCloseSpecified = true;
		}
		autoClose = ! (Boolean) (persist.getValue());

		
		AnInheritedAttributeValue reset = AttributeManager.getInheritedAttribute(method, AttributeNames.RESET_METHOD_INOVOKER, null);
		if (reset.getInheritanceKind() != InheritanceKind.DEFAULT) {
			autoResetSpecified = true;
		}
		autoReset = (Boolean) (reset.getValue());

		
		
		
		//showResult = (Boolean) AttributeNames.getDefaultOrSystemDefault(AttributeNames.INPLACE_METHOD_RESULT);
		inPlaceResult = (Boolean) AttributeManager.getInheritedAttribute(method, AttributeNames.INPLACE_METHOD_RESULT, null).getValue();
		//implicitInvoke = (Boolean) ClassDescriptor.getInheritedAttribute(method, AttributeNames.IMPLICIT_METHOD_INVOCATION).getValue();
		implicitInvoke = theImplicitInvoke;
		ClassProxy[] parameterTypes = theMethod.getParameterTypes();
		actualParameters = new AnInteractiveActualParameter[parameterTypes.length];
		alwaysShowResult = theAlwaysShowResult;
		signature = theSignature;
		for (int i = 0; i < parameterTypes.length; i++) {
			AnInteractiveActualParameter parameter = new AnInteractiveActualParameter(
						this, theInvocationManager, method, i, getParameterName(theParameterNames, i), parameterTypes[i]);
			//actualParameters.add(parameter);
			actualParameters[i] = parameter;
					
		}
		invocationManager = theInvocationManager;
		displayName = theDisplayName;
		if (implicitInvoke || displayName == null)
			dynamicCommands = new String[0];
	
		//if (displayName != null) {
		else
			dynamicCommands[0] = displayName;
		//}
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	String getParameterName(String[] parameterNames, int parameterIndex) {
	  	//String defaultName = "Parameter "+(parameterIndex+1)+":";
	  	String defaultName = "Parameter "+(parameterIndex+1);
	  	if ((parameterNames == null) ||(parameterIndex >= parameterNames.length))  return defaultName;
	  	//return parameterNames[parameterIndex]+ ":";
	  	return parameterNames[parameterIndex];
	  	
	  }
	/*
	public Vector<AnInteractiveActualParameter> getParameters() {
		return actualParameters;
	}
	*/
	/*
	public void setObject(Object theObject) {
		parentObject = theObject;
	}
	*/
	@Row(2)
	public InteractiveActualParameter[] getParameters() {
		return actualParameters;
	}
	public boolean preAutoClose() {
		return !autoCloseSpecified;
	}
	@Explanation("Determines if this window is closed after the operation is executed.")
	@Row(0) @Column(0)
	public boolean getAutoClose() {
		return autoClose;
	}
	public void setAutoClose(boolean newVal) {
		 autoClose = newVal;
		 propertyChangeSupport.firePropertyChange("AutoClose", null, newVal);

	}
	@Explanation("Determines if the parameters are reset after the operation is executed.")
	@Row(0) @Column(1)
	public boolean getAutoReset() {
		return autoReset;
	}
	public void setAutoReset(boolean newVal) {
		 autoReset = newVal;
		 propertyChangeSupport.firePropertyChange("AutoReset", null, newVal);

	}
//	@Row(0) @Column(2)
//	public boolean getInPlaceResult() {
//		return inPlaceResult;
//	}
	
//	public void setInPlaceResult(boolean newVal) {
//		inPlaceResult = newVal;
//		 propertyChangeSupport.firePropertyChange("InPlaceResult", null, inPlaceResult);
//
//	}
	
			
	
	@Override
	public String[] getDynamicCommands() {
		// TODO Auto-generated method stub
		return dynamicCommands;
	}
	
	
	
	boolean methodInvoked = false;
	@Override 
	public void invokeDynamicCommand(String theCommand) {
		// TODO Auto-generated method stub
		methodInvoked = true;
		result = invocationManager.invokeMethod();
//		if (autoClose)
		invocationManager.dispose();
//		else
//			resetAll();
		//if (preGetResult()) {
		 propertyChangeSupport.firePropertyChange("result", null, result);
		//}
		//System.out.println("invoke dynamic called");
		
	}
	@Override
	public Annotation getDynamicCommandAnnotation(String theCommand,
			Class annotationType) {
		if (annotationType == Row.class || annotationType == Column.class || annotationType == Position.class) {
			return null;
		}
		return method.getAnnotation(annotationType);
		
	}
	public boolean preGetResult() {
		//return /*showResult &&*/ !isVoid;
//		return  !isVoid && (alwaysShowResult || (inPlaceResult && methodInvoked) );
		return  !isVoid && (alwaysShowResult || (inPlaceResult) );

		//return result != null;
	}
	public Object getResult() {
		return result;
	}
	public void resetAll() {
		for (int i = 0; i < actualParameters.length; i++) {
			actualParameters[i].reset();
		}
//		methodInvoked = false;
		
	}
	PropertyChangeSupport propertyChangeSupport;
	@util.annotations.ObserverRegisterer(util.annotations.ObserverTypes.PROPERTY_LISTENER)
	public void addPropertyChangeListener(PropertyChangeListener l) {
		
		propertyChangeSupport.addPropertyChangeListener(l);
	}
	@Override
	public Object getDynamicPropertyType(String theProperty) {
		// TODO Auto-generated method stub
		if (theProperty.toLowerCase().equals("result"))
			return method.getReturnType();
		
		else return null;
	}
	@Visible(false)
	public String getVirtualClass() {
		return method.getDeclaringClass().getName() + signature;
	}
	@Visible(false)
	public void setInvocationManager (MethodInvocationManager theInvocationManager) {
		invocationManager = theInvocationManager;
		for (int i = 0; i < actualParameters.length; i++)
			actualParameters[i].setInvocationManager(theInvocationManager);
	}
	@Visible(false)
	public String getMethodDisplayName() {
		return displayName;
	}
	
	@Visible(false)
	public MethodProxy getMethod() {
		return method;
	}

}
