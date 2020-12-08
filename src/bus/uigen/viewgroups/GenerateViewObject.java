package bus.uigen.viewgroups;

import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import shapes.RemoteShape;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.PropertyDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTypeRegistry;
import bus.uigen.sadapters.HashtableStructure;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class GenerateViewObject {
	
	 static Hashtable viewMethods = new Hashtable();
	 public static void registerViewMethod(String className, 
			 String methodName) {
		 viewMethods.put(className, methodName);
	 }
	 public static Object getViewObject( Object object, boolean textMode) {
		 return getViewObject (null, object, textMode, null);
		 
	 }
	 public static Object generate( Object object) {
		 return getViewObject (object, true);
		 
	 }
	 
	 
	 // Check to see if the object has declared a view
	  // by implementing a method of the signature
	  // public Object uigenView(void)
	  // If so, invoke this method and return the viewobject
	  // Otherwise return the original object
	  public static Object getViewObject(CompositeAdapter parentAdapter, Object object, boolean textMode, String property) {
		  if (object == null) {
			  //System.err.println ("returning null for view object");
				return null;
		  }
	    ClassProxy c = RemoteSelector.getClass(object);
	    String viewMethodName = (String) viewMethods.get(c.getName());
		//if (uiBean.isPoint(object))
					   //return uiBean.toPointModel(object);
		
		//ShapeModel shape = uiBean.toShapeModel(object);
	    RemoteShape shape = null;
	    if (!textMode)
	    	 shape = IntrospectUtility.toShapeModel(object);
		if (!textMode && shape != null)
			return shape;
	    else if (viewMethodName == null) 
	      viewMethodName = IntrospectUtility.VIEW_GETTER;
		try {
			MethodProxy viewMethod = c.getMethod(viewMethodName,
													null);
			if (viewMethod != null) {
				//System.err.println("found" + viewMethod);
				Object viewObject = viewMethod.invoke(object, null);
				//System.err.println("uigen view" + viewObject);
				return getViewObject (parentAdapter, viewObject, textMode, property);
			} 
						   
		} catch (Exception e) {
			//ssem.out.println("try failed for view method" + e);
			//return object;
		}
		uiFrame frame = null;
		if (parentAdapter != null)
			frame = parentAdapter.getUIFrame();
		ConcreteType concreteType = ConcreteTypeRegistry.createConcreteType(c, object, frame);
		Object unNestedRecordHTTupleList = getUnNestedRecordHTTupleList(parentAdapter, object,concreteType, null, c);
		if (unNestedRecordHTTupleList != null) {
			return unNestedRecordHTTupleList;
		}
		Object unNestedMatrixObject = getUnNestedMatrix(parentAdapter, object, concreteType, null, c);
		if (unNestedMatrixObject != null)
			return unNestedMatrixObject;
		//return object;
		Object nestedRelation = getNestedRelationObject(parentAdapter, object, concreteType, null, c);
		if (nestedRelation != null)
			return nestedRelation;
		return getViewGroupObject(parentAdapter, object, c);
		/*
		
		//System.err.println("no view method");
		if (object instanceof Vector) return object;
		if (object instanceof Hashtable) return object;
		if (isPrimitiveClass(c)) return object;
		ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(c);
		PropertyDescriptor properties[] = cdesc.getPropertyDescriptors();
		FieldDescriptor reflectedFields[] = cdesc.getFieldDescriptors();
		if (reflectedFields.length >  1) return object;
		Vector propertyValues = propertyValues(properties, object);
		if (reflectedFields.length + propertyValues.size() != 1) return object;
		if (reflectedFields.length == 1) {
			try {
				return reflectedFields[0].getField().get(object);
			} catch (Exception e) {
				return object;
			}
		} else {
			//System.err.println("returning singleton property");
			return propertyValues.elementAt(0);
		}
		*/
		
	    
	  }
	  public static Object getViewObject(Object object) {
		  return getViewObject (null, object, null);
		  
	  }
	  public static Object getViewObject(CompositeAdapter parentAdapter, Object object, String property) {
		  if (object == null) {
			  //System.err.println ("returning null for view object");
				return null;
		  }
	    //Class c = object.getClass();
//		  ClassProxy c = RemoteSelector.getClass(object);
		  ClassProxy c = ReflectUtil.toMaybeProxyTargetClass(object);
	    String viewMethodName = (String) viewMethods.get(c.getName());
		//if (uiBean.isPoint(object))
					   //return uiBean.toPointModel(object);
	    if (viewMethodName == null) 
	      viewMethodName = "uigenView";
		try {
			//Method viewMethod = c.getDeclaredMethod(viewMethodName,
													//null);
			MethodProxy viewMethod = c.getMethod(viewMethodName);
			if (viewMethod != null) {
				//System.err.println("found" + viewMethod);
				Object viewObject = viewMethod.invoke(object, null);
				//System.err.println("uigen view" + viewObject);
				return getViewObject (parentAdapter, viewObject, uiGenerator.textMode(), null);
			} 
						   
		} catch (Exception e) {
			//ssem.out.println("try failed for view method" + e);
			//return object;
		}
		ConcreteType realConcreteType = ConcreteTypeRegistry.createConcreteType(c, object, null);
		//ConcreteType realConcreteType = ConcreteTypeRegistry.createConcreteType(c, object, parentAdapter.getUIFrame());
		Object unNestedRecordHTTupleList = getUnNestedRecordHTTupleList(parentAdapter, object, realConcreteType, property, c);
		if (unNestedRecordHTTupleList != null) {
			return unNestedRecordHTTupleList;
		}
		//return object;
		
		Object unNestedMatrixObject = getUnNestedMatrix(parentAdapter, object, realConcreteType, property, c);
		if (unNestedMatrixObject != null)
			object = unNestedMatrixObject;
			//return unNestedMatrixObject;
		
		Object nestedRelation = getNestedRelationObject(parentAdapter, object, realConcreteType, property, c);
		if (nestedRelation != null)
			return nestedRelation;
		
		Object vectorNavigator = getVectorNavigatorObject(parentAdapter, object, realConcreteType, property, c);
		if (vectorNavigator != null)
			return vectorNavigator;
		
		return getViewGroupObject(parentAdapter, object, c);
		//return object;
		/*
		
		//System.err.println("no view method");
		if (object instanceof Vector) return object;
		if (object instanceof Hashtable) return object;
		if (isPrimitiveClass(c)) return object;
		ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(c);
		PropertyDescriptor properties[] = cdesc.getPropertyDescriptors();
		FieldDescriptor reflectedFields[] = cdesc.getFieldDescriptors();
		if (reflectedFields.length >  1) return object;
		Vector propertyValues = propertyValues(properties, object);
		if (reflectedFields.length + propertyValues.size() != 1) return object;
		if (reflectedFields.length == 1) {
			try {
				return reflectedFields[0].getField().get(object);
			} catch (Exception e) {
				return object;
			}
		} else {
			//System.err.println("returning singleton property");
			return propertyValues.elementAt(0);
		}
		*/
		
	    
	  }
	  public static char NESTING_DELIMITER = '/';
	  public static String PARENT_INDICATOR = "../";
	  public static String VIEW_NAME = "view";
	  /*
	  public static Hashtable addChildNode(Hashtable parentNode, String child) {
		  if (parentNode.get(child) == null) {
			  Hashtable newNode = new Hashtable();
			  parentNode.put(child, newNode);
		  }
		  return (Hashtable) parentNode.get(child);
		  
		  
	  }
	  */
	  public static APropertyAndCommandFilter addChildNode(String child, 
			  APropertyAndCommandFilter parentNode, String virtualClassName) {
		  if (parentNode.getPropertyGroup(child) == null) {
			  APropertyAndCommandFilter newNode = new APropertyAndCommandFilter();
			  newNode.setVirtualClass(virtualClassName);
			  parentNode.addPropertyGroup(child, newNode);
		  }
		  return parentNode.getPropertyGroup(child); 
		  
	  }
	  /*
	  public static void parseGroupName (String name, Hashtable groups) {
		  int leftMark = 0;
		  int rightMark = 0;
		  Hashtable curNode = groups;		  
		  while (true) {
		  rightMark = name.indexOf(NESTING_DELIMITER, leftMark);
		  if (rightMark < 0 || rightMark >= name.length()) {
			  rightMark = name.length();			  
		  }
		  String group = name.substring(leftMark, rightMark);
		  curNode = addChildNode(curNode, group);
		  if (rightMark >= name.length())
			  return;
		  leftMark = rightMark + 1;
		  }		  
		  
	  }
	  */
	  public static APropertyAndCommandFilter  parseGroupName (String name, APropertyAndCommandFilter root) {
		  int leftMark = 0;
		  int rightMark = 0;
		  APropertyAndCommandFilter curNode = root;	
		  String fullName = root.getVirtualClass();
		  while (true) {
		  rightMark = name.indexOf(NESTING_DELIMITER, leftMark);
		  if (rightMark < 0 || rightMark >= name.length()) {
			  rightMark = name.length();			  
		  }
		  String group = name.substring(leftMark, rightMark);
		  /*
		  if (fullName == null)
			  fullName = group;
		  else
		  */
			  fullName = fullName + NESTING_DELIMITER + group;
		  curNode = addChildNode(group, curNode, fullName);
		  if (rightMark >= name.length())
			  return curNode;
		  leftMark = rightMark + 1;
		  }		  
		  
	  }
	  public static int ancestorLevel (String name) {
		  int level = 0;
		  int leftMark = 0;
		  int rightMark = 0;
		  while (true) {
			  rightMark = name.indexOf(PARENT_INDICATOR, leftMark);
			  if (rightMark < 0 || rightMark >= name.length()) {
				  return level;			  
			  }
			  level ++;			 
			  leftMark = rightMark + 1;
		  }	
	  }
	  public static Object  getViewObject (int ancestorLevel, Object selfViewObject, CompositeAdapter parentAdapter) {
		  Object retVal = selfViewObject;
		  CompositeAdapter ancestorAdapter = parentAdapter;
		  for (int i = 0; i < ancestorLevel; i++) {
			  if (ancestorAdapter == null) break;
			  retVal = ancestorAdapter.computeAndMaybeSetViewObject();
			  ancestorAdapter = ancestorAdapter.getParentAdapter();			  
		  }
		  return retVal;
		 
		  
	  }
	  public static String removeAncestorIndicators (String name, int level) {
		  return name.substring(PARENT_INDICATOR.length()*level);
	  }
	  public static Object getNestedRelationObject(CompositeAdapter parentAdapter, Object realObject,
			  Object concreteType, String property, ClassProxy realClass) {
//		  ClassProxy realClass = RemoteSelector.getClass(realObject);
		  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(realClass, realObject);
		  Boolean isNestedRelation = (Boolean) cd.getAttribute(AttributeNames.NESTED_RELATION);
		  if (isNestedRelation == null)
			  return null;
		  if (!isNestedRelation) 
			  return null;	
		  //ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(RemoteSelector.getClass(realObject), realObject, parentAdapter.getUIFrame());
		  if (!(concreteType instanceof VectorStructure)) {
				System.err.println("E**" + realClass + " is not an indexed list");
				return null;
			}
		  String primaryProperty = (String) cd.getPropertyAttribute(AttributeNames.ANY_ELEMENT, AttributeNames.PRIMARY_PROPERTY);
		  if (primaryProperty == null) {
			  System.err.println("E** no primary property  given for" + realClass);
			  return null;
		  }
		  
		  VectorStructure vectorStructure = (VectorStructure) concreteType;
		  ANestedRelationModel nestedRelation = new ANestedRelationModel(vectorStructure,  parentAdapter.getUIFrame(), parentAdapter);
		  isNestedRelation = nestedRelation.makeTable(primaryProperty);
		  if (!isNestedRelation)
			  return null;
		  else {
			  nestedRelation.addProperties(realObject, cd.getPropertyDescriptors());
			  return nestedRelation;
		  }
		 //return null;
	  }
	  public static Object getVectorNavigatorObject(CompositeAdapter parentAdapter, Object realObject, Object concreteType, String property, ClassProxy realClass) {
//		  ClassProxy realClass = RemoteSelector.getClass(realObject);
//		  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(realClass, realObject);
//		  Boolean isVectorNavigator = (Boolean) cd.getAttribute(AttributeNames.VECTOR_NAVIGATOR);
		  
		  Boolean isVectorNavigator = (Boolean) ObjectAdapter.getAttribute(parentAdapter, realClass, AttributeNames.VECTOR_NAVIGATOR, property);
		  

		  if (isVectorNavigator == null)
			  return null;
		  if (!isVectorNavigator) 
			  return null;	
		  //Integer vectorNavigatorSize = (Integer) cd.getAttribute(AttributeNames.VECTOR_NAVIGATOR_SIZE);
		  Integer vectorNavigatorSize = (Integer) ObjectAdapter.getAttribute(parentAdapter, realClass, AttributeNames.VECTOR_NAVIGATOR_SIZE, property);
		  Boolean showVectorNavigatorCommands = (Boolean) ObjectAdapter.getAttribute(parentAdapter, realClass, AttributeNames.SHOW_VECTOR_NAVIGATION_COMMANDS, property);
		  if (vectorNavigatorSize == null)
			  vectorNavigatorSize = 6;
		  //ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(RemoteSelector.getClass(realObject), realObject, parentAdapter.getUIFrame());
		  if (!(concreteType instanceof VectorStructure)) {
				System.err.println("E**" + realClass + " is not an indexed list");
				return null;
			}		  
		  
		  VectorStructure vectorStructure = (VectorStructure) concreteType;
		 return new AVectorNavigator(vectorStructure, vectorNavigatorSize, showVectorNavigatorCommands );
		  
		 //return null;
	  }
	  static HashtableStructure getHashtableStructure (RecordStructure record, CompositeAdapter parentAdapter) {
		  if (record instanceof HashtableStructure)
			  return (HashtableStructure) record;
		  Vector<String> componentNames = record.componentNames();
		  for (int i = 0; i < componentNames.size(); i++) {
			  String componentName = componentNames.elementAt(i) ;
			  Object childObject = record.get(componentName);
			  ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(RemoteSelector.getClass(childObject), childObject, parentAdapter.getUIFrame());
			  if (concreteType instanceof HashtableStructure)
				  return (HashtableStructure) concreteType;
		  }
		  return null;
		  
	  }
	  public static Object getUnNestedRecordHTTupleList(CompositeAdapter parentAdapter, Object realObject, 
			  ConcreteType concreteType, String property, ClassProxy realClass) {
//		  ClassProxy realClass = RemoteSelector.getClass(realObject);
//		  ClassProxy realClass = IntrospectUtility.toMaybeProxyTargetClass(realObject);
		  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(realClass, realObject);
		  Boolean isUnNestedRelation = (Boolean) cd.getAttribute(AttributeNames.UN_NEST_HT_RECORD);
		  if (isUnNestedRelation == null)
			  return null;
		  if (!isUnNestedRelation) 
			  return null;	
		  //ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(RemoteSelector.getClass(realObject), realObject, parentAdapter.getUIFrame());
		  if (!(concreteType instanceof RecordStructure)) {
				System.err.println("E**" + realClass + " is not an indexed list");
				return null;
			}
		  RecordStructure record = (RecordStructure) concreteType;
		  HashtableStructure hashTable = getHashtableStructure(record, parentAdapter);
		  if (hashTable == null) {
			  System.err.println("E** no hashtable in" + realClass);
			  return null;
		  }
		  AnUnNestedRecordHTTupleList retVal = new AnUnNestedRecordHTTupleList(record, hashTable, parentAdapter.getUIFrame(), parentAdapter);
		  return retVal;	  
	  }
	  public static Object getUnNestedMatrix(CompositeAdapter parentAdapter, Object realObject, ConcreteType concreteType, String property, ClassProxy realClass) {
//		  ClassProxy realClass = RemoteSelector.getClass(realObject);
		  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(realClass, realObject);
		  Boolean isUnNestedMatrix = (Boolean) cd.getAttribute(AttributeNames.UN_NEST_MATRIX);
		  if (isUnNestedMatrix == null)
			  return null;
		  if (!isUnNestedMatrix) 
			  return null;	
		  //ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(RemoteSelector.getClass(realObject), realObject, parentAdapter.getUIFrame());
		  if (!(concreteType instanceof VectorStructure)) {
				System.err.println("E**" + realClass + " is not a matrix");
				return null;
			}
		  
		  VectorStructure vector = (VectorStructure) concreteType;
		  
		  if (vector.size() == 0) {
			  System.err.println("E*** matrix is null");
				return null;
		  }
		  Object origChild = vector.elementAt(0);
		  Object child = getViewObject(parentAdapter, origChild, null);
		  
		  
		  //Class childClass = vector.addableElementType();
		  ClassProxy childClass = RemoteSelector.getClass(child);
		  if (childClass == null) {
			  System.err.println("E** vector child class is null");
				return null;
		  }
		  ConcreteType childType= ConcreteTypeRegistry.createConcreteType(childClass, child, parentAdapter.getUIFrame());
		  if (!(childType instanceof VectorStructure)) {
				System.err.println("E**" + realClass + " is not a matrix");
				return null;
			}
			
		 
		  AnUnNestedMatrix retVal = new AnUnNestedMatrix(vector, parentAdapter.getUIFrame(), parentAdapter);
		  return retVal;	  
	  }
	  static boolean addProperty(String name, Object realObject, Object viewObject, PropertyDescriptorProxy pd, CompositeAdapter parentAdapter) {
		  int ancestorLevel = ancestorLevel(name);
		  Object targetViewObject = getViewObject (ancestorLevel, viewObject, parentAdapter);
		  if (!(targetViewObject instanceof APropertyAndCommandFilter)) return false;
		  String propertyName = removeAncestorIndicators(name, ancestorLevel);
		  APropertyAndCommandFilter groupNode = parseGroupName(name, (APropertyAndCommandFilter)targetViewObject);
		  //foundViewGroup = true;
		  groupNode.addProperty(realObject, pd);
		  return true;
	  }
	  static boolean addMethod (String name, Object realObject, Object viewObject, MethodDescriptorProxy method, CompositeAdapter parentAdapter) {
		  int ancestorLevel = ancestorLevel(name);
		  Object targetViewObject = getViewObject (ancestorLevel, viewObject, parentAdapter);
		  if (!(targetViewObject instanceof APropertyAndCommandFilter))
		  return false;
		  String propertyName = removeAncestorIndicators(name, ancestorLevel);
		  APropertyAndCommandFilter groupNode = parseGroupName(name, (APropertyAndCommandFilter)targetViewObject);
		  //foundViewGroup = true;
		  groupNode.addMethod(realObject, method);
		  return true;
	  }
	  public static Object getViewGroupObject(CompositeAdapter parentAdapter, Object realObject, ClassProxy realClass) {
		  if (realObject == null) return null;
		  if (realObject instanceof APropertyAndCommandFilter) return realObject;
		  APropertyAndCommandFilter viewObject = new APropertyAndCommandFilter();
		  //viewObject.setVirtualClass(RemoteSelector.getClass(realObject).getName()+NESTING_DELIMITER+VIEW_NAME);
		  viewObject.setVirtualClass(realClass.getName()+NESTING_DELIMITER+VIEW_NAME);
		  Hashtable<String, Vector> groups = new Hashtable();
		  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(realClass, realObject);
		  PropertyDescriptorProxy[] properties = cd.getPropertyDescriptors();
		  boolean foundViewGroup = false;
		  //APropertyAndCommandFilter commonNode = parseGroupName("COMMON", (APropertyAndCommandFilter)viewObject);
		  for (int i = 0; i < properties.length; i++) {
			  PropertyDescriptorProxy pd = properties[i];
			  if (pd == null) break;
			  String name = (String) pd.getValue(AttributeNames.VIEW_GROUP);
			  List names =  (List) pd.getValue(AttributeNames.VIEW_GROUPS);
			  /*
			  if (commonNode != null)
				  commonNode.addProperty(realObject, pd);
				  */
			  //if (name == null )
			  if (name == null && ((names == null) || names.isEmpty()))
				  viewObject.addProperty(realObject, pd);
			  else {
				  /*
				  int ancestorLevel = ancestorLevel(name);
				  Object targetViewObject = getViewObject (ancestorLevel, viewObject, parentAdapter);
				  if (!(targetViewObject instanceof APropertyAndCommandFilter)) continue;
				  String propertyName = removeAncestorIndicators(name, ancestorLevel);
				  APropertyAndCommandFilter groupNode = parseGroupName(name, (APropertyAndCommandFilter)targetViewObject);
				  foundViewGroup = true;
				  groupNode.addProperty(realObject, pd);
				  */
				  foundViewGroup = addProperty(name, realObject, viewObject, pd, parentAdapter);
				  if (names == null)
					  continue;
				  for (int nameIndex = 0; nameIndex < names.size(); nameIndex++)
					  foundViewGroup = addProperty((String) names.get(nameIndex), realObject, viewObject, pd, parentAdapter);
			  }
			  
			  
		  }
		  MethodDescriptorProxy[] methods = cd.getMethodDescriptors();
		  if (methods == null)
			  return realObject;
		  for (int i = 0; i < methods.length; i++) {
			  MethodDescriptorProxy method =  methods[i];
			  String name = (String) method.getValue(AttributeNames.VIEW_GROUP);
			  List names =  (List)method.getValue(AttributeNames.VIEW_GROUPS);
			//if (name == null )
			  if (name == null && ((names == null) || names.isEmpty()))
				  viewObject.addMethod(realObject, method);
			  else {
				  /*
				  int ancestorLevel = ancestorLevel(name);
				  Object targetViewObject = getViewObject (ancestorLevel, viewObject, parentAdapter);
				  if (!(targetViewObject instanceof APropertyAndCommandFilter)) continue;
				  String propertyName = removeAncestorIndicators(name, ancestorLevel);
				  APropertyAndCommandFilter groupNode = parseGroupName(name, (APropertyAndCommandFilter)targetViewObject);
				  foundViewGroup = true;
				  groupNode.addMethod(realObject, method);
				  */
				  foundViewGroup = addMethod(name, realObject, viewObject, method, parentAdapter);
				  if (names == null)
					  continue;
				  for (int nameIndex = 0; nameIndex < names.size(); nameIndex++)
					  foundViewGroup = addMethod((String) names.get(nameIndex), realObject, viewObject, method, parentAdapter);
			  }
			  
			  
		  }
		  
		  if (foundViewGroup) return viewObject;
		  else return realObject;
		  
	  }
	  
	  
	

}
