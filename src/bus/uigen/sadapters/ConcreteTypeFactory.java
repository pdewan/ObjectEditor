package bus.uigen.sadapters;

import bus.uigen.uiFrame;import bus.uigen.reflect.ClassProxy;
public interface ConcreteTypeFactory {		public ConcreteType toConcreteType(ClassProxy theClass, Object theObject, uiFrame theFrame, boolean forceConversion);	public Class getConcreteType ();	public ConcreteType createConcreteType ();
	public String getPatternName();
	public String getPatternPath();
	boolean useInSearch();
}
