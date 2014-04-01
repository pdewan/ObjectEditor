package bus.uigen.undo;
import java.beans.MethodDescriptor;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import util.trace.Tracer;

import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
public class CommandCreator {
	
	
	public static Command createCommand (CommandListener listener, MethodProxy method,
			 Object object,
			 Object[] params, MethodProxy candidate, MethodProxy elementAtMethod) {
		Command command;
	command = makeVoidSubtractAll(listener, method, object, params);
	if (command != null) return command;
	command = makeSymmetric(listener, method, object, params, candidate);
	if (command != null) return command;				
	command = makeVoidSubractAddFirst(listener, method, object, params, candidate, elementAtMethod);
	if (command != null) return command;
	command = makeVoidSubtractAddLast(listener, method, object, params, candidate, elementAtMethod);
	if (command != null) return command;
	command = makeSubtractAddLast(listener, method, object, params, candidate, elementAtMethod);
	if (command != null) return command;				
	command = makeSubractAddFirst(listener, method, object, params, candidate, elementAtMethod);
	if (command != null) return command;
	command = makeAddSubractLast(listener, method, object, params, candidate);
	if (command != null) return command;
	command = makeAddSubtractFirst(listener, method, object, params, candidate);
	if (command != null) return command;
	return null;
	}
	static Map<MethodProxy, Command> methodToCommand = new HashMap();
	
	static boolean cacheCommands = true;
	public static Command createCommand (CommandListener listener, MethodProxy method,
			 Object object,
			 Object[] params) {	
		//Command command = methodToCommand.get(method);
		Command command = methodToCommand.get(method);
		if (command != null) {
			 return command.clone(object, params, null, listener);
		}
		command = createCommandBasic(listener, method, object, params);
		if (cacheCommands)
			methodToCommand.put(method, command);
		return command;
		
		
	}

	public static Command createCommandBasic (CommandListener listener, MethodProxy method,
										 Object object,
										 Object[] params) {
		//System.out.println("Creating command " + method);
		Command command;
		/*
		Command command = makeSetGet(listener, method, object, params);
		if (command != null) return command;
		*/
		String methodName = method.getName();
		
		//MethodProxy[] methods = uiBean.getMethods(ClassSelector.getClass(object));
		//ViewInfo cd = ClassDescriptorCache.getClassDescriptor(ClassSelector.getClass(object));
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(object);
		//MethodProxy[] methods = cd.getVirtualMethods();
		
		Object isUndoable = null;
		
		MethodDescriptorProxy md = cd.getMethodDescriptor(method);
		if (md != null) {
			//isUndoable = md.getValue(AttributeNames.IS_UNDOABLE);			
			isUndoable = AttributeManager.getInheritedAttribute (null, md, AttributeNames.IS_UNDOABLE, null).getValue();
			if (isUndoable == null) {
				util.annotations.NotInvertible notInvertible = (util.annotations.NotInvertible) method.getAnnotation(util.annotations.NotInvertible.class);
				if (notInvertible != null)
					isUndoable = false;
			}
		//Object isUndoable = md.getValue(AttributeNames.IS_UNDOABLE);
		}
		
		Boolean notUndoableCommandEmptiesHistory = (Boolean) AttributeManager.getInheritedAttribute (null, md, AttributeNames.NOT_UNDOABLE_EMPTIES_UNDO_HISTORY, null).getValue();
		
		if (method.isDynamicCommand() || (isUndoable != null && ! (Boolean) isUndoable)) {
			command = new BasicCommand(listener, method, object, params);
			//if (notUndoableCommandEmptiesHistory != null)
				command.setNotUndoablePurgesUndoHistory(notUndoableCommandEmptiesHistory);
			//return new BasicCommand(listener, method, object, params);
			return command;
		}
		
		int numIndices = method.getParameterTypes().length;
		//VirtualMethod elementAtMethod = uiBean.getElementAtMethod(object.getClass(), numIndices);
		MethodProxy elementAtMethod = (MethodProxy) cd.getAttribute(AttributeNames.READ_ELEMENT_METHOD);
		if (elementAtMethod == null)
			elementAtMethod = IntrospectUtility.getElementAtMethod(RemoteSelector.getClass(object), numIndices);		
		//VirtualMethod indexOfMethod = uiBean.getIndexOfMethod(object.getClass());
		//VirtualMethod insertElementAtMethod = uiBean.getInsertElementAtMethod(object.getClass());
		// dont understand this command anymore
		/*
		command = makeIndexlessVoidSubract(listener, method, object, params, insertElementAtMethod, elementAtMethod, indexOfMethod);
		if (command != null) return command;
		*/
		//VirtualMethod getMethod = uiBean.getGetMethod(object.getClass());
		//VirtualMethod putMethod = uiBean.getPutMethod(object.getClass());
		// the indexless method does not make sense!
		/*
		command = makeIndexlessVoidSubract(listener, method, object, params, putMethod, getMethod, null);
		if (command != null) return command;
		*/
		MethodDescriptorProxy[] mds = cd.getMethodDescriptors();
		MethodProxy inverseMethod = null;
		//MethodProxy inverseMethod = (MethodProxy) md.getValue(AttributeNames.INVERSE);
		if (md != null)
			inverseMethod = (MethodProxy) md.getValue(AttributeNames.INVERSE);
		if (inverseMethod != null)
			command = createCommand(listener, method, object, params, inverseMethod, elementAtMethod);
		else {
		//for (int i = 0; i < methods.length; i++) {
			util.annotations.Inverse inverse = method.getAnnotation(util.annotations.Inverse.class);
			String inverseName = null;
			if (inverse != null) {
				inverseName = inverse.value();
			}
		for (int i = 0; i < mds.length; i++) {
			//MethodProxy candidate = methods[i];
			MethodProxy candidate = mds[i].getMethod();
			if (candidate == null)
				continue;
			String candidateName = candidate.getName();
			
			//System.out.println("trying candidate name " + candidateName);
			if ((inverseName != null && inverseName.equalsIgnoreCase(candidateName))  || (inverseName == null && Inverses.isAntonym(methodName, candidateName))) {				
				command = createCommand(listener, method, object, params, candidate, elementAtMethod);
				//Message.info(candidate + "considered inverse of " + method + ". Use annotation or attrbbute" + AttributeNames.INVERSE + " to disambiguate");
				/*
				command = makeSymmetric(listener, method, object, params, candidate);
				if (command != null) return command;				
				command = makeVoidSubractAddFirst(listener, method, object, params, candidate, elementAtMethod);
				if (command != null) return command;
				command = makeVoidSubtractAddLast(listener, method, object, params, candidate, elementAtMethod);
				if (command != null) return command;
				command = makeSubtractAddLast(listener, method, object, params, candidate, elementAtMethod);
				if (command != null) return command;				
				command = makeSubractAddFirst(listener, method, object, params, candidate, elementAtMethod);
				if (command != null) return command;
				command = makeAddSubractLast(listener, method, object, params, candidate);
				if (command != null) return command;
				command = makeAddSubtractFirst(listener, method, object, params, candidate);
				*/
				if (command != null) return command;
				
			}
			/*
			else {
				command = makeSetGet(listener, method, object, params);
				if (command != null) return command;
			}
			*/
		}
		command = makeSetGet(listener, method, object, params);
		}
		//command.setNotUndoablePurgesUndoHistory(notUndoableCommandEmptiesHistory);
		//if (command != null) { return command;
		if (command == null) {
		command = new BasicCommand(listener, method, object, params);
		Tracer.info(CommandCreator.class, "No inverse found for " + method + ". Use annotation or atrribute to specify one");
		}
		command.setNotUndoablePurgesUndoHistory(notUndoableCommandEmptiesHistory);
		return command;
		//return new BasicCommand(listener, method, object, params);
		
	}
	public static Command makeVoidSubtractAll(CommandListener listener, MethodProxy method,
			 Object object,
			 Object[] params) {
		//ViewInfo cd = ClassDescriptorCache.getClassDescriptor(ClassSelector.getClass(object));
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(object);
		MethodDescriptorProxy md = cd.getMethodDescriptor(method);
		Boolean isRemoveAllMethod = (Boolean) md.getValue(AttributeNames.IS_REMOVE_ALL_METHOD);
		if (isRemoveAllMethod == null)
			return null;
		if (!isRemoveAllMethod)
			return null;
		MethodProxy addAllMethod = (MethodProxy) md.getValue(AttributeNames.INVERSE);
		if (addAllMethod == null)
			return null;
		try {
			MethodProxy cloneMethod = IntrospectUtility.getCloneMethod(RemoteSelector.getClass(object));
			if (cloneMethod == null)
				return null;
			Object[] emptyParams = {};
			Object retVal = cloneMethod.invoke(object, emptyParams);
			if (retVal == null)
				return null;
			Object[] inverseParams = {retVal};
			return new VoidSubtractAllCommand(listener, method, object,  inverseParams, addAllMethod);
		} catch (Exception e) {
			return null;
		}
		
		
	}
	public static Command makeSetGet(CommandListener listener, MethodProxy method,
										 Object object,
										 Object[] params) {
		try {
		//if (uiBean.isSetter(method)) {
		if (IntrospectUtility.isGeneralizedSetter(method)) {
			MethodProxy getter = IntrospectUtility.getGeneralizedGetMethod(ACompositeLoggable.getTargetClass(object), method);
			if (getter == null)
				return null;
			if (!Util.isPrefix(getter.getParameterTypes(), method.getParameterTypes()))
				return null;		
			/*
			Method getMethod = uiBean.getGetMethod(object.getClass(), method);
			Object[] emptyParams = {};			
			Object oldVal = getMethod.invoke(object, emptyParams);
			if (oldVal.equals(params[0])) return null;
			*/
			return new SetGetLastCommand(//getUIFrame(),										  listener,
										  method,										  object,										  params,
										  getter
										  //getMethod										  //uiBean.getGetMethod(object.getClass(), method)
										  );
		} else return null;
		} catch (Exception e) { return null;}
	}
	public static Command makeSymmetric(CommandListener listener, MethodProxy method, Object object, Object[] params, MethodProxy inverse) {
		/*
		if (method.getReturnType() != Void.TYPE || inverse.getReturnType() != Void.TYPE)
			return null;
		*/
		//System.out.println("Trying symmetic command: " + method);
		if (!Util.equal(method.getParameterTypes(), inverse.getParameterTypes()))
			return null;
		Tracer.info(CommandCreator.class, inverse + "considered inverse of " +  method + ". Use annotation or attribute " + AttributeNames.INVERSE + " to override.");
		return new SymmetricCommand(listener, method, object, params, inverse);
	}
	public static Command makeAddSubractLast(CommandListener listener, MethodProxy adder, Object object, Object[] params, MethodProxy subtracter) {
		if (isLastAddSubtractPair(adder, subtracter)) {
			Tracer.info(CommandCreator.class, subtracter + "  considered undoing indexed subtacter last for " + adder + ". Use attribute or annotation to disambiguate" );

			return new AddSubtractLastCommand(listener, adder, object, params, subtracter);
		}
		else return null;
	}
	public static Command makeAddSubtractFirst(CommandListener listener, MethodProxy adder, Object object, Object[] params, MethodProxy subtracter) {
		if (isFirstAddSubtractPair(adder, subtracter)) {
			Tracer.info(CommandCreator.class, subtracter + "  considered undoing indexed subtacter first for " + adder + ". Use attribute or annotation to disambiguate" );

			return new AddSubtractFirstCommand(listener, adder, object, params, subtracter);
		} else return null;
	}
	
	public static Command makeSubractAddFirst(CommandListener listener, MethodProxy subtracter, Object object, Object[] params, MethodProxy adder, MethodProxy elementAt) {
		if (isFirstSubtractAddPair(adder, subtracter))
			return new SubtractAddFirstCommand(listener, subtracter, object, params, adder);
		return null;
	}
	public static Command makeSubtractAddLast(CommandListener listener, MethodProxy subtracter, Object object, Object[] params, MethodProxy adder, MethodProxy elementAt) {
		if (isLastSubtractAddPair(adder, subtracter)) {
			Tracer.info(CommandCreator.class, adder + "  considered undoing indexed add last for " + subtracter + ". Use attribute or annotation to disambiguate" );

			return new SubtractAddLastCommand(listener, subtracter, object, params, adder);
		}
		return null;
	}
	public static Command makeVoidSubractAddFirst(CommandListener listener, MethodProxy subtracter, Object object, Object[] params, MethodProxy adder, MethodProxy elementAt) {
		if (isVoidFirstSubtractAddPair(adder, subtracter, elementAt)) {
			Tracer.info(adder + "  considered undoing indexed add first for " + subtracter + ". Use attribute or annotation to disambiguate" );
			return new VoidSubtractAddFirstCommand(listener, subtracter, object, params, adder, elementAt);
			
		}
		return null;
	}
	public static Command makeIndexlessVoidSubract(CommandListener listener, MethodProxy subtracter, 
			Object object, Object[] params, MethodProxy adder, 
			MethodProxy elementAt, MethodProxy indexOf) {
		if ((indexOf == null && elementAt == null) || adder == null || subtracter == null)
			return null;
		/*
		if (!uiBean.isRemoveElementMethod(subtracter))
			return null;
		*/
		Tracer.info(adder + "  considered undoing indexless add  " + subtracter + ". Use attribute or annotation to disambiguate" );
		return new IndexlessVoidSubtractCommand(listener, subtracter, object, params, adder, elementAt, indexOf);
		//return null;
	}
	public static Command makeVoidSubtractAddLast(CommandListener listener, MethodProxy subtracter, Object object, Object[] params, MethodProxy adder, MethodProxy elementAt) {
		if (isVoidLastSubtractAddPair(adder, subtracter, elementAt)) {
			Tracer.info(CommandCreator.class, adder + "  considered undoing indexed add las for " + subtracter + ". Use attribute or annotation to disambiguate" );

			return new VoidSubtractAddLastCommand(listener, subtracter, object, params, adder, elementAt);
		}
		return null;
	}
	
	public static boolean isMatchingGetterSetterPair (MethodProxy getter, MethodProxy setter) {
		if (!getter.getName().startsWith("get"))
			return false;
		if (!setter.getName().startsWith("set"))
			return false;
		String getterProperty = getter.getName().substring(3, getter.getName().length());
		String setterProperty = setter.getName().substring(3, setter.getName().length());
		if (!getterProperty.equals(setterProperty))
			return false;
		if (!Util.isPrefix(getter.getParameterTypes(), setter.getParameterTypes()))
			return false;
		return false;
	}
	
	public static boolean isLastAddSubtractPair(MethodProxy adder, MethodProxy subtracter) {
		ClassProxy[] adderParamTypes = adder.getParameterTypes();		
		if (!Util.isPrefix(subtracter.getParameterTypes(), adderParamTypes))
			return false;
		/*
		if (//adder.getReturnType() != Void.TYPE || 
			subtracter.getReturnType() != adderParamTypes[adderParamTypes.length-1])
			return false;
		*/
		Tracer.info(CommandCreator.class, subtracter + "  considered undoing indexed subtacter for add last " + adder + ". Use attribute or annotation to disambiguate" );

		return true;
	}
	public static boolean isFirstAddSubtractPair(MethodProxy adder, MethodProxy subtracter) {
		ClassProxy[] adderParamTypes = adder.getParameterTypes();
		
		if (!Util.isSuffix(subtracter.getParameterTypes(), adderParamTypes))
			return false;
		/*
		if (adder.getReturnType() != Void.TYPE || 
			subtracter.getReturnType() != adderParamTypes[0])
			return false;
		*/
		Tracer.info(CommandCreator.class, subtracter + "  considered undoing indexed subtacter for add first" + adder + ". Use attribute or annotation to disambiguate" );

		return true;
	}
	/*
	public static boolean isLastSubtractAddPair(VirtualMethod adder, VirtualMethod subtracter) {
		Class[] adderParamTypes = adder.getParameterTypes();		
		if (!Util.isPrefix(subtracter.getParameterTypes(), adderParamTypes))
			return false;
		if (subtracter.getReturnType() != Void.TYPE || 
			subtracter.getReturnType() != adderParamTypes[adderParamTypes.length-1])
			return false;
		return true;
	}
	*/
	public static boolean isLastSubtractAddPair(MethodProxy adder, MethodProxy subtracter) {
		ClassProxy[] adderParamTypes = adder.getParameterTypes();		
		if (!Util.isPrefix(subtracter.getParameterTypes(), adderParamTypes))
			return false;
		if (subtracter.getReturnType() != adderParamTypes[adderParamTypes.length-1])
			return false;
		return true;
	}
	public static boolean isFirstSubtractAddPair(MethodProxy adder, MethodProxy subtracter) {
		ClassProxy[] adderParamTypes = adder.getParameterTypes();
		
		if (!Util.isSuffix(subtracter.getParameterTypes(), adderParamTypes))
			return false;
		if (
				//subtracter.getReturnType() != Void.TYPE || 
			subtracter.getReturnType() != adderParamTypes[0])
			return false;
		return true;
	}
	public static boolean isVoidLastSubtractAddPair(MethodProxy adder, MethodProxy subtracter, MethodProxy elementAt) {
		ClassProxy[] adderParamTypes = adder.getParameterTypes();		
		
		// covers the queue/stack cases where pop is void
		if (subtracter.getParameterTypes().length == 0)
			return false;
		if (!Util.isPrefix(subtracter.getParameterTypes(), adderParamTypes))
			return false;
		if (elementAt == null || subtracter.getReturnType() != adder.getDeclaringClass().voidType())
			return false;
		return true;
	}
	public static boolean isVoidFirstSubtractAddPair(MethodProxy adder, MethodProxy subtracter, MethodProxy elementAt) {
		ClassProxy[] adderParamTypes = adder.getParameterTypes();
		
		// covers the void pop with no argument
		if (subtracter.getParameterTypes().length == 0)
			return false;
		
		if (!Util.isSuffix(subtracter.getParameterTypes(), adderParamTypes))
			return false;
		if (
				elementAt == null || subtracter.getReturnType() != adder.getDeclaringClass().voidType() 
			)
			return false;
		return true;
	}
										 
}
