package bus.uigen.sadapters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import util.annotations.StructurePatternNames;
import bus.uigen.uiFrame;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.DynamicMethods;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.trace.ConcreteStructureNodeCreated;
import bus.uigen.trace.SubstitutionOfDeclaredPattern;
import bus.uigen.trace.UndeclaredPattern;
public class ConcreteTypeRegistry
{
	static boolean cloneAdapters = true;
	static boolean usePatternAnnotations = true;
	static Map<ClassProxy, AbstractConcreteType> classToPrototypeConcreteType = new HashMap();
	static Set<ClassProxy> dynamicClasses = new HashSet();
	static Map<String, ConcreteTypeFactory> nameToConcreteTypeFactory = new HashMap();
	static Map<ClassProxy, ConcreteTypeFactory> classToConcreteTypeFactory = new HashMap();
	static Map<ClassProxy, String> classToPatternName = new HashMap();
//	static ConcreteTypeFactory primitiveFactory;
	static void registerPatternName(ConcreteTypeFactory newValue) {
		String name = newValue.getPatternName();
		if (name == null) return;
		nameToConcreteTypeFactory.put(name, newValue);
		
	}
	static void addLast(Vector v, ConcreteTypeFactory newValue) {
		if (v.contains(newValue)) return;
		v.addElement(newValue);
		registerPatternName(newValue);
		
	}
	static void addFirst(Vector v, ConcreteTypeFactory newValue) {
		if (v.contains(newValue)) return;
		v.insertElementAt (newValue, 0);
		registerPatternName(newValue);
	}
	public static void setCloneAdapters (boolean newVal) {
		cloneAdapters = newVal;
	}
	public static void setUsePatternAnnotations (boolean newVal) {
		usePatternAnnotations = newVal;
	}
	static Vector concreteTypeRegistry = new Vector();	
	public static void addLast(ConcreteTypeFactory newValue) {
		addLast(concreteTypeRegistry, newValue);
		
		
	}
	public static void addFirst(ConcreteTypeFactory newValue) {
		addFirst(concreteTypeRegistry, newValue);
		
	}	
	public static void removeElement(ConcreteTypeFactory oldValue) {
		concreteTypeRegistry.removeElement(oldValue);
	}
	public static ConcreteTypeFactory elementAt(int index) {
		return (ConcreteTypeFactory) concreteTypeRegistry.elementAt(index);
	}
	public static int size(){
		return  concreteTypeRegistry.size();
	}
	static boolean objectEditorHasRegistered = false;
	static boolean isDynamicClass(ClassProxy c) {		
		boolean retVal = dynamicClasses.contains(c);
		if (retVal)
			return retVal;
		retVal = DynamicMethods.getDynamicPropertyGetter(c) != null;
		if (retVal) {
			dynamicClasses.add(c);
		}
		return retVal;
	}
	public static void registerConcreteType (ClassProxy c, ConcreteType concreteType){
		if (concreteType == null)
			return;
		if (cloneAdapters && !isDynamicClass(c)) {
			classToPrototypeConcreteType.put (c, (AbstractConcreteType) concreteType);
			
		}
		
	}
	
//	public static void registerPrimitiveFactory(ConcreteTypeFactory aConcreteTypeFactory) {
//		primitiveFactory = aConcreteTypeFactory;
//	}
	public static ConcreteType createConcreteType(ConcreteTypeFactory factory, ClassProxy c, Object o, uiFrame f, boolean forceConversion) {
//		if (!objectEditorHasRegistered) {
//			ObjectEditor.register();
//			objectEditorHasRegistered = true;
//		}
		ConcreteType retVal = getPrototypeClone(c, o, f);
		if (retVal != null)
			return retVal;
		retVal = factory.toConcreteType (c, o, f, forceConversion);
		registerConcreteType(c, retVal);
		ConcreteStructureNodeCreated.newCase(retVal, ConcreteTypeRegistry.class);
		return retVal;
		
	}
	public static ConcreteType getPrototypeClone(ClassProxy c, Object o, uiFrame f) {
//		if (o == null)
//			return null;
		ConcreteType retVal = null;
		AbstractConcreteType prototype = classToPrototypeConcreteType.get(c);
		if (prototype != null) {
			retVal = (ConcreteType) prototype.clone();
			retVal.setTarget(o);
			retVal.setFrame(f);
		}
		return retVal;
	}
	static EnumToEnumerationFactory enumFactory = new EnumToEnumerationFactory();
	static ArrayToVectorStructureFactory arrayFactory = new ArrayToVectorStructureFactory();
	static VectorToVectorStructureFactory vectorFactory = new VectorToVectorStructureFactory();
	//static VectorToVectorStructureFactory listfactory = new ArrayListToVectorStructureFactory();
	static NoPatternFactory noPatternFactory =  new NoPatternFactory();
	
	static String getName (ClassProxy c, util.annotations.StructurePattern pattern) {
		if (pattern != null)
			return pattern.value();
		else {
			if (EnumToEnumerationFactory.isEnumeration(c))
			return enumFactory.getPatternName();
			else if (c.isArray())
				return arrayFactory.getPatternName();
			else if (c == c.vectorClass())
				return vectorFactory.getPatternName();
			else if (c == c.objectClass())
				return noPatternFactory.getPatternName();
		}
		return null;
	}
	
	public static ConcreteTypeFactory getConcreteTypeFactoory  (ClassProxy c, Object o) {
		ClassProxy targetClass = ACompositeLoggable.getTargetClass(o);

	
		ConcreteTypeFactory  classFactory = classToConcreteTypeFactory.get(targetClass);
		if (classFactory != null) {
			return classFactory;
		}
		String name = null;
		name = classToPatternName.get(c);
		if (name == null) {

			util.annotations.StructurePattern pattern = AClassDescriptor
					.getPatternAnnotationInSuperTypesOf(c);

			name = getName(c, pattern);

			if (name != null) {
				classToPatternName.put(c, name);
			}
			if (name == null) {

				return null;
			}
		}
		
		
		
		
//		if (pattern == null && usePatternAnnotations) {
//			if (EnumToEnumerationFactory.isEnumeration(c))
//				name = enumFactory.getPatternName();
//			
//		}
		return nameToConcreteTypeFactory.get(name);
		
		
	}
	public static ConcreteType createConcreteType(ClassProxy c, Object o, uiFrame f) {
//		if (!objectEditorHasRegistered) {
//			ObjectEditor.register();
//			objectEditorHasRegistered = true;
//		}
//		if (o == null) {
//			System.out.println("crate concrete type for null");
//		}
//		ConcreteType retVal = null;
//		AbstractConcreteType prototype = classToPrototypeConcreteType.get(c);
//		if (prototype != null) {
//			retVal = (ConcreteType) prototype.clone();
//			retVal.setTarget(o);
//			retVal.setFrame(f);
//			return retVal;
//		}
		ClassProxy targetClass = ACompositeLoggable.getTargetClass(o);
		ConcreteType retVal = getPrototypeClone(targetClass, o, f);
		if (retVal != null)
			return retVal;
//		util.annotations.StructurePattern pattern = AClassDescriptor.getPatternAnnotationInSuperTypesOf(c);
//		util.annotations.StructurePattern pattern = c.getAnnotation(util.annotations.StructurePattern.class);
	//	adding optimization for vector class
		ConcreteTypeFactory  classFactory = classToConcreteTypeFactory.get(targetClass);
		if (classFactory != null) {
			retVal = createConcreteType(classFactory, c, o, f, false);
			 registerConcreteType(targetClass, retVal);
			 return retVal;
		}
		String name = null;
		name = classToPatternName.get(c);
		if (name == null) {
		
		util.annotations.StructurePattern pattern = AClassDescriptor.getPatternAnnotationInSuperTypesOf(c);
		
		name = getName (c, pattern);
		if (name != null)
			classToPatternName.put(c, name);
		}
		
		if (name == null) {
			util.annotations.DisplayToString displayToString = (util.annotations.DisplayToString) c
					.getAnnotation(util.annotations.DisplayToString.class);
			if (displayToString != null && displayToString.value())
				name = StructurePatternNames.NO_PATTERN;
		}
		
//		if (pattern == null && usePatternAnnotations) {
//			if (EnumToEnumerationFactory.isEnumeration(c))
//				name = enumFactory.getPatternName();
//			
//		}
		if (name != null && usePatternAnnotations ) {
//			if (name == null)
//			 name = pattern.value();
//			
			//if (name != null) {
				ConcreteTypeFactory factory = nameToConcreteTypeFactory.get(name);
				 if (factory == null) {
					 util.trace.Tracer.error("No factory registered for " + name);
				 }
				 retVal = createConcreteType(factory, c, o, f, true);
				 if (retVal == null) {
					 util.trace.Tracer.error("Class:" + c.getName() + " does not follow declared pattern: " + name  + ". Ignoring pattern declaration.");
				 }  else {
//				 if (retVal != null) {
				 registerConcreteType(targetClass, retVal);
				 return retVal;
				 }
			//}
		}
		ClassProxy introspectedClass = c;
		if (o != null && o instanceof ACompositeLoggable) {
			ACompositeLoggable composite = (ACompositeLoggable) o;
			Object realObject = composite.getRealObject();
			introspectedClass = AClassProxy.classProxy(realObject.getClass());
		}
		

		
		for (int i = 0; i < concreteTypeRegistry.size(); i++) {
			ConcreteTypeFactory factory = elementAt(i);
			if (!factory.useInSearch()) 
				continue;			
			retVal = factory.toConcreteType (introspectedClass, o, f, false);
//			if (cloneAdapters && retVal != null && !isDynamicClass(c)) {
//				classToPrototypeConcreteType.put (c, (AbstractConcreteType) retVal);
//				return retVal;
//			}
			
			if (retVal != null) {
				String patternName = retVal.getPatternName();
				String patternPath = retVal.getPatternPath();
				if (patternName == null)
					patternName = factory.getPatternName();
				if (patternPath == null)
					patternPath = factory.getPatternPath();
				registerConcreteType(targetClass, retVal);
				if (!(factory instanceof ConcretePrimitiveFactory) && o != null)
					if (patternName == null)
						util.trace.Tracer
								.error("Could not find pattern name for factory"
										+ factory + " for class " + c.getName());
					else {
						if (!c.getName().startsWith("java") && name == null) {
							// util.misc.Message.warning("Use annotation @util.annotations.StructurePattern(\""
							// + patternName + "\") for class " + c.getName());
							UndeclaredPattern.newCase(patternPath, c, ConcreteTypeRegistry.class);

//							Tracer.warning("Assuming implicit pattern: " + patternPath + "\n"
//
//							+ "  If this pattern is correct, use annotation @util.annotations.StructurePattern(\""
//											+ patternPath
//											+ "\") for class "
//											+ c.getName());
						} else if (name != null && !name.equals(patternPath)) {
//							Tracer.warning("Assuming implicit pattern: " + patternPath + " instead of: "  + name);
							SubstitutionOfDeclaredPattern.newCase(name, patternPath, c, ConcreteTypeRegistry.class);

						}
					}
				return retVal;
			}
		}
		return retVal;
	}
	public static ConcreteType createConcreteType(ClassProxy c, uiFrame f) {
		return createConcreteType (c, null, f);
	}
	public static void init () {
//		if (!objectEditorHasRegistered) {
//			ObjectEditor.register();
//			objectEditorHasRegistered = true;
//		}
	}
	static {
		ConcreteTypeRegistry.registerStructureFactories();
		registerClassFactoryAssociations();
	}
	public static ConcreteType createConcreteType(Class targetClass) {
		init();
		ConcreteType retVal = null;
		for (int i = 0; i < concreteTypeRegistry.size(); i++) {
			
			ConcreteTypeFactory factory = elementAt(i);
			/*
			retVal = factory.toConcreteType (targetClass, null, null);
			if (retVal != null) return retVal;
			*/
			
			retVal = factory.createConcreteType ();
			if (targetClass.isAssignableFrom(retVal.getClass())) return retVal;
			
		}
		return retVal;
	}
	
	public static void registerPatternName(ClassProxy aClass, String name) {
		classToPatternName.put(aClass, name);
	}
	public static void registerPatternName(Class aClass, String name) {
		classToPatternName.put(AClassProxy.classProxy(aClass), name);
	}
	/*
	static Vector vectorRegistry = new Vector();	
	public static void addLast(VectorStructureFactory newValue) {
		addLast(vectorRegistry, newValue);
		
	}
	public static void addFirst(VectorStructureFactory newValue) {
		addFirst(vectorRegistry, newValue);
		
	}	
	public static void removeElement(VectorStructureFactory oldValue) {
		vectorRegistry.removeElement(oldValue);
	}	
	public static VectorStructure createVectorStructure(Class c, Object o, uiFrame f) {
		VectorStructure retVal = null;
		for (int i = 0; i < vectorRegistry.size(); i++) {
			VectorStructureFactory factory = ((VectorStructureFactory) vectorRegistry.elementAt(i));
			retVal = factory.toVectorStructure(c, o, f);
			if (retVal != null) return retVal;
		}
		return retVal;
	}
	static Vector hashtableRegistry = new Vector();
	public static void addLast(HashtableStructureFactory newValue) {
		addLast(hashtableRegistry, newValue);
	}
	public static void addFirst(HashtableStructureFactory newValue) {
		addFirst(hashtableRegistry, newValue);
	}
	public static void removeElement(HashtableStructureFactory oldValue) {
		hashtableRegistry.removeElement(oldValue);
	}
	public static HashtableStructure createHashtableStructure(Class c, Object o, uiFrame f) {
		HashtableStructure retVal = null;
		for (int i = 0; i < hashtableRegistry.size(); i++) {
			HashtableStructureFactory factory = ((HashtableStructureFactory) hashtableRegistry.elementAt(i));
			retVal = factory.toHashtableStructure(c, o, f);
			if (retVal != null) return retVal;
		}
		return retVal;
	}
	*/
//	static {
//		registerStructureFactories();
//	}
	
	public static void registerStructureFactories() {
		// why not add primitive to the start so we do not have to search others		
		addLast(new GenericPrimitiveToPrimitiveFactory());
		addLast(new EnumToEnumerationFactory());
		addLast(new GenericEnumerationToEnumerationFactory());
//		ConcreteTypeFactory factory = new GenericPrimitiveToPrimitiveFactory();
//		addLast(factory);		
//		registerPrimitiveFactory(factory);
//		addLast(new GenericPrimitiveToPrimitiveFactory());		
		addLast(new GenericTreeNodeToVectorStructureFactory());
		addLast(new GenericTableToVectorStructureFactory());
		addLast(new GenericHashtableToHashtableStructureFactory());
		addLast(new GenericMapToHashtableStructureFactory());
		addLast(new GenericSimpleTableToHashtableStructureFactory());
		addLast(new GenericPropertyDescriptorToHashtableStructureFactory());
		addLast(new VectorWithUserObjectToVectorStructureFactory());
		addLast(new VectorToVectorStructureFactory()); 
		addLast(new ArrayListToVectorStructureFactory()); 
		addLast(new GenericStackToVectorStructureFactory());
		addLast(new GenericVectorWithUserObjectToVectorStructureFactory());
		addLast(new GenericVectorToVectorStructureFactory());
		addLast(new GenericListWithUserObjectToVectorStructureFactory());
		addLast(new GenericListToVectorStructureFactory());		
		addLast(new ArrayWithUserObjectToVectorStructureFactory());
		addLast(new ArrayToVectorStructureFactory());
		addLast(new GenericAWTShapeToAWTShapeFactory());
		addLast(new GenericShapeToShapeFactory());
		addLast(new GenericPointToPointFactory());	
		addLast(new GenericLineToLineFactory());
		addLast(new GenericRectangleToRectangleFactory());
		addLast(new GenericOvalToOvalFactory());
		addLast(new GenericStringShapeToStringShapeFactory());
		addLast(new GenericImageShapeToImageShapeFactory());
		addLast(new GenericTextShapeToTextShapeFactory());
		addLast(new GenericArcToArcFactory());
		addLast(new GenericCurveToCurveFactory());
		addLast(new GenericLabelShapeToLabelShapeFactory());
		addLast(new BeanToRecordFactory());
		addLast(new NoPatternFactory());		
	}
	public static void registerClassFactoryAssociations() {
		VectorToVectorStructureFactory vectorFactory = new VectorToVectorStructureFactory();

		classToConcreteTypeFactory.put(IntrospectUtility.vectorClass(), vectorFactory);
//		VectorWithUserObjectToVectorStructureFactory vectorWithUserObjectFactory = new VectorWithUserObjectToVectorStructureFactory();

//		classToConcreteTypeFactory.put(IntrospectUtility.aListenableVectorClass(), vectorFactory);
//		classToConcreteTypeFactory.put(IntrospectUtility.aReusingListenableShapeVector(), vectorFactory);


//		classToConcreteTypeFactory.put(IntrospectUtility.aListenableVectorClass(), new VectorToVectorStructureFactory());
//		VectorToVectorStructureFactory vectorFactory = new VectorToVectorStructureFactory();
//
//		classToConcreteTypeFactory.put(IntrospectUtility.vectorClass(), vectorFactory);
		classToConcreteTypeFactory.put(IntrospectUtility.arrayListClass(), new ArrayListToVectorStructureFactory());

	}
	
	
}
