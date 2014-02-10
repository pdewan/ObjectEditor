package bus.uigen.sadapters;import javax.swing.JTable;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.editors.EditorRegistry;import bus.uigen.editors.TableAdapter;import bus.uigen.controller.MethodInvocationManager;
//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericTableToVectorStructureFactory extends  AbstractConcreteTypeFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theClass, Object theObject, uiFrame theFrame, boolean forceConversion) {		GenericTableToVectorStructure tableStructure = new GenericTableToVectorStructure (theObject, theFrame);		if (!tableStructure.isGenericTable()) return null;		else {		if (!EditorRegistry.hasWidgetClass(theClass)) 			EditorRegistry.register(theClass, theClass.tableClass());		return tableStructure;		}	}
		
	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame, false);	}	public Class getConcreteType () {		return GenericTableToVectorStructure.class;
	}	public ConcreteType createConcreteType () {		return new GenericTableToVectorStructure();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.TABLE_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.TABLE_PATTERN";	}
		
}
