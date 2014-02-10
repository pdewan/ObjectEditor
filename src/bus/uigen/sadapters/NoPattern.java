package bus.uigen.sadapters;

import java.util.Set;
import java.util.Vector;

import util.annotations.StructurePatternNames;

import bus.uigen.uiFrame;
import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.CommandListener;

public class NoPattern extends AbstractConcreteType implements RecordStructure{

	public NoPattern (ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame) {
		init(theTargetClass, theTargetObject, theFrame );
		//filterMethodDescriptors(theTargetObject);
	}
	public NoPattern() {
		
	}
	public void setMethods(ClassProxy objectClass) {
		// TODO Auto-generated method stub
		
	}
	static Vector nullVector = new Vector();
	@Override
	public Vector componentNames() {
		// TODO Auto-generated method stub
		return nullVector;
	}
	@Override
	public ClassProxy componentType(String componentName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object get(String componentName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector<Attribute> getComponentAttributes(String componentName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getExpansionObject() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasEditableUserObject() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasUserObject() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEditable() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isReadOnly(String componentName) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean preRead(String componentName) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean preWrite(String componentName) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object set(String componentName, Object value,
			CommandListener commandListener) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object set(String componentName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setUserObject(Object newVal) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate(String componentAnyCaseName, Object newVal) {
		// TODO Auto-generated method stub
		return false;
	}
	/*
	boolean excludeMethod (MethodProxy m) {
    	return uiBean.isPre(m);
    }
    */
	@Override
	public String getPatternName() {
		return StructurePatternNames.NO_PATTERN;		
	}
	@Override
	public String getPatternPath() {
		return StructurePatternNames.NO_PATTERN;		
	}
	@Override
	public Vector nonGraphicsComponentNames() {
		return nullVector;
	}
	@Override
	public Vector graphicsComponentNames() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isGraphics(String componentName) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Set<String> getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Set<String> getEdtitablePropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
