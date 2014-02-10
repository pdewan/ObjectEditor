package bus.uigen.loggable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import util.models.ADynamicRecord;
import util.models.AListenableVector;
import util.models.ATableWithObjectKeys;
import util.models.DynamicRecord;
import util.trace.Tracer;

import bus.uigen.ObjectEditor;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.HashtableAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.sadapters.EnumToEnumerationFactory;
import bus.uigen.sadapters.GenericPrimitiveToPrimitiveFactory;
import bus.uigen.sadapters.HashtableStructure;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;
import bus.uigen.visitors.CreateChildrenAdapterVisitor;

public class LoggableRegistry {
	static Hashtable <ACompositeLoggable, Set<ObjectAdapter>> loggableToAdapter = new Hashtable();
	// static Hashtable<Object, ClassProxy> proxyToClass = new Hashtable();
	static Hashtable<Integer, ACompositeLoggable> intIdToLoggableModel = new Hashtable();
	static Hashtable<String, ACompositeLoggable> stringIdToLoggableModel = new Hashtable();
	static Hashtable<Object, Integer> objectToIntId = new Hashtable();
	//static Hashtable<Object, String> objectToStringId = new Hashtable();
	static ATableWithObjectKeys<Object, String> objectToStringId = new ATableWithObjectKeys();
	static Hashtable<Object, ACompositeLoggable> objectToLoggableModel = new Hashtable();
	//static Hashtable<ACompositeLoggable, Object> loggableToObject = new Hashtable();
	//static Hashtable<String, Object> stringIdToObject = new Hashtable();
	static Hashtable<Integer, MethodProxy> intIdToMethod = new Hashtable();
	static Hashtable<MethodProxy, Integer> methodToIntId = new Hashtable();
	static Hashtable<String, MethodProxy> stringIdToMethod = new Hashtable();
	// static Hashtable<MethodProxy, String> methodToStringId = new Hashtable();
	// static Set<Integer> isReadOnly = new HashSet();
	static Set<String> isReadOnly = new HashSet();
	// static Set<Integer> returnsValue = new HashSet();
	static Set<String> returnsValue = new HashSet();
	static int nextId = 0;
	static int nextMethodId = 0;
	static ANotificationBus notificationBus = new ANotificationBus();

	public static Object convertAdapter(ObjectAdapter objectAdapter) {
		if (objectAdapter == null)
			return null;
		if (objectAdapter instanceof CompositeAdapter)
			return convertContainerAdapter((CompositeAdapter) objectAdapter);
		return objectAdapter.getRealObject();
	}

	static GenericPrimitiveToPrimitiveFactory primitiveFactory = new GenericPrimitiveToPrimitiveFactory();
	static EnumToEnumerationFactory enumFactory = new EnumToEnumerationFactory();

	static Hashtable<ACompositeLoggable, AnIdentifiableLoggable> compositeToIdentifiable = new Hashtable<ACompositeLoggable, AnIdentifiableLoggable>();
	
	public static AnIdentifiableLoggable removeRealObject(ACompositeLoggable source) {
		
		AnIdentifiableLoggable retVal = compositeToIdentifiable.get(source);
		if ( retVal != null ) {
			return retVal;
		}
		
		retVal = new AnIdentifiableLoggable();
		retVal.setObjectId(source.getObjectId());
		retVal.setObjectIds(source.getObjectIds());
		retVal.setVirtualClass(source.getVirtualClass());
		compositeToIdentifiable.put(source, retVal);
		return retVal;
	}
	public static Object getLoggableModel(Object realObject) {
		// uiObjectAdapter adapter = ObjectEditor.toObjectAdapter(realObject);
		if (realObject == null) return null;
		Object retVal = getExistingLoggableModel(realObject);		
		if (retVal != null) {
			//if (!(retVal instanceof ACompositeLoggable))
				return retVal;
			
			//return removeRealObject((ACompositeLoggable) retVal);
			//return retVal;
		}
		return createLoggableModel(realObject);
		/*
		int id = nextId++;
		String stringId = "" + id;
		return createLoggableModel(realObject, stringId);
		*/
		// return createCompositeLoggableModel(realObject);
	}
	static Object createLoggableModel (Object realObject) {
		int id = nextId++;
		String stringId = "" + id;
		return createLoggableModel(realObject, stringId);
	}
	public static Object getExistingLoggableModelOrRealObject (Object realObject) {
		Object retVal = getExistingLoggableModel(realObject);
		if (retVal == null)
			return realObject;
		else
			return retVal;
	}
	public static boolean  isPrimitive(Object realObject) {
		if (realObject == null)
			return false;
		ClassProxy realClass =  ReflectUtil.toMaybeProxyTargetClass(realObject);
		return isPrimitive(realClass);
		/*
		return primitiveFactory.isPrimitive(realClass)
		|| enumFactory.isEnumeration(realClass);
		*/
	}
	public static boolean  isPrimitive(ClassProxy realClass) {
		
		return primitiveFactory.isPrimitive(realClass)
		|| enumFactory.isEnumeration(realClass);
	}
	public static Object getExistingLoggableModel(Object realObject) {
		// uiObjectAdapter adapter = ObjectEditor.toObjectAdapter(realObject);
		if (realObject == null)
			return null;
		/*
		ClassProxy realClass = RemoteSelector.getClass(realObject);
		if (realObject instanceof ACompositeLoggable
				|| primitiveFactory.isPrimitive(realClass, realObject)
				|| enumFactory.isEnumeration(realClass))
			return realObject;
			*/
		if (realObject instanceof ACompositeLoggable || isPrimitive(realObject))
			return realObject;
		String existingId = objectToStringId.get(realObject);
		if (existingId != null) {
			ACompositeLoggable compositeModel =  stringIdToLoggableModel.get(existingId);
//			AnIdentifiableLoggable retVal = removeRealObject(compositeModel);
//			return retVal;
			return compositeModel;
			
		}
		return  null;
	}
	
	

	public static Object getRealObject(Object loggableModel) {
		if (loggableModel == null)
			return null;
		if (loggableModel instanceof ACompositeLoggable) {
			return ((ACompositeLoggable) loggableModel).getRealObject();
			//return loggableToObject.get(((ACompositeLoggable) loggableModel));
			//return stringIdToObject.get(((ACompositeLoggable) loggableModel).getObjectId());
		
		} else
			return loggableModel;
	}
	
	public static Object[] getRealObjects(Object[] loggableModels) {
		Object[] retVals = new Object[loggableModels.length];
		for (int i = 0; i < retVals.length; i++) {
			retVals[i] = getRealObject(loggableModels[i]);
		}
		return retVals;
	}
	
	

	static Object convertContainerAdapter(
			CompositeAdapter containerAdapter) {
		if (containerAdapter instanceof VectorAdapter)
			return convertVectorAdapter((VectorAdapter) containerAdapter);
		else if (containerAdapter instanceof HashtableAdapter)
			return convertHashtableAdapter((HashtableAdapter) containerAdapter);
		else if (containerAdapter instanceof ClassAdapter)
			return convertClassAdapter((ClassAdapter) containerAdapter);

		return null;
	}

	static Object convertVectorAdapter(VectorAdapter vectorAdapter) {
		AListenableVector listenableVector = new AListenableVector();
		fillVector(vectorAdapter, listenableVector);
		return listenableVector;
		// return null;
		// return convertVectorStructure (vectorAdapter.getVectorStructure());

	}

    static void fillVector(VectorAdapter vectorAdapter,
			AListenableVector listenableVector) {
		Vector<ObjectAdapter> objectAdapters = vectorAdapter
				.getIndexedAdaptersVector();
		// Vector properties = classAdapter.componentNames();
		// if (properties.size() == 0) return null;
		for (int i = 0; i < objectAdapters.size(); i++) {
			Object convertedElement = convertAdapter(objectAdapters
					.elementAt(i));
			listenableVector.add(convertedElement);
		}
		// return record;
	}

	static Object convertHashtableAdapter(
			HashtableAdapter hashtableAdapter) {
		return null;
		// return
		// convertHashtableStructure(hashtableAdapter.getHashtableStructure());
	}

	 static ACompositeLoggable convertClassAdapter(
			ClassAdapter classAdapter) {
		// ADynamicRecord record = new ADynamicRecord();
		ALoggableRecord record = new ALoggableRecord();
		ClassProxy realClass =  ReflectUtil.toMaybeProxyTargetClass(classAdapter
				.getRealObject());
		String realClassName = realClass.getName();
		record.setVirtualClass(realClassName);
		int id = nextId++;

		// idToLoggableModel.put(id, classAdapter.getRealObject());
		intIdToLoggableModel.put(id, record);
		objectToIntId.put(classAdapter.getRealObject(), id);
		setObjectId(classAdapter.getRealObject(), record, id);
		/*
		record.setObjectId(id);
		loggableToObject.put(record, classAdapter.getRealObject());
		*/
		//record.setRealObject(classAdapter.getRealObject());
		getProgramComponent().registerAsListener(classAdapter.getRealObject());
		// proxyToClass.put(record, realClass);
		return record;
		// return fillDynamicRecord(classAdapter, record);

	}

	/*
	 * public static ALoggableModel createCompositeLoggableModel(Object
	 * realObject) { //ADynamicRecord record = new ADynamicRecord(); Integer
	 * existingId = objectToId.get(realObject); if (existingId != null) { return
	 * idToLoggableModel.get(existingId); } int id = nextId++; return
	 * createCompositeLoggableModel(realObject, id); //return
	 * fillDynamicRecord(classAdapter, record); }
	 */
	 static ACompositeLoggable createLoggableModel(Object realObject,
			int id) {
		// ADynamicRecord record = new ADynamicRecord();

		ACompositeLoggable model = new ACompositeLoggable();
		ClassProxy realClass =  ReflectUtil.toMaybeProxyTargetClass(realObject);
		String realClassName = realClass.getName();
		model.setVirtualClass(realClassName);

		// idToLoggableModel.put(id, classAdapter.getRealObject());
		setObjectId(realObject, model, id);
		/*
		 * idToLoggableModel.put(id, model); objectToId.put(realObject, id);
		 * model.setObjectId(id); model.setRealObject(realObject);
		 */
		getProgramComponent().registerAsListener(realObject);
		// proxyToClass.put(record, realClass);
		return model;
		// return fillDynamicRecord(classAdapter, record);

	}
	public static ACompositeLoggable createLoggableModel(Object realObject,
			String id) {
		// ADynamicRecord record = new ADynamicRecord();

		ACompositeLoggable model = new ACompositeLoggable();
		ClassProxy realClass =  ReflectUtil.toMaybeProxyTargetClass(realObject);
		String realClassName = realClass.getName();
		model.setVirtualClass(realClassName);

		// idToLoggableModel.put(id, classAdapter.getRealObject());
		setObjectId(realObject, model, id);
		/*
		 * idToLoggableModel.put(id, model); objectToId.put(realObject, id);
		 * model.setObjectId(id); model.setRealObject(realObject);
		 */
		//if (isLocal())

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		getProgramComponent().registerAsListener(realObject);
		// proxyToClass.put(record, realClass);
		return model;
		// return fillDynamicRecord(classAdapter, record);

	}
	public static void setObjectId(Object realObject, ACompositeLoggable model,
			String id) {
		if (realObject == null)
			return;
		//nextId = Math.max(nextId, id + 1);
		stringIdToLoggableModel.put(id, model);
		model.setObjectId(id);
		objectToStringId.put(realObject, id);
		objectToLoggableModel.put(realObject, model);
		//stringIdToObject.put(id, realObject);
		
		/*
		objectToId.put(realObject, id);
		model.setObjectId(id);
		*/
		model.setRealObject(realObject);
		
	}

	 static void setObjectId(Object realObject, ACompositeLoggable model,
			int id) {

		nextId = Math.max(nextId, id + 1);
		//intIdToLoggableModel.put(id, model);
		stringIdToLoggableModel.put("" +id, model);
		objectToIntId.put(realObject, id);
		/*
		model.setObjectId(id);
		model.setRealObject(realObject);
		*/
	}

	 static Object fillDynamicRecord(ClassAdapter classAdapter,
			DynamicRecord record) {

		Vector properties = classAdapter.componentNames();
		// if (properties.size() == 0) return null;
		for (int i = 0; i < properties.size(); i++) {
			String property = (String) properties.elementAt(i);
			Object propertyVal = classAdapter.get(property);
			if (propertyVal != null)
				record.setDynamicProperty(property, convertAdapter(classAdapter
						.get(property)));
		}
		return record;
	}

	 static void convertRecordStructure(RecordStructure recordStructure) {
		Vector properties = recordStructure.componentNames();
		// if (properties.size() == 0) return;
		DynamicRecord record = new ADynamicRecord();
		for (int i = 0; i < properties.size(); i++) {
			String property = (String) properties.elementAt(i);
			record.setDynamicProperty(property, recordStructure.get(property));
		}

	}

	 static void convertVectorStructure(VectorStructure vectorStructure) {

	}

	 static void convertHashtableStructure(
			HashtableStructure hashtableStructure) {

	}

	static UIComponent remoteClientProxy = new AUIComponent();
	static ProgramComponent remoteServerProxy = new AProgramComponent();

	public static UIComponent getUIComponent() {
		return remoteClientProxy;
	}

	public static void setUIComponent(UIComponent newVal) {
		LoggableRegistry.remoteClientProxy = newVal;
	}

	public static Object invokeMethod(ACompositeLoggable parentObject,
			MethodProxy method, Object[] parameterValues) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		return getUIComponent().invokeMethod(parentObject, method,
				parameterValues);
		/*
		 * int objectId = parentObject.getObjectId(); int methodId =
		 * ModelLoggables.getMethodId(method); return
		 * ModelLoggables.getProgramComponent().invokeMethod(objectId, methodId,
		 * parameterValues);
		 */
	}

	 static int getNextMethodId() {
		return nextMethodId++;
	}

	/*
	 * public static int getMethodIntId(MethodProxy method) { Integer retVal =
	 * methodToIntId.get(method); if (retVal == null) { retVal = nextMethodId++;
	 * setMethodId(method, retVal); } return retVal; }
	 * 
	 * public static void setMethodId(MethodProxy method, int methodId) {
	 * methodToIntId.put(method, methodId); intIdToMethod.put(methodId, method); }
	 */
	public static String getMethodStringId(MethodProxy method) {
		if (method == null)
			System.out.println("Null Method to be invoked");
		String retVal = method.toString();
		
		// this is inefficient
		if (stringIdToMethod.get(retVal) == null) {
//			MethodProxy unparsedMethod = stringToMethodProxy(retVal);
//			if (method != unparsedMethod)
//				Message.warning("Unparse does not work");
//			System.out.println("registering " + retVal);
			setMethodId(method, retVal);
		
		}
		return retVal;
	}
	
	
	
	public  static MethodProxy stringToMethodProxy(String id) {
		int parameterStart = id.indexOf("(");
		int parameterEnd = id.indexOf(")");
		int methodNameIndex;
		for (methodNameIndex = parameterStart; id.charAt(methodNameIndex) != '.' && methodNameIndex > 0;methodNameIndex--);		
		if (methodNameIndex < 0)
			return null;
		String methodName = id.substring(methodNameIndex + 1, parameterStart);
		int classNameIndex;
		for (classNameIndex = methodNameIndex - 1; id.charAt(classNameIndex) != ' ' && classNameIndex > 0;classNameIndex--);		
		if (classNameIndex < 0)
			return null;
		String className = id.substring(classNameIndex + 1, methodNameIndex);
		String parameters = id.substring(parameterStart+1, parameterEnd);
		List<String> parameterNameList = parameterTypeNames(parameters);
		Class[] parameterClassList = parameterTypes(parameterNameList);
		MethodProxy methodProxy = toMethodProxy(className, methodName, parameterClassList);
		return methodProxy;
		
	}
	
	static boolean isClassNameChar(char ch) {
		return Character.isLetter(ch) || ch == '.' || ch == '$';
	}
	
	static Map<String, Class> stringToType = new HashMap();
	static {
		stringToType.put("int", Integer.TYPE);
		stringToType.put("double", Double.TYPE);
		stringToType.put("boolean", Boolean.TYPE);
		stringToType.put("short", Short.TYPE);
		stringToType.put("long", Long.TYPE);
		stringToType.put("float", Float.TYPE);
		stringToType.put("char", Character.TYPE);
		stringToType.put("String", String.class);


	}
	public static Class stringToType(String string) {
		Class retVal;
		try {
			retVal = stringToType.get(string);
			if (retVal == null) {
				retVal = Class.forName(string);
				stringToType.put(string, retVal);
			}
			return retVal;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Class[] parameterTypes(List<String> names) {
		Class[] retVal = new Class[names.size()];
		
		try {
		for (int i = 0; i < names.size(); i++) {
//			retVal[i] = stringToType.get(names.get(i));
//			if (retVal[i] == null)
			
			retVal[i] = stringToType(names.get(i));
		}
		return retVal;
		} catch (Exception e) {
			return null;
		}
	}
	
		
	public static List<String> parameterTypeNames(String parameters) {
		
		int start  = 0;
		int stop = 0;
		List<String> retVal = new ArrayList();
		
		while (true) {
			start = stop;
			if (start >= parameters.length())
				break;
			while (!isClassNameChar(parameters.charAt(start)))
				start++;
			if (start >= parameters.length())
				break;
			stop = start;
			while (stop < parameters.length() && isClassNameChar(parameters.charAt(stop)))
				stop++;
			String parameter = parameters.substring(start, stop);
			retVal.add(parameter);
		}
		return retVal;		
	}
	

	
	public static MethodProxy toMethodProxy(String className, String methodName, Class[] parameters) {
		try {
			Class cls = Class.forName(className);
			Method method = cls.getMethod(methodName, parameters);
			MethodProxy retVal = AVirtualMethod.virtualMethod(method);
			return retVal;
			
			
		} catch (Exception e) {
			return null;
		}		
	}

	 static MethodProxy getMethod(int id) {
		return intIdToMethod.get(id);
	}

	 static void setMethodId(MethodProxy method, String methodId) {
		// methodToStringId.put(method, methodId);
		
		 try {
			 stringIdToMethod.put(methodId, method);
			LoggableRegistry.setMethodReturnsValue( method );
		 }
		 catch ( Exception e ) {
			 System.out.println( methodId );
		 }
	}

	public static ProgramComponent getProgramComponent() {
		return remoteServerProxy;
	}

	public static void setProgramComponent(ProgramComponent remoteServerProxy) {
		LoggableRegistry.remoteServerProxy = remoteServerProxy;
	}

	 static ACompositeLoggable getLoggableModel(int objectId) {
		return intIdToLoggableModel.get(objectId);
	}
	
	public static ACompositeLoggable getLoggableModel(String objectId) {
		return stringIdToLoggableModel.get(objectId);
	}

	 static int getObjectIntId(Object object) {
		return objectToIntId.get(object);
	}
	public static void setMethodReturnsValue(MethodProxy method) {
		if (method == null)
			return;
		if ( method.getReturnType() == method.getReturnType().voidType() ) {
			return;
		}
		// int methodId = getMethodIntId(method);
		String methodId = getMethodStringId(method);
		returnsValue.add(methodId);
	}

	public static boolean methodReturnsValue(MethodProxy method) {
		// int methodId = getMethodIntId(method);
		String methodId = getMethodStringId(method);
		return methodReturnsValue(methodId);
	}

	/*
	 * public static boolean methodReturnsValue (int methodId) { return
	 * returnsValue.contains(methodId); }
	 */
	public static boolean methodReturnsValue(String methodId) {
		return returnsValue.contains(methodId);
	}

	public static void setMethodIsReadOnly(MethodProxy method) {
		if (method == null)
			return;
		String methodId = getMethodStringId(method);
		isReadOnly.add(methodId);
	}

	public static boolean isMethodReadOnly(MethodProxy method) {
		String methodId = getMethodStringId(method);
		return isMethodReadOnly(methodId);
		// return isReadOnly.contains(methodId);
	}

	public static void setMethodIsReadOnly(String methodId) {
		isReadOnly.add(methodId);
	}

	public static boolean isMethodReadOnly(String methodId) {
		return isReadOnly.contains(methodId);
	}

	 static ANotificationBus getNotificationBus() {
		return notificationBus;
	}

	 static void setNotificationBus(ANotificationBus newVal) {
		LoggableRegistry.notificationBus = newVal;
	}

	public static Set models() {
		return objectToIntId.keySet();
	}

	/*
	 * public static Hashtable<Object, Integer> getObjectToId() { return
	 * objectToId; }
	 * 
	 * public static void setObjectToId(Hashtable<Object, Integer> objectToId) {
	 * ModelLoggables.objectToId = objectToId; }
	 * 
	 * public static Hashtable<Integer, Object> getIdToObject() { return
	 * idToObject; }
	 * 
	 * public static void setIdToObject(Hashtable<Integer, Object> idToObject) {
	 * ModelLoggables.idToObject = idToObject; }
	 */
	static String hostId;
	static String hostName;
	static String userName;

	public static void setHostId(String newVal) {
		hostId = newVal;
	}
	
	public static void setHostName(String newVal) {
		hostName = newVal;
		hostId = getHostId(hostName, userName);
		
	}

	public static void setUserName(String userName) {
		hostId = getHostId(hostName, userName);
	}

	public static String getHostId() {
		return getHostId(hostName, userName);
	}
	public static String getHostId(String hostName, String userName) {
		if (userName == null)
			return hostName;
		return userName + "@" + hostName;
	}
	
	public static String getObjectId(String hostId, int nextVal) {
		return hostId + "#" + nextVal;
	}
	
	public static int getIntId (String objectId) {
		int idStart = objectId.indexOf("#") + 1;
		String idString = objectId.substring(idStart);
		try {
			return Integer.parseInt(idString);
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static void resetObjectId(String objectId) {
		int intId = getIntId(objectId);
		nextId = Math.max(intId + 1, nextId);
	}

	public static String getHostName() {
		return hostName;
	}

	public static String getUserName() {
		return userName;
	}
	
	public static int getNextId() {
		return nextId;
	}
	public static void setNextId(int newVal) {
		nextId = newVal;
	}
	public static void mapLoggableToAdapter (Object loggable, ObjectAdapter adapter) {
		if (loggable instanceof ACompositeLoggable) {
			mapLoggableToAdapter((ACompositeLoggable) loggable, adapter);
		}
	}
	public static void mapLoggableToAdapter (ObjectAdapter adapter) {
		mapLoggableToAdapter(adapter.getRealObject(), adapter);
	}
	
	public static void mapLoggableToAdapter (ACompositeLoggable loggable, ObjectAdapter adapter) {
		/*
		Set adapters = loggableToAdapter.get(loggable);
		if (adapters == null) {
			adapters = new HashSet();
			loggableToAdapter.put(loggable, adapters);			
		}
		adapters.add(adapter);
		*/
		String path = adapter.getPath();
		stringIdToLoggableModel.put(path, loggable);
		loggable.addObjectId(path);
	
		
	}
	public static void reMapLoggableToAdapter (ObjectAdapter adapter,
			AnIdentifiableLoggable oldLoggable, 
			ACompositeLoggable newLoggable 
			) {
		unMapLoggableFromAdapter(oldLoggable, adapter);
		mapLoggableToAdapter(newLoggable, adapter);
		
		
	
		
	}
	public static void unMapLoggableFromAdapter (Object loggable, ObjectAdapter adapter) {
		
		if (loggable instanceof ACompositeLoggable)
			unMapLoggableFromAdapter((ACompositeLoggable) loggable, adapter); 
	}
	
	public static void unMapLoggableFromAdapter (ObjectAdapter adapter) {
		
		
			unMapLoggableFromAdapter(adapter.getRealObject(), adapter); 
	}
	
	public static void unMapLoggableFromAdapter (AnIdentifiableLoggable loggable, ObjectAdapter adapter) {
		
		
		String path = adapter.getPath();
		//map will do this
		
		ACompositeLoggable existing = stringIdToLoggableModel.get(path);
		if (existing == loggable)
			stringIdToLoggableModel.remove(path);
			
		loggable.removeObjectId(path);
	}

	static {
		getUIComponent().setProgramComponent(getProgramComponent());
		getProgramComponent().setUIComponent(getUIComponent());
		try {
			hostName = InetAddress.getLocalHost().toString();
		} catch (Exception e) {
			hostName = "";
		}
		hostId = hostName;
	}
	static boolean isLocal = true;
	public static boolean isLocal() {
		return isLocal;
	}
	public static void  setIsLocal(boolean newVal) {
		isLocal = newVal;
	}
	public static boolean isPrimitive (ClassProxy[] argTypes) {
		for (int i = 0; i < argTypes.length; i++)
			if (!isPrimitive(argTypes[i]))
				return false;
		return true;
	}

}
