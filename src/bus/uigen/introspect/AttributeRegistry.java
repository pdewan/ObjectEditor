package bus.uigen.introspect;

import java.util.Enumeration;
import java.util.Hashtable;

import util.introspect.JavaIntrospectUtility;


//import bus.uigen.uiFrame;
import bus.uigen.uiFrame;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.undo.ExecutableCommand;

public class AttributeRegistry {
	public static String ATTRIBUTE_REGISTERER = "AttributeRegisterer";
	public static String ATTRIBUTE_REGISTERER_2 = "AR";
	static Hashtable <ClassProxy, ExecutableCommand> registry = new Hashtable();
	static Hashtable <String, String> alternatePackage = new Hashtable();
	static Hashtable <ClassProxy, ClassProxy> classToAR = new Hashtable();
	public static  ExecutableCommand put (ClassProxy cl, ExecutableCommand command) {
		return registry.put (cl, command);
	}
	public static  ExecutableCommand putDefault (ClassProxy cl, ExecutableCommand command) {
		if (registry.get(cl) == null)
		return registry.put (cl, command);
		else return null;
	}
	public static  ExecutableCommand putDefaultAndExecute (ClassProxy cl, ExecutableCommand command, Object theFrame) {
		if (registry.get(cl) == null) {
			ExecutableCommand retVal = registry.put (cl, command);
			command.execute(theFrame);
			return retVal;
		} else return null;
	}
	
	public static ExecutableCommand get (ClassProxy cl) {
		return registry.get(cl);
	}
	public static ExecutableCommand remove (ClassProxy cl) {
		return registry.remove(cl);
	}	
	public static Enumeration<ClassProxy> keys() {
		return registry.keys();
	}
	public static Enumeration<ExecutableCommand> elements() {
		return registry.elements();
	}
	public static void clear() {
		registry.clear();
	}
	public static void registerAll() {
		try {
			Enumeration<ExecutableCommand> elements = elements();
			while (elements.hasMoreElements()) {
				elements.nextElement().execute(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("AttributeRegistry: Register All: " + e);
		}
	}
	public static void registerAttributeRegistrer (ClassProxy modelClass, Object theFrame) {
		//String attributeRegistrer = modelClass.getName() + ATTRIBUTE_REGISTERER;
				
		try {			
			ClassProxy attributeRegistrerClass = getAttributeRegisterer(modelClass);
			if (attributeRegistrerClass == null) { 
			attributeRegistrerClass = AttributeRegistry.getAttributeRegistererClass(modelClass);

			if (attributeRegistrerClass == null)
				return;
			}
			
			//Class attributeRegistrerClass = Class.forName(attributeRegistrer);
			Object attributeRegistrerObject = attributeRegistrerClass.newInstance();
			if (attributeRegistrerObject instanceof ExecutableCommand) {
				putDefaultAndExecute (modelClass, (ExecutableCommand) attributeRegistrerObject, theFrame);
				//if (theFrame != null)
					//theFrame.addClassToSourceMenu(attributeRegistrerClass);
			} else
				System.err.println(attributeRegistrerClass + " must implement ExecutableCommand");
		} catch (Exception e) {
			
		}
		
	}
	public static void setAttributeRegisterer(ClassProxy baseClass, ClassProxy attributeRegisterer) {
		classToAR.put(baseClass, attributeRegisterer);
	}
	public static ClassProxy getAttributeRegisterer(ClassProxy baseClass) {
		return classToAR.get(baseClass);
	}
	
	public static ClassProxy getAttributeRegistererClass (ClassProxy modelClass) {
		String attributeRegistrer = modelClass.getName() + ATTRIBUTE_REGISTERER;
		//ClassProxy attributeRegistrerClass = RemoteSelector.classProxy(IntrospectUtility.classForName(attributeRegistrer));
		ClassProxy attributeRegistrerClass = RemoteSelector.classProxy(JavaIntrospectUtility.classForName(attributeRegistrer));
		if (attributeRegistrerClass != null)
			return attributeRegistrerClass;
		attributeRegistrer = modelClass.getName() + ATTRIBUTE_REGISTERER_2;
		attributeRegistrerClass = RemoteSelector.classProxy(JavaIntrospectUtility.classForName(attributeRegistrer));
		if (attributeRegistrerClass != null)
			return attributeRegistrerClass;
		return null;
	}

	


}
