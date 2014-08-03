/*
 * Created on Jun 3, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package bus.uigen.misc;

import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import util.pipe.AConsoleModel;
import util.pipe.ConsoleModel;
import util.remote.AProcessExecer;
import util.remote.ProcessExecer;
import util.trace.Tracer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.reflect.remote.RemoteClassProxy;
import bus.uigen.reflect.remote.StandardTypeConverter;
import bus.uigen.reflect.remote.StandardTypeIDs;
import bus.uigen.sadapters.EnumToEnumerationFactory;
import bus.uigen.trace.NoSpecializedEquals;
import bus.uigen.trace.NotSerializable;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualLabel;


public class OEMisc {
	static boolean checkSerializable = false;
	static boolean checkEquals = false;
	public static void setCheckSerializable(boolean newVal) {
		checkSerializable = false;
	}
    public static void setCheckEquals(boolean newVal) {
		checkEquals = newVal;
	}
	public static int indexOf (VirtualContainer parent, VirtualComponent comp) {
		if (parent == null) return -1;
		return indexOf (parent.getComponents(), comp);
	}
	public static int indexOf (Object[] parent, Object child) {
		if (parent == null) return -1;
		for (int i = 0; i < parent.length; i++) {
			if (parent[i] == child) return i;
		}
		return -1;
	}
	public static void sort (List objectList) {
		sort (objectList, 0, objectList.size());
	}
	public static List shallowCopy (List objectList) {
		int size = objectList.size();
		Vector tempList = new Vector(size);
		for (int i = 0; i < objectList.size(); i++)
			tempList.add (objectList.get(i));
		return tempList;
	}
	public static void shallowCopy (List objectList, List resultList) {
		if (objectList.size() != resultList.size()) {
			System.out.println("shallowCopy: source and result lists of different sizes");
		}
		for (int i = 0; i < objectList.size(); i++)
			resultList.set (i, objectList.get(i));
	}
	public static List reverseCopy (List objectList) {
		int size = objectList.size();
		Vector tempList = new Vector(size);
		for (int i = 0; i < objectList.size(); i++)
			tempList.add (objectList.get(size -1 - i));
		return tempList;
	}
	public static void reverse (List objectList) {
		List tempList = reverseCopy(objectList);
		shallowCopy(tempList, objectList);
	}
	public static void sort (List objectList, int startIndex, int stopIndex) {
		if (startIndex < 0 || stopIndex > objectList.size())
			return ;
		Hashtable stringToObject = new Hashtable();
		Vector strings = new Vector();
		for (int i = startIndex; i < stopIndex; i++) {
			String string = objectList.get(i).toString();
			String actualKey = string;	
			String suffix = "";
			while (stringToObject.get(actualKey) != null) {
				suffix += i + "\1";
				actualKey =  string + suffix;			
			}
			strings.addElement(actualKey);			
			stringToObject.put(actualKey, objectList.get(i));
		}
		Collections.sort(strings);
		for (int i = 0; i < strings.size(); i++) {
			objectList.set(i + startIndex, stringToObject.get(strings.elementAt(i)));
		}
				
	}
	public static Vector toVector (Enumeration elements) {
		Vector retVal = new Vector();
		while (elements.hasMoreElements())
			retVal.addElement(elements.nextElement());
		return retVal;
	  	
	  }
	
	public static Object[] add (Object[] original, Vector newElements) {
		if (newElements == null) return original;
		Object[] newArray = new Object [original.length + newElements.size()];
		for (int i = 0; i < original.length; i++) {
			newArray[i] = original[i];			
		}
		for (int i = 0; i < newElements.size(); i++ )
			newArray [i + original.length] = newElements.elementAt(i);
		return newArray;
		
		
	}
//	static Class[] classesWithCorrectEquals = {
//			Boolean.class,
//			String.class, 
//			Double.class,
//			Float.class,
//			Short.class,
//			Integer.class		
//		};
	static Set<Class> classesWithCorrectEquals = new HashSet();
	 static {
		 classesWithCorrectEquals.add(Boolean.class);
		 classesWithCorrectEquals.add(String.class);
		 classesWithCorrectEquals.add(Double.class);
		 classesWithCorrectEquals.add(Float.class);
		 classesWithCorrectEquals.add(Short.class);
		 classesWithCorrectEquals.add(Integer.class);
	}
	 static Set<Class> classesWithInCorrectEquals = new HashSet();
	 
	public static boolean contains (Object[] objects, Object object) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i].equals(object)) return true;
		}
		return false;
		
	}
	
	public static boolean isImmutable (Class c) {
	
		return c.isEnum() || StandardTypeConverter.immutableTypes.contains(c);
	}
	public static Object slowClone(Object o) {
		if (o == null) return null;
		if (!(o instanceof java.io.Serializable))
			return o;
		if (isImmutable(o.getClass()))
			return o;
		
//		if (o.getClass().getName().startsWith ("edu.unc.sync"))
//			System.out.println("Sync class cloned");
				//return 0;
		
		try {		

			//ObjectOutputStream fo = new ObjectOutputStream(new FileOutputStream("cloneFile"));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream fo = new ObjectOutputStream(bos);
			fo.writeObject(o);
			fo.flush();
			fo.close();
//			ObjectInputStream   fi = new ObjectInputStream(new FileInputStream("cloneFile"));
//			Object o_copy = fi.readObject();
//			fi.close();
			// Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            Object o_copy = in.readObject();

			return o_copy;
			
		} catch (Exception e2) {
			return o;
		}
	}
	static Set nonSerializableClasses = new HashSet();
	public static Object clone(Object o) {
		if (o == null) return null;
		
		// if we do not make an exception for sync, keep getting a null change.
		// either the serialization goes and destroys memory or somehow the mapping
		// to delegated object changes. Need to debug at some pt
		if (o.getClass().getName().startsWith ("edu.unc.sync"))
			//return 0;
			return o;
		if (nonSerializableClasses.contains(o.getClass()))
    		return o;
		if (!(o instanceof java.io.Serializable)) {
			nonSerializableClasses.add(o.getClass());
			if (checkSerializable)
				NotSerializable.newCase(RemoteSelector.classProxy(o.getClass()), OEMisc.class);
//				Tracer.warning("Make class " + o.getClass() + " serializable when you learn about this concept to make implicit refresh work reliably." );
        
			return o;
		}
		if (isImmutable(o.getClass()))
			return o;
		
//		if (o.getClass().getName().startsWith ("edu.unc.sync"))
//			System.out.println("Sync class cloned");
				//return 0;
		
		try {		

			return util.misc.Common.deepCopy(o);
			
		} catch (Exception e2) {
			return o;
		}
	}
	
	public static void saveState(Object o, String fileName) {
		if (!(o instanceof java.io.Serializable)) {
			System.out.println("E*** " + o.getClass() + " CANNOT BE SAVED AS IT IS NOT SERIALIABLE");
			return ;
		}
		try {
			ObjectOutputStream fo = new ObjectOutputStream(new FileOutputStream(fileName));
			fo.writeObject(o);
			fo.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Object loadState(String fileName) {
		try {
		ObjectInputStream f = new ObjectInputStream(new FileInputStream(fileName));
		  Object saved_object = f.readObject();
		  return saved_object;
		} catch (Exception e) {
			//e.printStackTrace();
			//System.out.println("Info**: Did not load saved state" );
			return null;
		}
	}
	public static Object loadState(Class c) {
		return loadState(classToSavedFile(c));
	}
	public static Object loadState(Object o) {
		return loadState(objectToSavedFile(o));
	}
	public static String classToSavedFile(Class c) {
		return c.getSimpleName()+".obj";
	}
	public static String objectToSavedFile(Object o) {
		if (o == null)
			return null;
		return classToSavedFile(o.getClass());
	}
	public static void saveState(Object o) {
		if (o == null)
			return ;
		
		String fileName = objectToSavedFile(o);
		saveState(o, fileName);
	}
	//public static  objectsWithCorrect
//	public static boolean oldEquals (Object o1, Object o2) {
//		if (o1 == null && o2 == null) return true;
//		if (o1 == null || o2 == null) return false;
//		Class o1Class = o1.getClass();
//		ClassProxy classProxy = ACompositeLoggable.getTargetClass(o1);
//		if (EnumToEnumerationFactory.isEnumeration(classProxy))
//			return o1 == o2;
//		//if (o1Class.isEnum()) return o1 == o2;
//		if (contains (classesWithCorrectEquals, o1Class))
//			return o1.equals(o2);	
//		try {
//		Class argClass[] = {o2.getClass()};
//		//Class argClass[] = {Object.class};
//		//try {
//		
//		Method equalMethod = o1Class.getMethod ("equals", argClass);
//		if (equalMethod == null) {
//			return	o1.toString().equals(o2.toString());
//		}
//		if (equalMethod.getDeclaringClass() == Object.class) return false;
//		Object arg[] = {o2};
//		Object retVal = equalMethod.invoke(o1, arg);
//		return (Boolean) retVal;
//		} catch (Exception e) {
//			return	o1.toString().equals(o2.toString());
//			//return false;
//		}
//	}
	public static boolean oldEquals (Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		if (o1 == null || o2 == null) return false;
		Class o1Class = o1.getClass();
		ClassProxy classProxy = ACompositeLoggable.getTargetClass(o1);
		if (EnumToEnumerationFactory.isEnumeration(classProxy))
			return o1 == o2;
		//if (o1Class.isEnum()) return o1 == o2;
		if (classesWithCorrectEquals.contains( o1Class))
			return o1.equals(o2);	
		try {
		Class argClass[] = {o2.getClass()};
		//Class argClass[] = {Object.class};
		//try {
		
		Method equalMethod = o1Class.getMethod ("equals", argClass);
		if (equalMethod == null) {
			return	o1.toString().equals(o2.toString());
		}
		if (equalMethod.getDeclaringClass() == Object.class) return false;
		Object arg[] = {o2};
		Object retVal = equalMethod.invoke(o1, arg);
		return (Boolean) retVal;
		} catch (Exception e) {
			return	o1.toString().equals(o2.toString());
			//return false;
		}
	}
	public static boolean equals (Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		if (o1 == null || o2 == null) return false;
		Class o1Class = o1.getClass();
		ClassProxy classProxy = ACompositeLoggable.getTargetClass(o1);
		if (EnumToEnumerationFactory.isEnumeration(classProxy))
			return o1 == o2;
		//if (o1Class.isEnum()) return o1 == o2;
		// should expand this class
		if (classesWithCorrectEquals.contains(o1Class))
			return o1.equals(o2);
		if (classesWithInCorrectEquals.contains(o1Class)) {			
			return false;
		}
		try {
//		Class argClass[] = {o2.getClass()};
		//Class argClass[] = {Object.class};
		//try {
		
		Method equalMethod = o1Class.getMethod ("equals", Object.class);
//		if (equalMethod == null) {
//			return	o1.toString().equals(o2.toString());
//		}
		if (equalMethod.getDeclaringClass() == Object.class) {
			classesWithInCorrectEquals.add(o1Class);
			if (checkEquals)
				NoSpecializedEquals.newCase(  ReflectUtil.toMaybeProxyTargetClass(o1), OEMisc.class);
//			Tracer.warning("equals() method of Object not overriden in: " + RemoteSelector.getClass(o1) + ". Please override it when you learn about it to make refresh work more reliably and efficiently");
			return false;
		} else {
			classesWithCorrectEquals.add(o1Class);
			Tracer.info(OEMisc.class, "equals() method of Object  overriden for: " + o1 + ". Assuming it is correct");

			return o1.equals(o2);
		}
		
		} catch (Exception e) {
			return	o1.toString().equals(o2.toString());
			//return false;
		}
	}
	public static boolean equals (List list1, List list2) {
		if (list1 == list2) return true;
		if (list1.size() != list2.size()) return false;
		for (int i = 0; i < list1.size(); i++) {
			if (!list1.get(i).equals(list2.get(i)))
				return false;
		}
		return false;
	}
	public static void setSize (VirtualComponent c, int width, int height) {
		VirtualDimension newSize = new VirtualDimension (width, height);
		c.setPreferredSize(newSize);
	}
	
	//static int MIN_WIDTH = 45;
	//static int MIN_WIDTH = 16;
	public static void setWidth (VirtualComponent c, int width) {
		if (width == 0) 
			return;
		int height = c.getPreferredSize().getHeight();
		if (height == 0)
			height = c.getSize().getHeight();
		//height = Misc.SWING_DEFAULT_HEIGHT;
		//if (height < 0)
		if (height <= 0)
			height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width <= 0)
			width = OEMisc.SWING_DEFAULT_WIDTH;
		//int pwidth = c.getPreferredSize().width;
		//Dimension newSize = new Dimension (Math.min(width, MIN_WIDTH), Math.min(height, MIN_HEIGHT));
		VirtualDimension newSize = new VirtualDimension (width, height);
		c.setPreferredSize(newSize);
		//c.setSize(newSize);
		//c.setSize(c.getPreferredSize());
		
		
	}
	public static void setWidth (VirtualComponent c, int width, Integer x, Integer y) {
		if (x == null || y == null) {
			setWidth(c, width);
			return;
		}
		if (width == 0) 
			return;
		int height = c.getPreferredSize().getHeight();
		if (height == 0)
			height = c.getSize().getHeight();
		//height = Misc.SWING_DEFAULT_HEIGHT;
		//if (height < 0)
		if (height <= 0)
			height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width <= 0)
			width = OEMisc.SWING_DEFAULT_WIDTH;
		c.setBounds (x, y, width, height);
		//int pwidth = c.getPreferredSize().width;
		//Dimension newSize = new Dimension (Math.min(width, MIN_WIDTH), Math.min(height, MIN_HEIGHT));
		//Dimension newSize = new Dimension (width, height);
		//c.setPreferredSize(newSize);
		//c.setSize(newSize);
		//c.setSize(c.getPreferredSize());
		
		
	}
	public static void setWidth(VirtualComponent label, ObjectAdapter adapter) {
		int labelLength = adapter.getLabelWidth();
		//if (labelLength != 0)
		OEMisc.setWidth(label, labelLength);
	}
	public static void setHeight (VirtualComponent c, int height) {
		if (height == 0) 
			return;
		
		int width =   c.getPreferredSize().getWidth();
//		if (c instanceof VirtualLabel) {
//			c.setPreferredSize(new VirtualDimension(width, height)); 
//			return;
//		}
		int width2 = c.getSize().getWidth();
		int width3 =  c.getMinimumSize().getWidth();
		//height = Misc.SWING_DEFAULT_HEIGHT;		
		if (height <= 0)
			height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width <= 0)
			width = OEMisc.SWING_DEFAULT_WIDTH;
		//int pwidth = c.getPreferredSize().width;
		//Dimension newSize = new Dimension (Math.min(width, MIN_WIDTH), Math.min(height, MIN_HEIGHT));
		VirtualDimension newSize = new VirtualDimension (width, height);
		c.setPreferredSize(newSize);
		//c.setSize(c.getPreferredSize());
		
		
	}
	public static void setHeight (VirtualComponent c, int height, Integer x, Integer y) {
		if (height == 0) 
			return;
		if (x == null || y == null) {
			OEMisc.setHeight(c, height);
			return;
		}
		int width = c.getPreferredSize().getWidth();
		int width2 = c.getSize().getWidth();
		int width3 =  c.getMinimumSize().getWidth();
		//height = Misc.SWING_DEFAULT_HEIGHT;		
		if (height <= 0)
			height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width <= 0)
			width = OEMisc.SWING_DEFAULT_WIDTH;
		//int pwidth = c.getPreferredSize().width;
		//Dimension newSize = new Dimension (Math.min(width, MIN_WIDTH), Math.min(height, MIN_HEIGHT));
		VirtualDimension newSize = new VirtualDimension (width, height);
		c.setPreferredSize(newSize);
		//c.setSize(c.getPreferredSize());
		
		
	}
	//defaults for swing
	public static int SWING_DEFAULT_HEIGHT = 16;
	public static int SWING_DEFAULT_WIDTH = 45;
	
	public static boolean equals (ClassProxy c1, ClassProxy c2) {
		//if (c1 == c2)
		if (c1.equals(c2))
			return true;
		ClassProxy outerClass1 = EnumToEnumerationFactory.getOuterEnumeration(c1);
		ClassProxy outerClass2 = EnumToEnumerationFactory.getOuterEnumeration(c2);
		if ((outerClass1 == null) && (outerClass2 == null))
			return false;
		if (outerClass1 == null)
			outerClass1 = c1;
		if (outerClass2 == null)
			outerClass2 = c2;
		return outerClass1 == outerClass2;
		/*
		if (EnumToEnumerationFactory.isEnumeration(c1) && EnumToEnumerationFactory.isEnumeration(c2)) {
			String c1Name = c1.getName();
			String c2Name = c2.getName();
			if (c1Name.contains("$") || c2Name.contains("$"))
					return c1Name.contains(c2Name) || c2Name.contains(c1Name);
			else
				return false;
			
		} else
			return false;
			*/
	}
	public static boolean equalsClass (Object o1, Object o2) {
		if (o1 == null && o2 == null)
			return true;
		if (o1 == null || o2 == null)
			return false;
		return equals (o1.getClass(), o2.getClass());
	}
	public static void askViewObjectToRefresh(Object viewObject) {
		try {
		MethodProxy viewRefresher = IntrospectUtility.getViewRefresher(viewObject);
		if (viewRefresher == null)
			return;
		Class[] params = {};
		MethodInvocationManager.invokeMethod(viewRefresher, viewObject, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	public static boolean isParsable (ClassProxy cp) {
		if (cp instanceof AClassProxy)
			return util.misc.Common.isParsable(((AClassProxy) cp).getJavaClass());
		else if (cp instanceof RemoteClassProxy) {
			int typeID = ((RemoteClassProxy) cp).getClassID();
			return typeID < StandardTypeIDs.MAX_IMMUTABLE_ID;
		} else
			return false;
		
	}
//	public static int CONSOLE_WIDTH = 320;
//	public static int CONSOLE_HEIGHT =350;
	public static int CONSOLE_WIDTH_MARGIN = 15;
	public static int CONSOLE_HEIGHT_MARGIN =0;

	
	public static ProcessExecer runWithObjectEditorConsole(Class aJavaClass, String args) {
		return runWithObjectEditorConsole(aJavaClass, args, new AConsoleModel());

	}	
	public static ProcessExecer runWithObjectEditorConsole(Class aJavaClass, String[] args) {
		return runWithObjectEditorConsole(aJavaClass, toArgString(args), new AConsoleModel());

	}
	public static ProcessExecer runWithObjectEditorConsole(Class aJavaClass, String args, ConsoleModel aConsoleModel) {
//		ProcessExecer processExecer = 
//				new AProcessExecer(aJavaClass, args);
//		Process process = processExecer.execProcess();
		ProcessExecer processExecer = runWithProcessExecer(aJavaClass, args, aConsoleModel);
		ConsoleModel consoleModel = processExecer.consoleModel();
		OEFrame frame = ObjectEditor.edit(consoleModel);
		frame.setSize(AConsoleModel.CONSOLE_WIDTH + CONSOLE_WIDTH_MARGIN, 
				AConsoleModel.CONSOLE_HEIGHT + CONSOLE_HEIGHT_MARGIN);
		consoleModel.initFrame((Frame) frame.getFrame().getPhysicalComponent());
		frame.setTitle(consoleModel.getTitle());
		return processExecer;
	}
	
	public static ProcessExecer runWithProcessExecer(Class aJavaClass, String args) {
//		ProcessExecer processExecer = 
//				new AProcessExecer(aJavaClass, args);
//		Process process = processExecer.execProcess();
//		return processExecer;
		return runWithProcessExecer(aJavaClass, args, new AConsoleModel());
	}
	public static String toArgString(String[] args) {
		StringBuilder anArgs = new StringBuilder();
		for (String anArg:args)
			anArgs.append(" " + anArg);
		return anArgs.toString();
	}
	public static ProcessExecer runWithProcessExecer(Class aJavaClass, String[] args) {
//		ProcessExecer processExecer = 
//				new AProcessExecer(aJavaClass, args);
//		Process process = processExecer.execProcess();
//		return processExecer;
		return runWithProcessExecer(aJavaClass, toArgString(args), new AConsoleModel());
	}
	public static ProcessExecer runWithProcessExecer( Class aJavaClass, String args, ConsoleModel aConsoleModel) {
		ProcessExecer processExecer = 
				new AProcessExecer(aJavaClass, args);
		processExecer.setConsoleModel(aConsoleModel);
		Process process = processExecer.execProcess();
		return processExecer;
	}
	public static Object defaultValue(ClassProxy cp) {
		
		if (cp instanceof AClassProxy) {
			return util.misc.Common.defaultValue(((AClassProxy) cp).getJavaClass());
		} else if (cp instanceof RemoteClassProxy) {
			initClassIDsToDefaultInstances();
			return classIDToDefaultInstance.get(((RemoteClassProxy) cp).getClassID());
		} else
			return null;
		
	}
	static Hashtable<Integer, Object> classIDToDefaultInstance = new Hashtable();
	static boolean classIDsToDefaultInstancesInitialized = false;
	static void initClassIDsToDefaultInstances() {
		if (classIDsToDefaultInstancesInitialized)
			return;
		classIDsToDefaultInstancesInitialized = true;
		classIDToDefaultInstance.put(StandardTypeIDs.DOUBLE_CLASS, 0.0);
		classIDToDefaultInstance.put(StandardTypeIDs.DOUBLE_TYPE, 0.0);
		classIDToDefaultInstance.put(StandardTypeIDs.FLOAT_CLASS, 0.0);
		classIDToDefaultInstance.put(StandardTypeIDs.FLOAT_TYPE, 0.0);
		classIDToDefaultInstance.put(StandardTypeIDs.INTEGER_CLASS, 0);
		classIDToDefaultInstance.put(StandardTypeIDs.SHORT_TYPE, 0);
		classIDToDefaultInstance.put(StandardTypeIDs.SHORT_CLASS, 0);
		classIDToDefaultInstance.put(StandardTypeIDs.SHORT_TYPE, 0);
		classIDToDefaultInstance.put(StandardTypeIDs.LONG_CLASS, 0);
		classIDToDefaultInstance.put(StandardTypeIDs.LONG_TYPE, 0);
		classIDToDefaultInstance.put(StandardTypeIDs.CHARACTER_CLASS, '.');
		classIDToDefaultInstance.put(StandardTypeIDs.CHARACTER_TYPE, '.');
		classIDToDefaultInstance.put(StandardTypeIDs.BOOLEAN_CLASS, true);
		classIDToDefaultInstance.put(StandardTypeIDs.BOOLEAN_TYPE, true);
		classIDToDefaultInstance.put(StandardTypeIDs.STRING_CLASS, "");
		

	}

}
