package bus.uigen.controller.models;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import bus.uigen.ObjectEditor;
import bus.uigen.uiGenerator;
import bus.uigen.adapters.CommandAndStatePanelAdapter;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.widgets.VirtualLabel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AColumnEliderModel extends AColumnDisplayerAbstractModel implements Serializable {
	
	boolean expanded = false;
	public  AColumnEliderModel (ObjectAdapter theAdapter) {
		
		super(theAdapter);
		expanded = adapter.getDefaultExpanded();
	}
	public boolean preCollapse() {
		return expanded;
	}
	public void collapse() {
		collapseOrExpand(false);
		expanded = false;
	}
	public boolean preExpand() {
		return !expanded;
	}
	public void expand() {
		collapseOrExpand(true);
		expanded = true;
	}
	
		
	void collapseOrExpand(boolean expand) {
		//if (adapter.getParentAdapter() == null) return;
		//ObjectEditor.setPropertyAttribute(c, property, attribute, value)
		//boolean expand = adapter.getDefaultExpanded();
		//ObjectEditor.setPropertyAttribute(parentClass, adapter.getPropertyName(), AttributeNames.DEFAULT_EXPANDED, expand);

		CompositeAdapter rowAdapter = adapter.getFlatTableRowAncestor();
		/*
		uiContainerAdapter ancestorAdapter =  (uiContainerAdapter) adapter.getTopAdapter();
		ClassProxy ancestorClass = ancestorAdapter.getPropertyClass();
		
		
		
		String relativePath = adapter.getGenericStringPathRelativeTo(ancestorAdapter);
		//ObjectEditor.setPropertyAttribute(parentClass, adapter.getComponentName(), AttributeNames.DEFAULT_EXPANDED, expand);
		ObjectEditor.setPropertyAttribute(ancestorClass, relativePath, AttributeNames.DEFAULT_EXPANDED, expand);
		*/
		ClassProxy tableClass = adapter.setAttributeRelativeToTableClass(AttributeNames.DEFAULT_EXPANDED, expand);
		//ObjectEditor.setVisible(parentClass, adapter.getPropertyName(), false);		
		//adapter.getTopAdapter().refreshAttributes(parentClass);
		//adapter.getTopAdapter().refreshAttributes(adapter.getTopAdapter().getPropertyClass());
		adapter.getTopAdapter().refreshAttributes(tableClass);
		//boolean visible = adapter.isVisible();
		boolean expanded = adapter.getDefaultExpanded();
		
		ObjectAdapter tableAdapter = adapter.getTableAdapter();
		//CommandAndStatePanelAdapter tableWidgetAdapter = (CommandAndStatePanelAdapter) tableAdapter.getWidgetAdapter();
		
		
		
		//if (adapter.grandParentFlatRowHasFlatTableRowDescendents())
		if (CommandAndStatePanelAdapter.hasDescendentRows(tableAdapter))
			adapter.deepElideDisplayChildrenOfTableWidgetAdapter(tableAdapter);
		else
			adapter.deepElideVisibleChildrenOfPeerParentAdapters();
		/*
		if (rowAdapter != null) {
			rebuildPanelsOfAllRows(rowAdapter);
			//uiGenerator.deepElide(adapter.getTopAdapter());
			//ancestor.getParentAdapter().invalidateComponentsSetInTree();
			//ancestor.getParentAdapter().getWidgetAdapter().rebuildPanel();
			//ancestor.getWidgetAdapter().descedentUIComponentsAdded();
			
			
		}
		*/
		adapter.getUIFrame().setImplicitRefresh(false);
		
			
		//uiGenerator.deepElide(adapter.getTableAdapter());
		adapter.getUIFrame().refreshWindow();
		//adapter.getUIFrame().validate();
		//ObjectEditor.refreshAttributes(this, adapter.getParentAdapter().getPropertyClass());
		
	}
	void rebuildPanelsOfAllRows (ObjectAdapter rowAdapter) {
		CompositeAdapter  tableAdapter = rowAdapter.getParentAdapter();
		if (tableAdapter == null)
			return;
		for (int i = 0; i < tableAdapter.getChildAdapterCount(); i++) {
			ObjectAdapter nextRow = tableAdapter.getChildAdapterAt(i);
			uiGenerator.deepElide(nextRow);
			nextRow.invalidateComponentsSetInTree();
			nextRow.getWidgetAdapter().rebuildPanel();
		}
		tableAdapter.getUIFrame().validate();
		tableAdapter.getUIFrame().repaint();
	}

}
