package bus.uigen.controller.models;

import java.awt.Frame;
import java.util.Vector;

import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.myLockManager;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeEditor;
import bus.uigen.controller.MethodParameters;
import bus.uigen.controller.SelectionManager;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ACustomizeOperationsModel implements FrameModel {
	uiFrame frame;
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		frame = theFrame;
	}
	public void selected() {
		//uiObjectAdapter editedAdapter = (uiObjectAdapter) uiSelectionManager.getCurrentSelection();
		ObjectAdapter editedAdapter = ANewEditorOperationsModel.getOperandAdapter(frame);
		if (editedAdapter == null) return;
//		Object editedObject = editedAdapter.getRealObject();
//		Class editedClass = editedObject.getClass();
//		ViewInfo cd = ClassDescriptorCache.getClassDescriptor(editedClass);
		ObjectEditor.edit(ClassDescriptorCustomizer.getClassDescriptorCustomizer(editedAdapter));
	}
	public void broadcast() {
		
	}
	
	public String[] getDynamicCommands() {
		return frame.getCustomizeClassNames();
	}
	public void invokeDynamicCommand (String className) {
		if (MethodParameters.EditBeanInfo) {
			try {
				//Frame editor = uiGenerator.generateUIFrame(ClassDescriptorCache.getClassDescriptor(Class.forName(name)), (myLockManager) null);
				//uiFrame editor = uiGenerator.generateUIFrame(ClassDescriptorCache.getClassDescriptor(uiClassFinder.forName(className)), (myLockManager) null);
				//editor.setVisible(true);
				ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(uiClassFinder.forName(className));
				ClassDescriptorCustomizer customizer = new ClassDescriptorCustomizer((AClassDescriptor) cd);
				  
				ObjectEditor.edit(customizer);
			}   catch (Exception ex) {}
		}
		else {
			Frame   editor = new AttributeEditor(AttributeManager.getEnvironment().getClassAttributeManager(className));
		}
	}
	/*
	public Vector<VirtualMethod> getVirtualMethods() {
		return frame.getConstructors();
	}
	*/

}
