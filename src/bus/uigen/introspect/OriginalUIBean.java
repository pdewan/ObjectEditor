package bus.uigen.introspect;

import java.beans.MethodDescriptor;
import java.lang.reflect.*;
import shapes.*;import util.models.HashtableListener;
import util.models.RemotePropertyChangeListener;
import util.models.VectorListener;
import util.trace.Tracer;

import java.util.Enumeration;import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;import java.util.Hashtable;
import javax.swing.JTable;
import javax.swing.JTree;

import bus.uigen.PrimitiveClassList;
import bus.uigen.uiGenerator;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.DynamicMethods;
import bus.uigen.reflect.FieldProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.reflect.remote.RemoteClassProxy;
//import bus.uigen.sadapters.ConcreteEnumeration;
//import bus.uigen.sadapters.EnumToEnumerationFactory;
//import bus.uigen.sadapters.GenericEnumerationToEnumerationFactory;


public class OriginalUIBean {
	public static String INIT_SERIALIZED_OBJECT = 	"initSerializedObject";	
	public static String VIEW_GETTER = "view";
	public static String VIEW_REFRESHER = "refreshView";
	public static Vector dynamicMethodsVector = uiGenerator.arrayToVector(DynamicMethods.dynamicMethods);
	
	
	
	
	static Object[] emptyArgs = {};
	static Class[] emptyArgTypes = {};
	public static MethodProxy[] getMethods(ClassProxy c) {
		return AClassDescriptor.getMethods(c);
	}
	public static MethodProxy getMethod(MethodDescriptorProxy md) {
		return AVirtualMethod.virtualMethod(md.getMethod());
	}
	/*
	public static MethodDescriptor toMethodDescriptor(Method method) {
		if (method == null)
			return null;
		Class c = method.getDeclaringClass();
		ViewInfo cd = ClassDescriptorCache.getClassDescriptor(c);
		//ClassDescriptor cd = (ClassDescriptor) ClassDescriptorCache.getClassDescriptor(c);
		MethodDescriptor[] mds = cd.getMethodDescriptors();
		//VirtualMethodDescriptor[] mds = cd.getVirtualMethodDescriptors();
		for (int i = 0; i < mds.length; i++) {
			MethodDescriptor md = mds[i];
			if (md.getMethod() == method)
				return md;
		}
		return null;
	}
	*/
	public static MethodDescriptorProxy toMethodDescriptor(MethodProxy method) {
		if (method == null)
			return null;
		ClassProxy c = method.getDeclaringClass();
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		return cd.getMethodDescriptor(method);
		/*
		//ClassDescriptor cd = (ClassDescriptor) ClassDescriptorCache.getClassDescriptor(c);
		MethodDescriptor[] mds = cd.getMethodDescriptors();
		//VirtualMethodDescriptor[] mds = cd.getVirtualMethodDescriptors();
		for (int i = 0; i < mds.length; i++) {
			MethodDescriptor md = mds[i];
			if (md.getMethod() == method)
				return md;
		}
		return null;
		*/
	}
	/*
	public static MethodDescriptor toMethodDescriptor(VirtualMethod method) {
		if (method == null)
			return null;
		return toMethodDescriptor(method.getMethod());
	}
	*/
	/*
	 public static VirtualMethod[] getAllMethods (Class c) {
		 Method[] methods = c.getMethods();
		 VirtualMethod[] vmethods = new VirtualMethod[methods.length];
		 for (int i = 0; i < methods.length; i++) {
			 vmethods[i] = AVirtualMethod.virtualMethod(methods[i]);
		 }
		return vmethods;
		 
		 
	 }
	 */
	public static boolean isVoid(MethodProxy method) {
		return method.getReturnType().equals(method.getDeclaringClass().voidType());
	}
	public static boolean isBoolean(MethodProxy method) {
		return method.getReturnType().equals(method.getDeclaringClass().booleanType());
	}
	public static boolean isString (MethodProxy method) {
		return method.getReturnType().equals(method.getDeclaringClass().stringClass());
	}
	public static boolean isVoidSynchronized(MethodProxy method) {	
	return (isVoid(method) &&
			//method.getReturnType() == Void.TYPE &&
			Modifier.isSynchronized(method.getModifiers()) ); /*&&
			method.getName().toLowerCase().lastIndexOf("animate") != -1);*/
	}
	public static boolean isVolatile(MethodProxy method) {	
		return (isVoid(method) &&
				Modifier.isVolatile(method.getModifiers()) ); /*&&
				method.getName().toLowerCase().lastIndexOf("animate") != -1);*/
		}
	public static boolean isVolatile(Method method) {	
		return (method.getReturnType() == Void.TYPE &&
				Modifier.isVolatile(method.getModifiers()) ); /*&&
				method.getName().toLowerCase().lastIndexOf("animate") != -1);*/
		}
	public static boolean isAsynchronous(MethodProxy method) {	
		//return isAsynchronous (uiMethodInvocationManager.virtualMethod(method));
		return (isVoid(method) &&
				Modifier.isSynchronized(method.getModifiers()) );
	}
	
	public static final String PRE = "pre";
	public static final String VALIDATE = PRE;
	public static String getTypeVariablesString(TypeVariable[] vars) {		
		String retVal = "Of";
		for (int i = 0; i < vars.length; i++) {
			if (i == 0)
				retVal = "Of";
			retVal += vars[i].getName();
			if (i != vars.length -1)
				retVal += "And";
		}
		return retVal;
	}
	public static String getParameterSignature(ClassProxy[] params) {
		String retVal = "";
		for (int i = 0; i < params.length; i++) {
			ClassProxy nextClass = params[i];
			//TypeVariable[] vars = nextClass.getTypeParameters();
			if (nextClass.isArray()) {
				ClassProxy componentType = nextClass.getComponentType();
				if (componentType != null)
					retVal += componentType.getSimpleName() + "Array";
			} else
			retVal += nextClass.getSimpleName() /*+ getTypeVariablesString(vars)*/;
			/*
			Class componentType = nextClass.getComponentType();
			if (componentType != null)
				retVal += "Of" + componentType.getSimpleName();
				*/
		}
		return retVal;
	}
	public static String nameWithSignature(MethodProxy m) {
		return m.getName() + getParameterSignature(m);
	}
	public static String getParameterSignature(MethodProxy m) {
		return getParameterSignature(m.getParameterTypes());
	}		public static boolean isPreEfficient(MethodProxy method, MethodProxy candidate) {		
		return candidate.getParameterTypes().length == 0 &&
			   isBoolean(candidate) &&			   //("pre" + method.getName()).toLowerCase().equals(
			   ((PRE + method.getName()).toLowerCase().equals(
						candidate.getName().toLowerCase()) || 
			   (PRE + method.getName() + getParameterSignature(method)).toLowerCase().equals(
						candidate.getName().toLowerCase())); 
			   	}
	static Map<MethodProxy, MethodProxy> preToMethod = new HashMap();
	static Set<MethodProxy> falsePre = new HashSet();
	
	public static boolean isPre(MethodProxy method, MethodProxy candidate) {
		if (!isBoolean(candidate) || !candidate.getName().toLowerCase().startsWith(PRE) ) {
			//falsePre.add(candidate);
			return  false;
		}
		if (preToMethod.get(candidate) == method)
			return true;
		if (falsePre.contains(candidate))
			return false;
		
		
		
		if (!(
			(PRE + method.getName()).equalsIgnoreCase(candidate.getName()) ||
			(PRE + method.getName() + getParameterSignature(method)).equalsIgnoreCase(candidate.getName()))) {
			//falsePre.add(candidate);
			//Message.info(candidate + " begins with a pre but its suffix does not match any other method in its class");
			
		
			return false;
		}
		
		/*
		if (!isBoolean(candidate)) {
			bus.uigen.Message.warning(candidate + " not recognized as an enabling method as it does not return a boolean");
			return false;		
		}
		*/
		if (candidate.getParameterTypes().length == 0 ) {
			preToMethod.put(candidate, method);
			return true;
		}
		falsePre.add(candidate);
			if (isValidate(method, candidate)) {
				
				Tracer.info(candidate + " considered a validate rather than enabling method because it takes arguments");
				return false;
			} else {
			
			Tracer.warning(candidate + " not recognized as an enabling method of + " + method + " because it takes arguments");
			return false;
			}
		
			/*
			return candidate.getParameterTypes().length == 0 &&
			   isBoolean(candidate) &&
			   //("pre" + method.getName()).toLowerCase().equals(
			   ((PRE + method.getName()).toLowerCase().equals(
						candidate.getName().toLowerCase()) || 
			   (PRE + method.getName() + getParameterSignature(method)).toLowerCase().equals(
						candidate.getName().toLowerCase())); 
			*/   
	
	}
	static Map<MethodProxy, MethodProxy> validateToMethod = new HashMap();
	static Set<MethodProxy> falseValidate = new HashSet();
	public static boolean isValidateEfficient(MethodProxy method, MethodProxy candidate) {		
		return 	
		   isBoolean(candidate) &&
			   (VALIDATE + method.getName()).toLowerCase().equals(
									candidate.getName().toLowerCase()) &&
									assignableFrom(candidate.getParameterTypes(), method.getParameterTypes()) 	
									; 
	}
	public static boolean isValidate(MethodProxy method, MethodProxy candidate) {
		if (!isBoolean(candidate) || !candidate.getName().toLowerCase().startsWith(VALIDATE) )
			return false;
		if (validateToMethod.get(candidate) == method)
			return true;
		if (falseValidate.contains(candidate))
			return false;
		
		if (!(VALIDATE + method.getName()).equalsIgnoreCase(
									candidate.getName())) {
			return false;
		}
		if (assignableFrom(candidate.getParameterTypes(), method.getParameterTypes())) {
			validateToMethod.put(candidate, method);
			return true;
		}
		if (isPre(method, candidate))
			return false;
		falseValidate.add(candidate);
		Tracer.warning(candidate + " not recognized as validate method of " +  method + " as their parameters do not match");
		return false;
		
	}
	public static boolean hasPre (ClassProxy c) {
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (getPre(methods[i], c) != null)
				return true;
		}
		return false;
	}
	/*
	public static boolean assignableFrom (VirtualMethod method1, VirtualMethod method2) {
		return assignableFrom (method1.getParameterTypes(), method2.getParameterTypes());
	}
	*/
	public static boolean assignableFrom (ClassProxy[] from, ClassProxy[] to ) {
		
		if (from.length != to.length) return false;
		for (int i = 0; i < from.length; i++) 
			if (!from[i].isAssignableFrom(to[i]))
				return false;
		return true;
	}
	public static boolean equalSignature (MethodProxy method1, MethodProxy method2 ) {
		ClassProxy[] pType1 = method1.getParameterTypes();
		ClassProxy rType1 = method1.getReturnType();
		ClassProxy[] pType2 = method2.getParameterTypes();
		ClassProxy rType2 = method2.getReturnType();
		if (pType1.length != pType2.length) return false;
		if (rType1 != rType2) return false;
		for (int i = 0; i < pType1.length; i++) 
			if (pType1[i] != pType2[i])
				return false;
		return true;
	}
	
	public static boolean isUndo (String method, String candidate) {
		return  candidate.toLowerCase().equals("undo" + method.toLowerCase());
	}
	public static MethodProxy getUndo (Object parentObject, MethodProxy method) {
		MethodProxy[] methods = RemoteSelector.getClass(parentObject).getMethods();
		for (int i = 0; i < methods.length; i++) {
			MethodProxy candidate = methods[i];
			if (isUndo(method.getName(), candidate.getName()) &&
					equalSignature(method, candidate))
				return candidate;
			
		}
		return null;
		
	}
	
	public static boolean isPre(Method method, Method candidate) {	
		
		//return isPre (uiMethodInvocationManager.virtualMethod(method), uiMethodInvocationManager.virtualMethod(candidate)); 
		// we need efficiency here
		return candidate.getParameterTypes().length == 0 &&
		candidate.getReturnType() == Boolean.TYPE &&
		   ("pre" + method.getName()).toLowerCase().equals(
								candidate.getName().toLowerCase()); 
	}
	public static boolean isPre(MethodProxy candidate) {		
		return //candidate.getParameterTypes().length == 0 &&
			   isBoolean(candidate) &&
			   candidate.getName().startsWith("pre");	}
	public static boolean isPre(Method candidate) {		
		//return isPre (uiMethodInvocationManager.virtualMethod(candidate));
		return candidate.getParameterTypes().length == 0 &&
		   candidate.getReturnType() == Boolean.TYPE &&
		   candidate.getName().startsWith("pre");
	}
	
		public static Method getPre(Method method, Class c) {
		if (method == null || c == null) return null;		Method[] methods = c.getMethods();		for (int i = 0; i < methods.length; i++)			if (isPre(method, methods[i])) return methods[i];		return null;
			}
	public static MethodProxy getPre(MethodProxy method, ClassProxy c) {
		if (method == null || c == null) return null;
		if (method.isDynamicCommand()) { // dynamic method
			return DynamicMethods.getDynamicCommandPre(c);
		}
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isPre(method, methods[i])) return methods[i];
		return null;
		
	}
	public static MethodProxy getValidate(MethodProxy method, ClassProxy c) {
		if (method == null || c == null) return null;
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isValidate(method, methods[i])) return methods[i];
		return null;
		
	}
	
	public static boolean isGetter(Method method) {
		//return isGetter (uiMethodInvocationManager.virtualMethod(method));
		
		String name = method.getName();
		if (name.startsWith("get") && !name.equals("get")) {
			if (method.getParameterTypes().length == 0 &&
				!method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		return  false;
			}
	public static boolean isGetter(MethodProxy method) {
		String name = method.getName();
		if (name.startsWith("get") && !name.equals("get")) {
			if (method.getParameterTypes().length == 0 &&
				!method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		return  false;
	}
	public static boolean isGetter(MethodProxy method, String propertyName) {
		String name = method.getName();
		if (name.equals("get" + propertyName) ||
				name.equals("is" + propertyName )) {
			if (method.getParameterTypes().length == 0 &&
				!method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		return  false;
	}	public static boolean isStaticGetter(MethodProxy method) {		if (Modifier.isStatic(method.getModifiers()))			return isGetter(method);
		else 
			return false;	}	public static boolean isStaticSetter(MethodProxy method) {		if (Modifier.isStatic(method.getModifiers()))			return isGetter(method);
		else 
			return false;	}
	
	public static boolean isIncrementer(MethodProxy method) {
		String name = method.getName();
		if (name.toLowerCase().equals("increment") && method.getParameterTypes().length == 0)
				return true;
			else
				return false;
		
	}
	public static boolean isDecrementer(MethodProxy method) {
		String name = method.getName();
		if (name.toLowerCase().equals("decrement") && method.getParameterTypes().length == 0)
				return true;
			else
				return false;
		
	}
	public static boolean isAssignableFrom (ClassProxy to, ClassProxy from)  {		
		return to.isAssignableFrom(from) || 
		(DefaultRegistry.getWrapper(to) != null && 
				DefaultRegistry.getWrapper(to).isAssignableFrom(from));
	}
	public static boolean isChildIncrementer(MethodProxy method, ClassProxy childType) {	

		String name = method.getName();
		if (name.toLowerCase().equals("increment") && 
				method.getParameterTypes().length == 1 && 
				//method.getParameterTypes()[0].isAssignableFrom( childType))
				isAssignableFrom(method.getParameterTypes()[0], childType))
				return true;
			else
				return false;
		
	}
	public static boolean isChildIncrementer(MethodProxy method, String p) {	

		String name = method.getName();
		if (name.toLowerCase().equals("increment" + p.toLowerCase()) && 
				method.getParameterTypes().length == 0) 
				//&& 
				//method.getParameterTypes()[0].isAssignableFrom( childType))
				//isAssignableFrom(method.getParameterTypes()[0], childType))
				return true;
			else
				return false;
		
	}
	public static boolean isChildDecrementer(MethodProxy method, ClassProxy childType) {
		String name = method.getName();
		if (name.toLowerCase().equals("decrement") && 
				method.getParameterTypes().length == 1 && 
				//method.getParameterTypes()[0].isAssignableFrom( childType))
				isAssignableFrom(method.getParameterTypes()[0], childType))
				return true;
			else
				return false;
		
	}
	public static boolean isChildDecrementer(MethodProxy method, String p) {	

		String name = method.getName();
		if (name.toLowerCase().equals("decrement" + p.toLowerCase()) && 
				method.getParameterTypes().length == 0) 
				//&& 
				//method.getParameterTypes()[0].isAssignableFrom( childType))
				//isAssignableFrom(method.getParameterTypes()[0], childType))
				return true;
			else
				return false;
		
	}
		public static String getPropertyName(Method method) {
		//return getPropertyName (uiMethodInvocationManager.virtualMethod(method));
		
		String name = method.getName();		return name.substring(3, name.length());
					}
	public static String getPropertyName(MethodProxy method) {
		String name = method.getName();
		return name.substring(3, name.length());		
	}
	
	//if class implements the get<PropName>Alternatives method then this method returns those string alternatives
	public static String[] getPropertyAlternatives(Object realObject, String _propertyName) {		//dunno if object or class is best to pass in
		//property name should have 1st letter alread capitalized.		try {
			//System.out.println("PropertyName is " + _propertyName);			//String propertyName = _propertyName.toLowerCase();			String propertyName = _propertyName;
			
			//System.exit(0);
						
			String methodName = propertyName+"Alternatives";
			System.out.println("uibean looking for " + methodName);			MethodProxy[] methods = RemoteSelector.getClass(realObject).getMethods();
			boolean foundMethod = false;
			int i = 0;			while (!foundMethod && (i < methods.length)) {				System.out.println("..." + methods[i].getName());
				if (methods[i].getName().equals(methodName))  { //if found the method
					foundMethod = true;					System.out.println("uibean found method and returning array");				//	System.exit(0);
					return ((String[])methods[i].invoke(realObject,null));  //return the vector of alternatives
				}				else					i++;			}//end while			return null;		}//end try
				catch (Exception er) {System.out.println("uibean getPropAlt exception");							  return null;		}
		
	}//end getPAlt		
	//if the class declares any constants/finals of a property and its type then these can be alternatives in the
	//JComboBox  e.g.   MALESEX = 2; FEMALESEX = 1; and getSex(...) is implemented then Sex should be filled w/ those constants	public static Vector getPropertyTypeFinals(Object realObject, String _propertyName, ClassProxy propertyClass) {		
				//System.out.println("Getting finals for " + _propertyName  + "  of type  " + propertyClass.getName() + " " + Modifier.FINAL);
		Vector matchedFields = new Vector(); 
		try {			FieldProxy[] fields = RemoteSelector.getClass(realObject).getFields();
			System.out.println("Getting finals for" + _propertyName + "with field count-" + fields.length);			int i = 0;			while (i < fields.length) {
			//	System.out.println("!!!working on: "+ fields[i].getName() +  " " + fields[i].getModifiers() + " " +primitiveClassList.getWrapperType(fields[i].getType()).getName());				if (fields[i].getModifiers() == Modifier.FINAL + Modifier.PUBLIC) {   //if the field is final then now check name
																		 //add 1 because of the public					if (fields[i].getName().endsWith(_propertyName.toUpperCase()))  {  //if it ends with the caps of propName						if (PrimitiveClassList.getWrapperType(fields[i].getType()).getName().equals(propertyClass.getName()))  {//if they are the same type
							matchedFields.addElement(fields[i]);  //add it to the vector
							//System.out.println("!!!finals added: "+ fields[i].getName());							
						}
					}
				}				i++;
			}//endwhile
		}
		
		catch (Exception ex) {System.out.println(ex.toString());}								//System.out.println("found final # " + matchedFields.size());
		return matchedFields;
		
	}		
	
	public static Enumeration getAllPropertyNames(Object realObject) {
		Hashtable propHash = new Hashtable();
		//Method[] methods = realObject.getClass().getMethods();
		MethodProxy[] methods = getMethods(RemoteSelector.getClass(realObject));
		
		for (int i=0; i < methods.length; i++)  {//for each method
			if (isSimplePropertyMethod(methods[i])) {  //if it's a property method
								String aProperty = methods[i].getName().substring(3);  //take the 3 letter set/get off
				propHash.put(aProperty,aProperty);  //hash it  ...assume get*sets will overwrite eachother
			}
		}
				if (!propHash.isEmpty())			return propHash.elements();
		else 
			return null;
		
	}//end getPNam	
	public static boolean isSetter(MethodProxy method) {
		String name = method.getName();		if (name.startsWith("set")) {
			if (method.getParameterTypes().length == 1 &&
				method.getReturnType().equals(method.getDeclaringClass().voidType()))
				return true;
			else
				return false;
		}
		else
			return false;	
			}
	public static boolean isGeneralizedSetter(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("set")) ;		
	}	
	public static boolean isGeneralizedGetter(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("get"));	
	}
	
	public static boolean isSetter(Method method) {
		//return isSetter(uiMethodInvocationManager.virtualMethod(method));
		
		String name = method.getName();
		if (name.startsWith("set")) {
			if (method.getParameterTypes().length == 1 &&
				method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		else
			return false;
				
		
	}
	public static boolean isComplexSetter(MethodProxy method) {		if (isSetter(method)) {
			if (uiGenerator.isPrimitiveClass(method.getParameterTypes()[0]))
				return false;
			else
				return true;
		}		return false;    	}	
	public static boolean isSimplePropertyMethod(MethodProxy method) {
		String name = method.getName();
		if (name.startsWith("get") && !name.equals("get")) {
			if (method.getParameterTypes().length == 0 &&
				!method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		else if (name.startsWith("set")) {
			if (method.getParameterTypes().length == 1 &&
				method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	public static boolean isSimplePropertyMethod(Method method) {
		String name = method.getName();
		if (name.startsWith("get") && !name.equals("get")) {
			if (method.getParameterTypes().length == 0 &&
				!method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		else if (name.startsWith("set")) {
			if (method.getParameterTypes().length == 1 &&
				method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	public static boolean isBooleanPropertyMethod(MethodProxy method) {
		ClassProxy c = method.getDeclaringClass();		if (isVector(method.getDeclaringClass()) && method.getName().equals("isEmpty"))			return false;
		String name = method.getName();
		if (name.startsWith("is") || name.startsWith("get")) {
			if (method.getParameterTypes().length == 0 &&
				//method.getReturnType().equals(Boolean.TYPE))
				method.getReturnType().equals(c.booleanType()))
				return true;
			else
				return false;
		}
		else if (name.startsWith("set")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				//params[0].equals(Boolean.TYPE) &&
				params[0].equals(c.booleanType()) &&
				method.getReturnType().equals(c.voidType()))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	public static boolean isBooleanPropertyMethod(Method method) {
		if (isVector(method.getDeclaringClass()) && method.getName().equals("isEmpty"))
			return false;
		String name = method.getName();
		if (name.startsWith("is") || name.startsWith("get")) {
			if (method.getParameterTypes().length == 0 &&
				method.getReturnType().equals(Boolean.TYPE))
				return true;
			else
				return false;
		}
		else if (name.startsWith("set")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(Boolean.TYPE) &&
				method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	public static boolean isIndexedPropertyMethod(MethodProxy method) {
		ClassProxy c = method.getDeclaringClass();
		String name = method.getName();
		if (name.startsWith("get")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(c.integerType()) &&
				!method.getReturnType().equals(c.voidType()))
				return true;
			else
				return false;
		}
		else if (name.startsWith("set")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 2 &&
				params[0].equals(c.integerType()))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	public static boolean isIndexedPropertyMethod(Method method) {
		String name = method.getName();
		if (name.startsWith("get")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(Integer.TYPE) &&
				!method.getReturnType().equals(Void.TYPE))
				return true;
			else
				return false;
		}
		else if (name.startsWith("set")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 2 &&
				params[0].equals(Integer.TYPE))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	public static boolean isBeanPropertyMethod(MethodProxy method) {
		return (isSimplePropertyMethod(method) ||
				isBooleanPropertyMethod(method) ||
				isIndexedPropertyMethod(method));
	}
	public static boolean isBeanPropertyMethod(Method method) {
		return (isSimplePropertyMethod(method) ||
				isBooleanPropertyMethod(method) ||
				isIndexedPropertyMethod(method));
	}
	
	/*
	public static util.Explanation explanationClass() {
		return ClassSelector.classProxy(util.Explanation.class);
	}
	public static ClassProxy htmlDocumentationClass() {
		return ClassSelector.classProxy(util.HTMLDocumentation.class);
	}
	public static ClassProxy keywordsClass() {
		return ClassSelector.classProxy(util.Keywords.class);
	}
	*/
	public static ClassProxy vectorClass() {
		return RemoteSelector.classProxy(Vector.class);
	}
	public static ClassProxy hashtableClass() {
		return RemoteSelector.classProxy(Hashtable.class);
	}
	public static ClassProxy colorClass() {
		return RemoteSelector.classProxy(java.awt.Color.class);
	}
	public static ClassProxy pointClass() {
		return RemoteSelector.classProxy(java.awt.Point.class);
	}
	public static ClassProxy remoteShapeClass() {
		return RemoteSelector.classProxy(RemoteShape.class);
	}
	public static ClassProxy remotePointClass() {
		return RemoteSelector.classProxy(RemotePoint.class);
	}
	public static ClassProxy remoteLineClass() {
		return RemoteSelector.classProxy(RemoteLine.class);
	}
	public static ClassProxy remoteTextClass() {
		return RemoteSelector.classProxy(RemoteText.class);
	}
	public static ClassProxy remoteRectangleClass() {
		return RemoteSelector.classProxy(RemoteRectangle.class);
	}
	public static ClassProxy remoteOvalClass() {
		return RemoteSelector.classProxy(RemoteOval.class);
	}
	public static ClassProxy pointModelClass() {
		return RemoteSelector.classProxy(PointModel.class);
	}
	
	public static ClassProxy labelModelClass() {
		return RemoteSelector.classProxy(LabelModel.class);
	}
	public static ClassProxy arcModelClass() {
		return RemoteSelector.classProxy(ArcModel.class);
	}
	public static ClassProxy curveModelClass() {
		return RemoteSelector.classProxy(CurveModel.class);
	}
	public static ClassProxy rectangleModelClass() {
		return RemoteSelector.classProxy(RectangleModel.class);
	}
	public static ClassProxy ovalModelClass() {
		return RemoteSelector.classProxy(OvalModel.class);
	}
	public static ClassProxy lineModelClass() {
		return RemoteSelector.classProxy(LineModel.class);
	}
	public static ClassProxy textModelClass() {
		return RemoteSelector.classProxy(TextModel.class);
	}
	
	public static boolean isIndexOfMethod(MethodProxy method) {
		String name = method.getName();
		if (name.startsWith("indexOf")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				//params[0].equals(Object.class) &&
				method.getDeclaringClass().integerType().isAssignableFrom( method.getReturnType()))
				return true;
			else
				return false;  		}		return false;
	}
	/*
	public static boolean isElementAtMethod(VirtualMethod method, int numIndices) {
		String name = method.getName();
		if (name.startsWith("elementAt")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(Integer.TYPE) &&
				(Object.class).isAssignableFrom( method.getReturnType()))
				return true;
			else
				return false;  		}		return false;
	}
	*/
	/*
	public static boolean isIndexOfMethod(VirtualMethod method, int numIndices) {
		String name = method.getName();
		if (name.startsWith("indexOf")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&
				//params[0].equals(Integer.TYPE) &&
				 method.getReturnType() == Integer.TYPE)
				return true;
			else
				return false;  
		}
		return false;
	}
	*/
	public static boolean isElementAtMethod(MethodProxy method, int numIndices) {
		String name = method.getName();
		//if (name.startsWith("elementAt")) {
		if (name.startsWith("insert")) return false;
		if (name.startsWith("remove")) return false;
		if (isInsertElementAtMethod(method)) return false;
		if (isRemoveElementMethod(method)) return  false;
		if (name.endsWith("At")) {
			ClassProxy[] params = method.getParameterTypes();
			if (checkIndices (params, numIndices) &&
				(method.getDeclaringClass().objectClass()).isAssignableFrom( method.getReturnType()))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isElementAtMethod(MethodProxy method) {
		return isElementAtMethod(method, 1);
		
	}
	public static boolean checkIndices (ClassProxy[] params, int numIndices) {
		if (params.length != numIndices) return false;
		for (int i = 0; i < numIndices; i++) {
			if (!params[i].equals(params[i].integerType())) return false;
		}
		return true;		
	}
	public static boolean isElementAtMethod(Method method) {
		String name = method.getName();
		if (name.startsWith("elementAt")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(Integer.TYPE) &&
				(Object.class).isAssignableFrom( method.getReturnType()))
				return true;
			else
				return false;  
		}
		return false;
	}	
	public static boolean isSetElementAtMethod(MethodProxy method) {
		String name = method.getName();
		if (name.startsWith("setElementAt")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 2 &&				method.getDeclaringClass().objectClass().isAssignableFrom(params[0]) &&
				params[1].equals(method.getDeclaringClass().integerType()) )
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isSetElementAtMethod(Method method) {
		String name = method.getName();
		if (name.startsWith("setElementAt")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 2 &&
				Object.class.isAssignableFrom(params[0]) &&
				params[1].equals(Integer.TYPE) )
				return true;
			else
				return false;  
		}
		return false;
	}	public static boolean isAddElementMethod(MethodProxy method) {		
		String name = method.getName();
		//if (name.startsWith("addElement")) {
		if (name.startsWith("add")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&				method.getDeclaringClass().objectClass().isAssignableFrom(params[0]))
				return true;
			else
				return false;  		}		return false;
	}	public static ClassProxy getAddElementClass(MethodProxy method) {		
		String name = method.getName();
		//if (name.startsWith("addElement")) {
		if (name.startsWith("add")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&				method.getDeclaringClass().objectClass().isAssignableFrom(params[0]))
				return params[0];
			else
				return null;  		}		return null;
	}	public static ClassProxy getVectorElementClass(ClassProxy vectorClass) {		if (vectorClass == vectorClass())			return vectorClass.objectClass();		else return getAddElementClass(getAddElementMethod(vectorClass));
	}
			public static boolean isInsertElementAtMethod(MethodProxy method) {
		String name = method.getName();
		//if (name.startsWith("insertElementAt")) {
		if (name.startsWith("insert") && name.endsWith("At")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 2 &&				method.getDeclaringClass().objectClass().isAssignableFrom(params[0]) &&
				params[1].equals(method.getDeclaringClass().integerType()) )
				return true;
			else
				return false;  		}		return false;
	}		public static boolean isRemoveElementMethod(MethodProxy method) {
		String name = method.getName();
		if (name.startsWith("removeElement")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&				method.getDeclaringClass().objectClass().isAssignableFrom(params[0]))
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isRemoveElementMethod(Method method) {
		String name = method.getName();
		if (name.startsWith("removeElement")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&
				Object.class.isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}		public static boolean isRemoveElementAtMethod(MethodProxy method) {
		String name = method.getName();
		//if (name.startsWith("removeElementAt")) {
		if (name.startsWith("remove") && name.endsWith("At")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(method.getDeclaringClass().integerType()) )
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isRemoveElementAtMethod(Method method) {
		String name = method.getName();
		if (name.startsWith("removeElementAt")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(Integer.TYPE) )
				return true;
			else
				return false;  
		}
		return false;
	}	
	public static boolean isPutMethod(Method method) {		
		String name = method.getName();
		if (name.startsWith("put")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 2 &&				Object.class.isAssignableFrom(params[0]) &&				Object.class.isAssignableFrom(params[1]))
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isPutMethod(MethodProxy method) {
		
		String name = method.getName();
		if (name.startsWith("put")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 2 &&
				method.getDeclaringClass().objectClass().isAssignableFrom(params[0]) &&
				method.getDeclaringClass().objectClass().isAssignableFrom(params[1]))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isRemoveMethod(MethodProxy method) {		
		String name = method.getName();
		if (name.startsWith("remove")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&				method.getDeclaringClass().objectClass().isAssignableFrom(params[0]))
				return true;
			else
				return false;  		}		return false;
	}
    	public static boolean isGetMethod(Method method) {
		//return isGetMethod (uiMethodInvocationManager.virtualMethod(method));
		
		String name = method.getName();
		if (name.equals("get")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 1 &&				(Object.class).isAssignableFrom( method.getReturnType()))
				return true;
			else
				return false;  		}		return false;
		
	}
	public static boolean isGetMethod(MethodProxy method) {
		String name = method.getName();
		if (name.equals("get")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				(method.getDeclaringClass().objectClass()).isAssignableFrom( method.getReturnType()))
				return true;
			else
				return false;  
		}
		return false;
	}
	
	public static boolean isIsEmptyMethod(MethodProxy method) {
		String name = method.getName();
		if (name.toLowerCase().equals("isempty")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0)
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isSizeMethod(MethodProxy method) {
		String name = method.getName();
		if (name.startsWith("size")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(method.getDeclaringClass().integerType()))
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isSizeMethod(Method method) {
		String name = method.getName();
		if (name.startsWith("size")) {
			Class[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(Integer.TYPE))
				return true;
			else
				return false;  
		}
		return false;
	}
		public static boolean isEnumerationGetter(MethodProxy method) {		
		String name = method.getName();		/*
		if (name.startsWith("elements")) {
		*/		
		ClassProxy[] params = method.getParameterTypes();
		if (params.length == 0 &&
			//method.getReturnType().isAssignableFrom(Enumeration.class)) {
			(method.getDeclaringClass().enumerationClass()).isAssignableFrom(method.getReturnType())) {			//System.out.println(method.getReturnType().getName());
			return true;
		} else
			return false;
		/*  		}		return false;
		*/		
	}
	public static boolean isElementsMethod(MethodProxy method) {		
		String name = method.getName();		
		if (name.startsWith("elements")) {			
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				//method.getReturnType().isAssignableFrom(Enumeration.class)) {
				(method.getDeclaringClass().enumerationClass()).isAssignableFrom(method.getReturnType())) {				//System.out.println(method.getReturnType().getName());
				return true;
			} else
				return false;
					}		return false;
				
	}
public static boolean isElementsMethod(Method method) {
		
		String name = method.getName();
		
		if (name.startsWith("elements")) {
			
			Class[] params = method.getParameterTypes();
			if (params.length == 0 &&
				//method.getReturnType().isAssignableFrom(Enumeration.class)) {
				(Enumeration.class).isAssignableFrom(method.getReturnType())) {
				//System.out.println(method.getReturnType().getName());
				return true;
			} else
				return false;
			
		}
		return false;
		
		
	}
	public static boolean isVariablePropertyMethod (MethodProxy method) {		return OriginalUIBean.isElementAtMethod(method) ||
			   OriginalUIBean.isElementsMethod(method) ||
			   OriginalUIBean.isIndexedPropertyMethod(method) ||
			   OriginalUIBean.isKeysMethod(method) ||			   OriginalUIBean.isSizeMethod(method) ||			   OriginalUIBean.isSetElementAtMethod(method) ||			   OriginalUIBean.isRemoveElementAtMethod(method) ||			   OriginalUIBean.isRemoveElementMethod(method) ||
			   OriginalUIBean.isPre(method);		
	}
	public static boolean isVariablePropertyMethod (Method method) {
		return OriginalUIBean.isElementAtMethod(method) ||
			   OriginalUIBean.isElementsMethod(method) ||
			   OriginalUIBean.isIndexedPropertyMethod(method) ||
			   OriginalUIBean.isKeysMethod(method) ||
			   OriginalUIBean.isSizeMethod(method) ||
			   OriginalUIBean.isSetElementAtMethod(method) ||
			   OriginalUIBean.isRemoveElementAtMethod(method) ||
			   OriginalUIBean.isRemoveElementMethod(method) ||
			   OriginalUIBean.isPre(method);
		
	}	public static boolean isKeysMethod(MethodProxy method) {		
		String name = method.getName();	
		if (name.startsWith("keys")) {		  
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				//method.getReturnType().isAssignableFrom(Enumeration.class)) {
				(method.getDeclaringClass().enumerationClass()).isAssignableFrom(method.getReturnType())) {				//System.out.println(method.getReturnType().getName());
				return true;
			} else
				return false;		}		return false;	  
	}
public static boolean isChecker (MethodProxy method) {
	ClassProxy[] params = method.getParameterTypes();
	if (params.length == 0 &&
		//method.getReturnType().isAssignableFrom(Enumeration.class)) {
		(method.getDeclaringClass().booleanType()).isAssignableFrom(method.getReturnType())) {
		//System.out.println(method.getReturnType().getName());
		return true;
	} else
		return false;
		
	
}
public static boolean isAssignableFrom (ClassProxy[] to, ClassProxy[] from) {
	if (to.length != from.length) return false;
	for (int i= 0; i < to.length; i++)
		if (!to[i].isAssignableFrom(from[i])) return false;
	return true;
	
	
}
public static boolean isChecker (MethodProxy method, ClassProxy[] arguments) {
	ClassProxy[] params = method.getParameterTypes();
	if (isAssignableFrom(params, arguments) &&
		//method.getReturnType().isAssignableFrom(Enumeration.class)) {
		(method.getDeclaringClass().booleanType()).isAssignableFrom(method.getReturnType())) {
		//System.out.println(method.getReturnType().getName());
		return true;
	} else
		return false;
		
	
}

public static boolean isViewRefresher(MethodProxy method) {
	ClassProxy[] params = method.getParameterTypes();
	if (params.length != 0 ) return false;
	return method.getName().equals(OriginalUIBean.VIEW_REFRESHER);
}

public static boolean isSetChecker (MethodProxy method) {
	ClassProxy[] params = method.getParameterTypes();
	if (params.length != 1 ) return false;
	if (params[0].isAssignableFrom(method.getDeclaringClass().booleanType())) return true;
	return false;
	/*
		return false;
		
		&&
		//method.getReturnType().isAssignableFrom(Enumeration.class)) {
		(Boolean.TYPE).isAssignableFrom(method.getReturnType())) {
		//System.out.println(method.getReturnType().getName());
		return true;
	} else
		return false;
		*/
		
	}

public static boolean isChecker (MethodProxy method, ClassProxy argType) {
	ClassProxy from[] = {argType};
	return isChecker(method, from);
}


public static boolean isIsEditableKey (MethodProxy method) {
	String methodNameLC = method.getName().toLowerCase();
	return (methodNameLC.equals("iseditablekey") ||  methodNameLC.equals("iskeyeditable"))
	&& isChecker(method, method.getDeclaringClass().objectClass());
}

public static boolean isIsEditableElement (MethodProxy method) {
	String methodNameLC = method.getName().toLowerCase();
	return (methodNameLC.equals("iseditableelement") || methodNameLC.equals("iseditable") || methodNameLC.equals("iseditablevalue") || methodNameLC.equals("isvalueeditable") || methodNameLC.equals("iselementeditable"))
	&& isChecker(method, method.getDeclaringClass().objectClass());
}
public static boolean isIsRemovable (MethodProxy method) {
	return method.getName().toLowerCase().startsWith("isremovable") && isChecker(method, method.getDeclaringClass().objectClass());
}
public static MethodProxy getIsEditableKey(ClassProxy c) {
	//Object[] params = {};
	//Method[] methods = c.getMethods();
	MethodProxy[] methods = getMethods(c);
	for (int i = 0; i < methods.length; i++)
		if (isIsEditableKey(methods[i])) return methods[i];
	return null;
}
public static MethodProxy getIsEditableElement(ClassProxy c) {
	//Object[] params = {};
	//Method[] methods = c.getMethods();
	MethodProxy[] methods = getMethods(c);
	for (int i = 0; i < methods.length; i++)
		if (isIsEditableElement(methods[i])) return methods[i];
	return null;
}
public static MethodProxy getViewRefresher (Object viewRefresher) {
	if (viewRefresher == null)
		return null;
	ClassProxy viewClass = RemoteSelector.getClass(viewRefresher);
	MethodProxy[] methods = getMethods(viewClass);
	for (int i = 0; i < methods.length; i++)
		if (isViewRefresher(methods[i])) return methods[i];
	return null;
	
}
public static MethodProxy getIsRemovable(ClassProxy c) {
	//Object[] params = {};
	//Method[] methods = c.getMethods();
	MethodProxy[] methods = getMethods(c);
	for (int i = 0; i < methods.length; i++)
		if (isIsRemovable(methods[i])) return methods[i];
	return null;
}
public static boolean isKeysMethod(Method method) {
		
		String name = method.getName();	
		if (name.startsWith("keys")) {		  
			Class[] params = method.getParameterTypes();
			if (params.length == 0 &&
				//method.getReturnType().isAssignableFrom(Enumeration.class)) {
				(Enumeration.class).isAssignableFrom(method.getReturnType())) {
				//System.out.println(method.getReturnType().getName());
				return true;
			} else
				return false;
		}
		return false;	  
	}	public static boolean isGetIntMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("get" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(method.getDeclaringClass().integerType()))
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isGetPointMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("get" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				//method.getReturnType().equals(Integer.TYPE))
				whichShape(method.getReturnType()) == OriginalUIBean.POINT_SHAPE)
				return true;
			else
				return false;  
		}
		return false;
	}
	
	 	public static boolean isGetMethod(Method method, String s, Class c) {
		//return isGetMethod (uiMethodInvocationManager.virtualMethod (method));
		
		String name = method.getName();
		if (name.equals("get" + s)) {
			Class[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(c))
				return true;
			else
				return false;  		}		return false;
		
	}
	public static boolean isGetMethod(MethodProxy method, String s, ClassProxy c) {
		String name = method.getName();
		if (name.equals("get" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(c))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isGetOrIsMethod(MethodProxy method, String s, ClassProxy c) {
		String name = method.getName();
		if (name.equals("get" + s) || name.equals("is" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(c))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isGetOrIsBooleanMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("get" + s) || name.equals("is" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(Boolean.class) || method.getReturnType().equals(method.getDeclaringClass().booleanType()))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isGeneralizedGetMethod(MethodProxy method, String s, ClassProxy c) {
		String name = method.getName();
		if (name.equals("get" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if ( //params.length == 0 &&
				method.getReturnType().equals(c))
				return true;
			else
				return false;  
		}
		return false;
	}	
		public static boolean isSetIntMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("set" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(method.getDeclaringClass().integerType()) )
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isSetPointMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("set" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				//params[0].equals(Integer.TYPE) )
					whichShape(params[0]) == OriginalUIBean.POINT_SHAPE)
				return true;
			else
				return false;  
		}
		return false;
	}	public static boolean isSetMethod(MethodProxy method, String s, ClassProxy c) {
		String name = method.getName();
		if (name.equals("set" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(c) )
				return true;
			else
				return false;  		}		return false;
	}
	public static boolean isSetBooleanMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("set" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				(params[0].equals(method.getDeclaringClass().booleanType()) || params[0].equals(Boolean.class)) )
				return true;
			else
				return false;  
		}
		return false;
	}
	
		public static boolean isGetStringMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("get" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 0 &&
				method.getReturnType().equals(method.getDeclaringClass().stringClass()))
				return true;
			else
				return false;  		}		return false;
	}	public static boolean isSetStringMethod(MethodProxy method, String s) {
		String name = method.getName();
		if (name.equals("set" + s)) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				params[0].equals(method.getDeclaringClass().stringClass()) )
				return true;
			else
				return false;  		}		return false;
	}
	public static MethodProxy getIncrementer(ClassProxy c) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isIncrementer(methods[i])) return methods[i];
		return null;
	}
	
	
	public static MethodProxy getDecrementer(ClassProxy c) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isDecrementer(methods[i])) return methods[i];
		return null;
	}
	public static MethodProxy getChildIncrementer(ClassProxy c, ClassProxy childType) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isChildIncrementer(methods[i], childType)) return methods[i];
		return null;
	}
	
	public static MethodProxy getChildDecrementer(ClassProxy c, ClassProxy childType) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isChildDecrementer(methods[i], childType)) return methods[i];
		return null;
	}
	public static MethodProxy getChildIncrementer(ClassProxy c, String p) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isChildIncrementer(methods[i], p)) return methods[i];
		return null;
	}
	public static MethodProxy getChildIncrementer(ClassDescriptorInterface cd, String p) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodDescriptorProxy[] methods = cd.getMethodDescriptors();
		for (int i = 0; i < methods.length; i++) {
			MethodProxy vm = VirtualMethodDescriptor.getVirtualMethod(methods[i]);
			if (isChildIncrementer(vm, p)) return vm;
		}
		return null;
	}
	public static MethodProxy getChildDecrementer(ClassProxy c, String p) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isChildDecrementer(methods[i], p)) return methods[i];
		return null;
	}
	public static MethodProxy getChildDecrementer(ClassDescriptorInterface cd, String p) {
		//Object[] params = {};
		//Method[] methods = c.getMethods();
		MethodDescriptorProxy[] methods = cd.getMethodDescriptors();
		for (int i = 0; i < methods.length; i++) {
			MethodProxy vm = VirtualMethodDescriptor.getVirtualMethod(methods[i]);
			if (isChildDecrementer(vm, p)) return vm;
		}
		return null;
	}
	public static MethodProxy getSizeMethod(ClassProxy c) {
		//Object[] params = {};		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isSizeMethod(methods[i])) return methods[i];		return null;	}
	public static Method getSizeMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getSizeMethod(cp);
		return vm.getMethod();
	}
	/*	public static VirtualMethod getElementAtMethod(Class c) {
		//Object[] params = {Integer.TYPE};		VirtualMethod[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isElementAtMethod(methods[i])) return methods[i];		return null;	}
	*/
	public static Method getElementAtMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getElementAtMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
		//return getElementAtMethod (c, 1);
	}
	public static MethodProxy getElementAtMethod(ClassProxy c) {
		return getElementAtMethod (c, 1);
	}
	public static MethodProxy getElementAtMethod(ClassProxy c, int numIndices) {
		//Object[] params = {Integer.TYPE};
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isElementAtMethod(methods[i], numIndices)) return methods[i];
		return null;
	}	public static MethodProxy getAddElementMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isAddElementMethod(methods[i])) return methods[i];		return null;	}
	public static Method getAddElementMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getAddElementMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
	}
		public static MethodProxy getInsertElementAtMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isInsertElementAtMethod(methods[i])) return methods[i];		return null;	}
	public static Method getInsertElementAtMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getInsertElementAtMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
	}	public static MethodProxy getRemoveElementMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isRemoveElementMethod(methods[i])) return methods[i];		return null;	}
	public static Method getRemoveElementMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getRemoveElementMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
	}	public static MethodProxy getRemoveElementAtMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isRemoveElementAtMethod(methods[i])) return methods[i];		return null;	}
	public static Method getRemoveElementAtMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getRemoveElementAtMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
	}	public static MethodProxy getIndexOfMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isIndexOfMethod(methods[i])) return methods[i];		return null;	}
	public static MethodProxy getSetElementAtMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isSetElementAtMethod(methods[i])) return methods[i];		return null;	}
	public static Method getSetElementAtMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getSetElementAtMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
	}
	public static MethodProxy getPutMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isPutMethod(methods[i])) return methods[i];		return null;	}
	public static Method getPutMethod(Class c) {
		//Object[] params = {Integer.TYPE};
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getPutMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
		
	}
	public static MethodProxy getRemoveMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		MethodProxy[] methods = c.getMethods();		for (int i = 0; i < methods.length; i++)			if (isRemoveMethod(methods[i])) return methods[i];		return null;	}
	public static Method getRemoveMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getRemoveMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
	}	public static MethodProxy getGetMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isGetMethod(methods[i])) return methods[i];		return null;	}
	public static Method getGetMethod(Class c) {
		//Object[] params = {Integer.TYPE};
		//Method[] methods = c.getMethods();
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod virtualMethod = (AVirtualMethod) getGetMethod(cp);
		if (virtualMethod == null) return null;
		return virtualMethod.getMethod();
		/*
		MethodPr[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isGetMethod(methods[i])) return methods[i];
		return null;
		*/
	}
	
	public static MethodProxy getElementsMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isElementsMethod(methods[i])) return methods[i];		return null;	}
	
	public static Method getElementsMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		AVirtualMethod vm = (AVirtualMethod) getElementsMethod(cp);
		if (vm == null) return null;
		return vm.getMethod();
		/*
		//Object[] params = {Integer.TYPE};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isElementsMethod(methods[i])) return methods[i];
		return null;
		*/
	}
	
	
	
	public static MethodProxy getKeysMethod(ClassProxy c) {
		//Object[] params = {Integer.TYPE};		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isKeysMethod(methods[i])) return methods[i];		return null;	}
	public static Method getKeysMethod(Class c) {
		ClassProxy cp = AClassProxy.classProxy(c);
		MethodProxy method = getKeysMethod(cp);
		if (method == null) return null;
		return ((AVirtualMethod) method).getMethod();
		/*
		//Object[] params = {Integer.TYPE};
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isKeysMethod(methods[i])) return methods[i];
		return null;
		*/
	}	public static Vector getEnumerationGetters(ClassProxy c) {
		//Object[] params = {		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		Vector retVal = new Vector();		for (int i = 0; i < methods.length; i++) {			if (isEnumerationGetter(methods[i])) retVal.addElement(methods[i]);
		}		return retVal;	}
	public static MethodProxy getEnumerationGetter(ClassProxy c) {
		Vector v = getEnumerationGetters(c);
		if (v.size() == 0) return null;
		return (MethodProxy) v.elementAt(0);			}	public static MethodProxy getGetIntMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isGetIntMethod(methods[i], name)) return methods[i];		return null;			}
	public static MethodProxy getGetIntMethod(ClassDescriptorInterface c, String name) {
		//Method[] methods = c.getMethods();
		PropertyDescriptorProxy pd = c.getPropertyDescriptor(name);
		
		if (pd != null && pd.getPropertyType() == c.getRealClass().integerType())
			return pd.getReadMethod();
		else
			return null;	
	}
	public static MethodProxy getGetStringMethod(ClassDescriptorInterface c, String name) {
		//Method[] methods = c.getMethods();
		PropertyDescriptorProxy pd = c.getPropertyDescriptor(name);
		
		if (pd != null && pd.getPropertyType() == c.getRealClass().stringClass())
			return pd.getReadMethod();
		else
			return null;	
	}
	public static MethodProxy getGetOrIsBooleanMethod(ClassDescriptorInterface c, String name) {
		PropertyDescriptorProxy pd = c.getPropertyDescriptor(name);
		
		if (pd != null && pd.getPropertyType() == c.getRealClass().booleanType())
			return pd.getReadMethod();
		else
			return null;	
		
	}
	public static MethodProxy getSetIntMethod(ClassDescriptorInterface c, String name) {
		//Method[] methods = c.getMethods();
		PropertyDescriptorProxy pd = c.getPropertyDescriptor(name);
		if (pd != null && pd.getPropertyType() == c.getRealClass().integerType())
			return pd.getWriteMethod();
		else
			return null;	
	}
	public static MethodProxy getSetStringMethod(ClassDescriptorInterface c, String name) {
		//Method[] methods = c.getMethods();
		PropertyDescriptorProxy pd = c.getPropertyDescriptor(name);
		if (pd != null && pd.getPropertyType() == c.getRealClass().stringClass())
			return pd.getWriteMethod();
		else
			return null;	
	}
	public static MethodProxy getSetBooleanMethod(ClassDescriptorInterface c, String name) {
		//Method[] methods = c.getMethods();
		PropertyDescriptorProxy pd = c.getPropertyDescriptor(name);
		if (pd != null && pd.getPropertyType() == c.getRealClass().booleanType())
			return pd.getWriteMethod();
		else
			return null;	
	}
	public static MethodProxy getGetPointMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isGetPointMethod(methods[i], name)) return methods[i];
		return null;
		
	}
	/*
	public static Method getGetMethod(Class c, String name, Class type) {
		Method[] methods = c.getMethods();		for (int i = 0; i < methods.length; i++)			if (isGetMethod(methods[i], name, type)) return methods[i];		return null;			}
	*/
	public static MethodProxy getGetMethod(ClassProxy c, String name, ClassProxy type) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isGetMethod(methods[i], name, type)) return methods[i];
		return null;
		
	}
	public static MethodProxy getGetMethod(ClassDescriptorInterface cd, String name, ClassProxy type) {
		//Method[] methods = c.getMethods();
		PropertyDescriptorProxy pd = cd.getPropertyDescriptor(name);
		if (pd != null  && pd.getPropertyType().isAssignableFrom(type)) {
			return  pd.getReadMethod();
		}		
		return null;
		
	}
	public static MethodProxy getSetMethod(ClassDescriptorInterface cd, String name, ClassProxy type) {
		//Method[] methods = c.getMethods();
		PropertyDescriptorProxy pd = cd.getPropertyDescriptor(name);
		if (pd != null && pd.getPropertyType().equals(type)) {
			return  pd.getWriteMethod();
		}		
		return null;
		
	}
	public static MethodProxy getGetOrIsBooleanMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isGetOrIsBooleanMethod(methods[i], name)) return methods[i];
		return null;
		
	}
	
	public static MethodProxy getGeneralizedGetMethod(ClassProxy c, String name, ClassProxy type) {
		//Method[] methods = c.getMethods();
		//VirtualMethod[] methods = getAllMethods(c);
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isGeneralizedGetMethod(methods[i], name, type)) return methods[i];
		return null;
		
	}
	
	/*	public static Method getGetMethod(Class c, Method setMethod) {
		 		return getGetMethod (c, getPropertyName(setMethod), setMethod.getParameterTypes()[0]);				}
	*/
	public static MethodProxy getGetMethod(ClassProxy c, MethodProxy setMethod) {
		return getGetMethod (c, getPropertyName(setMethod), setMethod.getParameterTypes()[0]);	
		
	}
	public static MethodProxy getGeneralizedGetMethod(ClassProxy c, MethodProxy setMethod) {
		int lastParameterNo = setMethod.getParameterTypes().length - 1;
		ClassProxy lastParameter = setMethod.getParameterTypes()[lastParameterNo];
		//return getGeneralizedGetMethod (c, getPropertyName(setMethod), setMethod.getParameterTypes()[0]);	
		return getGeneralizedGetMethod (c, getPropertyName(setMethod), lastParameter);	
		
	}
	public static boolean matchMethod(MethodProxy method, 								   String targetName, ClassProxy targetReturnType,								   ClassProxy[] targetParameterTypes) {
		String name = method.getName();		try {
		return (targetReturnType == null ||
				//method.getReturnType().isAssignableFrom(targetReturnType)) &&
				targetReturnType.isAssignableFrom(method.getReturnType())) &&
			   (targetName == null || method.getName().toLowerCase().equals(targetName.toLowerCase())) &&			   //(targetParameterTypes == null || (isAssignableFrom(method.getParameterTypes(), targetParameterTypes)));			   (targetParameterTypes == null || (isAssignableFrom(targetParameterTypes, method.getParameterTypes())));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean isAssignableFrom(Object[] p1Types, Object[] p2Types) {
		if (p1Types.length != p2Types.length) return false;
		for (int i = 0; i < p1Types.length; i++) {
			if (!((Class) p1Types[i]).isAssignableFrom( (Class)p2Types[i]))
				return false;
		}
		return true;		
	}
	/*	public static Method getMethod(Class c, 								   String targetName, Class targetReturnType,								   Class[] targetParameterTypes) {
		Method[] methods = c.getMethods();		for (int i = 0; i < methods.length; i++)			if (matchMethod(methods[i], targetName, targetReturnType, targetParameterTypes)) return methods[i];		return null;			}
	*/
	
	public static MethodProxy getMethod(ClassProxy c, 
			String targetName, ClassProxy targetReturnType,
			ClassProxy[] targetParameterTypes) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		return getMethod(methods, 
			 targetName,  targetReturnType,
			 targetParameterTypes);
		/*
		for (int i = 0; i < methods.length; i++)
			if (matchMethod(methods[i], targetName, targetReturnType, targetParameterTypes)) return methods[i];
		return null;
		*/
		
	}
	
	public static MethodProxy getMethod(MethodProxy[] methods, 
			String targetName, ClassProxy targetReturnType,
			ClassProxy[] targetParameterTypes) {
		for (int i = 0; i < methods.length; i++)
			if (matchMethod(methods[i], targetName, targetReturnType, targetParameterTypes)) return methods[i];
		return null;
		
	}
	public static Method getMethod(Class c, 
			String targetName, Class targetReturnType,
			Class[] targetParameterTypes) {
		//Method[] methods = c.getMethods();
		ClassProxy cProxy = AClassProxy.classProxy(c);
		ClassProxy targetProxy = AClassProxy.classProxy(targetReturnType);
		ClassProxy[] targetParameterTypesProxy = AClassProxy.classProxy(targetParameterTypes);
		MethodProxy retVal = getMethod(cProxy, targetName, targetProxy, targetParameterTypesProxy);
		if (retVal == null) return null;
		return ((AVirtualMethod) retVal).getMethod();
		//return getMethod(cProxy, targetName, targetProxy, targetParameterTypesProxy);
		/*
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (matchMethod(methods[i], targetName, targetReturnType, targetParameterTypes)) return methods[i];
		return null;
		*/
		
	}
	
	static Class[] nullParams = {};
	static Class[] objectParam = {Object.class};
	static ClassProxy[] nullProxyParams = {};
	//static ClassProxy[] objectProxyParam = {StandardProxyTypes.objectClass()};
	
	public static MethodProxy getParameterLessMethod(ClassProxy c, String methodName) {
		try {
		return c.getMethod(methodName, nullProxyParams);
		} catch (Exception e) {
			//Message.warning("Could not find method: " + methodName + " in class:" + c.getName());
			return null;
		}
	}
	
	public static MethodProxy getCloneMethod(ClassProxy c) {
		return getParameterLessMethod (c, "clone");
	}
	public static MethodProxy getSingleObjectParameterMethod(ClassProxy c, String methodName) {
		 ClassProxy[] objectProxyParam = {c.objectClass()};
		try {
		return AVirtualMethod.virtualMethod(c.getMethod(methodName, objectProxyParam));
		} catch (Exception e) {
			return null;
		}
	}
	public static MethodProxy getSingleParameterMethod(ClassProxy c, String methodName, ClassProxy paramClass) {
		try {
			ClassProxy[] params = {paramClass};
		return AVirtualMethod.virtualMethod(c.getMethod(methodName, params));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static MethodProxy getIsEmptyMethod (ClassProxy c) {
		return getParameterLessMethod(c, "isEmpty");
	}
	
	public static MethodProxy getValuesMethod(ClassProxy c) {
		return getParameterLessMethod(c, "values");
	}
	public static MethodProxy getKeySetMethod(ClassProxy c) {
		return getParameterLessMethod(c, "keySet");
	}
	public static MethodProxy getEntrySetMethod(ClassProxy c) {
		return getParameterLessMethod(c, "entrySet");
	}
	public static MethodProxy getClearMethod(ClassProxy c) {
		return getParameterLessMethod(c, "clear");
	}
	public static MethodProxy getContainsKeyMethod(ClassProxy c) {
		return getSingleObjectParameterMethod(c, "containsKey");
	}
	public static MethodProxy getContainsValueMethod(ClassProxy c) {
		return getSingleObjectParameterMethod(c, "containsValue");
	}
	public static MethodProxy getContainsMethod(ClassProxy c) {
		return getSingleObjectParameterMethod(c, "contains");
	}
	/*
	public static ClassProxy mapClass() {
		return RemoteSelector.classProxy(java.util.Map.class);
	}
	public static ClassProxy collectionClass() {
		return RemoteSelector.classProxy(java.util.Collection.class);
	}
	public static ClassProxy listClass() {
		return RemoteSelector.classProxy(java.util.List.class);
	}
	public static ClassProxy setClass() {
		return RemoteSelector.classProxy(java.util.Set.class);
	}
	
	public static ClassProxy tableClass() {
		return RemoteSelector.classProxy(JTable.class);
	}
	public static ClassProxy treeClass() {
		return RemoteSelector.classProxy(JTree.class);
	}
	*/
	public static MethodProxy getPutAllMethod (ClassProxy c) {
		return getSingleParameterMethod(c, "putAll", c.mapClass());
		 
		
	}
	
	public static MethodProxy getAddAllMethod (ClassProxy c) {
		return getSingleParameterMethod(c, "addAll", c.collectionClass());
		 
		
	}
	
	public static MethodProxy getCheckerMethod (ClassProxy c, String targetName) {
		ClassProxy[] targetParameterTypes = {};
		return getMethod (c, targetName, c.booleanType(), targetParameterTypes);
		
	}
	/*	public static Method getMethod(Object o, 								   String targetName, Class targetReturnType,								   Class[] targetParameterTypes) {
		if (o == null) return null;		return getMethod (o.getClass(), targetName, targetReturnType, targetParameterTypes);	}
	*/
	public static MethodProxy getMethod(Object o, 
			String targetName, ClassProxy targetReturnType,
			ClassProxy[] targetParameterTypes) {
		if (o == null) return null;
		return getMethod (RemoteSelector.getClass(o), targetName, targetReturnType, targetParameterTypes);
	}
	public static ClassProxy executedCommandListenerClass() {
		return RemoteSelector.classProxy(bus.uigen.undo.ExecutedCommandListener.class);
	}		public static MethodProxy getAddExecutedCommandListener(Object o, MethodProxy m) {
		ClassProxy[] targetParameterTypes = {executedCommandListenerClass()};
		return getMethod(o, "add" + m.getName() + "CommandListener", null, targetParameterTypes);	}	public static MethodProxy getAddExecutedCommandListener(Object o) {
		ClassProxy[] targetParameterTypes = {executedCommandListenerClass()};
		return getMethod(o, "addExecutedCommandListener", null, targetParameterTypes);	}
	public static boolean isAddExecutedCommandListener(MethodProxy m) {		ClassProxy params[] = {executedCommandListenerClass()};		return m.getName().equals("addExecutedCommandListener") &&
			   bus.uigen.undo.Util.equal(m.getParameterTypes(), params);	
	}	public static MethodProxy getSetIntMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isSetIntMethod(methods[i], name)) return methods[i];		return null;			}
	public static MethodProxy getSetPointMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isSetPointMethod(methods[i], name)) return methods[i];
		return null;
		
	}	public static MethodProxy getSetMethod(ClassProxy c, String name, ClassProxy type) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isSetMethod(methods[i], name, type)) return methods[i];		return null;			}
	public static MethodProxy getSetBooleanMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isSetBooleanMethod(methods[i], name)) return methods[i];
		return null;
		
	}
	public static MethodProxy getGetStringMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isGetStringMethod(methods[i], name)) return methods[i];		return null;			}	public static MethodProxy getSetStringMethod(ClassProxy c, String name) {
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);		for (int i = 0; i < methods.length; i++)			if (isSetStringMethod(methods[i], name)) return methods[i];		return null;			}
	public static MethodProxy  getTreeGetChildAtMethod (ClassProxy c) {
		ClassProxy[] args = {c.integerType()};
		return OriginalUIBean.getMethod(c, "getChildAt", null, args);
	}
	public static MethodProxy  getTreeGetChildCountMethod (ClassProxy c) { return OriginalUIBean.getGetIntMethod(c, "ChildCount");}
	public static MethodProxy getTreeIsLeafMethod (ClassProxy c) { return OriginalUIBean.getCheckerMethod(c, "isLeaf");}
	public static MethodProxy getTreeInsertMethod (ClassProxy c) {
		  ClassProxy[] insertArgs = {c.objectClass(), c.integerType()};
		  return OriginalUIBean.getMethod(c, "insert", c.voidType(), insertArgs);
	}
	public static MethodProxy getTreeRemoveMethod (ClassProxy c) {		  
		  ClassProxy[] removeArgs = {c.integerType()};
		  return OriginalUIBean.getMethod(c, "remove", c.voidType(), removeArgs);
	}
		
	public static MethodProxy getTreeGetUserObjectMethod (ClassProxy c) {	
		ClassProxy[] nullArgs = {};
		MethodProxy m = OriginalUIBean.getMethod(c, "getUserObject", null, nullArgs);
		if (m != null && m.getReturnType() != c.voidType()) return m;
		return OriginalUIBean.getMethod(c, "getSelfObject", null, nullArgs);
	}
	
	public static MethodProxy getExpansionObjectMethod (ClassProxy c) {
		ClassProxy[] nullArgs = {};
		MethodProxy m = OriginalUIBean.getMethod(c, "getExpansionObject", null, nullArgs);
		if (m != null && m.getReturnType() != c.voidType()) return m;
		return null;
		//return uiBean.getMethod(c, "getSelfObject", null, nullArgs);
	}
	
	
	public static  MethodProxy getTreeSetUserObjectMethod (ClassProxy c) {
		  ClassProxy[] userObjectArgs = {c.objectClass()};
		  MethodProxy m = OriginalUIBean.getMethod(c, "setUserObject", c.voidType(), userObjectArgs);
		  if (m != null) return m;
		  return OriginalUIBean.getMethod(c, "setSelfObject", c.voidType(), userObjectArgs);
	}
	
	public static MethodProxy  getTableGetColumnCountMethod (ClassProxy c) {
		return OriginalUIBean.getGetIntMethod(c, "ColumnCount");
	}
	public static MethodProxy  getTableGetRowCountMethod (ClassProxy c) {
		return OriginalUIBean.getGetIntMethod(c, "RowCount");
	}
	public static MethodProxy  getTableGetColumnNameMethod (ClassProxy c) {
		ClassProxy[] args = {c.integerType()};
		return OriginalUIBean.getMethod(c, "getColumnName", c.stringClass(), args);
	}
	
	public static MethodProxy  getTableGetValueAtMethod (ClassProxy c) {
		ClassProxy[] args = {c.integerType(), c.integerType()};
		return OriginalUIBean.getMethod(c, "getValueAt", null, args);
	}
	public static MethodProxy  getTableSetValueAtMethod (ClassProxy c) {
		ClassProxy[] args = {c.objectClass(), c.integerType(), c.integerType()};
		return OriginalUIBean.getMethod(c, "setValueAt", c.voidType(), args);
	}
	public static MethodProxy  getTableIsCellEditableMethod (ClassProxy c) {
		ClassProxy[] args = {c.integerType(), c.integerType()};
		return OriginalUIBean.getMethod(c, "isCellEditable", c.booleanType(), args);
	}
	
	public static MethodProxy getIsIndexedChildEditableMethod (ClassProxy c) {
		ClassProxy[] args = {c.integerType()};
		return OriginalUIBean.getMethod(c, "isEditable", c.booleanType(), args);
	}
	public static MethodProxy getIsEditableMethod (ClassProxy c) {
		ClassProxy[] args = {};
		return OriginalUIBean.getMethod(c, "isEditable", c.booleanType(), args);
	}
	
	public static MethodProxy getIsEditableMethod (RemoteClassProxy c) {
		ClassProxy[] args = {};
		return OriginalUIBean.getMethod(c, "isEditable", c.booleanType(), args);
	}
	
	public static MethodProxy getIsPropertyEditableMethod (ClassProxy c, String property) {
		ClassProxy[] args = {};
		return OriginalUIBean.getMethod(c, "isEditable" + property, c.booleanType(), args);
	}
	
	
	
	static String[] excludeClasses = {"bus.uigen.AMutableString", "bus.uigen.AListenableString", "budget.Test"};
	static Vector excludeVectorClasses = uiGenerator.arrayToVector(excludeClasses);	public static boolean isVector(ClassProxy c) {
		//System.out.println("checking for vector" + c.getName());						if (c == vectorClass()) return true;		if (excludeVectorClasses.contains(c.getName())) return false;		//System.out.println("
		//Object[] params = {		return getElementsMethod(c) != null ||
			   (getSizeMethod(c) != null && getElementAtMethod(c) != null);	}	public static boolean isHashtable(ClassProxy c) {
		//Object[] params = {		return ((getKeysMethod(c) != null) &&
				(getElementsMethod(c) != null || getGetMethod(c) != null));	}
	public static boolean hasXYLocation(ClassProxy c) {
		//Object[] params = {		return ((getGetIntMethod(c, "X") != null) &&
				(getGetIntMethod(c, "Y") != null) );	}
	public static boolean hasXYLocation(ClassDescriptorInterface c) {
		//Object[] params = {
		return ((getGetIntMethod(c, "X") != null) &&
				(getGetIntMethod(c, "Y") != null) );
	}
	public static boolean isPredefinedClass(ClassProxy c) {
		return c.getName().startsWith("java");
	}
	public static boolean hasLocation(ClassProxy c) {
		util.annotations.IsAtomicShape isAtomicShape = (util.annotations.IsAtomicShape) c.getAnnotation(util.annotations.IsAtomicShape.class);
		if (isAtomicShape != null && !isAtomicShape.value()) return false;
		boolean retVal = hasXYLocation(c) || hasPointLocation(c);
		
		return retVal;
		//return hasXYLocation(c) || hasPointLocation(c);
	}
	public static boolean hasLocation(ClassDescriptorInterface cd) {
		ClassProxy c = cd.getRealClass();
		util.annotations.IsAtomicShape isAtomicShape = (util.annotations.IsAtomicShape) c.getAnnotation(util.annotations.IsAtomicShape.class);
		if (isAtomicShape != null && !isAtomicShape.value()) return false;
		boolean retVal = hasXYLocation(cd) || hasPointLocation(c);
		
		return retVal;
		//return hasXYLocation(c) || hasPointLocation(c);
	}
	public static boolean hasPointLocation(ClassProxy c) {
		//Object[] params = {
		return ((getGetPointMethod(c, "Location") != null) );
	}
	
	public static boolean hasBounds(ClassProxy c) {
		//Object[] params = {
		boolean retVal = ((getGetIntMethod(c, "Width") != null) &&
				(getGetIntMethod(c, "Height") != null) );
		if (!retVal && !isPredefinedClass(c)) {
			if (!warnedAboutCoordinates.contains(c)) {
				Tracer.warning(c.getName() + " has X and Y coordinates and its name indicates an atomic shape. If it is indeed an atomic shape it should have int getters for both width and height");
			warnedAboutCoordinates.add(c);
			}
		}
		return retVal;
			}
	public static boolean hasBounds(ClassDescriptorInterface cd) {
		//Object[] params = {
		boolean retVal = ((getGetIntMethod(cd, "Width") != null) &&
				(getGetIntMethod(cd, "Height") != null) );
		ClassProxy c= cd.getRealClass();
		if (!retVal && !isPredefinedClass(c)) {
			if (!warnedAboutCoordinates.contains(c)) {
				Tracer.warning(c.getName() + " has X and Y coordinates and its name indicates an atomic shape. If it is indeed an atomic shape it should have int getters for both width and height");
			warnedAboutCoordinates.add(c);
			}
		}
		return retVal;
		
	}
	public static boolean hasAngle(ClassProxy c) {
		boolean retVal = ((getGetIntMethod(c, "StartAngle") != null) &&
				(getGetIntMethod(c, "EndAngle") != null) );
		if (!retVal && !isPredefinedClass(c))
			Tracer.warning("Class: " + c.getName() + " should have int getters for both startAngle and endAngle");
		return retVal;
		
	}
	public static boolean hasAngle(ClassDescriptorInterface cd) {
		ClassProxy c = cd.getRealClass();
		boolean retVal = ((getGetIntMethod(cd, "StartAngle") != null) &&
				(getGetIntMethod(cd, "EndAngle") != null) );
		if (!retVal && !isPredefinedClass(c))
			Tracer.warning("Class: " + c.getName() + " should have int getters for both startAngle and endAngle");
		return retVal;
		
	}
	public static boolean hasControlXY(ClassProxy c) {
		boolean retVal = (((getGetIntMethod(c, "ControlX") != null) &&
				(getGetIntMethod(c, "ControlY") != null) ))
				|| (((getGetIntMethod(c, "ControlX1") != null) &&
						(getGetIntMethod(c, "ControlY1") != null) ));
		if (!retVal && !isPredefinedClass(c))
			Tracer.warning("Class: " + c.getName() + " should have int getters for both controlX and controlY");
		return retVal;
		
	}
	public static boolean hasControlXY(ClassDescriptorInterface c) {
		boolean retVal = (((getGetIntMethod(c, "ControlX") != null) &&
				(getGetIntMethod(c, "ControlY") != null) ))
				|| (((getGetIntMethod(c, "ControlX1") != null) &&
						(getGetIntMethod(c, "ControlY1") != null) ));
		if (!retVal && !isPredefinedClass(c.getRealClass()))
			Tracer.warning("Class: " + c.getRealClass().getName() + " should have int getters for both controlX and controlY");
		return retVal;
		
	}
	public static boolean hasText(ClassProxy c) {
		//Object[] params = {		return getGetStringMethod(c, "Text") != null;	}
	public static boolean hasText(ClassDescriptorInterface c) {
		//Object[] params = {
		return getGetStringMethod(c, "Text") != null;
	}
	public static boolean hasImageFileName(ClassProxy c) {
		//Object[] params = {
		return getGetStringMethod(c, "ImageFileName") != null || 
		getGetStringMethod(c, "ImageFile") != null 	||
		getGetStringMethod(c, "Icon") != null;
	}
	public static boolean hasImageFileName(ClassDescriptorInterface c) {
		//Object[] params = {
		return getGetStringMethod(c, "ImageFileName") != null || 
		getGetStringMethod(c, "ImageFile") != null 	||
		getGetStringMethod(c, "Icon") != null;
	}	public static void addNew (Vector v, Object o) {
		if (v.contains(o))
			return;
		else
			v.addElement(o);	}	public static void addTypes (Vector v, ClassProxy c) {		
		if ((c == null) || (c == c.objectClass()) || (v.contains(c)))
			return;		
		v.addElement(c);		//addNew(v, c);
		addInterfaces(v, c);		ClassProxy superClass = c.getSuperclass();
		addTypes(v, superClass);	}	public static void addInterfaces (Vector v, ClassProxy c) {		
		if (c.isInterface())
			return;				ClassProxy[] interfaces = c.getInterfaces();		for (int i=0; i<interfaces.length; i++) {
			addTypes(v, interfaces[i]);		}	}
	public static Vector getTypes (ClassProxy c) {
		Vector v = new Vector();		addTypes(v, c);			return v;
	}
	public static boolean contains(String s, String subString) {		int index = s.lastIndexOf(subString);		if (index >= 0 && index <= s.length())			return true;		else			return false;
	}	public static boolean contains(ClassProxy c, String name) {
		/*
		Vector v = getTypes(c);
		for (int i = 0; i < v.size(); i++)
			if ( contains (((ClassProxy) v.elementAt(i)).getName(), name))
				return true;
		return false;
		*/
		boolean retVal = containsDeep(c, name);
		if (retVal)
			return true;
		/*
		util.IsAtomicShape isAtomicShape = (util.IsAtomicShape) c.getAnnotation(util.IsAtomicShape.class);
		if (isAtomicShape == null || 
				!isAtomicShape.value()) // it should not reach here is this is true
			return false; 
			*/		
		//Message.warning("The name of "+ c.getName() + " or one of its super types should contain the words Line, Rectangle, Point, or Oval");
		
		return false;	}
	public static boolean containsDeep(ClassProxy c, String name) {
		Vector v = getTypes(c);
		for (int i = 0; i < v.size(); i++)
			if ( contains (((ClassProxy) v.elementAt(i)).getName(), name))
				return true;
		return false;
	}
	public static boolean containsShallow(ClassProxy c, String name) {
		return contains (c.getName(), name);
	}
	
	
	
	public  static boolean isShapeModel (ClassProxy c) {
		try {	      //return Class.forName("shapes.RemoteShape").isAssignableFrom(c);
			return remoteShapeClass().isAssignableFrom(c) || 
				   remoteLineClass().isAssignableFrom(c) 
				   || remoteOvalClass().isAssignableFrom(c)
				   || remotePointClass().isAssignableFrom(c) // added this
		           || remoteTextClass().isAssignableFrom(c)
				   ;
				   
		} catch (Exception e) {
			return false;
		}	}
	public  static boolean isLineModel (ClassProxy c) {
		try {
	      //return Class.forName("shapes.RemoteShape").isAssignableFrom(c);
			return 
				   remoteLineClass().isAssignableFrom(c) ; 
				   
				   
		} catch (Exception e) {
			return false;
		}
	}
	public  static boolean isRectangleModel (ClassProxy c) {
		try {
	      //return Class.forName("shapes.RemoteShape").isAssignableFrom(c);
			return 
				   remoteRectangleClass().isAssignableFrom(c) ; 
				   
				   
		} catch (Exception e) {
			return false;
		}
	}
	public  static boolean isOvalModel (ClassProxy c) {
		try {
	      //return Class.forName("shapes.RemoteShape").isAssignableFrom(c);
			return 
				   remoteOvalClass().isAssignableFrom(c) ; 
				   
				   
		} catch (Exception e) {
			return false;
		}
	}
	public  static boolean isPointModel (ClassProxy c) {
		try {
	      //return Class.forName("shapes.RemoteShape").isAssignableFrom(c);
			return 
				   remotePointClass().isAssignableFrom(c) ; 
				   
				   
		} catch (Exception e) {
			return false;
		}
	}
	public  static boolean isTextModel (ClassProxy c) {
		try {
	      //return Class.forName("shapes.RemoteShape").isAssignableFrom(c);
			return 
				   remoteTextClass().isAssignableFrom(c) ; 
				   
				   
		} catch (Exception e) {
			return false;
		}
	}
	
		public static boolean isPoint(ClassProxy c) {		
		//Object[] params = {		return 
		isPointModel(c) || (
		//!isShapeModel(c) && 
		   hasLocation(c) && (contains(c, "Point")));	}	public static boolean isRectangle(ClassProxy c) {		
		//Object[] params = {		return 
		       isRectangleModel(c) || (
		       //!isShapeModel (c) && 
		       hasLocation(c) && 
			   hasBounds(c) &&
			   (contains(c, "Rectangle")|| contains(c, "Square")));	}
		public static boolean isOval(ClassProxy c) {		
		//Object[] params = {		return //!isShapeModel(c) && 
			   isOvalModel(c) || (
		       hasLocation(c) && 
			   hasBounds(c) &&
			   (contains(c, "Oval") || contains (c, "Ellipse")) || contains(c, "Circle"));	}	public static boolean isLine(ClassProxy c) {		
		//Object[] params = {		return 
			   //!isShapeModel(c) && 
			   isLineModel(c) || (
		       hasLocation(c) && 
			   hasBounds(c) &&
			   contains(c, "Line"));	}	
	public static boolean isTextBox(ClassProxy c) {		
		//Object[] params = {		return 
			   //!isShapeModel(c) && 
		       hasLocation(c) && 
			   hasBounds(c) &&
			   hasText(c) &&
			   contains(c, "Text");	}	public static final int NO_SHAPE = 0;
	public static final int POINT_SHAPE = 1;
	public static final int RECTANGLE_SHAPE = 2;
	public static final int OVAL_SHAPE = 3;
	public static final int LINE_SHAPE = 4;
	public static final int TEXT_SHAPE = 5;
	public static final int LABEL_SHAPE = 6;
	public static final int ARC_SHAPE = 7;
	public static final int CURVE_SHAPE = 8;
	
	public static boolean isShape(ClassProxy c) {		return whichShape(c) != NO_SHAPE;
	}	
	public static int whichShape(ClassProxy c) {
		if (c == pointModelClass()) return POINT_SHAPE;
		else if (c == lineModelClass()) return LINE_SHAPE;
		else if (c == rectangleModelClass()) return RECTANGLE_SHAPE;
		else if (c == ovalModelClass()) return OVAL_SHAPE;
		else if (c == textModelClass()) return TEXT_SHAPE;
		else if (c == labelModelClass()) return LABEL_SHAPE;
		else if (c == arcModelClass()) return ARC_SHAPE;
		else if (c == curveModelClass()) return CURVE_SHAPE;		if (isPointType(c)) return POINT_SHAPE;

		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		//if (/*isShapeModel(c) ||*/ !hasLocation(c)) return NO_SHAPE;
		//if (contains(c, "Point")) return POINT_SHAPE;				//if (!hasBounds(c)) return NO_SHAPE;		if ((contains(c, "Rectangle") || contains(c, "Oblong") || contains(c, "Square")) && hasLocation(c) && hasBounds(cd)) return RECTANGLE_SHAPE;
		if ((contains(c, "Line") || contains(c, "Segment"))&& hasLocation(c) && hasBounds(c))  return LINE_SHAPE;
		if ((contains(c, "Oval") || contains (c, "Ellipse") || contains (c, "Circle")) && hasLocation(cd) && hasBounds(cd)) return OVAL_SHAPE;
		if ( contains(c, "Text") && hasLocation(cd) && hasBounds(c) &&  hasText(c) ) return TEXT_SHAPE;
		if ( (contains(c, "Label") || contains(c, "Icon") || contains (c, "Image")) && hasLocation(cd) && hasBounds(cd) && (hasText(cd) || hasImageFileName(cd)))  return LABEL_SHAPE;
		if ( contains(c, "Arc") && hasLocation(cd) && hasBounds(cd) &&  hasAngle(cd) ) return ARC_SHAPE;
		if ( contains(c, "Curve") && hasLocation(cd) && hasBounds(cd) &&  hasControlXY(cd) ) return CURVE_SHAPE;
		if (hasXYLocation(cd)) {
			if (!warnedAboutCoordinates.contains(c)) {				Tracer.warning(c.getName() + " has X and Y coordinates. If it defines an atomic geometric object, the name of "+ c.getName() + " or one of its super types should contain the words Arc, Curve, Line, Rectangle, Point, or Oval");
			warnedAboutCoordinates.add(c);
			}
		}
		return NO_SHAPE;
	}
	static Set warnedAboutCoordinates = new HashSet();
	static Set pointTypes = new HashSet();
	public static boolean isPointType (ClassProxy c) {
		//if (/*isShapeModel(c) ||*/ !hasLocation(c)) return false;
		if (c == pointModelClass()) return true;
		if (c == pointClass()) return true;
		if (contains(c, "Point") && hasXYLocation(c)) return true;
		//if (hasXYLocation(c) && contains(c, "Point")) return true;
		return false;
	}
	
	
	public static boolean  isVector(Object o) {		if (o == null) return false;		return isVector(RemoteSelector.getClass(o));
			}	public static boolean  isHashtable(Object o) {		if (o == null) return false;		return isHashtable(RemoteSelector.getClass(o));
			}	public static boolean  isPoint(Object o) {		if (o == null) return false;		return isPoint(o.getClass());
			}	public static boolean  isRectangle(Object o) {		if (o == null) return false;		return isRectangle(RemoteSelector.getClass(o));
			}	public static boolean  isOval(Object o) {		if (o == null) return false;		return isOval(RemoteSelector.getClass(o));
			}	public static boolean  isLine(Object o) {		if (o == null) return false;		return isLine(RemoteSelector.getClass(o));
			}	
	public static void addElements(Vector vector, Method elements, Object object) {			try {
			Object[] params = {};
			if (elements == null) return;					Enumeration enumeration = (Enumeration) elements.invoke(object, params);
			while (enumeration.hasMoreElements()) {
				vector.addElement(enumeration.nextElement());			}		} catch (Exception e) {};
	}
	
	public static Vector toClassVector(Object object) 	{
		if (RemoteSelector.getClass(object).equals(vectorClass()))
			return (Vector) object;
		return copyToVector(object);
	}
	public static Vector toVector(Object object) 	{
		if (object instanceof Vector)
			return (Vector) object;
		return copyToVector(object);
	}	public static Vector copyToVector(Object object) 	{		if (object == null) return null;
		//if (object instanceof Vector && !(object instanceof AListenableVector))
		/*
		if (object instanceof Vector)
			return (Vector) object;
			*/		MethodProxy size = null;		MethodProxy elementAt = null;		MethodProxy elements = null;
		//VirtualMethod[] methods = object.getClass().getMethods();
		MethodProxy[] methods = getMethods(RemoteSelector.getClass(object));
		Object[] params = {};
		Vector vector = new Vector();		for (int i = 0; i < methods.length; i++)			if (isElementAtMethod(methods[i]) || isGetMethod(methods[i])) 				elementAt = methods[i];			else if (isSizeMethod(methods[i]))				size = methods[i];			else if (isElementsMethod(methods[i]))				elements = methods[i];
		try {
						if (elements != null) {
								Enumeration enumeration = (Enumeration) elements.invoke(object, params);
				while (enumeration.hasMoreElements())
					vector.addElement(enumeration.nextElement());
				return vector;	
								
			} else if (size != null && elementAt != null) {				int vectorSize = ((Integer) size.invoke(object, params)).intValue();				for (int i = 0; i < vectorSize; i++) {			   
					Object[] params2 = {new Integer(i)};					vector.addElement(elementAt.invoke(object, params2)); 				}				return vector;
			} else return null;
		} catch (Exception e) {			return null;
		}	}
	public static Hashtable toHashtable(HashMap object) 	{
		return new Hashtable(object);
		
	}
	public static Hashtable toHashtable(Hashtable object) 	{
		return new Hashtable(object);
		
	}
	public static Hashtable toHashtable(Object object) 	{
		
		if (object instanceof Hashtable)
			return toHashtable((Hashtable) object);	
		else if (object instanceof HashMap) 
			return toHashtable((HashMap) object);						MethodProxy keysMethod = null;		MethodProxy getMethod = null;		MethodProxy elementsMethod = null;
		MethodProxy[] methods = getMethods(RemoteSelector.getClass(object));
		Object[] params = {};
		Hashtable hashtable = new Hashtable();		for (int i = 0; i < methods.length; i++)			if (isKeysMethod(methods[i])) 				keysMethod = methods[i];			else if (isGetMethod(methods[i]))				getMethod = methods[i];			else if (isElementsMethod(methods[i]))				elementsMethod = methods[i];
		try {
						if (keysMethod != null) {								Enumeration keys = (Enumeration) keysMethod.invoke(object, params);				if (elementsMethod != null) {					Enumeration elements = (Enumeration) elementsMethod.invoke(object, params);							   
					while (keys.hasMoreElements() && elements.hasMoreElements())
						hashtable.put(keys.nextElement(), elements.nextElement());
					return hashtable;				} else if (getMethod != null) {
					while (keys.hasMoreElements()) {						Object nextKey = keys.nextElement();
						Object[] params2 = {nextKey};						hashtable.put(nextKey, getMethod.invoke(object, params2));
					}					return hashtable;				} else return null;
			} else return null;
		} catch (Exception e) {			return null;
		}	}	public static PointModel toPointModel(Object object, MethodProxy getXMethod, MethodProxy getYMethod) 	{		if (object == null) return null;
		if (object instanceof PointModel)
			return (PointModel) object;
		ClassProxy c = RemoteSelector.getClass(object);

		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);		//Method getXMethod = getGetIntMethod(c,"X");		//Method getYMethod = getGetIntMethod(c,"Y");
		getXMethod = getGetIntMethod(cd,"X");		getYMethod = getGetIntMethod(cd,"Y");		if (getXMethod == null || getYMethod == null)			return null;
		try {
		    Object[] params = {};			int x = ((Integer) getXMethod.invoke(object, params)).intValue();			int y = ((Integer) getYMethod.invoke(object, params)).intValue();			return new PointModel(x, y);		    
		} catch (Exception e) {			return null;
		}	}	public static PointModel toPointModel(Object object) 	{
		MethodProxy getXMethod = null, getYMethod = null;
		return toPointModel(object, getXMethod, getYMethod);	}
	public static void fillBounds(Object object, RemoteShape s) 	{		
		ClassProxy c = RemoteSelector.getClass(object);

		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);		MethodProxy getXMethod = getGetIntMethod(cd,"X");		MethodProxy getYMethod = getGetIntMethod(cd,"Y");
		MethodProxy getWidthMethod = getGetIntMethod(cd,"Width");		MethodProxy getHeightMethod = getGetIntMethod(cd,"Height");		if (getXMethod == null || getYMethod == null || getWidthMethod == null || getHeightMethod == null)			return;
		try {
		    Object[] params = {};			int x = ((Integer) getXMethod.invoke(object, params)).intValue();			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
			int width = ((Integer) getWidthMethod.invoke(object, params)).intValue();			int height = ((Integer) getHeightMethod.invoke(object, params)).intValue();			s.setBounds (x, y, width, height);		    
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}	}	public static RectangleModel toRectangleModel(Object object) 	{
		try {		if (object == null) return null;
		if (object instanceof RectangleModel)
			return (RectangleModel) object;		RectangleModel rectangleModel = new RectangleModel();
		fillBounds(object, rectangleModel);
		return rectangleModel;		/*
		Class c = object.getClass();		Method getXMethod = getGetIntMethod(c,"X");		Method getYMethod = getGetIntMethod(c,"Y");
		Method getWidthMethod = getGetIntMethod(c,"Width");		Method getHeightMethod = getGetIntMethod(c,"Height");		if (getXMethod == null || getYMethod == null || getWidthMethod == null || getHeightMethod == null)			return null;
		try {
		    Object[] params = {};			int x = ((Integer) getXMethod.invoke(object, params)).intValue();			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
			int width = ((Integer) getWidthMethod.invoke(object, params)).intValue();			int height = ((Integer) getHeightMethod.invoke(object, params)).intValue();			return new RectangleModel(x, y, width, height);		    
		} catch (Exception e) {			return null;
		}
		*/
		} catch (Exception e) {
			
			System.out.println(e);
			return null;
		}	}
		public static LineModel toLineModel(Object object) 	{
		try {		if (object == null) return null;
		if (object instanceof LineModel)
			return (LineModel) object;		LineModel lineModel = new LineModel();
		fillBounds(object, lineModel);
		return lineModel;		
		/*		Method getXMethod = getGetIntMethod(c,"X");		Method getYMethod = getGetIntMethod(c,"Y");
		Method getWidthMethod = getGetIntMethod(c,"Width");		Method getHeightMethod = getGetIntMethod(c,"Height");		if (getXMethod == null || getYMethod == null || getWidthMethod == null || getHeightMethod == null)			return null;
		try {
		    Object[] params = {};			int x = ((Integer) getXMethod.invoke(object, params)).intValue();			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
			int width = ((Integer) getWidthMethod.invoke(object, params)).intValue();			int height = ((Integer) getHeightMethod.invoke(object, params)).intValue();			return new LineModel(x, y, width, height);		    
		} catch (Exception e) {			return null;
		}
		*/
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}	}	public static OvalModel toOvalModel(Object object) 	{
		try {		if (object == null) return null;
		if (object instanceof OvalModel)
			return (OvalModel) object;				OvalModel ovalModel = new OvalModel();
		fillBounds(object, ovalModel);
		return ovalModel;		/*
		Class c = object.getClass();		Method getXMethod = getGetIntMethod(c,"X");		Method getYMethod = getGetIntMethod(c,"Y");
		Method getWidthMethod = getGetIntMethod(c,"Width");		Method getHeightMethod = getGetIntMethod(c,"Height");		if (getXMethod == null || getYMethod == null || getWidthMethod == null || getHeightMethod == null)			return null;
		try {
		    Object[] params = {};			int x = ((Integer) getXMethod.invoke(object, params)).intValue();			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
			int width = ((Integer) getWidthMethod.invoke(object, params)).intValue();			int height = ((Integer) getHeightMethod.invoke(object, params)).intValue();			return new OvalModel(x, y, width, height);		    
		} catch (Exception e) {			return null;
		}
		*/
	} catch (Exception e) {
		System.out.println(e);
		return null;
	}	}	public static TextModel toTextModel(Object object) 	{		if (object == null) return null;
		if (object instanceof TextModel)
			return (TextModel) object;		ClassProxy c = RemoteSelector.getClass(object);

		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		MethodProxy getTextMethod = getGetStringMethod(cd,"Text");
		TextModel textModel;		try {
		    Object[] params = {};			
			String text = (String) getTextMethod.invoke(object, params);
			textModel = new TextModel(text);			fillBounds(object, textModel);
		} catch (Exception e) {			return null;
		}
		fillBounds(object, textModel);
		return textModel;		/*
		Class c = object.getClass();		Method getXMethod = getGetIntMethod(c,"X");		Method getYMethod = getGetIntMethod(c,"Y");
		Method getWidthMethod = getGetIntMethod(c,"Width");		Method getHeightMethod = getGetIntMethod(c,"Height");
		Method getTextMethod = getGetStringMethod(c,"Text");		if (getXMethod == null || getYMethod == null || getWidthMethod == null || getHeightMethod == null)			return null;
		try {
		    Object[] params = {};			int x = ((Integer) getXMethod.invoke(object, params)).intValue();			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
			int width = ((Integer) getWidthMethod.invoke(object, params)).intValue();			int height = ((Integer) getHeightMethod.invoke(object, params)).intValue();
			String text = (String) getTextMethod.invoke(object, params);
			TextModel textModel = new TextModel(text);
			textModel.setBounds(x,y,width,height);			return textModel;		    
		} catch (Exception e) {			return null;
		}
		*/	}
	public static ArcModel toArcModel(Object object) 	{
		if (object == null) return null;
		if (object instanceof ArcModel)
			return (ArcModel) object;
		ClassProxy c = RemoteSelector.getClass(object);
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		MethodProxy getStartAngleMethod = getGetIntMethod(cd,"StartAngle");
		MethodProxy getEndAngleMethod = getGetIntMethod(cd,"EndAngle");
		ArcModel arcModel;
		try {
		    Object[] params = {};			
			int startAngle = (Integer) getStartAngleMethod.invoke(object, params);
			int endAngle = (Integer) getEndAngleMethod.invoke(object, params);
			
			arcModel = new ArcModel(startAngle, endAngle);
			fillBounds(object, arcModel);
		} catch (Exception e) {
			return null;
		}
		
		return arcModel;
		
	}
	public static CurveModel toCurveModel(Object object) 	{
		if (object == null) return null;
		if (object instanceof CurveModel)
			return (CurveModel) object;
		ClassProxy c = RemoteSelector.getClass(object);

		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		MethodProxy getControlXMethod = getGetIntMethod(cd,"ControlX");
		if (getControlXMethod == null)
			getControlXMethod = getGetIntMethod(cd,"ControlX1");
		MethodProxy getControlYMethod = getGetIntMethod(cd,"ControlY");
		if (getControlYMethod == null)
			getControlYMethod = getGetIntMethod(cd,"ControlY1");
		MethodProxy getControlX2Method = getGetIntMethod(cd,"ControlX2");
		MethodProxy getControlY2Method = getGetIntMethod(cd,"ControlY2");
		CurveModel curveModel;
		try {
		    Object[] params = {};			
			int controlX = (Integer) getControlXMethod.invoke(object, params);
			int controlY = (Integer) getControlYMethod.invoke(object, params);
			
			curveModel = new CurveModel(controlX, controlY);
			
			if (getControlX2Method != null && getControlY2Method != null) {
				int controlX2 = (Integer) getControlX2Method.invoke(object, params);
				int controlY2 = (Integer) getControlY2Method.invoke(object, params);
				curveModel.setControlX2(controlX2);
				curveModel.setControlY2(controlY2);
			}
			fillBounds(object, curveModel);
		} catch (Exception e) {
			return null;
		}
		
		return curveModel;
		
	}	//public static ShapeModel toShapeModel(Object object) 	{
	public static RemoteShape toShapeModel(Object object) 	{
		//if (object instanceof ShapeModel)
		if (object instanceof RemoteShape)
			return (RemoteShape) object;		int whichShape = whichShape(RemoteSelector.getClass(object));		switch (whichShape) {
			case NO_SHAPE: return null;
			case POINT_SHAPE: return toPointModel(object);
			case RECTANGLE_SHAPE: return toRectangleModel(object);			case OVAL_SHAPE: return toOvalModel(object);			case LINE_SHAPE: return toLineModel(object);			case TEXT_SHAPE: return toTextModel(object);
			case ARC_SHAPE: return toArcModel(object);
			case CURVE_SHAPE: return toCurveModel(object);
			//case LABEL_SHAPE: return toLabelModel(object);			default:   return null;		}			  	}	public static RemoteShape toShapeModel(Object object, int whichShape) 	{
		if (object instanceof RemoteShape)
			return (RemoteShape) object;		switch (whichShape) {
			case NO_SHAPE: return null;
			case POINT_SHAPE: return toPointModel(object);
			case RECTANGLE_SHAPE: return toRectangleModel(object);			case OVAL_SHAPE: return toOvalModel(object);			case LINE_SHAPE: return toLineModel(object);			case TEXT_SHAPE: return toTextModel(object);			default:   return null;		}			  	}
	static Set<MethodProxy> propertyChangeListeners = new HashSet();	public static boolean isAddPropertyChangeListenerMethod(MethodProxy method) {
		if (propertyChangeListeners.contains(method))
			return true;
		util.annotations.ObserverRegisterer observerRegister = method
				.getAnnotation(util.annotations.ObserverRegisterer.class);
		boolean annotated = false;
		boolean correctlyNamed = false;

		if (observerRegister != null
				&& observerRegister.value().equals(
						util.annotations.ObserverTypes.PROPERTY_LISTENER))
			annotated = true;
		else if (method.getName().equalsIgnoreCase("addPropertyChangeListener"))
			correctlyNamed = true;
		if (!annotated && !correctlyNamed)
			return false;

		ClassProxy[] params = method.getParameterTypes();
		if (params.length == 1
				&& propertyChangeListenerClass().isAssignableFrom(params[0])) {
			// Message.warning("Use annotation util.ObserverRegisterer + \"" +
			// util.ObserverTypes.PROPERTY_LISTENER + "\" for method " + method
			// ));
			if (!annotated) {
				Tracer.warning("Use annotation @util.annotations.ObserverRegisterer(\""
						+ util.annotations.ObserverTypes.PROPERTY_LISTENER
						+ "\") for method " + method);
			}
			propertyChangeListeners.add(method);
			return true;
		} else {
			Tracer
					.warning(method
							+ " not recognized as registerer of property change listener because it does not have a single parameter of type:"
							+ propertyChangeListenerClass());
			return false;
		}

	}
	public static boolean isPropertyChangeListenerMethod(MethodProxy method) {
		
		String name = method.getName();
		if (name.endsWith("PropertyChangeListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				propertyChangeListenerClass().isAssignableFrom(params[0])) {
				//Message.warning("Use annotation util.ObserverRegisterer + \"" + util.ObserverTypes.PROPERTY_LISTENER + "\" for method "  + method ));

				return true;
			} else
				return false;  
		}
		return false;
	}
	public static ClassProxy propertyChangeListenerClass() {
		return RemoteSelector.classProxy(java.beans.PropertyChangeListener.class);
	}
	public static ClassProxy vectorListenerClass() {
		return RemoteSelector.classProxy(VectorListener.class);
	}
	public static ClassProxy hashtableListenerClass() {
		return RemoteSelector.classProxy(HashtableListener.class);
	}
	public static boolean isAddPropertyChangeListenerMethodWorking(MethodProxy method) {
		String name = method.getName();
		if (name.equals("addPropertyChangeListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				propertyChangeListenerClass().isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	
	public static boolean isAddVectorListenerMethodWorking(MethodProxy method) {
		String name = method.getName();
		if (name.equals("addVectorListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				RemoteSelector.classProxy(VectorListener.class).isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	static Set<MethodProxy> vectorListeners = new HashSet();
	public static boolean isAddVectorListenerMethod(MethodProxy method) {
		if (vectorListeners.contains(method))
			return true;
		util.annotations.ObserverRegisterer observerRegisterer = method.getAnnotation(util.annotations.ObserverRegisterer.class);
		boolean annotated = observerRegisterer != null && observerRegisterer.value().equals(util.annotations.ObserverTypes.VECTOR_LISTENER);
		
		String name = method.getName();
		boolean correctlyNamed = false;
		if (!annotated)
			correctlyNamed = name.equals("addVectorListener");
		
			
			if (!annotated && !correctlyNamed)
				return false;

			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1
					&& vectorListenerClass().isAssignableFrom(params[0])) {
				// Message.warning("Use annotation util.ObserverRegisterer + \"" +
				// util.ObserverTypes.PROPERTY_LISTENER + "\" for method " + method
				// ));
				if (!annotated) {
					Tracer.warning("Use annotation @util.annotations.ObserverRegisterer(\""
							+ util.annotations.ObserverTypes.VECTOR_LISTENER
							+ "\") for method " + method);
				}
				vectorListeners.add(method);
				return true;
			} else {
				Tracer
						.warning(method
								+ " not recognized as registerer of property change listener because it does not have a single parameter of type:"
								+ vectorListenerClass());
				return false;
			}

			
	}
	public static boolean isAddHashtableListenerMethod(MethodProxy method) {
		String name = method.getName();
		if (name.equals("addHashtableListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				hashtableListenerClass().isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static ClassProxy remotePropertyChangeListenerClass() {
		return RemoteSelector.classProxy(util.models.RemotePropertyChangeListener.class);
	}
	public static boolean isAddRemotePropertyChangeListenerMethod(MethodProxy method) {
		String name = method.getName();
		if (name.equals("addRemotePropertyChangeListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
					remotePropertyChangeListenerClass().isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isRemotePropertyChangeListenerMethod(MethodProxy method) {
		String name = method.getName();
		if (name.endsWith("RemotePropertyChangeListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
					//used to be simply propertyChangeLis
				//propertyChangeListenerClass().isAssignableFrom(params[0]))
				remotePropertyChangeListenerClass().isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	
	public static MethodProxy getAddPropertyChangeListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		//VirtualMethod[] methods = c.getMethods();
		//MethodProxy[] methods = getMethods(c);
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isAddPropertyChangeListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static MethodProxy getAddVectorListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isAddVectorListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static MethodProxy getAddHashtableListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isAddHashtableListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static MethodProxy getAddRemotePropertyChangeListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isAddRemotePropertyChangeListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static boolean isTableModelListenerMethod(MethodProxy method) {
		String name = method.getName();
		if (name.endsWith("TableModelListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				RemoteSelector.classProxy(javax.swing.event.TableModelListener.class).isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isAddTableModelListenerMethod(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("add") && isTableModelListenerMethod(method));
	}
	public static boolean isRemoveTableModelListenerMethod(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("remove") && isTableModelListenerMethod(method));
	}
	public static MethodProxy getAddTableModelListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isAddTableModelListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static MethodProxy getRemoveTableModelListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		//Method[] methods = c.getMethods();
		MethodProxy[] methods = getMethods(c);
		for (int i = 0; i < methods.length; i++)
			if (isRemoveTableModelListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static boolean isTreeModelListenerMethod(MethodProxy method) {
		String name = method.getName();
		if (name.endsWith("TreeModelListener")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				RemoteSelector.classProxy(javax.swing.event.TreeModelListener.class).isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isAddTreeModelListenerMethod(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("add") && isTreeModelListenerMethod(method));
	}
	public static boolean isRemoveTreeModelListenerMethod(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("remove") && isTreeModelListenerMethod(method));
	}
	public static MethodProxy getAddTreeModelListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isAddTreeModelListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static MethodProxy getRemoveTreeModelListenerMethod(ClassProxy c) {
		if ( c == null) return null;
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isRemoveTreeModelListenerMethod(methods[i])) return methods[i];
		return null;
		
	}
	
	public static boolean isObserverMethod(MethodProxy method) {
		String name = method.getName();
		if (name.endsWith("Observer")) {
			ClassProxy[] params = method.getParameterTypes();
			if (params.length == 1 &&
				RemoteSelector.classProxy(Observer.class).isAssignableFrom(params[0]))
				return true;
			else
				return false;  
		}
		return false;
	}
	public static boolean isAddObserverMethod(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("add") && isObserverMethod(method));
	}
	public static boolean isDeleteObserverMethod(MethodProxy method) {
		String name = method.getName();
		return (name.startsWith("delete") && isObserverMethod(method));
	}
	public static MethodProxy getAddObserverMethod(ClassProxy c) {
		if ( c == null) return null;
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isAddObserverMethod(methods[i])) return methods[i];
		return null;
		
	}
	public static MethodProxy getDeleteObserverMethod(ClassProxy c) {
		if ( c == null) return null;
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isDeleteObserverMethod(methods[i])) return methods[i];
		return null;
		
	}
//	comp.
	//added so you can get the setter method by just giving the name of the property
	//used in TxfrButton's actnperf.
	//and DatabaseComposer
	public static MethodProxy getSetterMethod(ClassProxy c, String propertyName) {
		//Method[] methods = c.getMethods();
		//MethodProxy[] methods = getMethods(c);
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (isSetter(methods[i])  && methods[i].getName().toLowerCase().endsWith(propertyName.toLowerCase()))
				return methods[i];
		return null;
	}
	
//comp.
	//added so you can get the getter method by just giving the name of the property
	//used in TxfrButton's actnperf.
	//and DatabaseComposer
	public static MethodProxy getGetterMethod(ClassProxy c, String propertyName) {
		propertyName = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
		//Method[] methods = c.getMethods();
		//MethodProxy[] methods = getMethods(c);
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			//if (isGetter(methods[i])  && methods[i].getName().endsWith(propertyName))
			//if (methods[i].getName().equals("get" + propertyName) && isGetter(methods[i])  )
				if (isGetter(methods[i], propertyName))
			return methods[i];
		return null;
	}
//	F.O.
//	comp.
		/*
	public static Enumeration getAllPropertyNames(Object realObject) {
			Hashtable propHash = new Hashtable();
			Method[] methods = realObject.getClass().getMethods();
			
			for (int i=0; i < methods.length; i++)  {//for each method
				if (isSimplePropertyMethod(methods[i])) {  //if it's a property method
					
					String aProperty = methods[i].getName().substring(3);  //take the 3 letter set/get off
					propHash.put(aProperty,aProperty);  //hash it  ...assume get*sets will overwrite eachother
				}
			}
			
			if (!propHash.isEmpty())
				return propHash.elements();
			else 
				return null;
			
		}//end getPNam
		*/
		
		public static Enumeration getAllPropertiesNames(Object realObject) {  //difference is sthat this doesn't check if isSimple...rather if isProperty
			return getAllPropertiesNames(RemoteSelector.getClass(realObject));
			/*
			Hashtable propHash = new Hashtable();
			MethodProxy[] methods = RemoteSelector.getClass(realObject).getMethods();
			
			for (int i=0; i < methods.length; i++)  {//for each method
				if (isPropertyMethod(methods[i])) {  //if it's a property method
					
					String aProperty = methods[i].getName().substring(3);  //take the 3 letter set/get off
					aProperty = Character.toLowerCase(aProperty.charAt(0)) + aProperty.substring(1);
		//			System.out.println("UIBEAN" + aProperty);
					if (!aProperty.equals("Class"))
					propHash.put(aProperty,aProperty);  //hash it  ...assume get*sets will overwrite eachother
				}
			}
			
		//	System.exit(0);
			
			if (!propHash.isEmpty())
				return propHash.elements();
			else 
				return null;
				*/
			
		}//end getPNam
		public static Enumeration getAllPropertiesNames(ClassProxy cls) {
			return getAllPropertiesNamesVector(cls).elements();
		}
		public static boolean isUpperCase(String s) {
			for (int i = 0; i < s.length(); i++) {
				if (Character.isLowerCase(s.charAt(i)))
					return false;
			}
			return true;
		}
		public static Vector getAllPropertiesNamesVector(ClassProxy cls) {  //difference is sthat this doesn't check if isSimple...rather if isProperty
			Vector properties = new Vector();
			MethodProxy[] methods = cls.getMethods();
			
			for (int i=0; i < methods.length; i++)  {//for each method
				if (isPropertyMethod(methods[i])) {  //if it's a property method
					
					String aProperty = methods[i].getName().substring(3);  //take the 3 letter set/get off
					String suffix = aProperty.substring(1);
					if (!isUpperCase(suffix))
						aProperty = Character.toLowerCase(aProperty.charAt(0)) + suffix;
					//aProperty = Character.toLowerCase(aProperty.charAt(0)) + aProperty.substring(1);
		//			System.out.println("UIBEAN" + aProperty);
					if (!aProperty.equals("Class") && !properties.contains(aProperty) )
					properties.addElement(aProperty);  //hash it  ...assume get*sets will overwrite eachother
				}
			}
			
		//	System.exit(0);
			return properties;
			/*
			if (!propHash.isEmpty())
				return propHash.elements();
			else 
				return null;
				*/
			
		}//end getPNam
		
		
		
		//F.O. changed below from protected to public b/c I need it elsewhere...why make it protected?
				public static boolean isPropertyMethod(MethodProxy method) {
					if (!Modifier.isPublic(method.getModifiers()))
						return false;
					String name = method.getName();
					
					if (name.startsWith("get") && !name.equals("get")) {
						if (method.getParameterTypes().length == 0 &&
							!(method.getReturnType().equals(method.getDeclaringClass().voidType())) )
							return true;
						else
							return false;
					}
					else if (name.startsWith("set") && !name.equals("set")) {
						if (method.getParameterTypes().length == 1 &&
							method.getReturnType().equals(method.getDeclaringClass().voidType()))
							return true;
						else
							return false;
					}
					else
						return false;
				}
//	end comp.
				
//				F.O.
				public static MethodProxy getMethod(Object o, String origMeth) {
					String meth;
					if (Character.isDigit(origMeth.charAt(0)));
					   meth = "_" + origMeth + "_";
					ClassProxy c = RemoteSelector.getClass(o);
					MethodProxy[] methods = c.getMethods();
					
					//below is not good because it shouldn't be lowercasing the methodnames below before the test
					//can get away w/ it for now; dunno if it will mess things up
					for (int i = 0; i < methods.length; i++)  {
				//		System.out.println( methods[i].getName().toLowerCase() + " ,,,, " + meth.toLowerCase());
						if (methods[i].getName().toLowerCase().equals(meth.toLowerCase())) {
				//	System.out.println( methods[i].getName().toLowerCase() + " ,,,, " + meth.toLowerCase());
							return methods[i];
						}
					}
					return null;
				}
				
				public static String getVirtualClass(Object o) {
					if (o == null) return null;
					ClassProxy realClass = RemoteSelector.getClass(o);
					ClassProxy[] argTypes = {};
					try {
					MethodProxy m = realClass.getMethod("getVirtualClass", argTypes);
					if (m.getReturnType() != realClass.stringClass()) return null;
					Object[] args = {};
					String retVal = (String) m.invoke(o, args);
					return retVal;
					
					} catch (Exception e) {
						return null;
					}
					
				}
				
					
				public static void invokeInitSerializedObject(Object o) {
					if (o == null) return;
					try {
						MethodProxy init = RemoteSelector.getClass(o).getMethod(INIT_SERIALIZED_OBJECT, nullProxyParams);
						if (init == null) return;
						init.invoke(o, emptyArgs);
					} catch (NoSuchMethodException me) {
						System.out.println("Object: " + o + "does not have method: " + INIT_SERIALIZED_OBJECT);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
				
		
}

