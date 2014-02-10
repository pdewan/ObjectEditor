package bus.uigen.sadapters;import javax.swing.JTree;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.reflect.ClassProxy;import bus.uigen.controller.MethodInvocationManager;import bus.uigen.editors.EditorRegistry;
//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericTreeNodeToVectorStructureFactory extends  AbstractConcreteTypeFactory implements VectorStructureFactory  {
		public VectorStructure toVectorStructure(ClassProxy theClass, Object theObject, uiFrame theFrame, boolean forceConversion) {		GenericTreeNodeToVectorStructure treeStructure = new GenericTreeNodeToVectorStructure (theObject, theFrame);		if (!treeStructure.isGenericTreeNode()) return null;		else {			if (!EditorRegistry.hasWidgetClass(theClass)) 				EditorRegistry.register(theClass, theClass.treeClass());			return treeStructure;			}			}
		
	public ConcreteType toConcreteType(ClassProxy theTargetClass, Object theTargetObject, uiFrame theFrame, boolean forceConversion) {
		return toVectorStructure(theTargetClass, theTargetObject, theFrame, false);	}	public Class getConcreteType () {		return GenericTreeNodeToVectorStructure.class;
	}	public ConcreteType createConcreteType () {		return new GenericTreeNodeToVectorStructure();
	}	@Override	public String getPatternName() {		// TODO Auto-generated method stub		return util.annotations.StructurePatternNames.TREE_NODE_PATTERN;	}	@Override	public String getPatternPath() {		// TODO Auto-generated method stub		return "util.annotations.StructurePatternNames.TREE_NODE_PATTERN";	}
		
}
