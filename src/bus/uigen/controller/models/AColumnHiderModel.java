package bus.uigen.controller.models;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import util.trace.Tracer;
import util.trace.uigen.HidingLastChild;
import bus.uigen.ObjectEditor;
import bus.uigen.adapters.CommandAndStatePanelAdapter;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.widgets.VirtualLabel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AColumnHiderModel extends AColumnDisplayerAbstractModel {
	
	
	public  AColumnHiderModel (ObjectAdapter theAdapter) {
		
		super(theAdapter);
		
	}
	
	
	
	
	public void showAll() {
		//resetHiddenSiblingCommands();	
		/*
		for (int i = 0; i < componentNames.size(); i++) {
			//ObjectEditor.setVisible(parentClass, componentNames.get(i), true);
			//adapter.setSiblingAttributeRelativeToTopClass(componentNames.get(i), AttributeNames.VISIBLE, true);
			adapter.setSiblingAttributeRelativeToTableClass(componentNames.get(i), AttributeNames.VISIBLE, true);
		}
		*/
		for (int i = 0; i < labelNames.size(); i++) {
			//ObjectEditor.setVisible(parentClass, componentNames.get(i), true);
			//adapter.setSiblingAttributeRelativeToTopClass(componentNames.get(i), AttributeNames.VISIBLE, true);
			adapter.setSiblingAttributeRelativeToTableClass(getComponentName(labelNames.get(i)), AttributeNames.VISIBLE, true);
		}
		//adapter.getTopAdapter().refreshAttributes(parentClass);
		adapter.getTopAdapter().refreshAttributes(adapter.getTopAdapter().getPropertyClass());
		adapter.getUIFrame().setImplicitRefresh(false);
		adapter.redoVisibleChildrenOfPeerParentAdapters();
		adapter.getUIFrame().refreshWindow();
	}
	public void hide() {
		//if (adapter.getParentAdapter() == null) return;
		//ObjectEditor.setVisible(parentClass, adapter.getPropertyName(), false);
		//adapter.setAttributeRelativeToTopClass(AttributeNames.VISIBLE, false);
		if (adapter.getParentAdapter().getChildAdapterCount() == 1) {
//			Tracer.warning("Cannot hide last item of parent:" + adapter.getParentAdapter().getRealObject());
			HidingLastChild.newCase(adapter.getParentAdapter().getRealObject(), this);
			return;
		}
		ClassProxy tableClass = adapter.setAttributeRelativeToTableClass(AttributeNames.VISIBLE, false);
		//adapter.getTopAdapter().refreshAttributes(parentClass);
		//adapter.getTopAdapter().refreshAttributes(adapter.getTopAdapter().getPropertyClass());
		adapter.getTopAdapter().refreshAttributes(tableClass);
		//ObjectEditor.refreshAttributes(this, adapter.getParentAdapter().getPropertyClass());
		adapter.getUIFrame().setImplicitRefresh(false);
		ObjectAdapter tableAdapter = adapter.getTableAdapter();
		//CommandAndStatePanelAdapter tableWidgetAdapter = (CommandAndStatePanelAdapter) tableAdapter.getWidgetAdapter();
		
		
		
		//if (adapter.grandParentFlatRowHasFlatTableRowDescendents())
		if (CommandAndStatePanelAdapter.hasDescendentRows(tableAdapter))
			adapter.redoDisplayChildrenOfTableWidgetAdapter();
		else
			adapter.redoVisibleChildrenOfPeerParentAdapters();
		//adapter.redoDisplayChildrenOfTableWidgetAdapter();
		adapter.getUIFrame().refreshWindow();
		
	}

}
