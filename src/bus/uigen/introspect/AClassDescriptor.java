package bus.uigen.introspect;

import java.beans.MethodDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import util.annotations.Column;
import util.annotations.ComponentsVisible;
import util.annotations.DisplayToString;
import util.annotations.EditablePropertyNames;
import util.annotations.IsAtomicShape;
import util.annotations.IsCompositeShape;
import util.annotations.Label;
import util.annotations.Position;
import util.annotations.PropertyNames;
import util.annotations.ReturnsClassExplanation;
import util.annotations.ReturnsClassWebDocuments;
import util.annotations.Row;
import util.annotations.ShowDebugInfoWithToolTip;
import util.annotations.StructurePattern;
import util.introspect.JavaIntrospectUtility;
import util.misc.Common;
import util.models.ADynamicSparseList;
import util.models.PropertyListenerRegistrar;
import util.models.VectorListener;
import util.models.VectorListenerRegisterer;
import util.trace.Tracer;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.DynamicMethods;
import bus.uigen.reflect.ElementProxy;
import bus.uigen.reflect.FieldProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.trace.MissingGetterOfProperty;
import bus.uigen.trace.MissingSetterOfEditableProperty;
import bus.uigen.trace.UndeclaredEditableProperty;
import bus.uigen.trace.UndeclaredProperty;

public class AClassDescriptor implements ClassDescriptorInterface, Serializable {

	// public static int MAX_
	protected PropertyDescriptorProxy[] properties = {};
	protected MethodDescriptorProxy allMethods = new VirtualMethodDescriptor();
	protected MethodDescriptorProxy[] methods = {};
	protected FieldDescriptorProxy[] fields = {};
	protected FieldDescriptorProxy[] constants = {};
	protected ConstructorDescriptorProxy[] constructors = {};
	protected ClassProxy realClass;
	protected String virtualClass; // will need to be passed in actually
	protected Object prototypeObject;
	protected boolean prototypeInitialized = false;
	protected boolean dynamicMethodsInitialized = false;
	protected boolean dynamicPropertiesInitialized = false;
	// protected boolean isShapeClass = false;
	protected Vector doubleClickMethodsVector = new Vector();
	protected Vector selectMethodsVector = new Vector();
	protected Vector expandMethodsVector = new Vector();
	protected MethodProxy[] doubleClickMethods;
	protected MethodProxy[] selectMethods;
	protected MethodProxy[] expandMethods;
	Hashtable<String, VirtualMethodDescriptor> namesToMethodDescriptors = new Hashtable();
	Hashtable<String, PropertyDescriptorProxy> namesToPropertyDescriptors = new Hashtable();
	MethodProxy addPropertyListener;
	MethodProxy addObserver;
	MethodProxy addRefresher;
	MethodProxy addVectorListener;
	MethodProxy addHashtableListener;
	MethodProxy addTableListener;
	MethodProxy addTreeListener;
	MethodProxy addRemotePropertyListener;
	boolean componentsVisible = true;
	public static final String[] ignoreProperties = {"class", "bounds"};

	protected FeatureDescriptorProxy[] features = null;

	private PropertyChangeSupport propertyChange = new PropertyChangeSupport(
			this);
	protected Hashtable attributes = new Hashtable();

	public Hashtable getAttributes() {
		return attributes;
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChange.addPropertyChangeListener(l);
	}

	String beanFile;

	protected void setClass(ClassProxy cl) {
		if (cl == null)
			return;
		realClass = cl;
		beanFile = toShortName(realClass.getName()) + "BeanInfo.java";
		/*
		 * util.Explanation explanation = (util.Explanation)
		 * realClass.getAnnotation(util.Explanation.class); if (explanation !=
		 * null) setAttribute(AttributeNames.TOOL_TIP_TEXT,
		 * explanation.value());
		 */
		// isShapeClass = uiBean.isShapeModel(c) || (uiBean.isShape(c));
	}

	public ClassProxy getRealClass() {
		return realClass;
	}

	public String getVirtualClass() {
		return virtualClass;
	}

	public String getName() {
		if (getRealClass() != null)
			return getRealClass().getName();
		else
			return "";
	}

	public static String getMethodsMenuName(ClassProxy c) {
		// String name = toInterfaceName (toShortName (c.getName()));
		return toInterfaceName(c);
		/*
		 * if (name.equals("Vector")) return "List";
		 */
		// return name;
	}

	public static String getMethodsMenuName(String c) {
		// String name = toInterfaceName (toShortName (c.getName()));
		return c;
		/*
		 * if (name.equals("Vector")) return "List";
		 */
		// return name;
	}

	public static String nullString = "";

	public static String toString(String[] keywords) {
		String retVal = "Keywords: ";
		for (int i = 0; i < keywords.length; i++) {
			if (i != 0)
				retVal += ", ";
			retVal += keywords[i];
		}
		return retVal;
	}

	public static ClassProxy getMostSpecificType(MethodProxy m) {
		ClassProxy methodClass = m.getDynamicClass();
		if (methodClass == null)
			methodClass = m.getDeclaringClass();
		ClassProxy[] interfaces = methodClass.getInterfaces();
		ClassProxy declaringInterface = getDeclaringInterface(m, interfaces);
		if (declaringInterface != null)
			methodClass = declaringInterface;
		return methodClass;
	}

	public static ClassProxy getMostSpecificClass(MethodProxy m) {
		ClassProxy methodClass = m.getDynamicClass();
		if (methodClass == null)
			methodClass = m.getDeclaringClass();
		/*
		 * Class[] interfaces = methodClass.getInterfaces(); Class
		 * declaringInterface = getDeclaringInterface(m, interfaces); if
		 * (declaringInterface != null) methodClass = declaringInterface;
		 */
		return methodClass;
	}

	/*
	 * public static ClassProxy getDeclaringInterface(VirtualMethod m,
	 * ClassProxy[] interfaces) { for (int i = 0; i < interfaces.length; i++) {
	 * if (contains (interfaces[i], m)) { ClassProxy[] superInterfaces =
	 * interfaces[i].getInterfaces(); ClassProxy declaringInterface =
	 * getDeclaringInterface(m, superInterfaces); if (declaringInterface !=
	 * null) return declaringInterface; else return interfaces[i]; } } //String
	 * name = toInterfaceName (toShortName (c.getName())); return null; //return
	 * toInterfaceName(c);
	 * 
	 * //return name; }
	 */
	public static ClassProxy getDeclaringInterface(MethodProxy m,
			ClassProxy[] interfaces) {
		for (int i = 0; i < interfaces.length; i++) {
			if (contains(interfaces[i], m)) {
				ClassProxy[] superInterfaces = interfaces[i].getInterfaces();
				ClassProxy declaringInterface = getDeclaringInterface(m,
						superInterfaces);
				if (declaringInterface != null)
					return declaringInterface;
				else
					return interfaces[i];
			}
		}
		// String name = toInterfaceName (toShortName (c.getName()));
		return null;
		// return toInterfaceName(c);
		/*
		 * if (name.equals("Vector")) return "List";
		 */
		// return name;
	}

	/*
	 * public static boolean contains (ClassProxy c, Method m) { Method[]
	 * methods = c.getMethods(); for (int i = 0; i < methods.length; i ++ ) if
	 * (equals(m, methods[i])) return true; return false;
	 * 
	 * }
	 */
	public static boolean contains(ClassProxy c, MethodProxy m) {
		MethodProxy[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (equals(m, methods[i]))
				return true;
		return false;

	}

	public static boolean equals(MethodProxy m1, MethodProxy m2) {
		if (m1 == null || m2 == null)
			return false;
		if (!m1.getName().equals(m2.getName()))
			return false;
		ClassProxy[] params1 = m1.getParameterTypes();
		ClassProxy[] params2 = m2.getParameterTypes();
		if (params1.length != params2.length)
			return false;
		for (int i = 0; i < params1.length; i++)
			if (params1[i] != params2[i])
				return false;
		return true;
	}

	/*
	 * public static boolean equals (VirtualMethod m1, Method m2) { if
	 * (!m1.getName().equals(m2.getName())) return false; ClassProxy[] params1 =
	 * m1.getParameterTypes(); ClassProxy[] params2 = m2.getParameterTypes(); if
	 * (params1.length != params2.length) return false; for (int i = 0; i <
	 * params1.length; i++) if (params1[i] != params2[i]) return false; return
	 * true; }
	 */

	void print(ClassProxy[] classes) {
		for (int i = 0; i < classes.length; i++)
			System.out.println(classes[i]);
	}

	// need a virtual class here
	protected AClassDescriptor(ClassProxy c) {
		// System.out.println("CD" + c);
		// Class[] classes;
		// System.out.println ("classes");
		// print(c.getClasses());
		// System.out.println ("declared classes");
		// print(c.getDeclaredClasses());
		// System.out.println( "component type" + c.getComponentType());
		// setClass(c);
		init(c);
	}

	Object getVirtualRealAttribute(String name) {
		Object value = getBeanDescriptor().getValue(name);
		if (value == null && virtualClass != null) {
			ClassDescriptorInterface cd = ClassDescriptorCache
					.getClassDescriptor(realClass);
			value = cd.getAttribute(name);
		}
		return value;
	}

	boolean onlyDynamicMethods() {
		Object excludeStaticMethods = getVirtualRealAttribute(AttributeNames.ONLY_DYNAMIC_METHODS);
		/*
		 * if (virtualClass == null) { excludeStaticMethods =
		 * getBeanDescriptor().getValue(AttributeNames.ONLY_DYNAMIC_METHODS); }
		 * else { ViewInfo cd =
		 * ClassDescriptorCache.getClassDescriptor(realClass);
		 * excludeStaticMethods =
		 * cd.getAttribute(AttributeNames.ONLY_DYNAMIC_METHODS);
		 * 
		 * }
		 */
		return excludeStaticMethods != null
				&& ((Boolean) excludeStaticMethods).booleanValue() == true;

	}

	boolean onlyDynamicProperties() {
		// Object excludeStaticProperties =
		// getBeanDescriptor().getValue(AttributeNames.ONLY_DYNAMIC_METHODS);
		Object excludeStaticProperties = getVirtualRealAttribute(AttributeNames.ONLY_DYNAMIC_PROPERTIES);
		return excludeStaticProperties != null
				&& ((Boolean) excludeStaticProperties).booleanValue() == true;
	}

	void addDynamicProperties(Object thePrototypeObject) {
		// if (uiBean.getOnlyDynamicProperties(thePrototypeObject))
		if (dynamicPropertiesInitialized)
			return;
		dynamicPropertiesInitialized = true;
		if (onlyDynamicProperties())
			setPropertyDescriptors(dynamicPropertyDescriptors());
		else
			addProperties(dynamicPropertyDescriptors());

	}

	void addDynamicMethods(Object thePrototypeObject) {
		if (dynamicMethodsInitialized)
			return;
		dynamicMethodsInitialized = true;

		// if (uiBean.getOnlyDynamicCommands(thePrototypeObject))
		if (onlyDynamicMethods())
			setVirtualMethodDescriptors(getDynamicMethodDescriptors());
		else
			addMethods(getDynamicMethodDescriptors());

	}

	public void setPrototypeObject(Object thePrototypeObject) {
		// if (prototypeInitialized)
		/*
		 * if (prototypeInitialized && prototypeObject == thePrototypeObject)
		 * return;
		 */
		if (prototypeInitialized)
			return;
		if (prototypeInitialized) {
			removeDynamicProperties();
			removeDynamicMethods();
		}

		prototypeObject = thePrototypeObject;
		if (prototypeObject == null)
			return;
		/*
		 * Vector<PropertyDescriptor> dynamicProperties =
		 * dynamicPropertyDescriptors(); if (dynamicProperties != null &&
		 * dynamicProperties.size() > 1) { Object excludeStaticProperties =
		 * getBeanDescriptor().getValue(AttributeNames.ONLY_DYNAMIC_PROPERTIES);
		 * boolean excludeStatic = excludeStaticProperties != null && ((Boolean)
		 * excludeStaticProperties).booleanValue() == true; if (excludeStatic)
		 * setPropertyDescriptors(dynamicProperties); else
		 * 
		 * addProperties (dynamicPropertyDescriptors()); }
		 */
		addDynamicProperties(thePrototypeObject);
		/*
		 * if (uiBean.getOnlyDynamicProperties(thePrototypeObject))
		 * setPropertyDescriptors (dynamicPropertyDescriptors()); else
		 * addProperties (dynamicPropertyDescriptors());
		 */
		/*
		 * Vector<VirtualMethodDescriptor> dynamicMethods =
		 * dynamicMethodDescriptors(); if (dynamicMethods != null &&
		 * dynamicMethods.size() > 1) { Object excludeStaticMethods =
		 * getBeanDescriptor().getValue(AttributeNames.ONLY_DYNAMIC_METHODS);
		 * boolean excludeStatic = excludeStaticMethods != null && ((Boolean)
		 * excludeStaticMethods).booleanValue() == true; if (excludeStatic)
		 * setVirtualMethodDescriptors(dynamicMethods); else addMethods
		 * (dynamicMethods); //addProperties (dynamicPropertyDescriptors()); }
		 * addMethods (dynamicMethodDescriptors());
		 */
		addDynamicMethods(thePrototypeObject);
		/*
		 * if (uiBean.getOnlyDynamicCommands(thePrototypeObject))
		 * setVirtualMethodDescriptors (dynamicMethodDescriptors()); else
		 * addMethods (dynamicMethodDescriptors());
		 */
		// properties = add (properties, dynamicPropertyDescriptors());
		prototypeInitialized = true;
	}

	public static PropertyDescriptorProxy[] add(
			PropertyDescriptorProxy[] original,
			Vector<PropertyDescriptorProxy> newElements) {
		if (newElements == null)
			return original;
		PropertyDescriptorProxy[] newArray = new PropertyDescriptorProxy[original.length
				+ newElements.size()];
		for (int i = 0; i < original.length; i++) {
			newArray[i] = original[i];
		}
		for (int i = 0; i < newElements.size(); i++)
			newArray[i + original.length] = newElements.elementAt(i);
		return newArray;

	}

	public static PropertyDescriptorProxy[] remove(
			PropertyDescriptorProxy[] original,
			Vector<PropertyDescriptorProxy> oldElements) {
		if (oldElements == null || oldElements.size() == 0)
			return original;
		Vector<PropertyDescriptorProxy> propertyVector = util.misc.Common
				.deepArrayToVector(original);
		// PropertyDescriptor[] newArray = new PropertyDescriptor
		// [original.length - oldElements.size()];
		for (int i = 0; i < oldElements.size(); i++) {
			propertyVector.remove(oldElements.get(i));
		}
		PropertyDescriptorProxy[] newArray = propertyVector.toArray(original);
		return newArray;

	}

	public void addProperties(Vector<PropertyDescriptorProxy> newElements) {
		properties = add(properties, newElements);
	}

	public void removeDynamicProperties() {
		remove(properties, dynamicPropertyDescriptors);
		dynamicPropertiesInitialized = false;
		dynamicPropertyDescriptors = null;
	}

	public PropertyDescriptorProxy addProperty(String newProperty) {
		Vector<PropertyDescriptorProxy> v = new Vector();
		PropertyDescriptorProxy pd = null;
		try {
			pd = new APropertyDescriptorProxy(newProperty, null, null);
			v.addElement(pd);
			addProperties(v);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return pd;
	}

	public void addMethods(Vector<VirtualMethodDescriptor> newElements) {
		methods = add(methods, newElements);
	}

	public static MethodDescriptorProxy[] add(MethodDescriptorProxy[] original,
			Vector<VirtualMethodDescriptor> newElements) {
		if (newElements == null || original == null)
			return original;
		MethodDescriptorProxy[] newArray = new MethodDescriptorProxy[original.length
				+ newElements.size()];
		for (int i = 0; i < original.length; i++) {
			newArray[i] = original[i];
		}
		for (int i = 0; i < newElements.size(); i++)
			newArray[i + original.length] = newElements.elementAt(i);
		return newArray;

	}

	public static MethodDescriptorProxy[] remove(
			MethodDescriptorProxy[] original,
			Vector<VirtualMethodDescriptor> oldElements) {
		if (oldElements == null)
			return original;
		if (oldElements.size() == 0)
			return original;
		Vector<MethodDescriptorProxy> methodVector = util.misc.Common
				.deepArrayToVector(original);
		// PropertyDescriptor[] newArray = new PropertyDescriptor
		// [original.length - oldElements.size()];
		for (int i = 0; i < oldElements.size(); i++) {
			methodVector.remove(oldElements.get(i));
		}
		MethodDescriptorProxy[] template = { original[0] };
		// MethodDescriptor[] newArray = methodVector.toArray(original);

		// MethodDescriptorProxy[] newArray = methodVector.toArray(template);
		Object[] newObjectArray = methodVector.toArray();
		MethodDescriptorProxy[] newArray = new MethodDescriptorProxy[newObjectArray.length];
		for (int i = 0; i < newObjectArray.length; i++)
			newArray[i] = (MethodDescriptorProxy) newObjectArray[i];
		// MethodDescriptorProxy[] newArray = methodVector.;
		return newArray;

	}

	public void removeDynamicMethods() {
		methods = remove(methods, dynamicMethodDescriptors);
		dynamicMethodsInitialized = false;
		dynamicMethodDescriptors = null;
	}

	public void setMethodDescriptors(Vector<MethodDescriptorProxy> newElements) {
		MethodDescriptorProxy[] newArray = new MethodDescriptorProxy[newElements
				.size()];
		for (int i = 0; i < newElements.size(); i++)
			newArray[i] = newElements.elementAt(i);
		methods = newArray;

	}

	public void setVirtualMethodDescriptors(
			Vector<VirtualMethodDescriptor> newElements) {
		MethodDescriptorProxy[] newArray = new VirtualMethodDescriptor[newElements
				.size()];
		for (int i = 0; i < newElements.size(); i++)
			newArray[i] = newElements.elementAt(i);
		methods = newArray;

	}

	public void setPropertyDescriptors(
			Vector<PropertyDescriptorProxy> newElements) {
		PropertyDescriptorProxy[] newArray = new PropertyDescriptorProxy[newElements
				.size()];
		for (int i = 0; i < newElements.size(); i++)
			newArray[i] = newElements.elementAt(i);
		properties = newArray;

	}

	public AClassDescriptor(ClassProxy c, Object thePrototypeObject,
			String theVirtualClass) {
		// System.out.println("CD" + c);
		// Class[] classes;
		// System.out.println ("classes");
		// print(c.getClasses());
		// System.out.println ("declared classes");
		// print(c.getDeclaredClasses());
		// System.out.println( "component type" + c.getComponentType());
		prototypeObject = thePrototypeObject;
		if (prototypeObject != null)
			prototypeInitialized = true;
		// virtualClass = uiBean.getVirtualClass(thePrototypeObject);
		virtualClass = theVirtualClass;
		// init (thePrototypeObject.getClass());
		init(c);
		/*
		 * if (virtualClass == null) { init(o.getClass()); } else { init
		 * (virtualClass); }
		 */
		// setClass(c);
		// init(c);
	}

	// Do reflection here. this way
	// we avoid performing reflection on
	// classes that weve encountered before!
	//
	// ClassDescriptorCustomizer customizer;
	// public ClassDescriptorCustomizer getClassDescriptorCustomizer() {
	// return new ClassDescriptorCustomizer(this);
	// //return customizer;
	// }

	// should really create a table mapping annotation type to attribute name
	// and modularize this method
	public static Vector<Attribute> getMergedAttributeVector(
			ElementProxy elementProxy) {
		util.annotations.IntAttributes intAttributes = (util.annotations.IntAttributes) elementProxy
				.getAnnotation(util.annotations.IntAttributes.class);
		Vector<Attribute> intAttributesVector = toAttributeVector(intAttributes);
		Vector<Attribute> mergedAttributeVector = intAttributesVector;

		util.annotations.StringAttributes stringAttributes = (util.annotations.StringAttributes) elementProxy
				.getAnnotation(util.annotations.StringAttributes.class);
		Vector<Attribute> stringAttributeVector = toAttributeVector(stringAttributes);
		mergedAttributeVector = merge(mergedAttributeVector,
				stringAttributeVector);

		util.annotations.BooleanAttributes booleanAttributes = (util.annotations.BooleanAttributes) elementProxy
				.getAnnotation(util.annotations.BooleanAttributes.class);
		/*
		 * if (booleanAttributes != null) { System.out.println ("" + realClass);
		 * }
		 */
		Vector<Attribute> booleanAttributeVector = toAttributeVector(booleanAttributes);
		mergedAttributeVector = merge(mergedAttributeVector,
				booleanAttributeVector);

		util.annotations.MaxValue maxValue = (util.annotations.MaxValue) elementProxy
				.getAnnotation(util.annotations.MaxValue.class);
		Vector<Attribute> maxValueVector = AClassDescriptor
				.toAttributeVector(maxValue);
		mergedAttributeVector = merge(mergedAttributeVector, maxValueVector);

		util.annotations.MinValue minValue = (util.annotations.MinValue) elementProxy
				.getAnnotation(util.annotations.MinValue.class);
		Vector<Attribute> minValueVector = AClassDescriptor
				.toAttributeVector(minValue);
		mergedAttributeVector = merge(mergedAttributeVector, minValueVector);

		util.annotations.StepValue stepValue = (util.annotations.StepValue) elementProxy
				.getAnnotation(util.annotations.StepValue.class);
		Vector<Attribute> stepValueVector = AClassDescriptor
				.toAttributeVector(stepValue);
		mergedAttributeVector = merge(mergedAttributeVector, stepValueVector);

		Position position = (Position) elementProxy
				.getAnnotation(Position.class);
		Vector<Attribute> positionVector = AClassDescriptor
				.toAttributeVector(position);
		mergedAttributeVector = merge(mergedAttributeVector, positionVector);

		Row row = (Row) elementProxy.getAnnotation(Row.class);
		Vector<Attribute> rowVector = AClassDescriptor.toAttributeVector(row);
		mergedAttributeVector = merge(mergedAttributeVector, rowVector);

		Label label = (Label) elementProxy.getAnnotation(Label.class);
		Vector<Attribute> labelVector = AClassDescriptor
				.toAttributeVector(label);
		mergedAttributeVector = merge(mergedAttributeVector, labelVector);

		Column column = (Column) elementProxy.getAnnotation(Column.class);
		Vector<Attribute> columnVector = AClassDescriptor
				.toAttributeVector(column);
		mergedAttributeVector = merge(mergedAttributeVector, columnVector);

		util.annotations.Direction alignment = (util.annotations.Direction) elementProxy
				.getAnnotation(util.annotations.Direction.class);
		Vector<Attribute> alignmentVector = AClassDescriptor
				.toAttributeVector(alignment);
		mergedAttributeVector = merge(mergedAttributeVector, alignmentVector);

		util.annotations.ComponentWidth componentWidth = (util.annotations.ComponentWidth) elementProxy
				.getAnnotation(util.annotations.ComponentWidth.class);
		Vector<Attribute> componentWidthVector = AClassDescriptor
				.toAttributeVector(componentWidth);
		mergedAttributeVector = merge(mergedAttributeVector,
				componentWidthVector);
		
		util.annotations.ComponentHeight componentHeight = (util.annotations.ComponentHeight) elementProxy
				.getAnnotation(util.annotations.ComponentHeight.class);
		Vector<Attribute> componentHeightVector = AClassDescriptor
				.toAttributeVector(componentHeight);
		mergedAttributeVector = merge(mergedAttributeVector,
				componentHeightVector);
		

		util.annotations.PreferredWidgetClass widgetClass = (util.annotations.PreferredWidgetClass) elementProxy
				.getAnnotation(util.annotations.PreferredWidgetClass.class);
		Vector<Attribute> widgetClassVector = AClassDescriptor
				.toAttributeVector(widgetClass);
		mergedAttributeVector = merge(mergedAttributeVector, widgetClassVector);
		
		util.annotations.LayoutName layoutClass = (util.annotations.LayoutName) elementProxy
				.getAnnotation(util.annotations.LayoutName.class);
		Vector<Attribute> layoutClassVector = AClassDescriptor
				.toAttributeVector(layoutClass);
		mergedAttributeVector = merge(mergedAttributeVector, layoutClassVector);

		util.annotations.IsAtomicShape isAtomicShape = (util.annotations.IsAtomicShape) elementProxy
				.getAnnotation(util.annotations.IsAtomicShape.class);
		Vector<Attribute> isAtomicShapeVector = AClassDescriptor
				.toAttributeVector(isAtomicShape);
		mergedAttributeVector = merge(mergedAttributeVector,
				isAtomicShapeVector);
		
		util.annotations.IsCompositeShape isCompositeShape = (util.annotations.IsCompositeShape) elementProxy
				.getAnnotation(util.annotations.IsCompositeShape.class);
		Vector<Attribute> isCompositeShapeVector = AClassDescriptor
				.toAttributeVector(isCompositeShape);
		mergedAttributeVector = merge(mergedAttributeVector,
				isCompositeShapeVector);

		ReturnsClassExplanation returnsClassExplanation = (ReturnsClassExplanation) elementProxy
				.getAnnotation(ReturnsClassExplanation.class);
		Vector<Attribute> returnsClassExplanationVector = AClassDescriptor
				.toAttributeVector(returnsClassExplanation);
		mergedAttributeVector = merge(mergedAttributeVector,
				returnsClassExplanationVector);

		ReturnsClassWebDocuments returnsClassWebDocuments = (ReturnsClassWebDocuments) elementProxy
				.getAnnotation(ReturnsClassWebDocuments.class);
		Vector<Attribute> returnsClassWebDocumentsVector = AClassDescriptor
				.toAttributeVector(returnsClassWebDocuments);
		mergedAttributeVector = merge(mergedAttributeVector,
				returnsClassWebDocumentsVector);

		ShowDebugInfoWithToolTip showDebugInfoWithToolTip = (ShowDebugInfoWithToolTip) elementProxy
				.getAnnotation(ShowDebugInfoWithToolTip.class);
		Vector<Attribute> showDebugInfoWithToolTipVector = AClassDescriptor
				.toAttributeVector(showDebugInfoWithToolTip);
		mergedAttributeVector = merge(mergedAttributeVector,
				showDebugInfoWithToolTipVector);

		util.annotations.IsNestedShapesContainer isNestedShapesContainer = (util.annotations.IsNestedShapesContainer) elementProxy
				.getAnnotation(util.annotations.IsNestedShapesContainer.class);
		Vector<Attribute> isNestedShapesContainerVector = AClassDescriptor
				.toAttributeVector(isNestedShapesContainer);
		mergedAttributeVector = merge(mergedAttributeVector,
				isNestedShapesContainerVector);

		util.annotations.SeparateThread separateThread = (util.annotations.SeparateThread) elementProxy
				.getAnnotation(util.annotations.SeparateThread.class);
		Vector<Attribute> separateThreadVector = toAttributeVector(separateThread);
		mergedAttributeVector = merge(mergedAttributeVector,
				separateThreadVector);

		util.annotations.ShowButton showButton = (util.annotations.ShowButton) elementProxy
				.getAnnotation(util.annotations.ShowButton.class);
		Vector<Attribute> showButtonVector = toAttributeVector(showButton);
		mergedAttributeVector = merge(mergedAttributeVector, showButtonVector);

		util.annotations.Visible visible = (util.annotations.Visible) elementProxy
				.getAnnotation(util.annotations.Visible.class);
		Vector<Attribute> visibleVector = toAttributeVector(visible);
		mergedAttributeVector = merge(mergedAttributeVector, visibleVector);
		
		util.annotations.IndirectlyVisible indirectVisible = (util.annotations.IndirectlyVisible) elementProxy
				.getAnnotation(util.annotations.IndirectlyVisible.class);
		Vector<Attribute> indirectVisibleVector = toAttributeVector(indirectVisible);
		mergedAttributeVector = merge(mergedAttributeVector, indirectVisibleVector);
		//Dunno if we need this really as we are filtering at a higher level
		util.annotations.DisplayToString displayToString = (util.annotations.DisplayToString) elementProxy
				.getAnnotation(util.annotations.DisplayToString.class);
		Vector<Attribute> displayToStringVector = toAttributeVector(displayToString);
		mergedAttributeVector = merge(mergedAttributeVector, displayToStringVector);
		
		util.annotations.OnlyGraphicalDescendents onlyGraphicalDescendents = (util.annotations.OnlyGraphicalDescendents) elementProxy
				.getAnnotation(util.annotations.OnlyGraphicalDescendents.class);
		Vector<Attribute> onlyGraphicalDescendentsVector = toAttributeVector(onlyGraphicalDescendents);
		mergedAttributeVector = merge(mergedAttributeVector, onlyGraphicalDescendentsVector);

		return mergedAttributeVector;

	}

	public  boolean isVisible(MethodProxy aMethod) {
		util.annotations.Visible visible = (util.annotations.Visible) aMethod
				.getAnnotation(util.annotations.Visible.class);
		if (visible == null)
//			return true;
			return componentsVisible;

		else
			return visible.value();

	}

	public static String getExplanationAnnotation(List<ClassProxy> superTypes) {
		String retVal = null;
		boolean firstTime = true;
		for (int i = superTypes.size() - 1; i >= 0; i--) {
			ClassProxy aClass = superTypes.get(i);
			util.annotations.Explanation explanation = (util.annotations.Explanation) aClass
					.getAnnotation(util.annotations.Explanation.class);
			if (explanation != null) {
				String value = explanation.value();
				if (firstTime) {
					retVal = value;
					firstTime = false;
				} else
					retVal += " " + value;

			}
		}
		return retVal;
	}

	static StructurePattern getPatternAnnotation(List<ClassProxy> superTypes) {
		boolean firstTime = true;
		for (ClassProxy aClass : superTypes) {
			StructurePattern retVal = (StructurePattern) aClass
					.getAnnotation(StructurePattern.class);
			if (retVal != null)
				return retVal;
		}
		return null;
	}
	
	

	public static StructurePattern getPatternAnnotationInSuperTypesOf(
			ClassProxy aClass) {
		return getPatternAnnotation(IntrospectUtility.getSuperTypes(aClass));
	}

	static String getExplanationAnnotationOfMethod(
			List<MethodProxy> superMethods) {
		String retVal = null;
		boolean firstTime = true;
		for (int i = superMethods.size() - 1; i >= 0; i--) {
			MethodProxy aMethod = superMethods.get(i);
			util.annotations.Explanation explanation = (util.annotations.Explanation) aMethod
					.getAnnotation(util.annotations.Explanation.class);
			if (explanation != null) {
				String value = explanation.value();
				if (firstTime) {
					retVal = value;
					firstTime = false;
				} else
					retVal += " " + value;

			}
		}
		return retVal;
	}

	void initClass() {
		util.annotations.ComponentsVisible aComponentsVisible = (util.annotations.ComponentsVisible) realClass
				.getAnnotation(util.annotations.ComponentsVisible.class);
		if (aComponentsVisible != null)
			componentsVisible = aComponentsVisible.value();
		List<ClassProxy> superTypes = IntrospectUtility
				.getSuperTypes(realClass);
		String explanations = getExplanationAnnotation(superTypes);

		// util.annotations.Explanation explanation =
		// (util.annotations.Explanation)
		// realClass.getAnnotation(util.annotations.Explanation.class);
		// if (explanation != null)
		// setAttributeWithoutAddingKeyword(AttributeNames.EXPLANATION_ANNOTATION,
		// explanation.value());
		if (explanations != null)
			setAttributeWithoutAddingKeyword(
					AttributeNames.EXPLANATION, explanations);

		util.annotations.WebDocuments documentation = (util.annotations.WebDocuments) realClass
				.getAnnotation(util.annotations.WebDocuments.class);
		if (documentation != null)
			setAttributeWithoutAddingKeyword(AttributeNames.HTML_DOCUMENTATION,
					documentation.value());
		util.annotations.Keywords keywords = (util.annotations.Keywords) realClass
				.getAnnotation(util.annotations.Keywords.class);
		if (keywords != null)
			setAttributeWithoutAddingKeyword(
					AttributeNames.KEYWORDS_ANNOTATION, keywords.value());
		/*
		 * util.IntAttributes intAttributes = (util.IntAttributes)
		 * realClass.getAnnotation(util.IntAttributes.class); util.MaxValue
		 * maxValue = (util.MaxValue)
		 * realClass.getAnnotation(util.MaxValue.class); util.MinValue minValue
		 * = (util.MinValue) realClass.getAnnotation(util.MinValue.class);
		 * Vector<Attribute> intAttributesVector =
		 * toAttributeVector(intAttributes); Vector<Attribute> maxValueVector =
		 * toAttributeVector(maxValue); Vector<Attribute> minValueVector =
		 * toAttributeVector(minValue); Vector<Attribute> mergedAttributeVector
		 * = merge (intAttributesVector, merge (maxValueVector,
		 * minValueVector));
		 */

		/*
		 * if (intAttributes != null)
		 * setAttributeWithoutAddingKeyword(AttributeNames
		 * .MERGED_ATTRIBUTES_ANNOTATIONS, toAttributeVector(intAttributes));
		 */
		Vector<Attribute> mergedAttributeVector = getMergedAttributeVector(realClass);
		if (mergedAttributeVector != null)
			setAttributeWithoutAddingKeyword(
					AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS,
					mergedAttributeVector);
	}

	private void init(ClassProxy c) {
		// customizer = new ClassDescriptorCustomizer(this);
		
		if (c.equals(c.proxyClass())) 
			System.out.println("Proxy Class");

		setClass(c);
		boolean componentsIgnored = ClassIntrospectionFilterer.componentsIgnored(c);
		boolean operationsIgnored = ClassIntrospectionFilterer.operationsIgnored(c);
		if (!componentsIgnored)
		
		// Work with the raw class here
		   initFields(c);
		if (!operationsIgnored)
		   initConstructors(c);
		// Use the BeanInfo for these two
		// try {
		
		BeanInfoProxy binfo = null;
		if (!componentsIgnored || !operationsIgnored)
		binfo = AnIntrospectorProxy.getBeanInfo(c, componentsIgnored);
//		else
//			binfo = AnIntrospectorProxy.getBeanInfo(c, true); // refactor later
//		binfo = AnIntrospectorProxy.getBeanInfo(c, componentsIgnored);
		// else
		// binfo = AnIntrospectorProxy.getBeanInfo(realClass);
		initBeanDescriptor(binfo);
//		Class bclass = binfo.getClass();
		// initProperties(binfo);
		initClass();
		if (!operationsIgnored)
		initMethods(binfo);
		if (!componentsIgnored)
		initProperties(binfo);
		// causes problems with recursive calls to Class descriptor
		// AMenuDescriptor.registerAttributeRegistrer(c);
		// } catch (IntrospectionException e) {
		// initMethods(c);
		// }
	}

	public static ClassProxy getBeanInfoClass(ClassProxy modelClass) {
		String attributeRegistrer = modelClass.getName() + "BeanInfo";
		return RemoteSelector.classProxy(JavaIntrospectUtility
				.classForName(attributeRegistrer));

	}

	BeanDescriptorProxy beanDescriptor;

	private void initBeanDescriptor(BeanInfoProxy b) {
		BeanInfoProxy myBeanProxy;
		if (virtualClass == null && b != null)
			beanDescriptor = b.getBeanDescriptor();
		else
			beanDescriptor = new ABeanDescriptorProxy(realClass);

		/*
		 * if (virtualClass != null) myBeanProxy =
		 * AnIntrospectorProxy.getBeanInfo(realClass); beanDescriptor =
		 * b.getBeanDescriptor();
		 */
		/*
		 * //if (b != null) { if (virtualClass == null ) beanDescriptor = new
		 * ABeanDescriptorProxy(b.getBeanDescriptor()); else {
		 * 
		 * 
		 * beanDescriptor = new ABeanDescriptorProxy(new
		 * BeanDescriptor(((AClassProxy)realClass).getJavaClass());
		 * 
		 * } //} else //beanDescriptor = new ABeanDescriptorProxy(c);
		 */
	}

	// Get information about fields here
	private void initFields(ClassProxy c) {
		Vector vf = new Vector();
		Vector vc = new Vector();
		// Field[] f = c.getDeclaredFields();
		FieldProxy[] f = c.getFields();
		int i, modifiers;
		for (i = 0; i < f.length; i++) {
			// System.out.println("field" + f[i].getName());
			modifiers = f[i].getModifiers();
			if (Modifier.isPublic(modifiers)
			// && Modifier.isStatic(modifiers)
					&& Modifier.isFinal(modifiers))
				vc.addElement(f[i]);
			else if (Modifier.isPublic(modifiers))
				vf.addElement(f[i]);
		}
		/*
		 * f = c.getDeclaredFields(); for (i=0; i<f.length; i++) {
		 * System.out.println("field" + f[i].getName()); modifiers =
		 * f[i].getModifiers(); if (Modifier.isPublic(modifiers) &&
		 * Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) { if
		 * (!vc.contains(f[i])) vc.addElement(f[i]); } else if
		 * (Modifier.isPublic(modifiers)) { if (!vf.contains(f[i]))
		 * vf.addElement(f[i]); } }
		 */
		fields = new FieldDescriptorProxy[vf.size()];
		for (i = 0; i < vf.size(); i++) {
			// fields[i] = new AFieldDescriptorProxy(new
			// FieldDescriptor((FieldProxy) vf.elementAt(i)));
			fields[i] = new AFieldDescriptorProxy((FieldProxy) vf.elementAt(i));
		}
		constants = new FieldDescriptorProxy[vc.size()];
		for (i = 0; i < vc.size(); i++) {
			// constants[i] = new AConstantDescriptorProxy (new
			// ConstantDescriptor((Field) vc.elementAt(i)));
			constants[i] = new AConstantDescriptorProxy(
					(FieldProxy) vc.elementAt(i));
		}
	}

	private void initConstructors(ClassProxy c) {
		Vector vc = new Vector();
		MethodProxy[] cn = c.getConstructors();
		for (int i = 0; i < cn.length; i++) {
			if (Modifier.isPublic(cn[i].getModifiers())) {
				vc.addElement(cn[i]);
			}
		}
		constructors = new ConstructorDescriptorProxy[vc.size()];
		for (int i = 0; i < vc.size(); i++) {
			// constructors[i] = new AConstructorDescriptorProxy(new
			// ConstructorDescriptor((Constructor) vc.elementAt(i)));
			constructors[i] = new AConstructorDescriptorProxy(
					(MethodProxy) vc.elementAt(i));
			// Add a zero argument constructor to the menu
			// if (((Constructor) vc.elementAt(i)).getParameterTypes().length ==
			// 0) {
			if (((MethodProxy) vc.elementAt(i)).getParameterTypes().length == 0) {
				constructors[i].setValue(AttributeNames.METHOD_MENU_NAME,
						AttributeNames.NEW_OBJECT_MENU_NAME);
				constructors[i].setValue(AttributeNames.POSITION,
						new Integer(0));
				// constructors[i].setDisplayName("New "+c.getName()+" ...");
				constructors[i].setDisplayName("New "
						+ toShortName(c.getName()));
			} else {
				constructors[i].setValue(AttributeNames.METHOD_MENU_NAME,
						AttributeNames.NEW_OBJECT_MENU_NAME);
				constructors[i].setDisplayName("New "
						+ toShortName(c.getName()) + "...");
			}
		}
	}

	static public String toShortName(String s) {
		int i = s.lastIndexOf('.');
		if (i < s.length() && i >= 0) {
			uiClassFinder.addImport(s.substring(0, i));
			return s.substring(i + 1, s.length());

		} else
			return s;
	}

	// check that what we return is an interface
	public static String toInterfaceName(String className) {

		String retVal;
		int i = className.indexOf(uiClassMapper.CLASS_PREFIX2);
		if (i < className.length() && i >= 0)
			retVal = className.substring(
					i + uiClassMapper.CLASS_PREFIX2.length(),
					className.length());
		else {
			i = className.indexOf(uiClassMapper.CLASS_PREFIX1);
			if (i < className.length() && i >= 0)
				retVal = className.substring(
						i + uiClassMapper.CLASS_PREFIX1.length(),
						className.length());
			else {
				i = className.lastIndexOf(uiClassMapper.CLASS_SUFFIX);
				if (i < className.length() && i >= 0)
					retVal = className.substring(0, i);
				else
					retVal = className;
			}
		}
		if (retVal != className)
			try {
				if (Class.forName(retVal).isInterface())
					return retVal;
			} catch (Exception e) {
			}
		;
		return className;

	}

	public static String toInterfaceName(ClassProxy c) {
		String cName = toShortName(c.getName());
		int numContainedInterfaceNames = 0;
		String containedInterfaceName = cName;
		ClassProxy[] interfaceList = c.getInterfaces();
		for (int j = 0; j < interfaceList.length; j++) {
			String iName = toShortName(interfaceList[j].getName());
			int i = cName.indexOf(iName);
			if (i >= 0 && i <= cName.length()) {
				numContainedInterfaceNames++;
				containedInterfaceName = iName;
				// System.out.println("found " + containedInterfaceName);
			}
		}
		if (numContainedInterfaceNames == 1)
			return containedInterfaceName;
		else
			return cName;
	}

	/*
	 * public static String toInterfaceName(Class c) { String cName =
	 * toShortName(c.getName()); boolean foundShortName = false; Class[]
	 * interfaceList = c.getInterfaces(); for (int i = 0; i <
	 * interfaceList.length; i ++) { if (interfaceList.length != 1) return
	 * cName; else { String iName = toShortName(interfaceList[0].getName()); int
	 * i = cName.indexOf(iName); if (i >= 0 && i <= cName.length()) return
	 * iName; else return cName; } }
	 */
	final static String DOUBLE_CLICK_NAME = "open";
	final static String SELECT_NAME = "selected";
	final static String EXPAND_NAME = "expanded";

	/*
	 * void checkIfDoubleClickMethod(MethodDescriptor md) { Method m
	 * =md.getMethod(); //Method m = md.getMethod(); if
	 * (m.getParameterTypes().length != 1) return; if
	 * (ClassDescriptorCache.toBoolean
	 * (md.getValue(AttributeNames.DOUBLE_CLICK_METHOD)) ||
	 * m.getName().equals(DOUBLE_CLICK_NAME))
	 * doubleClickMethodsVector.addElement(m);
	 * 
	 * }
	 */
	boolean isDefaultSelectMethod(MethodProxy m) {
		/*
		 * int numParameters = m.getParameterTypes().length; boolean
		 * legalNumberOfParameters = numParameters == 1 || numParameters == 2;
		 */
		return m.getName().toLowerCase().equals(SELECT_NAME);
	}

	boolean isDefaultExpandMethod(MethodProxy m) {
		/*
		 * int numParameters = m.getParameterTypes().length; boolean
		 * legalNumberOfParameters = numParameters == 1 || numParameters == 2;
		 */
		return m.getName().toLowerCase().equals(EXPAND_NAME);
	}

	public boolean checkSelectDoubleClickParams(MethodProxy m) {
		int numParameters = m.getParameterTypes().length;
		return numParameters == 1 || numParameters == 2 || numParameters == 3;
	}

	void checkIfEventMethod(MethodDescriptorProxy md) {
		MethodProxy m = VirtualMethodDescriptor.getVirtualMethod(md);
		// Method m = md.getMethod();
		if (!checkSelectDoubleClickParams(m))
			return;
		/*
		 * if (m.getParameterTypes().length != 1 ) return;
		 */
		if (ClassDescriptorCache.toBoolean(md
				.getValue(AttributeNames.DOUBLE_CLICK_METHOD))
				|| m.getName().equals(DOUBLE_CLICK_NAME))
			if (!doubleClickMethodsVector.contains(m))
				doubleClickMethodsVector.addElement(m);
		if (ClassDescriptorCache.toBoolean(md
				.getValue(AttributeNames.SELECT_METHOD))
		// || m.getName().equals(SELECT_NAME))
				|| isDefaultSelectMethod(m))
			if (!selectMethodsVector.contains(m))
				selectMethodsVector.addElement(m);
		if (ClassDescriptorCache.toBoolean(md
				.getValue(AttributeNames.EXPAND_METHOD))
		// || m.getName().equals(SELECT_NAME))
				|| isDefaultExpandMethod(m))
			if (!expandMethodsVector.contains(m))
				expandMethodsVector.addElement(m);

	}

	public MethodProxy[] getDoubleClickMethods() {
		return doubleClickMethods;
	}

	public MethodProxy[] getSelectMethods() {
		return selectMethods;
	}

	public MethodProxy[] getExpandMethods() {
		return expandMethods;
	}

	public static int METHODS_IN_RIGHT_MENU = 100; // decides the number of
													// buttons that can be
													// on the toolbar (virtual
													// toolbar size)
													// i'm making it large for
													// now.

	static public boolean ignoreMethodsOfClass(ClassProxy c) {
		return c.equals(AClassProxy.classProxy(String.class))
				|| c.equals(AClassProxy.classProxy(Object.class))
				|| AClassProxy.classProxy(Number.class).isAssignableFrom(c)
				|| c.isPrimitive();

		// || c == Object.class || (Number.class).isAssignableFrom(c);
	}

	public static boolean ignoreMethod(MethodProxy m) {
		return m != null
				&& (IntrospectUtility.isPropertyChangeListenerMethod(m)
						|| IntrospectUtility.isTableModelListenerMethod(m)
						|| IntrospectUtility.isAddExecutedCommandListener(m)
						|| IntrospectUtility.isGetMethod(m)
						|| IntrospectUtility.isIsEmptyMethod(m) || ignoreMethodsOfClass(m
						.getDeclaringClass()));
	}

	public static boolean ignoreMethod(String name) {
		return name.indexOf("Listener") >= 0;
	}

	public static String parameterTypesToString(MethodProxy method) {
		ClassProxy[] pTypes = method.getParameterTypes();
		String retVal = "";
		if (pTypes.length == 0)
			return retVal;
		else
			retVal = "(";
		for (int i = 0; i < pTypes.length; i++) {
			if (i > 0)
				// retVal += ',';
				retVal += ", ";
			retVal += pTypes[i].getSimpleName();
		}
		return retVal + ')';
	}

	public static String getSignature(MethodProxy method) {
		String toString = method.toString();
		// String toString = parameterTypesToString(method);
		int leftParenStart = toString.indexOf('(');
		if (leftParenStart == -1)
			return "";
		int leftParenEnd = toString.indexOf(')');
		if (leftParenEnd == -1)
			return "";
		String retVal = toString.substring(leftParenStart, leftParenEnd + 1);
		return retVal;
	}

	/*
	 * public static String getSignature (MethodProxy m) { return m.getName() +
	 * parameterTypesToString(m); }
	 */
	public static String beautify(MethodProxy m, String name, String displayName) {
		if (name.equals(displayName)) {
			String beautifiedName = util.misc.Common.beautify(displayName);
			if (m.getParameterTypes().length != 0)
				// methods[i].setDisplayName(uiGenerator.beautify(methods[i].getName(),
				// methods[i].getDisplayName()) + " ...");
				// md.setDisplayName(beautifiedName + " ...");
				// return beautifiedName + getSignature(m);
				return beautifiedName + parameterTypesToString(m);
			else
				// methods[i].setDisplayName(uiGenerator.beautify(methods[i].getDisplayName()));
				return beautifiedName;

		} else
			return displayName;
	}

	public static Annotation getMethodAnnotation(MethodProxy m,
			Class annotationClass) {
		Annotation annotation = m.getAnnotation(annotationClass);
		if (annotation != null)
			return annotation;

		// if (m.getMethod() == null)
		if (!m.isMethod())
			return null;

		// Class declaringClass = m.getMethod().getDeclaringClass();
		ClassProxy declaringClass = m.getDeclaringClass();
		ClassProxy[] interfaces = declaringClass.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (excludeInterfacesSet.contains(interfaces[i].getName()))
				continue;
			// explanation = getMethodAnnotationInInterface (m.getMethod(),
			// interfaces[i]);
			annotation = getMethodAnnotationInInterface(m, interfaces[i],
					annotationClass);
			if (annotation != null)
				return annotation;
		}
		return null;

	}

	/*
	 * static util.IntAttributes getMethodIntAttributes(MethodProxy m) { return
	 * (util.IntAttributes) getMethodAnnotation(m, util.IntAttributes.class); }
	 */
	static util.annotations.Explanation getMethodExplanation(MethodProxy m) {
		return (util.annotations.Explanation) getMethodAnnotation(m,
				util.annotations.Explanation.class);
		/*
		 * util.Explanation explanation =
		 * m.getAnnotation(util.Explanation.class); if (explanation != null)
		 * return explanation;
		 * 
		 * //if (m.getMethod() == null) if (!m.isMethod()) return null;
		 * 
		 * 
		 * //Class declaringClass = m.getMethod().getDeclaringClass();
		 * ClassProxy declaringClass = m.getDeclaringClass(); ClassProxy[]
		 * interfaces = declaringClass.getInterfaces(); for (int i = 0; i <
		 * interfaces.length; i++) { //explanation =
		 * getMethodAnnotationInInterface (m.getMethod(), interfaces[i]);
		 * explanation = getMethodExplanationInInterface (m, interfaces[i]); if
		 * (explanation != null) return explanation; } return null;
		 */

	}

	/*
	 * static ClassProxy[] excludeAnnotationInterfaces = {
	 * AClassProxy.classProxy(util.Listenable.class),
	 * AClassProxy.classProxy(slm.ShapesList.class),
	 * AClassProxy.classProxy(bus.uigen.HashtableListenable.class),
	 * AClassProxy.classProxy(bus.uigen.HashtableInterface.class),
	 * AClassProxy.classProxy(bus.uigen.VectorInterface.class),
	 * AClassProxy.classProxy(bus.uigen.VectorListenable.class),
	 * AClassProxy.classProxy(bus.uigen.DynamicEnum.class) }; static Vector
	 * excludeAnnotationInterfacesVector =
	 * util.Misc.deepArrayToVector(excludeAnnotationInterfaces);
	 */
	/*
	 * static util.Explanation getMethodExplanationInInterface(MethodProxy m,
	 * ClassProxy intf) { //Method[] methods = intf.getMethods(); MethodProxy[]
	 * methods = uiBean.getMethods(intf); if ((intf instanceof AClassProxy) &&
	 * util.Misc.isJavaClass(((AClassProxy)intf).getJavaClass())) return null;
	 * if (excludeAnnotationInterfacesVector.contains(intf)) return null;
	 * 
	 * for (int i = 0; i < methods.length; i++) { //VirtualMethod vMethod =
	 * uiMethodInvocationManager.virtualMethod(methods[i]); //if
	 * (equals(methods[i], m)) { if (m.equals(methods[i])) { util.Explanation
	 * explanation = methods[i].getAnnotation(util.Explanation.class); if
	 * (explanation != null) return explanation; } } return null;
	 * 
	 * }
	 */
	static String[] excludeInterfacesArray = { "java.io.Serializable",
			"java.util.EventListener", "java.awt.event.ActionListener",
			"java.lang.Runnable", "java.lang.Cloneable", "java.rmi.Remote",
			"bus.uigen.loggable.AwareVectorListener",
			"bus.uigen.loggable.AwareVectorMethodsListener",
			"bus.uigen.loggable.AwareHashtableListener",
			"java.util.EventListener", "javax.swing.event.TableModelListener",
			"javax.swing.event.TreeModelListener", "java.util.Observer",
			"java.lang.Comparable", "util.Listenable", slm.ShapesList.class.getName(),
			"bus.uigen.HashtableListenable", "bus.uigen.HashtableInterface",
			"bus.uigen.VectorInterface.class",
			"bus.uigen.VectorListenable.class", "bus.uigen.DynamicEnum.class",
			"java.lang.Iterable", "java.util.RandomAccess",
			"bus.uigen.loggable.IdentifiableLoggable" };
	static Set<String> excludeInterfacesSet = util.misc.Common
			.arrayToSet(excludeInterfacesArray);

	static Annotation getMethodAnnotationInInterface(MethodProxy m,
			ClassProxy intf, Class annotationClass) {
		// Method[] methods = intf.getMethods();
		MethodProxy[] methods = IntrospectUtility.getMethods(intf);
		if ((intf instanceof AClassProxy)
				&& util.misc.Common.isJavaClass(((AClassProxy) intf)
						.getJavaClass()))
			return null;
		/*
		 * if (excludeAnnotationInterfacesVector.contains(intf)) return null;
		 */

		for (int i = 0; i < methods.length; i++) {
			// VirtualMethod vMethod =
			// uiMethodInvocationManager.virtualMethod(methods[i]);
			// if (equals(methods[i], m)) {
			if (m.equals(methods[i])) {
				Annotation annotation = methods[i]
						.getAnnotation(annotationClass);
				if (annotation != null)
					return annotation;
			}
		}
		return null;

	}

	/*
	 * static boolean equals (Method m1, Method m2) { return
	 * m1.getName().equals(m2.getName()) &&
	 * m1.getReturnType().equals(m2.getReturnType()) &&
	 * m1.getParameterTypes().equals(m2.getParameterTypes());
	 * 
	 * }
	 */
	public static Vector<Attribute> toAttributeVector(
			util.annotations.IntAttributes intAttributes) {
		if (intAttributes == null)
			return null;
		// Hashtable<String, Integer> attributeTable = new Hashtable();
		Vector<Attribute> attributeTable = new Vector();
		for (int i = 0; i < intAttributes.names().length
				&& i < intAttributes.values().length; i++) {
			Attribute attr = new Attribute(intAttributes.names()[i],
					intAttributes.values()[i]);
			attributeTable.add(attr);
			// attributeTable.put(intAttributes.names()[i],
			// intAttributes.values()[i]);
		}
		return attributeTable;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.StringAttributes stringAttributes) {
		if (stringAttributes == null)
			return null;
		// Hashtable<String, Integer> attributeTable = new Hashtable();
		Vector<Attribute> attributeTable = new Vector();
		for (int i = 0; i < stringAttributes.names().length
				&& i < stringAttributes.values().length; i++) {
			Attribute attr = new Attribute(stringAttributes.names()[i],
					stringAttributes.values()[i]);
			attributeTable.add(attr);
			// attributeTable.put(intAttributes.names()[i],
			// intAttributes.values()[i]);
		}
		return attributeTable;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.BooleanAttributes booleanAttributes) {
		if (booleanAttributes == null)
			return null;
		// Hashtable<String, Integer> attributeTable = new Hashtable();
		Vector<Attribute> attributeTable = new Vector();
		for (int i = 0; i < booleanAttributes.names().length
				&& i < booleanAttributes.values().length; i++) {
			Attribute attr = new Attribute(booleanAttributes.names()[i],
					booleanAttributes.values()[i]);
			attributeTable.add(attr);
			// attributeTable.put(intAttributes.names()[i],
			// intAttributes.values()[i]);
		}
		return attributeTable;
	}

	void initMethodAttribs(MethodDescriptorProxy md) {
		MethodProxy m = VirtualMethodDescriptor.getVirtualMethod(md);
		List<MethodProxy> superMethods = IntrospectUtility.getSuperMethods(m);
		String explanations = getExplanationAnnotationOfMethod(superMethods);
		if (explanations != null)
			md.setValue(AttributeNames.EXPLANATION, explanations);
		util.annotations.Explanation explanation = getMethodExplanation(m);
		// if (explanation != null)
		// md.setValue(AttributeNames.EXPLANATION_ANNOTATION,
		// explanation.value());

		Vector<Attribute> mergedAttributeVector = getMergedAttributeVector(m);
		if (mergedAttributeVector != null)
			md.setValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS,
					mergedAttributeVector);
		Object position = md.getValue(AttributeNames.POSITION);
		md.setDisplayName(beautify(m, md.getName(), md.getDisplayName()));
		
		if (md.getValue(AttributeNames.METHOD_MENU_NAME) == null) {
			// if (ignoreMethod(m.getMethod())) return;
			if (ignoreMethod(m))
				return;
			
			// Check if the method belongs to a super class
			/*
			 * if (m.getName().equals("calculateBMI"))
			 * System.out.println(" Method " + m + "Declaring class" +
			 * m.getDeclaringClass() + " class " + c);
			 */
			if (!m.isMethod()
					|| (m.getDeclaringClass().equals(realClass) && !Modifier
							.isStatic(m.getModifiers()))) {
				

				// we will let the interpreter decide what default to use
				// md.setValue(AttributeNames.MENU_NAME, getMethodsMenuName(m));
				// let us not put it by default in the toolbar
				// md.setValue(AttributeNames.PLACE_TOOLBAR,
				// uiFrame.TOOLBAR_PANEL_NAME/*"toolbar"*/);
				// localNum++;

			} else {
				
				// let us not put it by default in the toolbar
				// md.setValue(AttributeNames.TOOLBAR, new Boolean(true));
				// md.setValue(AttributeNames.MENU_NAME, getMethodsMenuName(m));
				// let us not put it by default in the toolbar
				md.setValue(AttributeNames.PLACE_TOOLBAR,
						AttributeNames.TOOLBAR_PANEL_NAME);

			}

		}

		checkIfEventMethod(md);

	}

	@Override
	public MethodProxy getAddPropertyChangeListenerMethod() {
		return addPropertyListener;
	}

	public MethodProxy getAddRemotePropertyChangeListenerMethod() {
		return addRemotePropertyListener;
	}

	public MethodProxy getAddVectorListenerMethod() {
		return addVectorListener;
	}

	public MethodProxy getAddHashtableListenerMethod() {
		return addHashtableListener;
	}

	public MethodProxy getAddTreeModelListenerMethod() {
		return addTreeListener;
	}

	public MethodProxy getAddTableModelListenerMethod() {
		return addTableListener;
	}

	@Override
	public MethodProxy getAddObserverMethod() {
		return addObserver;
	}

	@Override
	public MethodProxy getAddRefresherMethod() {
		return addRefresher;
	}

	private void initMethodAttribs() {

		int localNum = 0;
		if (methods == null) return;
		for (int i = 0; i < methods.length; i++) {
			// Method m = methods[i].getMethod();
			MethodProxy m = VirtualMethodDescriptor
					.getVirtualMethod(methods[i]);
			if (IntrospectUtility.isVolatile(m))
				continue;
			initMethodAttribs(methods[i]);
			Boolean explanationMethod = (Boolean) getMethodAttribute(methods[i], AttributeNames.RETURNS_CLASS_EXPLANATION);
			if (explanationMethod != null && explanationMethod) {
				setAttribute(AttributeNames.EXPLANATION_METHOD, methods[i].getMethod());
			}
			
			Boolean webDocumentsMethod = (Boolean) getMethodAttribute(methods[i], AttributeNames.RETURNS_CLASS_WEB_DOCUMENTS);
			if (webDocumentsMethod != null && webDocumentsMethod) {
				setAttribute(AttributeNames.WEB_DOCUMENTS_METHOD, methods[i].getMethod());
			}			
			
		}
		doubleClickMethods = toArray(doubleClickMethodsVector);
		selectMethods = toArray(selectMethodsVector);
		expandMethods = toArray(expandMethodsVector);
	}

	
	public void setAttributeWithoutAddingKeyword(String attribute, Object value) {
		getBeanDescriptor().setValue(attribute, value);

		if (attribute.equals(AttributeNames.ONLY_DYNAMIC_METHODS)) {
			if (((Boolean) value).booleanValue()) {
				includeOnlyDynamicMethods();
			}

		} else if (attribute.equals(AttributeNames.ONLY_DYNAMIC_PROPERTIES)) {
			if (((Boolean) value).booleanValue()) {
				includeOnlyDynamicProperties();
			}
		}
	}

	public void setAttribute(String attribute, Object value) {
		
		setAttributeWithoutAddingKeyword(attribute, value);
		notifyAttributeAdded(this, attribute, value);
		

	}

	static Set<ClassDescriptorListener> listeners = new HashSet();

	public static void addListener(ClassDescriptorListener listener) {
		listeners.add(listener);
	}

	public static void notifyAttributeAdded(ClassDescriptorInterface cd,
			String attributeName, Object value) {
		for (ClassDescriptorListener listener : listeners) {
			listener.attributeAdded(cd, attributeName, value);
		}
	}

	public Object getAttribute(String attribute) {
		Object retVal = getBeanDescriptor().getValue(attribute);
		if (retVal == null && AttributeNames.NAME_PROPERTY.equals(attribute))
			return AttributeNames.KEY_LABEL;
//		// hope this does not cause issues
//		if (retVal == null)
//			retVal = getAnnotationAttribute(getBeanDescriptor(), attribute);
			

		return retVal;
	}

	public PropertyDescriptorProxy createPropertyDescriptor(String name) {
		PropertyDescriptorProxy pd = namesToPropertyDescriptors.get(name);
		if (pd == null) {
			try {
				pd = new APropertyDescriptorProxy(name, null, null);
			} catch (Exception e) {
				pd = null;
			}
			namesToPropertyDescriptors.put(name, pd);
		}
		return pd;
	}

	public void setPropertyAttribute(String property, String attribute,
			Object value) {
		PropertyDescriptorProxy pd = getPropertyDescriptor(property);
		/*
		 * if (pd == null) {System.out.println("Unknown property " + property);
		 * else pd.setValue(attribute, value);
		 */
		if (pd == null) {
			if (AttributeNames.ANY_KEY.equals(property)
					|| AttributeNames.ANY_VALUE.equals(property)
					|| AttributeNames.ANY_ELEMENT.equals(property))
				pd = addProperty(property);
			else
				// this probbaly handles view group stuff
				pd = createPropertyDescriptor(property);
			/*
			 * pd = namesToPropertyDescriptors.get(property); if (pd == null) {
			 * try { pd = new PropertyDescriptor(property, null, null); } catch
			 * (Exception e) { pd = null; }
			 * namesToPropertyDescriptors.put(property, pd); }
			 */
		}
//		if (attribute.equals(AttributeNames.VISIBLE) && ((Boolean) value) == false) {
//			
//		}
		pd.setValue(attribute, value);
	}

	public void setAttributeOfAllProperties(String attribute, Object value) {
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptorProxy pd = properties[i];
			if (!pd.getName().equalsIgnoreCase("class")) // this property is shared by all objects
				pd.setValue(attribute, value);
		}

	}

	public Object getPropertyAttribute(String property, String attribute) {
		PropertyDescriptorProxy pd = getPropertyDescriptor(property);
		/*
		 * if (pd == null) {System.out.println("Unknown property " + property);
		 * else pd.setValue(attribute, value);
		 */
		if (pd == null) {
			return null;
			// pd = createPropertyDescriptor(property);
			/*
			 * pd = namesToPropertyDescriptors.get(property); if (pd == null) {
			 * try { pd = new PropertyDescriptor(property, null, null); } catch
			 * (Exception e) { pd = null; }
			 * namesToPropertyDescriptors.put(property, pd); }
			 */
		}
		return pd.getValue(attribute);
	}

	public VirtualMethodDescriptor createVirtualMethodDescriptor(String command) {
		VirtualMethodDescriptor vmd = namesToMethodDescriptors.get(command);
		if (vmd == null) {
			vmd = new VirtualMethodDescriptor();
			namesToMethodDescriptors.put(command, vmd);
		}
		return vmd;

	}

	/*
	 * public MethodDescriptor getMethodDescriptor (Method method) { for (int i
	 * = 0; i < methods.length; i++) { if (methods[i].getMethod() == method)
	 * return methods[i]; } return null; }
	 */
	public Object getMethodAttribute(String method, String attribute) {
		if (method == null)
			return null;
		MethodDescriptorProxy md = this.getMethodDescriptor(method);
		if (md == null) {
			// System.out.println("Unknown method " + method);
			/*
			 * VirtualMethodDescriptor vmd =
			 * namesToMethodDescriptors.get(method); if (vmd == null) { vmd =
			 * new VirtualMethodDescriptor();
			 * namesToMethodDescriptors.put(method, vmd); } md = vmd;
			 */
			md = createVirtualMethodDescriptor(method);
		}
		Object retVal = getMethodAttribute(md, attribute);
		if (retVal == null)
			retVal = allMethods.getValue(attribute);
		return retVal;
		// return md.getValue(attribute);
		// return getMethodAttribute(md, attribute);
	}
	@Override
	public Object getNonDefaultMethodAttribute(String method, String attribute) {
		if (method == null)
			return null;
		MethodDescriptorProxy md = this.getMethodDescriptor(method);
		if (md == null) {
			// System.out.println("Unknown method " + method);
			/*
			 * VirtualMethodDescriptor vmd =
			 * namesToMethodDescriptors.get(method); if (vmd == null) { vmd =
			 * new VirtualMethodDescriptor();
			 * namesToMethodDescriptors.put(method, vmd); } md = vmd;
			 */
			md = createVirtualMethodDescriptor(method);
		}
		Object retVal = getNonDefaultMethodAttribute(md, attribute);
		if (retVal == null)
			retVal = allMethods.getValue(attribute);
		return retVal;
		// return md.getValue(attribute);
		// return getMethodAttribute(md, attribute);
	}
	
	public static Object getNonDefaultMethodAttribute(MethodDescriptorProxy md,
			String attribute) {
		if (md == null)
			return null; // taking care of excluded methods for undo
		Object retVal = md.getValue(attribute);
		if (retVal != null) return retVal;
//		if (retVal == null) {
		else {
			// Hashtable<String, Integer> intAttributeTable = (Hashtable)
			// md.getValue(AttributeNames.INT_ATTRIBUTES_ANNOTATION);
			Vector<Attribute> intAttributeTable = (Vector) md
					.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
			if (intAttributeTable == null)
//				return AttributeNames.getDefaultOrSystemDefault(attribute);
			 return null;
			// return intAttributeTable.get(attribute);
			return Attribute.getAttribute(intAttributeTable, attribute);
		}
		// this was duplciated code
//		retVal = md.getValue(attribute);
//		if (retVal == null)
//			return retVal;
//		return md.getValue(attribute);
	}

	public static Object getMethodAttribute(MethodDescriptorProxy md,
			String attribute) {
		if (md == null)
			return null; // taking care of excluded methods for undo
		Object retVal = md.getValue(attribute);
		if (retVal != null) return retVal;
//		if (retVal == null) {
		else {
			// Hashtable<String, Integer> intAttributeTable = (Hashtable)
			// md.getValue(AttributeNames.INT_ATTRIBUTES_ANNOTATION);
			Vector<Attribute> intAttributeTable = (Vector) md
					.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
			if (intAttributeTable == null)
				return AttributeNames.getDefaultOrSystemDefault(attribute);
			// return null;
			// return intAttributeTable.get(attribute);
			return Attribute.getAttribute(intAttributeTable, attribute);
		}
		// this was duplciated code
//		retVal = md.getValue(attribute);
//		if (retVal == null)
//			return retVal;
//		return md.getValue(attribute);
	}

	public void setMethodAttribute(MethodProxy method, String attribute,
			Object value) {
		MethodDescriptorProxy md = this.getMethodDescriptor(method);
		if (md != null)
			md.setValue(attribute, value);
	}

	public void setMethodAttribute(String method, String attribute, Object value) {
		MethodDescriptorProxy md = this.getMethodDescriptor(method);
		if (md == null) {
			// System.out.println("Unknown method " + method);
			/*
			 * VirtualMethodDescriptor vmd =
			 * namesToMethodDescriptors.get(method); if (vmd == null) { vmd =
			 * new VirtualMethodDescriptor();
			 * namesToMethodDescriptors.put(method, vmd); } md = vmd;
			 */
			md = createVirtualMethodDescriptor(method);
		}
		md.setValue(attribute, value);
	}

	public void setAttributeOfAllMethods(String attribute, Object value) {
		allMethods.setValue(attribute, value);
		for (int i = 0; i < methods.length; i++) {
			MethodDescriptorProxy md = methods[i];
			Object existing = md.getValue(attribute);
			if (existing == null) // do not overwrite specific value
			md.setValue(attribute, value);
		}

	}

	public PropertyDescriptorProxy getPropertyDescriptor(String name) {
		for (int i = 0; i < properties.length; i++) {
			if (name.toLowerCase()
					.equals(properties[i].getName().toLowerCase()))
				return properties[i];
		}
		PropertyDescriptorProxy[] indexOrKey = getIndexOrKeyPropertyDescriptors();
		for (int i = 0; i < indexOrKey.length; i++) {
			if (name.toLowerCase()
					.equals(indexOrKey[i].getName().toLowerCase()))
				return indexOrKey[i];
		}

		return null;
	}

	public PropertyDescriptorProxy getDynamicPropertyDescriptor(String name) {
		for (int i = 0; i < properties.length; i++) {
			if (name.toLowerCase()
					.equals(properties[i].getName().toLowerCase()))
				return properties[i];
		}
		return null;
	}

	public MethodDescriptorProxy getMethodDescriptor(String name) {
		if (name == null)
			return null;

		String nameToLowerCase = name.toLowerCase();
		for (int i = 0; i < methods.length; i++) {
			String candidateNameWithSignature = IntrospectUtility
					.nameWithSignature(methods[i].getMethod()).toLowerCase();
			// String candidateName = methods[i].getName().toLowerCase();
			// if
			// (name.toLowerCase().equals(methods[i].getName().toLowerCase()))
			if (nameToLowerCase.equals(candidateNameWithSignature))
				return methods[i];
		}
		for (int i = 0; i < methods.length; i++) {
			// String candidateNameWithSignature =
			// uiBean.nameWithSignature(methods[i].getMethod()).toLowerCase();
			String candidateName = methods[i].getName().toLowerCase();
			// if
			// (name.toLowerCase().equals(methods[i].getName().toLowerCase()))
			if (nameToLowerCase.equals(candidateName))
				return methods[i];
		}
		return null;
	}

	public MethodDescriptorProxy getMethodDescriptor(MethodProxy method) {
		if (method == null)
			return null;
		for (int i = 0; i < methods.length; i++) {
			MethodProxy candidate = methods[i].getMethod();
			// if (method == methods[i].getMethod())
			if (equals(method, methods[i].getMethod()))
				return methods[i];
		}
		return null;
	}

	/*
	 * public MethodDescriptorProxy getMethodDescriptor (VirtualMethod method) {
	 * if (method == null) return null; for (int i = 0; i < methods.length; i++)
	 * { //if (method.getMethod() == methods[i].getMethod()) VirtualMethod
	 * vMethod = AVirtualMethod.virtualMethod(methods[i].getMethod()); if
	 * (method.equals(vMethod)) return methods[i]; } return null; }
	 */

	public MethodDescriptorProxy getDynamicCommandsMethodDescriptor() {
		MethodProxy method = DynamicMethods.getDynamicCommandsMethod(realClass);
		if (method == null)
			return null;
		return getMethodDescriptor(method);

	}

	public MethodDescriptorProxy getDynamicPropertiesGetter() {
		MethodProxy method = DynamicMethods.getDynamicPropertyGetter(realClass);
		if (method == null)
			return null;
		return getMethodDescriptor(method);

	}

	public MethodDescriptorProxy getDynamicPropertiesSetter() {
		MethodProxy getter = DynamicMethods.getDynamicPropertyGetter(realClass);
		if (getter == null)
			return null;
		MethodProxy method = DynamicMethods.getDynamicPropertySetter(realClass,
				getter.getReturnType());
		if (method == null)
			return null;
		return getMethodDescriptor(method);

	}

	void add(Vector sortedList, FeatureDescriptorProxy f) {
		int initVal = 0;
		if (sortedList.size() > 0 &&
		// ((FeatureDescriptor)
		// sortedList.elementAt(0)).getName().equals("name"))
				((FeatureDescriptorProxy) sortedList.elementAt(0)).getName()
						.equals(AttributeNames.NAME_PROPERTY))
			initVal = 1;

		for (int i = initVal; i < sortedList.size(); i++) {
			FeatureDescriptorProxy storedFeature = (FeatureDescriptorProxy) sortedList
					.elementAt(i);
			// if (f.getName().toLowerCase().equals("name")) {
			String nameLabel = ((String) getAttribute(AttributeNames.NAME_PROPERTY))
					.toLowerCase();
			if (f.getName().toLowerCase().equals(nameLabel)) {
				sortedList.insertElementAt(f, 0);
				return;
			}
			if (f.getName().compareTo(storedFeature.getName()) < 0) {
				sortedList.insertElementAt(f, i);
				return;
			}
		}
		sortedList.addElement(f);
	}

	public Object getMethodAttribute(MethodProxy m, String name) {
		if (m == null)
			return null;
		MethodDescriptorProxy md = getMethodDescriptor(m);
		if (md == null)
			return null;
		return getMethodAttribute(md, name);
		// return md.getValue(name);
	}

	public String getToolTipText(MethodProxy m) {
		Object val = getMethodAttribute(m,
				AttributeNames.EXPLANATION);
		return (String) val;
		/*
		 * if (val == null) return ""; return (String) val;
		 */

	}

	public static Vector<Attribute> merge(Vector<Attribute> list1,
			Vector<Attribute> list2) {
		if (list1 == null)
			return list2;
		else if (list2 == null)
			return list1;
		else {
			for (int i = 0; i < list2.size(); i++)
				list1.add((list2.elementAt(i)));
			return list1;
		}

	}

	Integer getPosition(Vector<Attribute> attributes) {
		if (attributes == null)
			return null;
		Integer retVal = null;
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).getAttributeName()
					.equals(AttributeNames.POSITION)) {
				return (Integer) attributes.get(i).getValue();
			}
		}
		return null;

	}
	
	public static Object getAnnotationAttribute (FeatureDescriptorProxy feature, String attributeName) {
		Object merged = feature.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
		if (merged == null)
			return null;
		Vector<Attribute> mergedAttributes = (Vector) merged;
		for (Attribute a:mergedAttributes) {
			if (a.getAttributeName().equals(attributeName)) {
				return a.getValue();
			}
		}
		return null;		
	}
	
	public Vector<Attribute> getWriteMethodAttributes(PropertyDescriptorProxy pd) {
		MethodProxy writeMethod = pd.getWriteMethod();
		MethodDescriptorProxy writeMD = getMethodDescriptor(writeMethod);
		Vector<Attribute> writeMergedAttributes = null;
		if (writeMD != null) {
			writeMergedAttributes = (Vector) writeMD
					.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
		}
		return writeMergedAttributes;
	}
	
	public Vector<Attribute> getReadMethodAttributes(PropertyDescriptorProxy pd) {
		MethodProxy readMethod = pd.getReadMethod();
		MethodDescriptorProxy readMD = getMethodDescriptor(readMethod);

		Vector<Attribute> readMergedAttributes = null;
		if (readMD != null) {
			readMergedAttributes = (Vector) readMD
					.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
			// pos = (Integer) readMD.getValue(AttributeNames.POSITION);
		}
		return readMergedAttributes;
	}

	public void initPropertyMergedAttributes(PropertyDescriptorProxy pd) {
//		MethodProxy readMethod = pd.getReadMethod();
//		MethodDescriptorProxy readMD = getMethodDescriptor(readMethod);
//
//		Vector<Attribute> readIntAttributes = null;
//		if (readMD != null) {
//			readIntAttributes = (Vector) readMD
//					.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
//			// pos = (Integer) readMD.getValue(AttributeNames.POSITION);
//		}
//		MethodProxy writeMethod = pd.getWriteMethod();
//		MethodDescriptorProxy writeMD = getMethodDescriptor(writeMethod);
//		Vector<Attribute> writeMergedAttributes = null;
//		if (writeMD != null) {
//			writeMergedAttributes = (Vector) writeMD
//					.getValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
//		}
//
//		Vector<Attribute> mergedAttributes = merge(readIntAttributes,
//				writeMergedAttributes);
//		Vector<Attribute> mergedAttributes = merge(getReadMethodAttributes(pd), getWriteMethodAttributes(pd));
		Vector<Attribute> mergedAttributes = getReadMethodAttributes(pd);


		if (mergedAttributes != null)
			pd.setValue(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS,
					mergedAttributes);
		Integer pos = getPosition(mergedAttributes);
		if (pos != null) {
			pd.setValue(AttributeNames.POSITION, pos);
		}

	}

	private void initPropertyAttribs() {
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptorProxy pd = properties[i];
			initPropertyMergedAttributes(pd);
			String propertyString;
			String readString = getToolTipText(pd.getReadMethod());
			String writeString = getToolTipText(pd.getWriteMethod());

			if ((readString == null || readString == "")
					&& (writeString == "" || writeString == null))
				continue;
			propertyString = "";
			if (readString != null)
				propertyString += readString;
			if (writeString != null)
				propertyString += writeString;
			pd.setValue(AttributeNames.EXPLANATION, propertyString);

		}
		/*
		 * Vector sortedList = new Vector(); for (int i=0; i <
		 * properties.length; i++) { PropertyDescriptor p = properties[i]; for
		 * (j = 0; j < sortedList.length; j++) { PropertyDescriptor
		 * storedProperty = (PropertyDescriptor) sortedList.elementAt(j); if
		 * (p.getName().compareTo(storedProperty.getName()) < 0) {
		 * sortedList.insertElementAt(p, j); break; } } } for (int i = 0; i <
		 * sortedList.length; i++ ) { PropertyDescriptor p =
		 * (PropertyDescriptor) sortedList.elementAt(i); if
		 * (p.getValue(AttributeNames.POSITION) == null) {
		 * 
		 * }
		 * 
		 * }
		 */

	}

	MethodProxy[] toArray(Vector methodsVector) {
		MethodProxy[] retVal = new MethodProxy[methodsVector.size()];
		for (int i = 0; i < retVal.length; i++)
			retVal[i] = (MethodProxy) methodsVector.elementAt(i);
		return retVal;
	}

	private void initMethods(ClassProxy c) {
		Vector vm = new Vector();
		MethodProxy[] m = c.getMethods();
		// System.out.println("get methods called for "+ c.getName() +
		// m.length);
		int i, modifiers;
		for (i = 0; i < m.length; i++) {
			System.out.println(m[i].getName());
			modifiers = m[i].getModifiers();
			if (Modifier.isPublic(modifiers))
				vm.addElement(m[i]);
		}
		methods = new MethodDescriptorProxy[vm.size()];
		for (i = 0; i < vm.size(); i++) {
			// Do default method categorization
			methods[i] = new AMethodDescriptorProxy(
					(MethodProxy) vm.elementAt(i));

			// System.out.println(methods[i].getName());
		}
		initMethodAttribs();
		// why was this call there?
		// getDynamicCommandsMethodDescriptor();
	}

//	static String[] excludeProperties = { "clientHost", "log", "ref",
//			"commonPort", "", "class", "virtualClass", "dynamicProperties" };
	static String[] excludeProperties = { "clientHost", "log", "ref",
		"commonPort", "", /*"class",*/ "virtualClass", "dynamicProperties" };
	static Vector excludedPropertiesVector = util.misc.Common
			.arrayToVector(excludeProperties);

	static String[] excludeProperties() {
		// String[] retVal = {};
		return excludeProperties;
	}

	public static boolean excluded(String property) {
		// Vector excludedPropertiesVector =
		// uiGenerator.arrayToVector(excludeProperties());
		return (excludedPropertiesVector.contains(property));
	}

	 boolean ignoredProperty(PropertyDescriptorProxy p, ClassProxy c,
			String name) {
		if (excluded(name))
			return true;
		MethodProxy method = p.getReadMethod();
		if (method != null) {
			util.annotations.Visible visible = (util.annotations.Visible) method
					.getAnnotation(util.annotations.Visible.class);
			if (visible != null)
				return (!visible.value());
			// this does not work as property is set after the class descriptor and properties are created
//			else {
//				Boolean visibleAttribute = (Boolean) p.getValue(AttributeNames.VISIBLE);
//				if (visibleAttribute != null) {
//					return visibleAttribute;
//				}
//			}
		}

		/*
		 * if (name.equals("class")) return true; if (name.equals("")) return
		 * true;
		 */
		/*
		 * Generic vector should remove this if (uiBean.isVector(c) &&
		 * name.equals("empty")) return true;
		 */
//		return false;
		return !componentsVisible;
	}
	
	static boolean displayToString(ClassProxy c) {
		util.annotations.DisplayToString displayToString = (util.annotations.DisplayToString) c
				.getAnnotation(util.annotations.DisplayToString.class);
		if (displayToString != null)
			return (displayToString.value());
		return false;
		
	}

	public PropertyDescriptorProxy[] getIndexOrKeyPropertyDescriptors() {
		PropertyDescriptorProxy[] retVal = new PropertyDescriptorProxy[namesToPropertyDescriptors
				.size()];
		int i = 0;
		Enumeration<PropertyDescriptorProxy> elements = namesToPropertyDescriptors
				.elements();
		while (elements.hasMoreElements()) {
			retVal[i] = elements.nextElement();
			i++;
		}
		return retVal;

	}

	Vector<PropertyDescriptorProxy> dynamicPropertyDescriptors;

	Vector<PropertyDescriptorProxy> dynamicPropertyDescriptors() {
		Vector<PropertyDescriptorProxy> v = new Vector();
		if (prototypeObject != null) {
			try {
				ClassProxy realClass = ACompositeLoggable
						.getTargetClass(prototypeObject);
				// String[] dynamicProperties =
				// DynamicMethods.getDynamicProperties(prototypeObject);
				String[] dynamicProperties = DynamicMethods
						.getDynamicProperties(prototypeObject, realClass);
				// String[] dynamicProperties =
				// DynamicMethods.getDynamicProperties(ACompositeLoggable.maybeExtractRealObject(prototypeObject));

				if (dynamicProperties != null) {
					for (int i = 0; i < dynamicProperties.length; i++) {
						// v.addElement(new PropertyDescriptor
						// (dynamicProperties[i].toString(), null, null));
						v.addElement(createPropertyDescriptor(dynamicProperties[i]
								.toString()));
					}
				}
			} catch (Exception e) {
				System.out.println("Dynamic Properties: " + e);
			}
		}
		dynamicPropertyDescriptors = v;
		return v;

	}

	Vector<VirtualMethodDescriptor> dynamicMethodDescriptors;

	public boolean isDynamic(MethodDescriptorProxy md) {
		return dynamicMethodDescriptors != null
				&& dynamicMethodDescriptors.contains(md);
	}

	public Vector<VirtualMethodDescriptor> getDynamicMethodDescriptors() {
		if (dynamicMethodsInitialized && dynamicMethodDescriptors != null)
			return dynamicMethodDescriptors;
		Vector<VirtualMethodDescriptor> v = new Vector();
		if (prototypeObject != null) {
			try {
				Vector<MethodProxy> virtualMethods = DynamicMethods
						.getVirtualMethods(prototypeObject);
				if (virtualMethods != null) {
					for (int i = 0; i < virtualMethods.size(); i++) {
						String name = virtualMethods.elementAt(i).getName();
						// v.addElement(new
						// VirtualMethodDescriptor(virtualMethods.elementAt(i)));
						VirtualMethodDescriptor vmd = createVirtualMethodDescriptor(name);
						vmd.setVirtualMethod(virtualMethods.elementAt(i));
						// vmd.setDynamiclClass(realClass);
						initMethodAttribs(vmd);
						v.addElement(vmd);
					}
				}
				String[] dynamicMethods = DynamicMethods
						.getDynamicCommands(prototypeObject);
				MethodProxy parameterTypesMethod = DynamicMethods
						.getGetDynamicCommandParameterTypes(ACompositeLoggable
								.getTargetClass(prototypeObject));
				MethodProxy returnTypeMethod = DynamicMethods
						.getGetDynamicCommandReturnType(ACompositeLoggable
								.getTargetClass(prototypeObject));

				if (dynamicMethods != null && dynamicMethods.length > 0) {
					// int j = 0;
					for (int i = 0; i < dynamicMethods.length; i++) {
						String command = dynamicMethods[i].toString();
						Object[] parameters = { command };
						ClassProxy[] parameterTypes = {};
						if (parameterTypesMethod != null) {
							parameterTypes = (ClassProxy[]) parameterTypesMethod
									.invoke(prototypeObject, parameters);
						}
						Class returnType = Void.TYPE;
						if (returnTypeMethod != null) {
							returnType = (Class) returnTypeMethod.invoke(
									prototypeObject, parameters);
						}
						MethodProxy method = new AVirtualMethod(command,
								parameterTypes,
								AClassProxy.classProxy(returnType),
								ACompositeLoggable
										.getTargetClass(prototypeObject));
						method.setSourceObject(prototypeObject);
						VirtualMethodDescriptor vmd = createVirtualMethodDescriptor(command);
						vmd.setVirtualMethod(method);
						// v.addElement(new VirtualMethodDescriptor (method));
						v.addElement(vmd);
					}
					MethodDescriptorProxy dmd = getDynamicCommandsMethodDescriptor();
					if (dmd != null) {
						String label = dynamicMethods[0].toString();
						if (dynamicMethods.length > 1)
							label += "..."
									+ dynamicMethods[dynamicMethods.length - 1]
											.toString();
						dmd.setValue(AttributeNames.LABEL, label);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Dynamic Properties: " + e);
			}
		}
		dynamicMethodDescriptors = v;
		return v;

	}

	MethodProxy[] virtualMethods;

	public MethodProxy[] getVirtualMethods() {
		if (virtualMethods != null)
			return virtualMethods;
		MethodDescriptorProxy[] mds = getMethodDescriptors();
		if (mds == null) return null;
		virtualMethods = new MethodProxy[mds.length];
		for (int i = 0; i < mds.length; i++) {
			if (mds[i] instanceof VirtualMethodDescriptor)
				virtualMethods[i] = ((VirtualMethodDescriptor) mds[i])
						.getVirtualMethod();
			else
				virtualMethods[i] = AVirtualMethod.virtualMethod(mds[i]
						.getMethod());

		}
		return virtualMethods;

	}

	MethodProxy[] virtualConstructors;

	public MethodProxy[] getVirtualConstructors() {
		if (virtualConstructors != null)
			return virtualConstructors;
		ConstructorDescriptorProxy[] mds = getConstructorDescriptors();
		virtualConstructors = new MethodProxy[mds.length];
		for (int i = 0; i < mds.length; i++) {
			/*
			 * if (mds[i] instanceof VirtualMethodDescriptor) virtualMethods[i]
			 * = ((VirtualMethodDescriptor) mds[i]).getVirtualMethod(); else
			 */
			virtualConstructors[i] = AVirtualMethod.virtualMethod(mds[i]
					.getConstructor());

		}
		return virtualConstructors;

	}

	public static MethodProxy[] getMethods(ClassProxy c) {
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(c);
		return cd.getVirtualMethods();

	}

	public static MethodProxy[] getConstructors(ClassProxy c) {
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(c);
		return cd.getVirtualConstructors();

	}

	public static MethodProxy parameterLessConstructor(ClassProxy theClass) {
		MethodProxy[] constructors = AClassDescriptor.getConstructors(theClass);
		for (int i = 0; i < constructors.length; i++) {
			if (constructors[i].getParameterTypes().length == 0)
				return constructors[i];
		}
		// return constructors[0];
		return null;
	}

	/*
	 * Vector dynamicPropertyDescriptors () { Vector v = new Vector(); if
	 * (prototypeObject != null) { try { Vector dynamicProperties =
	 * uiBean.getDynamicProperties(prototypeObject); if (dynamicProperties !=
	 * null) { for (int i = 0; i < dynamicProperties.size(); i++) {
	 * v.addElement(new PropertyDescriptor
	 * (dynamicProperties.elementAt(0).toString(), null, null)); } } } catch
	 * (Exception e) { System.out.println ("Dynamic Properties: " + e); } }
	 * return v;
	 * 
	 * }
	 */

	PropertyDescriptorProxy[] beanProperties;

	public static PropertyDescriptorProxy deepCopy(PropertyDescriptorProxy pd) {
		PropertyDescriptorProxy newVal = pd;

		try {
			newVal = new APropertyDescriptorProxy(pd.getName(),
					pd.getReadMethod(), pd.getWriteMethod());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newVal;
	}

	public static PropertyDescriptorProxy[] deepCopy(
			PropertyDescriptorProxy[] pds) {
		PropertyDescriptorProxy[] newVal = new PropertyDescriptorProxy[pds.length];
		for (int i = 0; i < pds.length; i++)
			newVal[i] = deepCopy(pds[i]);
		return newVal;
	}
	


	PropertyDescriptorProxy[] getPropertyDescriptors(BeanInfoProxy binfo) {
		
		if (displayToString(realClass))
			return new PropertyDescriptorProxy[0];
		

		Vector virtualAndNonIgnoredProperties = new Vector(); // this is place to put virtual properties
		// why put static properties in, they cause confusion
		PropertyNames propertyNames = realClass
				.getAnnotation(PropertyNames.class);
		List<String> propertyNamesList = null;
		List<String> editablePropertyNamesList = null;

		EditablePropertyNames editablePropertyNames = realClass
				.getAnnotation(EditablePropertyNames.class);
		if (propertyNames != null)
			propertyNamesList = Common.arrayToArrayList(propertyNames.value());
		if (editablePropertyNames != null)
			editablePropertyNamesList = Common
					.arrayToArrayList(editablePropertyNames.value());

		Hashtable staticGetters = new Hashtable();
		Hashtable staticSetters = new Hashtable();
		MethodProxy m[] = realClass.getMethods();
		for (int i = 0; i < m.length; i++) {
			if (IntrospectUtility.isStaticGetter(m[i]) && isVisible(m[i]))
				staticGetters
						.put(IntrospectUtility.getPropertyName(m[i]), m[i]);
			else if (IntrospectUtility.isStaticSetter(m[i]) && isVisible(m[i]))
				staticSetters
						.put(IntrospectUtility.getPropertyName(m[i]), m[i]);
		}
		beanProperties = new PropertyDescriptorProxy[staticGetters.size()];
		// this loop does not seem to be executed
		for (Enumeration keys = staticGetters.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			// int pos = -1;
			// int editPos = -1;
			if (propertyNamesList != null) {
				// pos = Common.indexOfIgnoreCase(propertyNamesList, key);
				if (!Common.containsIgnoreCaseAndRemove(propertyNamesList, key) && !key.equals("class")) {
					UndeclaredProperty.newCase(key, realClass, this);
					// Tracer.warning("Property: " + key + "of class " +
					// realClass +
					// " ignored as it is not in property names list. \n" +
					// " Associate annotation @Visible(false) with its getter.");
					continue;
				}
			}

			PropertyDescriptorProxy pd = null;
			try {
				if (staticSetters.containsKey(key)) {
					if (editablePropertyNamesList != null) {
						// editPos =
						// Common.indexOfIgnoreCase(editablePropertyNamesList,
						// key);
						if (!Common.containsIgnoreCaseAndRemove(
								editablePropertyNamesList, key)) {
							// Tracer.warning("Property: " + key + "of class " +
							// realClass +
							// " not considered editable as it is not in editable property names list. \n");
							UndeclaredEditableProperty.newCase(key, realClass,
									this);
							pd = new APropertyDescriptorProxy(key,
									(MethodProxy) staticGetters.get(key), null);
							continue;
						}
					}
					pd = new APropertyDescriptorProxy(key,
							(MethodProxy) staticGetters.get(key),
							(MethodProxy) staticSetters.get(key));

				} else
					pd = new APropertyDescriptorProxy(key,
							(MethodProxy) staticGetters.get(key), null);

				virtualAndNonIgnoredProperties.addElement(pd);
			} catch (Exception e) {
			}
			;
		}

		if (prototypeObject != null) {

			// prototypeInitialized = true;
			try {
				/*
				 * Vector dynamicProperties =
				 * uiBean.getDynamicProperties(prototypeObject); if
				 * (dynamicProperties != null) { for (int i = 0; i <
				 * dynamicProperties.size(); i++) { v.addElement(new
				 * PropertyDescriptor
				 * (dynamicProperties.elementAt(0).toString(), null, null)); } }
				 * } catch (Exception e) { System.out.println
				 * ("Dynamic Properties: " + e); }
				 */
				dynamicPropertyDescriptors();
				/*
				 * String[] dynamicProperties =
				 * DynamicMethods.getDynamicProperties(prototypeObject);
				 * 
				 * if (dynamicProperties != null) { for (int i = 0; i <
				 * dynamicProperties.length; i++) { //v.addElement(new
				 * PropertyDescriptor (dynamicProperties[i].toString(), null,
				 * null)); v.addElement(createPropertyDescriptor
				 * (dynamicProperties[i].toString())); } }
				 */
				// String[] dynamicProperties =
				// DynamicMethods.getDynamicProperties(prototypeObject);
				// if (dynamicProperties != null) {
				for (int i = 0; i < dynamicPropertyDescriptors.size(); i++) {
					// v.addElement(new PropertyDescriptor
					// (dynamicProperties[i].toString(), null, null));
					virtualAndNonIgnoredProperties.addElement(dynamicPropertyDescriptors.get(i));
				}
				// }
			} catch (Exception e) {
				System.out.println("Dynamic Properties: " + e);
			}
		}
		//
		// boolean foundElement = false;
		// boolean foundKey = false;
		// boolean foundValue = false;

		/*
		 * if (realClass.toString().indexOf("Number") != -1)
		 * System.out.println("NumberClass");
		 */

		PropertyDescriptorProxy[] pds;
		if (AClassDescriptor.withAttributeRegisterer())
			pds = binfo.getPropertyDescriptors();
		else
			pds = deepCopy(binfo.getPropertyDescriptors());
		MethodProxy explanationMethod = (MethodProxy) getAttribute(AttributeNames.EXPLANATION_METHOD);
		MethodProxy webDocumentationMethod = (MethodProxy) getAttribute(AttributeNames.WEB_DOCUMENTS_METHOD);

		for (int i = 0; i < pds.length; i++) {
			String name = null;
			try {
				// String name = pds[i].getName();
				name = pds[i].getName();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (name.equals("class"))
				continue;
			if (pds[i].getReadMethod() == null) {
				// String getterName = "get" +
				// Character.toUpperCase(name.charAt(0)) + name.substring(1);
				String getterName = IntrospectUtility.toGetterName(name);
				try {
					MethodProxy readMethod = realClass.getMethod(getterName,
							new ClassProxy[] {});
					pds[i].setReadMethod(readMethod);

				} catch (Exception e) {

				}

			}
			if (explanationMethod != null && pds[i].getReadMethod() == explanationMethod ||
					webDocumentationMethod != null && pds[i].getReadMethod() == webDocumentationMethod)
				continue;
			// boolean explicitProperty = false;
			// boolean explicitEditable = false;
			int propertyNameIndex = -1;
			int editablePropertyNameIndex = -1;
			if (propertyNamesList != null) {
				// explicitProperty =
				// Common.containsIgnoreCaseAndRemove(propertyNamesList, name);
				propertyNameIndex = Common.indexOfIgnoreCase(propertyNamesList,
						name);

			}

			if (editablePropertyNamesList != null) {
				editablePropertyNameIndex = Common.indexOfIgnoreCase(
						editablePropertyNamesList, name);
			}
			if (propertyNameIndex != -1 || editablePropertyNameIndex != -1) {
				virtualAndNonIgnoredProperties.addElement(pds[i]);
			} else if (ignoredProperty(pds[i], realClass, name)) {
				Tracer.info(AClassDescriptor.class, "Ignored  property " + name + " of class:"
						+ realClass);
			} else if (propertyNamesList == null) {
				virtualAndNonIgnoredProperties.addElement(pds[i]);
			} else if (!name.equals("class")) {
				// Tracer.warning("Implicit property: " + name +
				// " ignored as it is not in explicit property names list. \n" +
				// " Associate annotation @Visible(false) with its getter.");
				UndeclaredProperty.newCase(name, realClass, this);
			}
			if (editablePropertyNameIndex != -1
					&& pds[i].getWriteMethod() == null) {
				MissingSetterOfEditableProperty.newCase(name,
						pds[i].getReadMethod(), realClass, this);
				// Tracer.error("For property: " + name +
				// " in editable property names, please define a setter with the header:\n\t"
				// +
				// IntrospectUtility.toSetterSignature(editablePropertyNamesList.get(editablePropertyNameIndex),
				// pds[i].getReadMethod()));
				// Tracer.error("Setter not found for editable property:" + name
				// + " in editable property names list.");

			}

			if (editablePropertyNamesList != null) {
				if (editablePropertyNameIndex == -1
						&& pds[i].getWriteMethod() != null) {
					pds[i].setWriteMethod(null);
					UndeclaredEditableProperty.newCase(name, realClass, this);
					// Tracer.warning("Ignoring write method of property: " +
					// name + " as it is not in editable property names list.");
				}
			}

			if (propertyNameIndex != -1) {
				propertyNamesList.remove(propertyNameIndex);
			}

			if (editablePropertyNameIndex != -1) {
				editablePropertyNamesList.remove(editablePropertyNameIndex);
			}

			// // if (!name.equals("class") )
			// if (!ignoredProperty(pds[i], realClass, name))
			// v.addElement(pds[i]);
			// else
			// Tracer.info("Ignoring property " + name + " of class:"
			// + realClass);
			// if (name.equals("element"))
			// these are not really used now
			/*
			 * if (name.equals(AttributeNames.ELEMENT_NAME)) foundElement =
			 * true; else if (name.equals(AttributeNames.KEY_NAME)) foundKey =
			 * true; else if (name.equals(AttributeNames.VALUE_NAME)) foundValue
			 * = true;
			 */

		}
		if (propertyNamesList != null) {

			for (String propertyName : propertyNamesList) {
				MissingGetterOfProperty.newCase(propertyName, realClass, this);
				// Tracer.error("For property: "
				// + propertyName
				// +
				// " in  property names, please define a getter with the header:\n\t"
				// + IntrospectUtility.toGetterSignature(propertyName));
			}
			// recreate list to set positions
			propertyNamesList = Common.arrayToArrayList(propertyNames.value());

		}

		pds = new PropertyDescriptorProxy[virtualAndNonIgnoredProperties.size()];
		for (int i = 0; i < pds.length; i++) {
			pds[i] = (PropertyDescriptorProxy) virtualAndNonIgnoredProperties.elementAt(i);

			int index = Common.indexOfIgnoreCase(propertyNamesList,
					pds[i].getName());
			if (index != -1) {
				pds[i].setValue(AttributeNames.POSITION, index);
			}

		}
		return pds;
	}

	// Get information about properties
	private void initProperties(BeanInfoProxy binfo) {
		// properties = binfo.getPropertyDescriptors();
		properties = getPropertyDescriptors(binfo);
		initPropertyAttribs();
	}

	MethodDescriptor m;

	void includeOnlyDynamicMethods() {
		if (prototypeInitialized) {

			if (dynamicMethodsInitialized)
				return;
			dynamicMethodsInitialized = true;
			setVirtualMethodDescriptors(getDynamicMethodDescriptors());
		} else {
			MethodDescriptorProxy[] mds = {};
			methods = mds;
		}
	}

	void includeOnlyDynamicProperties() {

		if (prototypeInitialized) {
			if (dynamicPropertiesInitialized)
				return;
			dynamicPropertiesInitialized = true;
			setPropertyDescriptors(dynamicPropertyDescriptors());
		} else {
			PropertyDescriptorProxy[] pds = {};
			properties = pds;
		}
	}

	public static MethodDescriptorProxy deepCopy(MethodDescriptorProxy md) {
		MethodDescriptorProxy newVal = md;

		try {
			newVal = new AMethodDescriptorProxy(md.getMethod());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newVal;
	}

	public static MethodDescriptorProxy[] deepCopy(MethodDescriptorProxy[] mds) {
		MethodDescriptorProxy[] newVal = new MethodDescriptorProxy[mds.length];
		for (int i = 0; i < mds.length; i++)
			newVal[i] = deepCopy(mds[i]);
		return newVal;
	}

	static ClassProxy propertyListenerRegistererClass = RemoteSelector
			.classProxy(PropertyListenerRegistrar.class);
	static ClassProxy propertyChangeListenerClass = RemoteSelector
			.classProxy(PropertyChangeListener.class);

	static ClassProxy[] propertyListenerArgType = { propertyChangeListenerClass };
	static ClassProxy vectorListenerClass = RemoteSelector
			.classProxy(VectorListener.class);

	static ClassProxy vectorListenerRegistererClass = RemoteSelector
			.classProxy(VectorListenerRegisterer.class);
	static ClassProxy[] vectorListenerArgType = { vectorListenerClass };

	MethodDescriptorProxy[] getMethodDescriptors(BeanInfoProxy binfo) {
		/*
		 * if (onlyDynamicMethods()) { MethodDescriptor[] mds = {}; return mds;
		 * }
		 */

		if (propertyListenerRegistererClass.isAssignableFrom(realClass)) {
			try {
				addPropertyListener = realClass.getMethod(
						"addPropertyChangeListener", propertyListenerArgType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (vectorListenerRegistererClass.isAssignableFrom(realClass)) {
			try {
				addVectorListener = realClass.getMethod("addVectorListener",
						vectorListenerArgType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Vector v = new Vector();

		MethodDescriptorProxy[] mds;
		if (binfo == null) return null;
		if (AClassDescriptor.withAttributeRegisterer())
			mds = binfo.getMethodDescriptors();
		else
			mds = deepCopy(binfo.getMethodDescriptors());

		for (int i = 0; i < mds.length; i++) {
			String name = mds[i].getName();
			
			if (!AClassDescriptor.excludeMethods.contains(name)
					&& !IntrospectUtility.dynamicMethodsVector.contains(name))
				v.addElement(mds[i]);

			// else if (realClass.isPrimitive())
			else if (realClass.isImmutable())
				break;
			else if (addPropertyListener == null
					&& IntrospectUtility
							.isAddPropertyChangeListenerMethod(mds[i]
									.getMethod()))
				addPropertyListener = mds[i].getMethod();
			else if (IntrospectUtility
					.isAddRemotePropertyChangeListenerMethod(mds[i].getMethod()))
				addRemotePropertyListener = mds[i].getMethod();
			else if (addVectorListener == null
					&& IntrospectUtility.isAddVectorListenerMethod(mds[i]
							.getMethod()))
				addVectorListener = mds[i].getMethod();
			else if (IntrospectUtility.isAddHashtableListenerMethod(mds[i]
					.getMethod()))
				addHashtableListener = mds[i].getMethod();
			else if (IntrospectUtility.isAddObserverMethod(mds[i].getMethod()))
				addObserver = mds[i].getMethod();
			else if (IntrospectUtility.isAddRefresherMethod(mds[i].getMethod()))
				addRefresher = mds[i].getMethod();
			else if (IntrospectUtility.isAddTableModelListenerMethod(mds[i]
					.getMethod()))
				addTableListener = mds[i].getMethod();
			else if (IntrospectUtility.isAddTreeModelListenerMethod(mds[i]
					.getMethod()))
				addTreeListener = mds[i].getMethod();

		}

		mds = new MethodDescriptorProxy[v.size()];

		for (int i = 0; i < mds.length; i++)
			mds[i] = (MethodDescriptorProxy) v.elementAt(i);
		return mds;
	}

	void makeDynamicMethodsInvisible() {
		makeMethodInvisible(getDynamicCommandsMethodDescriptor());
		makeMethodInvisible(getDynamicPropertiesGetter());
		makeMethodInvisible(getDynamicPropertiesSetter());
	}

	void makeMethodInvisible(MethodDescriptorProxy md) {
		if (md == null)
			return;
		md.setValue(AttributeNames.VISIBLE, false);
	}

	// Get information about the methods
	private void initMethods(BeanInfoProxy binfo) {
		// methods = binfo.getMethodDescriptors();
		methods = getMethodDescriptors(binfo);
		if (prototypeInitialized)
			// addMethods(dynamicMethodDescriptors());
			addDynamicMethods(prototypeObject);
		initMethodAttribs();
		makeDynamicMethodsInvisible();
		/*
		 * MethodDescriptorProxy md = getDynamicCommandsMethodDescriptor(); if
		 * (md != null) md.setValue(AttributeNames.VISIBLE, false);
		 */
	}

	// ViewInfo method implementations
	public MethodDescriptorProxy[] getMethodDescriptors() {
		return methods;
	}

	// public PropertyDescriptorProxy[] getPropertyDesciptors() {
	// return properties;
	// }
	public void setMethodDescriptors(MethodDescriptorProxy[] newVal) {
		methods = newVal;
	}

	public PropertyDescriptorProxy[] getPropertyDescriptors() {
		return properties;
	}

	public void setPropertyDescriptors(PropertyDescriptorProxy[] newVal) {
		properties = newVal;

	}

	public FieldDescriptorProxy[] getFieldDescriptors() {
		return fields;
	}

	public FieldDescriptorProxy[] getConstantDescriptors() {
		return constants;
	}

	public ConstructorDescriptorProxy[] getConstructorDescriptors() {
		return constructors;
	}

	public static ADynamicSparseList sortMethodDescriptors(
			MethodDescriptorProxy[] mds) {
		ADynamicSparseList<MethodDescriptorProxy> retVal = new ADynamicSparseList();
		for (int i = 0; i < mds.length; i++) {
			Integer pos = (Integer) mds[i].getValue(AttributeNames.POSITION);
			if (pos != null && pos != -1)
				retVal.setOrInsertNewElementAbove(pos, mds[i]);
			else
				retVal.add(mds[i]);
		}
		for (int i = retVal.size() - 1; i >= 0; i--) {
			if (retVal.get(i) == null)
				retVal.remove(i);

		}
		return retVal;
	}

	// Impose an ordering on features. Currently only
	// fields and properties are considered here. A
	// future extension could add the rest.
	public FeatureDescriptorProxy[] getFeatureDescriptors() {
		if (features == null) {
			features = new FeatureDescriptorProxy[properties.length
					+ fields.length];
			for (int i = 0; i < features.length; i++)
				features[i] = null;
			Vector misses = new Vector();
			Vector dynamic = new Vector();
			Vector readOnlyProperties = new Vector();
			Integer pos;
			int p;
			// First put in all properties and fields in their place
			// Conflicts are stored in the miss Vector and put in
			// free locations later
			for (int i = 0; i < properties.length; i++) {
				pos = (Integer) properties[i].getValue(AttributeNames.POSITION);
				if (pos != null)
					p = pos.intValue();
				else
					p = -1;
				if (p >= 0 && p < features.length && features[p] == null) {
					features[p] = properties[i];
				} else if (dynamicPropertyDescriptors != null
						&& dynamicPropertyDescriptors.contains(properties[i]))
					dynamic.add(properties[i]);
				else if (properties[i].getWriteMethod() == null)
					add(readOnlyProperties, properties[i]);
				else
					// misses.addElement(properties[i]);
					add(misses, properties[i]);
			}
			// Same thing with fields
			for (int i = 0; i < fields.length; i++) {
				pos = (Integer) fields[i].getValue(AttributeNames.POSITION);
				if (pos != null)
					p = pos.intValue();
				else
					p = -1;
				if (p >= 0 && p < features.length && features[p] == null) {
					features[p] = fields[i];
				} else
					add(misses, fields[i]);
				// misses.addElement(fields[i]);
			}
			// Fill in the misses
			int j = 0;
			int k = 0;
			int l = 0;
			for (int i = 0; i < features.length; i++) {
				if (features[i] == null) {
					if (j < misses.size())
						features[i] = (FeatureDescriptorProxy) misses
								.elementAt(j++);
					else if (k < readOnlyProperties.size())
						features[i] = (FeatureDescriptorProxy) readOnlyProperties
								.elementAt(k++);
					else if (l < dynamic.size())
						features[i] = (FeatureDescriptorProxy) dynamic
								.elementAt(l++);
				}
			}
		}
		return features;
	}

	// Since we want to reuse the existing attribute
	// mechanism, we convert this form of atts to the
	// previous version (till were able to write a
	// new attribute mechanism
	// public uiClassAttributeManager toUiClassAttributeManager() {
	// uiClassAttributeManager m = new
	// uiClassAttributeManager(realClass.getName());
	// String name, an;
	// for (int i=0; i<properties.length; i++) {
	// name = properties[i].getName()+".";
	// Enumeration anames = properties[i].attributeNames();
	// while (anames.hasMoreElements()) {
	// an = (String) anames.nextElement();
	// m.addAttribute(new Attribute(name+an,
	// properties[i].getValue(an)));
	// }
	// // Add default attributes for elideString and label
	// if (m.getAttribute(name+AttributeNames.LABEL) == null)
	// m.addAttribute(new Attribute(name+AttributeNames.LABEL,
	// properties[i].getDisplayName()));
	// }
	//
	// for (int i=0; i<fields.length; i++) {
	// name = fields[i].getName()+".";
	// Enumeration anames = fields[i].attributeNames();
	// while (anames.hasMoreElements()) {
	// an = (String) anames.nextElement();
	// //will return correct value in getLabel()
	// m.addAttribute(new Attribute(name+an,
	// fields[i].getValue(an)));
	//
	// }
	// // Add default attributes for elideString and label
	// // will return correct value in getLabel()
	// /*
	// if (m.getAttribute(name+AttributeNames.LABEL) == null) {
	// m.addAttribute(new Attribute(name+AttributeNames.LABEL,
	// fields[i].getDisplayName()));
	// }
	// */
	// }
	//
	// // Add default ELIDESTR attribute (set to classname)
	// if (m.getAttribute(AttributeNames.ELIDE_STRING) == null)
	// m.addAttribute(new Attribute(AttributeNames.ELIDE_STRING,
	// Misc.beautify(" "+realClass.getName()+"...")));
	//
	// // Add the class attributes
	// Enumeration keys = attributes.keys();
	// while (keys.hasMoreElements()) {
	// String att = (String) keys.nextElement();
	// Object value = attributes.get(att);
	// m.addAttribute(new Attribute(att, value));
	// }
	//
	//
	// return m;
	// }

	public BeanDescriptorProxy getBeanDescriptor() {
		return beanDescriptor;
	}

	public String toString() {
		String vclass = getVirtualClass();
		if (vclass != null)
			return vclass;
		ClassProxy cls = getRealClass();
		if (cls != null)
			return cls.toString();
		return super.toString();
	}

	public static ClassProxy getTargetClass(Object maybeFakeModel) {
		return ACompositeLoggable.getTargetClass(maybeFakeModel);
	}

	// Customizer methods
	// used to customize BeanInfo classes

	/*
	 * private String propertyName = AttributeNames.LABEL; public void
	 * setPropertyName(String propertyName) { // Go through the Vector and set
	 * each element's // property name to propertyName Vector vector =
	 * getHtVector(); for (int i=0; i<vector.size(); i++) ((htElement)
	 * vector.elementAt(i)).setPropertyName(propertyName); this.propertyName =
	 * propertyName; VectorChanged(); } public String getPropertyName() { return
	 * propertyName; }
	 * 
	 * 
	 * public void setValue(Object obj) { attributes.put(getPropertyName(),
	 * obj); }
	 */
	// set up boolean controls to
	// switch between fields/properties
	// and methods
	/*
	 * private boolean Fields = true, Methods = false, Bean=false;
	 * 
	 * 
	 * // Expose these booleans as controls public boolean getFields() { return
	 * Fields; }
	 * 
	 * public void setFields(boolean b) { if (b) { Fields = b; Methods = Bean =
	 * false; methodsChanged(); beanChanged(); VectorChanged(); } }
	 * 
	 * public boolean getMethods() { return Methods; }
	 * 
	 * public void setMethods(boolean b) { if (b) { Methods = b; Fields = Bean =
	 * false; fieldsChanged(); beanChanged(); VectorChanged(); } }
	 * 
	 * public boolean getBean() { return Bean; }
	 * 
	 * public void setBean(boolean b) { if (b) { Bean = b; Fields = Methods =
	 * false; fieldsChanged(); methodsChanged(); VectorChanged(); } }
	 * 
	 * // Expose this vector // as an editable property public Vector
	 * getHtVector() { if (Fields) return FieldVector(); //return (Vector)
	 * FieldVector().clone(); else if (Methods) return MethodVector(); //return
	 * (Vector) MethodVector().clone(); else return BeanVector(); //return
	 * (Vector) BeanVector().clone(); }
	 * 
	 * 
	 * private void fieldsChanged() {
	 * propertyChange.firePropertyChange("fields", null, new
	 * Boolean(getFields())); }
	 * 
	 * private void methodsChanged() {
	 * propertyChange.firePropertyChange("methods", null, new
	 * Boolean(getMethods())); }
	 * 
	 * private void beanChanged() { propertyChange.firePropertyChange("bean",
	 * null, new Boolean(getBean())); }
	 * 
	 * private void VectorChanged() {
	 * propertyChange.firePropertyChange("htVector", null, getHtVector()); }
	 * 
	 * private void PropertyChanged() {
	 * propertyChange.firePropertyChange("propertyName", null,
	 * getPropertyName()); }
	 * 
	 * private Vector fieldVector = null, methodVector = null, beanVector =
	 * null;; private Vector FieldVector() { if (fieldVector == null) {
	 * fieldVector = new Vector(); int i; for (i=0; i<fields.length; i++)
	 * fieldVector.addElement(new htElement(fields[i], getPropertyName(),
	 * this)); for (i=0; i<properties.length; i++) if
	 * (!properties[i].getName().equals("")) fieldVector.addElement(new
	 * htElement(properties[i], getPropertyName(), this)); } return fieldVector;
	 * }
	 * 
	 * private Vector MethodVector() { if (methodVector == null) { methodVector
	 * = new Vector(); for (int i=0; i<methods.length; i++)
	 * methodVector.addElement(new htElement(methods[i],
	 * getPropertyName(),this)); } return methodVector; }
	 * 
	 * private Vector BeanVector() { if (beanVector == null) { beanVector = new
	 * Vector(); beanVector.addElement(new htElement(getBeanDescriptor(),
	 * realClass.getName(), getPropertyName(), this));
	 * 
	 * } return beanVector; }
	 * 
	 * // Expose this method as Save in the File menu public boolean
	 * writeBeanInfo(String filename) { //return
	 * BeanInfoWriter.writeBeanInfo(this, filename) && compile (fileName);
	 * return BeanInfoWriter.writeBeanInfo(this, filename); } // Expose this
	 * method as Save in the File menu public boolean writeBeanInfo() { //return
	 * BeanInfoWriter.writeBeanInfo(this, filename) && compile (fileName);
	 * System.out.println("writing to file:" + beanFile); return
	 * BeanInfoWriter.writeBeanInfo(this, beanFile); }
	 */
	/*
	 * public boolean compile (String fileName) { String arguments[] = new
	 * String[2]; //arguments[0] = "-classpath"; //arguments[1] = classpath;
	 * arguments[0] = "-nowarn"; arguments[1] = fileName;
	 * 
	 * sun.tools.javac.Main compiler; try { compiler = new
	 * sun.tools.javac.Main(System.err, "javac"); compiler.compile(arguments);
	 * return true; } catch (Exception e) {
	 * System.out.println("Could not load the JDK compiler"); //System.exit(1);
	 * return false; } }
	 */
	private Object[] helpers = null;
	public static boolean withAttributeRegisterer = true;
	public static String[] predefinedPackageSuffixes = { "java.", "slc.",
			"slm.", "bus." };
	public static Object[] excludeMethodsArray = { "toString", "notify",
			"notifyAll", "wait", "equals", "hashCode", "getClass",
			"getClientHost", "exportObject", "unexportObject", "toStub",
			"notifyAllListeners", "addPropertyChangeListener",
			"removePropertyChangeListener", "addPropertyChangeVetoer",
			"removePropertyChangeVetoer", "mouseClicked", "addRefresher",
			"addObserver", "notifyObservers", "deleteObserver",
			"deleteObservers", "countObservers", "main", "hasChanged",
			"initSerializedObject", "updateVector", "propertyChange",
			DynamicMethods.GET_DYNAMIC_COMMANDS,
			// "getDynamicProperty",
			// "setDynamicProperty",
			DynamicMethods.GET_DYNAMIC_PROPERTY_TYPE };
	// public static java.util.Vector excludeMethods = uiGenerator
	// .arrayToVector(excludeMethodsArray);
	public static java.util.Vector excludeMethods = util.misc.Common
			.arrayToVector(excludeMethodsArray);
	public Object[] getHelpers() {
		if (helpers == null) {
			helpers = new Object[1];
			helpers[0] = new AttributeNames();
		}
		return helpers;
	}

	public static boolean withAttributeRegisterer() {
		return withAttributeRegisterer;
	}

	public static void writeWithAttributeRegister(boolean newVal) {
		withAttributeRegisterer = newVal;
	}

	public static String getAnnotationString(ClassProxy c) {
		if (c == null)
			return "";
		try {

			String label = getLabel(c);
			String[] urlStrings = getHTMLDocumentation(c);
			String explanation = getExplanationAnnotation(c);
			String[] keywords = getKeywordsAnnotation(c);
			String helpString = nullString;
			if (explanation != null) {
				helpString += explanation;
			}
			if (keywords != null) {
				helpString += "\n\n" + toString(keywords);

			}
			if (urlStrings != null) {
				helpString += "\n\nMore info available at:"
						+ Common.toString(urlStrings);
			}
			return helpString;

		} catch (Exception e) {
			return nullString;
		}
	}

	public static String getAnnotationString(String className) {
		try {
			// Class c = Class.forName(className);
			ClassProxy c = AClassProxy.classProxy(util.misc.Common
					.asynchronousClassForName(className));
			if (c != null)
				return AClassDescriptor.getAnnotationString(c);
			else
				return "";

		} catch (Exception e) {
			return "";
		}
	}

	public static String[] getKeywordsAnnotation(String className) {
		try {
			// Class c = Class.forName(className);
			ClassProxy c = AClassProxy.classProxy(util.misc.Common
					.asynchronousClassForName(className));
			if (c != null)
				return getKeywordsAnnotation(c);
			else
				return null;

		} catch (Exception e) {
			return null;
		}
	}

	public static String[] getKeywordsAnnotation(ClassProxy c) {
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(c);
		String[] keywords = (String[]) cd
				.getAttribute(AttributeNames.KEYWORDS_ANNOTATION);

		return keywords;

	}

	public static String getExplanationAnnotation(ClassProxy c) {
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(c);
		String explanation = (String) cd
				.getAttribute(AttributeNames.EXPLANATION);

		return explanation;
	}

	public static String[] getHTMLDocumentation(ClassProxy c) {
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(c);
		String[] doc = (String[]) cd
				.getAttribute(AttributeNames.HTML_DOCUMENTATION);

		return doc;

	}

	public static String getLabel(ClassProxy c) {
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(c);
		String name = (String) cd.getAttribute(AttributeNames.LABEL);	
		if (name == null)
			// hope this does not cause issues
			
				name = (String) getAnnotationAttribute(cd.getBeanDescriptor(), AttributeNames.LABEL);
		if (name == null)
			name = toShortName(c.getName());
		return name;

	}

	public static boolean isPredefinedClass(ClassProxy c) {
		String className = c.getName();
		for (int i = 0; i < predefinedPackageSuffixes.length; i++) {
			if (className.startsWith(predefinedPackageSuffixes[i]))
				return true;
		}
		return false;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.MinValue minValue) {
		if (minValue == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.MIN_VALUE,
				minValue.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.ComponentWidth componentWidth) {
		if (componentWidth == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.COMPONENT_WIDTH,
				componentWidth.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.ComponentHeight componentHeight) {
		if (componentHeight == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.COMPONENT_HEIGHT,
				componentHeight.value());
		retVal.add(attr);
		return retVal;
	}
	public static Vector<Attribute> toAttributeVector(
			util.annotations.PreferredWidgetClass widgetClass) {
		if (widgetClass == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.PREFERRED_WIDGET,
				widgetClass.value().getName());
		retVal.add(attr);
		return retVal;
	}
	public static Vector<Attribute> toAttributeVector(
			util.annotations.LayoutName layoutClass) {
		if (layoutClass == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.LAYOUT,
				layoutClass.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			IsAtomicShape isAtomicShape) {
		if (isAtomicShape == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Boolean value = isAtomicShape.value();
		Attribute attr = new Attribute(AttributeNames.IS_ATOMIC_SHAPE,
				isAtomicShape.value());
		retVal.add(attr);
		if (value) {
			Attribute attr2 = new Attribute(AttributeNames.IS_COMPOSITE_SHAPE,
					false);
			retVal.add(attr2);
		}
		return retVal;
	}
	
	public static Vector<Attribute> toAttributeVector(
			IsCompositeShape isCompositeShape) {
		if (isCompositeShape == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Boolean value = isCompositeShape.value();
		Attribute attr = new Attribute(AttributeNames.IS_COMPOSITE_SHAPE,
				isCompositeShape.value());
		retVal.add(attr);
		if (value) {
			Attribute attr2 = new Attribute(AttributeNames.IS_ATOMIC_SHAPE,
					false);
			retVal.add(attr2);
		}
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			ReturnsClassExplanation returnsClassExplanation) {
		if (returnsClassExplanation == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(
				AttributeNames.RETURNS_CLASS_EXPLANATION,
				returnsClassExplanation.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			ReturnsClassWebDocuments returnsClassWebDocuments) {
		if (returnsClassWebDocuments == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(
				AttributeNames.RETURNS_CLASS_WEB_DOCUMENTS,
				returnsClassWebDocuments.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			ShowDebugInfoWithToolTip showDebugInfoWithToolTip) {
		if (showDebugInfoWithToolTip == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(
				AttributeNames.SHOW_DEBUG_INFO_WITH_TOOL_TIP,
				showDebugInfoWithToolTip.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.IsNestedShapesContainer isNestedShapesContainer) {
		if (isNestedShapesContainer == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(
				AttributeNames.IS_NESTED_SHAPES_CONTAINER,
				isNestedShapesContainer.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.MaxValue maxValue) {
		if (maxValue == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.MAX_VALUE,
				maxValue.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.StepValue stepValue) {
		if (stepValue == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.STEP_VALUE,
				stepValue.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.Position position) {
		if (position == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.POSITION,
				position.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(Row row) {
		if (row == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.ROW, row.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(Label label) {
		if (label == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.LABEL, label.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.Column column) {
		if (column == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.COLUMN, column.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.Direction alignment) {
		if (alignment == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.DIRECTION,
				alignment.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.SeparateThread separateThread) {
		if (separateThread == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.SEPARATE_THREAD,
				separateThread.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.ShowButton showButton) {
		if (showButton == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.SHOW_BUTTON,
				showButton.value());
		retVal.add(attr);
		return retVal;
	}

	public static Vector<Attribute> toAttributeVector(
			util.annotations.Visible visible) {
		if (visible == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.VISIBLE, visible.value());
		retVal.add(attr);
		return retVal;
	}
	
	public static Vector<Attribute> toAttributeVector(
			util.annotations.IndirectlyVisible indirectVisible) {
		if (indirectVisible == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.INDIRECTLY_VISIBLE, !(Boolean) indirectVisible.value());
		retVal.add(attr);
		return retVal;
	}
	
	public static Vector<Attribute> toAttributeVector(
			util.annotations.DisplayToString displayToString) {
		if (displayToString == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.DISPLAY_TO_STRING, displayToString.value());
		retVal.add(attr);
		return retVal;
	}
	
	public static Vector<Attribute> toAttributeVector(
			util.annotations.OnlyGraphicalDescendents val) {
		if (val == null)
			return null;
		Vector<Attribute> retVal = new Vector();
		Attribute attr = new Attribute(AttributeNames.ONLY_GRAPHIICAL_DESCENDENTS, val.value());
		retVal.add(attr);
		return retVal;
	}
	

	public static String getMenuName(ClassProxy classProxy) {
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(classProxy);
		String name = (String) cd.getAttribute(AttributeNames.LABEL);
		if (name == null)
			name = AClassDescriptor.toShortName(classProxy.getName());
		return name;

	}

	public static boolean excludeMethod(MethodProxy method) {
		return excludeMethods.contains(method.getName());
	}
	@Override
	public boolean isComponentsVisible() {
		return componentsVisible;
	}
	@Override
	public void setComponentsVisible(boolean componentsVisible) {
		this.componentsVisible = componentsVisible;
	}

	/*
	 * // public void setLABEL() { setPropertyName(AttributeNames.LABEL);
	 * PropertyChanged(); } public void setPOSITION() {
	 * setPropertyName(AttributeNames.POSITION); VectorChanged();
	 * PropertyChanged(); }
	 * 
	 * public void setMENU() { setPropertyName(AttributeNames.MENU_NAME);
	 * PropertyChanged(); }
	 * 
	 * public void setPLACE_TOOLBAR() {
	 * setPropertyName(AttributeNames.PLACE_TOOLBAR); PropertyChanged(); }
	 * 
	 * public void setRIGHTMENU() { setPropertyName(AttributeNames.RIGHTMENU);
	 * PropertyChanged(); } public void setTOOLBAR() {
	 * setPropertyName(AttributeNames.TOOLBAR); PropertyChanged(); } public void
	 * setICON() { setPropertyName(AttributeNames.ICON); PropertyChanged(); }
	 * 
	 * public void setVISIBLE() { setPropertyName(AttributeNames.VISIBLE);
	 * PropertyChanged(); }
	 * 
	 * 
	 * 
	 * public void setELIDE_IMAGE() {
	 * setPropertyName(AttributeNames.ELIDE_IMAGE); PropertyChanged(); } public
	 * void setLABELLED() { setPropertyName(AttributeNames.LABELLED);
	 * PropertyChanged(); } public void setHELPER_LABEL() {
	 * setPropertyName(AttributeNames.HELPER_LABEL); PropertyChanged(); } public
	 * void setHELPER_ICON() { setPropertyName(AttributeNames.HELPER_ICON);
	 * PropertyChanged(); } public void setHELPER_LOCN() {
	 * setPropertyName(AttributeNames.HELPER_LOCN); PropertyChanged(); } public
	 * void setTITLE() { setPropertyName(AttributeNames.TITLE);
	 * PropertyChanged(); }
	 * 
	 * 
	 * public void setPREFERRED_WIDGET() {
	 * setPropertyName(AttributeNames.PREFERRED_WIDGET); PropertyChanged(); }
	 * public void setDIRECTION() { setPropertyName(AttributeNames.DIRECTION);
	 * PropertyChanged(); } public void setNUM_COLUMNS() {
	 * setPropertyName(AttributeNames.TEXT_FIELD_LENGTH); PropertyChanged(); }
	 * 
	 * public void setDECINCUNIT() { setPropertyName(AttributeNames.DECINCUNIT);
	 * PropertyChanged(); }
	 * 
	 * 
	 * public void setINCREMENTAL() {
	 * setPropertyName(AttributeNames.INCREMENTAL); PropertyChanged(); } public
	 * void setDOUBLE_CLICK_METHOD() {
	 * setPropertyName(AttributeNames.DOUBLE_CLICK_METHOD); PropertyChanged(); }
	 */
	static {
		util.misc.Common.arrayToSet(excludeInterfacesArray,
				excludeInterfacesSet);
	}
}
