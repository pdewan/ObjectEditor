package bus.uigen.controller.models;

import java.util.Enumeration;
import java.util.Hashtable;


import bus.uigen.uiFrame;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.oadapters.ObjectAdapter;;

public class FrameModelRegistry {
	public static final String UNDO_REDO_MODEL_NAME = "Undo/Redo";
	public static final String DO_MODEL_NAME = "Do";
	public static final String SELECTION_MODEL_NAME = "Selection";
	public static final String MISC_EDIT_OPERATIONS_MODEL_NAME = "Misc Edit";
	public static final String REFRESH_OPERATIONS_MODEL_NAME = "Refresh";
	static Hashtable <String, FrameModel> registry = new Hashtable();
	static Hashtable <String, ClassProxy> registeredClasses = new Hashtable();
	public static  FrameModel put (String name, FrameModel model) {
		return registry.put (name, model);
	}
	public static  FrameModel putDefault (String name, FrameModel model) {
		if (registry.get(name) == null)
		return registry.put (name, model);
		else return null;
	}
	public static  ClassProxy putDefault (String name, ClassProxy modelClass) {
		if (registeredClasses.get(name) == null)
		return registeredClasses.put (name, modelClass);
		else return null;
	}
	
	public static FrameModel get (String name) {
		return registry.get(name);
	}
	public static FrameModel remove (String name) {
		return registry.remove(name);
	}	
	public static Enumeration<String> keys() {
		return registry.keys();
	}
	public static Enumeration<FrameModel> elements() {
		return registry.elements();
	}
	
	public static void clear() {
		registry.clear();
	}
//	public static void registerAll(uiFrame frame, Object theObject) {
//		try {
//			Enumeration<ClassProxy> elements = registeredClasses.elements();
//			while (elements.hasMoreElements()) {
//				ClassProxy frameModelClass = elements.nextElement();
//				FrameModel frameModel = (FrameModel) frameModelClass.newInstance();
//				frameModel.init(frame, theObject);
//				frame.addButNotDisplayMenuObject (frameModel);
//			}
//			frame.displayMenuTree();
//		} catch (Exception e) {
//			System.err.println("ModelRegistry: Register All: " + e);
//			e.printStackTrace();
//		}	
//	}
	public static void registerAll(uiFrame frame, Object theObject, ObjectAdapter theObjectAdapter) {
		try {
			for (String name: registeredClasses.keySet()) {
			 
				ClassProxy frameModelClass = registeredClasses.get(name);
				FrameModel frameModel = (FrameModel) frameModelClass.newInstance();
				frameModel.init(frame, theObject, theObjectAdapter);
				frame.addButNotDisplayMenuObject(name, frameModel);
			}
			frame.displayMenuTree();
		} catch (Exception e) {
			System.err.println("ModelRegistry: Register All: " + e);
			e.printStackTrace();
		}	
	}
}
