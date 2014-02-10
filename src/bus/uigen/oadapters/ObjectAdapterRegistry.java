package bus.uigen.oadapters;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.*;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;

import java.awt.Container;
public class ObjectAdapterRegistry {
	static void addLast(Vector v, Object newValue) {
		if (v.contains(newValue)) return;
		v.addElement(newValue);
	}
	static void addFirst(Vector v, Object newValue) {
		if (v.contains(newValue)) return;
		v.insertElementAt (newValue, 0);
	}
	static Vector objectAdapterRegistry = new Vector();	
	public static void addLast(ObjectAdapterFactory newValue) {
		addLast(objectAdapterRegistry, newValue);
		
	}
	public static void addFirst(ObjectAdapterFactory newValue) {
		addFirst(objectAdapterRegistry, newValue);		
	}	
	public static void removeElement(ObjectAdapterFactory oldValue) {
		objectAdapterRegistry.removeElement(oldValue);
	}
	public static ObjectAdapterFactory elementAt(int index) {
		return (ObjectAdapterFactory) objectAdapterRegistry.elementAt(index);
	}
	public static int size(){
		return  objectAdapterRegistry.size();
	}
	
	
	
	
	
	static Hashtable connections = new Hashtable();	
	public static ObjectAdapterFactory put(Class concreteType, ObjectAdapterFactory objectAdapterFactory) {
		//System.out.println("Putting concreteClass " + concreteType + " OAFactory " + objectAdapterFactory);
		return (ObjectAdapterFactory) connections.put(concreteType, objectAdapterFactory);
	}
	public static ObjectAdapterFactory get(Class concreteType) {
		return (ObjectAdapterFactory) connections.get(concreteType);
	}
	public static ObjectAdapterFactory remove(Class concreteType) {
		return (ObjectAdapterFactory) connections.remove(concreteType);
	}
	
	
	
	public static boolean match (ObjectAdapterFactory objectAdapterFactory,
									   Class concreteType) {
		return objectAdapterFactory.getConcreteType().isAssignableFrom(concreteType);
	}
	public static ObjectAdapterFactory matchObjectAdapterFactory (Class concreteType) {
		for (int i = 0; i < size(); i++) {
			if (match (elementAt(i), concreteType))
				return elementAt(i); 
		}
		return null;
	}
	public static void connectObjectAdapterFactoriesAndConcreteTypes() {
		for (int i = 0; i < ConcreteTypeRegistry.size(); i++) {
			Class concreteType = ConcreteTypeRegistry.elementAt(i).getConcreteType();
			ObjectAdapterFactory objectAdapterFactory = matchObjectAdapterFactory(concreteType);
			if (objectAdapterFactory != null)
				put (concreteType, objectAdapterFactory);
		}
	}
	
	//static boolean objectEditorHasRegistered = false;
	
	public static ObjectAdapter createObjectAdapter (ConcreteType concreteObject, 
									/*Container containW,*/
										   Object obj, 											  Object obj1, 											  Object parentObject, 
											  int posn,											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) {
		/*
		if (!objectEditorHasRegistered) {
			ObjectEditor.register();
			objectEditorHasRegistered = true;
		}
		*/
	  
		
	  ObjectAdapterFactory adapterFactory = get(concreteObject.getClass());
	  if (adapterFactory == null) return null;
 	  ObjectAdapter retVal = adapterFactory.createObjectAdapter (concreteObject,																   //containW, 
																   obj, 
									obj1, 									parentObject, 
									posn,									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);	  if (retVal == null) return null;
	  retVal.setConcreteObject(concreteObject);
	  return retVal;
	}
	static {
		registerObjectAdapterFactories();
		connectObjectAdapterFactoriesAndConcreteTypes();
	}
	public static void registerObjectAdapterFactories() {
		//if (objectAdapterFactoriesRegistered) return;		
		addLast(new EnumerationAdapterFactory());
		addLast(new PrimitiveAdapterFactory());
		addLast(new HashtableAdapterFactory());
		addLast(new VectorAdapterFactory());
		addLast(new AWTShapeAdapterFactory());
		addLast(new PointAdapterFactory());
		addLast(new LineAdapterFactory());
		addLast(new RectangleAdapterFactory());
		addLast(new OvalAdapterFactory());
		addLast(new StringShapeAdapterFactory());
		addLast(new ImageShapeAdapterFactory());
		addLast(new LabelShapeAdapterFactory());
		addLast(new TextShapeAdapterFactory());
		addLast(new ArcAdapterFactory());
		addLast(new CurveAdapterFactory());
		addLast(new ClassAdapterFactory());
		//objectAdapterFactoriesRegistered = true;		
	}

	
}
