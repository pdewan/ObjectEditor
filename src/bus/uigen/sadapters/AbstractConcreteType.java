package bus.uigen.sadapters;
import java.beans.MethodDescriptor;
import java.util.Vector;

import shapes.ShapeModel;
import util.trace.Tracer;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.VirtualMethodDescriptor;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.misc.OEMisc;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.ReflectUtil;
public abstract class AbstractConcreteType implements ConcreteType  {
	ClassProxy targetClass;
	Object targetObject;	uiFrame frame;	
	//VirtualMethod viewRefresher;
	public void init(Object theTargetObject, uiFrame theFrame) {
		//setTarget(theTargetObject);
		if (theTargetObject == null) return;
		if (theTargetObject instanceof ShapeModel)
			Tracer.error("Shape Model target");
		//init (RemoteSelector.getClass(theTargetObject), theTargetObject, theFrame);
//		init (ACompositeLoggable.getTargetClass(theTargetObject), theTargetObject, theFrame);
		init (ReflectUtil.toMaybeProxyTargetClass(theTargetObject), theTargetObject, theFrame);

		//viewRefresher = uiBean.getV
		/*		frame = theFrame;
		setMethods(targetClass);		*/
	}
	public void setFrame (uiFrame theFrame) {
		frame = theFrame;
	}
	public  void askViewObjectToRefresh(Object viewObject) {
		OEMisc.askViewObjectToRefresh(viewObject);
	}
	public void init(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {		
		//targetObject = theTargetObject;		//targetClass = theTargetClass;
		if (theTargetObject instanceof ShapeModel)
			Tracer.error("Shape Model target");		frame = theFrame;
		//setMethods(targetClass);
		setTarget(theTargetClass, theTargetObject);
		//filterMethodDescriptors(theTargetObject);
	}
	uiFrame getFrame() {
		return frame;
	}
	public  void filterMethodDescriptors (Object obj) {
    	if (obj == null) return;
    	ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(RemoteSelector.getClass(obj), obj);
    	MethodDescriptorProxy[] md = cd.getMethodDescriptors();
    	Vector v = new Vector();
    	for (int i = 0; i < md.length; i++) {
    		MethodProxy m = VirtualMethodDescriptor.getVirtualMethod(md[i]);
    		//if (uiBean.isGetter(m) || uiBean.isSetter(m) || uiBean.isPre(m)) continue;
    		if (excludeMethod(m)) continue;
    		v.add(md[i]);
    	}
    	cd.setMethodDescriptors(v);
    	
    	
		
	}
	boolean excludeMethod (MethodProxy m) {
    	return false;
    }
    	public void setTarget(Object theTargetObject) {
		if (targetObject instanceof ShapeModel)
			Tracer.error("SHape Model target");		if (targetObject == theTargetObject) return;
		ClassProxy newTargetClass;
		if (theTargetObject != null)
		//Class newTargetClass = theTargetObject.getClass();
			//newTargetClass = RemoteSelector.getClass(theTargetObject);
			newTargetClass = ACompositeLoggable.getTargetClass(theTargetObject);
		else
			newTargetClass = this.targetClass;
		setTarget (newTargetClass, theTargetObject);
		/*		//System.out.println("setTarget: changing target Object from " + targetObject + " to " + theTargetObject);		targetObject = theTargetObject;
		if (targetClass == newTargetClass) return;		//System.out.println("setTarget: changing target class from " + targetClass + " to " + newTargetClass);		targetClass = newTargetClass;
		setMethods(targetClass);
		*/	}	
	void targetObjectChanged() {
		
	}
	public void setTarget(ClassProxy newTargetClass, Object theTargetObject) {
		//		System.out.println("setTarget: changing target Object from " + targetObject + " to " + theTargetObject);
		if (targetObject instanceof ShapeModel)
			Tracer.error("Shape Model target");
		targetObject = theTargetObject;
		if (targetClass == newTargetClass) {
			targetObjectChanged();
			return;
		}
		//System.out.println("setTarget: changing target class from " + targetClass + " to " + newTargetClass);
		targetClass = newTargetClass;
		setMethods(targetClass);
		
	}
	public abstract void setMethods(ClassProxy objectClass);
	public Object getTargetObject() {
		return targetObject;
	}
	public ClassProxy getTargetClass() {
		return targetClass;
	}
	public boolean isEditingMethod (MethodProxy method) {
		return false;
	}
	public boolean isPatternMethod (MethodProxy method) {
		return isEditingMethod(method);
	}
	
	public String toString() {
		if (targetObject != null)
			return targetObject.toString();
		else
			return "";
	}
	public static final String PROGRAMMING_PATTERN = "Programming Pattern";
	
	public String typeKeyword() {
		return ObjectEditor.TYPE_KEYWORD;
	}
	public String programmingPatternKeyword() {
		return PROGRAMMING_PATTERN;
	}
	public boolean hasPreconditions() {
		return false;
	}
	public boolean hasValidation() {
		return false;
	}
	
	public String applicationKeyword() {
		return ObjectEditor.TEXT_KEYWORD ;
	}
	
	public Vector<Attribute> getAttributes() {
		// TODO Auto-generated method stub
		if (targetClass == null)
			return null;
		ClassDescriptorInterface cdesc;
		if (targetObject == null)
			 cdesc = ClassDescriptorCache.getClassDescriptor(targetClass);
		else
			cdesc = ClassDescriptorCache.getClassDescriptor(targetClass, targetObject);
		return (Vector) cdesc.getAttribute(AttributeNames.MERGED_ATTRIBUTES_ANNOTATIONS);
	}
	public Object clone () {
		try {
		return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	ConcreteType prototype;
	public void setPrototype (ConcreteType thePrototype) {
		prototype = thePrototype;
	}
	public ConcreteType getPrototype() {
		return prototype;
	}
	String patternName;
	public String getPatternName() {
		return patternName;
		
	}
	public void setPatternName(String newVal) {
		patternName = newVal;
	}
	String patternPath;
	public String getPatternPath() {
		return patternPath;
		
	}
	public void setPatternPath(String newVal) {
		patternPath = newVal;
	}
	
}
